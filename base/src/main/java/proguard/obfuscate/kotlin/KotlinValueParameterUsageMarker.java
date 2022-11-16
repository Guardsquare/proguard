/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
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
package proguard.obfuscate.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.Member;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinConstructorMetadata;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinFunctionMetadata;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.KotlinPropertyMetadata;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata;
import proguard.classfile.kotlin.KotlinValueParameterMetadata;
import proguard.classfile.kotlin.visitor.AllPropertyVisitor;
import proguard.classfile.kotlin.visitor.KotlinConstructorVisitor;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.classfile.kotlin.visitor.KotlinPropertyVisitor;
import proguard.classfile.kotlin.visitor.KotlinValueParameterVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.util.Processable;

import static proguard.util.ProcessingFlags.DONT_OBFUSCATE;

/**
 * This KotlinMetadataVisitor marks ValueParameters of constructors, properties and functions
 * if their referenced method is not obfuscated.
 */
public class KotlinValueParameterUsageMarker
implements KotlinMetadataVisitor,

           // Implementation interfaces.
           KotlinConstructorVisitor,
           KotlinPropertyVisitor,
           KotlinFunctionVisitor,
           MemberVisitor,
           KotlinValueParameterVisitor
{
    // A processing info flag to indicate the attribute is being used.
    private static final Object USED = new Object();

    private boolean keepParameterInfo;

    // Implementations for KotlinMetadataVisitor.

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz                              clazz,
                                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        kotlinDeclarationContainerMetadata.functionsAccept(clazz, this);
        kotlinDeclarationContainerMetadata.accept(clazz,
                                                  new AllPropertyVisitor(this));
    }

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        kotlinClassKindMetadata.constructorsAccept(clazz, this);
        visitKotlinDeclarationContainerMetadata(clazz, kotlinClassKindMetadata);
    }

    @Override
    public void visitKotlinSyntheticClassMetadata(Clazz clazz,
                                                  KotlinSyntheticClassKindMetadata kotlinSyntheticClassKindMetadata)
    {
        kotlinSyntheticClassKindMetadata.functionsAccept(clazz, this);
    }

    // Implementations for KotlinConstructorVisitor.

    @Override
    public void visitConstructor(Clazz                     clazz,
                                 KotlinClassKindMetadata   kotlinClassKindMetadata,
                                 KotlinConstructorMetadata kotlinConstructorMetadata)
    {
        keepParameterInfo = false;
        kotlinConstructorMetadata.referencedMethodAccept(clazz, this);

        if (keepParameterInfo)
        {
            kotlinConstructorMetadata.valueParametersAccept(clazz, kotlinClassKindMetadata, this);
        }
    }

    // Implementations for KotlinPropertyVisitor.

    @Override
    public void visitAnyProperty(Clazz                              clazz,
                                 KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                 KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        keepParameterInfo = false;
        if (kotlinPropertyMetadata.referencedSetterMethod != null)
        {
            kotlinPropertyMetadata.referencedSetterMethod.accept(clazz, this);
        }

        if (keepParameterInfo)
        {
            kotlinPropertyMetadata.setterParametersAccept(clazz, kotlinDeclarationContainerMetadata, this);
        }
    }

    // Implementations for KotlinFunctionVisitor.

    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        keepParameterInfo = false;
        kotlinFunctionMetadata.referencedMethodAccept(this);

        if (keepParameterInfo)
        {
            kotlinFunctionMetadata.valueParametersAccept(clazz, kotlinMetadata, this);
        }
    }

    // Implementations for MemberVisitor

    @Override
    public void visitAnyMember(Clazz clazz, Member member) {}


    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        keepParameterInfo = (programMethod.getProcessingFlags() & DONT_OBFUSCATE) != 0;
    }


    // Implementations for KotlinValueParameterVisitor.
    @Override
    public void visitAnyValueParameter(Clazz clazz, KotlinValueParameterMetadata kotlinValueParameterMetadata)
    {
        markAsUsed(kotlinValueParameterMetadata);
    }


    // Small utility methods.

    /**
     * Marks the given Processable as being used (or useful).
     * In this context, the Processable will be a KotlinValueParameter object.
     */
    private static void markAsUsed(Processable processable)
    {
        processable.setProcessingInfo(USED);
    }


    /**
     * Returns whether the given Processable has been marked as being used.
     * In this context, the Processable will be a KotlinValueParameter object.
     */
    static boolean isUsed(Processable processable)
    {
        return processable.getProcessingInfo() == USED;
    }
}
