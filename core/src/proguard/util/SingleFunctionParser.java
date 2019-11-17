/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2019 Guardsquare NV
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
