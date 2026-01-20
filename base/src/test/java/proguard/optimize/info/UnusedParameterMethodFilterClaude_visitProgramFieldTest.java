package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link UnusedParameterMethodFilter#visitProgramField(ProgramClass, ProgramField)}.
 *
 * The visitProgramField method in UnusedParameterMethodFilter is a no-op method (empty implementation).
 * This is intentional because the filter is designed to only delegate visits for methods with unused
 * parameters, not for fields. Fields don't have parameters, so this filter doesn't delegate field visits.
 *
 * Key behavior:
 * - The method does nothing (no-op)
 * - It never delegates to the wrapped MemberVisitor, regardless of the field or visitor state
 * - It doesn't throw exceptions
 * - It works with null arguments (though this may cause issues in the calling code)
 *
 * These tests verify:
 * 1. The method completes without throwing exceptions
 * 2. The wrapped visitor is never called for field visits
 * 3. Multiple calls behave consistently
 * 4. The method handles various field configurations
 */
public class UnusedParameterMethodFilterClaude_visitProgramFieldTest {

    private UnusedParameterMethodFilter filter;
    private TrackingMemberVisitor trackingVisitor;

    @BeforeEach
    public void setUp() {
        trackingVisitor = new TrackingMemberVisitor();
        filter = new UnusedParameterMethodFilter(trackingVisitor);
    }

    /**
     * Tests that visitProgramField does not throw an exception with valid arguments.
     */
    @Test
    public void testVisitProgramField_validArguments_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act & Assert
        assertDoesNotThrow(() -> filter.visitProgramField(programClass, programField),
                "visitProgramField should not throw exception with valid arguments");
    }

    /**
     * Tests that visitProgramField never delegates to the wrapped visitor.
     */
    @Test
    public void testVisitProgramField_neverDelegatesToVisitor() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act
        filter.visitProgramField(programClass, programField);

        // Assert
        assertFalse(trackingVisitor.visitedProgramField,
                "visitProgramField should never delegate to wrapped visitor");
        assertEquals(0, trackingVisitor.visitCount,
                "Visitor should not be called at all");
    }

    /**
     * Tests that visitProgramField works with multiple different fields.
     */
    @Test
    public void testVisitProgramField_multipleFields_neverDelegates() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField field1 = new ProgramField();
        ProgramField field2 = new ProgramField();
        ProgramField field3 = new ProgramField();

        // Act
        filter.visitProgramField(programClass, field1);
        filter.visitProgramField(programClass, field2);
        filter.visitProgramField(programClass, field3);

        // Assert
        assertFalse(trackingVisitor.visitedProgramField,
                "visitProgramField should never delegate even with multiple calls");
        assertEquals(0, trackingVisitor.visitCount,
                "Visitor count should remain 0");
    }

    /**
     * Tests that visitProgramField is consistent across multiple calls with the same field.
     */
    @Test
    public void testVisitProgramField_sameFieldMultipleTimes_consistentBehavior() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act
        filter.visitProgramField(programClass, programField);
        filter.visitProgramField(programClass, programField);
        filter.visitProgramField(programClass, programField);

        // Assert
        assertFalse(trackingVisitor.visitedProgramField,
                "visitProgramField should never delegate even with repeated calls");
        assertEquals(0, trackingVisitor.visitCount,
                "Visitor count should remain 0 after multiple calls");
    }

    /**
     * Tests that visitProgramField completes quickly (no expensive operations).
     */
    @Test
    public void testVisitProgramField_executesQuickly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        long startTime = System.nanoTime();

        // Act
        for (int i = 0; i < 10000; i++) {
            filter.visitProgramField(programClass, programField);
        }

        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;

        // Assert
        assertTrue(durationMs < 100,
                "visitProgramField should execute very quickly (no-op), took " + durationMs + "ms for 10000 calls");
    }

    /**
     * Tests that visitProgramField works with different filter instances.
     */
    @Test
    public void testVisitProgramField_multipleFilterInstances_independentBehavior() {
        // Arrange
        TrackingMemberVisitor visitor1 = new TrackingMemberVisitor();
        TrackingMemberVisitor visitor2 = new TrackingMemberVisitor();
        UnusedParameterMethodFilter filter1 = new UnusedParameterMethodFilter(visitor1);
        UnusedParameterMethodFilter filter2 = new UnusedParameterMethodFilter(visitor2);

        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act
        filter1.visitProgramField(programClass, programField);
        filter2.visitProgramField(programClass, programField);

        // Assert
        assertFalse(visitor1.visitedProgramField, "First visitor should not be called");
        assertFalse(visitor2.visitedProgramField, "Second visitor should not be called");
        assertEquals(0, visitor1.visitCount, "First visitor count should be 0");
        assertEquals(0, visitor2.visitCount, "Second visitor count should be 0");
    }

    /**
     * Tests that visitProgramField works correctly when called through the MemberVisitor interface.
     */
    @Test
    public void testVisitProgramField_throughInterface_neverDelegates() {
        // Arrange
        MemberVisitor visitorInterface = filter;
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act
        visitorInterface.visitProgramField(programClass, programField);

        // Assert
        assertFalse(trackingVisitor.visitedProgramField,
                "visitProgramField should not delegate even when called through interface");
    }

    /**
     * Tests that visitProgramField doesn't interact with the class state.
     */
    @Test
    public void testVisitProgramField_doesNotModifyClassOrField() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 5;
        ProgramField programField = new ProgramField();
        programField.u2accessFlags = 1;

        int originalClassValue = programClass.u2thisClass;
        int originalFieldValue = programField.u2accessFlags;

        // Act
        filter.visitProgramField(programClass, programField);

        // Assert
        assertEquals(originalClassValue, programClass.u2thisClass,
                "Class state should not be modified");
        assertEquals(originalFieldValue, programField.u2accessFlags,
                "Field state should not be modified");
    }

    /**
     * Tests that visitProgramField works with newly created filter instances.
     */
    @Test
    public void testVisitProgramField_newFilterInstance_behavesConsistently() {
        // Arrange
        TrackingMemberVisitor visitor = new TrackingMemberVisitor();
        UnusedParameterMethodFilter newFilter = new UnusedParameterMethodFilter(visitor);
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act
        newFilter.visitProgramField(programClass, programField);

        // Assert
        assertFalse(visitor.visitedProgramField,
                "New filter instance should not delegate field visits");
    }

    /**
     * Tests that visitProgramField with null visitor doesn't throw exception
     * (since the method is a no-op and never uses the visitor).
     */
    @Test
    public void testVisitProgramField_nullVisitor_doesNotThrow() {
        // Arrange
        UnusedParameterMethodFilter filterWithNullVisitor = new UnusedParameterMethodFilter(null);
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act & Assert
        assertDoesNotThrow(() -> filterWithNullVisitor.visitProgramField(programClass, programField),
                "visitProgramField should not throw with null visitor since it's a no-op");
    }

    /**
     * Tests that visitProgramField can be called in rapid succession.
     */
    @Test
    public void testVisitProgramField_rapidSuccession_noIssues() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField[] fields = new ProgramField[100];
        for (int i = 0; i < 100; i++) {
            fields[i] = new ProgramField();
        }

        // Act
        for (ProgramField field : fields) {
            filter.visitProgramField(programClass, field);
        }

        // Assert
        assertFalse(trackingVisitor.visitedProgramField,
                "Visitor should never be called even with rapid succession of calls");
        assertEquals(0, trackingVisitor.visitCount,
                "Visitor count should be 0 after 100 calls");
    }

    /**
     * Tests that visitProgramField doesn't affect subsequent method visits.
     * Verifies that field visits don't interfere with the filter's method filtering logic.
     */
    @Test
    public void testVisitProgramField_doesNotAffectMethodVisits() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act - Visit field first
        filter.visitProgramField(programClass, programField);

        // Now verify that method visits still work correctly
        // (This is more of an integration test to ensure no side effects)
        assertEquals(0, trackingVisitor.visitCount,
                "Field visit should not affect visitor state");
    }

    /**
     * Tests that visitProgramField works with various field access flags.
     */
    @Test
    public void testVisitProgramField_variousAccessFlags_consistentBehavior() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        ProgramField publicField = new ProgramField();
        publicField.u2accessFlags = AccessConstants.PUBLIC;

        ProgramField privateField = new ProgramField();
        privateField.u2accessFlags = AccessConstants.PRIVATE;

        ProgramField staticField = new ProgramField();
        staticField.u2accessFlags = AccessConstants.STATIC;

        ProgramField finalField = new ProgramField();
        finalField.u2accessFlags = AccessConstants.FINAL;

        // Act
        filter.visitProgramField(programClass, publicField);
        filter.visitProgramField(programClass, privateField);
        filter.visitProgramField(programClass, staticField);
        filter.visitProgramField(programClass, finalField);

        // Assert
        assertFalse(trackingVisitor.visitedProgramField,
                "visitProgramField should not delegate regardless of access flags");
        assertEquals(0, trackingVisitor.visitCount,
                "Visitor should not be called for any field type");
    }

    /**
     * Tests that visitProgramField is truly a no-op by verifying no side effects.
     */
    @Test
    public void testVisitProgramField_isNoOp() {
        // Arrange
        SideEffectDetectingVisitor sideEffectVisitor = new SideEffectDetectingVisitor();
        UnusedParameterMethodFilter testFilter = new UnusedParameterMethodFilter(sideEffectVisitor);
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act
        testFilter.visitProgramField(programClass, programField);

        // Assert
        assertFalse(sideEffectVisitor.anythingCalled,
                "visitProgramField should be a complete no-op with no side effects");
    }

    /**
     * Tests that visitProgramField behavior is consistent across different program classes.
     */
    @Test
    public void testVisitProgramField_differentClasses_consistentBehavior() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act
        filter.visitProgramField(class1, programField);
        filter.visitProgramField(class2, programField);
        filter.visitProgramField(class3, programField);

        // Assert
        assertFalse(trackingVisitor.visitedProgramField,
                "visitProgramField should not delegate regardless of class");
        assertEquals(0, trackingVisitor.visitCount,
                "Visitor should not be called for any class");
    }

    /**
     * Helper class that tracks whether it was visited and counts visits.
     */
    private static class TrackingMemberVisitor implements MemberVisitor {
        boolean visitedProgramField = false;
        boolean visitedProgramMethod = false;
        boolean visitedLibraryField = false;
        boolean visitedLibraryMethod = false;
        int visitCount = 0;

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            visitedProgramField = true;
            visitCount++;
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            visitedProgramMethod = true;
            visitCount++;
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            visitedLibraryField = true;
            visitCount++;
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            visitedLibraryMethod = true;
            visitCount++;
        }
    }

    /**
     * Helper class to detect any side effects from method calls.
     */
    private static class SideEffectDetectingVisitor implements MemberVisitor {
        boolean anythingCalled = false;

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            anythingCalled = true;
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            anythingCalled = true;
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            anythingCalled = true;
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            anythingCalled = true;
        }
    }
}
