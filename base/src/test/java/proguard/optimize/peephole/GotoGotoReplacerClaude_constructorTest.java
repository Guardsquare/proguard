package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link GotoGotoReplacer} constructors.
 * Tests the constructors with signatures:
 * - (Lproguard/classfile/editor/CodeAttributeEditor;)V
 * - (Lproguard/classfile/editor/CodeAttributeEditor;Lproguard/classfile/instruction/visitor/InstructionVisitor;)V
 *
 * This constructor simplifies unconditional branches to other unconditional branches.
 * The single-parameter constructor takes a CodeAttributeEditor and delegates to the two-parameter
 * constructor with null for the extraInstructionVisitor parameter.
 */
public class GotoGotoReplacerClaude_constructorTest {

    // ========================================
    // Single-parameter Constructor Tests
    // ========================================

    /**
     * Tests the single-parameter constructor with a valid non-null CodeAttributeEditor.
     * Verifies that the replacer can be instantiated with a valid parameter.
     */
    @Test
    public void testConstructorWithValidCodeAttributeEditor() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "GotoGotoReplacer should be created successfully");
    }

    /**
     * Tests the constructor with null CodeAttributeEditor.
     * Verifies that the replacer can be instantiated with null parameter.
     */
    @Test
    public void testConstructorWithNullCodeAttributeEditor() {
        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(null);

        // Assert
        assertNotNull(replacer, "GotoGotoReplacer should be created with null codeAttributeEditor");
    }

    /**
     * Tests creating multiple replacer instances with the same CodeAttributeEditor.
     * Verifies that multiple instances can be created using the same object.
     */
    @Test
    public void testMultipleReplacersWithSameCodeAttributeEditor() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        GotoGotoReplacer replacer1 = new GotoGotoReplacer(codeAttributeEditor);
        GotoGotoReplacer replacer2 = new GotoGotoReplacer(codeAttributeEditor);

        // Assert
        assertNotNull(replacer1, "First replacer should be created");
        assertNotNull(replacer2, "Second replacer should be created");
        assertNotSame(replacer1, replacer2, "Replacer instances should be different");
    }

    /**
     * Tests creating multiple replacer instances with different CodeAttributeEditors.
     * Verifies that replacers can be created independently with different instances.
     */
    @Test
    public void testMultipleReplacersWithDifferentCodeAttributeEditors() {
        // Arrange
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);

        // Act
        GotoGotoReplacer replacer1 = new GotoGotoReplacer(editor1);
        GotoGotoReplacer replacer2 = new GotoGotoReplacer(editor2);

        // Assert
        assertNotNull(replacer1, "First replacer should be created");
        assertNotNull(replacer2, "Second replacer should be created");
        assertNotSame(replacer1, replacer2, "Replacer instances should be different");
    }

    /**
     * Tests that the constructor doesn't invoke any methods on the CodeAttributeEditor.
     * Verifies that the constructor only stores the parameter without using it.
     */
    @Test
    public void testConstructorDoesNotInvokeCodeAttributeEditor() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "GotoGotoReplacer should be created");
        verifyNoInteractions(codeAttributeEditor);
    }

    /**
     * Tests that the constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testConstructorIsEfficient() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        long startTime = System.nanoTime();

        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(codeAttributeEditor);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(replacer, "GotoGotoReplacer should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that the constructor creates an instance of InstructionVisitor.
     * Verifies that GotoGotoReplacer implements the InstructionVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfInstructionVisitor() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(codeAttributeEditor);

        // Assert
        assertInstanceOf(InstructionVisitor.class, replacer,
            "GotoGotoReplacer should implement InstructionVisitor interface");
    }

    /**
     * Tests creating a sequence of replacers.
     * Verifies that multiple replacers can be created sequentially without issues.
     */
    @Test
    public void testSequentialReplacerCreation() {
        // Act & Assert - create multiple replacers sequentially
        for (int i = 0; i < 10; i++) {
            CodeAttributeEditor editor = mock(CodeAttributeEditor.class);
            GotoGotoReplacer replacer = new GotoGotoReplacer(editor);
            assertNotNull(replacer, "Replacer " + i + " should be created");
        }
    }

    /**
     * Tests that two replacers created with null are independent instances.
     * Verifies that even with null parameters, distinct instances are created.
     */
    @Test
    public void testMultipleReplacersWithNullAreIndependent() {
        // Act
        GotoGotoReplacer replacer1 = new GotoGotoReplacer(null);
        GotoGotoReplacer replacer2 = new GotoGotoReplacer(null);

        // Assert
        assertNotNull(replacer1, "First replacer should be created");
        assertNotNull(replacer2, "Second replacer should be created");
        assertNotSame(replacer1, replacer2, "Replacer instances should be different even with null parameters");
    }

    /**
     * Tests alternating creation of replacers with null and non-null parameters.
     * Verifies that replacers can be created with various parameter combinations.
     */
    @Test
    public void testAlternatingNullAndNonNullParameters() {
        // Arrange
        CodeAttributeEditor editor = mock(CodeAttributeEditor.class);

        // Act
        GotoGotoReplacer replacer1 = new GotoGotoReplacer(editor);
        GotoGotoReplacer replacer2 = new GotoGotoReplacer(null);
        GotoGotoReplacer replacer3 = new GotoGotoReplacer(editor);
        GotoGotoReplacer replacer4 = new GotoGotoReplacer(null);

        // Assert
        assertNotNull(replacer1, "Replacer 1 should be created");
        assertNotNull(replacer2, "Replacer 2 should be created");
        assertNotNull(replacer3, "Replacer 3 should be created");
        assertNotNull(replacer4, "Replacer 4 should be created");
    }

    /**
     * Tests that the single-parameter constructor properly initializes the instance for use.
     * Verifies that the created instance is in a valid state by checking it can be used as an InstructionVisitor.
     */
    @Test
    public void testConstructorInitializesInstanceProperly() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "GotoGotoReplacer should be created");
        // The replacer should be usable as an InstructionVisitor
        InstructionVisitor visitor = replacer;
        assertNotNull(visitor, "GotoGotoReplacer should be usable as InstructionVisitor");
    }

    // ========================================
    // Two-parameter Constructor Tests
    // ========================================

    /**
     * Tests the two-parameter constructor with valid non-null parameters.
     * Verifies that the replacer can be instantiated with both parameters provided.
     */
    @Test
    public void testTwoParamConstructorWithValidParameters() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(codeAttributeEditor, extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "GotoGotoReplacer should be created successfully");
    }

    /**
     * Tests the two-parameter constructor with null extraInstructionVisitor.
     * Verifies that the replacer can be instantiated with null InstructionVisitor.
     */
    @Test
    public void testTwoParamConstructorWithNullExtraInstructionVisitor() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(codeAttributeEditor, null);

        // Assert
        assertNotNull(replacer, "GotoGotoReplacer should be created with null extraInstructionVisitor");
    }

    /**
     * Tests the two-parameter constructor with null codeAttributeEditor.
     * Verifies that the replacer can be instantiated with null CodeAttributeEditor.
     */
    @Test
    public void testTwoParamConstructorWithNullCodeAttributeEditor() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(null, extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "GotoGotoReplacer should be created with null codeAttributeEditor");
    }

    /**
     * Tests the two-parameter constructor with both parameters null.
     * Verifies that the replacer can be instantiated with all null parameters.
     */
    @Test
    public void testTwoParamConstructorWithBothParametersNull() {
        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(null, null);

        // Assert
        assertNotNull(replacer, "GotoGotoReplacer should be created with both parameters null");
    }

    /**
     * Tests creating multiple replacer instances with the same parameters.
     * Verifies that multiple instances can be created using the same objects.
     */
    @Test
    public void testTwoParamConstructorMultipleInstancesWithSameParameters() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        GotoGotoReplacer replacer1 = new GotoGotoReplacer(codeAttributeEditor, extraInstructionVisitor);
        GotoGotoReplacer replacer2 = new GotoGotoReplacer(codeAttributeEditor, extraInstructionVisitor);

        // Assert
        assertNotNull(replacer1, "First replacer should be created");
        assertNotNull(replacer2, "Second replacer should be created");
        assertNotSame(replacer1, replacer2, "Replacer instances should be different");
    }

    /**
     * Tests creating multiple replacer instances with different parameters.
     * Verifies that replacers can be created independently with different instances.
     */
    @Test
    public void testTwoParamConstructorMultipleInstancesWithDifferentParameters() {
        // Arrange
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);
        InstructionVisitor visitor1 = mock(InstructionVisitor.class);
        InstructionVisitor visitor2 = mock(InstructionVisitor.class);

        // Act
        GotoGotoReplacer replacer1 = new GotoGotoReplacer(editor1, visitor1);
        GotoGotoReplacer replacer2 = new GotoGotoReplacer(editor2, visitor2);

        // Assert
        assertNotNull(replacer1, "First replacer should be created");
        assertNotNull(replacer2, "Second replacer should be created");
        assertNotSame(replacer1, replacer2, "Replacer instances should be different");
    }

    /**
     * Tests the two-parameter constructor with the same CodeAttributeEditor but different InstructionVisitor.
     * Verifies that replacers can share some parameters while being independent.
     */
    @Test
    public void testTwoParamConstructorWithSharedCodeAttributeEditor() {
        // Arrange
        CodeAttributeEditor sharedEditor = mock(CodeAttributeEditor.class);
        InstructionVisitor visitor1 = mock(InstructionVisitor.class);
        InstructionVisitor visitor2 = mock(InstructionVisitor.class);

        // Act
        GotoGotoReplacer replacer1 = new GotoGotoReplacer(sharedEditor, visitor1);
        GotoGotoReplacer replacer2 = new GotoGotoReplacer(sharedEditor, visitor2);

        // Assert
        assertNotNull(replacer1, "First replacer should be created");
        assertNotNull(replacer2, "Second replacer should be created");
        assertNotSame(replacer1, replacer2, "Replacer instances should be different");
    }

    /**
     * Tests the two-parameter constructor with the same InstructionVisitor but different CodeAttributeEditor.
     * Verifies that replacers can share some parameters while being independent.
     */
    @Test
    public void testTwoParamConstructorWithSharedInstructionVisitor() {
        // Arrange
        InstructionVisitor sharedVisitor = mock(InstructionVisitor.class);
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);

        // Act
        GotoGotoReplacer replacer1 = new GotoGotoReplacer(editor1, sharedVisitor);
        GotoGotoReplacer replacer2 = new GotoGotoReplacer(editor2, sharedVisitor);

        // Assert
        assertNotNull(replacer1, "First replacer should be created");
        assertNotNull(replacer2, "Second replacer should be created");
        assertNotSame(replacer1, replacer2, "Replacer instances should be different");
    }

    /**
     * Tests the two-parameter constructor with different combinations of null and non-null parameters.
     * Verifies that all combinations of null/non-null parameters are handled.
     */
    @Test
    public void testTwoParamConstructorWithVariousNullCombinations() {
        // Test case 1: both non-null
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        InstructionVisitor visitor1 = mock(InstructionVisitor.class);
        GotoGotoReplacer replacer1 = new GotoGotoReplacer(editor1, visitor1);
        assertNotNull(replacer1, "Replacer with both non-null should be created");

        // Test case 2: first null, second non-null
        InstructionVisitor visitor2 = mock(InstructionVisitor.class);
        GotoGotoReplacer replacer2 = new GotoGotoReplacer(null, visitor2);
        assertNotNull(replacer2, "Replacer with null CodeAttributeEditor should be created");

        // Test case 3: first non-null, second null
        CodeAttributeEditor editor3 = mock(CodeAttributeEditor.class);
        GotoGotoReplacer replacer3 = new GotoGotoReplacer(editor3, null);
        assertNotNull(replacer3, "Replacer with null InstructionVisitor should be created");

        // Test case 4: both null
        GotoGotoReplacer replacer4 = new GotoGotoReplacer(null, null);
        assertNotNull(replacer4, "Replacer with both null should be created");
    }

    /**
     * Tests that the two-parameter constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testTwoParamConstructorIsEfficient() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);
        long startTime = System.nanoTime();

        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(codeAttributeEditor, extraInstructionVisitor);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(replacer, "GotoGotoReplacer should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that the two-parameter constructor doesn't invoke any methods on the parameters.
     * Verifies that the constructor only stores the parameters without using them.
     */
    @Test
    public void testTwoParamConstructorDoesNotInvokeParameters() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(codeAttributeEditor, extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "GotoGotoReplacer should be created");
        verifyNoInteractions(codeAttributeEditor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that the two-parameter constructor creates an instance of InstructionVisitor.
     * Verifies that GotoGotoReplacer implements the InstructionVisitor interface.
     */
    @Test
    public void testTwoParamConstructorCreatesInstanceOfInstructionVisitor() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(codeAttributeEditor, extraInstructionVisitor);

        // Assert
        assertInstanceOf(InstructionVisitor.class, replacer,
            "GotoGotoReplacer should implement InstructionVisitor interface");
    }

    /**
     * Tests that the two-parameter constructor properly initializes the instance for use.
     * Verifies that the created instance is in a valid state.
     */
    @Test
    public void testTwoParamConstructorInitializesInstanceProperly() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(codeAttributeEditor, extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "GotoGotoReplacer should be created");
        // The replacer should be usable as an InstructionVisitor
        InstructionVisitor visitor = replacer;
        assertNotNull(visitor, "GotoGotoReplacer should be usable as InstructionVisitor");
    }

    /**
     * Tests that two replacers created with the two-parameter constructor with nulls are independent.
     * Verifies that even with null parameters, distinct instances are created.
     */
    @Test
    public void testTwoParamConstructorMultipleInstancesWithNullAreIndependent() {
        // Act
        GotoGotoReplacer replacer1 = new GotoGotoReplacer(null, null);
        GotoGotoReplacer replacer2 = new GotoGotoReplacer(null, null);

        // Assert
        assertNotNull(replacer1, "First replacer should be created");
        assertNotNull(replacer2, "Second replacer should be created");
        assertNotSame(replacer1, replacer2, "Replacer instances should be different even with null parameters");
    }

    /**
     * Tests creating a sequence of replacers with the two-parameter constructor.
     * Verifies that multiple replacers can be created sequentially without issues.
     */
    @Test
    public void testTwoParamConstructorSequentialCreation() {
        // Act & Assert - create multiple replacers sequentially
        for (int i = 0; i < 10; i++) {
            CodeAttributeEditor editor = mock(CodeAttributeEditor.class);
            InstructionVisitor visitor = mock(InstructionVisitor.class);
            GotoGotoReplacer replacer = new GotoGotoReplacer(editor, visitor);
            assertNotNull(replacer, "Replacer " + i + " should be created");
        }
    }

    /**
     * Tests the two-parameter constructor with only CodeAttributeEditor provided.
     * Verifies that replacer can be created with only the first parameter and null for the second.
     */
    @Test
    public void testTwoParamConstructorWithOnlyCodeAttributeEditor() {
        // Arrange
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(codeAttributeEditor, null);

        // Assert
        assertNotNull(replacer, "GotoGotoReplacer should be instantiated with only CodeAttributeEditor");
    }

    /**
     * Tests the two-parameter constructor with only extraInstructionVisitor provided.
     * Verifies that replacer can be created with only the second parameter and null for the first.
     */
    @Test
    public void testTwoParamConstructorWithOnlyExtraInstructionVisitor() {
        // Arrange
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        GotoGotoReplacer replacer = new GotoGotoReplacer(null, extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "GotoGotoReplacer should be instantiated with only extraInstructionVisitor");
    }

    // ========================================
    // Cross-constructor Tests
    // ========================================

    /**
     * Tests that the single-parameter and two-parameter constructors create independent instances.
     * Verifies that both constructors can be used interchangeably.
     */
    @Test
    public void testBothConstructorsCreateIndependentInstances() {
        // Arrange
        CodeAttributeEditor editor = mock(CodeAttributeEditor.class);
        InstructionVisitor visitor = mock(InstructionVisitor.class);

        // Act
        GotoGotoReplacer replacer1 = new GotoGotoReplacer(editor);
        GotoGotoReplacer replacer2 = new GotoGotoReplacer(editor, visitor);
        GotoGotoReplacer replacer3 = new GotoGotoReplacer(editor, null);

        // Assert
        assertNotNull(replacer1, "Single-param constructor should create instance");
        assertNotNull(replacer2, "Two-param constructor with visitor should create instance");
        assertNotNull(replacer3, "Two-param constructor with null visitor should create instance");
        assertNotSame(replacer1, replacer2, "Instances should be different");
        assertNotSame(replacer1, replacer3, "Instances should be different");
        assertNotSame(replacer2, replacer3, "Instances should be different");
    }

    /**
     * Tests that multiple GotoGotoReplacer instances work independently.
     * Verifies that using both constructors doesn't cause interference.
     */
    @Test
    public void testMultipleInstancesFromBothConstructorsWorkIndependently() {
        // Arrange
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);
        InstructionVisitor visitor = mock(InstructionVisitor.class);

        // Act
        GotoGotoReplacer replacer1 = new GotoGotoReplacer(editor1);
        GotoGotoReplacer replacer2 = new GotoGotoReplacer(editor2, visitor);

        // Assert
        assertNotNull(replacer1, "First replacer should be created");
        assertNotNull(replacer2, "Second replacer should be created");
        assertNotSame(replacer1, replacer2, "Replacer instances should be different");
        assertInstanceOf(InstructionVisitor.class, replacer1, "First replacer should implement InstructionVisitor");
        assertInstanceOf(InstructionVisitor.class, replacer2, "Second replacer should implement InstructionVisitor");
    }
}
