package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.setIgnorewarnings method.
 *
 * This test class verifies the behavior of the setIgnorewarnings(boolean) method
 * which controls whether ProGuard ignores warnings and continues processing.
 *
 * The method directly sets configuration.ignoreWarnings field.
 * Default value is false.
 *
 * No reflection is used as the Configuration fields are public.
 */
class ProGuardTaskClaude_setIgnorewarningsTest {

    @Test
    void testSetIgnorewarningsTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setIgnorewarnings(true);
        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setIgnorewarnings(false);
        assertFalse(task.configuration.ignoreWarnings);
    }

    @Test
    void testDefaultValueIsFalse() {
        ProGuardTask task = new ProGuardTask();
        assertFalse(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setIgnorewarnings(true);
        assertTrue(task.configuration.ignoreWarnings);
        task.setIgnorewarnings(false);
        assertFalse(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setIgnorewarnings(false);
        assertFalse(task.configuration.ignoreWarnings);
        task.setIgnorewarnings(true);
        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    void testMultipleCallsWithTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setIgnorewarnings(true);
        task.setIgnorewarnings(true);
        task.setIgnorewarnings(true);
        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    void testMultipleCallsWithFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setIgnorewarnings(false);
        task.setIgnorewarnings(false);
        task.setIgnorewarnings(false);
        assertFalse(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        task.setProject(project);
        task.setIgnorewarnings(true);
        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    void testIndependenceBetweenInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setIgnorewarnings(true);
        task2.setIgnorewarnings(false);

        assertTrue(task1.configuration.ignoreWarnings);
        assertFalse(task2.configuration.ignoreWarnings);
    }

    @Test
    void testDoesNotAffectOtherConfigurationFields() {
        ProGuardTask task = new ProGuardTask();
        Configuration config = task.configuration;

        // Capture initial state of other fields
        boolean initialObfuscate = config.obfuscate;
        boolean initialShrink = config.shrink;
        boolean initialOptimize = config.optimize;

        task.setIgnorewarnings(true);

        // Verify other fields unchanged
        assertEquals(initialObfuscate, config.obfuscate);
        assertEquals(initialShrink, config.shrink);
        assertEquals(initialOptimize, config.optimize);
    }

    @Test
    void testPersistsAcrossOtherSetterCalls() {
        ProGuardTask task = new ProGuardTask();

        task.setIgnorewarnings(true);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsTrueMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setIgnorewarnings(true);
        assertTrue(task.configuration.ignoreWarnings);

        task.setIgnorewarnings(true);
        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsFalseMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setIgnorewarnings(false);
        assertFalse(task.configuration.ignoreWarnings);

        task.setIgnorewarnings(false);
        assertFalse(task.configuration.ignoreWarnings);
    }

    @Test
    void testToggleIgnorewarningsMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setIgnorewarnings(true);
        assertTrue(task.configuration.ignoreWarnings);

        task.setIgnorewarnings(false);
        assertFalse(task.configuration.ignoreWarnings);

        task.setIgnorewarnings(true);
        assertTrue(task.configuration.ignoreWarnings);

        task.setIgnorewarnings(false);
        assertFalse(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsTrueWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setIgnorewarnings(true);

        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsFalseWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setIgnorewarnings(false);

        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsTrueWithObfuscationDisabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(false);
        task.setIgnorewarnings(true);

        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsBeforeAndAfterOtherSettings() {
        ProGuardTask task = new ProGuardTask();

        task.setIgnorewarnings(true);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.ignoreWarnings);
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testConfigurationReferenceNotNull() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task.configuration);
        task.setIgnorewarnings(true);
        assertNotNull(task.configuration);
    }

    @Test
    void testSetIgnorewarningsTrueWithAllProcessingOptionsEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setIgnorewarnings(true);

        assertTrue(task.configuration.shrink);
        assertTrue(task.configuration.optimize);
        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsTrueWithAllProcessingOptionsDisabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setOptimize(false);
        task.setObfuscate(false);
        task.setIgnorewarnings(true);

        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsTrueOverridesDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is false
        assertFalse(task.configuration.ignoreWarnings);

        // Override to true
        task.setIgnorewarnings(true);
        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsFalseKeepsDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is false
        assertFalse(task.configuration.ignoreWarnings);

        // Set to false explicitly
        task.setIgnorewarnings(false);
        assertFalse(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsWithWarn() {
        ProGuardTask task = new ProGuardTask();

        task.setWarn(false);
        task.setIgnorewarnings(true);

        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());
        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsIndependentFromWarn() {
        ProGuardTask task = new ProGuardTask();

        task.setIgnorewarnings(true);
        task.setWarn(false);

        assertTrue(task.configuration.ignoreWarnings);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());

        task.setWarn(true);
        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetIgnorewarningsTrueDoesNotSuppressWarnings() {
        ProGuardTask task = new ProGuardTask();

        // Even with ignoreWarnings set to true, warnings should still be enabled
        // (ignoreWarnings means "don't stop on warnings", not "don't show warnings")
        task.setIgnorewarnings(true);

        assertTrue(task.configuration.ignoreWarnings);
        // warn should still be null (enabled) by default
        assertNull(task.configuration.warn);
    }

    @Test
    void testSetIgnorewarningsCombinedWithWarnDisabled() {
        ProGuardTask task = new ProGuardTask();

        // Disable warnings and ignore them
        task.setWarn(false);
        task.setIgnorewarnings(true);

        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());
        assertTrue(task.configuration.ignoreWarnings);
    }
}
