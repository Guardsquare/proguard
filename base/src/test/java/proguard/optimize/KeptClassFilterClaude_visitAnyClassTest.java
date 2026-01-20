package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.visitor.ClassVisitor;
import proguard.optimize.info.ClassOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link KeptClassFilter#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method is the main visitor method that:
 * 1. Checks if the class is kept (using KeepMarker.isKept)
 * 2. Selects the appropriate visitor (acceptedVisitor for kept classes, rejectedVisitor for non-kept)
 * 3. Delegates to the selected visitor by calling clazz.accept(delegateVisitor)
 *
 * This method is a filtering pattern that routes classes to different visitors based on their kept status.
 */
public class KeptClassFilterClaude_visitAnyClassTest {

    /**
     * A simple tracking visitor that records when it's been visited.
     */
    private static class TrackingVisitor implements ClassVisitor {
        private int visitCount = 0;
        private Clazz lastVisitedClazz = null;

        @Override
        public void visitAnyClass(Clazz clazz) {
            visitCount++;
            lastVisitedClazz = clazz;
        }

        public int getVisitCount() {
            return visitCount;
        }

        public Clazz getLastVisitedClazz() {
            return lastVisitedClazz;
        }

        public void reset() {
            visitCount = 0;
            lastVisitedClazz = null;
        }
    }

    /**
     * Tests that visitAnyClass delegates to acceptedVisitor when class is kept.
     * When KeepMarker.isKept returns true, the acceptedVisitor should be invoked.
     */
    @Test
    public void testVisitAnyClass_withKeptClass_delegatesToAcceptedVisitor() {
        // Arrange
        ProgramClass keptClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(keptClass);  // Mark as kept

        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        TrackingVisitor rejectedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor, rejectedVisitor);

        // Act
        filter.visitAnyClass(keptClass);

        // Assert
        assertEquals(1, acceptedVisitor.getVisitCount(), "Accepted visitor should be called once");
        assertEquals(0, rejectedVisitor.getVisitCount(), "Rejected visitor should not be called");
        assertSame(keptClass, acceptedVisitor.getLastVisitedClazz(), "Accepted visitor should receive the kept class");
    }

    /**
     * Tests that visitAnyClass delegates to rejectedVisitor when class is not kept.
     * When KeepMarker.isKept returns false, the rejectedVisitor should be invoked.
     */
    @Test
    public void testVisitAnyClass_withNonKeptClass_delegatesToRejectedVisitor() {
        // Arrange
        ProgramClass nonKeptClass = new ProgramClass();
        // Don't set ClassOptimizationInfo - class is not kept

        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        TrackingVisitor rejectedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor, rejectedVisitor);

        // Act
        filter.visitAnyClass(nonKeptClass);

        // Assert
        assertEquals(0, acceptedVisitor.getVisitCount(), "Accepted visitor should not be called");
        assertEquals(1, rejectedVisitor.getVisitCount(), "Rejected visitor should be called once");
        assertSame(nonKeptClass, rejectedVisitor.getLastVisitedClazz(), "Rejected visitor should receive the non-kept class");
    }

    /**
     * Tests that visitAnyClass does nothing when class is kept but acceptedVisitor is null.
     * This tests the single-argument constructor case where only acceptedVisitor is provided.
     */
    @Test
    public void testVisitAnyClass_withKeptClassAndNullRejectedVisitor_callsAcceptedVisitor() {
        // Arrange
        ProgramClass keptClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(keptClass);

        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor);  // Only acceptedVisitor

        // Act
        filter.visitAnyClass(keptClass);

        // Assert
        assertEquals(1, acceptedVisitor.getVisitCount(), "Accepted visitor should be called once");
        assertSame(keptClass, acceptedVisitor.getLastVisitedClazz(), "Accepted visitor should receive the kept class");
    }

    /**
     * Tests that visitAnyClass does nothing when class is not kept and rejectedVisitor is null.
     * When only acceptedVisitor is provided and class is not kept, nothing should happen.
     */
    @Test
    public void testVisitAnyClass_withNonKeptClassAndNullRejectedVisitor_doesNothing() {
        // Arrange
        ProgramClass nonKeptClass = new ProgramClass();
        // Don't set ClassOptimizationInfo - class is not kept

        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor);  // Only acceptedVisitor

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> filter.visitAnyClass(nonKeptClass));
        assertEquals(0, acceptedVisitor.getVisitCount(), "Accepted visitor should not be called");
    }

    /**
     * Tests that visitAnyClass handles multiple kept classes correctly.
     * Each kept class should be routed to the acceptedVisitor.
     */
    @Test
    public void testVisitAnyClass_withMultipleKeptClasses_delegatesAllToAcceptedVisitor() {
        // Arrange
        ProgramClass keptClass1 = new ProgramClass();
        ProgramClass keptClass2 = new ProgramClass();
        ProgramClass keptClass3 = new ProgramClass();

        ClassOptimizationInfo.setClassOptimizationInfo(keptClass1);
        ClassOptimizationInfo.setClassOptimizationInfo(keptClass2);
        ClassOptimizationInfo.setClassOptimizationInfo(keptClass3);

        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        TrackingVisitor rejectedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor, rejectedVisitor);

        // Act
        filter.visitAnyClass(keptClass1);
        filter.visitAnyClass(keptClass2);
        filter.visitAnyClass(keptClass3);

        // Assert
        assertEquals(3, acceptedVisitor.getVisitCount(), "Accepted visitor should be called three times");
        assertEquals(0, rejectedVisitor.getVisitCount(), "Rejected visitor should not be called");
        assertSame(keptClass3, acceptedVisitor.getLastVisitedClazz(), "Last visited class should be the third one");
    }

    /**
     * Tests that visitAnyClass handles multiple non-kept classes correctly.
     * Each non-kept class should be routed to the rejectedVisitor.
     */
    @Test
    public void testVisitAnyClass_withMultipleNonKeptClasses_delegatesAllToRejectedVisitor() {
        // Arrange
        ProgramClass nonKeptClass1 = new ProgramClass();
        ProgramClass nonKeptClass2 = new ProgramClass();
        ProgramClass nonKeptClass3 = new ProgramClass();

        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        TrackingVisitor rejectedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor, rejectedVisitor);

        // Act
        filter.visitAnyClass(nonKeptClass1);
        filter.visitAnyClass(nonKeptClass2);
        filter.visitAnyClass(nonKeptClass3);

        // Assert
        assertEquals(0, acceptedVisitor.getVisitCount(), "Accepted visitor should not be called");
        assertEquals(3, rejectedVisitor.getVisitCount(), "Rejected visitor should be called three times");
        assertSame(nonKeptClass3, rejectedVisitor.getLastVisitedClazz(), "Last visited class should be the third one");
    }

    /**
     * Tests that visitAnyClass correctly routes a mix of kept and non-kept classes.
     * Kept classes should go to acceptedVisitor, non-kept to rejectedVisitor.
     */
    @Test
    public void testVisitAnyClass_withMixedClasses_routesCorrectly() {
        // Arrange
        ProgramClass keptClass1 = new ProgramClass();
        ProgramClass nonKeptClass1 = new ProgramClass();
        ProgramClass keptClass2 = new ProgramClass();
        ProgramClass nonKeptClass2 = new ProgramClass();

        ClassOptimizationInfo.setClassOptimizationInfo(keptClass1);
        ClassOptimizationInfo.setClassOptimizationInfo(keptClass2);

        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        TrackingVisitor rejectedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor, rejectedVisitor);

        // Act
        filter.visitAnyClass(keptClass1);
        filter.visitAnyClass(nonKeptClass1);
        filter.visitAnyClass(keptClass2);
        filter.visitAnyClass(nonKeptClass2);

        // Assert
        assertEquals(2, acceptedVisitor.getVisitCount(), "Accepted visitor should be called twice");
        assertEquals(2, rejectedVisitor.getVisitCount(), "Rejected visitor should be called twice");
    }

    /**
     * Tests that visitAnyClass works correctly with LibraryClass instances.
     * LibraryClass should be handled the same way as ProgramClass.
     */
    @Test
    public void testVisitAnyClass_withKeptLibraryClass_delegatesToAcceptedVisitor() {
        // Arrange
        LibraryClass keptLibraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(keptLibraryClass);

        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        TrackingVisitor rejectedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor, rejectedVisitor);

        // Act
        filter.visitAnyClass(keptLibraryClass);

        // Assert
        assertEquals(1, acceptedVisitor.getVisitCount(), "Accepted visitor should be called once");
        assertEquals(0, rejectedVisitor.getVisitCount(), "Rejected visitor should not be called");
        assertSame(keptLibraryClass, acceptedVisitor.getLastVisitedClazz(), "Accepted visitor should receive the library class");
    }

    /**
     * Tests that visitAnyClass works correctly with non-kept LibraryClass instances.
     * Non-kept LibraryClass should be routed to rejectedVisitor.
     */
    @Test
    public void testVisitAnyClass_withNonKeptLibraryClass_delegatesToRejectedVisitor() {
        // Arrange
        LibraryClass nonKeptLibraryClass = new LibraryClass();

        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        TrackingVisitor rejectedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor, rejectedVisitor);

        // Act
        filter.visitAnyClass(nonKeptLibraryClass);

        // Assert
        assertEquals(0, acceptedVisitor.getVisitCount(), "Accepted visitor should not be called");
        assertEquals(1, rejectedVisitor.getVisitCount(), "Rejected visitor should be called once");
        assertSame(nonKeptLibraryClass, rejectedVisitor.getLastVisitedClazz(), "Rejected visitor should receive the library class");
    }

    /**
     * Tests that visitAnyClass can be called repeatedly on the same class.
     * The filter should consistently route the same class based on its kept status.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimesOnSameClass_consistentBehavior() {
        // Arrange
        ProgramClass keptClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(keptClass);

        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        TrackingVisitor rejectedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor, rejectedVisitor);

        // Act
        filter.visitAnyClass(keptClass);
        filter.visitAnyClass(keptClass);
        filter.visitAnyClass(keptClass);

        // Assert
        assertEquals(3, acceptedVisitor.getVisitCount(), "Accepted visitor should be called three times");
        assertEquals(0, rejectedVisitor.getVisitCount(), "Rejected visitor should not be called");
    }

    /**
     * Tests that visitAnyClass correctly updates behavior if a class's kept status changes.
     * If a class transitions from non-kept to kept, routing should change.
     */
    @Test
    public void testVisitAnyClass_afterClassBecomesKept_routesToAcceptedVisitor() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        TrackingVisitor rejectedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor, rejectedVisitor);

        // Act - visit when not kept
        filter.visitAnyClass(programClass);
        assertEquals(0, acceptedVisitor.getVisitCount(), "Should route to rejected initially");
        assertEquals(1, rejectedVisitor.getVisitCount(), "Should route to rejected initially");

        // Mark as kept
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);
        acceptedVisitor.reset();
        rejectedVisitor.reset();

        // Act - visit after becoming kept
        filter.visitAnyClass(programClass);

        // Assert
        assertEquals(1, acceptedVisitor.getVisitCount(), "Should now route to accepted");
        assertEquals(0, rejectedVisitor.getVisitCount(), "Should no longer route to rejected");
    }

    /**
     * Tests that visitAnyClass correctly updates behavior if a class's kept status is removed.
     * If a class transitions from kept to non-kept, routing should change.
     */
    @Test
    public void testVisitAnyClass_afterKeptStatusRemoved_routesToRejectedVisitor() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        TrackingVisitor rejectedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor, rejectedVisitor);

        // Act - visit when kept
        filter.visitAnyClass(programClass);
        assertEquals(1, acceptedVisitor.getVisitCount(), "Should route to accepted initially");
        assertEquals(0, rejectedVisitor.getVisitCount(), "Should not route to rejected initially");

        // Remove kept status
        programClass.setProcessingInfo(null);
        acceptedVisitor.reset();
        rejectedVisitor.reset();

        // Act - visit after removing kept status
        filter.visitAnyClass(programClass);

        // Assert
        assertEquals(0, acceptedVisitor.getVisitCount(), "Should no longer route to accepted");
        assertEquals(1, rejectedVisitor.getVisitCount(), "Should now route to rejected");
    }

    /**
     * Tests that visitAnyClass works with a visitor that throws an exception.
     * The exception should propagate up from the delegated visitor.
     */
    @Test
    public void testVisitAnyClass_withVisitorThatThrows_propagatesException() {
        // Arrange
        ProgramClass keptClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(keptClass);

        ClassVisitor throwingVisitor = new ClassVisitor() {
            @Override
            public void visitAnyClass(Clazz clazz) {
                throw new RuntimeException("Test exception");
            }
        };

        KeptClassFilter filter = new KeptClassFilter(throwingVisitor, null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            filter.visitAnyClass(keptClass);
        });
        assertEquals("Test exception", exception.getMessage());
    }

    /**
     * Tests that visitAnyClass with both visitors null doesn't throw for non-kept class.
     * When both visitors are null and class is not kept, nothing should happen.
     */
    @Test
    public void testVisitAnyClass_withBothVisitorsNull_doesNotThrow() {
        // Arrange
        ProgramClass nonKeptClass = new ProgramClass();
        KeptClassFilter filter = new KeptClassFilter(null, null);

        // Act & Assert
        assertDoesNotThrow(() -> filter.visitAnyClass(nonKeptClass));
    }

    /**
     * Tests that visitAnyClass with null acceptedVisitor but class is kept doesn't throw.
     * When acceptedVisitor is null and class is kept, nothing should happen.
     */
    @Test
    public void testVisitAnyClass_withNullAcceptedVisitorButKeptClass_doesNotThrow() {
        // Arrange
        ProgramClass keptClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(keptClass);

        TrackingVisitor rejectedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(null, rejectedVisitor);

        // Act & Assert
        assertDoesNotThrow(() -> filter.visitAnyClass(keptClass));
        assertEquals(0, rejectedVisitor.getVisitCount(), "Rejected visitor should not be called");
    }

    /**
     * Tests that visitAnyClass can handle a large number of classes efficiently.
     * The filter should handle batch processing of many classes.
     */
    @Test
    public void testVisitAnyClass_withManyClasses_handlesEfficiently() {
        // Arrange
        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        TrackingVisitor rejectedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor, rejectedVisitor);

        // Act - process 100 classes (50 kept, 50 not kept)
        for (int i = 0; i < 100; i++) {
            ProgramClass programClass = new ProgramClass();
            if (i % 2 == 0) {
                ClassOptimizationInfo.setClassOptimizationInfo(programClass);
            }
            filter.visitAnyClass(programClass);
        }

        // Assert
        assertEquals(50, acceptedVisitor.getVisitCount(), "Should process 50 kept classes");
        assertEquals(50, rejectedVisitor.getVisitCount(), "Should process 50 non-kept classes");
    }

    /**
     * Tests that visitAnyClass can work with the same filter instance used multiple times.
     * The filter should be reusable across multiple invocations.
     */
    @Test
    public void testVisitAnyClass_reusingFilterInstance_worksCorrectly() {
        // Arrange
        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        TrackingVisitor rejectedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor, rejectedVisitor);

        ProgramClass keptClass = new ProgramClass();
        ProgramClass nonKeptClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(keptClass);

        // Act - use filter multiple times in different contexts
        filter.visitAnyClass(keptClass);
        filter.visitAnyClass(nonKeptClass);
        filter.visitAnyClass(keptClass);
        filter.visitAnyClass(nonKeptClass);

        // Assert
        assertEquals(2, acceptedVisitor.getVisitCount(), "Should handle kept classes correctly");
        assertEquals(2, rejectedVisitor.getVisitCount(), "Should handle non-kept classes correctly");
    }

    /**
     * Tests integration with KeepMarker visitor.
     * After KeepMarker visits a class, KeptClassFilter should route it to acceptedVisitor.
     */
    @Test
    public void testVisitAnyClass_afterKeepMarkerVisit_routesToAcceptedVisitor() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        KeepMarker keepMarker = new KeepMarker();

        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        TrackingVisitor rejectedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor, rejectedVisitor);

        // Act - mark the class with KeepMarker, then filter it
        keepMarker.visitAnyClass(programClass);
        filter.visitAnyClass(programClass);

        // Assert
        assertEquals(1, acceptedVisitor.getVisitCount(), "Should route to accepted after KeepMarker visit");
        assertEquals(0, rejectedVisitor.getVisitCount(), "Should not route to rejected after KeepMarker visit");
        assertSame(programClass, acceptedVisitor.getLastVisitedClazz(), "Should pass the same class instance");
    }

    /**
     * Tests that visitAnyClass can be chained with other visitors.
     * The filter can be used as part of a visitor chain.
     */
    @Test
    public void testVisitAnyClass_inVisitorChain_worksCorrectly() {
        // Arrange
        ProgramClass keptClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(keptClass);

        TrackingVisitor finalVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(finalVisitor, null);

        // Create a wrapper visitor that delegates to the filter
        ClassVisitor wrapperVisitor = new ClassVisitor() {
            @Override
            public void visitAnyClass(Clazz clazz) {
                filter.visitAnyClass(clazz);
            }
        };

        // Act - visit through the chain
        wrapperVisitor.visitAnyClass(keptClass);

        // Assert
        assertEquals(1, finalVisitor.getVisitCount(), "Final visitor should be called through the chain");
        assertSame(keptClass, finalVisitor.getLastVisitedClazz(), "Same class should reach final visitor");
    }

    /**
     * Tests that visitAnyClass doesn't modify the class being visited.
     * The filter should only route, not modify the class.
     */
    @Test
    public void testVisitAnyClass_doesNotModifyClass() {
        // Arrange
        ProgramClass keptClass = new ProgramClass();
        ClassOptimizationInfo optimizationInfo = new ClassOptimizationInfo();
        keptClass.setProcessingInfo(optimizationInfo);

        TrackingVisitor acceptedVisitor = new TrackingVisitor();
        KeptClassFilter filter = new KeptClassFilter(acceptedVisitor, null);

        // Act
        filter.visitAnyClass(keptClass);

        // Assert
        assertSame(optimizationInfo, keptClass.getProcessingInfo(),
                "Class processing info should not be modified");
    }
}
