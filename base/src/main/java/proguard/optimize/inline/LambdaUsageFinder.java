package proguard.optimize.inline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.instruction.visitor.InstructionOpCodeFilter;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.ClassUtil;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.TracedStack;
import proguard.optimize.inline.lambdalocator.Lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class will try to find method calls that consume the lambda, these are then passed to a lambdaUsageHandler which
 * can then decide to inline the lambda into the method that was found to be using the lambda as an argument.
 */
public class LambdaUsageFinder implements InstructionVisitor, AttributeVisitor, ConstantVisitor {
    private final Lambda targetLambda;
    private PartialEvaluator partialEvaluator;
    private final Map<Integer, Lambda> lambdaMap;
    private final LambdaInliner.LambdaUsageHandler lambdaUsageHandler;
    public MethodrefConstant methodrefConstant;
    public FieldrefConstant referencedFieldConstant;
    private final IterativeInstructionVisitor iterativeInstructionVisitor;
    private static final Logger logger = LogManager.getLogger(LambdaUsageFinder.class);


    public LambdaUsageFinder(Lambda targetLambda, Map<Integer, Lambda> lambdaMap, LambdaInliner.LambdaUsageHandler lambdaUsageHandler) {
        this.targetLambda = targetLambda;
        this.partialEvaluator = new PartialEvaluator();
        this.lambdaMap = lambdaMap;
        this.lambdaUsageHandler = lambdaUsageHandler;
        this.iterativeInstructionVisitor = new IterativeInstructionVisitor();
    }

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
        partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);
        iterativeInstructionVisitor.instructionsAccept(
            clazz,
            method,
            codeAttribute,
            new InstructionOpCodeFilter(
                new int[] {
                    Instruction.OP_INVOKESTATIC,
                    Instruction.OP_INVOKEVIRTUAL
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
        clazz.constantPoolEntryAccept(consumingMethodCallInstruction.constantIndex, this);

        //String methodDescriptor = methodrefConstant.referencedMethod.getDescriptor(methodrefConstant.referencedClass);
        String methodDescriptor = methodrefConstant.getType(clazz);

        int argCount = ClassUtil.internalMethodParameterCount(methodDescriptor);
        if (!methodDescriptor.contains("Lkotlin/jvm/functions/Function") && !methodDescriptor.contains("Ljava/lang/Object"))
            return;

        if (methodrefConstant.referencedMethod == null)
            logger.debug(methodrefConstant.getClassName(clazz) + "#" + methodrefConstant.getName(clazz));

        logger.debug(methodrefConstant.referencedMethod.getDescriptor(methodrefConstant.referencedClass));

        logger.debug("--------Start----------");
        logger.debug(consumingMethodCallInstruction);

        partialEvaluator = new PartialEvaluator();
        partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);
        TracedStack tracedStack = partialEvaluator.getStackBefore(offset);
        logger.debug(tracedStack);

        for (int argIndex = 0; argIndex < argCount; argIndex++) {
            int sizeAdjustedIndex = ClassUtil.internalMethodVariableIndex(methodDescriptor, true, argIndex);
            int stackEntryIndex = tracedStack.size() - ClassUtil.internalMethodVariableIndex(methodDescriptor, true, argCount) + sizeAdjustedIndex;
            int traceOffset = tracedStack.getBottomActualProducerValue(stackEntryIndex).instructionOffsetValue().instructionOffset(0);
            List<InstructionAtOffset> trace = SourceTracer.traceParameterOffset(partialEvaluator, codeAttribute, traceOffset);
            List<InstructionAtOffset> leafNodes = new ArrayList<>();
            SourceTracer.traceParameterTree(partialEvaluator, codeAttribute, traceOffset, leafNodes);
            boolean match = false;
            for (InstructionAtOffset tracedInstructionAtOffset : leafNodes) {
                logger.debug("Argument " + argIndex + " source " + tracedInstructionAtOffset);
                if (condition.apply(tracedInstructionAtOffset)) {
                    logger.debug("Lambda " + targetLambda + " consumed by " + methodrefConstant.referencedMethod.getName(methodrefConstant.referencedClass));
                    match = true;
                    break;
                }
            }

            if (match && lambdaUsageHandler.handle(
                targetLambda,
                methodrefConstant.referencedClass,
                methodrefConstant.referencedMethod,
                argIndex,
                offset,
                clazz,
                method,
                codeAttribute,
                trace,
                leafNodes.stream().filter(it -> it.instruction().opcode == Instruction.OP_GETSTATIC).map(it -> {
                    ConstantInstruction getStaticInstruction = (ConstantInstruction) it.instruction();
                    return lambdaMap.get(getStaticInstruction.constantIndex);
                }).collect(Collectors.toList())
            )) {
                // We can't continue the loop because we already changed the code, the offset of the instruction we
                // are currently operating on might have changed resulting in strange behaviour.
                iterativeInstructionVisitor.setCodeChanged(true);
                break;
            }
        }
        logger.debug("---------End-----------");
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
}
