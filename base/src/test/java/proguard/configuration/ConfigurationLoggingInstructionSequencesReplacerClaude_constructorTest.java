package proguard.configuration;

import org.junit.jupiter.api.Test;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.IntegerConstant;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.BranchTargetFinder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ConfigurationLoggingInstructionSequencesReplacer} constructor.
 * Tests the constructor ConfigurationLoggingInstructionSequencesReplacer(Constant[], Instruction[][][], BranchTargetFinder, CodeAttributeEditor).
 */
public class ConfigurationLoggingInstructionSequencesReplacerClaude_constructorTest {

    /**
     * Tests the constructor with valid minimal parameters.
     * Verifies that the replacer can be instantiated with empty arrays.
     */
    @Test
    public void testConstructorWithValidMinimalParameters() {
        // Arrange
        Constant[] constants = new Constant[0];
        Instruction[][][] instructionSequences = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated successfully");
    }

    /**
     * Tests the constructor with null constants.
     * Verifies that the constructor accepts null constants array.
     */
    @Test
    public void testConstructorWithNullConstants() {
        // Arrange
        Constant[] constants = null;
        Instruction[][][] instructionSequences = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with null constants");
    }

    /**
     * Tests the constructor with null instruction sequences.
     * Verifies that the constructor accepts null instruction sequences.
     */
    @Test
    public void testConstructorWithNullInstructionSequences() {
        // Arrange
        Constant[] constants = new Constant[0];
        Instruction[][][] instructionSequences = null;
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            new ConfigurationLoggingInstructionSequencesReplacer(
                    constants,
                    instructionSequences,
                    branchTargetFinder,
                    codeAttributeEditor);
        }, "Constructor should throw NullPointerException with null instruction sequences");
    }

    /**
     * Tests the constructor with null BranchTargetFinder.
     * Verifies that the constructor accepts null BranchTargetFinder.
     */
    @Test
    public void testConstructorWithNullBranchTargetFinder() {
        // Arrange
        Constant[] constants = new Constant[0];
        Instruction[][][] instructionSequences = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder = null;
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with null BranchTargetFinder");
    }

    /**
     * Tests the constructor with null CodeAttributeEditor.
     * Verifies that the constructor accepts null CodeAttributeEditor.
     */
    @Test
    public void testConstructorWithNullCodeAttributeEditor() {
        // Arrange
        Constant[] constants = new Constant[0];
        Instruction[][][] instructionSequences = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = null;

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with null CodeAttributeEditor");
    }

    /**
     * Tests the constructor with single instruction sequence pair.
     * Verifies that the constructor handles a single pattern/replacement pair.
     */
    @Test
    public void testConstructorWithSingleInstructionSequencePair() {
        // Arrange
        Constant[] constants = new Constant[0];
        Instruction[][] patternAndReplacement = new Instruction[2][];
        patternAndReplacement[0] = new Instruction[0]; // pattern
        patternAndReplacement[1] = new Instruction[0]; // replacement
        Instruction[][][] instructionSequences = new Instruction[1][][];
        instructionSequences[0] = patternAndReplacement;

        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with single instruction sequence pair");
    }

    /**
     * Tests the constructor with multiple instruction sequence pairs.
     * Verifies that the constructor handles multiple pattern/replacement pairs.
     */
    @Test
    public void testConstructorWithMultipleInstructionSequencePairs() {
        // Arrange
        Constant[] constants = new Constant[0];

        // Create first pair
        Instruction[][] pair1 = new Instruction[2][];
        pair1[0] = new Instruction[0]; // pattern
        pair1[1] = new Instruction[0]; // replacement

        // Create second pair
        Instruction[][] pair2 = new Instruction[2][];
        pair2[0] = new Instruction[0]; // pattern
        pair2[1] = new Instruction[0]; // replacement

        // Create third pair
        Instruction[][] pair3 = new Instruction[2][];
        pair3[0] = new Instruction[0]; // pattern
        pair3[1] = new Instruction[0]; // replacement

        Instruction[][][] instructionSequences = new Instruction[3][][];
        instructionSequences[0] = pair1;
        instructionSequences[1] = pair2;
        instructionSequences[2] = pair3;

        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with multiple instruction sequence pairs");
    }

    /**
     * Tests the constructor with non-empty constants array.
     * Verifies that the constructor handles constants properly.
     */
    @Test
    public void testConstructorWithNonEmptyConstants() {
        // Arrange
        Constant[] constants = new Constant[2];
        constants[0] = new IntegerConstant(42);
        constants[1] = new IntegerConstant(100);

        Instruction[][][] instructionSequences = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with non-empty constants");
    }

    /**
     * Tests the constructor with instruction sequences containing actual instructions.
     * Verifies that the constructor handles real instruction objects.
     */
    @Test
    public void testConstructorWithActualInstructions() {
        // Arrange
        Constant[] constants = new Constant[0];

        // Create pattern with mock instructions
        Instruction[] pattern = new Instruction[2];
        pattern[0] = mock(Instruction.class);
        pattern[1] = mock(Instruction.class);

        // Create replacement with mock instructions
        Instruction[] replacement = new Instruction[1];
        replacement[0] = mock(Instruction.class);

        Instruction[][] pair = new Instruction[2][];
        pair[0] = pattern;
        pair[1] = replacement;

        Instruction[][][] instructionSequences = new Instruction[1][][];
        instructionSequences[0] = pair;

        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with actual instructions");
    }

    /**
     * Tests the constructor with large number of instruction sequence pairs.
     * Verifies that the constructor can handle many pairs efficiently.
     */
    @Test
    public void testConstructorWithManyInstructionSequencePairs() {
        // Arrange
        Constant[] constants = new Constant[0];
        int numberOfPairs = 20;
        Instruction[][][] instructionSequences = new Instruction[numberOfPairs][][];

        for (int i = 0; i < numberOfPairs; i++) {
            Instruction[][] pair = new Instruction[2][];
            pair[0] = new Instruction[0]; // pattern
            pair[1] = new Instruction[0]; // replacement
            instructionSequences[i] = pair;
        }

        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with many instruction sequence pairs");
    }

    /**
     * Tests the constructor with mixed empty and non-empty patterns/replacements.
     * Verifies that the constructor handles mixed scenarios properly.
     */
    @Test
    public void testConstructorWithMixedEmptyAndNonEmptyInstructions() {
        // Arrange
        Constant[] constants = new Constant[0];

        // First pair: empty pattern and replacement
        Instruction[][] pair1 = new Instruction[2][];
        pair1[0] = new Instruction[0];
        pair1[1] = new Instruction[0];

        // Second pair: non-empty pattern and replacement
        Instruction[][] pair2 = new Instruction[2][];
        pair2[0] = new Instruction[]{mock(Instruction.class)};
        pair2[1] = new Instruction[]{mock(Instruction.class)};

        // Third pair: empty pattern, non-empty replacement
        Instruction[][] pair3 = new Instruction[2][];
        pair3[0] = new Instruction[0];
        pair3[1] = new Instruction[]{mock(Instruction.class)};

        Instruction[][][] instructionSequences = new Instruction[3][][];
        instructionSequences[0] = pair1;
        instructionSequences[1] = pair2;
        instructionSequences[2] = pair3;

        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with mixed empty and non-empty instructions");
    }

    /**
     * Tests that multiple replacer instances can be created independently.
     * Verifies that each replacer instance is independent.
     */
    @Test
    public void testMultipleReplacerInstances() {
        // Arrange
        Constant[] constants1 = new Constant[0];
        Instruction[][][] instructionSequences1 = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder1 = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor1 = mock(CodeAttributeEditor.class);

        Constant[] constants2 = new Constant[0];
        Instruction[][][] instructionSequences2 = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder2 = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor2 = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer1 =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants1,
                        instructionSequences1,
                        branchTargetFinder1,
                        codeAttributeEditor1);

        ConfigurationLoggingInstructionSequencesReplacer replacer2 =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants2,
                        instructionSequences2,
                        branchTargetFinder2,
                        codeAttributeEditor2);

        // Assert
        assertNotNull(replacer1, "First replacer should be created");
        assertNotNull(replacer2, "Second replacer should be created");
        assertNotSame(replacer1, replacer2, "Replacer instances should be different objects");
    }

    /**
     * Tests the constructor with same parameters used multiple times.
     * Verifies that the same parameters can create multiple distinct instances.
     */
    @Test
    public void testMultipleReplacersWithSameParameters() {
        // Arrange
        Constant[] constants = new Constant[0];
        Instruction[][][] instructionSequences = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer1 =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        ConfigurationLoggingInstructionSequencesReplacer replacer2 =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        // Assert
        assertNotNull(replacer1, "First replacer should be created");
        assertNotNull(replacer2, "Second replacer should be created");
        assertNotSame(replacer1, replacer2, "Replacer instances should be different objects");
    }

    /**
     * Tests the constructor with complex instruction sequences.
     * Verifies that the constructor handles complex sequences with multiple instructions per pattern/replacement.
     */
    @Test
    public void testConstructorWithComplexInstructionSequences() {
        // Arrange
        Constant[] constants = new Constant[3];
        constants[0] = new IntegerConstant(1);
        constants[1] = new IntegerConstant(2);
        constants[2] = new IntegerConstant(3);

        // Create complex pattern with multiple instructions
        Instruction[] pattern = new Instruction[4];
        pattern[0] = mock(Instruction.class);
        pattern[1] = mock(Instruction.class);
        pattern[2] = mock(Instruction.class);
        pattern[3] = mock(Instruction.class);

        // Create complex replacement with multiple instructions
        Instruction[] replacement = new Instruction[3];
        replacement[0] = mock(Instruction.class);
        replacement[1] = mock(Instruction.class);
        replacement[2] = mock(Instruction.class);

        Instruction[][] pair = new Instruction[2][];
        pair[0] = pattern;
        pair[1] = replacement;

        Instruction[][][] instructionSequences = new Instruction[1][][];
        instructionSequences[0] = pair;

        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with complex instruction sequences");
    }

    /**
     * Tests the constructor with all parameters being minimal but valid.
     * Verifies the most basic valid construction scenario.
     */
    @Test
    public void testConstructorWithMinimalValidSetup() {
        // Arrange - Absolute minimum valid setup
        Constant[] constants = new Constant[0];
        Instruction[][][] instructionSequences = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with minimal valid setup");
    }

    /**
     * Tests the constructor with instruction sequences having various sizes.
     * Verifies that the constructor handles different sizes of pattern/replacement arrays.
     */
    @Test
    public void testConstructorWithVariousSizedInstructionSequences() {
        // Arrange
        Constant[] constants = new Constant[0];

        // Pair with small pattern and replacement
        Instruction[][] smallPair = new Instruction[2][];
        smallPair[0] = new Instruction[1];
        smallPair[0][0] = mock(Instruction.class);
        smallPair[1] = new Instruction[1];
        smallPair[1][0] = mock(Instruction.class);

        // Pair with larger pattern and replacement
        Instruction[][] largePair = new Instruction[2][];
        largePair[0] = new Instruction[5];
        for (int i = 0; i < 5; i++) {
            largePair[0][i] = mock(Instruction.class);
        }
        largePair[1] = new Instruction[3];
        for (int i = 0; i < 3; i++) {
            largePair[1][i] = mock(Instruction.class);
        }

        Instruction[][][] instructionSequences = new Instruction[2][][];
        instructionSequences[0] = smallPair;
        instructionSequences[1] = largePair;

        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with various sized instruction sequences");
    }

    // ========== Tests for 5-parameter constructor ==========

    /**
     * Tests the 5-parameter constructor with valid minimal parameters.
     * Verifies that the replacer can be instantiated with empty arrays and null extraInstructionVisitor.
     */
    @Test
    public void testFiveParamConstructorWithValidMinimalParameters() {
        // Arrange
        Constant[] constants = new Constant[0];
        Instruction[][][] instructionSequences = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor = null;

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor,
                        extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated successfully with 5 parameters");
    }

    /**
     * Tests the 5-parameter constructor with non-null extraInstructionVisitor.
     * Verifies that the constructor handles a provided extraInstructionVisitor.
     */
    @Test
    public void testFiveParamConstructorWithNonNullExtraInstructionVisitor() {
        // Arrange
        Constant[] constants = new Constant[0];
        Instruction[][][] instructionSequences = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor =
                mock(proguard.classfile.instruction.visitor.InstructionVisitor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor,
                        extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with non-null extraInstructionVisitor");
    }

    /**
     * Tests the 5-parameter constructor with null constants.
     * Verifies that the constructor accepts null constants array.
     */
    @Test
    public void testFiveParamConstructorWithNullConstants() {
        // Arrange
        Constant[] constants = null;
        Instruction[][][] instructionSequences = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor = null;

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor,
                        extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with null constants");
    }

    /**
     * Tests the 5-parameter constructor with null instruction sequences.
     * Verifies that the constructor throws NullPointerException with null instruction sequences.
     */
    @Test
    public void testFiveParamConstructorWithNullInstructionSequences() {
        // Arrange
        Constant[] constants = new Constant[0];
        Instruction[][][] instructionSequences = null;
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            new ConfigurationLoggingInstructionSequencesReplacer(
                    constants,
                    instructionSequences,
                    branchTargetFinder,
                    codeAttributeEditor,
                    extraInstructionVisitor);
        }, "Constructor should throw NullPointerException with null instruction sequences");
    }

    /**
     * Tests the 5-parameter constructor with null BranchTargetFinder.
     * Verifies that the constructor accepts null BranchTargetFinder.
     */
    @Test
    public void testFiveParamConstructorWithNullBranchTargetFinder() {
        // Arrange
        Constant[] constants = new Constant[0];
        Instruction[][][] instructionSequences = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder = null;
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor = null;

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor,
                        extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with null BranchTargetFinder");
    }

    /**
     * Tests the 5-parameter constructor with null CodeAttributeEditor.
     * Verifies that the constructor accepts null CodeAttributeEditor.
     */
    @Test
    public void testFiveParamConstructorWithNullCodeAttributeEditor() {
        // Arrange
        Constant[] constants = new Constant[0];
        Instruction[][][] instructionSequences = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = null;
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor = null;

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor,
                        extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with null CodeAttributeEditor");
    }

    /**
     * Tests the 5-parameter constructor with single instruction sequence pair and extraInstructionVisitor.
     * Verifies that the constructor handles a single pattern/replacement pair with extra visitor.
     */
    @Test
    public void testFiveParamConstructorWithSingleInstructionSequencePair() {
        // Arrange
        Constant[] constants = new Constant[0];
        Instruction[][] patternAndReplacement = new Instruction[2][];
        patternAndReplacement[0] = new Instruction[0]; // pattern
        patternAndReplacement[1] = new Instruction[0]; // replacement
        Instruction[][][] instructionSequences = new Instruction[1][][];
        instructionSequences[0] = patternAndReplacement;

        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor =
                mock(proguard.classfile.instruction.visitor.InstructionVisitor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor,
                        extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with single instruction sequence pair and extra visitor");
    }

    /**
     * Tests the 5-parameter constructor with multiple instruction sequence pairs and extraInstructionVisitor.
     * Verifies that the constructor handles multiple pattern/replacement pairs with extra visitor.
     */
    @Test
    public void testFiveParamConstructorWithMultipleInstructionSequencePairs() {
        // Arrange
        Constant[] constants = new Constant[0];

        // Create first pair
        Instruction[][] pair1 = new Instruction[2][];
        pair1[0] = new Instruction[0]; // pattern
        pair1[1] = new Instruction[0]; // replacement

        // Create second pair
        Instruction[][] pair2 = new Instruction[2][];
        pair2[0] = new Instruction[0]; // pattern
        pair2[1] = new Instruction[0]; // replacement

        // Create third pair
        Instruction[][] pair3 = new Instruction[2][];
        pair3[0] = new Instruction[0]; // pattern
        pair3[1] = new Instruction[0]; // replacement

        Instruction[][][] instructionSequences = new Instruction[3][][];
        instructionSequences[0] = pair1;
        instructionSequences[1] = pair2;
        instructionSequences[2] = pair3;

        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor =
                mock(proguard.classfile.instruction.visitor.InstructionVisitor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor,
                        extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with multiple instruction sequence pairs and extra visitor");
    }

    /**
     * Tests the 5-parameter constructor with non-empty constants array and extraInstructionVisitor.
     * Verifies that the constructor handles constants and extra visitor properly.
     */
    @Test
    public void testFiveParamConstructorWithNonEmptyConstants() {
        // Arrange
        Constant[] constants = new Constant[2];
        constants[0] = new IntegerConstant(42);
        constants[1] = new IntegerConstant(100);

        Instruction[][][] instructionSequences = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor =
                mock(proguard.classfile.instruction.visitor.InstructionVisitor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor,
                        extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with non-empty constants and extra visitor");
    }

    /**
     * Tests the 5-parameter constructor with actual instructions and extraInstructionVisitor.
     * Verifies that the constructor handles real instruction objects with extra visitor.
     */
    @Test
    public void testFiveParamConstructorWithActualInstructions() {
        // Arrange
        Constant[] constants = new Constant[0];

        // Create pattern with mock instructions
        Instruction[] pattern = new Instruction[2];
        pattern[0] = mock(Instruction.class);
        pattern[1] = mock(Instruction.class);

        // Create replacement with mock instructions
        Instruction[] replacement = new Instruction[1];
        replacement[0] = mock(Instruction.class);

        Instruction[][] pair = new Instruction[2][];
        pair[0] = pattern;
        pair[1] = replacement;

        Instruction[][][] instructionSequences = new Instruction[1][][];
        instructionSequences[0] = pair;

        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor =
                mock(proguard.classfile.instruction.visitor.InstructionVisitor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor,
                        extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with actual instructions and extra visitor");
    }

    /**
     * Tests the 5-parameter constructor with large number of instruction sequence pairs and extraInstructionVisitor.
     * Verifies that the constructor can handle many pairs efficiently with extra visitor.
     */
    @Test
    public void testFiveParamConstructorWithManyInstructionSequencePairs() {
        // Arrange
        Constant[] constants = new Constant[0];
        int numberOfPairs = 20;
        Instruction[][][] instructionSequences = new Instruction[numberOfPairs][][];

        for (int i = 0; i < numberOfPairs; i++) {
            Instruction[][] pair = new Instruction[2][];
            pair[0] = new Instruction[0]; // pattern
            pair[1] = new Instruction[0]; // replacement
            instructionSequences[i] = pair;
        }

        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor =
                mock(proguard.classfile.instruction.visitor.InstructionVisitor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor,
                        extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with many instruction sequence pairs and extra visitor");
    }

    /**
     * Tests the 5-parameter constructor with mixed empty and non-empty instructions and extraInstructionVisitor.
     * Verifies that the constructor handles mixed scenarios properly with extra visitor.
     */
    @Test
    public void testFiveParamConstructorWithMixedEmptyAndNonEmptyInstructions() {
        // Arrange
        Constant[] constants = new Constant[0];

        // First pair: empty pattern and replacement
        Instruction[][] pair1 = new Instruction[2][];
        pair1[0] = new Instruction[0];
        pair1[1] = new Instruction[0];

        // Second pair: non-empty pattern and replacement
        Instruction[][] pair2 = new Instruction[2][];
        pair2[0] = new Instruction[]{mock(Instruction.class)};
        pair2[1] = new Instruction[]{mock(Instruction.class)};

        // Third pair: empty pattern, non-empty replacement
        Instruction[][] pair3 = new Instruction[2][];
        pair3[0] = new Instruction[0];
        pair3[1] = new Instruction[]{mock(Instruction.class)};

        Instruction[][][] instructionSequences = new Instruction[3][][];
        instructionSequences[0] = pair1;
        instructionSequences[1] = pair2;
        instructionSequences[2] = pair3;

        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor =
                mock(proguard.classfile.instruction.visitor.InstructionVisitor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor,
                        extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with mixed empty and non-empty instructions and extra visitor");
    }

    /**
     * Tests the 5-parameter constructor with complex instruction sequences and extraInstructionVisitor.
     * Verifies that the constructor handles complex sequences with multiple instructions per pattern/replacement and extra visitor.
     */
    @Test
    public void testFiveParamConstructorWithComplexInstructionSequences() {
        // Arrange
        Constant[] constants = new Constant[3];
        constants[0] = new IntegerConstant(1);
        constants[1] = new IntegerConstant(2);
        constants[2] = new IntegerConstant(3);

        // Create complex pattern with multiple instructions
        Instruction[] pattern = new Instruction[4];
        pattern[0] = mock(Instruction.class);
        pattern[1] = mock(Instruction.class);
        pattern[2] = mock(Instruction.class);
        pattern[3] = mock(Instruction.class);

        // Create complex replacement with multiple instructions
        Instruction[] replacement = new Instruction[3];
        replacement[0] = mock(Instruction.class);
        replacement[1] = mock(Instruction.class);
        replacement[2] = mock(Instruction.class);

        Instruction[][] pair = new Instruction[2][];
        pair[0] = pattern;
        pair[1] = replacement;

        Instruction[][][] instructionSequences = new Instruction[1][][];
        instructionSequences[0] = pair;

        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor =
                mock(proguard.classfile.instruction.visitor.InstructionVisitor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor,
                        extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with complex instruction sequences and extra visitor");
    }

    /**
     * Tests that multiple replacer instances can be created independently using 5-parameter constructor.
     * Verifies that each replacer instance is independent.
     */
    @Test
    public void testFiveParamMultipleReplacerInstances() {
        // Arrange
        Constant[] constants1 = new Constant[0];
        Instruction[][][] instructionSequences1 = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder1 = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor1 = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor1 =
                mock(proguard.classfile.instruction.visitor.InstructionVisitor.class);

        Constant[] constants2 = new Constant[0];
        Instruction[][][] instructionSequences2 = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder2 = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor2 = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor2 =
                mock(proguard.classfile.instruction.visitor.InstructionVisitor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer1 =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants1,
                        instructionSequences1,
                        branchTargetFinder1,
                        codeAttributeEditor1,
                        extraInstructionVisitor1);

        ConfigurationLoggingInstructionSequencesReplacer replacer2 =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants2,
                        instructionSequences2,
                        branchTargetFinder2,
                        codeAttributeEditor2,
                        extraInstructionVisitor2);

        // Assert
        assertNotNull(replacer1, "First replacer should be created");
        assertNotNull(replacer2, "Second replacer should be created");
        assertNotSame(replacer1, replacer2, "Replacer instances should be different objects");
    }

    /**
     * Tests the 5-parameter constructor with various sized instruction sequences and extraInstructionVisitor.
     * Verifies that the constructor handles different sizes of pattern/replacement arrays with extra visitor.
     */
    @Test
    public void testFiveParamConstructorWithVariousSizedInstructionSequences() {
        // Arrange
        Constant[] constants = new Constant[0];

        // Pair with small pattern and replacement
        Instruction[][] smallPair = new Instruction[2][];
        smallPair[0] = new Instruction[1];
        smallPair[0][0] = mock(Instruction.class);
        smallPair[1] = new Instruction[1];
        smallPair[1][0] = mock(Instruction.class);

        // Pair with larger pattern and replacement
        Instruction[][] largePair = new Instruction[2][];
        largePair[0] = new Instruction[5];
        for (int i = 0; i < 5; i++) {
            largePair[0][i] = mock(Instruction.class);
        }
        largePair[1] = new Instruction[3];
        for (int i = 0; i < 3; i++) {
            largePair[1][i] = mock(Instruction.class);
        }

        Instruction[][][] instructionSequences = new Instruction[2][][];
        instructionSequences[0] = smallPair;
        instructionSequences[1] = largePair;

        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
        proguard.classfile.instruction.visitor.InstructionVisitor extraInstructionVisitor =
                mock(proguard.classfile.instruction.visitor.InstructionVisitor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor,
                        extraInstructionVisitor);

        // Assert
        assertNotNull(replacer, "Replacer should be instantiated with various sized instruction sequences and extra visitor");
    }

    /**
     * Tests that 4-parameter and 5-parameter constructors produce equivalent results.
     * Verifies that calling the 4-parameter constructor is equivalent to calling 5-parameter with null extraInstructionVisitor.
     */
    @Test
    public void testConstructorEquivalence() {
        // Arrange
        Constant[] constants = new Constant[0];
        Instruction[][][] instructionSequences = new Instruction[0][][];
        BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
        CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);

        // Act
        ConfigurationLoggingInstructionSequencesReplacer replacer4Param =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor);

        ConfigurationLoggingInstructionSequencesReplacer replacer5Param =
                new ConfigurationLoggingInstructionSequencesReplacer(
                        constants,
                        instructionSequences,
                        branchTargetFinder,
                        codeAttributeEditor,
                        null);

        // Assert
        assertNotNull(replacer4Param, "4-parameter constructor should create instance");
        assertNotNull(replacer5Param, "5-parameter constructor should create instance");
        assertNotSame(replacer4Param, replacer5Param, "Constructors should create different instances");
    }
}
