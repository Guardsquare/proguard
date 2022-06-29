package proguard.classfile.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.visitor.ConstantVisitor;

public class MethodReferenceFinder implements ConstantVisitor
{
    private final Method referencedMethod;
    private boolean methodIsReferenced = false;

    public MethodReferenceFinder(Method referencedMethod)
    {
        this.referencedMethod = referencedMethod;
    }

    public void visitAnyConstant(Clazz clazz, Constant constant) {}

    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
    {
        if (anyMethodrefConstant.referencedMethod != null
                && anyMethodrefConstant.referencedMethod.equals(referencedMethod))
        {
            this.methodIsReferenced = true;
        }
    }

    public boolean methodReferenceFound()
    {
        return this.methodIsReferenced;
    }
}
