package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SideEffectClassFilter#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in SideEffectClassFilter filters classes based on whether
 * they have side effects when initialized. If a class is marked as having side effects,
 * the method delegates to the wrapped ClassVisitor by calling clazz.accept(classVisitor).
 *
 * Key behavior:
 * - If SideEffectClassMarker.hasSideEffects(clazz) returns true, the class is accepted by the wrapped visitor
 * - If SideEffectClassMarker.hasSideEffects(clazz) returns false, the class is filtered out (no delegation)
 * - The filtering decision is based on optimization info stored in the class itself
 *
 * These tests verify:
 * 1. Classes marked as having side effects are delegated to the visitor
 * 2. Classes not marked as having side effects are filtered out
 * 3. The method works correctly with both ProgramClass and LibraryClass
 * 4. The method handles null visitors appropriately
 * 5. Multiple calls behave consistently
 * 6. The filtering is independent for different class instances
 */
public class SideEffectClassFilterClaude_visitAnyClassTest {

    private SideEffectClassFilter filter;
    private TrackingClassVisitor trackingVisitor;

    @BeforeEach
    public void setUp() {
        trackingVisitor = new TrackingClassVisitor();
        filter = new SideEffectClassFilter(trackingVisitor);
    }

    /**
     * Tests that visitAnyClass delegates to the visitor for a class with side effects.
     */
    @Test
    public void testVisitAnyClass_withSideEffectClass_delegatesToVisitor() {
        // Arrange - Create a class with side effects
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setSideEffects();

        // Act
        filter.visitAnyClass(programClass);

        // Assert
        assertTrue(trackingVisitor.visited, "Visitor should be called for class with side effects");
        assertEquals(1, trackingVisitor.visitCount, "Visitor should be called exactly once");
    }

    /**
     * Tests that visitAnyClass does NOT delegate to the visitor for a class without side effects.
     */
    @Test
    public void testVisitAnyClass_withoutSideEffects_doesNotDelegateToVisitor() {
        // Arrange - Create a class that does NOT have side effects
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        // Note: not calling setSideEffects()

        // Act
        filter.visitAnyClass(programClass);

        // Assert
        assertFalse(trackingVisitor.visited, "Visitor should NOT be called for class without side effects");
        assertEquals(0, trackingVisitor.visitCount, "Visitor should not be called");
    }

    /**
     * Tests visitAnyClass with multiple classes having side effects.
     */
    @Test
    public void testVisitAnyClass_multipleSideEffectClasses_allDelegated() {
        // Arrange - Create multiple classes with side effects
        ProgramClass class1 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class1).setSideEffects();

        ProgramClass class2 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class2).setSideEffects();

        ProgramClass class3 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class3).setSideEffects();

        // Act
        filter.visitAnyClass(class1);
        filter.visitAnyClass(class2);
        filter.visitAnyClass(class3);

        // Assert
        assertEquals(3, trackingVisitor.visitCount, "Visitor should be called three times");
    }

    /**
     * Tests visitAnyClass with mixed classes with and without side effects.
     */
    @Test
    public void testVisitAnyClass_mixedClasses_onlySideEffectClassesDelegated() {
        // Arrange
        ProgramClass sideEffectClass1 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(sideEffectClass1);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(sideEffectClass1).setSideEffects();

        ProgramClass noSideEffectClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(noSideEffectClass);

        ProgramClass sideEffectClass2 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(sideEffectClass2);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(sideEffectClass2).setSideEffects();

        // Act
        filter.visitAnyClass(sideEffectClass1);
        filter.visitAnyClass(noSideEffectClass);
        filter.visitAnyClass(sideEffectClass2);

        // Assert
        assertEquals(2, trackingVisitor.visitCount,
            "Visitor should be called only for the 2 classes with side effects");
    }

    /**
     * Tests visitAnyClass with LibraryClass that has side effects.
     */
    @Test
    public void testVisitAnyClass_sideEffectLibraryClass_delegatesToVisitor() {
        // Arrange - Create a library class with side effects
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(libraryClass).setSideEffects();

        // Act
        filter.visitAnyClass(libraryClass);

        // Assert
        assertTrue(trackingVisitor.visited, "Visitor should be called for library class with side effects");
        assertEquals(1, trackingVisitor.visitCount, "Visitor should be called exactly once");
    }

    /**
     * Tests visitAnyClass with LibraryClass that does NOT have side effects.
     */
    @Test
    public void testVisitAnyClass_noSideEffectLibraryClass_doesNotDelegateToVisitor() {
        // Arrange - Create a library class that does NOT have side effects
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act
        filter.visitAnyClass(libraryClass);

        // Assert
        assertFalse(trackingVisitor.visited, "Visitor should NOT be called for library class without side effects");
        assertEquals(0, trackingVisitor.visitCount, "Visitor should not be called");
    }

    /**
     * Tests visitAnyClass with null visitor and class with side effects.
     * Should throw NullPointerException when trying to delegate.
     */
    @Test
    public void testVisitAnyClass_nullVisitorWithSideEffectClass_throwsNullPointerException() {
        // Arrange
        SideEffectClassFilter filterWithNullVisitor = new SideEffectClassFilter(null);
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setSideEffects();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            filterWithNullVisitor.visitAnyClass(programClass);
        }, "Should throw NullPointerException when delegating to null visitor");
    }

    /**
     * Tests visitAnyClass with null visitor and class without side effects.
     * Should NOT throw exception because the class is filtered out before delegation.
     */
    @Test
    public void testVisitAnyClass_nullVisitorWithoutSideEffectClass_doesNotThrow() {
        // Arrange
        SideEffectClassFilter filterWithNullVisitor = new SideEffectClassFilter(null);
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        // Note: not calling setSideEffects()

        // Act & Assert
        assertDoesNotThrow(() -> {
            filterWithNullVisitor.visitAnyClass(programClass);
        }, "Should not throw when filtering out class without side effects with null visitor");
    }

    /**
     * Tests visitAnyClass called multiple times on the same class with side effects.
     */
    @Test
    public void testVisitAnyClass_sameSideEffectClassMultipleTimes_delegatesEachTime() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setSideEffects();

        // Act
        filter.visitAnyClass(programClass);
        filter.visitAnyClass(programClass);
        filter.visitAnyClass(programClass);

        // Assert
        assertEquals(3, trackingVisitor.visitCount,
            "Visitor should be called three times for three visits");
    }

    /**
     * Tests visitAnyClass called multiple times on the same class without side effects.
     */
    @Test
    public void testVisitAnyClass_sameNoSideEffectClassMultipleTimes_neverDelegates() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        filter.visitAnyClass(programClass);
        filter.visitAnyClass(programClass);
        filter.visitAnyClass(programClass);

        // Assert
        assertEquals(0, trackingVisitor.visitCount,
            "Visitor should never be called for class without side effects");
    }

    /**
     * Tests that visitAnyClass does not throw exception when called.
     */
    @Test
    public void testVisitAnyClass_doesNotThrowException() {
        // Arrange
        ProgramClass sideEffectClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(sideEffectClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(sideEffectClass).setSideEffects();

        ProgramClass noSideEffectClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(noSideEffectClass);

        // Act & Assert
        assertDoesNotThrow(() -> filter.visitAnyClass(sideEffectClass),
            "visitAnyClass should not throw for class with side effects");
        assertDoesNotThrow(() -> filter.visitAnyClass(noSideEffectClass),
            "visitAnyClass should not throw for class without side effects");
    }

    /**
     * Tests that visitAnyClass filtering is independent for different class instances.
     */
    @Test
    public void testVisitAnyClass_independentFilteringForDifferentClasses() {
        // Arrange
        ProgramClass sideEffectClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(sideEffectClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(sideEffectClass).setSideEffects();

        ProgramClass noSideEffectClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(noSideEffectClass);

        // Act - visit class with side effects
        filter.visitAnyClass(sideEffectClass);
        assertEquals(1, trackingVisitor.visitCount, "Should delegate for class with side effects");

        // Act - visit class without side effects
        filter.visitAnyClass(noSideEffectClass);
        assertEquals(1, trackingVisitor.visitCount, "Should not delegate for class without side effects");

        // Act - visit class with side effects again
        filter.visitAnyClass(sideEffectClass);
        assertEquals(2, trackingVisitor.visitCount, "Should delegate again for class with side effects");
    }

    /**
     * Tests visitAnyClass with rapid succession of mixed classes.
     */
    @Test
    public void testVisitAnyClass_rapidSuccessionMixedClasses() {
        // Arrange
        ProgramClass[] sideEffectClasses = new ProgramClass[50];
        ProgramClass[] noSideEffectClasses = new ProgramClass[50];

        for (int i = 0; i < 50; i++) {
            sideEffectClasses[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(sideEffectClasses[i]);
            ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(sideEffectClasses[i]).setSideEffects();

            noSideEffectClasses[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(noSideEffectClasses[i]);
        }

        // Act - alternate between side effect and non-side effect classes
        for (int i = 0; i < 50; i++) {
            filter.visitAnyClass(sideEffectClasses[i]);
            filter.visitAnyClass(noSideEffectClasses[i]);
        }

        // Assert
        assertEquals(50, trackingVisitor.visitCount,
            "Visitor should be called 50 times (only for classes with side effects)");
    }

    /**
     * Tests that visitAnyClass works correctly through the ClassVisitor interface.
     */
    @Test
    public void testVisitAnyClass_throughClassVisitorInterface() {
        // Arrange
        ClassVisitor visitorInterface = filter;
        ProgramClass sideEffectClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(sideEffectClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(sideEffectClass).setSideEffects();

        // Act
        visitorInterface.visitAnyClass(sideEffectClass);

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
        SideEffectClassFilter filterWithDetailedVisitor = new SideEffectClassFilter(detailedVisitor);

        ProgramClass sideEffectProgramClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(sideEffectProgramClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(sideEffectProgramClass).setSideEffects();

        LibraryClass sideEffectLibraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(sideEffectLibraryClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(sideEffectLibraryClass).setSideEffects();

        // Act
        filterWithDetailedVisitor.visitAnyClass(sideEffectProgramClass);
        filterWithDetailedVisitor.visitAnyClass(sideEffectLibraryClass);

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
        SideEffectClassFilter filter1 = new SideEffectClassFilter(visitor1);
        SideEffectClassFilter filter2 = new SideEffectClassFilter(visitor2);

        ProgramClass sideEffectClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(sideEffectClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(sideEffectClass).setSideEffects();

        // Act
        filter1.visitAnyClass(sideEffectClass);
        filter2.visitAnyClass(sideEffectClass);

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
        ProgramClass sideEffectClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(sideEffectClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(sideEffectClass).setSideEffects();

        long startTime = System.nanoTime();

        // Act
        for (int i = 0; i < 1000; i++) {
            filter.visitAnyClass(sideEffectClass);
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
        ProgramClass sideEffectClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(sideEffectClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(sideEffectClass).setSideEffects();

        // Act - call multiple times and track counts
        filter.visitAnyClass(sideEffectClass);
        int count1 = trackingVisitor.visitCount;

        filter.visitAnyClass(sideEffectClass);
        int count2 = trackingVisitor.visitCount;

        filter.visitAnyClass(sideEffectClass);
        int count3 = trackingVisitor.visitCount;

        // Assert
        assertEquals(1, count1, "First call should increment count to 1");
        assertEquals(2, count2, "Second call should increment count to 2");
        assertEquals(3, count3, "Third call should increment count to 3");
    }

    /**
     * Tests visitAnyClass with a class that gets side effects after initial visit.
     * Note: This tests the current state of the class at visit time.
     */
    @Test
    public void testVisitAnyClass_classStateChanges() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - visit when NOT having side effects
        filter.visitAnyClass(programClass);
        assertEquals(0, trackingVisitor.visitCount, "Should not delegate when class has no side effects");

        // Arrange - mark as having side effects
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setSideEffects();

        // Act - visit when having side effects
        filter.visitAnyClass(programClass);
        assertEquals(1, trackingVisitor.visitCount, "Should delegate when class has side effects");
    }

    /**
     * Tests that visitAnyClass correctly implements the filtering contract.
     */
    @Test
    public void testVisitAnyClass_filteringContract() {
        // Arrange - create 10 classes, mark half as having side effects
        ProgramClass[] classes = new ProgramClass[10];
        for (int i = 0; i < 10; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
            if (i % 2 == 0) {
                ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(classes[i]).setSideEffects();
            }
        }

        // Act - visit all classes
        for (ProgramClass clazz : classes) {
            filter.visitAnyClass(clazz);
        }

        // Assert - only 5 should be delegated (even indices: 0, 2, 4, 6, 8)
        assertEquals(5, trackingVisitor.visitCount,
            "Exactly 5 classes with side effects should be delegated");
    }

    /**
     * Tests visitAnyClass with sequential filtering decisions.
     */
    @Test
    public void testVisitAnyClass_sequentialFilteringDecisions() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class1).setSideEffects();

        ProgramClass class2 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        ProgramClass class3 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(class3).setSideEffects();

        // Act & Assert - check after each visit
        filter.visitAnyClass(class1);
        assertEquals(1, trackingVisitor.visitCount, "Count should be 1 after first class with side effects");

        filter.visitAnyClass(class2);
        assertEquals(1, trackingVisitor.visitCount, "Count should still be 1 after class without side effects");

        filter.visitAnyClass(class3);
        assertEquals(2, trackingVisitor.visitCount, "Count should be 2 after second class with side effects");
    }

    /**
     * Tests that visitAnyClass handles batch processing correctly.
     */
    @Test
    public void testVisitAnyClass_batchProcessing() {
        // Arrange - create a large batch of classes
        final int batchSize = 100;
        ProgramClass[] sideEffectClasses = new ProgramClass[batchSize];

        for (int i = 0; i < batchSize; i++) {
            sideEffectClasses[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(sideEffectClasses[i]);
            ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(sideEffectClasses[i]).setSideEffects();
        }

        // Act
        for (ProgramClass clazz : sideEffectClasses) {
            filter.visitAnyClass(clazz);
        }

        // Assert
        assertEquals(batchSize, trackingVisitor.visitCount,
            "All " + batchSize + " classes with side effects should be delegated");
    }

    /**
     * Tests that visitAnyClass method signature matches ClassVisitor interface.
     */
    @Test
    public void testVisitAnyClass_methodSignatureMatchesInterface() {
        // Arrange
        ProgramClass sideEffectClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(sideEffectClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(sideEffectClass).setSideEffects();

        // Act - call through interface
        ClassVisitor visitor = filter;
        assertDoesNotThrow(() -> visitor.visitAnyClass(sideEffectClass),
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
        ProgramClass sideEffectClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(sideEffectClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(sideEffectClass).setSideEffects();

        ProgramClass noSideEffectClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(noSideEffectClass);

        // Act - repeat the same sequence multiple times
        for (int i = 0; i < 3; i++) {
            filter.visitAnyClass(sideEffectClass);
            filter.visitAnyClass(noSideEffectClass);
        }

        // Assert - should have exactly 3 delegations (one per side effect visit)
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
