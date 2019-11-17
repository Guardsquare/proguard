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
package proguard.classfile.attribute.annotation.visitor;

import proguard.classfile.*;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.util.SimplifiedVisitor;
import proguard.classfile.visitor.MemberVisitor;


/**
 * This AnnotationVisitor delegates all visits to a given MemberVisitor.
 * The latter visits the class member of each visited class member annotation
 * or method parameter annotation, although never twice in a row.
 *
 * @author Eric Lafortune
 */
public class AnnotationToAnnotatedMemberVisitor
extends      SimplifiedVisitor
implements   AnnotationVisitor
{
    private final MemberVisitor memberVisitor;

    private Member lastVisitedMember;


    public AnnotationToAnnotatedMemberVisitor(MemberVisitor memberVisitor)
    {
        this.memberVisitor = memberVisitor;
    }


    // Implementations for AnnotationVisitor.

    public void visitAnnotation(Clazz clazz, Member member, Annotation annotation)
    {
        if (!member.equals(lastVisitedMember))
        {
            member.accept(clazz, memberVisitor);

            lastVisitedMember = member;
        }
    }
}
