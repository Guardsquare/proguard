package proguard.optimize.info;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link MutableBoolean}.
 *
 * This class provides a mutable boolean flag. The tests cover all public methods including:
 * - Constructor and initialization
 * - set() method to set the flag to true
 * - reset() method to reset the flag to false
 * - isSet() method to check the current state
 */
public class MutableBooleanClaudeTest {

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests that the default constructor successfully creates a MutableBoolean instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        MutableBoolean mutableBoolean = new MutableBoolean();

        // Assert
        assertNotNull(mutableBoolean, "MutableBoolean instance should be created");
    }

    /**
     * Tests that the constructor initializes the flag to false by default.
     * In Java, boolean fields are initialized to false by default.
     */
    @Test
    public void testConstructor_initializesFlagToFalse() {
        // Act
        MutableBoolean mutableBoolean = new MutableBoolean();

        // Assert
        assertFalse(mutableBoolean.isSet(), "Flag should be false by default");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        MutableBoolean mb1 = new MutableBoolean();
        MutableBoolean mb2 = new MutableBoolean();
        MutableBoolean mb3 = new MutableBoolean();

        // Assert
        assertNotNull(mb1, "First instance should be created");
        assertNotNull(mb2, "Second instance should be created");
        assertNotNull(mb3, "Third instance should be created");
        assertNotSame(mb1, mb2, "First and second instances should be different");
        assertNotSame(mb2, mb3, "Second and third instances should be different");
        assertNotSame(mb1, mb3, "First and third instances should be different");
    }

    // =========================================================================
    // set() Method Tests
    // =========================================================================

    /**
     * Tests that set() method changes the flag to true.
     */
    @Test
    public void testSet_changesFlagToTrue() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();
        assertFalse(mutableBoolean.isSet(), "Flag should initially be false");

        // Act
        mutableBoolean.set();

        // Assert
        assertTrue(mutableBoolean.isSet(), "Flag should be true after set()");
    }

    /**
     * Tests that set() can be called when flag is already true.
     */
    @Test
    public void testSet_whenAlreadySet() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();
        mutableBoolean.set();
        assertTrue(mutableBoolean.isSet(), "Flag should be true after first set()");

        // Act
        mutableBoolean.set();

        // Assert
        assertTrue(mutableBoolean.isSet(), "Flag should remain true after second set()");
    }

    /**
     * Tests that set() can be called multiple times consecutively.
     */
    @Test
    public void testSet_canBeCalledMultipleTimes() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();

        // Act
        mutableBoolean.set();
        mutableBoolean.set();
        mutableBoolean.set();

        // Assert
        assertTrue(mutableBoolean.isSet(), "Flag should remain true after multiple set() calls");
    }

    /**
     * Tests that set() does not throw exceptions.
     */
    @Test
    public void testSet_doesNotThrowException() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();

        // Act & Assert
        assertDoesNotThrow(() -> mutableBoolean.set(), "set() should not throw any exception");
    }

    // =========================================================================
    // reset() Method Tests
    // =========================================================================

    /**
     * Tests that reset() method changes the flag to false.
     */
    @Test
    public void testReset_changesFlagToFalse() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();
        mutableBoolean.set();
        assertTrue(mutableBoolean.isSet(), "Flag should be true after set()");

        // Act
        mutableBoolean.reset();

        // Assert
        assertFalse(mutableBoolean.isSet(), "Flag should be false after reset()");
    }

    /**
     * Tests that reset() can be called when flag is already false.
     */
    @Test
    public void testReset_whenAlreadyReset() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();
        assertFalse(mutableBoolean.isSet(), "Flag should initially be false");

        // Act
        mutableBoolean.reset();

        // Assert
        assertFalse(mutableBoolean.isSet(), "Flag should remain false after reset()");
    }

    /**
     * Tests that reset() can be called multiple times consecutively.
     */
    @Test
    public void testReset_canBeCalledMultipleTimes() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();
        mutableBoolean.set();

        // Act
        mutableBoolean.reset();
        mutableBoolean.reset();
        mutableBoolean.reset();

        // Assert
        assertFalse(mutableBoolean.isSet(), "Flag should remain false after multiple reset() calls");
    }

    /**
     * Tests that reset() does not throw exceptions.
     */
    @Test
    public void testReset_doesNotThrowException() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();

        // Act & Assert
        assertDoesNotThrow(() -> mutableBoolean.reset(), "reset() should not throw any exception");
    }

    // =========================================================================
    // isSet() Method Tests
    // =========================================================================

    /**
     * Tests that isSet() returns false for a newly created instance.
     */
    @Test
    public void testIsSet_returnsFalseInitially() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();

        // Act & Assert
        assertFalse(mutableBoolean.isSet(), "isSet() should return false for a new instance");
    }

    /**
     * Tests that isSet() returns true after set() is called.
     */
    @Test
    public void testIsSet_returnsTrueAfterSet() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();
        mutableBoolean.set();

        // Act & Assert
        assertTrue(mutableBoolean.isSet(), "isSet() should return true after set()");
    }

    /**
     * Tests that isSet() returns false after reset() is called.
     */
    @Test
    public void testIsSet_returnsFalseAfterReset() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();
        mutableBoolean.set();
        mutableBoolean.reset();

        // Act & Assert
        assertFalse(mutableBoolean.isSet(), "isSet() should return false after reset()");
    }

    /**
     * Tests that isSet() does not modify the flag state.
     */
    @Test
    public void testIsSet_doesNotModifyState() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();
        mutableBoolean.set();

        // Act
        boolean firstCall = mutableBoolean.isSet();
        boolean secondCall = mutableBoolean.isSet();
        boolean thirdCall = mutableBoolean.isSet();

        // Assert
        assertTrue(firstCall, "First call should return true");
        assertTrue(secondCall, "Second call should return true");
        assertTrue(thirdCall, "Third call should return true");
        assertTrue(mutableBoolean.isSet(), "Flag should still be true");
    }

    /**
     * Tests that isSet() can be called multiple times without side effects.
     */
    @Test
    public void testIsSet_canBeCalledMultipleTimes() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();

        // Act & Assert
        assertFalse(mutableBoolean.isSet(), "Should be false initially");
        assertFalse(mutableBoolean.isSet(), "Should still be false");
        assertFalse(mutableBoolean.isSet(), "Should still be false");
    }

    // =========================================================================
    // Integration and State Transition Tests
    // =========================================================================

    /**
     * Tests the complete state lifecycle: false -> set -> true -> reset -> false.
     */
    @Test
    public void testStateTransition_completeLifecycle() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();

        // Assert initial state
        assertFalse(mutableBoolean.isSet(), "Should start as false");

        // Act & Assert - set to true
        mutableBoolean.set();
        assertTrue(mutableBoolean.isSet(), "Should be true after set()");

        // Act & Assert - reset to false
        mutableBoolean.reset();
        assertFalse(mutableBoolean.isSet(), "Should be false after reset()");
    }

    /**
     * Tests multiple set/reset cycles.
     */
    @Test
    public void testStateTransition_multipleCycles() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();

        // Cycle 1
        mutableBoolean.set();
        assertTrue(mutableBoolean.isSet(), "Should be true after first set()");
        mutableBoolean.reset();
        assertFalse(mutableBoolean.isSet(), "Should be false after first reset()");

        // Cycle 2
        mutableBoolean.set();
        assertTrue(mutableBoolean.isSet(), "Should be true after second set()");
        mutableBoolean.reset();
        assertFalse(mutableBoolean.isSet(), "Should be false after second reset()");

        // Cycle 3
        mutableBoolean.set();
        assertTrue(mutableBoolean.isSet(), "Should be true after third set()");
        mutableBoolean.reset();
        assertFalse(mutableBoolean.isSet(), "Should be false after third reset()");
    }

    /**
     * Tests that multiple instances are independent of each other.
     */
    @Test
    public void testInstances_areIndependent() {
        // Arrange
        MutableBoolean mb1 = new MutableBoolean();
        MutableBoolean mb2 = new MutableBoolean();

        // Act - modify first instance
        mb1.set();

        // Assert - second instance should not be affected
        assertTrue(mb1.isSet(), "First instance should be true");
        assertFalse(mb2.isSet(), "Second instance should still be false");

        // Act - modify second instance
        mb2.set();

        // Assert - both should now be true
        assertTrue(mb1.isSet(), "First instance should still be true");
        assertTrue(mb2.isSet(), "Second instance should now be true");

        // Act - reset first instance
        mb1.reset();

        // Assert - only first should be false
        assertFalse(mb1.isSet(), "First instance should be false after reset");
        assertTrue(mb2.isSet(), "Second instance should still be true");
    }

    /**
     * Tests all methods can be called without throwing exceptions.
     */
    @Test
    public void testAllMethods_noExceptions() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> {
            mutableBoolean.set();
            mutableBoolean.isSet();
            mutableBoolean.reset();
            mutableBoolean.isSet();
            mutableBoolean.set();
            mutableBoolean.isSet();
        }, "All methods should execute without exceptions");
    }

    /**
     * Tests the flag behavior with repeated set/reset operations.
     */
    @Test
    public void testRepeatedOperations_maintainsCorrectState() {
        // Arrange
        MutableBoolean mutableBoolean = new MutableBoolean();

        // Act & Assert - repeated sets
        for (int i = 0; i < 10; i++) {
            mutableBoolean.set();
            assertTrue(mutableBoolean.isSet(), "Should be true after set() iteration " + i);
        }

        // Act & Assert - repeated resets
        for (int i = 0; i < 10; i++) {
            mutableBoolean.reset();
            assertFalse(mutableBoolean.isSet(), "Should be false after reset() iteration " + i);
        }
    }

    /**
     * Tests thread safety of MutableBoolean instances.
     * Each thread gets its own instance, so there should be no interference.
     */
    @Test
    public void testThreadSafety_separateInstances() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int operationsPerThread = 100;
        Thread[] threads = new Thread[threadCount];
        final boolean[][] results = new boolean[threadCount][operationsPerThread];

        // Act - create instances in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                MutableBoolean mb = new MutableBoolean();
                for (int j = 0; j < operationsPerThread; j++) {
                    if (j % 2 == 0) {
                        mb.set();
                    } else {
                        mb.reset();
                    }
                    results[threadIndex][j] = mb.isSet();
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify expected pattern in each thread
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < operationsPerThread; j++) {
                boolean expected = (j % 2 == 0); // set on even, reset on odd
                assertEquals(expected, results[i][j],
                    "Thread " + i + ", operation " + j + " should be " + expected);
            }
        }
    }

    /**
     * Tests complex sequence of operations.
     */
    @Test
    public void testComplexSequence_maintainsCorrectState() {
        // Arrange
        MutableBoolean mb = new MutableBoolean();

        // Complex sequence
        assertFalse(mb.isSet(), "Initial state should be false");

        mb.set();
        assertTrue(mb.isSet(), "After set");

        mb.set();
        assertTrue(mb.isSet(), "After double set");

        mb.reset();
        assertFalse(mb.isSet(), "After reset");

        mb.reset();
        assertFalse(mb.isSet(), "After double reset");

        mb.set();
        assertTrue(mb.isSet(), "After set again");

        mb.reset();
        assertFalse(mb.isSet(), "After final reset");
    }
}
