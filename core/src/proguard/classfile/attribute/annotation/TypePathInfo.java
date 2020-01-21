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
package proguard.classfile.attribute.annotation;

/**
 * Representation of a path element in a type annotation.
 *
 * @author Eric Lafortune
 */
public class TypePathInfo
{
    public static final int KIND_Array             = 0;
    public static final int KIND_Nested            = 1;
    public static final int KIND_TypeArgumentBound = 2;
    public static final int KIND_TypeArgument      = 3;


    public int u1typePathKind;
    public int u1typeArgumentIndex;


    /**
     * Creates an uninitialized TypePathInfo.
     */
    public TypePathInfo()
    {
    }


    /**
     * Creates an initialized TypePathInfo.
     */
    public TypePathInfo(int u1typePathKind, int u1typeArgumentIndex)
    {
        this.u1typePathKind      = u1typePathKind;
        this.u1typeArgumentIndex = u1typeArgumentIndex;
    }
}
