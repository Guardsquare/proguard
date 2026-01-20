package proguard.optimize.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.LibraryClass;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link SimpleEnumFilter#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in SimpleEnumFilter is designed to throw an
 * UnsupportedOperationException. This is because SimpleEnumFilter is intended
 * to work specifically with visitProgramClass and visitLibraryClass methods,
 * which contain the actual filtering logic based on whether a class is marked
 * as a simple enum.
 *
 * Key behavior:
 * - visitAnyClass always throws UnsupportedOperationException
 * - The exception message includes the class name of the filter and the clazz type
 * - This behavior is consistent regardless of constructor parameters or class state
 *
 * These tests verify:
 * 1. visitAnyClass throws UnsupportedOperationException with any Clazz implementation
 * 2. The exception message includes relevant debugging information
 * 3. The behavior is consistent with different filter configurations
 * 4. The method rejects both ProgramClass and LibraryClass when called directly
 */
public class SimpleEnumFilterClaude_visitAnyClassTest {

    private SimpleEnumFilter filterWithBothVisitors;
    private SimpleEnumFilter filterWithOnlySimpleEnumVisitor;
    private TrackingClassVisitor simpleEnumVisitor;
    private TrackingClassVisitor otherVisitor;

    @BeforeEach
    public void setUp() {
        simpleEnumVisitor = new TrackingClassVisitor();
        otherVisitor = new TrackingClassVisitor();
        filterWithBothVisitors = new SimpleEnumFilter(simpleEnumVisitor, otherVisitor);
        filterWithOnlySimpleEnumVisitor = new SimpleEnumFilter(simpleEnumVisitor);
    }

    /**
     * Tests that visitAnyClass throws UnsupportedOperationException with ProgramClass.
     */
    @Test
    public void testVisitAnyClass_withProgramClass_throwsUnsupportedException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(programClass),
            "visitAnyClass should throw UnsupportedOperationException with ProgramClass"
        );

        // Verify exception message contains relevant information
        String message = exception.getMessage();
        assertNotNull(message, "Exception message should not be null");
        assertTrue(message.contains("SimpleEnumFilter"),
            "Exception message should contain the filter class name");
        assertTrue(message.contains("ProgramClass"),
            "Exception message should contain the clazz class name");
    }

    /**
     * Tests that visitAnyClass throws UnsupportedOperationException with LibraryClass.
     */
    @Test
    public void testVisitAnyClass_withLibraryClass_throwsUnsupportedException() {
        // Arrange
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(libraryClass),
            "visitAnyClass should throw UnsupportedOperationException with LibraryClass"
        );

        // Verify exception message contains relevant information
        String message = exception.getMessage();
        assertNotNull(message, "Exception message should not be null");
        assertTrue(message.contains("SimpleEnumFilter"),
            "Exception message should contain the filter class name");
        assertTrue(message.contains("LibraryClass"),
            "Exception message should contain the clazz class name");
    }

    /**
     * Tests that visitAnyClass throws exception even when class is marked as simple enum.
     */
    @Test
    public void testVisitAnyClass_withSimpleEnumMarkedClass_stillThrowsException() {
        // Arrange - create a class marked as simple enum
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        SimpleEnumMarker marker = new SimpleEnumMarker(true);
        marker.visitProgramClass(programClass);

        // Verify it's marked as simple enum
        assertTrue(SimpleEnumMarker.isSimpleEnum(programClass),
            "Class should be marked as simple enum");

        // Act & Assert - should still throw exception
        assertThrows(
            UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(programClass),
            "visitAnyClass should throw exception even for simple enum marked class"
        );
    }

    /**
     * Tests that visitAnyClass throws exception even when class is not marked as simple enum.
     */
    @Test
    public void testVisitAnyClass_withNonSimpleEnumClass_throwsException() {
        // Arrange - create a class NOT marked as simple enum
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);

        // Verify it's not marked as simple enum
        assertFalse(SimpleEnumMarker.isSimpleEnum(programClass),
            "Class should not be marked as simple enum");

        // Act & Assert - should throw exception
        assertThrows(
            UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(programClass),
            "visitAnyClass should throw exception for non-simple enum class"
        );
    }

    /**
     * Tests that visitAnyClass throws exception with filter that has only simple enum visitor.
     */
    @Test
    public void testVisitAnyClass_withOnlySimpleEnumVisitor_throwsException() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertThrows(
            UnsupportedOperationException.class,
            () -> filterWithOnlySimpleEnumVisitor.visitAnyClass(programClass),
            "visitAnyClass should throw exception even with single visitor constructor"
        );
    }

    /**
     * Tests that visitAnyClass throws exception with filter that has null visitors.
     */
    @Test
    public void testVisitAnyClass_withNullVisitors_throwsException() {
        // Arrange
        SimpleEnumFilter filterWithNullVisitors = new SimpleEnumFilter(null, null);
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertThrows(
            UnsupportedOperationException.class,
            () -> filterWithNullVisitors.visitAnyClass(programClass),
            "visitAnyClass should throw exception even with null visitors"
        );
    }

    /**
     * Tests that visitAnyClass does not delegate to any visitor before throwing.
     * Verifies that the exception is thrown immediately without side effects.
     */
    @Test
    public void testVisitAnyClass_doesNotDelegateBeforeThrowing() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        SimpleEnumMarker marker = new SimpleEnumMarker(true);
        marker.visitProgramClass(programClass);

        // Act & Assert
        assertThrows(
            UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(programClass)
        );

        // Verify no delegation occurred
        assertEquals(0, simpleEnumVisitor.visitCount,
            "Simple enum visitor should not be called");
        assertEquals(0, otherVisitor.visitCount,
            "Other visitor should not be called");
        assertFalse(simpleEnumVisitor.visited,
            "Simple enum visitor should not be visited");
        assertFalse(otherVisitor.visited,
            "Other visitor should not be visited");
    }

    /**
     * Tests that visitAnyClass throws exception consistently across multiple calls.
     */
    @Test
    public void testVisitAnyClass_multipleCalls_alwaysThrowsException() {
        // Arrange
        ProgramClass programClass1 = new ProgramClass();
        ProgramClass programClass2 = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert - each call should throw
        assertThrows(UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(programClass1));
        assertThrows(UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(programClass2));
        assertThrows(UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(libraryClass));
    }

    /**
     * Tests that visitAnyClass exception message format is consistent.
     */
    @Test
    public void testVisitAnyClass_exceptionMessageFormat() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(programClass)
        );

        // Assert
        String message = exception.getMessage();
        assertTrue(message.contains("does not support"),
            "Exception message should contain 'does not support'");
        assertTrue(message.matches(".*SimpleEnumFilter.*does not support.*ProgramClass.*"),
            "Exception message should follow expected format");
    }

    /**
     * Tests that visitAnyClass throws exception when called through ClassVisitor interface.
     */
    @Test
    public void testVisitAnyClass_throughInterface_throwsException() {
        // Arrange
        ClassVisitor visitorInterface = filterWithBothVisitors;
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertThrows(
            UnsupportedOperationException.class,
            () -> visitorInterface.visitAnyClass(programClass),
            "visitAnyClass should throw exception when called through interface"
        );
    }

    /**
     * Tests that visitAnyClass behavior is independent of filter state.
     */
    @Test
    public void testVisitAnyClass_independentOfFilterState() {
        // Arrange - use the filter for other operations first
        ProgramClass simpleEnumClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(simpleEnumClass);
        SimpleEnumMarker marker = new SimpleEnumMarker(true);
        marker.visitProgramClass(simpleEnumClass);

        // Use visitProgramClass successfully
        assertDoesNotThrow(() -> filterWithBothVisitors.visitProgramClass(simpleEnumClass));

        // Now try visitAnyClass - should still throw
        ProgramClass anotherClass = new ProgramClass();
        assertThrows(
            UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(anotherClass),
            "visitAnyClass should throw exception regardless of prior filter usage"
        );
    }

    /**
     * Tests that visitAnyClass throws exception immediately without checking class state.
     */
    @Test
    public void testVisitAnyClass_throwsWithoutCheckingClassState() {
        // Arrange - create a class without any optimization info
        ProgramClass programClass = new ProgramClass();
        // Note: not setting optimization info

        // Act & Assert - should throw before checking any class state
        assertThrows(
            UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(programClass),
            "visitAnyClass should throw exception without checking class state"
        );
    }

    /**
     * Tests that multiple filter instances all throw exception in visitAnyClass.
     */
    @Test
    public void testVisitAnyClass_multipleFilterInstances_allThrowException() {
        // Arrange
        SimpleEnumFilter filter1 = new SimpleEnumFilter(new TrackingClassVisitor());
        SimpleEnumFilter filter2 = new SimpleEnumFilter(new TrackingClassVisitor(), new TrackingClassVisitor());
        SimpleEnumFilter filter3 = new SimpleEnumFilter(null);

        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> filter1.visitAnyClass(programClass));
        assertThrows(UnsupportedOperationException.class, () -> filter2.visitAnyClass(programClass));
        assertThrows(UnsupportedOperationException.class, () -> filter3.visitAnyClass(programClass));
    }

    /**
     * Tests that visitAnyClass exception can be caught and handled.
     */
    @Test
    public void testVisitAnyClass_exceptionCanBeCaught() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        boolean exceptionCaught = false;

        // Act
        try {
            filterWithBothVisitors.visitAnyClass(programClass);
        } catch (UnsupportedOperationException e) {
            exceptionCaught = true;
        }

        // Assert
        assertTrue(exceptionCaught, "UnsupportedOperationException should be catchable");
    }

    /**
     * Tests that visitAnyClass throws the correct exception type.
     */
    @Test
    public void testVisitAnyClass_throwsCorrectExceptionType() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        try {
            filterWithBothVisitors.visitAnyClass(programClass);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass(),
                "Exception should be exactly UnsupportedOperationException, not a subclass");
        }
    }

    /**
     * Tests that visitAnyClass exception includes the actual clazz class name.
     * This is important for debugging when unexpected class types are passed.
     */
    @Test
    public void testVisitAnyClass_exceptionIncludesActualClazzClassName() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        LibraryClass libraryClass = new LibraryClass();

        // Act & Assert for ProgramClass
        UnsupportedOperationException programException = assertThrows(
            UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(programClass)
        );
        assertTrue(programException.getMessage().contains(programClass.getClass().getName()),
            "Exception message should contain actual ProgramClass class name");

        // Act & Assert for LibraryClass
        UnsupportedOperationException libraryException = assertThrows(
            UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(libraryClass)
        );
        assertTrue(libraryException.getMessage().contains(libraryClass.getClass().getName()),
            "Exception message should contain actual LibraryClass class name");
    }

    /**
     * Tests that visitAnyClass behavior matches the documented contract.
     * According to the implementation, visitAnyClass should throw UnsupportedOperationException
     * because SimpleEnumFilter is designed to work with specific visit methods.
     */
    @Test
    public void testVisitAnyClass_matchesDocumentedContract() {
        // Arrange
        ProgramClass programClass = new ProgramClass();

        // Act & Assert
        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(programClass),
            "visitAnyClass must throw UnsupportedOperationException as documented"
        );

        // Verify this is intentional behavior, not an accident
        assertNotNull(exception.getMessage(),
            "Exception should have a message explaining why it's not supported");
    }

    /**
     * Tests that visitAnyClass does not modify the clazz parameter.
     */
    @Test
    public void testVisitAnyClass_doesNotModifyClazz() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        ProgramClassOptimizationInfo.setProgramClassOptimizationInfo(programClass);
        boolean initialSimpleEnumState = SimpleEnumMarker.isSimpleEnum(programClass);

        // Act & Assert
        assertThrows(
            UnsupportedOperationException.class,
            () -> filterWithBothVisitors.visitAnyClass(programClass)
        );

        // Verify class state was not modified
        assertEquals(initialSimpleEnumState, SimpleEnumMarker.isSimpleEnum(programClass),
            "Class simple enum state should not be modified");
    }

    /**
     * Tests visitAnyClass with rapid succession to verify consistent behavior.
     */
    @Test
    public void testVisitAnyClass_rapidSuccession_consistentBehavior() {
        // Arrange
        ProgramClass programClass = new ProgramClass();
        int exceptionCount = 0;

        // Act
        for (int i = 0; i < 100; i++) {
            try {
                filterWithBothVisitors.visitAnyClass(programClass);
            } catch (UnsupportedOperationException e) {
                exceptionCount++;
            }
        }

        // Assert
        assertEquals(100, exceptionCount,
            "visitAnyClass should throw exception consistently on every call");
    }

    /**
     * Helper class that tracks whether it was visited and counts visits.
     */
    private static class TrackingClassVisitor implements ClassVisitor {
        boolean visited = false;
        int visitCount = 0;

        @Override
        public void visitAnyClass(Clazz clazz) {
            visited = true;
            visitCount++;
        }

        @Override
        public void visitProgramClass(ProgramClass programClass) {
            visited = true;
            visitCount++;
        }

        @Override
        public void visitLibraryClass(LibraryClass libraryClass) {
            visited = true;
            visitCount++;
        }
    }
}
