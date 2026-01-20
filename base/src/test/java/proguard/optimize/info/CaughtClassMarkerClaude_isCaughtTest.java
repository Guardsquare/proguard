package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link CaughtClassMarker#isCaught(Clazz)}.
 *
 * The isCaught method checks whether a class has been marked as caught, meaning it's
 * an exception class that occurs in exception handlers. The method delegates to
 * ClassOptimizationInfo.getClassOptimizationInfo(clazz).isCaught().
 *
 * Key behaviors:
 * 1. For ProgramClass with ProgramClassOptimizationInfo: returns the actual caught state
 * 2. For LibraryClass with ClassOptimizationInfo: always returns true (conservative default)
 * 3. For ProgramClass without optimization info set: may throw exception or return default
 * 4. The method is static and can be called without a CaughtClassMarker instance
 */
public class CaughtClassMarkerClaude_isCaughtTest {

    /**
     * Tests that isCaught returns false for a ProgramClass that hasn't been marked as caught.
     */
    @Test
    public void testIsCaught_unmarkedProgramClass_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        boolean result = CaughtClassMarker.isCaught(programClass);

        // Assert
        assertFalse(result, "Unmarked ProgramClass should not be caught");
    }

    /**
     * Tests that isCaught returns true for a ProgramClass that has been marked as caught.
     */
    @Test
    public void testIsCaught_markedProgramClass_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Mark the class as caught
        CaughtClassMarker marker = new CaughtClassMarker();
        marker.visitProgramClass(programClass);

        // Act
        boolean result = CaughtClassMarker.isCaught(programClass);

        // Assert
        assertTrue(result, "Marked ProgramClass should be caught");
    }

    /**
     * Tests that isCaught returns true for a LibraryClass.
     * LibraryClass uses the base ClassOptimizationInfo which conservatively returns true.
     */
    @Test
    public void testIsCaught_libraryClass_returnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act
        boolean result = CaughtClassMarker.isCaught(libraryClass);

        // Assert
        assertTrue(result, "LibraryClass should always be considered caught (conservative)");
    }

    /**
     * Tests that isCaught can be called multiple times with consistent results.
     */
    @Test
    public void testIsCaught_calledMultipleTimes_consistentResults() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call multiple times before marking
        boolean result1 = CaughtClassMarker.isCaught(programClass);
        boolean result2 = CaughtClassMarker.isCaught(programClass);
        boolean result3 = CaughtClassMarker.isCaught(programClass);

        // Assert - all should be false
        assertFalse(result1, "First call should return false");
        assertFalse(result2, "Second call should return false");
        assertFalse(result3, "Third call should return false");

        // Arrange - mark as caught
        CaughtClassMarker marker = new CaughtClassMarker();
        marker.visitProgramClass(programClass);

        // Act - call multiple times after marking
        boolean result4 = CaughtClassMarker.isCaught(programClass);
        boolean result5 = CaughtClassMarker.isCaught(programClass);
        boolean result6 = CaughtClassMarker.isCaught(programClass);

        // Assert - all should be true
        assertTrue(result4, "Fourth call should return true");
        assertTrue(result5, "Fifth call should return true");
        assertTrue(result6, "Sixth call should return true");
    }

    /**
     * Tests that isCaught correctly differentiates between caught and uncaught classes.
     */
    @Test
    public void testIsCaught_differentClasses_independentState() {
        // Arrange
        ProgramClass caughtClass = new ProgramClass();
        ProgramClass uncaughtClass = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(caughtClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(uncaughtClass);

        // Mark only one class
        CaughtClassMarker marker = new CaughtClassMarker();
        marker.visitProgramClass(caughtClass);

        // Act
        boolean caughtResult = CaughtClassMarker.isCaught(caughtClass);
        boolean uncaughtResult = CaughtClassMarker.isCaught(uncaughtClass);

        // Assert
        assertTrue(caughtResult, "Marked class should be caught");
        assertFalse(uncaughtResult, "Unmarked class should not be caught");
    }

    /**
     * Tests that isCaught is a static method and doesn't require a CaughtClassMarker instance.
     */
    @Test
    public void testIsCaught_staticMethod_worksWithoutInstance() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call static method directly without creating a CaughtClassMarker instance
        boolean result = CaughtClassMarker.isCaught(programClass);

        // Assert
        assertFalse(result, "Static method should work without marker instance");
    }

    /**
     * Tests that isCaught works correctly after a class is marked by visitProgramClass.
     */
    @Test
    public void testIsCaught_afterVisitProgramClass_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        CaughtClassMarker marker = new CaughtClassMarker();

        // Verify initial state
        assertFalse(CaughtClassMarker.isCaught(programClass),
                "Class should not be caught before visiting");

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertTrue(CaughtClassMarker.isCaught(programClass),
                "Class should be caught after visiting");
    }

    /**
     * Tests that isCaught works with multiple ProgramClass instances.
     */
    @Test
    public void testIsCaught_multipleProgramClasses_correctStates() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        CaughtClassMarker marker = new CaughtClassMarker();

        // Mark class1 and class3
        marker.visitProgramClass(class1);
        marker.visitProgramClass(class3);

        // Act & Assert
        assertTrue(CaughtClassMarker.isCaught(class1), "Class1 should be caught");
        assertFalse(CaughtClassMarker.isCaught(class2), "Class2 should not be caught");
        assertTrue(CaughtClassMarker.isCaught(class3), "Class3 should be caught");
    }

    /**
     * Tests that isCaught doesn't throw exceptions with valid inputs.
     */
    @Test
    public void testIsCaught_validInputs_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act & Assert
        assertDoesNotThrow(() -> CaughtClassMarker.isCaught(programClass),
                "isCaught should not throw exception for ProgramClass");
        assertDoesNotThrow(() -> CaughtClassMarker.isCaught(libraryClass),
                "isCaught should not throw exception for LibraryClass");
    }

    /**
     * Tests that isCaught state persists across multiple marker instances.
     * The state is stored in the class, not the marker.
     */
    @Test
    public void testIsCaught_multipleMarkerInstances_statePersists() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        CaughtClassMarker marker1 = new CaughtClassMarker();
        CaughtClassMarker marker2 = new CaughtClassMarker();

        // Act - mark with first marker
        marker1.visitProgramClass(programClass);

        // Assert - check with static method (no marker needed)
        assertTrue(CaughtClassMarker.isCaught(programClass),
                "Class should be caught after marker1 visits");

        // Act - visit again with second marker (should remain caught)
        marker2.visitProgramClass(programClass);

        // Assert - still caught
        assertTrue(CaughtClassMarker.isCaught(programClass),
                "Class should still be caught after marker2 visits");
    }

    /**
     * Tests that isCaught returns correct value immediately after marking.
     */
    @Test
    public void testIsCaught_immediatelyAfterMarking_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        CaughtClassMarker marker = new CaughtClassMarker();

        // Act - mark and immediately check
        marker.visitProgramClass(programClass);
        boolean result = CaughtClassMarker.isCaught(programClass);

        // Assert
        assertTrue(result, "Class should be caught immediately after marking");
    }

    /**
     * Tests that isCaught works correctly with a sequence of mark and check operations.
     */
    @Test
    public void testIsCaught_sequenceOfOperations_correctBehavior() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        CaughtClassMarker marker = new CaughtClassMarker();

        // Act & Assert - check initial state
        assertFalse(CaughtClassMarker.isCaught(class1), "Class1 initially not caught");
        assertFalse(CaughtClassMarker.isCaught(class2), "Class2 initially not caught");

        // Act & Assert - mark class1
        marker.visitProgramClass(class1);
        assertTrue(CaughtClassMarker.isCaught(class1), "Class1 should be caught");
        assertFalse(CaughtClassMarker.isCaught(class2), "Class2 should still not be caught");

        // Act & Assert - mark class2
        marker.visitProgramClass(class2);
        assertTrue(CaughtClassMarker.isCaught(class1), "Class1 should still be caught");
        assertTrue(CaughtClassMarker.isCaught(class2), "Class2 should now be caught");
    }

    /**
     * Tests that isCaught with LibraryClass always returns true regardless of marking.
     */
    @Test
    public void testIsCaught_libraryClass_alwaysReturnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act & Assert - before any marking
        assertTrue(CaughtClassMarker.isCaught(libraryClass),
                "LibraryClass should be caught even without marking");

        // Note: visitProgramClass doesn't handle LibraryClass, but we can verify
        // the isCaught behavior is consistent
        assertTrue(CaughtClassMarker.isCaught(libraryClass),
                "LibraryClass should still be caught");
    }

    /**
     * Tests that isCaught handles rapid successive calls correctly.
     */
    @Test
    public void testIsCaught_rapidSuccessiveCalls_consistentResults() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        CaughtClassMarker marker = new CaughtClassMarker();

        marker.visitProgramClass(programClass);

        // Act & Assert - call many times rapidly
        for (int i = 0; i < 1000; i++) {
            assertTrue(CaughtClassMarker.isCaught(programClass),
                    "Class should be caught on iteration " + i);
        }
    }

    /**
     * Tests that isCaught correctly identifies the state transition from uncaught to caught.
     */
    @Test
    public void testIsCaught_stateTransition_correctlyDetected() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        CaughtClassMarker marker = new CaughtClassMarker();

        // Act & Assert - initial state
        boolean beforeMarking = CaughtClassMarker.isCaught(programClass);
        assertFalse(beforeMarking, "Class should not be caught before marking");

        // Act - mark the class
        marker.visitProgramClass(programClass);

        // Assert - state after marking
        boolean afterMarking = CaughtClassMarker.isCaught(programClass);
        assertTrue(afterMarking, "Class should be caught after marking");

        // Assert - state has changed
        assertNotEquals(beforeMarking, afterMarking,
                "State should transition from false to true");
    }

    /**
     * Tests that isCaught can handle checking many different classes.
     */
    @Test
    public void testIsCaught_manyClasses_correctIndividualStates() {
        // Arrange
        final int classCount = 50;
        ProgramClass[] classes = new ProgramClass[classCount];

        for (int i = 0; i < classCount; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
        }

        CaughtClassMarker marker = new CaughtClassMarker();

        // Mark only even-indexed classes
        for (int i = 0; i < classCount; i += 2) {
            marker.visitProgramClass(classes[i]);
        }

        // Act & Assert
        for (int i = 0; i < classCount; i++) {
            boolean expected = (i % 2 == 0); // Even indices should be caught
            boolean actual = CaughtClassMarker.isCaught(classes[i]);
            assertEquals(expected, actual,
                    "Class at index " + i + " should " + (expected ? "" : "not ") + "be caught");
        }
    }

    /**
     * Tests that isCaught with ProgramClass having basic ClassOptimizationInfo returns true.
     * This tests the conservative default behavior when not using ProgramClassOptimizationInfo.
     */
    @Test
    public void testIsCaught_programClassWithBasicOptimizationInfo_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act
        boolean result = CaughtClassMarker.isCaught(programClass);

        // Assert
        assertTrue(result, "ProgramClass with basic ClassOptimizationInfo should be caught (conservative)");
    }

    /**
     * Tests that isCaught works correctly in a concurrent scenario.
     * The isCaught flag is volatile, so it should be thread-safe.
     */
    @Test
    public void testIsCaught_concurrentAccess_correctResults() throws InterruptedException {
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
                CaughtClassMarker marker = new CaughtClassMarker();
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
                    results[threadIndex][j] = CaughtClassMarker.isCaught(classes[threadIndex][j]);
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
                        "Class [" + i + "][" + j + "] should be caught");
            }
        }
    }
}
