package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.setMicroedition method.
 *
 * This test class verifies the behavior of the setMicroedition(boolean) method
 * which controls whether ProGuard targets Java Micro Edition (J2ME) for preverification.
 *
 * The method directly sets configuration.microEdition field.
 * Default value is false.
 *
 * No reflection is used as the Configuration fields are public.
 */
class ProGuardTaskClaude_setMicroeditionTest {

    @Test
    void testSetMicroeditionTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setMicroedition(true);
        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setMicroedition(false);
        assertFalse(task.configuration.microEdition);
    }

    @Test
    void testDefaultValueIsFalse() {
        ProGuardTask task = new ProGuardTask();
        assertFalse(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setMicroedition(true);
        assertTrue(task.configuration.microEdition);
        task.setMicroedition(false);
        assertFalse(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setMicroedition(false);
        assertFalse(task.configuration.microEdition);
        task.setMicroedition(true);
        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testMultipleCallsWithTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setMicroedition(true);
        task.setMicroedition(true);
        task.setMicroedition(true);
        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testMultipleCallsWithFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setMicroedition(false);
        task.setMicroedition(false);
        task.setMicroedition(false);
        assertFalse(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        task.setProject(project);
        task.setMicroedition(true);
        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testIndependenceBetweenInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setMicroedition(true);
        task2.setMicroedition(false);

        assertTrue(task1.configuration.microEdition);
        assertFalse(task2.configuration.microEdition);
    }

    @Test
    void testDoesNotAffectOtherConfigurationFields() {
        ProGuardTask task = new ProGuardTask();
        Configuration config = task.configuration;

        // Capture initial state of other fields
        boolean initialObfuscate = config.obfuscate;
        boolean initialShrink = config.shrink;
        boolean initialOptimize = config.optimize;

        task.setMicroedition(true);

        // Verify other fields unchanged
        assertEquals(initialObfuscate, config.obfuscate);
        assertEquals(initialShrink, config.shrink);
        assertEquals(initialOptimize, config.optimize);
    }

    @Test
    void testPersistsAcrossOtherSetterCalls() {
        ProGuardTask task = new ProGuardTask();

        task.setMicroedition(true);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionTrueMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setMicroedition(true);
        assertTrue(task.configuration.microEdition);

        task.setMicroedition(true);
        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionFalseMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setMicroedition(false);
        assertFalse(task.configuration.microEdition);

        task.setMicroedition(false);
        assertFalse(task.configuration.microEdition);
    }

    @Test
    void testToggleMicroeditionMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setMicroedition(true);
        assertTrue(task.configuration.microEdition);

        task.setMicroedition(false);
        assertFalse(task.configuration.microEdition);

        task.setMicroedition(true);
        assertTrue(task.configuration.microEdition);

        task.setMicroedition(false);
        assertFalse(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionTrueWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setMicroedition(true);

        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionFalseWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setMicroedition(false);

        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionTrueWithObfuscationDisabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(false);
        task.setMicroedition(true);

        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionBeforeAndAfterOtherSettings() {
        ProGuardTask task = new ProGuardTask();

        task.setMicroedition(true);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.microEdition);
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testConfigurationReferenceNotNull() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task.configuration);
        task.setMicroedition(true);
        assertNotNull(task.configuration);
    }

    @Test
    void testSetMicroeditionTrueWithAllProcessingOptionsEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setMicroedition(true);

        assertTrue(task.configuration.shrink);
        assertTrue(task.configuration.optimize);
        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionTrueWithAllProcessingOptionsDisabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setOptimize(false);
        task.setObfuscate(false);
        task.setMicroedition(true);

        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionTrueOverridesDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is false
        assertFalse(task.configuration.microEdition);

        // Override to true
        task.setMicroedition(true);
        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionFalseKeepsDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is false
        assertFalse(task.configuration.microEdition);

        // Set to false explicitly
        task.setMicroedition(false);
        assertFalse(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionWithPreverify() {
        ProGuardTask task = new ProGuardTask();
        task.setPreverify(true);
        task.setMicroedition(true);

        assertTrue(task.configuration.preverify);
        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionWithAndroid() {
        ProGuardTask task = new ProGuardTask();
        task.setAndroid(true);
        task.setMicroedition(true);

        assertTrue(task.configuration.android);
        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionIndependentFromPreverify() {
        ProGuardTask task = new ProGuardTask();

        task.setPreverify(false);
        task.setMicroedition(true);

        assertFalse(task.configuration.preverify);
        assertTrue(task.configuration.microEdition);

        task.setPreverify(true);
        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testSetMicroeditionIndependentFromAndroid() {
        ProGuardTask task = new ProGuardTask();

        task.setAndroid(true);
        task.setMicroedition(true);

        assertTrue(task.configuration.android);
        assertTrue(task.configuration.microEdition);

        task.setAndroid(false);
        assertTrue(task.configuration.microEdition);
    }

    @Test
    void testMutualExclusivityNotEnforcedBetweenMicroeditionAndAndroid() {
        ProGuardTask task = new ProGuardTask();

        // Both can be set to true (no mutual exclusivity enforcement at setter level)
        task.setMicroedition(true);
        task.setAndroid(true);

        assertTrue(task.configuration.microEdition);
        assertTrue(task.configuration.android);
    }
}
