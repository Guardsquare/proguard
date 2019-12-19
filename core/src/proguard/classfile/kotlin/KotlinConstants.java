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
package proguard.classfile.kotlin;

import proguard.classfile.*;
import proguard.classfile.editor.ClassBuilder;
import proguard.util.ProcessingFlags;

import java.util.*;

import static proguard.classfile.ClassConstants.ACC_PUBLIC;

public class KotlinConstants
{
    public static final int METADATA_KIND_CLASS                   = 1;
    public static final int METADATA_KIND_FILE_FACADE             = 2;
    public static final int METADATA_KIND_SYNTHETIC_CLASS         = 3;
    public static final int METADATA_KIND_MULTI_FILE_CLASS_FACADE = 4;
    public static final int METADATA_KIND_MULTI_FILE_CLASS_PART   = 5;

    public static final String NAME_KOTLIN_METADATA                  = "kotlin/Metadata";
    public static final String NAME_KOTLIN_COROUTINES_DEBUG_METADATA = "kotlin/coroutines/jvm/internal/DebugMetadata";
    public static final String TYPE_KOTLIN_METADATA                  = "Lkotlin/Metadata;";
    public static final String TYPE_KOTLIN_JVM_JVMNAME               = "Lkotlin/jvm/JvmName;";

    public static final String DEFAULT_METHOD_SUFFIX                 = "$default";
    public static final String DEFAULT_IMPLEMENTATIONS_SUFFIX        = "$DefaultImpls";
    public static final String WHEN_MAPPINGS_SUFFIX                  = "$WhenMappings";

    public static final String KOTLIN_INTRINSICS_CLASS               = "kotlin/jvm/internal/Intrinsics";

    private static final String[] KOTLIN_MAPPED_TYPES = {
        // Primitives
        "kotlin/Byte",
        "kotlin/Short",
        "kotlin/Int",
        "kotlin/Long",
        "kotlin/Char",
        "kotlin/Float",
        "kotlin/Double",
        "kotlin/Boolean",

        // Non-primitives
        "kotlin/Unit",
        "kotlin/Nothing",
        "kotlin/Any",
        "kotlin/Cloneable",
        "kotlin/Comparable",
        "kotlin/Enum",
        "kotlin/Annotation",
        "kotlin/CharSequence",
        "kotlin/String",
        "kotlin/Number",
        "kotlin/Throwable",

        // Collection types
        "kotlin/collections/Iterator",
        "kotlin/collections/Iterable",
        "kotlin/collections/Collection",
        "kotlin/collections/Set",
        "kotlin/collections/List",
        "kotlin/collections/ListIterator",
        "kotlin/collections/Map",
        "kotlin/collections/Map$Entry",
        "kotlin/collections/MutableIterator",
        "kotlin/collections/MutableIterable",
        "kotlin/collections/MutableCollection",
        "kotlin/collections/MutableSet",
        "kotlin/collections/MutableList",
        "kotlin/collections/MutableListIterator",
        "kotlin/collections/MutableMap",
        "kotlin/collections/MutableMap$MutableEntry",

        // Arrays
        "kotlin/Array",
        "kotlin/ByteArray",
        "kotlin/ShortArray",
        "kotlin/IntArray",
        "kotlin/LongArray",
        "kotlin/CharArray",
        "kotlin/FloatArray",
        "kotlin/DoubleArray",
        "kotlin/BooleanArray",

        // Primitive companions
        "kotlin/Byte$Companion",
        "kotlin/Short$Companion",
        "kotlin/Int$Companion",
        "kotlin/Long$Companion",
        "kotlin/Char$Companion",
        "kotlin/Float$Companion",
        "kotlin/Double$Companion",
        "kotlin/Boolean$Companion",

        // Non-primitive companions
        "kotlin/String$Companion",

        // kotlin/Function0..kotlin/FunctionN
        // kotlin/reflect/KFunction0..kotlin/reflect/KFunctionN
        // Created dynamically when encountered
        // See Kotlin documentation for more information:
        //  https://github.com/JetBrains/kotlin/blob/master/spec-docs/function-types.md

        ""
    };

    private static final Map<String, String> javaToKotlinTypeMap = new HashMap<>();

    static
    {
       javaToKotlinTypeMap.put("java/lang/Byte",      "kotlin/Byte");
       javaToKotlinTypeMap.put("java/lang/Short",     "kotlin/Short");
       javaToKotlinTypeMap.put("java/lang/Integer",   "kotlin/Int");
       javaToKotlinTypeMap.put("java/lang/Long",      "kotlin/Long");
       javaToKotlinTypeMap.put("java/lang/Character", "kotlin/Char");
       javaToKotlinTypeMap.put("java/lang/Float",     "kotlin/Float");
       javaToKotlinTypeMap.put("java/lang/Double",    "kotlin/Double");
       javaToKotlinTypeMap.put("java/lang/Boolean",   "kotlin/Boolean");

       javaToKotlinTypeMap.put("java/lang/Object",       "kotlin/Any");
       javaToKotlinTypeMap.put("java/lang/Cloneable",    "kotlin/Cloneable");
       javaToKotlinTypeMap.put("java/lang/Comparable",   "kotlin/Comparable");
       javaToKotlinTypeMap.put("java/lang/Enum",         "kotlin/Enum");
       javaToKotlinTypeMap.put("java/lang/Annotation",   "kotlin/Annotation");
       javaToKotlinTypeMap.put("java/lang/CharSequence", "kotlin/CharSequence");
       javaToKotlinTypeMap.put("java/lang/String",       "kotlin/String");
       javaToKotlinTypeMap.put("java/lang/Number",       "kotlin/Number");
       javaToKotlinTypeMap.put("java/lang/Throwable",    "kotlin/Throwable");

        javaToKotlinTypeMap.put("java/util/Iterator",     "kotlin/collections/Iterator");
        javaToKotlinTypeMap.put("java/lang/Iterable",     "kotlin/collections/Iterable");
        javaToKotlinTypeMap.put("java/util/Collection",   "kotlin/collections/Collection");
        javaToKotlinTypeMap.put("java/util/Set",          "kotlin/collections/Set");
        javaToKotlinTypeMap.put("java/util/List",         "kotlin/collections/List");
        javaToKotlinTypeMap.put("java/util/ListIterator", "kotlin/collections/ListIterator");
        javaToKotlinTypeMap.put("java/util/Map",          "kotlin/collections/Map");
        javaToKotlinTypeMap.put("java/util/Map$Entry",    "kotlin/collections/Map$Entry");
    }

    public static final ClassPool dummyClassPool = new ClassPool()
    {
        {
            for (String dummyType : KOTLIN_MAPPED_TYPES)
            {
                addClass(createDummyClass(dummyType));
            }
        }

        @Override
        public Clazz getClass(String className)
        {
            Clazz clazz = super.getClass(className);
            if (clazz == null && (className.startsWith("kotlin/Function") || className.startsWith("kotlin/reflect/KFunction")))
            {
                clazz = createDummyClass(className);
                super.addClass(clazz);
            }

            return clazz;
        }
    };

    /**
     * Get the Kotlin equivalent of a Java type.
     *
     * @param javaType the class of the Java type.
     *
     * @return Kotlin type class or the original Java type class if it doesn't have a Kotlin equivalent
     */
    public static Clazz getKotlinType(Clazz javaType)
    {
        String javaTypeName = javaType.getName();
        if (javaToKotlinTypeMap.containsKey(javaTypeName))
        {
            return dummyClassPool.getClass(javaToKotlinTypeMap.get(javaTypeName));
        }
        else
        {
            return javaType;
        }
    }

    // Small helper methods.

    private static ProgramClass createDummyClass(String name)
    {
        return new ClassBuilder(
            ACC_PUBLIC,
            name,
            null,
            ProcessingFlags.DONT_OBFUSCATE |
            ProcessingFlags.DONT_OPTIMIZE |
            ProcessingFlags.DONT_SHRINK)
            .getProgramClass();
    }
}
