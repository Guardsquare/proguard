package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SideEffectInstructionChecker#visitSimpleInstruction}.
 *
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;Lproguard/classfile/Method;Lproguard/classfile/attribute/CodeAttribute;ILproguard/classfile/instruction/SimpleInstruction;)V
 *
 * The visitSimpleInstruction method checks the opcode of a SimpleInstruction and sets the
 * hasSideEffects field based on:
 * - The opcode type
 * - Three boolean flags: includeReturnInstructions, includeArrayStoreInstructions, includeBuiltInExceptions
 *
 * Note: This method modifies internal state (hasSideEffects field) which is then read by the hasSideEffects() method.
 * We test this by calling visitSimpleInstruction and then checking the result via hasSideEffects().
 */
public class SideEffectInstructionCheckerClaude_visitSimpleInstructionTest {

    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    // =========================================================================
    // Division and Remainder Operations - includeBuiltInExceptions
    // =========================================================================

    /**
     * Tests IDIV instruction with includeBuiltInExceptions enabled.
     * Division can throw ArithmeticException (divide by zero).
     */
    @Test
    public void testVisitSimpleInstruction_idivWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IDIV);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify by checking hasSideEffects
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IDIV should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests IDIV instruction with includeBuiltInExceptions disabled.
     */
    @Test
    public void testVisitSimpleInstruction_idivWithoutBuiltInExceptions_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IDIV);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IDIV should set hasSideEffects to false when includeBuiltInExceptions is false");
    }

    /**
     * Tests LDIV instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_ldivWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LDIV);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "LDIV should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests IREM instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_iremWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IREM);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IREM should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests LREM instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_lremWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LREM);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "LREM should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests FDIV instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_fdivWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_FDIV);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "FDIV should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests FREM instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_fremWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_FREM);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "FREM should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests DDIV instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_ddivWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DDIV);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "DDIV should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests DREM instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_dremWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DREM);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "DREM should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    // =========================================================================
    // Array Load Operations - includeBuiltInExceptions
    // =========================================================================

    /**
     * Tests IALOAD instruction with includeBuiltInExceptions enabled.
     * Can throw NullPointerException and ArrayIndexOutOfBoundsException.
     */
    @Test
    public void testVisitSimpleInstruction_ialoadWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IALOAD should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests IALOAD instruction with includeBuiltInExceptions disabled.
     */
    @Test
    public void testVisitSimpleInstruction_ialoadWithoutBuiltInExceptions_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IALOAD should set hasSideEffects to false when includeBuiltInExceptions is false");
    }

    /**
     * Tests LALOAD instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_laloadWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "LALOAD should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests FALOAD instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_faloadWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_FALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "FALOAD should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests DALOAD instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_daloadWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "DALOAD should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests AALOAD instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_aaloadWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_AALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "AALOAD should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests BALOAD instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_baloadWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_BALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "BALOAD should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests CALOAD instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_caloadWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_CALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "CALOAD should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests SALOAD instruction with includeBuiltInExceptions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_saloadWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_SALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "SALOAD should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    // =========================================================================
    // NEWARRAY and ARRAYLENGTH - includeBuiltInExceptions
    // =========================================================================

    /**
     * Tests NEWARRAY instruction with includeBuiltInExceptions enabled.
     * Can throw NegativeArraySizeException.
     */
    @Test
    public void testVisitSimpleInstruction_newarrayWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NEWARRAY, 4);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "NEWARRAY should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests NEWARRAY instruction with includeBuiltInExceptions disabled.
     */
    @Test
    public void testVisitSimpleInstruction_newarrayWithoutBuiltInExceptions_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NEWARRAY, 4);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "NEWARRAY should set hasSideEffects to false when includeBuiltInExceptions is false");
    }

    /**
     * Tests ARRAYLENGTH instruction with includeBuiltInExceptions enabled.
     * Can throw NullPointerException.
     */
    @Test
    public void testVisitSimpleInstruction_arraylengthWithBuiltInExceptions_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ARRAYLENGTH);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ARRAYLENGTH should set hasSideEffects to true when includeBuiltInExceptions is true");
    }

    /**
     * Tests ARRAYLENGTH instruction with includeBuiltInExceptions disabled.
     */
    @Test
    public void testVisitSimpleInstruction_arraylengthWithoutBuiltInExceptions_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ARRAYLENGTH);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ARRAYLENGTH should set hasSideEffects to false when includeBuiltInExceptions is false");
    }

    // =========================================================================
    // Array Store Operations - includeArrayStoreInstructions
    // =========================================================================

    /**
     * Tests IASTORE instruction with includeArrayStoreInstructions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_iastoreWithArrayStoreEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IASTORE should set hasSideEffects to true when includeArrayStoreInstructions is true");
    }

    /**
     * Tests IASTORE instruction with includeArrayStoreInstructions disabled.
     */
    @Test
    public void testVisitSimpleInstruction_iastoreWithArrayStoreDisabled_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IASTORE should set hasSideEffects to false when includeArrayStoreInstructions is false");
    }

    /**
     * Tests LASTORE instruction with includeArrayStoreInstructions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_lastoreWithArrayStoreEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "LASTORE should set hasSideEffects to true when includeArrayStoreInstructions is true");
    }

    /**
     * Tests FASTORE instruction with includeArrayStoreInstructions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_fastoreWithArrayStoreEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_FASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "FASTORE should set hasSideEffects to true when includeArrayStoreInstructions is true");
    }

    /**
     * Tests DASTORE instruction with includeArrayStoreInstructions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_dastoreWithArrayStoreEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "DASTORE should set hasSideEffects to true when includeArrayStoreInstructions is true");
    }

    /**
     * Tests AASTORE instruction with includeArrayStoreInstructions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_aastoreWithArrayStoreEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_AASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "AASTORE should set hasSideEffects to true when includeArrayStoreInstructions is true");
    }

    /**
     * Tests BASTORE instruction with includeArrayStoreInstructions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_bastoreWithArrayStoreEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_BASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "BASTORE should set hasSideEffects to true when includeArrayStoreInstructions is true");
    }

    /**
     * Tests CASTORE instruction with includeArrayStoreInstructions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_castoreWithArrayStoreEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_CASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "CASTORE should set hasSideEffects to true when includeArrayStoreInstructions is true");
    }

    /**
     * Tests SASTORE instruction with includeArrayStoreInstructions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_sastoreWithArrayStoreEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_SASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "SASTORE should set hasSideEffects to true when includeArrayStoreInstructions is true");
    }

    // =========================================================================
    // Always Side Effect Operations
    // =========================================================================

    /**
     * Tests ATHROW instruction - always has side effects regardless of flags.
     */
    @Test
    public void testVisitSimpleInstruction_athrow_alwaysSetsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ATHROW);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ATHROW should always set hasSideEffects to true");
    }

    /**
     * Tests MONITORENTER instruction - always has side effects.
     */
    @Test
    public void testVisitSimpleInstruction_monitorenter_alwaysSetsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_MONITORENTER);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "MONITORENTER should always set hasSideEffects to true");
    }

    /**
     * Tests MONITOREXIT instruction - always has side effects.
     */
    @Test
    public void testVisitSimpleInstruction_monitorexit_alwaysSetsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_MONITOREXIT);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "MONITOREXIT should always set hasSideEffects to true");
    }

    /**
     * Tests that ATHROW has side effects even with all flags enabled.
     */
    @Test
    public void testVisitSimpleInstruction_athrowWithAllFlags_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ATHROW);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ATHROW should set hasSideEffects to true with all flags enabled");
    }

    // =========================================================================
    // Return Instructions - includeReturnInstructions
    // =========================================================================

    /**
     * Tests IRETURN instruction with includeReturnInstructions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_ireturnWithReturnEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IRETURN);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IRETURN should set hasSideEffects to true when includeReturnInstructions is true");
    }

    /**
     * Tests IRETURN instruction with includeReturnInstructions disabled.
     */
    @Test
    public void testVisitSimpleInstruction_ireturnWithReturnDisabled_setsHasSideEffectsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IRETURN);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IRETURN should set hasSideEffects to false when includeReturnInstructions is false");
    }

    /**
     * Tests LRETURN instruction with includeReturnInstructions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_lreturnWithReturnEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LRETURN);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "LRETURN should set hasSideEffects to true when includeReturnInstructions is true");
    }

    /**
     * Tests FRETURN instruction with includeReturnInstructions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_freturnWithReturnEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_FRETURN);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "FRETURN should set hasSideEffects to true when includeReturnInstructions is true");
    }

    /**
     * Tests DRETURN instruction with includeReturnInstructions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_dreturnWithReturnEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DRETURN);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "DRETURN should set hasSideEffects to true when includeReturnInstructions is true");
    }

    /**
     * Tests ARETURN instruction with includeReturnInstructions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_areturnWithReturnEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ARETURN);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ARETURN should set hasSideEffects to true when includeReturnInstructions is true");
    }

    /**
     * Tests RETURN instruction with includeReturnInstructions enabled.
     */
    @Test
    public void testVisitSimpleInstruction_returnWithReturnEnabled_setsHasSideEffectsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_RETURN);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "RETURN should set hasSideEffects to true when includeReturnInstructions is true");
    }

    // =========================================================================
    // Neutral Operations - No Side Effects
    // =========================================================================

    /**
     * Tests NOP instruction - should never have side effects.
     */
    @Test
    public void testVisitSimpleInstruction_nop_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NOP);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "NOP should never set hasSideEffects even with all flags enabled");
    }

    /**
     * Tests IADD instruction - arithmetic with no side effects.
     */
    @Test
    public void testVisitSimpleInstruction_iadd_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IADD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IADD should never set hasSideEffects");
    }

    /**
     * Tests IMUL instruction - multiplication with no side effects.
     */
    @Test
    public void testVisitSimpleInstruction_imul_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IMUL);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "IMUL should never set hasSideEffects");
    }

    /**
     * Tests ISUB instruction - subtraction with no side effects.
     */
    @Test
    public void testVisitSimpleInstruction_isub_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ISUB);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ISUB should never set hasSideEffects");
    }

    /**
     * Tests ICONST_0 instruction - constant push with no side effects.
     */
    @Test
    public void testVisitSimpleInstruction_iconst0_neverSetsHasSideEffects() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ICONST_0);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction),
                "ICONST_0 should never set hasSideEffects");
    }

    // =========================================================================
    // Integration Tests - Multiple Calls and State Management
    // =========================================================================

    /**
     * Tests that visitSimpleInstruction can be called multiple times with state updating correctly.
     */
    @Test
    public void testVisitSimpleInstruction_multipleCalls_updatesStateCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - call with different instructions and verify state
        SimpleInstruction idiv = new SimpleInstruction(Instruction.OP_IDIV);
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, idiv);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, idiv),
                "IDIV should set hasSideEffects to true");

        SimpleInstruction iadd = new SimpleInstruction(Instruction.OP_IADD);
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, iadd);
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0, iadd),
                "IADD should set hasSideEffects to false");

        SimpleInstruction athrow = new SimpleInstruction(Instruction.OP_ATHROW);
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, athrow);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, athrow),
                "ATHROW should set hasSideEffects to true");
    }

    /**
     * Tests visitSimpleInstruction with different offset values.
     */
    @Test
    public void testVisitSimpleInstruction_withVariousOffsets_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ATHROW);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - offset shouldn't affect the behavior
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction));

        checker.visitSimpleInstruction(clazz, method, codeAttribute, 100, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 100, instruction));

        checker.visitSimpleInstruction(clazz, method, codeAttribute, -1, instruction);
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, -1, instruction));
    }

    /**
     * Tests that visitSimpleInstruction doesn't throw exceptions for any valid opcode.
     */
    @Test
    public void testVisitSimpleInstruction_withAllFlags_doesNotThrow() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test various opcodes
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_NOP)));
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_ATHROW)));
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_IDIV)));
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_IASTORE)));
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_IRETURN)));
    }

    /**
     * Tests that the instruction object is not modified by visitSimpleInstruction.
     */
    @Test
    public void testVisitSimpleInstruction_doesNotModifyInstruction() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ATHROW);
        byte originalOpcode = instruction.opcode;
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertEquals(originalOpcode, instruction.opcode,
                "visitSimpleInstruction should not modify the instruction opcode");
    }

    /**
     * Tests visitSimpleInstruction with ProgramClass instance.
     */
    @Test
    public void testVisitSimpleInstruction_withProgramClass_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        Clazz realClazz = new ProgramClass();
        Method method = mock(ProgramMethod.class);
        CodeAttribute codeAttribute = mock(CodeAttribute.class);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ATHROW);

        // Act & Assert
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(realClazz, method, codeAttribute, 0, instruction));
        assertTrue(checker.hasSideEffects(realClazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that all three flags can work together correctly.
     */
    @Test
    public void testVisitSimpleInstruction_allFlagsEnabled_behavesCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - test instructions from each category
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_IRETURN)), "IRETURN with return flag");
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_IASTORE)), "IASTORE with array store flag");
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_IDIV)), "IDIV with built-in exceptions flag");
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_ATHROW)), "ATHROW always has side effects");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_NOP)), "NOP never has side effects");
    }

    /**
     * Tests that all three flags can be disabled together correctly.
     */
    @Test
    public void testVisitSimpleInstruction_allFlagsDisabled_behavesCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_IRETURN)), "IRETURN without return flag");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_IASTORE)), "IASTORE without array store flag");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_IDIV)), "IDIV without built-in exceptions flag");
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0,
                new SimpleInstruction(Instruction.OP_ATHROW)), "ATHROW always has side effects");
    }
}
