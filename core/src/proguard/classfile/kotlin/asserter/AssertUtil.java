/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 GuardSquare NV
 */
package proguard.classfile.kotlin.asserter;

import proguard.classfile.*;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.*;

import java.util.function.Consumer;

public class AssertUtil
{
    private final String         parentElement;
    private final Clazz          clazz;
    private final KotlinMetadata kotlinMetadata;
    private final Reporter       reporter;


    public AssertUtil(String         parentElement,
                      Clazz          clazz,
                      KotlinMetadata kotlinMetadata,
                      Reporter       reporter)
    {
        this.parentElement  = parentElement;
        this.clazz          = clazz;
        this.kotlinMetadata = kotlinMetadata;
        this.reporter       = reporter;
    }


    public Consumer<Object> reportIfNullReference(String checkedElement)
    {
        return metadataElement ->
               reportIfNullReference(metadataElement, checkedElement);
    }


    public Consumer<Field> reportIfFieldDangling(Clazz  checkedClass,
                                                 String checkedElement)
    {
        return field ->
               reportIfFieldDangling(checkedClass, field, checkedElement);
    }


    public void reportIfNullReference(Object checkedElement,
                                      String checkedElementName)
    {
        if (checkedElement == null)
        {
            reporter.report(new MissingReferenceError(parentElement,
                                                      checkedElementName,
                                                      clazz,
                                                      kotlinMetadata));
        }
    }


    public void reportIfFieldDangling(Clazz  checkedClass,
                                      Field  field,
                                      String checkedElementName)
    {
        ExactMemberMatcher match = new ExactMemberMatcher(field);

        checkedClass.accept(new AllFieldVisitor(match));

        if (!match.memberMatched)
        {
            reporter.report(new InvalidReferenceError(parentElement,
                                                      checkedElementName,
                                                      clazz,
                                                      kotlinMetadata));
        }
    }


    public void reportIfMethodDangling(Clazz  checkedClass,
                                       Method method,
                                       String checkedElementName)
    {
        ExactMemberMatcher match = new ExactMemberMatcher(method);

        checkedClass.accept(new AllMethodVisitor(match));

        if (!match.memberMatched)
        {
            reporter.report(new InvalidReferenceError(parentElement,
                                                      checkedElementName,
                                                      clazz,
                                                      kotlinMetadata));
        }
    }


    // Small helper classes.

    private static class ExactMemberMatcher
    extends    SimplifiedVisitor
    implements MemberVisitor
    {
        private final Member  memberToMatch;

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
