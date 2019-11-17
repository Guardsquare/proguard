/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.flags.KotlinValueParameterFlags;
import proguard.classfile.kotlin.visitors.*;
import proguard.util.*;


public class KotlinValueParameterMetadata
extends SimpleVisitorAccepter
implements VisitorAccepter
{
    public String parameterName;

    // Type will always be set

    public KotlinTypeMetadata type;

    // Vararg will also be set if it's a vararg ValueParameter

    public KotlinTypeMetadata varArgElementType;

    public KotlinValueParameterFlags flags;

    public int index;

    public KotlinValueParameterMetadata(int    flags,
                                        int    index,
                                        String parameterName)
    {
        this.parameterName = parameterName;
        this.index         = index;
        this.flags         = new KotlinValueParameterFlags(flags);
    }


    public void accept(Clazz                       clazz,
                       KotlinClassKindMetadata     kotlinClassKindMetadata,
                       KotlinConstructorMetadata   kotlinConstructorMetadata,
                       KotlinValueParameterVisitor kotlinValueParameterVisitor)
    {
        kotlinValueParameterVisitor.visitConstructorValParameter(clazz,
                                                                 kotlinClassKindMetadata,
                                                                 kotlinConstructorMetadata,
                                                                 this);
    }


    public void accept(Clazz                       clazz,
                       KotlinMetadata              kotlinMetadata,
                       KotlinFunctionMetadata      kotlinFunctionMetadata,
                       KotlinValueParameterVisitor kotlinValueParameterVisitor)
    {
        kotlinValueParameterVisitor.visitFunctionValParameter(clazz,
                                                              kotlinMetadata,
                                                              kotlinFunctionMetadata,
                                                              this);
    }


    public void accept(Clazz                              clazz,
                       KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                       KotlinPropertyMetadata             kotlinPropertyMetadata,
                       KotlinValueParameterVisitor        kotlinValueParameterVisitor)
    {
        kotlinValueParameterVisitor.visitPropertyValParameter(clazz,
                                                              kotlinDeclarationContainerMetadata,
                                                              kotlinPropertyMetadata,
                                                              this);
    }


    public void typeAccept(Clazz                  clazz,
                           KotlinMetadata         kotlinMetadata,
                           KotlinFunctionMetadata kotlinFunctionMetadata,
                           KotlinTypeVisitor      kotlinTypeVisitor)
    {
        kotlinTypeVisitor.visitFunctionValParamType(clazz,
                                                    kotlinMetadata,
                                                    kotlinFunctionMetadata,
                                                    this,
                                                    type);

        if (varArgElementType != null)
        {
            kotlinTypeVisitor.visitFunctionValParamVarArgType(clazz,
                                                              kotlinMetadata,
                                                              kotlinFunctionMetadata,
                                                              this,
                                                              varArgElementType);
        }
    }


    public void typeAccept(Clazz                     clazz,
                           KotlinClassKindMetadata   kotlinClassKindMetadata,
                           KotlinConstructorMetadata kotlinConstructorMetadata,
                           KotlinTypeVisitor         kotlinTypeVisitor)
    {
        kotlinTypeVisitor.visitConstructorValParamType(clazz,
                                                       kotlinClassKindMetadata,
                                                       kotlinConstructorMetadata,
                                                       this,
                                                       type);

        if (varArgElementType != null)
        {
            kotlinTypeVisitor.visitConstructorValParamVarArgType(clazz,
                                                                 kotlinClassKindMetadata,
                                                                 kotlinConstructorMetadata,
                                                                 this,
                                                                 varArgElementType);
        }
    }


    public void typeAccept(Clazz                              clazz,
                           KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                           KotlinPropertyMetadata             kotlinPropertyMetadata,
                           KotlinTypeVisitor                  kotlinTypeVisitor)
    {
        kotlinTypeVisitor.visitPropertyValParamType(clazz,
                                                    kotlinDeclarationContainerMetadata,
                                                    kotlinPropertyMetadata,
                                                    this,
                                                    type);

        if (varArgElementType != null)
        {
            kotlinTypeVisitor.visitPropertyValParamVarArgType(clazz,
                                                              kotlinDeclarationContainerMetadata,
                                                              kotlinPropertyMetadata,
                                                              this,
                                                              varArgElementType);
        }
    }


    // Implementations for Object.
    @Override
    public String toString()
    {
        return "Kotlin value parameter '" + parameterName + "'";
    }
}
