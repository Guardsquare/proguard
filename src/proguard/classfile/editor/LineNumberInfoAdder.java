/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2013 Eric Lafortune (eric@graphics.cornell.edu)
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
package proguard.classfile.editor;

import proguard.classfile.attribute.visitor.LineNumberInfoVisitor;
import proguard.classfile.attribute.*;
import proguard.classfile.*;

/**
 * This LineNumberInfoVisitor adds all line numbers that it visits to the given
 * target line number attribute.
 */
public class LineNumberInfoAdder
implements   LineNumberInfoVisitor
{
    private final LineNumberTableAttributeEditor lineNumberTableAttributeEditor;


    /**
     * Creates a new LineNumberInfoAdder that will copy line numbers into the
     * given target line number table.
     */
    public LineNumberInfoAdder(LineNumberTableAttribute targetLineNumberTableAttribute)
    {
        this.lineNumberTableAttributeEditor = new LineNumberTableAttributeEditor(targetLineNumberTableAttribute);
    }


    // Implementations for LineNumberInfoVisitor.

    public void visitLineNumberInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberInfo lineNumberInfo)
    {
        // Create a new line number.
        LineNumberInfo newLineNumberInfo =
            new LineNumberInfo(lineNumberInfo.u2startPC,
                               lineNumberInfo.u2lineNumber);

        // Add it to the target.
        lineNumberTableAttributeEditor.addLineNumberInfo(newLineNumberInfo);
    }
}
