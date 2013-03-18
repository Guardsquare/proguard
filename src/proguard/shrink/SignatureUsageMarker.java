/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2013 Eric Lafortune (eric@graphics.cornell.edu)
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

package proguard.shrink;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.ClassVisitor;

/**
 * This AttributeVisitor recursively marks all Signature attributes that it
 * visits and that point to used classes.
 *
 * @see UsageMarker
 *
 * @author Eric Lafortune
 */
public class SignatureUsageMarker
extends      SimplifiedVisitor
implements   AttributeVisitor,
             ClassVisitor,
             ConstantVisitor
{
    private final UsageMarker usageMarker;

    // Fields acting as a return parameters for several methods.
    private boolean attributeUsed;


    /**
     * Creates a new SignatureUsageMarker.
     * @param usageMarker the usage marker that is used to mark the classes
     *                    and class members.
     */
    public SignatureUsageMarker(UsageMarker usageMarker)
    {
        this.usageMarker = usageMarker;
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        // Only keep the signature if all of its classes are used.
        attributeUsed = true;
        signatureAttribute.referencedClassesAccept(this);

        if (attributeUsed)
        {
            // We got a positive used flag, so the signature is useful.
            usageMarker.markAsUsed(signatureAttribute);

            markConstant(clazz, signatureAttribute.u2attributeNameIndex);
            markConstant(clazz, signatureAttribute.u2signatureIndex);
        }
    }


    // Implementations for ClassVisitor.

    public void visitLibraryClass(LibraryClass libraryClass) {}


    public void visitProgramClass(ProgramClass programClass)
    {
        // Don't keep the signature if one of its classes is not used.
        if (!usageMarker.isUsed(programClass))
        {
            attributeUsed = false;
        }
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant)
    {
        usageMarker.markAsUsed(constant);
    }


    // Small utility methods.

    /**
     * Marks the given constant pool entry of the given class.
     */
    private void markConstant(Clazz clazz, int index)
    {
         clazz.constantPoolEntryAccept(index, this);
    }
}
