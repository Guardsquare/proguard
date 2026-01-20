package proguard.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ExtraDataEntryNameMap default constructor.
 */
public class ExtraDataEntryNameMapClaude_constructorTest {

    /**
     * Test that the default constructor successfully creates an ExtraDataEntryNameMap instance.
     */
    @Test
    public void testDefaultConstructorCreatesInstance() {
        ExtraDataEntryNameMap map = new ExtraDataEntryNameMap();

        assertNotNull(map, "ExtraDataEntryNameMap should be created successfully");
    }

    /**
     * Test that a newly constructed ExtraDataEntryNameMap is properly initialized
     * with an empty internal state.
     */
    @Test
    public void testDefaultConstructorInitializesEmptyState() {
        ExtraDataEntryNameMap map = new ExtraDataEntryNameMap();

        // Verify that the map is empty by checking various getter methods
        assertNotNull(map.getKeyDataEntryNames(), "Key data entry names should not be null");
        assertTrue(map.getKeyDataEntryNames().isEmpty(), "Key data entry names should be empty");

        assertNotNull(map.getAllExtraDataEntryNames(), "All extra data entry names should not be null");
        assertTrue(map.getAllExtraDataEntryNames().isEmpty(), "All extra data entry names should be empty");

        assertNull(map.getDefaultExtraDataEntryNames(), "Default extra data entry names should be null for empty map");
    }

    /**
     * Test that multiple instances created with the constructor are independent.
     */
    @Test
    public void testMultipleInstancesAreIndependent() {
        ExtraDataEntryNameMap map1 = new ExtraDataEntryNameMap();
        ExtraDataEntryNameMap map2 = new ExtraDataEntryNameMap();

        // Verify they are different instances
        assertNotSame(map1, map2, "Each constructor call should create a new instance");

        // Modify one and verify the other is not affected
        map1.addExtraDataEntry("test.txt");

        assertFalse(map2.getAllExtraDataEntryNames().contains("test.txt"),
                    "Changes to one instance should not affect another instance");
    }

    /**
     * Test that a newly constructed ExtraDataEntryNameMap can immediately be used
     * to add extra data entries.
     */
    @Test
    public void testConstructorAllowsImmediateUsage() {
        ExtraDataEntryNameMap map = new ExtraDataEntryNameMap();

        // Should be able to add data entries immediately after construction
        assertDoesNotThrow(() -> map.addExtraDataEntry("test.txt"),
                          "Should be able to add extra data entry immediately after construction");

        assertDoesNotThrow(() -> map.addExtraDataEntry("key.txt", "value.txt"),
                          "Should be able to add keyed extra data entry immediately after construction");
    }

    /**
     * Test that a newly constructed ExtraDataEntryNameMap has fully functional
     * getter methods that return proper collections.
     */
    @Test
    public void testConstructorInitializesFunctionalGetters() {
        ExtraDataEntryNameMap map = new ExtraDataEntryNameMap();

        // All getter methods should work without throwing exceptions
        assertDoesNotThrow(() -> map.getKeyDataEntryNames(),
                          "getKeyDataEntryNames should work on newly constructed instance");
        assertDoesNotThrow(() -> map.getAllExtraDataEntryNames(),
                          "getAllExtraDataEntryNames should work on newly constructed instance");
        assertDoesNotThrow(() -> map.getDefaultExtraDataEntryNames(),
                          "getDefaultExtraDataEntryNames should work on newly constructed instance");
        assertDoesNotThrow(() -> map.getExtraDataEntryNames("any.txt"),
                          "getExtraDataEntryNames should work on newly constructed instance");
    }

    /**
     * Test that a newly constructed ExtraDataEntryNameMap can be cleared
     * without issues.
     */
    @Test
    public void testConstructorAllowsClearOperation() {
        ExtraDataEntryNameMap map = new ExtraDataEntryNameMap();

        // Clear should work on a newly constructed (empty) instance
        assertDoesNotThrow(() -> map.clear(),
                          "clear should work on newly constructed instance");

        // After clear, it should still be empty
        assertTrue(map.getKeyDataEntryNames().isEmpty(),
                  "Map should still be empty after clearing an empty map");
    }

    /**
     * Test that the constructor properly initializes the internal state
     * to handle class-related operations.
     */
    @Test
    public void testConstructorSupportsClassOperations() {
        ExtraDataEntryNameMap map = new ExtraDataEntryNameMap();

        // Should be able to perform class-specific operations immediately
        assertDoesNotThrow(() -> map.addExtraClass("com/example/TestClass"),
                          "Should be able to add extra class immediately after construction");

        assertDoesNotThrow(() -> map.addExtraClassToClass("com/example/Key", "com/example/Value"),
                          "Should be able to add class-to-class mapping immediately after construction");

        assertDoesNotThrow(() -> map.clearExtraClasses(),
                          "Should be able to clear extra classes immediately after construction");
    }
}
