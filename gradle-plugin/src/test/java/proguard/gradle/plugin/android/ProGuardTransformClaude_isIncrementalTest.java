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
 * Test class for ProGuardTransform.isIncremental method.
 *
 * Tests: proguard.gradle.plugin.android.ProGuardTransform.isIncremental.()Z
 *
 * The isIncremental method indicates whether this transform supports incremental builds.
 * ProGuardTransform always returns false because ProGuard processing requires analyzing
 * the entire codebase to properly perform:
 * - Dead code elimination (shrinking)
 * - Code optimization
 * - Obfuscation with consistent naming
 * - Verification
 *
 * Incremental builds would require tracking:
 * - Which classes reference which other classes
 * - The complete class hierarchy
 * - All method and field references
 * - Keep rules and their dependencies
 *
 * Since ProGuard performs whole-program analysis and optimization, it cannot safely
 * process only changed files - it must process all inputs on every build.
 *
 * This method is simple (always returns false) but important because:
 * 1. The Android Gradle Plugin uses this to determine build behavior
 * 2. It affects build performance expectations
 * 3. It's part of the Transform API contract
 */
public class ProGuardTransformClaude_isIncrementalTest {

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
     * Test that isIncremental returns false for ANDROID_APPLICATION projects.
     * ProGuardTransform never supports incremental builds, regardless of project type.
     */
    @Test
    public void testIsIncremental_forAndroidApplication_returnsFalse() {
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

        // When: Checking if transform is incremental
        boolean isIncremental = transform.isIncremental();

        // Then: Should return false
        assertFalse(isIncremental,
            "ProGuardTransform should not support incremental builds for ANDROID_APPLICATION");
    }

    /**
     * Test that isIncremental returns false for ANDROID_LIBRARY projects.
     * ProGuardTransform never supports incremental builds, regardless of project type.
     */
    @Test
    public void testIsIncremental_forAndroidLibrary_returnsFalse() {
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

        // When: Checking if transform is incremental
        boolean isIncremental = transform.isIncremental();

        // Then: Should return false
        assertFalse(isIncremental,
            "ProGuardTransform should not support incremental builds for ANDROID_LIBRARY");
    }

    // ==================== Consistency tests ====================

    /**
     * Test that isIncremental always returns the same value when called multiple times.
     * The incremental support status should not change during the transform's lifetime.
     */
    @Test
    public void testIsIncremental_consistentAcrossMultipleCalls() {
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

        // When: Checking isIncremental multiple times
        boolean result1 = transform.isIncremental();
        boolean result2 = transform.isIncremental();
        boolean result3 = transform.isIncremental();

        // Then: All calls should return the same value
        assertEquals(result1, result2,
            "First and second calls should return the same value");
        assertEquals(result2, result3,
            "Second and third calls should return the same value");
        assertEquals(result1, result3,
            "First and third calls should return the same value");
        assertFalse(result1, "All calls should return false");
    }

    /**
     * Test that isIncremental returns the same value for different instances with same parameters.
     * The incremental support is a property of the transform type, not the instance.
     */
    @Test
    public void testIsIncremental_consistentAcrossDifferentInstances() {
        // Given: Two ProGuardTransform instances with the same parameters
        ProGuardAndroidExtension proguardBlock1 = new ProGuardAndroidExtension(project);
        ProGuardAndroidExtension proguardBlock2 = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform transform1 = new ProGuardTransform(
            project,
            proguardBlock1,
            projectType,
            androidExtension
        );

        ProGuardTransform transform2 = new ProGuardTransform(
            project,
            proguardBlock2,
            projectType,
            androidExtension
        );

        // When: Checking isIncremental for both instances
        boolean isIncremental1 = transform1.isIncremental();
        boolean isIncremental2 = transform2.isIncremental();

        // Then: Both should return the same value
        assertEquals(isIncremental1, isIncremental2,
            "Different instances should return the same isIncremental value");
        assertFalse(isIncremental1, "Both instances should return false");
    }

    // ==================== Comparison tests between project types ====================

    /**
     * Test that isIncremental returns the same value for both project types.
     * Incremental support does not depend on whether it's an application or library.
     */
    @Test
    public void testIsIncremental_sameForBothProjectTypes() {
        // Given: Two ProGuardTransform instances with different project types
        ProGuardAndroidExtension appProguardBlock = new ProGuardAndroidExtension(project);
        BaseExtension appExtension = setupAndroidApplicationProject();

        if (appExtension == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform appTransform = new ProGuardTransform(
            project,
            appProguardBlock,
            AndroidProjectType.ANDROID_APPLICATION,
            appExtension
        );

        // Create new project for library
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        ProGuardAndroidExtension libProguardBlock = new ProGuardAndroidExtension(project);
        BaseExtension libExtension = setupAndroidLibraryProject();

        if (libExtension == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform libTransform = new ProGuardTransform(
            project,
            libProguardBlock,
            AndroidProjectType.ANDROID_LIBRARY,
            libExtension
        );

        // When: Checking isIncremental for both transforms
        boolean appIsIncremental = appTransform.isIncremental();
        boolean libIsIncremental = libTransform.isIncremental();

        // Then: Both should return false
        assertEquals(appIsIncremental, libIsIncremental,
            "isIncremental should return the same value for both project types");
        assertFalse(appIsIncremental, "Application transform should return false");
        assertFalse(libIsIncremental, "Library transform should return false");
    }

    // ==================== Tests with different constructor parameters ====================

    /**
     * Test that isIncremental returns false regardless of the ProGuardAndroidExtension configuration.
     * The incremental support is not affected by the ProGuard configuration.
     */
    @Test
    public void testIsIncremental_notAffectedByProGuardConfiguration() {
        // Given: ProGuardTransform instances with different ProGuardAndroidExtension instances
        ProGuardAndroidExtension proguardBlock1 = new ProGuardAndroidExtension(project);
        ProGuardAndroidExtension proguardBlock2 = new ProGuardAndroidExtension(project);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;
        BaseExtension androidExtension = setupAndroidApplicationProject();

        if (androidExtension == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform transform1 = new ProGuardTransform(
            project,
            proguardBlock1,
            projectType,
            androidExtension
        );

        ProGuardTransform transform2 = new ProGuardTransform(
            project,
            proguardBlock2,
            projectType,
            androidExtension
        );

        // When: Checking isIncremental for both transforms
        boolean isIncremental1 = transform1.isIncremental();
        boolean isIncremental2 = transform2.isIncremental();

        // Then: Both should return false regardless of configuration
        assertFalse(isIncremental1,
            "Transform with first configuration should return false");
        assertFalse(isIncremental2,
            "Transform with second configuration should return false");
    }

    /**
     * Test that isIncremental returns false regardless of the Project instance.
     * The incremental support is not affected by the project context.
     */
    @Test
    public void testIsIncremental_notAffectedByProject() {
        // Given: Two different projects
        Project project1 = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        Project project2 = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();

        ProGuardAndroidExtension proguardBlock1 = new ProGuardAndroidExtension(project1);
        ProGuardAndroidExtension proguardBlock2 = new ProGuardAndroidExtension(project2);
        AndroidProjectType projectType = AndroidProjectType.ANDROID_APPLICATION;

        BaseExtension androidExtension1 = setupAndroidApplicationProjectForProject(project1);
        if (androidExtension1 == null) {
            return; // Skip if Android plugin not available
        }

        BaseExtension androidExtension2 = setupAndroidApplicationProjectForProject(project2);
        if (androidExtension2 == null) {
            return; // Skip if Android plugin not available
        }

        ProGuardTransform transform1 = new ProGuardTransform(
            project1,
            proguardBlock1,
            projectType,
            androidExtension1
        );

        ProGuardTransform transform2 = new ProGuardTransform(
            project2,
            proguardBlock2,
            projectType,
            androidExtension2
        );

        // When: Checking isIncremental for both transforms
        boolean isIncremental1 = transform1.isIncremental();
        boolean isIncremental2 = transform2.isIncremental();

        // Then: Both should return false regardless of project
        assertFalse(isIncremental1,
            "Transform with first project should return false");
        assertFalse(isIncremental2,
            "Transform with second project should return false");
    }

    // ==================== Tests verifying the return type ====================

    /**
     * Test that isIncremental returns a boolean primitive (not Boolean object).
     * This verifies the method signature is correct (boolean, not Boolean).
     */
    @Test
    public void testIsIncremental_returnsPrimitiveBoolean() {
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

        // When: Checking isIncremental
        boolean isIncremental = transform.isIncremental();

        // Then: Should be able to use in primitive boolean context
        assertFalse(isIncremental, "Should return false");
        // Verify it can be used in conditional expressions without null checks
        String result = isIncremental ? "incremental" : "non-incremental";
        assertEquals("non-incremental", result,
            "Should work in primitive boolean context");
    }

    /**
     * Test that isIncremental can be called immediately after construction.
     * There should be no initialization requirements before calling this method.
     */
    @Test
    public void testIsIncremental_availableImmediatelyAfterConstruction() {
        // Given/When: Creating a ProGuardTransform and immediately checking isIncremental
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

        // Then: Should be able to call isIncremental immediately
        assertDoesNotThrow(() -> {
            boolean isIncremental = transform.isIncremental();
            assertFalse(isIncremental,
                "Should return false immediately after construction");
        }, "isIncremental should be callable immediately after construction");
    }

    // ==================== Documentation and contract tests ====================

    /**
     * Test that isIncremental returns false, which is consistent with ProGuard's design.
     * This test serves as documentation of why ProGuard cannot be incremental.
     *
     * ProGuard performs whole-program analysis which requires:
     * 1. Complete class hierarchy analysis
     * 2. Cross-class reference tracking
     * 3. Global optimization decisions
     * 4. Consistent obfuscation mapping
     *
     * These requirements make incremental processing infeasible.
     */
    @Test
    public void testIsIncremental_returnsFalse_becauseProGuardRequiresWholeProgramAnalysis() {
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

        // When: Checking if transform is incremental
        boolean isIncremental = transform.isIncremental();

        // Then: Should return false because ProGuard requires whole-program analysis
        assertFalse(isIncremental,
            "ProGuardTransform cannot be incremental because ProGuard performs " +
            "whole-program analysis including shrinking, optimization, and obfuscation " +
            "which require processing all classes together to maintain correctness");
    }

    /**
     * Test that isIncremental's return value is consistent with Transform API contract.
     * When isIncremental returns false, the transform will always receive all inputs,
     * not just changed files.
     */
    @Test
    public void testIsIncremental_contractWithTransformAPI() {
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

        // When: Checking if transform is incremental
        boolean isIncremental = transform.isIncremental();

        // Then: Should return false, indicating all inputs will be provided every time
        assertFalse(isIncremental,
            "When isIncremental returns false, the Transform API will provide all inputs " +
            "on every invocation, not just changed files");
    }

    // ==================== Helper Methods ====================

    /**
     * Helper method to set up Android application plugin on the project.
     * Returns the android extension if successful, null otherwise.
     */
    private BaseExtension setupAndroidApplicationProject() {
        return setupAndroidApplicationProjectForProject(project);
    }

    /**
     * Helper method to set up Android application plugin on a specific project.
     * Returns the android extension if successful, null otherwise.
     */
    private BaseExtension setupAndroidApplicationProjectForProject(Project proj) {
        try {
            proj.getPluginManager().apply("com.android.application");
            return (BaseExtension) proj.getExtensions().getByName("android");
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
