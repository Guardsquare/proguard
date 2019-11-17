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
package proguard.shrink;

import proguard.classfile.*;
import proguard.classfile.visitor.MemberVisitor;

/**
 * This MemberVisitor delegates all its method calls to another MemberVisitor,
 * but only for Member objects that are marked as used.
 *
 * @see ClassUsageMarker
 *
 * @author Eric Lafortune
 */
public class UsedMemberFilter
implements   MemberVisitor
{
    private final SimpleUsageMarker usageMarker;
    private final MemberVisitor     usedMemberFilter;
    private final MemberVisitor     unusedMemberVisitor;


    public UsedMemberFilter(ClassUsageMarker classUsageMarker,
                            MemberVisitor    usedMemberFilter)
    {
        this(classUsageMarker.getUsageMarker(), usedMemberFilter);
    }


    /**
     * Creates a new UsedMemberFilter.
     * @param usageMarker   the usage marker that is used to mark the classes
     *                      and class members.
     * @param usedMemberFilter the member visitor to which the visiting will be
     *                      delegated.
     */
    public UsedMemberFilter(SimpleUsageMarker usageMarker,
                            MemberVisitor     usedMemberFilter)
    {
        this(usageMarker, usedMemberFilter, null);
    }


    /**
     * Creates a new UsedMemberFilter.
     *
     * @param usageMarker         the usage marker that is used to mark the classes
     *                            and class members.
     * @param usedMemberFilter    the member visitor to which the visiting of used members
     *                            will be delegated.
     * @param unusedMemberVisitor the member visitor to which the visiting of unused members
     *                            will bedelegated.
     */
    public UsedMemberFilter(SimpleUsageMarker usageMarker,
                            MemberVisitor     usedMemberFilter,
                            MemberVisitor     unusedMemberVisitor)
    {
        this.usageMarker         = usageMarker;
        this.usedMemberFilter    = usedMemberFilter;
        this.unusedMemberVisitor = unusedMemberVisitor;
    }


    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        MemberVisitor delegateVisitor = delegateVisitor(programField);
        if (delegateVisitor != null)
        {
            delegateVisitor.visitProgramField(programClass, programField);
        }
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        MemberVisitor delegateVisitor = delegateVisitor(programMethod);
        if (delegateVisitor != null)
        {
            delegateVisitor.visitProgramMethod(programClass, programMethod);
        }
    }


    public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField)
    {
        MemberVisitor delegateVisitor = delegateVisitor(libraryField);
        if (delegateVisitor != null)
        {
            delegateVisitor.visitLibraryField(libraryClass, libraryField);
        }
    }


    public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod)
    {
        MemberVisitor delegateVisitor = delegateVisitor(libraryMethod);
        if (delegateVisitor != null)
        {
            delegateVisitor.visitLibraryMethod(libraryClass, libraryMethod);
        }
    }


    // Small utility methods.
    private MemberVisitor delegateVisitor(Member member)
    {
        return usageMarker.isUsed(member) ? usedMemberFilter : unusedMemberVisitor;
    }
}
