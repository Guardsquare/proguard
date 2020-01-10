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


/**
 * This {@link ClassPoolVisitor} lets a given {@link ClassVisitor} visit
 * {@link Clazz} instances with a given name, in the class pools that it
 * visits.
 *
 * @author Eric Lafortune
 */
public class NamedClassVisitor implements ClassPoolVisitor
{
    private final ClassVisitor classVisitor;
    private final String       name;


    public NamedClassVisitor(ClassVisitor classVisitor,
                             String       name)
    {
        this.classVisitor = classVisitor;
        this.name         = name;
    }


    // Implementations for NamedClassVisitor.

    public void visitClassPool(ClassPool classPool)
    {
        classPool.classAccept(name, classVisitor);
    }
}
