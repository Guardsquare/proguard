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
import proguard.classfile.attribute.Attribute;
import proguard.classfile.io.*;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.*;
import proguard.util.*;

import java.io.*;

/**
 * This MemberVisitor copies all class members that it visits to the given
 * target class. Their processing info is set to the class members from which they
 * were copied.
 *
 * @author Eric Lafortune
 */
public class MemberAdder
extends      SimplifiedVisitor
implements   MemberVisitor
{
    //*
    private static final boolean DEBUG = false;
    /*/
    private static       boolean DEBUG = true;
    //*/


    private static final Attribute[] EMPTY_ATTRIBUTES = new Attribute[0];


    private final ProgramClass   targetClass;
    private final StringFunction nameTransformer;
    private final MemberVisitor  extraMemberVisitor;

    private final ClassEditor        classEditor;
    private final ConstantPoolEditor constantPoolEditor;


    /**
     * Creates a new MemberAdder that will copy methods into the given target
     * class.
     * @param targetClass the class to which all visited class members will be
     *                    added.
     */
    public MemberAdder(ProgramClass targetClass)
    {
        this(targetClass, null);
    }


    /**
     * Creates a new MemberAdder that will copy methods into the given target
     * class.
     * @param targetClass        the class to which all visited class members
     *                           will be added.
     * @param extraMemberVisitor an optional member visitor that visits each
     *                           new member right after it has been added. This
     *                           allows changing the processing info, for instance.
     */
    public MemberAdder(ProgramClass      targetClass,
                       MemberVisitor     extraMemberVisitor)
    {
        this(targetClass, null, extraMemberVisitor);
    }

    /**
     * Creates a new MemberAdder that will copy methods into the given target
     * class.
     * @param targetClass        the class to which all visited class members
     *                           will be added.
     * @param nameTransformer    the transformer to be applied to each member's
     *                           name before copying. If null, the original
     *                           name will be used.
     * @param extraMemberVisitor an optional member visitor that visits each
     *                           new member right after it has been added. This
     *                           allows changing the processing info, for instance.
     */
    public MemberAdder(ProgramClass   targetClass,
                       StringFunction nameTransformer,
                       MemberVisitor  extraMemberVisitor)
    {
        this.targetClass        = targetClass;
        this.nameTransformer    = nameTransformer;
        this.extraMemberVisitor = extraMemberVisitor;

        classEditor        = new ClassEditor(targetClass);
        constantPoolEditor = new ConstantPoolEditor(targetClass);
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        String name        = programField.getName(programClass);
        String descriptor  = programField.getDescriptor(programClass);
        int    accessFlags = programField.getAccessFlags();

        if (nameTransformer != null)
        {
            name = nameTransformer.transform(name);
        }

        // TODO: Handle field with the same name and descriptor in the target class.
        // We currently avoid this case, since renaming the identical field
        // still causes confused field references.
        //// Does the target class already have such a field?
        //ProgramField targetField = (ProgramField)targetClass.findField(name, descriptor);
        //if (targetField != null)
        //{
        //    // Is the field private or static?
        //    int targetAccessFlags = targetField.getAccessFlags();
        //    if ((targetAccessFlags &
        //         (AccessConstants.PRIVATE |
        //          AccessConstants.STATIC)) != 0)
        //    {
        //        // Rename the private or static field.
        //        String newName = newUniqueMemberName(name, targetClass.getName());
        //
        //        if (DEBUG)
        //        {
        //            System.out.println("MemberAdder: renaming field ["+targetClass.getName()+"."+name+" "+descriptor+"] to ["+newName+"]");
        //        }
        //
        //        targetField.u2nameIndex = constantPoolEditor.addUtf8Constant(newName);
        //    }
        //    else
        //    {
        //        // Keep the non-private and non-static field, but update its
        //        // contents, in order to keep any references to it valid.
        //        if (DEBUG)
        //        {
        //            System.out.println("MemberAdder: updating field ["+programClass+"."+programField.getName(programClass)+" "+programField.getDescriptor(programClass)+"] into ["+targetClass.getName()+"]");
        //        }
        //
        //        // Combine the access flags.
        //        targetField.u2accessFlags = accessFlags | targetAccessFlags;
        //
        //        // Add and replace any attributes.
        //        programField.attributesAccept(programClass,
        //                                      new AttributeAdder(targetClass,
        //                                                         targetField,
        //                                                         true));
        //
        //        // Don't add a new field.
        //        return;
        //    }
        //}

        if (DEBUG)
        {
            System.out.println("MemberAdder: copying field ["+programClass+"."+programField.getName(programClass)+" "+programField.getDescriptor(programClass)+"] into ["+targetClass.getName()+"]");
        }

        // Create a copy of the field, with the same processing flags and with
        // the processing info linking to this field.
        ProgramField newProgramField =
            new ProgramField(accessFlags,
                             constantPoolEditor.addUtf8Constant(name),
                             constantPoolEditor.addUtf8Constant(descriptor),
                             0,
                             programField.u2attributesCount > 0 ?
                                 new Attribute[programField.u2attributesCount] :
                                 EMPTY_ATTRIBUTES,
                             programField.referencedClass,
                             programField.processingFlags,
                             programField);

        // Copy its attributes.
        programField.attributesAccept(programClass,
                                      new AttributeAdder(targetClass,
                                                         newProgramField,
                                                         false));

        // Add the completed field.
        classEditor.addField(newProgramField);

        // Visit the newly added field, if necessary.
        if (extraMemberVisitor != null)
        {
            extraMemberVisitor.visitProgramField(targetClass, newProgramField);
        }
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        String name        = programMethod.getName(programClass);
        String descriptor  = programMethod.getDescriptor(programClass);
        int    accessFlags = programMethod.getAccessFlags();

        if (nameTransformer != null)
        {
            name = nameTransformer.transform(name);
        }

        // Does the target class already have such a method?
        ProgramMethod targetMethod = (ProgramMethod)targetClass.findMethod(name, descriptor);
        if (targetMethod != null)
        {
            // is this source method abstract?
            if ((accessFlags & AccessConstants.ABSTRACT) != 0)
            {
                // Keep the target method.
                if (DEBUG)
                {
                    System.out.println("MemberAdder: skipping abstract method ["+programClass.getName()+"."+name+descriptor+"] into ["+targetClass.getName()+"]");
                }

                // Don't add a new method.
                return;
            }

            // Is the target method abstract?
            int targetAccessFlags = targetMethod.getAccessFlags();
            if ((targetAccessFlags & AccessConstants.ABSTRACT) != 0)
            {
                // Keep the abstract method, but update its contents, in order
                // to keep any references to it valid.
                if (DEBUG)
                {
                    System.out.println("MemberAdder: updating method ["+programClass.getName()+"."+name+descriptor+"] into ["+targetClass.getName()+"]");
                }

                // Replace the access flags.
                targetMethod.u2accessFlags =
                    accessFlags & ~AccessConstants.FINAL;

                // Add and replace the attributes.
                programMethod.attributesAccept(programClass,
                                               new AttributeAdder(targetClass,
                                                                  targetMethod,
                                                                  true));

                // Don't add a new method.
                return;
            }

            if (DEBUG)
            {
                System.out.println("MemberAdder: renaming method ["+targetClass.getName()+"."+name+descriptor+"]");
            }

            // TODO: Handle non-abstract method with the same name and descriptor in the target class.
            // We currently avoid this case, since renaming the identical method
            // still causes confused method references.
            //// Rename the private (non-abstract) or static method.
            //targetMethod.u2nameIndex =
            //    constantPoolEditor.addUtf8Constant(newUniqueMemberName(name, descriptor));
        }

        if (DEBUG)
        {
            System.out.println("MemberAdder: copying method ["+programClass.getName()+"."+name+descriptor+"] into ["+targetClass.getName()+"]");
        }

        // Create a copy of the method, with the same processing flags and with
        // the processing info linking to this method.
        ProgramMethod newProgramMethod =
            new ProgramMethod(accessFlags & ~AccessConstants.FINAL,
                              constantPoolEditor.addUtf8Constant(name),
                              constantPoolEditor.addUtf8Constant(descriptor),
                              0,
                              programMethod.u2attributesCount > 0 ?
                                  new Attribute[programMethod.u2attributesCount] :
                                  EMPTY_ATTRIBUTES,
                              ArrayUtil.cloneOrNull(programMethod.referencedClasses),
                              programMethod.processingFlags,
                              programMethod);

        // Copy its attributes.
        programMethod.attributesAccept(programClass,
                                       new AttributeAdder(targetClass,
                                                          newProgramMethod,
                                                          false));

        // Add the completed method.
        classEditor.addMethod(newProgramMethod);

        // Visit the newly added method, if necessary.
        if (extraMemberVisitor != null)
        {
            extraMemberVisitor.visitProgramMethod(targetClass, newProgramMethod);
        }
    }


    // Small utility methods.

    /**
     * Returns a unique class member name, based on the given name and descriptor.
     */
    private String newUniqueMemberName(String name, String descriptor)
    {
        return name.equals(ClassConstants.METHOD_NAME_INIT) ?
            ClassConstants.METHOD_NAME_INIT :
            name + TypeConstants.SPECIAL_MEMBER_SEPARATOR + Long.toHexString(Math.abs((descriptor).hashCode()));
    }


    /**
     * This main method illustrates and tests the class, by reading an input
     * class file and copying its class members into a new class that it
     * writes to an output class file.
     */
    public static void main(String[] args)
    {
        String inputClassFileName  = args[0];
        String outputClassFileName = args[1];

        try
        {
            DataInputStream dataInputStream =
                new DataInputStream(
                new FileInputStream(inputClassFileName));

            try
            {
                // Read the input class.
                ProgramClass inputProgramClass =
                    new ProgramClass();

                inputProgramClass.accept(
                    new ProgramClassReader(dataInputStream));

                // Create an empty output class.
                ProgramClass outputProgramClass =
                    new ClassBuilder(
                        VersionConstants.CLASS_VERSION_1_8,
                        AccessConstants.PUBLIC,
                        "com/example/Test",
                        ClassConstants.NAME_JAVA_LANG_OBJECT).getProgramClass();

                // Copy over the class members.
                MemberAdder memberAdder =
                    new MemberAdder(outputProgramClass);

                inputProgramClass.fieldsAccept(memberAdder);
                inputProgramClass.methodsAccept(memberAdder);

                // Print out the output class.
                //outputProgramClass.accept(new ClassPrinter());

                // Write out the output class.
                DataOutputStream dataOutputStream =
                    new DataOutputStream(
                    new FileOutputStream(outputClassFileName));

                try
                {
                    outputProgramClass.accept(
                        new ProgramClassWriter(dataOutputStream));
                }
                finally
                {
                    dataOutputStream.close();
                }
            }
            finally
            {
                dataInputStream.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
