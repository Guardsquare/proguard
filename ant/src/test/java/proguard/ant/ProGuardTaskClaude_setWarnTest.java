package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.setWarn method.
 *
 * This test class verifies the behavior of the setWarn(boolean) method
 * which controls whether ProGuard prints warnings about potential issues.
 *
 * The method has special behavior:
 * - setWarn(true): Enables warnings by setting configuration.warn to null (if it was an empty list)
 * - setWarn(false): Disables warnings by setting configuration.warn to an empty ArrayList
 *
 * Default value is null (warnings are enabled by default).
 *
 * No reflection is used as the Configuration fields are public.
 */
class ProGuardTaskClaude_setWarnTest {

    @Test
    void testSetWarnFalseCreatesEmptyList() {
        ProGuardTask task = new ProGuardTask();
        task.setWarn(false);

        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());
    }

    @Test
    void testSetWarnTrueFromEmptyListSetsNull() {
        ProGuardTask task = new ProGuardTask();

        // First disable warnings (creates empty list)
        task.setWarn(false);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());

        // Then enable warnings (should set to null)
        task.setWarn(true);
        assertNull(task.configuration.warn);
    }

    @Test
    void testSetWarnTrueFromNullDoesNothing() {
        ProGuardTask task = new ProGuardTask();

        // Default is null
        assertNull(task.configuration.warn);

        // Setting to true when already null should keep it null
        task.setWarn(true);
        assertNull(task.configuration.warn);
    }

    @Test
    void testDefaultValueIsNull() {
        ProGuardTask task = new ProGuardTask();
        assertNull(task.configuration.warn);
    }

    @Test
    void testSetWarnFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();

        task.setWarn(false);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());

        task.setWarn(true);
        assertNull(task.configuration.warn);
    }

    @Test
    void testMultipleCallsWithFalse() {
        ProGuardTask task = new ProGuardTask();

        task.setWarn(false);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());

        task.setWarn(false);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());

        task.setWarn(false);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());
    }

    @Test
    void testMultipleCallsWithTrue() {
        ProGuardTask task = new ProGuardTask();

        // Start with false to create empty list
        task.setWarn(false);

        task.setWarn(true);
        assertNull(task.configuration.warn);

        task.setWarn(true);
        assertNull(task.configuration.warn);

        task.setWarn(true);
        assertNull(task.configuration.warn);
    }

    @Test
    void testSetWarnWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        task.setProject(project);

        task.setWarn(false);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());
    }

    @Test
    void testIndependenceBetweenInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setWarn(false);
        task2.setWarn(true);

        assertNotNull(task1.configuration.warn);
        assertTrue(task1.configuration.warn.isEmpty());
        assertNull(task2.configuration.warn);
    }

    @Test
    void testDoesNotAffectOtherConfigurationFields() {
        ProGuardTask task = new ProGuardTask();
        Configuration config = task.configuration;

        // Capture initial state of other fields
        boolean initialObfuscate = config.obfuscate;
        boolean initialShrink = config.shrink;
        boolean initialOptimize = config.optimize;

        task.setWarn(false);

        // Verify other fields unchanged
        assertEquals(initialObfuscate, config.obfuscate);
        assertEquals(initialShrink, config.shrink);
        assertEquals(initialOptimize, config.optimize);
    }

    @Test
    void testPersistsAcrossOtherSetterCalls() {
        ProGuardTask task = new ProGuardTask();

        task.setWarn(false);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());
    }

    @Test
    void testToggleWarnMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setWarn(false);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());

        task.setWarn(true);
        assertNull(task.configuration.warn);

        task.setWarn(false);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());

        task.setWarn(true);
        assertNull(task.configuration.warn);
    }

    @Test
    void testSetWarnFalseWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setWarn(false);

        assertTrue(task.configuration.obfuscate);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());
    }

    @Test
    void testSetWarnTrueWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setWarn(false);
        task.setWarn(true);

        assertTrue(task.configuration.obfuscate);
        assertNull(task.configuration.warn);
    }

    @Test
    void testSetWarnFalseWithObfuscationDisabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(false);
        task.setWarn(false);

        assertFalse(task.configuration.obfuscate);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());
    }

    @Test
    void testSetWarnBeforeAndAfterOtherSettings() {
        ProGuardTask task = new ProGuardTask();

        task.setWarn(false);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testConfigurationReferenceNotNull() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task.configuration);
        task.setWarn(false);
        assertNotNull(task.configuration);
    }

    @Test
    void testSetWarnFalseWithAllProcessingOptionsEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setWarn(false);

        assertTrue(task.configuration.shrink);
        assertTrue(task.configuration.optimize);
        assertTrue(task.configuration.obfuscate);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());
    }

    @Test
    void testSetWarnTrueWithAllProcessingOptionsDisabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setOptimize(false);
        task.setObfuscate(false);
        task.setWarn(false);
        task.setWarn(true);

        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
        assertFalse(task.configuration.obfuscate);
        assertNull(task.configuration.warn);
    }

    @Test
    void testSetWarnFalseOverridesDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is null
        assertNull(task.configuration.warn);

        // Override to false (empty list)
        task.setWarn(false);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());
    }

    @Test
    void testSetWarnTrueKeepsDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is null
        assertNull(task.configuration.warn);

        // Set to true explicitly (should stay null)
        task.setWarn(true);
        assertNull(task.configuration.warn);
    }

    @Test
    void testSetWarnFalseCreatesNewListEachTime() {
        ProGuardTask task = new ProGuardTask();

        task.setWarn(false);
        Object firstList = task.configuration.warn;
        assertNotNull(firstList);

        task.setWarn(false);
        Object secondList = task.configuration.warn;
        assertNotNull(secondList);

        // Should be different instances
        assertNotSame(firstList, secondList);
    }

    @Test
    void testSetWarnTrueFromNonEmptyListDoesNothing() {
        ProGuardTask task = new ProGuardTask();

        // Manually create a non-empty list
        task.configuration.warn = new ArrayList<>();
        task.configuration.warn.add("some.pattern.*");

        // Setting to true should not change non-empty list
        task.setWarn(true);
        assertNotNull(task.configuration.warn);
        assertFalse(task.configuration.warn.isEmpty());
        assertEquals(1, task.configuration.warn.size());
        assertEquals("some.pattern.*", task.configuration.warn.get(0));
    }

    @Test
    void testSetWarnFalseReplacesNonEmptyList() {
        ProGuardTask task = new ProGuardTask();

        // Manually create a non-empty list
        task.configuration.warn = new ArrayList<>();
        task.configuration.warn.add("some.pattern.*");

        // Setting to false should replace with empty list
        task.setWarn(false);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());
    }

    @Test
    void testSetWarnTrueOnlyAffectsEmptyList() {
        ProGuardTask task = new ProGuardTask();

        // Test with null (default)
        task.setWarn(true);
        assertNull(task.configuration.warn);

        // Test with empty list
        task.configuration.warn = new ArrayList<>();
        task.setWarn(true);
        assertNull(task.configuration.warn);

        // Test with non-empty list
        task.configuration.warn = new ArrayList<>();
        task.configuration.warn.add("pattern");
        task.setWarn(true);
        assertNotNull(task.configuration.warn);
        assertEquals(1, task.configuration.warn.size());
    }

    @Test
    void testSetWarnWithIgnoreWarnings() {
        ProGuardTask task = new ProGuardTask();

        task.setWarn(false);
        task.setIgnorewarnings(true);

        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());
        assertTrue(task.configuration.ignoreWarnings);
    }

    @Test
    void testSetWarnIndependentFromIgnoreWarnings() {
        ProGuardTask task = new ProGuardTask();

        task.setIgnorewarnings(true);
        task.setWarn(false);

        assertTrue(task.configuration.ignoreWarnings);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());

        task.setIgnorewarnings(false);
        assertNotNull(task.configuration.warn);
        assertTrue(task.configuration.warn.isEmpty());
    }
}
