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

/**
 * This class represents a keep option with class specification.
 *
 * @author Eric Lafortune
 */
public class KeepClassSpecification extends ClassSpecification
{
    public final boolean            markClasses;
    public final boolean            markClassMembers;
    public final boolean            markConditionally;
    public final boolean            markDescriptorClasses;
    public final boolean            markCodeAttributes;
    public final boolean            allowShrinking;
    public final boolean            allowOptimization;
    public final boolean            allowObfuscation;
    public final ClassSpecification condition;


    /**
     * Creates a new KeepClassSpecification.
     * @param markClasses           specifies whether to mark the classes.
     * @param markClassMembers      specifies whether to mark the class
     *                              members.
     * @param markConditionally     specifies whether to mark the classes and
     *                              class members conditionally. If true,
     *                              classes and class members are marked, on
     *                              the condition that all specified class
     *                              members are present.
     * @param markDescriptorClasses specifies whether to mark the classes in
     *                              the descriptors of the marked class members.
     * @param markCodeAttributes    specified whether to mark the code attributes
     *                              of the marked class methods.
     * @param allowShrinking        specifies whether shrinking is allowed.
     * @param allowOptimization     specifies whether optimization is allowed.
     * @param allowObfuscation      specifies whether obfuscation is allowed.
     * @param condition             an optional extra condition.
     * @param classSpecification    the specification of classes and class
     *                              members.
     */
    public KeepClassSpecification(boolean            markClasses,
                                  boolean            markClassMembers,
                                  boolean            markConditionally,
                                  boolean            markDescriptorClasses,
                                  boolean            markCodeAttributes,
                                  boolean            allowShrinking,
                                  boolean            allowOptimization,
                                  boolean            allowObfuscation,
                                  ClassSpecification condition,
                                  ClassSpecification classSpecification)
    {
        super(classSpecification);

        this.markClasses           = markClasses;
        this.markClassMembers      = markClassMembers;
        this.markConditionally     = markConditionally;
        this.markDescriptorClasses = markDescriptorClasses;
        this.markCodeAttributes    = markCodeAttributes;
        this.allowShrinking        = allowShrinking;
        this.allowOptimization     = allowOptimization;
        this.allowObfuscation      = allowObfuscation;
        this.condition             = condition;
    }


    /**
     * Creates a new KeepClassSpecification.
     *
     * @param markClassesAndMembers specifies whether to mark the classes.
     * @param markConditionally     specifies whether to mark the classes and
     *                              class members conditionally. If true,
     *                              classes and class members are marked, on
     *                              the condition that all specified class
     *                              members are present.
     * @param markDescriptorClasses specifies whether to mark the classes in
     *                              the descriptors of the marked class members.
     * @param markCodeAttributes    specified whether to mark the code attributes
     *                              of the marked class methods.
     * @param allowShrinking        specifies whether shrinking is allowed.
     * @param allowOptimization     specifies whether optimization is allowed.
     * @param allowObfuscation      specifies whether obfuscation is allowed.
     * @param condition             an optional extra condition.
     * @param classSpecification    the specification of classes and class
     *                              members.
     */
    @Deprecated
    public KeepClassSpecification(boolean            markClassesAndMembers,
                                  boolean            markConditionally,
                                  boolean            markDescriptorClasses,
                                  boolean            markCodeAttributes,
                                  boolean            allowShrinking,
                                  boolean            allowOptimization,
                                  boolean            allowObfuscation,
                                  ClassSpecification condition,
                                  ClassSpecification classSpecification)
    {
        this(markClassesAndMembers,
             markClassesAndMembers,
             markConditionally,
             markDescriptorClasses,
             markCodeAttributes,
             allowShrinking,
             allowOptimization,
             allowObfuscation,
             condition,
             classSpecification);
    }

    // Implementations for Object.

    @Override
    public boolean equals(Object object)
    {
        if (object == null ||
            this.getClass() != object.getClass())
        {
            return false;
        }

        KeepClassSpecification other = (KeepClassSpecification)object;
        return
            this.markClasses           == other.markClasses           &&
            this.markClassMembers      == other.markClassMembers      &&
            this.markConditionally     == other.markConditionally     &&
            this.markDescriptorClasses == other.markDescriptorClasses &&
            this.markCodeAttributes    == other.markCodeAttributes    &&
            this.allowShrinking        == other.allowShrinking        &&
            this.allowOptimization     == other.allowOptimization     &&
            this.allowObfuscation      == other.allowObfuscation      &&
            (this.condition            == null ?
                 other.condition       == null :
                 this.condition           .equals(other.condition))   &&
            super.equals(other);
    }

    @Override
    public int hashCode()
    {
        return
            (markClasses           ? 0 :                    1) ^
            (markClassMembers      ? 0 :                    2) ^
            (markConditionally     ? 0 :                    4) ^
            (markDescriptorClasses ? 0 :                    8) ^
            (markCodeAttributes    ? 0 :                   16) ^
            (allowShrinking        ? 0 :                   32) ^
            (allowOptimization     ? 0 :                   64) ^
            (allowObfuscation      ? 0 :                  128) ^
            (condition == null     ? 0 : condition.hashCode()) ^
            super.hashCode();
    }

    @Override
    public Object clone()
    {
//        try
//        {
            return super.clone();
//        }
//        catch (CloneNotSupportedException e)
//        {
//            return null;
//        }
    }
}
