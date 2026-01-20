package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SimpleEnumMarker}.
 *
 * This ClassVisitor marks all program classes that it visits with a given
 * flag for simple enums.
 *
 * The tests cover:
 * - Constructor initialization with true/false flags
 * - visitAnyClass (does nothing for non-program classes)
 * - visitProgramClass (marks program classes with simple enum flag)
 * - isSimpleEnum (static utility method to check simple enum status)
 */
public class SimpleEnumMarkerClaudeTest {

    private SimpleEnumMarker markerTrue;
    private SimpleEnumMarker markerFalse;

    @BeforeEach
    public void setUp() {
        markerTrue = new SimpleEnumMarker(true);
        markerFalse = new SimpleEnumMarker(false);
    }

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests that the constructor successfully creates an instance with true flag.
     */
    @Test
    public void testConstructor_withTrueFlag_createsInstance() {
        // Act
        SimpleEnumMarker marker = new SimpleEnumMarker(true);

        // Assert
        assertNotNull(marker, "Constructor should create a valid instance with true flag");
    }

    /**
     * Tests that the constructor successfully creates an instance with false flag.
     */
    @Test
    public void testConstructor_withFalseFlag_createsInstance() {
        // Act
        SimpleEnumMarker marker = new SimpleEnumMarker(false);

        // Assert
        assertNotNull(marker, "Constructor should create a valid instance with false flag");
    }

    /**
     * Tests that multiple instances can be created independently with different flags.
     */
    @Test
    public void testConstructor_createsMultipleIndependentInstances() {
        // Act
        SimpleEnumMarker marker1 = new SimpleEnumMarker(true);
        SimpleEnumMarker marker2 = new SimpleEnumMarker(false);
        SimpleEnumMarker marker3 = new SimpleEnumMarker(true);

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

        // Verify initial state - should not be simple enum initially
        ProgramClassOptimizationInfo info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertFalse(info.isSimpleEnum(), "Class should not be simple enum initially");

        // Act
        markerTrue.visitAnyClass(programClass);

        // Assert - should still not be simple enum (visitAnyClass does nothing)
        info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertFalse(info.isSimpleEnum(), "Class should still not be simple enum after visitAnyClass");
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
        assertFalse(info.isSimpleEnum(), "Library class should not be simple enum initially");

        // Act
        markerTrue.visitAnyClass(libraryClass);

        // Assert - should remain unchanged
        info = ClassOptimizationInfo.getClassOptimizationInfo(libraryClass);
        assertFalse(info.isSimpleEnum(), "Library class should still not be simple enum after visitAnyClass");
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
        markerTrue.visitAnyClass(programClass);
        markerTrue.visitAnyClass(programClass);
        markerTrue.visitAnyClass(programClass);

        // Assert - should still have no effect
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass),
                "Multiple calls to visitAnyClass should have no effect");
    }

    /**
     * Tests that visitAnyClass with markerFalse also does nothing.
     */
    @Test
    public void testVisitAnyClass_withFalseMarker_doesNothing() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // First mark as simple enum
        markerTrue.visitProgramClass(programClass);
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass), "Should be marked as simple enum");

        // Act - visitAnyClass should not change it
        markerFalse.visitAnyClass(programClass);

        // Assert - should still be simple enum (visitAnyClass does nothing)
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass),
                "visitAnyClass should not change the simple enum state");
    }

    // =========================================================================
    // visitProgramClass Tests - with true flag
    // =========================================================================

    /**
     * Tests that visitProgramClass marks a ProgramClass as simple enum when marker is true.
     * This is the core functionality for program classes.
     */
    @Test
    public void testVisitProgramClass_withTrueFlag_marksProgramClassAsSimpleEnum() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Verify initial state
        ProgramClassOptimizationInfo info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertFalse(info.isSimpleEnum(), "Class should not be simple enum initially");

        // Act
        markerTrue.visitProgramClass(programClass);

        // Assert
        info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertTrue(info.isSimpleEnum(), "Class should be marked as simple enum");
    }

    /**
     * Tests that the static helper method correctly reports the marker's effect.
     */
    @Test
    public void testVisitProgramClass_withTrueFlag_isSimpleEnumReturnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass),
                "Initially should not be simple enum");

        // Act
        markerTrue.visitProgramClass(programClass);

        // Assert
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass),
                "After visiting, should be simple enum");
    }

    /**
     * Tests that visitProgramClass can be called multiple times on the same class.
     * The flag should remain set to true.
     */
    @Test
    public void testVisitProgramClass_withTrueFlag_calledMultipleTimes_remainsMarked() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call multiple times
        markerTrue.visitProgramClass(programClass);
        markerTrue.visitProgramClass(programClass);
        markerTrue.visitProgramClass(programClass);

        // Assert - should still be marked
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass),
                "Multiple visits should maintain simple enum flag");
    }

    // =========================================================================
    // visitProgramClass Tests - with false flag
    // =========================================================================

    /**
     * Tests that visitProgramClass marks a ProgramClass as NOT simple enum when marker is false.
     */
    @Test
    public void testVisitProgramClass_withFalseFlag_marksProgramClassAsNotSimpleEnum() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Verify initial state
        ProgramClassOptimizationInfo info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertFalse(info.isSimpleEnum(), "Class should not be simple enum initially");

        // Act
        markerFalse.visitProgramClass(programClass);

        // Assert
        info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertFalse(info.isSimpleEnum(), "Class should be marked as NOT simple enum");
    }

    /**
     * Tests that a false marker can unmark a class previously marked as simple enum.
     */
    @Test
    public void testVisitProgramClass_withFalseFlag_canUnmarkSimpleEnum() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // First mark as simple enum
        markerTrue.visitProgramClass(programClass);
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass),
                "Class should be marked as simple enum after true marker");

        // Act - unmark with false marker
        markerFalse.visitProgramClass(programClass);

        // Assert
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass),
                "Class should be unmarked after false marker");
    }

    /**
     * Tests that visitProgramClass with false flag can be called multiple times.
     */
    @Test
    public void testVisitProgramClass_withFalseFlag_calledMultipleTimes_remainsUnmarked() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // First mark as simple enum
        markerTrue.visitProgramClass(programClass);
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass), "Should be marked initially");

        // Act - call false marker multiple times
        markerFalse.visitProgramClass(programClass);
        markerFalse.visitProgramClass(programClass);
        markerFalse.visitProgramClass(programClass);

        // Assert - should remain unmarked
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass),
                "Multiple visits with false flag should maintain unmarked state");
    }

    // =========================================================================
    // visitProgramClass Tests - switching between flags
    // =========================================================================

    /**
     * Tests switching a class between simple enum and not simple enum multiple times.
     */
    @Test
    public void testVisitProgramClass_switchingFlags_updatesCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert - switch multiple times
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass), "Initially false");

        markerTrue.visitProgramClass(programClass);
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass), "After true marker");

        markerFalse.visitProgramClass(programClass);
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass), "After false marker");

        markerTrue.visitProgramClass(programClass);
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass), "After true marker again");

        markerFalse.visitProgramClass(programClass);
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass), "After false marker again");
    }

    /**
     * Tests that different classes are marked independently with different markers.
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

        // Act - mark class1 as simple enum, leave class2 unmarked, mark class3 as simple enum
        markerTrue.visitProgramClass(class1);
        markerTrue.visitProgramClass(class3);

        // Assert - check each class independently
        assertTrue(SimpleEnumMarker.isSimpleEnum(class1),
                "Class1 should be marked as simple enum");
        assertFalse(SimpleEnumMarker.isSimpleEnum(class2),
                "Class2 should not be marked");
        assertTrue(SimpleEnumMarker.isSimpleEnum(class3),
                "Class3 should be marked as simple enum");
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
        markerTrue.visitProgramClass(programClass);

        // Assert
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass),
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
        assertFalse(info.isSimpleEnum(), "Class should not be simple enum initially");
        assertTrue(info.isInstantiated(), "Class should be instantiated");
        assertTrue(info.isEscaping(), "Class should be escaping");

        // Act
        markerTrue.visitProgramClass(programClass);

        // Assert
        info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertTrue(info.isSimpleEnum(), "Class should be simple enum after visiting");
        assertTrue(info.isInstantiated(), "Class should still be instantiated");
        assertTrue(info.isEscaping(), "Class should still be escaping");
    }

    // =========================================================================
    // isSimpleEnum Static Method Tests
    // =========================================================================

    /**
     * Tests isSimpleEnum returns false for a class that hasn't been visited.
     */
    @Test
    public void testIsSimpleEnum_unmarkedClass_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass),
                "Unmarked class should return false");
    }

    /**
     * Tests isSimpleEnum returns true for a marked program class.
     */
    @Test
    public void testIsSimpleEnum_markedProgramClass_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        markerTrue.visitProgramClass(programClass);

        // Act & Assert
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass),
                "Marked program class should return true");
    }

    /**
     * Tests isSimpleEnum returns false for a class marked with false flag.
     */
    @Test
    public void testIsSimpleEnum_classMarkedWithFalseFlag_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        markerFalse.visitProgramClass(programClass);

        // Act & Assert
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass),
                "Class marked with false flag should return false");
    }

    /**
     * Tests isSimpleEnum for a library class with default ClassOptimizationInfo.
     * Library classes with ClassOptimizationInfo return false for isSimpleEnum by default.
     */
    @Test
    public void testIsSimpleEnum_libraryClassWithClassOptimizationInfo_returnsFalse() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act & Assert
        assertFalse(SimpleEnumMarker.isSimpleEnum(libraryClass),
                "Library class with ClassOptimizationInfo should return false by default");
    }

    /**
     * Tests isSimpleEnum with both program and library classes.
     */
    @Test
    public void testIsSimpleEnum_mixedClassTypes() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act - mark only program class
        markerTrue.visitProgramClass(programClass);

        // Assert
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass),
                "Marked program class should return true");
        assertFalse(SimpleEnumMarker.isSimpleEnum(libraryClass),
                "Library class should return false by default");
    }

    /**
     * Tests isSimpleEnum after marking and unmarking.
     */
    @Test
    public void testIsSimpleEnum_afterMarkingAndUnmarking_correctlyReflectsState() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Initially false
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass),
                "Initially should be false");

        // Mark as simple enum
        markerTrue.visitProgramClass(programClass);
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass),
                "After marking should be true");

        // Unmark
        markerFalse.visitProgramClass(programClass);
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass),
                "After unmarking should be false");
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
        SimpleEnumMarker testMarkerTrue = new SimpleEnumMarker(true);
        SimpleEnumMarker testMarkerFalse = new SimpleEnumMarker(false);

        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();
        ProgramClass programClass3 = new ProgramClass();

        // Set up optimization info
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass3);

        // Verify all start unmarked
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass1));
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass2));
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass3));

        // Act - mark some classes
        testMarkerTrue.visitProgramClass(programClass1);
        testMarkerFalse.visitProgramClass(programClass2);

        // Assert - verify correct classes are marked
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass1),
                "programClass1 should be marked as simple enum");
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass2),
                "programClass2 should be marked as NOT simple enum");
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass3),
                "programClass3 should not be marked");
    }

    /**
     * Tests that multiple markers can be used independently.
     */
    @Test
    public void testIntegration_multipleMarkersAreIndependent() {
        // Arrange
        SimpleEnumMarker marker1 = new SimpleEnumMarker(true);
        SimpleEnumMarker marker2 = new SimpleEnumMarker(false);

        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);

        // Act - use different markers
        marker1.visitProgramClass(class1);
        marker2.visitProgramClass(class2);

        // Assert
        assertTrue(SimpleEnumMarker.isSimpleEnum(class1),
                "Class1 should be marked by marker1");
        assertFalse(SimpleEnumMarker.isSimpleEnum(class2),
                "Class2 should be unmarked by marker2");
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
        assertFalse(info.isSimpleEnum(), "Class should not be simple enum initially");

        // Act - mark as simple enum
        markerTrue.visitProgramClass(programClass);

        // Assert
        info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertTrue(info.isSimpleEnum(), "Class should be simple enum after marking");
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
        markerTrue.visitProgramClass(class1);
        assertTrue(SimpleEnumMarker.isSimpleEnum(class1),
                "Class1 should be marked after first visit");

        markerTrue.visitProgramClass(class2);
        assertTrue(SimpleEnumMarker.isSimpleEnum(class1),
                "Class1 should still be marked");
        assertTrue(SimpleEnumMarker.isSimpleEnum(class2),
                "Class2 should be marked after second visit");

        markerTrue.visitProgramClass(class3);
        assertTrue(SimpleEnumMarker.isSimpleEnum(class1),
                "Class1 should still be marked");
        assertTrue(SimpleEnumMarker.isSimpleEnum(class2),
                "Class2 should still be marked");
        assertTrue(SimpleEnumMarker.isSimpleEnum(class3),
                "Class3 should be marked after third visit");
    }

    // =========================================================================
    // Edge Case Tests
    // =========================================================================

    /**
     * Tests that calling visitProgramClass with a class that is already marked
     * doesn't cause any issues (idempotency).
     */
    @Test
    public void testEdgeCase_visitingAlreadyMarkedClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Mark the class first
        markerTrue.visitProgramClass(programClass);
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass),
                "Class should be marked initially");

        // Act - visit again
        markerTrue.visitProgramClass(programClass);

        // Assert - should still be marked and no exceptions thrown
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass),
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
        markerTrue.visitAnyClass(programClass1);  // Should do nothing
        markerTrue.visitProgramClass(programClass2);  // Should mark the class

        // Assert
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass1),
                "Class visited via visitAnyClass should not be marked");
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass2),
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
        markerTrue.visitProgramClass(programClass);  // Mark the class
        markerTrue.visitAnyClass(programClass);  // Should do nothing

        // Assert
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass),
                "Class should remain marked after visitAnyClass");
    }

    /**
     * Tests rapid switching between true and false markers.
     */
    @Test
    public void testEdgeCase_rapidSwitching() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert - rapid switching
        for (int i = 0; i < 10; i++) {
            markerTrue.visitProgramClass(programClass);
            assertTrue(SimpleEnumMarker.isSimpleEnum(programClass), "Should be true at iteration " + i);

            markerFalse.visitProgramClass(programClass);
            assertFalse(SimpleEnumMarker.isSimpleEnum(programClass), "Should be false at iteration " + i);
        }
    }

    /**
     * Tests marking multiple classes with different markers in various orders.
     */
    @Test
    public void testEdgeCase_complexScenarioWithMultipleClassesAndMarkers() {
        // Arrange
        ProgramClass[] classes = new ProgramClass[5];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
        }

        // Act - complex marking scenario
        markerTrue.visitProgramClass(classes[0]);
        markerTrue.visitProgramClass(classes[1]);
        markerFalse.visitProgramClass(classes[2]);
        // classes[3] not visited
        markerTrue.visitProgramClass(classes[4]);
        markerFalse.visitProgramClass(classes[1]);  // Override class 1

        // Assert
        assertTrue(SimpleEnumMarker.isSimpleEnum(classes[0]), "Class 0 should be simple enum");
        assertFalse(SimpleEnumMarker.isSimpleEnum(classes[1]), "Class 1 should NOT be simple enum (overridden)");
        assertFalse(SimpleEnumMarker.isSimpleEnum(classes[2]), "Class 2 should NOT be simple enum");
        assertFalse(SimpleEnumMarker.isSimpleEnum(classes[3]), "Class 3 should NOT be simple enum (not visited)");
        assertTrue(SimpleEnumMarker.isSimpleEnum(classes[4]), "Class 4 should be simple enum");
    }

    /**
     * Tests that optimization info properties are preserved when marking/unmarking simple enum.
     */
    @Test
    public void testEdgeCase_preservesOtherOptimizationInfoProperties() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);

        // Set multiple properties
        info.setInstantiated();
        info.setInstanceofed();
        info.setEscaping();
        info.setCaught();
        info.setDotClassed();

        // Act - mark and unmark simple enum
        markerTrue.visitProgramClass(programClass);
        markerFalse.visitProgramClass(programClass);
        markerTrue.visitProgramClass(programClass);

        // Assert - all other properties should be preserved
        info = ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass);
        assertTrue(info.isInstantiated(), "isInstantiated should be preserved");
        assertTrue(info.isInstanceofed(), "isInstanceofed should be preserved");
        assertTrue(info.isEscaping(), "isEscaping should be preserved");
        assertTrue(info.isCaught(), "isCaught should be preserved");
        assertTrue(info.isDotClassed(), "isDotClassed should be preserved");
        assertTrue(info.isSimpleEnum(), "isSimpleEnum should be true after final marking");
    }
}
