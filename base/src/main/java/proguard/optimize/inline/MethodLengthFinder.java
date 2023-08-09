package proguard.optimize.inline;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;

/**
 * A simple utility class that gets the length of a method, if the method has no code attribute it will return -1 as the
 * length.
 */
public class MethodLengthFinder {
    private static int codeLength;

    /**
     * @param method     A Method object from which we'll get the length.
     * @param clazz      The class in which the method is.
     * @return           The length of the method.
     */
    public static int getMethodCodeLength(Clazz clazz, Method method) {
        codeLength = -1; // If a method has no codeAttribute we don't want to return the previous method length value.
        method.accept(clazz, new AllAttributeVisitor(new AttributeVisitor() {
            @Override
            public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}
            @Override
            public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
                codeLength = codeAttribute.u4codeLength;
            }
        }));
        return codeLength;
    }
}
