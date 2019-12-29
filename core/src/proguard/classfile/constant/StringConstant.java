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
package proguard.classfile.constant;

import proguard.classfile.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.*;
import proguard.resources.file.ResourceFile;
import proguard.resources.file.visitor.ResourceFileVisitor;

/**
 * This Constant represents a string constant in the constant pool.
 *
 * @author Eric Lafortune
 */
public class StringConstant extends Constant
{
    public int u2stringIndex;

    /**
     * An extra field pointing to the referenced Clazz object, if this
     * string is being used in Class.forName(), .class, or
     * Class.getDeclaredField/Method constructs.
     * This field is typically filled out by the <code>{@link
     * proguard.classfile.util.DynamicClassReferenceInitializer
     * DynamicClassReferenceInitializer}</code> or by the <code>{@link
     * proguard.classfile.util.DynamicMemberReferenceInitializer
     * DynamicMemberReferenceInitializer}</code>.
     */
    public Clazz referencedClass;

    /**
     * An extra field pointing to the referenced Member object, if this
     * string is being used in Class.getDeclaredField/Method constructs.
     * This field is typically filled out by the <code>{@link
     * proguard.classfile.util.DynamicMemberReferenceInitializer
     * DynamicMemberReferenceInitializer}</code>.
     */
    public Member referencedMember;

    /**
     * An extra field pointing to the java.lang.String Clazz object.
     * This field is typically filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer
     * ClassReferenceInitializer}</code>..
     */
    public Clazz javaLangStringClass;

    /**
     * An extra field pointing to the referenced resource ID, if this
     * string references an Android resource.
     * Limitation: a String may point to multiple resources with the
     * same name but a different type - only one can be cached.
     */
    public int referencedResourceId;

    /**
     * An extra field pointing to the referenced resource file, if this
     * string references one.
     */
    public ResourceFile referencedResourceFile;


    /**
     * Creates an uninitialized StringConstant.
     */
    public StringConstant()
    {
    }


    /**
     * Creates a new StringConstant with the given string index.
     * @param u2stringIndex    the index of the string in the constant pool.
     * @param referencedClass  the referenced class, if any.
     * @param referencedMember the referenced class member, if any.
     */
    public StringConstant(int    u2stringIndex,
                          Clazz  referencedClass,
                          Member referencedMember)
    {
        this(u2stringIndex, referencedClass, referencedMember, 0, null);
    }


    /**
    * Creates a new StringConstant with the given string index.
    * @param u2stringIndex          the index of the string in the constant pool.
    * @param referencedResourceFile the referenced resource filename, if any.
    */
    public StringConstant(int          u2stringIndex,
                          ResourceFile referencedResourceFile)
    {
        this(u2stringIndex, null, null, 0, referencedResourceFile);
    }


    /**
     * Creates a new StringConstant with the given string index.
     * @param u2stringIndex          the index of the string in the constant pool.
     * @param referencedClass        the referenced class, if any.
     * @param referencedMember       the referenced class member, if any.
     * @param referencedResourceId   the referenced resource id, if any, 0 otherwise.
     * @param referencedResourceFile the referenced resource filename, if any.
     */
    public StringConstant(int          u2stringIndex,
                          Clazz        referencedClass,
                          Member       referencedMember,
                          int          referencedResourceId,
                          ResourceFile referencedResourceFile)
    {
        this.u2stringIndex          = u2stringIndex;
        this.referencedClass        = referencedClass;
        this.referencedMember       = referencedMember;
        this.referencedResourceId   = referencedResourceId;
        this.referencedResourceFile = referencedResourceFile;
    }


    /**
     * Returns the string value.
     */
    public String getString(Clazz clazz)
    {
        return clazz.getString(u2stringIndex);
    }


    // Implementations for Constant.

    public int getTag()
    {
        return Constant.STRING;
    }

    public void accept(Clazz clazz, ConstantVisitor constantVisitor)
    {
        constantVisitor.visitStringConstant(clazz, this);
    }


    /**
     * Lets the referenced class accept the given visitor.
     */
    public void referencedClassAccept(ClassVisitor classVisitor)
    {
        if (referencedClass  != null &&
            referencedMember == null)
        {
            referencedClass.accept(classVisitor);
        }
    }


    /**
     * Lets the referenced member accept the given visitor.
     */
    public void referencedMemberAccept(MemberVisitor memberVisitor)
    {
        if (referencedMember != null)
        {
            referencedMember.accept(referencedClass, memberVisitor);
        }
    }


    /**
     * Lets the referenced resource file accept the given visitor.
     */
    public void referencedResourceFileAccept(ResourceFileVisitor resourceFileVisitor)
    {
        if (referencedResourceFile != null)
        {
            referencedResourceFile.accept(resourceFileVisitor);
        }
    }
}
