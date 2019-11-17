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
package proguard.classfile.kotlin.asserter;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinMetadata;

/**
 * A class to represent errors in the Kotlin Metadata
 */
public abstract class KotlinMetadataError
{
    public final Clazz          clazz;
    public final KotlinMetadata kotlinMetadata;


    public KotlinMetadataError(Clazz clazz, KotlinMetadata kotlinMetadata)
    {
        this.clazz          = clazz;
        this.kotlinMetadata = kotlinMetadata;
    }


    public abstract String errorDescription();


    public String toString()
    {
        return "Kotlin Metadata Error in class " + clazz.getName() + ": " + errorDescription();
    }
}
