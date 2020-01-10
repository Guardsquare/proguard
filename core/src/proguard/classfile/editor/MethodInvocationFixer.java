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
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;

/**
 * This {@link AttributeVisitor} fixes all inappropriate special/virtual/static/interface
 * invocations of the code attributes that it visits.
 *
 * @author Eric Lafortune
 */
public class MethodInvocationFixer
extends      SimplifiedVisitor
implements   AttributeVisitor,
             InstructionVisitor,
             ConstantVisitor
{
    private static final boolean DEBUG = false;


    private final CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
    private final NestHostFinder      nestHostFinder      = new NestHostFinder();

    // Return values for the visitor methods.
    private Clazz  referencedClass;
    private Clazz  referencedMethodClass;
    private Method referencedMethod;


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        // Reset the code attribute editor.
        codeAttributeEditor.reset(codeAttribute.u4codeLength);

        // Remap the variables of the instructions.
        codeAttribute.instructionsAccept(clazz, method, this);

        // Apply the code atribute editor.
        codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitConstantInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, ConstantInstruction constantInstruction)
    {
        int constantIndex = constantInstruction.constantIndex;

        // Get information on the called class and method, if present.
        referencedMethod = null;

        clazz.constantPoolEntryAccept(constantIndex, this);

        // Did we find the called class and method?
        if (referencedClass  != null &&
            referencedMethod != null)
        {
            // Do we need to update the opcode?
            byte opcode = constantInstruction.opcode;

            // Is the method static?
            if ((referencedMethod.getAccessFlags() & AccessConstants.STATIC) != 0)
            {
                // But is it not a static invocation?
                if (opcode != Instruction.OP_INVOKESTATIC)
                {
                    // Replace the invocation by an invokestatic instruction.
                    Instruction replacementInstruction =
                        new ConstantInstruction(Instruction.OP_INVOKESTATIC,
                                                constantIndex);

                    codeAttributeEditor.replaceInstruction(offset, replacementInstruction);

                    if (DEBUG)
                    {
                        debug(clazz, method, offset, constantInstruction, replacementInstruction);
                    }
                }
            }

            // Is the method private, or an instance initializer?
            else if ((referencedMethod.getAccessFlags() & AccessConstants.PRIVATE) != 0 ||
                     referencedMethod.getName(referencedMethodClass).equals(ClassConstants.METHOD_NAME_INIT))
            {
                // But is it not a special invocation?
                if (opcode != Instruction.OP_INVOKESPECIAL &&
                    // Check if the two classes are in the same nest.
                    !nestHostFinder.inSameNest(clazz, referencedClass))
                {
                    // Replace the invocation by an invokespecial instruction.
                    Instruction replacementInstruction =
                        new ConstantInstruction(Instruction.OP_INVOKESPECIAL,
                                                constantIndex);

                    codeAttributeEditor.replaceInstruction(offset, replacementInstruction);

                    if (DEBUG)
                    {
                        debug(clazz, method, offset, constantInstruction, replacementInstruction);
                    }
                }
            }

            // Is the method an interface method?
            else if ((referencedClass.getAccessFlags() & AccessConstants.INTERFACE) != 0)
            {
                int invokeinterfaceConstant =
                    (ClassUtil.internalMethodParameterSize(referencedMethod.getDescriptor(referencedMethodClass), false)) << 8;

                if (opcode == Instruction.OP_INVOKESPECIAL &&
                    (referencedMethod.getAccessFlags() & AccessConstants.ABSTRACT) == 0)
                {
                    // Explicit calls to default interface methods *must* be preserved.
                }
                // But is it not an interface invocation, or is the parameter
                // size incorrect?
                else if (opcode != Instruction.OP_INVOKEINTERFACE ||
                         constantInstruction.constant != invokeinterfaceConstant)
                {
                    // Fix the parameter size of the interface invocation.
                    Instruction replacementInstruction =
                        new ConstantInstruction(Instruction.OP_INVOKEINTERFACE,
                                                constantIndex,
                                                invokeinterfaceConstant);

                    codeAttributeEditor.replaceInstruction(offset, replacementInstruction);

                    if (DEBUG)
                    {
                        debug(clazz, method, offset, constantInstruction, replacementInstruction);
                    }
                }
            }

            // The method is not static, private, an instance initializer, or
            // an interface method.
            else
            {
                // But is it not a virtual invocation?
                if (opcode != Instruction.OP_INVOKEVIRTUAL &&
                    (// Replace any non-invokespecial.
                     opcode != Instruction.OP_INVOKESPECIAL ||
                     // For invokespecial, replace invocations from the same
                     // class, and invocations to non-superclasses.
                     clazz.equals(referencedClass)                   ||
                     !clazz.extends_(referencedClass)))
                {
                    // Replace the invocation by an invokevirtual instruction.
                    Instruction replacementInstruction =
                        new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL,
                                                constantIndex);

                    codeAttributeEditor.replaceInstruction(offset, replacementInstruction);

                    if (DEBUG)
                    {
                        debug(clazz, method, offset, constantInstruction, replacementInstruction);
                    }
                }
            }
        }
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitAnyMethodrefConstant(Clazz clazz, AnyMethodrefConstant anyMethodrefConstant)
    {
        // Remember the referenced class. Note that we're interested in the
        // class of the method reference, not in the class in which the
        // method was actually found, unless it is an array type.
        if (ClassUtil.isInternalArrayType(anyMethodrefConstant.getClassName(clazz)))
        {
            // For an array type, the class will be java.lang.Object.
            referencedClass = anyMethodrefConstant.referencedClass;
        }
        else
        {
            clazz.constantPoolEntryAccept(anyMethodrefConstant.u2classIndex, this);
        }

        // Remember the referenced method.
        referencedMethodClass = anyMethodrefConstant.referencedClass;
        referencedMethod      = anyMethodrefConstant.referencedMethod;
    }


    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        // Remember the referenced class.
        referencedClass = classConstant.referencedClass;
    }


    // Small utility methods.

    private void debug(Clazz               clazz,
                       Method              method,
                       int                 offset,
                       ConstantInstruction constantInstruction,
                       Instruction         replacementInstruction)
    {
        System.out.println("MethodInvocationFixer:");
        System.out.println("  Class       = "+clazz.getName());
        System.out.println("  Method      = "+method.getName(clazz)+method.getDescriptor(clazz));
        System.out.println("  Instruction = "+constantInstruction.toString(offset));
        System.out.println("  -> Class    = "+referencedClass);
        System.out.println("     Method   = "+referencedMethod);
        if ((referencedClass.getAccessFlags() & AccessConstants.INTERFACE) != 0)
        {
            System.out.println("     Parameter size   = "+(ClassUtil.internalMethodParameterSize(referencedMethod.getDescriptor(referencedMethodClass), false)));
        }
        System.out.println("  Replacement instruction = "+replacementInstruction.toString(offset));
    }
}
