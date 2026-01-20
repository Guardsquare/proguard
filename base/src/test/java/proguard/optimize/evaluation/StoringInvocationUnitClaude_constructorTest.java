package proguard.optimize.evaluation;

import org.junit.jupiter.api.Test;
import proguard.evaluation.value.BasicValueFactory;
import proguard.evaluation.value.IdentifiedValueFactory;
import proguard.evaluation.value.ParticularValueFactory;
import proguard.evaluation.value.ValueFactory;
import proguard.evaluation.BasicInvocationUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link StoringInvocationUnit} constructors.
 * Tests the StoringInvocationUnit(ValueFactory) constructor which delegates to the full constructor
 * with default boolean flags set to true, and the 4-parameter constructor
 * StoringInvocationUnit(ValueFactory, boolean, boolean, boolean).
 */
public class StoringInvocationUnitClaude_constructorTest {

    /**
     * Tests the constructor with a ParticularValueFactory.
     * Verifies that the StoringInvocationUnit instance can be instantiated with a ParticularValueFactory.
     */
    @Test
    public void testConstructorWithParticularValueFactory() {
        // Arrange - Create a ParticularValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create StoringInvocationUnit
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        // Assert - Verify the StoringInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "StoringInvocationUnit should be instantiated successfully with ParticularValueFactory");
    }

    /**
     * Tests the constructor with a BasicValueFactory.
     * Verifies that the StoringInvocationUnit instance can be instantiated with a BasicValueFactory.
     */
    @Test
    public void testConstructorWithBasicValueFactory() {
        // Arrange - Create a BasicValueFactory
        ValueFactory valueFactory = new BasicValueFactory();

        // Act - Create StoringInvocationUnit
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        // Assert - Verify the StoringInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "StoringInvocationUnit should be instantiated successfully with BasicValueFactory");
    }

    /**
     * Tests the constructor with an IdentifiedValueFactory.
     * Verifies that the StoringInvocationUnit instance can be instantiated with an IdentifiedValueFactory.
     */
    @Test
    public void testConstructorWithIdentifiedValueFactory() {
        // Arrange - Create an IdentifiedValueFactory
        ValueFactory valueFactory = new IdentifiedValueFactory();

        // Act - Create StoringInvocationUnit
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        // Assert - Verify the StoringInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "StoringInvocationUnit should be instantiated successfully with IdentifiedValueFactory");
    }

    /**
     * Tests that the created StoringInvocationUnit is a valid BasicInvocationUnit.
     * Verifies that StoringInvocationUnit extends the BasicInvocationUnit class.
     */
    @Test
    public void testConstructorCreatesValidBasicInvocationUnit() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create StoringInvocationUnit
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

        // Assert - Verify it extends BasicInvocationUnit
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                "StoringInvocationUnit should extend BasicInvocationUnit");
    }

    /**
     * Tests that multiple StoringInvocationUnit instances can be created independently.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testMultipleStoringInvocationUnitInstances() {
        // Arrange - Create ValueFactories
        ValueFactory valueFactory1 = new ParticularValueFactory();
        ValueFactory valueFactory2 = new BasicValueFactory();

        // Act - Create two StoringInvocationUnit instances
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(valueFactory1);
        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(valueFactory2);

        // Assert - Verify both instances were created and are different
        assertNotNull(invocationUnit1, "First StoringInvocationUnit should be created");
        assertNotNull(invocationUnit2, "Second StoringInvocationUnit should be created");
        assertNotSame(invocationUnit1, invocationUnit2, "StoringInvocationUnit instances should be different objects");
    }

    /**
     * Tests that the same ValueFactory instance can be used to create multiple StoringInvocationUnits.
     * Verifies that multiple invocation units can share the same value factory.
     */
    @Test
    public void testMultipleInstancesWithSameValueFactory() {
        // Arrange - Create a single ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create two StoringInvocationUnit instances with the same ValueFactory
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(valueFactory);
        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(valueFactory);

        // Assert - Verify both instances were created and are different objects
        assertNotNull(invocationUnit1, "First StoringInvocationUnit should be created");
        assertNotNull(invocationUnit2, "Second StoringInvocationUnit should be created");
        assertNotSame(invocationUnit1, invocationUnit2, "StoringInvocationUnit instances should be different objects even with same ValueFactory");
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

        // Act - Create StoringInvocationUnits with different ValueFactories
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(particularValueFactory);
        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(basicValueFactory);
        StoringInvocationUnit invocationUnit3 = new StoringInvocationUnit(identifiedValueFactory);

        // Assert - Verify all extend BasicInvocationUnit
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit1,
                "StoringInvocationUnit with ParticularValueFactory should extend BasicInvocationUnit");
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit2,
                "StoringInvocationUnit with BasicValueFactory should extend BasicInvocationUnit");
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit3,
                "StoringInvocationUnit with IdentifiedValueFactory should extend BasicInvocationUnit");
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
            StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);
            assertNotNull(invocationUnit, "StoringInvocationUnit should be created on iteration " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "StoringInvocationUnit should extend BasicInvocationUnit on iteration " + i);
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
        assertDoesNotThrow(() -> new StoringInvocationUnit(valueFactory),
                "Constructor should not throw exception with valid ValueFactory");
    }

    /**
     * Tests the constructor with different ValueFactory types in alternating order.
     * Verifies that the constructor handles different ValueFactory implementations correctly when alternated.
     */
    @Test
    public void testConstructorWithAlternatingValueFactoryTypes() {
        // Act - Create invocation units with alternating ValueFactory types
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(new ParticularValueFactory());
        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(new BasicValueFactory());
        StoringInvocationUnit invocationUnit3 = new StoringInvocationUnit(new IdentifiedValueFactory());
        StoringInvocationUnit invocationUnit4 = new StoringInvocationUnit(new ParticularValueFactory());

        // Assert - Verify all instances were created successfully
        assertNotNull(invocationUnit1, "StoringInvocationUnit with ParticularValueFactory should be created");
        assertNotNull(invocationUnit2, "StoringInvocationUnit with BasicValueFactory should be created");
        assertNotNull(invocationUnit3, "StoringInvocationUnit with IdentifiedValueFactory should be created");
        assertNotNull(invocationUnit4, "StoringInvocationUnit with ParticularValueFactory (second) should be created");

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
        // Act - Create multiple StoringInvocationUnit instances with ParticularValueFactory
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(new ParticularValueFactory());
        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(new ParticularValueFactory());
        StoringInvocationUnit invocationUnit3 = new StoringInvocationUnit(new ParticularValueFactory());

        // Assert - Verify all instances were created successfully
        assertNotNull(invocationUnit1, "First StoringInvocationUnit should be created");
        assertNotNull(invocationUnit2, "Second StoringInvocationUnit should be created");
        assertNotNull(invocationUnit3, "Third StoringInvocationUnit should be created");

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
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(particularValueFactory);
        assertNotNull(invocationUnit1, "StoringInvocationUnit should work with ParticularValueFactory");
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit1);

        ValueFactory basicValueFactory = new BasicValueFactory();
        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(basicValueFactory);
        assertNotNull(invocationUnit2, "StoringInvocationUnit should work with BasicValueFactory");
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit2);

        ValueFactory identifiedValueFactory = new IdentifiedValueFactory();
        StoringInvocationUnit invocationUnit3 = new StoringInvocationUnit(identifiedValueFactory);
        assertNotNull(invocationUnit3, "StoringInvocationUnit should work with IdentifiedValueFactory");
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
        StoringInvocationUnit[] invocationUnits = new StoringInvocationUnit[instanceCount];

        // Act - Create instances
        for (int i = 0; i < instanceCount; i++) {
            ValueFactory valueFactory = new ParticularValueFactory();
            invocationUnits[i] = new StoringInvocationUnit(valueFactory);
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
            StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

            // Assert - Verify each instance is valid
            assertNotNull(invocationUnit, "StoringInvocationUnit should be created on attempt " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "StoringInvocationUnit should extend BasicInvocationUnit on attempt " + i);
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
            StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

            // Assert - Verify each instance is valid
            assertNotNull(invocationUnit, "StoringInvocationUnit should be created on attempt " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "StoringInvocationUnit should extend BasicInvocationUnit on attempt " + i);
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
            StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory);

            // Assert - Verify each instance is valid
            assertNotNull(invocationUnit, "StoringInvocationUnit should be created on attempt " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "StoringInvocationUnit should extend BasicInvocationUnit on attempt " + i);
        }
    }

    // ========== Tests for StoringInvocationUnit(ValueFactory, boolean, boolean, boolean) constructor ==========

    /**
     * Tests the 4-parameter constructor with all boolean parameters set to true.
     * Verifies that the StoringInvocationUnit can be instantiated with all flags enabled.
     */
    @Test
    public void testFourParameterConstructorWithAllTrue() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create StoringInvocationUnit with all true
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, true, true, true);

        // Assert - Verify the StoringInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "StoringInvocationUnit should be instantiated successfully with all parameters true");
    }

    /**
     * Tests the 4-parameter constructor with all boolean parameters set to false.
     * Verifies that the StoringInvocationUnit can be instantiated with all flags disabled.
     */
    @Test
    public void testFourParameterConstructorWithAllFalse() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create StoringInvocationUnit with all false
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, false, false);

        // Assert - Verify the StoringInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "StoringInvocationUnit should be instantiated successfully with all parameters false");
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
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(valueFactory, true, true, true);
        assertNotNull(invocationUnit1, "StoringInvocationUnit should work with (true, true, true)");

        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(valueFactory, true, true, false);
        assertNotNull(invocationUnit2, "StoringInvocationUnit should work with (true, true, false)");

        StoringInvocationUnit invocationUnit3 = new StoringInvocationUnit(valueFactory, true, false, true);
        assertNotNull(invocationUnit3, "StoringInvocationUnit should work with (true, false, true)");

        StoringInvocationUnit invocationUnit4 = new StoringInvocationUnit(valueFactory, true, false, false);
        assertNotNull(invocationUnit4, "StoringInvocationUnit should work with (true, false, false)");

        StoringInvocationUnit invocationUnit5 = new StoringInvocationUnit(valueFactory, false, true, true);
        assertNotNull(invocationUnit5, "StoringInvocationUnit should work with (false, true, true)");

        StoringInvocationUnit invocationUnit6 = new StoringInvocationUnit(valueFactory, false, true, false);
        assertNotNull(invocationUnit6, "StoringInvocationUnit should work with (false, true, false)");

        StoringInvocationUnit invocationUnit7 = new StoringInvocationUnit(valueFactory, false, false, true);
        assertNotNull(invocationUnit7, "StoringInvocationUnit should work with (false, false, true)");

        StoringInvocationUnit invocationUnit8 = new StoringInvocationUnit(valueFactory, false, false, false);
        assertNotNull(invocationUnit8, "StoringInvocationUnit should work with (false, false, false)");
    }

    /**
     * Tests that the 4-parameter constructor creates a valid BasicInvocationUnit.
     * Verifies that the created instance extends the BasicInvocationUnit class.
     */
    @Test
    public void testFourParameterConstructorCreatesBasicInvocationUnit() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create StoringInvocationUnit
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, true, true, true);

        // Assert - Verify it extends BasicInvocationUnit
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                "StoringInvocationUnit should extend BasicInvocationUnit");
    }

    /**
     * Tests the 4-parameter constructor with storeFieldValues set to true.
     * Verifies that the constructor works correctly when field value storing is enabled.
     */
    @Test
    public void testFourParameterConstructorWithStoreFieldValuesTrue() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create StoringInvocationUnit with storeFieldValues=true
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, true, false, false);

        // Assert - Verify the StoringInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "StoringInvocationUnit should be created with storeFieldValues=true");
    }

    /**
     * Tests the 4-parameter constructor with storeFieldValues set to false.
     * Verifies that the constructor works correctly when field value storing is disabled.
     */
    @Test
    public void testFourParameterConstructorWithStoreFieldValuesFalse() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create StoringInvocationUnit with storeFieldValues=false
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, true, true);

        // Assert - Verify the StoringInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "StoringInvocationUnit should be created with storeFieldValues=false");
    }

    /**
     * Tests the 4-parameter constructor with storeMethodParameterValues set to true.
     * Verifies that the constructor works correctly when method parameter value storing is enabled.
     */
    @Test
    public void testFourParameterConstructorWithStoreMethodParameterValuesTrue() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create StoringInvocationUnit with storeMethodParameterValues=true
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, true, false);

        // Assert - Verify the StoringInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "StoringInvocationUnit should be created with storeMethodParameterValues=true");
    }

    /**
     * Tests the 4-parameter constructor with storeMethodParameterValues set to false.
     * Verifies that the constructor works correctly when method parameter value storing is disabled.
     */
    @Test
    public void testFourParameterConstructorWithStoreMethodParameterValuesFalse() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create StoringInvocationUnit with storeMethodParameterValues=false
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, true, false, true);

        // Assert - Verify the StoringInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "StoringInvocationUnit should be created with storeMethodParameterValues=false");
    }

    /**
     * Tests the 4-parameter constructor with storeMethodReturnValues set to true.
     * Verifies that the constructor works correctly when method return value storing is enabled.
     */
    @Test
    public void testFourParameterConstructorWithStoreMethodReturnValuesTrue() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create StoringInvocationUnit with storeMethodReturnValues=true
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, false, true);

        // Assert - Verify the StoringInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "StoringInvocationUnit should be created with storeMethodReturnValues=true");
    }

    /**
     * Tests the 4-parameter constructor with storeMethodReturnValues set to false.
     * Verifies that the constructor works correctly when method return value storing is disabled.
     */
    @Test
    public void testFourParameterConstructorWithStoreMethodReturnValuesFalse() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create StoringInvocationUnit with storeMethodReturnValues=false
        StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, true, true, false);

        // Assert - Verify the StoringInvocationUnit instance was created successfully
        assertNotNull(invocationUnit, "StoringInvocationUnit should be created with storeMethodReturnValues=false");
    }

    /**
     * Tests that multiple StoringInvocationUnit instances can be created with the 4-parameter constructor.
     * Verifies that multiple instances are distinct objects.
     */
    @Test
    public void testFourParameterConstructorMultipleInstances() {
        // Arrange - Create ValueFactories
        ValueFactory valueFactory1 = new ParticularValueFactory();
        ValueFactory valueFactory2 = new BasicValueFactory();

        // Act - Create two StoringInvocationUnit instances
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(valueFactory1, true, true, true);
        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(valueFactory2, false, false, false);

        // Assert - Verify both instances were created and are different
        assertNotNull(invocationUnit1, "First StoringInvocationUnit should be created");
        assertNotNull(invocationUnit2, "Second StoringInvocationUnit should be created");
        assertNotSame(invocationUnit1, invocationUnit2, "StoringInvocationUnit instances should be different objects");
    }

    /**
     * Tests that the same ValueFactory can be used for multiple StoringInvocationUnit instances.
     * Verifies that multiple invocation units can share the same value factory.
     */
    @Test
    public void testFourParameterConstructorWithSharedValueFactory() {
        // Arrange - Create a single ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act - Create multiple StoringInvocationUnit instances with the same ValueFactory
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(valueFactory, true, true, true);
        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(valueFactory, false, false, false);

        // Assert - Verify both instances were created successfully
        assertNotNull(invocationUnit1, "First StoringInvocationUnit should be created");
        assertNotNull(invocationUnit2, "Second StoringInvocationUnit should be created");
        assertNotSame(invocationUnit1, invocationUnit2, "Different StoringInvocationUnit instances should be different objects");
    }

    /**
     * Tests that the 4-parameter constructor works with different ValueFactory implementations.
     * Verifies compatibility with various ValueFactory types.
     */
    @Test
    public void testFourParameterConstructorWithDifferentValueFactories() {
        // Act - Create StoringInvocationUnits with different ValueFactory implementations
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(new ParticularValueFactory(), true, false, true);
        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(new BasicValueFactory(), false, true, false);
        StoringInvocationUnit invocationUnit3 = new StoringInvocationUnit(new IdentifiedValueFactory(), true, true, false);

        // Assert - Verify all instances were created successfully
        assertNotNull(invocationUnit1, "StoringInvocationUnit should work with ParticularValueFactory");
        assertNotNull(invocationUnit2, "StoringInvocationUnit should work with BasicValueFactory");
        assertNotNull(invocationUnit3, "StoringInvocationUnit should work with IdentifiedValueFactory");
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
        assertDoesNotThrow(() -> new StoringInvocationUnit(valueFactory, true, true, true),
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
            boolean storeFieldValues = (i % 2 == 0);
            boolean storeMethodParameterValues = (i % 3 == 0);
            boolean storeMethodReturnValues = (i % 4 == 0);
            StoringInvocationUnit invocationUnit = new StoringInvocationUnit(
                    valueFactory, storeFieldValues, storeMethodParameterValues, storeMethodReturnValues);
            assertNotNull(invocationUnit, "StoringInvocationUnit should be created on iteration " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "StoringInvocationUnit should extend BasicInvocationUnit on iteration " + i);
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

        // Act - Create two StoringInvocationUnit instances with same parameters
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(valueFactory, true, true, true);
        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(valueFactory, true, true, true);

        // Assert - Verify both instances were created and are different objects
        assertNotNull(invocationUnit1, "First StoringInvocationUnit should be created");
        assertNotNull(invocationUnit2, "Second StoringInvocationUnit should be created");
        assertNotSame(invocationUnit1, invocationUnit2, "StoringInvocationUnit instances should be different objects even with same parameters");
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
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(valueFactory, true, true, true);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit1, "StoringInvocationUnit with (true, true, true) should extend BasicInvocationUnit");

        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(valueFactory, true, true, false);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit2, "StoringInvocationUnit with (true, true, false) should extend BasicInvocationUnit");

        StoringInvocationUnit invocationUnit3 = new StoringInvocationUnit(valueFactory, true, false, true);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit3, "StoringInvocationUnit with (true, false, true) should extend BasicInvocationUnit");

        StoringInvocationUnit invocationUnit4 = new StoringInvocationUnit(valueFactory, true, false, false);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit4, "StoringInvocationUnit with (true, false, false) should extend BasicInvocationUnit");

        StoringInvocationUnit invocationUnit5 = new StoringInvocationUnit(valueFactory, false, true, true);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit5, "StoringInvocationUnit with (false, true, true) should extend BasicInvocationUnit");

        StoringInvocationUnit invocationUnit6 = new StoringInvocationUnit(valueFactory, false, true, false);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit6, "StoringInvocationUnit with (false, true, false) should extend BasicInvocationUnit");

        StoringInvocationUnit invocationUnit7 = new StoringInvocationUnit(valueFactory, false, false, true);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit7, "StoringInvocationUnit with (false, false, true) should extend BasicInvocationUnit");

        StoringInvocationUnit invocationUnit8 = new StoringInvocationUnit(valueFactory, false, false, false);
        assertInstanceOf(BasicInvocationUnit.class, invocationUnit8, "StoringInvocationUnit with (false, false, false) should extend BasicInvocationUnit");
    }

    /**
     * Tests the 4-parameter constructor with various combinations of first two parameters.
     * Verifies that storeFieldValues and storeMethodParameterValues work together correctly.
     */
    @Test
    public void testFourParameterConstructorWithFieldAndParameterCombinations() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act & Assert - Test combinations of storeFieldValues and storeMethodParameterValues
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(valueFactory, true, true, false);
        assertNotNull(invocationUnit1, "Should work with storeFieldValues=true, storeMethodParameterValues=true");

        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(valueFactory, true, false, false);
        assertNotNull(invocationUnit2, "Should work with storeFieldValues=true, storeMethodParameterValues=false");

        StoringInvocationUnit invocationUnit3 = new StoringInvocationUnit(valueFactory, false, true, false);
        assertNotNull(invocationUnit3, "Should work with storeFieldValues=false, storeMethodParameterValues=true");

        StoringInvocationUnit invocationUnit4 = new StoringInvocationUnit(valueFactory, false, false, false);
        assertNotNull(invocationUnit4, "Should work with storeFieldValues=false, storeMethodParameterValues=false");
    }

    /**
     * Tests the 4-parameter constructor with various combinations of last two parameters.
     * Verifies that storeMethodParameterValues and storeMethodReturnValues work together correctly.
     */
    @Test
    public void testFourParameterConstructorWithParameterAndReturnCombinations() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act & Assert - Test combinations of storeMethodParameterValues and storeMethodReturnValues
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(valueFactory, false, true, true);
        assertNotNull(invocationUnit1, "Should work with storeMethodParameterValues=true, storeMethodReturnValues=true");

        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(valueFactory, false, true, false);
        assertNotNull(invocationUnit2, "Should work with storeMethodParameterValues=true, storeMethodReturnValues=false");

        StoringInvocationUnit invocationUnit3 = new StoringInvocationUnit(valueFactory, false, false, true);
        assertNotNull(invocationUnit3, "Should work with storeMethodParameterValues=false, storeMethodReturnValues=true");

        StoringInvocationUnit invocationUnit4 = new StoringInvocationUnit(valueFactory, false, false, false);
        assertNotNull(invocationUnit4, "Should work with storeMethodParameterValues=false, storeMethodReturnValues=false");
    }

    /**
     * Tests the 4-parameter constructor with various combinations of outer parameters.
     * Verifies that storeFieldValues and storeMethodReturnValues work together correctly.
     */
    @Test
    public void testFourParameterConstructorWithFieldAndReturnCombinations() {
        // Arrange - Create a ValueFactory
        ValueFactory valueFactory = new ParticularValueFactory();

        // Act & Assert - Test combinations of storeFieldValues and storeMethodReturnValues
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(valueFactory, true, false, true);
        assertNotNull(invocationUnit1, "Should work with storeFieldValues=true, storeMethodReturnValues=true");

        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(valueFactory, true, false, false);
        assertNotNull(invocationUnit2, "Should work with storeFieldValues=true, storeMethodReturnValues=false");

        StoringInvocationUnit invocationUnit3 = new StoringInvocationUnit(valueFactory, false, false, true);
        assertNotNull(invocationUnit3, "Should work with storeFieldValues=false, storeMethodReturnValues=true");

        StoringInvocationUnit invocationUnit4 = new StoringInvocationUnit(valueFactory, false, false, false);
        assertNotNull(invocationUnit4, "Should work with storeFieldValues=false, storeMethodReturnValues=false");
    }

    /**
     * Tests the 4-parameter constructor with all different ValueFactory types and boolean combinations.
     * Verifies that all ValueFactory implementations work with various boolean flag combinations.
     */
    @Test
    public void testFourParameterConstructorWithAllValueFactoryTypesAndBooleans() {
        // Act & Assert - Test with ParticularValueFactory
        StoringInvocationUnit invocationUnit1 = new StoringInvocationUnit(new ParticularValueFactory(), true, true, true);
        assertNotNull(invocationUnit1, "Should work with ParticularValueFactory and all true");

        StoringInvocationUnit invocationUnit2 = new StoringInvocationUnit(new ParticularValueFactory(), false, false, false);
        assertNotNull(invocationUnit2, "Should work with ParticularValueFactory and all false");

        // Test with BasicValueFactory
        StoringInvocationUnit invocationUnit3 = new StoringInvocationUnit(new BasicValueFactory(), true, false, true);
        assertNotNull(invocationUnit3, "Should work with BasicValueFactory");

        StoringInvocationUnit invocationUnit4 = new StoringInvocationUnit(new BasicValueFactory(), false, true, false);
        assertNotNull(invocationUnit4, "Should work with BasicValueFactory");

        // Test with IdentifiedValueFactory
        StoringInvocationUnit invocationUnit5 = new StoringInvocationUnit(new IdentifiedValueFactory(), true, true, false);
        assertNotNull(invocationUnit5, "Should work with IdentifiedValueFactory");

        StoringInvocationUnit invocationUnit6 = new StoringInvocationUnit(new IdentifiedValueFactory(), false, false, true);
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
        StoringInvocationUnit[] invocationUnits = new StoringInvocationUnit[instanceCount];

        // Act - Create instances with all 8 boolean combinations
        ValueFactory valueFactory = new ParticularValueFactory();
        invocationUnits[0] = new StoringInvocationUnit(valueFactory, true, true, true);
        invocationUnits[1] = new StoringInvocationUnit(valueFactory, true, true, false);
        invocationUnits[2] = new StoringInvocationUnit(valueFactory, true, false, true);
        invocationUnits[3] = new StoringInvocationUnit(valueFactory, true, false, false);
        invocationUnits[4] = new StoringInvocationUnit(valueFactory, false, true, true);
        invocationUnits[5] = new StoringInvocationUnit(valueFactory, false, true, false);
        invocationUnits[6] = new StoringInvocationUnit(valueFactory, false, false, true);
        invocationUnits[7] = new StoringInvocationUnit(valueFactory, false, false, false);

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
            StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, true, false, true);

            // Assert - Verify each instance is valid
            assertNotNull(invocationUnit, "StoringInvocationUnit should be created on attempt " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "StoringInvocationUnit should extend BasicInvocationUnit on attempt " + i);
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
            StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, false, true, false);

            // Assert - Verify each instance is valid
            assertNotNull(invocationUnit, "StoringInvocationUnit should be created on attempt " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "StoringInvocationUnit should extend BasicInvocationUnit on attempt " + i);
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
            StoringInvocationUnit invocationUnit = new StoringInvocationUnit(valueFactory, true, true, false);

            // Assert - Verify each instance is valid
            assertNotNull(invocationUnit, "StoringInvocationUnit should be created on attempt " + i);
            assertInstanceOf(BasicInvocationUnit.class, invocationUnit,
                    "StoringInvocationUnit should extend BasicInvocationUnit on attempt " + i);
        }
    }
}
