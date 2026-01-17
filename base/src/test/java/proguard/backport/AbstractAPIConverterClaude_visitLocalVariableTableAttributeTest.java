package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableTableAttribute;
import proguard.classfile.attribute.visitor.LocalVariableInfoVisitor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)}.
 *
 * The visitLocalVariableTableAttribute method delegates to the localVariablesAccept method
 * of the LocalVariableTableAttribute, which processes each local variable in the table
 * by calling back to the converter's visitLocalVariableInfo method.
 */
public class AbstractAPIConverterClaude_visitLocalVariableTableAttributeTest {

    private TestAPIConverter converter;
    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;
    private ClassVisitor modifiedClassVisitor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;
    private LocalVariableTableAttribute localVariableTableAttribute;

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
        localVariableTableAttribute = mock(LocalVariableTableAttribute.class);
    }

    /**
     * Tests that visitLocalVariableTableAttribute correctly delegates to localVariablesAccept.
     * This verifies the core functionality of the method - delegation to process local variables.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_delegatesToLocalVariablesAccept() {
        // Act
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify that localVariablesAccept was called with correct parameters
        verify(localVariableTableAttribute).localVariablesAccept(clazz, method, codeAttribute, converter);
    }

    /**
     * Tests that visitLocalVariableTableAttribute can be called with valid mocks without exceptions.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withValidMocks_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() ->
            converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute)
        );
    }

    /**
     * Tests that visitLocalVariableTableAttribute can be called multiple times.
     * Each call should independently delegate to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_calledMultipleTimes_delegatesEachTime() {
        // Act
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify localVariablesAccept was called exactly 3 times
        verify(localVariableTableAttribute, times(3))
            .localVariablesAccept(clazz, method, codeAttribute, converter);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with different attribute instances.
     * Each attribute instance should have its localVariablesAccept method called.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentAttributes_delegatesToEach() {
        // Arrange
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr3 = mock(LocalVariableTableAttribute.class);

        // Act
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr1);
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr2);
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr3);

        // Assert - verify each attribute's localVariablesAccept was called once
        verify(attr1).localVariablesAccept(clazz, method, codeAttribute, converter);
        verify(attr2).localVariablesAccept(clazz, method, codeAttribute, converter);
        verify(attr3).localVariablesAccept(clazz, method, codeAttribute, converter);
    }

    /**
     * Tests that visitLocalVariableTableAttribute passes the converter itself as the visitor.
     * This is crucial because the converter implements LocalVariableInfoVisitor.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_passesConverterAsVisitor() {
        // Act
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify that the converter itself is passed as the visitor parameter
        verify(localVariableTableAttribute).localVariablesAccept(
            eq(clazz),
            eq(method),
            eq(codeAttribute),
            same(converter)  // The converter itself should be passed as visitor
        );
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with different clazz instances.
     * Each clazz should be correctly passed through to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentClazz_passesCorrectClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);

        // Act
        converter.visitLocalVariableTableAttribute(clazz1, method, codeAttribute, localVariableTableAttribute);
        converter.visitLocalVariableTableAttribute(clazz2, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify the correct clazz was passed in each call
        verify(localVariableTableAttribute).localVariablesAccept(clazz1, method, codeAttribute, converter);
        verify(localVariableTableAttribute).localVariablesAccept(clazz2, method, codeAttribute, converter);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with different method instances.
     * Each method should be correctly passed through to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentMethod_passesCorrectMethod() {
        // Arrange
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        // Act
        converter.visitLocalVariableTableAttribute(clazz, method1, codeAttribute, localVariableTableAttribute);
        converter.visitLocalVariableTableAttribute(clazz, method2, codeAttribute, localVariableTableAttribute);

        // Assert - verify the correct method was passed in each call
        verify(localVariableTableAttribute).localVariablesAccept(clazz, method1, codeAttribute, converter);
        verify(localVariableTableAttribute).localVariablesAccept(clazz, method2, codeAttribute, converter);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with different code attribute instances.
     * Each code attribute should be correctly passed through to localVariablesAccept.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withDifferentCodeAttribute_passesCorrectCodeAttribute() {
        // Arrange
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);

        // Act
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttr1, localVariableTableAttribute);
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttr2, localVariableTableAttribute);

        // Assert - verify the correct code attribute was passed in each call
        verify(localVariableTableAttribute).localVariablesAccept(clazz, method, codeAttr1, converter);
        verify(localVariableTableAttribute).localVariablesAccept(clazz, method, codeAttr2, converter);
    }

    /**
     * Tests that visitLocalVariableTableAttribute doesn't interact with parameters beyond delegation.
     * The method should only delegate and not directly interact with clazz, method, or codeAttribute.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_doesNotDirectlyInteractWithParameters() {
        // Act
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify no direct interactions with parameters (they're only passed through)
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
        // localVariableTableAttribute should have been called via delegation
        verify(localVariableTableAttribute, times(1))
            .localVariablesAccept(any(), any(), any(), any(LocalVariableInfoVisitor.class));
    }

    /**
     * Tests that visitLocalVariableTableAttribute doesn't trigger warnings.
     * This method should operate without generating any warnings.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_doesNotTriggerWarnings() {
        // Act
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify no warnings were printed
        verifyNoInteractions(warningPrinter);
    }

    /**
     * Tests that visitLocalVariableTableAttribute doesn't trigger the modified class visitor.
     * This method just visits local variables and shouldn't mark the class as modified directly.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_doesNotTriggerModifiedClassVisitor() {
        // Act
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify the modified class visitor was not invoked
        verifyNoInteractions(modifiedClassVisitor);
    }

    /**
     * Tests that visitLocalVariableTableAttribute doesn't trigger the extra instruction visitor.
     * This method handles local variable attributes, not instructions.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_doesNotTriggerExtraInstructionVisitor() {
        // Act
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify the extra instruction visitor was not invoked
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with a converter with null warning printer.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withNullWarningPrinter_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullPrinter = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            null, // null warning printer
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Act
        converterWithNullPrinter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify delegation still occurred
        verify(localVariableTableAttribute).localVariablesAccept(clazz, method, codeAttribute, converterWithNullPrinter);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with a converter with null class visitor.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withNullClassVisitor_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null, // null class visitor
            extraInstructionVisitor
        );

        // Act
        converterWithNullVisitor.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify delegation still occurred
        verify(localVariableTableAttribute).localVariablesAccept(clazz, method, codeAttribute, converterWithNullVisitor);
    }

    /**
     * Tests that visitLocalVariableTableAttribute works with a converter with null instruction visitor.
     * The method should still delegate correctly even with null optional dependencies.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_withNullInstructionVisitor_delegatesCorrectly() {
        // Arrange
        TestAPIConverter converterWithNullInstrVisitor = new TestAPIConverter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null // null instruction visitor
        );

        // Act
        converterWithNullInstrVisitor.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify delegation still occurred
        verify(localVariableTableAttribute).localVariablesAccept(clazz, method, codeAttribute, converterWithNullInstrVisitor);
    }

    /**
     * Tests that visitLocalVariableTableAttribute maintains correct order when called with multiple attributes.
     * This ensures that sequential calls maintain independence and proper delegation.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_sequentialCalls_maintainIndependence() {
        // Arrange
        LocalVariableTableAttribute attr1 = mock(LocalVariableTableAttribute.class);
        LocalVariableTableAttribute attr2 = mock(LocalVariableTableAttribute.class);

        // Act - call with first attribute
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr1);
        verify(attr1).localVariablesAccept(clazz, method, codeAttribute, converter);

        // Act - call with second attribute
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, attr2);
        verify(attr2).localVariablesAccept(clazz, method, codeAttribute, converter);

        // Assert - first attribute should not have been called again
        verify(attr1, times(1)).localVariablesAccept(any(), any(), any(), any(LocalVariableInfoVisitor.class));
    }

    /**
     * Tests that visitLocalVariableTableAttribute integrates correctly with the visitor pattern.
     * The converter implements LocalVariableInfoVisitor, so it should be a valid visitor.
     */
    @Test
    public void testVisitLocalVariableTableAttribute_converterIsValidVisitor() {
        // Arrange & Assert - verify the converter is an instance of LocalVariableInfoVisitor
        assertTrue(converter instanceof LocalVariableInfoVisitor,
            "Converter should implement LocalVariableInfoVisitor to be used as a visitor");

        // Act
        converter.visitLocalVariableTableAttribute(clazz, method, codeAttribute, localVariableTableAttribute);

        // Assert - verify it's passed as a LocalVariableInfoVisitor
        verify(localVariableTableAttribute).localVariablesAccept(
            any(Clazz.class),
            any(Method.class),
            any(CodeAttribute.class),
            any(LocalVariableInfoVisitor.class)
        );
    }
}
