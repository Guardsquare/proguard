/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
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
