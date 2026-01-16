package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setAllowaccessmodification method.
 * Tests that the method properly sets the allowAccessModification field in the configuration.
 */
public class ProGuardTaskClaude_setAllowaccessmodificationTest {

    /**
     * Test that setAllowaccessmodification sets the configuration field to true.
     */
    @Test
    public void testSetAllowaccessmodificationTrue() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value is false
        assertFalse(task.configuration.allowAccessModification,
            "allowAccessModification should default to false");

        // Set to true
        task.setAllowaccessmodification(true);

        // Verify the value changed
        assertTrue(task.configuration.allowAccessModification,
            "allowAccessModification should be set to true");
    }

    /**
     * Test that setAllowaccessmodification sets the configuration field to false.
     */
    @Test
    public void testSetAllowaccessmodificationFalse() {
        ProGuardTask task = new ProGuardTask();

        // First set to true
        task.setAllowaccessmodification(true);
        assertTrue(task.configuration.allowAccessModification,
            "allowAccessModification should be true after setting");

        // Then set to false
        task.setAllowaccessmodification(false);

        // Verify the value changed back to false
        assertFalse(task.configuration.allowAccessModification,
            "allowAccessModification should be set to false");
    }

    /**
     * Test that setAllowaccessmodification can be called multiple times.
     */
    @Test
    public void testSetAllowaccessmodificationMultipleCalls() {
        ProGuardTask task = new ProGuardTask();

        // Call multiple times with different values
        task.setAllowaccessmodification(true);
        assertTrue(task.configuration.allowAccessModification);

        task.setAllowaccessmodification(false);
        assertFalse(task.configuration.allowAccessModification);

        task.setAllowaccessmodification(true);
        assertTrue(task.configuration.allowAccessModification);

        task.setAllowaccessmodification(true);
        assertTrue(task.configuration.allowAccessModification);
    }

    /**
     * Test that setAllowaccessmodification works with an Ant project context.
     */
    @Test
    public void testSetAllowaccessmodificationWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Should work the same way when associated with a project
        task.setAllowaccessmodification(true);
        assertTrue(task.configuration.allowAccessModification,
            "allowAccessModification should be true even with Ant project context");
    }

    /**
     * Test that setAllowaccessmodification on different task instances are independent.
     */
    @Test
    public void testSetAllowaccessmodificationIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        // Set different values on different instances
        task1.setAllowaccessmodification(true);
        task2.setAllowaccessmodification(false);

        // Verify each instance maintains its own value
        assertTrue(task1.configuration.allowAccessModification,
            "task1 should have allowAccessModification as true");
        assertFalse(task2.configuration.allowAccessModification,
            "task2 should have allowAccessModification as false");
    }

    /**
     * Test that setting allowAccessModification doesn't affect other configuration fields.
     */
    @Test
    public void testSetAllowaccessmodificationDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();

        // Capture initial values of other boolean fields
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;
        boolean initialPreverify = task.configuration.preverify;

        // Set allowAccessModification
        task.setAllowaccessmodification(true);

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
     */
    @Test
    public void testSetAllowaccessmodificationPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        // Set allowAccessModification to true
        task.setAllowaccessmodification(true);

        // Call other setters
        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        // Verify allowAccessModification is still true
        assertTrue(task.configuration.allowAccessModification,
            "allowAccessModification should remain true after other setters are called");
    }

    /**
     * Test that the default value is false.
     */
    @Test
    public void testSetAllowaccessmodificationDefaultValueIsFalse() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value
        assertFalse(task.configuration.allowAccessModification,
            "allowAccessModification should default to false");
    }

    /**
     * Test that setAllowaccessmodification(false) on a fresh instance maintains the default false value.
     */
    @Test
    public void testSetAllowaccessmodificationFalseOnFreshInstance() {
        ProGuardTask task = new ProGuardTask();

        assertFalse(task.configuration.allowAccessModification,
            "Fresh instance should have allowAccessModification = false");

        task.setAllowaccessmodification(false);

        assertFalse(task.configuration.allowAccessModification,
            "Setting to false on fresh instance should keep allowAccessModification at false");
    }

    /**
     * Test toggling between true and false multiple times.
     */
    @Test
    public void testSetAllowaccessmodificationToggling() {
        ProGuardTask task = new ProGuardTask();

        for (int i = 0; i < 5; i++) {
            task.setAllowaccessmodification(true);
            assertTrue(task.configuration.allowAccessModification,
                "Iteration " + i + ": allowAccessModification should be true");

            task.setAllowaccessmodification(false);
            assertFalse(task.configuration.allowAccessModification,
                "Iteration " + i + ": allowAccessModification should be false");
        }
    }

    /**
     * Test that setAllowaccessmodification(true) followed by setAllowaccessmodification(false) properly changes the value.
     */
    @Test
    public void testSetAllowaccessmodificationTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();

        task.setAllowaccessmodification(true);
        assertTrue(task.configuration.allowAccessModification);

        task.setAllowaccessmodification(false);
        assertFalse(task.configuration.allowAccessModification,
            "After setting to false, allowAccessModification should be false");
    }

    /**
     * Test that setAllowaccessmodification(false) followed by setAllowaccessmodification(true) properly changes the value.
     */
    @Test
    public void testSetAllowaccessmodificationFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();

        task.setAllowaccessmodification(false);
        assertFalse(task.configuration.allowAccessModification);

        task.setAllowaccessmodification(true);
        assertTrue(task.configuration.allowAccessModification,
            "After setting to true, allowAccessModification should be true");
    }

    /**
     * Test that different instances with same allowAccessModification value have same configuration value.
     */
    @Test
    public void testSetAllowaccessmodificationSameValueAcrossInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setAllowaccessmodification(true);
        task2.setAllowaccessmodification(true);

        assertEquals(task1.configuration.allowAccessModification, task2.configuration.allowAccessModification,
            "Both instances should have the same allowAccessModification value when set to true");
        assertTrue(task1.configuration.allowAccessModification);
    }

    /**
     * Test that setAllowaccessmodification works correctly after creating the task.
     */
    @Test
    public void testSetAllowaccessmodificationImmediatelyAfterConstruction() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setAllowaccessmodification(true),
            "Should be able to call setAllowaccessmodification immediately after construction");

        assertTrue(task.configuration.allowAccessModification,
            "allowAccessModification should be set correctly");
    }

    /**
     * Test that setAllowaccessmodification with different boolean values results in different configuration values.
     */
    @Test
    public void testSetAllowaccessmodificationTrueAndFalseDifferent() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setAllowaccessmodification(true);
        task2.setAllowaccessmodification(false);

        assertNotEquals(task1.configuration.allowAccessModification, task2.configuration.allowAccessModification,
            "true and false should result in different allowAccessModification values");
        assertTrue(task1.configuration.allowAccessModification);
        assertFalse(task2.configuration.allowAccessModification);
    }

    /**
     * Test that setAllowaccessmodification can be used in combination with other setters.
     */
    @Test
    public void testSetAllowaccessmodificationWithOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setAllowaccessmodification(true);
        task.setOptimize(false);

        assertTrue(task.configuration.allowAccessModification,
            "allowAccessModification should be true");
        assertFalse(task.configuration.shrink,
            "shrink should be false");
        assertFalse(task.configuration.optimize,
            "optimize should be false");
    }

    /**
     * Test that setAllowaccessmodification(true) repeated multiple times maintains true.
     */
    @Test
    public void testSetAllowaccessmodificationTrueRepeated() {
        ProGuardTask task = new ProGuardTask();

        task.setAllowaccessmodification(true);
        assertTrue(task.configuration.allowAccessModification);

        task.setAllowaccessmodification(true);
        assertTrue(task.configuration.allowAccessModification,
            "allowAccessModification should remain true when setAllowaccessmodification(true) is called again");
    }

    /**
     * Test that setAllowaccessmodification(false) repeated multiple times maintains false.
     */
    @Test
    public void testSetAllowaccessmodificationFalseRepeated() {
        ProGuardTask task = new ProGuardTask();

        task.setAllowaccessmodification(false);
        assertFalse(task.configuration.allowAccessModification);

        task.setAllowaccessmodification(false);
        assertFalse(task.configuration.allowAccessModification,
            "allowAccessModification should remain false when setAllowaccessmodification(false) is called again");
    }

    /**
     * Test that allowAccessModification can be enabled from its default false value.
     */
    @Test
    public void testSetAllowaccessmodificationCanEnableFromDefaultFalse() {
        ProGuardTask task = new ProGuardTask();

        assertFalse(task.configuration.allowAccessModification,
            "Default should be false");

        task.setAllowaccessmodification(true);

        assertTrue(task.configuration.allowAccessModification,
            "Should be able to enable allowAccessModification from its default false value");
    }

    /**
     * Test that setAllowaccessmodification works correctly with optimization settings.
     */
    @Test
    public void testSetAllowaccessmodificationWithOptimization() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimize(true);
        task.setAllowaccessmodification(true);

        assertTrue(task.configuration.optimize,
            "optimize should be true");
        assertTrue(task.configuration.allowAccessModification,
            "allowAccessModification should be true");
    }

    /**
     * Test that setAllowaccessmodification is independent from optimization passes setting.
     */
    @Test
    public void testSetAllowaccessmodificationIndependentFromOptimizationPasses() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimizationpasses(5);
        task.setAllowaccessmodification(true);

        assertEquals(5, task.configuration.optimizationPasses,
            "optimizationPasses should be 5");
        assertTrue(task.configuration.allowAccessModification,
            "allowAccessModification should be true");
    }
}
