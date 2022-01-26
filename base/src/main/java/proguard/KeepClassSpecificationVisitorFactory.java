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
package proguard;

import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.visitor.*;
import proguard.classfile.visitor.*;
import proguard.util.WildcardManager;

import java.util.List;

/**
 * This factory creates visitors to efficiently travel to specified classes and
 * class members.
 *
 * @author Eric Lafortune
 */
public class KeepClassSpecificationVisitorFactory
extends      ClassSpecificationVisitorFactory
{
    private final boolean shrinking;
    private final boolean optimizing;
    private final boolean obfuscating;


    /**
     * Creates a new KeepClassSpecificationVisitorFactory that creates
     * visitors for the specified goal.
     *
     * @param shrinking   a flag that specifies whether the visitors are
     *                    intended for the shrinking step.
     * @param optimizing  a flag that specifies whether the visitors are
     *                    intended for the optimization step.
     * @param obfuscating a flag that specifies whether the visitors are
     *                    intended for the obfuscation step.
     */
    public KeepClassSpecificationVisitorFactory(boolean shrinking,
                                                boolean optimizing,
                                                boolean obfuscating)
    {
        this.shrinking   = shrinking;
        this.optimizing  = optimizing;
        this.obfuscating = obfuscating;
    }


    // Overriding implementations for ClassSpecificationVisitorFactory.

    /**
     * Constructs a ClassPoolVisitor to efficiently travel to the specified
     * classes, class members and code attributes.
     *
     * @param keepClassSpecifications the specifications of the class(es) and
     *                                class members to visit.
     * @param classVisitor            an optional ClassVisitor to be applied to
     *                                matching classes.
     * @param fieldVisitor            an optional MemberVisitor to be applied
     *                                to matching fields.
     * @param methodVisitor           an optional MemberVisitor to be applied
     *                                to matching methods.
     * @param attributeVisitor        an optional AttributeVisitor to be applied
     *                                to matching code attributes.
     */
    public ClassPoolVisitor createClassPoolVisitor(List             keepClassSpecifications,
                                                   ClassVisitor     classVisitor,
                                                   MemberVisitor    fieldVisitor,
                                                   MemberVisitor    methodVisitor,
                                                   AttributeVisitor attributeVisitor)
    {
        MultiClassPoolVisitor multiClassPoolVisitor = new MultiClassPoolVisitor();

        if (keepClassSpecifications != null)
        {
            for (int index = 0; index < keepClassSpecifications.size(); index++)
            {
                KeepClassSpecification keepClassSpecification =
                    (KeepClassSpecification)keepClassSpecifications.get(index);

                if ((shrinking   && !keepClassSpecification.allowShrinking)    ||
                    (optimizing  && !keepClassSpecification.allowOptimization) ||
                    (obfuscating && !keepClassSpecification.allowObfuscation))
                {
                    multiClassPoolVisitor.addClassPoolVisitor(
                        createClassPoolVisitor(keepClassSpecification,
                                               classVisitor,
                                               fieldVisitor,
                                               methodVisitor,
                                               attributeVisitor));
                }
            }
        }

        return multiClassPoolVisitor;
    }


    /**
     * Constructs a ClassPoolVisitor to efficiently travel to the specified
     * classes, class members, and attributes.
     *
     * @param keepClassSpecification the specifications of the class(es) and
     *                               class members to visit.
     * @param classVisitor           an optional ClassVisitor to be applied to
     *                               matching classes.
     * @param fieldVisitor           an optional MemberVisitor to be applied
     *                               to matching fields.
     * @param methodVisitor          an optional MemberVisitor to be applied
     *                               to matching methods.
     * @param attributeVisitor       an optional AttributeVisitor to be applied
     *                               to matching code attributes.
     */
    public ClassPoolVisitor createClassPoolVisitor(KeepClassSpecification keepClassSpecification,
                                                   ClassVisitor           classVisitor,
                                                   MemberVisitor          fieldVisitor,
                                                   MemberVisitor          methodVisitor,
                                                   AttributeVisitor       attributeVisitor)
    {
        // Create a global wildcard manager, so wildcards can be referenced
        // from regular expressions. They are identified by their indices,
        // which imposes a number of tricky constraints:
        // - They need to be parsed in the right order, so the list is filled
        //   out in the expected order (corresponding to the text file
        //   configuration).
        // - They need to be matched in the right order, so the variable
        //   matchers are matched before they are referenced.
        WildcardManager wildcardManager = new WildcardManager();

        // If specified, let the class visitor also visit the descriptor
        // classes and the signature classes.
        if (keepClassSpecification.markDescriptorClasses &&
            classVisitor != null)
        {
            fieldVisitor = fieldVisitor == null ?
                new MemberDescriptorReferencedClassVisitor(classVisitor) :
                new MultiMemberVisitor(
                        fieldVisitor,
                        new MemberDescriptorReferencedClassVisitor(classVisitor));

            methodVisitor = methodVisitor == null ?
                new MemberDescriptorReferencedClassVisitor(true, classVisitor) :
                new MultiMemberVisitor(
                        methodVisitor,
                        new MemberDescriptorReferencedClassVisitor(true, classVisitor));
        }

        // Don't visit the classes if not specified.
        if (!keepClassSpecification.markClasses &&
            !keepClassSpecification.markConditionally)
        {
            classVisitor = null;
        }

        // Do we have an attribute visitor?
        if (attributeVisitor != null)
        {
            // Don't visit the code attributes if not specified.
            attributeVisitor = keepClassSpecification.markCodeAttributes ?
                new AttributeNameFilter(Attribute.CODE, attributeVisitor) :
                null;
        }

        ClassSpecification condition = keepClassSpecification.condition;
        if (condition != null)
        {
            // Parse the condition. We need to parse it before the actual keep
            // specification, to make sure the list of variable string matchers
            // is filled out in the right order.

            // Create a placeholder for the class pool visitor that
            // corresponds to the actual keep specification. Note that we
            // visit the entire class pool for each matched class.
            MultiClassPoolVisitor keepClassPoolVisitor =
                new MultiClassPoolVisitor();

            // Parse the condition.
            ClassPoolVisitor conditionalKeepClassPoolVisitor =
                createClassTester(condition,
                                  keepClassPoolVisitor,
                                  wildcardManager);

            // Parse the actual keep specification and add it to the
            // placeholder.
            keepClassPoolVisitor.addClassPoolVisitor(
                createClassPoolVisitor(keepClassSpecification,
                                       classVisitor,
                                       fieldVisitor,
                                       methodVisitor,
                                       attributeVisitor,
                                       wildcardManager));

            return conditionalKeepClassPoolVisitor;
        }
        else
        {
            // Just parse the actual keep specification.
            return createClassPoolVisitor(keepClassSpecification,
                                          classVisitor,
                                          fieldVisitor,
                                          methodVisitor,
                                          attributeVisitor,
                                          wildcardManager);
        }
    }


    /**
     * Constructs a ClassPoolVisitor to efficiently travel to the specified
     * classes and class members.
     *
     * @param keepClassSpecification the specifications of the class(es) and class
     *                               members to visit.
     * @param classVisitor           an optional ClassVisitor to be applied to
     *                               matching classes.
     * @param fieldVisitor           an optional MemberVisitor to be applied
     *                               to matching fields.
     * @param methodVisitor          an optional MemberVisitor to be applied
     *                               to matching methods.
     * @param attributeVisitor       an optional AttributeVisitor to be applied
     *                               to matching code attributes.
     * @param wildcardManager        a scope for StringMatcher instances
     *                               that match wildcards.
     */
    private ClassPoolVisitor createClassPoolVisitor(KeepClassSpecification keepClassSpecification,
                                                    ClassVisitor           classVisitor,
                                                    MemberVisitor          fieldVisitor,
                                                    MemberVisitor          methodVisitor,
                                                    AttributeVisitor       attributeVisitor,
                                                    WildcardManager        wildcardManager)
    {
        // If specified, let the marker visit the class and its class
        // members conditionally.
        if (keepClassSpecification.markConditionally)
        {
            // Parse the condition. We need to parse it before the actual keep
            // specification, to make sure the list of variable string matchers
            // is filled out in the right order.

            // Create a placeholder for the class visitor that corresponds to
            // the actual keep specification.
            MultiClassVisitor keepClassVisitor =
                new MultiClassVisitor();

            // Parse the condition. Only add string matchers locally.
            ClassPoolVisitor conditionalKeepClassPoolVisitor =
                createClassTester(keepClassSpecification,
                                  keepClassVisitor,
                                  new WildcardManager(wildcardManager));

            // Parse the actual keep specification and add it to the
            // placeholder.
            keepClassVisitor.addClassVisitor(
                createCombinedClassVisitor(keepClassSpecification.attributeNames,
                                           keepClassSpecification.fieldSpecifications,
                                           keepClassSpecification.methodSpecifications,
                                           classVisitor,
                                           fieldVisitor,
                                           methodVisitor,
                                           attributeVisitor,
                                           wildcardManager));

            return conditionalKeepClassPoolVisitor;
        }
        else
        {
            // Just parse the actual keep specification.
            return super.createClassPoolVisitor(keepClassSpecification,
                                                classVisitor,
                                                fieldVisitor,
                                                methodVisitor,
                                                attributeVisitor,
                                                wildcardManager);
        }
    }
}
