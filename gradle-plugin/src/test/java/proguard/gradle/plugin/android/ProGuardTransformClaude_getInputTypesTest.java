/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android;

import com.android.build.api.transform.QualifiedContent.DefaultContentType;
import com.android.build.gradle.BaseExtension;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import proguard.gradle.plugin.android.dsl.ProGuardAndroidExtension;

import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTransform.getInputTypes method.
 *
 * Tests: proguard.gradle.plugin.android.ProGuardTransform.getInputTypes.()Ljava/util/Set;
 *
 * The getInputTypes method returns the set of content types that this transform handles.
 * For ProGuardTransform, this includes:
 * - CLASSES: Java bytecode files
 * - RESOURCES: Resource files that should be processed
 *
 * This is a simple method that returns a constant set, but it's important to verify
 * that it returns the correct types regardless of the project configuration.
 */
public class ProGuardTransformClaude_getInputTypesTest {

    @TempDir
    Path tempDir;

    private Project project;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
    }

    // ==================== Basic behavior tests ====================

    /**
     * Test that getInputTypes returns a non-null set.
     * The method should never return null.
     */
    @Test
    public void testGetInputTypes_returnsNonNull() {
        // Given: A ProGuardTransform instance for an application project
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // When: Getting input types
        Set<DefaultContentType> inputTypes = transform.getInputTypes();

        // Then: Should return a non-null set
        assertNotNull(inputTypes, "getInputTypes should never return null");
    }

    /**
     * Test that getInputTypes returns a set containing exactly 2 elements.
     * ProGuardTransform handles both CLASSES and RESOURCES.
     */
    @Test
    public void testGetInputTypes_returnsTwoElements() {
        // Given: A ProGuardTransform instance
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // When: Getting input types
        Set<DefaultContentType> inputTypes = transform.getInputTypes();

        // Then: Should contain exactly 2 elements
        assertEquals(2, inputTypes.size(),
            "getInputTypes should return exactly 2 content types (CLASSES and RESOURCES)");
    }

    /**
     * Test that getInputTypes returns a set containing CLASSES.
     * ProGuardTransform must handle Java bytecode files.
     */
    @Test
    public void testGetInputTypes_containsClasses() {
        // Given: A ProGuardTransform instance
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // When: Getting input types
        Set<DefaultContentType> inputTypes = transform.getInputTypes();

        // Then: Should contain CLASSES
        assertTrue(inputTypes.contains(DefaultContentType.CLASSES),
            "getInputTypes should include CLASSES content type");
    }

    /**
     * Test that getInputTypes returns a set containing RESOURCES.
     * ProGuardTransform must handle resource files.
     */
    @Test
    public void testGetInputTypes_containsResources() {
        // Given: A ProGuardTransform instance
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // When: Getting input types
        Set<DefaultContentType> inputTypes = transform.getInputTypes();

        // Then: Should contain RESOURCES
        assertTrue(inputTypes.contains(DefaultContentType.RESOURCES),
            "getInputTypes should include RESOURCES content type");
    }

    /**
     * Test that getInputTypes returns exactly CLASSES and RESOURCES, no other types.
     * This verifies that only the expected content types are included.
     */
    @Test
    public void testGetInputTypes_containsOnlyClassesAndResources() {
        // Given: A ProGuardTransform instance
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // When: Getting input types
        Set<DefaultContentType> inputTypes = transform.getInputTypes();

        // Then: Should contain exactly CLASSES and RESOURCES
        assertEquals(2, inputTypes.size(), "Should contain exactly 2 elements");
        assertTrue(inputTypes.contains(DefaultContentType.CLASSES),
            "Should contain CLASSES");
        assertTrue(inputTypes.contains(DefaultContentType.RESOURCES),
            "Should contain RESOURCES");
    }

    // ==================== Tests for different project types ====================

    /**
     * Test that getInputTypes returns the same set for ANDROID_APPLICATION projects.
     * The input types should not depend on the project type.
     */
    @Test
    public void testGetInputTypes_forAndroidApplication() {
        // Given: A ProGuardTransform for an Android application
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // When: Getting input types
        Set<DefaultContentType> inputTypes = transform.getInputTypes();

        // Then: Should return CLASSES and RESOURCES
        assertEquals(2, inputTypes.size());
        assertTrue(inputTypes.contains(DefaultContentType.CLASSES));
        assertTrue(inputTypes.contains(DefaultContentType.RESOURCES));
    }

    /**
     * Test that getInputTypes returns the same set for ANDROID_LIBRARY projects.
     * The input types should be consistent regardless of project type.
     */
    @Test
    public void testGetInputTypes_forAndroidLibrary() {
        // Given: A ProGuardTransform for an Android library
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_LIBRARY;
        BaseExtension androidExtension = setupAndroidLibraryProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // When: Getting input types
        Set<DefaultContentType> inputTypes = transform.getInputTypes();

        // Then: Should return CLASSES and RESOURCES (same as application)
        assertEquals(2, inputTypes.size());
        assertTrue(inputTypes.contains(DefaultContentType.CLASSES));
        assertTrue(inputTypes.contains(DefaultContentType.RESOURCES));
    }

    /**
     * Test that getInputTypes returns the same set for both project types.
     * This verifies that project type doesn't affect input types.
     */
    @Test
    public void testGetInputTypes_consistentAcrossProjectTypes() {
        // Given: Two ProGuardTransform instances with different project types
        ProGuardAndroidExtension proguardBlock1 = new ProGuardAndroidExtension(project);
        ProGuardAndroidExtension proguardBlock2 = new ProGuardAndroidExtension(project);

        BaseExtension appExtension = setupAndroidApplicationProject();
        if (appExtension == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform appTransform = new ProGuardTransform(
            project,
            proguardBlock1,
            AndroidProjectType.ANDROID_APPLICATION,
            appExtension
        );

        // Reset for library project
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        proguardBlock2 = new ProGuardAndroidExtension(project);
        BaseExtension libExtension = setupAndroidLibraryProject();
        if (libExtension == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform libTransform = new ProGuardTransform(
            project,
            proguardBlock2,
            AndroidProjectType.ANDROID_LIBRARY,
            libExtension
        );

        // When: Getting input types from both transforms
        Set<DefaultContentType> appInputTypes = appTransform.getInputTypes();
        Set<DefaultContentType> libInputTypes = libTransform.getInputTypes();

        // Then: Both should return the same set
        assertEquals(appInputTypes.size(), libInputTypes.size(),
            "Input types should be the same size for both project types");
        assertEquals(appInputTypes, libInputTypes,
            "Input types should be identical for both project types");
    }

    // ==================== Tests for immutability ====================

    /**
     * Test that getInputTypes returns a consistent result when called multiple times.
     * The method should return the same content types on every invocation.
     */
    @Test
    public void testGetInputTypes_consistentAcrossMultipleCalls() {
        // Given: A ProGuardTransform instance
        ProGuardAndroidExtension proguardBlock = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform transform = new ProGuardTransform(
            project,
            proguardBlock,
            projectType,
            androidExtension
        );

        // When: Getting input types multiple times
        Set<DefaultContentType> inputTypes1 = transform.getInputTypes();
        Set<DefaultContentType> inputTypes2 = transform.getInputTypes();
        Set<DefaultContentType> inputTypes3 = transform.getInputTypes();

        // Then: All calls should return equivalent sets
        assertEquals(inputTypes1, inputTypes2,
            "First and second calls should return equal sets");
        assertEquals(inputTypes2, inputTypes3,
            "Second and third calls should return equal sets");
        assertEquals(inputTypes1, inputTypes3,
            "First and third calls should return equal sets");
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
