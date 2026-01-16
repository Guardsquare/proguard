package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setObfuscate method.
 * Tests that the method properly sets the obfuscate field in the configuration.
 */
public class ProGuardTaskClaude_setObfuscateTest {

    /**
     * Test that setObfuscate sets the configuration field to true.
     */
    @Test
    public void testSetObfuscateTrue() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value is true
        assertTrue(task.configuration.obfuscate,
            "obfuscate should default to true");

        // Set to false first
        task.setObfuscate(false);
        assertFalse(task.configuration.obfuscate);

        // Set to true
        task.setObfuscate(true);

        // Verify the value changed
        assertTrue(task.configuration.obfuscate,
            "obfuscate should be set to true");
    }

    /**
     * Test that setObfuscate sets the configuration field to false.
     */
    @Test
    public void testSetObfuscateFalse() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value is true
        assertTrue(task.configuration.obfuscate,
            "obfuscate should default to true");

        // Set to false
        task.setObfuscate(false);

        // Verify the value changed to false
        assertFalse(task.configuration.obfuscate,
            "obfuscate should be set to false");
    }

    /**
     * Test that setObfuscate can be called multiple times.
     */
    @Test
    public void testSetObfuscateMultipleCalls() {
        ProGuardTask task = new ProGuardTask();

        // Call multiple times with different values
        task.setObfuscate(false);
        assertFalse(task.configuration.obfuscate);

        task.setObfuscate(true);
        assertTrue(task.configuration.obfuscate);

        task.setObfuscate(false);
        assertFalse(task.configuration.obfuscate);

        task.setObfuscate(false);
        assertFalse(task.configuration.obfuscate);
    }

    /**
     * Test that setObfuscate works with an Ant project context.
     */
    @Test
    public void testSetObfuscateWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Should work the same way when associated with a project
        task.setObfuscate(false);
        assertFalse(task.configuration.obfuscate,
            "obfuscate should be false even with Ant project context");
    }

    /**
     * Test that setObfuscate on different task instances are independent.
     */
    @Test
    public void testSetObfuscateIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        // Set different values on different instances
        task1.setObfuscate(true);
        task2.setObfuscate(false);

        // Verify each instance maintains its own value
        assertTrue(task1.configuration.obfuscate,
            "task1 should have obfuscate as true");
        assertFalse(task2.configuration.obfuscate,
            "task2 should have obfuscate as false");
    }

    /**
     * Test that setting obfuscate doesn't affect other configuration fields.
     */
    @Test
    public void testSetObfuscateDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();

        // Capture initial values of other boolean fields
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialPreverify = task.configuration.preverify;

        // Set obfuscate
        task.setObfuscate(false);

        // Verify other fields are unchanged
        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialPreverify, task.configuration.preverify,
            "preverify field should not be affected");
    }

    /**
     * Test that the method maintains the value across multiple configuration calls.
     */
    @Test
    public void testSetObfuscatePersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        // Set obfuscate to false
        task.setObfuscate(false);

        // Call other setters
        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        // Verify obfuscate is still false
        assertFalse(task.configuration.obfuscate,
            "obfuscate should remain false after other setters are called");
    }

    /**
     * Test that the default value is true.
     */
    @Test
    public void testSetObfuscateDefaultValueIsTrue() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value
        assertTrue(task.configuration.obfuscate,
            "obfuscate should default to true");
    }

    /**
     * Test that setObfuscate(true) on a fresh instance maintains the default true value.
     */
    @Test
    public void testSetObfuscateTrueOnFreshInstance() {
        ProGuardTask task = new ProGuardTask();

        assertTrue(task.configuration.obfuscate,
            "Fresh instance should have obfuscate = true");

        task.setObfuscate(true);

        assertTrue(task.configuration.obfuscate,
            "Setting to true on fresh instance should keep obfuscate at true");
    }

    /**
     * Test toggling between true and false multiple times.
     */
    @Test
    public void testSetObfuscateToggling() {
        ProGuardTask task = new ProGuardTask();

        for (int i = 0; i < 5; i++) {
            task.setObfuscate(false);
            assertFalse(task.configuration.obfuscate,
                "Iteration " + i + ": obfuscate should be false");

            task.setObfuscate(true);
            assertTrue(task.configuration.obfuscate,
                "Iteration " + i + ": obfuscate should be true");
        }
    }

    /**
     * Test that setObfuscate(true) followed by setObfuscate(false) properly changes the value.
     */
    @Test
    public void testSetObfuscateTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();

        task.setObfuscate(true);
        assertTrue(task.configuration.obfuscate);

        task.setObfuscate(false);
        assertFalse(task.configuration.obfuscate,
            "After setting to false, obfuscate should be false");
    }

    /**
     * Test that setObfuscate(false) followed by setObfuscate(true) properly changes the value.
     */
    @Test
    public void testSetObfuscateFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();

        task.setObfuscate(false);
        assertFalse(task.configuration.obfuscate);

        task.setObfuscate(true);
        assertTrue(task.configuration.obfuscate,
            "After setting to true, obfuscate should be true");
    }

    /**
     * Test that different instances with same obfuscate value have same configuration value.
     */
    @Test
    public void testSetObfuscateSameValueAcrossInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setObfuscate(false);
        task2.setObfuscate(false);

        assertEquals(task1.configuration.obfuscate, task2.configuration.obfuscate,
            "Both instances should have the same obfuscate value when set to false");
        assertFalse(task1.configuration.obfuscate);
    }

    /**
     * Test that setObfuscate works correctly after creating the task.
     */
    @Test
    public void testSetObfuscateImmediatelyAfterConstruction() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setObfuscate(false),
            "Should be able to call setObfuscate immediately after construction");

        assertFalse(task.configuration.obfuscate,
            "obfuscate should be set correctly");
    }

    /**
     * Test that setObfuscate with different boolean values results in different configuration values.
     */
    @Test
    public void testSetObfuscateTrueAndFalseDifferent() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setObfuscate(true);
        task2.setObfuscate(false);

        assertNotEquals(task1.configuration.obfuscate, task2.configuration.obfuscate,
            "true and false should result in different obfuscate values");
        assertTrue(task1.configuration.obfuscate);
        assertFalse(task2.configuration.obfuscate);
    }

    /**
     * Test that setObfuscate can be used in combination with other setters.
     */
    @Test
    public void testSetObfuscateWithOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setObfuscate(false);
        task.setOptimize(false);

        assertFalse(task.configuration.obfuscate,
            "obfuscate should be false");
        assertFalse(task.configuration.shrink,
            "shrink should be false");
        assertFalse(task.configuration.optimize,
            "optimize should be false");
    }

    /**
     * Test that setObfuscate is independent from other boolean configuration fields.
     */
    @Test
    public void testSetObfuscateIndependentFromOtherBooleans() {
        ProGuardTask task = new ProGuardTask();

        // Set obfuscate to false, others to true
        task.setObfuscate(false);
        task.setShrink(true);
        task.setOptimize(true);
        task.setPreverify(true);

        assertFalse(task.configuration.obfuscate,
            "obfuscate should be false");
        assertTrue(task.configuration.shrink,
            "shrink should be true");
        assertTrue(task.configuration.optimize,
            "optimize should be true");
        assertTrue(task.configuration.preverify,
            "preverify should be true");
    }

    /**
     * Test that setObfuscate(false) repeated multiple times maintains false.
     */
    @Test
    public void testSetObfuscateFalseRepeated() {
        ProGuardTask task = new ProGuardTask();

        task.setObfuscate(false);
        assertFalse(task.configuration.obfuscate);

        task.setObfuscate(false);
        assertFalse(task.configuration.obfuscate,
            "obfuscate should remain false when setObfuscate(false) is called again");
    }

    /**
     * Test that setObfuscate(true) repeated multiple times maintains true.
     */
    @Test
    public void testSetObfuscateTrueRepeated() {
        ProGuardTask task = new ProGuardTask();

        task.setObfuscate(true);
        assertTrue(task.configuration.obfuscate);

        task.setObfuscate(true);
        assertTrue(task.configuration.obfuscate,
            "obfuscate should remain true when setObfuscate(true) is called again");
    }

    /**
     * Test that obfuscate can be disabled even though it's enabled by default.
     */
    @Test
    public void testSetObfuscateCanDisableDefaultTrue() {
        ProGuardTask task = new ProGuardTask();

        assertTrue(task.configuration.obfuscate,
            "Default should be true");

        task.setObfuscate(false);

        assertFalse(task.configuration.obfuscate,
            "Should be able to disable obfuscate from its default true value");
    }

    /**
     * Test that all processing options can be independently controlled.
     */
    @Test
    public void testSetObfuscateWithAllProcessingOptions() {
        ProGuardTask task = new ProGuardTask();

        // Disable obfuscate, keep others enabled
        task.setObfuscate(false);
        assertTrue(task.configuration.shrink,
            "shrink should still be true");
        assertTrue(task.configuration.optimize,
            "optimize should still be true");
        assertTrue(task.configuration.preverify,
            "preverify should still be true");

        // Enable obfuscate, disable others
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);
        task.setPreverify(false);

        assertTrue(task.configuration.obfuscate,
            "obfuscate should be true");
        assertFalse(task.configuration.shrink,
            "shrink should be false");
        assertFalse(task.configuration.optimize,
            "optimize should be false");
        assertFalse(task.configuration.preverify,
            "preverify should be false");
    }
}
