package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link LineNumberLinearizer#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in LineNumberLinearizer is an empty implementation (no-op).
 * It's part of the ClassVisitor interface but doesn't perform any actions because:
 * - The actual line number linearization is done in visitProgramClass()
 * - visitAnyClass() serves as a default no-op for when the visitor is called on non-specific class types
 *
 * These tests verify that:
 * 1. The method doesn't throw exceptions
 * 2. The method doesn't modify the class being visited
 * 3. The method can handle different types of Clazz instances
 * 4. The method is properly integrated into the visitor pattern
 */
public class LineNumberLinearizerClaude_visitAnyClassTest {

    private LineNumberLinearizer linearizer;

    @BeforeEach
    public void setUp() {
        linearizer = new LineNumberLinearizer();
    }

    // ========================================
    // Basic Functionality Tests
    // ========================================

    /**
     * Tests that visitAnyClass does not throw any exceptions with a ProgramClass.
     * The empty implementation should execute without errors.
     */
    @Test
    public void testVisitAnyClass_withProgramClass_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitAnyClass(programClass),
            "visitAnyClass should not throw exception with ProgramClass");
    }

    /**
     * Tests that visitAnyClass does not throw any exceptions with a LibraryClass.
     * The empty implementation should execute without errors.
     */
    @Test
    public void testVisitAnyClass_withLibraryClass_doesNotThrow() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitAnyClass(libraryClass),
            "visitAnyClass should not throw exception with LibraryClass");
    }

    /**
     * Tests that visitAnyClass does not throw exceptions with a mock Clazz.
     * Verifies the method doesn't make any calls to the Clazz parameter.
     */
    @Test
    public void testVisitAnyClass_withMockClazz_doesNotInteractWithClazz() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);

        // Act
        linearizer.visitAnyClass(mockClazz);

        // Assert - verify no interactions with the mock (empty method doesn't call anything)
        verifyNoInteractions(mockClazz);
    }

    /**
     * Tests that visitAnyClass with null does not throw.
     * Since the method body is empty, it doesn't dereference the parameter.
     */
    @Test
    public void testVisitAnyClass_withNull_doesNotThrow() {
        // Act & Assert
        // Empty methods don't dereference the parameter, so no NPE is expected
        assertDoesNotThrow(() -> linearizer.visitAnyClass(null),
            "Empty visitAnyClass should not throw with null since it doesn't use the parameter");
    }

    // ========================================
    // State Preservation Tests
    // ========================================

    /**
     * Tests that visitAnyClass does not modify the ProgramClass.
     * Since the method is empty, the class should remain unchanged.
     */
    @Test
    public void testVisitAnyClass_withProgramClass_doesNotModifyClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        Object originalProcessingInfo = new Object();
        programClass.setProcessingInfo(originalProcessingInfo);
        int originalAccessFlags = programClass.u2accessFlags;

        // Act
        linearizer.visitAnyClass(programClass);

        // Assert
        assertSame(originalProcessingInfo, programClass.getProcessingInfo(),
            "Processing info should not be modified");
        assertEquals(originalAccessFlags, programClass.u2accessFlags,
            "Access flags should not be modified");
    }

    /**
     * Tests that visitAnyClass does not modify the LibraryClass.
     * Since the method is empty, the class should remain unchanged.
     */
    @Test
    public void testVisitAnyClass_withLibraryClass_doesNotModifyClass() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        Object originalProcessingInfo = new Object();
        libraryClass.setProcessingInfo(originalProcessingInfo);
        int originalAccessFlags = libraryClass.u2accessFlags;

        // Act
        linearizer.visitAnyClass(libraryClass);

        // Assert
        assertSame(originalProcessingInfo, libraryClass.getProcessingInfo(),
            "Processing info should not be modified");
        assertEquals(originalAccessFlags, libraryClass.u2accessFlags,
            "Access flags should not be modified");
    }

    /**
     * Tests that visitAnyClass preserves class state across multiple calls.
     */
    @Test
    public void testVisitAnyClass_preservesClassStateAcrossMultipleCalls() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        Object originalInfo = new Object();
        programClass.setProcessingInfo(originalInfo);

        // Act - call many times
        for (int i = 0; i < 100; i++) {
            linearizer.visitAnyClass(programClass);
        }

        // Assert - processing info should never change
        assertSame(originalInfo, programClass.getProcessingInfo(),
            "Processing info should remain unchanged after 100 calls");
    }

    // ========================================
    // Multiple Call Tests
    // ========================================

    /**
     * Tests that visitAnyClass can be called multiple times on the same class.
     * The empty method should handle repeated calls without issues.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - call multiple times
        assertDoesNotThrow(() -> {
            linearizer.visitAnyClass(programClass);
            linearizer.visitAnyClass(programClass);
            linearizer.visitAnyClass(programClass);
        }, "Multiple calls to visitAnyClass should not throw");
    }

    /**
     * Tests that visitAnyClass can be called on multiple different classes.
     * The empty method should handle different class instances.
     */
    @Test
    public void testVisitAnyClass_withMultipleClasses_doesNotThrow() {
        // Arrange
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer.visitAnyClass(programClass1);
            linearizer.visitAnyClass(programClass2);
            linearizer.visitAnyClass(libraryClass);
        }, "visitAnyClass should handle multiple different classes");
    }

    /**
     * Tests that visitAnyClass can be called in rapid succession without issues.
     * Verifies no state issues occur.
     */
    @Test
    public void testVisitAnyClass_rapidSuccessiveCalls_noStateIssues() {
        // Arrange
        ProgramClass[] classes = new ProgramClass[100];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = new ProgramClass();
        }

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (ProgramClass clazz : classes) {
                linearizer.visitAnyClass(clazz);
            }
        }, "Rapid successive calls should not cause issues");
    }

    // ========================================
    // Visitor Pattern Integration Tests
    // ========================================

    /**
     * Tests that visitAnyClass can be used through the ClassVisitor interface.
     * Verifies proper integration with the visitor pattern.
     */
    @Test
    public void testVisitAnyClass_throughClassVisitorInterface_works() {
        // Arrange
        ClassVisitor visitor = linearizer;
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitAnyClass(programClass),
            "visitAnyClass should work when called through ClassVisitor interface");
    }

    /**
     * Tests that the empty visitAnyClass method doesn't prevent the linearizer
     * from being used as a ClassVisitor in a visitor chain.
     */
    @Test
    public void testVisitAnyClass_inVisitorChain_doesNotBreakChain() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        boolean[] visited = new boolean[1];

        ClassVisitor chainedVisitor = new ClassVisitor() {
            @Override
            public void visitAnyClass(Clazz clazz) {
                linearizer.visitAnyClass(clazz);
                visited[0] = true;
            }
        };

        // Act
        chainedVisitor.visitAnyClass(programClass);

        // Assert
        assertTrue(visited[0], "Visitor chain should complete successfully");
    }

    /**
     * Tests that visitAnyClass is truly a no-op by verifying it doesn't call
     * any methods on the Clazz parameter.
     */
    @Test
    public void testVisitAnyClass_isNoOp_verifiesNoMethodCalls() {
        // Arrange
        Clazz spyClazz = spy(ProgramClass.class);

        // Act
        linearizer.visitAnyClass(spyClazz);

        // Assert - The method should not call accept on the Clazz
        verify(spyClazz, never()).accept(any(ClassVisitor.class));
    }

    // ========================================
    // Performance Tests
    // ========================================

    /**
     * Tests that visitAnyClass completes execution quickly.
     * Since it's empty, it should return immediately.
     */
    @Test
    public void testVisitAnyClass_executesQuickly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        long startTime = System.nanoTime();

        // Act
        for (int i = 0; i < 10000; i++) {
            linearizer.visitAnyClass(programClass);
        }
        long endTime = System.nanoTime();

        // Assert - 10000 calls should complete very quickly (less than 100ms)
        long durationMs = (endTime - startTime) / 1_000_000;
        assertTrue(durationMs < 100,
            "10000 calls to empty visitAnyClass should complete in less than 100ms, took: " + durationMs + "ms");
    }

    // ========================================
    // Different Instance Tests
    // ========================================

    /**
     * Tests that multiple linearizer instances all have empty visitAnyClass behavior.
     * Verifies consistency across instances.
     */
    @Test
    public void testVisitAnyClass_multipleInstances_consistentBehavior() {
        // Arrange
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - both instances should behave identically
        assertDoesNotThrow(() -> linearizer1.visitAnyClass(programClass),
            "First linearizer should not throw");
        assertDoesNotThrow(() -> linearizer2.visitAnyClass(programClass),
            "Second linearizer should not throw");
    }

    /**
     * Tests that visitAnyClass doesn't interfere with subsequent visits.
     * The empty visitAnyClass should not affect the linearizer's state.
     */
    @Test
    public void testVisitAnyClass_doesNotAffectSubsequentVisits() {
        // Arrange
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();

        // Act - call visitAnyClass, then check linearizer is still usable
        linearizer.visitAnyClass(programClass1);

        // Assert - linearizer should still be usable (doesn't throw)
        assertDoesNotThrow(() -> linearizer.visitAnyClass(programClass2),
            "Linearizer should remain usable after calling visitAnyClass");
    }

    // ========================================
    // Various Class State Tests
    // ========================================

    /**
     * Tests that visitAnyClass works correctly with classes that have various states.
     * The empty method should handle all class states uniformly.
     */
    @Test
    public void testVisitAnyClass_withVariousClassStates_handlesUniformly() {
        // Arrange
        ProgramClass emptyClass = new ProgramClass();

        ProgramClass classWithProcessingInfo = new ProgramClass();
        classWithProcessingInfo.setProcessingInfo(new Object());

        ProgramClass classWithAccessFlags = new ProgramClass();
        classWithAccessFlags.u2accessFlags = 0x0001; // ACC_PUBLIC

        // Act & Assert - all should work the same
        assertDoesNotThrow(() -> linearizer.visitAnyClass(emptyClass),
            "Should handle empty class");
        assertDoesNotThrow(() -> linearizer.visitAnyClass(classWithProcessingInfo),
            "Should handle class with processing info");
        assertDoesNotThrow(() -> linearizer.visitAnyClass(classWithAccessFlags),
            "Should handle class with access flags");
    }

    /**
     * Tests that visitAnyClass works with classes in different configurations.
     */
    @Test
    public void testVisitAnyClass_withDifferentClassConfigurations_consistent() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        class1.u2accessFlags = 0x0001; // ACC_PUBLIC

        ProgramClass class2 = new ProgramClass();
        class2.u2accessFlags = 0x0002; // ACC_PRIVATE

        LibraryClass libClass = new LibraryClass();
        libClass.u2accessFlags = 0x0400; // ACC_ABSTRACT

        // Act & Assert - all should behave consistently
        assertDoesNotThrow(() -> {
            linearizer.visitAnyClass(class1);
            linearizer.visitAnyClass(class2);
            linearizer.visitAnyClass(libClass);
        }, "Should handle classes with different configurations");
    }

    // ========================================
    // Edge Case Tests
    // ========================================

    /**
     * Tests visitAnyClass after creating multiple linearizers.
     * Verifies that linearizer creation doesn't affect behavior.
     */
    @Test
    public void testVisitAnyClass_afterCreatingMultipleLinearizers() {
        // Arrange
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer3 = new LineNumberLinearizer();
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - each linearizer should work independently
        assertDoesNotThrow(() -> linearizer1.visitAnyClass(programClass),
            "First linearizer should work");
        assertDoesNotThrow(() -> linearizer2.visitAnyClass(programClass),
            "Second linearizer should work");
        assertDoesNotThrow(() -> linearizer3.visitAnyClass(programClass),
            "Third linearizer should work");
    }

    /**
     * Tests that visitAnyClass with alternating class types works correctly.
     */
    @Test
    public void testVisitAnyClass_withAlternatingClassTypes_consistent() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert - alternate between class types
        assertDoesNotThrow(() -> {
            linearizer.visitAnyClass(programClass);
            linearizer.visitAnyClass(libraryClass);
            linearizer.visitAnyClass(programClass);
            linearizer.visitAnyClass(libraryClass);
        }, "Should handle alternating class types");
    }

    /**
     * Tests that visitAnyClass can be called on freshly created classes.
     */
    @Test
    public void testVisitAnyClass_withFreshlyCreatedClasses() {
        // Act & Assert - create and visit classes inline
        assertDoesNotThrow(() -> linearizer.visitAnyClass(new ProgramClass()),
            "Should handle freshly created ProgramClass");
        assertDoesNotThrow(() -> linearizer.visitAnyClass(new LibraryClass()),
            "Should handle freshly created LibraryClass");
    }

    /**
     * Tests that visitAnyClass works immediately after linearizer construction.
     */
    @Test
    public void testVisitAnyClass_immediatelyAfterConstruction() {
        // Arrange & Act
        LineNumberLinearizer newLinearizer = new LineNumberLinearizer();
        ProgramClass programClass = new ProgramClass();

        // Assert
        assertDoesNotThrow(() -> newLinearizer.visitAnyClass(programClass),
            "visitAnyClass should work immediately after construction");
    }

    /**
     * Tests that visitAnyClass maintains consistent behavior across many sequential calls.
     */
    @Test
    public void testVisitAnyClass_manySequentialCalls_consistent() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - call many times
        for (int i = 0; i < 1000; i++) {
            final int iteration = i;
            assertDoesNotThrow(() -> linearizer.visitAnyClass(programClass),
                "Call " + iteration + " should not throw");
        }
    }

    /**
     * Tests that visitAnyClass works with both mock and real Clazz instances.
     */
    @Test
    public void testVisitAnyClass_withMockAndRealInstances_bothWork() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        ProgramClass realClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer.visitAnyClass(mockClazz);
            linearizer.visitAnyClass(realClass);
            linearizer.visitAnyClass(mockClazz);
            linearizer.visitAnyClass(realClass);
        }, "Should handle both mock and real Clazz instances");
    }

    /**
     * Tests that multiple calls to visitAnyClass don't accumulate state.
     */
    @Test
    public void testVisitAnyClass_doesNotAccumulateState() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.setProcessingInfo("initial");

        // Act - call multiple times
        linearizer.visitAnyClass(programClass);
        linearizer.visitAnyClass(programClass);
        linearizer.visitAnyClass(programClass);

        // Assert - processing info should still be the original
        assertEquals("initial", programClass.getProcessingInfo(),
            "Processing info should not change after multiple calls");
    }
}
