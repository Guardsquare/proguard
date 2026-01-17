package proguard;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.util.WarningPrinter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DescriptorKeepChecker#checkClassSpecifications(List)}.
 * Tests the checkClassSpecifications method with various List configurations.
 */
public class DescriptorKeepCheckerClaude_checkClassSpecificationsTest {

    /**
     * Tests checkClassSpecifications with an empty list.
     * Verifies that the method handles an empty list without errors.
     */
    @Test
    public void testCheckClassSpecifications_withEmptyList() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );
        List<KeepClassSpecification> emptyList = new ArrayList<>();

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(emptyList));
    }

    /**
     * Tests checkClassSpecifications with a null list.
     * Verifies behavior when null is passed.
     */
    @Test
    public void testCheckClassSpecifications_withNullList() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        // Act & Assert - may throw NullPointerException or handle gracefully
        assertDoesNotThrow(() -> checker.checkClassSpecifications(null));
    }

    /**
     * Tests checkClassSpecifications with a single keep specification.
     * Verifies that the method processes a list with one specification.
     */
    @Test
    public void testCheckClassSpecifications_withSingleSpecification() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        List<KeepClassSpecification> specifications = new ArrayList<>();
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true,  // markClasses
                false, // markClassMembers
                false, // markConditionally
                false, // markDescriptorClasses
                false, // markCodeAttributes
                false, // allowShrinking
                false, // allowOptimization
                false, // allowObfuscation
                null,  // condition
                classSpec
        );
        specifications.add(keepSpec);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specifications));
    }

    /**
     * Tests checkClassSpecifications with multiple keep specifications.
     * Verifies that the method processes a list with multiple specifications.
     */
    @Test
    public void testCheckClassSpecifications_withMultipleSpecifications() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        List<KeepClassSpecification> specifications = new ArrayList<>();

        // Add first specification
        ClassSpecification classSpec1 = new ClassSpecification();
        KeepClassSpecification keepSpec1 = new KeepClassSpecification(
                true, false, false, false, false, false, false, false,
                null, classSpec1
        );
        specifications.add(keepSpec1);

        // Add second specification
        ClassSpecification classSpec2 = new ClassSpecification();
        KeepClassSpecification keepSpec2 = new KeepClassSpecification(
                false, true, false, false, false, false, false, false,
                null, classSpec2
        );
        specifications.add(keepSpec2);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specifications));
    }

    /**
     * Tests checkClassSpecifications with empty class pools.
     * Verifies that the method handles empty pools correctly.
     */
    @Test
    public void testCheckClassSpecifications_withEmptyClassPools() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        List<KeepClassSpecification> specifications = new ArrayList<>();
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, false, false, false, false, false, false,
                null, classSpec
        );
        specifications.add(keepSpec);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specifications));
    }

    /**
     * Tests checkClassSpecifications with null program class pool.
     * Verifies behavior when program class pool is null.
     */
    @Test
    public void testCheckClassSpecifications_withNullProgramClassPool() {
        // Arrange
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                null,
                libraryClassPool,
                warningPrinter
        );

        List<KeepClassSpecification> specifications = new ArrayList<>();
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, false, false, false, false, false, false,
                null, classSpec
        );
        specifications.add(keepSpec);

        // Act & Assert - may throw NullPointerException
        assertThrows(NullPointerException.class,
                () -> checker.checkClassSpecifications(specifications));
    }

    /**
     * Tests checkClassSpecifications with null library class pool.
     * Verifies behavior when library class pool is null.
     */
    @Test
    public void testCheckClassSpecifications_withNullLibraryClassPool() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                null,
                warningPrinter
        );

        List<KeepClassSpecification> specifications = new ArrayList<>();
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, false, false, false, false, false, false,
                null, classSpec
        );
        specifications.add(keepSpec);

        // Act & Assert - may throw NullPointerException
        assertThrows(NullPointerException.class,
                () -> checker.checkClassSpecifications(specifications));
    }

    /**
     * Tests checkClassSpecifications can be called multiple times.
     * Verifies that the method can be invoked multiple times on the same instance.
     */
    @Test
    public void testCheckClassSpecifications_multipleInvocations() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        List<KeepClassSpecification> specifications = new ArrayList<>();
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, false, false, false, false, false, false, false,
                null, classSpec
        );
        specifications.add(keepSpec);

        // Act & Assert - should not throw any exception on multiple calls
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specifications));
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specifications));
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specifications));
    }

    /**
     * Tests checkClassSpecifications with specification marking class members.
     * Verifies that specifications with markClassMembers=true work correctly.
     */
    @Test
    public void testCheckClassSpecifications_withMarkClassMembers() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        List<KeepClassSpecification> specifications = new ArrayList<>();
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keepSpec = new KeepClassSpecification(
                false, // markClasses
                true,  // markClassMembers
                false, // markConditionally
                false, // markDescriptorClasses
                false, // markCodeAttributes
                false, // allowShrinking
                false, // allowOptimization
                false, // allowObfuscation
                null,  // condition
                classSpec
        );
        specifications.add(keepSpec);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specifications));
    }

    /**
     * Tests checkClassSpecifications with specification marking descriptor classes.
     * Verifies that specifications with markDescriptorClasses=true work correctly.
     */
    @Test
    public void testCheckClassSpecifications_withMarkDescriptorClasses() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        List<KeepClassSpecification> specifications = new ArrayList<>();
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true,  // markClasses
                false, // markClassMembers
                false, // markConditionally
                true,  // markDescriptorClasses
                false, // markCodeAttributes
                false, // allowShrinking
                false, // allowOptimization
                false, // allowObfuscation
                null,  // condition
                classSpec
        );
        specifications.add(keepSpec);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specifications));
    }

    /**
     * Tests checkClassSpecifications with specification allowing shrinking.
     * Verifies that specifications with allowShrinking=true work correctly.
     */
    @Test
    public void testCheckClassSpecifications_withAllowShrinking() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        List<KeepClassSpecification> specifications = new ArrayList<>();
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true,  // markClasses
                false, // markClassMembers
                false, // markConditionally
                false, // markDescriptorClasses
                false, // markCodeAttributes
                true,  // allowShrinking
                false, // allowOptimization
                false, // allowObfuscation
                null,  // condition
                classSpec
        );
        specifications.add(keepSpec);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specifications));
    }

    /**
     * Tests checkClassSpecifications with all boolean flags set to true.
     * Verifies that specifications with all options enabled work correctly.
     */
    @Test
    public void testCheckClassSpecifications_withAllFlagsTrue() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        List<KeepClassSpecification> specifications = new ArrayList<>();
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true, // markClasses
                true, // markClassMembers
                true, // markConditionally
                true, // markDescriptorClasses
                true, // markCodeAttributes
                true, // allowShrinking
                true, // allowOptimization
                true, // allowObfuscation
                null, // condition
                classSpec
        );
        specifications.add(keepSpec);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specifications));
    }

    /**
     * Tests checkClassSpecifications with all boolean flags set to false.
     * Verifies that specifications with all options disabled work correctly.
     */
    @Test
    public void testCheckClassSpecifications_withAllFlagsFalse() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        List<KeepClassSpecification> specifications = new ArrayList<>();
        ClassSpecification classSpec = new ClassSpecification();
        KeepClassSpecification keepSpec = new KeepClassSpecification(
                false, // markClasses
                false, // markClassMembers
                false, // markConditionally
                false, // markDescriptorClasses
                false, // markCodeAttributes
                false, // allowShrinking
                false, // allowOptimization
                false, // allowObfuscation
                null,  // condition
                classSpec
        );
        specifications.add(keepSpec);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specifications));
    }

    /**
     * Tests checkClassSpecifications with a specification that has a condition.
     * Verifies that conditional specifications work correctly.
     */
    @Test
    public void testCheckClassSpecifications_withCondition() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        List<KeepClassSpecification> specifications = new ArrayList<>();
        ClassSpecification classSpec = new ClassSpecification();
        ClassSpecification condition = new ClassSpecification();
        KeepClassSpecification keepSpec = new KeepClassSpecification(
                true,  // markClasses
                false, // markClassMembers
                true,  // markConditionally
                false, // markDescriptorClasses
                false, // markCodeAttributes
                false, // allowShrinking
                false, // allowOptimization
                false, // allowObfuscation
                condition, // condition
                classSpec
        );
        specifications.add(keepSpec);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specifications));
    }

    /**
     * Tests checkClassSpecifications with mixed specification configurations.
     * Verifies that a heterogeneous list of specifications is processed correctly.
     */
    @Test
    public void testCheckClassSpecifications_withMixedSpecifications() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        WarningPrinter warningPrinter = mock(WarningPrinter.class);
        DescriptorKeepChecker checker = new DescriptorKeepChecker(
                programClassPool,
                libraryClassPool,
                warningPrinter
        );

        List<KeepClassSpecification> specifications = new ArrayList<>();

        // Add specification with markClasses only
        specifications.add(new KeepClassSpecification(
                true, false, false, false, false, false, false, false,
                null, new ClassSpecification()
        ));

        // Add specification with markClassMembers only
        specifications.add(new KeepClassSpecification(
                false, true, false, false, false, false, false, false,
                null, new ClassSpecification()
        ));

        // Add specification with allowShrinking
        specifications.add(new KeepClassSpecification(
                true, false, false, false, false, true, false, false,
                null, new ClassSpecification()
        ));

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> checker.checkClassSpecifications(specifications));
    }
}
