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

import proguard.classfile.*;
import proguard.classfile.util.*;
import proguard.evaluation.value.Value;
import proguard.util.ArrayUtil;

/**
 * This class stores some optimization information that can be attached to
 * a method that can be analyzed in detail.
 *
 * @author Eric Lafortune
 */
public class ProgramMethodOptimizationInfo
extends      MethodOptimizationInfo
{
    private static final Value[] EMPTY_PARAMETERS      = new Value[0];
    private static final int[]   EMPTY_PARAMETER_SIZES = new int[0];


    private volatile boolean hasSideEffects           = false;
    private volatile boolean canBeMadePrivate         = true;
    private volatile boolean catchesExceptions        = false;
    private volatile boolean branchesBackward         = false;
    private volatile boolean invokesSuperMethods      = false;
    private volatile boolean invokesDynamically       = false;
    private volatile boolean accessesPrivateCode      = false;
    private volatile boolean accessesPackageCode      = false;
    private volatile boolean accessesProtectedCode    = false;
    private volatile boolean hasSynchronizedBlock     = false;
    private volatile boolean assignsFinalField        = false;
    private volatile boolean returnsWithNonEmptyStack = false;
    private volatile int     invocationCount          = 0;
    private volatile int     parameterSize            = 0;
    private volatile long    usedParameters           = 0L;
    private volatile long    escapedParameters        = 0L;
    private volatile long    escapingParameters       = 0L;
    private volatile long    modifiedParameters       = 0L;
    private volatile boolean modifiesAnything         = false;
    private volatile Value[] parameters;
    private volatile int[]   parameterSizes;
    private volatile long    returnedParameters       = 0L;
    private volatile boolean returnsNewInstances      = false;
    private volatile boolean returnsExternalValues    = false;


    /**
     * Creates a new MethodOptimizationInfo for the given method.
     */
    public ProgramMethodOptimizationInfo(Clazz clazz, Method method)
    {
        // Set up an array of the right size for storing information about the
        // passed parameters (including 'this', for non-static methods) and
        // their respective stack sizes.
        String desc      = method.getDescriptor(clazz);
        boolean isStatic = (method.getAccessFlags() & AccessConstants.STATIC) != 0;

        int parameterCount =
            ClassUtil.internalMethodParameterCount(desc, isStatic);

        parameters = parameterCount == 0 ?
            EMPTY_PARAMETERS :
            new Value[parameterCount];

        parameterSizes = parameterCount == 0 ?
            EMPTY_PARAMETER_SIZES :
            new int[parameterCount];

        InternalTypeEnumeration typeEnumeration =
            new InternalTypeEnumeration(desc);

        int idx = isStatic ? 0 : 1;

        // Pre-initialize the size of the first parameter
        // if the method is non-static.
        if (parameterSizes.length > 0 && !isStatic)
        {
            parameterSizes[0] = 1;
        }

        while (typeEnumeration.hasMoreTypes())
        {
            String internalType   = typeEnumeration.nextType();
            parameterSizes[idx++] = ClassUtil.internalTypeSize(internalType);
        }
    }


    // Overridden methods for MethodOptimizationInfo, plus more setters.

    public boolean isKept()
    {
        return false;
    }


    /**
     * Specifies that the method has side effects. Side effects include
     * changing static fields, changing instance fields, changing objects on
     * the heap, calling other methods with side effects, etc.
     *
     * The method {@link #setNoSideEffects()} gets precedence.
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
     * Specifies that the method can't be made private.
     */
    public void setCanNotBeMadePrivate()
    {
        canBeMadePrivate = false;
    }


    public boolean canBeMadePrivate()
    {
        return canBeMadePrivate;
    }


    /**
     * Specifies that the method body contains exception handlers.
     */
    public void setCatchesExceptions()
    {
        catchesExceptions = true;
    }


    public boolean catchesExceptions()
    {
        return catchesExceptions;
    }


    /**
     * Specifies that the method body contains backward branches.
     */
    public void setBranchesBackward()
    {
        branchesBackward = true;
    }


    public boolean branchesBackward()
    {
        return branchesBackward;
    }


    /**
     * Specifies that the method body invokes super methods.
     */
    public void setInvokesSuperMethods()
    {
        invokesSuperMethods = true;
    }


    public boolean invokesSuperMethods()
    {
        return invokesSuperMethods;
    }


    /**
     * Specifies that the method body invokes methods with
     * 'invokedynamic'.
     */
    public void setInvokesDynamically()
    {
        invokesDynamically = true;
    }


    public boolean invokesDynamically()
    {
        return invokesDynamically;
    }


    /**
     * Specifies that the method body accesses private fields or methods.
     */
    public void setAccessesPrivateCode()
    {
        accessesPrivateCode = true;
    }


    public boolean accessesPrivateCode()
    {
        return accessesPrivateCode;
    }


    /**
     * Specifies that the method body accesses package visible fields or
     * methods.
     */
    public void setAccessesPackageCode()
    {
        accessesPackageCode = true;
    }


    public boolean accessesPackageCode()
    {
        return accessesPackageCode;
    }


    /**
     * Specifies that the method body accesses protected fields or methods.
     */
    public void setAccessesProtectedCode()
    {
        accessesProtectedCode = true;
    }


    public boolean accessesProtectedCode()
    {
        return accessesProtectedCode;
    }


    /**
     * Specifies that the method body contains synchronization code
     * ('monitorenter' and 'monitorexit').
     */
    public void setHasSynchronizedBlock()
    {
        hasSynchronizedBlock = true;
    }


    public boolean hasSynchronizedBlock()
    {
        return hasSynchronizedBlock;
    }


    /**
     * Specifies that the method body assigns values to final fields.
     */
    public void setAssignsFinalField()
    {
        assignsFinalField = true;
    }


    public boolean assignsFinalField()
    {
        return assignsFinalField;
    }


    /**
     * Specifies that the method body contains `return` instructions that
     * leave a non-empty stack.
     */
    public void setReturnsWithNonEmptyStack()
    {
        returnsWithNonEmptyStack = true;
    }


    public boolean returnsWithNonEmptyStack()
    {
        return returnsWithNonEmptyStack;
    }


    /**
     * Increments the counter for number of times the method is invoked in the
     * known code base.
     */
    public void incrementInvocationCount()
    {
        invocationCount++;
    }


    public int getInvocationCount()
    {
        return invocationCount;
    }


    /**
     * Specifies the size that the parameters of the method take up on the stack.
     * The size takes into account long and double parameters taking up two
     * entries.
     */
    public synchronized void setParameterSize(int parameterSize)
    {
        this.parameterSize = parameterSize;
    }


    public int getParameterSize()
    {
        return parameterSize;
    }


    /**
     * Returns the stack size of the parameter at the given index.
     */
    public int getParameterSize(int parameterIndex)
    {
        return parameterSizes[parameterIndex];
    }


    /**
     * Specifies that the method actually uses the specified parameter.
     *
     * The variable index takes into account long and double parameters
     * taking up two entries.
     */
    public synchronized void setParameterUsed(int variableIndex)
    {
        usedParameters = setBit(usedParameters, variableIndex);
    }


    /**
     * Specifies a mask with the parameters that the method actually uses.
     *
     * The indices are variable indices of the variables. They take into
     * account long and double parameters taking up two entries.
     */
    public synchronized void updateUsedParameters(long usedParameters)
    {
        this.usedParameters |= usedParameters;
    }


    public boolean hasUnusedParameters()
    {
        return parameterSize < 64 ?
            (usedParameters | -1L << parameterSize) != -1L :
            usedParameters                         != -1L ;
    }


    public boolean isParameterUsed(int variableIndex)
    {
        return isBitSet(usedParameters, variableIndex);
    }


    public long getUsedParameters()
    {
        return usedParameters;
    }


    /**
     * Notifies this object that a parameter is inserted at the given
     * index with the given stack size.
     * @param parameterIndex the parameter index,
     *                       not taking into account the entry size,
     *                       but taking into account the 'this' parameter,
     *                       if any.
     * @param stackSize      the stack size that is occupied by the inserted
     *                       parameter.
     */
    public synchronized void insertParameter(int parameterIndex, int stackSize)
    {
        // The used parameter bits are indexed with their variable indices
        // (which take into account the sizes of the entries).
        //usedParameters   = insertBit(usedParameters,     parameterIndex, 1L);
        //parameterSize++;

        escapedParameters  = insertBit(escapedParameters,  parameterIndex, 1L);
        escapingParameters = insertBit(escapingParameters, parameterIndex, 1L);
        modifiedParameters = insertBit(modifiedParameters, parameterIndex, 1L);
        returnedParameters = insertBit(returnedParameters, parameterIndex, 1L);
        parameters         = ArrayUtil.insert(parameters,     parameters.length,     parameterIndex, null);
        parameterSizes     = ArrayUtil.insert(parameterSizes, parameterSizes.length, parameterIndex, stackSize);
    }


    /**
     * Notifies this object that the specified parameter is removed.
     * @param parameterIndex the parameter index,
     *                       not taking into account the entry size,
     *                       but taking into account the 'this' parameter,
     *                       if any.
     */
    public synchronized void removeParameter(int parameterIndex)
    {
        // The used parameter bits are indexed with their variable indices
        // (which take into account the sizes of the entries).
        //usedParameters   = removeBit(usedParameters,     parameterIndex, 1L);
        //parameterSize--;

        escapedParameters  = removeBit(escapedParameters,  parameterIndex, 1L);
        escapingParameters = removeBit(escapingParameters, parameterIndex, 1L);
        modifiedParameters = removeBit(modifiedParameters, parameterIndex, 1L);
        returnedParameters = removeBit(returnedParameters, parameterIndex, 1L);
        ArrayUtil.remove(parameters,     parameters.length,     parameterIndex);
        ArrayUtil.remove(parameterSizes, parameterSizes.length, parameterIndex);
    }


    /**
     * Specifies that the specified reference parameter has already escaped
     * to the heap when entering the method.
     *
     * The parameter index is based on the method descriptor, including 'this',
     * with each parameter having the same size.
     */
    public synchronized void setParameterEscaped(int parameterIndex)
    {
        escapedParameters = setBit(escapedParameters, parameterIndex);
    }


    /**
     * Specifies a mask with the reference parameters have already escaped to
     * the heap when entering the method.
     *
     * The parameter indices are based on the method descriptor, with each
     * with each parameter having the same size.
     */
    public synchronized void updateEscapedParameters(long escapedParameters)
    {
        this.escapedParameters |= escapedParameters;
    }


    public boolean hasParameterEscaped(int parameterIndex)
    {
        return isBitSet(escapedParameters, parameterIndex);
    }


    public long getEscapedParameters()
    {
        return escapedParameters;
    }


    /**
     * Specifies that the specified parameter escapes from the method.
     * An escaping parameter is a reference parameter that the method has made
     * reachable on the heap after the method has exited. For example,
     * System#setProperty and Set#add let their parameters escape, because
     * they become reachable.
     *
     * The parameter index is based on the method descriptor, including 'this',
     * with each parameter having the same size.
     */
    public synchronized void setParameterEscaping(int parameterIndex)
    {
        escapingParameters = setBit(escapingParameters, parameterIndex);
    }


    /**
     * Specifies a mask with the parameters that escape from the method.
     * Escaping parameters are reference parameters that the method has made
     * reachable on the heap after the method has exited. For example,
     * System#setProperty and Set#add let their parameters escape, because
     * they become reachable.
     *
     * The parameter indices are based on the method descriptor, with each
     * with each parameter having the same size.
     */
    public synchronized void updateEscapingParameters(long escapingParameters)
    {
        this.escapingParameters |= escapingParameters;
    }


    public boolean isParameterEscaping(int parameterIndex)
    {
        return
            !hasNoEscapingParameters &&
            (isBitSet(escapingParameters, parameterIndex));
    }


    public long getEscapingParameters()
    {
        return hasNoEscapingParameters ? 0L : escapingParameters;
    }


    /**
     * Specifies that the contents of the specified reference parameter are
     * modified in the method.
     *
     * The parameter index is based on the method descriptor, including 'this',
     * with each parameter having the same size.
     *
     * The methods {@link #setNoSideEffects()} and
     * {@link #setNoExternalSideEffects()} get precedence.
     */
    public synchronized void setParameterModified(int parameterIndex)
    {
        modifiedParameters = setBit(modifiedParameters, parameterIndex);
    }


    /**
     * Specifies a mask of the reference parameters whose contents are modified
     * in the method.
     *
     * The parameter indices are based on the method descriptor, with each
     * with each parameter having the same size.
     *
     * The methods {@link #setNoSideEffects()} and
     * {@link #setNoExternalSideEffects()} get precedence.
     */
    public synchronized void updateModifiedParameters(long modifiedParameters)
    {
        this.modifiedParameters |= modifiedParameters;
    }


    public boolean isParameterModified(int parameterIndex)
    {
        // TODO: Refine for static methods.
        return
            !hasNoSideEffects &&
            (!hasNoExternalSideEffects || parameterIndex == 0) &&
            (isBitSet((modifiesAnything ?
                           modifiedParameters | escapedParameters :
                           modifiedParameters), parameterIndex));
    }


    public long getModifiedParameters()
    {
        // TODO: Refine for static methods.
        return
            hasNoSideEffects         ? 0L :
                hasNoExternalSideEffects ? modifiedParameters & 1L :
                    modifiedParameters;
    }


    /**
     * Specifies that the method might modify objects that it can reach
     * through static fields or instance fields.
     *
     * The method {@link #setNoExternalSideEffects()} gets precedence.
     */
    public void setModifiesAnything()
    {
        modifiesAnything = true;
    }


    public boolean modifiesAnything()
    {
        return !hasNoExternalSideEffects && modifiesAnything;
    }


    /**
     * Specifies a possible representation of the specified method parameter.
     *
     * The parameter index is based on the method descriptor, including 'this',
     * with each parameter having the same size.
     */
    public synchronized void generalizeParameterValue(int parameterIndex, Value parameter)
    {
        parameters[parameterIndex] = parameters[parameterIndex] != null ?
            parameters[parameterIndex].generalize(parameter) :
            parameter;
    }


    public Value getParameterValue(int parameterIndex)
    {
        return parameters != null ?
            parameters[parameterIndex] :
            null;
    }


    /**
     * Specifies that the method might return the specified reference parameter
     * as its result.
     *
     * The parameter index is based on the method descriptor, including 'this',
     * with each parameter having the same size.
     */
    public synchronized void setParameterReturned(int parameterIndex)
    {
        returnedParameters = setBit(returnedParameters, parameterIndex);
    }


    /**
     * Specifies a mask of the reference parameters that the method might return.
     *
     * The parameter indices are based on the method descriptor, with each
     * with each parameter having the same size.
     */
    public synchronized void updateReturnedParameters(long returnedParameters)
    {
        this.returnedParameters |= returnedParameters;
    }


    public boolean returnsParameter(int parameterIndex)
    {
        return isBitSet(returnedParameters, parameterIndex);
    }


    public long getReturnedParameters()
    {
        return returnedParameters;
    }


    /**
     * Specifies that the method might create and return new instances.
     */
    public void setReturnsNewInstances()
    {
        returnsNewInstances = true;
    }


    public boolean returnsNewInstances()
    {
        return returnsNewInstances;
    }


    /**
     * Specifies that the method might return external values. External
     * return values are reference values that originate from the heap, but
     * not parameters or new instances. For example, Map#get has external
     * return values, but StringBuilder#toString has no external return
     * values.
     *
     * The method {@link #setNoExternalReturnValues()} gets precedence.
     */
    public void setReturnsExternalValues()
    {
        returnsExternalValues = true;
    }


    public boolean returnsExternalValues()
    {
        return
            !hasNoExternalReturnValues &&
            returnsExternalValues;
    }


    /**
     * Specifies a representation of the value that the method returns, or null
     * if it is unknown.
     */
    public synchronized void generalizeReturnValue(Value returnValue)
    {
        this.returnValue = this.returnValue != null ?
            this.returnValue.generalize(returnValue) :
            returnValue;
    }


    /**
     * Merges in the given information of a method that is inlined.
     */
    public synchronized void merge(MethodOptimizationInfo other)
    {
        this.catchesExceptions     |= other.catchesExceptions();
        this.branchesBackward      |= other.branchesBackward();
        this.invokesSuperMethods   |= other.invokesSuperMethods();
        this.invokesDynamically    |= other.invokesDynamically();
        this.accessesPrivateCode   |= other.accessesPrivateCode();
        this.accessesPackageCode   |= other.accessesPackageCode();
        this.accessesProtectedCode |= other.accessesProtectedCode();
        this.hasSynchronizedBlock  |= other.hasSynchronizedBlock();
        this.assignsFinalField     |= other.assignsFinalField();

        // Some of these should actually be recomputed, since these are
        // relative to the method:
        //     this.invokesSuperMethods
        //     this.accessesPrivateCode
        //     this.accessesPackageCode
        //     this.accessesProtectedCode
    }


    /**
     * Creates and sets a ProgramMethodOptimizationInfo instance on the
     * specified chain of linked methods.
     */
    public static void setProgramMethodOptimizationInfo(Clazz clazz, Method method)
    {
        MethodLinker.lastMember(method).setProcessingInfo(new ProgramMethodOptimizationInfo(clazz, method));
    }


    /**
     * Returns the ProgramMethodOptimizationInfo instance from the specified
     * chain of linked methods.
     */
    public static ProgramMethodOptimizationInfo getProgramMethodOptimizationInfo(Method method)
    {
        return (ProgramMethodOptimizationInfo)MethodLinker.lastMember(method).getProcessingInfo();
    }


    // Small utility methods.

    /**
     * Returns the given value with the specified bit set.
     */
    private long setBit(long bits, int index)
    {
        return index < 64 ?
            bits | (1L << index) :
            bits;
    }


    /**
     * Specifies that the specified bit is set in the given value
     * (or if the index exceeds the size of the long).
     */
    private boolean isBitSet(long bits, int index)
    {
        return index >= 64 || (bits & (1L << index)) != 0;
    }


    /**
     * Returns the given value with a given bit inserted at the given index.
     */
    private long insertBit(long value, int bitIndex, long bitValue)
    {
        long higherMask = -1L << bitIndex;
        long lowerMask  = ~higherMask;

        return ((value & higherMask) << 1) |
               ( value & lowerMask       ) |
               (bitValue << bitIndex);
    }


    /**
     * Returns the given value with a bit removed at the given index.
     * The given given bit value is shifted in as the new most significant bit.
     */
    private long removeBit(long value, int bitIndex, long highBitValue)
    {
        long higherMask = -1L << bitIndex;
        long lowerMask  = ~higherMask;

        return ((value & (higherMask<<1)) >>> 1) |
               ( value & lowerMask             ) |
               (highBitValue << 63);
    }
}
