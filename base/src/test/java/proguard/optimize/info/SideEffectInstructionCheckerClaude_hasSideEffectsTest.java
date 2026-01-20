package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SideEffectInstructionChecker#hasSideEffects}.
 *
 * Tests the method with signature:
 * (Lproguard/classfile/Clazz;Lproguard/classfile/Method;Lproguard/classfile/attribute/CodeAttribute;ILproguard/classfile/instruction/Instruction;)Z
 *
 * The hasSideEffects method determines whether an instruction has side effects based on:
 * - The instruction type and opcode
 * - Three boolean flags: includeReturnInstructions, includeArrayStoreInstructions, includeBuiltInExceptions
 */
public class SideEffectInstructionCheckerClaude_hasSideEffectsTest {

    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    // =========================================================================
    // SimpleInstruction Tests - Division/Remainder Operations
    // =========================================================================

    /**
     * Tests that division instructions have side effects when includeBuiltInExceptions is true.
     * Division can throw ArithmeticException (divide by zero).
     */
    @Test
    public void testHasSideEffects_idivWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IDIV);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "IDIV should have side effects when includeBuiltInExceptions is true");
    }

    /**
     * Tests that division instructions do not have side effects when includeBuiltInExceptions is false.
     */
    @Test
    public void testHasSideEffects_idivWithoutBuiltInExceptions_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IDIV);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "IDIV should not have side effects when includeBuiltInExceptions is false");
    }

    /**
     * Tests LDIV instruction with built-in exceptions enabled.
     */
    @Test
    public void testHasSideEffects_ldivWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LDIV);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "LDIV should have side effects when includeBuiltInExceptions is true");
    }

    /**
     * Tests remainder operations (IREM, LREM) with built-in exceptions.
     */
    @Test
    public void testHasSideEffects_iremWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IREM);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "IREM should have side effects when includeBuiltInExceptions is true");
    }

    /**
     * Tests LREM instruction.
     */
    @Test
    public void testHasSideEffects_lremWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LREM);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "LREM should have side effects when includeBuiltInExceptions is true");
    }

    /**
     * Tests floating-point division and remainder operations.
     * These can also trigger exceptions with includeBuiltInExceptions.
     */
    @Test
    public void testHasSideEffects_fdivWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_FDIV);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "FDIV should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_fremWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_FREM);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "FREM should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_ddivWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DDIV);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "DDIV should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_dremWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DREM);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "DREM should have side effects when includeBuiltInExceptions is true");
    }

    // =========================================================================
    // SimpleInstruction Tests - Array Load Operations
    // =========================================================================

    /**
     * Tests array load instructions with built-in exceptions.
     * Array loads can throw NullPointerException and ArrayIndexOutOfBoundsException.
     */
    @Test
    public void testHasSideEffects_ialoadWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "IALOAD should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_ialoadWithoutBuiltInExceptions_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "IALOAD should not have side effects when includeBuiltInExceptions is false");
    }

    @Test
    public void testHasSideEffects_laloadWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "LALOAD should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_faloadWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_FALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "FALOAD should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_daloadWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "DALOAD should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_aaloadWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_AALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "AALOAD should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_baloadWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_BALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "BALOAD should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_caloadWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_CALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "CALOAD should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_saloadWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_SALOAD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "SALOAD should have side effects when includeBuiltInExceptions is true");
    }

    // =========================================================================
    // SimpleInstruction Tests - Array Store Operations
    // =========================================================================

    /**
     * Tests array store instructions with includeArrayStoreInstructions flag.
     */
    @Test
    public void testHasSideEffects_iastoreWithArrayStoreEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "IASTORE should have side effects when includeArrayStoreInstructions is true");
    }

    @Test
    public void testHasSideEffects_iastoreWithArrayStoreDisabled_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "IASTORE should not have side effects when includeArrayStoreInstructions is false");
    }

    @Test
    public void testHasSideEffects_lastoreWithArrayStoreEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "LASTORE should have side effects when includeArrayStoreInstructions is true");
    }

    @Test
    public void testHasSideEffects_fastoreWithArrayStoreEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_FASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "FASTORE should have side effects when includeArrayStoreInstructions is true");
    }

    @Test
    public void testHasSideEffects_dastoreWithArrayStoreEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "DASTORE should have side effects when includeArrayStoreInstructions is true");
    }

    @Test
    public void testHasSideEffects_aastoreWithArrayStoreEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_AASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "AASTORE should have side effects when includeArrayStoreInstructions is true");
    }

    @Test
    public void testHasSideEffects_bastoreWithArrayStoreEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_BASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "BASTORE should have side effects when includeArrayStoreInstructions is true");
    }

    @Test
    public void testHasSideEffects_castoreWithArrayStoreEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_CASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "CASTORE should have side effects when includeArrayStoreInstructions is true");
    }

    @Test
    public void testHasSideEffects_sastoreWithArrayStoreEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, true, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_SASTORE);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "SASTORE should have side effects when includeArrayStoreInstructions is true");
    }

    // =========================================================================
    // SimpleInstruction Tests - Other Operations
    // =========================================================================

    /**
     * Tests NEWARRAY instruction with built-in exceptions.
     * Can throw NegativeArraySizeException.
     */
    @Test
    public void testHasSideEffects_newarrayWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NEWARRAY, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "NEWARRAY should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_newarrayWithoutBuiltInExceptions_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NEWARRAY, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "NEWARRAY should not have side effects when includeBuiltInExceptions is false");
    }

    /**
     * Tests ARRAYLENGTH instruction with built-in exceptions.
     * Can throw NullPointerException.
     */
    @Test
    public void testHasSideEffects_arraylengthWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ARRAYLENGTH);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "ARRAYLENGTH should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_arraylengthWithoutBuiltInExceptions_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ARRAYLENGTH);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "ARRAYLENGTH should not have side effects when includeBuiltInExceptions is false");
    }

    /**
     * Tests ATHROW instruction - always has side effects.
     */
    @Test
    public void testHasSideEffects_athrow_alwaysReturnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ATHROW);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "ATHROW should always have side effects");
    }

    /**
     * Tests MONITORENTER instruction - always has side effects.
     */
    @Test
    public void testHasSideEffects_monitorenter_alwaysReturnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_MONITORENTER);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "MONITORENTER should always have side effects");
    }

    /**
     * Tests MONITOREXIT instruction - always has side effects.
     */
    @Test
    public void testHasSideEffects_monitorexit_alwaysReturnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_MONITOREXIT);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "MONITOREXIT should always have side effects");
    }

    // =========================================================================
    // SimpleInstruction Tests - Return Instructions
    // =========================================================================

    /**
     * Tests IRETURN instruction with includeReturnInstructions enabled.
     */
    @Test
    public void testHasSideEffects_ireturnWithReturnEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IRETURN);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "IRETURN should have side effects when includeReturnInstructions is true");
    }

    @Test
    public void testHasSideEffects_ireturnWithReturnDisabled_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IRETURN);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "IRETURN should not have side effects when includeReturnInstructions is false");
    }

    @Test
    public void testHasSideEffects_lreturnWithReturnEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LRETURN);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "LRETURN should have side effects when includeReturnInstructions is true");
    }

    @Test
    public void testHasSideEffects_freturnWithReturnEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_FRETURN);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "FRETURN should have side effects when includeReturnInstructions is true");
    }

    @Test
    public void testHasSideEffects_dreturnWithReturnEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DRETURN);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "DRETURN should have side effects when includeReturnInstructions is true");
    }

    @Test
    public void testHasSideEffects_areturnWithReturnEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ARETURN);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "ARETURN should have side effects when includeReturnInstructions is true");
    }

    @Test
    public void testHasSideEffects_returnWithReturnEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_RETURN);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "RETURN should have side effects when includeReturnInstructions is true");
    }

    // =========================================================================
    // SimpleInstruction Tests - Neutral Instructions
    // =========================================================================

    /**
     * Tests that NOP instruction has no side effects regardless of flags.
     */
    @Test
    public void testHasSideEffects_nop_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NOP);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "NOP should not have side effects");
    }

    /**
     * Tests that IADD instruction has no side effects.
     */
    @Test
    public void testHasSideEffects_iadd_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IADD);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "IADD should not have side effects");
    }

    /**
     * Tests that IMUL instruction (multiplication) has no side effects.
     */
    @Test
    public void testHasSideEffects_imul_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IMUL);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "IMUL should not have side effects");
    }

    // =========================================================================
    // VariableInstruction Tests
    // =========================================================================

    /**
     * Tests RET instruction with includeReturnInstructions enabled.
     */
    @Test
    public void testHasSideEffects_retWithReturnEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_RET, 0);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "RET should have side effects when includeReturnInstructions is true");
    }

    @Test
    public void testHasSideEffects_retWithReturnDisabled_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_RET, 0);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "RET should not have side effects when includeReturnInstructions is false");
    }

    /**
     * Tests ILOAD instruction - should not have side effects.
     */
    @Test
    public void testHasSideEffects_iload_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_ILOAD, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "ILOAD should not have side effects");
    }

    /**
     * Tests ISTORE instruction - should not have side effects.
     */
    @Test
    public void testHasSideEffects_istore_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        VariableInstruction instruction = new VariableInstruction(Instruction.OP_ISTORE, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "ISTORE should not have side effects");
    }

    // =========================================================================
    // BranchInstruction Tests
    // =========================================================================

    /**
     * Tests JSR instruction with includeReturnInstructions enabled.
     */
    @Test
    public void testHasSideEffects_jsrWithReturnEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "JSR should have side effects when includeReturnInstructions is true");
    }

    @Test
    public void testHasSideEffects_jsrWithReturnDisabled_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "JSR should not have side effects when includeReturnInstructions is false");
    }

    /**
     * Tests JSR_W instruction with includeReturnInstructions enabled.
     */
    @Test
    public void testHasSideEffects_jsrWWithReturnEnabled_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, false, false);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR_W, 1000);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "JSR_W should have side effects when includeReturnInstructions is true");
    }

    @Test
    public void testHasSideEffects_jsrWWithReturnDisabled_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_JSR_W, 1000);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "JSR_W should not have side effects when includeReturnInstructions is false");
    }

    /**
     * Tests GOTO instruction - should not have side effects.
     */
    @Test
    public void testHasSideEffects_goto_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_GOTO, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "GOTO should not have side effects");
    }

    /**
     * Tests IFEQ instruction - should not have side effects.
     */
    @Test
    public void testHasSideEffects_ifeq_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        BranchInstruction instruction = new BranchInstruction(Instruction.OP_IFEQ, 10);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "IFEQ should not have side effects");
    }

    // =========================================================================
    // ConstantInstruction Tests - ANEWARRAY and MULTIANEWARRAY
    // =========================================================================

    /**
     * Tests ANEWARRAY instruction with built-in exceptions.
     * Can throw NegativeArraySizeException.
     */
    @Test
    public void testHasSideEffects_anewarrayWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "ANEWARRAY should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_anewarrayWithoutBuiltInExceptions_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "ANEWARRAY should not have side effects when includeBuiltInExceptions is false");
    }

    /**
     * Tests MULTIANEWARRAY instruction with built-in exceptions.
     */
    @Test
    public void testHasSideEffects_multianewarrayWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 1, 2);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "MULTIANEWARRAY should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_multianewarrayWithoutBuiltInExceptions_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 1, 2);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "MULTIANEWARRAY should not have side effects when includeBuiltInExceptions is false");
    }

    /**
     * Tests CHECKCAST instruction with built-in exceptions.
     * Can throw ClassCastException.
     */
    @Test
    public void testHasSideEffects_checkcastWithBuiltInExceptions_returnsTrue() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(result, "CHECKCAST should have side effects when includeBuiltInExceptions is true");
    }

    @Test
    public void testHasSideEffects_checkcastWithoutBuiltInExceptions_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "CHECKCAST should not have side effects when includeBuiltInExceptions is false");
    }

    // =========================================================================
    // ConstantInstruction Tests - LDC Instructions (neutral)
    // =========================================================================

    /**
     * Tests LDC instruction - should not have side effects.
     */
    @Test
    public void testHasSideEffects_ldc_returnsFalse() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act
        boolean result = checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(result, "LDC should not have side effects");
    }

    // =========================================================================
    // Integration Tests - Multiple Flags
    // =========================================================================

    /**
     * Tests that all flags can be enabled simultaneously.
     */
    @Test
    public void testHasSideEffects_allFlagsEnabled_returnsCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0,
            new SimpleInstruction(Instruction.OP_IRETURN)), "IRETURN with all flags");
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0,
            new SimpleInstruction(Instruction.OP_IASTORE)), "IASTORE with all flags");
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0,
            new SimpleInstruction(Instruction.OP_IDIV)), "IDIV with all flags");
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0,
            new SimpleInstruction(Instruction.OP_ATHROW)), "ATHROW with all flags");
    }

    /**
     * Tests that all flags can be disabled simultaneously.
     */
    @Test
    public void testHasSideEffects_allFlagsDisabled_returnsCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
            new SimpleInstruction(Instruction.OP_IRETURN)), "IRETURN with no flags");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
            new SimpleInstruction(Instruction.OP_IASTORE)), "IASTORE with no flags");
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
            new SimpleInstruction(Instruction.OP_IDIV)), "IDIV with no flags");
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0,
            new SimpleInstruction(Instruction.OP_ATHROW)), "ATHROW always has side effects");
    }

    /**
     * Tests that the method can be called multiple times with different instructions.
     */
    @Test
    public void testHasSideEffects_multipleCallsSameChecker_workCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, true);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - multiple calls should work independently
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0,
            new SimpleInstruction(Instruction.OP_IDIV)));
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
            new SimpleInstruction(Instruction.OP_IADD)));
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0,
            new SimpleInstruction(Instruction.OP_ATHROW)));
        assertFalse(checker.hasSideEffects(clazz, method, codeAttribute, 0,
            new SimpleInstruction(Instruction.OP_NOP)));
    }

    /**
     * Tests hasSideEffects with different offset values.
     */
    @Test
    public void testHasSideEffects_differentOffsets_worksCorrectly() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(false, false, false);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ATHROW);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        // Act & Assert - offset shouldn't affect the result for simple instructions
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction));
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 10, instruction));
        assertTrue(checker.hasSideEffects(clazz, method, codeAttribute, 100, instruction));
    }

    /**
     * Tests that hasSideEffects doesn't modify the instruction.
     */
    @Test
    public void testHasSideEffects_doesNotModifyInstruction() {
        // Arrange
        SideEffectInstructionChecker checker = new SideEffectInstructionChecker(true, true, true);
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ATHROW);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);

        byte originalOpcode = instruction.opcode;

        // Act
        checker.hasSideEffects(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertEquals(originalOpcode, instruction.opcode, "Instruction should not be modified");
    }
}
