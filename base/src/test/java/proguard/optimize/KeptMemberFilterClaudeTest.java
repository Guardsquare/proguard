package proguard.optimize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.LibraryMethod;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.visitor.MemberVisitor;
import proguard.optimize.info.FieldOptimizationInfo;
import proguard.optimize.info.MethodOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link KeptMemberFilter}.
 *
 * KeptMemberFilter is a MemberVisitor that delegates to another MemberVisitor
 * only when the member is marked as kept (via KeepMarker).
 *
 * The filter checks if each visited member is kept using KeepMarker.isKept(),
 * and only delegates to the wrapped visitor if the member is kept.
 *
 * These tests verify:
 * 1. Constructor properly stores the delegate visitor
 * 2. visitProgramField delegates only for kept fields
 * 3. visitProgramMethod delegates only for kept methods
 * 4. visitLibraryField delegates only for kept fields
 * 5. visitLibraryMethod delegates only for kept methods
 */
public class KeptMemberFilterClaudeTest {

    /**
     * A simple tracking visitor that records when it's been visited.
     * Used to verify delegation behavior.
     */
    private static class TrackingMemberVisitor implements MemberVisitor {
        private int programFieldVisitCount = 0;
        private int programMethodVisitCount = 0;
        private int libraryFieldVisitCount = 0;
        private int libraryMethodVisitCount = 0;

        private ProgramClass lastProgramClass = null;
        private ProgramField lastProgramField = null;
        private ProgramMethod lastProgramMethod = null;
        private LibraryClass lastLibraryClass = null;
        private LibraryField lastLibraryField = null;
        private LibraryMethod lastLibraryMethod = null;

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            programFieldVisitCount++;
            lastProgramClass = programClass;
            lastProgramField = programField;
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            programMethodVisitCount++;
            lastProgramClass = programClass;
            lastProgramMethod = programMethod;
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            libraryFieldVisitCount++;
            lastLibraryClass = libraryClass;
            lastLibraryField = libraryField;
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            libraryMethodVisitCount++;
            lastLibraryClass = libraryClass;
            lastLibraryMethod = libraryMethod;
        }

        public void reset() {
            programFieldVisitCount = 0;
            programMethodVisitCount = 0;
            libraryFieldVisitCount = 0;
            libraryMethodVisitCount = 0;
            lastProgramClass = null;
            lastProgramField = null;
            lastProgramMethod = null;
            lastLibraryClass = null;
            lastLibraryField = null;
            lastLibraryMethod = null;
        }
    }

    private TrackingMemberVisitor trackingVisitor;
    private KeptMemberFilter filter;

    @BeforeEach
    public void setUp() {
        trackingVisitor = new TrackingMemberVisitor();
        filter = new KeptMemberFilter(trackingVisitor);
    }

    // ========== Constructor Tests ==========

    /**
     * Tests that the constructor properly initializes the filter.
     * The filter should accept a non-null visitor.
     */
    @Test
    public void testConstructor_withValidVisitor_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> new KeptMemberFilter(trackingVisitor),
                "Constructor should accept valid visitor");
    }

    /**
     * Tests that the constructor can accept a null visitor.
     * While not typical usage, it should not throw during construction.
     */
    @Test
    public void testConstructor_withNullVisitor_doesNotThrow() {
        // Act & Assert
        assertDoesNotThrow(() -> new KeptMemberFilter(null),
                "Constructor should handle null visitor");
    }

    // ========== visitProgramField Tests ==========

    /**
     * Tests that visitProgramField delegates to the wrapped visitor when the field is kept.
     * A kept field (with FieldOptimizationInfo) should be passed to the delegate.
     */
    @Test
    public void testVisitProgramField_withKeptField_delegatesToVisitor() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, programField);

        // Act
        filter.visitProgramField(programClass, programField);

        // Assert
        assertEquals(1, trackingVisitor.programFieldVisitCount,
                "Visitor should be called once for kept field");
        assertSame(programClass, trackingVisitor.lastProgramClass,
                "Visitor should receive the same class");
        assertSame(programField, trackingVisitor.lastProgramField,
                "Visitor should receive the same field");
    }

    /**
     * Tests that visitProgramField does NOT delegate when the field is not kept.
     * A non-kept field (without FieldOptimizationInfo) should be filtered out.
     */
    @Test
    public void testVisitProgramField_withNonKeptField_doesNotDelegate() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        // Don't set FieldOptimizationInfo - field is not kept

        // Act
        filter.visitProgramField(programClass, programField);

        // Assert
        assertEquals(0, trackingVisitor.programFieldVisitCount,
                "Visitor should not be called for non-kept field");
    }

    /**
     * Tests that visitProgramField can handle multiple kept fields.
     * Each kept field should be delegated to the visitor.
     */
    @Test
    public void testVisitProgramField_withMultipleKeptFields_delegatesAll() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField field1 = new ProgramField();
        ProgramField field2 = new ProgramField();
        ProgramField field3 = new ProgramField();

        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, field1);
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, field2);
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, field3);

        // Act
        filter.visitProgramField(programClass, field1);
        filter.visitProgramField(programClass, field2);
        filter.visitProgramField(programClass, field3);

        // Assert
        assertEquals(3, trackingVisitor.programFieldVisitCount,
                "Visitor should be called three times");
        assertSame(field3, trackingVisitor.lastProgramField,
                "Last field should be the third one");
    }

    /**
     * Tests that visitProgramField correctly filters a mix of kept and non-kept fields.
     */
    @Test
    public void testVisitProgramField_withMixedFields_filtersCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField keptField1 = new ProgramField();
        ProgramField nonKeptField1 = new ProgramField();
        ProgramField keptField2 = new ProgramField();
        ProgramField nonKeptField2 = new ProgramField();

        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, keptField1);
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, keptField2);

        // Act
        filter.visitProgramField(programClass, keptField1);
        filter.visitProgramField(programClass, nonKeptField1);
        filter.visitProgramField(programClass, keptField2);
        filter.visitProgramField(programClass, nonKeptField2);

        // Assert
        assertEquals(2, trackingVisitor.programFieldVisitCount,
                "Visitor should only be called for kept fields");
    }

    /**
     * Tests that visitProgramField with null visitor doesn't throw for non-kept field.
     */
    @Test
    public void testVisitProgramField_withNullVisitorAndNonKeptField_doesNotThrow() {
        // Arrange
        KeptMemberFilter nullFilter = new KeptMemberFilter(null);
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act & Assert
        assertDoesNotThrow(() -> nullFilter.visitProgramField(programClass, programField),
                "Filter with null visitor should not throw for non-kept field");
    }

    /**
     * Tests that visitProgramField with null visitor throws NullPointerException for kept field.
     * When the field is kept, the filter attempts to delegate, causing NPE with null visitor.
     */
    @Test
    public void testVisitProgramField_withNullVisitorAndKeptField_throwsNullPointerException() {
        // Arrange
        KeptMemberFilter nullFilter = new KeptMemberFilter(null);
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, programField);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> nullFilter.visitProgramField(programClass, programField),
                "Filter with null visitor should throw NPE when delegating for kept field");
    }

    // ========== visitProgramMethod Tests ==========

    /**
     * Tests that visitProgramMethod delegates to the wrapped visitor when the method is kept.
     * A kept method (with MethodOptimizationInfo) should be passed to the delegate.
     */
    @Test
    public void testVisitProgramMethod_withKeptMethod_delegatesToVisitor() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert
        assertEquals(1, trackingVisitor.programMethodVisitCount,
                "Visitor should be called once for kept method");
        assertSame(programClass, trackingVisitor.lastProgramClass,
                "Visitor should receive the same class");
        assertSame(programMethod, trackingVisitor.lastProgramMethod,
                "Visitor should receive the same method");
    }

    /**
     * Tests that visitProgramMethod does NOT delegate when the method is not kept.
     * A non-kept method (without MethodOptimizationInfo) should be filtered out.
     */
    @Test
    public void testVisitProgramMethod_withNonKeptMethod_doesNotDelegate() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        // Don't set MethodOptimizationInfo - method is not kept

        // Act
        filter.visitProgramMethod(programClass, programMethod);

        // Assert
        assertEquals(0, trackingVisitor.programMethodVisitCount,
                "Visitor should not be called for non-kept method");
    }

    /**
     * Tests that visitProgramMethod can handle multiple kept methods.
     * Each kept method should be delegated to the visitor.
     */
    @Test
    public void testVisitProgramMethod_withMultipleKeptMethods_delegatesAll() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod method1 = new ProgramMethod();
        ProgramMethod method2 = new ProgramMethod();
        ProgramMethod method3 = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method2);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, method3);

        // Act
        filter.visitProgramMethod(programClass, method1);
        filter.visitProgramMethod(programClass, method2);
        filter.visitProgramMethod(programClass, method3);

        // Assert
        assertEquals(3, trackingVisitor.programMethodVisitCount,
                "Visitor should be called three times");
        assertSame(method3, trackingVisitor.lastProgramMethod,
                "Last method should be the third one");
    }

    /**
     * Tests that visitProgramMethod correctly filters a mix of kept and non-kept methods.
     */
    @Test
    public void testVisitProgramMethod_withMixedMethods_filtersCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramMethod keptMethod1 = new ProgramMethod();
        ProgramMethod nonKeptMethod1 = new ProgramMethod();
        ProgramMethod keptMethod2 = new ProgramMethod();
        ProgramMethod nonKeptMethod2 = new ProgramMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, keptMethod1);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, keptMethod2);

        // Act
        filter.visitProgramMethod(programClass, keptMethod1);
        filter.visitProgramMethod(programClass, nonKeptMethod1);
        filter.visitProgramMethod(programClass, keptMethod2);
        filter.visitProgramMethod(programClass, nonKeptMethod2);

        // Assert
        assertEquals(2, trackingVisitor.programMethodVisitCount,
                "Visitor should only be called for kept methods");
    }

    /**
     * Tests that visitProgramMethod with null visitor doesn't throw for non-kept method.
     */
    @Test
    public void testVisitProgramMethod_withNullVisitorAndNonKeptMethod_doesNotThrow() {
        // Arrange
        KeptMemberFilter nullFilter = new KeptMemberFilter(null);
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();

        // Act & Assert
        assertDoesNotThrow(() -> nullFilter.visitProgramMethod(programClass, programMethod),
                "Filter with null visitor should not throw for non-kept method");
    }

    /**
     * Tests that visitProgramMethod with null visitor throws NullPointerException for kept method.
     * When the method is kept, the filter attempts to delegate, causing NPE with null visitor.
     */
    @Test
    public void testVisitProgramMethod_withNullVisitorAndKeptMethod_throwsNullPointerException() {
        // Arrange
        KeptMemberFilter nullFilter = new KeptMemberFilter(null);
        ProgramClass programClass = new ProgramClass();
        ProgramMethod programMethod = new ProgramMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, programMethod);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> nullFilter.visitProgramMethod(programClass, programMethod),
                "Filter with null visitor should throw NPE when delegating for kept method");
    }

    // ========== visitLibraryField Tests ==========

    /**
     * Tests that visitLibraryField delegates to the wrapped visitor when the field is kept.
     * A kept library field (with FieldOptimizationInfo) should be passed to the delegate.
     */
    @Test
    public void testVisitLibraryField_withKeptField_delegatesToVisitor() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryField libraryField = new LibraryField();
        FieldOptimizationInfo.setFieldOptimizationInfo(libraryClass, libraryField);

        // Act
        filter.visitLibraryField(libraryClass, libraryField);

        // Assert
        assertEquals(1, trackingVisitor.libraryFieldVisitCount,
                "Visitor should be called once for kept library field");
        assertSame(libraryClass, trackingVisitor.lastLibraryClass,
                "Visitor should receive the same class");
        assertSame(libraryField, trackingVisitor.lastLibraryField,
                "Visitor should receive the same field");
    }

    /**
     * Tests that visitLibraryField does NOT delegate when the field is not kept.
     * A non-kept library field (without FieldOptimizationInfo) should be filtered out.
     */
    @Test
    public void testVisitLibraryField_withNonKeptField_doesNotDelegate() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryField libraryField = new LibraryField();
        // Don't set FieldOptimizationInfo - field is not kept

        // Act
        filter.visitLibraryField(libraryClass, libraryField);

        // Assert
        assertEquals(0, trackingVisitor.libraryFieldVisitCount,
                "Visitor should not be called for non-kept library field");
    }

    /**
     * Tests that visitLibraryField can handle multiple kept library fields.
     * Each kept library field should be delegated to the visitor.
     */
    @Test
    public void testVisitLibraryField_withMultipleKeptFields_delegatesAll() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryField field1 = new LibraryField();
        LibraryField field2 = new LibraryField();
        LibraryField field3 = new LibraryField();

        FieldOptimizationInfo.setFieldOptimizationInfo(libraryClass, field1);
        FieldOptimizationInfo.setFieldOptimizationInfo(libraryClass, field2);
        FieldOptimizationInfo.setFieldOptimizationInfo(libraryClass, field3);

        // Act
        filter.visitLibraryField(libraryClass, field1);
        filter.visitLibraryField(libraryClass, field2);
        filter.visitLibraryField(libraryClass, field3);

        // Assert
        assertEquals(3, trackingVisitor.libraryFieldVisitCount,
                "Visitor should be called three times");
        assertSame(field3, trackingVisitor.lastLibraryField,
                "Last field should be the third one");
    }

    /**
     * Tests that visitLibraryField correctly filters a mix of kept and non-kept library fields.
     */
    @Test
    public void testVisitLibraryField_withMixedFields_filtersCorrectly() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryField keptField1 = new LibraryField();
        LibraryField nonKeptField1 = new LibraryField();
        LibraryField keptField2 = new LibraryField();
        LibraryField nonKeptField2 = new LibraryField();

        FieldOptimizationInfo.setFieldOptimizationInfo(libraryClass, keptField1);
        FieldOptimizationInfo.setFieldOptimizationInfo(libraryClass, keptField2);

        // Act
        filter.visitLibraryField(libraryClass, keptField1);
        filter.visitLibraryField(libraryClass, nonKeptField1);
        filter.visitLibraryField(libraryClass, keptField2);
        filter.visitLibraryField(libraryClass, nonKeptField2);

        // Assert
        assertEquals(2, trackingVisitor.libraryFieldVisitCount,
                "Visitor should only be called for kept library fields");
    }

    /**
     * Tests that visitLibraryField with null visitor doesn't throw for non-kept field.
     */
    @Test
    public void testVisitLibraryField_withNullVisitorAndNonKeptField_doesNotThrow() {
        // Arrange
        KeptMemberFilter nullFilter = new KeptMemberFilter(null);
        LibraryClass libraryClass = new LibraryClass();
        LibraryField libraryField = new LibraryField();

        // Act & Assert
        assertDoesNotThrow(() -> nullFilter.visitLibraryField(libraryClass, libraryField),
                "Filter with null visitor should not throw for non-kept library field");
    }

    /**
     * Tests that visitLibraryField with null visitor throws NullPointerException for kept field.
     * When the library field is kept, the filter attempts to delegate, causing NPE with null visitor.
     */
    @Test
    public void testVisitLibraryField_withNullVisitorAndKeptField_throwsNullPointerException() {
        // Arrange
        KeptMemberFilter nullFilter = new KeptMemberFilter(null);
        LibraryClass libraryClass = new LibraryClass();
        LibraryField libraryField = new LibraryField();
        FieldOptimizationInfo.setFieldOptimizationInfo(libraryClass, libraryField);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> nullFilter.visitLibraryField(libraryClass, libraryField),
                "Filter with null visitor should throw NPE when delegating for kept library field");
    }

    // ========== visitLibraryMethod Tests ==========

    /**
     * Tests that visitLibraryMethod delegates to the wrapped visitor when the method is kept.
     * A kept library method (with MethodOptimizationInfo) should be passed to the delegate.
     */
    @Test
    public void testVisitLibraryMethod_withKeptMethod_delegatesToVisitor() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);

        // Act
        filter.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(1, trackingVisitor.libraryMethodVisitCount,
                "Visitor should be called once for kept library method");
        assertSame(libraryClass, trackingVisitor.lastLibraryClass,
                "Visitor should receive the same class");
        assertSame(libraryMethod, trackingVisitor.lastLibraryMethod,
                "Visitor should receive the same method");
    }

    /**
     * Tests that visitLibraryMethod does NOT delegate when the method is not kept.
     * A non-kept library method (without MethodOptimizationInfo) should be filtered out.
     */
    @Test
    public void testVisitLibraryMethod_withNonKeptMethod_doesNotDelegate() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        // Don't set MethodOptimizationInfo - method is not kept

        // Act
        filter.visitLibraryMethod(libraryClass, libraryMethod);

        // Assert
        assertEquals(0, trackingVisitor.libraryMethodVisitCount,
                "Visitor should not be called for non-kept library method");
    }

    /**
     * Tests that visitLibraryMethod can handle multiple kept library methods.
     * Each kept library method should be delegated to the visitor.
     */
    @Test
    public void testVisitLibraryMethod_withMultipleKeptMethods_delegatesAll() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod method1 = new LibraryMethod();
        LibraryMethod method2 = new LibraryMethod();
        LibraryMethod method3 = new LibraryMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, method1);
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, method2);
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, method3);

        // Act
        filter.visitLibraryMethod(libraryClass, method1);
        filter.visitLibraryMethod(libraryClass, method2);
        filter.visitLibraryMethod(libraryClass, method3);

        // Assert
        assertEquals(3, trackingVisitor.libraryMethodVisitCount,
                "Visitor should be called three times");
        assertSame(method3, trackingVisitor.lastLibraryMethod,
                "Last method should be the third one");
    }

    /**
     * Tests that visitLibraryMethod correctly filters a mix of kept and non-kept library methods.
     */
    @Test
    public void testVisitLibraryMethod_withMixedMethods_filtersCorrectly() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod keptMethod1 = new LibraryMethod();
        LibraryMethod nonKeptMethod1 = new LibraryMethod();
        LibraryMethod keptMethod2 = new LibraryMethod();
        LibraryMethod nonKeptMethod2 = new LibraryMethod();

        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, keptMethod1);
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, keptMethod2);

        // Act
        filter.visitLibraryMethod(libraryClass, keptMethod1);
        filter.visitLibraryMethod(libraryClass, nonKeptMethod1);
        filter.visitLibraryMethod(libraryClass, keptMethod2);
        filter.visitLibraryMethod(libraryClass, nonKeptMethod2);

        // Assert
        assertEquals(2, trackingVisitor.libraryMethodVisitCount,
                "Visitor should only be called for kept library methods");
    }

    /**
     * Tests that visitLibraryMethod with null visitor doesn't throw for non-kept method.
     */
    @Test
    public void testVisitLibraryMethod_withNullVisitorAndNonKeptMethod_doesNotThrow() {
        // Arrange
        KeptMemberFilter nullFilter = new KeptMemberFilter(null);
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();

        // Act & Assert
        assertDoesNotThrow(() -> nullFilter.visitLibraryMethod(libraryClass, libraryMethod),
                "Filter with null visitor should not throw for non-kept library method");
    }

    /**
     * Tests that visitLibraryMethod with null visitor throws NullPointerException for kept method.
     * When the library method is kept, the filter attempts to delegate, causing NPE with null visitor.
     */
    @Test
    public void testVisitLibraryMethod_withNullVisitorAndKeptMethod_throwsNullPointerException() {
        // Arrange
        KeptMemberFilter nullFilter = new KeptMemberFilter(null);
        LibraryClass libraryClass = new LibraryClass();
        LibraryMethod libraryMethod = new LibraryMethod();
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, libraryMethod);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> nullFilter.visitLibraryMethod(libraryClass, libraryMethod),
                "Filter with null visitor should throw NPE when delegating for kept library method");
    }

    // ========== Integration and Edge Case Tests ==========

    /**
     * Tests that the filter can be used as a MemberVisitor interface.
     * Verifies integration with the visitor pattern.
     */
    @Test
    public void testFilterUsedAsMemberVisitor_worksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, programField);

        MemberVisitor visitor = filter;

        // Act
        visitor.visitProgramField(programClass, programField);

        // Assert
        assertEquals(1, trackingVisitor.programFieldVisitCount,
                "Filter should work through MemberVisitor interface");
    }

    /**
     * Tests that the same filter instance can be reused for multiple visits.
     * The filter should maintain consistent behavior across multiple uses.
     */
    @Test
    public void testFilterReuse_worksConsistently() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        ProgramField keptProgramField = new ProgramField();
        ProgramMethod keptProgramMethod = new ProgramMethod();
        LibraryField keptLibraryField = new LibraryField();
        LibraryMethod keptLibraryMethod = new LibraryMethod();

        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, keptProgramField);
        MethodOptimizationInfo.setMethodOptimizationInfo(programClass, keptProgramMethod);
        FieldOptimizationInfo.setFieldOptimizationInfo(libraryClass, keptLibraryField);
        MethodOptimizationInfo.setMethodOptimizationInfo(libraryClass, keptLibraryMethod);

        // Act
        filter.visitProgramField(programClass, keptProgramField);
        filter.visitProgramMethod(programClass, keptProgramMethod);
        filter.visitLibraryField(libraryClass, keptLibraryField);
        filter.visitLibraryMethod(libraryClass, keptLibraryMethod);

        // Assert
        assertEquals(1, trackingVisitor.programFieldVisitCount, "Should visit program field");
        assertEquals(1, trackingVisitor.programMethodVisitCount, "Should visit program method");
        assertEquals(1, trackingVisitor.libraryFieldVisitCount, "Should visit library field");
        assertEquals(1, trackingVisitor.libraryMethodVisitCount, "Should visit library method");
    }

    /**
     * Tests that the filter correctly handles changing member kept status.
     * If a member's kept status changes between visits, the filter should respond accordingly.
     */
    @Test
    public void testFilterWithChangingKeptStatus_respondsToChanges() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();

        // Act & Assert - visit when not kept
        filter.visitProgramField(programClass, programField);
        assertEquals(0, trackingVisitor.programFieldVisitCount,
                "Should not delegate when field is not kept");

        // Mark as kept and visit again
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, programField);
        filter.visitProgramField(programClass, programField);
        assertEquals(1, trackingVisitor.programFieldVisitCount,
                "Should delegate when field becomes kept");

        // Remove kept status and visit again
        programField.setProcessingInfo(null);
        filter.visitProgramField(programClass, programField);
        assertEquals(1, trackingVisitor.programFieldVisitCount,
                "Should not delegate again when field is no longer kept");
    }

    /**
     * Tests integration with KeepMarker.
     * After KeepMarker visits a member, KeptMemberFilter should delegate it.
     */
    @Test
    public void testIntegrationWithKeepMarker_delegatesMarkedMembers() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        ProgramMethod programMethod = new ProgramMethod();

        KeepMarker keepMarker = new KeepMarker();

        // Act - mark the members with KeepMarker
        keepMarker.visitProgramField(programClass, programField);
        keepMarker.visitProgramMethod(programClass, programMethod);

        // Filter the members
        filter.visitProgramField(programClass, programField);
        filter.visitProgramMethod(programClass, programMethod);

        // Assert
        assertEquals(1, trackingVisitor.programFieldVisitCount,
                "Should delegate field marked by KeepMarker");
        assertEquals(1, trackingVisitor.programMethodVisitCount,
                "Should delegate method marked by KeepMarker");
    }

    /**
     * Tests that the filter handles a batch of members efficiently.
     * Simulates processing many members at once.
     */
    @Test
    public void testFilterWithManyMembers_handlesEfficiently() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        int totalFields = 100;
        int keptFields = 50;

        // Act - process 100 fields (50 kept, 50 not kept)
        for (int i = 0; i < totalFields; i++) {
            ProgramField field = new ProgramField();
            if (i < keptFields) {
                FieldOptimizationInfo.setFieldOptimizationInfo(programClass, field);
            }
            filter.visitProgramField(programClass, field);
        }

        // Assert
        assertEquals(keptFields, trackingVisitor.programFieldVisitCount,
                "Should only delegate kept fields");
    }

    /**
     * Tests that the filter does not modify the members it filters.
     * The filter should only route, not modify.
     */
    @Test
    public void testFilterDoesNotModifyMembers() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField programField = new ProgramField();
        FieldOptimizationInfo info = new FieldOptimizationInfo();
        programField.setProcessingInfo(info);

        // Act
        filter.visitProgramField(programClass, programField);

        // Assert
        assertSame(info, programField.getProcessingInfo(),
                "Field processing info should not be modified");
    }

    /**
     * Tests that exceptions from the delegate visitor are propagated.
     * The filter should not catch or suppress exceptions.
     */
    @Test
    public void testFilterPropagatesExceptions() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramField keptField = new ProgramField();
        FieldOptimizationInfo.setFieldOptimizationInfo(programClass, keptField);

        MemberVisitor throwingVisitor = new MemberVisitor() {
            @Override
            public void visitProgramField(ProgramClass programClass, ProgramField programField) {
                throw new RuntimeException("Test exception");
            }

            @Override
            public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {}

            @Override
            public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {}

            @Override
            public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {}
        };

        KeptMemberFilter throwingFilter = new KeptMemberFilter(throwingVisitor);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> throwingFilter.visitProgramField(programClass, keptField),
                "Exception from delegate should be propagated");
        assertEquals("Test exception", exception.getMessage());
    }
}
