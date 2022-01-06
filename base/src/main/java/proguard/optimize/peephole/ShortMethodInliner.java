/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2022 Guardsquare NV
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

import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.visitor.InstructionVisitor;

/**
 * This AttributeVisitor inlines short methods in the code attributes that it visits.
 */
public class ShortMethodInliner extends MethodInliner {

    public ShortMethodInliner(boolean microEdition,
                              boolean android,
                              boolean allowAccessModification)
    {
        super(microEdition, android, allowAccessModification);
    }

    public ShortMethodInliner(boolean            microEdition,
                              boolean            android,
                              boolean            allowAccessModification,
                              InstructionVisitor extraInlinedInvocationVisitor)
    {
        super(microEdition, android, allowAccessModification, extraInlinedInvocationVisitor);
    }

    // Implementations for MethodInliner.

    @Override
    protected boolean shouldInline(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        return codeAttribute.u4codeLength <= (android ?
            MAXIMUM_INLINED_CODE_LENGTH_android :
            MAXIMUM_INLINED_CODE_LENGTH_JVM);
    }
}
