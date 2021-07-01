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

import proguard.classfile.Clazz;

/**
 * This class stores some optimization information that can be attached to
 * a class.
 *
 * @author Eric Lafortune
 */
public class ClassOptimizationInfo
{
    protected boolean hasNoSideEffects = false;


    /**
     * Specifies that loading the class has no side effects.
     */
    public void setNoSideEffects()
    {
        hasNoSideEffects = true;
    }


    /**
     * Returns whether loading the class has side effects.
     */
    public boolean hasNoSideEffects()
    {
        return hasNoSideEffects;
    }


    /**
     * Returns whether the class is kept.
     */
    // TODO: This information is now available from the processing flags.
    public boolean isKept()
    {
        return true;
    }


    public boolean containsConstructors()
    {
        return true;
    }


    /**
     * Returns whether the class is instantiated in the known code.
     */
    public boolean isInstantiated()
    {
        return true;
    }


    /**
     * Returns whether the class is part of an 'instanceof' instruction in the
     * known code.
     */
    public boolean isInstanceofed()
    {
        // We're relaxing the strict assumption of "true".
        return !hasNoSideEffects;
    }


    /**
     * Returns whether the class is loaded with an 'ldc' instruction (a .class
     * construct in Java) in the known code.
     */
    public boolean isDotClassed()
    {
        // We're relaxing the strict assumption of "true".
        return !hasNoSideEffects;
    }


    /**
     * Returns whether the class is a Throwable that is caught in an exception
     * handler in the known code.
     */
    public boolean isCaught()
    {
        return true;
    }


    /**
     * Returns whether the class is an enum type that can be simplified to a
     * primitive integer.
     */
    public boolean isSimpleEnum()
    {
        return false;
    }


    /**
     * Returns whether instances of the class are ever escaping to the heap.
     * Otherwise, any instances are just created locally and passed as
     * parameters.
     */
    public boolean isEscaping()
    {
        return true;
    }


    /**
     * Returns whether loading the class has any side effects.
     */
    public boolean hasSideEffects()
    {
        return !hasNoSideEffects;
    }


    /**
     * Returns whether the class contains any package visible class members.
     */
    public boolean containsPackageVisibleMembers()
    {
        return true;
    }


    /**
     * Returns whether any code in the class accesses any package visible
     * class members.
     */
    public boolean invokesPackageVisibleMembers()
    {
        return true;
    }


    /**
     * Returns whether the class may be merged with other classes.
     */
    public boolean mayBeMerged()
    {
        return false;
    }


    /**
     * Returns the class for which this class is a simple wrapper without any
     * additional functionality, or null otherwise.
     */
    public Clazz getWrappedClass()
    {
        return null;
    }


    /**
     * Returns the class into which this class can be merged.
     */
    public Clazz getTargetClass()
    {
        return null;
    }


    /**
     * Creates and sets a ClassOptimizationInfo instance on the given class.
     */
    public static void setClassOptimizationInfo(Clazz clazz)
    {
        clazz.setProcessingInfo(new ClassOptimizationInfo());
    }


    /**
     * Returns the ClassOptimizationInfo instance from the given class.
     */
    public static ClassOptimizationInfo getClassOptimizationInfo(Clazz clazz)
    {
        return (ClassOptimizationInfo)clazz.getProcessingInfo();
    }
}
