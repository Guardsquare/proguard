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
package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.*;

import java.util.*;

/**
 * This ClassVisitor sorts the attributes of the classes that it visits.
 * The sorting order is based on the types of the attributes.
 *
 * @author Eric Lafortune
 */
public class AttributeSorter
extends      SimplifiedVisitor
implements   ClassVisitor, MemberVisitor, AttributeVisitor, Comparator
{
    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        // Sort the attributes.
        Arrays.sort(programClass.attributes, 0, programClass.u2attributesCount, this);

        // Sort the attributes of the class members.
        programClass.fieldsAccept(this);
        programClass.methodsAccept(this);
    }


    // Implementations for MemberVisitor.

    public void visitProgramMember(ProgramClass programClass, ProgramMember programMember)
    {
        // Sort the attributes.
        Arrays.sort(programMember.attributes, 0, programMember.u2attributesCount, this);

        // Sort the attributes of the attributes.
        programMember.attributesAccept(programClass, this);
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // Sort the attributes.
        Arrays.sort(codeAttribute.attributes, 0, codeAttribute.u2attributesCount, this);
    }


    // Implementations for Comparator.

    public int compare(Object object1, Object object2)
    {
        Attribute attribute1 = (Attribute)object1;
        Attribute attribute2 = (Attribute)object2;

        return attribute1.u2attributeNameIndex < attribute2.u2attributeNameIndex ? -1 :
               attribute1.u2attributeNameIndex > attribute2.u2attributeNameIndex ?  1 :
                                                                                    0;
    }
}
