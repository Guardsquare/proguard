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
package proguard.optimize.lambdainline;

import proguard.classfile.instruction.Instruction;

/**
 * A class that holds an instruction and offset at which it was found, this is useful in some cases where we need to
 * return both of them form somewhere.
 */
public final class InstructionAtOffset {
    private final Instruction instruction;
    private final int offset;

    InstructionAtOffset(Instruction instruction, int offset) {
        this.instruction = instruction;
        this.offset = offset;
    }

    public Instruction instruction() {
        return instruction;
    }

    public int offset() {
        return offset;
    }
}
