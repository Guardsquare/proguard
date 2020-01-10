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
 * An {@link InternalTypeEnumeration} provides an enumeration of all
 * types listed in a given internal descriptor or signature of a class, a
 * method, or a field.
 * <p/>
 * The leading formal type parameters, if any, can be retrieved separately.
 * <p/>
 * The return type of a method descriptor can also be retrieved separately.
 *
 * @author Eric Lafortune
 */
public class InternalTypeEnumeration
{
    private final String descriptor;
    private final int    formalTypeParametersIndex;
    private final int    openIndex;
    private final int    closeIndex;

    private int index;


    /**
     * Creates a new InternalTypeEnumeration for the given method descriptor.
     */
    public InternalTypeEnumeration(String descriptor)
    {
        this.descriptor = descriptor;

        // Find any formal type parameters.
        int formalTypeParametersIndex = 0;
        if (descriptor.charAt(0) == TypeConstants.GENERIC_START)
        {
            formalTypeParametersIndex = 1;

            int nestingLevel = 1;
            do
            {
                char c = descriptor.charAt(formalTypeParametersIndex++);
                switch (c)
                {
                    case TypeConstants.GENERIC_START:
                    {
                        nestingLevel++;
                        break;
                    }
                    case TypeConstants.GENERIC_END:
                    {
                        nestingLevel--;
                        break;
                    }
                }
            }
            while (nestingLevel > 0);
        }

        this.formalTypeParametersIndex = formalTypeParametersIndex;;

        this.openIndex  = descriptor.indexOf(TypeConstants.METHOD_ARGUMENTS_OPEN,
                                             formalTypeParametersIndex);

        this.closeIndex = openIndex >= 0 ?
            descriptor.indexOf(TypeConstants.METHOD_ARGUMENTS_CLOSE, openIndex) :
            descriptor.length();

        reset();
    }


    /**
     * Returns the number of types contained in the descriptor. This
     * is the number of types that the enumeration will return.
     */
    public int typeCount()
    {
        reset();

        int count = 0;

        while (hasMoreTypes())
        {
            nextType();

            count++;
        }

        reset();

        return count;
    }


    /**
     * Returns the total size of the types contained in the descriptor.
     * This accounts for long and double parameters taking up two entries.
     */
    public int typesSize()
    {
        reset();

        int size = 0;

        while (hasMoreTypes())
        {
            String type = nextType();

            size += ClassUtil.internalTypeSize(type);
        }

        reset();

        return size;
    }


    /**
     * Resets the enumeration.
     */
    private void reset()
    {
        this.index = openIndex >= 0 ?
            openIndex + 1 :
            formalTypeParametersIndex;
    }


    /**
     * Returns whether the descriptor has leading formal type parameters.
     */
    public boolean hasFormalTypeParameters()
    {
        return formalTypeParametersIndex > 0;
    }


    /**
     * Returns the leading formal type parameters from the descriptor.
     */
    public String formalTypeParameters()
    {
        return descriptor.substring(0, formalTypeParametersIndex);
    }


    /**
     * Returns whether the descriptor is a method signature.
     */
    public boolean isMethodSignature()
    {
        return openIndex >= 0;
    }


    /**
     * Returns whether the enumeration can provide more types from the method
     * descriptor.
     */
    public boolean hasMoreTypes()
    {
        return index < closeIndex;
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
            case TypeConstants.CLASS_START:
            case TypeConstants.GENERIC_VARIABLE_START:
            {
                skipClass();
                break;
            }
            case TypeConstants.GENERIC_START:
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
        return descriptor.substring(closeIndex + 1);
    }


    // Small utility methods.

    private void skipArray()
    {
        while (descriptor.charAt(index) == TypeConstants.ARRAY)
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
                case TypeConstants.GENERIC_START:
                    skipGeneric();
                    break;

                case TypeConstants.CLASS_END:
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
                case TypeConstants.GENERIC_START:
                    nestingLevel++;
                    break;

                case TypeConstants.GENERIC_END:
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

                if (enumeration.hasFormalTypeParameters())
                {
                    System.out.println("  Formal type parameters ["+enumeration.formalTypeParameters()+"]");
                }

                while (enumeration.hasMoreTypes())
                {
                    System.out.println("  Type ["+enumeration.nextType()+"]");
                }

                if (enumeration.isMethodSignature())
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
