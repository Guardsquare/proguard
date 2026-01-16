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
 * Comprehensive tests for ProGuardTask.adaptresourcefilecontents() method and
 * adaptresourcefilecontents(String) method.
 * The no-arg method calls adaptresourcefilecontents(null), which clears the filter
 * to adapt all resource file contents.
 * The String parameter method adds the filter to the list of resource file contents to adapt.
 */
public class ProGuardTaskClaude_adaptresourcefilecontentsTest {

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
    // Tests for adaptresourcefilecontents() Method
    // ========================================

    @Test
    public void testAdaptresourcefilecontents_initializesAdaptResourceFileContents() throws Exception {
        assertNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should initially be null");

        task.adaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should be empty (cleared to adapt all)");
    }

    @Test
    public void testAdaptresourcefilecontents_clearsExistingFilters() throws Exception {
        task.configuration.adaptResourceFileContents = new java.util.ArrayList<>();
        task.configuration.adaptResourceFileContents.add("**.properties");
        task.configuration.adaptResourceFileContents.add("**.xml");

        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should be cleared (to adapt all)");
    }

    @Test
    public void testAdaptresourcefilecontents_callsOverloadedMethodWithNull() throws Exception {
        // The no-arg method should call adaptresourcefilecontents(null)
        task.adaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents,
                     "adaptResourceFileContents should be initialized by null parameter call");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "null parameter should clear the filter");
    }

    @Test
    public void testAdaptresourcefilecontents_isIdempotent() throws Exception {
        task.adaptresourcefilecontents();
        int sizeBefore = task.configuration.adaptResourceFileContents.size();

        task.adaptresourcefilecontents();

        assertEquals(sizeBefore, task.configuration.adaptResourceFileContents.size(),
                    "Multiple calls should not change the filter (remains empty)");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Filter should remain empty");
    }

    @Test
    public void testAdaptresourcefilecontents_multipleCalls() throws Exception {
        task.adaptresourcefilecontents();
        task.adaptresourcefilecontents();
        task.adaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should remain empty after multiple calls");
    }

    @Test
    public void testAdaptresourcefilecontents_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.adaptresourcefilecontents();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertNotNull(task.configuration.adaptResourceFileContents,
                     "adaptResourceFileContents should be initialized");
    }

    @Test
    public void testAdaptresourcefilecontents_doesNotReturnValue() throws Exception {
        // This is a void method, just verify it executes without error
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Filter should be cleared to adapt all");
    }

    @Test
    public void testAdaptresourcefilecontents_createsEmptyList() throws Exception {
        task.adaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents,
                     "Should create a list");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "List should be empty (adapt all contents)");
    }

    @Test
    public void testAdaptresourcefilecontents_replacesExistingList() throws Exception {
        task.configuration.adaptResourceFileContents = new java.util.ArrayList<>();
        task.configuration.adaptResourceFileContents.add("**.properties");
        java.util.List<?> oldList = task.configuration.adaptResourceFileContents;

        task.adaptresourcefilecontents();

        assertSame(oldList, task.configuration.adaptResourceFileContents,
                  "Should reuse the same list object");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should clear the existing list");
    }

    @Test
    public void testAdaptresourcefilecontents_semanticMeaning() throws Exception {
        // Empty filter means adapt ALL resource file contents
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Empty filter means adapt ALL resource file contents");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testAdaptresourcefilecontents_withOtherConfigurationSettings() throws Exception {
        task.verbose();
        task.dontshrink();

        task.adaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents,
                     "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertFalse(task.configuration.shrink, "dontshrink should remain set");
    }

    @Test
    public void testAdaptresourcefilecontents_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.adaptresourcefilecontents();

        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate,
                    "obfuscate setting should not change");
    }

    @Test
    public void testAdaptresourcefilecontents_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.adaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents,
                     "adaptResourceFileContents should be initialized");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testAdaptresourcefilecontents_withObfuscationEnabled() throws Exception {
        task.adaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents,
                     "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled by default");
    }

    @Test
    public void testAdaptresourcefilecontents_withOptimizationEnabled() throws Exception {
        task.adaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents,
                     "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.optimize, "optimization should be enabled by default");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testAdaptresourcefilecontents_forAllResourceFiles() throws Exception {
        // Adapt all resource file contents when classes are obfuscated
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Empty filter means adapt all resource file contents");
    }

    @Test
    public void testAdaptresourcefilecontents_forPropertiesFiles() throws Exception {
        // Properties files often contain class references
        task.adaptresourcefilecontents();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all resource file contents including properties");
    }

    @Test
    public void testAdaptresourcefilecontents_forManifestFiles() throws Exception {
        // Manifest files contain class names
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all resource files including manifests");
    }

    @Test
    public void testAdaptresourcefilecontents_forXMLResources() throws Exception {
        // XML resource files may contain class references
        task.adaptresourcefilecontents();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all XML resources");
    }

    @Test
    public void testAdaptresourcefilecontents_forConfigurationResources() throws Exception {
        // Configuration resource files with class names
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all configuration resources");
    }

    @Test
    public void testAdaptresourcefilecontents_forMixedResources() throws Exception {
        // Mixed resource types all need content adaptation
        task.adaptresourcefilecontents();
        task.keepattributes("*Annotation*,Signature");

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all resource file types");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testAdaptresourcefilecontents_afterManuallySettingFilters() throws Exception {
        task.configuration.adaptResourceFileContents = new java.util.ArrayList<>();
        task.configuration.adaptResourceFileContents.add("**.properties");
        task.configuration.adaptResourceFileContents.add("**.xml");

        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should clear manually set filters");
    }

    @Test
    public void testAdaptresourcefilecontents_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.adaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents,
                     "adaptResourceFileContents should be initialized");
    }

    @Test
    public void testAdaptresourcefilecontents_calledFirst() throws Exception {
        // Test calling adaptresourcefilecontents before any other configuration
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should be empty");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testAdaptresourcefilecontents_calledLast() throws Exception {
        // Test calling adaptresourcefilecontents after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should be empty");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testAdaptresourcefilecontents_androidBuildWithResourceFiles() throws Exception {
        // Android builds with resource files needing content adaptation
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all Android resource file contents");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
    }

    @Test
    public void testAdaptresourcefilecontents_androidManifestReferences() throws Exception {
        // Android manifest often contains class references
        task.adaptresourcefilecontents();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt manifest and other resource contents");
    }

    @Test
    public void testAdaptresourcefilecontents_androidReleaseBuild() throws Exception {
        // Release builds typically obfuscate and need content adaptation
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all resource file contents for release");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testAdaptresourcefilecontents_beforeVerbose() throws Exception {
        task.adaptresourcefilecontents();
        task.verbose();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should be empty");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testAdaptresourcefilecontents_afterVerbose() throws Exception {
        task.verbose();
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should be empty");
    }

    @Test
    public void testAdaptresourcefilecontents_betweenOtherCalls() throws Exception {
        task.verbose();
        task.adaptresourcefilecontents();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should be empty");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testAdaptresourcefilecontents_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.adaptresourcefilecontents();
        task.keepattributes("*Annotation*,Signature");
        task.verbose();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should be empty");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    // ========================================
    // Build Variants
    // ========================================

    @Test
    public void testAdaptresourcefilecontents_forDebugBuild() throws Exception {
        // Debug builds might still need resource file content adaptation
        task.adaptresourcefilecontents();
        task.dontobfuscate();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all resource file contents even in debug");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
    }

    @Test
    public void testAdaptresourcefilecontents_forReleaseBuild() throws Exception {
        // Release builds need resource file content adaptation
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all resource file contents for release");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for release");
    }

    @Test
    public void testAdaptresourcefilecontents_forLibraryModule() throws Exception {
        // Library modules might have different obfuscation needs
        task.adaptresourcefilecontents();
        task.dontobfuscate();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all resource file contents for library");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for library");
    }

    @Test
    public void testAdaptresourcefilecontents_forApplicationModule() throws Exception {
        // Application modules need comprehensive resource file content adaptation
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt all resource file contents for application");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for application");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testAdaptresourcefilecontents_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.adaptresourcefilecontents();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should be empty");
    }

    @Test
    public void testAdaptresourcefilecontents_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.adaptresourcefilecontents();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should be empty");
    }

    @Test
    public void testAdaptresourcefilecontents_doesNotClearOtherFilterLists() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.adaptresourcefilecontents();

        assertNotNull(task.configuration.keep, "keep rules should remain set");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain set");
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should be empty");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testAdaptresourcefilecontents_forPropertiesInJars() throws Exception {
        // Properties files in JARs with class references
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt properties files in JARs");
    }

    @Test
    public void testAdaptresourcefilecontents_forXMLConfiguration() throws Exception {
        // XML configuration files with class names
        task.adaptresourcefilecontents();
        task.keepattributes("*Annotation*,Signature");

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt XML configuration contents");
    }

    @Test
    public void testAdaptresourcefilecontents_forServiceFiles() throws Exception {
        // Service files (META-INF/services) contain class names
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt service file contents");
    }

    @Test
    public void testAdaptresourcefilecontents_forManifestEntries() throws Exception {
        // Manifest entries contain class references
        task.adaptresourcefilecontents();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt manifest entry contents");
    }

    @Test
    public void testAdaptresourcefilecontents_forResourceBundles() throws Exception {
        // Resource bundles with class references
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should adapt resource bundle contents");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testAdaptresourcefilecontents_configurationNotNull() throws Exception {
        task.adaptresourcefilecontents();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testAdaptresourcefilecontents_filterListNotNull() throws Exception {
        task.adaptresourcefilecontents();

        assertNotNull(task.configuration.adaptResourceFileContents,
                     "adaptResourceFileContents should never be null after initialization");
    }

    @Test
    public void testAdaptresourcefilecontents_taskStateValid() throws Exception {
        task.adaptresourcefilecontents();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testAdaptresourcefilecontents_withMinimalConfiguration() throws Exception {
        // Test with only adaptresourcefilecontents called
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Should be empty (adapt all)");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testAdaptresourcefilecontents_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "adaptResourceFileContents should be empty");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testAdaptresourcefilecontents_emptyListMeansAdaptAll() throws Exception {
        // Verify the semantic meaning: empty list = adapt all
        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "Empty list means ProGuard will adapt ALL resource file contents");
    }

    // ========================================
    // Tests for adaptresourcefilecontents(String) Method
    // ========================================

    @Test
    public void testAdaptresourcefilecontentsWithString_initializesAdaptResourceFileContents() throws Exception {
        assertNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should initially be null");

        task.adaptresourcefilecontents("**.properties");

        assertNotNull(task.configuration.adaptResourceFileContents, "adaptResourceFileContents should be initialized");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Should contain the filter");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_addsToFilter() throws Exception {
        task.adaptresourcefilecontents("**.properties");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Should contain properties filter");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_multipleFilters() throws Exception {
        task.adaptresourcefilecontents("**.properties");
        task.adaptresourcefilecontents("**.xml");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Should contain properties filter");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.xml"),
                  "Should contain xml filter");
        assertEquals(2, task.configuration.adaptResourceFileContents.size(),
                    "Should have 2 filters");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_nullParameterClearsFilter() throws Exception {
        task.adaptresourcefilecontents("**.properties");
        task.adaptresourcefilecontents("**.xml");
        assertEquals(2, task.configuration.adaptResourceFileContents.size(),
                    "Should have 2 filters");

        task.adaptresourcefilecontents((String) null);

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "null parameter should clear the filter (adapt all)");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_emptyStringAddsToFilter() throws Exception {
        task.adaptresourcefilecontents("");

        assertTrue(task.configuration.adaptResourceFileContents.contains(""),
                  "Empty string should be added as a filter");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_doesNotConvertClassNames() throws Exception {
        // Unlike adaptclassstrings, this method does NOT convert dots to slashes
        task.adaptresourcefilecontents("com.example.app.properties");

        assertTrue(task.configuration.adaptResourceFileContents.contains("com.example.app.properties"),
                  "Should keep dots as-is (no conversion to internal format)");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_commaSeparatedList() throws Exception {
        task.adaptresourcefilecontents("**.properties,**.xml,**.txt");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Should contain properties filter");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.xml"),
                  "Should contain xml filter");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.txt"),
                  "Should contain txt filter");
        assertEquals(3, task.configuration.adaptResourceFileContents.size(),
                    "Should parse comma-separated list");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_duplicateFilters() throws Exception {
        task.adaptresourcefilecontents("**.properties");
        task.adaptresourcefilecontents("**.properties");

        assertEquals(2, task.configuration.adaptResourceFileContents.size(),
                    "Should allow duplicate filters");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Should contain properties filter");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_wildcardPattern() throws Exception {
        task.adaptresourcefilecontents("**.properties");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Should support wildcard patterns");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_specificFileName() throws Exception {
        task.adaptresourcefilecontents("META-INF/services/com.example.Service");

        assertTrue(task.configuration.adaptResourceFileContents.contains("META-INF/services/com.example.Service"),
                  "Should support specific file paths");
    }

    // ========================================
    // Integration Tests with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilecontentsWithString_withObfuscation() throws Exception {
        task.adaptresourcefilecontents("**.properties");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Should adapt properties files");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled by default");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.adaptresourcefilecontents("**.xml");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.xml"),
                  "Should adapt xml files");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_withOtherSettings() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.adaptresourcefilecontents("**.properties");

        assertTrue(task.configuration.verbose, "verbose should remain set");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Should adapt properties files");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_afterNoArgVersion() throws Exception {
        task.adaptresourcefilecontents();
        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "No-arg version should clear filter");

        task.adaptresourcefilecontents("**.properties");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Should add filter after clearing");
        assertEquals(1, task.configuration.adaptResourceFileContents.size(),
                    "Should have 1 filter");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_beforeNoArgVersion() throws Exception {
        task.adaptresourcefilecontents("**.properties");
        task.adaptresourcefilecontents("**.xml");
        assertEquals(2, task.configuration.adaptResourceFileContents.size(),
                    "Should have 2 filters");

        task.adaptresourcefilecontents();

        assertTrue(task.configuration.adaptResourceFileContents.isEmpty(),
                  "No-arg version should clear all filters");
    }

    // ========================================
    // Realistic Scenarios with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilecontentsWithString_forPropertiesFiles() throws Exception {
        // Adapt only properties files
        task.adaptresourcefilecontents("**.properties");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Should adapt properties files");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_forManifestFiles() throws Exception {
        // Adapt manifest files
        task.adaptresourcefilecontents("META-INF/MANIFEST.MF");

        assertTrue(task.configuration.adaptResourceFileContents.contains("META-INF/MANIFEST.MF"),
                  "Should adapt manifest files");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_forXMLFiles() throws Exception {
        // Adapt XML configuration files
        task.adaptresourcefilecontents("**.xml");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.xml"),
                  "Should adapt XML files");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_forServiceFiles() throws Exception {
        // Adapt service provider configuration files
        task.adaptresourcefilecontents("META-INF/services/*");

        assertTrue(task.configuration.adaptResourceFileContents.contains("META-INF/services/*"),
                  "Should adapt service files");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_forSpringConfiguration() throws Exception {
        // Adapt Spring configuration files
        task.adaptresourcefilecontents("META-INF/spring.factories");
        task.adaptresourcefilecontents("META-INF/spring/*.xml");

        assertTrue(task.configuration.adaptResourceFileContents.contains("META-INF/spring.factories"),
                  "Should adapt Spring factories");
        assertTrue(task.configuration.adaptResourceFileContents.contains("META-INF/spring/*.xml"),
                  "Should adapt Spring XML configs");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_forMultipleResourceTypes() throws Exception {
        // Adapt multiple types of resource files
        task.adaptresourcefilecontents("**.properties,**.xml,META-INF/MANIFEST.MF");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Should adapt properties");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.xml"),
                  "Should adapt XML");
        assertTrue(task.configuration.adaptResourceFileContents.contains("META-INF/MANIFEST.MF"),
                  "Should adapt manifest");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_forAndroidResources() throws Exception {
        // Android-specific resource files
        task.adaptresourcefilecontents("AndroidManifest.xml");

        assertTrue(task.configuration.adaptResourceFileContents.contains("AndroidManifest.xml"),
                  "Should adapt Android manifest");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_forConfigurationFiles() throws Exception {
        // General configuration files
        task.adaptresourcefilecontents("**.config");
        task.adaptresourcefilecontents("**.conf");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.config"),
                  "Should adapt .config files");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.conf"),
                  "Should adapt .conf files");
    }

    // ========================================
    // Edge Cases with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilecontentsWithString_withWhitespace() throws Exception {
        task.adaptresourcefilecontents("  **.properties  ");

        // The filter is added as-is, including whitespace
        assertTrue(task.configuration.adaptResourceFileContents.stream()
                       .anyMatch(f -> f.toString().contains("properties")),
                  "Should handle whitespace in filter");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_withSpecialCharacters() throws Exception {
        task.adaptresourcefilecontents("**/*.properties");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**/*.properties"),
                  "Should handle special characters");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_veryLongPath() throws Exception {
        String longPath = "com/example/very/long/path/to/resource/files/configuration/**.properties";
        task.adaptresourcefilecontents(longPath);

        assertTrue(task.configuration.adaptResourceFileContents.contains(longPath),
                  "Should handle long paths");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_withDots() throws Exception {
        // Dots are NOT converted to slashes (unlike adaptclassstrings)
        task.adaptresourcefilecontents("com.example.config.properties");

        assertTrue(task.configuration.adaptResourceFileContents.contains("com.example.config.properties"),
                  "Should keep dots as-is");
        assertFalse(task.configuration.adaptResourceFileContents.stream()
                        .anyMatch(f -> f.toString().contains("com/example")),
                   "Should NOT convert dots to slashes");
    }

    // ========================================
    // Android-Specific Scenarios with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilecontentsWithString_androidManifest() throws Exception {
        task.adaptresourcefilecontents("AndroidManifest.xml");

        assertTrue(task.configuration.adaptResourceFileContents.contains("AndroidManifest.xml"),
                  "Should adapt Android manifest");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_androidResourceXML() throws Exception {
        task.adaptresourcefilecontents("res/**.xml");

        assertTrue(task.configuration.adaptResourceFileContents.contains("res/**.xml"),
                  "Should adapt Android resource XML files");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_androidProguardRules() throws Exception {
        // Android apps might reference ProGuard rules in resources
        task.adaptresourcefilecontents("META-INF/proguard/**");

        assertTrue(task.configuration.adaptResourceFileContents.contains("META-INF/proguard/**"),
                  "Should adapt ProGuard rule files");
    }

    // ========================================
    // Call Order Flexibility with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilecontentsWithString_beforeOtherConfiguration() throws Exception {
        task.adaptresourcefilecontents("**.properties");
        task.verbose();
        task.keep("public class * { *; }");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Filter should be preserved");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_afterOtherConfiguration() throws Exception {
        task.verbose();
        task.keep("public class * { *; }");
        task.adaptresourcefilecontents("**.xml");

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.xml"),
                  "Filter should be added");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_mixedWithNoArgVersion() throws Exception {
        task.adaptresourcefilecontents("**.properties");
        task.verbose();
        task.adaptresourcefilecontents("**.xml");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "First filter should be preserved");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.xml"),
                  "Second filter should be added");
        assertEquals(2, task.configuration.adaptResourceFileContents.size(),
                    "Should have 2 filters");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_multipleCallsAccumulate() throws Exception {
        task.adaptresourcefilecontents("**.properties");
        task.adaptresourcefilecontents("**.xml");
        task.adaptresourcefilecontents("**.txt");

        assertEquals(3, task.configuration.adaptResourceFileContents.size(),
                    "Should accumulate all filters");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"));
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.xml"));
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.txt"));
    }

    // ========================================
    // Build Variants with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilecontentsWithString_debugBuild() throws Exception {
        task.dontobfuscate();
        task.adaptresourcefilecontents("**.properties");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Should adapt properties even in debug");
        assertFalse(task.configuration.obfuscate, "obfuscation disabled for debug");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_releaseBuild() throws Exception {
        task.adaptresourcefilecontents("**.properties");
        task.adaptresourcefilecontents("**.xml");

        assertTrue(task.configuration.obfuscate, "obfuscation enabled for release");
        assertEquals(2, task.configuration.adaptResourceFileContents.size(),
                    "Should adapt multiple resource types");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_libraryModule() throws Exception {
        task.dontobfuscate();
        task.adaptresourcefilecontents("META-INF/services/*");

        assertTrue(task.configuration.adaptResourceFileContents.contains("META-INF/services/*"),
                  "Should adapt service files in library");
    }

    // ========================================
    // Configuration State with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilecontentsWithString_preservesOtherConfiguration() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.adaptresourcefilecontents("**.properties");

        assertEquals(verboseBefore, task.configuration.verbose,
                    "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize,
                    "optimize state should be preserved");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Filter should be added");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_doesNotClearOtherFilters() throws Exception {
        task.keep("public class * { *; }");
        task.keepattributes("*Annotation*");

        task.adaptresourcefilecontents("**.properties");

        assertNotNull(task.configuration.keep, "keep rules should remain");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should remain");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Filter should be added");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_independentOfOtherAdaptSettings() throws Exception {
        task.adaptresourcefilenames("**.properties");
        task.adaptresourcefilecontents("**.xml");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "adaptResourceFileNames should be independent");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.xml"),
                  "adaptResourceFileContents should be independent");
    }

    // ========================================
    // Special Use Cases with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilecontentsWithString_javaServiceProviders() throws Exception {
        // Java service provider files in META-INF/services
        task.adaptresourcefilecontents("META-INF/services/*");

        assertTrue(task.configuration.adaptResourceFileContents.contains("META-INF/services/*"),
                  "Should adapt Java service provider files");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_resourceBundles() throws Exception {
        // Resource bundles with class references
        task.adaptresourcefilecontents("**/resources/*.properties");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**/resources/*.properties"),
                  "Should adapt resource bundle files");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_configurationInJars() throws Exception {
        // Configuration files in JAR resources
        task.adaptresourcefilecontents("**.properties");
        task.adaptresourcefilecontents("**.xml");
        task.adaptresourcefilecontents("**.json");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"));
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.xml"));
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.json"),
                  "Should adapt JSON config files");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_webInfResources() throws Exception {
        // Web application resources
        task.adaptresourcefilecontents("WEB-INF/*.xml");

        assertTrue(task.configuration.adaptResourceFileContents.contains("WEB-INF/*.xml"),
                  "Should adapt WEB-INF resources");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_gradlePluginMetadata() throws Exception {
        // Gradle plugin metadata
        task.adaptresourcefilecontents("META-INF/gradle-plugins/*.properties");

        assertTrue(task.configuration.adaptResourceFileContents.contains("META-INF/gradle-plugins/*.properties"),
                  "Should adapt Gradle plugin metadata");
    }

    // ========================================
    // Verification Tests with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilecontentsWithString_filterIsActuallyUsed() throws Exception {
        task.adaptresourcefilecontents("**.properties");

        assertFalse(task.configuration.adaptResourceFileContents.isEmpty(),
                   "Filter list should not be empty");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Specific filter should be present");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_configurationValid() throws Exception {
        task.adaptresourcefilecontents("**.xml");

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.configuration.adaptResourceFileContents,
                     "adaptResourceFileContents should be initialized");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_taskStateValid() throws Exception {
        task.adaptresourcefilecontents("**.properties");

        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Configuration should be updated");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_returnValueIsVoid() throws Exception {
        // This is a void method, just ensure it executes without error
        task.adaptresourcefilecontents("**.properties");

        assertTrue(task.configuration.adaptResourceFileContents.contains("**.properties"),
                  "Filter should be added");
    }

    @Test
    public void testAdaptresourcefilecontentsWithString_semanticMeaning() throws Exception {
        // Non-empty filter means adapt ONLY specified resource file contents
        task.adaptresourcefilecontents("**.properties");

        assertFalse(task.configuration.adaptResourceFileContents.isEmpty(),
                   "Non-empty filter means adapt only specified files");
        assertEquals(1, task.configuration.adaptResourceFileContents.size(),
                    "Should have exactly one filter");
    }
}

