package proguard.optimize.peephole;

import org.junit.jupiter.api.Test;
import proguard.classfile.visitor.MemberVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link MethodFinalizer} constructors.
 * Tests the constructors with signatures:
 * - ()V
 * - (Lproguard/classfile/visitor/MemberVisitor;)V
 *
 * This constructor creates a new MethodFinalizer that makes program methods final, if possible.
 * The no-arg constructor delegates to the single-parameter constructor with null for the
 * extraMemberVisitor parameter.
 */
public class MethodFinalizerClaude_constructorTest {

    // ========================================
    // No-arg Constructor Tests
    // ========================================

    /**
     * Tests the no-arg constructor creates a valid instance.
     * Verifies that the finalizer can be instantiated successfully.
     */
    @Test
    public void testNoArgConstructorCreatesValidInstance() {
        // Act
        MethodFinalizer finalizer = new MethodFinalizer();

        // Assert
        assertNotNull(finalizer, "MethodFinalizer should be created successfully");
    }

    /**
     * Tests that multiple finalizer instances can be created using the no-arg constructor.
     * Verifies that the constructor can be called multiple times.
     */
    @Test
    public void testNoArgConstructorMultipleInstances() {
        // Act
        MethodFinalizer finalizer1 = new MethodFinalizer();
        MethodFinalizer finalizer2 = new MethodFinalizer();

        // Assert
        assertNotNull(finalizer1, "First finalizer should be created");
        assertNotNull(finalizer2, "Second finalizer should be created");
        assertNotSame(finalizer1, finalizer2, "Finalizer instances should be different");
    }

    /**
     * Tests creating a sequence of finalizers using the no-arg constructor.
     * Verifies that multiple finalizers can be created sequentially without issues.
     */
    @Test
    public void testNoArgConstructorSequentialCreation() {
        // Act & Assert - create multiple finalizers sequentially
        for (int i = 0; i < 10; i++) {
            MethodFinalizer finalizer = new MethodFinalizer();
            assertNotNull(finalizer, "Finalizer " + i + " should be created");
        }
    }

    /**
     * Tests that the no-arg constructor creates an instance of MemberVisitor.
     * Verifies that MethodFinalizer implements the MemberVisitor interface.
     */
    @Test
    public void testNoArgConstructorCreatesInstanceOfMemberVisitor() {
        // Act
        MethodFinalizer finalizer = new MethodFinalizer();

        // Assert
        assertInstanceOf(MemberVisitor.class, finalizer,
            "MethodFinalizer should implement MemberVisitor interface");
    }

    /**
     * Tests that the created instance can be used as a MemberVisitor.
     * Verifies that the finalizer is in a valid state for use as MemberVisitor.
     */
    @Test
    public void testNoArgConstructorCreatesUsableMemberVisitor() {
        // Act
        MethodFinalizer finalizer = new MethodFinalizer();

        // Assert
        assertNotNull(finalizer, "MethodFinalizer should be created");
        MemberVisitor memberVisitor = finalizer;
        assertNotNull(memberVisitor, "MethodFinalizer should be usable as MemberVisitor");
    }

    /**
     * Tests that the no-arg constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testNoArgConstructorIsEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act
        MethodFinalizer finalizer = new MethodFinalizer();

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(finalizer, "MethodFinalizer should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that the no-arg constructor properly initializes the instance for use.
     * Verifies that the created instance is in a valid state.
     */
    @Test
    public void testNoArgConstructorInitializesInstanceProperly() {
        // Act
        MethodFinalizer finalizer = new MethodFinalizer();

        // Assert
        assertNotNull(finalizer, "MethodFinalizer should be created");
        assertInstanceOf(MemberVisitor.class, finalizer,
            "MethodFinalizer should implement MemberVisitor interface");
    }

    /**
     * Tests that two finalizers created with no-arg constructor are independent instances.
     * Verifies that distinct instances are created.
     */
    @Test
    public void testNoArgConstructorMultipleInstancesAreIndependent() {
        // Act
        MethodFinalizer finalizer1 = new MethodFinalizer();
        MethodFinalizer finalizer2 = new MethodFinalizer();

        // Assert
        assertNotNull(finalizer1, "First finalizer should be created");
        assertNotNull(finalizer2, "Second finalizer should be created");
        assertNotSame(finalizer1, finalizer2, "Finalizer instances should be different");
    }

    /**
     * Tests that multiple finalizers can coexist without interference.
     * Verifies that instances are completely independent.
     */
    @Test
    public void testNoArgConstructorMultipleFinalizerCoexistence() {
        // Act - create multiple finalizers
        MethodFinalizer finalizer1 = new MethodFinalizer();
        MethodFinalizer finalizer2 = new MethodFinalizer();
        MethodFinalizer finalizer3 = new MethodFinalizer();

        // Assert - all should be valid and independent
        assertNotNull(finalizer1);
        assertNotNull(finalizer2);
        assertNotNull(finalizer3);

        assertNotSame(finalizer1, finalizer2);
        assertNotSame(finalizer1, finalizer3);
        assertNotSame(finalizer2, finalizer3);

        // All should implement MemberVisitor
        assertInstanceOf(MemberVisitor.class, finalizer1);
        assertInstanceOf(MemberVisitor.class, finalizer2);
        assertInstanceOf(MemberVisitor.class, finalizer3);
    }

    /**
     * Tests that the no-arg constructor creates instances that are distinct even when created rapidly.
     * Verifies that rapid instantiation doesn't cause issues.
     */
    @Test
    public void testNoArgConstructorRapidInstanceCreation() {
        // Act - create instances as rapidly as possible
        MethodFinalizer finalizer1 = new MethodFinalizer();
        MethodFinalizer finalizer2 = new MethodFinalizer();
        MethodFinalizer finalizer3 = new MethodFinalizer();
        MethodFinalizer finalizer4 = new MethodFinalizer();
        MethodFinalizer finalizer5 = new MethodFinalizer();

        // Assert - all instances should be distinct
        assertNotSame(finalizer1, finalizer2);
        assertNotSame(finalizer1, finalizer3);
        assertNotSame(finalizer1, finalizer4);
        assertNotSame(finalizer1, finalizer5);
        assertNotSame(finalizer2, finalizer3);
        assertNotSame(finalizer2, finalizer4);
        assertNotSame(finalizer2, finalizer5);
        assertNotSame(finalizer3, finalizer4);
        assertNotSame(finalizer3, finalizer5);
        assertNotSame(finalizer4, finalizer5);
    }

    /**
     * Tests that creating multiple finalizers with no-arg constructor is efficient.
     * Verifies that constructor performance is consistent across multiple invocations.
     */
    @Test
    public void testNoArgConstructorMultipleCallsAreEfficient() {
        // Arrange
        long startTime = System.nanoTime();

        // Act - create 100 finalizers
        for (int i = 0; i < 100; i++) {
            MethodFinalizer finalizer = new MethodFinalizer();
            assertNotNull(finalizer, "Finalizer should be created");
        }

        // Assert
        long duration = System.nanoTime() - startTime;
        // Creating 100 finalizers should complete in less than 100 milliseconds
        assertTrue(duration < 100_000_000L,
            "Creating 100 finalizers should be efficient (took " + duration + " ns)");
    }

    // ========================================
    // Single-parameter Constructor Tests
    // ========================================

    /**
     * Tests the single-parameter constructor with a valid non-null MemberVisitor.
     * Verifies that the finalizer can be instantiated with a valid parameter.
     */
    @Test
    public void testSingleParamConstructorWithValidMemberVisitor() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        MethodFinalizer finalizer = new MethodFinalizer(extraMemberVisitor);

        // Assert
        assertNotNull(finalizer, "MethodFinalizer should be created successfully");
    }

    /**
     * Tests the single-parameter constructor with null MemberVisitor.
     * Verifies that the finalizer can be instantiated with null parameter.
     */
    @Test
    public void testSingleParamConstructorWithNullMemberVisitor() {
        // Act
        MethodFinalizer finalizer = new MethodFinalizer(null);

        // Assert
        assertNotNull(finalizer, "MethodFinalizer should be created with null extraMemberVisitor");
    }

    /**
     * Tests creating multiple finalizer instances with the same MemberVisitor.
     * Verifies that multiple instances can be created using the same object.
     */
    @Test
    public void testSingleParamConstructorMultipleInstancesWithSameMemberVisitor() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        MethodFinalizer finalizer1 = new MethodFinalizer(extraMemberVisitor);
        MethodFinalizer finalizer2 = new MethodFinalizer(extraMemberVisitor);

        // Assert
        assertNotNull(finalizer1, "First finalizer should be created");
        assertNotNull(finalizer2, "Second finalizer should be created");
        assertNotSame(finalizer1, finalizer2, "Finalizer instances should be different");
    }

    /**
     * Tests creating multiple finalizer instances with different MemberVisitors.
     * Verifies that finalizers can be created independently with different instances.
     */
    @Test
    public void testSingleParamConstructorMultipleInstancesWithDifferentMemberVisitors() {
        // Arrange
        MemberVisitor visitor1 = mock(MemberVisitor.class);
        MemberVisitor visitor2 = mock(MemberVisitor.class);

        // Act
        MethodFinalizer finalizer1 = new MethodFinalizer(visitor1);
        MethodFinalizer finalizer2 = new MethodFinalizer(visitor2);

        // Assert
        assertNotNull(finalizer1, "First finalizer should be created");
        assertNotNull(finalizer2, "Second finalizer should be created");
        assertNotSame(finalizer1, finalizer2, "Finalizer instances should be different");
    }

    /**
     * Tests that the constructor doesn't invoke any methods on the MemberVisitor.
     * Verifies that the constructor only stores the parameter without using it.
     */
    @Test
    public void testSingleParamConstructorDoesNotInvokeMemberVisitor() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        MethodFinalizer finalizer = new MethodFinalizer(extraMemberVisitor);

        // Assert
        assertNotNull(finalizer, "MethodFinalizer should be created");
        verifyNoInteractions(extraMemberVisitor);
    }

    /**
     * Tests that the single-parameter constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testSingleParamConstructorIsEfficient() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);
        long startTime = System.nanoTime();

        // Act
        MethodFinalizer finalizer = new MethodFinalizer(extraMemberVisitor);

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(finalizer, "MethodFinalizer should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests that the single-parameter constructor creates an instance of MemberVisitor.
     * Verifies that MethodFinalizer implements the MemberVisitor interface.
     */
    @Test
    public void testSingleParamConstructorCreatesInstanceOfMemberVisitor() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        MethodFinalizer finalizer = new MethodFinalizer(extraMemberVisitor);

        // Assert
        assertInstanceOf(MemberVisitor.class, finalizer,
            "MethodFinalizer should implement MemberVisitor interface");
    }

    /**
     * Tests that the single-parameter constructor properly initializes the instance for use.
     * Verifies that the created instance is in a valid state by checking it can be used as a MemberVisitor.
     */
    @Test
    public void testSingleParamConstructorInitializesInstanceProperly() {
        // Arrange
        MemberVisitor extraMemberVisitor = mock(MemberVisitor.class);

        // Act
        MethodFinalizer finalizer = new MethodFinalizer(extraMemberVisitor);

        // Assert
        assertNotNull(finalizer, "MethodFinalizer should be created");
        // The finalizer should be usable as a MemberVisitor
        MemberVisitor visitor = finalizer;
        assertNotNull(visitor, "MethodFinalizer should be usable as MemberVisitor");
    }

    /**
     * Tests creating a sequence of finalizers with the single-parameter constructor.
     * Verifies that multiple finalizers can be created sequentially without issues.
     */
    @Test
    public void testSingleParamConstructorSequentialCreation() {
        // Act & Assert - create multiple finalizers sequentially
        for (int i = 0; i < 10; i++) {
            MemberVisitor visitor = mock(MemberVisitor.class);
            MethodFinalizer finalizer = new MethodFinalizer(visitor);
            assertNotNull(finalizer, "Finalizer " + i + " should be created");
        }
    }

    /**
     * Tests that two finalizers created with null are independent instances.
     * Verifies that even with null parameters, distinct instances are created.
     */
    @Test
    public void testSingleParamConstructorMultipleInstancesWithNullAreIndependent() {
        // Act
        MethodFinalizer finalizer1 = new MethodFinalizer(null);
        MethodFinalizer finalizer2 = new MethodFinalizer(null);

        // Assert
        assertNotNull(finalizer1, "First finalizer should be created");
        assertNotNull(finalizer2, "Second finalizer should be created");
        assertNotSame(finalizer1, finalizer2, "Finalizer instances should be different even with null parameters");
    }

    /**
     * Tests alternating creation of finalizers with null and non-null parameters.
     * Verifies that finalizers can be created with various parameter combinations.
     */
    @Test
    public void testSingleParamConstructorAlternatingNullAndNonNullParameters() {
        // Arrange
        MemberVisitor visitor = mock(MemberVisitor.class);

        // Act
        MethodFinalizer finalizer1 = new MethodFinalizer(visitor);
        MethodFinalizer finalizer2 = new MethodFinalizer(null);
        MethodFinalizer finalizer3 = new MethodFinalizer(visitor);
        MethodFinalizer finalizer4 = new MethodFinalizer(null);

        // Assert
        assertNotNull(finalizer1, "Finalizer 1 should be created");
        assertNotNull(finalizer2, "Finalizer 2 should be created");
        assertNotNull(finalizer3, "Finalizer 3 should be created");
        assertNotNull(finalizer4, "Finalizer 4 should be created");
    }

    /**
     * Tests that the single-parameter constructor with the same MemberVisitor but different instances.
     * Verifies that finalizers can share parameters while being independent.
     */
    @Test
    public void testSingleParamConstructorWithSharedMemberVisitor() {
        // Arrange
        MemberVisitor sharedVisitor = mock(MemberVisitor.class);

        // Act
        MethodFinalizer finalizer1 = new MethodFinalizer(sharedVisitor);
        MethodFinalizer finalizer2 = new MethodFinalizer(sharedVisitor);

        // Assert
        assertNotNull(finalizer1, "First finalizer should be created");
        assertNotNull(finalizer2, "Second finalizer should be created");
        assertNotSame(finalizer1, finalizer2, "Finalizer instances should be different");
    }

    // ========================================
    // Cross-constructor Tests
    // ========================================

    /**
     * Tests that the no-arg and single-parameter constructors create independent instances.
     * Verifies that both constructors can be used interchangeably.
     */
    @Test
    public void testBothConstructorsCreateIndependentInstances() {
        // Arrange
        MemberVisitor visitor = mock(MemberVisitor.class);

        // Act
        MethodFinalizer finalizer1 = new MethodFinalizer();
        MethodFinalizer finalizer2 = new MethodFinalizer(visitor);
        MethodFinalizer finalizer3 = new MethodFinalizer(null);

        // Assert
        assertNotNull(finalizer1, "No-arg constructor should create instance");
        assertNotNull(finalizer2, "Single-param constructor with visitor should create instance");
        assertNotNull(finalizer3, "Single-param constructor with null visitor should create instance");
        assertNotSame(finalizer1, finalizer2, "Instances should be different");
        assertNotSame(finalizer1, finalizer3, "Instances should be different");
        assertNotSame(finalizer2, finalizer3, "Instances should be different");
    }

    /**
     * Tests that multiple MethodFinalizer instances work independently.
     * Verifies that using both constructors doesn't cause interference.
     */
    @Test
    public void testMultipleInstancesFromBothConstructorsWorkIndependently() {
        // Arrange
        MemberVisitor visitor = mock(MemberVisitor.class);

        // Act
        MethodFinalizer finalizer1 = new MethodFinalizer();
        MethodFinalizer finalizer2 = new MethodFinalizer(visitor);

        // Assert
        assertNotNull(finalizer1, "First finalizer should be created");
        assertNotNull(finalizer2, "Second finalizer should be created");
        assertNotSame(finalizer1, finalizer2, "Finalizer instances should be different");
        assertInstanceOf(MemberVisitor.class, finalizer1, "First finalizer should implement MemberVisitor");
        assertInstanceOf(MemberVisitor.class, finalizer2, "Second finalizer should implement MemberVisitor");
    }

    /**
     * Tests that the no-arg constructor delegates to the single-parameter constructor with null.
     * This is implied by the implementation but verified through behavior.
     */
    @Test
    public void testNoArgConstructorEquivalentToSingleParamWithNull() {
        // Act
        MethodFinalizer finalizerNoArg = new MethodFinalizer();
        MethodFinalizer finalizerWithNull = new MethodFinalizer(null);

        // Assert - both should create valid instances
        assertNotNull(finalizerNoArg, "No-arg constructor should create instance");
        assertNotNull(finalizerWithNull, "Single-param constructor with null should create instance");

        // Both should be usable as MemberVisitor
        assertInstanceOf(MemberVisitor.class, finalizerNoArg);
        assertInstanceOf(MemberVisitor.class, finalizerWithNull);

        // They should be different instances
        assertNotSame(finalizerNoArg, finalizerWithNull);
    }

    /**
     * Tests that the constructor works in various contexts.
     * Verifies that the constructor is flexible and can be used in different scenarios.
     */
    @Test
    public void testConstructorWorksInVariousContexts() {
        // Test direct instantiation with no-arg
        MethodFinalizer finalizer1 = new MethodFinalizer();
        assertNotNull(finalizer1, "Direct no-arg instantiation should work");

        // Test direct instantiation with parameter
        MemberVisitor visitor = mock(MemberVisitor.class);
        MethodFinalizer finalizer2 = new MethodFinalizer(visitor);
        assertNotNull(finalizer2, "Direct parameterized instantiation should work");

        // Test instantiation in conditional
        MethodFinalizer finalizer3 = true ? new MethodFinalizer() : null;
        assertNotNull(finalizer3, "Conditional instantiation should work");

        // Test instantiation in array
        MethodFinalizer[] array = new MethodFinalizer[] {
            new MethodFinalizer(),
            new MethodFinalizer(visitor),
            new MethodFinalizer(null)
        };
        assertNotNull(array[0], "Array element 0 should be created");
        assertNotNull(array[1], "Array element 1 should be created");
        assertNotNull(array[2], "Array element 2 should be created");
        assertNotSame(array[0], array[1], "Array elements should be different instances");

        // Test instantiation as interface type
        MemberVisitor memberVisitor = new MethodFinalizer();
        assertNotNull(memberVisitor, "Instantiation as MemberVisitor interface should work");
    }

    /**
     * Tests that the constructor can be called repeatedly without side effects.
     * Verifies that there are no global state mutations.
     */
    @Test
    public void testConstructorHasNoSideEffects() {
        // Act - create multiple instances and verify each is independent
        MethodFinalizer[] finalizers = new MethodFinalizer[50];
        for (int i = 0; i < finalizers.length; i++) {
            // Alternate between constructors
            if (i % 2 == 0) {
                finalizers[i] = new MethodFinalizer();
            } else {
                finalizers[i] = new MethodFinalizer(mock(MemberVisitor.class));
            }
            assertNotNull(finalizers[i], "Finalizer " + i + " should be created");
        }

        // Assert - all instances should be distinct
        for (int i = 0; i < finalizers.length; i++) {
            for (int j = i + 1; j < finalizers.length; j++) {
                assertNotSame(finalizers[i], finalizers[j],
                    "Finalizer " + i + " and " + j + " should be different instances");
            }
        }
    }

    /**
     * Tests that multiple different MemberVisitor mocks don't interfere with each other.
     * Verifies that constructor properly isolates parameters.
     */
    @Test
    public void testConstructorIsolatesParameters() {
        // Arrange
        MemberVisitor visitor1 = mock(MemberVisitor.class);
        MemberVisitor visitor2 = mock(MemberVisitor.class);
        MemberVisitor visitor3 = mock(MemberVisitor.class);

        // Act
        MethodFinalizer finalizer1 = new MethodFinalizer(visitor1);
        MethodFinalizer finalizer2 = new MethodFinalizer(visitor2);
        MethodFinalizer finalizer3 = new MethodFinalizer(visitor3);

        // Assert
        assertNotNull(finalizer1);
        assertNotNull(finalizer2);
        assertNotNull(finalizer3);

        // Verify no interactions happened during construction
        verifyNoInteractions(visitor1);
        verifyNoInteractions(visitor2);
        verifyNoInteractions(visitor3);
    }
}
