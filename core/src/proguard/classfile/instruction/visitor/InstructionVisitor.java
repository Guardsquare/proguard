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


/**
 * This interface specifies the methods for a visitor of
 * {@link Instruction} instances.
 *
 * @author Eric Lafortune
 */
public interface InstructionVisitor
{
    public void visitSimpleInstruction(      Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, SimpleInstruction       simpleInstruction);
    public void visitVariableInstruction(    Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction     variableInstruction);
    public void visitConstantInstruction(    Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction     constantInstruction);
    public void visitBranchInstruction(      Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, BranchInstruction       branchInstruction);
    public void visitTableSwitchInstruction( Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, TableSwitchInstruction  tableSwitchInstruction);
    public void visitLookUpSwitchInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, LookUpSwitchInstruction lookUpSwitchInstruction);
}
