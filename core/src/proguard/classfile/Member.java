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
package proguard.classfile;

import proguard.classfile.visitor.*;
import proguard.util.*;

/**
 * Representation of a class member (field or method).
 *
 * @author Eric Lafortune
 */
public interface Member extends Processable
{
    /**
     * Returns the access flags.
     */
    public int getAccessFlags();

    /**
     * Returns the class member name.
     */
    public String getName(Clazz clazz);

    /**
     * Returns the class member's descriptor.
     */
    public String getDescriptor(Clazz clazz);

    /**
     * Accepts the given class visitor.
     */
    public void accept(Clazz clazz, MemberVisitor memberVisitor);

    /**
     * Lets the Clazz objects referenced in the descriptor string
     * accept the given visitor.
     */
    public void referencedClassesAccept(ClassVisitor classVisitor);
}
