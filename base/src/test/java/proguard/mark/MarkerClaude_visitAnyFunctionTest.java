package proguard.mark;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.Constant;
import proguard.classfile.kotlin.KotlinFunctionMetadata;
import proguard.classfile.kotlin.KotlinMetadata;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link Marker.KotlinDontOptimizeMarker#visitAnyFunction(Clazz, KotlinMetadata, KotlinFunctionMetadata)}.
 * Tests the visitAnyFunction method which is a no-op implementation.
 */
public class MarkerClaude_visitAnyFunctionTest {

    private Marker.KotlinDontOptimizeMarker marker;
    private Clazz mockClazz;
    private KotlinMetadata mockKotlinMetadata;
    private KotlinFunctionMetadata mockKotlinFunctionMetadata;

    @BeforeEach
    public void setUp() {
        marker = new Marker.KotlinDontOptimizeMarker();
        mockClazz = mock(Clazz.class);
        mockKotlinMetadata = mock(KotlinMetadata.class);
        mockKotlinFunctionMetadata = mock(KotlinFunctionMetadata.class);
    }

    /**
     * Tests that visitAnyFunction can be called without throwing exceptions.
     * This is a no-op method, so it should simply return without doing anything.
     */
    @Test
    public void testVisitAnyFunction_doesNotThrowException() {
        assertDoesNotThrow(() -> {
            marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);
        }, "visitAnyFunction should not throw an exception");
    }

    /**
     * Tests that visitAnyFunction with null Clazz parameter does not throw exception.
     * The method is a no-op, so it should handle null parameters gracefully.
     */
    @Test
    public void testVisitAnyFunction_withNullClazz_doesNotThrowException() {
        assertDoesNotThrow(() -> {
            marker.visitAnyFunction(null, mockKotlinMetadata, mockKotlinFunctionMetadata);
        }, "visitAnyFunction should handle null Clazz");
    }

    /**
     * Tests that visitAnyFunction with null KotlinMetadata parameter does not throw exception.
     * The method is a no-op, so it should handle null parameters gracefully.
     */
    @Test
    public void testVisitAnyFunction_withNullKotlinMetadata_doesNotThrowException() {
        assertDoesNotThrow(() -> {
            marker.visitAnyFunction(mockClazz, null, mockKotlinFunctionMetadata);
        }, "visitAnyFunction should handle null KotlinMetadata");
    }

    /**
     * Tests that visitAnyFunction with null KotlinFunctionMetadata parameter does not throw exception.
     * The method is a no-op, so it should handle null parameters gracefully.
     */
    @Test
    public void testVisitAnyFunction_withNullKotlinFunctionMetadata_doesNotThrowException() {
        assertDoesNotThrow(() -> {
            marker.visitAnyFunction(mockClazz, mockKotlinMetadata, null);
        }, "visitAnyFunction should handle null KotlinFunctionMetadata");
    }

    /**
     * Tests that visitAnyFunction with all null parameters does not throw exception.
     * The method is a no-op, so it should handle null parameters gracefully.
     */
    @Test
    public void testVisitAnyFunction_withAllNull_doesNotThrowException() {
        assertDoesNotThrow(() -> {
            marker.visitAnyFunction(null, null, null);
        }, "visitAnyFunction should handle all null parameters");
    }

    /**
     * Tests that visitAnyFunction does not interact with the Clazz parameter.
     * Since this is a no-op method, it should not call any methods on the Clazz.
     */
    @Test
    public void testVisitAnyFunction_doesNotInteractWithClazz() {
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);

        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyFunction does not interact with the KotlinMetadata parameter.
     * Since this is a no-op method, it should not call any methods on the KotlinMetadata.
     */
    @Test
    public void testVisitAnyFunction_doesNotInteractWithKotlinMetadata() {
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);

        verifyNoInteractions(mockKotlinMetadata);
    }

    /**
     * Tests that visitAnyFunction does not interact with the KotlinFunctionMetadata parameter.
     * Since this is a no-op method, it should not call any methods on the KotlinFunctionMetadata.
     */
    @Test
    public void testVisitAnyFunction_doesNotInteractWithKotlinFunctionMetadata() {
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);

        verifyNoInteractions(mockKotlinFunctionMetadata);
    }

    /**
     * Tests that visitAnyFunction can be called multiple times without issue.
     * The method is a no-op, so multiple calls should be safe.
     */
    @Test
    public void testVisitAnyFunction_canBeCalledMultipleTimes() {
        assertDoesNotThrow(() -> {
            marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);
            marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);
            marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);
        }, "visitAnyFunction should handle multiple calls");
    }

    /**
     * Tests visitAnyFunction with a real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitAnyFunction_withRealProgramClass() {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;
        programClass.constantPool = new Constant[10];

        assertDoesNotThrow(() -> {
            marker.visitAnyFunction(programClass, mockKotlinMetadata, mockKotlinFunctionMetadata);
        }, "visitAnyFunction should work with real ProgramClass");
    }

    /**
     * Tests that visitAnyFunction completes quickly.
     * Since this is a no-op method, it should return immediately.
     */
    @Test
    public void testVisitAnyFunction_completesImmediately() {
        long startTime = System.nanoTime();

        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);

        long duration = System.nanoTime() - startTime;
        assertTrue(duration < 1_000_000,
                   "visitAnyFunction should complete immediately, took " + duration + " ns");
    }

    /**
     * Tests that multiple instances of KotlinDontOptimizeMarker behave the same way
     * when calling visitAnyFunction.
     */
    @Test
    public void testVisitAnyFunction_consistentBehaviorAcrossInstances() {
        Marker.KotlinDontOptimizeMarker marker1 = new Marker.KotlinDontOptimizeMarker();
        Marker.KotlinDontOptimizeMarker marker2 = new Marker.KotlinDontOptimizeMarker();

        assertDoesNotThrow(() -> {
            marker1.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);
            marker2.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);
        }, "All instances should behave the same way");

        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockKotlinMetadata);
        verifyNoInteractions(mockKotlinFunctionMetadata);
    }

    /**
     * Tests that visitAnyFunction can be called in sequence with different parameters.
     */
    @Test
    public void testVisitAnyFunction_withDifferentParameters() {
        Clazz mockClazz2 = mock(Clazz.class);
        KotlinMetadata mockMetadata2 = mock(KotlinMetadata.class);
        KotlinFunctionMetadata mockFunctionMetadata2 = mock(KotlinFunctionMetadata.class);

        assertDoesNotThrow(() -> {
            marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);
            marker.visitAnyFunction(mockClazz2, mockMetadata2, mockFunctionMetadata2);
            marker.visitAnyFunction(mockClazz, mockMetadata2, mockFunctionMetadata2);
            marker.visitAnyFunction(mockClazz2, mockKotlinMetadata, mockKotlinFunctionMetadata);
        }, "visitAnyFunction should handle different parameter combinations");
    }

    /**
     * Tests that the method maintains its no-op behavior regardless of the state of the marker.
     */
    @Test
    public void testVisitAnyFunction_maintainsNoOpBehavior() {
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);

        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockKotlinMetadata);
        verifyNoInteractions(mockKotlinFunctionMetadata);

        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);

        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockKotlinMetadata);
        verifyNoInteractions(mockKotlinFunctionMetadata);
    }

    /**
     * Tests that visitAnyFunction can be called with different combinations of null and non-null parameters.
     */
    @Test
    public void testVisitAnyFunction_withMixedNullParameters() {
        assertDoesNotThrow(() -> {
            marker.visitAnyFunction(mockClazz, null, null);
            marker.visitAnyFunction(null, mockKotlinMetadata, null);
            marker.visitAnyFunction(null, null, mockKotlinFunctionMetadata);
            marker.visitAnyFunction(mockClazz, mockKotlinMetadata, null);
            marker.visitAnyFunction(mockClazz, null, mockKotlinFunctionMetadata);
            marker.visitAnyFunction(null, mockKotlinMetadata, mockKotlinFunctionMetadata);
        }, "visitAnyFunction should handle mixed null/non-null parameters");
    }

    /**
     * Tests that sequential calls to visitAnyFunction do not affect each other.
     */
    @Test
    public void testVisitAnyFunction_sequentialCallsAreIndependent() {
        Clazz mockClazz2 = mock(Clazz.class);
        KotlinMetadata mockMetadata2 = mock(KotlinMetadata.class);
        KotlinFunctionMetadata mockFunctionMetadata2 = mock(KotlinFunctionMetadata.class);

        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);
        marker.visitAnyFunction(mockClazz2, mockMetadata2, mockFunctionMetadata2);

        // Verify no interactions with any of the mocks
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockKotlinMetadata);
        verifyNoInteractions(mockKotlinFunctionMetadata);
        verifyNoInteractions(mockClazz2);
        verifyNoInteractions(mockMetadata2);
        verifyNoInteractions(mockFunctionMetadata2);
    }

    /**
     * Tests that visitAnyFunction works correctly when the marker is used as a KotlinFunctionVisitor.
     * This verifies that the method can be called through the interface.
     */
    @Test
    public void testVisitAnyFunction_throughInterfaceReference() {
        proguard.classfile.kotlin.visitor.KotlinFunctionVisitor visitor = marker;

        assertDoesNotThrow(() -> {
            visitor.visitAnyFunction(mockClazz, mockKotlinMetadata, mockKotlinFunctionMetadata);
        }, "visitAnyFunction should work when called through interface reference");

        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockKotlinMetadata);
        verifyNoInteractions(mockKotlinFunctionMetadata);
    }
}
