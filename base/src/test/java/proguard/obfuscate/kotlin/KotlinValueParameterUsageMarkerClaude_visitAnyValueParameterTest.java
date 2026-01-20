package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinValueParameterMetadata;
import proguard.util.Processable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinValueParameterUsageMarker#visitAnyValueParameter(Clazz, KotlinValueParameterMetadata)}.
 * Tests the visitAnyValueParameter method which marks value parameters as used
 * by setting their processing info.
 */
public class KotlinValueParameterUsageMarkerClaude_visitAnyValueParameterTest {

    private KotlinValueParameterUsageMarker marker;
    private Clazz mockClazz;
    private KotlinValueParameterMetadata mockValueParameterMetadata;

    @BeforeEach
    public void setUp() {
        marker = new KotlinValueParameterUsageMarker();
        mockClazz = mock(Clazz.class);
        mockValueParameterMetadata = mock(KotlinValueParameterMetadata.class);
    }

    /**
     * Tests that visitAnyValueParameter can be called without throwing exceptions.
     */
    @Test
    public void testVisitAnyValueParameter_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            marker.visitAnyValueParameter(mockClazz, mockValueParameterMetadata);
        }, "visitAnyValueParameter should not throw an exception");
    }

    /**
     * Tests that visitAnyValueParameter calls setProcessingInfo on the value parameter metadata.
     * This verifies that the method marks the parameter as used.
     */
    @Test
    public void testVisitAnyValueParameter_callsSetProcessingInfo() {
        // Act
        marker.visitAnyValueParameter(mockClazz, mockValueParameterMetadata);

        // Assert - verify that setProcessingInfo is called
        verify(mockValueParameterMetadata, times(1)).setProcessingInfo(any());
    }

    /**
     * Tests that visitAnyValueParameter marks the parameter as used
     * by verifying through the isUsed static method.
     */
    @Test
    public void testVisitAnyValueParameter_marksParameterAsUsed() {
        // Arrange - use a real Processable mock that stores the processing info
        Processable processableMock = mock(Processable.class, RETURNS_DEEP_STUBS);
        KotlinValueParameterMetadata valueParameter = mock(KotlinValueParameterMetadata.class);

        // Set up to capture the processing info
        final Object[] capturedInfo = {null};
        doAnswer(invocation -> {
            capturedInfo[0] = invocation.getArgument(0);
            return null;
        }).when(valueParameter).setProcessingInfo(any());

        when(valueParameter.getProcessingInfo()).thenAnswer(invocation -> capturedInfo[0]);

        // Act
        marker.visitAnyValueParameter(mockClazz, valueParameter);

        // Assert - verify parameter is marked as used
        assertTrue(KotlinValueParameterUsageMarker.isUsed(valueParameter),
                   "Value parameter should be marked as used");
    }

    /**
     * Tests that visitAnyValueParameter does not interact with the Clazz parameter.
     */
    @Test
    public void testVisitAnyValueParameter_doesNotInteractWithClazz() {
        // Act
        marker.visitAnyValueParameter(mockClazz, mockValueParameterMetadata);

        // Assert - verify no interactions with clazz
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyValueParameter can be called with null Clazz.
     * The method should not use the Clazz parameter.
     */
    @Test
    public void testVisitAnyValueParameter_withNullClazz_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            marker.visitAnyValueParameter(null, mockValueParameterMetadata);
        }, "Should not throw with null Clazz");

        // Verify the parameter is still marked
        verify(mockValueParameterMetadata, times(1)).setProcessingInfo(any());
    }

    /**
     * Tests that visitAnyValueParameter with null metadata throws NullPointerException.
     */
    @Test
    public void testVisitAnyValueParameter_withNullMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitAnyValueParameter(mockClazz, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitAnyValueParameter can be called multiple times on the same parameter.
     * Each call should set the processing info again.
     */
    @Test
    public void testVisitAnyValueParameter_canBeCalledMultipleTimes() {
        // Act
        marker.visitAnyValueParameter(mockClazz, mockValueParameterMetadata);
        marker.visitAnyValueParameter(mockClazz, mockValueParameterMetadata);
        marker.visitAnyValueParameter(mockClazz, mockValueParameterMetadata);

        // Assert - setProcessingInfo should be called 3 times
        verify(mockValueParameterMetadata, times(3)).setProcessingInfo(any());
    }

    /**
     * Tests that visitAnyValueParameter works with different value parameter instances.
     */
    @Test
    public void testVisitAnyValueParameter_withDifferentParameters_marksEachOne() {
        // Arrange
        KotlinValueParameterMetadata param1 = mock(KotlinValueParameterMetadata.class);
        KotlinValueParameterMetadata param2 = mock(KotlinValueParameterMetadata.class);
        KotlinValueParameterMetadata param3 = mock(KotlinValueParameterMetadata.class);

        // Act
        marker.visitAnyValueParameter(mockClazz, param1);
        marker.visitAnyValueParameter(mockClazz, param2);
        marker.visitAnyValueParameter(mockClazz, param3);

        // Assert - each parameter should have setProcessingInfo called
        verify(param1, times(1)).setProcessingInfo(any());
        verify(param2, times(1)).setProcessingInfo(any());
        verify(param3, times(1)).setProcessingInfo(any());
    }

    /**
     * Tests that visitAnyValueParameter works correctly via interface reference.
     */
    @Test
    public void testVisitAnyValueParameter_asPartOfVisitorPattern() {
        // Arrange
        proguard.classfile.kotlin.visitor.KotlinValueParameterVisitor visitor = marker;

        // Act
        assertDoesNotThrow(() -> {
            visitor.visitAnyValueParameter(mockClazz, mockValueParameterMetadata);
        }, "Should work when used via interface reference");

        // Assert
        verify(mockValueParameterMetadata, times(1)).setProcessingInfo(any());
    }

    /**
     * Tests that visitAnyValueParameter can handle being called immediately after construction.
     */
    @Test
    public void testVisitAnyValueParameter_immediatelyAfterConstruction() {
        // Arrange
        KotlinValueParameterUsageMarker newMarker = new KotlinValueParameterUsageMarker();

        // Act & Assert
        assertDoesNotThrow(() -> {
            newMarker.visitAnyValueParameter(mockClazz, mockValueParameterMetadata);
        }, "Should be callable immediately after construction");

        verify(mockValueParameterMetadata, times(1)).setProcessingInfo(any());
    }

    /**
     * Tests that multiple marker instances mark parameters consistently.
     */
    @Test
    public void testVisitAnyValueParameter_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinValueParameterUsageMarker marker1 = new KotlinValueParameterUsageMarker();
        KotlinValueParameterUsageMarker marker2 = new KotlinValueParameterUsageMarker();
        KotlinValueParameterMetadata param1 = mock(KotlinValueParameterMetadata.class);
        KotlinValueParameterMetadata param2 = mock(KotlinValueParameterMetadata.class);

        // Act
        marker1.visitAnyValueParameter(mockClazz, param1);
        marker2.visitAnyValueParameter(mockClazz, param2);

        // Assert - both should mark their respective parameters
        verify(param1, times(1)).setProcessingInfo(any());
        verify(param2, times(1)).setProcessingInfo(any());
    }

    /**
     * Tests that visitAnyValueParameter returns void as expected.
     */
    @Test
    public void testVisitAnyValueParameter_returnsVoid() {
        // Act - method returns void, so just verify it executes
        marker.visitAnyValueParameter(mockClazz, mockValueParameterMetadata);

        // Assert - if we reach here without exception, the method completed successfully
        assertTrue(true, "Method should complete and return void");
    }

    /**
     * Tests that visitAnyValueParameter with different Clazz instances
     * still marks the same parameter correctly.
     */
    @Test
    public void testVisitAnyValueParameter_withDifferentClazz_stillMarksParameter() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act
        marker.visitAnyValueParameter(clazz1, mockValueParameterMetadata);
        marker.visitAnyValueParameter(clazz2, mockValueParameterMetadata);
        marker.visitAnyValueParameter(clazz3, mockValueParameterMetadata);

        // Assert - parameter should be marked 3 times (once per call)
        verify(mockValueParameterMetadata, times(3)).setProcessingInfo(any());
        // And Clazz instances should not be interacted with
        verifyNoInteractions(clazz1, clazz2, clazz3);
    }

    /**
     * Tests that visitAnyValueParameter only interacts with the value parameter metadata.
     */
    @Test
    public void testVisitAnyValueParameter_onlyInteractsWithMetadata() {
        // Act
        marker.visitAnyValueParameter(mockClazz, mockValueParameterMetadata);

        // Assert - only the metadata should have interactions
        verify(mockValueParameterMetadata, times(1)).setProcessingInfo(any());
        verifyNoMoreInteractions(mockValueParameterMetadata);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyValueParameter properly implements the KotlinValueParameterVisitor interface contract.
     */
    @Test
    public void testVisitAnyValueParameter_implementsVisitorContract() {
        // Act
        marker.visitAnyValueParameter(mockClazz, mockValueParameterMetadata);

        // Assert - verify that the visitor methods are properly called
        verify(mockValueParameterMetadata, times(1)).setProcessingInfo(any());
    }

    /**
     * Tests that the isUsed method correctly identifies marked parameters.
     * This test verifies the complete marking and checking workflow.
     */
    @Test
    public void testVisitAnyValueParameter_isUsedReturnsTrue_afterMarking() {
        // Arrange - use a mock that properly stores and returns processing info
        KotlinValueParameterMetadata valueParameter = mock(KotlinValueParameterMetadata.class);
        final Object[] capturedInfo = {null};

        doAnswer(invocation -> {
            capturedInfo[0] = invocation.getArgument(0);
            return null;
        }).when(valueParameter).setProcessingInfo(any());

        when(valueParameter.getProcessingInfo()).thenAnswer(invocation -> capturedInfo[0]);

        // Verify not used before
        assertFalse(KotlinValueParameterUsageMarker.isUsed(valueParameter),
                    "Parameter should not be marked as used initially");

        // Act
        marker.visitAnyValueParameter(mockClazz, valueParameter);

        // Assert - parameter should now be marked as used
        assertTrue(KotlinValueParameterUsageMarker.isUsed(valueParameter),
                   "Parameter should be marked as used after visiting");
    }

    /**
     * Tests that visitAnyValueParameter handles rapid successive calls correctly.
     */
    @Test
    public void testVisitAnyValueParameter_rapidSuccessiveCalls() {
        // Act - call 100 times
        for (int i = 0; i < 100; i++) {
            marker.visitAnyValueParameter(mockClazz, mockValueParameterMetadata);
        }

        // Assert - setProcessingInfo should be called 100 times
        verify(mockValueParameterMetadata, times(100)).setProcessingInfo(any());
    }

    /**
     * Tests that visitAnyValueParameter marks parameters independently.
     * Each parameter should be marked separately.
     */
    @Test
    public void testVisitAnyValueParameter_marksParametersIndependently() {
        // Arrange
        KotlinValueParameterMetadata param1 = mock(KotlinValueParameterMetadata.class);
        KotlinValueParameterMetadata param2 = mock(KotlinValueParameterMetadata.class);

        final Object[] capturedInfo1 = {null};
        final Object[] capturedInfo2 = {null};

        doAnswer(invocation -> {
            capturedInfo1[0] = invocation.getArgument(0);
            return null;
        }).when(param1).setProcessingInfo(any());

        doAnswer(invocation -> {
            capturedInfo2[0] = invocation.getArgument(0);
            return null;
        }).when(param2).setProcessingInfo(any());

        when(param1.getProcessingInfo()).thenAnswer(invocation -> capturedInfo1[0]);
        when(param2.getProcessingInfo()).thenAnswer(invocation -> capturedInfo2[0]);

        // Act - mark only param1
        marker.visitAnyValueParameter(mockClazz, param1);

        // Assert - param1 should be marked, param2 should not
        assertTrue(KotlinValueParameterUsageMarker.isUsed(param1),
                   "param1 should be marked as used");
        assertFalse(KotlinValueParameterUsageMarker.isUsed(param2),
                    "param2 should not be marked as used");

        // Act - now mark param2
        marker.visitAnyValueParameter(mockClazz, param2);

        // Assert - both should be marked now
        assertTrue(KotlinValueParameterUsageMarker.isUsed(param1),
                   "param1 should still be marked as used");
        assertTrue(KotlinValueParameterUsageMarker.isUsed(param2),
                   "param2 should now be marked as used");
    }

    /**
     * Tests that visitAnyValueParameter does not modify the Clazz parameter in any way.
     */
    @Test
    public void testVisitAnyValueParameter_doesNotModifyClazz() {
        // Arrange
        Clazz spyClazz = spy(Clazz.class);

        // Act
        marker.visitAnyValueParameter(spyClazz, mockValueParameterMetadata);

        // Assert - verify no methods were called on the clazz
        verifyNoInteractions(spyClazz);
    }

    /**
     * Tests that visitAnyValueParameter passes the correct object to setProcessingInfo.
     * The processing info should be a non-null marker object.
     */
    @Test
    public void testVisitAnyValueParameter_passesNonNullProcessingInfo() {
        // Arrange
        final Object[] capturedInfo = {null};
        doAnswer(invocation -> {
            capturedInfo[0] = invocation.getArgument(0);
            return null;
        }).when(mockValueParameterMetadata).setProcessingInfo(any());

        // Act
        marker.visitAnyValueParameter(mockClazz, mockValueParameterMetadata);

        // Assert - processing info should not be null
        assertNotNull(capturedInfo[0], "Processing info passed to setProcessingInfo should not be null");
    }

    /**
     * Tests that visitAnyValueParameter sets the same marker object for all parameters.
     * This verifies consistency in the marking mechanism.
     */
    @Test
    public void testVisitAnyValueParameter_usesSameMarkerObject_forAllParameters() {
        // Arrange
        KotlinValueParameterMetadata param1 = mock(KotlinValueParameterMetadata.class);
        KotlinValueParameterMetadata param2 = mock(KotlinValueParameterMetadata.class);

        final Object[] capturedInfo1 = {null};
        final Object[] capturedInfo2 = {null};

        doAnswer(invocation -> {
            capturedInfo1[0] = invocation.getArgument(0);
            return null;
        }).when(param1).setProcessingInfo(any());

        doAnswer(invocation -> {
            capturedInfo2[0] = invocation.getArgument(0);
            return null;
        }).when(param2).setProcessingInfo(any());

        // Act
        marker.visitAnyValueParameter(mockClazz, param1);
        marker.visitAnyValueParameter(mockClazz, param2);

        // Assert - both should receive the same marker object
        assertNotNull(capturedInfo1[0], "First parameter should receive processing info");
        assertNotNull(capturedInfo2[0], "Second parameter should receive processing info");
        assertSame(capturedInfo1[0], capturedInfo2[0],
                   "Both parameters should receive the same marker object");
    }

    /**
     * Tests that visitAnyValueParameter works correctly when called as part of a visitor chain.
     * This simulates real-world usage where the visitor is passed to accept methods.
     */
    @Test
    public void testVisitAnyValueParameter_asPartOfVisitorChain() {
        // Arrange
        proguard.classfile.kotlin.visitor.KotlinValueParameterVisitor visitor = marker;
        KotlinValueParameterMetadata[] parameters = {
            mock(KotlinValueParameterMetadata.class),
            mock(KotlinValueParameterMetadata.class),
            mock(KotlinValueParameterMetadata.class)
        };

        // Act - simulate a visitor chain calling visitAnyValueParameter
        for (KotlinValueParameterMetadata param : parameters) {
            visitor.visitAnyValueParameter(mockClazz, param);
        }

        // Assert - all parameters should be marked
        for (KotlinValueParameterMetadata param : parameters) {
            verify(param, times(1)).setProcessingInfo(any());
        }
    }

    /**
     * Tests that visitAnyValueParameter does not throw when both Clazz is null
     * and metadata is valid (only metadata is actually used).
     */
    @Test
    public void testVisitAnyValueParameter_withNullClazz_andValidMetadata_succeeds() {
        // Arrange
        KotlinValueParameterMetadata validMetadata = mock(KotlinValueParameterMetadata.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marker.visitAnyValueParameter(null, validMetadata);
        }, "Should not throw with null Clazz and valid metadata");

        verify(validMetadata, times(1)).setProcessingInfo(any());
    }
}
