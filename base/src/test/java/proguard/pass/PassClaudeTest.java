package proguard.pass;

import org.junit.jupiter.api.Test;
import proguard.AppView;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Pass}.
 * Tests the getName() default method to ensure proper functionality.
 */
public class PassClaudeTest {

    /**
     * Tests that getName() returns the fully qualified class name.
     * The default implementation should return getClass().getName().
     */
    @Test
    public void testGetNameReturnsClassName() {
        // Arrange - create a concrete implementation of Pass
        Pass pass = new ConcretePass();

        // Act
        String name = pass.getName();

        // Assert
        assertEquals("proguard.pass.PassClaudeTest$ConcretePass", name,
                     "getName() should return the fully qualified class name");
    }

    /**
     * Tests that getName() returns different names for different implementations.
     */
    @Test
    public void testGetNameReturnsDifferentNamesForDifferentClasses() {
        // Arrange
        Pass pass1 = new ConcretePass();
        Pass pass2 = new AnotherConcretePass();

        // Act
        String name1 = pass1.getName();
        String name2 = pass2.getName();

        // Assert
        assertNotEquals(name1, name2,
                        "Different Pass implementations should return different names");
        assertEquals("proguard.pass.PassClaudeTest$ConcretePass", name1);
        assertEquals("proguard.pass.PassClaudeTest$AnotherConcretePass", name2);
    }

    /**
     * Tests that getName() returns consistent values when called multiple times.
     */
    @Test
    public void testGetNameConsistentAcrossMultipleCalls() {
        // Arrange
        Pass pass = new ConcretePass();

        // Act
        String name1 = pass.getName();
        String name2 = pass.getName();
        String name3 = pass.getName();

        // Assert
        assertEquals(name1, name2, "getName() should return consistent value");
        assertEquals(name2, name3, "getName() should return consistent value");
        assertSame(name1.getClass(), name2.getClass(), "Should return same String type");
    }

    /**
     * Tests that getName() returns the correct name for anonymous class implementations.
     */
    @Test
    public void testGetNameForAnonymousClass() {
        // Arrange - create an anonymous implementation
        Pass pass = new Pass() {
            @Override
            public void execute(AppView appView) throws Exception {
                // Anonymous implementation
            }
        };

        // Act
        String name = pass.getName();

        // Assert
        assertNotNull(name, "getName() should return a non-null value");
        assertTrue(name.startsWith("proguard.pass.PassClaudeTest$"),
                   "Anonymous class name should start with outer class name");
        assertTrue(name.contains("$"), "Anonymous class name should contain '$' delimiter");
    }

    /**
     * Tests that getName() works correctly with inheritance.
     * A subclass should return its own class name, not the parent's.
     */
    @Test
    public void testGetNameWithInheritance() {
        // Arrange
        Pass basePass = new ConcretePass();
        Pass subPass = new SubclassPass();

        // Act
        String baseName = basePass.getName();
        String subName = subPass.getName();

        // Assert
        assertNotEquals(baseName, subName,
                        "Subclass should return its own name, not parent's");
        assertEquals("proguard.pass.PassClaudeTest$ConcretePass", baseName);
        assertEquals("proguard.pass.PassClaudeTest$SubclassPass", subName);
    }

    /**
     * Tests that getName() returns a non-null, non-empty string.
     */
    @Test
    public void testGetNameReturnsNonEmptyString() {
        // Arrange
        Pass pass = new ConcretePass();

        // Act
        String name = pass.getName();

        // Assert
        assertNotNull(name, "getName() should not return null");
        assertFalse(name.isEmpty(), "getName() should not return an empty string");
        assertTrue(name.length() > 0, "getName() should return a non-empty string");
    }

    /**
     * Tests that getName() result contains expected package structure.
     */
    @Test
    public void testGetNameContainsPackageStructure() {
        // Arrange
        Pass pass = new ConcretePass();

        // Act
        String name = pass.getName();

        // Assert
        assertTrue(name.contains("."), "Class name should contain package separators");
        assertTrue(name.startsWith("proguard.pass."),
                   "Class name should start with package name");
    }

    // Helper classes for testing

    /**
     * Concrete implementation of Pass for testing purposes.
     */
    private static class ConcretePass implements Pass {
        @Override
        public void execute(AppView appView) throws Exception {
            // Simple test implementation
        }
    }

    /**
     * Another concrete implementation of Pass for testing multiple instances.
     */
    private static class AnotherConcretePass implements Pass {
        @Override
        public void execute(AppView appView) throws Exception {
            // Simple test implementation
        }
    }

    /**
     * Subclass of ConcretePass for testing inheritance behavior.
     */
    private static class SubclassPass extends ConcretePass {
        // Inherits execute() from ConcretePass
    }
}
