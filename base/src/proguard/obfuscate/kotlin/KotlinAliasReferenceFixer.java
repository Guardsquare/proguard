/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 GuardSquare NV
 */
package proguard.obfuscate.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.KotlinTypeVisitor;
import proguard.classfile.util.ClassUtil;

public class KotlinAliasReferenceFixer
implements KotlinTypeVisitor
{

    // Implementations for KotlinTypeVisitor.

    @Override
    public void visitAnyType(Clazz clazz, KotlinTypeMetadata kotlinTypeMetadata)
    {
        if (kotlinTypeMetadata.aliasName != null)
        {
            String newName;

            if (kotlinTypeMetadata.referencedTypeAlias.referencedDeclarationContainer.k == KotlinConstants.METADATA_KIND_CLASS)
            {
                // Type alias declared within a class.
                // Inner classes in Kotlin metadata have a '.' separator instead of the standard '$'.
                newName = ((KotlinClassKindMetadata)kotlinTypeMetadata.referencedTypeAlias.referencedDeclarationContainer).className + "." +
                          kotlinTypeMetadata.referencedTypeAlias.name;
            }
            else
            {
                // Top-level alias declaration.
                // Package is that of the file facade (which is a declaration container).
                newName =
                    ClassUtil.internalPackagePrefix(kotlinTypeMetadata.referencedTypeAlias.referencedDeclarationContainer.ownerClassName) +
                    kotlinTypeMetadata.referencedTypeAlias.name;
            }

            kotlinTypeMetadata.aliasName = newName;
        }
    }
}