/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.visitors;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;

/**
 * This KotlinMetadataVisitor visits all ValueParameters that it finds down the tree of the visit Kotlin Metadata.
 *
 * @author Tim Van Den Broecke
 */
public class AllValueParameterVisitor
implements KotlinMetadataVisitor,

           // Implementation interfaces.
           KotlinConstructorVisitor,
           KotlinPropertyVisitor,
           KotlinFunctionVisitor
{
    private final KotlinValueParameterVisitor delegate;

    public AllValueParameterVisitor(KotlinValueParameterVisitor delegate)
    {
        this.delegate = delegate;
    }


    // Implementations for KotlinMetadataVisitor.
    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz                              clazz,
                                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        kotlinDeclarationContainerMetadata.functionsAccept(clazz, this);
        kotlinDeclarationContainerMetadata.accept(clazz,
                                                  new AllKotlinPropertiesVisitor(this));
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
        delegate.onNewFunctionStart();
        kotlinConstructorMetadata.valueParametersAccept(clazz, kotlinClassKindMetadata, delegate);
    }


    // Implementations for KotlinPropertyVisitor.
    @Override
    public void visitAnyProperty(Clazz                              clazz,
                                 KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                 KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        delegate.onNewFunctionStart();
        kotlinPropertyMetadata.setterParametersAccept(clazz, kotlinDeclarationContainerMetadata, delegate);
    }


    // Implementations for KotlinFunctionVisitor.
    @Override
    public void visitAnyFunction(Clazz                  clazz,
                                 KotlinMetadata         kotlinMetadata,
                                 KotlinFunctionMetadata kotlinFunctionMetadata)
    {
        delegate.onNewFunctionStart();
        kotlinFunctionMetadata.valueParametersAccept(clazz, kotlinMetadata, delegate);
    }
}
