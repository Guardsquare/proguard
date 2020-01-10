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
package proguard.classfile.attribute;

import proguard.classfile.Clazz;
import proguard.classfile.attribute.visitor.*;

/**
 * This {@link Attribute} represents a bootstrap methods attribute.
 *
 * @author Eric Lafortune
 */
public class BootstrapMethodsAttribute extends Attribute
{
    public int                   u2bootstrapMethodsCount;
    public BootstrapMethodInfo[] bootstrapMethods;


    /**
     * Creates an uninitialized BootstrapMethodsAttribute.
     */
    public BootstrapMethodsAttribute()
    {
    }


    /**
     * Creates an initialized BootstrapMethodsAttribute.
     */
    public BootstrapMethodsAttribute(int                   u2attributeNameIndex,
                                     int                   u2bootstrapMethodsCount,
                                     BootstrapMethodInfo[] bootstrapMethods)
    {
        super(u2attributeNameIndex);

        this.u2bootstrapMethodsCount = u2bootstrapMethodsCount;
        this.bootstrapMethods        = bootstrapMethods;
    }


    // Implementations for Attribute.

    public void accept(Clazz clazz, AttributeVisitor attributeVisitor)
    {
        attributeVisitor.visitBootstrapMethodsAttribute(clazz, this);
    }


    /**
     * Applies the given visitor to all bootstrap method info entries.
     */
    public void bootstrapMethodEntriesAccept(Clazz clazz, BootstrapMethodInfoVisitor bootstrapMethodInfoVisitor)
    {
        for (int index = 0; index < u2bootstrapMethodsCount; index++)
        {
            // We don't need double dispatching here, since there is only one
            // type of BootstrapMethodInfo.
            bootstrapMethodInfoVisitor.visitBootstrapMethodInfo(clazz, bootstrapMethods[index]);
        }
    }


    /**
     * Applies the given visitor to the specified bootstrap method info
     * entry.
     */
    public void bootstrapMethodEntryAccept(Clazz                      clazz,
                                           int                        bootstrapMethodIndex,
                                           BootstrapMethodInfoVisitor bootstrapMethodInfoVisitor)
    {
        // We don't need double dispatching here, since there is only one
        // type of BootstrapMethodInfo.
        bootstrapMethodInfoVisitor.visitBootstrapMethodInfo(clazz, bootstrapMethods[bootstrapMethodIndex]);
    }
}
