package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link CaughtClassMarker#visitProgramClass(ProgramClass)}.
 *
 * The visitProgramClass method marks program classes as "caught", meaning they are
 * exception classes that occur in exception handlers. This is done by calling
 * ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(clazz).setCaught().
 *
 * These tests verify that:
 * 1. The method correctly marks classes as caught
 * 2. The caught status can be verified using CaughtClassMarker.isCaught()
 * 3. The method handles multiple invocations correctly
 * 4. The method works with different ProgramClass instances
 */
public class CaughtClassMarkerClaude_visitProgramClassTest {

    private CaughtClassMarker marker;

    @BeforeEach
    public void setUp() {
        marker = new CaughtClassMarker();
    }

    /**
     * Tests that visitProgramClass marks a ProgramClass as caught.
     */
    @Test
    public void testVisitProgramClass_marksClassAsCaught() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Verify initial state - should not be caught
        assertFalse(CaughtClassMarker.isCaught(programClass),
                "Class should not be marked as caught initially");

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertTrue(CaughtClassMarker.isCaught(programClass),
                "Class should be marked as caught after visitProgramClass");
    }

    /**
     * Tests that visitProgramClass can be called multiple times on the same class.
     * The class should remain marked as caught.
     */
    @Test
    public void testVisitProgramClass_calledMultipleTimes_remainsCaught() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call multiple times
        marker.visitProgramClass(programClass);
        marker.visitProgramClass(programClass);
        marker.visitProgramClass(programClass);

        // Assert - should still be caught
        assertTrue(CaughtClassMarker.isCaught(programClass),
                "Class should remain marked as caught after multiple visits");
    }

    /**
     * Tests that visitProgramClass correctly marks multiple different classes as caught.
     */
    @Test
    public void testVisitProgramClass_multipleDifferentClasses_allMarkedAsCaught() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        // Verify initial state
        assertFalse(CaughtClassMarker.isCaught(class1), "Class1 should not be caught initially");
        assertFalse(CaughtClassMarker.isCaught(class2), "Class2 should not be caught initially");
        assertFalse(CaughtClassMarker.isCaught(class3), "Class3 should not be caught initially");

        // Act
        marker.visitProgramClass(class1);
        marker.visitProgramClass(class2);
        marker.visitProgramClass(class3);

        // Assert
        assertTrue(CaughtClassMarker.isCaught(class1), "Class1 should be marked as caught");
        assertTrue(CaughtClassMarker.isCaught(class2), "Class2 should be marked as caught");
        assertTrue(CaughtClassMarker.isCaught(class3), "Class3 should be marked as caught");
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
     * Tests that multiple CaughtClassMarker instances can mark the same class.
     * The caught status is stored in the class, not the marker.
     */
    @Test
    public void testVisitProgramClass_multipleMarkerInstances_sameBehavior() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        CaughtClassMarker marker1 = new CaughtClassMarker();
        CaughtClassMarker marker2 = new CaughtClassMarker();

        // Act - use first marker
        marker1.visitProgramClass(programClass);

        // Assert - verify caught using both static method and after using second marker
        assertTrue(CaughtClassMarker.isCaught(programClass),
                "Class should be caught after first marker visits");

        // Act - use second marker
        marker2.visitProgramClass(programClass);

        // Assert - should still be caught
        assertTrue(CaughtClassMarker.isCaught(programClass),
                "Class should still be caught after second marker visits");
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

        // Assert - only class1 should be caught
        assertTrue(CaughtClassMarker.isCaught(class1), "Class1 should be caught");
        assertFalse(CaughtClassMarker.isCaught(class2), "Class2 should not be caught");

        // Act - now mark class2
        marker.visitProgramClass(class2);

        // Assert - both should be caught
        assertTrue(CaughtClassMarker.isCaught(class1), "Class1 should still be caught");
        assertTrue(CaughtClassMarker.isCaught(class2), "Class2 should now be caught");
    }

    /**
     * Tests that the caught status persists after the marker is used.
     */
    @Test
    public void testVisitProgramClass_caughtStatusPersists() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitProgramClass(programClass);

        // Assert - check status multiple times
        assertTrue(CaughtClassMarker.isCaught(programClass), "Class should be caught - first check");
        assertTrue(CaughtClassMarker.isCaught(programClass), "Class should be caught - second check");
        assertTrue(CaughtClassMarker.isCaught(programClass), "Class should be caught - third check");
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

        // Verify still caught after all calls
        assertTrue(CaughtClassMarker.isCaught(programClass),
                "Class should be caught after 100 visits");
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
        assertTrue(CaughtClassMarker.isCaught(programClass),
                "Freshly created class should be marked as caught");
    }

    /**
     * Tests that isCaught returns false for a class that hasn't been visited.
     */
    @Test
    public void testVisitProgramClass_unvisitedClass_notCaught() {
        // Arrange
        ProgramClass visitedClass = new ProgramClass();
        ProgramClass unvisitedClass = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(visitedClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(unvisitedClass);

        // Act - visit only one class
        marker.visitProgramClass(visitedClass);

        // Assert
        assertTrue(CaughtClassMarker.isCaught(visitedClass),
                "Visited class should be caught");
        assertFalse(CaughtClassMarker.isCaught(unvisitedClass),
                "Unvisited class should not be caught");
    }

    /**
     * Tests that visitProgramClass works correctly when called from multiple threads.
     * The caught flag is volatile, so it should be thread-safe.
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
                CaughtClassMarker threadMarker = new CaughtClassMarker();
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

        // Assert - all classes should be marked as caught
        for (int i = 0; i < threadCount; i++) {
            for (int j = 0; j < classesPerThread; j++) {
                assertTrue(CaughtClassMarker.isCaught(classes[i][j]),
                        "Class [" + i + "][" + j + "] should be marked as caught");
            }
        }
    }
}
