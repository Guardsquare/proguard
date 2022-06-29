package proguard.classfile.attribute.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.attribute.InnerClassesAttribute;

/**
 * This {@link AllInnerClassesInfoVisitor} revisits each {@link InnerClassesAttribute} everytime its amount of
 * referenced classes has been modified in the meantime.
 */
public class ModifiedAllInnerClassesInfoVisitor extends AllInnerClassesInfoVisitor {

    public ModifiedAllInnerClassesInfoVisitor(InnerClassesInfoVisitor innerClassesInfoVisitor) {
        super(innerClassesInfoVisitor);
    }

    public void visitInnerClassesAttribute(Clazz clazz, InnerClassesAttribute innerClassesAttribute)
    {
        int originalClassesCount = -1;
        while (originalClassesCount != innerClassesAttribute.u2classesCount)
        {
            originalClassesCount = innerClassesAttribute.u2classesCount;
            super.visitInnerClassesAttribute(clazz, innerClassesAttribute);
        }
    }
}
