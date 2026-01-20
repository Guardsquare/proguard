package proguard.obfuscate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MultiMappingProcessor#processClassMapping(String, String)}.
 * Tests the processClassMapping method behavior.
 */
public class MultiMappingProcessorClaude_processClassMappingTest {

    /**
     * Tests processClassMapping with all processors returning false.
     * Verifies that the method returns false when no processor returns true.
     */
    @Test
    public void testProcessClassMappingAllProcessorsReturnFalse() {
        // Arrange - Create processors that return false
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        when(processor1.processClassMapping("OldClass", "NewClass")).thenReturn(false);
        when(processor2.processClassMapping("OldClass", "NewClass")).thenReturn(false);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping
        boolean result = multiProcessor.processClassMapping("OldClass", "NewClass");

        // Assert - Verify result is false
        assertFalse(result, "Should return false when all processors return false");
        verify(processor1, times(1)).processClassMapping("OldClass", "NewClass");
        verify(processor2, times(1)).processClassMapping("OldClass", "NewClass");
    }

    /**
     * Tests processClassMapping with all processors returning true.
     * Verifies that the method returns true when all processors return true.
     */
    @Test
    public void testProcessClassMappingAllProcessorsReturnTrue() {
        // Arrange - Create processors that return true
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        when(processor1.processClassMapping("OldClass", "NewClass")).thenReturn(true);
        when(processor2.processClassMapping("OldClass", "NewClass")).thenReturn(true);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping
        boolean result = multiProcessor.processClassMapping("OldClass", "NewClass");

        // Assert - Verify result is true
        assertTrue(result, "Should return true when all processors return true");
        verify(processor1, times(1)).processClassMapping("OldClass", "NewClass");
        verify(processor2, times(1)).processClassMapping("OldClass", "NewClass");
    }

    /**
     * Tests processClassMapping with first processor returning true.
     * Verifies that the method returns true when at least one processor returns true.
     */
    @Test
    public void testProcessClassMappingFirstProcessorReturnsTrue() {
        // Arrange - First processor returns true, second returns false
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        when(processor1.processClassMapping("OldClass", "NewClass")).thenReturn(true);
        when(processor2.processClassMapping("OldClass", "NewClass")).thenReturn(false);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping
        boolean result = multiProcessor.processClassMapping("OldClass", "NewClass");

        // Assert - Verify result is true (OR operation)
        assertTrue(result, "Should return true when first processor returns true");
        verify(processor1, times(1)).processClassMapping("OldClass", "NewClass");
        verify(processor2, times(1)).processClassMapping("OldClass", "NewClass");
    }

    /**
     * Tests processClassMapping with second processor returning true.
     * Verifies that the method returns true when at least one processor returns true.
     */
    @Test
    public void testProcessClassMappingSecondProcessorReturnsTrue() {
        // Arrange - First processor returns false, second returns true
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        when(processor1.processClassMapping("OldClass", "NewClass")).thenReturn(false);
        when(processor2.processClassMapping("OldClass", "NewClass")).thenReturn(true);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping
        boolean result = multiProcessor.processClassMapping("OldClass", "NewClass");

        // Assert - Verify result is true (OR operation)
        assertTrue(result, "Should return true when second processor returns true");
        verify(processor1, times(1)).processClassMapping("OldClass", "NewClass");
        verify(processor2, times(1)).processClassMapping("OldClass", "NewClass");
    }

    /**
     * Tests processClassMapping with empty array of processors.
     * Verifies that the method returns false when there are no processors.
     */
    @Test
    public void testProcessClassMappingWithEmptyArray() {
        // Arrange - Create MultiMappingProcessor with empty array
        MappingProcessor[] processors = new MappingProcessor[0];
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping
        boolean result = multiProcessor.processClassMapping("OldClass", "NewClass");

        // Assert - Verify result is false (no processors to process)
        assertFalse(result, "Should return false when no processors are present");
    }

    /**
     * Tests processClassMapping with single processor returning true.
     * Verifies that the method works correctly with a single processor.
     */
    @Test
    public void testProcessClassMappingWithSingleProcessorReturningTrue() {
        // Arrange - Create single processor returning true
        MappingProcessor processor = mock(MappingProcessor.class);
        when(processor.processClassMapping("OldClass", "NewClass")).thenReturn(true);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping
        boolean result = multiProcessor.processClassMapping("OldClass", "NewClass");

        // Assert - Verify result is true
        assertTrue(result, "Should return true when single processor returns true");
        verify(processor, times(1)).processClassMapping("OldClass", "NewClass");
    }

    /**
     * Tests processClassMapping with single processor returning false.
     * Verifies that the method works correctly with a single processor.
     */
    @Test
    public void testProcessClassMappingWithSingleProcessorReturningFalse() {
        // Arrange - Create single processor returning false
        MappingProcessor processor = mock(MappingProcessor.class);
        when(processor.processClassMapping("OldClass", "NewClass")).thenReturn(false);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping
        boolean result = multiProcessor.processClassMapping("OldClass", "NewClass");

        // Assert - Verify result is false
        assertFalse(result, "Should return false when single processor returns false");
        verify(processor, times(1)).processClassMapping("OldClass", "NewClass");
    }

    /**
     * Tests processClassMapping with multiple processors where middle one returns true.
     * Verifies that all processors are called and the result is true.
     */
    @Test
    public void testProcessClassMappingWithMiddleProcessorReturningTrue() {
        // Arrange - Three processors, middle one returns true
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        MappingProcessor processor3 = mock(MappingProcessor.class);
        when(processor1.processClassMapping("OldClass", "NewClass")).thenReturn(false);
        when(processor2.processClassMapping("OldClass", "NewClass")).thenReturn(true);
        when(processor3.processClassMapping("OldClass", "NewClass")).thenReturn(false);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2, processor3};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping
        boolean result = multiProcessor.processClassMapping("OldClass", "NewClass");

        // Assert - Verify result is true and all processors were called
        assertTrue(result, "Should return true when any processor returns true");
        verify(processor1, times(1)).processClassMapping("OldClass", "NewClass");
        verify(processor2, times(1)).processClassMapping("OldClass", "NewClass");
        verify(processor3, times(1)).processClassMapping("OldClass", "NewClass");
    }

    /**
     * Tests processClassMapping with null class names.
     * Verifies that the method handles null parameters.
     */
    @Test
    public void testProcessClassMappingWithNullClassNames() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        when(processor.processClassMapping(null, null)).thenReturn(false);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping with nulls
        boolean result = multiProcessor.processClassMapping(null, null);

        // Assert - Verify method was called with nulls
        assertFalse(result, "Should handle null parameters");
        verify(processor, times(1)).processClassMapping(null, null);
    }

    /**
     * Tests processClassMapping with empty strings.
     * Verifies that the method handles empty string parameters.
     */
    @Test
    public void testProcessClassMappingWithEmptyStrings() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        when(processor.processClassMapping("", "")).thenReturn(true);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping with empty strings
        boolean result = multiProcessor.processClassMapping("", "");

        // Assert - Verify method was called with empty strings
        assertTrue(result, "Should handle empty string parameters");
        verify(processor, times(1)).processClassMapping("", "");
    }

    /**
     * Tests processClassMapping called multiple times with same parameters.
     * Verifies that the method can be called multiple times.
     */
    @Test
    public void testProcessClassMappingCalledMultipleTimes() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        when(processor.processClassMapping("OldClass", "NewClass")).thenReturn(true);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping multiple times
        boolean result1 = multiProcessor.processClassMapping("OldClass", "NewClass");
        boolean result2 = multiProcessor.processClassMapping("OldClass", "NewClass");
        boolean result3 = multiProcessor.processClassMapping("OldClass", "NewClass");

        // Assert - Verify all calls succeeded
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        verify(processor, times(3)).processClassMapping("OldClass", "NewClass");
    }

    /**
     * Tests processClassMapping with different class names on each call.
     * Verifies that the method correctly delegates different parameters.
     */
    @Test
    public void testProcessClassMappingWithDifferentClassNames() {
        // Arrange - Create processor with different return values for different inputs
        MappingProcessor processor = mock(MappingProcessor.class);
        when(processor.processClassMapping("Class1", "NewClass1")).thenReturn(true);
        when(processor.processClassMapping("Class2", "NewClass2")).thenReturn(false);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping with different class names
        boolean result1 = multiProcessor.processClassMapping("Class1", "NewClass1");
        boolean result2 = multiProcessor.processClassMapping("Class2", "NewClass2");

        // Assert - Verify correct results for each call
        assertTrue(result1, "Should return true for Class1");
        assertFalse(result2, "Should return false for Class2");
        verify(processor, times(1)).processClassMapping("Class1", "NewClass1");
        verify(processor, times(1)).processClassMapping("Class2", "NewClass2");
    }

    /**
     * Tests processClassMapping with many processors (all returning false).
     * Verifies that the method correctly handles multiple processors.
     */
    @Test
    public void testProcessClassMappingWithManyProcessorsAllFalse() {
        // Arrange - Create five processors all returning false
        MappingProcessor[] processors = new MappingProcessor[5];
        for (int i = 0; i < 5; i++) {
            processors[i] = mock(MappingProcessor.class);
            when(processors[i].processClassMapping("OldClass", "NewClass")).thenReturn(false);
        }
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping
        boolean result = multiProcessor.processClassMapping("OldClass", "NewClass");

        // Assert - Verify result is false and all processors were called
        assertFalse(result, "Should return false when all processors return false");
        for (int i = 0; i < 5; i++) {
            verify(processors[i], times(1)).processClassMapping("OldClass", "NewClass");
        }
    }

    /**
     * Tests processClassMapping with many processors (last one returning true).
     * Verifies that the method correctly aggregates results with OR operation.
     */
    @Test
    public void testProcessClassMappingWithManyProcessorsLastTrue() {
        // Arrange - Create five processors, last one returns true
        MappingProcessor[] processors = new MappingProcessor[5];
        for (int i = 0; i < 4; i++) {
            processors[i] = mock(MappingProcessor.class);
            when(processors[i].processClassMapping("OldClass", "NewClass")).thenReturn(false);
        }
        processors[4] = mock(MappingProcessor.class);
        when(processors[4].processClassMapping("OldClass", "NewClass")).thenReturn(true);
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping
        boolean result = multiProcessor.processClassMapping("OldClass", "NewClass");

        // Assert - Verify result is true and all processors were called
        assertTrue(result, "Should return true when last processor returns true");
        for (int i = 0; i < 5; i++) {
            verify(processors[i], times(1)).processClassMapping("OldClass", "NewClass");
        }
    }

    /**
     * Tests processClassMapping with concrete MappingProcessor implementation.
     * Verifies that the method works with actual implementations, not just mocks.
     */
    @Test
    public void testProcessClassMappingWithConcreteImplementation() {
        // Arrange - Create concrete implementations
        MappingProcessor concreteProcessor1 = new MappingProcessor() {
            @Override
            public boolean processClassMapping(String className, String newClassName) {
                return className != null && className.equals("SpecialClass");
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
            }
        };

        MappingProcessor concreteProcessor2 = new MappingProcessor() {
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
            }
        };

        MappingProcessor[] processors = new MappingProcessor[]{concreteProcessor1, concreteProcessor2};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping with special class
        boolean result1 = multiProcessor.processClassMapping("SpecialClass", "NewSpecialClass");
        boolean result2 = multiProcessor.processClassMapping("OtherClass", "NewOtherClass");

        // Assert - Verify results
        assertTrue(result1, "Should return true for SpecialClass");
        assertFalse(result2, "Should return false for OtherClass");
    }

    /**
     * Tests processClassMapping with long class names.
     * Verifies that the method handles long string parameters.
     */
    @Test
    public void testProcessClassMappingWithLongClassNames() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        String longClassName = "com.example.very.long.package.name.with.many.segments.ClassName";
        String longNewClassName = "com.example.obfuscated.a.b.c.d.e.f.g.h.NewClassName";
        when(processor.processClassMapping(longClassName, longNewClassName)).thenReturn(true);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping with long names
        boolean result = multiProcessor.processClassMapping(longClassName, longNewClassName);

        // Assert - Verify method handles long names
        assertTrue(result, "Should handle long class names");
        verify(processor, times(1)).processClassMapping(longClassName, longNewClassName);
    }

    /**
     * Tests processClassMapping with special characters in class names.
     * Verifies that the method handles special characters.
     */
    @Test
    public void testProcessClassMappingWithSpecialCharacters() {
        // Arrange - Create processor
        MappingProcessor processor = mock(MappingProcessor.class);
        String className = "Class$Inner";
        String newClassName = "a$b";
        when(processor.processClassMapping(className, newClassName)).thenReturn(false);
        MappingProcessor[] processors = new MappingProcessor[]{processor};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping with special characters
        boolean result = multiProcessor.processClassMapping(className, newClassName);

        // Assert - Verify method handles special characters
        assertFalse(result, "Should handle special characters in class names");
        verify(processor, times(1)).processClassMapping(className, newClassName);
    }

    /**
     * Tests processClassMapping ensuring all processors are called even if first returns true.
     * Verifies that the method doesn't short-circuit (uses |= not ||).
     */
    @Test
    public void testProcessClassMappingDoesNotShortCircuit() {
        // Arrange - Create processors where first returns true
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        MappingProcessor processor3 = mock(MappingProcessor.class);
        when(processor1.processClassMapping("OldClass", "NewClass")).thenReturn(true);
        when(processor2.processClassMapping("OldClass", "NewClass")).thenReturn(false);
        when(processor3.processClassMapping("OldClass", "NewClass")).thenReturn(false);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2, processor3};
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Act - Call processClassMapping
        boolean result = multiProcessor.processClassMapping("OldClass", "NewClass");

        // Assert - Verify all processors were called (no short-circuit)
        assertTrue(result, "Should return true");
        verify(processor1, times(1)).processClassMapping("OldClass", "NewClass");
        verify(processor2, times(1)).processClassMapping("OldClass", "NewClass");
        verify(processor3, times(1)).processClassMapping("OldClass", "NewClass");
    }
}
