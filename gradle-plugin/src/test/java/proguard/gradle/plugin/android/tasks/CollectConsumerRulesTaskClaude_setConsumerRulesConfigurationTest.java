/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android.tasks;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CollectConsumerRulesTask.setConsumerRulesConfiguration()
 *
 * Tests: proguard.gradle.plugin.android.tasks.CollectConsumerRulesTask.setConsumerRulesConfiguration.(Lorg/gradle/api/artifacts/Configuration;)V
 *
 * The setConsumerRulesConfiguration() method sets the Configuration object for consumer rules.
 * This is a setter method for the 'lateinit var consumerRulesConfiguration' property in Kotlin.
 * The method completely replaces the current configuration with the provided configuration.
 * The Configuration represents the consumer rules configuration used during task execution.
 * The property is marked with @InputFiles annotation, making it a task input.
 */
public class CollectConsumerRulesTaskClaude_setConsumerRulesConfigurationTest {

    @TempDir
    Path tempDir;

    private Project project;
    private CollectConsumerRulesTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        task = project.getTasks().create("testCollectConsumerRules", CollectConsumerRulesTask.class);
    }

    // ==================== Basic Setter Tests ====================

    /**
     * Test that setConsumerRulesConfiguration sets a new configuration.
     * The most basic use case - setting a configuration.
     */
    @Test
    public void testSetConsumerRulesConfiguration_setsNewConfiguration() {
        // Given: A configuration
        Configuration config = project.getConfigurations().create("testConfig");

        // When: Setting the consumer rules configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Should have the new configuration
        assertNotNull(task.getConsumerRulesConfiguration(),
            "setConsumerRulesConfiguration() should set the configuration");
        assertSame(config, task.getConsumerRulesConfiguration(),
            "Should be the exact configuration that was set");
    }

    /**
     * Test that setConsumerRulesConfiguration replaces existing configuration.
     * Setting a new configuration should completely replace the old one.
     */
    @Test
    public void testSetConsumerRulesConfiguration_replacesExistingConfiguration() {
        // Given: A task with existing configuration
        Configuration oldConfig = project.getConfigurations().create("oldConfig");
        task.setConsumerRulesConfiguration(oldConfig);

        // When: Setting new configuration
        Configuration newConfig = project.getConfigurations().create("newConfig");
        task.setConsumerRulesConfiguration(newConfig);

        // Then: Old configuration should be replaced
        assertSame(newConfig, task.getConsumerRulesConfiguration(),
            "setConsumerRulesConfiguration() should replace old configuration");
        assertNotSame(oldConfig, task.getConsumerRulesConfiguration(),
            "Should not be the old configuration");
    }

    /**
     * Test that setConsumerRulesConfiguration stores the reference to the configuration.
     * The actual configuration reference should be stored, not a copy.
     */
    @Test
    public void testSetConsumerRulesConfiguration_storesReference() {
        // Given: A configuration
        Configuration config = project.getConfigurations().create("testConfig");

        // When: Setting the consumer rules configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Should store the actual reference
        assertSame(config, task.getConsumerRulesConfiguration(),
            "setConsumerRulesConfiguration() should store the actual configuration reference");
    }

    /**
     * Test that setConsumerRulesConfiguration can be called multiple times.
     * The setter should handle multiple invocations correctly.
     */
    @Test
    public void testSetConsumerRulesConfiguration_canBeCalledMultipleTimes() {
        // Given: Multiple configurations
        Configuration config1 = project.getConfigurations().create("config1");
        Configuration config2 = project.getConfigurations().create("config2");
        Configuration config3 = project.getConfigurations().create("config3");

        // When: Setting configurations multiple times
        task.setConsumerRulesConfiguration(config1);
        task.setConsumerRulesConfiguration(config2);
        task.setConsumerRulesConfiguration(config3);

        // Then: Should have the last configuration
        assertSame(config3, task.getConsumerRulesConfiguration(),
            "Should have the most recently set configuration");
    }

    /**
     * Test that setConsumerRulesConfiguration is reflected by getter immediately.
     * Changes should be visible immediately after setting.
     */
    @Test
    public void testSetConsumerRulesConfiguration_reflectedByGetterImmediately() {
        // Given: A configuration
        Configuration config = project.getConfigurations().create("testConfig");

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Getter should immediately return the new configuration
        Configuration retrieved = task.getConsumerRulesConfiguration();
        assertSame(config, retrieved,
            "Getter should immediately reflect the set configuration");
    }

    // ==================== Configuration Properties Tests ====================

    /**
     * Test that setConsumerRulesConfiguration preserves configuration name.
     * The configuration's name should remain unchanged.
     */
    @Test
    public void testSetConsumerRulesConfiguration_preservesConfigurationName() {
        // Given: A configuration with a specific name
        Configuration config = project.getConfigurations().create("consumerRules");

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Name should be preserved
        assertEquals("consumerRules", task.getConsumerRulesConfiguration().getName(),
            "Configuration name should be preserved");
    }

    /**
     * Test that setConsumerRulesConfiguration preserves configuration properties.
     * All configuration properties should remain intact.
     */
    @Test
    public void testSetConsumerRulesConfiguration_preservesConfigurationProperties() {
        // Given: A configuration with specific properties
        Configuration config = project.getConfigurations().create("testConfig");
        config.setDescription("Test configuration for consumer rules");
        config.setCanBeResolved(true);
        config.setVisible(true);

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Properties should be preserved
        Configuration retrieved = task.getConsumerRulesConfiguration();
        assertEquals("Test configuration for consumer rules", retrieved.getDescription(),
            "Description should be preserved");
        assertTrue(retrieved.isCanBeResolved(),
            "CanBeResolved should be preserved");
        assertTrue(retrieved.isVisible(),
            "Visibility should be preserved");
    }

    /**
     * Test that setConsumerRulesConfiguration works with resolvable configuration.
     * Resolvable configurations should work correctly.
     */
    @Test
    public void testSetConsumerRulesConfiguration_withResolvableConfiguration() {
        // Given: A resolvable configuration
        Configuration config = project.getConfigurations().create("resolvableConfig");
        config.setCanBeResolved(true);

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Should be resolvable
        assertTrue(task.getConsumerRulesConfiguration().isCanBeResolved(),
            "Resolvable configuration should remain resolvable");
    }

    /**
     * Test that setConsumerRulesConfiguration works with non-resolvable configuration.
     * Non-resolvable configurations should also work.
     */
    @Test
    public void testSetConsumerRulesConfiguration_withNonResolvableConfiguration() {
        // Given: A non-resolvable configuration
        Configuration config = project.getConfigurations().create("nonResolvableConfig");
        config.setCanBeResolved(false);

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Should remain non-resolvable
        assertFalse(task.getConsumerRulesConfiguration().isCanBeResolved(),
            "Non-resolvable configuration should remain non-resolvable");
    }

    // ==================== Configuration with Dependencies ====================

    /**
     * Test that setConsumerRulesConfiguration works with configuration containing dependencies.
     * Dependencies should be preserved.
     */
    @Test
    public void testSetConsumerRulesConfiguration_withDependencies() {
        // Given: A configuration with dependencies
        Configuration config = project.getConfigurations().create("configWithDeps");
        config.setCanBeResolved(true);
        project.getDependencies().add(config.getName(), "junit:junit:4.13.2");

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Dependencies should be preserved
        Configuration retrieved = task.getConsumerRulesConfiguration();
        assertFalse(retrieved.getDependencies().isEmpty(),
            "Dependencies should be preserved");
        assertEquals(1, retrieved.getDependencies().size(),
            "Should have one dependency");
    }

    /**
     * Test that setConsumerRulesConfiguration works with empty configuration.
     * Empty configurations (no dependencies) should work fine.
     */
    @Test
    public void testSetConsumerRulesConfiguration_withEmptyConfiguration() {
        // Given: An empty configuration
        Configuration config = project.getConfigurations().create("emptyConfig");

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Should be empty
        assertTrue(task.getConsumerRulesConfiguration().getDependencies().isEmpty(),
            "Empty configuration should remain empty");
    }

    /**
     * Test that setConsumerRulesConfiguration works with multiple dependencies.
     * Configurations with multiple dependencies should work correctly.
     */
    @Test
    public void testSetConsumerRulesConfiguration_withMultipleDependencies() {
        // Given: A configuration with multiple dependencies
        Configuration config = project.getConfigurations().create("multiDepsConfig");
        config.setCanBeResolved(true);
        project.getDependencies().add(config.getName(), "junit:junit:4.13.2");
        project.getDependencies().add(config.getName(), "org.mockito:mockito-core:4.11.0");

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: All dependencies should be preserved
        assertEquals(2, task.getConsumerRulesConfiguration().getDependencies().size(),
            "Should preserve all dependencies");
    }

    // ==================== Configuration Hierarchy Tests ====================

    /**
     * Test that setConsumerRulesConfiguration works with configuration hierarchy.
     * Child configurations extending parent configurations should work.
     */
    @Test
    public void testSetConsumerRulesConfiguration_withConfigurationHierarchy() {
        // Given: A configuration extending another
        Configuration parentConfig = project.getConfigurations().create("parent");
        Configuration childConfig = project.getConfigurations().create("child");
        childConfig.extendsFrom(parentConfig);

        // When: Setting the child configuration
        task.setConsumerRulesConfiguration(childConfig);

        // Then: Hierarchy should be preserved
        Configuration retrieved = task.getConsumerRulesConfiguration();
        assertEquals("child", retrieved.getName(),
            "Should be the child configuration");
        assertTrue(retrieved.getExtendsFrom().contains(parentConfig),
            "Should preserve hierarchy relationship");
    }

    /**
     * Test that setConsumerRulesConfiguration works with parent configuration.
     * Parent configurations should also work.
     */
    @Test
    public void testSetConsumerRulesConfiguration_withParentConfiguration() {
        // Given: A parent configuration
        Configuration parentConfig = project.getConfigurations().create("parent");
        Configuration childConfig = project.getConfigurations().create("child");
        childConfig.extendsFrom(parentConfig);

        // When: Setting the parent configuration
        task.setConsumerRulesConfiguration(parentConfig);

        // Then: Should be the parent
        assertEquals("parent", task.getConsumerRulesConfiguration().getName(),
            "Should be the parent configuration");
    }

    // ==================== Task Integration Tests ====================

    /**
     * Test that setConsumerRulesConfiguration integrates with other task properties.
     * Setting configuration should work alongside other property setters.
     */
    @Test
    public void testSetConsumerRulesConfiguration_integratesWithOtherProperties() {
        // Given: A configuration and other task properties
        Configuration config = project.getConfigurations().create("testConfig");
        task.setOutputFile(tempDir.resolve("output.txt").toFile());
        task.setConsumerRuleFilter(java.util.Collections.emptyList());

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: All properties should be set correctly
        assertSame(config, task.getConsumerRulesConfiguration(),
            "Configuration should be set");
        assertNotNull(task.getOutputFile(),
            "Output file should still be set");
        assertNotNull(task.getConsumerRuleFilter(),
            "Consumer rule filter should still be set");
    }

    /**
     * Test that setConsumerRulesConfiguration can be called before other properties.
     * Order of property setting should not matter.
     */
    @Test
    public void testSetConsumerRulesConfiguration_calledBeforeOtherProperties() {
        // Given: A configuration
        Configuration config = project.getConfigurations().create("testConfig");

        // When: Setting configuration first, then other properties
        task.setConsumerRulesConfiguration(config);
        task.setOutputFile(tempDir.resolve("output.txt").toFile());
        task.setConsumerRuleFilter(java.util.Collections.emptyList());

        // Then: All should be set correctly
        assertSame(config, task.getConsumerRulesConfiguration(),
            "Configuration should be set");
    }

    /**
     * Test that setConsumerRulesConfiguration can be called after other properties.
     * Order of property setting should not matter.
     */
    @Test
    public void testSetConsumerRulesConfiguration_calledAfterOtherProperties() {
        // Given: Other properties are set first
        task.setOutputFile(tempDir.resolve("output.txt").toFile());
        task.setConsumerRuleFilter(java.util.Collections.emptyList());

        // When: Setting configuration after
        Configuration config = project.getConfigurations().create("testConfig");
        task.setConsumerRulesConfiguration(config);

        // Then: Configuration should be set
        assertSame(config, task.getConsumerRulesConfiguration(),
            "Configuration should be set");
    }

    // ==================== Configuration State Tests ====================

    /**
     * Test that setConsumerRulesConfiguration preserves configuration visibility state.
     * Visible/invisible state should be maintained.
     */
    @Test
    public void testSetConsumerRulesConfiguration_preservesVisibilityState() {
        // Given: A visible configuration
        Configuration visibleConfig = project.getConfigurations().create("visible");
        visibleConfig.setVisible(true);

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(visibleConfig);

        // Then: Should remain visible
        assertTrue(task.getConsumerRulesConfiguration().isVisible(),
            "Visibility state should be preserved");
    }

    /**
     * Test that setConsumerRulesConfiguration preserves transitive state.
     * Transitive setting should be maintained.
     */
    @Test
    public void testSetConsumerRulesConfiguration_preservesTransitiveState() {
        // Given: A transitive configuration
        Configuration config = project.getConfigurations().create("transitive");
        config.setTransitive(true);

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Should remain transitive
        assertTrue(task.getConsumerRulesConfiguration().isTransitive(),
            "Transitive state should be preserved");
    }

    /**
     * Test that setConsumerRulesConfiguration preserves description.
     * Configuration description should be maintained.
     */
    @Test
    public void testSetConsumerRulesConfiguration_preservesDescription() {
        // Given: A configuration with description
        Configuration config = project.getConfigurations().create("described");
        config.setDescription("Consumer ProGuard rules for Android library");

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Description should be preserved
        assertEquals("Consumer ProGuard rules for Android library",
            task.getConsumerRulesConfiguration().getDescription(),
            "Description should be preserved");
    }

    // ==================== Realistic Scenario Tests ====================

    /**
     * Test setConsumerRulesConfiguration in Android library scenario.
     * Typical Android library use case.
     */
    @Test
    public void testSetConsumerRulesConfiguration_androidLibraryScenario() {
        // Given: A configuration for Android library consumer rules
        Configuration config = project.getConfigurations().create("consumerProguardRules");
        config.setCanBeResolved(true);
        config.setDescription("Consumer ProGuard rules configuration");

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Should be properly configured
        assertEquals("consumerProguardRules",
            task.getConsumerRulesConfiguration().getName(),
            "Should have correct name");
        assertTrue(task.getConsumerRulesConfiguration().isCanBeResolved(),
            "Should be resolvable");
    }

    /**
     * Test setConsumerRulesConfiguration for collecting rules from dependencies.
     * Main use case of the task.
     */
    @Test
    public void testSetConsumerRulesConfiguration_collectingFromDependencies() {
        // Given: A runtime classpath configuration
        Configuration config = project.getConfigurations().create("releaseRuntimeClasspath");
        config.setCanBeResolved(true);

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Should be ready for rule collection
        assertTrue(task.getConsumerRulesConfiguration().isCanBeResolved(),
            "Configuration should be resolvable for collecting consumer rules");
    }

    /**
     * Test setConsumerRulesConfiguration with custom configuration name.
     * Users might use custom naming.
     */
    @Test
    public void testSetConsumerRulesConfiguration_customConfigurationName() {
        // Given: A configuration with custom name
        Configuration config = project.getConfigurations().create("myCustomConsumerRules");

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Custom name should be preserved
        assertEquals("myCustomConsumerRules",
            task.getConsumerRulesConfiguration().getName(),
            "Custom configuration name should be preserved");
    }

    /**
     * Test setConsumerRulesConfiguration for debug variant.
     * Debug build variant scenario.
     */
    @Test
    public void testSetConsumerRulesConfiguration_debugVariant() {
        // Given: A debug variant configuration
        Configuration config = project.getConfigurations().create("debugRuntimeClasspath");
        config.setCanBeResolved(true);

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Should work for debug variant
        assertEquals("debugRuntimeClasspath",
            task.getConsumerRulesConfiguration().getName(),
            "Should support debug variant configuration");
    }

    /**
     * Test setConsumerRulesConfiguration for release variant.
     * Release build variant scenario.
     */
    @Test
    public void testSetConsumerRulesConfiguration_releaseVariant() {
        // Given: A release variant configuration
        Configuration config = project.getConfigurations().create("releaseRuntimeClasspath");
        config.setCanBeResolved(true);

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Should work for release variant
        assertEquals("releaseRuntimeClasspath",
            task.getConsumerRulesConfiguration().getName(),
            "Should support release variant configuration");
    }

    // ==================== Consistency Tests ====================

    /**
     * Test that setConsumerRulesConfiguration maintains consistency across task lifecycle.
     * Configuration should remain consistent throughout task execution.
     */
    @Test
    public void testSetConsumerRulesConfiguration_maintainsConsistencyAcrossLifecycle() {
        // Given: A configuration
        Configuration config = project.getConfigurations().create("testConfig");

        // When: Setting and accessing multiple times
        task.setConsumerRulesConfiguration(config);
        Configuration first = task.getConsumerRulesConfiguration();
        Configuration second = task.getConsumerRulesConfiguration();

        // Then: Should be consistent
        assertSame(first, second,
            "Should maintain consistency across multiple accesses");
        assertSame(config, first,
            "Should be the original configuration");
    }

    /**
     * Test that setConsumerRulesConfiguration is idempotent with same configuration.
     * Setting the same configuration multiple times should work.
     */
    @Test
    public void testSetConsumerRulesConfiguration_idempotentWithSameConfiguration() {
        // Given: A configuration
        Configuration config = project.getConfigurations().create("testConfig");

        // When: Setting the same configuration multiple times
        task.setConsumerRulesConfiguration(config);
        task.setConsumerRulesConfiguration(config);
        task.setConsumerRulesConfiguration(config);

        // Then: Should still be the same configuration
        assertSame(config, task.getConsumerRulesConfiguration(),
            "Should remain the same configuration after multiple sets");
    }

    /**
     * Test that setConsumerRulesConfiguration works with configurations from same project.
     * All configurations should be from the same project.
     */
    @Test
    public void testSetConsumerRulesConfiguration_withConfigurationsFromSameProject() {
        // Given: Multiple configurations from same project
        Configuration config1 = project.getConfigurations().create("config1");
        Configuration config2 = project.getConfigurations().create("config2");

        // When: Setting different configurations
        task.setConsumerRulesConfiguration(config1);
        Configuration retrieved1 = task.getConsumerRulesConfiguration();

        task.setConsumerRulesConfiguration(config2);
        Configuration retrieved2 = task.getConsumerRulesConfiguration();

        // Then: Both should be from the same project
        assertTrue(project.getConfigurations().contains(retrieved1),
            "First configuration should be from project");
        assertTrue(project.getConfigurations().contains(retrieved2),
            "Second configuration should be from project");
    }

    /**
     * Test that setConsumerRulesConfiguration allows switching between configurations.
     * Should be able to switch back and forth between configurations.
     */
    @Test
    public void testSetConsumerRulesConfiguration_allowsSwitchingBetweenConfigurations() {
        // Given: Two configurations
        Configuration config1 = project.getConfigurations().create("config1");
        Configuration config2 = project.getConfigurations().create("config2");

        // When: Switching between configurations
        task.setConsumerRulesConfiguration(config1);
        assertEquals(config1, task.getConsumerRulesConfiguration(),
            "Should be config1");

        task.setConsumerRulesConfiguration(config2);
        assertEquals(config2, task.getConsumerRulesConfiguration(),
            "Should be config2");

        task.setConsumerRulesConfiguration(config1);
        assertEquals(config1, task.getConsumerRulesConfiguration(),
            "Should be back to config1");
    }

    // ==================== Configuration Attributes Tests ====================

    /**
     * Test that setConsumerRulesConfiguration preserves configuration attributes.
     * Custom attributes should be maintained.
     */
    @Test
    public void testSetConsumerRulesConfiguration_preservesConfigurationAttributes() {
        // Given: A configuration (attributes are preserved by reference)
        Configuration config = project.getConfigurations().create("testConfig");

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Should maintain all attributes
        assertNotNull(task.getConsumerRulesConfiguration().getAttributes(),
            "Configuration attributes should be accessible");
    }

    /**
     * Test that setConsumerRulesConfiguration works after task initialization.
     * Configuration can be set at any point after task creation.
     */
    @Test
    public void testSetConsumerRulesConfiguration_worksAfterTaskInitialization() {
        // Given: A task that has been initialized
        assertNotNull(task, "Task should be initialized");

        // When: Setting configuration after initialization
        Configuration config = project.getConfigurations().create("lateConfig");
        task.setConsumerRulesConfiguration(config);

        // Then: Should work correctly
        assertSame(config, task.getConsumerRulesConfiguration(),
            "Configuration should be set after initialization");
    }

    /**
     * Test that setConsumerRulesConfiguration works with task from different project.
     * Task should work with configurations from its own project.
     */
    @Test
    public void testSetConsumerRulesConfiguration_withTaskProject() {
        // Given: Task's project configuration
        Configuration config = project.getConfigurations().create("taskProjectConfig");

        // When: Setting the configuration
        task.setConsumerRulesConfiguration(config);

        // Then: Should use the task's project configuration
        assertEquals(project, task.getProject(),
            "Task should belong to the project");
        assertTrue(project.getConfigurations().contains(task.getConsumerRulesConfiguration()),
            "Configuration should be from task's project");
    }
}
