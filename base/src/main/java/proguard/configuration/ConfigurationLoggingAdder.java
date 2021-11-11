/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
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
package proguard.configuration;


import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.PeepholeEditor;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.BranchTargetFinder;
import proguard.classfile.util.ClassReferenceInitializer;
import proguard.classfile.util.ClassSubHierarchyInitializer;
import proguard.classfile.visitor.AllMethodVisitor;
import proguard.classfile.visitor.ClassPoolFiller;
import proguard.classfile.visitor.ClassProcessingFlagFilter;
import proguard.classfile.visitor.MultiClassVisitor;
import proguard.io.ClassPathDataEntry;
import proguard.io.ClassReader;
import proguard.io.ExtraDataEntryNameMap;
import proguard.util.ProcessingFlagSetter;
import proguard.util.ProcessingFlags;

import java.io.IOException;

import static proguard.configuration.ConfigurationLoggingInstructionSequenceConstants.*;

/**
 * This class can add configuration debug logging code to all code that
 * relies on reflection. The added code prints suggestions on which keep
 * rules to add to ensure the reflection code will continue working after
 * obfuscation and shrinking.
 *
 * @author Johan Leys
 */
public class ConfigurationLoggingAdder
{
    /**
     * Instruments the given program class pool.
     */
    public void execute(ClassPool programClassPool,
                        ClassPool libraryClassPool,
                        ExtraDataEntryNameMap extraDataEntryNameMap) throws IOException
    {
        // Load the logging utility classes in the program class pool.
        // TODO: The initialization could be incomplete if the loaded classes depend on one another.
        ClassReader classReader =
            new ClassReader(false, false, false, false, null,
            new MultiClassVisitor(
                new ClassPoolFiller(programClassPool),
                new ClassReferenceInitializer(programClassPool, libraryClassPool),
                new ClassSubHierarchyInitializer(),
                new ProcessingFlagSetter(ProcessingFlags.INJECTED
            )));

        classReader.read(new ClassPathDataEntry(ConfigurationLogger.ClassInfo.class));
        classReader.read(new ClassPathDataEntry(ConfigurationLogger.MemberInfo.class));
        classReader.read(new ClassPathDataEntry(ConfigurationLogger.class));

        // Initialize the ConfigurationLogger class with the actual packageName.
        initializeConfigurationLogger(programClassPool);

        // Set up the instruction sequences and their replacements.
        ConfigurationLoggingInstructionSequenceConstants constants =
             new ConfigurationLoggingInstructionSequenceConstants(programClassPool,
                                                                  libraryClassPool);

        BranchTargetFinder  branchTargetFinder  = new BranchTargetFinder();
        CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

        // Replace the instruction sequences in all classes.
        // Do not add configuration debugging to any ProGuard runtime classes,
        // to avoid false positives.
        programClassPool.classesAccept(
            new ClassProcessingFlagFilter(0, ProcessingFlags.INJECTED,
            new AllMethodVisitor(
            new AllAttributeVisitor(
            new PeepholeEditor(branchTargetFinder, codeAttributeEditor,
                               new ConfigurationLoggingInstructionSequencesReplacer(constants.CONSTANTS,
                                                                 constants.RESOURCE,
                                                                 branchTargetFinder,
                                                                 codeAttributeEditor,
                                                                 new ExtraClassAdder(extraDataEntryNameMap)))))));
    }


    /**
     * Initialized the ConfigurationLogger class by injecting the actual packageName.
     */
    private void initializeConfigurationLogger(ClassPool programClassPool)
    {
        ProgramClass configurationLoggerClass =
            (ProgramClass) programClassPool.getClass(LOGGER_CLASS_NAME);

        if (configurationLoggerClass == null)
        {
            throw new RuntimeException("ConfigurationLogger class could not be found in the program classpool.");
        }
    }


    private static class ExtraClassAdder
    implements           InstructionVisitor
    {
        private final ExtraDataEntryNameMap extraDataEntryNameMap;


        ExtraClassAdder(ExtraDataEntryNameMap extraDataEntryNameMap)
        {
            this.extraDataEntryNameMap = extraDataEntryNameMap;
        }


        // Implementations for InstructionVisitor.

        public void visitAnyInstruction(Clazz clazz, Method method, CodeAttribute codeAttribute, int offset, Instruction instruction)
        {
            // Add a dependency from the modified class on the logging class.
            extraDataEntryNameMap.addExtraClassToClass(clazz, ConfigurationLogger.class);
            extraDataEntryNameMap.addExtraClassToClass(clazz, ConfigurationLogger.ClassInfo.class);
            extraDataEntryNameMap.addExtraClassToClass(clazz, ConfigurationLogger.MemberInfo.class);
        }
    }
}
