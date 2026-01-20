package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link NoSideEffectMethodMarker}.
 *
 * This MemberVisitor marks all methods that it visits as not having any side
 * effects. It will make the SideEffectMethodMarker consider them as such
 * without further analysis.
 *
 * The tests cover:
 * - Constructor initialization
 * - visitAnyMember (should ignore non-method members like fields)
 * - visitProgramMethod (marks ProgramMethod as having no side effects)
 * - visitLibraryMethod (marks LibraryMethod as having no side effects)
 * - hasNoSideEffects (static utility method to check side effects status)
 */
public class NoSideEffectMethodMarkerClaudeTest {

    private NoSideEffectMethodMarker marker;

    @BeforeEach
    public void setUp() {
        marker = new NoSideEffectMethodMarker();
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
        NoSideEffectMethodMarker newMarker = new NoSideEffectMethodMarker();

        // Assert
        assertNotNull(newMarker, "Constructor should create a valid instance");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        NoSideEffectMethodMarker marker1 = new NoSideEffectMethodMarker();
        NoSideEffectMethodMarker marker2 = new NoSideEffectMethodMarker();
        NoSideEffectMethodMarker marker3 = new NoSideEffectMethodMarker();

        // Assert
        assertNotNull(marker1, "First marker should be created");
        assertNotNull(marker2, "Second marker should be created");
        assertNotNull(marker3, "Third marker should be created");
        assertNotSame(marker1, marker2, "Markers should be different instances");
        assertNotSame(marker2, marker3, "Markers should be different instances");
        assertNotSame(marker1, marker3, "Markers should be different instances");
    }

    // =========================================================================
    // visitAnyMember Tests
    // =========================================================================

    /**
     * Tests that visitAnyMember ignores fields and doesn't mark them.
     * This verifies the method's defensive behavior against non-method members.
     */
    @Test
    public void testVisitAnyMember_withProgramField_doesNotMarkField() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act
        marker.visitAnyMember(programClass, programField);

        // Assert - field should not have optimization info set
        assertNull(programField.getProcessingInfo(),
                "visitAnyMember should not set processing info on fields");
    }

    /**
     * Tests that visitAnyMember ignores library fields.
     */
    @Test
    public void testVisitAnyMember_withLibraryField_doesNotMarkField() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryField libraryField = new LibraryField();

        // Act
        marker.visitAnyMember(libraryClass, libraryField);

        // Assert - field should not have optimization info set
        assertNull(libraryField.getProcessingInfo(),
                "visitAnyMember should not set processing info on library fields");
    }

    /**
     * Tests that visitAnyMember can be called multiple times without side effects.
     */
    @Test
    public void testVisitAnyMember_calledMultipleTimes_noSideEffects() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act - call multiple times
        marker.visitAnyMember(programClass, programField);
        marker.visitAnyMember(programClass, programField);
        marker.visitAnyMember(programClass, programField);

        // Assert - still should not have processing info
        assertNull(programField.getProcessingInfo(),
                "Multiple calls to visitAnyMember should not affect fields");
    }

    /**
     * Tests that visitAnyMember with a method doesn't actually mark it.
     * The specific visit methods (visitProgramMethod, visitLibraryMethod) should be used instead.
     */
    @Test
    public void testVisitAnyMember_withProgramMethod_doesNotMark() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act
        marker.visitAnyMember(programClass, programMethod);

        // Assert - method should not be marked via visitAnyMember
        assertNull(programMethod.getProcessingInfo(),
                "visitAnyMember should not mark methods - specific visit methods should be used");
    }

    // =========================================================================
    // visitProgramMethod Tests
    // =========================================================================

    /**
     * Tests that visitProgramMethod marks a ProgramMethod as having no side effects.
     * This is the core functionality for program methods.
     */
    @Test
    public void testVisitProgramMethod_marksMethodAsNoSideEffects() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Verify initial state
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertFalse(info.hasNoSideEffects(),
                "Method should initially have side effects");
        assertTrue(info.hasSideEffects(),
                "Method should initially have side effects (hasSideEffects)");

        // Act
        marker.visitProgramMethod(programClass, programMethod);

        // Assert
        info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertTrue(info.hasNoSideEffects(),
                "Method should be marked as having no side effects");
        assertFalse(info.hasSideEffects(),
                "Method should not have side effects (hasSideEffects)");
    }

    /**
     * Tests that the static helper method correctly reports the marker's effect.
     */
    @Test
    public void testVisitProgramMethod_hasNoSideEffectsReturnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(programMethod),
                "Initially should have side effects");

        // Act
        marker.visitProgramMethod(programClass, programMethod);

        // Assert
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(programMethod),
                "After visiting, should have no side effects");
    }

    /**
     * Tests that visitProgramMethod can be called multiple times on the same method.
     * The flag should remain set.
     */
    @Test
    public void testVisitProgramMethod_calledMultipleTimes_remainsMarked() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Act - call multiple times
        marker.visitProgramMethod(programClass, programMethod);
        marker.visitProgramMethod(programClass, programMethod);
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - should still be marked
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(programMethod),
                "Multiple visits should maintain no side effects flag");
    }

    /**
     * Tests that different methods are marked independently.
     */
    @Test
    public void testVisitProgramMethod_multipleMethods_markedIndependently() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method2);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method3);

        // Act - mark only method1 and method3
        marker.visitProgramMethod(programClass, method1);
        marker.visitProgramMethod(programClass, method3);

        // Assert - check each method independently
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(method1),
                "Method1 should be marked");
        assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(method2),
                "Method2 should not be marked");
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(method3),
                "Method3 should be marked");
    }

    /**
     * Tests visiting a method after explicitly setting up MethodOptimizationInfo.
     * This verifies the standard workflow where info is initialized before visiting.
     */
    @Test
    public void testVisitProgramMethod_withExplicitOptimizationInfo_works() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Verify no processing info initially
        assertNull(programMethod.getProcessingInfo(),
                "Method should not have processing info initially");

        // Act - set up optimization info first, then visit
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);
        marker.visitProgramMethod(programClass, programMethod);

        // Assert
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(programMethod),
                "Method should be marked after proper setup and visit");
    }

    // =========================================================================
    // visitLibraryMethod Tests
    // =========================================================================

    /**
     * Tests that visitLibraryMethod marks a LibraryMethod as having no side effects.
     * This is the core functionality for library methods.
     */
    @Test
    public void testVisitLibraryMethod_marksMethodAsNoSideEffects() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);

        // Verify initial state
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);
        assertFalse(info.hasNoSideEffects(),
                "Method should initially have side effects");

        // Act
        marker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        info = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);
        assertTrue(info.hasNoSideEffects(),
                "Method should be marked as having no side effects");
    }

    /**
     * Tests that the static helper method correctly reports the marker's effect on library methods.
     */
    @Test
    public void testVisitLibraryMethod_hasNoSideEffectsReturnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);

        assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(libraryMethod),
                "Initially should have side effects");

        // Act
        marker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(libraryMethod),
                "After visiting, should have no side effects");
    }

    /**
     * Tests that visitLibraryMethod can be called multiple times on the same method.
     * The flag should remain set.
     */
    @Test
    public void testVisitLibraryMethod_calledMultipleTimes_remainsMarked() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);

        // Act - call multiple times
        marker.visitLibraryMethod(libraryClass, libraryMethod);
        marker.visitLibraryMethod(libraryClass, libraryMethod);
        marker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - should still be marked
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(libraryMethod),
                "Multiple visits should maintain no side effects flag");
    }

    /**
     * Tests that different library methods are marked independently.
     */
    @Test
    public void testVisitLibraryMethod_multipleMethods_markedIndependently() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();
        LibraryMethod method3 = new LibraryMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, method2);
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, method3);

        // Act - mark only method1 and method3
        marker.visitLibraryMethod(libraryClass, method1);
        marker.visitLibraryMethod(libraryClass, method3);

        // Assert - check each method independently
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(method1),
                "Method1 should be marked");
        assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(method2),
                "Method2 should not be marked");
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(method3),
                "Method3 should be marked");
    }

    /**
     * Tests visiting a library method after explicitly setting up MethodOptimizationInfo.
     * This verifies the standard workflow where info is initialized before visiting.
     */
    @Test
    public void testVisitLibraryMethod_withExplicitOptimizationInfo_works() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();

        // Verify no processing info initially
        assertNull(libraryMethod.getProcessingInfo(),
                "Method should not have processing info initially");

        // Act - set up optimization info first, then visit
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);
        marker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(libraryMethod),
                "Method should be marked after proper setup and visit");
    }

    // =========================================================================
    // hasNoSideEffects Static Method Tests
    // =========================================================================

    /**
     * Tests hasNoSideEffects returns false for a method that hasn't been visited.
     */
    @Test
    public void testHasNoSideEffects_unmarkedMethod_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Act & Assert
        assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(programMethod),
                "Unmarked method should return false");
    }

    /**
     * Tests hasNoSideEffects returns true for a marked program method.
     */
    @Test
    public void testHasNoSideEffects_markedProgramMethod_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);
        marker.visitProgramMethod(programClass, programMethod);

        // Act & Assert
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(programMethod),
                "Marked program method should return true");
    }

    /**
     * Tests hasNoSideEffects returns true for a marked library method.
     */
    @Test
    public void testHasNoSideEffects_markedLibraryMethod_returnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);
        marker.visitLibraryMethod(libraryClass, libraryMethod);

        // Act & Assert
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(libraryMethod),
                "Marked library method should return true");
    }

    /**
     * Tests hasNoSideEffects with both program and library methods in the same test.
     */
    @Test
    public void testHasNoSideEffects_mixedMethodTypes() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);

        // Act - mark only library method
        marker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(programMethod),
                "Unmarked program method should return false");
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(libraryMethod),
                "Marked library method should return true");
    }

    // =========================================================================
    // Integration Tests
    // =========================================================================

    /**
     * Tests the complete workflow: create marker, visit methods, check results.
     */
    @Test
    public void testIntegration_completeWorkflow() {
        // Arrange
        NoSideEffectMethodMarker testMarker = new NoSideEffectMethodMarker();

        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod1 = new ProgramMethod();
        ProgramMethod programMethod2 = new ProgramMethod();

        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod1 = new LibraryMethod();
        LibraryMethod libraryMethod2 = new LibraryMethod();

        // Set up optimization info
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod1);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod2);
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod1);
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod2);

        // Verify all start unmarked
        assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(programMethod1));
        assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(programMethod2));
        assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(libraryMethod1));
        assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(libraryMethod2));

        // Act - mark some methods
        testMarker.visitProgramMethod(programClass, programMethod1);
        testMarker.visitLibraryMethod(libraryClass, libraryMethod1);

        // Assert - verify correct methods are marked
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(programMethod1),
                "programMethod1 should be marked");
        assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(programMethod2),
                "programMethod2 should not be marked");
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(libraryMethod1),
                "libraryMethod1 should be marked");
        assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(libraryMethod2),
                "libraryMethod2 should not be marked");
    }

    /**
     * Tests that the marker affects related optimization flags.
     * Verifies that marking a method as having no side effects also sets related flags.
     */
    @Test
    public void testIntegration_affectsRelatedOptimizationFlags() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);

        // Verify initial state
        assertTrue(info.hasSideEffects(), "Method should have side effects initially");
        assertTrue(info.modifiesAnything(), "Method should modify anything initially");
        assertFalse(info.hasNoEscapingParameters(), "Method should have escaping parameters initially");
        assertFalse(info.hasNoExternalSideEffects(), "Method should have external side effects initially");
        assertFalse(info.hasNoSideEffects(), "Method should have side effects initially");

        // Act - mark as no side effects
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - related flags should be updated
        info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertFalse(info.hasSideEffects(), "Method should not have side effects after marking");
        assertFalse(info.modifiesAnything(), "Method should not modify anything after marking");
        assertTrue(info.hasNoEscapingParameters(), "Method should have no escaping parameters after marking");
        assertTrue(info.hasNoExternalSideEffects(), "Method should have no external side effects after marking");
        assertTrue(info.hasNoSideEffects(), "Method should have no side effects after marking");
    }

    /**
     * Tests that marking a method as having no side effects affects parameter modification checks.
     * With no side effects, no parameters can be modified (including 'this').
     */
    @Test
    public void testIntegration_affectsParameterModificationChecks() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);

        // Verify initial state - all parameters can be modified
        assertTrue(info.isParameterModified(0), "Parameter 0 should be modifiable initially");
        assertTrue(info.isParameterModified(1), "Parameter 1 should be modifiable initially");
        assertEquals(-1L, info.getModifiedParameters(), "All parameters should be modifiable");

        // Act - mark as no side effects
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - no parameters can be modified
        info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertFalse(info.isParameterModified(0), "Parameter 0 should not be modifiable after marking");
        assertFalse(info.isParameterModified(1), "Parameter 1 should not be modifiable after marking");
        assertEquals(0L, info.getModifiedParameters(), "No parameters should be modifiable");
    }

    /**
     * Tests that marking a method as having no side effects affects escaping parameter checks.
     */
    @Test
    public void testIntegration_affectsEscapingParameterChecks() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);

        // Verify initial state - parameters can escape
        assertTrue(info.isParameterEscaping(0), "Parameter 0 should escape initially");
        assertTrue(info.isParameterEscaping(1), "Parameter 1 should escape initially");
        assertEquals(-1L, info.getEscapingParameters(), "All parameters should be escaping");

        // Act - mark as no side effects
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - no parameters escape
        info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertFalse(info.isParameterEscaping(0), "Parameter 0 should not escape after marking");
        assertFalse(info.isParameterEscaping(1), "Parameter 1 should not escape after marking");
        assertEquals(0L, info.getEscapingParameters(), "No parameters should be escaping");
    }

    /**
     * Tests that multiple markers can be used independently.
     */
    @Test
    public void testIntegration_multipleMarkersAreIndependent() {
        // Arrange
        NoSideEffectMethodMarker marker1 = new NoSideEffectMethodMarker();
        NoSideEffectMethodMarker marker2 = new NoSideEffectMethodMarker();

        ProgramClass programClass = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method2);

        // Act - use different markers
        marker1.visitProgramMethod(programClass, method1);
        marker2.visitProgramMethod(programClass, method2);

        // Assert - both methods should be marked (markers are independent but have same effect)
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(method1),
                "Method1 should be marked by marker1");
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(method2),
                "Method2 should be marked by marker2");
    }

    /**
     * Tests edge case where visitAnyMember is called on methods indirectly.
     * This shouldn't mark them - specific visitor methods should be used.
     */
    @Test
    public void testEdgeCase_visitAnyMemberDoesNotMarkMethods() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);

        // Act - call visitAnyMember instead of specific visitor methods
        marker.visitAnyMember(programClass, programMethod);
        marker.visitAnyMember(libraryClass, libraryMethod);

        // Assert - methods should not be marked
        assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(programMethod),
                "visitAnyMember should not mark program methods");
        assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(libraryMethod),
                "visitAnyMember should not mark library methods");
    }

    /**
     * Tests the semantic distinction: no side effects means the method doesn't modify anything.
     * This is stricter than no external side effects, which allows internal modifications.
     */
    @Test
    public void testSemantics_noSideEffectsStricterThanNoExternalSideEffects() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Act - mark as no side effects
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - should have both no side effects and no external side effects
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertFalse(info.hasSideEffects(), "Method should not have side effects");
        assertTrue(info.hasNoSideEffects(), "Method should have no side effects");
        assertTrue(info.hasNoExternalSideEffects(), "Method should have no external side effects");
        assertFalse(info.modifiesAnything(), "Method should not modify anything");
    }

    /**
     * Tests that marking works correctly with program and library methods together.
     */
    @Test
    public void testIntegration_programAndLibraryMethodsTogether() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);

        // Act - mark both
        marker.visitProgramMethod(programClass, programMethod);
        marker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert - both should be marked and have similar behavior
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(programMethod));
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(libraryMethod));

        MethodOptimizationInfo programInfo = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        MethodOptimizationInfo libraryInfo = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);

        assertTrue(programInfo.hasNoSideEffects());
        assertTrue(libraryInfo.hasNoSideEffects());
        assertFalse(programInfo.hasSideEffects());
        assertFalse(libraryInfo.hasSideEffects());
        assertFalse(programInfo.modifiesAnything());
        assertFalse(libraryInfo.modifiesAnything());
    }

    /**
     * Tests that calling visitProgramMethod with a method that already has no side effects
     * doesn't cause any issues (idempotency).
     */
    @Test
    public void testEdgeCase_visitingAlreadyMarkedMethod() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Mark the method first
        marker.visitProgramMethod(programClass, programMethod);
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(programMethod),
                "Method should be marked initially");

        // Act - visit again
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - should still be marked and no exceptions thrown
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(programMethod),
                "Method should still be marked after second visit");
    }

    /**
     * Tests that the marker works correctly when applied to methods in sequence.
     * This verifies that the marker maintains correct state across multiple invocations.
     */
    @Test
    public void testIntegration_sequentialMarking() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method2);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method3);

        // Act - mark methods sequentially
        marker.visitProgramMethod(programClass, method1);
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(method1),
                "Method1 should be marked after first visit");

        marker.visitProgramMethod(programClass, method2);
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(method1),
                "Method1 should still be marked");
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(method2),
                "Method2 should be marked after second visit");

        marker.visitProgramMethod(programClass, method3);
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(method1),
                "Method1 should still be marked");
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(method2),
                "Method2 should still be marked");
        assertTrue(NoSideEffectMethodMarker.hasNoSideEffects(method3),
                "Method3 should be marked after third visit");
    }
}
