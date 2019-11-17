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
import proguard.classfile.attribute.visitor.BootstrapMethodInfoVisitor;

/**
 * This BootstrapMethodInfoVisitor adds all bootstrap methods that it visits to
 * the given target bootstrap methods attribute.
 */
public class BootstrapMethodInfoAdder
implements   BootstrapMethodInfoVisitor
{
    private final ConstantAdder                   constantAdder;
    private final BootstrapMethodsAttributeEditor bootstrapMethodsAttributeEditor;

    private int bootstrapMethodIndex;


    /**
     * Creates a new BootstrapMethodInfoAdder that will copy bootstrap methods
     * into the given bootstrap methods attribute.
     */
    public BootstrapMethodInfoAdder(ProgramClass              targetClass,
                                    BootstrapMethodsAttribute targetBootstrapMethodsAttribute)
    {
        this.constantAdder                   = new ConstantAdder(targetClass);
        this.bootstrapMethodsAttributeEditor = new BootstrapMethodsAttributeEditor(targetBootstrapMethodsAttribute);
    }


    /**
     * Returns the index of the most recently added bootstrap method.
     */
    public int getBootstrapMethodIndex()
    {
        return bootstrapMethodIndex;
    }


    // Implementations for BootstrapMethodInfoVisitor.

    public void visitBootstrapMethodInfo(Clazz clazz, BootstrapMethodInfo bootstrapMethodInfo)
    {
        // Copy the method arguments.
        int   methodArgumentCount = bootstrapMethodInfo.u2methodArgumentCount;
        int[] methodArguments     = bootstrapMethodInfo.u2methodArguments;
        int[] newMethodArguments  = new int[methodArgumentCount];

        for (int index = 0; index < methodArgumentCount; index++)
        {
            newMethodArguments[index] =
                constantAdder.addConstant(clazz, methodArguments[index]);
        }

        // Create a new bootstrap method.
        BootstrapMethodInfo newBootstrapMethodInfo =
            new BootstrapMethodInfo(constantAdder.addConstant(clazz, bootstrapMethodInfo.u2methodHandleIndex),
                                    methodArgumentCount,
                                    newMethodArguments);

        // Add it to the target.
        bootstrapMethodIndex =
            bootstrapMethodsAttributeEditor.addBootstrapMethodInfo(newBootstrapMethodInfo);
    }
}