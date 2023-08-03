package proguard.optimize.inline;

import proguard.optimize.inline.lambda_locator.LambdaLocator;
import proguard.AppView;
import proguard.classfile.AccessConstants;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.instruction.visitor.AllInstructionVisitor;
import proguard.classfile.instruction.visitor.InstructionOpCodeFilter;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.ClassPrinter;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.TracedStack;
import proguard.optimize.inline.lambda_locator.Lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LambdaUsageFinder implements InstructionVisitor, AttributeVisitor, ConstantVisitor {
    private final Lambda targetLambda;
    private PartialEvaluator partialEvaluator;
    private final Map<Integer, Lambda> lambdaMap;
    private final AppView appView;
    private final LambdaUsageHandler lambdaUsageHandler;
    public MethodrefConstant methodrefConstant;
    public FieldrefConstant referencedFieldConstant;
    private final boolean inlineFromFields;
    private final boolean inlineFromMethods;
    private final int[] typedReturnInstructions = new int[] {
        Instruction.OP_IRETURN,
        Instruction.OP_LRETURN,
        Instruction.OP_FRETURN,
        Instruction.OP_DRETURN,
        Instruction.OP_ARETURN
    };

    public LambdaUsageFinder(Lambda targetLambda, Map<Integer, Lambda> lambdaMap, AppView appView, boolean inlineFromFields, boolean inlineFromMethods, LambdaUsageHandler lambdaUsageHandler) {
        this.targetLambda = targetLambda;
        this.partialEvaluator = new PartialEvaluator();
        this.lambdaMap = lambdaMap;
        this.appView = appView;
        this.lambdaUsageHandler = lambdaUsageHandler;
        this.inlineFromFields = inlineFromFields;
        this.inlineFromMethods = inlineFromMethods;
    }

    public LambdaUsageFinder(Lambda targetLambda, Map<Integer, Lambda> lambdaMap, AppView appView, LambdaUsageHandler lambdaUsageHandler) {
        this(targetLambda, lambdaMap, appView, false, false, lambdaUsageHandler);
    }

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
        partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);
        codeAttribute.instructionsAccept(clazz, method,
                new InstructionOpCodeFilter(
                        new int[] {
                            Instruction.OP_INVOKESTATIC,
                            Instruction.OP_INVOKEVIRTUAL,
                            Instruction.OP_IRETURN,
                            Instruction.OP_LRETURN,
                            Instruction.OP_FRETURN,
                            Instruction.OP_DRETURN,
                            Instruction.OP_ARETURN
                        },
                        this
                )
        );
    }

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}

    @Override
    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
        // We deleted instructions while iterating, the loop however cannot read the new codeAttribute.u4codeLength value so we must stop it manually!
        if (offset >= codeAttribute.u4codeLength) {
            return;
        }
        findUsage(clazz, method, codeAttribute, constantInstruction, offset, this::isTargetLambda);
    }

    @Override
    public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction) {}

    private void findUsage(Clazz clazz, Method method, CodeAttribute codeAttribute, ConstantInstruction consumingMethodCallInstruction, int offset, Function<InstructionAtOffset, Boolean> condition) {
        System.out.println("--------Start----------");
        System.out.println(consumingMethodCallInstruction);

        clazz.constantPoolEntryAccept(consumingMethodCallInstruction.constantIndex, this);

        partialEvaluator = new PartialEvaluator();
        partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);
        TracedStack tracedStack = partialEvaluator.getStackBefore(offset);
        System.out.println(tracedStack);

        System.out.println(methodrefConstant.referencedMethod.getDescriptor(methodrefConstant.referencedClass));
        String methodDescriptor = methodrefConstant.referencedMethod.getDescriptor(methodrefConstant.referencedClass);
        int argCount = ClassUtil.internalMethodParameterCount(methodDescriptor);
        for (int argIndex = 0; argIndex < argCount; argIndex++) {
            int stackAdjustedIndex = ClassUtil.internalMethodVariableIndex(methodDescriptor, true, argIndex);
            int traceOffset = tracedStack.getBottomActualProducerValue(tracedStack.size() - ClassUtil.internalMethodVariableIndex(methodDescriptor, true, argCount) + stackAdjustedIndex).instructionOffsetValue().instructionOffset(0);
            List<InstructionAtOffset> trace = Util.traceParameterOffset(partialEvaluator, codeAttribute, traceOffset);
            List<InstructionAtOffset> leafNodes = new ArrayList<>();
            Util.traceParameterTree(partialEvaluator, codeAttribute, traceOffset, leafNodes);
            boolean match = false;
            for (InstructionAtOffset tracedInstructionAtOffset : leafNodes) {
                System.out.println("Argument " + argIndex + " source " + tracedInstructionAtOffset);
                if (condition.apply(tracedInstructionAtOffset)) {
                    codeAttribute.accept(clazz, method, new ClassPrinter());
                    System.out.println("Lambda " + targetLambda + " consumed by " + methodrefConstant.referencedMethod.getName(methodrefConstant.referencedClass));
                    match = true;
                    break;
                }
            }

            if (match) {
                lambdaUsageHandler.handle(
                    targetLambda,
                    methodrefConstant.referencedClass,
                    methodrefConstant.referencedMethod,
                    offset,
                    clazz,
                    method,
                    codeAttribute,
                    trace,
                    leafNodes.stream().filter(it -> it.instruction().opcode == Instruction.OP_GETSTATIC).map(it -> {
                        ConstantInstruction getStaticInstruction = (ConstantInstruction) it.instruction();
                        return lambdaMap.get(getStaticInstruction.constantIndex);
                    }).collect(Collectors.toList())
                );
            }
        }
        System.out.println("---------End-----------");
    }

    private boolean isTargetLambda(InstructionAtOffset instructionAtOffset) {
        if (instructionAtOffset.instruction().opcode == Instruction.OP_GETSTATIC) {
            ConstantInstruction constantInstruction = (ConstantInstruction) instructionAtOffset.instruction();
            Lambda lambda = lambdaMap.get(constantInstruction.constantIndex);
            return lambda != null && lambda.equals(targetLambda);
        }
        return false;
    }

    @Override
    public void visitMethodrefConstant(Clazz clazz, MethodrefConstant methodrefConstant) {
        this.methodrefConstant = methodrefConstant;
    }

    @Override
    public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant) {
        this.referencedFieldConstant = fieldrefConstant;
    }

    public interface LambdaUsageHandler {
        void handle(Lambda lambda, Clazz consumingClazz, Method consumingMethod, int consumingCallOffset, Clazz consumingCallClass, Method consumingCallMethod, CodeAttribute consumingCallCodeAttribute, List<InstructionAtOffset> sourceTrace, List<Lambda> possibleLambdas);
    }
}
