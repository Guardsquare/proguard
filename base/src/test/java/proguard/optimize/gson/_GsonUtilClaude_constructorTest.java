package proguard.optimize.gson;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link _GsonUtil} constructor.
 * Tests the default constructor: <init>.()V
 *
 * The _GsonUtil class is a utility class with only static methods. It has an implicit
 * default constructor that is never actually used in the codebase, but we test it
 * for completeness and to achieve full code coverage.
 */
public class _GsonUtilClaude_constructorTest {

    // =========================================================================
    // Tests for constructor: <init>.()V
    // =========================================================================

    /**
     * Tests that the default constructor successfully creates an instance.
     * While _GsonUtil is a utility class not meant to be instantiated,
     * the constructor should still work without throwing exceptions.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        _GsonUtil instance = new _GsonUtil();

        // Assert
        assertNotNull(instance, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new _GsonUtil(),
                "Constructor should not throw any exception");
    }

    /**
     * Tests that multiple instances can be created independently.
     * Each constructor call should create a distinct object.
     */
    @Test
    public void testConstructor_multipleInstances_createDistinctObjects() {
        // Act
        _GsonUtil instance1 = new _GsonUtil();
        _GsonUtil instance2 = new _GsonUtil();
        _GsonUtil instance3 = new _GsonUtil();

        // Assert
        assertNotNull(instance1, "First instance should be created");
        assertNotNull(instance2, "Second instance should be created");
        assertNotNull(instance3, "Third instance should be created");
        assertNotSame(instance1, instance2, "Instances should be distinct objects");
        assertNotSame(instance2, instance3, "Instances should be distinct objects");
        assertNotSame(instance1, instance3, "Instances should be distinct objects");
    }

    /**
     * Tests that the created instance is of the correct type.
     */
    @Test
    public void testConstructor_createsCorrectType() {
        // Act
        _GsonUtil instance = new _GsonUtil();

        // Assert
        assertTrue(instance instanceof _GsonUtil,
                "Created instance should be of type _GsonUtil");
    }

    /**
     * Tests that consecutive constructor calls work correctly.
     */
    @Test
    public void testConstructor_consecutiveCalls_allSucceed() {
        // Act & Assert
        for (int i = 0; i < 5; i++) {
            _GsonUtil instance = new _GsonUtil();
            assertNotNull(instance, "Instance " + i + " should be created successfully");
        }
    }

    /**
     * Tests that the instance created by the constructor is a proper Java object
     * by verifying it has the basic Object methods available.
     */
    @Test
    public void testConstructor_instanceHasObjectMethods() {
        // Act
        _GsonUtil instance = new _GsonUtil();

        // Assert
        assertNotNull(instance.toString(), "Instance should have toString() method");
        assertNotNull(instance.getClass(), "Instance should have getClass() method");
        assertEquals(instance, instance, "Instance should equal itself");
        assertNotEquals(instance.hashCode(), 0, "Instance should have a hashCode");
    }

    /**
     * Tests that different instances have different identity hash codes,
     * confirming they are distinct objects in memory.
     */
    @Test
    public void testConstructor_distinctInstances_haveDifferentIdentities() {
        // Act
        _GsonUtil instance1 = new _GsonUtil();
        _GsonUtil instance2 = new _GsonUtil();

        // Assert
        assertNotEquals(
                System.identityHashCode(instance1),
                System.identityHashCode(instance2),
                "Different instances should have different identity hash codes"
        );
    }

    /**
     * Tests that the class is final and cannot be subclassed.
     * This is a design characteristic of utility classes.
     */
    @Test
    public void testConstructor_classIsFinal() {
        // Act
        _GsonUtil instance = new _GsonUtil();
        Class<?> clazz = instance.getClass();

        // Assert
        assertTrue(java.lang.reflect.Modifier.isFinal(clazz.getModifiers()),
                "_GsonUtil should be a final class");
    }

    /**
     * Tests that the class is public and accessible.
     */
    @Test
    public void testConstructor_classIsPublic() {
        // Act
        _GsonUtil instance = new _GsonUtil();
        Class<?> clazz = instance.getClass();

        // Assert
        assertTrue(java.lang.reflect.Modifier.isPublic(clazz.getModifiers()),
                "_GsonUtil should be a public class");
    }
}
