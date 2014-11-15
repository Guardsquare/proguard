/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2014 Eric Lafortune (eric@graphics.cornell.edu)
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
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.util.*;
import proguard.classfile.visitor.ClassVisitor;

import java.util.Arrays;

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

            // Update the signature.
            programClass.attributesAccept(this);

            // Remove any duplicate entries.
            boolean[] delete = null;
            for (int index = 1; index < interfacesCount; index++)
            {
                Clazz interfaceClass = programClass.getInterface(index);
                if (interfaces[index] == interfaces[index - 1])
                {
                    // Lazily create the array.
                    if (delete == null)
                    {
                        delete = new boolean[interfacesCount];
                    }

                    delete[index] = true;
                }
            }

            if (delete != null)
            {
                new InterfaceDeleter(delete).visitProgramClass(programClass);
            }
        }
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        // Process the generic definitions, superclass, and implemented
        // interfaces.
        String signature = signatureAttribute.getSignature(clazz);

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
            newSignatureBuffer.append(internalTypes[index]);
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
