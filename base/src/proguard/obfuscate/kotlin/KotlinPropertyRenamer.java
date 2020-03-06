/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 GuardSquare NV
 */

package proguard.obfuscate.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.KotlinPropertyVisitor;

/**
 * @author James Hamilton
 */
public class KotlinPropertyRenamer implements KotlinPropertyVisitor
{
    @Override
    public void visitAnyProperty(Clazz                              clazz,
                                 KotlinDeclarationContainerMetadata kotlinDeclarationContainerMetadata,
                                 KotlinPropertyMetadata             kotlinPropertyMetadata)
    {
        if (kotlinPropertyMetadata.getProcessingInfo() != null)
        {
            String originalName = kotlinPropertyMetadata.name;
            String newName      = (String)kotlinPropertyMetadata.getProcessingInfo();
            if (!originalName.equals(newName))
            {
                kotlinPropertyMetadata.name = newName;
            }
        }
    }
}
