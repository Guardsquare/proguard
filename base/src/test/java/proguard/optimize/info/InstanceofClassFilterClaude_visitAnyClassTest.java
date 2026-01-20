package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstanceofClassFilter#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in InstanceofClassFilter filters classes based on whether
 * they are marked as "instanceofed" (used in instanceof tests). If a class is marked
 * as instanceofed, the method delegates to the wrapped ClassVisitor by calling
 * clazz.accept(classVisitor).
 *
 * Key behavior:
 * - If InstanceofClassMarker.isInstanceofed(clazz) returns true, the class is accepted by the wrapped visitor
 * - If InstanceofClassMarker.isInstanceofed(clazz) returns false, the class is filtered out (no delegation)
 * - The filtering decision is based on optimization info stored in the class itself
 *
 * These tests verify:
 * 1. Classes marked as instanceofed are delegated to the visitor
 * 2. Classes not marked as instanceofed are filtered out
 * 3. The method works correctly with both ProgramClass and LibraryClass
 * 4. The method handles null visitors appropriately
 * 5. Multiple calls behave consistently
 * 6. The filtering is independent for different class instances
 */
public class InstanceofClassFilterClaude_visitAnyClassTest {

    private InstanceofClassFilter filter;
    private TrackingClassVisitor trackingVisitor;

    @BeforeEach
    public void setUp() {
        trackingVisitor = new TrackingClassVisitor();
        filter = new InstanceofClassFilter(trackingVisitor);
    }

    /**
     * Tests that visitAnyClass delegates to the visitor for an instanceofed class.
     */
    @Test
    public void testVisitAnyClass_withInstanceofedClass_delegatesToVisitor() {
        // Arrange - Create an instanceofed class
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setInstanceofed();

        // Act
        filter.visitAnyClass(programClass);

        // Assert
        assertTrue(trackingVisitor.visited, "Visitor should be called for instanceofed class");
        assertEquals(1, trackingVisitor.visitCount, "Visitor should be called exactly once");
    }

    /**
     * Tests that visitAnyClass does NOT delegate to the visitor for a non-instanceofed class.
     */
    @Test
    public void testVisitAnyClass_withNonInstanceofedClass_doesNotDelegateToVisitor() {
        // Arrange - Create a class that is NOT instanceofed
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        // Note: not calling setInstanceofed()

        // Act
        filter.visitAnyClass(programClass);

        // Assert
        assertFalse(trackingVisitor.visited, "Visitor should NOT be called for non-instanceofed class");
        assertEquals(0, trackingVisitor.visitCount, "Visitor should not be called");
    }

    /**
     * Tests visitAnyClass with multiple instanceofed classes.
     */
    @Test
    public void testVisitAnyClass_multipleInstanceofedClasses_allDelegated() {
        // Arrange - Create multiple instanceofed classes
        ProgramClass class1 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class1).setInstanceofed();

        ProgramClass class2 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class2).setInstanceofed();

        ProgramClass class3 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class3).setInstanceofed();

        // Act
        filter.visitAnyClass(class1);
        filter.visitAnyClass(class2);
        filter.visitAnyClass(class3);

        // Assert
        assertEquals(3, trackingVisitor.visitCount, "Visitor should be called three times");
    }

    /**
     * Tests visitAnyClass with mixed instanceofed and non-instanceofed classes.
     */
    @Test
    public void testVisitAnyClass_mixedClasses_onlyInstanceofedDelegated() {
        // Arrange
        ProgramClass instanceofedClass1 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedClass1);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedClass1).setInstanceofed();

        ProgramClass nonInstanceofedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonInstanceofedClass);

        ProgramClass instanceofedClass2 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedClass2);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedClass2).setInstanceofed();

        // Act
        filter.visitAnyClass(instanceofedClass1);
        filter.visitAnyClass(nonInstanceofedClass);
        filter.visitAnyClass(instanceofedClass2);

        // Assert
        assertEquals(2, trackingVisitor.visitCount,
            "Visitor should be called only for the 2 instanceofed classes");
    }

    /**
     * Tests visitAnyClass with LibraryClass that is instanceofed.
     */
    @Test
    public void testVisitAnyClass_instanceofedLibraryClass_delegatesToVisitor() {
        // Arrange - Create an instanceofed library class
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(libraryClass).setInstanceofed();

        // Act
        filter.visitAnyClass(libraryClass);

        // Assert
        assertTrue(trackingVisitor.visited, "Visitor should be called for instanceofed library class");
        assertEquals(1, trackingVisitor.visitCount, "Visitor should be called exactly once");
    }

    /**
     * Tests visitAnyClass with LibraryClass that is NOT instanceofed.
     */
    @Test
    public void testVisitAnyClass_nonInstanceofedLibraryClass_doesNotDelegateToVisitor() {
        // Arrange - Create a library class that is NOT instanceofed
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act
        filter.visitAnyClass(libraryClass);

        // Assert
        assertFalse(trackingVisitor.visited, "Visitor should NOT be called for non-instanceofed library class");
        assertEquals(0, trackingVisitor.visitCount, "Visitor should not be called");
    }

    /**
     * Tests visitAnyClass with null visitor and instanceofed class.
     * Should throw NullPointerException when trying to delegate.
     */
    @Test
    public void testVisitAnyClass_nullVisitorWithInstanceofedClass_throwsNullPointerException() {
        // Arrange
        InstanceofClassFilter filterWithNullVisitor = new InstanceofClassFilter(null);
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setInstanceofed();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            filterWithNullVisitor.visitAnyClass(programClass);
        }, "Should throw NullPointerException when delegating to null visitor");
    }

    /**
     * Tests visitAnyClass with null visitor and non-instanceofed class.
     * Should NOT throw exception because the class is filtered out before delegation.
     */
    @Test
    public void testVisitAnyClass_nullVisitorWithNonInstanceofedClass_doesNotThrow() {
        // Arrange
        InstanceofClassFilter filterWithNullVisitor = new InstanceofClassFilter(null);
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        // Note: not calling setInstanceofed()

        // Act & Assert
        assertDoesNotThrow(() -> {
            filterWithNullVisitor.visitAnyClass(programClass);
        }, "Should not throw when filtering out non-instanceofed class with null visitor");
    }

    /**
     * Tests visitAnyClass called multiple times on the same instanceofed class.
     */
    @Test
    public void testVisitAnyClass_sameInstanceofedClassMultipleTimes_delegatesEachTime() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setInstanceofed();

        // Act
        filter.visitAnyClass(programClass);
        filter.visitAnyClass(programClass);
        filter.visitAnyClass(programClass);

        // Assert
        assertEquals(3, trackingVisitor.visitCount,
            "Visitor should be called three times for three visits");
    }

    /**
     * Tests visitAnyClass called multiple times on the same non-instanceofed class.
     */
    @Test
    public void testVisitAnyClass_sameNonInstanceofedClassMultipleTimes_neverDelegates() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        filter.visitAnyClass(programClass);
        filter.visitAnyClass(programClass);
        filter.visitAnyClass(programClass);

        // Assert
        assertEquals(0, trackingVisitor.visitCount,
            "Visitor should never be called for non-instanceofed class");
    }

    /**
     * Tests that visitAnyClass does not throw exception when called.
     */
    @Test
    public void testVisitAnyClass_doesNotThrowException() {
        // Arrange
        ProgramClass instanceofedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedClass).setInstanceofed();

        ProgramClass nonInstanceofedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonInstanceofedClass);

        // Act & Assert
        assertDoesNotThrow(() -> filter.visitAnyClass(instanceofedClass),
            "visitAnyClass should not throw for instanceofed class");
        assertDoesNotThrow(() -> filter.visitAnyClass(nonInstanceofedClass),
            "visitAnyClass should not throw for non-instanceofed class");
    }

    /**
     * Tests that visitAnyClass filtering is independent for different class instances.
     */
    @Test
    public void testVisitAnyClass_independentFilteringForDifferentClasses() {
        // Arrange
        ProgramClass instanceofedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedClass).setInstanceofed();

        ProgramClass nonInstanceofedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonInstanceofedClass);

        // Act - visit instanceofed class
        filter.visitAnyClass(instanceofedClass);
        assertEquals(1, trackingVisitor.visitCount, "Should delegate for instanceofed class");

        // Act - visit non-instanceofed class
        filter.visitAnyClass(nonInstanceofedClass);
        assertEquals(1, trackingVisitor.visitCount, "Should not delegate for non-instanceofed class");

        // Act - visit instanceofed class again
        filter.visitAnyClass(instanceofedClass);
        assertEquals(2, trackingVisitor.visitCount, "Should delegate again for instanceofed class");
    }

    /**
     * Tests visitAnyClass with rapid succession of mixed classes.
     */
    @Test
    public void testVisitAnyClass_rapidSuccessionMixedClasses() {
        // Arrange
        ProgramClass[] instanceofedClasses = new ProgramClass[50];
        ProgramClass[] nonInstanceofedClasses = new ProgramClass[50];

        for (int i = 0; i < 50; i++) {
            instanceofedClasses[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedClasses[i]);
            ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedClasses[i]).setInstanceofed();

            nonInstanceofedClasses[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonInstanceofedClasses[i]);
        }

        // Act - alternate between instanceofed and non-instanceofed
        for (int i = 0; i < 50; i++) {
            filter.visitAnyClass(instanceofedClasses[i]);
            filter.visitAnyClass(nonInstanceofedClasses[i]);
        }

        // Assert
        assertEquals(50, trackingVisitor.visitCount,
            "Visitor should be called 50 times (only for instanceofed classes)");
    }

    /**
     * Tests that visitAnyClass works correctly through the ClassVisitor interface.
     */
    @Test
    public void testVisitAnyClass_throughClassVisitorInterface() {
        // Arrange
        ClassVisitor visitorInterface = filter;
        ProgramClass instanceofedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedClass).setInstanceofed();

        // Act
        visitorInterface.visitAnyClass(instanceofedClass);

        // Assert
        assertTrue(trackingVisitor.visited,
            "Visitor should be called when invoked through ClassVisitor interface");
    }

    /**
     * Tests that visitAnyClass delegates using clazz.accept().
     * Verifies the delegation path by checking that the correct visitor method is invoked.
     */
    @Test
    public void testVisitAnyClass_delegatesViaClazzAccept() {
        // Arrange
        DetailedTrackingVisitor detailedVisitor = new DetailedTrackingVisitor();
        InstanceofClassFilter filterWithDetailedVisitor = new InstanceofClassFilter(detailedVisitor);

        ProgramClass instanceofedProgramClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedProgramClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedProgramClass).setInstanceofed();

        LibraryClass instanceofedLibraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedLibraryClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedLibraryClass).setInstanceofed();

        // Act
        filterWithDetailedVisitor.visitAnyClass(instanceofedProgramClass);
        filterWithDetailedVisitor.visitAnyClass(instanceofedLibraryClass);

        // Assert - clazz.accept() should call the specific visit methods
        assertTrue(detailedVisitor.programClassVisited,
            "visitProgramClass should be called via clazz.accept()");
        assertTrue(detailedVisitor.libraryClassVisited,
            "visitLibraryClass should be called via clazz.accept()");
    }

    /**
     * Tests visitAnyClass with multiple filter instances sharing classes.
     */
    @Test
    public void testVisitAnyClass_multipleFilterInstances_independentFiltering() {
        // Arrange
        TrackingClassVisitor visitor1 = new TrackingClassVisitor();
        TrackingClassVisitor visitor2 = new TrackingClassVisitor();
        InstanceofClassFilter filter1 = new InstanceofClassFilter(visitor1);
        InstanceofClassFilter filter2 = new InstanceofClassFilter(visitor2);

        ProgramClass instanceofedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedClass).setInstanceofed();

        // Act
        filter1.visitAnyClass(instanceofedClass);
        filter2.visitAnyClass(instanceofedClass);

        // Assert
        assertEquals(1, visitor1.visitCount, "First visitor should be called once");
        assertEquals(1, visitor2.visitCount, "Second visitor should be called once");
    }

    /**
     * Tests that visitAnyClass execution completes quickly.
     */
    @Test
    public void testVisitAnyClass_executesQuickly() {
        // Arrange
        ProgramClass instanceofedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedClass).setInstanceofed();

        long startTime = System.nanoTime();

        // Act
        for (int i = 0; i < 1000; i++) {
            filter.visitAnyClass(instanceofedClass);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert
        assertTrue(durationMs < 100,
            "visitAnyClass should execute quickly, took " + durationMs + "ms for 1000 calls");
    }

    /**
     * Tests that visitAnyClass behavior is consistent across multiple invocations.
     */
    @Test
    public void testVisitAnyClass_consistentBehavior() {
        // Arrange
        ProgramClass instanceofedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedClass).setInstanceofed();

        // Act - call multiple times and track counts
        filter.visitAnyClass(instanceofedClass);
        int count1 = trackingVisitor.visitCount;

        filter.visitAnyClass(instanceofedClass);
        int count2 = trackingVisitor.visitCount;

        filter.visitAnyClass(instanceofedClass);
        int count3 = trackingVisitor.visitCount;

        // Assert
        assertEquals(1, count1, "First call should increment count to 1");
        assertEquals(2, count2, "Second call should increment count to 2");
        assertEquals(3, count3, "Third call should increment count to 3");
    }

    /**
     * Tests visitAnyClass with a class that becomes instanceofed after initial visit.
     * Note: This tests the current state of the class at visit time.
     */
    @Test
    public void testVisitAnyClass_classStateChanges() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - visit when NOT instanceofed
        filter.visitAnyClass(programClass);
        assertEquals(0, trackingVisitor.visitCount, "Should not delegate when not instanceofed");

        // Arrange - mark as instanceofed
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setInstanceofed();

        // Act - visit when instanceofed
        filter.visitAnyClass(programClass);
        assertEquals(1, trackingVisitor.visitCount, "Should delegate when instanceofed");
    }

    /**
     * Tests that visitAnyClass correctly implements the filtering contract.
     */
    @Test
    public void testVisitAnyClass_filteringContract() {
        // Arrange - create 10 classes, mark half as instanceofed
        ProgramClass[] classes = new ProgramClass[10];
        for (int i = 0; i < 10; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
            if (i % 2 == 0) {
                ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(classes[i]).setInstanceofed();
            }
        }

        // Act - visit all classes
        for (ProgramClass clazz : classes) {
            filter.visitAnyClass(clazz);
        }

        // Assert - only 5 should be delegated (even indices: 0, 2, 4, 6, 8)
        assertEquals(5, trackingVisitor.visitCount,
            "Exactly 5 instanceofed classes should be delegated");
    }

    /**
     * Tests visitAnyClass with sequential filtering decisions.
     */
    @Test
    public void testVisitAnyClass_sequentialFilteringDecisions() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class1).setInstanceofed();

        ProgramClass class2 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        ProgramClass class3 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class3).setInstanceofed();

        // Act & Assert - check after each visit
        filter.visitAnyClass(class1);
        assertEquals(1, trackingVisitor.visitCount, "Count should be 1 after first instanceofed class");

        filter.visitAnyClass(class2);
        assertEquals(1, trackingVisitor.visitCount, "Count should still be 1 after non-instanceofed class");

        filter.visitAnyClass(class3);
        assertEquals(2, trackingVisitor.visitCount, "Count should be 2 after second instanceofed class");
    }

    /**
     * Tests that visitAnyClass handles batch processing correctly.
     */
    @Test
    public void testVisitAnyClass_batchProcessing() {
        // Arrange - create a large batch of classes
        final int batchSize = 100;
        ProgramClass[] instanceofedClasses = new ProgramClass[batchSize];

        for (int i = 0; i < batchSize; i++) {
            instanceofedClasses[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedClasses[i]);
            ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedClasses[i]).setInstanceofed();
        }

        // Act
        for (ProgramClass clazz : instanceofedClasses) {
            filter.visitAnyClass(clazz);
        }

        // Assert
        assertEquals(batchSize, trackingVisitor.visitCount,
            "All " + batchSize + " instanceofed classes should be delegated");
    }

    /**
     * Tests that visitAnyClass method signature matches ClassVisitor interface.
     */
    @Test
    public void testVisitAnyClass_methodSignatureMatchesInterface() {
        // Arrange
        ProgramClass instanceofedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedClass).setInstanceofed();

        // Act - call through interface
        ClassVisitor visitor = filter;
        assertDoesNotThrow(() -> visitor.visitAnyClass(instanceofedClass),
            "visitAnyClass should be callable through ClassVisitor interface");

        // Assert
        assertEquals(1, trackingVisitor.visitCount, "Delegation should work through interface");
    }

    /**
     * Tests that visitAnyClass filtering is deterministic.
     */
    @Test
    public void testVisitAnyClass_deterministicFiltering() {
        // Arrange
        ProgramClass instanceofedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instanceofedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instanceofedClass).setInstanceofed();

        ProgramClass nonInstanceofedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonInstanceofedClass);

        // Act - repeat the same sequence multiple times
        for (int i = 0; i < 3; i++) {
            filter.visitAnyClass(instanceofedClass);
            filter.visitAnyClass(nonInstanceofedClass);
        }

        // Assert - should have exactly 3 delegations (one per instanceofed visit)
        assertEquals(3, trackingVisitor.visitCount,
            "Filtering should be deterministic across repeated sequences");
    }

    /**
     * Helper class that tracks whether it was visited and counts visits.
     */
    private static class TrackingClassVisitor implements ClassVisitor {
        boolean visited = false;
        int visitCount = 0;

        @Override
        public void visitAnyClass(Clazz clazz) {
            visited = true;
            visitCount++;
        }

        @Override
        public void visitProgramClass(ProgramClass programClass) {
            visited = true;
            visitCount++;
        }

        @Override
        public void visitLibraryClass(LibraryClass libraryClass) {
            visited = true;
            visitCount++;
        }
    }

    /**
     * Helper class that tracks which specific visitor methods were called.
     */
    private static class DetailedTrackingVisitor implements ClassVisitor {
        boolean programClassVisited = false;
        boolean libraryClassVisited = false;

        @Override
        public void visitAnyClass(Clazz clazz) {
            // Not expected to be called directly in this test
        }

        @Override
        public void visitProgramClass(ProgramClass programClass) {
            programClassVisited = true;
        }

        @Override
        public void visitLibraryClass(LibraryClass libraryClass) {
            libraryClassVisited = true;
        }
    }
}
