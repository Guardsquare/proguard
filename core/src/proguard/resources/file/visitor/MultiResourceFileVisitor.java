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
package proguard.resources.file.visitor;

import proguard.resources.file.ResourceFile;
import proguard.util.ArrayUtil;


/**
 * This ResourceFileVisitor delegates all visits to all ResourceFileVisitor
 * instances in the given list.
 *
 * @author Johan Leys
 */
public class MultiResourceFileVisitor
extends      SimplifiedResourceFileVisitor
implements   ResourceFileVisitor
{
    private ResourceFileVisitor[] resourceFileVisitors;


    public MultiResourceFileVisitor(ResourceFileVisitor... resourceFileVisitors)
    {
        this.resourceFileVisitors = resourceFileVisitors;
    }



    public void addResourceFileVisitor(ResourceFileVisitor visitor)
    {
        resourceFileVisitors = ArrayUtil.add(resourceFileVisitors,
                                             resourceFileVisitors.length,
                                             visitor);
    }


    // Implementations for ResourceFileVisitor.

    @Override
    public void visitAnyResourceFile(ResourceFile resourceFile)
    {
        for (int index = 0; index < resourceFileVisitors.length; index++)
        {
            resourceFile.accept(resourceFileVisitors[index]);
        }
    }
}
