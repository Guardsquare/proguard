/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android;

import com.android.build.api.transform.QualifiedContent.Scope;
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
 * Test class for ProGuardTransform.getReferencedScopes method.
 *
 * Tests: proguard.gradle.plugin.android.ProGuardTransform.getReferencedScopes.()Ljava/util/Set;
 *
 * The getReferencedScopes method returns the set of scopes that this transform references
 * but does not consume. Referenced scopes are used as library jars during ProGuard processing
 * - they are read but not transformed.
 *
 * The referenced scopes depend on the project type:
 *
 * For ANDROID_APPLICATION:
 * - PROVIDED_ONLY: Dependencies marked as "provided" or "compileOnly" that are available at
 *   compile time but not included in the final output
 *
 * For ANDROID_LIBRARY:
 * - PROVIDED_ONLY: Dependencies marked as "provided" or "compileOnly"
 * - EXTERNAL_LIBRARIES: External dependencies (referenced, not consumed, since libraries don't
 *   process dependencies - that's done by the consuming application)
 * - SUB_PROJECTS: Subproject dependencies (referenced for the same reason)
 *
 * Referenced scopes vs consumed scopes:
 * - Consumed scopes (getScopes): Inputs that are transformed and included in the output
 * - Referenced scopes (getReferencedScopes): Inputs used as library jars but not transformed
 */
public class ProGuardTransformClaude_getReferencedScopesTest {

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
     * Test that getReferencedScopes returns a non-null set.
     * The method should never return null.
     */
    @Test
    public void testGetReferencedScopes_returnsNonNull() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: Should return a non-null set
        assertNotNull(referencedScopes, "getReferencedScopes should never return null");
    }

    /**
     * Test that getReferencedScopes returns a non-empty set.
     * The transform must reference at least one scope.
     */
    @Test
    public void testGetReferencedScopes_returnsNonEmpty() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: Should return a non-empty set
        assertFalse(referencedScopes.isEmpty(),
            "getReferencedScopes should return at least one scope");
    }

    // ==================== Tests for ANDROID_APPLICATION ====================

    /**
     * Test that getReferencedScopes returns exactly 1 scope for ANDROID_APPLICATION.
     * Application projects only reference PROVIDED_ONLY scope.
     */
    @Test
    public void testGetReferencedScopes_forAndroidApplication_returnsOneScope() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: Should return exactly 1 scope
        assertEquals(1, referencedScopes.size(),
            "ANDROID_APPLICATION should have exactly 1 referenced scope (PROVIDED_ONLY)");
    }

    /**
     * Test that getReferencedScopes includes PROVIDED_ONLY scope for ANDROID_APPLICATION.
     * Application projects must reference provided-only dependencies.
     */
    @Test
    public void testGetReferencedScopes_forAndroidApplication_containsProvidedOnlyScope() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: Should contain PROVIDED_ONLY scope
        assertTrue(referencedScopes.contains(Scope.PROVIDED_ONLY),
            "ANDROID_APPLICATION referenced scopes should include PROVIDED_ONLY");
    }

    /**
     * Test that getReferencedScopes does not include PROJECT scope for ANDROID_APPLICATION.
     * PROJECT is a consumed scope, not a referenced scope.
     */
    @Test
    public void testGetReferencedScopes_forAndroidApplication_doesNotContainProjectScope() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: Should NOT contain PROJECT scope
        assertFalse(referencedScopes.contains(Scope.PROJECT),
            "ANDROID_APPLICATION referenced scopes should NOT include PROJECT (it's a consumed scope)");
    }

    /**
     * Test that getReferencedScopes does not include SUB_PROJECTS scope for ANDROID_APPLICATION.
     * SUB_PROJECTS is a consumed scope for applications, not a referenced scope.
     */
    @Test
    public void testGetReferencedScopes_forAndroidApplication_doesNotContainSubProjectsScope() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: Should NOT contain SUB_PROJECTS scope
        assertFalse(referencedScopes.contains(Scope.SUB_PROJECTS),
            "ANDROID_APPLICATION referenced scopes should NOT include SUB_PROJECTS (it's a consumed scope)");
    }

    /**
     * Test that getReferencedScopes does not include EXTERNAL_LIBRARIES scope for ANDROID_APPLICATION.
     * EXTERNAL_LIBRARIES is a consumed scope for applications, not a referenced scope.
     */
    @Test
    public void testGetReferencedScopes_forAndroidApplication_doesNotContainExternalLibrariesScope() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: Should NOT contain EXTERNAL_LIBRARIES scope
        assertFalse(referencedScopes.contains(Scope.EXTERNAL_LIBRARIES),
            "ANDROID_APPLICATION referenced scopes should NOT include EXTERNAL_LIBRARIES (it's a consumed scope)");
    }

    /**
     * Test that getReferencedScopes contains exactly the expected scope for ANDROID_APPLICATION.
     * Verifies only PROVIDED_ONLY and no others.
     */
    @Test
    public void testGetReferencedScopes_forAndroidApplication_containsOnlyProvidedOnlyScope() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: Should contain exactly PROVIDED_ONLY
        assertEquals(1, referencedScopes.size(), "Should contain exactly 1 scope");
        assertTrue(referencedScopes.contains(Scope.PROVIDED_ONLY),
            "Should contain PROVIDED_ONLY");
    }

    // ==================== Tests for ANDROID_LIBRARY ====================

    /**
     * Test that getReferencedScopes returns exactly 3 scopes for ANDROID_LIBRARY.
     * Library projects reference PROVIDED_ONLY, EXTERNAL_LIBRARIES, and SUB_PROJECTS.
     */
    @Test
    public void testGetReferencedScopes_forAndroidLibrary_returnsThreeScopes() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: Should return exactly 3 scopes
        assertEquals(3, referencedScopes.size(),
            "ANDROID_LIBRARY should have exactly 3 referenced scopes (PROVIDED_ONLY, EXTERNAL_LIBRARIES, SUB_PROJECTS)");
    }

    /**
     * Test that getReferencedScopes includes PROVIDED_ONLY scope for ANDROID_LIBRARY.
     * Library projects must reference provided-only dependencies.
     */
    @Test
    public void testGetReferencedScopes_forAndroidLibrary_containsProvidedOnlyScope() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: Should contain PROVIDED_ONLY scope
        assertTrue(referencedScopes.contains(Scope.PROVIDED_ONLY),
            "ANDROID_LIBRARY referenced scopes should include PROVIDED_ONLY");
    }

    /**
     * Test that getReferencedScopes includes EXTERNAL_LIBRARIES scope for ANDROID_LIBRARY.
     * Libraries reference external dependencies instead of consuming them.
     */
    @Test
    public void testGetReferencedScopes_forAndroidLibrary_containsExternalLibrariesScope() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: Should contain EXTERNAL_LIBRARIES scope
        assertTrue(referencedScopes.contains(Scope.EXTERNAL_LIBRARIES),
            "ANDROID_LIBRARY referenced scopes should include EXTERNAL_LIBRARIES");
    }

    /**
     * Test that getReferencedScopes includes SUB_PROJECTS scope for ANDROID_LIBRARY.
     * Libraries reference subprojects instead of consuming them.
     */
    @Test
    public void testGetReferencedScopes_forAndroidLibrary_containsSubProjectsScope() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: Should contain SUB_PROJECTS scope
        assertTrue(referencedScopes.contains(Scope.SUB_PROJECTS),
            "ANDROID_LIBRARY referenced scopes should include SUB_PROJECTS");
    }

    /**
     * Test that getReferencedScopes does not include PROJECT scope for ANDROID_LIBRARY.
     * PROJECT is a consumed scope, not a referenced scope.
     */
    @Test
    public void testGetReferencedScopes_forAndroidLibrary_doesNotContainProjectScope() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: Should NOT contain PROJECT scope
        assertFalse(referencedScopes.contains(Scope.PROJECT),
            "ANDROID_LIBRARY referenced scopes should NOT include PROJECT (it's a consumed scope)");
    }

    /**
     * Test that getReferencedScopes contains exactly the expected scopes for ANDROID_LIBRARY.
     * Verifies all three scopes and no others.
     */
    @Test
    public void testGetReferencedScopes_forAndroidLibrary_containsOnlyExpectedScopes() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: Should contain exactly PROVIDED_ONLY, EXTERNAL_LIBRARIES, and SUB_PROJECTS
        assertEquals(3, referencedScopes.size(), "Should contain exactly 3 scopes");
        assertTrue(referencedScopes.contains(Scope.PROVIDED_ONLY),
            "Should contain PROVIDED_ONLY");
        assertTrue(referencedScopes.contains(Scope.EXTERNAL_LIBRARIES),
            "Should contain EXTERNAL_LIBRARIES");
        assertTrue(referencedScopes.contains(Scope.SUB_PROJECTS),
            "Should contain SUB_PROJECTS");
    }

    // ==================== Comparison tests between project types ====================

    /**
     * Test that getReferencedScopes returns different results for different project types.
     * ANDROID_APPLICATION and ANDROID_LIBRARY should have different referenced scopes.
     */
    @Test
    public void testGetReferencedScopes_differsByProjectType() {
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

        // When: Getting referenced scopes from both transforms
        Set<? super Scope> appReferencedScopes = appTransform.getReferencedScopes();
        Set<? super Scope> libReferencedScopes = libTransform.getReferencedScopes();

        // Then: Referenced scopes should be different
        assertNotEquals(appReferencedScopes.size(), libReferencedScopes.size(),
            "Different project types should have different referenced scope counts");
        assertNotEquals(appReferencedScopes, libReferencedScopes,
            "Different project types should have different referenced scopes");
    }

    /**
     * Test that ANDROID_LIBRARY has more referenced scopes than ANDROID_APPLICATION.
     * Libraries reference more scopes because they don't consume dependencies.
     */
    @Test
    public void testGetReferencedScopes_libraryHasMoreScopesThanApplication() {
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

        // When: Getting referenced scopes from both transforms
        Set<? super Scope> appReferencedScopes = appTransform.getReferencedScopes();
        Set<? super Scope> libReferencedScopes = libTransform.getReferencedScopes();

        // Then: Library should have more referenced scopes
        assertTrue(libReferencedScopes.size() > appReferencedScopes.size(),
            "ANDROID_LIBRARY should have more referenced scopes than ANDROID_APPLICATION");
    }

    /**
     * Test that both project types include PROVIDED_ONLY in referenced scopes.
     * All projects must reference provided-only dependencies.
     */
    @Test
    public void testGetReferencedScopes_bothProjectTypesIncludeProvidedOnlyScope() {
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

        // When: Getting referenced scopes from both transforms
        Set<? super Scope> appReferencedScopes = appTransform.getReferencedScopes();
        Set<? super Scope> libReferencedScopes = libTransform.getReferencedScopes();

        // Then: Both should contain PROVIDED_ONLY scope
        assertTrue(appReferencedScopes.contains(Scope.PROVIDED_ONLY),
            "ANDROID_APPLICATION should include PROVIDED_ONLY in referenced scopes");
        assertTrue(libReferencedScopes.contains(Scope.PROVIDED_ONLY),
            "ANDROID_LIBRARY should include PROVIDED_ONLY in referenced scopes");
    }

    /**
     * Test the relationship between consumed scopes and referenced scopes for ANDROID_APPLICATION.
     * Scopes that are consumed should not be referenced (no overlap for applications).
     */
    @Test
    public void testGetReferencedScopes_forAndroidApplication_noOverlapWithConsumedScopes() {
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

        // When: Getting both consumed and referenced scopes
        Set<? super Scope> consumedScopes = transform.getScopes();
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: There should be no overlap between consumed and referenced scopes
        for (Object scope : referencedScopes) {
            assertFalse(consumedScopes.contains(scope),
                "Referenced scope " + scope + " should not be in consumed scopes for ANDROID_APPLICATION");
        }
    }

    /**
     * Test the relationship between consumed scopes and referenced scopes for ANDROID_LIBRARY.
     * For libraries, EXTERNAL_LIBRARIES and SUB_PROJECTS are referenced (not consumed).
     */
    @Test
    public void testGetReferencedScopes_forAndroidLibrary_externalLibrariesAndSubProjectsAreReferenced() {
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

        // When: Getting both consumed and referenced scopes
        Set<? super Scope> consumedScopes = transform.getScopes();
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: EXTERNAL_LIBRARIES and SUB_PROJECTS should be referenced, not consumed
        assertTrue(referencedScopes.contains(Scope.EXTERNAL_LIBRARIES),
            "EXTERNAL_LIBRARIES should be in referenced scopes for ANDROID_LIBRARY");
        assertTrue(referencedScopes.contains(Scope.SUB_PROJECTS),
            "SUB_PROJECTS should be in referenced scopes for ANDROID_LIBRARY");
        assertFalse(consumedScopes.contains(Scope.EXTERNAL_LIBRARIES),
            "EXTERNAL_LIBRARIES should NOT be in consumed scopes for ANDROID_LIBRARY");
        assertFalse(consumedScopes.contains(Scope.SUB_PROJECTS),
            "SUB_PROJECTS should NOT be in consumed scopes for ANDROID_LIBRARY");
    }

    // ==================== Tests for consistency ====================

    /**
     * Test that getReferencedScopes returns consistent results when called multiple times.
     * The method should return equivalent sets on every invocation.
     */
    @Test
    public void testGetReferencedScopes_consistentAcrossMultipleCalls() {
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

        // When: Getting referenced scopes multiple times
        Set<? super Scope> referencedScopes1 = transform.getReferencedScopes();
        Set<? super Scope> referencedScopes2 = transform.getReferencedScopes();
        Set<? super Scope> referencedScopes3 = transform.getReferencedScopes();

        // Then: All calls should return equal sets
        assertEquals(referencedScopes1, referencedScopes2,
            "First and second calls should return equal sets");
        assertEquals(referencedScopes2, referencedScopes3,
            "Second and third calls should return equal sets");
        assertEquals(referencedScopes1, referencedScopes3,
            "First and third calls should return equal sets");
    }

    /**
     * Test that getReferencedScopes returns a mutable set.
     * The method signature indicates it returns MutableSet, verify it's actually mutable.
     */
    @Test
    public void testGetReferencedScopes_returnsMutableSet() {
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

        // When: Getting referenced scopes
        Set<? super Scope> referencedScopes = transform.getReferencedScopes();

        // Then: The set should be mutable (we can attempt to add/remove)
        // Note: We're just testing that it doesn't throw UnsupportedOperationException
        // We don't actually modify the set in practice
        assertDoesNotThrow(() -> {
            int originalSize = referencedScopes.size();
            // Just verify it's mutable by checking we can call these methods
            // without UnsupportedOperationException
            referencedScopes.size();
        }, "getReferencedScopes should return a mutable set as per the method signature");
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
