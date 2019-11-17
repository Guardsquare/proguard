/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
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
package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.util.ArrayUtil;

/**
 * This class can add interfaces and class members to a given class.
 * Elements to be added must be filled out beforehand, including their
 * references to the constant pool.
 *
 * @author Eric Lafortune
 */
public class ClassEditor
{
    private static final boolean DEBUG = false;

    private ProgramClass targetClass;


    /**
     * Creates a new ClassEditor that will edit elements in the given
     * target class.
     */
    public ClassEditor(ProgramClass targetClass)
    {
        this.targetClass = targetClass;
    }


    /**
     * Adds the given interface.
     */
    public void addInterface(int interfaceConstantIndex)
    {
        targetClass.u2interfaces =
            ArrayUtil.add(targetClass.u2interfaces,
                          targetClass.u2interfacesCount++,
                          interfaceConstantIndex);
        if (DEBUG)
        {
            System.out.println(targetClass.getName()+": adding interface ["+targetClass.getClassName(interfaceConstantIndex)+"]");
        }
    }

    /**
     * Removes the given interface.
     */
    public void removeInterface(int interfaceConstantIndex)
    {
        if (DEBUG)
        {
            System.out.println(targetClass.getName()+": removing interface ["+targetClass.getClassName(interfaceConstantIndex)+"]");
        }

        ArrayUtil.remove(targetClass.u2interfaces,
                         targetClass.u2interfacesCount--,
                         findInterfaceIndex(interfaceConstantIndex));
    }


    /**
     * Finds the index of the given interface in the target class.
     */

    private int findInterfaceIndex(int interfaceConstantIndex)
    {
        int   interfacesCount = targetClass.u2interfacesCount;
        int[] interfaces      = targetClass.u2interfaces;

        for (int index = 0; index < interfacesCount; index++)
        {
            if (interfaces[index] == interfaceConstantIndex)
            {
                return index;
            }
        }

        return interfacesCount;
    }


    /**
     * Adds the given field.
     */
    public void addField(Field field)
    {
        if (DEBUG)
        {
            System.out.println(targetClass.getName()+": adding field ["+field.getName(targetClass)+" "+field.getDescriptor(targetClass)+"]");
        }

        targetClass.fields =
            (ProgramField[])ArrayUtil.add(targetClass.fields,
                                          targetClass.u2fieldsCount++,
                                          field);
    }


    /**
     * Removes the given field. Note that removing a field that is still being
     * referenced can cause unpredictable effects.
     */
    public void removeField(Field field)
    {
        if (DEBUG)
        {
            System.out.println(targetClass.getName()+": removing field ["+field.getName(targetClass)+" "+field.getDescriptor(targetClass)+"]");
        }

        ArrayUtil.remove(targetClass.fields,
                         targetClass.u2fieldsCount--,
                         findFieldIndex(field));
    }


    /**
     * Finds the index of the given field in the target class.
     */

    private int findFieldIndex(Field field)
    {
        int     fieldsCount = targetClass.u2fieldsCount;
        Field[] fields      = targetClass.fields;

        for (int index = 0; index < fieldsCount; index++)
        {
            if (fields[index].equals(field))
            {
                return index;
            }
        }

        return fieldsCount;
    }


    /**
     * Adds the given method.
     */
    public void addMethod(Method method)
    {
        if (DEBUG)
        {
            System.out.println(targetClass.getName()+": adding method ["+method.getName(targetClass)+method.getDescriptor(targetClass)+"]");
        }

        targetClass.methods =
            (ProgramMethod[])ArrayUtil.add(targetClass.methods,
                                           targetClass.u2methodsCount++,
                                           method);
    }


    /**
     * Removes the given method. Note that removing a method that is still being
     * referenced can cause unpredictable effects.
     */
    public void removeMethod(Method method)
    {
        if (DEBUG)
        {
            System.out.println(targetClass.getName()+": removing method ["+method.getName(targetClass)+method.getDescriptor(targetClass)+"]");
        }

        ArrayUtil.remove(targetClass.methods,
                         targetClass.u2methodsCount--,
                         findMethodIndex(method));
    }


    /**
     * Finds the index of the given method in the target class.
     */

    private int findMethodIndex(Method method)
    {
        int      methodsCount = targetClass.u2methodsCount;
        Method[] methods      = targetClass.methods;

        for (int index = 0; index < methodsCount; index++)
        {
            if (methods[index].equals(method))
            {
                return index;
            }
        }

        return methodsCount;
    }
}
