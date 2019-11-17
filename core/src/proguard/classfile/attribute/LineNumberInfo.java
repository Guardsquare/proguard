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
package proguard.classfile.attribute;

/**
 * Representation of an Line Number table entry.
 *
 * @author Eric Lafortune
 */
public class LineNumberInfo
{
    public int u2startPC;
    public int u2lineNumber;


    /**
     * Creates an uninitialized LineNumberInfo.
     */
    public LineNumberInfo()
    {
    }


    /**
     * Creates an initialized LineNumberInfo.
     */
    public LineNumberInfo(int u2startPC, int u2lineNumber)
    {
        this.u2startPC    = u2startPC;
        this.u2lineNumber = u2lineNumber;
    }


    /**
     * Returns a description of the source of the line, if known, or null
     * otherwise. Standard line number entries don't contain information
     * about their source; it is assumed to be the same source file.
     */
    public String getSource()
    {
        return null;
    }
}
