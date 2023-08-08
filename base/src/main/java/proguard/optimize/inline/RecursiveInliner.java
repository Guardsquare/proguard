package proguard.optimize.inline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.AccessConstants;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.BootstrapMethodsAttribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeNameFilter;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.constant.InvokeDynamicConstant;
import proguard.classfile.constant.MethodHandleConstant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.VariableInstruction;
import proguard.classfile.instruction.visitor.InstructionOpCodeFilter;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.MemberVisitor;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.TracedStack;

import static proguard.optimize.inline.FirstLambdaParameterFinder.findFirstLambdaParameter;

/**
 * Recursively inline functions that make use of the lambda parameter in the arguments of the current function.
 * The first step, is finding out who actually uses our lambda parameter, we do this using the partial evaluator.
 */
public class RecursiveInliner implements AttributeVisitor, InstructionVisitor, MemberVisitor, ConstantVisitor {
    private Clazz consumingClazz;
    private Method copiedConsumingMethod;
    private boolean isStatic;
    private final PartialEvaluator partialEvaluator;
    private static final Logger logger = LogManager.getLogger(RecursiveInliner.class);


    public RecursiveInliner() {
        this.consumingClazz = null;
        this.copiedConsumingMethod = null;
        this.partialEvaluator = new PartialEvaluator();
    }

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
        partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);
        codeAttribute.instructionsAccept(clazz, method,
            new InstructionOpCodeFilter(
                new int[] {
                    Instruction.OP_INVOKESTATIC,
                    Instruction.OP_INVOKEVIRTUAL,
                    Instruction.OP_INVOKESPECIAL,
                    Instruction.OP_INVOKEDYNAMIC,
                    Instruction.OP_INVOKEINTERFACE
                },
                new CalledMethodHandler(this)
            )
        );
    }

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}

    /**
     * This visit function is used to visit the source instructions of arguments to methods called within the current
     * method, we check if the source instruction is a lambda, if it is we will stop trying to inline this lambda.
     */
    @Override
    public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction) {
        if (variableInstruction.canonicalOpcode() == Instruction.OP_ALOAD) {
            logger.debug("Value from " + variableInstruction + " " + variableInstruction.variableIndex);
            Instruction sourceInstruction = SourceTracer.traceParameter(partialEvaluator, codeAttribute, offset);
            if (sourceInstruction == null) {
                throw new CannotInlineException("Argument has multiple source locations, cannot inline lambda!");
            }

            logger.debug(variableInstruction + " gets it's value from " + sourceInstruction);
            if (sourceInstruction.canonicalOpcode() == Instruction.OP_ALOAD) {
                VariableInstruction variableSourceInstruction = (VariableInstruction) sourceInstruction;

                String consumingMethodDescriptor = copiedConsumingMethod.getDescriptor(consumingClazz);
                int calledLambdaRealIndex = findFirstLambdaParameter(consumingMethodDescriptor);
                int sizeAdjustedLambdaIndex = ClassUtil.internalMethodVariableIndex(consumingMethodDescriptor, isStatic, calledLambdaRealIndex);

                if (variableSourceInstruction.variableIndex == sizeAdjustedLambdaIndex) {
                    throw new CannotInlineException("Cannot inline lambdas into functions that call other functions that consume this lambda!");
                }
            }
        }
    }

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
        this.consumingClazz = programClass;
        this.copiedConsumingMethod = programMethod;
        this.isStatic = (copiedConsumingMethod.getAccessFlags() & AccessConstants.STATIC) != 0;
        programMethod.attributesAccept(programClass, this);
    }

    private class CalledMethodHandler implements InstructionVisitor, ConstantVisitor, AttributeVisitor {
        private final InstructionVisitor sourceInstructionVisitor;
        private TracedStack tracedStack;
        private Clazz clazz;
        private Method method;
        private CodeAttribute codeAttribute;

        public CalledMethodHandler(InstructionVisitor sourceInstructionVisitor) {
            this.sourceInstructionVisitor = sourceInstructionVisitor;
        }

        @Override
        public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int callOffset, ConstantInstruction constantInstruction) {
            this.clazz = clazz;
            this.method = method;
            this.codeAttribute = codeAttribute;
            this.tracedStack = partialEvaluator.getStackBefore(callOffset);

            if (tracedStack.size() <= 0)
                return;

            clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
        }

        @Override
        public void visitMethodrefConstant(Clazz clazz, MethodrefConstant methodrefConstant) {
            handleCalledMethod(methodrefConstant.referencedClass, methodrefConstant.referencedMethod);
        }

        @Override
        public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant) {
            clazz.attributesAccept(
                new AttributeNameFilter(Attribute.BOOTSTRAP_METHODS,
                new IndyLambdaImplVisitor(invokeDynamicConstant, new MemberVisitor() {
                    @Override
                    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
                        handleCalledMethod(programClass, programMethod);
                    }
                }))
            );
        }

        @Override
        public void visitInterfaceMethodrefConstant(Clazz clazz, InterfaceMethodrefConstant interfaceMethodrefConstant) {
            handleCalledMethod(interfaceMethodrefConstant.referencedClass, interfaceMethodrefConstant.referencedMethod);
        }

        public void handleCalledMethod(Clazz referencedClass, Method referencedMethod) {
            // There can be null checks on the lambda parameter, this doesn't stop us from inlining because we will
            // remove these later.
            if (referencedClass.getName().equals("kotlin/jvm/internal/Intrinsics") &&
                    referencedMethod.getName(referencedClass).equals("checkNotNullParameter") &&
                    referencedMethod.getDescriptor(referencedClass).equals("(Ljava/lang/Object;Ljava/lang/String;)V"))
                return;

            String methodDescriptor = referencedMethod.getDescriptor(referencedClass);
            int argCount = ClassUtil.internalMethodParameterCount(methodDescriptor);
            boolean referencedMethodStatic = (referencedMethod.getAccessFlags() & AccessConstants.STATIC) != 0;
            // We will now check if any of the arguments has a value that comes from the lambda parameters
            for (int argIndex = 0; argIndex < argCount; argIndex++) {
                int sizeAdjustedIndex = ClassUtil.internalMethodVariableIndex(methodDescriptor, referencedMethodStatic, argIndex);
                int stackEntryIndex = tracedStack.size() - ClassUtil.internalMethodVariableIndex(methodDescriptor, referencedMethodStatic, argCount) + sizeAdjustedIndex;
                int traceOffset = tracedStack.getBottomActualProducerValue(stackEntryIndex).instructionOffsetValue().instructionOffset(0);
                codeAttribute.instructionAccept(clazz, method, traceOffset, sourceInstructionVisitor);
            }
        }
    }

    /**
     * A simple class that visits the static invocation method that implements an invokedynamic lambda. The
     * implementation method is visited by the implMethodVisitor.
     */
    private static class IndyLambdaImplVisitor implements AttributeVisitor {
        private final InvokeDynamicConstant invokeDynamicConstant;
        private final MemberVisitor implMethodVisitor;

        public IndyLambdaImplVisitor(InvokeDynamicConstant invokeDynamicConstant, MemberVisitor implMethodVisitor) {
            this.invokeDynamicConstant = invokeDynamicConstant;
            this.implMethodVisitor = implMethodVisitor;
        }

        @Override
        public void visitBootstrapMethodsAttribute(Clazz clazz, BootstrapMethodsAttribute bootstrapMethodsAttribute) {
            bootstrapMethodsAttribute.bootstrapMethodEntryAccept(clazz, invokeDynamicConstant.u2bootstrapMethodAttributeIndex, (bootstrapClazz, bootstrapMethodInfo) -> {
                ProgramClass programClass = (ProgramClass) bootstrapClazz;
                MethodHandleConstant bootstrapMethodHandle =
                        (MethodHandleConstant) programClass.getConstant(bootstrapMethodInfo.u2methodHandleIndex);

                if (bootstrapMethodHandle.getClassName(bootstrapClazz).equals("java/lang/invoke/LambdaMetafactory")) {
                    MethodHandleConstant methodHandleConstant = (MethodHandleConstant) ((ProgramClass) bootstrapClazz).getConstant(bootstrapMethodInfo.u2methodArguments[1]);
                    Method referencedMethod = new RefMethodFinder(bootstrapClazz).findReferencedMethod(methodHandleConstant.u2referenceIndex);

                    referencedMethod.accept(bootstrapClazz, implMethodVisitor);
                }
            });
        }
    }
}
