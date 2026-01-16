package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setSkipnonpubliclibraryclassmembers method.
 * Tests that the method properly sets the skipNonPublicLibraryClassMembers field in the configuration.
 */
public class ProGuardTaskClaude_setSkipnonpubliclibraryclassmembersTest {

    /**
     * Test that setSkipnonpubliclibraryclassmembers sets the configuration field to true.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassmembersTrue() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value is true
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
            "skipNonPublicLibraryClassMembers should default to true");

        // Set to false first
        task.setSkipnonpubliclibraryclassmembers(false);
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers);

        // Set to true
        task.setSkipnonpubliclibraryclassmembers(true);

        // Verify the value changed
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
            "skipNonPublicLibraryClassMembers should be set to true");
    }

    /**
     * Test that setSkipnonpubliclibraryclassmembers sets the configuration field to false.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassmembersFalse() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value is true
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
            "skipNonPublicLibraryClassMembers should default to true");

        // Set to false
        task.setSkipnonpubliclibraryclassmembers(false);

        // Verify the value changed to false
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
            "skipNonPublicLibraryClassMembers should be set to false");
    }

    /**
     * Test that setSkipnonpubliclibraryclassmembers can be called multiple times.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassmembersMultipleCalls() {
        ProGuardTask task = new ProGuardTask();

        // Call multiple times with different values
        task.setSkipnonpubliclibraryclassmembers(false);
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers);

        task.setSkipnonpubliclibraryclassmembers(true);
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers);

        task.setSkipnonpubliclibraryclassmembers(false);
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers);

        task.setSkipnonpubliclibraryclassmembers(false);
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers);
    }

    /**
     * Test that setSkipnonpubliclibraryclassmembers works with an Ant project context.
     * This ensures the method works correctly in a real Ant environment.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassmembersWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Should work the same way when associated with a project
        task.setSkipnonpubliclibraryclassmembers(false);
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
            "skipNonPublicLibraryClassMembers should be false even with Ant project context");
    }

    /**
     * Test that setSkipnonpubliclibraryclassmembers on different task instances are independent.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassmembersIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        // Set different values on different instances
        task1.setSkipnonpubliclibraryclassmembers(true);
        task2.setSkipnonpubliclibraryclassmembers(false);

        // Verify each instance maintains its own value
        assertTrue(task1.configuration.skipNonPublicLibraryClassMembers,
            "task1 should have skipNonPublicLibraryClassMembers as true");
        assertFalse(task2.configuration.skipNonPublicLibraryClassMembers,
            "task2 should have skipNonPublicLibraryClassMembers as false");
    }

    /**
     * Test that setting skipNonPublicLibraryClassMembers doesn't affect other configuration fields.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassmembersDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();

        // Capture initial values of other boolean fields
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;
        boolean initialPreverify = task.configuration.preverify;
        boolean initialSkipNonPublicLibraryClasses = task.configuration.skipNonPublicLibraryClasses;

        // Set skipNonPublicLibraryClassMembers
        task.setSkipnonpubliclibraryclassmembers(false);

        // Verify other fields are unchanged
        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
            "obfuscate field should not be affected");
        assertEquals(initialPreverify, task.configuration.preverify,
            "preverify field should not be affected");
        assertEquals(initialSkipNonPublicLibraryClasses, task.configuration.skipNonPublicLibraryClasses,
            "skipNonPublicLibraryClasses field should not be affected");
    }

    /**
     * Test that the method maintains the value across multiple configuration calls.
     * This ensures the setting persists as expected when used in a typical Ant build script.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassmembersPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        // Set skipNonPublicLibraryClassMembers to false
        task.setSkipnonpubliclibraryclassmembers(false);

        // Call other setters
        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        // Verify skipNonPublicLibraryClassMembers is still false
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
            "skipNonPublicLibraryClassMembers should remain false after other setters are called");
    }

    /**
     * Test that the default value is true.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassmembersDefaultValueIsTrue() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
            "skipNonPublicLibraryClassMembers should default to true");
    }

    /**
     * Test that setSkipnonpubliclibraryclassmembers is independent from skipNonPublicLibraryClasses.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassmembersIndependentFromSkipNonPublicLibraryClasses() {
        ProGuardTask task = new ProGuardTask();

        // Set both fields to different values
        task.setSkipnonpubliclibraryclasses(true);
        task.setSkipnonpubliclibraryclassmembers(false);

        // Verify each field maintains its own value
        assertTrue(task.configuration.skipNonPublicLibraryClasses,
            "skipNonPublicLibraryClasses should be true");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
            "skipNonPublicLibraryClassMembers should be false");
    }

    /**
     * Test that setSkipnonpubliclibraryclassmembers and setSkipnonpubliclibraryclasses
     * can both be set to the same value without interfering.
     */
    @Test
    public void testSetSkipnonpubliclibraryclassmembersWithSkipNonPublicLibraryClassesSameValue() {
        ProGuardTask task = new ProGuardTask();

        // Set both fields to true
        task.setSkipnonpubliclibraryclasses(true);
        task.setSkipnonpubliclibraryclassmembers(true);

        assertTrue(task.configuration.skipNonPublicLibraryClasses,
            "skipNonPublicLibraryClasses should be true");
        assertTrue(task.configuration.skipNonPublicLibraryClassMembers,
            "skipNonPublicLibraryClassMembers should be true");

        // Set both fields to false
        task.setSkipnonpubliclibraryclasses(false);
        task.setSkipnonpubliclibraryclassmembers(false);

        assertFalse(task.configuration.skipNonPublicLibraryClasses,
            "skipNonPublicLibraryClasses should be false");
        assertFalse(task.configuration.skipNonPublicLibraryClassMembers,
            "skipNonPublicLibraryClassMembers should be false");
    }
}
