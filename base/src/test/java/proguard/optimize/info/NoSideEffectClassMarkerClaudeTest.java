package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link NoSideEffectClassMarker}.
 *
 * This ClassVisitor marks all classes that it visits as not having any side
 * effects. It will make the SideEffectClassMarker consider them as such
 * without further analysis.
 *
 * The tests cover:
 * - Constructor initialization
 * - visitAnyClass (marks classes as having no side effects)
 * - hasNoSideEffects (static utility method to check side effects status)
 */
public class NoSideEffectClassMarkerClaudeTest {

    private NoSideEffectClassMarker marker;

    @BeforeEach
    public void setUp() {
        marker = new NoSideEffectClassMarker();
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
        NoSideEffectClassMarker newMarker = new NoSideEffectClassMarker();

        // Assert
        assertNotNull(newMarker, "Constructor should create a valid instance");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        NoSideEffectClassMarker marker1 = new NoSideEffectClassMarker();
        NoSideEffectClassMarker marker2 = new NoSideEffectClassMarker();
        NoSideEffectClassMarker marker3 = new NoSideEffectClassMarker();

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
     * Tests that visitAnyClass marks a ProgramClass as having no side effects.
     * This is the core functionality for program classes.
     */
    @Test
    public void testVisitAnyClass_marksProgramClassAsNoSideEffects() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Verify initial state
        ClassOptimizationInfo info = ClassOptimizationInfo.getClassOptimizationInfo(programClass);
        assertFalse(info.hasNoSideEffects(),
                "Class should initially have side effects");
        assertTrue(info.hasSideEffects(),
                "Class should initially have side effects (hasSideEffects)");

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        info = ClassOptimizationInfo.getClassOptimizationInfo(programClass);
        assertTrue(info.hasNoSideEffects(),
                "Class should be marked as having no side effects");
        assertFalse(info.hasSideEffects(),
                "Class should not have side effects (hasSideEffects)");
    }

    /**
     * Tests that visitAnyClass marks a LibraryClass as having no side effects.
     * This is the core functionality for library classes.
     */
    @Test
    public void testVisitAnyClass_marksLibraryClassAsNoSideEffects() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Verify initial state
        ClassOptimizationInfo info = ClassOptimizationInfo.getClassOptimizationInfo(libraryClass);
        assertFalse(info.hasNoSideEffects(),
                "Class should initially have side effects");

        // Act
        marker.visitAnyClass(libraryClass);

        // Assert
        info = ClassOptimizationInfo.getClassOptimizationInfo(libraryClass);
        assertTrue(info.hasNoSideEffects(),
                "Class should be marked as having no side effects");
    }

    /**
     * Tests that the static helper method correctly reports the marker's effect.
     */
    @Test
    public void testVisitAnyClass_hasNoSideEffectsReturnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        assertFalse(NoSideEffectClassMarker.hasNoSideEffects(programClass),
                "Initially should have side effects");

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(programClass),
                "After visiting, should have no side effects");
    }

    /**
     * Tests that visitAnyClass can be called multiple times on the same class.
     * The flag should remain set.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_remainsMarked() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act - call multiple times
        marker.visitAnyClass(programClass);
        marker.visitAnyClass(programClass);
        marker.visitAnyClass(programClass);

        // Assert - should still be marked
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(programClass),
                "Multiple visits should maintain no side effects flag");
    }

    /**
     * Tests that different classes are marked independently.
     */
    @Test
    public void testVisitAnyClass_multipleClasses_markedIndependently() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        ClassOptimizationInfo.setClassOptimizationInfo(class1);
        ClassOptimizationInfo.setClassOptimizationInfo(class2);
        ClassOptimizationInfo.setClassOptimizationInfo(class3);

        // Act - mark only class1 and class3
        marker.visitAnyClass(class1);
        marker.visitAnyClass(class3);

        // Assert - check each class independently
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(class1),
                "Class1 should be marked");
        assertFalse(NoSideEffectClassMarker.hasNoSideEffects(class2),
                "Class2 should not be marked");
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(class3),
                "Class3 should be marked");
    }

    /**
     * Tests visiting a class after explicitly setting up ClassOptimizationInfo.
     * This verifies the standard workflow where info is initialized before visiting.
     */
    @Test
    public void testVisitAnyClass_withExplicitOptimizationInfo_works() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Verify no processing info initially
        assertNull(programClass.getProcessingInfo(),
                "Class should not have processing info initially");

        // Act - set up optimization info first, then visit
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(programClass),
                "Class should be marked after proper setup and visit");
    }

    // =========================================================================
    // hasNoSideEffects Static Method Tests
    // =========================================================================

    /**
     * Tests hasNoSideEffects returns false for a class that hasn't been visited.
     */
    @Test
    public void testHasNoSideEffects_unmarkedClass_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act & Assert
        assertFalse(NoSideEffectClassMarker.hasNoSideEffects(programClass),
                "Unmarked class should return false");
    }

    /**
     * Tests hasNoSideEffects returns true for a marked program class.
     */
    @Test
    public void testHasNoSideEffects_markedProgramClass_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);
        marker.visitAnyClass(programClass);

        // Act & Assert
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(programClass),
                "Marked program class should return true");
    }

    /**
     * Tests hasNoSideEffects returns true for a marked library class.
     */
    @Test
    public void testHasNoSideEffects_markedLibraryClass_returnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);
        marker.visitAnyClass(libraryClass);

        // Act & Assert
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(libraryClass),
                "Marked library class should return true");
    }

    /**
     * Tests hasNoSideEffects with both program and library classes in the same test.
     */
    @Test
    public void testHasNoSideEffects_mixedClassTypes() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        ClassOptimizationInfo.setClassOptimizationInfo(programClass);
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act - mark only library class
        marker.visitAnyClass(libraryClass);

        // Assert
        assertFalse(NoSideEffectClassMarker.hasNoSideEffects(programClass),
                "Unmarked program class should return false");
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(libraryClass),
                "Marked library class should return true");
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
        NoSideEffectClassMarker testMarker = new NoSideEffectClassMarker();

        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();

        LibraryClass libraryClass1 = new LibraryClass();
        LibraryClass libraryClass2 = new LibraryClass();

        // Set up optimization info
        ClassOptimizationInfo.setClassOptimizationInfo(programClass1);
        ClassOptimizationInfo.setClassOptimizationInfo(programClass2);
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass1);
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass2);

        // Verify all start unmarked
        assertFalse(NoSideEffectClassMarker.hasNoSideEffects(programClass1));
        assertFalse(NoSideEffectClassMarker.hasNoSideEffects(programClass2));
        assertFalse(NoSideEffectClassMarker.hasNoSideEffects(libraryClass1));
        assertFalse(NoSideEffectClassMarker.hasNoSideEffects(libraryClass2));

        // Act - mark some classes
        testMarker.visitAnyClass(programClass1);
        testMarker.visitAnyClass(libraryClass1);

        // Assert - verify correct classes are marked
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(programClass1),
                "programClass1 should be marked");
        assertFalse(NoSideEffectClassMarker.hasNoSideEffects(programClass2),
                "programClass2 should not be marked");
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(libraryClass1),
                "libraryClass1 should be marked");
        assertFalse(NoSideEffectClassMarker.hasNoSideEffects(libraryClass2),
                "libraryClass2 should not be marked");
    }

    /**
     * Tests that the marker affects related optimization flags.
     * Verifies that marking a class as having no side effects affects isDotClassed and isInstanceofed.
     */
    @Test
    public void testIntegration_affectsRelatedOptimizationFlags() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        ClassOptimizationInfo info = ClassOptimizationInfo.getClassOptimizationInfo(programClass);

        // Verify initial state
        assertTrue(info.hasSideEffects(), "Class should have side effects initially");
        assertTrue(info.isDotClassed(), "Class should be dotClassed initially");
        assertTrue(info.isInstanceofed(), "Class should be instanceofed initially");
        assertFalse(info.hasNoSideEffects(), "Class should not have no side effects initially");

        // Act - mark as no side effects
        marker.visitAnyClass(programClass);

        // Assert - related flags should be updated
        info = ClassOptimizationInfo.getClassOptimizationInfo(programClass);
        assertFalse(info.hasSideEffects(), "Class should not have side effects after marking");
        assertFalse(info.isDotClassed(), "Class should not be dotClassed after marking");
        assertFalse(info.isInstanceofed(), "Class should not be instanceofed after marking");
        assertTrue(info.hasNoSideEffects(), "Class should have no side effects after marking");
    }

    /**
     * Tests that multiple markers can be used independently.
     */
    @Test
    public void testIntegration_multipleMarkersAreIndependent() {
        // Arrange
        NoSideEffectClassMarker marker1 = new NoSideEffectClassMarker();
        NoSideEffectClassMarker marker2 = new NoSideEffectClassMarker();

        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        ClassOptimizationInfo.setClassOptimizationInfo(class1);
        ClassOptimizationInfo.setClassOptimizationInfo(class2);

        // Act - use different markers
        marker1.visitAnyClass(class1);
        marker2.visitAnyClass(class2);

        // Assert - both classes should be marked (markers are independent but have same effect)
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(class1),
                "Class1 should be marked by marker1");
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(class2),
                "Class2 should be marked by marker2");
    }

    /**
     * Tests that marking works correctly with program and library classes together.
     */
    @Test
    public void testIntegration_programAndLibraryClassesTogether() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        ClassOptimizationInfo.setClassOptimizationInfo(programClass);
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act - mark both
        marker.visitAnyClass(programClass);
        marker.visitAnyClass(libraryClass);

        // Assert - both should be marked and have similar behavior
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(programClass));
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(libraryClass));

        ClassOptimizationInfo programInfo = ClassOptimizationInfo.getClassOptimizationInfo(programClass);
        ClassOptimizationInfo libraryInfo = ClassOptimizationInfo.getClassOptimizationInfo(libraryClass);

        assertTrue(programInfo.hasNoSideEffects());
        assertTrue(libraryInfo.hasNoSideEffects());
        assertFalse(programInfo.hasSideEffects());
        assertFalse(libraryInfo.hasSideEffects());
    }

    /**
     * Tests that the marker works correctly with the ClassOptimizationInfo isKept method.
     */
    @Test
    public void testIntegration_doesNotAffectIsKeptFlag() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        ClassOptimizationInfo info = ClassOptimizationInfo.getClassOptimizationInfo(programClass);

        // Verify initial state
        assertTrue(info.isKept(), "Class should be kept initially");

        // Act - mark as no side effects
        marker.visitAnyClass(programClass);

        // Assert - isKept should not be affected
        info = ClassOptimizationInfo.getClassOptimizationInfo(programClass);
        assertTrue(info.isKept(), "Class should still be kept after marking");
    }

    /**
     * Tests that the marker works correctly with the ClassOptimizationInfo isInstantiated method.
     */
    @Test
    public void testIntegration_doesNotAffectIsInstantiatedFlag() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        ClassOptimizationInfo info = ClassOptimizationInfo.getClassOptimizationInfo(programClass);

        // Verify initial state
        assertTrue(info.isInstantiated(), "Class should be instantiated initially");

        // Act - mark as no side effects
        marker.visitAnyClass(programClass);

        // Assert - isInstantiated should not be affected
        info = ClassOptimizationInfo.getClassOptimizationInfo(programClass);
        assertTrue(info.isInstantiated(), "Class should still be instantiated after marking");
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

        ClassOptimizationInfo.setClassOptimizationInfo(class1);
        ClassOptimizationInfo.setClassOptimizationInfo(class2);
        ClassOptimizationInfo.setClassOptimizationInfo(class3);

        // Act - mark classes sequentially
        marker.visitAnyClass(class1);
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(class1),
                "Class1 should be marked after first visit");

        marker.visitAnyClass(class2);
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(class1),
                "Class1 should still be marked");
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(class2),
                "Class2 should be marked after second visit");

        marker.visitAnyClass(class3);
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(class1),
                "Class1 should still be marked");
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(class2),
                "Class2 should still be marked");
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(class3),
                "Class3 should be marked after third visit");
    }

    // =========================================================================
    // Edge Case Tests
    // =========================================================================

    /**
     * Tests that calling visitAnyClass with a class that already has no side effects
     * doesn't cause any issues (idempotency).
     */
    @Test
    public void testEdgeCase_visitingAlreadyMarkedClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Mark the class first
        marker.visitAnyClass(programClass);
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(programClass),
                "Class should be marked initially");

        // Act - visit again
        marker.visitAnyClass(programClass);

        // Assert - should still be marked and no exceptions thrown
        assertTrue(NoSideEffectClassMarker.hasNoSideEffects(programClass),
                "Class should still be marked after second visit");
    }

}
