package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.setKeepkotlinmetadata method.
 *
 * This test class verifies the behavior of the setKeepkotlinmetadata(boolean) method
 * which controls whether ProGuard keeps Kotlin metadata annotations.
 *
 * The method directly sets configuration.keepKotlinMetadata field.
 * Default value is false.
 *
 * No reflection is used as the Configuration fields are public.
 */
class ProGuardTaskClaude_setKeepkotlinmetadataTest {

    @Test
    void testSetKeepkotlinmetadataTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setKeepkotlinmetadata(true);
        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setKeepkotlinmetadata(false);
        assertFalse(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testDefaultValueIsFalse() {
        ProGuardTask task = new ProGuardTask();
        assertFalse(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setKeepkotlinmetadata(true);
        assertTrue(task.configuration.keepKotlinMetadata);
        task.setKeepkotlinmetadata(false);
        assertFalse(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setKeepkotlinmetadata(false);
        assertFalse(task.configuration.keepKotlinMetadata);
        task.setKeepkotlinmetadata(true);
        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testMultipleCallsWithTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setKeepkotlinmetadata(true);
        task.setKeepkotlinmetadata(true);
        task.setKeepkotlinmetadata(true);
        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testMultipleCallsWithFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setKeepkotlinmetadata(false);
        task.setKeepkotlinmetadata(false);
        task.setKeepkotlinmetadata(false);
        assertFalse(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        task.setProject(project);
        task.setKeepkotlinmetadata(true);
        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testIndependenceBetweenInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setKeepkotlinmetadata(true);
        task2.setKeepkotlinmetadata(false);

        assertTrue(task1.configuration.keepKotlinMetadata);
        assertFalse(task2.configuration.keepKotlinMetadata);
    }

    @Test
    void testDoesNotAffectOtherConfigurationFields() {
        ProGuardTask task = new ProGuardTask();
        Configuration config = task.configuration;

        // Capture initial state of other fields
        boolean initialObfuscate = config.obfuscate;
        boolean initialShrink = config.shrink;
        boolean initialOptimize = config.optimize;

        task.setKeepkotlinmetadata(true);

        // Verify other fields unchanged
        assertEquals(initialObfuscate, config.obfuscate);
        assertEquals(initialShrink, config.shrink);
        assertEquals(initialOptimize, config.optimize);
    }

    @Test
    void testPersistsAcrossOtherSetterCalls() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepkotlinmetadata(true);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataTrueMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepkotlinmetadata(true);
        assertTrue(task.configuration.keepKotlinMetadata);

        task.setKeepkotlinmetadata(true);
        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataFalseMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepkotlinmetadata(false);
        assertFalse(task.configuration.keepKotlinMetadata);

        task.setKeepkotlinmetadata(false);
        assertFalse(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testToggleKeepkotlinmetadataMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepkotlinmetadata(true);
        assertTrue(task.configuration.keepKotlinMetadata);

        task.setKeepkotlinmetadata(false);
        assertFalse(task.configuration.keepKotlinMetadata);

        task.setKeepkotlinmetadata(true);
        assertTrue(task.configuration.keepKotlinMetadata);

        task.setKeepkotlinmetadata(false);
        assertFalse(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataTrueWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setKeepkotlinmetadata(true);

        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataFalseWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setKeepkotlinmetadata(false);

        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataTrueWithObfuscationDisabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(false);
        task.setKeepkotlinmetadata(true);

        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataBeforeAndAfterOtherSettings() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepkotlinmetadata(true);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.keepKotlinMetadata);
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testConfigurationReferenceNotNull() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task.configuration);
        task.setKeepkotlinmetadata(true);
        assertNotNull(task.configuration);
    }

    @Test
    void testSetKeepkotlinmetadataTrueWithAllProcessingOptionsEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setKeepkotlinmetadata(true);

        assertTrue(task.configuration.shrink);
        assertTrue(task.configuration.optimize);
        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataTrueWithAllProcessingOptionsDisabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setOptimize(false);
        task.setObfuscate(false);
        task.setKeepkotlinmetadata(true);

        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataTrueOverridesDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is false
        assertFalse(task.configuration.keepKotlinMetadata);

        // Override to true
        task.setKeepkotlinmetadata(true);
        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataFalseKeepsDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is false
        assertFalse(task.configuration.keepKotlinMetadata);

        // Set to false explicitly
        task.setKeepkotlinmetadata(false);
        assertFalse(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataIndependentFromAndroid() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepkotlinmetadata(true);
        task.setAndroid(false);

        assertTrue(task.configuration.keepKotlinMetadata);
        assertFalse(task.configuration.android);

        task.setAndroid(true);
        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataWithAndroidEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setAndroid(true);
        task.setKeepkotlinmetadata(true);

        assertTrue(task.configuration.android);
        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataWithAddconfigurationdebugging() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepkotlinmetadata(true);
        task.setAddconfigurationdebugging(true);

        assertTrue(task.configuration.keepKotlinMetadata);
        assertTrue(task.configuration.addConfigurationDebugging);
    }

    @Test
    void testMultipleInstancesWithDifferentValues() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        ProGuardTask task3 = new ProGuardTask();

        task1.setKeepkotlinmetadata(true);
        task2.setKeepkotlinmetadata(false);
        // task3 keeps default

        assertTrue(task1.configuration.keepKotlinMetadata);
        assertFalse(task2.configuration.keepKotlinMetadata);
        assertFalse(task3.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataWithProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();

        task.setProject(project);
        task.setKeepkotlinmetadata(true);

        assertTrue(task.configuration.keepKotlinMetadata);
        assertSame(project, task.getProject());
    }

    @Test
    void testSetKeepkotlinmetadataBeforeProject() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepkotlinmetadata(true);

        Project project = new Project();
        project.init();
        task.setProject(project);

        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataAfterProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        task.setKeepkotlinmetadata(true);

        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataDoesNotAffectKeepparameternames() {
        ProGuardTask task = new ProGuardTask();

        boolean initialKeepParameterNames = task.configuration.keepParameterNames;

        task.setKeepkotlinmetadata(true);

        assertEquals(initialKeepParameterNames, task.configuration.keepParameterNames);
    }

    @Test
    void testSetKeepkotlinmetadataWithKeepparameternames() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepparameternames(true);
        task.setKeepkotlinmetadata(true);

        assertTrue(task.configuration.keepParameterNames);
        assertTrue(task.configuration.keepKotlinMetadata);
    }

    @Test
    void testSetKeepkotlinmetadataIndependentFromKeepparameternames() {
        ProGuardTask task = new ProGuardTask();

        task.setKeepkotlinmetadata(true);
        task.setKeepparameternames(false);

        assertTrue(task.configuration.keepKotlinMetadata);
        assertFalse(task.configuration.keepParameterNames);

        task.setKeepparameternames(true);
        assertTrue(task.configuration.keepKotlinMetadata);
    }
}
