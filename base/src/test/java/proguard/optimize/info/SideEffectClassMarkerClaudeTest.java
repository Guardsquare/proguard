package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SideEffectClassMarker}.
 *
 * This ClassVisitor marks all classes that it visits as having side effects.
 *
 * The tests cover:
 * - Constructor initialization
 * - visitAnyClass (does nothing for non-program classes)
 * - visitProgramClass (marks program classes as having side effects)
 * - hasSideEffects (static utility method to check side effects status)
 */
public class SideEffectClassMarkerClaudeTest {

    private SideEffectClassMarker marker;

    @BeforeEach
    public void setUp() {
        marker = new SideEffectClassMarker();
    }

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests that the constructor successfully creates an instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        SideEffectClassMarker newMarker = new SideEffectClassMarker();

        // Assert
        assertNotNull(newMarker, "Constructor should create a valid instance");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        SideEffectClassMarker marker1 = new SideEffectClassMarker();
        SideEffectClassMarker marker2 = new SideEffectClassMarker();
        SideEffectClassMarker marker3 = new SideEffectClassMarker();

        // Assert
        assertNotNull(marker1, "First marker should be created");
        assertNotNull(marker2, "Second marker should be created");
        assertNotNull(marker3, "Third marker should be created");
        assertNotSame(marker1, marker2, "Markers should be different instances");
        assertNotSame(marker2, marker3, "Markers should be different instances");
        assertNotSame(marker1, marker3, "Markers should be different instances");
    }

    // =========================================================================
    // visitAnyClass Tests
    // =========================================================================

    /**
     * Tests that visitAnyClass does nothing for a ProgramClass.
     * The method is empty and should have no effect.
     */
    @Test
    public void testVisitAnyClass_programClass_doesNothing() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Verify initial state - should not have side effects initially
        ProgramClassOptimizationInfo info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertFalse(info.hasSideEffects(), "Class should not have side effects initially");

        // Act
        marker.visitAnyClass(programClass);

        // Assert - should still not have side effects (visitAnyClass does nothing)
        info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertFalse(info.hasSideEffects(), "Class should still not have side effects after visitAnyClass");
    }

    /**
     * Tests that visitAnyClass does nothing for a LibraryClass.
     */
    @Test
    public void testVisitAnyClass_libraryClass_doesNothing() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Verify initial state
        ClassOptimizationInfo info = ClassOptimizationInfo.getClassOptimizationInfo(libraryClass);
        assertTrue(info.hasSideEffects(), "Library class should have side effects initially");

        // Act
        marker.visitAnyClass(libraryClass);

        // Assert - should remain unchanged
        info = ClassOptimizationInfo.getClassOptimizationInfo(libraryClass);
        assertTrue(info.hasSideEffects(), "Library class should still have side effects after visitAnyClass");
    }

    /**
     * Tests that visitAnyClass can be called multiple times without effect.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_noEffect() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call multiple times
        marker.visitAnyClass(programClass);
        marker.visitAnyClass(programClass);
        marker.visitAnyClass(programClass);

        // Assert - should still have no effect
        assertFalse(SideEffectClassMarker.hasSideEffects(programClass),
                "Multiple calls to visitAnyClass should have no effect");
    }

    // =========================================================================
    // visitProgramClass Tests
    // =========================================================================

    /**
     * Tests that visitProgramClass marks a ProgramClass as having side effects.
     * This is the core functionality for program classes.
     */
    @Test
    public void testVisitProgramClass_marksProgramClassAsSideEffects() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Verify initial state
        ProgramClassOptimizationInfo info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertFalse(info.hasSideEffects(), "Class should not have side effects initially");

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertTrue(info.hasSideEffects(), "Class should be marked as having side effects");
    }

    /**
     * Tests that the static helper method correctly reports the marker's effect.
     */
    @Test
    public void testVisitProgramClass_hasSideEffectsReturnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        assertFalse(SideEffectClassMarker.hasSideEffects(programClass),
                "Initially should not have side effects");

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        assertTrue(SideEffectClassMarker.hasSideEffects(programClass),
                "After visiting, should have side effects");
    }

    /**
     * Tests that visitProgramClass can be called multiple times on the same class.
     * The flag should remain set.
     */
    @Test
    public void testVisitProgramClass_calledMultipleTimes_remainsMarked() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call multiple times
        marker.visitProgramClass(programClass);
        marker.visitProgramClass(programClass);
        marker.visitProgramClass(programClass);

        // Assert - should still be marked
        assertTrue(SideEffectClassMarker.hasSideEffects(programClass),
                "Multiple visits should maintain side effects flag");
    }

    /**
     * Tests that different classes are marked independently.
     */
    @Test
    public void testVisitProgramClass_multipleClasses_markedIndependently() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        // Act - mark only class1 and class3
        marker.visitProgramClass(class1);
        marker.visitProgramClass(class3);

        // Assert - check each class independently
        assertTrue(SideEffectClassMarker.hasSideEffects(class1),
                "Class1 should be marked");
        assertFalse(SideEffectClassMarker.hasSideEffects(class2),
                "Class2 should not be marked");
        assertTrue(SideEffectClassMarker.hasSideEffects(class3),
                "Class3 should be marked");
    }

    /**
     * Tests visiting a class after explicitly setting up ProgramClassOptimizationInfo.
     * This verifies the standard workflow where info is initialized before visiting.
     */
    @Test
    public void testVisitProgramClass_withExplicitOptimizationInfo_works() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Verify no processing info initially
        assertNull(programClass.getProcessingInfo(),
                "Class should not have processing info initially");

        // Act - set up optimization info first, then visit
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        marker.visitProgramClass(programClass);

        // Assert
        assertTrue(SideEffectClassMarker.hasSideEffects(programClass),
                "Class should be marked after proper setup and visit");
    }

    /**
     * Tests that visitProgramClass works with a class that already has optimization info set.
     */
    @Test
    public void testVisitProgramClass_withPreExistingOptimizationInfo_works() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Set some other properties
        ProgramClassOptimizationInfo info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        info.setInstantiated();
        info.setEscaping();

        // Verify initial state
        assertFalse(info.hasSideEffects(), "Class should not have side effects initially");
        assertTrue(info.isInstantiated(), "Class should be instantiated");
        assertTrue(info.isEscaping(), "Class should be escaping");

        // Act
        marker.visitProgramClass(programClass);

        // Assert
        info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertTrue(info.hasSideEffects(), "Class should have side effects after visiting");
        assertTrue(info.isInstantiated(), "Class should still be instantiated");
        assertTrue(info.isEscaping(), "Class should still be escaping");
    }

    // =========================================================================
    // hasSideEffects Static Method Tests
    // =========================================================================

    /**
     * Tests hasSideEffects returns false for a class that hasn't been visited.
     */
    @Test
    public void testHasSideEffects_unmarkedClass_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert
        assertFalse(SideEffectClassMarker.hasSideEffects(programClass),
                "Unmarked class should return false");
    }

    /**
     * Tests hasSideEffects returns true for a marked program class.
     */
    @Test
    public void testHasSideEffects_markedProgramClass_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        marker.visitProgramClass(programClass);

        // Act & Assert
        assertTrue(SideEffectClassMarker.hasSideEffects(programClass),
                "Marked program class should return true");
    }

    /**
     * Tests hasSideEffects for a library class with default ClassOptimizationInfo.
     * Library classes with ClassOptimizationInfo return true for hasSideEffects by default.
     */
    @Test
    public void testHasSideEffects_libraryClassWithClassOptimizationInfo_returnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act & Assert
        assertTrue(SideEffectClassMarker.hasSideEffects(libraryClass),
                "Library class with ClassOptimizationInfo should return true by default");
    }

    /**
     * Tests hasSideEffects with both program and library classes.
     */
    @Test
    public void testHasSideEffects_mixedClassTypes() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act - mark only program class
        marker.visitProgramClass(programClass);

        // Assert
        assertTrue(SideEffectClassMarker.hasSideEffects(programClass),
                "Marked program class should return true");
        assertTrue(SideEffectClassMarker.hasSideEffects(libraryClass),
                "Library class should return true by default");
    }

    /**
     * Tests hasSideEffects when hasNoSideEffects is set.
     * When a class is marked as having no side effects, hasSideEffects should return false
     * even if setSideEffects was called.
     */
    @Test
    public void testHasSideEffects_withNoSideEffectsSet_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        ProgramClassOptimizationInfo info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);

        // Act - mark as having side effects, then as having no side effects
        marker.visitProgramClass(programClass);
        info.setNoSideEffects();

        // Assert - hasNoSideEffects takes precedence
        assertFalse(SideEffectClassMarker.hasSideEffects(programClass),
                "When hasNoSideEffects is set, hasSideEffects should return false");
    }

    // =========================================================================
    // Integration Tests
    // =========================================================================

    /**
     * Tests the complete workflow: create marker, visit classes, check results.
     */
    @Test
    public void testIntegration_completeWorkflow() {
        // Arrange
        SideEffectClassMarker testMarker = new SideEffectClassMarker();

        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();

        // Set up optimization info
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass2);

        // Verify all start unmarked
        assertFalse(SideEffectClassMarker.hasSideEffects(programClass1));
        assertFalse(SideEffectClassMarker.hasSideEffects(programClass2));

        // Act - mark some classes
        testMarker.visitProgramClass(programClass1);

        // Assert - verify correct classes are marked
        assertTrue(SideEffectClassMarker.hasSideEffects(programClass1),
                "programClass1 should be marked");
        assertFalse(SideEffectClassMarker.hasSideEffects(programClass2),
                "programClass2 should not be marked");
    }

    /**
     * Tests that multiple markers can be used independently.
     */
    @Test
    public void testIntegration_multipleMarkersAreIndependent() {
        // Arrange
        SideEffectClassMarker marker1 = new SideEffectClassMarker();
        SideEffectClassMarker marker2 = new SideEffectClassMarker();

        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        // Act - use different markers
        marker1.visitProgramClass(class1);
        marker2.visitProgramClass(class2);

        // Assert - both classes should be marked (markers are independent but have same effect)
        assertTrue(SideEffectClassMarker.hasSideEffects(class1),
                "Class1 should be marked by marker1");
        assertTrue(SideEffectClassMarker.hasSideEffects(class2),
                "Class2 should be marked by marker2");
    }

    /**
     * Tests that the marker works correctly with the ProgramClassOptimizationInfo.
     */
    @Test
    public void testIntegration_workingWithProgramClassOptimizationInfo() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        ProgramClassOptimizationInfo info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);

        // Verify initial state
        assertFalse(info.hasSideEffects(), "Class should not have side effects initially");

        // Act - mark as having side effects
        marker.visitProgramClass(programClass);

        // Assert
        info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertTrue(info.hasSideEffects(), "Class should have side effects after marking");
    }

    /**
     * Tests that the marker can be applied to classes in sequence.
     * This verifies that the marker maintains correct state across multiple invocations.
     */
    @Test
    public void testIntegration_sequentialMarking() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        // Act - mark classes sequentially
        marker.visitProgramClass(class1);
        assertTrue(SideEffectClassMarker.hasSideEffects(class1),
                "Class1 should be marked after first visit");

        marker.visitProgramClass(class2);
        assertTrue(SideEffectClassMarker.hasSideEffects(class1),
                "Class1 should still be marked");
        assertTrue(SideEffectClassMarker.hasSideEffects(class2),
                "Class2 should be marked after second visit");

        marker.visitProgramClass(class3);
        assertTrue(SideEffectClassMarker.hasSideEffects(class1),
                "Class1 should still be marked");
        assertTrue(SideEffectClassMarker.hasSideEffects(class2),
                "Class2 should still be marked");
        assertTrue(SideEffectClassMarker.hasSideEffects(class3),
                "Class3 should be marked after third visit");
    }

    // =========================================================================
    // Edge Case Tests
    // =========================================================================

    /**
     * Tests that calling visitProgramClass with a class that already has side effects
     * doesn't cause any issues (idempotency).
     */
    @Test
    public void testEdgeCase_visitingAlreadyMarkedClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Mark the class first
        marker.visitProgramClass(programClass);
        assertTrue(SideEffectClassMarker.hasSideEffects(programClass),
                "Class should be marked initially");

        // Act - visit again
        marker.visitProgramClass(programClass);

        // Assert - should still be marked and no exceptions thrown
        assertTrue(SideEffectClassMarker.hasSideEffects(programClass),
                "Class should still be marked after second visit");
    }

    /**
     * Tests that visitAnyClass and visitProgramClass behave differently.
     * visitAnyClass should do nothing, while visitProgramClass marks the class.
     */
    @Test
    public void testEdgeCase_visitAnyClassVsVisitProgramClass() {
        // Arrange
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass2);

        // Act
        marker.visitAnyClass(programClass1);  // Should do nothing
        marker.visitProgramClass(programClass2);  // Should mark the class

        // Assert
        assertFalse(SideEffectClassMarker.hasSideEffects(programClass1),
                "Class visited via visitAnyClass should not be marked");
        assertTrue(SideEffectClassMarker.hasSideEffects(programClass2),
                "Class visited via visitProgramClass should be marked");
    }

    /**
     * Tests that calling visitProgramClass then visitAnyClass maintains the marked state.
     */
    @Test
    public void testEdgeCase_visitProgramClassThenVisitAnyClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitProgramClass(programClass);  // Mark the class
        marker.visitAnyClass(programClass);  // Should do nothing

        // Assert
        assertTrue(SideEffectClassMarker.hasSideEffects(programClass),
                "Class should remain marked after visitAnyClass");
    }
}
