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

/**
 * This ClassVisitor inlines the direct subclasses into the program classes
 * that it visits, whenever possible.
 *
 * @see ClassMerger
 * @author Eric Lafortune
 */
public class VerticalClassMerger
implements   ClassVisitor
{
    private final boolean                       allowAccessModification;
    private final boolean                       mergeInterfacesAggressively;
    private final ClassVisitor                  extraClassVisitor;


    /**
     * Creates a new VerticalClassMerger.
     *
     * @param allowAccessModification     specifies whether the access modifiers
     *                                    of classes can be changed in order to
     *                                    merge them.
     * @param mergeInterfacesAggressively specifies whether interfaces may
     *                                    be merged aggressively.
     */
    public VerticalClassMerger(boolean allowAccessModification,
                               boolean mergeInterfacesAggressively)
    {
        this(allowAccessModification, mergeInterfacesAggressively, null);
    }


    /**
     * Creates a new VerticalClassMerger.

     * @param allowAccessModification     specifies whether the access modifiers
     *                                    of classes can be changed in order to
     *                                    merge them.
     * @param mergeInterfacesAggressively specifies whether interfaces may
     *                                    be merged aggressively.
     * @param extraClassVisitor           an optional extra visitor for all
     *                                    merged classes.
     */
    public VerticalClassMerger(boolean      allowAccessModification,
                               boolean      mergeInterfacesAggressively,
                               ClassVisitor extraClassVisitor           )
    {
        this.allowAccessModification     = allowAccessModification;
        this.mergeInterfacesAggressively = mergeInterfacesAggressively;
        this.extraClassVisitor           = extraClassVisitor;
    }


    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) { }


    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        // Try inlining all immediate subclasses into this class.
        programClass.subclassesAccept(new InjectedClassFilter(null,
                                      new ClassMerger(programClass,
                                                      allowAccessModification,
                                                      mergeInterfacesAggressively,
                                                      false,
                                                      extraClassVisitor)));
    }
}
