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

import proguard.classfile.attribute.visitor.*;
import proguard.classfile.instruction.visitor.*;
import proguard.classfile.visitor.MemberVisitor;
import proguard.evaluation.*;
import proguard.evaluation.value.TypedReferenceValueFactory;
import proguard.optimize.evaluation.*;
import proguard.optimize.info.*;

/**
 * The Member Visitors created by this factory checks whether a particular member contains side effects.
 */
class SideEffectVisitorMarkerFactory
implements InfluenceFixpointVisitor.MemberVisitorFactory
{
    public SideEffectVisitorMarkerFactory()
    {
    }

    // Implementations for MemberVisitorFactory

    public MemberVisitor createMemberVisitor(MemberVisitor influencedMethodCollector)
    {
        ReferenceTracingValueFactory referenceTracingValueFactory1 =
            new ReferenceTracingValueFactory(new TypedReferenceValueFactory());
        PartialEvaluator partialEvaluator =
            new PartialEvaluator(referenceTracingValueFactory1,
                                 new ParameterTracingInvocationUnit(new BasicInvocationUnit(referenceTracingValueFactory1)),
                                 false,
                                 referenceTracingValueFactory1);
        InstructionUsageMarker instructionUsageMarker =
            new InstructionUsageMarker(partialEvaluator, false, false);


        // Create the various markers.
        // They will be used as code attribute visitors and
        // instruction visitors this time.
        // We're currently marking read and written fields once,
        // outside of these iterations, for better performance,
        // at the cost of some effectiveness (test2209).
        //ReadWriteFieldMarker readWriteFieldMarker =
        //    new ReadWriteFieldMarker(repeatTrigger);
        SideEffectMethodMarker sideEffectMethodMarker = new SideEffectMethodMarker(influencedMethodCollector);
        ParameterEscapeMarker parameterEscapeMarker =
            new ParameterEscapeMarker(partialEvaluator, false, influencedMethodCollector);

        return
            new OptimizationInfoMemberFilter(
                // Methods with editable optimization info.
                new AllAttributeVisitor(
                    new DebugAttributeVisitor("Marking fields, methods, and parameters",
                                              new MultiAttributeVisitor(
                                                  partialEvaluator,
                                                  parameterEscapeMarker,
                                                  instructionUsageMarker,
                                                  new AllInstructionVisitor(
                                                      instructionUsageMarker.necessaryInstructionFilter(
                                                          new MultiInstructionVisitor(
                                                              // All read / write field instruction are already marked
                                                              // for all code (see above), there is no need to mark them again.
                                                              // If unused code is removed that accesses fields, the
                                                              // respective field will be removed in the next iteration.
                                                              // This is a trade-off between performance and correctness.
                                                              // TODO: improve the marking for read / write fields after
                                                              //       performance improvements have been implemented.
                                                              //readWriteFieldMarker,
                                                              sideEffectMethodMarker,
                                                              parameterEscapeMarker
                                                          ))))))

                // TODO: disabled for now, see comment above.
                // Methods without editable optimization info, for
                // which we can't mark side-effects or escaping
                // parameters, so we can save some effort.
                //new AllAttributeVisitor(
                //new DebugAttributeVisitor("Marking fields",
                //new MultiAttributeVisitor(
                //    partialEvaluator,
                //    instructionUsageMarker,
                //    new AllInstructionVisitor(
                //    instructionUsageMarker.necessaryInstructionFilter(
                //    readWriteFieldMarker)))))
            );
    }
}
