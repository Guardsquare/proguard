package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.setAddconfigurationdebugging method.
 *
 * This test class verifies the behavior of the setAddconfigurationdebugging(boolean) method
 * which controls whether ProGuard adds configuration debugging information.
 *
 * The method directly sets configuration.addConfigurationDebugging field.
 * Default value is false.
 *
 * No reflection is used as the Configuration fields are public.
 */
class ProGuardTaskClaude_setAddconfigurationdebuggingTest {

    @Test
    void testSetAddconfigurationdebuggingTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setAddconfigurationdebugging(true);
        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setAddconfigurationdebugging(false);
        assertFalse(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testDefaultValueIsFalse() {
        ProGuardTask task = new ProGuardTask();
        assertFalse(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setAddconfigurationdebugging(true);
        assertTrue(task.configuration.addConfigurationDebugging);
        task.setAddconfigurationdebugging(false);
        assertFalse(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setAddconfigurationdebugging(false);
        assertFalse(task.configuration.addConfigurationDebugging);
        task.setAddconfigurationdebugging(true);
        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testMultipleCallsWithTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setAddconfigurationdebugging(true);
        task.setAddconfigurationdebugging(true);
        task.setAddconfigurationdebugging(true);
        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testMultipleCallsWithFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setAddconfigurationdebugging(false);
        task.setAddconfigurationdebugging(false);
        task.setAddconfigurationdebugging(false);
        assertFalse(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        task.setProject(project);
        task.setAddconfigurationdebugging(true);
        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testIndependenceBetweenInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setAddconfigurationdebugging(true);
        task2.setAddconfigurationdebugging(false);

        assertTrue(task1.configuration.addConfigurationDebugging);
        assertFalse(task2.configuration.addConfigurationDebugging);
    }

    @Test
    void testDoesNotAffectOtherConfigurationFields() {
        ProGuardTask task = new ProGuardTask();
        Configuration config = task.configuration;

        // Capture initial state of other fields
        boolean initialObfuscate = config.obfuscate;
        boolean initialShrink = config.shrink;
        boolean initialOptimize = config.optimize;

        task.setAddconfigurationdebugging(true);

        // Verify other fields unchanged
        assertEquals(initialObfuscate, config.obfuscate);
        assertEquals(initialShrink, config.shrink);
        assertEquals(initialOptimize, config.optimize);
    }

    @Test
    void testPersistsAcrossOtherSetterCalls() {
        ProGuardTask task = new ProGuardTask();

        task.setAddconfigurationdebugging(true);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingTrueMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setAddconfigurationdebugging(true);
        assertTrue(task.configuration.addConfigurationDebugging);

        task.setAddconfigurationdebugging(true);
        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingFalseMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setAddconfigurationdebugging(false);
        assertFalse(task.configuration.addConfigurationDebugging);

        task.setAddconfigurationdebugging(false);
        assertFalse(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testToggleAddconfigurationdebuggingMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setAddconfigurationdebugging(true);
        assertTrue(task.configuration.addConfigurationDebugging);

        task.setAddconfigurationdebugging(false);
        assertFalse(task.configuration.addConfigurationDebugging);

        task.setAddconfigurationdebugging(true);
        assertTrue(task.configuration.addConfigurationDebugging);

        task.setAddconfigurationdebugging(false);
        assertFalse(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingTrueWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setAddconfigurationdebugging(true);

        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingFalseWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setAddconfigurationdebugging(false);

        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingTrueWithObfuscationDisabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(false);
        task.setAddconfigurationdebugging(true);

        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingBeforeAndAfterOtherSettings() {
        ProGuardTask task = new ProGuardTask();

        task.setAddconfigurationdebugging(true);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.addConfigurationDebugging);
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testConfigurationReferenceNotNull() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task.configuration);
        task.setAddconfigurationdebugging(true);
        assertNotNull(task.configuration);
    }

    @Test
    void testSetAddconfigurationdebuggingTrueWithAllProcessingOptionsEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setAddconfigurationdebugging(true);

        assertTrue(task.configuration.shrink);
        assertTrue(task.configuration.optimize);
        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingTrueWithAllProcessingOptionsDisabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setOptimize(false);
        task.setObfuscate(false);
        task.setAddconfigurationdebugging(true);

        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingTrueOverridesDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is false
        assertFalse(task.configuration.addConfigurationDebugging);

        // Override to true
        task.setAddconfigurationdebugging(true);
        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingFalseKeepsDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is false
        assertFalse(task.configuration.addConfigurationDebugging);

        // Set to false explicitly
        task.setAddconfigurationdebugging(false);
        assertFalse(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingWithVerbose() {
        ProGuardTask task = new ProGuardTask();

        task.setVerbose(true);
        task.setAddconfigurationdebugging(true);

        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingIndependentFromVerbose() {
        ProGuardTask task = new ProGuardTask();

        task.setAddconfigurationdebugging(true);
        task.setVerbose(false);

        assertTrue(task.configuration.addConfigurationDebugging);
        assertFalse(task.configuration.verbose);

        task.setVerbose(true);
        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingWithPrintconfiguration() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        task.setAddconfigurationdebugging(true);
        task.setPrintconfiguration(new java.io.File("config.txt"));

        assertTrue(task.configuration.addConfigurationDebugging);
        assertNotNull(task.configuration.printConfiguration);
    }

    @Test
    void testSetAddconfigurationdebuggingCombinedWithDump() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        task.setAddconfigurationdebugging(true);
        task.setDump(new java.io.File("dump.txt"));

        assertTrue(task.configuration.addConfigurationDebugging);
        assertNotNull(task.configuration.dump);
    }

    @Test
    void testMultipleInstancesWithDifferentValues() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        ProGuardTask task3 = new ProGuardTask();

        task1.setAddconfigurationdebugging(true);
        task2.setAddconfigurationdebugging(false);
        // task3 keeps default

        assertTrue(task1.configuration.addConfigurationDebugging);
        assertFalse(task2.configuration.addConfigurationDebugging);
        assertFalse(task3.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingWithProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();

        task.setProject(project);
        task.setAddconfigurationdebugging(true);

        assertTrue(task.configuration.addConfigurationDebugging);
        assertSame(project, task.getProject());
    }

    @Test
    void testSetAddconfigurationdebuggingBeforeProject() {
        ProGuardTask task = new ProGuardTask();

        task.setAddconfigurationdebugging(true);

        Project project = new Project();
        project.init();
        task.setProject(project);

        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingAfterProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        task.setAddconfigurationdebugging(true);

        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testSetAddconfigurationdebuggingDoesNotAffectDump() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        task.setDump(new java.io.File("dump.txt"));
        java.io.File initialDump = task.configuration.dump;

        task.setAddconfigurationdebugging(true);

        assertSame(initialDump, task.configuration.dump);
    }

    @Test
    void testSetAddconfigurationdebuggingDoesNotAffectPrintconfiguration() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        task.setPrintconfiguration(new java.io.File("config.txt"));
        java.io.File initialConfig = task.configuration.printConfiguration;

        task.setAddconfigurationdebugging(true);

        assertSame(initialConfig, task.configuration.printConfiguration);
    }
}
