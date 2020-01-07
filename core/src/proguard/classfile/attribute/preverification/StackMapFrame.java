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
import proguard.util.SimpleProcessable;

/**
 * This abstract class represents a stack map frame. Specific types
 * of entries are subclassed from it.
 *
 * @author Eric Lafortune
 */
public abstract class StackMapFrame extends SimpleProcessable
{
    public static final int SAME_ZERO_FRAME          =   0;
    public static final int SAME_ONE_FRAME           =  64;
    public static final int SAME_ONE_FRAME_EXTENDED  = 247;
    public static final int LESS_ZERO_FRAME          = 248;
    public static final int SAME_ZERO_FRAME_EXTENDED = 251;
    public static final int MORE_ZERO_FRAME          = 252;
    public static final int FULL_FRAME               = 255;


    public int u2offsetDelta;


    /**
     * Returns the bytecode offset delta relative to the previous stack map
     * frame.
     */
    public int getOffsetDelta()
    {
        return u2offsetDelta;
    }


    // Abstract methods to be implemented by extensions.

    /**
     * Returns the stack map frame tag that specifies the entry type.
     */
    public abstract int getTag();


    /**
     * Accepts the given visitor.
     */
    public abstract void accept(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, StackMapFrameVisitor stackMapFrameVisitor);


    // Implementations for Object.

    public boolean equals(Object object)
    {
        if (object == null ||
            this.getClass() != object.getClass())
        {
            return false;
        }

        StackMapFrame other = (StackMapFrame)object;

        return this.u2offsetDelta == other.u2offsetDelta;
    }


    public int hashCode()
    {
        return getClass().hashCode() ^
               u2offsetDelta;
    }


    public String toString()
    {
        return "[" + u2offsetDelta + "] ";
    }
}
