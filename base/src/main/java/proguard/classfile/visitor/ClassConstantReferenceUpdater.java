package proguard.classfile.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.visitor.ConstantVisitor;

public class ClassConstantReferenceUpdater implements ConstantVisitor
{
    private final Clazz originalClass;
    private final Clazz replacingClass;
    public ClassConstantReferenceUpdater(Clazz originalClass, Clazz replacingClass)
    {
        this.originalClass  = originalClass;
        this.replacingClass = replacingClass;
    }

    @Override
    public void visitAnyConstant(Clazz clazz, Constant constant) {}

    @Override
    public void visitClassConstant(Clazz clazz, ClassConstant classConstant) {
        if (classConstant.referencedClass == originalClass)
        {
            classConstant.referencedClass = replacingClass;
        }
    }
}
