/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.optimize.lambdainline;

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
import java.util.List;

/**
 * Recursively inline functions that make use of the lambda parameter in the arguments of the current function.
 * The first step, is finding out who actually uses our lambda parameter, we do this using the partial evaluator.
 * <p>
 * If we see a lambda is used by a method being called from within the consuming method we will currently abort the
 * inlining process.
 */
public class RecursiveInliner implements AttributeVisitor, InstructionVisitor, MemberVisitor, ConstantVisitor {
    private Clazz consumingClazz;
    private Method copiedConsumingMethod;
    private final PartialEvaluator partialEvaluator;
    private final int lambdaConsumingMethodArgIndex;
    private static final Logger logger = LogManager.getLogger(RecursiveInliner.class);

    public RecursiveInliner(int lambdaConsumingMethodArgIndex) {
        this.consumingClazz = null;
        this.copiedConsumingMethod = null;
        this.partialEvaluator = new PartialEvaluator();
        this.lambdaConsumingMethodArgIndex = lambdaConsumingMethodArgIndex;
    }

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

    /**
     * Given a code attribute, look at all the call instructions and see if they call a method that uses the lambda we
     * are currently attempting to inline.
     */
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

            List<Instruction> sourceInstructions = SourceTracer.traceParameterSources(partialEvaluator, codeAttribute, offset);
            for (Instruction sourceInstruction : sourceInstructions) {
                logger.debug(variableInstruction + " gets it's value from " + sourceInstruction);
                if (sourceInstruction.canonicalOpcode() == Instruction.OP_ALOAD) {
                    VariableInstruction variableSourceInstruction = (VariableInstruction) sourceInstruction;
                    /*
                     * If the source instruction was an aload_x instruction we will compare the x with the index of the
                     * lambda that we are currently inlining. Because arguments can sometimes take up 2 slots on the stack
                     * we have to adjust this index accordingly.
                     */
                    String consumingMethodDescriptor = copiedConsumingMethod.getDescriptor(consumingClazz);
                    // We use true for isStatic here because lambdaConsumingMethodArgIndex already keeps in mind if the method was static or not
                    int sizeAdjustedLambdaIndex = ClassUtil.internalMethodVariableIndex(consumingMethodDescriptor, true, lambdaConsumingMethodArgIndex);

                    if (variableSourceInstruction.variableIndex == sizeAdjustedLambdaIndex) {
                        throw new CannotInlineException("Cannot inline lambdas into functions that call other functions that consume this lambda!");
                    }
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

    /**
     * This class visits call instructions and visits the source instructions of each argument of each call using the
     * sourceInstructionVisitor. The sourceInstructionVisitor can then check if the argument is in fact the lambda we
     * are currently inlining.
     */
    private class CalledMethodHandler implements InstructionVisitor, ConstantVisitor, AttributeVisitor {
        private final InstructionVisitor sourceInstructionVisitor;
        private TracedStack tracedStack;
        private Clazz clazz;
        private Method method;
        private CodeAttribute codeAttribute;

        /**
         * @param sourceInstructionVisitor This is the visitor that will visit the source instructions for the arguments
         *                                 of method calls. The source instructions are the instructions that originally
         *                                 produced the value used in the argument. In the context of recursive inlining
         *                                 this could be something like aload_1 which loads the first argument in a
         *                                 virtual function, we also know which index  the lambda argument is at and
         *                                 that allows us to see if this method makes use of the lambda argument of the
         *                                 consuming method or not.
         */
        public CalledMethodHandler(InstructionVisitor sourceInstructionVisitor) {
            this.sourceInstructionVisitor = sourceInstructionVisitor;
        }

        /**
         * This method will visit all the call instructions, it will visit the constantIndex using a constant visitor
         * to see which method is referenced depending on if it's an invokeinterface/invokedynamic/invokestatic/...
         * call.
         */
        @Override
        public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int callOffset, ConstantInstruction constantInstruction) {
            this.clazz = clazz;
            this.method = method;
            this.codeAttribute = codeAttribute;
            this.tracedStack = partialEvaluator.getStackBefore(callOffset);

            if (tracedStack.size() <= 0)
                return;

            // Handle the referenced method for all call instructions by visiting the constantIndex.
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

        /**
         * This is a helper method that handles the case where a referencedMethod is called from the consuming method.
         * @param referencedClass The class in which the method that is called from within the consuming method resides.
         * @param referencedMethod The method that is called from within the consuming method.
         */
        private void handleCalledMethod(Clazz referencedClass, Method referencedMethod) {
            /*
             * Null checks can be on the lambda parameter, these are also function calls but they shouldn't stop us from
             * inlining because we will remove these later.
             */
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
