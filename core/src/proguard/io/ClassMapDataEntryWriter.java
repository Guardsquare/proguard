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
package proguard.io;

import proguard.classfile.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.MemberVisitor;

import java.io.*;
import java.util.Iterator;


/**
 * This DataEntryWriter writes a class mapping to the given data entry, used
 * for debugging of the configuration.
 *
 * Syntax of the mapping file (one line per class):
 *
 * originalClassName,newClassName,hasObfuscatedMethods,hasObfuscatedFields
 *
 * hasObfuscatedMethods and hasObfuscatedFields can either take the value
 * 0 (false) or 1 (true).
 *
 * @author Johan Leys
 */
public class ClassMapDataEntryWriter
extends      SimplifiedVisitor
implements   DataEntryWriter,

             // Implementation interfaces.
             MemberVisitor
{
    private final ClassPool programClassPool;

    private final DataEntryWriter dataEntryWriter;

    private boolean obfuscatedMethods = false;
    private boolean obfuscatedFields  = false;


    public ClassMapDataEntryWriter(ClassPool       programClassPool,
                                   DataEntryWriter dataEntryWriter  )
    {
        this.programClassPool   = programClassPool;
        this.dataEntryWriter    = dataEntryWriter;
    }


    // Implementations for DataEntryWriter.

    public void close() throws IOException
    {
        dataEntryWriter.close();
    }


    public boolean createDirectory(DataEntry dataEntry) throws IOException
    {
        return dataEntryWriter.createDirectory(dataEntry);
    }


    public boolean sameOutputStream(DataEntry dataEntry1, DataEntry dataEntry2) throws IOException
    {
        return dataEntryWriter.sameOutputStream(dataEntry1, dataEntry2);
    }


    public OutputStream createOutputStream(DataEntry dataEntry) throws IOException
    {
        OutputStream os     = dataEntryWriter.createOutputStream(dataEntry);
        PrintWriter  writer = new PrintWriter(new OutputStreamWriter(os));
        writeClassMap(writer, programClassPool);
        writer.close();
        return os;
    }


    public void println(PrintWriter pw, String prefix)
    {
        pw.println(prefix + "ClassMapDataEntryWriter");
        dataEntryWriter.println(pw, prefix + "  ");
    }


    // Private utility methods.

    private void writeClassMap(PrintWriter writer, ClassPool classPool)
    {
        Iterator iterator = classPool.classNames();
        while (iterator.hasNext())
        {
            String className = (String)iterator.next();

            StringBuilder builder   = new StringBuilder();

            builder.append(ClassUtil.externalClassName(className));
            builder.append(",");

            ProgramClass clazz = (ProgramClass)classPool.getClass(className);
            builder.append(ClassUtil.externalClassName(clazz.getName()));
            builder.append(",");

            boolean hasRemovedMethods = (clazz.u2accessFlags & AccessConstants.REMOVED_METHODS) != 0;
            builder.append(hasRemovedMethods || hasObfuscatedMethods(clazz) ? 1 : 0);
            builder.append(",");

            boolean hasRemovedFields = (clazz.u2accessFlags & AccessConstants.REMOVED_FIELDS) != 0;
            builder.append(hasRemovedFields || hasObfuscatedFields(clazz) ? 1 : 0);
            writer.println(builder.toString());
        }
    }


    private boolean hasObfuscatedMethods(ProgramClass clazz)
    {
        obfuscatedMethods = false;
        clazz.methodsAccept(this);
        return obfuscatedMethods;
    }


    private boolean hasObfuscatedFields(ProgramClass clazz)
    {
        obfuscatedFields = false;
        clazz.fieldsAccept(this);
        return obfuscatedFields;
    }


    // Implementations for MemberVisitor.

    public void visitAnyMember(Clazz clazz, Member member) {}


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        obfuscatedMethods |= (programMethod.getAccessFlags() & AccessConstants.RENAMED) != 0;
    }


    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        obfuscatedFields |= (programField.getAccessFlags() & AccessConstants.RENAMED) != 0;
    }
}
