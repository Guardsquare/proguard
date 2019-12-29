/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.classfile.attribute.annotation;

import proguard.classfile.*;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.util.SimpleVisitorAccepter;

/**
 * This abstract class represents an element value that is attached to an
 * annotation or an annotation default. Specific types of element values are
 * subclassed from it.
 *
 * @author Eric Lafortune
 */
public abstract class ElementValue extends SimpleVisitorAccepter
{
    public static final char TAG_STRING_CONSTANT = 's';
    public static final char TAG_ENUM_CONSTANT   = 'e';
    public static final char TAG_CLASS           = 'c';
    public static final char TAG_ANNOTATION      = '@';
    public static final char TAG_ARRAY           = '[';


    /**
     * An extra field for the optional element name. It is used in element value
     * pairs of annotations. Otherwise, it is 0.
     */
    public int u2elementNameIndex;

    /**
     * An extra field pointing to the referenced <code>Clazz</code>
     * object, if applicable. This field is typically filled out by the
     * <code>{@link proguard.classfile.util.ClassReferenceInitializer}</code>.
     */
    public Clazz referencedClass;

    /**
     * An extra field pointing to the referenced <code>Method</code>
     * object, if applicable. This field is typically filled out by the
     * <code>{@link proguard.classfile.util.ClassReferenceInitializer}</code>.
     */
    public Method referencedMethod;

    /**
     * Creates an uninitialized ElementValue.
     */
    protected ElementValue()
    {
    }


    /**
     * Creates an initialized ElementValue.
     */
    protected ElementValue(int u2elementNameIndex)
    {
        this.u2elementNameIndex = u2elementNameIndex;
    }


    /**
     * Returns the element name.
     */
    public String getMethodName(Clazz clazz)
    {
        return clazz.getString(u2elementNameIndex);
    }


    // Abstract methods to be implemented by extensions.

    /**
     * Returns the tag of this element value.
     */
    public abstract char getTag();


    /**
     * Accepts the given visitor.
     */
    public abstract void accept(Clazz clazz, Annotation annotation, ElementValueVisitor elementValueVisitor);



    /**
     * Applies the given visitor to the referenced method.
     */
    public void referencedMethodAccept(MemberVisitor memberVisitor)
    {
        if (referencedMethod != null)
        {
            referencedMethod.accept(referencedClass, memberVisitor);
        }
    }
}
