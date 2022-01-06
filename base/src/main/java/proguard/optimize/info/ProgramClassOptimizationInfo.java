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
 * a class that can be analyzed in detail.
 *
 * @author Eric Lafortune
 */
public class ProgramClassOptimizationInfo
extends      ClassOptimizationInfo
{
    private volatile boolean containsConstructors          = false;
    private volatile boolean isInstantiated                = false;
    private volatile boolean isInstanceofed                = false;
    private volatile boolean isDotClassed                  = false;
    private volatile boolean isCaught                      = false;
    private volatile boolean isSimpleEnum                  = false;
    private volatile boolean isEscaping                    = false;
    private volatile boolean hasSideEffects                = false;
    private volatile boolean containsPackageVisibleMembers = false;
    private volatile boolean invokesPackageVisibleMembers  = false;
    private volatile boolean mayBeMerged                   = true;
    private volatile Clazz   wrappedClass;
    private volatile Clazz   targetClass;
    private volatile Clazz   lambdaGroup;


    public boolean isKept()
    {
        return false;
    }


    public void setContainsConstructors()
    {
        containsConstructors = true;
    }


    public boolean containsConstructors()
    {
        return containsConstructors;
    }

    /**
     * Specifies that the class is instantiated in the known code.
     */
    public void setInstantiated()
    {
        isInstantiated = true;
    }


    public boolean isInstantiated()
    {
        return isInstantiated;
    }


    /**
     * Specifies that the class is part of an 'instanceof' instruction in the
     * known code.
     */
    public void setInstanceofed()
    {
        isInstanceofed = true;
    }


    public boolean isInstanceofed()
    {
        return isInstanceofed;
    }


    /**
     * Specifies that the class is loaded with an 'ldc' instruction (a .class
     * construct in Java) in the known code.
     */
    public void setDotClassed()
    {
        isDotClassed = true;
    }


    public boolean isDotClassed()
    {
        return isDotClassed;
    }


    /**
     * Specifies that the class is a Throwable that is caught in an exception
     * handler in the known code.
     */
    public void setCaught()
    {
        isCaught = true;
    }


    public boolean isCaught()
    {
        return isCaught;
    }


    /**
     * Specifies whether the class is an enum type that can be simplified to a
     * primitive integer.
     */
    public void setSimpleEnum(boolean simple)
    {
        isSimpleEnum = simple;
    }


    public boolean isSimpleEnum()
    {
        return isSimpleEnum;
    }


    /**
     * Specifies that instances of the class are escaping to the heap.
     * Otherwise, any instances are just created locally and passed as
     * parameters.
     */
    public void setEscaping()
    {
        isEscaping = true;
    }


    public boolean isEscaping()
    {
        return isEscaping;
    }


    /**
     * Specifies that loading the class has side effects.
     */
    public void setSideEffects()
    {
        hasSideEffects = true;
    }


    public boolean hasSideEffects()
    {
        return !hasNoSideEffects && hasSideEffects;
    }


    /**
     * Specifies that the class contains package visible class members.
     */
    public void setContainsPackageVisibleMembers()
    {
        containsPackageVisibleMembers = true;
    }


    public boolean containsPackageVisibleMembers()
    {
        return containsPackageVisibleMembers;
    }


    /**
     * Specifies that code in the class accesses package visible class members.
     */
    public void setInvokesPackageVisibleMembers()
    {
        invokesPackageVisibleMembers = true;
    }


    public boolean invokesPackageVisibleMembers()
    {
        return invokesPackageVisibleMembers;
    }


    /**
     * Specifies that the class may be not merged with other classes.
     */
    public void setMayNotBeMerged()
    {
        mayBeMerged = false;
    }


    public boolean mayBeMerged()
    {
        return mayBeMerged;
    }


    /**
     * Specifies the class for which this class is a simple wrapper without any
     * additional functionality.
     */
    public void setWrappedClass(Clazz wrappedClass)
    {
        this.wrappedClass = wrappedClass;
    }


    public Clazz getWrappedClass()
    {
        return wrappedClass;
    }


    /**
     * Specifies the class into which this class can be merged.
     */
    public void setTargetClass(Clazz targetClass)
    {
        this.targetClass = targetClass;
    }


    public Clazz getTargetClass()
    {
        return targetClass;
    }


    public void setLambdaGroup(Clazz lambdaGroup)
    {
        this.lambdaGroup = lambdaGroup;
    }


    public Clazz getLambdaGroup()
    {
        return lambdaGroup;
    }


    /**
     * Merges in the given information of a class that is merged.
     */
    public void merge(ClassOptimizationInfo other)
    {
        this.isInstantiated                |= other.isInstantiated();
        this.isInstanceofed                |= other.isInstanceofed();
        this.isDotClassed                  |= other.isDotClassed();
        this.isCaught                      |= other.isCaught();
        this.isSimpleEnum                  |= other.isSimpleEnum();
        this.isEscaping                    |= other.isEscaping();
        this.hasSideEffects                |= other.hasSideEffects();
        this.containsPackageVisibleMembers |= other.containsPackageVisibleMembers();
        this.invokesPackageVisibleMembers  |= other.invokesPackageVisibleMembers();
        this.containsConstructors          |= other.containsConstructors();
    }


    /**
     * Creates and sets a ProgramClassOptimizationInfo instance on the given class.
     */
    public static void setProgramClassOptimizationInfo(Clazz clazz)
    {
        clazz.setProcessingInfo(new ProgramClassOptimizationInfo());
    }


    /**
     * Returns the ProgramClassOptimizationInfo instance from the given class.
     */
    public static ProgramClassOptimizationInfo getProgramClassOptimizationInfo(Clazz clazz)
    {
        return (ProgramClassOptimizationInfo)clazz.getProcessingInfo();
    }
}
