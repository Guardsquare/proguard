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
 * Test class for CollectConsumerRulesTask.getConsumerRulesConfiguration()
 *
 * Tests: proguard.gradle.plugin.android.tasks.CollectConsumerRulesTask.getConsumerRulesConfiguration.()Lorg/gradle/api/artifacts/Configuration;
 *
 * The getConsumerRulesConfiguration() method returns the Configuration object that was set.
 * This Configuration represents the consumer rules configuration used during the task execution.
 * The property is marked with @InputFiles annotation, making it a task input.
 */
public class CollectConsumerRulesTaskClaude_getConsumerRulesConfigurationTest {

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

    // ==================== Basic Getter Tests ====================

    /**
     * Test that getConsumerRulesConfiguration returns the configuration that was set.
     * After setting a configuration, the getter should return that same configuration.
     */
    @Test
    public void testGetConsumerRulesConfiguration_returnsSetConfiguration() {
        // Given: A configuration is set
        Configuration config = project.getConfigurations().create("testConfig");
        task.setConsumerRulesConfiguration(config);

        // When: Getting the consumer rules configuration
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should return the same configuration
        assertNotNull(result, "getConsumerRulesConfiguration() should not return null");
        assertSame(config, result, "Should return the exact configuration that was set");
    }

    /**
     * Test that getConsumerRulesConfiguration returns non-null after setting.
     * The getter should never return null once a configuration is set.
     */
    @Test
    public void testGetConsumerRulesConfiguration_notNull() {
        // Given: A configuration is set
        Configuration config = project.getConfigurations().create("testConfig");
        task.setConsumerRulesConfiguration(config);

        // When: Getting the consumer rules configuration
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should not be null
        assertNotNull(result, "getConsumerRulesConfiguration() should not return null");
    }

    /**
     * Test that getConsumerRulesConfiguration returns Configuration type.
     * The return type should be a Configuration instance.
     */
    @Test
    public void testGetConsumerRulesConfiguration_returnsConfigurationType() {
        // Given: A configuration is set
        Configuration config = project.getConfigurations().create("testConfig");
        task.setConsumerRulesConfiguration(config);

        // When: Getting the consumer rules configuration
        Object result = task.getConsumerRulesConfiguration();

        // Then: Should be a Configuration instance
        assertInstanceOf(Configuration.class, result,
            "getConsumerRulesConfiguration() should return Configuration type");
    }

    /**
     * Test that getConsumerRulesConfiguration returns the same instance on multiple calls.
     * Multiple calls should return the exact same Configuration object.
     */
    @Test
    public void testGetConsumerRulesConfiguration_returnsSameInstance() {
        // Given: A configuration is set
        Configuration config = project.getConfigurations().create("testConfig");
        task.setConsumerRulesConfiguration(config);

        // When: Getting the consumer rules configuration multiple times
        Configuration result1 = task.getConsumerRulesConfiguration();
        Configuration result2 = task.getConsumerRulesConfiguration();
        Configuration result3 = task.getConsumerRulesConfiguration();

        // Then: Should return same instance every time
        assertSame(result1, result2, "Multiple calls should return same instance");
        assertSame(result2, result3, "Multiple calls should return same instance");
        assertSame(config, result1, "Should be the original configuration");
    }

    // ==================== Configuration Properties Tests ====================

    /**
     * Test that the returned configuration has correct name.
     * The configuration should retain its name after being set and retrieved.
     */
    @Test
    public void testGetConsumerRulesConfiguration_configurationHasCorrectName() {
        // Given: A configuration with a specific name
        Configuration config = project.getConfigurations().create("consumerRules");
        task.setConsumerRulesConfiguration(config);

        // When: Getting the consumer rules configuration
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should have the correct name
        assertEquals("consumerRules", result.getName(),
            "Configuration should have correct name");
    }

    /**
     * Test that the returned configuration is resolvable.
     * The configuration should be usable for resolving dependencies.
     */
    @Test
    public void testGetConsumerRulesConfiguration_configurationIsResolvable() {
        // Given: A resolvable configuration
        Configuration config = project.getConfigurations().create("testConfig");
        config.setCanBeResolved(true);
        task.setConsumerRulesConfiguration(config);

        // When: Getting the consumer rules configuration
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should be resolvable
        assertTrue(result.isCanBeResolved(),
            "Configuration should be resolvable");
    }

    /**
     * Test that the returned configuration belongs to the same project.
     * The configuration should be from the same project as the task.
     */
    @Test
    public void testGetConsumerRulesConfiguration_belongsToProject() {
        // Given: A configuration from the project
        Configuration config = project.getConfigurations().create("testConfig");
        task.setConsumerRulesConfiguration(config);

        // When: Getting the consumer rules configuration
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should be from the same project
        assertTrue(project.getConfigurations().contains(result),
            "Configuration should belong to the project");
    }

    // ==================== Configuration Update Tests ====================

    /**
     * Test that getConsumerRulesConfiguration reflects updates when configuration is replaced.
     * Setting a new configuration should be reflected by the getter.
     */
    @Test
    public void testGetConsumerRulesConfiguration_afterReplacingConfiguration() {
        // Given: Initial configuration is set
        Configuration config1 = project.getConfigurations().create("config1");
        task.setConsumerRulesConfiguration(config1);

        // When: Replacing with a different configuration
        Configuration config2 = project.getConfigurations().create("config2");
        task.setConsumerRulesConfiguration(config2);
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should return the new configuration
        assertSame(config2, result, "Should return the updated configuration");
        assertNotSame(config1, result, "Should not return the old configuration");
    }

    /**
     * Test that getConsumerRulesConfiguration can be called multiple times after updates.
     * The getter should always return the most recently set configuration.
     */
    @Test
    public void testGetConsumerRulesConfiguration_multipleUpdates() {
        // Given: Multiple configuration updates
        Configuration config1 = project.getConfigurations().create("config1");
        Configuration config2 = project.getConfigurations().create("config2");
        Configuration config3 = project.getConfigurations().create("config3");

        // When: Setting configurations in sequence
        task.setConsumerRulesConfiguration(config1);
        assertEquals(config1, task.getConsumerRulesConfiguration(),
            "Should return first configuration");

        task.setConsumerRulesConfiguration(config2);
        assertEquals(config2, task.getConsumerRulesConfiguration(),
            "Should return second configuration");

        task.setConsumerRulesConfiguration(config3);
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should return the latest configuration
        assertSame(config3, result, "Should return the most recent configuration");
    }

    // ==================== Configuration with Dependencies ====================

    /**
     * Test that getConsumerRulesConfiguration works with configuration containing dependencies.
     * The configuration should work even when it has dependencies.
     */
    @Test
    public void testGetConsumerRulesConfiguration_withDependencies() {
        // Given: A configuration with dependencies
        Configuration config = project.getConfigurations().create("testConfig");
        config.setCanBeResolved(true);
        project.getDependencies().add(config.getName(), "junit:junit:4.13.2");
        task.setConsumerRulesConfiguration(config);

        // When: Getting the consumer rules configuration
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should return the configuration with dependencies
        assertNotNull(result, "Should return configuration");
        assertFalse(result.getDependencies().isEmpty(),
            "Configuration should have dependencies");
    }

    /**
     * Test that getConsumerRulesConfiguration preserves empty configuration.
     * An empty configuration should remain empty when retrieved.
     */
    @Test
    public void testGetConsumerRulesConfiguration_withEmptyConfiguration() {
        // Given: An empty configuration
        Configuration config = project.getConfigurations().create("emptyConfig");
        task.setConsumerRulesConfiguration(config);

        // When: Getting the consumer rules configuration
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should be empty
        assertTrue(result.getDependencies().isEmpty(),
            "Configuration should be empty");
    }

    // ==================== Multiple Configurations Tests ====================

    /**
     * Test that getConsumerRulesConfiguration distinguishes between different configurations.
     * Different configurations should be distinguishable.
     */
    @Test
    public void testGetConsumerRulesConfiguration_distinguishesDifferentConfigurations() {
        // Given: Two different configurations
        Configuration config1 = project.getConfigurations().create("config1");
        Configuration config2 = project.getConfigurations().create("config2");

        // When: Setting different configurations
        task.setConsumerRulesConfiguration(config1);
        Configuration result1 = task.getConsumerRulesConfiguration();

        task.setConsumerRulesConfiguration(config2);
        Configuration result2 = task.getConsumerRulesConfiguration();

        // Then: Should return different configurations
        assertNotSame(result1, result2, "Different configurations should not be the same");
        assertEquals("config1", result1.getName(), "First should be config1");
        assertEquals("config2", result2.getName(), "Second should be config2");
    }

    // ==================== Task Integration Tests ====================

    /**
     * Test that getConsumerRulesConfiguration works in context of task execution.
     * The configuration should be accessible for task operations.
     */
    @Test
    public void testGetConsumerRulesConfiguration_inTaskContext() {
        // Given: A properly configured task
        Configuration config = project.getConfigurations().create("consumerRules");
        config.setCanBeResolved(true);
        task.setConsumerRulesConfiguration(config);

        // When: Getting the configuration in task context
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should be ready for task execution
        assertNotNull(result, "Configuration should not be null");
        assertTrue(result.isCanBeResolved(),
            "Configuration should be resolvable for task execution");
    }

    /**
     * Test that getConsumerRulesConfiguration is idempotent.
     * Multiple calls should not change the state.
     */
    @Test
    public void testGetConsumerRulesConfiguration_isIdempotent() {
        // Given: A configuration is set
        Configuration config = project.getConfigurations().create("testConfig");
        task.setConsumerRulesConfiguration(config);

        // When: Calling getter multiple times
        Configuration result1 = task.getConsumerRulesConfiguration();
        Configuration result2 = task.getConsumerRulesConfiguration();
        Configuration result3 = task.getConsumerRulesConfiguration();

        // Then: All results should be identical
        assertSame(result1, result2, "First and second calls should return same instance");
        assertSame(result2, result3, "Second and third calls should return same instance");
    }

    /**
     * Test that getConsumerRulesConfiguration works with configuration hierarchy.
     * Configurations can extend from other configurations.
     */
    @Test
    public void testGetConsumerRulesConfiguration_withConfigurationHierarchy() {
        // Given: A configuration extending another
        Configuration parentConfig = project.getConfigurations().create("parent");
        Configuration childConfig = project.getConfigurations().create("child");
        childConfig.extendsFrom(parentConfig);
        task.setConsumerRulesConfiguration(childConfig);

        // When: Getting the consumer rules configuration
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should return the child configuration
        assertSame(childConfig, result, "Should return the child configuration");
        assertEquals("child", result.getName(), "Should have correct name");
    }

    // ==================== Configuration State Tests ====================

    /**
     * Test that getConsumerRulesConfiguration returns configuration with correct visibility.
     * The configuration's visibility should be preserved.
     */
    @Test
    public void testGetConsumerRulesConfiguration_preservesVisibility() {
        // Given: A configuration with specific visibility
        Configuration config = project.getConfigurations().create("testConfig");
        config.setVisible(true);
        task.setConsumerRulesConfiguration(config);

        // When: Getting the consumer rules configuration
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should preserve visibility
        assertTrue(result.isVisible(), "Configuration visibility should be preserved");
    }

    /**
     * Test that getConsumerRulesConfiguration returns configuration with correct transitive setting.
     * The configuration's transitive setting should be preserved.
     */
    @Test
    public void testGetConsumerRulesConfiguration_preservesTransitive() {
        // Given: A configuration with transitive enabled
        Configuration config = project.getConfigurations().create("testConfig");
        config.setTransitive(true);
        task.setConsumerRulesConfiguration(config);

        // When: Getting the consumer rules configuration
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should preserve transitive setting
        assertTrue(result.isTransitive(), "Configuration transitive setting should be preserved");
    }

    // ==================== Realistic Scenario Tests ====================

    /**
     * Test getConsumerRulesConfiguration in a typical Android library scenario.
     * Android libraries publish consumer ProGuard rules.
     */
    @Test
    public void testGetConsumerRulesConfiguration_androidLibraryScenario() {
        // Given: A consumer rules configuration for Android library
        Configuration config = project.getConfigurations().create("consumerProguardRules");
        config.setCanBeResolved(true);
        config.setDescription("Consumer ProGuard rules configuration");
        task.setConsumerRulesConfiguration(config);

        // When: Getting the configuration
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should be properly configured for Android library
        assertNotNull(result, "Configuration should not be null");
        assertEquals("consumerProguardRules", result.getName(),
            "Should have correct name");
        assertTrue(result.isCanBeResolved(),
            "Should be resolvable");
    }

    /**
     * Test getConsumerRulesConfiguration when collecting rules from dependencies.
     * This is the main use case for this task.
     */
    @Test
    public void testGetConsumerRulesConfiguration_collectingFromDependencies() {
        // Given: A configuration with library dependencies
        Configuration config = project.getConfigurations().create("releaseRuntimeClasspath");
        config.setCanBeResolved(true);
        task.setConsumerRulesConfiguration(config);

        // When: Getting the configuration for rule collection
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should be ready for collecting consumer rules
        assertNotNull(result, "Configuration should be available");
        assertTrue(result.isCanBeResolved(),
            "Configuration should be resolvable to get artifacts");
    }

    /**
     * Test getConsumerRulesConfiguration with custom configuration name.
     * Users might use custom configuration names.
     */
    @Test
    public void testGetConsumerRulesConfiguration_customConfigurationName() {
        // Given: A configuration with custom name
        Configuration config = project.getConfigurations().create("myCustomConsumerRules");
        task.setConsumerRulesConfiguration(config);

        // When: Getting the consumer rules configuration
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should preserve custom name
        assertEquals("myCustomConsumerRules", result.getName(),
            "Should preserve custom configuration name");
    }

    // ==================== Consistency Tests ====================

    /**
     * Test that getConsumerRulesConfiguration is consistent across task lifecycle.
     * The getter should return consistent results during task lifetime.
     */
    @Test
    public void testGetConsumerRulesConfiguration_consistentAcrossTaskLifecycle() {
        // Given: A configuration is set
        Configuration config = project.getConfigurations().create("testConfig");
        task.setConsumerRulesConfiguration(config);

        // When: Getting configuration at different points
        Configuration beforeAccess = task.getConsumerRulesConfiguration();
        // Simulate some task operations
        task.setConsumerRuleFilter(java.util.Collections.emptyList());
        Configuration afterAccess = task.getConsumerRulesConfiguration();

        // Then: Should be consistent
        assertSame(beforeAccess, afterAccess,
            "Configuration should remain the same across task lifecycle");
    }

    /**
     * Test that getConsumerRulesConfiguration returns configuration that can be queried.
     * The configuration should support typical queries.
     */
    @Test
    public void testGetConsumerRulesConfiguration_supportQueries() {
        // Given: A configuration with properties
        Configuration config = project.getConfigurations().create("testConfig");
        config.setCanBeResolved(true);
        config.setDescription("Test configuration for consumer rules");
        task.setConsumerRulesConfiguration(config);

        // When: Getting and querying the configuration
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should support queries
        assertNotNull(result.getName(), "Should have queryable name");
        assertNotNull(result.getDescription(), "Should have queryable description");
        assertEquals("testConfig", result.getName(), "Name query should work");
    }

    /**
     * Test that getConsumerRulesConfiguration works after task is fully configured.
     * All properties should work together.
     */
    @Test
    public void testGetConsumerRulesConfiguration_afterFullTaskConfiguration() {
        // Given: A fully configured task
        Configuration config = project.getConfigurations().create("consumerRules");
        task.setConsumerRulesConfiguration(config);
        task.setOutputFile(tempDir.resolve("output.txt").toFile());
        task.setConsumerRuleFilter(java.util.Arrays.asList(
            new ConsumerRuleFilterEntry("com.example", "library")
        ));

        // When: Getting the configuration
        Configuration result = task.getConsumerRulesConfiguration();

        // Then: Should work correctly with other task properties
        assertNotNull(result, "Configuration should be accessible");
        assertSame(config, result, "Should be the set configuration");
        assertNotNull(task.getOutputFile(), "Other properties should also work");
        assertFalse(task.getConsumerRuleFilter().isEmpty(), "Filter should be set");
    }
}
