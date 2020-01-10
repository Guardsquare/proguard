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
package proguard.classfile.util;

import proguard.classfile.*;
import proguard.classfile.visitor.*;

/**
 * This {@link MemberVisitor} lets a given parameter visitor visit all the parameters
 * of the methods that it visits. The parameters optionally include the
 * 'this' parameters of non-static methods, but never the return value.
 *
 * @author Eric Lafortune
 */
public class AllParameterVisitor
implements   MemberVisitor
{
    private final boolean          includeThisParameter;
    private final ParameterVisitor parameterVisitor;


    /**
     * Creates a new AllParameterVisitor for the given parameter
     * visitor.
     *
     * @param includeThisParameter specifies whether to visit the 'this'
     *                             parameters.
     * @param parameterVisitor     the visitor for the parameters of the
     *                             visited methods.
     */
    public AllParameterVisitor(boolean          includeThisParameter,
                               ParameterVisitor parameterVisitor)
    {
        this.includeThisParameter = includeThisParameter;
        this.parameterVisitor     = parameterVisitor;
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        visitFieldType(programClass,
                       programField,
                       programField.referencedClass);
    }


    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField)
    {
        visitFieldType(libraryClass,
                       libraryField,
                       libraryField.referencedClass);
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        visitParameters(programClass,
                        programMethod,
                        programMethod.referencedClasses);
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        visitParameters(libraryClass,
                        libraryMethod,
                        libraryMethod.referencedClasses);
    }


    // Small utility methods.

    /**
     * Lets the parameter visitor visit the type of the given field.
     */
    private void visitFieldType(Clazz clazz,
                                Field field,
                                Clazz referencedClass)
    {
        String descriptor = field.getDescriptor(clazz);
        parameterVisitor.visitParameter(clazz,
                                        field,
                                        0,
                                        1,
                                        0,
                                        ClassUtil.internalTypeSize(descriptor),
                                        descriptor,
                                        referencedClass);
    }


    /**
     * Lets the parameter visitor visit the parameters of the given method.
     */
    private void visitParameters(Clazz   clazz,
                                 Method  method,
                                 Clazz[] referencedClasses)
    {
        String descriptor = method.getDescriptor(clazz);

        // Count the number of parameters and their total size.
        int parameterCount  = 0;
        int parameterSize   = 0;

        int index = 1;

        loop: while (true)
        {
            char c = descriptor.charAt(index++);
            switch (c)
            {
                case TypeConstants.LONG:
                case TypeConstants.DOUBLE:
                {
                    // Long and double primitive types.
                    parameterSize++;
                    break;
                }
                default:
                {
                    // All other primitive types.
                    break;
                }
                case TypeConstants.CLASS_START:
                {
                    // Class types.
                    // Skip the class name.
                    index = descriptor.indexOf(TypeConstants.CLASS_END, index) + 1;
                    break;
                }
                case TypeConstants.ARRAY:
                {
                    // Array types.
                    // Skip all array characters.
                    while ((c = descriptor.charAt(index++)) == TypeConstants.ARRAY) {}

                    if (c == TypeConstants.CLASS_START)
                    {
                        // Skip the class type.
                        index = descriptor.indexOf(TypeConstants.CLASS_END, index) + 1;
                    }
                    break;
                }
                case TypeConstants.METHOD_ARGUMENTS_CLOSE:
                {
                    break loop;
                }
            }

            parameterCount++;
            parameterSize++;
        }

        // Visit the parameters.
        int parameterIndex  = 0;
        int parameterOffset = 0;
        int referenceClassIndex = 0;

        // Visit the 'this' parameter if applicable.
        if (includeThisParameter &&
            (method.getAccessFlags() & AccessConstants.STATIC) == 0)
        {
            parameterVisitor.visitParameter(clazz,
                                            method,
                                            parameterIndex++,
                                            ++parameterCount,
                                            parameterOffset++,
                                            ++parameterSize,
                                            ClassUtil.internalTypeFromClassName(clazz.getName()),
                                            clazz);
        }

        index = 1;

        while (true)
        {
            int    newIndex          = index + 1;
            int    thisParameterSize = 1;
            Clazz  referencedClass   = null;

            char c = descriptor.charAt(index);
            switch (c)
            {
                case TypeConstants.LONG:
                case TypeConstants.DOUBLE:
                {
                    // Long and double primitive types.
                    thisParameterSize = 2;
                    break;
                }
                default:
                {
                    // All other primitive types.
                    break;
                }
                case TypeConstants.CLASS_START:
                {
                    // Class types.
                    // Skip the class name.
                    newIndex = descriptor.indexOf(TypeConstants.CLASS_END, newIndex) + 1;
                    referencedClass = referencedClasses == null ? null :
                        referencedClasses[referenceClassIndex++];
                    break;
                }
                case TypeConstants.ARRAY:
                {
                    // Array types.
                    // Skip all array characters.
                    while ((c = descriptor.charAt(newIndex++)) == TypeConstants.ARRAY) {}

                    if (c == TypeConstants.CLASS_START)
                    {
                        // Skip the class type.
                        newIndex = descriptor.indexOf(TypeConstants.CLASS_END, newIndex) + 1;
                        referencedClass = referencedClasses == null ? null :
                            referencedClasses[referenceClassIndex++];
                    }
                    break;
                }
                case TypeConstants.METHOD_ARGUMENTS_CLOSE:
                {
                    // End of the method parameters.
                    return;
                }
            }

            parameterVisitor.visitParameter(clazz,
                                            method,
                                            parameterIndex++,
                                            parameterCount,
                                            parameterOffset,
                                            parameterSize,
                                            descriptor.substring(index, newIndex),
                                            referencedClass);

            // Continue with the next parameter.
            index = newIndex;
            parameterOffset += thisParameterSize;
        }
    }
}
