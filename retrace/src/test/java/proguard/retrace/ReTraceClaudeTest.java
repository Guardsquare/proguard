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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Test class for ReTrace.
 */
public class ReTraceClaudeTest {

    @TempDir
    Path tempDir;

    /**
     * Custom exception to handle System.exit() calls in tests.
     */
    private static class ExitException extends SecurityException {
        public final int status;
        public ExitException(int status) {
            super("System.exit() called");
            this.status = status;
        }
    }

    /**
     * Security manager that prevents System.exit() calls.
     */
    private static class NoExitSecurityManager extends SecurityManager {
        @Override
        public void checkPermission(java.security.Permission perm) {
            // Allow everything
        }

        @Override
        public void checkPermission(java.security.Permission perm, Object context) {
            // Allow everything
        }

        @Override
        public void checkExit(int status) {
            super.checkExit(status);
            throw new ExitException(status);
        }
    }

    /**
     * Test constructor with File parameter.
     */
    @Test
    public void testConstructorWithFile() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();

        // Act
        ReTrace reTrace = new ReTrace(mappingFile);

        // Assert
        assertNotNull(reTrace);
    }

    /**
     * Test constructor with all parameters.
     */
    @Test
    public void testConstructorWithAllParameters() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        String regex = ReTrace.REGULAR_EXPRESSION;
        String regex2 = ReTrace.REGULAR_EXPRESSION2;
        boolean allClassNames = true;
        boolean verbose = true;

        // Act
        ReTrace reTrace = new ReTrace(regex, regex2, allClassNames, verbose, mappingFile);

        // Assert
        assertNotNull(reTrace);
    }

    /**
     * Test constructor with non-existent mapping file.
     */
    @Test
    public void testConstructorWithNonExistentFile() {
        // Arrange
        File nonExistentFile = new File(tempDir.toFile(), "nonexistent.map");

        // Act
        ReTrace reTrace = new ReTrace(nonExistentFile);

        // Assert - constructor should not throw, but retrace will fail
        assertNotNull(reTrace);
    }

    /**
     * Test retrace with simple stack trace.
     */
    @Test
    public void testRetraceWithSimpleStackTrace() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        String stackTrace = "    at a.b.c(SourceFile:10)";

        LineNumberReader reader = new LineNumberReader(new StringReader(stackTrace));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        ReTrace reTrace = new ReTrace(mappingFile);

        // Act
        reTrace.retrace(reader, writer);

        // Assert
        String result = output.toString();
        assertNotNull(result);
        assertTrue(result.contains("com.example.OriginalClass"));
    }

    /**
     * Test retrace with empty stack trace.
     */
    @Test
    public void testRetraceWithEmptyStackTrace() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        String stackTrace = "";

        LineNumberReader reader = new LineNumberReader(new StringReader(stackTrace));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        ReTrace reTrace = new ReTrace(mappingFile);

        // Act
        reTrace.retrace(reader, writer);

        // Assert
        String result = output.toString();
        assertNotNull(result);
        assertEquals("", result.trim());
    }

    /**
     * Test retrace with multiple lines.
     */
    @Test
    public void testRetraceWithMultipleLines() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        String stackTrace = "Exception in thread \"main\" java.lang.NullPointerException\n" +
                           "    at a.b.c(SourceFile:10)\n" +
                           "    at a.b.d(SourceFile:20)";

        LineNumberReader reader = new LineNumberReader(new StringReader(stackTrace));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        ReTrace reTrace = new ReTrace(mappingFile);

        // Act
        reTrace.retrace(reader, writer);

        // Assert
        String result = output.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    /**
     * Test retrace with verbose mode.
     */
    @Test
    public void testRetraceWithVerboseMode() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        String stackTrace = "    at a.b.c(SourceFile:10)";

        LineNumberReader reader = new LineNumberReader(new StringReader(stackTrace));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        ReTrace reTrace = new ReTrace(ReTrace.REGULAR_EXPRESSION, ReTrace.REGULAR_EXPRESSION2, false, true, mappingFile);

        // Act
        reTrace.retrace(reader, writer);

        // Assert
        String result = output.toString();
        assertNotNull(result);
    }

    /**
     * Test retrace with allClassNames mode.
     */
    @Test
    public void testRetraceWithAllClassNamesMode() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        String stackTrace = "java.lang.ClassCastException: a.b cannot be cast to a.c";

        LineNumberReader reader = new LineNumberReader(new StringReader(stackTrace));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        ReTrace reTrace = new ReTrace(ReTrace.REGULAR_EXPRESSION, ReTrace.REGULAR_EXPRESSION2, true, false, mappingFile);

        // Act
        reTrace.retrace(reader, writer);

        // Assert
        String result = output.toString();
        assertNotNull(result);
    }

    /**
     * Test retrace with invalid mapping file (IOException during retrace).
     */
    @Test
    public void testRetraceWithInvalidMappingFile() throws IOException {
        // Arrange
        File invalidMappingFile = createInvalidMappingFile();
        String stackTrace = "    at a.b.c(SourceFile:10)";

        LineNumberReader reader = new LineNumberReader(new StringReader(stackTrace));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        ReTrace reTrace = new ReTrace(invalidMappingFile);

        // Act & Assert - retrace should handle the invalid mapping gracefully
        assertDoesNotThrow(() -> reTrace.retrace(reader, writer));
    }

    /**
     * Test retrace with non-existent mapping file (should throw IOException).
     */
    @Test
    public void testRetraceWithNonExistentMappingFile() {
        // Arrange
        File nonExistentFile = new File(tempDir.toFile(), "nonexistent.map");
        String stackTrace = "    at a.b.c(SourceFile:10)";

        LineNumberReader reader = new LineNumberReader(new StringReader(stackTrace));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        ReTrace reTrace = new ReTrace(nonExistentFile);

        // Act & Assert
        assertThrows(IOException.class, () -> reTrace.retrace(reader, writer));
    }

    /**
     * Test retrace with custom regex.
     */
    @Test
    public void testRetraceWithCustomRegex() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        String customRegex = ".*?\\bat\\s+%c\\.%m";
        String stackTrace = "    at a.b.c";

        LineNumberReader reader = new LineNumberReader(new StringReader(stackTrace));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        ReTrace reTrace = new ReTrace(customRegex, ReTrace.REGULAR_EXPRESSION2, false, false, mappingFile);

        // Act
        reTrace.retrace(reader, writer);

        // Assert
        String result = output.toString();
        assertNotNull(result);
    }

    /**
     * Test main method with no arguments.
     */
    @Test
    public void testMainWithNoArguments() {
        // Arrange
        String[] args = {};

        // Capture System.err
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        SecurityManager originalSecurityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());

        try {
            // Act
            ReTrace.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Assert
            assertEquals(-1, e.status);
            String errorOutput = errContent.toString();
            assertTrue(errorOutput.contains("Usage:"));
        } finally {
            System.setErr(originalErr);
            System.setSecurityManager(originalSecurityManager);
        }
    }

    /**
     * Test main method with mapping file only.
     */
    @Test
    public void testMainWithMappingFileOnly() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        String[] args = {mappingFile.getAbsolutePath()};

        // Provide empty input
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream("".getBytes()));

        // Capture System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        SecurityManager originalSecurityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());

        try {
            // Act
            ReTrace.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Assert
            assertEquals(0, e.status);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
            System.setSecurityManager(originalSecurityManager);
        }
    }

    /**
     * Test main method with mapping file and stack trace file.
     */
    @Test
    public void testMainWithMappingAndStackTraceFiles() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        File stackTraceFile = createStackTraceFile();
        String[] args = {mappingFile.getAbsolutePath(), stackTraceFile.getAbsolutePath()};

        // Capture System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        SecurityManager originalSecurityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());

        try {
            // Act
            ReTrace.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Assert
            assertEquals(0, e.status);
            String output = outContent.toString();
            assertNotNull(output);
        } finally {
            System.setOut(originalOut);
            System.setSecurityManager(originalSecurityManager);
        }
    }

    /**
     * Test main method with -verbose option.
     */
    @Test
    public void testMainWithVerboseOption() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        File stackTraceFile = createStackTraceFile();
        String[] args = {"-verbose", mappingFile.getAbsolutePath(), stackTraceFile.getAbsolutePath()};

        // Capture System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        SecurityManager originalSecurityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());

        try {
            // Act
            ReTrace.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Assert
            assertEquals(0, e.status);
        } finally {
            System.setOut(originalOut);
            System.setSecurityManager(originalSecurityManager);
        }
    }

    /**
     * Test main method with -allclassnames option.
     */
    @Test
    public void testMainWithAllClassNamesOption() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        File stackTraceFile = createStackTraceFile();
        String[] args = {"-allclassnames", mappingFile.getAbsolutePath(), stackTraceFile.getAbsolutePath()};

        // Capture System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        SecurityManager originalSecurityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());

        try {
            // Act
            ReTrace.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Assert
            assertEquals(0, e.status);
        } finally {
            System.setOut(originalOut);
            System.setSecurityManager(originalSecurityManager);
        }
    }

    /**
     * Test main method with -regex option.
     */
    @Test
    public void testMainWithRegexOption() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        File stackTraceFile = createStackTraceFile();
        String customRegex = ".*?\\bat\\s+%c\\.%m";
        String[] args = {"-regex", customRegex, mappingFile.getAbsolutePath(), stackTraceFile.getAbsolutePath()};

        // Capture System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        SecurityManager originalSecurityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());

        try {
            // Act
            ReTrace.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Assert
            assertEquals(0, e.status);
        } finally {
            System.setOut(originalOut);
            System.setSecurityManager(originalSecurityManager);
        }
    }

    /**
     * Test main method with all options combined.
     */
    @Test
    public void testMainWithAllOptions() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        File stackTraceFile = createStackTraceFile();
        String customRegex = ".*?\\bat\\s+%c\\.%m";
        String[] args = {"-regex", customRegex, "-verbose", "-allclassnames", mappingFile.getAbsolutePath(), stackTraceFile.getAbsolutePath()};

        // Capture System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        SecurityManager originalSecurityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());

        try {
            // Act
            ReTrace.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Assert
            assertEquals(0, e.status);
        } finally {
            System.setOut(originalOut);
            System.setSecurityManager(originalSecurityManager);
        }
    }

    /**
     * Test main method with only options (missing mapping file).
     */
    @Test
    public void testMainWithOnlyOptions() {
        // Arrange
        String[] args = {"-verbose", "-allclassnames"};

        // Capture System.err
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        SecurityManager originalSecurityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());

        try {
            // Act
            ReTrace.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Assert
            assertEquals(-1, e.status);
            String errorOutput = errContent.toString();
            assertTrue(errorOutput.contains("Usage:"));
        } finally {
            System.setErr(originalErr);
            System.setSecurityManager(originalSecurityManager);
        }
    }

    /**
     * Test main method with non-existent mapping file.
     */
    @Test
    public void testMainWithNonExistentMappingFile() {
        // Arrange
        String[] args = {"nonexistent.map"};

        // Provide empty input
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream("".getBytes()));

        // Capture System.err and System.out
        PrintStream originalErr = System.err;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        System.setOut(new PrintStream(outContent));

        SecurityManager originalSecurityManager = System.getSecurityManager();
        System.setSecurityManager(new NoExitSecurityManager());

        try {
            // Act
            ReTrace.main(args);
            fail("Expected ExitException to be thrown");
        } catch (ExitException e) {
            // Assert
            assertEquals(1, e.status);
            String errorOutput = errContent.toString();
            assertTrue(errorOutput.contains("Error:"));
        } finally {
            System.setIn(originalIn);
            System.setErr(originalErr);
            System.setOut(originalOut);
            System.setSecurityManager(originalSecurityManager);
        }
    }

    /**
     * Test retrace with method mapping.
     */
    @Test
    public void testRetraceWithMethodMapping() throws IOException {
        // Arrange
        File mappingFile = createMethodMappingFile();
        String stackTrace = "    at a.b.c(SourceFile:15)";

        LineNumberReader reader = new LineNumberReader(new StringReader(stackTrace));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        ReTrace reTrace = new ReTrace(mappingFile);

        // Act
        reTrace.retrace(reader, writer);

        // Assert
        String result = output.toString();
        assertNotNull(result);
        assertTrue(result.contains("com.example.OriginalClass") || result.contains("originalMethod"));
    }

    /**
     * Test retrace with field mapping.
     */
    @Test
    public void testRetraceWithFieldMapping() throws IOException {
        // Arrange
        File mappingFile = createFieldMappingFile();
        String stackTrace = "java.lang.NullPointerException: Attempt to read from field 'java.lang.String a.b.c' on a null object reference";

        LineNumberReader reader = new LineNumberReader(new StringReader(stackTrace));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        ReTrace reTrace = new ReTrace(mappingFile);

        // Act
        reTrace.retrace(reader, writer);

        // Assert
        String result = output.toString();
        assertNotNull(result);
    }

    /**
     * Test retrace with ClassCastException pattern.
     */
    @Test
    public void testRetraceWithClassCastException() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        String stackTrace = "java.lang.ClassCastException: a.b cannot be cast to java.lang.String";

        LineNumberReader reader = new LineNumberReader(new StringReader(stackTrace));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        ReTrace reTrace = new ReTrace(mappingFile);

        // Act
        reTrace.retrace(reader, writer);

        // Assert
        String result = output.toString();
        assertNotNull(result);
    }

    /**
     * Test retrace preserves non-matching lines.
     */
    @Test
    public void testRetracePreservesNonMatchingLines() throws IOException {
        // Arrange
        File mappingFile = createSimpleMappingFile();
        String nonMatchingLine = "This is a non-matching line";

        LineNumberReader reader = new LineNumberReader(new StringReader(nonMatchingLine));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        ReTrace reTrace = new ReTrace(mappingFile);

        // Act
        reTrace.retrace(reader, writer);

        // Assert
        String result = output.toString().trim();
        assertEquals(nonMatchingLine, result);
    }

    // Helper methods

    /**
     * Creates a simple mapping file for testing.
     */
    private File createSimpleMappingFile() throws IOException {
        File mappingFile = tempDir.resolve("mapping.txt").toFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(mappingFile))) {
            writer.println("com.example.OriginalClass -> a.b:");
            writer.println("    int originalField -> c");
            writer.println("    void originalMethod() -> d");
        }
        return mappingFile;
    }

    /**
     * Creates a mapping file with method line number information.
     */
    private File createMethodMappingFile() throws IOException {
        File mappingFile = tempDir.resolve("method_mapping.txt").toFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(mappingFile))) {
            writer.println("com.example.OriginalClass -> a.b:");
            writer.println("    10:20:void originalMethod():10:20 -> c");
        }
        return mappingFile;
    }

    /**
     * Creates a mapping file with field information.
     */
    private File createFieldMappingFile() throws IOException {
        File mappingFile = tempDir.resolve("field_mapping.txt").toFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(mappingFile))) {
            writer.println("com.example.OriginalClass -> a.b:");
            writer.println("    java.lang.String originalField -> c");
        }
        return mappingFile;
    }

    /**
     * Creates an invalid mapping file.
     */
    private File createInvalidMappingFile() throws IOException {
        File mappingFile = tempDir.resolve("invalid_mapping.txt").toFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(mappingFile))) {
            writer.println("This is not a valid mapping file format");
            writer.println("Random text here");
        }
        return mappingFile;
    }

    /**
     * Creates a stack trace file for testing.
     */
    private File createStackTraceFile() throws IOException {
        File stackTraceFile = tempDir.resolve("stacktrace.txt").toFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(stackTraceFile))) {
            writer.println("Exception in thread \"main\" java.lang.NullPointerException");
            writer.println("    at a.b.c(SourceFile:10)");
            writer.println("    at a.b.d(SourceFile:20)");
        }
        return stackTraceFile;
    }
}
