/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2021 Guardsquare NV
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
package proguard.optimize;

import proguard.classfile.*;
import proguard.classfile.visitor.*;
import proguard.optimize.info.*;
import proguard.util.MultiValueMap;

/**
 * This classes is a data class that is used to query which methods need to be reconsidered for side effects
 * when side effects for a certain method are derived.
 */
public class ReverseDependencyStore
{
    private final MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByProgramMethodOptimizationInfo;
    private final MultiValueMap<Method, ClassMemberPair>               calledBy;


    /**
     * Constructs a ReverseDependencyStore by its contents.
     *
     * @param calledBy Maps a method to all LocatedMembers that refer to it
     * @param methodsByProgramMethodOptimizationInfo Maps some optimizationInfo to all Methods that use it
     */
    public ReverseDependencyStore(MultiValueMap<Method, ClassMemberPair>               calledBy,
                                  MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByProgramMethodOptimizationInfo)
    {
        this.calledBy                               = calledBy;
        this.methodsByProgramMethodOptimizationInfo = methodsByProgramMethodOptimizationInfo;
    }


    /**
     * This MemberVisitor travels to the set of influenced methods when side effects for a certain method
     * are derived.
     *
     * This is a two step process:
     *    1) get all the methods which share the same methodOptimizationInfo
     *       (i.e. all inherited methods share their methodOptimizationInfo throughout the whole tree)
     *    2) get all the methods which refer to a methods collected in step 1
     *
     */
    public class InfluencedMethodTraveller
    implements   MemberVisitor
    {
        private final MemberVisitor memberVisitor;


        public InfluencedMethodTraveller(MemberVisitor memberVisitor) {this.memberVisitor = memberVisitor;}


        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
        {
            MethodOptimizationInfo methodOptimizationInfo =
                MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);


            if (methodOptimizationInfo instanceof ProgramMethodOptimizationInfo)
            {
                ProgramMethodOptimizationInfo info =
                    (ProgramMethodOptimizationInfo)methodOptimizationInfo;

                for (Method rootMethod : methodsByProgramMethodOptimizationInfo.get(info))
                {
                    if (!calledBy.keySet().contains(rootMethod))
                    {
                        continue;
                    }

                    for (ClassMemberPair locatedMember : calledBy.get(rootMethod))
                    {
                        locatedMember.accept(memberVisitor);
                    }
                }
            }
        }
    }
}
