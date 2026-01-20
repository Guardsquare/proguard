package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.util.MethodLinker;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SynchronizedBlockMethodMarker#hasSynchronizedBlock(Method)}.
 *
 * The hasSynchronizedBlock static method retrieves the MethodOptimizationInfo from the given Method
 * and returns whether it has a synchronized block (monitorenter/monitorexit instructions).
 */
public class SynchronizedBlockMethodMarkerClaude_hasSynchronizedBlockTest {

    /**
     * Tests hasSynchronizedBlock returns false when method has no synchronized block.
     */
    @Test
    public void testHasSynchronizedBlock_withNoSynchronizedBlock_returnsFalse() {
        // Arrange - create a method with ProgramMethodOptimizationInfo (hasSynchronizedBlock = false by default)
        ProgramClass mockClazz = mock(ProgramClass.class);
        ProgramMethod method = mock(ProgramMethod.class);

        when(method.getDescriptor(any())).thenReturn("()V");
        when(method.getAccessFlags()).thenReturn(0);

        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, method);
        when(method.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method);

        // Assert
        assertFalse(result, "hasSynchronizedBlock should return false when no synchronized block is present");
    }

    /**
     * Tests hasSynchronizedBlock returns true when method has a synchronized block.
     */
    @Test
    public void testHasSynchronizedBlock_withSynchronizedBlock_returnsTrue() {
        // Arrange - create a method with ProgramMethodOptimizationInfo and set hasSynchronizedBlock
        ProgramClass mockClazz = mock(ProgramClass.class);
        ProgramMethod method = mock(ProgramMethod.class);

        when(method.getDescriptor(any())).thenReturn("()V");
        when(method.getAccessFlags()).thenReturn(0);

        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, method);
        info.setHasSynchronizedBlock();
        when(method.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method);

        // Assert
        assertTrue(result, "hasSynchronizedBlock should return true when synchronized block is present");
    }

    /**
     * Tests hasSynchronizedBlock with MethodOptimizationInfo (not Program variant).
     * MethodOptimizationInfo always returns true by default (conservative).
     */
    @Test
    public void testHasSynchronizedBlock_withBasicMethodOptimizationInfo_returnsTrue() {
        // Arrange - create a method with basic MethodOptimizationInfo (conservative default)
        ProgramMethod method = mock(ProgramMethod.class);

        MethodOptimizationInfo info = new MethodOptimizationInfo();
        when(method.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method);

        // Assert
        assertTrue(result, "hasSynchronizedBlock should return true with basic MethodOptimizationInfo (conservative default)");
    }

    /**
     * Tests hasSynchronizedBlock with null processing info.
     * This should throw a NullPointerException as the method expects valid info.
     */
    @Test
    public void testHasSynchronizedBlock_withNullProcessingInfo_throwsNullPointerException() {
        // Arrange
        ProgramMethod method = mock(ProgramMethod.class);
        when(method.getProcessingInfo()).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            SynchronizedBlockMethodMarker.hasSynchronizedBlock(method);
        }, "hasSynchronizedBlock should throw NullPointerException when processing info is null");
    }

    /**
     * Tests hasSynchronizedBlock with multiple methods sharing the same optimization info.
     */
    @Test
    public void testHasSynchronizedBlock_withSharedOptimizationInfo_returnsSameValue() {
        // Arrange - create two methods sharing the same optimization info
        ProgramClass mockClazz = mock(ProgramClass.class);
        ProgramMethod method1 = mock(ProgramMethod.class);
        ProgramMethod method2 = mock(ProgramMethod.class);

        when(method1.getDescriptor(any())).thenReturn("()V");
        when(method1.getAccessFlags()).thenReturn(0);

        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, method1);
        info.setHasSynchronizedBlock();

        when(method1.getProcessingInfo()).thenReturn(info);
        when(method2.getProcessingInfo()).thenReturn(info);

        // Act
        boolean result1 = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method1);
        boolean result2 = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method2);

        // Assert
        assertTrue(result1, "First method should return true");
        assertTrue(result2, "Second method should return true (same info)");
        assertEquals(result1, result2, "Both methods should return the same result");
    }

    /**
     * Tests hasSynchronizedBlock after setting and then checking the flag.
     */
    @Test
    public void testHasSynchronizedBlock_afterSettingFlag_reflectsChange() {
        // Arrange
        ProgramClass mockClazz = mock(ProgramClass.class);
        ProgramMethod method = mock(ProgramMethod.class);

        when(method.getDescriptor(any())).thenReturn("()V");
        when(method.getAccessFlags()).thenReturn(0);

        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, method);
        when(method.getProcessingInfo()).thenReturn(info);

        // Act - check initial state
        boolean resultBefore = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method);

        // Set the flag
        info.setHasSynchronizedBlock();

        // Check after setting
        boolean resultAfter = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method);

        // Assert
        assertFalse(resultBefore, "Initially should return false");
        assertTrue(resultAfter, "After setting should return true");
    }

    /**
     * Tests hasSynchronizedBlock with different method types (static, instance).
     */
    @Test
    public void testHasSynchronizedBlock_withDifferentMethodTypes_behavesConsistently() {
        // Arrange - instance method
        ProgramClass mockClazz = mock(ProgramClass.class);
        ProgramMethod instanceMethod = mock(ProgramMethod.class);
        when(instanceMethod.getDescriptor(any())).thenReturn("()V");
        when(instanceMethod.getAccessFlags()).thenReturn(0);

        ProgramMethodOptimizationInfo instanceInfo = new ProgramMethodOptimizationInfo(mockClazz, instanceMethod);
        instanceInfo.setHasSynchronizedBlock();
        when(instanceMethod.getProcessingInfo()).thenReturn(instanceInfo);

        // Arrange - static method
        ProgramMethod staticMethod = mock(ProgramMethod.class);
        when(staticMethod.getDescriptor(any())).thenReturn("()V");
        when(staticMethod.getAccessFlags()).thenReturn(AccessConstants.STATIC);

        ProgramMethodOptimizationInfo staticInfo = new ProgramMethodOptimizationInfo(mockClazz, staticMethod);
        staticInfo.setHasSynchronizedBlock();
        when(staticMethod.getProcessingInfo()).thenReturn(staticInfo);

        // Act
        boolean instanceResult = SynchronizedBlockMethodMarker.hasSynchronizedBlock(instanceMethod);
        boolean staticResult = SynchronizedBlockMethodMarker.hasSynchronizedBlock(staticMethod);

        // Assert
        assertTrue(instanceResult, "Instance method should return true");
        assertTrue(staticResult, "Static method should return true");
    }

    /**
     * Tests hasSynchronizedBlock is consistent across multiple calls.
     */
    @Test
    public void testHasSynchronizedBlock_multipleCallsConsistent() {
        // Arrange
        ProgramClass mockClazz = mock(ProgramClass.class);
        ProgramMethod method = mock(ProgramMethod.class);

        when(method.getDescriptor(any())).thenReturn("()V");
        when(method.getAccessFlags()).thenReturn(0);

        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, method);
        info.setHasSynchronizedBlock();
        when(method.getProcessingInfo()).thenReturn(info);

        // Act - call multiple times
        boolean result1 = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method);
        boolean result2 = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method);
        boolean result3 = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method);

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
    }

    /**
     * Tests hasSynchronizedBlock with methods having different descriptors.
     */
    @Test
    public void testHasSynchronizedBlock_withDifferentDescriptors_independentResults() {
        // Arrange - method with no parameters
        ProgramClass mockClazz = mock(ProgramClass.class);
        ProgramMethod method1 = mock(ProgramMethod.class);
        when(method1.getDescriptor(any())).thenReturn("()V");
        when(method1.getAccessFlags()).thenReturn(0);

        ProgramMethodOptimizationInfo info1 = new ProgramMethodOptimizationInfo(mockClazz, method1);
        info1.setHasSynchronizedBlock();
        when(method1.getProcessingInfo()).thenReturn(info1);

        // Arrange - method with parameters
        ProgramMethod method2 = mock(ProgramMethod.class);
        when(method2.getDescriptor(any())).thenReturn("(ILjava/lang/String;)V");
        when(method2.getAccessFlags()).thenReturn(0);

        ProgramMethodOptimizationInfo info2 = new ProgramMethodOptimizationInfo(mockClazz, method2);
        // Don't set synchronized block for method2
        when(method2.getProcessingInfo()).thenReturn(info2);

        // Act
        boolean result1 = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method1);
        boolean result2 = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method2);

        // Assert
        assertTrue(result1, "Method1 should have synchronized block");
        assertFalse(result2, "Method2 should not have synchronized block");
    }

    /**
     * Tests hasSynchronizedBlock with merged optimization info.
     */
    @Test
    public void testHasSynchronizedBlock_afterMerge_reflectsOrOperation() {
        // Arrange - create two optimization infos
        ProgramClass mockClazz = mock(ProgramClass.class);
        ProgramMethod method1 = mock(ProgramMethod.class);
        when(method1.getDescriptor(any())).thenReturn("()V");
        when(method1.getAccessFlags()).thenReturn(0);

        ProgramMethodOptimizationInfo info1 = new ProgramMethodOptimizationInfo(mockClazz, method1);
        // info1 has no synchronized block initially

        ProgramMethod method2 = mock(ProgramMethod.class);
        when(method2.getDescriptor(any())).thenReturn("()V");
        when(method2.getAccessFlags()).thenReturn(0);

        ProgramMethodOptimizationInfo info2 = new ProgramMethodOptimizationInfo(mockClazz, method2);
        info2.setHasSynchronizedBlock();

        when(method1.getProcessingInfo()).thenReturn(info1);

        // Act - check before merge
        boolean beforeMerge = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method1);

        // Merge info2 into info1
        info1.merge(info2);

        // Check after merge
        boolean afterMerge = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method1);

        // Assert
        assertFalse(beforeMerge, "Before merge should return false");
        assertTrue(afterMerge, "After merge should return true (OR operation)");
    }

    /**
     * Tests hasSynchronizedBlock behavior is thread-safe across concurrent reads.
     */
    @Test
    public void testHasSynchronizedBlock_concurrentReads_consistent() throws InterruptedException {
        // Arrange
        ProgramClass mockClazz = mock(ProgramClass.class);
        ProgramMethod method = mock(ProgramMethod.class);

        when(method.getDescriptor(any())).thenReturn("()V");
        when(method.getAccessFlags()).thenReturn(0);

        ProgramMethodOptimizationInfo info = new ProgramMethodOptimizationInfo(mockClazz, method);
        info.setHasSynchronizedBlock();
        when(method.getProcessingInfo()).thenReturn(info);

        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        final boolean[] results = new boolean[threadCount];

        // Act - read from multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = SynchronizedBlockMethodMarker.hasSynchronizedBlock(method);
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - all threads should get the same result
        for (int i = 0; i < threadCount; i++) {
            assertTrue(results[i], "Thread " + i + " should return true");
        }
    }
}
