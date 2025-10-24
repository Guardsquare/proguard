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
package proguard.optimize.peephole;

import proguard.AppView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LineNumberInfo;
import proguard.classfile.attribute.LineNumberInfoBlock;
import proguard.classfile.attribute.LineNumberTableAttribute;
import proguard.classfile.attribute.StructuredLineNumberInfo;
import proguard.classfile.attribute.visitor.AllAttributeVisitor;
import proguard.classfile.attribute.visitor.AllLineNumberInfoVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.attribute.visitor.LineNumberRangeFinder;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.pass.Pass;

import java.util.Arrays;
import java.util.Stack;

/**
 * This pass disambiguates line numbers, in the classes that it
 * visits. It shifts line numbers that originate from different classes
 * (e.g. due to method inlining or class merging) to blocks that don't
 * overlap with the main line numbers and with each other. The line numbers
 * then uniquely identify the inlined and merged code in the classes.
 *
 * @author Eric Lafortune
 */
public class LineNumberLinearizer
implements   Pass,
             ClassVisitor,
             MemberVisitor,
             AttributeVisitor
{
    private static final Logger logger = LogManager.getLogger(LineNumberLinearizer.class);

    public  static final int SHIFT_ROUNDING       = 1000;
    private static final int SHIFT_ROUNDING_LIMIT = 50000;


    private final Stack<MyLineNumberBlock> enclosingLineNumbers = new Stack<>();
    private       LineNumberInfo           previousLineNumberInfo;
    private       int                      highestUsedLineNumber;
    private       int                      currentLineNumberShift;


    /**
     * Disambiguates the line numbers of all program classes, after
     * optimizations like method inlining and class merging.
     */
    @Override
    public void execute(AppView appView) {
        appView.programClassPool.classesAccept(this);
    }

    // Implementations for ClassVisitor.

    @Override
    public void visitAnyClass(Clazz clazz) {
    }

    @Override
    public void visitProgramClass(ProgramClass programClass)
    {
        // Find the highest line number in the entire class.
        LineNumberRangeFinder lineNumberRangeFinder =
            new LineNumberRangeFinder();

        programClass.methodsAccept(new AllAttributeVisitor(true,
                                   new AllLineNumberInfoVisitor(
                                   lineNumberRangeFinder)));

        // Are there any inlined line numbers?
        if (lineNumberRangeFinder.hasSource())
        {
            // Remember the minimum initial shift.
            highestUsedLineNumber = lineNumberRangeFinder.getHighestLineNumber();

            // Shift the inlined line numbers.
            programClass.methodsAccept(this);
        }
    }

    // Implementations for MemberVisitor.

    @Override
    public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod)
    {
        programMethod.attributesAccept(programClass, this);
    }

    // Implementations for AttributeVisitor.

    @Override
    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}

    @Override
    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        codeAttribute.attributesAccept(clazz, method, this);
    }

    @Override
    public void visitLineNumberTableAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute, LineNumberTableAttribute lineNumberTableAttribute)
    {
        logger.debug("LineNumberLinearizer [{}.{}{}]:",
                     clazz.getName(),
                     method.getName(clazz),
                     method.getDescriptor(clazz)
        );

        enclosingLineNumbers.clear();

        // Figure out which lines need linearizing. Only freshly inlined blocks need to be linearized.

        LineNumberInfo[] infos = lineNumberTableAttribute.lineNumberTable;
        int lineNumberTableLength = lineNumberTableAttribute.u2lineNumberTableLength;
        boolean[] inlinedBlock = new boolean[lineNumberTableLength];

        int currentDepth = 0;
        for (int i = 0; i < lineNumberTableLength; i++)
        {
            LineNumberInfo currentInfo = infos[i];
            if (currentInfo.u2lineNumber == MethodInliner.INLINED_METHOD_START_LINE_NUMBER)
            {
                currentDepth++;
            }

            inlinedBlock[i] = currentDepth > 0;

            if (currentInfo.u2lineNumber == MethodInliner.INLINED_METHOD_END_LINE_NUMBER)
            {
                currentDepth--;
            }
        }

        // Linearize the line numbers.

        LineNumberInfo previousLineNumberInfo = null;
        for (int i = 0; i < lineNumberTableLength; i++)
        {
            LineNumberInfo lineNumberInfo = infos[i];
            String source = lineNumberInfo.getSource();

            logger.debug("    [{}] line {}{}", lineNumberInfo.u2startPC, lineNumberInfo.u2lineNumber, source == null ? "" : " [" + source + "]");

            // Is it an inlined line number?
            if (source != null && inlinedBlock[i])
            {
                int lineNumber = lineNumberInfo.u2lineNumber;

                // Are we entering a new inlined block?
                if (lineNumber == MethodInliner.INLINED_METHOD_START_LINE_NUMBER)
                {
                    // Remember information about the inlined block.
                    enclosingLineNumbers.push(
                            previousLineNumberInfo != null
                                    ? new MyLineNumberBlock(
                                    currentLineNumberShift,
                                    previousLineNumberInfo.u2lineNumber,
                                    previousLineNumberInfo.getSource() != null
                                            ? previousLineNumberInfo.getBlock()
                                            : null)
                                    : new MyLineNumberBlock(0, 0, null));

                    // Parse the end line number from the source string, so we know how large a block this
                    // will be.
                    int separatorIndex1 = source.indexOf(':');
                    int separatorIndex2 = source.indexOf(':', separatorIndex1 + 1);

                    int startLineNumber =
                            Integer.parseInt(source.substring(separatorIndex1 + 1, separatorIndex2));
                    int endLineNumber = Integer.parseInt(source.substring(separatorIndex2 + 1));

                    // TODO: this matches a quirk in the old behavior where the opening line is always :0:0
                    // this is a bug that probably causes overlapping line numbers but for now we will match
                    // this behavior so we can directly compare old and new mappings.
                    startLineNumber = 0;
                    endLineNumber = 0;

                    // Start shifting, if necessary, so the block ends up beyond the highest used line number.
                    // We're striving for rounded shifts, unless we've reached a given limit, to avoid running
                    // out of line numbers too quickly.
                    currentLineNumberShift =
                            highestUsedLineNumber > SHIFT_ROUNDING_LIMIT
                                    ? highestUsedLineNumber - startLineNumber + 1
                                    : startLineNumber > highestUsedLineNumber
                                    ? 0
                                    : (highestUsedLineNumber - startLineNumber + SHIFT_ROUNDING)
                                    / SHIFT_ROUNDING
                                    * SHIFT_ROUNDING;

                    highestUsedLineNumber = endLineNumber + currentLineNumberShift;


                    logger.debug(" (enter with shift {})", currentLineNumberShift);
                }

                // Are we exiting an inlined block?
                else if (lineNumber == MethodInliner.INLINED_METHOD_END_LINE_NUMBER)
                {
                    // TODO: There appear to be cases where the stack is empty at this point, so we've added a
                    // check.
                    if (enclosingLineNumbers.isEmpty())
                    {
                        logger.debug("Problem linearizing line numbers for optimized code ({}.{})", clazz.getName(), method.getName(clazz));
                    }
                    else
                    {
                        // Pop information about the enclosing line number.
                        MyLineNumberBlock lineNumberBlock = enclosingLineNumbers.pop();

                        // Set this end of the block to the line at which it was inlined.
                        lineNumberInfo =
                                lineNumberBlock.enclosingSource != null
                                        ? lineNumberBlock.enclosingSource.line(
                                        lineNumberInfo.u2startPC, lineNumberBlock.enclosingLineNumber)
                                        : new LineNumberInfo(
                                        lineNumberInfo.u2startPC, lineNumberBlock.enclosingLineNumber);
                        infos[i] = lineNumberInfo;

                        // Reset the shift to the shift of the block.
                        currentLineNumberShift = lineNumberBlock.lineNumberShift;

                        logger.debug(" (exit to shift {})", currentLineNumberShift);
                    }
                }
                else
                {
                    logger.debug(" (apply shift {})", currentLineNumberShift);


                    // Apply the shift.
                    lineNumberInfo.u2lineNumber += currentLineNumberShift;
                }
            }

            previousLineNumberInfo = lineNumberInfo;

            logger.debug(" -> line {}", lineNumberInfo.u2lineNumber);
        }

        lineNumberTableAttribute.lineNumberTable =
                Arrays.stream(infos, 0, lineNumberTableLength)
                        .filter(info -> info.u2lineNumber != MethodInliner.INLINED_METHOD_START_LINE_NUMBER)
                        .toArray(LineNumberInfo[]::new);
        lineNumberTableAttribute.u2lineNumberTableLength =
                lineNumberTableAttribute.lineNumberTable.length;
    }


    /**
     * This class represents a block of line numbers that originates from the
     * same inlined method.
     */
    private static class MyLineNumberBlock
    {
        public final int                 lineNumberShift;
        public final int                 enclosingLineNumber;
        public final LineNumberInfoBlock enclosingSource;

        public MyLineNumberBlock(int                 lineNumberShift,
                                 int                 enclosingLineNumber,
                                 LineNumberInfoBlock enclosingSource) {
            this.lineNumberShift     = lineNumberShift;
            this.enclosingLineNumber = enclosingLineNumber;
            this.enclosingSource     = enclosingSource;
        }
    }
}
