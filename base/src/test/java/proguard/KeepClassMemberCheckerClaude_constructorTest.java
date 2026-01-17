package proguard;

import org.junit.jupiter.api.Test;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link KeepClassMemberChecker} constructor.
 * Tests KeepClassMemberChecker(WarningPrinter) constructor.
 */
public class KeepClassMemberCheckerClaude_constructorTest {

    /**
     * Tests the constructor with a valid non-null WarningPrinter.
     * Verifies that the checker can be instantiated with a valid warning printer.
     */
    @Test
    public void testConstructorWithValidWarningPrinter() {
        // Arrange - Create a valid warning printer
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create KeepClassMemberChecker with valid parameter
        KeepClassMemberChecker checker = new KeepClassMemberChecker(warningPrinter);

        // Assert - Verify the checker was created successfully
        assertNotNull(checker, "KeepClassMemberChecker should be instantiated successfully");
    }

    /**
     * Tests the constructor with a null WarningPrinter.
     * Verifies that the constructor accepts a null warning printer.
     */
    @Test
    public void testConstructorWithNullWarningPrinter() {
        // Act - Create checker with null parameter
        KeepClassMemberChecker checker = new KeepClassMemberChecker(null);

        // Assert - Verify the checker was created
        assertNotNull(checker, "KeepClassMemberChecker should be instantiated even with null warning printer");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that each checker instance is independent.
     */
    @Test
    public void testMultipleCheckerInstances() {
        // Arrange - Create different warning printers for each instance
        WarningPrinter warningPrinter1 = mock(WarningPrinter.class);
        WarningPrinter warningPrinter2 = mock(WarningPrinter.class);

        // Act - Create two checker instances
        KeepClassMemberChecker checker1 = new KeepClassMemberChecker(warningPrinter1);
        KeepClassMemberChecker checker2 = new KeepClassMemberChecker(warningPrinter2);

        // Assert - Verify both checkers were created successfully
        assertNotNull(checker1, "First checker should be created");
        assertNotNull(checker2, "Second checker should be created");
        assertNotSame(checker1, checker2, "Checker instances should be different objects");
    }

    /**
     * Tests the constructor with the same WarningPrinter used to create multiple checkers.
     * Verifies that the same warning printer can be used for multiple checkers.
     */
    @Test
    public void testMultipleCheckersWithSameWarningPrinter() {
        // Arrange - Create a single warning printer to use for multiple checkers
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create two checker instances with the same parameter
        KeepClassMemberChecker checker1 = new KeepClassMemberChecker(warningPrinter);
        KeepClassMemberChecker checker2 = new KeepClassMemberChecker(warningPrinter);

        // Assert - Verify both checkers were created successfully
        assertNotNull(checker1, "First checker should be created");
        assertNotNull(checker2, "Second checker should be created");
        assertNotSame(checker1, checker2, "Checker instances should be different objects");
    }

    /**
     * Tests the constructor with different WarningPrinter implementations.
     * Verifies that the checker can be created with different types of warning printers.
     */
    @Test
    public void testConstructorWithDifferentWarningPrinterImplementations() {
        // Arrange - Create different warning printer mocks
        WarningPrinter warningPrinter1 = mock(WarningPrinter.class);
        WarningPrinter warningPrinter2 = mock(WarningPrinter.class);

        // Act - Create checkers with different warning printers
        KeepClassMemberChecker checker1 = new KeepClassMemberChecker(warningPrinter1);
        KeepClassMemberChecker checker2 = new KeepClassMemberChecker(warningPrinter2);

        // Assert - Verify both checkers were created successfully
        assertNotNull(checker1, "First checker should be instantiated");
        assertNotNull(checker2, "Second checker should be instantiated");
    }

    /**
     * Tests that the constructor creates a functioning instance.
     * Verifies that the checker instance can be used to call its public method.
     */
    @Test
    public void testConstructorCreatesFunctioningInstance() {
        // Arrange - Create a valid warning printer
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create checker
        KeepClassMemberChecker checker = new KeepClassMemberChecker(warningPrinter);

        // Assert - Verify the checker can call its public method without exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(null),
                "Checker should be able to call checkClassSpecifications without throwing");
    }

    /**
     * Tests constructor creates instance that can handle empty list.
     * Verifies that a checker created with the constructor can process an empty list.
     */
    @Test
    public void testConstructorInstanceCanHandleEmptyList() {
        // Arrange - Create a valid warning printer
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create checker
        KeepClassMemberChecker checker = new KeepClassMemberChecker(warningPrinter);

        // Assert - Verify the checker can handle an empty list without exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(java.util.Collections.emptyList()),
                "Checker should handle empty list without throwing");
    }

    /**
     * Tests that the type of the created instance is correct.
     * Verifies that the constructor creates an instance of KeepClassMemberChecker.
     */
    @Test
    public void testConstructorCreatesCorrectType() {
        // Arrange - Create a valid warning printer
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create checker
        KeepClassMemberChecker checker = new KeepClassMemberChecker(warningPrinter);

        // Assert - Verify the instance type
        assertInstanceOf(KeepClassMemberChecker.class, checker,
                "Constructor should create an instance of KeepClassMemberChecker");
    }

    /**
     * Tests sequential instantiation of multiple checkers.
     * Verifies that checkers can be created one after another without issues.
     */
    @Test
    public void testSequentialInstantiation() {
        // Arrange - Create warning printers
        WarningPrinter warningPrinter1 = mock(WarningPrinter.class);
        WarningPrinter warningPrinter2 = mock(WarningPrinter.class);
        WarningPrinter warningPrinter3 = mock(WarningPrinter.class);

        // Act - Create checkers sequentially
        KeepClassMemberChecker checker1 = new KeepClassMemberChecker(warningPrinter1);
        assertNotNull(checker1, "First checker should be created");

        KeepClassMemberChecker checker2 = new KeepClassMemberChecker(warningPrinter2);
        assertNotNull(checker2, "Second checker should be created");

        KeepClassMemberChecker checker3 = new KeepClassMemberChecker(warningPrinter3);
        assertNotNull(checker3, "Third checker should be created");

        // Assert - Verify all instances are different
        assertNotSame(checker1, checker2, "First and second checkers should be different");
        assertNotSame(checker2, checker3, "Second and third checkers should be different");
        assertNotSame(checker1, checker3, "First and third checkers should be different");
    }

    /**
     * Tests constructor with null followed by non-null parameter.
     * Verifies that creating a checker with null doesn't affect subsequent creations.
     */
    @Test
    public void testConstructorNullThenNonNull() {
        // Act - Create checker with null, then with valid parameter
        KeepClassMemberChecker checker1 = new KeepClassMemberChecker(null);
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        KeepClassMemberChecker checker2 = new KeepClassMemberChecker(warningPrinter);

        // Assert - Verify both checkers were created successfully
        assertNotNull(checker1, "Checker with null parameter should be created");
        assertNotNull(checker2, "Checker with valid parameter should be created");
        assertNotSame(checker1, checker2, "Checker instances should be different");
    }
}
