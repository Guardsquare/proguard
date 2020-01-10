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
 * This {@link MemberVisitor} delegates its visits to another given
 * {@link MemberVisitor}, but only when the visited member has the proper
 * processing flags.
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
