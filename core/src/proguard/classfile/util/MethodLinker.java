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
package proguard.classfile.util;

import proguard.classfile.*;
import proguard.classfile.visitor.*;
import proguard.util.Processable;

import java.util.*;

/**
 * This ClassVisitor links all corresponding non-private, non-static,
 * non-initializer methods in the class hierarchies of all visited classes.
 * Visited classes are typically all class files that are not being subclassed.
 * Chains of links that have been created in previous invocations are merged
 * with new chains of links, in order to create a consistent set of chains.
 *
 * @author Eric Lafortune
 */
public class MethodLinker
extends      SimplifiedVisitor
implements   ClassVisitor,
             MemberVisitor
{
    // An object that is reset and reused every time.
    // The map: [method name+' '+descriptor - method info]
    private final Map memberMap = new HashMap();


    // Implementations for ClassVisitor.

    public void visitAnyClass(Clazz clazz)
    {
        // Collect all non-private members in this class hierarchy.
        clazz.hierarchyAccept(true, true, true, false,
            new AllMethodVisitor(
            new MemberAccessFilter(0, AccessConstants.PRIVATE | AccessConstants.STATIC,
            this)));

        // Clean up for the next class hierarchy.
        memberMap.clear();
    }


    // Implementations for MemberVisitor.

    public void visitAnyMember(Clazz clazz, Member member)
    {
        // Get the method's name and descriptor.
        String name       = member.getName(clazz);
        String descriptor = member.getDescriptor(clazz);

        // Special cases: <clinit> and <init> are always kept unchanged.
        // We can ignore them here.
        if (ClassUtil.isInitializer(name))
        {
            return;
        }

        // See if we've already come across a method with the same name and
        // descriptor.
        String key = name + ' ' + descriptor;
        Member otherMember = (Member)memberMap.get(key);

        if (otherMember == null)
        {
            // Get the last method in the chain.
            Member thisLastMember = lastMember(member);

            // Store the new class method in the map.
            memberMap.put(key, thisLastMember);
        }
        else
        {
            // Link both members.
            link(member, otherMember);
        }
    }


    // Small utility methods.

    /**
     * Links the two given methods.
     */
    private static void link(Member member1, Member member2)
    {
        // Get the last methods in the both chains.
        Member lastMember1 = lastMember(member1);
        Member lastMember2 = lastMember(member2);

        // Check if both link chains aren't already ending in the same element.
        if (!lastMember1.equals(lastMember2))
        {
            // Merge the two chains, with the library members last.
            if (lastMember2 instanceof LibraryMember)
            {
                lastMember1.setProcessingInfo(lastMember2);
            }
            else
            {
                lastMember2.setProcessingInfo(lastMember1);
            }
        }
    }


    /**
     * Finds the last method in the linked list of related methods.
     * @param member the given method.
     * @return the last method in the linked list.
     */
    public static Member lastMember(Member member)
    {
        Member lastMember = member;
        while (lastMember.getProcessingInfo() != null &&
               lastMember.getProcessingInfo() instanceof Member)
        {
            lastMember = (Member)lastMember.getProcessingInfo();
        }

        return lastMember;
    }


    /**
     * Finds the last method in the linked list of related methods.
     * @param processable the given method.
     * @return the last method in the linked list.
     */
    public static Processable lastProcessable(Processable processable)
    {
        Processable lastProcessable = processable;
        while (lastProcessable.getProcessingInfo() != null &&
               lastProcessable.getProcessingInfo() instanceof Processable)
        {
            lastProcessable = (Processable)lastProcessable.getProcessingInfo();
        }

        return lastProcessable;
    }
}
