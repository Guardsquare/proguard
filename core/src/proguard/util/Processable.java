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
 * Base interface for entities that need flags and/or additional information
 * when they are processed, typically by visitor classes.
 *
 * The processing flags provide simple boolean markers. In ProGuard, they
 * mark entities to be kept across processing steps, for example.
 *
 * The processing info provides any more general information. In ProGuard,
 * the contain relatively short-lived information inside processing steps.
 *
 * @author Johan Leys
 * @Author Eric Lafortune
 */
public interface Processable
{
    /**
     * Sets the processing flags.
     */
    public void setProcessingFlags(int processingFlags);


    /**
     * Returns the processing flags.
     */
    public int getProcessingFlags();


    /**
     * Sets the processing information.
     */
    public void setProcessingInfo(Object processingInfo);


    /**
     * Gets the processing information.
     */
    public Object getProcessingInfo();
}
