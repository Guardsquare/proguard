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

/**
 * Base interface for entities that need flags when they are processed.
 * The processing flags provide details on how the entity should be
 * processed, possibly across processing steps.
 *
 * @see ProcessingFlags
 *
 * @author Johan Leys
 */
public interface Processable
{
    /**
     * Returns the processing flags for this entity.
     */
    public int getProcessingFlags();


    /**
     * Sets the processing flags for this entity.
     *
     * @param processingFlags the processing flags to be set.
     */
    public void setProcessingFlags(int processingFlags);
}
