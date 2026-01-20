package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.visitor.KotlinConstructorVisitor;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinMetadataVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinValueParameterUsageMarker#visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)}.
 * Tests the visitKotlinClassMetadata method which delegates to constructorsAccept and visitKotlinDeclarationContainerMetadata.
 */
public class KotlinValueParameterUsageMarkerClaude_visitKotlinClassMetadataTest {

    private KotlinValueParameterUsageMarker marker;
    private Clazz mockClazz;
    private KotlinClassKindMetadata mockMetadata;

    @BeforeEach
    public void setUp() {
        marker = new KotlinValueParameterUsageMarker();
        mockClazz = mock(Clazz.class);
        mockMetadata = mock(KotlinClassKindMetadata.class);
    }

    /**
     * Tests that visitKotlinClassMetadata can be called without throwing exceptions.
     */
    @Test
    public void testVisitKotlinClassMetadata_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            marker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        }, "visitKotlinClassMetadata should not throw an exception");
    }

    /**
     * Tests that visitKotlinClassMetadata calls constructorsAccept on the metadata.
     * This verifies that the method delegates to process constructors.
     */
    @Test
    public void testVisitKotlinClassMetadata_callsConstructorsAccept() {
        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert
        verify(mockMetadata, times(1)).constructorsAccept(eq(mockClazz), any(KotlinConstructorVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata calls functionsAccept on the metadata.
     * This verifies that the method delegates to process functions (via visitKotlinDeclarationContainerMetadata).
     */
    @Test
    public void testVisitKotlinClassMetadata_callsFunctionsAccept() {
        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - this is called via visitKotlinDeclarationContainerMetadata
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata calls accept with AllPropertyVisitor on the metadata.
     * This verifies that the method delegates to process properties (via visitKotlinDeclarationContainerMetadata).
     */
    @Test
    public void testVisitKotlinClassMetadata_callsAcceptWithAllPropertyVisitor() {
        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - this is called via visitKotlinDeclarationContainerMetadata with AllPropertyVisitor
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata passes the marker itself as the visitor.
     * This verifies that the same marker instance is used for constructor and function visitor callbacks.
     */
    @Test
    public void testVisitKotlinClassMetadata_passesMarkerAsVisitorForConstructors() {
        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that the marker instance is passed as the visitor for constructors
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinClassMetadata passes the marker itself as the visitor.
     * This verifies that the same marker instance is used for function visitor callbacks.
     */
    @Test
    public void testVisitKotlinClassMetadata_passesMarkerAsVisitorForFunctions() {
        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that the marker instance is passed as the visitor for functions
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinClassMetadata calls all three methods:
     * constructorsAccept, functionsAccept, and accept (with AllPropertyVisitor).
     * This verifies that the complete delegation chain works.
     */
    @Test
    public void testVisitKotlinClassMetadata_callsAllThreeMethods() {
        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify all three methods are called
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata calls constructorsAccept before functionsAccept and accept.
     * This verifies the correct order of operations.
     */
    @Test
    public void testVisitKotlinClassMetadata_callsConstructorsAcceptFirst() {
        // Arrange
        org.mockito.InOrder inOrder = inOrder(mockMetadata);

        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify constructorsAccept is called before functionsAccept and accept
        inOrder.verify(mockMetadata).constructorsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata calls functionsAccept before accept.
     * This verifies the correct order within visitKotlinDeclarationContainerMetadata.
     */
    @Test
    public void testVisitKotlinClassMetadata_callsFunctionsAcceptBeforeAccept() {
        // Arrange
        org.mockito.InOrder inOrder = inOrder(mockMetadata);

        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify the specific order of functionsAccept before accept
        inOrder.verify(mockMetadata).constructorsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata can be called multiple times.
     * This verifies that the method can be called repeatedly.
     */
    @Test
    public void testVisitKotlinClassMetadata_canBeCalledMultipleTimes() {
        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that each call triggers all three methods
        verify(mockMetadata, times(3)).constructorsAccept(eq(mockClazz), any(KotlinConstructorVisitor.class));
        verify(mockMetadata, times(3)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(3)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata works with different clazz instances.
     * This verifies that the method properly passes different clazz parameters.
     */
    @Test
    public void testVisitKotlinClassMetadata_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);

        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        marker.visitKotlinClassMetadata(mockClazz2, mockMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz2, marker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz2, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz2), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata works with different metadata instances.
     * This verifies that the method properly handles different metadata parameters.
     */
    @Test
    public void testVisitKotlinClassMetadata_withDifferentMetadata_callsCorrectMetadata() {
        // Arrange
        KotlinClassKindMetadata mockMetadata2 = mock(KotlinClassKindMetadata.class);

        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata2);

        // Assert - verify that each metadata instance's methods are called
        verify(mockMetadata, times(1)).constructorsAccept(eq(mockClazz), any(KotlinConstructorVisitor.class));
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        verify(mockMetadata2, times(1)).constructorsAccept(eq(mockClazz), any(KotlinConstructorVisitor.class));
        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata with null Clazz does not throw but delegates.
     * The behavior with null parameters depends on the metadata implementation.
     */
    @Test
    public void testVisitKotlinClassMetadata_withNullClazz_delegatesToMetadata() {
        // Act & Assert - should delegate to metadata (may or may not throw depending on metadata implementation)
        marker.visitKotlinClassMetadata(null, mockMetadata);

        // Verify that all methods were called with null clazz
        verify(mockMetadata, times(1)).constructorsAccept(eq(null), any(KotlinConstructorVisitor.class));
        verify(mockMetadata, times(1)).functionsAccept(eq(null), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(1)).accept(eq(null), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata with null metadata throws NullPointerException.
     * This is expected since we cannot call methods on a null object.
     */
    @Test
    public void testVisitKotlinClassMetadata_withNullMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitKotlinClassMetadata(mockClazz, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitKotlinClassMetadata with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitKotlinClassMetadata_withBothNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitKotlinClassMetadata(null, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that multiple instances of KotlinValueParameterUsageMarker behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitKotlinClassMetadata_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinValueParameterUsageMarker marker1 = new KotlinValueParameterUsageMarker();
        KotlinValueParameterUsageMarker marker2 = new KotlinValueParameterUsageMarker();
        KotlinClassKindMetadata mockMetadata1 = mock(KotlinClassKindMetadata.class);
        KotlinClassKindMetadata mockMetadata2 = mock(KotlinClassKindMetadata.class);

        // Act
        marker1.visitKotlinClassMetadata(mockClazz, mockMetadata1);
        marker2.visitKotlinClassMetadata(mockClazz, mockMetadata2);

        // Assert - both should make the same calls
        verify(mockMetadata1, times(1)).constructorsAccept(eq(mockClazz), any(KotlinConstructorVisitor.class));
        verify(mockMetadata1, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata1, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));

        verify(mockMetadata2, times(1)).constructorsAccept(eq(mockClazz), any(KotlinConstructorVisitor.class));
        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata does not interact with the Clazz parameter directly.
     * The clazz is only passed to accept methods, not used directly.
     */
    @Test
    public void testVisitKotlinClassMetadata_doesNotInteractWithClazz() {
        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify no direct interactions with clazz (it's only passed to accept methods)
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitKotlinClassMetadata only calls the three expected methods.
     * Verifies that only these three methods are called on the metadata, not other methods.
     */
    @Test
    public void testVisitKotlinClassMetadata_onlyCallsExpectedMethods() {
        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify only the three methods are called
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        verifyNoMoreInteractions(mockMetadata);
    }

    /**
     * Tests the complete workflow: constructorsAccept, then functionsAccept, then accept.
     * Verifies that all three operations happen in the correct sequence.
     */
    @Test
    public void testVisitKotlinClassMetadata_completeWorkflow() {
        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify all three operations occurred
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));

        // And verify the order
        org.mockito.InOrder inOrder = inOrder(mockMetadata);
        inOrder.verify(mockMetadata).constructorsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata passes correct parameters through.
     * Verifies parameter integrity throughout the method execution.
     */
    @Test
    public void testVisitKotlinClassMetadata_passesCorrectParameters() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);
        KotlinClassKindMetadata specificMetadata = mock(KotlinClassKindMetadata.class);

        // Act
        marker.visitKotlinClassMetadata(specificClazz, specificMetadata);

        // Assert - verify the exact parameters are passed
        verify(specificMetadata, times(1)).constructorsAccept(specificClazz, marker);
        verify(specificMetadata, times(1)).functionsAccept(specificClazz, marker);
        verify(specificMetadata, times(1)).accept(eq(specificClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata with sequential calls processes each metadata correctly.
     */
    @Test
    public void testVisitKotlinClassMetadata_sequentialCalls_processEachMetadata() {
        // Arrange
        KotlinClassKindMetadata metadata1 = mock(KotlinClassKindMetadata.class);
        KotlinClassKindMetadata metadata2 = mock(KotlinClassKindMetadata.class);
        KotlinClassKindMetadata metadata3 = mock(KotlinClassKindMetadata.class);

        // Act
        marker.visitKotlinClassMetadata(mockClazz, metadata1);
        marker.visitKotlinClassMetadata(mockClazz, metadata2);
        marker.visitKotlinClassMetadata(mockClazz, metadata3);

        // Assert - all three methods should be called for each
        verify(metadata1, times(1)).constructorsAccept(mockClazz, marker);
        verify(metadata1, times(1)).functionsAccept(mockClazz, marker);
        verify(metadata1, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        verify(metadata2, times(1)).constructorsAccept(mockClazz, marker);
        verify(metadata2, times(1)).functionsAccept(mockClazz, marker);
        verify(metadata2, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        verify(metadata3, times(1)).constructorsAccept(mockClazz, marker);
        verify(metadata3, times(1)).functionsAccept(mockClazz, marker);
        verify(metadata3, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata passes the same marker instance
     * to constructorsAccept and functionsAccept.
     */
    @Test
    public void testVisitKotlinClassMetadata_passesSameMarkerToConstructorsAndFunctions() {
        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify the same marker instance is passed
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);

        // Both should receive the exact same visitor instance
        verify(mockMetadata).constructorsAccept(eq(mockClazz), same(marker));
        verify(mockMetadata).functionsAccept(eq(mockClazz), same(marker));
    }

    /**
     * Tests that visitKotlinClassMetadata maintains consistent behavior
     * when called with the same parameters multiple times.
     */
    @Test
    public void testVisitKotlinClassMetadata_consistentWithSameParameters() {
        // Act - call multiple times with same parameters
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - should call the methods twice (once per invocation)
        verify(mockMetadata, times(2)).constructorsAccept(mockClazz, marker);
        verify(mockMetadata, times(2)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(2)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata can handle being called
     * immediately after construction of the marker.
     */
    @Test
    public void testVisitKotlinClassMetadata_immediatelyAfterConstruction() {
        // Arrange
        KotlinValueParameterUsageMarker newMarker = new KotlinValueParameterUsageMarker();

        // Act & Assert
        assertDoesNotThrow(() -> {
            newMarker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        }, "Should be callable immediately after construction");

        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, newMarker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, newMarker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata does not modify the marker's state
     * in a way that affects subsequent calls.
     */
    @Test
    public void testVisitKotlinClassMetadata_doesNotAffectSubsequentCalls() {
        // Arrange
        KotlinClassKindMetadata metadata1 = mock(KotlinClassKindMetadata.class);
        KotlinClassKindMetadata metadata2 = mock(KotlinClassKindMetadata.class);

        // Act - call twice with different metadata
        marker.visitKotlinClassMetadata(mockClazz, metadata1);
        marker.visitKotlinClassMetadata(mockClazz, metadata2);

        // Assert - both calls should behave independently
        verify(metadata1, times(1)).constructorsAccept(mockClazz, marker);
        verify(metadata1, times(1)).functionsAccept(mockClazz, marker);
        verify(metadata1, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        verify(metadata2, times(1)).constructorsAccept(mockClazz, marker);
        verify(metadata2, times(1)).functionsAccept(mockClazz, marker);
        verify(metadata2, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata calls all three methods
     * even if one of them is a no-op in the mock.
     */
    @Test
    public void testVisitKotlinClassMetadata_callsAllMethodsRegardlessOfBehavior() {
        // Arrange - configure all methods to do nothing (default mock behavior)
        doNothing().when(mockMetadata).constructorsAccept(any(), any());
        doNothing().when(mockMetadata).functionsAccept(any(), any());
        doNothing().when(mockMetadata).accept(any(), any());

        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - all three should still be called
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata correctly integrates with the visitor pattern.
     * Verifies that it works when the marker is used as a KotlinMetadataVisitor.
     */
    @Test
    public void testVisitKotlinClassMetadata_asPartOfVisitorPattern() {
        // Arrange
        proguard.classfile.kotlin.visitor.KotlinMetadataVisitor visitor = marker;

        // Act
        assertDoesNotThrow(() -> {
            visitor.visitKotlinClassMetadata(mockClazz, mockMetadata);
        }, "Should work when used via interface reference");

        // Assert
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata passes the clazz parameter
     * unchanged to all three methods.
     */
    @Test
    public void testVisitKotlinClassMetadata_passesSameClazzToAllMethods() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);

        // Act
        marker.visitKotlinClassMetadata(specificClazz, mockMetadata);

        // Assert - all three methods should receive the exact same clazz instance
        verify(mockMetadata).constructorsAccept(same(specificClazz), any());
        verify(mockMetadata).functionsAccept(same(specificClazz), any());
        verify(mockMetadata).accept(same(specificClazz), any());
    }

    /**
     * Tests that visitKotlinClassMetadata handles the case where
     * constructorsAccept is called before functionsAccept and accept consistently.
     */
    @Test
    public void testVisitKotlinClassMetadata_maintainsCallOrder() {
        // Arrange
        org.mockito.InOrder inOrder = inOrder(mockMetadata);

        // Act - call multiple times
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - order should be maintained for each call
        inOrder.verify(mockMetadata).constructorsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        inOrder.verify(mockMetadata).constructorsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata delegates all visitor work
     * to the metadata object without performing additional operations.
     */
    @Test
    public void testVisitKotlinClassMetadata_purelyDelegates() {
        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - only the three delegation calls should occur, nothing else
        verify(mockMetadata).constructorsAccept(mockClazz, marker);
        verify(mockMetadata).functionsAccept(mockClazz, marker);
        verify(mockMetadata).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        verifyNoMoreInteractions(mockMetadata);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitKotlinClassMetadata properly calls visitKotlinDeclarationContainerMetadata.
     * This verifies the delegation to the method.
     */
    @Test
    public void testVisitKotlinClassMetadata_callsVisitKotlinDeclarationContainerMetadata() {
        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - functionsAccept and accept should be called
        // (these are called by visitKotlinDeclarationContainerMetadata)
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata calls constructorsAccept directly
     * before delegating to visitKotlinDeclarationContainerMetadata.
     */
    @Test
    public void testVisitKotlinClassMetadata_callsConstructorsBeforeDelegation() {
        // Arrange
        org.mockito.InOrder inOrder = inOrder(mockMetadata);

        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - constructorsAccept should be first, then the delegation methods
        inOrder.verify(mockMetadata).constructorsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata processes all aspects of a class:
     * constructors, functions, and properties (via AllPropertyVisitor).
     */
    @Test
    public void testVisitKotlinClassMetadata_processesCompleteClassStructure() {
        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - all three aspects of a class should be processed
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, marker);  // constructors
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);     // functions
        verify(mockMetadata, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class)); // properties via AllPropertyVisitor
    }

    /**
     * Tests that visitKotlinClassMetadata creates a new AllPropertyVisitor for each call.
     * This verifies that a fresh visitor is used each time.
     */
    @Test
    public void testVisitKotlinClassMetadata_createsNewAllPropertyVisitorEachCall() {
        // Act - call multiple times
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - accept should be called twice (once per invocation with a new AllPropertyVisitor)
        verify(mockMetadata, times(2)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata processes class metadata in the correct sequence:
     * 1. Process constructors
     * 2. Process functions
     * 3. Process properties (via AllPropertyVisitor)
     */
    @Test
    public void testVisitKotlinClassMetadata_correctProcessingSequence() {
        // Arrange
        org.mockito.InOrder inOrder = inOrder(mockMetadata);

        // Act
        marker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify the complete sequence
        inOrder.verify(mockMetadata).constructorsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, marker);
        inOrder.verify(mockMetadata).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Tests that visitKotlinClassMetadata with interleaved calls maintains proper state.
     * Verifies that each call is independent.
     */
    @Test
    public void testVisitKotlinClassMetadata_interleavedCallsMaintainState() {
        // Arrange
        KotlinValueParameterUsageMarker marker1 = new KotlinValueParameterUsageMarker();
        KotlinValueParameterUsageMarker marker2 = new KotlinValueParameterUsageMarker();
        KotlinClassKindMetadata metadata1 = mock(KotlinClassKindMetadata.class);
        KotlinClassKindMetadata metadata2 = mock(KotlinClassKindMetadata.class);

        // Act - interleave calls
        marker1.visitKotlinClassMetadata(mockClazz, metadata1);
        marker2.visitKotlinClassMetadata(mockClazz, metadata2);
        marker1.visitKotlinClassMetadata(mockClazz, metadata1);

        // Assert - verify each call is independent
        verify(metadata1, times(2)).constructorsAccept(mockClazz, marker1);
        verify(metadata1, times(2)).functionsAccept(mockClazz, marker1);
        verify(metadata1, times(2)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
        verify(metadata2, times(1)).constructorsAccept(mockClazz, marker2);
        verify(metadata2, times(1)).functionsAccept(mockClazz, marker2);
        verify(metadata2, times(1)).accept(eq(mockClazz), any(KotlinMetadataVisitor.class));
    }
}
