package proguard.obfuscate;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SimpleNameFactory}.
 * Tests all public methods including constructors, reset, nextName, and main.
 */
public class SimpleNameFactoryClaudeTest {

    // ========== Constructor Tests ==========

    /**
     * Tests the default constructor SimpleNameFactory().
     * Verifies that it generates mixed-case names by default.
     */
    @Test
    public void testDefaultConstructor() {
        // Arrange & Act
        SimpleNameFactory factory = new SimpleNameFactory();

        // Assert - Generate enough names to see both lower and upper case
        Set<Character> seenChars = new HashSet<>();
        for (int i = 0; i < 60; i++) {
            String name = factory.nextName();
            for (char c : name.toCharArray()) {
                seenChars.add(c);
            }
        }

        // Should have both upper and lower case letters
        boolean hasLowerCase = seenChars.stream().anyMatch(Character::isLowerCase);
        boolean hasUpperCase = seenChars.stream().anyMatch(Character::isUpperCase);

        assertTrue(hasLowerCase, "Default constructor should generate lower case letters");
        assertTrue(hasUpperCase, "Default constructor should generate upper case letters (mixed-case mode)");
    }

    /**
     * Tests the parameterized constructor SimpleNameFactory(boolean) with true.
     * Verifies that it generates mixed-case names when flag is true.
     */
    @Test
    public void testConstructorWithMixedCaseTrue() {
        // Arrange & Act
        SimpleNameFactory factory = new SimpleNameFactory(true);

        // Assert - Generate enough names to see both cases
        Set<Character> seenChars = new HashSet<>();
        for (int i = 0; i < 60; i++) {
            String name = factory.nextName();
            for (char c : name.toCharArray()) {
                seenChars.add(c);
            }
        }

        boolean hasLowerCase = seenChars.stream().anyMatch(Character::isLowerCase);
        boolean hasUpperCase = seenChars.stream().anyMatch(Character::isUpperCase);

        assertTrue(hasLowerCase, "Constructor(true) should generate lower case letters");
        assertTrue(hasUpperCase, "Constructor(true) should generate upper case letters");
    }

    /**
     * Tests the parameterized constructor SimpleNameFactory(boolean) with false.
     * Verifies that it generates only lower-case names when flag is false.
     */
    @Test
    public void testConstructorWithMixedCaseFalse() {
        // Arrange & Act
        SimpleNameFactory factory = new SimpleNameFactory(false);

        // Assert - Generate many names and verify they're all lower case
        for (int i = 0; i < 100; i++) {
            String name = factory.nextName();
            assertEquals(name.toLowerCase(), name,
                    "Constructor(false) should generate only lower-case names, but got: " + name);
        }
    }

    /**
     * Tests that both constructors create valid instances.
     * Verifies that instances can be used immediately after construction.
     */
    @Test
    public void testConstructorsCreateValidInstances() {
        // Arrange & Act
        SimpleNameFactory factory1 = new SimpleNameFactory();
        SimpleNameFactory factory2 = new SimpleNameFactory(true);
        SimpleNameFactory factory3 = new SimpleNameFactory(false);

        // Assert - All should be able to generate names
        assertNotNull(factory1.nextName());
        assertNotNull(factory2.nextName());
        assertNotNull(factory3.nextName());
    }

    // ========== reset() Tests ==========

    /**
     * Tests that reset() resets the name sequence.
     * Verifies that after reset, nextName returns the first name again.
     */
    @Test
    public void testReset() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory();
        String firstName = factory.nextName();
        factory.nextName();
        factory.nextName();

        // Act
        factory.reset();
        String nameAfterReset = factory.nextName();

        // Assert
        assertEquals(firstName, nameAfterReset,
                "After reset, nextName should return the first name again");
    }

    /**
     * Tests reset() on a factory with mixed-case names.
     * Verifies the sequence restarts correctly.
     */
    @Test
    public void testResetMixedCase() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory(true);

        // Act - Get first few names
        String name1 = factory.nextName();
        String name2 = factory.nextName();
        String name3 = factory.nextName();

        // Reset and get names again
        factory.reset();
        String nameAfterReset1 = factory.nextName();
        String nameAfterReset2 = factory.nextName();
        String nameAfterReset3 = factory.nextName();

        // Assert
        assertEquals(name1, nameAfterReset1, "First name after reset should match original first name");
        assertEquals(name2, nameAfterReset2, "Second name after reset should match original second name");
        assertEquals(name3, nameAfterReset3, "Third name after reset should match original third name");
    }

    /**
     * Tests reset() on a factory with lower-case only names.
     * Verifies the sequence restarts correctly.
     */
    @Test
    public void testResetLowerCaseOnly() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory(false);

        // Act - Get first few names
        String name1 = factory.nextName();
        String name2 = factory.nextName();

        // Reset and get names again
        factory.reset();
        String nameAfterReset1 = factory.nextName();
        String nameAfterReset2 = factory.nextName();

        // Assert
        assertEquals(name1, nameAfterReset1);
        assertEquals(name2, nameAfterReset2);
    }

    /**
     * Tests that reset() can be called multiple times.
     * Verifies that consecutive resets work correctly.
     */
    @Test
    public void testResetMultipleTimes() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory();
        String firstName = factory.nextName();

        // Act - Reset multiple times
        factory.nextName();
        factory.reset();
        String afterFirst = factory.nextName();

        factory.nextName();
        factory.reset();
        String afterSecond = factory.nextName();

        factory.nextName();
        factory.reset();
        String afterThird = factory.nextName();

        // Assert
        assertEquals(firstName, afterFirst);
        assertEquals(firstName, afterSecond);
        assertEquals(firstName, afterThird);
    }

    /**
     * Tests reset() after generating many names.
     * Verifies that reset works even after a long sequence.
     */
    @Test
    public void testResetAfterManyNames() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory();
        String firstName = factory.nextName();

        // Generate many names
        for (int i = 0; i < 1000; i++) {
            factory.nextName();
        }

        // Act
        factory.reset();
        String nameAfterReset = factory.nextName();

        // Assert
        assertEquals(firstName, nameAfterReset);
    }

    /**
     * Tests reset() immediately after construction.
     * Verifies that reset on a fresh factory doesn't cause issues.
     */
    @Test
    public void testResetImmediatelyAfterConstruction() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory();

        // Act
        factory.reset();
        String name = factory.nextName();

        // Assert
        assertNotNull(name);
        assertFalse(name.isEmpty());
    }

    // ========== nextName() Tests ==========

    /**
     * Tests that nextName() returns a non-null value.
     * Verifies basic contract of the method.
     */
    @Test
    public void testNextNameReturnsNonNull() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory();

        // Act
        String name = factory.nextName();

        // Assert
        assertNotNull(name, "nextName should never return null");
    }

    /**
     * Tests that nextName() returns a non-empty string.
     * Verifies that generated names are usable.
     */
    @Test
    public void testNextNameReturnsNonEmpty() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory();

        // Act
        String name = factory.nextName();

        // Assert
        assertFalse(name.isEmpty(), "nextName should never return an empty string");
        assertTrue(name.length() > 0, "nextName should return a string with length > 0");
    }

    /**
     * Tests that nextName() generates unique names.
     * Verifies that each call returns a different value.
     */
    @Test
    public void testNextNameGeneratesUniqueNames() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory();
        Set<String> generatedNames = new HashSet<>();

        // Act - Generate 200 names
        for (int i = 0; i < 200; i++) {
            String name = factory.nextName();
            generatedNames.add(name);
        }

        // Assert - All names should be unique
        assertEquals(200, generatedNames.size(),
                "All 200 generated names should be unique");
    }

    /**
     * Tests nextName() sequence with mixed-case mode.
     * Verifies the initial sequence: a, b, c, ..., z, A, B, C, ..., Z, aa, ...
     */
    @Test
    public void testNextNameSequenceMixedCase() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory(true);

        // Act & Assert - First 26 should be lowercase a-z
        assertEquals("a", factory.nextName());
        assertEquals("b", factory.nextName());
        assertEquals("c", factory.nextName());

        // Skip to near end of lowercase
        for (int i = 3; i < 25; i++) {
            factory.nextName();
        }
        assertEquals("z", factory.nextName()); // 26th name

        // Next 26 should be uppercase A-Z
        assertEquals("A", factory.nextName()); // 27th name
        assertEquals("B", factory.nextName());

        // Skip ahead
        for (int i = 2; i < 25; i++) {
            factory.nextName();
        }
        assertEquals("Z", factory.nextName()); // 52nd name

        // After exhausting single chars, should start with two chars
        String name53 = factory.nextName();
        assertEquals(2, name53.length(), "After 52 single-char names, should generate 2-char names");
        assertEquals("aa", name53);
    }

    /**
     * Tests nextName() sequence with lower-case only mode.
     * Verifies the sequence: a, b, c, ..., z, aa, ab, ...
     */
    @Test
    public void testNextNameSequenceLowerCaseOnly() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory(false);

        // Act & Assert - First 26 should be lowercase a-z
        assertEquals("a", factory.nextName());
        assertEquals("b", factory.nextName());
        assertEquals("c", factory.nextName());

        // Skip ahead
        for (int i = 3; i < 25; i++) {
            factory.nextName();
        }
        assertEquals("z", factory.nextName()); // 26th name

        // After exhausting single chars, should start with two chars
        String name27 = factory.nextName();
        assertEquals(2, name27.length(), "After 26 single-char names, should generate 2-char names");
        assertEquals("aa", name27);
    }

    /**
     * Tests that nextName() avoids Windows reserved names.
     * Verifies that reserved names like AUX, CON, NUL, PRN are modified.
     */
    @Test
    public void testNextNameAvoidsReservedNames() {
        // Arrange - We need to find where these reserved names would appear
        SimpleNameFactory factory = new SimpleNameFactory(true);

        // Act - Generate many names and check for reserved names
        Set<String> generatedNames = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            String name = factory.nextName();
            generatedNames.add(name);
        }

        // Assert - Reserved names should not appear exactly (they should be modified)
        // However, the code adds a character to them, so "AUX" becomes "AUXA" or similar
        // We verify that if these patterns would appear, they're handled
        assertFalse(generatedNames.contains("AUX"), "Should not generate 'AUX' exactly");
        assertFalse(generatedNames.contains("CON"), "Should not generate 'CON' exactly");
        assertFalse(generatedNames.contains("NUL"), "Should not generate 'NUL' exactly");
        assertFalse(generatedNames.contains("PRN"), "Should not generate 'PRN' exactly");

        // Also check lowercase versions shouldn't appear alone
        assertFalse(generatedNames.contains("aux"), "Should not generate 'aux' exactly");
        assertFalse(generatedNames.contains("con"), "Should not generate 'con' exactly");
        assertFalse(generatedNames.contains("nul"), "Should not generate 'nul' exactly");
        assertFalse(generatedNames.contains("prn"), "Should not generate 'prn' exactly");
    }

    /**
     * Tests nextName() generates valid Java identifier characters.
     * Verifies that all generated names contain only letters.
     */
    @Test
    public void testNextNameGeneratesOnlyLetters() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory();

        // Act & Assert - Generate many names
        for (int i = 0; i < 500; i++) {
            String name = factory.nextName();
            assertTrue(name.matches("[a-zA-Z]+"),
                    "Name '" + name + "' should contain only letters");
        }
    }

    /**
     * Tests nextName() can generate many names without errors.
     * Verifies stability over long sequences.
     */
    @Test
    public void testNextNameGeneratesManyNames() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory();

        // Act & Assert - Generate 5000 names without exception
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5000; i++) {
                String name = factory.nextName();
                assertNotNull(name);
                assertFalse(name.isEmpty());
            }
        });
    }

    /**
     * Tests nextName() with multiple factory instances.
     * Verifies that each instance maintains independent state.
     */
    @Test
    public void testNextNameWithMultipleInstances() {
        // Arrange
        SimpleNameFactory factory1 = new SimpleNameFactory(true);
        SimpleNameFactory factory2 = new SimpleNameFactory(true);

        // Act
        String name1_1 = factory1.nextName();
        String name2_1 = factory2.nextName();
        String name1_2 = factory1.nextName();
        String name2_2 = factory2.nextName();

        // Assert - Both should generate the same sequence independently
        assertEquals(name1_1, name2_1, "Both factories should generate same first name");
        assertEquals(name1_2, name2_2, "Both factories should generate same second name");
    }

    /**
     * Tests nextName() through the NameFactory interface.
     * Verifies polymorphic behavior works correctly.
     */
    @Test
    public void testNextNameThroughInterface() {
        // Arrange
        NameFactory factory = new SimpleNameFactory();

        // Act
        String name1 = factory.nextName();
        String name2 = factory.nextName();

        // Assert
        assertNotNull(name1);
        assertNotNull(name2);
        assertNotEquals(name1, name2, "Consecutive names should be different");
    }

    /**
     * Tests that nextName() generates progressively longer names.
     * Verifies that name length increases as more names are generated.
     */
    @Test
    public void testNextNameLengthProgression() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory(true);

        // Act - Generate 52 single-char names (a-z, A-Z in mixed case)
        for (int i = 0; i < 52; i++) {
            String name = factory.nextName();
            assertEquals(1, name.length(), "First 52 names should be single character");
        }

        // Act - Next names should be 2 characters
        for (int i = 0; i < 100; i++) {
            String name = factory.nextName();
            assertTrue(name.length() >= 2, "After 52 names, names should be at least 2 characters");
        }
    }

    /**
     * Tests nextName() consistency with lower-case mode.
     * Verifies that lower-case mode never generates uppercase.
     */
    @Test
    public void testNextNameLowerCaseConsistency() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory(false);

        // Act & Assert - Generate many names
        for (int i = 0; i < 1000; i++) {
            String name = factory.nextName();
            assertEquals(name, name.toLowerCase(),
                    "Lower-case mode should never generate uppercase letters");
            assertFalse(name.matches(".*[A-Z].*"),
                    "Name should not contain any uppercase letters");
        }
    }

    /**
     * Tests nextName() behavior after reset cycles.
     * Verifies that reset and nextName work correctly together.
     */
    @Test
    public void testNextNameAfterResetCycles() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory();

        // Act & Assert - Multiple reset cycles
        for (int cycle = 0; cycle < 5; cycle++) {
            String name1 = factory.nextName();
            String name2 = factory.nextName();
            String name3 = factory.nextName();

            assertNotNull(name1);
            assertNotNull(name2);
            assertNotNull(name3);
            assertNotEquals(name1, name2);
            assertNotEquals(name2, name3);

            factory.reset();
        }
    }

    /**
     * Tests that nextName() starts with single character.
     * Verifies the first name is always a single lowercase letter.
     */
    @Test
    public void testNextNameFirstNameIsSingleChar() {
        // Arrange
        SimpleNameFactory factory1 = new SimpleNameFactory(true);
        SimpleNameFactory factory2 = new SimpleNameFactory(false);

        // Act
        String mixedCaseFirst = factory1.nextName();
        String lowerCaseFirst = factory2.nextName();

        // Assert
        assertEquals(1, mixedCaseFirst.length(), "First name should be single character");
        assertEquals("a", mixedCaseFirst, "First name should be 'a'");
        assertEquals(1, lowerCaseFirst.length(), "First name should be single character");
        assertEquals("a", lowerCaseFirst, "First name should be 'a'");
    }

    /**
     * Tests nextName() for deterministic behavior.
     * Verifies that the same configuration produces the same sequence.
     */
    @Test
    public void testNextNameDeterministic() {
        // Arrange
        SimpleNameFactory factory1 = new SimpleNameFactory(true);
        SimpleNameFactory factory2 = new SimpleNameFactory(true);

        // Act - Generate sequences from both
        String[] sequence1 = new String[50];
        String[] sequence2 = new String[50];
        for (int i = 0; i < 50; i++) {
            sequence1[i] = factory1.nextName();
            sequence2[i] = factory2.nextName();
        }

        // Assert - Both sequences should be identical
        assertArrayEquals(sequence1, sequence2,
                "Same configuration should produce identical sequences");
    }

    /**
     * Tests nextName() difference between mixed and lower-case modes.
     * Verifies that the modes produce different sequences.
     */
    @Test
    public void testNextNameMixedVsLowerCaseDifference() {
        // Arrange
        SimpleNameFactory mixedFactory = new SimpleNameFactory(true);
        SimpleNameFactory lowerFactory = new SimpleNameFactory(false);

        // Act - Skip past the first 26 common names (a-z)
        for (int i = 0; i < 26; i++) {
            assertEquals(mixedFactory.nextName(), lowerFactory.nextName(),
                    "First 26 names should be the same (a-z)");
        }

        // Act - Get the 27th name
        String mixedName27 = mixedFactory.nextName();
        String lowerName27 = lowerFactory.nextName();

        // Assert - Mixed should have 'A', lower should have 'aa'
        assertEquals("A", mixedName27, "Mixed-case 27th name should be 'A'");
        assertEquals("aa", lowerName27, "Lower-case 27th name should be 'aa'");
    }

    // ========== main() Tests ==========

    /**
     * Tests the main method executes without errors.
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
            assertDoesNotThrow(() -> SimpleNameFactory.main(new String[]{}));

            // Assert
            String output = outputStream.toString();
            assertFalse(output.isEmpty(), "Main should produce output");

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method produces expected output structure.
     * Verifies that main prints samples as documented.
     */
    @Test
    public void testMainProducesExpectedOutput() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            SimpleNameFactory.main(new String[]{});

            // Assert
            String output = outputStream.toString();
            assertTrue(output.contains("Some mixed-case names:"),
                    "Output should contain 'Some mixed-case names:'");
            assertTrue(output.contains("Some lower-case names:"),
                    "Output should contain 'Some lower-case names:'");
            assertTrue(output.contains("Some more mixed-case names:"),
                    "Output should contain 'Some more mixed-case names:'");
            assertTrue(output.contains("Some more lower-case names:"),
                    "Output should contain 'Some more lower-case names:'");

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method prints names in brackets.
     * Verifies the format of printed names.
     */
    @Test
    public void testMainPrintsNamesInBrackets() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            SimpleNameFactory.main(new String[]{});

            // Assert
            String output = outputStream.toString();
            assertTrue(output.contains("[a]"), "Output should contain '[a]'");
            assertTrue(output.contains("[b]"), "Output should contain '[b]'");
            assertTrue(output.contains("[c]"), "Output should contain '[c]'");

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method prints the correct number of samples.
     * Verifies that the right number of names are printed for each section.
     */
    @Test
    public void testMainPrintsCorrectNumberOfSamples() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            SimpleNameFactory.main(new String[]{});

            // Assert
            String output = outputStream.toString();
            String[] lines = output.split("\n");

            // Count lines with brackets (actual name samples)
            long bracketLines = 0;
            for (String line : lines) {
                if (line.trim().matches("\\[.*\\]")) {
                    bracketLines++;
                }
            }

            // Should have 60 + 60 + 80 + 80 = 280 name samples
            assertEquals(280, bracketLines,
                    "Should print 280 name samples (60+60+80+80)");

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method can be called with null args.
     * Verifies robustness of main method.
     */
    @Test
    public void testMainWithNullArgs() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act - Main doesn't use args, so null should be fine
            assertDoesNotThrow(() -> SimpleNameFactory.main(null));

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method can be called with empty args.
     * Verifies standard main method signature behavior.
     */
    @Test
    public void testMainWithEmptyArgs() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act
            assertDoesNotThrow(() -> SimpleNameFactory.main(new String[]{}));

            // Assert
            String output = outputStream.toString();
            assertFalse(output.isEmpty());

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method can be called with arbitrary args.
     * Verifies that main ignores arguments it doesn't use.
     */
    @Test
    public void testMainWithArbitraryArgs() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Act - Main doesn't use args
            assertDoesNotThrow(() -> SimpleNameFactory.main(new String[]{"arg1", "arg2", "arg3"}));

            // Assert
            String output = outputStream.toString();
            assertFalse(output.isEmpty());

        } finally {
            // Cleanup
            System.setOut(originalOut);
        }
    }

    // ========== Integration Tests ==========

    /**
     * Tests the complete lifecycle of a SimpleNameFactory.
     * Verifies constructor, nextName, reset work together correctly.
     */
    @Test
    public void testCompleteLifecycle() {
        // Arrange
        SimpleNameFactory factory = new SimpleNameFactory(true);

        // Act & Assert - Generate, reset, generate again
        String name1 = factory.nextName();
        String name2 = factory.nextName();
        String name3 = factory.nextName();

        assertNotNull(name1);
        assertNotNull(name2);
        assertNotNull(name3);

        factory.reset();

        String nameAfterReset1 = factory.nextName();
        String nameAfterReset2 = factory.nextName();
        String nameAfterReset3 = factory.nextName();

        assertEquals(name1, nameAfterReset1);
        assertEquals(name2, nameAfterReset2);
        assertEquals(name3, nameAfterReset3);
    }

    /**
     * Tests NameFactory interface compliance.
     * Verifies that SimpleNameFactory properly implements NameFactory.
     */
    @Test
    public void testNameFactoryInterfaceCompliance() {
        // Arrange
        NameFactory factory = new SimpleNameFactory();

        // Act - Use interface methods
        String name1 = factory.nextName();
        String name2 = factory.nextName();
        factory.reset();
        String name3 = factory.nextName();

        // Assert
        assertNotNull(name1);
        assertNotNull(name2);
        assertEquals(name1, name3, "After reset, should get first name again");
    }
}
