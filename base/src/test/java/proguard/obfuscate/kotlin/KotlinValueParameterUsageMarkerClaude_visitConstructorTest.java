package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.kotlin.KotlinClassKindMetadata;
import proguard.classfile.kotlin.KotlinConstructorMetadata;
import proguard.classfile.kotlin.visitor.KotlinValueParameterVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.util.ProcessingFlags;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinValueParameterUsageMarker#visitConstructor(Clazz, KotlinClassKindMetadata, KotlinConstructorMetadata)}.
 * Tests the visitConstructor method which checks if the referenced method should not be obfuscated,
 * and if so, marks the value parameters as used.
 */
public class KotlinValueParameterUsageMarkerClaude_visitConstructorTest {

    private KotlinValueParameterUsageMarker marker;
    private Clazz mockClazz;
    private KotlinClassKindMetadata mockClassMetadata;
    private KotlinConstructorMetadata mockConstructorMetadata;
    private ProgramMethod mockProgramMethod;
    private ProgramClass mockProgramClass;

    @BeforeEach
    public void setUp() {
        marker = new KotlinValueParameterUsageMarker();
        mockClazz = mock(Clazz.class);
        mockClassMetadata = mock(KotlinClassKindMetadata.class);
        mockConstructorMetadata = mock(KotlinConstructorMetadata.class);
        mockProgramMethod = mock(ProgramMethod.class);
        mockProgramClass = mock(ProgramClass.class);
    }

    /**
     * Tests that visitConstructor can be called without throwing exceptions.
     */
    @Test
    public void testVisitConstructor_doesNotThrowException() {
        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);
        }, "visitConstructor should not throw an exception");
    }

    /**
     * Tests that visitConstructor calls referencedMethodAccept on the constructor metadata.
     * This verifies that the method checks the referenced method.
     */
    @Test
    public void testVisitConstructor_callsReferencedMethodAccept() {
        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert
        verify(mockConstructorMetadata, times(1)).referencedMethodAccept(eq(mockClazz), any(MemberVisitor.class));
    }

    /**
     * Tests that visitConstructor passes the marker itself to referencedMethodAccept.
     * This verifies that the marker is used as the member visitor.
     */
    @Test
    public void testVisitConstructor_passesMarkerToReferencedMethodAccept() {
        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - verify that the marker instance is passed as the visitor
        verify(mockConstructorMetadata, times(1)).referencedMethodAccept(mockClazz, marker);
    }

    /**
     * Tests that visitConstructor does NOT call valueParametersAccept when the referenced method
     * does not have the DONT_OBFUSCATE flag (default behavior with mocks).
     */
    @Test
    public void testVisitConstructor_doesNotCallValueParametersAccept_whenMethodCanBeObfuscated() {
        // Arrange - mock returns null for referencedMethodAccept (no method to visit)
        doNothing().when(mockConstructorMetadata).referencedMethodAccept(any(), any());

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - valueParametersAccept should NOT be called
        verify(mockConstructorMetadata, never()).valueParametersAccept(any(), any(), any());
    }

    /**
     * Tests that visitConstructor calls valueParametersAccept when the referenced method
     * has the DONT_OBFUSCATE flag set.
     */
    @Test
    public void testVisitConstructor_callsValueParametersAccept_whenMethodHasDontObfuscateFlag() {
        // Arrange - set up the referenced method with DONT_OBFUSCATE flag
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockConstructorMetadata).referencedMethodAccept(any(), any());

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - valueParametersAccept should be called
        verify(mockConstructorMetadata, times(1)).valueParametersAccept(
            eq(mockClazz),
            eq(mockClassMetadata),
            any(KotlinValueParameterVisitor.class)
        );
    }

    /**
     * Tests that visitConstructor passes correct parameters to valueParametersAccept.
     */
    @Test
    public void testVisitConstructor_passesCorrectParametersToValueParametersAccept() {
        // Arrange
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockConstructorMetadata).referencedMethodAccept(any(), any());

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - verify exact parameters
        verify(mockConstructorMetadata, times(1)).valueParametersAccept(mockClazz, mockClassMetadata, marker);
    }

    /**
     * Tests that visitConstructor does NOT call valueParametersAccept when the method
     * has other flags but not DONT_OBFUSCATE.
     */
    @Test
    public void testVisitConstructor_doesNotCallValueParametersAccept_whenMethodHasOtherFlags() {
        // Arrange - set up with a different flag (not DONT_OBFUSCATE)
        when(mockProgramMethod.getProcessingFlags()).thenReturn(0x0001); // Some other flag
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockConstructorMetadata).referencedMethodAccept(any(), any());

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - valueParametersAccept should NOT be called
        verify(mockConstructorMetadata, never()).valueParametersAccept(any(), any(), any());
    }

    /**
     * Tests that visitConstructor can be called multiple times and each call is independent.
     */
    @Test
    public void testVisitConstructor_canBeCalledMultipleTimes() {
        // Arrange
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockConstructorMetadata).referencedMethodAccept(any(), any());

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - both methods should be called 3 times
        verify(mockConstructorMetadata, times(3)).referencedMethodAccept(mockClazz, marker);
        verify(mockConstructorMetadata, times(3)).valueParametersAccept(mockClazz, mockClassMetadata, marker);
    }

    /**
     * Tests that visitConstructor with null Clazz delegates to metadata.
     */
    @Test
    public void testVisitConstructor_withNullClazz_delegatesToMetadata() {
        // Act
        marker.visitConstructor(null, mockClassMetadata, mockConstructorMetadata);

        // Assert - referencedMethodAccept should still be called with null
        verify(mockConstructorMetadata, times(1)).referencedMethodAccept(null, marker);
    }

    /**
     * Tests that visitConstructor with null constructor metadata throws NullPointerException.
     */
    @Test
    public void testVisitConstructor_withNullConstructorMetadata_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitConstructor(mockClazz, mockClassMetadata, null);
        }, "Should throw NullPointerException when constructor metadata is null");
    }

    /**
     * Tests that visitConstructor does not interact with Clazz directly.
     */
    @Test
    public void testVisitConstructor_doesNotInteractWithClazz() {
        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - verify no direct interactions with clazz
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitConstructor works with different clazz instances.
     */
    @Test
    public void testVisitConstructor_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz mockClazz2 = mock(Clazz.class);
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockConstructorMetadata).referencedMethodAccept(any(), any());

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);
        marker.visitConstructor(mockClazz2, mockClassMetadata, mockConstructorMetadata);

        // Assert - verify correct clazz is passed to each call
        verify(mockConstructorMetadata, times(1)).referencedMethodAccept(mockClazz, marker);
        verify(mockConstructorMetadata, times(1)).referencedMethodAccept(mockClazz2, marker);
        verify(mockConstructorMetadata, times(1)).valueParametersAccept(mockClazz, mockClassMetadata, marker);
        verify(mockConstructorMetadata, times(1)).valueParametersAccept(mockClazz2, mockClassMetadata, marker);
    }

    /**
     * Tests that visitConstructor works with different class metadata instances.
     */
    @Test
    public void testVisitConstructor_withDifferentClassMetadata_passesCorrectMetadata() {
        // Arrange
        KotlinClassKindMetadata mockClassMetadata2 = mock(KotlinClassKindMetadata.class);
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockConstructorMetadata).referencedMethodAccept(any(), any());

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);
        marker.visitConstructor(mockClazz, mockClassMetadata2, mockConstructorMetadata);

        // Assert - verify correct class metadata is passed
        verify(mockConstructorMetadata, times(1)).valueParametersAccept(mockClazz, mockClassMetadata, marker);
        verify(mockConstructorMetadata, times(1)).valueParametersAccept(mockClazz, mockClassMetadata2, marker);
    }

    /**
     * Tests that visitConstructor works with different constructor metadata instances.
     */
    @Test
    public void testVisitConstructor_withDifferentConstructorMetadata_callsCorrectMetadata() {
        // Arrange
        KotlinConstructorMetadata mockConstructorMetadata2 = mock(KotlinConstructorMetadata.class);

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata2);

        // Assert - verify each metadata instance's methods are called
        verify(mockConstructorMetadata, times(1)).referencedMethodAccept(mockClazz, marker);
        verify(mockConstructorMetadata2, times(1)).referencedMethodAccept(mockClazz, marker);
    }

    /**
     * Tests that multiple marker instances behave consistently.
     */
    @Test
    public void testVisitConstructor_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinValueParameterUsageMarker marker1 = new KotlinValueParameterUsageMarker();
        KotlinValueParameterUsageMarker marker2 = new KotlinValueParameterUsageMarker();
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockConstructorMetadata).referencedMethodAccept(any(), any());

        // Act
        marker1.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);
        marker2.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - both should behave the same
        verify(mockConstructorMetadata, times(2)).referencedMethodAccept(eq(mockClazz), any(MemberVisitor.class));
        verify(mockConstructorMetadata, times(2)).valueParametersAccept(eq(mockClazz), eq(mockClassMetadata), any(KotlinValueParameterVisitor.class));
    }

    /**
     * Tests that visitConstructor calls referencedMethodAccept before valueParametersAccept.
     * This verifies the correct order of operations.
     */
    @Test
    public void testVisitConstructor_callsReferencedMethodAcceptBeforeValueParametersAccept() {
        // Arrange
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockConstructorMetadata).referencedMethodAccept(any(), any());

        org.mockito.InOrder inOrder = inOrder(mockConstructorMetadata);

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - verify order
        inOrder.verify(mockConstructorMetadata).referencedMethodAccept(mockClazz, marker);
        inOrder.verify(mockConstructorMetadata).valueParametersAccept(mockClazz, mockClassMetadata, marker);
    }

    /**
     * Tests that visitConstructor can handle being called immediately after construction.
     */
    @Test
    public void testVisitConstructor_immediatelyAfterConstruction() {
        // Arrange
        KotlinValueParameterUsageMarker newMarker = new KotlinValueParameterUsageMarker();

        // Act & Assert
        assertDoesNotThrow(() -> {
            newMarker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);
        }, "Should be callable immediately after construction");

        verify(mockConstructorMetadata, times(1)).referencedMethodAccept(mockClazz, newMarker);
    }

    /**
     * Tests that visitConstructor works correctly via interface reference.
     */
    @Test
    public void testVisitConstructor_asPartOfVisitorPattern() {
        // Arrange
        proguard.classfile.kotlin.visitor.KotlinConstructorVisitor visitor = marker;

        // Act
        assertDoesNotThrow(() -> {
            visitor.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);
        }, "Should work when used via interface reference");

        // Assert
        verify(mockConstructorMetadata, times(1)).referencedMethodAccept(mockClazz, marker);
    }

    /**
     * Tests that visitConstructor resets keepParameterInfo flag on each call.
     * This ensures each constructor is evaluated independently.
     */
    @Test
    public void testVisitConstructor_resetsKeepParameterInfoFlag_eachCall() {
        // Arrange - first call sets flag to true
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockConstructorMetadata).referencedMethodAccept(any(), any());

        KotlinConstructorMetadata mockConstructorMetadata2 = mock(KotlinConstructorMetadata.class);
        // Second constructor has no method reference (doesn't set flag)

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata2);

        // Assert - first should call valueParametersAccept, second should not
        verify(mockConstructorMetadata, times(1)).valueParametersAccept(mockClazz, mockClassMetadata, marker);
        verify(mockConstructorMetadata2, never()).valueParametersAccept(any(), any(), any());
    }

    /**
     * Tests that visitConstructor handles the case where referencedMethodAccept
     * calls visitAnyMember instead of visitProgramMethod.
     */
    @Test
    public void testVisitConstructor_withVisitAnyMember_doesNotCallValueParametersAccept() {
        // Arrange - set up to call visitAnyMember (which does nothing)
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitAnyMember(mockClazz, mockProgramMethod);
            return null;
        }).when(mockConstructorMetadata).referencedMethodAccept(any(), any());

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - valueParametersAccept should NOT be called (visitAnyMember is a no-op)
        verify(mockConstructorMetadata, never()).valueParametersAccept(any(), any(), any());
    }

    /**
     * Tests that visitConstructor with method having flags including DONT_OBFUSCATE
     * still calls valueParametersAccept.
     */
    @Test
    public void testVisitConstructor_withMultipleFlags_includingDontObfuscate_callsValueParametersAccept() {
        // Arrange - set multiple flags including DONT_OBFUSCATE
        int multipleFlags = ProcessingFlags.DONT_OBFUSCATE | 0x0001 | 0x0002;
        when(mockProgramMethod.getProcessingFlags()).thenReturn(multipleFlags);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockConstructorMetadata).referencedMethodAccept(any(), any());

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - valueParametersAccept should be called
        verify(mockConstructorMetadata, times(1)).valueParametersAccept(mockClazz, mockClassMetadata, marker);
    }

    /**
     * Tests that visitConstructor returns void as expected.
     */
    @Test
    public void testVisitConstructor_returnsVoid() {
        // Act - method returns void, so just verify it executes
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - if we reach here without exception, the method completed successfully
        assertTrue(true, "Method should complete and return void");
    }

    /**
     * Tests the conditional logic: valueParametersAccept is called only if keepParameterInfo is true.
     */
    @Test
    public void testVisitConstructor_conditionalLogic_valueParametersAcceptOnlyIfFlagSet() {
        // Arrange - two constructors: one with flag, one without
        KotlinConstructorMetadata constructorWithFlag = mock(KotlinConstructorMetadata.class);
        KotlinConstructorMetadata constructorWithoutFlag = mock(KotlinConstructorMetadata.class);

        ProgramMethod methodWithFlag = mock(ProgramMethod.class);
        when(methodWithFlag.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, methodWithFlag);
            return null;
        }).when(constructorWithFlag).referencedMethodAccept(any(), any());

        // constructorWithoutFlag has no answer, so no callback happens

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, constructorWithFlag);
        marker.visitConstructor(mockClazz, mockClassMetadata, constructorWithoutFlag);

        // Assert
        verify(constructorWithFlag, times(1)).valueParametersAccept(mockClazz, mockClassMetadata, marker);
        verify(constructorWithoutFlag, never()).valueParametersAccept(any(), any(), any());
    }

    /**
     * Tests that visitConstructor only calls the expected methods on constructor metadata.
     */
    @Test
    public void testVisitConstructor_onlyCallsExpectedMethods_whenFlagNotSet() {
        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - only referencedMethodAccept should be called, not valueParametersAccept
        verify(mockConstructorMetadata, times(1)).referencedMethodAccept(mockClazz, marker);
        verify(mockConstructorMetadata, never()).valueParametersAccept(any(), any(), any());
        verifyNoMoreInteractions(mockConstructorMetadata);
    }

    /**
     * Tests that visitConstructor calls both expected methods when flag is set.
     */
    @Test
    public void testVisitConstructor_callsBothMethods_whenFlagSet() {
        // Arrange
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockConstructorMetadata).referencedMethodAccept(any(), any());

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - both methods should be called
        verify(mockConstructorMetadata, times(1)).referencedMethodAccept(mockClazz, marker);
        verify(mockConstructorMetadata, times(1)).valueParametersAccept(mockClazz, mockClassMetadata, marker);
        verifyNoMoreInteractions(mockConstructorMetadata);
    }

    /**
     * Tests that visitConstructor handles rapid successive calls correctly.
     */
    @Test
    public void testVisitConstructor_rapidSuccessiveCalls() {
        // Arrange
        when(mockProgramMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, mockProgramMethod);
            return null;
        }).when(mockConstructorMetadata).referencedMethodAccept(any(), any());

        // Act - call 10 times
        for (int i = 0; i < 10; i++) {
            marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);
        }

        // Assert - both methods should be called 10 times
        verify(mockConstructorMetadata, times(10)).referencedMethodAccept(mockClazz, marker);
        verify(mockConstructorMetadata, times(10)).valueParametersAccept(mockClazz, mockClassMetadata, marker);
    }

    /**
     * Tests that visitConstructor properly implements the KotlinConstructorVisitor interface contract.
     */
    @Test
    public void testVisitConstructor_implementsVisitorContract() {
        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, mockConstructorMetadata);

        // Assert - verify that the visitor methods are properly called
        verify(mockConstructorMetadata, times(1)).referencedMethodAccept(any(Clazz.class), any(MemberVisitor.class));
    }

    /**
     * Tests that visitConstructor's conditional behavior depends on the referenced method's flags.
     */
    @Test
    public void testVisitConstructor_conditionalBehavior_dependsOnMethodFlags() {
        // Arrange - create two scenarios
        ProgramMethod obfuscatableMethod = mock(ProgramMethod.class);
        when(obfuscatableMethod.getProcessingFlags()).thenReturn(0);

        ProgramMethod nonObfuscatableMethod = mock(ProgramMethod.class);
        when(nonObfuscatableMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        KotlinConstructorMetadata constructor1 = mock(KotlinConstructorMetadata.class);
        KotlinConstructorMetadata constructor2 = mock(KotlinConstructorMetadata.class);

        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, obfuscatableMethod);
            return null;
        }).when(constructor1).referencedMethodAccept(any(), any());

        doAnswer(invocation -> {
            MemberVisitor visitor = invocation.getArgument(1);
            visitor.visitProgramMethod(mockProgramClass, nonObfuscatableMethod);
            return null;
        }).when(constructor2).referencedMethodAccept(any(), any());

        // Act
        marker.visitConstructor(mockClazz, mockClassMetadata, constructor1);
        marker.visitConstructor(mockClazz, mockClassMetadata, constructor2);

        // Assert - only constructor2 should call valueParametersAccept
        verify(constructor1, never()).valueParametersAccept(any(), any(), any());
        verify(constructor2, times(1)).valueParametersAccept(mockClazz, mockClassMetadata, marker);
    }
}
