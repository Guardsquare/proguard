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
 * This class can add/remove bootstrap methods to/from a given inner classes
 * attribute. Inner classes to be added must have been filled out beforehand.
 *
 * @author Thomas Neidhart
 */
public class InnerClassesAttributeEditor
{
    private InnerClassesAttribute targetInnerClassesAttribute;


    /**
     * Creates a new InnerClassesAttributeEditor that will edit inner
     * classes in the given inner classes attribute.
     */
    public InnerClassesAttributeEditor(InnerClassesAttribute targetInnerClassesAttribute)
    {
        this.targetInnerClassesAttribute = targetInnerClassesAttribute;
    }


    /**
     * Adds a given inner class to the inner classes attribute.
     * @return the index of the inner class.
     */
    public int addInnerClassesInfo(InnerClassesInfo innerClassesInfo)
    {
        targetInnerClassesAttribute.classes =
            ArrayUtil.add(targetInnerClassesAttribute.classes,
                          targetInnerClassesAttribute.u2classesCount,
                          innerClassesInfo);

        return targetInnerClassesAttribute.u2classesCount++;
    }


    /**
     * Removes the given inner class from the inner classes attribute.
     */
    public void removeInnerClassesInfo(InnerClassesInfo innerClassesInfo)
    {
        ArrayUtil.remove(targetInnerClassesAttribute.classes,
                         targetInnerClassesAttribute.u2classesCount--,
                         findInnerClassesInfoIndex(innerClassesInfo));
    }


    /**
     * Finds the index of the given bootstrap method info in the target attribute.
     */
    private int findInnerClassesInfoIndex(InnerClassesInfo innerClassesInfo)
    {
        int                innerClassesCount = targetInnerClassesAttribute.u2classesCount;
        InnerClassesInfo[] innerClassesInfos = targetInnerClassesAttribute.classes;

        for (int index = 0; index < innerClassesCount; index++)
        {
            if (innerClassesInfos[index].equals(innerClassesInfo))
            {
                return index;
            }
        }

        return innerClassesCount;
    }
}