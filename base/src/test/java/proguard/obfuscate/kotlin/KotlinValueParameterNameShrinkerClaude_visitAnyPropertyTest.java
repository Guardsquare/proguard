package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.KotlinPropertyMetadata;
import proguard.classfile.kotlin.visitor.KotlinValueParameterVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinValueParameterNameShrinker#visitAnyProperty(Clazz, KotlinDeclarationContainerMetadata, KotlinPropertyMetadata)}.
 * Tests the visitAnyProperty method which delegates to setterParametersAccept.
 */
public class KotlinValueParameterNameShrinkerClaude_visitAnyPropertyTest {

    private KotlinValueParameterNameShrinker shrinker;
    private Clazz mockClazz;
    private KotlinDeclarationContainerMetadata mockDeclarationContainerMetadata;
    private KotlinPropertyMetadata mockPropertyMetadata;

    @BeforeEach
    public void setUp() {
        shrinker = new KotlinValueParameterNameShrinker();
        mockClazz = mock(Clazz.class);
        mockDeclarationContainerMetadata = mock(KotlinDeclarationContainerMetadata.class);
        mockPropertyMetadata = mock(KotlinPropertyMetadata.class);
    }

    /**
     * Tests that visitAnyProperty can be called without throwing exceptions.
     */
    @Test
    public void testVisitAnyProperty_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);
        }, "visitAnyProperty should not throw an exception");
    }

    /**
     * Tests that visitAnyProperty calls setterParametersAccept on the property metadata.
     * This verifies that the method delegates to process setter parameters.
     */
    @Test
    public void testVisitAnyProperty_callsSetterParametersAccept() {
        // Act
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);

        // Assert
        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty passes the correct clazz parameter to setterParametersAccept.
     */
    @Test
    public void testVisitAnyProperty_passesCorrectClazz() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);

        // Act
        shrinker.visitAnyProperty(specificClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);

        // Assert
        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                same(specificClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty passes the correct declaration container metadata to setterParametersAccept.
     */
    @Test
    public void testVisitAnyProperty_passesCorrectDeclarationContainerMetadata() {
        // Arrange
        KotlinDeclarationContainerMetadata specificMetadata = mock(KotlinDeclarationContainerMetadata.class);

        // Act
        shrinker.visitAnyProperty(mockClazz, specificMetadata, mockPropertyMetadata);

        // Assert
        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                eq(mockClazz),
                same(specificMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty passes a KotlinValueParameterVisitor to setterParametersAccept.
     */
    @Test
    public void testVisitAnyProperty_passesValueParameterVisitor() {
        // Act
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);

        // Assert - verify that a KotlinValueParameterVisitor is passed
        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                any(Clazz.class),
                any(KotlinDeclarationContainerMetadata.class),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty can be called multiple times.
     */
    @Test
    public void testVisitAnyProperty_canBeCalledMultipleTimes() {
        // Act
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);

        // Assert - verify that each call triggers setterParametersAccept
        verify(mockPropertyMetadata, times(3)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty works with different clazz instances.
     */
    @Test
    public void testVisitAnyProperty_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);

        // Act
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);
        shrinker.visitAnyProperty(mockClazz2, mockDeclarationContainerMetadata, mockPropertyMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                same(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                same(mockClazz2),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty works with different declaration container metadata instances.
     */
    @Test
    public void testVisitAnyProperty_withDifferentDeclarationContainerMetadata_passesCorrectMetadata() {
        // Arrange
        KotlinDeclarationContainerMetadata mockMetadata2 = mock(KotlinDeclarationContainerMetadata.class);

        // Act
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);
        shrinker.visitAnyProperty(mockClazz, mockMetadata2, mockPropertyMetadata);

        // Assert
        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                eq(mockClazz),
                same(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                eq(mockClazz),
                same(mockMetadata2),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty works with different property metadata instances.
     */
    @Test
    public void testVisitAnyProperty_withDifferentPropertyMetadata_callsCorrectProperty() {
        // Arrange
        KotlinPropertyMetadata mockProperty2 = mock(KotlinPropertyMetadata.class);

        // Act
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockProperty2);

        // Assert - verify that each property metadata instance's methods are called
        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(mockProperty2, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty with null Clazz delegates to property metadata.
     */
    @Test
    public void testVisitAnyProperty_withNullClazz_delegatesToPropertyMetadata() {
        // Act & Assert - should delegate to property metadata
        shrinker.visitAnyProperty(null, mockDeclarationContainerMetadata, mockPropertyMetadata);

        // Verify that setterParametersAccept was called with null clazz
        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                eq(null),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty with null declaration container metadata delegates to property metadata.
     */
    @Test
    public void testVisitAnyProperty_withNullDeclarationContainerMetadata_delegatesToPropertyMetadata() {
        // Act & Assert - should delegate to property metadata
        shrinker.visitAnyProperty(mockClazz, null, mockPropertyMetadata);

        // Verify that setterParametersAccept was called with null metadata
        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(null),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty with null property metadata throws NullPointerException.
     */
    @Test
    public void testVisitAnyProperty_withNullPropertyMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, null);
        }, "Should throw NullPointerException when property metadata is null");
    }

    /**
     * Tests that visitAnyProperty with all null parameters throws NullPointerException.
     */
    @Test
    public void testVisitAnyProperty_withAllNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            shrinker.visitAnyProperty(null, null, null);
        }, "Should throw NullPointerException when property metadata is null");
    }

    /**
     * Tests that multiple instances of KotlinValueParameterNameShrinker behave consistently.
     */
    @Test
    public void testVisitAnyProperty_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinValueParameterNameShrinker shrinker1 = new KotlinValueParameterNameShrinker();
        KotlinValueParameterNameShrinker shrinker2 = new KotlinValueParameterNameShrinker();
        KotlinPropertyMetadata mockProperty1 = mock(KotlinPropertyMetadata.class);
        KotlinPropertyMetadata mockProperty2 = mock(KotlinPropertyMetadata.class);

        // Act
        shrinker1.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockProperty1);
        shrinker2.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockProperty2);

        // Assert - both should make the same calls
        verify(mockProperty1, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(mockProperty2, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty does not interact with the Clazz parameter directly.
     */
    @Test
    public void testVisitAnyProperty_doesNotInteractWithClazz() {
        // Act
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);

        // Assert - verify no direct interactions with clazz
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyProperty does not interact with the declaration container metadata directly.
     */
    @Test
    public void testVisitAnyProperty_doesNotInteractWithDeclarationContainerMetadata() {
        // Act
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);

        // Assert - verify no direct interactions with declaration container metadata
        verifyNoInteractions(mockDeclarationContainerMetadata);
    }

    /**
     * Tests that visitAnyProperty only calls setterParametersAccept on the property metadata.
     */
    @Test
    public void testVisitAnyProperty_onlyCallsSetterParametersAccept() {
        // Act
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);

        // Assert - verify only setterParametersAccept is called
        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verifyNoMoreInteractions(mockPropertyMetadata);
    }

    /**
     * Tests that visitAnyProperty passes correct parameters through.
     */
    @Test
    public void testVisitAnyProperty_passesCorrectParameters() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);
        KotlinDeclarationContainerMetadata specificMetadata = mock(KotlinDeclarationContainerMetadata.class);
        KotlinPropertyMetadata specificProperty = mock(KotlinPropertyMetadata.class);

        // Act
        shrinker.visitAnyProperty(specificClazz, specificMetadata, specificProperty);

        // Assert - verify the exact parameters are passed
        verify(specificProperty, times(1)).setterParametersAccept(
                same(specificClazz),
                same(specificMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty with sequential calls processes each property correctly.
     */
    @Test
    public void testVisitAnyProperty_sequentialCalls_processEachProperty() {
        // Arrange
        KotlinPropertyMetadata property1 = mock(KotlinPropertyMetadata.class);
        KotlinPropertyMetadata property2 = mock(KotlinPropertyMetadata.class);
        KotlinPropertyMetadata property3 = mock(KotlinPropertyMetadata.class);

        // Act
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, property1);
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, property2);
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, property3);

        // Assert - setterParametersAccept should be called for each
        verify(property1, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(property2, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(property3, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty maintains consistent behavior when called with the same parameters multiple times.
     */
    @Test
    public void testVisitAnyProperty_consistentWithSameParameters() {
        // Act - call multiple times with same parameters
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);

        // Assert - should call the method twice (once per invocation)
        verify(mockPropertyMetadata, times(2)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty can handle being called immediately after construction of the shrinker.
     */
    @Test
    public void testVisitAnyProperty_immediatelyAfterConstruction() {
        // Arrange
        KotlinValueParameterNameShrinker newShrinker = new KotlinValueParameterNameShrinker();

        // Act & Assert
        assertDoesNotThrow(() -> {
            newShrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);
        }, "Should be callable immediately after construction");

        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty does not modify the shrinker's state in a way that affects subsequent calls.
     */
    @Test
    public void testVisitAnyProperty_doesNotAffectSubsequentCalls() {
        // Arrange
        KotlinPropertyMetadata property1 = mock(KotlinPropertyMetadata.class);
        KotlinPropertyMetadata property2 = mock(KotlinPropertyMetadata.class);

        // Act - call twice with different properties
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, property1);
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, property2);

        // Assert - both calls should behave independently
        verify(property1, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(property2, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty calls setterParametersAccept even if it's a no-op in the mock.
     */
    @Test
    public void testVisitAnyProperty_callsSetterParametersAcceptRegardlessOfBehavior() {
        // Arrange - configure method to do nothing (default mock behavior)
        doNothing().when(mockPropertyMetadata).setterParametersAccept(any(), any(), any());

        // Act
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);

        // Assert - should still be called
        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty correctly integrates with the visitor pattern.
     */
    @Test
    public void testVisitAnyProperty_asPartOfVisitorPattern() {
        // Arrange
        proguard.classfile.kotlin.visitor.KotlinPropertyVisitor visitor = shrinker;

        // Act
        assertDoesNotThrow(() -> {
            visitor.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);
        }, "Should work when used via interface reference");

        // Assert
        verify(mockPropertyMetadata, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty purely delegates to the property metadata.
     */
    @Test
    public void testVisitAnyProperty_purelyDelegates() {
        // Act
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);

        // Assert - only the delegation call should occur, nothing else
        verify(mockPropertyMetadata).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verifyNoMoreInteractions(mockPropertyMetadata);
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockDeclarationContainerMetadata);
    }

    /**
     * Tests that visitAnyProperty creates a new visitor instance for each call.
     * This is important because MyValueParameterShrinker maintains state (parameterNumber).
     */
    @Test
    public void testVisitAnyProperty_createsNewVisitorInstanceForEachCall() {
        // Arrange
        KotlinPropertyMetadata property1 = mock(KotlinPropertyMetadata.class);
        KotlinPropertyMetadata property2 = mock(KotlinPropertyMetadata.class);

        // Act
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, property1);
        shrinker.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, property2);

        // Assert - each call should receive its own visitor instance
        // We can't directly verify they're different instances through mocks,
        // but we verify each property got its own call
        verify(property1, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(property2, times(1)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty can be called in different contexts without side effects.
     */
    @Test
    public void testVisitAnyProperty_noSideEffectsBetweenCalls() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        KotlinDeclarationContainerMetadata metadata1 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata metadata2 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinPropertyMetadata property1 = mock(KotlinPropertyMetadata.class);
        KotlinPropertyMetadata property2 = mock(KotlinPropertyMetadata.class);

        // Act - call with different combinations of parameters
        shrinker.visitAnyProperty(clazz1, metadata1, property1);
        shrinker.visitAnyProperty(clazz2, metadata2, property2);
        shrinker.visitAnyProperty(clazz1, metadata2, property1);
        shrinker.visitAnyProperty(clazz2, metadata1, property2);

        // Assert - each call should be independent
        verify(property1, times(2)).setterParametersAccept(
                any(Clazz.class),
                any(KotlinDeclarationContainerMetadata.class),
                any(KotlinValueParameterVisitor.class)
        );
        verify(property2, times(2)).setterParametersAccept(
                any(Clazz.class),
                any(KotlinDeclarationContainerMetadata.class),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty works correctly when used through different shrinker instances.
     */
    @Test
    public void testVisitAnyProperty_worksWithDifferentShrinkerInstances() {
        // Arrange
        KotlinValueParameterNameShrinker shrinker1 = new KotlinValueParameterNameShrinker();
        KotlinValueParameterNameShrinker shrinker2 = new KotlinValueParameterNameShrinker();
        KotlinValueParameterNameShrinker shrinker3 = new KotlinValueParameterNameShrinker();

        // Act
        shrinker1.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);
        shrinker2.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);
        shrinker3.visitAnyProperty(mockClazz, mockDeclarationContainerMetadata, mockPropertyMetadata);

        // Assert - all should call setterParametersAccept
        verify(mockPropertyMetadata, times(3)).setterParametersAccept(
                eq(mockClazz),
                eq(mockDeclarationContainerMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyProperty passes all three parameters correctly in a single call.
     */
    @Test
    public void testVisitAnyProperty_passesAllThreeParameters() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);
        KotlinDeclarationContainerMetadata specificMetadata = mock(KotlinDeclarationContainerMetadata.class);
        KotlinPropertyMetadata specificProperty = mock(KotlinPropertyMetadata.class);

        // Act
        shrinker.visitAnyProperty(specificClazz, specificMetadata, specificProperty);

        // Assert - verify all three parameters are passed correctly
        verify(specificProperty, times(1)).setterParametersAccept(
                same(specificClazz),
                same(specificMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }
}
