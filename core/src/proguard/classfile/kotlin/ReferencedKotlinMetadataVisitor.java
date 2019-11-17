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
package proguard.classfile.kotlin;

import proguard.classfile.*;
import proguard.classfile.kotlin.visitors.KotlinMetadataVisitor;
import proguard.classfile.visitor.ClassVisitor;

/**
 * Initializes the kotlin metadata for each Kotlin class. After initialization, all
 * info from the annotation is represented in the Clazz's `kotlinMetadata` field. All
 * arrays in kotlinMetadata are initialized, even if empty.
 */
public class ReferencedKotlinMetadataVisitor
implements   ClassVisitor
{
    private final KotlinMetadataVisitor kotlinMetadataVisitor;

    public ReferencedKotlinMetadataVisitor(KotlinMetadataVisitor kotlinMetadataVisitor)
    {
        this.kotlinMetadataVisitor = kotlinMetadataVisitor;
    }

    // Implementations for ClassVisitor.

    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        programClass.kotlinMetadataAccept(kotlinMetadataVisitor);
    }

    @Override
    public void visitLibraryClass(LibraryClass libraryClass)
    {
        libraryClass.kotlinMetadataAccept(kotlinMetadataVisitor);
    }
}
