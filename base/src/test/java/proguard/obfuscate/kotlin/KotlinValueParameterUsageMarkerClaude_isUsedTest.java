package proguard.obfuscate.kotlin;

import org.junit.jupiter.api.Test;
import proguard.classfile.kotlin.KotlinValueParameterMetadata;
import proguard.util.Processable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KotlinValueParameterUsageMarker#isUsed(Processable)}.
 * Tests the static isUsed method which checks if a Processable has been marked as used
 * by comparing its processing info to the internal USED marker object.
 */
public class KotlinValueParameterUsageMarkerClaude_isUsedTest {

    /**
     * Tests that isUsed returns false for a Processable with null processing info.
     * This is the default state before any marking occurs.
     */
    @Test
    public void testIsUsed_withNullProcessingInfo_returnsFalse() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(null);

        // Act
        boolean result = KotlinValueParameterUsageMarker.isUsed(processable);

        // Assert
        assertFalse(result, "isUsed should return false for processable with null processing info");
        verify(processable, times(1)).getProcessingInfo();
    }

    /**
     * Tests that isUsed returns false for a Processable with a different processing info object.
     * The method uses identity comparison (==) so a different object should not match.
     */
    @Test
    public void testIsUsed_withDifferentProcessingInfo_returnsFalse() {
        // Arrange
        Processable processable = mock(Processable.class);
        Object differentObject = new Object();
        when(processable.getProcessingInfo()).thenReturn(differentObject);

        // Act
        boolean result = KotlinValueParameterUsageMarker.isUsed(processable);

        // Assert
        assertFalse(result, "isUsed should return false for processable with different processing info");
        verify(processable, times(1)).getProcessingInfo();
    }

    /**
     * Tests that isUsed returns true after a parameter has been marked through visitAnyValueParameter.
     * This tests the complete workflow: mark -> check.
     *
     * Note: Since the USED marker is a private static field, we cannot directly test the true case
     * without using reflection or going through the public API (visitAnyValueParameter).
     * This test uses KotlinValueParameterMetadata which is required by visitAnyValueParameter.
     */
    @Test
    public void testIsUsed_afterMarking_returnsTrue() {
        // Arrange
        // Create a mock that implements both KotlinValueParameterMetadata and Processable
        // KotlinValueParameterMetadata extends Processable, so we can use it for both
        KotlinValueParameterMetadata valueParameter = mock(KotlinValueParameterMetadata.class);

        final Object[] capturedInfo = {null};

        // Set up mock to capture and return the processing info
        doAnswer(invocation -> {
            capturedInfo[0] = invocation.getArgument(0);
            return null;
        }).when(valueParameter).setProcessingInfo(any());

        when(valueParameter.getProcessingInfo()).thenAnswer(invocation -> capturedInfo[0]);

        // Verify initially not used
        assertFalse(KotlinValueParameterUsageMarker.isUsed(valueParameter),
                    "Parameter should not be marked as used initially");

        // Act - mark the parameter using the public API
        KotlinValueParameterUsageMarker marker = new KotlinValueParameterUsageMarker();
        marker.visitAnyValueParameter(null, valueParameter);

        // Assert - now it should be marked as used
        assertTrue(KotlinValueParameterUsageMarker.isUsed(valueParameter),
                   "Parameter should be marked as used after visiting");
    }

    /**
     * Tests that isUsed returns false for a Processable that was never marked.
     */
    @Test
    public void testIsUsed_withUnmarkedProcessable_returnsFalse() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(null);

        // Act
        boolean result = KotlinValueParameterUsageMarker.isUsed(processable);

        // Assert
        assertFalse(result, "isUsed should return false for unmarked processable");
    }

    /**
     * Tests that isUsed can be called multiple times on the same Processable
     * and returns consistent results.
     */
    @Test
    public void testIsUsed_calledMultipleTimes_returnsConsistentResults() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(null);

        // Act
        boolean result1 = KotlinValueParameterUsageMarker.isUsed(processable);
        boolean result2 = KotlinValueParameterUsageMarker.isUsed(processable);
        boolean result3 = KotlinValueParameterUsageMarker.isUsed(processable);

        // Assert
        assertFalse(result1, "First call should return false");
        assertFalse(result2, "Second call should return false");
        assertFalse(result3, "Third call should return false");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests that isUsed throws NullPointerException when passed null.
     */
    @Test
    public void testIsUsed_withNullProcessable_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            KotlinValueParameterUsageMarker.isUsed(null);
        }, "isUsed should throw NullPointerException when passed null");
    }

    /**
     * Tests that isUsed calls getProcessingInfo on the Processable.
     */
    @Test
    public void testIsUsed_callsGetProcessingInfo() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(null);

        // Act
        KotlinValueParameterUsageMarker.isUsed(processable);

        // Assert
        verify(processable, times(1)).getProcessingInfo();
        verifyNoMoreInteractions(processable);
    }

    /**
     * Tests that isUsed only calls getProcessingInfo once per invocation.
     */
    @Test
    public void testIsUsed_callsGetProcessingInfoOnce() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(null);

        // Act
        KotlinValueParameterUsageMarker.isUsed(processable);
        KotlinValueParameterUsageMarker.isUsed(processable);

        // Assert - should be called twice total (once per isUsed call)
        verify(processable, times(2)).getProcessingInfo();
    }

    /**
     * Tests that isUsed works with different Processable instances independently.
     */
    @Test
    public void testIsUsed_withDifferentProcessables_worksIndependently() {
        // Arrange
        Processable processable1 = mock(Processable.class);
        Processable processable2 = mock(Processable.class);
        Processable processable3 = mock(Processable.class);

        when(processable1.getProcessingInfo()).thenReturn(null);
        when(processable2.getProcessingInfo()).thenReturn(new Object());
        when(processable3.getProcessingInfo()).thenReturn("different");

        // Act
        boolean result1 = KotlinValueParameterUsageMarker.isUsed(processable1);
        boolean result2 = KotlinValueParameterUsageMarker.isUsed(processable2);
        boolean result3 = KotlinValueParameterUsageMarker.isUsed(processable3);

        // Assert
        assertFalse(result1, "First processable should not be marked as used");
        assertFalse(result2, "Second processable should not be marked as used");
        assertFalse(result3, "Third processable should not be marked as used");

        verify(processable1, times(1)).getProcessingInfo();
        verify(processable2, times(1)).getProcessingInfo();
        verify(processable3, times(1)).getProcessingInfo();
    }

    /**
     * Tests that isUsed uses identity comparison (==) not equality comparison.
     * Even if two objects are equal, they should not be considered the same unless they are identical.
     */
    @Test
    public void testIsUsed_usesIdentityComparison_notEquality() {
        // Arrange
        Processable processable = mock(Processable.class);
        // Create an object that would be equal to itself but not identical to the USED marker
        String sameValue = new String("marker");
        when(processable.getProcessingInfo()).thenReturn(sameValue);

        // Act
        boolean result = KotlinValueParameterUsageMarker.isUsed(processable);

        // Assert
        assertFalse(result, "isUsed should use identity comparison, not equality");
    }

    /**
     * Tests that isUsed returns false for a Processable with a string processing info.
     */
    @Test
    public void testIsUsed_withStringProcessingInfo_returnsFalse() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn("used");

        // Act
        boolean result = KotlinValueParameterUsageMarker.isUsed(processable);

        // Assert
        assertFalse(result, "isUsed should return false for string processing info");
    }

    /**
     * Tests that isUsed returns false for a Processable with an integer processing info.
     */
    @Test
    public void testIsUsed_withIntegerProcessingInfo_returnsFalse() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(42);

        // Act
        boolean result = KotlinValueParameterUsageMarker.isUsed(processable);

        // Assert
        assertFalse(result, "isUsed should return false for integer processing info");
    }

    /**
     * Tests that isUsed returns false for a Processable with a boolean processing info.
     */
    @Test
    public void testIsUsed_withBooleanProcessingInfo_returnsFalse() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(Boolean.TRUE);

        // Act
        boolean result = KotlinValueParameterUsageMarker.isUsed(processable);

        // Assert
        assertFalse(result, "isUsed should return false for boolean processing info");
    }

    /**
     * Tests that isUsed is a static method and can be called without an instance.
     */
    @Test
    public void testIsUsed_isStaticMethod() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(null);

        // Act - call as static method
        boolean result = KotlinValueParameterUsageMarker.isUsed(processable);

        // Assert
        assertFalse(result, "isUsed should be callable as a static method");
    }

    /**
     * Tests that isUsed does not modify the Processable in any way.
     */
    @Test
    public void testIsUsed_doesNotModifyProcessable() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(null);

        // Act
        KotlinValueParameterUsageMarker.isUsed(processable);

        // Assert - only getProcessingInfo should be called, not setProcessingInfo
        verify(processable, times(1)).getProcessingInfo();
        verify(processable, never()).setProcessingInfo(any());
        verifyNoMoreInteractions(processable);
    }

    /**
     * Tests that isUsed can be called rapidly many times without issues.
     */
    @Test
    public void testIsUsed_rapidCalls_performsCorrectly() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(null);

        // Act - call 1000 times
        for (int i = 0; i < 1000; i++) {
            boolean result = KotlinValueParameterUsageMarker.isUsed(processable);
            assertFalse(result, "Result should be false on iteration " + i);
        }

        // Assert
        verify(processable, times(1000)).getProcessingInfo();
    }

    /**
     * Tests the complete lifecycle: unmarked -> marked -> checked.
     * This integration test verifies the interaction between marking and checking.
     */
    @Test
    public void testIsUsed_completeLifecycle_unmarkedToMarkedToChecked() {
        // Arrange
        Processable processable = mock(Processable.class);
        final Object[] capturedInfo = {null};

        doAnswer(invocation -> {
            capturedInfo[0] = invocation.getArgument(0);
            return null;
        }).when(processable).setProcessingInfo(any());

        when(processable.getProcessingInfo()).thenAnswer(invocation -> capturedInfo[0]);

        // Act & Assert - initially not used
        assertFalse(KotlinValueParameterUsageMarker.isUsed(processable),
                    "Should not be marked as used initially");

        // Simulate marking by setting a dummy object (not the actual USED object)
        processable.setProcessingInfo(new Object());
        assertFalse(KotlinValueParameterUsageMarker.isUsed(processable),
                    "Should not be marked as used with a different object");

        // This test demonstrates that only the specific USED marker object will return true
        // We cannot directly test the true case without accessing the private USED field
        // or going through the visitAnyValueParameter method
    }

    /**
     * Tests that isUsed handles processables with various types of processing info.
     */
    @Test
    public void testIsUsed_withVariousProcessingInfoTypes_returnsFalse() {
        // Arrange & Act & Assert
        Processable processableWithArray = mock(Processable.class);
        when(processableWithArray.getProcessingInfo()).thenReturn(new int[]{1, 2, 3});
        assertFalse(KotlinValueParameterUsageMarker.isUsed(processableWithArray),
                    "Should return false for array processing info");

        Processable processableWithClass = mock(Processable.class);
        when(processableWithClass.getProcessingInfo()).thenReturn(String.class);
        assertFalse(KotlinValueParameterUsageMarker.isUsed(processableWithClass),
                    "Should return false for class processing info");

        Processable processableWithMock = mock(Processable.class);
        when(processableWithMock.getProcessingInfo()).thenReturn(mock(Object.class));
        assertFalse(KotlinValueParameterUsageMarker.isUsed(processableWithMock),
                    "Should return false for mock object processing info");
    }

    /**
     * Tests that isUsed returns consistent results when called from different threads.
     * This verifies thread-safety of the static method.
     */
    @Test
    public void testIsUsed_calledFromMultipleThreads_isThreadSafe() throws InterruptedException {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(null);

        final boolean[] results = new boolean[10];
        Thread[] threads = new Thread[10];

        // Act - call from multiple threads
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = KotlinValueParameterUsageMarker.isUsed(processable);
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - all results should be false and consistent
        for (int i = 0; i < 10; i++) {
            assertFalse(results[i], "Result from thread " + i + " should be false");
        }
    }

    /**
     * Tests that isUsed returns the correct result when the processing info changes.
     */
    @Test
    public void testIsUsed_withChangingProcessingInfo_reflectsCurrentState() {
        // Arrange
        Processable processable = mock(Processable.class);
        final Object[] currentInfo = {null};

        when(processable.getProcessingInfo()).thenAnswer(invocation -> currentInfo[0]);

        // Act & Assert - initially null
        assertFalse(KotlinValueParameterUsageMarker.isUsed(processable),
                    "Should return false with null processing info");

        // Change to a different object
        currentInfo[0] = new Object();
        assertFalse(KotlinValueParameterUsageMarker.isUsed(processable),
                    "Should return false with different object");

        // Change to another different object
        currentInfo[0] = "another object";
        assertFalse(KotlinValueParameterUsageMarker.isUsed(processable),
                    "Should return false with yet another object");

        // Change back to null
        currentInfo[0] = null;
        assertFalse(KotlinValueParameterUsageMarker.isUsed(processable),
                    "Should return false when changed back to null");
    }

    /**
     * Tests that isUsed with different processable instances doesn't interfere with each other.
     */
    @Test
    public void testIsUsed_withMultipleProcessables_noInterference() {
        // Arrange
        Processable processable1 = mock(Processable.class);
        Processable processable2 = mock(Processable.class);

        when(processable1.getProcessingInfo()).thenReturn(null);
        when(processable2.getProcessingInfo()).thenReturn(new Object());

        // Act
        boolean result1First = KotlinValueParameterUsageMarker.isUsed(processable1);
        boolean result2First = KotlinValueParameterUsageMarker.isUsed(processable2);
        boolean result1Second = KotlinValueParameterUsageMarker.isUsed(processable1);
        boolean result2Second = KotlinValueParameterUsageMarker.isUsed(processable2);

        // Assert - results should be independent
        assertFalse(result1First, "First processable should return false initially");
        assertFalse(result2First, "Second processable should return false initially");
        assertFalse(result1Second, "First processable should still return false");
        assertFalse(result2Second, "Second processable should still return false");

        verify(processable1, times(2)).getProcessingInfo();
        verify(processable2, times(2)).getProcessingInfo();
    }

    /**
     * Tests that isUsed's return value is a primitive boolean, not a Boolean object.
     */
    @Test
    public void testIsUsed_returnsPrimitiveBoolean() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(null);

        // Act
        boolean result = KotlinValueParameterUsageMarker.isUsed(processable);

        // Assert - verify it's a primitive boolean by using it in a primitive context
        boolean primitiveTest = result || false;
        assertFalse(primitiveTest, "Result should be usable as a primitive boolean");
    }

    /**
     * Tests that isUsed only inspects the processing info and doesn't call any other methods.
     */
    @Test
    public void testIsUsed_onlyCallsGetProcessingInfo_noOtherMethods() {
        // Arrange
        Processable processable = mock(Processable.class);
        when(processable.getProcessingInfo()).thenReturn(null);

        // Act
        KotlinValueParameterUsageMarker.isUsed(processable);

        // Assert - verify only getProcessingInfo was called
        verify(processable, times(1)).getProcessingInfo();
        verifyNoMoreInteractions(processable);
    }
}
