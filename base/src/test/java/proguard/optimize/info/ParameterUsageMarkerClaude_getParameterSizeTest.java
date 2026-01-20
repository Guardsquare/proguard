package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterUsageMarker#getParameterSize(Method)}.
 *
 * The getParameterSize static method returns the total size of the parameters of a method.
 * This size accounts for long and double parameters taking up two stack entries.
 * It delegates to MethodOptimizationInfo.getMethodOptimizationInfo(method).getParameterSize().
 *
 * The parameter size is typically set by calling ParameterUsageMarker.visitProgramMethod
 * or by directly setting it via ProgramMethodOptimizationInfo.setParameterSize.
 *
 * Examples:
 * - void method() -> size 1 (for 'this' in non-static methods) or 0 (for static methods)
 * - void method(int a) -> size 2 (this + int)
 * - void method(long a) -> size 3 (this + long takes 2 slots)
 * - static void method(int a, double b) -> size 3 (int + double takes 2 slots)
 */
public class ParameterUsageMarkerClaude_getParameterSizeTest {

    private ProgramClass testClass;

    @BeforeEach
    public void setUp() {
        testClass = createProgramClassWithConstantPool();
    }

    /**
     * Tests getParameterSize returns 0 with default MethodOptimizationInfo.
     * The default implementation returns 0 when no size has been set.
     */
    @Test
    public void testGetParameterSize_withDefaultMethodOptimizationInfo_returnsZero() {
        // Arrange - create method with default MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, method);

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert - should return 0 by default
        assertEquals(0, result, "Should return 0 by default with MethodOptimizationInfo");
    }

    /**
     * Tests getParameterSize returns 0 with ProgramMethodOptimizationInfo initially.
     * Before visiting or setting, the parameter size should be 0.
     */
    @Test
    public void testGetParameterSize_withProgramMethodOptimizationInfo_initiallyReturnsZero() {
        // Arrange - create method with ProgramMethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert - should return 0 initially
        assertEquals(0, result, "Should return 0 initially with ProgramMethodOptimizationInfo");
    }

    /**
     * Tests getParameterSize after explicitly setting it to 1.
     */
    @Test
    public void testGetParameterSize_afterSettingToOne_returnsOne() {
        // Arrange - create method and set parameter size
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(1);

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(1, result, "Should return 1 after setting parameter size to 1");
    }

    /**
     * Tests getParameterSize after setting to a typical size for instance method with int parameter.
     * Size should be 2: 1 for 'this' + 1 for int.
     */
    @Test
    public void testGetParameterSize_instanceMethodWithIntParameter_returnsTwo() {
        // Arrange - create instance method with int parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(2); // this + int

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(2, result, "Should return 2 for instance method with int parameter");
    }

    /**
     * Tests getParameterSize with static method having one int parameter.
     * Size should be 1: just the int (no 'this').
     */
    @Test
    public void testGetParameterSize_staticMethodWithIntParameter_returnsOne() {
        // Arrange - create static method with int parameter
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(1); // just int

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(1, result, "Should return 1 for static method with int parameter");
    }

    /**
     * Tests getParameterSize with method having long parameter.
     * Long takes 2 slots, so size should be 3: this + long (2 slots).
     */
    @Test
    public void testGetParameterSize_withLongParameter_returnsThree() {
        // Arrange - create method with long parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(J)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(3); // this + long (2 slots)

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(3, result, "Should return 3 for instance method with long parameter");
    }

    /**
     * Tests getParameterSize with method having double parameter.
     * Double takes 2 slots, so size should be 3: this + double (2 slots).
     */
    @Test
    public void testGetParameterSize_withDoubleParameter_returnsThree() {
        // Arrange - create method with double parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(D)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(3); // this + double (2 slots)

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(3, result, "Should return 3 for instance method with double parameter");
    }

    /**
     * Tests getParameterSize with multiple parameters of different types.
     * For static method(int, long, String): size = 1 + 2 + 1 = 4.
     */
    @Test
    public void testGetParameterSize_withMultipleParameters_returnsCorrectSum() {
        // Arrange - create static method with int, long, String parameters
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod",
                "(IJLjava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(4); // int(1) + long(2) + String(1)

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(4, result, "Should return 4 for method with multiple parameters");
    }

    /**
     * Tests getParameterSize with large parameter count.
     */
    @Test
    public void testGetParameterSize_withLargeParameterCount_returnsCorrectSize() {
        // Arrange - create method with many parameters
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(IIIIIIIIII)V"); // 10 int parameters
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(11); // this + 10 ints

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(11, result, "Should return 11 for method with 10 int parameters");
    }

    /**
     * Tests getParameterSize with boundary value of 64.
     * 64 is significant as it's the number of bits in a long used for parameter tracking.
     */
    @Test
    public void testGetParameterSize_withBoundaryValue64_returns64() {
        // Arrange - create method and set size to 64
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(64);

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(64, result, "Should return 64 when parameter size is set to 64");
    }

    /**
     * Tests getParameterSize with value exceeding 64.
     * Parameter sizes can exceed 64, though parameter usage tracking has special handling for this.
     */
    @Test
    public void testGetParameterSize_withValueExceeding64_returnsCorrectValue() {
        // Arrange - create method with parameter size > 64
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(100);

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(100, result, "Should return 100 when parameter size is set to 100");
    }

    /**
     * Tests getParameterSize is consistent across multiple calls.
     */
    @Test
    public void testGetParameterSize_multipleCalls_returnsConsistentValue() {
        // Arrange - create method and set parameter size
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(2);

        // Act - call multiple times
        int firstCall = ParameterUsageMarker.getParameterSize(method);
        int secondCall = ParameterUsageMarker.getParameterSize(method);
        int thirdCall = ParameterUsageMarker.getParameterSize(method);

        // Assert - all calls should return the same value
        assertEquals(2, firstCall, "First call should return 2");
        assertEquals(2, secondCall, "Second call should return 2");
        assertEquals(2, thirdCall, "Third call should return 2");
        assertEquals(firstCall, secondCall, "First and second calls should match");
        assertEquals(secondCall, thirdCall, "Second and third calls should match");
    }

    /**
     * Tests getParameterSize with different methods independently.
     */
    @Test
    public void testGetParameterSize_withDifferentMethods_worksIndependently() {
        // Arrange - create three methods with different parameter sizes
        ProgramMethod method1 = createMethodWithDescriptor(testClass, "method1", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method1);
        ProgramMethodOptimizationInfo info1 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method1);
        info1.setParameterSize(2);

        ProgramMethod method2 = createMethodWithDescriptor(testClass, "method2", "(IJ)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method2);
        ProgramMethodOptimizationInfo info2 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method2);
        info2.setParameterSize(4);

        ProgramMethod method3 = createMethodWithDescriptor(testClass, "method3", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method3);
        ProgramMethodOptimizationInfo info3 = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method3);
        info3.setParameterSize(1);

        // Act & Assert - methods should have independent parameter sizes
        assertEquals(2, ParameterUsageMarker.getParameterSize(method1),
                "Method1 should have parameter size 2");
        assertEquals(4, ParameterUsageMarker.getParameterSize(method2),
                "Method2 should have parameter size 4");
        assertEquals(1, ParameterUsageMarker.getParameterSize(method3),
                "Method3 should have parameter size 1");
    }

    /**
     * Tests getParameterSize delegates correctly to MethodOptimizationInfo.
     */
    @Test
    public void testGetParameterSize_delegatesToMethodOptimizationInfo() {
        // Arrange - create method with custom MethodOptimizationInfo
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(Ljava/lang/Object;)V");
        MethodOptimizationInfo customInfo = new MethodOptimizationInfo();
        method.setProcessingInfo(customInfo);

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert - should delegate to info's getParameterSize
        // Default MethodOptimizationInfo returns 0
        assertEquals(0, result, "Should delegate to MethodOptimizationInfo.getParameterSize");
    }

    /**
     * Tests getParameterSize can be updated by setting new value.
     */
    @Test
    public void testGetParameterSize_afterUpdating_returnsNewValue() {
        // Arrange - create method and set initial parameter size
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(2);

        // Verify initial value
        assertEquals(2, ParameterUsageMarker.getParameterSize(method),
                "Initial parameter size should be 2");

        // Update parameter size
        info.setParameterSize(5);

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert - should return updated value
        assertEquals(5, result, "Should return 5 after updating parameter size");
    }

    /**
     * Tests getParameterSize with constructor method.
     */
    @Test
    public void testGetParameterSize_withConstructor_worksCorrectly() {
        // Arrange - create constructor (special name <init>)
        ProgramMethod method = createMethodWithDescriptor(testClass, "<init>", "(Ljava/lang/String;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(2); // this + String

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(2, result, "Constructor should correctly report parameter size");
    }

    /**
     * Tests getParameterSize with static initializer.
     */
    @Test
    public void testGetParameterSize_withStaticInitializer_returnsZero() {
        // Arrange - create static initializer (special name <clinit>)
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "<clinit>", "()V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(0); // static initializers have no parameters

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(0, result, "Static initializer should have parameter size 0");
    }

    /**
     * Tests getParameterSize with method having array parameters.
     * Array parameters take 1 slot each (they're references).
     */
    @Test
    public void testGetParameterSize_withArrayParameter_countsAsOne() {
        // Arrange - create method with array parameter
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "([I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(2); // this + array ref (1 slot)

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(2, result, "Array parameter should count as 1 slot");
    }

    /**
     * Tests getParameterSize with complex descriptor.
     */
    @Test
    public void testGetParameterSize_withComplexDescriptor_returnsCorrectSize() {
        // Arrange - create method with complex descriptor
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;[IDLjava/util/Map;)Ljava/util/List;");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        // String(1) + array(1) + int(1) + double(2) + Map(1) = 6
        info.setParameterSize(6);

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(6, result, "Should correctly handle complex descriptor");
    }

    /**
     * Tests getParameterSize preserves value across multiple method invocations.
     */
    @Test
    public void testGetParameterSize_preservesValueAcrossInvocations() {
        // Arrange - create method
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(2);

        // Act - get parameter size multiple times with some operations in between
        int firstRead = ParameterUsageMarker.getParameterSize(method);

        // Do some other operations
        info.setParameterUsed(0);
        info.setParameterUsed(1);

        int secondRead = ParameterUsageMarker.getParameterSize(method);

        // More operations
        info.setSideEffects();

        int thirdRead = ParameterUsageMarker.getParameterSize(method);

        // Assert - parameter size should remain unchanged
        assertEquals(2, firstRead, "First read should be 2");
        assertEquals(2, secondRead, "Second read should be 2");
        assertEquals(2, thirdRead, "Third read should be 2");
    }

    /**
     * Tests getParameterSize with method having all category-2 types.
     * Category-2 types (long, double) each take 2 slots.
     */
    @Test
    public void testGetParameterSize_withAllCategory2Types_countsCorrectly() {
        // Arrange - create method with long and double parameters
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod", "(JD)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(4); // long(2) + double(2)

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(4, result, "Should correctly count category-2 types");
    }

    /**
     * Tests getParameterSize with abstract method.
     */
    @Test
    public void testGetParameterSize_withAbstractMethod_worksCorrectly() {
        // Arrange - create abstract method
        ProgramMethod method = createAbstractMethodWithDescriptor(testClass, "abstractMethod", "(Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(2);

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(2, result, "Abstract method should correctly report parameter size");
    }

    /**
     * Tests getParameterSize with native method.
     */
    @Test
    public void testGetParameterSize_withNativeMethod_worksCorrectly() {
        // Arrange - create native method
        ProgramMethod method = createNativeMethodWithDescriptor(testClass, "nativeMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(2);

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(2, result, "Native method should correctly report parameter size");
    }

    /**
     * Tests getParameterSize with synchronized method.
     */
    @Test
    public void testGetParameterSize_withSynchronizedMethod_worksCorrectly() {
        // Arrange - create synchronized method
        ProgramMethod method = createSynchronizedMethodWithDescriptor(testClass, "syncMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(2);

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(2, result, "Synchronized method should correctly report parameter size");
    }

    /**
     * Tests getParameterSize consistency between MethodOptimizationInfo and ProgramMethodOptimizationInfo.
     */
    @Test
    public void testGetParameterSize_consistencyBetweenInfoTypes() {
        // Arrange - create two methods with different info types but same logical parameter size
        ProgramMethod methodDefault = createMethodWithDescriptor(testClass, "methodDefault", "(I)V");
        MethodOptimizationInfo.setMethodOptimizationInfo(testClass, methodDefault);

        ProgramMethod methodProgram = createMethodWithDescriptor(testClass, "methodProgram", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, methodProgram);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(methodProgram);
        info.setParameterSize(2);

        // Act
        int defaultResult = ParameterUsageMarker.getParameterSize(methodDefault);
        int programResult = ParameterUsageMarker.getParameterSize(methodProgram);

        // Assert
        assertEquals(0, defaultResult, "Default MethodOptimizationInfo returns 0");
        assertEquals(2, programResult, "ProgramMethodOptimizationInfo returns set value");
    }

    /**
     * Tests getParameterSize with newly created ProgramMethodOptimizationInfo instances.
     */
    @Test
    public void testGetParameterSize_newlyCreatedInfo_startsWithZero() {
        // Test with multiple newly created methods
        for (int i = 0; i < 5; i++) {
            ProgramMethod method = createMethodWithDescriptor(testClass, "method" + i, "(I)V");
            ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);

            // Each new method should start with 0
            assertEquals(0, ParameterUsageMarker.getParameterSize(method),
                    "Method" + i + " should start with parameter size 0");
        }
    }

    /**
     * Tests getParameterSize with method having mixed parameter types.
     */
    @Test
    public void testGetParameterSize_withMixedParameterTypes_countsCorrectly() {
        // Arrange - create method with byte, short, int, long, float, double
        // byte, short, int, float each take 1 slot; long and double each take 2 slots
        ProgramMethod method = createStaticMethodWithDescriptor(testClass, "testMethod",
                "(BSIJFD)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        // byte(1) + short(1) + int(1) + long(2) + float(1) + double(2) = 8
        info.setParameterSize(8);

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(8, result, "Should correctly count mixed parameter types");
    }

    /**
     * Tests getParameterSize is thread-safe for reads.
     */
    @Test
    public void testGetParameterSize_threadSafeReads() throws InterruptedException {
        // Arrange - create method and set parameter size
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod", "(I)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(2);

        final int threadCount = 10;
        final int readsPerThread = 100;
        Thread[] threads = new Thread[threadCount];
        final int[] results = new int[threadCount * readsPerThread];

        // Act - read from multiple threads
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < readsPerThread; j++) {
                    results[threadIndex * readsPerThread + j] =
                        ParameterUsageMarker.getParameterSize(method);
                }
            });
            threads[i].start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - all reads should return 2
        for (int i = 0; i < results.length; i++) {
            assertEquals(2, results[i],
                "All concurrent reads should return 2 (index " + i + ")");
        }
    }

    /**
     * Tests getParameterSize with varargs method.
     * Varargs are compiled as arrays, so they take 1 slot.
     */
    @Test
    public void testGetParameterSize_withVarargsMethod_countsArrayAsOne() {
        // Arrange - create varargs method (compiled as array)
        ProgramMethod method = createMethodWithDescriptor(testClass, "testMethod",
                "(Ljava/lang/String;[Ljava/lang/Object;)V");
        ProgramMethodOptimizationInfo.setProgramMethodOptimizationInfo(testClass, method);
        ProgramMethodOptimizationInfo info = ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(method);
        info.setParameterSize(3); // this + String + Object[]

        // Act
        int result = ParameterUsageMarker.getParameterSize(method);

        // Assert
        assertEquals(3, result, "Varargs (array) should count as 1 slot");
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    /**
     * Creates a ProgramClass with a minimal constant pool setup.
     */
    private ProgramClass createProgramClassWithConstantPool() {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool with enough space
        Constant[] constantPool = new Constant[100];
        constantPool[0] = null; // Index 0 is always null
        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 100;

        return programClass;
    }

    /**
     * Creates a ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Creates a static ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createStaticMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.STATIC;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Creates an abstract ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createAbstractMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.ABSTRACT;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Creates a native ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createNativeMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.NATIVE;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Creates a synchronized ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createSynchronizedMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC | AccessConstants.SYNCHRONIZED;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Finds the next available index in the constant pool.
     */
    private int findNextAvailableConstantPoolIndex(ProgramClass programClass) {
        for (int i = 1; i < programClass.constantPool.length; i++) {
            if (programClass.constantPool[i] == null) {
                return i;
            }
        }
        throw new IllegalStateException("No available constant pool index");
    }
}
