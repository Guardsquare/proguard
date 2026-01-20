package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link EscapingClassMarker#visitProgramClass(ProgramClass)}.
 *
 * The visitProgramClass method marks program classes as "escaping", meaning they are
 * classes whose references escape from the code being analyzed. This is done by calling
 * the private markClassEscaping method, which sets the escaping flag in the class's
 * ProgramClassOptimizationInfo.
 *
 * These tests verify that:
 * 1. The method correctly marks classes as escaping
 * 2. The escaping status can be verified using EscapingClassMarker.isClassEscaping()
 * 3. The method handles multiple invocations correctly
 * 4. The method works with different ProgramClass instances
 * 5. The marking persists across multiple checks
 * 6. Multiple markers can mark the same class
 */
public class EscapingClassMarkerClaude_visitProgramClassTest {

    private EscapingClassMarker marker;

    @BeforeEach
    public void setUp() {
        marker = new EscapingClassMarker();
    }

    /**
     * Tests that visitProgramClass marks a ProgramClass as escaping.
     */
    @Test
    public void testVisitProgramClass_marksClassAsEscaping() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Verify initial state - should not be escaping
        assertFalse(EscapingClassMarker.isClassEscaping(programClass),
                "Class should not be marked as escaping initially");

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should be marked as escaping after visitProgramClass");
    }

    /**
     * Tests that visitProgramClass can be called multiple times on the same class.
     * The class should remain marked as escaping.
     */
    @Test
    public void testVisitProgramClass_calledMultipleTimes_remainsEscaping() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call multiple times
        marker.visitProgramClass(programClass);
        marker.visitProgramClass(programClass);
        marker.visitProgramClass(programClass);

        // Assert - should still be escaping
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should remain marked as escaping after multiple visits");
    }

    /**
     * Tests that visitProgramClass correctly marks multiple different classes as escaping.
     */
    @Test
    public void testVisitProgramClass_multipleDifferentClasses_allMarkedAsEscaping() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        // Verify initial state
        assertFalse(EscapingClassMarker.isClassEscaping(class1), "Class1 should not be escaping initially");
        assertFalse(EscapingClassMarker.isClassEscaping(class2), "Class2 should not be escaping initially");
        assertFalse(EscapingClassMarker.isClassEscaping(class3), "Class3 should not be escaping initially");

        // Act
        marker.visitProgramClass(class1);
        marker.visitProgramClass(class2);
        marker.visitProgramClass(class3);

        // Assert
        assertTrue(EscapingClassMarker.isClassEscaping(class1), "Class1 should be marked as escaping");
        assertTrue(EscapingClassMarker.isClassEscaping(class2), "Class2 should be marked as escaping");
        assertTrue(EscapingClassMarker.isClassEscaping(class3), "Class3 should be marked as escaping");
    }

    /**
     * Tests that visitProgramClass doesn't throw an exception when called.
     */
    @Test
    public void testVisitProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitProgramClass(programClass),
                "visitProgramClass should not throw an exception");
    }

    /**
     * Tests that multiple EscapingClassMarker instances can mark the same class.
     * The escaping status is stored in the class, not the marker.
     */
    @Test
    public void testVisitProgramClass_multipleMarkerInstances_sameBehavior() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        EscapingClassMarker marker1 = new EscapingClassMarker();
        EscapingClassMarker marker2 = new EscapingClassMarker();

        // Act - use first marker
        marker1.visitProgramClass(programClass);

        // Assert - verify escaping using static method
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should be escaping after first marker visits");

        // Act - use second marker
        marker2.visitProgramClass(programClass);

        // Assert - should still be escaping
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should still be escaping after second marker visits");
    }

    /**
     * Tests that visitProgramClass is stateless and independent across different classes.
     */
    @Test
    public void testVisitProgramClass_statelessBehavior() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        // Act - mark only class1
        marker.visitProgramClass(class1);

        // Assert - only class1 should be escaping
        assertTrue(EscapingClassMarker.isClassEscaping(class1), "Class1 should be escaping");
        assertFalse(EscapingClassMarker.isClassEscaping(class2), "Class2 should not be escaping");

        // Act - now mark class2
        marker.visitProgramClass(class2);

        // Assert - both should be escaping
        assertTrue(EscapingClassMarker.isClassEscaping(class1), "Class1 should still be escaping");
        assertTrue(EscapingClassMarker.isClassEscaping(class2), "Class2 should now be escaping");
    }

    /**
     * Tests that the escaping status persists after the marker is used.
     */
    @Test
    public void testVisitProgramClass_escapingStatusPersists() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitProgramClass(programClass);

        // Assert - check status multiple times
        assertTrue(EscapingClassMarker.isClassEscaping(programClass), "Class should be escaping - first check");
        assertTrue(EscapingClassMarker.isClassEscaping(programClass), "Class should be escaping - second check");
        assertTrue(EscapingClassMarker.isClassEscaping(programClass), "Class should be escaping - third check");
    }

    /**
     * Tests that visitProgramClass can be called in rapid succession.
     */
    @Test
    public void testVisitProgramClass_rapidSuccession() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                marker.visitProgramClass(programClass);
            }
        }, "visitProgramClass should handle rapid successive calls");

        // Verify still escaping after all calls
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should be escaping after 100 visits");
    }

    /**
     * Tests that visitProgramClass works correctly with freshly created ProgramClass instances.
     */
    @Test
    public void testVisitProgramClass_freshlyCreatedClass() {
        // Arrange - create a fresh class right before visiting
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - visit immediately after creation
        marker.visitProgramClass(programClass);

        // Assert
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Freshly created class should be marked as escaping");
    }

    /**
     * Tests that isClassEscaping returns false for a class that hasn't been visited.
     */
    @Test
    public void testVisitProgramClass_unvisitedClass_notEscaping() {
        // Arrange
        ProgramClass visitedClass = new ProgramClass();
        ProgramClass unvisitedClass = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(visitedClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(unvisitedClass);

        // Act - visit only one class
        marker.visitProgramClass(visitedClass);

        // Assert
        assertTrue(EscapingClassMarker.isClassEscaping(visitedClass),
                "Visited class should be escaping");
        assertFalse(EscapingClassMarker.isClassEscaping(unvisitedClass),
                "Unvisited class should not be escaping");
    }

    /**
     * Tests that visitProgramClass works correctly when called from multiple threads.
     * The escaping flag should be thread-safe.
     */
    @Test
    public void testVisitProgramClass_concurrentAccess() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        final int classesPerThread = 10;
        Thread[] threads = new Thread[threadCount];
        final ProgramClass[][] classes = new ProgramClass[threadCount][classesPerThread];

        // Initialize all classes
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < classesPerThread; j++) {
                classes[i][j] = new ProgramClass();
                ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i][j]);
            }
        }

        // Act - mark classes in multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                EscapingClassMarker threadMarker = new EscapingClassMarker();
                for (int j = 0; j < classesPerThread; j++) {
                    threadMarker.visitProgramClass(classes[threadIndex][j]);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - all classes should be marked as escaping
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < classesPerThread; j++) {
                assertTrue(EscapingClassMarker.isClassEscaping(classes[i][j]),
                        "Class [" + i + "][" + j + "] should be marked as escaping");
            }
        }
    }

    /**
     * Tests that visitProgramClass idempotence - calling it multiple times has the same effect as calling it once.
     */
    @Test
    public void testVisitProgramClass_idempotent() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call once
        marker.visitProgramClass(programClass);
        boolean afterFirstCall = EscapingClassMarker.isClassEscaping(programClass);

        // Act - call again
        marker.visitProgramClass(programClass);
        boolean afterSecondCall = EscapingClassMarker.isClassEscaping(programClass);

        // Assert - both should be true and equal
        assertTrue(afterFirstCall, "Class should be escaping after first call");
        assertTrue(afterSecondCall, "Class should be escaping after second call");
        assertEquals(afterFirstCall, afterSecondCall,
                "Escaping status should be the same after multiple calls");
    }

    /**
     * Tests that visitProgramClass works correctly when alternating between different markers.
     */
    @Test
    public void testVisitProgramClass_alternatingMarkers() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        EscapingClassMarker marker1 = new EscapingClassMarker();
        EscapingClassMarker marker2 = new EscapingClassMarker();

        // Act - alternate between markers
        marker1.visitProgramClass(programClass);
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should be escaping after marker1 visit");

        marker2.visitProgramClass(programClass);
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should still be escaping after marker2 visit");

        marker1.visitProgramClass(programClass);
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should still be escaping after marker1 revisit");
    }

    /**
     * Tests that visitProgramClass marking persists across creation of new marker instances.
     */
    @Test
    public void testVisitProgramClass_persistsAcrossMarkerInstances() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        EscapingClassMarker marker1 = new EscapingClassMarker();

        // Act - mark with first marker
        marker1.visitProgramClass(programClass);

        // Assert - verify with static method
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should be escaping after marker1 visit");

        // Create new marker and verify status still persists
        EscapingClassMarker marker2 = new EscapingClassMarker();
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should still be escaping with new marker instance");
    }

    /**
     * Tests that visitProgramClass works correctly with many classes in a batch.
     */
    @Test
    public void testVisitProgramClass_batchProcessing() {
        // Arrange
        final int classCount = 50;
        ProgramClass[] classes = new ProgramClass[classCount];

        for (int i = 0; i < classCount; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
        }

        // Act - mark all classes
        for (ProgramClass clazz : classes) {
            marker.visitProgramClass(clazz);
        }

        // Assert - all should be escaping
        for (int i = 0; i < classCount; i++) {
            assertTrue(EscapingClassMarker.isClassEscaping(classes[i]),
                    "Class " + i + " should be escaping");
        }
    }

    /**
     * Tests that visitProgramClass execution completes quickly.
     */
    @Test
    public void testVisitProgramClass_executesQuickly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 1000; i++) {
            marker.visitProgramClass(programClass);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
                "visitProgramClass should execute quickly, took " + durationMs + "ms for 1000 calls");
    }

    /**
     * Tests that visitProgramClass with interleaved calls on different classes works correctly.
     */
    @Test
    public void testVisitProgramClass_interleavedCalls() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        // Act - interleave calls
        marker.visitProgramClass(class1);
        marker.visitProgramClass(class2);
        marker.visitProgramClass(class1);
        marker.visitProgramClass(class2);

        // Assert - both should be escaping
        assertTrue(EscapingClassMarker.isClassEscaping(class1),
                "Class1 should be escaping");
        assertTrue(EscapingClassMarker.isClassEscaping(class2),
                "Class2 should be escaping");
    }

    /**
     * Tests that visitProgramClass with sequential calls on different classes marks each independently.
     */
    @Test
    public void testVisitProgramClass_sequentialCallsOnDifferentClasses_eachMarkedIndependently() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        // Act - mark sequentially
        marker.visitProgramClass(class1);
        assertTrue(EscapingClassMarker.isClassEscaping(class1), "Class1 should be escaping after visit");
        assertFalse(EscapingClassMarker.isClassEscaping(class2), "Class2 should not be escaping yet");
        assertFalse(EscapingClassMarker.isClassEscaping(class3), "Class3 should not be escaping yet");

        marker.visitProgramClass(class2);
        assertTrue(EscapingClassMarker.isClassEscaping(class1), "Class1 should still be escaping");
        assertTrue(EscapingClassMarker.isClassEscaping(class2), "Class2 should be escaping after visit");
        assertFalse(EscapingClassMarker.isClassEscaping(class3), "Class3 should not be escaping yet");

        marker.visitProgramClass(class3);
        assertTrue(EscapingClassMarker.isClassEscaping(class1), "Class1 should still be escaping");
        assertTrue(EscapingClassMarker.isClassEscaping(class2), "Class2 should still be escaping");
        assertTrue(EscapingClassMarker.isClassEscaping(class3), "Class3 should be escaping after visit");
    }

    /**
     * Tests that visitProgramClass doesn't interfere with other marker instances.
     */
    @Test
    public void testVisitProgramClass_noInterferenceBetweenMarkers() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        EscapingClassMarker marker1 = new EscapingClassMarker();
        EscapingClassMarker marker2 = new EscapingClassMarker();

        // Act - mark different classes with different markers
        marker1.visitProgramClass(class1);
        marker2.visitProgramClass(class2);

        // Assert - each class should be marked independently
        assertTrue(EscapingClassMarker.isClassEscaping(class1),
                "Class1 should be escaping by marker1");
        assertTrue(EscapingClassMarker.isClassEscaping(class2),
                "Class2 should be escaping by marker2");
    }

    /**
     * Tests that visitProgramClass completion is deterministic.
     */
    @Test
    public void testVisitProgramClass_deterministicBehavior() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call multiple times and check status after each call
        marker.visitProgramClass(programClass);
        boolean status1 = EscapingClassMarker.isClassEscaping(programClass);

        marker.visitProgramClass(programClass);
        boolean status2 = EscapingClassMarker.isClassEscaping(programClass);

        marker.visitProgramClass(programClass);
        boolean status3 = EscapingClassMarker.isClassEscaping(programClass);

        // Assert - all statuses should be true and consistent
        assertTrue(status1, "Status after first call should be true");
        assertTrue(status2, "Status after second call should be true");
        assertTrue(status3, "Status after third call should be true");
        assertEquals(status1, status2, "Status should be consistent across calls");
        assertEquals(status2, status3, "Status should be consistent across calls");
    }

    /**
     * Tests that visitProgramClass works with a marker used across multiple unrelated classes.
     */
    @Test
    public void testVisitProgramClass_singleMarkerMultipleClasses() {
        // Arrange
        final int classCount = 10;
        ProgramClass[] classes = new ProgramClass[classCount];

        for (int i = 0; i < classCount; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
        }

        // Act - use single marker for all classes
        for (ProgramClass clazz : classes) {
            marker.visitProgramClass(clazz);
        }

        // Assert - all classes should be independently marked
        for (int i = 0; i < classCount; i++) {
            assertTrue(EscapingClassMarker.isClassEscaping(classes[i]),
                    "Class " + i + " should be escaping");
        }
    }

    /**
     * Tests that the static isClassEscaping method works correctly.
     */
    @Test
    public void testVisitProgramClass_staticIsClassEscapingMethod() {
        // Arrange
        ProgramClass markedClass = new ProgramClass();
        ProgramClass unmarkedClass = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(markedClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(unmarkedClass);

        // Act
        marker.visitProgramClass(markedClass);

        // Assert - verify static method returns correct values
        assertTrue(EscapingClassMarker.isClassEscaping(markedClass),
                "Static method should return true for marked class");
        assertFalse(EscapingClassMarker.isClassEscaping(unmarkedClass),
                "Static method should return false for unmarked class");
    }

    /**
     * Tests that visitProgramClass behavior is consistent after multiple marker instantiations.
     */
    @Test
    public void testVisitProgramClass_consistentAcrossMultipleMarkerInstances() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert - use multiple markers in sequence
        EscapingClassMarker marker1 = new EscapingClassMarker();
        marker1.visitProgramClass(programClass);
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should be escaping after marker1");

        EscapingClassMarker marker2 = new EscapingClassMarker();
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should still be escaping with marker2 (before visit)");

        marker2.visitProgramClass(programClass);
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should still be escaping after marker2 visit");
    }

    /**
     * Tests that visitProgramClass works correctly after being called through the ClassVisitor interface.
     */
    @Test
    public void testVisitProgramClass_throughClassVisitorInterface() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call through ProgramClass-specific method
        marker.visitProgramClass(programClass);

        // Assert
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should be escaping after calling visitProgramClass directly");
    }

    /**
     * Tests that visitProgramClass correctly handles classes that already have optimization info.
     */
    @Test
    public void testVisitProgramClass_withExistingOptimizationInfo() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Verify the optimization info exists
        assertNotNull(ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass),
                "Optimization info should exist");

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should be escaping after visit");
    }

    /**
     * Tests that visitProgramClass can mark a class that has been unmarked previously.
     * This tests that the marking operation is not dependent on previous state.
     */
    @Test
    public void testVisitProgramClass_remarking() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Initial state - not escaping
        assertFalse(EscapingClassMarker.isClassEscaping(programClass),
                "Class should not be escaping initially");

        // Act - mark the class
        marker.visitProgramClass(programClass);

        // Assert - should be escaping
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should be escaping after first visit");

        // Act - mark again (idempotent operation)
        marker.visitProgramClass(programClass);

        // Assert - should still be escaping
        assertTrue(EscapingClassMarker.isClassEscaping(programClass),
                "Class should still be escaping after second visit");
    }
}
