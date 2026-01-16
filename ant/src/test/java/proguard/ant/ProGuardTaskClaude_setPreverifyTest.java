package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.setPreverify method.
 *
 * This test class verifies the behavior of the setPreverify(boolean) method
 * which controls whether ProGuard preverifies the processed classes for Java Micro Edition
 * or for Java 6 or higher.
 *
 * The method directly sets configuration.preverify field.
 * Default value is true.
 *
 * No reflection is used as the Configuration fields are public.
 */
class ProGuardTaskClaude_setPreverifyTest {

    @Test
    void testSetPreverifyTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setPreverify(true);
        assertTrue(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setPreverify(false);
        assertFalse(task.configuration.preverify);
    }

    @Test
    void testDefaultValueIsTrue() {
        ProGuardTask task = new ProGuardTask();
        assertTrue(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyTrueThenFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setPreverify(true);
        assertTrue(task.configuration.preverify);
        task.setPreverify(false);
        assertFalse(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyFalseThenTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setPreverify(false);
        assertFalse(task.configuration.preverify);
        task.setPreverify(true);
        assertTrue(task.configuration.preverify);
    }

    @Test
    void testMultipleCallsWithTrue() {
        ProGuardTask task = new ProGuardTask();
        task.setPreverify(true);
        task.setPreverify(true);
        task.setPreverify(true);
        assertTrue(task.configuration.preverify);
    }

    @Test
    void testMultipleCallsWithFalse() {
        ProGuardTask task = new ProGuardTask();
        task.setPreverify(false);
        task.setPreverify(false);
        task.setPreverify(false);
        assertFalse(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        task.setProject(project);
        task.setPreverify(false);
        assertFalse(task.configuration.preverify);
    }

    @Test
    void testIndependenceBetweenInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setPreverify(true);
        task2.setPreverify(false);

        assertTrue(task1.configuration.preverify);
        assertFalse(task2.configuration.preverify);
    }

    @Test
    void testDoesNotAffectOtherConfigurationFields() {
        ProGuardTask task = new ProGuardTask();
        Configuration config = task.configuration;

        // Capture initial state of other fields
        boolean initialObfuscate = config.obfuscate;
        boolean initialShrink = config.shrink;
        boolean initialOptimize = config.optimize;

        task.setPreverify(false);

        // Verify other fields unchanged
        assertEquals(initialObfuscate, config.obfuscate);
        assertEquals(initialShrink, config.shrink);
        assertEquals(initialOptimize, config.optimize);
    }

    @Test
    void testPersistsAcrossOtherSetterCalls() {
        ProGuardTask task = new ProGuardTask();

        task.setPreverify(false);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertFalse(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyTrueMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setPreverify(true);
        assertTrue(task.configuration.preverify);

        task.setPreverify(true);
        assertTrue(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyFalseMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setPreverify(false);
        assertFalse(task.configuration.preverify);

        task.setPreverify(false);
        assertFalse(task.configuration.preverify);
    }

    @Test
    void testTogglePreverifyMultipleTimes() {
        ProGuardTask task = new ProGuardTask();

        task.setPreverify(true);
        assertTrue(task.configuration.preverify);

        task.setPreverify(false);
        assertFalse(task.configuration.preverify);

        task.setPreverify(true);
        assertTrue(task.configuration.preverify);

        task.setPreverify(false);
        assertFalse(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyFalseWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setPreverify(false);

        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyTrueWithObfuscationEnabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(true);
        task.setPreverify(true);

        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyFalseWithObfuscationDisabled() {
        ProGuardTask task = new ProGuardTask();
        task.setObfuscate(false);
        task.setPreverify(false);

        assertFalse(task.configuration.obfuscate);
        assertFalse(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyBeforeAndAfterOtherSettings() {
        ProGuardTask task = new ProGuardTask();

        task.setPreverify(false);
        task.setObfuscate(true);
        task.setShrink(false);
        task.setOptimize(false);

        assertFalse(task.configuration.preverify);
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testConfigurationReferenceNotNull() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task.configuration);
        task.setPreverify(false);
        assertNotNull(task.configuration);
    }

    @Test
    void testSetPreverifyFalseWithAllProcessingOptionsEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setPreverify(false);

        assertTrue(task.configuration.shrink);
        assertTrue(task.configuration.optimize);
        assertTrue(task.configuration.obfuscate);
        assertFalse(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyTrueWithAllProcessingOptionsEnabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(true);
        task.setOptimize(true);
        task.setObfuscate(true);
        task.setPreverify(true);

        assertTrue(task.configuration.shrink);
        assertTrue(task.configuration.optimize);
        assertTrue(task.configuration.obfuscate);
        assertTrue(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyFalseWithAllProcessingOptionsDisabled() {
        ProGuardTask task = new ProGuardTask();

        task.setShrink(false);
        task.setOptimize(false);
        task.setObfuscate(false);
        task.setPreverify(false);

        assertFalse(task.configuration.shrink);
        assertFalse(task.configuration.optimize);
        assertFalse(task.configuration.obfuscate);
        assertFalse(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyFalseOverridesDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is true
        assertTrue(task.configuration.preverify);

        // Override to false
        task.setPreverify(false);
        assertFalse(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyTrueKeepsDefault() {
        ProGuardTask task = new ProGuardTask();

        // Verify default is true
        assertTrue(task.configuration.preverify);

        // Set to true explicitly
        task.setPreverify(true);
        assertTrue(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyWithMicroedition() {
        ProGuardTask task = new ProGuardTask();
        task.setMicroedition(true);
        task.setPreverify(false);

        assertTrue(task.configuration.microEdition);
        assertFalse(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyWithAndroid() {
        ProGuardTask task = new ProGuardTask();
        task.setAndroid(true);
        task.setPreverify(false);

        assertTrue(task.configuration.android);
        assertFalse(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyIndependentFromMicroedition() {
        ProGuardTask task = new ProGuardTask();

        task.setMicroedition(true);
        task.setPreverify(false);

        assertTrue(task.configuration.microEdition);
        assertFalse(task.configuration.preverify);

        task.setMicroedition(false);
        assertFalse(task.configuration.preverify);
    }

    @Test
    void testSetPreverifyIndependentFromAndroid() {
        ProGuardTask task = new ProGuardTask();

        task.setAndroid(true);
        task.setPreverify(false);

        assertTrue(task.configuration.android);
        assertFalse(task.configuration.preverify);

        task.setAndroid(false);
        assertFalse(task.configuration.preverify);
    }
}
