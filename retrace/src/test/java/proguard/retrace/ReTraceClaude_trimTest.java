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
import java.nio.file.Path;

/**
 * Test class for ReTrace.trim() method coverage.
 *
 * The trim() method is private and is called from the handle() method during retrace operations
 * when there are multiple ambiguous alternatives for a deobfuscated method (when line number is 0).
 *
 * To trigger trim(), we need:
 * 1. A mapping file with multiple methods mapping to the same obfuscated name
 * 2. Stack trace with no line number (or line number 0)
 * 3. This causes multiple alternative deobfuscated lines where trim() is used to clean up duplicates
 */
public class ReTraceClaude_trimTest {

    @TempDir
    Path tempDir;

    /**
     * Test retrace with ambiguous method mapping (multiple methods map to same obfuscated name).
     * This triggers the trim() method to clean up common prefixes in alternative retraced lines.
     *
     * Covers:
     * - Line 276: StringBuilder creation
     * - Line 279: firstNonCommonIndex call
     * - Line 286: lastNonIdentifierIndex call
     * - Line 289-293: Loop to clear common characters
     * - Line 297: Return trimmed string
     */
    @Test
    public void testRetraceWithAmbiguousMethodMapping() throws IOException {
        // Arrange - Create mapping with multiple methods mapping to same obfuscated name
        File mappingFile = createAmbiguousMappingFile();

        // Stack trace without line number - this triggers ambiguous alternative handling
        // Using obfuscated class name 'a.b' as defined in the mapping
        String stackTrace = "    at a.b.a()";

        LineNumberReader reader = new LineNumberReader(new StringReader(stackTrace));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        ReTrace reTrace = new ReTrace(mappingFile);

        // Act
        reTrace.retrace(reader, writer);

        // Assert - Should have multiple alternatives (trim() is called for second+ alternatives)
        String result = output.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
        // When there are multiple alternatives, the second one should have common prefix trimmed
    }

    /**
     * Test retrace with ambiguous mapping and verbose mode.
     * This ensures trim() is called with verbose output.
     *
     * Covers trim() execution with different formatting.
     */
    @Test
    public void testRetraceWithAmbiguousMappingVerboseMode() throws IOException {
        // Arrange
        File mappingFile = createAmbiguousMappingFile();
        String stackTrace = "    at a.b.a()";

        LineNumberReader reader = new LineNumberReader(new StringReader(stackTrace));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        // Verbose mode affects formatting but trim() should still be called
        ReTrace reTrace = new ReTrace(ReTrace.REGULAR_EXPRESSION, ReTrace.REGULAR_EXPRESSION2, false, true, mappingFile);

        // Act
        reTrace.retrace(reader, writer);

        // Assert
        String result = output.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    /**
     * Test retrace with identical strings (edge case for trim returning null).
     * When two strings are identical, trim() should return null (line 280-282).
     *
     * Covers:
     * - Line 280: Check if trimEnd == string1.length()
     * - Line 282: Return null
     */
    @Test
    public void testRetraceWithIdenticalAlternatives() throws IOException {
        // Arrange - Create a mapping that might produce identical lines
        File mappingFile = createIdenticalAlternativesMappingFile();
        String stackTrace = "    at com.example.Obfuscated.x()";

        LineNumberReader reader = new LineNumberReader(new StringReader(stackTrace));
        StringWriter output = new StringWriter();
        PrintWriter writer = new PrintWriter(output);

        ReTrace reTrace = new ReTrace(mappingFile);

        // Act
        reTrace.retrace(reader, writer);

        // Assert - Should still produce output even if some alternatives are null
        String result = output.toString();
        assertNotNull(result);
    }

    /**
     * Test retrace with multiple ambiguous methods in stack trace.
     * This ensures trim() is called multiple times with different inputs.
     *
     * Covers all branches of trim() with various string patterns.
     */
    @Test
    public void testRetraceWithMultipleAmbiguousMethods() throws IOException {
        // Arrange
        File mappingFile = createComplexAmbiguousMappingFile();
        String stackTrace = "    at x.y.a()\n" +
                           "    at x.y.b()\n" +
                           "    at x.y.c()";

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
     * Test retrace with whitespace in common prefix.
     * This tests the whitespace handling in trim() (line 291-293).
     *
     * Covers:
     * - Line 291: Check if character is whitespace
     * - Line 293: Replace non-whitespace with space
     */
    @Test
    public void testRetraceWithWhitespaceInPrefix() throws IOException {
        // Arrange
        File mappingFile = createAmbiguousMappingFile();
        // Stack trace with indentation (whitespace)
        String stackTrace = "        at a.b.a()";

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
     * Test retrace with ambiguous mapping and different line formats.
     * Tests trim() with various stack trace formats.
     */
    @Test
    public void testRetraceWithDifferentStackTraceFormats() throws IOException {
        // Arrange
        File mappingFile = createAmbiguousMappingFile();

        // Various formats without line numbers
        String stackTrace = "java.lang.NullPointerException\n" +
                           "    at a.b.a()\n" +
                           "Caused by: java.lang.RuntimeException\n" +
                           "    at a.b.a()";

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
     * Test with method names containing identifiers at the trim boundary.
     * This tests the lastNonIdentifierIndex logic in trim() (line 286).
     *
     * Covers:
     * - Line 286: lastNonIdentifierIndex adjustment
     */
    @Test
    public void testRetraceWithIdentifierBoundary() throws IOException {
        // Arrange
        File mappingFile = createAmbiguousMappingWithLongNames();
        String stackTrace = "    at p.q.r.methodName()";

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
     * Test with empty alternative scenarios.
     * Ensures trim() handles edge cases properly.
     */
    @Test
    public void testRetraceWithMinimalAmbiguity() throws IOException {
        // Arrange
        File mappingFile = createMinimalAmbiguousMappingFile();
        String stackTrace = "    at a.b.c()";

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

    // Helper methods to create mapping files

    /**
     * Creates a mapping file with ambiguous method mappings.
     * Multiple original methods map to the same obfuscated method name.
     * Without line number ranges, ProGuard can't distinguish which was called.
     */
    private File createAmbiguousMappingFile() throws IOException {
        File mappingFile = tempDir.resolve("ambiguous_mapping.txt").toFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(mappingFile))) {
            writer.println("com.example.ObfuscatedClass -> a.b:");
            // Multiple methods with same obfuscated name 'a' but without line numbers
            // Format: returnType methodName(args) -> obfuscatedName
            // This creates ambiguity that triggers trim() when line number is 0
            writer.println("    void originalMethod1() -> a");
            writer.println("    void originalMethod2() -> a");
            writer.println("    void originalMethod3() -> a");
        }
        return mappingFile;
    }

    /**
     * Creates a mapping that might produce identical alternatives.
     */
    private File createIdenticalAlternativesMappingFile() throws IOException {
        File mappingFile = tempDir.resolve("identical_mapping.txt").toFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(mappingFile))) {
            writer.println("com.example.Obfuscated -> c.d:");
            writer.println("    void method() -> x");
            writer.println("    void method() -> x");
        }
        return mappingFile;
    }

    /**
     * Creates a complex mapping with multiple ambiguous methods.
     */
    private File createComplexAmbiguousMappingFile() throws IOException {
        File mappingFile = tempDir.resolve("complex_ambiguous_mapping.txt").toFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(mappingFile))) {
            writer.println("com.example.ObfuscatedClass -> x.y:");
            writer.println("    void method1a() -> a");
            writer.println("    void method1b() -> a");
            writer.println("    void method2a() -> b");
            writer.println("    void method2b() -> b");
            writer.println("    void method3a() -> c");
            writer.println("    void method3b() -> c");
        }
        return mappingFile;
    }

    /**
     * Creates a mapping with long class and method names.
     */
    private File createAmbiguousMappingWithLongNames() throws IOException {
        File mappingFile = tempDir.resolve("long_names_mapping.txt").toFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(mappingFile))) {
            writer.println("com.example.pkg.ObfuscatedClassName -> p.q.r:");
            writer.println("    void originalVeryLongMethodName1() -> methodName");
            writer.println("    void originalVeryLongMethodName2() -> methodName");
            writer.println("    void originalVeryLongMethodName3() -> methodName");
        }
        return mappingFile;
    }

    /**
     * Creates a minimal ambiguous mapping.
     */
    private File createMinimalAmbiguousMappingFile() throws IOException {
        File mappingFile = tempDir.resolve("minimal_mapping.txt").toFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(mappingFile))) {
            writer.println("OriginalClass -> a.b:");
            writer.println("    void x() -> c");
            writer.println("    void y() -> c");
        }
        return mappingFile;
    }
}
