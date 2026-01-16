package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask constructor.
 * Tests that the default constructor properly initializes the ProGuardTask instance.
 */
public class ProGuardTaskClaude_constructorTest {

    /**
     * Test that the default constructor successfully creates a ProGuardTask instance.
     */
    @Test
    public void testConstructorCreatesInstance() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task, "ProGuardTask should be created successfully");
    }

    /**
     * Test that the constructor initializes the inherited Configuration object.
     * The configuration field is inherited from ConfigurationTask and should be initialized
     * by the parent constructor.
     */
    @Test
    public void testConstructorInitializesConfiguration() {
        ProGuardTask task = new ProGuardTask();
        assertNotNull(task.configuration, "Configuration should be initialized by constructor");
    }

    /**
     * Test that multiple instances created by the constructor are independent.
     */
    @Test
    public void testConstructorCreatesIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();

        assertNotNull(task1, "First task should be created");
        assertNotNull(task2, "Second task should be created");
        assertNotSame(task1, task2, "Tasks should be different instances");
        assertNotSame(task1.configuration, task2.configuration, "Configurations should be different instances");
    }

    /**
     * Test that the constructor creates a task that can be assigned to a project.
     * This verifies that the constructor properly sets up the task to work within
     * the Ant framework.
     */
    @Test
    public void testConstructorCreatesTaskCompatibleWithAntProject() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();

        assertDoesNotThrow(() -> task.setProject(project),
            "Task should be compatible with Ant Project");
    }

    /**
     * Test that the constructor creates a task with a fresh Configuration state.
     * The Configuration should start with default/null values for most fields.
     */
    @Test
    public void testConstructorInitializesEmptyConfiguration() {
        ProGuardTask task = new ProGuardTask();

        assertNotNull(task.configuration, "Configuration should exist");
        assertNull(task.configuration.programJars, "programJars should start as null");
        assertNull(task.configuration.libraryJars, "libraryJars should start as null");
        assertNull(task.configuration.keep, "keep should start as null");
        assertNull(task.configuration.whyAreYouKeeping, "whyAreYouKeeping should start as null");
        assertNull(task.configuration.optimizations, "optimizations should start as null");
    }

    /**
     * Test that the constructor creates a task that inherits from ConfigurationTask.
     * This verifies the inheritance chain is properly set up.
     */
    @Test
    public void testConstructorCreatesTaskThatExtendsConfigurationTask() {
        ProGuardTask task = new ProGuardTask();

        assertTrue(task instanceof ConfigurationTask,
            "ProGuardTask should extend ConfigurationTask");
    }

    /**
     * Test that the constructor creates a task that inherits from Ant's Task.
     * This verifies the task is properly integrated into the Ant framework.
     */
    @Test
    public void testConstructorCreatesTaskThatExtendsAntTask() {
        ProGuardTask task = new ProGuardTask();

        assertTrue(task instanceof org.apache.tools.ant.Task,
            "ProGuardTask should extend Ant Task");
    }

    /**
     * Test that configuration object created by constructor has expected default boolean values.
     */
    @Test
    public void testConstructorInitializesConfigurationWithDefaultBooleanValues() {
        ProGuardTask task = new ProGuardTask();

        // Check default boolean values in Configuration
        assertTrue(task.configuration.shrink, "shrink should default to true");
        assertTrue(task.configuration.optimize, "optimize should default to true");
        assertTrue(task.configuration.obfuscate, "obfuscate should default to true");
        assertTrue(task.configuration.preverify, "preverify should default to true");
    }

    /**
     * Test that configuration object created by constructor has expected default numeric values.
     */
    @Test
    public void testConstructorInitializesConfigurationWithDefaultNumericValues() {
        ProGuardTask task = new ProGuardTask();

        assertEquals(1, task.configuration.optimizationPasses,
            "optimizationPasses should default to 1");
        assertEquals(0L, task.configuration.lastModified,
            "lastModified should default to 0");
    }

    /**
     * Test that the constructor allows the task to be used for configuration immediately.
     * This tests that all necessary initialization is complete after constructor.
     */
    @Test
    public void testConstructorAllowsImmediateConfiguration() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Should be able to call configuration methods immediately
        assertDoesNotThrow(() -> task.setShrink(false),
            "Task should support configuration immediately after construction");
        assertFalse(task.configuration.shrink, "Configuration change should take effect");
    }
}
