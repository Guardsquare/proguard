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
