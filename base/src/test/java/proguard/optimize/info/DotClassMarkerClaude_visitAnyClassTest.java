package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DotClassMarker#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method marks classes as "dot classed", meaning they are
 * referenced through .class constructs (e.g., String.class). This is done by calling
 * ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(clazz).setDotClassed().
 *
 * This marking is important for optimization decisions - classes referenced via .class
 * constructs may need different handling than classes referenced through other means.
 *
 * These tests verify that:
 * 1. The method correctly marks classes as dot classed
 * 2. The dot classed status can be verified using DotClassMarker.isDotClassed()
 * 3. The method handles multiple invocations correctly
 * 4. The method works with different ProgramClass instances
 * 5. The marking persists across multiple checks
 * 6. Multiple markers can mark the same class
 */
public class DotClassMarkerClaude_visitAnyClassTest {

    private DotClassMarker marker;

    @BeforeEach
    public void setUp() {
        marker = new DotClassMarker();
    }

    /**
     * Tests that visitAnyClass marks a ProgramClass as dot classed.
     */
    @Test
    public void testVisitAnyClass_marksClassAsDotClassed() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Verify initial state - should not be dot classed
        assertFalse(DotClassMarker.isDotClassed(programClass),
                "Class should not be marked as dot classed initially");

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should be marked as dot classed after visitAnyClass");
    }

    /**
     * Tests that visitAnyClass can be called multiple times on the same class.
     * The class should remain marked as dot classed.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_remainsDotClassed() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call multiple times
        marker.visitAnyClass(programClass);
        marker.visitAnyClass(programClass);
        marker.visitAnyClass(programClass);

        // Assert - should still be dot classed
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should remain marked as dot classed after multiple visits");
    }

    /**
     * Tests that visitAnyClass correctly marks multiple different classes as dot classed.
     */
    @Test
    public void testVisitAnyClass_multipleDifferentClasses_allMarkedAsDotClassed() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        // Verify initial state
        assertFalse(DotClassMarker.isDotClassed(class1), "Class1 should not be dot classed initially");
        assertFalse(DotClassMarker.isDotClassed(class2), "Class2 should not be dot classed initially");
        assertFalse(DotClassMarker.isDotClassed(class3), "Class3 should not be dot classed initially");

        // Act
        marker.visitAnyClass(class1);
        marker.visitAnyClass(class2);
        marker.visitAnyClass(class3);

        // Assert
        assertTrue(DotClassMarker.isDotClassed(class1), "Class1 should be marked as dot classed");
        assertTrue(DotClassMarker.isDotClassed(class2), "Class2 should be marked as dot classed");
        assertTrue(DotClassMarker.isDotClassed(class3), "Class3 should be marked as dot classed");
    }

    /**
     * Tests that visitAnyClass doesn't throw an exception when called.
     */
    @Test
    public void testVisitAnyClass_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyClass(programClass),
                "visitAnyClass should not throw an exception");
    }

    /**
     * Tests that multiple DotClassMarker instances can mark the same class.
     * The dot classed status is stored in the class, not the marker.
     */
    @Test
    public void testVisitAnyClass_multipleMarkerInstances_sameBehavior() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        DotClassMarker marker1 = new DotClassMarker();
        DotClassMarker marker2 = new DotClassMarker();

        // Act - use first marker
        marker1.visitAnyClass(programClass);

        // Assert - verify dot classed using static method
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should be dot classed after first marker visits");

        // Act - use second marker
        marker2.visitAnyClass(programClass);

        // Assert - should still be dot classed
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should still be dot classed after second marker visits");
    }

    /**
     * Tests that visitAnyClass is stateless and independent across different classes.
     */
    @Test
    public void testVisitAnyClass_statelessBehavior() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        // Act - mark only class1
        marker.visitAnyClass(class1);

        // Assert - only class1 should be dot classed
        assertTrue(DotClassMarker.isDotClassed(class1), "Class1 should be dot classed");
        assertFalse(DotClassMarker.isDotClassed(class2), "Class2 should not be dot classed");

        // Act - now mark class2
        marker.visitAnyClass(class2);

        // Assert - both should be dot classed
        assertTrue(DotClassMarker.isDotClassed(class1), "Class1 should still be dot classed");
        assertTrue(DotClassMarker.isDotClassed(class2), "Class2 should now be dot classed");
    }

    /**
     * Tests that the dot classed status persists after the marker is used.
     */
    @Test
    public void testVisitAnyClass_dotClassedStatusPersists() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert - check status multiple times
        assertTrue(DotClassMarker.isDotClassed(programClass), "Class should be dot classed - first check");
        assertTrue(DotClassMarker.isDotClassed(programClass), "Class should be dot classed - second check");
        assertTrue(DotClassMarker.isDotClassed(programClass), "Class should be dot classed - third check");
    }

    /**
     * Tests that visitAnyClass can be called in rapid succession.
     */
    @Test
    public void testVisitAnyClass_rapidSuccession() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                marker.visitAnyClass(programClass);
            }
        }, "visitAnyClass should handle rapid successive calls");

        // Verify still dot classed after all calls
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should be dot classed after 100 visits");
    }

    /**
     * Tests that visitAnyClass works correctly with freshly created ProgramClass instances.
     */
    @Test
    public void testVisitAnyClass_freshlyCreatedClass() {
        // Arrange - create a fresh class right before visiting
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - visit immediately after creation
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Freshly created class should be marked as dot classed");
    }

    /**
     * Tests that isDotClassed returns false for a class that hasn't been visited.
     */
    @Test
    public void testVisitAnyClass_unvisitedClass_notDotClassed() {
        // Arrange
        ProgramClass visitedClass = new ProgramClass();
        ProgramClass unvisitedClass = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(visitedClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(unvisitedClass);

        // Act - visit only one class
        marker.visitAnyClass(visitedClass);

        // Assert
        assertTrue(DotClassMarker.isDotClassed(visitedClass),
                "Visited class should be dot classed");
        assertFalse(DotClassMarker.isDotClassed(unvisitedClass),
                "Unvisited class should not be dot classed");
    }

    /**
     * Tests that visitAnyClass works correctly when called from multiple threads.
     * The dot classed flag should be thread-safe.
     */
    @Test
    public void testVisitAnyClass_concurrentAccess() throws InterruptedException {
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
                DotClassMarker threadMarker = new DotClassMarker();
                for (int j = 0; j < classesPerThread; j++) {
                    threadMarker.visitAnyClass(classes[threadIndex][j]);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - all classes should be marked as dot classed
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < classesPerThread; j++) {
                assertTrue(DotClassMarker.isDotClassed(classes[i][j]),
                        "Class [" + i + "][" + j + "] should be marked as dot classed");
            }
        }
    }

    /**
     * Tests that visitAnyClass implements the ClassVisitor interface correctly.
     */
    @Test
    public void testVisitAnyClass_implementsClassVisitorInterface() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Assert - marker should be a ClassVisitor
        assertTrue(marker instanceof ClassVisitor,
                "DotClassMarker should implement ClassVisitor");

        // Act - call through interface
        ClassVisitor visitor = marker;
        visitor.visitAnyClass(programClass);

        // Assert
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should be dot classed after visiting through ClassVisitor interface");
    }

    /**
     * Tests that visitAnyClass can be called through the ClassVisitor interface.
     */
    @Test
    public void testVisitAnyClass_throughClassVisitorInterface_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ClassVisitor visitor = marker;

        // Act
        visitor.visitAnyClass(programClass);

        // Assert
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should be dot classed when called through ClassVisitor interface");
    }

    /**
     * Tests that visitAnyClass with sequential calls on different classes marks each independently.
     */
    @Test
    public void testVisitAnyClass_sequentialCallsOnDifferentClasses_eachMarkedIndependently() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        // Act - mark sequentially
        marker.visitAnyClass(class1);
        assertTrue(DotClassMarker.isDotClassed(class1), "Class1 should be dot classed after visit");
        assertFalse(DotClassMarker.isDotClassed(class2), "Class2 should not be dot classed yet");
        assertFalse(DotClassMarker.isDotClassed(class3), "Class3 should not be dot classed yet");

        marker.visitAnyClass(class2);
        assertTrue(DotClassMarker.isDotClassed(class1), "Class1 should still be dot classed");
        assertTrue(DotClassMarker.isDotClassed(class2), "Class2 should be dot classed after visit");
        assertFalse(DotClassMarker.isDotClassed(class3), "Class3 should not be dot classed yet");

        marker.visitAnyClass(class3);
        assertTrue(DotClassMarker.isDotClassed(class1), "Class1 should still be dot classed");
        assertTrue(DotClassMarker.isDotClassed(class2), "Class2 should still be dot classed");
        assertTrue(DotClassMarker.isDotClassed(class3), "Class3 should be dot classed after visit");
    }

    /**
     * Tests that visitAnyClass idempotence - calling it multiple times has the same effect as calling it once.
     */
    @Test
    public void testVisitAnyClass_idempotent() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call once
        marker.visitAnyClass(programClass);
        boolean afterFirstCall = DotClassMarker.isDotClassed(programClass);

        // Act - call again
        marker.visitAnyClass(programClass);
        boolean afterSecondCall = DotClassMarker.isDotClassed(programClass);

        // Assert - both should be true and equal
        assertTrue(afterFirstCall, "Class should be dot classed after first call");
        assertTrue(afterSecondCall, "Class should be dot classed after second call");
        assertEquals(afterFirstCall, afterSecondCall,
                "Dot classed status should be the same after multiple calls");
    }

    /**
     * Tests that visitAnyClass works correctly when alternating between different markers.
     */
    @Test
    public void testVisitAnyClass_alternatingMarkers() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        DotClassMarker marker1 = new DotClassMarker();
        DotClassMarker marker2 = new DotClassMarker();

        // Act - alternate between markers
        marker1.visitAnyClass(programClass);
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should be dot classed after marker1 visit");

        marker2.visitAnyClass(programClass);
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should still be dot classed after marker2 visit");

        marker1.visitAnyClass(programClass);
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should still be dot classed after marker1 revisit");
    }

    /**
     * Tests that visitAnyClass marking persists across creation of new marker instances.
     */
    @Test
    public void testVisitAnyClass_persistsAcrossMarkerInstances() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        DotClassMarker marker1 = new DotClassMarker();

        // Act - mark with first marker
        marker1.visitAnyClass(programClass);

        // Assert - verify with static method
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should be dot classed after marker1 visit");

        // Create new marker and verify status still persists
        DotClassMarker marker2 = new DotClassMarker();
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should still be dot classed with new marker instance");
    }

    /**
     * Tests that visitAnyClass works correctly with many classes in a batch.
     */
    @Test
    public void testVisitAnyClass_batchProcessing() {
        // Arrange
        final int classCount = 50;
        ProgramClass[] classes = new ProgramClass[classCount];

        for (int i = 0; i < classCount; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
        }

        // Act - mark all classes
        for (ProgramClass clazz : classes) {
            marker.visitAnyClass(clazz);
        }

        // Assert - all should be dot classed
        for (int i = 0; i < classCount; i++) {
            assertTrue(DotClassMarker.isDotClassed(classes[i]),
                    "Class " + i + " should be dot classed");
        }
    }

    /**
     * Tests that visitAnyClass execution completes quickly.
     */
    @Test
    public void testVisitAnyClass_executesQuickly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 1000; i++) {
            marker.visitAnyClass(programClass);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
                "visitAnyClass should execute quickly, took " + durationMs + "ms for 1000 calls");
    }

    /**
     * Tests that visitAnyClass with interleaved calls on different classes works correctly.
     */
    @Test
    public void testVisitAnyClass_interleavedCalls() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        // Act - interleave calls
        marker.visitAnyClass(class1);
        marker.visitAnyClass(class2);
        marker.visitAnyClass(class1);
        marker.visitAnyClass(class2);

        // Assert - both should be dot classed
        assertTrue(DotClassMarker.isDotClassed(class1),
                "Class1 should be dot classed");
        assertTrue(DotClassMarker.isDotClassed(class2),
                "Class2 should be dot classed");
    }

    /**
     * Tests that visitAnyClass method signature matches the ClassVisitor interface.
     */
    @Test
    public void testVisitAnyClass_methodSignatureMatchesInterface() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - verify method can be called through interface
        ClassVisitor visitor = marker;
        assertDoesNotThrow(() -> visitor.visitAnyClass(programClass),
                "visitAnyClass should be callable through ClassVisitor interface");

        // Assert
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should be dot classed");
    }

    /**
     * Tests that visitAnyClass doesn't interfere with other marker instances.
     */
    @Test
    public void testVisitAnyClass_noInterferenceBetweenMarkers() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        DotClassMarker marker1 = new DotClassMarker();
        DotClassMarker marker2 = new DotClassMarker();

        // Act - mark different classes with different markers
        marker1.visitAnyClass(class1);
        marker2.visitAnyClass(class2);

        // Assert - each class should be marked independently
        assertTrue(DotClassMarker.isDotClassed(class1),
                "Class1 should be dot classed by marker1");
        assertTrue(DotClassMarker.isDotClassed(class2),
                "Class2 should be dot classed by marker2");
    }

    /**
     * Tests that visitAnyClass completion is deterministic.
     */
    @Test
    public void testVisitAnyClass_deterministicBehavior() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call multiple times and check status after each call
        marker.visitAnyClass(programClass);
        boolean status1 = DotClassMarker.isDotClassed(programClass);

        marker.visitAnyClass(programClass);
        boolean status2 = DotClassMarker.isDotClassed(programClass);

        marker.visitAnyClass(programClass);
        boolean status3 = DotClassMarker.isDotClassed(programClass);

        // Assert - all statuses should be true and consistent
        assertTrue(status1, "Status after first call should be true");
        assertTrue(status2, "Status after second call should be true");
        assertTrue(status3, "Status after third call should be true");
        assertEquals(status1, status2, "Status should be consistent across calls");
        assertEquals(status2, status3, "Status should be consistent across calls");
    }

    /**
     * Tests that visitAnyClass works with a marker used across multiple unrelated classes.
     */
    @Test
    public void testVisitAnyClass_singleMarkerMultipleClasses() {
        // Arrange
        final int classCount = 10;
        ProgramClass[] classes = new ProgramClass[classCount];

        for (int i = 0; i < classCount; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
        }

        // Act - use single marker for all classes
        for (ProgramClass clazz : classes) {
            marker.visitAnyClass(clazz);
        }

        // Assert - all classes should be independently marked
        for (int i = 0; i < classCount; i++) {
            assertTrue(DotClassMarker.isDotClassed(classes[i]),
                    "Class " + i + " should be dot classed");
        }
    }

    /**
     * Tests that the static isDotClassed method works correctly.
     */
    @Test
    public void testVisitAnyClass_staticIsDotClassedMethod() {
        // Arrange
        ProgramClass markedClass = new ProgramClass();
        ProgramClass unmarkedClass = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(markedClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(unmarkedClass);

        // Act
        marker.visitAnyClass(markedClass);

        // Assert - verify static method returns correct values
        assertTrue(DotClassMarker.isDotClassed(markedClass),
                "Static method should return true for marked class");
        assertFalse(DotClassMarker.isDotClassed(unmarkedClass),
                "Static method should return false for unmarked class");
    }

    /**
     * Tests that visitAnyClass behavior is consistent after multiple marker instantiations.
     */
    @Test
    public void testVisitAnyClass_consistentAcrossMultipleMarkerInstances() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert - use multiple markers in sequence
        DotClassMarker marker1 = new DotClassMarker();
        marker1.visitAnyClass(programClass);
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should be dot classed after marker1");

        DotClassMarker marker2 = new DotClassMarker();
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should still be dot classed with marker2 (before visit)");

        marker2.visitAnyClass(programClass);
        assertTrue(DotClassMarker.isDotClassed(programClass),
                "Class should still be dot classed after marker2 visit");
    }
}
