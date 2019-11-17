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
package proguard.classfile.constant;

import proguard.classfile.Clazz;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.util.SimpleVisitorAccepter;

/**
 * This abstract class represents an entry in the ConstantPool. Specific types
 * of entries are subclassed from it.
 *
 * @author Eric Lafortune
 */
public abstract class Constant extends SimpleVisitorAccepter
{
    //public int  u1tag;
    //public byte info[];

    // Abstract methods to be implemented by extensions.

    /**
     * Returns the constant pool info tag that specifies the entry type.
     */
    public abstract int getTag();


    /**
     * Accepts the given visitor.
     */
    public abstract void accept(Clazz clazz, ConstantVisitor constantVisitor);
}
