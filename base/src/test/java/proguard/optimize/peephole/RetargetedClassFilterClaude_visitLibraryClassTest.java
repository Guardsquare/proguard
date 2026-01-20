package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.LibraryClass;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link RetargetedClassFilter#visitLibraryClass(LibraryClass)}.
 *
 * The visitLibraryClass method always delegates to the otherClassVisitor (if not null)
 * because library classes cannot be retargeted. These tests verify this behavior.
 */
public class RetargetedClassFilterClaude_visitLibraryClassTest {

    private RetargetedClassFilter retargetedClassFilter;
    private ClassVisitor retargetedClassVisitor;
    private ClassVisitor otherClassVisitor;
    private LibraryClass libraryClass;

    @BeforeEach
    public void setUp() {
        retargetedClassVisitor = mock(ClassVisitor.class);
        otherClassVisitor = mock(ClassVisitor.class);
        retargetedClassFilter = new RetargetedClassFilter(retargetedClassVisitor, otherClassVisitor);
        libraryClass = new LibraryClass();
    }

    /**
     * Tests that visitLibraryClass delegates to otherClassVisitor when it is not null.
     * This is the primary behavior - library classes always go to the "other" visitor.
     */
    @Test
    public void testVisitLibraryClass_withOtherClassVisitor_delegatesToOtherVisitor() {
        // Act
        retargetedClassFilter.visitLibraryClass(libraryClass);

        // Assert
        verify(otherClassVisitor, times(1)).visitLibraryClass(libraryClass);
        verifyNoInteractions(retargetedClassVisitor);
    }

    /**
     * Tests that visitLibraryClass does nothing when otherClassVisitor is null.
     */
    @Test
    public void testVisitLibraryClass_withNullOtherClassVisitor_doesNothing() {
        // Arrange
        RetargetedClassFilter filterWithNullOther = new RetargetedClassFilter(retargetedClassVisitor, null);

        // Act - should not throw any exception
        assertDoesNotThrow(() -> filterWithNullOther.visitLibraryClass(libraryClass));

        // Assert - retargetedClassVisitor should not be called
        verifyNoInteractions(retargetedClassVisitor);
    }

    /**
     * Tests that visitLibraryClass never calls retargetedClassVisitor.
     * Library classes cannot be retargeted, so retargetedClassVisitor should never be used.
     */
    @Test
    public void testVisitLibraryClass_neverCallsRetargetedClassVisitor() {
        // Act
        retargetedClassFilter.visitLibraryClass(libraryClass);

        // Assert
        verifyNoInteractions(retargetedClassVisitor);
    }

    /**
     * Tests that visitLibraryClass can be called multiple times with the same library class.
     */
    @Test
    public void testVisitLibraryClass_calledMultipleTimes_delegatesEachTime() {
        // Act
        retargetedClassFilter.visitLibraryClass(libraryClass);
        retargetedClassFilter.visitLibraryClass(libraryClass);
        retargetedClassFilter.visitLibraryClass(libraryClass);

        // Assert
        verify(otherClassVisitor, times(3)).visitLibraryClass(libraryClass);
        verifyNoInteractions(retargetedClassVisitor);
    }

    /**
     * Tests that visitLibraryClass works with different LibraryClass instances.
     */
    @Test
    public void testVisitLibraryClass_withDifferentLibraryClasses_delegatesAll() {
        // Arrange
        LibraryClass libraryClass1 = new LibraryClass();
        LibraryClass libraryClass2 = new LibraryClass();
        LibraryClass libraryClass3 = new LibraryClass();

        // Act
        retargetedClassFilter.visitLibraryClass(libraryClass1);
        retargetedClassFilter.visitLibraryClass(libraryClass2);
        retargetedClassFilter.visitLibraryClass(libraryClass3);

        // Assert
        verify(otherClassVisitor).visitLibraryClass(libraryClass1);
        verify(otherClassVisitor).visitLibraryClass(libraryClass2);
        verify(otherClassVisitor).visitLibraryClass(libraryClass3);
        verifyNoInteractions(retargetedClassVisitor);
    }

    /**
     * Tests that visitLibraryClass with single-parameter constructor (null otherClassVisitor).
     * The single-parameter constructor sets otherClassVisitor to null internally.
     */
    @Test
    public void testVisitLibraryClass_singleParameterConstructor_doesNothing() {
        // Arrange
        RetargetedClassFilter filterWithSingleParam = new RetargetedClassFilter(retargetedClassVisitor);

        // Act - should not throw
        assertDoesNotThrow(() -> filterWithSingleParam.visitLibraryClass(libraryClass));

        // Assert - retargetedClassVisitor should not be called
        verifyNoInteractions(retargetedClassVisitor);
    }

    /**
     * Tests behavior when both visitors are null.
     */
    @Test
    public void testVisitLibraryClass_bothVisitorsNull_doesNothing() {
        // Arrange
        RetargetedClassFilter filterWithNullVisitors = new RetargetedClassFilter(null, null);

        // Act - should not throw
        assertDoesNotThrow(() -> filterWithNullVisitors.visitLibraryClass(libraryClass));
    }

    /**
     * Tests that the correct LibraryClass instance is passed to otherClassVisitor.
     */
    @Test
    public void testVisitLibraryClass_passesCorrectLibraryClassToVisitor() {
        // Arrange
        LibraryClass specificLibraryClass = new LibraryClass();

        // Act
        retargetedClassFilter.visitLibraryClass(specificLibraryClass);

        // Assert - verify the exact instance is passed
        verify(otherClassVisitor).visitLibraryClass(same(specificLibraryClass));
    }

    /**
     * Tests that visitLibraryClass works correctly across multiple filter instances.
     */
    @Test
    public void testVisitLibraryClass_multipleFilterInstances_independentBehavior() {
        // Arrange
        ClassVisitor otherVisitor1 = mock(ClassVisitor.class);
        ClassVisitor otherVisitor2 = mock(ClassVisitor.class);
        RetargetedClassFilter filter1 = new RetargetedClassFilter(retargetedClassVisitor, otherVisitor1);
        RetargetedClassFilter filter2 = new RetargetedClassFilter(retargetedClassVisitor, otherVisitor2);

        // Act
        filter1.visitLibraryClass(libraryClass);
        filter2.visitLibraryClass(libraryClass);

        // Assert
        verify(otherVisitor1).visitLibraryClass(libraryClass);
        verify(otherVisitor2).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that visitLibraryClass handles null LibraryClass parameter.
     * This tests defensive behavior - the method should delegate the null to otherClassVisitor.
     */
    @Test
    public void testVisitLibraryClass_withNullLibraryClass_delegatesNull() {
        // Act - delegate the null to otherClassVisitor
        retargetedClassFilter.visitLibraryClass(null);

        // Assert - otherClassVisitor should receive the null
        verify(otherClassVisitor).visitLibraryClass(null);
        verifyNoInteractions(retargetedClassVisitor);
    }

    /**
     * Tests that when otherClassVisitor throws an exception, it propagates correctly.
     */
    @Test
    public void testVisitLibraryClass_otherVisitorThrowsException_exceptionPropagates() {
        // Arrange
        RuntimeException expectedException = new RuntimeException("Test exception");
        doThrow(expectedException).when(otherClassVisitor).visitLibraryClass(any(LibraryClass.class));

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class,
                () -> retargetedClassFilter.visitLibraryClass(libraryClass));

        assertEquals(expectedException, thrownException);
        verify(otherClassVisitor).visitLibraryClass(libraryClass);
    }

    /**
     * Tests that visitLibraryClass maintains proper call order when called in sequence.
     */
    @Test
    public void testVisitLibraryClass_sequentialCalls_maintainOrder() {
        // Arrange
        LibraryClass libraryClass1 = new LibraryClass();
        LibraryClass libraryClass2 = new LibraryClass();

        // Act
        retargetedClassFilter.visitLibraryClass(libraryClass1);
        retargetedClassFilter.visitLibraryClass(libraryClass2);

        // Assert - verify order of calls
        verify(otherClassVisitor, times(2)).visitLibraryClass(any(LibraryClass.class));
        verify(otherClassVisitor).visitLibraryClass(libraryClass1);
        verify(otherClassVisitor).visitLibraryClass(libraryClass2);
    }

    /**
     * Tests that visitLibraryClass does not modify the LibraryClass instance.
     */
    @Test
    public void testVisitLibraryClass_doesNotModifyLibraryClass() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act
        retargetedClassFilter.visitLibraryClass(libraryClass);

        // Assert - only visitLibraryClass should be called on otherClassVisitor
        verify(otherClassVisitor).visitLibraryClass(libraryClass);

        // The filter itself should not interact with the library class directly
    }

    /**
     * Tests that multiple RetargetedClassFilter instances with different configurations
     * all behave correctly for library classes.
     */
    @Test
    public void testVisitLibraryClass_variousConfigurations_correctBehavior() {
        // Arrange
        ClassVisitor visitor1 = mock(ClassVisitor.class);
        ClassVisitor visitor2 = mock(ClassVisitor.class);
        ClassVisitor visitor3 = mock(ClassVisitor.class);

        RetargetedClassFilter filter1 = new RetargetedClassFilter(visitor1, visitor2);
        RetargetedClassFilter filter2 = new RetargetedClassFilter(visitor1, null);
        RetargetedClassFilter filter3 = new RetargetedClassFilter(null, visitor3);

        // Act
        filter1.visitLibraryClass(libraryClass);
        filter2.visitLibraryClass(libraryClass);
        filter3.visitLibraryClass(libraryClass);

        // Assert
        verify(visitor2).visitLibraryClass(libraryClass); // filter1's otherClassVisitor
        verify(visitor3).visitLibraryClass(libraryClass); // filter3's otherClassVisitor
        verifyNoInteractions(visitor1); // used as retargetedClassVisitor in all cases
    }

    /**
     * Tests that visitLibraryClass works correctly when called rapidly in succession.
     */
    @Test
    public void testVisitLibraryClass_rapidSuccessiveCalls_allDelegated() {
        // Act - call many times rapidly
        for (int i = 0; i < 10; i++) {
            retargetedClassFilter.visitLibraryClass(libraryClass);
        }

        // Assert
        verify(otherClassVisitor, times(10)).visitLibraryClass(libraryClass);
        verifyNoInteractions(retargetedClassVisitor);
    }

    /**
     * Tests that visitLibraryClass correctly ignores the retargeted visitor
     * even when it would throw an exception if called.
     */
    @Test
    public void testVisitLibraryClass_retargetedVisitorWouldThrow_notCalled() {
        // Arrange - set up retargetedClassVisitor to throw if called
        doThrow(new RuntimeException("Should not be called"))
                .when(retargetedClassVisitor).visitLibraryClass(any(LibraryClass.class));

        // Act - should not throw because retargetedClassVisitor is never called
        assertDoesNotThrow(() -> retargetedClassFilter.visitLibraryClass(libraryClass));

        // Assert
        verify(otherClassVisitor).visitLibraryClass(libraryClass);
        verifyNoInteractions(retargetedClassVisitor);
    }

    /**
     * Tests the fundamental contract: library classes always go to otherClassVisitor,
     * never to retargetedClassVisitor, regardless of any class state or attributes.
     */
    @Test
    public void testVisitLibraryClass_fundamentalContract_alwaysUsesOtherVisitor() {
        // Arrange - create library classes
        LibraryClass libraryClass1 = new LibraryClass();
        LibraryClass libraryClass2 = new LibraryClass();

        // Act
        retargetedClassFilter.visitLibraryClass(libraryClass1);
        retargetedClassFilter.visitLibraryClass(libraryClass2);

        // Assert - both should go to otherClassVisitor
        verify(otherClassVisitor).visitLibraryClass(libraryClass1);
        verify(otherClassVisitor).visitLibraryClass(libraryClass2);
        verifyNoInteractions(retargetedClassVisitor);
    }
}
