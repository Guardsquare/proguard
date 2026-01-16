package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setOverloadaggressively method.
 * Tests that the method properly sets the overloadAggressively field in the configuration.
 */
public class ProGuardTaskClaude_setOverloadaggressivelyTest {

    /**
     * Test that setOverloadaggressively sets the configuration field to true.
     */
    @Test
    public void testSetOverloadaggressivelyTrue() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value is false
        assertFalse(task.configuration.overloadAggressively,
            "overloadAggressively should default to false");

        // Set to true
        task.setOverloadaggressively(true);

        // Verify the value changed
        assertTrue(task.configuration.overloadAggressively,
            "overloadAggressively should be set to true");
    }

    /**
     * Test that setOverloadaggressively sets the configuration field to false.
     */
    @Test
    public void testSetOverloadaggressivelyFalse() {
        ProGuardTask task = new ProGuardTask();

        // First set to true
        task.setOverloadaggressively(true);
        assertTrue(task.configuration.overloadAggressively,
            "overloadAggressively should be true after setting");

        // Then set to false
        task.setOverloadaggressively(false);

        // Verify the value changed back to false
        assertFalse(task.configuration.overloadAggressively,
            "overloadAggressively should be set to false");
    }

    /**
     * Test that setOverloadaggressively can be called multiple times.
     */
    @Test
    public void testSetOverloadaggressivelyMultipleCalls() {
        ProGuardTask task = new ProGuardTask();

        // Call multiple times with different values
        task.setOverloadaggressively(true);
        assertTrue(task.configuration.overloadAggressively);

        task.setOverloadaggressively(false);
        assertFalse(task.configuration.overloadAggressively);

        task.setOverloadaggressively(true);
        assertTrue(task.configuration.overloadAggressively);

        task.setOverloadaggressively(true);
        assertTrue(task.configuration.overloadAggressively);
    }

    /**
     * Test that setOverloadaggressively works with an Ant project context.
     */
    @Test
    public void testSetOverloadaggressivelyWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Should work the same way when associated with a project
        task.setOverloadaggressively(true);
        assertTrue(task.configuration.overloadAggressively,
            "overloadAggressively should be true even with Ant project context");
    }

    /**
     * Test that setOverloadaggressively on different task instances are independent.
     */
    @Test
    public void testSetOverloadaggressivelyIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        // Set different values on different instances
        task1.setOverloadaggressively(true);
        task2.setOverloadaggressively(false);

        // Verify each instance maintains its own value
        assertTrue(task1.configuration.overloadAggressively,
            "task1 should have overloadAggressively as true");
        assertFalse(task2.configuration.overloadAggressively,
            "task2 should have overloadAggressively as false");
    }

    /**
     * Test that setting overloadAggressively doesn't affect other configuration fields.
     */
    @Test
    public void testSetOverloadaggressivelyDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();

        // Capture initial values of other boolean fields
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;
        boolean initialPreverify = task.configuration.preverify;

        // Set overloadAggressively
        task.setOverloadaggressively(true);

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
    public void testSetOverloadaggressivelyPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        // Set overloadAggressively to true
        task.setOverloadaggressively(true);

        // Call other setters
        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        // Verify overloadAggressively is still true
        assertTrue(task.configuration.overloadAggressively,
            "overloadAggressively should remain true after other setters are called");
    }

    /**
     * Test that the default value is false.
     */
    @Test
    public void testSetOverloadaggressivelyDefaultValueIsFalse() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value
        assertFalse(task.configuration.overloadAggressively,
            "overloadAggressively should default to false");
    }

    /**
     * Test that setOverloadaggressively(false) on a fresh instance maintains the default false value.
     */
    @Test
    public void testSetOverloadaggressivelyFalseOnFreshInstance() {
        ProGuardTask task = new ProGuardTask();

        assertFalse(task.configuration.overloadAggressively,
            "Fresh instance should have overloadAggressively = false");

        task.setOverloadaggressively(false);

        assertFalse(task.configuration.overloadAggressively,
            "Setting to false on fresh instance should keep overloadAggressively at false");
    }

    /**
     * Test toggling between true and false multiple times.
     */
    @Test
    public void testSetOverloadaggressivelyToggling() {
        ProGuardTask task = new ProGuardTask();

        for (int i = 0; i < 5; i++) {
            task.setOverloadaggressively(true);
            assertTrue(task.configuration.overloadAggressively,
                "Iteration " + i + ": overloadAggressively should be true");

            task.setOverloadaggressively(false);
            assertFalse(task.configuration.overloadAggressively,
                "Iteration " + i + ": overloadAggressively should be false");
        }
    }

    /**
     * Test that setOverloadaggressively(true) followed by setOverloadaggressively(false) properly changes the value.
     */
    @Test
    public void testSetOverloadaggressivelyTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();

        task.setOverloadaggressively(true);
        assertTrue(task.configuration.overloadAggressively);

        task.setOverloadaggressively(false);
        assertFalse(task.configuration.overloadAggressively,
            "After setting to false, overloadAggressively should be false");
    }

    /**
     * Test that setOverloadaggressively(false) followed by setOverloadaggressively(true) properly changes the value.
     */
    @Test
    public void testSetOverloadaggressivelyFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();

        task.setOverloadaggressively(false);
        assertFalse(task.configuration.overloadAggressively);

        task.setOverloadaggressively(true);
        assertTrue(task.configuration.overloadAggressively,
            "After setting to true, overloadAggressively should be true");
    }

    /**
     * Test that different instances with same overloadAggressively value have same configuration value.
     */
    @Test
    public void testSetOverloadaggressivelySameValueAcrossInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setOverloadaggressively(true);
        task2.setOverloadaggressively(true);

        assertEquals(task1.configuration.overloadAggressively, task2.configuration.overloadAggressively,
            "Both instances should have the same overloadAggressively value when set to true");
        assertTrue(task1.configuration.overloadAggressively);
    }

    /**
     * Test that setOverloadaggressively works correctly after creating the task.
     */
    @Test
    public void testSetOverloadaggressivelyImmediatelyAfterConstruction() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setOverloadaggressively(true),
            "Should be able to call setOverloadaggressively immediately after construction");

        assertTrue(task.configuration.overloadAggressively,
            "overloadAggressively should be set correctly");
    }

    /**
     * Test that setOverloadaggressively with different boolean values results in different configuration values.
     */
    @Test
    public void testSetOverloadaggressivelyTrueAndFalseDifferent() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setOverloadaggressively(true);
        task2.setOverloadaggressively(false);

        assertNotEquals(task1.configuration.overloadAggressively, task2.configuration.overloadAggressively,
            "true and false should result in different overloadAggressively values");
        assertTrue(task1.configuration.overloadAggressively);
        assertFalse(task2.configuration.overloadAggressively);
    }

    /**
     * Test that setOverloadaggressively can be used in combination with other setters.
     */
    @Test
    public void testSetOverloadaggressivelyWithOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        task.setObfuscate(true);
        task.setOverloadaggressively(true);
        task.setShrink(false);

        assertTrue(task.configuration.overloadAggressively,
            "overloadAggressively should be true");
        assertTrue(task.configuration.obfuscate,
            "obfuscate should be true");
        assertFalse(task.configuration.shrink,
            "shrink should be false");
    }

    /**
     * Test that setOverloadaggressively(true) repeated multiple times maintains true.
     */
    @Test
    public void testSetOverloadaggressivelyTrueRepeated() {
        ProGuardTask task = new ProGuardTask();

        task.setOverloadaggressively(true);
        assertTrue(task.configuration.overloadAggressively);

        task.setOverloadaggressively(true);
        assertTrue(task.configuration.overloadAggressively,
            "overloadAggressively should remain true when setOverloadaggressively(true) is called again");
    }

    /**
     * Test that setOverloadaggressively(false) repeated multiple times maintains false.
     */
    @Test
    public void testSetOverloadaggressivelyFalseRepeated() {
        ProGuardTask task = new ProGuardTask();

        task.setOverloadaggressively(false);
        assertFalse(task.configuration.overloadAggressively);

        task.setOverloadaggressively(false);
        assertFalse(task.configuration.overloadAggressively,
            "overloadAggressively should remain false when setOverloadaggressively(false) is called again");
    }

    /**
     * Test that overloadAggressively can be enabled from its default false value.
     */
    @Test
    public void testSetOverloadaggressivelyCanEnableFromDefaultFalse() {
        ProGuardTask task = new ProGuardTask();

        assertFalse(task.configuration.overloadAggressively,
            "Default should be false");

        task.setOverloadaggressively(true);

        assertTrue(task.configuration.overloadAggressively,
            "Should be able to enable overloadAggressively from its default false value");
    }

    /**
     * Test that setOverloadaggressively works correctly with obfuscation settings.
     */
    @Test
    public void testSetOverloadaggressivelyWithObfuscation() {
        ProGuardTask task = new ProGuardTask();

        task.setObfuscate(true);
        task.setOverloadaggressively(true);

        assertTrue(task.configuration.obfuscate,
            "obfuscate should be true");
        assertTrue(task.configuration.overloadAggressively,
            "overloadAggressively should be true");
    }

    /**
     * Test that setOverloadaggressively works when obfuscation is disabled.
     */
    @Test
    public void testSetOverloadaggressivelyWithObfuscationDisabled() {
        ProGuardTask task = new ProGuardTask();

        task.setObfuscate(false);
        task.setOverloadaggressively(true);

        assertFalse(task.configuration.obfuscate,
            "obfuscate should be false");
        assertTrue(task.configuration.overloadAggressively,
            "overloadAggressively should still be true even when obfuscate is disabled");
    }
}
