package proguard.classfile.visitor;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.util.ProcessingFlags;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link InjectedClassFilter}.
 * Tests the following methods:
 * - <init>.(Lproguard/classfile/visitor/ClassVisitor;Lproguard/classfile/visitor/ClassVisitor;)V
 * - visitAnyClass.(Lproguard/classfile/Clazz;)V
 * - visitProgramClass.(Lproguard/classfile/ProgramClass;)V
 * - visitLibraryClass.(Lproguard/classfile/LibraryClass;)V
 */
public class InjectedClassFilterClaudeTest {

    // ========== Constructor Tests ==========

    /**
     * Tests the constructor with both non-null visitors.
     * Verifies that the InjectedClassFilter can be instantiated with valid parameters.
     */
    @Test
    public void testConstructorWithBothNonNullVisitors() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);
        ClassVisitor otherVisitor = mock(ClassVisitor.class);

        // Act
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, otherVisitor);

        // Assert
        assertNotNull(filter, "InjectedClassFilter should be created successfully");
    }

    /**
     * Tests the constructor with null injected visitor.
     * Verifies that null is accepted for the injected class visitor.
     */
    @Test
    public void testConstructorWithNullInjectedVisitor() {
        // Arrange
        ClassVisitor otherVisitor = mock(ClassVisitor.class);

        // Act
        InjectedClassFilter filter = new InjectedClassFilter(null, otherVisitor);

        // Assert
        assertNotNull(filter, "InjectedClassFilter should be created with null injected visitor");
    }

    /**
     * Tests the constructor with null other visitor.
     * Verifies that null is accepted for the other class visitor.
     */
    @Test
    public void testConstructorWithNullOtherVisitor() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);

        // Act
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, null);

        // Assert
        assertNotNull(filter, "InjectedClassFilter should be created with null other visitor");
    }

    /**
     * Tests the constructor with both null visitors.
     * Verifies that both parameters can be null.
     */
    @Test
    public void testConstructorWithBothNullVisitors() {
        // Arrange & Act
        InjectedClassFilter filter = new InjectedClassFilter(null, null);

        // Assert
        assertNotNull(filter, "InjectedClassFilter should be created with both null visitors");
    }

    // ========== visitAnyClass Tests ==========

    /**
     * Tests visitAnyClass with a generic Clazz.
     * Verifies that UnsupportedOperationException is thrown as per the implementation.
     */
    @Test
    public void testVisitAnyClassThrowsUnsupportedOperationException() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);
        ClassVisitor otherVisitor = mock(ClassVisitor.class);
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, otherVisitor);
        Clazz clazz = mock(Clazz.class);

        // Act & Assert
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> filter.visitAnyClass(clazz),
            "visitAnyClass should throw UnsupportedOperationException"
        );

        // Verify exception message contains class names
        assertTrue(exception.getMessage().contains("InjectedClassFilter"),
            "Exception message should contain InjectedClassFilter class name");
        assertTrue(exception.getMessage().contains("does not support"),
            "Exception message should contain 'does not support'");
    }

    /**
     * Tests visitAnyClass with null.
     * Verifies behavior when null is passed.
     */
    @Test
    public void testVisitAnyClassWithNull() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);
        ClassVisitor otherVisitor = mock(ClassVisitor.class);
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, otherVisitor);

        // Act & Assert
        assertThrows(
            Exception.class,
            () -> filter.visitAnyClass(null),
            "visitAnyClass with null should throw exception"
        );
    }

    // ========== visitProgramClass Tests - Injected Classes ==========

    /**
     * Tests visitProgramClass with an injected ProgramClass and non-null injected visitor.
     * Verifies that the injected visitor is called.
     */
    @Test
    public void testVisitProgramClassWithInjectedFlagAndNonNullInjectedVisitor() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);
        ClassVisitor otherVisitor = mock(ClassVisitor.class);
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, otherVisitor);

        ProgramClass programClass = new ProgramClass();
        programClass.processingFlags = ProcessingFlags.INJECTED;

        // Act
        filter.visitProgramClass(programClass);

        // Assert
        verify(injectedVisitor, times(1)).visitProgramClass(programClass);
        verify(otherVisitor, never()).visitProgramClass(any());
    }

    /**
     * Tests visitProgramClass with an injected ProgramClass but null injected visitor.
     * Verifies that no visitor is called when the delegate is null.
     */
    @Test
    public void testVisitProgramClassWithInjectedFlagAndNullInjectedVisitor() {
        // Arrange
        ClassVisitor otherVisitor = mock(ClassVisitor.class);
        InjectedClassFilter filter = new InjectedClassFilter(null, otherVisitor);

        ProgramClass programClass = new ProgramClass();
        programClass.processingFlags = ProcessingFlags.INJECTED;

        // Act
        filter.visitProgramClass(programClass);

        // Assert
        verify(otherVisitor, never()).visitProgramClass(any());
    }

    /**
     * Tests visitProgramClass with an injected ProgramClass and multiple flags set.
     * Verifies that the injected visitor is called when INJECTED flag is present
     * even with other flags.
     */
    @Test
    public void testVisitProgramClassWithInjectedFlagAndOtherFlags() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);
        ClassVisitor otherVisitor = mock(ClassVisitor.class);
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, otherVisitor);

        ProgramClass programClass = new ProgramClass();
        // Set multiple flags including INJECTED
        programClass.processingFlags = ProcessingFlags.INJECTED | ProcessingFlags.DONT_SHRINK;

        // Act
        filter.visitProgramClass(programClass);

        // Assert
        verify(injectedVisitor, times(1)).visitProgramClass(programClass);
        verify(otherVisitor, never()).visitProgramClass(any());
    }

    // ========== visitProgramClass Tests - Non-Injected Classes ==========

    /**
     * Tests visitProgramClass with a non-injected ProgramClass and non-null other visitor.
     * Verifies that the other visitor is called.
     */
    @Test
    public void testVisitProgramClassWithoutInjectedFlagAndNonNullOtherVisitor() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);
        ClassVisitor otherVisitor = mock(ClassVisitor.class);
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, otherVisitor);

        ProgramClass programClass = new ProgramClass();
        programClass.processingFlags = 0; // No flags set

        // Act
        filter.visitProgramClass(programClass);

        // Assert
        verify(otherVisitor, times(1)).visitProgramClass(programClass);
        verify(injectedVisitor, never()).visitProgramClass(any());
    }

    /**
     * Tests visitProgramClass with a non-injected ProgramClass but null other visitor.
     * Verifies that no visitor is called when the delegate is null.
     */
    @Test
    public void testVisitProgramClassWithoutInjectedFlagAndNullOtherVisitor() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, null);

        ProgramClass programClass = new ProgramClass();
        programClass.processingFlags = 0; // No flags set

        // Act
        filter.visitProgramClass(programClass);

        // Assert
        verify(injectedVisitor, never()).visitProgramClass(any());
    }

    /**
     * Tests visitProgramClass with a non-injected ProgramClass with other flags set.
     * Verifies that the other visitor is called when INJECTED flag is not present
     * even with other flags.
     */
    @Test
    public void testVisitProgramClassWithOtherFlagsButNotInjected() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);
        ClassVisitor otherVisitor = mock(ClassVisitor.class);
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, otherVisitor);

        ProgramClass programClass = new ProgramClass();
        // Set other flags but not INJECTED
        programClass.processingFlags = ProcessingFlags.DONT_SHRINK | ProcessingFlags.DONT_OPTIMIZE;

        // Act
        filter.visitProgramClass(programClass);

        // Assert
        verify(otherVisitor, times(1)).visitProgramClass(programClass);
        verify(injectedVisitor, never()).visitProgramClass(any());
    }

    /**
     * Tests visitProgramClass with both visitors null and non-injected class.
     * Verifies that no exceptions are thrown when both visitors are null.
     */
    @Test
    public void testVisitProgramClassWithBothVisitorsNullAndNotInjected() {
        // Arrange
        InjectedClassFilter filter = new InjectedClassFilter(null, null);
        ProgramClass programClass = new ProgramClass();
        programClass.processingFlags = 0;

        // Act & Assert
        assertDoesNotThrow(() -> filter.visitProgramClass(programClass),
            "visitProgramClass should not throw exception with null visitors");
    }

    /**
     * Tests visitProgramClass with both visitors null and injected class.
     * Verifies that no exceptions are thrown when both visitors are null.
     */
    @Test
    public void testVisitProgramClassWithBothVisitorsNullAndInjected() {
        // Arrange
        InjectedClassFilter filter = new InjectedClassFilter(null, null);
        ProgramClass programClass = new ProgramClass();
        programClass.processingFlags = ProcessingFlags.INJECTED;

        // Act & Assert
        assertDoesNotThrow(() -> filter.visitProgramClass(programClass),
            "visitProgramClass should not throw exception with null visitors");
    }

    // ========== visitLibraryClass Tests ==========

    /**
     * Tests visitLibraryClass with non-null other visitor.
     * Verifies that the other visitor is always called for library classes.
     */
    @Test
    public void testVisitLibraryClassWithNonNullOtherVisitor() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);
        ClassVisitor otherVisitor = mock(ClassVisitor.class);
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, otherVisitor);

        LibraryClass libraryClass = new LibraryClass();

        // Act
        filter.visitLibraryClass(libraryClass);

        // Assert
        verify(otherVisitor, times(1)).visitLibraryClass(libraryClass);
        verify(injectedVisitor, never()).visitLibraryClass(any());
    }

    /**
     * Tests visitLibraryClass with null other visitor.
     * Verifies that no visitor is called when other visitor is null.
     */
    @Test
    public void testVisitLibraryClassWithNullOtherVisitor() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, null);

        LibraryClass libraryClass = new LibraryClass();

        // Act
        filter.visitLibraryClass(libraryClass);

        // Assert
        verify(injectedVisitor, never()).visitLibraryClass(any());
    }

    /**
     * Tests visitLibraryClass with both visitors null.
     * Verifies that no exceptions are thrown when both visitors are null.
     */
    @Test
    public void testVisitLibraryClassWithBothVisitorsNull() {
        // Arrange
        InjectedClassFilter filter = new InjectedClassFilter(null, null);
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        assertDoesNotThrow(() -> filter.visitLibraryClass(libraryClass),
            "visitLibraryClass should not throw exception with null visitors");
    }

    /**
     * Tests visitLibraryClass multiple times with same visitor.
     * Verifies that the visitor is called each time.
     */
    @Test
    public void testVisitLibraryClassMultipleTimes() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);
        ClassVisitor otherVisitor = mock(ClassVisitor.class);
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, otherVisitor);

        LibraryClass libraryClass1 = new LibraryClass();
        LibraryClass libraryClass2 = new LibraryClass();
        LibraryClass libraryClass3 = new LibraryClass();

        // Act
        filter.visitLibraryClass(libraryClass1);
        filter.visitLibraryClass(libraryClass2);
        filter.visitLibraryClass(libraryClass3);

        // Assert
        verify(otherVisitor, times(3)).visitLibraryClass(any());
        verify(otherVisitor, times(1)).visitLibraryClass(libraryClass1);
        verify(otherVisitor, times(1)).visitLibraryClass(libraryClass2);
        verify(otherVisitor, times(1)).visitLibraryClass(libraryClass3);
    }

    // ========== Edge Cases and Integration Tests ==========

    /**
     * Tests the filter with a sequence of different class types.
     * Verifies correct routing for a mixed sequence of visits.
     */
    @Test
    public void testMixedSequenceOfVisits() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);
        ClassVisitor otherVisitor = mock(ClassVisitor.class);
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, otherVisitor);

        ProgramClass injectedProgram = new ProgramClass();
        injectedProgram.processingFlags = ProcessingFlags.INJECTED;

        ProgramClass normalProgram = new ProgramClass();
        normalProgram.processingFlags = 0;

        LibraryClass libraryClass = new LibraryClass();

        // Act
        filter.visitProgramClass(injectedProgram);
        filter.visitProgramClass(normalProgram);
        filter.visitLibraryClass(libraryClass);

        // Assert
        verify(injectedVisitor, times(1)).visitProgramClass(injectedProgram);
        verify(otherVisitor, times(1)).visitProgramClass(normalProgram);
        verify(otherVisitor, times(1)).visitLibraryClass(libraryClass);
        verify(injectedVisitor, times(1)).visitProgramClass(any());
        verify(otherVisitor, times(1)).visitProgramClass(any());
        verify(otherVisitor, times(1)).visitLibraryClass(any());
    }

    /**
     * Tests that the filter correctly handles the same ProgramClass visited multiple times.
     * Verifies consistent behavior on repeated visits.
     */
    @Test
    public void testRepeatedVisitToSameProgramClass() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);
        ClassVisitor otherVisitor = mock(ClassVisitor.class);
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, otherVisitor);

        ProgramClass programClass = new ProgramClass();
        programClass.processingFlags = ProcessingFlags.INJECTED;

        // Act
        filter.visitProgramClass(programClass);
        filter.visitProgramClass(programClass);
        filter.visitProgramClass(programClass);

        // Assert
        verify(injectedVisitor, times(3)).visitProgramClass(programClass);
        verify(otherVisitor, never()).visitProgramClass(any());
    }

    /**
     * Tests that modifying processingFlags affects routing.
     * Verifies that the filter checks the flag state at the time of each visit.
     */
    @Test
    public void testProcessingFlagsModificationAffectsRouting() {
        // Arrange
        ClassVisitor injectedVisitor = mock(ClassVisitor.class);
        ClassVisitor otherVisitor = mock(ClassVisitor.class);
        InjectedClassFilter filter = new InjectedClassFilter(injectedVisitor, otherVisitor);

        ProgramClass programClass = new ProgramClass();
        programClass.processingFlags = 0;

        // Act & Assert - First visit as non-injected
        filter.visitProgramClass(programClass);
        verify(otherVisitor, times(1)).visitProgramClass(programClass);
        verify(injectedVisitor, never()).visitProgramClass(any());

        // Modify flags to mark as injected
        programClass.processingFlags = ProcessingFlags.INJECTED;

        // Act & Assert - Second visit as injected
        filter.visitProgramClass(programClass);
        verify(injectedVisitor, times(1)).visitProgramClass(programClass);
        verify(otherVisitor, times(1)).visitProgramClass(any());
    }
}
