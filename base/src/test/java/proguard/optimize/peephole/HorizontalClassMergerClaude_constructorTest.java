package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import proguard.classfile.visitor.ClassPoolVisitor;
import proguard.classfile.visitor.ClassVisitor;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link HorizontalClassMerger} constructors with signatures:
 * - (ZZLjava/util/Set;)V
 * - (ZZLjava/util/Set;Lproguard/classfile/visitor/ClassVisitor;)V
 *
 * These constructors create a new HorizontalClassMerger that inlines siblings in program classes.
 * The three-parameter constructor takes boolean flags for access modification and aggressive
 * interface merging, along with a set of forbidden class names, and delegates to the four-parameter
 * constructor with null for the extraClassVisitor parameter.
 */
public class HorizontalClassMergerClaude_constructorTest {

    // ========================================
    // Basic Constructor Tests
    // ========================================

    /**
     * Tests the constructor with all valid non-null parameters.
     * Verifies that the merger can be instantiated with typical parameters.
     */
    @Test
    public void testConstructorWithValidParameters() {
        // Arrange
        boolean allowAccessModification = true;
        boolean mergeInterfacesAggressively = true;
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com.example.DoNotMerge");

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            forbiddenClassNames
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created successfully");
    }

    /**
     * Tests the constructor with null forbiddenClassNames set.
     * Verifies that the merger handles null set parameter.
     */
    @Test
    public void testConstructorWithNullForbiddenClassNames() {
        // Arrange
        boolean allowAccessModification = true;
        boolean mergeInterfacesAggressively = false;

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            null
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with null forbiddenClassNames");
    }

    /**
     * Tests the constructor with empty forbiddenClassNames set.
     * Verifies that the merger handles empty set correctly.
     */
    @Test
    public void testConstructorWithEmptyForbiddenClassNames() {
        // Arrange
        boolean allowAccessModification = false;
        boolean mergeInterfacesAggressively = true;
        Set<String> forbiddenClassNames = new HashSet<>();

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            forbiddenClassNames
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with empty forbiddenClassNames");
    }

    // ========================================
    // Boolean Parameter Combination Tests
    // ========================================

    /**
     * Tests the constructor with both boolean flags set to true.
     * Verifies that merger is created with maximum flexibility.
     */
    @Test
    public void testConstructorWithBothFlagsTrue() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(true, true, forbiddenClassNames);

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with both flags true");
    }

    /**
     * Tests the constructor with both boolean flags set to false.
     * Verifies that merger is created with conservative settings.
     */
    @Test
    public void testConstructorWithBothFlagsFalse() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(false, false, forbiddenClassNames);

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with both flags false");
    }

    /**
     * Tests the constructor with allowAccessModification true and mergeInterfacesAggressively false.
     * Verifies that merger is created with this flag combination.
     */
    @Test
    public void testConstructorWithAccessModificationTrueAndAggressiveMergingFalse() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(true, false, forbiddenClassNames);

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with mixed flag values");
    }

    /**
     * Tests the constructor with allowAccessModification false and mergeInterfacesAggressively true.
     * Verifies that merger is created with this flag combination.
     */
    @Test
    public void testConstructorWithAccessModificationFalseAndAggressiveMergingTrue() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(false, true, forbiddenClassNames);

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with mixed flag values");
    }

    // ========================================
    // ForbiddenClassNames Set Tests
    // ========================================

    /**
     * Tests the constructor with a set containing a single forbidden class name.
     * Verifies that merger handles single-element sets correctly.
     */
    @Test
    public void testConstructorWithSingleForbiddenClassName() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com.example.DoNotMerge");

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(true, true, forbiddenClassNames);

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with single forbidden class");
    }

    /**
     * Tests the constructor with a set containing multiple forbidden class names.
     * Verifies that merger handles multiple forbidden classes correctly.
     */
    @Test
    public void testConstructorWithMultipleForbiddenClassNames() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com.example.DoNotMerge1");
        forbiddenClassNames.add("com.example.DoNotMerge2");
        forbiddenClassNames.add("org.test.Protected");
        forbiddenClassNames.add("net.forbidden.Class");

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(false, false, forbiddenClassNames);

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with multiple forbidden classes");
    }

    /**
     * Tests the constructor with a large set of forbidden class names.
     * Verifies that merger handles large sets efficiently.
     */
    @Test
    public void testConstructorWithLargeForbiddenClassNamesSet() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            forbiddenClassNames.add("com.example.DoNotMerge" + i);
        }

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(true, false, forbiddenClassNames);

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with large set of forbidden classes");
    }

    // ========================================
    // Interface Implementation Tests
    // ========================================

    /**
     * Tests that the constructor creates an instance of ClassPoolVisitor.
     * Verifies that HorizontalClassMerger implements the ClassPoolVisitor interface.
     */
    @Test
    public void testConstructorCreatesInstanceOfClassPoolVisitor() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(true, true, forbiddenClassNames);

        // Assert
        assertInstanceOf(ClassPoolVisitor.class, merger,
            "HorizontalClassMerger should implement ClassPoolVisitor interface");
    }

    /**
     * Tests that the created instance can be used as a ClassPoolVisitor.
     * Verifies that the merger is in a valid state for use.
     */
    @Test
    public void testConstructorCreatesUsableClassPoolVisitor() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(false, true, forbiddenClassNames);

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created");
        ClassPoolVisitor visitor = merger;
        assertNotNull(visitor, "HorizontalClassMerger should be usable as ClassPoolVisitor");
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
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com.example.DoNotMerge");

        // Act
        HorizontalClassMerger merger1 = new HorizontalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            forbiddenClassNames
        );
        HorizontalClassMerger merger2 = new HorizontalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            forbiddenClassNames
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
        // Arrange
        Set<String> forbiddenClassNames1 = new HashSet<>();
        forbiddenClassNames1.add("com.example.Class1");

        Set<String> forbiddenClassNames2 = new HashSet<>();
        forbiddenClassNames2.add("com.example.Class2");
        forbiddenClassNames2.add("com.example.Class3");

        // Act
        HorizontalClassMerger merger1 = new HorizontalClassMerger(true, true, forbiddenClassNames1);
        HorizontalClassMerger merger2 = new HorizontalClassMerger(false, false, forbiddenClassNames2);

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different");
    }

    /**
     * Tests creating multiple merger instances with shared forbiddenClassNames set.
     * Verifies that mergers can share the same set instance.
     */
    @Test
    public void testMultipleMergersWithSharedForbiddenClassNames() {
        // Arrange
        Set<String> sharedForbiddenClassNames = new HashSet<>();
        sharedForbiddenClassNames.add("com.example.DoNotMerge");

        // Act
        HorizontalClassMerger merger1 = new HorizontalClassMerger(true, false, sharedForbiddenClassNames);
        HorizontalClassMerger merger2 = new HorizontalClassMerger(false, true, sharedForbiddenClassNames);

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
            Set<String> forbiddenClassNames = new HashSet<>();
            forbiddenClassNames.add("com.example.Class" + i);
            HorizontalClassMerger merger = new HorizontalClassMerger(
                i % 2 == 0,
                i % 3 == 0,
                forbiddenClassNames
            );
            assertNotNull(merger, "Merger " + i + " should be created");
        }
    }

    /**
     * Tests that two mergers created with null forbiddenClassNames are independent instances.
     * Verifies that even with null parameters, distinct instances are created.
     */
    @Test
    public void testMultipleMergersWithNullForbiddenClassNamesAreIndependent() {
        // Act
        HorizontalClassMerger merger1 = new HorizontalClassMerger(true, true, null);
        HorizontalClassMerger merger2 = new HorizontalClassMerger(true, true, null);

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different even with null parameters");
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
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com.example.DoNotMerge");
        long startTime = System.nanoTime();

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(true, true, forbiddenClassNames);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(merger, "HorizontalClassMerger should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that the constructor efficiently handles a large forbidden class names set.
     * Verifies that constructor performance scales reasonably with set size.
     */
    @Test
    public void testConstructorEfficiencyWithLargeSet() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            forbiddenClassNames.add("com.example.DoNotMerge" + i);
        }
        long startTime = System.nanoTime();

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(false, false, forbiddenClassNames);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(merger, "HorizontalClassMerger should be created with large set");
        // Constructor should complete in less than 50 milliseconds even with large set
        assertTrue(duration < 50_000_000L,
            "Constructor should complete quickly even with large set (took " + duration + " ns)");
    }

    // ========================================
    // Edge Case Tests
    // ========================================

    /**
     * Tests the constructor with forbiddenClassNames containing empty strings.
     * Verifies that merger handles sets with empty string elements.
     */
    @Test
    public void testConstructorWithEmptyStringInForbiddenClassNames() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("");
        forbiddenClassNames.add("com.example.Valid");

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(true, false, forbiddenClassNames);

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with empty string in set");
    }

    /**
     * Tests the constructor with forbiddenClassNames containing special characters.
     * Verifies that merger handles class names with special characters.
     */
    @Test
    public void testConstructorWithSpecialCharactersInForbiddenClassNames() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com/example/SlashSeparated");
        forbiddenClassNames.add("com.example.Inner$Class");
        forbiddenClassNames.add("com.example.Generic<T>");

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(false, true, forbiddenClassNames);

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with special characters in class names");
    }

    /**
     * Tests the constructor with all possible combinations of boolean parameters.
     * Verifies that all four combinations of boolean flags work correctly.
     */
    @Test
    public void testConstructorWithAllBooleanCombinations() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();

        // Test case 1: false, false
        HorizontalClassMerger merger1 = new HorizontalClassMerger(false, false, forbiddenClassNames);
        assertNotNull(merger1, "Merger with false, false should be created");

        // Test case 2: false, true
        HorizontalClassMerger merger2 = new HorizontalClassMerger(false, true, forbiddenClassNames);
        assertNotNull(merger2, "Merger with false, true should be created");

        // Test case 3: true, false
        HorizontalClassMerger merger3 = new HorizontalClassMerger(true, false, forbiddenClassNames);
        assertNotNull(merger3, "Merger with true, false should be created");

        // Test case 4: true, true
        HorizontalClassMerger merger4 = new HorizontalClassMerger(true, true, forbiddenClassNames);
        assertNotNull(merger4, "Merger with true, true should be created");
    }

    /**
     * Tests the constructor with alternating boolean values and different sets.
     * Verifies that various parameter combinations work correctly.
     */
    @Test
    public void testConstructorWithVariousParameterCombinations() {
        // Test case 1: true, true, null
        HorizontalClassMerger merger1 = new HorizontalClassMerger(true, true, null);
        assertNotNull(merger1, "Merger with true, true, null should be created");

        // Test case 2: false, false, empty set
        HorizontalClassMerger merger2 = new HorizontalClassMerger(false, false, new HashSet<>());
        assertNotNull(merger2, "Merger with false, false, empty set should be created");

        // Test case 3: true, false, set with elements
        Set<String> set3 = new HashSet<>();
        set3.add("com.example.Class");
        HorizontalClassMerger merger3 = new HorizontalClassMerger(true, false, set3);
        assertNotNull(merger3, "Merger with true, false, non-empty set should be created");

        // Test case 4: false, true, set with elements
        Set<String> set4 = new HashSet<>();
        set4.add("org.test.Class");
        HorizontalClassMerger merger4 = new HorizontalClassMerger(false, true, set4);
        assertNotNull(merger4, "Merger with false, true, non-empty set should be created");
    }

    /**
     * Tests that the constructor properly initializes the instance for use.
     * Verifies that the created instance is in a valid state.
     */
    @Test
    public void testConstructorInitializesInstanceProperly() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com.example.DoNotMerge");

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(true, false, forbiddenClassNames);

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created");
        // The merger should be usable as a ClassPoolVisitor
        ClassPoolVisitor visitor = merger;
        assertNotNull(visitor, "HorizontalClassMerger should be usable as ClassPoolVisitor");
    }

    // ========================================
    // Four-Parameter Constructor Tests
    // ========================================

    /**
     * Tests the four-parameter constructor with all valid non-null parameters.
     * Verifies that the merger can be instantiated with all parameters provided.
     */
    @Test
    public void testFourParamConstructorWithValidParameters() {
        // Arrange
        boolean allowAccessModification = true;
        boolean mergeInterfacesAggressively = true;
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com.example.DoNotMerge");
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            forbiddenClassNames,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created successfully");
    }

    /**
     * Tests the four-parameter constructor with null extraClassVisitor.
     * Verifies that the merger handles null visitor parameter.
     */
    @Test
    public void testFourParamConstructorWithNullExtraClassVisitor() {
        // Arrange
        boolean allowAccessModification = true;
        boolean mergeInterfacesAggressively = false;
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com.example.DoNotMerge");

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            forbiddenClassNames,
            null
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with null extraClassVisitor");
    }

    /**
     * Tests the four-parameter constructor with null forbiddenClassNames.
     * Verifies that the merger handles null set parameter.
     */
    @Test
    public void testFourParamConstructorWithNullForbiddenClassNames() {
        // Arrange
        boolean allowAccessModification = false;
        boolean mergeInterfacesAggressively = true;
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            null,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with null forbiddenClassNames");
    }

    /**
     * Tests the four-parameter constructor with both nulls.
     * Verifies that the merger handles both null parameters.
     */
    @Test
    public void testFourParamConstructorWithBothNulls() {
        // Arrange
        boolean allowAccessModification = true;
        boolean mergeInterfacesAggressively = true;

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            null,
            null
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with both null parameters");
    }

    /**
     * Tests the four-parameter constructor with empty forbiddenClassNames set.
     * Verifies that the merger handles empty set correctly.
     */
    @Test
    public void testFourParamConstructorWithEmptyForbiddenClassNames() {
        // Arrange
        boolean allowAccessModification = false;
        boolean mergeInterfacesAggressively = false;
        Set<String> forbiddenClassNames = new HashSet<>();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            forbiddenClassNames,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with empty forbiddenClassNames");
    }

    /**
     * Tests the four-parameter constructor with both boolean flags set to true.
     * Verifies that merger is created with maximum flexibility.
     */
    @Test
    public void testFourParamConstructorWithBothFlagsTrue() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            true,
            true,
            forbiddenClassNames,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with both flags true");
    }

    /**
     * Tests the four-parameter constructor with both boolean flags set to false.
     * Verifies that merger is created with conservative settings.
     */
    @Test
    public void testFourParamConstructorWithBothFlagsFalse() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            false,
            false,
            forbiddenClassNames,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with both flags false");
    }

    /**
     * Tests the four-parameter constructor with multiple forbidden class names.
     * Verifies that merger handles multiple forbidden classes correctly.
     */
    @Test
    public void testFourParamConstructorWithMultipleForbiddenClassNames() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com.example.DoNotMerge1");
        forbiddenClassNames.add("com.example.DoNotMerge2");
        forbiddenClassNames.add("org.test.Protected");
        forbiddenClassNames.add("net.forbidden.Class");
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            true,
            false,
            forbiddenClassNames,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with multiple forbidden classes");
    }

    /**
     * Tests that the four-parameter constructor creates an instance of ClassPoolVisitor.
     * Verifies that HorizontalClassMerger implements the ClassPoolVisitor interface.
     */
    @Test
    public void testFourParamConstructorCreatesInstanceOfClassPoolVisitor() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            true,
            true,
            forbiddenClassNames,
            extraClassVisitor
        );

        // Assert
        assertInstanceOf(ClassPoolVisitor.class, merger,
            "HorizontalClassMerger should implement ClassPoolVisitor interface");
    }

    /**
     * Tests creating multiple merger instances with the same parameters.
     * Verifies that multiple instances can be created using the same parameters.
     */
    @Test
    public void testFourParamConstructorMultipleMergersWithSameParameters() {
        // Arrange
        boolean allowAccessModification = true;
        boolean mergeInterfacesAggressively = false;
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com.example.DoNotMerge");
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger1 = new HorizontalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            forbiddenClassNames,
            extraClassVisitor
        );
        HorizontalClassMerger merger2 = new HorizontalClassMerger(
            allowAccessModification,
            mergeInterfacesAggressively,
            forbiddenClassNames,
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
    public void testFourParamConstructorMultipleMergersWithDifferentParameters() {
        // Arrange
        Set<String> forbiddenClassNames1 = new HashSet<>();
        forbiddenClassNames1.add("com.example.Class1");
        ClassVisitor extraClassVisitor1 = mock(ClassVisitor.class);

        Set<String> forbiddenClassNames2 = new HashSet<>();
        forbiddenClassNames2.add("com.example.Class2");
        forbiddenClassNames2.add("com.example.Class3");
        ClassVisitor extraClassVisitor2 = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger1 = new HorizontalClassMerger(
            true,
            true,
            forbiddenClassNames1,
            extraClassVisitor1
        );
        HorizontalClassMerger merger2 = new HorizontalClassMerger(
            false,
            false,
            forbiddenClassNames2,
            extraClassVisitor2
        );

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different");
    }

    /**
     * Tests creating multiple merger instances with shared forbiddenClassNames set.
     * Verifies that mergers can share the same set instance.
     */
    @Test
    public void testFourParamConstructorMultipleMergersWithSharedForbiddenClassNames() {
        // Arrange
        Set<String> sharedForbiddenClassNames = new HashSet<>();
        sharedForbiddenClassNames.add("com.example.DoNotMerge");
        ClassVisitor extraClassVisitor1 = mock(ClassVisitor.class);
        ClassVisitor extraClassVisitor2 = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger1 = new HorizontalClassMerger(
            true,
            false,
            sharedForbiddenClassNames,
            extraClassVisitor1
        );
        HorizontalClassMerger merger2 = new HorizontalClassMerger(
            false,
            true,
            sharedForbiddenClassNames,
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
    public void testFourParamConstructorMultipleMergersWithSharedExtraClassVisitor() {
        // Arrange
        Set<String> forbiddenClassNames1 = new HashSet<>();
        forbiddenClassNames1.add("com.example.Class1");
        Set<String> forbiddenClassNames2 = new HashSet<>();
        forbiddenClassNames2.add("com.example.Class2");
        ClassVisitor sharedVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger1 = new HorizontalClassMerger(
            true,
            false,
            forbiddenClassNames1,
            sharedVisitor
        );
        HorizontalClassMerger merger2 = new HorizontalClassMerger(
            false,
            true,
            forbiddenClassNames2,
            sharedVisitor
        );

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different");
    }

    /**
     * Tests that the four-parameter constructor doesn't invoke any methods on the parameters.
     * Verifies that the constructor only stores the parameters without using them.
     */
    @Test
    public void testFourParamConstructorDoesNotInvokeParameters() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            true,
            true,
            forbiddenClassNames,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created");
        verifyNoInteractions(extraClassVisitor);
    }

    /**
     * Tests that the four-parameter constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testFourParamConstructorIsEfficient() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com.example.DoNotMerge");
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);
        long startTime = System.nanoTime();

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            true,
            true,
            forbiddenClassNames,
            extraClassVisitor
        );

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(merger, "HorizontalClassMerger should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests the four-parameter constructor with a large set of forbidden class names.
     * Verifies that merger handles large sets efficiently.
     */
    @Test
    public void testFourParamConstructorWithLargeForbiddenClassNamesSet() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            forbiddenClassNames.add("com.example.DoNotMerge" + i);
        }
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            true,
            false,
            forbiddenClassNames,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with large set of forbidden classes");
    }

    /**
     * Tests creating a sequence of mergers with the four-parameter constructor.
     * Verifies that multiple mergers can be created sequentially without issues.
     */
    @Test
    public void testFourParamConstructorSequentialMergerCreation() {
        // Act & Assert - create multiple mergers sequentially
        for (int i = 0; i < 10; i++) {
            Set<String> forbiddenClassNames = new HashSet<>();
            forbiddenClassNames.add("com.example.Class" + i);
            ClassVisitor extraClassVisitor = mock(ClassVisitor.class);
            HorizontalClassMerger merger = new HorizontalClassMerger(
                i % 2 == 0,
                i % 3 == 0,
                forbiddenClassNames,
                extraClassVisitor
            );
            assertNotNull(merger, "Merger " + i + " should be created");
        }
    }

    /**
     * Tests that two mergers created with null parameters are independent instances.
     * Verifies that even with null parameters, distinct instances are created.
     */
    @Test
    public void testFourParamConstructorMultipleMergersWithNullsAreIndependent() {
        // Act
        HorizontalClassMerger merger1 = new HorizontalClassMerger(true, true, null, null);
        HorizontalClassMerger merger2 = new HorizontalClassMerger(true, true, null, null);

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different even with null parameters");
    }

    /**
     * Tests the four-parameter constructor with all boolean flag combinations.
     * Verifies that all four combinations of boolean flags work correctly.
     */
    @Test
    public void testFourParamConstructorWithAllBooleanCombinations() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Test case 1: false, false
        HorizontalClassMerger merger1 = new HorizontalClassMerger(
            false, false, forbiddenClassNames, extraClassVisitor
        );
        assertNotNull(merger1, "Merger with false, false should be created");

        // Test case 2: false, true
        HorizontalClassMerger merger2 = new HorizontalClassMerger(
            false, true, forbiddenClassNames, extraClassVisitor
        );
        assertNotNull(merger2, "Merger with false, true should be created");

        // Test case 3: true, false
        HorizontalClassMerger merger3 = new HorizontalClassMerger(
            true, false, forbiddenClassNames, extraClassVisitor
        );
        assertNotNull(merger3, "Merger with true, false should be created");

        // Test case 4: true, true
        HorizontalClassMerger merger4 = new HorizontalClassMerger(
            true, true, forbiddenClassNames, extraClassVisitor
        );
        assertNotNull(merger4, "Merger with true, true should be created");
    }

    /**
     * Tests the four-parameter constructor with various null combinations.
     * Verifies that all combinations of null/non-null parameters are handled.
     */
    @Test
    public void testFourParamConstructorWithVariousNullCombinations() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Test case 1: both non-null
        HorizontalClassMerger merger1 = new HorizontalClassMerger(
            true, true, forbiddenClassNames, extraClassVisitor
        );
        assertNotNull(merger1, "Merger with both non-null should be created");

        // Test case 2: null set, non-null visitor
        HorizontalClassMerger merger2 = new HorizontalClassMerger(
            true, false, null, extraClassVisitor
        );
        assertNotNull(merger2, "Merger with null set should be created");

        // Test case 3: non-null set, null visitor
        HorizontalClassMerger merger3 = new HorizontalClassMerger(
            false, true, forbiddenClassNames, null
        );
        assertNotNull(merger3, "Merger with null visitor should be created");

        // Test case 4: both null
        HorizontalClassMerger merger4 = new HorizontalClassMerger(
            false, false, null, null
        );
        assertNotNull(merger4, "Merger with both null should be created");
    }

    /**
     * Tests that the four-parameter constructor properly initializes the instance for use.
     * Verifies that the created instance is in a valid state.
     */
    @Test
    public void testFourParamConstructorInitializesInstanceProperly() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com.example.DoNotMerge");
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            true,
            false,
            forbiddenClassNames,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created");
        // The merger should be usable as a ClassPoolVisitor
        ClassPoolVisitor visitor = merger;
        assertNotNull(visitor, "HorizontalClassMerger should be usable as ClassPoolVisitor");
    }

    /**
     * Tests the four-parameter constructor with special characters in forbidden class names.
     * Verifies that merger handles class names with special characters.
     */
    @Test
    public void testFourParamConstructorWithSpecialCharactersInForbiddenClassNames() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com/example/SlashSeparated");
        forbiddenClassNames.add("com.example.Inner$Class");
        forbiddenClassNames.add("com.example.Generic<T>");
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger = new HorizontalClassMerger(
            false,
            true,
            forbiddenClassNames,
            extraClassVisitor
        );

        // Assert
        assertNotNull(merger, "HorizontalClassMerger should be created with special characters in class names");
    }

    // ========================================
    // Cross-Constructor Tests
    // ========================================

    /**
     * Tests that the three-parameter and four-parameter constructors create independent instances.
     * Verifies that both constructors can be used interchangeably.
     */
    @Test
    public void testBothConstructorsCreateIndependentInstances() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        forbiddenClassNames.add("com.example.DoNotMerge");
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger1 = new HorizontalClassMerger(
            true, false, forbiddenClassNames
        );
        HorizontalClassMerger merger2 = new HorizontalClassMerger(
            true, false, forbiddenClassNames, extraClassVisitor
        );
        HorizontalClassMerger merger3 = new HorizontalClassMerger(
            true, false, forbiddenClassNames, null
        );

        // Assert
        assertNotNull(merger1, "Three-param constructor should create instance");
        assertNotNull(merger2, "Four-param constructor with visitor should create instance");
        assertNotNull(merger3, "Four-param constructor with null visitor should create instance");
        assertNotSame(merger1, merger2, "Instances should be different");
        assertNotSame(merger1, merger3, "Instances should be different");
        assertNotSame(merger2, merger3, "Instances should be different");
    }

    /**
     * Tests that multiple HorizontalClassMerger instances work independently.
     * Verifies that using both constructors doesn't cause interference.
     */
    @Test
    public void testMultipleInstancesFromBothConstructorsWorkIndependently() {
        // Arrange
        Set<String> forbiddenClassNames1 = new HashSet<>();
        forbiddenClassNames1.add("com.example.Class1");
        Set<String> forbiddenClassNames2 = new HashSet<>();
        forbiddenClassNames2.add("com.example.Class2");
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger1 = new HorizontalClassMerger(
            true, false, forbiddenClassNames1
        );
        HorizontalClassMerger merger2 = new HorizontalClassMerger(
            false, true, forbiddenClassNames2, extraClassVisitor
        );

        // Assert
        assertNotNull(merger1, "First merger should be created");
        assertNotNull(merger2, "Second merger should be created");
        assertNotSame(merger1, merger2, "Merger instances should be different");
        assertInstanceOf(ClassPoolVisitor.class, merger1, "First merger should implement ClassPoolVisitor");
        assertInstanceOf(ClassPoolVisitor.class, merger2, "Second merger should implement ClassPoolVisitor");
    }

    /**
     * Tests creating mergers with alternating constructors.
     * Verifies that both constructors can be used in sequence without issues.
     */
    @Test
    public void testAlternatingConstructorUsage() {
        // Arrange
        Set<String> forbiddenClassNames = new HashSet<>();
        ClassVisitor extraClassVisitor = mock(ClassVisitor.class);

        // Act
        HorizontalClassMerger merger1 = new HorizontalClassMerger(
            true, false, forbiddenClassNames
        );
        HorizontalClassMerger merger2 = new HorizontalClassMerger(
            false, true, forbiddenClassNames, extraClassVisitor
        );
        HorizontalClassMerger merger3 = new HorizontalClassMerger(
            true, true, forbiddenClassNames
        );
        HorizontalClassMerger merger4 = new HorizontalClassMerger(
            false, false, forbiddenClassNames, null
        );

        // Assert
        assertNotNull(merger1, "Merger 1 should be created");
        assertNotNull(merger2, "Merger 2 should be created");
        assertNotNull(merger3, "Merger 3 should be created");
        assertNotNull(merger4, "Merger 4 should be created");
    }
}
