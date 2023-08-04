package proguard.optimize.inline;

import proguard.AppView;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.ClassEditor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.ConstantPoolEditor;
import proguard.classfile.editor.MethodCopier;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.InstructionFactory;
import proguard.classfile.instruction.VariableInstruction;
import proguard.classfile.instruction.visitor.AllInstructionVisitor;
import proguard.classfile.instruction.visitor.InstructionOpCodeFilter;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.ClassReferenceInitializer;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.util.InternalTypeEnumeration;
import proguard.classfile.visitor.MemberVisitor;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.TracedStack;
import proguard.optimize.inline.lambda_locator.Lambda;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class BaseLambdaInliner implements MemberVisitor, InstructionVisitor, ConstantVisitor, AttributeVisitor {
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

    public BaseLambdaInliner(AppView appView, Clazz consumingClass, Method consumingMethod, Lambda lambda) {
        this.consumingClass = consumingClass;
        this.consumingMethod = consumingMethod;
        this.appView = appView;
        this.lambda = lambda;
        this.codeAttributeEditor = new CodeAttributeEditor();
        this.isStatic = (consumingMethod.getAccessFlags() & AccessConstants.STATIC) != 0;
        this.calledLambdaIndex = Util.findFirstLambdaParameter(consumingMethod.getDescriptor(consumingClass), isStatic);
        this.sizeAdjustedLamdaIndex = ClassUtil.internalMethodVariableIndex(consumingMethod.getDescriptor(consumingClass), true, calledLambdaIndex);
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
            copiedConsumingMethod.accept(consumingClass, new RecursiveInliner(appView, consumingMethod, lambda));
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

        //  Remove return value's casting from staticInvokeMethod
        staticInvokeMethod.accept(consumingClass, new AllAttributeVisitor(this));

        if (referencedInterfaceConstant != null) {
            // Remove casting before and after invoke method call
            // Uses same codeAttributeEditor as LambdaInvokeReplacer
            copiedConsumingMethod.accept(consumingClass, new AllAttributeVisitor(new PrePostCastRemover(invokeMethodDescriptor)));
        }

        // Important for inlining, we need this so that method invocations have non-null referenced methods.
        appView.programClassPool.classesAccept(
                new ClassReferenceInitializer(appView.programClassPool, appView.libraryClassPool)
        );

        // Inlining phase 2, inline that static invoke method into the actual function that uses the lambda.
        Util.inlineMethodInClass(consumingClass, staticInvokeMethod);

        // Remove the static invoke method once it has been inlined
        ClassEditor classEditor = new ClassEditor((ProgramClass) consumingClass);
        classEditor.removeMethod(staticInvokeMethod);

        // Remove checkNotNullParameter() call because arguments that are lambdas will be removed
        copiedConsumingMethod.accept(consumingClass, new AllAttributeVisitor(new AllInstructionVisitor(new NullCheckRemover(sizeAdjustedLamdaIndex))));

        //remove inlined lambda from arguments through the descriptor
        Method methodWithoutLambdaParameter = descriptorModifier.modify(copiedConsumingMethod, desc -> {
            List<String> list = new ArrayList<>();
            InternalTypeEnumeration internalTypeEnumeration = new InternalTypeEnumeration(desc);
            while(internalTypeEnumeration.hasMoreTypes()) {
                list.add(internalTypeEnumeration.nextType());
            }
            list.remove(calledLambdaIndex - (isStatic ? 0 : 1));

            return ClassUtil.internalMethodDescriptorFromInternalTypes(internalTypeEnumeration.returnType(), list);
        }, true);


        // Remove one of the arguments
        methodWithoutLambdaParameter.accept(consumingClass, new LocalUsageRemover(codeAttributeEditor, sizeAdjustedLamdaIndex));

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
            Instruction tracedInstruction = Util.traceParameter(partialEvaluator, codeAttribute, offset);
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

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
        int len = codeAttribute.u4codeLength;
        CodeAttributeEditor codeAttributeEditorStaticInvoke = new CodeAttributeEditor();
        codeAttributeEditorStaticInvoke.reset(codeAttribute.u4codeLength);
        codeAttribute.instructionsAccept(clazz, method, len - 4, len, new CastRemover(codeAttributeEditorStaticInvoke));
        codeAttributeEditorStaticInvoke.visitCodeAttribute(clazz, method, codeAttribute);
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
                int startOffset = max((invokeMethodCallOffset -(6 * nbrArgs)), 0);
                int endOffset = invokeMethodCallOffset + 8 + 1;
                if (InstructionFactory.create(codeAttribute.code, invokeMethodCallOffset + InstructionFactory.create(codeAttribute.code, invokeMethodCallOffset).length(invokeMethodCallOffset)).opcode == Instruction.OP_POP) {
                    endOffset = invokeMethodCallOffset;
                }

                codeAttribute.instructionsAccept(consumingClass, method, startOffset, endOffset,
                        new CastRemover(codeAttributeEditor, keepList)
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
}
