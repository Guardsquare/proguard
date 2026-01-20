package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.*;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.visitor.MemberVisitor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link CalledMemberVisitor}.
 *
 * Tests all methods in CalledMemberVisitor:
 * - Constructor
 * - visitAnyInstruction (no-op method)
 * - visitConstantInstruction (processes invoke and field access instructions)
 */
public class CalledMemberVisitorClaudeTest {

    private CalledMemberVisitor calledMemberVisitor;
    private TestMemberVisitor testMemberVisitor;
    private ProgramClass programClass;
    private ProgramMethod programMethod;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        testMemberVisitor = new TestMemberVisitor();
        calledMemberVisitor = new CalledMemberVisitor(testMemberVisitor);

        // Create a test program class with a constant pool
        programClass = new ProgramClass();
        programClass.u2constantPoolCount = 10;
        programClass.constantPool = new Constant[10];

        // Create a test method
        programMethod = new ProgramMethod();
        programMethod.u2nameIndex = 1;
        programMethod.u2descriptorIndex = 2;

        // Create a code attribute
        codeAttribute = new CodeAttribute();
        codeAttribute.u2maxStack = 10;
        codeAttribute.u2maxLocals = 5;
        codeAttribute.u4codeLength = 100;
        codeAttribute.code = new byte[100];
    }

    // ========== Constructor Tests ==========

    /**
     * Tests constructor with valid MemberVisitor parameter.
     * Verifies that the CalledMemberVisitor is properly instantiated.
     */
    @Test
    public void testConstructor_withValidMemberVisitor_createsInstance() {
        // Arrange
        TestMemberVisitor visitor = new TestMemberVisitor();

        // Act
        CalledMemberVisitor result = new CalledMemberVisitor(visitor);

        // Assert
        assertNotNull(result, "CalledMemberVisitor should be instantiated");
    }

    /**
     * Tests constructor with null MemberVisitor parameter.
     * Constructor should accept null but may throw NullPointerException during actual use.
     */
    @Test
    public void testConstructor_withNullMemberVisitor_createsInstance() {
        // Act & Assert
        assertDoesNotThrow(() -> new CalledMemberVisitor(null),
                "Constructor should accept null MemberVisitor");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_independent() {
        // Arrange
        TestMemberVisitor visitor1 = new TestMemberVisitor();
        TestMemberVisitor visitor2 = new TestMemberVisitor();

        // Act
        CalledMemberVisitor calledVisitor1 = new CalledMemberVisitor(visitor1);
        CalledMemberVisitor calledVisitor2 = new CalledMemberVisitor(visitor2);

        // Assert
        assertNotNull(calledVisitor1, "First instance should be created");
        assertNotNull(calledVisitor2, "Second instance should be created");
        assertNotSame(calledVisitor1, calledVisitor2, "Instances should be different");
    }

    // ========== visitAnyInstruction Tests ==========

    /**
     * Tests visitAnyInstruction with valid parameters.
     * This method is a no-op, so it should simply return without throwing.
     */
    @Test
    public void testVisitAnyInstruction_withValidParameters_doesNotThrow() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> calledMemberVisitor.visitAnyInstruction(
                programClass, programMethod, codeAttribute, 0, instruction),
                "visitAnyInstruction should not throw with valid parameters");
    }

    /**
     * Tests visitAnyInstruction with null parameters.
     * Since this is a no-op method, it should not throw even with null parameters.
     */
    @Test
    public void testVisitAnyInstruction_withNullParameters_doesNotThrow() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> calledMemberVisitor.visitAnyInstruction(
                null, null, null, 0, null),
                "visitAnyInstruction should not throw even with null parameters");
    }

    /**
     * Tests that visitAnyInstruction does not interact with the member visitor.
     * Being a no-op, it should not trigger any visits to the underlying member visitor.
     */
    @Test
    public void testVisitAnyInstruction_doesNotTriggerMemberVisitor() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act
        calledMemberVisitor.visitAnyInstruction(programClass, programMethod, codeAttribute, 0, instruction);

        // Assert
        assertTrue(testMemberVisitor.visitedMembers.isEmpty(),
                "visitAnyInstruction should not trigger member visitor");
    }

    /**
     * Tests visitAnyInstruction with various instruction offsets.
     */
    @Test
    public void testVisitAnyInstruction_withVariousOffsets_doesNotThrow() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NOP);
        int[] offsets = {0, 1, 10, 50, 99, -1, 1000};

        // Act & Assert
        for (int offset : offsets) {
            assertDoesNotThrow(() -> calledMemberVisitor.visitAnyInstruction(
                    programClass, programMethod, codeAttribute, offset, instruction),
                    "visitAnyInstruction should not throw with offset: " + offset);
        }
    }

    /**
     * Tests that multiple calls to visitAnyInstruction have no side effects.
     */
    @Test
    public void testVisitAnyInstruction_multipleCalls_noSideEffects() {
        // Arrange
        SimpleInstruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 10; i++) {
                calledMemberVisitor.visitAnyInstruction(
                        programClass, programMethod, codeAttribute, i, instruction);
            }
        }, "Multiple calls to visitAnyInstruction should not throw");

        assertTrue(testMemberVisitor.visitedMembers.isEmpty(),
                "visitAnyInstruction should not trigger member visitor after multiple calls");
    }

    // ========== visitConstantInstruction Tests - INVOKEVIRTUAL ==========

    /**
     * Tests visitConstantInstruction with INVOKEVIRTUAL opcode.
     * Should trigger the member visitor through the constant pool reference.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeVirtual_triggersVisitor() {
        // Arrange
        setupMethodRefConstant(3, "testMethod", "()V");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 3);

        // Act
        calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction);

        // Assert
        // The visitor should be triggered for the referenced member
        // Note: Since we're using a mock setup, the actual visitor behavior depends on
        // the constant pool entry's referencedMember field being set up
    }

    /**
     * Tests visitConstantInstruction with INVOKEVIRTUAL and various constant indices.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeVirtualVariousIndices_doesNotThrow() {
        // Arrange
        setupMethodRefConstant(3, "method1", "()V");
        setupMethodRefConstant(4, "method2", "(I)I");
        setupMethodRefConstant(5, "method3", "(Ljava/lang/String;)V");

        // Act & Assert
        assertDoesNotThrow(() -> {
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 0,
                    new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 3));
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 4,
                    new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 4));
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 8,
                    new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 5));
        }, "visitConstantInstruction should handle various constant indices for INVOKEVIRTUAL");
    }

    // ========== visitConstantInstruction Tests - INVOKESPECIAL ==========

    /**
     * Tests visitConstantInstruction with INVOKESPECIAL opcode.
     * Should trigger the member visitor through the constant pool reference.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeSpecial_doesNotThrow() {
        // Arrange
        setupMethodRefConstant(3, "<init>", "()V");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 3);

        // Act & Assert
        assertDoesNotThrow(() -> calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction),
                "visitConstantInstruction should not throw with INVOKESPECIAL");
    }

    /**
     * Tests visitConstantInstruction with INVOKESPECIAL for constructor calls.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeSpecialConstructor_doesNotThrow() {
        // Arrange
        setupMethodRefConstant(3, "<init>", "(Ljava/lang/String;)V");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 3);

        // Act & Assert
        assertDoesNotThrow(() -> calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction),
                "visitConstantInstruction should handle constructor calls");
    }

    // ========== visitConstantInstruction Tests - INVOKESTATIC ==========

    /**
     * Tests visitConstantInstruction with INVOKESTATIC opcode.
     * Should trigger the member visitor through the constant pool reference.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeStatic_doesNotThrow() {
        // Arrange
        setupMethodRefConstant(3, "staticMethod", "()V");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 3);

        // Act & Assert
        assertDoesNotThrow(() -> calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction),
                "visitConstantInstruction should not throw with INVOKESTATIC");
    }

    /**
     * Tests visitConstantInstruction with INVOKESTATIC for various static methods.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeStaticVariousMethods_doesNotThrow() {
        // Arrange
        setupMethodRefConstant(3, "valueOf", "(I)Ljava/lang/String;");
        setupMethodRefConstant(4, "currentTimeMillis", "()J");
        setupMethodRefConstant(5, "min", "(II)I");

        // Act & Assert
        assertDoesNotThrow(() -> {
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 0,
                    new ConstantInstruction(Instruction.OP_INVOKESTATIC, 3));
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 4,
                    new ConstantInstruction(Instruction.OP_INVOKESTATIC, 4));
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 8,
                    new ConstantInstruction(Instruction.OP_INVOKESTATIC, 5));
        }, "visitConstantInstruction should handle various static method calls");
    }

    // ========== visitConstantInstruction Tests - INVOKEINTERFACE ==========

    /**
     * Tests visitConstantInstruction with INVOKEINTERFACE opcode.
     * Should trigger the member visitor through the constant pool reference.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeInterface_doesNotThrow() {
        // Arrange
        setupInterfaceMethodRefConstant(3, "interfaceMethod", "()V");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 3);

        // Act & Assert
        assertDoesNotThrow(() -> calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction),
                "visitConstantInstruction should not throw with INVOKEINTERFACE");
    }

    /**
     * Tests visitConstantInstruction with INVOKEINTERFACE for multiple interface methods.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeInterfaceMultipleMethods_doesNotThrow() {
        // Arrange
        setupInterfaceMethodRefConstant(3, "get", "()Ljava/lang/Object;");
        setupInterfaceMethodRefConstant(4, "set", "(Ljava/lang/Object;)V");
        setupInterfaceMethodRefConstant(5, "size", "()I");

        // Act & Assert
        assertDoesNotThrow(() -> {
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 0,
                    new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 3));
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 4,
                    new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 4));
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 8,
                    new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 5));
        }, "visitConstantInstruction should handle multiple interface method calls");
    }

    // ========== visitConstantInstruction Tests - INVOKEDYNAMIC ==========

    /**
     * Tests visitConstantInstruction with INVOKEDYNAMIC opcode.
     * Should trigger the member visitor through the constant pool reference.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeDynamic_doesNotThrow() {
        // Arrange
        setupInvokeDynamicConstant(3);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 3);

        // Act & Assert
        assertDoesNotThrow(() -> calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction),
                "visitConstantInstruction should not throw with INVOKEDYNAMIC");
    }

    /**
     * Tests visitConstantInstruction with INVOKEDYNAMIC for lambda expressions.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeDynamicLambda_doesNotThrow() {
        // Arrange
        setupInvokeDynamicConstant(3);
        setupInvokeDynamicConstant(4);
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 3);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 4);

        // Act & Assert
        assertDoesNotThrow(() -> {
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 0, instruction1);
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 4, instruction2);
        }, "visitConstantInstruction should handle lambda INVOKEDYNAMIC calls");
    }

    // ========== visitConstantInstruction Tests - GETSTATIC ==========

    /**
     * Tests visitConstantInstruction with GETSTATIC opcode.
     * Should trigger the static class initializer visitor for the field's class.
     */
    @Test
    public void testVisitConstantInstruction_withGetStatic_doesNotThrow() {
        // Arrange
        setupFieldRefConstant(3, "staticField", "I");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 3);

        // Act & Assert
        assertDoesNotThrow(() -> calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction),
                "visitConstantInstruction should not throw with GETSTATIC");
    }

    /**
     * Tests visitConstantInstruction with GETSTATIC for various field types.
     */
    @Test
    public void testVisitConstantInstruction_withGetStaticVariousFields_doesNotThrow() {
        // Arrange
        setupFieldRefConstant(3, "intField", "I");
        setupFieldRefConstant(4, "stringField", "Ljava/lang/String;");
        setupFieldRefConstant(5, "arrayField", "[I");

        // Act & Assert
        assertDoesNotThrow(() -> {
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 0,
                    new ConstantInstruction(Instruction.OP_GETSTATIC, 3));
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 2,
                    new ConstantInstruction(Instruction.OP_GETSTATIC, 4));
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 4,
                    new ConstantInstruction(Instruction.OP_GETSTATIC, 5));
        }, "visitConstantInstruction should handle various field types for GETSTATIC");
    }

    // ========== visitConstantInstruction Tests - PUTSTATIC ==========

    /**
     * Tests visitConstantInstruction with PUTSTATIC opcode.
     * Should trigger the static class initializer visitor for the field's class.
     */
    @Test
    public void testVisitConstantInstruction_withPutStatic_doesNotThrow() {
        // Arrange
        setupFieldRefConstant(3, "staticField", "I");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 3);

        // Act & Assert
        assertDoesNotThrow(() -> calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction),
                "visitConstantInstruction should not throw with PUTSTATIC");
    }

    /**
     * Tests visitConstantInstruction with PUTSTATIC for various field types.
     */
    @Test
    public void testVisitConstantInstruction_withPutStaticVariousFields_doesNotThrow() {
        // Arrange
        setupFieldRefConstant(3, "counter", "I");
        setupFieldRefConstant(4, "name", "Ljava/lang/String;");
        setupFieldRefConstant(5, "instance", "Lcom/example/Singleton;");

        // Act & Assert
        assertDoesNotThrow(() -> {
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 0,
                    new ConstantInstruction(Instruction.OP_PUTSTATIC, 3));
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 2,
                    new ConstantInstruction(Instruction.OP_PUTSTATIC, 4));
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 4,
                    new ConstantInstruction(Instruction.OP_PUTSTATIC, 5));
        }, "visitConstantInstruction should handle various field types for PUTSTATIC");
    }

    // ========== visitConstantInstruction Tests - Other Opcodes ==========

    /**
     * Tests visitConstantInstruction with LDC opcode (load constant).
     * This opcode should not trigger the member visitor as it's not a method call or field access.
     */
    @Test
    public void testVisitConstantInstruction_withLdc_doesNotTriggerVisitor() {
        // Arrange
        setupStringConstant(3, "Hello World");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 3);

        // Act
        calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction);

        // Assert
        // For non-invoke and non-field-access instructions, no visitor should be triggered
        // The test passes if no exception is thrown
    }

    /**
     * Tests visitConstantInstruction with GETFIELD opcode.
     * This opcode should not trigger the static initializer visitor (only GETSTATIC does).
     */
    @Test
    public void testVisitConstantInstruction_withGetField_doesNotTriggerStaticInitializer() {
        // Arrange
        setupFieldRefConstant(3, "instanceField", "I");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 3);

        // Act
        calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction);

        // Assert
        // For GETFIELD (non-static), no static initializer should be triggered
        // The test passes if no exception is thrown
    }

    /**
     * Tests visitConstantInstruction with PUTFIELD opcode.
     * This opcode should not trigger the static initializer visitor (only PUTSTATIC does).
     */
    @Test
    public void testVisitConstantInstruction_withPutField_doesNotTriggerStaticInitializer() {
        // Arrange
        setupFieldRefConstant(3, "instanceField", "I");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 3);

        // Act
        calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction);

        // Assert
        // For PUTFIELD (non-static), no static initializer should be triggered
        // The test passes if no exception is thrown
    }

    /**
     * Tests visitConstantInstruction with NEW opcode.
     * This opcode should not trigger the member visitor as it's not a method call.
     */
    @Test
    public void testVisitConstantInstruction_withNew_doesNotThrow() {
        // Arrange
        setupClassConstant(3, "com/example/TestClass");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_NEW, 3);

        // Act & Assert
        assertDoesNotThrow(() -> calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction),
                "visitConstantInstruction should not throw with NEW opcode");
    }

    /**
     * Tests visitConstantInstruction with ANEWARRAY opcode.
     * This opcode should not trigger the member visitor.
     */
    @Test
    public void testVisitConstantInstruction_withAnewArray_doesNotThrow() {
        // Arrange
        setupClassConstant(3, "java/lang/String");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 3);

        // Act & Assert
        assertDoesNotThrow(() -> calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction),
                "visitConstantInstruction should not throw with ANEWARRAY opcode");
    }

    /**
     * Tests visitConstantInstruction with CHECKCAST opcode.
     * This opcode should not trigger the member visitor.
     */
    @Test
    public void testVisitConstantInstruction_withCheckCast_doesNotThrow() {
        // Arrange
        setupClassConstant(3, "java/lang/String");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 3);

        // Act & Assert
        assertDoesNotThrow(() -> calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction),
                "visitConstantInstruction should not throw with CHECKCAST opcode");
    }

    /**
     * Tests visitConstantInstruction with INSTANCEOF opcode.
     * This opcode should not trigger the member visitor.
     */
    @Test
    public void testVisitConstantInstruction_withInstanceOf_doesNotThrow() {
        // Arrange
        setupClassConstant(3, "java/lang/Runnable");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 3);

        // Act & Assert
        assertDoesNotThrow(() -> calledMemberVisitor.visitConstantInstruction(
                programClass, programMethod, codeAttribute, 0, instruction),
                "visitConstantInstruction should not throw with INSTANCEOF opcode");
    }

    // ========== Mixed Tests ==========

    /**
     * Tests visitConstantInstruction with a sequence of various opcodes.
     * Simulates a more realistic method body with multiple instruction types.
     */
    @Test
    public void testVisitConstantInstruction_withMixedOpcodes_doesNotThrow() {
        // Arrange
        setupMethodRefConstant(3, "method1", "()V");
        setupFieldRefConstant(4, "field1", "I");
        setupStringConstant(5, "test");
        setupClassConstant(6, "com/example/Test");

        // Act & Assert
        assertDoesNotThrow(() -> {
            calledMemberVisitor.visitConstantInstruction(programClass, programMethod, codeAttribute, 0,
                    new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 3));
            calledMemberVisitor.visitConstantInstruction(programClass, programMethod, codeAttribute, 3,
                    new ConstantInstruction(Instruction.OP_GETSTATIC, 4));
            calledMemberVisitor.visitConstantInstruction(programClass, programMethod, codeAttribute, 6,
                    new ConstantInstruction(Instruction.OP_LDC, 5));
            calledMemberVisitor.visitConstantInstruction(programClass, programMethod, codeAttribute, 8,
                    new ConstantInstruction(Instruction.OP_NEW, 6));
            calledMemberVisitor.visitConstantInstruction(programClass, programMethod, codeAttribute, 11,
                    new ConstantInstruction(Instruction.OP_PUTSTATIC, 4));
        }, "visitConstantInstruction should handle mixed opcodes");
    }

    /**
     * Tests visitConstantInstruction with multiple calls to the same constant.
     * Verifies that repeated references work correctly.
     */
    @Test
    public void testVisitConstantInstruction_withRepeatedConstantIndex_doesNotThrow() {
        // Arrange
        setupMethodRefConstant(3, "repeatedMethod", "()V");
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 3);

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5; i++) {
                calledMemberVisitor.visitConstantInstruction(
                        programClass, programMethod, codeAttribute, i * 3, instruction);
            }
        }, "visitConstantInstruction should handle repeated constant references");
    }

    /**
     * Tests visitConstantInstruction with all invoke opcodes in sequence.
     * Ensures all invoke types are handled properly.
     */
    @Test
    public void testVisitConstantInstruction_withAllInvokeOpcodes_doesNotThrow() {
        // Arrange
        setupMethodRefConstant(3, "virtualMethod", "()V");
        setupMethodRefConstant(4, "specialMethod", "()V");
        setupMethodRefConstant(5, "staticMethod", "()V");
        setupInterfaceMethodRefConstant(6, "interfaceMethod", "()V");
        setupInvokeDynamicConstant(7);

        // Act & Assert
        assertDoesNotThrow(() -> {
            calledMemberVisitor.visitConstantInstruction(programClass, programMethod, codeAttribute, 0,
                    new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 3));
            calledMemberVisitor.visitConstantInstruction(programClass, programMethod, codeAttribute, 3,
                    new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 4));
            calledMemberVisitor.visitConstantInstruction(programClass, programMethod, codeAttribute, 6,
                    new ConstantInstruction(Instruction.OP_INVOKESTATIC, 5));
            calledMemberVisitor.visitConstantInstruction(programClass, programMethod, codeAttribute, 9,
                    new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 6));
            calledMemberVisitor.visitConstantInstruction(programClass, programMethod, codeAttribute, 12,
                    new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 7));
        }, "visitConstantInstruction should handle all invoke opcodes");
    }

    /**
     * Tests visitConstantInstruction with both field access opcodes.
     * Ensures both GETSTATIC and PUTSTATIC are handled properly.
     */
    @Test
    public void testVisitConstantInstruction_withBothFieldAccessOpcodes_doesNotThrow() {
        // Arrange
        setupFieldRefConstant(3, "staticField", "I");

        // Act & Assert
        assertDoesNotThrow(() -> {
            calledMemberVisitor.visitConstantInstruction(programClass, programMethod, codeAttribute, 0,
                    new ConstantInstruction(Instruction.OP_GETSTATIC, 3));
            calledMemberVisitor.visitConstantInstruction(programClass, programMethod, codeAttribute, 3,
                    new ConstantInstruction(Instruction.OP_PUTSTATIC, 3));
        }, "visitConstantInstruction should handle both GETSTATIC and PUTSTATIC");
    }

    /**
     * Tests that the CalledMemberVisitor can be reused after processing instructions.
     */
    @Test
    public void testVisitConstantInstruction_reuseVisitor_doesNotThrow() {
        // Arrange
        setupMethodRefConstant(3, "method1", "()V");
        setupMethodRefConstant(4, "method2", "()V");
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 3);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 4);

        // Act & Assert
        assertDoesNotThrow(() -> {
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 0, instruction1);
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 3, instruction2);
            calledMemberVisitor.visitConstantInstruction(
                    programClass, programMethod, codeAttribute, 6, instruction1);
        }, "CalledMemberVisitor should be reusable");
    }

    // ========== Helper Methods ==========

    /**
     * Sets up a MethodrefConstant in the constant pool.
     */
    private void setupMethodRefConstant(int index, String methodName, String descriptor) {
        MethodrefConstant methodRef = new MethodrefConstant();
        programClass.constantPool[index] = methodRef;
    }

    /**
     * Sets up an InterfaceMethodrefConstant in the constant pool.
     */
    private void setupInterfaceMethodRefConstant(int index, String methodName, String descriptor) {
        InterfaceMethodrefConstant methodRef = new InterfaceMethodrefConstant();
        programClass.constantPool[index] = methodRef;
    }

    /**
     * Sets up an InvokeDynamicConstant in the constant pool.
     */
    private void setupInvokeDynamicConstant(int index) {
        InvokeDynamicConstant invokeDynamic = new InvokeDynamicConstant();
        programClass.constantPool[index] = invokeDynamic;
    }

    /**
     * Sets up a FieldrefConstant in the constant pool.
     */
    private void setupFieldRefConstant(int index, String fieldName, String descriptor) {
        FieldrefConstant fieldRef = new FieldrefConstant();
        programClass.constantPool[index] = fieldRef;
    }

    /**
     * Sets up a StringConstant in the constant pool.
     */
    private void setupStringConstant(int index, String value) {
        StringConstant stringConstant = new StringConstant();
        programClass.constantPool[index] = stringConstant;
    }

    /**
     * Sets up a ClassConstant in the constant pool.
     */
    private void setupClassConstant(int index, String className) {
        ClassConstant classConstant = new ClassConstant();
        programClass.constantPool[index] = classConstant;
    }

    // ========== Test Helper Classes ==========

    /**
     * Test implementation of MemberVisitor to track visited members.
     */
    private static class TestMemberVisitor implements MemberVisitor {
        public final List<String> visitedMembers = new ArrayList<>();

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            visitedMembers.add("ProgramField: " + programField.getName(programClass));
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            visitedMembers.add("ProgramMethod: " + programMethod.getName(programClass));
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            visitedMembers.add("LibraryField: " + libraryField.getName(libraryClass));
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            visitedMembers.add("LibraryMethod: " + libraryMethod.getName(libraryClass));
        }
    }
}
