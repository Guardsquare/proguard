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
package proguard.util.kotlin.asserter;

import proguard.classfile.*;
import proguard.classfile.visitor.*;

import java.util.*;

import static proguard.classfile.kotlin.KotlinConstants.dummyClassPool;

public class AssertUtil
{
    private       String    parentElement;
    private final Reporter  reporter;
    private final ClassPool programClassPool;
    private final ClassPool libraryClassPool;

    public AssertUtil(String    parentElement,
                      Reporter  reporter,
                      ClassPool programClassPool,
                      ClassPool libraryClassPool)
    {
        this.parentElement    = parentElement;
        this.reporter         = reporter;
        this.programClassPool = programClassPool;
        this.libraryClassPool = libraryClassPool;
    }

    public void setParentElement(String parentElement)
    {
        this.parentElement = parentElement;
    }

    public void reportIfNull(String checkedElementName, Object ... checkedElement)
    {
        if (Arrays.stream(checkedElement).allMatch(Objects::isNull))
        {
            reporter.report(parentElement + " has no " + checkedElementName + ".");
        }
    }

    public void reportIfNullReference(String checkedElementName, Object checkedElement)
    {
        if (checkedElement == null)
        {
            reporter.report(parentElement + " has no reference for its " + checkedElementName + ".");
        }
    }

    public void reportIfClassDangling(String checkedElementName,
                                      Clazz  clazz)
    {
        if (clazz != null)
        {
            if (!programClassPool.contains(clazz) &&
                !libraryClassPool.contains(clazz) &&
                !dummyClassPool  .contains(clazz))
            {
                reporter.report(parentElement + " has dangling class reference for its " + checkedElementName + ".");
            }
        }
    }

    public void reportIfFieldDangling(String checkedElementName,
                                      Clazz checkedClass,
                                      Field field)
    {
        if (checkedClass != null && field != null)
        {
            ExactMemberMatcher match = new ExactMemberMatcher(field);

            checkedClass.accept(new AllFieldVisitor(match));

            if (!match.memberMatched)
            {
                reporter.report(parentElement + " has a dangling reference for its " + checkedElementName + ".");
            }
        }
    }

    public void reportIfMethodDangling(String checkedElementName,
                                       Clazz checkedClass,
                                       Method method)
    {
        if (checkedClass != null && method != null)
        {
            ExactMemberMatcher match = new ExactMemberMatcher(method);

            checkedClass.accept(new AllMethodVisitor(match));

            if (!match.memberMatched)
            {
                reporter.report(parentElement + " has a dangling reference for its " + checkedElementName + ".");
            }
        }
    }


    // Small helper classes.

    private static class ExactMemberMatcher
    implements           MemberVisitor
    {
        private final Member memberToMatch;

        boolean memberMatched;

        ExactMemberMatcher(Member memberToMatch)
        {
            this.memberToMatch = memberToMatch;
        }


        // Implementations for MemberVisitor.
        @Override
        public void visitAnyMember(Clazz clazz, Member member)
        {
            if (member == memberToMatch)
            {
                memberMatched = true;
            }
        }
    }
}
