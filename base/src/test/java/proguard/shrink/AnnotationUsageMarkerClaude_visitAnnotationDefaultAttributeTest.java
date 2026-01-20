package proguard.shrink;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.annotation.AnnotationDefaultAttribute;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AnnotationUsageMarker#visitAnnotationDefaultAttribute(Clazz, Method, AnnotationDefaultAttribute)}.
 *
 * The visitAnnotationDefaultAttribute method processes annotation default values:
 * 1. Accepts the default value through defaultValueAccept
 * 2. Always marks the annotation default attribute as used
 * 3. Marks the constant at the attribute name index
 */
public class AnnotationUsageMarkerClaude_visitAnnotationDefaultAttributeTest {

    private AnnotationUsageMarker marker;
    private ClassUsageMarker classUsageMarker;
    private Clazz clazz;
    private Method method;
    private TestAnnotationDefaultAttribute annotationDefaultAttribute;

    /**
     * Simple test stub for AnnotationDefaultAttribute that allows setting the field value.
     * This stub is necessary because AnnotationDefaultAttribute has public fields that cannot
     * be mocked with Mockito, so we need a concrete implementation for testing.
     */
    private static class TestAnnotationDefaultAttribute extends AnnotationDefaultAttribute {
        private boolean defaultValueAcceptCalled = false;
        private Clazz lastClazz = null;
        private ElementValueVisitor lastVisitor = null;

        public TestAnnotationDefaultAttribute(int attributeNameIndex) {
            this.u2attributeNameIndex = attributeNameIndex;
        }

        @Override
        public void defaultValueAccept(Clazz clazz, ElementValueVisitor elementValueVisitor) {
            defaultValueAcceptCalled = true;
            lastClazz = clazz;
            lastVisitor = elementValueVisitor;
        }

        public boolean wasDefaultValueAcceptCalled() {
            return defaultValueAcceptCalled;
        }

        public Clazz getLastClazz() {
            return lastClazz;
        }

        public ElementValueVisitor getLastVisitor() {
            return lastVisitor;
        }

        public void reset() {
            defaultValueAcceptCalled = false;
            lastClazz = null;
            lastVisitor = null;
        }
    }

    @BeforeEach
    public void setUp() {
        classUsageMarker = mock(ClassUsageMarker.class);
        marker = new AnnotationUsageMarker(classUsageMarker);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);

        // Create a test attribute with a default attribute name index of 1
        annotationDefaultAttribute = new TestAnnotationDefaultAttribute(1);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute correctly delegates to defaultValueAccept.
     * This is the first action the method performs.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_delegatesToDefaultValueAccept() {
        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify that defaultValueAccept was called with correct parameters
        assertTrue(annotationDefaultAttribute.wasDefaultValueAcceptCalled(),
            "defaultValueAccept should have been called");
        assertSame(clazz, annotationDefaultAttribute.getLastClazz(),
            "Clazz should be passed to defaultValueAccept");
        assertSame(marker, annotationDefaultAttribute.getLastVisitor(),
            "Marker should be passed as visitor to defaultValueAccept");
    }

    /**
     * Tests that visitAnnotationDefaultAttribute always marks the attribute as used.
     * Unlike other visitor methods that conditionally mark, this always marks.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_alwaysMarksAttributeAsUsed() {
        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify that the attribute is marked as used
        verify(classUsageMarker).markAsUsed(annotationDefaultAttribute);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute marks the constant at the attribute name index.
     * This constant must be preserved for the attribute to be valid.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_marksAttributeNameConstant() {
        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify the constant pool entry was accessed
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute performs all three actions in order.
     * The method should: 1) accept default value, 2) mark as used, 3) mark constant.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_performsAllActions() {
        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify all actions occurred
        assertTrue(annotationDefaultAttribute.wasDefaultValueAcceptCalled());
        verify(classUsageMarker).markAsUsed(annotationDefaultAttribute);
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute does not throw an exception with valid parameters.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withValidParameters_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() ->
            marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute)
        );
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with different attribute name indices.
     * The constant marking should work for various valid indices.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentAttributeNameIndices() {
        // Arrange
        TestAnnotationDefaultAttribute attr = new TestAnnotationDefaultAttribute(5);

        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, attr);

        // Assert - verify the correct index was used
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute handles attribute name index of 0.
     * Index 0 is typically invalid and should not cause constant marking.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withZeroAttributeNameIndex_doesNotMarkConstant() {
        // Arrange
        TestAnnotationDefaultAttribute attr = new TestAnnotationDefaultAttribute(0);

        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, attr);

        // Assert - verify constant pool entry was not accessed for index 0
        verify(clazz, never()).constantPoolEntryAccept(eq(0), any());

        // But attribute should still be marked as used
        verify(classUsageMarker).markAsUsed(attr);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute passes the marker itself as the visitor.
     * The marker implements ElementValueVisitor, so it should be used for callbacks.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_passesMarkerAsVisitor() {
        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify the marker itself is passed as the visitor
        assertSame(marker, annotationDefaultAttribute.getLastVisitor());
    }

    /**
     * Tests that visitAnnotationDefaultAttribute can be called multiple times.
     * Each call should independently perform all actions.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_calledMultipleTimes_performsActionsEachTime() {
        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify actions were performed 3 times
        verify(classUsageMarker, times(3)).markAsUsed(annotationDefaultAttribute);
        verify(clazz, times(3)).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with different attribute instances.
     * Each attribute should be processed independently.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentAttributes_processesEach() {
        // Arrange
        TestAnnotationDefaultAttribute attr1 = new TestAnnotationDefaultAttribute(1);
        TestAnnotationDefaultAttribute attr2 = new TestAnnotationDefaultAttribute(2);

        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, attr1);
        marker.visitAnnotationDefaultAttribute(clazz, method, attr2);

        // Assert - verify each attribute was processed
        assertTrue(attr1.wasDefaultValueAcceptCalled());
        assertTrue(attr2.wasDefaultValueAcceptCalled());
        verify(classUsageMarker).markAsUsed(attr1);
        verify(classUsageMarker).markAsUsed(attr2);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with different clazz instances.
     * Each clazz should be correctly used in the processing.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentClazz_usesCorrectClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act
        marker.visitAnnotationDefaultAttribute(clazz1, method, annotationDefaultAttribute);
        annotationDefaultAttribute.reset();
        marker.visitAnnotationDefaultAttribute(clazz2, method, annotationDefaultAttribute);

        // Assert - verify the correct clazz was used in the last call
        assertSame(clazz2, annotationDefaultAttribute.getLastClazz());
        verify(clazz1).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz2).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with different method instances.
     * The method parameter is accepted but not used in the actual processing.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withDifferentMethods_acceptsEach() {
        // Arrange
        Method method1 = mock(ProgramMethod.class);
        Method method2 = mock(ProgramMethod.class);

        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method1, annotationDefaultAttribute);
        marker.visitAnnotationDefaultAttribute(clazz, method2, annotationDefaultAttribute);

        // Assert - verify processing occurred for each method context
        verify(classUsageMarker, times(2)).markAsUsed(annotationDefaultAttribute);
    }

    /**
     * Tests that the marker is a valid ElementValueVisitor.
     * This is required for the defaultValueAccept callback to work correctly.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_markerIsValidElementValueVisitor() {
        // Assert - verify the marker implements ElementValueVisitor
        assertTrue(marker instanceof ElementValueVisitor,
            "Marker should implement ElementValueVisitor for defaultValueAccept callbacks");

        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify it's passed as an ElementValueVisitor
        assertTrue(annotationDefaultAttribute.getLastVisitor() instanceof ElementValueVisitor);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute always marks as used regardless of other state.
     * This is different from conditional marking in other visitor methods.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_alwaysMarksUsed_unconditionally() {
        // Act - call multiple times
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify marking happened every time, unconditionally
        verify(classUsageMarker, times(2)).markAsUsed(annotationDefaultAttribute);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute does not directly interact with the method parameter.
     * The method provides context but is not used in the delegation or marking.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_doesNotInteractWithMethod() {
        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify no interactions with the method
        verifyNoInteractions(method);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works with various attribute name index values.
     * All positive index values should be handled correctly.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withVariousIndices_marksCorrectConstant() {
        // Arrange
        int[] indices = {1, 5, 10, 100, 1000};

        for (int index : indices) {
            TestAnnotationDefaultAttribute attr = new TestAnnotationDefaultAttribute(index);

            // Act
            marker.visitAnnotationDefaultAttribute(clazz, method, attr);

            // Assert
            verify(clazz).constantPoolEntryAccept(eq(index), eq(marker));
            verify(classUsageMarker).markAsUsed(attr);
        }
    }

    /**
     * Tests that multiple markers can process the same attribute independently.
     * Each marker should perform its own marking operations.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withMultipleMarkers_operateIndependently() {
        // Arrange
        ClassUsageMarker usageMarker2 = mock(ClassUsageMarker.class);
        AnnotationUsageMarker marker2 = new AnnotationUsageMarker(usageMarker2);

        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        marker2.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify each marker performed its own marking
        verify(classUsageMarker).markAsUsed(annotationDefaultAttribute);
        verify(usageMarker2).markAsUsed(annotationDefaultAttribute);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute completes all actions even if defaultValueAccept does nothing.
     * The marking should happen regardless of what the callback does.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_completesAllActions_evenWithNoOpCallback() {
        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify marking still happens
        verify(classUsageMarker).markAsUsed(annotationDefaultAttribute);
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitAnnotationDefaultAttribute can be called in rapid succession without issues.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_rapidSuccession_noIssues() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
            }
        }, "Rapid successive calls should not cause issues");

        // Verify all actions occurred for all calls
        verify(classUsageMarker, times(1000)).markAsUsed(annotationDefaultAttribute);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute works correctly as part of the AttributeVisitor interface.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_asAttributeVisitorInterface_worksCorrectly() {
        // Arrange - treat marker as AttributeVisitor interface
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = marker;

        // Act
        visitor.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify standard processing occurred
        assertTrue(annotationDefaultAttribute.wasDefaultValueAcceptCalled());
        verify(classUsageMarker).markAsUsed(annotationDefaultAttribute);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute on a newly created marker behaves consistently.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_onNewInstance_behavesConsistently() {
        // Arrange - create a fresh marker
        ClassUsageMarker freshUsageMarker = mock(ClassUsageMarker.class);
        AnnotationUsageMarker freshMarker = new AnnotationUsageMarker(freshUsageMarker);

        // Act - immediately call visitAnnotationDefaultAttribute
        freshMarker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - should work properly
        assertTrue(annotationDefaultAttribute.wasDefaultValueAcceptCalled());
        verify(freshUsageMarker).markAsUsed(annotationDefaultAttribute);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute correctly handles the semantic difference
     * from other visitor methods: it ALWAYS marks, not conditionally.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_semanticDifference_alwaysMarks() {
        // This test documents the key semantic difference:
        // visitAnnotationDefaultAttribute ALWAYS marks the attribute as used,
        // unlike visitAnyAnnotationsAttribute which conditionally marks based on annotation usage.

        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - always marks regardless of any conditions
        verify(classUsageMarker).markAsUsed(annotationDefaultAttribute);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute passes clazz to both defaultValueAccept and constantPoolEntryAccept.
     * The clazz is used for both operations to maintain context.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_usesClazzForBothOperations() {
        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

        // Assert - verify clazz is used for both operations
        assertSame(clazz, annotationDefaultAttribute.getLastClazz());
        verify(clazz).constantPoolEntryAccept(anyInt(), any());
    }

    /**
     * Tests that visitAnnotationDefaultAttribute handles sequential calls with different combinations.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_sequentialDifferentCombinations_allProcessed() {
        // Arrange
        Clazz clazz2 = mock(ProgramClass.class);
        Method method2 = mock(ProgramMethod.class);
        TestAnnotationDefaultAttribute attr2 = new TestAnnotationDefaultAttribute(2);

        // Act - various combinations
        marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        marker.visitAnnotationDefaultAttribute(clazz2, method2, attr2);
        marker.visitAnnotationDefaultAttribute(clazz, method2, attr2);

        // Assert - all processed correctly
        assertTrue(annotationDefaultAttribute.wasDefaultValueAcceptCalled());
        assertTrue(attr2.wasDefaultValueAcceptCalled());
        verify(classUsageMarker).markAsUsed(annotationDefaultAttribute);
        verify(classUsageMarker, times(2)).markAsUsed(attr2);
    }

    /**
     * Tests that visitAnnotationDefaultAttribute executes quickly as a simple delegation method.
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            marker.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
            "visitAnnotationDefaultAttribute should execute quickly, took " + durationMs + "ms");
    }

    /**
     * Tests that negative attribute name index values are treated as invalid (less than or equal to 0).
     */
    @Test
    public void testVisitAnnotationDefaultAttribute_withNegativeIndex_doesNotMarkConstant() {
        // Arrange
        TestAnnotationDefaultAttribute attr = new TestAnnotationDefaultAttribute(-1);

        // Act
        marker.visitAnnotationDefaultAttribute(clazz, method, attr);

        // Assert - verify constant pool entry was not accessed for negative index
        verify(clazz, never()).constantPoolEntryAccept(eq(-1), any());

        // But attribute should still be marked as used
        verify(classUsageMarker).markAsUsed(attr);
    }
}
