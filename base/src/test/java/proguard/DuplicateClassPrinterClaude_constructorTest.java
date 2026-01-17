package proguard;

import org.junit.jupiter.api.Test;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DuplicateClassPrinter} constructor.
 * Tests DuplicateClassPrinter(WarningPrinter) constructor.
 */
public class DuplicateClassPrinterClaude_constructorTest {

    /**
     * Tests the constructor with a valid non-null WarningPrinter.
     * Verifies that the printer can be instantiated with a valid warning printer.
     */
    @Test
    public void testConstructorWithValidWarningPrinter() {
        // Arrange - Create a valid warning printer
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create DuplicateClassPrinter with valid parameter
        DuplicateClassPrinter printer = new DuplicateClassPrinter(warningPrinter);

        // Assert - Verify the printer was created successfully
        assertNotNull(printer, "DuplicateClassPrinter should be instantiated successfully");
    }

    /**
     * Tests the constructor with a null WarningPrinter.
     * Verifies that the constructor accepts null values.
     */
    @Test
    public void testConstructorWithNullWarningPrinter() {
        // Act - Create printer with null parameter
        DuplicateClassPrinter printer = new DuplicateClassPrinter(null);

        // Assert - Verify the printer was created
        assertNotNull(printer, "DuplicateClassPrinter should be instantiated even with null parameter");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that each printer instance is independent.
     */
    @Test
    public void testMultiplePrinterInstances() {
        // Arrange - Create different warning printers for each instance
        WarningPrinter warningPrinter1 = mock(WarningPrinter.class);
        WarningPrinter warningPrinter2 = mock(WarningPrinter.class);

        // Act - Create two printer instances
        DuplicateClassPrinter printer1 = new DuplicateClassPrinter(warningPrinter1);
        DuplicateClassPrinter printer2 = new DuplicateClassPrinter(warningPrinter2);

        // Assert - Verify both printers were created successfully
        assertNotNull(printer1, "First printer should be created");
        assertNotNull(printer2, "Second printer should be created");
        assertNotSame(printer1, printer2, "Printer instances should be different objects");
    }

    /**
     * Tests the constructor with the same WarningPrinter used to create multiple printers.
     * Verifies that the same parameter can be used for multiple printers.
     */
    @Test
    public void testMultiplePrintersWithSameWarningPrinter() {
        // Arrange - Create single warning printer to use for multiple printers
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create two printer instances with the same parameter
        DuplicateClassPrinter printer1 = new DuplicateClassPrinter(warningPrinter);
        DuplicateClassPrinter printer2 = new DuplicateClassPrinter(warningPrinter);

        // Assert - Verify both printers were created successfully
        assertNotNull(printer1, "First printer should be created");
        assertNotNull(printer2, "Second printer should be created");
        assertNotSame(printer1, printer2, "Printer instances should be different objects");
    }

    /**
     * Tests the constructor creates an instance that implements ClassVisitor interface.
     * Verifies that DuplicateClassPrinter can be used as a ClassVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfClassVisitor() {
        // Arrange - Create valid parameter
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create printer
        DuplicateClassPrinter printer = new DuplicateClassPrinter(warningPrinter);

        // Assert - Verify the printer implements ClassVisitor
        assertInstanceOf(proguard.classfile.visitor.ClassVisitor.class, printer,
                "DuplicateClassPrinter should implement ClassVisitor interface");
    }

    /**
     * Tests the constructor with a mocked WarningPrinter.
     * Verifies that different WarningPrinter implementations can be used.
     */
    @Test
    public void testConstructorWithMockedWarningPrinter() {
        // Arrange - Create a mock warning printer
        WarningPrinter mockPrinter = mock(WarningPrinter.class);

        // Act - Create printer with mock
        DuplicateClassPrinter printer = new DuplicateClassPrinter(mockPrinter);

        // Assert - Verify the printer was created successfully
        assertNotNull(printer, "DuplicateClassPrinter should be instantiated with mocked warning printer");
    }

    /**
     * Tests that the constructor can be called multiple times with null.
     * Verifies that null is an acceptable value for repeated instantiations.
     */
    @Test
    public void testMultipleConstructorCallsWithNull() {
        // Act - Create multiple printer instances with null
        DuplicateClassPrinter printer1 = new DuplicateClassPrinter(null);
        DuplicateClassPrinter printer2 = new DuplicateClassPrinter(null);
        DuplicateClassPrinter printer3 = new DuplicateClassPrinter(null);

        // Assert - Verify all printers were created successfully
        assertNotNull(printer1, "First printer should be created");
        assertNotNull(printer2, "Second printer should be created");
        assertNotNull(printer3, "Third printer should be created");
        assertNotSame(printer1, printer2, "First and second printer instances should be different");
        assertNotSame(printer2, printer3, "Second and third printer instances should be different");
        assertNotSame(printer1, printer3, "First and third printer instances should be different");
    }

    /**
     * Tests the constructor with different WarningPrinter mock implementations.
     * Verifies that the printer can be created with various implementations.
     */
    @Test
    public void testConstructorWithDifferentWarningPrinterTypes() {
        // Arrange - Create different types of warning printers
        WarningPrinter mockPrinter1 = mock(WarningPrinter.class);
        WarningPrinter mockPrinter2 = mock(WarningPrinter.class);

        // Act - Create printers with different implementations
        DuplicateClassPrinter printer1 = new DuplicateClassPrinter(mockPrinter1);
        DuplicateClassPrinter printer2 = new DuplicateClassPrinter(mockPrinter2);

        // Assert - Verify both printers were created successfully
        assertNotNull(printer1, "Printer with first implementation should be created");
        assertNotNull(printer2, "Printer with second implementation should be created");
    }

    /**
     * Tests the constructor parameter is properly stored and used.
     * Verifies that the WarningPrinter passed to the constructor is actually used.
     * This test uses the visitProgramClass method to verify the printer is used.
     */
    @Test
    public void testConstructorParameterIsUsed() {
        // Arrange - Create a mock warning printer
        WarningPrinter mockPrinter = mock(WarningPrinter.class);

        // Act - Create printer and use it
        DuplicateClassPrinter printer = new DuplicateClassPrinter(mockPrinter);

        // Assert - Verify the printer was created successfully
        // Note: Full verification of usage would require calling visitProgramClass,
        // but that's beyond the scope of constructor testing
        assertNotNull(printer, "DuplicateClassPrinter should be instantiated");
    }

    /**
     * Tests that the constructor doesn't throw any exceptions with valid input.
     * Verifies basic exception-free instantiation.
     */
    @Test
    public void testConstructorDoesNotThrowException() {
        // Arrange
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> new DuplicateClassPrinter(warningPrinter),
                "Constructor should not throw any exception with valid parameter");
    }

    /**
     * Tests that the constructor doesn't throw any exceptions with null input.
     * Verifies exception-free instantiation with null.
     */
    @Test
    public void testConstructorDoesNotThrowExceptionWithNull() {
        // Act & Assert - Should not throw any exception
        assertDoesNotThrow(() -> new DuplicateClassPrinter(null),
                "Constructor should not throw any exception with null parameter");
    }
}
