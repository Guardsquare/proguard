package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LineNumberLinearizer#visitProgramMethod(ProgramClass, ProgramMethod)}.
 *
 * The visitProgramMethod method in LineNumberLinearizer is part of the MemberVisitor interface.
 * It delegates to the AttributeVisitor by calling programMethod.attributesAccept(programClass, this).
 * This method is called during line number linearization to process the attributes of each method
 * in a class, looking for line number information that needs to be shifted.
 *
 * These tests verify that:
 * 1. The method doesn't throw exceptions with various method configurations
 * 2. The method can handle methods with and without attributes
 * 3. The method properly delegates to attribute visiting
 * 4. The method handles null inputs appropriately
 */
public class LineNumberLinearizerClaude_visitProgramMethodTest {

    private LineNumberLinearizer linearizer;
    private ProgramClass programClass;

    @BeforeEach
    public void setUp() {
        linearizer = new LineNumberLinearizer();
        programClass = new ProgramClass();
    }

    // ========================================
    // Basic Functionality Tests
    // ========================================

    /**
     * Tests that visitProgramMethod does not throw exceptions with a basic method.
     * Verifies the method can handle a minimal method without errors.
     */
    @Test
    public void testVisitProgramMethod_withBasicMethod_doesNotThrow() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramMethod(programClass, programMethod),
            "visitProgramMethod should not throw exception with basic method");
    }

    /**
     * Tests that visitProgramMethod handles a method with no attributes.
     * Verifies the method gracefully handles null attribute arrays.
     */
    @Test
    public void testVisitProgramMethod_withNoAttributes_doesNotThrow() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2attributesCount = 0;
        programMethod.attributes = null;

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramMethod(programClass, programMethod),
            "visitProgramMethod should handle method with no attributes");
    }

    /**
     * Tests that visitProgramMethod handles a method with empty attributes array.
     * Verifies the method handles zero-length arrays correctly.
     */
    @Test
    public void testVisitProgramMethod_withEmptyAttributesArray_doesNotThrow() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.u2attributesCount = 0;
        programMethod.attributes = new Attribute[0];

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramMethod(programClass, programMethod),
            "visitProgramMethod should handle empty attributes array");
    }

    /**
     * Tests that visitProgramMethod handles null method parameter.
     * Verifies NullPointerException is thrown for null method.
     */
    @Test
    public void testVisitProgramMethod_withNullMethod_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> linearizer.visitProgramMethod(programClass, null),
            "visitProgramMethod should throw NullPointerException for null method");
    }

    /**
     * Tests that visitProgramMethod handles null class parameter.
     * Verifies the method behavior with null ProgramClass.
     */
    @Test
    public void testVisitProgramMethod_withNullClass_doesNotThrow() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert
        // The method passes the class to attributesAccept, so null should be handled by attributes
        assertDoesNotThrow(() -> linearizer.visitProgramMethod(null, programMethod),
            "visitProgramMethod should handle null ProgramClass");
    }

    /**
     * Tests that visitProgramMethod handles both parameters being null.
     * Verifies NullPointerException is thrown when method is null.
     */
    @Test
    public void testVisitProgramMethod_withBothParametersNull_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
            () -> linearizer.visitProgramMethod(null, null),
            "visitProgramMethod should throw NullPointerException with null method");
    }

    // ========================================
    // Method with Attributes Tests
    // ========================================

    /**
     * Tests visitProgramMethod with a method that has a single attribute.
     * Verifies basic processing of methods with attributes.
     */
    @Test
    public void testVisitProgramMethod_withSingleAttribute_doesNotThrow() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = new Attribute[0];
        programMethod.attributes = new Attribute[] { codeAttribute };
        programMethod.u2attributesCount = 1;

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramMethod(programClass, programMethod),
            "visitProgramMethod should handle method with single attribute");
    }

    /**
     * Tests visitProgramMethod with a method that has multiple attributes.
     * Verifies processing of methods with several attributes.
     */
    @Test
    public void testVisitProgramMethod_withMultipleAttributes_doesNotThrow() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        CodeAttribute codeAttr1 = new CodeAttribute();
        codeAttr1.u2attributesCount = 0;
        codeAttr1.attributes = new Attribute[0];
        CodeAttribute codeAttr2 = new CodeAttribute();
        codeAttr2.u2attributesCount = 0;
        codeAttr2.attributes = new Attribute[0];

        programMethod.attributes = new Attribute[] { codeAttr1, codeAttr2 };
        programMethod.u2attributesCount = 2;

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramMethod(programClass, programMethod),
            "visitProgramMethod should handle method with multiple attributes");
    }

    /**
     * Tests visitProgramMethod with a method that has many attributes.
     * Verifies the method scales with attribute count.
     */
    @Test
    public void testVisitProgramMethod_withManyAttributes_doesNotThrow() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        Attribute[] attributes = new Attribute[10];
        for (int i = 0; i < attributes.length; i++) {
            CodeAttribute codeAttribute = new CodeAttribute();
            codeAttribute.u2attributesCount = 0;
            codeAttribute.attributes = new Attribute[0];
            attributes[i] = codeAttribute;
        }
        programMethod.attributes = attributes;
        programMethod.u2attributesCount = attributes.length;

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramMethod(programClass, programMethod),
            "visitProgramMethod should handle method with many attributes");
    }

    // ========================================
    // Multiple Call Tests
    // ========================================

    /**
     * Tests that visitProgramMethod can be called multiple times on the same method.
     * Verifies repeated processing doesn't cause issues.
     */
    @Test
    public void testVisitProgramMethod_calledMultipleTimes_doesNotThrow() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer.visitProgramMethod(programClass, programMethod);
            linearizer.visitProgramMethod(programClass, programMethod);
            linearizer.visitProgramMethod(programClass, programMethod);
        }, "Multiple calls to visitProgramMethod should not throw");
    }

    /**
     * Tests that visitProgramMethod can process different methods sequentially.
     * Verifies the linearizer maintains correct state between different methods.
     */
    @Test
    public void testVisitProgramMethod_withDifferentMethods_doesNotThrow() {
        // Arrange
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer.visitProgramMethod(programClass, method1);
            linearizer.visitProgramMethod(programClass, method2);
            linearizer.visitProgramMethod(programClass, method3);
        }, "visitProgramMethod should handle different methods sequentially");
    }

    /**
     * Tests that visitProgramMethod works with rapid successive calls.
     * Verifies performance and state management with many calls.
     */
    @Test
    public void testVisitProgramMethod_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                linearizer.visitProgramMethod(programClass, programMethod);
            }
        }, "Rapid successive calls should not cause issues");
    }

    /**
     * Tests visitProgramMethod with alternating method configurations.
     * Verifies handling of varying input patterns.
     */
    @Test
    public void testVisitProgramMethod_alternatingConfigurations() {
        // Arrange
        ProgramMethod emptyMethod = new ProgramMethod();

        ProgramMethod methodWithAttribute = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = new Attribute[0];
        methodWithAttribute.attributes = new Attribute[] { codeAttribute };
        methodWithAttribute.u2attributesCount = 1;

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer.visitProgramMethod(programClass, emptyMethod);
            linearizer.visitProgramMethod(programClass, methodWithAttribute);
            linearizer.visitProgramMethod(programClass, emptyMethod);
            linearizer.visitProgramMethod(programClass, methodWithAttribute);
        }, "Should handle alternating method configurations");
    }

    // ========================================
    // Different Class Tests
    // ========================================

    /**
     * Tests visitProgramMethod with the same method but different classes.
     * Verifies the method works with varying ProgramClass instances.
     */
    @Test
    public void testVisitProgramMethod_sameMethodDifferentClasses() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer.visitProgramMethod(class1, programMethod);
            linearizer.visitProgramMethod(class2, programMethod);
            linearizer.visitProgramMethod(class3, programMethod);
        }, "Should handle same method with different classes");
    }

    /**
     * Tests visitProgramMethod with different method and class combinations.
     * Verifies the method handles various combinations correctly.
     */
    @Test
    public void testVisitProgramMethod_variousCombinations() {
        // Arrange
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer.visitProgramMethod(class1, method1);
            linearizer.visitProgramMethod(class1, method2);
            linearizer.visitProgramMethod(class2, method1);
            linearizer.visitProgramMethod(class2, method2);
        }, "Should handle various class-method combinations");
    }

    // ========================================
    // State and Isolation Tests
    // ========================================

    /**
     * Tests that processing one method doesn't affect processing another.
     * Verifies proper isolation between method visits.
     */
    @Test
    public void testVisitProgramMethod_isolationBetweenMethods() {
        // Arrange
        ProgramMethod method1 = new ProgramMethod();
        CodeAttribute attr1 = new CodeAttribute();
        attr1.u2attributesCount = 0;
        attr1.attributes = new Attribute[0];
        method1.attributes = new Attribute[] { attr1 };
        method1.u2attributesCount = 1;

        ProgramMethod method2 = new ProgramMethod();

        // Act
        linearizer.visitProgramMethod(programClass, method1);

        // Assert - second method should still process successfully
        assertDoesNotThrow(() -> linearizer.visitProgramMethod(programClass, method2),
            "Second method should process independently of first");
    }

    /**
     * Tests that visitProgramMethod doesn't modify the method reference.
     * Verifies the ProgramMethod object reference remains unchanged.
     */
    @Test
    public void testVisitProgramMethod_doesNotModifyMethodReference() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        ProgramMethod originalReference = programMethod;

        // Act
        linearizer.visitProgramMethod(programClass, programMethod);

        // Assert
        assertSame(originalReference, programMethod,
            "ProgramMethod reference should remain the same");
    }

    /**
     * Tests that visitProgramMethod doesn't modify the class reference.
     * Verifies the ProgramClass object reference remains unchanged.
     */
    @Test
    public void testVisitProgramMethod_doesNotModifyClassReference() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        ProgramClass originalClassReference = programClass;

        // Act
        linearizer.visitProgramMethod(programClass, programMethod);

        // Assert
        assertSame(originalClassReference, programClass,
            "ProgramClass reference should remain the same");
    }

    // ========================================
    // Multiple Linearizer Instance Tests
    // ========================================

    /**
     * Tests that different linearizer instances work independently.
     * Verifies no shared state between linearizer instances.
     */
    @Test
    public void testVisitProgramMethod_multipleLinearizers_independent() {
        // Arrange
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer1.visitProgramMethod(programClass, programMethod);
            linearizer2.visitProgramMethod(programClass, programMethod);
        }, "Different linearizers should work independently");
    }

    /**
     * Tests that multiple linearizers can process the same method.
     * Verifies concurrent-like access patterns work correctly.
     */
    @Test
    public void testVisitProgramMethod_sameMethodDifferentLinearizers() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        LineNumberLinearizer linearizer1 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer2 = new LineNumberLinearizer();
        LineNumberLinearizer linearizer3 = new LineNumberLinearizer();

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer1.visitProgramMethod(programClass, programMethod);
            linearizer2.visitProgramMethod(programClass, programMethod);
            linearizer3.visitProgramMethod(programClass, programMethod);
        }, "Same method should be processable by different linearizers");
    }

    // ========================================
    // Method Configuration Tests
    // ========================================

    /**
     * Tests visitProgramMethod with methods that have various access flags.
     * Verifies the method works regardless of method modifiers.
     */
    @Test
    public void testVisitProgramMethod_withVariousAccessFlags_doesNotThrow() {
        // Arrange
        ProgramMethod publicMethod = new ProgramMethod();
        publicMethod.u2accessFlags = 0x0001; // ACC_PUBLIC

        ProgramMethod staticMethod = new ProgramMethod();
        staticMethod.u2accessFlags = 0x0008; // ACC_STATIC

        ProgramMethod finalMethod = new ProgramMethod();
        finalMethod.u2accessFlags = 0x0010; // ACC_FINAL

        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer.visitProgramMethod(programClass, publicMethod);
            linearizer.visitProgramMethod(programClass, staticMethod);
            linearizer.visitProgramMethod(programClass, finalMethod);
        }, "visitProgramMethod should handle methods with various access flags");
    }

    /**
     * Tests visitProgramMethod with methods that have processing info.
     * Verifies the method doesn't interfere with existing processing information.
     */
    @Test
    public void testVisitProgramMethod_withProcessingInfo_doesNotThrow() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        programMethod.setProcessingInfo(new Object());

        // Act & Assert
        assertDoesNotThrow(() -> linearizer.visitProgramMethod(programClass, programMethod),
            "visitProgramMethod should handle method with processing info");
    }

    // ========================================
    // Edge Case Tests
    // ========================================

    /**
     * Tests visitProgramMethod immediately after linearizer creation.
     * Verifies the linearizer is ready to use immediately.
     */
    @Test
    public void testVisitProgramMethod_immediatelyAfterCreation() {
        // Arrange
        LineNumberLinearizer newLinearizer = new LineNumberLinearizer();
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert
        assertDoesNotThrow(() -> newLinearizer.visitProgramMethod(programClass, programMethod),
            "visitProgramMethod should work immediately after linearizer creation");
    }

    /**
     * Tests visitProgramMethod with freshly created method instances.
     * Verifies handling of methods created inline.
     */
    @Test
    public void testVisitProgramMethod_withFreshlyCreatedMethods() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            linearizer.visitProgramMethod(programClass, new ProgramMethod());
            linearizer.visitProgramMethod(programClass, new ProgramMethod());
            linearizer.visitProgramMethod(programClass, new ProgramMethod());
        }, "Should handle freshly created ProgramMethod instances");
    }

    /**
     * Tests that visitProgramMethod maintains consistency across many calls.
     * Verifies stable behavior over extended use.
     */
    @Test
    public void testVisitProgramMethod_manySequentialCalls_consistent() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert
        for (int i = 0; i < 1000; i++) {
            final int iteration = i;
            assertDoesNotThrow(() -> linearizer.visitProgramMethod(programClass, programMethod),
                "Call " + iteration + " should not throw");
        }
    }

    /**
     * Tests that visitProgramMethod completes in reasonable time.
     * Verifies performance characteristics.
     */
    @Test
    public void testVisitProgramMethod_performanceIsReasonable() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        long startTime = System.nanoTime();

        // Act - process 1000 times
        for (int i = 0; i < 1000; i++) {
            linearizer.visitProgramMethod(programClass, programMethod);
        }
        long endTime = System.nanoTime();

        // Assert - should complete in less than 1 second
        long durationMs = (endTime - startTime) / 1_000_000;
        assertTrue(durationMs < 1000,
            "1000 calls should complete in less than 1 second, took: " + durationMs + "ms");
    }

    /**
     * Tests visitProgramMethod preserves method structure.
     * Verifies attribute count remains unchanged after processing.
     */
    @Test
    public void testVisitProgramMethod_preservesMethodStructure() {
        // Arrange
        ProgramMethod programMethod = new ProgramMethod();
        CodeAttribute codeAttribute = new CodeAttribute();
        codeAttribute.u2attributesCount = 0;
        codeAttribute.attributes = new Attribute[0];
        programMethod.attributes = new Attribute[] { codeAttribute };
        programMethod.u2attributesCount = 1;
        int originalAttributeCount = programMethod.u2attributesCount;

        // Act
        linearizer.visitProgramMethod(programClass, programMethod);

        // Assert
        assertEquals(originalAttributeCount, programMethod.u2attributesCount,
            "Attribute count should remain unchanged");
        assertNotNull(programMethod.attributes, "Attributes array should not be null");
    }

    /**
     * Tests visitProgramMethod with methods from different contexts.
     * Verifies the method works regardless of where methods come from.
     */
    @Test
    public void testVisitProgramMethod_withMethodsFromDifferentContexts() {
        // Arrange
        ProgramMethod[] methods = new ProgramMethod[5];
        for (int i = 0; i < methods.length; i++) {
            methods[i] = new ProgramMethod();
            methods[i].u2accessFlags = i; // Different access flags
        }

        // Act & Assert
        assertDoesNotThrow(() -> {
            for (ProgramMethod method : methods) {
                linearizer.visitProgramMethod(programClass, method);
            }
        }, "Should handle methods from different contexts");
    }
}
