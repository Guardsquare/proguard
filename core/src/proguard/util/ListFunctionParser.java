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
 * This StringFunctionParser can create StringFunction instances for regular
 * expressions. The created function returns a String, or null if it doesn't
 * match. The regular expressions are either presented as a list, or they are
 * interpreted as comma-separated lists, optionally prefixed with '!' negators.
 * If an entry with a negator matches, a negative match is returned, without
 * considering any subsequent entries in the list. The creation of
 * StringFunction instances for the entries is delegated to the given
 * StringFunctionParser.
 *
 * @author Eric Lafortune
 */
public class ListFunctionParser implements StringFunctionParser
{
    private final StringFunctionParser stringFunctionParser;


    /**
     * Creates a new ListFunctionParser that parses individual elements in the
     * comma-separated list with the given StringFunctionParser.
     */
    public ListFunctionParser(StringFunctionParser stringFunctionParser)
    {
        this.stringFunctionParser = stringFunctionParser;
    }


    // Implementations for StringFunctionParser.

    public StringFunction parse(String regularExpression)
    {
        // Split the regular expression to a list and parse it.
        return parse(ListUtil.commaSeparatedList(regularExpression));
    }


    /**
     * Creates a StringFunction for the given regular expression, which can
     * be a list of optionally negated simple entries.
     * <p>
     * An empty list results in a StringFunction that matches any string.
     */
    public StringFunction parse(List regularExpressions)
    {
        StringFunction listFunction = null;

        // Loop over all simple regular expressions, backward, creating a
        // linked list of functions.
        for (int index = regularExpressions.size()-1; index >= 0; index--)
        {
            String regularExpression = (String)regularExpressions.get(index);

            StringFunction entryFunction = parseEntry(regularExpression);

            // Prepend the entry function.
            listFunction =
                listFunction == null ?
                    entryFunction :
                isNegated(regularExpression) ?
                    new AndStringFunction(entryFunction, listFunction) :
                    new OrStringFunction(entryFunction, listFunction);
        }

        return listFunction == null ?
            StringFunction.IDENTITY_FUNCTION :
            listFunction;
    }


    // Small utility methods.

    /**
     * Creates a StringFunction for the given regular expression, which is a
     * an optionally negated simple expression.
     */
    private StringFunction parseEntry(String regularExpression)
    {
        // Wrap the matcher if the regular expression starts with a '!' negator.
        return isNegated(regularExpression) ?
            new NotStringFunction(stringFunctionParser.parse(regularExpression.substring(1))) :
            stringFunctionParser.parse(regularExpression);
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
     * A main method for testing name matching and replacement.
     */
    public static void main(String[] args)
    {
        try
        {
            System.out.println("Regular expression ["+args[0]+"]");
            WildcardManager wildcardManager = new WildcardManager();

            NameParser stringParser =
                new NameParser(wildcardManager);

            StringFunctionParser stringFunctionParser =
                new ListFunctionParser(
                new SingleFunctionParser(stringParser, wildcardManager));

            StringFunction function =
                stringFunctionParser.parse(args[0]);

            for (int index = 1; index < args.length; index++)
            {
                String string = args[index];
                System.out.print("String             ["+string+"]");
                System.out.println(" -> tranformed = "+function.transform(args[index]));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
