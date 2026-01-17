package proguard.configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ConfigurationLogger.checkGetMethods(Class, String) method.
 * This method logs suggestions for keeping all public methods when getMethods() is called.
 */
public class ConfigurationLoggerClaude_checkGetMethodsTest {

    private PrintStream originalErr;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        originalErr = System.err;
        outputStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        System.setErr(originalErr);
    }

    // ============ Null Parameter Tests ============

    @Test
    public void testCheckGetMethods_nullClass_throwsNPE() {
        assertThrows(NullPointerException.class, () -> {
            ConfigurationLogger.checkGetMethods(null, "com.example.Caller");
        });
    }

    @Test
    public void testCheckGetMethods_nullCallingClassName_doesNotThrow() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String.class, null);
        });
    }

    @Test
    public void testCheckGetMethods_bothParametersNull_throwsNPE() {
        assertThrows(NullPointerException.class, () -> {
            ConfigurationLogger.checkGetMethods(null, null);
        });
    }

    // ============ Empty String Tests ============

    @Test
    public void testCheckGetMethods_emptyCallingClassName() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String.class, "");
        });
    }

    @Test
    public void testCheckGetMethods_whitespaceCallingClassName() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(Integer.class, "   ");
        });
    }

    // ============ Standard Class Types Tests ============

    @Test
    public void testCheckGetMethods_withStringClass() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String.class, "com.example.TestCaller");
        });
    }

    @Test
    public void testCheckGetMethods_withIntegerClass() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(Integer.class, "com.example.Caller");
        });
    }

    @Test
    public void testCheckGetMethods_withObjectClass() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(Object.class, "com.example.ObjectCaller");
        });
    }

    @Test
    public void testCheckGetMethods_withListClass() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(List.class, "com.example.ListCaller");
        });
    }

    @Test
    public void testCheckGetMethods_withArrayListClass() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(ArrayList.class, "com.example.ArrayListCaller");
        });
    }

    // ============ Array Class Tests ============

    @Test
    public void testCheckGetMethods_withArrayClass() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String[].class, "com.example.ArrayCaller");
        });
    }

    @Test
    public void testCheckGetMethods_withMultiDimensionalArray() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(int[][].class, "com.example.MultiArrayCaller");
        });
    }

    @Test
    public void testCheckGetMethods_withPrimitiveArray() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(int[].class, "com.example.PrimitiveCaller");
        });
    }

    // ============ Primitive Types Tests ============

    @Test
    public void testCheckGetMethods_withIntPrimitive() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(int.class, "com.example.IntCaller");
        });
    }

    @Test
    public void testCheckGetMethods_withBooleanPrimitive() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(boolean.class, "com.example.BooleanCaller");
        });
    }

    @Test
    public void testCheckGetMethods_withDoublePrimitive() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(double.class, "com.example.DoubleCaller");
        });
    }

    // ============ Special Class Types Tests ============

    @Test
    public void testCheckGetMethods_withEnumClass() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(Thread.State.class, "com.example.EnumCaller");
        });
    }

    @Test
    public void testCheckGetMethods_withInterfaceClass() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(Runnable.class, "com.example.InterfaceCaller");
        });
    }

    @Test
    public void testCheckGetMethods_withAbstractClass() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(Number.class, "com.example.AbstractCaller");
        });
    }

    @Test
    public void testCheckGetMethods_withExceptionClass() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(RuntimeException.class, "com.example.ExceptionCaller");
        });
    }

    @Test
    public void testCheckGetMethods_withThrowableClass() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(Throwable.class, "com.example.ThrowableCaller");
        });
    }

    // ============ Edge Cases Tests ============

    @Test
    public void testCheckGetMethods_withVoidType() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(void.class, "com.example.VoidCaller");
        });
    }

    @Test
    public void testCheckGetMethods_withThisClass() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(ConfigurationLoggerClaude_checkGetMethodsTest.class, "com.example.SelfCaller");
        });
    }

    @Test
    public void testCheckGetMethods_withConfigurationLoggerClass() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(ConfigurationLogger.class, "com.example.LoggerCaller");
        });
    }

    // ============ Calling Class Name Format Tests ============

    @Test
    public void testCheckGetMethods_withSimpleClassName() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String.class, "SimpleName");
        });
    }

    @Test
    public void testCheckGetMethods_withFullyQualifiedName() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String.class, "com.example.package.ClassName");
        });
    }

    @Test
    public void testCheckGetMethods_withInnerClassName() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String.class, "com.example.OuterClass$InnerClass");
        });
    }

    @Test
    public void testCheckGetMethods_withUnicodeCharacters() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String.class, "com.example.クラス");
        });
    }

    @Test
    public void testCheckGetMethods_withSpecialCharacters() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String.class, "com.example.Class_123");
        });
    }

    @Test
    public void testCheckGetMethods_withMixedCaseClassName() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String.class, "CoM.ExAmPlE.MiXeDcAsE");
        });
    }

    // ============ Multiple Calls Tests ============

    @Test
    public void testCheckGetMethods_multipleCalls_sameClass() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String.class, "com.example.Caller1");
            ConfigurationLogger.checkGetMethods(String.class, "com.example.Caller2");
            ConfigurationLogger.checkGetMethods(String.class, "com.example.Caller3");
        });
    }

    @Test
    public void testCheckGetMethods_multipleCalls_differentClasses() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String.class, "com.example.Caller");
            ConfigurationLogger.checkGetMethods(Integer.class, "com.example.Caller");
            ConfigurationLogger.checkGetMethods(Double.class, "com.example.Caller");
        });
    }

    @Test
    public void testCheckGetMethods_rapidSequentialCalls() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                ConfigurationLogger.checkGetMethods(String.class, "com.example.RapidCaller" + i);
            }
        });
    }

    // ============ Thread Safety Tests ============

    @Test
    public void testCheckGetMethods_concurrentCalls() throws InterruptedException {
        int threadCount = 10;
        int callsPerThread = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Throwable> exceptions = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < callsPerThread; j++) {
                        ConfigurationLogger.checkGetMethods(
                            String.class,
                            "com.example.Thread" + threadId + "Call" + j
                        );
                    }
                } catch (Throwable t) {
                    synchronized (exceptions) {
                        exceptions.add(t);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        assertTrue(exceptions.isEmpty(), "No exceptions should occur during concurrent execution");
    }

    @Test
    public void testCheckGetMethods_concurrentCallsDifferentClasses() throws InterruptedException {
        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Class<?>[] classes = {String.class, Integer.class, Double.class, Boolean.class, Long.class};
        List<Throwable> exceptions = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    ConfigurationLogger.checkGetMethods(
                        classes[threadId],
                        "com.example.ConcurrentCaller" + threadId
                    );
                } catch (Throwable t) {
                    synchronized (exceptions) {
                        exceptions.add(t);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        assertTrue(exceptions.isEmpty(), "No exceptions should occur during concurrent execution");
    }

    // ============ Parameter Independence Tests ============

    @Test
    public void testCheckGetMethods_callingClassNameDoesNotAffectBehavior() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String.class, "Caller1");
            ConfigurationLogger.checkGetMethods(String.class, "Caller2");
            ConfigurationLogger.checkGetMethods(String.class, null);
            ConfigurationLogger.checkGetMethods(String.class, "");
        });
    }

    @Test
    public void testCheckGetMethods_differentClassesSameCaller() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String.class, "com.example.Caller");
            ConfigurationLogger.checkGetMethods(Integer.class, "com.example.Caller");
            ConfigurationLogger.checkGetMethods(Object.class, "com.example.Caller");
        });
    }

    // ============ Comparison with checkGetDeclaredMethods ============

    @Test
    public void testCheckGetMethods_comparisonWithCheckGetDeclaredMethods() {
        assertDoesNotThrow(() -> {
            // Call both methods with same parameters
            ConfigurationLogger.checkGetMethods(String.class, "com.example.Caller");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Caller");

            // Both should handle the same edge cases
            ConfigurationLogger.checkGetMethods(int[].class, "com.example.ArrayCaller");
            ConfigurationLogger.checkGetDeclaredMethods(int[].class, "com.example.ArrayCaller");

            // Both should handle null calling class name
            ConfigurationLogger.checkGetMethods(Object.class, null);
            ConfigurationLogger.checkGetDeclaredMethods(Object.class, null);
        });
    }

    @Test
    public void testCheckGetMethods_bothMethodsThrowOnNullClass() {
        assertThrows(NullPointerException.class, () -> {
            ConfigurationLogger.checkGetMethods(null, "com.example.Caller");
        });

        assertThrows(NullPointerException.class, () -> {
            ConfigurationLogger.checkGetDeclaredMethods(null, "com.example.Caller");
        });
    }

    // ============ Integration Tests ============

    @Test
    public void testCheckGetMethods_mixedWithOtherChecks() {
        assertDoesNotThrow(() -> {
            ConfigurationLogger.checkGetMethods(String.class, "com.example.Caller");
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Caller");
            ConfigurationLogger.checkGetMethods(Integer.class, "com.example.Caller");
            ConfigurationLogger.checkGetDeclaredMethods(Integer.class, "com.example.Caller");
        });
    }

    @Test
    public void testCheckGetMethods_afterMultipleOtherOperations() {
        assertDoesNotThrow(() -> {
            // Simulate a realistic scenario with multiple logging operations
            ConfigurationLogger.checkGetDeclaredMethods(String.class, "com.example.Step1");
            ConfigurationLogger.checkGetMethods(String.class, "com.example.Step2");
            ConfigurationLogger.checkGetDeclaredMethods(Integer.class, "com.example.Step3");
            ConfigurationLogger.checkGetMethods(Integer.class, "com.example.Step4");
        });
    }
}
