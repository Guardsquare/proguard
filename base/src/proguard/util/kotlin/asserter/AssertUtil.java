/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.util.kotlin.asserter;

import proguard.classfile.*;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.*;

import java.util.*;

public class AssertUtil
{
    private       String      parentElement;
    private final Reporter    reporter;

    public AssertUtil(String parentElement, Reporter reporter)
    {
        this.parentElement  = parentElement;
        this.reporter       = reporter;
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
        extends SimplifiedVisitor
        implements MemberVisitor
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
