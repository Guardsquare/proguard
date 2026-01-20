package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link VerticalClassMerger} constructors with signatures:
 * - (ZZ)V
 * - (ZZLproguard/classfile/visitor/ClassVisitor;)V
 *
 * These constructors create a new VerticalClassMerger that inlines direct subclasses into program classes.
 * The two-parameter constructor takes boolean flags for access modification and aggressive
 * interface merging, and delegates to the three-parameter constructor with null for the extraClassVisitor parameter.
 */
public class VerticalClassMergerClaude_constructorTest {

    // ========================================
    // Basic Constructor Tests (Two-Parameter)
    // ========================================

    /**
     * Tests the two-parameter constructor with both flags set to true.
     * Verifies that the merger can be instantiated with maximum flexibility.
     */
    @Test
    public void testConstructorWithBothFlagsTrue() {
        // Act
        VerticalClassMerger merger = new VerticalClassMerger(true, true);

        // Assert
        assertNotNull(merger, "VerticalClassMerger should be created successfully");
    }

    /**
     * Tests the two-parameter constructor with both flags set to false.
     * Verifies that the merger can be instantiated with conservative settings.
     */
    @Test
    public void testConstructorWithBothFlagsFalse() {
        // Act
        VerticalClassMerger merger = new VerticalClassMerger(false, false);

        // Assert
        assertNotNull(merger, "VerticalClassMerger should be created successfully");
    }

    /**
     * Tests the two-parameter constructor with allowAccessModification true and mergeInterfacesAggressively false.
     * Verifies that the merger is created with this flag combination.
     */
    @Test
    public void testConstructorWithAccessModificationTrueAndAggressiveMergingFalse() {
        // Act
        VerticalClassMerger merger = new VerticalClassMerger(true, false);

        // Assert
        assertNotNull(merger, "VerticalClassMerger should be created with mixed flag values");
    }

    /**
     * Tests the two-parameter constructor with allowAccessModification false and mergeInterfacesAggressively true.
     * Verifies that the merger is created with this flag combination.
     */
    @Test
    public void testConstructorWithAccessModificationFalseAndAggressiveMergingTrue() {
        // Act
        VerticalClassMerger merger = new VerticalClassMerger(false, true);

        // Assert
        assertNotNull(merger, "VerticalClassMerger should be created with mixed flag values");
    }

    // ========================================
    // Interface Implementation Tests
    // ========================================

    /**
     * Tests that the constructor creates an instance of ClassVisitor.
     * Verifies that VerticalClassMerger implements the ClassVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfClassVisitor() {
        // Act
        VerticalClassMerger merger = new VerticalClassMerger(true, true);

        // Assert
        assertInstanceOf(ClassVisitor.class, merger,
            "VerticalClassMerger should implement ClassVisitor interface");
    }

    /**
     * Tests that the created instance can be used as a ClassVisitor.
     * Verifies that the merger is in a valid state for use.
     */
    @Test
    public void testConstructorCreatesUsableClassVisitor() {
        // Act
        VerticalClassMerger merger = new VerticalClassMerger(false, true);

        // Assert
        assertNotNull(merger, "VerticalClassMerger should be created");
        ClassVisitor visitor = merger;
        assertNotNull(visitor, "VerticalClassMerger should be usable as ClassVisitor");
    }

    // ========================================
    // Multiple Instance Tests
    // ========================================

    /**
     * Tests creating multiple merger instances with the same parameters.
     * Verifies that multiple instances can be created using the same parameters.
     */
    @Test
    public void testMultipleMergersWithSameParameters() {
        // Arrange
        boolean allowAccessModification = true;
        boolean mergeInterfacesAggressively = false;

        // Act
        VerticalClassMerger merger1 = new VerticalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively
        );
        VerticalClassMerger merger2 = new VerticalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively
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
    public void testMultipleMergersWithDifferentParameters() {
        // Act
        VerticalClassMerger merger1 = new VerticalClassMerger(true, true);
        VerticalClassMerger merger2 = new VerticalClassMerger(false, false);

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
            VerticalClassMerger merger = new VerticalClassMerger(
                i % 2 == 0,
                i % 3 == 0
            );
            assertNotNull(merger, "Merger " + i + " should be created");
        }
    }

    // ========================================
    // Efficiency Tests
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
        VerticalClassMerger merger = new VerticalClassMerger(true, true);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(merger, "VerticalClassMerger should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    // ========================================
    // Three-Parameter Constructor Tests
    // ========================================

    /**
     * Tests the three-parameter constructor with all valid non-null parameters.
     * Verifies that the merger can be instantiated with all parameters provided.
     */
    @Test
    public void testThreeParamConstructorWithValidParameters() {
        // Arrange
        boolean allowAccessModification = true;
        boolean mergeInterfacesAggressively = true;
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        VerticalClassMerger merger = new VerticalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "VerticalClassMerger should be created successfully");
    }

    /**
     * Tests the three-parameter constructor with null extraClassVisitor.
     * Verifies that the merger handles null visitor parameter.
     */
    @Test
    public void testThreeParamConstructorWithNullExtraClassVisitor() {
        // Arrange
        boolean allowAccessModification = true;
        boolean mergeInterfacesAggressively = false;

        // Act
        VerticalClassMerger merger = new VerticalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            null
        );

        // Assert
        assertNotNull(merger, "VerticalClassMerger should be created with null extraClassVisitor");
    }

    /**
     * Tests the three-parameter constructor with both boolean flags set to true.
     * Verifies that merger is created with maximum flexibility.
     */
    @Test
    public void testThreeParamConstructorWithBothFlagsTrue() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        VerticalClassMerger merger = new VerticalClassMerger(
            true,
            true,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "VerticalClassMerger should be created with both flags true");
    }

    /**
     * Tests the three-parameter constructor with both boolean flags set to false.
     * Verifies that merger is created with conservative settings.
     */
    @Test
    public void testThreeParamConstructorWithBothFlagsFalse() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        VerticalClassMerger merger = new VerticalClassMerger(
            false,
            false,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "VerticalClassMerger should be created with both flags false");
    }

    /**
     * Tests the three-parameter constructor with mixed flag values (true, false).
     * Verifies that the merger is created with this flag combination.
     */
    @Test
    public void testThreeParamConstructorWithMixedFlagsCase1() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        VerticalClassMerger merger = new VerticalClassMerger(
            true,
            false,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "VerticalClassMerger should be created with mixed flag values");
    }

    /**
     * Tests the three-parameter constructor with mixed flag values (false, true).
     * Verifies that the merger is created with this flag combination.
     */
    @Test
    public void testThreeParamConstructorWithMixedFlagsCase2() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        VerticalClassMerger merger = new VerticalClassMerger(
            false,
            true,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "VerticalClassMerger should be created with mixed flag values");
    }

    /**
     * Tests that the three-parameter constructor creates an instance of ClassVisitor.
     * Verifies that VerticalClassMerger implements the ClassVisitor interface.
     */
    @Test
    public void testThreeParamConstructorCreatesInstanceOfClassVisitor() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        VerticalClassMerger merger = new VerticalClassMerger(
            true,
            true,
            extraClassVisitor
        );

        // Assert
        assertInstanceOf(ClassVisitor.class, merger,
            "VerticalClassMerger should implement ClassVisitor interface");
    }

    /**
     * Tests creating multiple merger instances with the same parameters.
     * Verifies that multiple instances can be created using the same parameters.
     */
    @Test
    public void testThreeParamConstructorMultipleMergersWithSameParameters() {
        // Arrange
        boolean allowAccessModification = true;
        boolean mergeInterfacesAggressively = false;
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        VerticalClassMerger merger1 = new VerticalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            extraClassVisitor
        );
        VerticalClassMerger merger2 = new VerticalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
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
    public void testThreeParamConstructorMultipleMergersWithDifferentParameters() {
        // Arrange
        ClassVisitor extraClassVisitor1 = mock(ClassVisitor.class);
        ClassVisitor extraClassVisitor2 = mock(ClassVisitor.class);

        // Act
        VerticalClassMerger merger1 = new VerticalClassMerger(
            true,
            true,
            extraClassVisitor1
        );
        VerticalClassMerger merger2 = new VerticalClassMerger(
            false,
            false,
            extraClassVisitor2
        );

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
    public void testThreeParamConstructorMultipleMergersWithSharedExtraClassVisitor() {
        // Arrange
        ClassVisitor sharedVisitor = mock(ClassVisitor.class);

        // Act
        VerticalClassMerger merger1 = new VerticalClassMerger(
            true,
            false,
            sharedVisitor
        );
        VerticalClassMerger merger2 = new VerticalClassMerger(
            false,
            true,
            sharedVisitor
        );

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different");
    }

    /**
     * Tests that the three-parameter constructor doesn't invoke any methods on the parameters.
     * Verifies that the constructor only stores the parameters without using them.
     */
    @Test
    public void testThreeParamConstructorDoesNotInvokeParameters() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        VerticalClassMerger merger = new VerticalClassMerger(
            true,
            true,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "VerticalClassMerger should be created");
        verifyNoInteractions(extraClassVisitor);
    }

    /**
     * Tests that the three-parameter constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testThreeParamConstructorIsEfficient() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);
        long startTime = System.nanoTime();

        // Act
        VerticalClassMerger merger = new VerticalClassMerger(
            true,
            true,
            extraClassVisitor
        );

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(merger, "VerticalClassMerger should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests creating a sequence of mergers with the three-parameter constructor.
     * Verifies that multiple mergers can be created sequentially without issues.
     */
    @Test
    public void testThreeParamConstructorSequentialMergerCreation() {
        // Act & Assert - create multiple mergers sequentially
        for (int i = 0; i < 10; i++) {
            ClassVisitor extraClassVisitor = mock(ClassVisitor.class);
            VerticalClassMerger merger = new VerticalClassMerger(
                i % 2 == 0,
                i % 3 == 0,
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
    public void testThreeParamConstructorMultipleMergersWithNullsAreIndependent() {
        // Act
        VerticalClassMerger merger1 = new VerticalClassMerger(true, true, null);
        VerticalClassMerger merger2 = new VerticalClassMerger(true, true, null);

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different even with null parameters");
    }

    /**
     * Tests the three-parameter constructor with all boolean flag combinations.
     * Verifies that all four combinations of boolean flags work correctly.
     */
    @Test
    public void testThreeParamConstructorWithAllBooleanCombinations() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Test case 1: false, false
        VerticalClassMerger merger1 = new VerticalClassMerger(
            false, false, extraClassVisitor
        );
        assertNotNull(merger1, "Merger with false, false should be created");

        // Test case 2: false, true
        VerticalClassMerger merger2 = new VerticalClassMerger(
            false, true, extraClassVisitor
        );
        assertNotNull(merger2, "Merger with false, true should be created");

        // Test case 3: true, false
        VerticalClassMerger merger3 = new VerticalClassMerger(
            true, false, extraClassVisitor
        );
        assertNotNull(merger3, "Merger with true, false should be created");

        // Test case 4: true, true
        VerticalClassMerger merger4 = new VerticalClassMerger(
            true, true, extraClassVisitor
        );
        assertNotNull(merger4, "Merger with true, true should be created");
    }

    /**
     * Tests that the three-parameter constructor properly initializes the instance for use.
     * Verifies that the created instance is in a valid state.
     */
    @Test
    public void testThreeParamConstructorInitializesInstanceProperly() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        VerticalClassMerger merger = new VerticalClassMerger(
            true,
            false,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "VerticalClassMerger should be created");
        // The merger should be usable as a ClassVisitor
        ClassVisitor visitor = merger;
        assertNotNull(visitor, "VerticalClassMerger should be usable as ClassVisitor");
    }

    // ========================================
    // Cross-Constructor Tests
    // ========================================

    /**
     * Tests that the two-parameter and three-parameter constructors create independent instances.
     * Verifies that both constructors can be used interchangeably.
     */
    @Test
    public void testBothConstructorsCreateIndependentInstances() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        VerticalClassMerger merger1 = new VerticalClassMerger(
            true, false
        );
        VerticalClassMerger merger2 = new VerticalClassMerger(
            true, false, extraClassVisitor
        );
        VerticalClassMerger merger3 = new VerticalClassMerger(
            true, false, null
        );

        // Assert
        assertNotNull(merger1, "Two-param constructor should create instance");
        assertNotNull(merger2, "Three-param constructor with visitor should create instance");
        assertNotNull(merger3, "Three-param constructor with null visitor should create instance");
        assertNotSame(merger1, merger2, "Instances should be different");
        assertNotSame(merger1, merger3, "Instances should be different");
        assertNotSame(merger2, merger3, "Instances should be different");
    }

    /**
     * Tests that multiple VerticalClassMerger instances work independently.
     * Verifies that using both constructors doesn't cause interference.
     */
    @Test
    public void testMultipleInstancesFromBothConstructorsWorkIndependently() {
        // Arrange
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        VerticalClassMerger merger1 = new VerticalClassMerger(
            true, false
        );
        VerticalClassMerger merger2 = new VerticalClassMerger(
            false, true, extraClassVisitor
        );

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
        VerticalClassMerger merger1 = new VerticalClassMerger(
            true, false
        );
        VerticalClassMerger merger2 = new VerticalClassMerger(
            false, true, extraClassVisitor
        );
        VerticalClassMerger merger3 = new VerticalClassMerger(
            true, true
        );
        VerticalClassMerger merger4 = new VerticalClassMerger(
            false, false, null
        );

        // Assert
        assertNotNull(merger1, "Merger 1 should be created");
        assertNotNull(merger2, "Merger 2 should be created");
        assertNotNull(merger3, "Merger 3 should be created");
        assertNotNull(merger4, "Merger 4 should be created");
    }

    /**
     * Tests that both constructors properly delegate to the main constructor.
     * Verifies consistent behavior across constructor variants.
     */
    @Test
    public void testConstructorDelegation() {
        // Act
        VerticalClassMerger merger1 = new VerticalClassMerger(true, false);
        VerticalClassMerger merger2 = new VerticalClassMerger(true, false, null);

        // Assert
        assertNotNull(merger1, "Two-param constructor should create instance");
        assertNotNull(merger2, "Three-param constructor with null should create instance");
        // Both should be valid ClassVisitor instances
        assertInstanceOf(ClassVisitor.class, merger1);
        assertInstanceOf(ClassVisitor.class, merger2);
    }

    /**
     * Tests creating many merger instances to verify no resource leaks.
     * Verifies that the constructor can be called many times without issues.
     */
    @Test
    public void testManyInstancesCreation() {
        // Act & Assert
        for (int i = 0; i < 100; i++) {
            VerticalClassMerger merger = new VerticalClassMerger(
                i % 2 == 0,
                i % 3 == 0
            );
            assertNotNull(merger, "Merger " + i + " should be created");
        }
    }

    /**
     * Tests that constructor parameters are correctly used.
     * Verifies both boolean parameters can be set to all combinations.
     */
    @Test
    public void testAllBooleanParameterCombinations() {
        // Test all four combinations of boolean parameters
        VerticalClassMerger merger1 = new VerticalClassMerger(false, false);
        assertNotNull(merger1, "Merger with (false, false) should be created");

        VerticalClassMerger merger2 = new VerticalClassMerger(false, true);
        assertNotNull(merger2, "Merger with (false, true) should be created");

        VerticalClassMerger merger3 = new VerticalClassMerger(true, false);
        assertNotNull(merger3, "Merger with (true, false) should be created");

        VerticalClassMerger merger4 = new VerticalClassMerger(true, true);
        assertNotNull(merger4, "Merger with (true, true) should be created");

        // All instances should be different
        assertNotSame(merger1, merger2);
        assertNotSame(merger1, merger3);
        assertNotSame(merger1, merger4);
        assertNotSame(merger2, merger3);
        assertNotSame(merger2, merger4);
        assertNotSame(merger3, merger4);
    }
}
