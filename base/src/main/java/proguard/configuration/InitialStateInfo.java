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
import proguard.classfile.util.ClassUtil;
import proguard.resources.file.ResourceFilePool;

import java.util.*;

import static proguard.util.HashUtil.hashFnv1a32_UTF8;

/**
 * Stores the initial state of a classpool and resource files including
 * class names, super class names and hashes of fields and methods; and
 * resource filenames.
 *
 * @author James Hamilton
 */
public class InitialStateInfo
{
    private final List<String>                             classNames      = new ArrayList<>();
    private final Map<String, String>                      superClassNames = new HashMap<>();
    private final Map<String, Map<ProgramMethod, Integer>> methodHashes    = new HashMap<>();
    private final Map<String, Map<ProgramField, Integer>>  fieldHashes     = new HashMap<>();


    public InitialStateInfo(ClassPool classPool)
    {
        this.initialize(classPool);
    }


    /**
     *
     * @return The size of the class pool
     */
    public int size()
    {
        return this.classNames.size();
    }

    /**
     *
     * @return The class names
     */
    public List<String> classNames()
    {
        return classNames;
    }


    /**
     * Given a class name return it's super class name.
     *
     * @param className a class name
     *
     * @return super class name
     */
    public String getSuperClassName(String className)
    {
        return this.superClassNames.get(className);
    }

    /**
     * Given a class name return a mapping of method -> original method hash
     *
     * @param className a class name
     *
     * @return map ProgramMethod -> hash(original method signature)
     */
    public Map<ProgramMethod, Integer> getMethodHashMap(String className)
    {
        return this.methodHashes.containsKey(className) ? this.methodHashes.get(className) : new HashMap<>();
    }

    /**
     * Given a class name return a mapping of a field -> original field hash
     *
     * @param className a class name
     *
     * @return map ProgramField -> hash(original field name)
     */
    public Map<ProgramField, Integer> getFieldHashMap(String className)
    {
        return this.fieldHashes.containsKey(className) ? this.fieldHashes.get(className) : new HashMap<>();
    }


    // Private utility methods.

    private void initialize(ClassPool classPool)
    {
        Iterator<String> iterator = classPool.classNames();

        while(iterator.hasNext()) {
            String className = iterator.next();
            classNames.add(className);
            ProgramClass programClass = (ProgramClass)classPool.getClass(className);
            superClassNames.put(className, programClass.getSuperName());

            for (ProgramMethod programMethod : programClass.methods)
            {
                if (!methodHashes.containsKey(className))
                {
                    methodHashes.put(className, new HashMap<>());
                }
                methodHashes.get(className).put(programMethod, hash(programClass, programMethod));
            }

            for (ProgramField programField : programClass.fields)
            {
                if (!fieldHashes.containsKey(className))
                {
                    fieldHashes.put(className, new HashMap<>());
                }
                fieldHashes.get(className).put(programField, hash(programClass, programField));
            }
        }
    }


    private static int hash(Clazz clazz, Method method)
    {
        return hashFnv1a32_UTF8(method.getName(clazz) + "(" + ClassUtil.externalMethodArguments(method.getDescriptor(clazz)) + ")") ;
    }


    private static int hash(Clazz clazz, Field field)
    {
        return hashFnv1a32_UTF8(field.getName(clazz));
    }
}
