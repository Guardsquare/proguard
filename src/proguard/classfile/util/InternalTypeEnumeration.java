/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2013 Eric Lafortune (eric@graphics.cornell.edu)
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
package proguard.classfile.util;

import proguard.classfile.ClassConstants;


/**
 * An <code>InternalTypeEnumeration</code> provides an enumeration of all
 * parameter types listed in a given internal method descriptor or signature.
 * The signature can also be a class signature. The return type of a method
 * descriptor can be retrieved separately.
 *
 * @author Eric Lafortune
 */
public class InternalTypeEnumeration
{
    private String descriptor;
    private int    firstIndex;
    private int    lastIndex;
    private int    index;


    /**
     * Creates a new InternalTypeEnumeration for the given method descriptor.
     */
    public InternalTypeEnumeration(String descriptor)
    {
        this.descriptor = descriptor;
        this.firstIndex = descriptor.indexOf(ClassConstants.INTERNAL_METHOD_ARGUMENTS_OPEN);
        this.lastIndex  = descriptor.indexOf(ClassConstants.INTERNAL_METHOD_ARGUMENTS_CLOSE);
        this.index      = firstIndex + 1;

        if (lastIndex < 0)
        {
            lastIndex = descriptor.length();
        }
    }


    /**
     * Returns the formal type parameters from the descriptor, assuming it's a
     * method descriptor.
     */
    public String formalTypeParameters()
    {
        return descriptor.substring(0, firstIndex);
    }


    /**
     * Returns whether the enumeration can provide more types from the method
     * descriptor.
     */
    public boolean hasMoreTypes()
    {
        return index < lastIndex;
    }


    /**
     * Returns the next type from the method descriptor.
     */
    public String nextType()
    {
        int startIndex = index;

        skipArray();

        char c = descriptor.charAt(index++);
        switch (c)
        {
            case ClassConstants.INTERNAL_TYPE_CLASS_START:
            case ClassConstants.INTERNAL_TYPE_GENERIC_VARIABLE_START:
            {
                skipClass();
                break;
            }
            case ClassConstants.INTERNAL_TYPE_GENERIC_START:
            {
                skipGeneric();
                break;
            }
        }

        return descriptor.substring(startIndex, index);
    }


    /**
     * Returns the return type from the descriptor, assuming it's a method
     * descriptor.
     */
    public String returnType()
    {
        return descriptor.substring(lastIndex + 1);
    }


    // Small utility methods.

    private void skipArray()
    {
        while (descriptor.charAt(index) == ClassConstants.INTERNAL_TYPE_ARRAY)
        {
            index++;
        }
    }


    private void skipClass()
    {
        while (true)
        {
            char c = descriptor.charAt(index++);
            switch (c)
            {
                case ClassConstants.INTERNAL_TYPE_GENERIC_START:
                    skipGeneric();
                    break;

                case ClassConstants.INTERNAL_TYPE_CLASS_END:
                    return;
            }
        }
    }


    private void skipGeneric()
    {
        int nestingLevel = 1;

        do
        {
            char c = descriptor.charAt(index++);
            switch (c)
            {
                case ClassConstants.INTERNAL_TYPE_GENERIC_START:
                    nestingLevel++;
                    break;

                case ClassConstants.INTERNAL_TYPE_GENERIC_END:
                    nestingLevel--;
                    break;
            }
        }
        while (nestingLevel > 0);
    }


    /**
     * A main method for testing the type enumeration.
     */
    public static void main(String[] args)
    {
        try
        {
            for (int index = 0; index < args.length; index++)
            {
                String descriptor = args[index];

                System.out.println("Descriptor ["+descriptor+"]");
                InternalTypeEnumeration enumeration = new InternalTypeEnumeration(descriptor);

                if (enumeration.firstIndex >= 0)
                {
                    System.out.println("  Formal type parameters ["+enumeration.formalTypeParameters()+"]");
                }

                while (enumeration.hasMoreTypes())
                {
                    System.out.println("  Type ["+enumeration.nextType()+"]");
                }

                if (enumeration.lastIndex < descriptor.length())
                {
                    System.out.println("  Return type ["+enumeration.returnType()+"]");
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
