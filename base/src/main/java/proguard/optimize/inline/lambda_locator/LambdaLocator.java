package proguard.optimize.inline.lambda_locator;

import proguard.backport.LambdaExpression;
import proguard.backport.LambdaExpressionCollector;
//import proguard.classfile.*;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ClassPool;
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

import java.util.*;

public class LambdaLocator implements InstructionVisitor, ConstantVisitor, MemberVisitor {
    private final Map<Clazz, Map<Method, Set<Lambda>>> classLambdas = new HashMap<>();
    private final List<Lambda> staticLambdas = new ArrayList<>();
    private final Map<Integer, Lambda> staticLambdaMap = new HashMap<>();
    private final Set<Clazz> lambdaClasses = new HashSet<>();
    private final ClassPool classPool;

    public LambdaLocator(ClassPool classPool, String classNameFilter) {
        this.classPool = classPool;

        classPool.classesAccept(classNameFilter, clazz -> {
            // Find classes that inherit from kotlin.jvm.internal.Lambda
            clazz.superClassConstantAccept(this);
        });

        // Find static lambdas
        classPool.classesAccept(classNameFilter, clazz -> {

            HashMap<Integer, LambdaExpression> h = new HashMap<>();
            LambdaExpressionCollector lec = new LambdaExpressionCollector(h);
            lec.visitProgramClass((ProgramClass) clazz);

            clazz.methodsAccept(this);
        });
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
            clazz.constantPoolEntryAccept(constantInstruction.constantIndex, new StaticLambdaFinder(method, codeAttribute, constantInstruction, offset));
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

    public Map<Clazz, Map<Method, Set<Lambda>>> getLambdasByClass() {
        return classLambdas;
    }

    public List<Lambda> getStaticLambdas() {
        return staticLambdas;
    }

    public Map<Integer, Lambda> getStaticLambdaMap() {
        return staticLambdaMap;
    }

    private class StaticLambdaFinder implements ConstantVisitor {
        private final ConstantInstruction constantInstruction;
        private final Method method;
        private final int offset;
        private final CodeAttribute codeAttribute;

        public StaticLambdaFinder(Method method, CodeAttribute codeAttribute, ConstantInstruction constantInstruction, int offset) {
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
                    System.out.println("Found a lambda invocation " + constantInstruction);

                    classLambdas.putIfAbsent(clazz, new HashMap<>());
                    classLambdas.get(clazz).putIfAbsent(method, new HashSet<>());
                    classLambdas.get(clazz).get(method).add(new Lambda(clazz, method, codeAttribute, offset, constantInstruction));

                    Lambda lambda = new Lambda(clazz, method, codeAttribute, offset, constantInstruction);
                    staticLambdas.add(lambda);
                    staticLambdaMap.put(lambda.constantInstruction().constantIndex, lambda);
                }
            });
        }
    }
}
