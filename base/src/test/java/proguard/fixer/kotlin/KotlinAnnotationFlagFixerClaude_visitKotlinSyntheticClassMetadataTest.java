package proguard.fixer.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinSyntheticClassKindMetadata;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinAnnotationFlagFixer#visitKotlinSyntheticClassMetadata(Clazz, KotlinSyntheticClassKindMetadata)}.
 * Tests the visitKotlinSyntheticClassMetadata method which processes synthetic class metadata by visiting its functions.
 */
public class KotlinAnnotationFlagFixerClaude_visitKotlinSyntheticClassMetadataTest {

    private KotlinAnnotationFlagFixer fixer;
    private Clazz mockClazz;
    private KotlinSyntheticClassKindMetadata mockMetadata;

    @BeforeEach
    public void setUp() {
        fixer = new KotlinAnnotationFlagFixer();
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
            fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        }, "visitKotlinSyntheticClassMetadata should not throw an exception");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata calls functionsAccept on the metadata.
     * This is the primary behavior of this method.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_callsFunctionsAccept() {
        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that functionsAccept was called
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata passes the fixer itself as the visitor.
     * This verifies that the fixer instance is used for processing functions.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_passesFixerAsVisitor() {
        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that the fixer instance is passed as the function visitor
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, fixer);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata passes the correct clazz parameter.
     * This verifies that the clazz parameter is correctly forwarded.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_passesCorrectClazz() {
        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that mockClazz was passed to functionsAccept
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, fixer);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata can be called multiple times.
     * This verifies that the method is stateless and can be called repeatedly.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_canBeCalledMultipleTimes() {
        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify that functionsAccept was called three times
        verify(mockMetadata, times(3)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata works with different clazz instances.
     * This verifies that the method properly handles different clazz parameters.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);

        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinSyntheticClassMetadata(mockClazz2, mockMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, fixer);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz2, fixer);
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
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata2);

        // Assert - verify that each metadata instance's functionsAccept was called
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with null clazz still calls functionsAccept.
     * The behavior with null parameters depends on the metadata implementation.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withNullClazz_callsFunctionsAccept() {
        // Act
        fixer.visitKotlinSyntheticClassMetadata(null, mockMetadata);

        // Assert - verify that functionsAccept was called with null clazz
        verify(mockMetadata, times(1)).functionsAccept(null, fixer);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with null metadata throws NullPointerException.
     * This is expected since we cannot call methods on a null object.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withNullMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitKotlinSyntheticClassMetadata(mockClazz, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_withBothNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitKotlinSyntheticClassMetadata(null, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that multiple instances of KotlinAnnotationFlagFixer behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinAnnotationFlagFixer fixer1 = new KotlinAnnotationFlagFixer();
        KotlinAnnotationFlagFixer fixer2 = new KotlinAnnotationFlagFixer();
        KotlinSyntheticClassKindMetadata mockMetadata1 = mock(KotlinSyntheticClassKindMetadata.class);
        KotlinSyntheticClassKindMetadata mockMetadata2 = mock(KotlinSyntheticClassKindMetadata.class);

        // Act
        fixer1.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata1);
        fixer2.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata2);

        // Assert - both should make the same call
        verify(mockMetadata1, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata only calls functionsAccept and no other methods.
     * This verifies that only function processing occurs for synthetic classes.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_onlyCallsFunctionsAccept() {
        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify only functionsAccept was called, no other interactions
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, fixer);
        verifyNoMoreInteractions(mockMetadata);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata does not interact with the clazz parameter directly.
     * The clazz should only be passed to the metadata's functionsAccept method.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_doesNotInteractWithClazz() {
        // Act
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify no direct interactions with clazz (only passed as parameter)
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata behavior is predictable with sequential calls.
     * This verifies that each call is independent and produces the same result.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_sequentialCallsBehavePredictably() {
        // Act - make sequential calls with the same parameters
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify each call resulted in one functionsAccept call
        verify(mockMetadata, times(2)).functionsAccept(mockClazz, fixer);
    }

    /**
     * Tests that visitKotlinSyntheticClassMetadata works correctly when called on different fixer instances
     * with the same metadata object.
     */
    @Test
    public void testVisitKotlinSyntheticClassMetadata_differentFixersSameMetadata() {
        // Arrange
        KotlinAnnotationFlagFixer fixer1 = new KotlinAnnotationFlagFixer();
        KotlinAnnotationFlagFixer fixer2 = new KotlinAnnotationFlagFixer();

        // Act
        fixer1.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);
        fixer2.visitKotlinSyntheticClassMetadata(mockClazz, mockMetadata);

        // Assert - verify functionsAccept was called twice, once with each fixer
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, fixer1);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, fixer2);
        verify(mockMetadata, times(2)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }
}
