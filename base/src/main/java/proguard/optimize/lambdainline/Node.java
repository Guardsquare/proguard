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

import java.util.List;

/**
 * A very simple DTO that contains Node objects used to build a tree of InstructionAtOffset objects used in the
 * SourceTracer. A tree is used because an argument of a method call can have multiple source locations. The program
 * will at runtime only get the value form one location of course, but we cannot predict which branches the code will
 * take at compile time, because of this we have a tree that contains all the options.
 */
public final class Node {
    public InstructionAtOffset value;
    public List<Node> children;

    Node(InstructionAtOffset value, List<Node> children) {
        this.value = value;
        this.children = children;
    }
}
