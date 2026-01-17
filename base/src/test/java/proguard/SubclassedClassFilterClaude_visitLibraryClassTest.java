package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.LibraryClass;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link SubclassedClassFilter#visitLibraryClass(LibraryClass)}.
 *
 * The visitLibraryClass method filters library classes based on whether they have subclasses.
 * It only delegates to the wrapped ClassVisitor if libraryClass.subClassCount > 0.
 * These tests verify the filtering logic and delegation behavior.
 */
public class SubclassedClassFilterClaude_visitLibraryClassTest {

    private SubclassedClassFilter subclassedClassFilter;
    private ClassVisitor classVisitor;
    private LibraryClass libraryClass;

    @BeforeEach
    public void setUp() {
        classVisitor = mock(ClassVisitor.class);
        subclassedClassFilter = new SubclassedClassFilter(classVisitor);
        libraryClass = new LibraryClass();
    }

    /**
     * Tests that visitLibraryClass delegates to the visitor when subClassCount is greater than 0.
     * Verifies the basic filtering behavior for subclassed classes.
     */
    @Test
    public void testVisitLibraryClass_withSubclasses_delegatesToVisitor() {
        // Arrange
        libraryClass.subClassCount = 1;

        // Act
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - verify visitor was called
        verify(classVisitor, times(1)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that visitLibraryClass does NOT delegate when subClassCount is 0.
     * Verifies that classes without subclasses are filtered out.
     */
    @Test
    public void testVisitLibraryClass_withNoSubclasses_doesNotDelegateToVisitor() {
        // Arrange
        libraryClass.subClassCount = 0;

        // Act
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - verify visitor was NOT called
        verify(classVisitor, never()).visitLibraryClass(any(LibraryClass.class));
    }

    /**
     * Tests that visitLibraryClass delegates when subClassCount is exactly 1.
     * Verifies the boundary condition.
     */
    @Test
    public void testVisitLibraryClass_withSubClassCountOne_delegatesToVisitor() {
        // Arrange
        libraryClass.subClassCount = 1;

        // Act
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - verify visitor was called with the exact library class
        verify(classVisitor).visitLibraryClass(eq(libraryClass));
    }

    /**
     * Tests that visitLibraryClass delegates when subClassCount is a large number.
     * Verifies the behavior with many subclasses.
     */
    @Test
    public void testVisitLibraryClass_withManySubclasses_delegatesToVisitor() {
        // Arrange
        libraryClass.subClassCount = 100;

        // Act
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - verify visitor was called
        verify(classVisitor).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that visitLibraryClass does not delegate when subClassCount is negative.
     * Verifies behavior with invalid/unexpected negative values.
     */
    @Test
    public void testVisitLibraryClass_withNegativeSubClassCount_doesNotDelegate() {
        // Arrange
        libraryClass.subClassCount = -1;

        // Act
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - verify visitor was NOT called
        verify(classVisitor, never()).visitLibraryClass(any(LibraryClass.class));
    }

    /**
     * Tests that visitLibraryClass can be called multiple times with subclassed classes.
     * Verifies each call delegates to the visitor.
     */
    @Test
    public void testVisitLibraryClass_calledMultipleTimes_withSubclasses_delegatesEachTime() {
        // Arrange
        libraryClass.subClassCount = 1;

        // Act - call three times
        subclassedClassFilter.visitLibraryClass(libraryClass);
        subclassedClassFilter.visitLibraryClass(libraryClass);
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - verify visitor was called three times
        verify(classVisitor, times(3)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that visitLibraryClass can be called multiple times with non-subclassed classes.
     * Verifies the visitor is never called.
     */
    @Test
    public void testVisitLibraryClass_calledMultipleTimes_withoutSubclasses_neverDelegates() {
        // Arrange
        libraryClass.subClassCount = 0;

        // Act - call three times
        subclassedClassFilter.visitLibraryClass(libraryClass);
        subclassedClassFilter.visitLibraryClass(libraryClass);
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - verify visitor was never called
        verify(classVisitor, never()).visitLibraryClass(any(LibraryClass.class));
    }

    /**
     * Tests visitLibraryClass with different LibraryClass instances with varying subClassCounts.
     * Verifies that each instance is handled according to its subClassCount.
     */
    @Test
    public void testVisitLibraryClass_withDifferentClasses_filtersCorrectly() {
        // Arrange
        LibraryClass class1 = new LibraryClass();
        class1.subClassCount = 0; // No subclasses - should not delegate

        LibraryClass class2 = new LibraryClass();
        class2.subClassCount = 1; // Has subclasses - should delegate

        LibraryClass class3 = new LibraryClass();
        class3.subClassCount = 5; // Has subclasses - should delegate

        // Act
        subclassedClassFilter.visitLibraryClass(class1);
        subclassedClassFilter.visitLibraryClass(class2);
        subclassedClassFilter.visitLibraryClass(class3);

        // Assert - only class2 and class3 should be delegated
        verify(classVisitor, never()).visitLibraryClass(class1);
        verify(classVisitor, times(1)).visitLibraryClass(class2);
        verify(classVisitor, times(1)).visitLibraryClass(class3);
    }

    /**
     * Tests that visitLibraryClass throws NullPointerException when LibraryClass is null.
     */
    @Test
    public void testVisitLibraryClass_withNullClass_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> subclassedClassFilter.visitLibraryClass(null));
    }

    /**
     * Tests that visitLibraryClass with null ClassVisitor throws NullPointerException
     * when trying to delegate for a subclassed class.
     */
    @Test
    public void testVisitLibraryClass_withNullVisitorAndSubclasses_throwsNullPointerException() {
        // Arrange
        SubclassedClassFilter filterWithNullVisitor = new SubclassedClassFilter(null);
        libraryClass.subClassCount = 1;

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> filterWithNullVisitor.visitLibraryClass(libraryClass));
    }

    /**
     * Tests that visitLibraryClass with null ClassVisitor does NOT throw
     * when the class has no subclasses (no delegation occurs).
     */
    @Test
    public void testVisitLibraryClass_withNullVisitorAndNoSubclasses_doesNotThrow() {
        // Arrange
        SubclassedClassFilter filterWithNullVisitor = new SubclassedClassFilter(null);
        libraryClass.subClassCount = 0;

        // Act & Assert
        assertDoesNotThrow(() -> filterWithNullVisitor.visitLibraryClass(libraryClass));
    }

    /**
     * Tests visitLibraryClass does not throw exception with valid inputs and subclasses.
     */
    @Test
    public void testVisitLibraryClass_withValidInputsAndSubclasses_doesNotThrow() {
        // Arrange
        libraryClass.subClassCount = 1;

        // Act & Assert
        assertDoesNotThrow(() -> subclassedClassFilter.visitLibraryClass(libraryClass));
    }

    /**
     * Tests visitLibraryClass does not throw exception with valid inputs and no subclasses.
     */
    @Test
    public void testVisitLibraryClass_withValidInputsAndNoSubclasses_doesNotThrow() {
        // Arrange
        libraryClass.subClassCount = 0;

        // Act & Assert
        assertDoesNotThrow(() -> subclassedClassFilter.visitLibraryClass(libraryClass));
    }

    /**
     * Tests that visitLibraryClass on multiple filter instances behaves independently.
     */
    @Test
    public void testVisitLibraryClass_multipleFilterInstances_behaviorIsIndependent() {
        // Arrange
        ClassVisitor visitor1 = mock(ClassVisitor.class);
        ClassVisitor visitor2 = mock(ClassVisitor.class);
        SubclassedClassFilter filter1 = new SubclassedClassFilter(visitor1);
        SubclassedClassFilter filter2 = new SubclassedClassFilter(visitor2);

        libraryClass.subClassCount = 1;

        // Act
        filter1.visitLibraryClass(libraryClass);
        filter2.visitLibraryClass(libraryClass);

        // Assert - each filter should call its own visitor
        verify(visitor1, times(1)).visitLibraryClass(libraryClass);
        verify(visitor2, times(1)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests the boundary condition where subClassCount transitions from 0 to 1.
     * Verifies delegation starts at exactly 1.
     */
    @Test
    public void testVisitLibraryClass_boundaryCondition_zeroToOne() {
        // Arrange - first call with 0 subclasses
        libraryClass.subClassCount = 0;

        // Act
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - should not delegate
        verify(classVisitor, never()).visitLibraryClass(any(LibraryClass.class));

        // Arrange - change to 1 subclass
        libraryClass.subClassCount = 1;

        // Act
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - should now delegate
        verify(classVisitor, times(1)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests visitLibraryClass with different subClassCount values to verify the threshold.
     */
    @Test
    public void testVisitLibraryClass_variousSubClassCounts_correctThreshold() {
        // Test subClassCount = 0 (should not delegate)
        libraryClass.subClassCount = 0;
        subclassedClassFilter.visitLibraryClass(libraryClass);
        verify(classVisitor, never()).visitLibraryClass(any(LibraryClass.class));

        // Reset mock
        reset(classVisitor);

        // Test subClassCount = 1 (should delegate)
        libraryClass.subClassCount = 1;
        subclassedClassFilter.visitLibraryClass(libraryClass);
        verify(classVisitor, times(1)).visitLibraryClass(libraryClass);

        // Reset mock
        reset(classVisitor);

        // Test subClassCount = 2 (should delegate)
        libraryClass.subClassCount = 2;
        subclassedClassFilter.visitLibraryClass(libraryClass);
        verify(classVisitor, times(1)).visitLibraryClass(libraryClass);

        // Reset mock
        reset(classVisitor);

        // Test subClassCount = 10 (should delegate)
        libraryClass.subClassCount = 10;
        subclassedClassFilter.visitLibraryClass(libraryClass);
        verify(classVisitor, times(1)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that visitLibraryClass passes the exact same LibraryClass instance to the visitor.
     */
    @Test
    public void testVisitLibraryClass_passesExactSameInstance() {
        // Arrange
        LibraryClass specificClass = new LibraryClass();
        specificClass.subClassCount = 1;

        // Act
        subclassedClassFilter.visitLibraryClass(specificClass);

        // Assert - verify the exact same instance was passed
        verify(classVisitor).visitLibraryClass(same(specificClass));
    }

    /**
     * Tests visitLibraryClass is stateless across invocations.
     * The filter should not maintain state between calls.
     */
    @Test
    public void testVisitLibraryClass_statelessBehavior() {
        // Arrange
        LibraryClass class1 = new LibraryClass();
        class1.subClassCount = 1;

        LibraryClass class2 = new LibraryClass();
        class2.subClassCount = 0;

        // Act - alternate between subclassed and non-subclassed
        subclassedClassFilter.visitLibraryClass(class1); // Should delegate
        subclassedClassFilter.visitLibraryClass(class2); // Should not delegate
        subclassedClassFilter.visitLibraryClass(class1); // Should delegate again

        // Assert - verify correct number of calls
        verify(classVisitor, times(2)).visitLibraryClass(class1);
        verify(classVisitor, never()).visitLibraryClass(class2);
    }

    /**
     * Tests that visitLibraryClass does not modify the LibraryClass.
     */
    @Test
    public void testVisitLibraryClass_doesNotModifyLibraryClass() {
        // Arrange
        libraryClass.subClassCount = 5;
        int originalCount = libraryClass.subClassCount;

        // Act
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - subClassCount should remain unchanged
        assertEquals(originalCount, libraryClass.subClassCount,
                "visitLibraryClass should not modify the subClassCount");
    }

    /**
     * Tests visitLibraryClass with maximum integer value for subClassCount.
     */
    @Test
    public void testVisitLibraryClass_withMaxSubClassCount_delegatesToVisitor() {
        // Arrange
        libraryClass.subClassCount = Integer.MAX_VALUE;

        // Act
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - should delegate
        verify(classVisitor).visitLibraryClass(libraryClass);
    }

    /**
     * Tests visitLibraryClass with minimum integer value for subClassCount.
     */
    @Test
    public void testVisitLibraryClass_withMinSubClassCount_doesNotDelegate() {
        // Arrange
        libraryClass.subClassCount = Integer.MIN_VALUE;

        // Act
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - should not delegate (negative value)
        verify(classVisitor, never()).visitLibraryClass(any(LibraryClass.class));
    }

    /**
     * Tests that only visitLibraryClass is called on the visitor, not other methods.
     */
    @Test
    public void testVisitLibraryClass_onlyCallsVisitLibraryClassMethod() {
        // Arrange
        libraryClass.subClassCount = 1;

        // Act
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - verify only visitLibraryClass was called, not other methods
        verify(classVisitor, times(1)).visitLibraryClass(libraryClass);
        verify(classVisitor, never()).visitProgramClass(any());
        verify(classVisitor, never()).visitAnyClass(any());
    }

    /**
     * Tests visitLibraryClass with rapid sequential calls alternating between
     * classes with and without subclasses.
     */
    @Test
    public void testVisitLibraryClass_rapidAlternatingCalls_correctFiltering() {
        // Arrange
        LibraryClass withSubclasses = new LibraryClass();
        withSubclasses.subClassCount = 1;

        LibraryClass withoutSubclasses = new LibraryClass();
        withoutSubclasses.subClassCount = 0;

        // Act - alternate rapidly
        for (int i = 0; i < 5; i++) {
            subclassedClassFilter.visitLibraryClass(withSubclasses);
            subclassedClassFilter.visitLibraryClass(withoutSubclasses);
        }

        // Assert - only withSubclasses should have been delegated (5 times)
        verify(classVisitor, times(5)).visitLibraryClass(withSubclasses);
        verify(classVisitor, never()).visitLibraryClass(withoutSubclasses);
    }

    /**
     * Tests that the filter works correctly after being used with other visitor methods.
     */
    @Test
    public void testVisitLibraryClass_afterUsingOtherVisitorMethods_stillWorksCorrectly() {
        // Arrange
        proguard.classfile.ProgramClass programClass = new proguard.classfile.ProgramClass();
        programClass.subClassCount = 1;

        libraryClass.subClassCount = 1;

        // Act - call visitProgramClass first, then visitLibraryClass
        subclassedClassFilter.visitProgramClass(programClass);
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - both should have been delegated
        verify(classVisitor, times(1)).visitProgramClass(programClass);
        verify(classVisitor, times(1)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests visitLibraryClass with a LibraryClass that has all default values.
     */
    @Test
    public void testVisitLibraryClass_withDefaultLibraryClass_doesNotDelegate() {
        // Arrange - LibraryClass with default values (subClassCount defaults to 0)
        LibraryClass defaultClass = new LibraryClass();

        // Act
        subclassedClassFilter.visitLibraryClass(defaultClass);

        // Assert - should not delegate since subClassCount is 0 by default
        verify(classVisitor, never()).visitLibraryClass(any(LibraryClass.class));
    }

    /**
     * Tests that the visitor is called with the correct method signature.
     */
    @Test
    public void testVisitLibraryClass_callsCorrectVisitorMethod() {
        // Arrange
        libraryClass.subClassCount = 1;

        // Act
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - verify visitLibraryClass was called (not visitAnyClass or visitProgramClass)
        verify(classVisitor, times(1)).visitLibraryClass(any(LibraryClass.class));
        verify(classVisitor, times(0)).visitAnyClass(any());
        verify(classVisitor, times(0)).visitProgramClass(any());
    }

    /**
     * Tests visitLibraryClass after visitProgramClass to verify the filter
     * handles both class types independently.
     */
    @Test
    public void testVisitLibraryClass_afterVisitProgramClass_independentBehavior() {
        // Arrange
        proguard.classfile.ProgramClass programClass = new proguard.classfile.ProgramClass();
        programClass.subClassCount = 0; // No subclasses - should not delegate

        libraryClass.subClassCount = 1; // Has subclasses - should delegate

        // Act
        subclassedClassFilter.visitProgramClass(programClass);
        subclassedClassFilter.visitLibraryClass(libraryClass);

        // Assert - only library class should be delegated
        verify(classVisitor, never()).visitProgramClass(programClass);
        verify(classVisitor, times(1)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that multiple different SubclassedClassFilter instances all
     * filter LibraryClass instances consistently.
     */
    @Test
    public void testVisitLibraryClass_multipleFiltersWithDifferentVisitors_consistentFiltering() {
        // Arrange
        ClassVisitor visitor1 = mock(ClassVisitor.class);
        ClassVisitor visitor2 = mock(ClassVisitor.class);
        ClassVisitor visitor3 = mock(ClassVisitor.class);

        SubclassedClassFilter filter1 = new SubclassedClassFilter(visitor1);
        SubclassedClassFilter filter2 = new SubclassedClassFilter(visitor2);
        SubclassedClassFilter filter3 = new SubclassedClassFilter(visitor3);

        LibraryClass withSubclasses = new LibraryClass();
        withSubclasses.subClassCount = 1;

        LibraryClass withoutSubclasses = new LibraryClass();
        withoutSubclasses.subClassCount = 0;

        // Act
        filter1.visitLibraryClass(withSubclasses);
        filter1.visitLibraryClass(withoutSubclasses);

        filter2.visitLibraryClass(withSubclasses);
        filter2.visitLibraryClass(withoutSubclasses);

        filter3.visitLibraryClass(withSubclasses);
        filter3.visitLibraryClass(withoutSubclasses);

        // Assert - all filters should only delegate withSubclasses
        verify(visitor1, times(1)).visitLibraryClass(withSubclasses);
        verify(visitor1, never()).visitLibraryClass(withoutSubclasses);

        verify(visitor2, times(1)).visitLibraryClass(withSubclasses);
        verify(visitor2, never()).visitLibraryClass(withoutSubclasses);

        verify(visitor3, times(1)).visitLibraryClass(withSubclasses);
        verify(visitor3, never()).visitLibraryClass(withoutSubclasses);
    }

    /**
     * Tests visitLibraryClass with alternating calls to other visitor methods.
     * Verifies consistent behavior regardless of call pattern.
     */
    @Test
    public void testVisitLibraryClass_alternatingWithOtherMethods_consistentBehavior() {
        // Arrange
        proguard.classfile.ProgramClass programClass = new proguard.classfile.ProgramClass();
        programClass.subClassCount = 1;

        libraryClass.subClassCount = 1;

        // Act & Assert - alternate calls
        assertDoesNotThrow(() -> subclassedClassFilter.visitProgramClass(programClass));
        assertDoesNotThrow(() -> subclassedClassFilter.visitLibraryClass(libraryClass));
        assertDoesNotThrow(() -> subclassedClassFilter.visitProgramClass(programClass));
        assertDoesNotThrow(() -> subclassedClassFilter.visitLibraryClass(libraryClass));

        // Verify both were delegated correctly
        verify(classVisitor, times(2)).visitProgramClass(programClass);
        verify(classVisitor, times(2)).visitLibraryClass(libraryClass);
    }
}
