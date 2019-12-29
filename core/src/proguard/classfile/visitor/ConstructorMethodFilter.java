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
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.MemberVisitor;

/**
 * This MemberVisitor delegates its visits to one of three delegates, depending on whether the visited method is:
 *
 * - a constructor
 * - a constructor that calls a super constructor
 * - or another method.
 *
 * @author Johan Leys
 */
public class ConstructorMethodFilter
extends      SimplifiedVisitor
implements   MemberVisitor,

             // Implementation interfaces.
             AttributeVisitor,
             InstructionVisitor
{
    private static final int FIELD_INDEX = InstructionSequenceMatcher.X;

    private static final Constant[] CONSTANTS = new Constant[] {};

    private static final Instruction[] INVOKE_INSTRUCTIONS = new Instruction[]
    {
        new ConstantInstruction(Instruction.OP_INVOKESPECIAL, FIELD_INDEX),
    };

    private final InstructionSequenceMatcher invokeMatcher = new InstructionSequenceMatcher(CONSTANTS, INVOKE_INSTRUCTIONS);

    private final MemberVisitor superCallingConstructorVisitor;
    private final MemberVisitor constructorVisitor;
    private final MemberVisitor otherMethodVisitor;

    private boolean isSuperConstructorCalled;


    public ConstructorMethodFilter(MemberVisitor constructorVisitor)
    {
        this(constructorVisitor, constructorVisitor, null);
    }



    public ConstructorMethodFilter(MemberVisitor constructorVisitor,
                                   MemberVisitor otherMethodVisitor)
    {
        this(constructorVisitor, constructorVisitor, otherMethodVisitor);
    }


    public ConstructorMethodFilter(MemberVisitor superCallingConstructorVisitor,
                                   MemberVisitor constructorVisitor,
                                   MemberVisitor otherMethodVisitor)
    {
        this.superCallingConstructorVisitor = superCallingConstructorVisitor;
        this.constructorVisitor             = constructorVisitor;
        this.otherMethodVisitor             = otherMethodVisitor;
    }


    // Implementations for MemberVisitor.

    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        // Delegate the visit.
        MemberVisitor delegateVisitor = delegateVisitor(programClass, programMethod);
        if (delegateVisitor != null)
        {
            delegateVisitor.visitProgramMethod(programClass, programMethod);
        }
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        codeAttribute.instructionsAccept(clazz, method, this);
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
    {
        instruction.accept(clazz, method, codeAttribute, offset, invokeMatcher);
        if (invokeMatcher.isMatching())
        {
            MethodrefConstant methodrefConstant = (MethodrefConstant)((ProgramClass)clazz).getConstant(invokeMatcher.matchedArgument(FIELD_INDEX));
            if (ClassConstants.METHOD_NAME_INIT.equals(methodrefConstant.getName(clazz)))
            {
                isSuperConstructorCalled |=
                    methodrefConstant.getClassName(clazz).equals(clazz.getSuperName());
            }
        }
    }


    // Small utility methods.

    private MemberVisitor delegateVisitor(ProgramClass programClass, ProgramMethod programMethod)
    {
        isSuperConstructorCalled = false;

        if (ClassConstants.METHOD_NAME_INIT.equals(programMethod.getName(programClass)))
        {
            // Search the code attribute for super.<init> invocations.
            programMethod.attributesAccept(programClass, this);
            return isSuperConstructorCalled ? superCallingConstructorVisitor : constructorVisitor;
        }
        else
        {
            return otherMethodVisitor;
        }
    }
}
