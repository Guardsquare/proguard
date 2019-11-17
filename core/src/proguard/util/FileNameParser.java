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

import java.io.File;

/**
 * This StringParser can create StringMatcher instances for regular expressions
 * matching file names. The regular expressions can contain the following
 * wildcards:
 * '?'  for a single regular file name character,
 * '*'  for any number of regular file name characters, and
 * '**' for any number of regular file name characters or directory separator
 *      characters (always including '/').
 *
 * @author Eric Lafortune
 */
public class FileNameParser implements StringParser
{
    private final WildcardManager wildcardManager;


    /**
     * Creates a new FileNameParser.
     */
    public FileNameParser()
    {
        this(null);
    }


    /**
     * Creates a new FileNameParser that supports references to earlier
     * wildcards.
     *
     * @param wildcardManager an optional scope for StringMatcher instances
     *                        that match wildcards.
     */
    public FileNameParser(WildcardManager wildcardManager)
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
            // Is there a '**' wildcard?
            if (regularExpression.regionMatches(index, "**", 0, 2))
            {
                // Create a settable matcher so we can parse wildcards from
                // left to right.
                SettableMatcher settableMatcher = new SettableMatcher();

                // Create a matcher for the wildcard.
                VariableStringMatcher variableStringMatcher =
                    new VariableStringMatcher(null,
                                              null,
                                              0,
                                              Integer.MAX_VALUE,
                                              settableMatcher);

                // Remember it so it can be referenced back.
                if (wildcardManager != null)
                {
                    wildcardManager.rememberVariableStringMatcher(variableStringMatcher);
                }

                // Recursively create a matcher for the rest of the string.
                settableMatcher.setMatcher(parse(regularExpression.substring(index + 2)));

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
                                              new char[] { File.pathSeparatorChar, '/' },
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
                                              new char[] { File.pathSeparatorChar, '/' },
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
        }

        // Return a matcher for the fixed first part of the regular expression,
        // if any, and the remainder.
        return index != 0 ?
            new FixedStringMatcher(regularExpression.substring(0, index), nextMatcher) :
            nextMatcher;
    }


    /**
     * A main method for testing file name matching.
     */
    public static void main(String[] args)
    {
        try
        {
            System.out.println("Regular expression ["+args[0]+"]");
            FileNameParser parser  = new FileNameParser();
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
