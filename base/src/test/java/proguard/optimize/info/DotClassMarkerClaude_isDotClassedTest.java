package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DotClassMarker#isDotClassed(Clazz)}.
 *
 * The isDotClassed method checks whether a class has been marked as "dot classed", meaning it's
 * referenced through .class constructs (e.g., String.class). The method delegates to
 * ClassOptimizationInfo.getClassOptimizationInfo(clazz).isDotClassed().
 *
 * Key behaviors:
 * 1. For ProgramClass with ProgramClassOptimizationInfo: returns the actual dot classed state
 * 2. For LibraryClass with ClassOptimizationInfo: always returns true (conservative default)
 * 3. For ProgramClass without optimization info set: may throw exception or return default
 * 4. The method is static and can be called without a DotClassMarker instance
 *
 * This marking is important for optimization decisions - classes referenced via .class
 * constructs may need different handling than classes referenced through other means.
 */
public class DotClassMarkerClaude_isDotClassedTest {

    /**
     * Tests that isDotClassed returns false for a ProgramClass that hasn't been marked as dot classed.
     */
    @Test
    public void testIsDotClassed_unmarkedProgramClass_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        boolean result = DotClassMarker.isDotClassed(programClass);

        // Assert
        assertFalse(result, "Unmarked ProgramClass should not be dot classed");
    }

    /**
     * Tests that isDotClassed returns true for a ProgramClass that has been marked as dot classed.
     */
    @Test
    public void testIsDotClassed_markedProgramClass_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Mark the class as dot classed
        DotClassMarker marker = new DotClassMarker();
        marker.visitAnyClass(programClass);

        // Act
        boolean result = DotClassMarker.isDotClassed(programClass);

        // Assert
        assertTrue(result, "Marked ProgramClass should be dot classed");
    }

    /**
     * Tests that isDotClassed returns true for a LibraryClass.
     * LibraryClass uses the base ClassOptimizationInfo which conservatively returns true.
     */
    @Test
    public void testIsDotClassed_libraryClass_returnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act
        boolean result = DotClassMarker.isDotClassed(libraryClass);

        // Assert
        assertTrue(result, "LibraryClass should always be considered dot classed (conservative)");
    }

    /**
     * Tests that isDotClassed can be called multiple times with consistent results.
     */
    @Test
    public void testIsDotClassed_calledMultipleTimes_consistentResults() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call multiple times before marking
        boolean result1 = DotClassMarker.isDotClassed(programClass);
        boolean result2 = DotClassMarker.isDotClassed(programClass);
        boolean result3 = DotClassMarker.isDotClassed(programClass);

        // Assert - all should be false
        assertFalse(result1, "First call should return false");
        assertFalse(result2, "Second call should return false");
        assertFalse(result3, "Third call should return false");

        // Arrange - mark as dot classed
        DotClassMarker marker = new DotClassMarker();
        marker.visitAnyClass(programClass);

        // Act - call multiple times after marking
        boolean result4 = DotClassMarker.isDotClassed(programClass);
        boolean result5 = DotClassMarker.isDotClassed(programClass);
        boolean result6 = DotClassMarker.isDotClassed(programClass);

        // Assert - all should be true
        assertTrue(result4, "Fourth call should return true");
        assertTrue(result5, "Fifth call should return true");
        assertTrue(result6, "Sixth call should return true");
    }

    /**
     * Tests that isDotClassed correctly differentiates between dot classed and non-dot classed classes.
     */
    @Test
    public void testIsDotClassed_differentClasses_independentState() {
        // Arrange
        ProgramClass dotClassedClass = new ProgramClass();
        ProgramClass notDotClassedClass = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(dotClassedClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(notDotClassedClass);

        // Mark only one class
        DotClassMarker marker = new DotClassMarker();
        marker.visitAnyClass(dotClassedClass);

        // Act
        boolean dotClassedResult = DotClassMarker.isDotClassed(dotClassedClass);
        boolean notDotClassedResult = DotClassMarker.isDotClassed(notDotClassedClass);

        // Assert
        assertTrue(dotClassedResult, "Marked class should be dot classed");
        assertFalse(notDotClassedResult, "Unmarked class should not be dot classed");
    }

    /**
     * Tests that isDotClassed is a static method and doesn't require a DotClassMarker instance.
     */
    @Test
    public void testIsDotClassed_staticMethod_worksWithoutInstance() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call static method directly without creating a DotClassMarker instance
        boolean result = DotClassMarker.isDotClassed(programClass);

        // Assert
        assertFalse(result, "Static method should work without marker instance");
    }

    /**
     * Tests that isDotClassed works correctly after a class is marked by visitAnyClass.
     */
    @Test
    public void testIsDotClassed_afterVisitAnyClass_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        DotClassMarker marker = new DotClassMarker();

        // Verify initial state
        assertFalse(DotClassMarker.isDotClassed(programClass),
                "Class should not be dot classed before visiting");

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should be dot classed after visiting");
    }

    /**
     * Tests that isDotClassed works with multiple ProgramClass instances.
     */
    @Test
    public void testIsDotClassed_multipleProgramClasses_correctStates() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        DotClassMarker marker = new DotClassMarker();

        // Mark class1 and class3
        marker.visitAnyClass(class1);
        marker.visitAnyClass(class3);

        // Act & Assert
        assertTrue(DotClassMarker.isDotClassed(class1), "Class1 should be dot classed");
        assertFalse(DotClassMarker.isDotClassed(class2), "Class2 should not be dot classed");
        assertTrue(DotClassMarker.isDotClassed(class3), "Class3 should be dot classed");
    }

    /**
     * Tests that isDotClassed doesn't throw exceptions with valid inputs.
     */
    @Test
    public void testIsDotClassed_validInputs_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act & Assert
        assertDoesNotThrow(() -> DotClassMarker.isDotClassed(programClass),
                "isDotClassed should not throw exception for ProgramClass");
        assertDoesNotThrow(() -> DotClassMarker.isDotClassed(libraryClass),
                "isDotClassed should not throw exception for LibraryClass");
    }

    /**
     * Tests that isDotClassed state persists across multiple marker instances.
     * The state is stored in the class, not the marker.
     */
    @Test
    public void testIsDotClassed_multipleMarkerInstances_statePersists() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        DotClassMarker marker1 = new DotClassMarker();
        DotClassMarker marker2 = new DotClassMarker();

        // Act - mark with first marker
        marker1.visitAnyClass(programClass);

        // Assert - check with static method (no marker needed)
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should be dot classed after marker1 visits");

        // Act - visit again with second marker (should remain dot classed)
        marker2.visitAnyClass(programClass);

        // Assert - still dot classed
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should still be dot classed after marker2 visits");
    }

    /**
     * Tests that isDotClassed returns correct value immediately after marking.
     */
    @Test
    public void testIsDotClassed_immediatelyAfterMarking_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        DotClassMarker marker = new DotClassMarker();

        // Act - mark and immediately check
        marker.visitAnyClass(programClass);
        boolean result = DotClassMarker.isDotClassed(programClass);

        // Assert
        assertTrue(result, "Class should be dot classed immediately after marking");
    }

    /**
     * Tests that isDotClassed works correctly with a sequence of mark and check operations.
     */
    @Test
    public void testIsDotClassed_sequenceOfOperations_correctBehavior() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        DotClassMarker marker = new DotClassMarker();

        // Act & Assert - check initial state
        assertFalse(DotClassMarker.isDotClassed(class1), "Class1 initially not dot classed");
        assertFalse(DotClassMarker.isDotClassed(class2), "Class2 initially not dot classed");

        // Act & Assert - mark class1
        marker.visitAnyClass(class1);
        assertTrue(DotClassMarker.isDotClassed(class1), "Class1 should be dot classed");
        assertFalse(DotClassMarker.isDotClassed(class2), "Class2 should still not be dot classed");

        // Act & Assert - mark class2
        marker.visitAnyClass(class2);
        assertTrue(DotClassMarker.isDotClassed(class1), "Class1 should still be dot classed");
        assertTrue(DotClassMarker.isDotClassed(class2), "Class2 should now be dot classed");
    }

    /**
     * Tests that isDotClassed with LibraryClass always returns true regardless of marking.
     */
    @Test
    public void testIsDotClassed_libraryClass_alwaysReturnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act & Assert - before any marking
        assertTrue(DotClassMarker.isDotClassed(libraryClass),
                "LibraryClass should be dot classed even without marking");

        // Note: visitAnyClass works with any Clazz, but LibraryClass optimization info
        // returns true by default for conservative behavior
        assertTrue(DotClassMarker.isDotClassed(libraryClass),
                "LibraryClass should still be dot classed");
    }

    /**
     * Tests that isDotClassed handles rapid successive calls correctly.
     */
    @Test
    public void testIsDotClassed_rapidSuccessiveCalls_consistentResults() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        DotClassMarker marker = new DotClassMarker();

        marker.visitAnyClass(programClass);

        // Act & Assert - call many times rapidly
        for (int i = 0; i < 1000; i++) {
            assertTrue(DotClassMarker.isDotClassed(programClass),
                    "Class should be dot classed on iteration " + i);
        }
    }

    /**
     * Tests that isDotClassed correctly identifies the state transition from not dot classed to dot classed.
     */
    @Test
    public void testIsDotClassed_stateTransition_correctlyDetected() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        DotClassMarker marker = new DotClassMarker();

        // Act & Assert - initial state
        boolean beforeMarking = DotClassMarker.isDotClassed(programClass);
        assertFalse(beforeMarking, "Class should not be dot classed before marking");

        // Act - mark the class
        marker.visitAnyClass(programClass);

        // Assert - state after marking
        boolean afterMarking = DotClassMarker.isDotClassed(programClass);
        assertTrue(afterMarking, "Class should be dot classed after marking");

        // Assert - state has changed
        assertNotEquals(beforeMarking, afterMarking,
                "State should transition from false to true");
    }

    /**
     * Tests that isDotClassed can handle checking many different classes.
     */
    @Test
    public void testIsDotClassed_manyClasses_correctIndividualStates() {
        // Arrange
        final int classCount = 50;
        ProgramClass[] classes = new ProgramClass[classCount];

        for (int i = 0; i < classCount; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
        }

        DotClassMarker marker = new DotClassMarker();

        // Mark only even-indexed classes
        for (int i = 0; i < classCount; i += 2) {
            marker.visitAnyClass(classes[i]);
        }

        // Act & Assert
        for (int i = 0; i < classCount; i++) {
            boolean expected = (i % 2 == 0); // Even indices should be dot classed
            boolean actual = DotClassMarker.isDotClassed(classes[i]);
            assertEquals(expected, actual,
                    "Class at index " + i + " should " + (expected ? "" : "not ") + "be dot classed");
        }
    }

    /**
     * Tests that isDotClassed with ProgramClass having basic ClassOptimizationInfo returns true.
     * This tests the conservative default behavior when not using ProgramClassOptimizationInfo.
     */
    @Test
    public void testIsDotClassed_programClassWithBasicOptimizationInfo_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act
        boolean result = DotClassMarker.isDotClassed(programClass);

        // Assert
        assertTrue(result, "ProgramClass with basic ClassOptimizationInfo should be dot classed (conservative)");
    }

    /**
     * Tests that isDotClassed works correctly in a concurrent scenario.
     * The isDotClassed flag should be thread-safe.
     */
    @Test
    public void testIsDotClassed_concurrentAccess_correctResults() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int classesPerThread = 10;
        final ProgramClass[][] classes = new ProgramClass[threadCount][classesPerThread];

        // Initialize all classes
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < classesPerThread; j++) {
                classes[i][j] = new ProgramClass();
                ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i][j]);
            }
        }

        // Mark all classes in parallel
        Thread[] markingThreads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            markingThreads[i] = new Thread(() -> {
                DotClassMarker marker = new DotClassMarker();
                for (int j = 0; j < classesPerThread; j++) {
                    marker.visitAnyClass(classes[threadIndex][j]);
                }
            });
            markingThreads[i].start();
        }

        // Wait for all marking threads to complete
        for (Thread thread : markingThreads) {
            thread.join();
        }

        // Act & Assert - check all classes in parallel
        Thread[] checkingThreads = new Thread[threadCount];
        final boolean[][] results = new boolean[threadCount][classesPerThread];

        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            checkingThreads[i] = new Thread(() -> {
                for (int j = 0; j < classesPerThread; j++) {
                    results[threadIndex][j] = DotClassMarker.isDotClassed(classes[threadIndex][j]);
                }
            });
            checkingThreads[i].start();
        }

        // Wait for all checking threads to complete
        for (Thread thread : checkingThreads) {
            thread.join();
        }

        // Assert all results are true
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < classesPerThread; j++) {
                assertTrue(results[i][j],
                        "Class [" + i + "][" + j + "] should be dot classed");
            }
        }
    }

    /**
     * Tests that isDotClassed returns false for freshly created unmarked classes.
     */
    @Test
    public void testIsDotClassed_freshlyCreatedClass_returnsFalse() {
        // Arrange & Act
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        boolean result = DotClassMarker.isDotClassed(programClass);

        // Assert
        assertFalse(result, "Freshly created class should not be dot classed");
    }

    /**
     * Tests that isDotClassed works correctly when classes are marked through the visitor chain.
     * This simulates the typical usage where LDC instructions lead to marking classes.
     */
    @Test
    public void testIsDotClassed_throughVisitorChain_correctlyMarked() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Verify initial state
        assertFalse(DotClassMarker.isDotClassed(programClass),
                "Class should not be dot classed initially");

        // Act - mark through visitAnyClass (simulating the visitor chain endpoint)
        DotClassMarker marker = new DotClassMarker();
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should be dot classed after visitor chain");
    }

    /**
     * Tests that isDotClassed is consistent across different marker instances checking the same class.
     */
    @Test
    public void testIsDotClassed_differentMarkerInstances_consistentResults() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        DotClassMarker marker1 = new DotClassMarker();
        DotClassMarker marker2 = new DotClassMarker();
        DotClassMarker marker3 = new DotClassMarker();

        // Act - mark with marker1
        marker1.visitAnyClass(programClass);

        // Assert - check with static method from different contexts
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Direct static call should return true");

        // Create new markers and verify they all see the same state
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Static call with marker2 in scope should return true");
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Static call with marker3 in scope should return true");
    }

    /**
     * Tests that isDotClassed correctly reports state for classes marked and unmarked in alternating pattern.
     */
    @Test
    public void testIsDotClassed_alternatingPattern_correctStates() {
        // Arrange
        final int classCount = 10;
        ProgramClass[] classes = new ProgramClass[classCount];

        for (int i = 0; i < classCount; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
        }

        DotClassMarker marker = new DotClassMarker();

        // Mark alternating classes (0, 2, 4, 6, 8)
        for (int i = 0; i < classCount; i += 2) {
            marker.visitAnyClass(classes[i]);
        }

        // Act & Assert - verify alternating pattern
        for (int i = 0; i < classCount; i++) {
            boolean expected = (i % 2 == 0);
            boolean actual = DotClassMarker.isDotClassed(classes[i]);
            assertEquals(expected, actual,
                    "Class " + i + " should " + (expected ? "" : "not ") + "be dot classed");
        }
    }

    /**
     * Tests that isDotClassed is idempotent - checking doesn't change the state.
     */
    @Test
    public void testIsDotClassed_idempotentCheck_stateUnchanged() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        DotClassMarker marker = new DotClassMarker();

        marker.visitAnyClass(programClass);

        // Act - check multiple times
        boolean result1 = DotClassMarker.isDotClassed(programClass);
        boolean result2 = DotClassMarker.isDotClassed(programClass);
        boolean result3 = DotClassMarker.isDotClassed(programClass);

        // Assert - all results should be identical
        assertTrue(result1, "First check should return true");
        assertTrue(result2, "Second check should return true");
        assertTrue(result3, "Third check should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that isDotClassed works correctly with batch operations.
     */
    @Test
    public void testIsDotClassed_batchOperations_correctResults() {
        // Arrange
        final int batchSize = 20;
        ProgramClass[] batch1 = new ProgramClass[batchSize];
        ProgramClass[] batch2 = new ProgramClass[batchSize];

        for (int i = 0; i < batchSize; i++) {
            batch1[i] = new ProgramClass();
            batch2[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(batch1[i]);
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(batch2[i]);
        }

        DotClassMarker marker = new DotClassMarker();

        // Mark only batch1
        for (ProgramClass clazz : batch1) {
            marker.visitAnyClass(clazz);
        }

        // Act & Assert - batch1 should be dot classed, batch2 should not
        for (int i = 0; i < batchSize; i++) {
            assertTrue(DotClassMarker.isDotClassed(batch1[i]),
                    "Batch1 class " + i + " should be dot classed");
            assertFalse(DotClassMarker.isDotClassed(batch2[i]),
                    "Batch2 class " + i + " should not be dot classed");
        }
    }

    /**
     * Tests that isDotClassed executes quickly even with many calls.
     */
    @Test
    public void testIsDotClassed_performance_executesQuickly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        DotClassMarker marker = new DotClassMarker();
        marker.visitAnyClass(programClass);

        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 10000; i++) {
            DotClassMarker.isDotClassed(programClass);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 100ms for 10000 calls)
        assertTrue(durationMs < 100,
                "isDotClassed should execute quickly, took " + durationMs + "ms for 10000 calls");
    }

    /**
     * Tests that isDotClassed correctly identifies mixed states in a collection of classes.
     */
    @Test
    public void testIsDotClassed_mixedStates_correctIdentification() {
        // Arrange
        ProgramClass markedClass1 = new ProgramClass();
        ProgramClass markedClass2 = new ProgramClass();
        ProgramClass unmarkedClass1 = new ProgramClass();
        ProgramClass unmarkedClass2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(markedClass1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(markedClass2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(unmarkedClass1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(unmarkedClass2);

        DotClassMarker marker = new DotClassMarker();
        marker.visitAnyClass(markedClass1);
        marker.visitAnyClass(markedClass2);

        // Act & Assert
        assertTrue(DotClassMarker.isDotClassed(markedClass1), "MarkedClass1 should be dot classed");
        assertTrue(DotClassMarker.isDotClassed(markedClass2), "MarkedClass2 should be dot classed");
        assertFalse(DotClassMarker.isDotClassed(unmarkedClass1), "UnmarkedClass1 should not be dot classed");
        assertFalse(DotClassMarker.isDotClassed(unmarkedClass2), "UnmarkedClass2 should not be dot classed");
    }

    /**
     * Tests that isDotClassed works correctly after repeated marking of the same class.
     */
    @Test
    public void testIsDotClassed_repeatedMarking_remainsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        DotClassMarker marker = new DotClassMarker();

        // Act - mark multiple times
        marker.visitAnyClass(programClass);
        assertTrue(DotClassMarker.isDotClassed(programClass), "Should be dot classed after first mark");

        marker.visitAnyClass(programClass);
        assertTrue(DotClassMarker.isDotClassed(programClass), "Should remain dot classed after second mark");

        marker.visitAnyClass(programClass);
        assertTrue(DotClassMarker.isDotClassed(programClass), "Should remain dot classed after third mark");
    }

    /**
     * Tests that isDotClassed method signature is correct and accessible as a static method.
     */
    @Test
    public void testIsDotClassed_staticMethodSignature_correct() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call as static method (should compile and run)
        boolean result = DotClassMarker.isDotClassed(programClass);

        // Assert
        assertFalse(result, "Static method should be callable and return correct value");
    }
}
