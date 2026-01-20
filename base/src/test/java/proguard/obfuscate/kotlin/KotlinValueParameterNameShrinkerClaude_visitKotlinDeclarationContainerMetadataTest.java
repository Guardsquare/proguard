package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinPropertyVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinValueParameterNameShrinker#visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)}.
 * Tests the visitKotlinDeclarationContainerMetadata method which delegates to functionsAccept and propertiesAccept.
 */
public class KotlinValueParameterNameShrinkerClaude_visitKotlinDeclarationContainerMetadataTest {

    private KotlinValueParameterNameShrinker shrinker;
    private Clazz mockClazz;
    private KotlinDeclarationContainerMetadata mockMetadata;

    @BeforeEach
    public void setUp() {
        shrinker = new KotlinValueParameterNameShrinker();
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
            shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        }, "visitKotlinDeclarationContainerMetadata should not throw an exception");
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls functionsAccept on the metadata.
     * This verifies that the method delegates to process functions.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsFunctionsAccept() {
        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls propertiesAccept on the metadata.
     * This verifies that the method delegates to process properties.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsPropertiesAccept() {
        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata passes the shrinker itself as the visitor.
     * This verifies that the same shrinker instance is used for visitor callbacks.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_passesShrinkerAsVisitorForFunctions() {
        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify that the shrinker instance is passed as the visitor for functions
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata passes the shrinker itself as the visitor.
     * This verifies that the same shrinker instance is used for visitor callbacks.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_passesShrinkerAsVisitorForProperties() {
        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify that the shrinker instance is passed as the visitor for properties
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls both functionsAccept and propertiesAccept.
     * This verifies that both delegations occur.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsBothFunctionsAndPropertiesAccept() {
        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify both methods are called
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls functionsAccept before propertiesAccept.
     * This verifies the correct order of operations.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsFunctionsAcceptBeforePropertiesAccept() {
        // Arrange
        org.mockito.InOrder inOrder = inOrder(mockMetadata);

        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify functionsAccept is called before propertiesAccept
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata can be called multiple times.
     * This verifies that the method can be called repeatedly.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_canBeCalledMultipleTimes() {
        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify that each call triggers functionsAccept and propertiesAccept
        verify(mockMetadata, times(3)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(3)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
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
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz2, mockMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz2, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz2, shrinker);
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
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata2);

        // Assert - verify that each metadata instance's methods are called
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with null Clazz does not throw but delegates.
     * The behavior with null parameters depends on the metadata implementation.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withNullClazz_delegatesToMetadata() {
        // Act & Assert - should delegate to metadata (may or may not throw depending on metadata implementation)
        shrinker.visitKotlinDeclarationContainerMetadata(null, mockMetadata);

        // Verify that both accept methods were called with null clazz
        verify(mockMetadata, times(1)).functionsAccept(eq(null), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(1)).propertiesAccept(eq(null), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with null metadata throws NullPointerException.
     * This is expected since we cannot call methods on a null object.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withNullMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withBothNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            shrinker.visitKotlinDeclarationContainerMetadata(null, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that multiple instances of KotlinValueParameterNameShrinker behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinValueParameterNameShrinker shrinker1 = new KotlinValueParameterNameShrinker();
        KotlinValueParameterNameShrinker shrinker2 = new KotlinValueParameterNameShrinker();
        KotlinDeclarationContainerMetadata mockMetadata1 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata mockMetadata2 = mock(KotlinDeclarationContainerMetadata.class);

        // Act
        shrinker1.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata1);
        shrinker2.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata2);

        // Assert - both should make the same calls
        verify(mockMetadata1, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata1, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));

        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata does not interact with the Clazz parameter directly.
     * The clazz is only passed to functionsAccept and propertiesAccept, not used directly.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_doesNotInteractWithClazz() {
        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify no direct interactions with clazz (it's only passed to accept methods)
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata only calls functionsAccept and propertiesAccept.
     * Verifies that only these two methods are called on the metadata, not other methods.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_onlyCallsExpectedMethods() {
        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify only functionsAccept and propertiesAccept are called
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);
        verifyNoMoreInteractions(mockMetadata);
    }

    /**
     * Tests the complete workflow: functionsAccept then propertiesAccept.
     * Verifies that both operations happen in sequence.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_completeWorkflow() {
        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify both operations occurred
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);

        // And verify the order
        org.mockito.InOrder inOrder = inOrder(mockMetadata);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).propertiesAccept(mockClazz, shrinker);
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
        shrinker.visitKotlinDeclarationContainerMetadata(specificClazz, specificMetadata);

        // Assert - verify the exact parameters are passed
        verify(specificMetadata, times(1)).functionsAccept(specificClazz, shrinker);
        verify(specificMetadata, times(1)).propertiesAccept(specificClazz, shrinker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with sequential calls
     * processes each metadata correctly.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_sequentialCalls_processEachMetadata() {
        // Arrange
        KotlinDeclarationContainerMetadata metadata1 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata metadata2 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata metadata3 = mock(KotlinDeclarationContainerMetadata.class);

        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, metadata1);
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, metadata2);
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, metadata3);

        // Assert - both functionsAccept and propertiesAccept should be called for each
        verify(metadata1, times(1)).functionsAccept(mockClazz, shrinker);
        verify(metadata1, times(1)).propertiesAccept(mockClazz, shrinker);
        verify(metadata2, times(1)).functionsAccept(mockClazz, shrinker);
        verify(metadata2, times(1)).propertiesAccept(mockClazz, shrinker);
        verify(metadata3, times(1)).functionsAccept(mockClazz, shrinker);
        verify(metadata3, times(1)).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata passes the same shrinker instance
     * to both functionsAccept and propertiesAccept.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_passesSameShrinkerToBothMethods() {
        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify the same shrinker instance is passed to both
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);

        // Both should receive the exact same visitor instance
        verify(mockMetadata).functionsAccept(eq(mockClazz), same(shrinker));
        verify(mockMetadata).propertiesAccept(eq(mockClazz), same(shrinker));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata maintains consistent behavior
     * when called with the same parameters multiple times.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_consistentWithSameParameters() {
        // Act - call multiple times with same parameters
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - should call the methods twice (once per invocation)
        verify(mockMetadata, times(2)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(2)).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata can handle being called
     * immediately after construction of the shrinker.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_immediatelyAfterConstruction() {
        // Arrange
        KotlinValueParameterNameShrinker newShrinker = new KotlinValueParameterNameShrinker();

        // Act & Assert
        assertDoesNotThrow(() -> {
            newShrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        }, "Should be callable immediately after construction");

        verify(mockMetadata, times(1)).functionsAccept(mockClazz, newShrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, newShrinker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata does not modify the shrinker's state
     * in a way that affects subsequent calls.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_doesNotAffectSubsequentCalls() {
        // Arrange
        KotlinDeclarationContainerMetadata metadata1 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata metadata2 = mock(KotlinDeclarationContainerMetadata.class);

        // Act - call twice with different metadata
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, metadata1);
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, metadata2);

        // Assert - both calls should behave independently
        verify(metadata1, times(1)).functionsAccept(mockClazz, shrinker);
        verify(metadata1, times(1)).propertiesAccept(mockClazz, shrinker);
        verify(metadata2, times(1)).functionsAccept(mockClazz, shrinker);
        verify(metadata2, times(1)).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls both methods
     * even if one of them is a no-op in the mock.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsBothMethodsRegardlessOfBehavior() {
        // Arrange - configure one method to do nothing (default mock behavior)
        doNothing().when(mockMetadata).functionsAccept(any(), any());
        doNothing().when(mockMetadata).propertiesAccept(any(), any());

        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - both should still be called
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata correctly integrates with the visitor pattern.
     * Verifies that it works when the shrinker is used as a KotlinMetadataVisitor.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_asPartOfVisitorPattern() {
        // Arrange
        proguard.classfile.kotlin.visitor.KotlinMetadataVisitor visitor = shrinker;

        // Act
        assertDoesNotThrow(() -> {
            visitor.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        }, "Should work when used via interface reference");

        // Assert
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata passes the clazz parameter
     * unchanged to both functionsAccept and propertiesAccept.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_passesSameClazzToBothMethods() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);

        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(specificClazz, mockMetadata);

        // Assert - both methods should receive the exact same clazz instance
        verify(mockMetadata).functionsAccept(same(specificClazz), any());
        verify(mockMetadata).propertiesAccept(same(specificClazz), any());
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata handles the case where
     * functionsAccept is called before propertiesAccept consistently.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_maintainsCallOrder() {
        // Arrange
        org.mockito.InOrder inOrder = inOrder(mockMetadata);

        // Act - call multiple times
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - order should be maintained for each call
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).propertiesAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata delegates all visitor work
     * to the metadata object without performing additional operations.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_purelyDelegates() {
        // Act
        shrinker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - only the two delegation calls should occur, nothing else
        verify(mockMetadata).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata).propertiesAccept(mockClazz, shrinker);
        verifyNoMoreInteractions(mockMetadata);
        verifyNoInteractions(mockClazz);
    }
}
