package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.BootstrapMethodsAttribute;
import proguard.classfile.attribute.visitor.BootstrapMethodInfoVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link StringConcatenationConverter#visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)}.
 *
 * The visitBootstrapMethodsAttribute method delegates to the BootstrapMethodsAttribute to accept
 * a specific bootstrap method entry at the referencedBootstrapMethodIndex. This method is invoked
 * during the string concatenation conversion process to extract the recipe and constants from
 * the bootstrap method used by the invokedynamic instruction.
 */
public class StringConcatenationConverterClaude_visitBootstrapMethodsAttributeTest {

    private StringConcatenationConverter converter;
    private CodeAttributeEditor codeAttributeEditor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private BootstrapMethodsAttribute bootstrapMethodsAttribute;

    @BeforeEach
    public void setUp() {
        codeAttributeEditor = mock(CodeAttributeEditor.class);
        extraInstructionVisitor = mock(InstructionVisitor.class);
        converter = new StringConcatenationConverter(extraInstructionVisitor, codeAttributeEditor);
        clazz = mock(ProgramClass.class);
        bootstrapMethodsAttribute = mock(BootstrapMethodsAttribute.class);
    }

    /**
     * Tests that visitBootstrapMethodsAttribute calls bootstrapMethodEntryAccept on the attribute.
     * This is the primary behavior of this method - delegating to the attribute with the
     * referencedBootstrapMethodIndex that was set during visitConstantInstruction.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_callsBootstrapMethodEntryAccept() {
        // Arrange
        // The referencedBootstrapMethodIndex is set by visitConstantInstruction, but we can't
        // access it directly. We'll test that the method calls bootstrapMethodEntryAccept.

        // Act
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - verify that bootstrapMethodEntryAccept was called
        // Note: The method should be called with the internal referencedBootstrapMethodIndex
        verify(bootstrapMethodsAttribute, times(1)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(converter)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute can be called with valid mock objects without throwing exceptions.
     * The method should complete successfully when given valid inputs.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute can be called with null Clazz parameter.
     * The attribute's bootstrapMethodEntryAccept will receive the null, which should be passed through.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withNullClazz_callsAttribute() {
        // Act
        converter.visitBootstrapMethodsAttribute(null, bootstrapMethodsAttribute);

        // Assert - verify that bootstrapMethodEntryAccept was called even with null clazz
        verify(bootstrapMethodsAttribute, times(1)).bootstrapMethodEntryAccept(
                isNull(),
                anyInt(),
                eq(converter)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute with null attribute throws NullPointerException.
     * Since the method calls a method on the attribute, null will cause an exception.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withNullAttribute_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> converter.visitBootstrapMethodsAttribute(clazz, null));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute can be called multiple times in succession.
     * Each call should delegate to the attribute's bootstrapMethodEntryAccept method.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_calledMultipleTimes_delegatesEachTime() {
        // Act
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - verify that bootstrapMethodEntryAccept was called three times
        verify(bootstrapMethodsAttribute, times(3)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(converter)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute passes the converter as the visitor.
     * The converter implements BootstrapMethodInfoVisitor, so it should be passed as the visitor.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_passesConverterAsVisitor() {
        // Act
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - verify that the converter itself is passed as the visitor
        verify(bootstrapMethodsAttribute).bootstrapMethodEntryAccept(
                any(Clazz.class),
                anyInt(),
                argThat(visitor -> visitor == converter)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works with different Clazz instances.
     * The method should work with any Clazz implementation.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withDifferentClazzInstances_delegatesCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);

        // Act
        converter.visitBootstrapMethodsAttribute(clazz1, bootstrapMethodsAttribute);
        converter.visitBootstrapMethodsAttribute(clazz2, bootstrapMethodsAttribute);
        converter.visitBootstrapMethodsAttribute(clazz3, bootstrapMethodsAttribute);

        // Assert - verify that bootstrapMethodEntryAccept was called three times
        verify(bootstrapMethodsAttribute, times(3)).bootstrapMethodEntryAccept(
                any(Clazz.class),
                anyInt(),
                eq(converter)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works with different BootstrapMethodsAttribute instances.
     * Each attribute should have its bootstrapMethodEntryAccept method called.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withDifferentAttributes_delegatesCorrectly() {
        // Arrange
        BootstrapMethodsAttribute attr1 = mock(BootstrapMethodsAttribute.class);
        BootstrapMethodsAttribute attr2 = mock(BootstrapMethodsAttribute.class);
        BootstrapMethodsAttribute attr3 = mock(BootstrapMethodsAttribute.class);

        // Act
        converter.visitBootstrapMethodsAttribute(clazz, attr1);
        converter.visitBootstrapMethodsAttribute(clazz, attr2);
        converter.visitBootstrapMethodsAttribute(clazz, attr3);

        // Assert - verify each attribute had its method called exactly once
        verify(attr1, times(1)).bootstrapMethodEntryAccept(eq(clazz), anyInt(), eq(converter));
        verify(attr2, times(1)).bootstrapMethodEntryAccept(eq(clazz), anyInt(), eq(converter));
        verify(attr3, times(1)).bootstrapMethodEntryAccept(eq(clazz), anyInt(), eq(converter));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute execution completes quickly.
     * Since it's a simple delegation method, it should have minimal overhead.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_executesQuickly() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - call the method many times
        for (int i = 0; i < 1000; i++) {
            converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete very quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100,
                "visitBootstrapMethodsAttribute should execute quickly as it's a simple delegation");
    }

    /**
     * Tests that multiple converters can independently call visitBootstrapMethodsAttribute.
     * Each converter should maintain its own independent state for referencedBootstrapMethodIndex.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_multipleConvertersIndependent() {
        // Arrange
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);
        StringConcatenationConverter converter1 = new StringConcatenationConverter(null, editor1);
        StringConcatenationConverter converter2 = new StringConcatenationConverter(null, editor2);

        BootstrapMethodsAttribute attr1 = mock(BootstrapMethodsAttribute.class);
        BootstrapMethodsAttribute attr2 = mock(BootstrapMethodsAttribute.class);

        // Act
        converter1.visitBootstrapMethodsAttribute(clazz, attr1);
        converter2.visitBootstrapMethodsAttribute(clazz, attr2);

        // Assert - verify each converter called its respective attribute
        verify(attr1, times(1)).bootstrapMethodEntryAccept(eq(clazz), anyInt(), eq(converter1));
        verify(attr2, times(1)).bootstrapMethodEntryAccept(eq(clazz), anyInt(), eq(converter2));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute passes the converter (which implements BootstrapMethodInfoVisitor).
     * The converter should be passed as an instance of BootstrapMethodInfoVisitor.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_passesConverterAsBootstrapMethodInfoVisitor() {
        // Act
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - verify that a BootstrapMethodInfoVisitor was passed (which is the converter)
        verify(bootstrapMethodsAttribute).bootstrapMethodEntryAccept(
                any(Clazz.class),
                anyInt(),
                any(BootstrapMethodInfoVisitor.class)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute with different combinations of parameters works correctly.
     * Various combinations should all result in proper delegation.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withVariousParameterCombinations() {
        // Arrange
        ProgramClass programClass1 = mock(ProgramClass.class);
        ProgramClass programClass2 = mock(ProgramClass.class);
        BootstrapMethodsAttribute attr1 = mock(BootstrapMethodsAttribute.class);
        BootstrapMethodsAttribute attr2 = mock(BootstrapMethodsAttribute.class);

        // Act & Assert - should not throw any exception with various combinations
        assertDoesNotThrow(() -> {
            converter.visitBootstrapMethodsAttribute(programClass1, attr1);
            converter.visitBootstrapMethodsAttribute(programClass2, attr1);
            converter.visitBootstrapMethodsAttribute(programClass1, attr2);
            converter.visitBootstrapMethodsAttribute(programClass2, attr2);
        });

        // Verify all calls were made
        verify(attr1, times(2)).bootstrapMethodEntryAccept(any(Clazz.class), anyInt(), eq(converter));
        verify(attr2, times(2)).bootstrapMethodEntryAccept(any(Clazz.class), anyInt(), eq(converter));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute maintains correct behavior across many operations.
     * The converter should remain in a valid state after many operations.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_afterManyOperations_stillWorksCorrectly() {
        // Act - perform many operations
        for (int i = 0; i < 100; i++) {
            converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        }

        // Assert - final call should still work correctly
        assertDoesNotThrow(() -> converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));

        // Verify the total number of calls
        verify(bootstrapMethodsAttribute, times(101)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(converter)
        );
    }

    /**
     * Tests visitBootstrapMethodsAttribute returns normally (no return value to verify).
     * Verifies the method signature and behavior (void return type).
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_returnsNormally() {
        // Act - method has void return type, just verify it completes
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - if we reach here, the method completed normally
        verify(bootstrapMethodsAttribute, times(1)).bootstrapMethodEntryAccept(
                any(Clazz.class),
                anyInt(),
                eq(converter)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute with sequential calls works correctly.
     * Sequential calls should all succeed and delegate properly.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_sequentialCalls() {
        // Act - make sequential calls
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - all calls should have been delegated
        verify(bootstrapMethodsAttribute, times(3)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(converter)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute works correctly when the attribute throws an exception.
     * The exception should propagate through without being caught.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_whenAttributeThrowsException_propagatesException() {
        // Arrange
        doThrow(new RuntimeException("Test exception"))
                .when(bootstrapMethodsAttribute)
                .bootstrapMethodEntryAccept(any(), anyInt(), any());

        // Act & Assert - exception should propagate
        assertThrows(RuntimeException.class,
                () -> converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute uses the referencedBootstrapMethodIndex.
     * Although we can't directly verify the index value without reflection, we can verify it's passed to the attribute.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_usesReferencedBootstrapMethodIndex() {
        // Act
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - verify that an integer index was passed (we can't verify the exact value
        // without reflection, but we can verify the method was called with the correct parameters)
        verify(bootstrapMethodsAttribute).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(), // This is the referencedBootstrapMethodIndex
                eq(converter)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute can handle rapid alternating calls.
     * Rapid calls with different attributes should all work correctly.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_rapidAlternatingCalls() {
        // Arrange
        BootstrapMethodsAttribute attr1 = mock(BootstrapMethodsAttribute.class);
        BootstrapMethodsAttribute attr2 = mock(BootstrapMethodsAttribute.class);

        // Act - rapid alternating calls
        for (int i = 0; i < 50; i++) {
            converter.visitBootstrapMethodsAttribute(clazz, attr1);
            converter.visitBootstrapMethodsAttribute(clazz, attr2);
        }

        // Assert - verify each attribute was called 50 times
        verify(attr1, times(50)).bootstrapMethodEntryAccept(eq(clazz), anyInt(), eq(converter));
        verify(attr2, times(50)).bootstrapMethodEntryAccept(eq(clazz), anyInt(), eq(converter));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute passes the correct Clazz to bootstrapMethodEntryAccept.
     * The exact Clazz instance provided should be passed through.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_passesCorrectClazz() {
        // Arrange
        ProgramClass specificClass = mock(ProgramClass.class);

        // Act
        converter.visitBootstrapMethodsAttribute(specificClass, bootstrapMethodsAttribute);

        // Assert - verify the specific clazz was passed through
        verify(bootstrapMethodsAttribute).bootstrapMethodEntryAccept(
                same(specificClass),
                anyInt(),
                eq(converter)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute with same parameters repeatedly calls the attribute.
     * Each call should result in a delegation to the attribute.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_repeatedCallsWithSameParameters_delegatesEachTime() {
        // Act - call multiple times with same parameters
        for (int i = 0; i < 10; i++) {
            converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        }

        // Assert - verify bootstrapMethodEntryAccept was called 10 times
        verify(bootstrapMethodsAttribute, times(10)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(converter)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute doesn't interact with the converter's dependencies.
     * The delegation method should not use codeAttributeEditor or extraInstructionVisitor directly.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_doesNotDirectlyInteractWithConverterDependencies() {
        // Act
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - verify no direct interactions with dependencies during delegation
        // Note: The dependencies may be used indirectly through callback to visitBootstrapMethodInfo
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that visitBootstrapMethodsAttribute with converter created with null extraInstructionVisitor.
     * The method should work correctly even when the converter has null dependencies.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withNullExtraVisitorInConverter_doesNotThrow() {
        // Arrange
        converter = new StringConcatenationConverter(null, codeAttributeEditor);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));

        // Verify the delegation still occurred
        verify(bootstrapMethodsAttribute, times(1)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(converter)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute with converter created with null codeAttributeEditor.
     * The method should work correctly even when the converter has null dependencies.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withNullCodeEditorInConverter_doesNotThrow() {
        // Arrange
        converter = new StringConcatenationConverter(extraInstructionVisitor, null);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));

        // Verify the delegation still occurred
        verify(bootstrapMethodsAttribute, times(1)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(converter)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute with converter created with both dependencies null.
     * The method should work correctly even when the converter has all null dependencies.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withBothConverterDependenciesNull_doesNotThrow() {
        // Arrange
        converter = new StringConcatenationConverter(null, null);

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));

        // Verify the delegation still occurred
        verify(bootstrapMethodsAttribute, times(1)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(converter)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute doesn't throw when attribute callback succeeds.
     * The method should complete without exceptions when the delegation succeeds.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_whenAttributeCallbackSucceeds_doesNotThrow() {
        // Arrange
        doNothing().when(bootstrapMethodsAttribute).bootstrapMethodEntryAccept(any(), anyInt(), any());

        // Act & Assert
        assertDoesNotThrow(() -> converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute handles different exception types from the attribute.
     * Various exception types thrown by the attribute should all propagate correctly.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_withDifferentExceptionTypes_propagatesCorrectly() {
        // Test RuntimeException
        doThrow(new RuntimeException("Test runtime exception"))
                .when(bootstrapMethodsAttribute)
                .bootstrapMethodEntryAccept(any(), anyInt(), any());
        assertThrows(RuntimeException.class,
                () -> converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));

        // Reset and test IllegalArgumentException
        reset(bootstrapMethodsAttribute);
        doThrow(new IllegalArgumentException("Test illegal argument"))
                .when(bootstrapMethodsAttribute)
                .bootstrapMethodEntryAccept(any(), anyInt(), any());
        assertThrows(IllegalArgumentException.class,
                () -> converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));

        // Reset and test IllegalStateException
        reset(bootstrapMethodsAttribute);
        doThrow(new IllegalStateException("Test illegal state"))
                .when(bootstrapMethodsAttribute)
                .bootstrapMethodEntryAccept(any(), anyInt(), any());
        assertThrows(IllegalStateException.class,
                () -> converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute));
    }

    /**
     * Tests that visitBootstrapMethodsAttribute is idempotent when called with the same parameters.
     * Multiple calls with the same parameters should produce the same delegation behavior.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_isIdempotent() {
        // Act - call multiple times with same parameters
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

        // Assert - all calls should have been handled identically
        verify(bootstrapMethodsAttribute, times(3)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(converter)
        );
    }

    /**
     * Tests that visitBootstrapMethodsAttribute can be used in a typical string concatenation workflow.
     * This simulates how the method is typically invoked during the conversion process.
     */
    @Test
    public void testVisitBootstrapMethodsAttribute_inTypicalWorkflow_delegatesCorrectly() {
        // Arrange - Simulate a typical workflow where visitConstantInstruction sets up state
        // then visitBootstrapMethodsAttribute is called

        // Act - In the typical workflow, visitBootstrapMethodsAttribute is called after
        // visitConstantInstruction has set the referencedBootstrapMethodIndex
        assertDoesNotThrow(() -> {
            converter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);
        });

        // Assert - verify the attribute method was called
        verify(bootstrapMethodsAttribute, times(1)).bootstrapMethodEntryAccept(
                eq(clazz),
                anyInt(),
                eq(converter)
        );
    }
}
