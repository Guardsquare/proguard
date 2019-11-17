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

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This AttributeVisitor lets a given ClassVisitor visit all the
 * classes referenced by the type descriptors of the signatures that it visits.
 *
 * @author Joachim Vandersmissen
 */
public class SignatureAttributeReferencedClassVisitor
extends      SimplifiedVisitor
implements   AttributeVisitor
{
    private final ClassVisitor classVisitor;


    public SignatureAttributeReferencedClassVisitor(ClassVisitor classVisitor)
    {
        this.classVisitor = classVisitor;
    }


    // Implementations for AttributeVisitor


    public void visitAnyAttribute(Clazz clazz, Attribute attribute)
    {

    }


    public void visitSignatureAttribute(Clazz clazz,
                                        SignatureAttribute signatureAttribute)
    {
        signatureAttribute.referencedClassesAccept(classVisitor);
    }


    public void visitSignatureAttribute(Clazz clazz,
                                        Field field,
                                        SignatureAttribute signatureAttribute)
    {
        signatureAttribute.referencedClassesAccept(classVisitor);
    }


    public void visitSignatureAttribute(Clazz clazz,
                                        Method method,
                                        SignatureAttribute signatureAttribute)
    {
        signatureAttribute.referencedClassesAccept(classVisitor);
    }
}
