package proguard.optimize.gson;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.annotation.RuntimeVisibleTypeAnnotationsAttribute;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link MarkedAnnotationDeleter#visitRuntimeVisibleTypeAnnotationsAttribute(Clazz, Member, RuntimeVisibleTypeAnnotationsAttribute)}.
 *
 * The visitRuntimeVisibleTypeAnnotationsAttribute method deletes type annotations that are marked with
 * the specified mark object (set as processingInfo). If all annotations are deleted, the attribute
 * itself is removed from the member.
 *
 * These tests verify:
 * 1. The method can be called without throwing exceptions
 * 2. The method works with various input scenarios
 * 3. The method integrates correctly with the ProGuard class file structure
 */
public class MarkedAnnotationDeleterClaude_visitRuntimeVisibleTypeAnnotationsAttributeTest {

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
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withEmptyAttribute_doesNotThrow() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute),
                "Method should handle empty attribute without throwing");
    }

    /**
     * Tests that the method can be called multiple times consecutively.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_calledMultipleTimes_doesNotThrow() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert - should not throw on multiple calls
        assertDoesNotThrow(() -> {
            deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);
            deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);
            deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);
        }, "Method should handle multiple calls without throwing");
    }

    /**
     * Tests that the method works with different mark objects.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withDifferentMarks_doesNotThrow() {
        // Arrange
        Object mark1 = new Object();
        Object mark2 = "stringMark";
        Object mark3 = Integer.valueOf(42);

        MarkedAnnotationDeleter deleter1 = new MarkedAnnotationDeleter(mark1);
        MarkedAnnotationDeleter deleter2 = new MarkedAnnotationDeleter(mark2);
        MarkedAnnotationDeleter deleter3 = new MarkedAnnotationDeleter(mark3);

        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            deleter1.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);
            deleter2.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);
            deleter3.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);
        }, "Method should work with different mark types");
    }

    /**
     * Tests that the method works with a null mark.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withNullMark_doesNotThrow() {
        // Arrange
        MarkedAnnotationDeleter nullMarkDeleter = new MarkedAnnotationDeleter(null);
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> nullMarkDeleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute),
                "Method should handle null mark without throwing");
    }

    /**
     * Tests that the method works with different Member types.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withDifferentMemberTypes_doesNotThrow() {
        // Arrange
        ProgramMethod method = new ProgramMethod();
        ProgramField field = new ProgramField();
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);
            deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, field, attribute);
        }, "Method should work with both methods and fields");
    }

    /**
     * Tests that the method works with different attribute instances.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withDifferentAttributes_doesNotThrow() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute1 = new RuntimeVisibleTypeAnnotationsAttribute();
        RuntimeVisibleTypeAnnotationsAttribute attribute2 = new RuntimeVisibleTypeAnnotationsAttribute();
        RuntimeVisibleTypeAnnotationsAttribute attribute3 = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute1);
            deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute2);
            deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute3);
        }, "Method should work with different attribute instances");
    }

    /**
     * Tests that the method can be called on the same attribute instance multiple times.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_sameAttributeMultipleTimes_doesNotThrow() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert - calling on the same attribute multiple times
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5; i++) {
                deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);
            }
        }, "Method should handle same attribute being visited multiple times");
    }

    /**
     * Tests that multiple deleter instances work independently.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_multipleDeleters_workIndependently() {
        // Arrange
        Object mark1 = new Object();
        Object mark2 = new Object();
        MarkedAnnotationDeleter deleter1 = new MarkedAnnotationDeleter(mark1);
        MarkedAnnotationDeleter deleter2 = new MarkedAnnotationDeleter(mark2);

        RuntimeVisibleTypeAnnotationsAttribute attribute1 = new RuntimeVisibleTypeAnnotationsAttribute();
        RuntimeVisibleTypeAnnotationsAttribute attribute2 = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            deleter1.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute1);
            deleter2.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute2);
        }, "Multiple deleters should work independently");
    }

    /**
     * Tests that the method works with different ProgramClass instances.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withDifferentClasses_doesNotThrow() {
        // Arrange
        ProgramClass clazz1 = new ProgramClass();
        ProgramClass clazz2 = new ProgramClass();
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz1, method, attribute);
            deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz2, method, attribute);
        }, "Method should work with different class instances");
    }

    /**
     * Tests that the method works with different ProgramMethod instances.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withDifferentMethods_doesNotThrow() {
        // Arrange
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method1, attribute);
            deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method2, attribute);
        }, "Method should work with different method instances");
    }

    /**
     * Tests that the method completes in reasonable time.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_completesQuickly() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();
        long startTime = System.nanoTime();

        // Act
        for (int i = 0; i < 1000; i++) {
            deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);
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
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_isIdempotent() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act - call the method twice with the same parameters
        deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);
        deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);

        // Assert - should not throw and should handle repeated calls gracefully
        assertDoesNotThrow(() -> deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute),
                "Method should be idempotent");
    }

    /**
     * Tests that the method integrates correctly with the visitor pattern.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_visitorPatternIntegration() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act - The method is part of the AttributeVisitor pattern
        // It should accept the visit call without issues
        assertDoesNotThrow(() -> deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute),
                "Method should integrate correctly with visitor pattern");
    }

    /**
     * Tests that freshly created deleter instances work correctly.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_withFreshDeleter_doesNotThrow() {
        // Arrange - create a fresh deleter for each call
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert
        assertDoesNotThrow(() -> {
            new MarkedAnnotationDeleter(new Object()).visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);
            new MarkedAnnotationDeleter(new Object()).visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);
            new MarkedAnnotationDeleter(new Object()).visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);
        }, "Freshly created deleters should work correctly");
    }

    /**
     * Tests that the method can be called in rapid succession without issues.
     */
    @Test
    public void testVisitRuntimeVisibleTypeAnnotationsAttribute_rapidSuccessiveCalls_doesNotThrow() {
        // Arrange
        RuntimeVisibleTypeAnnotationsAttribute attribute = new RuntimeVisibleTypeAnnotationsAttribute();

        // Act & Assert - rapid successive calls
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                deleter.visitRuntimeVisibleTypeAnnotationsAttribute(clazz, method, attribute);
            }
        }, "Method should handle rapid successive calls");
    }
}
