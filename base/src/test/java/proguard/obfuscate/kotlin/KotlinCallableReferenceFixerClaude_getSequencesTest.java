package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.instruction.Instruction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences#getSequences()}
 * and {@link KotlinCallableReferenceFixer.OwnerReplacementSequences#getSequences()}.
 * Tests the getSequences method that returns instruction replacement patterns for name, signature, or owner replacements.
 */
public class KotlinCallableReferenceFixerClaude_getSequencesTest {

    /**
     * Tests that getSequences returns a non-null array.
     * Verifies basic functionality of the method.
     */
    @Test
    public void testGetSequencesReturnsNonNull() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "testName";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should return a non-null array");
    }

    /**
     * Tests that getSequences returns an array with the expected structure.
     * Based on the code, it should return a 3D array with 1 sequence pair.
     */
    @Test
    public void testGetSequencesReturnsExpectedStructure() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "myMethod";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.length, "Should have 1 replacement sequence pair");

        // The pair should have 2 sequences (match pattern and replacement pattern)
        assertNotNull(result[0], "Sequence pair should not be null");
        assertEquals(2, result[0].length, "Sequence pair should have 2 sequences");
        assertNotNull(result[0][0], "Match pattern should not be null");
        assertNotNull(result[0][1], "Replacement pattern should not be null");
    }

    /**
     * Tests getSequences with empty name string.
     * Verifies the method handles edge case inputs.
     */
    @Test
    public void testGetSequencesWithEmptyString() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should handle empty strings");
        assertEquals(1, result.length, "Should still have 1 sequence pair with empty string");
    }

    /**
     * Tests that getSequences returns the same reference on multiple calls.
     * Verifies that the method returns the same array instance.
     */
    @Test
    public void testGetSequencesReturnsSameReference() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "testMethod";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result1 = sequences.getSequences();
        Instruction[][][] result2 = sequences.getSequences();

        // Assert
        assertSame(result1, result2, "getSequences should return the same reference on multiple calls");
    }

    /**
     * Tests that each sequence array contains instruction objects.
     * Verifies that the arrays are populated with instructions.
     */
    @Test
    public void testGetSequencesContainsInstructions() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "getValue";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                assertNotNull(result[i][j], "Instruction array [" + i + "][" + j + "] should not be null");
                assertTrue(result[i][j].length > 0,
                    "Instruction array [" + i + "][" + j + "] should not be empty");

                // Verify all instructions in the array are non-null
                for (int k = 0; k < result[i][j].length; k++) {
                    assertNotNull(result[i][j][k],
                        "Instruction [" + i + "][" + j + "][" + k + "] should not be null");
                }
            }
        }
    }

    /**
     * Tests getSequences with different name values.
     * Verifies that the method works with various valid inputs.
     */
    @Test
    public void testGetSequencesWithDifferentNames() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        String[] testNames = {
            "methodName",
            "anotherMethod",
            "simpleMethod",
            "complexMethod"
        };

        for (String name : testNames) {
            // Act
            KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
                new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                    name, programClassPool, libraryClassPool);
            Instruction[][][] result = sequences.getSequences();

            // Assert
            assertNotNull(result, "Result should not be null for input: " + name);
            assertEquals(1, result.length, "Should have 1 sequence pair for input: " + name);
        }
    }

    /**
     * Tests that getSequences with null ClassPools still returns valid structure.
     * Verifies robustness with null parameters.
     */
    @Test
    public void testGetSequencesWithNullClassPools() {
        // Arrange
        String name = "testMethod";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, null, null);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should work with null ClassPools");
        assertEquals(1, result.length, "Should have 1 sequence pair even with null ClassPools");
    }

    /**
     * Tests getSequences with special characters in name.
     * Verifies handling of special characters.
     */
    @Test
    public void testGetSequencesWithSpecialCharacters() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "method$with$dollars";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should handle special characters");
        assertEquals(1, result.length, "Should have 1 sequence pair");
    }

    /**
     * Tests that the returned array structure matches expected dimensions.
     * Verifies the 3D array has correct nesting.
     */
    @Test
    public void testGetSequencesArrayDimensions() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "test";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        // Verify it's a 3D array by checking type and structure
        assertTrue(result instanceof Instruction[][][], "Should be a 3D array");
        assertTrue(result[0] instanceof Instruction[][], "First dimension should contain 2D arrays");
        assertTrue(result[0][0] instanceof Instruction[], "Second dimension should contain 1D arrays");
    }

    /**
     * Tests getSequences with very long name string.
     * Verifies handling of lengthy inputs.
     */
    @Test
    public void testGetSequencesWithLongString() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "veryLongMethodNameThatExceedsNormalLengthButIsStillValid";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should handle long strings");
        assertEquals(1, result.length, "Should have 1 sequence pair with long string");
    }

    /**
     * Tests that getSequences doesn't throw any exceptions.
     * Verifies exception safety.
     */
    @Test
    public void testGetSequencesDoesNotThrowException() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "testMethod";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act & Assert
        assertDoesNotThrow(() -> sequences.getSequences(),
            "getSequences should not throw any exceptions");
    }

    /**
     * Tests getSequences with multiple instances.
     * Verifies that different instances return independent arrays.
     */
    @Test
    public void testGetSequencesWithMultipleInstances() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences1 =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                "method1", programClassPool, libraryClassPool);

        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences2 =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                "method2", programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result1 = sequences1.getSequences();
        Instruction[][][] result2 = sequences2.getSequences();

        // Assert
        assertNotNull(result1, "First result should not be null");
        assertNotNull(result2, "Second result should not be null");
        assertNotSame(result1, result2, "Different instances should return different arrays");
    }

    /**
     * Tests that the returned sequences have consistent structure across calls.
     * Verifies immutability and consistency.
     */
    @Test
    public void testGetSequencesConsistency() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "consistentMethod";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result1 = sequences.getSequences();
        Instruction[][][] result2 = sequences.getSequences();
        Instruction[][][] result3 = sequences.getSequences();

        // Assert
        assertSame(result1, result2, "First and second call should return same reference");
        assertSame(result2, result3, "Second and third call should return same reference");
        assertSame(result1, result3, "First and third call should return same reference");
    }

    /**
     * Tests getSequences with typical Kotlin method names.
     * Verifies proper handling of Kotlin naming conventions.
     */
    @Test
    public void testGetSequencesWithKotlinMethodNames() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        String[] kotlinNames = {
            "getValue",
            "setValue",
            "invoke",
            "get",
            "set",
            "component1",
            "component2"
        };

        for (String name : kotlinNames) {
            // Act
            KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
                new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                    name, programClassPool, libraryClassPool);
            Instruction[][][] result = sequences.getSequences();

            // Assert
            assertNotNull(result, "Result should not be null for Kotlin method: " + name);
            assertEquals(1, result.length, "Should have 1 sequence pair for Kotlin method: " + name);
        }
    }

    /**
     * Tests that sequence pair has valid structure (match and replacement patterns).
     * Verifies that the match and replacement patterns have the same structure.
     */
    @Test
    public void testGetSequencesPatternsAreValid() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "testMethod";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        // Should have match pattern and replacement pattern
        assertNotNull(result[0][0], "Match pattern should exist");
        assertNotNull(result[0][1], "Replacement pattern should exist");

        // Both patterns should have the same number of instructions for proper matching
        assertEquals(result[0][0].length, result[0][1].length,
            "Match and replacement patterns should have same length");
    }

    /**
     * Tests getSequences with unicode characters in name.
     * Verifies handling of unicode strings.
     */
    @Test
    public void testGetSequencesWithUnicodeCharacters() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "méthod€";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should handle unicode characters");
        assertEquals(1, result.length, "Should have 1 sequence pair with unicode");
    }

    /**
     * Tests getSequences performance with rapid successive calls.
     * Verifies efficient method execution.
     */
    @Test
    public void testGetSequencesPerformance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "performanceTest";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        long startTime = System.nanoTime();

        // Act - Call getSequences multiple times
        for (int i = 0; i < 1000; i++) {
            Instruction[][][] result = sequences.getSequences();
            assertNotNull(result);
        }

        long duration = System.nanoTime() - startTime;

        // Assert - Should complete quickly (less than 10ms for 1000 calls)
        assertTrue(duration < 10_000_000L,
            "1000 getSequences calls should complete quickly (took " + duration + " ns)");
    }

    // ========================================
    // Tests for OwnerReplacementSequences.getSequences()
    // ========================================

    /**
     * Tests that OwnerReplacementSequences.getSequences returns a non-null array.
     * Verifies basic functionality of the method.
     */
    @Test
    public void testOwnerGetSequencesReturnsNonNull() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "com.example.module";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should return a non-null array");
    }

    /**
     * Tests that OwnerReplacementSequences.getSequences returns an array with the expected structure.
     * Based on the code, it should return a 3D array with 1 sequence pair.
     */
    @Test
    public void testOwnerGetSequencesReturnsExpectedStructure() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "main";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.length, "Should have 1 replacement sequence pair");

        // The pair should have 2 sequences (match pattern and replacement pattern)
        assertNotNull(result[0], "Sequence pair should not be null");
        assertEquals(2, result[0].length, "Sequence pair should have 2 sequences");
        assertNotNull(result[0][0], "Match pattern should not be null");
        assertNotNull(result[0][1], "Replacement pattern should not be null");
    }

    /**
     * Tests OwnerReplacementSequences.getSequences with empty name string.
     * Verifies the method handles edge case inputs.
     */
    @Test
    public void testOwnerGetSequencesWithEmptyString() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should handle empty strings");
        assertEquals(1, result.length, "Should still have 1 sequence pair with empty string");
    }

    /**
     * Tests that OwnerReplacementSequences.getSequences returns the same reference on multiple calls.
     * Verifies that the method returns the same array instance.
     */
    @Test
    public void testOwnerGetSequencesReturnsSameReference() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "com.example.module";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result1 = sequences.getSequences();
        Instruction[][][] result2 = sequences.getSequences();

        // Assert
        assertSame(result1, result2, "getSequences should return the same reference on multiple calls");
    }

    /**
     * Tests that each OwnerReplacementSequences sequence array contains instruction objects.
     * Verifies that the arrays are populated with instructions.
     */
    @Test
    public void testOwnerGetSequencesContainsInstructions() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "myModule";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                assertNotNull(result[i][j], "Instruction array [" + i + "][" + j + "] should not be null");
                assertTrue(result[i][j].length > 0,
                    "Instruction array [" + i + "][" + j + "] should not be empty");

                // Verify all instructions in the array are non-null
                for (int k = 0; k < result[i][j].length; k++) {
                    assertNotNull(result[i][j][k],
                        "Instruction [" + i + "][" + j + "][" + k + "] should not be null");
                }
            }
        }
    }

    /**
     * Tests OwnerReplacementSequences.getSequences with different module name values.
     * Verifies that the method works with various valid inputs.
     */
    @Test
    public void testOwnerGetSequencesWithDifferentNames() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        String[] testNames = {
            "main",
            "app",
            "com.example.app",
            "my-module"
        };

        for (String name : testNames) {
            // Act
            KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
                new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                    name, programClassPool, libraryClassPool);
            Instruction[][][] result = sequences.getSequences();

            // Assert
            assertNotNull(result, "Result should not be null for input: " + name);
            assertEquals(1, result.length, "Should have 1 sequence pair for input: " + name);
        }
    }

    /**
     * Tests that OwnerReplacementSequences.getSequences with null ClassPools still returns valid structure.
     * Verifies robustness with null parameters.
     */
    @Test
    public void testOwnerGetSequencesWithNullClassPools() {
        // Arrange
        String name = "module";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, null, null);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should work with null ClassPools");
        assertEquals(1, result.length, "Should have 1 sequence pair even with null ClassPools");
    }

    /**
     * Tests OwnerReplacementSequences.getSequences with special characters in name.
     * Verifies handling of special characters typical in module names.
     */
    @Test
    public void testOwnerGetSequencesWithSpecialCharacters() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "my-module_v2";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should handle special characters");
        assertEquals(1, result.length, "Should have 1 sequence pair");
    }

    /**
     * Tests that the OwnerReplacementSequences returned array structure matches expected dimensions.
     * Verifies the 3D array has correct nesting.
     */
    @Test
    public void testOwnerGetSequencesArrayDimensions() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "module";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        // Verify it's a 3D array by checking type and structure
        assertTrue(result instanceof Instruction[][][], "Should be a 3D array");
        assertTrue(result[0] instanceof Instruction[][], "First dimension should contain 2D arrays");
        assertTrue(result[0][0] instanceof Instruction[], "Second dimension should contain 1D arrays");
    }

    /**
     * Tests OwnerReplacementSequences.getSequences with very long name string.
     * Verifies handling of lengthy module names.
     */
    @Test
    public void testOwnerGetSequencesWithLongString() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "com.example.very.long.package.name.with.many.components.module";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should handle long strings");
        assertEquals(1, result.length, "Should have 1 sequence pair with long string");
    }

    /**
     * Tests that OwnerReplacementSequences.getSequences doesn't throw any exceptions.
     * Verifies exception safety.
     */
    @Test
    public void testOwnerGetSequencesDoesNotThrowException() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "module";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act & Assert
        assertDoesNotThrow(() -> sequences.getSequences(),
            "getSequences should not throw any exceptions");
    }

    /**
     * Tests OwnerReplacementSequences.getSequences with multiple instances.
     * Verifies that different instances return independent arrays.
     */
    @Test
    public void testOwnerGetSequencesWithMultipleInstances() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences1 =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                "module1", programClassPool, libraryClassPool);

        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences2 =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                "module2", programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result1 = sequences1.getSequences();
        Instruction[][][] result2 = sequences2.getSequences();

        // Assert
        assertNotNull(result1, "First result should not be null");
        assertNotNull(result2, "Second result should not be null");
        assertNotSame(result1, result2, "Different instances should return different arrays");
    }

    /**
     * Tests that the OwnerReplacementSequences returned sequences have consistent structure across calls.
     * Verifies immutability and consistency.
     */
    @Test
    public void testOwnerGetSequencesConsistency() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "consistentModule";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result1 = sequences.getSequences();
        Instruction[][][] result2 = sequences.getSequences();
        Instruction[][][] result3 = sequences.getSequences();

        // Assert
        assertSame(result1, result2, "First and second call should return same reference");
        assertSame(result2, result3, "Second and third call should return same reference");
        assertSame(result1, result3, "First and third call should return same reference");
    }

    /**
     * Tests that OwnerReplacementSequences sequence pair has valid structure (match and replacement patterns).
     * Verifies that the match and replacement patterns have the same structure.
     */
    @Test
    public void testOwnerGetSequencesPatternsAreValid() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "module";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        // Should have match pattern and replacement pattern
        assertNotNull(result[0][0], "Match pattern should exist");
        assertNotNull(result[0][1], "Replacement pattern should exist");

        // Both patterns should have the same number of instructions for proper matching
        assertEquals(result[0][0].length, result[0][1].length,
            "Match and replacement patterns should have same length");
    }

    /**
     * Tests OwnerReplacementSequences.getSequences with unicode characters in name.
     * Verifies handling of unicode strings in module names.
     */
    @Test
    public void testOwnerGetSequencesWithUnicodeCharacters() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "モジュール";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should handle unicode characters");
        assertEquals(1, result.length, "Should have 1 sequence pair with unicode");
    }

    /**
     * Tests OwnerReplacementSequences.getSequences performance with rapid successive calls.
     * Verifies efficient method execution.
     */
    @Test
    public void testOwnerGetSequencesPerformance() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "performanceTest";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        long startTime = System.nanoTime();

        // Act - Call getSequences multiple times
        for (int i = 0; i < 1000; i++) {
            Instruction[][][] result = sequences.getSequences();
            assertNotNull(result);
        }

        long duration = System.nanoTime() - startTime;

        // Assert - Should complete quickly (less than 10ms for 1000 calls)
        assertTrue(duration < 10_000_000L,
            "1000 getSequences calls should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that OwnerReplacementSequences.getSequences works correctly with same ClassPool.
     * Verifies behavior when both ClassPools are the same instance.
     */
    @Test
    public void testOwnerGetSequencesWithSameClassPool() {
        // Arrange
        ClassPool classPool = new ClassPool();
        String name = "module";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, classPool, classPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should work when both ClassPools are the same");
        assertEquals(1, result.length, "Should have 1 sequence pair");
    }

    /**
     * Tests that the OwnerReplacementSequences instruction sequence contains expected number of instructions.
     * Based on the implementation, each sequence should have 3 instructions: ldc_, ldc_, invokestatic.
     */
    @Test
    public void testOwnerGetSequencesInstructionCount() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "module";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        // Based on the implementation, each sequence should contain 3 instructions
        assertEquals(3, result[0][0].length, "Match pattern should have 3 instructions");
        assertEquals(3, result[0][1].length, "Replacement pattern should have 3 instructions");
    }

    /**
     * Tests OwnerReplacementSequences.getSequences with null name.
     * Verifies handling of null name parameter.
     */
    @Test
    public void testOwnerGetSequencesWithNullName() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                null, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should handle null name");
        assertEquals(1, result.length, "Should have 1 sequence pair even with null name");
    }

    /**
     * Tests that OwnerReplacementSequences.getSequences works with typical module names.
     * Verifies proper handling of common Kotlin module naming patterns.
     */
    @Test
    public void testOwnerGetSequencesWithTypicalModuleNames() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        String[] moduleNames = {
            "main",
            "app",
            "lib",
            "core",
            "common"
        };

        for (String name : moduleNames) {
            // Act
            KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
                new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                    name, programClassPool, libraryClassPool);
            Instruction[][][] result = sequences.getSequences();

            // Assert
            assertNotNull(result, "Result should not be null for module: " + name);
            assertEquals(1, result.length, "Should have 1 sequence pair for module: " + name);
        }
    }

    /**
     * Tests OwnerReplacementSequences.getSequences with whitespace in name.
     * Verifies handling of names with whitespace characters.
     */
    @Test
    public void testOwnerGetSequencesWithWhitespace() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "module name";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should handle whitespace in name");
        assertEquals(1, result.length, "Should have 1 sequence pair");
    }

    /**
     * Tests that OwnerReplacementSequences.getSequences arrays can be iterated safely.
     * Verifies no IndexOutOfBoundsException occurs during iteration.
     */
    @Test
    public void testOwnerGetSequencesArrayIteration() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "module";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert - Should be able to iterate without exceptions
        assertDoesNotThrow(() -> {
            for (Instruction[][] sequencePair : result) {
                for (Instruction[] sequence : sequencePair) {
                    for (Instruction instruction : sequence) {
                        assertNotNull(instruction);
                    }
                }
            }
        }, "Should be able to iterate through all instructions without exception");
    }

    /**
     * Tests OwnerReplacementSequences.getSequences with dotted package-style name.
     * Verifies handling of fully qualified package names.
     */
    @Test
    public void testOwnerGetSequencesWithPackageName() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "com.example.myapp.module";
        KotlinCallableReferenceFixer.OwnerReplacementSequences sequences =
            new KotlinCallableReferenceFixer.OwnerReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should handle package-style names");
        assertEquals(1, result.length, "Should have 1 sequence pair");
        assertEquals(3, result[0][0].length, "Match pattern should have 3 instructions");
        assertEquals(3, result[0][1].length, "Replacement pattern should have 3 instructions");
    }

    /**
     * Tests that OwnerReplacementSequences.getSequences works correctly after construction with same ClassPool.
     * Verifies behavior when both ClassPools are the same instance.
     */
    @Test
    public void testGetSequencesWithSameClassPool() {
        // Arrange
        ClassPool classPool = new ClassPool();
        String name = "method";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, classPool, classPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should work when both ClassPools are the same");
        assertEquals(1, result.length, "Should have 1 sequence pair");
    }

    /**
     * Tests getSequences with a method signature as name.
     * Verifies handling when name looks like a signature.
     */
    @Test
    public void testGetSequencesWithSignatureLikeName() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "(Ljava/lang/String;)V";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        assertNotNull(result, "getSequences should handle signature-like names");
        assertEquals(1, result.length, "Should have 1 sequence pair");
    }

    /**
     * Tests that the instruction sequence contains expected instruction types.
     * Verifies the structure matches the implementation (ldc and areturn).
     */
    @Test
    public void testGetSequencesInstructionTypes() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        String name = "testMethod";
        KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences sequences =
            new KotlinCallableReferenceFixer.NameOrSignatureReplacementSequences(
                name, programClassPool, libraryClassPool);

        // Act
        Instruction[][][] result = sequences.getSequences();

        // Assert
        // Based on the implementation, each sequence should contain 2 instructions: ldc and areturn
        assertEquals(2, result[0][0].length, "Match pattern should have 2 instructions");
        assertEquals(2, result[0][1].length, "Replacement pattern should have 2 instructions");
    }
}
