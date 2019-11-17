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

/**
 * This StringFunctionParser creates StringFunction instances for regular
 * expressions with optional "->" transformations. It parses the first
 * part with a given StringParser and the second part with an optional
 * WildcardManager.
 *
 * @author Eric Lafortune
 */
public class SingleFunctionParser implements StringFunctionParser
{
    private final StringParser    stringParser;
    private final WildcardManager wildcardManager;


    /**
     * Creates a new SingleFunctionParser.
     */
    public SingleFunctionParser(StringParser    stringParser,
                                WildcardManager wildcardManager)
    {
        this.stringParser    = stringParser;
        this.wildcardManager = wildcardManager;
    }


    // Implementations for StringFunctionParser.

    public StringFunction parse(String regularExpression)
    {
        // Reset the string matchers, since all wildcards have to be local
        // inside this regular expression.
        if (wildcardManager != null)
        {
            wildcardManager.reset();
        }

        // Does the regular expression specify a transformation?
        int arrowIndex = regularExpression.indexOf("->");
        if (arrowIndex < 0)
        {
            // Otherwise just pass any matched strings.
            return new MatchingStringFunction(stringParser.parse(regularExpression));
        }

        // Split the regular expression into its two parts.
        String patternExpression     = regularExpression.substring(0, arrowIndex);
        String replacementExpression = regularExpression.substring(arrowIndex + 2);

        // First parse the pattern, possibly with wildcards.
        StringFunction patternStringFunction =
            new MatchingStringFunction(stringParser.parse(patternExpression));

        // Then parse the replacement, possibly with references to
        // these wildcards.
        StringFunction replacementStringFunction = wildcardManager == null ?
            new ConstantStringFunction(replacementExpression) :
            wildcardManager.createMatchedStringFunction(replacementExpression);

        // Finally combine the two.
        return new AndStringFunction(patternStringFunction,
                                     replacementStringFunction);
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
                new SingleFunctionParser(stringParser, wildcardManager);

            StringFunction function =
                stringFunctionParser.parse(args[0]);

            for (int index = 1; index < args.length; index++)
            {
                String string = args[index];
                System.out.print("String              ["+string+"]");
                System.out.println(" -> transformed = "+function.transform(args[index]));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
