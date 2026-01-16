package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.setAndroid method.
 *
 * This test class verifies the behavior of the setAndroid(boolean) method
 * which controls whether ProGuard targets the Android platform for optimization
 * and preverification.
 *
 * The method directly sets configuration.android field.
 * Default value is false.
 *
 * No reflection is used as the Configuration fields are public.
 */
class ProGuardTaskClaude_setAndroidTest {

    @Test
    void testSetAndroidTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setAndroid(true);
        assertTrue(task.configuration.android);
    }

    @Test
    void testSetAndroidFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setAndroid(false);
        assertFalse(task.configuration.android);
    }

    @Test
    void testDefaultValueIsFalse() {
        ProGuardTask task = new ProGuardTask();
        assertFalse(task.configuration.android);
    }

    @Test
    void testSetAndroidTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setAndroid(true);
        assertTrue(task.configuration.android);
        task.setAndroid(false);
        assertFalse(task.configuration.android);
    }

    @Test
    void testSetAndroidFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setAndroid(false);
        assertFalse(task.configuration.android);
        task.setAndroid(true);
        assertTrue(task.configuration.android);
    }

    @Test
    void testMultipleCallsWithTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setAndroid(true);
        task.setAndroid(true);
        task.setAndroid(true);
        assertTrue(task.configuration.android);
    }

    @Test
    void testMultipleCallsWithFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setAndroid(false);
        task.setAndroid(false);
        task.setAndroid(false);
        assertFalse(task.configuration.android);
    }

    @Test
    void testSetAndroidWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        task.setProject(project);
        task.setAndroid(true);
        assertTrue(task.configuration.android);
    }

    @Test
    void testIndependenceBetweenInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setAndroid(true);
        task2.setAndroid(false);

        assertTrue(task1.configuration.android);
        assertFalse(task2.configuration.android);
    }

    @Test
    void testDoesNotAffectOtherConfigurationFields() {
        ProGuardTask task = new ProGuardTask();
        Configuration config = task.configuration;

        // Capture initial state of other fields
        boolean initialObfuscate = config.obfuscate;
        boolean initialShrink = config.shrink;
        boolean initialOptimize = config.optimize;

        task.setAndroid(true);

        // Verify other fields unchanged
        assertEquals(initialObfuscate, config.obfuscate);
        assertEquals(initialShrink, config.shrink);
        assertEquals(initialOptimize, config.optimize);
    }

    @Test
    void testPersistsAcrossOtherSetterCalls() {
        ProGuardTask task = new ProGuardTask();

        task.setAndroid(true);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.android);
    }

    @Test
    void testSetAndroidTrueMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setAndroid(true);
        assertTrue(task.configuration.android);

        task.setAndroid(true);
        assertTrue(task.configuration.android);
    }

    @Test
    void testSetAndroidFalseMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setAndroid(false);
        assertFalse(task.configuration.android);

        task.setAndroid(false);
        assertFalse(task.configuration.android);
    }

    @Test
    void testToggleAndroidMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setAndroid(true);
        assertTrue(task.configuration.android);

        task.setAndroid(false);
        assertFalse(task.configuration.android);

        task.setAndroid(true);
        assertTrue(task.configuration.android);

        task.setAndroid(false);
        assertFalse(task.configuration.android);
    }

    @Test
    void testSetAndroidTrueWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setAndroid(true);

        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.android);
    }

    @Test
    void testSetAndroidFalseWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setAndroid(false);

        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.android);
    }

    @Test
    void testSetAndroidTrueWithObfuscationDisabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(false);
        task.setAndroid(true);

        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.android);
    }

    @Test
    void testSetAndroidBeforeAndAfterOtherSettings() {
        ProGuardTask task = new ProGuardTask();

        task.setAndroid(true);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertTrue(task.configuration.android);
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testConfigurationReferenceNotNull() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task.configuration);
        task.setAndroid(true);
        assertNotNull(task.configuration);
    }

    @Test
    void testSetAndroidTrueWithAllProcessingOptionsEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setAndroid(true);

        assertTrue(task.configuration.shrink);
        assertTrue(task.configuration.optimize);
        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.android);
    }

    @Test
    void testSetAndroidTrueWithAllProcessingOptionsDisabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setOptimize(false);
        task.setObfuscate(false);
        task.setAndroid(true);

        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.android);
    }

    @Test
    void testSetAndroidTrueOverridesDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is false
        assertFalse(task.configuration.android);

        // Override to true
        task.setAndroid(true);
        assertTrue(task.configuration.android);
    }

    @Test
    void testSetAndroidFalseKeepsDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is false
        assertFalse(task.configuration.android);

        // Set to false explicitly
        task.setAndroid(false);
        assertFalse(task.configuration.android);
    }

    @Test
    void testSetAndroidWithPreverify() {
        ProGuardTask task = new ProGuardTask();
        task.setPreverify(true);
        task.setAndroid(true);

        assertTrue(task.configuration.preverify);
        assertTrue(task.configuration.android);
    }

    @Test
    void testSetAndroidWithMicroedition() {
        ProGuardTask task = new ProGuardTask();
        task.setMicroedition(true);
        task.setAndroid(true);

        assertTrue(task.configuration.microEdition);
        assertTrue(task.configuration.android);
    }

    @Test
    void testSetAndroidIndependentFromPreverify() {
        ProGuardTask task = new ProGuardTask();

        task.setPreverify(false);
        task.setAndroid(true);

        assertFalse(task.configuration.preverify);
        assertTrue(task.configuration.android);

        task.setPreverify(true);
        assertTrue(task.configuration.android);
    }

    @Test
    void testSetAndroidIndependentFromMicroedition() {
        ProGuardTask task = new ProGuardTask();

        task.setMicroedition(true);
        task.setAndroid(true);

        assertTrue(task.configuration.microEdition);
        assertTrue(task.configuration.android);

        task.setMicroedition(false);
        assertTrue(task.configuration.android);
    }

    @Test
    void testMutualExclusivityNotEnforcedBetweenAndroidAndMicroedition() {
        ProGuardTask task = new ProGuardTask();

        // Both can be set to true (no mutual exclusivity enforcement at setter level)
        task.setAndroid(true);
        task.setMicroedition(true);

        assertTrue(task.configuration.android);
        assertTrue(task.configuration.microEdition);
    }
}
