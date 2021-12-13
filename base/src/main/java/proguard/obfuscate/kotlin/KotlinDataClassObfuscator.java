/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.obfuscate.kotlin;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.*;
import proguard.classfile.instruction.*;
import proguard.classfile.instruction.visitor.*;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.*;
import proguard.classfile.kotlin.visitor.filter.KotlinFunctionFilter;
import proguard.obfuscate.ClassObfuscator;

import java.util.*;
import java.util.function.Function;

import static proguard.classfile.ClassConstants.METHOD_NAME_TOSTRING;
import static proguard.classfile.ClassConstants.METHOD_TYPE_TOSTRING;
import static proguard.classfile.util.ClassUtil.internalSimpleClassName;
import static proguard.obfuscate.ClassObfuscator.hasOriginalClassName;
import static proguard.obfuscate.ClassObfuscator.newClassName;

/**
 * Data classes in Kotlin hold data and the compiler uses this fact to automatically
 * generate functions that a programmer would normally implement manually.
 *
 * One of these functions is the default toString() of the form "User(name=John, age=42)".
 *
 * This exposes unobfuscated names so we must update these strings with the obfuscated
 * versions.
 *
 * This class relies on the {@link KotlinPropertyNameObfuscator} storing the obfuscated
 * name in the processingInfo field. And that the {@link ClassObfuscator} gives us the
 * new class name (internally this also relies on the new class name being in the
 * processingInfo field).
 */
public class KotlinDataClassObfuscator
implements   KotlinMetadataVisitor
{
    private static final Comparator<String> REVERSE_LENGTH_STRING_ORDER =
        Comparator
            .comparingInt(String::length)
            .reversed()
            .thenComparing(Function.identity());


    // Implementations for KotlinMetadataVisitor.

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        // Retrieve all the property names and their obfuscated versions.
        // Start with the longest strings, in-case the smaller strings appear within longer ones.
        Map<String, String> nameMap = new TreeMap<>(REVERSE_LENGTH_STRING_ORDER);

        kotlinClassKindMetadata.propertiesAccept(clazz, new PropertyNameCollector(nameMap));


        // Add the original class name/obfuscated class name to the map as well
        // but add "(" to class name to differentiate in-case the class is named the same as a property.
        if (!hasOriginalClassName(clazz))
        {
            nameMap.put(internalSimpleClassName(kotlinClassKindMetadata.className) + "(",
                        internalSimpleClassName(newClassName(clazz)) + "(");
        }

        // We visit all the ldc instructions in the automatically declared toString function
        // and use the nameMap to replace string values.

        kotlinClassKindMetadata.functionsAccept(clazz,
            new KotlinFunctionFilter(
                fun -> !fun.flags.isDeclaration &&
                       fun.name.equals(METHOD_NAME_TOSTRING) &&
                       fun.jvmSignature.descriptor.toString().equals(METHOD_TYPE_TOSTRING),
                new KotlinFunctionToMethodVisitor(
                new AllAttributeVisitor(
                new MyObfuscatedToStringFixer(nameMap, new ConstantPoolEditor((ProgramClass)clazz))))));
    }


    private static final class MyObfuscatedToStringFixer
    implements                 AttributeVisitor,
                               InstructionVisitor,
                               ConstantVisitor
    {
        private final Map<String, String> originalToNewName;
        private final ConstantPoolEditor  constantPoolEditor;
        private final CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
        private       String              replacement         = null;

        private MyObfuscatedToStringFixer(Map<String, String> originalToNewName, ConstantPoolEditor constantPoolEditor)
        {
            this.originalToNewName  = originalToNewName;
            this.constantPoolEditor = constantPoolEditor;
        }

        // Implementations for AttributeVisitor.

        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

        @Override
        public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
        {
            codeAttributeEditor.reset(codeAttribute.u4codeLength);
            codeAttribute.instructionsAccept(clazz, method, this);
            codeAttribute.accept(clazz, method, codeAttributeEditor);
        }

        // Implementations for InstructionVisitor.

        @Override
        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction) {}

        @Override
        public void visitConstantInstruction(Clazz clazz,
                                             Method method,
                                             CodeAttribute codeAttribute,
                                             int offset,
                                             ConstantInstruction constantInstruction)
        {
            if (constantInstruction.opcode == Instruction.OP_LDC ||
                constantInstruction.opcode == Instruction.OP_LDC_W)
            {
                replacement = null;
                clazz.constantPoolEntryAccept(constantInstruction.constantIndex, this);
                if (replacement != null)
                {
                    constantInstruction.constantIndex = constantPoolEditor.addStringConstant(replacement);
                    codeAttributeEditor.replaceInstruction(offset, constantInstruction);
                }
            }
        }

        // Implementations for ConstantVisitor.

        @Override
        public void visitStringConstant(Clazz clazz, StringConstant stringConstant)
        {
            clazz.constantPoolEntryAccept(stringConstant.u2stringIndex, this);
        }

        @Override
        public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant)
        {
            Map<String, String> copy = new TreeMap<>(originalToNewName);

            boolean foundAny = false;
            replacement = utf8Constant.getString();

            for (String originalName : copy.keySet())
            {
                if (replacement.contains(originalName))
                {
                    replacement = replacement.replace(originalName, copy.get(originalName));
                    foundAny = true;

                    originalToNewName.remove(originalName);
                }
            }

            if (!foundAny)
            {
                replacement = null;
            }
        }

        @Override
        public void visitAnyConstant(Clazz clazz, Constant constant) {}
    }

    // Collect the original name/new name map - assumes the old name is in the processingInfo.
    private static final class PropertyNameCollector
    implements KotlinPropertyVisitor
    {
        private final Map<String, String> nameMap;

        private PropertyNameCollector(Map<String, String> nameMap) {
            this.nameMap = nameMap;
        }

        // Implementations for KotlinPropertyVisitor.

        @Override
        public void visitAnyProperty(Clazz                              clazz,
                                     KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                     KotlinPropertyMetadata             kotlinPropertyMetadata)
        {
            if (kotlinPropertyMetadata.getProcessingInfo() != null)
            {
                this.nameMap.put(kotlinPropertyMetadata.name + "=", kotlinPropertyMetadata.getProcessingInfo() + "=");
            }
        }
    }
}
