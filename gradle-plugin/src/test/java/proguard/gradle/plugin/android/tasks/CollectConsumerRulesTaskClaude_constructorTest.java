/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android.tasks;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CollectConsumerRulesTask constructor.
 *
 * Tests: proguard.gradle.plugin.android.tasks.CollectConsumerRulesTask.<init>.()V
 *
 * The constructor is the default no-arg constructor which initializes:
 * - The task as a DefaultTask
 * - consumerRuleFilter to an empty list
 * - consumerRuleIgnoreFilter to a list containing the androidx.window entry
 */
public class CollectConsumerRulesTaskClaude_constructorTest {

    @TempDir
    Path tempDir;

    private Project project;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
    }

    // ==================== Tests for constructor ====================

    /**
     * Test that the constructor successfully creates an instance.
     * The constructor should create a non-null CollectConsumerRulesTask instance.
     */
    @Test
    public void testConstructor_createsInstance() {
        // When: Creating a CollectConsumerRulesTask instance
        CollectConsumerRulesTask task = project.getTasks().create("testTask", CollectConsumerRulesTask.class);

        // Then: Instance should be created successfully
        assertNotNull(task, "Constructor should create a non-null instance");
    }

    /**
     * Test that the constructor initializes the task as a Gradle task.
     * The task should have basic Gradle task properties like name.
     */
    @Test
    public void testConstructor_initializesAsGradleTask() {
        // When: Creating a CollectConsumerRulesTask instance with a specific name
        CollectConsumerRulesTask task = project.getTasks().create("myConsumerRulesTask", CollectConsumerRulesTask.class);

        // Then: The task should have the correct name
        assertNotNull(task, "Constructor should create a non-null instance");
        assertEquals("myConsumerRulesTask", task.getName(), "Task should have the assigned name");
    }

    /**
     * Test that the constructor initializes consumerRuleFilter to an empty list.
     * The default value for consumerRuleFilter should be an empty list.
     */
    @Test
    public void testConstructor_initializesConsumerRuleFilterToEmptyList() {
        // When: Creating a CollectConsumerRulesTask instance
        CollectConsumerRulesTask task = project.getTasks().create("testTask", CollectConsumerRulesTask.class);

        // Then: consumerRuleFilter should be initialized to an empty list
        assertNotNull(task, "Constructor should create a non-null instance");
        assertNotNull(task.getConsumerRuleFilter(), "consumerRuleFilter should not be null");
        assertTrue(task.getConsumerRuleFilter().isEmpty(),
            "consumerRuleFilter should be initialized to an empty list");
        assertEquals(0, task.getConsumerRuleFilter().size(),
            "consumerRuleFilter size should be 0");
    }

    /**
     * Test that the constructor initializes consumerRuleIgnoreFilter correctly.
     * The consumerRuleIgnoreFilter should contain the androidx.window entry by default.
     */
    @Test
    public void testConstructor_initializesConsumerRuleIgnoreFilter() {
        // When: Creating a CollectConsumerRulesTask instance
        CollectConsumerRulesTask task = project.getTasks().create("testTask", CollectConsumerRulesTask.class);

        // Then: consumerRuleIgnoreFilter should be initialized with the default entry
        assertNotNull(task, "Constructor should create a non-null instance");
        assertNotNull(task.getConsumerRuleIgnoreFilter(), "consumerRuleIgnoreFilter should not be null");
        assertEquals(1, task.getConsumerRuleIgnoreFilter().size(),
            "consumerRuleIgnoreFilter should have 1 default entry");
    }

    /**
     * Test that the constructor initializes consumerRuleIgnoreFilter with androidx.window entry.
     * The default entry should have group "androidx.window" and module "window".
     */
    @Test
    public void testConstructor_initializesConsumerRuleIgnoreFilterWithAndroidXWindowEntry() {
        // When: Creating a CollectConsumerRulesTask instance
        CollectConsumerRulesTask task = project.getTasks().create("testTask", CollectConsumerRulesTask.class);

        // Then: consumerRuleIgnoreFilter should contain the androidx.window entry
        assertNotNull(task, "Constructor should create a non-null instance");
        assertNotNull(task.getConsumerRuleIgnoreFilter(), "consumerRuleIgnoreFilter should not be null");
        assertFalse(task.getConsumerRuleIgnoreFilter().isEmpty(),
            "consumerRuleIgnoreFilter should not be empty");

        ConsumerRuleFilterEntry entry = task.getConsumerRuleIgnoreFilter().get(0);
        assertEquals("androidx.window", entry.getGroup(),
            "First entry should have group 'androidx.window'");
        assertEquals("window", entry.getModule(),
            "First entry should have module 'window'");
    }

    /**
     * Test that the task is properly registered in the project's task container.
     * The task should be accessible via the project's tasks.
     */
    @Test
    public void testConstructor_taskIsRegisteredInProject() {
        // When: Creating a CollectConsumerRulesTask instance
        CollectConsumerRulesTask task = project.getTasks().create("collectRules", CollectConsumerRulesTask.class);

        // Then: The task should be findable in the project's task container
        assertNotNull(task, "Constructor should create a non-null instance");
        assertNotNull(project.getTasks().findByName("collectRules"),
            "Task should be registered in project");
        assertEquals(task, project.getTasks().findByName("collectRules"),
            "Retrieved task should be the same instance");
    }

    /**
     * Test that multiple instances can be created independently.
     * Each instance should be distinct and independent.
     */
    @Test
    public void testConstructor_allowsMultipleInstances() {
        // When: Creating multiple CollectConsumerRulesTask instances
        CollectConsumerRulesTask task1 = project.getTasks().create("task1", CollectConsumerRulesTask.class);
        CollectConsumerRulesTask task2 = project.getTasks().create("task2", CollectConsumerRulesTask.class);

        // Then: Both instances should be created and be distinct
        assertNotNull(task1, "First instance should be created");
        assertNotNull(task2, "Second instance should be created");
        assertNotSame(task1, task2, "Instances should be distinct objects");
        assertNotEquals(task1.getName(), task2.getName(),
            "Tasks should have different names");
    }

    /**
     * Test that the task has the expected type hierarchy.
     * CollectConsumerRulesTask should be a DefaultTask.
     */
    @Test
    public void testConstructor_taskIsDefaultTask() {
        // When: Creating a CollectConsumerRulesTask instance
        CollectConsumerRulesTask task = project.getTasks().create("testTask", CollectConsumerRulesTask.class);

        // Then: The task should be an instance of DefaultTask
        assertNotNull(task, "Constructor should create a non-null instance");
        assertTrue(task instanceof org.gradle.api.DefaultTask,
            "CollectConsumerRulesTask should extend DefaultTask");
    }

    /**
     * Test that the task can be created without a project build file.
     * The constructor should work with a minimal project setup.
     */
    @Test
    public void testConstructor_worksWithMinimalProject() {
        // Given: A minimal project without build files
        Project minimalProject = ProjectBuilder.builder()
                .withProjectDir(tempDir.resolve("minimal").toFile())
                .build();

        // When: Creating a CollectConsumerRulesTask instance
        CollectConsumerRulesTask task = minimalProject.getTasks().create("minimal", CollectConsumerRulesTask.class);

        // Then: The task should be created successfully
        assertNotNull(task, "Constructor should work with minimal project setup");
        assertEquals("minimal", task.getName(), "Task should have correct name");
    }

    /**
     * Test that consumerRuleFilter can be modified after construction.
     * The consumerRuleFilter property should be mutable.
     */
    @Test
    public void testConstructor_consumerRuleFilterIsMutable() {
        // Given: A CollectConsumerRulesTask instance
        CollectConsumerRulesTask task = project.getTasks().create("testTask", CollectConsumerRulesTask.class);

        // When: Setting a new value for consumerRuleFilter
        java.util.List<ConsumerRuleFilterEntry> newFilter = java.util.Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library")
        );
        task.setConsumerRuleFilter(newFilter);

        // Then: The value should be updated
        assertNotNull(task.getConsumerRuleFilter(), "consumerRuleFilter should not be null");
        assertEquals(1, task.getConsumerRuleFilter().size(),
            "consumerRuleFilter should have 1 entry");
        assertEquals("com.example", task.getConsumerRuleFilter().get(0).getGroup(),
            "Filter entry should have correct group");
    }

    /**
     * Test that the task has a proper string representation.
     * The task should return a meaningful toString() result.
     */
    @Test
    public void testConstructor_taskHasProperStringRepresentation() {
        // When: Creating a CollectConsumerRulesTask instance
        CollectConsumerRulesTask task = project.getTasks().create("myTask", CollectConsumerRulesTask.class);

        // Then: The task should have a string representation containing its name
        assertNotNull(task, "Constructor should create a non-null instance");
        String taskString = task.toString();
        assertNotNull(taskString, "Task toString should not return null");
        assertTrue(taskString.contains("myTask") || taskString.contains("task"),
            "Task string representation should be meaningful");
    }

    /**
     * Test that the task belongs to the project that created it.
     * The task's project property should reference the creating project.
     */
    @Test
    public void testConstructor_taskBelongsToCreatingProject() {
        // When: Creating a CollectConsumerRulesTask instance
        CollectConsumerRulesTask task = project.getTasks().create("testTask", CollectConsumerRulesTask.class);

        // Then: The task should belong to the project
        assertNotNull(task, "Constructor should create a non-null instance");
        assertEquals(project, task.getProject(), "Task should belong to the creating project");
    }

    /**
     * Test that the task has a proper path in the project hierarchy.
     * The task path should include the project path and task name.
     */
    @Test
    public void testConstructor_taskHasProperPath() {
        // When: Creating a CollectConsumerRulesTask instance
        CollectConsumerRulesTask task = project.getTasks().create("testTask", CollectConsumerRulesTask.class);

        // Then: The task should have a proper path
        assertNotNull(task, "Constructor should create a non-null instance");
        String taskPath = task.getPath();
        assertNotNull(taskPath, "Task path should not be null");
        assertTrue(taskPath.contains("testTask"), "Task path should contain task name");
    }

    /**
     * Test that consumerRuleIgnoreFilter is immutable or at least initialized.
     * The consumerRuleIgnoreFilter should be set after construction.
     */
    @Test
    public void testConstructor_consumerRuleIgnoreFilterIsInitialized() {
        // When: Creating a CollectConsumerRulesTask instance
        CollectConsumerRulesTask task = project.getTasks().create("testTask", CollectConsumerRulesTask.class);

        // Then: consumerRuleIgnoreFilter should be initialized and accessible
        assertNotNull(task, "Constructor should create a non-null instance");
        assertNotNull(task.getConsumerRuleIgnoreFilter(),
            "consumerRuleIgnoreFilter should be initialized");
        assertFalse(task.getConsumerRuleIgnoreFilter().isEmpty(),
            "consumerRuleIgnoreFilter should not be empty after construction");
    }
}
