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
 * Test class for {@link WrapperClassMerger#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in WrapperClassMerger is a no-op implementation (empty method body).
 * It's required by the ClassVisitor interface but intentionally does nothing, as WrapperClassMerger
 * only operates on ProgramClass instances via the visitProgramClass method.
 *
 * These tests verify that the method:
 * 1. Does not throw exceptions when called
 * 2. Does not modify the class or merger state
 * 3. Works correctly with different types of Clazz implementations
 */
public class WrapperClassMergerClaude_visitAnyClassTest {

    private WrapperClassMerger wrapperClassMerger;

    @BeforeEach
    public void setUp() {
        // Create WrapperClassMerger with default settings
        wrapperClassMerger = new WrapperClassMerger(false);
    }

    // ========================================
    // visitAnyClass Tests - No-op Verification
    // ========================================

    /**
     * Tests visitAnyClass with a ProgramClass.
     * The method should do nothing (no-op) and not throw any exceptions.
     */
    @Test
    public void testVisitAnyClass_withProgramClass_doesNothing() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0;
        programClass.u4version = 50;
        int originalAccessFlags = programClass.u2accessFlags;
        int originalVersion = programClass.u4version;

        // Act
        assertDoesNotThrow(() -> wrapperClassMerger.visitAnyClass(programClass),
                "visitAnyClass should not throw exceptions");

        // Assert - the class should be completely unchanged
        assertEquals(originalAccessFlags, programClass.u2accessFlags,
                "visitAnyClass should not modify access flags");
        assertEquals(originalVersion, programClass.u4version,
                "visitAnyClass should not modify class version");
    }

    /**
     * Tests visitAnyClass with a LibraryClass.
     * The method should do nothing (no-op) and not throw any exceptions.
     */
    @Test
    public void testVisitAnyClass_withLibraryClass_doesNothing() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        libraryClass.u2accessFlags = 0;
        int originalAccessFlags = libraryClass.u2accessFlags;

        // Act
        assertDoesNotThrow(() -> wrapperClassMerger.visitAnyClass(libraryClass),
                "visitAnyClass should not throw exceptions with LibraryClass");

        // Assert - the library class should be unchanged
        assertEquals(originalAccessFlags, libraryClass.u2accessFlags,
                "visitAnyClass should not modify library class access flags");
    }

    /**
     * Tests that visitAnyClass can be called multiple times without side effects.
     * Each call should remain a no-op.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_remainsNoOp() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 42; // Arbitrary value
        int originalAccessFlags = programClass.u2accessFlags;

        // Act - call multiple times
        wrapperClassMerger.visitAnyClass(programClass);
        wrapperClassMerger.visitAnyClass(programClass);
        wrapperClassMerger.visitAnyClass(programClass);

        // Assert - should still be unchanged
        assertEquals(originalAccessFlags, programClass.u2accessFlags,
                "Multiple calls to visitAnyClass should not modify the class");
    }

    /**
     * Tests visitAnyClass with different WrapperClassMerger configurations.
     * The no-op behavior should be independent of WrapperClassMerger settings.
     */
    @Test
    public void testVisitAnyClass_withDifferentMergerSettings_alwaysNoOp() {
        // Arrange - WrapperClassMerger with different settings
        WrapperClassMerger merger1 = new WrapperClassMerger(true);
        WrapperClassMerger merger2 = new WrapperClassMerger(false);

        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 100;
        int originalAccessFlags = programClass.u2accessFlags;

        // Act - call visitAnyClass with each merger
        merger1.visitAnyClass(programClass);
        merger2.visitAnyClass(programClass);

        // Assert - class should remain unchanged regardless of settings
        assertEquals(originalAccessFlags, programClass.u2accessFlags,
                "visitAnyClass should be no-op regardless of WrapperClassMerger configuration");
    }

    /**
     * Tests visitAnyClass with classes that have various access flags.
     * The method should not modify any flags.
     */
    @Test
    public void testVisitAnyClass_withVariousAccessFlags_doesNotModify() {
        // Arrange - classes with different access flags
        ProgramClass publicClass = new ProgramClass();
        publicClass.u2accessFlags = 0x0001; // ACC_PUBLIC

        ProgramClass finalClass = new ProgramClass();
        finalClass.u2accessFlags = 0x0010; // ACC_FINAL

        ProgramClass abstractClass = new ProgramClass();
        abstractClass.u2accessFlags = 0x0400; // ACC_ABSTRACT

        int originalPublicFlags = publicClass.u2accessFlags;
        int originalFinalFlags = finalClass.u2accessFlags;
        int originalAbstractFlags = abstractClass.u2accessFlags;

        // Act
        wrapperClassMerger.visitAnyClass(publicClass);
        wrapperClassMerger.visitAnyClass(finalClass);
        wrapperClassMerger.visitAnyClass(abstractClass);

        // Assert - all flags should remain unchanged
        assertEquals(originalPublicFlags, publicClass.u2accessFlags,
                "visitAnyClass should not modify public class flags");
        assertEquals(originalFinalFlags, finalClass.u2accessFlags,
                "visitAnyClass should not modify final class flags");
        assertEquals(originalAbstractFlags, abstractClass.u2accessFlags,
                "visitAnyClass should not modify abstract class flags");
    }

    /**
     * Tests that visitAnyClass does not affect the merger's state.
     * Calling visitAnyClass should not interfere with the WrapperClassMerger's state.
     */
    @Test
    public void testVisitAnyClass_doesNotAffectMergerState() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        class1.u2accessFlags = 50;
        ProgramClass class2 = new ProgramClass();
        class2.u2accessFlags = 100;

        // Act - call visitAnyClass first
        wrapperClassMerger.visitAnyClass(class1);

        int class2OriginalFlags = class2.u2accessFlags;

        // Call visitAnyClass on second class
        wrapperClassMerger.visitAnyClass(class2);

        // Assert - second class should still be unchanged
        assertEquals(class2OriginalFlags, class2.u2accessFlags,
                "visitAnyClass should not affect subsequent calls");
    }

    /**
     * Tests that visitAnyClass does not interact with any merging state.
     * It should truly be a no-op, not preparing or affecting any merge operations.
     */
    @Test
    public void testVisitAnyClass_doesNotPrepareMerge() {
        // Arrange
        ProgramClass sourceClass = new ProgramClass();
        sourceClass.u2accessFlags = 0;
        sourceClass.u4version = 50;

        // Act - call visitAnyClass (which should do nothing)
        wrapperClassMerger.visitAnyClass(sourceClass);

        // Assert - since visitAnyClass does nothing, the source class should be
        // completely unmodified and no merge should have been initiated
        // We verify this by checking that the class remains in its original state
        assertEquals(0, sourceClass.u2accessFlags,
                "visitAnyClass should not prepare or initiate any merge");
    }

    /**
     * Tests visitAnyClass with null-like minimal class configurations.
     * The no-op should handle even minimally initialized classes.
     */
    @Test
    public void testVisitAnyClass_withMinimalClass_doesNotThrow() {
        // Arrange - a minimally initialized class
        ProgramClass minimalClass = new ProgramClass();
        // Don't set any fields - leave everything at defaults

        // Act & Assert
        assertDoesNotThrow(() -> wrapperClassMerger.visitAnyClass(minimalClass),
                "visitAnyClass should not throw even with minimal class");
    }

    /**
     * Tests that multiple WrapperClassMerger instances with visitAnyClass
     * don't interfere with each other.
     */
    @Test
    public void testVisitAnyClass_multipleInstances_independent() {
        // Arrange
        WrapperClassMerger merger1 = new WrapperClassMerger(true);
        WrapperClassMerger merger2 = new WrapperClassMerger(false);

        ProgramClass testClass = new ProgramClass();
        testClass.u2accessFlags = 123;
        int originalFlags = testClass.u2accessFlags;

        // Act
        merger1.visitAnyClass(testClass);
        merger2.visitAnyClass(testClass);

        // Assert - both no-ops should leave class unchanged
        assertEquals(originalFlags, testClass.u2accessFlags,
                "Multiple merger instances calling visitAnyClass should not affect the class");
    }

    /**
     * Tests visitAnyClass with a merger that has an extraClassVisitor.
     * The no-op behavior should remain the same, and the extra visitor should not be invoked.
     */
    @Test
    public void testVisitAnyClass_withExtraClassVisitor_doesNotInvokeVisitor() {
        // Arrange
        ClassVisitor extraVisitor = mock(ClassVisitor.class);
        WrapperClassMerger merger = new WrapperClassMerger(true, extraVisitor);

        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 50;
        int originalFlags = programClass.u2accessFlags;

        // Act
        merger.visitAnyClass(programClass);

        // Assert - the extra visitor should not be invoked
        verifyNoInteractions(extraVisitor);
        assertEquals(originalFlags, programClass.u2accessFlags,
                "visitAnyClass should not modify class even with extra visitor");
    }

    /**
     * Tests visitAnyClass with different class versions.
     * The method should not modify version information.
     */
    @Test
    public void testVisitAnyClass_withDifferentVersions_doesNotModify() {
        // Arrange - classes with different versions
        ProgramClass java6Class = new ProgramClass();
        java6Class.u4version = 50; // Java 6

        ProgramClass java8Class = new ProgramClass();
        java8Class.u4version = 52; // Java 8

        ProgramClass java11Class = new ProgramClass();
        java11Class.u4version = 55; // Java 11

        // Act
        wrapperClassMerger.visitAnyClass(java6Class);
        wrapperClassMerger.visitAnyClass(java8Class);
        wrapperClassMerger.visitAnyClass(java11Class);

        // Assert - all versions should remain unchanged
        assertEquals(50, java6Class.u4version,
                "visitAnyClass should not modify Java 6 class version");
        assertEquals(52, java8Class.u4version,
                "visitAnyClass should not modify Java 8 class version");
        assertEquals(55, java11Class.u4version,
                "visitAnyClass should not modify Java 11 class version");
    }

    /**
     * Tests visitAnyClass with all boolean parameter combinations.
     * The no-op behavior should be consistent across all configurations.
     */
    @Test
    public void testVisitAnyClass_withAllBooleanCombinations_alwaysNoOp() {
        // Arrange
        WrapperClassMerger[] mergers = new WrapperClassMerger[] {
            new WrapperClassMerger(false),
            new WrapperClassMerger(true)
        };

        ProgramClass testClass = new ProgramClass();
        testClass.u2accessFlags = 77;
        testClass.u4version = 51;
        int originalFlags = testClass.u2accessFlags;
        int originalVersion = testClass.u4version;

        // Act - call visitAnyClass with each merger configuration
        for (WrapperClassMerger merger : mergers) {
            merger.visitAnyClass(testClass);
        }

        // Assert - class should remain unchanged after all calls
        assertEquals(originalFlags, testClass.u2accessFlags,
                "visitAnyClass should be no-op for all boolean combinations");
        assertEquals(originalVersion, testClass.u4version,
                "visitAnyClass should be no-op for all boolean combinations");
    }

    /**
     * Tests visitAnyClass returns normally (void method completes).
     * Verifies that the method signature is correct and completes execution.
     */
    @Test
    public void testVisitAnyClass_returnsNormally() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should complete without exceptions or blocking
        assertDoesNotThrow(() -> {
            wrapperClassMerger.visitAnyClass(programClass);
            // If we reach this point, the method returned normally
        }, "visitAnyClass should return normally");
    }

    /**
     * Tests visitAnyClass with a LibraryClass and various configurations.
     * The no-op should work consistently with library classes too.
     */
    @Test
    public void testVisitAnyClass_withLibraryClassAndVariousConfigs_alwaysNoOp() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        libraryClass.u2accessFlags = 200;
        int originalFlags = libraryClass.u2accessFlags;

        WrapperClassMerger merger1 = new WrapperClassMerger(true);
        WrapperClassMerger merger2 = new WrapperClassMerger(false);

        // Act
        merger1.visitAnyClass(libraryClass);
        merger2.visitAnyClass(libraryClass);

        // Assert
        assertEquals(originalFlags, libraryClass.u2accessFlags,
                "visitAnyClass should not modify library class with any configuration");
    }

    /**
     * Tests that visitAnyClass implements the ClassVisitor interface correctly.
     * The method signature and behavior should match interface requirements.
     */
    @Test
    public void testVisitAnyClass_implementsClassVisitorInterface() {
        // Arrange
        ClassVisitor visitor = wrapperClassMerger; // Should compile due to interface
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 33;
        int originalFlags = programClass.u2accessFlags;

        // Act
        visitor.visitAnyClass(programClass);

        // Assert
        assertEquals(originalFlags, programClass.u2accessFlags,
                "visitAnyClass through ClassVisitor interface should be no-op");
    }

    /**
     * Tests visitAnyClass with rapid successive calls.
     * Verifies that the no-op is truly stateless and efficient.
     */
    @Test
    public void testVisitAnyClass_rapidSuccessiveCalls_remainsEfficient() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 99;
        int originalFlags = programClass.u2accessFlags;

        // Act - call many times rapidly
        for (int i = 0; i < 1000; i++) {
            wrapperClassMerger.visitAnyClass(programClass);
        }

        // Assert - should still be unchanged
        assertEquals(originalFlags, programClass.u2accessFlags,
                "visitAnyClass should remain no-op even after many rapid calls");
    }

    /**
     * Tests visitAnyClass with alternating class types.
     * The no-op should work consistently regardless of class type alternation.
     */
    @Test
    public void testVisitAnyClass_withAlternatingClassTypes_consistentNoOp() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 11;
        LibraryClass libraryClass = new LibraryClass();
        libraryClass.u2accessFlags = 22;

        int originalProgramFlags = programClass.u2accessFlags;
        int originalLibraryFlags = libraryClass.u2accessFlags;

        // Act - alternate between class types
        wrapperClassMerger.visitAnyClass(programClass);
        wrapperClassMerger.visitAnyClass(libraryClass);
        wrapperClassMerger.visitAnyClass(programClass);
        wrapperClassMerger.visitAnyClass(libraryClass);

        // Assert - both should remain unchanged
        assertEquals(originalProgramFlags, programClass.u2accessFlags,
                "ProgramClass should remain unchanged");
        assertEquals(originalLibraryFlags, libraryClass.u2accessFlags,
                "LibraryClass should remain unchanged");
    }

    /**
     * Tests that visitAnyClass is truly empty and doesn't execute hidden logic.
     * By verifying no state changes occur in various scenarios.
     */
    @Test
    public void testVisitAnyClass_trulyEmpty_noHiddenLogic() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        class1.u2accessFlags = 1;
        class1.u4version = 50;
        class1.u2thisClass = 5;
        class1.u2superClass = 10;

        int originalAccessFlags = class1.u2accessFlags;
        int originalVersion = class1.u4version;
        int originalThisClass = class1.u2thisClass;
        int originalSuperClass = class1.u2superClass;

        // Act
        wrapperClassMerger.visitAnyClass(class1);

        // Assert - verify all fields remain unchanged
        assertEquals(originalAccessFlags, class1.u2accessFlags,
                "Access flags should not be modified");
        assertEquals(originalVersion, class1.u4version,
                "Version should not be modified");
        assertEquals(originalThisClass, class1.u2thisClass,
                "This class index should not be modified");
        assertEquals(originalSuperClass, class1.u2superClass,
                "Super class index should not be modified");
    }

    /**
     * Tests visitAnyClass with WrapperClassMerger configured with both boolean values.
     * Verifies no-op behavior is consistent across all possible constructor parameters.
     */
    @Test
    public void testVisitAnyClass_withBothBooleanValues_consistentBehavior() {
        // Arrange
        WrapperClassMerger mergerTrue = new WrapperClassMerger(true);
        WrapperClassMerger mergerFalse = new WrapperClassMerger(false);

        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 55;
        int originalFlags = programClass.u2accessFlags;

        // Act
        mergerTrue.visitAnyClass(programClass);
        mergerFalse.visitAnyClass(programClass);

        // Assert
        assertEquals(originalFlags, programClass.u2accessFlags,
                "visitAnyClass should be no-op regardless of allowAccessModification flag");
    }

    /**
     * Tests visitAnyClass with merger created using two-parameter constructor.
     * Verifies no-op behavior is consistent across different constructor variants.
     */
    @Test
    public void testVisitAnyClass_withTwoParameterConstructor_alsoNoOp() {
        // Arrange
        ClassVisitor extraVisitor = mock(ClassVisitor.class);
        WrapperClassMerger merger = new WrapperClassMerger(true, extraVisitor);

        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 75;
        int originalFlags = programClass.u2accessFlags;

        // Act
        merger.visitAnyClass(programClass);

        // Assert
        assertEquals(originalFlags, programClass.u2accessFlags,
                "visitAnyClass should be no-op with two-parameter constructor");
        verifyNoInteractions(extraVisitor);
    }

    /**
     * Tests visitAnyClass does not trigger any visitor methods on extraClassVisitor.
     * Confirms that the empty implementation truly does nothing.
     */
    @Test
    public void testVisitAnyClass_doesNotTriggerExtraVisitor() {
        // Arrange
        ClassVisitor extraVisitor = mock(ClassVisitor.class);
        WrapperClassMerger merger = new WrapperClassMerger(false, extraVisitor);

        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        // Act
        merger.visitAnyClass(programClass);
        merger.visitAnyClass(libraryClass);

        // Assert - extraVisitor should never be called
        verifyNoInteractions(extraVisitor);
    }

    /**
     * Tests visitAnyClass with sequential different class instances.
     * Verifies that the no-op is stateless across different class instances.
     */
    @Test
    public void testVisitAnyClass_withSequentialDifferentInstances_stateless() {
        // Arrange
        ProgramClass[] classes = new ProgramClass[5];
        int[] originalFlags = new int[5];

        for (int i = 0; i < 5; i++) {
            classes[i] = new ProgramClass();
            classes[i].u2accessFlags = i * 10;
            originalFlags[i] = classes[i].u2accessFlags;
        }

        // Act
        for (ProgramClass clazz : classes) {
            wrapperClassMerger.visitAnyClass(clazz);
        }

        // Assert - all classes should remain unchanged
        for (int i = 0; i < 5; i++) {
            assertEquals(originalFlags[i], classes[i].u2accessFlags,
                    "Class " + i + " should remain unchanged");
        }
    }

    /**
     * Tests that visitAnyClass completes quickly even with many calls.
     * Verifies the no-op implementation is truly efficient.
     */
    @Test
    public void testVisitAnyClass_performance_completesQuickly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 42;
        long startTime = System.nanoTime();

        // Act - call many times
        for (int i = 0; i < 10000; i++) {
            wrapperClassMerger.visitAnyClass(programClass);
        }

        // Assert - should complete very quickly (less than 100ms for 10000 calls)
        long duration = System.nanoTime() - startTime;
        assertTrue(duration < 100_000_000L,
                "10000 calls to visitAnyClass should complete in under 100ms (took " + duration + " ns)");
    }
}
