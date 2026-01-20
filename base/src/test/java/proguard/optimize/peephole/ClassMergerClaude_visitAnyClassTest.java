package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ClassMerger#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in ClassMerger is a no-op implementation (empty method body).
 * It's required by the ClassVisitor interface but intentionally does nothing, as ClassMerger
 * only operates on ProgramClass instances via the visitProgramClass method.
 *
 * These tests verify that the method:
 * 1. Does not throw exceptions when called
 * 2. Does not modify the class or merger state
 * 3. Works correctly with different types of Clazz implementations
 */
public class ClassMergerClaude_visitAnyClassTest {

    private ProgramClass targetClass;
    private ClassMerger classMerger;

    @BeforeEach
    public void setUp() {
        // Create a target class for the ClassMerger
        targetClass = new ProgramClass();
        targetClass.u2accessFlags = 0;
        targetClass.u2thisClass = 0;
        targetClass.u2superClass = 0;
        targetClass.u4version = 50; // Java 6 version

        // Create ClassMerger with default settings
        classMerger = new ClassMerger(targetClass, false, false, false);
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
        assertDoesNotThrow(() -> classMerger.visitAnyClass(programClass),
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
        assertDoesNotThrow(() -> classMerger.visitAnyClass(libraryClass),
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
        classMerger.visitAnyClass(programClass);
        classMerger.visitAnyClass(programClass);
        classMerger.visitAnyClass(programClass);

        // Assert - should still be unchanged
        assertEquals(originalAccessFlags, programClass.u2accessFlags,
                "Multiple calls to visitAnyClass should not modify the class");
    }

    /**
     * Tests visitAnyClass with different ClassMerger configurations.
     * The no-op behavior should be independent of ClassMerger settings.
     */
    @Test
    public void testVisitAnyClass_withDifferentMergerSettings_alwaysNoOp() {
        // Arrange - ClassMerger with different settings
        ClassMerger merger1 = new ClassMerger(targetClass, true, false, false);
        ClassMerger merger2 = new ClassMerger(targetClass, false, true, false);
        ClassMerger merger3 = new ClassMerger(targetClass, false, false, true);
        ClassMerger merger4 = new ClassMerger(targetClass, true, true, true);

        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 100;
        int originalAccessFlags = programClass.u2accessFlags;

        // Act - call visitAnyClass with each merger
        merger1.visitAnyClass(programClass);
        merger2.visitAnyClass(programClass);
        merger3.visitAnyClass(programClass);
        merger4.visitAnyClass(programClass);

        // Assert - class should remain unchanged regardless of settings
        assertEquals(originalAccessFlags, programClass.u2accessFlags,
                "visitAnyClass should be no-op regardless of ClassMerger configuration");
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
        classMerger.visitAnyClass(publicClass);
        classMerger.visitAnyClass(finalClass);
        classMerger.visitAnyClass(abstractClass);

        // Assert - all flags should remain unchanged
        assertEquals(originalPublicFlags, publicClass.u2accessFlags,
                "visitAnyClass should not modify public class flags");
        assertEquals(originalFinalFlags, finalClass.u2accessFlags,
                "visitAnyClass should not modify final class flags");
        assertEquals(originalAbstractFlags, abstractClass.u2accessFlags,
                "visitAnyClass should not modify abstract class flags");
    }

    /**
     * Tests that visitAnyClass does not affect subsequent operations.
     * Calling visitAnyClass should not interfere with the ClassMerger's state.
     */
    @Test
    public void testVisitAnyClass_doesNotAffectMergerState() {
        // Arrange
        ProgramClass unrelatedClass = new ProgramClass();
        unrelatedClass.u2accessFlags = 0;

        // Act - call visitAnyClass first
        classMerger.visitAnyClass(unrelatedClass);

        // Verify the merger still works normally by checking target class is unchanged
        int targetAccessFlags = targetClass.u2accessFlags;

        // Call visitAnyClass on target
        classMerger.visitAnyClass(targetClass);

        // Assert - target class should still be unchanged
        assertEquals(targetAccessFlags, targetClass.u2accessFlags,
                "visitAnyClass should not affect the merger's target class");
    }

    /**
     * Tests visitAnyClass with the same class that is used as the target.
     * Even when called on the target class, it should do nothing.
     */
    @Test
    public void testVisitAnyClass_withTargetClass_doesNothing() {
        // Arrange
        int originalAccessFlags = targetClass.u2accessFlags;
        int originalVersion = targetClass.u4version;

        // Act - call visitAnyClass on the target class itself
        classMerger.visitAnyClass(targetClass);

        // Assert - target class should remain unchanged
        assertEquals(originalAccessFlags, targetClass.u2accessFlags,
                "visitAnyClass should not modify target class access flags");
        assertEquals(originalVersion, targetClass.u4version,
                "visitAnyClass should not modify target class version");
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
        classMerger.visitAnyClass(sourceClass);

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
        assertDoesNotThrow(() -> classMerger.visitAnyClass(minimalClass),
                "visitAnyClass should not throw even with minimal class");
    }

    /**
     * Tests that multiple ClassMerger instances with visitAnyClass
     * don't interfere with each other.
     */
    @Test
    public void testVisitAnyClass_multipleInstances_independent() {
        // Arrange
        ProgramClass target1 = new ProgramClass();
        target1.u4version = 50;
        ProgramClass target2 = new ProgramClass();
        target2.u4version = 51;

        ClassMerger merger1 = new ClassMerger(target1, false, false, false);
        ClassMerger merger2 = new ClassMerger(target2, false, false, false);

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
}
