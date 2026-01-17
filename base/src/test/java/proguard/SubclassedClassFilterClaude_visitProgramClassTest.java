package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SubclassedClassFilter#visitProgramClass(ProgramClass)}.
 *
 * The visitProgramClass method filters program classes based on whether they have subclasses.
 * It only delegates to the wrapped ClassVisitor if programClass.subClassCount > 0.
 * These tests verify the filtering logic and delegation behavior.
 */
public class SubclassedClassFilterClaude_visitProgramClassTest {

    private SubclassedClassFilter subclassedClassFilter;
    private ClassVisitor classVisitor;
    private ProgramClass programClass;

    @BeforeEach
    public void setUp() {
        classVisitor = mock(ClassVisitor.class);
        subclassedClassFilter = new SubclassedClassFilter(classVisitor);
        programClass = new ProgramClass();
    }

    /**
     * Tests that visitProgramClass delegates to the visitor when subClassCount is greater than 0.
     * Verifies the basic filtering behavior for subclassed classes.
     */
    @Test
    public void testVisitProgramClass_withSubclasses_delegatesToVisitor() {
        // Arrange
        programClass.subClassCount = 1;

        // Act
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - verify visitor was called
        verify(classVisitor, times(1)).visitProgramClass(programClass);
    }

    /**
     * Tests that visitProgramClass does NOT delegate when subClassCount is 0.
     * Verifies that classes without subclasses are filtered out.
     */
    @Test
    public void testVisitProgramClass_withNoSubclasses_doesNotDelegateToVisitor() {
        // Arrange
        programClass.subClassCount = 0;

        // Act
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - verify visitor was NOT called
        verify(classVisitor, never()).visitProgramClass(any(ProgramClass.class));
    }

    /**
     * Tests that visitProgramClass delegates when subClassCount is exactly 1.
     * Verifies the boundary condition.
     */
    @Test
    public void testVisitProgramClass_withSubClassCountOne_delegatesToVisitor() {
        // Arrange
        programClass.subClassCount = 1;

        // Act
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - verify visitor was called with the exact program class
        verify(classVisitor).visitProgramClass(eq(programClass));
    }

    /**
     * Tests that visitProgramClass delegates when subClassCount is a large number.
     * Verifies the behavior with many subclasses.
     */
    @Test
    public void testVisitProgramClass_withManySubclasses_delegatesToVisitor() {
        // Arrange
        programClass.subClassCount = 100;

        // Act
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - verify visitor was called
        verify(classVisitor).visitProgramClass(programClass);
    }

    /**
     * Tests that visitProgramClass does not delegate when subClassCount is negative.
     * Verifies behavior with invalid/unexpected negative values.
     */
    @Test
    public void testVisitProgramClass_withNegativeSubClassCount_doesNotDelegate() {
        // Arrange
        programClass.subClassCount = -1;

        // Act
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - verify visitor was NOT called
        verify(classVisitor, never()).visitProgramClass(any(ProgramClass.class));
    }

    /**
     * Tests that visitProgramClass can be called multiple times with subclassed classes.
     * Verifies each call delegates to the visitor.
     */
    @Test
    public void testVisitProgramClass_calledMultipleTimes_withSubclasses_delegatesEachTime() {
        // Arrange
        programClass.subClassCount = 1;

        // Act - call three times
        subclassedClassFilter.visitProgramClass(programClass);
        subclassedClassFilter.visitProgramClass(programClass);
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - verify visitor was called three times
        verify(classVisitor, times(3)).visitProgramClass(programClass);
    }

    /**
     * Tests that visitProgramClass can be called multiple times with non-subclassed classes.
     * Verifies the visitor is never called.
     */
    @Test
    public void testVisitProgramClass_calledMultipleTimes_withoutSubclasses_neverDelegates() {
        // Arrange
        programClass.subClassCount = 0;

        // Act - call three times
        subclassedClassFilter.visitProgramClass(programClass);
        subclassedClassFilter.visitProgramClass(programClass);
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - verify visitor was never called
        verify(classVisitor, never()).visitProgramClass(any(ProgramClass.class));
    }

    /**
     * Tests visitProgramClass with different ProgramClass instances with varying subClassCounts.
     * Verifies that each instance is handled according to its subClassCount.
     */
    @Test
    public void testVisitProgramClass_withDifferentClasses_filtersCorrectly() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        class1.subClassCount = 0; // No subclasses - should not delegate

        ProgramClass class2 = new ProgramClass();
        class2.subClassCount = 1; // Has subclasses - should delegate

        ProgramClass class3 = new ProgramClass();
        class3.subClassCount = 5; // Has subclasses - should delegate

        // Act
        subclassedClassFilter.visitProgramClass(class1);
        subclassedClassFilter.visitProgramClass(class2);
        subclassedClassFilter.visitProgramClass(class3);

        // Assert - only class2 and class3 should be delegated
        verify(classVisitor, never()).visitProgramClass(class1);
        verify(classVisitor, times(1)).visitProgramClass(class2);
        verify(classVisitor, times(1)).visitProgramClass(class3);
    }

    /**
     * Tests that visitProgramClass throws NullPointerException when ProgramClass is null.
     */
    @Test
    public void testVisitProgramClass_withNullClass_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> subclassedClassFilter.visitProgramClass(null));
    }

    /**
     * Tests that visitProgramClass with null ClassVisitor throws NullPointerException
     * when trying to delegate for a subclassed class.
     */
    @Test
    public void testVisitProgramClass_withNullVisitorAndSubclasses_throwsNullPointerException() {
        // Arrange
        SubclassedClassFilter filterWithNullVisitor = new SubclassedClassFilter(null);
        programClass.subClassCount = 1;

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> filterWithNullVisitor.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass with null ClassVisitor does NOT throw
     * when the class has no subclasses (no delegation occurs).
     */
    @Test
    public void testVisitProgramClass_withNullVisitorAndNoSubclasses_doesNotThrow() {
        // Arrange
        SubclassedClassFilter filterWithNullVisitor = new SubclassedClassFilter(null);
        programClass.subClassCount = 0;

        // Act & Assert
        assertDoesNotThrow(() -> filterWithNullVisitor.visitProgramClass(programClass));
    }

    /**
     * Tests visitProgramClass does not throw exception with valid inputs and subclasses.
     */
    @Test
    public void testVisitProgramClass_withValidInputsAndSubclasses_doesNotThrow() {
        // Arrange
        programClass.subClassCount = 1;

        // Act & Assert
        assertDoesNotThrow(() -> subclassedClassFilter.visitProgramClass(programClass));
    }

    /**
     * Tests visitProgramClass does not throw exception with valid inputs and no subclasses.
     */
    @Test
    public void testVisitProgramClass_withValidInputsAndNoSubclasses_doesNotThrow() {
        // Arrange
        programClass.subClassCount = 0;

        // Act & Assert
        assertDoesNotThrow(() -> subclassedClassFilter.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass on multiple filter instances behaves independently.
     */
    @Test
    public void testVisitProgramClass_multipleFilterInstances_behaviorIsIndependent() {
        // Arrange
        ClassVisitor visitor1 = mock(ClassVisitor.class);
        ClassVisitor visitor2 = mock(ClassVisitor.class);
        SubclassedClassFilter filter1 = new SubclassedClassFilter(visitor1);
        SubclassedClassFilter filter2 = new SubclassedClassFilter(visitor2);

        programClass.subClassCount = 1;

        // Act
        filter1.visitProgramClass(programClass);
        filter2.visitProgramClass(programClass);

        // Assert - each filter should call its own visitor
        verify(visitor1, times(1)).visitProgramClass(programClass);
        verify(visitor2, times(1)).visitProgramClass(programClass);
    }

    /**
     * Tests the boundary condition where subClassCount transitions from 0 to 1.
     * Verifies delegation starts at exactly 1.
     */
    @Test
    public void testVisitProgramClass_boundaryCondition_zeroToOne() {
        // Arrange - first call with 0 subclasses
        programClass.subClassCount = 0;

        // Act
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - should not delegate
        verify(classVisitor, never()).visitProgramClass(any(ProgramClass.class));

        // Arrange - change to 1 subclass
        programClass.subClassCount = 1;

        // Act
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - should now delegate
        verify(classVisitor, times(1)).visitProgramClass(programClass);
    }

    /**
     * Tests visitProgramClass with different subClassCount values to verify the threshold.
     */
    @Test
    public void testVisitProgramClass_variousSubClassCounts_correctThreshold() {
        // Test subClassCount = 0 (should not delegate)
        programClass.subClassCount = 0;
        subclassedClassFilter.visitProgramClass(programClass);
        verify(classVisitor, never()).visitProgramClass(any(ProgramClass.class));

        // Reset mock
        reset(classVisitor);

        // Test subClassCount = 1 (should delegate)
        programClass.subClassCount = 1;
        subclassedClassFilter.visitProgramClass(programClass);
        verify(classVisitor, times(1)).visitProgramClass(programClass);

        // Reset mock
        reset(classVisitor);

        // Test subClassCount = 2 (should delegate)
        programClass.subClassCount = 2;
        subclassedClassFilter.visitProgramClass(programClass);
        verify(classVisitor, times(1)).visitProgramClass(programClass);

        // Reset mock
        reset(classVisitor);

        // Test subClassCount = 10 (should delegate)
        programClass.subClassCount = 10;
        subclassedClassFilter.visitProgramClass(programClass);
        verify(classVisitor, times(1)).visitProgramClass(programClass);
    }

    /**
     * Tests that visitProgramClass passes the exact same ProgramClass instance to the visitor.
     */
    @Test
    public void testVisitProgramClass_passesExactSameInstance() {
        // Arrange
        ProgramClass specificClass = new ProgramClass();
        specificClass.subClassCount = 1;

        // Act
        subclassedClassFilter.visitProgramClass(specificClass);

        // Assert - verify the exact same instance was passed
        verify(classVisitor).visitProgramClass(same(specificClass));
    }

    /**
     * Tests visitProgramClass is stateless across invocations.
     * The filter should not maintain state between calls.
     */
    @Test
    public void testVisitProgramClass_statelessBehavior() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        class1.subClassCount = 1;

        ProgramClass class2 = new ProgramClass();
        class2.subClassCount = 0;

        // Act - alternate between subclassed and non-subclassed
        subclassedClassFilter.visitProgramClass(class1); // Should delegate
        subclassedClassFilter.visitProgramClass(class2); // Should not delegate
        subclassedClassFilter.visitProgramClass(class1); // Should delegate again

        // Assert - verify correct number of calls
        verify(classVisitor, times(2)).visitProgramClass(class1);
        verify(classVisitor, never()).visitProgramClass(class2);
    }

    /**
     * Tests that visitProgramClass does not modify the ProgramClass.
     */
    @Test
    public void testVisitProgramClass_doesNotModifyProgramClass() {
        // Arrange
        programClass.subClassCount = 5;
        int originalCount = programClass.subClassCount;

        // Act
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - subClassCount should remain unchanged
        assertEquals(originalCount, programClass.subClassCount,
                "visitProgramClass should not modify the subClassCount");
    }

    /**
     * Tests visitProgramClass with maximum integer value for subClassCount.
     */
    @Test
    public void testVisitProgramClass_withMaxSubClassCount_delegatesToVisitor() {
        // Arrange
        programClass.subClassCount = Integer.MAX_VALUE;

        // Act
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - should delegate
        verify(classVisitor).visitProgramClass(programClass);
    }

    /**
     * Tests visitProgramClass with minimum integer value for subClassCount.
     */
    @Test
    public void testVisitProgramClass_withMinSubClassCount_doesNotDelegate() {
        // Arrange
        programClass.subClassCount = Integer.MIN_VALUE;

        // Act
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - should not delegate (negative value)
        verify(classVisitor, never()).visitProgramClass(any(ProgramClass.class));
    }

    /**
     * Tests that only visitProgramClass is called on the visitor, not other methods.
     */
    @Test
    public void testVisitProgramClass_onlyCallsVisitProgramClassMethod() {
        // Arrange
        programClass.subClassCount = 1;

        // Act
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - verify only visitProgramClass was called, not other methods
        verify(classVisitor, times(1)).visitProgramClass(programClass);
        verify(classVisitor, never()).visitLibraryClass(any());
        verify(classVisitor, never()).visitAnyClass(any());
    }

    /**
     * Tests visitProgramClass with rapid sequential calls alternating between
     * classes with and without subclasses.
     */
    @Test
    public void testVisitProgramClass_rapidAlternatingCalls_correctFiltering() {
        // Arrange
        ProgramClass withSubclasses = new ProgramClass();
        withSubclasses.subClassCount = 1;

        ProgramClass withoutSubclasses = new ProgramClass();
        withoutSubclasses.subClassCount = 0;

        // Act - alternate rapidly
        for (int i = 0; i < 5; i++) {
            subclassedClassFilter.visitProgramClass(withSubclasses);
            subclassedClassFilter.visitProgramClass(withoutSubclasses);
        }

        // Assert - only withSubclasses should have been delegated (5 times)
        verify(classVisitor, times(5)).visitProgramClass(withSubclasses);
        verify(classVisitor, never()).visitProgramClass(withoutSubclasses);
    }

    /**
     * Tests that the filter works correctly after being used with other visitor methods.
     */
    @Test
    public void testVisitProgramClass_afterUsingOtherVisitorMethods_stillWorksCorrectly() {
        // Arrange
        proguard.classfile.LibraryClass libraryClass = new proguard.classfile.LibraryClass();
        libraryClass.subClassCount = 1;

        programClass.subClassCount = 1;

        // Act - call visitLibraryClass first, then visitProgramClass
        subclassedClassFilter.visitLibraryClass(libraryClass);
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - both should have been delegated
        verify(classVisitor, times(1)).visitLibraryClass(libraryClass);
        verify(classVisitor, times(1)).visitProgramClass(programClass);
    }

    /**
     * Tests visitProgramClass with a ProgramClass that has all default values.
     */
    @Test
    public void testVisitProgramClass_withDefaultProgramClass_doesNotDelegate() {
        // Arrange - ProgramClass with default values (subClassCount defaults to 0)
        ProgramClass defaultClass = new ProgramClass();

        // Act
        subclassedClassFilter.visitProgramClass(defaultClass);

        // Assert - should not delegate since subClassCount is 0 by default
        verify(classVisitor, never()).visitProgramClass(any(ProgramClass.class));
    }

    /**
     * Tests that the visitor is called with the correct method signature.
     */
    @Test
    public void testVisitProgramClass_callsCorrectVisitorMethod() {
        // Arrange
        programClass.subClassCount = 1;

        // Act
        subclassedClassFilter.visitProgramClass(programClass);

        // Assert - verify visitProgramClass was called (not visitAnyClass or visitLibraryClass)
        verify(classVisitor, times(1)).visitProgramClass(any(ProgramClass.class));
        verify(classVisitor, times(0)).visitAnyClass(any());
        verify(classVisitor, times(0)).visitLibraryClass(any());
    }
}
