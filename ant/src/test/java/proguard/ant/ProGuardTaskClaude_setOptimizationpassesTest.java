package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setOptimizationpasses method.
 * Tests that the method properly sets the optimizationPasses field in the configuration.
 */
public class ProGuardTaskClaude_setOptimizationpassesTest {

    /**
     * Test that setOptimizationpasses sets the configuration field to a positive value.
     */
    @Test
    public void testSetOptimizationpassesPositiveValue() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value
        assertEquals(1, task.configuration.optimizationPasses,
            "optimizationPasses should default to 1");

        // Set to a different positive value
        task.setOptimizationpasses(5);

        // Verify the value changed
        assertEquals(5, task.configuration.optimizationPasses,
            "optimizationPasses should be set to 5");
    }

    /**
     * Test that setOptimizationpasses can set the value to 1.
     */
    @Test
    public void testSetOptimizationpassesOne() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimizationpasses(1);

        assertEquals(1, task.configuration.optimizationPasses,
            "optimizationPasses should be set to 1");
    }

    /**
     * Test that setOptimizationpasses can set the value to 0.
     */
    @Test
    public void testSetOptimizationpassesZero() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimizationpasses(0);

        assertEquals(0, task.configuration.optimizationPasses,
            "optimizationPasses should be set to 0");
    }

    /**
     * Test that setOptimizationpasses can set large values.
     */
    @Test
    public void testSetOptimizationpassesLargeValue() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimizationpasses(100);

        assertEquals(100, task.configuration.optimizationPasses,
            "optimizationPasses should be set to 100");
    }

    /**
     * Test that setOptimizationpasses can accept negative values.
     */
    @Test
    public void testSetOptimizationpassesNegativeValue() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimizationpasses(-1);

        assertEquals(-1, task.configuration.optimizationPasses,
            "optimizationPasses should be set to -1");
    }

    /**
     * Test that setOptimizationpasses can be called multiple times.
     */
    @Test
    public void testSetOptimizationpassesMultipleCalls() {
        ProGuardTask task = new ProGuardTask();

        // Call multiple times with different values
        task.setOptimizationpasses(3);
        assertEquals(3, task.configuration.optimizationPasses);

        task.setOptimizationpasses(7);
        assertEquals(7, task.configuration.optimizationPasses);

        task.setOptimizationpasses(1);
        assertEquals(1, task.configuration.optimizationPasses);

        task.setOptimizationpasses(10);
        assertEquals(10, task.configuration.optimizationPasses);
    }

    /**
     * Test that setOptimizationpasses works with an Ant project context.
     */
    @Test
    public void testSetOptimizationpassesWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Should work the same way when associated with a project
        task.setOptimizationpasses(8);
        assertEquals(8, task.configuration.optimizationPasses,
            "optimizationPasses should be 8 even with Ant project context");
    }

    /**
     * Test that setOptimizationpasses on different task instances are independent.
     */
    @Test
    public void testSetOptimizationpassesIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        // Set different values on different instances
        task1.setOptimizationpasses(3);
        task2.setOptimizationpasses(9);

        // Verify each instance maintains its own value
        assertEquals(3, task1.configuration.optimizationPasses,
            "task1 should have optimizationPasses as 3");
        assertEquals(9, task2.configuration.optimizationPasses,
            "task2 should have optimizationPasses as 9");
    }

    /**
     * Test that setting optimizationPasses doesn't affect other configuration fields.
     */
    @Test
    public void testSetOptimizationpassesDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();

        // Capture initial values of other fields
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;
        boolean initialPreverify = task.configuration.preverify;

        // Set optimizationPasses
        task.setOptimizationpasses(5);

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
     * Test that the optimizationPasses value persists across other setter calls.
     */
    @Test
    public void testSetOptimizationpassesPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        // Set optimizationPasses to 7
        task.setOptimizationpasses(7);

        // Call other setters
        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        // Verify optimizationPasses is still 7
        assertEquals(7, task.configuration.optimizationPasses,
            "optimizationPasses should remain 7 after other setters are called");
    }

    /**
     * Test that the default value is 1.
     */
    @Test
    public void testSetOptimizationpassesDefaultValueIsOne() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value
        assertEquals(1, task.configuration.optimizationPasses,
            "optimizationPasses should default to 1");
    }

    /**
     * Test that setOptimizationpasses can change from default value.
     */
    @Test
    public void testSetOptimizationpassesChangeFromDefault() {
        ProGuardTask task = new ProGuardTask();

        assertEquals(1, task.configuration.optimizationPasses,
            "Default should be 1");

        task.setOptimizationpasses(4);

        assertEquals(4, task.configuration.optimizationPasses,
            "Should be able to change optimizationPasses from default value");
    }

    /**
     * Test that setOptimizationpasses can set back to default value.
     */
    @Test
    public void testSetOptimizationpassesSetToDefault() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimizationpasses(10);
        assertEquals(10, task.configuration.optimizationPasses);

        task.setOptimizationpasses(1);
        assertEquals(1, task.configuration.optimizationPasses,
            "Should be able to set back to default value of 1");
    }

    /**
     * Test that different instances with same optimizationPasses value have same configuration value.
     */
    @Test
    public void testSetOptimizationpassesSameValueAcrossInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setOptimizationpasses(6);
        task2.setOptimizationpasses(6);

        assertEquals(task1.configuration.optimizationPasses, task2.configuration.optimizationPasses,
            "Both instances should have the same optimizationPasses value when set to 6");
        assertEquals(6, task1.configuration.optimizationPasses);
    }

    /**
     * Test that setOptimizationpasses works correctly after creating the task.
     */
    @Test
    public void testSetOptimizationpassesImmediatelyAfterConstruction() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setOptimizationpasses(3),
            "Should be able to call setOptimizationpasses immediately after construction");

        assertEquals(3, task.configuration.optimizationPasses,
            "optimizationPasses should be set correctly");
    }

    /**
     * Test that setOptimizationpasses with different values results in different configuration values.
     */
    @Test
    public void testSetOptimizationpassesDifferentValues() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setOptimizationpasses(2);
        task2.setOptimizationpasses(8);

        assertNotEquals(task1.configuration.optimizationPasses, task2.configuration.optimizationPasses,
            "Different values should result in different optimizationPasses");
        assertEquals(2, task1.configuration.optimizationPasses);
        assertEquals(8, task2.configuration.optimizationPasses);
    }

    /**
     * Test that setOptimizationpasses can be used in combination with other setters.
     */
    @Test
    public void testSetOptimizationpassesWithOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setOptimizationpasses(5);
        task.setOptimize(true);

        assertEquals(5, task.configuration.optimizationPasses,
            "optimizationPasses should be 5");
        assertFalse(task.configuration.shrink,
            "shrink should be false");
        assertTrue(task.configuration.optimize,
            "optimize should be true");
    }

    /**
     * Test that setOptimizationpasses can set various common values.
     */
    @Test
    public void testSetOptimizationpassesCommonValues() {
        ProGuardTask task = new ProGuardTask();

        // Test common values
        int[] commonValues = {1, 2, 3, 5, 10};
        for (int value : commonValues) {
            task.setOptimizationpasses(value);
            assertEquals(value, task.configuration.optimizationPasses,
                "optimizationPasses should be set to " + value);
        }
    }

    /**
     * Test that setOptimizationpasses repeated with same value maintains that value.
     */
    @Test
    public void testSetOptimizationpassesRepeatedSameValue() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimizationpasses(4);
        assertEquals(4, task.configuration.optimizationPasses);

        task.setOptimizationpasses(4);
        assertEquals(4, task.configuration.optimizationPasses,
            "optimizationPasses should remain 4 when set again");
    }

    /**
     * Test that setOptimizationpasses can handle boundary value Integer.MAX_VALUE.
     */
    @Test
    public void testSetOptimizationpassesMaxInt() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimizationpasses(Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, task.configuration.optimizationPasses,
            "optimizationPasses should be set to Integer.MAX_VALUE");
    }

    /**
     * Test that setOptimizationpasses can handle boundary value Integer.MIN_VALUE.
     */
    @Test
    public void testSetOptimizationpassesMinInt() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimizationpasses(Integer.MIN_VALUE);

        assertEquals(Integer.MIN_VALUE, task.configuration.optimizationPasses,
            "optimizationPasses should be set to Integer.MIN_VALUE");
    }

    /**
     * Test that setOptimizationpasses can transition between various values.
     */
    @Test
    public void testSetOptimizationpassesTransitions() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimizationpasses(5);
        assertEquals(5, task.configuration.optimizationPasses);

        task.setOptimizationpasses(0);
        assertEquals(0, task.configuration.optimizationPasses);

        task.setOptimizationpasses(20);
        assertEquals(20, task.configuration.optimizationPasses);

        task.setOptimizationpasses(1);
        assertEquals(1, task.configuration.optimizationPasses);
    }

    /**
     * Test that setOptimizationpasses is independent when optimize is disabled.
     */
    @Test
    public void testSetOptimizationpassesWithOptimizeDisabled() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimize(false);
        task.setOptimizationpasses(10);

        assertFalse(task.configuration.optimize,
            "optimize should be false");
        assertEquals(10, task.configuration.optimizationPasses,
            "optimizationPasses should still be set to 10 even when optimize is disabled");
    }

    /**
     * Test that setOptimizationpasses works with optimize enabled.
     */
    @Test
    public void testSetOptimizationpassesWithOptimizeEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setOptimize(true);
        task.setOptimizationpasses(7);

        assertTrue(task.configuration.optimize,
            "optimize should be true");
        assertEquals(7, task.configuration.optimizationPasses,
            "optimizationPasses should be set to 7 when optimize is enabled");
    }
}
