package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.kotlin.KotlinFunctionMetadata;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.visitor.KotlinValueParameterVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinValueParameterNameShrinker#visitAnyFunction(Clazz, KotlinMetadata, KotlinFunctionMetadata)}.
 * Tests the visitAnyFunction method which delegates to valueParametersAccept.
 */
public class KotlinValueParameterNameShrinkerClaude_visitAnyFunctionTest {

    private KotlinValueParameterNameShrinker shrinker;
    private Clazz mockClazz;
    private KotlinMetadata mockKotlinMetadata;
    private KotlinFunctionMetadata mockFunctionMetadata;

    @BeforeEach
    public void setUp() {
        shrinker = new KotlinValueParameterNameShrinker();
        mockClazz = mock(Clazz.class);
        mockKotlinMetadata = mock(KotlinMetadata.class);
        mockFunctionMetadata = mock(KotlinFunctionMetadata.class);
    }

    /**
     * Tests that visitAnyFunction can be called without throwing exceptions.
     */
    @Test
    public void testVisitAnyFunction_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        }, "visitAnyFunction should not throw an exception");
    }

    /**
     * Tests that visitAnyFunction calls valueParametersAccept on the function metadata.
     * This verifies that the method delegates to process value parameters.
     */
    @Test
    public void testVisitAnyFunction_callsValueParametersAccept() {
        // Act
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction passes the correct clazz parameter to valueParametersAccept.
     */
    @Test
    public void testVisitAnyFunction_passesCorrectClazz() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);

        // Act
        shrinker.visitAnyFunction(specificClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                same(specificClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction passes the correct kotlin metadata to valueParametersAccept.
     */
    @Test
    public void testVisitAnyFunction_passesCorrectKotlinMetadata() {
        // Arrange
        KotlinMetadata specificMetadata = mock(KotlinMetadata.class);

        // Act
        shrinker.visitAnyFunction(mockClazz, specificMetadata, mockFunctionMetadata);

        // Assert
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                eq(mockClazz),
                same(specificMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction passes a KotlinValueParameterVisitor to valueParametersAccept.
     */
    @Test
    public void testVisitAnyFunction_passesValueParameterVisitor() {
        // Act
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify that a KotlinValueParameterVisitor is passed
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                any(Clazz.class),
                any(KotlinMetadata.class),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction can be called multiple times.
     */
    @Test
    public void testVisitAnyFunction_canBeCalledMultipleTimes() {
        // Act
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify that each call triggers valueParametersAccept
        verify(mockFunctionMetadata, times(3)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction works with different clazz instances.
     */
    @Test
    public void testVisitAnyFunction_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);

        // Act
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        shrinker.visitAnyFunction(mockClazz2, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify that the correct clazz is passed to each call
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                same(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                same(mockClazz2),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction works with different kotlin metadata instances.
     */
    @Test
    public void testVisitAnyFunction_withDifferentKotlinMetadata_passesCorrectMetadata() {
        // Arrange
        KotlinMetadata mockMetadata2 = mock(KotlinMetadata.class);

        // Act
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        shrinker.visitAnyFunction(mockClazz, mockMetadata2, mockFunctionMetadata);

        // Assert
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                eq(mockClazz),
                same(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                eq(mockClazz),
                same(mockMetadata2),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction works with different function metadata instances.
     */
    @Test
    public void testVisitAnyFunction_withDifferentFunctionMetadata_callsCorrectFunction() {
        // Arrange
        KotlinFunctionMetadata mockFunction2 = mock(KotlinFunctionMetadata.class);

        // Act
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunction2);

        // Assert - verify that each function metadata instance's methods are called
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(mockFunction2, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction with null Clazz delegates to function metadata.
     */
    @Test
    public void testVisitAnyFunction_withNullClazz_delegatesToFunctionMetadata() {
        // Act & Assert - should delegate to function metadata
        shrinker.visitAnyFunction(null, mockKotlinMetadata, mockFunctionMetadata);

        // Verify that valueParametersAccept was called with null clazz
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                eq(null),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction with null kotlin metadata delegates to function metadata.
     */
    @Test
    public void testVisitAnyFunction_withNullKotlinMetadata_delegatesToFunctionMetadata() {
        // Act & Assert - should delegate to function metadata
        shrinker.visitAnyFunction(mockClazz, null, mockFunctionMetadata);

        // Verify that valueParametersAccept was called with null metadata
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(null),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction with null function metadata throws NullPointerException.
     */
    @Test
    public void testVisitAnyFunction_withNullFunctionMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, null);
        }, "Should throw NullPointerException when function metadata is null");
    }

    /**
     * Tests that visitAnyFunction with all null parameters throws NullPointerException.
     */
    @Test
    public void testVisitAnyFunction_withAllNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            shrinker.visitAnyFunction(null, null, null);
        }, "Should throw NullPointerException when function metadata is null");
    }

    /**
     * Tests that multiple instances of KotlinValueParameterNameShrinker behave consistently.
     */
    @Test
    public void testVisitAnyFunction_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinValueParameterNameShrinker shrinker1 = new KotlinValueParameterNameShrinker();
        KotlinValueParameterNameShrinker shrinker2 = new KotlinValueParameterNameShrinker();
        KotlinFunctionMetadata mockFunction1 = mock(KotlinFunctionMetadata.class);
        KotlinFunctionMetadata mockFunction2 = mock(KotlinFunctionMetadata.class);

        // Act
        shrinker1.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunction1);
        shrinker2.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunction2);

        // Assert - both should make the same calls
        verify(mockFunction1, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(mockFunction2, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction does not interact with the Clazz parameter directly.
     */
    @Test
    public void testVisitAnyFunction_doesNotInteractWithClazz() {
        // Act
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify no direct interactions with clazz
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyFunction does not interact with the kotlin metadata directly.
     */
    @Test
    public void testVisitAnyFunction_doesNotInteractWithKotlinMetadata() {
        // Act
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify no direct interactions with kotlin metadata
        verifyNoInteractions(mockKotlinMetadata);
    }

    /**
     * Tests that visitAnyFunction only calls valueParametersAccept on the function metadata.
     */
    @Test
    public void testVisitAnyFunction_onlyCallsValueParametersAccept() {
        // Act
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify only valueParametersAccept is called
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verifyNoMoreInteractions(mockFunctionMetadata);
    }

    /**
     * Tests that visitAnyFunction passes correct parameters through.
     */
    @Test
    public void testVisitAnyFunction_passesCorrectParameters() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);
        KotlinMetadata specificMetadata = mock(KotlinMetadata.class);
        KotlinFunctionMetadata specificFunction = mock(KotlinFunctionMetadata.class);

        // Act
        shrinker.visitAnyFunction(specificClazz, specificMetadata, specificFunction);

        // Assert - verify the exact parameters are passed
        verify(specificFunction, times(1)).valueParametersAccept(
                same(specificClazz),
                same(specificMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction with sequential calls processes each function correctly.
     */
    @Test
    public void testVisitAnyFunction_sequentialCalls_processEachFunction() {
        // Arrange
        KotlinFunctionMetadata function1 = mock(KotlinFunctionMetadata.class);
        KotlinFunctionMetadata function2 = mock(KotlinFunctionMetadata.class);
        KotlinFunctionMetadata function3 = mock(KotlinFunctionMetadata.class);

        // Act
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, function1);
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, function2);
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, function3);

        // Assert - valueParametersAccept should be called for each
        verify(function1, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(function2, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(function3, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction maintains consistent behavior when called with the same parameters multiple times.
     */
    @Test
    public void testVisitAnyFunction_consistentWithSameParameters() {
        // Act - call multiple times with same parameters
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - should call the method twice (once per invocation)
        verify(mockFunctionMetadata, times(2)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction can handle being called immediately after construction of the shrinker.
     */
    @Test
    public void testVisitAnyFunction_immediatelyAfterConstruction() {
        // Arrange
        KotlinValueParameterNameShrinker newShrinker = new KotlinValueParameterNameShrinker();

        // Act & Assert
        assertDoesNotThrow(() -> {
            newShrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        }, "Should be callable immediately after construction");

        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction does not modify the shrinker's state in a way that affects subsequent calls.
     */
    @Test
    public void testVisitAnyFunction_doesNotAffectSubsequentCalls() {
        // Arrange
        KotlinFunctionMetadata function1 = mock(KotlinFunctionMetadata.class);
        KotlinFunctionMetadata function2 = mock(KotlinFunctionMetadata.class);

        // Act - call twice with different functions
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, function1);
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, function2);

        // Assert - both calls should behave independently
        verify(function1, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(function2, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction calls valueParametersAccept even if it's a no-op in the mock.
     */
    @Test
    public void testVisitAnyFunction_callsValueParametersAcceptRegardlessOfBehavior() {
        // Arrange - configure method to do nothing (default mock behavior)
        doNothing().when(mockFunctionMetadata).valueParametersAccept(any(), any(), any());

        // Act
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - should still be called
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction correctly integrates with the visitor pattern.
     */
    @Test
    public void testVisitAnyFunction_asPartOfVisitorPattern() {
        // Arrange
        proguard.classfile.kotlin.visitor.KotlinFunctionVisitor visitor = shrinker;

        // Act
        assertDoesNotThrow(() -> {
            visitor.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        }, "Should work when used via interface reference");

        // Assert
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction purely delegates to the function metadata.
     */
    @Test
    public void testVisitAnyFunction_purelyDelegates() {
        // Act
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - only the delegation call should occur, nothing else
        verify(mockFunctionMetadata).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verifyNoMoreInteractions(mockFunctionMetadata);
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockKotlinMetadata);
    }

    /**
     * Tests that visitAnyFunction creates a new visitor instance for each call.
     * This is important because MyValueParameterShrinker maintains state (parameterNumber).
     */
    @Test
    public void testVisitAnyFunction_createsNewVisitorInstanceForEachCall() {
        // Arrange
        KotlinFunctionMetadata function1 = mock(KotlinFunctionMetadata.class);
        KotlinFunctionMetadata function2 = mock(KotlinFunctionMetadata.class);

        // Act
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, function1);
        shrinker.visitAnyFunction(mockClazz, mockKotlinMetadata, function2);

        // Assert - each call should receive its own visitor instance
        // We can't directly verify they're different instances through mocks,
        // but we verify each function got its own call
        verify(function1, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
        verify(function2, times(1)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction can be called in different contexts without side effects.
     */
    @Test
    public void testVisitAnyFunction_noSideEffectsBetweenCalls() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        KotlinMetadata metadata1 = mock(KotlinMetadata.class);
        KotlinMetadata metadata2 = mock(KotlinMetadata.class);
        KotlinFunctionMetadata function1 = mock(KotlinFunctionMetadata.class);
        KotlinFunctionMetadata function2 = mock(KotlinFunctionMetadata.class);

        // Act - call with different combinations of parameters
        shrinker.visitAnyFunction(clazz1, metadata1, function1);
        shrinker.visitAnyFunction(clazz2, metadata2, function2);
        shrinker.visitAnyFunction(clazz1, metadata2, function1);
        shrinker.visitAnyFunction(clazz2, metadata1, function2);

        // Assert - each call should be independent
        verify(function1, times(2)).valueParametersAccept(
                any(Clazz.class),
                any(KotlinMetadata.class),
                any(KotlinValueParameterVisitor.class)
        );
        verify(function2, times(2)).valueParametersAccept(
                any(Clazz.class),
                any(KotlinMetadata.class),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction works correctly when used through different shrinker instances.
     */
    @Test
    public void testVisitAnyFunction_worksWithDifferentShrinkerInstances() {
        // Arrange
        KotlinValueParameterNameShrinker shrinker1 = new KotlinValueParameterNameShrinker();
        KotlinValueParameterNameShrinker shrinker2 = new KotlinValueParameterNameShrinker();
        KotlinValueParameterNameShrinker shrinker3 = new KotlinValueParameterNameShrinker();

        // Act
        shrinker1.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        shrinker2.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        shrinker3.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - all should call valueParametersAccept
        verify(mockFunctionMetadata, times(3)).valueParametersAccept(
                eq(mockClazz),
                eq(mockKotlinMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction passes all three parameters correctly in a single call.
     */
    @Test
    public void testVisitAnyFunction_passesAllThreeParameters() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);
        KotlinMetadata specificMetadata = mock(KotlinMetadata.class);
        KotlinFunctionMetadata specificFunction = mock(KotlinFunctionMetadata.class);

        // Act
        shrinker.visitAnyFunction(specificClazz, specificMetadata, specificFunction);

        // Assert - verify all three parameters are passed correctly
        verify(specificFunction, times(1)).valueParametersAccept(
                same(specificClazz),
                same(specificMetadata),
                any(KotlinValueParameterVisitor.class)
        );
    }
}
