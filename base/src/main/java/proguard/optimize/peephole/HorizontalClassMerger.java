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
package proguard.optimize.peephole;

import proguard.classfile.*;
import proguard.classfile.visitor.*;
import proguard.optimize.KeepMarker;
import proguard.optimize.info.*;
import proguard.util.*;

import java.util.*;
import java.util.stream.*;

/**
 * This ClassPoolVisitor inlines siblings in the program classes that it visits, whenever possible.
 *
 * @author Eric Lafortune
 * @see ClassMerger
 */
public class HorizontalClassMerger
implements   ClassPoolVisitor
{
    private static final int NOT_INSTANTIATED_NO_MEMBERS   = 3;
    private static final int NOT_INSTANTIATED_WITH_MEMBERS = 2;
    private static final int INSTANTIATED_NO_MEMBERS       = 1;
    private static final int INSTANTIATED_WITH_MEMBERS     = 0;

    private final boolean      allowAccessModification;
    private final boolean      mergeInterfacesAggressively;
    private final ClassVisitor extraClassVisitor;
    private final Set<String>  forbiddenClassNames;


    /**
     * Creates a new HorizontalClassMerger.
     *  @param allowAccessModification    specifies whether the access modifiers of classes can be changed in order to
     *                                    merge them.
     * @param mergeInterfacesAggressively specifies whether interfaces may be merged aggressively.
     * @param forbiddenClassNames         specifies the names of classes which are excluded from the chance of being merged
     */
    public HorizontalClassMerger(boolean allowAccessModification,
                                 boolean mergeInterfacesAggressively,
                                 Set<String> forbiddenClassNames)
    {
        this(allowAccessModification, mergeInterfacesAggressively, forbiddenClassNames, null);
    }


    /**
     * Creates a new HorizontalClassMerger.
     *  @param allowAccessModification     specifies whether the access modifiers of classes can be changed in order to
     *                                    merge them.
     * @param mergeInterfacesAggressively specifies whether interfaces may be merged aggressively.
     * @param forbiddenClassNames         specifies the names of classes which are excluded from the chance of being merged
     * @param extraClassVisitor           an optional extra visitor for all merged classes.
     */
    public HorizontalClassMerger(boolean allowAccessModification,
                                 boolean mergeInterfacesAggressively,
                                 Set<String> forbiddenClassNames,
                                 ClassVisitor extraClassVisitor)
    {
        this.allowAccessModification = allowAccessModification;
        this.mergeInterfacesAggressively = mergeInterfacesAggressively;
        this.forbiddenClassNames = forbiddenClassNames;
        this.extraClassVisitor = extraClassVisitor;
    }

    // Implementations for ClassPoolVisitor.


    public void visitClassPool(ClassPool classPool)
    {
        // 1. Make a stream of classes in the classPool
        // 2. Filter out all classes with no chance of success
        // 3. Partition the remaining classes into sets of siblings
        Map<Clazz, List<Clazz>> siblingsCollections = StreamSupport.stream(classPool.classes().spliterator(), false)
                                                                .filter(this::isCandidateForMerging)
                                                                .collect(Collectors.groupingBy(Clazz::getSuperClass));

        // 4. Try horizontal merging for each set of siblings
        siblingsCollections.values().forEach(this::handleSiblings);
    }


    // Utility Methods

    /**
     * This method tries to merge a set of siblings, but preclassifies them so that the introducesUnwantedFields
     * constraint of the class merger is certainly fullfilled.
     */
    private void handleSiblings(List<Clazz> classes)
    {
        Map<Integer, List<Clazz>> partition =
            classes.stream().collect(Collectors.groupingBy(HorizontalClassMerger::classify));

        List<Clazz> notInstantiatedNoMembers   = partition.getOrDefault(NOT_INSTANTIATED_NO_MEMBERS,
                                                                        Collections.emptyList());
        List<Clazz> notInstantiatedWithMembers = partition.getOrDefault(NOT_INSTANTIATED_WITH_MEMBERS,
                                                                        Collections.emptyList());
        List<Clazz> instantiatedNoMembers      = partition.getOrDefault(INSTANTIATED_NO_MEMBERS,
                                                                        Collections.emptyList());

        // The conditions for merging are almost symmetric, the extra constraints are put onto the
        // source of the merging, so we opt to put the largest set as the first set.
        mergeInto(classes,                    notInstantiatedNoMembers);
        mergeInto(notInstantiatedWithMembers, notInstantiatedWithMembers);
        mergeInto(instantiatedNoMembers,      instantiatedNoMembers);
    }


    /**
     * Tries to merge all sourceClasses into the targetClasses
     * @param sourceClasses
     * @param targetClasses
     */
    private void mergeInto(List<Clazz> sourceClasses, List<Clazz> targetClasses)
    {
        for (Clazz target : targetClasses)
        {
            ClassMerger classMerger = new ClassMerger((ProgramClass)target,
                                                      allowAccessModification,
                                                      mergeInterfacesAggressively,
                                                      false,
                                                      extraClassVisitor);
            for (Clazz source : sourceClasses)
            {
                source.accept(classMerger);
            }
        }
    }


    /**
     * Checks if a class can trivially not be used for merging (either as target or source)
     * @param clazz the class.
     * @return true if the give clazz is a candidate for merging.
     */
    public boolean isCandidateForMerging(Clazz clazz)
    {
        return !(
            KeepMarker.isKept(clazz)
            || ClassMerger.getTargetClass(clazz) != null
            || clazz instanceof LibraryClass
            || ClassMerger.hasSignatureAttribute(clazz)
            || DotClassMarker.isDotClassed(clazz)
            || (clazz.getAccessFlags() & AccessConstants.ANNOTATION) != 0
            || (clazz.getProcessingFlags() & ProcessingFlags.INJECTED) != 0
            || forbiddenClassNames.contains(clazz.getName())
            || clazz.getSuperClass() == null);
    }


    /**
     * Returns an integer that classifies a class into four types:
     *  0: This class is     instantiated and contains    non-static members
     *  1: This class is not instantiated and contains    non-static members.
     *  2: This class is     instantiated and contains no non-static members
     *  3: This class is not instantiated and contains no non-static members
     */
    private static Integer classify(Clazz clazz)
    {
        boolean isInstantiated = InstantiationClassMarker.isInstantiated(clazz)
                                 || (((ProgramClass)clazz).subClassCount > 0);

        MemberCounter counter = new MemberCounter();
        // Count all non-static fields in the the source class.
        clazz.fieldsAccept(new MemberAccessFilter(0, AccessConstants.STATIC, counter));
        boolean hasMembers = counter.getCount() > 0;

        if (isInstantiated)
        {
            if (hasMembers)
            {
                return INSTANTIATED_WITH_MEMBERS;
            }
            else
            {
                return INSTANTIATED_NO_MEMBERS;
            }
        }
        else
        {
            if (hasMembers)
            {
                return NOT_INSTANTIATED_WITH_MEMBERS;
            }
            else
            {
                return NOT_INSTANTIATED_NO_MEMBERS;
            }
        }
    }

}
