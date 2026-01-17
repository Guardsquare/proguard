package proguard.classfile.pass;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.AppView;
import proguard.classfile.ClassPool;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link PrimitiveArrayConstantIntroducer}.
 * Tests the constructor, execute method, and inherited getName method.
 */
public class PrimitiveArrayConstantIntroducerClaudeTest {

    private PrimitiveArrayConstantIntroducer introducer;

    @BeforeEach
    public void setUp() {
        introducer = new PrimitiveArrayConstantIntroducer();
    }

    // ==================== Constructor Tests ====================

    /**
     * Tests that the no-argument constructor creates a valid instance.
     * Verifies that the instance is not null and is of the correct type.
     */
    @Test
    public void testConstructor_createsValidInstance() {
        // Act
        PrimitiveArrayConstantIntroducer instance = new PrimitiveArrayConstantIntroducer();

        // Assert
        assertNotNull(instance, "Constructor should create a non-null instance");
        assertTrue(instance instanceof PrimitiveArrayConstantIntroducer,
                "Instance should be of type PrimitiveArrayConstantIntroducer");
    }

    /**
     * Tests that multiple constructor calls create separate instances.
     * Verifies that each instantiation creates a new object.
     */
    @Test
    public void testConstructor_createsDistinctInstances() {
        // Act
        PrimitiveArrayConstantIntroducer instance1 = new PrimitiveArrayConstantIntroducer();
        PrimitiveArrayConstantIntroducer instance2 = new PrimitiveArrayConstantIntroducer();

        // Assert
        assertNotSame(instance1, instance2,
                "Each constructor call should create a new instance");
    }

    // ==================== execute() Tests ====================

    /**
     * Tests execute method with a valid non-null AppView.
     * Verifies that the method executes without throwing exceptions.
     * This test does not mock, relying on the actual behavior of ArrayInitializationReplacer
     * which should handle an empty ClassPool gracefully.
     */
    @Test
    public void testExecute_withValidAppView_executesWithoutException() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        AppView appView = new AppView(programClassPool, libraryClassPool);

        // Act & Assert - should not throw any exception
        assertDoesNotThrow(() -> introducer.execute(appView),
                "Execute should not throw exception with valid AppView");
    }

    /**
     * Tests execute method with an AppView containing an empty program class pool.
     * Verifies that the method handles empty pools gracefully.
     */
    @Test
    public void testExecute_withEmptyClassPool_executesWithoutException() {
        // Arrange
        AppView appView = new AppView(); // Uses default constructor with empty pools

        // Act & Assert
        assertDoesNotThrow(() -> introducer.execute(appView),
                "Execute should handle empty class pools without exception");
    }

    /**
     * Tests execute method with a null AppView.
     * Verifies that the method throws a NullPointerException when AppView is null.
     */
    @Test
    public void testExecute_withNullAppView_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> introducer.execute(null),
                "Execute should throw NullPointerException when AppView is null");
    }

    /**
     * Tests that execute can be called multiple times on the same instance.
     * Verifies that the introducer is reusable and maintains no problematic state.
     */
    @Test
    public void testExecute_calledMultipleTimes_executesWithoutException() {
        // Arrange
        AppView appView = new AppView();

        // Act & Assert - call execute multiple times
        assertDoesNotThrow(() -> {
            introducer.execute(appView);
            introducer.execute(appView);
            introducer.execute(appView);
        }, "Execute should be callable multiple times without exception");
    }

    /**
     * Tests execute with different AppView instances.
     * Verifies that the introducer can process different AppViews.
     */
    @Test
    public void testExecute_withDifferentAppViews_executesWithoutException() {
        // Arrange
        AppView appView1 = new AppView();
        AppView appView2 = new AppView(new ClassPool(), new ClassPool());

        // Act & Assert
        assertDoesNotThrow(() -> {
            introducer.execute(appView1);
            introducer.execute(appView2);
        }, "Execute should handle different AppView instances");
    }

    // ==================== getName() Tests ====================

    /**
     * Tests that getName returns the fully qualified class name.
     * This tests the default implementation from the Pass interface.
     */
    @Test
    public void testGetName_returnsFullyQualifiedClassName() {
        // Act
        String name = introducer.getName();

        // Assert
        assertNotNull(name, "getName should not return null");
        assertEquals("proguard.classfile.pass.PrimitiveArrayConstantIntroducer", name,
                "getName should return the fully qualified class name");
    }

    /**
     * Tests that getName is consistent across multiple calls.
     * Verifies that the name doesn't change between invocations.
     */
    @Test
    public void testGetName_isConsistent() {
        // Act
        String name1 = introducer.getName();
        String name2 = introducer.getName();

        // Assert
        assertEquals(name1, name2, "getName should return the same value consistently");
    }

    /**
     * Tests that different instances return the same name.
     * Verifies that getName returns the class name, not instance-specific information.
     */
    @Test
    public void testGetName_sameForDifferentInstances() {
        // Arrange
        PrimitiveArrayConstantIntroducer instance1 = new PrimitiveArrayConstantIntroducer();
        PrimitiveArrayConstantIntroducer instance2 = new PrimitiveArrayConstantIntroducer();

        // Act
        String name1 = instance1.getName();
        String name2 = instance2.getName();

        // Assert
        assertEquals(name1, name2,
                "All instances should return the same name");
    }
}
