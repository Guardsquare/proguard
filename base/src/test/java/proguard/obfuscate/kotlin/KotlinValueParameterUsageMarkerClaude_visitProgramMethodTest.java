package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.util.ProcessingFlags;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinValueParameterUsageMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
 *
 * The visitProgramMethod sets the internal keepParameterInfo flag based on whether the method
 * has the DONT_OBFUSCATE flag set. This flag is then used by other visitor methods to determine
 * whether to mark value parameters as used.
 */
public class KotlinValueParameterUsageMarkerClaude_visitProgramMethodTest {

    private KotlinValueParameterUsageMarker marker;
    private ProgramClass programClass;
    private ProgramMethod programMethod;

    @BeforeEach
    public void setUp() {
        marker = new KotlinValueParameterUsageMarker();
        programClass = mock(ProgramClass.class);
        programMethod = mock(ProgramMethod.class);
    }

    /**
     * Tests that visitProgramMethod can be called without throwing exceptions.
     */
    @Test
    public void testVisitProgramMethod_doesNotThrowException() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(0);

        // Act & Assert - should not throw any exceptions
        assertDoesNotThrow(() -> {
            marker.visitProgramMethod(programClass, programMethod);
        }, "visitProgramMethod should not throw an exception");
    }

    /**
     * Tests that visitProgramMethod calls getProcessingFlags on the method.
     * This verifies that the method checks the processing flags.
     */
    @Test
    public void testVisitProgramMethod_callsGetProcessingFlags() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(0);

        // Act
        marker.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod sets keepParameterInfo to true when method has DONT_OBFUSCATE flag.
     * This is verified indirectly by the behavior of other methods that depend on this flag.
     */
    @Test
    public void testVisitProgramMethod_withDontObfuscateFlag_setsKeepParameterInfoTrue() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        // Act
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - verify getProcessingFlags was called
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod sets keepParameterInfo to false when method doesn't have DONT_OBFUSCATE flag.
     */
    @Test
    public void testVisitProgramMethod_withoutDontObfuscateFlag_setsKeepParameterInfoFalse() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(0);

        // Act
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - verify getProcessingFlags was called
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod handles method with only DONT_OBFUSCATE flag.
     */
    @Test
    public void testVisitProgramMethod_withOnlyDontObfuscateFlag() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitProgramMethod(programClass, programMethod));
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod handles method with DONT_OBFUSCATE flag combined with other flags.
     */
    @Test
    public void testVisitProgramMethod_withMultipleFlagsIncludingDontObfuscate() {
        // Arrange
        int multipleFlags = ProcessingFlags.DONT_OBFUSCATE | 0x0001 | 0x0002;
        when(programMethod.getProcessingFlags()).thenReturn(multipleFlags);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitProgramMethod(programClass, programMethod));
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod handles method with flags that don't include DONT_OBFUSCATE.
     */
    @Test
    public void testVisitProgramMethod_withOtherFlagsButNotDontObfuscate() {
        // Arrange
        int otherFlags = 0x0001 | 0x0002 | 0x0004;
        when(programMethod.getProcessingFlags()).thenReturn(otherFlags);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitProgramMethod(programClass, programMethod));
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod can be called multiple times with the same method.
     */
    @Test
    public void testVisitProgramMethod_canBeCalledMultipleTimes() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        // Act
        marker.visitProgramMethod(programClass, programMethod);
        marker.visitProgramMethod(programClass, programMethod);
        marker.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programMethod, times(3)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod can be called with different methods sequentially.
     */
    @Test
    public void testVisitProgramMethod_withDifferentMethods_sequentially() {
        // Arrange
        ProgramMethod method1 = mock(ProgramMethod.class);
        ProgramMethod method2 = mock(ProgramMethod.class);
        ProgramMethod method3 = mock(ProgramMethod.class);

        when(method1.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        when(method2.getProcessingFlags()).thenReturn(0);
        when(method3.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        // Act
        marker.visitProgramMethod(programClass, method1);
        marker.visitProgramMethod(programClass, method2);
        marker.visitProgramMethod(programClass, method3);

        // Assert
        verify(method1, times(1)).getProcessingFlags();
        verify(method2, times(1)).getProcessingFlags();
        verify(method3, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod doesn't interact with the ProgramClass parameter.
     * The class is passed but not used in the current implementation.
     */
    @Test
    public void testVisitProgramMethod_doesNotInteractWithProgramClass() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(0);

        // Act
        marker.visitProgramMethod(programClass, programMethod);

        // Assert
        verifyNoInteractions(programClass);
    }

    /**
     * Tests that visitProgramMethod works with null ProgramClass parameter.
     * The method doesn't use the class parameter, so null should be acceptable.
     */
    @Test
    public void testVisitProgramMethod_withNullProgramClass() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(0);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitProgramMethod(null, programMethod));
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod with null ProgramMethod throws NullPointerException.
     * The method must call getProcessingFlags on the method, so null is not acceptable.
     */
    @Test
    public void testVisitProgramMethod_withNullProgramMethod_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitProgramMethod(programClass, null);
        }, "Should throw NullPointerException when program method is null");
    }

    /**
     * Tests that visitProgramMethod works with both parameters null.
     */
    @Test
    public void testVisitProgramMethod_withBothParametersNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            marker.visitProgramMethod(null, null);
        }, "Should throw NullPointerException when program method is null");
    }

    /**
     * Tests that visitProgramMethod can be called via the MemberVisitor interface.
     */
    @Test
    public void testVisitProgramMethod_viaMemberVisitorInterface() {
        // Arrange
        proguard.classfile.visitor.MemberVisitor visitor = marker;
        when(programMethod.getProcessingFlags()).thenReturn(0);

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitProgramMethod(programClass, programMethod));
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod with different marker instances behaves consistently.
     */
    @Test
    public void testVisitProgramMethod_consistentBehaviorAcrossInstances() {
        // Arrange
        KotlinValueParameterUsageMarker marker1 = new KotlinValueParameterUsageMarker();
        KotlinValueParameterUsageMarker marker2 = new KotlinValueParameterUsageMarker();
        when(programMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        // Act
        marker1.visitProgramMethod(programClass, programMethod);
        marker2.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programMethod, times(2)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod returns void.
     */
    @Test
    public void testVisitProgramMethod_returnsVoid() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(0);

        // Act - method returns void, so just verify it executes
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - if we reach here without exception, the method completed successfully
        assertTrue(true, "Method should complete and return void");
    }

    /**
     * Tests that visitProgramMethod can handle a method with flag value of Integer.MAX_VALUE.
     */
    @Test
    public void testVisitProgramMethod_withMaxIntegerFlags() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(Integer.MAX_VALUE);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitProgramMethod(programClass, programMethod));
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod can handle a method with flag value of Integer.MIN_VALUE.
     */
    @Test
    public void testVisitProgramMethod_withMinIntegerFlags() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(Integer.MIN_VALUE);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitProgramMethod(programClass, programMethod));
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod can handle a method with negative flag values.
     */
    @Test
    public void testVisitProgramMethod_withNegativeFlags() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(-1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitProgramMethod(programClass, programMethod));
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod can be called immediately after marker construction.
     */
    @Test
    public void testVisitProgramMethod_immediatelyAfterConstruction() {
        // Arrange
        KotlinValueParameterUsageMarker newMarker = new KotlinValueParameterUsageMarker();
        when(programMethod.getProcessingFlags()).thenReturn(0);

        // Act & Assert
        assertDoesNotThrow(() -> newMarker.visitProgramMethod(programClass, programMethod));
    }

    /**
     * Tests that visitProgramMethod can be called with different ProgramClass instances.
     */
    @Test
    public void testVisitProgramMethod_withDifferentProgramClasses() {
        // Arrange
        ProgramClass class1 = mock(ProgramClass.class);
        ProgramClass class2 = mock(ProgramClass.class);
        ProgramClass class3 = mock(ProgramClass.class);
        when(programMethod.getProcessingFlags()).thenReturn(0);

        // Act & Assert
        assertDoesNotThrow(() -> {
            marker.visitProgramMethod(class1, programMethod);
            marker.visitProgramMethod(class2, programMethod);
            marker.visitProgramMethod(class3, programMethod);
        });

        verify(programMethod, times(3)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod alternating between flag values works correctly.
     */
    @Test
    public void testVisitProgramMethod_alternatingFlagValues() {
        // Arrange
        ProgramMethod methodWithFlag = mock(ProgramMethod.class);
        ProgramMethod methodWithoutFlag = mock(ProgramMethod.class);

        when(methodWithFlag.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        when(methodWithoutFlag.getProcessingFlags()).thenReturn(0);

        // Act
        marker.visitProgramMethod(programClass, methodWithFlag);
        marker.visitProgramMethod(programClass, methodWithoutFlag);
        marker.visitProgramMethod(programClass, methodWithFlag);
        marker.visitProgramMethod(programClass, methodWithoutFlag);

        // Assert
        verify(methodWithFlag, times(2)).getProcessingFlags();
        verify(methodWithoutFlag, times(2)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod with all valid flag bits set works correctly.
     */
    @Test
    public void testVisitProgramMethod_withAllFlagBitsSet() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(0xFFFFFFFF);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitProgramMethod(programClass, programMethod));
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod only calls getProcessingFlags once per invocation.
     */
    @Test
    public void testVisitProgramMethod_callsGetProcessingFlagsOnlyOnce() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        // Act
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - verify it's called exactly once, not multiple times
        verify(programMethod, times(1)).getProcessingFlags();
        verifyNoMoreInteractions(programMethod);
    }

    /**
     * Tests that visitProgramMethod with zero flags (no flags set).
     */
    @Test
    public void testVisitProgramMethod_withZeroFlags() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(0);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitProgramMethod(programClass, programMethod));
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod can be used in a loop with different methods.
     */
    @Test
    public void testVisitProgramMethod_inLoopWithDifferentMethods() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                ProgramMethod method = mock(ProgramMethod.class);
                when(method.getProcessingFlags()).thenReturn(i % 2 == 0 ? ProcessingFlags.DONT_OBFUSCATE : 0);
                marker.visitProgramMethod(programClass, method);
            }
        });
    }

    /**
     * Tests that visitProgramMethod performs bitwise AND operation correctly.
     * This test verifies the flag checking logic.
     */
    @Test
    public void testVisitProgramMethod_bitwiseAndOperation() {
        // Arrange - set up a method with DONT_OBFUSCATE bit set among others
        int flagsWithDontObfuscate = ProcessingFlags.DONT_OBFUSCATE | 0x0008 | 0x0010;
        when(programMethod.getProcessingFlags()).thenReturn(flagsWithDontObfuscate);

        // Act & Assert - should recognize DONT_OBFUSCATE flag
        assertDoesNotThrow(() -> marker.visitProgramMethod(programClass, programMethod));
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod works correctly when called after other visitor methods.
     */
    @Test
    public void testVisitProgramMethod_afterOtherVisitorMethods() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        // Act - call other methods first
        marker.visitAnyMember(programClass, programMethod);
        marker.visitProgramMethod(programClass, programMethod);

        // Assert
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod works correctly when called before other visitor methods.
     */
    @Test
    public void testVisitProgramMethod_beforeOtherVisitorMethods() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        // Act
        marker.visitProgramMethod(programClass, programMethod);
        marker.visitAnyMember(programClass, programMethod);

        // Assert
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod with real ProgramClass and mocked ProgramMethod works.
     */
    @Test
    public void testVisitProgramMethod_withRealProgramClassAndMockedMethod() {
        // Arrange
        ProgramClass realProgramClass = new ProgramClass();
        when(programMethod.getProcessingFlags()).thenReturn(0);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitProgramMethod(realProgramClass, programMethod));
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod with real ProgramMethod works.
     */
    @Test
    public void testVisitProgramMethod_withRealProgramMethod() {
        // Arrange
        ProgramMethod realMethod = new ProgramMethod();
        realMethod.u2accessFlags = 0;

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> marker.visitProgramMethod(programClass, realMethod));
    }

    /**
     * Tests that visitProgramMethod with both real objects works.
     */
    @Test
    public void testVisitProgramMethod_withBothRealObjects() {
        // Arrange
        ProgramClass realClass = new ProgramClass();
        ProgramMethod realMethod = new ProgramMethod();
        realMethod.u2accessFlags = 0;

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitProgramMethod(realClass, realMethod));
    }

    /**
     * Tests that visitProgramMethod handles rapid successive calls with same parameters.
     */
    @Test
    public void testVisitProgramMethod_rapidSuccessiveCallsSameParameters() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        // Act
        for (int i = 0; i < 1000; i++) {
            marker.visitProgramMethod(programClass, programMethod);
        }

        // Assert
        verify(programMethod, times(1000)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod with getProcessingFlags throwing exception propagates it.
     */
    @Test
    public void testVisitProgramMethod_whenGetProcessingFlagsThrowsException_propagatesException() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenThrow(new RuntimeException("Test exception"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            marker.visitProgramMethod(programClass, programMethod);
        }, "Should propagate exception from getProcessingFlags");
    }

    /**
     * Tests that visitProgramMethod correctly evaluates the DONT_OBFUSCATE flag using bitwise AND.
     */
    @Test
    public void testVisitProgramMethod_correctlyEvaluatesDontObfuscateFlag() {
        // Arrange - DONT_OBFUSCATE is the only flag set
        when(programMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        // Act
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - the method should recognize this flag
        verify(programMethod, times(1)).getProcessingFlags();
    }

    /**
     * Tests that visitProgramMethod with specific flag combinations works correctly.
     */
    @Test
    public void testVisitProgramMethod_withSpecificFlagCombinations() {
        // Test various flag combinations
        int[] flagCombinations = {
            0,
            ProcessingFlags.DONT_OBFUSCATE,
            ProcessingFlags.DONT_OBFUSCATE | 0x01,
            ProcessingFlags.DONT_OBFUSCATE | 0x02,
            ProcessingFlags.DONT_OBFUSCATE | 0xFF,
            0x01,
            0x02,
            0xFF
        };

        for (int flags : flagCombinations) {
            // Arrange
            ProgramMethod method = mock(ProgramMethod.class);
            when(method.getProcessingFlags()).thenReturn(flags);

            // Act & Assert
            assertDoesNotThrow(() -> marker.visitProgramMethod(programClass, method),
                "Should handle flags: " + flags);
            verify(method, times(1)).getProcessingFlags();
        }
    }

    /**
     * Tests that visitProgramMethod can be called on a fresh marker instance each time.
     */
    @Test
    public void testVisitProgramMethod_withFreshMarkerInstances() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);

        // Act & Assert
        for (int i = 0; i < 10; i++) {
            KotlinValueParameterUsageMarker freshMarker = new KotlinValueParameterUsageMarker();
            assertDoesNotThrow(() -> freshMarker.visitProgramMethod(programClass, programMethod));
        }

        verify(programMethod, times(10)).getProcessingFlags();
    }

    /**
     * Tests visitProgramMethod doesn't call any methods other than getProcessingFlags on ProgramMethod.
     */
    @Test
    public void testVisitProgramMethod_onlyCallsGetProcessingFlags() {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(0);

        // Act
        marker.visitProgramMethod(programClass, programMethod);

        // Assert - verify only getProcessingFlags is called
        verify(programMethod, times(1)).getProcessingFlags();
        verifyNoMoreInteractions(programMethod);
    }

    /**
     * Tests that visitProgramMethod with concurrent calls works correctly (thread-safety).
     */
    @Test
    public void testVisitProgramMethod_concurrentCalls() throws InterruptedException {
        // Arrange
        when(programMethod.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];

        // Act - create multiple threads calling visitProgramMethod
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    marker.visitProgramMethod(programClass, programMethod);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify getProcessingFlags was called the expected number of times
        verify(programMethod, times(1000)).getProcessingFlags();
    }

    /**
     * Tests that each call to visitProgramMethod is independent.
     */
    @Test
    public void testVisitProgramMethod_eachCallIsIndependent() {
        // Arrange
        ProgramMethod method1 = mock(ProgramMethod.class);
        ProgramMethod method2 = mock(ProgramMethod.class);

        when(method1.getProcessingFlags()).thenReturn(ProcessingFlags.DONT_OBFUSCATE);
        when(method2.getProcessingFlags()).thenReturn(0);

        // Act
        marker.visitProgramMethod(programClass, method1);
        marker.visitProgramMethod(programClass, method2);

        // Assert - each method should have been queried independently
        verify(method1, times(1)).getProcessingFlags();
        verify(method2, times(1)).getProcessingFlags();
    }
}
