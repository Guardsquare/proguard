/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
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
package proguard.classfile.visitor;

import proguard.classfile.*;
import proguard.util.*;

import java.util.List;

/**
 * This <code>ClassVisitor</code> delegates its visits to another given
 * <code>ClassVisitor</code>, but only when the visited class has a name that
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
