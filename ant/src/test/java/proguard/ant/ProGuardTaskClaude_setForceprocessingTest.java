package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setForceprocessing method.
 * Tests that the method properly sets the lastModified field in the configuration
 * based on the forceProcessing boolean value.
 */
public class ProGuardTaskClaude_setForceprocessingTest {

    /**
     * Test that setForceprocessing with true sets lastModified to Long.MAX_VALUE.
     */
    @Test
    public void testSetForceprocessingTrue() {
        ProGuardTask task = new ProGuardTask();

        // Verify default value
        assertEquals(0L, task.configuration.lastModified,
            "lastModified should default to 0");

        // Set force processing to true
        task.setForceprocessing(true);

        // Verify lastModified is set to Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
            "lastModified should be set to Long.MAX_VALUE when forceProcessing is true");
    }

    /**
     * Test that setForceprocessing with false sets lastModified to 0.
     */
    @Test
    public void testSetForceprocessingFalse() {
        ProGuardTask task = new ProGuardTask();

        // First set to true
        task.setForceprocessing(true);
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
            "lastModified should be Long.MAX_VALUE after setting to true");

        // Then set to false
        task.setForceprocessing(false);

        // Verify lastModified is set back to 0
        assertEquals(0L, task.configuration.lastModified,
            "lastModified should be set to 0 when forceProcessing is false");
    }

    /**
     * Test that setForceprocessing can be called multiple times.
     */
    @Test
    public void testSetForceprocessingMultipleCalls() {
        ProGuardTask task = new ProGuardTask();

        // Call multiple times with different values
        task.setForceprocessing(true);
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified);

        task.setForceprocessing(false);
        assertEquals(0L, task.configuration.lastModified);

        task.setForceprocessing(true);
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified);

        task.setForceprocessing(true);
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified);
    }

    /**
     * Test that setForceprocessing works with an Ant project context.
     */
    @Test
    public void testSetForceprocessingWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Should work the same way when associated with a project
        task.setForceprocessing(true);
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
            "lastModified should be Long.MAX_VALUE even with Ant project context");
    }

    /**
     * Test that setForceprocessing on different task instances are independent.
     */
    @Test
    public void testSetForceprocessingIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        // Set different values on different instances
        task1.setForceprocessing(true);
        task2.setForceprocessing(false);

        // Verify each instance maintains its own value
        assertEquals(Long.MAX_VALUE, task1.configuration.lastModified,
            "task1 should have lastModified as Long.MAX_VALUE");
        assertEquals(0L, task2.configuration.lastModified,
            "task2 should have lastModified as 0");
    }

    /**
     * Test that setting forceProcessing doesn't affect other configuration fields.
     */
    @Test
    public void testSetForceprocessingDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();

        // Capture initial values of other fields
        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;
        boolean initialPreverify = task.configuration.preverify;
        int initialTargetClassVersion = task.configuration.targetClassVersion;

        // Set forceProcessing
        task.setForceprocessing(true);

        // Verify other fields are unchanged
        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
            "obfuscate field should not be affected");
        assertEquals(initialPreverify, task.configuration.preverify,
            "preverify field should not be affected");
        assertEquals(initialTargetClassVersion, task.configuration.targetClassVersion,
            "targetClassVersion field should not be affected");
    }

    /**
     * Test that the forceProcessing value persists across other setter calls.
     */
    @Test
    public void testSetForceprocessingPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        // Set forceProcessing to true
        task.setForceprocessing(true);

        // Call other setters
        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        // Verify lastModified is still Long.MAX_VALUE
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
            "lastModified should remain Long.MAX_VALUE after other setters are called");
    }

    /**
     * Test the default value of lastModified is 0.
     */
    @Test
    public void testLastModifiedDefaultValueIsZero() {
        ProGuardTask task = new ProGuardTask();

        assertEquals(0L, task.configuration.lastModified,
            "lastModified should default to 0");
    }

    /**
     * Test that setForceprocessing(true) sets a very large value.
     */
    @Test
    public void testSetForceprocessingTrueSetsMaxValue() {
        ProGuardTask task = new ProGuardTask();

        task.setForceprocessing(true);

        assertTrue(task.configuration.lastModified > 0,
            "lastModified should be positive when forceProcessing is true");
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
            "lastModified should be exactly Long.MAX_VALUE");
    }

    /**
     * Test that setForceprocessing(false) resets to 0 even if called multiple times.
     */
    @Test
    public void testSetForceprocessingFalseRepeated() {
        ProGuardTask task = new ProGuardTask();

        task.setForceprocessing(true);
        task.setForceprocessing(false);
        assertEquals(0L, task.configuration.lastModified);

        task.setForceprocessing(false);
        assertEquals(0L, task.configuration.lastModified,
            "lastModified should remain 0 when setForceprocessing(false) is called again");
    }

    /**
     * Test that setForceprocessing(true) sets the same value every time.
     */
    @Test
    public void testSetForceprocessingTrueRepeated() {
        ProGuardTask task = new ProGuardTask();

        task.setForceprocessing(true);
        long firstValue = task.configuration.lastModified;

        task.setForceprocessing(false);
        task.setForceprocessing(true);
        long secondValue = task.configuration.lastModified;

        assertEquals(firstValue, secondValue,
            "setForceprocessing(true) should set the same value each time");
        assertEquals(Long.MAX_VALUE, firstValue,
            "Both values should be Long.MAX_VALUE");
    }

    /**
     * Test toggling between true and false multiple times.
     */
    @Test
    public void testSetForceprocessingToggling() {
        ProGuardTask task = new ProGuardTask();

        for (int i = 0; i < 5; i++) {
            task.setForceprocessing(true);
            assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
                "Iteration " + i + ": lastModified should be Long.MAX_VALUE");

            task.setForceprocessing(false);
            assertEquals(0L, task.configuration.lastModified,
                "Iteration " + i + ": lastModified should be 0");
        }
    }

    /**
     * Test that setForceprocessing(true) followed by setForceprocessing(false)
     * properly resets the value.
     */
    @Test
    public void testSetForceprocessingTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();

        task.setForceprocessing(true);
        assertNotEquals(0L, task.configuration.lastModified,
            "After setting to true, lastModified should not be 0");

        task.setForceprocessing(false);
        assertEquals(0L, task.configuration.lastModified,
            "After setting to false, lastModified should be reset to 0");
    }

    /**
     * Test that setForceprocessing(false) followed by setForceprocessing(true)
     * properly sets the value.
     */
    @Test
    public void testSetForceprocessingFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();

        task.setForceprocessing(false);
        assertEquals(0L, task.configuration.lastModified);

        task.setForceprocessing(true);
        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
            "After setting to true, lastModified should be Long.MAX_VALUE");
    }

    /**
     * Test that the method maps boolean true to Long.MAX_VALUE exactly.
     */
    @Test
    public void testSetForceprocessingTrueValueMapping() {
        ProGuardTask task = new ProGuardTask();

        task.setForceprocessing(true);

        assertEquals(9223372036854775807L, task.configuration.lastModified,
            "Long.MAX_VALUE should be exactly 9223372036854775807");
    }

    /**
     * Test that the method maps boolean false to 0 exactly.
     */
    @Test
    public void testSetForceprocessingFalseValueMapping() {
        ProGuardTask task = new ProGuardTask();

        task.setForceprocessing(true);  // Set to non-zero first
        task.setForceprocessing(false);

        assertEquals(0L, task.configuration.lastModified,
            "lastModified should be exactly 0 when forceProcessing is false");
    }

    /**
     * Test that different instances with same forceProcessing value have same lastModified.
     */
    @Test
    public void testSetForceprocessingSameValueAcrossInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setForceprocessing(true);
        task2.setForceprocessing(true);

        assertEquals(task1.configuration.lastModified, task2.configuration.lastModified,
            "Both instances should have the same lastModified value when set to true");
        assertEquals(Long.MAX_VALUE, task1.configuration.lastModified,
            "Both should be Long.MAX_VALUE");
    }

    /**
     * Test that setForceprocessing works correctly after creating the task.
     */
    @Test
    public void testSetForceprocessingImmediatelyAfterConstruction() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setForceprocessing(true),
            "Should be able to call setForceprocessing immediately after construction");

        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
            "lastModified should be set correctly");
    }

    /**
     * Test that setForceprocessing with different boolean values
     * results in different lastModified values.
     */
    @Test
    public void testSetForceprocessingTrueAndFalseDifferent() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setForceprocessing(true);
        task2.setForceprocessing(false);

        assertNotEquals(task1.configuration.lastModified, task2.configuration.lastModified,
            "true and false should result in different lastModified values");
        assertEquals(Long.MAX_VALUE, task1.configuration.lastModified);
        assertEquals(0L, task2.configuration.lastModified);
    }

    /**
     * Test that setForceprocessing(false) on a fresh instance does nothing
     * (since it's already 0).
     */
    @Test
    public void testSetForceprocessingFalseOnFreshInstance() {
        ProGuardTask task = new ProGuardTask();

        assertEquals(0L, task.configuration.lastModified,
            "Fresh instance should have lastModified = 0");

        task.setForceprocessing(false);

        assertEquals(0L, task.configuration.lastModified,
            "Setting to false on fresh instance should keep lastModified at 0");
    }

    /**
     * Test that setForceprocessing can be used in combination with other setters.
     */
    @Test
    public void testSetForceprocessingWithOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setForceprocessing(true);
        task.setOptimize(false);

        assertEquals(Long.MAX_VALUE, task.configuration.lastModified,
            "lastModified should be Long.MAX_VALUE");
        assertFalse(task.configuration.shrink,
            "shrink should be false");
        assertFalse(task.configuration.optimize,
            "optimize should be false");
    }
}
