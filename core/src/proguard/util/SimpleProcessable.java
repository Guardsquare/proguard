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
 * This class provides a straightforward implementation of the Processable
 * interface.
 *
 * @author Eric Lafortune
 */
public class SimpleProcessable
implements   Processable
{
    public int    processingFlags;
    public Object processingInfo;


    /**
     * Creates an uninitialized SimpleProcessable.
     */
    public SimpleProcessable() {}


    /**
     * Creates an initialized SimpleProcessable.
     */
    public SimpleProcessable(int    processingFlags,
                             Object processingInfo)
    {
        this.processingFlags = processingFlags;
        this.processingInfo  = processingInfo;
    }

    
    // Implementations for Processable.

    @Override
    public int getProcessingFlags()
    {
        return processingFlags;
    }


    @Override
    public void setProcessingFlags(int processingFlags)
    {
        this.processingFlags = processingFlags;
    }


    @Override
    public Object getProcessingInfo()
    {
        return processingInfo;
    }


    @Override
    public void setProcessingInfo(Object processingInfo)
    {
        this.processingInfo = processingInfo;
    }
}
