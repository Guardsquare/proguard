package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReachableCodeMarker#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method in ReachableCodeMarker is an empty implementation (no-op).
 * It's part of the AttributeVisitor interface but doesn't perform any actions because:
 * - The actual reachability marking is done in visitCodeAttribute()
 * - visitAnyAttribute() serves as a default no-op for when the visitor is called on non-specific attribute types
 *
 * These tests verify that:
 * 1. The method doesn't throw exceptions with various inputs
 * 2. The method doesn't modify the attribute or class being visited
 * 3. The method can handle different types of attributes
 * 4. The method is properly integrated into the visitor pattern
 * 5. The method maintains the reachable code marker state unchanged
 */
public class ReachableCodeMarkerClaude_visitAnyAttributeTest {

    private ReachableCodeMarker marker;
    private Clazz clazz;

    @BeforeEach
    public void setUp() {
        marker = new ReachableCodeMarker();
        clazz = new ProgramClass();
    }

    // ========================================
    // Basic Functionality Tests
    // ========================================

    /**
     * Tests that visitAnyAttribute does not throw exceptions with valid mock objects.
     * Verifies the empty implementation executes without errors.
     */
    @Test
    public void testVisitAnyAttribute_withValidMocks_doesNotThrow() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, attribute),
            "visitAnyAttribute should not throw exception with valid parameters");
    }

    /**
     * Tests that visitAnyAttribute handles a real CodeAttribute.
     * Verifies the method works with actual attribute instances.
     */
    @Test
    public void testVisitAnyAttribute_withCodeAttribute_doesNotThrow() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, codeAttribute),
            "visitAnyAttribute should handle CodeAttribute");
    }

    /**
     * Tests that visitAnyAttribute handles a SourceFileAttribute.
     * Verifies the method works with different attribute types.
     */
    @Test
    public void testVisitAnyAttribute_withSourceFileAttribute_doesNotThrow() {
        // Arrange
        SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, sourceFileAttribute),
            "visitAnyAttribute should handle SourceFileAttribute");
    }

    /**
     * Tests that visitAnyAttribute handles a LineNumberTableAttribute.
     * Verifies the method works with line number related attributes.
     */
    @Test
    public void testVisitAnyAttribute_withLineNumberTableAttribute_doesNotThrow() {
        // Arrange
        LineNumberTableAttribute lineNumberTableAttribute = new LineNumberTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, lineNumberTableAttribute),
            "visitAnyAttribute should handle LineNumberTableAttribute");
    }

    /**
     * Tests that visitAnyAttribute handles a LocalVariableTableAttribute.
     * Verifies the method works with local variable related attributes.
     */
    @Test
    public void testVisitAnyAttribute_withLocalVariableTableAttribute_doesNotThrow() {
        // Arrange
        LocalVariableTableAttribute localVariableTableAttribute = new LocalVariableTableAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, localVariableTableAttribute),
            "visitAnyAttribute should handle LocalVariableTableAttribute");
    }

    /**
     * Tests that visitAnyAttribute handles an ExceptionsAttribute.
     * Verifies the method works with exceptions related attributes.
     */
    @Test
    public void testVisitAnyAttribute_withExceptionsAttribute_doesNotThrow() {
        // Arrange
        ExceptionsAttribute exceptionsAttribute = new ExceptionsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, exceptionsAttribute),
            "visitAnyAttribute should handle ExceptionsAttribute");
    }

    // ========================================
    // Null Parameter Handling Tests
    // ========================================

    /**
     * Tests that visitAnyAttribute handles null attribute parameter.
     * Verifies the no-op implementation doesn't dereference null.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, null),
            "visitAnyAttribute should handle null attribute");
    }

    /**
     * Tests that visitAnyAttribute handles null clazz parameter.
     * Verifies the no-op implementation doesn't dereference null.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrow() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(null, attribute),
            "visitAnyAttribute should handle null clazz");
    }

    /**
     * Tests that visitAnyAttribute handles both parameters being null.
     * Verifies the no-op implementation is robust to all null inputs.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(null, null),
            "visitAnyAttribute should handle both null parameters");
    }

    // ========================================
    // Different Clazz Types Tests
    // ========================================

    /**
     * Tests that visitAnyAttribute works with ProgramClass.
     * Verifies the method handles concrete class implementations.
     */
    @Test
    public void testVisitAnyAttribute_withProgramClass_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(programClass, attribute),
            "visitAnyAttribute should handle ProgramClass");
    }

    /**
     * Tests that visitAnyAttribute works with LibraryClass.
     * Verifies the method handles different class types.
     */
    @Test
    public void testVisitAnyAttribute_withLibraryClass_doesNotThrow() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyAttribute(libraryClass, attribute),
            "visitAnyAttribute should handle LibraryClass");
    }

    // ========================================
    // State Preservation Tests
    // ========================================

    /**
     * Tests that visitAnyAttribute doesn't affect the reachability state.
     * Verifies calling visitAnyAttribute doesn't mark any offsets as reachable.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectReachabilityState() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // Act
        marker.visitAnyAttribute(clazz, attribute);

        // Assert - check that no offsets have been marked as reachable
        assertFalse(marker.isReachable(0), "Offset 0 should remain unreachable");
        assertFalse(marker.isReachable(10), "Offset 10 should remain unreachable");
        assertFalse(marker.isReachable(100), "Offset 100 should remain unreachable");
    }

    /**
     * Tests that visitAnyAttribute doesn't affect reachability range state.
     * Verifies calling visitAnyAttribute doesn't mark any ranges as reachable.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectRangeReachabilityState() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // Act
        marker.visitAnyAttribute(clazz, attribute);

        // Assert - check that no offset ranges have been marked as reachable
        assertFalse(marker.isReachable(0, 10), "Offset range 0-10 should remain unreachable");
        assertFalse(marker.isReachable(50, 100), "Offset range 50-100 should remain unreachable");
    }

    /**
     * Tests that multiple calls to visitAnyAttribute don't accumulate state changes.
     * Verifies the no-op behavior is consistent across multiple invocations.
     */
    @Test
    public void testVisitAnyAttribute_multipleCallsNoStateChange() {
        // Arrange
        Attribute attribute1 = mock(Attribute.class);
        Attribute attribute2 = mock(Attribute.class);

        // Act
        marker.visitAnyAttribute(clazz, attribute1);
        marker.visitAnyAttribute(clazz, attribute2);
        marker.visitAnyAttribute(clazz, attribute1);

        // Assert - state should remain unchanged
        assertFalse(marker.isReachable(0), "Offset 0 should remain unreachable after multiple calls");
        assertFalse(marker.isReachable(0, 100), "Offset range should remain unreachable after multiple calls");
    }

    // ========================================
    // Mock Interaction Tests
    // ========================================

    /**
     * Tests that visitAnyAttribute doesn't interact with mock attribute.
     * Verifies the no-op implementation doesn't call any methods on the attribute.
     */
    @Test
    public void testVisitAnyAttribute_noInteractionWithAttribute() {
        // Arrange
        Attribute mockAttribute = mock(Attribute.class);

        // Act
        marker.visitAnyAttribute(clazz, mockAttribute);

        // Assert - no methods should be called on the mock
        verifyNoInteractions(mockAttribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with mock clazz.
     * Verifies the no-op implementation doesn't call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_noInteractionWithClazz() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        Attribute attribute = mock(Attribute.class);

        // Act
        marker.visitAnyAttribute(mockClazz, attribute);

        // Assert - no methods should be called on the mock
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(attribute);
    }

    // ========================================
    // Multiple Instances Tests
    // ========================================

    /**
     * Tests that multiple marker instances behave independently.
     * Verifies that calling visitAnyAttribute on one marker doesn't affect another.
     */
    @Test
    public void testVisitAnyAttribute_multipleInstancesIndependent() {
        // Arrange
        ReachableCodeMarker marker1 = new ReachableCodeMarker();
        ReachableCodeMarker marker2 = new ReachableCodeMarker();
        Attribute attribute = mock(Attribute.class);

        // Act
        marker1.visitAnyAttribute(clazz, attribute);
        marker2.visitAnyAttribute(clazz, attribute);

        // Assert - both markers should remain independent with no reachable offsets
        assertFalse(marker1.isReachable(0), "First marker: offset 0 should remain unreachable");
        assertFalse(marker2.isReachable(0), "Second marker: offset 0 should remain unreachable");
    }

    // ========================================
    // Edge Cases Tests
    // ========================================

    /**
     * Tests visitAnyAttribute with various attribute types in sequence.
     * Verifies consistent no-op behavior across different attribute types.
     */
    @Test
    public void testVisitAnyAttribute_variousAttributeTypes_consistentBehavior() {
        // Arrange
        CodeAttribute codeAttribute = new CodeAttribute();
        SourceFileAttribute sourceFileAttribute = new SourceFileAttribute();
        LineNumberTableAttribute lineNumberTableAttribute = new LineNumberTableAttribute();

        // Act & Assert - all should be no-ops
        assertDoesNotThrow(() -> {
            marker.visitAnyAttribute(clazz, codeAttribute);
            marker.visitAnyAttribute(clazz, sourceFileAttribute);
            marker.visitAnyAttribute(clazz, lineNumberTableAttribute);
        }, "visitAnyAttribute should handle various attribute types");

        // Verify state remains unchanged
        assertFalse(marker.isReachable(0), "State should remain unchanged after visiting various attributes");
    }

    /**
     * Tests visitAnyAttribute can be called repeatedly without issues.
     * Verifies the marker remains functional after many no-op calls.
     */
    @Test
    public void testVisitAnyAttribute_repeatedCalls_remainsStable() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // Act - call many times
        for (int i = 0; i < 100; i++) {
            marker.visitAnyAttribute(clazz, attribute);
        }

        // Assert - marker should still be functional and state unchanged
        assertFalse(marker.isReachable(0), "State should remain unchanged after many calls");
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't affect subsequent visitCodeAttribute behavior.
     * Verifies the no-op doesn't interfere with the marker's main functionality.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectSubsequentCodeAttributeVisit() {
        // Arrange
        Attribute nonCodeAttribute = new SourceFileAttribute();

        // Act - call visitAnyAttribute first
        marker.visitAnyAttribute(clazz, nonCodeAttribute);

        // Assert - marker should still be ready to process code attributes
        // This is verified by checking the state remains clean (no false positives)
        assertFalse(marker.isReachable(0), "Marker should be ready for actual code processing");
        assertFalse(marker.isReachable(0, 100), "Marker should not have any reachable ranges");
    }

    /**
     * Tests visitAnyAttribute with the same attribute instance multiple times.
     * Verifies the no-op behavior is consistent even with repeated identical calls.
     */
    @Test
    public void testVisitAnyAttribute_sameAttributeMultipleTimes_consistentNoOp() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // Act
        marker.visitAnyAttribute(clazz, attribute);
        marker.visitAnyAttribute(clazz, attribute);
        marker.visitAnyAttribute(clazz, attribute);

        // Assert - no interactions even after multiple calls with same instance
        verifyNoInteractions(attribute);
        assertFalse(marker.isReachable(0), "State should remain unchanged");
    }

    /**
     * Tests visitAnyAttribute with different clazz instances.
     * Verifies the no-op behavior works consistently across different clazz instances.
     */
    @Test
    public void testVisitAnyAttribute_differentClazzInstances_consistentBehavior() {
        // Arrange
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        Attribute attribute = mock(Attribute.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marker.visitAnyAttribute(programClass1, attribute);
            marker.visitAnyAttribute(programClass2, attribute);
            marker.visitAnyAttribute(libraryClass, attribute);
        }, "visitAnyAttribute should handle different clazz instances");

        verifyNoInteractions(attribute);
    }
}
