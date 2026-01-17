package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DefaultInterfaceMethodConverter#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern for attributes that don't have specialized visitor methods.
 */
public class DefaultInterfaceMethodConverterClaude_visitAnyAttributeTest {

    private DefaultInterfaceMethodConverter converter;
    private ClassVisitor modifiedClassVisitor;
    private MemberVisitor extraMemberVisitor;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        modifiedClassVisitor = mock(ClassVisitor.class);
        extraMemberVisitor = mock(MemberVisitor.class);

        converter = new DefaultInterfaceMethodConverter(
            modifiedClassVisitor,
            extraMemberVisitor
        );

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
        assertDoesNotThrow(() -> converter.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Clazz parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyAttribute(null, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called with null Attribute parameter.
     * The method should handle null gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withNullAttribute_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyAttribute(clazz, null));
    }

    /**
     * Tests that visitAnyAttribute can be called with both parameters null.
     * The method should handle null parameters gracefully since it's a no-op.
     */
    @Test
    public void testVisitAnyAttribute_withBothParametersNull_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitAnyAttribute(null, null));
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times in succession.
     * The method should be idempotent and handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimes_doesNotThrowException() {
        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> {
            converter.visitAnyAttribute(clazz, attribute);
            converter.visitAnyAttribute(clazz, attribute);
            converter.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't interact with the Clazz parameter.
     * Since it's a no-op method, it should not call any methods on the clazz.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithClazz() {
        // Act
        converter.visitAnyAttribute(clazz, attribute);

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
        converter.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with the attribute mock
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute doesn't affect the converter's internal state.
     * Calling the method should not change any fields or trigger any side effects.
     */
    @Test
    public void testVisitAnyAttribute_doesNotModifyConverterState() {
        // Arrange - create another converter with the same configuration
        DefaultInterfaceMethodConverter converter2 = new DefaultInterfaceMethodConverter(
            modifiedClassVisitor,
            extraMemberVisitor
        );

        // Act - call visitAnyAttribute on the first converter
        converter.visitAnyAttribute(clazz, attribute);

        // Assert - both converters should be functionally equivalent
        // Since visitAnyAttribute is a no-op, we verify no visitors were invoked
        verifyNoInteractions(modifiedClassVisitor);
        verifyNoInteractions(extraMemberVisitor);
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
            converter.visitAnyAttribute(clazz1, attribute);
            converter.visitAnyAttribute(clazz2, attribute);
            converter.visitAnyAttribute(clazz3, attribute);
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
            converter.visitAnyAttribute(clazz, attr1);
            converter.visitAnyAttribute(clazz, attr2);
            converter.visitAnyAttribute(clazz, attr3);
        });
    }

    /**
     * Tests that visitAnyAttribute doesn't trigger the modified class visitor.
     * Since it's a no-op method, it should not mark any classes as modified.
     */
    @Test
    public void testVisitAnyAttribute_doesNotTriggerModifiedClassVisitor() {
        // Act
        converter.visitAnyAttribute(clazz, attribute);

        // Assert - verify the modified class visitor was not invoked
        verifyNoInteractions(modifiedClassVisitor);
    }

    /**
     * Tests that visitAnyAttribute doesn't trigger the extra member visitor.
     * Since it's a no-op method, it should not affect member processing.
     */
    @Test
    public void testVisitAnyAttribute_doesNotTriggerExtraMemberVisitor() {
        // Act
        converter.visitAnyAttribute(clazz, attribute);

        // Assert - verify the extra member visitor was not invoked
        verifyNoInteractions(extraMemberVisitor);
    }

    /**
     * Tests that visitAnyAttribute can be called on a converter with null class visitor.
     * The method should work even if optional dependencies are null.
     */
    @Test
    public void testVisitAnyAttribute_withNullClassVisitor_doesNotThrowException() {
        // Arrange - create converter with null class visitor
        DefaultInterfaceMethodConverter converterWithNullVisitor =
            new DefaultInterfaceMethodConverter(null, extraMemberVisitor);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converterWithNullVisitor.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called on a converter with null member visitor.
     * The method should work even if optional dependencies are null.
     */
    @Test
    public void testVisitAnyAttribute_withNullMemberVisitor_doesNotThrowException() {
        // Arrange - create converter with null member visitor
        DefaultInterfaceMethodConverter converterWithNullVisitor =
            new DefaultInterfaceMethodConverter(modifiedClassVisitor, null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converterWithNullVisitor.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute can be called on a converter with all null dependencies.
     * The method should work even when the converter is minimally configured.
     */
    @Test
    public void testVisitAnyAttribute_withAllNullDependencies_doesNotThrowException() {
        // Arrange - create converter with all null dependencies
        DefaultInterfaceMethodConverter converterWithNulls =
            new DefaultInterfaceMethodConverter(null, null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converterWithNulls.visitAnyAttribute(clazz, attribute));
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
            converter.visitAnyAttribute(clazz, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyAttribute should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyAttribute doesn't affect subsequent calls to other methods.
     * The no-op should not interfere with the converter's normal operation.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectSubsequentOperations() {
        // Act - call visitAnyAttribute first
        converter.visitAnyAttribute(clazz, attribute);

        // Then call visitAnyAttribute again
        converter.visitAnyAttribute(clazz, attribute);

        // Assert - verify no side effects occurred
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
        verifyNoInteractions(modifiedClassVisitor);
        verifyNoInteractions(extraMemberVisitor);
    }

    /**
     * Tests that visitAnyAttribute with real Attribute implementations doesn't throw exceptions.
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
            converter.visitAnyAttribute(clazz, attr1);
            converter.visitAnyAttribute(clazz, attr2);
            converter.visitAnyAttribute(clazz, attr3);
        });

        // Verify that the attribute methods were not called since it's a no-op
        verify(attr1, never()).getAttributeName(any());
        verify(attr2, never()).getAttributeName(any());
        verify(attr3, never()).getAttributeName(any());
    }

    /**
     * Tests that multiple converters can call visitAnyAttribute independently.
     * Each converter's no-op should not affect others.
     */
    @Test
    public void testVisitAnyAttribute_withMultipleConverters_operateIndependently() {
        // Arrange - create multiple converters
        ClassVisitor visitor1 = mock(ClassVisitor.class);
        ClassVisitor visitor2 = mock(ClassVisitor.class);
        MemberVisitor memberVisitor1 = mock(MemberVisitor.class);
        MemberVisitor memberVisitor2 = mock(MemberVisitor.class);

        DefaultInterfaceMethodConverter converter1 =
            new DefaultInterfaceMethodConverter(visitor1, memberVisitor1);
        DefaultInterfaceMethodConverter converter2 =
            new DefaultInterfaceMethodConverter(visitor2, memberVisitor2);

        // Act - call visitAnyAttribute on both converters
        converter1.visitAnyAttribute(clazz, attribute);
        converter2.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred on any visitor
        verifyNoInteractions(visitor1);
        verifyNoInteractions(visitor2);
        verifyNoInteractions(memberVisitor1);
        verifyNoInteractions(memberVisitor2);
    }

    /**
     * Tests that visitAnyAttribute can be called with the same parameters repeatedly
     * without accumulating any state or causing issues.
     */
    @Test
    public void testVisitAnyAttribute_repeatedCallsWithSameParameters_noStateAccumulation() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            converter.visitAnyAttribute(clazz, attribute);
        }

        // Assert - verify no interactions occurred despite multiple calls
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
        verifyNoInteractions(modifiedClassVisitor);
        verifyNoInteractions(extraMemberVisitor);
    }
}
