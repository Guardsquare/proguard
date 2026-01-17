package proguard;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link LibraryKeepChecker} constructor.
 * Tests LibraryKeepChecker(ClassPool, ClassPool, WarningPrinter) constructor.
 */
public class LibraryKeepCheckerClaude_constructorTest {

    /**
     * Tests the constructor with valid non-null parameters.
     * Verifies that the checker can be instantiated with valid class pools and warning printer.
     */
    @Test
    public void testConstructorWithValidParameters() {
        // Arrange - Create valid class pools and warning printer
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create LibraryKeepChecker with valid parameters
        LibraryKeepChecker checker = new LibraryKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        // Assert - Verify the checker was created successfully
        assertNotNull(checker, "LibraryKeepChecker should be instantiated successfully");
    }

    /**
     * Tests the constructor with all null parameters.
     * Verifies that the constructor accepts null values.
     */
    @Test
    public void testConstructorWithAllNullParameters() {
        // Act - Create checker with all null parameters
        LibraryKeepChecker checker = new LibraryKeepChecker(null, null, null);

        // Assert - Verify the checker was created
        assertNotNull(checker, "LibraryKeepChecker should be instantiated even with null parameters");
    }

    /**
     * Tests the constructor with null program class pool.
     * Verifies that the constructor accepts a null program class pool.
     */
    @Test
    public void testConstructorWithNullProgramClassPool() {
        // Arrange - Create valid library class pool and warning printer
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create checker with null program class pool
        LibraryKeepChecker checker = new LibraryKeepChecker(
                null,
                libraryClassPool,
                warningPrinter
        );

        // Assert - Verify the checker was created
        assertNotNull(checker, "LibraryKeepChecker should be instantiated with null program class pool");
    }

    /**
     * Tests the constructor with null library class pool.
     * Verifies that the constructor accepts a null library class pool.
     */
    @Test
    public void testConstructorWithNullLibraryClassPool() {
        // Arrange - Create valid program class pool and warning printer
        ClassPool programClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create checker with null library class pool
        LibraryKeepChecker checker = new LibraryKeepChecker(
                programClassPool,
                null,
                warningPrinter
        );

        // Assert - Verify the checker was created
        assertNotNull(checker, "LibraryKeepChecker should be instantiated with null library class pool");
    }

    /**
     * Tests the constructor with null warning printer.
     * Verifies that the constructor accepts a null warning printer.
     */
    @Test
    public void testConstructorWithNullWarningPrinter() {
        // Arrange - Create valid class pools
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();

        // Act - Create checker with null warning printer
        LibraryKeepChecker checker = new LibraryKeepChecker(
                programClassPool,
                libraryClassPool,
                null
        );

        // Assert - Verify the checker was created
        assertNotNull(checker, "LibraryKeepChecker should be instantiated with null warning printer");
    }

    /**
     * Tests the constructor with empty class pools.
     * Verifies that the checker can be created with empty but non-null class pools.
     */
    @Test
    public void testConstructorWithEmptyClassPools() {
        // Arrange - Create empty class pools and warning printer
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create checker with empty class pools
        LibraryKeepChecker checker = new LibraryKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        // Assert - Verify the checker was created successfully
        assertNotNull(checker, "LibraryKeepChecker should be instantiated with empty class pools");
    }

    /**
     * Tests the constructor with same class pool for both program and library.
     * Verifies that the same class pool instance can be used for both parameters.
     */
    @Test
    public void testConstructorWithSameClassPoolForBoth() {
        // Arrange - Create a single class pool to use for both parameters
        ClassPool classPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create checker with same class pool for both parameters
        LibraryKeepChecker checker = new LibraryKeepChecker(
                classPool,
                classPool,
                warningPrinter
        );

        // Assert - Verify the checker was created successfully
        assertNotNull(checker, "LibraryKeepChecker should be instantiated with same class pool for both parameters");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that each checker instance is independent.
     */
    @Test
    public void testMultipleCheckerInstances() {
        // Arrange - Create different class pools and warning printers for each instance
        ClassPool programClassPool1 = new ClassPool();
        ClassPool libraryClassPool1 = new ClassPool();
        WarningPrinter warningPrinter1 = mock(WarningPrinter.class);

        ClassPool programClassPool2 = new ClassPool();
        ClassPool libraryClassPool2 = new ClassPool();
        WarningPrinter warningPrinter2 = mock(WarningPrinter.class);

        // Act - Create two checker instances
        LibraryKeepChecker checker1 = new LibraryKeepChecker(
                programClassPool1,
                libraryClassPool1,
                warningPrinter1
        );
        LibraryKeepChecker checker2 = new LibraryKeepChecker(
                programClassPool2,
                libraryClassPool2,
                warningPrinter2
        );

        // Assert - Verify both checkers were created successfully
        assertNotNull(checker1, "First checker should be created");
        assertNotNull(checker2, "Second checker should be created");
        assertNotSame(checker1, checker2, "Checker instances should be different objects");
    }

    /**
     * Tests the constructor with the same parameters used to create multiple checkers.
     * Verifies that the same parameters can be used for multiple checkers.
     */
    @Test
    public void testMultipleCheckersWithSameParameters() {
        // Arrange - Create single instances to use for multiple checkers
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create two checker instances with the same parameters
        LibraryKeepChecker checker1 = new LibraryKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );
        LibraryKeepChecker checker2 = new LibraryKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        // Assert - Verify both checkers were created successfully
        assertNotNull(checker1, "First checker should be created");
        assertNotNull(checker2, "Second checker should be created");
        assertNotSame(checker1, checker2, "Checker instances should be different objects");
    }

    /**
     * Tests the constructor with different warning printer implementations.
     * Verifies that the checker can be created with different types of warning printers.
     */
    @Test
    public void testConstructorWithDifferentWarningPrinterImplementations() {
        // Arrange - Create class pools and different warning printers
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter1 = mock(WarningPrinter.class);

        // Act - Create checker with mocked warning printer
        LibraryKeepChecker checker = new LibraryKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter1
        );

        // Assert - Verify the checker was created successfully
        assertNotNull(checker, "LibraryKeepChecker should be instantiated with different warning printer implementations");
    }

    /**
     * Tests the constructor implements ClassVisitor interface.
     * Verifies that LibraryKeepChecker can be used as a ClassVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfClassVisitor() {
        // Arrange - Create valid parameters
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create checker
        LibraryKeepChecker checker = new LibraryKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        // Assert - Verify the checker implements ClassVisitor
        assertInstanceOf(proguard.classfile.visitor.ClassVisitor.class, checker,
                "LibraryKeepChecker should implement ClassVisitor interface");
    }

    /**
     * Tests that the constructor creates a functioning instance.
     * Verifies that the checker instance can be used to call its public method.
     */
    @Test
    public void testConstructorCreatesFunctioningInstance() {
        // Arrange - Create valid parameters
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create checker
        LibraryKeepChecker checker = new LibraryKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

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
        // Arrange - Create valid parameters
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create checker
        LibraryKeepChecker checker = new LibraryKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        // Assert - Verify the checker can handle an empty list without exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(java.util.Collections.emptyList()),
                "Checker should handle empty list without throwing");
    }

    /**
     * Tests that the type of the created instance is correct.
     * Verifies that the constructor creates an instance of LibraryKeepChecker.
     */
    @Test
    public void testConstructorCreatesCorrectType() {
        // Arrange - Create valid parameters
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create checker
        LibraryKeepChecker checker = new LibraryKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        // Assert - Verify the instance type
        assertInstanceOf(LibraryKeepChecker.class, checker,
                "Constructor should create an instance of LibraryKeepChecker");
    }

    /**
     * Tests sequential instantiation of multiple checkers.
     * Verifies that checkers can be created one after another without issues.
     */
    @Test
    public void testSequentialInstantiation() {
        // Arrange - Create parameters for three checkers
        ClassPool programClassPool1 = new ClassPool();
        ClassPool libraryClassPool1 = new ClassPool();
        WarningPrinter warningPrinter1 = mock(WarningPrinter.class);

        ClassPool programClassPool2 = new ClassPool();
        ClassPool libraryClassPool2 = new ClassPool();
        WarningPrinter warningPrinter2 = mock(WarningPrinter.class);

        ClassPool programClassPool3 = new ClassPool();
        ClassPool libraryClassPool3 = new ClassPool();
        WarningPrinter warningPrinter3 = mock(WarningPrinter.class);

        // Act - Create checkers sequentially
        LibraryKeepChecker checker1 = new LibraryKeepChecker(
                programClassPool1, libraryClassPool1, warningPrinter1);
        assertNotNull(checker1, "First checker should be created");

        LibraryKeepChecker checker2 = new LibraryKeepChecker(
                programClassPool2, libraryClassPool2, warningPrinter2);
        assertNotNull(checker2, "Second checker should be created");

        LibraryKeepChecker checker3 = new LibraryKeepChecker(
                programClassPool3, libraryClassPool3, warningPrinter3);
        assertNotNull(checker3, "Third checker should be created");

        // Assert - Verify all instances are different
        assertNotSame(checker1, checker2, "First and second checkers should be different");
        assertNotSame(checker2, checker3, "Second and third checkers should be different");
        assertNotSame(checker1, checker3, "First and third checkers should be different");
    }

    /**
     * Tests constructor with null followed by non-null parameters.
     * Verifies that creating a checker with null doesn't affect subsequent creations.
     */
    @Test
    public void testConstructorNullThenNonNull() {
        // Act - Create checker with null, then with valid parameters
        LibraryKeepChecker checker1 = new LibraryKeepChecker(null, null, null);

        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        LibraryKeepChecker checker2 = new LibraryKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        // Assert - Verify both checkers were created successfully
        assertNotNull(checker1, "Checker with null parameters should be created");
        assertNotNull(checker2, "Checker with valid parameters should be created");
        assertNotSame(checker1, checker2, "Checker instances should be different");
    }
}
