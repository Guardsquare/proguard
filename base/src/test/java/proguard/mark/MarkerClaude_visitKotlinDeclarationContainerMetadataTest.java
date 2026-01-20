package proguard.mark;

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
 * Test class for {@link Marker.KotlinDontOptimizeMarker#visitKotlinDeclarationContainerMetadata(Clazz, KotlinDeclarationContainerMetadata)}.
 * Tests the visitKotlinDeclarationContainerMetadata method which delegates to functionsAccept.
 */
public class MarkerClaude_visitKotlinDeclarationContainerMetadataTest {

    private Marker.KotlinDontOptimizeMarker marker;
    private Clazz mockClazz;
    private KotlinDeclarationContainerMetadata mockMetadata;

    @BeforeEach
    public void setUp() {
        marker = new Marker.KotlinDontOptimizeMarker();
        mockClazz = mock(Clazz.class);
        mockMetadata = mock(KotlinDeclarationContainerMetadata.class);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata can be called without throwing exceptions.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_doesNotThrowException() {
        assertDoesNotThrow(() -> {
            marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        }, "visitKotlinDeclarationContainerMetadata should not throw an exception");
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata calls functionsAccept on the metadata.
     * This verifies that the method delegates to process functions.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_callsFunctionsAccept() {
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata passes the marker itself as the visitor.
     * This verifies that the same marker instance is used for the visitor callback.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_passesMarkerAsVisitor() {
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata does NOT call propertiesAccept.
     * The KotlinDontOptimizeMarker only processes functions, not properties.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_doesNotCallPropertiesAccept() {
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        verify(mockMetadata, never()).propertiesAccept(any(), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata does NOT call typeAliasesAccept.
     * The KotlinDontOptimizeMarker only processes functions, not type aliases.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_doesNotCallTypeAliasesAccept() {
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        verify(mockMetadata, never()).typeAliasesAccept(any(), any(KotlinTypeAliasVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata does NOT call delegatedPropertiesAccept.
     * The KotlinDontOptimizeMarker only processes functions, not delegated properties.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_doesNotCallDelegatedPropertiesAccept() {
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        verify(mockMetadata, never()).delegatedPropertiesAccept(any(), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata only calls functionsAccept and nothing else.
     * This verifies the complete delegation workflow for this specific implementation.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_onlyCallsFunctionsAccept() {
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Verify functionsAccept is called exactly once
        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));

        // Verify other methods are never called
        verify(mockMetadata, never()).propertiesAccept(any(), any(KotlinPropertyVisitor.class));
        verify(mockMetadata, never()).typeAliasesAccept(any(), any(KotlinTypeAliasVisitor.class));
        verify(mockMetadata, never()).delegatedPropertiesAccept(any(), any(KotlinPropertyVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata can be called multiple times.
     * This verifies that the method is stateless and can be called repeatedly.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_canBeCalledMultipleTimes() {
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        verify(mockMetadata, times(3)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata works with different clazz instances.
     * This verifies that the method properly passes different clazz parameters.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withDifferentClazz_passesCorrectClazz() {
        Clazz mockClazz2 = mock(Clazz.class);

        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz2, mockMetadata);

        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz2, marker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata works with different metadata instances.
     * This verifies that the method properly handles different metadata parameters.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withDifferentMetadata_callsCorrectMetadata() {
        KotlinDeclarationContainerMetadata mockMetadata2 = mock(KotlinDeclarationContainerMetadata.class);

        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata2);

        verify(mockMetadata, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with null Clazz delegates to metadata.
     * The behavior with null parameters depends on the metadata implementation.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withNullClazz_delegatesToMetadata() {
        marker.visitKotlinDeclarationContainerMetadata(null, mockMetadata);

        verify(mockMetadata, times(1)).functionsAccept(eq(null), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with null metadata throws NullPointerException.
     * This is expected since we cannot call methods on a null object.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withNullMetadata_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            marker.visitKotlinDeclarationContainerMetadata(mockClazz, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_withBothNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            marker.visitKotlinDeclarationContainerMetadata(null, null);
        }, "Should throw NullPointerException when metadata is null");
    }

    /**
     * Tests that multiple instances of KotlinDontOptimizeMarker behave consistently.
     * This verifies that the behavior is not instance-specific.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_consistentBehaviorAcrossInstances() {
        Marker.KotlinDontOptimizeMarker marker1 = new Marker.KotlinDontOptimizeMarker();
        Marker.KotlinDontOptimizeMarker marker2 = new Marker.KotlinDontOptimizeMarker();
        KotlinDeclarationContainerMetadata mockMetadata1 = mock(KotlinDeclarationContainerMetadata.class);
        KotlinDeclarationContainerMetadata mockMetadata2 = mock(KotlinDeclarationContainerMetadata.class);

        marker1.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata1);
        marker2.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata2);

        verify(mockMetadata1, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
        verify(mockMetadata2, times(1)).functionsAccept(eq(mockClazz), any(KotlinFunctionVisitor.class));
    }

    /**
     * Tests that the method is idempotent when called with the same parameters.
     * Each call should independently trigger functionsAccept.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_isIdempotent() {
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);

        // Each call should trigger functionsAccept
        verify(mockMetadata, times(2)).functionsAccept(mockClazz, marker);
    }

    /**
     * Tests that visitKotlinDeclarationContainerMetadata works correctly in sequence with varying parameters.
     */
    @Test
    public void testVisitKotlinDeclarationContainerMetadata_sequentialCallsWithVaryingParameters() {
        Clazz mockClazz2 = mock(Clazz.class);
        KotlinDeclarationContainerMetadata mockMetadata2 = mock(KotlinDeclarationContainerMetadata.class);

        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz2, mockMetadata);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz, mockMetadata2);
        marker.visitKotlinDeclarationContainerMetadata(mockClazz2, mockMetadata2);

        // Verify each combination was called correctly
        verify(mockMetadata, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata, times(1)).functionsAccept(mockClazz2, marker);
        verify(mockMetadata2, times(1)).functionsAccept(mockClazz, marker);
        verify(mockMetadata2, times(1)).functionsAccept(mockClazz2, marker);
    }
}
