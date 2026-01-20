package proguard.obfuscate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link NumericNameFactory} constructor.
 * Tests the default constructor NumericNameFactory().
 */
public class NumericNameFactoryClaude_constructorTest {

    /**
     * Tests that the default constructor creates a valid NumericNameFactory instance.
     * Verifies that the instance is not null.
     */
    @Test
    public void testConstructorCreatesValidInstance() {
        // Act - Create a NumericNameFactory using the default constructor
        NumericNameFactory nameFactory = new NumericNameFactory();

        // Assert - Verify the instance was created successfully
        assertNotNull(nameFactory, "NumericNameFactory should be instantiated successfully");
    }

    /**
     * Tests that the constructor creates an instance that implements NameFactory.
     * Verifies that NumericNameFactory can be used as a NameFactory.
     */
    @Test
    public void testConstructorCreatesInstanceOfNameFactory() {
        // Act - Create a NumericNameFactory
        NumericNameFactory nameFactory = new NumericNameFactory();

        // Assert - Verify the instance implements NameFactory
        assertInstanceOf(NameFactory.class, nameFactory,
                "NumericNameFactory should implement NameFactory interface");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Verifies that the constructor can be called multiple times.
     */
    @Test
    public void testConstructorCreatesMultipleIndependentInstances() {
        // Act - Create multiple NumericNameFactory instances
        NumericNameFactory nameFactory1 = new NumericNameFactory();
        NumericNameFactory nameFactory2 = new NumericNameFactory();

        // Assert - Verify both instances are created and are distinct
        assertNotNull(nameFactory1, "First NumericNameFactory instance should be created");
        assertNotNull(nameFactory2, "Second NumericNameFactory instance should be created");
        assertNotSame(nameFactory1, nameFactory2,
                "Multiple instances should be distinct objects");
    }

    /**
     * Tests that the constructor initializes the factory to generate names starting from "1".
     * Verifies that the first name generated after construction is "1".
     */
    @Test
    public void testConstructorInitializesFactoryToStartAtOne() {
        // Act - Create a NumericNameFactory and get the first name
        NumericNameFactory nameFactory = new NumericNameFactory();
        String firstName = nameFactory.nextName();

        // Assert - Verify the first name is "1"
        assertEquals("1", firstName,
                "NumericNameFactory should start generating names from '1'");
    }

    /**
     * Tests that the constructor initializes the factory correctly for sequential name generation.
     * Verifies that consecutive names follow the expected numeric sequence.
     */
    @Test
    public void testConstructorInitializesForSequentialGeneration() {
        // Act - Create a NumericNameFactory and generate multiple names
        NumericNameFactory nameFactory = new NumericNameFactory();
        String name1 = nameFactory.nextName();
        String name2 = nameFactory.nextName();
        String name3 = nameFactory.nextName();

        // Assert - Verify the sequence is correct
        assertEquals("1", name1, "First name should be '1'");
        assertEquals("2", name2, "Second name should be '2'");
        assertEquals("3", name3, "Third name should be '3'");
    }

    /**
     * Tests that multiple instances created independently generate the same initial sequence.
     * Verifies that each instance starts from the same initial state.
     */
    @Test
    public void testMultipleInstancesGenerateSameInitialSequence() {
        // Act - Create two separate NumericNameFactory instances
        NumericNameFactory nameFactory1 = new NumericNameFactory();
        NumericNameFactory nameFactory2 = new NumericNameFactory();

        // Get first names from each
        String firstName1 = nameFactory1.nextName();
        String firstName2 = nameFactory2.nextName();

        // Assert - Both should start at "1"
        assertEquals("1", firstName1, "First instance should start at '1'");
        assertEquals("1", firstName2, "Second instance should start at '1'");
        assertEquals(firstName1, firstName2,
                "Both instances should generate the same first name");
    }

    /**
     * Tests that instances created independently maintain separate state.
     * Verifies that advancing one instance does not affect another.
     */
    @Test
    public void testMultipleInstancesMaintainIndependentState() {
        // Act - Create two separate NumericNameFactory instances
        NumericNameFactory nameFactory1 = new NumericNameFactory();
        NumericNameFactory nameFactory2 = new NumericNameFactory();

        // Advance the first factory multiple times
        nameFactory1.nextName(); // "1"
        nameFactory1.nextName(); // "2"
        nameFactory1.nextName(); // "3"

        // Get the next name from the second factory
        String nameFromFactory2 = nameFactory2.nextName();

        // Assert - The second factory should still be at "1"
        assertEquals("1", nameFromFactory2,
                "Second instance should be unaffected by first instance");
    }

    /**
     * Tests that the constructor creates an instance that can be reset.
     * Verifies that reset() method works correctly on a newly constructed instance.
     */
    @Test
    public void testConstructorCreatesResettableInstance() {
        // Act - Create a NumericNameFactory, advance it, then reset it
        NumericNameFactory nameFactory = new NumericNameFactory();
        nameFactory.nextName(); // "1"
        nameFactory.nextName(); // "2"
        nameFactory.reset();
        String nameAfterReset = nameFactory.nextName();

        // Assert - After reset, should start from "1" again
        assertEquals("1", nameAfterReset,
                "After reset, factory should start generating from '1' again");
    }

    /**
     * Tests that a newly constructed instance behaves the same as a reset instance.
     * Verifies that the initial state matches the reset state.
     */
    @Test
    public void testConstructorStateMatchesResetState() {
        // Arrange - Create a factory and advance it
        NumericNameFactory nameFactory = new NumericNameFactory();
        nameFactory.nextName(); // "1"
        nameFactory.nextName(); // "2"
        nameFactory.reset();

        // Create a new factory
        NumericNameFactory newFactory = new NumericNameFactory();

        // Act - Get first name from both
        String nameFromReset = nameFactory.nextName();
        String nameFromNew = newFactory.nextName();

        // Assert - Both should produce the same first name
        assertEquals(nameFromNew, nameFromReset,
                "A newly constructed factory should behave the same as a reset factory");
    }

    /**
     * Tests that the constructor creates an instance that can generate many sequential names.
     * Verifies that the factory can handle large sequences.
     */
    @Test
    public void testConstructorCreatesFactoryCapableOfGeneratingManyNames() {
        // Act - Create a NumericNameFactory and generate many names
        NumericNameFactory nameFactory = new NumericNameFactory();

        // Generate and verify the first few names
        assertEquals("1", nameFactory.nextName());
        assertEquals("2", nameFactory.nextName());

        // Generate many more names
        for (int i = 3; i <= 100; i++) {
            nameFactory.nextName();
        }

        // Verify the 101st name
        String name101 = nameFactory.nextName();

        // Assert - Should be "101"
        assertEquals("101", name101,
                "Factory should correctly generate the 101st name");
    }
}
