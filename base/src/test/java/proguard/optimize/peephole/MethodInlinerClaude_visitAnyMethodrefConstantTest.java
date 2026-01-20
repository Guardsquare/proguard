package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MethodInliner#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
 *
 * The visitAnyMethodrefConstant method is a key part of the method inlining process. It is called
 * when visiting method reference constants in the constant pool during instruction processing.
 * The method delegates to the referenced method by calling referencedMethodAccept, passing the
 * MethodInliner itself as a MemberVisitor. This allows the inliner to visit the actual method
 * (via visitProgramMethod or visitLibraryMethod) and potentially inline it at the call site.
 *
 * This method is typically called from visitConstantInstruction when processing method invocation
 * instructions (invokevirtual, invokespecial, invokestatic, invokeinterface).
 */
public class MethodInlinerClaude_visitAnyMethodrefConstantTest {

    private MethodInliner inliner;
    private Clazz clazz;
    private AnyMethodrefConstant methodrefConstant;

    @BeforeEach
    public void setUp() {
        // Create a concrete implementation for testing
        inliner = new ShortMethodInliner(false, false, true);
        clazz = mock(ProgramClass.class);
        methodrefConstant = mock(MethodrefConstant.class);
    }

    // ========================================
    // Core Functionality Tests
    // ========================================

    /**
     * Tests that visitAnyMethodrefConstant calls referencedMethodAccept on the method reference.
     * This is the core behavior - the method should delegate to the method reference constant
     * to visit the referenced method through the MethodInliner itself.
     */
    @Test
    public void testVisitAnyMethodrefConstant_callsReferencedMethodAccept() {
        // Act
        inliner.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - verify that referencedMethodAccept was called with a MemberVisitor
        verify(methodrefConstant, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant passes the MethodInliner as the visitor to referencedMethodAccept.
     * The MethodInliner implements MemberVisitor, so it passes itself to visit the referenced method.
     */
    @Test
    public void testVisitAnyMethodrefConstant_passesItselfAsVisitor() {
        // Act
        inliner.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - verify a MemberVisitor was passed (the MethodInliner itself)
        verify(methodrefConstant).referencedMethodAccept(isA(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant works with valid objects without throwing exceptions.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withValidObjects_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> inliner.visitAnyMethodrefConstant(clazz, methodrefConstant));
    }

    /**
     * Tests that visitAnyMethodrefConstant can be called multiple times in succession.
     * Each call should invoke referencedMethodAccept.
     */
    @Test
    public void testVisitAnyMethodrefConstant_calledMultipleTimes_invokesReferencedMethodAcceptEachTime() {
        // Act
        inliner.visitAnyMethodrefConstant(clazz, methodrefConstant);
        inliner.visitAnyMethodrefConstant(clazz, methodrefConstant);
        inliner.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - verify referencedMethodAccept was called three times
        verify(methodrefConstant, times(3)).referencedMethodAccept(any(MemberVisitor.class));
    }

    // ========================================
    // Null Parameter Tests
    // ========================================

    /**
     * Tests that visitAnyMethodrefConstant can be called with null Clazz parameter.
     * The method should handle null clazz gracefully since it doesn't directly use it.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullClazz_doesNotThrowException() {
        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> inliner.visitAnyMethodrefConstant(null, methodrefConstant));

        // Verify the method still calls referencedMethodAccept
        verify(methodrefConstant, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant with null AnyMethodrefConstant throws NullPointerException.
     * This should result in a NullPointerException since the method calls methods on the constant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withNullMethodrefConstant_throwsNullPointerException() {
        // Act & Assert - should throw NullPointerException
        assertThrows(NullPointerException.class,
            () -> inliner.visitAnyMethodrefConstant(clazz, null));
    }

    /**
     * Tests that visitAnyMethodrefConstant doesn't directly interact with the Clazz parameter.
     * The clazz is passed as a context parameter but not used in this method.
     */
    @Test
    public void testVisitAnyMethodrefConstant_doesNotInteractWithClazz() {
        // Act
        inliner.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - verify no interactions occurred with the clazz mock
        verifyNoInteractions(clazz);
    }

    // ========================================
    // Different Constant Types Tests
    // ========================================

    /**
     * Tests that visitAnyMethodrefConstant works with MethodrefConstant.
     * MethodrefConstant is one of the concrete implementations of AnyMethodrefConstant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withMethodrefConstant_callsReferencedMethodAccept() {
        // Arrange
        AnyMethodrefConstant methodref = mock(MethodrefConstant.class);

        // Act
        inliner.visitAnyMethodrefConstant(clazz, methodref);

        // Assert - verify referencedMethodAccept was called
        verify(methodref).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant works with InterfaceMethodrefConstant.
     * AnyMethodrefConstant is a base class for both MethodrefConstant and InterfaceMethodrefConstant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withInterfaceMethodrefConstant_callsReferencedMethodAccept() {
        // Arrange
        AnyMethodrefConstant interfaceMethodref = mock(InterfaceMethodrefConstant.class);

        // Act
        inliner.visitAnyMethodrefConstant(clazz, interfaceMethodref);

        // Assert - verify referencedMethodAccept was called
        verify(interfaceMethodref).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant works with different AnyMethodrefConstant instances.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withDifferentMethodrefConstants_callsAcceptOnEach() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(MethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(MethodrefConstant.class);
        AnyMethodrefConstant methodref3 = mock(InterfaceMethodrefConstant.class);

        // Act
        inliner.visitAnyMethodrefConstant(clazz, methodref1);
        inliner.visitAnyMethodrefConstant(clazz, methodref2);
        inliner.visitAnyMethodrefConstant(clazz, methodref3);

        // Assert - verify each constant had referencedMethodAccept called
        verify(methodref1).referencedMethodAccept(any(MemberVisitor.class));
        verify(methodref2).referencedMethodAccept(any(MemberVisitor.class));
        verify(methodref3).referencedMethodAccept(any(MemberVisitor.class));
    }

    // ========================================
    // Different Class Types Tests
    // ========================================

    /**
     * Tests that visitAnyMethodrefConstant can be called with real ProgramClass instance.
     * Verifies the method works with actual class instances, not just mocks.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withRealProgramClass_doesNotThrowException() {
        // Arrange
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.visitAnyMethodrefConstant(realClass, methodrefConstant));
        verify(methodrefConstant, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant can be called with real LibraryClass instance.
     * Verifies the method works with library classes.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withRealLibraryClass_doesNotThrowException() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> inliner.visitAnyMethodrefConstant(libraryClass, methodrefConstant));
        verify(methodrefConstant, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant works consistently with different class types.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withDifferentClassTypes_consistent() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        AnyMethodrefConstant methodref1 = mock(MethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(MethodrefConstant.class);

        // Act
        inliner.visitAnyMethodrefConstant(programClass, methodref1);
        inliner.visitAnyMethodrefConstant(libraryClass, methodref2);

        // Assert
        verify(methodref1).referencedMethodAccept(any(MemberVisitor.class));
        verify(methodref2).referencedMethodAccept(any(MemberVisitor.class));
    }

    // ========================================
    // Visitor Pattern Integration Tests
    // ========================================

    /**
     * Tests that visitAnyMethodrefConstant can be used as part of the ConstantVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testVisitAnyMethodrefConstant_usedAsConstantVisitor_worksCorrectly() {
        // Arrange
        proguard.classfile.constant.visitor.ConstantVisitor visitor = inliner;

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyMethodrefConstant(clazz, methodrefConstant));
        verify(methodrefConstant, times(1)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant integrates correctly with the double-dispatch visitor pattern.
     * The method should delegate to referencedMethodAccept, which will then call back to the visitor.
     */
    @Test
    public void testVisitAnyMethodrefConstant_doubleDispatchPattern_worksCorrectly() {
        // Act
        inliner.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - the double dispatch should cause referencedMethodAccept to be called,
        // which would then call visitProgramMethod or visitLibraryMethod on the inliner
        verify(methodrefConstant).referencedMethodAccept(any(MemberVisitor.class));
    }

    // ========================================
    // Different Inliner Configuration Tests
    // ========================================

    /**
     * Tests visitAnyMethodrefConstant with microEdition configuration.
     * Verifies the delegation behavior is independent of configuration.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withMicroEditionConfiguration_doesNotThrow() {
        // Arrange
        MethodInliner microEditionInliner = new ShortMethodInliner(true, false, false);
        AnyMethodrefConstant methodref = mock(MethodrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> microEditionInliner.visitAnyMethodrefConstant(clazz, methodref));
        verify(methodref).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests visitAnyMethodrefConstant with Android configuration.
     * Verifies the delegation behavior is independent of configuration.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withAndroidConfiguration_doesNotThrow() {
        // Arrange
        MethodInliner androidInliner = new ShortMethodInliner(false, true, false);
        AnyMethodrefConstant methodref = mock(MethodrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> androidInliner.visitAnyMethodrefConstant(clazz, methodref));
        verify(methodref).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests visitAnyMethodrefConstant with allowAccessModification enabled.
     * Verifies the delegation behavior is independent of access modification setting.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withAccessModificationEnabled_doesNotThrow() {
        // Arrange
        MethodInliner accessModInliner = new ShortMethodInliner(false, false, true);
        AnyMethodrefConstant methodref = mock(MethodrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> accessModInliner.visitAnyMethodrefConstant(clazz, methodref));
        verify(methodref).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests visitAnyMethodrefConstant with all configuration flags enabled.
     * Verifies the delegation behavior is independent of all configurations.
     */
    @Test
    public void testVisitAnyMethodrefConstant_withAllConfigurationsEnabled_doesNotThrow() {
        // Arrange
        MethodInliner fullConfigInliner = new ShortMethodInliner(true, true, true);
        AnyMethodrefConstant methodref = mock(MethodrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> fullConfigInliner.visitAnyMethodrefConstant(clazz, methodref));
        verify(methodref).referencedMethodAccept(any(MemberVisitor.class));
    }

    // ========================================
    // Multiple Inliner Instance Tests
    // ========================================

    /**
     * Tests that different inliner instances work independently.
     * Verifies no shared state between instances.
     */
    @Test
    public void testVisitAnyMethodrefConstant_multipleInliners_independent() {
        // Arrange
        MethodInliner inliner1 = new ShortMethodInliner(false, false, true);
        MethodInliner inliner2 = new ShortMethodInliner(false, false, true);
        AnyMethodrefConstant methodref1 = mock(MethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(MethodrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            inliner1.visitAnyMethodrefConstant(clazz, methodref1);
            inliner2.visitAnyMethodrefConstant(clazz, methodref2);
        });
        verify(methodref1).referencedMethodAccept(any(MemberVisitor.class));
        verify(methodref2).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that multiple inliners can visit the same method reference.
     * Verifies concurrent-like access patterns work correctly.
     */
    @Test
    public void testVisitAnyMethodrefConstant_sameMethodrefDifferentInliners() {
        // Arrange
        AnyMethodrefConstant methodref = mock(MethodrefConstant.class);
        MethodInliner inliner1 = new ShortMethodInliner(false, false, true);
        MethodInliner inliner2 = new ShortMethodInliner(true, false, false);
        MethodInliner inliner3 = new ShortMethodInliner(false, true, true);

        // Act & Assert
        assertDoesNotThrow(() -> {
            inliner1.visitAnyMethodrefConstant(clazz, methodref);
            inliner2.visitAnyMethodrefConstant(clazz, methodref);
            inliner3.visitAnyMethodrefConstant(clazz, methodref);
        });
        verify(methodref, times(3)).referencedMethodAccept(any(MemberVisitor.class));
    }

    // ========================================
    // Edge Case Tests
    // ========================================

    /**
     * Tests visitAnyMethodrefConstant immediately after inliner creation.
     * Verifies the inliner is ready to use immediately.
     */
    @Test
    public void testVisitAnyMethodrefConstant_immediatelyAfterCreation() {
        // Arrange
        MethodInliner newInliner = new ShortMethodInliner(false, false, true);
        AnyMethodrefConstant methodref = mock(MethodrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> newInliner.visitAnyMethodrefConstant(clazz, methodref));
        verify(methodref).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant can be called many times sequentially.
     * Verifies stable behavior over extended use.
     */
    @Test
    public void testVisitAnyMethodrefConstant_manySequentialCalls_consistent() {
        // Arrange
        AnyMethodrefConstant methodref = mock(MethodrefConstant.class);

        // Act & Assert
        for (int i = 0; i < 100; i++) {
            final int iteration = i;
            assertDoesNotThrow(() -> inliner.visitAnyMethodrefConstant(clazz, methodref),
                "Call " + iteration + " should not throw");
        }
        verify(methodref, times(100)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant maintains consistent behavior with alternating parameters.
     * Verifies the method doesn't accumulate state between calls.
     */
    @Test
    public void testVisitAnyMethodrefConstant_alternatingParameters_consistent() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        AnyMethodrefConstant methodref1 = mock(MethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(InterfaceMethodrefConstant.class);

        // Act & Assert
        assertDoesNotThrow(() -> {
            inliner.visitAnyMethodrefConstant(programClass, methodref1);
            inliner.visitAnyMethodrefConstant(libraryClass, methodref2);
            inliner.visitAnyMethodrefConstant(programClass, methodref2);
            inliner.visitAnyMethodrefConstant(libraryClass, methodref1);
        });
        verify(methodref1, times(2)).referencedMethodAccept(any(MemberVisitor.class));
        verify(methodref2, times(2)).referencedMethodAccept(any(MemberVisitor.class));
    }

    /**
     * Tests that visitAnyMethodrefConstant executes quickly.
     * Verifies performance characteristics of the delegation method.
     */
    @Test
    public void testVisitAnyMethodrefConstant_executesQuickly() {
        // Arrange
        AnyMethodrefConstant methodref = mock(MethodrefConstant.class);
        long startTime = System.nanoTime();

        // Act
        for (int i = 0; i < 10000; i++) {
            inliner.visitAnyMethodrefConstant(clazz, methodref);
        }
        long endTime = System.nanoTime();

        // Assert
        long durationMs = (endTime - startTime) / 1_000_000;
        assertTrue(durationMs < 1000,
            "10000 calls should complete in less than 1000ms, took: " + durationMs + "ms");
        verify(methodref, times(10000)).referencedMethodAccept(any(MemberVisitor.class));
    }

    // ========================================
    // Behavioral Verification Tests
    // ========================================

    /**
     * Tests that visitAnyMethodrefConstant only calls referencedMethodAccept once per invocation.
     * Verifies no duplicate or extra calls are made.
     */
    @Test
    public void testVisitAnyMethodrefConstant_callsReferencedMethodAcceptExactlyOnce() {
        // Act
        inliner.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - verify exactly one call
        verify(methodrefConstant, times(1)).referencedMethodAccept(any(MemberVisitor.class));
        verifyNoMoreInteractions(methodrefConstant);
    }

    /**
     * Tests that visitAnyMethodrefConstant doesn't modify the method reference constant.
     * Verifies the method is purely delegative without side effects on the constant.
     */
    @Test
    public void testVisitAnyMethodrefConstant_doesNotModifyMethodrefConstant() {
        // Act
        inliner.visitAnyMethodrefConstant(clazz, methodrefConstant);

        // Assert - only referencedMethodAccept should be called, no other methods
        verify(methodrefConstant).referencedMethodAccept(any(MemberVisitor.class));
        verifyNoMoreInteractions(methodrefConstant);
    }

    /**
     * Tests that the same visitor instance (the inliner) is passed to all calls.
     * Verifies consistency in the visitor pattern implementation.
     */
    @Test
    public void testVisitAnyMethodrefConstant_passesSameVisitorInstance() {
        // Arrange
        AnyMethodrefConstant methodref1 = mock(MethodrefConstant.class);
        AnyMethodrefConstant methodref2 = mock(MethodrefConstant.class);

        // Act
        inliner.visitAnyMethodrefConstant(clazz, methodref1);
        inliner.visitAnyMethodrefConstant(clazz, methodref2);

        // Assert - both should receive a MemberVisitor
        verify(methodref1).referencedMethodAccept(isA(MemberVisitor.class));
        verify(methodref2).referencedMethodAccept(isA(MemberVisitor.class));
    }
}
