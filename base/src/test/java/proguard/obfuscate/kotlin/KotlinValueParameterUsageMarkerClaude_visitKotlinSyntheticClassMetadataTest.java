package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinValueParameterUsageMarker#visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)}.
 * Tests the visitKotlinSyntheticClassMetadata method which delegates to functionsAccept.
 */
public class KotlinValueParameterUsageMarkerClaude_visitKotlinSyntheticClassMetadataTest {

    private KotlinValueParameterUsageMarker marker;
    private Clazz mockClazz;
    private KotlinSyntheticClassKindMetadata mockMetadata;

    @BeforeEach
    public void setUp() {
        marker = new KotlinValueParameterUsageMarker();
        mockClazz = mock(Clazz.class);
        mockMetadata = mock(KotlinSyntheticClassKindMetadata.class);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata can be called without throwing exceptions.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "visitKotlinSyntheticClassMetadata should not throw an exception");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata calls functionsAccept on the metadata.
     * This verifies that the method delegates to process functions.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_callsFunctionsAccept() {
        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata passes the marker itself as the visitor.
     * This verifies that the same marker instance is used for the visitor callback.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_passesMarkerAsVisitor() {
        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that the marker instance is passed as the visitor
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata can be called multiple times.
     * This verifies that the method can be called repeatedly.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_canBeCalledMultipleTimes() {
        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that each call triggers functionsAccept
        verify(mockMetadata, times(3)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata works with different clazz instances.
     * This verifies that the method properly passes different clazz parameters.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);

        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        marker.visitKotlinSyntheticClassMetadata(mockClazz2, mockMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz2, marker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata works with different metadata instances.
     * This verifies that the method properly handles different metadata parameters.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withDifferentMetadata_callsCorrectMetadata() {
        // Arrange
        KotlinSyntheticClassKindMetadata mockMetadata2 = mock(KotlinSyntheticClassKindMetadata.class);

        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata2);

        // Assert - verify that each metadata instance's methods are called
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with null Clazz does not throw but delegates.
     * The behavior with null parameters depends on the metadata implementation.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withNullClazz_delegatesToMetadata() {
        // Act & Assert - should delegate to metadata (may or may not throw depending on metadata implementation)
        marker.visitKotlinSyntheticClassMetadata(null, mockMetadata);

        // Verify that functionsAccept was called with null clazz
        verify(mockMetadata, times(1)).functionsAccept(eq(null), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with null metadata throws NullPointerException.
     * This is expected since we cannot call methods on a null object.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withNullMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitKotlinSyntheticClassMetadata(mockClazz, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withBothNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitKotlinSyntheticClassMetadata(null, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that multiple instances of KotlinValueParameterUsageMarker behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinValueParameterUsageMarker marker1 = new KotlinValueParameterUsageMarker();
        KotlinValueParameterUsageMarker marker2 = new KotlinValueParameterUsageMarker();
        KotlinSyntheticClassKindMetadata mockMetadata1 = mock(KotlinSyntheticClassKindMetadata.class);
        KotlinSyntheticClassKindMetadata mockMetadata2 = mock(KotlinSyntheticClassKindMetadata.class);

        // Act
        marker1.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata1);
        marker2.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata2);

        // Assert - both should make the same calls
        verify(mockMetadata1, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata does not interact with the Clazz parameter directly.
     * The clazz is only passed to functionsAccept, not used directly.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_doesNotInteractWithClazz() {
        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify no direct interactions with clazz (it's only passed to functionsAccept)
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata only calls the expected functionsAccept method.
     * Verifies that only this method is called on the metadata, not other methods.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_onlyCallsExpectedMethods() {
        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify only functionsAccept is called
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verifyNoMoreInteractions(mockMetadata);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata passes correct parameters through.
     * Verifies parameter integrity throughout the method execution.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_passesCorrectParameters() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);
        KotlinSyntheticClassKindMetadata specificMetadata = mock(KotlinSyntheticClassKindMetadata.class);

        // Act
        marker.visitKotlinSyntheticClassMetadata(specificClazz, specificMetadata);

        // Assert - verify the exact parameters are passed
        verify(specificMetadata, times(1)).functionsAccept(specificClazz, marker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with sequential calls processes each metadata correctly.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_sequentialCalls_processEachMetadata() {
        // Arrange
        KotlinSyntheticClassKindMetadata metadata1 = mock(KotlinSyntheticClassKindMetadata.class);
        KotlinSyntheticClassKindMetadata metadata2 = mock(KotlinSyntheticClassKindMetadata.class);
        KotlinSyntheticClassKindMetadata metadata3 = mock(KotlinSyntheticClassKindMetadata.class);

        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, metadata1);
        marker.visitKotlinSyntheticClassMetadata(mockClazz, metadata2);
        marker.visitKotlinSyntheticClassMetadata(mockClazz, metadata3);

        // Assert - functionsAccept should be called for each
        verify(metadata1, times(1)).functionsAccept(mockClazz, marker);
        verify(metadata2, times(1)).functionsAccept(mockClazz, marker);
        verify(metadata3, times(1)).functionsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata passes the same marker instance
     * to functionsAccept.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_passesSameMarkerInstance() {
        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify the same marker instance is passed
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);

        // Verify that the exact same visitor instance is passed
        verify(mockMetadata).functionsAccept(eq(mockClazz), same(marker));
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata maintains consistent behavior
     * when called with the same parameters multiple times.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_consistentWithSameParameters() {
        // Act - call multiple times with same parameters
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - should call the method twice (once per invocation)
        verify(mockMetadata, times(2)).functionsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata can handle being called
     * immediately after construction of the marker.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_immediatelyAfterConstruction() {
        // Arrange
        KotlinValueParameterUsageMarker newMarker = new KotlinValueParameterUsageMarker();

        // Act & Assert
        assertDoesNotThrow(() -> {
            newMarker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "Should be callable immediately after construction");

        verify(mockMetadata, times(1)).functionsAccept(mockClazz, newMarker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata does not modify the marker's state
     * in a way that affects subsequent calls.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_doesNotAffectSubsequentCalls() {
        // Arrange
        KotlinSyntheticClassKindMetadata metadata1 = mock(KotlinSyntheticClassKindMetadata.class);
        KotlinSyntheticClassKindMetadata metadata2 = mock(KotlinSyntheticClassKindMetadata.class);

        // Act - call twice with different metadata
        marker.visitKotlinSyntheticClassMetadata(mockClazz, metadata1);
        marker.visitKotlinSyntheticClassMetadata(mockClazz, metadata2);

        // Assert - both calls should behave independently
        verify(metadata1, times(1)).functionsAccept(mockClazz, marker);
        verify(metadata2, times(1)).functionsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata calls functionsAccept
     * even if it is a no-op in the mock.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_callsFunctionsAcceptRegardlessOfBehavior() {
        // Arrange - configure the method to do nothing (default mock behavior)
        doNothing().when(mockMetadata).functionsAccept(any(), any());

        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - functionsAccept should still be called
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata correctly integrates with the visitor pattern.
     * Verifies that it works when the marker is used as a KotlinMetadataVisitor.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_asPartOfVisitorPattern() {
        // Arrange
        proguard.classfile.kotlin.visitor.KotlinMetadataVisitor visitor = marker;

        // Act
        assertDoesNotThrow(() -> {
            visitor.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "Should work when used via interface reference");

        // Assert
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata passes the clazz parameter
     * unchanged to functionsAccept.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_passesSameClazz() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);

        // Act
        marker.visitKotlinSyntheticClassMetadata(specificClazz, mockMetadata);

        // Assert - functionsAccept should receive the exact same clazz instance
        verify(mockMetadata).functionsAccept(same(specificClazz), any());
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata purely delegates
     * to the metadata object without performing additional operations.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_purelyDelegates() {
        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - only the delegation call should occur, nothing else
        verify(mockMetadata).functionsAccept(mockClazz, marker);
        verifyNoMoreInteractions(mockMetadata);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata handles rapid successive calls.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_rapidSuccessiveCalls() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
            }
        }, "Should handle rapid successive calls");

        // Verify that functionsAccept was called 100 times
        verify(mockMetadata, times(100)).functionsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with different clazz instances
     * in rapid succession works correctly.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_rapidCallsWithDifferentClazz() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act
        marker.visitKotlinSyntheticClassMetadata(clazz1, mockMetadata);
        marker.visitKotlinSyntheticClassMetadata(clazz2, mockMetadata);
        marker.visitKotlinSyntheticClassMetadata(clazz3, mockMetadata);

        // Assert - each clazz should be passed to functionsAccept
        verify(mockMetadata, times(1)).functionsAccept(clazz1, marker);
        verify(mockMetadata, times(1)).functionsAccept(clazz2, marker);
        verify(mockMetadata, times(1)).functionsAccept(clazz3, marker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata correctly implements
     * the KotlinMetadataVisitor interface contract.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_implementsVisitorContract() {
        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that the visitor is properly passed to accept method
        verify(mockMetadata, times(1)).functionsAccept(any(Clazz.class), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata returns void as expected.
     * Verifies the method signature and behavior.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_returnsVoid() {
        // Act - method returns void, so just verify it executes
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - if we reach here without exception, the method completed successfully
        assertTrue(true, "Method should complete and return void");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata handles side effects
     * from functionsAccept gracefully.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_handlesFunctionsAcceptSideEffects() {
        // Arrange - set up mock to simulate some callback during functionsAccept
        doAnswer(invocation -> {
            // Simulate some processing that might happen
            return null;
        }).when(mockMetadata).functionsAccept(any(Clazz.class), any(KotlinFunctionVisitor.class));

        // Act & Assert - should still complete without issues
        assertDoesNotThrow(() -> {
            marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        });

        // functionsAccept should still be called
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with interleaved calls
     * maintains proper state for each marker instance.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_interleavedCallsMaintainState() {
        // Arrange
        KotlinValueParameterUsageMarker marker1 = new KotlinValueParameterUsageMarker();
        KotlinValueParameterUsageMarker marker2 = new KotlinValueParameterUsageMarker();
        KotlinSyntheticClassKindMetadata metadata1 = mock(KotlinSyntheticClassKindMetadata.class);
        KotlinSyntheticClassKindMetadata metadata2 = mock(KotlinSyntheticClassKindMetadata.class);

        // Act - interleave calls
        marker1.visitKotlinSyntheticClassMetadata(mockClazz, metadata1);
        marker2.visitKotlinSyntheticClassMetadata(mockClazz, metadata2);
        marker1.visitKotlinSyntheticClassMetadata(mockClazz, metadata1);

        // Assert - verify each call is independent
        verify(metadata1, times(2)).functionsAccept(mockClazz, marker1);
        verify(metadata2, times(1)).functionsAccept(mockClazz, marker2);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata processes synthetic class metadata
     * by delegating to functionsAccept (synthetic classes typically contain lambda functions).
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_processesSyntheticClassFunctions() {
        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - synthetic class functions should be processed
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata does not call other metadata methods.
     * Synthetic classes don't have constructors or properties like regular classes.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_doesNotCallConstructorsOrProperties() {
        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - only functionsAccept should be called, no constructors or properties
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verifyNoMoreInteractions(mockMetadata);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata calls functionsAccept exactly once.
     * Verifies that no extra calls are made.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_callsFunctionsAcceptExactlyOnce() {
        // Act
        marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify the method is called exactly once, not more
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata behavior is consistent
     * regardless of how many times it's called.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_consistentBehaviorAcrossCalls() {
        // Act - call multiple times
        for (int i = 0; i < 5; i++) {
            marker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }

        // Assert - verify consistent behavior (5 calls)
        verify(mockMetadata, times(5)).functionsAccept(mockClazz, marker);
    }
}
