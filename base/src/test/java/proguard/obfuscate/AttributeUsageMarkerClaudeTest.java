package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.attribute.Attribute;
import proguard.util.Processable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AttributeUsageMarker}.
 *
 * This class tests the AttributeUsageMarker which marks attributes as being used
 * by setting processing information on Processable objects (typically Attribute instances).
 *
 * The tests cover:
 * - Constructor initialization
 * - visitAnyAttribute marking behavior
 * - isUsed static method verification
 */
public class AttributeUsageMarkerClaudeTest {

    private AttributeUsageMarker attributeUsageMarker;
    private Clazz mockClazz;
    private Attribute mockAttribute;

    @BeforeEach
    public void setUp() {
        attributeUsageMarker = new AttributeUsageMarker();
        mockClazz = mock(Clazz.class);
        mockAttribute = mock(Attribute.class);
    }

    // Tests for constructor: <init>.()V

    /**
     * Tests that the constructor successfully creates an AttributeUsageMarker instance.
     * This verifies the default no-args constructor works correctly.
     */
    @Test
    public void testConstructor_createsValidInstance() {
        // Act
        AttributeUsageMarker marker = new AttributeUsageMarker();

        // Assert
        assertNotNull(marker, "Constructor should create a non-null instance");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleIndependentInstances() {
        // Act
        AttributeUsageMarker marker1 = new AttributeUsageMarker();
        AttributeUsageMarker marker2 = new AttributeUsageMarker();
        AttributeUsageMarker marker3 = new AttributeUsageMarker();

        // Assert
        assertNotNull(marker1);
        assertNotNull(marker2);
        assertNotNull(marker3);
        assertNotSame(marker1, marker2, "Each constructor call should create a distinct instance");
        assertNotSame(marker2, marker3, "Each constructor call should create a distinct instance");
        assertNotSame(marker1, marker3, "Each constructor call should create a distinct instance");
    }

    // Tests for visitAnyAttribute.(Lproguard/classfile/Clazz;Lproguard/classfile/attribute/Attribute;)V

    /**
     * Tests that visitAnyAttribute marks an attribute as used.
     * After visiting, the attribute should have processing info set.
     */
    @Test
    public void testVisitAnyAttribute_marksAttributeAsUsed() {
        // Act
        attributeUsageMarker.visitAnyAttribute(mockClazz, mockAttribute);

        // Assert
        verify(mockAttribute, times(1)).setProcessingInfo(any());
    }

    /**
     * Tests that visitAnyAttribute can be called without exceptions.
     */
    @Test
    public void testVisitAnyAttribute_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> attributeUsageMarker.visitAnyAttribute(mockClazz, mockAttribute));
    }

    /**
     * Tests that visitAnyAttribute works with a null Clazz parameter.
     * The method should handle this gracefully since it only operates on the attribute.
     */
    @Test
    public void testVisitAnyAttribute_withNullClazz() {
        // Act & Assert
        assertDoesNotThrow(() -> attributeUsageMarker.visitAnyAttribute(null, mockAttribute));
        verify(mockAttribute, times(1)).setProcessingInfo(any());
    }

    /**
     * Tests that visitAnyAttribute can mark multiple attributes as used.
     */
    @Test
    public void testVisitAnyAttribute_marksMultipleAttributes() {
        // Arrange
        Attribute attribute1 = mock(Attribute.class);
        Attribute attribute2 = mock(Attribute.class);
        Attribute attribute3 = mock(Attribute.class);

        // Act
        attributeUsageMarker.visitAnyAttribute(mockClazz, attribute1);
        attributeUsageMarker.visitAnyAttribute(mockClazz, attribute2);
        attributeUsageMarker.visitAnyAttribute(mockClazz, attribute3);

        // Assert
        verify(attribute1, times(1)).setProcessingInfo(any());
        verify(attribute2, times(1)).setProcessingInfo(any());
        verify(attribute3, times(1)).setProcessingInfo(any());
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times on the same attribute.
     * Each call should set the processing info again.
     */
    @Test
    public void testVisitAnyAttribute_calledMultipleTimesOnSameAttribute() {
        // Act
        attributeUsageMarker.visitAnyAttribute(mockClazz, mockAttribute);
        attributeUsageMarker.visitAnyAttribute(mockClazz, mockAttribute);
        attributeUsageMarker.visitAnyAttribute(mockClazz, mockAttribute);

        // Assert
        verify(mockAttribute, times(3)).setProcessingInfo(any());
    }

    /**
     * Tests that visitAnyAttribute works with different Clazz instances.
     */
    @Test
    public void testVisitAnyAttribute_withDifferentClazzes() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        // Act
        attributeUsageMarker.visitAnyAttribute(clazz1, mockAttribute);
        attributeUsageMarker.visitAnyAttribute(clazz2, mockAttribute);

        // Assert
        verify(mockAttribute, times(2)).setProcessingInfo(any());
    }

    /**
     * Tests that multiple AttributeUsageMarker instances can mark attributes independently.
     */
    @Test
    public void testVisitAnyAttribute_multipleMarkerInstances() {
        // Arrange
        AttributeUsageMarker marker1 = new AttributeUsageMarker();
        AttributeUsageMarker marker2 = new AttributeUsageMarker();
        Attribute attr1 = mock(Attribute.class);
        Attribute attr2 = mock(Attribute.class);

        // Act
        marker1.visitAnyAttribute(mockClazz, attr1);
        marker2.visitAnyAttribute(mockClazz, attr2);

        // Assert
        verify(attr1, times(1)).setProcessingInfo(any());
        verify(attr2, times(1)).setProcessingInfo(any());
    }

    // Tests for isUsed.(Lproguard/util/Processable;)Z

    /**
     * Tests that isUsed returns false for an attribute that hasn't been marked.
     * An attribute with null or different processing info should not be considered used.
     */
    @Test
    public void testIsUsed_returnsFalseForUnmarkedAttribute() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(null);

        // Act
        boolean result = AttributeUsageMarker.isUsed(processable);

        // Assert
        assertFalse(result, "isUsed should return false for attribute with null processing info");
    }

    /**
     * Tests that isUsed returns false for an attribute with different processing info.
     */
    @Test
    public void testIsUsed_returnsFalseForDifferentProcessingInfo() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(new Object());

        // Act
        boolean result = AttributeUsageMarker.isUsed(processable);

        // Assert
        assertFalse(result, "isUsed should return false for attribute with different processing info");
    }

    /**
     * Tests that isUsed can be called multiple times on the same processable.
     */
    @Test
    public void testIsUsed_calledMultipleTimes() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(null);

        // Act
        boolean result1 = AttributeUsageMarker.isUsed(processable);
        boolean result2 = AttributeUsageMarker.isUsed(processable);
        boolean result3 = AttributeUsageMarker.isUsed(processable);

        // Assert
        assertFalse(result1);
        assertFalse(result2);
        assertFalse(result3);
    }

    /**
     * Tests that isUsed works with different Processable instances.
     */
    @Test
    public void testIsUsed_withMultipleProcessables() {
        // Arrange
        Processable processable1 = mock(Processable.class);
        Processable processable2 = mock(Processable.class);
        Processable processable3 = mock(Processable.class);

        when(processable1.getProcessingInfo()).thenReturn(null);
        when(processable2.getProcessingInfo()).thenReturn(new Object());
        when(processable3.getProcessingInfo()).thenReturn(null);

        // Act
        boolean result1 = AttributeUsageMarker.isUsed(processable1);
        boolean result2 = AttributeUsageMarker.isUsed(processable2);
        boolean result3 = AttributeUsageMarker.isUsed(processable3);

        // Assert
        assertFalse(result1);
        assertFalse(result2);
        assertFalse(result3);
    }

    // Integration tests combining visitAnyAttribute and isUsed

    /**
     * Tests the full workflow: marking an attribute as used and then checking if it's used.
     * This is an integration test that verifies the interaction between visitAnyAttribute
     * and isUsed methods.
     *
     * Note: This test uses a real-world scenario where we mark an attribute and then check
     * its status. Since we're using mocks, we need to simulate the behavior of setting
     * and retrieving the processing info.
     */
    @Test
    public void testIntegration_markAndCheckAttribute() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        // We need to capture what value is set and then return it when getProcessingInfo is called
        // This simulates the real behavior of the Processable interface
        final Object[] capturedValue = new Object[1];
        doAnswer(invocation -> {
            capturedValue[0] = invocation.getArgument(0);
            return null;
        }).when(attribute).setProcessingInfo(any());

        when(attribute.getProcessingInfo()).thenAnswer(invocation -> capturedValue[0]);

        // Act
        attributeUsageMarker.visitAnyAttribute(mockClazz, attribute);
        boolean isUsedAfterMarking = AttributeUsageMarker.isUsed(attribute);

        // Assert
        assertTrue(isUsedAfterMarking, "Attribute should be marked as used after visitAnyAttribute is called");
    }

    /**
     * Tests that an attribute remains marked across multiple checks.
     */
    @Test
    public void testIntegration_attributeRemainsMarked() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        final Object[] capturedValue = new Object[1];
        doAnswer(invocation -> {
            capturedValue[0] = invocation.getArgument(0);
            return null;
        }).when(attribute).setProcessingInfo(any());

        when(attribute.getProcessingInfo()).thenAnswer(invocation -> capturedValue[0]);

        // Act
        attributeUsageMarker.visitAnyAttribute(mockClazz, attribute);
        boolean check1 = AttributeUsageMarker.isUsed(attribute);
        boolean check2 = AttributeUsageMarker.isUsed(attribute);
        boolean check3 = AttributeUsageMarker.isUsed(attribute);

        // Assert
        assertTrue(check1, "First check should return true");
        assertTrue(check2, "Second check should return true");
        assertTrue(check3, "Third check should return true");
    }

    /**
     * Tests that marking one attribute doesn't affect other attributes.
     */
    @Test
    public void testIntegration_markingOneAttributeDoesNotAffectOthers() {
        // Arrange
        Attribute markedAttribute = mock(Attribute.class);
        Attribute unmarkedAttribute = mock(Attribute.class);

        final Object[] capturedValueMarked = new Object[1];
        doAnswer(invocation -> {
            capturedValueMarked[0] = invocation.getArgument(0);
            return null;
        }).when(markedAttribute).setProcessingInfo(any());

        when(markedAttribute.getProcessingInfo()).thenAnswer(invocation -> capturedValueMarked[0]);
        when(unmarkedAttribute.getProcessingInfo()).thenReturn(null);

        // Act
        attributeUsageMarker.visitAnyAttribute(mockClazz, markedAttribute);
        boolean markedIsUsed = AttributeUsageMarker.isUsed(markedAttribute);
        boolean unmarkedIsUsed = AttributeUsageMarker.isUsed(unmarkedAttribute);

        // Assert
        assertTrue(markedIsUsed, "Marked attribute should be used");
        assertFalse(unmarkedIsUsed, "Unmarked attribute should not be used");
    }

    /**
     * Tests that multiple markers can mark different attributes independently.
     */
    @Test
    public void testIntegration_multipleMarkersWorkIndependently() {
        // Arrange
        AttributeUsageMarker marker1 = new AttributeUsageMarker();
        AttributeUsageMarker marker2 = new AttributeUsageMarker();

        Attribute attribute1 = mock(Attribute.class);
        Attribute attribute2 = mock(Attribute.class);

        final Object[] capturedValue1 = new Object[1];
        final Object[] capturedValue2 = new Object[1];

        doAnswer(invocation -> {
            capturedValue1[0] = invocation.getArgument(0);
            return null;
        }).when(attribute1).setProcessingInfo(any());

        doAnswer(invocation -> {
            capturedValue2[0] = invocation.getArgument(0);
            return null;
        }).when(attribute2).setProcessingInfo(any());

        when(attribute1.getProcessingInfo()).thenAnswer(invocation -> capturedValue1[0]);
        when(attribute2.getProcessingInfo()).thenAnswer(invocation -> capturedValue2[0]);

        // Act
        marker1.visitAnyAttribute(mockClazz, attribute1);
        marker2.visitAnyAttribute(mockClazz, attribute2);

        boolean attr1IsUsed = AttributeUsageMarker.isUsed(attribute1);
        boolean attr2IsUsed = AttributeUsageMarker.isUsed(attribute2);

        // Assert
        assertTrue(attr1IsUsed, "Attribute 1 should be marked as used");
        assertTrue(attr2IsUsed, "Attribute 2 should be marked as used");
    }

    /**
     * Tests that remarking an already marked attribute keeps it marked.
     */
    @Test
    public void testIntegration_remarksAlreadyMarkedAttribute() {
        // Arrange
        Attribute attribute = mock(Attribute.class);

        final Object[] capturedValue = new Object[1];
        doAnswer(invocation -> {
            capturedValue[0] = invocation.getArgument(0);
            return null;
        }).when(attribute).setProcessingInfo(any());

        when(attribute.getProcessingInfo()).thenAnswer(invocation -> capturedValue[0]);

        // Act
        attributeUsageMarker.visitAnyAttribute(mockClazz, attribute);
        boolean usedAfterFirstMark = AttributeUsageMarker.isUsed(attribute);

        attributeUsageMarker.visitAnyAttribute(mockClazz, attribute);
        boolean usedAfterSecondMark = AttributeUsageMarker.isUsed(attribute);

        // Assert
        assertTrue(usedAfterFirstMark, "Should be used after first mark");
        assertTrue(usedAfterSecondMark, "Should still be used after second mark");
    }

    /**
     * Tests the static nature of isUsed - it can be called without an instance.
     */
    @Test
    public void testIsUsed_staticMethodBehavior() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(null);

        // Act - call isUsed without using any instance
        boolean result = AttributeUsageMarker.isUsed(processable);

        // Assert
        assertFalse(result);
        // Verify we can call it multiple times statically
        assertFalse(AttributeUsageMarker.isUsed(processable));
        assertFalse(AttributeUsageMarker.isUsed(processable));
    }
}
