package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setOptimize method.
 * Tests that the method properly sets the optimize field in the configuration.
 */
public class ProGuardTaskClaude_setOptimizeTest {

    /**
     * Test that setOptimize sets the configuration field to true.
     */
    @Test
    public void testSetOptimizeTrue() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value is true
        assertTrue(task.configuration.optimize,
            "optimize should default to true");

        // Set to false first
        task.setOptimize(false);
        assertFalse(task.configuration.optimize);

        // Set to true
        task.setOptimize(true);

        // Verify the value changed
        assertTrue(task.configuration.optimize,
            "optimize should be set to true");
    }

    /**
     * Test that setOptimize sets the configuration field to false.
     */
    @Test
    public void testSetOptimizeFalse() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value is true
        assertTrue(task.configuration.optimize,
            "optimize should default to true");

        // Set to false
        task.setOptimize(false);

        // Verify the value changed to false
        assertFalse(task.configuration.optimize,
            "optimize should be set to false");
    }

    /**
     * Test that setOptimize can be called multiple times.
     */
    @Test
    public void testSetOptimizeMultipleCalls() {
        ProGuardTask task = new ProGuardTask();

        // Call multiple times with different values
        task.setOptimize(false);
        assertFalse(task.configuration.optimize);

        task.setOptimize(true);
        assertTrue(task.configuration.optimize);

        task.setOptimize(false);
        assertFalse(task.configuration.optimize);

        task.setOptimize(false);
        assertFalse(task.configuration.optimize);
    }

    /**
     * Test that setOptimize works with an Ant project context.
     */
    @Test
    public void testSetOptimizeWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Should work the same way when associated with a project
        task.setOptimize(false);
        assertFalse(task.configuration.optimize,
            "optimize should be false even with Ant project context");
    }

    /**
     * Test that setOptimize on different task instances are independent.
     */
    @Test
    public void testSetOptimizeIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        // Set different values on different instances
        task1.setOptimize(true);
        task2.setOptimize(false);

        // Verify each instance maintains its own value
        assertTrue(task1.configuration.optimize,
            "task1 should have optimize as true");
        assertFalse(task2.configuration.optimize,
            "task2 should have optimize as false");
    }

    /**
     * Test that setting optimize doesn't affect other configuration fields.
     */
    @Test
    public void testSetOptimizeDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();

        // Capture initial values of other boolean fields
        boolean initialShrink = task.configuration.shrink;
        boolean initialObfuscate = task.configuration.obfuscate;
        boolean initialPreverify = task.configuration.preverify;

        // Set optimize
        task.setOptimize(false);

        // Verify other fields are unchanged
        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
            "obfuscate field should not be affected");
        assertEquals(initialPreverify, task.configuration.preverify,
            "preverify field should not be affected");
    }

    /**
     * Test that the method maintains the value across multiple configuration calls.
     */
    @Test
    public void testSetOptimizePersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        // Set optimize to false
        task.setOptimize(false);

        // Call other setters
        task.setShrink(false);
        task.setObfuscate(false);
        task.setVerbose(true);

        // Verify optimize is still false
        assertFalse(task.configuration.optimize,
            "optimize should remain false after other setters are called");
    }

    /**
     * Test that the default value is true.
     */
    @Test
    public void testSetOptimizeDefaultValueIsTrue() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value
        assertTrue(task.configuration.optimize,
            "optimize should default to true");
    }

    /**
     * Test that setOptimize(true) on a fresh instance maintains the default true value.
     */
    @Test
    public void testSetOptimizeTrueOnFreshInstance() {
        ProGuardTask task = new ProGuardTask();

        assertTrue(task.configuration.optimize,
            "Fresh instance should have optimize = true");

        task.setOptimize(true);

        assertTrue(task.configuration.optimize,
            "Setting to true on fresh instance should keep optimize at true");
    }

    /**
     * Test toggling between true and false multiple times.
     */
    @Test
    public void testSetOptimizeToggling() {
        ProGuardTask task = new ProGuardTask();

        for (int i = 0; i < 5; i++) {
            task.setOptimize(false);
            assertFalse(task.configuration.optimize,
                "Iteration " + i + ": optimize should be false");

            task.setOptimize(true);
            assertTrue(task.configuration.optimize,
                "Iteration " + i + ": optimize should be true");
        }
    }

    /**
     * Test that setOptimize(true) followed by setOptimize(false) properly changes the value.
     */
    @Test
    public void testSetOptimizeTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimize(true);
        assertTrue(task.configuration.optimize);

        task.setOptimize(false);
        assertFalse(task.configuration.optimize,
            "After setting to false, optimize should be false");
    }

    /**
     * Test that setOptimize(false) followed by setOptimize(true) properly changes the value.
     */
    @Test
    public void testSetOptimizeFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimize(false);
        assertFalse(task.configuration.optimize);

        task.setOptimize(true);
        assertTrue(task.configuration.optimize,
            "After setting to true, optimize should be true");
    }

    /**
     * Test that different instances with same optimize value have same configuration value.
     */
    @Test
    public void testSetOptimizeSameValueAcrossInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setOptimize(false);
        task2.setOptimize(false);

        assertEquals(task1.configuration.optimize, task2.configuration.optimize,
            "Both instances should have the same optimize value when set to false");
        assertFalse(task1.configuration.optimize);
    }

    /**
     * Test that setOptimize works correctly after creating the task.
     */
    @Test
    public void testSetOptimizeImmediatelyAfterConstruction() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setOptimize(false),
            "Should be able to call setOptimize immediately after construction");

        assertFalse(task.configuration.optimize,
            "optimize should be set correctly");
    }

    /**
     * Test that setOptimize with different boolean values results in different configuration values.
     */
    @Test
    public void testSetOptimizeTrueAndFalseDifferent() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setOptimize(true);
        task2.setOptimize(false);

        assertNotEquals(task1.configuration.optimize, task2.configuration.optimize,
            "true and false should result in different optimize values");
        assertTrue(task1.configuration.optimize);
        assertFalse(task2.configuration.optimize);
    }

    /**
     * Test that setOptimize can be used in combination with other setters.
     */
    @Test
    public void testSetOptimizeWithOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setOptimize(false);
        task.setObfuscate(false);

        assertFalse(task.configuration.optimize,
            "optimize should be false");
        assertFalse(task.configuration.shrink,
            "shrink should be false");
        assertFalse(task.configuration.obfuscate,
            "obfuscate should be false");
    }

    /**
     * Test that setOptimize is independent from other boolean configuration fields.
     */
    @Test
    public void testSetOptimizeIndependentFromOtherBooleans() {
        ProGuardTask task = new ProGuardTask();

        // Set optimize to false, others to true
        task.setOptimize(false);
        task.setShrink(true);
        task.setObfuscate(true);
        task.setPreverify(true);

        assertFalse(task.configuration.optimize,
            "optimize should be false");
        assertTrue(task.configuration.shrink,
            "shrink should be true");
        assertTrue(task.configuration.obfuscate,
            "obfuscate should be true");
        assertTrue(task.configuration.preverify,
            "preverify should be true");
    }

    /**
     * Test that setOptimize(false) repeated multiple times maintains false.
     */
    @Test
    public void testSetOptimizeFalseRepeated() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimize(false);
        assertFalse(task.configuration.optimize);

        task.setOptimize(false);
        assertFalse(task.configuration.optimize,
            "optimize should remain false when setOptimize(false) is called again");
    }

    /**
     * Test that setOptimize(true) repeated multiple times maintains true.
     */
    @Test
    public void testSetOptimizeTrueRepeated() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimize(true);
        assertTrue(task.configuration.optimize);

        task.setOptimize(true);
        assertTrue(task.configuration.optimize,
            "optimize should remain true when setOptimize(true) is called again");
    }

    /**
     * Test that optimize can be disabled even though it's enabled by default.
     */
    @Test
    public void testSetOptimizeCanDisableDefaultTrue() {
        ProGuardTask task = new ProGuardTask();

        assertTrue(task.configuration.optimize,
            "Default should be true");

        task.setOptimize(false);

        assertFalse(task.configuration.optimize,
            "Should be able to disable optimize from its default true value");
    }

    /**
     * Test that all processing options can be independently controlled.
     */
    @Test
    public void testSetOptimizeWithAllProcessingOptions() {
        ProGuardTask task = new ProGuardTask();

        // Disable optimize, keep others enabled
        task.setOptimize(false);
        assertTrue(task.configuration.shrink,
            "shrink should still be true");
        assertTrue(task.configuration.obfuscate,
            "obfuscate should still be true");
        assertTrue(task.configuration.preverify,
            "preverify should still be true");

        // Enable optimize, disable others
        task.setOptimize(true);
        task.setShrink(false);
        task.setObfuscate(false);
        task.setPreverify(false);

        assertTrue(task.configuration.optimize,
            "optimize should be true");
        assertFalse(task.configuration.shrink,
            "shrink should be false");
        assertFalse(task.configuration.obfuscate,
            "obfuscate should be false");
        assertFalse(task.configuration.preverify,
            "preverify should be false");
    }
}
