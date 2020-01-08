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
 * descriptors in Java source code.
 *
 * @see TypeConstants
 *
 * @author Eric Lafortune
 */
public class JavaTypeConstants
{
    public static final String VOID    = "void";
    public static final String BOOLEAN = "boolean";
    public static final String BYTE    = "byte";
    public static final String CHAR    = "char";
    public static final String SHORT   = "short";
    public static final String INT     = "int";
    public static final String FLOAT   = "float";
    public static final String LONG    = "long";
    public static final String DOUBLE  = "double";
    public static final String ARRAY   = "[]";

    public static final char PACKAGE_SEPARATOR        = '.';
    public static final char INNER_CLASS_SEPARATOR    = '.';
    public static final char SPECIAL_CLASS_CHARACTER  = '-';
    public static final char SPECIAL_MEMBER_SEPARATOR = '$';

    public static final char METHOD_ARGUMENTS_OPEN      = '(';
    public static final char METHOD_ARGUMENTS_CLOSE     = ')';
    public static final char METHOD_ARGUMENTS_SEPARATOR = ',';
}
