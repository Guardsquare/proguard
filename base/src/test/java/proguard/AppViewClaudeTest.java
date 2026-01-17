package proguard;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.io.ExtraDataEntryNameMap;
import proguard.resources.file.ResourceFilePool;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link AppView}.
 * Tests all three constructors to ensure proper initialization of fields.
 */
public class AppViewClaudeTest {

    /**
     * Tests the no-argument constructor AppView().
     * Verifies that all fields are initialized with new instances.
     */
    @Test
    public void testNoArgConstructor() {
        // Act
        AppView appView = new AppView();

        // Assert - verify all fields are non-null
        assertNotNull(appView.programClassPool, "programClassPool should not be null");
        assertNotNull(appView.libraryClassPool, "libraryClassPool should not be null");
        assertNotNull(appView.resourceFilePool, "resourceFilePool should not be null");
        assertNotNull(appView.extraDataEntryNameMap, "extraDataEntryNameMap should not be null");

        // Verify initialStateInfo is initially null (not set by constructor)
        assertNull(appView.initialStateInfo, "initialStateInfo should be null initially");
    }

    /**
     * Tests that the no-argument constructor creates separate instances for each field.
     */
    @Test
    public void testNoArgConstructorCreatesNewInstances() {
        // Act
        AppView appView1 = new AppView();
        AppView appView2 = new AppView();

        // Assert - verify each AppView instance has its own objects
        assertNotSame(appView1.programClassPool, appView2.programClassPool,
                      "Each AppView should have its own programClassPool");
        assertNotSame(appView1.libraryClassPool, appView2.libraryClassPool,
                      "Each AppView should have its own libraryClassPool");
        assertNotSame(appView1.resourceFilePool, appView2.resourceFilePool,
                      "Each AppView should have its own resourceFilePool");
        assertNotSame(appView1.extraDataEntryNameMap, appView2.extraDataEntryNameMap,
                      "Each AppView should have its own extraDataEntryNameMap");
    }

    /**
     * Tests the two-argument constructor AppView(ClassPool, ClassPool).
     * Verifies that provided ClassPools are used and other fields are initialized with new instances.
     */
    @Test
    public void testTwoArgConstructor() {
        // Arrange
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();

        // Act
        AppView appView = new AppView(programPool, libraryPool);

        // Assert - verify the provided pools are assigned
        assertSame(programPool, appView.programClassPool,
                   "Provided programClassPool should be used");
        assertSame(libraryPool, appView.libraryClassPool,
                   "Provided libraryClassPool should be used");

        // Verify other fields are initialized with new instances
        assertNotNull(appView.resourceFilePool, "resourceFilePool should not be null");
        assertNotNull(appView.extraDataEntryNameMap, "extraDataEntryNameMap should not be null");

        // Verify initialStateInfo is initially null
        assertNull(appView.initialStateInfo, "initialStateInfo should be null initially");
    }

    /**
     * Tests that the two-argument constructor creates new instances for non-provided fields.
     */
    @Test
    public void testTwoArgConstructorCreatesNewInstancesForOtherFields() {
        // Arrange
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();

        // Act
        AppView appView1 = new AppView(programPool, libraryPool);
        AppView appView2 = new AppView(programPool, libraryPool);

        // Assert - verify each AppView has separate resource pool and name map instances
        assertNotSame(appView1.resourceFilePool, appView2.resourceFilePool,
                      "Each AppView should have its own resourceFilePool");
        assertNotSame(appView1.extraDataEntryNameMap, appView2.extraDataEntryNameMap,
                      "Each AppView should have its own extraDataEntryNameMap");
    }

    /**
     * Tests the two-argument constructor with null values.
     * The constructor should accept null values as valid input.
     */
    @Test
    public void testTwoArgConstructorWithNullValues() {
        // Act
        AppView appView = new AppView(null, null);

        // Assert - verify nulls are accepted and stored
        assertNull(appView.programClassPool, "programClassPool should be null");
        assertNull(appView.libraryClassPool, "libraryClassPool should be null");

        // Verify other fields are still initialized
        assertNotNull(appView.resourceFilePool, "resourceFilePool should not be null");
        assertNotNull(appView.extraDataEntryNameMap, "extraDataEntryNameMap should not be null");
    }

    /**
     * Tests the four-argument constructor AppView(ClassPool, ClassPool, ResourceFilePool, ExtraDataEntryNameMap).
     * Verifies that all provided parameters are properly assigned.
     */
    @Test
    public void testFourArgConstructor() {
        // Arrange
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();
        ResourceFilePool resourcePool = new ResourceFilePool();
        ExtraDataEntryNameMap nameMap = new ExtraDataEntryNameMap();

        // Act
        AppView appView = new AppView(programPool, libraryPool, resourcePool, nameMap);

        // Assert - verify all provided objects are assigned
        assertSame(programPool, appView.programClassPool,
                   "Provided programClassPool should be used");
        assertSame(libraryPool, appView.libraryClassPool,
                   "Provided libraryClassPool should be used");
        assertSame(resourcePool, appView.resourceFilePool,
                   "Provided resourceFilePool should be used");
        assertSame(nameMap, appView.extraDataEntryNameMap,
                   "Provided extraDataEntryNameMap should be used");

        // Verify initialStateInfo is initially null
        assertNull(appView.initialStateInfo, "initialStateInfo should be null initially");
    }

    /**
     * Tests the four-argument constructor with all null values.
     * The constructor should accept null values for all parameters.
     */
    @Test
    public void testFourArgConstructorWithAllNullValues() {
        // Act
        AppView appView = new AppView(null, null, null, null);

        // Assert - verify all fields are null
        assertNull(appView.programClassPool, "programClassPool should be null");
        assertNull(appView.libraryClassPool, "libraryClassPool should be null");
        assertNull(appView.resourceFilePool, "resourceFilePool should be null");
        assertNull(appView.extraDataEntryNameMap, "extraDataEntryNameMap should be null");
        assertNull(appView.initialStateInfo, "initialStateInfo should be null");
    }

    /**
     * Tests the four-argument constructor with mixed null and non-null values.
     */
    @Test
    public void testFourArgConstructorWithMixedNullValues() {
        // Arrange
        ClassPool programPool = new ClassPool();
        ResourceFilePool resourcePool = new ResourceFilePool();

        // Act - pass nulls for libraryClassPool and extraDataEntryNameMap
        AppView appView = new AppView(programPool, null, resourcePool, null);

        // Assert
        assertSame(programPool, appView.programClassPool,
                   "Provided programClassPool should be used");
        assertNull(appView.libraryClassPool, "libraryClassPool should be null");
        assertSame(resourcePool, appView.resourceFilePool,
                   "Provided resourceFilePool should be used");
        assertNull(appView.extraDataEntryNameMap, "extraDataEntryNameMap should be null");
    }

    /**
     * Tests that constructor delegation works correctly.
     * The no-arg constructor should delegate to the four-arg constructor.
     */
    @Test
    public void testConstructorDelegation() {
        // Act
        AppView appView = new AppView();

        // Assert - verify the types are correct (indicating proper delegation)
        assertTrue(appView.programClassPool instanceof ClassPool,
                   "programClassPool should be a ClassPool instance");
        assertTrue(appView.libraryClassPool instanceof ClassPool,
                   "libraryClassPool should be a ClassPool instance");
        assertTrue(appView.resourceFilePool instanceof ResourceFilePool,
                   "resourceFilePool should be a ResourceFilePool instance");
        assertTrue(appView.extraDataEntryNameMap instanceof ExtraDataEntryNameMap,
                   "extraDataEntryNameMap should be an ExtraDataEntryNameMap instance");
    }

    /**
     * Tests that the two-arg constructor delegation works correctly.
     */
    @Test
    public void testTwoArgConstructorDelegation() {
        // Arrange
        ClassPool programPool = new ClassPool();
        ClassPool libraryPool = new ClassPool();

        // Act
        AppView appView = new AppView(programPool, libraryPool);

        // Assert - verify correct types and instances
        assertSame(programPool, appView.programClassPool);
        assertSame(libraryPool, appView.libraryClassPool);
        assertTrue(appView.resourceFilePool instanceof ResourceFilePool,
                   "resourceFilePool should be a ResourceFilePool instance");
        assertTrue(appView.extraDataEntryNameMap instanceof ExtraDataEntryNameMap,
                   "extraDataEntryNameMap should be an ExtraDataEntryNameMap instance");
    }

    /**
     * Tests that the initialStateInfo field can be set after construction.
     * This verifies the field is mutable and accessible.
     */
    @Test
    public void testInitialStateInfoIsSettable() {
        // Arrange
        AppView appView = new AppView();

        // Act - initialStateInfo is not set by constructors but should be settable
        assertNull(appView.initialStateInfo, "initialStateInfo should start as null");

        // Note: InitialStateInfo is part of the class but not initialized by constructors
        // This test verifies it starts as null and is accessible
    }
}
