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
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;

/**
 * This AttributeVisitor computes and updates the maximum local variable frame
 * size of the code attributes that it visits. It also cleans up the local
 * variable tables.
 *
 * @author Eric Lafortune
 */
public class VariableSizeUpdater
extends      SimplifiedVisitor
implements   AttributeVisitor,
             InstructionVisitor
{
    //*
    private static final boolean DEBUG = false;
    /*/
    private static       boolean DEBUG = true;
    //*/


    private VariableCleaner variableCleaner = new VariableCleaner();


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
//        DEBUG =
//            clazz.getName().equals("abc/Def") &&
//            method.getName(clazz).equals("abc");

        // The minimum variable size is determined by the arguments.
        codeAttribute.u2maxLocals =
            ClassUtil.internalMethodParameterSize(method.getDescriptor(clazz),
                                                  method.getAccessFlags());

        if (DEBUG)
        {
            System.out.println("VariableSizeUpdater: "+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz));
            System.out.println("  Max locals: "+codeAttribute.u2maxLocals+" <- parameters");
        }

        // Go over all instructions.
        codeAttribute.instructionsAccept(clazz, method, this);

        // Remove the unused variables of the attributes.
        variableCleaner.visitCodeAttribute(clazz, method, codeAttribute);
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction)
    {
        int variableSize = variableInstruction.variableIndex + 1;
        if (variableInstruction.stackPopCount(clazz)  == 2 ||
            variableInstruction.stackPushCount(clazz) == 2)
        {
            variableSize++;
        }

        if (codeAttribute.u2maxLocals < variableSize)
        {
            codeAttribute.u2maxLocals = variableSize;

            if (DEBUG)
            {
                System.out.println("  Max locals: "+codeAttribute.u2maxLocals+" <- "+variableInstruction.toString(offset));
            }
        }
    }
}
