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
package proguard.classfile.instruction.visitor;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This InstructionVisitor counts the number of instructions that has been visited.
 *
 * @author Eric Lafortune
 */
public class InstructionCounter
extends      SimplifiedVisitor
implements   InstructionVisitor
{
    private int count;


    /**
     * Returns the number of instructions that has been visited so far.
     */
    public synchronized int getCount()
    {
        return count;
    }


    // Implementations for InstructionVisitor.

    public synchronized void visitAnyInstruction(Clazz         clazz,
                                                 Method        method,
                                                 CodeAttribute codeAttribute,
                                                 int           offset,
                                                 Instruction   instruction)
    {
        count++;
    }
}
