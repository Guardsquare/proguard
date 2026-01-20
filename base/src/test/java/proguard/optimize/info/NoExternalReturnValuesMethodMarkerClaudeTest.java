package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link NoExternalReturnValuesMethodMarker}.
 *
 * This MemberVisitor marks methods as not having external return values.
 * External return values are reference values that originate from the heap,
 * but not parameters or new instances. For example, Map#get has external
 * return values, but StringBuilder#toString has no external return values.
 *
 * The tests cover:
 * - Constructor initialization
 * - visitAnyMember (no-op for non-method members)
 * - visitProgramMethod (marks program methods)
 * - visitLibraryMethod (marks library methods)
 * - hasNoExternalReturnValues static utility method
 */
public class NoExternalReturnValuesMethodMarkerClaudeTest {

    private NoExternalReturnValuesMethodMarker marker;

    @BeforeEach
    public void setUp() {
        marker = new NoExternalReturnValuesMethodMarker();
    }

    // =========================================================================
    // Constructor Tests
    // =========================================================================

    /**
     * Tests that the default constructor successfully creates a marker instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        NoExternalReturnValuesMethodMarker newMarker = new NoExternalReturnValuesMethodMarker();

        // Assert
        assertNotNull(newMarker, "NoExternalReturnValuesMethodMarker instance should be created");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleInstances() {
        // Act
        NoExternalReturnValuesMethodMarker marker1 = new NoExternalReturnValuesMethodMarker();
        NoExternalReturnValuesMethodMarker marker2 = new NoExternalReturnValuesMethodMarker();
        NoExternalReturnValuesMethodMarker marker3 = new NoExternalReturnValuesMethodMarker();

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
     * Tests that visitAnyMember is a no-op and doesn't throw exceptions.
     * This method serves as the default handler for non-method members like fields.
     */
    @Test
    public void testVisitAnyMember_withProgramField_doesNotThrow() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramField field = new ProgramField();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyMember(clazz, field));
    }

    /**
     * Tests that visitAnyMember can handle null parameters gracefully.
     */
    @Test
    public void testVisitAnyMember_withNullParameters_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyMember(null, null));
    }

    /**
     * Tests that visitAnyMember can be called multiple times without issues.
     */
    @Test
    public void testVisitAnyMember_calledMultipleTimes_doesNotThrow() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramField field = new ProgramField();

        // Act & Assert
        assertDoesNotThrow(() -> {
            marker.visitAnyMember(clazz, field);
            marker.visitAnyMember(clazz, field);
            marker.visitAnyMember(clazz, field);
        });
    }

    /**
     * Tests that visitAnyMember with library fields works correctly.
     */
    @Test
    public void testVisitAnyMember_withLibraryField_doesNotThrow() {
        // Arrange
        LibraryClass clazz = new LibraryClass();
        LibraryField field = new LibraryField();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyMember(clazz, field));
    }

    // =========================================================================
    // visitProgramMethod Tests
    // =========================================================================

    /**
     * Tests that visitProgramMethod marks the method as having no external return values.
     */
    @Test
    public void testVisitProgramMethod_marksMethodAsNoExternalReturnValues() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        // Verify initial state
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Method should initially have external return values");

        // Act
        marker.visitProgramMethod(clazz, method);

        // Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Method should be marked as having no external return values");
    }

    /**
     * Tests that visitProgramMethod can be called multiple times idempotently.
     */
    @Test
    public void testVisitProgramMethod_calledMultipleTimes_remainsMarked() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        // Act - call multiple times
        marker.visitProgramMethod(clazz, method);
        marker.visitProgramMethod(clazz, method);
        marker.visitProgramMethod(clazz, method);

        // Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Method should remain marked after multiple calls");
    }

    /**
     * Tests that visitProgramMethod works with multiple different methods.
     */
    @Test
    public void testVisitProgramMethod_withMultipleMethods_marksEachIndependently() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method2);
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method3);

        // Act - mark only method1 and method3
        marker.visitProgramMethod(clazz, method1);
        marker.visitProgramMethod(clazz, method3);

        // Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method1),
                "Method1 should be marked");
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method2),
                "Method2 should not be marked");
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method3),
                "Method3 should be marked");
    }

    /**
     * Tests that visitProgramMethod works across multiple marker instances.
     */
    @Test
    public void testVisitProgramMethod_withMultipleMarkers_allMarkCorrectly() {
        // Arrange
        NoExternalReturnValuesMethodMarker marker1 = new NoExternalReturnValuesMethodMarker();
        NoExternalReturnValuesMethodMarker marker2 = new NoExternalReturnValuesMethodMarker();

        ProgramClass clazz = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method2);

        // Act - use different markers
        marker1.visitProgramMethod(clazz, method1);
        marker2.visitProgramMethod(clazz, method2);

        // Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method1),
                "Method1 should be marked by marker1");
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method2),
                "Method2 should be marked by marker2");
    }

    /**
     * Tests that visitProgramMethod doesn't throw with null clazz.
     */
    @Test
    public void testVisitProgramMethod_withNullClazz_doesNotThrow() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> marker.visitProgramMethod(null, method));
    }

    /**
     * Tests that visitProgramMethod correctly updates the MethodOptimizationInfo.
     */
    @Test
    public void testVisitProgramMethod_updatesMethodOptimizationInfo() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(method);
        assertFalse(info.hasNoExternalReturnValues(), "Should initially have external return values");

        // Act
        marker.visitProgramMethod(clazz, method);

        // Assert
        MethodOptimizationInfo updatedInfo = MethodOptimizationInfo.getMethodOptimizationInfo(method);
        assertTrue(updatedInfo.hasNoExternalReturnValues(), "Info should be updated");
    }

    /**
     * Tests visitProgramMethod with methods from different classes.
     */
    @Test
    public void testVisitProgramMethod_withDifferentClasses_marksCorrectly() {
        // Arrange
        ProgramClass clazz1 = new ProgramClass();
        ProgramClass clazz2 = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(clazz1, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz2, method2);

        // Act
        marker.visitProgramMethod(clazz1, method1);
        marker.visitProgramMethod(clazz2, method2);

        // Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method1),
                "Method from clazz1 should be marked");
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method2),
                "Method from clazz2 should be marked");
    }

    // =========================================================================
    // visitLibraryMethod Tests
    // =========================================================================

    /**
     * Tests that visitLibraryMethod marks the method as having no external return values.
     */
    @Test
    public void testVisitLibraryMethod_marksMethodAsNoExternalReturnValues() {
        // Arrange
        LibraryClass clazz = new LibraryClass();
        LibraryMethod method = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        // Verify initial state
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Method should initially have external return values");

        // Act
        marker.visitLibraryMethod(clazz, method);

        // Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Method should be marked as having no external return values");
    }

    /**
     * Tests that visitLibraryMethod can be called multiple times idempotently.
     */
    @Test
    public void testVisitLibraryMethod_calledMultipleTimes_remainsMarked() {
        // Arrange
        LibraryClass clazz = new LibraryClass();
        LibraryMethod method = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        // Act - call multiple times
        marker.visitLibraryMethod(clazz, method);
        marker.visitLibraryMethod(clazz, method);
        marker.visitLibraryMethod(clazz, method);

        // Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Method should remain marked after multiple calls");
    }

    /**
     * Tests that visitLibraryMethod works with multiple different methods.
     */
    @Test
    public void testVisitLibraryMethod_withMultipleMethods_marksEachIndependently() {
        // Arrange
        LibraryClass clazz = new LibraryClass();
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();
        LibraryMethod method3 = new LibraryMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method2);
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method3);

        // Act - mark only method1 and method3
        marker.visitLibraryMethod(clazz, method1);
        marker.visitLibraryMethod(clazz, method3);

        // Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method1),
                "Method1 should be marked");
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method2),
                "Method2 should not be marked");
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method3),
                "Method3 should be marked");
    }

    /**
     * Tests that visitLibraryMethod works across multiple marker instances.
     */
    @Test
    public void testVisitLibraryMethod_withMultipleMarkers_allMarkCorrectly() {
        // Arrange
        NoExternalReturnValuesMethodMarker marker1 = new NoExternalReturnValuesMethodMarker();
        NoExternalReturnValuesMethodMarker marker2 = new NoExternalReturnValuesMethodMarker();

        LibraryClass clazz = new LibraryClass();
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method2);

        // Act - use different markers
        marker1.visitLibraryMethod(clazz, method1);
        marker2.visitLibraryMethod(clazz, method2);

        // Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method1),
                "Method1 should be marked by marker1");
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method2),
                "Method2 should be marked by marker2");
    }

    /**
     * Tests that visitLibraryMethod doesn't throw with null clazz.
     */
    @Test
    public void testVisitLibraryMethod_withNullClazz_doesNotThrow() {
        // Arrange
        LibraryClass clazz = new LibraryClass();
        LibraryMethod method = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> marker.visitLibraryMethod(null, method));
    }

    /**
     * Tests that visitLibraryMethod correctly updates the MethodOptimizationInfo.
     */
    @Test
    public void testVisitLibraryMethod_updatesMethodOptimizationInfo() {
        // Arrange
        LibraryClass clazz = new LibraryClass();
        LibraryMethod method = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(method);
        assertFalse(info.hasNoExternalReturnValues(), "Should initially have external return values");

        // Act
        marker.visitLibraryMethod(clazz, method);

        // Assert
        MethodOptimizationInfo updatedInfo = MethodOptimizationInfo.getMethodOptimizationInfo(method);
        assertTrue(updatedInfo.hasNoExternalReturnValues(), "Info should be updated");
    }

    /**
     * Tests visitLibraryMethod with methods from different classes.
     */
    @Test
    public void testVisitLibraryMethod_withDifferentClasses_marksCorrectly() {
        // Arrange
        LibraryClass clazz1 = new LibraryClass();
        LibraryClass clazz2 = new LibraryClass();
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(clazz1, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz2, method2);

        // Act
        marker.visitLibraryMethod(clazz1, method1);
        marker.visitLibraryMethod(clazz2, method2);

        // Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method1),
                "Method from clazz1 should be marked");
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method2),
                "Method from clazz2 should be marked");
    }

    // =========================================================================
    // hasNoExternalReturnValues Tests
    // =========================================================================

    /**
     * Tests that hasNoExternalReturnValues returns false for unmarked ProgramMethod.
     */
    @Test
    public void testHasNoExternalReturnValues_withUnmarkedProgramMethod_returnsFalse() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        // Act & Assert
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Unmarked method should return false");
    }

    /**
     * Tests that hasNoExternalReturnValues returns true for marked ProgramMethod.
     */
    @Test
    public void testHasNoExternalReturnValues_withMarkedProgramMethod_returnsTrue() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);
        marker.visitProgramMethod(clazz, method);

        // Act & Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Marked method should return true");
    }

    /**
     * Tests that hasNoExternalReturnValues returns false for unmarked LibraryMethod.
     */
    @Test
    public void testHasNoExternalReturnValues_withUnmarkedLibraryMethod_returnsFalse() {
        // Arrange
        LibraryClass clazz = new LibraryClass();
        LibraryMethod method = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        // Act & Assert
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Unmarked method should return false");
    }

    /**
     * Tests that hasNoExternalReturnValues returns true for marked LibraryMethod.
     */
    @Test
    public void testHasNoExternalReturnValues_withMarkedLibraryMethod_returnsTrue() {
        // Arrange
        LibraryClass clazz = new LibraryClass();
        LibraryMethod method = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);
        marker.visitLibraryMethod(clazz, method);

        // Act & Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Marked method should return true");
    }

    /**
     * Tests that hasNoExternalReturnValues is consistent across multiple calls.
     */
    @Test
    public void testHasNoExternalReturnValues_consistentAcrossMultipleCalls() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        // Act - check before marking
        boolean before1 = NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method);
        boolean before2 = NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method);

        // Mark the method
        marker.visitProgramMethod(clazz, method);

        // Check after marking
        boolean after1 = NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method);
        boolean after2 = NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method);

        // Assert
        assertFalse(before1, "Should be false before marking");
        assertFalse(before2, "Should consistently be false before marking");
        assertTrue(after1, "Should be true after marking");
        assertTrue(after2, "Should consistently be true after marking");
    }

    /**
     * Tests hasNoExternalReturnValues with multiple methods.
     */
    @Test
    public void testHasNoExternalReturnValues_withMultipleMethods_returnsCorrectly() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method2);
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method3);

        // Mark only some methods
        marker.visitProgramMethod(clazz, method1);
        marker.visitProgramMethod(clazz, method3);

        // Act & Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method1),
                "Method1 should return true");
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method2),
                "Method2 should return false");
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method3),
                "Method3 should return true");
    }

    // =========================================================================
    // Integration Tests
    // =========================================================================

    /**
     * Tests integration between visitProgramMethod and hasNoExternalReturnValues.
     */
    @Test
    public void testIntegration_programMethodMarkingAndChecking() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        // Act & Assert - before marking
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Should initially return false");

        // Mark the method
        marker.visitProgramMethod(clazz, method);

        // Assert - after marking
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Should return true after marking");

        // Verify the MethodOptimizationInfo is properly updated
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(method);
        assertTrue(info.hasNoExternalReturnValues(), "Info should have no external return values");
        assertFalse(info.returnsExternalValues(), "Info should not return external values");
    }

    /**
     * Tests integration between visitLibraryMethod and hasNoExternalReturnValues.
     */
    @Test
    public void testIntegration_libraryMethodMarkingAndChecking() {
        // Arrange
        LibraryClass clazz = new LibraryClass();
        LibraryMethod method = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        // Act & Assert - before marking
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Should initially return false");

        // Mark the method
        marker.visitLibraryMethod(clazz, method);

        // Assert - after marking
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Should return true after marking");

        // Verify the MethodOptimizationInfo is properly updated
        MethodOptimizationInfo info = MethodOptimizationInfo.getMethodOptimizationInfo(method);
        assertTrue(info.hasNoExternalReturnValues(), "Info should have no external return values");
        assertFalse(info.returnsExternalValues(), "Info should not return external values");
    }

    /**
     * Tests that marker works correctly with both ProgramMethod and LibraryMethod.
     */
    @Test
    public void testIntegration_mixedMethodTypes() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        ProgramMethod programMethod = new ProgramMethod();
        LibraryMethod libraryMethod = new LibraryMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);

        // Act
        marker.visitProgramMethod(programClass, programMethod);
        marker.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(programMethod),
                "Program method should be marked");
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(libraryMethod),
                "Library method should be marked");
    }

    /**
     * Tests that the marker can process multiple methods in sequence.
     */
    @Test
    public void testIntegration_sequentialProcessing() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method2);
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method3);

        // Act - process all methods sequentially
        marker.visitProgramMethod(clazz, method1);
        marker.visitProgramMethod(clazz, method2);
        marker.visitProgramMethod(clazz, method3);

        // Assert - all should be marked
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method1),
                "Method1 should be marked");
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method2),
                "Method2 should be marked");
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method3),
                "Method3 should be marked");
    }

    /**
     * Tests that visitAnyMember doesn't interfere with method marking.
     */
    @Test
    public void testIntegration_visitAnyMemberDoesNotInterfere() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        ProgramField field = new ProgramField();

        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        // Act - call visitAnyMember before and after visitProgramMethod
        marker.visitAnyMember(clazz, field);
        marker.visitProgramMethod(clazz, method);
        marker.visitAnyMember(clazz, field);

        // Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                "Method should still be marked despite visitAnyMember calls");
    }

    /**
     * Tests that the marker can be reused for multiple processing sessions.
     */
    @Test
    public void testIntegration_markerReusability() {
        // Arrange
        ProgramClass clazz1 = new ProgramClass();
        ProgramClass clazz2 = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(clazz1, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz2, method2);

        // Act - use the same marker for different methods
        marker.visitProgramMethod(clazz1, method1);
        marker.visitProgramMethod(clazz2, method2);

        // Assert
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method1),
                "First method should be marked");
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method2),
                "Second method should be marked");
    }

    /**
     * Tests the complete workflow: create marker, visit methods, check results.
     */
    @Test
    public void testIntegration_completeWorkflow() {
        // Arrange - create multiple methods
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        ProgramMethod programMethod1 = new ProgramMethod();
        ProgramMethod programMethod2 = new ProgramMethod();
        LibraryMethod libraryMethod1 = new LibraryMethod();
        LibraryMethod libraryMethod2 = new LibraryMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod1);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod2);
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod1);
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod2);

        // Verify all start unmarked
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(programMethod1));
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(programMethod2));
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(libraryMethod1));
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(libraryMethod2));

        // Act - mark selected methods
        marker.visitProgramMethod(programClass, programMethod1);
        marker.visitLibraryMethod(libraryClass, libraryMethod2);

        // Assert - check final state
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(programMethod1),
                "ProgramMethod1 should be marked");
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(programMethod2),
                "ProgramMethod2 should not be marked");
        assertFalse(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(libraryMethod1),
                "LibraryMethod1 should not be marked");
        assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(libraryMethod2),
                "LibraryMethod2 should be marked");
    }

    /**
     * Tests that marking is persistent across multiple hasNoExternalReturnValues checks.
     */
    @Test
    public void testIntegration_markingIsPersistent() {
        // Arrange
        ProgramClass clazz = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

        // Act - mark the method
        marker.visitProgramMethod(clazz, method);

        // Assert - check multiple times
        for (int i = 0; i < 10; i++) {
            assertTrue(NoExternalReturnValuesMethodMarker.hasNoExternalReturnValues(method),
                    "Should remain marked on check " + i);
        }
    }
}
