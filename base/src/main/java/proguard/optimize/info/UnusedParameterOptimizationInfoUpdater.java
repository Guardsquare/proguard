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
package proguard.optimize.info;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import proguard.optimize.*;

import java.util.*;

/**
 * This AttributeVisitor removes unused parameters from the optimization info
 * of the methods that it visits. This includes 'this' parameters.
 *
 * @see ParameterUsageMarker
 * @see MethodStaticizer
 * @see MethodDescriptorShrinker
 * @author Eric Lafortune
 */
public class UnusedParameterOptimizationInfoUpdater
implements   AttributeVisitor
{
    private static final Logger logger = LogManager.getLogger(UnusedParameterOptimizationInfoUpdater.class);


    private final MemberVisitor extraUnusedParameterMethodVisitor;


    /**
     * Creates a new UnusedParameterOptimizationInfoUpdater.
     */
    public UnusedParameterOptimizationInfoUpdater()
    {
        this(null);
    }


    /**
     * Creates a new UnusedParameterOptimizationInfoUpdater with an extra
     * visitor.
     * @param extraUnusedParameterMethodVisitor an optional extra visitor for
     *                                          all removed parameters.
     */
    public UnusedParameterOptimizationInfoUpdater(MemberVisitor extraUnusedParameterMethodVisitor)
    {
        this.extraUnusedParameterMethodVisitor = extraUnusedParameterMethodVisitor;
    }


    // Implementations for AttributeVisitor.

    public void visitAnyAttribute(Clazz clazz, Attribute attribute) {}


    public void visitCodeAttribute(Clazz clazz, Method method, CodeAttribute codeAttribute)
    {
        logger.debug("UnusedParameterOptimizationInfoUpdater: {}.{}{}",
                     clazz.getName(),
                     method.getName(clazz),
                     method.getDescriptor(clazz)
        );

        // Get the original parameter size that was saved.
        int oldParameterSize = ParameterUsageMarker.getParameterSize(method);

        // Compute the new parameter size from the shrunk descriptor.
        int newParameterSize =
            ClassUtil.internalMethodParameterSize(method.getDescriptor(clazz),
                                                   method.getAccessFlags());

        if (oldParameterSize > newParameterSize)
        {
            ProgramMethodOptimizationInfo programMethodOptimizationInfo =
                ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);

            List<Integer> removedParameters = new ArrayList<Integer>();
            for (int parameterIndex = 0, variableIndex = 0; variableIndex < oldParameterSize; parameterIndex++)
            {
                // Is the variable required as a parameter?
                if (!ParameterUsageMarker.isParameterUsed(method, variableIndex))
                {
                    logger.debug("  Deleting parameter #{} (v{})", parameterIndex, variableIndex);

                    removedParameters.add(parameterIndex);
                }

                variableIndex += programMethodOptimizationInfo.getParameterSize(parameterIndex);
            }

            // Remove the parameters in reverse order.
            for (int i = removedParameters.size() - 1; i >= 0; i--)
            {
                programMethodOptimizationInfo.removeParameter(removedParameters.get(i));
            }

            programMethodOptimizationInfo.setParameterSize(newParameterSize);
            programMethodOptimizationInfo.updateUsedParameters(-1L);

            // Visit the method, if required.
            if (extraUnusedParameterMethodVisitor != null)
            {
                method.accept(clazz, extraUnusedParameterMethodVisitor);
            }
        }
    }
}
