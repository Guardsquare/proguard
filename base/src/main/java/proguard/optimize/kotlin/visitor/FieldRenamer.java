/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
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
package proguard.optimize.kotlin.visitor;

import proguard.classfile.Clazz;
import proguard.classfile.Field;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.MemberVisitor;

import java.util.HashMap;
import java.util.Map;

public class FieldRenamer implements MemberVisitor, ConstantVisitor {

    private final String newFieldNamePrefix;
    private int newFieldNameIndex = 0;
    private final boolean useDescriptorBasedNames;
    private final Map<String, Integer> descriptorIndex = new HashMap<>();
    private Field lastVisitedField;
    private Clazz lastVisitedClass;

    public FieldRenamer(String newFieldNamePrefix)
    {
        this.newFieldNamePrefix      = newFieldNamePrefix;
        this.useDescriptorBasedNames = false;
    }

    public FieldRenamer(boolean useDescriptorBasedNames)
    {
        this.newFieldNamePrefix      = "";
        this.useDescriptorBasedNames = useDescriptorBasedNames;
    }

    public void resetIndex()
    {
        this.newFieldNameIndex = 0;
    }

    @Override
    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        this.lastVisitedClass = programClass;
        this.lastVisitedField = programField;
        programClass.constantPoolEntryAccept(programField.u2nameIndex, this);
    }

    @Override
    public void visitAnyConstant(Clazz clazz, Constant constant) {}

    @Override
    public void visitUtf8Constant(Clazz clazz, Utf8Constant utf8Constant)
    {
        String newName = getNextFieldName();
        utf8Constant.setString(newName);
        this.newFieldNameIndex++;
        String descriptor = this.lastVisitedField.getDescriptor(this.lastVisitedClass);
        this.descriptorIndex.put(descriptor, this.descriptorIndex.getOrDefault(descriptor, 0) + 1);

    }

    public String getNextFieldName()
    {
        String newName;
        if (useDescriptorBasedNames)
        {
            // This is non-logical behaviour: the method name suggests a globally correct next name would be
            // returned, but here it depends on the previously visited field, while in practice
            // we don't know whether the next field will have the same descriptor
            String descriptor = this.lastVisitedField.getDescriptor(this.lastVisitedClass);
            newName = descriptor.replace(";", "").replace("/", "").replace("[", "") + this.descriptorIndex.getOrDefault(descriptor, 0);

        }
        else
        {
            newName = this.newFieldNamePrefix + (this.newFieldNameIndex + 1);
        }
        return newName;
    }
}
