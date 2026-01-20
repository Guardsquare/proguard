package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinValueParameterNameShrinker#visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)}.
 * Tests the visitKotlinSyntheticClassMetadata method which delegates to functionsAccept.
 */
public class KotlinValueParameterNameShrinkerClaude_visitKotlinSyntheticClassMetadataTest {

    private KotlinValueParameterNameShrinker shrinker;
    private Clazz mockClazz;
    private KotlinSyntheticClassKindMetadata mockMetadata;

    @BeforeEach
    public void setUp() {
        shrinker = new KotlinValueParameterNameShrinker();
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
            shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "visitKotlinSyntheticClassMetadata should not throw an exception");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata calls functionsAccept on the metadata.
     * This verifies that the method delegates to process functions.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_callsFunctionsAccept() {
        // Act
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata passes the shrinker itself as the visitor.
     * This verifies that the same shrinker instance is used for the visitor callback.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_passesShrinkerAsVisitor() {
        // Act
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that the shrinker instance is passed as the visitor
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata can be called multiple times.
     * This verifies that the method can be called repeatedly.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_canBeCalledMultipleTimes() {
        // Act
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

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
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz2, mockMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz2, shrinker);
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
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata2);

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
        shrinker.visitKotlinSyntheticClassMetadata(null, mockMetadata);

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
            shrinker.visitKotlinSyntheticClassMetadata(mockClazz, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withBothNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            shrinker.visitKotlinSyntheticClassMetadata(null, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that multiple instances of KotlinValueParameterNameShrinker behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinValueParameterNameShrinker shrinker1 = new KotlinValueParameterNameShrinker();
        KotlinValueParameterNameShrinker shrinker2 = new KotlinValueParameterNameShrinker();
        KotlinSyntheticClassKindMetadata mockMetadata1 = mock(KotlinSyntheticClassKindMetadata.class);
        KotlinSyntheticClassKindMetadata mockMetadata2 = mock(KotlinSyntheticClassKindMetadata.class);

        // Act
        shrinker1.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata1);
        shrinker2.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata2);

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
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

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
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify only functionsAccept is called
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
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
        shrinker.visitKotlinSyntheticClassMetadata(specificClazz, specificMetadata);

        // Assert - verify the exact parameters are passed
        verify(specificMetadata, times(1)).functionsAccept(specificClazz, shrinker);
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
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, metadata1);
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, metadata2);
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, metadata3);

        // Assert - functionsAccept should be called for each
        verify(metadata1, times(1)).functionsAccept(mockClazz, shrinker);
        verify(metadata2, times(1)).functionsAccept(mockClazz, shrinker);
        verify(metadata3, times(1)).functionsAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata passes the same shrinker instance
     * to functionsAccept.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_passesSameShrinkerInstance() {
        // Act
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify the same shrinker instance is passed
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);

        // Verify that the exact same visitor instance is passed
        verify(mockMetadata).functionsAccept(eq(mockClazz), same(shrinker));
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata maintains consistent behavior
     * when called with the same parameters multiple times.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_consistentWithSameParameters() {
        // Act - call multiple times with same parameters
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - should call the method twice (once per invocation)
        verify(mockMetadata, times(2)).functionsAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata can handle being called
     * immediately after construction of the shrinker.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_immediatelyAfterConstruction() {
        // Arrange
        KotlinValueParameterNameShrinker newShrinker = new KotlinValueParameterNameShrinker();

        // Act & Assert
        assertDoesNotThrow(() -> {
            newShrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "Should be callable immediately after construction");

        verify(mockMetadata, times(1)).functionsAccept(mockClazz, newShrinker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata does not modify the shrinker's state
     * in a way that affects subsequent calls.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_doesNotAffectSubsequentCalls() {
        // Arrange
        KotlinSyntheticClassKindMetadata metadata1 = mock(KotlinSyntheticClassKindMetadata.class);
        KotlinSyntheticClassKindMetadata metadata2 = mock(KotlinSyntheticClassKindMetadata.class);

        // Act - call twice with different metadata
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, metadata1);
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, metadata2);

        // Assert - both calls should behave independently
        verify(metadata1, times(1)).functionsAccept(mockClazz, shrinker);
        verify(metadata2, times(1)).functionsAccept(mockClazz, shrinker);
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
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - functionsAccept should still be called
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata correctly integrates with the visitor pattern.
     * Verifies that it works when the shrinker is used as a KotlinMetadataVisitor.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_asPartOfVisitorPattern() {
        // Arrange
        proguard.classfile.kotlin.visitor.KotlinMetadataVisitor visitor = shrinker;

        // Act
        assertDoesNotThrow(() -> {
            visitor.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "Should work when used via interface reference");

        // Assert
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
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
        shrinker.visitKotlinSyntheticClassMetadata(specificClazz, mockMetadata);

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
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - only the delegation call should occur, nothing else
        verify(mockMetadata).functionsAccept(mockClazz, shrinker);
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
                shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
            }
        }, "Should handle rapid successive calls");

        // Verify that functionsAccept was called 100 times
        verify(mockMetadata, times(100)).functionsAccept(mockClazz, shrinker);
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
        shrinker.visitKotlinSyntheticClassMetadata(clazz1, mockMetadata);
        shrinker.visitKotlinSyntheticClassMetadata(clazz2, mockMetadata);
        shrinker.visitKotlinSyntheticClassMetadata(clazz3, mockMetadata);

        // Assert - each clazz should be passed to functionsAccept
        verify(mockMetadata, times(1)).functionsAccept(clazz1, shrinker);
        verify(mockMetadata, times(1)).functionsAccept(clazz2, shrinker);
        verify(mockMetadata, times(1)).functionsAccept(clazz3, shrinker);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata correctly implements
     * the KotlinMetadataVisitor interface contract.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_implementsVisitorContract() {
        // Act
        shrinker.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that the visitor is properly passed to accept method
        verify(mockMetadata, times(1)).functionsAccept(any(Clazz.class), any(KotlinFunctionVisitor.class));
    }
}
