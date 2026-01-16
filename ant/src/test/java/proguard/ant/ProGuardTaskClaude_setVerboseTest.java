package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.setVerbose method.
 *
 * This test class verifies the behavior of the setVerbose(boolean) method
 * which controls whether ProGuard produces verbose output during processing.
 *
 * The method directly sets configuration.verbose field.
 * Default value is false.
 *
 * No reflection is used as the Configuration fields are public.
 */
class ProGuardTaskClaude_setVerboseTest {

    @Test
    void testSetVerboseTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setVerbose(true);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testSetVerboseFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setVerbose(false);
        assertFalse(task.configuration.verbose);
    }

    @Test
    void testDefaultValueIsFalse() {
        ProGuardTask task = new ProGuardTask();
        assertFalse(task.configuration.verbose);
    }

    @Test
    void testSetVerboseTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setVerbose(true);
        assertTrue(task.configuration.verbose);
        task.setVerbose(false);
        assertFalse(task.configuration.verbose);
    }

    @Test
    void testSetVerboseFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setVerbose(false);
        assertFalse(task.configuration.verbose);
        task.setVerbose(true);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testMultipleCallsWithTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setVerbose(true);
        task.setVerbose(true);
        task.setVerbose(true);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testMultipleCallsWithFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setVerbose(false);
        task.setVerbose(false);
        task.setVerbose(false);
        assertFalse(task.configuration.verbose);
    }

    @Test
    void testSetVerboseWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        task.setProject(project);
        task.setVerbose(true);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testIndependenceBetweenInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setVerbose(true);
        task2.setVerbose(false);

        assertTrue(task1.configuration.verbose);
        assertFalse(task2.configuration.verbose);
    }

    @Test
    void testDoesNotAffectOtherConfigurationFields() {
        ProGuardTask task = new ProGuardTask();
        Configuration config = task.configuration;

        // Capture initial state of other fields
        boolean initialObfuscate = config.obfuscate;
        boolean initialShrink = config.shrink;
        boolean initialOptimize = config.optimize;

        task.setVerbose(true);

        // Verify other fields unchanged
        assertEquals(initialObfuscate, config.obfuscate);
        assertEquals(initialShrink, config.shrink);
        assertEquals(initialOptimize, config.optimize);
    }

    @Test
    void testPersistsAcrossOtherSetterCalls() {
        ProGuardTask task = new ProGuardTask();

        task.setVerbose(true);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.verbose);
    }

    @Test
    void testSetVerboseTrueMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setVerbose(true);
        assertTrue(task.configuration.verbose);

        task.setVerbose(true);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testSetVerboseFalseMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setVerbose(false);
        assertFalse(task.configuration.verbose);

        task.setVerbose(false);
        assertFalse(task.configuration.verbose);
    }

    @Test
    void testToggleVerboseMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setVerbose(true);
        assertTrue(task.configuration.verbose);

        task.setVerbose(false);
        assertFalse(task.configuration.verbose);

        task.setVerbose(true);
        assertTrue(task.configuration.verbose);

        task.setVerbose(false);
        assertFalse(task.configuration.verbose);
    }

    @Test
    void testSetVerboseTrueWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setVerbose(true);

        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testSetVerboseFalseWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setVerbose(false);

        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.verbose);
    }

    @Test
    void testSetVerboseTrueWithObfuscationDisabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(false);
        task.setVerbose(true);

        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testSetVerboseBeforeAndAfterOtherSettings() {
        ProGuardTask task = new ProGuardTask();

        task.setVerbose(true);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testConfigurationReferenceNotNull() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task.configuration);
        task.setVerbose(true);
        assertNotNull(task.configuration);
    }

    @Test
    void testSetVerboseTrueWithAllProcessingOptionsEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setVerbose(true);

        assertTrue(task.configuration.shrink);
        assertTrue(task.configuration.optimize);
        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testSetVerboseTrueWithAllProcessingOptionsDisabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setOptimize(false);
        task.setObfuscate(false);
        task.setVerbose(true);

        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testSetVerboseTrueOverridesDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is false
        assertFalse(task.configuration.verbose);

        // Override to true
        task.setVerbose(true);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testSetVerboseFalseKeepsDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is false
        assertFalse(task.configuration.verbose);

        // Set to false explicitly
        task.setVerbose(false);
        assertFalse(task.configuration.verbose);
    }
}
