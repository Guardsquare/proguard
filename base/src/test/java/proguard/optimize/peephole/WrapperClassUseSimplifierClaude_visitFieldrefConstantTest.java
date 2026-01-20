package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link WrapperClassUseSimplifier#visitFieldrefConstant(Clazz, FieldrefConstant)}.
 *
 * The visitFieldrefConstant method in WrapperClassUseSimplifier is called when a field reference
 * constant is encountered. The method:
 * 1. Calls referencedClassAccept on the FieldrefConstant to visit the class that declares the field
 * 2. This allows the simplifier to check if the field is in a wrapper class using the ClassVisitor
 *    implementation (visitProgramClass), which sets the wrappedClass field if the class is a wrapper
 * 3. The wrappedClass field is then used by other methods to determine if wrapper class simplification
 *    should occur
 *
 * These tests verify that:
 * - The method correctly delegates to referencedClassAccept
 * - The simplifier (this) is passed as the ClassVisitor
 * - The method handles various parameter types correctly
 * - The method does not directly interact with the Clazz parameter
 */
public class WrapperClassUseSimplifierClaude_visitFieldrefConstantTest {

    private WrapperClassUseSimplifier simplifier;
    private InstructionVisitor mockExtraVisitor;
    private Clazz mockClazz;
    private FieldrefConstant mockFieldrefConstant;

    @BeforeEach
    public void setUp() {
        mockExtraVisitor = mock(InstructionVisitor.class);
        mockClazz = mock(Clazz.class);
        mockFieldrefConstant = mock(FieldrefConstant.class);
    }

    // ========================================
    // Core Behavior Tests
    // ========================================

    /**
     * Tests that visitFieldrefConstant calls referencedClassAccept on the FieldrefConstant.
     * This is the core behavior - delegating to the field reference to visit the class
     * that declares the field.
     */
    @Test
    public void testVisitFieldrefConstant_callsReferencedClassAccept() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act
        simplifier.visitFieldrefConstant(mockClazz, mockFieldrefConstant);

        // Assert
        verify(mockFieldrefConstant, times(1)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitFieldrefConstant passes the simplifier itself as the ClassVisitor.
     * The simplifier implements ClassVisitor to handle wrapper class detection.
     */
    @Test
    public void testVisitFieldrefConstant_passesSimplifierAsVisitor() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act
        simplifier.visitFieldrefConstant(mockClazz, mockFieldrefConstant);

        // Assert - verify the simplifier itself is passed as the visitor
        verify(mockFieldrefConstant).referencedClassAccept(same(simplifier));
    }

    /**
     * Tests that visitFieldrefConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitFieldrefConstant_withValidMocks_doesNotThrowException() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> simplifier.visitFieldrefConstant(mockClazz, mockFieldrefConstant));
    }

    /**
     * Tests that visitFieldrefConstant with no-arg constructor works correctly.
     */
    @Test
    public void testVisitFieldrefConstant_withNoArgConstructor_callsReferencedClassAccept() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier();

        // Act
        simplifier.visitFieldrefConstant(mockClazz, mockFieldrefConstant);

        // Assert
        verify(mockFieldrefConstant, times(1)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitFieldrefConstant with extra visitor works correctly.
     * The extra visitor is not directly involved in this method.
     */
    @Test
    public void testVisitFieldrefConstant_withExtraVisitor_callsReferencedClassAccept() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act
        simplifier.visitFieldrefConstant(mockClazz, mockFieldrefConstant);

        // Assert
        verify(mockFieldrefConstant, times(1)).referencedClassAccept(eq(simplifier));
        verifyNoInteractions(mockExtraVisitor);
    }

    // ========================================
    // Null Parameter Tests
    // ========================================

    /**
     * Tests that visitFieldrefConstant can be called with null Clazz parameter.
     * The method should handle null clazz gracefully since it doesn't directly use it.
     */
    @Test
    public void testVisitFieldrefConstant_withNullClazz_doesNotThrowException() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> simplifier.visitFieldrefConstant(null, mockFieldrefConstant));

        // Verify the method still calls referencedClassAccept
        verify(mockFieldrefConstant, times(1)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitFieldrefConstant with null FieldrefConstant throws NullPointerException.
     * This should result in NPE since the method calls a method on fieldrefConstant.
     */
    @Test
    public void testVisitFieldrefConstant_withNullFieldrefConstant_throwsNullPointerException() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> simplifier.visitFieldrefConstant(mockClazz, null));
    }

    /**
     * Tests that visitFieldrefConstant with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitFieldrefConstant_withBothNullParameters_throwsNullPointerException() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> simplifier.visitFieldrefConstant(null, null));
    }

    // ========================================
    // Multiple Invocation Tests
    // ========================================

    /**
     * Tests that visitFieldrefConstant can be called multiple times in succession.
     * Each call should invoke referencedClassAccept.
     */
    @Test
    public void testVisitFieldrefConstant_calledMultipleTimes_invokesAcceptMethodEachTime() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act
        simplifier.visitFieldrefConstant(mockClazz, mockFieldrefConstant);
        simplifier.visitFieldrefConstant(mockClazz, mockFieldrefConstant);
        simplifier.visitFieldrefConstant(mockClazz, mockFieldrefConstant);

        // Assert
        verify(mockFieldrefConstant, times(3)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitFieldrefConstant works correctly with rapid successive calls.
     */
    @Test
    public void testVisitFieldrefConstant_rapidSuccessiveCalls_eachInvokesAccept() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call many times rapidly
        for (int i = 0; i < 100; i++) {
            simplifier.visitFieldrefConstant(mockClazz, mockFieldrefConstant);
        }

        // Assert
        verify(mockFieldrefConstant, times(100)).referencedClassAccept(eq(simplifier));
    }

    // ========================================
    // Different Parameter Type Tests
    // ========================================

    /**
     * Tests that visitFieldrefConstant doesn't directly interact with the Clazz parameter.
     * The clazz is passed as a context parameter but not used in this method.
     */
    @Test
    public void testVisitFieldrefConstant_doesNotInteractWithClazz() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act
        simplifier.visitFieldrefConstant(mockClazz, mockFieldrefConstant);

        // Assert
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitFieldrefConstant works with different Clazz implementations.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentClazzTypes_doesNotThrowException() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitFieldrefConstant(programClass, mockFieldrefConstant));
        assertDoesNotThrow(() -> simplifier.visitFieldrefConstant(libraryClass, mockFieldrefConstant));

        // Verify both calls invoked referencedClassAccept
        verify(mockFieldrefConstant, times(2)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitFieldrefConstant works with different FieldrefConstant mock instances.
     */
    @Test
    public void testVisitFieldrefConstant_withDifferentFieldrefConstants_callsEachOne() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        FieldrefConstant const1 = mock(FieldrefConstant.class);
        FieldrefConstant const2 = mock(FieldrefConstant.class);
        FieldrefConstant const3 = mock(FieldrefConstant.class);

        // Act
        simplifier.visitFieldrefConstant(mockClazz, const1);
        simplifier.visitFieldrefConstant(mockClazz, const2);
        simplifier.visitFieldrefConstant(mockClazz, const3);

        // Assert - each constant should have its referencedClassAccept called
        verify(const1, times(1)).referencedClassAccept(eq(simplifier));
        verify(const2, times(1)).referencedClassAccept(eq(simplifier));
        verify(const3, times(1)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitFieldrefConstant works with different combinations of parameters.
     */
    @Test
    public void testVisitFieldrefConstant_withVariousClazzAndConstantCombinations() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        FieldrefConstant const1 = mock(FieldrefConstant.class);
        FieldrefConstant const2 = mock(FieldrefConstant.class);

        // Act
        simplifier.visitFieldrefConstant(clazz1, const1);
        simplifier.visitFieldrefConstant(clazz1, const2);
        simplifier.visitFieldrefConstant(clazz2, const1);
        simplifier.visitFieldrefConstant(clazz2, const2);

        // Assert - verify each constant was visited twice
        verify(const1, times(2)).referencedClassAccept(eq(simplifier));
        verify(const2, times(2)).referencedClassAccept(eq(simplifier));

        // Verify clazz parameters were not interacted with
        verifyNoInteractions(clazz1);
        verifyNoInteractions(clazz2);
    }

    // ========================================
    // Multiple Instance Tests
    // ========================================

    /**
     * Tests that multiple WrapperClassUseSimplifier instances can independently
     * call visitFieldrefConstant.
     */
    @Test
    public void testVisitFieldrefConstant_multipleInstances_independent() {
        // Arrange
        InstructionVisitor mockVisitor1 = mock(InstructionVisitor.class);
        InstructionVisitor mockVisitor2 = mock(InstructionVisitor.class);
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(mockVisitor1);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(mockVisitor2);

        FieldrefConstant const1 = mock(FieldrefConstant.class);
        FieldrefConstant const2 = mock(FieldrefConstant.class);

        // Act
        simplifier1.visitFieldrefConstant(mockClazz, const1);
        simplifier2.visitFieldrefConstant(mockClazz, const2);

        // Assert - each constant should have been visited by its respective simplifier
        verify(const1, times(1)).referencedClassAccept(eq(simplifier1));
        verify(const2, times(1)).referencedClassAccept(eq(simplifier2));

        // Verify extra visitors were not invoked (they're only for instruction visiting)
        verifyNoInteractions(mockVisitor1);
        verifyNoInteractions(mockVisitor2);
    }

    /**
     * Tests that the same simplifier instance can visit different field reference constants.
     */
    @Test
    public void testVisitFieldrefConstant_sameInstanceDifferentConstants() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        FieldrefConstant[] constants = new FieldrefConstant[5];
        for (int i = 0; i < 5; i++) {
            constants[i] = mock(FieldrefConstant.class);
        }

        // Act
        for (FieldrefConstant constant : constants) {
            simplifier.visitFieldrefConstant(mockClazz, constant);
        }

        // Assert - each constant should have been visited exactly once
        for (FieldrefConstant constant : constants) {
            verify(constant, times(1)).referencedClassAccept(eq(simplifier));
        }
    }

    // ========================================
    // Interface Contract Tests
    // ========================================

    /**
     * Tests that visitFieldrefConstant properly implements the ConstantVisitor interface.
     */
    @Test
    public void testVisitFieldrefConstant_asConstantVisitor_properImplementation() {
        // Arrange
        WrapperClassUseSimplifier visitor = new WrapperClassUseSimplifier(null);

        // Act & Assert - use as ConstantVisitor interface
        assertDoesNotThrow(() -> visitor.visitFieldrefConstant(mockClazz, mockFieldrefConstant),
                "visitFieldrefConstant should work correctly through ConstantVisitor interface");

        // Verify the delegation occurred
        verify(mockFieldrefConstant).referencedClassAccept(same(visitor));
    }

    /**
     * Tests that visitFieldrefConstant returns normally (void method completes).
     */
    @Test
    public void testVisitFieldrefConstant_returnsNormally() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert - should complete without exceptions or blocking
        assertDoesNotThrow(() -> {
            simplifier.visitFieldrefConstant(mockClazz, mockFieldrefConstant);
            // If we reach this point, the method returned normally
        }, "visitFieldrefConstant should return normally");
    }

    // ========================================
    // State and Consistency Tests
    // ========================================

    /**
     * Tests that calling visitFieldrefConstant doesn't interfere with subsequent calls.
     */
    @Test
    public void testVisitFieldrefConstant_multipleCallsDoNotInterfere() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        FieldrefConstant const1 = mock(FieldrefConstant.class);
        FieldrefConstant const2 = mock(FieldrefConstant.class);

        // Act
        simplifier.visitFieldrefConstant(mockClazz, const1);
        simplifier.visitFieldrefConstant(mockClazz, const2);
        simplifier.visitFieldrefConstant(mockClazz, const1); // call const1 again

        // Assert - verify correct number of calls to each
        verify(const1, times(2)).referencedClassAccept(eq(simplifier));
        verify(const2, times(1)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitFieldrefConstant maintains consistent behavior across constructor variants.
     */
    @Test
    public void testVisitFieldrefConstant_withBothConstructors_consistentBehavior() {
        // Arrange
        WrapperClassUseSimplifier simplifierNoArg = new WrapperClassUseSimplifier();
        WrapperClassUseSimplifier simplifierWithVisitor = new WrapperClassUseSimplifier(mockExtraVisitor);
        FieldrefConstant const1 = mock(FieldrefConstant.class);
        FieldrefConstant const2 = mock(FieldrefConstant.class);

        // Act
        simplifierNoArg.visitFieldrefConstant(mockClazz, const1);
        simplifierWithVisitor.visitFieldrefConstant(mockClazz, const2);

        // Assert - both should delegate to referencedClassAccept
        verify(const1, times(1)).referencedClassAccept(eq(simplifierNoArg));
        verify(const2, times(1)).referencedClassAccept(eq(simplifierWithVisitor));
    }

    /**
     * Tests that the extra visitor is not involved in visitFieldrefConstant.
     * This method only deals with constant visiting, not instruction visiting.
     */
    @Test
    public void testVisitFieldrefConstant_doesNotInvolveExtraVisitor() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act
        simplifier.visitFieldrefConstant(mockClazz, mockFieldrefConstant);
        simplifier.visitFieldrefConstant(mockClazz, mockFieldrefConstant);
        simplifier.visitFieldrefConstant(mockClazz, mockFieldrefConstant);

        // Assert - extra visitor should never be called
        verifyNoInteractions(mockExtraVisitor);
    }

    // ========================================
    // Edge Case and Stress Tests
    // ========================================

    /**
     * Tests that visitFieldrefConstant handles alternating between different instances.
     */
    @Test
    public void testVisitFieldrefConstant_alternatingInstances() {
        // Arrange
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(null);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(mockExtraVisitor);
        FieldrefConstant constant = mock(FieldrefConstant.class);

        // Act - alternate between simplifiers
        simplifier1.visitFieldrefConstant(mockClazz, constant);
        simplifier2.visitFieldrefConstant(mockClazz, constant);
        simplifier1.visitFieldrefConstant(mockClazz, constant);
        simplifier2.visitFieldrefConstant(mockClazz, constant);

        // Assert - should have 4 total calls with correct visitors
        verify(constant, times(2)).referencedClassAccept(eq(simplifier1));
        verify(constant, times(2)).referencedClassAccept(eq(simplifier2));
    }

    /**
     * Tests that visitFieldrefConstant works correctly with sequential different class instances.
     */
    @Test
    public void testVisitFieldrefConstant_withSequentialDifferentClasses() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);
        Clazz[] classes = new Clazz[5];
        for (int i = 0; i < 5; i++) {
            classes[i] = mock(Clazz.class);
        }

        // Act
        for (Clazz clazz : classes) {
            simplifier.visitFieldrefConstant(clazz, mockFieldrefConstant);
        }

        // Assert - fieldref should be visited 5 times
        verify(mockFieldrefConstant, times(5)).referencedClassAccept(eq(simplifier));

        // Verify no clazz was interacted with
        for (Clazz clazz : classes) {
            verifyNoInteractions(clazz);
        }
    }

    /**
     * Tests that visitFieldrefConstant correctly passes the simplifier reference,
     * not some other object or null.
     */
    @Test
    public void testVisitFieldrefConstant_passesCorrectSimplifierReference() {
        // Arrange
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(null);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(null);
        FieldrefConstant constant = mock(FieldrefConstant.class);

        // Act
        simplifier1.visitFieldrefConstant(mockClazz, constant);
        simplifier2.visitFieldrefConstant(mockClazz, constant);

        // Assert - verify each simplifier passed itself, not the other
        verify(constant, times(1)).referencedClassAccept(same(simplifier1));
        verify(constant, times(1)).referencedClassAccept(same(simplifier2));
        verify(constant, never()).referencedClassAccept(null);
    }

    /**
     * Tests that visitFieldrefConstant maintains proper method signature and behavior
     * when called with various timing patterns.
     */
    @Test
    public void testVisitFieldrefConstant_withVariousTimingPatterns() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        FieldrefConstant const1 = mock(FieldrefConstant.class);
        FieldrefConstant const2 = mock(FieldrefConstant.class);

        // Act - various timing patterns
        simplifier.visitFieldrefConstant(mockClazz, const1);
        simplifier.visitFieldrefConstant(mockClazz, const1);
        simplifier.visitFieldrefConstant(mockClazz, const2);
        simplifier.visitFieldrefConstant(mockClazz, const1);
        simplifier.visitFieldrefConstant(mockClazz, const2);

        // Assert
        verify(const1, times(3)).referencedClassAccept(eq(simplifier));
        verify(const2, times(2)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitFieldrefConstant does not modify any visible state
     * that would affect other visitor methods.
     */
    @Test
    public void testVisitFieldrefConstant_doesNotAffectOtherVisitorMethods() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        FieldrefConstant constant = mock(FieldrefConstant.class);

        // Act - call visitFieldrefConstant
        simplifier.visitFieldrefConstant(mockClazz, constant);

        // Assert - should still be able to call it again without issues
        assertDoesNotThrow(() -> simplifier.visitFieldrefConstant(mockClazz, constant),
                "Subsequent calls should work normally");

        // Verify total of 2 calls
        verify(constant, times(2)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitFieldrefConstant with ProgramClass doesn't throw exceptions.
     */
    @Test
    public void testVisitFieldrefConstant_withProgramClass_works() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitFieldrefConstant(programClass, mockFieldrefConstant));
        verify(mockFieldrefConstant, times(1)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitFieldrefConstant with LibraryClass doesn't throw exceptions.
     */
    @Test
    public void testVisitFieldrefConstant_withLibraryClass_works() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitFieldrefConstant(libraryClass, mockFieldrefConstant));
        verify(mockFieldrefConstant, times(1)).referencedClassAccept(eq(simplifier));
    }
}
