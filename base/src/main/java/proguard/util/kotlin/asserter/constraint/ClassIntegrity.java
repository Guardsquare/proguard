/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
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
package proguard.util.kotlin.asserter.constraint;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.kotlin.*;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.ClassVisitor;
import proguard.util.kotlin.asserter.AssertUtil;

import static proguard.classfile.kotlin.KotlinConstants.KOTLIN_OBJECT_INSTANCE_FIELD_NAME;

/**
 * This class checks the assumption: All functions need a JVM signature
 */
public class ClassIntegrity
extends AbstractKotlinMetadataConstraint
{
    @Override
    public void visitKotlinClassMetadata(Clazz clazz,
                                         KotlinClassKindMetadata kotlinClassKindMetadata)
    {
        AssertUtil util = new AssertUtil("Class " + kotlinClassKindMetadata.className, reporter, programClassPool, libraryClassPool);

        util.reportIfNullReference("referenced class", kotlinClassKindMetadata.referencedClass);
        util.reportIfClassDangling("referenced class", kotlinClassKindMetadata.referencedClass);

        if (kotlinClassKindMetadata.referencedModule != null)
        {
            // Module references should only be on file facades and multi-file parts.
            reporter.report("Unexpected module reference on class kind: " + kotlinClassKindMetadata.referencedModule.name);
        }

        if (kotlinClassKindMetadata.companionObjectName != null)
        {
            util.reportIfNullReference("companion", kotlinClassKindMetadata.referencedCompanionClass);
            util.reportIfClassDangling("companion", kotlinClassKindMetadata.referencedCompanionClass);
            util.reportIfNullReference("companion field", kotlinClassKindMetadata.referencedCompanionField);

            String referencedCompanionClassName = kotlinClassKindMetadata.referencedCompanionClass.getName();

            if (!referencedCompanionClassName.contains("$"))
            {
                reporter.report( "Companion for " + clazz.getName() + " should have $ in the name, found " + referencedCompanionClassName);
            }

            String referencedCompanionFieldName = kotlinClassKindMetadata.referencedCompanionField.getName(clazz);

            if (!referencedCompanionFieldName.equals(referencedCompanionClassName.substring(referencedCompanionClassName.lastIndexOf("$") + 1)))
            {
                reporter.report("Companion field should have same name as companion class: " +
                                kotlinClassKindMetadata.referencedCompanionField.getName(clazz) +
                                " (in " + clazz.getName() + " )" + " != " +
                                ClassUtil.internalSimpleClassName(referencedCompanionClassName));
            }

            kotlinClassKindMetadata.companionAccept(new MyCompanionObjectFlagChecker());
        }

        if (kotlinClassKindMetadata.superTypes.isEmpty())
        {
            reporter.report("Kotlin class " + kotlinClassKindMetadata.className + " has no super types");
        }

        kotlinClassKindMetadata.referencedEnumEntries
            .forEach(enumEntry -> {
                util.reportIfNullReference("enum entries", enumEntry);
                util.reportIfFieldDangling("enum entries", clazz, enumEntry);
            });

        kotlinClassKindMetadata.referencedNestedClasses
            .forEach(nestedClass -> {
                util.reportIfNullReference("nested classes", nestedClass);
                util.reportIfClassDangling("nested classes", nestedClass);
            });

        kotlinClassKindMetadata.referencedSealedSubClasses
            .forEach(sealedSubClass -> {
                util.reportIfNullReference("sealed subclasses", sealedSubClass);
                util.reportIfClassDangling("sealed subclasses", sealedSubClass);
            });

        if (kotlinClassKindMetadata.flags.isObject &&
            kotlinClassKindMetadata.referencedClass != null &&
            kotlinClassKindMetadata.referencedClass.findField(KOTLIN_OBJECT_INSTANCE_FIELD_NAME, null) == null)
        {
            reporter.report("Object class should have a field named " + KOTLIN_OBJECT_INSTANCE_FIELD_NAME);
        }
    }


    // Small helper classes.
    private class MyCompanionObjectFlagChecker
        implements KotlinMetadataVisitor
    {
        // Implementations for KotlinFunctionVisitor.
        @Override
        public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {}

        @Override
        public void visitKotlinClassMetadata(Clazz clazz,
                                             KotlinClassKindMetadata kotlinClassKindMetadata)
        {
            if (!kotlinClassKindMetadata.flags.isCompanionObject)
            {
                reporter.report("Companion class '" + clazz.getName() + "' flag isCompanionObject should be true");
            }

            // The ClassObfuscator assumes that there will be an outer class for the
            // Companion class, otherwise it won't be renamed as an inner class (with a "$").
            // This causes a problem for setting the field name as the class name because the
            // class name will not be an inner class name. See 00001040.
            // We can check that the required inner class attribute exists.
            kotlinClassKindMetadata.referencedClass.accept(new MyInnerClassChecker());
        }
    }

    private class MyInnerClassChecker
        implements ClassVisitor,
                   AttributeVisitor
    {
        private       boolean                 hasInnerClassesAttribute;

        @Override
        public void visitAnyClass(Clazz clazz) {}

        @Override
        public void visitProgramClass(ProgramClass programClass)
        {
            hasInnerClassesAttribute = false;
            programClass.attributeAccept(Attribute.INNER_CLASSES, this);
            if (!hasInnerClassesAttribute)
            {
                reporter.report("Missing inner classes attribute for " + programClass.getName());
            }
        }

        @Override
        public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

        @Override
        public void visitInnerClassesAttribute(Clazz clazz, InnerClassesAttribute innerClassesAttribute)
        {
            hasInnerClassesAttribute = true;
        }
    }
}
