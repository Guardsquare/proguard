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
import proguard.classfile.instruction.*;
import proguard.util.ArrayUtil;


/**
 * This {@link InstructionVisitor} delegates all visits to each {@link InstructionVisitor}
 * in a given list.
 *
 * @author Eric Lafortune
 */
public class MultiInstructionVisitor implements InstructionVisitor
{
    private InstructionVisitor[] instructionVisitors;
    private int                  instructionVisitorCount;


    public MultiInstructionVisitor()
    {
        this.instructionVisitors = new InstructionVisitor[16];
    }


    public MultiInstructionVisitor(InstructionVisitor... instructionVisitors)
    {
        this.instructionVisitors     = instructionVisitors;
        this.instructionVisitorCount = instructionVisitors.length;
    }


    public void addInstructionVisitor(InstructionVisitor instructionVisitor)
    {
        instructionVisitors =
            ArrayUtil.add(instructionVisitors,
                          instructionVisitorCount++,
                          instructionVisitor);
    }


    // Implementations for InstructionVisitor.

    public void visitSimpleInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction simpleInstruction)
    {
        for (int index = 0; index < instructionVisitorCount; index++)
        {
            instructionVisitors[index].visitSimpleInstruction(clazz, method, codeAttribute, offset, simpleInstruction);
        }
    }

    public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction)
    {
        for (int index = 0; index < instructionVisitorCount; index++)
        {
            instructionVisitors[index].visitVariableInstruction(clazz, method, codeAttribute, offset, variableInstruction);
        }
    }

    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        for (int index = 0; index < instructionVisitorCount; index++)
        {
            instructionVisitors[index].visitConstantInstruction(clazz, method, codeAttribute, offset, constantInstruction);
        }
    }

    public void visitBranchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, BranchInstruction branchInstruction)
    {
        for (int index = 0; index < instructionVisitorCount; index++)
        {
            instructionVisitors[index].visitBranchInstruction(clazz, method, codeAttribute, offset, branchInstruction);
        }
    }

    public void visitTableSwitchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, TableSwitchInstruction tableSwitchInstruction)
    {
        for (int index = 0; index < instructionVisitorCount; index++)
        {
            instructionVisitors[index].visitTableSwitchInstruction(clazz, method, codeAttribute, offset, tableSwitchInstruction);
        }
    }

    public void visitLookUpSwitchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, LookUpSwitchInstruction lookUpSwitchInstruction)
    {
        for (int index = 0; index < instructionVisitorCount; index++)
        {
            instructionVisitors[index].visitLookUpSwitchInstruction(clazz, method, codeAttribute, offset, lookUpSwitchInstruction);
        }
    }
}
