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
package proguard.classfile.attribute;

import proguard.util.SimpleProcessable;

/**
 * Representation of an Exception table entry.
 *
 * @author Eric Lafortune
 */
public class ExceptionInfo extends SimpleProcessable
{
    public int u2startPC;
    public int u2endPC;
    public int u2handlerPC;
    public int u2catchType;

    /**
     * Creates an uninitialized ExceptionInfo.
     */
    public ExceptionInfo()
    {
        this(0, 0, 0, 0);
    }


    /**
     * Creates an ExceptionInfo with the given properties.
     */
    public ExceptionInfo(int u2startPC,
                         int u2endPC,
                         int u2handlerPC,
                         int u2catchType)
    {
        this.u2startPC   = u2startPC;
        this.u2endPC     = u2endPC;
        this.u2handlerPC = u2handlerPC;
        this.u2catchType = u2catchType;
    }


    /**
     * Returns whether the exception's try block contains the instruction at the
     * given offset.
     */
    public boolean isApplicable(int instructionOffset)
    {
        return instructionOffset >= u2startPC &&
               instructionOffset <  u2endPC;
    }


    /**
     * Returns whether the exception's try block overlaps with the specified
     * block of instructions.
     */
    public boolean isApplicable(int startOffset, int endOffset)
    {
        return u2startPC < endOffset &&
               u2endPC   > startOffset;
    }
}
