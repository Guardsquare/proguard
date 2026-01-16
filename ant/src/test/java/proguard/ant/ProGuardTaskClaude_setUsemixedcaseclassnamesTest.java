package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.setUsemixedcaseclassnames method.
 *
 * This test class verifies the behavior of the setUsemixedcaseclassnames(boolean) method
 * which controls whether obfuscated packages and classes can get mixed-case names.
 *
 * The method directly sets configuration.useMixedCaseClassNames field.
 * Default value is true.
 *
 * No reflection is used as the Configuration fields are public.
 */
class ProGuardTaskClaude_setUsemixedcaseclassnamesTest {

    @Test
    void testSetUsemixedcaseclassnamesTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setUsemixedcaseclassnames(true);
        assertTrue(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testSetUsemixedcaseclassnamesFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setUsemixedcaseclassnames(false);
        assertFalse(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testDefaultValueIsTrue() {
        ProGuardTask task = new ProGuardTask();
        assertTrue(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testSetUsemixedcaseclassnamesTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setUsemixedcaseclassnames(true);
        assertTrue(task.configuration.useMixedCaseClassNames);
        task.setUsemixedcaseclassnames(false);
        assertFalse(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testSetUsemixedcaseclassnamesFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setUsemixedcaseclassnames(false);
        assertFalse(task.configuration.useMixedCaseClassNames);
        task.setUsemixedcaseclassnames(true);
        assertTrue(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testMultipleCallsWithTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setUsemixedcaseclassnames(true);
        task.setUsemixedcaseclassnames(true);
        task.setUsemixedcaseclassnames(true);
        assertTrue(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testMultipleCallsWithFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setUsemixedcaseclassnames(false);
        task.setUsemixedcaseclassnames(false);
        task.setUsemixedcaseclassnames(false);
        assertFalse(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testSetUsemixedcaseclassnamesWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        task.setProject(project);
        task.setUsemixedcaseclassnames(false);
        assertFalse(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testIndependenceBetweenInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setUsemixedcaseclassnames(true);
        task2.setUsemixedcaseclassnames(false);

        assertTrue(task1.configuration.useMixedCaseClassNames);
        assertFalse(task2.configuration.useMixedCaseClassNames);
    }

    @Test
    void testDoesNotAffectOtherConfigurationFields() {
        ProGuardTask task = new ProGuardTask();
        Configuration config = task.configuration;

        // Capture initial state of other fields
        boolean initialObfuscate = config.obfuscate;
        boolean initialShrink = config.shrink;
        boolean initialOptimize = config.optimize;

        task.setUsemixedcaseclassnames(false);

        // Verify other fields unchanged
        assertEquals(initialObfuscate, config.obfuscate);
        assertEquals(initialShrink, config.shrink);
        assertEquals(initialOptimize, config.optimize);
    }

    @Test
    void testPersistsAcrossOtherSetterCalls() {
        ProGuardTask task = new ProGuardTask();

        task.setUsemixedcaseclassnames(false);
        task.setObfuscate(false);
        task.setShrink(false);
        task.setOptimize(false);

        assertFalse(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testSetUsemixedcaseclassnamesTrueMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setUsemixedcaseclassnames(true);
        assertTrue(task.configuration.useMixedCaseClassNames);

        task.setUsemixedcaseclassnames(true);
        assertTrue(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testSetUsemixedcaseclassnamesFalseMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setUsemixedcaseclassnames(false);
        assertFalse(task.configuration.useMixedCaseClassNames);

        task.setUsemixedcaseclassnames(false);
        assertFalse(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testToggleUsemixedcaseclassnamesMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setUsemixedcaseclassnames(false);
        assertFalse(task.configuration.useMixedCaseClassNames);

        task.setUsemixedcaseclassnames(true);
        assertTrue(task.configuration.useMixedCaseClassNames);

        task.setUsemixedcaseclassnames(false);
        assertFalse(task.configuration.useMixedCaseClassNames);

        task.setUsemixedcaseclassnames(true);
        assertTrue(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testSetUsemixedcaseclassnamesFalseWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setUsemixedcaseclassnames(false);

        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testSetUsemixedcaseclassnamesTrueWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setUsemixedcaseclassnames(true);

        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testSetUsemixedcaseclassnamesFalseWithObfuscationDisabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(false);
        task.setUsemixedcaseclassnames(false);

        assertFalse(task.configuration.obfuscate);
        assertFalse(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testSetUsemixedcaseclassnamesBeforeAndAfterOtherSettings() {
        ProGuardTask task = new ProGuardTask();

        task.setUsemixedcaseclassnames(false);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertFalse(task.configuration.useMixedCaseClassNames);
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testConfigurationReferenceNotNull() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task.configuration);
        task.setUsemixedcaseclassnames(false);
        assertNotNull(task.configuration);
    }

    @Test
    void testSetUsemixedcaseclassnamesFalseWithAllProcessingOptionsEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setUsemixedcaseclassnames(false);

        assertTrue(task.configuration.shrink);
        assertTrue(task.configuration.optimize);
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testSetUsemixedcaseclassnamesTrueWithAllProcessingOptionsDisabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setOptimize(false);
        task.setObfuscate(false);
        task.setUsemixedcaseclassnames(true);

        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testIndependenceFromUseuniqueclassmembernames() {
        ProGuardTask task = new ProGuardTask();

        task.setUseuniqueclassmembernames(true);
        task.setUsemixedcaseclassnames(false);

        assertTrue(task.configuration.useUniqueClassMemberNames);
        assertFalse(task.configuration.useMixedCaseClassNames);

        task.setUseuniqueclassmembernames(false);
        assertFalse(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testSetUsemixedcaseclassnamesDoesNotAffectUseuniqueclassmembernames() {
        ProGuardTask task = new ProGuardTask();

        task.setUseuniqueclassmembernames(true);
        boolean initialUseUniqueClassMemberNames = task.configuration.useUniqueClassMemberNames;

        task.setUsemixedcaseclassnames(false);

        assertEquals(initialUseUniqueClassMemberNames, task.configuration.useUniqueClassMemberNames);
        assertFalse(task.configuration.useMixedCaseClassNames);
    }

    @Test
    void testSetUsemixedcaseclassnamesFalseOverridesDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is true
        assertTrue(task.configuration.useMixedCaseClassNames);

        // Override to false
        task.setUsemixedcaseclassnames(false);
        assertFalse(task.configuration.useMixedCaseClassNames);
    }
}
