package proguard.optimize.inline;

import proguard.classfile.*;
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
import proguard.classfile.visitor.ClassPrinter;
import proguard.classfile.visitor.MemberVisitor;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.TracedStack;

/**
 * Recursively inline functions that make use of the lambda parameter in the arguments of the current function.
 * The first step, is finding out who actually uses our lambda parameter, we do this using the partial evaluator.
 */
public class RecursiveInliner implements AttributeVisitor, InstructionVisitor, MemberVisitor, ConstantVisitor {
    private Clazz consumingClazz;
    private Method copiedConsumingMethod;
    private final boolean isStatic;
    private final PartialEvaluator partialEvaluator;

    /**
     * @param originalConsumingMethod The original consuming method reference, this is used to detect recursion.
     */
    public RecursiveInliner(Method originalConsumingMethod) {
        this.consumingClazz = null;
        this.copiedConsumingMethod = null;
        this.isStatic = (originalConsumingMethod.getAccessFlags() & AccessConstants.STATIC) != 0;
        this.partialEvaluator = new PartialEvaluator();
    }

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
        codeAttribute.accept(clazz, method, new ClassPrinter());
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
                new InstructionVisitor() {
                    @Override
                    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
                        System.out.println("At " + constantInstruction);
                        TracedStack tracedStack = partialEvaluator.getStackBefore(offset);

                        System.out.println("Stack size " + tracedStack.size());
                        if (tracedStack.size() <= 0)
                            return;

                        clazz.constantPoolEntryAccept(constantInstruction.constantIndex, new ConstantVisitor() {
                            @Override
                            public void visitMethodrefConstant(Clazz clazz, MethodrefConstant methodrefConstant) {
                                handleCalledMethod(methodrefConstant.referencedClass, methodrefConstant.referencedMethod);
                            }

                            @Override
                            public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant) {
                                // MethodRefTraveler

                                clazz.attributesAccept(new AttributeNameFilter(Attribute.BOOTSTRAP_METHODS, new AttributeVisitor() {
                                    @Override
                                    public void visitBootstrapMethodsAttribute(Clazz clazz, BootstrapMethodsAttribute bootstrapMethodsAttribute) {
                                        bootstrapMethodsAttribute.bootstrapMethodEntryAccept(clazz, invokeDynamicConstant.u2bootstrapMethodAttributeIndex, (bootstrapClazz, bootstrapMethodInfo) -> {
                                            ProgramClass programClass = (ProgramClass) bootstrapClazz;
                                            MethodHandleConstant bootstrapMethodHandle =
                                                    (MethodHandleConstant) programClass.getConstant(bootstrapMethodInfo.u2methodHandleIndex);

                                            if (bootstrapMethodHandle.getClassName(bootstrapClazz).equals("java/lang/invoke/LambdaMetafactory"))
                                            {
                                                MethodHandleConstant methodHandleConstant = (MethodHandleConstant) ((ProgramClass) bootstrapClazz).getConstant(bootstrapMethodInfo.u2methodArguments[1]);
                                                Method referencedMethod = new RefMethodFinder(bootstrapClazz).findReferencedMethod(methodHandleConstant.u2referenceIndex);

                                                handleCalledMethod(bootstrapClazz, referencedMethod);
                                            }
                                        });
                                    }
                                }));
                            }

                            @Override
                            public void visitInterfaceMethodrefConstant(Clazz clazz, InterfaceMethodrefConstant interfaceMethodrefConstant) {
                                handleCalledMethod(interfaceMethodrefConstant.referencedClass, interfaceMethodrefConstant.referencedMethod);
                            }

                            public void handleCalledMethod(Clazz referencedClass, Method referencedMethod) {
                                if (referencedClass.getName().equals("kotlin/jvm/internal/Intrinsics") &&
                                        referencedMethod.getName(referencedClass).equals("checkNotNullParameter") &&
                                        referencedMethod.getDescriptor(referencedClass).equals("(Ljava/lang/Object;Ljava/lang/String;)V"))
                                    return;

                                String methodDescriptor = referencedMethod.getDescriptor(referencedClass);
                                int argCount = ClassUtil.internalMethodParameterCount(methodDescriptor);
                                boolean referencedMethodStatic = (referencedMethod.getAccessFlags() & AccessConstants.STATIC) != 0;
                                // We will now check if any of the arguments has a value that comes from the lambda parameters
                                for (int argIndex = 0; argIndex < argCount; argIndex++) {
                                    System.out.println("Stack before offset: " + partialEvaluator.getStackBefore(offset));
                                    constantInstruction.accept(clazz, method, codeAttribute, offset, new ClassPrinter());
                                    codeAttribute.accept(clazz, method, new ClassPrinter());
                                    int stackAdjustedIndex = ClassUtil.internalMethodVariableIndex(methodDescriptor, referencedMethodStatic, argIndex);
                                    int traceOffset = tracedStack.getBottomActualProducerValue(tracedStack.size() - ClassUtil.internalMethodVariableIndex(methodDescriptor, referencedMethodStatic, argCount) + stackAdjustedIndex).instructionOffsetValue().instructionOffset(0);
                                    codeAttribute.instructionAccept(clazz, method, traceOffset, RecursiveInliner.this);
                                }
                            }
                        });
                    }
                }
            )
        );
    }

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}

    @Override
    public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction) {
        if (variableInstruction.canonicalOpcode() == Instruction.OP_ALOAD) {
            System.out.println("Value from " + variableInstruction + " " + variableInstruction.variableIndex);

            Instruction sourceInstruction = Util.traceParameter(partialEvaluator, codeAttribute, offset);
            if (sourceInstruction == null) {
                throw new CannotInlineException("Argument has multiple source locations, cannot inline lambda!");
            }

            System.out.println(variableInstruction + " gets it's value from " + sourceInstruction);
            if (sourceInstruction.canonicalOpcode() == Instruction.OP_ALOAD) {
                VariableInstruction variableSourceInstruction = (VariableInstruction) sourceInstruction;

                String consumingMethodDescriptor = copiedConsumingMethod.getDescriptor(consumingClazz);
                int calledLambdaRealIndex = Util.findFirstLambdaParameter(consumingMethodDescriptor);
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
        programMethod.attributesAccept(programClass, this);
    }
}
