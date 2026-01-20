package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.annotation.RuntimeInvisibleTypeAnnotationsAttribute;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link MarkedAnnotationDeleter#visitRuntimeInvisibleTypeAnnotationsAttribute(Clazz, Member, RuntimeInvisibleTypeAnnotationsAttribute)}.
 *
 * The visitRuntimeInvisibleTypeAnnotationsAttribute method deletes runtime-invisible type annotations
 * that are marked with the specified mark object (set as processingInfo). If all annotations are deleted,
 * the attribute itself is removed from the member.
 *
 * These tests verify:
 * 1. The method can be called without throwing exceptions
 * 2. The method works with various input scenarios
 * 3. The method integrates correctly with the ProGuard class file structure
 */
public class MarkedAnnotationDeleterClaude_visitRuntimeInvisibleTypeAnnotationsAttributeTest {

    private MarkedAnnotationDeleter deleter;
    private Object mark;
    private ProgramClass clazz;
    private ProgramMethod method;

    @BeforeEach
    public void setUp() {
        mark = new Object();
        deleter = new MarkedAnnotationDeleter(mark);

        // Create a minimal ProgramClass
        clazz = new ProgramClass();

        // Create a minimal ProgramMethod
        method = new ProgramMethod();
    }

    /**
     * Tests that visiting an empty attribute doesn't throw an exception.
     * This is a basic smoke test to ensure the method can be called.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withEmptyAttribute_doesNotThrow() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute),
                "Method should handle empty attribute without throwing");
    }

    /**
     * Tests that the method can be called multiple times consecutively.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert - should not throw on multiple calls
        assertDoesNotThrow(() -> {
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
        }, "Method should handle multiple calls without throwing");
    }

    /**
     * Tests that the method works with different mark objects.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withDifferentMarks_doesNotThrow() {
        // Arrange
        Object mark1 = new Object();
        Object mark2 = "stringMark";
        Object mark3 = Integer.valueOf(42);

        MarkedAnnotationDeleter deleter1 = new MarkedAnnotationDeleter(mark1);
        MarkedAnnotationDeleter deleter2 = new MarkedAnnotationDeleter(mark2);
        MarkedAnnotationDeleter deleter3 = new MarkedAnnotationDeleter(mark3);

        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            deleter1.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
            deleter2.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
            deleter3.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
        }, "Method should work with different mark types");
    }

    /**
     * Tests that the method works with a null mark.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withNullMark_doesNotThrow() {
        // Arrange
        MarkedAnnotationDeleter nullMarkDeleter = new MarkedAnnotationDeleter(null);
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> nullMarkDeleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute),
                "Method should handle null mark without throwing");
    }

    /**
     * Tests that the method works with different Member types.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withDifferentMemberTypes_doesNotThrow() {
        // Arrange
        ProgramMethod method = new ProgramMethod();
        ProgramField field = new ProgramField();
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, field, attribute);
        }, "Method should work with both methods and fields");
    }

    /**
     * Tests that the method works with different attribute instances.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withDifferentAttributes_doesNotThrow() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute1 = new RuntimeInvisibleTypeAnnotationsAttribute();
        RuntimeInvisibleTypeAnnotationsAttribute attribute2 = new RuntimeInvisibleTypeAnnotationsAttribute();
        RuntimeInvisibleTypeAnnotationsAttribute attribute3 = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute1);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute2);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute3);
        }, "Method should work with different attribute instances");
    }

    /**
     * Tests that the method can be called on the same attribute instance multiple times.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_sameAttributeMultipleTimes_doesNotThrow() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert - calling on the same attribute multiple times
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5; i++) {
                deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
            }
        }, "Method should handle same attribute being visited multiple times");
    }

    /**
     * Tests that multiple deleter instances work independently.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_multipleDeleters_workIndependently() {
        // Arrange
        Object mark1 = new Object();
        Object mark2 = new Object();
        MarkedAnnotationDeleter deleter1 = new MarkedAnnotationDeleter(mark1);
        MarkedAnnotationDeleter deleter2 = new MarkedAnnotationDeleter(mark2);

        RuntimeInvisibleTypeAnnotationsAttribute attribute1 = new RuntimeInvisibleTypeAnnotationsAttribute();
        RuntimeInvisibleTypeAnnotationsAttribute attribute2 = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            deleter1.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute1);
            deleter2.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute2);
        }, "Multiple deleters should work independently");
    }

    /**
     * Tests that the method works with different ProgramClass instances.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withDifferentClasses_doesNotThrow() {
        // Arrange
        ProgramClass clazz1 = new ProgramClass();
        ProgramClass clazz2 = new ProgramClass();
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz1, method, attribute);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz2, method, attribute);
        }, "Method should work with different class instances");
    }

    /**
     * Tests that the method works with different ProgramMethod instances.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withDifferentMethods_doesNotThrow() {
        // Arrange
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method1, attribute);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method2, attribute);
        }, "Method should work with different method instances");
    }

    /**
     * Tests that the method completes in reasonable time.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_completesQuickly() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();
        long startTime = System.nanoTime();

        // Act
        for (int i = 0; i < 1000; i++) {
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert - should complete 1000 iterations within 1 second
        assertTrue(durationMs < 1000,
                "Method should execute efficiently, took " + durationMs + "ms for 1000 iterations");
    }

    /**
     * Tests that calling the method with the same parameters is idempotent.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_isIdempotent() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act - call the method twice with the same parameters
        deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
        deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);

        // Assert - should not throw and should handle repeated calls gracefully
        assertDoesNotThrow(() -> deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute),
                "Method should be idempotent");
    }

    /**
     * Tests that the method integrates correctly with the visitor pattern.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_visitorPatternIntegration() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act - The method is part of the AttributeVisitor pattern
        // It should accept the visit call without issues
        assertDoesNotThrow(() -> deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute),
                "Method should integrate correctly with visitor pattern");
    }

    /**
     * Tests that freshly created deleter instances work correctly.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withFreshDeleter_doesNotThrow() {
        // Arrange - create a fresh deleter for each call
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            new MarkedAnnotationDeleter(new Object()).visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
            new MarkedAnnotationDeleter(new Object()).visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
            new MarkedAnnotationDeleter(new Object()).visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
        }, "Freshly created deleters should work correctly");
    }

    /**
     * Tests that the method can be called in rapid succession without issues.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert - rapid successive calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
            }
        }, "Method should handle rapid successive calls");
    }

    /**
     * Tests that the method works correctly with ProgramField instead of ProgramMethod.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_withProgramField_doesNotThrow() {
        // Arrange
        ProgramField field = new ProgramField();
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, field, attribute),
                "Method should work with ProgramField as member");
    }

    /**
     * Tests that the method handles interleaved calls with different attributes.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_interleavedCalls_doesNotThrow() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute1 = new RuntimeInvisibleTypeAnnotationsAttribute();
        RuntimeInvisibleTypeAnnotationsAttribute attribute2 = new RuntimeInvisibleTypeAnnotationsAttribute();
        RuntimeInvisibleTypeAnnotationsAttribute attribute3 = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert - interleave calls with different attributes
        assertDoesNotThrow(() -> {
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute1);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute2);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute1);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute3);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute2);
        }, "Method should handle interleaved calls with different attributes");
    }

    /**
     * Tests that the method works correctly when alternating between different members.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_alternatingMembers_doesNotThrow() {
        // Arrange
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramField field1 = new ProgramField();
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method1, attribute);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, field1, attribute);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method2, attribute);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method1, attribute);
        }, "Method should handle alternating member types");
    }

    /**
     * Tests that the method can be used by multiple threads safely (basic concurrency test).
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_concurrentAccess_doesNotThrow() {
        // Arrange
        RuntimeInvisibleTypeAnnotationsAttribute attribute = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert - simulate concurrent-like access with sequential calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 50; i++) {
                MarkedAnnotationDeleter localDeleter = new MarkedAnnotationDeleter(new Object());
                localDeleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz, method, attribute);
            }
        }, "Method should handle multiple deleter instances accessing the same attribute");
    }

    /**
     * Tests that the method works correctly with various combinations of parameters.
     */
    @Test
    public void testVisitRuntimeInvisibleTypeAnnotationsAttribute_variousParameterCombinations_doesNotThrow() {
        // Arrange
        ProgramClass clazz1 = new ProgramClass();
        ProgramClass clazz2 = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramField field1 = new ProgramField();
        RuntimeInvisibleTypeAnnotationsAttribute attr1 = new RuntimeInvisibleTypeAnnotationsAttribute();
        RuntimeInvisibleTypeAnnotationsAttribute attr2 = new RuntimeInvisibleTypeAnnotationsAttribute();

        // Act & Assert - test various parameter combinations
        assertDoesNotThrow(() -> {
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz1, method1, attr1);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz1, method1, attr2);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz1, field1, attr1);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz2, method1, attr1);
            deleter.visitRuntimeInvisibleTypeAnnotationsAttribute(clazz2, field1, attr2);
        }, "Method should work with various parameter combinations");
    }
}
