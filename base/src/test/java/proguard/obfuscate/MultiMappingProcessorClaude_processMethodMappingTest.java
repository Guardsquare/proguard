package proguard.obfuscate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MultiMappingProcessor#processMethodMapping(String, int, int, String, String, String, String, int, int, String)}.
 * Tests the processMethodMapping method behavior.
 */
public class MultiMappingProcessorClaude_processMethodMappingTest {

    /**
     * Tests processMethodMapping with two processors.
     * Verifies that both processors are called with the correct parameters.
     */
    @Test
    public void testProcessMethodMappingWithTwoProcessors() {
        // Arrange - Create two processors
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping
        multiProcessor.processMethodMapping(
            "OldClass", 1, 10, "void", "method", "()", "NewClass", 1, 10, "a");

        // Assert - Verify both processors were called with correct parameters
        verify(processor1, times(1)).processMethodMapping(
            "OldClass", 1, 10, "void", "method", "()", "NewClass", 1, 10, "a");
        verify(processor2, times(1)).processMethodMapping(
            "OldClass", 1, 10, "void", "method", "()", "NewClass", 1, 10, "a");
    }

    /**
     * Tests processMethodMapping with single processor.
     * Verifies that the single processor is called with the correct parameters.
     */
    @Test
    public void testProcessMethodMappingWithSingleProcessor() {
        // Arrange - Create single processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping
        multiProcessor.processMethodMapping(
            "TestClass", 5, 15, "int", "calculate", "(int,int)", "NewClass", 5, 15, "b");

        // Assert - Verify processor was called
        verify(processor, times(1)).processMethodMapping(
            "TestClass", 5, 15, "int", "calculate", "(int,int)", "NewClass", 5, 15, "b");
    }

    /**
     * Tests processMethodMapping with empty array.
     * Verifies that the method completes without error when no processors are present.
     */
    @Test
    public void testProcessMethodMappingWithEmptyArray() {
        // Arrange - Create MultiMappingProcessor with empty array
        MappingProcessor[] processors = new MappingProcessor[0];
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            multiProcessor.processMethodMapping(
                "Class", 1, 10, "void", "method", "()", "NewClass", 1, 10, "a"),
            "Should handle empty array without throwing exception");
    }

    /**
     * Tests processMethodMapping with multiple processors.
     * Verifies that all three processors are called in order.
     */
    @Test
    public void testProcessMethodMappingWithMultipleProcessors() {
        // Arrange - Create three processors
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        MappingProcessor processor3 = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2, processor3};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping
        multiProcessor.processMethodMapping(
            "Class", 20, 30, "boolean", "isValid", "()", "NewClass", 20, 30, "c");

        // Assert - Verify all three processors were called
        verify(processor1, times(1)).processMethodMapping(
            "Class", 20, 30, "boolean", "isValid", "()", "NewClass", 20, 30, "c");
        verify(processor2, times(1)).processMethodMapping(
            "Class", 20, 30, "boolean", "isValid", "()", "NewClass", 20, 30, "c");
        verify(processor3, times(1)).processMethodMapping(
            "Class", 20, 30, "boolean", "isValid", "()", "NewClass", 20, 30, "c");
    }

    /**
     * Tests processMethodMapping with zero line numbers.
     * Verifies that the method handles zero line numbers (meaning unknown).
     */
    @Test
    public void testProcessMethodMappingWithZeroLineNumbers() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with zero line numbers
        multiProcessor.processMethodMapping(
            "Class", 0, 0, "void", "method", "()", "NewClass", 0, 0, "a");

        // Assert - Verify processor was called with zeros
        verify(processor, times(1)).processMethodMapping(
            "Class", 0, 0, "void", "method", "()", "NewClass", 0, 0, "a");
    }

    /**
     * Tests processMethodMapping with null string parameters.
     * Verifies that the method handles null string parameters correctly.
     */
    @Test
    public void testProcessMethodMappingWithNullStrings() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with null strings
        multiProcessor.processMethodMapping(
            null, 1, 10, null, null, null, null, 1, 10, null);

        // Assert - Verify processor was called with nulls
        verify(processor, times(1)).processMethodMapping(
            null, 1, 10, null, null, null, null, 1, 10, null);
    }

    /**
     * Tests processMethodMapping with empty strings.
     * Verifies that the method handles empty string parameters correctly.
     */
    @Test
    public void testProcessMethodMappingWithEmptyStrings() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with empty strings
        multiProcessor.processMethodMapping(
            "", 0, 0, "", "", "", "", 0, 0, "");

        // Assert - Verify processor was called with empty strings
        verify(processor, times(1)).processMethodMapping(
            "", 0, 0, "", "", "", "", 0, 0, "");
    }

    /**
     * Tests processMethodMapping with complex method arguments.
     * Verifies that the method handles complex argument signatures.
     */
    @Test
    public void testProcessMethodMappingWithComplexArguments() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with complex arguments
        multiProcessor.processMethodMapping(
            "OldClass", 10, 20, "java.util.List",
            "process", "(java.lang.String,int,java.util.List)",
            "NewClass", 10, 20, "a");

        // Assert - Verify processor was called with complex arguments
        verify(processor, times(1)).processMethodMapping(
            "OldClass", 10, 20, "java.util.List",
            "process", "(java.lang.String,int,java.util.List)",
            "NewClass", 10, 20, "a");
    }

    /**
     * Tests processMethodMapping with array return types.
     * Verifies that the method handles array return types correctly.
     */
    @Test
    public void testProcessMethodMappingWithArrayReturnType() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with array return type
        multiProcessor.processMethodMapping(
            "Class", 1, 5, "int[]", "getArray", "()", "NewClass", 1, 5, "a");

        // Assert - Verify processor was called with array return type
        verify(processor, times(1)).processMethodMapping(
            "Class", 1, 5, "int[]", "getArray", "()", "NewClass", 1, 5, "a");
    }

    /**
     * Tests processMethodMapping with large line numbers.
     * Verifies that the method handles large line numbers correctly.
     */
    @Test
    public void testProcessMethodMappingWithLargeLineNumbers() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with large line numbers
        multiProcessor.processMethodMapping(
            "Class", 1000, 2000, "void", "method", "()", "NewClass", 3000, 4000, "a");

        // Assert - Verify processor was called with large line numbers
        verify(processor, times(1)).processMethodMapping(
            "Class", 1000, 2000, "void", "method", "()", "NewClass", 3000, 4000, "a");
    }

    /**
     * Tests processMethodMapping called multiple times.
     * Verifies that the method can be called multiple times with different parameters.
     */
    @Test
    public void testProcessMethodMappingCalledMultipleTimes() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping three times
        multiProcessor.processMethodMapping(
            "Class1", 1, 10, "void", "method1", "()", "NewClass1", 1, 10, "a");
        multiProcessor.processMethodMapping(
            "Class2", 5, 15, "int", "method2", "(int)", "NewClass2", 5, 15, "b");
        multiProcessor.processMethodMapping(
            "Class3", 10, 20, "String", "method3", "(String,int)", "NewClass3", 10, 20, "c");

        // Assert - Verify processor was called three times with different parameters
        verify(processor, times(1)).processMethodMapping(
            "Class1", 1, 10, "void", "method1", "()", "NewClass1", 1, 10, "a");
        verify(processor, times(1)).processMethodMapping(
            "Class2", 5, 15, "int", "method2", "(int)", "NewClass2", 5, 15, "b");
        verify(processor, times(1)).processMethodMapping(
            "Class3", 10, 20, "String", "method3", "(String,int)", "NewClass3", 10, 20, "c");
    }

    /**
     * Tests processMethodMapping with constructor (init method).
     * Verifies that the method handles constructor mappings.
     */
    @Test
    public void testProcessMethodMappingWithConstructor() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with constructor
        multiProcessor.processMethodMapping(
            "OldClass", 10, 15, "void", "<init>", "()", "NewClass", 10, 15, "<init>");

        // Assert - Verify processor was called with constructor
        verify(processor, times(1)).processMethodMapping(
            "OldClass", 10, 15, "void", "<init>", "()", "NewClass", 10, 15, "<init>");
    }

    /**
     * Tests processMethodMapping with static initializer.
     * Verifies that the method handles static initializer mappings.
     */
    @Test
    public void testProcessMethodMappingWithStaticInitializer() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with static initializer
        multiProcessor.processMethodMapping(
            "OldClass", 5, 8, "void", "<clinit>", "()", "NewClass", 5, 8, "<clinit>");

        // Assert - Verify processor was called with static initializer
        verify(processor, times(1)).processMethodMapping(
            "OldClass", 5, 8, "void", "<clinit>", "()", "NewClass", 5, 8, "<clinit>");
    }

    /**
     * Tests processMethodMapping with fully qualified class names.
     * Verifies that the method handles fully qualified class names correctly.
     */
    @Test
    public void testProcessMethodMappingWithFullyQualifiedClassNames() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with fully qualified names
        multiProcessor.processMethodMapping(
            "com.example.package.OldClass", 1, 10, "void", "method", "()",
            "com.obfuscated.a.b.NewClass", 1, 10, "a");

        // Assert - Verify processor was called
        verify(processor, times(1)).processMethodMapping(
            "com.example.package.OldClass", 1, 10, "void", "method", "()",
            "com.obfuscated.a.b.NewClass", 1, 10, "a");
    }

    /**
     * Tests processMethodMapping with different line number ranges.
     * Verifies that the method handles various line number ranges correctly.
     */
    @Test
    public void testProcessMethodMappingWithDifferentLineRanges() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with same first and last line
        multiProcessor.processMethodMapping(
            "Class", 42, 42, "void", "method", "()", "NewClass", 100, 100, "a");

        // Assert - Verify processor was called with single-line method
        verify(processor, times(1)).processMethodMapping(
            "Class", 42, 42, "void", "method", "()", "NewClass", 100, 100, "a");
    }

    /**
     * Tests processMethodMapping with many processors.
     * Verifies that all processors are called even with a large array.
     */
    @Test
    public void testProcessMethodMappingWithManyProcessors() {
        // Arrange - Create five processors
        MappingProcessor[] processors = new MappingProcessor[5];
        for (int i = 0; i < 5; i++) {
            processors[i] = mock(MappingProcessor.class);
        }
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping
        multiProcessor.processMethodMapping(
            "OldClass", 1, 10, "void", "method", "()", "NewClass", 1, 10, "a");

        // Assert - Verify all five processors were called
        for (int i = 0; i < 5; i++) {
            verify(processors[i], times(1)).processMethodMapping(
                "OldClass", 1, 10, "void", "method", "()", "NewClass", 1, 10, "a");
        }
    }

    /**
     * Tests processMethodMapping with concrete MappingProcessor implementation.
     * Verifies that the method works with actual implementations, not just mocks.
     */
    @Test
    public void testProcessMethodMappingWithConcreteImplementation() {
        // Arrange - Create concrete implementation with state tracking
        final StringBuilder callLog = new StringBuilder();

        MappingProcessor concreteProcessor = new MappingProcessor() {
            @Override
            public boolean processClassMapping(String className, String newClassName) {
                return false;
            }

            @Override
            public void processFieldMapping(String className, String fieldType, String fieldName,
                                           String newClassName, String newFieldName) {
            }

            @Override
            public void processMethodMapping(String className, int firstLineNumber, int lastLineNumber,
                                            String methodReturnType, String methodName, String methodArguments,
                                            String newClassName, int newFirstLineNumber, int newLastLineNumber,
                                            String newMethodName) {
                callLog.append("Called: ").append(className).append(".").append(methodName);
            }
        };

        MappingProcessor[] processors = new MappingProcessor[]{concreteProcessor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping
        multiProcessor.processMethodMapping(
            "TestClass", 1, 10, "void", "testMethod", "()", "NewClass", 1, 10, "a");

        // Assert - Verify the concrete processor was called
        assertEquals("Called: TestClass.testMethod", callLog.toString(),
            "Concrete processor should have been called with correct parameters");
    }

    /**
     * Tests processMethodMapping with primitive return types.
     * Verifies that the method handles all primitive return types correctly.
     */
    @Test
    public void testProcessMethodMappingWithPrimitiveReturnTypes() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with different primitive return types
        multiProcessor.processMethodMapping("Class", 1, 5, "int", "getInt", "()", "NewClass", 1, 5, "a");
        multiProcessor.processMethodMapping("Class", 6, 10, "long", "getLong", "()", "NewClass", 6, 10, "b");
        multiProcessor.processMethodMapping("Class", 11, 15, "double", "getDouble", "()", "NewClass", 11, 15, "c");
        multiProcessor.processMethodMapping("Class", 16, 20, "boolean", "isValid", "()", "NewClass", 16, 20, "d");
        multiProcessor.processMethodMapping("Class", 21, 25, "byte", "getByte", "()", "NewClass", 21, 25, "e");
        multiProcessor.processMethodMapping("Class", 26, 30, "char", "getChar", "()", "NewClass", 26, 30, "f");
        multiProcessor.processMethodMapping("Class", 31, 35, "short", "getShort", "()", "NewClass", 31, 35, "g");
        multiProcessor.processMethodMapping("Class", 36, 40, "float", "getFloat", "()", "NewClass", 36, 40, "h");

        // Assert - Verify all primitive return types were processed
        verify(processor, times(1)).processMethodMapping("Class", 1, 5, "int", "getInt", "()", "NewClass", 1, 5, "a");
        verify(processor, times(1)).processMethodMapping("Class", 6, 10, "long", "getLong", "()", "NewClass", 6, 10, "b");
        verify(processor, times(1)).processMethodMapping("Class", 11, 15, "double", "getDouble", "()", "NewClass", 11, 15, "c");
        verify(processor, times(1)).processMethodMapping("Class", 16, 20, "boolean", "isValid", "()", "NewClass", 16, 20, "d");
        verify(processor, times(1)).processMethodMapping("Class", 21, 25, "byte", "getByte", "()", "NewClass", 21, 25, "e");
        verify(processor, times(1)).processMethodMapping("Class", 26, 30, "char", "getChar", "()", "NewClass", 26, 30, "f");
        verify(processor, times(1)).processMethodMapping("Class", 31, 35, "short", "getShort", "()", "NewClass", 31, 35, "g");
        verify(processor, times(1)).processMethodMapping("Class", 36, 40, "float", "getFloat", "()", "NewClass", 36, 40, "h");
    }

    /**
     * Tests processMethodMapping with methods that have no arguments.
     * Verifies that the method handles empty argument list correctly.
     */
    @Test
    public void testProcessMethodMappingWithNoArguments() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with no arguments
        multiProcessor.processMethodMapping(
            "Class", 1, 10, "void", "noArgs", "()", "NewClass", 1, 10, "a");

        // Assert - Verify processor was called with empty arguments
        verify(processor, times(1)).processMethodMapping(
            "Class", 1, 10, "void", "noArgs", "()", "NewClass", 1, 10, "a");
    }

    /**
     * Tests processMethodMapping with methods that have multiple arguments.
     * Verifies that the method handles complex argument lists correctly.
     */
    @Test
    public void testProcessMethodMappingWithMultipleArguments() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with multiple arguments
        multiProcessor.processMethodMapping(
            "Class", 1, 10, "void", "multiArgs",
            "(int,java.lang.String,boolean,java.util.List)",
            "NewClass", 1, 10, "a");

        // Assert - Verify processor was called with multiple arguments
        verify(processor, times(1)).processMethodMapping(
            "Class", 1, 10, "void", "multiArgs",
            "(int,java.lang.String,boolean,java.util.List)",
            "NewClass", 1, 10, "a");
    }

    /**
     * Tests processMethodMapping does not throw when processors throw exceptions.
     * Note: This test verifies current behavior - if a processor throws, it propagates.
     */
    @Test
    public void testProcessMethodMappingWhenProcessorThrowsException() {
        // Arrange - Create processor that throws exception
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        doThrow(new RuntimeException("Test exception")).when(processor1)
            .processMethodMapping(anyString(), anyInt(), anyInt(), anyString(), anyString(),
                anyString(), anyString(), anyInt(), anyInt(), anyString());

        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act & Assert - Exception should propagate
        assertThrows(RuntimeException.class, () ->
            multiProcessor.processMethodMapping(
                "Class", 1, 10, "void", "method", "()", "NewClass", 1, 10, "a"),
            "Exception from processor should propagate");

        // Verify first processor was called but second was not (exception stopped execution)
        verify(processor1, times(1)).processMethodMapping(
            "Class", 1, 10, "void", "method", "()", "NewClass", 1, 10, "a");
        verify(processor2, never()).processMethodMapping(
            anyString(), anyInt(), anyInt(), anyString(), anyString(),
            anyString(), anyString(), anyInt(), anyInt(), anyString());
    }

    /**
     * Tests processMethodMapping with same parameters called repeatedly.
     * Verifies that the method handles repeated calls with identical parameters.
     */
    @Test
    public void testProcessMethodMappingRepeatedCalls() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping three times with same parameters
        multiProcessor.processMethodMapping(
            "Class", 1, 10, "void", "method", "()", "NewClass", 1, 10, "a");
        multiProcessor.processMethodMapping(
            "Class", 1, 10, "void", "method", "()", "NewClass", 1, 10, "a");
        multiProcessor.processMethodMapping(
            "Class", 1, 10, "void", "method", "()", "NewClass", 1, 10, "a");

        // Assert - Verify processor was called three times
        verify(processor, times(3)).processMethodMapping(
            "Class", 1, 10, "void", "method", "()", "NewClass", 1, 10, "a");
    }

    /**
     * Tests processMethodMapping with inner class methods.
     * Verifies that the method handles inner class notation correctly.
     */
    @Test
    public void testProcessMethodMappingWithInnerClassMethods() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with inner class
        multiProcessor.processMethodMapping(
            "OuterClass$InnerClass", 1, 10, "void", "method", "()",
            "a$b", 1, 10, "c");

        // Assert - Verify processor was called with inner class notation
        verify(processor, times(1)).processMethodMapping(
            "OuterClass$InnerClass", 1, 10, "void", "method", "()",
            "a$b", 1, 10, "c");
    }

    /**
     * Tests processMethodMapping execution order.
     * Verifies that processors are called in the order they appear in the array.
     */
    @Test
    public void testProcessMethodMappingExecutionOrder() {
        // Arrange - Create processors with order tracking
        final StringBuilder orderLog = new StringBuilder();

        MappingProcessor processor1 = new MappingProcessor() {
            @Override
            public boolean processClassMapping(String className, String newClassName) {
                return false;
            }

            @Override
            public void processFieldMapping(String className, String fieldType, String fieldName,
                                           String newClassName, String newFieldName) {
            }

            @Override
            public void processMethodMapping(String className, int firstLineNumber, int lastLineNumber,
                                            String methodReturnType, String methodName, String methodArguments,
                                            String newClassName, int newFirstLineNumber, int newLastLineNumber,
                                            String newMethodName) {
                orderLog.append("1");
            }
        };

        MappingProcessor processor2 = new MappingProcessor() {
            @Override
            public boolean processClassMapping(String className, String newClassName) {
                return false;
            }

            @Override
            public void processFieldMapping(String className, String fieldType, String fieldName,
                                           String newClassName, String newFieldName) {
            }

            @Override
            public void processMethodMapping(String className, int firstLineNumber, int lastLineNumber,
                                            String methodReturnType, String methodName, String methodArguments,
                                            String newClassName, int newFirstLineNumber, int newLastLineNumber,
                                            String newMethodName) {
                orderLog.append("2");
            }
        };

        MappingProcessor processor3 = new MappingProcessor() {
            @Override
            public boolean processClassMapping(String className, String newClassName) {
                return false;
            }

            @Override
            public void processFieldMapping(String className, String fieldType, String fieldName,
                                           String newClassName, String newFieldName) {
            }

            @Override
            public void processMethodMapping(String className, int firstLineNumber, int lastLineNumber,
                                            String methodReturnType, String methodName, String methodArguments,
                                            String newClassName, int newFirstLineNumber, int newLastLineNumber,
                                            String newMethodName) {
                orderLog.append("3");
            }
        };

        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2, processor3};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping
        multiProcessor.processMethodMapping(
            "Class", 1, 10, "void", "method", "()", "NewClass", 1, 10, "a");

        // Assert - Verify processors were called in order
        assertEquals("123", orderLog.toString(),
            "Processors should be called in the order they appear in the array");
    }

    /**
     * Tests processMethodMapping with obfuscated method names.
     * Verifies that the method handles typical obfuscated names (single characters).
     */
    @Test
    public void testProcessMethodMappingWithObfuscatedNames() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with obfuscated names
        multiProcessor.processMethodMapping(
            "a", 1, 10, "b", "c", "d", "e", 1, 10, "f");

        // Assert - Verify processor was called with obfuscated names
        verify(processor, times(1)).processMethodMapping(
            "a", 1, 10, "b", "c", "d", "e", 1, 10, "f");
    }

    /**
     * Tests processMethodMapping with long method names.
     * Verifies that the method handles long string parameters.
     */
    @Test
    public void testProcessMethodMappingWithLongMethodNames() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with long method name
        String longMethodName = "thisIsAVeryLongMethodNameThatSomeoneDecidedToUseInTheirCode";
        multiProcessor.processMethodMapping(
            "Class", 1, 10, "void", longMethodName, "()", "NewClass", 1, 10, "a");

        // Assert - Verify processor was called with long method name
        verify(processor, times(1)).processMethodMapping(
            "Class", 1, 10, "void", longMethodName, "()", "NewClass", 1, 10, "a");
    }

    /**
     * Tests processMethodMapping with negative line numbers.
     * Verifies that the method handles negative line numbers (edge case).
     */
    @Test
    public void testProcessMethodMappingWithNegativeLineNumbers() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with negative line numbers
        multiProcessor.processMethodMapping(
            "Class", -1, -1, "void", "method", "()", "NewClass", -1, -1, "a");

        // Assert - Verify processor was called with negative line numbers
        verify(processor, times(1)).processMethodMapping(
            "Class", -1, -1, "void", "method", "()", "NewClass", -1, -1, "a");
    }

    /**
     * Tests processMethodMapping with special characters in method names.
     * Verifies that the method handles special characters in method names.
     */
    @Test
    public void testProcessMethodMappingWithSpecialCharactersInMethodName() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processMethodMapping with special characters
        multiProcessor.processMethodMapping(
            "Class", 1, 10, "void", "$method", "()", "NewClass", 1, 10, "$a");

        // Assert - Verify processor was called with special characters
        verify(processor, times(1)).processMethodMapping(
            "Class", 1, 10, "void", "$method", "()", "NewClass", 1, 10, "$a");
    }
}
