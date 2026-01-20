package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MarkedAnnotationDeleter#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern for attributes that don't have specialized visitor methods.
 * The real functionality of MarkedAnnotationDeleter is in the specialized visitor methods like
 * visitRuntimeVisibleAnnotationsAttribute, visitRuntimeInvisibleAnnotationsAttribute, etc.
 */
public class MarkedAnnotationDeleterClaude_visitAnyAttributeTest {

    private MarkedAnnotationDeleter deleter;
    private Object mark;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        mark = new Object(); // Create a unique mark object
        deleter = new MarkedAnnotationDeleter(mark);
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
        assertDoesNotThrow(() -> deleter.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> deleter.visitAnyAttribute(null, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> deleter.visitAnyAttribute(clazz, null));
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> deleter.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            deleter.visitAnyAttribute(clazz, attribute);
            deleter.visitAnyAttribute(clazz, attribute);
            deleter.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Act
        deleter.visitAnyAttribute(clazz, attribute);

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
        deleter.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the attribute mock
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't affect the deleter's internal state.
     * Calling the method should not change any fields or trigger any side effects.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyDeleterState() {
        // Arrange - create another deleter with the same mark
        MarkedAnnotationDeleter deleter2 = new MarkedAnnotationDeleter(mark);

        // Act - call visitAnyAttribute on the first deleter
        deleter.visitAnyAttribute(clazz, attribute);

        // Assert - both deleters should be functionally equivalent
        // Since visitAnyAttribute is a no-op, verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
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
            deleter.visitAnyAttribute(clazz1, attribute);
            deleter.visitAnyAttribute(clazz2, attribute);
            deleter.visitAnyAttribute(clazz3, attribute);
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
            deleter.visitAnyAttribute(clazz, attr1);
            deleter.visitAnyAttribute(clazz, attr2);
            deleter.visitAnyAttribute(clazz, attr3);
        });
    }

    /**
     * Tests that visitAnyAttribute with different mark objects doesn't affect behavior.
     * Since the method is a no-op, the mark parameter should not matter for this method.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentMarks_doesNotThrowException() {
        // Arrange - create deleters with different marks
        MarkedAnnotationDeleter deleter1 = new MarkedAnnotationDeleter(new Object());
        MarkedAnnotationDeleter deleter2 = new MarkedAnnotationDeleter("stringMark");
        MarkedAnnotationDeleter deleter3 = new MarkedAnnotationDeleter(123);

        // Act & Assert - should not throw any exception with different marks
        assertDoesNotThrow(() -> {
            deleter1.visitAnyAttribute(clazz, attribute);
            deleter2.visitAnyAttribute(clazz, attribute);
            deleter3.visitAnyAttribute(clazz, attribute);
        });
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
            deleter.visitAnyAttribute(clazz, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyAttribute should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyAttribute doesn't affect subsequent calls to other methods.
     * The no-op should not interfere with the deleter's normal operation.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyAttribute first
        deleter.visitAnyAttribute(clazz, attribute);

        // Then call visitAnyAttribute again
        deleter.visitAnyAttribute(clazz, attribute);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute with mocked Attribute types doesn't throw exceptions.
     * This ensures the no-op works with various attribute types.
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
            deleter.visitAnyAttribute(clazz, attr1);
            deleter.visitAnyAttribute(clazz, attr2);
            deleter.visitAnyAttribute(clazz, attr3);
        });

        // Verify that the attribute methods were not called since it's a no-op
        verify(attr1, never()).getAttributeName(any());
        verify(attr2, never()).getAttributeName(any());
        verify(attr3, never()).getAttributeName(any());
    }

    /**
     * Tests that multiple deleters can call visitAnyAttribute independently.
     * Each deleter's no-op should not affect others.
     */
    @Test
    public void testVisitAnyAttribute_withMultipleDeleters_operateIndependently() {
        // Arrange - create multiple deleters with different marks
        Object mark1 = new Object();
        Object mark2 = new Object();
        MarkedAnnotationDeleter deleter1 = new MarkedAnnotationDeleter(mark1);
        MarkedAnnotationDeleter deleter2 = new MarkedAnnotationDeleter(mark2);

        // Act - call visitAnyAttribute on both deleters
        deleter1.visitAnyAttribute(clazz, attribute);
        deleter2.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyAttribute_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            deleter.visitAnyAttribute(clazz, attribute);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute works correctly when called before and after
     * other visitor methods (though we're just testing the no-op behavior here).
     */
    @Test
    public void testVisitAnyAttribute_mixedWithOtherCalls_doesNotThrowException() {
        // Act & Assert - call visitAnyAttribute interspersed with itself
        assertDoesNotThrow(() -> {
            deleter.visitAnyAttribute(clazz, attribute);
            deleter.visitAnyAttribute(clazz, mock(Attribute.class));
            deleter.visitAnyAttribute(mock(ProgramClass.class), attribute);
            deleter.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute can be invoked with null mark in constructor.
     * The method should still work as a no-op even with null mark.
     */
    @Test
    public void testVisitAnyAttribute_withNullMark_doesNotThrowException() {
        // Arrange - create deleter with null mark
        MarkedAnnotationDeleter deleterWithNullMark = new MarkedAnnotationDeleter(null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> deleterWithNullMark.visitAnyAttribute(clazz, attribute));
    }
}
