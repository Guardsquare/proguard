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
package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.ClassVisitor;

import java.util.*;

/**
 * This ClassVisitor sorts the interfaces of the program classes that it visits.
 *
 * @author Eric Lafortune
 */
public class InterfaceSorter
extends      SimplifiedVisitor
implements   ClassVisitor,
             AttributeVisitor
{
    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        int[] interfaces      = programClass.u2interfaces;
        int   interfacesCount = programClass.u2interfacesCount;

        if (interfacesCount > 1)
        {
            // Sort the interfaces.
            Arrays.sort(interfaces, 0, interfacesCount);

            // Remove any duplicate entries.
            int newInterfacesCount     = 0;
            int previousInterfaceIndex = 0;
            for (int index = 0; index < interfacesCount; index++)
            {
                int interfaceIndex = interfaces[index];

                // Isn't this a duplicate of the previous interface?
                if (interfaceIndex != previousInterfaceIndex)
                {
                    interfaces[newInterfacesCount++] = interfaceIndex;

                    // Remember the interface.
                    previousInterfaceIndex = interfaceIndex;
                }
            }

            programClass.u2interfacesCount = newInterfacesCount;

            // Update the signature, if any
            programClass.attributesAccept(this);
        }
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        // Process the generic definitions, superclass, and implemented
        // interfaces.
        String signature = clazz.getString(signatureAttribute.u2signatureIndex);

        // Count the signature types.
        InternalTypeEnumeration internalTypeEnumeration =
            new InternalTypeEnumeration(signature);

        int count           =  0;
        int interfacesCount = -1;
        while (internalTypeEnumeration.hasMoreTypes())
        {
            String internalType = internalTypeEnumeration.nextType();

            count++;

            if (ClassUtil.isInternalClassType(internalType))
            {
                interfacesCount++;
            }
        }

        // Put the signature types in an array.
        internalTypeEnumeration =
            new InternalTypeEnumeration(signature);

        String[] internalTypes = new String[count];

        for (int index = 0; index < count; index++)
        {
            String internalType = internalTypeEnumeration.nextType();

            internalTypes[index] = internalType;
        }

        // Sort the interface types in the array.
        Arrays.sort(internalTypes, count - interfacesCount, count);

        // Recompose the signature types in a string.
        StringBuffer newSignatureBuffer = new StringBuffer();

        for (int index = 0; index < count; index++)
        {
            // Is this not an interface type, or an interface type that isn't
            // a duplicate of the previous interface type?
            if (index < count - interfacesCount ||
                !internalTypes[index].equals(internalTypes[index-1]))
            {
                newSignatureBuffer.append(internalTypes[index]);
            }
        }

        String newSignature = newSignatureBuffer.toString();

        // Did the signature change?
        if (!newSignature.equals(signature))
        {
            // Update the signature.
            ((Utf8Constant)((ProgramClass)clazz).constantPool[signatureAttribute.u2signatureIndex]).setString(newSignatureBuffer.toString());

            // Clear the referenced classes.
            // TODO: Properly update the referenced classes.
            signatureAttribute.referencedClasses = null;
        }
    }
}
