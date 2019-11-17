/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import kotlinx.metadata.jvm.JvmMethodSignature;
import proguard.classfile.*;
import proguard.classfile.kotlin.flags.KotlinFunctionFlags;
import proguard.classfile.kotlin.visitors.*;
import proguard.classfile.visitor.MemberVisitor;
import proguard.util.*;

import java.util.*;

public class KotlinFunctionMetadata
extends SimpleVisitorAccepter
implements VisitorAccepter
{
    public String name;


    public List<KotlinContractMetadata> contracts;

    public KotlinTypeMetadata receiverType;

    public KotlinTypeMetadata returnType;

    public List<KotlinTypeParameterMetadata> typeParameters;

    public List<KotlinValueParameterMetadata> valueParameters;

    public KotlinVersionRequirementMetadata versionRequirement;

    public KotlinFunctionFlags flags;

    // Extensions.
    public JvmMethodSignature jvmSignature;
    public Method             referencedMethod;
    public Clazz              referencedMethodClass;

    // Functions with default parameters have a corresponding $default method.
    // this could be in the $DefaultImpls of an interface, so we need a class ref as well.
    public Method             referencedDefaultMethod;
    public Clazz              referencedDefaultMethodClass;

    // Interfaces with non-abstract methods have an implementation in a corresponding inner class called DefaultImpls.
    public Method             referencedDefaultImplementationMethod;
    public Clazz              referencedDefaultImplementationMethodClass;

    // The JVM internal name of the original class the lambda class where this function is copied from. This
    // information is present for lambdas copied from bodies of inline functions to the use site by kotlinc.
    public String             lambdaClassOriginName;
    public Clazz              referencedLambdaClassOrigin;


    public KotlinFunctionMetadata(int    flags,
                                  String name)
    {
        this.name  = name;
        this.flags = new KotlinFunctionFlags(flags);
    }


    public void accept(Clazz                              clazz,
                       KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                       KotlinFunctionVisitor              kotlinFunctionVisitor)
    {
        kotlinFunctionVisitor.visitFunction(clazz, kotlinDeclarationContainerMetadata, this);
    }


    public void accept(Clazz                            clazz,
                       KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata,
                       KotlinFunctionVisitor            kotlinFunctionVisitor)
    {
        kotlinFunctionVisitor.visitSyntheticFunction(clazz, kotlinSyntheticClassKindMetadata, this);
    }


    public void valueParametersAccept(Clazz                       clazz,
                                      KotlinMetadata              kotlinMetadata,
                                      KotlinValueParameterVisitor kotlinValueParameterVisitor)
    {
        for (KotlinValueParameterMetadata valueParameter : valueParameters)
        {
            valueParameter.accept(clazz, kotlinMetadata, this, kotlinValueParameterVisitor);
        }
    }


    public void typeParametersAccept(Clazz clazz, KotlinMetadata kotlinMetadata, KotlinTypeParameterVisitor kotlinTypeParameterVisitor)
    {
        for (KotlinTypeParameterMetadata typeParameter : typeParameters)
        {
            typeParameter.accept(clazz, kotlinMetadata, this, kotlinTypeParameterVisitor);
        }
    }


    public void returnTypeAccept(Clazz             clazz,
                                 KotlinMetadata    kotlinMetadata,
                                 KotlinTypeVisitor kotlinTypeVisitor)
    {
        //TODO unusual?
//        type.accept(clazz, kotlinDeclarationContainerMetadata, this, kotlinTypeVisitor);
        kotlinTypeVisitor.visitFunctionReturnType(clazz, kotlinMetadata, this, returnType);
    }


    public void receiverTypeAccept(Clazz             clazz,
                                   KotlinMetadata    kotlinMetadata,
                                   KotlinTypeVisitor kotlinTypeVisitor)
    {
        if (receiverType != null)
        {
            //TODO unusual?
//        receiverType.accept(clazz, kotlinDeclarationContainerMetadata, this, kotlinTypeVisitor);
            kotlinTypeVisitor.visitFunctionReceiverType(clazz,
                                                        kotlinMetadata,
                                                        this,
                                                        receiverType);
        }
    }


    public void contractsAccept(Clazz                 clazz,
                                KotlinMetadata        kotlinMetadata,
                                KotlinContractVisitor kotlinContractVisitor)
    {
        for (KotlinContractMetadata contract : contracts)
        {
            contract.accept(clazz, kotlinMetadata, this, kotlinContractVisitor);
        }
    }


    public void versionRequirementAccept(Clazz                           clazz,
                                         KotlinMetadata                  kotlinMetadata,
                                         KotlinVersionRequirementVisitor kotlinVersionRequirementVisitor)
    {
        if (versionRequirement != null)
        {
            versionRequirement.accept(clazz, kotlinMetadata, this, kotlinVersionRequirementVisitor);
        }
    }


    public void referencedMethodAccept(Clazz clazz, MemberVisitor methodVisitor)
    {
        referencedMethod.accept(clazz, methodVisitor);
    }


    // Implementations for Object.
    @Override
    public String toString()
    {
        return "Kotlin " +
               (flags.isSynthesized ? "synthesized " : "") +
               "function(" + name + ")";
    }
}
