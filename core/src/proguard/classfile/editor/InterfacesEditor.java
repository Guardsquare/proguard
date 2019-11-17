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

import proguard.classfile.ProgramClass;
import proguard.util.ArrayUtil;

/**
 * This class can add and delete interfaces to and from classes. References to
 * the constant pool must be filled out beforehand.
 *
 * @author Eric Lafortune
 */
public class InterfacesEditor
{
    private final ProgramClass targetClass;


    /**
     * Creates a new InterfacesEditor that will edit interfaces in the given
     * target class.
     */
    public InterfacesEditor(ProgramClass targetClass)
    {
        this.targetClass = targetClass;
    }


    /**
     * Adds the specified interface to the target class, if it isn't present yet.
     */
    public void addInterface(int interfaceConstantIndex)
    {
        // Is the interface not yet present?
        if (findInterfaceIndex(interfaceConstantIndex) < 0)
        {
            // Append the interface.
            targetClass.u2interfaces =
                ArrayUtil.add(targetClass.u2interfaces,
                              targetClass.u2interfacesCount++,
                              interfaceConstantIndex);
        }
    }


    /**
     * Deletes the given interface from the target class, if it is present.
     */
    public void deleteInterface(int interfaceConstantIndex)
    {
        // Is the interface already present?
        int interfaceIndex = findInterfaceIndex(interfaceConstantIndex);
        if (interfaceIndex >= 0)
        {
            int   interfacesCount = --targetClass.u2interfacesCount;
            int[] interfaces      = targetClass.u2interfaces;

            // Shift the other interfaces in the array.
            for (int index = interfaceIndex; index < interfacesCount; index++)
            {
                interfaces[index] = interfaces[index + 1];
            }

            // Clear the remaining entry in the array.
            interfaces[interfacesCount] = 0;
        }
    }


    // Small utility methods.

    /**
     * Finds the index of the specified interface in the list of interfaces of
     * the target class.
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

        return -1;
    }
}