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
package proguard.classfile.attribute;

import proguard.classfile.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.ClassVisitor;

/**
 * This {@link Attribute} represents a signature attribute.
 *
 * @author Eric Lafortune
 */
public class SignatureAttribute extends Attribute
{
    public int u2signatureIndex;

    /**
     * An extra field containing all the classes referenced in the
     * signature string. This field is filled out by the {@link
     * proguard.classfile.util.ClassReferenceInitializer ClassReferenceInitializer}.
     * The size of the array is the number of classes in the signature.
     * Primitive types and arrays of primitive types are ignored.
     * Unknown classes are represented as null values.
     */
    public Clazz[] referencedClasses;


    /**
     * Creates an uninitialized SignatureAttribute.
     */
    public SignatureAttribute()
    {
    }


    /**
     * Creates an initialized SignatureAttribute.
     */
    public SignatureAttribute(int u2attributeNameIndex,
                              int u2signatureIndex)
    {
        super(u2attributeNameIndex);

        this.u2signatureIndex = u2signatureIndex;
    }


    /**
     * Returns the signature.
     */
    public String getSignature(Clazz clazz)
    {
        return clazz.getString(u2signatureIndex);
    }


    /**
     * Lets the Clazz objects referenced in the signature string accept the
     * given visitor.
     */
    public void referencedClassesAccept(ClassVisitor classVisitor)
    {
        if (referencedClasses != null)
        {
            for (int index = 0; index < referencedClasses.length; index++)
            {
                if (referencedClasses[index] != null)
                {
                    referencedClasses[index].accept(classVisitor);
                }
            }
        }
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitSignatureAttribute(clazz, this);
    }

    public void accept(Clazz clazz, Field field, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitSignatureAttribute(clazz, field, this);
    }

    public void accept(Clazz clazz, Method method, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitSignatureAttribute(clazz, method, this);
    }
 }
