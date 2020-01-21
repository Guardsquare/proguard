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
 * Internal names and descriptors of common classes, fields, and methods from
 * the Java runtime.
 *
 * @author Eric Lafortune
 */
public class ClassConstants
{
    public static final String CLASS_FILE_EXTENSION   = ".class";

    public static final String PACKAGE_JAVA_LANG                            = "java/lang/";
    public static final String NAME_JAVA_LANG_OBJECT                        = "java/lang/Object";
    public static final String TYPE_JAVA_LANG_OBJECT                        = "Ljava/lang/Object;";
    public static final String NAME_JAVA_LANG_CLONEABLE                     = "java/lang/Cloneable";
    public static final String NAME_JAVA_LANG_THROWABLE                     = "java/lang/Throwable";
    public static final String NAME_JAVA_LANG_EXCEPTION                     = "java/lang/Exception";
    public static final String NAME_JAVA_LANG_UNSUPPORTED_OP_EXCEPTION      = "java/lang/UnsupportedOperationException";
    public static final String NAME_JAVA_LANG_NUMBER_FORMAT_EXCEPTION       = "java/lang/NumberFormatException";
    public static final String NAME_JAVA_LANG_CLASS                         = "java/lang/Class";
    public static final String TYPE_JAVA_LANG_CLASS                         = "Ljava/lang/Class;";
    public static final String NAME_JAVA_LANG_CLASS_LOADER                  = "java/lang/ClassLoader";
    public static final String NAME_JAVA_LANG_ENUM                          = "java/lang/Enum";
    public static final String NAME_JAVA_LANG_ANNOTATION_ANNOTATION         = "java/lang/annotation/Annotation";
    public static final String NAME_JAVA_LANG_STRING                        = "java/lang/String";
    public static final String TYPE_JAVA_LANG_STRING                        = "Ljava/lang/String;";
    public static final String NAME_JAVA_LANG_STRING_BUFFER                 = "java/lang/StringBuffer";
    public static final String NAME_JAVA_LANG_STRING_BUILDER                = "java/lang/StringBuilder";
    public static final String NAME_JAVA_LANG_INVOKE_METHOD_HANDLE          = "java/lang/invoke/MethodHandle";
    public static final String NAME_JAVA_LANG_INVOKE_METHOD_TYPE            = "java/lang/invoke/MethodType";
    public static final String NAME_JAVA_LANG_INVOKE_STRING_CONCAT_FACTORY  = "java/lang/invoke/StringConcatFactory";
    public static final String NAME_JAVA_LANG_VOID                          = "java/lang/Void";
    public static final String NAME_JAVA_LANG_BOOLEAN                       = "java/lang/Boolean";
    public static final String TYPE_JAVA_LANG_BOOLEAN                       = "Ljava/lang/Boolean;";
    public static final String NAME_JAVA_LANG_BYTE                          = "java/lang/Byte";
    public static final String NAME_JAVA_LANG_SHORT                         = "java/lang/Short";
    public static final String NAME_JAVA_LANG_CHARACTER                     = "java/lang/Character";
    public static final String NAME_JAVA_LANG_INTEGER                       = "java/lang/Integer";
    public static final String NAME_JAVA_LANG_LONG                          = "java/lang/Long";
    public static final String NAME_JAVA_LANG_FLOAT                         = "java/lang/Float";
    public static final String NAME_JAVA_LANG_DOUBLE                        = "java/lang/Double";
    public static final String NAME_JAVA_LANG_MATH                          = "java/lang/Math";
    public static final String NAME_JAVA_LANG_SYSTEM                        = "java/lang/System";
    public static final String NAME_JAVA_LANG_RUNTIME                       = "java/lang/Runtime";
    public static final String NAME_JAVA_LANG_REFLECT_ARRAY                 = "java/lang/reflect/Array";
    public static final String NAME_JAVA_LANG_REFLECT_FIELD                 = "java/lang/reflect/Field";
    public static final String NAME_JAVA_LANG_REFLECT_METHOD                = "java/lang/reflect/Method";
    public static final String NAME_JAVA_LANG_REFLECT_CONSTRUCTOR           = "java/lang/reflect/Constructor";
    public static final String NAME_JAVA_LANG_REFLECT_ACCESSIBLE_OBJECT     = "java/lang/reflect/AccessibleObject";
    public static final String NAME_JAVA_IO_SERIALIZABLE                    = "java/io/Serializable";
    public static final String NAME_JAVA_IO_BYTE_ARRAY_INPUT_STREAM         = "java/io/ByteArrayInputStream";
    public static final String NAME_JAVA_IO_DATA_INPUT_STREAM               = "java/io/DataInputStream";
    public static final String NAME_JAVA_IO_INPUT_STREAM                    = "java/io/InputStream";
    public static final String NAME_JAVA_NIO_BUFFER                         = "java/nio/Buffer";
    public static final String NAME_JAVA_NIO_BYTE_BUFFER                    = "java/nio/ByteBuffer";
    public static final String NAME_JAVA_NIO_CHAR_BUFFER                    = "java/nio/CharBuffer";
    public static final String NAME_JAVA_NIO_SHORT_BUFFER                   = "java/nio/ShortBuffer";
    public static final String NAME_JAVA_NIO_INT_BUFFER                     = "java/nio/IntBuffer";
    public static final String NAME_JAVA_NIO_LONG_BUFFER                    = "java/nio/LongBuffer";
    public static final String NAME_JAVA_NIO_FLOAT_BUFFER                   = "java/nio/FloatBuffer";
    public static final String NAME_JAVA_NIO_DOUBLE_BUFFER                  = "java/nio/DoubleBuffer";
    public static final String NAME_JAVA_NIO_CHANNELS_CHANNELS              = "java/nio/channels/Channels";
    public static final String NAME_JAVA_NIO_CHANNELS_READABLE_BYTE_CHANNEL = "java/nio/channels/ReadableByteChannel";
    public static final String NAME_JAVA_UTIL_MAP                           = "java/util/Map";
    public static final String TYPE_JAVA_UTIL_MAP                           = "Ljava/util/Map;";
    public static final String NAME_JAVA_UTIL_HASH_MAP                      = "java/util/HashMap";
    public static final String NAME_JAVA_UTIL_LIST                          = "java/util/List";
    public static final String TYPE_JAVA_UTIL_LIST                          = "Ljava/util/List;";
    public static final String NAME_JAVA_UTIL_ARRAY_LIST                    = "java/util/ArrayList";

    public static final String NAME_ANDROID_APP_ACTIVITY                    = "android/app/Activity";
    public static final String NAME_ANDROID_APP_APPLICATION                 = "android/app/Application";
    public static final String NAME_ANDROID_APP_FRAGMENT                    = "android/app/Fragment";
    public static final String NAME_ANDROID_APP_INSTRUMENTATION             = "android/app/Instrumentation";
    public static final String NAME_ANDROID_APP_SERVICE                     = "android/app/Service";
    public static final String NAME_ANDROID_APP_BACKUP_BACKUP_AGENT         = "android/app/backup/BackupAgent";
    public static final String NAME_ANDROID_CONTENT_BROADCAST_RECEIVER      = "android/content/BroadcastReceiver";
    public static final String NAME_ANDROID_CONTENT_CONTENT_PROVIDER        = "android/content/ContentProvider";
    public static final String NAME_ANDROID_CONTENT_CONTEXT                 = "android/content/Context";
    public static final String NAME_ANDROID_CONTENT_RES_RESOURCES           = "android/content/res/Resources";
    public static final String NAME_ANDROID_PREFERENCE_PREFERENCE           = "android/preference/Preference";
    public static final String NAME_ANDROID_PREFERENCE_PREFERENCE_FRAGMENT  = "android/preference/PreferenceFragment";
    public static final String NAME_ANDROID_VIEW_ACTION_PROVIDER            = "android/view/ActionProvider";
    public static final String NAME_ANDROID_VIEW_VIEW                       = "android/view/View";
    public static final String NAME_ANDROID_UTIL_FLOAT_MATH                 = "android/util/FloatMath";
    public static final String NAME_ANDROID_WEBKIT_WEB_VIEW                 = "android/webkit/WebView";
    public static final String NAME_ANDROID_SUPPORT_V4_APP_FRAGMENT         = "android/support/v4/app/Fragment";

    public static final String NAME_JAVA_UTIL_CONCURRENT_ATOMIC_ATOMIC_INTEGER_FIELD_UPDATER   = "java/util/concurrent/atomic/AtomicIntegerFieldUpdater";
    public static final String NAME_JAVA_UTIL_CONCURRENT_ATOMIC_ATOMIC_LONG_FIELD_UPDATER      = "java/util/concurrent/atomic/AtomicLongFieldUpdater";
    public static final String NAME_JAVA_UTIL_CONCURRENT_ATOMIC_ATOMIC_REFERENCE_FIELD_UPDATER = "java/util/concurrent/atomic/AtomicReferenceFieldUpdater";

    public static final String METHOD_NAME_INIT   = "<init>";
    public static final String METHOD_TYPE_INIT   = "()V";
    public static final String METHOD_NAME_CLINIT = "<clinit>";
    public static final String METHOD_TYPE_CLINIT = "()V";

    public static final String METHOD_NAME_OBJECT_GET_CLASS                 = "getClass";
    public static final String METHOD_TYPE_OBJECT_GET_CLASS                 = "()Ljava/lang/Class;";
    public static final String METHOD_NAME_CLASS_FOR_NAME                   = "forName";
    public static final String METHOD_TYPE_CLASS_FOR_NAME                   = "(Ljava/lang/String;)Ljava/lang/Class;";
    public static final String METHOD_TYPE_CLASS_FOR_NAME_CLASSLOADER       = "(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;";
    public static final String METHOD_NAME_CLASS_IS_INSTANCE                = "isInstance";
    public static final String METHOD_TYPE_CLASS_IS_INSTANCE                = "(Ljava/lang/Object;)Z";
    public static final String METHOD_NAME_CLASS_IS_ASSIGNABLE_FROM         = "isAssignableFrom";
    public static final String METHOD_TYPE_CLASS_IS_ASSIGNABLE_FROM         = "(Ljava/lang/Class;)Z";
    public static final String METHOD_NAME_CLASS_GET_CLASS_LOADER           = "getClassLoader";
    public static final String METHOD_NAME_CLASS_GET_COMPONENT_TYPE         = "getComponentType";
    public static final String METHOD_TYPE_CLASS_GET_COMPONENT_TYPE         = "()Ljava/lang/Class;";
    public static final String METHOD_NAME_CLASS_GET_FIELD                  = "getField";
    public static final String METHOD_TYPE_CLASS_GET_FIELD                  = "(Ljava/lang/String;)Ljava/lang/reflect/Field;";
    public static final String METHOD_NAME_CLASS_GET_DECLARED_FIELD         = "getDeclaredField";
    public static final String METHOD_TYPE_CLASS_GET_DECLARED_FIELD         = "(Ljava/lang/String;)Ljava/lang/reflect/Field;";
    public static final String METHOD_NAME_CLASS_GET_FIELDS                 = "getFields";
    public static final String METHOD_TYPE_CLASS_GET_FIELDS                 = "()[Ljava/lang/reflect/Field;";
    public static final String METHOD_NAME_CLASS_GET_DECLARED_FIELDS        = "getDeclaredFields";
    public static final String METHOD_TYPE_CLASS_GET_DECLARED_FIELDS        = "()[Ljava/lang/reflect/Field;";
    public static final String METHOD_NAME_CLASS_GET_CONSTRUCTOR            = "getConstructor";
    public static final String METHOD_TYPE_CLASS_GET_CONSTRUCTOR            = "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;";
    public static final String METHOD_NAME_CLASS_GET_DECLARED_CONSTRUCTOR   = "getDeclaredConstructor";
    public static final String METHOD_TYPE_CLASS_GET_DECLARED_CONSTRUCTOR   = "([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;";
    public static final String METHOD_NAME_CLASS_GET_CONSTRUCTORS           = "getConstructors";
    public static final String METHOD_TYPE_CLASS_GET_CONSTRUCTORS           = "()[Ljava/lang/reflect/Constructor;";
    public static final String METHOD_NAME_CLASS_GET_DECLARED_CONSTRUCTORS  = "getDeclaredConstructors";
    public static final String METHOD_TYPE_CLASS_GET_DECLARED_CONSTRUCTORS  = "()[Ljava/lang/reflect/Constructor;";
    public static final String METHOD_NAME_CLASS_GET_METHOD                 = "getMethod";
    public static final String METHOD_TYPE_CLASS_GET_METHOD                 = "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;";
    public static final String METHOD_NAME_CLASS_GET_DECLARED_METHOD        = "getDeclaredMethod";
    public static final String METHOD_TYPE_CLASS_GET_DECLARED_METHOD        = "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;";
    public static final String METHOD_NAME_CLASS_GET_METHODS                = "getMethods";
    public static final String METHOD_TYPE_CLASS_GET_METHODS                = "()[Ljava/lang/reflect/Method;";
    public static final String METHOD_NAME_CLASS_GET_DECLARED_METHODS       = "getDeclaredMethods";
    public static final String METHOD_TYPE_CLASS_GET_DECLARED_METHODS       = "()[Ljava/lang/reflect/Method;";
    public static final String METHOD_NAME_FIND_CLASS                       = "findClass";
    public static final String METHOD_TYPE_FIND_CLASS                       = "(Ljava/lang/String;)Ljava/lang/Class;";
    public static final String METHOD_NAME_LOAD_CLASS                       = "loadClass";
    public static final String METHOD_TYPE_LOAD_CLASS                       = "(Ljava/lang/String;Z)Ljava/lang/Class;";
    public static final String METHOD_NAME_FIND_LIBRARY                     = "findLibrary";
    public static final String METHOD_TYPE_FIND_LIBRARY                     = "(Ljava/lang/String;)Ljava/lang/String;";
    public static final String METHOD_NAME_LOAD_LIBRARY                     = "loadLibrary";
    public static final String METHOD_TYPE_LOAD_LIBRARY                     = "(Ljava/lang/String;)V";
    public static final String METHOD_NAME_LOAD                             = "load";
    public static final String METHOD_NAME_DO_LOAD                          = "doLoad";
    public static final String METHOD_TYPE_LOAD                             = "(Ljava/lang/String;)V";
    public static final String METHOD_TYPE_LOAD2                            = "(Ljava/lang/String;Ljava/lang/ClassLoader;)V";
    public static final String METHOD_NAME_NATIVE_LOAD                      = "nativeLoad";
    public static final String METHOD_TYPE_NATIVE_LOAD                      = "(Ljava/lang/String;Ljava/lang/ClassLoader;)V";
    public static final String METHOD_NAME_MAP_LIBRARY_NAME                 = "mapLibraryName";
    public static final String METHOD_TYPE_MAP_LIBRARY_NAME                 = "(Ljava/lang/String;)Ljava/lang/String;";
    public static final String METHOD_NAME_GET_RUNTIME                      = "getRuntime";
    public static final String METHOD_TYPE_GET_RUNTIME                      = "()Ljava/lang/Runtime;";
    public static final String METHOD_NAME_CLASS_GET_DECLARING_CLASS        = "getDeclaringClass";
    public static final String METHOD_NAME_CLASS_GET_ENCLOSING_CLASS        = "getEnclosingClass";
    public static final String METHOD_NAME_CLASS_GET_ENCLOSING_CONSTRUCTOR  = "getEnclosingConstructor";
    public static final String METHOD_NAME_CLASS_GET_ENCLOSING_METHOD       = "getEnclosingMethod";
    public static final String METHOD_NAME_GET_ANNOTATION                   = "getAnnotation";
    public static final String METHOD_NAME_GET_ANNOTATIONS                  = "getAnnotations";
    public static final String METHOD_NAME_GET_DECLARED_ANNOTATIONS         = "getDeclaredAnnotations";
    public static final String METHOD_NAME_GET_PARAMETER_ANNOTATIONS        = "getParameterAnnotations";
    public static final String METHOD_NAME_GET_TYPE_PREFIX                  = "getType";
    public static final String METHOD_NAME_GET_GENERIC_PREFIX               = "getGeneric";
    public static final String METHOD_NAME_NEW_UPDATER                      = "newUpdater";
    public static final String METHOD_TYPE_NEW_INTEGER_UPDATER              = "(Ljava/lang/Class;Ljava/lang/String;)Ljava/util/concurrent/atomic/AtomicIntegerFieldUpdater;";
    public static final String METHOD_TYPE_NEW_LONG_UPDATER                 = "(Ljava/lang/Class;Ljava/lang/String;)Ljava/util/concurrent/atomic/AtomicLongFieldUpdater;";
    public static final String METHOD_TYPE_NEW_REFERENCE_UPDATER            = "(Ljava/lang/Class;Ljava/lang/Class;Ljava/lang/String;)Ljava/util/concurrent/atomic/AtomicReferenceFieldUpdater;";
    public static final String METHOD_NAME_FIELD_GET                        = "get";
    public static final String METHOD_TYPE_FIELD_GET                        = "(Ljava/lang/Object;)Ljava/lang/Object;";
    public static final String METHOD_NAME_FIELD_SET                        = "set";
    public static final String METHOD_TYPE_FIELD_SET                        = "(Ljava/lang/Object;Ljava/lang/Object;)V";
    public static final String METHOD_NAME_METHOD_INVOKE                    = "invoke";
    public static final String METHOD_TYPE_METHOD_INVOKE                    = "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;";
    public static final String METHOD_NAME_CONSTRUCTOR_NEW_INSTANCE         = "newInstance";
    public static final String METHOD_TYPE_CONSTRUCTOR_NEW_INSTANCE         = "([Ljava/lang/Object;)Ljava/lang/Object;";
    public static final String METHOD_NAME_ARRAY_NEW_INSTANCE               = "newInstance";
    public static final String METHOD_TYPE_ARRAY_NEW_INSTANCE               = "(Ljava/lang/Class;I)Ljava/lang/Object;";
    public static final String METHOD_TYPE_ARRAY_NEW_INSTANCE2              = "(Ljava/lang/Class;[I)Ljava/lang/Object;";
    public static final String METHOD_NAME_ACCESSIBLE_OBJECT_SET_ACCESSIBLE = "setAccessible";
    public static final String METHOD_TYPE_ACCESSIBLE_OBJECT_SET_ACCESSIBLE = "(Z)V";
    public static final String METHOD_NAME_GET_CAUSE                        = "getCause";
    public static final String METHOD_TYPE_GET_CAUSE                        = "()Ljava/lang/Throwable;";
    public static final String METHOD_TYPE_INIT_THROWABLE                   = "(Ljava/lang/Throwable;)V";
    public static final String METHOD_NAME_MAKE_CONCAT                      = "makeConcat";
    public static final String METHOD_NAME_MAKE_CONCAT_WITH_CONSTANTS       = "makeConcatWithConstants";

    public static final String METHOD_TYPE_INIT_COLLECTION                  = "(Ljava/util/Collection;)V";
    public static final String METHOD_NAME_ADD                              = "add";
    public static final String METHOD_TYPE_ADD                              = "(Ljava/lang/Object;)Z";
    public static final String METHOD_NAME_ADD_ALL                          = "addAll";
    public static final String METHOD_TYPE_ADD_ALL                          = "(Ljava/util/Collection;)Z";
    public static final String METHOD_NAME_IS_EMPTY                         = "isEmpty";
    public static final String METHOD_TYPE_IS_EMPTY                         = "()Z";
    public static final String METHOD_NAME_MAP_PUT                          = "put";
    public static final String METHOD_TYPE_MAP_PUT                          = "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;";
    public static final String METHOD_NAME_MAP_GET                          = "get";
    public static final String METHOD_TYPE_MAP_GET                          = "(Ljava/lang/Object;)Ljava/lang/Object;";

    public static final String METHOD_NAME_BOOLEAN_VALUE    = "booleanValue";
    public static final String METHOD_TYPE_BOOLEAN_VALUE    = "()Z";
    public static final String METHOD_NAME_CHAR_VALUE       = "charValue";
    public static final String METHOD_TYPE_CHAR_VALUE       = "()C";
    public static final String METHOD_NAME_SHORT_VALUE      = "shortValue";
    public static final String METHOD_TYPE_SHORT_VALUE      = "()S";
    public static final String METHOD_NAME_INT_VALUE        = "intValue";
    public static final String METHOD_TYPE_INT_VALUE        = "()I";
    public static final String METHOD_NAME_BYTE_VALUE       = "byteValue";
    public static final String METHOD_TYPE_BYTE_VALUE       = "()B";
    public static final String METHOD_NAME_LONG_VALUE       = "longValue";
    public static final String METHOD_TYPE_LONG_VALUE       = "()J";
    public static final String METHOD_NAME_FLOAT_VALUE      = "floatValue";
    public static final String METHOD_TYPE_FLOAT_VALUE      = "()F";
    public static final String METHOD_NAME_DOUBLE_VALUE     = "doubleValue";
    public static final String METHOD_TYPE_DOUBLE_VALUE     = "()D";

    // Serialization methods.
    public static final String METHOD_NAME_READ_OBJECT   = "readObject";
    public static final String METHOD_TYPE_READ_OBJECT   = "(Ljava/io/ObjectInputStream;)V";
    public static final String METHOD_NAME_READ_RESOLVE  = "readResolve";
    public static final String METHOD_TYPE_READ_RESOLVE  = "()Ljava/lang/Object;";
    public static final String METHOD_NAME_WRITE_OBJECT  = "writeObject";
    public static final String METHOD_TYPE_WRITE_OBJECT  = "(Ljava/io/ObjectOutputStream;)V";
    public static final String METHOD_NAME_WRITE_REPLACE = "writeReplace";
    public static final String METHOD_TYPE_WRITE_REPLACE = "()Ljava/lang/Object;";

    public static final String METHOD_NAME_DOT_CLASS_JAVAC = "class$";
    public static final String METHOD_TYPE_DOT_CLASS_JAVAC = "(Ljava/lang/String;)Ljava/lang/Class;";
    public static final String METHOD_NAME_DOT_CLASS_JIKES = "class";
    public static final String METHOD_TYPE_DOT_CLASS_JIKES = "(Ljava/lang/String;Z)Ljava/lang/Class;";

    public static final String METHOD_TYPE_INIT_ENUM = "(Ljava/lang/String;I)V";

    public static final String METHOD_NAME_NEW_INSTANCE = "newInstance";
    public static final String METHOD_TYPE_NEW_INSTANCE = "()Ljava/lang/Object;";

    public static final String METHOD_NAME_VALUE_OF         = "valueOf";
    public static final String METHOD_TYPE_VALUE_OF_BOOLEAN = "(Z)Ljava/lang/Boolean;";
    public static final String METHOD_TYPE_VALUE_OF_CHAR    = "(C)Ljava/lang/Character;";
    public static final String METHOD_TYPE_VALUE_OF_BYTE    = "(B)Ljava/lang/Byte;";
    public static final String METHOD_TYPE_VALUE_OF_SHORT   = "(S)Ljava/lang/Short;";
    public static final String METHOD_TYPE_VALUE_OF_INT     = "(I)Ljava/lang/Integer;";
    public static final String METHOD_TYPE_VALUE_OF_LONG    = "(J)Ljava/lang/Long;";
    public static final String METHOD_TYPE_VALUE_OF_FLOAT   = "(F)Ljava/lang/Float;";
    public static final String METHOD_TYPE_VALUE_OF_DOUBLE  = "(D)Ljava/lang/Double;";

    public static final String FIELD_NAME_TYPE                    = "TYPE";
    public static final String FIELD_TYPE_TYPE                    = "Ljava/lang/Class;";
    public static final String METHOD_NAME_EQUALS                 = "equals";
    public static final String METHOD_TYPE_EQUALS                 = "(Ljava/lang/Object;)Z";
    public static final String METHOD_NAME_LENGTH                 = "length";
    public static final String METHOD_TYPE_LENGTH                 = "()I";
    public static final String METHOD_NAME_VALUEOF                = "valueOf";
    public static final String METHOD_TYPE_VALUEOF_BOOLEAN        = "(Z)Ljava/lang/String;";
    public static final String METHOD_TYPE_TOSTRING_BOOLEAN       = "(Z)Ljava/lang/String;";
    public static final String METHOD_TYPE_VALUEOF_CHAR           = "(C)Ljava/lang/String;";
    public static final String METHOD_TYPE_VALUEOF_INT            = "(I)Ljava/lang/String;";
    public static final String METHOD_TYPE_VALUEOF_LONG           = "(J)Ljava/lang/String;";
    public static final String METHOD_TYPE_VALUEOF_FLOAT          = "(F)Ljava/lang/String;";
    public static final String METHOD_TYPE_VALUEOF_DOUBLE         = "(D)Ljava/lang/String;";
    public static final String METHOD_TYPE_VALUEOF_OBJECT         = "(Ljava/lang/Object;)Ljava/lang/String;";
    public static final String METHOD_NAME_INTERN                 = "intern";
    public static final String METHOD_TYPE_INTERN                 = "()Ljava/lang/String;";

    public static final String METHOD_NAME_APPEND                 = "append";
    public static final String METHOD_TYPE_INT_VOID               = "(I)V";
    public static final String METHOD_TYPE_STRING_VOID            = "(Ljava/lang/String;)V";
    public static final String METHOD_TYPE_BYTES_VOID             = "([B)V";
    public static final String METHOD_TYPE_BYTES_INT_VOID         = "([BI)V";
    public static final String METHOD_TYPE_CHARS_VOID             = "([C)V";
    public static final String METHOD_TYPE_BOOLEAN_STRING_BUFFER  = "(Z)Ljava/lang/StringBuffer;";
    public static final String METHOD_TYPE_CHAR_STRING_BUFFER     = "(C)Ljava/lang/StringBuffer;";
    public static final String METHOD_TYPE_INT_STRING_BUFFER      = "(I)Ljava/lang/StringBuffer;";
    public static final String METHOD_TYPE_LONG_STRING_BUFFER     = "(J)Ljava/lang/StringBuffer;";
    public static final String METHOD_TYPE_FLOAT_STRING_BUFFER    = "(F)Ljava/lang/StringBuffer;";
    public static final String METHOD_TYPE_DOUBLE_STRING_BUFFER   = "(D)Ljava/lang/StringBuffer;";
    public static final String METHOD_TYPE_STRING_STRING_BUFFER   = "(Ljava/lang/String;)Ljava/lang/StringBuffer;";
    public static final String METHOD_TYPE_OBJECT_STRING_BUFFER   = "(Ljava/lang/Object;)Ljava/lang/StringBuffer;";
    public static final String METHOD_TYPE_BOOLEAN_STRING_BUILDER = "(Z)Ljava/lang/StringBuilder;";
    public static final String METHOD_TYPE_CHAR_STRING_BUILDER    = "(C)Ljava/lang/StringBuilder;";
    public static final String METHOD_TYPE_INT_STRING_BUILDER     = "(I)Ljava/lang/StringBuilder;";
    public static final String METHOD_TYPE_LONG_STRING_BUILDER    = "(J)Ljava/lang/StringBuilder;";
    public static final String METHOD_TYPE_FLOAT_STRING_BUILDER   = "(F)Ljava/lang/StringBuilder;";
    public static final String METHOD_TYPE_DOUBLE_STRING_BUILDER  = "(D)Ljava/lang/StringBuilder;";
    public static final String METHOD_TYPE_STRING_STRING_BUILDER  = "(Ljava/lang/String;)Ljava/lang/StringBuilder;";
    public static final String METHOD_TYPE_OBJECT_STRING_BUILDER  = "(Ljava/lang/Object;)Ljava/lang/StringBuilder;";
    public static final String METHOD_NAME_TOSTRING               = "toString";
    public static final String METHOD_TYPE_TOSTRING               = "()Ljava/lang/String;";
    public static final String METHOD_NAME_CLONE                  = "clone";
    public static final String METHOD_TYPE_CLONE                  = "()Ljava/lang/Object;";

    public static final String METHOD_NAME_VALUES                 = "values";
    public static final String METHOD_NAME_ORDINAL                = "ordinal";
    public static final String METHOD_TYPE_ORDINAL                = "()I";

    public static final String METHOD_NAME_ABS                    = "abs";
    public static final String METHOD_NAME_SQRT                   = "sqrt";
    public static final String METHOD_NAME_COS                    = "cos";
    public static final String METHOD_NAME_SIN                    = "sin";
    public static final String METHOD_NAME_FLOOR                  = "floor";
    public static final String METHOD_NAME_CEIL                   = "ceil";
    public static final String METHOD_TYPE_FLOAT_FLOAT            = "(F)F";
    public static final String METHOD_TYPE_DOUBLE_DOUBLE          = "(D)D";
    public static final String METHOD_NAME_MIN                    = "min";
    public static final String METHOD_NAME_MAX                    = "max";
    public static final String METHOD_TYPE_FLOAT_FLOAT_FLOAT      = "(FF)F";
    public static final String METHOD_TYPE_DOUBLE_DOUBLE_DOUBLE   = "(DD)D";

    public static final String METHOD_NAME_ADD_JAVASCRIPT_INTERFACE = "addJavascriptInterface";
    public static final String METHOD_TYPE_ADD_JAVASCRIPT_INTERFACE = "(Ljava/lang/Object;Ljava/lang/String;)V";

    public static final String METHOD_TYPE_ON_CLICK_HANDLER = "(L**;)L***;";
}
