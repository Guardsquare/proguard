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
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.*;

/**
 * This {@link AttributeVisitor} lets a given instruction visitor edit the code
 * attributes that it visits. The instruction visitor can use a given
 * (optional) branch target finder and code attribute editor, which this
 * class sets up and applies, for convenience.
 *
 * @author Eric Lafortune
 */
public class PeepholeEditor
extends      SimplifiedVisitor
implements   AttributeVisitor
{
    private final BranchTargetFinder  branchTargetFinder;
    private final CodeAttributeEditor codeAttributeEditor;
    private final InstructionVisitor  instructionVisitor;


    /**
     * Creates a new PeepholeEditor.
     * @param codeAttributeEditor the code attribute editor that will be reset
     *                            and then executed.
     * @param instructionVisitor  the instruction visitor that performs
     *                            peephole optimizations using the above code
     *                            attribute editor.
     */
    public PeepholeEditor(CodeAttributeEditor codeAttributeEditor,
                          InstructionVisitor  instructionVisitor)
    {
        this(null, codeAttributeEditor, instructionVisitor);
    }


    /**
     * Creates a new PeepholeEditor.
     * @param branchTargetFinder  branch target finder that will be initialized
     *                            to indicate branch targets in the visited code.
     * @param codeAttributeEditor the code attribute editor that will be reset
     *                            and then executed.
     * @param instructionVisitor  the instruction visitor that performs
     *                            peephole optimizations using the above code
     *                            attribute editor.
     */
    public PeepholeEditor(BranchTargetFinder  branchTargetFinder,
                          CodeAttributeEditor codeAttributeEditor,
                          InstructionVisitor  instructionVisitor)
    {
        this.branchTargetFinder  = branchTargetFinder;
        this.codeAttributeEditor = codeAttributeEditor;
        this.instructionVisitor  = instructionVisitor;
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        if (branchTargetFinder != null)
        {
            // Set up the branch target finder.
            branchTargetFinder.visitCodeAttribute(clazz, method, codeAttribute);
        }

        // Set up the code attribute editor.
        codeAttributeEditor.reset(codeAttribute.u4codeLength);

        // Find the peephole optimizations.
        codeAttribute.instructionsAccept(clazz, method, instructionVisitor);

        // Apply the peephole optimizations.
        codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
    }
}
