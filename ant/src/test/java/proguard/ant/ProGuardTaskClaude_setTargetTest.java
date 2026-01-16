package proguard.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setTarget method.
 * Tests that the method properly sets the targetClassVersion field in the configuration
 * and validates the target string.
 */
public class ProGuardTaskClaude_setTargetTest {

    /**
     * Test that setTarget accepts Java 1.5 target.
     */
    @Test
    public void testSetTargetJava15() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setTarget("1.5"),
            "Should accept '1.5' as valid target");

        assertTrue(task.configuration.targetClassVersion > 0,
            "targetClassVersion should be set to a positive value for Java 1.5");
    }

    /**
     * Test that setTarget accepts Java 1.6 target.
     */
    @Test
    public void testSetTargetJava16() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setTarget("1.6"),
            "Should accept '1.6' as valid target");

        assertTrue(task.configuration.targetClassVersion > 0,
            "targetClassVersion should be set to a positive value for Java 1.6");
    }

    /**
     * Test that setTarget accepts Java 1.7 target.
     */
    @Test
    public void testSetTargetJava17() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setTarget("1.7"),
            "Should accept '1.7' as valid target");

        assertTrue(task.configuration.targetClassVersion > 0,
            "targetClassVersion should be set to a positive value for Java 1.7");
    }

    /**
     * Test that setTarget accepts Java 1.8 target.
     */
    @Test
    public void testSetTargetJava18() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setTarget("1.8"),
            "Should accept '1.8' as valid target");

        assertTrue(task.configuration.targetClassVersion > 0,
            "targetClassVersion should be set to a positive value for Java 1.8");
    }

    /**
     * Test that setTarget accepts Java 9 target (modern format).
     */
    @Test
    public void testSetTargetJava9() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setTarget("9"),
            "Should accept '9' as valid target");

        assertTrue(task.configuration.targetClassVersion > 0,
            "targetClassVersion should be set to a positive value for Java 9");
    }

    /**
     * Test that setTarget accepts Java 10 target.
     */
    @Test
    public void testSetTargetJava10() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setTarget("10"),
            "Should accept '10' as valid target");

        assertTrue(task.configuration.targetClassVersion > 0,
            "targetClassVersion should be set to a positive value for Java 10");
    }

    /**
     * Test that setTarget accepts Java 11 target.
     */
    @Test
    public void testSetTargetJava11() {
        ProGuardTask task = new ProGuardTask();

        assertDoesNotThrow(() -> task.setTarget("11"),
            "Should accept '11' as valid target");

        assertTrue(task.configuration.targetClassVersion > 0,
            "targetClassVersion should be set to a positive value for Java 11");
    }

    /**
     * Test that setTarget throws BuildException for invalid target.
     */
    @Test
    public void testSetTargetInvalidString() {
        ProGuardTask task = new ProGuardTask();

        BuildException exception = assertThrows(BuildException.class,
            () -> task.setTarget("invalid"),
            "Should throw BuildException for invalid target string");

        assertTrue(exception.getMessage().contains("Unsupported target"),
            "Exception message should mention 'Unsupported target'");
        assertTrue(exception.getMessage().contains("invalid"),
            "Exception message should include the invalid target value");
    }

    /**
     * Test that setTarget throws BuildException for empty string.
     */
    @Test
    public void testSetTargetEmptyString() {
        ProGuardTask task = new ProGuardTask();

        BuildException exception = assertThrows(BuildException.class,
            () -> task.setTarget(""),
            "Should throw BuildException for empty string");

        assertTrue(exception.getMessage().contains("Unsupported target"),
            "Exception message should mention 'Unsupported target'");
    }

    /**
     * Test that setTarget throws BuildException for null.
     */
    @Test
    public void testSetTargetNull() {
        ProGuardTask task = new ProGuardTask();

        assertThrows(Exception.class,
            () -> task.setTarget(null),
            "Should throw exception for null target");
    }

    /**
     * Test that setTarget throws BuildException for invalid version like 0.5.
     */
    @Test
    public void testSetTargetInvalidVersion() {
        ProGuardTask task = new ProGuardTask();

        BuildException exception = assertThrows(BuildException.class,
            () -> task.setTarget("0.5"),
            "Should throw BuildException for invalid version");

        assertTrue(exception.getMessage().contains("Unsupported target"),
            "Exception message should mention 'Unsupported target'");
    }

    /**
     * Test that setTarget can be called multiple times with different values.
     */
    @Test
    public void testSetTargetMultipleCalls() {
        ProGuardTask task = new ProGuardTask();

        task.setTarget("1.5");
        int version15 = task.configuration.targetClassVersion;

        task.setTarget("1.8");
        int version18 = task.configuration.targetClassVersion;

        assertNotEquals(version15, version18,
            "Different Java versions should result in different targetClassVersion values");
        assertTrue(version18 > version15,
            "Java 1.8 version number should be higher than Java 1.5");
    }

    /**
     * Test that setTarget works with Ant project context.
     */
    @Test
    public void testSetTargetWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        assertDoesNotThrow(() -> task.setTarget("1.8"),
            "Should work with Ant project context");

        assertTrue(task.configuration.targetClassVersion > 0,
            "targetClassVersion should be set with Ant project context");
    }

    /**
     * Test that setTarget on different task instances are independent.
     */
    @Test
    public void testSetTargetIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        task1.setTarget("1.5");
        task2.setTarget("1.8");

        assertNotEquals(task1.configuration.targetClassVersion,
            task2.configuration.targetClassVersion,
            "Different instances should have independent targetClassVersion values");
    }

    /**
     * Test that setting target doesn't affect other configuration fields.
     */
    @Test
    public void testSetTargetDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();

        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        task.setTarget("1.8");

        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
            "obfuscate field should not be affected");
    }

    /**
     * Test that targetClassVersion is 0 before setTarget is called.
     */
    @Test
    public void testTargetClassVersionDefaultIsZero() {
        ProGuardTask task = new ProGuardTask();

        assertEquals(0, task.configuration.targetClassVersion,
            "targetClassVersion should default to 0");
    }

    /**
     * Test that setTarget sets targetClassVersion to non-zero value.
     */
    @Test
    public void testSetTargetSetsNonZeroValue() {
        ProGuardTask task = new ProGuardTask();

        assertEquals(0, task.configuration.targetClassVersion,
            "targetClassVersion should start at 0");

        task.setTarget("1.8");

        assertNotEquals(0, task.configuration.targetClassVersion,
            "targetClassVersion should be non-zero after setTarget");
        assertTrue(task.configuration.targetClassVersion > 0,
            "targetClassVersion should be positive");
    }

    /**
     * Test that the target value persists across other setter calls.
     */
    @Test
    public void testSetTargetPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();

        task.setTarget("1.8");
        int targetVersion = task.configuration.targetClassVersion;

        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        assertEquals(targetVersion, task.configuration.targetClassVersion,
            "targetClassVersion should remain unchanged after other setters are called");
    }

    /**
     * Test that different Java 1.x versions result in different version numbers.
     */
    @Test
    public void testSetTargetDifferentJava1xVersions() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        ProGuardTask task3 = new ProGuardTask();

        task1.setTarget("1.5");
        task2.setTarget("1.6");
        task3.setTarget("1.7");

        int version15 = task1.configuration.targetClassVersion;
        int version16 = task2.configuration.targetClassVersion;
        int version17 = task3.configuration.targetClassVersion;

        assertTrue(version15 < version16 && version16 < version17,
            "Version numbers should increase: 1.5 < 1.6 < 1.7");
    }

    /**
     * Test that setTarget throws BuildException for random garbage input.
     */
    @Test
    public void testSetTargetGarbageInput() {
        ProGuardTask task = new ProGuardTask();

        BuildException exception = assertThrows(BuildException.class,
            () -> task.setTarget("xyz123"),
            "Should throw BuildException for garbage input");

        assertTrue(exception.getMessage().contains("Unsupported target"),
            "Exception message should mention 'Unsupported target'");
        assertTrue(exception.getMessage().contains("xyz123"),
            "Exception message should include the invalid input");
    }

    /**
     * Test that setTarget throws BuildException for negative version.
     */
    @Test
    public void testSetTargetNegativeVersion() {
        ProGuardTask task = new ProGuardTask();

        BuildException exception = assertThrows(BuildException.class,
            () -> task.setTarget("-1"),
            "Should throw BuildException for negative version");

        assertTrue(exception.getMessage().contains("Unsupported target"),
            "Exception message should mention 'Unsupported target'");
    }

    /**
     * Test that setTarget accepts Java 12 and higher versions.
     */
    @Test
    public void testSetTargetJava12AndHigher() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        assertDoesNotThrow(() -> task1.setTarget("12"),
            "Should accept '12' as valid target");
        assertDoesNotThrow(() -> task2.setTarget("17"),
            "Should accept '17' as valid target");

        assertTrue(task1.configuration.targetClassVersion > 0,
            "targetClassVersion should be set for Java 12");
        assertTrue(task2.configuration.targetClassVersion > 0,
            "targetClassVersion should be set for Java 17");
    }

    /**
     * Test that setTarget with whitespace throws exception.
     */
    @Test
    public void testSetTargetWithWhitespace() {
        ProGuardTask task = new ProGuardTask();

        BuildException exception = assertThrows(BuildException.class,
            () -> task.setTarget(" 1.8 "),
            "Should throw BuildException for target with whitespace");

        assertTrue(exception.getMessage().contains("Unsupported target"),
            "Exception message should mention 'Unsupported target'");
    }

    /**
     * Test that after an exception, targetClassVersion remains 0.
     */
    @Test
    public void testSetTargetExceptionLeavesVersionAtZero() {
        ProGuardTask task = new ProGuardTask();

        assertEquals(0, task.configuration.targetClassVersion,
            "targetClassVersion should start at 0");

        assertThrows(BuildException.class, () -> task.setTarget("invalid"));

        assertEquals(0, task.configuration.targetClassVersion,
            "targetClassVersion should remain 0 after exception");
    }

    /**
     * Test that setTarget can overwrite a previously set valid target.
     */
    @Test
    public void testSetTargetOverwritesPreviousValue() {
        ProGuardTask task = new ProGuardTask();

        task.setTarget("1.5");
        int firstVersion = task.configuration.targetClassVersion;
        assertTrue(firstVersion > 0, "First version should be set");

        task.setTarget("11");
        int secondVersion = task.configuration.targetClassVersion;
        assertTrue(secondVersion > 0, "Second version should be set");

        assertNotEquals(firstVersion, secondVersion,
            "Second setTarget should overwrite the first value");
    }
}
