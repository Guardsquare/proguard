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
package proguard.classfile;

/**
 * Constant characters that are part of primitive and non-primitive type
 * descriptors.
 *
 * @author Eric Lafortune
 */
public class TypeConstants
{
    public static final char VOID                     = 'V';
    public static final char BOOLEAN                  = 'Z';
    public static final char BYTE                     = 'B';
    public static final char CHAR                     = 'C';
    public static final char SHORT                    = 'S';
    public static final char INT                      = 'I';
    public static final char LONG                     = 'J';
    public static final char FLOAT                    = 'F';
    public static final char DOUBLE                   = 'D';
    public static final char CLASS_START              = 'L';
    public static final char CLASS_END                = ';';
    public static final char ARRAY                    = '[';

    public static final char GENERIC_VARIABLE_START   = 'T';
    public static final char GENERIC_START            = '<';
    public static final char GENERIC_BOUND            = ':';
    public static final char GENERIC_END              = '>';

    public static final char PACKAGE_SEPARATOR        = '/';
    public static final char INNER_CLASS_SEPARATOR    = '$';
    public static final char SPECIAL_CLASS_CHARACTER  = '-';
    public static final char SPECIAL_MEMBER_SEPARATOR = '$';

    public static final char METHOD_ARGUMENTS_OPEN    = '(';
    public static final char METHOD_ARGUMENTS_CLOSE   = ')';
}
