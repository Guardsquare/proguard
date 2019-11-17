/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.initialize;

import proguard.classfile.*;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitors.*;
import proguard.classfile.util.*;

import java.util.Collections;

import static proguard.classfile.kotlin.KotlinConstants.DEFAULT_IMPLEMENTATIONS_SUFFIX;
import static proguard.classfile.kotlin.KotlinConstants.DEFAULT_METHOD_SUFFIX;

/**
 * This class initialises the $default methods, these are siblings to
 * methods that have default parameters.
 *
 * They have the same name as the original method but with a $default suffix.
 *
 * The descriptor differs from the original in a logical way - see getDescriptor method.
 *
 * The $default method will be in the same class or in the case of an interface, in the
 * interface's $DefaultImpls class.
 */
public class KotlinDefaultMethodInitializer
implements KotlinMetadataVisitor
{
    /*
    private static final boolean DEBUG = false;
    /*/
    public  static       boolean DEBUG = System.getProperty("kdmi") != null;
    //*/

    private final MemberFinder strictMemberFinder = new MemberFinder(false);

    private final ClassPool                        classPool;
    private final WarningPrinter                   warningPrinter;
    private final MyKotlinDefaultMethodInitializer kotlinDefaultMethodInitializer = new MyKotlinDefaultMethodInitializer();

    public KotlinDefaultMethodInitializer(ClassPool classPool, WarningPrinter warningPrinter)
    {
        this.classPool      = classPool;
        this.warningPrinter = warningPrinter;
    }

    // Implementations for KotlinMetadataVisitor.

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz                              clazz,
                                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        kotlinDefaultMethodInitializer.isInterface = false;
        kotlinDeclarationContainerMetadata.functionsAccept(clazz, kotlinDefaultMethodInitializer);
    }

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        kotlinDefaultMethodInitializer.isInterface = kotlinClassKindMetadata.flags.isInterface;
        kotlinClassKindMetadata.functionsAccept(clazz, kotlinDefaultMethodInitializer);
    }

    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz                            clazz,
                                                  KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        kotlinDefaultMethodInitializer.isInterface = false;
        kotlinSyntheticClassKindMetadata.functionsAccept(clazz, kotlinDefaultMethodInitializer);
    }

    // Initializer implementation class.

    private class MyKotlinDefaultMethodInitializer
    implements    KotlinFunctionVisitor
    {
        boolean isInterface = false;
        boolean hasDefaults = false;

        // Implementations for KotlinFunctionVisitor.

        @Override
        public void visitAnyFunction(Clazz                  clazz,
                                     KotlinMetadata         kotlinMetadata,
                                     KotlinFunctionMetadata kotlinFunctionMetadata)
        {
            if (kotlinFunctionMetadata.referencedMethod == null)
            {
                return;
            }

            // Use the jvm name because the jvm name might not match the metadata name.
            String methodName
                = kotlinFunctionMetadata.referencedMethod.getName(kotlinFunctionMetadata.referencedMethodClass);

            if (methodName.endsWith(DEFAULT_METHOD_SUFFIX))
            {
                return;
            }

            // Check if there are parameters with default values.
            hasDefaults = false;
            kotlinFunctionMetadata.valueParametersAccept(clazz, kotlinMetadata, (_clazz, vp) -> hasDefaults |= vp.flags.hasDefaultValue);

            if (!hasDefaults)
            {
                return;
            }

            String defaultMethodName = methodName + DEFAULT_METHOD_SUFFIX;
            String descriptor        = getDescriptor(kotlinFunctionMetadata);

            if (DEBUG)
            {
                System.out.println("SEARCHING FOR: " + defaultMethodName + "(" + kotlinFunctionMetadata.name + ")" + descriptor);
            }

            kotlinFunctionMetadata.referencedDefaultMethod =
                strictMemberFinder.findMethod(kotlinFunctionMetadata.referencedMethodClass,
                                              defaultMethodName,
                                              descriptor);

            kotlinFunctionMetadata.referencedDefaultMethodClass = strictMemberFinder.correspondingClass();

            if (kotlinFunctionMetadata.referencedDefaultMethod == null && isInterface)
            {
                Clazz defaultImplsClass = classPool.getClass(clazz.getName() + DEFAULT_IMPLEMENTATIONS_SUFFIX);

                if (defaultImplsClass != null)
                {
                    kotlinFunctionMetadata.referencedDefaultMethod =
                        strictMemberFinder.findMethod(defaultImplsClass, defaultMethodName, descriptor);

                    kotlinFunctionMetadata.referencedDefaultMethodClass = strictMemberFinder.correspondingClass();
                }
            }
        }
    }

    // Small helper methods.

    /**
     * $default methods are static.
     *
     * The descriptor matches the original method but with 2 or more extra parameters:
     *
     *  - the first parameter is an instance reference if the original method was not static.
     *  - the last 2+ parameters
     *     - int masks for every 32 parameters that encode which parameter values were passed
     *     - an object for future use? seems to be always null for now?
     *
     * @param kotlinFunctionMetadata a kotlin function
     * @return the descriptor for the default method.
     */
    private static String getDescriptor(KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        String originalDescriptor =
            kotlinFunctionMetadata.referencedMethod.getDescriptor(kotlinFunctionMetadata.referencedMethodClass);

        // Each int param encodes up to 32 parameters as being present or not.
        int requiredIntParams = 1 + (ClassUtil.internalMethodParameterCount(originalDescriptor) / 32);

        String descriptor = originalDescriptor.replace(
            ")",
            String.join("", Collections.nCopies(requiredIntParams, "I")) + "Ljava/lang/Object;)"
        );

        if ((kotlinFunctionMetadata.referencedMethod.getAccessFlags() & ClassConstants.ACC_STATIC) == 0)
        {
            descriptor = descriptor.replace("(", "(L" + kotlinFunctionMetadata.referencedMethodClass.getName() + ";");
        }

        return descriptor;
    }
}
