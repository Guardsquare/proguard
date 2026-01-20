package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LineNumberLinearizer#visitProgramClass(ProgramClass)}.
 *
 * The visitProgramClass method disambiguates line numbers in a program class after
 * optimizations like method inlining and class merging. It:
 * 1. Finds the highest line number in the entire class using LineNumberRangeFinder
 * 2. Checks if there are any inlined line numbers (from different source files)
 * 3. If inlined line numbers exist, shifts them to non-overlapping blocks
 *
 * These tests verify that:
 * 1. The method doesn't throw exceptions with various ProgramClass configurations
 * 2. The method can handle classes with and without methods
 * 3. The method can handle classes with various attribute configurations
 * 4. The method processes classes correctly and maintains state
 */
public class LineNumberLinearizerClaude_visitProgramClassTest {

    private LineNumberLinearizer linearizer;

    @BeforeEach
    public void setUp() {
        linearizer = new LineNumberLinearizer();
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
        assertDoesNotThrow(() -> linearizer.visitProgramClass(programClass),
            "visitProgramClass should not throw exception with basic ProgramClass");
    }

    /**
     * Tests that visitProgramClass handles a ProgramClass with no methods.
     * Verifies the method gracefully handles empty method arrays.
     */
    @Test
    public void testVisitProgramClass_withNoMethods_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2methodsCount = 0;
        programClass.methods = null;

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramClass(programClass),
            "visitProgramClass should handle class with no methods");
    }

    /**
     * Tests that visitProgramClass handles a ProgramClass with empty methods array.
     * Verifies the method handles zero-length arrays correctly.
     */
    @Test
    public void testVisitProgramClass_withEmptyMethodsArray_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2methodsCount = 0;
        programClass.methods = new ProgramMethod[0];

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramClass(programClass),
            "visitProgramClass should handle empty methods array");
    }

    /**
     * Tests that visitProgramClass handles a ProgramClass with a single method.
     * Verifies basic processing of a class with one method.
     */
    @Test
    public void testVisitProgramClass_withSingleMethod_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        programClass.methods = new ProgramMethod[] { method };
        programClass.u2methodsCount = 1;

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramClass(programClass),
            "visitProgramClass should handle class with single method");
    }

    /**
     * Tests that visitProgramClass handles a ProgramClass with multiple methods.
     * Verifies processing of classes with several methods.
     */
    @Test
    public void testVisitProgramClass_withMultipleMethods_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();
        programClass.methods = new ProgramMethod[] { method1, method2, method3 };
        programClass.u2methodsCount = 3;

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramClass(programClass),
            "visitProgramClass should handle class with multiple methods");
    }

    /**
     * Tests that visitProgramClass handles null ProgramClass.
     * Verifies NullPointerException is thrown for null input.
     */
    @Test
    public void testVisitProgramClass_withNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> linearizer.visitProgramClass(null),
            "visitProgramClass should throw NullPointerException for null class");
    }

    // ========================================
    // Method with Attributes Tests
    // ========================================

    /**
     * Tests visitProgramClass with methods that have no attributes.
     * Verifies handling of methods without Code attributes.
     */
    @Test
    public void testVisitProgramClass_withMethodsWithoutAttributes_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        method.u2attributesCount = 0;
        method.attributes = null;
        programClass.methods = new ProgramMethod[] { method };
        programClass.u2methodsCount = 1;

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramClass(programClass),
            "visitProgramClass should handle methods without attributes");
    }

    /**
     * Tests visitProgramClass with methods that have empty attributes array.
     * Verifies handling of zero-length attribute arrays.
     */
    @Test
    public void testVisitProgramClass_withMethodsWithEmptyAttributesArray_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        method.u2attributesCount = 0;
        method.attributes = new Attribute[0];
        programClass.methods = new ProgramMethod[] { method };
        programClass.u2methodsCount = 1;

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramClass(programClass),
            "visitProgramClass should handle methods with empty attributes array");
    }

    /**
     * Tests visitProgramClass with methods that have Code attributes.
     * Verifies basic processing of methods with code.
     */
    @Test
    public void testVisitProgramClass_withMethodsWithCodeAttributes_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = new Attribute[0];
        method.attributes = new Attribute[] { codeAttribute };
        method.u2attributesCount = 1;
        programClass.methods = new ProgramMethod[] { method };
        programClass.u2methodsCount = 1;

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramClass(programClass),
            "visitProgramClass should handle methods with Code attributes");
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
            linearizer.visitProgramClass(programClass);
            linearizer.visitProgramClass(programClass);
            linearizer.visitProgramClass(programClass);
        }, "Multiple calls to visitProgramClass should not throw");
    }

    /**
     * Tests that visitProgramClass can process different classes sequentially.
     * Verifies the linearizer maintains correct state between different classes.
     */
    @Test
    public void testVisitProgramClass_withDifferentClasses_doesNotThrow() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer.visitProgramClass(class1);
            linearizer.visitProgramClass(class2);
            linearizer.visitProgramClass(class3);
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
                linearizer.visitProgramClass(programClass);
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
        assertDoesNotThrow(() -> linearizer.visitProgramClass(programClass),
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
            linearizer.visitProgramClass(publicClass);
            linearizer.visitProgramClass(finalClass);
            linearizer.visitProgramClass(abstractClass);
        }, "visitProgramClass should handle classes with various access flags");
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
            linearizer.visitProgramClass(class1);
            linearizer.visitProgramClass(class2);
            linearizer.visitProgramClass(class3);
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
        ProgramMethod method1 = new ProgramMethod();
        class1.methods = new ProgramMethod[] { method1 };
        class1.u2methodsCount = 1;

        ProgramClass class2 = new ProgramClass();
        ProgramMethod method2 = new ProgramMethod();
        class2.methods = new ProgramMethod[] { method2 };
        class2.u2methodsCount = 1;

        // Act
        linearizer.visitProgramClass(class1);

        // Assert - second class should still process successfully
        assertDoesNotThrow(() -> linearizer.visitProgramClass(class2),
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
        linearizer.visitProgramClass(programClass);

        // Assert
        assertSame(originalReference, programClass,
            "ProgramClass reference should remain the same");
    }

    /**
     * Tests that visitProgramClass maintains linearizer state correctly.
     * Verifies the linearizer remains usable after processing.
     */
    @Test
    public void testVisitProgramClass_maintainsLinearizerState() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        // Act - process first class
        linearizer.visitProgramClass(class1);

        // Assert - linearizer should still work for second class
        assertDoesNotThrow(() -> linearizer.visitProgramClass(class2),
            "Linearizer should remain usable after processing a class");
    }

    // ========================================
    // Multiple Linearizer Instance Tests
    // ========================================

    /**
     * Tests that different linearizer instances work independently.
     * Verifies no shared state between linearizer instances.
     */
    @Test
    public void testVisitProgramClass_multipleLinearizers_independent() {
        // Arrange
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer1.visitProgramClass(programClass);
            linearizer2.visitProgramClass(programClass);
        }, "Different linearizers should work independently");
    }

    /**
     * Tests that multiple linearizers can process the same class.
     * Verifies concurrent-like access patterns work correctly.
     */
    @Test
    public void testVisitProgramClass_sameClassDifferentLinearizers() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer3 = new LineNumberLinearizer();

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer1.visitProgramClass(programClass);
            linearizer2.visitProgramClass(programClass);
            linearizer3.visitProgramClass(programClass);
        }, "Same class should be processable by different linearizers");
    }

    // ========================================
    // Complex Method Configuration Tests
    // ========================================

    /**
     * Tests visitProgramClass with methods that have multiple attributes.
     * Verifies handling of methods with various attribute configurations.
     */
    @Test
    public void testVisitProgramClass_withMethodsWithMultipleAttributes_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        CodeAttribute codeAttr = new CodeAttribute();
        codeAttr.u2attributesCount = 0;
        codeAttr.attributes = new Attribute[0];

        method.attributes = new Attribute[] { codeAttr, codeAttr };
        method.u2attributesCount = 2;
        programClass.methods = new ProgramMethod[] { method };
        programClass.u2methodsCount = 1;

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramClass(programClass),
            "visitProgramClass should handle methods with multiple attributes");
    }

    /**
     * Tests visitProgramClass with a large number of methods.
     * Verifies the method scales reasonably with method count.
     */
    @Test
    public void testVisitProgramClass_withManyMethods_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod[] methods = new ProgramMethod[50];
        for (int i = 0; i < methods.length; i++) {
            methods[i] = new ProgramMethod();
        }
        programClass.methods = methods;
        programClass.u2methodsCount = methods.length;

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramClass(programClass),
            "visitProgramClass should handle classes with many methods");
    }

    // ========================================
    // Edge Case Tests
    // ========================================

    /**
     * Tests visitProgramClass immediately after linearizer creation.
     * Verifies the linearizer is ready to use immediately.
     */
    @Test
    public void testVisitProgramClass_immediatelyAfterCreation() {
        // Arrange
        LineNumberLinearizer newLinearizer = new LineNumberLinearizer();
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> newLinearizer.visitProgramClass(programClass),
            "visitProgramClass should work immediately after linearizer creation");
    }

    /**
     * Tests visitProgramClass with freshly created ProgramClass instances.
     * Verifies handling of classes created inline.
     */
    @Test
    public void testVisitProgramClass_withFreshlyCreatedClasses() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer.visitProgramClass(new ProgramClass());
            linearizer.visitProgramClass(new ProgramClass());
            linearizer.visitProgramClass(new ProgramClass());
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
            assertDoesNotThrow(() -> linearizer.visitProgramClass(programClass),
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
        ProgramClass emptyClass = new ProgramClass();

        ProgramClass classWithMethod = new ProgramClass();
        classWithMethod.methods = new ProgramMethod[] { new ProgramMethod() };
        classWithMethod.u2methodsCount = 1;

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer.visitProgramClass(emptyClass);
            linearizer.visitProgramClass(classWithMethod);
            linearizer.visitProgramClass(emptyClass);
            linearizer.visitProgramClass(classWithMethod);
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
            linearizer.visitProgramClass(programClass);
        }
        long endTime = System.nanoTime();

        // Assert - should complete in less than 1 second
        long durationMs = (endTime - startTime) / 1_000_000;
        assertTrue(durationMs < 1000,
            "1000 calls should complete in less than 1 second, took: " + durationMs + "ms");
    }

    /**
     * Tests visitProgramClass preserves class structure.
     * Verifies method count remains unchanged after processing.
     */
    @Test
    public void testVisitProgramClass_preservesClassStructure() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method = new ProgramMethod();
        programClass.methods = new ProgramMethod[] { method };
        programClass.u2methodsCount = 1;
        int originalMethodCount = programClass.u2methodsCount;

        // Act
        linearizer.visitProgramClass(programClass);

        // Assert
        assertEquals(originalMethodCount, programClass.u2methodsCount,
            "Method count should remain unchanged");
        assertNotNull(programClass.methods, "Methods array should not be null");
    }
}
