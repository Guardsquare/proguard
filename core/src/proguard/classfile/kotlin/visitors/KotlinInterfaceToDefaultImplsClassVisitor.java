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
package proguard.classfile.kotlin.visitors;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.visitor.ClassVisitor;

public class KotlinInterfaceToDefaultImplsClassVisitor
implements   KotlinMetadataVisitor
{

    private final ClassVisitor classVisitor;

    public KotlinInterfaceToDefaultImplsClassVisitor(ClassVisitor classVisitor) {
        this.classVisitor = classVisitor;
    }

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        if (kotlinClassKindMetadata.flags.isInterface && kotlinClassKindMetadata.referencedDefaultImplsClass != null)
        {
            kotlinClassKindMetadata.referencedDefaultImplsClass.accept(this.classVisitor);
        }
    }
}
