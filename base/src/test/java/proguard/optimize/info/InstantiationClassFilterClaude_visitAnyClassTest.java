package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link InstantiationClassFilter#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in InstantiationClassFilter filters classes based on whether
 * they are marked as "instantiated" (created with NEW instruction). If a class is marked
 * as instantiated, the method delegates to the wrapped ClassVisitor by calling
 * clazz.accept(classVisitor).
 *
 * Key behavior:
 * - If InstantiationClassMarker.isInstantiated(clazz) returns true, the class is accepted by the wrapped visitor
 * - If InstantiationClassMarker.isInstantiated(clazz) returns false, the class is filtered out (no delegation)
 * - The filtering decision is based on optimization info stored in the class itself
 *
 * These tests verify:
 * 1. Classes marked as instantiated are delegated to the visitor
 * 2. Classes not marked as instantiated are filtered out
 * 3. The method works correctly with both ProgramClass and LibraryClass
 * 4. The method handles null visitors appropriately
 * 5. Multiple calls behave consistently
 * 6. The filtering is independent for different class instances
 */
public class InstantiationClassFilterClaude_visitAnyClassTest {

    private InstantiationClassFilter filter;
    private TrackingClassVisitor trackingVisitor;

    @BeforeEach
    public void setUp() {
        trackingVisitor = new TrackingClassVisitor();
        filter = new InstantiationClassFilter(trackingVisitor);
    }

    /**
     * Tests that visitAnyClass delegates to the visitor for an instantiated class.
     */
    @Test
    public void testVisitAnyClass_withInstantiatedClass_delegatesToVisitor() {
        // Arrange - Create an instantiated class
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setInstantiated();

        // Act
        filter.visitAnyClass(programClass);

        // Assert
        assertTrue(trackingVisitor.visited, "Visitor should be called for instantiated class");
        assertEquals(1, trackingVisitor.visitCount, "Visitor should be called exactly once");
    }

    /**
     * Tests that visitAnyClass does NOT delegate to the visitor for a non-instantiated class.
     */
    @Test
    public void testVisitAnyClass_withNonInstantiatedClass_doesNotDelegateToVisitor() {
        // Arrange - Create a class that is NOT instantiated
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        // Note: not calling setInstantiated()

        // Act
        filter.visitAnyClass(programClass);

        // Assert
        assertFalse(trackingVisitor.visited, "Visitor should NOT be called for non-instantiated class");
        assertEquals(0, trackingVisitor.visitCount, "Visitor should not be called");
    }

    /**
     * Tests visitAnyClass with multiple instantiated classes.
     */
    @Test
    public void testVisitAnyClass_multipleInstantiatedClasses_allDelegated() {
        // Arrange - Create multiple instantiated classes
        ProgramClass class1 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class1).setInstantiated();

        ProgramClass class2 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class2).setInstantiated();

        ProgramClass class3 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class3).setInstantiated();

        // Act
        filter.visitAnyClass(class1);
        filter.visitAnyClass(class2);
        filter.visitAnyClass(class3);

        // Assert
        assertEquals(3, trackingVisitor.visitCount, "Visitor should be called three times");
    }

    /**
     * Tests visitAnyClass with mixed instantiated and non-instantiated classes.
     */
    @Test
    public void testVisitAnyClass_mixedClasses_onlyInstantiatedDelegated() {
        // Arrange
        ProgramClass instantiatedClass1 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedClass1);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedClass1).setInstantiated();

        ProgramClass nonInstantiatedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonInstantiatedClass);

        ProgramClass instantiatedClass2 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedClass2);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedClass2).setInstantiated();

        // Act
        filter.visitAnyClass(instantiatedClass1);
        filter.visitAnyClass(nonInstantiatedClass);
        filter.visitAnyClass(instantiatedClass2);

        // Assert
        assertEquals(2, trackingVisitor.visitCount,
            "Visitor should be called only for the 2 instantiated classes");
    }

    /**
     * Tests visitAnyClass with LibraryClass that is instantiated.
     */
    @Test
    public void testVisitAnyClass_instantiatedLibraryClass_delegatesToVisitor() {
        // Arrange - Create an instantiated library class
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(libraryClass).setInstantiated();

        // Act
        filter.visitAnyClass(libraryClass);

        // Assert
        assertTrue(trackingVisitor.visited, "Visitor should be called for instantiated library class");
        assertEquals(1, trackingVisitor.visitCount, "Visitor should be called exactly once");
    }

    /**
     * Tests visitAnyClass with LibraryClass that is NOT instantiated.
     */
    @Test
    public void testVisitAnyClass_nonInstantiatedLibraryClass_doesNotDelegateToVisitor() {
        // Arrange - Create a library class that is NOT instantiated
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act
        filter.visitAnyClass(libraryClass);

        // Assert
        assertFalse(trackingVisitor.visited, "Visitor should NOT be called for non-instantiated library class");
        assertEquals(0, trackingVisitor.visitCount, "Visitor should not be called");
    }

    /**
     * Tests visitAnyClass with null visitor and instantiated class.
     * Should throw NullPointerException when trying to delegate.
     */
    @Test
    public void testVisitAnyClass_nullVisitorWithInstantiatedClass_throwsNullPointerException() {
        // Arrange
        InstantiationClassFilter filterWithNullVisitor = new InstantiationClassFilter(null);
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setInstantiated();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            filterWithNullVisitor.visitAnyClass(programClass);
        }, "Should throw NullPointerException when delegating to null visitor");
    }

    /**
     * Tests visitAnyClass with null visitor and non-instantiated class.
     * Should NOT throw exception because the class is filtered out before delegation.
     */
    @Test
    public void testVisitAnyClass_nullVisitorWithNonInstantiatedClass_doesNotThrow() {
        // Arrange
        InstantiationClassFilter filterWithNullVisitor = new InstantiationClassFilter(null);
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        // Note: not calling setInstantiated()

        // Act & Assert
        assertDoesNotThrow(() -> {
            filterWithNullVisitor.visitAnyClass(programClass);
        }, "Should not throw when filtering out non-instantiated class with null visitor");
    }

    /**
     * Tests visitAnyClass called multiple times on the same instantiated class.
     */
    @Test
    public void testVisitAnyClass_sameInstantiatedClassMultipleTimes_delegatesEachTime() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setInstantiated();

        // Act
        filter.visitAnyClass(programClass);
        filter.visitAnyClass(programClass);
        filter.visitAnyClass(programClass);

        // Assert
        assertEquals(3, trackingVisitor.visitCount,
            "Visitor should be called three times for three visits");
    }

    /**
     * Tests visitAnyClass called multiple times on the same non-instantiated class.
     */
    @Test
    public void testVisitAnyClass_sameNonInstantiatedClassMultipleTimes_neverDelegates() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        filter.visitAnyClass(programClass);
        filter.visitAnyClass(programClass);
        filter.visitAnyClass(programClass);

        // Assert
        assertEquals(0, trackingVisitor.visitCount,
            "Visitor should never be called for non-instantiated class");
    }

    /**
     * Tests that visitAnyClass does not throw exception when called.
     */
    @Test
    public void testVisitAnyClass_doesNotThrowException() {
        // Arrange
        ProgramClass instantiatedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedClass).setInstantiated();

        ProgramClass nonInstantiatedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonInstantiatedClass);

        // Act & Assert
        assertDoesNotThrow(() -> filter.visitAnyClass(instantiatedClass),
            "visitAnyClass should not throw for instantiated class");
        assertDoesNotThrow(() -> filter.visitAnyClass(nonInstantiatedClass),
            "visitAnyClass should not throw for non-instantiated class");
    }

    /**
     * Tests that visitAnyClass filtering is independent for different class instances.
     */
    @Test
    public void testVisitAnyClass_independentFilteringForDifferentClasses() {
        // Arrange
        ProgramClass instantiatedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedClass).setInstantiated();

        ProgramClass nonInstantiatedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonInstantiatedClass);

        // Act - visit instantiated class
        filter.visitAnyClass(instantiatedClass);
        assertEquals(1, trackingVisitor.visitCount, "Should delegate for instantiated class");

        // Act - visit non-instantiated class
        filter.visitAnyClass(nonInstantiatedClass);
        assertEquals(1, trackingVisitor.visitCount, "Should not delegate for non-instantiated class");

        // Act - visit instantiated class again
        filter.visitAnyClass(instantiatedClass);
        assertEquals(2, trackingVisitor.visitCount, "Should delegate again for instantiated class");
    }

    /**
     * Tests visitAnyClass with rapid succession of mixed classes.
     */
    @Test
    public void testVisitAnyClass_rapidSuccessionMixedClasses() {
        // Arrange
        ProgramClass[] instantiatedClasses = new ProgramClass[50];
        ProgramClass[] nonInstantiatedClasses = new ProgramClass[50];

        for (int i = 0; i < 50; i++) {
            instantiatedClasses[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedClasses[i]);
            ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedClasses[i]).setInstantiated();

            nonInstantiatedClasses[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonInstantiatedClasses[i]);
        }

        // Act - alternate between instantiated and non-instantiated
        for (int i = 0; i < 50; i++) {
            filter.visitAnyClass(instantiatedClasses[i]);
            filter.visitAnyClass(nonInstantiatedClasses[i]);
        }

        // Assert
        assertEquals(50, trackingVisitor.visitCount,
            "Visitor should be called 50 times (only for instantiated classes)");
    }

    /**
     * Tests that visitAnyClass works correctly through the ClassVisitor interface.
     */
    @Test
    public void testVisitAnyClass_throughClassVisitorInterface() {
        // Arrange
        ClassVisitor visitorInterface = filter;
        ProgramClass instantiatedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedClass).setInstantiated();

        // Act
        visitorInterface.visitAnyClass(instantiatedClass);

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
        InstantiationClassFilter filterWithDetailedVisitor = new InstantiationClassFilter(detailedVisitor);

        ProgramClass instantiatedProgramClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedProgramClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedProgramClass).setInstantiated();

        LibraryClass instantiatedLibraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedLibraryClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedLibraryClass).setInstantiated();

        // Act
        filterWithDetailedVisitor.visitAnyClass(instantiatedProgramClass);
        filterWithDetailedVisitor.visitAnyClass(instantiatedLibraryClass);

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
        InstantiationClassFilter filter1 = new InstantiationClassFilter(visitor1);
        InstantiationClassFilter filter2 = new InstantiationClassFilter(visitor2);

        ProgramClass instantiatedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedClass).setInstantiated();

        // Act
        filter1.visitAnyClass(instantiatedClass);
        filter2.visitAnyClass(instantiatedClass);

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
        ProgramClass instantiatedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedClass).setInstantiated();

        long startTime = System.nanoTime();

        // Act
        for (int i = 0; i < 1000; i++) {
            filter.visitAnyClass(instantiatedClass);
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
        ProgramClass instantiatedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedClass).setInstantiated();

        // Act - call multiple times and track counts
        filter.visitAnyClass(instantiatedClass);
        int count1 = trackingVisitor.visitCount;

        filter.visitAnyClass(instantiatedClass);
        int count2 = trackingVisitor.visitCount;

        filter.visitAnyClass(instantiatedClass);
        int count3 = trackingVisitor.visitCount;

        // Assert
        assertEquals(1, count1, "First call should increment count to 1");
        assertEquals(2, count2, "Second call should increment count to 2");
        assertEquals(3, count3, "Third call should increment count to 3");
    }

    /**
     * Tests visitAnyClass with a class that becomes instantiated after initial visit.
     * Note: This tests the current state of the class at visit time.
     */
    @Test
    public void testVisitAnyClass_classStateChanges() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - visit when NOT instantiated
        filter.visitAnyClass(programClass);
        assertEquals(0, trackingVisitor.visitCount, "Should not delegate when not instantiated");

        // Arrange - mark as instantiated
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setInstantiated();

        // Act - visit when instantiated
        filter.visitAnyClass(programClass);
        assertEquals(1, trackingVisitor.visitCount, "Should delegate when instantiated");
    }

    /**
     * Tests that visitAnyClass correctly implements the filtering contract.
     */
    @Test
    public void testVisitAnyClass_filteringContract() {
        // Arrange - create 10 classes, mark half as instantiated
        ProgramClass[] classes = new ProgramClass[10];
        for (int i = 0; i < 10; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
            if (i % 2 == 0) {
                ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(classes[i]).setInstantiated();
            }
        }

        // Act - visit all classes
        for (ProgramClass clazz : classes) {
            filter.visitAnyClass(clazz);
        }

        // Assert - only 5 should be delegated (even indices: 0, 2, 4, 6, 8)
        assertEquals(5, trackingVisitor.visitCount,
            "Exactly 5 instantiated classes should be delegated");
    }

    /**
     * Tests visitAnyClass with sequential filtering decisions.
     */
    @Test
    public void testVisitAnyClass_sequentialFilteringDecisions() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class1).setInstantiated();

        ProgramClass class2 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        ProgramClass class3 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class3).setInstantiated();

        // Act & Assert - check after each visit
        filter.visitAnyClass(class1);
        assertEquals(1, trackingVisitor.visitCount, "Count should be 1 after first instantiated class");

        filter.visitAnyClass(class2);
        assertEquals(1, trackingVisitor.visitCount, "Count should still be 1 after non-instantiated class");

        filter.visitAnyClass(class3);
        assertEquals(2, trackingVisitor.visitCount, "Count should be 2 after second instantiated class");
    }

    /**
     * Tests that visitAnyClass handles batch processing correctly.
     */
    @Test
    public void testVisitAnyClass_batchProcessing() {
        // Arrange - create a large batch of classes
        final int batchSize = 100;
        ProgramClass[] instantiatedClasses = new ProgramClass[batchSize];

        for (int i = 0; i < batchSize; i++) {
            instantiatedClasses[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedClasses[i]);
            ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedClasses[i]).setInstantiated();
        }

        // Act
        for (ProgramClass clazz : instantiatedClasses) {
            filter.visitAnyClass(clazz);
        }

        // Assert
        assertEquals(batchSize, trackingVisitor.visitCount,
            "All " + batchSize + " instantiated classes should be delegated");
    }

    /**
     * Tests that visitAnyClass method signature matches ClassVisitor interface.
     */
    @Test
    public void testVisitAnyClass_methodSignatureMatchesInterface() {
        // Arrange
        ProgramClass instantiatedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedClass).setInstantiated();

        // Act - call through interface
        ClassVisitor visitor = filter;
        assertDoesNotThrow(() -> visitor.visitAnyClass(instantiatedClass),
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
        ProgramClass instantiatedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(instantiatedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(instantiatedClass).setInstantiated();

        ProgramClass nonInstantiatedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(nonInstantiatedClass);

        // Act - repeat the same sequence multiple times
        for (int i = 0; i < 3; i++) {
            filter.visitAnyClass(instantiatedClass);
            filter.visitAnyClass(nonInstantiatedClass);
        }

        // Assert - should have exactly 3 delegations (one per instantiated visit)
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
