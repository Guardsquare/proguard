/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 GuardSquare NV
 */
package proguard.obfuscate.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.*;
import proguard.obfuscate.NameFactory;
import proguard.util.ProcessingFlags;

public class KotlinAliasNameObfuscator
implements   KotlinMetadataVisitor,

             // Implementation interfaces.
             KotlinTypeAliasVisitor,
             KotlinTypeVisitor
{
    private final NameFactory nameFactory;

    public KotlinAliasNameObfuscator(NameFactory nameFactory)
    {
        this.nameFactory = nameFactory;
    }


    // Implementations for KotlinMetadataVisitor.
    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}


    @Override
    public void visitKotlinDeclarationContainerMetadata(Clazz                              clazz,
                                                        KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata)
    {
        this.nameFactory.reset();
        kotlinDeclarationContainerMetadata.typeAliasesAccept(clazz, this);
    }

    @Override
    public void visitAnyType(Clazz clazz, KotlinTypeMetadata kotlinTypeMetadata) {}

    @Override
    public void visitTypeAlias(Clazz                              clazz,
                               KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                               KotlinTypeAliasMetadata            kotlinTypeAliasMetadata)
    {
        kotlinTypeAliasMetadata.expandedTypeAccept(clazz, kotlinDeclarationContainerMetadata, this);
    }

    @Override
    public void visitAliasExpandedType(Clazz clazz,
                                       KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                       KotlinTypeAliasMetadata kotlinTypeAliasMetadata,
                                       KotlinTypeMetadata kotlinTypeMetadata)
    {
        // If the expanded type class is kept, assume it's a public API so keep the alias also.
        if ((kotlinTypeMetadata.referencedClass.getProcessingFlags() & ProcessingFlags.DONT_OBFUSCATE) == 0)
        {
            kotlinTypeAliasMetadata.name = nameFactory.nextName();
        }
    }
}
