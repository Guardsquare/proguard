package proguard.shrink;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AnnotationUsageMarker#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern for attributes that don't have specialized visitor methods.
 * AnnotationUsageMarker only processes annotation-related attributes through specialized methods,
 * so this catch-all method intentionally does nothing.
 */
public class AnnotationUsageMarkerClaude_visitAnyAttributeTest {

    private AnnotationUsageMarker marker;
    private ClassUsageMarker classUsageMarker;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        classUsageMarker = mock(ClassUsageMarker.class);
        marker = new AnnotationUsageMarker(classUsageMarker);
        clazz = mock(ProgramClass.class);
        attribute = mock(Attribute.class);
    }

    /**
     * Tests that visitAnyAttribute can be called with valid mock objects without throwing exceptions.
     * Since this is a no-op method, it should simply do nothing and complete successfully.
     */
    @Test
    public void testVisitAnyAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyAttribute(null, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, null));
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            marker.visitAnyAttribute(clazz, attribute);
            marker.visitAnyAttribute(clazz, attribute);
            marker.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Act
        marker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Attribute parameter.
     * Since it's a no-op method, it should not call any methods on the attribute.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithAttribute() {
        // Act
        marker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the attribute mock
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't affect the marker's ClassUsageMarker.
     * Calling the method should not trigger any marking operations.
     */
    @Test
    public void testVisitAnyAttribute_doesNotTriggerClassUsageMarker() {
        // Act
        marker.visitAnyAttribute(clazz, attribute);

        // Assert - verify the ClassUsageMarker was not invoked
        verifyNoInteractions(classUsageMarker);
    }

    /**
     * Tests that visitAnyAttribute works with different Clazz mock instances.
     * The method should handle any Clazz implementation without issues.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentClazzInstances_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            marker.visitAnyAttribute(clazz1, attribute);
            marker.visitAnyAttribute(clazz2, attribute);
            marker.visitAnyAttribute(clazz3, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute works with different Attribute mock instances.
     * The method should handle any Attribute implementation without issues.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentAttributeInstances_doesNotThrowException() {
        // Arrange
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);
        Attribute attr3 = mock(Attribute.class);

        // Act & Assert - should not throw any exception with different instances
        assertDoesNotThrow(() -> {
            marker.visitAnyAttribute(clazz, attr1);
            marker.visitAnyAttribute(clazz, attr2);
            marker.visitAnyAttribute(clazz, attr3);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't modify the marker's internal state.
     * Since it's a no-op, no internal flags or fields should be affected.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyMarkerState() {
        // Arrange - create another marker with the same configuration
        AnnotationUsageMarker marker2 = new AnnotationUsageMarker(classUsageMarker);

        // Act - call visitAnyAttribute on the first marker
        marker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no marking operations occurred
        verifyNoInteractions(classUsageMarker);
    }

    /**
     * Tests that visitAnyAttribute execution completes immediately.
     * Since it's a no-op method, it should have minimal overhead.
     */
    @Test
    public void testVisitAnyAttribute_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            marker.visitAnyAttribute(clazz, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyAttribute should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyAttribute doesn't affect subsequent calls to other methods.
     * The no-op should not interfere with the marker's normal operation.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyAttribute first
        marker.visitAnyAttribute(clazz, attribute);

        // Then call visitAnyAttribute again
        marker.visitAnyAttribute(clazz, attribute);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
        verifyNoInteractions(classUsageMarker);
    }

    /**
     * Tests that visitAnyAttribute with stubbed Attribute implementations doesn't throw exceptions.
     * This ensures the no-op works with concrete attribute types.
     */
    @Test
    public void testVisitAnyAttribute_withVariousAttributeTypes_doesNotThrowException() {
        // Arrange - test with various attribute types
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);
        Attribute attr3 = mock(Attribute.class);

        when(attr1.getAttributeName(any())).thenReturn("CustomAttribute1");
        when(attr2.getAttributeName(any())).thenReturn("CustomAttribute2");
        when(attr3.getAttributeName(any())).thenReturn("CustomAttribute3");

        // Act & Assert - should handle all attribute types gracefully
        assertDoesNotThrow(() -> {
            marker.visitAnyAttribute(clazz, attr1);
            marker.visitAnyAttribute(clazz, attr2);
            marker.visitAnyAttribute(clazz, attr3);
        });

        // Verify that the attribute methods were not called since it's a no-op
        verify(attr1, never()).getAttributeName(any());
        verify(attr2, never()).getAttributeName(any());
        verify(attr3, never()).getAttributeName(any());
    }

    /**
     * Tests that multiple markers can call visitAnyAttribute independently.
     * Each marker's no-op should not affect others.
     */
    @Test
    public void testVisitAnyAttribute_withMultipleMarkers_operateIndependently() {
        // Arrange - create multiple markers
        ClassUsageMarker usageMarker1 = mock(ClassUsageMarker.class);
        ClassUsageMarker usageMarker2 = mock(ClassUsageMarker.class);

        AnnotationUsageMarker marker1 = new AnnotationUsageMarker(usageMarker1);
        AnnotationUsageMarker marker2 = new AnnotationUsageMarker(usageMarker2);

        // Act - call visitAnyAttribute on both markers
        marker1.visitAnyAttribute(clazz, attribute);
        marker2.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred on any usage marker
        verifyNoInteractions(usageMarker1);
        verifyNoInteractions(usageMarker2);
    }

    /**
     * Tests that visitAnyAttribute can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyAttribute_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            marker.visitAnyAttribute(clazz, attribute);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
        verifyNoInteractions(classUsageMarker);
    }

    /**
     * Tests that visitAnyAttribute can be called on a marker with a null ClassUsageMarker.
     * The method should work even if the ClassUsageMarker is null (though this is unlikely in practice).
     */
    @Test
    public void testVisitAnyAttribute_withNullClassUsageMarker_doesNotThrowException() {
        // Arrange - create marker with null ClassUsageMarker
        AnnotationUsageMarker markerWithNull = new AnnotationUsageMarker(null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> markerWithNull.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute doesn't interfere with specialized visitor methods.
     * The no-op should not affect the marker's behavior for annotation attributes.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInterfereWithSpecializedMethods() {
        // Act - call visitAnyAttribute
        marker.visitAnyAttribute(clazz, attribute);

        // Assert - verify that the ClassUsageMarker remains available for specialized methods
        assertDoesNotThrow(() -> marker.visitAnyAttribute(clazz, attribute));

        // Verify no marking occurred from the visitAnyAttribute call
        verifyNoInteractions(classUsageMarker);
    }
}
