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
package proguard.classfile.visitor;

import proguard.classfile.ClassPool;
import proguard.util.ArrayUtil;


/**
 * This {@link ClassPoolVisitor} delegates all visits to each {@link ClassPoolVisitor}
 * in a given list.
 *
 * @author Eric Lafortune
 */
public class MultiClassPoolVisitor implements ClassPoolVisitor
{
    private ClassPoolVisitor[] classPoolVisitors;
    private int                classPoolVisitorCount;


    public MultiClassPoolVisitor()
    {
        this.classPoolVisitors = new ClassPoolVisitor[16];
    }


    public MultiClassPoolVisitor(ClassPoolVisitor... classPoolVisitors)
    {
        this.classPoolVisitors     = classPoolVisitors;
        this.classPoolVisitorCount = classPoolVisitors.length;
    }


    public void addClassPoolVisitor(ClassPoolVisitor classPoolVisitor)
    {
        classPoolVisitors =
            ArrayUtil.add(classPoolVisitors,
                          classPoolVisitorCount++,
                          classPoolVisitor);
    }


    // Implementations for ClassPoolVisitor.

    public void visitClassPool(ClassPool classPool)
    {
        for (int index = 0; index < classPoolVisitorCount; index++)
        {
            classPoolVisitors[index].visitClassPool(classPool);
        }
    }
}
