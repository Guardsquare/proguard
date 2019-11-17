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
package proguard.classfile.visitor;

import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.SimplifiedVisitor;


/**
 * This InstructionVisitor lets a given <code>ClassVisitor</code> visit all
 * classes involved in any <code>.class</code> constructs that it visits.
 * <p>
 * Note that before JDK 1.5, <code>.class</code> constructs are actually
 * compiled differently, using <code>Class.forName</code> constructs.
 *
 * @author Eric Lafortune
 */
public class DotClassClassVisitor
extends      SimplifiedVisitor
implements   InstructionVisitor,
             ConstantVisitor
{
    private final ClassVisitor classVisitor;


    /**
     * Creates a new ClassHierarchyTraveler.
     * @param classVisitor the <code>ClassVisitor</code> to which visits will
     *                     be delegated.
     */
    public DotClassClassVisitor(ClassVisitor classVisitor)
    {
        this.classVisitor = classVisitor;
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        byte opcode = constantInstruction.opcode;

        // Could this instruction be a .class construct?
        if (opcode == InstructionConstants.OP_LDC ||
            opcode == InstructionConstants.OP_LDC_W)
        {
            clazz.constantPoolEntryAccept(constantInstruction.constantIndex,
                                          this);
        }
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        // Visit the referenced class from the .class construct.
        classConstant.referencedClassAccept(classVisitor);
    }
}
