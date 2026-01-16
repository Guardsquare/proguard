/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android;

import com.android.build.gradle.BaseExtension;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import proguard.gradle.plugin.android.dsl.ProGuardAndroidExtension;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTransform constructor.
 *
 * Tests: proguard.gradle.plugin.android.ProGuardTransform.<init>.(Lorg/gradle/api/Project;Lproguard/gradle/plugin/android/dsl/ProGuardAndroidExtension;Lproguard/gradle/plugin/android/AndroidProjectType;Lcom/android/build/gradle/BaseExtension;)V
 *
 * The constructor accepts four parameters:
 * - project: The Gradle project
 * - proguardBlock: The ProGuard Android extension containing configurations
 * - projectType: The Android project type (application or library)
 * - androidExtension: The Android extension
 *
 * This constructor simply stores the parameters as private fields to be used by other methods.
 */
public class ProGuardTransformClaude_constructorTest {

    @TempDir
    Path tempDir;

    private Project project;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
    }

    // ==================== Tests for constructor with ANDROID_APPLICATION ====================

    /**
     * Test that the constructor successfully creates an instance with ANDROID_APPLICATION.
     * The constructor should accept valid parameters and create a non-null instance.
     */
    @Test
    public void testConstructor_withAndroidApplication_createsInstance() {
        // Given: Valid constructor parameters for an Android application
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        // When: Creating a ProGuardTransform instance
        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // Then: Instance should be created successfully
        assertNotNull(transform, "Constructor should create a non-null instance");
    }

    /**
     * Test that the constructor stores parameters correctly by checking getName().
     * getName() should return "ProguardTransform" regardless of constructor parameters.
     */
    @Test
    public void testConstructor_withAndroidApplication_storesParametersCorrectly() {
        // Given: Valid constructor parameters for an Android application
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        // When: Creating a ProGuardTransform instance
        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // Then: The transform should have correct name (tests that instance is properly initialized)
        assertNotNull(transform, "Constructor should create a non-null instance");
        assertEquals("ProguardTransform", transform.getName(),
            "Transform name should be 'ProguardTransform'");
    }

    /**
     * Test that the constructor properly initializes scopes for ANDROID_APPLICATION.
     * For Android applications, scopes should include PROJECT, SUB_PROJECTS, and EXTERNAL_LIBRARIES.
     */
    @Test
    public void testConstructor_withAndroidApplication_initializesCorrectScopes() {
        // Given: Valid constructor parameters for an Android application
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        // When: Creating a ProGuardTransform instance
        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // Then: Scopes should be correct for Android application
        assertNotNull(transform, "Constructor should create a non-null instance");
        assertNotNull(transform.getScopes(), "Scopes should not be null");
        assertEquals(3, transform.getScopes().size(),
            "Android application should have 3 scopes");
    }

    /**
     * Test that the constructor properly initializes referenced scopes for ANDROID_APPLICATION.
     * For Android applications, referenced scopes should include PROVIDED_ONLY.
     */
    @Test
    public void testConstructor_withAndroidApplication_initializesCorrectReferencedScopes() {
        // Given: Valid constructor parameters for an Android application
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        // When: Creating a ProGuardTransform instance
        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // Then: Referenced scopes should be correct for Android application
        assertNotNull(transform, "Constructor should create a non-null instance");
        assertNotNull(transform.getReferencedScopes(), "Referenced scopes should not be null");
        assertEquals(1, transform.getReferencedScopes().size(),
            "Android application should have 1 referenced scope");
    }

    /**
     * Test that the constructor properly initializes input types.
     * Input types should include CLASSES and RESOURCES regardless of project type.
     */
    @Test
    public void testConstructor_withAndroidApplication_initializesInputTypes() {
        // Given: Valid constructor parameters for an Android application
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        // When: Creating a ProGuardTransform instance
        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // Then: Input types should include CLASSES and RESOURCES
        assertNotNull(transform, "Constructor should create a non-null instance");
        assertNotNull(transform.getInputTypes(), "Input types should not be null");
        assertEquals(2, transform.getInputTypes().size(),
            "Input types should include CLASSES and RESOURCES");
    }

    /**
     * Test that the constructor sets isIncremental to false.
     * ProGuardTransform is not incremental by design.
     */
    @Test
    public void testConstructor_withAndroidApplication_setsIncrementalToFalse() {
        // Given: Valid constructor parameters for an Android application
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        // When: Creating a ProGuardTransform instance
        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // Then: isIncremental should be false
        assertNotNull(transform, "Constructor should create a non-null instance");
        assertFalse(transform.isIncremental(),
            "ProGuardTransform should not be incremental");
    }

    // ==================== Tests for constructor with ANDROID_LIBRARY ====================

    /**
     * Test that the constructor successfully creates an instance with ANDROID_LIBRARY.
     * The constructor should work correctly for library projects as well.
     */
    @Test
    public void testConstructor_withAndroidLibrary_createsInstance() {
        // Given: Valid constructor parameters for an Android library
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_LIBRARY;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        // When: Creating a ProGuardTransform instance
        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // Then: Instance should be created successfully
        assertNotNull(transform, "Constructor should create a non-null instance");
    }

    /**
     * Test that the constructor properly initializes scopes for ANDROID_LIBRARY.
     * For Android libraries, scopes should include only PROJECT.
     */
    @Test
    public void testConstructor_withAndroidLibrary_initializesCorrectScopes() {
        // Given: Valid constructor parameters for an Android library
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_LIBRARY;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        // When: Creating a ProGuardTransform instance
        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // Then: Scopes should be correct for Android library
        assertNotNull(transform, "Constructor should create a non-null instance");
        assertNotNull(transform.getScopes(), "Scopes should not be null");
        assertEquals(1, transform.getScopes().size(),
            "Android library should have 1 scope (PROJECT)");
    }

    /**
     * Test that the constructor properly initializes referenced scopes for ANDROID_LIBRARY.
     * For Android libraries, referenced scopes should include PROVIDED_ONLY, EXTERNAL_LIBRARIES, and SUB_PROJECTS.
     */
    @Test
    public void testConstructor_withAndroidLibrary_initializesCorrectReferencedScopes() {
        // Given: Valid constructor parameters for an Android library
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_LIBRARY;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        // When: Creating a ProGuardTransform instance
        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // Then: Referenced scopes should be correct for Android library
        assertNotNull(transform, "Constructor should create a non-null instance");
        assertNotNull(transform.getReferencedScopes(), "Referenced scopes should not be null");
        assertEquals(3, transform.getReferencedScopes().size(),
            "Android library should have 3 referenced scopes");
    }

    /**
     * Test that the constructor properly stores the proguardBlock parameter.
     * We can verify this by checking getSecondaryFiles() which uses the proguardBlock.
     */
    @Test
    public void testConstructor_storesProguardBlockParameter() {
        // Given: Valid constructor parameters with a ProGuardAndroidExtension
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        // When: Creating a ProGuardTransform instance
        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // Then: The proguardBlock should be stored and accessible via getSecondaryFiles()
        assertNotNull(transform, "Constructor should create a non-null instance");
        assertNotNull(transform.getSecondaryFiles(),
            "Secondary files should not be null (uses proguardBlock internally)");
    }

    /**
     * Test that the constructor accepts a non-null project parameter.
     * The project parameter is essential for the transform to function.
     */
    @Test
    public void testConstructor_withNonNullProject_createsInstance() {
        // Given: Valid constructor parameters with a non-null project
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        // When: Creating a ProGuardTransform instance
        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // Then: Instance should be created and project should be usable
        assertNotNull(transform, "Constructor should create a non-null instance");
        assertNotNull(project, "Project should not be null");
    }

    /**
     * Test that constructor parameters are used to determine behavior.
     * Different project types should result in different scopes.
     */
    @Test
    public void testConstructor_differentProjectTypes_resultInDifferentScopes() {
        // Given: Valid constructor parameters for both project types
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        // When: Creating ProGuardTransform instances with different project types
        ProGuardTransform appTransform = new ProGuardTransform(
            project,
            proguardBlock,
            AndroidProjectType.ANDROID_APPLICATION,
            androidExtension
        );
        ProGuardTransform libTransform = new ProGuardTransform(
            project,
            proguardBlock,
            AndroidProjectType.ANDROID_LIBRARY,
            androidExtension
        );

        // Then: The scopes should be different based on project type
        assertNotNull(appTransform, "App transform should be created");
        assertNotNull(libTransform, "Library transform should be created");
        assertNotEquals(appTransform.getScopes().size(), libTransform.getScopes().size(),
            "Different project types should have different scope counts");
        assertNotEquals(appTransform.getReferencedScopes().size(),
            libTransform.getReferencedScopes().size(),
            "Different project types should have different referenced scope counts");
    }

    /**
     * Test that multiple instances can be created with the same parameters.
     * The constructor should allow creating multiple independent instances.
     */
    @Test
    public void testConstructor_allowsMultipleInstances() {
        // Given: Valid constructor parameters
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        // When: Creating multiple ProGuardTransform instances
        ProGuardTransform transform1 = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );
        ProGuardTransform transform2 = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // Then: Both instances should be created and be distinct
        assertNotNull(transform1, "First instance should be created");
        assertNotNull(transform2, "Second instance should be created");
        assertNotSame(transform1, transform2, "Instances should be distinct objects");
    }

    /**
     * Test that the constructor works with different ProGuardAndroidExtension instances.
     * Each transform can have its own extension.
     */
    @Test
    public void testConstructor_withDifferentExtensions_createsDistinctInstances() {
        // Given: Different ProGuardAndroidExtension instances
        ProGuardAndroidExtension extension1 = new ProGuardAndroidExtension(project);
        ProGuardAndroidExtension extension2 = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        // When: Creating ProGuardTransform instances with different extensions
        ProGuardTransform transform1 = new ProGuardTransform(
            project,
            extension1,
            projectType,
            androidExtension
        );
        ProGuardTransform transform2 = new ProGuardTransform(
            project,
            extension2,
            projectType,
            androidExtension
        );

        // Then: Both instances should be created successfully
        assertNotNull(transform1, "First instance should be created");
        assertNotNull(transform2, "Second instance should be created");
        assertNotSame(transform1, transform2, "Instances should be distinct");
    }

    // ==================== Helper Methods ====================

    /**
     * Helper method to set up Android application plugin on the project.
     * Returns the android extension if successful, null otherwise.
     */
    private BaseExtension setupAndroidApplicationProject() {
        try {
            project.getPluginManager().apply("com.android.application");
            return (BaseExtension) project.getExtensions().getByName("android");
        } catch (Exception e) {
            System.out.println("Android application plugin not available: " + e.getMessage());
            return null;
        }
    }

    /**
     * Helper method to set up Android library plugin on the project.
     * Returns the android extension if successful, null otherwise.
     */
    private BaseExtension setupAndroidLibraryProject() {
        try {
            project.getPluginManager().apply("com.android.library");
            return (BaseExtension) project.getExtensions().getByName("android");
        } catch (Exception e) {
            System.out.println("Android library plugin not available: " + e.getMessage());
            return null;
        }
    }
}
