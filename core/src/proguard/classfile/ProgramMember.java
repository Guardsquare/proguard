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
import proguard.classfile.visitor.MemberVisitor;
import proguard.util.SimpleProcessable;

/**
 * Base representation of a field or method from a {@link ProgramClass}.
 *
 * @author Eric Lafortune
 */
public abstract class ProgramMember
extends               SimpleProcessable
implements            Member
{
    public int         u2accessFlags;
    public int         u2nameIndex;
    public int         u2descriptorIndex;
    public int         u2attributesCount;
    public Attribute[] attributes;

    /**
     * Creates an uninitialized ProgramMember.
     */
    protected ProgramMember()
    {
    }


    /**
     * Creates an initialized ProgramMember.
     */
    protected ProgramMember(int         u2accessFlags,
                            int         u2nameIndex,
                            int         u2descriptorIndex,
                            int         u2attributesCount,
                            Attribute[] attributes)
    {
        this(u2accessFlags,
             u2nameIndex,
             u2descriptorIndex,
             u2attributesCount,
             attributes,
             0,
             null);
    }


    /**
     * Creates an initialized ProgramMember.
     */
    protected ProgramMember(int         u2accessFlags,
                            int         u2nameIndex,
                            int         u2descriptorIndex,
                            int         u2attributesCount,
                            Attribute[] attributes,
                            int         processingFlags,
                            Object      processingInfo)
    {
        super(processingFlags, processingInfo);

        this.u2accessFlags     = u2accessFlags;
        this.u2nameIndex       = u2nameIndex;
        this.u2descriptorIndex = u2descriptorIndex;
        this.u2attributesCount = u2attributesCount;
        this.attributes        = attributes;
    }


    /**
     * Returns the (first) attribute with the given name.
     */
    private Attribute getAttribute(Clazz clazz, String name)
    {
        for (int index = 0; index < u2attributesCount; index++)
        {
            Attribute attribute = attributes[index];
            if (attribute.getAttributeName(clazz).equals(name))
            {
                return attribute;
            }
        }

        return null;
    }


    /**
     * Accepts the given member info visitor.
     */
    public abstract void accept(ProgramClass  programClass,
                                MemberVisitor memberVisitor);



    /**
     * Lets the given attribute info visitor visit all the attributes of
     * this member info.
     */
    public abstract void attributesAccept(ProgramClass     programClass,
                                          AttributeVisitor attributeVisitor);


    // Implementations for Member.

    public int getAccessFlags()
    {
        return u2accessFlags;
    }

    public String getName(Clazz clazz)
    {
        return clazz.getString(u2nameIndex);
    }

    public String getDescriptor(Clazz clazz)
    {
        return clazz.getString(u2descriptorIndex);
    }

    public void accept(Clazz clazz, MemberVisitor memberVisitor)
    {
        accept((ProgramClass)clazz, memberVisitor);
    }
}
