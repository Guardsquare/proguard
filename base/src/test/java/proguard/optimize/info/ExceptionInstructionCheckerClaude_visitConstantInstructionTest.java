package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ExceptionInstructionChecker#visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)}.
 *
 * The visitConstantInstruction method checks if a ConstantInstruction may throw exceptions.
 * It sets an internal mayThrowExceptions field to true for instructions that can throw exceptions:
 * - Field access operations (GETSTATIC, PUTSTATIC, GETFIELD, PUTFIELD)
 * - Method invocation operations (INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE, INVOKEDYNAMIC)
 * - Object creation and type operations (NEW, ANEWARRAY, CHECKCAST, INSTANCEOF, MULTIANEWARRAY)
 *
 * Note: This test uses reflection to access the private mayThrowExceptions field because:
 * 1. The visitConstantInstruction method has no return value
 * 2. The method only modifies the private mayThrowExceptions field
 * 3. There is no public getter or observable side effect to verify the method's behavior
 * 4. The current implementation of mayThrowExceptions(Clazz, Method, CodeAttribute, int, Instruction)
 *    does not actually invoke the visitor methods, so the field cannot be tested indirectly
 * Therefore, reflection is the only way to test this method's correctness.
 */
public class ExceptionInstructionCheckerClaude_visitConstantInstructionTest {

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

    // Tests for field access operations that throw exceptions

    /**
     * Tests that visitConstantInstruction sets mayThrowExceptions to true for OP_GETSTATIC.
     * Static field access can throw ExceptionInInitializerError, NoClassDefFoundError, etc.
     */
    @Test
    public void testVisitConstantInstruction_withOpGetstatic_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_GETSTATIC should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitConstantInstruction sets mayThrowExceptions to true for OP_PUTSTATIC.
     * Static field write can throw various exceptions during class initialization.
     */
    @Test
    public void testVisitConstantInstruction_withOpPutstatic_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_PUTSTATIC should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitConstantInstruction sets mayThrowExceptions to true for OP_GETFIELD.
     * Instance field access can throw NullPointerException.
     */
    @Test
    public void testVisitConstantInstruction_withOpGetfield_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_GETFIELD should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitConstantInstruction sets mayThrowExceptions to true for OP_PUTFIELD.
     * Instance field write can throw NullPointerException.
     */
    @Test
    public void testVisitConstantInstruction_withOpPutfield_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_PUTFIELD should set mayThrowExceptions to true");
    }

    // Tests for method invocation operations

    /**
     * Tests that visitConstantInstruction sets mayThrowExceptions to true for OP_INVOKEVIRTUAL.
     * Virtual method invocation can throw NullPointerException and various exceptions from the method.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokevirtual_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_INVOKEVIRTUAL should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitConstantInstruction sets mayThrowExceptions to true for OP_INVOKESPECIAL.
     * Special method invocation (constructors, private methods, super calls) can throw exceptions.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokespecial_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_INVOKESPECIAL should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitConstantInstruction sets mayThrowExceptions to true for OP_INVOKESTATIC.
     * Static method invocation can throw various exceptions during class initialization and from the method.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokestatic_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_INVOKESTATIC should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitConstantInstruction sets mayThrowExceptions to true for OP_INVOKEINTERFACE.
     * Interface method invocation can throw NullPointerException and exceptions from the method.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokeinterface_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_INVOKEINTERFACE should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitConstantInstruction sets mayThrowExceptions to true for OP_INVOKEDYNAMIC.
     * Dynamic invocation can throw various exceptions during bootstrap and from the method.
     */
    @Test
    public void testVisitConstantInstruction_withOpInvokedynamic_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_INVOKEDYNAMIC should set mayThrowExceptions to true");
    }

    // Tests for object creation and type operations

    /**
     * Tests that visitConstantInstruction sets mayThrowExceptions to true for OP_NEW.
     * Object creation can throw OutOfMemoryError, ExceptionInInitializerError, etc.
     */
    @Test
    public void testVisitConstantInstruction_withOpNew_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_NEW, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_NEW should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitConstantInstruction sets mayThrowExceptions to true for OP_ANEWARRAY.
     * Reference array creation can throw NegativeArraySizeException and OutOfMemoryError.
     */
    @Test
    public void testVisitConstantInstruction_withOpAnewarray_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_ANEWARRAY should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitConstantInstruction sets mayThrowExceptions to true for OP_CHECKCAST.
     * Cast checking can throw ClassCastException.
     */
    @Test
    public void testVisitConstantInstruction_withOpCheckcast_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_CHECKCAST should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitConstantInstruction sets mayThrowExceptions to true for OP_INSTANCEOF.
     * Instance checking may throw exceptions during class loading/initialization.
     */
    @Test
    public void testVisitConstantInstruction_withOpInstanceof_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_INSTANCEOF should set mayThrowExceptions to true");
    }

    /**
     * Tests that visitConstantInstruction sets mayThrowExceptions to true for OP_MULTIANEWARRAY.
     * Multi-dimensional array creation can throw NegativeArraySizeException and OutOfMemoryError.
     */
    @Test
    public void testVisitConstantInstruction_withOpMultianewarray_setsMayThrowExceptionsTrue() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_MULTIANEWARRAY, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "OP_MULTIANEWARRAY should set mayThrowExceptions to true");
    }

    // Tests for non-exception-throwing constant instructions

    /**
     * Tests that visitConstantInstruction does NOT set mayThrowExceptions for OP_LDC.
     * Loading constants from the constant pool does not throw exceptions.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdc_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_LDC should not set mayThrowExceptions to true");
    }

    /**
     * Tests that visitConstantInstruction does NOT set mayThrowExceptions for OP_LDC_W.
     * Wide constant loading does not throw exceptions.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdcW_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC_W, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_LDC_W should not set mayThrowExceptions to true");
    }

    /**
     * Tests that visitConstantInstruction does NOT set mayThrowExceptions for OP_LDC2_W.
     * Loading long/double constants does not throw exceptions.
     */
    @Test
    public void testVisitConstantInstruction_withOpLdc2W_doesNotSetMayThrowExceptions() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC2_W, 1);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertFalse(getMayThrowExceptions(),
                "OP_LDC2_W should not set mayThrowExceptions to true");
    }

    // Tests for different constant indices

    /**
     * Tests that visitConstantInstruction with exception-throwing opcodes works with different constant indices.
     * The constant index should not affect whether the flag is set.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentConstantIndices_behavesConsistently() throws Exception {
        // Arrange
        int[] constantIndices = {0, 1, 10, 100, 1000, 65535};

        // Act & Assert
        for (int constantIndex : constantIndices) {
            resetMayThrowExceptions();
            ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, constantIndex);
            checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
            assertTrue(getMayThrowExceptions(),
                    "OP_INVOKEVIRTUAL should set mayThrowExceptions with constant index " + constantIndex);
        }
    }

    /**
     * Tests that visitConstantInstruction works correctly with various offset values.
     * The offset should not affect the behavior.
     */
    @Test
    public void testVisitConstantInstruction_withVariousOffsets_behavesConsistently() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_NEW, 1);
        int[] offsets = {0, 1, 10, 100, -1, Integer.MAX_VALUE};

        // Act & Assert
        for (int offset : offsets) {
            resetMayThrowExceptions();
            checker.visitConstantInstruction(clazz, method, codeAttribute, offset, instruction);
            assertTrue(getMayThrowExceptions(),
                    "OP_NEW should set mayThrowExceptions with offset " + offset);
        }
    }

    // Tests for multiple invocations

    /**
     * Tests that multiple calls to visitConstantInstruction with exception-throwing opcodes
     * keep the field set to true.
     */
    @Test
    public void testVisitConstantInstruction_multipleExceptionThrowingInstructions_maintainsTrue() throws Exception {
        // Arrange
        ConstantInstruction getstatic = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);
        ConstantInstruction invokevirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 2);
        ConstantInstruction newInst = new ConstantInstruction(Instruction.OP_NEW, 3);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, getstatic);
        assertTrue(getMayThrowExceptions(), "Should be true after first exception-throwing instruction");

        checker.visitConstantInstruction(clazz, method, codeAttribute, 2, invokevirtual);
        assertTrue(getMayThrowExceptions(), "Should remain true after second exception-throwing instruction");

        checker.visitConstantInstruction(clazz, method, codeAttribute, 4, newInst);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "Should remain true after multiple exception-throwing instructions");
    }

    /**
     * Tests that calling visitConstantInstruction with a non-exception-throwing instruction
     * after an exception-throwing one does not change the field from true.
     */
    @Test
    public void testVisitConstantInstruction_nonExceptionInstructionAfterExceptionInstruction_maintainsTrue() throws Exception {
        // Arrange
        ConstantInstruction newInst = new ConstantInstruction(Instruction.OP_NEW, 1);
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 2);
        resetMayThrowExceptions();

        // Act
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, newInst);
        assertTrue(getMayThrowExceptions(), "Should be true after exception-throwing instruction");

        checker.visitConstantInstruction(clazz, method, codeAttribute, 2, ldc);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "Should remain true even after non-exception-throwing instruction");
    }

    // Tests for all field access operations

    /**
     * Tests that all field access operations set mayThrowExceptions.
     */
    @Test
    public void testVisitConstantInstruction_allFieldAccessOperations_setMayThrowExceptions() throws Exception {
        // Test all field access operations
        byte[] fieldAccessOpcodes = {
            Instruction.OP_GETSTATIC,
            Instruction.OP_PUTSTATIC,
            Instruction.OP_GETFIELD,
            Instruction.OP_PUTFIELD
        };

        for (byte opcode : fieldAccessOpcodes) {
            resetMayThrowExceptions();
            ConstantInstruction instruction = new ConstantInstruction(opcode, 1);
            checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
            assertTrue(getMayThrowExceptions(),
                    "Opcode " + opcode + " should set mayThrowExceptions");
        }
    }

    /**
     * Tests that all method invocation operations set mayThrowExceptions.
     */
    @Test
    public void testVisitConstantInstruction_allMethodInvocationOperations_setMayThrowExceptions() throws Exception {
        // Test all method invocation operations
        byte[] methodInvocationOpcodes = {
            Instruction.OP_INVOKEVIRTUAL,
            Instruction.OP_INVOKESPECIAL,
            Instruction.OP_INVOKESTATIC,
            Instruction.OP_INVOKEINTERFACE,
            Instruction.OP_INVOKEDYNAMIC
        };

        for (byte opcode : methodInvocationOpcodes) {
            resetMayThrowExceptions();
            ConstantInstruction instruction = new ConstantInstruction(opcode, 1);
            checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
            assertTrue(getMayThrowExceptions(),
                    "Opcode " + opcode + " should set mayThrowExceptions");
        }
    }

    /**
     * Tests that all object creation and type operations set mayThrowExceptions.
     */
    @Test
    public void testVisitConstantInstruction_allObjectCreationAndTypeOperations_setMayThrowExceptions() throws Exception {
        // Test all object creation and type operations
        byte[] objectCreationOpcodes = {
            Instruction.OP_NEW,
            Instruction.OP_ANEWARRAY,
            Instruction.OP_CHECKCAST,
            Instruction.OP_INSTANCEOF,
            Instruction.OP_MULTIANEWARRAY
        };

        for (byte opcode : objectCreationOpcodes) {
            resetMayThrowExceptions();
            ConstantInstruction instruction = new ConstantInstruction(opcode, 1);
            checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
            assertTrue(getMayThrowExceptions(),
                    "Opcode " + opcode + " should set mayThrowExceptions");
        }
    }

    /**
     * Tests that all constant loading operations do NOT set mayThrowExceptions.
     */
    @Test
    public void testVisitConstantInstruction_allConstantLoadingOperations_doNotSetMayThrowExceptions() throws Exception {
        // Test all constant loading operations
        byte[] constantLoadingOpcodes = {
            Instruction.OP_LDC,
            Instruction.OP_LDC_W,
            Instruction.OP_LDC2_W
        };

        for (byte opcode : constantLoadingOpcodes) {
            resetMayThrowExceptions();
            ConstantInstruction instruction = new ConstantInstruction(opcode, 1);
            checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
            assertFalse(getMayThrowExceptions(),
                    "Opcode " + opcode + " should not set mayThrowExceptions");
        }
    }

    // Edge case tests

    /**
     * Tests that visitConstantInstruction can be called with null parameters without throwing NPE
     * for non-exception-throwing instructions (the method doesn't access the parameters).
     */
    @Test
    public void testVisitConstantInstruction_withNullParameters_doesNotThrowForNonExceptionInstruction() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);
        resetMayThrowExceptions();

        // Act & Assert - should not throw NPE
        assertDoesNotThrow(() -> checker.visitConstantInstruction(null, null, null, 0, instruction));
        assertFalse(getMayThrowExceptions(),
                "Should not set mayThrowExceptions even with null parameters");
    }

    /**
     * Tests that visitConstantInstruction with exception-throwing opcode works with null parameters.
     */
    @Test
    public void testVisitConstantInstruction_withNullParametersAndExceptionOpcode_setsFlag() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_NEW, 1);
        resetMayThrowExceptions();

        // Act & Assert - should not throw NPE
        assertDoesNotThrow(() -> checker.visitConstantInstruction(null, null, null, 0, instruction));
        assertTrue(getMayThrowExceptions(),
                "Should set mayThrowExceptions even with null parameters");
    }

    /**
     * Tests sequential calls with different exception-throwing opcodes.
     */
    @Test
    public void testVisitConstantInstruction_sequentialCallsWithDifferentOpcodes_allSetFlag() throws Exception {
        // Arrange & Act & Assert
        byte[] exceptionOpcodes = {
            Instruction.OP_GETSTATIC,
            Instruction.OP_INVOKEVIRTUAL,
            Instruction.OP_NEW,
            Instruction.OP_CHECKCAST
        };

        for (int i = 0; i < exceptionOpcodes.length; i++) {
            resetMayThrowExceptions();
            ConstantInstruction instruction = new ConstantInstruction(exceptionOpcodes[i], i + 1);
            checker.visitConstantInstruction(clazz, method, codeAttribute, i * 2, instruction);
            assertTrue(getMayThrowExceptions(),
                    "Opcode " + exceptionOpcodes[i] + " should set mayThrowExceptions");
        }
    }

    /**
     * Tests that the method correctly distinguishes between exception-throwing and non-exception-throwing opcodes.
     */
    @Test
    public void testVisitConstantInstruction_distinguishesExceptionVsNonException_correctly() throws Exception {
        // Test non-exception-throwing opcode
        resetMayThrowExceptions();
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 1);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);
        assertFalse(getMayThrowExceptions(), "LDC should not set flag");

        // Test exception-throwing opcode
        resetMayThrowExceptions();
        ConstantInstruction invokeVirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 2);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 2, invokeVirtual);
        assertTrue(getMayThrowExceptions(), "INVOKEVIRTUAL should set flag");

        // Test another non-exception-throwing opcode
        resetMayThrowExceptions();
        ConstantInstruction ldcW = new ConstantInstruction(Instruction.OP_LDC_W, 3);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 4, ldcW);
        assertFalse(getMayThrowExceptions(), "LDC_W should not set flag");
    }

    /**
     * Tests visitConstantInstruction with the same instruction called multiple times.
     */
    @Test
    public void testVisitConstantInstruction_sameInstructionMultipleTimes_maintainsCorrectState() throws Exception {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 5);
        resetMayThrowExceptions();

        // Act - call multiple times
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        assertTrue(getMayThrowExceptions(), "Should be true after first call");

        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        assertTrue(getMayThrowExceptions(), "Should remain true after second call");

        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        assertTrue(getMayThrowExceptions(),
                "Should remain true after multiple calls with same instruction");
    }

    /**
     * Tests that visitConstantInstruction handles boundary constant index values correctly.
     */
    @Test
    public void testVisitConstantInstruction_withBoundaryConstantIndices_behavesCorrectly() throws Exception {
        // Test with minimum constant index
        resetMayThrowExceptions();
        ConstantInstruction minIndex = new ConstantInstruction(Instruction.OP_NEW, 0);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, minIndex);
        assertTrue(getMayThrowExceptions(), "Should work with constant index 0");

        // Test with maximum constant index
        resetMayThrowExceptions();
        ConstantInstruction maxIndex = new ConstantInstruction(Instruction.OP_NEW, 65535);
        checker.visitConstantInstruction(clazz, method, codeAttribute, 0, maxIndex);
        assertTrue(getMayThrowExceptions(), "Should work with constant index 65535");
    }

    /**
     * Tests that visitConstantInstruction handles all 14 exception-throwing opcodes correctly.
     */
    @Test
    public void testVisitConstantInstruction_allFourteenExceptionThrowingOpcodes_setMayThrowExceptions() throws Exception {
        // All 14 exception-throwing opcodes from the implementation
        byte[] allExceptionOpcodes = {
            Instruction.OP_GETSTATIC,
            Instruction.OP_PUTSTATIC,
            Instruction.OP_GETFIELD,
            Instruction.OP_PUTFIELD,
            Instruction.OP_INVOKEVIRTUAL,
            Instruction.OP_INVOKESPECIAL,
            Instruction.OP_INVOKESTATIC,
            Instruction.OP_INVOKEINTERFACE,
            Instruction.OP_INVOKEDYNAMIC,
            Instruction.OP_NEW,
            Instruction.OP_ANEWARRAY,
            Instruction.OP_CHECKCAST,
            Instruction.OP_INSTANCEOF,
            Instruction.OP_MULTIANEWARRAY
        };

        assertEquals(14, allExceptionOpcodes.length, "Should test all 14 exception-throwing opcodes");

        for (byte opcode : allExceptionOpcodes) {
            resetMayThrowExceptions();
            ConstantInstruction instruction = new ConstantInstruction(opcode, 1);
            checker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
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
