/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android.dsl;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for proguard.gradle.plugin.android.dsl.ProGuardAndroidExtension.
 *
 * Tests the following methods:
 * - <init>(Lorg/gradle/api/Project;)V - Constructor that initializes the extension
 * - getConfigurations()Lorg/gradle/api/NamedDomainObjectContainer; - Returns the configurations container
 *
 * This class is used to configure ProGuard for Android builds. It provides a DSL
 * for defining variant-specific ProGuard configurations.
 */
public class ProGuardAndroidExtensionClaudeTest {

    private Project project;

    @BeforeEach
    public void setUp() {
        // Create a test project using Gradle's ProjectBuilder
        project = ProjectBuilder.builder().build();
    }

    // ==================== Tests for constructor ====================

    /**
     * Test that constructor successfully creates an instance with a valid project.
     * The constructor should initialize the configurations container.
     */
    @Test
    public void testConstructor_withValidProject_createsInstance() {
        // When: Creating a ProGuardAndroidExtension with a valid project
        ProGuardAndroidExtension extension = new ProGuardAndroidExtension(project);

        // Then: Should create a non-null instance
        assertNotNull(extension, "Constructor should create a non-null instance");
    }

    /**
     * Test that constructor initializes the configurations container.
     * The constructor should create a NamedDomainObjectContainer for VariantConfiguration.
     */
    @Test
    public void testConstructor_initializesConfigurations() {
        // When: Creating a ProGuardAndroidExtension
        ProGuardAndroidExtension extension = new ProGuardAndroidExtension(project);

        // Then: The configurations container should be initialized
        assertNotNull(extension.getConfigurations(),
            "Constructor should initialize configurations container");
    }

    /**
     * Test that constructor creates an empty configurations container.
     * Initially, the configurations container should have no elements.
     */
    @Test
    public void testConstructor_configurationsContainerIsEmpty() {
        // When: Creating a ProGuardAndroidExtension
        ProGuardAndroidExtension extension = new ProGuardAndroidExtension(project);

        // Then: The configurations container should be empty initially
        assertTrue(extension.getConfigurations().isEmpty(),
            "Configurations container should be empty initially");
    }

    /**
     * Test that constructor with null project throws exception.
     * The constructor should not accept null projects.
     */
    @Test
    public void testConstructor_withNullProject_throwsException() {
        // When/Then: Creating a ProGuardAndroidExtension with null project should throw
        assertThrows(Exception.class, () -> {
            new ProGuardAndroidExtension(null);
        }, "Constructor should throw exception for null project");
    }

    // ==================== Tests for getConfigurations() method ====================

    /**
     * Test that getConfigurations returns a non-null container.
     * The getter should always return a container, never null.
     */
    @Test
    public void testGetConfigurations_returnsNonNull() {
        // Given: A ProGuardAndroidExtension
        ProGuardAndroidExtension extension = new ProGuardAndroidExtension(project);

        // When: Getting the configurations
        NamedDomainObjectContainer<VariantConfiguration> configurations = extension.getConfigurations();

        // Then: Should return a non-null container
        assertNotNull(configurations, "getConfigurations() should not return null");
    }

    /**
     * Test that getConfigurations returns the same instance on multiple calls.
     * The configurations container should be a singleton within the extension.
     */
    @Test
    public void testGetConfigurations_returnsSameInstance() {
        // Given: A ProGuardAndroidExtension
        ProGuardAndroidExtension extension = new ProGuardAndroidExtension(project);

        // When: Getting the configurations multiple times
        NamedDomainObjectContainer<VariantConfiguration> configurations1 = extension.getConfigurations();
        NamedDomainObjectContainer<VariantConfiguration> configurations2 = extension.getConfigurations();

        // Then: Should return the same instance
        assertSame(configurations1, configurations2,
            "getConfigurations() should return the same instance");
    }

    /**
     * Test that getConfigurations returns a container that can be modified.
     * The container should support adding new VariantConfiguration instances.
     */
    @Test
    public void testGetConfigurations_containerIsModifiable() {
        // Given: A ProGuardAndroidExtension
        ProGuardAndroidExtension extension = new ProGuardAndroidExtension(project);

        // When: Adding a configuration to the container
        NamedDomainObjectContainer<VariantConfiguration> configurations = extension.getConfigurations();
        VariantConfiguration config = configurations.create("debug");

        // Then: The configuration should be added successfully
        assertNotNull(config, "Should be able to create a configuration");
        assertEquals("debug", config.getName(), "Configuration should have the correct name");
        assertEquals(1, configurations.size(), "Container should have one element");
        assertTrue(configurations.getNames().contains("debug"),
            "Container should contain the 'debug' configuration");
    }

    /**
     * Test that getConfigurations returns a container that supports multiple configurations.
     * The container should be able to hold multiple VariantConfiguration instances.
     */
    @Test
    public void testGetConfigurations_supportsMultipleConfigurations() {
        // Given: A ProGuardAndroidExtension
        ProGuardAndroidExtension extension = new ProGuardAndroidExtension(project);

        // When: Adding multiple configurations
        NamedDomainObjectContainer<VariantConfiguration> configurations = extension.getConfigurations();
        configurations.create("debug");
        configurations.create("release");
        configurations.create("staging");

        // Then: The container should hold all configurations
        assertEquals(3, configurations.size(), "Container should have three elements");
        assertTrue(configurations.getNames().contains("debug"), "Should contain 'debug'");
        assertTrue(configurations.getNames().contains("release"), "Should contain 'release'");
        assertTrue(configurations.getNames().contains("staging"), "Should contain 'staging'");
    }

    /**
     * Test that getConfigurations returns a container that allows retrieval by name.
     * The container should support named lookup of configurations.
     */
    @Test
    public void testGetConfigurations_supportsNamedLookup() {
        // Given: A ProGuardAndroidExtension with a configuration
        ProGuardAndroidExtension extension = new ProGuardAndroidExtension(project);
        NamedDomainObjectContainer<VariantConfiguration> configurations = extension.getConfigurations();
        configurations.create("debug");

        // When: Retrieving the configuration by name
        VariantConfiguration retrieved = configurations.getByName("debug");

        // Then: Should retrieve the correct configuration
        assertNotNull(retrieved, "Should be able to retrieve configuration by name");
        assertEquals("debug", retrieved.getName(), "Retrieved configuration should have correct name");
    }

    /**
     * Test that getConfigurations returns a container that rejects duplicate names.
     * The container should enforce unique names for configurations.
     */
    @Test
    public void testGetConfigurations_rejectsDuplicateNames() {
        // Given: A ProGuardAndroidExtension with a configuration
        ProGuardAndroidExtension extension = new ProGuardAndroidExtension(project);
        NamedDomainObjectContainer<VariantConfiguration> configurations = extension.getConfigurations();
        configurations.create("debug");

        // When/Then: Attempting to create a duplicate should throw
        assertThrows(Exception.class, () -> {
            configurations.create("debug");
        }, "Container should reject duplicate configuration names");
    }

    /**
     * Test that getConfigurations returns a NamedDomainObjectContainer instance.
     * The return type should be the correct Gradle container type.
     */
    @Test
    public void testGetConfigurations_returnsCorrectType() {
        // Given: A ProGuardAndroidExtension
        ProGuardAndroidExtension extension = new ProGuardAndroidExtension(project);

        // When: Getting the configurations
        Object configurations = extension.getConfigurations();

        // Then: Should be an instance of NamedDomainObjectContainer
        assertInstanceOf(NamedDomainObjectContainer.class, configurations,
            "getConfigurations() should return a NamedDomainObjectContainer");
    }

    /**
     * Test that configurations container elements are of correct type.
     * Created configurations should be instances of VariantConfiguration.
     */
    @Test
    public void testGetConfigurations_elementsAreCorrectType() {
        // Given: A ProGuardAndroidExtension
        ProGuardAndroidExtension extension = new ProGuardAndroidExtension(project);

        // When: Creating a configuration
        NamedDomainObjectContainer<VariantConfiguration> configurations = extension.getConfigurations();
        Object config = configurations.create("debug");

        // Then: The created element should be a VariantConfiguration
        assertInstanceOf(VariantConfiguration.class, config,
            "Container elements should be VariantConfiguration instances");
    }
}
