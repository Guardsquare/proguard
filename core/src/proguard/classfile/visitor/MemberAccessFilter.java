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
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This <code>MemberVisitor</code> delegates its visits to another given
 * <code>MemberVisitor</code>, but only when the visited member has the proper
 * access flags.
 * <p>
 * If conflicting access flags (public/private/protected) are specified,
 * having one of them set will be considered sufficient.
 *
 * @see ClassConstants
 *
 * @author Eric Lafortune
 */
public class MemberAccessFilter
extends      SimplifiedVisitor
implements   MemberVisitor
{
    // A mask of conflicting access flags. These are interpreted in a special
    // way if more of them are required at the same time. In that case, one
    // of them being set is sufficient.
    private static final int ACCESS_MASK =
        ClassConstants.ACC_PUBLIC  |
        ClassConstants.ACC_PRIVATE |
        ClassConstants.ACC_PROTECTED;

    private final int           requiredSetAccessFlags;
    private final int           requiredUnsetAccessFlags;
    private final int           requiredOneSetAccessFlags;
    private final MemberVisitor acceptedMemberVisitor;
    private final MemberVisitor rejectedMemberVisitor;


    /**
     * Creates a new MemberAccessFilter.
     * @param requiredSetAccessFlags   the member access flags that should be
     *                                 set.
     * @param requiredUnsetAccessFlags the member access flags that should be
     *                                 unset.
     * @param acceptedMemberVisitor    the <code>MemberVisitor</code> to
     *                                 which visits will be delegated.
     */
    public MemberAccessFilter(int           requiredSetAccessFlags,
                              int           requiredUnsetAccessFlags,
                              MemberVisitor acceptedMemberVisitor)
    {
        this(requiredSetAccessFlags, requiredUnsetAccessFlags, acceptedMemberVisitor, null);
    }


    /**
     * Creates a new MemberAccessFilter.
     * @param requiredSetAccessFlags   the member access flags that should be
     *                                 set.
     * @param requiredUnsetAccessFlags the member access flags that should be
     *                                 unset.
     * @param acceptedMemberVisitor    the <code>MemberVisitor</code> to
     *                                 which visits will be delegated.
     * @param rejectedMemberVisitor    the <code>MemberVisitor</code> to which visits of members that do not have
     *                                 the proper flags will be delegated.
     */
    public MemberAccessFilter(int           requiredSetAccessFlags,
                              int           requiredUnsetAccessFlags,
                              MemberVisitor acceptedMemberVisitor,
                              MemberVisitor rejectedMemberVisitor)
    {
        this.requiredSetAccessFlags    = requiredSetAccessFlags & ~ACCESS_MASK;
        this.requiredUnsetAccessFlags  = requiredUnsetAccessFlags;
        this.requiredOneSetAccessFlags = requiredSetAccessFlags & ACCESS_MASK;
        this.acceptedMemberVisitor     = acceptedMemberVisitor;
        this.rejectedMemberVisitor     = rejectedMemberVisitor;
    }


    // Implementations for MemberVisitor.

    @Override
    public void visitAnyMember(Clazz clazz, Member member)
    {
        MemberVisitor delegateVisitor = getDelegateVisitor(member.getAccessFlags());
        if (delegateVisitor != null)
        {
            member.accept(clazz, delegateVisitor);
        }
    }


    // Small utility methods.

    private MemberVisitor getDelegateVisitor(int accessFlags)
    {
        return accepted(accessFlags) ? acceptedMemberVisitor : rejectedMemberVisitor;
    }


    private boolean accepted(int accessFlags)
    {
        return (requiredSetAccessFlags    & ~accessFlags) == 0 &&
               (requiredUnsetAccessFlags  &  accessFlags) == 0 &&
               (requiredOneSetAccessFlags == 0                 ||
               (requiredOneSetAccessFlags &  accessFlags) != 0);
    }
}
