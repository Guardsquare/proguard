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

import java.util.Set;

/**
 * This MemberVisitor collects dot-separated classname.membername.descriptor
 * strings of the class members that it visits.
 *
 * @author Eric Lafortune
 */
public class MemberCollector
extends      SimplifiedVisitor
implements   MemberVisitor
{
    private final boolean includeClassName;
    private final boolean includeMemberName;
    private final boolean includeMemberDescriptor;

    private final Set set;


    /**
     * Creates a new MemberCollector.
     * @param includeClassName        specifies whether to include the class
     *                                name in each collected strings.
     * @param includeMemberName       specifies whether to include the member
     *                                name in each collected strings.
     * @param includeMemberDescriptor specifies whether to include the member
     *                                descriptor in each collected strings.
     * @param set                     the Set in which all strings will be
     *                                collected.
     */
    public MemberCollector(boolean includeClassName,
                           boolean includeMemberName,
                           boolean includeMemberDescriptor,
                           Set     set)
    {
        this.includeClassName        = includeClassName;
        this.includeMemberName       = includeMemberName;
        this.includeMemberDescriptor = includeMemberDescriptor;

        this.set = set;
    }


    // Implementations for MemberVisitor.

    public void visitAnyMember(Clazz clazz, Member member)
    {
        StringBuffer buffer = new StringBuffer();

        if (includeClassName)
        {
            buffer.append(clazz.getName()).append('.');
        }

        if (includeMemberName)
        {
            buffer.append(member.getName(clazz)).append('.');
        }

        if (includeMemberDescriptor)
        {
            buffer.append(member.getDescriptor(clazz));
        }

        set.add(buffer.toString());
    }
}