package proguard.optimize.inline.lambda_locator;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.ConstantInstruction;

import java.util.Objects;

public final class Lambda {
    private final Clazz clazz;
    private final Method method;
    private final CodeAttribute codeAttribute;
    private final int offset;
    private final ConstantInstruction constantInstruction;

    Lambda(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
        this.clazz = clazz;
        this.method = method;
        this.codeAttribute = codeAttribute;
        this.offset = offset;
        this.constantInstruction = constantInstruction;
    }

    public Clazz clazz() {
        return clazz;
    }

    public Method method() {
        return method;
    }

    public CodeAttribute codeAttribute() {
        return codeAttribute;
    }

    public int offset() {
        return offset;
    }

    public ConstantInstruction constantInstruction() {
        return constantInstruction;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Lambda that = (Lambda) obj;
        return Objects.equals(this.clazz, that.clazz) &&
                Objects.equals(this.method, that.method) &&
                Objects.equals(this.codeAttribute, that.codeAttribute) &&
                this.offset == that.offset &&
                Objects.equals(this.constantInstruction, that.constantInstruction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, method, codeAttribute, offset, constantInstruction);
    }

    @Override
    public String toString() {
        return constantInstruction.toString();
    }
}