package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReadWriteFieldMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)}.
 *
 * The visitConstantInstruction method processes field access instructions (GETSTATIC, GETFIELD, PUTSTATIC, PUTFIELD)
 * and LDC instructions that may reference fields. It sets internal reading/writing flags and then calls
 * clazz.constantPoolEntryAccept() to process the constant, which eventually leads to marking fields as read or written.
 *
 * Key behaviors:
 * - OP_LDC, OP_LDC_W: Sets reading=true, writing=true (field may be read and written)
 * - OP_GETSTATIC, OP_GETFIELD: Sets reading=true, writing=false (field is read)
 * - OP_PUTSTATIC, OP_PUTFIELD: Sets reading=false, writing=true (field is written)
 * - Other opcodes: No action taken (method returns without doing anything)
 */
public class ReadWriteFieldMarkerClaude_visitConstantInstructionTest {

    private ReadWriteFieldMarker marker;
    private MutableBoolean repeatTrigger;
    private Clazz clazz;
    private Method method;
    private CodeAttribute codeAttribute;

    @BeforeEach
    public void setUp() {
        repeatTrigger = new MutableBoolean();
        marker = new ReadWriteFieldMarker(repeatTrigger);
        clazz = mock(ProgramClass.class);
        method = mock(ProgramMethod.class);
        codeAttribute = mock(CodeAttribute.class);
    }

    // =========================================================================
    // Tests for OP_GETSTATIC - Read field access (static)
    // =========================================================================

    /**
     * Tests that visitConstantInstruction with OP_GETSTATIC calls constantPoolEntryAccept.
     * This verifies the method processes static field reads.
     */
    @Test
    public void testVisitConstantInstruction_withGetStatic_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 5);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with OP_GETSTATIC works with different constant indices.
     */
    @Test
    public void testVisitConstantInstruction_withGetStaticDifferentIndices_accessesCorrectEntries() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);
        ConstantInstruction instruction10 = new ConstantInstruction(Instruction.OP_GETSTATIC, 10);
        ConstantInstruction instruction100 = new ConstantInstruction(Instruction.OP_GETSTATIC, 100);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction10);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction100);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(10), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(100), eq(marker));
    }

    // =========================================================================
    // Tests for OP_GETFIELD - Read field access (instance)
    // =========================================================================

    /**
     * Tests that visitConstantInstruction with OP_GETFIELD calls constantPoolEntryAccept.
     * This verifies the method processes instance field reads.
     */
    @Test
    public void testVisitConstantInstruction_withGetField_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 7);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(7), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with OP_GETFIELD works with different constant indices.
     */
    @Test
    public void testVisitConstantInstruction_withGetFieldDifferentIndices_accessesCorrectEntries() {
        // Arrange
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_GETFIELD, 2);
        ConstantInstruction instruction20 = new ConstantInstruction(Instruction.OP_GETFIELD, 20);
        ConstantInstruction instruction200 = new ConstantInstruction(Instruction.OP_GETFIELD, 200);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction2);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction20);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction200);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(20), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(200), eq(marker));
    }

    // =========================================================================
    // Tests for OP_PUTSTATIC - Write field access (static)
    // =========================================================================

    /**
     * Tests that visitConstantInstruction with OP_PUTSTATIC calls constantPoolEntryAccept.
     * This verifies the method processes static field writes.
     */
    @Test
    public void testVisitConstantInstruction_withPutStatic_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTSTATIC, 3);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with OP_PUTSTATIC works with different constant indices.
     */
    @Test
    public void testVisitConstantInstruction_withPutStaticDifferentIndices_accessesCorrectEntries() {
        // Arrange
        ConstantInstruction instruction3 = new ConstantInstruction(Instruction.OP_PUTSTATIC, 3);
        ConstantInstruction instruction30 = new ConstantInstruction(Instruction.OP_PUTSTATIC, 30);
        ConstantInstruction instruction300 = new ConstantInstruction(Instruction.OP_PUTSTATIC, 300);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction3);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction30);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction300);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(30), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(300), eq(marker));
    }

    // =========================================================================
    // Tests for OP_PUTFIELD - Write field access (instance)
    // =========================================================================

    /**
     * Tests that visitConstantInstruction with OP_PUTFIELD calls constantPoolEntryAccept.
     * This verifies the method processes instance field writes.
     */
    @Test
    public void testVisitConstantInstruction_withPutField_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 9);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(9), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with OP_PUTFIELD works with different constant indices.
     */
    @Test
    public void testVisitConstantInstruction_withPutFieldDifferentIndices_accessesCorrectEntries() {
        // Arrange
        ConstantInstruction instruction4 = new ConstantInstruction(Instruction.OP_PUTFIELD, 4);
        ConstantInstruction instruction40 = new ConstantInstruction(Instruction.OP_PUTFIELD, 40);
        ConstantInstruction instruction400 = new ConstantInstruction(Instruction.OP_PUTFIELD, 400);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction4);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction40);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction400);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(4), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(40), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(400), eq(marker));
    }

    // =========================================================================
    // Tests for OP_LDC - Load constant (may reference field)
    // =========================================================================

    /**
     * Tests that visitConstantInstruction with OP_LDC calls constantPoolEntryAccept.
     * LDC can load field references and marks them as both read and written.
     */
    @Test
    public void testVisitConstantInstruction_withLdc_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC, 15);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(15), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with OP_LDC works with different constant indices.
     */
    @Test
    public void testVisitConstantInstruction_withLdcDifferentIndices_accessesCorrectEntries() {
        // Arrange
        ConstantInstruction instruction5 = new ConstantInstruction(Instruction.OP_LDC, 5);
        ConstantInstruction instruction50 = new ConstantInstruction(Instruction.OP_LDC, 50);
        ConstantInstruction instruction255 = new ConstantInstruction(Instruction.OP_LDC, 255);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction5);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction50);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction255);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(50), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(255), eq(marker));
    }

    // =========================================================================
    // Tests for OP_LDC_W - Load constant (wide index)
    // =========================================================================

    /**
     * Tests that visitConstantInstruction with OP_LDC_W calls constantPoolEntryAccept.
     * LDC_W is like LDC but uses a 16-bit index.
     */
    @Test
    public void testVisitConstantInstruction_withLdcW_callsConstantPoolEntryAccept() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_LDC_W, 500);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(500), eq(marker));
    }

    /**
     * Tests that visitConstantInstruction with OP_LDC_W works with different constant indices.
     */
    @Test
    public void testVisitConstantInstruction_withLdcWDifferentIndices_accessesCorrectEntries() {
        // Arrange
        ConstantInstruction instruction256 = new ConstantInstruction(Instruction.OP_LDC_W, 256);
        ConstantInstruction instruction1000 = new ConstantInstruction(Instruction.OP_LDC_W, 1000);
        ConstantInstruction instruction65535 = new ConstantInstruction(Instruction.OP_LDC_W, 65535);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction256);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1000);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction65535);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(256), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(1000), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(65535), eq(marker));
    }

    // =========================================================================
    // Tests for non-field-related opcodes (should be no-op)
    // =========================================================================

    /**
     * Tests that visitConstantInstruction with non-field opcodes doesn't call constantPoolEntryAccept.
     * Instructions that don't involve field access should be ignored.
     */
    @Test
    public void testVisitConstantInstruction_withNonFieldOpcodes_doesNotAccessConstantPool() {
        // Arrange - instructions that don't involve fields
        ConstantInstruction ldc2W = new ConstantInstruction(Instruction.OP_LDC2_W, 1);
        ConstantInstruction invokeVirtual = new ConstantInstruction(Instruction.OP_INVOKEVIRTUAL, 2);
        ConstantInstruction invokeSpecial = new ConstantInstruction(Instruction.OP_INVOKESPECIAL, 3);
        ConstantInstruction invokeStatic = new ConstantInstruction(Instruction.OP_INVOKESTATIC, 4);
        ConstantInstruction invokeInterface = new ConstantInstruction(Instruction.OP_INVOKEINTERFACE, 5);
        ConstantInstruction newInst = new ConstantInstruction(Instruction.OP_NEW, 6);
        ConstantInstruction anewarray = new ConstantInstruction(Instruction.OP_ANEWARRAY, 7);
        ConstantInstruction checkcast = new ConstantInstruction(Instruction.OP_CHECKCAST, 8);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc2W);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeVirtual);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeSpecial);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, invokeInterface);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, newInst);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, anewarray);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, checkcast);

        // Assert - verify no interactions with constant pool
        verifyNoInteractions(clazz);
    }

    // =========================================================================
    // Tests for mixed field access opcodes
    // =========================================================================

    /**
     * Tests that visitConstantInstruction handles a sequence of different field access instructions.
     */
    @Test
    public void testVisitConstantInstruction_withMixedFieldOpcodes_processesAllCorrectly() {
        // Arrange
        ConstantInstruction getStatic = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);
        ConstantInstruction getField = new ConstantInstruction(Instruction.OP_GETFIELD, 2);
        ConstantInstruction putStatic = new ConstantInstruction(Instruction.OP_PUTSTATIC, 3);
        ConstantInstruction putField = new ConstantInstruction(Instruction.OP_PUTFIELD, 4);
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 5);
        ConstantInstruction ldcW = new ConstantInstruction(Instruction.OP_LDC_W, 6);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 1, getField);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 2, putStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 3, putField);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 4, ldc);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 5, ldcW);

        // Assert - verify all were processed
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(4), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(6), eq(marker));
    }

    // =========================================================================
    // Tests for multiple calls to same instruction
    // =========================================================================

    /**
     * Tests that visitConstantInstruction can be called multiple times with the same instruction.
     */
    @Test
    public void testVisitConstantInstruction_calledMultipleTimesWithSameInstruction_accessesConstantPoolEachTime() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 10);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 10, instruction);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 20, instruction);

        // Assert
        verify(clazz, times(3)).constantPoolEntryAccept(eq(10), eq(marker));
    }

    // =========================================================================
    // Tests with different offsets
    // =========================================================================

    /**
     * Tests that visitConstantInstruction works with various offset values.
     * The offset doesn't affect the processing of field instructions.
     */
    @Test
    public void testVisitConstantInstruction_withVariousOffsets_worksCorrectly() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction));
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, 100, instruction));
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, -1, instruction));
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, Integer.MAX_VALUE, instruction));
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, codeAttribute, Integer.MIN_VALUE, instruction));
    }

    // =========================================================================
    // Tests with constant index 0
    // =========================================================================

    /**
     * Tests that visitConstantInstruction with constant index 0 works.
     */
    @Test
    public void testVisitConstantInstruction_withConstantIndex0_accessesConstantPool() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 0);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(0), eq(marker));
    }

    // =========================================================================
    // Tests with different constructor configurations
    // =========================================================================

    /**
     * Tests that visitConstantInstruction works with a marker configured for read-only.
     */
    @Test
    public void testVisitConstantInstruction_withReadOnlyMarker_stillProcessesInstructions() {
        // Arrange
        MutableBoolean trigger = new MutableBoolean();
        ReadWriteFieldMarker readOnlyMarker = new ReadWriteFieldMarker(trigger, true, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 5);

        // Act
        readOnlyMarker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(5), eq(readOnlyMarker));
    }

    /**
     * Tests that visitConstantInstruction works with a marker configured for write-only.
     */
    @Test
    public void testVisitConstantInstruction_withWriteOnlyMarker_stillProcessesInstructions() {
        // Arrange
        MutableBoolean trigger = new MutableBoolean();
        ReadWriteFieldMarker writeOnlyMarker = new ReadWriteFieldMarker(trigger, false, true);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_PUTFIELD, 7);

        // Act
        writeOnlyMarker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(7), eq(writeOnlyMarker));
    }

    /**
     * Tests that visitConstantInstruction works with a marker configured with both flags disabled.
     */
    @Test
    public void testVisitConstantInstruction_withBothFlagsDisabled_stillProcessesInstructions() {
        // Arrange
        MutableBoolean trigger = new MutableBoolean();
        ReadWriteFieldMarker disabledMarker = new ReadWriteFieldMarker(trigger, false, false);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETSTATIC, 9);

        // Act
        disabledMarker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(9), eq(disabledMarker));
    }

    // =========================================================================
    // Tests for null parameters
    // =========================================================================

    /**
     * Tests that visitConstantInstruction with null Clazz throws NullPointerException.
     * The method needs to call methods on the clazz parameter, so null should cause NPE.
     */
    @Test
    public void testVisitConstantInstruction_withNullClazz_throwsNullPointerException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> marker.visitConstantInstruction(null, method, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction with null Method works (method parameter not used).
     */
    @Test
    public void testVisitConstantInstruction_withNullMethod_doesNotThrowException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, null, codeAttribute, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction with null CodeAttribute works (codeAttribute parameter not used).
     */
    @Test
    public void testVisitConstantInstruction_withNullCodeAttribute_doesNotThrowException() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);

        // Act & Assert
        assertDoesNotThrow(() -> marker.visitConstantInstruction(clazz, method, null, 0, instruction));
    }

    /**
     * Tests that visitConstantInstruction with null ConstantInstruction throws NullPointerException.
     * The method needs to access the instruction's opcode, so null should cause NPE.
     */
    @Test
    public void testVisitConstantInstruction_withNullInstruction_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> marker.visitConstantInstruction(clazz, method, codeAttribute, 0, null));
    }

    // =========================================================================
    // Tests for performance
    // =========================================================================

    /**
     * Tests that visitConstantInstruction executes quickly with many calls.
     */
    @Test
    public void testVisitConstantInstruction_executesQuickly() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);
        long startTime = System.nanoTime();

        // Act
        for (int i = 0; i < 1000; i++) {
            marker.visitConstantInstruction(clazz, method, codeAttribute, i, instruction);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete quickly (within 100ms for 1000 calls)
        assertTrue(durationMs < 100, "visitConstantInstruction should execute quickly");
    }

    // =========================================================================
    // Tests for thread safety
    // =========================================================================

    /**
     * Tests that visitConstantInstruction can be called concurrently by multiple threads.
     * Note: The marker has internal state (reading/writing flags), so concurrent calls
     * from the same marker instance may have race conditions, but shouldn't crash.
     */
    @Test
    public void testVisitConstantInstruction_concurrentCalls_doesNotCrash() throws InterruptedException {
        // Arrange
        final int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);

        // Act - create threads that call visitConstantInstruction
        for (int i = 0; i < threadCount; i++) {
            final int offset = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    marker.visitConstantInstruction(clazz, method, codeAttribute, offset, instruction);
                }
            });
            threads[i].start();
        }

        // Wait for completion
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert - verify constantPoolEntryAccept was called (should be 1000 times)
        verify(clazz, atLeast(1000)).constantPoolEntryAccept(eq(1), eq(marker));
    }

    // =========================================================================
    // Tests for all field-related opcodes coverage
    // =========================================================================

    /**
     * Tests that all four field access opcodes are handled correctly.
     */
    @Test
    public void testVisitConstantInstruction_allFieldOpcodes_processed() {
        // Arrange
        ConstantInstruction getStatic = new ConstantInstruction(Instruction.OP_GETSTATIC, 1);
        ConstantInstruction getField = new ConstantInstruction(Instruction.OP_GETFIELD, 2);
        ConstantInstruction putStatic = new ConstantInstruction(Instruction.OP_PUTSTATIC, 3);
        ConstantInstruction putField = new ConstantInstruction(Instruction.OP_PUTFIELD, 4);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, getField);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putStatic);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, putField);

        // Assert - verify each was processed
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(3), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(4), eq(marker));
    }

    /**
     * Tests that both LDC opcodes are handled correctly.
     */
    @Test
    public void testVisitConstantInstruction_allLdcOpcodes_processed() {
        // Arrange
        ConstantInstruction ldc = new ConstantInstruction(Instruction.OP_LDC, 1);
        ConstantInstruction ldcW = new ConstantInstruction(Instruction.OP_LDC_W, 2);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldc);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, ldcW);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(1), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(2), eq(marker));
    }

    // =========================================================================
    // Tests for rapid succession
    // =========================================================================

    /**
     * Tests that visitConstantInstruction can be called in rapid succession.
     */
    @Test
    public void testVisitConstantInstruction_rapidSuccession_noIssues() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                marker.visitConstantInstruction(clazz, method, codeAttribute, i, instruction);
            }
        });

        // Verify it was called many times
        verify(clazz, times(1000)).constantPoolEntryAccept(eq(1), eq(marker));
    }

    // =========================================================================
    // Tests for different Clazz instances
    // =========================================================================

    /**
     * Tests that visitConstantInstruction works with different Clazz instances.
     */
    @Test
    public void testVisitConstantInstruction_withDifferentClazzInstances_callsEachClazz() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        Clazz clazz3 = mock(ProgramClass.class);
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 5);

        // Act
        marker.visitConstantInstruction(clazz1, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz2, method, codeAttribute, 0, instruction);
        marker.visitConstantInstruction(clazz3, method, codeAttribute, 0, instruction);

        // Assert
        verify(clazz1).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz2).constantPoolEntryAccept(eq(5), eq(marker));
        verify(clazz3).constantPoolEntryAccept(eq(5), eq(marker));
    }

    // =========================================================================
    // Tests that Method and CodeAttribute parameters are not used
    // =========================================================================

    /**
     * Tests that the Method parameter is not accessed by visitConstantInstruction.
     */
    @Test
    public void testVisitConstantInstruction_doesNotInteractWithMethod() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(method);
    }

    /**
     * Tests that the CodeAttribute parameter is not accessed by visitConstantInstruction.
     */
    @Test
    public void testVisitConstantInstruction_doesNotInteractWithCodeAttribute() {
        // Arrange
        ConstantInstruction instruction = new ConstantInstruction(Instruction.OP_GETFIELD, 1);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction);

        // Assert
        verifyNoInteractions(codeAttribute);
    }

    // =========================================================================
    // Edge case: Very large constant indices
    // =========================================================================

    /**
     * Tests that visitConstantInstruction works with very large constant indices.
     */
    @Test
    public void testVisitConstantInstruction_withLargeConstantIndices_worksCorrectly() {
        // Arrange
        ConstantInstruction instruction1 = new ConstantInstruction(Instruction.OP_GETFIELD, 32767);
        ConstantInstruction instruction2 = new ConstantInstruction(Instruction.OP_PUTFIELD, 65535);

        // Act
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction1);
        marker.visitConstantInstruction(clazz, method, codeAttribute, 0, instruction2);

        // Assert
        verify(clazz).constantPoolEntryAccept(eq(32767), eq(marker));
        verify(clazz).constantPoolEntryAccept(eq(65535), eq(marker));
    }
}
