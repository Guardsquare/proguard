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
package proguard.classfile.util;

import proguard.classfile.*;

import java.util.List;

/**
 * Utility methods for converting between internal and external representations
 * of names and descriptions.
 *
 * @author Eric Lafortune
 */
public class ClassUtil
{
    private static final String EMPTY_STRING = "";


    /**
     * Checks whether the given class magic number is correct.
     * @param magicNumber the magic number.
     * @throws UnsupportedOperationException when the magic number is incorrect.
     */
    public static void checkMagicNumber(int magicNumber) throws UnsupportedOperationException
    {
        if (magicNumber != VersionConstants.MAGIC)
        {
            throw new UnsupportedOperationException("Invalid magic number ["+Integer.toHexString(magicNumber)+"] in class");
        }
    }


    /**
     * Returns the combined class version number.
     * @param majorVersion the major part of the class version number.
     * @param minorVersion the minor part of the class version number.
     * @return the combined class version number.
     */
    public static int internalClassVersion(int majorVersion, int minorVersion)
    {
        return (majorVersion << 16) | minorVersion;
    }


    /**
     * Returns the major part of the given class version number.
     * @param internalClassVersion the combined class version number.
     * @return the major part of the class version number.
     */
    public static int internalMajorClassVersion(int internalClassVersion)
    {
        return internalClassVersion >>> 16;
    }


    /**
     * Returns the internal class version number.
     * @param internalClassVersion the external class version number.
     * @return the internal class version number.
     */
    public static int internalMinorClassVersion(int internalClassVersion)
    {
        return internalClassVersion & 0xffff;
    }


    /**
     * Returns the internal class version number.
     * @param externalClassVersion the external class version number.
     * @return the internal class version number.
     */
    public static int internalClassVersion(String externalClassVersion)
    {
        return
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_0) ||
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_1) ? VersionConstants.CLASS_VERSION_1_0 :
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_2) ? VersionConstants.CLASS_VERSION_1_2 :
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_3) ? VersionConstants.CLASS_VERSION_1_3 :
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_4) ? VersionConstants.CLASS_VERSION_1_4 :
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_5_ALIAS) ||
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_5) ? VersionConstants.CLASS_VERSION_1_5 :
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_6_ALIAS) ||
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_6) ? VersionConstants.CLASS_VERSION_1_6 :
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_7_ALIAS) ||
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_7) ? VersionConstants.CLASS_VERSION_1_7 :
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_8_ALIAS) ||
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_8) ? VersionConstants.CLASS_VERSION_1_8 :
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_9_ALIAS) ||
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_1_9) ? VersionConstants.CLASS_VERSION_1_9 :
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_10)  ? VersionConstants.CLASS_VERSION_10  :
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_11)  ? VersionConstants.CLASS_VERSION_11  :
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_12)  ? VersionConstants.CLASS_VERSION_12  :
            externalClassVersion.equals(JavaVersionConstants.CLASS_VERSION_13)  ? VersionConstants.CLASS_VERSION_13  :
                                                                                  0;
    }


    /**
     * Returns the minor part of the given class version number.
     * @param internalClassVersion the combined class version number.
     * @return the minor part of the class version number.
     */
    public static String externalClassVersion(int internalClassVersion)
    {
        switch (internalClassVersion)
        {
            case VersionConstants.CLASS_VERSION_1_0: return JavaVersionConstants.CLASS_VERSION_1_0;
            case VersionConstants.CLASS_VERSION_1_2: return JavaVersionConstants.CLASS_VERSION_1_2;
            case VersionConstants.CLASS_VERSION_1_3: return JavaVersionConstants.CLASS_VERSION_1_3;
            case VersionConstants.CLASS_VERSION_1_4: return JavaVersionConstants.CLASS_VERSION_1_4;
            case VersionConstants.CLASS_VERSION_1_5: return JavaVersionConstants.CLASS_VERSION_1_5;
            case VersionConstants.CLASS_VERSION_1_6: return JavaVersionConstants.CLASS_VERSION_1_6;
            case VersionConstants.CLASS_VERSION_1_7: return JavaVersionConstants.CLASS_VERSION_1_7;
            case VersionConstants.CLASS_VERSION_1_8: return JavaVersionConstants.CLASS_VERSION_1_8;
            case VersionConstants.CLASS_VERSION_1_9: return JavaVersionConstants.CLASS_VERSION_1_9;
            case VersionConstants.CLASS_VERSION_10:  return JavaVersionConstants.CLASS_VERSION_10;
            case VersionConstants.CLASS_VERSION_11:  return JavaVersionConstants.CLASS_VERSION_11;
            case VersionConstants.CLASS_VERSION_12:  return JavaVersionConstants.CLASS_VERSION_12;
            case VersionConstants.CLASS_VERSION_13:  return JavaVersionConstants.CLASS_VERSION_13;
            default:                                 return null;
        }
    }


    /**
     * Checks whether the given class version number is supported.
     * @param internalClassVersion the combined class version number.
     * @throws UnsupportedOperationException when the version is not supported.
     */
    public static void checkVersionNumbers(int internalClassVersion) throws UnsupportedOperationException
    {
        if (internalClassVersion < VersionConstants.CLASS_VERSION_1_0 ||
            internalClassVersion > VersionConstants.CLASS_VERSION_13)
        {
            throw new UnsupportedOperationException("Unsupported version number ["+
                                                    internalMajorClassVersion(internalClassVersion)+"."+
                                                    internalMinorClassVersion(internalClassVersion)+"] (maximum "+
                                                    VersionConstants.CLASS_VERSION_13_MAJOR+"."+
                                                    VersionConstants.CLASS_VERSION_13_MINOR+", Java "+
                                                    JavaVersionConstants.CLASS_VERSION_13+")");
        }
    }


    /**
     * Converts an external class name into an internal class name.
     * @param externalClassName the external class name,
     *                          e.g. "<code>java.lang.Object</code>"
     * @return the internal class name,
     *                          e.g. "<code>java/lang/Object</code>".
     */
    public static String internalClassName(String externalClassName)
    {
        return externalClassName.replace(JavaTypeConstants.PACKAGE_SEPARATOR,
                                         TypeConstants.PACKAGE_SEPARATOR);
    }


    /**
     * Converts an internal class description into an external class description.
     * @param accessFlags       the access flags of the class.
     * @param internalClassName the internal class name,
     *                          e.g. "<code>java/lang/Object</code>".
     * @return the external class description,
     *                          e.g. "<code>public java.lang.Object</code>".
     */
    public static String externalFullClassDescription(int    accessFlags,
                                                      String internalClassName)
    {
        return externalClassAccessFlags(accessFlags) +
               externalClassName(internalClassName);
    }


    /**
     * Converts an internal class name into an external class name.
     * @param internalClassName the internal class name,
     *                          e.g. "<code>java/lang/Object</code>".
     * @return the external class name,
     *                          e.g. "<code>java.lang.Object</code>".
     */
    public static String externalClassName(String internalClassName)
    {
        return //internalClassName.startsWith(ClassConstants.PACKAGE_JAVA_LANG) &&
               //internalClassName.indexOf(TypeConstants.PACKAGE_SEPARATOR, ClassConstants.PACKAGE_JAVA_LANG.length() + 1) < 0 ?
               //internalClassName.substring(ClassConstants.PACKAGE_JAVA_LANG.length()) :
               internalClassName.replace(TypeConstants.PACKAGE_SEPARATOR,
                                         JavaTypeConstants.PACKAGE_SEPARATOR);
    }


    /**
     * Returns the external base type of an external array type, dropping any
     * array brackets.
     * @param externalArrayType the external array type,
     *                          e.g. "<code>java.lang.Object[][]</code>"
     * @return the external base type,
     *                          e.g. "<code>java.lang.Object</code>".
     */
    public static String externalBaseType(String externalArrayType)
    {
        int index = externalArrayType.indexOf(JavaTypeConstants.ARRAY);
        return index >= 0 ?
            externalArrayType.substring(0, index) :
            externalArrayType;
    }


    /**
     * Returns the external short class name of an external class name, dropping
     * the package specification.
     * @param externalClassName the external class name,
     *                          e.g. "<code>java.lang.Object</code>"
     * @return the external short class name,
     *                          e.g. "<code>Object</code>".
     */
    public static String externalShortClassName(String externalClassName)
    {
        int index = externalClassName.lastIndexOf(JavaTypeConstants.PACKAGE_SEPARATOR);
        return externalClassName.substring(index+1);
    }


    /**
     * Returns the internal short class name of an internal class name, dropping
     * the package specification.
     * @param internalClassName the internal class name,
     *                          e.g. "<code>java/lang/Object</code>"
     * @return the internal short class name,
     *                          e.g. "<code>Object</code>".
     */
    public static String internalShortClassName(String internalClassName)
    {
        int index = internalClassName.lastIndexOf(TypeConstants.PACKAGE_SEPARATOR);
        return internalClassName.substring(index+1);
    }


    /**
     * Returns whether the given internal type is an array type.
     * @param internalType the internal type,
     *                     e.g. "<code>[[Ljava/lang/Object;</code>".
     * @return <code>true</code> if the given type is an array type,
     *         <code>false</code> otherwise.
     */
    public static boolean isInternalArrayType(String internalType)
    {
        return internalType.length() > 1 &&
               internalType.charAt(0) == TypeConstants.ARRAY;
    }


    /**
     * Returns the number of dimensions of the given internal type.
     * @param internalType the internal type,
     *                     e.g. "<code>[[Ljava/lang/Object;</code>".
     * @return the number of dimensions, e.g. 2.
     */
    public static int internalArrayTypeDimensionCount(String internalType)
    {
        int dimensions = 0;
        while (internalType.charAt(dimensions) == TypeConstants.ARRAY)
        {
            dimensions++;
        }

        return dimensions;
    }


    /**
     * Returns whether the given internal class name is one of the interfaces
     * that is implemented by all array types. These class names are
     * "<code>java/lang/Object</code>", "<code>java/lang/Cloneable</code>", and
     * "<code>java/io/Serializable</code>"
     * @param internalClassName the internal class name,
     *                          e.g. "<code>java/lang/Object</code>".
     * @return <code>true</code> if the given type is an array interface name,
     *         <code>false</code> otherwise.
     */
    public static boolean isInternalArrayInterfaceName(String internalClassName)
    {
        return ClassConstants.NAME_JAVA_LANG_OBJECT.equals(internalClassName)    ||
               ClassConstants.NAME_JAVA_LANG_CLONEABLE.equals(internalClassName) ||
               ClassConstants.NAME_JAVA_IO_SERIALIZABLE.equals(internalClassName);
    }


    /**
     * Returns whether the given internal type is a plain primitive type
     * (not void).
     * @param internalType the internal type,
     *                     e.g. "<code>I</code>".
     * @return <code>true</code> if the given type is a class type,
     *         <code>false</code> otherwise.
     */
    public static boolean isInternalPrimitiveType(char internalType)
    {
        return internalType == TypeConstants.BOOLEAN ||
               internalType == TypeConstants.BYTE    ||
               internalType == TypeConstants.CHAR    ||
               internalType == TypeConstants.SHORT   ||
               internalType == TypeConstants.INT     ||
               internalType == TypeConstants.FLOAT   ||
               internalType == TypeConstants.LONG    ||
               internalType == TypeConstants.DOUBLE;
    }


    /**
     * Returns whether the given internal type is a plain primitive type
     * (not void).
     * @param internalType the internal type,
     *                     e.g. "<code>I</code>".
     * @return <code>true</code> if the given type is a class type,
     *         <code>false</code> otherwise.
     */
    public static boolean isInternalPrimitiveType(String internalType)
    {
        return  isInternalPrimitiveType(internalType.charAt(0));
    }


    /**
     * Returns whether the given internal type is a primitive Category 2 type.
     * @param internalType the internal type,
     *                     e.g. "<code>L</code>".
     * @return <code>true</code> if the given type is a Category 2 type,
     *         <code>false</code> otherwise.
     */
    public static boolean isInternalCategory2Type(String internalType)
    {
        return internalType.length() == 1 &&
               (internalType.charAt(0) == TypeConstants.LONG ||
                internalType.charAt(0) == TypeConstants.DOUBLE);
    }


    /**
     * Returns whether the given internal type is a plain class type
     * (including an array type of a plain class type).
     * @param internalType the internal type,
     *                     e.g. "<code>Ljava/lang/Object;</code>".
     * @return <code>true</code> if the given type is a class type,
     *         <code>false</code> otherwise.
     */
    public static boolean isInternalClassType(String internalType)
    {
        int length = internalType.length();
        return length > 1 &&
//             internalType.charAt(0)        == TypeConstants.CLASS_START &&
               internalType.charAt(length-1) == TypeConstants.CLASS_END;
    }


    /**
     * Returns the internal type of a given class name.
     * @param internalClassName the internal class name,
     *                          e.g. "<code>java/lang/Object</code>".
     * @return the internal type,
     *                          e.g. "<code>Ljava/lang/Object;</code>".
     */
    public static String internalTypeFromClassName(String internalClassName)
    {
        return internalArrayTypeFromClassName(internalClassName, 0);
    }


    /**
     * Returns the internal array type of a given class name with a given number
     * of dimensions. If the number of dimensions is 0, the class name itself is
     * returned.
     * @param internalClassName the internal class name,
     *                          e.g. "<code>java/lang/Object</code>".
     * @param dimensionCount    the number of array dimensions.
     * @return the internal array type of the array elements,
     *                          e.g. "<code>Ljava/lang/Object;</code>".
     */
    public static String internalArrayTypeFromClassName(String internalClassName,
                                                        int    dimensionCount)
    {
        StringBuffer buffer = new StringBuffer(internalClassName.length() + dimensionCount + 2);

        for (int dimension = 0; dimension < dimensionCount; dimension++)
        {
            buffer.append(TypeConstants.ARRAY);
        }

        return buffer.append(TypeConstants.CLASS_START)
                     .append(internalClassName)
                     .append(TypeConstants.CLASS_END)
                     .toString();
    }


    /**
     * Returns the internal array type of a given type, with a given number of
     * additional dimensions.
     * @param internalType the internal class name,
     *                          e.g. "<code>[Ljava/lang/Object;</code>".
     * @param dimensionDelta    the number of additional array dimensions,
     *                          e.g. 1.
     * @return the internal array type of the array elements,
     *                          e.g. "<code>[[Ljava/lang/Object;</code>".
     */
    public static String internalArrayTypeFromType(String internalType,
                                                   int    dimensionDelta)
    {
        StringBuffer buffer = new StringBuffer(internalType.length() + dimensionDelta);

        for (int dimension = 0; dimension < dimensionDelta; dimension++)
        {
            buffer.append(TypeConstants.ARRAY);
        }

        return buffer.append(internalType).toString();
    }


    /**
     * Returns the internal element type of a given internal array type.
     * @param internalArrayType the internal array type,
     *                          e.g. "<code>[[Ljava/lang/Object;</code>" or
     *                               "<code>[I</code>".
     * @return the internal type of the array elements,
     *                          e.g. "<code>Ljava/lang/Object;</code>" or
     *                               "<code>I</code>".
     */
    public static String internalTypeFromArrayType(String internalArrayType)
    {
        int index = internalArrayType.lastIndexOf(TypeConstants.ARRAY);
        return internalArrayType.substring(index + 1);
    }


    /**
     * Returns the internal class type (class name or array type) of a given
     * internal type (including an array type). This is the type that can be
     * stored in a class constant.
     * @param internalType the internal class type,
     *                          e.g. "<code>[I</code>",
     *                               "<code>[Ljava/lang/Object;</code>", or
     *                               "<code>Ljava/lang/Object;</code>".
     * @return the internal class name,
     *                          e.g. "<code>[I</code>",
     *                               "<code>[Ljava/lang/Object;</code>", or
     *                               "<code>java/lang/Object</code>".
     */
    public static String internalClassTypeFromType(String internalType)
    {
        return isInternalArrayType(internalType) ?
            internalType :
            internalClassNameFromClassType(internalType);
    }


    /**
     * Returns the internal type of of a given class type (class name or array
     * type). This is the type that can be stored in a class constant.
     * @param internalType the internal class type,
     *                          e.g. "<code>[I</code>",
     *                               "<code>[Ljava/lang/Object;</code>", or
     *                               "<code>java/lang/Object</code>".
     * @return the internal class name,
     *                          e.g. "<code>[I</code>",
     *                               "<code>[Ljava/lang/Object;</code>", or
     *                               "<code>Ljava/lang/Object;</code>".
     */
    public static String internalTypeFromClassType(String internalType)
    {
        return isInternalArrayType(internalType) ?
            internalType :
            internalTypeFromClassName(internalType);
    }


    /**
     * Returns the internal class name of a given internal class type
     * (including an array type). Types involving primitive types are returned
     * unchanged.
     * @param internalClassType the internal class type,
     *                          e.g. "<code>[Ljava/lang/Object;</code>",
     *                               "<code>Ljava/lang/Object;</code>", or
     *                               "<code>java/lang/Object</code>".
     * @return the internal class name,
     *                          e.g. "<code>java/lang/Object</code>".
     */
    public static String internalClassNameFromClassType(String internalClassType)
    {
        return isInternalClassType(internalClassType) ?
            internalClassType.substring(internalClassType.indexOf(TypeConstants.CLASS_START)+1,
                                        internalClassType.length()-1) :
            internalClassType;
    }


    /**
     * Returns the internal class name of any given internal descriptor type,
     * disregarding array prefixes.
     * @param internalClassType the internal class type,
     *                          e.g. "<code>Ljava/lang/Object;</code>" or
     *                               "<code>[[I</code>".
     * @return the internal class name,
     *                          e.g. "<code>java/lang/Object</code>" or
     *                               <code>null</code>.
     */
    public static String internalClassNameFromType(String internalClassType)
    {
        if (!isInternalClassType(internalClassType))
        {
            return null;
        }

        // Is it an array type?
        if (isInternalArrayType(internalClassType))
        {
            internalClassType = internalTypeFromArrayType(internalClassType);
        }

        return internalClassNameFromClassType(internalClassType);
    }


    /**
     * Returns the internal numeric (or void or array) class name corresponding
     * to the given internal primitive type.
     * @param internalPrimitiveType the internal class type,
     *                          e.g. "<code>I</code>" or
     *                               "<code>V</code>".
     * @return the internal class name,
     *                          e.g. "<code>java/lang/Integer</code>" or
     *                               <code>java/lang/Void</code>.
     */
    public static String internalNumericClassNameFromPrimitiveType(char internalPrimitiveType)
    {
        switch (internalPrimitiveType)
        {
            case TypeConstants.VOID:    return ClassConstants.NAME_JAVA_LANG_VOID;
            case TypeConstants.BOOLEAN: return ClassConstants.NAME_JAVA_LANG_BOOLEAN;
            case TypeConstants.BYTE:    return ClassConstants.NAME_JAVA_LANG_BYTE;
            case TypeConstants.CHAR:    return ClassConstants.NAME_JAVA_LANG_CHARACTER;
            case TypeConstants.SHORT:   return ClassConstants.NAME_JAVA_LANG_SHORT;
            case TypeConstants.INT:     return ClassConstants.NAME_JAVA_LANG_INTEGER;
            case TypeConstants.LONG:    return ClassConstants.NAME_JAVA_LANG_LONG;
            case TypeConstants.FLOAT:   return ClassConstants.NAME_JAVA_LANG_FLOAT;
            case TypeConstants.DOUBLE:  return ClassConstants.NAME_JAVA_LANG_DOUBLE;
            case TypeConstants.ARRAY:   return ClassConstants.NAME_JAVA_LANG_REFLECT_ARRAY;
            default:
                throw new IllegalArgumentException("Unexpected primitive type ["+internalPrimitiveType+"]");
        }
    }


    /**
     * Returns the internal numeric (or void or array) class name corresponding
     * to the given internal primitive type.
     * @param internalPrimitiveClassName the internal class name,
     *                          e.g. "<code>java/lang/Integer</code>" or
     *                               <code>java/lang/Void</code>.
     * @return the internal class type,
     *                          e.g. "<code>I</code>" or
     *                               "<code>V</code>".
     */
    public static char internalPrimitiveTypeFromNumericClassName(String internalPrimitiveClassName)
    {
        if (internalPrimitiveClassName.equals(ClassConstants.NAME_JAVA_LANG_VOID))          return TypeConstants.VOID;
        if (internalPrimitiveClassName.equals(ClassConstants.NAME_JAVA_LANG_BOOLEAN))       return TypeConstants.BOOLEAN;
        if (internalPrimitiveClassName.equals(ClassConstants.NAME_JAVA_LANG_BYTE))          return TypeConstants.BYTE;
        if (internalPrimitiveClassName.equals(ClassConstants.NAME_JAVA_LANG_CHARACTER))     return TypeConstants.CHAR;
        if (internalPrimitiveClassName.equals(ClassConstants.NAME_JAVA_LANG_SHORT))         return TypeConstants.SHORT;
        if (internalPrimitiveClassName.equals(ClassConstants.NAME_JAVA_LANG_INTEGER))       return TypeConstants.INT;
        if (internalPrimitiveClassName.equals(ClassConstants.NAME_JAVA_LANG_LONG))          return TypeConstants.LONG;
        if (internalPrimitiveClassName.equals(ClassConstants.NAME_JAVA_LANG_FLOAT))         return TypeConstants.FLOAT;
        if (internalPrimitiveClassName.equals(ClassConstants.NAME_JAVA_LANG_DOUBLE))        return TypeConstants.DOUBLE;
        if (internalPrimitiveClassName.equals(ClassConstants.NAME_JAVA_LANG_REFLECT_ARRAY)) return TypeConstants.ARRAY;

        throw new IllegalArgumentException("Unexpected primitive class name ["+internalPrimitiveClassName+"]");
    }


    /**
     * Returns the simple name of an internal class name, dropping the package
     * specification and any outer class part.
     * @param internalClassName the internal class name,
     *                          e.g. "<code>java/lang/Object</code>"
     * @return the simple class name,
     *                          e.g. "<code>Object</code>".
     */
    public static String internalSimpleClassName(String internalClassName)
    {
        int index1 = internalClassName.lastIndexOf(TypeConstants.PACKAGE_SEPARATOR);
        int index2 = internalClassName.lastIndexOf(TypeConstants.INNER_CLASS_SEPARATOR);

        return internalClassName.substring(Math.max(index1, index2) + 1);
    }


    /**
     * Returns whether the given method name refers to a class initializer or
     * an instance initializer.
     * @param internalMethodName the internal method name,
     *                           e.g. "<code>&ltclinit&gt;</code>".
     * @return whether the method name refers to an initializer,
     *                           e.g. <code>true</code>.
     */
    public static boolean isInitializer(String internalMethodName)
    {
        return internalMethodName.equals(ClassConstants.METHOD_NAME_CLINIT) ||
               internalMethodName.equals(ClassConstants.METHOD_NAME_INIT);
    }


    /**
     * Returns the internal type of the given internal method descriptor.
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(II)Z</code>".
     * @return the internal return type,
     *                                 e.g. "<code>Z</code>".
     */
    public static String internalMethodReturnType(String internalMethodDescriptor)
    {
        int index = internalMethodDescriptor.indexOf(TypeConstants.METHOD_ARGUMENTS_CLOSE);
        return internalMethodDescriptor.substring(index + 1);
    }


    /**
     * Returns the number of parameters of the given internal method descriptor.
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(ID)Z</code>".
     * @return the number of parameters,
     *                                 e.g. 2.
     */
    public static int internalMethodParameterCount(String internalMethodDescriptor)
    {
        return internalMethodParameterCount(internalMethodDescriptor, true);
    }


    /**
     * Returns the number of parameters of the given internal method descriptor.
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(ID)Z</code>".
     * @param accessFlags              the access flags of the method,
     *                                 e.g. 0.
     * @return the number of parameters,
     *                                 e.g. 3.
     */
    public static int internalMethodParameterCount(String internalMethodDescriptor,
                                                   int    accessFlags)
    {
        return internalMethodParameterCount(internalMethodDescriptor,
                                            (accessFlags & AccessConstants.STATIC) != 0);
    }


    /**
     * Returns the number of parameters of the given internal method descriptor.
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(ID)Z</code>".
     * @param isStatic                 specifies whether the method is static,
     *                                 e.g. false.
     * @return the number of parameters,
     *                                 e.g. 3.
     */
    public static int internalMethodParameterCount(String  internalMethodDescriptor,
                                                   boolean isStatic)
    {
        int counter = isStatic ? 0 : 1;
        int index   = 1;

        while (true)
        {
            char c = internalMethodDescriptor.charAt(index++);
            switch (c)
            {
                case TypeConstants.ARRAY:
                {
                    // Just ignore all array characters.
                    break;
                }
                case TypeConstants.CLASS_START:
                {
                    counter++;

                    // Skip the class name.
                    index = internalMethodDescriptor.indexOf(TypeConstants.CLASS_END, index) + 1;
                    break;
                }
                default:
                {
                    counter++;
                    break;
                }
                case TypeConstants.METHOD_ARGUMENTS_CLOSE:
                {
                    return counter;
                }
            }
        }
    }


    /**
     * Returns the size taken up on the stack by the parameters of the given
     * internal method descriptor. This accounts for long and double parameters
     * taking up two entries.
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(ID)Z</code>".
     * @return the size taken up on the stack,
     *                                 e.g. 3.
     */
    public static int internalMethodParameterSize(String internalMethodDescriptor)
    {
        return internalMethodParameterSize(internalMethodDescriptor, true);
    }


    /**
     * Returns the size taken up on the stack by the parameters of the given
     * internal method descriptor. This accounts for long and double parameters
     * taking up two entries, and a non-static method taking up an additional
     * entry.
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(ID)Z</code>".
     * @param accessFlags              the access flags of the method,
     *                                 e.g. 0.
     * @return the size taken up on the stack,
     *                                 e.g. 4.
     */
    public static int internalMethodParameterSize(String internalMethodDescriptor,
                                                  int    accessFlags)
    {
        return internalMethodParameterSize(internalMethodDescriptor,
                                           (accessFlags & AccessConstants.STATIC) != 0);
    }


    /**
     * Returns the size taken up on the stack by the parameters of the given
     * internal method descriptor. This accounts for long and double parameters
     * taking up two spaces, and a non-static method taking up an additional
     * entry.
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(ID)Z</code>".
     * @param isStatic                 specifies whether the method is static,
     *                                 e.g. false.
     * @return the size taken up on the stack,
     *                                 e.g. 4.
     */
    public static int internalMethodParameterSize(String  internalMethodDescriptor,
                                                  boolean isStatic)
    {
        int size  = isStatic ? 0 : 1;
        int index = 1;

        while (true)
        {
            char c = internalMethodDescriptor.charAt(index++);
            switch (c)
            {
                case TypeConstants.LONG:
                case TypeConstants.DOUBLE:
                {
                    size += 2;
                    break;
                }
                case TypeConstants.CLASS_START:
                {
                    size++;

                    // Skip the class name.
                    index = internalMethodDescriptor.indexOf(TypeConstants.CLASS_END, index) + 1;
                    break;
                }
                case TypeConstants.ARRAY:
                {
                    size++;

                    // Skip all array characters.
                    while ((c = internalMethodDescriptor.charAt(index++)) == TypeConstants.ARRAY) {}

                    if (c == TypeConstants.CLASS_START)
                    {
                        // Skip the class type.
                        index = internalMethodDescriptor.indexOf(TypeConstants.CLASS_END, index) + 1;
                    }
                    break;
                }
                default:
                {
                    size++;
                    break;
                }
                case TypeConstants.METHOD_ARGUMENTS_CLOSE:
                {
                    return size;
                }
            }
        }
    }


    /**
     * Returns the parameter number in the given internal method descriptor,
     * corresponding to the given variable index. This accounts for long and
     * double parameters taking up two spaces, and a non-static method taking
     * up an additional entry. The method returns 0 if the index corresponds
     * to the 'this' parameter and -1 if the index does not correspond to a
     * parameter.
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(IDI)Z</code>".
     * @param accessFlags              the access flags of the method,
     *                                 e.g. 0.
     * @param variableIndex            the variable index of the parameter,
     *                                 e.g. 4.
     * @return the parameter number in the descriptor,
     *                                 e.g. 3.
     */
    public static int internalMethodParameterNumber(String internalMethodDescriptor,
                                                    int    accessFlags,
                                                    int    variableIndex)
    {
        return internalMethodParameterNumber(internalMethodDescriptor,
                                             (accessFlags & AccessConstants.STATIC) != 0,
                                             variableIndex);
    }


    /**
     * Returns the parameter number in the given internal method descriptor,
     * corresponding to the given variable index. This accounts for long and
     * double parameters taking up two spaces, and a non-static method taking
     * up an additional entry. The method returns 0 if the index corresponds
     * to the 'this' parameter and -1 if the index does not correspond to a
     * parameter.
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(IDI)Z</code>".
     * @param isStatic                 specifies whether the method is static,
     *                                 e.g. false.
     * @param variableIndex            the variable index of the parameter,
     *                                 e.g. 4.
     * @return the parameter number in the descriptor,
     *                                 e.g. 3.
     */
    public static int internalMethodParameterNumber(String  internalMethodDescriptor,
                                                    boolean isStatic,
                                                    int     variableIndex)
    {
        int parameterIndex  = 0;
        int parameterNumber = 0;

        // Is it a non-static method?
        if (!isStatic)
        {
            if (variableIndex == 0)
            {
                return 0;
            }

            variableIndex--;
            parameterNumber++;
        }

        // Loop over all variables until we've found the right index.
        InternalTypeEnumeration internalTypeEnumeration =
            new InternalTypeEnumeration(internalMethodDescriptor);

        while (internalTypeEnumeration.hasMoreTypes())
        {
            if (variableIndex == parameterIndex)
            {
                return parameterNumber;
            }

            String internalType = internalTypeEnumeration.nextType();

            parameterIndex += internalTypeSize(internalType);
            parameterNumber++;
        }

        return -1;
    }


    /**
     * Returns the variable index corresponding to the given parameter number
     * in the given internal method descriptor. This accounts for long and
     * double parameters taking up two spaces, and a non-static method taking
     * up an additional entry. The method returns 0 if the number corresponds
     * to the 'this' parameter and -1 if the number does not correspond to a
     * parameter.
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(IDI)Z</code>".
     * @param accessFlags              the access flags of the method,
     *                                 e.g. 0.
     * @param parameterNumber          the parameter number,
     *                                 e.g. 3.
     * @return the corresponding variable index,
     *                                 e.g. 4.
     */
    public static int internalMethodVariableIndex(String internalMethodDescriptor,
                                                  int    accessFlags,
                                                  int    parameterNumber)
    {
        return internalMethodVariableIndex(internalMethodDescriptor,
                                           (accessFlags & AccessConstants.STATIC) != 0,
                                           parameterNumber);
    }


    /**
     * Returns the parameter index in the given internal method descriptor,
     * corresponding to the given variable number. This accounts for long and
     * double parameters taking up two spaces, and a non-static method taking
     * up an additional entry. The method returns 0 if the number corresponds
     * to the 'this' parameter and -1 if the number does not correspond to a
     * parameter.
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(IDI)Z</code>".
     * @param isStatic                 specifies whether the method is static,
     *                                 e.g. false.
     * @param parameterNumber          the parameter number,
     *                                 e.g. 3.
     * @return the corresponding variable index,
     *                                 e.g. 4.
     */
    public static int internalMethodVariableIndex(String  internalMethodDescriptor,
                                                  boolean isStatic,
                                                  int     parameterNumber)
    {
        int variableNumber = 0;
        int variableIndex  = isStatic ? 0 : 1;

        // Loop over the given number of parameters.
        InternalTypeEnumeration internalTypeEnumeration =
            new InternalTypeEnumeration(internalMethodDescriptor);

        for (int counter = 0; counter < parameterNumber; counter++)
        {
            String internalType = internalTypeEnumeration.nextType();

            variableIndex += internalTypeSize(internalType);
        }

        return variableIndex;
    }


    /**
     * Returns the internal type of the parameter in the given method descriptor,
     * at the given index.
     *
     * @param internalMethodDescriptor the internal method descriptor
     *                                 e.g. "<code>(IDI)Z</code>".
     * @param parameterIndex           the parameter index, e.g. 1.
     * @return the parameter's type, e.g. "<code>D</code>".
     */
    public static String internalMethodParameterType(String  internalMethodDescriptor,
                                                     int     parameterIndex)
    {
        InternalTypeEnumeration typeEnum = new InternalTypeEnumeration(internalMethodDescriptor);
        String                  type     = null;
        for (int i = 0; i <= parameterIndex; i++)
        {
            type = typeEnum.nextType();
        }
        return type;
    }


    /**
     * Returns the size taken up on the stack by the given internal type.
     * The size is 1, except for long and double types, for which it is 2,
     * and for the void type, for which 0 is returned.
     * @param internalType the internal type,
     *                     e.g. "<code>I</code>".
     * @return the size taken up on the stack,
     *                     e.g. 1.
     */
    public static int internalTypeSize(String internalType)
    {
        if (internalType.length() == 1)
        {
            char internalPrimitiveType = internalType.charAt(0);
            if      (internalPrimitiveType == TypeConstants.LONG ||
                     internalPrimitiveType == TypeConstants.DOUBLE)
            {
                return 2;
            }
            else if (internalPrimitiveType == TypeConstants.VOID)
            {
                return 0;
            }
        }

        return 1;
    }


    /**
     * Converts an external type into an internal type.
     * @param externalType the external type,
     *                     e.g. "<code>java.lang.Object[][]</code>" or
     *                          "<code>int[]</code>".
     * @return the internal type,
     *                     e.g. "<code>[[Ljava/lang/Object;</code>" or
     *                          "<code>[I</code>".
     */
    public static String internalType(String externalType)
    {
        // Strip the array part, if any.
        int dimensionCount = externalArrayTypeDimensionCount(externalType);
        if (dimensionCount > 0)
        {
            externalType = externalType.substring(0, externalType.length() - dimensionCount * JavaTypeConstants.ARRAY.length());
        }

        // Analyze the actual type part.
        char internalTypeChar =
            externalType.equals(JavaTypeConstants.VOID   ) ? TypeConstants.VOID    :
            externalType.equals(JavaTypeConstants.BOOLEAN) ? TypeConstants.BOOLEAN :
            externalType.equals(JavaTypeConstants.BYTE   ) ? TypeConstants.BYTE    :
            externalType.equals(JavaTypeConstants.CHAR   ) ? TypeConstants.CHAR    :
            externalType.equals(JavaTypeConstants.SHORT  ) ? TypeConstants.SHORT   :
            externalType.equals(JavaTypeConstants.INT    ) ? TypeConstants.INT     :
            externalType.equals(JavaTypeConstants.FLOAT  ) ? TypeConstants.FLOAT   :
            externalType.equals(JavaTypeConstants.LONG   ) ? TypeConstants.LONG    :
            externalType.equals(JavaTypeConstants.DOUBLE ) ? TypeConstants.DOUBLE  :
            externalType.equals("%"                       ) ? '%'                         :
                                                              (char)0;

        String internalType =
            internalTypeChar != 0 ? String.valueOf(internalTypeChar) :
                                    TypeConstants.CLASS_START +
                                    internalClassName(externalType) +
                                    TypeConstants.CLASS_END;

        // Prepend the array part, if any.
        for (int count = 0; count < dimensionCount; count++)
        {
            internalType = TypeConstants.ARRAY + internalType;
        }

        return internalType;
    }


    /**
     * Returns the number of dimensions of the given external type.
     * @param externalType the external type,
     *                     e.g. "<code>[[Ljava/lang/Object;</code>".
     * @return the number of dimensions, e.g. 2.
     */
    public static int externalArrayTypeDimensionCount(String externalType)
    {
        int dimensions = 0;
        int length = JavaTypeConstants.ARRAY.length();
        int offset = externalType.length() - length;
        while (externalType.regionMatches(offset,
                                          JavaTypeConstants.ARRAY,
                                          0,
                                          length))
        {
            dimensions++;
            offset -= length;
        }

        return dimensions;
    }


    /**
     * Converts an internal type into an external type.
     * @param internalType the internal type,
     *                     e.g. "<code>Ljava/lang/Object;</code>" or
     *                          "<code>[[Ljava/lang/Object;</code>" or
     *                          "<code>[I</code>".
     * @return the external type,
     *                     e.g. "<code>java.lang.Object</code>" or
     *                          "<code>java.lang.Object[][]</code>" or
     *                          "<code>int[]</code>".
     */
    public static String externalType(String internalType)
    {
        // Strip the array part, if any.
        int dimensionCount = internalArrayTypeDimensionCount(internalType);
        if (dimensionCount > 0)
        {
            internalType = internalType.substring(dimensionCount);
        }

        // Analyze the actual type part.
        char internalTypeChar = internalType.charAt(0);

        String externalType =
            internalTypeChar == TypeConstants.VOID        ? JavaTypeConstants.VOID    :
            internalTypeChar == TypeConstants.BOOLEAN     ? JavaTypeConstants.BOOLEAN :
            internalTypeChar == TypeConstants.BYTE        ? JavaTypeConstants.BYTE    :
            internalTypeChar == TypeConstants.CHAR        ? JavaTypeConstants.CHAR    :
            internalTypeChar == TypeConstants.SHORT       ? JavaTypeConstants.SHORT   :
            internalTypeChar == TypeConstants.INT         ? JavaTypeConstants.INT     :
            internalTypeChar == TypeConstants.FLOAT       ? JavaTypeConstants.FLOAT   :
            internalTypeChar == TypeConstants.LONG        ? JavaTypeConstants.LONG    :
            internalTypeChar == TypeConstants.DOUBLE      ? JavaTypeConstants.DOUBLE  :
            internalTypeChar == '%'                       ? "%"                       :
            internalTypeChar == TypeConstants.CLASS_START ? externalClassName(internalType.substring(1, internalType.indexOf(TypeConstants.CLASS_END))) :
                                                            null;

        if (externalType == null)
        {
            throw new IllegalArgumentException("Unknown type ["+internalType+"]");
        }

        // Append the array part, if any.
        for (int count = 0; count < dimensionCount; count++)
        {
            externalType += JavaTypeConstants.ARRAY;
        }

        return externalType;
    }


    /**
     * Converts an internal type into an external type, as expected by
     * Class.forName.
     * @param internalType the internal type,
     *                     e.g. "<code>Ljava/lang/Object;</code>" or
     *                          "<code>[[Ljava/lang/Object;</code>" or
     *                          "<code>[I</code>".
     * @return the external type,
     *                     e.g. "<code>java.lang.Object</code>" or
     *                          "<code>[[Ljava.lang.Object;</code>" or
     *                          "<code>[I</code>".
     */
    public static String externalClassForNameType(String internalType)
    {
        return isInternalArrayType(internalType) ?
            externalClassName(internalType) :
            externalClassName(internalClassNameFromClassType(internalType));
    }


    /**
     * Returns whether the given internal descriptor String represents a method
     * descriptor.
     * @param internalDescriptor the internal descriptor String,
     *                           e.g. "<code>(II)Z</code>".
     * @return <code>true</code> if the given String is a method descriptor,
     *         <code>false</code> otherwise.
     */
    public static boolean isInternalMethodDescriptor(String internalDescriptor)
    {
        return internalDescriptor.charAt(0) == TypeConstants.METHOD_ARGUMENTS_OPEN;
    }


    /**
     * Returns whether the given member String represents an external method
     * name with arguments.
     * @param externalMemberNameAndArguments the external member String,
     *                                       e.g. "<code>myField</code>" or
     *                                       e.g. "<code>myMethod(int,int)</code>".
     * @return <code>true</code> if the given String refers to a method,
     *         <code>false</code> otherwise.
     */
    public static boolean isExternalMethodNameAndArguments(String externalMemberNameAndArguments)
    {
        return externalMemberNameAndArguments.indexOf(JavaTypeConstants.METHOD_ARGUMENTS_OPEN) > 0;
    }


    /**
     * Returns the name part of the given external method name and arguments.
     * @param externalMethodNameAndArguments the external method name and arguments,
     *                                       e.g. "<code>myMethod(int,int)</code>".
     * @return the name part of the String, e.g. "<code>myMethod</code>".
     */
    public static String externalMethodName(String externalMethodNameAndArguments)
    {
        ExternalTypeEnumeration externalTypeEnumeration =
            new ExternalTypeEnumeration(externalMethodNameAndArguments);

        return externalTypeEnumeration.methodName();
    }


    /**
     * Converts the given external method return type and name and arguments to
     * an internal method descriptor.
     * @param externalReturnType             the external method return type,
     *                                       e.g. "<code>boolean</code>".
     * @param externalMethodNameAndArguments the external method name and arguments,
     *                                       e.g. "<code>myMethod(int,int)</code>".
     * @return the internal method descriptor,
     *                                       e.g. "<code>(II)Z</code>".
     */
    public static String internalMethodDescriptor(String externalReturnType,
                                                  String externalMethodNameAndArguments)
    {
        StringBuffer internalMethodDescriptor = new StringBuffer();
        internalMethodDescriptor.append(TypeConstants.METHOD_ARGUMENTS_OPEN);

        ExternalTypeEnumeration externalTypeEnumeration =
            new ExternalTypeEnumeration(externalMethodNameAndArguments);

        while (externalTypeEnumeration.hasMoreTypes())
        {
            internalMethodDescriptor.append(internalType(externalTypeEnumeration.nextType()));
        }

        internalMethodDescriptor.append(TypeConstants.METHOD_ARGUMENTS_CLOSE);
        internalMethodDescriptor.append(internalType(externalReturnType));

        return internalMethodDescriptor.toString();
    }


    /**
     * Converts the given external method return type and List of arguments to
     * an internal method descriptor.
     * @param externalReturnType the external method return type,
     *                                       e.g. "<code>boolean</code>".
     * @param externalArguments the external method arguments,
     *                                       e.g. <code>{ "int", "int" }</code>.
     * @return the internal method descriptor,
     *                                       e.g. "<code>(II)Z</code>".
     */
    public static String internalMethodDescriptor(String externalReturnType,
                                                  List   externalArguments)
    {
        StringBuffer internalMethodDescriptor = new StringBuffer();
        internalMethodDescriptor.append(TypeConstants.METHOD_ARGUMENTS_OPEN);

        for (int index = 0; index < externalArguments.size(); index++)
        {
            internalMethodDescriptor.append(internalType((String)externalArguments.get(index)));
        }

        internalMethodDescriptor.append(TypeConstants.METHOD_ARGUMENTS_CLOSE);
        internalMethodDescriptor.append(internalType(externalReturnType));

        return internalMethodDescriptor.toString();
    }


    /**
     * Converts the given internal method return type and List of arguments to
     * an internal method descriptor.
     *
     * @param internalReturnType the external method return type,
     *                           e.g. "<code>Z</code>".
     * @param internalArguments  the external method arguments,
     *                           e.g. <code>{ "I", "I" }</code>.
     * @return the internal method descriptor, e.g. "<code>(II)Z</code>".
     */
    public static String internalMethodDescriptorFromInternalTypes(String       internalReturnType,
                                                                   List<String> internalArguments)
    {
        StringBuilder internalMethodDescriptor = new StringBuilder();
        internalMethodDescriptor.append(TypeConstants.METHOD_ARGUMENTS_OPEN);

        for (String argument : internalArguments)
        {
            internalMethodDescriptor.append(argument);
        }

        internalMethodDescriptor.append(TypeConstants.METHOD_ARGUMENTS_CLOSE);
        internalMethodDescriptor.append(internalReturnType);

        return internalMethodDescriptor.toString();
    }


    /**
     * Converts an internal field description into an external full field description.
     * @param accessFlags             the access flags of the field.
     * @param fieldName               the field name,
     *                                e.g. "<code>myField</code>".
     * @param internalFieldDescriptor the internal field descriptor,
     *                                e.g. "<code>Z</code>".
     * @return the external full field description,
     *                                e.g. "<code>public boolean myField</code>".
     */
    public static String externalFullFieldDescription(int    accessFlags,
                                                      String fieldName,
                                                      String internalFieldDescriptor)
    {
        return externalFieldAccessFlags(accessFlags) +
               externalType(internalFieldDescriptor) +
               ' ' +
               fieldName;
    }


    /**
     * Converts an internal method description into an external full method description.
     * @param internalClassName        the internal name of the class of the method,
     *                                 e.g. "<code>mypackage/MyClass</code>".
     * @param accessFlags              the access flags of the method.
     * @param internalMethodName       the internal method name,
     *                                 e.g. "<code>myMethod</code>" or
     *                                      "<code>&lt;init&gt;</code>".
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(II)Z</code>".
     * @return the external full method description,
     *                                 e.g. "<code>public boolean myMethod(int,int)</code>" or
     *                                      "<code>public MyClass(int,int)</code>".
     */
    public static String externalFullMethodDescription(String internalClassName,
                                                       int    accessFlags,
                                                       String internalMethodName,
                                                       String internalMethodDescriptor)
    {
        return externalMethodAccessFlags(accessFlags) +
               externalMethodReturnTypeAndName(internalClassName,
                                               internalMethodName,
                                               internalMethodDescriptor) +
               JavaTypeConstants.METHOD_ARGUMENTS_OPEN +
               externalMethodArguments(internalMethodDescriptor) +
               JavaTypeConstants.METHOD_ARGUMENTS_CLOSE;
    }


    /**
     * Converts internal class access flags into an external access description.
     * @param accessFlags the class access flags.
     * @return the external class access description,
     *         e.g. "<code>public final </code>".
     */
    public static String externalClassAccessFlags(int accessFlags)
    {
        return externalClassAccessFlags(accessFlags, "");
    }


    /**
     * Converts internal class access flags into an external access description.
     * @param accessFlags the class access flags.
     * @param prefix      a prefix that is added to each access modifier.
     * @return the external class access description,
     *         e.g. "<code>public final </code>".
     */
    public static String externalClassAccessFlags(int accessFlags, String prefix)
    {
        if (accessFlags == 0)
        {
            return EMPTY_STRING;
        }

        StringBuffer string = new StringBuffer(50);

        if ((accessFlags & AccessConstants.PUBLIC) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.PUBLIC).append(' ');
        }
        if ((accessFlags & AccessConstants.PRIVATE) != 0)
        {
            // Only in InnerClasses attributes.
            string.append(prefix).append(JavaAccessConstants.PRIVATE).append(' ');
        }
        if ((accessFlags & AccessConstants.PROTECTED) != 0)
        {
            // Only in InnerClasses attributes.
            string.append(prefix).append(JavaAccessConstants.PROTECTED).append(' ');
        }
        if ((accessFlags & AccessConstants.STATIC) != 0)
        {
            // Only in InnerClasses attributes.
            string.append(prefix).append(JavaAccessConstants.STATIC).append(' ');
        }
        if ((accessFlags & AccessConstants.FINAL) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.FINAL).append(' ');
        }
        if ((accessFlags & AccessConstants.ANNOTATION) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.ANNOTATION);
        }
        if ((accessFlags & AccessConstants.INTERFACE) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.INTERFACE).append(' ');
        }
        else if ((accessFlags & AccessConstants.ENUM) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.ENUM).append(' ');
        }
        else if ((accessFlags & AccessConstants.ABSTRACT) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.ABSTRACT).append(' ');
        }
        else if ((accessFlags & AccessConstants.SYNTHETIC) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.SYNTHETIC).append(' ');
        }
        else if ((accessFlags & AccessConstants.MODULE) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.MODULE).append(' ');
        }

        return string.toString();
    }


    /**
     * Converts internal field access flags into an external access description.
     * @param accessFlags the field access flags.
     * @return the external field access description,
     *         e.g. "<code>public volatile </code>".
     */
    public static String externalFieldAccessFlags(int accessFlags)
    {
        return externalFieldAccessFlags(accessFlags, "");
    }


    /**
     * Converts internal field access flags into an external access description.
     * @param accessFlags the field access flags.
     * @param prefix      a prefix that is added to each access modifier.
     * @return the external field access description,
     *         e.g. "<code>public volatile </code>".
     */
    public static String externalFieldAccessFlags(int accessFlags, String prefix)
    {
        if (accessFlags == 0)
        {
            return EMPTY_STRING;
        }

        StringBuffer string = new StringBuffer(50);

        if ((accessFlags & AccessConstants.PUBLIC) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.PUBLIC).append(' ');
        }
        if ((accessFlags & AccessConstants.PRIVATE) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.PRIVATE).append(' ');
        }
        if ((accessFlags & AccessConstants.PROTECTED) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.PROTECTED).append(' ');
        }
        if ((accessFlags & AccessConstants.STATIC) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.STATIC).append(' ');
        }
        if ((accessFlags & AccessConstants.FINAL) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.FINAL).append(' ');
        }
        if ((accessFlags & AccessConstants.VOLATILE) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.VOLATILE).append(' ');
        }
        if ((accessFlags & AccessConstants.TRANSIENT) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.TRANSIENT).append(' ');
        }
        if ((accessFlags & AccessConstants.SYNTHETIC) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.SYNTHETIC).append(' ');
        }

        return string.toString();
    }


    /**
     * Converts internal method access flags into an external access description.
     * @param accessFlags the method access flags.
     * @return the external method access description,
     *                    e.g. "<code>public synchronized </code>".
     */
    public static String externalMethodAccessFlags(int accessFlags)
    {
        return externalMethodAccessFlags(accessFlags, "");
    }


    /**
     * Converts internal method access flags into an external access description.
     * @param accessFlags the method access flags.
     * @param prefix      a prefix that is added to each access modifier.
     * @return the external method access description,
     *                    e.g. "public synchronized ".
     */
    public static String externalMethodAccessFlags(int accessFlags, String prefix)
    {
        if (accessFlags == 0)
        {
            return EMPTY_STRING;
        }

        StringBuffer string = new StringBuffer(50);

        if ((accessFlags & AccessConstants.PUBLIC) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.PUBLIC).append(' ');
        }
        if ((accessFlags & AccessConstants.PRIVATE) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.PRIVATE).append(' ');
        }
        if ((accessFlags & AccessConstants.PROTECTED) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.PROTECTED).append(' ');
        }
        if ((accessFlags & AccessConstants.STATIC) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.STATIC).append(' ');
        }
        if ((accessFlags & AccessConstants.FINAL) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.FINAL).append(' ');
        }
        if ((accessFlags & AccessConstants.SYNCHRONIZED) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.SYNCHRONIZED).append(' ');
        }
        if ((accessFlags & AccessConstants.BRIDGE) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.BRIDGE).append(' ');
        }
        if ((accessFlags & AccessConstants.VARARGS) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.VARARGS).append(' ');
        }
        if ((accessFlags & AccessConstants.NATIVE) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.NATIVE).append(' ');
        }
        if ((accessFlags & AccessConstants.ABSTRACT) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.ABSTRACT).append(' ');
        }
        if ((accessFlags & AccessConstants.STRICT) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.STRICT).append(' ');
        }
        if ((accessFlags & AccessConstants.SYNTHETIC) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.SYNTHETIC).append(' ');
        }

        return string.toString();
    }


    /**
     * Converts internal method parameter access flags into an external access
     * description.
     * @param accessFlags the method parameter access flags.
     * @return the external method parameter access description,
     *                    e.g. "<code>final mandated </code>".
     */
    public static String externalParameterAccessFlags(int accessFlags)
    {
        return externalParameterAccessFlags(accessFlags, "");
    }


    /**
     * Converts internal method parameter access flags into an external access
     * description.
     * @param accessFlags the method parameter access flags.
     * @param prefix      a prefix that is added to each access modifier.
     * @return the external method parameter access description,
     *                    e.g. "final mandated ".
     */
    public static String externalParameterAccessFlags(int accessFlags, String prefix)
    {
        if (accessFlags == 0)
        {
            return EMPTY_STRING;
        }

        StringBuffer string = new StringBuffer(50);

        if ((accessFlags & AccessConstants.FINAL) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.FINAL).append(' ');
        }
        if ((accessFlags & AccessConstants.SYNTHETIC) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.SYNTHETIC).append(' ');
        }
        if ((accessFlags & AccessConstants.MANDATED) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.MANDATED).append(' ');
        }

        return string.toString();
    }


    /**
     * Converts an internal method descriptor into an external method return type.
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(II)Z</code>".
     * @return the external method return type,
     *                                 e.g. "<code>boolean</code>".
     */
    public static String externalMethodReturnType(String internalMethodDescriptor)
    {
        return externalType(internalMethodReturnType(internalMethodDescriptor));
    }


    /**
     * Converts internal module access flags into an external access
     * description.
     * @param accessFlags the module access flags.
     * @return the external module access description,
     *                    e.g. "<code>open mandated </code>".
     */
    public static String externalModuleAccessFlags(int accessFlags)
    {
        return externalModuleAccessFlags(accessFlags, "");
    }


    /**
     * Converts internal module access flags into an external access
     * description.
     * @param accessFlags the module access flags.
     * @param prefix      a prefix that is added to each access modifier.
     * @return the external module access description,
     *                    e.g. "<code>final mandated </code>".
     */
    public static String externalModuleAccessFlags(int accessFlags, String prefix)
    {
        if (accessFlags == 0)
        {
            return EMPTY_STRING;
        }

        StringBuffer string = new StringBuffer(50);

        if ((accessFlags & AccessConstants.OPEN) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.OPEN).append(' ');
        }
        if ((accessFlags & AccessConstants.SYNTHETIC) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.SYNTHETIC).append(' ');
        }
        if ((accessFlags & AccessConstants.MANDATED) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.MANDATED).append(' ');
        }

        return string.toString();
    }


    /**
     * Converts internal module requires access flags into an external access
     * description.
     * @param accessFlags the module requires access flags.
     * @return the external module requires access description,
     *                    e.g. "<code>static mandated </code>".
     */
    public static String externalRequiresAccessFlags(int accessFlags)
    {
        return externalRequiresAccessFlags(accessFlags, "");
    }


    /**
     * Converts internal module requires access flags into an external access
     * description.
     * @param accessFlags the module requires access flags.
     * @param prefix      a prefix that is added to each access modifier.
     * @return the external module requires access description,
     *                    e.g. "<code>static mandated </code>".
     */
    public static String externalRequiresAccessFlags(int accessFlags, String prefix)
    {
        if (accessFlags == 0)
        {
            return EMPTY_STRING;
        }

        StringBuffer string = new StringBuffer(50);

        if ((accessFlags & AccessConstants.TRANSITIVE) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.TRANSITIVE).append(' ');
        }
        if ((accessFlags & AccessConstants.STATIC_PHASE) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.STATIC).append(' ');
        }
        if ((accessFlags & AccessConstants.SYNTHETIC) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.SYNTHETIC).append(' ');
        }
        if ((accessFlags & AccessConstants.MANDATED) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.MANDATED).append(' ');
        }

        return string.toString();
    }


    /**
     * Converts internal module exports access flags into an external access
     * description.
     * @param accessFlags the module exports access flags.
     * @return the external module exports access description,
     *                    e.g. "<code>synthetic mandated </code>".
     */
    public static String externalExportsAccessFlags(int accessFlags)
    {
        return externalExportsAccessFlags(accessFlags, "");
    }


    /**
     * Converts internal module exports access flags into an external access
     * description.
     * @param accessFlags the module exports access flags.
     * @param prefix      a prefix that is added to each access modifier.
     * @return the external module exports access description,
     *                    e.g. "<code>static mandated </code>".
     */
    public static String externalExportsAccessFlags(int accessFlags, String prefix)
    {
        if (accessFlags == 0)
        {
            return EMPTY_STRING;
        }

        StringBuffer string = new StringBuffer(50);

        if ((accessFlags & AccessConstants.SYNTHETIC) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.SYNTHETIC).append(' ');
        }
        if ((accessFlags & AccessConstants.MANDATED) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.MANDATED).append(' ');
        }

        return string.toString();
    }


    /**
     * Converts internal module opens access flags into an external access
     * description.
     * @param accessFlags the module opens access flags.
     * @return the external module opens access description,
     *                    e.g. "<code>synthetic mandated </code>".
     */
    public static String externalOpensAccessFlags(int accessFlags)
    {
        return externalOpensAccessFlags(accessFlags, "");
    }


    /**
     * Converts internal module opens access flags into an external access
     * description.
     * @param accessFlags the module opens access flags.
     * @param prefix      a prefix that is added to each access modifier.
     * @return the external module opens access description,
     *                    e.g. "<code>static mandated </code>".
     */
    public static String externalOpensAccessFlags(int accessFlags, String prefix)
    {
        if (accessFlags == 0)
        {
            return EMPTY_STRING;
        }

        StringBuffer string = new StringBuffer(50);

        if ((accessFlags & AccessConstants.SYNTHETIC) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.SYNTHETIC).append(' ');
        }
        if ((accessFlags & AccessConstants.MANDATED) != 0)
        {
            string.append(prefix).append(JavaAccessConstants.MANDATED).append(' ');
        }

        return string.toString();
    }


    /**
     * Converts an internal class name, method name, and method descriptor to
     * an external method return type and name.
     * @param internalClassName        the internal name of the class of the method,
     *                                 e.g. "<code>mypackage/MyClass</code>".
     * @param internalMethodName       the internal method name,
     *                                 e.g. "<code>myMethod</code>" or
     *                                      "<code>&lt;init&gt;</code>".
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(II)Z</code>".
     * @return the external method return type and name,
     *                                 e.g. "<code>boolean myMethod</code>" or
     *                                      "<code>MyClass</code>".
     */
    private static String externalMethodReturnTypeAndName(String internalClassName,
                                                          String internalMethodName,
                                                          String internalMethodDescriptor)
    {
        return internalMethodName.equals(ClassConstants.METHOD_NAME_INIT) ?
            externalShortClassName(externalClassName(internalClassName)) :
            (externalMethodReturnType(internalMethodDescriptor) +
             ' ' +
             internalMethodName);
    }


    /**
     * Converts an internal method descriptor into an external method argument
     * description.
     * @param internalMethodDescriptor the internal method descriptor,
     *                                 e.g. "<code>(II)Z</code>".
     * @return the external method argument description,
     *                                 e.g. "<code>int,int</code>".
     */
    public static String externalMethodArguments(String internalMethodDescriptor)
    {
        StringBuffer externalMethodNameAndArguments = new StringBuffer();

        InternalTypeEnumeration internalTypeEnumeration =
            new InternalTypeEnumeration(internalMethodDescriptor);

        while (internalTypeEnumeration.hasMoreTypes())
        {
            externalMethodNameAndArguments.append(externalType(internalTypeEnumeration.nextType()));
            if (internalTypeEnumeration.hasMoreTypes())
            {
                externalMethodNameAndArguments.append(JavaTypeConstants.METHOD_ARGUMENTS_SEPARATOR);
            }
        }

        return externalMethodNameAndArguments.toString();
    }


    /**
     * Returns the internal package name of the given internal class name.
     * @param internalClassName the internal class name,
     *                          e.g. "<code>java/lang/Object</code>".
     * @return the internal package name,
     *                          e.g. "<code>java/lang</code>".
     */
    public static String internalPackageName(String internalClassName)
    {
        String internalPackagePrefix = internalPackagePrefix(internalClassName);
        int length = internalPackagePrefix.length();
        return length > 0 ?
            internalPackagePrefix.substring(0, length - 1) :
            "";
    }


    /**
     * Returns the internal package prefix of the given internal class name.
     * @param internalClassName the internal class name,
     *                          e.g. "<code>java/lang/Object</code>".
     * @return the internal package prefix,
     *                          e.g. "<code>java/lang/</code>".
     */
    public static String internalPackagePrefix(String internalClassName)
    {
        return internalClassName.substring(0, internalClassName.lastIndexOf(TypeConstants.PACKAGE_SEPARATOR,
                                                                            internalClassName.length() - 2) + 1);
    }


    /**
     * Returns the external package name of the given external class name.
     * @param externalClassName the external class name,
     *                          e.g. "<code>java.lang.Object</code>".
     * @return the external package name,
     *                          e.g. "<code>java.lang</code>".
     */
    public static String externalPackageName(String externalClassName)
    {
        String externalPackagePrefix = externalPackagePrefix(externalClassName);
        int length = externalPackagePrefix.length();
        return length > 0 ?
            externalPackagePrefix.substring(0, length - 1) :
            "";
    }


    /**
     * Returns the external package prefix of the given external class name.
     * @param externalClassName the external class name,
     *                          e.g. "<code>java.lang.Object</code>".
     * @return the external package prefix,
     *                          e.g. "<code>java.lang.</code>".
     */
    public static String externalPackagePrefix(String externalClassName)
    {
        return externalClassName.substring(0, externalClassName.lastIndexOf(JavaTypeConstants.PACKAGE_SEPARATOR,
                                                                            externalClassName.length() - 2) + 1);
    }
}
