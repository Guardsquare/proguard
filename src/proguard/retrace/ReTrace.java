/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2013 Eric Lafortune (eric@graphics.cornell.edu)
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

import proguard.classfile.util.ClassUtil;
import proguard.obfuscate.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;


/**
 * Tool for de-obfuscating stack traces of applications that were obfuscated
 * with ProGuard.
 *
 * @author Eric Lafortune
 */
public class ReTrace
implements   MappingProcessor
{
    private static final String REGEX_OPTION   = "-regex";
    private static final String VERBOSE_OPTION = "-verbose";


    public static final String STACK_TRACE_EXPRESSION = "(?:.*?\\bat\\s+%c\\.%m\\s*\\(.*?(?::%l)?\\)\\s*)|(?:(?:.*?[:\"]\\s+)?%c(?::.*)?)";

    private static final String REGEX_CLASS       = "\\b(?:[A-Za-z0-9_$]+\\.)*[A-Za-z0-9_$]+\\b";
    private static final String REGEX_CLASS_SLASH = "\\b(?:[A-Za-z0-9_$]+/)*[A-Za-z0-9_$]+\\b";
    private static final String REGEX_LINE_NUMBER = "\\b[0-9]+\\b";
    private static final String REGEX_TYPE        = REGEX_CLASS + "(?:\\[\\])*";
    private static final String REGEX_MEMBER      = "<?\\b[A-Za-z0-9_$]+\\b>?";
    private static final String REGEX_ARGUMENTS   = "(?:" + REGEX_TYPE + "(?:\\s*,\\s*" + REGEX_TYPE + ")*)?";

    // The class settings.
    private final String  regularExpression;
    private final boolean verbose;
    private final File    mappingFile;
    private final File    stackTraceFile;

    private Map classMap       = new HashMap();
    private Map classFieldMap  = new HashMap();
    private Map classMethodMap = new HashMap();


    /**
     * Creates a new ReTrace object to process stack traces on the standard
     * input, based on the given mapping file name.
     * @param regularExpression the regular expression for parsing the lines in
     *                          the stack trace.
     * @param verbose           specifies whether the de-obfuscated stack trace
     *                          should be verbose.
     * @param mappingFile       the mapping file that was written out by
     *                          ProGuard.
     */
    public ReTrace(String  regularExpression,
                   boolean verbose,
                   File    mappingFile)
    {
        this(regularExpression, verbose, mappingFile, null);
    }


    /**
     * Creates a new ReTrace object to process a stack trace from the given file,
     * based on the given mapping file name.
     * @param regularExpression the regular expression for parsing the lines in
     *                          the stack trace.
     * @param verbose           specifies whether the de-obfuscated stack trace
     *                          should be verbose.
     * @param mappingFile       the mapping file that was written out by
     *                          ProGuard.
     * @param stackTraceFile    the optional name of the file that contains the
     *                          stack trace.
     */
    public ReTrace(String  regularExpression,
                   boolean verbose,
                   File    mappingFile,
                   File    stackTraceFile)
    {
        this.regularExpression = regularExpression;
        this.verbose           = verbose;
        this.mappingFile       = mappingFile;
        this.stackTraceFile    = stackTraceFile;
    }


    /**
     * Performs the subsequent ReTrace operations.
     */
    public void execute() throws IOException
    {
        // Read the mapping file.
        MappingReader mappingReader = new MappingReader(mappingFile);
        mappingReader.pump(this);

        // Construct the regular expression.
        StringBuffer expressionBuffer    = new StringBuffer(regularExpression.length() + 32);
        char[]       expressionTypes     = new char[32];
        int          expressionTypeCount = 0;
        int index = 0;
        while (true)
        {
            int nextIndex = regularExpression.indexOf('%', index);
            if (nextIndex < 0                             ||
                nextIndex == regularExpression.length()-1 ||
                expressionTypeCount == expressionTypes.length)
            {
                break;
            }

            expressionBuffer.append(regularExpression.substring(index, nextIndex));
            expressionBuffer.append('(');

            char expressionType = regularExpression.charAt(nextIndex + 1);
            switch(expressionType)
            {
                case 'c':
                    expressionBuffer.append(REGEX_CLASS);
                    break;

                case 'C':
                    expressionBuffer.append(REGEX_CLASS_SLASH);
                    break;

                case 'l':
                    expressionBuffer.append(REGEX_LINE_NUMBER);
                    break;

                case 't':
                    expressionBuffer.append(REGEX_TYPE);
                    break;

                case 'f':
                    expressionBuffer.append(REGEX_MEMBER);
                    break;

                case 'm':
                    expressionBuffer.append(REGEX_MEMBER);
                    break;

                case 'a':
                    expressionBuffer.append(REGEX_ARGUMENTS);
                    break;
            }

            expressionBuffer.append(')');

            expressionTypes[expressionTypeCount++] = expressionType;

            index = nextIndex + 2;
        }

        expressionBuffer.append(regularExpression.substring(index));

        Pattern pattern = Pattern.compile(expressionBuffer.toString());

        // Open the stack trace file.
        LineNumberReader reader =
            new LineNumberReader(stackTraceFile == null ?
                (Reader)new InputStreamReader(System.in) :
                (Reader)new BufferedReader(new FileReader(stackTraceFile)));

        // Read and process the lines of the stack trace.
        try
        {
            StringBuffer outLine       = new StringBuffer(256);
            List         extraOutLines = new ArrayList();

            String className = null;

            // Read all lines from the stack trace.
            while (true)
            {
                // Read a line.
                String line = reader.readLine();
                if (line == null)
                {
                    break;
                }

                // Try to match it against the regular expression.
                Matcher matcher = pattern.matcher(line);

                if (matcher.matches())
                {
                    // The line matched the regular expression.
                    int    lineNumber = 0;
                    String type       = null;
                    String arguments  = null;

                    // Extract a class name, a line number, a type, and
                    // arguments.
                    for (int expressionTypeIndex = 0; expressionTypeIndex < expressionTypeCount; expressionTypeIndex++)
                    {
                        int startIndex = matcher.start(expressionTypeIndex + 1);
                        if (startIndex >= 0)
                        {
                            String match = matcher.group(expressionTypeIndex + 1);

                            char expressionType = expressionTypes[expressionTypeIndex];
                            switch (expressionType)
                            {
                                case 'c':
                                    className = originalClassName(match);
                                    break;

                                case 'C':
                                    className = originalClassName(ClassUtil.externalClassName(match));
                                    break;

                                case 'l':
                                    lineNumber = Integer.parseInt(match);
                                    break;

                                case 't':
                                    type = originalType(match);
                                    break;

                                case 'a':
                                    arguments = originalArguments(match);
                                    break;
                            }
                        }
                    }

                    // Deconstruct the input line and reconstruct the output
                    // line. Also collect any additional output lines for this
                    // line.
                    int lineIndex = 0;

                    outLine.setLength(0);
                    extraOutLines.clear();

                    for (int expressionTypeIndex = 0; expressionTypeIndex < expressionTypeCount; expressionTypeIndex++)
                    {
                        int startIndex = matcher.start(expressionTypeIndex + 1);
                        if (startIndex >= 0)
                        {
                            int    endIndex = matcher.end(expressionTypeIndex + 1);
                            String match    = matcher.group(expressionTypeIndex + 1);

                            // Copy a literal piece of the input line.
                            outLine.append(line.substring(lineIndex, startIndex));

                            // Copy a matched and translated piece of the input line.
                            char expressionType = expressionTypes[expressionTypeIndex];
                            switch (expressionType)
                            {
                                case 'c':
                                    className = originalClassName(match);
                                    outLine.append(className);
                                    break;

                                case 'C':
                                    className = originalClassName(ClassUtil.externalClassName(match));
                                    outLine.append(ClassUtil.internalClassName(className));
                                    break;

                                case 'l':
                                    lineNumber = Integer.parseInt(match);
                                    outLine.append(match);
                                    break;

                                case 't':
                                    type = originalType(match);
                                    outLine.append(type);
                                    break;

                                case 'f':
                                    originalFieldName(className,
                                                      match,
                                                      type,
                                                      outLine,
                                                      extraOutLines);
                                    break;

                                case 'm':
                                    originalMethodName(className,
                                                       match,
                                                       lineNumber,
                                                       type,
                                                       arguments,
                                                       outLine,
                                                       extraOutLines);
                                    break;

                                case 'a':
                                    arguments = originalArguments(match);
                                    outLine.append(arguments);
                                    break;
                            }

                            // Skip the original element whose processed version
                            // has just been appended.
                            lineIndex = endIndex;
                        }
                    }

                    // Copy the last literal piece of the input line.
                    outLine.append(line.substring(lineIndex));

                    // Print out the processed line.
                    System.out.println(outLine);

                    // Print out any additional lines.
                    for (int extraLineIndex = 0; extraLineIndex < extraOutLines.size(); extraLineIndex++)
                    {
                        System.out.println(extraOutLines.get(extraLineIndex));
                    }
                }
                else
                {
                    // The line didn't match the regular expression.
                    // Print out the original line.
                    System.out.println(line);
                }
            }
        }
        catch (IOException ex)
        {
            throw new IOException("Can't read stack trace (" + ex.getMessage() + ")");
        }
        finally
        {
            if (stackTraceFile != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException ex)
                {
                    // This shouldn't happen.
                }
            }
        }
    }


    /**
     * Finds the original field name(s), appending the first one to the out
     * line, and any additional alternatives to the extra lines.
     */
    private void originalFieldName(String       className,
                                   String       obfuscatedFieldName,
                                   String       type,
                                   StringBuffer outLine,
                                   List         extraOutLines)
    {
        int extraIndent = -1;

        // Class name -> obfuscated field names.
        Map fieldMap = (Map)classFieldMap.get(className);
        if (fieldMap != null)
        {
            // Obfuscated field names -> fields.
            Set fieldSet = (Set)fieldMap.get(obfuscatedFieldName);
            if (fieldSet != null)
            {
                // Find all matching fields.
                Iterator fieldInfoIterator = fieldSet.iterator();
                while (fieldInfoIterator.hasNext())
                {
                    FieldInfo fieldInfo = (FieldInfo)fieldInfoIterator.next();
                    if (fieldInfo.matches(type))
                    {
                        // Is this the first matching field?
                        if (extraIndent < 0)
                        {
                            extraIndent = outLine.length();

                            // Append the first original name.
                            if (verbose)
                            {
                                outLine.append(fieldInfo.type).append(' ');
                            }
                            outLine.append(fieldInfo.originalName);
                        }
                        else
                        {
                            // Create an additional line with the proper
                            // indentation.
                            StringBuffer extraBuffer = new StringBuffer();
                            for (int counter = 0; counter < extraIndent; counter++)
                            {
                                extraBuffer.append(' ');
                            }

                            // Append the alternative name.
                            if (verbose)
                            {
                                extraBuffer.append(fieldInfo.type).append(' ');
                            }
                            extraBuffer.append(fieldInfo.originalName);

                            // Store the additional line.
                            extraOutLines.add(extraBuffer);
                        }
                    }
                }
            }
        }

        // Just append the obfuscated name if we haven't found any matching
        // fields.
        if (extraIndent < 0)
        {
            outLine.append(obfuscatedFieldName);
        }
    }


    /**
     * Finds the original method name(s), appending the first one to the out
     * line, and any additional alternatives to the extra lines.
     */
    private void originalMethodName(String       className,
                                    String       obfuscatedMethodName,
                                    int          lineNumber,
                                    String       type,
                                    String       arguments,
                                    StringBuffer outLine,
                                    List         extraOutLines)
    {
        int extraIndent = -1;

        // Class name -> obfuscated method names.
        Map methodMap = (Map)classMethodMap.get(className);
        if (methodMap != null)
        {
            // Obfuscated method names -> methods.
            Set methodSet = (Set)methodMap.get(obfuscatedMethodName);
            if (methodSet != null)
            {
                // Find all matching methods.
                Iterator methodInfoIterator = methodSet.iterator();
                while (methodInfoIterator.hasNext())
                {
                    MethodInfo methodInfo = (MethodInfo)methodInfoIterator.next();
                    if (methodInfo.matches(lineNumber, type, arguments))
                    {
                        // Is this the first matching method?
                        if (extraIndent < 0)
                        {
                            extraIndent = outLine.length();

                            // Append the first original name.
                            if (verbose)
                            {
                                outLine.append(methodInfo.type).append(' ');
                            }
                            outLine.append(methodInfo.originalName);
                            if (verbose)
                            {
                                outLine.append('(').append(methodInfo.arguments).append(')');
                            }
                        }
                        else
                        {
                            // Create an additional line with the proper
                            // indentation.
                            StringBuffer extraBuffer = new StringBuffer();
                            for (int counter = 0; counter < extraIndent; counter++)
                            {
                                extraBuffer.append(' ');
                            }

                            // Append the alternative name.
                            if (verbose)
                            {
                                extraBuffer.append(methodInfo.type).append(' ');
                            }
                            extraBuffer.append(methodInfo.originalName);
                            if (verbose)
                            {
                                extraBuffer.append('(').append(methodInfo.arguments).append(')');
                            }

                            // Store the additional line.
                            extraOutLines.add(extraBuffer);
                        }
                    }
                }
            }
        }

        // Just append the obfuscated name if we haven't found any matching
        // methods.
        if (extraIndent < 0)
        {
            outLine.append(obfuscatedMethodName);
        }
    }


    /**
     * Returns the original argument types.
     */
    private String originalArguments(String obfuscatedArguments)
    {
        StringBuffer originalArguments = new StringBuffer();

        int startIndex = 0;
        while (true)
        {
            int endIndex = obfuscatedArguments.indexOf(',', startIndex);
            if (endIndex < 0)
            {
                break;
            }

            originalArguments.append(originalType(obfuscatedArguments.substring(startIndex, endIndex).trim())).append(',');

            startIndex = endIndex + 1;
        }

        originalArguments.append(originalType(obfuscatedArguments.substring(startIndex).trim()));

        return originalArguments.toString();
    }


    /**
     * Returns the original type.
     */
    private String originalType(String obfuscatedType)
    {
        int index = obfuscatedType.indexOf('[');

        return index >= 0 ?
            originalClassName(obfuscatedType.substring(0, index)) + obfuscatedType.substring(index) :
            originalClassName(obfuscatedType);
    }


    /**
     * Returns the original class name.
     */
    private String originalClassName(String obfuscatedClassName)
    {
        String originalClassName = (String)classMap.get(obfuscatedClassName);

        return originalClassName != null ?
            originalClassName :
            obfuscatedClassName;
    }


    // Implementations for MappingProcessor.

    public boolean processClassMapping(String className, String newClassName)
    {
        // Obfuscated class name -> original class name.
        classMap.put(newClassName, className);

        return true;
    }


    public void processFieldMapping(String className, String fieldType, String fieldName, String newFieldName)
    {
        // Original class name -> obfuscated field names.
        Map fieldMap = (Map)classFieldMap.get(className);
        if (fieldMap == null)
        {
            fieldMap = new HashMap();
            classFieldMap.put(className, fieldMap);
        }

        // Obfuscated field name -> fields.
        Set fieldSet = (Set)fieldMap.get(newFieldName);
        if (fieldSet == null)
        {
            fieldSet = new LinkedHashSet();
            fieldMap.put(newFieldName, fieldSet);
        }

        // Add the field information.
        fieldSet.add(new FieldInfo(fieldType,
                                   fieldName));
    }


    public void processMethodMapping(String className, int firstLineNumber, int lastLineNumber, String methodReturnType, String methodName, String methodArguments, String newMethodName)
    {
        // Original class name -> obfuscated method names.
        Map methodMap = (Map)classMethodMap.get(className);
        if (methodMap == null)
        {
            methodMap = new HashMap();
            classMethodMap.put(className, methodMap);
        }

        // Obfuscated method name -> methods.
        Set methodSet = (Set)methodMap.get(newMethodName);
        if (methodSet == null)
        {
            methodSet = new LinkedHashSet();
            methodMap.put(newMethodName, methodSet);
        }

        // Add the method information.
        methodSet.add(new MethodInfo(firstLineNumber,
                                     lastLineNumber,
                                     methodReturnType,
                                     methodArguments,
                                     methodName));
    }


    /**
     * A field record.
     */
    private static class FieldInfo
    {
        private String type;
        private String originalName;


        private FieldInfo(String type, String originalName)
        {
            this.type         = type;
            this.originalName = originalName;
        }


        private boolean matches(String type)
        {
            return
                type == null || type.equals(this.type);
        }
    }


    /**
     * A method record.
     */
    private static class MethodInfo
    {
        private int    firstLineNumber;
        private int    lastLineNumber;
        private String type;
        private String arguments;
        private String originalName;


        private MethodInfo(int firstLineNumber, int lastLineNumber, String type, String arguments, String originalName)
        {
            this.firstLineNumber = firstLineNumber;
            this.lastLineNumber  = lastLineNumber;
            this.type            = type;
            this.arguments       = arguments;
            this.originalName    = originalName;
        }


        private boolean matches(int lineNumber, String type, String arguments)
        {
            return
                (lineNumber == 0    || (firstLineNumber <= lineNumber && lineNumber <= lastLineNumber) || lastLineNumber == 0) &&
                (type       == null || type.equals(this.type))                                                                 &&
                (arguments  == null || arguments.equals(this.arguments));
        }
    }


    /**
     * The main program for ReTrace.
     */
    public static void main(String[] args)
    {
        if (args.length < 1)
        {
            System.err.println("Usage: java proguard.ReTrace [-verbose] <mapping_file> [<stacktrace_file>]");
            System.exit(-1);
        }

        String  regularExpresssion = STACK_TRACE_EXPRESSION;
        boolean verbose            = false;

        int argumentIndex = 0;
        while (argumentIndex < args.length)
        {
            String arg = args[argumentIndex];
            if (arg.equals(REGEX_OPTION))
            {
                regularExpresssion = args[++argumentIndex];
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
            System.err.println("Usage: java proguard.ReTrace [-regex <regex>] [-verbose] <mapping_file> [<stacktrace_file>]");
            System.exit(-1);
        }

        File mappingFile    = new File(args[argumentIndex++]);
        File stackTraceFile = argumentIndex < args.length ?
            new File(args[argumentIndex]) :
            null;

        ReTrace reTrace = new ReTrace(regularExpresssion, verbose, mappingFile, stackTraceFile);

        try
        {
            // Execute ReTrace with its given settings.
            reTrace.execute();
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
