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
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.util.Processable;

import java.util.Arrays;


/**
 * This ClassVisitor removes NameAndType constant pool entries that are not
 * used.
 *
 * @author Eric Lafortune
 */
public class NameAndTypeShrinker
extends      SimplifiedVisitor
implements   ClassVisitor,
             ConstantVisitor,
             AttributeVisitor
{
    // A processing info flag to indicate the NameAndType constant pool entry is being used.
    private static final Object USED = new Object();

    private       int[]                constantIndexMap;
    private final ConstantPoolRemapper constantPoolRemapper = new ConstantPoolRemapper();


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        // Mark the NameAndType entries referenced by all other constant pool
        // entries.
        programClass.constantPoolEntriesAccept(this);

        // Mark the NameAndType entries referenced by all EnclosingMethod
        // attributes.
        programClass.attributesAccept(this);

        // Shift the used constant pool entries together, filling out the
        // index map.
        int newConstantPoolCount =
            shrinkConstantPool(programClass.constantPool,
                               programClass.u2constantPoolCount);

        // Remap the references to the constant pool if it has shrunk.
        if (newConstantPoolCount < programClass.u2constantPoolCount)
        {
            programClass.u2constantPoolCount = newConstantPoolCount;

            // Remap all constant pool references.
            constantPoolRemapper.setConstantIndexMap(constantIndexMap);
            constantPoolRemapper.visitProgramClass(programClass);
        }
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}


    public void visitInvokeDynamicConstant(Clazz clazz, InvokeDynamicConstant invokeDynamicConstant)
    {
        markNameAndTypeConstant(clazz, invokeDynamicConstant.u2nameAndTypeIndex);
    }


    public void visitAnyRefConstant(Clazz clazz, RefConstant refConstant)
    {
        markNameAndTypeConstant(clazz, refConstant.u2nameAndTypeIndex);
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitEnclosingMethodAttribute(Clazz clazz, EnclosingMethodAttribute enclosingMethodAttribute)
    {
        if (enclosingMethodAttribute.u2nameAndTypeIndex != 0)
        {
            markNameAndTypeConstant(clazz, enclosingMethodAttribute.u2nameAndTypeIndex);
        }
    }


    // Small utility methods.

    /**
     * Marks the given UTF-8 constant pool entry of the given class.
     */
    private void markNameAndTypeConstant(Clazz clazz, int index)
    {
         markAsUsed(((ProgramClass)clazz).getConstant(index));
    }


    /**
     * Marks the given Processable as being used.
     * In this context, the Processable will be a NameAndTypeConstant object.
     */
    private void markAsUsed(Processable processable)
    {
        processable.setProcessingInfo(USED);
    }


    /**
     * Returns whether the given Processable has been marked as being used.
     * In this context, the Processable will be a NameAndTypeConstant object.
     */
    private boolean isUsed(Processable processable)
    {
        return processable.getProcessingInfo() == USED;
    }


    /**
     * Removes all NameAndType entries that are not marked as being used
     * from the given constant pool.
     * @return the new number of entries.
     */
    private int shrinkConstantPool(Constant[] constantPool, int length)
    {
        // Create a new index map, if necessary.
        if (constantIndexMap == null ||
            constantIndexMap.length < length)
        {
            constantIndexMap = new int[length];
        }

        int     counter = 1;
        boolean isUsed  = false;

        // Shift the used constant pool entries together.
        for (int index = 1; index < length; index++)
        {
            Constant constant = constantPool[index];

            // Is the constant being used? Don't update the flag if this is the
            // second half of a long entry.
            if (constant != null)
            {
                isUsed = constant.getTag() != Constant.NAME_AND_TYPE ||
                         isUsed(constant);
            }

            if (isUsed)
            {
                // Remember the new index.
                constantIndexMap[index] = counter;

                // Shift the constant pool entry.
                constantPool[counter++] = constant;
            }
            else
            {
                // Remember an invalid index.
                constantIndexMap[index] = -1;
            }
        }

        // Clear the remaining constant pool elements.
        Arrays.fill(constantPool, counter, length, null);

        return counter;
    }
}
