package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ExceptionInstructionChecker#visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)}.
 *
 * The visitSimpleInstruction method checks if a SimpleInstruction may throw exceptions.
 * It sets an internal mayThrowExceptions field to true for instructions that can throw exceptions:
 * - Division operations (IDIV, LDIV, IREM, LREM)
 * - Array load operations (IALOAD, LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD, SALOAD)
 * - Array store operations (IASTORE, LASTORE, FASTORE, DASTORE, AASTORE, BASTORE, CASTORE, SASTORE)
 * - Array creation (NEWARRAY)
 * - Array length (ARRAYLENGTH)
 * - Throw instruction (ATHROW)
 * - Monitor enter (MONITORENTER)
 *
 * Note: This test uses reflection to access the private mayThrowExceptions field because:
 * 1. The visitSimpleInstruction method has no return value
 * 2. The method only modifies the private mayThrowExceptions field
 * 3. There is no public getter or observable side effect to verify the method's behavior
 * 4. The current implementation of mayThrowExceptions(Clazz, Method, CodeAttribute, int, Instruction)
 *    does not actually invoke the visitor methods, so the field cannot be tested indirectly
 * Therefore, reflection is the only way to test this method's correctness.
 */
public class ExceptionInstructionCheckerClaude_visitSimpleInstructionTest {

    private ExceptionInstructionChecker checker;
    private ProgramClass clazz;
    private ProgramMethod method;
    private CodeAttribute codeAttribute;
    private Field mayThrowExceptionsField;

    @BeforeEach
    public void setUp() throws Exception {
        checker = new ExceptionInstructionChecker();
        clazz = createProgramClassWithConstantPool();
        method = createMethodWithDescriptor(clazz, "testMethod", "()V");
        codeAttribute = new CodeAttribute();

        // Use reflection to access the private mayThrowExceptions field
        // This is necessary because the field is private and has no public accessor
        mayThrowExceptionsField = ExceptionInstructionChecker.class.getDeclaredField("mayThrowExceptions");
        mayThrowExceptionsField.setAccessible(true);
    }

    /**
     * Helper method to get the current value of mayThrowExceptions field using reflection.
     */
    private boolean getMayThrowExceptions() throws Exception {
        return mayThrowExceptionsField.getBoolean(checker);
    }

    /**
     * Helper method to reset the mayThrowExceptions field to false.
     */
    private void resetMayThrowExceptions() throws Exception {
        mayThrowExceptionsField.setBoolean(checker, false);
    }

    // Tests for division operations that throw ArithmeticException

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_IDIV.
     * Integer division can throw ArithmeticException (divide by zero).
     */
    @Test
    public void testVisitSimpleInstruction_withOpIdiv_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IDIV);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_IDIV should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_LDIV.
     * Long division can throw ArithmeticException (divide by zero).
     */
    @Test
    public void testVisitSimpleInstruction_withOpLdiv_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LDIV);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_LDIV should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_IREM.
     * Integer remainder can throw ArithmeticException (divide by zero).
     */
    @Test
    public void testVisitSimpleInstruction_withOpIrem_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IREM);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_IREM should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_LREM.
     * Long remainder can throw ArithmeticException (divide by zero).
     */
    @Test
    public void testVisitSimpleInstruction_withOpLrem_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LREM);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_LREM should set mayThrowExceptions to true");
    }

    // Tests for array load operations that throw NullPointerException and ArrayIndexOutOfBoundsException

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_IALOAD.
     * Integer array load can throw NullPointerException and ArrayIndexOutOfBoundsException.
     */
    @Test
    public void testVisitSimpleInstruction_withOpIaload_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IALOAD);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_IALOAD should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_LALOAD.
     */
    @Test
    public void testVisitSimpleInstruction_withOpLaload_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LALOAD);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_LALOAD should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_FALOAD.
     */
    @Test
    public void testVisitSimpleInstruction_withOpFaload_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_FALOAD);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_FALOAD should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_DALOAD.
     */
    @Test
    public void testVisitSimpleInstruction_withOpDaload_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DALOAD);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_DALOAD should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_AALOAD.
     */
    @Test
    public void testVisitSimpleInstruction_withOpAaload_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_AALOAD);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_AALOAD should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_BALOAD.
     */
    @Test
    public void testVisitSimpleInstruction_withOpBaload_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_BALOAD);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_BALOAD should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_CALOAD.
     */
    @Test
    public void testVisitSimpleInstruction_withOpCaload_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_CALOAD);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_CALOAD should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_SALOAD.
     */
    @Test
    public void testVisitSimpleInstruction_withOpSaload_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_SALOAD);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_SALOAD should set mayThrowExceptions to true");
    }

    // Tests for array store operations

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_IASTORE.
     * Integer array store can throw NullPointerException and ArrayIndexOutOfBoundsException.
     */
    @Test
    public void testVisitSimpleInstruction_withOpIastore_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IASTORE);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_IASTORE should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_LASTORE.
     */
    @Test
    public void testVisitSimpleInstruction_withOpLastore_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_LASTORE);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_LASTORE should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_FASTORE.
     */
    @Test
    public void testVisitSimpleInstruction_withOpFastore_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_FASTORE);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_FASTORE should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_DASTORE.
     */
    @Test
    public void testVisitSimpleInstruction_withOpDastore_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DASTORE);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_DASTORE should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_AASTORE.
     * Reference array store can also throw ArrayStoreException in addition to NPE and AIOOBE.
     */
    @Test
    public void testVisitSimpleInstruction_withOpAastore_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_AASTORE);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_AASTORE should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_BASTORE.
     */
    @Test
    public void testVisitSimpleInstruction_withOpBasore_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_BASTORE);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_BASTORE should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_CASTORE.
     */
    @Test
    public void testVisitSimpleInstruction_withOpCastore_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_CASTORE);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_CASTORE should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_SASTORE.
     */
    @Test
    public void testVisitSimpleInstruction_withOpSastore_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_SASTORE);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_SASTORE should set mayThrowExceptions to true");
    }

    // Tests for other exception-throwing operations

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_NEWARRAY.
     * Array creation can throw NegativeArraySizeException.
     */
    @Test
    public void testVisitSimpleInstruction_withOpNewarray_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NEWARRAY);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_NEWARRAY should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_ARRAYLENGTH.
     * Array length can throw NullPointerException.
     */
    @Test
    public void testVisitSimpleInstruction_withOpArraylength_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ARRAYLENGTH);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_ARRAYLENGTH should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_ATHROW.
     * This instruction explicitly throws an exception.
     */
    @Test
    public void testVisitSimpleInstruction_withOpAthrow_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ATHROW);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_ATHROW should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction sets mayThrowExceptions to true for OP_MONITORENTER.
     * Monitor enter can throw NullPointerException.
     */
    @Test
    public void testVisitSimpleInstruction_withOpMonitorenter_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_MONITORENTER);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_MONITORENTER should set mayThrowExceptions to true");
    }

    // Tests for non-exception-throwing operations

    /**
     * Tests that visitSimpleInstruction does NOT set mayThrowExceptions for OP_NOP.
     * NOP is a no-operation instruction that cannot throw exceptions.
     */
    @Test
    public void testVisitSimpleInstruction_withOpNop_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NOP);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_NOP should not set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction does NOT set mayThrowExceptions for OP_ICONST_0.
     */
    @Test
    public void testVisitSimpleInstruction_withOpIconst0_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ICONST_0);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_ICONST_0 should not set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction does NOT set mayThrowExceptions for OP_ACONST_NULL.
     */
    @Test
    public void testVisitSimpleInstruction_withOpAconstNull_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ACONST_NULL);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_ACONST_NULL should not set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction does NOT set mayThrowExceptions for OP_IADD.
     * Addition does not throw exceptions (overflow wraps around).
     */
    @Test
    public void testVisitSimpleInstruction_withOpIadd_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IADD);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_IADD should not set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction does NOT set mayThrowExceptions for OP_ISUB.
     */
    @Test
    public void testVisitSimpleInstruction_withOpIsub_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ISUB);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_ISUB should not set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction does NOT set mayThrowExceptions for OP_IMUL.
     */
    @Test
    public void testVisitSimpleInstruction_withOpImul_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IMUL);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_IMUL should not set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction does NOT set mayThrowExceptions for OP_RETURN.
     */
    @Test
    public void testVisitSimpleInstruction_withOpReturn_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_RETURN);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_RETURN should not set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction does NOT set mayThrowExceptions for OP_IRETURN.
     */
    @Test
    public void testVisitSimpleInstruction_withOpIreturn_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IRETURN);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_IRETURN should not set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction does NOT set mayThrowExceptions for OP_ARETURN.
     */
    @Test
    public void testVisitSimpleInstruction_withOpAreturn_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_ARETURN);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_ARETURN should not set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction does NOT set mayThrowExceptions for OP_POP.
     */
    @Test
    public void testVisitSimpleInstruction_withOpPop_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_POP);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_POP should not set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction does NOT set mayThrowExceptions for OP_DUP.
     */
    @Test
    public void testVisitSimpleInstruction_withOpDup_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_DUP);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_DUP should not set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction does NOT set mayThrowExceptions for OP_SWAP.
     */
    @Test
    public void testVisitSimpleInstruction_withOpSwap_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_SWAP);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_SWAP should not set mayThrowExceptions to true");
    }

    /**
     * Tests that visitSimpleInstruction does NOT set mayThrowExceptions for OP_MONITOREXIT.
     * According to the code comments, monitorexit is intentionally not marked as throwing exceptions
     * except for the deprecated asynchronous ThreadDeath.
     */
    @Test
    public void testVisitSimpleInstruction_withOpMonitorexit_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_MONITOREXIT);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_MONITOREXIT should not set mayThrowExceptions to true per implementation");
    }

    // Tests for edge cases and behavior verification

    /**
     * Tests that multiple calls to visitSimpleInstruction with exception-throwing opcodes
     * keep the field set to true.
     */
    @Test
    public void testVisitSimpleInstruction_multipleExceptionThrowingInstructions_maintainsTrue() throws Exception {
        // Arrange
        SimpleInstruction idiv = new SimpleInstruction(Instruction.OP_IDIV);
        SimpleInstruction athrow = new SimpleInstruction(Instruction.OP_ATHROW);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, idiv);
        assertTrue(getMayThrowExceptions(), "Should be true after first exception-throwing instruction");

        checker.visitSimpleInstruction(clazz, method, codeAttribute, 2, athrow);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "Should remain true after multiple exception-throwing instructions");
    }

    /**
     * Tests that calling visitSimpleInstruction with a non-exception-throwing instruction
     * after an exception-throwing one does not change the field from true.
     */
    @Test
    public void testVisitSimpleInstruction_nonExceptionInstructionAfterExceptionInstruction_maintainsTrue() throws Exception {
        // Arrange
        SimpleInstruction athrow = new SimpleInstruction(Instruction.OP_ATHROW);
        SimpleInstruction nop = new SimpleInstruction(Instruction.OP_NOP);
        resetMayThrowExceptions();

        // Act
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, athrow);
        assertTrue(getMayThrowExceptions(), "Should be true after exception-throwing instruction");

        checker.visitSimpleInstruction(clazz, method, codeAttribute, 2, nop);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "Should remain true even after non-exception-throwing instruction");
    }

    /**
     * Tests that visitSimpleInstruction works correctly with various offset values.
     * The offset should not affect the behavior.
     */
    @Test
    public void testVisitSimpleInstruction_withVariousOffsets_behavesConsistently() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_IDIV);

        // Test with offset 0
        resetMayThrowExceptions();
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);
        assertTrue(getMayThrowExceptions(), "Should work with offset 0");

        // Test with offset 100
        resetMayThrowExceptions();
        checker.visitSimpleInstruction(clazz, method, codeAttribute, 100, instruction);
        assertTrue(getMayThrowExceptions(), "Should work with offset 100");

        // Test with negative offset
        resetMayThrowExceptions();
        checker.visitSimpleInstruction(clazz, method, codeAttribute, -1, instruction);
        assertTrue(getMayThrowExceptions(), "Should work with negative offset");
    }

    /**
     * Tests that visitSimpleInstruction can be called with null parameters without throwing NPE
     * for non-exception-throwing instructions (the method doesn't access the parameters).
     */
    @Test
    public void testVisitSimpleInstruction_withNullParameters_doesNotThrowForNonExceptionInstruction() throws Exception {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NOP);
        resetMayThrowExceptions();

        // Act & Assert - should not throw NPE
        assertDoesNotThrow(() -> checker.visitSimpleInstruction(null, null, null, 0, instruction));
        assertFalse(getMayThrowExceptions(),
                "Should not set mayThrowExceptions even with null parameters");
    }

    /**
     * Tests that visitSimpleInstruction properly handles the switch statement for all
     * exception-throwing division and remainder operations.
     */
    @Test
    public void testVisitSimpleInstruction_allDivisionOperations_setMayThrowExceptions() throws Exception {
        // Test all division and remainder operations
        byte[] divisionOpcodes = {
            Instruction.OP_IDIV,
            Instruction.OP_LDIV,
            Instruction.OP_IREM,
            Instruction.OP_LREM
        };

        for (byte opcode : divisionOpcodes) {
            resetMayThrowExceptions();
            SimpleInstruction instruction = new SimpleInstruction(opcode);
            checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);
            assertTrue(getMayThrowExceptions(),
                    "Opcode " + opcode + " should set mayThrowExceptions");
        }
    }

    /**
     * Tests that visitSimpleInstruction properly handles all array load operations.
     */
    @Test
    public void testVisitSimpleInstruction_allArrayLoadOperations_setMayThrowExceptions() throws Exception {
        // Test all array load operations
        byte[] arrayLoadOpcodes = {
            Instruction.OP_IALOAD,
            Instruction.OP_LALOAD,
            Instruction.OP_FALOAD,
            Instruction.OP_DALOAD,
            Instruction.OP_AALOAD,
            Instruction.OP_BALOAD,
            Instruction.OP_CALOAD,
            Instruction.OP_SALOAD
        };

        for (byte opcode : arrayLoadOpcodes) {
            resetMayThrowExceptions();
            SimpleInstruction instruction = new SimpleInstruction(opcode);
            checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);
            assertTrue(getMayThrowExceptions(),
                    "Opcode " + opcode + " should set mayThrowExceptions");
        }
    }

    /**
     * Tests that visitSimpleInstruction properly handles all array store operations.
     */
    @Test
    public void testVisitSimpleInstruction_allArrayStoreOperations_setMayThrowExceptions() throws Exception {
        // Test all array store operations
        byte[] arrayStoreOpcodes = {
            Instruction.OP_IASTORE,
            Instruction.OP_LASTORE,
            Instruction.OP_FASTORE,
            Instruction.OP_DASTORE,
            Instruction.OP_AASTORE,
            Instruction.OP_BASTORE,
            Instruction.OP_CASTORE,
            Instruction.OP_SASTORE
        };

        for (byte opcode : arrayStoreOpcodes) {
            resetMayThrowExceptions();
            SimpleInstruction instruction = new SimpleInstruction(opcode);
            checker.visitSimpleInstruction(clazz, method, codeAttribute, 0, instruction);
            assertTrue(getMayThrowExceptions(),
                    "Opcode " + opcode + " should set mayThrowExceptions");
        }
    }

    // Helper methods

    /**
     * Creates a ProgramClass with a basic constant pool.
     */
    private ProgramClass createProgramClassWithConstantPool() {
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 1;

        // Create a constant pool with enough space
        Constant[] constantPool = new Constant[100];
        constantPool[0] = null; // Index 0 is always null
        programClass.constantPool = constantPool;
        programClass.u2constantPoolCount = 100;

        return programClass;
    }

    /**
     * Creates a ProgramMethod with a specific name and descriptor.
     */
    private ProgramMethod createMethodWithDescriptor(ProgramClass programClass, String methodName, String descriptor) {
        ProgramMethod method = new ProgramMethod();
        method.u2accessFlags = AccessConstants.PUBLIC;

        // Find next available constant pool index
        int nextIndex = findNextAvailableConstantPoolIndex(programClass);

        // Add method name to constant pool
        programClass.constantPool[nextIndex] = new Utf8Constant(methodName);
        method.u2nameIndex = nextIndex;

        // Add descriptor to constant pool
        programClass.constantPool[nextIndex + 1] = new Utf8Constant(descriptor);
        method.u2descriptorIndex = nextIndex + 1;

        return method;
    }

    /**
     * Finds the next available index in the constant pool.
     */
    private int findNextAvailableConstantPoolIndex(ProgramClass programClass) {
        for (int i = 1; i < programClass.constantPool.length; i++) {
            if (programClass.constantPool[i] == null) {
                return i;
            }
        }
        throw new IllegalStateException("Constant pool is full");
    }
}
