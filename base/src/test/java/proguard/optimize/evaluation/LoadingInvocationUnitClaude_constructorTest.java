package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.evaluation.value.BasicValueFactory;
import proguard.evaluation.value.IdentifiedValueFactory;
import proguard.evaluation.value.ParticularValueFactory;
import proguard.evaluation.value.ValueFactory;
import proguard.evaluation.BasicInvocationUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LoadingInvocationUnit} constructors.
 * Tests the LoadingInvocationUnit(ValueFactory) constructor which delegates to the full constructor
 * with default boolean flags set to true, and the 4-parameter constructor
 * LoadingInvocationUnit(ValueFactory, boolean, boolean, boolean).
 */
public class LoadingInvocationUnitClaude_constructorTest {

    /**
     * Tests the constructor with a ParticularValueFactory.
     * Verifies that the LoadingInvocationUnit instance can be instantiated with a ParticularValueFactory.
     */
    @Test
    public void testConstructorWithParticularValueFactory() {
        // Arrange - Create a ParticularValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create LoadingInvocationUnit
        LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory);

        // Assert - Verify the LoadingInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "LoadingInvocationUnit should be instantiated successfully with ParticularValueFactory");
    }

    /**
     * Tests the constructor with a BasicValueFactory.
     * Verifies that the LoadingInvocationUnit instance can be instantiated with a BasicValueFactory.
     */
    @Test
    public void testConstructorWithBasicValueFactory() {
        // Arrange - Create a BasicValueFactory
        ValueFactory valueFactory = new BasicValueFactory();

        // Act - Create LoadingInvocationUnit
        LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory);

        // Assert - Verify the LoadingInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "LoadingInvocationUnit should be instantiated successfully with BasicValueFactory");
    }

    /**
     * Tests the constructor with an IdentifiedValueFactory.
     * Verifies that the LoadingInvocationUnit instance can be instantiated with an IdentifiedValueFactory.
     */
    @Test
    public void testConstructorWithIdentifiedValueFactory() {
        // Arrange - Create an IdentifiedValueFactory
        ValueFactory valueFactory = new IdentifiedValueFactory();

        // Act - Create LoadingInvocationUnit
        LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory);

        // Assert - Verify the LoadingInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "LoadingInvocationUnit should be instantiated successfully with IdentifiedValueFactory");
    }

    /**
     * Tests that the created LoadingInvocationUnit is a valid BasicInvocationUnit.
     * Verifies that LoadingInvocationUnit extends the BasicInvocationUnit class.
     */
    @Test
    public void testConstructorCreatesValidBasicInvocationUnit() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create LoadingInvocationUnit
        LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory);

        // Assert - Verify it extends BasicInvocationUnit
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                "LoadingInvocationUnit should extend BasicInvocationUnit");
    }

    /**
     * Tests that multiple LoadingInvocationUnit instances can be created independently.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testMultipleLoadingInvocationUnitInstances() {
        // Arrange - Create ValueFactories
        ValueFactory valueFactory1 = new ParticularValueFactory();
        ValueFactory valueFactory2 = new BasicValueFactory();

        // Act - Create two LoadingInvocationUnit instances
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(valueFactory1);
        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(valueFactory2);

        // Assert - Verify both instances were created and are different
        assertNotNull(invocationUnit1, "First LoadingInvocationUnit should be created");
        assertNotNull(invocationUnit2, "Second LoadingInvocationUnit should be created");
        assertNotSame(invocationUnit1, invocationUnit2, "LoadingInvocationUnit instances should be different objects");
    }

    /**
     * Tests that the same ValueFactory instance can be used to create multiple LoadingInvocationUnits.
     * Verifies that multiple invocation units can share the same value factory.
     */
    @Test
    public void testMultipleInstancesWithSameValueFactory() {
        // Arrange - Create a single ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create two LoadingInvocationUnit instances with the same ValueFactory
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(valueFactory);
        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(valueFactory);

        // Assert - Verify both instances were created and are different objects
        assertNotNull(invocationUnit1, "First LoadingInvocationUnit should be created");
        assertNotNull(invocationUnit2, "Second LoadingInvocationUnit should be created");
        assertNotSame(invocationUnit1, invocationUnit2, "LoadingInvocationUnit instances should be different objects even with same ValueFactory");
    }

    /**
     * Tests the constructor creates a BasicInvocationUnit with all ValueFactory types.
     * Verifies interface implementation with different ValueFactory implementations.
     */
    @Test
    public void testConstructorAlwaysCreatesBasicInvocationUnit() {
        // Arrange - Create different ValueFactory implementations
        ValueFactory particularValueFactory = new ParticularValueFactory();
        ValueFactory basicValueFactory = new BasicValueFactory();
        ValueFactory identifiedValueFactory = new IdentifiedValueFactory();

        // Act - Create LoadingInvocationUnits with different ValueFactories
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(particularValueFactory);
        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(basicValueFactory);
        LoadingInvocationUnit invocationUnit3 = new LoadingInvocationUnit(identifiedValueFactory);

        // Assert - Verify all extend BasicInvocationUnit
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit1,
                "LoadingInvocationUnit with ParticularValueFactory should extend BasicInvocationUnit");
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit2,
                "LoadingInvocationUnit with BasicValueFactory should extend BasicInvocationUnit");
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit3,
                "LoadingInvocationUnit with IdentifiedValueFactory should extend BasicInvocationUnit");
    }

    /**
     * Tests that the constructor can be called multiple times in sequence.
     * Verifies stability of the constructor when called repeatedly.
     */
    @Test
    public void testConstructorRepeatedInvocation() {
        // Act & Assert - Create multiple invocation units in sequence
        for (int i = 0; i < 5; i++) {
            ValueFactory valueFactory = (i % 2 == 0) ? new ParticularValueFactory() : new BasicValueFactory();
            LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory);
            assertNotNull(invocationUnit, "LoadingInvocationUnit should be created on iteration " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "LoadingInvocationUnit should extend BasicInvocationUnit on iteration " + i);
        }
    }

    /**
     * Tests that the constructor does not throw any exceptions with valid ValueFactory.
     * Verifies exception-free construction.
     */
    @Test
    public void testConstructorDoesNotThrowException() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act & Assert - Verify no exception is thrown
        assertDoesNotThrow(() -> new LoadingInvocationUnit(valueFactory),
                "Constructor should not throw exception with valid ValueFactory");
    }

    /**
     * Tests the constructor with different ValueFactory types in alternating order.
     * Verifies that the constructor handles different ValueFactory implementations correctly when alternated.
     */
    @Test
    public void testConstructorWithAlternatingValueFactoryTypes() {
        // Act - Create invocation units with alternating ValueFactory types
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(new ParticularValueFactory());
        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(new BasicValueFactory());
        LoadingInvocationUnit invocationUnit3 = new LoadingInvocationUnit(new IdentifiedValueFactory());
        LoadingInvocationUnit invocationUnit4 = new LoadingInvocationUnit(new ParticularValueFactory());

        // Assert - Verify all instances were created successfully
        assertNotNull(invocationUnit1, "LoadingInvocationUnit with ParticularValueFactory should be created");
        assertNotNull(invocationUnit2, "LoadingInvocationUnit with BasicValueFactory should be created");
        assertNotNull(invocationUnit3, "LoadingInvocationUnit with IdentifiedValueFactory should be created");
        assertNotNull(invocationUnit4, "LoadingInvocationUnit with ParticularValueFactory (second) should be created");

        // Verify they are all different instances
        assertNotSame(invocationUnit1, invocationUnit2, "invocationUnit1 and invocationUnit2 should be different");
        assertNotSame(invocationUnit1, invocationUnit3, "invocationUnit1 and invocationUnit3 should be different");
        assertNotSame(invocationUnit1, invocationUnit4, "invocationUnit1 and invocationUnit4 should be different");
        assertNotSame(invocationUnit2, invocationUnit3, "invocationUnit2 and invocationUnit3 should be different");
        assertNotSame(invocationUnit2, invocationUnit4, "invocationUnit2 and invocationUnit4 should be different");
        assertNotSame(invocationUnit3, invocationUnit4, "invocationUnit3 and invocationUnit4 should be different");
    }

    /**
     * Tests that multiple instances created with ParticularValueFactory are independent.
     * Verifies that creating multiple invocation units with the same factory type works correctly.
     */
    @Test
    public void testMultipleInstancesWithSameFactoryType() {
        // Act - Create multiple LoadingInvocationUnit instances with ParticularValueFactory
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(new ParticularValueFactory());
        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(new ParticularValueFactory());
        LoadingInvocationUnit invocationUnit3 = new LoadingInvocationUnit(new ParticularValueFactory());

        // Assert - Verify all instances were created successfully
        assertNotNull(invocationUnit1, "First LoadingInvocationUnit should be created");
        assertNotNull(invocationUnit2, "Second LoadingInvocationUnit should be created");
        assertNotNull(invocationUnit3, "Third LoadingInvocationUnit should be created");

        // Verify they are all different instances
        assertNotSame(invocationUnit1, invocationUnit2, "invocationUnit1 and invocationUnit2 should be different");
        assertNotSame(invocationUnit1, invocationUnit3, "invocationUnit1 and invocationUnit3 should be different");
        assertNotSame(invocationUnit2, invocationUnit3, "invocationUnit2 and invocationUnit3 should be different");
    }

    /**
     * Tests that the constructor works correctly with all available ValueFactory implementations.
     * Verifies compatibility with all common ValueFactory types.
     */
    @Test
    public void testConstructorWithAllValueFactoryImplementations() {
        // Act & Assert - Test with all available ValueFactory implementations
        ValueFactory particularValueFactory = new ParticularValueFactory();
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(particularValueFactory);
        assertNotNull(invocationUnit1, "LoadingInvocationUnit should work with ParticularValueFactory");
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit1);

        ValueFactory basicValueFactory = new BasicValueFactory();
        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(basicValueFactory);
        assertNotNull(invocationUnit2, "LoadingInvocationUnit should work with BasicValueFactory");
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit2);

        ValueFactory identifiedValueFactory = new IdentifiedValueFactory();
        LoadingInvocationUnit invocationUnit3 = new LoadingInvocationUnit(identifiedValueFactory);
        assertNotNull(invocationUnit3, "LoadingInvocationUnit should work with IdentifiedValueFactory");
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit3);
    }

    /**
     * Tests that each instance is truly independent by creating many instances.
     * Verifies that the constructor scales correctly and produces independent instances.
     */
    @Test
    public void testConstructorCreatesIndependentInstances() {
        // Arrange - Create multiple invocation units
        int instanceCount = 10;
        LoadingInvocationUnit[] invocationUnits = new LoadingInvocationUnit[instanceCount];

        // Act - Create instances
        for (int i = 0; i < instanceCount; i++) {
            ValueFactory valueFactory = new ParticularValueFactory();
            invocationUnits[i] = new LoadingInvocationUnit(valueFactory);
        }

        // Assert - Verify all instances are non-null and unique
        for (int i = 0; i < instanceCount; i++) {
            assertNotNull(invocationUnits[i], "Instance " + i + " should be non-null");
            for (int j = i + 1; j < instanceCount; j++) {
                assertNotSame(invocationUnits[i], invocationUnits[j],
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
            ValueFactory valueFactory = new ParticularValueFactory();
            LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory);

            // Assert - Verify each instance is valid
            assertNotNull(invocationUnit, "LoadingInvocationUnit should be created on attempt " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "LoadingInvocationUnit should extend BasicInvocationUnit on attempt " + i);
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
            ValueFactory valueFactory = new BasicValueFactory();
            LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory);

            // Assert - Verify each instance is valid
            assertNotNull(invocationUnit, "LoadingInvocationUnit should be created on attempt " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "LoadingInvocationUnit should extend BasicInvocationUnit on attempt " + i);
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
            ValueFactory valueFactory = new IdentifiedValueFactory();
            LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory);

            // Assert - Verify each instance is valid
            assertNotNull(invocationUnit, "LoadingInvocationUnit should be created on attempt " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "LoadingInvocationUnit should extend BasicInvocationUnit on attempt " + i);
        }
    }

    // ========== Tests for LoadingInvocationUnit(ValueFactory, boolean, boolean, boolean) constructor ==========

    /**
     * Tests the 4-parameter constructor with all boolean parameters set to true.
     * Verifies that the LoadingInvocationUnit can be instantiated with all flags enabled.
     */
    @Test
    public void testFourParameterConstructorWithAllTrue() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create LoadingInvocationUnit with all true
        LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory, true, true, true);

        // Assert - Verify the LoadingInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "LoadingInvocationUnit should be instantiated successfully with all parameters true");
    }

    /**
     * Tests the 4-parameter constructor with all boolean parameters set to false.
     * Verifies that the LoadingInvocationUnit can be instantiated with all flags disabled.
     */
    @Test
    public void testFourParameterConstructorWithAllFalse() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create LoadingInvocationUnit with all false
        LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory, false, false, false);

        // Assert - Verify the LoadingInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "LoadingInvocationUnit should be instantiated successfully with all parameters false");
    }

    /**
     * Tests the 4-parameter constructor with all 8 possible boolean combinations.
     * Verifies that all combinations of boolean parameters work correctly.
     */
    @Test
    public void testFourParameterConstructorWithAllBooleanCombinations() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act & Assert - Test all 8 combinations of boolean flags
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(valueFactory, true, true, true);
        assertNotNull(invocationUnit1, "LoadingInvocationUnit should work with (true, true, true)");

        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(valueFactory, true, true, false);
        assertNotNull(invocationUnit2, "LoadingInvocationUnit should work with (true, true, false)");

        LoadingInvocationUnit invocationUnit3 = new LoadingInvocationUnit(valueFactory, true, false, true);
        assertNotNull(invocationUnit3, "LoadingInvocationUnit should work with (true, false, true)");

        LoadingInvocationUnit invocationUnit4 = new LoadingInvocationUnit(valueFactory, true, false, false);
        assertNotNull(invocationUnit4, "LoadingInvocationUnit should work with (true, false, false)");

        LoadingInvocationUnit invocationUnit5 = new LoadingInvocationUnit(valueFactory, false, true, true);
        assertNotNull(invocationUnit5, "LoadingInvocationUnit should work with (false, true, true)");

        LoadingInvocationUnit invocationUnit6 = new LoadingInvocationUnit(valueFactory, false, true, false);
        assertNotNull(invocationUnit6, "LoadingInvocationUnit should work with (false, true, false)");

        LoadingInvocationUnit invocationUnit7 = new LoadingInvocationUnit(valueFactory, false, false, true);
        assertNotNull(invocationUnit7, "LoadingInvocationUnit should work with (false, false, true)");

        LoadingInvocationUnit invocationUnit8 = new LoadingInvocationUnit(valueFactory, false, false, false);
        assertNotNull(invocationUnit8, "LoadingInvocationUnit should work with (false, false, false)");
    }

    /**
     * Tests that the 4-parameter constructor creates a valid BasicInvocationUnit.
     * Verifies that the created instance extends the BasicInvocationUnit class.
     */
    @Test
    public void testFourParameterConstructorCreatesBasicInvocationUnit() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create LoadingInvocationUnit
        LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory, true, true, true);

        // Assert - Verify it extends BasicInvocationUnit
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                "LoadingInvocationUnit should extend BasicInvocationUnit");
    }

    /**
     * Tests the 4-parameter constructor with loadFieldValues set to true.
     * Verifies that the constructor works correctly when field value loading is enabled.
     */
    @Test
    public void testFourParameterConstructorWithLoadFieldValuesTrue() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create LoadingInvocationUnit with loadFieldValues=true
        LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory, true, false, false);

        // Assert - Verify the LoadingInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "LoadingInvocationUnit should be created with loadFieldValues=true");
    }

    /**
     * Tests the 4-parameter constructor with loadFieldValues set to false.
     * Verifies that the constructor works correctly when field value loading is disabled.
     */
    @Test
    public void testFourParameterConstructorWithLoadFieldValuesFalse() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create LoadingInvocationUnit with loadFieldValues=false
        LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory, false, true, true);

        // Assert - Verify the LoadingInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "LoadingInvocationUnit should be created with loadFieldValues=false");
    }

    /**
     * Tests the 4-parameter constructor with loadMethodParameterValues set to true.
     * Verifies that the constructor works correctly when method parameter value loading is enabled.
     */
    @Test
    public void testFourParameterConstructorWithLoadMethodParameterValuesTrue() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create LoadingInvocationUnit with loadMethodParameterValues=true
        LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory, false, true, false);

        // Assert - Verify the LoadingInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "LoadingInvocationUnit should be created with loadMethodParameterValues=true");
    }

    /**
     * Tests the 4-parameter constructor with loadMethodParameterValues set to false.
     * Verifies that the constructor works correctly when method parameter value loading is disabled.
     */
    @Test
    public void testFourParameterConstructorWithLoadMethodParameterValuesFalse() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create LoadingInvocationUnit with loadMethodParameterValues=false
        LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory, true, false, true);

        // Assert - Verify the LoadingInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "LoadingInvocationUnit should be created with loadMethodParameterValues=false");
    }

    /**
     * Tests the 4-parameter constructor with loadMethodReturnValues set to true.
     * Verifies that the constructor works correctly when method return value loading is enabled.
     */
    @Test
    public void testFourParameterConstructorWithLoadMethodReturnValuesTrue() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create LoadingInvocationUnit with loadMethodReturnValues=true
        LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory, false, false, true);

        // Assert - Verify the LoadingInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "LoadingInvocationUnit should be created with loadMethodReturnValues=true");
    }

    /**
     * Tests the 4-parameter constructor with loadMethodReturnValues set to false.
     * Verifies that the constructor works correctly when method return value loading is disabled.
     */
    @Test
    public void testFourParameterConstructorWithLoadMethodReturnValuesFalse() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create LoadingInvocationUnit with loadMethodReturnValues=false
        LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory, true, true, false);

        // Assert - Verify the LoadingInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "LoadingInvocationUnit should be created with loadMethodReturnValues=false");
    }

    /**
     * Tests that multiple LoadingInvocationUnit instances can be created with the 4-parameter constructor.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testFourParameterConstructorMultipleInstances() {
        // Arrange - Create ValueFactories
        ValueFactory valueFactory1 = new ParticularValueFactory();
        ValueFactory valueFactory2 = new BasicValueFactory();

        // Act - Create two LoadingInvocationUnit instances
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(valueFactory1, true, true, true);
        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(valueFactory2, false, false, false);

        // Assert - Verify both instances were created and are different
        assertNotNull(invocationUnit1, "First LoadingInvocationUnit should be created");
        assertNotNull(invocationUnit2, "Second LoadingInvocationUnit should be created");
        assertNotSame(invocationUnit1, invocationUnit2, "LoadingInvocationUnit instances should be different objects");
    }

    /**
     * Tests that the same ValueFactory can be used for multiple LoadingInvocationUnit instances.
     * Verifies that multiple invocation units can share the same value factory.
     */
    @Test
    public void testFourParameterConstructorWithSharedValueFactory() {
        // Arrange - Create a single ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create multiple LoadingInvocationUnit instances with the same ValueFactory
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(valueFactory, true, true, true);
        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(valueFactory, false, false, false);

        // Assert - Verify both instances were created successfully
        assertNotNull(invocationUnit1, "First LoadingInvocationUnit should be created");
        assertNotNull(invocationUnit2, "Second LoadingInvocationUnit should be created");
        assertNotSame(invocationUnit1, invocationUnit2, "Different LoadingInvocationUnit instances should be different objects");
    }

    /**
     * Tests that the 4-parameter constructor works with different ValueFactory implementations.
     * Verifies compatibility with various ValueFactory types.
     */
    @Test
    public void testFourParameterConstructorWithDifferentValueFactories() {
        // Act - Create LoadingInvocationUnits with different ValueFactory implementations
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(new ParticularValueFactory(), true, false, true);
        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(new BasicValueFactory(), false, true, false);
        LoadingInvocationUnit invocationUnit3 = new LoadingInvocationUnit(new IdentifiedValueFactory(), true, true, false);

        // Assert - Verify all instances were created successfully
        assertNotNull(invocationUnit1, "LoadingInvocationUnit should work with ParticularValueFactory");
        assertNotNull(invocationUnit2, "LoadingInvocationUnit should work with BasicValueFactory");
        assertNotNull(invocationUnit3, "LoadingInvocationUnit should work with IdentifiedValueFactory");
    }

    /**
     * Tests that the 4-parameter constructor does not throw exceptions with valid parameters.
     * Verifies exception-free construction.
     */
    @Test
    public void testFourParameterConstructorDoesNotThrowException() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act & Assert - Verify no exception is thrown
        assertDoesNotThrow(() -> new LoadingInvocationUnit(valueFactory, true, true, true),
                "Constructor should not throw exception with valid parameters");
    }

    /**
     * Tests that the 4-parameter constructor can be called multiple times in sequence.
     * Verifies stability of the constructor when called repeatedly.
     */
    @Test
    public void testFourParameterConstructorRepeatedInvocation() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act & Assert - Create multiple invocation units in sequence
        for (int i = 0; i < 8; i++) {
            boolean loadFieldValues = (i % 2 == 0);
            boolean loadMethodParameterValues = (i % 3 == 0);
            boolean loadMethodReturnValues = (i % 4 == 0);
            LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(
                    valueFactory, loadFieldValues, loadMethodParameterValues, loadMethodReturnValues);
            assertNotNull(invocationUnit, "LoadingInvocationUnit should be created on iteration " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "LoadingInvocationUnit should extend BasicInvocationUnit on iteration " + i);
        }
    }

    /**
     * Tests the 4-parameter constructor with multiple instances using same parameter combinations.
     * Verifies that instances with same parameters are still distinct objects.
     */
    @Test
    public void testFourParameterConstructorMultipleInstancesSameParameters() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create two LoadingInvocationUnit instances with same parameters
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(valueFactory, true, true, true);
        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(valueFactory, true, true, true);

        // Assert - Verify both instances were created and are different objects
        assertNotNull(invocationUnit1, "First LoadingInvocationUnit should be created");
        assertNotNull(invocationUnit2, "Second LoadingInvocationUnit should be created");
        assertNotSame(invocationUnit1, invocationUnit2, "LoadingInvocationUnit instances should be different objects even with same parameters");
    }

    /**
     * Tests that the 4-parameter constructor creates instances extending BasicInvocationUnit with all combinations.
     * Verifies inheritance across all parameter combinations.
     */
    @Test
    public void testFourParameterConstructorAlwaysCreatesBasicInvocationUnit() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act & Assert - Test all 8 combinations
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(valueFactory, true, true, true);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit1, "LoadingInvocationUnit with (true, true, true) should extend BasicInvocationUnit");

        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(valueFactory, true, true, false);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit2, "LoadingInvocationUnit with (true, true, false) should extend BasicInvocationUnit");

        LoadingInvocationUnit invocationUnit3 = new LoadingInvocationUnit(valueFactory, true, false, true);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit3, "LoadingInvocationUnit with (true, false, true) should extend BasicInvocationUnit");

        LoadingInvocationUnit invocationUnit4 = new LoadingInvocationUnit(valueFactory, true, false, false);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit4, "LoadingInvocationUnit with (true, false, false) should extend BasicInvocationUnit");

        LoadingInvocationUnit invocationUnit5 = new LoadingInvocationUnit(valueFactory, false, true, true);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit5, "LoadingInvocationUnit with (false, true, true) should extend BasicInvocationUnit");

        LoadingInvocationUnit invocationUnit6 = new LoadingInvocationUnit(valueFactory, false, true, false);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit6, "LoadingInvocationUnit with (false, true, false) should extend BasicInvocationUnit");

        LoadingInvocationUnit invocationUnit7 = new LoadingInvocationUnit(valueFactory, false, false, true);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit7, "LoadingInvocationUnit with (false, false, true) should extend BasicInvocationUnit");

        LoadingInvocationUnit invocationUnit8 = new LoadingInvocationUnit(valueFactory, false, false, false);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit8, "LoadingInvocationUnit with (false, false, false) should extend BasicInvocationUnit");
    }

    /**
     * Tests the 4-parameter constructor with various combinations of first two parameters.
     * Verifies that loadFieldValues and loadMethodParameterValues work together correctly.
     */
    @Test
    public void testFourParameterConstructorWithFieldAndParameterCombinations() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act & Assert - Test combinations of loadFieldValues and loadMethodParameterValues
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(valueFactory, true, true, false);
        assertNotNull(invocationUnit1, "Should work with loadFieldValues=true, loadMethodParameterValues=true");

        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(valueFactory, true, false, false);
        assertNotNull(invocationUnit2, "Should work with loadFieldValues=true, loadMethodParameterValues=false");

        LoadingInvocationUnit invocationUnit3 = new LoadingInvocationUnit(valueFactory, false, true, false);
        assertNotNull(invocationUnit3, "Should work with loadFieldValues=false, loadMethodParameterValues=true");

        LoadingInvocationUnit invocationUnit4 = new LoadingInvocationUnit(valueFactory, false, false, false);
        assertNotNull(invocationUnit4, "Should work with loadFieldValues=false, loadMethodParameterValues=false");
    }

    /**
     * Tests the 4-parameter constructor with various combinations of last two parameters.
     * Verifies that loadMethodParameterValues and loadMethodReturnValues work together correctly.
     */
    @Test
    public void testFourParameterConstructorWithParameterAndReturnCombinations() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act & Assert - Test combinations of loadMethodParameterValues and loadMethodReturnValues
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(valueFactory, false, true, true);
        assertNotNull(invocationUnit1, "Should work with loadMethodParameterValues=true, loadMethodReturnValues=true");

        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(valueFactory, false, true, false);
        assertNotNull(invocationUnit2, "Should work with loadMethodParameterValues=true, loadMethodReturnValues=false");

        LoadingInvocationUnit invocationUnit3 = new LoadingInvocationUnit(valueFactory, false, false, true);
        assertNotNull(invocationUnit3, "Should work with loadMethodParameterValues=false, loadMethodReturnValues=true");

        LoadingInvocationUnit invocationUnit4 = new LoadingInvocationUnit(valueFactory, false, false, false);
        assertNotNull(invocationUnit4, "Should work with loadMethodParameterValues=false, loadMethodReturnValues=false");
    }

    /**
     * Tests the 4-parameter constructor with various combinations of outer parameters.
     * Verifies that loadFieldValues and loadMethodReturnValues work together correctly.
     */
    @Test
    public void testFourParameterConstructorWithFieldAndReturnCombinations() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act & Assert - Test combinations of loadFieldValues and loadMethodReturnValues
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(valueFactory, true, false, true);
        assertNotNull(invocationUnit1, "Should work with loadFieldValues=true, loadMethodReturnValues=true");

        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(valueFactory, true, false, false);
        assertNotNull(invocationUnit2, "Should work with loadFieldValues=true, loadMethodReturnValues=false");

        LoadingInvocationUnit invocationUnit3 = new LoadingInvocationUnit(valueFactory, false, false, true);
        assertNotNull(invocationUnit3, "Should work with loadFieldValues=false, loadMethodReturnValues=true");

        LoadingInvocationUnit invocationUnit4 = new LoadingInvocationUnit(valueFactory, false, false, false);
        assertNotNull(invocationUnit4, "Should work with loadFieldValues=false, loadMethodReturnValues=false");
    }

    /**
     * Tests the 4-parameter constructor with all different ValueFactory types and boolean combinations.
     * Verifies that all ValueFactory implementations work with various boolean flag combinations.
     */
    @Test
    public void testFourParameterConstructorWithAllValueFactoryTypesAndBooleans() {
        // Act & Assert - Test with ParticularValueFactory
        LoadingInvocationUnit invocationUnit1 = new LoadingInvocationUnit(new ParticularValueFactory(), true, true, true);
        assertNotNull(invocationUnit1, "Should work with ParticularValueFactory and all true");

        LoadingInvocationUnit invocationUnit2 = new LoadingInvocationUnit(new ParticularValueFactory(), false, false, false);
        assertNotNull(invocationUnit2, "Should work with ParticularValueFactory and all false");

        // Test with BasicValueFactory
        LoadingInvocationUnit invocationUnit3 = new LoadingInvocationUnit(new BasicValueFactory(), true, false, true);
        assertNotNull(invocationUnit3, "Should work with BasicValueFactory");

        LoadingInvocationUnit invocationUnit4 = new LoadingInvocationUnit(new BasicValueFactory(), false, true, false);
        assertNotNull(invocationUnit4, "Should work with BasicValueFactory");

        // Test with IdentifiedValueFactory
        LoadingInvocationUnit invocationUnit5 = new LoadingInvocationUnit(new IdentifiedValueFactory(), true, true, false);
        assertNotNull(invocationUnit5, "Should work with IdentifiedValueFactory");

        LoadingInvocationUnit invocationUnit6 = new LoadingInvocationUnit(new IdentifiedValueFactory(), false, false, true);
        assertNotNull(invocationUnit6, "Should work with IdentifiedValueFactory");
    }

    /**
     * Tests that the 4-parameter constructor creates independent instances.
     * Verifies that many instances can be created with different parameter combinations.
     */
    @Test
    public void testFourParameterConstructorCreatesIndependentInstances() {
        // Arrange - Create multiple invocation units with different parameters
        int instanceCount = 8;
        LoadingInvocationUnit[] invocationUnits = new LoadingInvocationUnit[instanceCount];

        // Act - Create instances with all 8 boolean combinations
        ValueFactory valueFactory = new ParticularValueFactory();
        invocationUnits[0] = new LoadingInvocationUnit(valueFactory, true, true, true);
        invocationUnits[1] = new LoadingInvocationUnit(valueFactory, true, true, false);
        invocationUnits[2] = new LoadingInvocationUnit(valueFactory, true, false, true);
        invocationUnits[3] = new LoadingInvocationUnit(valueFactory, true, false, false);
        invocationUnits[4] = new LoadingInvocationUnit(valueFactory, false, true, true);
        invocationUnits[5] = new LoadingInvocationUnit(valueFactory, false, true, false);
        invocationUnits[6] = new LoadingInvocationUnit(valueFactory, false, false, true);
        invocationUnits[7] = new LoadingInvocationUnit(valueFactory, false, false, false);

        // Assert - Verify all instances are non-null and unique
        for (int i = 0; i < instanceCount; i++) {
            assertNotNull(invocationUnits[i], "Instance " + i + " should be non-null");
            for (int j = i + 1; j < instanceCount; j++) {
                assertNotSame(invocationUnits[i], invocationUnits[j],
                        "Instance " + i + " should be different from instance " + j);
            }
        }
    }

    /**
     * Tests the 4-parameter constructor consistency with ParticularValueFactory.
     * Verifies that repeated construction with the same parameters is stable.
     */
    @Test
    public void testFourParameterConstructorConsistencyWithParticularValueFactory() {
        // Act - Create multiple instances
        for (int i = 0; i < 3; i++) {
            ValueFactory valueFactory = new ParticularValueFactory();
            LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory, true, false, true);

            // Assert - Verify each instance is valid
            assertNotNull(invocationUnit, "LoadingInvocationUnit should be created on attempt " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "LoadingInvocationUnit should extend BasicInvocationUnit on attempt " + i);
        }
    }

    /**
     * Tests the 4-parameter constructor consistency with BasicValueFactory.
     * Verifies that repeated construction with BasicValueFactory is stable.
     */
    @Test
    public void testFourParameterConstructorConsistencyWithBasicValueFactory() {
        // Act - Create multiple instances
        for (int i = 0; i < 3; i++) {
            ValueFactory valueFactory = new BasicValueFactory();
            LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory, false, true, false);

            // Assert - Verify each instance is valid
            assertNotNull(invocationUnit, "LoadingInvocationUnit should be created on attempt " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "LoadingInvocationUnit should extend BasicInvocationUnit on attempt " + i);
        }
    }

    /**
     * Tests the 4-parameter constructor consistency with IdentifiedValueFactory.
     * Verifies that repeated construction with IdentifiedValueFactory is stable.
     */
    @Test
    public void testFourParameterConstructorConsistencyWithIdentifiedValueFactory() {
        // Act - Create multiple instances
        for (int i = 0; i < 3; i++) {
            ValueFactory valueFactory = new IdentifiedValueFactory();
            LoadingInvocationUnit invocationUnit = new LoadingInvocationUnit(valueFactory, true, true, false);

            // Assert - Verify each instance is valid
            assertNotNull(invocationUnit, "LoadingInvocationUnit should be created on attempt " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "LoadingInvocationUnit should extend BasicInvocationUnit on attempt " + i);
        }
    }
}
