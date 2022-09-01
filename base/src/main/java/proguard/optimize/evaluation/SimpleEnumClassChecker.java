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
package proguard.optimize.evaluation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.AccessConstants;
import proguard.classfile.Clazz;
import proguard.classfile.Member;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberAccessFilter;
import proguard.classfile.visitor.MemberToClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.optimize.OptimizationInfoClassFilter;
import proguard.optimize.info.SimpleEnumMarker;

import java.util.Collections;
import java.util.Set;

import static proguard.classfile.ClassConstants.METHOD_NAME_INIT;
import static proguard.classfile.ClassConstants.METHOD_TYPE_INIT_ENUM;

/**
 * This ClassVisitor marks all program classes that it visits as simple enums,
 * if their methods qualify.
 *
 * @author Eric Lafortune
 */
public class SimpleEnumClassChecker
implements   ClassVisitor
{
    private static final Logger logger = LogManager.getLogger(SimpleEnumClassChecker.class);


    private final ClassVisitor  simpleEnumMarker      = new OptimizationInfoClassFilter(
                                                        new SimpleEnumMarker(true));
    private final MemberVisitor instanceMemberChecker = new MemberAccessFilter(0,
                                                                               AccessConstants.STATIC,
                                                        new MemberToClassVisitor(
                                                        new SimpleEnumMarker(false)));


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) { }


    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        // Does the class have the simple enum constructor?
        Method simpleEnumConstructor = programClass.findMethod(METHOD_NAME_INIT,
                                                               METHOD_TYPE_INIT_ENUM);
        if (simpleEnumConstructor == null)
        {
            return;
        }

        logger.debug("SimpleEnumClassChecker: [{}] is a candidate simple enum, without extra fields", programClass.getName());

        // Mark it.
        simpleEnumMarker.visitProgramClass(programClass);

        // Unmark it again if it has any instance fields
        // or methods.
        programClass.fieldsAccept(instanceMemberChecker);
        programClass.methodsAccept(
                new MemberCollectionFilter(
                        // The constructor should not be taken into account here
                        // because it is handled by the SimpleEnumClassSimplifier.
                        Collections.singleton(simpleEnumConstructor),
                        null,
                        instanceMemberChecker)
        );
    }

    // TODO: replace usage with version in ProGuardCORE when available.
    private static class MemberCollectionFilter
    implements   MemberVisitor
    {
        private final Set<Member> members;

        private final MemberVisitor acceptedVisitor;
        private final MemberVisitor rejectedVisitor;

        /**
         * Creates a new MemberCollectionFilter.
         *
         * @param members         the members collection to be searched in.
         * @param acceptedVisitor this visitor will be called for members that are
         *                        present in the member collection.
         */
        public MemberCollectionFilter(Set<Member>   members,
                                      MemberVisitor acceptedVisitor)
        {
            this(members, acceptedVisitor, null);
        }


        /**
         * Creates a new MemberCollectionFilter.
         *
         * @param members         the member collection to be searched in.
         * @param acceptedVisitor this visitor will be called for members that are
         *                        present in the member collection.
         * @param rejectedVisitor this visitor will be called otherwise.
         */
        public MemberCollectionFilter(Set<Member>   members,
                                      MemberVisitor acceptedVisitor,
                                      MemberVisitor rejectedVisitor)
        {
            this.members       = members;
            this.acceptedVisitor = acceptedVisitor;
            this.rejectedVisitor = rejectedVisitor;
        }


        // Implementations for MemberVisitor.

        public void visitAnyMember(Clazz clazz, Member member)
        {
            MemberVisitor delegateVisitor = delegateVisitor(member);
            if (delegateVisitor != null)
            {
                member.accept(clazz, delegateVisitor);
            }
        }


        // Small utility methods.

        private MemberVisitor delegateVisitor(Member member)
        {
            return (members.contains(member)) ?
                acceptedVisitor : rejectedVisitor;
        }
    }
}
