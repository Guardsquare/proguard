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
package proguard.classfile.attribute.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This <code>AttributeVisitor</code> delegates its visits to another given
 * <code>AttributeVisitor</code>, but only when the visited attribute has the proper
 * processing flags.
 * <p>
 *
 * @see proguard.util.ProcessingFlags
 *
 * @author Johan Leys
 */
public class AttributeProcessingFlagFilter
extends      SimplifiedVisitor
implements   AttributeVisitor
{
    private final int              requiredSetProcessingFlags;
    private final int              requiredUnsetProcessingFlags;
    private final AttributeVisitor attributeVisitor;


    /**
     * Creates a new AttributeProcessingFlagFilter.
     *
     * @param requiredSetProcessingFlags   the attribute processing flags that should be set.
     * @param requiredUnsetProcessingFlags the attribute processing flags that should beunset.
     * @param attributeVisitor             the <code>AttributeVisitor</code> to which visits will be delegated.
     */
    public AttributeProcessingFlagFilter(int              requiredSetProcessingFlags,
                                         int              requiredUnsetProcessingFlags,
                                         AttributeVisitor attributeVisitor)
    {
        this.requiredSetProcessingFlags   = requiredSetProcessingFlags;
        this.requiredUnsetProcessingFlags = requiredUnsetProcessingFlags;
        this.attributeVisitor             = attributeVisitor;
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        if (accepted(codeAttribute.processingFlags))
        {
            codeAttribute.accept(clazz, method, attributeVisitor);
        }
    }

    // Small utility methods.

    private boolean accepted(int processingFlags)
    {
        return (requiredSetProcessingFlags & ~processingFlags) == 0 &&
               (requiredUnsetProcessingFlags &  processingFlags) == 0;
    }
}
