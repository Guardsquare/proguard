package proguard.optimize.inline.lambdalocator;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.ConstantInstruction;

public final class Lambda {
    private final Clazz clazz;
    private final Method method;
    private final CodeAttribute codeAttribute;
    private final int offset;
    private final ConstantInstruction constantInstruction;

    public Lambda(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction) {
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
    public String toString() {
        return constantInstruction.toString();
    }
}
