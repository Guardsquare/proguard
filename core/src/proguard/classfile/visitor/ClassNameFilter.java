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
package proguard.classfile.visitor;

import proguard.classfile.*;
import proguard.util.*;

import java.util.List;

/**
 * This {@link ClassVisitor} delegates its visits to another given
 * {@link ClassVisitor}, but only when the visited class has a name that
 * matches a given regular expression.
 *
 * @author Eric Lafortune
 */
public class ClassNameFilter implements ClassVisitor
{
    private final StringMatcher regularExpressionMatcher;
    private final ClassVisitor  acceptedClassVisitor;
    private final ClassVisitor  rejectedClassVisitor;


    /**
     * Creates a new ClassNameFilter.
     * @param regularExpression    the regular expression against which class
     *                             names will be matched.
     * @param acceptedClassVisitor the <code>ClassVisitor</code> to which
     *                             accepted visits will be delegated.
     */
    public ClassNameFilter(String       regularExpression,
                           ClassVisitor acceptedClassVisitor)
    {
        this(regularExpression,
             (WildcardManager)null,
             acceptedClassVisitor);
    }


    /**
     * Creates a new ClassNameFilter.
     * @param regularExpression    the regular expression against which class
     *                             names will be matched.
     * @param wildcardManager      an optional scope for StringMatcher instances
     *                             that match wildcards.
     * @param acceptedClassVisitor the <code>ClassVisitor</code> to which
     *                             accepted visits will be delegated.
     */
    public ClassNameFilter(String          regularExpression,
                           WildcardManager wildcardManager,
                           ClassVisitor    acceptedClassVisitor)
    {
        this(regularExpression,
             wildcardManager,
             acceptedClassVisitor,
             null);
    }


    /**
     * Creates a new ClassNameFilter.
     * @param regularExpression    the regular expression against which class
     *                             names will be matched.
     * @param acceptedClassVisitor the <code>ClassVisitor</code> to which
     *                             accepted visits will be delegated.
     */
    public ClassNameFilter(List         regularExpression,
                           ClassVisitor acceptedClassVisitor)
    {
        this(regularExpression,
             (WildcardManager)null,
             acceptedClassVisitor);
    }


    /**
     * Creates a new ClassNameFilter.
     * @param regularExpression    the regular expression against which class
     *                             names will be matched.
     * @param wildcardManager      an optional scope for StringMatcher instances
     *                             that match wildcards.
     * @param acceptedClassVisitor the <code>ClassVisitor</code> to which
     *                             accepted visits will be delegated.
     */
    public ClassNameFilter(List            regularExpression,
                           WildcardManager wildcardManager,
                           ClassVisitor    acceptedClassVisitor)
    {
        this(regularExpression,
             wildcardManager,
             acceptedClassVisitor,
             null);
    }


    /**
     * Creates a new ClassNameFilter.
     * @param regularExpressionMatcher the string matcher against which
     *                                 class names will be matched.
     * @param acceptedClassVisitor     the <code>ClassVisitor</code> to which
     *                                 accepted visits will be delegated.
     */
    public ClassNameFilter(StringMatcher regularExpressionMatcher,
                           ClassVisitor  acceptedClassVisitor)
    {
        this(regularExpressionMatcher,
             acceptedClassVisitor,
             null);
    }


    /**
     * Creates a new ClassNameFilter.
     * @param regularExpression    the regular expression against which class
     *                             names will be matched.
     * @param acceptedClassVisitor the <code>ClassVisitor</code> to which
     *                             accepted visits will be delegated.
     * @param rejectedClassVisitor the <code>ClassVisitor</code> to which
     *                             rejected visits will be delegated.
     */
    public ClassNameFilter(String       regularExpression,
                           ClassVisitor acceptedClassVisitor,
                           ClassVisitor rejectedClassVisitor)
    {
        this(regularExpression,
             null,
             acceptedClassVisitor,
             rejectedClassVisitor);
    }


    /**
     * Creates a new ClassNameFilter.
     * @param regularExpression    the regular expression against which class
     *                             names will be matched.
     * @param wildcardManager      an optional scope for StringMatcher instances
     *                             that match wildcards.
     * @param acceptedClassVisitor the <code>ClassVisitor</code> to which
     *                             accepted visits will be delegated.
     * @param rejectedClassVisitor the <code>ClassVisitor</code> to which
     *                             rejected visits will be delegated.
     */
    public ClassNameFilter(String          regularExpression,
                           WildcardManager wildcardManager,
                           ClassVisitor    acceptedClassVisitor,
                           ClassVisitor    rejectedClassVisitor)
    {
        this(new ListParser(new ClassNameParser(wildcardManager)).parse(regularExpression),
             acceptedClassVisitor,
             rejectedClassVisitor);
    }


    /**
     * Creates a new ClassNameFilter.
     * @param regularExpression    the regular expression against which class
     *                             names will be matched.
     * @param acceptedClassVisitor the <code>ClassVisitor</code> to which
     *                             accepted visits will be delegated.
     * @param rejectedClassVisitor the <code>ClassVisitor</code> to which
     *                             rejected visits will be delegated.
     */
    public ClassNameFilter(List         regularExpression,
                           ClassVisitor acceptedClassVisitor,
                           ClassVisitor rejectedClassVisitor)
    {
        this(regularExpression,
             null,
             acceptedClassVisitor,
             rejectedClassVisitor);
    }


    /**
     * Creates a new ClassNameFilter.
     * @param regularExpression    the regular expression against which class
     *                             names will be matched.
     * @param wildcardManager      an optional scope for StringMatcher instances
     *                             that match wildcards.
     * @param acceptedClassVisitor the <code>ClassVisitor</code> to which
     *                             accepted visits will be delegated.
     * @param rejectedClassVisitor the <code>ClassVisitor</code> to which
     *                             rejected visits will be delegated.
     */
    public ClassNameFilter(List            regularExpression,
                           WildcardManager wildcardManager,
                           ClassVisitor    acceptedClassVisitor,
                           ClassVisitor    rejectedClassVisitor)
    {
        this(new ListParser(new ClassNameParser(wildcardManager)).parse(regularExpression),
             acceptedClassVisitor,
             rejectedClassVisitor);
    }


    /**
     * Creates a new ClassNameFilter.
     * @param regularExpressionMatcher the string matcher against which
     *                                 class names will be matched.
     * @param acceptedClassVisitor     the <code>ClassVisitor</code> to which
     *                                 accepted visits will be delegated.
     * @param rejectedClassVisitor     the <code>ClassVisitor</code> to which
     *                                 rejected visits will be delegated.
     */
    public ClassNameFilter(StringMatcher regularExpressionMatcher,
                           ClassVisitor  acceptedClassVisitor,
                           ClassVisitor  rejectedClassVisitor)
    {
        this.regularExpressionMatcher = regularExpressionMatcher;
        this.acceptedClassVisitor     = acceptedClassVisitor;
        this.rejectedClassVisitor     = rejectedClassVisitor;
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        ClassVisitor delegate = getDelegateVisitor(programClass);
        if (delegate != null)
        {
            delegate.visitProgramClass(programClass);
        }
    }


    @Override
    public void visitLibraryClass(LibraryClass libraryClass)
    {
        ClassVisitor delegate = getDelegateVisitor(libraryClass);
        if (delegate != null)
        {
            delegate.visitLibraryClass(libraryClass);
        }
    }


    // Small utility methods.

    private ClassVisitor getDelegateVisitor(Clazz clazz)
    {
        return regularExpressionMatcher.matches(clazz.getName()) ?
            acceptedClassVisitor :
            rejectedClassVisitor;
    }
}
