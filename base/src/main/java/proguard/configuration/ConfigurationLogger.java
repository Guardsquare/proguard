/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.configuration;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class can be injected in applications to log information about reflection
 * being used in the application code, and suggest appropriate ProGuard rules for
 * keeping the reflected classes, methods and/or fields.
 *
 * @author Johan Leys
 */
public class ConfigurationLogger implements Runnable
{
    private static final Logger logger = LogManager.getLogger(ConfigurationLogger.class);

    // Logging constants.
    private static final boolean LOG_ONCE         = false;
    private static final String  ANDROID_UTIL_LOG = "android.util.Log";
    private static final String  LOG_TAG          = "ProGuard";

    // Java API constants.
    private static final String CLASS_CLASS                     = "Class";
    private static final String CLASS_CLASS_LOADER              = "ClassLoader";

    private static final String METHOD_FOR_NAME                 = "forName";
    private static final String METHOD_LOAD_CLASS               = "loadClass";
    private static final String METHOD_GET_DECLARED_FIELD       = "getDeclaredField";
    private static final String METHOD_GET_FIELD                = "getField";
    private static final String METHOD_GET_DECLARED_CONSTRUCTOR = "getDeclaredConstructor";
    private static final String METHOD_GET_CONSTRUCTOR          = "getConstructor";
    private static final String METHOD_GET_DECLARED_METHOD      = "getDeclaredMethod";
    private static final String METHOD_GET_METHOD               = "getMethod";

    // Configuration constants.
    private static final String KEEP                = "-keep";
    private static final String KEEP_CLASS_MEMBERS  = "-keepclassmembers";

    // Configuration files.
    public static final String CLASS_MAP_FILENAME             = "classmap.txt";

    // Class processing flags.
    public static final int CLASS_KEPT                     = 1 << 0;
    public static final int ALL_DECLARED_CONSTRUCTORS_KEPT = 1 << 1;
    public static final int ALL_PUBLIC_CONSTRUCTORS_KEPT   = 1 << 2;
    public static final int ALL_DECLARED_FIELDS_KEPT       = 1 << 3;
    public static final int ALL_PUBLIC_FIELDS_KEPT         = 1 << 4;
    public static final int ALL_DECLARED_METHODS_KEPT      = 1 << 5;
    public static final int ALL_PUBLIC_METHODS_KEPT        = 1 << 6;
    public static final int CLASS_SHRUNK                   = 1 << 7;

    // Member processing flags.
    public static final int MEMBER_KEPT                    = 1 << 0;
    public static final int MEMBER_SHRUNK                  = 1 << 1;

    private static final String EMPTY_LINE = "\u00a0\n";

    private static final String INIT = "<init>";

    // Constants for the FNV1-a hashCode algorithm.
    private static final int FNV_HASH_INIT  = 0x811c9dc5;
    private static final int FNV_HASH_PRIME = 0x01000193;

    // Android logging method.
    private static final Method logMethod = getLogMethod();


    // Configuration and processing information about classes, class members, resources, ...

    // Map from obfuscated class names to class infos.
    private static final Map<String, ClassInfo>        sObfuscatedClassNameInfoMap = new HashMap<>();


    // Data structures to keep track of which suggestions have already been logged.

    // Set with missing class names.
    private static final Set<String>              sMissingClasses            = new HashSet<>();
    // Map from class name to missing field names.
    private static final Map<String, Set<String>> sMissingFields             = new HashMap<>();
    // Map from class name to missing method signatures.
    private static final Map<String, Set<String>> sMissingMethods            = new HashMap<>();
    // Set of classes on which getFields or getDeclaredFields is invoked.
    private static final Set<String>              sFieldListingCLasses       = new HashSet<>();
    // Set of classes on which getConstructors or getDeclaredConstructors is invoked.
    private static final Set<String>              sConstructorListingClasses = new HashSet<>();
    // Set of classes on which getMethods or getDeclaredMethods is invoked.
    private static final Set<String>              sMethodListingClasses      = new HashSet<>();


    static
    {
        // Initialize all configuration and processing information data structures.
        try
        {
            initializeClassMap();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    // Classes.

    /**
     * Check if a class that is loaded via Class.forName() is kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param reflectedClassName the name of the class that is introspected.
     * @param callingClassName   the class from which the reflection API is called.
     */
    public static void checkForName(String reflectedClassName,
                                    String callingClassName)
    {
        checkClass(CLASS_CLASS, METHOD_FOR_NAME, reflectedClassName, callingClassName);
    }


    /**
     * Check if a class that is loaded via ClassLoader.loadClass() is kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param reflectedClassName the name of the class that is introspected.
     * @param callingClassName   the class from which the reflection API is called.
     */
    public static void checkLoadClass(String reflectedClassName,
                                      String callingClassName)
    {
        checkClass(CLASS_CLASS_LOADER, METHOD_LOAD_CLASS, reflectedClassName, callingClassName);
    }


    /**
     * Check if a class that is loaded via reflection is kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param reflectedClassName the name of the class that is introspected.
     * @param callingClassName   the class from which the reflection API is called.
     */
    public static void checkClass(String reflectionClassName,
                                  String reflectionMethodName,
                                  String reflectedClassName,
                                  String callingClassName)
    {
        ClassInfo classInfo = sObfuscatedClassNameInfoMap.get(reflectedClassName);

        //classInfo will be null if the class was unavailable or is a library class,
        //in this case a keep rule doesn't make sense anyway
        if (classInfo == null) {
            return;
        }

        //at this point the class was in the original program class pool

        //do not log already kept classes (where the user already put a -keep rule in the config)
        if (isKept(classInfo))
        {
            return;
        }

        if (shouldLog(sMissingClasses, reflectedClassName))
        {
            log("The class '" + originalClassName(callingClassName) + "' " +
                "is calling " + reflectionClassName + "." + reflectionMethodName + " to retrieve\n" +
                "the class '" + reflectedClassName + "'" +
                (originalClassName(reflectedClassName).equals(reflectedClassName) ? "" : "(originally '" + originalClassName(reflectedClassName) + "')") +
                ", but there is no rule to keep the class.\n" +
                "You should consider preserving the class,\n" +
                "with a setting like:\n" +
                EMPTY_LINE +
                keepClassRule(originalClassName(reflectedClassName))
            );
        }
    }


    // Fields.

    /**
     * Check if a field that is retrieved via Class.getField() is kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param reflectedClass     the class that is introspected.
     * @param reflectedFieldName the field that is retrieved via reflection.
     * @param callingClassName   the class from which the reflection API is called.
     */
    public static void checkGetField(Class  reflectedClass,
                                     String reflectedFieldName,
                                     String callingClassName)
    {
        checkGetField(METHOD_GET_FIELD, reflectedClass, reflectedFieldName, callingClassName);
    }


    /**
     * Check if a field that is retrieved via Class.getDeclaredField() is kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param reflectedClass     the class that is introspected.
     * @param reflectedFieldName the field that is retrieved via reflection.
     * @param callingClassName   the class from which the reflection API is called.
     */
    public static void checkGetDeclaredField(Class  reflectedClass,
                                             String reflectedFieldName,
                                             String callingClassName)
    {
        checkGetField(METHOD_GET_DECLARED_FIELD, reflectedClass, reflectedFieldName, callingClassName);
    }


    /**
     * Check if a field that is retrieved via reflection is kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param reflectionMethodName the method of the Java reflection API that is invoked.
     * @param reflectedClass       the class that is introspected.
     * @param reflectedFieldName   the field that is retrieved via reflection.
     * @param callingClassName     the class from which the reflection API is called.
     */
    private static void checkGetField(String reflectionMethodName,
                                      Class  reflectedClass,
                                      String reflectedFieldName,
                                      String callingClassName)
    {
        MemberInfo fieldInfo = getDeclaringClass(reflectedClass, reflectedFieldName);

        if (fieldInfo != null &&
            !isKept(fieldInfo) &&
            shouldLog(reflectedClass, sMissingFields, reflectedFieldName))
        {
            String keepClassName = fieldInfo.declaringClassName;
            String reflectedClassName = originalClassName(reflectedClass.getName());
            log("The class '" + originalClassName(callingClassName) +
                "' is calling Class." + reflectionMethodName + "\n" +
                "on class '" + originalClassName(reflectedClass) +
                "' to retrieve the field '" + reflectedFieldName + "'" +
                (!reflectedClassName.equals(keepClassName) ? " (declared in class '" + keepClassName + "')": "") +
                 ",\n but there is no rule to keep the field." + "\n" +
                 "You should consider preserving it, with a rule like:" + "\n" +
                EMPTY_LINE +
                keepFieldRule(keepClassName, reflectedFieldName) + "\n" +
                EMPTY_LINE);
        }
    }

    /**
     * Check if the fields of a class whose fields are retrieved via Class.getDeclaredFields() are all kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param callingClassName  the class from which the reflection API is called.
     * @param reflectedClass    the class that is introspected.
     */
    public static void checkGetDeclaredFields(Class  reflectedClass,
                                              String callingClassName)
    {
        if (!allDeclaredFieldsKept(reflectedClass) &&
            shouldLog(sFieldListingCLasses, reflectedClass))
        {
            log("The class '" + originalClassName(callingClassName) +
                "' is calling Class." + "getDeclaredFields" + "\n" +
                "on class '" + originalClassName(reflectedClass) +
                "' to retrieve its fields.\n" +
                "You might consider preserving all fields with their original names,\n" +
                "with a setting like:\n" +
                EMPTY_LINE +
                keepAllFieldsRule(reflectedClass) + "\n" +
                EMPTY_LINE);
        }
    }


    /**
     * Check if the fields of a class whose fields are retrieved via Class.getFields() are all kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param reflectedClass    the class that is introspected.
     * @param callingClassName  the class from which the reflection API is called.
     */
    public static void checkGetFields(Class  reflectedClass,
                                      String callingClassName)
    {
        if (!allPublicFieldsKept(reflectedClass) &&
            shouldLog(sFieldListingCLasses, reflectedClass))
        {
            log("The class '" + originalClassName(callingClassName) +
                "' is calling Class." + "getFields" + "\n" +
                "on class '" + originalClassName(reflectedClass) +
                "' to retrieve its fields.\n" +
                "You might consider preserving all public fields with their original names,\n" +
                "with a setting like:\n" +
                EMPTY_LINE +
                keepAllPublicFieldsRule(reflectedClass) + "\n" +
                EMPTY_LINE);
        }
    }


    // Constructors.

    /**
     * Check if a constructor that is retrieved via Class.getDeclaredConstructor() is kept and if not,
     * log a keep rule suggestion for it.
     */
    public static void checkGetDeclaredConstructor(Class   reflectedClass,
                                                   Class[] constructorParameters,
                                                   String  callingClassName)
    {
        checkGetConstructor(METHOD_GET_DECLARED_CONSTRUCTOR, reflectedClass, constructorParameters, callingClassName);
    }


    /**
     * Check if a constructor that is retrieved via Class.getConstructor() is kept and if not,
     * log a keep rule suggestion for it.
     */
    public static void checkGetConstructor(Class   reflectedClass,
                                           Class[] constructorParameters,
                                           String  callingClassName)
    {
        checkGetConstructor(METHOD_GET_CONSTRUCTOR, reflectedClass, constructorParameters, callingClassName);
    }


    /**
     * Check if a constructor that is retrieved via reflection is kept and if not,
     * log a keep rule suggestion for it.
     */
    public static void checkGetConstructor(String  reflectionMethodName,
                                           Class   reflectedClass,
                                           Class[] constructorParameters,
                                           String  callingClassName)
    {
        MemberInfo constructorInfo = getDeclaringClass(reflectedClass,
                                                       INIT,
                                                       constructorParameters,
                                                       false);

        if (constructorInfo != null && !isKept(constructorInfo) && constructorParameters.length > 0) {
            String signature = signatureString(INIT, constructorParameters, true);
            if (shouldLog(reflectedClass, sMissingMethods, signature))
            {
                String keepClassName = reflectedClass.getName();

                log("The class '" + originalClassName(callingClassName) +
                    "' is calling Class." + reflectionMethodName + "\n" +
                    "on class '" + originalClassName(reflectedClass) + "' to retrieve\n" +
                    "the constructor with signature " + signature + ", " +
                    "but there is no rule to keep the constructor." + "\n" +
                    "You should consider preserving it, with a rule like:" + "\n" +
                    EMPTY_LINE +
                    keepConstructorRule(keepClassName, signature) + "\n" +
                    EMPTY_LINE);
            }
        }
    }


    /**
     * Check if the constructors of a class on which getDeclaredConstructors() is called are all kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param reflectedClass   the class that is introspected.
     * @param callingClassName the class from which the reflection API is called.
     */
    public static void checkGetDeclaredConstructors(Class reflectedClass,
                                                    String callingClassName)
    {
        if (!allDeclaredConstructorsKept(reflectedClass) &&
            shouldLog(sConstructorListingClasses, reflectedClass))
        {
            log("The class '" + originalClassName(callingClassName) +
                "' is calling Class.getDeclaredConstructors" + "\n" +
                "on class '" + originalClassName(reflectedClass) +
                "' to retrieve its constructors.\n" +
                "You might consider preserving all constructors with their original names,\n" +
                "with a setting like:\n" +
                EMPTY_LINE +
                keepAllConstructorsRule(reflectedClass) + "\n" +
                EMPTY_LINE);
        }
    }


    /**
     * Check if the constructors of a class on which getConstructors() is called are all kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param reflectedClass   the class that is introspected.
     * @param callingClassName the class from which the reflection API is called.
     */
    public static void checkGetConstructors(Class reflectedClass,
                                            String callingClassName)
    {
        if (!allPublicConstructorsKept(reflectedClass) &&
            shouldLog(sConstructorListingClasses, reflectedClass))
        {
            log("The class '" + originalClassName(callingClassName) +
                "' is calling Class.getConstructors" + "\n" +
                "on class '" + originalClassName(reflectedClass) +
                "' to retrieve its constructors.\n" +
                "You might consider preserving all constructors with their original names,\n" +
                "with a setting like:\n" +
                EMPTY_LINE +
                keepAllConstructorsRule(reflectedClass) + "\n" +
                EMPTY_LINE);
        }
    }


    // Methods.

    /**
     * Check if a method that is retrieved via Class.getDeclaredMethod() is kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param reflectedClass            the class that is introspected.
     * @param reflectedMethodName       the method that is retrieved via reflection.
     * @param reflectedMethodParameters the parameters of the method that is retrieved via reflection.
     * @param callingClassName          the class from which the reflection API is called.
     */
    public static void checkGetDeclaredMethod(Class   reflectedClass,
                                              String  reflectedMethodName,
                                              Class[] reflectedMethodParameters,
                                              String  callingClassName)
    {
        checkGetMethod(METHOD_GET_DECLARED_METHOD, reflectedClass, reflectedMethodName, reflectedMethodParameters, callingClassName);
    }


    /**
     * Check if a method that is retrieved via Class.getMethod() is kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param reflectedClass            the class that is introspected.
     * @param reflectedMethodName       the method that is retrieved via reflection.
     * @param reflectedMethodParameters the parameters of the method that is retrieved via reflection.
     * @param callingClassName          the class from which the reflection API is called.
     */
    public static void checkGetMethod(Class   reflectedClass,
                                      String  reflectedMethodName,
                                      Class[] reflectedMethodParameters,
                                      String  callingClassName)
    {
        checkGetMethod(METHOD_GET_METHOD, reflectedClass, reflectedMethodName, reflectedMethodParameters, callingClassName);
    }


    /**
     * Check if a method that is retrieved via reflection is kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param reflectionMethodName      the method of the Java reflection API that is invoked.
     * @param reflectedClass            the class that is introspected.
     * @param reflectedMethodName       the method that is retrieved via reflection.
     * @param reflectedMethodParameters the parameters of the method that is retrieved via reflection.
     * @param callingClassName          the class from which the reflection API is called.
     */
    private static void checkGetMethod(String  reflectionMethodName,
                                       Class   reflectedClass,
                                       String  reflectedMethodName,
                                       Class[] reflectedMethodParameters,
                                       String  callingClassName          )
    {
        MemberInfo methodInfo = getDeclaringClass(reflectedClass,
                                                  reflectedMethodName,
                                                  reflectedMethodParameters,
                                                  true);

        if (methodInfo != null && !isKept(methodInfo))
        {
            String signature = signatureString(reflectedMethodName, reflectedMethodParameters, true);
            if (shouldLog(reflectedClass, sMissingMethods, signature))
            {
                String keepClassName      =  methodInfo.declaringClassName;
                String reflectedClassName = originalClassName(reflectedClass);

                log("The class '" + originalClassName(callingClassName) +
                    "' is calling Class." + reflectionMethodName + "\n" +
                    "on class '" + reflectedClassName +
                    "' to retrieve the method\n" + signature +
                    (!reflectedClassName.equals(keepClassName) ? " (declared in class '" + keepClassName + "')": "") +
                    ", but there is no rule to keep the method." + "\n" +
                    "You should consider preserving it, with a rule like:" + "\n" +
                    EMPTY_LINE +
                    keepMethodRule(keepClassName, signature) + "\n" +
                    EMPTY_LINE);
            }
        }
    }


    /**
     * Check if the methods of a class on which getDeclaredMethods() is called are all kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param reflectedClass   the class that is introspected.
     * @param callingClassName the class from which the reflection API is called.
     */
    public static void checkGetDeclaredMethods(Class reflectedClass,
                                               String callingClassName)
    {
        if (!allDeclaredMethodsKept(reflectedClass) &&
            shouldLog(sMethodListingClasses, reflectedClass))
        {
            log("The class '" + originalClassName(callingClassName) +
                "' is calling Class." + "getDeclaredMethods" + "\n" +
                "on class '" + originalClassName(reflectedClass) +
                "' to retrieve its methods.\n" +
                "You might consider preserving all methods with their original names,\n" +
                "with a setting like:\n" +
                EMPTY_LINE +
                keepAllMethodsRule(reflectedClass) + "\n" +
                EMPTY_LINE);
        }
    }


    /**
     * Check if the methods of a class on which getMethods() is called are all kept and if not,
     * log a keep rule suggestion for it.
     *
     * @param reflectedClass   the class that is introspected.
     * @param callingClassName the class from which the reflection API is called.
     */
    public static void checkGetMethods(Class reflectedClass,
                                       String callingClassName)
    {
        if (!allPublicMethodsKept(reflectedClass) &&
            shouldLog(sMethodListingClasses, reflectedClass))
        {
            log("The class '" + originalClassName(callingClassName) +
                "' is calling Class." + "getMethods" + "\n" +
                "on class '" + originalClassName(reflectedClass) +
                "' to retrieve its methods.\n" +
                "You might consider preserving all public methods with their original names,\n" +
                "with a setting like:\n" +
                EMPTY_LINE +
                keepAllPublicMethodsRule(reflectedClass) + "\n" +
                EMPTY_LINE);
        }
    }


    /**
     * Returns whether the given member is kept on the specified class.
     *
     * @param memberInfo Member info
     * @return true if the member is kept
     */
    private static boolean isKept(MemberInfo memberInfo)
    {
        return memberInfo != null && (memberInfo.flags & MEMBER_KEPT) != 0;
    }


     /**
     * Is the member shrunk away?
     *
     * @param memberInfo Member information.
     * @return true if it's not in the classpool anymore.
     */
    private static boolean isShrunk(MemberInfo memberInfo)
    {
        return memberInfo != null && (memberInfo.flags & MEMBER_SHRUNK) != 0;
    }

    /**
     * Is the class kept?
     *
     * @param classInfo Class information.
     * @return true if it's kept.
     */
    private static boolean isKept(ClassInfo classInfo)
    {
        return classInfo != null && (classInfo.flags & CLASS_KEPT) != 0;
    }

    /**
     * Is the class shrunk away?
     *
     * @param classInfo Class information.
     * @return true if it's not in the classpool anymore.
     */
    private static boolean isShrunk(ClassInfo classInfo)
    {
        return classInfo != null && (classInfo.flags & CLASS_SHRUNK) != 0;
    }


    /**
     * Returns the declaring class of a given field by looking at the hierarchy
     * of the given class.
     */
    private static MemberInfo getDeclaringClass(Class   reflectedClass,
                                                String  fieldName)
    {
        String className = reflectedClass.getName();

        while (className != null)
        {
            ClassInfo classInfo = sObfuscatedClassNameInfoMap.get(className);
            if (classInfo != null)
            {
                int signatureHash = hashFnv1a32_UTF8(fieldName);
                for (int i = 0; i < classInfo.fieldHashes.length; i++)
                {
                    if (classInfo.fieldHashes[i] == signatureHash)
                    {
                        return new MemberInfo(classInfo.originalClassName, classInfo.fieldFlags[i]);
                    }
                }

                className = classInfo.superClassName;
            }
            else
            {
                className = null;
            }
        }

        return null;
    }


    /**
     * Returns the declaring class of a given method by looking at the hierarchy
     * of the given class.
     */
    private static MemberInfo getDeclaringClass(Class   reflectedClass,
                                                String  methodName,
                                                Class[] parameters,
                                                boolean checkHierarchy)
    {
        String className = reflectedClass.getName();

        while (className != null)
        {
            ClassInfo classInfo = sObfuscatedClassNameInfoMap.get(className);
            if (classInfo != null)
            {
                int signatureHash = hashFnv1a32_UTF8(signatureString(methodName, parameters, false));
                for (int i = 0; i < classInfo.methodHashes.length; i++)
                {
                    if (classInfo.methodHashes[i] == signatureHash)
                    {
                        return new MemberInfo(classInfo.originalClassName, classInfo.methodFlags[i]);
                    }
                }

                className = checkHierarchy ? classInfo.superClassName : null;
            }
            else
            {
                className = null;
            }
        }

        return null;
    }


    /**
     * Returns whether all declared fields of the given class are kept
     * (not including fields in super classes).
     */
    private static boolean allDeclaredFieldsKept(Class clazz)
    {
        ClassInfo classInfo = sObfuscatedClassNameInfoMap.get(clazz.getName());
        return classInfo != null && (classInfo.flags & ALL_DECLARED_FIELDS_KEPT) != 0;
    }


    /**
     * Returns whether all public fields of the given class are kept
     * (including fields in super classes).
     */
    private static boolean allPublicFieldsKept(Class clazz)
    {
        ClassInfo classInfo = sObfuscatedClassNameInfoMap.get(clazz.getName());
        return classInfo != null && (classInfo.flags & ALL_PUBLIC_FIELDS_KEPT) != 0;

    }


    /**
     * Returns whether all declared constructors of the given class are kept.
     */
    private static boolean allDeclaredConstructorsKept(Class clazz)
    {
        ClassInfo classInfo = sObfuscatedClassNameInfoMap.get(clazz.getName());
        return classInfo != null && (classInfo.flags & ALL_DECLARED_CONSTRUCTORS_KEPT) != 0;
    }


    /**
     * Returns whether all public constructors of the given class are kept.
     */
    private static boolean allPublicConstructorsKept(Class clazz)
    {
        ClassInfo classInfo = sObfuscatedClassNameInfoMap.get(clazz.getName());
        return classInfo != null && (classInfo.flags & ALL_PUBLIC_CONSTRUCTORS_KEPT) != 0;
    }


    /**
     * Returns whether all declared methods of the given class are kept
     * (not including methods in super classes).
     */
    private static boolean allDeclaredMethodsKept(Class clazz)
    {
        ClassInfo classInfo = sObfuscatedClassNameInfoMap.get(clazz.getName());
        return classInfo != null && (classInfo.flags & ALL_DECLARED_METHODS_KEPT) != 0;
    }


    /**
     * Returns whether all public methods of the given class are kept
     * (including methods in super classes).
     */
    private static boolean allPublicMethodsKept(Class clazz)
    {
        ClassInfo classInfo = sObfuscatedClassNameInfoMap.get(clazz.getName());
        return classInfo != null && (classInfo.flags & ALL_PUBLIC_METHODS_KEPT) != 0;
    }


    /**
     * Returns a String of concatenated class names for the given classes, separated by a comma.
     */
    private static String signatureString(String  methodName,
                                          Class[] parameters,
                                          boolean deobfuscate)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(methodName);
        builder.append("(");
        if (parameters != null)
        {
            for (int i = 0; i < parameters.length; i++)
            {
                if (i != 0)
                {
                    builder.append(",");
                }
                builder.append(deobfuscate ? originalClassName(parameters[i]) : parameters[i].getName());
            }
        }
        builder.append(")");
        return builder.toString();
    }


    // Implementations for Runnable.

    public void run()
    {
        printConfiguration();
    }


    private static void printConfiguration()
    {
        log("The following settings may help solving issues related to\n" +
            "missing classes, methods and/or fields:\n");

        for (String clazz : sMissingClasses)
        {
            log(keepClassRule(clazz) + "\n");
        }

        for (String clazz : sMissingMethods.keySet())
        {
            for (String method : sMissingMethods.get(clazz))
            {
                log(keepMethodRule(clazz, method) + "\n");
            }
        }

        for (String clazz : sMissingFields.keySet())
        {
            for (String field : sMissingFields.get(clazz))
            {
                log(keepFieldRule(clazz, field) + "\n");
            }
        }
    }


    // Helper methods to print rules.

    private static String keepClassRule(String className)
    {
        return KEEP + " class " + className;
    }


    private static String keepConstructorRule(String className,
                                              String originalMethodSignature)
    {
        return KEEP_CLASS_MEMBERS + " class " + originalClassName(className) + " {\n" +
               "    " + originalMethodSignature + ";\n" +
               "}";
    }


    private static String keepMethodRule(String className,
                                         String originalMethodSignature)
    {
        return KEEP_CLASS_MEMBERS + " class " + originalClassName(className) + " {\n" +
               "    *** " + originalMethodSignature + ";\n" +
               "}";
    }


    private static String keepFieldRule(String className,
                                        String fieldName)
    {
        return KEEP_CLASS_MEMBERS + " class " + originalClassName(className) + " {\n" +
               "    *** " + fieldName + ";\n" +
               "}";
    }


    private static String keepAllConstructorsRule(Class className)
    {
        return KEEP_CLASS_MEMBERS + " class " + originalClassName(className) + " {\n" +
               "    <init>(...);\n" +
               "}";
    }


    private static String keepAllMethodsRule(Class className)
    {
        return KEEP_CLASS_MEMBERS + " class " + originalClassName(className) + " {\n" +
               "    <methods>;\n" +
               "}";
    }


    private static String keepAllPublicMethodsRule(Class className)
    {
        return KEEP_CLASS_MEMBERS + " class " + originalClassName(className) + " {\n" +
               "    public <methods>;\n" +
               "}";
    }


    private static String keepAllFieldsRule(Class className)
    {
        return KEEP_CLASS_MEMBERS + " class " + originalClassName(className) + " {\n" +
               "    <fields>;\n" +
               "}";
    }

    private static String keepAllPublicFieldsRule(Class className)
    {
        return KEEP_CLASS_MEMBERS + " class " + originalClassName(className) + " {\n" +
               "    public <fields>;\n" +
               "}";
    }



    private static String originalClassName(Class className)
    {
        return originalClassName(className.getName());
    }


    private static String originalClassName(String className)
    {
        // The dimension of the array, if className is an array type.
        // 0 otherwise.
        int arrayDimension = className.lastIndexOf("[") + 1;

        // Normalize array-type class names.
        if (arrayDimension != 0)
        {
            // Remove brackets to look up the external class name.
            className = getExternalClassNameFromComponentType(className.substring(arrayDimension));
        }

        // Look up original class in mappings.
        ClassInfo classInfo = sObfuscatedClassNameInfoMap.get(className);
        String originalClassName = classInfo != null ? classInfo.originalClassName : className;
        return addArrayBrackets(originalClassName, arrayDimension);
    }


    /**
     * Appends the necessary array brackets to the end of the given classname.
     * eg: className      : "java.lang.String"
     *     arrayDimension : 2
     *     return         : "java.lang.String[][]"
     */
    private static String addArrayBrackets(String className, int arrayDimension)
    {
        StringBuilder sb = new StringBuilder(className);
        for (int index = 0; index < arrayDimension; index++)
        {
            sb.append("[]");
        }
        return sb.toString();
    }


    /**
     * Returns the external class name for a given component type
     * of an array class type.
     * <p>
     * The following are examples of what Class.getName() returns:
     * byte.class.getName()    : "byte"
     * String.class.getName()  : "java.lang.String"
     * byte[].class.getName()  : "[B"
     * String[].class.getName(): "[Ljava.lang.String;"
     *
     * @link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.3
     */
    private static String getExternalClassNameFromComponentType(String componentType)
    {
        switch (componentType.charAt(0))
        {
            case 'V':
                return "void";
            case 'Z':
                return "boolean";
            case 'B':
                return "byte";
            case 'C':
                return "char";
            case 'S':
                return "short";
            case 'I':
                return "int";
            case 'J':
                return "long";
            case 'F':
                return "float";
            case 'D':
                return "double";
            case 'L':
                return componentType.substring(1, componentType.length() - 1);
        }
        throw new IllegalArgumentException("Unknown component type ["+componentType+"]");
    }


    /**
     * Returns whether the given class is a library class or not.
     *
     * It's a library class if it's not in the classmap.txt file.
     */
    private static boolean isLibraryClass(Class clazz)
    {
        return !sObfuscatedClassNameInfoMap.containsKey(clazz.getName());
    }


    // Initialization.

    /**
     * Returns an Android logging method, or null if it can't be found.
     */
    private static Method getLogMethod()
    {
        try
        {
            Class<?> logClass = Class.forName(ANDROID_UTIL_LOG);
            return logClass.getMethod("w", String.class, String. class);
        }
        catch (Exception e)
        {
            return null;
        }
    }


    /**
     * Initializes all class-specific processing information.
     */
    private static void initializeClassMap() throws IOException
    {
        loadClassMap(ConfigurationLogger.class.getClassLoader().getResourceAsStream(CLASS_MAP_FILENAME), sObfuscatedClassNameInfoMap);
    }


    /**
     * Load a classmap.txt file into a map.
     *
     * @param inputStream Input stream from which to read the info.
     * @param map The map to load the info into.
     * @throws IOException If the input stream could not be read.
     */
    public static void loadClassMap(InputStream inputStream, Map<String, ClassInfo> map) throws IOException
    {
        if (inputStream == null)
        {
            return;
        }

        DataInputStream dataInputStream = new DataInputStream(inputStream);

        int classCount = dataInputStream.readInt();
        for (int i = 0; i < classCount; i++)
        {
            String originalClassName   = dataInputStream.readUTF ();
            String obfuscatedClassName = dataInputStream.readUTF ();
            String superClassName      = dataInputStream.readUTF ();

            short   flags              = dataInputStream.readShort();

            int fieldCount    = dataInputStream.readShort();
            int[] fieldHashes = new int[fieldCount];
            byte[] fieldFlags = new byte[fieldCount];
            for (int j = 0; j < fieldCount; j++)
            {
                // Name hash.
                fieldHashes[j] = dataInputStream.readInt();
                fieldFlags[j]  = dataInputStream.readByte();
            }

            int methodCount    = dataInputStream.readShort();
            int[] methodHashes = new int[methodCount];
            byte[] methodFlags = new byte[methodCount];
            for (int j = 0; j < methodCount; j++)
            {
                // Signature hash.
                methodHashes[j] = dataInputStream.readInt();
                methodFlags[j]  = dataInputStream.readByte();
            }

            ClassInfo classInfo = new ClassInfo(originalClassName,
                                                superClassName,
                                                flags,
                                                fieldHashes,
                                                fieldFlags,
                                                methodHashes,
                                                methodFlags);

            map.put(obfuscatedClassName, classInfo);
        }
    }


    // Logging utility methods.

    private static <T> boolean shouldLog(Class               reflectedClass,
                                         Map<String, Set<T>> classValuesMap,
                                         T                   value)
    {
        if (isLibraryClass(reflectedClass))
        {
            return false;
        }
        Set<T> values = computeIfAbsent(classValuesMap, reflectedClass.getName());

        return shouldLog(values, value);
    }


    private static boolean shouldLog(Set<String> classes,
                                     Class       reflectedClass)
    {
        return !isLibraryClass(reflectedClass) && shouldLog(classes, reflectedClass.getName());
    }


    private static <T> boolean shouldLog(Set<T> values,
                                         T      value)
    {
        return !LOG_ONCE || values.add(value);
    }


    private static <T> Set<T> computeIfAbsent(Map<String, Set<T>> map,
                                              String              key )
    {
        Set<T> set = map.get(key);
        if (set == null)
        {
            set = new HashSet<T>();
            map.put(key, set);
        }
        return set;
    }


    /**
     * Log a message, either on the Android Logcat, if available, or on the
     * Standard error output stream otherwise.
     *
     * @param message the message to be logged.
     */
    private static void log(String message)
    {
        if (logMethod != null)
        {
            try
            {
                logMethod.invoke(null, LOG_TAG, message);
            }
            catch (Exception e)
            {
                logger.error(message);
            }
        }
        else
        {
            logger.error(message);
        }
    }


    // Hashing utility methods.

    /**
     * Convenience method for computing the FNV-1a hash on the UTF-8 encoded byte representation of the given String.
     */
    private static int hashFnv1a32_UTF8(String string)
    {
        return hash(string, FNV_HASH_INIT);
    }


    /**
     * Convenience method for computing the FNV-1a hash on the UTF-8 encoded byte representation of the given String,
     * using the given initial hash value.
     */
    private static int hash(String string, int init)
    {
        return hash(string.getBytes(StandardCharsets.UTF_8), init);
    }


    /**
     * Computes a hash of the given bytes, using the FNV-1a hashing function, but with a custom inital hash value.
     */
    private static int hash(byte[] data, int init)
    {
        int hash = init;
        for (byte b : data)
        {
            hash ^= b;
            hash *= FNV_HASH_PRIME;
        }

        return hash;
    }


    /**
     * Container of processing information of a class.
     */
    public static class ClassInfo
    {
        final String originalClassName;
        final String superClassName;
        final short  flags;

        public final int[]  fieldHashes;
        final byte[] fieldFlags;
        public final int[]  methodHashes;
        final byte[] methodFlags;


        ClassInfo(String originalClassName,
                  String superClassName,
                  short  flags,
                  int[]  fieldHashes,
                  byte[] fieldFlags,
                  int[]  methodHashes,
                  byte[] methodFlags)
        {
            this.originalClassName = originalClassName;
            this.superClassName    = superClassName;
            this.flags             = flags;
            this.fieldHashes       = fieldHashes;
            this.fieldFlags        = fieldFlags;
            this.methodHashes      = methodHashes;
            this.methodFlags       = methodFlags;
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append(this.originalClassName);
            if (!superClassName.equals("")) sb.append(" extends ").append(superClassName);
            sb.append(" (");
            if ((this.flags & CLASS_KEPT) != 0) sb.append("kept"); else sb.append("not kept");
            if ((this.flags & CLASS_SHRUNK) != 0) sb.append(", shrunk"); else sb.append(", not shrunk");
            sb.append(")");
            sb.append(" ");
            sb.append(fieldHashes.length).append(" fields, ").append(methodHashes.length).append(" methods");
            return sb.toString();
        }
    }

    public static class MemberInfo
    {
        final String declaringClassName;
        final byte   flags;

        MemberInfo(String declaringClassName,
                   byte   flags)
        {
            this.declaringClassName = declaringClassName;
            this.flags              = flags;
        }

        public String toString()
        {
            return this.declaringClassName + " (" + ((this.flags & MEMBER_KEPT) != 0 ? "kept" : "not kept") + ", " + ((this.flags & MEMBER_SHRUNK) != 0 ? "shrunk" : "not shrunk") + ")";
        }
    }
}
