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
package proguard.classfile;

import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.*;

/**
 * Representation of a method from a program class.
 *
 * @author Eric Lafortune
 */
public class ProgramMethod extends ProgramMember implements Method
{
    private static final Attribute[] EMPTY_ATTRIBUTES = new Attribute[0];


    /**
     * An extra field containing all the classes referenced in the
     * descriptor string. This field is filled out by the {@link
     * proguard.classfile.util.ClassReferenceInitializer ClassReferenceInitializer}.
     * The size of the array is the number of classes in the descriptor.
     * Primitive types and arrays of primitive types are ignored.
     * Unknown classes are represented as null values.
     */
    public Clazz[] referencedClasses;


    /**
     * Creates an uninitialized ProgramMethod.
     */
    public ProgramMethod()
    {
    }


    /**
     * Creates an initialized ProgramMethod without attributes.
     */
    public ProgramMethod(int     u2accessFlags,
                         int     u2nameIndex,
                         int     u2descriptorIndex,
                         Clazz[] referencedClasses)
    {
        this(u2accessFlags,
             u2nameIndex,
             u2descriptorIndex,
             referencedClasses,
             0,
             null);
    }


    /**
     * Creates an initialized ProgramMethod without attributes.
     */
    public ProgramMethod(int     u2accessFlags,
                         int     u2nameIndex,
                         int     u2descriptorIndex,
                         Clazz[] referencedClasses,
                         int     processingFlags,
                         Object  processingInfo)
    {
        this(u2accessFlags,
             u2nameIndex,
             u2descriptorIndex,
             0,
             EMPTY_ATTRIBUTES,
             referencedClasses,
             processingFlags,
             processingInfo);
    }


    /**
     * Creates an initialized ProgramMethod.
     */
    public ProgramMethod(int         u2accessFlags,
                         int         u2nameIndex,
                         int         u2descriptorIndex,
                         int         u2attributesCount,
                         Attribute[] attributes,
                         Clazz[]     referencedClasses)
    {
        this(u2accessFlags,
             u2nameIndex,
             u2descriptorIndex,
             u2attributesCount,
             attributes,
             referencedClasses,
             0,
             null);
    }


    /**
     * Creates an initialized ProgramMethod.
     */
    public ProgramMethod(int         u2accessFlags,
                         int         u2nameIndex,
                         int         u2descriptorIndex,
                         int         u2attributesCount,
                         Attribute[] attributes,
                         Clazz[]     referencedClasses,
                         int         processingFlags,
                         Object      processingInfo)
    {
        super(u2accessFlags,
              u2nameIndex,
              u2descriptorIndex,
              u2attributesCount,
              attributes,
              processingFlags,
              processingInfo);

        this.referencedClasses = referencedClasses;
    }


    // Implementations for ProgramMember.

    public void accept(ProgramClass programClass, MemberVisitor memberVisitor)
    {
        memberVisitor.visitProgramMethod(programClass, this);
    }


    public void attributesAccept(ProgramClass programClass, AttributeVisitor attributeVisitor)
    {
        for (int index = 0; index < u2attributesCount; index++)
        {
            attributes[index].accept(programClass, this, attributeVisitor);
        }
    }


    // Implementations for Member.

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
}
