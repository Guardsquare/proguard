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
package proguard.classfile;

import proguard.classfile.visitor.MemberVisitor;
import proguard.util.SimpleProcessable;

/**
 * Base representation of a field or method from a {@link LibraryClass}.
 *
 * @author Eric Lafortune
 */
public abstract class LibraryMember
extends               SimpleProcessable
implements            Member
{
    public int    u2accessFlags;
    public String name;
    public String descriptor;

    /**
     * Creates an uninitialized LibraryMember.
     */
    protected LibraryMember()
    {
    }


    /**
     * Creates an initialized LibraryMember.
     */
    protected LibraryMember(int    u2accessFlags,
                            String name,
                            String descriptor)
    {
        this.u2accessFlags = u2accessFlags;
        this.name          = name;
        this.descriptor    = descriptor;
    }


    /**
     * Accepts the given member info visitor.
     */
    public abstract void accept(LibraryClass  libraryClass,
                                MemberVisitor memberVisitor);


    // Implementations for Member.

    public int getAccessFlags()
    {
        return u2accessFlags;
    }

    public String getName(Clazz clazz)
    {
        return name;
    }

    public String getDescriptor(Clazz clazz)
    {
        return descriptor;
    }

    public void accept(Clazz clazz, MemberVisitor memberVisitor)
    {
        accept((LibraryClass)clazz, memberVisitor);
    }
}
