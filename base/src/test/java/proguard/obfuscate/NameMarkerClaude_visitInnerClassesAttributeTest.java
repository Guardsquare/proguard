package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.InnerClassesAttribute;
import proguard.classfile.attribute.InnerClassesInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link NameMarker#visitInnerClassesAttribute(Clazz, InnerClassesAttribute)}.
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;Lproguard/classfile/attribute/InnerClassesAttribute;)V
 *
 * The visitInnerClassesAttribute method ensures that outer class names are kept (not obfuscated)
 * by delegating to innerClassEntriesAccept to process each inner class entry. This is part of
 * the name marking strategy where inner class relationships are preserved.
 */
public class NameMarkerClaude_visitInnerClassesAttributeTest {

    private NameMarker nameMarker;
    private Clazz mockClazz;
    private InnerClassesAttribute mockInnerClassesAttribute;

    @BeforeEach
    public void setUp() {
        nameMarker = new NameMarker();
        mockClazz = mock(ProgramClass.class);
        mockInnerClassesAttribute = mock(InnerClassesAttribute.class);
    }

    /**
     * Tests that visitInnerClassesAttribute calls innerClassEntriesAccept on the attribute.
     * This is the primary behavior of the method - it delegates to the attribute to process
     * all inner class entries.
     */
    @Test
    public void testVisitInnerClassesAttribute_callsInnerClassEntriesAccept() {
        // Act
        nameMarker.visitInnerClassesAttribute(mockClazz, mockInnerClassesAttribute);

        // Assert
        verify(mockInnerClassesAttribute, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute does not throw an exception with valid inputs.
     */
    @Test
    public void testVisitInnerClassesAttribute_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> nameMarker.visitInnerClassesAttribute(mockClazz, mockInnerClassesAttribute));
    }

    /**
     * Tests that visitInnerClassesAttribute can be called multiple times on the same attribute.
     * Each call should delegate to innerClassEntriesAccept.
     */
    @Test
    public void testVisitInnerClassesAttribute_calledMultipleTimes() {
        // Act
        nameMarker.visitInnerClassesAttribute(mockClazz, mockInnerClassesAttribute);
        nameMarker.visitInnerClassesAttribute(mockClazz, mockInnerClassesAttribute);
        nameMarker.visitInnerClassesAttribute(mockClazz, mockInnerClassesAttribute);

        // Assert
        verify(mockInnerClassesAttribute, times(3)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute works with different InnerClassesAttribute instances.
     */
    @Test
    public void testVisitInnerClassesAttribute_withDifferentAttributes() {
        // Arrange
        InnerClassesAttribute attribute1 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attribute2 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attribute3 = mock(InnerClassesAttribute.class);

        // Act
        nameMarker.visitInnerClassesAttribute(mockClazz, attribute1);
        nameMarker.visitInnerClassesAttribute(mockClazz, attribute2);
        nameMarker.visitInnerClassesAttribute(mockClazz, attribute3);

        // Assert
        verify(attribute1, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
        verify(attribute2, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
        verify(attribute3, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute works with different Clazz instances.
     */
    @Test
    public void testVisitInnerClassesAttribute_withDifferentClazzes() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act
        nameMarker.visitInnerClassesAttribute(clazz1, mockInnerClassesAttribute);
        nameMarker.visitInnerClassesAttribute(clazz2, mockInnerClassesAttribute);
        nameMarker.visitInnerClassesAttribute(clazz3, mockInnerClassesAttribute);

        // Assert
        verify(mockInnerClassesAttribute, times(1)).innerClassEntriesAccept(eq(clazz1), eq(nameMarker));
        verify(mockInnerClassesAttribute, times(1)).innerClassEntriesAccept(eq(clazz2), eq(nameMarker));
        verify(mockInnerClassesAttribute, times(1)).innerClassEntriesAccept(eq(clazz3), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute passes the correct NameMarker instance
     * (itself) as the visitor to innerClassEntriesAccept.
     */
    @Test
    public void testVisitInnerClassesAttribute_passesCorrectVisitor() {
        // Arrange
        NameMarker anotherMarker = new NameMarker();

        InnerClassesAttribute attribute1 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attribute2 = mock(InnerClassesAttribute.class);

        // Act
        nameMarker.visitInnerClassesAttribute(mockClazz, attribute1);
        anotherMarker.visitInnerClassesAttribute(mockClazz, attribute2);

        // Assert
        // Each marker should pass itself as the visitor
        verify(attribute1, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
        verify(attribute2, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(anotherMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute handles an attribute with no inner class entries.
     * The method should still call innerClassEntriesAccept even if there are no entries.
     */
    @Test
    public void testVisitInnerClassesAttribute_withEmptyAttribute() {
        // Arrange
        InnerClassesAttribute emptyAttribute = mock(InnerClassesAttribute.class);
        // Mock the behavior to do nothing when innerClassEntriesAccept is called
        doNothing().when(emptyAttribute).innerClassEntriesAccept(any(Clazz.class), any());

        // Act
        nameMarker.visitInnerClassesAttribute(mockClazz, emptyAttribute);

        // Assert
        verify(emptyAttribute, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute properly delegates when the attribute
     * has multiple inner class entries by verifying the callback is invoked.
     */
    @Test
    public void testVisitInnerClassesAttribute_withMultipleInnerClassEntries() {
        // Arrange
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);

        // Simulate the attribute calling back to the visitor for each inner class entry
        doAnswer(invocation -> {
            Clazz clazz = invocation.getArgument(0);
            NameMarker visitor = invocation.getArgument(1);

            // Simulate calling visitInnerClassesInfo for multiple entries
            InnerClassesInfo info1 = mock(InnerClassesInfo.class);
            InnerClassesInfo info2 = mock(InnerClassesInfo.class);
            InnerClassesInfo info3 = mock(InnerClassesInfo.class);

            visitor.visitInnerClassesInfo(clazz, info1);
            visitor.visitInnerClassesInfo(clazz, info2);
            visitor.visitInnerClassesInfo(clazz, info3);

            return null;
        }).when(attribute).innerClassEntriesAccept(any(Clazz.class), any());

        // Act
        nameMarker.visitInnerClassesAttribute(mockClazz, attribute);

        // Assert
        verify(attribute, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
    }

    /**
     * Tests that multiple NameMarker instances can independently process
     * InnerClassesAttributes without interfering with each other.
     */
    @Test
    public void testVisitInnerClassesAttribute_multipleMarkersIndependent() {
        // Arrange
        NameMarker marker1 = new NameMarker();
        NameMarker marker2 = new NameMarker();

        InnerClassesAttribute attr1 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attr2 = mock(InnerClassesAttribute.class);
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);

        // Act
        marker1.visitInnerClassesAttribute(clazz1, attr1);
        marker2.visitInnerClassesAttribute(clazz2, attr2);

        // Assert
        verify(attr1, times(1)).innerClassEntriesAccept(eq(clazz1), eq(marker1));
        verify(attr2, times(1)).innerClassEntriesAccept(eq(clazz2), eq(marker2));
    }

    /**
     * Tests that visitInnerClassesAttribute correctly integrates with the NameMarker's
     * role as an InnerClassesInfoVisitor by verifying the visitor pattern works end-to-end.
     */
    @Test
    public void testVisitInnerClassesAttribute_visitorPatternIntegration() {
        // Arrange
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);
        Clazz clazz = mock(Clazz.class);

        // Use a flag to track if the visitor pattern was properly invoked
        final boolean[] visitorWasCalled = {false};

        doAnswer(invocation -> {
            visitorWasCalled[0] = true;
            return null;
        }).when(attribute).innerClassEntriesAccept(any(Clazz.class), any());

        // Act
        nameMarker.visitInnerClassesAttribute(clazz, attribute);

        // Assert
        assertTrue(visitorWasCalled[0], "The visitor pattern should have been invoked");
        verify(attribute, times(1)).innerClassEntriesAccept(eq(clazz), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute works correctly when called as part of
     * the AttributeVisitor interface implementation.
     */
    @Test
    public void testVisitInnerClassesAttribute_asAttributeVisitor() {
        // Arrange
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);

        // Act - Call through the AttributeVisitor interface
        proguard.classfile.attribute.visitor.AttributeVisitor visitor = nameMarker;
        visitor.visitInnerClassesAttribute(mockClazz, attribute);

        // Assert
        verify(attribute, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute maintains proper state when processing
     * multiple attributes in sequence.
     */
    @Test
    public void testVisitInnerClassesAttribute_sequentialProcessing() {
        // Arrange
        InnerClassesAttribute attr1 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attr2 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attr3 = mock(InnerClassesAttribute.class);

        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act - Process multiple attributes in sequence
        nameMarker.visitInnerClassesAttribute(clazz1, attr1);
        nameMarker.visitInnerClassesAttribute(clazz2, attr2);
        nameMarker.visitInnerClassesAttribute(clazz3, attr3);

        // Assert - Each should have been processed exactly once with correct parameters
        verify(attr1, times(1)).innerClassEntriesAccept(eq(clazz1), eq(nameMarker));
        verify(attr2, times(1)).innerClassEntriesAccept(eq(clazz2), eq(nameMarker));
        verify(attr3, times(1)).innerClassEntriesAccept(eq(clazz3), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute only interacts with the InnerClassesAttribute
     * and not directly with the Clazz parameter.
     */
    @Test
    public void testVisitInnerClassesAttribute_onlyInteractsWithAttribute() {
        // Arrange
        Clazz spyClazz = mock(Clazz.class);
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);

        // Act
        nameMarker.visitInnerClassesAttribute(spyClazz, attribute);

        // Assert
        // The method should only call innerClassEntriesAccept on the attribute
        verify(attribute, times(1)).innerClassEntriesAccept(eq(spyClazz), eq(nameMarker));
        // The clazz itself should not have any methods called on it directly
        verifyNoInteractions(spyClazz);
    }

    /**
     * Tests that visitInnerClassesAttribute can be called in rapid succession
     * without any issues or state interference.
     */
    @Test
    public void testVisitInnerClassesAttribute_rapidSequentialCalls() {
        // Arrange
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);

        // Act - Multiple rapid calls
        for (int i = 0; i < 100; i++) {
            nameMarker.visitInnerClassesAttribute(mockClazz, attribute);
        }

        // Assert
        verify(attribute, times(100)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute works correctly with attributes
     * from different classes in alternating sequence.
     */
    @Test
    public void testVisitInnerClassesAttribute_alternatingClasses() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        InnerClassesAttribute attr1 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attr2 = mock(InnerClassesAttribute.class);

        // Act - Alternate between different clazz/attribute pairs
        nameMarker.visitInnerClassesAttribute(clazz1, attr1);
        nameMarker.visitInnerClassesAttribute(clazz2, attr2);
        nameMarker.visitInnerClassesAttribute(clazz1, attr1);
        nameMarker.visitInnerClassesAttribute(clazz2, attr2);

        // Assert
        verify(attr1, times(2)).innerClassEntriesAccept(eq(clazz1), eq(nameMarker));
        verify(attr2, times(2)).innerClassEntriesAccept(eq(clazz2), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute delegates correctly when
     * the attribute's innerClassEntriesAccept triggers nested visitor calls.
     */
    @Test
    public void testVisitInnerClassesAttribute_withNestedVisitorCalls() {
        // Arrange
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);
        final int[] callbackCount = {0};

        doAnswer(invocation -> {
            callbackCount[0]++;
            Clazz clazz = invocation.getArgument(0);
            NameMarker visitor = invocation.getArgument(1);

            // Simulate nested behavior by creating inner class info
            InnerClassesInfo info = mock(InnerClassesInfo.class);
            visitor.visitInnerClassesInfo(clazz, info);

            return null;
        }).when(attribute).innerClassEntriesAccept(any(Clazz.class), any());

        // Act
        nameMarker.visitInnerClassesAttribute(mockClazz, attribute);

        // Assert
        assertEquals(1, callbackCount[0], "Callback should have been invoked once");
        verify(attribute, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute handles the case where
     * innerClassEntriesAccept is called but processes no entries.
     */
    @Test
    public void testVisitInnerClassesAttribute_withNoEntriesProcessed() {
        // Arrange
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);

        // Mock to do nothing (simulating empty inner classes)
        doNothing().when(attribute).innerClassEntriesAccept(any(Clazz.class), any());

        // Act
        nameMarker.visitInnerClassesAttribute(mockClazz, attribute);

        // Assert
        verify(attribute, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute works with both ProgramClass and LibraryClass.
     */
    @Test
    public void testVisitInnerClassesAttribute_withDifferentClazzTypes() {
        // Arrange
        Clazz programClazz = mock(ProgramClass.class);
        Clazz libraryClazz = mock(proguard.classfile.LibraryClass.class);
        InnerClassesAttribute attr1 = mock(InnerClassesAttribute.class);
        InnerClassesAttribute attr2 = mock(InnerClassesAttribute.class);

        // Act
        nameMarker.visitInnerClassesAttribute(programClazz, attr1);
        nameMarker.visitInnerClassesAttribute(libraryClazz, attr2);

        // Assert
        verify(attr1, times(1)).innerClassEntriesAccept(eq(programClazz), eq(nameMarker));
        verify(attr2, times(1)).innerClassEntriesAccept(eq(libraryClazz), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute passes exact object references
     * and doesn't create new instances.
     */
    @Test
    public void testVisitInnerClassesAttribute_passesExactReferences() {
        // Arrange
        Clazz clazz = mock(Clazz.class);
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);

        // Use argument captor to verify exact references
        final Object[] capturedArgs = new Object[2];

        doAnswer(invocation -> {
            capturedArgs[0] = invocation.getArgument(0);
            capturedArgs[1] = invocation.getArgument(1);
            return null;
        }).when(attribute).innerClassEntriesAccept(any(Clazz.class), any());

        // Act
        nameMarker.visitInnerClassesAttribute(clazz, attribute);

        // Assert
        assertSame(clazz, capturedArgs[0], "Should pass exact Clazz reference");
        assertSame(nameMarker, capturedArgs[1], "Should pass exact NameMarker reference");
    }

    /**
     * Tests that visitInnerClassesAttribute is idempotent - multiple calls
     * with the same parameters should behave consistently.
     */
    @Test
    public void testVisitInnerClassesAttribute_isIdempotent() {
        // Arrange
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);

        // Act - Call multiple times with same parameters
        nameMarker.visitInnerClassesAttribute(mockClazz, attribute);
        nameMarker.visitInnerClassesAttribute(mockClazz, attribute);
        nameMarker.visitInnerClassesAttribute(mockClazz, attribute);

        // Assert - Each call should trigger the same behavior
        verify(attribute, times(3)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute correctly implements the expected
     * behavior described in the method's purpose: ensuring outer class names are kept.
     */
    @Test
    public void testVisitInnerClassesAttribute_ensuresOuterClassNamesAreKept() {
        // Arrange
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);

        // The method should delegate to innerClassEntriesAccept which will then
        // call visitInnerClassesInfo for each entry, ultimately keeping outer class names
        final boolean[] delegated = {false};

        doAnswer(invocation -> {
            delegated[0] = true;
            return null;
        }).when(attribute).innerClassEntriesAccept(any(Clazz.class), any());

        // Act
        nameMarker.visitInnerClassesAttribute(mockClazz, attribute);

        // Assert
        assertTrue(delegated[0], "Should have delegated to innerClassEntriesAccept");
        verify(attribute, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
    }

    /**
     * Tests that visitInnerClassesAttribute maintains the correct visitor chain
     * by ensuring the NameMarker instance itself is passed as the visitor.
     */
    @Test
    public void testVisitInnerClassesAttribute_maintainsVisitorChain() {
        // Arrange
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);

        // Verify that the visitor parameter passed is the NameMarker instance
        doAnswer(invocation -> {
            Object visitor = invocation.getArgument(1);
            assertTrue(visitor instanceof NameMarker, "Visitor should be NameMarker instance");
            assertSame(nameMarker, visitor, "Visitor should be the same NameMarker instance");
            return null;
        }).when(attribute).innerClassEntriesAccept(any(Clazz.class), any());

        // Act
        nameMarker.visitInnerClassesAttribute(mockClazz, attribute);

        // Assert
        verify(attribute, times(1)).innerClassEntriesAccept(any(Clazz.class), any());
    }

    /**
     * Tests that visitInnerClassesAttribute completes successfully even
     * when the attribute has complex inner class hierarchies.
     */
    @Test
    public void testVisitInnerClassesAttribute_withComplexInnerClassHierarchy() {
        // Arrange
        InnerClassesAttribute attribute = mock(InnerClassesAttribute.class);

        // Simulate a complex inner class hierarchy
        doAnswer(invocation -> {
            Clazz clazz = invocation.getArgument(0);
            NameMarker visitor = invocation.getArgument(1);

            // Simulate multiple levels of inner classes
            for (int i = 0; i < 5; i++) {
                InnerClassesInfo info = mock(InnerClassesInfo.class);
                visitor.visitInnerClassesInfo(clazz, info);
            }

            return null;
        }).when(attribute).innerClassEntriesAccept(any(Clazz.class), any());

        // Act & Assert
        assertDoesNotThrow(() -> nameMarker.visitInnerClassesAttribute(mockClazz, attribute));
        verify(attribute, times(1)).innerClassEntriesAccept(eq(mockClazz), eq(nameMarker));
    }
}
