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
package proguard.classfile.editor;

/**
 * Typical sizes, counts, and lengths for elements in class files.
 * These can be useful as initial sizes when creating or editing them.
 *
 * @author Eric Lafortune
 */
public class ClassEstimates
{
    public static final int TYPICAL_CONSTANT_POOL_SIZE               = 256;
    public static final int TYPICAL_FIELD_COUNT                      = 64;
    public static final int TYPICAL_METHOD_COUNT                     = 64;
    public static final int TYPICAL_PARAMETER_COUNT                  = 32;
    public static final int TYPICAL_CODE_LENGTH                      = 8096;
    public static final int TYPICAL_LINE_NUMBER_TABLE_LENGTH         = 1024;
    public static final int TYPICAL_EXCEPTION_TABLE_LENGTH           = 16;
    public static final int TYPICAL_VARIABLES_SIZE                   = 64;
    public static final int TYPICAL_STACK_SIZE                       = 16;
    public static final int TYPICAL_BOOTSTRAP_METHODS_ATTRIBUTE_SIZE = 16;
}
