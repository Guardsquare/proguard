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
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.*;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This {@link InstructionVisitor} lets a given {@link ConstantVisitor} visit all constants
 * of the instructions it visits.
 *
 * @author Eric Lafortune
 */
public class InstructionConstantVisitor
extends      SimplifiedVisitor
implements   InstructionVisitor
{
    private final ConstantVisitor constantVisitor;


    /**
     * Creates a new InstructionConstantVisitor.
     * @param constantVisitor the ConstantVisitor to which visits will be
     *                        delegated.
     */
    public InstructionConstantVisitor(ConstantVisitor constantVisitor)
    {
        this.constantVisitor = constantVisitor;
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        clazz.constantPoolEntryAccept(constantInstruction.constantIndex,
                                      constantVisitor);
    }
}
