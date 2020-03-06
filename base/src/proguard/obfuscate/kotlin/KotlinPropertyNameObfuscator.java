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

public class KotlinPropertyNameObfuscator
implements   KotlinMetadataVisitor,

             // Implementation interfaces.
             KotlinPropertyVisitor
{
    private final NameFactory nameFactory;

    public KotlinPropertyNameObfuscator(NameFactory nameFactory)
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
        kotlinDeclarationContainerMetadata.propertiesAccept(clazz, this);
    }

    // Implementations for KotlinPropertyVisitor.
    @Override
    public void visitAnyProperty(Clazz                              clazz,
                                 KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                 KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        // Don't rename a property if its backing field or
        // any accessor are explicitly kept.
        if ((kotlinPropertyMetadata.referencedBackingField != null &&
             (kotlinPropertyMetadata.referencedBackingField.getProcessingFlags() & ProcessingFlags.DONT_OBFUSCATE) != 0) ||

            (kotlinPropertyMetadata.referencedGetterMethod != null &&
             (kotlinPropertyMetadata.referencedGetterMethod.getProcessingFlags() & ProcessingFlags.DONT_OBFUSCATE) != 0) ||

            (kotlinPropertyMetadata.referencedSetterMethod != null &&
             (kotlinPropertyMetadata.referencedSetterMethod.getProcessingFlags() & ProcessingFlags.DONT_OBFUSCATE) != 0))
        {
            return;
        }

        kotlinPropertyMetadata.setProcessingInfo(nameFactory.nextName());
    }
}
