/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.initialize;

import kotlinx.metadata.jvm.JvmMethodSignature;
import proguard.classfile.*;
import proguard.classfile.attribute.EnclosingMethodAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitors.*;
import proguard.classfile.util.*;

/**
 * This KotlinMetadataVisitor initializes missing references for known cases where
 * KotlinPropertyMetadata refers to hard-to-find Java elements.
 */
public class KotlinInterClassReferenceInitializer
implements   KotlinMetadataVisitor
{
    /*
    private static final boolean DEBUG = false;
    /*/
    public  static       boolean DEBUG = System.getProperty("kicri") != null;
    //*/

    private final MemberFinder memberFinder = new MemberFinder(false);

    private final ClassPool      classPool;
    private final WarningPrinter warningPrinter;

    public KotlinInterClassReferenceInitializer(ClassPool classPool, WarningPrinter warningPrinter)
    {
        this.classPool      = classPool;
        this.warningPrinter = warningPrinter;
    }

    // Implementations for KotlinMetadataVisitor.

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        // Some properties in Companion classes have their backing field on the parent clazz.
        kotlinClassKindMetadata.companionAccept(new AllKotlinPropertiesVisitor(
                                                new MyInterClassReferenceInitializer(clazz)));

        // Some interface properties have their synthetic methods in a $DefaultImpls clazz.
        if (kotlinClassKindMetadata.flags.isInterface)
        {
            Clazz defaultImplsClass = classPool.getClass(clazz.getName() + "$DefaultImpls");
            kotlinClassKindMetadata.accept(clazz,
                                           new AllKotlinPropertiesVisitor(
                                           new MyInterClassReferenceInitializer(defaultImplsClass)));
        }
    }

    @Override
    public void visitKotlinMultiFilePartMetadata(Clazz                           clazz,
                                                 KotlinMultiFilePartKindMetadata kotlinMultiFilePartKindMetadata)
    {
        // Some file parts have their backing field on the facade clazz.
        kotlinMultiFilePartKindMetadata.accept(clazz,
                                               new AllKotlinPropertiesVisitor(
                                               new MyInterClassReferenceInitializer(
                                                   kotlinMultiFilePartKindMetadata.referencedFacade)));
    }


    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz clazz,
                                                  KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        kotlinSyntheticClassKindMetadata.functionsAccept(clazz, new MyInterClassSyntheticFunctionInitializer());
    }

    private static class MyInterClassSyntheticFunctionInitializer
    extends    SimplifiedVisitor
    implements KotlinFunctionVisitor,
               AttributeVisitor
    {
        private KotlinFunctionMetadata currentFunction;

        @Override
        public void visitAnyFunction(Clazz                  clazz,
                                     KotlinMetadata         kotlinMetadata,
                                     KotlinFunctionMetadata kotlinFunctionMetadata) {}

        @Override
        public void visitSyntheticFunction(Clazz                            clazz,
                                           KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata,
                                           KotlinFunctionMetadata           kotlinFunctionMetadata)
        {
            if (kotlinFunctionMetadata.referencedMethod == null)
            {
                this.currentFunction = kotlinFunctionMetadata;
                clazz.attributeAccept(ClassConstants.ATTR_EnclosingMethod, this);
            }
        }

        @Override
        public void visitEnclosingMethodAttribute(Clazz clazz, EnclosingMethodAttribute enclosingMethodAttribute)
        {
            Method enclosingMethod      = enclosingMethodAttribute.referencedMethod;
            Clazz  enclosingMethodClass = enclosingMethodAttribute.referencedClass;

            if (DEBUG && currentFunction.jvmSignature == null && enclosingMethod != null)
            {
                System.err.println("EnclosingMethod is initialised for " + currentFunction.name + " but jvmSignature is null");
            }

            //PROBBUG There are some EnclosingMethod attributes that refer to methods that don't exist.
            //        This seems like a bug as $$forInline should have been stripped, it happens for the following:
            //           collect$$forInline, jvmSignature: collect(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
            //           emit$$forInline,    jvmSignature: null
            /*if (enclosingMethod == null && enclosingMethodAttribute.getName(clazz).contains("$$forInline"))
            {
                if (DEBUG)
                {
                    System.err.println(
                        "Found EnclosingMethod with incorrect name: " +
                        enclosingMethodAttribute.getName(clazz) +
                        " [" + currentFunction.jvmSignature + "]"
                    );
                }

                if (currentFunction.jvmSignature != null)
                {
                    enclosingMethod =
                        memberFinder.findMethod(
                            enclosingMethodClass,
                            currentFunction.jvmSignature.getName(),
                            currentFunction.jvmSignature.getDesc()
                        );
                }
                else
                {
                    MemberCounter memberCounter = new MemberCounter();
                    enclosingMethodClass.accept(new AllMethodVisitor(
                                                new MemberNameFilter(currentFunction.name,
                                                    memberCounter)));

                    if (memberCounter.getCount() == 1)
                    {
                        enclosingMethod =
                            memberFinder.findMethod(
                                enclosingMethodClass,
                                currentFunction.name,
                                null
                            );
                    }
                    else if (memberCounter.getCount() > 1)
                    {
                        warningPrinter.print(currentFunction.referencedMethodClass.getName(),
                                             "Found multiple referencedMethod candidates for " + currentFunction.name);
                    }
                }

                if (enclosingMethod != null)
                {
                    enclosingMethodAttribute.referencedMethod = enclosingMethod;
                }
            }*/

            if (enclosingMethod != null)
            {
                // Note: jvmSignature may have been null even when the enclosingMethod was originally initialised.
                if (currentFunction.jvmSignature == null)
                {
                    currentFunction.jvmSignature =
                        new JvmMethodSignature(
                            enclosingMethod.getName(enclosingMethodClass),
                            enclosingMethod.getDescriptor(enclosingMethodClass)
                        );

                    if (DEBUG)
                    {
                        System.out.println("Setting jvmSignature for " + currentFunction.name + " to " + currentFunction.jvmSignature);
                    }
                }

                currentFunction.referencedMethod      = enclosingMethod;
                currentFunction.referencedMethodClass = enclosingMethodClass;
            }
        }
    }

    private class MyInterClassReferenceInitializer
    implements KotlinPropertyVisitor
    {
        private final Clazz clazz;

        MyInterClassReferenceInitializer(Clazz clazz)
        {
            this.clazz = clazz;
        }

        // Implementations for KotlinPropertyVisitor.

        @Override
        public void visitAnyProperty(Clazz                              clazz,
                                     KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                     KotlinPropertyMetadata             kotlinPropertyMetadata)
        {
            if (kotlinPropertyMetadata.backingFieldSignature  != null &&
                kotlinPropertyMetadata.referencedBackingField == null)
            {
                kotlinPropertyMetadata.referencedBackingField =
                    memberFinder.findField(this.clazz,
                                           kotlinPropertyMetadata.backingFieldSignature.getName(),
                                           kotlinPropertyMetadata.backingFieldSignature.getDesc());

                kotlinPropertyMetadata.referencedBackingFieldClass = memberFinder.correspondingClass();
            }

            /* Default implementations of methods are stored in the $DefaultImpls class not in the
             * interface class.
             *
             * We fix any synthetic methods for annotations that were not already set (because
             * they weren't found the in interface class) - these will be found in the $DefaultImpls class.
             *
             * Not necessary for geters/setters as they are stored on the interface class itself.
             */
            if (kotlinPropertyMetadata.syntheticMethodForAnnotations           != null &&
                kotlinPropertyMetadata.referencedSyntheticMethodForAnnotations == null)
            {
                kotlinPropertyMetadata.referencedSyntheticMethodForAnnotations =
                    memberFinder.findMethod(this.clazz,
                                            kotlinPropertyMetadata.syntheticMethodForAnnotations.getName(),
                                            kotlinPropertyMetadata.syntheticMethodForAnnotations.getDesc());

                kotlinPropertyMetadata.referencedSyntheticMethodClass = memberFinder.correspondingClass();
            }
        }
    }
}
