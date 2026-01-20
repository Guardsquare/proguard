package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.constant.Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link KotlinUnsupportedExceptionReplacementSequences#getConstants()}.
 * Tests the getConstants method:
 * getConstants.()[Lproguard/classfile/constant/Constant;
 */
public class KotlinUnsupportedExceptionReplacementSequencesClaude_getConstantsTest {

    /**
     * Tests that getConstants() returns a non-null array.
     * Verifies that the method always returns a valid Constant array.
     */
    @Test
    public void testGetConstantsReturnsNonNull() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act
        Constant[] constants = sequences.getConstants();

        // Assert
        assertNotNull(constants, "getConstants() should return a non-null array");
    }

    /**
     * Tests that getConstants() returns an array (not null).
     * Verifies that the return type is correctly a Constant array.
     */
    @Test
    public void testGetConstantsReturnsArray() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act
        Constant[] constants = sequences.getConstants();

        // Assert
        assertTrue(constants instanceof Constant[], "getConstants() should return a Constant array");
    }

    /**
     * Tests that multiple calls to getConstants() return the same reference.
     * Verifies that the method returns the same instance on each call.
     */
    @Test
    public void testGetConstantsReturnsSameReference() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act
        Constant[] constants1 = sequences.getConstants();
        Constant[] constants2 = sequences.getConstants();

        // Assert
        assertSame(constants1, constants2,
            "Multiple calls to getConstants() should return the same reference");
    }

    /**
     * Tests that getConstants() is consistent across multiple sequential calls.
     * Verifies that the method is idempotent.
     */
    @Test
    public void testGetConstantsIsConsistent() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act
        Constant[] constants1 = sequences.getConstants();
        Constant[] constants2 = sequences.getConstants();
        Constant[] constants3 = sequences.getConstants();

        // Assert
        assertSame(constants1, constants2, "First and second calls should return same reference");
        assertSame(constants2, constants3, "Second and third calls should return same reference");
        assertSame(constants1, constants3, "First and third calls should return same reference");
    }

    /**
     * Tests that getConstants() does not throw any exceptions.
     * Verifies that the method is exception-safe.
     */
    @Test
    public void testGetConstantsDoesNotThrow() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act & Assert
        assertDoesNotThrow(() -> sequences.getConstants(),
            "getConstants() should not throw any exceptions");
    }

    /**
     * Tests that getConstants() can be called immediately after construction.
     * Verifies that no additional initialization is required before calling the method.
     */
    @Test
    public void testGetConstantsCallableImmediatelyAfterConstruction() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        // Act
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);
        Constant[] constants = sequences.getConstants();

        // Assert
        assertNotNull(constants, "getConstants() should be callable immediately after construction");
    }

    /**
     * Tests that getConstants() returns the same result when called multiple times in succession.
     * Verifies method stability.
     */
    @Test
    public void testGetConstantsStability() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act
        Constant[] constants = sequences.getConstants();
        int initialLength = constants.length;

        // Call multiple times
        for (int i = 0; i < 10; i++) {
            Constant[] result = sequences.getConstants();

            // Assert
            assertSame(constants, result, "Call " + i + " should return same reference");
            assertEquals(initialLength, result.length, "Array length should remain constant");
        }
    }

    /**
     * Tests that getConstants() returns the expected array type.
     * Verifies the return type matches the method signature.
     */
    @Test
    public void testGetConstantsReturnsCorrectType() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act
        Object result = sequences.getConstants();

        // Assert
        assertTrue(result instanceof Constant[],
            "getConstants() should return an instance of Constant[]");
        assertEquals(Constant[].class, result.getClass(),
            "Return type should be exactly Constant[]");
    }

    /**
     * Tests that different instances return different constant arrays.
     * Verifies that each instance has its own constants array.
     */
    @Test
    public void testGetConstantsDifferentInstancesReturnDifferentArrays() {
        // Arrange
        ClassPool programClassPool1 = new ClassPool();
        ClassPool libraryClassPool1 = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences1 =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool1, libraryClassPool1);

        ClassPool programClassPool2 = new ClassPool();
        ClassPool libraryClassPool2 = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences2 =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool2, libraryClassPool2);

        // Act
        Constant[] constants1 = sequences1.getConstants();
        Constant[] constants2 = sequences2.getConstants();

        // Assert
        assertNotSame(constants1, constants2,
            "Different instances should return different Constant arrays");
    }

    /**
     * Tests that getConstants() works with empty ClassPools.
     * Verifies that the method handles edge cases correctly.
     */
    @Test
    public void testGetConstantsWithEmptyClassPools() {
        // Arrange
        ClassPool emptyProgramClassPool = new ClassPool();
        ClassPool emptyLibraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(emptyProgramClassPool, emptyLibraryClassPool);

        // Act
        Constant[] constants = sequences.getConstants();

        // Assert
        assertNotNull(constants, "getConstants() should return non-null even with empty ClassPools");
    }

    /**
     * Tests that getConstants() works with same ClassPool for both arguments.
     * Verifies that the method handles shared ClassPools correctly.
     */
    @Test
    public void testGetConstantsWithSameClassPool() {
        // Arrange
        ClassPool sameClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(sameClassPool, sameClassPool);

        // Act
        Constant[] constants = sequences.getConstants();

        // Assert
        assertNotNull(constants, "getConstants() should work with same ClassPool for both arguments");
    }

    /**
     * Tests that getConstants() result can be used in a for-each loop.
     * Verifies that the returned array is iterable.
     */
    @Test
    public void testGetConstantsResultIsIterable() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act
        Constant[] constants = sequences.getConstants();

        // Assert
        assertDoesNotThrow(() -> {
            for (Constant constant : constants) {
                // Just iterate, don't need to do anything
            }
        }, "Should be able to iterate over the constants array");
    }

    /**
     * Tests that getConstants() result has a valid length property.
     * Verifies that the array length is accessible and non-negative.
     */
    @Test
    public void testGetConstantsArrayHasValidLength() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act
        Constant[] constants = sequences.getConstants();

        // Assert
        assertTrue(constants.length >= 0, "Array length should be non-negative");
    }

    /**
     * Tests that getConstants() returns consistent length across multiple calls.
     * Verifies that the array length doesn't change between calls.
     */
    @Test
    public void testGetConstantsConsistentLength() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act
        Constant[] constants1 = sequences.getConstants();
        int length1 = constants1.length;
        Constant[] constants2 = sequences.getConstants();
        int length2 = constants2.length;

        // Assert
        assertEquals(length1, length2,
            "Array length should be consistent across multiple calls");
    }

    /**
     * Tests that getConstants() can be called from ReplacementSequences interface reference.
     * Verifies that the method is accessible through the interface.
     */
    @Test
    public void testGetConstantsAccessibleThroughInterface() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        proguard.obfuscate.util.ReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act
        Constant[] constants = sequences.getConstants();

        // Assert
        assertNotNull(constants, "getConstants() should be accessible through ReplacementSequences interface");
    }

    /**
     * Tests that getConstants() result is not modified by subsequent calls to getSequences().
     * Verifies independence between getConstants() and getSequences().
     */
    @Test
    public void testGetConstantsIndependentOfGetSequences() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act
        Constant[] constantsBefore = sequences.getConstants();
        sequences.getSequences(); // Call getSequences() in between
        Constant[] constantsAfter = sequences.getConstants();

        // Assert
        assertSame(constantsBefore, constantsAfter,
            "getConstants() should return same reference even after calling getSequences()");
    }

    /**
     * Tests that getConstants() result is not modified by multiple calls to getSequences().
     * Verifies stability when both methods are called alternately.
     */
    @Test
    public void testGetConstantsStableWithInterleavedGetSequences() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act & Assert
        Constant[] constants1 = sequences.getConstants();
        sequences.getSequences();
        Constant[] constants2 = sequences.getConstants();
        sequences.getSequences();
        Constant[] constants3 = sequences.getConstants();

        assertSame(constants1, constants2, "First and second calls should return same reference");
        assertSame(constants2, constants3, "Second and third calls should return same reference");
    }

    /**
     * Tests that getConstants() behaves correctly across multiple new instances.
     * Verifies consistent behavior across different object lifecycles.
     */
    @Test
    public void testGetConstantsConsistentAcrossMultipleInstances() {
        // Arrange & Act
        for (int i = 0; i < 5; i++) {
            ClassPool programClassPool = new ClassPool();
            ClassPool libraryClassPool = new ClassPool();
            KotlinUnsupportedExceptionReplacementSequences sequences =
                new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

            Constant[] constants = sequences.getConstants();

            // Assert
            assertNotNull(constants, "Instance " + i + " should return non-null constants");
        }
    }

    /**
     * Tests that getConstants() result has the correct component type.
     * Verifies the array's component type is Constant.
     */
    @Test
    public void testGetConstantsArrayComponentType() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act
        Constant[] constants = sequences.getConstants();

        // Assert
        assertEquals(Constant.class, constants.getClass().getComponentType(),
            "Array component type should be Constant");
    }

    /**
     * Tests that getConstants() can be called multiple times without side effects.
     * Verifies that the method has no observable side effects.
     */
    @Test
    public void testGetConstantsNoSideEffects() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        KotlinUnsupportedExceptionReplacementSequences sequences =
            new KotlinUnsupportedExceptionReplacementSequences(programClassPool, libraryClassPool);

        // Act - Call many times
        Constant[] first = sequences.getConstants();
        for (int i = 0; i < 100; i++) {
            sequences.getConstants();
        }
        Constant[] last = sequences.getConstants();

        // Assert
        assertSame(first, last,
            "First and last calls should return same reference, indicating no side effects");
    }
}
