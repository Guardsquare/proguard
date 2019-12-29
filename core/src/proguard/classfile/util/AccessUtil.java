/*
 * ProGuard Core -- library to process Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proguard.classfile.util;

import proguard.classfile.*;


/**
 * Utility methods for working with access flags. For convenience, this class
 * defines access levels, in ascending order: <code>PRIVATE</code>,
 * <code>PACKAGE_VISIBLE</code>, <code>PROTECTED</code>, and <code>PUBLIC</code>.
 *
 * @author Eric Lafortune
 */
public class AccessUtil
{
    public static final int PRIVATE         = 0;
    public static final int PACKAGE_VISIBLE = 1;
    public static final int PROTECTED       = 2;
    public static final int PUBLIC          = 3;


    // The mask of access flags.
    private static final int ACCESS_MASK =
        AccessConstants.PUBLIC  |
        AccessConstants.PRIVATE |
        AccessConstants.PROTECTED;


    /**
     * Returns the corresponding access level of the given access flags.
     * @param accessFlags the internal access flags as a logical bit mask of
     *                    {@link AccessConstants#PRIVATE},
     *                    {@link AccessConstants#PROTECTED}, and
     *                    {@link AccessConstants#PUBLIC}.
     * @return the corresponding access level:
     *         {@link #PRIVATE},
     *         {@link #PACKAGE_VISIBLE},
     *         {@link #PROTECTED}, or
     *         {@link #PUBLIC}.
     */
    public static int accessLevel(int accessFlags)
    {
        switch (accessFlags & ACCESS_MASK)
        {
            case AccessConstants.PRIVATE:   return PRIVATE;
            default:                        return PACKAGE_VISIBLE;
            case AccessConstants.PROTECTED: return PROTECTED;
            case AccessConstants.PUBLIC:    return PUBLIC;
        }
    }


    /**
     * Returns the corresponding access flags of the given access level.
     * @param accessLevel the access level:
     *                    {@link #PRIVATE},
     *                    {@link #PACKAGE_VISIBLE},
     *                    {@link #PROTECTED}, or
     *                    {@link #PUBLIC}.
     * @return the corresponding internal access flags as a logical bit mask of
     *         {@link AccessConstants#PRIVATE},
     *         {@link AccessConstants#PROTECTED}, and
     *         {@link AccessConstants#PUBLIC}.
     */
    public static int accessFlags(int accessLevel)
    {
        switch (accessLevel)
        {
            case PRIVATE:   return AccessConstants.PRIVATE;
            default:        return 0;
            case PROTECTED: return AccessConstants.PROTECTED;
            case PUBLIC:    return AccessConstants.PUBLIC;
        }
    }


    /**
     * Replaces the access part of the given access flags.
     * @param accessFlags    the internal access flags as a logical bit mask of
     *                       {@link AccessConstants#PRIVATE},
     *                       {@link AccessConstants#PROTECTED}, and
     *                       {@link AccessConstants#PUBLIC}.
     * @param newAccessFlags the new access flags as a logical bit mask of
     *                       {@link AccessConstants#PRIVATE},
     *                       {@link AccessConstants#PROTECTED}, and
     *                       {@link AccessConstants#PUBLIC}.
     */
    public static int replaceAccessFlags(int accessFlags, int newAccessFlags)
    {
        // A private class member should not be explicitly final.
        if (newAccessFlags == AccessConstants.PRIVATE)
        {
            accessFlags &= ~AccessConstants.FINAL;
        }

        return (accessFlags    & ~ACCESS_MASK) |
               (newAccessFlags &  ACCESS_MASK);
    }


    /**
     * Returns whether the given access flags match the required set and unset
     * access flags.
     * @param accessFlags              the internal access flags as a logical
     *                                 bit mask of
     *                                 {@link AccessConstants#PRIVATE},
     *                                 {@link AccessConstants#PROTECTED}, and
     *                                 {@link AccessConstants#PUBLIC}.
     * @param requiredSetAccessFlags   the internal access flags that should be
     *                                 set, as a logical bit mask of
     *                                 {@link AccessConstants#PRIVATE},
     *                                 {@link AccessConstants#PROTECTED}, and
     *                                 {@link AccessConstants#PUBLIC}.
     * @param requiredUnsetAccessFlags the internal access flags that should be
     *                                 unset, as a logical bit mask of
     *                                 {@link AccessConstants#PRIVATE},
     *                                 {@link AccessConstants#PROTECTED}, and
     *                                 {@link AccessConstants#PUBLIC}.
     */
    public static boolean accepted(int accessFlags,
                                   int requiredSetAccessFlags,
                                   int requiredUnsetAccessFlags)
    {
        int requiredCombinedSetAccessFlags = requiredSetAccessFlags & ~ACCESS_MASK;
        int requiredOneSetAccessFlags      = requiredSetAccessFlags &  ACCESS_MASK;

        return (requiredCombinedSetAccessFlags & ~accessFlags) == 0 &&
               (requiredUnsetAccessFlags       &  accessFlags) == 0 &&
               (requiredOneSetAccessFlags == 0 || (requiredOneSetAccessFlags & accessFlags) != 0);
    }
}
