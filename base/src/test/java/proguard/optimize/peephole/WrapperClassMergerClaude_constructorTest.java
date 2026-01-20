package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link WrapperClassMerger} constructors with signatures:
 * - (Z)V
 * - (ZLproguard/classfile/visitor/ClassVisitor;)V
 *
 * These constructors create a new WrapperClassMerger that inlines wrapper classes into their
 * wrapped classes. The single-parameter constructor takes a boolean flag for access modification
 * and delegates to the two-parameter constructor with null for the extraClassVisitor parameter.
 */
public class WrapperClassMergerClaude_constructorTest {

    // ========================================
    // Basic Constructor Tests
    // ========================================

    /**
     * Tests the constructor with allowAccessModification set to true.
     * Verifies that the merger can be instantiated with access modification allowed.
     */
    @Test
    public void testConstructorWithAllowAccessModificationTrue() {
        // Arrange & Act
        WrapperClassMerger merger = new WrapperClassMerger(true);

        // Assert
        assertNotNull(merger, "WrapperClassMerger should be created successfully");
    }

    /**
     * Tests the constructor with allowAccessModification set to false.
     * Verifies that the merger can be instantiated with access modification disallowed.
     */
    @Test
    public void testConstructorWithAllowAccessModificationFalse() {
        // Arrange & Act
        WrapperClassMerger merger = new WrapperClassMerger(false);

        // Assert
        assertNotNull(merger, "WrapperClassMerger should be created successfully");
    }

    // ========================================
    // Interface Implementation Tests
    // ========================================

    /**
     * Tests that the constructor creates an instance of ClassVisitor.
     * Verifies that WrapperClassMerger implements the ClassVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfClassVisitor() {
        // Arrange & Act
        WrapperClassMerger merger = new WrapperClassMerger(true);

        // Assert
        assertInstanceOf(ClassVisitor.class, merger,
            "WrapperClassMerger should implement ClassVisitor interface");
    }

    /**
     * Tests that the created instance can be used as a ClassVisitor.
     * Verifies that the merger is in a valid state for use.
     */
    @Test
    public void testConstructorCreatesUsableClassVisitor() {
        // Arrange & Act
        WrapperClassMerger merger = new WrapperClassMerger(false);

        // Assert
        assertNotNull(merger, "WrapperClassMerger should be created");
        ClassVisitor visitor = merger;
        assertNotNull(visitor, "WrapperClassMerger should be usable as ClassVisitor");
    }

    // ========================================
    // Multiple Instance Tests
    // ========================================

    /**
     * Tests creating multiple merger instances with the same parameter.
     * Verifies that multiple instances can be created using the same parameters.
     */
    @Test
    public void testMultipleMergersWithSameParameter() {
        // Arrange & Act
        WrapperClassMerger merger1 = new WrapperClassMerger(true);
        WrapperClassMerger merger2 = new WrapperClassMerger(true);

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different");
    }

    /**
     * Tests creating multiple merger instances with different parameters.
     * Verifies that mergers can be created independently with different configurations.
     */
    @Test
    public void testMultipleMergersWithDifferentParameters() {
        // Arrange & Act
        WrapperClassMerger merger1 = new WrapperClassMerger(true);
        WrapperClassMerger merger2 = new WrapperClassMerger(false);

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different");
    }

    /**
     * Tests creating a sequence of mergers.
     * Verifies that multiple mergers can be created sequentially without issues.
     */
    @Test
    public void testSequentialMergerCreation() {
        // Act & Assert - create multiple mergers sequentially
        for (int i = 0; i < 10; i++) {
            WrapperClassMerger merger = new WrapperClassMerger(i % 2 == 0);
            assertNotNull(merger, "Merger " + i + " should be created");
        }
    }

    // ========================================
    // Efficiency and Performance Tests
    // ========================================

    /**
     * Tests that the constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testConstructorIsEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        WrapperClassMerger merger = new WrapperClassMerger(true);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(merger, "WrapperClassMerger should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that the constructor properly initializes the instance for use.
     * Verifies that the created instance is in a valid state.
     */
    @Test
    public void testConstructorInitializesInstanceProperly() {
        // Arrange & Act
        WrapperClassMerger merger = new WrapperClassMerger(true);

        // Assert
        assertNotNull(merger, "WrapperClassMerger should be created");
        // The merger should be usable as a ClassVisitor
        ClassVisitor visitor = merger;
        assertNotNull(visitor, "WrapperClassMerger should be usable as ClassVisitor");
    }

    // ========================================
    // Two-Parameter Constructor Tests
    // ========================================

    /**
     * Tests the two-parameter constructor with all valid non-null parameters.
     * Verifies that the merger can be instantiated with all parameters provided.
     */
    @Test
    public void testTwoParamConstructorWithValidParameters() {
        // Arrange
        boolean allowAccessModification = true;
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        WrapperClassMerger merger = new WrapperClassMerger(
            allowAccessModification,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "WrapperClassMerger should be created successfully");
    }

    /**
     * Tests the two-parameter constructor with null extraClassVisitor.
     * Verifies that the merger handles null visitor parameter.
     */
    @Test
    public void testTwoParamConstructorWithNullExtraClassVisitor() {
        // Arrange
        boolean allowAccessModification = true;

        // Act
        WrapperClassMerger merger = new WrapperClassMerger(
            allowAccessModification,
            null
        );

        // Assert
        assertNotNull(merger, "WrapperClassMerger should be created with null extraClassVisitor");
    }

    /**
     * Tests the two-parameter constructor with allowAccessModification true.
     * Verifies that merger is created with access modification allowed.
     */
    @Test
    public void testTwoParamConstructorWithAccessModificationTrue() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        WrapperClassMerger merger = new WrapperClassMerger(true, extraClassVisitor);

        // Assert
        assertNotNull(merger, "WrapperClassMerger should be created with access modification true");
    }

    /**
     * Tests the two-parameter constructor with allowAccessModification false.
     * Verifies that merger is created with access modification disallowed.
     */
    @Test
    public void testTwoParamConstructorWithAccessModificationFalse() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        WrapperClassMerger merger = new WrapperClassMerger(false, extraClassVisitor);

        // Assert
        assertNotNull(merger, "WrapperClassMerger should be created with access modification false");
    }

    /**
     * Tests that the two-parameter constructor creates an instance of ClassVisitor.
     * Verifies that WrapperClassMerger implements the ClassVisitor interface.
     */
    @Test
    public void testTwoParamConstructorCreatesInstanceOfClassVisitor() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        WrapperClassMerger merger = new WrapperClassMerger(true, extraClassVisitor);

        // Assert
        assertInstanceOf(ClassVisitor.class, merger,
            "WrapperClassMerger should implement ClassVisitor interface");
    }

    /**
     * Tests creating multiple merger instances with the same parameters.
     * Verifies that multiple instances can be created using the same parameters.
     */
    @Test
    public void testTwoParamConstructorMultipleMergersWithSameParameters() {
        // Arrange
        boolean allowAccessModification = true;
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        WrapperClassMerger merger1 = new WrapperClassMerger(
            allowAccessModification,
            extraClassVisitor
        );
        WrapperClassMerger merger2 = new WrapperClassMerger(
            allowAccessModification,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different");
    }

    /**
     * Tests creating multiple merger instances with different parameters.
     * Verifies that mergers can be created independently with different configurations.
     */
    @Test
    public void testTwoParamConstructorMultipleMergersWithDifferentParameters() {
        // Arrange
        ClassVisitor extraClassVisitor1 = mock(ClassVisitor.class);
        ClassVisitor extraClassVisitor2 = mock(ClassVisitor.class);

        // Act
        WrapperClassMerger merger1 = new WrapperClassMerger(true, extraClassVisitor1);
        WrapperClassMerger merger2 = new WrapperClassMerger(false, extraClassVisitor2);

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different");
    }

    /**
     * Tests creating multiple merger instances with shared extraClassVisitor.
     * Verifies that mergers can share the same visitor instance.
     */
    @Test
    public void testTwoParamConstructorMultipleMergersWithSharedExtraClassVisitor() {
        // Arrange
        ClassVisitor sharedVisitor = mock(ClassVisitor.class);

        // Act
        WrapperClassMerger merger1 = new WrapperClassMerger(true, sharedVisitor);
        WrapperClassMerger merger2 = new WrapperClassMerger(false, sharedVisitor);

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different");
    }

    /**
     * Tests that the two-parameter constructor doesn't invoke any methods on the parameters.
     * Verifies that the constructor only stores the parameters without using them.
     */
    @Test
    public void testTwoParamConstructorDoesNotInvokeParameters() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        WrapperClassMerger merger = new WrapperClassMerger(true, extraClassVisitor);

        // Assert
        assertNotNull(merger, "WrapperClassMerger should be created");
        verifyNoInteractions(extraClassVisitor);
    }

    /**
     * Tests that the two-parameter constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testTwoParamConstructorIsEfficient() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);
        long startTime = System.nanoTime();

        // Act
        WrapperClassMerger merger = new WrapperClassMerger(true, extraClassVisitor);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(merger, "WrapperClassMerger should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests creating a sequence of mergers with the two-parameter constructor.
     * Verifies that multiple mergers can be created sequentially without issues.
     */
    @Test
    public void testTwoParamConstructorSequentialMergerCreation() {
        // Act & Assert - create multiple mergers sequentially
        for (int i = 0; i < 10; i++) {
            ClassVisitor extraClassVisitor = mock(ClassVisitor.class);
            WrapperClassMerger merger = new WrapperClassMerger(
                i % 2 == 0,
                extraClassVisitor
            );
            assertNotNull(merger, "Merger " + i + " should be created");
        }
    }

    /**
     * Tests that two mergers created with null extraClassVisitor are independent instances.
     * Verifies that even with null parameters, distinct instances are created.
     */
    @Test
    public void testTwoParamConstructorMultipleMergersWithNullsAreIndependent() {
        // Act
        WrapperClassMerger merger1 = new WrapperClassMerger(true, null);
        WrapperClassMerger merger2 = new WrapperClassMerger(true, null);

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different even with null parameters");
    }

    /**
     * Tests the two-parameter constructor with all boolean flag combinations.
     * Verifies that both combinations of boolean flag work correctly.
     */
    @Test
    public void testTwoParamConstructorWithAllBooleanCombinations() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Test case 1: false
        WrapperClassMerger merger1 = new WrapperClassMerger(false, extraClassVisitor);
        assertNotNull(merger1, "Merger with false should be created");

        // Test case 2: true
        WrapperClassMerger merger2 = new WrapperClassMerger(true, extraClassVisitor);
        assertNotNull(merger2, "Merger with true should be created");
    }

    /**
     * Tests the two-parameter constructor with various null combinations.
     * Verifies that both combinations of null/non-null parameters are handled.
     */
    @Test
    public void testTwoParamConstructorWithVariousNullCombinations() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Test case 1: non-null visitor
        WrapperClassMerger merger1 = new WrapperClassMerger(true, extraClassVisitor);
        assertNotNull(merger1, "Merger with non-null visitor should be created");

        // Test case 2: null visitor
        WrapperClassMerger merger2 = new WrapperClassMerger(false, null);
        assertNotNull(merger2, "Merger with null visitor should be created");
    }

    /**
     * Tests that the two-parameter constructor properly initializes the instance for use.
     * Verifies that the created instance is in a valid state.
     */
    @Test
    public void testTwoParamConstructorInitializesInstanceProperly() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        WrapperClassMerger merger = new WrapperClassMerger(true, extraClassVisitor);

        // Assert
        assertNotNull(merger, "WrapperClassMerger should be created");
        // The merger should be usable as a ClassVisitor
        ClassVisitor visitor = merger;
        assertNotNull(visitor, "WrapperClassMerger should be usable as ClassVisitor");
    }

    // ========================================
    // Cross-Constructor Tests
    // ========================================

    /**
     * Tests that the single-parameter and two-parameter constructors create independent instances.
     * Verifies that both constructors can be used interchangeably.
     */
    @Test
    public void testBothConstructorsCreateIndependentInstances() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        WrapperClassMerger merger1 = new WrapperClassMerger(true);
        WrapperClassMerger merger2 = new WrapperClassMerger(true, extraClassVisitor);
        WrapperClassMerger merger3 = new WrapperClassMerger(true, null);

        // Assert
        assertNotNull(merger1, "Single-param constructor should create instance");
        assertNotNull(merger2, "Two-param constructor with visitor should create instance");
        assertNotNull(merger3, "Two-param constructor with null visitor should create instance");
        assertNotSame(merger1, merger2, "Instances should be different");
        assertNotSame(merger1, merger3, "Instances should be different");
        assertNotSame(merger2, merger3, "Instances should be different");
    }

    /**
     * Tests that multiple WrapperClassMerger instances work independently.
     * Verifies that using both constructors doesn't cause interference.
     */
    @Test
    public void testMultipleInstancesFromBothConstructorsWorkIndependently() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        WrapperClassMerger merger1 = new WrapperClassMerger(true);
        WrapperClassMerger merger2 = new WrapperClassMerger(false, extraClassVisitor);

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different");
        assertInstanceOf(ClassVisitor.class, merger1, "First merger should implement ClassVisitor");
        assertInstanceOf(ClassVisitor.class, merger2, "Second merger should implement ClassVisitor");
    }

    /**
     * Tests creating mergers with alternating constructors.
     * Verifies that both constructors can be used in sequence without issues.
     */
    @Test
    public void testAlternatingConstructorUsage() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        WrapperClassMerger merger1 = new WrapperClassMerger(true);
        WrapperClassMerger merger2 = new WrapperClassMerger(false, extraClassVisitor);
        WrapperClassMerger merger3 = new WrapperClassMerger(true);
        WrapperClassMerger merger4 = new WrapperClassMerger(false, null);

        // Assert
        assertNotNull(merger1, "Merger 1 should be created");
        assertNotNull(merger2, "Merger 2 should be created");
        assertNotNull(merger3, "Merger 3 should be created");
        assertNotNull(merger4, "Merger 4 should be created");
    }
}
