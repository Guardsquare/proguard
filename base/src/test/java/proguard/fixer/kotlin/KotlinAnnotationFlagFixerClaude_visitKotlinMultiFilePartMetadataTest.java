package proguard.fixer.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinMultiFilePartKindMetadata;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinPropertyVisitor;
import proguard.classfile.kotlin.visitor.KotlinTypeAliasVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinAnnotationFlagFixer#visitKotlinMultiFilePartMetadata(Clazz, KotlinMultiFilePartKindMetadata)}.
 * Tests the visitKotlinMultiFilePartMetadata method which delegates to visitKotlinDeclarationContainerMetadata.
 */
public class KotlinAnnotationFlagFixerClaude_visitKotlinMultiFilePartMetadataTest {

    private KotlinAnnotationFlagFixer fixer;
    private Clazz mockClazz;
    private KotlinMultiFilePartKindMetadata mockMetadata;

    @BeforeEach
    public void setUp() {
        fixer = new KotlinAnnotationFlagFixer();
        mockClazz = mock(Clazz.class);
        mockMetadata = mock(KotlinMultiFilePartKindMetadata.class);
    }

    /**
     * Tests that visitKotlinMultiFilePartMetadata can be called without throwing exceptions.
     */
    @Test
    public void testVisitKotlinMultiFilePartMetadata_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            fixer.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata);
        }, "visitKotlinMultiFilePartMetadata should not throw an exception");
    }

    /**
     * Tests that visitKotlinMultiFilePartMetadata delegates to propertiesAccept.
     * This verifies that the method properly delegates to visitKotlinDeclarationContainerMetadata.
     */
    @Test
    public void testVisitKotlinMultiFilePartMetadata_delegatesToPropertiesAccept() {
        // Act
        fixer.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata);

        // Assert - verify delegation to declaration container processing
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinMultiFilePartMetadata delegates to functionsAccept.
     * This verifies that the method properly delegates to visitKotlinDeclarationContainerMetadata.
     */
    @Test
    public void testVisitKotlinMultiFilePartMetadata_delegatesToFunctionsAccept() {
        // Act
        fixer.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata);

        // Assert - verify delegation to declaration container processing
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinMultiFilePartMetadata delegates to typeAliasesAccept.
     * This verifies that the method properly delegates to visitKotlinDeclarationContainerMetadata.
     */
    @Test
    public void testVisitKotlinMultiFilePartMetadata_delegatesToTypeAliasesAccept() {
        // Act
        fixer.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata);

        // Assert - verify delegation to declaration container processing
        verify(mockMetadata, times(1)).typeAliasesAccept(eq(mockClazz), any(KotlinTypeAliasVisitor.class));
    }

    /**
     * Tests that visitKotlinMultiFilePartMetadata delegates to delegatedPropertiesAccept.
     * This verifies that the method properly delegates to visitKotlinDeclarationContainerMetadata.
     */
    @Test
    public void testVisitKotlinMultiFilePartMetadata_delegatesToDelegatedPropertiesAccept() {
        // Act
        fixer.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata);

        // Assert - verify delegation to declaration container processing
        verify(mockMetadata, times(1)).delegatedPropertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinMultiFilePartMetadata calls all accept methods.
     * This verifies the complete delegation workflow to visitKotlinDeclarationContainerMetadata.
     */
    @Test
    public void testVisitKotlinMultiFilePartMetadata_delegatesToAllAcceptMethods() {
        // Act
        fixer.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata);

        // Assert - verify all four methods are called (through delegation)
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(1)).typeAliasesAccept(eq(mockClazz), any(KotlinTypeAliasVisitor.class));
        verify(mockMetadata, times(1)).delegatedPropertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinMultiFilePartMetadata passes the fixer itself as the visitor.
     * This verifies that the same fixer instance is used for all visitor callbacks.
     */
    @Test
    public void testVisitKotlinMultiFilePartMetadata_passesFixerAsVisitor() {
        // Act
        fixer.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata);

        // Assert - verify that the fixer instance is passed as the visitor
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, fixer);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, fixer);
        verify(mockMetadata, times(1)).typeAliasesAccept(mockClazz, fixer);
        verify(mockMetadata, times(1)).delegatedPropertiesAccept(mockClazz, fixer);
    }

    /**
     * Tests that visitKotlinMultiFilePartMetadata can be called multiple times.
     * This verifies that the method is stateless and can be called repeatedly.
     */
    @Test
    public void testVisitKotlinMultiFilePartMetadata_canBeCalledMultipleTimes() {
        // Act
        fixer.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata);

        // Assert - verify that each call triggers all the accept methods
        verify(mockMetadata, times(3)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        verify(mockMetadata, times(3)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(3)).typeAliasesAccept(eq(mockClazz), any(KotlinTypeAliasVisitor.class));
        verify(mockMetadata, times(3)).delegatedPropertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinMultiFilePartMetadata works with different clazz instances.
     * This verifies that the method properly passes different clazz parameters.
     */
    @Test
    public void testVisitKotlinMultiFilePartMetadata_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);

        // Act
        fixer.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinMultiFilePartMetadata(mockClazz2, mockMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, fixer);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz2, fixer);
    }

    /**
     * Tests that visitKotlinMultiFilePartMetadata works with different metadata instances.
     * This verifies that the method properly handles different metadata parameters.
     */
    @Test
    public void testVisitKotlinMultiFilePartMetadata_withDifferentMetadata_callsCorrectMetadata() {
        // Arrange
        KotlinMultiFilePartKindMetadata mockMetadata2 = mock(KotlinMultiFilePartKindMetadata.class);

        // Act
        fixer.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata2);

        // Assert - verify that each metadata instance's methods are called
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        verify(mockMetadata2, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinMultiFilePartMetadata with null Clazz delegates to metadata.
     * The behavior with null parameters depends on the metadata implementation.
     */
    @Test
    public void testVisitKotlinMultiFilePartMetadata_withNullClazz_delegatesToMetadata() {
        // Act
        fixer.visitKotlinMultiFilePartMetadata(null, mockMetadata);

        // Assert - verify that the calls were made with null clazz
        verify(mockMetadata, times(1)).propertiesAccept(eq(null), any(KotlinPropertyVisitor.class));
        verify(mockMetadata, times(1)).functionsAccept(eq(null), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(1)).typeAliasesAccept(eq(null), any(KotlinTypeAliasVisitor.class));
        verify(mockMetadata, times(1)).delegatedPropertiesAccept(eq(null), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinMultiFilePartMetadata with null metadata throws NullPointerException.
     * This is expected since we cannot call methods on a null object.
     */
    @Test
    public void testVisitKotlinMultiFilePartMetadata_withNullMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitKotlinMultiFilePartMetadata(mockClazz, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitKotlinMultiFilePartMetadata with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitKotlinMultiFilePartMetadata_withBothNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitKotlinMultiFilePartMetadata(null, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that multiple instances of KotlinAnnotationFlagFixer behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitKotlinMultiFilePartMetadata_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinAnnotationFlagFixer fixer1 = new KotlinAnnotationFlagFixer();
        KotlinAnnotationFlagFixer fixer2 = new KotlinAnnotationFlagFixer();
        KotlinMultiFilePartKindMetadata mockMetadata1 = mock(KotlinMultiFilePartKindMetadata.class);
        KotlinMultiFilePartKindMetadata mockMetadata2 = mock(KotlinMultiFilePartKindMetadata.class);

        // Act
        fixer1.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata1);
        fixer2.visitKotlinMultiFilePartMetadata(mockClazz, mockMetadata2);

        // Assert - both should make the same calls
        verify(mockMetadata1, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        verify(mockMetadata1, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata1, times(1)).typeAliasesAccept(eq(mockClazz), any(KotlinTypeAliasVisitor.class));
        verify(mockMetadata1, times(1)).delegatedPropertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));

        verify(mockMetadata2, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).typeAliasesAccept(eq(mockClazz), any(KotlinTypeAliasVisitor.class));
        verify(mockMetadata2, times(1)).delegatedPropertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }
}
