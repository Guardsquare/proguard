package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.evaluation.BasicInvocationUnit;
import proguard.evaluation.value.BasicValueFactory;
import proguard.evaluation.value.IdentifiedValueFactory;
import proguard.evaluation.value.ParticularValueFactory;
import proguard.evaluation.value.ValueFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ParameterTracingInvocationUnit} constructor.
 * Tests the ParameterTracingInvocationUnit(SimplifiedInvocationUnit) constructor which delegates
 * to the parent ReferenceTracingInvocationUnit constructor.
 */
public class ParameterTracingInvocationUnitClaude_constructorTest {

    /**
     * Tests the constructor with a BasicInvocationUnit created with ParticularValueFactory.
     * Verifies that the ParameterTracingInvocationUnit instance can be instantiated.
     */
    @Test
    public void testConstructorWithBasicInvocationUnitFromParticularValueFactory() {
        // Arrange - Create a BasicInvocationUnit with ParticularValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();
        BasicInvocationUnit invocationUnit = new BasicInvocationUnit(valueFactory);

        // Act - Create ParameterTracingInvocationUnit
        ParameterTracingInvocationUnit parameterTracingUnit = new ParameterTracingInvocationUnit(invocationUnit);

        // Assert - Verify the ParameterTracingInvocationUnit instance was created successfully
        assertNotNull(parameterTracingUnit, "ParameterTracingInvocationUnit should be instantiated successfully");
    }

    /**
     * Tests the constructor with a BasicInvocationUnit created with BasicValueFactory.
     * Verifies that the ParameterTracingInvocationUnit instance can be instantiated.
     */
    @Test
    public void testConstructorWithBasicInvocationUnitFromBasicValueFactory() {
        // Arrange - Create a BasicInvocationUnit with BasicValueFactory
        ValueFactory valueFactory = new BasicValueFactory();
        BasicInvocationUnit invocationUnit = new BasicInvocationUnit(valueFactory);

        // Act - Create ParameterTracingInvocationUnit
        ParameterTracingInvocationUnit parameterTracingUnit = new ParameterTracingInvocationUnit(invocationUnit);

        // Assert - Verify the ParameterTracingInvocationUnit instance was created successfully
        assertNotNull(parameterTracingUnit, "ParameterTracingInvocationUnit should be instantiated successfully");
    }

    /**
     * Tests the constructor with a BasicInvocationUnit created with IdentifiedValueFactory.
     * Verifies that the ParameterTracingInvocationUnit instance can be instantiated.
     */
    @Test
    public void testConstructorWithBasicInvocationUnitFromIdentifiedValueFactory() {
        // Arrange - Create a BasicInvocationUnit with IdentifiedValueFactory
        ValueFactory valueFactory = new IdentifiedValueFactory();
        BasicInvocationUnit invocationUnit = new BasicInvocationUnit(valueFactory);

        // Act - Create ParameterTracingInvocationUnit
        ParameterTracingInvocationUnit parameterTracingUnit = new ParameterTracingInvocationUnit(invocationUnit);

        // Assert - Verify the ParameterTracingInvocationUnit instance was created successfully
        assertNotNull(parameterTracingUnit, "ParameterTracingInvocationUnit should be instantiated successfully");
    }

    /**
     * Tests that multiple ParameterTracingInvocationUnit instances can be created independently.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testMultipleParameterTracingInvocationUnitInstances() {
        // Arrange - Create BasicInvocationUnits
        BasicInvocationUnit invocationUnit1 = new BasicInvocationUnit(new ParticularValueFactory());
        BasicInvocationUnit invocationUnit2 = new BasicInvocationUnit(new BasicValueFactory());

        // Act - Create two ParameterTracingInvocationUnit instances
        ParameterTracingInvocationUnit parameterTracingUnit1 = new ParameterTracingInvocationUnit(invocationUnit1);
        ParameterTracingInvocationUnit parameterTracingUnit2 = new ParameterTracingInvocationUnit(invocationUnit2);

        // Assert - Verify both instances were created and are different
        assertNotNull(parameterTracingUnit1, "First ParameterTracingInvocationUnit should be created");
        assertNotNull(parameterTracingUnit2, "Second ParameterTracingInvocationUnit should be created");
        assertNotSame(parameterTracingUnit1, parameterTracingUnit2, "ParameterTracingInvocationUnit instances should be different objects");
    }

    /**
     * Tests that the same BasicInvocationUnit instance can be used to create multiple ParameterTracingInvocationUnits.
     * Verifies that multiple tracing units can share the same base invocation unit.
     */
    @Test
    public void testMultipleInstancesWithSameInvocationUnit() {
        // Arrange - Create a single BasicInvocationUnit
        BasicInvocationUnit invocationUnit = new BasicInvocationUnit(new ParticularValueFactory());

        // Act - Create two ParameterTracingInvocationUnit instances with the same BasicInvocationUnit
        ParameterTracingInvocationUnit parameterTracingUnit1 = new ParameterTracingInvocationUnit(invocationUnit);
        ParameterTracingInvocationUnit parameterTracingUnit2 = new ParameterTracingInvocationUnit(invocationUnit);

        // Assert - Verify both instances were created and are different objects
        assertNotNull(parameterTracingUnit1, "First ParameterTracingInvocationUnit should be created");
        assertNotNull(parameterTracingUnit2, "Second ParameterTracingInvocationUnit should be created");
        assertNotSame(parameterTracingUnit1, parameterTracingUnit2, "ParameterTracingInvocationUnit instances should be different objects even with same invocation unit");
    }

    /**
     * Tests that the constructor can be called multiple times in sequence.
     * Verifies stability of the constructor when called repeatedly.
     */
    @Test
    public void testConstructorRepeatedInvocation() {
        // Act & Assert - Create multiple tracing units in sequence
        for (int i = 0; i < 5; i++) {
            ValueFactory valueFactory = (i % 2 == 0) ? new ParticularValueFactory() : new BasicValueFactory();
            BasicInvocationUnit invocationUnit = new BasicInvocationUnit(valueFactory);
            ParameterTracingInvocationUnit parameterTracingUnit = new ParameterTracingInvocationUnit(invocationUnit);
            assertNotNull(parameterTracingUnit, "ParameterTracingInvocationUnit should be created on iteration " + i);
        }
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid invocation unit.
     * Verifies exception-free construction.
     */
    @Test
    public void testConstructorDoesNotThrowException() {
        // Arrange - Create a BasicInvocationUnit
        BasicInvocationUnit invocationUnit = new BasicInvocationUnit(new ParticularValueFactory());

        // Act & Assert - Verify no exception is thrown
        assertDoesNotThrow(() -> new ParameterTracingInvocationUnit(invocationUnit),
                "Constructor should not throw exception with valid invocation unit");
    }

    /**
     * Tests the constructor with different BasicInvocationUnit types in alternating order.
     * Verifies that the constructor handles different invocation unit configurations correctly.
     */
    @Test
    public void testConstructorWithAlternatingInvocationUnitTypes() {
        // Act - Create tracing units with alternating ValueFactory types
        ParameterTracingInvocationUnit parameterTracingUnit1 = new ParameterTracingInvocationUnit(
                new BasicInvocationUnit(new ParticularValueFactory()));
        ParameterTracingInvocationUnit parameterTracingUnit2 = new ParameterTracingInvocationUnit(
                new BasicInvocationUnit(new BasicValueFactory()));
        ParameterTracingInvocationUnit parameterTracingUnit3 = new ParameterTracingInvocationUnit(
                new BasicInvocationUnit(new IdentifiedValueFactory()));
        ParameterTracingInvocationUnit parameterTracingUnit4 = new ParameterTracingInvocationUnit(
                new BasicInvocationUnit(new ParticularValueFactory()));

        // Assert - Verify all instances were created successfully
        assertNotNull(parameterTracingUnit1, "ParameterTracingInvocationUnit with ParticularValueFactory should be created");
        assertNotNull(parameterTracingUnit2, "ParameterTracingInvocationUnit with BasicValueFactory should be created");
        assertNotNull(parameterTracingUnit3, "ParameterTracingInvocationUnit with IdentifiedValueFactory should be created");
        assertNotNull(parameterTracingUnit4, "ParameterTracingInvocationUnit with ParticularValueFactory (second) should be created");

        // Verify they are all different instances
        assertNotSame(parameterTracingUnit1, parameterTracingUnit2, "parameterTracingUnit1 and parameterTracingUnit2 should be different");
        assertNotSame(parameterTracingUnit1, parameterTracingUnit3, "parameterTracingUnit1 and parameterTracingUnit3 should be different");
        assertNotSame(parameterTracingUnit1, parameterTracingUnit4, "parameterTracingUnit1 and parameterTracingUnit4 should be different");
        assertNotSame(parameterTracingUnit2, parameterTracingUnit3, "parameterTracingUnit2 and parameterTracingUnit3 should be different");
        assertNotSame(parameterTracingUnit2, parameterTracingUnit4, "parameterTracingUnit2 and parameterTracingUnit4 should be different");
        assertNotSame(parameterTracingUnit3, parameterTracingUnit4, "parameterTracingUnit3 and parameterTracingUnit4 should be different");
    }

    /**
     * Tests that multiple instances created with the same factory type are independent.
     * Verifies that creating multiple tracing units with the same factory type works correctly.
     */
    @Test
    public void testMultipleInstancesWithSameFactoryType() {
        // Act - Create multiple ParameterTracingInvocationUnit instances with ParticularValueFactory
        ParameterTracingInvocationUnit parameterTracingUnit1 = new ParameterTracingInvocationUnit(
                new BasicInvocationUnit(new ParticularValueFactory()));
        ParameterTracingInvocationUnit parameterTracingUnit2 = new ParameterTracingInvocationUnit(
                new BasicInvocationUnit(new ParticularValueFactory()));
        ParameterTracingInvocationUnit parameterTracingUnit3 = new ParameterTracingInvocationUnit(
                new BasicInvocationUnit(new ParticularValueFactory()));

        // Assert - Verify all instances were created successfully
        assertNotNull(parameterTracingUnit1, "First ParameterTracingInvocationUnit should be created");
        assertNotNull(parameterTracingUnit2, "Second ParameterTracingInvocationUnit should be created");
        assertNotNull(parameterTracingUnit3, "Third ParameterTracingInvocationUnit should be created");

        // Verify they are all different instances
        assertNotSame(parameterTracingUnit1, parameterTracingUnit2, "parameterTracingUnit1 and parameterTracingUnit2 should be different");
        assertNotSame(parameterTracingUnit1, parameterTracingUnit3, "parameterTracingUnit1 and parameterTracingUnit3 should be different");
        assertNotSame(parameterTracingUnit2, parameterTracingUnit3, "parameterTracingUnit2 and parameterTracingUnit3 should be different");
    }

    /**
     * Tests that the constructor works correctly with all available ValueFactory implementations.
     * Verifies compatibility with all common ValueFactory types.
     */
    @Test
    public void testConstructorWithAllValueFactoryImplementations() {
        // Act & Assert - Test with all available ValueFactory implementations
        ValueFactory particularValueFactory = new ParticularValueFactory();
        ParameterTracingInvocationUnit parameterTracingUnit1 = new ParameterTracingInvocationUnit(
                new BasicInvocationUnit(particularValueFactory));
        assertNotNull(parameterTracingUnit1, "ParameterTracingInvocationUnit should work with ParticularValueFactory");

        ValueFactory basicValueFactory = new BasicValueFactory();
        ParameterTracingInvocationUnit parameterTracingUnit2 = new ParameterTracingInvocationUnit(
                new BasicInvocationUnit(basicValueFactory));
        assertNotNull(parameterTracingUnit2, "ParameterTracingInvocationUnit should work with BasicValueFactory");

        ValueFactory identifiedValueFactory = new IdentifiedValueFactory();
        ParameterTracingInvocationUnit parameterTracingUnit3 = new ParameterTracingInvocationUnit(
                new BasicInvocationUnit(identifiedValueFactory));
        assertNotNull(parameterTracingUnit3, "ParameterTracingInvocationUnit should work with IdentifiedValueFactory");
    }

    /**
     * Tests that each instance is truly independent by creating many instances.
     * Verifies that the constructor scales correctly and produces independent instances.
     */
    @Test
    public void testConstructorCreatesIndependentInstances() {
        // Arrange - Create multiple tracing units
        int instanceCount = 10;
        ParameterTracingInvocationUnit[] tracingUnits = new ParameterTracingInvocationUnit[instanceCount];

        // Act - Create instances
        for (int i = 0; i < instanceCount; i++) {
            BasicInvocationUnit invocationUnit = new BasicInvocationUnit(new ParticularValueFactory());
            tracingUnits[i] = new ParameterTracingInvocationUnit(invocationUnit);
        }

        // Assert - Verify all instances are non-null and unique
        for (int i = 0; i < instanceCount; i++) {
            assertNotNull(tracingUnits[i], "Instance " + i + " should be non-null");
            for (int j = i + 1; j < instanceCount; j++) {
                assertNotSame(tracingUnits[i], tracingUnits[j],
                        "Instance " + i + " should be different from instance " + j);
            }
        }
    }

    /**
     * Tests the constructor with ParticularValueFactory multiple times to ensure consistency.
     * Verifies that repeated construction with the same factory type is stable.
     */
    @Test
    public void testConstructorConsistencyWithParticularValueFactory() {
        // Act - Create multiple instances
        for (int i = 0; i < 3; i++) {
            BasicInvocationUnit invocationUnit = new BasicInvocationUnit(new ParticularValueFactory());
            ParameterTracingInvocationUnit parameterTracingUnit = new ParameterTracingInvocationUnit(invocationUnit);

            // Assert - Verify each instance is valid
            assertNotNull(parameterTracingUnit, "ParameterTracingInvocationUnit should be created on attempt " + i);
        }
    }

    /**
     * Tests the constructor with BasicValueFactory multiple times to ensure consistency.
     * Verifies that repeated construction with BasicValueFactory is stable.
     */
    @Test
    public void testConstructorConsistencyWithBasicValueFactory() {
        // Act - Create multiple instances
        for (int i = 0; i < 3; i++) {
            BasicInvocationUnit invocationUnit = new BasicInvocationUnit(new BasicValueFactory());
            ParameterTracingInvocationUnit parameterTracingUnit = new ParameterTracingInvocationUnit(invocationUnit);

            // Assert - Verify each instance is valid
            assertNotNull(parameterTracingUnit, "ParameterTracingInvocationUnit should be created on attempt " + i);
        }
    }

    /**
     * Tests the constructor with IdentifiedValueFactory multiple times to ensure consistency.
     * Verifies that repeated construction with IdentifiedValueFactory is stable.
     */
    @Test
    public void testConstructorConsistencyWithIdentifiedValueFactory() {
        // Act - Create multiple instances
        for (int i = 0; i < 3; i++) {
            BasicInvocationUnit invocationUnit = new BasicInvocationUnit(new IdentifiedValueFactory());
            ParameterTracingInvocationUnit parameterTracingUnit = new ParameterTracingInvocationUnit(invocationUnit);

            // Assert - Verify each instance is valid
            assertNotNull(parameterTracingUnit, "ParameterTracingInvocationUnit should be created on attempt " + i);
        }
    }

    /**
     * Tests the constructor with a LoadingInvocationUnit (which is also a SimplifiedInvocationUnit).
     * Verifies that the constructor works with different SimplifiedInvocationUnit implementations.
     */
    @Test
    public void testConstructorWithLoadingInvocationUnit() {
        // Arrange - Create a LoadingInvocationUnit
        LoadingInvocationUnit loadingInvocationUnit = new LoadingInvocationUnit(new ParticularValueFactory());

        // Act - Create ParameterTracingInvocationUnit with LoadingInvocationUnit
        ParameterTracingInvocationUnit parameterTracingUnit = new ParameterTracingInvocationUnit(loadingInvocationUnit);

        // Assert - Verify the ParameterTracingInvocationUnit instance was created successfully
        assertNotNull(parameterTracingUnit, "ParameterTracingInvocationUnit should work with LoadingInvocationUnit");
    }

    /**
     * Tests the constructor with different SimplifiedInvocationUnit implementations.
     * Verifies that the constructor handles different implementations correctly.
     */
    @Test
    public void testConstructorWithDifferentInvocationUnitImplementations() {
        // Act - Create ParameterTracingInvocationUnits with different invocation unit types
        ParameterTracingInvocationUnit parameterTracingUnit1 = new ParameterTracingInvocationUnit(
                new BasicInvocationUnit(new ParticularValueFactory()));
        ParameterTracingInvocationUnit parameterTracingUnit2 = new ParameterTracingInvocationUnit(
                new LoadingInvocationUnit(new ParticularValueFactory()));

        // Assert - Verify both instances were created successfully
        assertNotNull(parameterTracingUnit1, "ParameterTracingInvocationUnit should work with BasicInvocationUnit");
        assertNotNull(parameterTracingUnit2, "ParameterTracingInvocationUnit should work with LoadingInvocationUnit");
        assertNotSame(parameterTracingUnit1, parameterTracingUnit2, "Instances should be different objects");
    }

    /**
     * Tests that the constructor can handle creating instances in a mixed pattern.
     * Verifies stability when alternating between different invocation unit types.
     */
    @Test
    public void testConstructorWithMixedInvocationUnitPattern() {
        // Act - Create instances with mixed patterns
        ParameterTracingInvocationUnit parameterTracingUnit1 = new ParameterTracingInvocationUnit(
                new BasicInvocationUnit(new ParticularValueFactory()));
        ParameterTracingInvocationUnit parameterTracingUnit2 = new ParameterTracingInvocationUnit(
                new LoadingInvocationUnit(new BasicValueFactory()));
        ParameterTracingInvocationUnit parameterTracingUnit3 = new ParameterTracingInvocationUnit(
                new BasicInvocationUnit(new IdentifiedValueFactory()));
        ParameterTracingInvocationUnit parameterTracingUnit4 = new ParameterTracingInvocationUnit(
                new LoadingInvocationUnit(new ParticularValueFactory()));

        // Assert - Verify all instances were created successfully and are distinct
        assertNotNull(parameterTracingUnit1, "First ParameterTracingInvocationUnit should be created");
        assertNotNull(parameterTracingUnit2, "Second ParameterTracingInvocationUnit should be created");
        assertNotNull(parameterTracingUnit3, "Third ParameterTracingInvocationUnit should be created");
        assertNotNull(parameterTracingUnit4, "Fourth ParameterTracingInvocationUnit should be created");

        assertNotSame(parameterTracingUnit1, parameterTracingUnit2);
        assertNotSame(parameterTracingUnit1, parameterTracingUnit3);
        assertNotSame(parameterTracingUnit1, parameterTracingUnit4);
        assertNotSame(parameterTracingUnit2, parameterTracingUnit3);
        assertNotSame(parameterTracingUnit2, parameterTracingUnit4);
        assertNotSame(parameterTracingUnit3, parameterTracingUnit4);
    }

    /**
     * Tests that creating many ParameterTracingInvocationUnit instances in succession works correctly.
     * Verifies the constructor can handle bulk instantiation.
     */
    @Test
    public void testConstructorBulkInstantiation() {
        // Arrange - Prepare to create many instances
        int count = 20;
        ParameterTracingInvocationUnit[] units = new ParameterTracingInvocationUnit[count];

        // Act - Create many instances
        for (int i = 0; i < count; i++) {
            ValueFactory factory = (i % 3 == 0) ? new ParticularValueFactory() :
                                   (i % 3 == 1) ? new BasicValueFactory() :
                                   new IdentifiedValueFactory();
            units[i] = new ParameterTracingInvocationUnit(new BasicInvocationUnit(factory));
        }

        // Assert - Verify all instances are created and unique
        for (int i = 0; i < count; i++) {
            assertNotNull(units[i], "Instance " + i + " should be created");
        }
        // Verify first and last are different
        assertNotSame(units[0], units[count - 1], "First and last instances should be different");
    }

    /**
     * Tests the constructor with LoadingInvocationUnit configured with different boolean flags.
     * Verifies that the constructor works with different LoadingInvocationUnit configurations.
     */
    @Test
    public void testConstructorWithConfiguredLoadingInvocationUnit() {
        // Act - Create ParameterTracingInvocationUnit with differently configured LoadingInvocationUnits
        ParameterTracingInvocationUnit parameterTracingUnit1 = new ParameterTracingInvocationUnit(
                new LoadingInvocationUnit(new ParticularValueFactory(), true, true, true));
        ParameterTracingInvocationUnit parameterTracingUnit2 = new ParameterTracingInvocationUnit(
                new LoadingInvocationUnit(new ParticularValueFactory(), false, false, false));

        // Assert - Verify both instances were created successfully
        assertNotNull(parameterTracingUnit1, "ParameterTracingInvocationUnit should work with LoadingInvocationUnit (all true)");
        assertNotNull(parameterTracingUnit2, "ParameterTracingInvocationUnit should work with LoadingInvocationUnit (all false)");
        assertNotSame(parameterTracingUnit1, parameterTracingUnit2, "Instances should be different objects");
    }
}
