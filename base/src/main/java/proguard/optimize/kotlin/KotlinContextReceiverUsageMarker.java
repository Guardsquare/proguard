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
package proguard.optimize.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinConstructorMetadata;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinFunctionMetadata;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.KotlinPropertyMetadata;
import proguard.classfile.kotlin.KotlinTypeMetadata;
import proguard.classfile.kotlin.visitor.KotlinConstructorVisitor;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.classfile.kotlin.visitor.KotlinPropertyVisitor;
import proguard.optimize.MethodDescriptorShrinker;
import proguard.optimize.info.ParameterUsageMarker;
import proguard.optimize.info.ProgramMethodOptimizationInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

import static proguard.classfile.AccessConstants.STATIC;
import static proguard.optimize.info.ParameterUsageMarker.markParameterUsed;

/**
 * This Kotlin metadata visitor marks the parameters of methods used,
 * using the {@link ParameterUsageMarker}, if those parameters correspond
 * to Kotlin context receivers.
 * <p>
 * This is done to ensure that the list of context receivers does not
 * get out of sync, as otherwise unused context receiver parameters may be removed.
 * <p>
 * On the Java method level, each context receiver is passed as a leading parameter
 * to a function's JVM method, a property getter/setter method, or a constructor.
 * <p>
 * <code>
 *    context(Foo, Bar)
 *    fun foo(string:String) { }
 *    // -> public static void foo(LFoo;LBar;Ljava/lang/String;)V
 * </code>
 * <p>
 * TODO(T18173): Implement proper shrinking of context receivers when the underlying
 *               parameter is not used i.e. consistently remove context receivers when the
 *               underlying parameter is removed in e.g. {@link MethodDescriptorShrinker}
 *
 * @see ParameterUsageMarker
 * @see MethodDescriptorShrinker
 *
 * @author James Hamilton
 */
public class KotlinContextReceiverUsageMarker implements
        KotlinMetadataVisitor,
        KotlinFunctionVisitor,
        KotlinConstructorVisitor,
        KotlinPropertyVisitor
{

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) { }

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        kotlinClassKindMetadata.constructorsAccept(clazz, this);
    }

    @Override
    public void visitAnyFunction(Clazz clazz, KotlinMetadata kotlinMetadata, KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        markContextReceiverParameters(kotlinFunctionMetadata.contextReceivers,
            kotlinFunctionMetadata.referencedMethod,
            kotlinFunctionMetadata.referencedDefaultMethod);
    }

    @Override
    public void visitConstructor(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata, KotlinConstructorMetadata kotlinConstructorMetadata)
    {
        markContextReceiverParameters(kotlinClassKindMetadata.contextReceivers,
            kotlinConstructorMetadata.referencedMethod
        );
    }

    @Override
    public void visitAnyProperty(Clazz clazz, KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata, KotlinPropertyMetadata kotlinPropertyMetadata)
    {
        markContextReceiverParameters(kotlinPropertyMetadata.contextReceivers,
            kotlinPropertyMetadata.referencedGetterMethod,
            kotlinPropertyMetadata.referencedSetterMethod);
    }

    private void markContextReceiverParameters(List<KotlinTypeMetadata> contextReceivers, Method...methods)
    {
        if (contextReceivers == null)
        {
            return;
        }

        Function<Method, Boolean> isStatic = method -> (method.getAccessFlags() & STATIC) != 0;

        // Mark all the parameters of the given methods
        // at each context receiver index as used.
        IntStream
            .range(0, contextReceivers.size())
            .forEach(paramIndex -> Arrays.stream(methods)
                .filter(Objects::nonNull)
                .filter(it -> it.getProcessingInfo() instanceof ProgramMethodOptimizationInfo)
                .forEachOrdered(it -> markParameterUsed(it, isStatic.apply(it) ? paramIndex : paramIndex + 1)));
    }
}
