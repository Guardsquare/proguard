package proguard.fixer.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinFileFacadeKindMetadata;
import proguard.classfile.kotlin.visitor.KotlinFunctionVisitor;
import proguard.classfile.kotlin.visitor.KotlinPropertyVisitor;
import proguard.classfile.kotlin.visitor.KotlinTypeAliasVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinAnnotationFlagFixer#visitKotlinFileFacadeMetadata(Clazz, KotlinFileFacadeKindMetadata)}.
 * Tests the visitKotlinFileFacadeMetadata method which delegates to visitKotlinDeclarationContainerMetadata.
 */
public class KotlinAnnotationFlagFixerClaude_visitKotlinFileFacadeMetadataTest {

    private KotlinAnnotationFlagFixer fixer;
    private Clazz mockClazz;
    private KotlinFileFacadeKindMetadata mockMetadata;

    @BeforeEach
    public void setUp() {
        fixer = new KotlinAnnotationFlagFixer();
        mockClazz = mock(Clazz.class);
        mockMetadata = mock(KotlinFileFacadeKindMetadata.class);
    }

    /**
     * Tests that visitKotlinFileFacadeMetadata can be called without throwing exceptions.
     */
    @Test
    public void testVisitKotlinFileFacadeMetadata_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            fixer.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata);
        }, "visitKotlinFileFacadeMetadata should not throw an exception");
    }

    /**
     * Tests that visitKotlinFileFacadeMetadata delegates to propertiesAccept.
     * This verifies that the method properly delegates to visitKotlinDeclarationContainerMetadata.
     */
    @Test
    public void testVisitKotlinFileFacadeMetadata_delegatesToPropertiesAccept() {
        // Act
        fixer.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata);

        // Assert - verify delegation to declaration container processing
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinFileFacadeMetadata delegates to functionsAccept.
     * This verifies that the method properly delegates to visitKotlinDeclarationContainerMetadata.
     */
    @Test
    public void testVisitKotlinFileFacadeMetadata_delegatesToFunctionsAccept() {
        // Act
        fixer.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata);

        // Assert - verify delegation to declaration container processing
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinFileFacadeMetadata delegates to typeAliasesAccept.
     * This verifies that the method properly delegates to visitKotlinDeclarationContainerMetadata.
     */
    @Test
    public void testVisitKotlinFileFacadeMetadata_delegatesToTypeAliasesAccept() {
        // Act
        fixer.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata);

        // Assert - verify delegation to declaration container processing
        verify(mockMetadata, times(1)).typeAliasesAccept(eq(mockClazz), any(KotlinTypeAliasVisitor.class));
    }

    /**
     * Tests that visitKotlinFileFacadeMetadata delegates to delegatedPropertiesAccept.
     * This verifies that the method properly delegates to visitKotlinDeclarationContainerMetadata.
     */
    @Test
    public void testVisitKotlinFileFacadeMetadata_delegatesToDelegatedPropertiesAccept() {
        // Act
        fixer.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata);

        // Assert - verify delegation to declaration container processing
        verify(mockMetadata, times(1)).delegatedPropertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinFileFacadeMetadata calls all accept methods.
     * This verifies the complete delegation workflow to visitKotlinDeclarationContainerMetadata.
     */
    @Test
    public void testVisitKotlinFileFacadeMetadata_delegatesToAllAcceptMethods() {
        // Act
        fixer.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata);

        // Assert - verify all four methods are called (through delegation)
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(1)).typeAliasesAccept(eq(mockClazz), any(KotlinTypeAliasVisitor.class));
        verify(mockMetadata, times(1)).delegatedPropertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinFileFacadeMetadata passes the fixer itself as the visitor.
     * This verifies that the same fixer instance is used for all visitor callbacks.
     */
    @Test
    public void testVisitKotlinFileFacadeMetadata_passesFixerAsVisitor() {
        // Act
        fixer.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata);

        // Assert - verify that the fixer instance is passed as the visitor
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, fixer);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, fixer);
        verify(mockMetadata, times(1)).typeAliasesAccept(mockClazz, fixer);
        verify(mockMetadata, times(1)).delegatedPropertiesAccept(mockClazz, fixer);
    }

    /**
     * Tests that visitKotlinFileFacadeMetadata can be called multiple times.
     * This verifies that the method is stateless and can be called repeatedly.
     */
    @Test
    public void testVisitKotlinFileFacadeMetadata_canBeCalledMultipleTimes() {
        // Act
        fixer.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata);

        // Assert - verify that each call triggers all the accept methods
        verify(mockMetadata, times(3)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        verify(mockMetadata, times(3)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(3)).typeAliasesAccept(eq(mockClazz), any(KotlinTypeAliasVisitor.class));
        verify(mockMetadata, times(3)).delegatedPropertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinFileFacadeMetadata works with different clazz instances.
     * This verifies that the method properly passes different clazz parameters.
     */
    @Test
    public void testVisitKotlinFileFacadeMetadata_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);

        // Act
        fixer.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinFileFacadeMetadata(mockClazz2, mockMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz, fixer);
        verify(mockMetadata, times(1)).propertiesAccept(mockClazz2, fixer);
    }

    /**
     * Tests that visitKotlinFileFacadeMetadata works with different metadata instances.
     * This verifies that the method properly handles different metadata parameters.
     */
    @Test
    public void testVisitKotlinFileFacadeMetadata_withDifferentMetadata_callsCorrectMetadata() {
        // Arrange
        KotlinFileFacadeKindMetadata mockMetadata2 = mock(KotlinFileFacadeKindMetadata.class);

        // Act
        fixer.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata);
        fixer.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata2);

        // Assert - verify that each metadata instance's methods are called
        verify(mockMetadata, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
        verify(mockMetadata2, times(1)).propertiesAccept(eq(mockClazz), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinFileFacadeMetadata with null Clazz delegates to metadata.
     * The behavior with null parameters depends on the metadata implementation.
     */
    @Test
    public void testVisitKotlinFileFacadeMetadata_withNullClazz_delegatesToMetadata() {
        // Act
        fixer.visitKotlinFileFacadeMetadata(null, mockMetadata);

        // Assert - verify that the calls were made with null clazz
        verify(mockMetadata, times(1)).propertiesAccept(eq(null), any(KotlinPropertyVisitor.class));
        verify(mockMetadata, times(1)).functionsAccept(eq(null), any(KotlinFunctionVisitor.class));
        verify(mockMetadata, times(1)).typeAliasesAccept(eq(null), any(KotlinTypeAliasVisitor.class));
        verify(mockMetadata, times(1)).delegatedPropertiesAccept(eq(null), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinFileFacadeMetadata with null metadata throws NullPointerException.
     * This is expected since we cannot call methods on a null object.
     */
    @Test
    public void testVisitKotlinFileFacadeMetadata_withNullMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitKotlinFileFacadeMetadata(mockClazz, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitKotlinFileFacadeMetadata with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitKotlinFileFacadeMetadata_withBothNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            fixer.visitKotlinFileFacadeMetadata(null, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that multiple instances of KotlinAnnotationFlagFixer behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitKotlinFileFacadeMetadata_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinAnnotationFlagFixer fixer1 = new KotlinAnnotationFlagFixer();
        KotlinAnnotationFlagFixer fixer2 = new KotlinAnnotationFlagFixer();
        KotlinFileFacadeKindMetadata mockMetadata1 = mock(KotlinFileFacadeKindMetadata.class);
        KotlinFileFacadeKindMetadata mockMetadata2 = mock(KotlinFileFacadeKindMetadata.class);

        // Act
        fixer1.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata1);
        fixer2.visitKotlinFileFacadeMetadata(mockClazz, mockMetadata2);

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
