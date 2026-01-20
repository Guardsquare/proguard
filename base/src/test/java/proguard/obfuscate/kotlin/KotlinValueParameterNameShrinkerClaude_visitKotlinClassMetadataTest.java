package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.visitor.KotlinConstructorVisitor;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinPropertyVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinValueParameterNameShrinker#visitKotlinClassMetadata(Clazz, KotlinClassKindMetadata)}.
 * Tests the visitKotlinClassMetadata method which delegates to constructorsAccept and visitKotlinDeclarationContainerMetadata.
 */
public class KotlinValueParameterNameShrinkerClaude_visitKotlinClassMetadataTest {

    private KotlinValueParameterNameShrinker shrinker;
    private Clazz mockClazz;
    private KotlinClassKindMetadata mockMetadata;

    @BeforeEach
    public void setUp() {
        shrinker = new KotlinValueParameterNameShrinker();
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
            shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        }, "visitKotlinClassMetadata should not throw an exception");
    }

    /**
     * Tests that visitKotlinClassMetadata calls constructorsAccept on the metadata.
     * This verifies that the method delegates to process constructors.
     */
    @Test
    public void testVisitKotlinClassMetadata_callsConstructorsAccept() {
        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

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
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - this is called via visitKotlinDeclarationContainerMetadata
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata calls propertiesAccept on the metadata.
     * This verifies that the method delegates to process properties (via visitKotlinDeclarationContainerMetadata).
     */
    @Test
    public void testVisitKotlinClassMetadata_callsPropertiesAccept() {
        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - this is called via visitKotlinDeclarationContainerMetadata
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata passes the shrinker itself as the visitor.
     * This verifies that the same shrinker instance is used for all visitor callbacks.
     */
    @Test
    public void testVisitKotlinClassMetadata_passesShrinkerAsVisitorForConstructors() {
        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that the shrinker instance is passed as the visitor for constructors
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinClassMetadata passes the shrinker itself as the visitor.
     * This verifies that the same shrinker instance is used for all visitor callbacks.
     */
    @Test
    public void testVisitKotlinClassMetadata_passesShrinkerAsVisitorForFunctions() {
        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that the shrinker instance is passed as the visitor for functions
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinClassMetadata passes the shrinker itself as the visitor.
     * This verifies that the same shrinker instance is used for all visitor callbacks.
     */
    @Test
    public void testVisitKotlinClassMetadata_passesShrinkerAsVisitorForProperties() {
        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that the shrinker instance is passed as the visitor for properties
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinClassMetadata calls all three accept methods:
     * constructorsAccept, functionsAccept, and propertiesAccept.
     * This verifies that the complete delegation chain works.
     */
    @Test
    public void testVisitKotlinClassMetadata_callsAllThreeAcceptMethods() {
        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify all three methods are called
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinClassMetadata calls constructorsAccept before functionsAccept and propertiesAccept.
     * This verifies the correct order of operations.
     */
    @Test
    public void testVisitKotlinClassMetadata_callsConstructorsAcceptFirst() {
        // Arrange
        org.mockito.InOrder inOrder = inOrder(mockMetadata);

        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify constructorsAccept is called before functionsAccept and propertiesAccept
        inOrder.verify(mockMetadata).constructorsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinClassMetadata calls functionsAccept before propertiesAccept.
     * This verifies the correct order within visitKotlinDeclarationContainerMetadata.
     */
    @Test
    public void testVisitKotlinClassMetadata_callsFunctionsAcceptBeforePropertiesAccept() {
        // Arrange
        org.mockito.InOrder inOrder = inOrder(mockMetadata);

        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify the specific order of functionsAccept before propertiesAccept
        inOrder.verify(mockMetadata).constructorsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinClassMetadata can be called multiple times.
     * This verifies that the method can be called repeatedly.
     */
    @Test
    public void testVisitKotlinClassMetadata_canBeCalledMultipleTimes() {
        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that each call triggers all three accept methods
        verify(mockMetadata, times(3)).constructorsAccept(eq(mockClazz), any(KotlinConstructorVisitor.class));
        verify(mockMetadata, times(3)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(3)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
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
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinClassMetadata(mockClazz2, mockMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz2, shrinker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz2, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz2, shrinker);
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
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata2);

        // Assert - verify that each metadata instance's methods are called
        verify(mockMetadata, times(1)).constructorsAccept(eq(mockClazz), any(KotlinConstructorVisitor.class));
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        verify(mockMetadata2, times(1)).constructorsAccept(eq(mockClazz), any(KotlinConstructorVisitor.class));
        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata with null Clazz does not throw but delegates.
     * The behavior with null parameters depends on the metadata implementation.
     */
    @Test
    public void testVisitKotlinClassMetadata_withNullClazz_delegatesToMetadata() {
        // Act & Assert - should delegate to metadata (may or may not throw depending on metadata implementation)
        shrinker.visitKotlinClassMetadata(null, mockMetadata);

        // Verify that all accept methods were called with null clazz
        verify(mockMetadata, times(1)).constructorsAccept(eq(null), any(KotlinConstructorVisitor.class));
        verify(mockMetadata, times(1)).functionsAccept(eq(null), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(1)).propertiesAccept(eq(null), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata with null metadata throws NullPointerException.
     * This is expected since we cannot call methods on a null object.
     */
    @Test
    public void testVisitKotlinClassMetadata_withNullMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            shrinker.visitKotlinClassMetadata(mockClazz, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitKotlinClassMetadata with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitKotlinClassMetadata_withBothNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            shrinker.visitKotlinClassMetadata(null, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that multiple instances of KotlinValueParameterNameShrinker behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitKotlinClassMetadata_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinValueParameterNameShrinker shrinker1 = new KotlinValueParameterNameShrinker();
        KotlinValueParameterNameShrinker shrinker2 = new KotlinValueParameterNameShrinker();
        KotlinClassKindMetadata mockMetadata1 = mock(KotlinClassKindMetadata.class);
        KotlinClassKindMetadata mockMetadata2 = mock(KotlinClassKindMetadata.class);

        // Act
        shrinker1.visitKotlinClassMetadata(mockClazz, mockMetadata1);
        shrinker2.visitKotlinClassMetadata(mockClazz, mockMetadata2);

        // Assert - both should make the same calls
        verify(mockMetadata1, times(1)).constructorsAccept(eq(mockClazz), any(KotlinConstructorVisitor.class));
        verify(mockMetadata1, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata1, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));

        verify(mockMetadata2, times(1)).constructorsAccept(eq(mockClazz), any(KotlinConstructorVisitor.class));
        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinClassMetadata does not interact with the Clazz parameter directly.
     * The clazz is only passed to accept methods, not used directly.
     */
    @Test
    public void testVisitKotlinClassMetadata_doesNotInteractWithClazz() {
        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify no direct interactions with clazz (it's only passed to accept methods)
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitKotlinClassMetadata only calls the three expected accept methods.
     * Verifies that only these three methods are called on the metadata, not other methods.
     */
    @Test
    public void testVisitKotlinClassMetadata_onlyCallsExpectedMethods() {
        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify only the three accept methods are called
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);
        verifyNoMoreInteractions(mockMetadata);
    }

    /**
     * Tests the complete workflow: constructorsAccept, then functionsAccept, then propertiesAccept.
     * Verifies that all three operations happen in the correct sequence.
     */
    @Test
    public void testVisitKotlinClassMetadata_completeWorkflow() {
        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify all three operations occurred
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);

        // And verify the order
        org.mockito.InOrder inOrder = inOrder(mockMetadata);
        inOrder.verify(mockMetadata).constructorsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).propertiesAccept(mockClazz, shrinker);
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
        shrinker.visitKotlinClassMetadata(specificClazz, specificMetadata);

        // Assert - verify the exact parameters are passed
        verify(specificMetadata, times(1)).constructorsAccept(specificClazz, shrinker);
        verify(specificMetadata, times(1)).functionsAccept(specificClazz, shrinker);
        verify(specificMetadata, times(1)).propertiesAccept(specificClazz, shrinker);
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
        shrinker.visitKotlinClassMetadata(mockClazz, metadata1);
        shrinker.visitKotlinClassMetadata(mockClazz, metadata2);
        shrinker.visitKotlinClassMetadata(mockClazz, metadata3);

        // Assert - all three accept methods should be called for each
        verify(metadata1, times(1)).constructorsAccept(mockClazz, shrinker);
        verify(metadata1, times(1)).functionsAccept(mockClazz, shrinker);
        verify(metadata1, times(1)).propertiesAccept(mockClazz, shrinker);
        verify(metadata2, times(1)).constructorsAccept(mockClazz, shrinker);
        verify(metadata2, times(1)).functionsAccept(mockClazz, shrinker);
        verify(metadata2, times(1)).propertiesAccept(mockClazz, shrinker);
        verify(metadata3, times(1)).constructorsAccept(mockClazz, shrinker);
        verify(metadata3, times(1)).functionsAccept(mockClazz, shrinker);
        verify(metadata3, times(1)).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinClassMetadata passes the same shrinker instance
     * to all three accept methods.
     */
    @Test
    public void testVisitKotlinClassMetadata_passesSameShrinkerToAllMethods() {
        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - verify the same shrinker instance is passed to all three
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);

        // All three should receive the exact same visitor instance
        verify(mockMetadata).constructorsAccept(eq(mockClazz), same(shrinker));
        verify(mockMetadata).functionsAccept(eq(mockClazz), same(shrinker));
        verify(mockMetadata).propertiesAccept(eq(mockClazz), same(shrinker));
    }

    /**
     * Tests that visitKotlinClassMetadata maintains consistent behavior
     * when called with the same parameters multiple times.
     */
    @Test
    public void testVisitKotlinClassMetadata_consistentWithSameParameters() {
        // Act - call multiple times with same parameters
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - should call the methods twice (once per invocation)
        verify(mockMetadata, times(2)).constructorsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(2)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(2)).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinClassMetadata can handle being called
     * immediately after construction of the shrinker.
     */
    @Test
    public void testVisitKotlinClassMetadata_immediatelyAfterConstruction() {
        // Arrange
        KotlinValueParameterNameShrinker newShrinker = new KotlinValueParameterNameShrinker();

        // Act & Assert
        assertDoesNotThrow(() -> {
            newShrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        }, "Should be callable immediately after construction");

        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, newShrinker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, newShrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, newShrinker);
    }

    /**
     * Tests that visitKotlinClassMetadata does not modify the shrinker's state
     * in a way that affects subsequent calls.
     */
    @Test
    public void testVisitKotlinClassMetadata_doesNotAffectSubsequentCalls() {
        // Arrange
        KotlinClassKindMetadata metadata1 = mock(KotlinClassKindMetadata.class);
        KotlinClassKindMetadata metadata2 = mock(KotlinClassKindMetadata.class);

        // Act - call twice with different metadata
        shrinker.visitKotlinClassMetadata(mockClazz, metadata1);
        shrinker.visitKotlinClassMetadata(mockClazz, metadata2);

        // Assert - both calls should behave independently
        verify(metadata1, times(1)).constructorsAccept(mockClazz, shrinker);
        verify(metadata1, times(1)).functionsAccept(mockClazz, shrinker);
        verify(metadata1, times(1)).propertiesAccept(mockClazz, shrinker);
        verify(metadata2, times(1)).constructorsAccept(mockClazz, shrinker);
        verify(metadata2, times(1)).functionsAccept(mockClazz, shrinker);
        verify(metadata2, times(1)).propertiesAccept(mockClazz, shrinker);
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
        doNothing().when(mockMetadata).propertiesAccept(any(), any());

        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - all three should still be called
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinClassMetadata correctly integrates with the visitor pattern.
     * Verifies that it works when the shrinker is used as a KotlinMetadataVisitor.
     */
    @Test
    public void testVisitKotlinClassMetadata_asPartOfVisitorPattern() {
        // Arrange
        proguard.classfile.kotlin.visitor.KotlinMetadataVisitor visitor = shrinker;

        // Act
        assertDoesNotThrow(() -> {
            visitor.visitKotlinClassMetadata(mockClazz, mockMetadata);
        }, "Should work when used via interface reference");

        // Assert
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinClassMetadata passes the clazz parameter
     * unchanged to all three accept methods.
     */
    @Test
    public void testVisitKotlinClassMetadata_passesSameClazzToAllMethods() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);

        // Act
        shrinker.visitKotlinClassMetadata(specificClazz, mockMetadata);

        // Assert - all three methods should receive the exact same clazz instance
        verify(mockMetadata).constructorsAccept(same(specificClazz), any());
        verify(mockMetadata).functionsAccept(same(specificClazz), any());
        verify(mockMetadata).propertiesAccept(same(specificClazz), any());
    }

    /**
     * Tests that visitKotlinClassMetadata handles the case where
     * constructorsAccept is called before functionsAccept and propertiesAccept consistently.
     */
    @Test
    public void testVisitKotlinClassMetadata_maintainsCallOrder() {
        // Arrange
        org.mockito.InOrder inOrder = inOrder(mockMetadata);

        // Act - call multiple times
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - order should be maintained for each call
        inOrder.verify(mockMetadata).constructorsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).propertiesAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).constructorsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinClassMetadata delegates all visitor work
     * to the metadata object without performing additional operations.
     */
    @Test
    public void testVisitKotlinClassMetadata_purelyDelegates() {
        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - only the three delegation calls should occur, nothing else
        verify(mockMetadata).constructorsAccept(mockClazz, shrinker);
        verify(mockMetadata).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata).propertiesAccept(mockClazz, shrinker);
        verifyNoMoreInteractions(mockMetadata);
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitKotlinClassMetadata properly calls visitKotlinDeclarationContainerMetadata.
     * This verifies the delegation to the superclass method.
     */
    @Test
    public void testVisitKotlinClassMetadata_callsVisitKotlinDeclarationContainerMetadata() {
        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - functionsAccept and propertiesAccept should be called
        // (these are called by visitKotlinDeclarationContainerMetadata)
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);
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
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - constructorsAccept should be first, then the delegation methods
        inOrder.verify(mockMetadata).constructorsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).functionsAccept(mockClazz, shrinker);
        inOrder.verify(mockMetadata).propertiesAccept(mockClazz, shrinker);
    }

    /**
     * Tests that visitKotlinClassMetadata processes all aspects of a class:
     * constructors, functions, and properties.
     */
    @Test
    public void testVisitKotlinClassMetadata_processesCompleteClassStructure() {
        // Act
        shrinker.visitKotlinClassMetadata(mockClazz, mockMetadata);

        // Assert - all three aspects of a class should be processed
        verify(mockMetadata, times(1)).constructorsAccept(mockClazz, shrinker);  // constructors
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, shrinker);     // functions
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, shrinker);    // properties
    }
}
