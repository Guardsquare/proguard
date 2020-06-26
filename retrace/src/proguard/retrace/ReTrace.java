/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
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
package proguard.retrace;

import proguard.obfuscate.MappingReader;

import java.io.*;
import java.util.*;

/**
 * Tool for de-obfuscating stack traces of applications that were obfuscated
 * with ProGuard.
 *
 * @author Eric Lafortune
 */
public class ReTrace
{
    private static final String USAGE                  = "Usage: java proguard.retrace.ReTrace [-regex <regex>] [-allclassnames] [-verbose] <mapping_file> [<stacktrace_file>]";
    private static final String DEFAULT_REGEX          = "Default regex: ";
    private static final String REGEX_OPTION           = "-regex";
    private static final String ALL_CLASS_NAMES_OPTION = "-allclassnames";
    private static final String VERBOSE_OPTION         = "-verbose";

    // For example: "com.example.Foo.bar"
    private static final String REGULAR_EXPRESSION_CLASS_METHOD     = "%c\\.%m";

    // For example:
    // "(Foo.java:123:0) ~[0]"
    // "()(Foo.java:123:0)"     (DGD-1732, unknown origin, possibly Sentry)
    // or no source line info   (DGD-1732, Sentry)
    private static final String REGULAR_EXPRESSION_SOURCE_LINE      = "(?:\\(\\))?(?:\\((?:%s)?(?::?%l)?(?::\\d+)?\\))?\\s*(?:~\\[.*\\])?";

    // For example: "at o.afc.b + 45(:45)"
    // Might be present in recent stacktraces accessible from crashlytics.
    private static final String REGULAR_EXPRESSION_OPTIONAL_SOURCE_LINE_INFO = "(?:\\+\\s+[0-9]+)?";

    // For example: "    at com.example.Foo.bar(Foo.java:123:0) ~[0]"
    private static final String REGULAR_EXPRESSION_AT               = ".*?\\bat\\s+" + REGULAR_EXPRESSION_CLASS_METHOD + "\\s*" + REGULAR_EXPRESSION_OPTIONAL_SOURCE_LINE_INFO + REGULAR_EXPRESSION_SOURCE_LINE;

    // For example: "java.lang.ClassCastException: com.example.Foo cannot be cast to com.example.Bar"
    // Every line can only have a single matched class, so we try to avoid
    // longer non-obfuscated class names.
    private static final String REGULAR_EXPRESSION_CAST1            = ".*?\\bjava\\.lang\\.ClassCastException: %c cannot be cast to .{5,}";
    private static final String REGULAR_EXPRESSION_CAST2            = ".*?\\bjava\\.lang\\.ClassCastException: .* cannot be cast to %c";

    // For example: "java.lang.NullPointerException: Attempt to read from field 'java.lang.String com.example.Foo.bar' on a null object reference"
    private static final String REGULAR_EXPRESSION_NULL_FIELD_READ  = ".*?\\bjava\\.lang\\.NullPointerException: Attempt to read from field '%t %c\\.%f' on a null object reference";

    // For example: "java.lang.NullPointerException: Attempt to write to field 'java.lang.String com.example.Foo.bar' on a null object reference"
    private static final String REGULAR_EXPRESSION_NULL_FIELD_WRITE = ".*?\\bjava\\.lang\\.NullPointerException: Attempt to write to field '%t %c\\.%f' on a null object reference";

    // For example: "java.lang.NullPointerException: Attempt to invoke virtual method 'void com.example.Foo.bar(int,boolean)' on a null object reference"
    private static final String REGULAR_EXPRESSION_NULL_METHOD      = ".*?\\bjava\\.lang\\.NullPointerException: Attempt to invoke (?:virtual|interface) method '%t %c\\.%m\\(%a\\)' on a null object reference";

    // For example: "Something: com.example.FooException: something"
    private static final String REGULAR_EXPRESSION_THROW            = "(?:.*?[:\"]\\s+)?%c(?::.*)?";

    // The overall regular expression for a line in the stack trace.
    public static final String REGULAR_EXPRESSION  = "(?:" + REGULAR_EXPRESSION_AT               + ")|" +
                                                     "(?:" + REGULAR_EXPRESSION_CAST1            + ")|" +
                                                     "(?:" + REGULAR_EXPRESSION_CAST2            + ")|" +
                                                     "(?:" + REGULAR_EXPRESSION_NULL_FIELD_READ  + ")|" +
                                                     "(?:" + REGULAR_EXPRESSION_NULL_FIELD_WRITE + ")|" +
                                                     "(?:" + REGULAR_EXPRESSION_NULL_METHOD      + ")|" +
                                                     "(?:" + REGULAR_EXPRESSION_THROW            + ")";

    // The settings.
    private final String  regularExpression;
    private final boolean allClassNames;
    private final boolean verbose;
    private final File    mappingFile;


    /**
     * Creates a new ReTrace instance with a default regular expression,
     * @param mappingFile the mapping file that was written out by
     *                    ProGuard.
     */
    public ReTrace(File mappingFile)
    {
        this(REGULAR_EXPRESSION, false, false, mappingFile);
    }


    /**
     * Creates a new ReTrace instance.
     * @param regularExpression the regular expression for parsing the lines in
     *                          the stack trace.
     * @param allClassNames     specifies whether all words that match class
     *                          names should be de-obfuscated, even if they
     *                          aren't matching the regular expression.
     * @param verbose           specifies whether the de-obfuscated stack trace
     *                          should be verbose.
     * @param mappingFile       the mapping file that was written out by
     *                          ProGuard.
     */
    public ReTrace(String  regularExpression,
                   boolean allClassNames,
                   boolean verbose,
                   File    mappingFile)
    {
        this.regularExpression = regularExpression;
        this.allClassNames     = allClassNames;
        this.verbose           = verbose;
        this.mappingFile       = mappingFile;
    }


    /**
     * De-obfuscates a given stack trace.
     * @param stackTraceReader a reader for the obfuscated stack trace.
     * @param stackTraceWriter a writer for the de-obfuscated stack trace.
     */
    public void retrace(LineNumberReader stackTraceReader,
                        PrintWriter      stackTraceWriter) throws IOException
    {
        // Create a pattern for stack frames.
        FramePattern pattern = new FramePattern(regularExpression, verbose);

        // Create a remapper.
        FrameRemapper mapper = new FrameRemapper();

        // Read the mapping file.
        MappingReader mappingReader = new MappingReader(mappingFile);
        mappingReader.pump(mapper);

        // Read and process the lines of the stack trace.
        while (true)
        {
            // Read a line.
            String obfuscatedLine = stackTraceReader.readLine();
            if (obfuscatedLine == null)
            {
                break;
            }

            // Try to match it against the regular expression.
            FrameInfo obfuscatedFrame = pattern.parse(obfuscatedLine);
            if (obfuscatedFrame != null)
            {
                // Transform the obfuscated frame back to one or more
                // original frames.
                Iterator<FrameInfo> retracedFrames =
                    mapper.transform(obfuscatedFrame).iterator();

                String previousLine = null;

                while (retracedFrames.hasNext())
                {
                    // Retrieve the next retraced frame.
                    FrameInfo retracedFrame = retracedFrames.next();

                    // Format the retraced line.
                    String retracedLine =
                        pattern.format(obfuscatedLine, retracedFrame);

                    // Clear the common first part of ambiguous alternative
                    // retraced lines, to present a cleaner list of
                    // alternatives.
                    String trimmedLine =
                        previousLine != null &&
                        obfuscatedFrame.getLineNumber() == 0 ?
                            trim(retracedLine, previousLine) :
                            retracedLine;

                    // Print out the retraced line.
                    if (trimmedLine != null)
                    {
                        if (allClassNames)
                        {
                            trimmedLine = deobfuscateTokens(trimmedLine, mapper);
                        }

                        stackTraceWriter.println(trimmedLine);
                    }

                    previousLine = retracedLine;
                }
            }
            else
            {
                if (allClassNames)
                {
                    obfuscatedLine = deobfuscateTokens(obfuscatedLine, mapper);
                }

                // Print out the original line.
                stackTraceWriter.println(obfuscatedLine);
            }
        }

        stackTraceWriter.flush();
    }


    /**
     * Attempts to deobfuscate each token of the line to a corresponding
     * original classname if possible.
     */
    private String deobfuscateTokens(String line, FrameRemapper mapper)
    {
        StringBuilder sb = new StringBuilder();

        // Try to deobfuscate any token encountered in the line.
        StringTokenizer st = new StringTokenizer(line, "[]{}()/\\:;, '\"<>", true);
        while (st.hasMoreTokens())
        {
            sb.append(mapper.originalClassName(st.nextToken()));
        }

        return sb.toString();
    }


    /**
     * Returns the first given string, with any leading characters that it has
     * in common with the second string replaced by spaces.
     */
    private String trim(String string1, String string2)
    {
        StringBuilder line = new StringBuilder(string1);

        // Find the common part.
        int trimEnd = firstNonCommonIndex(string1, string2);
        if (trimEnd == string1.length())
        {
            return null;
        }

        // Don't clear the last identifier characters.
        trimEnd = lastNonIdentifierIndex(string1, trimEnd) + 1;

        // Clear the common characters.
        for (int index = 0; index < trimEnd; index++)
        {
            if (!Character.isWhitespace(string1.charAt(index)))
            {
                line.setCharAt(index, ' ');
            }
        }

        return line.toString();
    }


    /**
     * Returns the index of the first character that is not the same in both
     * given strings.
     */
    private int firstNonCommonIndex(String string1, String string2)
    {
        int index = 0;
        while (index < string1.length() &&
               index < string2.length() &&
               string1.charAt(index) == string2.charAt(index))
        {
            index++;
        }

        return index;
    }


    /**
     * Returns the index of the last character that is not an identifier
     * character in the given string, at or before the given index.
     */
    private int lastNonIdentifierIndex(String line, int index)
    {
        while (index >= 0 &&
               Character.isJavaIdentifierPart(line.charAt(index)))
        {
            index--;
        }

        return index;
    }


    /**
     * The main program for ReTrace.
     */
    public static void main(String[] args)
    {
        // Parse the arguments.
        if (args.length < 1)
        {
            System.err.println(USAGE);
            System.err.println();
            System.err.println(DEFAULT_REGEX + REGULAR_EXPRESSION);
            System.exit(-1);
        }

        String  regularExpresssion = REGULAR_EXPRESSION;
        boolean verbose            = false;
        boolean allClassNames             = false;

        int argumentIndex = 0;
        while (argumentIndex < args.length)
        {
            String arg = args[argumentIndex];
            if (arg.equals(REGEX_OPTION))
            {
                regularExpresssion = args[++argumentIndex];
            }
            else if (arg.equals(ALL_CLASS_NAMES_OPTION))
            {
                allClassNames = true;
            }
            else if (arg.equals(VERBOSE_OPTION))
            {
                verbose = true;
            }
            else
            {
                break;
            }

            argumentIndex++;
        }

        if (argumentIndex >= args.length)
        {
            System.err.println(USAGE);
            System.exit(-1);
        }

        // Convert the arguments into File instances.
        File mappingFile    = new File(args[argumentIndex++]);
        File stackTraceFile = argumentIndex < args.length ?
            new File(args[argumentIndex]) :
            null;

        try
        {
            // Open the input stack trace. We're always using the UTF-8
            // character encoding, even for reading from the standard
            // input.
            LineNumberReader reader =
                new LineNumberReader(
                new BufferedReader(
                new InputStreamReader(stackTraceFile == null ? System.in :
                new FileInputStream(stackTraceFile), "UTF-8")));

            // Open the output stack trace, again using UTF-8 encoding.
            PrintWriter writer =
                new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"));

            try
            {
                // Execute ReTrace with the collected settings.
                new ReTrace(regularExpresssion, allClassNames, verbose, mappingFile)
                    .retrace(reader, writer);
            }
            finally
            {
                // Close the input stack trace if it was a file.
                if (stackTraceFile != null)
                {
                    reader.close();
                }
            }
        }
        catch (IOException ex)
        {
            if (verbose)
            {
                // Print a verbose stack trace.
                ex.printStackTrace();
            }
            else
            {
                // Print just the stack trace message.
                System.err.println("Error: "+ex.getMessage());
            }

            System.exit(1);
        }

        System.exit(0);
    }
}
