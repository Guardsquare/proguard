package proguard.optimize.inline.lambdalocator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.backport.LambdaExpression;
import proguard.backport.LambdaExpressionCollector;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.AllInstructionVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.MemberVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class that in a program locates the starting point of a potential Kotlin lambda usage. The starting point means
 * that it looks for <code>getstatic</code> instructions that get a reference to an INSTANCE field of a lambda class. A
 * lambda class is a class that implements the kotlin/jvm/internal/Lambda interface.
 */
public class LambdaLocator implements InstructionVisitor, ConstantVisitor, MemberVisitor {
    private final List<Lambda> kotlinLambdas = new ArrayList<>();
    private final Map<Integer, Lambda> kotlinLambdaMap = new HashMap<>();
    private final Set<Clazz> lambdaClasses = new HashSet<>();
    private final ClassPool classPool;
    private static final Logger logger = LogManager.getLogger(LambdaLocator.class);
    public LambdaLocator(ClassPool classPool, String classNameFilter) {
        this.classPool = classPool;

        classPool.classesAccept(classNameFilter, clazz -> {
            // Find classes that inherit from kotlin.jvm.internal.Lambda
            clazz.superClassConstantAccept(this);
        });

        // Find Kotlin lambdas
        classPool.classesAccept(classNameFilter, clazz -> {

            HashMap<Integer, LambdaExpression> h = new HashMap<>();
            LambdaExpressionCollector lec = new LambdaExpressionCollector(h);
            lec.visitProgramClass((ProgramClass) clazz);

            clazz.methodsAccept(this);
        });

        logger.info("Number of lambdas found :                " + kotlinLambdas.size());
    }

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
        programMethod.accept(programClass, new AllAttributeVisitor(new AllInstructionVisitor(this)));
    }

    @Override
    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}

    @Override
    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
        if (constantInstruction.opcode == Instruction.OP_GETSTATIC) {
            clazz.constantPoolEntryAccept(constantInstruction.constantIndex, new KotlinLambdaFinder(method, codeAttribute, constantInstruction, offset));
        }
    }

    @Override
    public void visitClassConstant(Clazz clazz, ClassConstant classConstant) {
        clazz.constantPoolEntryAccept(classConstant.u2nameIndex, this);
    }

    @Override
    public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant) {
        if (utf8Constant.getString().equals("kotlin/jvm/internal/Lambda")) {
            lambdaClasses.add(clazz);
        }
    }

    public List<Lambda> getKotlinLambdas() {
        return kotlinLambdas;
    }

    public Map<Integer, Lambda> getKotlinLambdaMap() {
        return kotlinLambdaMap;
    }

    private class KotlinLambdaFinder implements ConstantVisitor {
        private final ConstantInstruction constantInstruction;
        private final Method method;
        private final int offset;
        private final CodeAttribute codeAttribute;

        public KotlinLambdaFinder(Method method, CodeAttribute codeAttribute, ConstantInstruction constantInstruction, int offset) {
            this.method = method;
            this.codeAttribute = codeAttribute;
            this.constantInstruction = constantInstruction;
            this.offset = offset;
        }

        @Override
        public void visitFieldrefConstant(Clazz clazz, FieldrefConstant fieldrefConstant) {
            clazz.constantPoolEntryAccept(fieldrefConstant.u2classIndex, this);
        }

        @Override
        public void visitClassConstant(Clazz clazz, ClassConstant classConstant) {
            clazz.constantPoolEntryAccept(classConstant.u2nameIndex, this);
        }

        @Override
        public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant) {
            classPool.classAccept(utf8Constant.getString(), referencedClazz -> {
                if (lambdaClasses.contains(referencedClazz)) {
                    logger.debug("Found a lambda invocation " + constantInstruction);

                    Lambda lambda = new Lambda(clazz, method, codeAttribute, offset, constantInstruction);
                    kotlinLambdas.add(lambda);
                    kotlinLambdaMap.put(lambda.constantInstruction().constantIndex, lambda);
                }
            });
        }
    }
}
