package proguard.optimize.inline;

import proguard.AppView;
import proguard.classfile.AccessConstants;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Member;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.AccessFixer;
import proguard.classfile.editor.ClassEditor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.ConstantPoolEditor;
import proguard.classfile.editor.MethodCopier;
import proguard.classfile.editor.PeepholeEditor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.InstructionFactory;
import proguard.classfile.instruction.VariableInstruction;
import proguard.classfile.instruction.visitor.AllInstructionVisitor;
import proguard.classfile.instruction.visitor.InstructionCounter;
import proguard.classfile.instruction.visitor.InstructionOpCodeFilter;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.ClassReferenceInitializer;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.util.InternalTypeEnumeration;
import proguard.classfile.visitor.ClassPrinter;
import proguard.classfile.visitor.MemberVisitor;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.TracedStack;
import proguard.optimize.inline.lambda_locator.Lambda;
import proguard.optimize.peephole.MethodInliner;

import java.util.ArrayList;
import java.util.List;

public class BaseLambdaInliner implements MemberVisitor, InstructionVisitor, ConstantVisitor {
    private final Clazz consumingClass;
    private final Method consumingMethod;
    private final AppView appView;
    private final Lambda lambda;
    private final CodeAttributeEditor codeAttributeEditor;
    private final boolean isStatic;
    private final int calledLambdaIndex;
    private final int sizeAdjustedLamdaIndex;
    private final PartialEvaluator partialEvaluator;
    private Clazz interfaceClass;
    private Clazz lambdaClass;
    private Method lambdaInvokeMethod;
    private String bridgeDescriptor;
    private Method inlinedLambdaMethod;
    private Method staticInvokeMethod;
    private InterfaceMethodrefConstant referencedInterfaceConstant;
    private final List<Integer> invokeMethodCallOffsets;

    public BaseLambdaInliner(AppView appView, Clazz consumingClass, Method consumingMethod, int calledLambdaIndex, Lambda lambda) {
        this.consumingClass = consumingClass;
        this.consumingMethod = consumingMethod;
        this.appView = appView;
        this.lambda = lambda;
        this.codeAttributeEditor = new CodeAttributeEditor();
        this.isStatic = (consumingMethod.getAccessFlags() & AccessConstants.STATIC) != 0;
        this.calledLambdaIndex = calledLambdaIndex + (isStatic ? 0 : 1);
        this.sizeAdjustedLamdaIndex = ClassUtil.internalMethodVariableIndex(consumingMethod.getDescriptor(consumingClass), true, this.calledLambdaIndex);
        this.partialEvaluator = new PartialEvaluator();
        this.invokeMethodCallOffsets = new ArrayList<>();
    }

    /**
     * Idea: Inline a lambda by just giving it a consuming method and class in which it has to inline the particular
     * lambda,  a reference to the newly created function with the inlined lambda is returned, the caller can then
     * replace the call instruction with a call to this newly created function. This way we don't need to think about
     * how it is called exactly, it can be used as a higher abstraction in other places.
     */
    public Method inline() {
        if (consumingMethod instanceof LibraryMethod)
            return null;

        ConstantInstruction c = lambda.constantInstruction();
        lambda.clazz().constantPoolEntryAccept(c.constantIndex, new LambdaImplementationVisitor((programClass, programMethod, interfaceClass, bridgeDescriptor) -> {
            this.interfaceClass = interfaceClass;
            this.lambdaClass = programClass;
            this.lambdaInvokeMethod = programMethod;
            this.bridgeDescriptor = bridgeDescriptor;

            // First we copy the method from the anonymous lambda class into the class where it is used.
            MethodCopier copier = new MethodCopier(programClass, programMethod, BaseLambdaInliner.this);
            consumingClass.accept(copier);
        }));

        return inlinedLambdaMethod;
    }

    @Override
    public void visitAnyMember(Clazz clazz, Member member) {
        ProgramMethod method = (ProgramMethod) member;
        method.u2accessFlags = (method.getAccessFlags() & ~AccessConstants.BRIDGE & ~AccessConstants.SYNTHETIC & ~AccessConstants.PRIVATE)
                | AccessConstants.STATIC;

        // Copy the invoke method
        String invokeMethodDescriptor = method.getDescriptor(consumingClass);
        DescriptorModifier descriptorModifier = new DescriptorModifier(consumingClass);
        staticInvokeMethod = descriptorModifier.modify(method,
            originalDescriptor -> {
                // The method becomes static
                String modifiedDescriptor = originalDescriptor.replace("(", "(Ljava/lang/Object;");
                // Change return type if it has an effect on the stack size
                modifiedDescriptor = modifiedDescriptor.replace(")Ljava/lang/Double;", ")D");
                modifiedDescriptor = modifiedDescriptor.replace(")Ljava/lang/Float;", ")F");
                return modifiedDescriptor.replace(")Ljava/lang/Long;", ")J");
            }
        , true);

        ProgramMethod copiedConsumingMethod = descriptorModifier.modify((ProgramMethod) consumingMethod, descriptor -> descriptor);

        // Don't inline if the lamdba is passed to another method
        try {
            copiedConsumingMethod.accept(consumingClass, new RecursiveInliner(calledLambdaIndex));
        } catch(CannotInlineException cie) {
            ClassEditor classEditor = new ClassEditor((ProgramClass) consumingClass);
            classEditor.removeMethod(copiedConsumingMethod);
            classEditor.removeMethod(staticInvokeMethod);
            return;
        }

        referencedInterfaceConstant = null;
        invokeMethodCallOffsets.clear();

        // Replace invokeinterface call to invoke method with invokestatic to staticInvokeMethod
        codeAttributeEditor.reset(getMethodLength(copiedConsumingMethod, consumingClass));
        copiedConsumingMethod.accept(consumingClass,
                new AllAttributeVisitor(
                new AllInstructionVisitor(
                new InstructionOpCodeFilter(new int[] { Instruction.OP_INVOKEINTERFACE }, this))));

        if (referencedInterfaceConstant != null) {
            // Remove casting before and after invoke method call
            // Uses same codeAttributeEditor as LambdaInvokeReplacer
            copiedConsumingMethod.accept(consumingClass, new AllAttributeVisitor(new PrePostCastRemover(invokeMethodDescriptor)));
        }

        // Remove return value's casting from staticInvokeMethod
        staticInvokeMethod.accept(consumingClass, new AllAttributeVisitor(new PeepholeEditor(codeAttributeEditor, new CastPatternRemover(codeAttributeEditor))));

        // Important for inlining, we need this so that method invocations have non-null referenced methods.
        appView.programClassPool.classesAccept(
                new ClassReferenceInitializer(appView.programClassPool, appView.libraryClassPool)
        );

        // Inlining phase 2, inline that static invoke method into the actual function that uses the lambda.
        inlineMethodInClass(consumingClass, staticInvokeMethod);

        // Remove the static invoke method once it has been inlined
        ClassEditor classEditor = new ClassEditor((ProgramClass) consumingClass);
        classEditor.removeMethod(staticInvokeMethod);

        // Remove checkNotNullParameter() call because arguments that are lambdas will be removed
        InstructionCounter removedNullCheckInstrCounter = new InstructionCounter();
        copiedConsumingMethod.accept(consumingClass, new AllAttributeVisitor(new PeepholeEditor(codeAttributeEditor, new NullCheckRemover(sizeAdjustedLamdaIndex, codeAttributeEditor, removedNullCheckInstrCounter))));

        //remove inlined lambda from arguments through the descriptor
        Method methodWithoutLambdaParameter = descriptorModifier.modify(copiedConsumingMethod, desc -> {
            List<String> list = new ArrayList<>();
            InternalTypeEnumeration internalTypeEnumeration = new InternalTypeEnumeration(desc);
            while(internalTypeEnumeration.hasMoreTypes()) {
                list.add(internalTypeEnumeration.nextType());
            }
            // We adjust the index to not take the this parameter into account because this is not visible in the
            // method descriptor.
            list.remove(calledLambdaIndex - (isStatic ? 0 : 1));

            return ClassUtil.internalMethodDescriptorFromInternalTypes(internalTypeEnumeration.returnType(), list);
        }, true);


        // Remove one of the arguments
        lambda.clazz().constantPoolEntryAccept(lambda.constantInstruction().constantIndex, new ConstantVisitor() {
            @Override
            public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant) {
                ConstantPoolEditor constantPoolEditor = new ConstantPoolEditor((ProgramClass) consumingClass);
                int lambdaInstanceFieldIndex = constantPoolEditor.addFieldrefConstant(fieldrefConstant.referencedClass, fieldrefConstant.referencedField);

                // If the argument had a null check removed then it was non-nullable, so we can replace usages with null
                // just fine because no one will ever null check on it. Even if the programmer did do a null check on it
                // the kotlin compiler will remove it.
                Instruction replacementInstruction = removedNullCheckInstrCounter.getCount() != 0 ?
                        new VariableInstruction(Instruction.OP_ACONST_NULL) :
                        new ConstantInstruction(Instruction.OP_GETSTATIC, lambdaInstanceFieldIndex);
                methodWithoutLambdaParameter.accept(consumingClass, new LocalUsageRemover(codeAttributeEditor, sizeAdjustedLamdaIndex, replacementInstruction));
            }
        });
        appView.programClassPool.classesAccept(new AccessFixer());

        // The resulting new method is: methodWithoutLambdaParameter, the user of the BaseLambdaInliner can then replace
        // calls to the old function to calls to this new function.
        inlinedLambdaMethod = methodWithoutLambdaParameter;
    }

    @Override
    public void visitConstantInstruction(Clazz consumingClass, Method consumingMethod, CodeAttribute consumingMethodCodeAttribute, int consumingMethodCallOffset, ConstantInstruction constantInstruction) {
        consumingClass.constantPoolEntryAccept(constantInstruction.constantIndex, this);
        Method invokeInterfaceMethod = referencedInterfaceConstant.referencedMethod;
        Clazz invokeInterfaceClass = referencedInterfaceConstant.referencedClass;

        // Check if this is an invokeinterface call to the lambda class invoke method
        if (invokeInterfaceClass.getName().equals(interfaceClass.getName()) &&
                invokeInterfaceMethod.getName(invokeInterfaceClass).equals(lambdaInvokeMethod.getName(lambdaClass)) &&
                invokeInterfaceMethod.getDescriptor(invokeInterfaceClass).equals(bridgeDescriptor)
        ) {
            partialEvaluator.visitCodeAttribute(consumingClass, consumingMethod, consumingMethodCodeAttribute);
            TracedStack tracedStack = partialEvaluator.getStackBefore(consumingMethodCallOffset);

            // If we have a lambda with n arguments we have to skip those n arguments and then get the instance of the lambda used for calling invokeinterface.
            int lambdaInstanceStackIndex = ClassUtil.internalMethodParameterCount(invokeInterfaceMethod.getDescriptor(invokeInterfaceClass));

            consumingMethodCodeAttribute.instructionAccept(
                consumingClass,
                consumingMethod,
                tracedStack.getTopActualProducerValue(lambdaInstanceStackIndex).instructionOffsetValue().instructionOffset(0),
                new LambdaInvokeUsageReplacer(consumingMethodCallOffset)
            );
        }
    }

    @Override
    public void visitInterfaceMethodrefConstant(Clazz clazz, InterfaceMethodrefConstant interfaceMethodrefConstant) {
        referencedInterfaceConstant = interfaceMethodrefConstant;
    }

    private class LambdaInvokeUsageReplacer implements InstructionVisitor {
        private final int possibleLambdaInvokeCallOffset;

        public LambdaInvokeUsageReplacer(int possibleLambdaInvokeCallOffset) {
            this.possibleLambdaInvokeCallOffset = possibleLambdaInvokeCallOffset;
        }

        @Override
        public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction) {
            // Uses same codeAttributeEditor as the casting remover
            Instruction tracedInstruction = SourceTracer.traceParameter(partialEvaluator, codeAttribute, offset);
            if (tracedInstruction.canonicalOpcode() == Instruction.OP_ALOAD) {
                variableInstruction = (VariableInstruction) tracedInstruction;

                // Check if the aload index is the same as the current lambda index in the arguments
                if (variableInstruction.variableIndex == sizeAdjustedLamdaIndex) {
                    // Replace call to lambda invoke method by a static call to the copied static invoke method
                    ConstantPoolEditor constantPoolEditor = new ConstantPoolEditor((ProgramClass) clazz);
                    int constantIndex = constantPoolEditor.addMethodrefConstant(clazz, staticInvokeMethod);

                    codeAttributeEditor.replaceInstruction(possibleLambdaInvokeCallOffset, new ConstantInstruction(Instruction.OP_INVOKESTATIC, constantIndex));
                    if (staticInvokeMethod.getDescriptor(consumingClass).endsWith("V")) {
                        // Lambda that returns void, so we delete the pop instruction after the invoke because it originally returned the Object type
                        codeAttributeEditor.deleteInstruction(possibleLambdaInvokeCallOffset + InstructionFactory.create(codeAttribute.code, possibleLambdaInvokeCallOffset).length(possibleLambdaInvokeCallOffset));
                    }

                    invokeMethodCallOffsets.add(possibleLambdaInvokeCallOffset);
                }
            }
        }
    }

    private class PrePostCastRemover implements AttributeVisitor{
        private final int nbrArgs;
        private final List<Integer> keepList;

        private PrePostCastRemover(String invokeMethodDescriptor) {
            this.nbrArgs = ClassUtil.internalMethodParameterCount(invokeMethodDescriptor);
            this.keepList = new ArrayList<>();

            InternalTypeEnumeration internalTypeEnumeration = new InternalTypeEnumeration(invokeMethodDescriptor);
            int i = 0;
            while(internalTypeEnumeration.hasMoreTypes()) {
                if (internalTypeEnumeration.nextType().equals("Ljava/lang/Object;")) {
                    // Argument i is object, we should keep the cast for this argument
                    keepList.add(i);
                }
                i++;
            }
        }

        @Override
        public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
            for (int invokeMethodCallOffset : invokeMethodCallOffsets) {
                int endOffset = invokeMethodCallOffset + 8 + 1;
                // If  the next instruction is pop then we are not using the return value so we don't need to remove
                // casts on the return value.
                if (InstructionFactory.create(codeAttribute.code, invokeMethodCallOffset + InstructionFactory.create(codeAttribute.code, invokeMethodCallOffset).length(invokeMethodCallOffset)).opcode == Instruction.OP_POP) {
                    endOffset = invokeMethodCallOffset;
                }

                for (int i = 0; i < nbrArgs; i++) {
                    int offset = partialEvaluator.getStackBefore(invokeMethodCallOffset).getTopActualProducerValue(nbrArgs - i - 1).instructionOffsetValue().instructionOffset(0);
                    codeAttribute.instructionAccept(clazz, method, offset, new CastRemover(codeAttributeEditor, keepList, i));
                }

                codeAttribute.instructionsAccept(consumingClass, method, invokeMethodCallOffset, endOffset,
                    new CastRemover(codeAttributeEditor)
                );
            }
            codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
        }

        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}
    }

    /**
     * @param method     A Method object from which we'll get the length.
     * @param clazz      The class in which the method is.
     * @return           The length of the method.
     */
    private int getMethodLength(Method method, Clazz clazz) {
        final int[] length = new int[1];
        method.accept(clazz, new AllAttributeVisitor(new AttributeVisitor() {
            @Override
            public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}
            @Override
            public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
                length[0] = codeAttribute.u4codeLength;
            }
        }));
        return length[0];
    }

    /**
     * Inline a specified method in the locations it is called in the current clazz.
     * @param clazz The clazz in which we want to inline the targetMethod
     * @param targetMethod The method we want to inline.
     */
    private void inlineMethodInClass(Clazz clazz, Method targetMethod) {
        clazz.methodsAccept(new AllAttributeVisitor(new MethodInliner(false, false, true, false, null) {
            @Override
            protected boolean shouldInline(Clazz clazz, Method method, CodeAttribute codeAttribute) {
                return method.equals(targetMethod);
            }
        }));
    }
}
