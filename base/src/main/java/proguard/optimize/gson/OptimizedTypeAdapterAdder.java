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
package proguard.optimize.gson;

import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.JavaTypeConstants;
import proguard.classfile.ProgramClass;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.util.ClassReferenceInitializer;
import proguard.classfile.util.ClassSubHierarchyInitializer;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.ClassPoolFiller;
import proguard.classfile.visitor.ClassPresenceFilter;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MultiClassVisitor;
import proguard.io.ClassPathDataEntry;
import proguard.io.ClassReader;
import proguard.io.ExtraDataEntryNameMap;
import proguard.util.ProcessingFlagSetter;
import proguard.util.ProcessingFlags;

import java.io.IOException;
import java.util.Map;

import static proguard.optimize.gson.OptimizedClassConstants.NAME_OPTIMIZED_TYPE_ADAPTER_IMPL;

/**
 * This ClassVisitor visits domain classes that can be involved in a GSON
 * (de)serialization and injects an optimized TypeAdapter for each of them.
 *
 * @author Lars Vandenbergh
 */
public class OptimizedTypeAdapterAdder implements ClassVisitor
{
    //*
    public static final boolean DEBUG = false;
    /*/
    public static       boolean DEBUG = System.getProperty("otaa") != null;
    //*/


    private final ClassPool             programClassPool;
    private final ClassPool             libraryClassPool;
    private final CodeAttributeEditor   codeAttributeEditor;
    private final OptimizedJsonInfo     serializationInfo;
    private final OptimizedJsonInfo     deserializationInfo;
    private final ExtraDataEntryNameMap extraDataEntryNameMap;
    private final Map<String, String>   typeAdapterRegistry;
    private final GsonRuntimeSettings   gsonRuntimeSettings;


    /**
     * Creates a new OptimizedTypeAdapterAdder.
     *
     * @param programClassPool         the program class pool used for looking
     *                                 up references to program classes.
     * @param libraryClassPool         the library class pool used for looking
     *                                 up references to library classes.
     * @param codeAttributeEditor      the code attribute editor used for
     *                                 implementing the added type adapters.
     * @param serializationInfo        contains information on which classes
     *                                 and fields to serialize and how.
     * @param deserializationInfo      contains information on which classes
     *                                 and fields to deserialize and how.
     * @param extraDataEntryNameMap    map to which the names of new type
     *                                 adapter classes are added.
     * @param typeAdapterRegistry      the registry to which the corresponding
     *                                 type adapter class name is added for a
     *                                 given domain class name.
     * @param gsonRuntimeSettings      keeps track of all GsonBuilder invocations.
     */
    public OptimizedTypeAdapterAdder(ClassPool             programClassPool,
                                     ClassPool             libraryClassPool,
                                     CodeAttributeEditor   codeAttributeEditor,
                                     OptimizedJsonInfo     serializationInfo,
                                     OptimizedJsonInfo     deserializationInfo,
                                     ExtraDataEntryNameMap extraDataEntryNameMap,
                                     Map<String, String>   typeAdapterRegistry,
                                     GsonRuntimeSettings   gsonRuntimeSettings)
    {
        this.programClassPool         = programClassPool;
        this.libraryClassPool         = libraryClassPool;
        this.codeAttributeEditor      = codeAttributeEditor;
        this.serializationInfo        = serializationInfo;
        this.deserializationInfo      = deserializationInfo;
        this.extraDataEntryNameMap    = extraDataEntryNameMap;
        this.typeAdapterRegistry      = typeAdapterRegistry;
        this.gsonRuntimeSettings      = gsonRuntimeSettings;
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) { }


    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        // Derive class name for optimized type adapter from the name of the
        // domain class.
        String externalClassName            = ClassUtil.externalClassName(programClass.getName());
        String packageName                  = ClassUtil.externalPackageName(externalClassName);
        String shortClassName               = ClassUtil.externalShortClassName(externalClassName);
        String externalTypeAdapterClassName = packageName + JavaTypeConstants.PACKAGE_SEPARATOR +
                                              "Optimized" + shortClassName + "TypeAdapter";
        String typeAdapterClassName         = ClassUtil.internalClassName(externalTypeAdapterClassName);


        if (programClassPool.getClass(typeAdapterClassName) == null)
        {
            if (DEBUG)
            {
                System.out.println("OptimizedTypeAdapterAdder: injecting " +
                                   typeAdapterClassName);
            }

            ClassReader templateClassReader =
                new ClassReader(false, false, false, false, null,
                                new OptimizedTypeAdapterInitializer(
                                    typeAdapterClassName,
                                    programClass,
                                    codeAttributeEditor,
                                    serializationInfo,
                                    deserializationInfo,
                                    gsonRuntimeSettings.instanceCreatorClassPool,
                                    new MultiClassVisitor(
                                        new ProcessingFlagSetter(ProcessingFlags.INJECTED),
                                        new ClassPresenceFilter(programClassPool, null,
                                            new ClassPoolFiller(programClassPool)),
                                            new ClassReferenceInitializer(programClassPool, libraryClassPool),
                                        new ClassSubHierarchyInitializer())));

            try
            {
                String dataEntryName = getDataEntryName(NAME_OPTIMIZED_TYPE_ADAPTER_IMPL);
                templateClassReader.read(new ClassPathDataEntry(dataEntryName));
                extraDataEntryNameMap.addExtraClassToClass(programClass.getName(), typeAdapterClassName);
                typeAdapterRegistry.put(programClass.getName(), typeAdapterClassName);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    // Utility methods.

    private static String getDataEntryName(String internalClassName)
    {
        // This is mostly done to make sure the internal class name gets
        // adapted properly during obfuscation.
        return internalClassName + ".class";
    }
}
