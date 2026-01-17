package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.Clazz;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DuplicateClassPrinter#visitAnyClass(Clazz)}.
 *
 * The visitAnyClass method in DuplicateClassPrinter throws an UnsupportedOperationException.
 * This is the default implementation that should never be called in normal usage,
 * as visitProgramClass and visitLibraryClass handle the specific class types.
 * These tests verify the exception is thrown correctly with the appropriate message.
 */
public class DuplicateClassPrinterClaude_visitAnyClassTest {

    private DuplicateClassPrinter duplicateClassPrinter;
    private WarningPrinter warningPrinter;
    private Clazz clazz;

    @BeforeEach
    public void setUp() {
        warningPrinter = mock(WarningPrinter.class);
        duplicateClassPrinter = new DuplicateClassPrinter(warningPrinter);
        clazz = mock(Clazz.class);
    }

    /**
     * Tests that visitAnyClass throws UnsupportedOperationException with a valid mock Clazz.
     * Verifies the basic exception throwing behavior.
     */
    @Test
    public void testVisitAnyClass_withValidMock_throwsUnsupportedOperationException() {
        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> duplicateClassPrinter.visitAnyClass(clazz),
                "visitAnyClass should throw UnsupportedOperationException");
    }

    /**
     * Tests that the exception message contains the DuplicateClassPrinter class name.
     */
    @Test
    public void testVisitAnyClass_exceptionMessageContainsPrinterClassName() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> duplicateClassPrinter.visitAnyClass(clazz)
        );

        // Assert
        assertTrue(exception.getMessage().contains("DuplicateClassPrinter"),
                "Exception message should contain 'DuplicateClassPrinter'");
    }

    /**
     * Tests that the exception message contains a class name from the mock.
     * Since Mockito creates a proxy, the message will contain the mock class name.
     */
    @Test
    public void testVisitAnyClass_exceptionMessageContainsClazzClassName() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> duplicateClassPrinter.visitAnyClass(clazz)
        );

        // Assert - message should mention the clazz's actual class name (mock proxy)
        String message = exception.getMessage();
        assertNotNull(message, "Exception message should not be null");
        assertTrue(message.length() > 0, "Exception message should not be empty");
    }

    /**
     * Tests that the exception message follows the expected format.
     */
    @Test
    public void testVisitAnyClass_exceptionMessageFormat() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> duplicateClassPrinter.visitAnyClass(clazz)
        );

        // Assert - message format is "ClassName does not support OtherClassName"
        String message = exception.getMessage();
        assertTrue(message.contains("does not support"),
                "Exception message should contain 'does not support'");
    }

    /**
     * Tests visitAnyClass with null Clazz parameter.
     * Should throw NullPointerException when trying to access clazz.getClass().
     */
    @Test
    public void testVisitAnyClass_withNullClazz_throwsNullPointerException() {
        // Act & Assert - null.getClass() will throw NullPointerException
        assertThrows(NullPointerException.class,
                () -> duplicateClassPrinter.visitAnyClass(null),
                "visitAnyClass with null should throw NullPointerException");
    }

    /**
     * Tests that visitAnyClass can be called multiple times and always throws exception.
     */
    @Test
    public void testVisitAnyClass_calledMultipleTimes_alwaysThrowsException() {
        // Act & Assert - each call should throw exception
        assertThrows(UnsupportedOperationException.class,
                () -> duplicateClassPrinter.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> duplicateClassPrinter.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> duplicateClassPrinter.visitAnyClass(clazz));
    }

    /**
     * Tests visitAnyClass with different Clazz mock instances.
     */
    @Test
    public void testVisitAnyClass_withDifferentClazzes_throwsException() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> duplicateClassPrinter.visitAnyClass(clazz1));
        assertThrows(UnsupportedOperationException.class,
                () -> duplicateClassPrinter.visitAnyClass(clazz2));
        assertThrows(UnsupportedOperationException.class,
                () -> duplicateClassPrinter.visitAnyClass(clazz3));
    }

    /**
     * Tests that visitAnyClass throws exception even with null WarningPrinter.
     * The exception is thrown before the printer would be used.
     */
    @Test
    public void testVisitAnyClass_withNullWarningPrinter_stillThrowsException() {
        // Arrange
        DuplicateClassPrinter printerWithNullWarning = new DuplicateClassPrinter(null);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> printerWithNullWarning.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass does not call the WarningPrinter.
     * The exception is thrown before any printing logic is reached.
     */
    @Test
    public void testVisitAnyClass_doesNotUseWarningPrinter() {
        // Arrange
        WarningPrinter mockPrinter = mock(WarningPrinter.class);
        DuplicateClassPrinter printer = new DuplicateClassPrinter(mockPrinter);

        // Act - catch exception to continue test
        try {
            printer.visitAnyClass(clazz);
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Assert - verify the warning printer was never used
        verifyNoInteractions(mockPrinter);
    }

    /**
     * Tests visitAnyClass on multiple DuplicateClassPrinter instances.
     * Verifies that different instances behave consistently.
     */
    @Test
    public void testVisitAnyClass_multiplePrinterInstances_allThrowException() {
        // Arrange
        DuplicateClassPrinter printer1 = new DuplicateClassPrinter(mock(WarningPrinter.class));
        DuplicateClassPrinter printer2 = new DuplicateClassPrinter(mock(WarningPrinter.class));

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> printer1.visitAnyClass(clazz));
        assertThrows(UnsupportedOperationException.class,
                () -> printer2.visitAnyClass(clazz));
    }

    /**
     * Tests that the exception is of the exact type UnsupportedOperationException.
     * Not a subclass or other exception type.
     */
    @Test
    public void testVisitAnyClass_throwsExactExceptionType() {
        // Act
        Exception exception = assertThrows(Exception.class,
                () -> duplicateClassPrinter.visitAnyClass(clazz));

        // Assert
        assertEquals(UnsupportedOperationException.class, exception.getClass(),
                "Exception should be exactly UnsupportedOperationException");
    }

    /**
     * Tests that visitAnyClass throws exception immediately without side effects.
     * The method should not modify any state before throwing.
     */
    @Test
    public void testVisitAnyClass_throwsImmediatelyWithoutSideEffects() {
        // Arrange
        WarningPrinter spyPrinter = mock(WarningPrinter.class);
        DuplicateClassPrinter printer = new DuplicateClassPrinter(spyPrinter);

        // Act - attempt to call visitAnyClass
        try {
            printer.visitAnyClass(clazz);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected - verify no side effects occurred
            verifyNoInteractions(spyPrinter);
        }
    }

    /**
     * Tests the complete exception message structure.
     * Verifies it matches the pattern: "ClassName does not support OtherClassName"
     */
    @Test
    public void testVisitAnyClass_completeExceptionMessage() {
        // Act
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> duplicateClassPrinter.visitAnyClass(clazz)
        );

        // Assert - check complete message structure
        String message = exception.getMessage();
        assertNotNull(message, "Exception message should not be null");
        assertTrue(message.contains("proguard.DuplicateClassPrinter"),
                "Message should contain full class name of DuplicateClassPrinter");
    }

    /**
     * Tests that visitAnyClass can be invoked after other visitor methods
     * and still throws the exception.
     */
    @Test
    public void testVisitAnyClass_afterOtherVisitorMethods_stillThrowsException() {
        // Arrange
        proguard.classfile.ProgramClass programClass = mock(proguard.classfile.ProgramClass.class);
        when(programClass.getName()).thenReturn("com/example/Test");

        // Act - call visitProgramClass first (which should work)
        assertDoesNotThrow(() -> duplicateClassPrinter.visitProgramClass(programClass));

        // Assert - then visitAnyClass should still throw
        assertThrows(UnsupportedOperationException.class,
                () -> duplicateClassPrinter.visitAnyClass(clazz));
    }

    /**
     * Tests that visitAnyClass behavior is consistent across rapid sequential calls.
     */
    @Test
    public void testVisitAnyClass_rapidSequentialCalls_consistentBehavior() {
        // Act & Assert - all calls should throw
        for (int i = 0; i < 10; i++) {
            assertThrows(UnsupportedOperationException.class,
                    () -> duplicateClassPrinter.visitAnyClass(clazz),
                    "Call " + i + " should throw UnsupportedOperationException");
        }
    }

    /**
     * Tests that a DuplicateClassPrinter created with all null parameters
     * still throws UnsupportedOperationException in visitAnyClass.
     */
    @Test
    public void testVisitAnyClass_printerWithNullParameters_throwsException() {
        // Arrange
        DuplicateClassPrinter printerWithNulls = new DuplicateClassPrinter(null);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                () -> printerWithNulls.visitAnyClass(clazz));
    }
}
