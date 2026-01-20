package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstructionSequenceConstants}.
 *
 * Tests all methods in InstructionSequenceConstants:
 * - {@code <init>(ClassPool, ClassPool)V} - Constructor that initializes instruction sequences
 * - {@code main([Ljava/lang/String;)V} - Main method that prints instruction sequences
 *
 * The InstructionSequenceConstants class contains instruction sequences used for peephole optimization.
 * The constructor builds a large set of instruction sequence patterns and their replacements.
 * The main method is a utility that prints these sequences for debugging/documentation purposes.
 */
public class InstructionSequenceConstantsClaudeTest {

    // ========================================
    // Constructor Tests
    // ========================================

    /**
     * Tests the constructor with empty class pools.
     * Verifies that the constructor successfully initializes all instruction sequence arrays
     * and constants when given empty class pools.
     */
    @Test
    public void testConstructor_withEmptyClassPools() {
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        InstructionSequenceConstants constants = new InstructionSequenceConstants(programClassPool, libraryClassPool);

        // Verify that all sequence arrays are initialized
        assertNotNull(constants.VARIABLE_SEQUENCES, "VARIABLE_SEQUENCES should be initialized");
        assertNotNull(constants.ARITHMETIC_SEQUENCES, "ARITHMETIC_SEQUENCES should be initialized");
        assertNotNull(constants.FIELD_SEQUENCES, "FIELD_SEQUENCES should be initialized");
        assertNotNull(constants.CAST_SEQUENCES, "CAST_SEQUENCES should be initialized");
        assertNotNull(constants.BRANCH_SEQUENCES, "BRANCH_SEQUENCES should be initialized");
        assertNotNull(constants.STRING_SEQUENCES, "STRING_SEQUENCES should be initialized");
        assertNotNull(constants.OBJECT_SEQUENCES, "OBJECT_SEQUENCES should be initialized");
        assertNotNull(constants.MATH_SEQUENCES, "MATH_SEQUENCES should be initialized");
        assertNotNull(constants.MATH_ANDROID_SEQUENCES, "MATH_ANDROID_SEQUENCES should be initialized");
        assertNotNull(constants.CONSTANTS, "CONSTANTS should be initialized");

        // Verify that the arrays contain sequences
        assertTrue(constants.VARIABLE_SEQUENCES.length > 0, "VARIABLE_SEQUENCES should contain sequences");
        assertTrue(constants.ARITHMETIC_SEQUENCES.length > 0, "ARITHMETIC_SEQUENCES should contain sequences");
        assertTrue(constants.FIELD_SEQUENCES.length > 0, "FIELD_SEQUENCES should contain sequences");
        assertTrue(constants.CAST_SEQUENCES.length > 0, "CAST_SEQUENCES should contain sequences");
        assertTrue(constants.BRANCH_SEQUENCES.length > 0, "BRANCH_SEQUENCES should contain sequences");
        assertTrue(constants.STRING_SEQUENCES.length > 0, "STRING_SEQUENCES should contain sequences");
        assertTrue(constants.OBJECT_SEQUENCES.length > 0, "OBJECT_SEQUENCES should contain sequences");
        assertTrue(constants.MATH_SEQUENCES.length > 0, "MATH_SEQUENCES should contain sequences");
        assertTrue(constants.MATH_ANDROID_SEQUENCES.length > 0, "MATH_ANDROID_SEQUENCES should contain sequences");
        assertTrue(constants.CONSTANTS.length > 0, "CONSTANTS should contain constants");
    }

    /**
     * Tests that the constructor can be called multiple times with the same class pools.
     * This verifies that the constructor doesn't have side effects that would prevent reuse.
     */
    @Test
    public void testConstructor_multipleInstances() {
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        InstructionSequenceConstants constants1 = new InstructionSequenceConstants(programClassPool, libraryClassPool);
        InstructionSequenceConstants constants2 = new InstructionSequenceConstants(programClassPool, libraryClassPool);

        // Both instances should be valid
        assertNotNull(constants1.CONSTANTS);
        assertNotNull(constants2.CONSTANTS);

        // The arrays should have the same lengths (they should be structurally identical)
        assertEquals(constants1.VARIABLE_SEQUENCES.length, constants2.VARIABLE_SEQUENCES.length);
        assertEquals(constants1.ARITHMETIC_SEQUENCES.length, constants2.ARITHMETIC_SEQUENCES.length);
        assertEquals(constants1.FIELD_SEQUENCES.length, constants2.FIELD_SEQUENCES.length);
        assertEquals(constants1.CAST_SEQUENCES.length, constants2.CAST_SEQUENCES.length);
        assertEquals(constants1.BRANCH_SEQUENCES.length, constants2.BRANCH_SEQUENCES.length);
        assertEquals(constants1.STRING_SEQUENCES.length, constants2.STRING_SEQUENCES.length);
        assertEquals(constants1.OBJECT_SEQUENCES.length, constants2.OBJECT_SEQUENCES.length);
        assertEquals(constants1.MATH_SEQUENCES.length, constants2.MATH_SEQUENCES.length);
        assertEquals(constants1.MATH_ANDROID_SEQUENCES.length, constants2.MATH_ANDROID_SEQUENCES.length);
    }

    /**
     * Tests the constructor with different class pool instances.
     * This verifies that the constructor works regardless of which class pools are provided.
     */
    @Test
    public void testConstructor_withDifferentClassPools() {
        ClassPool programClassPool1 = new ClassPool();
        ClassPool libraryClassPool1 = new ClassPool();

        ClassPool programClassPool2 = new ClassPool();
        ClassPool libraryClassPool2 = new ClassPool();

        InstructionSequenceConstants constants1 = new InstructionSequenceConstants(programClassPool1, libraryClassPool1);
        InstructionSequenceConstants constants2 = new InstructionSequenceConstants(programClassPool2, libraryClassPool2);

        // Both should be successfully initialized
        assertNotNull(constants1.CONSTANTS);
        assertNotNull(constants2.CONSTANTS);
    }

    /**
     * Tests that the constructor initializes sequences with proper structure.
     * Each sequence pair should have at least a pattern (and optionally a replacement).
     */
    @Test
    public void testConstructor_sequenceStructure() {
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        InstructionSequenceConstants constants = new InstructionSequenceConstants(programClassPool, libraryClassPool);

        // Verify that sequence arrays have proper structure
        // Each sequence pair should have at least one element (the pattern)
        for (int i = 0; i < constants.VARIABLE_SEQUENCES.length; i++) {
            assertNotNull(constants.VARIABLE_SEQUENCES[i], "Sequence pair " + i + " should not be null");
            assertTrue(constants.VARIABLE_SEQUENCES[i].length >= 1,
                      "Sequence pair " + i + " should have at least a pattern");
            assertTrue(constants.VARIABLE_SEQUENCES[i].length <= 2,
                      "Sequence pair " + i + " should have at most pattern and replacement");
        }
    }

    // ========================================
    // Main Method Tests
    // ========================================

    /**
     * Tests the main method with no arguments.
     * The main method prints out all instruction sequences for debugging/documentation.
     * We verify it runs without throwing exceptions.
     */
    @Test
    public void testMain_withNoArgs() {
        // Capture System.out to verify output is produced
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Should run without throwing exceptions
            assertDoesNotThrow(() -> InstructionSequenceConstants.main(new String[0]),
                             "main method should not throw exceptions");

            // Verify some output was produced
            String output = outputStream.toString();
            assertTrue(output.length() > 0, "main method should produce output");

            // The output should contain sequence delimiters
            assertTrue(output.contains("=>"), "Output should contain sequence replacement markers");
        } finally {
            // Restore System.out
            System.setOut(originalOut);
        }
    }

    /**
     * Tests the main method with empty string array.
     * This is functionally equivalent to no args but ensures the args parameter is handled.
     */
    @Test
    public void testMain_withEmptyArgs() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            assertDoesNotThrow(() -> InstructionSequenceConstants.main(new String[]{}),
                             "main method should handle empty args array");

            String output = outputStream.toString();
            assertTrue(output.length() > 0, "main method should produce output");
        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * Tests the main method with arguments (which are ignored).
     * The main method doesn't use the args parameter, so any arguments should be ignored.
     */
    @Test
    public void testMain_withIgnoredArgs() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            assertDoesNotThrow(() -> InstructionSequenceConstants.main(new String[]{"ignored", "args"}),
                             "main method should ignore arguments");

            String output = outputStream.toString();
            assertTrue(output.length() > 0, "main method should produce output");
        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that the main method produces expected output format.
     * The output should contain patterns and their replacements (or "delete").
     */
    @Test
    public void testMain_outputFormat() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            InstructionSequenceConstants.main(new String[0]);

            String output = outputStream.toString();

            // The output should contain the replacement marker
            assertTrue(output.contains("=>"), "Output should contain '=>' replacement markers");

            // The output should contain either replacement instructions or "delete"
            assertTrue(output.contains("delete") || output.contains("=>"),
                      "Output should show deletions or replacements");

        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that main method can be called multiple times.
     * This verifies that the method doesn't have side effects that prevent multiple calls.
     */
    @Test
    public void testMain_multipleCalls() {
        ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            // First call
            System.setOut(new PrintStream(outputStream1));
            assertDoesNotThrow(() -> InstructionSequenceConstants.main(new String[0]));
            String output1 = outputStream1.toString();

            // Second call
            System.setOut(new PrintStream(outputStream2));
            assertDoesNotThrow(() -> InstructionSequenceConstants.main(new String[0]));
            String output2 = outputStream2.toString();

            // Both should produce the same output
            assertEquals(output1, output2, "Multiple calls should produce identical output");

        } finally {
            System.setOut(originalOut);
        }
    }
}
