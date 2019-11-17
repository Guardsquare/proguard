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
package proguard.classfile.editor;

import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.util.SimplifiedVisitor;

/**
 * This AttributeVisitor adds the line numbers of all line number attributes
 * that it visits to the given target line number attribute. It ensures that
 * the sources of the line numbers are preserved or set.
 */
public class LineNumberInfoAdder
extends      SimplifiedVisitor
implements   AttributeVisitor,
             LineNumberInfoVisitor
{
    private final LineNumberTableAttributeEditor lineNumberTableAttributeEditor;

    private String source;


    /**
     * Creates a new LineNumberInfoAdder that will copy line numbers into the
     * given target line number table.
     */
    public LineNumberInfoAdder(LineNumberTableAttribute targetLineNumberTableAttribute)
    {
        this.lineNumberTableAttributeEditor = new LineNumberTableAttributeEditor(targetLineNumberTableAttribute);
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitLineNumberTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberTableAttribute lineNumberTableAttribute)
    {
        // Remember the source.
        source =
            clazz.getName()                                + '.' +
            method.getName(clazz)                          +
            method.getDescriptor(clazz)                    + ':' +
            lineNumberTableAttribute.getLowestLineNumber() + ':' +
            lineNumberTableAttribute.getHighestLineNumber();

        // Copy all line numbers.
        lineNumberTableAttribute.lineNumbersAccept(clazz, method, codeAttribute, this);
    }


    // Implementations for LineNumberInfoVisitor.

    public void visitLineNumberInfo(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberInfo lineNumberInfo)
    {
        // Make sure we have a source.
        String newSource = lineNumberInfo.getSource() != null ?
            lineNumberInfo.getSource() :
            source;

        // Create a new line number.
        LineNumberInfo newLineNumberInfo =
            new ExtendedLineNumberInfo(lineNumberInfo.u2startPC,
                                       lineNumberInfo.u2lineNumber,
                                       newSource);

        // Add it to the target.
        lineNumberTableAttributeEditor.addLineNumberInfo(newLineNumberInfo);
    }
}
