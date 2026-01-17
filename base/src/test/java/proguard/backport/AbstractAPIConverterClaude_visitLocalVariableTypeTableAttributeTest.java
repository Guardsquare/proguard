package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableTypeTableAttribute;
import proguard.classfile.attribute.visitor.LocalVariableTypeInfoVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)}.
 *
 * The visitLocalVariableTypeTableAttribute method delegates to the localVariablesAccept method
 * of the LocalVariableTypeTableAttribute, which processes each local variable type entry in the table
 * by calling back to the converter's visitLocalVariableTypeInfo method.
 */
public class AbstractAPIConverterClaude_visitLocalVariableTypeTableAttributeTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;
    private LocalVariableTypeTableAttribute localVariableTypeTableAttribute;

    /**
     * Creates a concrete test subclass of AbstractAPIConverter for testing purposes.
     * This is necessary because AbstractAPIConverter is abstract.
     */
    private static class TestAPIConverter extends AbstractAPIConverter {
        TestAPIConverter(ClassPool programClassPool,
                        ClassPool libraryClassPool,
                        WarningPrinter warningPrinter,
                        ClassVisitor modifiedClassVisitor,
                        InstructionVisitor extraInstructionVisitor) {
            super(programClassPool, libraryClassPool, warningPrinter,
                  modifiedClassVisitor, extraInstructionVisitor);

            // Initialize with empty replacements to avoid NullPointerExceptions
            setTypeReplacements(new TypeReplacement[0]);
            setMethodReplacements(new MethodReplacement[0]);
        }
    }

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        warningPrinter = mock(WarningPrinter.class);
        modifiedClassVisitor = mock(ClassVisitor.class);
        extraInstructionVisitor = mock(InstructionVisitor.class);

        converter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        clazz = mock(ProgramClass.class);
        method = mock(Method.class);
        codeAttribute = mock(CodeAttribute.class);
        localVariableTypeTableAttribute = mock(LocalVariableTypeTableAttribute.class);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute correctly delegates to localVariablesAccept.
     * This verifies the core functionality of the method - delegation to process local variable types.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_delegatesToLocalVariablesAccept() {
        // Act
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify that localVariablesAccept was called with correct parameters
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz, method, codeAttribute, converter);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can be called with valid mocks without exceptions.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
            converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute)
        );
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute can be called multiple times.
     * Each call should independently delegate to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_calledMultipleTimes_delegatesEachTime() {
        // Act
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify localVariablesAccept was called exactly 3 times
        verify(localVariableTypeTableAttribute, times(3))
            .localVariablesAccept(clazz, method, codeAttribute, converter);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with different attribute instances.
     * Each attribute instance should have its localVariablesAccept method called.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentAttributes_delegatesToEach() {
        // Arrange
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr3 = mock(LocalVariableTypeTableAttribute.class);

        // Act
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr1);
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr2);
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr3);

        // Assert - verify each attribute's localVariablesAccept was called once
        verify(attr1).localVariablesAccept(clazz, method, codeAttribute, converter);
        verify(attr2).localVariablesAccept(clazz, method, codeAttribute, converter);
        verify(attr3).localVariablesAccept(clazz, method, codeAttribute, converter);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute passes the converter itself as the visitor.
     * This is crucial because the converter implements LocalVariableTypeInfoVisitor.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_passesConverterAsVisitor() {
        // Act
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify that the converter itself is passed as the visitor parameter
        verify(localVariableTypeTableAttribute).localVariablesAccept(
            eq(clazz),
            eq(method),
            eq(codeAttribute),
            same(converter)  // The converter itself should be passed as visitor
        );
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with different clazz instances.
     * Each clazz should be correctly passed through to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act
        converter.visitLocalVariableTypeTableAttribute(clazz1, method, codeAttribute, localVariableTypeTableAttribute);
        converter.visitLocalVariableTypeTableAttribute(clazz2, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify the correct clazz was passed in each call
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz1, method, codeAttribute, converter);
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz2, method, codeAttribute, converter);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with different method instances.
     * Each method should be correctly passed through to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentMethod_passesCorrectMethod() {
        // Arrange
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        // Act
        converter.visitLocalVariableTypeTableAttribute(clazz, method1, codeAttribute, localVariableTypeTableAttribute);
        converter.visitLocalVariableTypeTableAttribute(clazz, method2, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify the correct method was passed in each call
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz, method1, codeAttribute, converter);
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz, method2, codeAttribute, converter);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with different code attribute instances.
     * Each code attribute should be correctly passed through to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withDifferentCodeAttribute_passesCorrectCodeAttribute() {
        // Arrange
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);

        // Act
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttr1, localVariableTypeTableAttribute);
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttr2, localVariableTypeTableAttribute);

        // Assert - verify the correct code attribute was passed in each call
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz, method, codeAttr1, converter);
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz, method, codeAttr2, converter);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute doesn't interact with parameters beyond delegation.
     * The method should only delegate and not directly interact with clazz, method, or codeAttribute.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_doesNotDirectlyInteractWithParameters() {
        // Act
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify no direct interactions with parameters (they're only passed through)
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        // localVariableTypeTableAttribute should have been called via delegation
        verify(localVariableTypeTableAttribute, times(1))
            .localVariablesAccept(any(), any(), any(), any(LocalVariableTypeInfoVisitor.class));
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute doesn't trigger warnings.
     * This method should operate without generating any warnings.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_doesNotTriggerWarnings() {
        // Act
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute doesn't trigger the modified class visitor.
     * This method just visits local variable types and shouldn't mark the class as modified directly.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_doesNotTriggerModifiedClassVisitor() {
        // Act
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify the modified class visitor was not invoked
        verifyNoInteractions(modifiedClassVisitor);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute doesn't trigger the extra instruction visitor.
     * This method handles local variable type attributes, not instructions.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_doesNotTriggerExtraInstructionVisitor() {
        // Act
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify the extra instruction visitor was not invoked
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with a converter with null warning printer.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withNullWarningPrinter_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act
        converterWithNullPrinter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify delegation still occurred
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz, method, codeAttribute, converterWithNullPrinter);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with a converter with null class visitor.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withNullClassVisitor_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        // Act
        converterWithNullVisitor.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify delegation still occurred
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz, method, codeAttribute, converterWithNullVisitor);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute works with a converter with null instruction visitor.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_withNullInstructionVisitor_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        // Act
        converterWithNullInstrVisitor.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify delegation still occurred
        verify(localVariableTypeTableAttribute).localVariablesAccept(clazz, method, codeAttribute, converterWithNullInstrVisitor);
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute maintains correct order when called with multiple attributes.
     * This ensures that sequential calls maintain independence and proper delegation.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_sequentialCalls_maintainIndependence() {
        // Arrange
        LocalVariableTypeTableAttribute attr1 = mock(LocalVariableTypeTableAttribute.class);
        LocalVariableTypeTableAttribute attr2 = mock(LocalVariableTypeTableAttribute.class);

        // Act - call with first attribute
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr1);
        verify(attr1).localVariablesAccept(clazz, method, codeAttribute, converter);

        // Act - call with second attribute
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, attr2);
        verify(attr2).localVariablesAccept(clazz, method, codeAttribute, converter);

        // Assert - first attribute should not have been called again
        verify(attr1, times(1)).localVariablesAccept(any(), any(), any(), any(LocalVariableTypeInfoVisitor.class));
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute integrates correctly with the visitor pattern.
     * The converter implements LocalVariableTypeInfoVisitor, so it should be a valid visitor.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_converterIsValidVisitor() {
        // Arrange & Assert - verify the converter is an instance of LocalVariableTypeInfoVisitor
        assertTrue(converter instanceof LocalVariableTypeInfoVisitor,
            "Converter should implement LocalVariableTypeInfoVisitor to be used as a visitor");

        // Act
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify it's passed as a LocalVariableTypeInfoVisitor
        verify(localVariableTypeTableAttribute).localVariablesAccept(
            any(Clazz.class),
            any(Method.class),
            any(CodeAttribute.class),
            any(LocalVariableTypeInfoVisitor.class)
        );
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute handles repeated calls with the same parameters idempotently.
     * While each call should delegate, the delegation should be consistent.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_repeatedCallsWithSameParameters_delegatesConsistently() {
        // Act
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);
        converter.visitLocalVariableTypeTableAttribute(clazz, method, codeAttribute, localVariableTypeTableAttribute);

        // Assert - verify delegation happened twice with identical parameters
        verify(localVariableTypeTableAttribute, times(2))
            .localVariablesAccept(same(clazz), same(method), same(codeAttribute), same(converter));
    }

    /**
     * Tests that visitLocalVariableTypeTableAttribute properly integrates with all four parameters.
     * This is a comprehensive test verifying the complete parameter flow.
     */
    @Test
    public void testVisitLocalVariableTypeTableAttribute_integratesAllParameters() {
        // Arrange - create specific mocks
        Clazz specificClazz = mock(ProgramClass.class, "specificClazz");
        Method specificMethod = mock(Method.class, "specificMethod");
        CodeAttribute specificCodeAttr = mock(CodeAttribute.class, "specificCodeAttr");
        LocalVariableTypeTableAttribute specificAttr = mock(LocalVariableTypeTableAttribute.class, "specificAttr");

        // Act
        converter.visitLocalVariableTypeTableAttribute(specificClazz, specificMethod, specificCodeAttr, specificAttr);

        // Assert - verify all specific parameters were passed correctly
        verify(specificAttr).localVariablesAccept(specificClazz, specificMethod, specificCodeAttr, converter);
    }
}
