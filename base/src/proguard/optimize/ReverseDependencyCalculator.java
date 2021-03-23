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
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.instruction.visitor.*;
import proguard.classfile.visitor.*;
import proguard.optimize.info.*;
import proguard.util.MultiValueMap;

/**
 * This class serves to construct a ReverseDependencyStore through computing its depending objects;
 *  - isCalledBy:       a mapping which tells which classes refer to which other classes
 *  - classEqualizers:  a mapping which tells which classes have some method with a certain ProgramMethodOptimizationInfo
 *
 *  Note that we never consider library classes as their optimizationInfo is not mutable.
 */
public class ReverseDependencyCalculator
{
    private static final boolean DETAILS = System.getProperty("rdc") != null;

    private final ClassPool classPool;


    public ReverseDependencyCalculator(ClassPool classPool)
    {
        this.classPool = classPool;
    }


    /**
     * This function constructs the reverseDependencyStore based on the object
     * @return a new ReverseDependencyStore based on the class pool given to the Calculator at construction time
     */
    public ReverseDependencyStore reverseDependencyStore()
    {
        long start = 0;
        if (DETAILS)
        {
            System.out.print("Calculating Reverse Dependencies................");
            start = System.currentTimeMillis();
        }
        ReverseDependencyStore out = new ReverseDependencyStore(isCalledBy(), methodsByProgramMethodOptimizationInfo());
        if (DETAILS)
        {
            long end = System.currentTimeMillis();
            System.out.printf(" took: %6d ms%n", (end - start));
        }

        return out;
    }


    /**
     * This function constructs a map which maps a class to all other classes which refer to the key.
     *
     * The construction of this map is a two step process:
     *   - Add all direct dependencies: These are found through a ReferencedClassVisitor
     *   - Add the references to superclasses: These are found through a hierarchyAccept
     */
    private MultiValueMap<Method, ClassMemberPair> isCalledBy()
    {
        MultiValueMap<Method, ClassMemberPair> isCalledBy = new MultiValueMap<>();
        classPool.classesAccept(new AllMethodVisitor(new AddDependencies(isCalledBy)));
        return isCalledBy;
    }


    /**
     * This function constructs a map which maps a ProgramMethodOptimizationInfo to all methods have the key
     * object as their OptimizationInfo.
     *
     * This map is constructed through a single pass over all methods in the classPool.
     *
     */
    private MultiValueMap<ProgramMethodOptimizationInfo, Method> methodsByProgramMethodOptimizationInfo()
    {
        MultiValueMap<ProgramMethodOptimizationInfo, Method> map = new MultiValueMap<>();
        classPool.classesAccept(new AllMethodVisitor(new FillMethodsByProgramMethodOptimizationInfo(map)));
        return map;
    }


    /**
     * Fills the map which maps ProgramMethodOptimizationInfo to all the methods which have the key object
     * as their OptimizationInfo.
     */
    private static class FillMethodsByProgramMethodOptimizationInfo
    implements           MemberVisitor
    {
        private final MultiValueMap<ProgramMethodOptimizationInfo, Method> map;


        public FillMethodsByProgramMethodOptimizationInfo(MultiValueMap<ProgramMethodOptimizationInfo, Method> map)
        {
            this.map = map;
        }


        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) { }


        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
        {
            MethodOptimizationInfo methodOptimizationInfo =
                MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
            if (methodOptimizationInfo instanceof ProgramMethodOptimizationInfo)
            {
                map.put((ProgramMethodOptimizationInfo)methodOptimizationInfo,programMethod);
            }
        }


        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {}


        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {}
    }

    /**
     * Fills the isCalledBy map which maps a method to all LocatedMembers which can be influenced by it.
     */
    private static class AddDependencies
    implements           MemberVisitor
    {
        private final MultiValueMap<Method, ClassMemberPair> isCalledBy;


        AddDependencies(MultiValueMap<Method, ClassMemberPair> isCalledBy)
        {
            this.isCalledBy = isCalledBy;
        }


        @Override
        public void visitAnyMember(Clazz clazz, Member member)
        {
            member.accept(clazz, new AllAttributeVisitor(
                          new AllInstructionVisitor(
                          new CalledMemberVisitor(
                          new Adder(new ClassMemberPair(clazz, member))))));
        }


        /**
         * Adds a certain tuple to the map
         */
        private class Adder
        implements    MemberVisitor
        {
            private final ClassMemberPair source;


            Adder(ClassMemberPair source)
            {
                this.source = source;
            }


            @Override
            public void visitAnyMember(Clazz clazz, Member member) { }


            @Override
            public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
            {
                isCalledBy.put(programMethod, source);
            }
        }
    }
}
