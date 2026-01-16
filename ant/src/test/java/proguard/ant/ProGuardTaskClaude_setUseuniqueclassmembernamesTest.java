package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.setUseuniqueclassmembernames method.
 *
 * This test class verifies the behavior of the setUseuniqueclassmembernames(boolean) method
 * which controls whether ProGuard generates globally unique class member names.
 *
 * The method directly sets configuration.useUniqueClassMemberNames field.
 * Default value is false.
 *
 * No reflection is used as the Configuration fields are public.
 */
class ProGuardTaskClaude_setUseuniqueclassmembernamesTest {

    @Test
    void testSetUseuniqueclassmembernamesTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setUseuniqueclassmembernames(true);
        assertTrue(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testSetUseuniqueclassmembernamesFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setUseuniqueclassmembernames(false);
        assertFalse(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testDefaultValueIsFalse() {
        ProGuardTask task = new ProGuardTask();
        assertFalse(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testSetUseuniqueclassmembernamesTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setUseuniqueclassmembernames(true);
        assertTrue(task.configuration.useUniqueClassMemberNames);
        task.setUseuniqueclassmembernames(false);
        assertFalse(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testSetUseuniqueclassmembernamesFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setUseuniqueclassmembernames(false);
        assertFalse(task.configuration.useUniqueClassMemberNames);
        task.setUseuniqueclassmembernames(true);
        assertTrue(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testMultipleCallsWithTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setUseuniqueclassmembernames(true);
        task.setUseuniqueclassmembernames(true);
        task.setUseuniqueclassmembernames(true);
        assertTrue(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testMultipleCallsWithFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setUseuniqueclassmembernames(true);
        task.setUseuniqueclassmembernames(false);
        task.setUseuniqueclassmembernames(false);
        task.setUseuniqueclassmembernames(false);
        assertFalse(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testSetUseuniqueclassmembernamesWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        task.setProject(project);
        task.setUseuniqueclassmembernames(true);
        assertTrue(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testIndependenceBetweenInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setUseuniqueclassmembernames(true);
        task2.setUseuniqueclassmembernames(false);

        assertTrue(task1.configuration.useUniqueClassMemberNames);
        assertFalse(task2.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testDoesNotAffectOtherConfigurationFields() {
        ProGuardTask task = new ProGuardTask();
        Configuration config = task.configuration;

        // Capture initial state of other fields
        boolean initialObfuscate = config.obfuscate;
        boolean initialShrink = config.shrink;
        boolean initialOptimize = config.optimize;

        task.setUseuniqueclassmembernames(true);

        // Verify other fields unchanged
        assertEquals(initialObfuscate, config.obfuscate);
        assertEquals(initialShrink, config.shrink);
        assertEquals(initialOptimize, config.optimize);
    }

    @Test
    void testPersistsAcrossOtherSetterCalls() {
        ProGuardTask task = new ProGuardTask();

        task.setUseuniqueclassmembernames(true);
        task.setObfuscate(false);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testSetUseuniqueclassmembernamesTrueMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setUseuniqueclassmembernames(true);
        assertTrue(task.configuration.useUniqueClassMemberNames);

        task.setUseuniqueclassmembernames(true);
        assertTrue(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testSetUseuniqueclassmembernamesFalseMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setUseuniqueclassmembernames(false);
        assertFalse(task.configuration.useUniqueClassMemberNames);

        task.setUseuniqueclassmembernames(false);
        assertFalse(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testToggleUseuniqueclassmembernamesMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setUseuniqueclassmembernames(true);
        assertTrue(task.configuration.useUniqueClassMemberNames);

        task.setUseuniqueclassmembernames(false);
        assertFalse(task.configuration.useUniqueClassMemberNames);

        task.setUseuniqueclassmembernames(true);
        assertTrue(task.configuration.useUniqueClassMemberNames);

        task.setUseuniqueclassmembernames(false);
        assertFalse(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testSetUseuniqueclassmembernamesTrueWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setUseuniqueclassmembernames(true);

        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testSetUseuniqueclassmembernamesTrueWithObfuscationDisabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(false);
        task.setUseuniqueclassmembernames(true);

        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testSetUseuniqueclassmembernamesFalseWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setUseuniqueclassmembernames(false);

        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testSetUseuniqueclassmembernamesBeforeAndAfterOtherSettings() {
        ProGuardTask task = new ProGuardTask();

        task.setUseuniqueclassmembernames(true);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.useUniqueClassMemberNames);
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testConfigurationReferenceNotNull() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task.configuration);
        task.setUseuniqueclassmembernames(true);
        assertNotNull(task.configuration);
    }

    @Test
    void testSetUseuniqueclassmembernamesWithAllProcessingOptionsEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setUseuniqueclassmembernames(true);

        assertTrue(task.configuration.shrink);
        assertTrue(task.configuration.optimize);
        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testSetUseuniqueclassmembernamesWithAllProcessingOptionsDisabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setOptimize(false);
        task.setObfuscate(false);
        task.setUseuniqueclassmembernames(true);

        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testIndependenceFromOverloadaggressively() {
        ProGuardTask task = new ProGuardTask();

        task.setOverloadaggressively(true);
        task.setUseuniqueclassmembernames(true);

        assertTrue(task.configuration.overloadAggressively);
        assertTrue(task.configuration.useUniqueClassMemberNames);

        task.setOverloadaggressively(false);
        assertTrue(task.configuration.useUniqueClassMemberNames);
    }

    @Test
    void testSetUseuniqueclassmembernamesDoesNotAffectOverloadaggressively() {
        ProGuardTask task = new ProGuardTask();

        task.setOverloadaggressively(true);
        boolean initialOverloadAggressively = task.configuration.overloadAggressively;

        task.setUseuniqueclassmembernames(true);

        assertEquals(initialOverloadAggressively, task.configuration.overloadAggressively);
        assertTrue(task.configuration.useUniqueClassMemberNames);
    }
}
