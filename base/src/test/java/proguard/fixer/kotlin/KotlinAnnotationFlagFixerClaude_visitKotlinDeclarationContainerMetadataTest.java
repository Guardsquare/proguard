package proguard.fixer.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinDeclarationContainerMetadata;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinPropertyVisitor;
import proguard.classfile.kotlin.visitor.KotlinTypeAliasVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinAnnotationFlagFixer#visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)}.
 * Tests the visitKotlinDeclarationContainerMetadata method which delegates to various accept methods.
 */
public class KotlinAnnotationFlagFixerClaude_visitKotlinDeclarationContainerMetadataTest {

    private KotlinAnnotationFlagFixer fixer;
    private Clazz mockClazz;
    private KotlinDeclarationContainerMetadata mockMetadata;

    @BeforeEach
    public void setUp() {
        fixer = new KotlinAnnotationFlagFixer();
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
            fixer.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        }, "visitKotlinDeclarationContainerMetadata should not throw an exception");
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls propertiesAccept on the metadata.
     * This verifies that the method delegates to process properties.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsPropertiesAccept() {
        // Act
        fixer.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls functionsAccept on the metadata.
     * This verifies that the method delegates to process functions.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsFunctionsAccept() {
        // Act
        fixer.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls typeAliasesAccept on the metadata.
     * This verifies that the method delegates to process type aliases.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsTypeAliasesAccept() {
        // Act
        fixer.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert
        verify(mockMetadata, times(1)).typeAliasesAccept(eq(mockClazz), any(KotlinTypeAliasVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls delegatedPropertiesAccept on the metadata.
     * This verifies that the method delegates to process delegated properties.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsDelegatedPropertiesAccept() {
        // Act
        fixer.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert
        verify(mockMetadata, times(1)).delegatedPropertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls all accept methods in the correct order.
     * This verifies the complete delegation workflow.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsAllAcceptMethods() {
        // Act
        fixer.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify all four methods are called
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(1)).typeAliasesAccept(eq(mockClazz), any(KotlinTypeAliasVisitor.class));
        verify(mockMetadata, times(1)).delegatedPropertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata passes the fixer itself as the visitor.
     * This verifies that the same fixer instance is used for all visitor callbacks.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_passesFixerAsVisitor() {
        // Act
        fixer.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify that the fixer instance is passed as the visitor
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, fixer);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, fixer);
        verify(mockMetadata, times(1)).typeAliasesAccept(mockClazz, fixer);
        verify(mockMetadata, times(1)).delegatedPropertiesAccept(mockClazz, fixer);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata can be called multiple times.
     * This verifies that the method is stateless and can be called repeatedly.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_canBeCalledMultipleTimes() {
        // Act
        fixer.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Assert - verify that each call triggers all the accept methods
        verify(mockMetadata, times(3)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        verify(mockMetadata, times(3)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(3)).typeAliasesAccept(eq(mockClazz), any(KotlinTypeAliasVisitor.class));
        verify(mockMetadata, times(3)).delegatedPropertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
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
        fixer.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinDeclarationContainerMetadata(mockClazz2, mockMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, fixer);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz2, fixer);
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
        fixer.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata2);

        // Assert - verify that each metadata instance's methods are called
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        verify(mockMetadata2, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with null Clazz does not throw but may fail.
     * The behavior with null parameters depends on the metadata implementation.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withNullClazz_delegatesToMetadata() {
        // Act & Assert - should delegate to metadata (may or may not throw depending on metadata implementation)
        fixer.visitKotlinDeclarationContainerMetadata(null, mockMetadata);

        // Verify that the calls were made with null clazz (metadata implementation decides if it's valid)
        verify(mockMetadata, times(1)).propertiesAccept(eq(null), any(KotlinPropertyVisitor.class));
        verify(mockMetadata, times(1)).functionsAccept(eq(null), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(1)).typeAliasesAccept(eq(null), any(KotlinTypeAliasVisitor.class));
        verify(mockMetadata, times(1)).delegatedPropertiesAccept(eq(null), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with null metadata throws NullPointerException.
     * This is expected since we cannot call methods on a null object.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withNullMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitKotlinDeclarationContainerMetadata(mockClazz, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withBothNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitKotlinDeclarationContainerMetadata(null, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that multiple instances of KotlinAnnotationFlagFixer behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinAnnotationFlagFixer fixer1 = new KotlinAnnotationFlagFixer();
        KotlinAnnotationFlagFixer fixer2 = new KotlinAnnotationFlagFixer();
        KotlinDeclarationContainerMetadata mockMetadata1 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata mockMetadata2 = mock(KotlinDeclarationContainerMetadata.class);

        // Act
        fixer1.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata1);
        fixer2.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata2);

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
