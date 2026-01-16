package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setSkipnonpubliclibraryclasses method.
 * Tests that the method properly sets the skipNonPublicLibraryClasses field in the configuration.
 */
public class ProGuardTaskClaude_setSkipnonpubliclibraryclassesTest {

    /**
     * Test that setSkipnonpubliclibraryclasses sets the configuration field to true.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassesTrue() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value
        assertFalse(task.configuration.skipNonPublicLibraryClasses,
            "skipNonPublicLibraryClasses should default to false");

        // Set to true
        task.setSkipnonpubliclibraryclasses(true);

        // Verify the value changed
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
            "skipNonPublicLibraryClasses should be set to true");
    }

    /**
     * Test that setSkipnonpubliclibraryclasses sets the configuration field to false.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassesFalse() {
        ProGuardTask task = new ProGuardTask();

        // First set to true
        task.setSkipnonpubliclibraryclasses(true);
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
            "skipNonPublicLibraryClasses should be true after setting");

        // Then set to false
        task.setSkipnonpubliclibraryclasses(false);

        // Verify the value changed back to false
        assertFalse(task.configuration.skipNonPublicLibraryClasses,
            "skipNonPublicLibraryClasses should be set to false");
    }

    /**
     * Test that setSkipnonpubliclibraryclasses can be called multiple times.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassesMultipleCalls() {
        ProGuardTask task = new ProGuardTask();

        // Call multiple times with different values
        task.setSkipnonpubliclibraryclasses(true);
        assertTrue(task.configuration.skipNonPublicLibraryClasses);

        task.setSkipnonpubliclibraryclasses(false);
        assertFalse(task.configuration.skipNonPublicLibraryClasses);

        task.setSkipnonpubliclibraryclasses(true);
        assertTrue(task.configuration.skipNonPublicLibraryClasses);

        task.setSkipnonpubliclibraryclasses(true);
        assertTrue(task.configuration.skipNonPublicLibraryClasses);
    }

    /**
     * Test that setSkipnonpubliclibraryclasses works with an Ant project context.
     * This ensures the method works correctly in a real Ant environment.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassesWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Should work the same way when associated with a project
        task.setSkipnonpubliclibraryclasses(true);
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
            "skipNonPublicLibraryClasses should be true even with Ant project context");
    }

    /**
     * Test that setSkipnonpubliclibraryclasses on different task instances are independent.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassesIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        // Set different values on different instances
        task1.setSkipnonpubliclibraryclasses(true);
        task2.setSkipnonpubliclibraryclasses(false);

        // Verify each instance maintains its own value
        assertTrue(task1.configuration.skipNonPublicLibraryClasses,
            "task1 should have skipNonPublicLibraryClasses as true");
        assertFalse(task2.configuration.skipNonPublicLibraryClasses,
            "task2 should have skipNonPublicLibraryClasses as false");
    }

    /**
     * Test that setting skipNonPublicLibraryClasses doesn't affect other configuration fields.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassesDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();

        // Capture initial values of other boolean fields
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;
        boolean initialPreverify = task.configuration.preverify;

        // Set skipNonPublicLibraryClasses
        task.setSkipnonpubliclibraryclasses(true);

        // Verify other fields are unchanged
        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
            "obfuscate field should not be affected");
        assertEquals(initialPreverify, task.configuration.preverify,
            "preverify field should not be affected");
    }

    /**
     * Test that the method maintains the value across multiple configuration calls.
     * This ensures the setting persists as expected when used in a typical Ant build script.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassesPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        // Set skipNonPublicLibraryClasses to true
        task.setSkipnonpubliclibraryclasses(true);

        // Call other setters
        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        // Verify skipNonPublicLibraryClasses is still true
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
            "skipNonPublicLibraryClasses should remain true after other setters are called");
    }
}
