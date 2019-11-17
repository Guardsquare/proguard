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

import proguard.classfile.attribute.*;
import proguard.util.ArrayUtil;

/**
 * This class can add/remove bootstrap methods to/from a given bootstrap methods
 * attribute. Bootstrap methods to be added must have been filled out beforehand.
 *
 * @author Eric Lafortune
 */
public class BootstrapMethodsAttributeEditor
{
    private BootstrapMethodsAttribute targetBootstrapMethodsAttribute;


    /**
     * Creates a new BootstrapMethodsAttributeEditor that will edit bootstrap
     * methods in the given bootstrap methods attribute.
     */
    public BootstrapMethodsAttributeEditor(BootstrapMethodsAttribute targetBootstrapMethodsAttribute)
    {
        this.targetBootstrapMethodsAttribute = targetBootstrapMethodsAttribute;
    }


    /**
     * Adds a given bootstrap method to the bootstrap methods attribute.
     * @return the index of the bootstrap method.
     */
    public int addBootstrapMethodInfo(BootstrapMethodInfo bootstrapMethodInfo)
    {
        targetBootstrapMethodsAttribute.bootstrapMethods =
            ArrayUtil.add(targetBootstrapMethodsAttribute.bootstrapMethods,
                          targetBootstrapMethodsAttribute.u2bootstrapMethodsCount,
                          bootstrapMethodInfo);

        return targetBootstrapMethodsAttribute.u2bootstrapMethodsCount++;
    }


    /**
     * Removes the given bootstrap method from the bootstrap method attribute.
     */
    public void removeBootstrapMethodInfo(BootstrapMethodInfo bootstrapMethodInfo)
    {
        ArrayUtil.remove(targetBootstrapMethodsAttribute.bootstrapMethods,
                         targetBootstrapMethodsAttribute.u2bootstrapMethodsCount--,
                         findBootstrapMethodInfoIndex(bootstrapMethodInfo));
    }


    /**
     * Finds the index of the given bootstrap method info in the target attribute.
     */
    private int findBootstrapMethodInfoIndex(BootstrapMethodInfo bootstrapMethodInfo)
    {
        int                   methodsCount = targetBootstrapMethodsAttribute.u2bootstrapMethodsCount;
        BootstrapMethodInfo[] methodInfos  = targetBootstrapMethodsAttribute.bootstrapMethods;

        for (int index = 0; index < methodsCount; index++)
        {
            if (methodInfos[index].equals(bootstrapMethodInfo))
            {
                return index;
            }
        }

        return methodsCount;
    }
}