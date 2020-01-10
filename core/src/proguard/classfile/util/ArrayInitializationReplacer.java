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
package proguard.classfile.util;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.evaluation.*;
import proguard.evaluation.value.*;

/**
 * This {@link ClassVisitor} replaces array initialization instructions with optimized
 * primitive array constants.
 * <p/>
 * These constants are not supported by any Java specification and therefore
 * only for internal use.
 *
 * @see PrimitiveArrayConstantReplacer
 * @author Thomas Neidhart
 */
public class ArrayInitializationReplacer
extends      SimplifiedVisitor
implements   ClassVisitor,

             // Implementation interfaces.
             AttributeVisitor,
             InstructionVisitor
{
    private final ValueFactory               valueFactory               = new ParticularValueFactory(new BasicValueFactory());
    private final PartialEvaluator           partialEvaluator           = new PartialEvaluator(valueFactory,
                                                                                               new BasicInvocationUnit(valueFactory),
                                                                                               true);
    private final ArrayInitializationMatcher arrayInitializationMatcher = new ArrayInitializationMatcher(partialEvaluator);
    private final CodeAttributeEditor        codeAttributeEditor        = new CodeAttributeEditor();

    private ConstantPoolEditor constantPoolEditor;
    private int                lastInstructionOffset;
    private int                lastInstructionStackPushCount;
    private int                arrayInitializationStart;
    private int                arrayInitializationEnd;


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        constantPoolEditor = new ConstantPoolEditor(programClass);

        // Visit all methods that have "NEWARRAY" instructions.
        programClass.methodsAccept(
            new AllAttributeVisitor(
            new ArrayInitializationFilter(
            this)));
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        codeAttributeEditor.reset(codeAttribute.u4codeLength);

        partialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);

        lastInstructionOffset         = -1;
        lastInstructionStackPushCount = -1;
        arrayInitializationStart      = -1;
        arrayInitializationEnd        = -1;
        codeAttribute.instructionsAccept(clazz, method, this);

        if (codeAttributeEditor.isModified())
        {
            codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
        }
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz         clazz,
                                    Method        method,
                                    CodeAttribute codeAttribute,
                                    int           offset,
                                    Instruction   instruction)
    {
        // Verify that the previous instruction pushed the array size on the
        // stack: the java compiler will always do so, but obfuscators may
        // have reordered the instructions.
        if (instruction.opcode == Instruction.OP_NEWARRAY &&
            lastInstructionStackPushCount == 1)
        {
            if (arrayInitializationMatcher.matchesArrayInitialization(clazz,
                                                                      method,
                                                                      codeAttribute,
                                                                      offset,
                                                                      (SimpleInstruction)instruction))
            {
                Object values        = arrayInitializationMatcher.array();
                int    constantIndex = constantPoolEditor.addPrimitiveArrayConstant(values);

                // We need to replace the previous instruction, which pushes
                // the array length onto the stack.
                codeAttributeEditor.replaceInstruction(lastInstructionOffset,
                                                       new ConstantInstruction(Instruction.OP_LDC,
                                                                               constantIndex));

                // Remove the newarray instruction itself.
                codeAttributeEditor.deleteInstruction(offset);

                // Mark the start/end of the array initialization sequence.
                arrayInitializationStart = arrayInitializationMatcher.arrayInitializationStart();
                arrayInitializationEnd   = arrayInitializationMatcher.arrayInitializationEnd();
            }
        }

        // Remove any instruction inside the array initialization sequence.
        if (arrayInitializationEnd != -1       &&
            offset >= arrayInitializationStart &&
            offset <= arrayInitializationEnd)
        {
            codeAttributeEditor.deleteInstruction(offset);
        }

        lastInstructionOffset         = offset;
        lastInstructionStackPushCount = instruction.stackPushCount(clazz);
    }


    /**
     * Private utility class to visit only CodeAttributes that contain
     * "NEWARRAY" instructions.
     */
    private static class ArrayInitializationFilter
    extends    SimplifiedVisitor
    implements AttributeVisitor
    {
        private final AttributeVisitor acceptedVisitor;


        public ArrayInitializationFilter(AttributeVisitor acceptedVisitor)
        {
            this.acceptedVisitor = acceptedVisitor;
        }


        // Implementations for AttributeVisitor.

        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


        public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
        {
            boolean delegateVisit = false;
            // Directly iterate of all instructions and exit early if
            // we encounter a "NEWARRAY" instruction.
            for (int offset = 0; offset < codeAttribute.u4codeLength;)
            {
                Instruction instruction = InstructionFactory.create(codeAttribute.code, offset);
                if (instruction.opcode == Instruction.OP_NEWARRAY)
                {
                    delegateVisit = true;
                    break;
                }

                offset += instruction.length(offset);
            }

            if (delegateVisit)
            {
                acceptedVisitor.visitCodeAttribute(clazz, method, codeAttribute);
            }
        }
    }
}
