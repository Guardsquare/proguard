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
 * Comprehensive tests for ProGuardTask.getadaptresourcefilecontents() method.
 * This is a Groovy DSL getter method that returns null and calls adaptresourcefilecontents().
 */
public class ProGuardTaskClaude_getadaptresourcefilecontentsTest {

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
    // Tests for getadaptresourcefilecontents() Method
    // ========================================

    @Test
    public void testGetadaptresourcefilecontents_returnsNull() throws Exception {
        Object result = task.getadaptresourcefilecontents();

        assertNull(result, "getadaptresourcefilecontents should return null for Groovy DSL support");
    }

    @Test
    public void testGetadaptresourcefilecontents_initializesAdaptResourceFileContents() throws Exception {
        assertNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should initially be null");

        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should be empty (cleared to keep all)");
    }

    @Test
    public void testGetadaptresourcefilecontents_multipleCalls() throws Exception {
        task.getadaptresourcefilecontents();
        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized after first call");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(), "adaptResourceFileContents should be empty after first call");

        task.getadaptresourcefilecontents();
        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should remain initialized after second call");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(), "adaptResourceFileContents should remain empty after second call");

        task.getadaptresourcefilecontents();
        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should remain initialized after third call");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(), "adaptResourceFileContents should remain empty after third call");
    }

    @Test
    public void testGetadaptresourcefilecontents_isIdempotent() throws Exception {
        for (int i = 0; i < 5; i++) {
            task.getadaptresourcefilecontents();
            assertNotNull(task.configuration.adaptResourceFileContents,
                        "adaptResourceFileContents should be initialized on iteration " + i);
            assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                      "adaptResourceFileContents should be empty on iteration " + i);
        }
    }

    @Test
    public void testGetadaptresourcefilecontents_alwaysReturnsNull() throws Exception {
        Object result1 = task.getadaptresourcefilecontents();
        Object result2 = task.getadaptresourcefilecontents();
        Object result3 = task.getadaptresourcefilecontents();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetadaptresourcefilecontents_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
    }

    @Test
    public void testGetadaptresourcefilecontents_clearsExistingFilter() throws Exception {
        // Manually add some filters
        task.adaptresourcefilecontents("**.properties");
        assertFalse(task.configuration.adaptResourceFileContents.isEmpty(),
                   "adaptResourceFileContents should contain filters");

        task.getadaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should be cleared (to keep all)");
    }

    @Test
    public void testGetadaptresourcefilecontents_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.dontshrink();

        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertFalse(task.configuration.shrink, "dontshrink should remain set");
    }

    @Test
    public void testGetadaptresourcefilecontents_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.getadaptresourcefilecontents();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testGetadaptresourcefilecontents_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testGetadaptresourcefilecontents_withObfuscationEnabled() throws Exception {
        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled by default");
    }

    @Test
    public void testGetadaptresourcefilecontents_withOptimizationEnabled() throws Exception {
        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.optimize, "optimization should be enabled by default");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testGetadaptresourcefilecontents_forAdaptingAllResourceFileContents() throws Exception {
        // Adapt all resource file contents
        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Empty filter means adapt all resource file contents");
    }

    @Test
    public void testGetadaptresourcefilecontents_forPropertiesFiles() throws Exception {
        // Adapt properties files that contain class names
        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testGetadaptresourcefilecontents_forManifestFiles() throws Exception {
        // Adapt manifest files that reference classes
        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
    }

    @Test
    public void testGetadaptresourcefilecontents_forXMLResources() throws Exception {
        // Adapt XML resource files with class references
        task.getadaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all resource file contents");
    }

    @Test
    public void testGetadaptresourcefilecontents_withObfuscation() throws Exception {
        // adaptresourcefilecontents works with obfuscation to update file contents
        task.getadaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all resource file contents");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testGetadaptresourcefilecontents_forConfigurationResources() throws Exception {
        // Adapt resource file contents in configuration directories
        task.getadaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all resource file contents");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testGetadaptresourcefilecontents_afterManuallySet() throws Exception {
        // Manually set adaptResourceFileContents
        task.adaptresourcefilecontents("**.properties");
        assertFalse(task.configuration.adaptResourceFileContents.isEmpty(),
                   "Should have filters after manual set");

        task.getadaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should be cleared after getadaptresourcefilecontents call");
    }

    @Test
    public void testGetadaptresourcefilecontents_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
    }

    @Test
    public void testGetadaptresourcefilecontents_calledFirst() throws Exception {
        // Test calling getadaptresourcefilecontents before any other configuration
        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetadaptresourcefilecontents_calledLast() throws Exception {
        // Test calling getadaptresourcefilecontents after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testGetadaptresourcefilecontents_androidBuild() throws Exception {
        // Android resource files often contain class references
        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
    }

    @Test
    public void testGetadaptresourcefilecontents_androidManifest() throws Exception {
        // Android manifest contains class references
        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
    }

    @Test
    public void testGetadaptresourcefilecontents_androidReleaseBuild() throws Exception {
        // Release build with resource file content adaptation
        task.getadaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all resource file contents");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for release");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testGetadaptresourcefilecontents_beforeVerbose() throws Exception {
        task.getadaptresourcefilecontents();
        task.verbose();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testGetadaptresourcefilecontents_afterVerbose() throws Exception {
        task.verbose();
        task.getadaptresourcefilecontents();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
    }

    @Test
    public void testGetadaptresourcefilecontents_betweenOtherCalls() throws Exception {
        task.verbose();
        task.getadaptresourcefilecontents();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testGetadaptresourcefilecontents_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.getadaptresourcefilecontents();
        task.keepattributes("Signature,Exceptions");
        task.verbose();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testGetadaptresourcefilecontents_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.getadaptresourcefilecontents();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
    }

    @Test
    public void testGetadaptresourcefilecontents_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.getadaptresourcefilecontents();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
    }

    @Test
    public void testGetadaptresourcefilecontents_doesNotAffectOtherFilters() throws Exception {
        task.keeppackagenames("com.example.*");
        task.keepattributes("Signature");

        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames filter should be preserved");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes filter should be preserved");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testGetadaptresourcefilecontents_forPropertiesInJars() throws Exception {
        // Properties files in JARs that contain class names
        task.getadaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all resource file contents");
    }

    @Test
    public void testGetadaptresourcefilecontents_forXMLConfiguration() throws Exception {
        // XML configuration files with class references
        task.getadaptresourcefilecontents();
        task.keepattributes("*Annotation*");

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
    }

    @Test
    public void testGetadaptresourcefilecontents_forServiceFiles() throws Exception {
        // META-INF/services files with class names
        task.getadaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt resource file contents for service files");
    }

    @Test
    public void testGetadaptresourcefilecontents_forManifestEntries() throws Exception {
        // Manifest entries that reference classes
        task.getadaptresourcefilecontents();
        task.keep("public class * { *; }");

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
    }

    @Test
    public void testGetadaptresourcefilecontents_forResourceBundles() throws Exception {
        // Resource bundle files
        task.getadaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all resource bundle file contents");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testGetadaptresourcefilecontents_configurationNotNull() throws Exception {
        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetadaptresourcefilecontents_returnValueConsistency() throws Exception {
        Object result1 = task.getadaptresourcefilecontents();
        Object result2 = task.getadaptresourcefilecontents();

        assertEquals(result1, result2, "Return value should be consistent (both null)");
    }

    @Test
    public void testGetadaptresourcefilecontents_taskStateValid() throws Exception {
        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetadaptresourcefilecontents_withMinimalConfiguration() throws Exception {
        // Test with only getadaptresourcefilecontents called
        Object result = task.getadaptresourcefilecontents();

        assertNull(result, "Should return null");
        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(), "adaptResourceFileContents should be empty");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetadaptresourcefilecontents_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testGetadaptresourcefilecontents_emptyListMeansAdaptAll() throws Exception {
        task.getadaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should not be null");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Empty list means adapt all resource file contents");
    }
}
