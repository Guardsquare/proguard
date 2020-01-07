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
import proguard.classfile.visitor.ClassVisitor;
import proguard.util.SimpleProcessable;

/**
 * Representation of an Local Variable table entry.
 *
 * @author Eric Lafortune
 */
public class LocalVariableTypeInfo
extends      SimpleProcessable
implements   Comparable
{
    public int u2startPC;
    public int u2length;
    public int u2nameIndex;
    public int u2signatureIndex;
    public int u2index;

    /**
     * An extra field pointing to the Clazz objects referenced in the
     * type string. This field is typically filled out by the <code>{@link
     * proguard.classfile.util.ClassReferenceInitializer
     * ClassReferenceInitializer}</code>.
     * References to primitive types are ignored.
     */
    public Clazz[] referencedClasses;


    /**
     * Creates an uninitialized LocalVariableTypeInfo.
     */
    public LocalVariableTypeInfo()
    {
    }


    /**
     * Creates an initialized LocalVariableTypeInfo.
     */
    public LocalVariableTypeInfo(int u2startPC,
                                 int u2length,
                                 int u2nameIndex,
                                 int u2signatureIndex,
                                 int u2index)
    {
        this.u2startPC        = u2startPC;
        this.u2length         = u2length;
        this.u2nameIndex      = u2nameIndex;
        this.u2signatureIndex = u2signatureIndex;
        this.u2index          = u2index;
    }


    /**
     * Returns the name.
     */
    public String getName(Clazz clazz)
    {
        return clazz.getString(u2nameIndex);
    }


    /**
     * Returns the signature.
     */
    public String getSignature(Clazz clazz)
    {
        return clazz.getString(u2signatureIndex);
    }


    /**
     * Applies the given visitor to all referenced classes.
     */
    public void referencedClassesAccept(ClassVisitor classVisitor)
    {
        if (referencedClasses != null)
        {
            for (int index = 0; index < referencedClasses.length; index++)
            {
                Clazz referencedClass = referencedClasses[index];
                if (referencedClass != null)
                {
                    referencedClass.accept(classVisitor);
                }
            }
        }
    }


    // Implementations for Comparable.

    public int compareTo(Object object)
    {
        LocalVariableTypeInfo other = (LocalVariableTypeInfo)object;

        return
            this.u2startPC         < other.u2startPC         ? -1 : this.u2startPC         > other.u2startPC         ? 1 :
            this.u2index           < other.u2index           ? -1 : this.u2index           > other.u2index           ? 1 :
            this.u2length          < other.u2length          ? -1 : this.u2length          > other.u2length          ? 1 :
            this.u2signatureIndex  < other.u2signatureIndex  ? -1 : this.u2signatureIndex  > other.u2signatureIndex  ? 1 :
            this.u2nameIndex       < other.u2nameIndex       ? -1 : this.u2nameIndex       > other.u2nameIndex       ? 1 :
                                                                                                                       0;
    }
}
