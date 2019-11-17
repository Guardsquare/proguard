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
 * instances  for the entries is delegated to the given StringParser.
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
        for (int index = regularExpressions.size()-1; index >= 0; index--)
        {
            String regularExpression = (String)regularExpressions.get(index);

            StringMatcher entryMatcher = parseEntry(regularExpression);

            // Prepend the entry matcher.
            listMatcher =
                listMatcher == null ?
                    (StringMatcher)entryMatcher :
                isNegated(regularExpression) ?
                    (StringMatcher)new AndMatcher(entryMatcher, listMatcher) :
                    (StringMatcher)new OrMatcher(entryMatcher, listMatcher);
        }

        return listMatcher != null ? listMatcher : new ConstantMatcher(true);
    }


    // Small utility methods.

    /**
     * Creates a StringMatcher for the given regular expression, which is a
     * an optionally negated simple expression.
     */
    private StringMatcher parseEntry(String regularExpression)
    {
        // Wrap the matcher if the regular expression starts with a '!' negator.
        return isNegated(regularExpression) ?
            new NotMatcher(stringParser.parse(regularExpression.substring(1))) :
            stringParser.parse(regularExpression);
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
