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
package proguard.util;

import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.visitor.*;
import proguard.resources.file.visitor.ResourceFileVisitor;

/**
 * This visitor clears the specified processing flags of the {@link Processable}
 * instances that it visits.
 *
 * @author Johan Leys
 */
public class ProcessingFlagCleaner
extends      SimplifiedProcessableVisitor
implements   ClassVisitor,
             MemberVisitor,
             AttributeVisitor,
             ResourceFileVisitor
{
    private final int processingFlags;


    /**
     * Creates a new ProcessingFlagCleaner.
     *
     * @param processingFlags the processing flags to be set.
     */
    public ProcessingFlagCleaner(int processingFlags)
    {
        this.processingFlags = processingFlags;
    }


    // Implementations for SimplifiedProcessableVisitor.

    @Override
    public void visitAnyProcessable(Processable processable)
    {
        processable.setProcessingFlags(processable.getProcessingFlags() & ~processingFlags);
    }
}
