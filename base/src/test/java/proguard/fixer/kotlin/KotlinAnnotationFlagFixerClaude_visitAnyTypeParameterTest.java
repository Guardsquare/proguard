package proguard.fixer.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinTypeParameterMetadata;
import proguard.classfile.kotlin.visitor.KotlinTypeVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinAnnotationFlagFixer#visitAnyTypeParameter(Clazz, KotlinTypeParameterMetadata)}.
 * Tests the visitAnyTypeParameter method which delegates to upperBoundsAccept on the type parameter metadata.
 */
public class KotlinAnnotationFlagFixerClaude_visitAnyTypeParameterTest {

    private KotlinAnnotationFlagFixer fixer;
    private Clazz mockClazz;
    private KotlinTypeParameterMetadata mockTypeParameterMetadata;

    @BeforeEach
    public void setUp() {
        fixer = new KotlinAnnotationFlagFixer();
        mockClazz = mock(Clazz.class);
        mockTypeParameterMetadata = mock(KotlinTypeParameterMetadata.class);
    }

    /**
     * Tests that visitAnyTypeParameter can be called without throwing exceptions.
     */
    @Test
    public void testVisitAnyTypeParameter_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata);
        }, "visitAnyTypeParameter should not throw an exception");
    }

    /**
     * Tests that visitAnyTypeParameter calls upperBoundsAccept on the type parameter metadata.
     * This verifies that the method delegates to the type parameter metadata's upper bounds.
     */
    @Test
    public void testVisitAnyTypeParameter_callsUpperBoundsAccept() {
        // Act
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata);

        // Assert
        verify(mockTypeParameterMetadata, times(1)).upperBoundsAccept(eq(mockClazz), any(KotlinTypeVisitor.class));
    }

    /**
     * Tests that visitAnyTypeParameter passes the fixer itself as the visitor.
     * This verifies that the same fixer instance is used for the visitor callback.
     */
    @Test
    public void testVisitAnyTypeParameter_passesFixerAsVisitor() {
        // Act
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata);

        // Assert - verify that the fixer instance is passed as the visitor
        verify(mockTypeParameterMetadata, times(1)).upperBoundsAccept(mockClazz, fixer);
    }

    /**
     * Tests that visitAnyTypeParameter can be called multiple times.
     * This verifies that the method is stateless and can be called repeatedly.
     */
    @Test
    public void testVisitAnyTypeParameter_canBeCalledMultipleTimes() {
        // Act
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata);
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata);
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata);

        // Assert - verify that each call triggers upperBoundsAccept
        verify(mockTypeParameterMetadata, times(3)).upperBoundsAccept(eq(mockClazz), any(KotlinTypeVisitor.class));
    }

    /**
     * Tests that visitAnyTypeParameter works with different clazz instances.
     * This verifies that the method properly passes different clazz parameters.
     */
    @Test
    public void testVisitAnyTypeParameter_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);

        // Act
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata);
        fixer.visitAnyTypeParameter(mockClazz2, mockTypeParameterMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockTypeParameterMetadata, times(1)).upperBoundsAccept(mockClazz, fixer);
        verify(mockTypeParameterMetadata, times(1)).upperBoundsAccept(mockClazz2, fixer);
    }

    /**
     * Tests that visitAnyTypeParameter works with different type parameter metadata instances.
     * This verifies that the method properly handles different metadata parameters.
     */
    @Test
    public void testVisitAnyTypeParameter_withDifferentTypeParameterMetadata_callsCorrectInstance() {
        // Arrange
        KotlinTypeParameterMetadata mockTypeParameterMetadata2 = mock(KotlinTypeParameterMetadata.class);

        // Act
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata);
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata2);

        // Assert - verify that each metadata instance's upperBoundsAccept is called
        verify(mockTypeParameterMetadata, times(1)).upperBoundsAccept(eq(mockClazz), any(KotlinTypeVisitor.class));
        verify(mockTypeParameterMetadata2, times(1)).upperBoundsAccept(eq(mockClazz), any(KotlinTypeVisitor.class));
    }

    /**
     * Tests that visitAnyTypeParameter with null Clazz does not throw but delegates to metadata.
     * The behavior with null parameters depends on the metadata implementation.
     */
    @Test
    public void testVisitAnyTypeParameter_withNullClazz_delegatesToMetadata() {
        // Act
        fixer.visitAnyTypeParameter(null, mockTypeParameterMetadata);

        // Assert - verify that the call was made with null clazz (metadata implementation decides if it's valid)
        verify(mockTypeParameterMetadata, times(1)).upperBoundsAccept(eq(null), any(KotlinTypeVisitor.class));
    }

    /**
     * Tests that visitAnyTypeParameter with null type parameter metadata throws NullPointerException.
     * This is expected since we cannot call methods on a null object.
     */
    @Test
    public void testVisitAnyTypeParameter_withNullTypeParameterMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitAnyTypeParameter(mockClazz, null);
        }, "Should throw NullPointerException when type parameter metadata is null");
    }

    /**
     * Tests that visitAnyTypeParameter with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitAnyTypeParameter_withBothNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitAnyTypeParameter(null, null);
        }, "Should throw NullPointerException when type parameter metadata is null");
    }

    /**
     * Tests that multiple instances of KotlinAnnotationFlagFixer behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitAnyTypeParameter_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinAnnotationFlagFixer fixer1 = new KotlinAnnotationFlagFixer();
        KotlinAnnotationFlagFixer fixer2 = new KotlinAnnotationFlagFixer();
        KotlinTypeParameterMetadata mockTypeParameterMetadata1 = mock(KotlinTypeParameterMetadata.class);
        KotlinTypeParameterMetadata mockTypeParameterMetadata2 = mock(KotlinTypeParameterMetadata.class);

        // Act
        fixer1.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata1);
        fixer2.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata2);

        // Assert - both should make the same calls
        verify(mockTypeParameterMetadata1, times(1)).upperBoundsAccept(eq(mockClazz), any(KotlinTypeVisitor.class));
        verify(mockTypeParameterMetadata2, times(1)).upperBoundsAccept(eq(mockClazz), any(KotlinTypeVisitor.class));
    }

    /**
     * Tests that visitAnyTypeParameter passes both parameters correctly in the same call.
     * This verifies the complete parameter passing workflow.
     */
    @Test
    public void testVisitAnyTypeParameter_passesBothParametersCorrectly() {
        // Act
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata);

        // Assert - verify both clazz and fixer are passed correctly
        verify(mockTypeParameterMetadata, times(1)).upperBoundsAccept(mockClazz, fixer);
        verifyNoMoreInteractions(mockTypeParameterMetadata);
    }

    /**
     * Tests that visitAnyTypeParameter only calls upperBoundsAccept once per invocation.
     * This verifies that the method doesn't make duplicate calls.
     */
    @Test
    public void testVisitAnyTypeParameter_callsUpperBoundsAcceptOnlyOnce() {
        // Act
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata);

        // Assert - verify exactly one call
        verify(mockTypeParameterMetadata, times(1)).upperBoundsAccept(any(Clazz.class), any(KotlinTypeVisitor.class));
        verifyNoMoreInteractions(mockTypeParameterMetadata);
    }

    /**
     * Tests that visitAnyTypeParameter works correctly when called in sequence with different parameters.
     * This verifies that the method properly handles a sequence of different invocations.
     */
    @Test
    public void testVisitAnyTypeParameter_sequentialCallsWithDifferentParameters() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);
        Clazz mockClazz3 = mock(Clazz.class);
        KotlinTypeParameterMetadata mockTypeParameterMetadata2 = mock(KotlinTypeParameterMetadata.class);
        KotlinTypeParameterMetadata mockTypeParameterMetadata3 = mock(KotlinTypeParameterMetadata.class);

        // Act
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata);
        fixer.visitAnyTypeParameter(mockClazz2, mockTypeParameterMetadata2);
        fixer.visitAnyTypeParameter(mockClazz3, mockTypeParameterMetadata3);

        // Assert - verify each call was made with the correct parameters
        verify(mockTypeParameterMetadata, times(1)).upperBoundsAccept(mockClazz, fixer);
        verify(mockTypeParameterMetadata2, times(1)).upperBoundsAccept(mockClazz2, fixer);
        verify(mockTypeParameterMetadata3, times(1)).upperBoundsAccept(mockClazz3, fixer);
    }

    /**
     * Tests that visitAnyTypeParameter does not modify the state of the fixer.
     * This verifies that the method is side-effect free with respect to the fixer's state.
     */
    @Test
    public void testVisitAnyTypeParameter_doesNotModifyFixerState() {
        // Arrange
        KotlinTypeParameterMetadata mockTypeParameterMetadata2 = mock(KotlinTypeParameterMetadata.class);

        // Act - call with first metadata
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata);

        // Act - call with second metadata (should behave identically)
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata2);

        // Assert - both calls should behave the same way, indicating no state change
        verify(mockTypeParameterMetadata, times(1)).upperBoundsAccept(mockClazz, fixer);
        verify(mockTypeParameterMetadata2, times(1)).upperBoundsAccept(mockClazz, fixer);
    }

    /**
     * Tests that visitAnyTypeParameter passes the exact same fixer instance to upperBoundsAccept.
     * This verifies that a consistent visitor instance is used.
     */
    @Test
    public void testVisitAnyTypeParameter_passesConsistentFixerInstance() {
        // Arrange
        KotlinTypeParameterMetadata mockTypeParameterMetadata2 = mock(KotlinTypeParameterMetadata.class);
        KotlinTypeParameterMetadata mockTypeParameterMetadata3 = mock(KotlinTypeParameterMetadata.class);

        // Act
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata);
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata2);
        fixer.visitAnyTypeParameter(mockClazz, mockTypeParameterMetadata3);

        // Assert - verify that the same fixer instance is passed to all calls
        verify(mockTypeParameterMetadata, times(1)).upperBoundsAccept(mockClazz, fixer);
        verify(mockTypeParameterMetadata2, times(1)).upperBoundsAccept(mockClazz, fixer);
        verify(mockTypeParameterMetadata3, times(1)).upperBoundsAccept(mockClazz, fixer);
    }
}
