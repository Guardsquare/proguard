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
package proguard.util;

import proguard.classfile.ClassConstants;

import java.util.*;

/**
 * This StringParser can create StringMatcher instances for regular expressions
 * matching internal class names (or descriptors containing class names).
 * The regular expressions can contain the following wildcards:
 * '%'     for a single internal primitive type character (V, Z, B, C, S, I, F,
 *         J, or D),
 * '?'     for a single regular class name character,
 * '*'     for any number of regular class name characters,
 * '**'    for any number of regular class name characters or package separator
 *         characters ('/'),
 * 'L***;' for a single internal type (class name or primitive type,
 *         array or non-array),
 * 'L///;' for any number of internal types (class names and primitive
 *         types), and
 * '<n>'   for a reference to an earlier wildcard (n = 1, 2, ...)
 *
 * @author Eric Lafortune
 */
public class ClassNameParser implements StringParser
{
    private static final char[] PRIMITIVE_TYPES = new char[]
    {
        ClassConstants.TYPE_VOID,
        ClassConstants.TYPE_BOOLEAN,
        ClassConstants.TYPE_BYTE,
        ClassConstants.TYPE_CHAR,
        ClassConstants.TYPE_SHORT,
        ClassConstants.TYPE_INT,
        ClassConstants.TYPE_LONG,
        ClassConstants.TYPE_FLOAT,
        ClassConstants.TYPE_DOUBLE,
    };


    private final WildcardManager wildcardManager;


    /**
     * Creates a new ClassNameParser.
     */
    public ClassNameParser()
    {
        this(null);
    }


    /**
     * Creates a new ClassNameParser that supports references to earlier
     * wildcards.
     *
     * @param wildcardManager an optional scope for StringMatcher instances
     *                        that match wildcards.
     */
    public ClassNameParser(WildcardManager wildcardManager)
    {
        this.wildcardManager = wildcardManager;
    }


    // Implementations for StringParser.

    public StringMatcher parse(String regularExpression)
    {
        int           index;
        StringMatcher nextMatcher = new EmptyStringMatcher();

        // Look for wildcards.
        for (index = 0; index < regularExpression.length(); index++)
        {
            // Is there an 'L///;' wildcard?
            if (regularExpression.regionMatches(index, "L///;", 0, 5))
            {
                // Create a settable matcher so we can parse wildcards from
                // left to right.
                SettableMatcher settableMatcher = new SettableMatcher();

                // Create a matcher for the wildcard.
                VariableStringMatcher variableStringMatcher =
                    new VariableStringMatcher(null,
                                              new char[] { ClassConstants.METHOD_ARGUMENTS_CLOSE },
                                              0,
                                              Integer.MAX_VALUE,
                                              settableMatcher);

                // Remember it so it can be referenced back.
                if (wildcardManager != null)
                {
                    wildcardManager.rememberVariableStringMatcher(variableStringMatcher);
                }

                // Recursively create a matcher for the rest of the string.
                settableMatcher.setMatcher(parse(regularExpression.substring(index + 5)));

                nextMatcher = variableStringMatcher;
                break;
            }

            // Is there an 'L***;' wildcard?
            else if (regularExpression.regionMatches(index, "L***;", 0, 5))
            {
                // Create a settable matcher so we can parse wildcards from
                // left to right.
                SettableMatcher settableMatcher = new SettableMatcher();

                // Create a matcher for the wildcard.
                // TODO: The returned variable matcher is actually a composite that doesn't return the entire matched string.
                VariableStringMatcher variableStringMatcher =
                    createAnyTypeMatcher(settableMatcher);

                // Remember it so it can be referenced back.
                if (wildcardManager != null)
                {
                    wildcardManager.rememberVariableStringMatcher(variableStringMatcher);
                }

                // Recursively create a matcher for the rest of the string.
                settableMatcher.setMatcher(parse(regularExpression.substring(index + 5)));

                nextMatcher = variableStringMatcher;
                break;
            }

            // Is there a '**' wildcard?
            else if (regularExpression.regionMatches(index, "**", 0, 2))
            {
                // Handle the end of the regular expression more efficiently,
                // without any next matcher for the variable string matcher.
                SettableMatcher settableMatcher =
                    index + 2 == regularExpression.length() ? null :
                        new SettableMatcher();

                // Create a matcher for the wildcard.
                VariableStringMatcher variableStringMatcher =
                    new VariableStringMatcher(null,
                                              new char[] { ClassConstants.TYPE_CLASS_END },
                                              0,
                                              Integer.MAX_VALUE,
                                              settableMatcher);

                // Remember it so it can be referenced back.
                if (wildcardManager != null)
                {
                    wildcardManager.rememberVariableStringMatcher(variableStringMatcher);
                }

                // Recursively create a matcher for the rest of the string.
                if (settableMatcher != null)
                {
                    settableMatcher.setMatcher(parse(regularExpression.substring(index + 2)));
                }

                nextMatcher = variableStringMatcher;
                break;
            }

            // Is there a '*' wildcard?
            else if (regularExpression.charAt(index) == '*')
            {
                // Create a settable matcher so we can parse wildcards from
                // left to right.
                SettableMatcher settableMatcher = new SettableMatcher();

                // Create a matcher for the wildcard.
                VariableStringMatcher variableStringMatcher =
                    new VariableStringMatcher(null,
                                              new char[] { ClassConstants.TYPE_CLASS_END, ClassConstants.PACKAGE_SEPARATOR },
                                              0,
                                              Integer.MAX_VALUE,
                                              settableMatcher);

                // Remember it so it can be referenced back.
                if (wildcardManager != null)
                {
                    wildcardManager.rememberVariableStringMatcher(variableStringMatcher);
                }

                // Recursively create a matcher for the rest of the string.
                settableMatcher.setMatcher(parse(regularExpression.substring(index + 1)));

                nextMatcher = variableStringMatcher;
                break;
            }

            // Is there a '?' wildcard?
            else if (regularExpression.charAt(index) == '?')
            {
                // Create a settable matcher so we can parse wildcards from
                // left to right.
                SettableMatcher settableMatcher = new SettableMatcher();

                // Create a matcher for the wildcard.
                VariableStringMatcher variableStringMatcher =
                    new VariableStringMatcher(null,
                                              new char[] { ClassConstants.TYPE_CLASS_END, ClassConstants.PACKAGE_SEPARATOR },
                                              1,
                                              1,
                                              settableMatcher);

                // Remember it so it can be referenced back.
                if (wildcardManager != null)
                {
                    wildcardManager.rememberVariableStringMatcher(variableStringMatcher);
                }

                // Recursively create a matcher for the rest of the string.
                settableMatcher.setMatcher(parse(regularExpression.substring(index + 1)));

                nextMatcher = variableStringMatcher;
                break;
            }

            // Is there a '%' wildcard?
            else if (regularExpression.charAt(index) == '%')
            {
                // Create a settable matcher so we can parse wildcards from
                // left to right.
                SettableMatcher settableMatcher = new SettableMatcher();

                // Create a matcher for the wildcard.
                VariableStringMatcher variableStringMatcher =
                    new VariableStringMatcher(PRIMITIVE_TYPES,
                                              null,
                                              1,
                                              1,
                                              settableMatcher);

                // Remember it so it can be referenced back.
                if (wildcardManager != null)
                {
                    wildcardManager.rememberVariableStringMatcher(variableStringMatcher);
                }

                // Recursively create a matcher for the rest of the string.
                settableMatcher.setMatcher(parse(regularExpression.substring(index + 1)));

                nextMatcher = variableStringMatcher;
                break;
            }

            // Is there a '<n>' wildcard?
            else if (wildcardManager != null)
            {
                int wildCardIndex = wildcardManager.wildCardIndex(regularExpression, index);
                if (wildCardIndex >= 0)
                {
                    // Find the index of the closing bracket again.
                    int closingIndex = regularExpression.indexOf('>', index + 1);

                    // Retrieve the specified variable string matcher and
                    // recursively create a matcher for the rest of the string.
                    nextMatcher = wildcardManager.createMatchedStringMatcher(
                        wildCardIndex,
                        parse(regularExpression.substring(closingIndex + 1)));

                    break;
                }
            }
        }

        // Return a matcher for the fixed first part of the regular expression,
        // if any, and the remainder.
        return index != 0 ?
            new FixedStringMatcher(regularExpression.substring(0, index), nextMatcher) :
            nextMatcher;
    }


    // Small utility methods.

    /**
     * Creates a StringMatcher that matches any type (class or primitive type,
     * array or non-array) and then the given matcher.
     */
    private VariableStringMatcher createAnyTypeMatcher(StringMatcher nextMatcher)
    {
        return
            // Any number of '['.
            new VariableStringMatcher(new char[] { ClassConstants.TYPE_ARRAY },
                                      null,
                                      0,
                                      255,
            // Followed by:
            new OrMatcher(
                // A primitive type.
                new VariableStringMatcher(PRIMITIVE_TYPES,
                                          null,
                                          1,
                                          1,
                                          nextMatcher),

                // Or a class type.
                new VariableStringMatcher(new char[] { ClassConstants.TYPE_CLASS_START },
                                                                    null,
                                                                    1,
                                                                    1,
                new VariableStringMatcher(null,
                                          new char[] { ClassConstants.TYPE_CLASS_END },
                                          0,
                                          Integer.MAX_VALUE,
                new VariableStringMatcher(new char[] { ClassConstants.TYPE_CLASS_END },
                                          null,
                                          1,
                                          1,
                                          nextMatcher)))));
    }


    /**
     * A main method for testing class name matching.
     */
    public static void main(String[] args)
    {
        try
        {
            System.out.println("Regular expression ["+args[0]+"]");
            ClassNameParser parser  = new ClassNameParser();
            StringMatcher  matcher = parser.parse(args[0]);
            for (int index = 1; index < args.length; index++)
            {
                String string = args[index];
                System.out.print("String             ["+string+"]");
                System.out.println(" -> match = "+matcher.matches(args[index]));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
