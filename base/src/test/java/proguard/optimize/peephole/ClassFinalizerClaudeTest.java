package proguard.optimize.peephole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.AccessConstants;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassVisitor;
import proguard.optimize.KeepMarker;
import proguard.optimize.info.ProgramClassOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ClassFinalizer}.
 *
 * Tests all methods in ClassFinalizer:
 * - {@code <init>()V} - Constructor with no arguments
 * - {@code <init>(ClassVisitor)V} - Constructor with extraClassVisitor
 * - {@code visitAnyClass(Clazz)V} - Visitor method for any class
 * - {@code visitProgramClass(ProgramClass)V} - Visitor method for program classes
 *
 * The ClassFinalizer makes program classes final if possible. A class can be made final if:
 * 1. It is not already final, interface, or abstract
 * 2. It is not marked as kept by KeepMarker
 * 3. It has no subclasses (subClassCount == 0)
 *
 * These tests verify all branches and conditions for good coverage.
 */
public class ClassFinalizerClaudeTest {

    private ClassFinalizer finalizer;
    private ClassVisitor mockExtraClassVisitor;

    @BeforeEach
    public void setUp() {
        mockExtraClassVisitor = mock(ClassVisitor.class);
    }

    // ========================================
    // Constructor Tests
    // ========================================

    /**
     * Tests the no-argument constructor.
     * This constructor creates a ClassFinalizer with no extra visitor.
     */
    @Test
    public void testConstructor_noArgument() {
        // Act
        ClassFinalizer finalizer = new ClassFinalizer();

        // Assert - should not throw, and should be usable
        assertNotNull(finalizer, "ClassFinalizer should be created successfully");

        // Test that it works by finalizing a class
        ProgramClass programClass = createFinalizableClass();
        finalizer.visitProgramClass(programClass);

        assertTrue(isFinal(programClass),
                "ClassFinalizer created with no-arg constructor should finalize classes");
    }

    /**
     * Tests the constructor with a null extraClassVisitor.
     * This should behave the same as the no-argument constructor.
     */
    @Test
    public void testConstructor_withNullVisitor() {
        // Act
        ClassFinalizer finalizer = new ClassFinalizer(null);

        // Assert
        assertNotNull(finalizer, "ClassFinalizer should be created with null visitor");

        // Test that it works
        ProgramClass programClass = createFinalizableClass();
        finalizer.visitProgramClass(programClass);

        assertTrue(isFinal(programClass),
                "ClassFinalizer with null visitor should finalize classes");
    }

    /**
     * Tests the constructor with a valid extraClassVisitor.
     * The extraClassVisitor should be called for finalized classes.
     */
    @Test
    public void testConstructor_withValidVisitor() {
        // Act
        ClassFinalizer finalizer = new ClassFinalizer(mockExtraClassVisitor);

        // Assert
        assertNotNull(finalizer, "ClassFinalizer should be created with visitor");

        // Verify the visitor is used when a class is finalized
        ProgramClass programClass = createFinalizableClass();
        finalizer.visitProgramClass(programClass);

        verify(mockExtraClassVisitor).visitProgramClass(programClass);
    }

    // ========================================
    // visitAnyClass Tests
    // ========================================

    /**
     * Tests visitAnyClass with a ProgramClass.
     * This method should do nothing (no-op).
     */
    @Test
    public void testVisitAnyClass_withProgramClass() {
        // Arrange
        finalizer = new ClassFinalizer();
        ProgramClass programClass = createFinalizableClass();
        int originalAccessFlags = programClass.u2accessFlags;

        // Act
        finalizer.visitAnyClass(programClass);

        // Assert - should be unchanged
        assertEquals(originalAccessFlags, programClass.u2accessFlags,
                "visitAnyClass should not modify access flags");
        assertFalse(isFinal(programClass),
                "visitAnyClass should not make class final");
    }

    /**
     * Tests visitAnyClass with a LibraryClass.
     * This method should do nothing (no-op).
     */
    @Test
    public void testVisitAnyClass_withLibraryClass() {
        // Arrange
        finalizer = new ClassFinalizer();
        LibraryClass libraryClass = new LibraryClass();
        libraryClass.u2accessFlags = 0;
        int originalAccessFlags = libraryClass.u2accessFlags;

        // Act
        assertDoesNotThrow(() -> finalizer.visitAnyClass(libraryClass),
                "visitAnyClass should not throw with LibraryClass");

        // Assert
        assertEquals(originalAccessFlags, libraryClass.u2accessFlags,
                "visitAnyClass should not modify library class");
    }

    // ========================================
    // visitProgramClass Tests - Successful Finalization
    // ========================================

    /**
     * Tests visitProgramClass with a class that can be finalized.
     * The class should be made final.
     */
    @Test
    public void testVisitProgramClass_finalizableClass_makesFinal() {
        // Arrange
        finalizer = new ClassFinalizer();
        ProgramClass programClass = createFinalizableClass();

        // Verify preconditions
        assertFalse(isFinal(programClass), "Class should not be final initially");
        assertFalse(isInterface(programClass), "Class should not be interface");
        assertFalse(isAbstract(programClass), "Class should not be abstract");
        assertEquals(0, programClass.subClassCount, "Class should have no subclasses");

        // Act
        finalizer.visitProgramClass(programClass);

        // Assert
        assertTrue(isFinal(programClass), "Class should be made final");
    }

    /**
     * Tests that extraClassVisitor is called when a class is finalized.
     */
    @Test
    public void testVisitProgramClass_finalizableClass_callsExtraVisitor() {
        // Arrange
        finalizer = new ClassFinalizer(mockExtraClassVisitor);
        ProgramClass programClass = createFinalizableClass();

        // Act
        finalizer.visitProgramClass(programClass);

        // Assert
        assertTrue(isFinal(programClass), "Class should be made final");
        verify(mockExtraClassVisitor).visitProgramClass(programClass);
    }

    /**
     * Tests that extraClassVisitor is called exactly once per finalization.
     */
    @Test
    public void testVisitProgramClass_extraVisitorCalledOnce() {
        // Arrange
        finalizer = new ClassFinalizer(mockExtraClassVisitor);
        ProgramClass programClass = createFinalizableClass();

        // Act
        finalizer.visitProgramClass(programClass);

        // Assert
        verify(mockExtraClassVisitor, times(1)).visitProgramClass(programClass);
    }

    // ========================================
    // visitProgramClass Tests - Already Final
    // ========================================

    /**
     * Tests visitProgramClass with a class that is already final.
     * The class should remain final but extraClassVisitor should not be called.
     */
    @Test
    public void testVisitProgramClass_alreadyFinal_noChange() {
        // Arrange
        finalizer = new ClassFinalizer(mockExtraClassVisitor);
        ProgramClass programClass = createFinalizableClass();
        programClass.u2accessFlags |= AccessConstants.FINAL;

        // Verify precondition
        assertTrue(isFinal(programClass), "Class should be final initially");

        // Act
        finalizer.visitProgramClass(programClass);

        // Assert
        assertTrue(isFinal(programClass), "Class should remain final");
        verify(mockExtraClassVisitor, never()).visitProgramClass(any());
    }

    // ========================================
    // visitProgramClass Tests - Interface
    // ========================================

    /**
     * Tests visitProgramClass with an interface.
     * Interfaces should not be made final.
     */
    @Test
    public void testVisitProgramClass_interface_notFinalized() {
        // Arrange
        finalizer = new ClassFinalizer(mockExtraClassVisitor);
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.INTERFACE;
        programClass.subClassCount = 0;

        // Verify precondition
        assertTrue(isInterface(programClass), "Class should be an interface");

        // Act
        finalizer.visitProgramClass(programClass);

        // Assert
        assertFalse(isFinal(programClass), "Interface should not be made final");
        verify(mockExtraClassVisitor, never()).visitProgramClass(any());
    }

    // ========================================
    // visitProgramClass Tests - Abstract
    // ========================================

    /**
     * Tests visitProgramClass with an abstract class.
     * Abstract classes should not be made final.
     */
    @Test
    public void testVisitProgramClass_abstract_notFinalized() {
        // Arrange
        finalizer = new ClassFinalizer(mockExtraClassVisitor);
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.ABSTRACT;
        programClass.subClassCount = 0;

        // Verify precondition
        assertTrue(isAbstract(programClass), "Class should be abstract");

        // Act
        finalizer.visitProgramClass(programClass);

        // Assert
        assertFalse(isFinal(programClass), "Abstract class should not be made final");
        verify(mockExtraClassVisitor, never()).visitProgramClass(any());
    }

    // ========================================
    // visitProgramClass Tests - Kept Classes
    // ========================================

    /**
     * Tests visitProgramClass with a class marked as kept.
     * Kept classes should not be made final.
     */
    @Test
    public void testVisitProgramClass_keptClass_notFinalized() {
        // Arrange
        finalizer = new ClassFinalizer(mockExtraClassVisitor);
        ProgramClass programClass = createFinalizableClass();

        // Mark the class as kept
        KeepMarker keepMarker = new KeepMarker();
        keepMarker.visitAnyClass(programClass);

        // Verify precondition
        assertTrue(KeepMarker.isKept(programClass), "Class should be marked as kept");

        // Act
        finalizer.visitProgramClass(programClass);

        // Assert
        assertFalse(isFinal(programClass), "Kept class should not be made final");
        verify(mockExtraClassVisitor, never()).visitProgramClass(any());
    }

    // ========================================
    // visitProgramClass Tests - Has Subclasses
    // ========================================

    /**
     * Tests visitProgramClass with a class that has subclasses.
     * Classes with subclasses should not be made final.
     */
    @Test
    public void testVisitProgramClass_hasOneSubclass_notFinalized() {
        // Arrange
        finalizer = new ClassFinalizer(mockExtraClassVisitor);
        ProgramClass programClass = createFinalizableClass();
        programClass.subClassCount = 1;

        // Act
        finalizer.visitProgramClass(programClass);

        // Assert
        assertFalse(isFinal(programClass),
                "Class with subclasses should not be made final");
        verify(mockExtraClassVisitor, never()).visitProgramClass(any());
    }

    /**
     * Tests visitProgramClass with a class that has multiple subclasses.
     * Classes with subclasses should not be made final.
     */
    @Test
    public void testVisitProgramClass_hasMultipleSubclasses_notFinalized() {
        // Arrange
        finalizer = new ClassFinalizer(mockExtraClassVisitor);
        ProgramClass programClass = createFinalizableClass();
        programClass.subClassCount = 5;

        // Act
        finalizer.visitProgramClass(programClass);

        // Assert
        assertFalse(isFinal(programClass),
                "Class with multiple subclasses should not be made final");
        verify(mockExtraClassVisitor, never()).visitProgramClass(any());
    }

    // ========================================
    // visitProgramClass Tests - Multiple Conditions
    // ========================================

    /**
     * Tests visitProgramClass with an abstract interface (both flags set).
     * Should not be made final.
     */
    @Test
    public void testVisitProgramClass_abstractInterface_notFinalized() {
        // Arrange
        finalizer = new ClassFinalizer(mockExtraClassVisitor);
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.ABSTRACT | AccessConstants.INTERFACE;
        programClass.subClassCount = 0;

        // Act
        finalizer.visitProgramClass(programClass);

        // Assert
        assertFalse(isFinal(programClass),
                "Abstract interface should not be made final");
        verify(mockExtraClassVisitor, never()).visitProgramClass(any());
    }

    /**
     * Tests visitProgramClass with a final, abstract class (unusual but possible).
     * Should remain final but visitor should not be called since condition fails.
     */
    @Test
    public void testVisitProgramClass_finalAndAbstract_noVisitorCall() {
        // Arrange
        finalizer = new ClassFinalizer(mockExtraClassVisitor);
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = AccessConstants.FINAL | AccessConstants.ABSTRACT;
        programClass.subClassCount = 0;

        // Act
        finalizer.visitProgramClass(programClass);

        // Assert
        assertTrue(isFinal(programClass), "Class should remain final");
        verify(mockExtraClassVisitor, never()).visitProgramClass(any());
    }

    /**
     * Tests visitProgramClass with a kept class that also has subclasses.
     * Should not be made final (multiple reasons).
     */
    @Test
    public void testVisitProgramClass_keptWithSubclasses_notFinalized() {
        // Arrange
        finalizer = new ClassFinalizer(mockExtraClassVisitor);
        ProgramClass programClass = createFinalizableClass();
        programClass.subClassCount = 2;

        // Mark as kept
        KeepMarker keepMarker = new KeepMarker();
        keepMarker.visitAnyClass(programClass);

        // Act
        finalizer.visitProgramClass(programClass);

        // Assert
        assertFalse(isFinal(programClass),
                "Kept class with subclasses should not be made final");
        verify(mockExtraClassVisitor, never()).visitProgramClass(any());
    }

    // ========================================
    // visitProgramClass Tests - Edge Cases
    // ========================================

    /**
     * Tests that the same class can be visited multiple times.
     * After first visit it becomes final, subsequent visits should not call visitor.
     */
    @Test
    public void testVisitProgramClass_visitedTwice_visitorCalledOnce() {
        // Arrange
        finalizer = new ClassFinalizer(mockExtraClassVisitor);
        ProgramClass programClass = createFinalizableClass();

        // Act
        finalizer.visitProgramClass(programClass);
        finalizer.visitProgramClass(programClass); // Visit again

        // Assert
        assertTrue(isFinal(programClass), "Class should be final");
        // Visitor should only be called once (first time when it was finalized)
        verify(mockExtraClassVisitor, times(1)).visitProgramClass(programClass);
    }

    /**
     * Tests visitProgramClass with multiple different finalizable classes.
     * All should be made final and visitor should be called for each.
     */
    @Test
    public void testVisitProgramClass_multipleClasses_allFinalized() {
        // Arrange
        finalizer = new ClassFinalizer(mockExtraClassVisitor);
        ProgramClass class1 = createFinalizableClass();
        ProgramClass class2 = createFinalizableClass();
        ProgramClass class3 = createFinalizableClass();

        // Act
        finalizer.visitProgramClass(class1);
        finalizer.visitProgramClass(class2);
        finalizer.visitProgramClass(class3);

        // Assert
        assertTrue(isFinal(class1), "Class1 should be final");
        assertTrue(isFinal(class2), "Class2 should be final");
        assertTrue(isFinal(class3), "Class3 should be final");
        verify(mockExtraClassVisitor, times(3)).visitProgramClass(any());
    }

    /**
     * Tests visitProgramClass without extraClassVisitor (null).
     * Should finalize class without errors.
     */
    @Test
    public void testVisitProgramClass_nullExtraVisitor_finalizesSuccessfully() {
        // Arrange
        finalizer = new ClassFinalizer(null);
        ProgramClass programClass = createFinalizableClass();

        // Act
        assertDoesNotThrow(() -> finalizer.visitProgramClass(programClass),
                "Should not throw with null extraClassVisitor");

        // Assert
        assertTrue(isFinal(programClass), "Class should be made final");
    }

    /**
     * Tests visitProgramClass with subClassCount at boundary (0 vs 1).
     * Only 0 should allow finalization.
     */
    @Test
    public void testVisitProgramClass_subClassCountBoundary() {
        // Arrange
        finalizer = new ClassFinalizer();

        ProgramClass classWithZeroSubclasses = createFinalizableClass();
        classWithZeroSubclasses.subClassCount = 0;

        ProgramClass classWithOneSubclass = createFinalizableClass();
        classWithOneSubclass.subClassCount = 1;

        // Act
        finalizer.visitProgramClass(classWithZeroSubclasses);
        finalizer.visitProgramClass(classWithOneSubclass);

        // Assert
        assertTrue(isFinal(classWithZeroSubclasses),
                "Class with 0 subclasses should be final");
        assertFalse(isFinal(classWithOneSubclass),
                "Class with 1 subclass should not be final");
    }

    /**
     * Tests that multiple ClassFinalizer instances work independently.
     */
    @Test
    public void testMultipleFinalizerInstances_workIndependently() {
        // Arrange
        ClassVisitor visitor1 = mock(ClassVisitor.class);
        ClassVisitor visitor2 = mock(ClassVisitor.class);

        ClassFinalizer finalizer1 = new ClassFinalizer(visitor1);
        ClassFinalizer finalizer2 = new ClassFinalizer(visitor2);

        ProgramClass class1 = createFinalizableClass();
        ProgramClass class2 = createFinalizableClass();

        // Act
        finalizer1.visitProgramClass(class1);
        finalizer2.visitProgramClass(class2);

        // Assert
        assertTrue(isFinal(class1), "Class1 should be final");
        assertTrue(isFinal(class2), "Class2 should be final");
        verify(visitor1).visitProgramClass(class1);
        verify(visitor2).visitProgramClass(class2);
        verify(visitor1, never()).visitProgramClass(class2);
        verify(visitor2, never()).visitProgramClass(class1);
    }

    /**
     * Tests that extraClassVisitor receives the correct class instance.
     */
    @Test
    public void testVisitProgramClass_extraVisitorReceivesCorrectClass() {
        // Arrange
        finalizer = new ClassFinalizer(mockExtraClassVisitor);
        ProgramClass programClass = createFinalizableClass();

        // Act
        finalizer.visitProgramClass(programClass);

        // Assert
        verify(mockExtraClassVisitor).visitProgramClass(eq(programClass));
    }

    // ========================================
    // Helper Methods
    // ========================================

    /**
     * Creates a ProgramClass that can be finalized.
     * Not final, not interface, not abstract, no subclasses, not kept.
     */
    private ProgramClass createFinalizableClass() {
        ProgramClass programClass = new ProgramClass();
        programClass.u2accessFlags = 0; // No flags set
        programClass.subClassCount = 0;
        // Don't mark as kept
        return programClass;
    }

    /**
     * Checks if a class has the FINAL flag set.
     */
    private boolean isFinal(ProgramClass programClass) {
        return (programClass.u2accessFlags & AccessConstants.FINAL) != 0;
    }

    /**
     * Checks if a class has the INTERFACE flag set.
     */
    private boolean isInterface(ProgramClass programClass) {
        return (programClass.u2accessFlags & AccessConstants.INTERFACE) != 0;
    }

    /**
     * Checks if a class has the ABSTRACT flag set.
     */
    private boolean isAbstract(ProgramClass programClass) {
        return (programClass.u2accessFlags & AccessConstants.ABSTRACT) != 0;
    }
}
