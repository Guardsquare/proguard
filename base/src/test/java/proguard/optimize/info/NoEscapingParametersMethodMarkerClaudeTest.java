package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link NoEscapingParametersMethodMarker}.
 *
 * This MemberVisitor marks all methods that it visits as not having any
 * escaping parameters (including 'this'). It will make the ParameterEscapeMarker
 * consider them as such without further analysis.
 *
 * The tests cover:
 * - Constructor initialization
 * - visitAnyMember (should ignore non-method members like fields)
 * - visitProgramMethod (marks ProgramMethod as having no escaping parameters)
 * - visitLibraryMethod (marks LibraryMethod as having no escaping parameters)
 * - hasNoParameterEscaping (static utility method to check parameter escaping status)
 */
public class NoEscapingParametersMethodMarkerClaudeTest {

    private NoEscapingParametersMethodMarker marker;

    @BeforeEach
    public void setUp() {
        marker = new NoEscapingParametersMethodMarker();
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
        NoEscapingParametersMethodMarker newMarker = new NoEscapingParametersMethodMarker();

        // Assert
        assertNotNull(newMarker, "Constructor should create a valid instance");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        NoEscapingParametersMethodMarker marker1 = new NoEscapingParametersMethodMarker();
        NoEscapingParametersMethodMarker marker2 = new NoEscapingParametersMethodMarker();
        NoEscapingParametersMethodMarker marker3 = new NoEscapingParametersMethodMarker();

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
     * Tests that visitProgramMethod marks a ProgramMethod as having no escaping parameters.
     * This is the core functionality for program methods.
     */
    @Test
    public void testVisitProgramMethod_marksMethodAsNoEscapingParameters() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Verify initial state
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertFalse(info.hasNoEscapingParameters(),
                "Method should initially have escaping parameters");

        // Act
        marker.visitProgramMethod(programClass, programMethod);

        // Assert
        info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertTrue(info.hasNoEscapingParameters(),
                "Method should be marked as having no escaping parameters");
    }

    /**
     * Tests that the static helper method correctly reports the marker's effect.
     */
    @Test
    public void testVisitProgramMethod_hasNoParameterEscapingReturnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(programMethod),
                "Initially should have parameter escaping");

        // Act
        marker.visitProgramMethod(programClass, programMethod);

        // Assert
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(programMethod),
                "After visiting, should have no parameter escaping");
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
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(programMethod),
                "Multiple visits should maintain no escaping parameters flag");
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
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(method1),
                "Method1 should be marked");
        assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(method2),
                "Method2 should not be marked");
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(method3),
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
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(programMethod),
                "Method should be marked after proper setup and visit");
    }

    // =========================================================================
    // visitLibraryMethod Tests
    // =========================================================================

    /**
     * Tests that visitLibraryMethod marks a LibraryMethod as having no escaping parameters.
     * This is the core functionality for library methods.
     */
    @Test
    public void testVisitLibraryMethod_marksMethodAsNoEscapingParameters() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);

        // Verify initial state
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);
        assertFalse(info.hasNoEscapingParameters(),
                "Method should initially have escaping parameters");

        // Act
        marker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        info = MethodOptimizationInfo.getMethodOptimizationInfo(libraryMethod);
        assertTrue(info.hasNoEscapingParameters(),
                "Method should be marked as having no escaping parameters");
    }

    /**
     * Tests that the static helper method correctly reports the marker's effect on library methods.
     */
    @Test
    public void testVisitLibraryMethod_hasNoParameterEscapingReturnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);

        assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(libraryMethod),
                "Initially should have parameter escaping");

        // Act
        marker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(libraryMethod),
                "After visiting, should have no parameter escaping");
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
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(libraryMethod),
                "Multiple visits should maintain no escaping parameters flag");
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
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(method1),
                "Method1 should be marked");
        assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(method2),
                "Method2 should not be marked");
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(method3),
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
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(libraryMethod),
                "Method should be marked after proper setup and visit");
    }

    // =========================================================================
    // hasNoParameterEscaping Static Method Tests
    // =========================================================================

    /**
     * Tests hasNoParameterEscaping returns false for a method that hasn't been visited.
     */
    @Test
    public void testHasNoParameterEscaping_unmarkedMethod_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Act & Assert
        assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(programMethod),
                "Unmarked method should return false");
    }

    /**
     * Tests hasNoParameterEscaping returns true for a marked program method.
     */
    @Test
    public void testHasNoParameterEscaping_markedProgramMethod_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);
        marker.visitProgramMethod(programClass, programMethod);

        // Act & Assert
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(programMethod),
                "Marked program method should return true");
    }

    /**
     * Tests hasNoParameterEscaping returns true for a marked library method.
     */
    @Test
    public void testHasNoParameterEscaping_markedLibraryMethod_returnsTrue() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);
        marker.visitLibraryMethod(libraryClass, libraryMethod);

        // Act & Assert
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(libraryMethod),
                "Marked library method should return true");
    }

    /**
     * Tests hasNoParameterEscaping with both program and library methods in the same test.
     */
    @Test
    public void testHasNoParameterEscaping_mixedMethodTypes() {
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
        assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(programMethod),
                "Unmarked program method should return false");
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(libraryMethod),
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
        NoEscapingParametersMethodMarker testMarker = new NoEscapingParametersMethodMarker();

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
        assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(programMethod1));
        assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(programMethod2));
        assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(libraryMethod1));
        assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(libraryMethod2));

        // Act - mark some methods
        testMarker.visitProgramMethod(programClass, programMethod1);
        testMarker.visitLibraryMethod(libraryClass, libraryMethod1);

        // Assert - verify correct methods are marked
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(programMethod1),
                "programMethod1 should be marked");
        assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(programMethod2),
                "programMethod2 should not be marked");
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(libraryMethod1),
                "libraryMethod1 should be marked");
        assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(libraryMethod2),
                "libraryMethod2 should not be marked");
    }

    /**
     * Tests that the marker works correctly with the MethodOptimizationInfo relationships.
     * Verifies that marking affects isParameterEscaping checks.
     */
    @Test
    public void testIntegration_affectsParameterEscapingChecks() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);

        // Verify initial state - parameters are escaping
        assertTrue(info.isParameterEscaping(0), "Parameter 0 should be escaping initially");
        assertTrue(info.isParameterEscaping(1), "Parameter 1 should be escaping initially");
        assertEquals(-1L, info.getEscapingParameters(), "All parameters should be escaping");

        // Act - mark as no escaping parameters
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - parameters should no longer be escaping
        info = MethodOptimizationInfo.getMethodOptimizationInfo(programMethod);
        assertFalse(info.isParameterEscaping(0), "Parameter 0 should not be escaping after marking");
        assertFalse(info.isParameterEscaping(1), "Parameter 1 should not be escaping after marking");
        assertEquals(0L, info.getEscapingParameters(), "No parameters should be escaping");
    }

    /**
     * Tests that multiple markers can be used independently.
     */
    @Test
    public void testIntegration_multipleMarkersAreIndependent() {
        // Arrange
        NoEscapingParametersMethodMarker marker1 = new NoEscapingParametersMethodMarker();
        NoEscapingParametersMethodMarker marker2 = new NoEscapingParametersMethodMarker();

        ProgramClass programClass = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method2);

        // Act - use different markers
        marker1.visitProgramMethod(programClass, method1);
        marker2.visitProgramMethod(programClass, method2);

        // Assert - both methods should be marked (markers are independent but have same effect)
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(method1),
                "Method1 should be marked by marker1");
        assertTrue(NoEscapingParametersMethodMarker.hasNoParameterEscaping(method2),
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
        assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(programMethod),
                "visitAnyMember should not mark program methods");
        assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(libraryMethod),
                "visitAnyMember should not mark library methods");
    }
}
