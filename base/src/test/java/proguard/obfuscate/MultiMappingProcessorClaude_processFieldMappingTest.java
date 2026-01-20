package proguard.obfuscate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MultiMappingProcessor#processFieldMapping(String, String, String, String, String)}.
 * Tests the processFieldMapping method behavior.
 */
public class MultiMappingProcessorClaude_processFieldMappingTest {

    /**
     * Tests processFieldMapping with two processors.
     * Verifies that both processors are called with the correct parameters.
     */
    @Test
    public void testProcessFieldMappingWithTwoProcessors() {
        // Arrange - Create two processors
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping
        multiProcessor.processFieldMapping("OldClass", "int", "field", "NewClass", "newField");

        // Assert - Verify both processors were called with correct parameters
        verify(processor1, times(1)).processFieldMapping("OldClass", "int", "field", "NewClass", "newField");
        verify(processor2, times(1)).processFieldMapping("OldClass", "int", "field", "NewClass", "newField");
    }

    /**
     * Tests processFieldMapping with single processor.
     * Verifies that the single processor is called with the correct parameters.
     */
    @Test
    public void testProcessFieldMappingWithSingleProcessor() {
        // Arrange - Create single processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping
        multiProcessor.processFieldMapping("OldClass", "String", "name", "NewClass", "a");

        // Assert - Verify processor was called
        verify(processor, times(1)).processFieldMapping("OldClass", "String", "name", "NewClass", "a");
    }

    /**
     * Tests processFieldMapping with empty array.
     * Verifies that the method completes without error when no processors are present.
     */
    @Test
    public void testProcessFieldMappingWithEmptyArray() {
        // Arrange - Create MultiMappingProcessor with empty array
        MappingProcessor[] processors = new MappingProcessor[0];
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() ->
            multiProcessor.processFieldMapping("OldClass", "int", "field", "NewClass", "newField"),
            "Should handle empty array without throwing exception");
    }

    /**
     * Tests processFieldMapping with multiple processors.
     * Verifies that all three processors are called in order.
     */
    @Test
    public void testProcessFieldMappingWithMultipleProcessors() {
        // Arrange - Create three processors
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        MappingProcessor processor3 = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2, processor3};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping
        multiProcessor.processFieldMapping("Class", "boolean", "flag", "NewClass", "f");

        // Assert - Verify all three processors were called
        verify(processor1, times(1)).processFieldMapping("Class", "boolean", "flag", "NewClass", "f");
        verify(processor2, times(1)).processFieldMapping("Class", "boolean", "flag", "NewClass", "f");
        verify(processor3, times(1)).processFieldMapping("Class", "boolean", "flag", "NewClass", "f");
    }

    /**
     * Tests processFieldMapping with null parameters.
     * Verifies that the method handles null parameters correctly.
     */
    @Test
    public void testProcessFieldMappingWithNullParameters() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping with null parameters
        multiProcessor.processFieldMapping(null, null, null, null, null);

        // Assert - Verify processor was called with nulls
        verify(processor, times(1)).processFieldMapping(null, null, null, null, null);
    }

    /**
     * Tests processFieldMapping with empty strings.
     * Verifies that the method handles empty string parameters correctly.
     */
    @Test
    public void testProcessFieldMappingWithEmptyStrings() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping with empty strings
        multiProcessor.processFieldMapping("", "", "", "", "");

        // Assert - Verify processor was called with empty strings
        verify(processor, times(1)).processFieldMapping("", "", "", "", "");
    }

    /**
     * Tests processFieldMapping called multiple times.
     * Verifies that the method can be called multiple times.
     */
    @Test
    public void testProcessFieldMappingCalledMultipleTimes() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping three times
        multiProcessor.processFieldMapping("Class1", "int", "field1", "NewClass1", "a");
        multiProcessor.processFieldMapping("Class2", "String", "field2", "NewClass2", "b");
        multiProcessor.processFieldMapping("Class3", "boolean", "field3", "NewClass3", "c");

        // Assert - Verify processor was called three times with different parameters
        verify(processor, times(1)).processFieldMapping("Class1", "int", "field1", "NewClass1", "a");
        verify(processor, times(1)).processFieldMapping("Class2", "String", "field2", "NewClass2", "b");
        verify(processor, times(1)).processFieldMapping("Class3", "boolean", "field3", "NewClass3", "c");
    }

    /**
     * Tests processFieldMapping with complex field types.
     * Verifies that the method handles complex type strings correctly.
     */
    @Test
    public void testProcessFieldMappingWithComplexFieldTypes() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping with complex types
        multiProcessor.processFieldMapping(
            "OldClass",
            "java.util.List<java.lang.String>",
            "items",
            "NewClass",
            "a"
        );

        // Assert - Verify processor was called with complex type
        verify(processor, times(1)).processFieldMapping(
            "OldClass",
            "java.util.List<java.lang.String>",
            "items",
            "NewClass",
            "a"
        );
    }

    /**
     * Tests processFieldMapping with array field types.
     * Verifies that the method handles array type strings correctly.
     */
    @Test
    public void testProcessFieldMappingWithArrayFieldTypes() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping with array types
        multiProcessor.processFieldMapping("OldClass", "int[]", "array", "NewClass", "a");
        multiProcessor.processFieldMapping("OldClass", "String[][]", "matrix", "NewClass", "b");

        // Assert - Verify processor was called with array types
        verify(processor, times(1)).processFieldMapping("OldClass", "int[]", "array", "NewClass", "a");
        verify(processor, times(1)).processFieldMapping("OldClass", "String[][]", "matrix", "NewClass", "b");
    }

    /**
     * Tests processFieldMapping with fully qualified class names.
     * Verifies that the method handles fully qualified class names correctly.
     */
    @Test
    public void testProcessFieldMappingWithFullyQualifiedClassNames() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping with fully qualified names
        multiProcessor.processFieldMapping(
            "com.example.package.OldClass",
            "java.lang.String",
            "field",
            "com.obfuscated.a.b.NewClass",
            "a"
        );

        // Assert - Verify processor was called
        verify(processor, times(1)).processFieldMapping(
            "com.example.package.OldClass",
            "java.lang.String",
            "field",
            "com.obfuscated.a.b.NewClass",
            "a"
        );
    }

    /**
     * Tests processFieldMapping with primitive types.
     * Verifies that the method handles all primitive types correctly.
     */
    @Test
    public void testProcessFieldMappingWithPrimitiveTypes() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping with different primitive types
        multiProcessor.processFieldMapping("Class", "int", "intField", "NewClass", "a");
        multiProcessor.processFieldMapping("Class", "long", "longField", "NewClass", "b");
        multiProcessor.processFieldMapping("Class", "double", "doubleField", "NewClass", "c");
        multiProcessor.processFieldMapping("Class", "boolean", "boolField", "NewClass", "d");
        multiProcessor.processFieldMapping("Class", "byte", "byteField", "NewClass", "e");
        multiProcessor.processFieldMapping("Class", "char", "charField", "NewClass", "f");
        multiProcessor.processFieldMapping("Class", "short", "shortField", "NewClass", "g");
        multiProcessor.processFieldMapping("Class", "float", "floatField", "NewClass", "h");

        // Assert - Verify all primitive types were processed
        verify(processor, times(1)).processFieldMapping("Class", "int", "intField", "NewClass", "a");
        verify(processor, times(1)).processFieldMapping("Class", "long", "longField", "NewClass", "b");
        verify(processor, times(1)).processFieldMapping("Class", "double", "doubleField", "NewClass", "c");
        verify(processor, times(1)).processFieldMapping("Class", "boolean", "boolField", "NewClass", "d");
        verify(processor, times(1)).processFieldMapping("Class", "byte", "byteField", "NewClass", "e");
        verify(processor, times(1)).processFieldMapping("Class", "char", "charField", "NewClass", "f");
        verify(processor, times(1)).processFieldMapping("Class", "short", "shortField", "NewClass", "g");
        verify(processor, times(1)).processFieldMapping("Class", "float", "floatField", "NewClass", "h");
    }

    /**
     * Tests processFieldMapping with inner class field types.
     * Verifies that the method handles inner class notation correctly.
     */
    @Test
    public void testProcessFieldMappingWithInnerClassFieldTypes() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping with inner class type
        multiProcessor.processFieldMapping(
            "OuterClass$InnerClass",
            "OuterClass$InnerClass$DeepInner",
            "field",
            "a$b",
            "c"
        );

        // Assert - Verify processor was called with inner class notation
        verify(processor, times(1)).processFieldMapping(
            "OuterClass$InnerClass",
            "OuterClass$InnerClass$DeepInner",
            "field",
            "a$b",
            "c"
        );
    }

    /**
     * Tests processFieldMapping with many processors.
     * Verifies that all processors are called even with a large array.
     */
    @Test
    public void testProcessFieldMappingWithManyProcessors() {
        // Arrange - Create five processors
        MappingProcessor[] processors = new MappingProcessor[5];
        for (int i = 0; i < 5; i++) {
            processors[i] = mock(MappingProcessor.class);
        }
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping
        multiProcessor.processFieldMapping("OldClass", "int", "field", "NewClass", "a");

        // Assert - Verify all five processors were called
        for (int i = 0; i < 5; i++) {
            verify(processors[i], times(1)).processFieldMapping("OldClass", "int", "field", "NewClass", "a");
        }
    }

    /**
     * Tests processFieldMapping with concrete MappingProcessor implementation.
     * Verifies that the method works with actual implementations, not just mocks.
     */
    @Test
    public void testProcessFieldMappingWithConcreteImplementation() {
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
                callLog.append("Called: ").append(className).append(".").append(fieldName);
            }

            @Override
            public void processMethodMapping(String className, int firstLineNumber, int lastLineNumber,
                                            String methodReturnType, String methodName, String methodArguments,
                                            String newClassName, int newFirstLineNumber, int newLastLineNumber,
                                            String newMethodName) {
            }
        };

        MappingProcessor[] processors = new MappingProcessor[]{concreteProcessor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping
        multiProcessor.processFieldMapping("TestClass", "int", "value", "NewClass", "a");

        // Assert - Verify the concrete processor was called
        assertEquals("Called: TestClass.value", callLog.toString(),
            "Concrete processor should have been called with correct parameters");
    }

    /**
     * Tests processFieldMapping with long field names.
     * Verifies that the method handles long string parameters.
     */
    @Test
    public void testProcessFieldMappingWithLongFieldNames() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping with long field name
        String longFieldName = "thisIsAVeryLongFieldNameThatSomeoneDecidedToUseInTheirCode";
        multiProcessor.processFieldMapping("OldClass", "String", longFieldName, "NewClass", "a");

        // Assert - Verify processor was called with long field name
        verify(processor, times(1)).processFieldMapping("OldClass", "String", longFieldName, "NewClass", "a");
    }

    /**
     * Tests processFieldMapping does not throw when processors throw exceptions.
     * Note: This test verifies current behavior - if a processor throws, it propagates.
     */
    @Test
    public void testProcessFieldMappingWhenProcessorThrowsException() {
        // Arrange - Create processor that throws exception
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        doThrow(new RuntimeException("Test exception")).when(processor1)
            .processFieldMapping(anyString(), anyString(), anyString(), anyString(), anyString());

        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act & Assert - Exception should propagate
        assertThrows(RuntimeException.class, () ->
            multiProcessor.processFieldMapping("Class", "int", "field", "NewClass", "a"),
            "Exception from processor should propagate");

        // Verify first processor was called but second was not (exception stopped execution)
        verify(processor1, times(1)).processFieldMapping("Class", "int", "field", "NewClass", "a");
        verify(processor2, never()).processFieldMapping(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    /**
     * Tests processFieldMapping with same parameters called repeatedly.
     * Verifies that the method handles repeated calls with identical parameters.
     */
    @Test
    public void testProcessFieldMappingRepeatedCalls() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping three times with same parameters
        multiProcessor.processFieldMapping("Class", "int", "field", "NewClass", "a");
        multiProcessor.processFieldMapping("Class", "int", "field", "NewClass", "a");
        multiProcessor.processFieldMapping("Class", "int", "field", "NewClass", "a");

        // Assert - Verify processor was called three times
        verify(processor, times(3)).processFieldMapping("Class", "int", "field", "NewClass", "a");
    }

    /**
     * Tests processFieldMapping with obfuscated field names.
     * Verifies that the method handles typical obfuscated names (single characters).
     */
    @Test
    public void testProcessFieldMappingWithObfuscatedNames() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping with obfuscated names
        multiProcessor.processFieldMapping("a", "b", "c", "d", "e");

        // Assert - Verify processor was called with obfuscated names
        verify(processor, times(1)).processFieldMapping("a", "b", "c", "d", "e");
    }

    /**
     * Tests processFieldMapping with mixed null and non-null parameters.
     * Verifies that the method handles partial null parameters.
     */
    @Test
    public void testProcessFieldMappingWithMixedNullParameters() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping with some nulls
        multiProcessor.processFieldMapping("Class", null, "field", "NewClass", null);

        // Assert - Verify processor was called with mixed nulls
        verify(processor, times(1)).processFieldMapping("Class", null, "field", "NewClass", null);
    }

    /**
     * Tests processFieldMapping execution order.
     * Verifies that processors are called in the order they appear in the array.
     */
    @Test
    public void testProcessFieldMappingExecutionOrder() {
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
                orderLog.append("1");
            }

            @Override
            public void processMethodMapping(String className, int firstLineNumber, int lastLineNumber,
                                            String methodReturnType, String methodName, String methodArguments,
                                            String newClassName, int newFirstLineNumber, int newLastLineNumber,
                                            String newMethodName) {
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
                orderLog.append("2");
            }

            @Override
            public void processMethodMapping(String className, int firstLineNumber, int lastLineNumber,
                                            String methodReturnType, String methodName, String methodArguments,
                                            String newClassName, int newFirstLineNumber, int newLastLineNumber,
                                            String newMethodName) {
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
                orderLog.append("3");
            }

            @Override
            public void processMethodMapping(String className, int firstLineNumber, int lastLineNumber,
                                            String methodReturnType, String methodName, String methodArguments,
                                            String newClassName, int newFirstLineNumber, int newLastLineNumber,
                                            String newMethodName) {
            }
        };

        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2, processor3};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping
        multiProcessor.processFieldMapping("Class", "int", "field", "NewClass", "a");

        // Assert - Verify processors were called in order
        assertEquals("123", orderLog.toString(),
            "Processors should be called in the order they appear in the array");
    }

    /**
     * Tests processFieldMapping with special characters in field names.
     * Verifies that the method handles special characters in field names.
     */
    @Test
    public void testProcessFieldMappingWithSpecialCharactersInFieldName() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processFieldMapping with special characters (underscore is common)
        multiProcessor.processFieldMapping("Class", "int", "_private_field", "NewClass", "a");
        multiProcessor.processFieldMapping("Class", "int", "$field", "NewClass", "b");

        // Assert - Verify processor was called with special characters
        verify(processor, times(1)).processFieldMapping("Class", "int", "_private_field", "NewClass", "a");
        verify(processor, times(1)).processFieldMapping("Class", "int", "$field", "NewClass", "b");
    }
}
