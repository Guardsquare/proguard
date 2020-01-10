/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.constant.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;

import java.util.Arrays;

/**
 * This {@link ClassVisitor} removes specified interfaces from the classes and class
 * signatures that it visits.
 *
 * @author Eric Lafortune
 */
public class InterfaceDeleter
extends      SimplifiedVisitor
implements   ClassVisitor,
             AttributeVisitor
{
    private static final boolean DEBUG = false;


    private final boolean[] delete;
    private final boolean   removeSubclasses;


    /**
     * Creates a new InterfaceDeleter to remove the specified interfaces.
     * @param delete           an array that corresponds to the interfaces of
     *                         a class and that specifies the ones to be
     *                         removed.
     * @param removeSubclasses specifies whether to remove the specified
     *                         interfaces as subclasses of their superclasses.
     */
    public InterfaceDeleter(boolean[] delete, boolean removeSubclasses)
    {
        this.delete           = delete;
        this.removeSubclasses = removeSubclasses;
    }


    // Implementations for ClassVisitor.

    public void visitProgramClass(ProgramClass programClass)
    {
        int[] interfaces      = programClass.u2interfaces;
        int   interfacesCount = programClass.u2interfacesCount;

        if (DEBUG)
        {
            System.out.println("InterfaceDeleter: "+programClass.getName()+" ("+interfacesCount+" interfaces)");
        }

        // Copy the interfaces that aren't deleted.
        int newInterfacesCount = 0;
        for (int index = 0; index < interfacesCount; index++)
        {
            if (DEBUG)
            {
                System.out.println("InterfaceDeleter:   "+(delete[index]?"- ":"+ ")+programClass.getInterfaceName(index));
            }

            int interfaceIndex = interfaces[index];

            // Should we delete the interface from the list?
            if (!delete[index])
            {
                // Just copy the interface.
                interfaces[newInterfacesCount++] = interfaceIndex;
            }
            else if (removeSubclasses)
            {
                // Remove the class from the subclasses of the interface.
                programClass.constantPoolEntryAccept(interfaceIndex,
                    new ReferencedClassVisitor(
                    new SubclassRemover(programClass)));
            }
        }

        // Update the signature.
        if (newInterfacesCount < interfacesCount)
        {
            programClass.u2interfacesCount = newInterfacesCount;

            programClass.attributesAccept(this);
        }
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitSignatureAttribute(Clazz clazz, SignatureAttribute signatureAttribute)
    {
        Clazz[] referencedClasses = signatureAttribute.referencedClasses;
        if (referencedClasses != null)
        {
            // Process the generic definitions, superclass, and implemented
            // interfaces.
            InternalTypeEnumeration internalTypeEnumeration =
                new InternalTypeEnumeration(signatureAttribute.getSignature(clazz));

            // Recompose the signature types in a string buffer.
            StringBuffer newSignatureBuffer = new StringBuffer();

            // Also update the array with referenced classes.
            int referencedClassIndex    = 0;
            int newReferencedClassIndex = 0;

            // Copy the variable type declarations.
            if (internalTypeEnumeration.hasFormalTypeParameters())
            {
                String type = internalTypeEnumeration.formalTypeParameters();

                // Append the type.
                newSignatureBuffer.append(type);

                // Copy any referenced classes.
                int classCount =
                    new DescriptorClassEnumeration(type).classCount();

                for (int counter = 0; counter < classCount; counter++)
                {
                    referencedClasses[newReferencedClassIndex++] =
                        referencedClasses[referencedClassIndex++];
                }

                if (DEBUG)
                {
                    System.out.println("InterfaceDeleter:   type parameters = " + type);
                }
            }

            // Copy the super class type.
            if (internalTypeEnumeration.hasMoreTypes())
            {
                String type = internalTypeEnumeration.nextType();

                // Append the type.
                newSignatureBuffer.append(type);

                // Copy any referenced classes.
                int classCount =
                    new DescriptorClassEnumeration(type).classCount();

                for (int counter = 0; counter < classCount; counter++)
                {
                    referencedClasses[newReferencedClassIndex++] =
                        referencedClasses[referencedClassIndex++];
                }

                if (DEBUG)
                {
                    System.out.println("InterfaceDeleter:   super class type = " + type);
                }
            }

            // Copy the interface types.
            int index = 0;
            while (internalTypeEnumeration.hasMoreTypes())
            {
                String type = internalTypeEnumeration.nextType();

                int classCount =
                    new DescriptorClassEnumeration(type).classCount();

                if (DEBUG)
                {
                    System.out.println("InterfaceDeleter:   interface type " + (delete[index] ? "- " : "+ ") + type + " (" + classCount + " referenced classes)");
                }

                if (!delete[index++])
                {
                    // Append the type.
                    newSignatureBuffer.append(type);

                    // Copy any referenced classes.
                    for (int counter = 0; counter < classCount; counter++)
                    {
                        referencedClasses[newReferencedClassIndex++] =
                            referencedClasses[referencedClassIndex++];
                    }
                }
                else
                {
                    referencedClassIndex += classCount;
                }
            }

            // Update the signature.
            ((Utf8Constant)((ProgramClass)clazz).constantPool[signatureAttribute.u2signatureIndex]).setString(newSignatureBuffer.toString());

            // Clear the remaining referenced classes.
            Arrays.fill(referencedClasses,
                        newReferencedClassIndex,
                        referencedClassIndex,
                        null);
        }
    }
}
