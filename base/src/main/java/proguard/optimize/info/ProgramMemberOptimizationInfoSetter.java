/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
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
package proguard.optimize.info;

import proguard.classfile.*;
import proguard.classfile.util.*;
import proguard.classfile.visitor.MemberVisitor;

/**
 * This MemberVisitor attaches a ProgramFieldOptimizationInfo instance to every
 * field and a ProgramMethodOptimizationInfo instance to every method that is
 * not being kept that it visits.
 *
 * @author Eric Lafortune
 */
public class ProgramMemberOptimizationInfoSetter
implements   MemberVisitor
{
    private final boolean overwrite;
    private final boolean optimizeConservatively;


    /**
     * Creates a new ProgramMemberOptimizationInfoSetter that only attaches a
     * ProgramFieldOptimizationInfo to a member if no other info is present
     * on the member yet, and does not apply conservative optimization
     */
    public ProgramMemberOptimizationInfoSetter()
    {
        this(false);
    }


    /**
     * Creates a new ProgramMemberOptimizationInfoSetter that does not
     * apply conservative optimization.
     *
     * @param overwrite boolean indicating whether an existing processing info on
     *                  a visited member should be overwritten or not.
     */
    public ProgramMemberOptimizationInfoSetter(boolean overwrite)
    {
        this(overwrite, false);
    }

    /**
     * Creates a new ProgramMemberOptimizationInfoSetter
     * @param overwrite              boolean indicating whether an existing
     *                               processing info on a visited member should
     *                               be overwritten or not.
     * @param optimizeConservatively boolean indicating whether conservative
     *                               optimization should be applied
     */
    public ProgramMemberOptimizationInfoSetter(boolean overwrite,
                                               boolean optimizeConservatively)
    {
        this.overwrite              = overwrite;
        this.optimizeConservatively = optimizeConservatively;
    }

    // Implementations for MemberVisitor.

    public void visitProgramField(ProgramClass programClass, ProgramField programField)
    {
        if (programField.getProcessingInfo() == null || overwrite)
        {
            ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(programClass,
                                                                         programField,
                                                                         optimizeConservatively);
        }
    }


    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        if (MethodLinker.lastMember(programMethod).getProcessingInfo() == null || overwrite)
        {
            ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(programClass,
                                                                           programMethod);
        }
    }
}
