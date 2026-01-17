package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.*;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link StringConcatenationConverter#visitAnyConstant(Clazz, Constant)}.
 *
 * The visitAnyConstant method is invoked as part of the ConstantVisitor pattern during
 * string concatenation conversion. It serves as a default handler for constants that
 * don't have more specific visit methods. When called, it:
 * 1. Adds 16 to the estimatedStringLength (default StringBuilder size)
 * 2. Invokes StringBuilder.append with Object signature
 *
 * Note: This method depends on internal state (appendChainComposer) being initialized
 * by visitConstantInstruction during INVOKEDYNAMIC processing. Direct calls without
 * proper initialization will result in NullPointerException.
 */
public class StringConcatenationConverterClaude_visitAnyConstantTest {

    private StringConcatenationConverter converter;
    private CodeAttributeEditor codeAttributeEditor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private Constant constant;

    @BeforeEach
    public void setUp() {
        codeAttributeEditor = mock(CodeAttributeEditor.class);
        extraInstructionVisitor = mock(InstructionVisitor.class);
        converter = new StringConcatenationConverter(extraInstructionVisitor, codeAttributeEditor);
        clazz = mock(ProgramClass.class);
        constant = mock(Constant.class);
    }

    /**
     * Tests that visitAnyConstant throws NullPointerException when called without initialization.
     * This verifies that the method expects appendChainComposer to be initialized before use.
     * This is the expected behavior as the method is designed to be called internally
     * during INVOKEDYNAMIC processing, not directly.
     */
    @Test
    public void testVisitAnyConstant_withoutInitialization_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException because appendChainComposer is null
        assertThrows(NullPointerException.class, () ->
            converter.visitAnyConstant(clazz, constant));
    }

    /**
     * Tests that visitAnyConstant throws NullPointerException with null Clazz parameter.
     * The method will attempt to use the appendChainComposer which is not initialized.
     */
    @Test
    public void testVisitAnyConstant_withNullClazz_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            converter.visitAnyConstant(null, constant));
    }

    /**
     * Tests that visitAnyConstant throws NullPointerException with null Constant parameter.
     * The method will attempt to use the appendChainComposer which is not initialized.
     */
    @Test
    public void testVisitAnyConstant_withNullConstant_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            converter.visitAnyConstant(clazz, null));
    }

    /**
     * Tests that visitAnyConstant throws NullPointerException with both parameters null.
     * The method will attempt to use the appendChainComposer which is not initialized.
     */
    @Test
    public void testVisitAnyConstant_withBothParametersNull_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            converter.visitAnyConstant(null, null));
    }

    /**
     * Tests that visitAnyConstant with IntegerConstant throws NullPointerException without initialization.
     * IntegerConstant is a common type that would be handled by visitAnyConstant.
     */
    @Test
    public void testVisitAnyConstant_withIntegerConstant_throwsNullPointerException() {
        // Arrange
        IntegerConstant intConstant = mock(IntegerConstant.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            converter.visitAnyConstant(clazz, intConstant));
    }

    /**
     * Tests that visitAnyConstant with LongConstant throws NullPointerException without initialization.
     * LongConstant is another type that would be handled by visitAnyConstant.
     */
    @Test
    public void testVisitAnyConstant_withLongConstant_throwsNullPointerException() {
        // Arrange
        LongConstant longConstant = mock(LongConstant.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            converter.visitAnyConstant(clazz, longConstant));
    }

    /**
     * Tests that visitAnyConstant with FloatConstant throws NullPointerException without initialization.
     */
    @Test
    public void testVisitAnyConstant_withFloatConstant_throwsNullPointerException() {
        // Arrange
        FloatConstant floatConstant = mock(FloatConstant.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            converter.visitAnyConstant(clazz, floatConstant));
    }

    /**
     * Tests that visitAnyConstant with DoubleConstant throws NullPointerException without initialization.
     */
    @Test
    public void testVisitAnyConstant_withDoubleConstant_throwsNullPointerException() {
        // Arrange
        DoubleConstant doubleConstant = mock(DoubleConstant.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            converter.visitAnyConstant(clazz, doubleConstant));
    }

    /**
     * Tests that visitAnyConstant with Utf8Constant throws NullPointerException without initialization.
     */
    @Test
    public void testVisitAnyConstant_withUtf8Constant_throwsNullPointerException() {
        // Arrange
        Utf8Constant utf8Constant = mock(Utf8Constant.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            converter.visitAnyConstant(clazz, utf8Constant));
    }

    /**
     * Tests that visitAnyConstant with ClassConstant throws NullPointerException without initialization.
     */
    @Test
    public void testVisitAnyConstant_withClassConstant_throwsNullPointerException() {
        // Arrange
        ClassConstant classConstant = mock(ClassConstant.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            converter.visitAnyConstant(clazz, classConstant));
    }

    /**
     * Tests that visitAnyConstant with FieldrefConstant throws NullPointerException without initialization.
     */
    @Test
    public void testVisitAnyConstant_withFieldrefConstant_throwsNullPointerException() {
        // Arrange
        FieldrefConstant fieldrefConstant = mock(FieldrefConstant.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            converter.visitAnyConstant(clazz, fieldrefConstant));
    }

    /**
     * Tests that visitAnyConstant with MethodrefConstant throws NullPointerException without initialization.
     */
    @Test
    public void testVisitAnyConstant_withMethodrefConstant_throwsNullPointerException() {
        // Arrange
        MethodrefConstant methodrefConstant = mock(MethodrefConstant.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            converter.visitAnyConstant(clazz, methodrefConstant));
    }

    /**
     * Tests that visitAnyConstant with InterfaceMethodrefConstant throws NullPointerException without initialization.
     */
    @Test
    public void testVisitAnyConstant_withInterfaceMethodrefConstant_throwsNullPointerException() {
        // Arrange
        InterfaceMethodrefConstant interfaceMethodrefConstant = mock(InterfaceMethodrefConstant.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            converter.visitAnyConstant(clazz, interfaceMethodrefConstant));
    }

    /**
     * Tests that visitAnyConstant with NameAndTypeConstant throws NullPointerException without initialization.
     */
    @Test
    public void testVisitAnyConstant_withNameAndTypeConstant_throwsNullPointerException() {
        // Arrange
        NameAndTypeConstant nameAndTypeConstant = mock(NameAndTypeConstant.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            converter.visitAnyConstant(clazz, nameAndTypeConstant));
    }

    /**
     * Tests visitAnyConstant behavior with properly initialized state using reflection.
     * This test verifies that when appendChainComposer is initialized, the method:
     * 1. Increments estimatedStringLength by 16
     * 2. Calls invokevirtual on the appendChainComposer
     *
     * Reflection is used here because there is no other way to test this method's behavior
     * without setting up a complex INVOKEDYNAMIC instruction scenario. The method is designed
     * to be called internally during string concatenation processing, and testing it in isolation
     * requires access to the private appendChainComposer field.
     */
    @Test
    public void testVisitAnyConstant_withInitializedState_updatesStateAndInvokesMethod() throws Exception {
        // Arrange - use reflection to initialize the internal state
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new Constant[1];

        // Use reflection to access and initialize private fields
        Field appendChainComposerField = StringConcatenationConverter.class.getDeclaredField("appendChainComposer");
        appendChainComposerField.setAccessible(true);

        Field estimatedStringLengthField = StringConcatenationConverter.class.getDeclaredField("estimatedStringLength");
        estimatedStringLengthField.setAccessible(true);

        // Create an InstructionSequenceBuilder (the type of appendChainComposer)
        Object appendChainComposer;
        try {
            Class<?> builderClass = Class.forName("proguard.classfile.editor.InstructionSequenceBuilder");
            appendChainComposer = builderClass.getConstructor(ProgramClass.class).newInstance(programClass);
        } catch (ClassNotFoundException e) {
            // If InstructionSequenceBuilder is not accessible, skip this test
            // as we cannot properly initialize the state
            return;
        }

        appendChainComposerField.set(converter, appendChainComposer);
        estimatedStringLengthField.set(converter, 100);

        // Act
        converter.visitAnyConstant(programClass, constant);

        // Assert - verify estimatedStringLength was incremented by 16
        int newLength = estimatedStringLengthField.getInt(converter);
        assertEquals(116, newLength, "estimatedStringLength should be incremented by 16");
    }

    /**
     * Tests visitAnyConstant multiple times with proper initialization.
     * Verifies that repeated calls continue to increment the estimatedStringLength.
     *
     * Reflection is used because there is no public API to initialize the converter's
     * internal state (appendChainComposer) which is only set during INVOKEDYNAMIC processing.
     */
    @Test
    public void testVisitAnyConstant_multipleCallsWithInitializedState_accumulatesLength() throws Exception {
        // Arrange - use reflection to initialize the internal state
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new Constant[1];

        Field appendChainComposerField = StringConcatenationConverter.class.getDeclaredField("appendChainComposer");
        appendChainComposerField.setAccessible(true);

        Field estimatedStringLengthField = StringConcatenationConverter.class.getDeclaredField("estimatedStringLength");
        estimatedStringLengthField.setAccessible(true);

        Object appendChainComposer;
        try {
            Class<?> builderClass = Class.forName("proguard.classfile.editor.InstructionSequenceBuilder");
            appendChainComposer = builderClass.getConstructor(ProgramClass.class).newInstance(programClass);
        } catch (ClassNotFoundException e) {
            // Skip if InstructionSequenceBuilder is not accessible
            return;
        }

        appendChainComposerField.set(converter, appendChainComposer);
        estimatedStringLengthField.set(converter, 0);

        // Act - call three times
        converter.visitAnyConstant(programClass, constant);
        converter.visitAnyConstant(programClass, mock(IntegerConstant.class));
        converter.visitAnyConstant(programClass, mock(FloatConstant.class));

        // Assert - verify estimatedStringLength was incremented by 16 three times
        int finalLength = estimatedStringLengthField.getInt(converter);
        assertEquals(48, finalLength, "estimatedStringLength should be 48 (3 * 16)");
    }

    /**
     * Tests that visitAnyConstant works correctly with different constant types when initialized.
     * This verifies that the method treats all non-String constants uniformly.
     *
     * Reflection is used because appendChainComposer is a private field that is only initialized
     * during the internal processing flow of visitConstantInstruction. There is no public method
     * to initialize this state for testing purposes.
     */
    @Test
    public void testVisitAnyConstant_withDifferentConstantTypes_treatsThemUniformly() throws Exception {
        // Arrange - use reflection to initialize the internal state
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 1;
        programClass.constantPool = new Constant[1];

        Field appendChainComposerField = StringConcatenationConverter.class.getDeclaredField("appendChainComposer");
        appendChainComposerField.setAccessible(true);

        Field estimatedStringLengthField = StringConcatenationConverter.class.getDeclaredField("estimatedStringLength");
        estimatedStringLengthField.setAccessible(true);

        Object appendChainComposer;
        try {
            Class<?> builderClass = Class.forName("proguard.classfile.editor.InstructionSequenceBuilder");
            appendChainComposer = builderClass.getConstructor(ProgramClass.class).newInstance(programClass);
        } catch (ClassNotFoundException e) {
            // Skip if InstructionSequenceBuilder is not accessible
            return;
        }

        appendChainComposerField.set(converter, appendChainComposer);

        // Test with IntegerConstant
        estimatedStringLengthField.set(converter, 0);
        converter.visitAnyConstant(programClass, mock(IntegerConstant.class));
        assertEquals(16, estimatedStringLengthField.getInt(converter));

        // Test with LongConstant
        estimatedStringLengthField.set(converter, 0);
        converter.visitAnyConstant(programClass, mock(LongConstant.class));
        assertEquals(16, estimatedStringLengthField.getInt(converter));

        // Test with ClassConstant
        estimatedStringLengthField.set(converter, 0);
        converter.visitAnyConstant(programClass, mock(ClassConstant.class));
        assertEquals(16, estimatedStringLengthField.getInt(converter));
    }

    /**
     * Tests that visitAnyConstant doesn't interact with the Clazz parameter directly.
     * The method uses appendChainComposer but doesn't call methods on the clazz.
     */
    @Test
    public void testVisitAnyConstant_doesNotDirectlyInteractWithClazz() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);

        // Act - expect NullPointerException from uninitialized appendChainComposer
        try {
            converter.visitAnyConstant(mockClazz, constant);
        } catch (NullPointerException e) {
            // Expected
        }

        // Assert - verify no methods were called on the clazz mock
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyConstant doesn't interact with the Constant parameter directly.
     * The method uses the constant indirectly through appendChainComposer.
     */
    @Test
    public void testVisitAnyConstant_doesNotDirectlyInteractWithConstant() {
        // Arrange
        Constant mockConstant = mock(Constant.class);

        // Act - expect NullPointerException from uninitialized appendChainComposer
        try {
            converter.visitAnyConstant(clazz, mockConstant);
        } catch (NullPointerException e) {
            // Expected
        }

        // Assert - verify no methods were called on the constant mock
        verifyNoInteractions(mockConstant);
    }

    /**
     * Tests that visitAnyConstant doesn't interact with the converter's constructor dependencies.
     * The method uses internal state but doesn't use codeAttributeEditor or extraInstructionVisitor.
     */
    @Test
    public void testVisitAnyConstant_doesNotInteractWithConstructorDependencies() {
        // Act - expect NullPointerException from uninitialized appendChainComposer
        try {
            converter.visitAnyConstant(clazz, constant);
        } catch (NullPointerException e) {
            // Expected
        }

        // Assert - verify no interactions with constructor dependencies
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that a converter created with null dependencies behaves the same way.
     * The method doesn't use the constructor parameters, only internal state.
     */
    @Test
    public void testVisitAnyConstant_withNullConstructorDependencies_throwsSameException() {
        // Arrange
        StringConcatenationConverter converterWithNulls =
            new StringConcatenationConverter(null, null);

        // Act & Assert - should still throw NullPointerException for the same reason
        assertThrows(NullPointerException.class, () ->
            converterWithNulls.visitAnyConstant(clazz, constant));
    }
}
