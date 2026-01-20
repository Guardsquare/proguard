package proguard.optimize;

import org.junit.jupiter.api.Test;
import proguard.pass.Pass;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link LineNumberTrimmer#LineNumberTrimmer()}.
 *
 * The LineNumberTrimmer class has an implicit default no-argument constructor.
 * This constructor performs no operations beyond calling the superclass (Object) constructor.
 * The class has no instance fields to initialize and no side effects.
 *
 * These tests verify that the constructor:
 * 1. Successfully creates an instance without throwing exceptions
 * 2. Returns a non-null object
 * 3. Creates an object of the correct type
 * 4. Creates an object that implements the Pass interface
 */
public class LineNumberTrimmerClaude_constructorTest {

    /**
     * Tests that the constructor successfully creates a new instance.
     * This is the primary behavior of the default constructor.
     */
    @Test
    public void testConstructor_createsInstance() {
        // Act
        LineNumberTrimmer trimmer = new LineNumberTrimmer();

        // Assert
        assertNotNull(trimmer, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor creates an instance of the correct type.
     * Verifies the object is an instance of LineNumberTrimmer.
     */
    @Test
    public void testConstructor_createsCorrectType() {
        // Act
        LineNumberTrimmer trimmer = new LineNumberTrimmer();

        // Assert
        assertTrue(trimmer instanceof LineNumberTrimmer,
                "Constructor should create an instance of LineNumberTrimmer");
    }

    /**
     * Tests that the constructed object implements the Pass interface.
     * LineNumberTrimmer implements Pass, so instances should be assignable to Pass.
     */
    @Test
    public void testConstructor_implementsPassInterface() {
        // Act
        LineNumberTrimmer trimmer = new LineNumberTrimmer();

        // Assert
        assertTrue(trimmer instanceof Pass,
                "LineNumberTrimmer instance should implement Pass interface");
    }

    /**
     * Tests that the constructor can be called multiple times to create independent instances.
     * Each call should create a distinct object.
     */
    @Test
    public void testConstructor_createsIndependentInstances() {
        // Act
        LineNumberTrimmer trimmer1 = new LineNumberTrimmer();
        LineNumberTrimmer trimmer2 = new LineNumberTrimmer();

        // Assert
        assertNotNull(trimmer1, "First instance should be non-null");
        assertNotNull(trimmer2, "Second instance should be non-null");
        assertNotSame(trimmer1, trimmer2,
                "Each constructor call should create a distinct instance");
    }

    /**
     * Tests that the constructor does not throw any exceptions.
     * The default constructor should complete successfully.
     */
    @Test
    public void testConstructor_doesNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> new LineNumberTrimmer(),
                "Constructor should not throw any exceptions");
    }

    /**
     * Tests that multiple consecutive instantiations work correctly.
     * Verifies the constructor can be called repeatedly without issues.
     */
    @Test
    public void testConstructor_multipleInstantiations() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 100; i++) {
                LineNumberTrimmer trimmer = new LineNumberTrimmer();
                assertNotNull(trimmer, "Instance " + i + " should be non-null");
            }
        }, "Should be able to create multiple instances without error");
    }

    /**
     * Tests that the constructed object can be assigned to a Pass variable.
     * This verifies polymorphic usage is possible.
     */
    @Test
    public void testConstructor_assignableToPassInterface() {
        // Act
        Pass pass = new LineNumberTrimmer();

        // Assert
        assertNotNull(pass, "Instance assigned to Pass should be non-null");
        assertTrue(pass instanceof LineNumberTrimmer,
                "Pass variable should hold a LineNumberTrimmer instance");
    }

    /**
     * Tests that the object's class is correctly reported.
     * Verifies getClass() returns the expected class.
     */
    @Test
    public void testConstructor_correctClassType() {
        // Act
        LineNumberTrimmer trimmer = new LineNumberTrimmer();

        // Assert
        assertEquals(LineNumberTrimmer.class, trimmer.getClass(),
                "Object should report its class as LineNumberTrimmer");
    }

    /**
     * Tests that the constructed instance inherits from Object.
     * All Java objects inherit from Object, including LineNumberTrimmer.
     */
    @Test
    public void testConstructor_inheritsFromObject() {
        // Act
        LineNumberTrimmer trimmer = new LineNumberTrimmer();

        // Assert
        assertTrue(trimmer instanceof Object,
                "LineNumberTrimmer instance should be an instance of Object");
    }

    /**
     * Tests that the getName() method (inherited from Pass interface) works after construction.
     * This verifies the object is properly initialized to use interface default methods.
     */
    @Test
    public void testConstructor_defaultMethodsWorkAfterConstruction() {
        // Act
        LineNumberTrimmer trimmer = new LineNumberTrimmer();
        String name = trimmer.getName();

        // Assert
        assertNotNull(name, "getName() should return a non-null value");
        assertEquals("proguard.optimize.LineNumberTrimmer", name,
                "getName() should return the fully qualified class name");
    }

    /**
     * Tests that toString() can be called on the constructed instance.
     * Verifies the object is properly constructed with working Object methods.
     */
    @Test
    public void testConstructor_toStringWorks() {
        // Act
        LineNumberTrimmer trimmer = new LineNumberTrimmer();
        String toString = trimmer.toString();

        // Assert
        assertNotNull(toString, "toString() should return a non-null value");
        assertTrue(toString.contains("LineNumberTrimmer"),
                "toString() should contain the class name");
    }

    /**
     * Tests that hashCode() can be called on the constructed instance.
     * Verifies the object is properly constructed with working Object methods.
     */
    @Test
    public void testConstructor_hashCodeWorks() {
        // Act
        LineNumberTrimmer trimmer = new LineNumberTrimmer();

        // Assert
        assertDoesNotThrow(() -> trimmer.hashCode(),
                "hashCode() should work on constructed instance");
    }

    /**
     * Tests that equals() can be called on the constructed instance.
     * Verifies the object is properly constructed with working Object methods.
     */
    @Test
    public void testConstructor_equalsWorks() {
        // Act
        LineNumberTrimmer trimmer1 = new LineNumberTrimmer();
        LineNumberTrimmer trimmer2 = new LineNumberTrimmer();

        // Assert
        assertDoesNotThrow(() -> trimmer1.equals(trimmer2),
                "equals() should work on constructed instances");
        assertTrue(trimmer1.equals(trimmer1),
                "Instance should equal itself");
    }
}
