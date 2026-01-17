package proguard.backport;

import org.junit.jupiter.api.Test;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link StringConcatenationConverter} constructor.
 * Tests the constructor with signature:
 * (Lproguard/classfile/instruction/visitor/InstructionVisitor;Lproguard/classfile/editor/CodeAttributeEditor;)V
 */
public class StringConcatenationConverterClaude_constructorTest {

    /**
     * Tests the constructor with valid non-null parameters.
     * Verifies that the converter can be instantiated with both parameters provided.
     */
    @Test
    public void testConstructorWithValidParameters() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        StringConcatenationConverter converter =
            new StringConcatenationConverter(extraInstructionVisitor, codeAttributeEditor);

        // Assert
        assertNotNull(converter, "Converter should be created successfully");
    }

    /**
     * Tests the constructor with null extraInstructionVisitor.
     * Verifies that the converter can be instantiated with null InstructionVisitor.
     */
    @Test
    public void testConstructorWithNullExtraInstructionVisitor() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        StringConcatenationConverter converter =
            new StringConcatenationConverter(null, codeAttributeEditor);

        // Assert
        assertNotNull(converter, "Converter should be created with null extraInstructionVisitor");
    }

    /**
     * Tests the constructor with null codeAttributeEditor.
     * Verifies that the converter can be instantiated with null CodeAttributeEditor.
     */
    @Test
    public void testConstructorWithNullCodeAttributeEditor() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        StringConcatenationConverter converter =
            new StringConcatenationConverter(extraInstructionVisitor, null);

        // Assert
        assertNotNull(converter, "Converter should be created with null codeAttributeEditor");
    }

    /**
     * Tests the constructor with both parameters null.
     * Verifies that the converter can be instantiated with all null parameters.
     */
    @Test
    public void testConstructorWithBothParametersNull() {
        // Act
        StringConcatenationConverter converter =
            new StringConcatenationConverter(null, null);

        // Assert
        assertNotNull(converter, "Converter should be created with both parameters null");
    }

    /**
     * Tests creating multiple converter instances with the same parameters.
     * Verifies that multiple instances can be created using the same objects.
     */
    @Test
    public void testMultipleConvertersWithSameParameters() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        StringConcatenationConverter converter1 =
            new StringConcatenationConverter(extraInstructionVisitor, codeAttributeEditor);
        StringConcatenationConverter converter2 =
            new StringConcatenationConverter(extraInstructionVisitor, codeAttributeEditor);

        // Assert
        assertNotNull(converter1, "First converter should be created");
        assertNotNull(converter2, "Second converter should be created");
        assertNotSame(converter1, converter2, "Converter instances should be different");
    }

    /**
     * Tests creating multiple converter instances with different parameters.
     * Verifies that converters can be created independently with different instances.
     */
    @Test
    public void testMultipleConvertersWithDifferentParameters() {
        // Arrange
        InstructionVisitor visitor1 = mock(InstructionVisitor.class);
        InstructionVisitor visitor2 = mock(InstructionVisitor.class);
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);

        // Act
        StringConcatenationConverter converter1 =
            new StringConcatenationConverter(visitor1, editor1);
        StringConcatenationConverter converter2 =
            new StringConcatenationConverter(visitor2, editor2);

        // Assert
        assertNotNull(converter1, "First converter should be created");
        assertNotNull(converter2, "Second converter should be created");
        assertNotSame(converter1, converter2, "Converter instances should be different");
    }

    /**
     * Tests the constructor with the same InstructionVisitor but different CodeAttributeEditor.
     * Verifies that converters can share some parameters while being independent.
     */
    @Test
    public void testConstructorWithSharedInstructionVisitor() {
        // Arrange
        InstructionVisitor sharedVisitor = mock(InstructionVisitor.class);
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);

        // Act
        StringConcatenationConverter converter1 =
            new StringConcatenationConverter(sharedVisitor, editor1);
        StringConcatenationConverter converter2 =
            new StringConcatenationConverter(sharedVisitor, editor2);

        // Assert
        assertNotNull(converter1, "First converter should be created");
        assertNotNull(converter2, "Second converter should be created");
        assertNotSame(converter1, converter2, "Converter instances should be different");
    }

    /**
     * Tests the constructor with the same CodeAttributeEditor but different InstructionVisitor.
     * Verifies that converters can share some parameters while being independent.
     */
    @Test
    public void testConstructorWithSharedCodeAttributeEditor() {
        // Arrange
        InstructionVisitor visitor1 = mock(InstructionVisitor.class);
        InstructionVisitor visitor2 = mock(InstructionVisitor.class);
        CodeAttributeEditor sharedEditor = mock(CodeAttributeEditor.class);

        // Act
        StringConcatenationConverter converter1 =
            new StringConcatenationConverter(visitor1, sharedEditor);
        StringConcatenationConverter converter2 =
            new StringConcatenationConverter(visitor2, sharedEditor);

        // Assert
        assertNotNull(converter1, "First converter should be created");
        assertNotNull(converter2, "Second converter should be created");
        assertNotSame(converter1, converter2, "Converter instances should be different");
    }

    /**
     * Tests the constructor with different combinations of null and non-null parameters.
     * Verifies that all combinations of null/non-null parameters are handled.
     */
    @Test
    public void testConstructorWithVariousNullCombinations() {
        // Test case 1: both non-null
        InstructionVisitor visitor1 = mock(InstructionVisitor.class);
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        StringConcatenationConverter converter1 =
            new StringConcatenationConverter(visitor1, editor1);
        assertNotNull(converter1, "Converter with both non-null should be created");

        // Test case 2: first null, second non-null
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);
        StringConcatenationConverter converter2 =
            new StringConcatenationConverter(null, editor2);
        assertNotNull(converter2, "Converter with null InstructionVisitor should be created");

        // Test case 3: first non-null, second null
        InstructionVisitor visitor3 = mock(InstructionVisitor.class);
        StringConcatenationConverter converter3 =
            new StringConcatenationConverter(visitor3, null);
        assertNotNull(converter3, "Converter with null CodeAttributeEditor should be created");

        // Test case 4: both null
        StringConcatenationConverter converter4 =
            new StringConcatenationConverter(null, null);
        assertNotNull(converter4, "Converter with both null should be created");
    }

    /**
     * Tests that the constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testConstructorIsEfficient() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        long startTime = System.nanoTime();

        // Act
        StringConcatenationConverter converter =
            new StringConcatenationConverter(extraInstructionVisitor, codeAttributeEditor);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(converter, "Converter should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests the constructor doesn't invoke any methods on the parameters.
     * Verifies that the constructor only stores the parameters without using them.
     */
    @Test
    public void testConstructorDoesNotInvokeParameters() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        StringConcatenationConverter converter =
            new StringConcatenationConverter(extraInstructionVisitor, codeAttributeEditor);

        // Assert
        assertNotNull(converter, "Converter should be created");
        verifyNoInteractions(extraInstructionVisitor);
        verifyNoInteractions(codeAttributeEditor);
    }

    /**
     * Tests that the constructor creates an instance of InstructionVisitor.
     * Verifies that StringConcatenationConverter can be used as an InstructionVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfInstructionVisitor() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        StringConcatenationConverter converter =
            new StringConcatenationConverter(extraInstructionVisitor, codeAttributeEditor);

        // Assert
        assertInstanceOf(InstructionVisitor.class, converter,
            "StringConcatenationConverter should implement InstructionVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of AttributeVisitor.
     * Verifies that StringConcatenationConverter can be used as an AttributeVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfAttributeVisitor() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        StringConcatenationConverter converter =
            new StringConcatenationConverter(extraInstructionVisitor, codeAttributeEditor);

        // Assert
        assertInstanceOf(proguard.classfile.attribute.visitor.AttributeVisitor.class, converter,
            "StringConcatenationConverter should implement AttributeVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of BootstrapMethodInfoVisitor.
     * Verifies that StringConcatenationConverter can be used as a BootstrapMethodInfoVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfBootstrapMethodInfoVisitor() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        StringConcatenationConverter converter =
            new StringConcatenationConverter(extraInstructionVisitor, codeAttributeEditor);

        // Assert
        assertInstanceOf(proguard.classfile.attribute.visitor.BootstrapMethodInfoVisitor.class, converter,
            "StringConcatenationConverter should implement BootstrapMethodInfoVisitor interface");
    }

    /**
     * Tests that the constructor creates an instance of ConstantVisitor.
     * Verifies that StringConcatenationConverter can be used as a ConstantVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfConstantVisitor() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        StringConcatenationConverter converter =
            new StringConcatenationConverter(extraInstructionVisitor, codeAttributeEditor);

        // Assert
        assertInstanceOf(proguard.classfile.constant.visitor.ConstantVisitor.class, converter,
            "StringConcatenationConverter should implement ConstantVisitor interface");
    }

    /**
     * Tests the constructor with only extraInstructionVisitor provided.
     * Verifies that converter can be created with only the first parameter and null for the second.
     */
    @Test
    public void testConstructorWithOnlyExtraInstructionVisitor() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        StringConcatenationConverter converter =
            new StringConcatenationConverter(extraInstructionVisitor, null);

        // Assert
        assertNotNull(converter, "Converter should be instantiated with only extraInstructionVisitor");
    }

    /**
     * Tests the constructor with only codeAttributeEditor provided.
     * Verifies that converter can be created with only the second parameter and null for the first.
     */
    @Test
    public void testConstructorWithOnlyCodeAttributeEditor() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        StringConcatenationConverter converter =
            new StringConcatenationConverter(null, codeAttributeEditor);

        // Assert
        assertNotNull(converter, "Converter should be instantiated with only codeAttributeEditor");
    }
}
