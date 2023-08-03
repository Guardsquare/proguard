package proguard.optimize.inline;

import proguard.optimize.inline.lambda_locator.LambdaLocator;
import proguard.AppView;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.ClassEditor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.ConstantPoolEditor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.VariableInstruction;
import proguard.classfile.instruction.visitor.InstructionOpCodeFilter;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.ClassReferenceInitializer;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.ClassPrinter;
import proguard.classfile.visitor.MemberVisitor;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.TracedStack;
import proguard.optimize.inline.lambda_locator.Lambda;

/**
 * Recursively inline functions that make use of the lambda parameter in the arguments of the current function.
 * The first step, is finding out who actually uses our lambda parameter, we do this using the partial evaluator.
 */
public class RecursiveInliner implements AttributeVisitor, InstructionVisitor, MemberVisitor {
    private final AppView appView;
    private Clazz consumingClazz;
    private Method copiedConsumingMethod;
    private final Method originalConsumingMethod;
    private final boolean isStatic;
    private final Lambda lambda;
    private final boolean enableRecursiveMerging;
    private final PartialEvaluator partialEvaluator;
    private Clazz referencedClass;
    private Method referencedMethod;
    private int callOffset;

    /**
     * @param originalConsumingMethod The original consuming method reference, this is used to detect recursion.
     */
    public RecursiveInliner(AppView appView, Method originalConsumingMethod, Lambda lambda, boolean enableRecursiveMerging) {
        this.appView = appView;
        this.consumingClazz = null;
        this.copiedConsumingMethod = null;
        this.originalConsumingMethod = originalConsumingMethod;
        this.isStatic = (originalConsumingMethod.getAccessFlags() & AccessConstants.STATIC) != 0;
        this.lambda = lambda;
        this.enableRecursiveMerging = enableRecursiveMerging;
        this.partialEvaluator = new PartialEvaluator();
        this.referencedMethod = null;
    }

    public RecursiveInliner(AppView appView, Method originalConsumingMethod, Lambda lambda) {
        this(appView, originalConsumingMethod, lambda, false);
    }

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
        codeAttribute.accept(clazz, method, new ClassPrinter());
        partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);
        codeAttribute.instructionsAccept(clazz, method,
            new InstructionOpCodeFilter(new int[] {Instruction.OP_INVOKESTATIC, Instruction.OP_INVOKEVIRTUAL, Instruction.OP_INVOKESPECIAL},
            new InstructionVisitor() {
                @Override
                public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
                    System.out.println("At " + constantInstruction);
                    TracedStack tracedStack = partialEvaluator.getStackBefore(offset);

                    if (tracedStack == null)
                        return;

                    System.out.println("Stack size " + tracedStack.size());
                    if (tracedStack.size() <= 0)
                        return;

                    clazz.constantPoolEntryAccept(constantInstruction.constantIndex, new ConstantVisitor() {
                        @Override
                        public void visitMethodrefConstant(Clazz clazz, MethodrefConstant methodrefConstant) {
                            if (methodrefConstant.getClassName(clazz).equals("kotlin/jvm/internal/Intrinsics") &&
                                    methodrefConstant.getName(clazz).equals("checkNotNullParameter") &&
                                    methodrefConstant.getType(clazz).equals("(Ljava/lang/Object;Ljava/lang/String;)V"))
                                return;

                            String methodDescriptor = methodrefConstant.referencedMethod.getDescriptor(methodrefConstant.referencedClass);
                            int argCount = ClassUtil.internalMethodParameterCount(methodDescriptor);
                            boolean referencedMethodStatic = (methodrefConstant.referencedMethod.getAccessFlags() & AccessConstants.STATIC) != 0;
                            // We will now check if any of the arguments has a value that comes from the lambda parameters
                            for (int argIndex = 0; argIndex < argCount; argIndex++) {
                                int stackAdjustedIndex = ClassUtil.internalMethodVariableIndex(methodDescriptor, referencedMethodStatic, argIndex);
                                int traceOffset = tracedStack.getBottomActualProducerValue(tracedStack.size() - ClassUtil.internalMethodVariableIndex(methodDescriptor, referencedMethodStatic, argCount) + stackAdjustedIndex).instructionOffsetValue().instructionOffset(0);
                                referencedMethod = methodrefConstant.referencedMethod;
                                referencedClass = methodrefConstant.referencedClass;
                                callOffset = offset;
                                codeAttribute.instructionAccept(clazz, method, traceOffset, RecursiveInliner.this);
                            }
                        }
                    });
                }
        }));
    }

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}

    @Override
    public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction) {
        if (variableInstruction.canonicalOpcode() == Instruction.OP_ALOAD) {
            System.out.println("Value from " + variableInstruction + " " + variableInstruction.variableIndex);

            Instruction sourceInstruction = Util.traceParameter(partialEvaluator, codeAttribute, offset);
            System.out.println(variableInstruction + " gets it's value from " + sourceInstruction);
            if (sourceInstruction.canonicalOpcode() == Instruction.OP_ALOAD) {
                VariableInstruction variableSourceInstruction = (VariableInstruction) sourceInstruction;

                String consumingMethodDescriptor = copiedConsumingMethod.getDescriptor(consumingClazz);
                int calledLambdaRealIndex = Util.findFirstLambdaParameter(consumingMethodDescriptor);
                int sizeAdjustedLambdaIndex = ClassUtil.internalMethodVariableIndex(consumingMethodDescriptor, isStatic, calledLambdaRealIndex);

                if (variableSourceInstruction.variableIndex == sizeAdjustedLambdaIndex) {
                    throw new CannotInlineException("Cannot inline lambda into recursive function");
                }
            }
        }
    }

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
        this.consumingClazz = programClass;
        this.copiedConsumingMethod = programMethod;
        programMethod.attributesAccept(programClass, this);
    }
}
