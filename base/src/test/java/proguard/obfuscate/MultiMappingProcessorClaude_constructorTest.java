package proguard.obfuscate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MultiMappingProcessor} constructor.
 * Tests MultiMappingProcessor(MappingProcessor[]) constructor.
 */
public class MultiMappingProcessorClaude_constructorTest {

    /**
     * Tests the constructor with a valid array of MappingProcessors.
     * Verifies that the processor can be instantiated with valid mapping processors.
     */
    @Test
    public void testConstructorWithValidMappingProcessors() {
        // Arrange - Create valid MappingProcessors
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2};

        // Act - Create MultiMappingProcessor with valid parameters
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Assert - Verify the processor was created successfully
        assertNotNull(multiProcessor, "MultiMappingProcessor should be instantiated successfully");
    }

    /**
     * Tests the constructor with an empty array of MappingProcessors.
     * Verifies that the constructor accepts an empty array.
     */
    @Test
    public void testConstructorWithEmptyArray() {
        // Arrange - Create empty array
        MappingProcessor[] processors = new MappingProcessor[0];

        // Act - Create MultiMappingProcessor with empty array
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Assert - Verify the processor was created successfully
        assertNotNull(multiProcessor, "MultiMappingProcessor should be instantiated with empty array");
    }

    /**
     * Tests the constructor with null MappingProcessor array.
     * Verifies that the constructor accepts null.
     */
    @Test
    public void testConstructorWithNullArray() {
        // Act - Create processor with null array
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(null);

        // Assert - Verify the processor was created
        assertNotNull(multiProcessor, "MultiMappingProcessor should be instantiated even with null array");
    }

    /**
     * Tests the constructor creates an instance that implements MappingProcessor interface.
     * Verifies that MultiMappingProcessor can be used as a MappingProcessor.
     */
    @Test
    public void testConstructorCreatesInstanceOfMappingProcessor() {
        // Arrange - Create valid MappingProcessors
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};

        // Act - Create processor
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Assert - Verify the processor implements MappingProcessor
        assertInstanceOf(MappingProcessor.class, multiProcessor,
                "MultiMappingProcessor should implement MappingProcessor interface");
    }

    /**
     * Tests that the constructor properly stores the processors by verifying delegation behavior.
     * Verifies the stored processors are called when processClassMapping is invoked.
     */
    @Test
    public void testConstructorStoresProcessorsForClassMapping() {
        // Arrange - Create mock processors
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        when(processor1.processClassMapping(anyString(), anyString())).thenReturn(true);
        when(processor2.processClassMapping(anyString(), anyString())).thenReturn(false);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2};

        // Act - Create processor and invoke processClassMapping
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);
        boolean result = multiProcessor.processClassMapping("OldClass", "NewClass");

        // Assert - Verify both processors were called
        verify(processor1, times(1)).processClassMapping("OldClass", "NewClass");
        verify(processor2, times(1)).processClassMapping("OldClass", "NewClass");
        assertTrue(result, "Result should be true since processor1 returned true");
    }

    /**
     * Tests that the constructor properly stores the processors by verifying delegation behavior.
     * Verifies the stored processors are called when processFieldMapping is invoked.
     */
    @Test
    public void testConstructorStoresProcessorsForFieldMapping() {
        // Arrange - Create mock processors
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2};

        // Act - Create processor and invoke processFieldMapping
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);
        multiProcessor.processFieldMapping("OldClass", "int", "field", "NewClass", "newField");

        // Assert - Verify both processors were called
        verify(processor1, times(1)).processFieldMapping("OldClass", "int", "field", "NewClass", "newField");
        verify(processor2, times(1)).processFieldMapping("OldClass", "int", "field", "NewClass", "newField");
    }

    /**
     * Tests that the constructor properly stores the processors by verifying delegation behavior.
     * Verifies the stored processors are called when processMethodMapping is invoked.
     */
    @Test
    public void testConstructorStoresProcessorsForMethodMapping() {
        // Arrange - Create mock processors
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2};

        // Act - Create processor and invoke processMethodMapping
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);
        multiProcessor.processMethodMapping(
                "OldClass", 1, 10, "void", "method", "()",
                "NewClass", 1, 10, "newMethod");

        // Assert - Verify both processors were called
        verify(processor1, times(1)).processMethodMapping(
                "OldClass", 1, 10, "void", "method", "()",
                "NewClass", 1, 10, "newMethod");
        verify(processor2, times(1)).processMethodMapping(
                "OldClass", 1, 10, "void", "method", "()",
                "NewClass", 1, 10, "newMethod");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that each processor instance is independent.
     */
    @Test
    public void testMultipleProcessorInstances() {
        // Arrange - Create different processor arrays for each instance
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        MappingProcessor[] processors1 = new MappingProcessor[]{processor1};
        MappingProcessor[] processors2 = new MappingProcessor[]{processor2};

        // Act - Create two processor instances
        MultiMappingProcessor multiProcessor1 = new MultiMappingProcessor(processors1);
        MultiMappingProcessor multiProcessor2 = new MultiMappingProcessor(processors2);

        // Assert - Verify both processors were created successfully
        assertNotNull(multiProcessor1, "First processor should be created");
        assertNotNull(multiProcessor2, "Second processor should be created");
        assertNotSame(multiProcessor1, multiProcessor2, "Processor instances should be different objects");
    }

    /**
     * Tests the constructor with a single MappingProcessor.
     * Verifies that the processor works with a single-element array.
     */
    @Test
    public void testConstructorWithSingleProcessor() {
        // Arrange - Create a single processor
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};

        // Act - Create MultiMappingProcessor
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Assert - Verify the processor was created successfully
        assertNotNull(multiProcessor, "MultiMappingProcessor should be instantiated with single processor");
    }

    /**
     * Tests the constructor with multiple processors (more than 2).
     * Verifies that the processor can handle arrays with multiple elements.
     */
    @Test
    public void testConstructorWithMultipleProcessors() {
        // Arrange - Create multiple processors
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        MappingProcessor processor3 = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2, processor3};

        // Act - Create MultiMappingProcessor
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Assert - Verify the processor was created successfully
        assertNotNull(multiProcessor, "MultiMappingProcessor should be instantiated with multiple processors");
    }

    /**
     * Tests that the constructor properly stores all processors in the array.
     * Verifies that all three processors are called when methods are invoked.
     */
    @Test
    public void testConstructorStoresAllProcessors() {
        // Arrange - Create three mock processors
        MappingProcessor processor1 = mock(MappingProcessor.class);
        MappingProcessor processor2 = mock(MappingProcessor.class);
        MappingProcessor processor3 = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor1, processor2, processor3};

        // Act - Create processor and invoke processFieldMapping
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);
        multiProcessor.processFieldMapping("Class", "String", "field", "NewClass", "newField");

        // Assert - Verify all three processors were called
        verify(processor1, times(1)).processFieldMapping("Class", "String", "field", "NewClass", "newField");
        verify(processor2, times(1)).processFieldMapping("Class", "String", "field", "NewClass", "newField");
        verify(processor3, times(1)).processFieldMapping("Class", "String", "field", "NewClass", "newField");
    }

    /**
     * Tests the constructor with an array containing null elements.
     * Verifies the constructor accepts an array with null elements.
     */
    @Test
    public void testConstructorWithArrayContainingNulls() {
        // Arrange - Create array with null elements
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor, null, processor};

        // Act - Create MultiMappingProcessor
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Assert - Verify the processor was created
        assertNotNull(multiProcessor, "MultiMappingProcessor should be instantiated with array containing nulls");
    }

    /**
     * Tests that the constructor with same array reference used multiple times.
     * Verifies that the same array can be used for multiple processor instances.
     */
    @Test
    public void testConstructorWithSameArrayReference() {
        // Arrange - Create a processor array to use for multiple instances
        MappingProcessor processor = mock(MappingProcessor.class);
        MappingProcessor[] processors = new MappingProcessor[]{processor};

        // Act - Create two processor instances with the same array
        MultiMappingProcessor multiProcessor1 = new MultiMappingProcessor(processors);
        MultiMappingProcessor multiProcessor2 = new MultiMappingProcessor(processors);

        // Assert - Verify both processors were created successfully
        assertNotNull(multiProcessor1, "First processor should be created");
        assertNotNull(multiProcessor2, "Second processor should be created");
        assertNotSame(multiProcessor1, multiProcessor2, "Processor instances should be different objects");
    }

    /**
     * Tests the constructor with concrete MappingProcessor implementations.
     * Verifies that the processor works with actual processor implementations.
     */
    @Test
    public void testConstructorWithConcreteMappingProcessorImplementation() {
        // Arrange - Create concrete implementations of MappingProcessor
        MappingProcessor concreteProcessor1 = new MappingProcessor() {
            @Override
            public boolean processClassMapping(String className, String newClassName) {
                return false;
            }

            @Override
            public void processFieldMapping(String className, String fieldType, String fieldName,
                                           String newClassName, String newFieldName) {
                // Simple implementation for testing
            }

            @Override
            public void processMethodMapping(String className, int firstLineNumber, int lastLineNumber,
                                            String methodReturnType, String methodName, String methodArguments,
                                            String newClassName, int newFirstLineNumber, int newLastLineNumber,
                                            String newMethodName) {
                // Simple implementation for testing
            }
        };

        MappingProcessor concreteProcessor2 = new MappingProcessor() {
            @Override
            public boolean processClassMapping(String className, String newClassName) {
                return true;
            }

            @Override
            public void processFieldMapping(String className, String fieldType, String fieldName,
                                           String newClassName, String newFieldName) {
                // Simple implementation for testing
            }

            @Override
            public void processMethodMapping(String className, int firstLineNumber, int lastLineNumber,
                                            String methodReturnType, String methodName, String methodArguments,
                                            String newClassName, int newFirstLineNumber, int newLastLineNumber,
                                            String newMethodName) {
                // Simple implementation for testing
            }
        };

        MappingProcessor[] processors = new MappingProcessor[]{concreteProcessor1, concreteProcessor2};

        // Act - Create processor with concrete implementations
        MultiMappingProcessor multiProcessor = new MultiMappingProcessor(processors);

        // Assert - Verify the processor was created successfully
        assertNotNull(multiProcessor, "MultiMappingProcessor should be instantiated with concrete processors");
    }

    /**
     * Tests the constructor with nested MultiMappingProcessors.
     * Verifies that MultiMappingProcessors can be composed together.
     */
    @Test
    public void testConstructorWithNestedMultiMappingProcessors() {
        // Arrange - Create nested MultiMappingProcessors
        MappingProcessor innerProcessor = mock(MappingProcessor.class);
        MappingProcessor[] innerProcessors = new MappingProcessor[]{innerProcessor};
        MultiMappingProcessor innerMultiProcessor = new MultiMappingProcessor(innerProcessors);

        MappingProcessor outerProcessor = mock(MappingProcessor.class);
        MappingProcessor[] outerProcessors = new MappingProcessor[]{innerMultiProcessor, outerProcessor};

        // Act - Create outer MultiMappingProcessor
        MultiMappingProcessor outerMultiProcessor = new MultiMappingProcessor(outerProcessors);

        // Assert - Verify the processor was created successfully
        assertNotNull(outerMultiProcessor, "Outer MultiMappingProcessor should be created");
        assertNotNull(innerMultiProcessor, "Inner MultiMappingProcessor should exist");
        assertNotSame(outerMultiProcessor, innerMultiProcessor, "Nested processors should be different objects");
    }
}
