package proguard.optimize.lambdainline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.AccessConstants;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryMethod;
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
import proguard.classfile.visitor.MemberVisitor;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.TracedStack;
import proguard.optimize.lambdainline.lambdalocator.Lambda;
import proguard.optimize.peephole.MethodInliner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A class that given a method that consumes a lambda is able to create a new method which has this lambda inlined. The
 * caller will still need to replace the call instruction and handle removing arguments provided to the original call
 * instruction if this class manages to inline the lambda.
 */
public abstract class BaseLambdaInliner {
    private static final Logger logger = LogManager.getLogger();
    private final Clazz consumingClass;
    private final Method consumingMethod;
    private final ClassPool programClassPool;
    private final ClassPool libraryClassPool;
    private final Lambda lambda;
    private final CodeAttributeEditor codeAttributeEditor;
    private final boolean isStatic;
    private final int calledLambdaIndex;
    private final int sizeAdjustedLambdaIndex;
    private final PartialEvaluator partialEvaluator;
    private Clazz interfaceClass;
    private Clazz lambdaClass;
    private Method lambdaInvokeMethod;
    private String bridgeDescriptor;
    private ProgramMethod inlinedLambdaMethod;
    private Method staticInvokeMethod;
    private InterfaceMethodrefConstant referencedInterfaceConstant;
    private final List<Integer> invokeMethodCallOffsets;

    public BaseLambdaInliner(ClassPool programClassPool, ClassPool libraryClassPool, Clazz consumingClass, Method consumingMethod, int calledLambdaIndex, Lambda lambda) {
        this.consumingClass = consumingClass;
        this.consumingMethod = consumingMethod;
        this.programClassPool = programClassPool;
        this.libraryClassPool = libraryClassPool;
        this.lambda = lambda;
        this.codeAttributeEditor = new CodeAttributeEditor();
        this.isStatic = (consumingMethod.getAccessFlags() & AccessConstants.STATIC) != 0;
        this.calledLambdaIndex = calledLambdaIndex + (isStatic ? 0 : 1);
        this.sizeAdjustedLambdaIndex = ClassUtil.internalMethodVariableIndex(consumingMethod.getDescriptor(consumingClass), true, this.calledLambdaIndex);
        this.partialEvaluator = new PartialEvaluator();
        this.invokeMethodCallOffsets = new ArrayList<>();
    }

    /**
     * Idea: Inline a lambda by just giving it a consuming method and class in which it has to inline the particular
     * lambda,  a reference to the newly created function with the inlined lambda is returned, the caller can then
     * replace the call instruction with a call to this newly created function. This way we don't need to think about
     * how it is called exactly, it can be used as a higher abstraction in other places such as in the recursive
     * inliner.
     * <p>
     * The inlining uses the following steps:
     * <ul>
     * <li> First we will try to find the invoke method that implements this Kotlin lambda.
     * <li> We check if we should try inline this based on the condition defined in shouldInline. This could be for
     * example, only inline lambdas of a certain length.
     * <li> We will copy this invoke method into the consuming class.
     * <li> Then we will make it static and change the descriptor to have a fake <code>this</code> parameter, we need
     * this parameter because otherwise <code>aload_0</code> would load the wrong thing, it would load the first actual
     * argument instead of <code>this</code> because it is now static.
     * <li> We will then make a copy of the consuming method because we will modify it to have this lambda inlined and
     * the argument removed.
     * <li> Then we check if the consuming method uses the lambda to call another method that also consumes the lambda
     * in this slimmed down implementation we just abort the inlining process in that case.
     * <li> Then we will try find all calls to the previously mentioned invoke method and replace them with calls to our
     * new static invoke method.
     * <li> Then we do cast removal, we remove the casts before and after the invoke call, these were previously needed
     * because a bridge method was used, and now we use the non type-erased invoke method. We also remove a cast at the
     * end of this invoke method if it is present. This cast boxes primitive types.
     * <li> After doing that we can use the <code>MethodInliner</code> to actually inline the implementation itself.
     * <li> We will then delete this static invoke method, we no longer need it.
     * <li> The consuming method has the lambda inlined now but still has the lambda in its arguments, we will remove
     * this argument now.
     * <li> We first remove the null check that was present for this argument, we no longer need it. Then we will edit
     * the descriptor of the consuming method to get rid of the lambda argument.
     * <li> With the argument gone the load and store instructions use the wrong indices, we will shift the indices that
     * are higher than the lambda index down so that it is correct again. The <code>aload_x</code> instructions with
     * x = the lambda
     * index will be replaced with <code>aconst_null</code> or if this lambda was nullable, with a <code>getstatic</code>
     * to the lambda class instance. The latter is not ideal but nullable lambdas are uncommon. Future optimisation is
     * possible here. Because of safety concerns replacing with <code>aconst_null</code> has been disabled currently.
     * The usage of the <code>getstatic</code> to obtain a reference to this field might not be allowed here, so we use
     * the <code>AccessFixer</code> to make it possible. <li> The lambda is now fully inlined into this copy of the
     * consuming method and is returned to the caller.
     * </ul>
     *
     *
     * @return Returns a new method which has the lambda inlined in it, the lambda argument is also removed. If this
     *         class was unable to inline the lambda into the method it will return null.
     */
    public ProgramMethod inline() {
        if (consumingMethod instanceof LibraryMethod)
            return null;

        ConstantInstruction c = lambda.getstaticInstruction();
        lambda.clazz().constantPoolEntryAccept(c.constantIndex, new LambdaImplementationVisitor((programClass, programMethod, interfaceClass, bridgeDescriptor) -> {
            this.interfaceClass = interfaceClass;
            this.lambdaClass = programClass;
            this.lambdaInvokeMethod = programMethod;
            this.bridgeDescriptor = bridgeDescriptor;

            if (!shouldInline(consumingClass, consumingMethod, programClass, lambdaInvokeMethod)) {
                return;
            }

            // First we copy the method from the anonymous lambda class into the class where it is used. We will then
            // call the inner class InvokeMethodInliner to inline this copied invoke method.
            MethodCopier copier = new MethodCopier(programClass, programMethod, new InvokeMethodInliner());
            consumingClass.accept(copier);
        }));

        return inlinedLambdaMethod;
    }

    /**
     * A method that allows to conditionally inline lambdas based on information about the consuming method and the
     * lambda implementation method.
     * @param consumingClass The consuming class, this is the class that contains the consuming method.
     * @param consumingMethod The consuming method (method taking the lambda as an argument).
     * @param lambdaClass The class containing the implementation method of the lambda.
     * @param lambdaImplMethod The implementation method of the lambda.
     * @return Returns true if it should attempt to inline this lambda, if it's false it will stop and this lambda will
     *         not be inlined.
     */
    protected abstract boolean shouldInline(Clazz consumingClass, Method consumingMethod, Clazz lambdaClass, Method lambdaImplMethod);

    /**
     * A private inner class that hides all the visitors form the main BaseLambdaInliner API. It takes the copied invoke
     * method in the visitProgramMethod method and it inlines that invoke method into the consuming method.
     */
    private class InvokeMethodInliner implements MemberVisitor, InstructionVisitor, ConstantVisitor {
        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod method) {
            method.u2accessFlags = (method.getAccessFlags() & ~AccessConstants.BRIDGE & ~AccessConstants.SYNTHETIC & ~AccessConstants.PRIVATE)
                    | AccessConstants.STATIC;

            // Copy the invoke method
            String invokeMethodDescriptor = method.getDescriptor(consumingClass);
            DescriptorModifier descriptorModifier = new DescriptorModifier(consumingClass);
            staticInvokeMethod = descriptorModifier.modify(method, originalDescriptor -> {
                // The method becomes static
                String modifiedDescriptor = originalDescriptor.replace("(", "(Ljava/lang/Object;");
                // Change return type if it has an effect on the stack size
                // We will later remove the cast at the end of the invoke method that boxes primitive types, so we
                // change the descriptor here accordingly.
                modifiedDescriptor = modifiedDescriptor.replace(")Ljava/lang/Double;", ")D");
                modifiedDescriptor = modifiedDescriptor.replace(")Ljava/lang/Float;", ")F");
                return modifiedDescriptor.replace(")Ljava/lang/Long;", ")J");
            }, true);

            ProgramMethod copiedConsumingMethod = descriptorModifier.modify((ProgramMethod) consumingMethod, descriptor -> descriptor);

            // Don't inline if the lamdba is passed to another method
            try {
                copiedConsumingMethod.accept(consumingClass, new RecursiveInliner(calledLambdaIndex));
            } catch (CannotInlineException cie) {
                logger.debug("Aborting inlining this lambda into this method. Reason:");
                logger.debug(cie.getMessage());
                ClassEditor classEditor = new ClassEditor((ProgramClass) consumingClass);
                classEditor.removeMethod(copiedConsumingMethod);
                classEditor.removeMethod(staticInvokeMethod);
                return;
            }

            referencedInterfaceConstant = null;
            invokeMethodCallOffsets.clear();

            // Replace invokeinterface call to invoke method with invokestatic to staticInvokeMethod
            Optional<Integer> consumingMethodLength = MethodLengthFinder.getMethodCodeLength(consumingClass, copiedConsumingMethod);
            assert consumingMethodLength.isPresent();
            codeAttributeEditor.reset(consumingMethodLength.get());
            copiedConsumingMethod.accept(consumingClass,
                new AllAttributeVisitor(
                new AllInstructionVisitor(
                new InstructionOpCodeFilter(new int[] { Instruction.OP_INVOKEINTERFACE }, this)
            )));

            if (referencedInterfaceConstant != null) {
                // Remove casting before and after invoke method call
                // Uses same codeAttributeEditor as LambdaInvokeReplacer
                copiedConsumingMethod.accept(consumingClass, new AllAttributeVisitor(new PrePostCastRemover(invokeMethodDescriptor)));
            }

            // Remove return value's casting from staticInvokeMethod.
            staticInvokeMethod.accept(consumingClass, new AllAttributeVisitor(new PeepholeEditor(codeAttributeEditor, new CastPatternRemover(codeAttributeEditor))));

            // Important for inlining, we need this so that method invocations have non-null referenced methods.
            programClassPool.classesAccept(
                    new ClassReferenceInitializer(programClassPool, libraryClassPool)
            );

            // Inlining phase 2, inline that static invoke method into the actual function that uses the lambda.
            inlineMethodInClass(consumingClass, staticInvokeMethod);

            // Remove the static invoke method once it has been inlined.
            ClassEditor classEditor = new ClassEditor((ProgramClass) consumingClass);
            classEditor.removeMethod(staticInvokeMethod);

            // Remove checkNotNullParameter() call because arguments that are lambdas will be removed.
            InstructionCounter removedNullCheckInstrCounter = new InstructionCounter();
            copiedConsumingMethod.accept(consumingClass, new AllAttributeVisitor(new PeepholeEditor(codeAttributeEditor, new NullCheckRemover(sizeAdjustedLambdaIndex, codeAttributeEditor, removedNullCheckInstrCounter))));

            // Remove inlined lambda from arguments through the descriptor.
            ProgramMethod methodWithoutLambdaParameter = descriptorModifier.modify(copiedConsumingMethod, desc -> {
                List<String> list = new ArrayList<>();
                InternalTypeEnumeration internalTypeEnumeration = new InternalTypeEnumeration(desc);
                while (internalTypeEnumeration.hasMoreTypes()) {
                    list.add(internalTypeEnumeration.nextType());
                }
                // We adjust the index to not take the "this" parameter into account because this is not visible in the
                // method descriptor.
                list.remove(calledLambdaIndex - (isStatic ? 0 : 1));

                return ClassUtil.internalMethodDescriptorFromInternalTypes(internalTypeEnumeration.returnType(), list);
            }, true);


            // Remove one of the arguments
            lambda.clazz().constantPoolEntryAccept(lambda.getstaticInstruction().constantIndex, new ConstantVisitor() {
                @Override
                public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant) {
                    ConstantPoolEditor constantPoolEditor = new ConstantPoolEditor((ProgramClass) consumingClass);
                    int lambdaInstanceFieldIndex = constantPoolEditor.addFieldrefConstant(fieldrefConstant.referencedClass, fieldrefConstant.referencedField);
                    Instruction replacementInstruction = new ConstantInstruction(Instruction.OP_GETSTATIC, lambdaInstanceFieldIndex);
                    methodWithoutLambdaParameter.accept(consumingClass, new LocalUsageRemover(codeAttributeEditor, sizeAdjustedLambdaIndex, replacementInstruction));
                }
            });
            programClassPool.classesAccept(new AccessFixer());

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
                if (variableInstruction.variableIndex == sizeAdjustedLambdaIndex) {
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

    /**
     * A class that removes casts before and after the invoke call. The casts before are for  the arguments the casts
     * after are for the return value.
     */
    private class PrePostCastRemover implements AttributeVisitor{
        private final int nbrArgs;
        private final List<Integer> keepList;

        private PrePostCastRemover(String invokeMethodDescriptor) {
            this.nbrArgs = ClassUtil.internalMethodParameterCount(invokeMethodDescriptor);
            this.keepList = new ArrayList<>();

            InternalTypeEnumeration internalTypeEnumeration = new InternalTypeEnumeration(invokeMethodDescriptor);
            int i = 0;
            while (internalTypeEnumeration.hasMoreTypes()) {
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
                // If  the next instruction is pop then we are not using the return value so we don't need to remove
                // casts on the return value.
                if (InstructionFactory.create(codeAttribute.code, invokeMethodCallOffset + InstructionFactory.create(codeAttribute.code, invokeMethodCallOffset).length(invokeMethodCallOffset)).opcode != Instruction.OP_POP) {
                    int endOffset = invokeMethodCallOffset + 8 + 1;
                    codeAttribute.instructionsAccept(consumingClass, method, invokeMethodCallOffset, endOffset,
                            new CastRemover(codeAttributeEditor)
                    );
                }

                // Remove casts on the arguments, because arguments can have things like calculations in them, we use
                // the partial evaluator, but because we already ran it on this code attribute in a previous operation
                // it doesn't really cost us much at all.
                for (int i = 0; i < nbrArgs; i++) {
                    int offset = partialEvaluator.getStackBefore(invokeMethodCallOffset).getTopActualProducerValue(nbrArgs - i - 1).instructionOffsetValue().instructionOffset(0);
                    codeAttribute.instructionAccept(clazz, method, offset, new CastRemover(codeAttributeEditor, keepList, i));
                }
            }
            codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
        }

        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}
    }
}
