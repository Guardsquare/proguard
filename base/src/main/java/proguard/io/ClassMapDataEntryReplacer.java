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
package proguard.io;

import proguard.classfile.*;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.*;
import proguard.configuration.InitialStateInfo;
import proguard.util.ProcessingFlags;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static proguard.configuration.ConfigurationLogger.*;


/**
 * This DataEntryReader writes a class mapping to each received data
 * entry, used for debugging of the configuration.
 *
 * @author Johan Leys
 */
public class ClassMapDataEntryReplacer
implements   DataEntryReader
{
    private final ClassPool        programClassPool;
    private final InitialStateInfo initialStateInfo;
    private final DataEntryWriter  dataEntryWriter;
    private       DataOutputStream dataOutputStream;

    public ClassMapDataEntryReplacer(ClassPool        programClassPool,
                                     InitialStateInfo initialStateInfo,
                                     DataEntryWriter  dataEntryWriter)
    {
        this.programClassPool   = programClassPool;
        this.initialStateInfo   = initialStateInfo;
        this.dataEntryWriter    = dataEntryWriter;
    }

    // Implementations for DataEntryReader.

    @Override
    public void read(DataEntry dataEntry) throws IOException
    {
        OutputStream outputStream = dataEntryWriter.createOutputStream(dataEntry);
        if (outputStream != null)
        {
            dataOutputStream = new DataOutputStream(outputStream);
            try
            {
                dataOutputStream.writeInt(initialStateInfo.size());
                writeClassMap();
            }
            finally
            {
                dataOutputStream.close();
            }
        }
    }

    // Private utility methods.

    private void writeClassMap() throws IOException
    {
        for (String className : initialStateInfo.classNames())
        {
            ProgramClass clazz = (ProgramClass)programClassPool.getClass(className);

            dataOutputStream.writeUTF(ClassUtil.externalClassName(className));

            if (clazz == null)
            {
                // The class is no longer in the classpool or encrypted class pool so it must have been shrunk.
                // obfuscated name (original name, because it was removed from class pool so not obfuscated)
                dataOutputStream.writeUTF(ClassUtil.externalClassName(className));
                dataOutputStream.writeUTF(ClassUtil.externalClassName(initialStateInfo.getSuperClassName(className)));
                // flag
                dataOutputStream.writeShort(CLASS_SHRUNK);
                writeMembers(initialStateInfo.getFieldHashMap(className), Collections.emptyList());
                writeMembers(initialStateInfo.getMethodHashMap(className), Collections.emptyList());
            }
            else
            {
                // obfuscated name
                dataOutputStream.writeUTF(ClassUtil.externalClassName(clazz.getName()));
                dataOutputStream.writeUTF(ClassUtil.externalClassName(clazz.getSuperName()));
                dataOutputStream.writeShort(
                    (isKept(clazz.processingFlags) ? CLASS_KEPT : 0) |
                    (allDeclaredFieldsKept(clazz) ? ALL_DECLARED_FIELDS_KEPT : 0) |
                    (allPublicFieldsKept(clazz) ? ALL_PUBLIC_FIELDS_KEPT : 0) |
                    (allDeclaredConstructorsKept(clazz) ? ALL_DECLARED_CONSTRUCTORS_KEPT : 0) |
                    (allPublicConstructorsKept(clazz) ? ALL_PUBLIC_CONSTRUCTORS_KEPT : 0) |
                    (allDeclaredMethodsKept(clazz) ? ALL_DECLARED_METHODS_KEPT : 0) |
                    (allPublicMethodsKept(clazz) ? ALL_PUBLIC_METHODS_KEPT : 0)
                );
                writeMembers(initialStateInfo.getFieldHashMap(className), Arrays.asList(clazz.fields));
                writeMembers(initialStateInfo.getMethodHashMap(className), Arrays.asList(clazz.methods));
            }
        }
    }

    private <T extends Member> void writeMembers(Map<T, Integer> originalMemberHashes, Collection<T> currentMembers)
    throws IOException
    {
        dataOutputStream.writeShort(originalMemberHashes.size());
        for (Map.Entry<T, Integer> entry : originalMemberHashes.entrySet())
        {
            dataOutputStream.writeInt(entry.getValue());
            dataOutputStream.writeByte((currentMembers.contains(entry.getKey())     ? 0 : MEMBER_SHRUNK) |
                                       (isKept(entry.getKey().getProcessingFlags()) ? MEMBER_KEPT : 0));
        }
    }

    // Small utility methods.

    static boolean isKept(int processingFlags)
    {
        return (processingFlags & ProcessingFlags.DONT_OBFUSCATE) != 0 &&
               (processingFlags & ProcessingFlags.DONT_SHRINK)    != 0;
    }


    /**
     * Returns whether all fields of the class (not including the fields of super classes) are kept.
     */
    private static boolean allDeclaredFieldsKept(Clazz clazz)
    {
        if ((clazz.getProcessingFlags() & ProcessingFlags.REMOVED_FIELDS) != 0)
        {
            return false;
        }

        MemberCounter unkeptCounter = new MemberCounter();

        clazz.fieldsAccept(
            new MemberProcessingFlagFilter(0, ProcessingFlags.INJECTED,
            new MemberProcessingFlagFilter(ProcessingFlags.DONT_SHRINK | ProcessingFlags.DONT_OBFUSCATE, 0,
            null,
            unkeptCounter)));

        return unkeptCounter.getCount() == 0;
    }


    /**
     * Returns whether all public fields of the class, including the public fields of super classes, are kept.
     */
    private static boolean allPublicFieldsKept(Clazz clazz)
    {
        ClassCounter  removedCounter = new ClassCounter();
        MemberCounter unkeptCounter  = new MemberCounter();

        clazz.hierarchyAccept(true, true, false, false,
            new ProgramClassFilter(
            new MultiClassVisitor(
                // Check for removed public fields.
                new ClassProcessingFlagFilter(ProcessingFlags.REMOVED_PUBLIC_FIELDS, 0,
                removedCounter),
                // Check for unkept public fields.
                new AllFieldVisitor(
                new MemberAccessFilter(AccessConstants.PUBLIC, 0,
                new MemberProcessingFlagFilter(0, ProcessingFlags.INJECTED,
                new MemberProcessingFlagFilter(ProcessingFlags.DONT_SHRINK | ProcessingFlags.DONT_OBFUSCATE, 0,
                null,
                unkeptCounter))))
            )));

        return removedCounter.getCount() == 0 && unkeptCounter.getCount() == 0;
    }


    /**
     * Returns whether all constructors of the class (not including the constructors of super classes) are kept.
     */
    private static boolean allDeclaredConstructorsKept(Clazz clazz)
    {
        if ((clazz.getProcessingFlags() & ProcessingFlags.REMOVED_CONSTRUCTORS) != 0)
        {
            return false;
        }

        MemberCounter unkeptCounter = new MemberCounter();

        clazz.methodsAccept(
            new ConstructorMethodFilter(
            new MemberProcessingFlagFilter(0, ProcessingFlags.INJECTED,
            new MemberProcessingFlagFilter(ProcessingFlags.DONT_SHRINK | ProcessingFlags.DONT_OBFUSCATE, 0,
            null,
            unkeptCounter))));

        return unkeptCounter.getCount() == 0;
    }



    /**
     * Returns whether all public constructors of the class (not including the public constructors of super classes)
     * are kept.
     */
    private static boolean allPublicConstructorsKept(Clazz clazz)
    {
        if ((clazz.getProcessingFlags() & ProcessingFlags.REMOVED_PUBLIC_CONSTRUCTORS) != 0)
        {
            return false;
        }

        MemberCounter unkeptCounter  = new MemberCounter();

        clazz.hierarchyAccept(true, true, false, false,
            new ProgramClassFilter(
            new AllMethodVisitor(
            new ConstructorMethodFilter(
            new MemberAccessFilter(AccessConstants.PUBLIC, 0,
            new MemberProcessingFlagFilter(0, ProcessingFlags.INJECTED,
            new MemberProcessingFlagFilter(ProcessingFlags.DONT_SHRINK | ProcessingFlags.DONT_OBFUSCATE, 0,
            null,
            unkeptCounter)))))));

        return unkeptCounter.getCount() == 0;
    }


    /**
     * Returns whether all methods of the class (not including the methods of super classes) are kept.
     */
    private static boolean allDeclaredMethodsKept(Clazz clazz)
    {
        if ((clazz.getProcessingFlags() & ProcessingFlags.REMOVED_METHODS) != 0)
        {
            return false;
        }

        MemberCounter unkeptCounter = new MemberCounter();

        clazz.methodsAccept(
            new InitializerMethodFilter(
            null,
            new MemberProcessingFlagFilter(0, ProcessingFlags.INJECTED,
            new MemberProcessingFlagFilter(ProcessingFlags.DONT_SHRINK | ProcessingFlags.DONT_OBFUSCATE, 0,
            null,
            unkeptCounter))));

        return unkeptCounter.getCount() == 0;
    }


    /**
     * Returns whether all public methods of the class, including the public methods of super classes, are kept.
     */
    private static boolean allPublicMethodsKept(Clazz clazz)
    {
        ClassCounter  removedCounter = new ClassCounter();
        MemberCounter unkeptCounter  = new MemberCounter();

        clazz.hierarchyAccept(true, true, false, false,
            new ProgramClassFilter(
            new MultiClassVisitor(
                // Check for removed public methods.
                new ClassProcessingFlagFilter(ProcessingFlags.REMOVED_PUBLIC_METHODS, 0,
                removedCounter),
                // Check for unkept public methods.
                new AllMethodVisitor(
                new InitializerMethodFilter(
                null,
                new MemberAccessFilter(AccessConstants.PUBLIC, 0,
                new MemberProcessingFlagFilter(0, ProcessingFlags.INJECTED,
                new MemberProcessingFlagFilter(ProcessingFlags.DONT_SHRINK | ProcessingFlags.DONT_OBFUSCATE, 0,
                null,
                unkeptCounter)))))
            )));

        return removedCounter.getCount() == 0 && unkeptCounter.getCount() == 0;
    }
}
