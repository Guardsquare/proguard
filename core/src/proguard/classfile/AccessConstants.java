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
 * Access flags for classes, fields, methods, parameters, and modules.
 *
 * @author Eric Lafortune
 */
public class AccessConstants
{
    public static final int PUBLIC       = 0x0001;
    public static final int PRIVATE      = 0x0002;
    public static final int PROTECTED    = 0x0004;
    public static final int STATIC       = 0x0008;
    public static final int FINAL        = 0x0010;
    public static final int SUPER        = 0x0020;
    public static final int SYNCHRONIZED = 0x0020;
    public static final int VOLATILE     = 0x0040;
    public static final int TRANSIENT    = 0x0080;
    public static final int BRIDGE       = 0x0040;
    public static final int VARARGS      = 0x0080;
    public static final int NATIVE       = 0x0100;
    public static final int INTERFACE    = 0x0200;
    public static final int ABSTRACT     = 0x0400;
    public static final int STRICT       = 0x0800;
    public static final int SYNTHETIC    = 0x1000;
    public static final int ANNOTATION   = 0x2000;
    public static final int ENUM         = 0x4000;
    public static final int MANDATED     = 0x8000;
    public static final int MODULE       = 0x8000;
    public static final int OPEN         = 0x0020;
    public static final int TRANSITIVE   = 0x0020;
    public static final int STATIC_PHASE = 0x0040;

    // Custom flags introduced by ProGuard.
    public static final int RENAMED             = 0x00010000; // Marks whether a class or class member has been renamed.
    public static final int REMOVED_METHODS     = 0x00020000; // Marks whether a class has (at least one) methods removed.
    public static final int REMOVED_FIELDS      = 0x00040000; // Marks whether a class has (at least one) fields removed.

    public static final int VALID_FLAGS_CLASS     = PUBLIC       |
                                                    FINAL        |
                                                    SUPER        |
                                                    INTERFACE    |
                                                    ABSTRACT     |
                                                    SYNTHETIC    |
                                                    ANNOTATION   |
                                                    MODULE       |
                                                    ENUM;
    public static final int VALID_FLAGS_FIELD     = PUBLIC       |
                                                    PRIVATE      |
                                                    PROTECTED    |
                                                    STATIC       |
                                                    FINAL        |
                                                    VOLATILE     |
                                                    TRANSIENT    |
                                                    SYNTHETIC    |
                                                    ENUM;
    public static final int VALID_FLAGS_METHOD    = PUBLIC       |
                                                    PRIVATE      |
                                                    PROTECTED    |
                                                    STATIC       |
                                                    FINAL        |
                                                    SYNCHRONIZED |
                                                    BRIDGE       |
                                                    VARARGS      |
                                                    NATIVE       |
                                                    ABSTRACT     |
                                                    STRICT       |
                                                    SYNTHETIC;
    public static final int VALID_FLAGS_PARAMETER = FINAL        |
                                                    SYNTHETIC    |
                                                    MANDATED;
    public static final int VALID_FLAGS_MODULE    = OPEN         |
                                                    SYNTHETIC    |
                                                    MANDATED;
    public static final int VALID_FLAGS_REQUIRES  = TRANSITIVE   |
                                                    STATIC_PHASE |
                                                    SYNTHETIC    |
                                                    MANDATED;
    public static final int VALID_FLAGS_EXPORTS   = SYNTHETIC    |
                                                    MANDATED;
    public static final int VALID_FLAGS_OPENS     = SYNTHETIC    |
                                                    MANDATED;
}
