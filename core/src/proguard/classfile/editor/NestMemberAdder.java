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
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.util.ArrayUtil;

/**
 * This {@link ConstantVisitor} and {@link ClassVisitor} adds the class constants or the
 * classes that it visits to the given target nest member attribute.
 */
public class NestMemberAdder
extends      SimplifiedVisitor
implements   ConstantVisitor,
             ClassVisitor

{
    private final ConstantPoolEditor   constantPoolEditor;
    private final NestMembersAttribute targetNestMembersAttribute;


    /**
     * Creates a new NestMemberAdder that will add classes to the
     * given target nest members attribute.
     */
    public NestMemberAdder(ProgramClass         targetClass,
                           NestMembersAttribute targetNestMembersAttribute)
    {
        this.constantPoolEditor         = new ConstantPoolEditor(targetClass);
        this.targetNestMembersAttribute = targetNestMembersAttribute;
    }


    // Implementations for ConstantVisitor.

    public void visitAnyConstant(Clazz clazz, Constant constant) {}

    public void visitClassConstant(Clazz clazz, ClassConstant classConstant)
    {
        targetNestMembersAttribute.u2classes =
            ArrayUtil.add(targetNestMembersAttribute.u2classes,
                          targetNestMembersAttribute.u2classesCount++,
                          constantPoolEditor.addClassConstant(classConstant.getName(clazz),
                                                              classConstant.referencedClass));
    }


    // Implementations for ClassVisitor.

    public void visitAnyClass(Clazz clazz)
    {
        targetNestMembersAttribute.u2classes =
            ArrayUtil.add(targetNestMembersAttribute.u2classes,
                          targetNestMembersAttribute.u2classesCount++,
                          constantPoolEditor.addClassConstant(clazz));
    }
}
