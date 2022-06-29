package proguard.classfile.attribute.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.InnerClassesInfo;
import proguard.classfile.constant.visitor.ConstantVisitor;

public class InnerClassInfoClassConstantVisitor implements InnerClassesInfoVisitor {

    private final ConstantVisitor innerClassConstantVisitor;
    private final ConstantVisitor outerClassConstantVisitor;

    public InnerClassInfoClassConstantVisitor(ConstantVisitor innerClassConstantVisitor, ConstantVisitor outerClassConstantVisitor)
    {
        this.innerClassConstantVisitor = innerClassConstantVisitor;
        this.outerClassConstantVisitor = outerClassConstantVisitor;
    }

    @Override
    public void visitInnerClassesInfo(Clazz clazz, InnerClassesInfo innerClassesInfo) {
        if (this.innerClassConstantVisitor != null)
        {
            innerClassesInfo.innerClassConstantAccept(clazz, this.innerClassConstantVisitor);
        }
        if (this.outerClassConstantVisitor != null)
        {
            innerClassesInfo.outerClassConstantAccept(clazz, this.outerClassConstantVisitor);
        }
    }
}
