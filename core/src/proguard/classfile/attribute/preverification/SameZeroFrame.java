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
package proguard.classfile.attribute.preverification;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.preverification.visitor.StackMapFrameVisitor;

/**
 * This StackMapFrame represents a "same frame" or a "same frame extended".
 *
 * @author Eric Lafortune
 * @noinspection PointlessArithmeticExpression
 */
public class SameZeroFrame extends StackMapFrame
{
    /**
     * Creates an uninitialized SameZeroFrame.
     */
    public SameZeroFrame()
    {
    }


    /**
     * Creates a SameZeroFrame with the given tag.
     */
    public SameZeroFrame(int tag)
    {
        u2offsetDelta = tag - SAME_ZERO_FRAME;
    }


    // Implementations for StackMapFrame.

    public int getTag()
    {
        return u2offsetDelta < 64 ?
            SAME_ZERO_FRAME + u2offsetDelta :
            SAME_ZERO_FRAME_EXTENDED;
    }


    public void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, StackMapFrameVisitor stackMapFrameVisitor)
    {
        stackMapFrameVisitor.visitSameZeroFrame(clazz, method, codeAttribute, offset, this);
    }


    // Implementations for Object.

    public String toString()
    {
        return super.toString()+"Var: ..., Stack: (empty)";
    }
}
