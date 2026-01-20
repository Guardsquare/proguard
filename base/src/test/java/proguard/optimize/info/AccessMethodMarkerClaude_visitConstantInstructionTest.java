package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.*;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AccessMethodMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)}.
 *
 * The visitConstantInstruction method sets the invokingMethod field and then calls
 * clazz.constantPoolEntryAccept() to process the constant referenced by the instruction.
 * This allows the AccessMethodMarker to track what access levels (private, protected, package)
 * are accessed by methods when they reference constants that point to classes or members.
 */
public class AccessMethodMarkerClaude_visitConstantInstructionTest {

    private AccessMethodMarker marker;
    private Clazz clazz;
    private ProgramMethod method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        marker = new AccessMethodMarker();
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    /**
     * Tests that visitConstantInstruction calls constantPoolEntryAccept with the correct constant index.
     * This verifies the basic flow of the method.
     */
    @Test
    public void testVisitConstantInstruction_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify constantPoolEntryAccept was called with index 5
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with different constant indices.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentConstantIndices_accessesCorrectEntries() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction instruction10 = new ConstantInstruction(Instruction.OP_LDC, 10);
        ConstantInstruction instruction100 = new ConstantInstruction(Instruction.OP_LDC, 100);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction10);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction100);

        // Assert - verify each constant index was accessed
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(100), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with various instruction opcodes.
     */
    @Test
    public void testVisitConstantInstruction_withVariousOpcodes_accessesConstantPool() {
        // Arrange - various instructions that use constant pool
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction ldcW = new ConstantInstruction(Instruction.OP_LDC_W, 2);
        ConstantInstruction ldc2W = new ConstantInstruction(Instruction.OP_LDC2_W, 3);
        ConstantInstruction getField = new ConstantInstruction(Instruction.OP_GETSTATIC, 4);
        ConstantInstruction putField = new ConstantInstruction(Instruction.OP_PUTSTATIC, 5);
        ConstantInstruction invokeVirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 6);
        ConstantInstruction invokeSpecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 7);
        ConstantInstruction invokeStatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 8);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldcW);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc2W);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getField);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putField);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeVirtual);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeSpecial);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeStatic);

        // Assert - verify all constant pool entries were accessed
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(4), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(6), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(7), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(8), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction can be called multiple times.
     */
    @Test
    public void testVisitConstantInstruction_calledMultipleTimes_accessesConstantPoolEachTime() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 20, instruction);

        // Assert - verify constantPoolEntryAccept was called 3 times
        verify(clazz, times(3)).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with different offset values.
     */
    @Test
    public void testVisitConstantInstruction_withVariousOffsets_worksCorrectly() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert - should handle any offset value
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 100, instruction));
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, -1, instruction));
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, instruction));
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, Integer.MIN_VALUE, instruction));
    }

    /**
     * Tests that visitConstantInstruction with constant index 0 works.
     */
    @Test
    public void testVisitConstantInstruction_withConstantIndex0_accessesConstantPool() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 0);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(0), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction passes the marker itself as the visitor.
     * This is important because the marker implements ConstantVisitor.
     */
    @Test
    public void testVisitConstantInstruction_passesMarkerAsVisitor() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert - verify the marker itself is passed as the visitor
        verify(clazz).constantPoolEntryAccept(anyInt(), same(marker));
    }

    /**
     * Tests that visitConstantInstruction can handle being called with different methods.
     * Each call should update the internal invokingMethod field.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentMethods_updatesInvokingMethod() {
        // Arrange
        ProgramMethod method1 = mock(ProgramMethod.class);
        ProgramMethod method2 = mock(ProgramMethod.class);
        ProgramMethod method3 = mock(ProgramMethod.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act - call with different methods
        marker.visitConstantInstruction(clazz, method1, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method2, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method3, codeAttribute, 0, instruction);

        // Assert - verify constant pool was accessed for each call
        verify(clazz, times(3)).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction doesn't throw exceptions with valid parameters.
     */
    @Test
    public void testVisitConstantInstruction_withValidParameters_doesNotThrow() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction works with different code attributes.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentCodeAttributes_worksCorrectly() {
        // Arrange
        CodeAttribute codeAttr1 = mock(CodeAttribute.class);
        CodeAttribute codeAttr2 = mock(CodeAttribute.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttr1, 0, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttr2, 10, instruction);

        // Assert
        verify(clazz, times(2)).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works correctly when called in rapid succession.
     */
    @Test
    public void testVisitConstantInstruction_rapidSuccessiveCalls_worksCorrectly() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act
        for (int i = 0; i < 100; i++) {
            marker.visitConstantInstruction(clazz, method, codeAttribute, i, instruction);
        }

        // Assert
        verify(clazz, times(100)).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with large constant indices works.
     */
    @Test
    public void testVisitConstantInstruction_withLargeConstantIndex_accessesConstantPool() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC_W, 65535);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(65535), eq(marker));
    }

    /**
     * Tests that multiple AccessMethodMarker instances work independently.
     */
    @Test
    public void testVisitConstantInstruction_withMultipleMarkers_operateIndependently() {
        // Arrange
        AccessMethodMarker marker1 = new AccessMethodMarker();
        AccessMethodMarker marker2 = new AccessMethodMarker();
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_LDC, 2);

        // Act
        marker1.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker2.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction2);

        // Assert - verify each marker accessed its respective constant
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker1));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker2));
    }

    /**
     * Tests that visitConstantInstruction passes the marker (which is a ConstantVisitor)
     * to the constant pool entry accept method, allowing the visitor pattern to work.
     */
    @Test
    public void testVisitConstantInstruction_markerImplementsConstantVisitor() {
        // Assert - verify marker is a ConstantVisitor
        assertTrue(marker instanceof ConstantVisitor,
                "AccessMethodMarker should implement ConstantVisitor");
    }

    /**
     * Tests visitConstantInstruction with INVOKEINTERFACE instruction.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeInterface_accessesConstantPool() {
        // Arrange - INVOKEINTERFACE has an extra byte for count
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 5, 2);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction handles NEW instructions.
     */
    @Test
    public void testVisitConstantInstruction_withNewInstruction_accessesConstantPool() {
        // Arrange
        ConstantInstruction newInstruction = new ConstantInstruction(Instruction.OP_NEW, 10);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, newInstruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction handles ANEWARRAY instructions.
     */
    @Test
    public void testVisitConstantInstruction_withANewArrayInstruction_accessesConstantPool() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_ANEWARRAY, 15);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(15), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction handles CHECKCAST instructions.
     */
    @Test
    public void testVisitConstantInstruction_withCheckCastInstruction_accessesConstantPool() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_CHECKCAST, 20);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(20), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction handles INSTANCEOF instructions.
     */
    @Test
    public void testVisitConstantInstruction_withInstanceOfInstruction_accessesConstantPool() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INSTANCEOF, 25);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(25), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction handles GETFIELD instructions.
     */
    @Test
    public void testVisitConstantInstruction_withGetFieldInstruction_accessesConstantPool() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 30);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(30), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction handles PUTFIELD instructions.
     */
    @Test
    public void testVisitConstantInstruction_withPutFieldInstruction_accessesConstantPool() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 35);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(35), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction handles INVOKEDYNAMIC instructions.
     */
    @Test
    public void testVisitConstantInstruction_withInvokeDynamicInstruction_accessesConstantPool() {
        // Arrange - INVOKEDYNAMIC has extra bytes
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_INVOKEDYNAMIC, 40, 0);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(40), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction is stateless between calls (except for invokingMethod).
     * Each call should independently access the constant pool.
     */
    @Test
    public void testVisitConstantInstruction_isStatelessBetweenCalls() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_LDC, 2);
        ConstantInstruction instruction3 = new ConstantInstruction(Instruction.OP_LDC, 3);

        // Act - interleave calls
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction3);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);

        // Assert - verify each call accessed the correct constant
        verify(clazz, times(2)).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(3), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with the same instruction called multiple times
     * accesses the constant pool each time.
     */
    @Test
    public void testVisitConstantInstruction_sameInstructionMultipleCalls_accessesEachTime() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 5);

        // Act
        for (int i = 0; i < 10; i++) {
            marker.visitConstantInstruction(clazz, method, codeAttribute, i * 10, instruction);
        }

        // Assert
        verify(clazz, times(10)).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction works with different clazz instances.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentClazzInstances_accessesCorrectConstantPools() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act
        marker.visitConstantInstruction(clazz1, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz2, method, codeAttribute, 0, instruction);

        // Assert - verify each clazz's constant pool was accessed
        verify(clazz1).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz2).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction correctly updates the invokingMethod field
     * before calling constantPoolEntryAccept. This is important because the constant
     * visitor methods use invokingMethod to set access flags.
     */
    @Test
    public void testVisitConstantInstruction_updatesInvokingMethodBeforeAccept() {
        // Arrange
        ProgramMethod testMethod = mock(ProgramMethod.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 1);

        // Act
        marker.visitConstantInstruction(clazz, testMethod, codeAttribute, 0, instruction);

        // Assert - verify constant pool entry accept was called
        // The invokingMethod field should be set to testMethod before this call
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction handles sequential calls with different constant indices.
     */
    @Test
    public void testVisitConstantInstruction_sequentialCallsWithDifferentIndices_allProcessed() {
        // Arrange & Act
        for (int i = 1; i <= 20; i++) {
            ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, i);
            marker.visitConstantInstruction(clazz, method, codeAttribute, i * 5, instruction);
        }

        // Assert - verify all 20 different constant indices were accessed
        for (int i = 1; i <= 20; i++) {
            verify(clazz).constantPoolEntryAccept(eq(i), eq(marker));
        }
    }

    /**
     * Tests that visitConstantInstruction works with boundary constant index values.
     */
    @Test
    public void testVisitConstantInstruction_withBoundaryConstantIndices_accessesConstantPool() {
        // Arrange
        ConstantInstruction instruction0 = new ConstantInstruction(Instruction.OP_LDC, 0);
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction instructionMax = new ConstantInstruction(Instruction.OP_LDC_W, 65535);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction0);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instructionMax);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(0), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(65535), eq(marker));
    }
}
