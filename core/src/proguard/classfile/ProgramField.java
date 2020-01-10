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
 * This {@link Field} represents a field in a {@link ProgramClass}.
 *
 * @author Eric Lafortune
 */
public class ProgramField extends ProgramMember implements Field
{
    private static final Attribute[] EMPTY_ATTRIBUTES = new Attribute[0];


    /**
     * An extra field pointing to the Clazz object referenced in the
     * descriptor string. This field is filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer ClassReferenceInitializer}</code>.
     * References to primitive types are ignored.
     */
    public Clazz referencedClass;


    /**
     * Creates an uninitialized ProgramField.
     */
    public ProgramField()
    {
    }


    /**
     * Creates an initialized ProgramField without attributes.
     */
    public ProgramField(int   u2accessFlags,
                        int   u2nameIndex,
                        int   u2descriptorIndex,
                        Clazz referencedClass)
    {
        this(u2accessFlags,
             u2nameIndex,
             u2descriptorIndex,
             0,
             EMPTY_ATTRIBUTES,
             referencedClass,
             0,
             null);
    }


    /**
     * Creates an initialized ProgramField without attributes.
     */
    public ProgramField(int    u2accessFlags,
                        int    u2nameIndex,
                        int    u2descriptorIndex,
                        Clazz  referencedClass,
                        int    processingFlags,
                        Object processingInfo)
    {
        this(u2accessFlags,
             u2nameIndex,
             u2descriptorIndex,
             0,
             EMPTY_ATTRIBUTES,
             referencedClass,
             processingFlags,
             processingInfo);
    }


    /**
     * Creates an initialized ProgramField.
     */
    public ProgramField(int         u2accessFlags,
                        int         u2nameIndex,
                        int         u2descriptorIndex,
                        int         u2attributesCount,
                        Attribute[] attributes,
                        Clazz       referencedClass)
    {
        this(u2accessFlags,
             u2nameIndex,
             u2descriptorIndex,
             u2attributesCount,
             attributes,
             referencedClass,
             0,
             null);
    }


    /**
     * Creates an initialized ProgramField.
     */
    public ProgramField(int         u2accessFlags,
                        int         u2nameIndex,
                        int         u2descriptorIndex,
                        int         u2attributesCount,
                        Attribute[] attributes,
                        Clazz       referencedClass,
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

        this.referencedClass = referencedClass;
    }


    // Implementations for ProgramMember.

    public void accept(ProgramClass programClass, MemberVisitor memberVisitor)
    {
        memberVisitor.visitProgramField(programClass, this);
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
        if (referencedClass != null)
        {
            referencedClass.accept(classVisitor);
        }
    }
}
