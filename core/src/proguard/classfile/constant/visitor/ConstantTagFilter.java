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
package proguard.classfile.constant.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.constant.Constant;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This {@link ConstantVisitor} delegates its visits to one or more
 * specified types of constants.
 *
 * @author Eric Lafortune
 */
public class ConstantTagFilter
extends      SimplifiedVisitor
implements   ConstantVisitor
{
    private final int             constantTagMask;
    private final ConstantVisitor constantVisitor;


    /**
     * Creates a new ConstantTagFilter.
     * @param constantTag     the type of constants for which visits will be
     *                        delegated.
     * @param constantVisitor the <code>ConstantVisitor</code> to which visits
     *                        will be delegated.
     */
    public ConstantTagFilter(int             constantTag,
                             ConstantVisitor constantVisitor)
    {
        this.constantTagMask = 1 << constantTag;
        this.constantVisitor = constantVisitor;
    }


    /**
     * Creates a new ConstantTagFilter.
     * @param constantTags    the types of constants for which visits will be
     *                        delegated.
     * @param constantVisitor the <code>ConstantVisitor</code> to which visits
     *                        will be delegated.
     */
    public ConstantTagFilter(int[]           constantTags,
                             ConstantVisitor constantVisitor)
    {
        int constantTagMask = 0;
        for (int index = 0; index < constantTags.length; index++)
        {
            constantTagMask |= 1 << constantTags[index];
        }

        this.constantTagMask = constantTagMask;
        this.constantVisitor = constantVisitor;
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant)
    {
        if (((1 << constant.getTag()) & constantTagMask) != 0)
        {
            constant.accept(clazz, constantVisitor);
        }
    }
}