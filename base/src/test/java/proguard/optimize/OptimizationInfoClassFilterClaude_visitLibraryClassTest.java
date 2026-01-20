package proguard.optimize;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.LibraryClass;
import proguard.classfile.visitor.ClassVisitor;
import proguard.optimize.info.ClassOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link OptimizationInfoClassFilter#visitLibraryClass(LibraryClass)}.
 *
 * The visitLibraryClass method checks if the LibraryClass has editable optimization info
 * (specifically ProgramClassOptimizationInfo). If it does, it delegates to the wrapped
 * ClassVisitor's visitLibraryClass method. If not, it does nothing.
 *
 * Key behaviors:
 * 1. Delegates to classVisitor when class has ProgramClassOptimizationInfo
 * 2. Does not delegate when class has basic ClassOptimizationInfo
 * 3. Does not delegate when class has no optimization info (null)
 * 4. Properly filters based on instanceof check
 */
public class OptimizationInfoClassFilterClaude_visitLibraryClassTest {

    private ClassVisitor mockClassVisitor;
    private OptimizationInfoClassFilter filter;

    @BeforeEach
    public void setUp() {
        mockClassVisitor = mock(ClassVisitor.class);
        filter = new OptimizationInfoClassFilter(mockClassVisitor);
    }

    /**
     * Tests that visitLibraryClass delegates to the classVisitor when LibraryClass
     * has ProgramClassOptimizationInfo.
     */
    @Test
    public void testVisitLibraryClass_withProgramClassOptimizationInfo_delegatesToVisitor() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act
        filter.visitLibraryClass(libraryClass);

        // Assert
        verify(mockClassVisitor, times(1)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that visitLibraryClass does NOT delegate when LibraryClass has
     * basic ClassOptimizationInfo (not editable).
     */
    @Test
    public void testVisitLibraryClass_withBasicClassOptimizationInfo_doesNotDelegate() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act
        filter.visitLibraryClass(libraryClass);

        // Assert
        verify(mockClassVisitor, never()).visitLibraryClass(any());
    }

    /**
     * Tests that visitLibraryClass does NOT delegate when LibraryClass has no optimization info.
     */
    @Test
    public void testVisitLibraryClass_withNoOptimizationInfo_doesNotDelegate() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        // No optimization info set

        // Act
        filter.visitLibraryClass(libraryClass);

        // Assert
        verify(mockClassVisitor, never()).visitLibraryClass(any());
    }

    /**
     * Tests that visitLibraryClass can be called multiple times with classes that have
     * ProgramClassOptimizationInfo, delegating each time.
     */
    @Test
    public void testVisitLibraryClass_multipleCallsWithProgramClassOptimizationInfo_delegatesEachTime() {
        // Arrange
        LibraryClass libraryClass1 = new LibraryClass();
        LibraryClass libraryClass2 = new LibraryClass();
        LibraryClass libraryClass3 = new LibraryClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass2);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass3);

        // Act
        filter.visitLibraryClass(libraryClass1);
        filter.visitLibraryClass(libraryClass2);
        filter.visitLibraryClass(libraryClass3);

        // Assert
        verify(mockClassVisitor, times(1)).visitLibraryClass(libraryClass1);
        verify(mockClassVisitor, times(1)).visitLibraryClass(libraryClass2);
        verify(mockClassVisitor, times(1)).visitLibraryClass(libraryClass3);
        verify(mockClassVisitor, times(3)).visitLibraryClass(any(LibraryClass.class));
    }

    /**
     * Tests that visitLibraryClass correctly filters a mix of classes with different
     * optimization info types.
     */
    @Test
    public void testVisitLibraryClass_withMixedOptimizationInfo_delegatesOnlyForProgramClassOptimizationInfo() {
        // Arrange
        LibraryClass classWithProgramInfo = new LibraryClass();
        LibraryClass classWithBasicInfo = new LibraryClass();
        LibraryClass classWithNoInfo = new LibraryClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classWithProgramInfo);
        ClassOptimizationInfo.setClassOptimizationInfo(classWithBasicInfo);
        // classWithNoInfo has no optimization info

        // Act
        filter.visitLibraryClass(classWithProgramInfo);
        filter.visitLibraryClass(classWithBasicInfo);
        filter.visitLibraryClass(classWithNoInfo);

        // Assert
        verify(mockClassVisitor, times(1)).visitLibraryClass(classWithProgramInfo);
        verify(mockClassVisitor, never()).visitLibraryClass(classWithBasicInfo);
        verify(mockClassVisitor, never()).visitLibraryClass(classWithNoInfo);
    }

    /**
     * Tests that visitLibraryClass does not throw exception when called with null visitor
     * but LibraryClass lacks ProgramClassOptimizationInfo.
     */
    @Test
    public void testVisitLibraryClass_withNullVisitorAndNoOptimizationInfo_doesNotThrow() {
        // Arrange
        OptimizationInfoClassFilter filterWithNullVisitor = new OptimizationInfoClassFilter(null);
        LibraryClass libraryClass = new LibraryClass();
        // No optimization info

        // Act & Assert
        assertDoesNotThrow(() -> filterWithNullVisitor.visitLibraryClass(libraryClass));
    }

    /**
     * Tests that visitLibraryClass throws NullPointerException when called with null visitor
     * and LibraryClass has ProgramClassOptimizationInfo.
     */
    @Test
    public void testVisitLibraryClass_withNullVisitorAndProgramClassOptimizationInfo_throwsNullPointerException() {
        // Arrange
        OptimizationInfoClassFilter filterWithNullVisitor = new OptimizationInfoClassFilter(null);
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> filterWithNullVisitor.visitLibraryClass(libraryClass));
    }

    /**
     * Tests that visitLibraryClass works correctly with the same LibraryClass instance
     * called multiple times.
     */
    @Test
    public void testVisitLibraryClass_sameInstanceCalledMultipleTimes_delegatesEachTime() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act
        filter.visitLibraryClass(libraryClass);
        filter.visitLibraryClass(libraryClass);
        filter.visitLibraryClass(libraryClass);

        // Assert
        verify(mockClassVisitor, times(3)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that visitLibraryClass respects changes to optimization info on the same class.
     * First call without optimization info should not delegate, second call with it should delegate.
     */
    @Test
    public void testVisitLibraryClass_afterAddingOptimizationInfo_startsDelegating() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        // Start with no optimization info

        // Act & Assert - first call without optimization info
        filter.visitLibraryClass(libraryClass);
        verify(mockClassVisitor, never()).visitLibraryClass(any());

        // Add ProgramClassOptimizationInfo
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act & Assert - second call with optimization info
        filter.visitLibraryClass(libraryClass);
        verify(mockClassVisitor, times(1)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that visitLibraryClass respects removal of editable optimization info.
     * First call with ProgramClassOptimizationInfo should delegate, second call
     * with basic ClassOptimizationInfo should not.
     */
    @Test
    public void testVisitLibraryClass_afterChangingToBasicInfo_stopsDelegating() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act & Assert - first call with ProgramClassOptimizationInfo
        filter.visitLibraryClass(libraryClass);
        verify(mockClassVisitor, times(1)).visitLibraryClass(libraryClass);

        // Change to basic ClassOptimizationInfo
        ClassOptimizationInfo.setClassOptimizationInfo(libraryClass);

        // Act & Assert - second call with basic info
        filter.visitLibraryClass(libraryClass);
        // Should still be 1 time (not called again)
        verify(mockClassVisitor, times(1)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that visitLibraryClass doesn't throw exception with valid input.
     */
    @Test
    public void testVisitLibraryClass_withValidInput_doesNotThrow() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act & Assert
        assertDoesNotThrow(() -> filter.visitLibraryClass(libraryClass));
    }

    /**
     * Tests that visitLibraryClass works with multiple filter instances.
     */
    @Test
    public void testVisitLibraryClass_withMultipleFilters_eachDelegatesToOwnVisitor() {
        // Arrange
        ClassVisitor visitor1 = mock(ClassVisitor.class);
        ClassVisitor visitor2 = mock(ClassVisitor.class);
        OptimizationInfoClassFilter filter1 = new OptimizationInfoClassFilter(visitor1);
        OptimizationInfoClassFilter filter2 = new OptimizationInfoClassFilter(visitor2);

        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act
        filter1.visitLibraryClass(libraryClass);
        filter2.visitLibraryClass(libraryClass);

        // Assert
        verify(visitor1, times(1)).visitLibraryClass(libraryClass);
        verify(visitor2, times(1)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that visitLibraryClass passes the exact same LibraryClass instance to the visitor.
     */
    @Test
    public void testVisitLibraryClass_passesExactSameInstance() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act
        filter.visitLibraryClass(libraryClass);

        // Assert - verify exact instance was passed
        verify(mockClassVisitor).visitLibraryClass(same(libraryClass));
    }

    /**
     * Tests that visitLibraryClass works correctly when visitor throws exception.
     */
    @Test
    public void testVisitLibraryClass_whenVisitorThrowsException_propagatesException() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        RuntimeException testException = new RuntimeException("Test exception");
        doThrow(testException).when(mockClassVisitor).visitLibraryClass(any());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> filter.visitLibraryClass(libraryClass));
        assertSame(testException, thrown);
    }

    /**
     * Tests that visitLibraryClass can handle many sequential calls efficiently.
     */
    @Test
    public void testVisitLibraryClass_manySequentialCalls_handlesEfficiently() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act
        for (int i = 0; i < 100; i++) {
            filter.visitLibraryClass(libraryClass);
        }

        // Assert
        verify(mockClassVisitor, times(100)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests visitLibraryClass doesn't modify the LibraryClass.
     */
    @Test
    public void testVisitLibraryClass_doesNotModifyLibraryClass() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo optimizationInfo = new ProgramClassOptimizationInfo();
        libraryClass.setProcessingInfo(optimizationInfo);

        // Act
        filter.visitLibraryClass(libraryClass);

        // Assert
        assertSame(optimizationInfo, libraryClass.getProcessingInfo(),
                "LibraryClass optimization info should not be modified");
    }

    /**
     * Tests that visitLibraryClass correctly handles the instanceof check.
     */
    @Test
    public void testVisitLibraryClass_instanceofCheck_correctlyIdentifiesProgramClassOptimizationInfo() {
        // Arrange
        LibraryClass classWithProgramInfo = new LibraryClass();
        LibraryClass classWithBasicInfo = new LibraryClass();

        // Create actual instances to test instanceof behavior
        classWithProgramInfo.setProcessingInfo(new ProgramClassOptimizationInfo());
        classWithBasicInfo.setProcessingInfo(new ClassOptimizationInfo());

        // Act
        filter.visitLibraryClass(classWithProgramInfo);
        filter.visitLibraryClass(classWithBasicInfo);

        // Assert
        verify(mockClassVisitor, times(1)).visitLibraryClass(classWithProgramInfo);
        verify(mockClassVisitor, never()).visitLibraryClass(classWithBasicInfo);
    }

    /**
     * Tests that visitLibraryClass handles alternating classes with and without optimization info.
     */
    @Test
    public void testVisitLibraryClass_alternatingClassTypes_delegatesCorrectly() {
        // Arrange
        LibraryClass classWithInfo = new LibraryClass();
        LibraryClass classWithoutInfo = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(classWithInfo);

        // Act - alternate between classes
        filter.visitLibraryClass(classWithInfo);
        filter.visitLibraryClass(classWithoutInfo);
        filter.visitLibraryClass(classWithInfo);
        filter.visitLibraryClass(classWithoutInfo);

        // Assert
        verify(mockClassVisitor, times(2)).visitLibraryClass(classWithInfo);
        verify(mockClassVisitor, never()).visitLibraryClass(classWithoutInfo);
    }

    /**
     * Tests that visitLibraryClass behavior is consistent across different filter instances
     * with the same visitor.
     */
    @Test
    public void testVisitLibraryClass_differentFiltersWithSameVisitor_bothDelegate() {
        // Arrange
        ClassVisitor sharedVisitor = mock(ClassVisitor.class);
        OptimizationInfoClassFilter filter1 = new OptimizationInfoClassFilter(sharedVisitor);
        OptimizationInfoClassFilter filter2 = new OptimizationInfoClassFilter(sharedVisitor);

        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act
        filter1.visitLibraryClass(libraryClass);
        filter2.visitLibraryClass(libraryClass);

        // Assert
        verify(sharedVisitor, times(2)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that visitLibraryClass works correctly in a visitor chain.
     */
    @Test
    public void testVisitLibraryClass_inVisitorChain_delegatesCorrectly() {
        // Arrange
        ClassVisitor finalVisitor = mock(ClassVisitor.class);
        OptimizationInfoClassFilter intermediateFilter = new OptimizationInfoClassFilter(finalVisitor);
        OptimizationInfoClassFilter outerFilter = new OptimizationInfoClassFilter(intermediateFilter);

        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act
        outerFilter.visitLibraryClass(libraryClass);

        // Assert - should propagate through the chain
        verify(finalVisitor, times(1)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that visitLibraryClass doesn't call any other visitor methods.
     */
    @Test
    public void testVisitLibraryClass_onlyCallsVisitLibraryClassOnVisitor() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        // Act
        filter.visitLibraryClass(libraryClass);

        // Assert - verify only visitLibraryClass is called, not visitAnyClass or visitProgramClass
        verify(mockClassVisitor, times(1)).visitLibraryClass(libraryClass);
        verify(mockClassVisitor, never()).visitAnyClass(any());
        verify(mockClassVisitor, never()).visitProgramClass(any());
    }

    /**
     * Tests visitLibraryClass with null LibraryClass.
     * Should throw NullPointerException when trying to access processingInfo.
     */
    @Test
    public void testVisitLibraryClass_withNullLibraryClass_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> filter.visitLibraryClass(null));
    }

    /**
     * Tests that multiple visitLibraryClass calls with classes lacking optimization info
     * never trigger delegation.
     */
    @Test
    public void testVisitLibraryClass_multipleClassesWithoutInfo_neverDelegates() {
        // Arrange
        LibraryClass class1 = new LibraryClass();
        LibraryClass class2 = new LibraryClass();
        LibraryClass class3 = new LibraryClass();
        // None have optimization info

        // Act
        filter.visitLibraryClass(class1);
        filter.visitLibraryClass(class2);
        filter.visitLibraryClass(class3);

        // Assert
        verify(mockClassVisitor, never()).visitLibraryClass(any());
    }

    /**
     * Tests visitLibraryClass after the filter has processed other visitor methods.
     */
    @Test
    public void testVisitLibraryClass_afterOtherVisitorMethods_stillWorksCorrectly() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass);

        proguard.classfile.ProgramClass programClass = new proguard.classfile.ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Act - call visitProgramClass first, then visitLibraryClass
        filter.visitProgramClass(programClass);
        filter.visitLibraryClass(libraryClass);

        // Assert
        verify(mockClassVisitor, times(1)).visitProgramClass(programClass);
        verify(mockClassVisitor, times(1)).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that visitLibraryClass and visitProgramClass can be used together
     * on the same filter with consistent behavior.
     */
    @Test
    public void testVisitLibraryClass_mixedWithVisitProgramClass_bothWorkCorrectly() {
        // Arrange
        LibraryClass libraryClass1 = new LibraryClass();
        LibraryClass libraryClass2 = new LibraryClass();
        proguard.classfile.ProgramClass programClass1 = new proguard.classfile.ProgramClass();
        proguard.classfile.ProgramClass programClass2 = new proguard.classfile.ProgramClass();

        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClass1);
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass1);
        // libraryClass2 and programClass2 have no optimization info

        // Act - interleave calls
        filter.visitLibraryClass(libraryClass1);
        filter.visitProgramClass(programClass1);
        filter.visitLibraryClass(libraryClass2);
        filter.visitProgramClass(programClass2);

        // Assert
        verify(mockClassVisitor, times(1)).visitLibraryClass(libraryClass1);
        verify(mockClassVisitor, times(1)).visitProgramClass(programClass1);
        verify(mockClassVisitor, never()).visitLibraryClass(libraryClass2);
        verify(mockClassVisitor, never()).visitProgramClass(programClass2);
    }

    /**
     * Tests visitLibraryClass with a LibraryClass that has null processing info explicitly set.
     */
    @Test
    public void testVisitLibraryClass_withExplicitNullProcessingInfo_doesNotDelegate() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();
        libraryClass.setProcessingInfo(null);

        // Act
        filter.visitLibraryClass(libraryClass);

        // Assert
        verify(mockClassVisitor, never()).visitLibraryClass(any());
    }

    /**
     * Tests that visitLibraryClass handles classes in a batch processing scenario.
     */
    @Test
    public void testVisitLibraryClass_batchProcessing_filtersCorrectly() {
        // Arrange - create 10 library classes, 5 with optimization info
        LibraryClass[] libraryClasses = new LibraryClass[10];
        for (int i = 0; i < 10; i++) {
            libraryClasses[i] = new LibraryClass();
            if (i % 2 == 0) {
                ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(libraryClasses[i]);
            }
        }

        // Act - process all classes
        for (LibraryClass libraryClass : libraryClasses) {
            filter.visitLibraryClass(libraryClass);
        }

        // Assert - only 5 should have been delegated
        verify(mockClassVisitor, times(5)).visitLibraryClass(any(LibraryClass.class));
    }
}
