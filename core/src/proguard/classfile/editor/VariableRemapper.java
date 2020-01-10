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
import proguard.classfile.attribute.annotation.*;
import proguard.classfile.attribute.annotation.target.*;
import proguard.classfile.attribute.annotation.target.visitor.*;
import proguard.classfile.attribute.annotation.visitor.TypeAnnotationVisitor;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This {@link AttributeVisitor} remaps variable indexes in all attributes that it
 * visits, based on a given index map.
 *
 * @author Eric Lafortune
 */
public class VariableRemapper
extends      SimplifiedVisitor
implements   AttributeVisitor,
             InstructionVisitor,
             LocalVariableInfoVisitor,
             LocalVariableTypeInfoVisitor,
             TypeAnnotationVisitor,
             TargetInfoVisitor,
             LocalVariableTargetElementVisitor
{
    //*
    private static final boolean DEBUG = false;
    /*/
    private static       boolean DEBUG = System.getProperty("vr") != null;
    //*/


    private final CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

    private int[] variableMap;


    /**
     * Sets the given mapping of old variable indexes to their new indexes.
     * Variables that should disappear can be mapped to -1.
     */
    public void setVariableMap(int[] variableMap)
    {
        this.variableMap = variableMap;
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitMethodParametersAttribute(Clazz clazz, Method method, MethodParametersAttribute methodParametersAttribute)
    {
        // Reorder the array with parameter information.
        ParameterInfo[] oldParameters = methodParametersAttribute.parameters;
        ParameterInfo[] newParameters =
            new ParameterInfo[methodParametersAttribute.u1parametersCount];

        for (int index = 0; index < methodParametersAttribute.u1parametersCount; index++)
        {
            newParameters[remapVariable(index)] = oldParameters[index];
        }

        methodParametersAttribute.parameters = newParameters;
    }


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        if (DEBUG)
        {
            System.out.println("VariableRemapper: "+clazz.getName()+"."+method.getName(clazz)+method.getDescriptor(clazz));
            for (int index= 0; index < codeAttribute.u2maxLocals; index++)
            {
                System.out.println("  v"+index+" -> "+variableMap[index]);
            }
        }

        // Remap the variables of the attributes, before editing the code and
        // cleaning up its local variable frame.
        codeAttribute.attributesAccept(clazz, method, this);

        // Initially, the code attribute editor doesn't contain any changes.
        codeAttributeEditor.reset(codeAttribute.u4codeLength);

        // Remap the variables of the instructions.
        codeAttribute.instructionsAccept(clazz, method, this);

        // Apply the code attribute editor.
        codeAttributeEditor.visitCodeAttribute(clazz, method, codeAttribute);
    }


    public void visitLocalVariableTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTableAttribute localVariableTableAttribute)
    {
        // Remap the variable references of the local variables.
        localVariableTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
    }


    public void visitLocalVariableTypeTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeTableAttribute localVariableTypeTableAttribute)
    {
        // Remap the variable references of the local variables.
        localVariableTypeTableAttribute.localVariablesAccept(clazz, method, codeAttribute, this);
    }


    public void visitAnyTypeAnnotationsAttribute(Clazz clazz, TypeAnnotationsAttribute typeAnnotationsAttribute)
    {
        // Remap the variable references of local variable type annotations.
        typeAnnotationsAttribute.typeAnnotationsAccept(clazz, this);
    }


    // Implementations for LocalVariableInfoVisitor.

    public void visitLocalVariableInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableInfo localVariableInfo)
    {
        localVariableInfo.u2index =
            remapVariable(localVariableInfo.u2index);
    }


    // Implementations for LocalVariableTypeInfoVisitor.

    public void visitLocalVariableTypeInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LocalVariableTypeInfo localVariableTypeInfo)
    {
        localVariableTypeInfo.u2index =
            remapVariable(localVariableTypeInfo.u2index);
    }


    // Implementations for TypeAnnotationVisitor.

    public void visitTypeAnnotation(Clazz clazz, TypeAnnotation typeAnnotation)
    {
        typeAnnotation.targetInfoAccept(clazz, this);
    }


    // Implementations for TargetInfoVisitor.

    public void visitAnyTargetInfo(Clazz clazz, TypeAnnotation typeAnnotation, TargetInfo targetInfo) {}


    public void visitLocalVariableTargetInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, TypeAnnotation typeAnnotation, LocalVariableTargetInfo localVariableTargetInfo)
    {
        localVariableTargetInfo.targetElementsAccept(clazz, method, codeAttribute, typeAnnotation, this);
    }


    // Implementations for LocalVariableTargetElementVisitor.

    public void visitLocalVariableTargetElement(Clazz clazz, Method method, CodeAttribute codeAttribute, TypeAnnotation typeAnnotation, LocalVariableTargetInfo localVariableTargetInfo, LocalVariableTargetElement localVariableTargetElement)
    {
        localVariableTargetElement.u2index  =
            remapVariable(localVariableTargetElement.u2index);
    }


    // Implementations for InstructionVisitor.

    public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}


    public void visitVariableInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, VariableInstruction variableInstruction)
    {
        // Is the new variable index different from the original one?
        int oldVariableIndex = variableInstruction.variableIndex;
        int newVariableIndex = remapVariable(oldVariableIndex);
        if (newVariableIndex != oldVariableIndex)
        {
            // Replace the instruction.
            Instruction replacementInstruction =
                new VariableInstruction(variableInstruction.opcode,
                                        newVariableIndex,
                                        variableInstruction.constant);

            codeAttributeEditor.replaceInstruction(offset, replacementInstruction);
        }
    }


    // Small utility methods.

    /**
     * Returns the new variable index of the given variable.
     */
    private int remapVariable(int variableIndex)
    {
        return variableMap[variableIndex];
    }
}
