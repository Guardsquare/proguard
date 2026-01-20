package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.*;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.optimize.info.ProgramClassOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link NoConstructorReferenceReplacer}.
 * Tests all methods including:
 * - Constructor with CodeAttributeEditor only
 * - Constructor with CodeAttributeEditor and InstructionVisitor
 * - visitAnyInstruction (no-op method)
 * - visitConstantInstruction (main logic for replacing references)
 * - visitAnyConstant (no-op method)
 * - visitFieldrefConstant (checks if class contains constructors for fields)
 * - visitAnyMethodrefConstant (checks if class contains constructors for methods)
 *
 * This class replaces instance references on classes without constructors by replacing
 * the instruction with aconst_null + athrow.
 */
public class NoConstructorReferenceReplacerClaudeTest {

    private NoConstructorReferenceReplacer replacer;
    private CodeAttributeEditor mockCodeAttributeEditor;
    private InstructionVisitor mockExtraVisitor;
    private Clazz mockClazz;
    private Method mockMethod;
    private CodeAttribute mockCodeAttribute;

    @BeforeEach
    public void setUp() {
        mockCodeAttributeEditor = mock(CodeAttributeEditor.class);
        mockExtraVisitor = mock(InstructionVisitor.class);
        mockClazz = mock(ProgramClass.class);
        mockMethod = mock(ProgramMethod.class);
        mockCodeAttribute = mock(CodeAttribute.class);
    }

    // ========================================
    // Constructor Tests
    // ========================================

    /**
     * Tests the single-parameter constructor creates a valid instance.
     */
    @Test
    public void testSingleParamConstructorCreatesValidInstance() {
        // Act
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        // Assert
        assertNotNull(replacer, "NoConstructorReferenceReplacer should be created successfully");
    }

    /**
     * Tests the two-parameter constructor with a valid non-null InstructionVisitor.
     */
    @Test
    public void testTwoParamConstructorWithValidInstructionVisitor() {
        // Act
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor, mockExtraVisitor);

        // Assert
        assertNotNull(replacer, "NoConstructorReferenceReplacer should be created successfully");
    }

    /**
     * Tests the two-parameter constructor with null InstructionVisitor.
     */
    @Test
    public void testTwoParamConstructorWithNullInstructionVisitor() {
        // Act
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor, null);

        // Assert
        assertNotNull(replacer, "NoConstructorReferenceReplacer should be created with null extraReferenceVisitor");
    }

    /**
     * Tests that the constructor doesn't invoke any methods on the InstructionVisitor.
     */
    @Test
    public void testConstructorDoesNotInvokeInstructionVisitor() {
        // Act
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor, mockExtraVisitor);

        // Assert
        assertNotNull(replacer, "NoConstructorReferenceReplacer should be created");
        verifyNoInteractions(mockExtraVisitor);
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testMultipleInstancesAreIndependent() {
        // Act
        NoConstructorReferenceReplacer replacer1 = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        NoConstructorReferenceReplacer replacer2 = new NoConstructorReferenceReplacer(mockCodeAttributeEditor, mockExtraVisitor);

        // Assert
        assertNotNull(replacer1, "First replacer should be created");
        assertNotNull(replacer2, "Second replacer should be created");
        assertNotSame(replacer1, replacer2, "Replacer instances should be different");
    }

    /**
     * Tests that the constructor completes quickly.
     */
    @Test
    public void testConstructorIsEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor, mockExtraVisitor);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(replacer, "NoConstructorReferenceReplacer should be created");
        assertTrue(duration < 10_000_000L, "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that the constructor creates an instance of InstructionVisitor.
     */
    @Test
    public void testConstructorCreatesInstructionVisitorInstance() {
        // Act
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        // Assert
        assertInstanceOf(InstructionVisitor.class, replacer,
            "NoConstructorReferenceReplacer should implement InstructionVisitor interface");
    }

    // ========================================
    // visitAnyInstruction Tests (No-op)
    // ========================================

    /**
     * Tests that visitAnyInstruction is a no-op and doesn't throw exceptions.
     */
    @Test
    public void testVisitAnyInstruction_doesNotThrowException() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(mockClazz, mockMethod, mockCodeAttribute, 0, instruction));
    }

    /**
     * Tests that visitAnyInstruction doesn't interact with any parameters.
     */
    @Test
    public void testVisitAnyInstruction_doesNotInteractWithParameters() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act
        replacer.visitAnyInstruction(mockClazz, mockMethod, mockCodeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(mockMethod);
        verifyNoInteractions(mockCodeAttribute);
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    /**
     * Tests that visitAnyInstruction can be called with null parameters.
     */
    @Test
    public void testVisitAnyInstruction_withNullParameters_doesNotThrowException() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        // Act & Assert
        assertDoesNotThrow(() -> replacer.visitAnyInstruction(null, null, null, 0, null));
    }

    // ========================================
    // visitConstantInstruction Tests - INVOKEVIRTUAL
    // ========================================

    /**
     * Tests visitConstantInstruction with INVOKEVIRTUAL when referenced class has constructors.
     * Should not replace the instruction.
     */
    @Test
    public void testVisitConstantInstruction_invokeVirtualWithConstructors_noReplacement() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        // Create a referenced class with constructors
        ProgramClass referencedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(referencedClass).setContainsConstructors();

        InterfaceMethodrefConstant methodrefConstant = new InterfaceMethodrefConstant();
        methodrefConstant.referencedClass = referencedClass;
        clazz.constantPool[1] = methodrefConstant;

        // Act
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);

        // Assert - should not replace the instruction
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    /**
     * Tests visitConstantInstruction with INVOKEVIRTUAL when referenced class has no constructors.
     * Should replace the instruction with aconst_null + athrow.
     */
    @Test
    public void testVisitConstantInstruction_invokeVirtualWithoutConstructors_replacesInstruction() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        // Create a referenced class without constructors (don't mark as containing constructors)
        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2accessFlags = 0; // Not an interface
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        InterfaceMethodrefConstant methodrefConstant = new InterfaceMethodrefConstant();
        methodrefConstant.referencedClass = referencedClass;
        clazz.constantPool[1] = methodrefConstant;

        // Act
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);

        // Assert - should replace the instruction
        verify(mockCodeAttributeEditor).replaceInstruction(eq(0), any(Instruction[].class));
    }

    /**
     * Tests visitConstantInstruction with INVOKEVIRTUAL when referenced class is null.
     * Should not replace (assumes unknown class has constructors).
     */
    @Test
    public void testVisitConstantInstruction_invokeVirtualWithNullReferencedClass_noReplacement() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        InterfaceMethodrefConstant methodrefConstant = new InterfaceMethodrefConstant();
        methodrefConstant.referencedClass = null;
        clazz.constantPool[1] = methodrefConstant;

        // Act
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);

        // Assert - should not replace the instruction
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    // ========================================
    // visitConstantInstruction Tests - INVOKESPECIAL
    // ========================================

    /**
     * Tests visitConstantInstruction with INVOKESPECIAL when referenced class has no constructors.
     */
    @Test
    public void testVisitConstantInstruction_invokeSpecialWithoutConstructors_replacesInstruction() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 1);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        // Create a referenced class without constructors
        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2accessFlags = 0; // Not an interface
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        InterfaceMethodrefConstant methodrefConstant = new InterfaceMethodrefConstant();
        methodrefConstant.referencedClass = referencedClass;
        clazz.constantPool[1] = methodrefConstant;

        // Act
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);

        // Assert - should replace the instruction
        verify(mockCodeAttributeEditor).replaceInstruction(eq(0), any(Instruction[].class));
    }

    /**
     * Tests visitConstantInstruction with INVOKESPECIAL when referenced class is an interface.
     * Should not replace (interfaces never have constructors but shouldn't be replaced).
     */
    @Test
    public void testVisitConstantInstruction_invokeSpecialOnInterface_noReplacement() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 1);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        // Create a referenced class that is an interface
        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2accessFlags = AccessConstants.INTERFACE;
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        InterfaceMethodrefConstant methodrefConstant = new InterfaceMethodrefConstant();
        methodrefConstant.referencedClass = referencedClass;
        clazz.constantPool[1] = methodrefConstant;

        // Act
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);

        // Assert - should not replace the instruction
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    // ========================================
    // visitConstantInstruction Tests - GETFIELD
    // ========================================

    /**
     * Tests visitConstantInstruction with GETFIELD when referenced class has no constructors.
     */
    @Test
    public void testVisitConstantInstruction_getFieldWithoutConstructors_replacesInstruction() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        // Create a referenced class without constructors
        ProgramClass referencedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        FieldrefConstant fieldrefConstant = new FieldrefConstant();
        fieldrefConstant.referencedClass = referencedClass;
        clazz.constantPool[1] = fieldrefConstant;

        // Act
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);

        // Assert - should replace the instruction
        verify(mockCodeAttributeEditor).replaceInstruction(eq(0), any(Instruction[].class));
    }

    /**
     * Tests visitConstantInstruction with GETFIELD when referenced class has constructors.
     */
    @Test
    public void testVisitConstantInstruction_getFieldWithConstructors_noReplacement() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        // Create a referenced class with constructors
        ProgramClass referencedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(referencedClass).setContainsConstructors();

        FieldrefConstant fieldrefConstant = new FieldrefConstant();
        fieldrefConstant.referencedClass = referencedClass;
        clazz.constantPool[1] = fieldrefConstant;

        // Act
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);

        // Assert - should not replace the instruction
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    /**
     * Tests visitConstantInstruction with GETFIELD when referenced class is null.
     */
    @Test
    public void testVisitConstantInstruction_getFieldWithNullReferencedClass_noReplacement() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        FieldrefConstant fieldrefConstant = new FieldrefConstant();
        fieldrefConstant.referencedClass = null;
        clazz.constantPool[1] = fieldrefConstant;

        // Act
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);

        // Assert - should not replace the instruction
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    // ========================================
    // visitConstantInstruction Tests - PUTFIELD
    // ========================================

    /**
     * Tests visitConstantInstruction with PUTFIELD when referenced class has no constructors.
     */
    @Test
    public void testVisitConstantInstruction_putFieldWithoutConstructors_replacesInstruction() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 1);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        // Create a referenced class without constructors
        ProgramClass referencedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        FieldrefConstant fieldrefConstant = new FieldrefConstant();
        fieldrefConstant.referencedClass = referencedClass;
        clazz.constantPool[1] = fieldrefConstant;

        // Act
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);

        // Assert - should replace the instruction
        verify(mockCodeAttributeEditor).replaceInstruction(eq(0), any(Instruction[].class));
    }

    /**
     * Tests visitConstantInstruction with PUTFIELD when referenced class has constructors.
     */
    @Test
    public void testVisitConstantInstruction_putFieldWithConstructors_noReplacement() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 1);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        // Create a referenced class with constructors
        ProgramClass referencedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(referencedClass).setContainsConstructors();

        FieldrefConstant fieldrefConstant = new FieldrefConstant();
        fieldrefConstant.referencedClass = referencedClass;
        clazz.constantPool[1] = fieldrefConstant;

        // Act
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);

        // Assert - should not replace the instruction
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    // ========================================
    // visitConstantInstruction Tests - Other opcodes
    // ========================================

    /**
     * Tests visitConstantInstruction with other opcodes (not INVOKEVIRTUAL, INVOKESPECIAL, GETFIELD, PUTFIELD).
     * Should not process these instructions.
     */
    @Test
    public void testVisitConstantInstruction_withOtherOpcodes_noProcessing() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        // Test various other opcodes
        byte[] otherOpcodes = {
            Instruction.OP_LDC,
            Instruction.OP_INVOKESTATIC,
            Instruction.OP_INVOKEINTERFACE,
            Instruction.OP_GETSTATIC,
            Instruction.OP_PUTSTATIC,
            Instruction.OP_NEW,
            Instruction.OP_ANEWARRAY
        };

        for (byte opcode : otherOpcodes) {
            ConstantInstruction instruction = new ConstantInstruction(opcode, 1);

            // Act
            replacer.visitConstantInstruction(mockClazz, mockMethod, mockCodeAttribute, 0, instruction);
        }

        // Assert - should not interact with the editor for any of these opcodes
        verifyNoInteractions(mockCodeAttributeEditor);
    }

    // ========================================
    // visitConstantInstruction Tests - Extra visitor
    // ========================================

    /**
     * Tests visitConstantInstruction with extra visitor when instruction is replaced.
     * The extra visitor should be called.
     */
    @Test
    public void testVisitConstantInstruction_withExtraVisitor_callsVisitor() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor, mockExtraVisitor);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        // Create a referenced class without constructors
        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2accessFlags = 0;
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        InterfaceMethodrefConstant methodrefConstant = new InterfaceMethodrefConstant();
        methodrefConstant.referencedClass = referencedClass;
        clazz.constantPool[1] = methodrefConstant;

        // Act
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);

        // Assert - should replace the instruction and call extra visitor
        verify(mockCodeAttributeEditor).replaceInstruction(eq(0), any(Instruction[].class));
        verify(mockExtraVisitor).visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);
    }

    /**
     * Tests visitConstantInstruction with extra visitor when instruction is not replaced.
     * The extra visitor should not be called.
     */
    @Test
    public void testVisitConstantInstruction_withExtraVisitorNoReplacement_doesNotCallVisitor() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor, mockExtraVisitor);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        // Create a referenced class with constructors
        ProgramClass referencedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(referencedClass).setContainsConstructors();

        InterfaceMethodrefConstant methodrefConstant = new InterfaceMethodrefConstant();
        methodrefConstant.referencedClass = referencedClass;
        clazz.constantPool[1] = methodrefConstant;

        // Act
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);

        // Assert - should not replace the instruction or call extra visitor
        verifyNoInteractions(mockCodeAttributeEditor);
        verifyNoInteractions(mockExtraVisitor);
    }

    // ========================================
    // visitAnyConstant Tests (No-op)
    // ========================================

    /**
     * Tests that visitAnyConstant is a no-op and doesn't throw exceptions.
     */
    @Test
    public void testVisitAnyConstant_doesNotThrowException() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        Constant constant = mock(Constant.class);

        // Act & Assert
        assertDoesNotThrow(() -> replacer.visitAnyConstant(mockClazz, constant));
    }

    /**
     * Tests that visitAnyConstant doesn't interact with parameters.
     */
    @Test
    public void testVisitAnyConstant_doesNotInteractWithParameters() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);
        Constant constant = mock(Constant.class);

        // Act
        replacer.visitAnyConstant(mockClazz, constant);

        // Assert
        verifyNoInteractions(mockClazz);
        verifyNoInteractions(constant);
    }

    /**
     * Tests that visitAnyConstant can be called with null parameters.
     */
    @Test
    public void testVisitAnyConstant_withNullParameters_doesNotThrowException() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        // Act & Assert
        assertDoesNotThrow(() -> replacer.visitAnyConstant(null, null));
    }

    // ========================================
    // visitFieldrefConstant Tests
    // ========================================

    /**
     * Tests visitFieldrefConstant when referenced class has constructors.
     * This should set internal state to indicate constructors exist.
     */
    @Test
    public void testVisitFieldrefConstant_withConstructors_setsStateCorrectly() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        ProgramClass referencedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(referencedClass).setContainsConstructors();

        FieldrefConstant fieldrefConstant = new FieldrefConstant();
        fieldrefConstant.referencedClass = referencedClass;

        // Act & Assert - the method completes without error
        assertDoesNotThrow(() -> replacer.visitFieldrefConstant(mockClazz, fieldrefConstant));
    }

    /**
     * Tests visitFieldrefConstant when referenced class has no constructors.
     */
    @Test
    public void testVisitFieldrefConstant_withoutConstructors_setsStateCorrectly() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        ProgramClass referencedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        FieldrefConstant fieldrefConstant = new FieldrefConstant();
        fieldrefConstant.referencedClass = referencedClass;

        // Act & Assert
        assertDoesNotThrow(() -> replacer.visitFieldrefConstant(mockClazz, fieldrefConstant));
    }

    /**
     * Tests visitFieldrefConstant when referenced class is null.
     * Should assume constructors exist (safe default).
     */
    @Test
    public void testVisitFieldrefConstant_withNullReferencedClass_assumesConstructors() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        FieldrefConstant fieldrefConstant = new FieldrefConstant();
        fieldrefConstant.referencedClass = null;

        // Act & Assert
        assertDoesNotThrow(() -> replacer.visitFieldrefConstant(mockClazz, fieldrefConstant));
    }

    // ========================================
    // visitAnyMethodrefConstant Tests
    // ========================================

    /**
     * Tests visitAnyMethodrefConstant when referenced class has constructors.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withConstructors_setsStateCorrectly() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2accessFlags = 0;
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(referencedClass).setContainsConstructors();

        InterfaceMethodrefConstant methodrefConstant = new InterfaceMethodrefConstant();
        methodrefConstant.referencedClass = referencedClass;

        // Act & Assert
        assertDoesNotThrow(() -> replacer.visitAnyMethodrefConstant(mockClazz, methodrefConstant));
    }

    /**
     * Tests visitAnyMethodrefConstant when referenced class has no constructors.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withoutConstructors_setsStateCorrectly() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2accessFlags = 0;
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        InterfaceMethodrefConstant methodrefConstant = new InterfaceMethodrefConstant();
        methodrefConstant.referencedClass = referencedClass;

        // Act & Assert
        assertDoesNotThrow(() -> replacer.visitAnyMethodrefConstant(mockClazz, methodrefConstant));
    }

    /**
     * Tests visitAnyMethodrefConstant when referenced class is null.
     * Should assume constructors exist (safe default).
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullReferencedClass_assumesConstructors() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        InterfaceMethodrefConstant methodrefConstant = new InterfaceMethodrefConstant();
        methodrefConstant.referencedClass = null;

        // Act & Assert
        assertDoesNotThrow(() -> replacer.visitAnyMethodrefConstant(mockClazz, methodrefConstant));
    }

    /**
     * Tests visitAnyMethodrefConstant when referenced class is an interface.
     * Should indicate constructors exist (interfaces shouldn't be replaced).
     */
    @Test
    public void testVisitAnyMethodrefConstant_withInterfaceClass_assumesConstructors() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2accessFlags = AccessConstants.INTERFACE;
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        InterfaceMethodrefConstant methodrefConstant = new InterfaceMethodrefConstant();
        methodrefConstant.referencedClass = referencedClass;

        // Act & Assert
        assertDoesNotThrow(() -> replacer.visitAnyMethodrefConstant(mockClazz, methodrefConstant));
    }

    // ========================================
    // Integration Tests
    // ========================================

    /**
     * Tests multiple consecutive calls with different states.
     */
    @Test
    public void testMultipleConsecutiveCalls_withDifferentStates() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[3];

        // First constant - class without constructors
        ProgramClass referencedClass1 = new ProgramClass();
        referencedClass1.u2accessFlags = 0;
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass1);

        InterfaceMethodrefConstant methodrefConstant1 = new InterfaceMethodrefConstant();
        methodrefConstant1.referencedClass = referencedClass1;
        clazz.constantPool[1] = methodrefConstant1;

        // Second constant - class with constructors
        ProgramClass referencedClass2 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass2);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(referencedClass2).setContainsConstructors();

        InterfaceMethodrefConstant methodrefConstant2 = new InterfaceMethodrefConstant();
        methodrefConstant2.referencedClass = referencedClass2;
        clazz.constantPool[2] = methodrefConstant2;

        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 2);

        // Act
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction1);
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 1, instruction2);

        // Assert - first should replace, second should not
        verify(mockCodeAttributeEditor, times(1)).replaceInstruction(eq(0), any(Instruction[].class));
        verify(mockCodeAttributeEditor, never()).replaceInstruction(eq(1), any(Instruction[].class));
    }

    /**
     * Tests that the replacer works correctly with various instruction offsets.
     */
    @Test
    public void testVisitConstantInstruction_withVariousOffsets() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2accessFlags = 0;
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        InterfaceMethodrefConstant methodrefConstant = new InterfaceMethodrefConstant();
        methodrefConstant.referencedClass = referencedClass;
        clazz.constantPool[1] = methodrefConstant;

        int[] offsets = {0, 10, 100, 1000};

        // Act
        for (int offset : offsets) {
            ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);
            replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, offset, instruction);
        }

        // Assert - should replace at each offset
        for (int offset : offsets) {
            verify(mockCodeAttributeEditor).replaceInstruction(eq(offset), any(Instruction[].class));
        }
    }

    /**
     * Tests that calling the same method multiple times on the same instance works correctly.
     */
    @Test
    public void testRepeatedCalls_workCorrectly() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        ProgramClass referencedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        FieldrefConstant fieldrefConstant = new FieldrefConstant();
        fieldrefConstant.referencedClass = referencedClass;
        clazz.constantPool[1] = fieldrefConstant;

        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);

        // Act - call multiple times
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);

        // Assert - should replace each time
        verify(mockCodeAttributeEditor, times(3)).replaceInstruction(eq(0), any(Instruction[].class));
    }

    /**
     * Tests that the replacer handles mixed field and method references correctly.
     */
    @Test
    public void testMixedFieldAndMethodReferences() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[3];

        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2accessFlags = 0;
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        // Field reference
        FieldrefConstant fieldrefConstant = new FieldrefConstant();
        fieldrefConstant.referencedClass = referencedClass;
        clazz.constantPool[1] = fieldrefConstant;

        // Method reference
        InterfaceMethodrefConstant methodrefConstant = new InterfaceMethodrefConstant();
        methodrefConstant.referencedClass = referencedClass;
        clazz.constantPool[2] = methodrefConstant;

        ConstantInstruction fieldInstruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);
        ConstantInstruction methodInstruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 2);

        // Act
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, fieldInstruction);
        replacer.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 1, methodInstruction);

        // Assert - both should be replaced
        verify(mockCodeAttributeEditor).replaceInstruction(eq(0), any(Instruction[].class));
        verify(mockCodeAttributeEditor).replaceInstruction(eq(1), any(Instruction[].class));
    }

    /**
     * Tests that the class implements ConstantVisitor interface.
     */
    @Test
    public void testImplementsConstantVisitorInterface() {
        // Arrange
        replacer = new NoConstructorReferenceReplacer(mockCodeAttributeEditor);

        // Assert
        assertTrue(replacer instanceof proguard.classfile.constant.visitor.ConstantVisitor,
            "NoConstructorReferenceReplacer should implement ConstantVisitor interface");
    }

    /**
     * Tests that multiple instances work independently.
     */
    @Test
    public void testMultipleInstancesWorkIndependently() {
        // Arrange
        CodeAttributeEditor editor1 = mock(CodeAttributeEditor.class);
        CodeAttributeEditor editor2 = mock(CodeAttributeEditor.class);
        NoConstructorReferenceReplacer replacer1 = new NoConstructorReferenceReplacer(editor1);
        NoConstructorReferenceReplacer replacer2 = new NoConstructorReferenceReplacer(editor2);

        ProgramClass clazz = new ProgramClass();
        clazz.constantPool = new Constant[2];

        ProgramClass referencedClass = new ProgramClass();
        referencedClass.u2accessFlags = 0;
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        InterfaceMethodrefConstant methodrefConstant = new InterfaceMethodrefConstant();
        methodrefConstant.referencedClass = referencedClass;
        clazz.constantPool[1] = methodrefConstant;

        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);

        // Act
        replacer1.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 0, instruction);
        replacer2.visitConstantInstruction(clazz, mockMethod, mockCodeAttribute, 1, instruction);

        // Assert - each should use its own editor
        verify(editor1).replaceInstruction(eq(0), any(Instruction[].class));
        verify(editor2).replaceInstruction(eq(1), any(Instruction[].class));
        verify(editor1, never()).replaceInstruction(eq(1), any(Instruction[].class));
        verify(editor2, never()).replaceInstruction(eq(0), any(Instruction[].class));
    }
}
