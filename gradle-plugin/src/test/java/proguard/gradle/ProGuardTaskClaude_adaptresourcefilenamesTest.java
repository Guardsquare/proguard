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
 * Comprehensive tests for ProGuardTask.adaptresourcefilenames() method.
 * This method initializes/clears the adaptResourceFileNames filter to adapt all resource file names.
 */
public class ProGuardTaskClaude_adaptresourcefilenamesTest {

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
    // Tests for adaptresourcefilenames() Method
    // ========================================

    @Test
    public void testAdaptresourcefilenames_initializesAdaptResourceFileNames() throws Exception {
        assertNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should initially be null");

        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "adaptResourceFileNames should be empty (cleared to keep all)");
    }

    @Test
    public void testAdaptresourcefilenames_isVoid() throws Exception {
        // Verify method doesn't return a value (void method)
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_multipleCalls() throws Exception {
        task.adaptresourcefilenames();
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized after first call");
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(), "adaptResourceFileNames should be empty after first call");

        task.adaptresourcefilenames();
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should remain initialized after second call");
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(), "adaptResourceFileNames should remain empty after second call");

        task.adaptresourcefilenames();
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should remain initialized after third call");
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(), "adaptResourceFileNames should remain empty after third call");
    }

    @Test
    public void testAdaptresourcefilenames_isIdempotent() throws Exception {
        for (int i = 0; i < 10; i++) {
            task.adaptresourcefilenames();
            assertNotNull(task.configuration.adaptResourceFileNames,
                        "adaptResourceFileNames should be initialized on iteration " + i);
            assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                      "adaptResourceFileNames should be empty on iteration " + i);
        }
    }

    @Test
    public void testAdaptresourcefilenames_clearsExistingFilter() throws Exception {
        // Manually add some filters
        task.adaptresourcefilenames("**.properties");
        assertFalse(task.configuration.adaptResourceFileNames.isEmpty(),
                   "adaptResourceFileNames should contain filters");

        task.adaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "adaptResourceFileNames should be cleared (to keep all)");
    }

    @Test
    public void testAdaptresourcefilenames_configurationInitialized() throws Exception {
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.adaptresourcefilenames();

        assertNotNull(task.configuration, "Configuration should still be initialized");
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_doesNotReturnValue() throws Exception {
        // This is a void method - just verify it executes without issues
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_emptyListMeansAdaptAll() throws Exception {
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should not be null");
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Empty list means adapt all resource file names");
    }

    // ========================================
    // Integration Tests
    // ========================================

    @Test
    public void testAdaptresourcefilenames_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testAdaptresourcefilenames_withObfuscationEnabled() throws Exception {
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled by default");
    }

    @Test
    public void testAdaptresourcefilenames_withObfuscationDisabled() throws Exception {
        task.dontobfuscate();
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testAdaptresourcefilenames_withOptimizationEnabled() throws Exception {
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.optimize, "optimization should be enabled by default");
    }

    @Test
    public void testAdaptresourcefilenames_withOptimizationDisabled() throws Exception {
        task.dontoptimize();
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
    }

    @Test
    public void testAdaptresourcefilenames_withShrinkingDisabled() throws Exception {
        task.dontshrink();
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertFalse(task.configuration.shrink, "shrinking should be disabled");
    }

    @Test
    public void testAdaptresourcefilenames_withVerboseEnabled() throws Exception {
        task.verbose();
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testAdaptresourcefilenames_withKeepAttributes() throws Exception {
        task.keepattributes("Signature,Exceptions");
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes should be set");
    }

    @Test
    public void testAdaptresourcefilenames_doesNotAffectOtherSettings() throws Exception {
        task.dontoptimize();
        task.dontobfuscate();
        boolean optimizeBefore = task.configuration.optimize;
        boolean obfuscateBefore = task.configuration.obfuscate;

        task.adaptresourcefilenames();

        assertEquals(optimizeBefore, task.configuration.optimize, "optimize setting should not change");
        assertEquals(obfuscateBefore, task.configuration.obfuscate, "obfuscate setting should not change");
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    // ========================================
    // Realistic Scenarios
    // ========================================

    @Test
    public void testAdaptresourcefilenames_forAdaptingAllResourceFiles() throws Exception {
        // Adapt all resource file names
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Empty filter means adapt all resource file names");
    }

    @Test
    public void testAdaptresourcefilenames_forPropertiesFiles() throws Exception {
        // Adapt properties files with obfuscated class names
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testAdaptresourcefilenames_forManifestFiles() throws Exception {
        // Adapt manifest files that reference classes
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_forXMLResources() throws Exception {
        // Adapt XML resource files with class references
        task.adaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource files");
    }

    @Test
    public void testAdaptresourcefilenames_withObfuscation() throws Exception {
        // adaptresourcefilenames works with obfuscation to update resource file names
        task.adaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource file names");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testAdaptresourcefilenames_forConfigurationResources() throws Exception {
        // Adapt resource files in configuration directories
        task.adaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource file names");
    }

    @Test
    public void testAdaptresourcefilenames_forPropertiesInJars() throws Exception {
        // Properties files in JARs that reference classes
        task.adaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource file names");
    }

    @Test
    public void testAdaptresourcefilenames_forXMLConfiguration() throws Exception {
        // XML configuration files with class references
        task.adaptresourcefilenames();
        task.keepattributes("*Annotation*");

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_forServiceFiles() throws Exception {
        // META-INF/services files with class names
        task.adaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt resource file names for service files");
    }

    @Test
    public void testAdaptresourcefilenames_forManifestEntries() throws Exception {
        // Manifest entries that reference classes
        task.adaptresourcefilenames();
        task.keep("public class * { *; }");

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_forResourceBundles() throws Exception {
        // Resource bundle files
        task.adaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource bundle file names");
    }

    // ========================================
    // Edge Cases
    // ========================================

    @Test
    public void testAdaptresourcefilenames_calledFirst() throws Exception {
        // Test calling adaptresourcefilenames before any other configuration
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_calledLast() throws Exception {
        // Test calling adaptresourcefilenames after other configuration
        task.verbose();
        task.keep("public class * { *; }");
        task.dontoptimize();

        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.libraryjars("lib.jar");
        task.keep("public class * { *; }");
        task.adaptresourcefilenames();
        task.keepattributes("Signature,Exceptions");
        task.verbose();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.keep, "keep rules should be set");
    }

    @Test
    public void testAdaptresourcefilenames_withNullConfiguration() throws Exception {
        // Configuration should never be null, but test defensive behavior
        assertNotNull(task.configuration, "Configuration should be initialized");

        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    // ========================================
    // Android-Specific Scenarios
    // ========================================

    @Test
    public void testAdaptresourcefilenames_androidBuild() throws Exception {
        // Android resource files often reference classes
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
    }

    @Test
    public void testAdaptresourcefilenames_androidManifest() throws Exception {
        // Android manifest references activities, services, etc.
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_androidReleaseBuild() throws Exception {
        // Release build with resource file name adaptation
        task.adaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource file names");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled for release");
    }

    @Test
    public void testAdaptresourcefilenames_androidDebugBuild() throws Exception {
        // Debug build might also need resource file name adaptation
        task.adaptresourcefilenames();
        task.dontobfuscate();
        task.dontoptimize();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource file names");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for debug");
        assertFalse(task.configuration.optimize, "optimization should be disabled for debug");
    }

    // ========================================
    // Call Order Flexibility Tests
    // ========================================

    @Test
    public void testAdaptresourcefilenames_beforeVerbose() throws Exception {
        task.adaptresourcefilenames();
        task.verbose();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    @Test
    public void testAdaptresourcefilenames_afterVerbose() throws Exception {
        task.verbose();
        task.adaptresourcefilenames();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_betweenOtherCalls() throws Exception {
        task.verbose();
        task.adaptresourcefilenames();
        task.dontoptimize();

        assertTrue(task.configuration.verbose, "verbose should be set");
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testAdaptresourcefilenames_multipleTimesWithOtherCalls() throws Exception {
        task.adaptresourcefilenames();
        task.verbose();
        task.adaptresourcefilenames();
        task.dontoptimize();
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should be set");
        assertFalse(task.configuration.optimize, "optimize should be disabled");
    }

    @Test
    public void testAdaptresourcefilenames_withAllProcessingSteps() throws Exception {
        // Test with all processing steps configured
        task.adaptresourcefilenames();
        // Shrinking, optimization, obfuscation all enabled by default

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.shrink, "shrinking should be enabled");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    // ========================================
    // Build Variants
    // ========================================

    @Test
    public void testAdaptresourcefilenames_debugVariant() throws Exception {
        // Debug variant with resource file name adaptation
        task.adaptresourcefilenames();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertFalse(task.configuration.shrink, "shrinking should be disabled");
        assertFalse(task.configuration.optimize, "optimization should be disabled");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled");
    }

    @Test
    public void testAdaptresourcefilenames_releaseVariant() throws Exception {
        // Typical release variant configuration
        task.adaptresourcefilenames();
        // All processing enabled by default

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource file names");
        assertTrue(task.configuration.shrink, "shrinking should be enabled");
        assertTrue(task.configuration.optimize, "optimization should be enabled");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testAdaptresourcefilenames_stagingVariant() throws Exception {
        // Staging variant: optimized but somewhat debuggable
        task.adaptresourcefilenames();
        task.dontobfuscate();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource file names");
        assertFalse(task.configuration.obfuscate, "obfuscation should be disabled for staging");
    }

    // ========================================
    // Configuration State Tests
    // ========================================

    @Test
    public void testAdaptresourcefilenames_configurationStatePreserved() throws Exception {
        task.verbose();
        task.dontoptimize();
        task.keep("public class * { *; }");

        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.adaptresourcefilenames();

        assertEquals(verboseBefore, task.configuration.verbose, "verbose state should be preserved");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize state should be preserved");
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_doesNotClearOtherBooleanFlags() throws Exception {
        task.allowaccessmodification();
        task.forceprocessing();
        task.mergeinterfacesaggressively();

        task.adaptresourcefilenames();

        assertTrue(task.configuration.allowAccessModification, "allowAccessModification should remain set");
        assertTrue(task.configuration.lastModified == Long.MAX_VALUE, "forceprocessing should remain set");
        assertTrue(task.configuration.mergeInterfacesAggressively, "mergeInterfacesAggressively should remain set");
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_doesNotAffectOtherFilters() throws Exception {
        task.keeppackagenames("com.example.*");
        task.keepattributes("Signature");

        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames filter should be preserved");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes filter should be preserved");
    }

    @Test
    public void testAdaptresourcefilenames_doesNotAffectOtherStringFields() throws Exception {
        task.configuration.flattenPackageHierarchy = "com/example";
        task.configuration.repackageClasses = "com/obf";

        task.adaptresourcefilenames();

        assertEquals("com/example", task.configuration.flattenPackageHierarchy,
                    "flattenPackageHierarchy should be preserved");
        assertEquals("com/obf", task.configuration.repackageClasses,
                    "repackageClasses should be preserved");
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    // ========================================
    // Special Use Cases
    // ========================================

    @Test
    public void testAdaptresourcefilenames_forSpringConfiguration() throws Exception {
        // Spring configuration files with class references
        task.adaptresourcefilenames();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt all resource files for Spring");
    }

    @Test
    public void testAdaptresourcefilenames_forHibernateMapping() throws Exception {
        // Hibernate mapping files with class references
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_forJAXBBindings() throws Exception {
        // JAXB binding files
        task.adaptresourcefilenames();
        task.keepattributes("*Annotation*");

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt resource files for JAXB");
    }

    @Test
    public void testAdaptresourcefilenames_forPersistenceXML() throws Exception {
        // JPA persistence.xml with entity class references
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_forWebXML() throws Exception {
        // web.xml with servlet class references
        task.adaptresourcefilenames();
        task.keep("public class * extends javax.servlet.** { *; }");

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should adapt web.xml resource files");
    }

    // ========================================
    // Verification Tests
    // ========================================

    @Test
    public void testAdaptresourcefilenames_configurationNotNull() throws Exception {
        task.adaptresourcefilenames();

        assertNotNull(task.configuration, "Configuration should never be null");
    }

    @Test
    public void testAdaptresourcefilenames_taskStateValid() throws Exception {
        task.adaptresourcefilenames();

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testAdaptresourcefilenames_withMinimalConfiguration() throws Exception {
        // Test with only adaptresourcefilenames called
        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(), "adaptResourceFileNames should be empty");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    @Test
    public void testAdaptresourcefilenames_withMaximalConfiguration() throws Exception {
        // Test with extensive configuration
        task.verbose();
        task.dontshrink();
        task.dontoptimize();
        task.dontobfuscate();
        task.keep("public class * { *; }");
        task.keepattributes("*");
        task.allowaccessmodification();

        task.adaptresourcefilenames();

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    @Test
    public void testAdaptresourcefilenames_repeatedConfiguration() throws Exception {
        // Test setting and resetting configuration
        task.adaptresourcefilenames();
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(), "Should be empty after first call");

        task.adaptresourcefilenames("**.properties");
        assertFalse(task.configuration.adaptResourceFileNames.isEmpty(), "Should have filters after adding");

        task.adaptresourcefilenames();
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(), "Should be empty again after second call");
    }

    // ========================================
    // Tests for adaptresourcefilenames(String) Method
    // ========================================

    @Test
    public void testAdaptresourcefilenamesWithString_singlePattern() throws Exception {
        task.adaptresourcefilenames("**.properties");

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertFalse(task.configuration.adaptResourceFileNames.isEmpty(), "adaptResourceFileNames should contain filter");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "Should contain properties pattern");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_xmlFiles() throws Exception {
        task.adaptresourcefilenames("**.xml");

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.xml"),
                  "Should contain XML pattern");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_multipleCallsAccumulate() throws Exception {
        task.adaptresourcefilenames("**.properties");
        task.adaptresourcefilenames("**.xml");

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "Should contain first filter");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.xml"),
                  "Should contain second filter");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_commaSeparatedList() throws Exception {
        task.adaptresourcefilenames("**.properties,**.xml,**.json");

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "Should contain properties pattern");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.xml"),
                  "Should contain XML pattern");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.json"),
                  "Should contain JSON pattern");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_nullClearsFilter() throws Exception {
        task.adaptresourcefilenames("**.properties");
        assertFalse(task.configuration.adaptResourceFileNames.isEmpty(),
                   "Should have filters after adding");

        task.adaptresourcefilenames(null);

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Null should clear the filter (adapt all)");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_afterNoArgVersion() throws Exception {
        task.adaptresourcefilenames();
        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should be empty after no-arg call");

        task.adaptresourcefilenames("**.properties");

        assertFalse(task.configuration.adaptResourceFileNames.isEmpty(),
                   "Should now contain filters");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "Should contain the added filter");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_beforeNoArgVersion() throws Exception {
        task.adaptresourcefilenames("**.properties");
        assertFalse(task.configuration.adaptResourceFileNames.isEmpty(),
                   "Should have filters after String call");

        task.adaptresourcefilenames();

        assertTrue(task.configuration.adaptResourceFileNames.isEmpty(),
                  "Should be cleared by no-arg call");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_specificDirectory() throws Exception {
        task.adaptresourcefilenames("META-INF/*.properties");

        assertTrue(task.configuration.adaptResourceFileNames.contains("META-INF/*.properties"),
                  "Should support directory-specific patterns");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_multiplePatterns() throws Exception {
        task.adaptresourcefilenames("**/config/*.xml");
        task.adaptresourcefilenames("**/resources/*.properties");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**/config/*.xml"),
                  "Should contain config XML pattern");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**/resources/*.properties"),
                  "Should contain resources properties pattern");
    }

    // ========================================
    // Integration Tests with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilenamesWithString_withKeepRules() throws Exception {
        task.keep("public class * { *; }");
        task.adaptresourcefilenames("**.properties");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "adaptResourceFileNames should contain filter");
        assertNotNull(task.configuration.keep, "keep rules should be preserved");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_withObfuscation() throws Exception {
        task.adaptresourcefilenames("**.xml");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**.xml"),
                  "adaptResourceFileNames should contain filter");
        assertTrue(task.configuration.obfuscate, "obfuscation should be enabled");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_withVerbose() throws Exception {
        task.verbose();
        task.adaptresourcefilenames("**.properties");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "adaptResourceFileNames should contain filter");
        assertTrue(task.configuration.verbose, "verbose should remain set");
    }

    // ========================================
    // Realistic Scenarios with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilenamesWithString_forPropertiesFiles() throws Exception {
        // Adapt only properties files
        task.adaptresourcefilenames("**.properties");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "Should adapt properties files");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_forXMLConfiguration() throws Exception {
        // Adapt XML configuration files
        task.adaptresourcefilenames("**.xml");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**.xml"),
                  "Should adapt XML configuration files");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_forManifestFiles() throws Exception {
        // Adapt manifest files
        task.adaptresourcefilenames("META-INF/MANIFEST.MF");

        assertTrue(task.configuration.adaptResourceFileNames.contains("META-INF/MANIFEST.MF"),
                  "Should adapt manifest files");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_forServiceFiles() throws Exception {
        // Adapt service provider files
        task.adaptresourcefilenames("META-INF/services/*");

        assertTrue(task.configuration.adaptResourceFileNames.contains("META-INF/services/*"),
                  "Should adapt service files");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_forMultipleFileTypes() throws Exception {
        // Adapt multiple resource file types
        task.adaptresourcefilenames("**.properties,**.xml,META-INF/services/*");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "Should adapt properties files");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.xml"),
                  "Should adapt XML files");
        assertTrue(task.configuration.adaptResourceFileNames.contains("META-INF/services/*"),
                  "Should adapt service files");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_excludingSystemFiles() throws Exception {
        // Adapt application resource files but not system files
        task.adaptresourcefilenames("com/myapp/**.properties");

        assertTrue(task.configuration.adaptResourceFileNames.contains("com/myapp/**.properties"),
                  "Should adapt only application resource files");
    }

    // ========================================
    // Edge Cases with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilenamesWithString_emptyString() throws Exception {
        task.adaptresourcefilenames("");

        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should be initialized");
        assertTrue(task.configuration.adaptResourceFileNames.contains(""),
                  "Should contain empty string");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_duplicateFilters() throws Exception {
        task.adaptresourcefilenames("**.properties");
        task.adaptresourcefilenames("**.properties");

        // Filter list may contain duplicates or not - just verify it contains the pattern
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "Should contain the filter");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_veryLongPattern() throws Exception {
        String longPattern = "com/example/very/long/path/to/resources/**/configuration/*.properties";
        task.adaptresourcefilenames(longPattern);

        assertTrue(task.configuration.adaptResourceFileNames.contains(longPattern),
                  "Should handle long patterns");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_withComplexConfiguration() throws Exception {
        task.injars("input.jar");
        task.outjars("output.jar");
        task.keep("public class * { *; }");
        task.adaptresourcefilenames("**.properties,**.xml");
        task.keepattributes("Signature");
        task.verbose();

        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "Should contain properties filter");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.xml"),
                  "Should contain XML filter");
        assertTrue(task.configuration.verbose, "verbose should be set");
    }

    // ========================================
    // Android-Specific Scenarios with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilenamesWithString_androidResources() throws Exception {
        // Android resource files
        task.adaptresourcefilenames("res/**");

        assertTrue(task.configuration.adaptResourceFileNames.contains("res/**"),
                  "Should adapt Android resource files");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_androidManifest() throws Exception {
        // Android manifest
        task.adaptresourcefilenames("AndroidManifest.xml");

        assertTrue(task.configuration.adaptResourceFileNames.contains("AndroidManifest.xml"),
                  "Should adapt Android manifest");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_androidAssets() throws Exception {
        // Android assets
        task.adaptresourcefilenames("assets/**");

        assertTrue(task.configuration.adaptResourceFileNames.contains("assets/**"),
                  "Should adapt Android assets");
    }

    // ========================================
    // Configuration State Tests with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilenamesWithString_doesNotAffectOtherSettings() throws Exception {
        task.verbose();
        task.dontoptimize();
        boolean verboseBefore = task.configuration.verbose;
        boolean optimizeBefore = task.configuration.optimize;

        task.adaptresourcefilenames("**.properties");

        assertEquals(verboseBefore, task.configuration.verbose, "verbose should not change");
        assertEquals(optimizeBefore, task.configuration.optimize, "optimize should not change");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "adaptResourceFileNames should contain filter");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_doesNotAffectOtherFilters() throws Exception {
        task.keeppackagenames("com.example.*");
        task.keepattributes("Signature");

        task.adaptresourcefilenames("**.xml");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**.xml"),
                  "adaptResourceFileNames should contain filter");
        assertNotNull(task.configuration.keepPackageNames, "keepPackageNames filter should be preserved");
        assertNotNull(task.configuration.keepAttributes, "keepAttributes filter should be preserved");
    }

    // ========================================
    // Special Use Cases with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilenamesWithString_forSpringConfiguration() throws Exception {
        // Spring configuration files
        task.adaptresourcefilenames("**/spring/*.xml,**/applicationContext.xml");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**/spring/*.xml"),
                  "Should adapt Spring config files");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**/applicationContext.xml"),
                  "Should adapt Spring context files");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_forHibernateMapping() throws Exception {
        // Hibernate mapping files
        task.adaptresourcefilenames("**.hbm.xml");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**.hbm.xml"),
                  "Should adapt Hibernate mapping files");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_forPersistenceXML() throws Exception {
        // JPA persistence.xml
        task.adaptresourcefilenames("META-INF/persistence.xml");

        assertTrue(task.configuration.adaptResourceFileNames.contains("META-INF/persistence.xml"),
                  "Should adapt persistence.xml");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_forWebXML() throws Exception {
        // web.xml
        task.adaptresourcefilenames("WEB-INF/web.xml");

        assertTrue(task.configuration.adaptResourceFileNames.contains("WEB-INF/web.xml"),
                  "Should adapt web.xml");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_forResourceBundles() throws Exception {
        // Resource bundle files
        task.adaptresourcefilenames("**/messages*.properties");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**/messages*.properties"),
                  "Should adapt resource bundle files");
    }

    // ========================================
    // Common Patterns
    // ========================================

    @Test
    public void testAdaptresourcefilenamesWithString_allPropertiesFiles() throws Exception {
        // Adapt all properties files
        task.adaptresourcefilenames("**.properties");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "Should adapt all properties files");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_allXMLFiles() throws Exception {
        // Adapt all XML files
        task.adaptresourcefilenames("**.xml");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**.xml"),
                  "Should adapt all XML files");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_metaInfResources() throws Exception {
        // Adapt META-INF resources
        task.adaptresourcefilenames("META-INF/**");

        assertTrue(task.configuration.adaptResourceFileNames.contains("META-INF/**"),
                  "Should adapt all META-INF resources");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_specificFileExtensions() throws Exception {
        // Adapt specific file extensions
        task.adaptresourcefilenames("**.properties,**.xml,**.json,**.yaml");

        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "Should adapt properties files");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.xml"),
                  "Should adapt XML files");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.json"),
                  "Should adapt JSON files");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.yaml"),
                  "Should adapt YAML files");
    }

    // ========================================
    // Verification Tests with String Parameter
    // ========================================

    @Test
    public void testAdaptresourcefilenamesWithString_configurationNotNull() throws Exception {
        task.adaptresourcefilenames("**.properties");

        assertNotNull(task.configuration, "Configuration should never be null");
        assertNotNull(task.configuration.adaptResourceFileNames, "adaptResourceFileNames should not be null");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_taskStateValid() throws Exception {
        task.adaptresourcefilenames("**.xml");

        assertNotNull(task.configuration, "Configuration should be valid");
        assertNotNull(task.getProject(), "Project should be valid");
        assertEquals("proguard", task.getName(), "Task name should be correct");
    }

    @Test
    public void testAdaptresourcefilenamesWithString_filtersAccumulate() throws Exception {
        task.adaptresourcefilenames("**.properties");
        int sizeBefore = task.configuration.adaptResourceFileNames.size();

        task.adaptresourcefilenames("**.xml");
        int sizeAfter = task.configuration.adaptResourceFileNames.size();

        assertTrue(sizeAfter > sizeBefore, "Filters should accumulate");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.properties"),
                  "Should still contain first filter");
        assertTrue(task.configuration.adaptResourceFileNames.contains("**.xml"),
                  "Should contain second filter");
    }
}
