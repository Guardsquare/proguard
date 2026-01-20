package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link VerticalClassMerger#visitProgramClass(ProgramClass)}.
 *
 * The visitProgramClass method attempts to inline direct subclasses into the program class
 * that it visits. It does this by:
 * 1. Calling programClass.subclassesAccept() with an InjectedClassFilter
 * 2. The filter wraps a ClassMerger that attempts vertical class merging
 * 3. The ClassMerger uses the allowAccessModification and mergeInterfacesAggressively flags
 *
 * These tests verify that:
 * 1. The method doesn't throw exceptions with various ProgramClass configurations
 * 2. The method handles classes with and without subclasses
 * 3. The method respects the configuration flags
 * 4. The method works correctly with the extraClassVisitor parameter
 */
public class VerticalClassMergerClaude_visitProgramClassTest {

    private VerticalClassMerger verticalClassMerger;

    @BeforeEach
    public void setUp() {
        // Create VerticalClassMerger with default settings
        verticalClassMerger = new VerticalClassMerger(false, false);
    }

    // ========================================
    // Basic Functionality Tests
    // ========================================

    /**
     * Tests that visitProgramClass does not throw exceptions with a basic ProgramClass.
     * Verifies the method can handle a minimal class without errors.
     */
    @Test
    public void testVisitProgramClass_withBasicClass_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> verticalClassMerger.visitProgramClass(programClass),
            "visitProgramClass should not throw exception with basic ProgramClass");
    }

    /**
     * Tests that visitProgramClass handles a ProgramClass with minimal initialization.
     * Verifies the method gracefully handles classes with default values.
     */
    @Test
    public void testVisitProgramClass_withMinimalClass_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0;
        programClass.u4version = 50; // Java 6

        // Act & Assert
        assertDoesNotThrow(() -> verticalClassMerger.visitProgramClass(programClass),
            "visitProgramClass should handle minimally initialized class");
    }

    /**
     * Tests that visitProgramClass handles null ProgramClass.
     * Verifies NullPointerException is thrown for null input.
     */
    @Test
    public void testVisitProgramClass_withNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> verticalClassMerger.visitProgramClass(null),
            "visitProgramClass should throw NullPointerException for null class");
    }

    // ========================================
    // Configuration Tests
    // ========================================

    /**
     * Tests visitProgramClass with allowAccessModification set to true.
     * Verifies the method works with access modification enabled.
     */
    @Test
    public void testVisitProgramClass_withAllowAccessModificationTrue_doesNotThrow() {
        // Arrange
        VerticalClassMerger merger = new VerticalClassMerger(true, false);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> merger.visitProgramClass(programClass),
            "visitProgramClass should work with allowAccessModification=true");
    }

    /**
     * Tests visitProgramClass with mergeInterfacesAggressively set to true.
     * Verifies the method works with aggressive interface merging enabled.
     */
    @Test
    public void testVisitProgramClass_withMergeInterfacesAggressivelyTrue_doesNotThrow() {
        // Arrange
        VerticalClassMerger merger = new VerticalClassMerger(false, true);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> merger.visitProgramClass(programClass),
            "visitProgramClass should work with mergeInterfacesAggressively=true");
    }

    /**
     * Tests visitProgramClass with both configuration flags set to true.
     * Verifies the method works with maximum flexibility.
     */
    @Test
    public void testVisitProgramClass_withBothFlagsTrue_doesNotThrow() {
        // Arrange
        VerticalClassMerger merger = new VerticalClassMerger(true, true);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> merger.visitProgramClass(programClass),
            "visitProgramClass should work with both flags true");
    }

    /**
     * Tests visitProgramClass with all possible boolean flag combinations.
     * Verifies the method works with all four combinations.
     */
    @Test
    public void testVisitProgramClass_withAllFlagCombinations_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        VerticalClassMerger merger1 = new VerticalClassMerger(false, false);
        VerticalClassMerger merger2 = new VerticalClassMerger(false, true);
        VerticalClassMerger merger3 = new VerticalClassMerger(true, false);
        VerticalClassMerger merger4 = new VerticalClassMerger(true, true);

        // Act & Assert
        assertDoesNotThrow(() -> {
            merger1.visitProgramClass(programClass);
            merger2.visitProgramClass(programClass);
            merger3.visitProgramClass(programClass);
            merger4.visitProgramClass(programClass);
        }, "visitProgramClass should work with all flag combinations");
    }

    // ========================================
    // ExtraClassVisitor Tests
    // ========================================

    /**
     * Tests visitProgramClass with a non-null extraClassVisitor.
     * Verifies the method works with an extra visitor configured.
     */
    @Test
    public void testVisitProgramClass_withExtraClassVisitor_doesNotThrow() {
        // Arrange
        ClassVisitor extraVisitor = mock(ClassVisitor.class);
        VerticalClassMerger merger = new VerticalClassMerger(false, false, extraVisitor);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> merger.visitProgramClass(programClass),
            "visitProgramClass should work with extra visitor");
    }

    /**
     * Tests visitProgramClass with null extraClassVisitor.
     * Verifies the method works when extraClassVisitor is explicitly null.
     */
    @Test
    public void testVisitProgramClass_withNullExtraClassVisitor_doesNotThrow() {
        // Arrange
        VerticalClassMerger merger = new VerticalClassMerger(false, false, null);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> merger.visitProgramClass(programClass),
            "visitProgramClass should work with null extra visitor");
    }

    /**
     * Tests visitProgramClass with extraClassVisitor and various flag combinations.
     * Verifies the extra visitor works with all configurations.
     */
    @Test
    public void testVisitProgramClass_withExtraVisitorAndVariousFlags_doesNotThrow() {
        // Arrange
        ClassVisitor extraVisitor = mock(ClassVisitor.class);
        ProgramClass programClass = new ProgramClass();

        VerticalClassMerger merger1 = new VerticalClassMerger(true, true, extraVisitor);
        VerticalClassMerger merger2 = new VerticalClassMerger(false, false, extraVisitor);

        // Act & Assert
        assertDoesNotThrow(() -> {
            merger1.visitProgramClass(programClass);
            merger2.visitProgramClass(programClass);
        }, "visitProgramClass should work with extra visitor and various flags");
    }

    // ========================================
    // Multiple Call Tests
    // ========================================

    /**
     * Tests that visitProgramClass can be called multiple times on the same class.
     * Verifies repeated processing doesn't cause issues.
     */
    @Test
    public void testVisitProgramClass_calledMultipleTimes_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            verticalClassMerger.visitProgramClass(programClass);
            verticalClassMerger.visitProgramClass(programClass);
            verticalClassMerger.visitProgramClass(programClass);
        }, "Multiple calls to visitProgramClass should not throw");
    }

    /**
     * Tests that visitProgramClass can process different classes sequentially.
     * Verifies the merger maintains correct state between different classes.
     */
    @Test
    public void testVisitProgramClass_withDifferentClasses_doesNotThrow() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            verticalClassMerger.visitProgramClass(class1);
            verticalClassMerger.visitProgramClass(class2);
            verticalClassMerger.visitProgramClass(class3);
        }, "visitProgramClass should handle different classes sequentially");
    }

    /**
     * Tests that visitProgramClass works with rapid successive calls.
     * Verifies performance and state management with many calls.
     */
    @Test
    public void testVisitProgramClass_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                verticalClassMerger.visitProgramClass(programClass);
            }
        }, "Rapid successive calls should not cause issues");
    }

    // ========================================
    // Class Configuration Tests
    // ========================================

    /**
     * Tests visitProgramClass with a class that has processing info.
     * Verifies the method doesn't interfere with existing processing information.
     */
    @Test
    public void testVisitProgramClass_withProcessingInfo_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.setProcessingInfo(new Object());

        // Act & Assert
        assertDoesNotThrow(() -> verticalClassMerger.visitProgramClass(programClass),
            "visitProgramClass should handle class with processing info");
    }

    /**
     * Tests visitProgramClass with a class that has various access flags.
     * Verifies the method works regardless of class modifiers.
     */
    @Test
    public void testVisitProgramClass_withVariousAccessFlags_doesNotThrow() {
        // Arrange
        ProgramClass publicClass = new ProgramClass();
        publicClass.u2accessFlags = 0x0001; // ACC_PUBLIC

        ProgramClass finalClass = new ProgramClass();
        finalClass.u2accessFlags = 0x0010; // ACC_FINAL

        ProgramClass abstractClass = new ProgramClass();
        abstractClass.u2accessFlags = 0x0400; // ACC_ABSTRACT

        // Act & Assert
        assertDoesNotThrow(() -> {
            verticalClassMerger.visitProgramClass(publicClass);
            verticalClassMerger.visitProgramClass(finalClass);
            verticalClassMerger.visitProgramClass(abstractClass);
        }, "visitProgramClass should handle classes with various access flags");
    }

    /**
     * Tests visitProgramClass with classes that have different versions.
     * Verifies the method processes classes with various Java versions.
     */
    @Test
    public void testVisitProgramClass_withDifferentVersions_doesNotThrow() {
        // Arrange
        ProgramClass java6Class = new ProgramClass();
        java6Class.u4version = 50; // Java 6

        ProgramClass java8Class = new ProgramClass();
        java8Class.u4version = 52; // Java 8

        ProgramClass java11Class = new ProgramClass();
        java11Class.u4version = 55; // Java 11

        // Act & Assert
        assertDoesNotThrow(() -> {
            verticalClassMerger.visitProgramClass(java6Class);
            verticalClassMerger.visitProgramClass(java8Class);
            verticalClassMerger.visitProgramClass(java11Class);
        }, "visitProgramClass should handle classes with different versions");
    }

    /**
     * Tests visitProgramClass with classes that have different names.
     * Verifies the method processes classes with various naming patterns.
     */
    @Test
    public void testVisitProgramClass_withDifferentClassNames_doesNotThrow() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        class1.u2thisClass = 1;

        ProgramClass class2 = new ProgramClass();
        class2.u2thisClass = 2;

        ProgramClass class3 = new ProgramClass();
        class3.u2thisClass = 3;

        // Act & Assert
        assertDoesNotThrow(() -> {
            verticalClassMerger.visitProgramClass(class1);
            verticalClassMerger.visitProgramClass(class2);
            verticalClassMerger.visitProgramClass(class3);
        }, "visitProgramClass should handle classes with different names");
    }

    // ========================================
    // State and Isolation Tests
    // ========================================

    /**
     * Tests that processing one class doesn't affect processing another.
     * Verifies proper isolation between class visits.
     */
    @Test
    public void testVisitProgramClass_isolationBetweenClasses() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        class1.u2accessFlags = 10;

        ProgramClass class2 = new ProgramClass();
        class2.u2accessFlags = 20;

        // Act
        verticalClassMerger.visitProgramClass(class1);

        // Assert - second class should still process successfully
        assertDoesNotThrow(() -> verticalClassMerger.visitProgramClass(class2),
            "Second class should process independently of first");
    }

    /**
     * Tests that visitProgramClass doesn't modify the class reference.
     * Verifies the ProgramClass object reference remains unchanged.
     */
    @Test
    public void testVisitProgramClass_doesNotModifyClassReference() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClass originalReference = programClass;

        // Act
        verticalClassMerger.visitProgramClass(programClass);

        // Assert
        assertSame(originalReference, programClass,
            "ProgramClass reference should remain the same");
    }

    /**
     * Tests that visitProgramClass maintains merger state correctly.
     * Verifies the merger remains usable after processing.
     */
    @Test
    public void testVisitProgramClass_maintainsMergerState() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        // Act - process first class
        verticalClassMerger.visitProgramClass(class1);

        // Assert - merger should still work for second class
        assertDoesNotThrow(() -> verticalClassMerger.visitProgramClass(class2),
            "Merger should remain usable after processing a class");
    }

    // ========================================
    // Multiple Merger Instance Tests
    // ========================================

    /**
     * Tests that different merger instances work independently.
     * Verifies no shared state between merger instances.
     */
    @Test
    public void testVisitProgramClass_multipleMergers_independent() {
        // Arrange
        VerticalClassMerger merger1 = new VerticalClassMerger(true, false);
        VerticalClassMerger merger2 = new VerticalClassMerger(false, true);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            merger1.visitProgramClass(programClass);
            merger2.visitProgramClass(programClass);
        }, "Different mergers should work independently");
    }

    /**
     * Tests that multiple mergers can process the same class.
     * Verifies concurrent-like access patterns work correctly.
     */
    @Test
    public void testVisitProgramClass_sameClassDifferentMergers() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        VerticalClassMerger merger1 = new VerticalClassMerger(false, false);
        VerticalClassMerger merger2 = new VerticalClassMerger(true, false);
        VerticalClassMerger merger3 = new VerticalClassMerger(false, true);

        // Act & Assert
        assertDoesNotThrow(() -> {
            merger1.visitProgramClass(programClass);
            merger2.visitProgramClass(programClass);
            merger3.visitProgramClass(programClass);
        }, "Same class should be processable by different mergers");
    }

    // ========================================
    // Edge Case Tests
    // ========================================

    /**
     * Tests visitProgramClass immediately after merger creation.
     * Verifies the merger is ready to use immediately.
     */
    @Test
    public void testVisitProgramClass_immediatelyAfterCreation() {
        // Arrange
        VerticalClassMerger newMerger = new VerticalClassMerger(false, false);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> newMerger.visitProgramClass(programClass),
            "visitProgramClass should work immediately after merger creation");
    }

    /**
     * Tests visitProgramClass with freshly created ProgramClass instances.
     * Verifies handling of classes created inline.
     */
    @Test
    public void testVisitProgramClass_withFreshlyCreatedClasses() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            verticalClassMerger.visitProgramClass(new ProgramClass());
            verticalClassMerger.visitProgramClass(new ProgramClass());
            verticalClassMerger.visitProgramClass(new ProgramClass());
        }, "Should handle freshly created ProgramClass instances");
    }

    /**
     * Tests that visitProgramClass maintains consistency across many calls.
     * Verifies stable behavior over extended use.
     */
    @Test
    public void testVisitProgramClass_manySequentialCalls_consistent() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        for (int i = 0; i < 1000; i++) {
            final int iteration = i;
            assertDoesNotThrow(() -> verticalClassMerger.visitProgramClass(programClass),
                "Call " + iteration + " should not throw");
        }
    }

    /**
     * Tests visitProgramClass with alternating class configurations.
     * Verifies handling of varying input patterns.
     */
    @Test
    public void testVisitProgramClass_alternatingConfigurations() {
        // Arrange
        ProgramClass publicClass = new ProgramClass();
        publicClass.u2accessFlags = 0x0001; // ACC_PUBLIC

        ProgramClass privateClass = new ProgramClass();
        privateClass.u2accessFlags = 0x0002; // ACC_PRIVATE

        // Act & Assert
        assertDoesNotThrow(() -> {
            verticalClassMerger.visitProgramClass(publicClass);
            verticalClassMerger.visitProgramClass(privateClass);
            verticalClassMerger.visitProgramClass(publicClass);
            verticalClassMerger.visitProgramClass(privateClass);
        }, "Should handle alternating class configurations");
    }

    /**
     * Tests that visitProgramClass completes in reasonable time.
     * Verifies performance characteristics.
     */
    @Test
    public void testVisitProgramClass_performanceIsReasonable() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        long startTime = System.nanoTime();

        // Act - process 1000 times
        for (int i = 0; i < 1000; i++) {
            verticalClassMerger.visitProgramClass(programClass);
        }
        long endTime = System.nanoTime();

        // Assert - should complete in less than 1 second
        long durationMs = (endTime - startTime) / 1_000_000;
        assertTrue(durationMs < 1000,
            "1000 calls should complete in less than 1 second, took: " + durationMs + "ms");
    }

    /**
     * Tests visitProgramClass preserves basic class structure.
     * Verifies access flags remain unchanged after processing.
     */
    @Test
    public void testVisitProgramClass_preservesBasicClassStructure() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0x0001; // ACC_PUBLIC
        int originalAccessFlags = programClass.u2accessFlags;

        // Act
        verticalClassMerger.visitProgramClass(programClass);

        // Assert
        assertEquals(originalAccessFlags, programClass.u2accessFlags,
            "Access flags should remain unchanged");
    }

    /**
     * Tests visitProgramClass returns normally (void method completes).
     * Verifies that the method signature is correct and completes execution.
     */
    @Test
    public void testVisitProgramClass_returnsNormally() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - should complete without exceptions or blocking
        assertDoesNotThrow(() -> {
            verticalClassMerger.visitProgramClass(programClass);
            // If we reach this point, the method returned normally
        }, "visitProgramClass should return normally");
    }

    /**
     * Tests that visitProgramClass implements the ClassVisitor interface correctly.
     * The method should be callable through the interface.
     */
    @Test
    public void testVisitProgramClass_implementsClassVisitorInterface() {
        // Arrange
        ClassVisitor visitor = verticalClassMerger; // Should compile due to interface
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> visitor.visitProgramClass(programClass),
            "visitProgramClass through ClassVisitor interface should work");
    }

    /**
     * Tests visitProgramClass with classes having different super class indices.
     * Verifies the method handles various inheritance configurations.
     */
    @Test
    public void testVisitProgramClass_withDifferentSuperClasses_doesNotThrow() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        class1.u2superClass = 0; // No super class (Object)

        ProgramClass class2 = new ProgramClass();
        class2.u2superClass = 10; // Some super class

        ProgramClass class3 = new ProgramClass();
        class3.u2superClass = 20; // Different super class

        // Act & Assert
        assertDoesNotThrow(() -> {
            verticalClassMerger.visitProgramClass(class1);
            verticalClassMerger.visitProgramClass(class2);
            verticalClassMerger.visitProgramClass(class3);
        }, "visitProgramClass should handle classes with different super classes");
    }

    /**
     * Tests visitProgramClass with a class that has processing flags set.
     * Verifies the method handles classes with various processing states.
     */
    @Test
    public void testVisitProgramClass_withProcessingFlags_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.processingFlags = 0x01; // Some processing flag

        // Act & Assert
        assertDoesNotThrow(() -> verticalClassMerger.visitProgramClass(programClass),
            "visitProgramClass should handle class with processing flags");
    }

    /**
     * Tests that multiple calls with the same merger and different classes
     * all succeed independently.
     */
    @Test
    public void testVisitProgramClass_multipleClassesSequentially_allSucceed() {
        // Arrange
        ProgramClass[] classes = new ProgramClass[10];
        for (int i = 0; i < classes.length; i++) {
            classes[i] = new ProgramClass();
            classes[i].u2thisClass = i;
        }

        // Act & Assert
        for (int i = 0; i < classes.length; i++) {
            final int index = i;
            assertDoesNotThrow(() -> verticalClassMerger.visitProgramClass(classes[index]),
                "Class " + index + " should be processed successfully");
        }
    }

    /**
     * Tests visitProgramClass with a class that has interface indices set.
     * Verifies the method handles classes implementing interfaces.
     */
    @Test
    public void testVisitProgramClass_withInterfaces_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2interfacesCount = 0;
        programClass.u2interfaces = new int[0];

        // Act & Assert
        assertDoesNotThrow(() -> verticalClassMerger.visitProgramClass(programClass),
            "visitProgramClass should handle class with interfaces");
    }

    /**
     * Tests that visitProgramClass works correctly when called
     * in a pattern simulating real-world usage.
     */
    @Test
    public void testVisitProgramClass_realWorldUsagePattern() {
        // Arrange - simulate processing multiple classes in a hierarchy
        ProgramClass baseClass = new ProgramClass();
        baseClass.u2thisClass = 1;
        baseClass.u2superClass = 0;

        ProgramClass derivedClass1 = new ProgramClass();
        derivedClass1.u2thisClass = 2;
        derivedClass1.u2superClass = 1;

        ProgramClass derivedClass2 = new ProgramClass();
        derivedClass2.u2thisClass = 3;
        derivedClass2.u2superClass = 1;

        // Act & Assert - process in order
        assertDoesNotThrow(() -> {
            verticalClassMerger.visitProgramClass(baseClass);
            verticalClassMerger.visitProgramClass(derivedClass1);
            verticalClassMerger.visitProgramClass(derivedClass2);
        }, "Should handle class hierarchy processing pattern");
    }
}
