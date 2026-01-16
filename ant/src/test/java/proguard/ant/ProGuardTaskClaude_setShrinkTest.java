package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setShrink method.
 * Tests that the method properly sets the shrink field in the configuration.
 */
public class ProGuardTaskClaude_setShrinkTest {

    /**
     * Test that setShrink sets the configuration field to true.
     */
    @Test
    public void testSetShrinkTrue() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value is true
        assertTrue(task.configuration.shrink,
            "shrink should default to true");

        // Set to false first
        task.setShrink(false);
        assertFalse(task.configuration.shrink);

        // Set to true
        task.setShrink(true);

        // Verify the value changed
        assertTrue(task.configuration.shrink,
            "shrink should be set to true");
    }

    /**
     * Test that setShrink sets the configuration field to false.
     */
    @Test
    public void testSetShrinkFalse() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value is true
        assertTrue(task.configuration.shrink,
            "shrink should default to true");

        // Set to false
        task.setShrink(false);

        // Verify the value changed to false
        assertFalse(task.configuration.shrink,
            "shrink should be set to false");
    }

    /**
     * Test that setShrink can be called multiple times.
     */
    @Test
    public void testSetShrinkMultipleCalls() {
        ProGuardTask task = new ProGuardTask();

        // Call multiple times with different values
        task.setShrink(false);
        assertFalse(task.configuration.shrink);

        task.setShrink(true);
        assertTrue(task.configuration.shrink);

        task.setShrink(false);
        assertFalse(task.configuration.shrink);

        task.setShrink(false);
        assertFalse(task.configuration.shrink);
    }

    /**
     * Test that setShrink works with an Ant project context.
     */
    @Test
    public void testSetShrinkWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Should work the same way when associated with a project
        task.setShrink(false);
        assertFalse(task.configuration.shrink,
            "shrink should be false even with Ant project context");
    }

    /**
     * Test that setShrink on different task instances are independent.
     */
    @Test
    public void testSetShrinkIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        // Set different values on different instances
        task1.setShrink(true);
        task2.setShrink(false);

        // Verify each instance maintains its own value
        assertTrue(task1.configuration.shrink,
            "task1 should have shrink as true");
        assertFalse(task2.configuration.shrink,
            "task2 should have shrink as false");
    }

    /**
     * Test that setting shrink doesn't affect other configuration fields.
     */
    @Test
    public void testSetShrinkDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();

        // Capture initial values of other boolean fields
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;
        boolean initialPreverify = task.configuration.preverify;

        // Set shrink
        task.setShrink(false);

        // Verify other fields are unchanged
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
            "obfuscate field should not be affected");
        assertEquals(initialPreverify, task.configuration.preverify,
            "preverify field should not be affected");
    }

    /**
     * Test that the method maintains the value across multiple configuration calls.
     */
    @Test
    public void testSetShrinkPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        // Set shrink to false
        task.setShrink(false);

        // Call other setters
        task.setOptimize(false);
        task.setObfuscate(false);
        task.setVerbose(true);

        // Verify shrink is still false
        assertFalse(task.configuration.shrink,
            "shrink should remain false after other setters are called");
    }

    /**
     * Test that the default value is true.
     */
    @Test
    public void testSetShrinkDefaultValueIsTrue() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value
        assertTrue(task.configuration.shrink,
            "shrink should default to true");
    }

    /**
     * Test that setShrink(true) on a fresh instance maintains the default true value.
     */
    @Test
    public void testSetShrinkTrueOnFreshInstance() {
        ProGuardTask task = new ProGuardTask();

        assertTrue(task.configuration.shrink,
            "Fresh instance should have shrink = true");

        task.setShrink(true);

        assertTrue(task.configuration.shrink,
            "Setting to true on fresh instance should keep shrink at true");
    }

    /**
     * Test toggling between true and false multiple times.
     */
    @Test
    public void testSetShrinkToggling() {
        ProGuardTask task = new ProGuardTask();

        for (int i = 0; i < 5; i++) {
            task.setShrink(false);
            assertFalse(task.configuration.shrink,
                "Iteration " + i + ": shrink should be false");

            task.setShrink(true);
            assertTrue(task.configuration.shrink,
                "Iteration " + i + ": shrink should be true");
        }
    }

    /**
     * Test that setShrink(true) followed by setShrink(false) properly changes the value.
     */
    @Test
    public void testSetShrinkTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        assertTrue(task.configuration.shrink);

        task.setShrink(false);
        assertFalse(task.configuration.shrink,
            "After setting to false, shrink should be false");
    }

    /**
     * Test that setShrink(false) followed by setShrink(true) properly changes the value.
     */
    @Test
    public void testSetShrinkFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        assertFalse(task.configuration.shrink);

        task.setShrink(true);
        assertTrue(task.configuration.shrink,
            "After setting to true, shrink should be true");
    }

    /**
     * Test that different instances with same shrink value have same configuration value.
     */
    @Test
    public void testSetShrinkSameValueAcrossInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setShrink(false);
        task2.setShrink(false);

        assertEquals(task1.configuration.shrink, task2.configuration.shrink,
            "Both instances should have the same shrink value when set to false");
        assertFalse(task1.configuration.shrink);
    }

    /**
     * Test that setShrink works correctly after creating the task.
     */
    @Test
    public void testSetShrinkImmediatelyAfterConstruction() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setShrink(false),
            "Should be able to call setShrink immediately after construction");

        assertFalse(task.configuration.shrink,
            "shrink should be set correctly");
    }

    /**
     * Test that setShrink with different boolean values results in different configuration values.
     */
    @Test
    public void testSetShrinkTrueAndFalseDifferent() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setShrink(true);
        task2.setShrink(false);

        assertNotEquals(task1.configuration.shrink, task2.configuration.shrink,
            "true and false should result in different shrink values");
        assertTrue(task1.configuration.shrink);
        assertFalse(task2.configuration.shrink);
    }

    /**
     * Test that setShrink can be used in combination with other setters.
     */
    @Test
    public void testSetShrinkWithOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimize(false);
        task.setShrink(false);
        task.setObfuscate(false);

        assertFalse(task.configuration.shrink,
            "shrink should be false");
        assertFalse(task.configuration.optimize,
            "optimize should be false");
        assertFalse(task.configuration.obfuscate,
            "obfuscate should be false");
    }

    /**
     * Test that setShrink is independent from other boolean configuration fields.
     */
    @Test
    public void testSetShrinkIndependentFromOtherBooleans() {
        ProGuardTask task = new ProGuardTask();

        // Set shrink to false, others to true
        task.setShrink(false);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setPreverify(true);

        assertFalse(task.configuration.shrink,
            "shrink should be false");
        assertTrue(task.configuration.optimize,
            "optimize should be true");
        assertTrue(task.configuration.obfuscate,
            "obfuscate should be true");
        assertTrue(task.configuration.preverify,
            "preverify should be true");
    }

    /**
     * Test that setShrink(false) repeated multiple times maintains false.
     */
    @Test
    public void testSetShrinkFalseRepeated() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        assertFalse(task.configuration.shrink);

        task.setShrink(false);
        assertFalse(task.configuration.shrink,
            "shrink should remain false when setShrink(false) is called again");
    }

    /**
     * Test that setShrink(true) repeated multiple times maintains true.
     */
    @Test
    public void testSetShrinkTrueRepeated() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        assertTrue(task.configuration.shrink);

        task.setShrink(true);
        assertTrue(task.configuration.shrink,
            "shrink should remain true when setShrink(true) is called again");
    }

    /**
     * Test that shrink can be disabled even though it's enabled by default.
     */
    @Test
    public void testSetShrinkCanDisableDefaultTrue() {
        ProGuardTask task = new ProGuardTask();

        assertTrue(task.configuration.shrink,
            "Default should be true");

        task.setShrink(false);

        assertFalse(task.configuration.shrink,
            "Should be able to disable shrink from its default true value");
    }

    /**
     * Test that all processing options can be independently controlled.
     */
    @Test
    public void testSetShrinkWithAllProcessingOptions() {
        ProGuardTask task = new ProGuardTask();

        // Disable shrink, keep others enabled
        task.setShrink(false);
        assertTrue(task.configuration.optimize,
            "optimize should still be true");
        assertTrue(task.configuration.obfuscate,
            "obfuscate should still be true");
        assertTrue(task.configuration.preverify,
            "preverify should still be true");

        // Enable shrink, disable others
        task.setShrink(true);
        task.setOptimize(false);
        task.setObfuscate(false);
        task.setPreverify(false);

        assertTrue(task.configuration.shrink,
            "shrink should be true");
        assertFalse(task.configuration.optimize,
            "optimize should be false");
        assertFalse(task.configuration.obfuscate,
            "obfuscate should be false");
        assertFalse(task.configuration.preverify,
            "preverify should be false");
    }
}
