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
 * Comprehensive tests for ProGuardTask.getadaptresourcefilenames() method.
 * This is a Groovy DSL getter method that returns null and calls adaptresourcefilenames().
 */
public class ProGuardTaskClaude_getadaptresourcefilenamesTest {

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
    // Tests for getadaptresourcefilenames() Method
    // ========================================

    @Test
    public void testGetadaptresourcefilenames_returnsNull() throws Exception {
        Object result = task.getadaptresourcefilenames();

        assertNull(result, "getadaptresourcefilenames should return null for Groovy DSL support");
    }

    @Test
    public void testGetadaptresourcefilenames_initializesAdaptResourceFileNames() throws Exception {
        assertNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should initially be null");

        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "adaptResourceFileNames should be empty (cleared to keep all)");
    }

    @Test
    public void testGetadaptresourcefilenames_multipleCalls() throws Exception {
        task.getadaptresourcefilenames();
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized after first call");
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(), "adaptResourceFileNames should be empty after first call");

        task.getadaptresourcefilenames();
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should remain initialized after second call");
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(), "adaptResourceFileNames should remain empty after second call");

        task.getadaptresourcefilenames();
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should remain initialized after third call");
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(), "adaptResourceFileNames should remain empty after third call");
    }

    @Test
    public void testGetadaptresourcefilenames_isIdempotent() throws Exception {
        for (int i = 0; i < 5; i++) {
            task.getadaptresourcefilenames();
            assertNotNull(task.configuration.adaptResourceFileNames,
                        "adaptResourceFileNames should be initialized on iteration " + i);
            assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                      "adaptResourceFileNames should be empty on iteration " + i);
        }
    }

    @Test
    public void testGetadaptresourcefilenames_alwaysReturnsNull() throws Exception {
        Object result1 = task.getadaptresourcefilenames();
        Object result2 = task.getadaptresourcefilenames();
        Object result3 = task.getadaptresourcefilenames();

        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
    }

    @Test
    public void testGetadaptresourcefilenames_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getadaptresourcefilenames();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testGetadaptresourcefilenames_clearsExistingFilter() throws Exception {
        // Manually add some filters
        task.adaptresourcefilenames("**.properties");
        assertFalse(task.configuration.adaptResourceFileNames.isEmpty(),
                   "adaptResourceFileNames should contain filters");

        task.getadaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "adaptResourceFileNames should be cleared (to keep all)");
    }

    @Test
    public void testGetadaptresourcefilenames_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.dontshrink();

        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertFalse(task.configuration.shrink, "dontshrink should remain set");
    }

    @Test
    public void testGetadaptresourcefilenames_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.getadaptresourcefilenames();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testGetadaptresourcefilenames_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testGetadaptresourcefilenames_withObfuscationEnabled() throws Exception {
        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled by default");
    }

    @Test
    public void testGetadaptresourcefilenames_withOptimizationEnabled() throws Exception {
        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.optimize, "optimization should be enabled by default");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testGetadaptresourcefilenames_forAdaptingAllResourceFiles() throws Exception {
        // Adapt all resource file names
        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Empty filter means adapt all resource file names");
    }

    @Test
    public void testGetadaptresourcefilenames_forPropertiesFiles() throws Exception {
        // Adapt properties files with obfuscated class names
        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testGetadaptresourcefilenames_forManifestFiles() throws Exception {
        // Adapt manifest files that reference classes
        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testGetadaptresourcefilenames_forXMLResources() throws Exception {
        // Adapt XML resource files with class references
        task.getadaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource files");
    }

    @Test
    public void testGetadaptresourcefilenames_withObfuscation() throws Exception {
        // adaptresourcefilenames works with obfuscation to update resource file names
        task.getadaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource file names");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testGetadaptresourcefilenames_forConfigurationResources() throws Exception {
        // Adapt resource files in configuration directories
        task.getadaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource file names");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testGetadaptresourcefilenames_afterManuallySet() throws Exception {
        // Manually set adaptResourceFileNames
        task.adaptresourcefilenames("**.properties");
        assertFalse(task.configuration.adaptResourceFileNames.isEmpty(),
                   "Should have filters after manual set");

        task.getadaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should be cleared after getadaptresourcefilenames call");
    }

    @Test
    public void testGetadaptresourcefilenames_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testGetadaptresourcefilenames_calledFirst() throws Exception {
        // Test calling getadaptresourcefilenames before any other configuration
        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetadaptresourcefilenames_calledLast() throws Exception {
        // Test calling getadaptresourcefilenames after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testGetadaptresourcefilenames_androidBuild() throws Exception {
        // Android resource files often reference classes
        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
    }

    @Test
    public void testGetadaptresourcefilenames_androidManifest() throws Exception {
        // Android manifest references activities, services, etc.
        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testGetadaptresourcefilenames_androidReleaseBuild() throws Exception {
        // Release build with resource file name adaptation
        task.getadaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource file names");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for release");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testGetadaptresourcefilenames_beforeVerbose() throws Exception {
        task.getadaptresourcefilenames();
        task.verbose();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testGetadaptresourcefilenames_afterVerbose() throws Exception {
        task.verbose();
        task.getadaptresourcefilenames();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testGetadaptresourcefilenames_betweenOtherCalls() throws Exception {
        task.verbose();
        task.getadaptresourcefilenames();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testGetadaptresourcefilenames_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.getadaptresourcefilenames();
        task.keepattributes("Signature,Exceptions");
        task.verbose();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testGetadaptresourcefilenames_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.getadaptresourcefilenames();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testGetadaptresourcefilenames_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.getadaptresourcefilenames();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testGetadaptresourcefilenames_doesNotAffectOtherFilters() throws Exception {
        task.keeppackagenames("com.example.*");
        task.keepattributes("Signature");

        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames filter should be preserved");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes filter should be preserved");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testGetadaptresourcefilenames_forPropertiesInJars() throws Exception {
        // Properties files in JARs that reference classes
        task.getadaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource file names");
    }

    @Test
    public void testGetadaptresourcefilenames_forXMLConfiguration() throws Exception {
        // XML configuration files with class references
        task.getadaptresourcefilenames();
        task.keepattributes("*Annotation*");

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testGetadaptresourcefilenames_forServiceFiles() throws Exception {
        // META-INF/services files with class names
        task.getadaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt resource file names for service files");
    }

    @Test
    public void testGetadaptresourcefilenames_forManifestEntries() throws Exception {
        // Manifest entries that reference classes
        task.getadaptresourcefilenames();
        task.keep("public class * { *; }");

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testGetadaptresourcefilenames_forResourceBundles() throws Exception {
        // Resource bundle files
        task.getadaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource bundle file names");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testGetadaptresourcefilenames_configurationNotNull() throws Exception {
        task.getadaptresourcefilenames();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testGetadaptresourcefilenames_returnValueConsistency() throws Exception {
        Object result1 = task.getadaptresourcefilenames();
        Object result2 = task.getadaptresourcefilenames();

        assertEquals(result1, result2, "Return value should be consistent (both null)");
    }

    @Test
    public void testGetadaptresourcefilenames_taskStateValid() throws Exception {
        task.getadaptresourcefilenames();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testGetadaptresourcefilenames_withMinimalConfiguration() throws Exception {
        // Test with only getadaptresourcefilenames called
        Object result = task.getadaptresourcefilenames();

        assertNull(result, "Should return null");
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(), "adaptResourceFileNames should be empty");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testGetadaptresourcefilenames_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testGetadaptresourcefilenames_emptyListMeansAdaptAll() throws Exception {
        task.getadaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should not be null");
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Empty list means adapt all resource file names");
    }
}
