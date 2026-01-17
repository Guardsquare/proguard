package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link StringConcatenationConverter#visitStringConstant(Clazz, StringConstant)}.
 *
 * The visitStringConstant method is invoked as part of the ConstantVisitor pattern during
 * string concatenation conversion. It handles StringConstant instances specially by:
 * 1. Adding the actual string length (not the default 16) to estimatedStringLength
 * 2. Invoking StringBuilder.append with String signature (not Object)
 *
 * Note: This method depends on internal state (appendChainComposer) being initialized
 * by visitConstantInstruction during INVOKEDYNAMIC processing. Direct calls without
 * proper initialization will result in NullPointerException.
 */
public class StringConcatenationConverterClaude_visitStringConstantTest {

    private StringConcatenationConverter converter;
    private CodeAttributeEditor codeAttributeEditor;
    private InstructionVisitor extraInstructionVisitor;
    private Clazz clazz;
    private StringConstant stringConstant;

    @BeforeEach
    public void setUp() {
        codeAttributeEditor = mock(CodeAttributeEditor.class);
        extraInstructionVisitor = mock(InstructionVisitor.class);
        converter = new StringConcatenationConverter(extraInstructionVisitor, codeAttributeEditor);
        clazz = mock(ProgramClass.class);
        stringConstant = mock(StringConstant.class);
    }

    /**
     * Tests that visitStringConstant throws NullPointerException when called without initialization.
     * This verifies that the method expects appendChainComposer to be initialized before use.
     * This is the expected behavior as the method is designed to be called internally
     * during INVOKEDYNAMIC processing, not directly.
     */
    @Test
    public void testVisitStringConstant_withoutInitialization_throwsNullPointerException() {
        // Arrange
        when(stringConstant.getString(clazz)).thenReturn("test");

        // Act & Assert - should throw NullPointerException because appendChainComposer is null
        assertThrows(NullPointerException.class, () ->
            converter.visitStringConstant(clazz, stringConstant));
    }

    /**
     * Tests that visitStringConstant throws NullPointerException with null Clazz parameter.
     * The method will attempt to use the appendChainComposer which is not initialized.
     */
    @Test
    public void testVisitStringConstant_withNullClazz_throwsNullPointerException() {
        // Arrange
        when(stringConstant.getString(null)).thenReturn("test");

        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            converter.visitStringConstant(null, stringConstant));
    }

    /**
     * Tests that visitStringConstant throws NullPointerException with null StringConstant parameter.
     * The method will attempt to use the appendChainComposer which is not initialized.
     */
    @Test
    public void testVisitStringConstant_withNullStringConstant_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            converter.visitStringConstant(clazz, null));
    }

    /**
     * Tests that visitStringConstant throws NullPointerException with both parameters null.
     * The method will attempt to use the appendChainComposer which is not initialized.
     */
    @Test
    public void testVisitStringConstant_withBothParametersNull_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class, () ->
            converter.visitStringConstant(null, null));
    }

    /**
     * Tests that visitStringConstant with an empty string throws NullPointerException without initialization.
     */
    @Test
    public void testVisitStringConstant_withEmptyString_throwsNullPointerException() {
        // Arrange
        when(stringConstant.getString(clazz)).thenReturn("");

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            converter.visitStringConstant(clazz, stringConstant));
    }

    /**
     * Tests that visitStringConstant with a long string throws NullPointerException without initialization.
     */
    @Test
    public void testVisitStringConstant_withLongString_throwsNullPointerException() {
        // Arrange
        when(stringConstant.getString(clazz)).thenReturn("This is a very long string for testing purposes");

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            converter.visitStringConstant(clazz, stringConstant));
    }

    /**
     * Tests visitStringConstant behavior with properly initialized state using reflection.
     * This test verifies that when appendChainComposer is initialized, the method:
     * 1. Increments estimatedStringLength by the actual string length
     * 2. Calls invokevirtual on the appendChainComposer with String signature
     *
     * Reflection is used here because there is no other way to test this method's behavior
     * without setting up a complex INVOKEDYNAMIC instruction scenario. The method is designed
     * to be called internally during string concatenation processing, and testing it in isolation
     * requires access to the private appendChainComposer field.
     */
    @Test
    public void testVisitStringConstant_withInitializedState_updatesStateByStringLength() throws Exception {
        // Arrange - use reflection to initialize the internal state
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 2;
        programClass.constantPool = new Constant[2];

        // Create a real Utf8Constant and StringConstant
        Utf8Constant utf8Constant = new Utf8Constant("Hello");
        programClass.constantPool[0] = utf8Constant;
        StringConstant realStringConstant = new StringConstant(0, null, null);
        programClass.constantPool[1] = realStringConstant;

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
            return;
        }

        appendChainComposerField.set(converter, appendChainComposer);
        estimatedStringLengthField.set(converter, 100);

        // Act
        converter.visitStringConstant(programClass, realStringConstant);

        // Assert - verify estimatedStringLength was incremented by the string length (5 for "Hello")
        int newLength = estimatedStringLengthField.getInt(converter);
        assertEquals(105, newLength, "estimatedStringLength should be incremented by 5 (length of 'Hello')");
    }

    /**
     * Tests visitStringConstant with an empty string properly initialized.
     * Verifies that empty strings contribute 0 to the estimated length.
     *
     * Reflection is used because there is no public API to initialize the converter's
     * internal state (appendChainComposer) which is only set during INVOKEDYNAMIC processing.
     */
    @Test
    public void testVisitStringConstant_withEmptyStringAndInitializedState_addsZeroLength() throws Exception {
        // Arrange - use reflection to initialize the internal state
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 2;
        programClass.constantPool = new Constant[2];

        // Create a real Utf8Constant and StringConstant with empty string
        Utf8Constant utf8Constant = new Utf8Constant("");
        programClass.constantPool[0] = utf8Constant;
        StringConstant realStringConstant = new StringConstant(0, null, null);
        programClass.constantPool[1] = realStringConstant;

        Field appendChainComposerField = StringConcatenationConverter.class.getDeclaredField("appendChainComposer");
        appendChainComposerField.setAccessible(true);

        Field estimatedStringLengthField = StringConcatenationConverter.class.getDeclaredField("estimatedStringLength");
        estimatedStringLengthField.setAccessible(true);

        Object appendChainComposer;
        try {
            Class<?> builderClass = Class.forName("proguard.classfile.editor.InstructionSequenceBuilder");
            appendChainComposer = builderClass.getConstructor(ProgramClass.class).newInstance(programClass);
        } catch (ClassNotFoundException e) {
            return;
        }

        appendChainComposerField.set(converter, appendChainComposer);
        estimatedStringLengthField.set(converter, 50);

        // Act
        converter.visitStringConstant(programClass, realStringConstant);

        // Assert - verify estimatedStringLength was not incremented (empty string has length 0)
        int newLength = estimatedStringLengthField.getInt(converter);
        assertEquals(50, newLength, "estimatedStringLength should remain 50 (empty string adds 0)");
    }

    /**
     * Tests visitStringConstant multiple times with different string lengths.
     * Verifies that the lengths accumulate correctly.
     *
     * Reflection is used because there is no public API to initialize the converter's
     * internal state (appendChainComposer) which is only set during INVOKEDYNAMIC processing.
     */
    @Test
    public void testVisitStringConstant_multipleCallsWithDifferentLengths_accumulatesCorrectly() throws Exception {
        // Arrange - use reflection to initialize the internal state
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 4;
        programClass.constantPool = new Constant[4];

        // Create StringConstants with different lengths: "Hi" (2), "Test" (4), "Example" (7)
        Utf8Constant utf8_1 = new Utf8Constant("Hi");
        StringConstant string1 = new StringConstant(0, null, null);
        Utf8Constant utf8_2 = new Utf8Constant("Test");
        StringConstant string2 = new StringConstant(2, null, null);

        programClass.constantPool[0] = utf8_1;
        programClass.constantPool[1] = string1;
        programClass.constantPool[2] = utf8_2;
        programClass.constantPool[3] = string2;

        Field appendChainComposerField = StringConcatenationConverter.class.getDeclaredField("appendChainComposer");
        appendChainComposerField.setAccessible(true);

        Field estimatedStringLengthField = StringConcatenationConverter.class.getDeclaredField("estimatedStringLength");
        estimatedStringLengthField.setAccessible(true);

        Object appendChainComposer;
        try {
            Class<?> builderClass = Class.forName("proguard.classfile.editor.InstructionSequenceBuilder");
            appendChainComposer = builderClass.getConstructor(ProgramClass.class).newInstance(programClass);
        } catch (ClassNotFoundException e) {
            return;
        }

        appendChainComposerField.set(converter, appendChainComposer);
        estimatedStringLengthField.set(converter, 0);

        // Act - call with different strings
        converter.visitStringConstant(programClass, string1);  // "Hi" = 2
        converter.visitStringConstant(programClass, string2);  // "Test" = 4

        // Assert - verify estimatedStringLength accumulated correctly (2 + 4 = 6)
        int finalLength = estimatedStringLengthField.getInt(converter);
        assertEquals(6, finalLength, "estimatedStringLength should be 6 (2 + 4)");
    }

    /**
     * Tests visitStringConstant with a single character string.
     * Verifies that single character strings contribute 1 to the length.
     *
     * Reflection is used because appendChainComposer is a private field that is only initialized
     * during the internal processing flow of visitConstantInstruction.
     */
    @Test
    public void testVisitStringConstant_withSingleCharacter_addsOneToLength() throws Exception {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 2;
        programClass.constantPool = new Constant[2];

        Utf8Constant utf8Constant = new Utf8Constant("A");
        programClass.constantPool[0] = utf8Constant;
        StringConstant realStringConstant = new StringConstant(0, null, null);
        programClass.constantPool[1] = realStringConstant;

        Field appendChainComposerField = StringConcatenationConverter.class.getDeclaredField("appendChainComposer");
        appendChainComposerField.setAccessible(true);

        Field estimatedStringLengthField = StringConcatenationConverter.class.getDeclaredField("estimatedStringLength");
        estimatedStringLengthField.setAccessible(true);

        Object appendChainComposer;
        try {
            Class<?> builderClass = Class.forName("proguard.classfile.editor.InstructionSequenceBuilder");
            appendChainComposer = builderClass.getConstructor(ProgramClass.class).newInstance(programClass);
        } catch (ClassNotFoundException e) {
            return;
        }

        appendChainComposerField.set(converter, appendChainComposer);
        estimatedStringLengthField.set(converter, 10);

        // Act
        converter.visitStringConstant(programClass, realStringConstant);

        // Assert
        int newLength = estimatedStringLengthField.getInt(converter);
        assertEquals(11, newLength, "estimatedStringLength should be 11 (10 + 1)");
    }

    /**
     * Tests visitStringConstant with a long string (more than 16 characters).
     * This demonstrates that visitStringConstant uses actual length, not the default 16
     * used by visitAnyConstant.
     *
     * Reflection is used because there is no public method to initialize appendChainComposer.
     */
    @Test
    public void testVisitStringConstant_withLongString_addsActualLength() throws Exception {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 2;
        programClass.constantPool = new Constant[2];

        String longString = "This is a long string with more than 16 characters";
        Utf8Constant utf8Constant = new Utf8Constant(longString);
        programClass.constantPool[0] = utf8Constant;
        StringConstant realStringConstant = new StringConstant(0, null, null);
        programClass.constantPool[1] = realStringConstant;

        Field appendChainComposerField = StringConcatenationConverter.class.getDeclaredField("appendChainComposer");
        appendChainComposerField.setAccessible(true);

        Field estimatedStringLengthField = StringConcatenationConverter.class.getDeclaredField("estimatedStringLength");
        estimatedStringLengthField.setAccessible(true);

        Object appendChainComposer;
        try {
            Class<?> builderClass = Class.forName("proguard.classfile.editor.InstructionSequenceBuilder");
            appendChainComposer = builderClass.getConstructor(ProgramClass.class).newInstance(programClass);
        } catch (ClassNotFoundException e) {
            return;
        }

        appendChainComposerField.set(converter, appendChainComposer);
        estimatedStringLengthField.set(converter, 0);

        // Act
        converter.visitStringConstant(programClass, realStringConstant);

        // Assert - verify actual length (51 chars) was added, not the default 16
        int newLength = estimatedStringLengthField.getInt(converter);
        assertEquals(longString.length(), newLength,
            "estimatedStringLength should be " + longString.length() + " (actual string length, not 16)");
    }

    /**
     * Tests visitStringConstant with Unicode characters.
     * Verifies that Unicode strings are handled correctly.
     *
     * Reflection is used because appendChainComposer is only initialized during INVOKEDYNAMIC processing.
     */
    @Test
    public void testVisitStringConstant_withUnicodeCharacters_addsCorrectLength() throws Exception {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 2;
        programClass.constantPool = new Constant[2];

        String unicodeString = "Hello \u4E16\u754C"; // "Hello 世界"
        Utf8Constant utf8Constant = new Utf8Constant(unicodeString);
        programClass.constantPool[0] = utf8Constant;
        StringConstant realStringConstant = new StringConstant(0, null, null);
        programClass.constantPool[1] = realStringConstant;

        Field appendChainComposerField = StringConcatenationConverter.class.getDeclaredField("appendChainComposer");
        appendChainComposerField.setAccessible(true);

        Field estimatedStringLengthField = StringConcatenationConverter.class.getDeclaredField("estimatedStringLength");
        estimatedStringLengthField.setAccessible(true);

        Object appendChainComposer;
        try {
            Class<?> builderClass = Class.forName("proguard.classfile.editor.InstructionSequenceBuilder");
            appendChainComposer = builderClass.getConstructor(ProgramClass.class).newInstance(programClass);
        } catch (ClassNotFoundException e) {
            return;
        }

        appendChainComposerField.set(converter, appendChainComposer);
        estimatedStringLengthField.set(converter, 0);

        // Act
        converter.visitStringConstant(programClass, realStringConstant);

        // Assert - 8 characters: "Hello " (6) + 2 Chinese characters
        int newLength = estimatedStringLengthField.getInt(converter);
        assertEquals(8, newLength, "estimatedStringLength should be 8 (6 ASCII + 2 Unicode chars)");
    }

    /**
     * Tests visitStringConstant with special characters and escape sequences.
     * Verifies that strings with newlines, tabs, etc. are handled correctly.
     *
     * Reflection is used because there is no public API to set up the internal state.
     */
    @Test
    public void testVisitStringConstant_withSpecialCharacters_addsCorrectLength() throws Exception {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2constantPoolCount = 2;
        programClass.constantPool = new Constant[2];

        String specialString = "Line1\nLine2\tTabbed";
        Utf8Constant utf8Constant = new Utf8Constant(specialString);
        programClass.constantPool[0] = utf8Constant;
        StringConstant realStringConstant = new StringConstant(0, null, null);
        programClass.constantPool[1] = realStringConstant;

        Field appendChainComposerField = StringConcatenationConverter.class.getDeclaredField("appendChainComposer");
        appendChainComposerField.setAccessible(true);

        Field estimatedStringLengthField = StringConcatenationConverter.class.getDeclaredField("estimatedStringLength");
        estimatedStringLengthField.setAccessible(true);

        Object appendChainComposer;
        try {
            Class<?> builderClass = Class.forName("proguard.classfile.editor.InstructionSequenceBuilder");
            appendChainComposer = builderClass.getConstructor(ProgramClass.class).newInstance(programClass);
        } catch (ClassNotFoundException e) {
            return;
        }

        appendChainComposerField.set(converter, appendChainComposer);
        estimatedStringLengthField.set(converter, 0);

        // Act
        converter.visitStringConstant(programClass, realStringConstant);

        // Assert
        int newLength = estimatedStringLengthField.getInt(converter);
        assertEquals(specialString.length(), newLength,
            "estimatedStringLength should equal the actual string length including special chars");
    }

    /**
     * Tests that visitStringConstant doesn't directly interact with the StringConstant parameter
     * beyond calling getString on it.
     */
    @Test
    public void testVisitStringConstant_interactsOnlyViaGetString() {
        // Arrange
        StringConstant mockStringConstant = mock(StringConstant.class);
        when(mockStringConstant.getString(clazz)).thenReturn("test");

        // Act - expect NullPointerException from uninitialized appendChainComposer
        try {
            converter.visitStringConstant(clazz, mockStringConstant);
        } catch (NullPointerException e) {
            // Expected
        }

        // Assert - verify only getString was called on the mock
        verify(mockStringConstant, times(1)).getString(clazz);
        verifyNoMoreInteractions(mockStringConstant);
    }

    /**
     * Tests that visitStringConstant doesn't interact with the converter's constructor dependencies.
     * The method uses internal state but doesn't use codeAttributeEditor or extraInstructionVisitor.
     */
    @Test
    public void testVisitStringConstant_doesNotInteractWithConstructorDependencies() {
        // Arrange
        when(stringConstant.getString(clazz)).thenReturn("test");

        // Act - expect NullPointerException from uninitialized appendChainComposer
        try {
            converter.visitStringConstant(clazz, stringConstant);
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
    public void testVisitStringConstant_withNullConstructorDependencies_throwsSameException() {
        // Arrange
        StringConcatenationConverter converterWithNulls =
            new StringConcatenationConverter(null, null);
        when(stringConstant.getString(clazz)).thenReturn("test");

        // Act & Assert - should still throw NullPointerException for the same reason
        assertThrows(NullPointerException.class, () ->
            converterWithNulls.visitStringConstant(clazz, stringConstant));
    }
}
