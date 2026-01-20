package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link NoExternalSideEffectMethodMarker}.
 *
 * This MemberVisitor marks all methods that it visits as not having any
 * external side effects. It will make the SideEffectMethodMarker consider them
 * as such without further analysis.
 *
 * External side effects include changing static fields and generally changing objects
 * on the heap, but not changing fields of the instance on which the method is called
 * (or the class, in case of static methods), or any instances that can only be reached
 * through the instance. For example, StringBuilder#append has side effects, but no
 * external side effects.
 *
 * The tests cover:
 * - Constructor initialization
 * - visitAnyMember (should ignore non-method members like fields)
 * - visitProgramMethod (marks ProgramMethod as having no external side effects)
 * - visitLibraryMethod (marks LibraryMethod as having no external side effects)
 * - hasNoExternalSideEffects (static utility method to check external side effects status)
 */
public class NoExternalSideEffectMethodMarkerClaudeTest {

    private NoExternalSideEffectMethodMarker marker;

    @BeforeEach
    public void setUp() {
        marker = new NoExternalSideEffectMethodMarker();
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
        NoExternalSideEffectMethodMarker newMarker = new NoExternalSideEffectMethodMarker();

        // Assert
        assertNotNull(newMarker, "Constructor should create a valid instance");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        NoExternalSideEffectMethodMarker marker1 = new NoExternalSideEffectMethodMarker();
        NoExternalSideEffectMethodMarker marker2 = new NoExternalSideEffectMethodMarker();
        NoExternalSideEffectMethodMarker marker3 = new NoExternalSideEffectMethodMarker();

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
     * Tests that visitProgramMethod marks a ProgramMethod as having no external side effects.
     * This is the core functionality for program methods.
     */
    @Test
    public void testVisitProgramMethod_marksMethodAsNoExternalSideEffects() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Verify initial state
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertFalse(info.hasNoExternalSideEffects(),
                "Method should initially have external side effects");

        // Act
        marker.visitProgramMethod(programClass, programMethod);

        // Assert
        info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertTrue(info.hasNoExternalSideEffects(),
                "Method should be marked as having no external side effects");
    }

    /**
     * Tests that the static helper method correctly reports the marker's effect.
     */
    @Test
    public void testVisitProgramMethod_hasNoExternalSideEffectsReturnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(programMethod),
                "Initially should have external side effects");

        // Act
        marker.visitProgramMethod(programClass, programMethod);

        // Assert
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(programMethod),
                "After visiting, should have no external side effects");
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
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(programMethod),
                "Multiple visits should maintain no external side effects flag");
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
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(method1),
                "Method1 should be marked");
        assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(method2),
                "Method2 should not be marked");
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(method3),
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
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(programMethod),
                "Method should be marked after proper setup and visit");
    }

    // =========================================================================
    // visitLibraryMethod Tests
    // =========================================================================

    /**
     * Tests that visitLibraryMethod marks a LibraryMethod as having no external side effects.
     * This is the core functionality for library methods.
     */
    @Test
    public void testVisitLibraryMethod_marksMethodAsNoExternalSideEffects() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);

        // Verify initial state
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);
        assertFalse(info.hasNoExternalSideEffects(),
                "Method should initially have external side effects");

        // Act
        marker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        info = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);
        assertTrue(info.hasNoExternalSideEffects(),
                "Method should be marked as having no external side effects");
    }

    /**
     * Tests that the static helper method correctly reports the marker's effect on library methods.
     */
    @Test
    public void testVisitLibraryMethod_hasNoExternalSideEffectsReturnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);

        assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(libraryMethod),
                "Initially should have external side effects");

        // Act
        marker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(libraryMethod),
                "After visiting, should have no external side effects");
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
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(libraryMethod),
                "Multiple visits should maintain no external side effects flag");
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
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(method1),
                "Method1 should be marked");
        assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(method2),
                "Method2 should not be marked");
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(method3),
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
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(libraryMethod),
                "Method should be marked after proper setup and visit");
    }

    // =========================================================================
    // hasNoExternalSideEffects Static Method Tests
    // =========================================================================

    /**
     * Tests hasNoExternalSideEffects returns false for a method that hasn't been visited.
     */
    @Test
    public void testHasNoExternalSideEffects_unmarkedMethod_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Act & Assert
        assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(programMethod),
                "Unmarked method should return false");
    }

    /**
     * Tests hasNoExternalSideEffects returns true for a marked program method.
     */
    @Test
    public void testHasNoExternalSideEffects_markedProgramMethod_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);
        marker.visitProgramMethod(programClass, programMethod);

        // Act & Assert
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(programMethod),
                "Marked program method should return true");
    }

    /**
     * Tests hasNoExternalSideEffects returns true for a marked library method.
     */
    @Test
    public void testHasNoExternalSideEffects_markedLibraryMethod_returnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);
        marker.visitLibraryMethod(libraryClass, libraryMethod);

        // Act & Assert
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(libraryMethod),
                "Marked library method should return true");
    }

    /**
     * Tests hasNoExternalSideEffects with both program and library methods in the same test.
     */
    @Test
    public void testHasNoExternalSideEffects_mixedMethodTypes() {
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
        assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(programMethod),
                "Unmarked program method should return false");
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(libraryMethod),
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
        NoExternalSideEffectMethodMarker testMarker = new NoExternalSideEffectMethodMarker();

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
        assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(programMethod1));
        assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(programMethod2));
        assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(libraryMethod1));
        assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(libraryMethod2));

        // Act - mark some methods
        testMarker.visitProgramMethod(programClass, programMethod1);
        testMarker.visitLibraryMethod(libraryClass, libraryMethod1);

        // Assert - verify correct methods are marked
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(programMethod1),
                "programMethod1 should be marked");
        assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(programMethod2),
                "programMethod2 should not be marked");
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(libraryMethod1),
                "libraryMethod1 should be marked");
        assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(libraryMethod2),
                "libraryMethod2 should not be marked");
    }

    /**
     * Tests that the marker works correctly with the MethodOptimizationInfo relationships.
     * Verifies that marking affects modifiesAnything checks and also sets hasNoEscapingParameters.
     */
    @Test
    public void testIntegration_affectsRelatedOptimizationFlags() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);

        // Verify initial state
        assertTrue(info.modifiesAnything(), "Method should modify anything initially");
        assertFalse(info.hasNoEscapingParameters(), "Method should have escaping parameters initially");
        assertFalse(info.hasNoExternalSideEffects(), "Method should have external side effects initially");

        // Act - mark as no external side effects
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - related flags should be updated
        info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertFalse(info.modifiesAnything(), "Method should not modify anything after marking");
        assertTrue(info.hasNoEscapingParameters(), "Method should have no escaping parameters after marking");
        assertTrue(info.hasNoExternalSideEffects(), "Method should have no external side effects after marking");
    }

    /**
     * Tests that marking a method as having no external side effects also affects parameter modification checks.
     * With no external side effects, only parameter 0 ('this') can be modified.
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

        // Act - mark as no external side effects
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - only 'this' (parameter 0) can be modified
        info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertTrue(info.isParameterModified(0), "Parameter 0 ('this') can still be modified");
        assertFalse(info.isParameterModified(1), "Parameter 1 should not be modifiable after marking");
        assertEquals(1L, info.getModifiedParameters(), "Only parameter 0 should be modifiable");
    }

    /**
     * Tests that multiple markers can be used independently.
     */
    @Test
    public void testIntegration_multipleMarkersAreIndependent() {
        // Arrange
        NoExternalSideEffectMethodMarker marker1 = new NoExternalSideEffectMethodMarker();
        NoExternalSideEffectMethodMarker marker2 = new NoExternalSideEffectMethodMarker();

        ProgramClass programClass = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method2);

        // Act - use different markers
        marker1.visitProgramMethod(programClass, method1);
        marker2.visitProgramMethod(programClass, method2);

        // Assert - both methods should be marked (markers are independent but have same effect)
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(method1),
                "Method1 should be marked by marker1");
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(method2),
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
        assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(programMethod),
                "visitAnyMember should not mark program methods");
        assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(libraryMethod),
                "visitAnyMember should not mark library methods");
    }

    /**
     * Tests the semantic difference between no external side effects and no side effects.
     * No external side effects means the method can still modify 'this' (internal state).
     */
    @Test
    public void testSemantics_noExternalSideEffectsAllowsInternalModification() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Act - mark as no external side effects
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - should still have side effects (can modify 'this'), but no external side effects
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertTrue(info.hasSideEffects(), "Method should still have side effects (can modify 'this')");
        assertTrue(info.hasNoExternalSideEffects(), "Method should have no external side effects");
        assertFalse(info.hasNoSideEffects(), "Method should not have 'no side effects' (can still modify 'this')");
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
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(programMethod));
        assertTrue(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(libraryMethod));

        MethodOptimizationInfo programInfo = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        MethodOptimizationInfo libraryInfo = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);

        assertTrue(programInfo.hasNoExternalSideEffects());
        assertTrue(libraryInfo.hasNoExternalSideEffects());
        assertFalse(programInfo.modifiesAnything());
        assertFalse(libraryInfo.modifiesAnything());
    }
}
