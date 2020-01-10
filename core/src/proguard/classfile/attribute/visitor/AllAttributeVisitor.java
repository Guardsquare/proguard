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
package proguard.classfile.attribute.visitor;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.*;

/**
 * This {@link ClassVisitor}, {@link MemberVisitor}, and {@link AttributeVisitor} lets a given
 * {@link AttributeVisitor} visit all Attribute instances of the program classes,
 * program class members, or code attributes, respectively, that it visits.
 *
 * @author Eric Lafortune
 */
public class AllAttributeVisitor
extends      SimplifiedVisitor
implements   ClassVisitor,
             MemberVisitor,
             AttributeVisitor
{
    private final boolean          deep;
    private final AttributeVisitor attributeVisitor;


    /**
     * Creates a new shallow AllAttributeVisitor.
     * @param attributeVisitor the AttributeVisitor to which visits will be
     *                         delegated.
     */
    public AllAttributeVisitor(AttributeVisitor attributeVisitor)
    {
        this(false, attributeVisitor);
    }


    /**
     * Creates a new optionally deep AllAttributeVisitor.
     * @param deep             specifies whether the attributes contained
     *                         further down the class structure should be
     *                         visited too.
     * @param attributeVisitor the AttributeVisitor to which visits will be
     *                         delegated.
     */
    public AllAttributeVisitor(boolean          deep,
                               AttributeVisitor attributeVisitor)
    {
        this.deep             = deep;
        this.attributeVisitor = attributeVisitor;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        programClass.attributesAccept(attributeVisitor);

        // Visit the attributes further down the class structure, if required.
        if (deep)
        {
            programClass.fieldsAccept(this);
            programClass.methodsAccept(this);
            programClass.attributesAccept(this);
        }
    }


    public void visitLibraryClass(LibraryClass libraryClass) {}


    // Implementations for MemberVisitor.

    public void visitProgramMember(ProgramClass programClass, ProgramMember programMember)
    {
        programMember.attributesAccept(programClass, attributeVisitor);

        // Visit the attributes further down the member structure, if required.
        if (deep)
        {
            programMember.attributesAccept(programClass, this);
        }
    }


    public void visitLibraryMember(LibraryClass programClass, LibraryMember programMember) {}


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        codeAttribute.attributesAccept(clazz, method, attributeVisitor);
    }
}
