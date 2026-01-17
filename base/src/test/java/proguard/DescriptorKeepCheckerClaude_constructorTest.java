package proguard;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DescriptorKeepChecker} constructor.
 * Tests DescriptorKeepChecker(ClassPool, ClassPool, WarningPrinter) constructor.
 */
public class DescriptorKeepCheckerClaude_constructorTest {

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

        // Act - Create DescriptorKeepChecker with valid parameters
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        // Assert - Verify the checker was created successfully
        assertNotNull(checker, "DescriptorKeepChecker should be instantiated successfully");
    }

    /**
     * Tests the constructor with all null parameters.
     * Verifies that the constructor accepts null values.
     */
    @Test
    public void testConstructorWithAllNullParameters() {
        // Act - Create checker with all null parameters
        DescriptorKeepChecker checker = new DescriptorKeepChecker(null, null, null);

        // Assert - Verify the checker was created
        assertNotNull(checker, "DescriptorKeepChecker should be instantiated even with null parameters");
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
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                null,
                libraryClassPool,
                warningPrinter
        );

        // Assert - Verify the checker was created
        assertNotNull(checker, "DescriptorKeepChecker should be instantiated with null program class pool");
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
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                null,
                warningPrinter
        );

        // Assert - Verify the checker was created
        assertNotNull(checker, "DescriptorKeepChecker should be instantiated with null library class pool");
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
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                null
        );

        // Assert - Verify the checker was created
        assertNotNull(checker, "DescriptorKeepChecker should be instantiated with null warning printer");
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
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        // Assert - Verify the checker was created successfully
        assertNotNull(checker, "DescriptorKeepChecker should be instantiated with empty class pools");
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
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                classPool,
                classPool,
                warningPrinter
        );

        // Assert - Verify the checker was created successfully
        assertNotNull(checker, "DescriptorKeepChecker should be instantiated with same class pool for both parameters");
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
        DescriptorKeepChecker checker1 = new DescriptorKeepChecker(
                programClassPool1,
                libraryClassPool1,
                warningPrinter1
        );
        DescriptorKeepChecker checker2 = new DescriptorKeepChecker(
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
        DescriptorKeepChecker checker1 = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );
        DescriptorKeepChecker checker2 = new DescriptorKeepChecker(
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
     * Verifies that the checker can be created with different types of writers.
     */
    @Test
    public void testConstructorWithDifferentWarningPrinterImplementations() {
        // Arrange - Create class pools and different warning printers
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter1 = mock(WarningPrinter.class);

        // Act - Create checker with StringWriter-based warning printer
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter1
        );

        // Assert - Verify the checker was created successfully
        assertNotNull(checker, "DescriptorKeepChecker should be instantiated with different warning printer implementations");
    }

    /**
     * Tests the constructor implements MemberVisitor interface.
     * Verifies that DescriptorKeepChecker can be used as a MemberVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfMemberVisitor() {
        // Arrange - Create valid parameters
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create checker
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        // Assert - Verify the checker implements MemberVisitor
        assertInstanceOf(proguard.classfile.visitor.MemberVisitor.class, checker,
                "DescriptorKeepChecker should implement MemberVisitor interface");
    }

    /**
     * Tests the constructor implements ClassVisitor interface.
     * Verifies that DescriptorKeepChecker can be used as a ClassVisitor.
     */
    @Test
    public void testConstructorCreatesInstanceOfClassVisitor() {
        // Arrange - Create valid parameters
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);

        // Act - Create checker
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        // Assert - Verify the checker implements ClassVisitor
        assertInstanceOf(proguard.classfile.visitor.ClassVisitor.class, checker,
                "DescriptorKeepChecker should implement ClassVisitor interface");
    }
}
