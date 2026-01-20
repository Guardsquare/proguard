package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link EscapingClassMarker#isClassEscaping(Clazz)}.
 *
 * The isClassEscaping method checks whether a class has been marked as escaping, meaning
 * its references escape from the code being analyzed. The method delegates to
 * ClassOptimizationInfo.getClassOptimizationInfo(clazz).isEscaping().
 *
 * Key behaviors:
 * 1. For ProgramClass with ProgramClassOptimizationInfo: returns the actual escaping state
 * 2. For LibraryClass with ClassOptimizationInfo: always returns true (conservative default)
 * 3. For ProgramClass without optimization info set: may throw exception or return default
 * 4. The method is static and can be called without an EscapingClassMarker instance
 */
public class EscapingClassMarkerClaude_isClassEscapingTest {

    /**
     * Tests that isClassEscaping returns false for a ProgramClass that hasn't been marked as escaping.
     */
    @Test
    public void testIsClassEscaping_unmarkedProgramClass_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        boolean result = EscapingClassMarker.isClassEscaping(programClass);

        // Assert
        assertFalse(result, "Unmarked ProgramClass should not be escaping");
    }

    /**
     * Tests that isClassEscaping returns true for a ProgramClass that has been marked as escaping.
     */
    @Test
    public void testIsClassEscaping_markedProgramClass_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Mark the class as escaping
        EscapingClassMarker marker = new EscapingClassMarker();
        marker.visitProgramClass(programClass);

        // Act
        boolean result = EscapingClassMarker.isClassEscaping(programClass);

        // Assert
        assertTrue(result, "Marked ProgramClass should be escaping");
    }

    /**
     * Tests that isClassEscaping returns true for a LibraryClass.
     * LibraryClass uses the base ClassOptimizationInfo which conservatively returns true.
     */
    @Test
    public void testIsClassEscaping_libraryClass_returnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act
        boolean result = EscapingClassMarker.isClassEscaping(libraryClass);

        // Assert
        assertTrue(result, "LibraryClass should always be considered escaping (conservative)");
    }

    /**
     * Tests that isClassEscaping can be called multiple times with consistent results.
     */
    @Test
    public void testIsClassEscaping_calledMultipleTimes_consistentResults() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call multiple times before marking
        boolean result1 = EscapingClassMarker.isClassEscaping(programClass);
        boolean result2 = EscapingClassMarker.isClassEscaping(programClass);
        boolean result3 = EscapingClassMarker.isClassEscaping(programClass);

        // Assert - all should be false
        assertFalse(result1, "First call should return false");
        assertFalse(result2, "Second call should return false");
        assertFalse(result3, "Third call should return false");

        // Arrange - mark as escaping
        EscapingClassMarker marker = new EscapingClassMarker();
        marker.visitProgramClass(programClass);

        // Act - call multiple times after marking
        boolean result4 = EscapingClassMarker.isClassEscaping(programClass);
        boolean result5 = EscapingClassMarker.isClassEscaping(programClass);
        boolean result6 = EscapingClassMarker.isClassEscaping(programClass);

        // Assert - all should be true
        assertTrue(result4, "Fourth call should return true");
        assertTrue(result5, "Fifth call should return true");
        assertTrue(result6, "Sixth call should return true");
    }

    /**
     * Tests that isClassEscaping correctly differentiates between escaping and non-escaping classes.
     */
    @Test
    public void testIsClassEscaping_differentClasses_independentState() {
        // Arrange
        ProgramClass escapingClass = new ProgramClass();
        ProgramClass nonEscapingClass = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(escapingClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonEscapingClass);

        // Mark only one class
        EscapingClassMarker marker = new EscapingClassMarker();
        marker.visitProgramClass(escapingClass);

        // Act
        boolean escapingResult = EscapingClassMarker.isClassEscaping(escapingClass);
        boolean nonEscapingResult = EscapingClassMarker.isClassEscaping(nonEscapingClass);

        // Assert
        assertTrue(escapingResult, "Marked class should be escaping");
        assertFalse(nonEscapingResult, "Unmarked class should not be escaping");
    }

    /**
     * Tests that isClassEscaping is a static method and doesn't require an EscapingClassMarker instance.
     */
    @Test
    public void testIsClassEscaping_staticMethod_worksWithoutInstance() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call static method directly without creating an EscapingClassMarker instance
        boolean result = EscapingClassMarker.isClassEscaping(programClass);

        // Assert
        assertFalse(result, "Static method should work without marker instance");
    }

    /**
     * Tests that isClassEscaping works correctly after a class is marked by visitProgramClass.
     */
    @Test
    public void testIsClassEscaping_afterVisitProgramClass_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        EscapingClassMarker marker = new EscapingClassMarker();

        // Verify initial state
        assertFalse(EscapingClassMarker.isClassEscaping(programClass),
                "Class should not be escaping before visiting");

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should be escaping after visiting");
    }

    /**
     * Tests that isClassEscaping works with multiple ProgramClass instances.
     */
    @Test
    public void testIsClassEscaping_multipleProgramClasses_correctStates() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        EscapingClassMarker marker = new EscapingClassMarker();

        // Mark class1 and class3
        marker.visitProgramClass(class1);
        marker.visitProgramClass(class3);

        // Act & Assert
        assertTrue(EscapingClassMarker.isClassEscaping(class1), "Class1 should be escaping");
        assertFalse(EscapingClassMarker.isClassEscaping(class2), "Class2 should not be escaping");
        assertTrue(EscapingClassMarker.isClassEscaping(class3), "Class3 should be escaping");
    }

    /**
     * Tests that isClassEscaping doesn't throw exceptions with valid inputs.
     */
    @Test
    public void testIsClassEscaping_validInputs_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act & Assert
        assertDoesNotThrow(() -> EscapingClassMarker.isClassEscaping(programClass),
                "isClassEscaping should not throw exception for ProgramClass");
        assertDoesNotThrow(() -> EscapingClassMarker.isClassEscaping(libraryClass),
                "isClassEscaping should not throw exception for LibraryClass");
    }

    /**
     * Tests that isClassEscaping state persists across multiple marker instances.
     * The state is stored in the class, not the marker.
     */
    @Test
    public void testIsClassEscaping_multipleMarkerInstances_statePersists() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        EscapingClassMarker marker1 = new EscapingClassMarker();
        EscapingClassMarker marker2 = new EscapingClassMarker();

        // Act - mark with first marker
        marker1.visitProgramClass(programClass);

        // Assert - check with static method (no marker needed)
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should be escaping after marker1 visits");

        // Act - visit again with second marker (should remain escaping)
        marker2.visitProgramClass(programClass);

        // Assert - still escaping
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should still be escaping after marker2 visits");
    }

    /**
     * Tests that isClassEscaping returns correct value immediately after marking.
     */
    @Test
    public void testIsClassEscaping_immediatelyAfterMarking_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        EscapingClassMarker marker = new EscapingClassMarker();

        // Act - mark and immediately check
        marker.visitProgramClass(programClass);
        boolean result = EscapingClassMarker.isClassEscaping(programClass);

        // Assert
        assertTrue(result, "Class should be escaping immediately after marking");
    }

    /**
     * Tests that isClassEscaping works correctly with a sequence of mark and check operations.
     */
    @Test
    public void testIsClassEscaping_sequenceOfOperations_correctBehavior() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        EscapingClassMarker marker = new EscapingClassMarker();

        // Act & Assert - check initial state
        assertFalse(EscapingClassMarker.isClassEscaping(class1), "Class1 initially not escaping");
        assertFalse(EscapingClassMarker.isClassEscaping(class2), "Class2 initially not escaping");

        // Act & Assert - mark class1
        marker.visitProgramClass(class1);
        assertTrue(EscapingClassMarker.isClassEscaping(class1), "Class1 should be escaping");
        assertFalse(EscapingClassMarker.isClassEscaping(class2), "Class2 should still not be escaping");

        // Act & Assert - mark class2
        marker.visitProgramClass(class2);
        assertTrue(EscapingClassMarker.isClassEscaping(class1), "Class1 should still be escaping");
        assertTrue(EscapingClassMarker.isClassEscaping(class2), "Class2 should now be escaping");
    }

    /**
     * Tests that isClassEscaping with LibraryClass always returns true regardless of marking.
     */
    @Test
    public void testIsClassEscaping_libraryClass_alwaysReturnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act & Assert - before any marking
        assertTrue(EscapingClassMarker.isClassEscaping(libraryClass),
                "LibraryClass should be escaping even without marking");

        // Note: visitProgramClass doesn't handle LibraryClass, but we can verify
        // the isClassEscaping behavior is consistent
        assertTrue(EscapingClassMarker.isClassEscaping(libraryClass),
                "LibraryClass should still be escaping");
    }

    /**
     * Tests that isClassEscaping handles rapid successive calls correctly.
     */
    @Test
    public void testIsClassEscaping_rapidSuccessiveCalls_consistentResults() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        EscapingClassMarker marker = new EscapingClassMarker();

        marker.visitProgramClass(programClass);

        // Act & Assert - call many times rapidly
        for (int i = 0; i < 1000; i++) {
            assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                    "Class should be escaping on iteration " + i);
        }
    }

    /**
     * Tests that isClassEscaping correctly identifies the state transition from non-escaping to escaping.
     */
    @Test
    public void testIsClassEscaping_stateTransition_correctlyDetected() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        EscapingClassMarker marker = new EscapingClassMarker();

        // Act & Assert - initial state
        boolean beforeMarking = EscapingClassMarker.isClassEscaping(programClass);
        assertFalse(beforeMarking, "Class should not be escaping before marking");

        // Act - mark the class
        marker.visitProgramClass(programClass);

        // Assert - state after marking
        boolean afterMarking = EscapingClassMarker.isClassEscaping(programClass);
        assertTrue(afterMarking, "Class should be escaping after marking");

        // Assert - state has changed
        assertNotEquals(beforeMarking, afterMarking,
                "State should transition from false to true");
    }

    /**
     * Tests that isClassEscaping can handle checking many different classes.
     */
    @Test
    public void testIsClassEscaping_manyClasses_correctIndividualStates() {
        // Arrange
        final int classCount = 50;
        ProgramClass[] classes = new ProgramClass[classCount];

        for (int i = 0; i < classCount; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
        }

        EscapingClassMarker marker = new EscapingClassMarker();

        // Mark only even-indexed classes
        for (int i = 0; i < classCount; i += 2) {
            marker.visitProgramClass(classes[i]);
        }

        // Act & Assert
        for (int i = 0; i < classCount; i++) {
            boolean expected = (i % 2 == 0); // Even indices should be escaping
            boolean actual = EscapingClassMarker.isClassEscaping(classes[i]);
            assertEquals(expected, actual,
                    "Class at index " + i + " should " + (expected ? "" : "not ") + "be escaping");
        }
    }

    /**
     * Tests that isClassEscaping with ProgramClass having basic ClassOptimizationInfo returns true.
     * This tests the conservative default behavior when not using ProgramClassOptimizationInfo.
     */
    @Test
    public void testIsClassEscaping_programClassWithBasicOptimizationInfo_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act
        boolean result = EscapingClassMarker.isClassEscaping(programClass);

        // Assert
        assertTrue(result, "ProgramClass with basic ClassOptimizationInfo should be escaping (conservative)");
    }

    /**
     * Tests that isClassEscaping works correctly in a concurrent scenario.
     * The isEscaping flag is volatile, so it should be thread-safe.
     */
    @Test
    public void testIsClassEscaping_concurrentAccess_correctResults() throws InterruptedException {
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
                EscapingClassMarker marker = new EscapingClassMarker();
                for (int j = 0; j < classesPerThread; j++) {
                    marker.visitProgramClass(classes[threadIndex][j]);
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
                    results[threadIndex][j] = EscapingClassMarker.isClassEscaping(classes[threadIndex][j]);
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
                        "Class [" + i + "][" + j + "] should be escaping");
            }
        }
    }

    /**
     * Tests that isClassEscaping returns false for freshly created unmarked classes.
     */
    @Test
    public void testIsClassEscaping_freshlyCreatedClass_returnsFalse() {
        // Arrange & Act
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Assert
        assertFalse(EscapingClassMarker.isClassEscaping(programClass),
                "Freshly created class should not be escaping");
    }

    /**
     * Tests that isClassEscaping maintains correct state across multiple operations.
     */
    @Test
    public void testIsClassEscaping_multipleOperations_maintainsCorrectState() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        EscapingClassMarker marker = new EscapingClassMarker();

        // Act & Assert - initial state
        assertFalse(EscapingClassMarker.isClassEscaping(programClass),
                "Initial state should be false");

        // Act - mark as escaping
        marker.visitProgramClass(programClass);

        // Assert - state should be true and remain true
        assertTrue(EscapingClassMarker.isClassEscaping(programClass), "Should be escaping after marking");
        assertTrue(EscapingClassMarker.isClassEscaping(programClass), "Should remain escaping");
        assertTrue(EscapingClassMarker.isClassEscaping(programClass), "Should still remain escaping");

        // Act - mark again (idempotent)
        marker.visitProgramClass(programClass);

        // Assert - state should still be true
        assertTrue(EscapingClassMarker.isClassEscaping(programClass), "Should still be escaping");
    }

    /**
     * Tests that isClassEscaping works correctly with mixed ProgramClass and LibraryClass.
     */
    @Test
    public void testIsClassEscaping_mixedClassTypes_correctBehavior() {
        // Arrange
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass2);
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        EscapingClassMarker marker = new EscapingClassMarker();

        // Mark only programClass1
        marker.visitProgramClass(programClass1);

        // Act & Assert
        assertTrue(EscapingClassMarker.isClassEscaping(programClass1),
                "Marked ProgramClass should be escaping");
        assertFalse(EscapingClassMarker.isClassEscaping(programClass2),
                "Unmarked ProgramClass should not be escaping");
        assertTrue(EscapingClassMarker.isClassEscaping(libraryClass),
                "LibraryClass should always be escaping");
    }

    /**
     * Tests that isClassEscaping is deterministic and always returns the same value for the same input.
     */
    @Test
    public void testIsClassEscaping_deterministicBehavior() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        EscapingClassMarker marker = new EscapingClassMarker();

        marker.visitProgramClass(programClass);

        // Act - call multiple times
        boolean result1 = EscapingClassMarker.isClassEscaping(programClass);
        boolean result2 = EscapingClassMarker.isClassEscaping(programClass);
        boolean result3 = EscapingClassMarker.isClassEscaping(programClass);

        // Assert - all results should be identical
        assertTrue(result1, "First result should be true");
        assertTrue(result2, "Second result should be true");
        assertTrue(result3, "Third result should be true");
        assertEquals(result1, result2, "Results should be identical");
        assertEquals(result2, result3, "Results should be identical");
    }

    /**
     * Tests that isClassEscaping correctly handles boundary case with zero marked classes.
     */
    @Test
    public void testIsClassEscaping_noMarkedClasses_allReturnFalse() {
        // Arrange
        final int classCount = 10;
        ProgramClass[] classes = new ProgramClass[classCount];

        for (int i = 0; i < classCount; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
        }

        // Act & Assert - none should be escaping
        for (int i = 0; i < classCount; i++) {
            assertFalse(EscapingClassMarker.isClassEscaping(classes[i]),
                    "Class " + i + " should not be escaping (no classes marked)");
        }
    }

    /**
     * Tests that isClassEscaping correctly handles boundary case with all marked classes.
     */
    @Test
    public void testIsClassEscaping_allMarkedClasses_allReturnTrue() {
        // Arrange
        final int classCount = 10;
        ProgramClass[] classes = new ProgramClass[classCount];
        EscapingClassMarker marker = new EscapingClassMarker();

        for (int i = 0; i < classCount; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
            marker.visitProgramClass(classes[i]);
        }

        // Act & Assert - all should be escaping
        for (int i = 0; i < classCount; i++) {
            assertTrue(EscapingClassMarker.isClassEscaping(classes[i]),
                    "Class " + i + " should be escaping (all classes marked)");
        }
    }

    /**
     * Tests that isClassEscaping performance is acceptable for repeated calls.
     */
    @Test
    public void testIsClassEscaping_performance_acceptableForRepeatedCalls() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        EscapingClassMarker marker = new EscapingClassMarker();
        marker.visitProgramClass(programClass);

        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 10000; i++) {
            EscapingClassMarker.isClassEscaping(programClass);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 100ms for 10000 calls)
        assertTrue(durationMs < 100,
                "isClassEscaping should execute quickly, took " + durationMs + "ms for 10000 calls");
    }

    /**
     * Tests that isClassEscaping works correctly when checking classes in reverse order of marking.
     */
    @Test
    public void testIsClassEscaping_reverseOrderChecking_correctResults() {
        // Arrange
        final int classCount = 5;
        ProgramClass[] classes = new ProgramClass[classCount];
        EscapingClassMarker marker = new EscapingClassMarker();

        for (int i = 0; i < classCount; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
            marker.visitProgramClass(classes[i]);
        }

        // Act & Assert - check in reverse order
        for (int i = classCount - 1; i >= 0; i--) {
            assertTrue(EscapingClassMarker.isClassEscaping(classes[i]),
                    "Class " + i + " should be escaping when checked in reverse order");
        }
    }
}
