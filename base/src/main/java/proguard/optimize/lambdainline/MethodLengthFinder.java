package proguard.optimize.lambdainline;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;

import java.util.Optional;

/**
 * A simple utility class that can be used to easily obtain the length of a method.
 */
public class MethodLengthFinder {
    private static Optional<Integer> codeLength;

    /**
     * @param method     A Method object from which we'll get the length.
     * @param clazz      The class in which the method is.
     * @return           The length of the method, will return an empty optional if there is no code attribute.
     */
    public static Optional<Integer> getMethodCodeLength(Clazz clazz, Method method) {
        // If a method has no codeAttribute we don't want to return the previous method length value.
        codeLength = Optional.empty();
        method.accept(clazz, new AllAttributeVisitor(new AttributeVisitor() {
            @Override
            public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}
            @Override
            public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute) {
                codeLength = Optional.of(codeAttribute.u4codeLength);
            }
        }));
        return codeLength;
    }
}
