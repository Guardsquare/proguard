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
package proguard.classfile.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This ConstantVisitor and ElementValueVisitor lets a given MemberVisitor
 * visit all the referenced class members of the elements that it visits.
 *
 * @author Eric Lafortune
 */
public class ReferencedMemberVisitor
extends      SimplifiedVisitor
implements   ConstantVisitor,
             ElementValueVisitor
{
    protected final MemberVisitor memberVisitor;


    public ReferencedMemberVisitor(MemberVisitor memberVisitor)
    {
        this.memberVisitor = memberVisitor;
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
    {
        stringConstant.referencedMemberAccept(memberVisitor);
    }


    public void visitAnyRefConstant(Clazz clazz, RefConstant refConstant)
    {
        refConstant.referencedMemberAccept(memberVisitor);
    }


    // Implementations for ElementValueVisitor.

    public void visitAnyElementValue(Clazz clazz, Annotation annotation, ElementValue elementValue)
    {
        elementValue.referencedMethodAccept(memberVisitor);
    }
}
