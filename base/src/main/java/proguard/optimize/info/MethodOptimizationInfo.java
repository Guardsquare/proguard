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
import proguard.classfile.util.MethodLinker;
import proguard.evaluation.value.Value;

/**
 * This class stores some optimization information that can be attached to
 * a method.
 *
 * @author Eric Lafortune
 */
public class MethodOptimizationInfo
{
    protected boolean hasNoSideEffects          = false;
    protected boolean hasNoExternalSideEffects  = false;
    protected boolean hasNoEscapingParameters   = false;
    protected boolean hasNoExternalReturnValues = false;
    protected Value   returnValue               = null;


    /**
     * Returns whether the method is kept.
     */
    // TODO: This information is now available from the processing flags.
    public boolean isKept()
    {
        return true;
    }


    /**
     * Specifies that the method has no side effects. Side effects include
     * changing static fields, changing instance fields, changing objects on
     * the heap, calling other methods with side effects, etc.
     */
    public void setNoSideEffects()
    {
        hasNoSideEffects         = true;
        hasNoExternalSideEffects = true;
        hasNoEscapingParameters  = true;
    }


    /**
     * Specifies that the method has no side effects. Side effects include
     * changing static fields, changing instance fields, changing objects on
     * the heap, calling other methods with side effects, etc.
     */
    public boolean hasNoSideEffects()
    {
        return hasNoSideEffects;
    }


    /**
     * Specifies that the method has no external side effects. External side
     * effects include changing static fields and generally changing objects
     * on the heap, but not changing fields of the instance on which the
     * method is called (or the class, in case of static methods), or any
     * instances that can only be reached through the instance. For example,
     * the append methods of StringBuilder have side effects, but no external
     * side effects.
     */
    public void setNoExternalSideEffects()
    {
        hasNoExternalSideEffects = true;
        hasNoEscapingParameters  = true;
    }


    /**
     * Retruns whether the method has no external side effects. External side
     * effects include changing static fields and generally changing objects
     * on the heap, but not changing fields of the instance on which the
     * method is called (or the class, in case of static methods), or any
     * instances that can only be reached through the instance. For example,
     * StringBuilder#append has side effects, but no external side effects.
     */
    public boolean hasNoExternalSideEffects()
    {
        return hasNoExternalSideEffects;
    }


    /**
     * Specifies that the method doesn't have escaping parameters. Escaping
     * parameters are reference parameters that the method has made reachable
     * on the heap after the method has exited. For example, System#setProperty
     * and Set#add let their parameters escape, because they become reachable.
     */
    public void setNoEscapingParameters()
    {
        hasNoEscapingParameters = true;
    }


    /**
     * Returns whether the method doesn't have escaping parameters. Escaping
     * parameters are reference parameters that the method has made reachable
     * on the heap after the method has exited. For example, System#setProperty
     * and Set#add let their parameters escape, because they become reachable.
     */
    public boolean hasNoEscapingParameters()
    {
        return hasNoEscapingParameters;
    }


    /**
     * Specifies that the method doesn't return external values. External
     * return values are reference values that originate from the heap, but
     * not parameters or new instances. For example, Map#get has external
     * return values, but StringBuilder#toString has no external return
     * values.
     */
    public void setNoExternalReturnValues()
    {
        hasNoExternalReturnValues = true;
    }


    /**
     * Returns whether the method returns external values. External return
     * values are reference values that originate from the heap, but not
     * parameters or new instances. For example, Map#get has external return
     * values, but StringBuilder#toString has no external return values.
     */
    public boolean hasNoExternalReturnValues()
    {
        return hasNoExternalReturnValues;
    }


    /**
     * Specifies the return value of the method.
     */
    public void setReturnValue(Value returnValue)
    {
        this.returnValue = returnValue;
    }


    /**
     * Returns a representation of the return value of the method, or null
     * if it is unknown.
     */
    public Value getReturnValue()
    {
        return returnValue;
    }


    // Methods that may be specialized.

    /**
     * Returns whether the method has side effects. Side effects include
     * changing static fields, changing instance fields, changing objects on
     * the heap, calling other methods with side effects, etc.
     */
    public boolean hasSideEffects()
    {
        return !hasNoSideEffects;
    }


    /**
     * Returns whether the method can be made private.
     */
    public boolean canBeMadePrivate()
    {
        return false;
    }


    /**
     * Returns whether the method body contains any exception handlers.
     */
    public boolean catchesExceptions()
    {
        return true;
    }


    /**
     * Returns whether the method body contains any backward branches.
     */
    public boolean branchesBackward()
    {
        return true;
    }


    /**
     * Returns whether the method body invokes any super methods.
     */
    public boolean invokesSuperMethods()
    {
        return true;
    }


    /**
     * Returns whether the method body invokes any methods with
     * 'invokedynamic'.
     */
    public boolean invokesDynamically()
    {
        return true;
    }


    /**
     * Returns whether the method body accesses any private fields or methods.
     */
    public boolean accessesPrivateCode()
    {
        return true;
    }


    /**
     * Returns whether the method body accesses any package visible fields or
     * methods.
     */
    public boolean accessesPackageCode()
    {
        return true;
    }


    /**
     * Returns whether the method body accesses any protected fields or methods.
     */
    public boolean accessesProtectedCode()
    {
        return true;
    }


    /**
     * Returns whether the method body contains any synchronization code
     * ('monitorenter' and 'monitorexit').
     */
    public boolean hasSynchronizedBlock()
    {
        return true;
    }


    /**
     * Returns whether the method body assigns any values to final fields.
     */
    public boolean assignsFinalField()
    {
        return true;
    }


    /**
     * Returns whether the method body contains `return` instructions that
     * leave a non-empty stack.
     */
    public boolean returnsWithNonEmptyStack()
    {
        return false;
    }


    /**
     * Returns the number of times the method is invoked in the known code
     * base.
     */
    public int getInvocationCount()
    {
        return Integer.MAX_VALUE;
    }


    /**
     * Returns the size that the parameters of the method take up on the stack.
     * The size takes into account long and double parameters taking up two
     * entries.
     */
    public int getParameterSize()
    {
        return 0;
    }


    /**
     * Returns whether the method has any unused parameters.
     */
    public boolean hasUnusedParameters()
    {
        return false;
    }


    /**
     * Returns whether the method actually uses the specified parameter.
     *
     * The variable index takes into account long and double parameters
     * taking up two entries.
     */
    public boolean isParameterUsed(int variableIndex)
    {
        return true;
    }


    /**
     * Returns a mask with the parameters that the method actually uses.
     *
     * The indices are variable indices of the variables. They take into
     * account long and double parameters taking up two entries.
     */
    public long getUsedParameters()
    {
        return -1L;
    }


    /**
     * Returns whether the specified reference parameter has already escaped
     * to the heap when entering the method.
     *
     * The parameter index is based on the method descriptor, including 'this',
     * with each parameter having the same size.
     */
    public boolean hasParameterEscaped(int parameterIndex)
    {
        return true;
    }


    /**
     * Returns a mask with the reference parameters have already escaped to
     * the heap when entering the method.
     *
     * The parameter index is based on the method descriptor, including 'this',
     * with each parameter having the same size.
     */
    public long getEscapedParameters()
    {
        return -1L;
    }


    /**
     * Returns whether the specified parameter escapes from the method.
     * An escaping parameter is a reference parameter that the method has made
     * reachable on the heap after the method has exited. For example,
     * System#setProperty and Set#add let their parameters escape, because
     * they become reachable.
     *
     * The parameter index is based on the method descriptor, with each
     * with each parameter having the same size.
     */
    public boolean isParameterEscaping(int parameterIndex)
    {
        return !hasNoEscapingParameters;
    }


    /**
     * Returns a mask with the parameters that escape from the method.
     * Escaping parameters are reference parameters that the method has made
     * reachable on the heap after the method has exited. For example,
     * System#setProperty and Set#add let their parameters escape, because
     * they become reachable.
     *
     * The parameter index is based on the method descriptor, including 'this',
     * with each parameter having the same size.
     */
    public long getEscapingParameters()
    {
        return hasNoEscapingParameters ? 0L : -1L;
    }


    /**
     * Returns whether the contents of the specified reference parameter are
     * modified in the method.
     *
     * The parameter index is based on the method descriptor, with each
     * with each parameter having the same size.
     */
    public boolean isParameterModified(int parameterIndex)
    {
        // TODO: Refine for static methods.
        return
            !hasNoSideEffects &&
            (!hasNoExternalSideEffects || parameterIndex == 0);
    }


    /**
     * Returns a mask of the reference parameters whose contents are modified
     * in the method.
     *
     * The parameter index is based on the method descriptor, including 'this',
     * with each parameter having the same size.
     */
    public long getModifiedParameters()
    {
        // TODO: Refine for static methods.
        return
            hasNoSideEffects         ? 0L :
            hasNoExternalSideEffects ? 1L :
                                       -1L;
    }


    /**
     * Returns whether the method might modify objects that it can reach
     * through static fields or instance fields.
     */
    public boolean modifiesAnything()
    {
        return !hasNoExternalSideEffects;
    }


    /**
     * Returns a representation of the specified method parameter, or null
     * if it is unknown.
     *
     * The parameter index is based on the method descriptor, with each
     * with each parameter having the same size.
     */
    public Value getParameterValue(int parameterIndex)
    {
        return null;
    }


    /**
     * Returns whether the method might return the specified reference
     * parameter as its result.
     *
     * The parameter index is based on the method descriptor, with each
     * with each parameter having the same size.
     */
    public boolean returnsParameter(int parameterIndex)
    {
        return true;
    }


    /**
     * Returns a mask of the reference parameters that the method might return.
     *
     * The parameter index is based on the method descriptor, including 'this',
     * with each parameter having the same size.
     */
    public long getReturnedParameters()
    {
        return -1L;
    }


    /**
     * Returns whether the method might create and return new instances.
     */
    public boolean returnsNewInstances()
    {
        return true;
    }


    /**
     * Returns whether the method might return external values. External
     * return values are reference values that originate from the heap, but
     * not parameters or new instances. For example, Map#get has external
     * return values, but StringBuilder#toString has no external return
     * values.
     */
    public boolean returnsExternalValues()
    {
        return !hasNoExternalReturnValues;
    }


    /**
     * Creates and sets a MethodOptimizationInfo instance on the specified
     * chain of linked methods.
     */
    public static void setMethodOptimizationInfo(Clazz clazz, Method method)
    {
        MethodLinker.lastMember(method).setProcessingInfo(new MethodOptimizationInfo());
    }


    /**
     * Returns the MethodOptimizationInfo instance from the specified chain of
     * linked methods.
     */
    public static MethodOptimizationInfo getMethodOptimizationInfo(Method method)
    {
        return (MethodOptimizationInfo)MethodLinker.lastMember(method).getProcessingInfo();
    }
}
