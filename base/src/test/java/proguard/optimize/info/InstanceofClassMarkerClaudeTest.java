package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test class for {@link InstanceofClassMarker}.
 *
 * Tests all methods in the InstanceofClassMarker class:
 * - Constructor: <init>()
 * - visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)
 * - visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)
 * - visitClassConstant(Clazz, ClassConstant)
 * - visitAnyClass(Clazz)
 * - isInstanceofed(Clazz)
 *
 * The InstanceofClassMarker marks classes that are used in instanceof tests.
 * When it encounters an OP_INSTANCEOF instruction, it processes the class constant
 * referenced by that instruction and marks the referenced class as "instanceofed".
 */
public class InstanceofClassMarkerClaudeTest {

    private InstanceofClassMarker marker;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        marker = new InstanceofClassMarker();
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    // ========== Constructor Tests ==========

    /**
     * Tests that the constructor creates a valid InstanceofClassMarker instance.
     */
    @Test
    public void testConstructor_createsValidInstance() {
        // Act
        InstanceofClassMarker newMarker = new InstanceofClassMarker();

        // Assert
        assertNotNull(newMarker, "Constructor should create a valid instance");
    }

    /**
     * Tests that multiple InstanceofClassMarker instances can be created independently.
     */
    @Test
    public void testConstructor_multipleInstances_areIndependent() {
        // Act
        InstanceofClassMarker marker1 = new InstanceofClassMarker();
        InstanceofClassMarker marker2 = new InstanceofClassMarker();

        // Assert
        assertNotNull(marker1, "First instance should be created");
        assertNotNull(marker2, "Second instance should be created");
        assertNotSame(marker1, marker2, "Instances should be different objects");
    }

    /**
     * Tests that the constructor initializes the marker to implement required interfaces.
     */
    @Test
    public void testConstructor_implementsRequiredInterfaces() {
        // Act
        InstanceofClassMarker newMarker = new InstanceofClassMarker();

        // Assert
        assertTrue(newMarker instanceof proguard.classfile.instruction.visitor.InstructionVisitor,
                "Should implement InstructionVisitor");
        assertTrue(newMarker instanceof proguard.classfile.constant.visitor.ConstantVisitor,
                "Should implement ConstantVisitor");
        assertTrue(newMarker instanceof ClassVisitor,
                "Should implement ClassVisitor");
    }

    // ========== visitAnyInstruction Tests ==========

    /**
     * Tests that visitAnyInstruction does nothing (it's a no-op method).
     * This method is part of the InstructionVisitor interface but has an empty implementation.
     */
    @Test
    public void testVisitAnyInstruction_doesNothing() {
        // Arrange
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> marker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction));

        // Assert - should not interact with any parameters
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction can be called multiple times without side effects.
     */
    @Test
    public void testVisitAnyInstruction_calledMultipleTimes_noSideEffects() {
        // Arrange
        Instruction instruction = new SimpleInstruction(Instruction.OP_NOP);

        // Act
        marker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        marker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);
        marker.visitAnyInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - should not interact with any parameters
        verifyNoInteractions(clazz);
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that visitAnyInstruction with null parameters doesn't throw exceptions.
     */
    @Test
    public void testVisitAnyInstruction_withNullParameters_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyInstruction(null, null, null, 0, null));
    }

    /**
     * Tests that visitAnyInstruction with various instruction types behaves consistently.
     */
    @Test
    public void testVisitAnyInstruction_withVariousInstructions_behavesConsistently() {
        // Arrange
        Instruction nop = new SimpleInstruction(Instruction.OP_NOP);
        Instruction dup = new SimpleInstruction(Instruction.OP_DUP);
        Instruction ret = new SimpleInstruction(Instruction.OP_RETURN);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitAnyInstruction(clazz, method, codeAttribute, 0, nop));
        assertDoesNotThrow(() -> marker.visitAnyInstruction(clazz, method, codeAttribute, 0, dup));
        assertDoesNotThrow(() -> marker.visitAnyInstruction(clazz, method, codeAttribute, 0, ret));

        verifyNoInteractions(clazz);
    }

    // ========== visitConstantInstruction Tests ==========

    /**
     * Tests that visitConstantInstruction calls constantPoolEntryAccept for OP_INSTANCEOF.
     * This is the core behavior - instanceof instructions are processed to mark referenced classes.
     */
    @Test
    public void testVisitConstantInstruction_withOpInstanceof_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction does NOT process OP_CHECKCAST instruction.
     * Only OP_INSTANCEOF should be processed.
     */
    @Test
    public void testVisitConstantInstruction_withOpCheckcast_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does NOT process OP_NEW instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpNew_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_NEW, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does NOT process OP_ANEWARRAY instruction.
     */
    @Test
    public void testVisitConstantInstruction_withOpAnewarray_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does NOT process field access instructions.
     */
    @Test
    public void testVisitConstantInstruction_withFieldAccessOpcodes_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction getStatic = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);
        ConstantInstruction putStatic = new ConstantInstruction(Instruction.OP_PUTSTATIC, 2);
        ConstantInstruction getField = new ConstantInstruction(Instruction.OP_GETFIELD, 3);
        ConstantInstruction putField = new ConstantInstruction(Instruction.OP_PUTFIELD, 4);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getField);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putField);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does NOT process method invocation instructions.
     */
    @Test
    public void testVisitConstantInstruction_withMethodInvocationOpcodes_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction invokeVirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 1);
        ConstantInstruction invokeSpecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 2);
        ConstantInstruction invokeStatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 3);
        ConstantInstruction invokeInterface = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 4, 2);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeVirtual);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeSpecial);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeInterface);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction does NOT process LDC instructions.
     */
    @Test
    public void testVisitConstantInstruction_withLdcOpcodes_doesNotCallConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction ldcW = new ConstantInstruction(Instruction.OP_LDC_W, 2);
        ConstantInstruction ldc2W = new ConstantInstruction(Instruction.OP_LDC2_W, 3);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldcW);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc2W);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitConstantInstruction works with different constant indices for OP_INSTANCEOF.
     */
    @Test
    public void testVisitConstantInstruction_withOpInstanceofAndDifferentIndices_usesCorrectIndex() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 0);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 1);
        ConstantInstruction instruction3 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 100);
        ConstantInstruction instruction4 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 65535);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction3);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction4);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(0), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(100), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(65535), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with various offset values.
     * The offset parameter should not affect the behavior.
     */
    @Test
    public void testVisitConstantInstruction_withVariousOffsets_behavesConsistently() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 100, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, -1, instruction);

        // Assert
        verify(clazz, times(4)).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction can be called multiple times with OP_INSTANCEOF.
     */
    @Test
    public void testVisitConstantInstruction_calledMultipleTimesWithOpInstanceof_processesEachCall() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 1);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 2);
        ConstantInstruction instruction3 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 3);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 5, instruction2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction3);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(clazz, times(3)).constantPoolEntryAccept(anyInt(), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with null method parameter.
     */
    @Test
    public void testVisitConstantInstruction_withNullMethod_stillProcessesInstruction() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 5);

        // Act
        marker.visitConstantInstruction(clazz, null, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with null codeAttribute parameter.
     */
    @Test
    public void testVisitConstantInstruction_withNullCodeAttribute_stillProcessesInstruction() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, null, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with null instruction throws NullPointerException.
     */
    @Test
    public void testVisitConstantInstruction_withNullInstruction_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            marker.visitConstantInstruction(clazz, method, codeAttribute, 0, null));
    }

    /**
     * Tests that visitConstantInstruction with same instruction called twice processes both times.
     */
    @Test
    public void testVisitConstantInstruction_sameInstructionCalledTwice_processesBothTimes() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 7);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction);

        // Assert
        verify(clazz, times(2)).constantPoolEntryAccept(eq(7), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction doesn't interact with method or codeAttribute parameters.
     */
    @Test
    public void testVisitConstantInstruction_doesNotInteractWithMethodOrCodeAttribute() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(method);
        verifyNoInteractions(codeAttribute);
    }

    /**
     * Tests that multiple InstanceofClassMarker instances work independently.
     */
    @Test
    public void testVisitConstantInstruction_withMultipleMarkers_workIndependently() {
        // Arrange
        InstanceofClassMarker marker1 = new InstanceofClassMarker();
        InstanceofClassMarker marker2 = new InstanceofClassMarker();
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 1);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 2);

        // Act
        marker1.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker2.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction2);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker1));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker2));
    }

    /**
     * Tests that visitConstantInstruction passes the InstanceofClassMarker itself as the visitor.
     */
    @Test
    public void testVisitConstantInstruction_passesMarkerAsVisitor() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(5), same(marker));
    }

    /**
     * Tests that visitConstantInstruction processes only INSTANCEOF instructions in mixed sequence.
     */
    @Test
    public void testVisitConstantInstruction_withMixedOpcodes_processesOnlyInstanceofInstructions() {
        // Arrange
        ConstantInstruction instanceof1 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 1);
        ConstantInstruction checkcast = new ConstantInstruction(Instruction.OP_CHECKCAST, 2);
        ConstantInstruction instanceof2 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 3);
        ConstantInstruction newInst = new ConstantInstruction(Instruction.OP_NEW, 4);
        ConstantInstruction instanceof3 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instanceof1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, checkcast);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instanceof2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, newInst);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instanceof3);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz, times(3)).constantPoolEntryAccept(anyInt(), eq(marker));
        verify(clazz, never()).constantPoolEntryAccept(eq(2), any());
        verify(clazz, never()).constantPoolEntryAccept(eq(4), any());
    }

    /**
     * Tests that visitConstantInstruction works correctly when called on different clazz instances.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentClazzInstances_accessesEachSeparately() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 5);

        // Act
        marker.visitConstantInstruction(clazz1, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz2, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz1).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz2).constantPoolEntryAccept(eq(5), eq(marker));
    }

    // ========== visitClassConstant Tests ==========

    /**
     * Tests that visitClassConstant calls referencedClassAccept on the ClassConstant.
     * This is the core behavior - the method delegates to the class constant
     * to visit the referenced class through the filtered marker.
     */
    @Test
    public void testVisitClassConstant_callsReferencedClassAccept() {
        // Arrange
        ClassConstant classConstant = mock(ClassConstant.class);

        // Act
        marker.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant passes a ClassVisitor to the class constant.
     */
    @Test
    public void testVisitClassConstant_passesClassVisitor() {
        // Arrange
        ClassConstant classConstant = mock(ClassConstant.class);

        // Act
        marker.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant).referencedClassAccept(isA(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitClassConstant_withValidMocks_doesNotThrowException() {
        // Arrange
        ClassConstant classConstant = mock(ClassConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitClassConstant(clazz, classConstant));
    }

    /**
     * Tests that visitClassConstant can be called with null Clazz parameter.
     */
    @Test
    public void testVisitClassConstant_withNullClazz_doesNotThrowException() {
        // Arrange
        ClassConstant classConstant = mock(ClassConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitClassConstant(null, classConstant));
        verify(classConstant, times(1)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant with null ClassConstant throws NullPointerException.
     */
    @Test
    public void testVisitClassConstant_withNullClassConstant_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> marker.visitClassConstant(clazz, null));
    }

    /**
     * Tests that visitClassConstant can be called multiple times in succession.
     */
    @Test
    public void testVisitClassConstant_calledMultipleTimes_invokesAcceptMethodEachTime() {
        // Arrange
        ClassConstant classConstant = mock(ClassConstant.class);

        // Act
        marker.visitClassConstant(clazz, classConstant);
        marker.visitClassConstant(clazz, classConstant);
        marker.visitClassConstant(clazz, classConstant);

        // Assert
        verify(classConstant, times(3)).referencedClassAccept(any(ClassVisitor.class));
    }

    /**
     * Tests that visitClassConstant doesn't directly interact with the Clazz parameter.
     */
    @Test
    public void testVisitClassConstant_doesNotInteractWithClazz() {
        // Arrange
        ClassConstant classConstant = mock(ClassConstant.class);

        // Act
        marker.visitClassConstant(clazz, classConstant);

        // Assert
        verifyNoInteractions(clazz);
    }

    /**
     * Tests that visitClassConstant works with different ClassConstant instances.
     */
    @Test
    public void testVisitClassConstant_withDifferentClassConstants_callsAcceptOnEach() {
        // Arrange
        ClassConstant classConstant1 = mock(ClassConstant.class);
        ClassConstant classConstant2 = mock(ClassConstant.class);
        ClassConstant classConstant3 = mock(ClassConstant.class);

        // Act
        marker.visitClassConstant(clazz, classConstant1);
        marker.visitClassConstant(clazz, classConstant2);
        marker.visitClassConstant(clazz, classConstant3);

        // Assert
        verify(classConstant1).referencedClassAccept(any(ClassVisitor.class));
        verify(classConstant2).referencedClassAccept(any(ClassVisitor.class));
        verify(classConstant3).referencedClassAccept(any(ClassVisitor.class));
    }

    // ========== visitAnyClass Tests ==========

    /**
     * Tests that visitAnyClass marks a ProgramClass as instanceofed.
     * This is the core marking behavior.
     */
    @Test
    public void testVisitAnyClass_withProgramClass_marksAsInstanceofed() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(InstanceofClassMarker.isInstanceofed(programClass),
                "Class should be marked as instanceofed");
    }

    /**
     * Tests that visitAnyClass marks a LibraryClass as instanceofed.
     */
    @Test
    public void testVisitAnyClass_withLibraryClass_marksAsInstanceofed() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act
        marker.visitAnyClass(libraryClass);

        // Assert
        assertTrue(InstanceofClassMarker.isInstanceofed(libraryClass),
                "Library class should be marked as instanceofed");
    }

    /**
     * Tests that visitAnyClass can be called multiple times on the same class.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_remainsMarked() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);
        marker.visitAnyClass(programClass);
        marker.visitAnyClass(programClass);

        // Assert
        assertTrue(InstanceofClassMarker.isInstanceofed(programClass),
                "Class should remain marked as instanceofed");
    }

    /**
     * Tests that visitAnyClass marks different classes independently.
     */
    @Test
    public void testVisitAnyClass_withDifferentClasses_marksEachIndependently() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(class3);

        // Act
        marker.visitAnyClass(class1);
        marker.visitAnyClass(class2);

        // Assert
        assertTrue(InstanceofClassMarker.isInstanceofed(class1), "Class1 should be marked");
        assertTrue(InstanceofClassMarker.isInstanceofed(class2), "Class2 should be marked");
        assertFalse(InstanceofClassMarker.isInstanceofed(class3), "Class3 should NOT be marked");
    }

    /**
     * Tests that visitAnyClass works correctly when called through ClassVisitor interface.
     */
    @Test
    public void testVisitAnyClass_throughClassVisitorInterface_marksClass() {
        // Arrange
        ClassVisitor visitor = marker;
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        visitor.visitAnyClass(programClass);

        // Assert
        assertTrue(InstanceofClassMarker.isInstanceofed(programClass),
                "Class should be marked when called through interface");
    }

    /**
     * Tests that visitAnyClass can mark many classes in succession.
     */
    @Test
    public void testVisitAnyClass_withManyClasses_marksAllCorrectly() {
        // Arrange
        ProgramClass[] classes = new ProgramClass[100];
        for (int i = 0; i < 100; i++) {
            classes[i] = new ProgramClass();
            ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classes[i]);
        }

        // Act
        for (ProgramClass clazz : classes) {
            marker.visitAnyClass(clazz);
        }

        // Assert
        for (ProgramClass clazz : classes) {
            assertTrue(InstanceofClassMarker.isInstanceofed(clazz),
                    "All classes should be marked as instanceofed");
        }
    }

    // ========== isInstanceofed Tests ==========

    /**
     * Tests that isInstanceofed returns true for a marked class.
     */
    @Test
    public void testIsInstanceofed_withMarkedClass_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        ProgramClassOptimizationInfo.getProgramClassOptimizationInfo(programClass).setInstanceofed();

        // Act
        boolean result = InstanceofClassMarker.isInstanceofed(programClass);

        // Assert
        assertTrue(result, "Should return true for marked class");
    }

    /**
     * Tests that isInstanceofed returns false for an unmarked class.
     */
    @Test
    public void testIsInstanceofed_withUnmarkedClass_returnsFalse() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        boolean result = InstanceofClassMarker.isInstanceofed(programClass);

        // Assert
        assertFalse(result, "Should return false for unmarked class");
    }

    /**
     * Tests that isInstanceofed works correctly after visitAnyClass.
     */
    @Test
    public void testIsInstanceofed_afterVisitAnyClass_returnsTrue() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        marker.visitAnyClass(programClass);
        boolean result = InstanceofClassMarker.isInstanceofed(programClass);

        // Assert
        assertTrue(result, "Should return true after visitAnyClass");
    }

    /**
     * Tests that isInstanceofed can distinguish between marked and unmarked classes.
     */
    @Test
    public void testIsInstanceofed_distinguishesBetweenMarkedAndUnmarked() {
        // Arrange
        ProgramClass markedClass = new ProgramClass();
        ProgramClass unmarkedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(markedClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(unmarkedClass);
        marker.visitAnyClass(markedClass);

        // Act & Assert
        assertTrue(InstanceofClassMarker.isInstanceofed(markedClass),
                "Marked class should return true");
        assertFalse(InstanceofClassMarker.isInstanceofed(unmarkedClass),
                "Unmarked class should return false");
    }

    /**
     * Tests that isInstanceofed works with LibraryClass.
     */
    @Test
    public void testIsInstanceofed_withLibraryClass_worksCorrectly() {
        // Arrange
        LibraryClass markedLibraryClass = new LibraryClass();
        LibraryClass unmarkedLibraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(markedLibraryClass);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(unmarkedLibraryClass);
        marker.visitAnyClass(markedLibraryClass);

        // Act & Assert
        assertTrue(InstanceofClassMarker.isInstanceofed(markedLibraryClass),
                "Marked library class should return true");
        assertFalse(InstanceofClassMarker.isInstanceofed(unmarkedLibraryClass),
                "Unmarked library class should return false");
    }

    /**
     * Tests that isInstanceofed is consistent across multiple calls.
     */
    @Test
    public void testIsInstanceofed_consistentAcrossMultipleCalls() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        marker.visitAnyClass(programClass);

        // Act
        boolean result1 = InstanceofClassMarker.isInstanceofed(programClass);
        boolean result2 = InstanceofClassMarker.isInstanceofed(programClass);
        boolean result3 = InstanceofClassMarker.isInstanceofed(programClass);

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
    }

    // ========== Integration Tests ==========

    /**
     * Integration test: Tests the complete flow from instruction to class marking.
     * Tests that when an instanceof instruction is visited, the referenced class gets marked.
     */
    @Test
    public void testIntegration_instanceofInstructionMarksReferencedClass() {
        // Arrange
        ProgramClass containingClass = new ProgramClass();
        containingClass.u2constantPoolCount = 10;

        ProgramClass referencedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        ClassConstant classConstant = new ClassConstant();
        classConstant.referencedClass = referencedClass;

        containingClass.constantPool = new Constant[10];
        containingClass.constantPool[5] = classConstant;

        ConstantInstruction instanceofInstruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 5);

        // Initially, the referenced class should not be marked
        assertFalse(InstanceofClassMarker.isInstanceofed(referencedClass),
                "Referenced class should not be marked initially");

        // Act - visit the instanceof instruction
        marker.visitConstantInstruction(containingClass, method, codeAttribute, 0, instanceofInstruction);

        // Assert - the referenced class should now be marked
        assertTrue(InstanceofClassMarker.isInstanceofed(referencedClass),
                "Referenced class should be marked after visiting instanceof instruction");
    }

    /**
     * Integration test: Tests that non-instanceof instructions don't mark classes.
     */
    @Test
    public void testIntegration_nonInstanceofInstructionsDoNotMarkClasses() {
        // Arrange
        ProgramClass containingClass = new ProgramClass();
        containingClass.u2constantPoolCount = 10;

        ProgramClass referencedClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass);

        ClassConstant classConstant = new ClassConstant();
        classConstant.referencedClass = referencedClass;

        containingClass.constantPool = new Constant[10];
        containingClass.constantPool[5] = classConstant;

        ConstantInstruction checkcastInstruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 5);
        ConstantInstruction newInstruction = new ConstantInstruction(Instruction.OP_NEW, 5);

        // Act - visit non-instanceof instructions
        marker.visitConstantInstruction(containingClass, method, codeAttribute, 0, checkcastInstruction);
        marker.visitConstantInstruction(containingClass, method, codeAttribute, 0, newInstruction);

        // Assert - the referenced class should NOT be marked
        assertFalse(InstanceofClassMarker.isInstanceofed(referencedClass),
                "Referenced class should NOT be marked by non-instanceof instructions");
    }

    /**
     * Integration test: Tests that multiple instanceof instructions mark multiple classes.
     */
    @Test
    public void testIntegration_multipleInstanceofInstructionsMarkMultipleClasses() {
        // Arrange
        ProgramClass containingClass = new ProgramClass();
        containingClass.u2constantPoolCount = 20;
        containingClass.constantPool = new Constant[20];

        ProgramClass referencedClass1 = new ProgramClass();
        ProgramClass referencedClass2 = new ProgramClass();
        ProgramClass referencedClass3 = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(referencedClass3);

        ClassConstant classConstant1 = new ClassConstant();
        classConstant1.referencedClass = referencedClass1;
        ClassConstant classConstant2 = new ClassConstant();
        classConstant2.referencedClass = referencedClass2;
        ClassConstant classConstant3 = new ClassConstant();
        classConstant3.referencedClass = referencedClass3;

        containingClass.constantPool[1] = classConstant1;
        containingClass.constantPool[2] = classConstant2;
        containingClass.constantPool[3] = classConstant3;

        ConstantInstruction instanceof1 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 1);
        ConstantInstruction instanceof2 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 2);
        ConstantInstruction instanceof3 = new ConstantInstruction(Instruction.OP_INSTANCEOF, 3);

        // Act
        marker.visitConstantInstruction(containingClass, method, codeAttribute, 0, instanceof1);
        marker.visitConstantInstruction(containingClass, method, codeAttribute, 0, instanceof2);
        marker.visitConstantInstruction(containingClass, method, codeAttribute, 0, instanceof3);

        // Assert
        assertTrue(InstanceofClassMarker.isInstanceofed(referencedClass1),
                "First referenced class should be marked");
        assertTrue(InstanceofClassMarker.isInstanceofed(referencedClass2),
                "Second referenced class should be marked");
        assertTrue(InstanceofClassMarker.isInstanceofed(referencedClass3),
                "Third referenced class should be marked");
    }
}
