package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for ProGuardTask.getkeepkotlinmetadata() method.
 * This is a Groovy DSL getter method that returns null and calls keepkotlinmetadata().
 */
public class ProGuardTaskClaude_getkeepkotlinmetadataTest {

    @TempDir
    public Path temporaryFolder;

    private Project project;
    private ProGuardTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(temporaryFolder.toFile())
                .build();
        task = project.getTasks().create("proguard", ProGuardTask.class);
    }

    @AfterEach
    public void tearDown() {
        project = null;
        task = null;
    }

    // ========================================
    // Tests for getkeepkotlinmetadata() Method
    // ========================================

    @Test
    public void testGetkeepkotlinmetadata_returnsNull() throws Exception {
        Object result = task.getkeepkotlinmetadata();

        assertNull(result, "getkeepkotlinmetadata should return null for Groovy DSL support");
    }

    @Test
    public void testGetkeepkotlinmetadata_setsKeepKotlinMetadata() throws Exception {
        assertFalse(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should initially be false");

        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set to true");
    }

    @Test
    public void testGetkeepkotlinmetadata_multipleCalls() throws Exception {
        task.getkeepkotlinmetadata();
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be true after first call");

        task.getkeepkotlinmetadata();
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should remain true after second call");

        task.getkeepkotlinmetadata();
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should remain true after third call");
    }

    @Test
    public void testGetkeepkotlinmetadata_isIdempotent() throws Exception {
        for (int i = 0; i < 5; i++) {
            task.getkeepkotlinmetadata();
            assertTrue(task.configuration.keepKotlinMetadata,
                      "keepKotlinMetadata should remain true on iteration " + i);
        }
    }

    @Test
    public void testGetkeepkotlinmetadata_alwaysReturnsNull() throws Exception {
        Object result1 = task.getkeepkotlinmetadata();
        Object result2 = task.getkeepkotlinmetadata();
        Object result3 = task.getkeepkotlinmetadata();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetkeepkotlinmetadata_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getkeepkotlinmetadata();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    @Test
    public void testGetkeepkotlinmetadata_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.dontshrink();

        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertFalse(task.configuration.shrink, "dontshrink should remain set");
    }

    @Test
    public void testGetkeepkotlinmetadata_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.getkeepkotlinmetadata();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testGetkeepkotlinmetadata_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testGetkeepkotlinmetadata_withObfuscationEnabled() throws Exception {
        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled by default");
    }

    @Test
    public void testGetkeepkotlinmetadata_withOptimizationEnabled() throws Exception {
        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.optimize, "optimization should be enabled by default");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testGetkeepkotlinmetadata_forKotlinProject() throws Exception {
        // Kotlin projects need to keep metadata for reflection and introspection
        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for Kotlin");
    }

    @Test
    public void testGetkeepkotlinmetadata_forKotlinReflection() throws Exception {
        // Kotlin reflection relies on metadata
        task.getkeepkotlinmetadata();
        task.keepattributes("*Annotation*,Signature");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertNotNull(task.configuration.keepAttributes, "Annotations should be kept");
    }

    @Test
    public void testGetkeepkotlinmetadata_forKotlinLibrary() throws Exception {
        // Kotlin libraries need metadata preserved
        task.getkeepkotlinmetadata();
        task.dontobfuscate();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for libraries");
    }

    @Test
    public void testGetkeepkotlinmetadata_withKotlinCoroutines() throws Exception {
        // Kotlin coroutines need metadata
        task.getkeepkotlinmetadata();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for coroutines");
    }

    @Test
    public void testGetkeepkotlinmetadata_withKotlinSerialization() throws Exception {
        // Kotlin serialization requires metadata
        task.getkeepkotlinmetadata();
        task.keepattributes("*Annotation*,Signature");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for serialization");
    }

    @Test
    public void testGetkeepkotlinmetadata_forMultiplatformKotlin() throws Exception {
        // Multiplatform Kotlin projects need metadata
        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for multiplatform");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testGetkeepkotlinmetadata_afterManuallySetToFalse() throws Exception {
        task.configuration.keepKotlinMetadata = false;

        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata,
                  "keepKotlinMetadata should be set to true even after being explicitly set to false");
    }

    @Test
    public void testGetkeepkotlinmetadata_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    @Test
    public void testGetkeepkotlinmetadata_calledFirst() throws Exception {
        // Test calling getkeepkotlinmetadata before any other configuration
        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetkeepkotlinmetadata_calledLast() throws Exception {
        // Test calling getkeepkotlinmetadata after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testGetkeepkotlinmetadata_androidKotlinApp() throws Exception {
        // Android apps written in Kotlin
        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for Android Kotlin");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
    }

    @Test
    public void testGetkeepkotlinmetadata_androidKotlinLibrary() throws Exception {
        // Android library modules written in Kotlin
        task.getkeepkotlinmetadata();
        task.dontobfuscate();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for libraries");
    }

    @Test
    public void testGetkeepkotlinmetadata_androidWithKotlinExtensions() throws Exception {
        // Android projects using Kotlin Android Extensions
        task.getkeepkotlinmetadata();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertNotNull(task.configuration.keepAttributes, "Annotations should be kept");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testGetkeepkotlinmetadata_beforeVerbose() throws Exception {
        task.getkeepkotlinmetadata();
        task.verbose();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testGetkeepkotlinmetadata_afterVerbose() throws Exception {
        task.verbose();
        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    @Test
    public void testGetkeepkotlinmetadata_betweenOtherCalls() throws Exception {
        task.verbose();
        task.getkeepkotlinmetadata();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testGetkeepkotlinmetadata_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.getkeepkotlinmetadata();
        task.keepattributes("*Annotation*,Signature");
        task.verbose();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testGetkeepkotlinmetadata_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.getkeepkotlinmetadata();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    @Test
    public void testGetkeepkotlinmetadata_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testGetkeepkotlinmetadata_forKotlinDataClasses() throws Exception {
        // Kotlin data classes rely on metadata for component functions
        task.getkeepkotlinmetadata();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for data classes");
    }

    @Test
    public void testGetkeepkotlinmetadata_forKotlinSealedClasses() throws Exception {
        // Sealed classes need metadata
        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for sealed classes");
    }

    @Test
    public void testGetkeepkotlinmetadata_forKotlinInlineClasses() throws Exception {
        // Inline classes (value classes) need metadata
        task.getkeepkotlinmetadata();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for inline classes");
    }

    @Test
    public void testGetkeepkotlinmetadata_forKotlinDelegatedProperties() throws Exception {
        // Delegated properties need metadata
        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for delegated properties");
    }

    @Test
    public void testGetkeepkotlinmetadata_forKotlinTypeAliases() throws Exception {
        // Type aliases benefit from metadata
        task.getkeepkotlinmetadata();
        task.keepattributes("Signature");

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set for type aliases");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testGetkeepkotlinmetadata_configurationNotNull() throws Exception {
        task.getkeepkotlinmetadata();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetkeepkotlinmetadata_returnValueConsistency() throws Exception {
        Object result1 = task.getkeepkotlinmetadata();
        Object result2 = task.getkeepkotlinmetadata();

        assertEquals(result1, result2, "Return value should be consistent (both null)");
    }

    @Test
    public void testGetkeepkotlinmetadata_taskStateValid() throws Exception {
        task.getkeepkotlinmetadata();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetkeepkotlinmetadata_withMinimalConfiguration() throws Exception {
        // Test with only getkeepkotlinmetadata called
        Object result = task.getkeepkotlinmetadata();

        assertNull(result, "Should return null");
        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetkeepkotlinmetadata_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.getkeepkotlinmetadata();

        assertTrue(task.configuration.keepKotlinMetadata, "keepKotlinMetadata should be set");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }
}
