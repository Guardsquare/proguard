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
package proguard.obfuscate.kotlin;

import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMember;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMember;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;
import proguard.classfile.visitor.AllFieldVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static proguard.classfile.util.ClassUtil.internalSimpleClassName;
import static proguard.obfuscate.ClassObfuscator.newClassName;
import static proguard.obfuscate.ClassObfuscator.setNewClassName;
import static proguard.obfuscate.MemberObfuscator.newMemberName;
import static proguard.obfuscate.MemberObfuscator.setFixedNewMemberName;
import static proguard.util.ProcessingFlags.DONT_OBFUSCATE;

public class KotlinCompanionEqualizer
implements   KotlinMetadataVisitor {

    @Override
    public void visitAnyKotlinMetadata(Clazz clazz, KotlinMetadata kotlinMetadata) {
    }

    @Override
    public void visitKotlinClassMetadata(Clazz clazz, KotlinClassKindMetadata kotlinClassKindMetadata) {
        if (kotlinClassKindMetadata.companionObjectName != null) {
            String newCompanionClassName = newClassName(kotlinClassKindMetadata.referencedCompanionClass);

            if (newCompanionClassName == null) return;

            // The name should be an inner class, but if for some reason it isn't
            // then don't try to rename it as it could lead to problems.
            // The Kotlin asserter will check the field name, so will throw the metadata away if
            // it wasn't named correctly.

            if (newCompanionClassName.contains("$")) {
                if ((kotlinClassKindMetadata.referencedCompanionField.getProcessingFlags() & DONT_OBFUSCATE) != 0) {
                    // The field is to be kept, so the class name should be kept as well.
                    setNewClassName(kotlinClassKindMetadata.referencedCompanionClass,
                            newClassName(kotlinClassKindMetadata.referencedClass) + "$" + kotlinClassKindMetadata.companionObjectName);
                } else {
                    final String newCompanionSimpleClassName = internalSimpleClassName(newCompanionClassName);

                    // Check whether there is already a field with the same fixed new name in the hierarchy.
                    ConflictingFieldChecker conflictingFieldChecker =
                            new ConflictingFieldChecker(newCompanionSimpleClassName);
                    clazz.hierarchyAccept(
                            false, true, true, false, new AllFieldVisitor(conflictingFieldChecker));

                    // There is a field in the hierarchy with the same fixed name. Rename the companion
                    // class and associated field to avoid the class being renamed inconsistently during
                    // conflict resolution and causing runtime crashes.
                    if (conflictingFieldChecker.isConflicting) {
                        newCompanionClassName = newCompanionClassName + "_";
                        setNewClassName(
                                kotlinClassKindMetadata.referencedCompanionClass, newCompanionClassName);
                    }
                    // Set a fixed member name to make sure it gets priority when resolving naming conflicts and collecting
                    // already used member names.
                    setFixedNewMemberName(kotlinClassKindMetadata.referencedCompanionField,
                            internalSimpleClassName(newCompanionClassName));
                }
            }
        }
    }
    private static class ConflictingFieldChecker implements MemberVisitor {

        public boolean isConflicting = false;
        private String newCompanionSimpleClassName;

        public ConflictingFieldChecker(String newCompanionSimpleClassName) {
            this.newCompanionSimpleClassName = newCompanionSimpleClassName;
        }

        @Override
        public void visitLibraryMember(LibraryClass libraryClass, LibraryMember libraryMember) {}

        @Override
        public void visitProgramMember(ProgramClass programClass, ProgramMember programMember) {
            isConflicting |= newCompanionSimpleClassName.equals(newMemberName(programMember));
        }
    }
}
