package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.setKeepparameternames method.
 *
 * This test class verifies the behavior of the setKeepparameternames(boolean) method
 * which controls whether ProGuard keeps parameter names in the processed classes.
 *
 * The method directly sets configuration.keepParameterNames field.
 * Default value is false.
 *
 * No reflection is used as the Configuration fields are public.
 */
class ProGuardTaskClaude_setKeepparameternamesTest {

    @Test
    void testSetKeepparameternamesTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setKeepparameternames(true);
        assertTrue(task.configuration.keepParameterNames);
    }

    @Test
    void testSetKeepparameternameFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setKeepparameternames(false);
        assertFalse(task.configuration.keepParameterNames);
    }

    @Test
    void testDefaultValueIsFalse() {
        ProGuardTask task = new ProGuardTask();
        assertFalse(task.configuration.keepParameterNames);
    }

    @Test
    void testSetKeepparameternameTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setKeepparameternames(true);
        assertTrue(task.configuration.keepParameterNames);
        task.setKeepparameternames(false);
        assertFalse(task.configuration.keepParameterNames);
    }

    @Test
    void testSetKeepparameternameFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setKeepparameternames(false);
        assertFalse(task.configuration.keepParameterNames);
        task.setKeepparameternames(true);
        assertTrue(task.configuration.keepParameterNames);
    }

    @Test
    void testMultipleCallsWithTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setKeepparameternames(true);
        task.setKeepparameternames(true);
        task.setKeepparameternames(true);
        assertTrue(task.configuration.keepParameterNames);
    }

    @Test
    void testMultipleCallsWithFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setKeepparameternames(false);
        task.setKeepparameternames(false);
        task.setKeepparameternames(false);
        assertFalse(task.configuration.keepParameterNames);
    }

    @Test
    void testSetKeepparameternamesWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        task.setProject(project);
        task.setKeepparameternames(true);
        assertTrue(task.configuration.keepParameterNames);
    }

    @Test
    void testIndependenceBetweenInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setKeepparameternames(true);
        task2.setKeepparameternames(false);

        assertTrue(task1.configuration.keepParameterNames);
        assertFalse(task2.configuration.keepParameterNames);
    }

    @Test
    void testDoesNotAffectOtherConfigurationFields() {
        ProGuardTask task = new ProGuardTask();
        Configuration config = task.configuration;

        // Capture initial state of other fields
        boolean initialObfuscate = config.obfuscate;
        boolean initialShrink = config.shrink;
        boolean initialOptimize = config.optimize;

        task.setKeepparameternames(true);

        // Verify other fields unchanged
        assertEquals(initialObfuscate, config.obfuscate);
        assertEquals(initialShrink, config.shrink);
        assertEquals(initialOptimize, config.optimize);
    }

    @Test
    void testPersistsAcrossOtherSetterCalls() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepparameternames(true);
        task.setObfuscate(false);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.keepParameterNames);
    }

    @Test
    void testSetKeepparameternameTrueMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepparameternames(true);
        assertTrue(task.configuration.keepParameterNames);

        task.setKeepparameternames(true);
        assertTrue(task.configuration.keepParameterNames);
    }

    @Test
    void testSetKeepparameternameFalseMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepparameternames(false);
        assertFalse(task.configuration.keepParameterNames);

        task.setKeepparameternames(false);
        assertFalse(task.configuration.keepParameterNames);
    }

    @Test
    void testToggleKeepparameternamesMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepparameternames(true);
        assertTrue(task.configuration.keepParameterNames);

        task.setKeepparameternames(false);
        assertFalse(task.configuration.keepParameterNames);

        task.setKeepparameternames(true);
        assertTrue(task.configuration.keepParameterNames);

        task.setKeepparameternames(false);
        assertFalse(task.configuration.keepParameterNames);
    }

    @Test
    void testSetKeepparameternameTrueWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setKeepparameternames(true);

        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.keepParameterNames);
    }

    @Test
    void testSetKeepparameternameTrueWithObfuscationDisabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(false);
        task.setKeepparameternames(true);

        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.keepParameterNames);
    }

    @Test
    void testSetKeepparameternameFalseWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setKeepparameternames(false);

        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.keepParameterNames);
    }

    @Test
    void testSetKeepparameternamesBeforeAndAfterOtherSettings() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepparameternames(true);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.keepParameterNames);
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testConfigurationReferenceNotNull() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task.configuration);
        task.setKeepparameternames(true);
        assertNotNull(task.configuration);
    }

    @Test
    void testSetKeepparameternameTrueWithAllProcessingOptionsEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setKeepparameternames(true);

        assertTrue(task.configuration.shrink);
        assertTrue(task.configuration.optimize);
        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.keepParameterNames);
    }

    @Test
    void testSetKeepparameternameTrueWithAllProcessingOptionsDisabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setOptimize(false);
        task.setObfuscate(false);
        task.setKeepparameternames(true);

        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.keepParameterNames);
    }

    @Test
    void testSetKeepparameternameFalseOverridesDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is false
        assertFalse(task.configuration.keepParameterNames);

        // Set to false explicitly
        task.setKeepparameternames(false);
        assertFalse(task.configuration.keepParameterNames);
    }

    @Test
    void testSetKeepparameternameTrueOverridesDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is false
        assertFalse(task.configuration.keepParameterNames);

        // Override to true
        task.setKeepparameternames(true);
        assertTrue(task.configuration.keepParameterNames);
    }
}
