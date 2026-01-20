package proguard.optimize;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassVisitor;
import proguard.optimize.info.ClassOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link OptimizationInfoClassFilter#visitProgramClass(ProgramClass)}.
 *
 * The visitProgramClass method checks if the ProgramClass has editable optimization info
 * (specifically ProgramClassOptimizationInfo). If it does, it delegates to the wrapped
 * ClassVisitor's visitProgramClass method. If not, it does nothing.
 *
 * Key behaviors:
 * 1. Delegates to classVisitor when class has ProgramClassOptimizationInfo
 * 2. Does not delegate when class has basic ClassOptimizationInfo
 * 3. Does not delegate when class has no optimization info (null)
 * 4. Properly filters based on instanceof check
 */
public class OptimizationInfoClassFilterClaude_visitProgramClassTest {

    private ClassVisitor mockClassVisitor;
    private OptimizationInfoClassFilter filter;

    @BeforeEach
    public void setUp() {
        mockClassVisitor = mock(ClassVisitor.class);
        filter = new OptimizationInfoClassFilter(mockClassVisitor);
    }

    /**
     * Tests that visitProgramClass delegates to the classVisitor when ProgramClass
     * has ProgramClassOptimizationInfo.
     */
    @Test
    public void testVisitProgramClass_withProgramClassOptimizationInfo_delegatesToVisitor() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        filter.visitProgramClass(programClass);

        // Assert
        verify(mockClassVisitor, times(1)).visitProgramClass(programClass);
    }

    /**
     * Tests that visitProgramClass does NOT delegate when ProgramClass has
     * basic ClassOptimizationInfo (not editable).
     */
    @Test
    public void testVisitProgramClass_withBasicClassOptimizationInfo_doesNotDelegate() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act
        filter.visitProgramClass(programClass);

        // Assert
        verify(mockClassVisitor, never()).visitProgramClass(any());
    }

    /**
     * Tests that visitProgramClass does NOT delegate when ProgramClass has no optimization info.
     */
    @Test
    public void testVisitProgramClass_withNoOptimizationInfo_doesNotDelegate() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        // No optimization info set

        // Act
        filter.visitProgramClass(programClass);

        // Assert
        verify(mockClassVisitor, never()).visitProgramClass(any());
    }

    /**
     * Tests that visitProgramClass can be called multiple times with classes that have
     * ProgramClassOptimizationInfo, delegating each time.
     */
    @Test
    public void testVisitProgramClass_multipleCallsWithProgramClassOptimizationInfo_delegatesEachTime() {
        // Arrange
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();
        ProgramClass programClass3 = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass3);

        // Act
        filter.visitProgramClass(programClass1);
        filter.visitProgramClass(programClass2);
        filter.visitProgramClass(programClass3);

        // Assert
        verify(mockClassVisitor, times(1)).visitProgramClass(programClass1);
        verify(mockClassVisitor, times(1)).visitProgramClass(programClass2);
        verify(mockClassVisitor, times(1)).visitProgramClass(programClass3);
        verify(mockClassVisitor, times(3)).visitProgramClass(any(ProgramClass.class));
    }

    /**
     * Tests that visitProgramClass correctly filters a mix of classes with different
     * optimization info types.
     */
    @Test
    public void testVisitProgramClass_withMixedOptimizationInfo_delegatesOnlyForProgramClassOptimizationInfo() {
        // Arrange
        ProgramClass classWithProgramInfo = new ProgramClass();
        ProgramClass classWithBasicInfo = new ProgramClass();
        ProgramClass classWithNoInfo = new ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classWithProgramInfo);
        ClassOptimizationInfo.setClassOptimizationInfo(classWithBasicInfo);
        // classWithNoInfo has no optimization info

        // Act
        filter.visitProgramClass(classWithProgramInfo);
        filter.visitProgramClass(classWithBasicInfo);
        filter.visitProgramClass(classWithNoInfo);

        // Assert
        verify(mockClassVisitor, times(1)).visitProgramClass(classWithProgramInfo);
        verify(mockClassVisitor, never()).visitProgramClass(classWithBasicInfo);
        verify(mockClassVisitor, never()).visitProgramClass(classWithNoInfo);
    }

    /**
     * Tests that visitProgramClass does not throw exception when called with null visitor
     * but ProgramClass lacks ProgramClassOptimizationInfo.
     */
    @Test
    public void testVisitProgramClass_withNullVisitorAndNoOptimizationInfo_doesNotThrow() {
        // Arrange
        OptimizationInfoClassFilter filterWithNullVisitor = new OptimizationInfoClassFilter(null);
        ProgramClass programClass = new ProgramClass();
        // No optimization info

        // Act & Assert
        assertDoesNotThrow(() -> filterWithNullVisitor.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass throws NullPointerException when called with null visitor
     * and ProgramClass has ProgramClassOptimizationInfo.
     */
    @Test
    public void testVisitProgramClass_withNullVisitorAndProgramClassOptimizationInfo_throwsNullPointerException() {
        // Arrange
        OptimizationInfoClassFilter filterWithNullVisitor = new OptimizationInfoClassFilter(null);
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> filterWithNullVisitor.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass works correctly with the same ProgramClass instance
     * called multiple times.
     */
    @Test
    public void testVisitProgramClass_sameInstanceCalledMultipleTimes_delegatesEachTime() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        filter.visitProgramClass(programClass);
        filter.visitProgramClass(programClass);
        filter.visitProgramClass(programClass);

        // Assert
        verify(mockClassVisitor, times(3)).visitProgramClass(programClass);
    }

    /**
     * Tests that visitProgramClass respects changes to optimization info on the same class.
     * First call without optimization info should not delegate, second call with it should delegate.
     */
    @Test
    public void testVisitProgramClass_afterAddingOptimizationInfo_startsDelaying() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        // Start with no optimization info

        // Act & Assert - first call without optimization info
        filter.visitProgramClass(programClass);
        verify(mockClassVisitor, never()).visitProgramClass(any());

        // Add ProgramClassOptimizationInfo
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert - second call with optimization info
        filter.visitProgramClass(programClass);
        verify(mockClassVisitor, times(1)).visitProgramClass(programClass);
    }

    /**
     * Tests that visitProgramClass respects removal of editable optimization info.
     * First call with ProgramClassOptimizationInfo should delegate, second call
     * with basic ClassOptimizationInfo should not.
     */
    @Test
    public void testVisitProgramClass_afterChangingToBasicInfo_stopsDelegating() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert - first call with ProgramClassOptimizationInfo
        filter.visitProgramClass(programClass);
        verify(mockClassVisitor, times(1)).visitProgramClass(programClass);

        // Change to basic ClassOptimizationInfo
        ClassOptimizationInfo.setClassOptimizationInfo(programClass);

        // Act & Assert - second call with basic info
        filter.visitProgramClass(programClass);
        // Should still be 1 time (not called again)
        verify(mockClassVisitor, times(1)).visitProgramClass(programClass);
    }

    /**
     * Tests that visitProgramClass doesn't throw exception with valid input.
     */
    @Test
    public void testVisitProgramClass_withValidInput_doesNotThrow() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act & Assert
        assertDoesNotThrow(() -> filter.visitProgramClass(programClass));
    }

    /**
     * Tests that visitProgramClass works with multiple filter instances.
     */
    @Test
    public void testVisitProgramClass_withMultipleFilters_eachDelegatesToOwnVisitor() {
        // Arrange
        ClassVisitor visitor1 = mock(ClassVisitor.class);
        ClassVisitor visitor2 = mock(ClassVisitor.class);
        OptimizationInfoClassFilter filter1 = new OptimizationInfoClassFilter(visitor1);
        OptimizationInfoClassFilter filter2 = new OptimizationInfoClassFilter(visitor2);

        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        filter1.visitProgramClass(programClass);
        filter2.visitProgramClass(programClass);

        // Assert
        verify(visitor1, times(1)).visitProgramClass(programClass);
        verify(visitor2, times(1)).visitProgramClass(programClass);
    }

    /**
     * Tests that visitProgramClass passes the exact same ProgramClass instance to the visitor.
     */
    @Test
    public void testVisitProgramClass_passesExactSameInstance() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        filter.visitProgramClass(programClass);

        // Assert - verify exact instance was passed
        verify(mockClassVisitor).visitProgramClass(same(programClass));
    }

    /**
     * Tests that visitProgramClass works correctly when visitor throws exception.
     */
    @Test
    public void testVisitProgramClass_whenVisitorThrowsException_propagatesException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        RuntimeException testException = new RuntimeException("Test exception");
        doThrow(testException).when(mockClassVisitor).visitProgramClass(any());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> filter.visitProgramClass(programClass));
        assertSame(testException, thrown);
    }

    /**
     * Tests that visitProgramClass can handle many sequential calls efficiently.
     */
    @Test
    public void testVisitProgramClass_manySequentialCalls_handlesEfficiently() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        for (int i = 0; i < 100; i++) {
            filter.visitProgramClass(programClass);
        }

        // Assert
        verify(mockClassVisitor, times(100)).visitProgramClass(programClass);
    }

    /**
     * Tests visitProgramClass doesn't modify the ProgramClass.
     */
    @Test
    public void testVisitProgramClass_doesNotModifyProgramClass() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo optimizationInfo = new ProgramClassOptimizationInfo();
        programClass.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitProgramClass(programClass);

        // Assert
        assertSame(optimizationInfo, programClass.getProcessingInfo(),
                "ProgramClass optimization info should not be modified");
    }

    /**
     * Tests that visitProgramClass correctly handles the instanceof check.
     */
    @Test
    public void testVisitProgramClass_instanceofCheck_correctlyIdentifiesProgramClassOptimizationInfo() {
        // Arrange
        ProgramClass classWithProgramInfo = new ProgramClass();
        ProgramClass classWithBasicInfo = new ProgramClass();

        // Create actual instances to test instanceof behavior
        classWithProgramInfo.setProcessingInfo(new ProgramClassOptimizationInfo());
        classWithBasicInfo.setProcessingInfo(new ClassOptimizationInfo());

        // Act
        filter.visitProgramClass(classWithProgramInfo);
        filter.visitProgramClass(classWithBasicInfo);

        // Assert
        verify(mockClassVisitor, times(1)).visitProgramClass(classWithProgramInfo);
        verify(mockClassVisitor, never()).visitProgramClass(classWithBasicInfo);
    }

    /**
     * Tests that visitProgramClass handles alternating classes with and without optimization info.
     */
    @Test
    public void testVisitProgramClass_alternatingClassTypes_delegatesCorrectly() {
        // Arrange
        ProgramClass classWithInfo = new ProgramClass();
        ProgramClass classWithoutInfo = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classWithInfo);

        // Act - alternate between classes
        filter.visitProgramClass(classWithInfo);
        filter.visitProgramClass(classWithoutInfo);
        filter.visitProgramClass(classWithInfo);
        filter.visitProgramClass(classWithoutInfo);

        // Assert
        verify(mockClassVisitor, times(2)).visitProgramClass(classWithInfo);
        verify(mockClassVisitor, never()).visitProgramClass(classWithoutInfo);
    }

    /**
     * Tests that visitProgramClass behavior is consistent across different filter instances
     * with the same visitor.
     */
    @Test
    public void testVisitProgramClass_differentFiltersWithSameVisitor_bothDelegate() {
        // Arrange
        ClassVisitor sharedVisitor = mock(ClassVisitor.class);
        OptimizationInfoClassFilter filter1 = new OptimizationInfoClassFilter(sharedVisitor);
        OptimizationInfoClassFilter filter2 = new OptimizationInfoClassFilter(sharedVisitor);

        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        filter1.visitProgramClass(programClass);
        filter2.visitProgramClass(programClass);

        // Assert
        verify(sharedVisitor, times(2)).visitProgramClass(programClass);
    }

    /**
     * Tests that visitProgramClass works correctly in a visitor chain.
     */
    @Test
    public void testVisitProgramClass_inVisitorChain_delegatesCorrectly() {
        // Arrange
        ClassVisitor finalVisitor = mock(ClassVisitor.class);
        OptimizationInfoClassFilter intermediateFilter = new OptimizationInfoClassFilter(finalVisitor);
        OptimizationInfoClassFilter outerFilter = new OptimizationInfoClassFilter(intermediateFilter);

        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        outerFilter.visitProgramClass(programClass);

        // Assert - should propagate through the chain
        verify(finalVisitor, times(1)).visitProgramClass(programClass);
    }

    /**
     * Tests that visitProgramClass doesn't call any other visitor methods.
     */
    @Test
    public void testVisitProgramClass_onlyCallsVisitProgramClassOnVisitor() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act
        filter.visitProgramClass(programClass);

        // Assert - verify only visitProgramClass is called, not visitAnyClass or visitLibraryClass
        verify(mockClassVisitor, times(1)).visitProgramClass(programClass);
        verify(mockClassVisitor, never()).visitAnyClass(any());
        verify(mockClassVisitor, never()).visitLibraryClass(any());
    }

    /**
     * Tests visitProgramClass with null ProgramClass.
     * Should throw NullPointerException when trying to access processingInfo.
     */
    @Test
    public void testVisitProgramClass_withNullProgramClass_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> filter.visitProgramClass(null));
    }

    /**
     * Tests that multiple visitProgramClass calls with classes lacking optimization info
     * never trigger delegation.
     */
    @Test
    public void testVisitProgramClass_multipleClassesWithoutInfo_neverDelegates() {
        // Arrange
        ProgramClass class1 = new ProgramClass();
        ProgramClass class2 = new ProgramClass();
        ProgramClass class3 = new ProgramClass();
        // None have optimization info

        // Act
        filter.visitProgramClass(class1);
        filter.visitProgramClass(class2);
        filter.visitProgramClass(class3);

        // Assert
        verify(mockClassVisitor, never()).visitProgramClass(any());
    }

    /**
     * Tests visitProgramClass after the filter has processed other visitor methods.
     */
    @Test
    public void testVisitProgramClass_afterOtherVisitorMethods_stillWorksCorrectly() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        proguard.classfile.LibraryClass libraryClass = new proguard.classfile.LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act - call visitLibraryClass first, then visitProgramClass
        filter.visitLibraryClass(libraryClass);
        filter.visitProgramClass(programClass);

        // Assert
        verify(mockClassVisitor, times(1)).visitLibraryClass(libraryClass);
        verify(mockClassVisitor, times(1)).visitProgramClass(programClass);
    }
}
