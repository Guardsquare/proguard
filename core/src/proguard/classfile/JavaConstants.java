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
 * External names and descriptors of common classes, fields, and methods from
 * the Java runtime.
 *
 * @see ClassConstants
 *
 * @author Eric Lafortune
 */
public class JavaConstants
{
    public static final String JAVA_FILE_EXTENSION = ".java";

    public static final String TYPE_JAVA_LANG_OBJECT                = "java.lang.Object";
    public static final String TYPE_JAVA_LANG_STRING                = "java.lang.String";
    public static final String TYPE_JAVA_LANG_CLASS                 = "java.lang.Class";
    public static final String TYPE_JAVA_LANG_ENUM                  = "java.lang.Enum";
    public static final String TYPE_JAVA_LANG_ANNOTATION_ANNOTATION = "java.lang.annotation.Annotation";
    public static final String TYPE_JAVA_LANG_REFLECT_ARRAY         = "java.lang.reflect.Array";
    public static final String TYPE_JAVA_LANG_INVOKE_METHODHANDLE   = "java.lang.invoke.MethodHandle";
    public static final String TYPE_JAVA_LANG_INVOKE_METHODTYPE     = "java.lang.invoke.MethodType";
    public static final String PACKAGE_JAVA_LANG                    = "java.lang.";
}
