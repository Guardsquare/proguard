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

import java.util.Stack;

/**
 * A <code>DescriptorClassEnumeration</code> provides an enumeration of all
 * classes mentioned in a given descriptor or signature.
 *
 * @author Eric Lafortune
 */
public class DescriptorClassEnumeration
{
    private String  descriptor;

    private int     index;
    private int     nestingLevel;
    private boolean isInnerClassName;
    private String  accumulatedClassName;
    private Stack   accumulatedClassNames;


    /**
     * Creates a new DescriptorClassEnumeration for the given descriptor.
     */
    public DescriptorClassEnumeration(String descriptor)
    {
        this.descriptor = descriptor;
    }


    /**
     * Returns the number of classes contained in the descriptor. This
     * is the number of class names that the enumeration will return.
     */
    public int classCount()
    {
        int count = 0;

        reset();

        nextFluff();
        while (hasMoreClassNames())
        {
            count++;

            nextClassName();
            nextFluff();
        }

        reset();

        return count;
    }


    /**
     * Resets the enumeration.
     */
    private void reset()
    {
        index                 = 0;
        nestingLevel          = 0;
        isInnerClassName      = false;
        accumulatedClassName  = null;
        accumulatedClassNames = null;
    }


    /**
     * Returns whether the enumeration can provide more class names from the
     * descriptor.
     */
    public boolean hasMoreClassNames()
    {
        return index < descriptor.length();
    }


    /**
     * Returns the next fluff (surrounding class names) from the descriptor.
     */
    public String nextFluff()
    {
        int fluffStartIndex = index;

        // Find the first token marking the start of a class name 'L' or '.'.
        loop: while (index < descriptor.length())
        {
            switch (descriptor.charAt(index++))
            {
                case TypeConstants.GENERIC_START:
                {
                    nestingLevel++;

                    // Make sure we have a stack.
                    if (accumulatedClassNames == null)
                    {
                        accumulatedClassNames = new Stack();
                    }

                    // Remember the accumulated class name.
                    accumulatedClassNames.push(accumulatedClassName);

                    break;
                }
                case TypeConstants.GENERIC_END:
                {
                    nestingLevel--;

                    // Return to the accumulated class name outside the
                    // generic block.
                    accumulatedClassName = (String)accumulatedClassNames.pop();

                    continue loop;
                }
                case TypeConstants.GENERIC_BOUND:
                case TypeConstants.ARRAY:
                {
                    continue loop;
                }
                case TypeConstants.CLASS_START:
                {
                    // We've found the start of an ordinary class name.
                    nestingLevel += 2;
                    isInnerClassName = false;
                    break loop;
                }
                case TypeConstants.CLASS_END:
                {
                    nestingLevel -= 2;
                    break;
                }
                case JavaTypeConstants.INNER_CLASS_SEPARATOR:
                {
                    // We've found the start of an inner class name in a signature.
                    isInnerClassName = true;
                    break loop;
                }
                case TypeConstants.GENERIC_VARIABLE_START:
                {
                    // We've found the start of a type identifier. Skip to the end.
                    while (descriptor.charAt(index++) != TypeConstants.CLASS_END);
                    break;
                }
            }

            if (nestingLevel == 1 &&
                descriptor.charAt(index) != TypeConstants.GENERIC_END)
            {
                // We're at the start of a type parameter. Skip to the start
                // of the bounds.
                while (descriptor.charAt(index++) != TypeConstants.GENERIC_BOUND);
            }
        }

        return descriptor.substring(fluffStartIndex, index);
    }


    /**
     * Returns the next class name from the descriptor.
     */
    public String nextClassName()
    {
        int classNameStartIndex = index;

        // Find the first token marking the end of a class name '<' or ';'.
        loop: while (true)
        {
            switch (descriptor.charAt(index))
            {
                case TypeConstants.GENERIC_START:
                case TypeConstants.CLASS_END:
                case JavaTypeConstants.INNER_CLASS_SEPARATOR:
                {
                    break loop;
                }
            }

            index++;
        }

        String className = descriptor.substring(classNameStartIndex, index);

        // Recompose the inner class name if necessary.
        accumulatedClassName = isInnerClassName ?
            accumulatedClassName + TypeConstants.INNER_CLASS_SEPARATOR + className :
            className;

        return accumulatedClassName;
    }


    /**
     * Returns whether the most recently returned class name was a recomposed
     * inner class name from a signature.
     */
    public boolean isInnerClassName()
    {
        return isInnerClassName;
    }


    /**
     * A main method for testing the class name enumeration.
     */
    public static void main(String[] args)
    {
        try
        {
            for (int index = 0; index < args.length; index++)
            {
                String descriptor = args[index];

                System.out.println("Descriptor ["+descriptor+"]");
                DescriptorClassEnumeration enumeration = new DescriptorClassEnumeration(descriptor);
                System.out.println("  Fluff: ["+enumeration.nextFluff()+"]");
                while (enumeration.hasMoreClassNames())
                {
                    System.out.println("  Name:  ["+enumeration.nextClassName()+"]");
                    System.out.println("  Fluff: ["+enumeration.nextFluff()+"]");
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
