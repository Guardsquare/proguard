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
package proguard.classfile.attribute.annotation.target;

/**
 * Representation of an local variable target table entry.
 *
 * @author Eric Lafortune
 */
public class LocalVariableTargetElement
{
    public int u2startPC;
    public int u2length;
    public int u2index;

    /**
     * Creates an uninitialized LocalVariableTargetElement.
     */
    public LocalVariableTargetElement()
    {
    }


    /**
     * Creates an initialized LocalVariableTargetElement.
     */
    public LocalVariableTargetElement(int u2startPC,
                                      int u2length,
                                      int u2index)
    {
        this.u2startPC = u2startPC;
        this.u2length  = u2length;
        this.u2index   = u2index;
    }
}
