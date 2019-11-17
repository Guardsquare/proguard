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
package proguard.classfile.attribute.visitor;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This InstructionVisitor delegates to a given AttributeVisitor.
 *
 * @author Johan Leys
 */
public class InstructionToAttributeVisitor
extends      SimplifiedVisitor
implements   InstructionVisitor
{
    private final AttributeVisitor attributeVisitor;


    /**
     * Creates a new InstructionToAttributeVisitor.
     */
    public InstructionToAttributeVisitor(AttributeVisitor attributeVisitor)
    {
        this.attributeVisitor = attributeVisitor;
    }


    // Implementations for InstructionVisitor.


    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
    {
        codeAttribute.accept(clazz, method, attributeVisitor);
    }
}

