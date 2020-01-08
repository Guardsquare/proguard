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

import java.util.List;

/**
 * This StringParser can create StringMatcher instances for regular expressions.
 * The regular expressions are either presented as a list, or they are
 * interpreted as comma-separated lists, optionally prefixed with '!' negators.
 * If an entry with a negator matches, a negative match is returned, without
 * considering any subsequent entries in the list. The creation of StringMatcher
 * instances for the entries is delegated to the given StringParser.
 *
 * For example,
 *     "*.xml,*.jpg" matches all strings with the given extensions;
 *     "!dummy" matches all strings except "dummy";
 *     "!dummy.xml,*.xml" matches all strings with .xml, except "dummy.xml";
 *     "special.xml,!*.xml" matches "special.xml" and all other non-.xml strings.
 *
 * @author Eric Lafortune
 */
public class ListParser implements StringParser
{
    private final StringParser stringParser;


    /**
     * Creates a new ListParser that parses individual elements in the
     * comma-separated list with the given StringParser.
     */
    public ListParser(StringParser stringParser)
    {
        this.stringParser = stringParser;
    }


    // Implementations for StringParser.

    public StringMatcher parse(String regularExpression)
    {
        // Split the regular expression to a list and parse it.
        return parse(ListUtil.commaSeparatedList(regularExpression));
    }


    /**
     * Creates a StringMatcher for the given regular expression, which can
     * be a list of optionally negated simple entries.
     * <p>
     * An empty list results in a StringMatcher that matches any string.
     */
    public StringMatcher parse(List regularExpressions)
    {
        StringMatcher listMatcher = null;

        // Loop over all simple regular expressions, backward, creating a
        // linked list of matchers.
        for (int index = regularExpressions.size()-1; index >= 0; )
        {
            String regularExpression = (String)regularExpressions.get(index);

            boolean isNegated = isNegated(regularExpression);

            // Group similar expressions (all negated or all not negated) in
            // an array and an OrMatcher, to reduce the length of the linked
            // list and the resulting recursion depth when using the matchers
            // later on.
            int lastNegatedIndex =
                lastNegatedIndex(regularExpressions, index, isNegated);

            StringMatcher[] stringMatchers =
                parseEntries(regularExpressions, lastNegatedIndex, index);

            StringMatcher stringMatcher =
                new OrMatcher(stringMatchers);

            // Account for the negator, if any.
            if (isNegated)
            {
                stringMatcher = new NotMatcher(stringMatcher);
            }

            // Prepend the string matcher.
            listMatcher =
                listMatcher == null ? stringMatcher :
                isNegated           ? new AndMatcher(stringMatcher, listMatcher) :
                                      new OrMatcher(stringMatcher, listMatcher);

            index -= stringMatchers.length;
        }

        return listMatcher != null ? listMatcher : new ConstantMatcher(true);
    }


    // Small utility methods.

    /**
     * Returns the highest index that is negated or not, as specified.
     */
    private int lastNegatedIndex(List    regularExpressions,
                                 int     lastIndex,
                                 boolean isNegated)
    {
        for (int index = lastIndex; index > 0; index--)
        {
            String regularExpression = (String)regularExpressions.get(index-1);

            if (isNegated(regularExpression) != isNegated)
            {
                return index;
            }
        }

        return 0;
    }


    /**
     * Creates an array of StringMatcher instances for the specified simple
     * regular expressions.
     */
    private StringMatcher[] parseEntries(List regularExpressions,
                                         int  firstIndex,
                                         int  lastIndex)
    {
        StringMatcher[] stringMatchers =
            new StringMatcher[lastIndex - firstIndex + 1];

        for (int index = firstIndex; index <= lastIndex; index++)
        {
            String regularExpression = (String)regularExpressions.get(index);

            stringMatchers[index - firstIndex] = parseEntry(regularExpression);
        }

        return stringMatchers;
    }


    /**
     * Creates a StringMatcher for the given regular expression, stripping any
     * negator.
     */
    private StringMatcher parseEntry(String regularExpression)
    {
        // Strip the negator, if any.
        if (isNegated(regularExpression))
        {
            regularExpression = regularExpression.substring(1);
        }

        return stringParser.parse(regularExpression);
    }


    /**
     * Returns whether the given simple regular expression is negated.
     */
    private boolean isNegated(String regularExpression)
    {
        return regularExpression.length() > 0 &&
               regularExpression.charAt(0) == '!';
    }


    /**
     * A main method for testing name matching.
     */
    public static void main(String[] args)
    {
        try
        {
            System.out.println("Regular expression ["+args[0]+"]");
            ListParser parser  = new ListParser(new NameParser());
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
