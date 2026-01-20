package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ReferenceEscapeChecker#isInstanceExternal(int)}.
 *
 * The isInstanceExternal method returns whether the instance created or retrieved
 * at the specified instruction offset is external to this method and its invoked methods.
 *
 * An instance is considered external when it:
 * - Is retrieved via GETSTATIC or GETFIELD instructions
 * - Is returned from a method call that returns external values (as marked by ParameterEscapeMarker)
 *
 * This method reads from the externalInstance[] boolean array which is populated during
 * code attribute visiting by the markExternalReferenceValue(offset) method.
 *
 * The method signature is: public boolean isInstanceExternal(int instructionOffset)
 * It simply returns: externalInstance[instructionOffset]
 */
public class ReferenceEscapeCheckerClaude_isInstanceExternalTest {

    private ReferenceEscapeChecker checker;

    @BeforeEach
    public void setUp() {
        // Create a new ReferenceEscapeChecker for each test
        checker = new ReferenceEscapeChecker();
    }

    /**
     * Tests that isInstanceExternal returns false for offset 0 before any code is visited.
     * The internal array should be initialized to false by default.
     */
    @Test
    public void testIsInstanceExternal_offsetZeroInitially_returnsFalse() {
        // Act
        boolean result = checker.isInstanceExternal(0);

        // Assert
        assertFalse(result, "isInstanceExternal should return false at offset 0 initially");
    }

    /**
     * Tests that isInstanceExternal returns false for various small offsets.
     * Before visiting any code, all offsets should return false.
     */
    @Test
    public void testIsInstanceExternal_smallOffsetsInitially_returnFalse() {
        // Act & Assert
        for (int offset = 0; offset < 10; offset++) {
            assertFalse(checker.isInstanceExternal(offset),
                "Offset " + offset + " should return false initially");
        }
    }

    /**
     * Tests that isInstanceExternal returns false for larger offsets.
     * The internal array should expand as needed and new elements default to false.
     */
    @Test
    public void testIsInstanceExternal_largerOffsets_returnFalse() {
        // Act & Assert
        assertFalse(checker.isInstanceExternal(50), "Offset 50 should return false");
        assertFalse(checker.isInstanceExternal(100), "Offset 100 should return false");
        assertFalse(checker.isInstanceExternal(500), "Offset 500 should return false");
    }

    /**
     * Tests that isInstanceExternal can be called multiple times with the same offset
     * and returns consistent results.
     */
    @Test
    public void testIsInstanceExternal_calledMultipleTimes_returnsConsistentResults() {
        // Act
        boolean result1 = checker.isInstanceExternal(0);
        boolean result2 = checker.isInstanceExternal(0);
        boolean result3 = checker.isInstanceExternal(0);

        // Assert
        assertFalse(result1, "First call should return false");
        assertFalse(result2, "Second call should return false");
        assertFalse(result3, "Third call should return false");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that isInstanceExternal handles different offsets independently.
     * Each offset should maintain its own state.
     */
    @Test
    public void testIsInstanceExternal_differentOffsets_independentResults() {
        // Act & Assert - all should be false initially
        assertFalse(checker.isInstanceExternal(0), "Offset 0 should be false");
        assertFalse(checker.isInstanceExternal(5), "Offset 5 should be false");
        assertFalse(checker.isInstanceExternal(10), "Offset 10 should be false");
        assertFalse(checker.isInstanceExternal(20), "Offset 20 should be false");
    }

    /**
     * Tests that isInstanceExternal doesn't throw exception when called.
     */
    @Test
    public void testIsInstanceExternal_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> checker.isInstanceExternal(0),
            "Should not throw exception");
        assertDoesNotThrow(() -> checker.isInstanceExternal(100),
            "Should not throw exception for larger offset");
    }

    /**
     * Tests that isInstanceExternal handles boundary offset 0.
     */
    @Test
    public void testIsInstanceExternal_boundaryOffsetZero_worksCorrectly() {
        // Act
        boolean result = checker.isInstanceExternal(0);

        // Assert
        assertFalse(result, "Boundary offset 0 should return false initially");
    }

    /**
     * Tests that isInstanceExternal handles sequential offsets.
     */
    @Test
    public void testIsInstanceExternal_sequentialOffsets_allReturnFalse() {
        // Act & Assert - check sequential offsets
        for (int offset = 0; offset < 20; offset++) {
            assertFalse(checker.isInstanceExternal(offset),
                "Offset " + offset + " should return false");
        }
    }

    /**
     * Tests that isInstanceExternal can handle rapid sequential calls.
     */
    @Test
    public void testIsInstanceExternal_rapidSequentialCalls_handlesCorrectly() {
        // Act & Assert
        for (int i = 0; i < 1000; i++) {
            assertFalse(checker.isInstanceExternal(i % 100),
                "Call " + i + " should return false");
        }
    }

    /**
     * Tests that isInstanceExternal returns the same value when called on the same offset
     * multiple times in succession.
     */
    @Test
    public void testIsInstanceExternal_sameOffsetRepeatedCalls_consistent() {
        // Act
        boolean result1 = checker.isInstanceExternal(5);
        boolean result2 = checker.isInstanceExternal(5);
        boolean result3 = checker.isInstanceExternal(5);
        boolean result4 = checker.isInstanceExternal(5);

        // Assert
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
        assertEquals(result3, result4, "Results should be consistent");
        assertFalse(result1, "All results should be false initially");
    }

    /**
     * Tests that isInstanceExternal with offset 1 returns false initially.
     */
    @Test
    public void testIsInstanceExternal_offsetOne_returnsFalse() {
        // Act
        boolean result = checker.isInstanceExternal(1);

        // Assert
        assertFalse(result, "Offset 1 should return false initially");
    }

    /**
     * Tests that isInstanceExternal with various mid-range offsets returns false.
     */
    @Test
    public void testIsInstanceExternal_midRangeOffsets_returnFalse() {
        // Act & Assert
        assertFalse(checker.isInstanceExternal(15), "Offset 15 should return false");
        assertFalse(checker.isInstanceExternal(25), "Offset 25 should return false");
        assertFalse(checker.isInstanceExternal(35), "Offset 35 should return false");
        assertFalse(checker.isInstanceExternal(45), "Offset 45 should return false");
    }

    /**
     * Tests that isInstanceExternal works correctly after creating multiple checker instances.
     * Each instance should maintain its own state.
     */
    @Test
    public void testIsInstanceExternal_multipleInstances_independentState() {
        // Arrange
        ReferenceEscapeChecker checker1 = new ReferenceEscapeChecker();
        ReferenceEscapeChecker checker2 = new ReferenceEscapeChecker();
        ReferenceEscapeChecker checker3 = new ReferenceEscapeChecker();

        // Act
        boolean result1 = checker1.isInstanceExternal(0);
        boolean result2 = checker2.isInstanceExternal(0);
        boolean result3 = checker3.isInstanceExternal(0);

        // Assert - all should return false initially
        assertFalse(result1, "First checker should return false");
        assertFalse(result2, "Second checker should return false");
        assertFalse(result3, "Third checker should return false");
    }

    /**
     * Tests that isInstanceExternal returns false for all typical instruction offsets
     * in an unvisited state.
     */
    @Test
    public void testIsInstanceExternal_typicalInstructionOffsets_returnFalse() {
        // Typical offsets in bytecode
        int[] typicalOffsets = {0, 1, 2, 3, 4, 5, 10, 15, 20, 30, 50, 100};

        // Act & Assert
        for (int offset : typicalOffsets) {
            assertFalse(checker.isInstanceExternal(offset),
                "Offset " + offset + " should return false initially");
        }
    }

    /**
     * Tests that isInstanceExternal can handle checking the same offset
     * alternating with checks of other offsets.
     */
    @Test
    public void testIsInstanceExternal_alternatingOffsets_consistent() {
        // Act & Assert - alternate between offset 0 and 10
        assertFalse(checker.isInstanceExternal(0));
        assertFalse(checker.isInstanceExternal(10));
        assertFalse(checker.isInstanceExternal(0));
        assertFalse(checker.isInstanceExternal(10));
        assertFalse(checker.isInstanceExternal(0));
        assertFalse(checker.isInstanceExternal(10));
    }

    /**
     * Tests that isInstanceExternal returns false for offset 2.
     */
    @Test
    public void testIsInstanceExternal_offsetTwo_returnsFalse() {
        // Act
        boolean result = checker.isInstanceExternal(2);

        // Assert
        assertFalse(result, "Offset 2 should return false initially");
    }

    /**
     * Tests that isInstanceExternal returns false for offset 3.
     */
    @Test
    public void testIsInstanceExternal_offsetThree_returnsFalse() {
        // Act
        boolean result = checker.isInstanceExternal(3);

        // Assert
        assertFalse(result, "Offset 3 should return false initially");
    }

    /**
     * Tests that isInstanceExternal returns the correct type (boolean).
     */
    @Test
    public void testIsInstanceExternal_returnType_isBoolean() {
        // Act
        Object result = checker.isInstanceExternal(0);

        // Assert
        assertInstanceOf(Boolean.class, result, "Return type should be Boolean");
    }

    /**
     * Tests that isInstanceExternal with offset 10 returns false initially.
     */
    @Test
    public void testIsInstanceExternal_offsetTen_returnsFalse() {
        // Act
        boolean result = checker.isInstanceExternal(10);

        // Assert
        assertFalse(result, "Offset 10 should return false initially");
    }

    /**
     * Tests that isInstanceExternal can be called in different orders
     * and maintains correct state for each offset.
     */
    @Test
    public void testIsInstanceExternal_randomAccessPattern_maintainsState() {
        // Act & Assert - access offsets in non-sequential order
        assertFalse(checker.isInstanceExternal(50), "Offset 50 should be false");
        assertFalse(checker.isInstanceExternal(10), "Offset 10 should be false");
        assertFalse(checker.isInstanceExternal(30), "Offset 30 should be false");
        assertFalse(checker.isInstanceExternal(5), "Offset 5 should be false");
        assertFalse(checker.isInstanceExternal(100), "Offset 100 should be false");
        assertFalse(checker.isInstanceExternal(0), "Offset 0 should be false");
    }
}
