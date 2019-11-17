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
 * processing flags.
 * <p>
 * @see proguard.util.ProcessingFlags
 *
 * @author Johan Leys
 */
public class MemberProcessingFlagFilter
extends      SimplifiedVisitor
implements   MemberVisitor
{
    private final int           requiredSetProcessingFlags;
    private final int           requiredUnsetProcessingFlags;
    private final MemberVisitor acceptedMemberVisitor;
    private final MemberVisitor rejectedMemberVisitor;


    /**
     * Creates a new MemberProcessingFlagFilter.
     *
     * @param requiredSetProcessingFlags   the member processing flags that should be set.
     * @param requiredUnsetProcessingFlags the member processing flags that should be unset.
     * @param acceptedMemberVisitor        the <code>MemberVisitor</code> to which visits of members with the proper
     *                                     processing flags will be delegated.
     */
    public MemberProcessingFlagFilter(int           requiredSetProcessingFlags,
                                      int           requiredUnsetProcessingFlags,
                                      MemberVisitor acceptedMemberVisitor)
    {
        this(requiredSetProcessingFlags, requiredUnsetProcessingFlags, acceptedMemberVisitor, null);
    }


    /**
     * Creates a new MemberProcessingFlagFilter.
     *
     * @param requiredSetProcessingFlags   the member processing flags that should be set.
     * @param requiredUnsetProcessingFlags the member processing flags that should be unset.
     * @param acceptedMemberVisitor        the <code>MemberVisitor</code> to which visits of members with the proper
     *                                     processing flags will be delegated.
     * @param rejectedMemberVisitor        the <code>MemberVisitor</code> to which all other visits will be delegated.
     */
    public MemberProcessingFlagFilter(int           requiredSetProcessingFlags,
                                      int           requiredUnsetProcessingFlags,
                                      MemberVisitor acceptedMemberVisitor,
                                      MemberVisitor rejectedMemberVisitor)
    {
        this.requiredSetProcessingFlags   = requiredSetProcessingFlags;
        this.requiredUnsetProcessingFlags = requiredUnsetProcessingFlags;
        this.acceptedMemberVisitor        = acceptedMemberVisitor;
        this.rejectedMemberVisitor        = rejectedMemberVisitor;
    }


    // Implementations for MemberVisitor.

    @Override
    public void visitAnyMember(Clazz clazz, Member member)
    {
        MemberVisitor delegate = getDelegate(member.getProcessingFlags());
        if (delegate != null)
        {
            member.accept(clazz, delegate);
        }
    }


    // Small utility methods.

    private MemberVisitor getDelegate(int processingFlags)
    {
        return accepted(processingFlags) ? acceptedMemberVisitor : rejectedMemberVisitor;
    }


    private boolean accepted(int processingFlags)
    {
        return (requiredSetProcessingFlags   & ~processingFlags) == 0 &&
               (requiredUnsetProcessingFlags &  processingFlags) == 0;
    }
}
