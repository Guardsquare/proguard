package proguard.mark;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.Constant;
import proguard.classfile.kotlin.KotlinMetadata;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link Marker.KotlinDontOptimizeMarker#visitAnyKotlinMetadata(Clazz, KotlinMetadata)}.
 * Tests the visitAnyKotlinMetadata method which is a no-op implementation.
 */
public class MarkerClaude_visitAnyKotlinMetadataTest {

    private Marker.KotlinDontOptimizeMarker marker;
    private Clazz mockClazz;
    private KotlinMetadata mockKotlinMetadata;

    @BeforeEach
    public void setUp() {
        marker = new Marker.KotlinDontOptimizeMarker();
        mockClazz = mock(Clazz.class);
        mockKotlinMetadata = mock(KotlinMetadata.class);
    }

    /**
     * Tests that visitAnyKotlinMetadata can be called without throwing exceptions.
     * This is a no-op method, so it should simply return without doing anything.
     */
    @Test
    public void testVisitAnyKotlinMetadata_doesNotThrowException() {
        assertDoesNotThrow(() -> {
            marker.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
        }, "visitAnyKotlinMetadata should not throw an exception");
    }

    /**
     * Tests that visitAnyKotlinMetadata with null Clazz parameter does not throw exception.
     * The method is a no-op, so it should handle null parameters gracefully.
     */
    @Test
    public void testVisitAnyKotlinMetadata_withNullClazz_doesNotThrowException() {
        assertDoesNotThrow(() -> {
            marker.visitAnyKotlinMetadata(null, mockKotlinMetadata);
        }, "visitAnyKotlinMetadata should handle null Clazz");
    }

    /**
     * Tests that visitAnyKotlinMetadata with null KotlinMetadata parameter does not throw exception.
     * The method is a no-op, so it should handle null parameters gracefully.
     */
    @Test
    public void testVisitAnyKotlinMetadata_withNullKotlinMetadata_doesNotThrowException() {
        assertDoesNotThrow(() -> {
            marker.visitAnyKotlinMetadata(mockClazz, null);
        }, "visitAnyKotlinMetadata should handle null KotlinMetadata");
    }

    /**
     * Tests that visitAnyKotlinMetadata with both null parameters does not throw exception.
     * The method is a no-op, so it should handle null parameters gracefully.
     */
    @Test
    public void testVisitAnyKotlinMetadata_withBothNull_doesNotThrowException() {
        assertDoesNotThrow(() -> {
            marker.visitAnyKotlinMetadata(null, null);
        }, "visitAnyKotlinMetadata should handle both null parameters");
    }

    /**
     * Tests that visitAnyKotlinMetadata does not interact with the Clazz parameter.
     * Since this is a no-op method, it should not call any methods on the Clazz.
     */
    @Test
    public void testVisitAnyKotlinMetadata_doesNotInteractWithClazz() {
        marker.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);

        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyKotlinMetadata does not interact with the KotlinMetadata parameter.
     * Since this is a no-op method, it should not call any methods on the KotlinMetadata.
     */
    @Test
    public void testVisitAnyKotlinMetadata_doesNotInteractWithKotlinMetadata() {
        marker.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);

        verifyNoInteractions(mockKotlinMetadata);
    }

    /**
     * Tests that visitAnyKotlinMetadata can be called multiple times without issue.
     * The method is a no-op, so multiple calls should be safe.
     */
    @Test
    public void testVisitAnyKotlinMetadata_canBeCalledMultipleTimes() {
        assertDoesNotThrow(() -> {
            marker.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
            marker.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
            marker.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
        }, "visitAnyKotlinMetadata should handle multiple calls");
    }

    /**
     * Tests visitAnyKotlinMetadata with a real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitAnyKotlinMetadata_withRealProgramClass() {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;
        programClass.constantPool = new Constant[10];

        assertDoesNotThrow(() -> {
            marker.visitAnyKotlinMetadata(programClass, mockKotlinMetadata);
        }, "visitAnyKotlinMetadata should work with real ProgramClass");
    }

    /**
     * Tests that visitAnyKotlinMetadata completes quickly.
     * Since this is a no-op method, it should return immediately.
     */
    @Test
    public void testVisitAnyKotlinMetadata_completesImmediately() {
        long startTime = System.nanoTime();

        marker.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);

        long duration = System.nanoTime() - startTime;
        assertTrue(duration < 1_000_000,
                   "visitAnyKotlinMetadata should complete immediately, took " + duration + " ns");
    }

    /**
     * Tests that multiple instances of KotlinDontOptimizeMarker behave the same way
     * when calling visitAnyKotlinMetadata.
     */
    @Test
    public void testVisitAnyKotlinMetadata_consistentBehaviorAcrossInstances() {
        Marker.KotlinDontOptimizeMarker marker1 = new Marker.KotlinDontOptimizeMarker();
        Marker.KotlinDontOptimizeMarker marker2 = new Marker.KotlinDontOptimizeMarker();

        assertDoesNotThrow(() -> {
            marker1.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
            marker2.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
        }, "All instances should behave the same way");

        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockKotlinMetadata);
    }

    /**
     * Tests that visitAnyKotlinMetadata can be called in sequence with different parameters.
     */
    @Test
    public void testVisitAnyKotlinMetadata_withDifferentParameters() {
        Clazz mockClazz2 = mock(Clazz.class);
        KotlinMetadata mockMetadata2 = mock(KotlinMetadata.class);

        assertDoesNotThrow(() -> {
            marker.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);
            marker.visitAnyKotlinMetadata(mockClazz2, mockMetadata2);
            marker.visitAnyKotlinMetadata(mockClazz, mockMetadata2);
            marker.visitAnyKotlinMetadata(mockClazz2, mockKotlinMetadata);
        }, "visitAnyKotlinMetadata should handle different parameter combinations");
    }

    /**
     * Tests that the method maintains its no-op behavior regardless of the state of the marker.
     */
    @Test
    public void testVisitAnyKotlinMetadata_maintainsNoOpBehavior() {
        marker.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);

        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockKotlinMetadata);

        marker.visitAnyKotlinMetadata(mockClazz, mockKotlinMetadata);

        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockKotlinMetadata);
    }
}
