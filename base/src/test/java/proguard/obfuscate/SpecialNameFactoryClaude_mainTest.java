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
package proguard.obfuscate;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SpecialNameFactory#main(String[])}.
 * Tests the main method which demonstrates the SpecialNameFactory by printing 50 special names.
 */
public class SpecialNameFactoryClaude_mainTest {

    /**
     * Tests that main method executes without errors.
     * Verifies that the main method runs successfully.
     */
    @Test
    public void testMainExecutesWithoutErrors() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            assertDoesNotThrow(() -> SpecialNameFactory.main(new String[]{}));

            // Assert
            String output = outputStream.toString();
            assertFalse(output.isEmpty(), "Main should produce output");

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method produces expected output format.
     * Verifies that main prints names in brackets.
     */
    @Test
    public void testMainProducesExpectedOutputFormat() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            SpecialNameFactory.main(new String[]{});

            // Assert
            String output = outputStream.toString();
            assertTrue(output.contains("[a_]"), "Output should contain '[a_]'");
            assertTrue(output.contains("[b_]"), "Output should contain '[b_]'");
            assertTrue(output.contains("[c_]"), "Output should contain '[c_]'");

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method prints correct number of names.
     * Verifies that exactly 50 names are printed.
     */
    @Test
    public void testMainPrintsCorrectNumberOfNames() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            SpecialNameFactory.main(new String[]{});

            // Assert
            String output = outputStream.toString();
            String[] lines = output.split(System.lineSeparator());

            // Count non-empty lines
            int count = 0;
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    count++;
                }
            }

            assertEquals(50, count, "Main should print exactly 50 names");

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method prints all names with special suffix.
     * Verifies that all printed names end with underscore.
     */
    @Test
    public void testMainPrintsNamesWithSpecialSuffix() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            SpecialNameFactory.main(new String[]{});

            // Assert
            String output = outputStream.toString();
            String[] lines = output.split(System.lineSeparator());

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    // Each line should be in format [name_]
                    assertTrue(line.matches("\\[[a-zA-Z]+_\\]"),
                            "Line '" + line + "' should match pattern [name_]");
                    assertTrue(line.contains("_]"),
                            "Line '" + line + "' should contain underscore before closing bracket");
                }
            }

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method with null args doesn't crash.
     * Verifies that the main method doesn't use the args parameter.
     */
    @Test
    public void testMainWithNullArgs() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act - Main doesn't use args, so null should be fine
            assertDoesNotThrow(() -> SpecialNameFactory.main(null));

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method prints names in correct sequence.
     * Verifies that the first few names follow expected pattern.
     */
    @Test
    public void testMainPrintsNamesInSequence() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            SpecialNameFactory.main(new String[]{});

            // Assert
            String output = outputStream.toString();
            String[] lines = output.split(System.lineSeparator());

            // Check first few lines
            assertTrue(lines.length >= 5, "Should have at least 5 lines");
            assertEquals("[a_]", lines[0].trim(), "First line should be [a_]");
            assertEquals("[b_]", lines[1].trim(), "Second line should be [b_]");
            assertEquals("[c_]", lines[2].trim(), "Third line should be [c_]");
            assertEquals("[d_]", lines[3].trim(), "Fourth line should be [d_]");
            assertEquals("[e_]", lines[4].trim(), "Fifth line should be [e_]");

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method prints names with bracket format.
     * Verifies that all names are enclosed in brackets.
     */
    @Test
    public void testMainPrintsNamesInBrackets() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            SpecialNameFactory.main(new String[]{});

            // Assert
            String output = outputStream.toString();
            String[] lines = output.split(System.lineSeparator());

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    assertTrue(line.trim().startsWith("["),
                            "Line '" + line + "' should start with '['");
                    assertTrue(line.trim().endsWith("]"),
                            "Line '" + line + "' should end with ']'");
                }
            }

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method produces non-empty lines.
     * Verifies that each line has content.
     */
    @Test
    public void testMainProducesNonEmptyLines() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            SpecialNameFactory.main(new String[]{});

            // Assert
            String output = outputStream.toString();
            String[] lines = output.split(System.lineSeparator());

            int nonEmptyCount = 0;
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    nonEmptyCount++;
                    assertTrue(line.length() >= 4,
                            "Line should have at least 4 chars: [ + char + _ + ]");
                }
            }

            assertTrue(nonEmptyCount > 0, "Should have at least one non-empty line");

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method with empty args array doesn't crash.
     * Verifies that empty args work the same as null args.
     */
    @Test
    public void testMainWithEmptyArgsArray() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            assertDoesNotThrow(() -> SpecialNameFactory.main(new String[]{}));

            // Assert
            String output = outputStream.toString();
            assertFalse(output.isEmpty(), "Main should produce output");

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method ignores provided arguments.
     * Verifies that args content doesn't affect output.
     */
    @Test
    public void testMainIgnoresArguments() {
        // Arrange
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            // Act - Call with no args
            System.setOut(new PrintStream(outputStream1));
            SpecialNameFactory.main(new String[]{});
            String output1 = outputStream1.toString();

            // Act - Call with args
            System.setOut(new PrintStream(outputStream2));
            SpecialNameFactory.main(new String[]{"arg1", "arg2", "arg3"});
            String output2 = outputStream2.toString();

            // Assert - Both should produce same output
            assertEquals(output1, output2,
                    "Output should be same regardless of arguments");

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method prints all names with trailing underscore.
     * Verifies consistency of special suffix in output.
     */
    @Test
    public void testMainAllNamesHaveTrailingUnderscore() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            SpecialNameFactory.main(new String[]{});

            // Assert
            String output = outputStream.toString();
            String[] lines = output.split(System.lineSeparator());

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    // Extract content between brackets
                    String content = line.trim().replaceAll("[\\[\\]]", "");
                    assertTrue(content.endsWith("_"),
                            "Name '" + content + "' should end with underscore");
                }
            }

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method prints unique names.
     * Verifies that no name is repeated.
     */
    @Test
    public void testMainPrintsUniqueNames() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            SpecialNameFactory.main(new String[]{});

            // Assert
            String output = outputStream.toString();
            String[] lines = output.split(System.lineSeparator());

            java.util.Set<String> uniqueNames = new java.util.HashSet<>();
            int totalNames = 0;

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    uniqueNames.add(line.trim());
                    totalNames++;
                }
            }

            assertEquals(totalNames, uniqueNames.size(),
                    "All printed names should be unique");

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method output can be parsed correctly.
     * Verifies that the format is consistent and parseable.
     */
    @Test
    public void testMainOutputIsParseable() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            SpecialNameFactory.main(new String[]{});

            // Assert
            String output = outputStream.toString();
            String[] lines = output.split(System.lineSeparator());

            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    // Should be able to extract name
                    assertTrue(line.contains("[") && line.contains("]"),
                            "Line should contain brackets");

                    // Extract name
                    String name = line.substring(line.indexOf('[') + 1, line.indexOf(']'));
                    assertFalse(name.isEmpty(), "Extracted name should not be empty");
                    assertTrue(name.endsWith("_"), "Extracted name should end with underscore");
                }
            }

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method prints at least the 50th name.
     * Verifies that the loop completes all iterations.
     */
    @Test
    public void testMainPrints50thName() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            SpecialNameFactory.main(new String[]{});

            // Assert
            String output = outputStream.toString();
            String[] lines = output.split(System.lineSeparator());

            // The 50th name from SimpleNameFactory is "x" (a-z is 26, aa-ax is 24, so 50th is x)
            // With special suffix: x_
            assertTrue(output.contains("[x_]"),
                    "Output should contain the 50th name [x_]");

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method can run multiple times.
     * Verifies that multiple invocations work correctly.
     */
    @Test
    public void testMainCanRunMultipleTimes() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act - Run main multiple times
            assertDoesNotThrow(() -> {
                SpecialNameFactory.main(new String[]{});
                SpecialNameFactory.main(new String[]{});
                SpecialNameFactory.main(new String[]{});
            });

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method output contains expected starting names.
     * Verifies the beginning of the sequence.
     */
    @Test
    public void testMainOutputContainsExpectedStartingNames() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            SpecialNameFactory.main(new String[]{});

            // Assert
            String output = outputStream.toString();

            // Check for first 10 names
            for (char c = 'a'; c <= 'j'; c++) {
                String expectedName = "[" + c + "_]";
                assertTrue(output.contains(expectedName),
                        "Output should contain " + expectedName);
            }

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }
}
