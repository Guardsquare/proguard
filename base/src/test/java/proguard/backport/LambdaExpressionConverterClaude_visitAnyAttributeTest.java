package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.visitor.ClassVisitor;
import proguard.io.ExtraDataEntryNameMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link LambdaExpressionConverter#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is an empty implementation (no-op) that serves as a default
 * handler in the AttributeVisitor pattern for attributes that don't require specialized processing.
 * The LambdaExpressionConverter only processes CodeAttribute via visitCodeAttribute;
 * all other attribute types are handled by this no-op method.
 */
public class LambdaExpressionConverterClaude_visitAnyAttributeTest {

    private LambdaExpressionConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private ExtraDataEntryNameMap extraDataEntryNameMap;
    private ClassVisitor extraClassVisitor;
    private Clazz clazz;
    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        extraDataEntryNameMap = new ExtraDataEntryNameMap();
        extraClassVisitor = mock(ClassVisitor.class);
        converter = new LambdaExpressionConverter(
                programClassPool,
                libraryClassPool,
                extraDataEntryNameMap,
                extraClassVisitor
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
     * Tests that visitAnyAttribute doesn't interact with either mock parameter.
     * Since it's a no-op method, it should not call any methods on either parameter.
     */
    @Test
    public void testVisitAnyAttribute_doesNotInteractWithEitherParameter() {
        // Act
        converter.visitAnyAttribute(clazz, attribute);

        // Assert - verify no interactions occurred with either mock
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
     * Tests that visitAnyAttribute can be called on different converter instances.
     * Each converter instance should work independently.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentConverters_doesNotThrowException() {
        // Arrange
        LambdaExpressionConverter converter1 = new LambdaExpressionConverter(
                programClassPool, libraryClassPool, extraDataEntryNameMap, extraClassVisitor);
        LambdaExpressionConverter converter2 = new LambdaExpressionConverter(
                programClassPool, libraryClassPool, extraDataEntryNameMap, extraClassVisitor);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            converter1.visitAnyAttribute(clazz, attribute);
            converter2.visitAnyAttribute(clazz, attribute);
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
            converter.visitAnyAttribute(clazz, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitAnyAttribute should execute quickly as it's a no-op");
    }

    /**
     * Tests that visitAnyAttribute with mixed null and valid calls works correctly.
     * The method should handle mixed null and non-null calls without issues.
     */
    @Test
    public void testVisitAnyAttribute_mixedNullAndValidCalls_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            converter.visitAnyAttribute(null, null);
            converter.visitAnyAttribute(clazz, attribute);
            converter.visitAnyAttribute(null, attribute);
            converter.visitAnyAttribute(clazz, null);
        });
    }

    /**
     * Tests that visitAnyAttribute can be called with different Clazz instances and same Attribute.
     * The method should handle any combination of parameters.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentClazzInstancesSameAttribute_doesNotThrowException() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act & Assert - should not throw any exception with different clazz instances
        assertDoesNotThrow(() -> {
            converter.visitAnyAttribute(clazz1, attribute);
            converter.visitAnyAttribute(clazz2, attribute);
            converter.visitAnyAttribute(clazz3, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute can be called with same Clazz and different Attributes.
     * The method should handle any combination of parameters.
     */
    @Test
    public void testVisitAnyAttribute_withSameClazzDifferentAttributes_doesNotThrowException() {
        // Arrange
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);
        Attribute attr3 = mock(Attribute.class);

        // Act & Assert - should not throw any exception with different attribute instances
        assertDoesNotThrow(() -> {
            converter.visitAnyAttribute(clazz, attr1);
            converter.visitAnyAttribute(clazz, attr2);
            converter.visitAnyAttribute(clazz, attr3);
        });
    }

    /**
     * Tests that visitAnyAttribute can be called alternately with other visitor methods.
     * The methods should work independently without interfering with each other.
     */
    @Test
    public void testVisitAnyAttribute_alternatingWithOtherMethods_doesNotThrowException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> {
            converter.visitAnyAttribute(clazz, attribute);
            converter.visitAnyClass(clazz);
            converter.visitAnyAttribute(clazz, attribute);
            converter.visitProgramClass(programClass);
            converter.visitAnyAttribute(clazz, attribute);
        });
    }

    /**
     * Tests that visitAnyAttribute with various configured attribute mocks doesn't throw exceptions.
     * This ensures the no-op works with attributes that have stubbed methods.
     */
    @Test
    public void testVisitAnyAttribute_withConfiguredAttributeMocks_doesNotThrowException() {
        // Arrange - test with various attribute types with stubbed methods
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
    }

    /**
     * Tests that visitAnyAttribute can be invoked using the AttributeVisitor interface.
     * Verifies polymorphic behavior through the interface.
     */
    @Test
    public void testVisitAnyAttribute_viaAttributeVisitorInterface_doesNotThrowException() {
        // Arrange - use the converter as an AttributeVisitor
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = converter;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> visitor.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that multiple converters can independently call visitAnyAttribute.
     * Each converter should maintain its own independent state.
     */
    @Test
    public void testVisitAnyAttribute_multipleConvertersIndependent() {
        // Arrange
        ClassPool pool1 = new ClassPool();
        ClassPool pool2 = new ClassPool();
        LambdaExpressionConverter converter1 = new LambdaExpressionConverter(
                pool1, libraryClassPool, extraDataEntryNameMap, extraClassVisitor);
        LambdaExpressionConverter converter2 = new LambdaExpressionConverter(
                pool2, libraryClassPool, extraDataEntryNameMap, extraClassVisitor);

        // Act
        converter1.visitAnyAttribute(clazz, attribute);
        converter2.visitAnyAttribute(clazz, attribute);

        // Assert - verify each converter works independently
        assertNotSame(converter1, converter2);
        verifyNoInteractions(clazz);
        verifyNoInteractions(attribute);
    }

    /**
     * Tests that visitAnyAttribute created with null constructor parameters works correctly.
     * The no-op method should work regardless of the converter's internal state.
     */
    @Test
    public void testVisitAnyAttribute_withNullConstructorParameters_doesNotThrowException() {
        // Arrange
        LambdaExpressionConverter converterWithNulls = new LambdaExpressionConverter(
                null, null, null, null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converterWithNulls.visitAnyAttribute(clazz, attribute));
    }

    /**
     * Tests that visitAnyAttribute doesn't affect other operations on the converter.
     * Calling visitAnyAttribute should not interfere with the converter's other methods.
     */
    @Test
    public void testVisitAnyAttribute_doesNotAffectOtherOperations() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act - call visitAnyAttribute and then other methods
        converter.visitAnyAttribute(clazz, attribute);

        // Assert - other methods should still work normally
        assertDoesNotThrow(() -> {
            converter.visitAnyClass(clazz);
            converter.visitProgramClass(programClass);
        });
    }
}
