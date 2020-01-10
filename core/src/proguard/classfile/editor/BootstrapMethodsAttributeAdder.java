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
 * This {@link BootstrapMethodInfoVisitor} adds all bootstrap methods that it visits to
 * the given target class, creating a bootstrap methods attribute if necessary.
 */
public class BootstrapMethodsAttributeAdder
implements   BootstrapMethodInfoVisitor
{
    private final ProgramClass             targetClass;
    private final ConstantPoolEditor       constantPoolEditor;
    private       BootstrapMethodInfoAdder bootstrapMethodInfoAdder;


    /**
     * Creates a new BootstrapMethodsAttributeAdder that will copy bootstrap
     * methods into the given target class/
     */
    public BootstrapMethodsAttributeAdder(ProgramClass targetClass)
    {
        this.targetClass        = targetClass;
        this.constantPoolEditor = new ConstantPoolEditor(targetClass);
    }


    /**
     * Returns the index of the most recently added bootstrap method.
     */
    public int getBootstrapMethodIndex()
    {
        return bootstrapMethodInfoAdder.getBootstrapMethodIndex();
    }


    // Implementations for BootstrapMethodInfoVisitor.

    public void visitBootstrapMethodInfo(Clazz clazz, BootstrapMethodInfo bootstrapMethodInfo)
    {
        // Make sure we have a bootstrap methods attribute adder.
        if (bootstrapMethodInfoAdder == null)
        {
            // Make sure we have a target bootstrap methods attribute.
            AttributesEditor attributesEditor =
                new AttributesEditor(targetClass, false);

            BootstrapMethodsAttribute targetBootstrapMethodsAttribute =
                (BootstrapMethodsAttribute)attributesEditor.findAttribute(Attribute.BOOTSTRAP_METHODS);

            if (targetBootstrapMethodsAttribute == null)
            {
                targetBootstrapMethodsAttribute =
                    new BootstrapMethodsAttribute(constantPoolEditor.addUtf8Constant(Attribute.BOOTSTRAP_METHODS),
                                                  0,
                                                  new BootstrapMethodInfo[0]);

                attributesEditor.addAttribute(targetBootstrapMethodsAttribute);
            }

            // Create a bootstrap method adder for it.
            bootstrapMethodInfoAdder = new BootstrapMethodInfoAdder(targetClass,
                                                                    targetBootstrapMethodsAttribute);
        }

        // Delegate to the bootstrap method adder.
        bootstrapMethodInfoAdder.visitBootstrapMethodInfo(clazz, bootstrapMethodInfo);
    }
}