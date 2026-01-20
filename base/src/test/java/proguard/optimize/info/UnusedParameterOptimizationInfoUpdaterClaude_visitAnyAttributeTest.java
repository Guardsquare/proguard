package proguard.optimize.info;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.attribute.*;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link UnusedParameterOptimizationInfoUpdater#visitAnyAttribute(Clazz, Attribute)}.
 *
 * The visitAnyAttribute method is a no-op implementation that does nothing. It serves as the
 * default fallback in the AttributeVisitor pattern, while specific attribute types are handled
 * by specialized methods like visitCodeAttribute.
 */
public class UnusedParameterOptimizationInfoUpdaterClaude_visitAnyAttributeTest {

    /**
     * Tests that visitAnyAttribute does nothing when called with valid parameters.
     */
    @Test
    public void testVisitAnyAttributeWithValidParameters() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();
        ProgramClass programClass = new ProgramClass();
        Attribute attribute = mock(Attribute.class);

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(programClass, attribute);
        }, "visitAnyAttribute should not throw exception with valid parameters");
    }

    /**
     * Tests that visitAnyAttribute does nothing with a mock Clazz.
     */
    @Test
    public void testVisitAnyAttributeWithMockClazz() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();
        Clazz mockClazz = mock(ProgramClass.class);
        Attribute attribute = mock(Attribute.class);

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(mockClazz, attribute);
        }, "visitAnyAttribute should not throw exception with mock Clazz");
    }

    /**
     * Tests visitAnyAttribute with a LibraryClass instead of ProgramClass.
     */
    @Test
    public void testVisitAnyAttributeWithLibraryClass() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();
        LibraryClass libraryClass = new LibraryClass();
        Attribute attribute = mock(Attribute.class);

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(libraryClass, attribute);
        }, "visitAnyAttribute should handle LibraryClass without exception");
    }

    /**
     * Tests visitAnyAttribute with different attribute types.
     */
    @Test
    public void testVisitAnyAttributeWithDifferentAttributeTypes() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();
        ProgramClass programClass = new ProgramClass();

        // Test with various attribute types
        Attribute mockAttribute1 = mock(Attribute.class);
        Attribute mockAttribute2 = mock(SourceFileAttribute.class);
        Attribute mockAttribute3 = mock(InnerClassesAttribute.class);

        // Act & Assert - Should not throw any exception for any attribute type
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(programClass, mockAttribute1);
            updater.visitAnyAttribute(programClass, mockAttribute2);
            updater.visitAnyAttribute(programClass, mockAttribute3);
        }, "visitAnyAttribute should handle different attribute types without exception");
    }

    /**
     * Tests visitAnyAttribute with an updater that has a visitor.
     */
    @Test
    public void testVisitAnyAttributeWithVisitor() {
        // Arrange
        MemberVisitor mockVisitor = mock(MemberVisitor.class);
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater(mockVisitor);
        ProgramClass programClass = new ProgramClass();
        Attribute attribute = mock(Attribute.class);

        // Act
        updater.visitAnyAttribute(programClass, attribute);

        // Assert - The visitor should not be called since visitAnyAttribute does nothing
        verifyNoInteractions(mockVisitor);
    }

    /**
     * Tests visitAnyAttribute with a null visitor.
     */
    @Test
    public void testVisitAnyAttributeWithNullVisitor() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater(null);
        ProgramClass programClass = new ProgramClass();
        Attribute attribute = mock(Attribute.class);

        // Act & Assert - Should not throw exception even with null visitor
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(programClass, attribute);
        }, "visitAnyAttribute should not throw exception with null visitor");
    }

    /**
     * Tests that visitAnyAttribute can be called multiple times.
     */
    @Test
    public void testVisitAnyAttributeCalledMultipleTimes() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();
        ProgramClass programClass = new ProgramClass();
        Attribute attribute1 = mock(Attribute.class);
        Attribute attribute2 = mock(Attribute.class);
        Attribute attribute3 = mock(Attribute.class);

        // Act & Assert - Should handle multiple calls without issue
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(programClass, attribute1);
            updater.visitAnyAttribute(programClass, attribute2);
            updater.visitAnyAttribute(programClass, attribute3);
        }, "visitAnyAttribute should handle multiple calls without exception");
    }

    /**
     * Tests that visitAnyAttribute doesn't modify the class.
     */
    @Test
    public void testVisitAnyAttributeDoesNotModifyClass() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();
        ProgramClass programClass = new ProgramClass();
        programClass.u2thisClass = 42; // Set a value to check it's not modified
        Attribute attribute = mock(Attribute.class);

        // Act
        updater.visitAnyAttribute(programClass, attribute);

        // Assert - Class should remain unchanged
        assertEquals(42, programClass.u2thisClass, "visitAnyAttribute should not modify the class");
    }

    /**
     * Tests that visitAnyAttribute doesn't modify the attribute.
     */
    @Test
    public void testVisitAnyAttributeDoesNotModifyAttribute() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();
        ProgramClass programClass = new ProgramClass();
        Attribute mockAttribute = mock(Attribute.class);

        // Act
        updater.visitAnyAttribute(programClass, mockAttribute);

        // Assert - Attribute should not have any methods called on it
        verifyNoInteractions(mockAttribute);
    }

    /**
     * Tests visitAnyAttribute with the same updater instance multiple times with different classes.
     */
    @Test
    public void testVisitAnyAttributeWithDifferentClasses() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();
        Attribute attribute = mock(Attribute.class);

        // Act & Assert - Should handle different class types
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(programClass1, attribute);
            updater.visitAnyAttribute(programClass2, attribute);
            updater.visitAnyAttribute(libraryClass, attribute);
        }, "visitAnyAttribute should handle different class instances without exception");
    }

    /**
     * Tests that visitAnyAttribute is a true no-op by verifying no state changes.
     */
    @Test
    public void testVisitAnyAttributeIsNoOp() {
        // Arrange
        TrackingMemberVisitor trackingVisitor = new TrackingMemberVisitor();
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater(trackingVisitor);
        ProgramClass programClass = new ProgramClass();
        Attribute attribute = mock(Attribute.class);

        // Act
        updater.visitAnyAttribute(programClass, attribute);

        // Assert - Verify no visitor methods were called
        assertFalse(trackingVisitor.visitedProgramField, "visitAnyAttribute should not trigger field visits");
        assertFalse(trackingVisitor.visitedProgramMethod, "visitAnyAttribute should not trigger method visits");
        assertFalse(trackingVisitor.visitedLibraryField, "visitAnyAttribute should not trigger library field visits");
        assertFalse(trackingVisitor.visitedLibraryMethod, "visitAnyAttribute should not trigger library method visits");
    }

    /**
     * Tests visitAnyAttribute with various concrete attribute implementations.
     */
    @Test
    public void testVisitAnyAttributeWithConcreteAttributes() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();
        ProgramClass programClass = new ProgramClass();

        // Test with different concrete attribute types
        SourceFileAttribute sourceFileAttr = new SourceFileAttribute();
        InnerClassesAttribute innerClassesAttr = new InnerClassesAttribute();
        DeprecatedAttribute deprecatedAttr = new DeprecatedAttribute();

        // Act & Assert - Should handle all concrete attribute types
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(programClass, sourceFileAttr);
            updater.visitAnyAttribute(programClass, innerClassesAttr);
            updater.visitAnyAttribute(programClass, deprecatedAttr);
        }, "visitAnyAttribute should handle concrete attribute types without exception");
    }

    /**
     * Tests that visitAnyAttribute doesn't throw NullPointerException with null Clazz.
     * Note: This tests the actual behavior - the method doesn't validate null inputs.
     */
    @Test
    public void testVisitAnyAttributeWithNullClazz() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();
        Attribute attribute = mock(Attribute.class);

        // Act & Assert - Empty method doesn't access parameters, so no NPE
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(null, attribute);
        }, "visitAnyAttribute should not throw exception with null Clazz since it's a no-op");
    }

    /**
     * Tests that visitAnyAttribute doesn't throw NullPointerException with null Attribute.
     * Note: This tests the actual behavior - the method doesn't validate null inputs.
     */
    @Test
    public void testVisitAnyAttributeWithNullAttribute() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();
        ProgramClass programClass = new ProgramClass();

        // Act & Assert - Empty method doesn't access parameters, so no NPE
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(programClass, null);
        }, "visitAnyAttribute should not throw exception with null Attribute since it's a no-op");
    }

    /**
     * Tests that visitAnyAttribute doesn't throw exception with both null parameters.
     */
    @Test
    public void testVisitAnyAttributeWithBothParametersNull() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();

        // Act & Assert - Empty method doesn't access parameters, so no NPE
        assertDoesNotThrow(() -> {
            updater.visitAnyAttribute(null, null);
        }, "visitAnyAttribute should not throw exception with null parameters since it's a no-op");
    }

    /**
     * Tests visitAnyAttribute with multiple updater instances.
     */
    @Test
    public void testVisitAnyAttributeWithMultipleUpdaters() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater1 = new UnusedParameterOptimizationInfoUpdater();
        UnusedParameterOptimizationInfoUpdater updater2 = new UnusedParameterOptimizationInfoUpdater(mock(MemberVisitor.class));
        UnusedParameterOptimizationInfoUpdater updater3 = new UnusedParameterOptimizationInfoUpdater(null);

        ProgramClass programClass = new ProgramClass();
        Attribute attribute = mock(Attribute.class);

        // Act & Assert - All updaters should handle the call without exception
        assertDoesNotThrow(() -> {
            updater1.visitAnyAttribute(programClass, attribute);
            updater2.visitAnyAttribute(programClass, attribute);
            updater3.visitAnyAttribute(programClass, attribute);
        }, "All updater instances should handle visitAnyAttribute without exception");
    }

    /**
     * Tests that visitAnyAttribute can be called in succession without side effects.
     */
    @Test
    public void testVisitAnyAttributeHasNoSideEffects() {
        // Arrange
        UnusedParameterOptimizationInfoUpdater updater = new UnusedParameterOptimizationInfoUpdater();
        ProgramClass programClass = new ProgramClass();
        Attribute attribute = mock(Attribute.class);

        // Act - Call multiple times
        updater.visitAnyAttribute(programClass, attribute);
        updater.visitAnyAttribute(programClass, attribute);
        updater.visitAnyAttribute(programClass, attribute);

        // Assert - Verify attribute was never accessed (confirming no-op behavior)
        verifyNoInteractions(attribute);
    }

    /**
     * MemberVisitor implementation that tracks whether it was visited.
     */
    private static class TrackingMemberVisitor implements MemberVisitor {
        boolean visitedProgramField = false;
        boolean visitedProgramMethod = false;
        boolean visitedLibraryField = false;
        boolean visitedLibraryMethod = false;

        @Override
        public void visitProgramField(ProgramClass programClass, ProgramField programField) {
            visitedProgramField = true;
        }

        @Override
        public void visitProgramMethod(ProgramClass programClass, ProgramMethod programMethod) {
            visitedProgramMethod = true;
        }

        @Override
        public void visitLibraryField(LibraryClass libraryClass, LibraryField libraryField) {
            visitedLibraryField = true;
        }

        @Override
        public void visitLibraryMethod(LibraryClass libraryClass, LibraryMethod libraryMethod) {
            visitedLibraryMethod = true;
        }
    }
}
