package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinValueParameterUsageMarker#visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)}.
 * Tests the visitKotlinDeclarationContainerMetadata method which calls functionsAccept and creates an AllPropertyVisitor.
 */
public class KotlinValueParameterUsageMarkerClaude_visitKotlinDeclarationContainerMetadataTest {

    private KotlinValueParameterUsageMarker marker;
    private Clazz mockClazz;
    private KotlinDeclarationContainerMetadata mockMetadata;

    @BeforeEach
    public void setUp() {
        marker = new KotlinValueParameterUsageMarker();
        mockClazz = mock(Clazz.class);
        mockMetadata = mock(KotlinDeclarationContainerMetadata.class);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata can be called without throwing exceptions.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        }, "visitKotlinDeclarationContainerMetadata should not throw an exception");
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls functionsAccept on the metadata.
     * This verifies that the method delegates to process functions.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsFunctionsAccept() {
        // Act
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata passes the marker itself to functionsAccept.
     * This verifies that the same marker instance is used for visitor callbacks.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_passesMarkerToFunctionsAccept() {
        // Act
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify that the marker instance is passed as the visitor
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls accept with an AllPropertyVisitor.
     * This verifies that properties are processed through AllPropertyVisitor.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsAcceptWithAllPropertyVisitor() {
        // Act
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify accept is called with a KotlinMetadataVisitor (AllPropertyVisitor is a KotlinMetadataVisitor)
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls functionsAccept before accept.
     * This verifies the correct order of operations.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsFunctionsAcceptBeforeAccept() {
        // Arrange
        org.mockito.InOrder inOrder = inOrder(mockMetadata);

        // Act
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify functionsAccept is called before accept
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata can be called multiple times.
     * This verifies that the method can be called repeatedly.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_canBeCalledMultipleTimes() {
        // Act
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify that each call triggers functionsAccept and accept
        verify(mockMetadata, times(3)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(3)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata works with different clazz instances.
     * This verifies that the method properly passes different clazz parameters.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);

        // Act
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz2, mockMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz2, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        verify(mockMetadata, times(1)).accept(eq(mockClazz2), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata works with different metadata instances.
     * This verifies that the method properly handles different metadata parameters.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withDifferentMetadata_callsCorrectMetadata() {
        // Arrange
        KotlinDeclarationContainerMetadata mockMetadata2 = mock(KotlinDeclarationContainerMetadata.class);

        // Act
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata2);

        // Assert - verify that each metadata instance's methods are called
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        verify(mockMetadata2, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with null Clazz delegates to metadata.
     * The behavior with null parameters depends on the metadata implementation.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withNullClazz_delegatesToMetadata() {
        // Act
        marker.visitKotlinDeclarationContainerMetadata(null, mockMetadata);

        // Assert - verify that both calls were made with null clazz
        verify(mockMetadata, times(1)).functionsAccept(eq(null), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(1)).accept(eq(null), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with null metadata throws NullPointerException.
     * This is expected since we cannot call methods on a null object.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withNullMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitKotlinDeclarationContainerMetadata(mockClazz, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withBothNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitKotlinDeclarationContainerMetadata(null, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that multiple instances of KotlinValueParameterUsageMarker behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinValueParameterUsageMarker marker1 = new KotlinValueParameterUsageMarker();
        KotlinValueParameterUsageMarker marker2 = new KotlinValueParameterUsageMarker();
        KotlinDeclarationContainerMetadata mockMetadata1 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata mockMetadata2 = mock(KotlinDeclarationContainerMetadata.class);

        // Act
        marker1.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata1);
        marker2.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata2);

        // Assert - both should make the same calls
        verify(mockMetadata1, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata1, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));

        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata does not interact with the Clazz parameter directly.
     * The clazz is only passed to functionsAccept and accept, not used directly.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_doesNotInteractWithClazz() {
        // Act
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify no direct interactions with clazz (it's only passed to functionsAccept and accept)
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests the complete workflow: functionsAccept then accept.
     * Verifies that both operations happen in sequence.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_completeWorkflow() {
        // Act
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify both operations occurred
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));

        // And verify the order
        org.mockito.InOrder inOrder = inOrder(mockMetadata);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata passes correct parameters through.
     * Verifies parameter integrity throughout the method execution.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_passesCorrectParameters() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);
        KotlinDeclarationContainerMetadata specificMetadata = mock(KotlinDeclarationContainerMetadata.class);

        // Act
        marker.visitKotlinDeclarationContainerMetadata(specificClazz, specificMetadata);

        // Assert - verify the exact parameters are passed
        verify(specificMetadata, times(1)).functionsAccept(specificClazz, marker);
        verify(specificMetadata, times(1)).accept(eq(specificClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with sequential calls
     * calls functionsAccept and accept for each invocation.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_sequentialCalls_callBothMethodsEachTime() {
        // Arrange
        KotlinDeclarationContainerMetadata metadata1 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata metadata2 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata metadata3 = mock(KotlinDeclarationContainerMetadata.class);

        // Act
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, metadata1);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, metadata2);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, metadata3);

        // Assert - both methods should be called for each metadata
        verify(metadata1, times(1)).functionsAccept(mockClazz, marker);
        verify(metadata1, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        verify(metadata2, times(1)).functionsAccept(mockClazz, marker);
        verify(metadata2, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        verify(metadata3, times(1)).functionsAccept(mockClazz, marker);
        verify(metadata3, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls both functionsAccept and accept exactly once.
     * Verifies that no extra calls are made.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsEachMethodExactlyOnce() {
        // Act
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify each method is called exactly once, not more
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata creates a new AllPropertyVisitor for each call.
     * This is important because AllPropertyVisitor wraps the marker, and a new instance should be created each time.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_createsNewAllPropertyVisitorEachTime() {
        // Act - call multiple times
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - accept should be called twice (once per invocation)
        verify(mockMetadata, times(2)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata properly handles the case
     * where functionsAccept might modify some state (though in this case it shouldn't affect us).
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_handlesFunctionsAcceptSideEffects() {
        // Arrange - set up mock to simulate some callback during functionsAccept
        doAnswer(invocation -> {
            // Simulate some processing that might happen
            return null;
        }).when(mockMetadata).functionsAccept(any(Clazz.class), any(KotlinFunctionVisitor.class));

        // Act & Assert - should still complete without issues
        assertDoesNotThrow(() -> {
            marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        });

        // Both methods should still be called
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata properly handles the case
     * where accept might modify some state.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_handlesAcceptSideEffects() {
        // Arrange - set up mock to simulate some callback during accept
        doAnswer(invocation -> {
            // Simulate some processing that might happen
            return null;
        }).when(mockMetadata).accept(any(Clazz.class), any(KotlinMetadataVisitor.class));

        // Act & Assert - should still complete without issues
        assertDoesNotThrow(() -> {
            marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        });

        // Both methods should still be called
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that the method can be called via the interface reference.
     * Verifies polymorphic behavior works correctly.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_viaInterfaceReference() {
        // Arrange
        proguard.classfile.kotlin.visitor.KotlinMetadataVisitor visitor = marker;

        // Act & Assert - should work when called via interface reference
        assertDoesNotThrow(() -> {
            visitor.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        }, "Should work when called via interface reference");

        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata returns void as expected.
     * Verifies the method signature and behavior.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_returnsVoid() {
        // Act - method returns void, so just verify it executes
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - if we reach here without exception, the method completed successfully
        assertTrue(true, "Method should complete and return void");
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls only the expected methods on metadata.
     * Verifies that no unexpected method calls are made.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsOnlyExpectedMethods() {
        // Act
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify only functionsAccept and accept are called
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        verifyNoMoreInteractions(mockMetadata);
    }

    /**
     * Tests that the marker instance passed to functionsAccept is the same one.
     * Verifies object identity is preserved.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_passesCorrectMarkerInstance() {
        // Arrange
        KotlinValueParameterUsageMarker specificMarker = new KotlinValueParameterUsageMarker();

        // Act
        specificMarker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify the specific marker instance is passed
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, specificMarker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata can handle being called
     * immediately after construction.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_immediatelyAfterConstruction() {
        // Arrange
        KotlinValueParameterUsageMarker newMarker = new KotlinValueParameterUsageMarker();

        // Act & Assert - should work immediately after construction
        assertDoesNotThrow(() -> {
            newMarker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        }, "Should be callable immediately after construction");

        verify(mockMetadata, times(1)).functionsAccept(mockClazz, newMarker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata maintains correct behavior
     * with interleaved calls on different markers.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_interleavedCallsOnDifferentMarkers() {
        // Arrange
        KotlinValueParameterUsageMarker marker1 = new KotlinValueParameterUsageMarker();
        KotlinValueParameterUsageMarker marker2 = new KotlinValueParameterUsageMarker();
        KotlinDeclarationContainerMetadata metadata1 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata metadata2 = mock(KotlinDeclarationContainerMetadata.class);

        // Act - interleave calls
        marker1.visitKotlinDeclarationContainerMetadata(mockClazz, metadata1);
        marker2.visitKotlinDeclarationContainerMetadata(mockClazz, metadata2);
        marker1.visitKotlinDeclarationContainerMetadata(mockClazz, metadata1);

        // Assert - verify each call is independent
        verify(metadata1, times(2)).functionsAccept(mockClazz, marker1);
        verify(metadata1, times(2)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        verify(metadata2, times(1)).functionsAccept(mockClazz, marker2);
        verify(metadata2, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata behavior is consistent
     * regardless of how many times it's called.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_consistentBehaviorAcrossCalls() {
        // Act - call multiple times
        for (int i = 0; i < 5; i++) {
            marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        }

        // Assert - verify consistent behavior (5 calls each)
        verify(mockMetadata, times(5)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(5)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata properly delegates to both
     * functionsAccept and accept even when called in rapid succession.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_rapidSuccessiveCalls() {
        // Act - call rapidly 100 times
        for (int i = 0; i < 100; i++) {
            marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        }

        // Assert - verify all calls were made
        verify(mockMetadata, times(100)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(100)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }
}
