/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.fixer;

import proguard.classfile.*;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitors.*;
import proguard.classfile.util.*;

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
                    ClassUtil.internalPackageName(kotlinTypeMetadata.referencedTypeAlias.referencedDeclarationContainer.ownerClassName) +
                    ClassConstants.PACKAGE_SEPARATOR +
                    kotlinTypeMetadata.referencedTypeAlias.name;
            }

            kotlinTypeMetadata.aliasName = newName;
        }

        kotlinTypeMetadata.typeArgumentsAccept(clazz, this);
        kotlinTypeMetadata.outerClassAccept(   clazz, this);
        kotlinTypeMetadata.upperBoundsAccept(  clazz, this);
        kotlinTypeMetadata.abbreviationAccept( clazz, this);
    }
}