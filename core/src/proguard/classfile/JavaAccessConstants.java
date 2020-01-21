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
 * Access modifiers for classes, fields, methods, parameters, and modules
 * in Java source code.
 *
 * @see AccessConstants
 *
 * @author Eric Lafortune
 */
public class JavaAccessConstants
{
    public static final String PUBLIC       = "public";
    public static final String PRIVATE      = "private";
    public static final String PROTECTED    = "protected";
    public static final String STATIC       = "static";
    public static final String FINAL        = "final";
    public static final String SUPER        = "super";
    public static final String SYNCHRONIZED = "synchronized";
    public static final String VOLATILE     = "volatile";
    public static final String TRANSIENT    = "transient";
    public static final String BRIDGE       = "bridge";
    public static final String VARARGS      = "varargs";
    public static final String NATIVE       = "native";
    public static final String INTERFACE    = "interface";
    public static final String ABSTRACT     = "abstract";
    public static final String STRICT       = "strictfp";
    public static final String SYNTHETIC    = "synthetic";
    public static final String ANNOTATION   = "@";
    public static final String ENUM         = "enum";
    public static final String MANDATED     = "mandated";
//  public static final String CONSTRUCTOR  = "constructor";
    public static final String MODULE       = "module";
    public static final String OPEN         = "open";
    public static final String TRANSITIVE   = "transitive";
    public static final String STATIC_PHASE = "static_phase";
}
