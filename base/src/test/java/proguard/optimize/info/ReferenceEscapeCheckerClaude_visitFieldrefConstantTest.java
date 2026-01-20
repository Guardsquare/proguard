package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReferenceEscapeChecker#visitFieldrefConstant(Clazz, FieldrefConstant)}.
 *
 * The visitFieldrefConstant method processes field reference constants by delegating to the
 * ClassConstant referenced by the FieldrefConstant. This allows the checker to continue
 * visiting the constant pool entry that represents the class containing the field.
 *
 * Method behavior (line 291-294):
 * - Calls: clazz.constantPoolEntryAccept(fieldrefConstant.u2classIndex, this)
 * - This delegates to visiting the class constant at the specified index
 * - Passes itself (the ReferenceEscapeChecker) as the ConstantVisitor
 * - The u2classIndex field in FieldrefConstant points to the ClassConstant in the constant pool
 *
 * This method is part of the ConstantVisitor pattern, allowing the checker to traverse
 * from a field reference to the class that contains that field.
 */
public class ReferenceEscapeCheckerClaude_visitFieldrefConstantTest {

    private ReferenceEscapeChecker checker;
    private Clazz clazz;
    private FieldrefConstant fieldrefConstant;

    @BeforeEach
    public void setUp() {
        checker = new ReferenceEscapeChecker();
        clazz = mock(ProgramClass.class);
        fieldrefConstant = new FieldrefConstant();
    }

    /**
     * Tests that visitFieldrefConstant delegates to the constant pool entry accept method.
     * The method should call constantPoolEntryAccept with the class index from the fieldref.
     */
    @Test
    public void testVisitFieldrefConstant_delegatesToConstantPoolEntry() {
        // Arrange
        int classIndex = 42;
        fieldrefConstant.u2classIndex = classIndex;

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify that constantPoolEntryAccept was called with the correct index
        verify(clazz, times(1)).constantPoolEntryAccept(eq(classIndex), any(ConstantVisitor.class));
    }

    /**
     * Tests that visitFieldrefConstant passes itself as the ConstantVisitor.
     * This ensures the checker continues to be used for visiting the referenced class constant.
     */
    @Test
    public void testVisitFieldrefConstant_passesSelfAsVisitor() {
        // Arrange
        int classIndex = 10;
        fieldrefConstant.u2classIndex = classIndex;

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify that the checker itself is passed as the visitor
        verify(clazz, times(1)).constantPoolEntryAccept(eq(classIndex), eq(checker));
    }

    /**
     * Tests that visitFieldrefConstant can be called with valid objects without throwing exceptions.
     */
    @Test
    public void testVisitFieldrefConstant_withValidObjects_doesNotThrowException() {
        // Arrange
        fieldrefConstant.u2classIndex = 5;

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.visitFieldrefConstant(clazz, fieldrefConstant));
    }

    /**
     * Tests that visitFieldrefConstant works with different class indices.
     * The method should handle various valid constant pool indices.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentClassIndices_callsCorrectIndex() {
        // Arrange & Act & Assert
        int[] indices = {0, 1, 5, 10, 50, 100, 255, 1000};

        for (int index : indices) {
            fieldrefConstant.u2classIndex = index;
            checker.visitFieldrefConstant(clazz, fieldrefConstant);
            verify(clazz, times(1)).constantPoolEntryAccept(eq(index), eq(checker));
            reset(clazz);
        }
    }

    /**
     * Tests that visitFieldrefConstant with class index 0 works correctly.
     * Index 0 is special (usually null) but the method should still handle it.
     */
    @Test
    public void testVisitFieldrefConstant_withClassIndexZero_callsConstantPoolEntryAccept() {
        // Arrange
        fieldrefConstant.u2classIndex = 0;

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(clazz, times(1)).constantPoolEntryAccept(eq(0), eq(checker));
    }

    /**
     * Tests that visitFieldrefConstant with a large class index works correctly.
     * This verifies the method handles indices near the upper bounds of the constant pool.
     */
    @Test
    public void testVisitFieldrefConstant_withLargeClassIndex_callsConstantPoolEntryAccept() {
        // Arrange
        fieldrefConstant.u2classIndex = 65535; // Max value for u2

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(clazz, times(1)).constantPoolEntryAccept(eq(65535), eq(checker));
    }

    /**
     * Tests that visitFieldrefConstant can be called multiple times with the same fieldref.
     * Each call should invoke constantPoolEntryAccept.
     */
    @Test
    public void testVisitFieldrefConstant_calledMultipleTimes_invokesAcceptMethodEachTime() {
        // Arrange
        fieldrefConstant.u2classIndex = 15;

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);
        checker.visitFieldrefConstant(clazz, fieldrefConstant);
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(clazz, times(3)).constantPoolEntryAccept(eq(15), eq(checker));
    }

    /**
     * Tests that visitFieldrefConstant works with different FieldrefConstant instances.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentFieldrefConstants_callsEachIndex() {
        // Arrange
        FieldrefConstant const1 = new FieldrefConstant();
        const1.u2classIndex = 10;

        FieldrefConstant const2 = new FieldrefConstant();
        const2.u2classIndex = 20;

        FieldrefConstant const3 = new FieldrefConstant();
        const3.u2classIndex = 30;

        // Act
        checker.visitFieldrefConstant(clazz, const1);
        checker.visitFieldrefConstant(clazz, const2);
        checker.visitFieldrefConstant(clazz, const3);

        // Assert
        verify(clazz, times(1)).constantPoolEntryAccept(eq(10), eq(checker));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(20), eq(checker));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(30), eq(checker));
    }

    /**
     * Tests that visitFieldrefConstant works with different Clazz implementations.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentClazzTypes_callsConstantPoolEntryAccept() {
        // Arrange
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);
        fieldrefConstant.u2classIndex = 7;

        // Act
        checker.visitFieldrefConstant(programClass, fieldrefConstant);
        checker.visitFieldrefConstant(libraryClass, fieldrefConstant);

        // Assert
        verify(programClass, times(1)).constantPoolEntryAccept(eq(7), eq(checker));
        verify(libraryClass, times(1)).constantPoolEntryAccept(eq(7), eq(checker));
    }

    /**
     * Tests that visitFieldrefConstant with null FieldrefConstant throws NullPointerException.
     * This should result in NPE since the method accesses fieldrefConstant.u2classIndex.
     */
    @Test
    public void testVisitFieldrefConstant_withNullFieldrefConstant_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> checker.visitFieldrefConstant(clazz, null));
    }

    /**
     * Tests that visitFieldrefConstant with null Clazz throws NullPointerException.
     * This should result in NPE since the method calls a method on clazz.
     */
    @Test
    public void testVisitFieldrefConstant_withNullClazz_throwsNullPointerException() {
        // Arrange
        fieldrefConstant.u2classIndex = 5;

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> checker.visitFieldrefConstant(null, fieldrefConstant));
    }

    /**
     * Tests that visitFieldrefConstant with both parameters null throws NullPointerException.
     */
    @Test
    public void testVisitFieldrefConstant_withBothParametersNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> checker.visitFieldrefConstant(null, null));
    }

    /**
     * Tests that visitFieldrefConstant can be invoked through the ConstantVisitor interface.
     */
    @Test
    public void testVisitFieldrefConstant_throughConstantVisitorInterface_works() {
        // Arrange
        ConstantVisitor visitor = checker;
        fieldrefConstant.u2classIndex = 12;

        // Act
        visitor.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert
        verify(clazz, times(1)).constantPoolEntryAccept(eq(12), eq(checker));
    }

    /**
     * Tests that multiple checker instances can independently visit field references.
     */
    @Test
    public void testVisitFieldrefConstant_withMultipleCheckers_operateIndependently() {
        // Arrange
        ReferenceEscapeChecker checker1 = new ReferenceEscapeChecker();
        ReferenceEscapeChecker checker2 = new ReferenceEscapeChecker();
        ReferenceEscapeChecker checker3 = new ReferenceEscapeChecker();

        Clazz mockClazz1 = mock(ProgramClass.class);
        Clazz mockClazz2 = mock(ProgramClass.class);
        Clazz mockClazz3 = mock(ProgramClass.class);

        fieldrefConstant.u2classIndex = 8;

        // Act
        checker1.visitFieldrefConstant(mockClazz1, fieldrefConstant);
        checker2.visitFieldrefConstant(mockClazz2, fieldrefConstant);
        checker3.visitFieldrefConstant(mockClazz3, fieldrefConstant);

        // Assert - each checker should call its respective clazz
        verify(mockClazz1, times(1)).constantPoolEntryAccept(eq(8), eq(checker1));
        verify(mockClazz2, times(1)).constantPoolEntryAccept(eq(8), eq(checker2));
        verify(mockClazz3, times(1)).constantPoolEntryAccept(eq(8), eq(checker3));
    }

    /**
     * Tests that visitFieldrefConstant correctly uses the u2classIndex field.
     * This verifies the method reads the correct field from FieldrefConstant.
     */
    @Test
    public void testVisitFieldrefConstant_usesU2ClassIndexField() {
        // Arrange
        fieldrefConstant.u2classIndex = 123;
        fieldrefConstant.u2nameAndTypeIndex = 456; // This should NOT be used

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - should use u2classIndex, not u2nameAndTypeIndex
        verify(clazz, times(1)).constantPoolEntryAccept(eq(123), eq(checker));
        verify(clazz, never()).constantPoolEntryAccept(eq(456), any());
    }

    /**
     * Tests that visitFieldrefConstant handles sequential calls with changing indices.
     */
    @Test
    public void testVisitFieldrefConstant_withChangingIndices_callsCorrectIndexEachTime() {
        // Arrange & Act
        fieldrefConstant.u2classIndex = 1;
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        fieldrefConstant.u2classIndex = 2;
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        fieldrefConstant.u2classIndex = 3;
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify each call used the correct index
        verify(clazz, times(1)).constantPoolEntryAccept(eq(1), eq(checker));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(2), eq(checker));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(3), eq(checker));
    }

    /**
     * Tests that visitFieldrefConstant works correctly when called in rapid succession.
     */
    @Test
    public void testVisitFieldrefConstant_rapidSuccession_allCallsProcessed() {
        // Arrange
        fieldrefConstant.u2classIndex = 50;

        // Act - call 100 times rapidly
        for (int i = 0; i < 100; i++) {
            checker.visitFieldrefConstant(clazz, fieldrefConstant);
        }

        // Assert - all 100 calls should have been made
        verify(clazz, times(100)).constantPoolEntryAccept(eq(50), eq(checker));
    }

    /**
     * Tests that visitFieldrefConstant preserves the checker's internal state.
     * Calling this method should not affect the checker's escape analysis state.
     */
    @Test
    public void testVisitFieldrefConstant_doesNotModifyCheckerState() {
        // Arrange
        fieldrefConstant.u2classIndex = 25;

        // Check initial state
        assertFalse(checker.isInstanceExternal(0), "Initial state should be false");
        assertFalse(checker.isInstanceEscaping(0), "Initial state should be false");

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - state should remain unchanged (this method just delegates)
        assertFalse(checker.isInstanceExternal(0), "State should remain unchanged");
        assertFalse(checker.isInstanceEscaping(0), "State should remain unchanged");
    }

    /**
     * Tests that visitFieldrefConstant works correctly with boundary values for u2classIndex.
     * The u2 prefix indicates an unsigned 16-bit integer (0-65535).
     */
    @Test
    public void testVisitFieldrefConstant_withBoundaryValues_handlesCorrectly() {
        // Arrange & Act & Assert
        // Minimum value
        fieldrefConstant.u2classIndex = 0;
        checker.visitFieldrefConstant(clazz, fieldrefConstant);
        verify(clazz, times(1)).constantPoolEntryAccept(eq(0), eq(checker));
        reset(clazz);

        // Typical small values
        fieldrefConstant.u2classIndex = 1;
        checker.visitFieldrefConstant(clazz, fieldrefConstant);
        verify(clazz, times(1)).constantPoolEntryAccept(eq(1), eq(checker));
        reset(clazz);

        // Mid-range value
        fieldrefConstant.u2classIndex = 32767;
        checker.visitFieldrefConstant(clazz, fieldrefConstant);
        verify(clazz, times(1)).constantPoolEntryAccept(eq(32767), eq(checker));
        reset(clazz);

        // Maximum value for unsigned 16-bit
        fieldrefConstant.u2classIndex = 65535;
        checker.visitFieldrefConstant(clazz, fieldrefConstant);
        verify(clazz, times(1)).constantPoolEntryAccept(eq(65535), eq(checker));
    }

    /**
     * Tests that visitFieldrefConstant only calls constantPoolEntryAccept once per invocation.
     * This verifies the method doesn't make multiple or redundant calls.
     */
    @Test
    public void testVisitFieldrefConstant_callsConstantPoolEntryAcceptOnce() {
        // Arrange
        fieldrefConstant.u2classIndex = 33;

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - should be called exactly once
        verify(clazz, times(1)).constantPoolEntryAccept(anyInt(), any(ConstantVisitor.class));
        verify(clazz, times(1)).constantPoolEntryAccept(eq(33), eq(checker));
    }

    /**
     * Tests that visitFieldrefConstant works when the checker has been used for other operations.
     */
    @Test
    public void testVisitFieldrefConstant_afterOtherCheckerOperations_stillWorks() {
        // Arrange - use the checker for other operations first
        checker.isInstanceExternal(0);
        checker.isInstanceEscaping(10);
        checker.isInstanceReturned(20);
        checker.isInstanceModified(30);

        fieldrefConstant.u2classIndex = 40;

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - should still work correctly
        verify(clazz, times(1)).constantPoolEntryAccept(eq(40), eq(checker));
    }

    /**
     * Tests that visitFieldrefConstant is part of the visitor chain.
     * This method enables traversal from field references to class constants.
     */
    @Test
    public void testVisitFieldrefConstant_facilitatesVisitorChain() {
        // Arrange
        fieldrefConstant.u2classIndex = 15;

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - the method should pass the checker as visitor,
        // enabling continued traversal through the constant pool
        verify(clazz).constantPoolEntryAccept(eq(15), same(checker));
    }

    /**
     * Tests that visitFieldrefConstant handles interleaved calls with different parameters.
     */
    @Test
    public void testVisitFieldrefConstant_interleavedCalls_handlesCorrectly() {
        // Arrange
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(ProgramClass.class);
        FieldrefConstant ref1 = new FieldrefConstant();
        ref1.u2classIndex = 10;
        FieldrefConstant ref2 = new FieldrefConstant();
        ref2.u2classIndex = 20;

        // Act - interleave calls
        checker.visitFieldrefConstant(clazz1, ref1);
        checker.visitFieldrefConstant(clazz2, ref2);
        checker.visitFieldrefConstant(clazz1, ref2);
        checker.visitFieldrefConstant(clazz2, ref1);

        // Assert
        verify(clazz1, times(1)).constantPoolEntryAccept(eq(10), eq(checker));
        verify(clazz1, times(1)).constantPoolEntryAccept(eq(20), eq(checker));
        verify(clazz2, times(1)).constantPoolEntryAccept(eq(20), eq(checker));
        verify(clazz2, times(1)).constantPoolEntryAccept(eq(10), eq(checker));
    }

    /**
     * Tests the core delegation behavior with explicit verification.
     */
    @Test
    public void testVisitFieldrefConstant_coreDelegationBehavior() {
        // Arrange
        fieldrefConstant.u2classIndex = 99;

        // Act
        checker.visitFieldrefConstant(clazz, fieldrefConstant);

        // Assert - verify the exact delegation:
        // 1. Method is called on clazz
        // 2. With the class index from fieldrefConstant
        // 3. Passing the checker as the visitor
        verify(clazz).constantPoolEntryAccept(
            eq(fieldrefConstant.u2classIndex),
            same(checker)
        );
    }
}
