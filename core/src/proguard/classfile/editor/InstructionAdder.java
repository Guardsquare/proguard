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
package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This {@link InstructionVisitor} adds all instructions that it visits to the given
 * target code attribute.
 *
 * @author Eric Lafortune
 */
public class InstructionAdder
extends      SimplifiedVisitor
implements   InstructionVisitor
{
    private final ConstantAdder         constantAdder;
    private final CodeAttributeComposer codeAttributeComposer;


    /**
     * Creates a new InstructionAdder that will copy classes into the given
     * target code attribute.
     */
    public InstructionAdder(ProgramClass          targetClass,
                            CodeAttributeComposer targetComposer)
    {
        constantAdder         = new ConstantAdder(targetClass);
        codeAttributeComposer = targetComposer;
    }


    // Implementations for InstructionVisitor.


    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
    {
        // Add the instruction.
        codeAttributeComposer.appendInstruction(offset, instruction);
    }


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        // Create a copy of the instruction.
        Instruction newConstantInstruction =
            new ConstantInstruction(constantInstruction.opcode,
                                    constantAdder.addConstant(clazz, constantInstruction.constantIndex),
                                    constantInstruction.constant);

        // Add the instruction.
        codeAttributeComposer.appendInstruction(offset, newConstantInstruction);
    }
}