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
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This {@link ClassVisitor} removes all the classes it visits from a given
 * class pool.
 *
 * @author Eric Lafortune
 */
public class ClassPoolRemover
extends      SimplifiedVisitor
implements   ClassVisitor
{
    private final ClassPool classPool;


    /**
     * Creates a new ClassPoolFiller.
     */
    public ClassPoolRemover(ClassPool classPool)
    {
        this.classPool = classPool;
    }


    // Implementations for ClassVisitor.

    public void visitAnyClass(Clazz clazz)
    {
        classPool.removeClass(clazz);
    }
}
