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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.ClassPool;
import proguard.classfile.visitor.*;
import proguard.optimize.info.ParameterEscapedMarker;

/**
 * A simple class pool visitor that will output timing information.
 */
public class TimedClassPoolVisitor
implements   ClassPoolVisitor
{
    private static final Logger logger = LogManager.getFormatterLogger(TimedClassPoolVisitor.class);

    private final String           message;
    private final ClassPoolVisitor classPoolVisitor;

    public TimedClassPoolVisitor(String message, ClassVisitor classVisitor)
    {
        this(message, new AllClassVisitor(classVisitor));
    }

    public TimedClassPoolVisitor(String message, ClassPoolVisitor classPoolVisitor)
    {
        this.message          = message;
        this.classPoolVisitor = classPoolVisitor;
    }


    // Implementations for ClassPoolVisitor.

    public void visitClassPool(ClassPool classPool)
    {
        long start = System.currentTimeMillis();

        classPool.accept(classPoolVisitor);

        long end = System.currentTimeMillis();
        logger.trace("%s %s took: %6d ms", message, getPadding(message.length(), 48), (end - start));
    }


    // Private helper methods

    private String getPadding(int pos, int size)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = pos; i < size; i++)
        {
            sb.append('.');
        }
        return sb.toString();
    }
}
