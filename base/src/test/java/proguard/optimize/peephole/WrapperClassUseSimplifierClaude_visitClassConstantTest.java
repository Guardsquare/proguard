package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.instruction.visitor.InstructionVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link WrapperClassUseSimplifier#visitClassConstant(Clazz, ClassConstant)}.
 *
 * The visitClassConstant method in WrapperClassUseSimplifier is called when a class reference
 * constant is encountered (e.g., in .class expressions or Type literals). The method:
 * 1. Calls referencedClassAccept on the ClassConstant to visit the class being referenced
 * 2. This allows the simplifier to check if the referenced class is a wrapper class using the ClassVisitor
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
public class WrapperClassUseSimplifierClaude_visitClassConstantTest {

    private WrapperClassUseSimplifier simplifier;
    private InstructionVisitor mockExtraVisitor;
    private Clazz mockClazz;
    private ClassConstant mockClassConstant;

    @BeforeEach
    public void setUp() {
        mockExtraVisitor = mock(InstructionVisitor.class);
        mockClazz = mock(Clazz.class);
        mockClassConstant = mock(ClassConstant.class);
    }

    // ========================================
    // Core Behavior Tests
    // ========================================

    /**
     * Tests that visitClassConstant calls referencedClassAccept on the ClassConstant.
     * This is the core behavior - delegating to the class constant to visit the referenced class.
     */
    @Test
    public void testVisitClassConstant_callsReferencedClassAccept() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act
        simplifier.visitClassConstant(mockClazz, mockClassConstant);

        // Assert
        verify(mockClassConstant, times(1)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitClassConstant passes the simplifier itself as the ClassVisitor.
     * The simplifier implements ClassVisitor to handle wrapper class detection.
     */
    @Test
    public void testVisitClassConstant_passesSimplifierAsVisitor() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act
        simplifier.visitClassConstant(mockClazz, mockClassConstant);

        // Assert - verify the simplifier itself is passed as the visitor
        verify(mockClassConstant).referencedClassAccept(same(simplifier));
    }

    /**
     * Tests that visitClassConstant works with valid mock objects without throwing exceptions.
     */
    @Test
    public void testVisitClassConstant_withValidMocks_doesNotThrowException() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> simplifier.visitClassConstant(mockClazz, mockClassConstant));
    }

    /**
     * Tests that visitClassConstant with no-arg constructor works correctly.
     */
    @Test
    public void testVisitClassConstant_withNoArgConstructor_callsReferencedClassAccept() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier();

        // Act
        simplifier.visitClassConstant(mockClazz, mockClassConstant);

        // Assert
        verify(mockClassConstant, times(1)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitClassConstant with extra visitor works correctly.
     * The extra visitor is not directly involved in this method.
     */
    @Test
    public void testVisitClassConstant_withExtraVisitor_callsReferencedClassAccept() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act
        simplifier.visitClassConstant(mockClazz, mockClassConstant);

        // Assert
        verify(mockClassConstant, times(1)).referencedClassAccept(eq(simplifier));
        verifyNoInteractions(mockExtraVisitor);
    }

    // ========================================
    // Null Parameter Tests
    // ========================================

    /**
     * Tests that visitClassConstant can be called with null Clazz parameter.
     * The method should handle null clazz gracefully since it doesn't directly use it.
     */
    @Test
    public void testVisitClassConstant_withNullClazz_doesNotThrowException() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> simplifier.visitClassConstant(null, mockClassConstant));

        // Verify the method still calls referencedClassAccept
        verify(mockClassConstant, times(1)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitClassConstant with null ClassConstant throws NullPointerException.
     * This should result in NPE since the method calls a method on classConstant.
     */
    @Test
    public void testVisitClassConstant_withNullClassConstant_throwsNullPointerException() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> simplifier.visitClassConstant(mockClazz, null));
    }

    /**
     * Tests that visitClassConstant with both null parameters throws NullPointerException.
     */
    @Test
    public void testVisitClassConstant_withBothNullParameters_throwsNullPointerException() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> simplifier.visitClassConstant(null, null));
    }

    // ========================================
    // Multiple Invocation Tests
    // ========================================

    /**
     * Tests that visitClassConstant can be called multiple times in succession.
     * Each call should invoke referencedClassAccept.
     */
    @Test
    public void testVisitClassConstant_calledMultipleTimes_invokesAcceptMethodEachTime() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act
        simplifier.visitClassConstant(mockClazz, mockClassConstant);
        simplifier.visitClassConstant(mockClazz, mockClassConstant);
        simplifier.visitClassConstant(mockClazz, mockClassConstant);

        // Assert
        verify(mockClassConstant, times(3)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitClassConstant works correctly with rapid successive calls.
     */
    @Test
    public void testVisitClassConstant_rapidSuccessiveCalls_eachInvokesAccept() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act - call many times rapidly
        for (int i = 0; i < 100; i++) {
            simplifier.visitClassConstant(mockClazz, mockClassConstant);
        }

        // Assert
        verify(mockClassConstant, times(100)).referencedClassAccept(eq(simplifier));
    }

    // ========================================
    // Different Parameter Type Tests
    // ========================================

    /**
     * Tests that visitClassConstant doesn't directly interact with the Clazz parameter.
     * The clazz is passed as a context parameter but not used in this method.
     */
    @Test
    public void testVisitClassConstant_doesNotInteractWithClazz() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act
        simplifier.visitClassConstant(mockClazz, mockClassConstant);

        // Assert
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitClassConstant works with different Clazz implementations.
     */
    @Test
    public void testVisitClassConstant_withDifferentClazzTypes_doesNotThrowException() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);
        Clazz programClass = mock(ProgramClass.class);
        Clazz libraryClass = mock(LibraryClass.class);

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitClassConstant(programClass, mockClassConstant));
        assertDoesNotThrow(() -> simplifier.visitClassConstant(libraryClass, mockClassConstant));

        // Verify both calls invoked referencedClassAccept
        verify(mockClassConstant, times(2)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitClassConstant works with different ClassConstant mock instances.
     */
    @Test
    public void testVisitClassConstant_withDifferentClassConstants_callsEachOne() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);
        ClassConstant const3 = mock(ClassConstant.class);

        // Act
        simplifier.visitClassConstant(mockClazz, const1);
        simplifier.visitClassConstant(mockClazz, const2);
        simplifier.visitClassConstant(mockClazz, const3);

        // Assert - each constant should have its referencedClassAccept called
        verify(const1, times(1)).referencedClassAccept(eq(simplifier));
        verify(const2, times(1)).referencedClassAccept(eq(simplifier));
        verify(const3, times(1)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitClassConstant works with different combinations of parameters.
     */
    @Test
    public void testVisitClassConstant_withVariousClazzAndConstantCombinations() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);
        Clazz clazz1 = mock(ProgramClass.class);
        Clazz clazz2 = mock(LibraryClass.class);
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);

        // Act
        simplifier.visitClassConstant(clazz1, const1);
        simplifier.visitClassConstant(clazz1, const2);
        simplifier.visitClassConstant(clazz2, const1);
        simplifier.visitClassConstant(clazz2, const2);

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
     * call visitClassConstant.
     */
    @Test
    public void testVisitClassConstant_multipleInstances_independent() {
        // Arrange
        InstructionVisitor mockVisitor1 = mock(InstructionVisitor.class);
        InstructionVisitor mockVisitor2 = mock(InstructionVisitor.class);
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(mockVisitor1);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(mockVisitor2);

        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);

        // Act
        simplifier1.visitClassConstant(mockClazz, const1);
        simplifier2.visitClassConstant(mockClazz, const2);

        // Assert - each constant should have been visited by its respective simplifier
        verify(const1, times(1)).referencedClassAccept(eq(simplifier1));
        verify(const2, times(1)).referencedClassAccept(eq(simplifier2));

        // Verify extra visitors were not invoked (they're only for instruction visiting)
        verifyNoInteractions(mockVisitor1);
        verifyNoInteractions(mockVisitor2);
    }

    /**
     * Tests that the same simplifier instance can visit different class constants.
     */
    @Test
    public void testVisitClassConstant_sameInstanceDifferentConstants() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        ClassConstant[] constants = new ClassConstant[5];
        for (int i = 0; i < 5; i++) {
            constants[i] = mock(ClassConstant.class);
        }

        // Act
        for (ClassConstant constant : constants) {
            simplifier.visitClassConstant(mockClazz, constant);
        }

        // Assert - each constant should have been visited exactly once
        for (ClassConstant constant : constants) {
            verify(constant, times(1)).referencedClassAccept(eq(simplifier));
        }
    }

    // ========================================
    // Interface Contract Tests
    // ========================================

    /**
     * Tests that visitClassConstant properly implements the ConstantVisitor interface.
     */
    @Test
    public void testVisitClassConstant_asConstantVisitor_properImplementation() {
        // Arrange
        WrapperClassUseSimplifier visitor = new WrapperClassUseSimplifier(null);

        // Act & Assert - use as ConstantVisitor interface
        assertDoesNotThrow(() -> visitor.visitClassConstant(mockClazz, mockClassConstant),
                "visitClassConstant should work correctly through ConstantVisitor interface");

        // Verify the delegation occurred
        verify(mockClassConstant).referencedClassAccept(same(visitor));
    }

    /**
     * Tests that visitClassConstant returns normally (void method completes).
     */
    @Test
    public void testVisitClassConstant_returnsNormally() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);

        // Act & Assert - should complete without exceptions or blocking
        assertDoesNotThrow(() -> {
            simplifier.visitClassConstant(mockClazz, mockClassConstant);
            // If we reach this point, the method returned normally
        }, "visitClassConstant should return normally");
    }

    // ========================================
    // State and Consistency Tests
    // ========================================

    /**
     * Tests that calling visitClassConstant doesn't interfere with subsequent calls.
     */
    @Test
    public void testVisitClassConstant_multipleCallsDoNotInterfere() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);

        // Act
        simplifier.visitClassConstant(mockClazz, const1);
        simplifier.visitClassConstant(mockClazz, const2);
        simplifier.visitClassConstant(mockClazz, const1); // call const1 again

        // Assert - verify correct number of calls to each
        verify(const1, times(2)).referencedClassAccept(eq(simplifier));
        verify(const2, times(1)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitClassConstant maintains consistent behavior across constructor variants.
     */
    @Test
    public void testVisitClassConstant_withBothConstructors_consistentBehavior() {
        // Arrange
        WrapperClassUseSimplifier simplifierNoArg = new WrapperClassUseSimplifier();
        WrapperClassUseSimplifier simplifierWithVisitor = new WrapperClassUseSimplifier(mockExtraVisitor);
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);

        // Act
        simplifierNoArg.visitClassConstant(mockClazz, const1);
        simplifierWithVisitor.visitClassConstant(mockClazz, const2);

        // Assert - both should delegate to referencedClassAccept
        verify(const1, times(1)).referencedClassAccept(eq(simplifierNoArg));
        verify(const2, times(1)).referencedClassAccept(eq(simplifierWithVisitor));
    }

    /**
     * Tests that the extra visitor is not involved in visitClassConstant.
     * This method only deals with constant visiting, not instruction visiting.
     */
    @Test
    public void testVisitClassConstant_doesNotInvolveExtraVisitor() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);

        // Act
        simplifier.visitClassConstant(mockClazz, mockClassConstant);
        simplifier.visitClassConstant(mockClazz, mockClassConstant);
        simplifier.visitClassConstant(mockClazz, mockClassConstant);

        // Assert - extra visitor should never be called
        verifyNoInteractions(mockExtraVisitor);
    }

    // ========================================
    // Edge Case and Stress Tests
    // ========================================

    /**
     * Tests that visitClassConstant handles alternating between different instances.
     */
    @Test
    public void testVisitClassConstant_alternatingInstances() {
        // Arrange
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(null);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(mockExtraVisitor);
        ClassConstant constant = mock(ClassConstant.class);

        // Act - alternate between simplifiers
        simplifier1.visitClassConstant(mockClazz, constant);
        simplifier2.visitClassConstant(mockClazz, constant);
        simplifier1.visitClassConstant(mockClazz, constant);
        simplifier2.visitClassConstant(mockClazz, constant);

        // Assert - should have 4 total calls with correct visitors
        verify(constant, times(2)).referencedClassAccept(eq(simplifier1));
        verify(constant, times(2)).referencedClassAccept(eq(simplifier2));
    }

    /**
     * Tests that visitClassConstant works correctly with sequential different class instances.
     */
    @Test
    public void testVisitClassConstant_withSequentialDifferentClasses() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);
        Clazz[] classes = new Clazz[5];
        for (int i = 0; i < 5; i++) {
            classes[i] = mock(Clazz.class);
        }

        // Act
        for (Clazz clazz : classes) {
            simplifier.visitClassConstant(clazz, mockClassConstant);
        }

        // Assert - classConstant should be visited 5 times
        verify(mockClassConstant, times(5)).referencedClassAccept(eq(simplifier));

        // Verify no clazz was interacted with
        for (Clazz clazz : classes) {
            verifyNoInteractions(clazz);
        }
    }

    /**
     * Tests that visitClassConstant correctly passes the simplifier reference,
     * not some other object or null.
     */
    @Test
    public void testVisitClassConstant_passesCorrectSimplifierReference() {
        // Arrange
        WrapperClassUseSimplifier simplifier1 = new WrapperClassUseSimplifier(null);
        WrapperClassUseSimplifier simplifier2 = new WrapperClassUseSimplifier(null);
        ClassConstant constant = mock(ClassConstant.class);

        // Act
        simplifier1.visitClassConstant(mockClazz, constant);
        simplifier2.visitClassConstant(mockClazz, constant);

        // Assert - verify each simplifier passed itself, not the other
        verify(constant, times(1)).referencedClassAccept(same(simplifier1));
        verify(constant, times(1)).referencedClassAccept(same(simplifier2));
        verify(constant, never()).referencedClassAccept(null);
    }

    /**
     * Tests that visitClassConstant maintains proper method signature and behavior
     * when called with various timing patterns.
     */
    @Test
    public void testVisitClassConstant_withVariousTimingPatterns() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        ClassConstant const1 = mock(ClassConstant.class);
        ClassConstant const2 = mock(ClassConstant.class);

        // Act - various timing patterns
        simplifier.visitClassConstant(mockClazz, const1);
        simplifier.visitClassConstant(mockClazz, const1);
        simplifier.visitClassConstant(mockClazz, const2);
        simplifier.visitClassConstant(mockClazz, const1);
        simplifier.visitClassConstant(mockClazz, const2);

        // Assert
        verify(const1, times(3)).referencedClassAccept(eq(simplifier));
        verify(const2, times(2)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitClassConstant does not modify any visible state
     * that would affect other visitor methods.
     */
    @Test
    public void testVisitClassConstant_doesNotAffectOtherVisitorMethods() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        ClassConstant constant = mock(ClassConstant.class);

        // Act - call visitClassConstant
        simplifier.visitClassConstant(mockClazz, constant);

        // Assert - should still be able to call it again without issues
        assertDoesNotThrow(() -> simplifier.visitClassConstant(mockClazz, constant),
                "Subsequent calls should work normally");

        // Verify total of 2 calls
        verify(constant, times(2)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitClassConstant with ProgramClass doesn't throw exceptions.
     */
    @Test
    public void testVisitClassConstant_withProgramClass_works() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(null);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitClassConstant(programClass, mockClassConstant));
        verify(mockClassConstant, times(1)).referencedClassAccept(eq(simplifier));
    }

    /**
     * Tests that visitClassConstant with LibraryClass doesn't throw exceptions.
     */
    @Test
    public void testVisitClassConstant_withLibraryClass_works() {
        // Arrange
        simplifier = new WrapperClassUseSimplifier(mockExtraVisitor);
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> simplifier.visitClassConstant(libraryClass, mockClassConstant));
        verify(mockClassConstant, times(1)).referencedClassAccept(eq(simplifier));
    }
}
