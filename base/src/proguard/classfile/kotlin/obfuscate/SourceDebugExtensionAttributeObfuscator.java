/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.obfuscate;

import proguard.classfile.Clazz;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This class sets the SourceDebugExtension attribute to a basic minimum
 * SMAP entry. See DGD-1417.
 */
public class SourceDebugExtensionAttributeObfuscator
extends      SimplifiedVisitor
implements   AttributeVisitor
{
    private static final String MINIMUM_SMAP = "SMAP\n" +       // SMAP Header.
                                               "\n" +           // Name of generated file (blank).
                                               "Kotlin\n" +     // Default stratum name.
                                               "*S Kotlin\n" +  // Kotlin stratum.
                                               "*F\n" +         // File section.
                                               "+ 1 \n" +       // File ID 1, blank name.
                                               "\n" +           // File ID 1 source path.
                                               "*L\n" +         // Lines section.
                                               "1#1,1:1\n" +    // File 1#line 1,repeatCount:outputStartLine
                                               "*E";            // End.

    @Override
    public void visitSourceDebugExtensionAttribute(Clazz                         clazz,
                                                   SourceDebugExtensionAttribute sourceDebugExtensionAttribute)
    {
        sourceDebugExtensionAttribute.info = MINIMUM_SMAP.getBytes();
        sourceDebugExtensionAttribute.u4attributeLength = sourceDebugExtensionAttribute.info.length;
    }

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}
}
