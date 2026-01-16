package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setMergeinterfacesaggressively method.
 * Tests that the method properly sets the mergeInterfacesAggressively field in the configuration.
 */
public class ProGuardTaskClaude_setMergeinterfacesaggressivelyTest {

    /**
     * Test that setMergeinterfacesaggressively sets the configuration field to true.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyTrue() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value is false
        assertFalse(task.configuration.mergeInterfacesAggressively,
            "mergeInterfacesAggressively should default to false");

        // Set to true
        task.setMergeinterfacesaggressively(true);

        // Verify the value changed
        assertTrue(task.configuration.mergeInterfacesAggressively,
            "mergeInterfacesAggressively should be set to true");
    }

    /**
     * Test that setMergeinterfacesaggressively sets the configuration field to false.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyFalse() {
        ProGuardTask task = new ProGuardTask();

        // First set to true
        task.setMergeinterfacesaggressively(true);
        assertTrue(task.configuration.mergeInterfacesAggressively,
            "mergeInterfacesAggressively should be true after setting");

        // Then set to false
        task.setMergeinterfacesaggressively(false);

        // Verify the value changed back to false
        assertFalse(task.configuration.mergeInterfacesAggressively,
            "mergeInterfacesAggressively should be set to false");
    }

    /**
     * Test that setMergeinterfacesaggressively can be called multiple times.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyMultipleCalls() {
        ProGuardTask task = new ProGuardTask();

        // Call multiple times with different values
        task.setMergeinterfacesaggressively(true);
        assertTrue(task.configuration.mergeInterfacesAggressively);

        task.setMergeinterfacesaggressively(false);
        assertFalse(task.configuration.mergeInterfacesAggressively);

        task.setMergeinterfacesaggressively(true);
        assertTrue(task.configuration.mergeInterfacesAggressively);

        task.setMergeinterfacesaggressively(true);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    /**
     * Test that setMergeinterfacesaggressively works with an Ant project context.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Should work the same way when associated with a project
        task.setMergeinterfacesaggressively(true);
        assertTrue(task.configuration.mergeInterfacesAggressively,
            "mergeInterfacesAggressively should be true even with Ant project context");
    }

    /**
     * Test that setMergeinterfacesaggressively on different task instances are independent.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        // Set different values on different instances
        task1.setMergeinterfacesaggressively(true);
        task2.setMergeinterfacesaggressively(false);

        // Verify each instance maintains its own value
        assertTrue(task1.configuration.mergeInterfacesAggressively,
            "task1 should have mergeInterfacesAggressively as true");
        assertFalse(task2.configuration.mergeInterfacesAggressively,
            "task2 should have mergeInterfacesAggressively as false");
    }

    /**
     * Test that setting mergeInterfacesAggressively doesn't affect other configuration fields.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();

        // Capture initial values of other boolean fields
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;
        boolean initialPreverify = task.configuration.preverify;
        boolean initialAllowAccessModification = task.configuration.allowAccessModification;

        // Set mergeInterfacesAggressively
        task.setMergeinterfacesaggressively(true);

        // Verify other fields are unchanged
        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
            "obfuscate field should not be affected");
        assertEquals(initialPreverify, task.configuration.preverify,
            "preverify field should not be affected");
        assertEquals(initialAllowAccessModification, task.configuration.allowAccessModification,
            "allowAccessModification field should not be affected");
    }

    /**
     * Test that the method maintains the value across multiple configuration calls.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        // Set mergeInterfacesAggressively to true
        task.setMergeinterfacesaggressively(true);

        // Call other setters
        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        // Verify mergeInterfacesAggressively is still true
        assertTrue(task.configuration.mergeInterfacesAggressively,
            "mergeInterfacesAggressively should remain true after other setters are called");
    }

    /**
     * Test that the default value is false.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyDefaultValueIsFalse() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value
        assertFalse(task.configuration.mergeInterfacesAggressively,
            "mergeInterfacesAggressively should default to false");
    }

    /**
     * Test that setMergeinterfacesaggressively(false) on a fresh instance maintains the default false value.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyFalseOnFreshInstance() {
        ProGuardTask task = new ProGuardTask();

        assertFalse(task.configuration.mergeInterfacesAggressively,
            "Fresh instance should have mergeInterfacesAggressively = false");

        task.setMergeinterfacesaggressively(false);

        assertFalse(task.configuration.mergeInterfacesAggressively,
            "Setting to false on fresh instance should keep mergeInterfacesAggressively at false");
    }

    /**
     * Test toggling between true and false multiple times.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyToggling() {
        ProGuardTask task = new ProGuardTask();

        for (int i = 0; i < 5; i++) {
            task.setMergeinterfacesaggressively(true);
            assertTrue(task.configuration.mergeInterfacesAggressively,
                "Iteration " + i + ": mergeInterfacesAggressively should be true");

            task.setMergeinterfacesaggressively(false);
            assertFalse(task.configuration.mergeInterfacesAggressively,
                "Iteration " + i + ": mergeInterfacesAggressively should be false");
        }
    }

    /**
     * Test that setMergeinterfacesaggressively(true) followed by setMergeinterfacesaggressively(false) properly changes the value.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();

        task.setMergeinterfacesaggressively(true);
        assertTrue(task.configuration.mergeInterfacesAggressively);

        task.setMergeinterfacesaggressively(false);
        assertFalse(task.configuration.mergeInterfacesAggressively,
            "After setting to false, mergeInterfacesAggressively should be false");
    }

    /**
     * Test that setMergeinterfacesaggressively(false) followed by setMergeinterfacesaggressively(true) properly changes the value.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();

        task.setMergeinterfacesaggressively(false);
        assertFalse(task.configuration.mergeInterfacesAggressively);

        task.setMergeinterfacesaggressively(true);
        assertTrue(task.configuration.mergeInterfacesAggressively,
            "After setting to true, mergeInterfacesAggressively should be true");
    }

    /**
     * Test that different instances with same mergeInterfacesAggressively value have same configuration value.
     */
    @Test
    public void testSetMergeinterfacesaggressivelySameValueAcrossInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setMergeinterfacesaggressively(true);
        task2.setMergeinterfacesaggressively(true);

        assertEquals(task1.configuration.mergeInterfacesAggressively, task2.configuration.mergeInterfacesAggressively,
            "Both instances should have the same mergeInterfacesAggressively value when set to true");
        assertTrue(task1.configuration.mergeInterfacesAggressively);
    }

    /**
     * Test that setMergeinterfacesaggressively works correctly after creating the task.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyImmediatelyAfterConstruction() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setMergeinterfacesaggressively(true),
            "Should be able to call setMergeinterfacesaggressively immediately after construction");

        assertTrue(task.configuration.mergeInterfacesAggressively,
            "mergeInterfacesAggressively should be set correctly");
    }

    /**
     * Test that setMergeinterfacesaggressively with different boolean values results in different configuration values.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyTrueAndFalseDifferent() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setMergeinterfacesaggressively(true);
        task2.setMergeinterfacesaggressively(false);

        assertNotEquals(task1.configuration.mergeInterfacesAggressively, task2.configuration.mergeInterfacesAggressively,
            "true and false should result in different mergeInterfacesAggressively values");
        assertTrue(task1.configuration.mergeInterfacesAggressively);
        assertFalse(task2.configuration.mergeInterfacesAggressively);
    }

    /**
     * Test that setMergeinterfacesaggressively can be used in combination with other setters.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyWithOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setMergeinterfacesaggressively(true);
        task.setOptimize(false);

        assertTrue(task.configuration.mergeInterfacesAggressively,
            "mergeInterfacesAggressively should be true");
        assertFalse(task.configuration.shrink,
            "shrink should be false");
        assertFalse(task.configuration.optimize,
            "optimize should be false");
    }

    /**
     * Test that setMergeinterfacesaggressively(true) repeated multiple times maintains true.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyTrueRepeated() {
        ProGuardTask task = new ProGuardTask();

        task.setMergeinterfacesaggressively(true);
        assertTrue(task.configuration.mergeInterfacesAggressively);

        task.setMergeinterfacesaggressively(true);
        assertTrue(task.configuration.mergeInterfacesAggressively,
            "mergeInterfacesAggressively should remain true when setMergeinterfacesaggressively(true) is called again");
    }

    /**
     * Test that setMergeinterfacesaggressively(false) repeated multiple times maintains false.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyFalseRepeated() {
        ProGuardTask task = new ProGuardTask();

        task.setMergeinterfacesaggressively(false);
        assertFalse(task.configuration.mergeInterfacesAggressively);

        task.setMergeinterfacesaggressively(false);
        assertFalse(task.configuration.mergeInterfacesAggressively,
            "mergeInterfacesAggressively should remain false when setMergeinterfacesaggressively(false) is called again");
    }

    /**
     * Test that mergeInterfacesAggressively can be enabled from its default false value.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyCanEnableFromDefaultFalse() {
        ProGuardTask task = new ProGuardTask();

        assertFalse(task.configuration.mergeInterfacesAggressively,
            "Default should be false");

        task.setMergeinterfacesaggressively(true);

        assertTrue(task.configuration.mergeInterfacesAggressively,
            "Should be able to enable mergeInterfacesAggressively from its default false value");
    }

    /**
     * Test that setMergeinterfacesaggressively works correctly with optimization settings.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyWithOptimization() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimize(true);
        task.setMergeinterfacesaggressively(true);

        assertTrue(task.configuration.optimize,
            "optimize should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively,
            "mergeInterfacesAggressively should be true");
    }

    /**
     * Test that setMergeinterfacesaggressively is independent from allowAccessModification.
     */
    @Test
    public void testSetMergeinterfacesaggressivelyIndependentFromAllowAccessModification() {
        ProGuardTask task = new ProGuardTask();

        task.setAllowaccessmodification(true);
        task.setMergeinterfacesaggressively(true);

        assertTrue(task.configuration.allowAccessModification,
            "allowAccessModification should be true");
        assertTrue(task.configuration.mergeInterfacesAggressively,
            "mergeInterfacesAggressively should be true");
    }
}
