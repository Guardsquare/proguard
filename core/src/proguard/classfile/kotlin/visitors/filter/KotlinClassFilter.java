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
package proguard.classfile.kotlin.visitors.filter;

import proguard.classfile.*;
import proguard.classfile.attribute.annotation.visitor.*;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.visitor.ClassVisitor;

public class KotlinClassFilter
implements ClassVisitor
{
    private final ClassVisitor filteredDelegate;

    public KotlinClassFilter(ClassVisitor delegate)
    {
        this.filteredDelegate =
            new AllAttributeVisitor(
            new AllAnnotationVisitor(
            new AnnotationTypeFilter(KotlinConstants.TYPE_KOTLIN_METADATA,
                                     new AnnotationToAnnotatedClassVisitor(delegate))));
    }

    // Implementations for ClassVisitor.

    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        programClass.accept(filteredDelegate);
    }

    @Override
    public void visitLibraryClass(LibraryClass libraryClass)
    {
        libraryClass.accept(filteredDelegate);
    }
}
