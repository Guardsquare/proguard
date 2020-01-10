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

import java.util.*;

/**
 * This utility class creates and manages {@link StringMatcher} instances that
 * (1) match wildcards, and
 * (2) refer back to matched wildcards.
 * <p/>
 * Each instance represents a scope in which wildcards can be specified
 * and referenced.
 *
 * @author Eric Lafortune
 */
public class WildcardManager
{
    private List<VariableStringMatcher> variableStringMatchers;


    /**
     * Creates a new WildcardManager.
     */
    public WildcardManager()
    {
        this(new ArrayList<VariableStringMatcher>());
    }


    /**
     * Creates a new WildcardManager with the current list of string matchers
     * of the given WildcardManager.
     */
    public WildcardManager(WildcardManager wildcardManager)
    {
        this(new ArrayList<VariableStringMatcher>(wildcardManager.variableStringMatchers));
    }


    /**
     * Creates a new WildcardManager with the given list of string matchers.
     */
    private WildcardManager(List<VariableStringMatcher> variableStringMatchers)
    {
        this.variableStringMatchers = variableStringMatchers;
    }


    /**
     * Resets the list of string matchers.
     */
    public void reset()
    {
        variableStringMatchers.clear();
    }


    /**
     * Creates a new VariableStringMatcher and remembers it as a next element
     * in the list of string matchers, so it can be referenced by its index
     * later.
     *
     * @param allowedCharacters    an optional list of allowed characters.
     * @param disallowedCharacters an optional list of disallowed characters.
     * @param minimumLength        the minimum length of te variable string.
     * @param maximumLength        the maximum length of te variable string.
     * @param nextMatcher          an optional next matcher for the remainder
     *                             of the string.
     */
    public VariableStringMatcher createVariableStringMatcher(char[]        allowedCharacters,
                                                             char[]        disallowedCharacters,
                                                             int           minimumLength,
                                                             int           maximumLength,
                                                             StringMatcher nextMatcher)
    {
        VariableStringMatcher variableStringMatcher =
            new VariableStringMatcher(allowedCharacters,
                                      disallowedCharacters,
                                      minimumLength,
                                      maximumLength,
                                      nextMatcher);

        rememberVariableStringMatcher(variableStringMatcher);;

        return variableStringMatcher;
    }


    /**
     * Remembers the given VariableStringMatcher as a next element in the list
     * of string matchers, so it can be referenced by its index later.
     */
    public void rememberVariableStringMatcher(VariableStringMatcher variableStringMatcher)
    {
        variableStringMatchers.add(variableStringMatcher);
    }


    /**
     * Creates new MatchedStringMatcher to match the the specified variable
     * string matcher from the list of string matchers.
     *
     * @param index       the 0-based index of previously created and
     *                    remembered VariableStringMatcher instances.
     * @param nextMatcher an optional next matcher for the remainder of the
     *                    string.
     */
    public MatchedStringMatcher createMatchedStringMatcher(int           index,
                                                           StringMatcher nextMatcher)
    {
        return new MatchedStringMatcher(variableStringMatchers.get(index),
                                        nextMatcher);
    }


    /**
     * Creates new StringFunction that transforms the given expression
     * with possible references to wildcards, based on the list of
     * string matchers.
     *
     * For example:
     *     "foo<1>" returns a string function that prepends "foo"
     *     to the first wildcard of the recently matched string.
     */
    public StringFunction createMatchedStringFunction(String expression)
    {
        StringFunction stringFunction = null;

        int prefixIndex = 0;

        // Look for wildcards.
        for (int index = 0; index < expression.length(); index++)
        {
            int wildCardIndex = wildCardIndex(expression, index);
            if (wildCardIndex >= 0)
            {
                // Create the function for the matched wildcard.
                StringFunction matchedStringFunction =
                    new MatchedStringFunction(variableStringMatchers.get(wildCardIndex));

                // Prepend any skipped prefix.
                StringFunction additionalStringFunction =
                    prefixIndex == index ?
                        matchedStringFunction :
                        new ConcatenatingStringFunction(new ConstantStringFunction(expression.substring(prefixIndex, index)), matchedStringFunction);

                // Prepend any collected string function.
                stringFunction =
                    stringFunction == null ?
                        additionalStringFunction :
                        new ConcatenatingStringFunction(stringFunction, additionalStringFunction);

                // Find the index of the closing bracket again.
                index       = expression.indexOf('>', index + 1);
                prefixIndex = index + 1;
            }
        }

        // Make sure we have a function. Append any suffix.
        return
            stringFunction == null             ? new ConstantStringFunction(expression) :
            prefixIndex == expression.length() ? stringFunction                         :
                                                 new ConcatenatingStringFunction(stringFunction, new ConstantStringFunction(expression.substring(prefixIndex)));
    }


    /**
     * Parses a reference to a wildcard at a given index, if any.
     *
     * For example:
     *     "foo<1>" at index 0 returns -1,
     *     "foo<1>" at index 3 returns 0.
     *     "foo<8>" at index 3 returns 7.
     *
     * @param regularExpression the regular expression.
     * @param index             the index at which the regular expression is
     *                          to be checked for a reference.
     * @return the 0-based index of the back reference, or -1 otherwise.
     * @throws IllegalArgumentException if the reference is malformed.
     */
    public int wildCardIndex(String regularExpression,
                             int    index)
    throws IllegalArgumentException
    {
        if (variableStringMatchers == null ||
            regularExpression.charAt(index) != '<')
        {
            return -1;
        }

        int closingBracketIndex = regularExpression.indexOf('>', index);
        if (closingBracketIndex < 0)
        {
            throw new IllegalArgumentException("Missing closing angular bracket after opening bracket at index "+index+" in ["+regularExpression+"]");
        }

        String argumentBetweenBrackets = regularExpression.substring(index+1, closingBracketIndex);

        try
        {
            int wildcardIndex = Integer.parseInt(argumentBetweenBrackets);
            if (wildcardIndex < 1 ||
                wildcardIndex > variableStringMatchers.size())
            {
                throw new IllegalArgumentException("Invalid reference to wildcard ("+wildcardIndex+", must lie between 1 and "+variableStringMatchers.size()+" in ["+regularExpression+"])");
            }

            return wildcardIndex - 1;
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }
}
