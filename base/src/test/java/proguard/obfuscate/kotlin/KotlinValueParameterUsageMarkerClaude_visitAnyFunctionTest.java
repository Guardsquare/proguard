package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.kotlin.KotlinFunctionMetadata;
import proguard.classfile.kotlin.KotlinMetadata;
import proguard.classfile.kotlin.visitor.KotlinValueParameterVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.util.ProcessingFlags;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinValueParameterUsageMarker#visitAnyFunction(Clazz, KotlinMetadata, KotlinFunctionMetadata)}.
 * Tests the visitAnyFunction method which checks if the referenced method should not be obfuscated,
 * and if so, marks the value parameters as used.
 */
public class KotlinValueParameterUsageMarkerClaude_visitAnyFunctionTest {

    private KotlinValueParameterUsageMarker marker;
    private Clazz mockClazz;
    private KotlinMetadata mockKotlinMetadata;
    private KotlinFunctionMetadata mockFunctionMetadata;
    private ProgramMethod mockProgramMethod;
    private ProgramClass mockProgramClass;

    @BeforeEach
    public void setUp() {
        marker = new KotlinValueParameterUsageMarker();
        mockClazz = mock(Clazz.class);
        mockKotlinMetadata = mock(KotlinMetadata.class);
        mockFunctionMetadata = mock(KotlinFunctionMetadata.class);
        mockProgramMethod = mock(ProgramMethod.class);
        mockProgramClass = mock(ProgramClass.class);
    }

    /**
     * Tests that visitAnyFunction can be called without throwing exceptions.
     */
    @Test
    public void testVisitAnyFunction_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        }, "visitAnyFunction should not throw an exception");
    }

    /**
     * Tests that visitAnyFunction calls referencedMethodAccept on the function metadata.
     * This verifies that the method checks the referenced method.
     */
    @Test
    public void testVisitAnyFunction_callsReferencedMethodAccept() {
        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert
        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyFunction passes the marker itself to referencedMethodAccept.
     * This verifies that the marker is used as the member visitor.
     */
    @Test
    public void testVisitAnyFunction_passesMarkerToReferencedMethodAccept() {
        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify that the marker instance is passed as the visitor
        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(marker);
    }

    /**
     * Tests that visitAnyFunction does NOT call valueParametersAccept when the referenced method
     * does not have the DONT_OBFUSCATE flag (default behavior with mocks).
     */
    @Test
    public void testVisitAnyFunction_doesNotCallValueParametersAccept_whenMethodCanBeObfuscated() {
        // Arrange - mock returns null for referencedMethodAccept (no method to visit)
        doNothing().when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - valueParametersAccept should NOT be called
        verify(mockFunctionMetadata, never()).valueParametersAccept(any(), any(), any());
    }

    /**
     * Tests that visitAnyFunction calls valueParametersAccept when the referenced method
     * has the DONT_OBFUSCATE flag set.
     */
    @Test
    public void testVisitAnyFunction_callsValueParametersAccept_whenMethodHasDontObfuscateFlag() {
        // Arrange - set up the referenced method with DONT_OBFUSCATE flag
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - valueParametersAccept should be called
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(
            eq(mockClazz),
            eq(mockKotlinMetadata),
            any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitAnyFunction passes correct parameters to valueParametersAccept.
     */
    @Test
    public void testVisitAnyFunction_passesCorrectParametersToValueParametersAccept() {
        // Arrange
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify exact parameters
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(mockClazz, mockKotlinMetadata, marker);
    }

    /**
     * Tests that visitAnyFunction does NOT call valueParametersAccept when the method
     * has other flags but not DONT_OBFUSCATE.
     */
    @Test
    public void testVisitAnyFunction_doesNotCallValueParametersAccept_whenMethodHasOtherFlags() {
        // Arrange - set up with a different flag (not DONT_OBFUSCATE)
        when(mockProgramMethod.getProcessingFlags()).thenReturn(0x0001); // Some other flag
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - valueParametersAccept should NOT be called
        verify(mockFunctionMetadata, never()).valueParametersAccept(any(), any(), any());
    }

    /**
     * Tests that visitAnyFunction can be called multiple times and each call is independent.
     */
    @Test
    public void testVisitAnyFunction_canBeCalledMultipleTimes() {
        // Arrange
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - both methods should be called 3 times
        verify(mockFunctionMetadata, times(3)).referencedMethodAccept(marker);
        verify(mockFunctionMetadata, times(3)).valueParametersAccept(mockClazz, mockKotlinMetadata, marker);
    }

    /**
     * Tests that visitAnyFunction with null Clazz delegates to metadata.
     */
    @Test
    public void testVisitAnyFunction_withNullClazz_delegatesToMetadata() {
        // Act
        marker.visitAnyFunction(null, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - referencedMethodAccept should still be called
        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(marker);
    }

    /**
     * Tests that visitAnyFunction with null function metadata throws NullPointerException.
     */
    @Test
    public void testVisitAnyFunction_withNullFunctionMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitAnyFunction(mockClazz, mockKotlinMetadata, null);
        }, "Should throw NullPointerException when function metadata is null");
    }

    /**
     * Tests that visitAnyFunction does not interact with Clazz directly.
     */
    @Test
    public void testVisitAnyFunction_doesNotInteractWithClazz() {
        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify no direct interactions with clazz
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyFunction works with different clazz instances.
     */
    @Test
    public void testVisitAnyFunction_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        marker.visitAnyFunction(mockClazz2, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify correct clazz is passed to each call
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(mockClazz, mockKotlinMetadata, marker);
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(mockClazz2, mockKotlinMetadata, marker);
    }

    /**
     * Tests that visitAnyFunction works with different kotlin metadata instances.
     */
    @Test
    public void testVisitAnyFunction_withDifferentKotlinMetadata_passesCorrectMetadata() {
        // Arrange
        KotlinMetadata mockKotlinMetadata2 = mock(KotlinMetadata.class);
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata2, mockFunctionMetadata);

        // Assert - verify correct kotlin metadata is passed
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(mockClazz, mockKotlinMetadata, marker);
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(mockClazz, mockKotlinMetadata2, marker);
    }

    /**
     * Tests that visitAnyFunction works with different function metadata instances.
     */
    @Test
    public void testVisitAnyFunction_withDifferentFunctionMetadata_callsCorrectMetadata() {
        // Arrange
        KotlinFunctionMetadata mockFunctionMetadata2 = mock(KotlinFunctionMetadata.class);

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata2);

        // Assert - verify each metadata instance's methods are called
        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(marker);
        verify(mockFunctionMetadata2, times(1)).referencedMethodAccept(marker);
    }

    /**
     * Tests that multiple marker instances behave consistently.
     */
    @Test
    public void testVisitAnyFunction_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinValueParameterUsageMarker marker1 = new KotlinValueParameterUsageMarker();
        KotlinValueParameterUsageMarker marker2 = new KotlinValueParameterUsageMarker();
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker1.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        marker2.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - both should behave the same
        verify(mockFunctionMetadata, times(2)).referencedMethodAccept(any(MemberVisitor.class));
        verify(mockFunctionMetadata, times(2)).valueParametersAccept(eq(mockClazz), eq(mockKotlinMetadata), any(KotlinValueParameterVisitor.class));
    }

    /**
     * Tests that visitAnyFunction calls referencedMethodAccept before valueParametersAccept.
     * This verifies the correct order of operations.
     */
    @Test
    public void testVisitAnyFunction_callsReferencedMethodAcceptBeforeValueParametersAccept() {
        // Arrange
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        org.mockito.InOrder inOrder = inOrder(mockFunctionMetadata);

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify order
        inOrder.verify(mockFunctionMetadata).referencedMethodAccept(marker);
        inOrder.verify(mockFunctionMetadata).valueParametersAccept(mockClazz, mockKotlinMetadata, marker);
    }

    /**
     * Tests that visitAnyFunction can handle being called immediately after construction.
     */
    @Test
    public void testVisitAnyFunction_immediatelyAfterConstruction() {
        // Arrange
        KotlinValueParameterUsageMarker newMarker = new KotlinValueParameterUsageMarker();

        // Act & Assert
        assertDoesNotThrow(() -> {
            newMarker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        }, "Should be callable immediately after construction");

        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(newMarker);
    }

    /**
     * Tests that visitAnyFunction works correctly via interface reference.
     */
    @Test
    public void testVisitAnyFunction_asPartOfVisitorPattern() {
        // Arrange
        proguard.classfile.kotlin.visitor.KotlinFunctionVisitor visitor = marker;

        // Act
        assertDoesNotThrow(() -> {
            visitor.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        }, "Should work when used via interface reference");

        // Assert
        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(marker);
    }

    /**
     * Tests that visitAnyFunction resets keepParameterInfo flag on each call.
     * This ensures each function is evaluated independently.
     */
    @Test
    public void testVisitAnyFunction_resetsKeepParameterInfoFlag_eachCall() {
        // Arrange - first call sets flag to true
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        KotlinFunctionMetadata mockFunctionMetadata2 = mock(KotlinFunctionMetadata.class);
        // Second function has no method reference (doesn't set flag)

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata2);

        // Assert - first should call valueParametersAccept, second should not
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(mockClazz, mockKotlinMetadata, marker);
        verify(mockFunctionMetadata2, never()).valueParametersAccept(any(), any(), any());
    }

    /**
     * Tests that visitAnyFunction handles the case where referencedMethodAccept
     * calls visitAnyMember instead of visitProgramMethod.
     */
    @Test
    public void testVisitAnyFunction_withVisitAnyMember_doesNotCallValueParametersAccept() {
        // Arrange - set up to call visitAnyMember (which does nothing)
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitAnyMember(mockClazz, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - valueParametersAccept should NOT be called (visitAnyMember is a no-op)
        verify(mockFunctionMetadata, never()).valueParametersAccept(any(), any(), any());
    }

    /**
     * Tests that visitAnyFunction with method having flags including DONT_OBFUSCATE
     * still calls valueParametersAccept.
     */
    @Test
    public void testVisitAnyFunction_withMultipleFlags_includingDontObfuscate_callsValueParametersAccept() {
        // Arrange - set multiple flags including DONT_OBFUSCATE
        int multipleFlags = ProcessingFlags.DONT_OBFUSCATE | 0x0001 | 0x0002;
        when(mockProgramMethod.getProcessingFlags()).thenReturn(multipleFlags);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - valueParametersAccept should be called
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(mockClazz, mockKotlinMetadata, marker);
    }

    /**
     * Tests that visitAnyFunction returns void as expected.
     */
    @Test
    public void testVisitAnyFunction_returnsVoid() {
        // Act - method returns void, so just verify it executes
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - if we reach here without exception, the method completed successfully
        assertTrue(true, "Method should complete and return void");
    }

    /**
     * Tests the conditional logic: valueParametersAccept is called only if keepParameterInfo is true.
     */
    @Test
    public void testVisitAnyFunction_conditionalLogic_valueParametersAcceptOnlyIfFlagSet() {
        // Arrange - two functions: one with flag, one without
        KotlinFunctionMetadata functionWithFlag = mock(KotlinFunctionMetadata.class);
        KotlinFunctionMetadata functionWithoutFlag = mock(KotlinFunctionMetadata.class);

        ProgramMethod methodWithFlag = mock(ProgramMethod.class);
        when(methodWithFlag.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, methodWithFlag);
            return null;
        }).when(functionWithFlag).referencedMethodAccept(any());

        // functionWithoutFlag has no answer, so no callback happens

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, functionWithFlag);
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, functionWithoutFlag);

        // Assert
        verify(functionWithFlag, times(1)).valueParametersAccept(mockClazz, mockKotlinMetadata, marker);
        verify(functionWithoutFlag, never()).valueParametersAccept(any(), any(), any());
    }

    /**
     * Tests that visitAnyFunction only calls the expected methods on function metadata.
     */
    @Test
    public void testVisitAnyFunction_onlyCallsExpectedMethods_whenFlagNotSet() {
        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - only referencedMethodAccept should be called, not valueParametersAccept
        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(marker);
        verify(mockFunctionMetadata, never()).valueParametersAccept(any(), any(), any());
        verifyNoMoreInteractions(mockFunctionMetadata);
    }

    /**
     * Tests that visitAnyFunction calls both expected methods when flag is set.
     */
    @Test
    public void testVisitAnyFunction_callsBothMethods_whenFlagSet() {
        // Arrange
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - both methods should be called
        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(marker);
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(mockClazz, mockKotlinMetadata, marker);
        verifyNoMoreInteractions(mockFunctionMetadata);
    }

    /**
     * Tests that visitAnyFunction handles rapid successive calls correctly.
     */
    @Test
    public void testVisitAnyFunction_rapidSuccessiveCalls() {
        // Arrange
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act - call 10 times
        for (int i = 0; i < 10; i++) {
            marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);
        }

        // Assert - both methods should be called 10 times
        verify(mockFunctionMetadata, times(10)).referencedMethodAccept(marker);
        verify(mockFunctionMetadata, times(10)).valueParametersAccept(mockClazz, mockKotlinMetadata, marker);
    }

    /**
     * Tests that visitAnyFunction properly implements the KotlinFunctionVisitor interface contract.
     */
    @Test
    public void testVisitAnyFunction_implementsVisitorContract() {
        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify that the visitor methods are properly called
        verify(mockFunctionMetadata, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyFunction's conditional behavior depends on the referenced method's flags.
     */
    @Test
    public void testVisitAnyFunction_conditionalBehavior_dependsOnMethodFlags() {
        // Arrange - create two scenarios
        ProgramMethod obfuscatableMethod = mock(ProgramMethod.class);
        when(obfuscatableMethod.getProcessingFlags()).thenReturn(0);

        ProgramMethod nonObfuscatableMethod = mock(ProgramMethod.class);
        when(nonObfuscatableMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        KotlinFunctionMetadata function1 = mock(KotlinFunctionMetadata.class);
        KotlinFunctionMetadata function2 = mock(KotlinFunctionMetadata.class);

        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, obfuscatableMethod);
            return null;
        }).when(function1).referencedMethodAccept(any());

        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, nonObfuscatableMethod);
            return null;
        }).when(function2).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, function1);
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, function2);

        // Assert - only function2 should call valueParametersAccept
        verify(function1, never()).valueParametersAccept(any(), any(), any());
        verify(function2, times(1)).valueParametersAccept(mockClazz, mockKotlinMetadata, marker);
    }

    /**
     * Tests that visitAnyFunction does not interact with KotlinMetadata directly.
     */
    @Test
    public void testVisitAnyFunction_doesNotInteractWithKotlinMetadata() {
        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify no direct interactions with kotlin metadata
        verifyNoInteractions(mockKotlinMetadata);
    }

    /**
     * Tests that visitAnyFunction with null kotlin metadata passes it through correctly.
     */
    @Test
    public void testVisitAnyFunction_withNullKotlinMetadata_passesToValueParametersAccept() {
        // Arrange
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, null, mockFunctionMetadata);

        // Assert - null kotlin metadata should be passed to valueParametersAccept
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(mockClazz, null, marker);
    }

    /**
     * Tests that visitAnyFunction with all null parameters throws NullPointerException
     * when trying to call referencedMethodAccept on null function metadata.
     */
    @Test
    public void testVisitAnyFunction_withAllNullParameters_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitAnyFunction(null, null, null);
        }, "Should throw NullPointerException when function metadata is null");
    }

    /**
     * Tests that visitAnyFunction correctly implements the visitor pattern
     * by acting as a MemberVisitor when visiting the referenced method.
     */
    @Test
    public void testVisitAnyFunction_actsAsMemberVisitor() {
        // Arrange
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            // Verify that the visitor passed is the marker itself
            assertSame(marker, visitor, "The visitor should be the marker instance");
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - the assertion in the doAnswer above verifies the behavior
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(mockClazz, mockKotlinMetadata, marker);
    }

    /**
     * Tests that visitAnyFunction handles sequential calls with alternating flag states.
     */
    @Test
    public void testVisitAnyFunction_alternatingFlagStates() {
        // Arrange
        ProgramMethod obfuscatableMethod = mock(ProgramMethod.class);
        when(obfuscatableMethod.getProcessingFlags()).thenReturn(0);

        ProgramMethod nonObfuscatableMethod = mock(ProgramMethod.class);
        when(nonObfuscatableMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        KotlinFunctionMetadata function1 = mock(KotlinFunctionMetadata.class);
        KotlinFunctionMetadata function2 = mock(KotlinFunctionMetadata.class);
        KotlinFunctionMetadata function3 = mock(KotlinFunctionMetadata.class);

        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, nonObfuscatableMethod);
            return null;
        }).when(function1).referencedMethodAccept(any());

        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, obfuscatableMethod);
            return null;
        }).when(function2).referencedMethodAccept(any());

        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, nonObfuscatableMethod);
            return null;
        }).when(function3).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, function1); // Should call valueParametersAccept
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, function2); // Should NOT call valueParametersAccept
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, function3); // Should call valueParametersAccept

        // Assert
        verify(function1, times(1)).valueParametersAccept(mockClazz, mockKotlinMetadata, marker);
        verify(function2, never()).valueParametersAccept(any(), any(), any());
        verify(function3, times(1)).valueParametersAccept(mockClazz, mockKotlinMetadata, marker);
    }

    /**
     * Tests that visitAnyFunction does not throw when all parameters are valid mocks.
     */
    @Test
    public void testVisitAnyFunction_withValidMocks_doesNotThrow() {
        // Arrange
        Clazz validClazz = mock(Clazz.class);
        KotlinMetadata validMetadata = mock(KotlinMetadata.class);
        KotlinFunctionMetadata validFunction = mock(KotlinFunctionMetadata.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marker.visitAnyFunction(validClazz, validMetadata, validFunction);
        }, "Should not throw with valid mocks");
    }

    /**
     * Tests that visitAnyFunction passes the clazz parameter correctly to valueParametersAccept.
     */
    @Test
    public void testVisitAnyFunction_passesClazzCorrectly() {
        // Arrange
        Clazz specificClazz = mock(Clazz.class);
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(specificClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify the exact clazz instance is passed
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(same(specificClazz), any(), any());
    }

    /**
     * Tests that visitAnyFunction passes the kotlin metadata parameter correctly to valueParametersAccept.
     */
    @Test
    public void testVisitAnyFunction_passesKotlinMetadataCorrectly() {
        // Arrange
        KotlinMetadata specificMetadata = mock(KotlinMetadata.class);
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, specificMetadata, mockFunctionMetadata);

        // Assert - verify the exact kotlin metadata instance is passed
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(any(), same(specificMetadata), any());
    }

    /**
     * Tests that visitAnyFunction passes the marker as the visitor to valueParametersAccept.
     */
    @Test
    public void testVisitAnyFunction_passesMarkerAsVisitor() {
        // Arrange
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(0);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockFunctionMetadata).referencedMethodAccept(any());

        // Act
        marker.visitAnyFunction(mockClazz, mockKotlinMetadata, mockFunctionMetadata);

        // Assert - verify the marker itself is passed as the visitor
        verify(mockFunctionMetadata, times(1)).valueParametersAccept(any(), any(), same(marker));
    }
}
