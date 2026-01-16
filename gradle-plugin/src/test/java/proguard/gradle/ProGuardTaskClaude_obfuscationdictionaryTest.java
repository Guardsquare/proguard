package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.obfuscationdictionary(Object) method.
 *
 * This tests the method that sets configuration.obfuscationDictionary to a URL.
 */
class ProGuardTaskClaude_obfuscationdictionaryTest {

    @TempDir
    File testProjectDir;

    private Project project;
    private ProGuardTask task;

    @BeforeEach
    void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(testProjectDir)
                .build();
        task = project.getTasks().create("testProguard", ProGuardTask.class);
    }

    // ============================================================
    // Basic Functionality Tests
    // ============================================================

    @Test
    void testObfuscationdictionaryWithString_setsObfuscationDictionaryToURL() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile.getAbsolutePath());
        assertNotNull(task.configuration.obfuscationDictionary, "obfuscationDictionary should not be null");
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("dictionary.txt"),
                "obfuscationDictionary should reference dictionary.txt");
    }

    @Test
    void testObfuscationdictionaryWithFile_acceptsFileObject() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        assertNotNull(task.configuration.obfuscationDictionary, "obfuscationDictionary should not be null");
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("dictionary.txt"),
                "obfuscationDictionary should reference dictionary.txt");
    }

    @Test
    void testObfuscationdictionaryWithURL_acceptsURLObject() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();
        URL dictURL = dictFile.toURI().toURL();

        task.obfuscationdictionary(dictURL);
        assertNotNull(task.configuration.obfuscationDictionary, "obfuscationDictionary should not be null");
        assertEquals(dictURL, task.configuration.obfuscationDictionary,
                "obfuscationDictionary should match provided URL");
    }

    @Test
    void testObfuscationdictionaryChangesConfigurationFromNull() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        assertNull(task.configuration.obfuscationDictionary, "obfuscationDictionary should initially be null");
        task.obfuscationdictionary(dictFile);
        assertNotNull(task.configuration.obfuscationDictionary, "obfuscationDictionary should be set after obfuscationdictionary()");
    }

    @Test
    void testObfuscationdictionaryWithFreshTask() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        freshTask.obfuscationdictionary(dictFile);
        assertNotNull(freshTask.configuration.obfuscationDictionary);
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    void testObfuscationdictionaryOverwritesPreviousValue() throws Exception {
        File firstDict = new File(testProjectDir, "first.txt");
        firstDict.createNewFile();
        File secondDict = new File(testProjectDir, "second.txt");
        secondDict.createNewFile();

        task.obfuscationdictionary(firstDict);
        URL firstURL = task.configuration.obfuscationDictionary;

        task.obfuscationdictionary(secondDict);
        URL secondURL = task.configuration.obfuscationDictionary;

        assertNotEquals(firstURL, secondURL, "obfuscationDictionary should be overwritten");
        assertTrue(secondURL.toString().contains("second.txt"),
                "obfuscationDictionary should reference second.txt");
    }

    @Test
    void testObfuscationdictionaryMultipleCallsWithDifferentFiles() throws Exception {
        File first = new File(testProjectDir, "first.txt");
        first.createNewFile();
        File second = new File(testProjectDir, "second.txt");
        second.createNewFile();
        File third = new File(testProjectDir, "third.txt");
        third.createNewFile();

        task.obfuscationdictionary(first);
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("first.txt"));

        task.obfuscationdictionary(second);
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("second.txt"));

        task.obfuscationdictionary(third);
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("third.txt"));
    }

    @Test
    void testObfuscationdictionaryCalledMultipleTimes() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.obfuscationdictionary(dictFile);
        task.obfuscationdictionary(dictFile);

        assertNotNull(task.configuration.obfuscationDictionary,
                "Multiple calls with same file should work");
    }

    // ============================================================
    // File Path Variations
    // ============================================================

    @Test
    void testObfuscationdictionaryWithRelativePath() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary("dictionary.txt");
        assertNotNull(task.configuration.obfuscationDictionary, "obfuscationDictionary should not be null");
    }

    @Test
    void testObfuscationdictionaryWithNestedDirectoryPath() throws Exception {
        File dir = new File(testProjectDir, "config");
        dir.mkdirs();
        File dictFile = new File(dir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        assertNotNull(task.configuration.obfuscationDictionary, "obfuscationDictionary should not be null");
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("dictionary.txt"));
    }

    @Test
    void testObfuscationdictionaryWithDifferentExtensions() throws Exception {
        String[] extensions = {"dict.txt", "words.dict", "obfuscation.txt", "names.list"};
        for (String filename : extensions) {
            File dictFile = new File(testProjectDir, filename);
            dictFile.createNewFile();

            ProGuardTask freshTask = project.getTasks().create("proguard_" + filename.replace(".", "_"), ProGuardTask.class);
            freshTask.obfuscationdictionary(dictFile);
            assertNotNull(freshTask.configuration.obfuscationDictionary,
                    "obfuscationDictionary should not be null for " + filename);
        }
    }

    @Test
    void testObfuscationdictionaryWithAbsolutePath() throws Exception {
        File absoluteFile = new File(testProjectDir, "absolute-dict.txt");
        absoluteFile.createNewFile();

        task.obfuscationdictionary(absoluteFile.getAbsolutePath());
        assertNotNull(task.configuration.obfuscationDictionary, "obfuscationDictionary should not be null");
    }

    @Test
    void testObfuscationdictionaryWithComplexPath() throws Exception {
        File dir1 = new File(testProjectDir, "build");
        File dir2 = new File(dir1, "config");
        File dir3 = new File(dir2, "proguard");
        dir3.mkdirs();
        File dictFile = new File(dir3, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("dictionary.txt"));
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    void testObfuscationdictionaryWithDontobfuscate() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.dontobfuscate();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testObfuscationdictionaryWithDontshrink() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.dontshrink();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertFalse(task.configuration.shrink);
    }

    @Test
    void testObfuscationdictionaryWithDontoptimize() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.dontoptimize();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testObfuscationdictionaryWithVerbose() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testObfuscationdictionaryWithAllowAccessModification() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testObfuscationdictionaryWithMergeInterfacesAggressively() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testObfuscationdictionaryWithMultipleConfigMethods() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testObfuscationdictionaryWithAllOptimizationSettings() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    void testObfuscationdictionaryBeforeOtherConfig() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testObfuscationdictionaryAfterOtherConfig() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.verbose();
        task.obfuscationdictionary(dictFile);

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testObfuscationdictionaryBetweenOtherConfig() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.obfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    void testObfuscationdictionaryForCustomNaming() throws Exception {
        // Using custom dictionary for predictable obfuscation
        File dictFile = new File(testProjectDir, "custom-names.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testObfuscationdictionaryForBrandedNames() throws Exception {
        // Using dictionary with brand-specific names
        File dictFile = new File(testProjectDir, "branded-dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testObfuscationdictionaryForConsistentObfuscation() throws Exception {
        // Maintaining consistent obfuscation across builds
        File dictFile = new File(testProjectDir, "consistent-dict.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testObfuscationdictionaryInMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup with dictionary
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);

        assertNotNull(task.configuration.obfuscationDictionary);
    }

    @Test
    void testObfuscationdictionaryInMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    void testObfuscationdictionaryWithNullConfiguration() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        // Configuration should never be null in normal usage
        assertNotNull(task.configuration);
        task.obfuscationdictionary(dictFile);
        assertNotNull(task.configuration.obfuscationDictionary);
    }

    @Test
    void testObfuscationdictionaryOnMultipleTasks() throws Exception {
        File dict1 = new File(testProjectDir, "dictionary1.txt");
        dict1.createNewFile();
        File dict2 = new File(testProjectDir, "dictionary2.txt");
        dict2.createNewFile();
        File dict3 = new File(testProjectDir, "dictionary3.txt");
        dict3.createNewFile();

        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.obfuscationdictionary(dict1);
        task2.obfuscationdictionary(dict2);
        task3.obfuscationdictionary(dict3);

        assertTrue(task1.configuration.obfuscationDictionary.toString().contains("dictionary1.txt"));
        assertTrue(task2.configuration.obfuscationDictionary.toString().contains("dictionary2.txt"));
        assertTrue(task3.configuration.obfuscationDictionary.toString().contains("dictionary3.txt"));
    }

    @Test
    void testObfuscationdictionaryIndependentOfOtherTasks() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.obfuscationdictionary(dictFile);

        assertNotNull(task.configuration.obfuscationDictionary);
        assertNull(otherTask.configuration.obfuscationDictionary, "Other task should not be affected");
    }

    @Test
    void testObfuscationdictionaryAfterConfigurationAccess() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        // Access configuration before calling obfuscationdictionary
        assertNotNull(task.configuration);

        task.obfuscationdictionary(dictFile);
        assertNotNull(task.configuration.obfuscationDictionary);
    }

    @Test
    void testObfuscationdictionaryRepeatedOverTime() throws Exception {
        File dict1 = new File(testProjectDir, "dictionary1.txt");
        dict1.createNewFile();
        File dict2 = new File(testProjectDir, "dictionary2.txt");
        dict2.createNewFile();
        File dict3 = new File(testProjectDir, "dictionary3.txt");
        dict3.createNewFile();

        // Simulate multiple calls over time
        task.obfuscationdictionary(dict1);
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("dictionary1.txt"));

        task.allowaccessmodification();
        task.obfuscationdictionary(dict2);
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("dictionary2.txt"));

        task.verbose();
        task.obfuscationdictionary(dict3);
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("dictionary3.txt"));
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    void testObfuscationdictionaryInAndroidReleaseVariant() throws Exception {
        // Android release variant with custom dictionary
        File dictFile = new File(testProjectDir, "android-dict.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testObfuscationdictionaryInAndroidLibraryModule() throws Exception {
        // Android library with custom dictionary
        File dictFile = new File(testProjectDir, "library-dict.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);

        assertNotNull(task.configuration.obfuscationDictionary);
    }

    @Test
    void testObfuscationdictionaryForAndroidMultiDexBuild() throws Exception {
        // Multi-dex build with custom dictionary
        File dictFile = new File(testProjectDir, "multidex-dict.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    void testObfuscationdictionaryInProductionBuild() throws Exception {
        // Production release build with custom dictionary
        File dictFile = new File(testProjectDir, "production-dict.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testObfuscationdictionaryInStagingForQa() throws Exception {
        // QA staging build with custom dictionary
        File dictFile = new File(testProjectDir, "staging-dict.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testObfuscationdictionaryInBetaBuild() throws Exception {
        // Beta testing build with custom dictionary
        File dictFile = new File(testProjectDir, "beta-dict.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testObfuscationdictionaryInAlphaBuild() throws Exception {
        // Alpha testing build with custom dictionary
        File dictFile = new File(testProjectDir, "alpha-dict.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    void testObfuscationdictionaryModifiesConfigurationState() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        assertNull(task.configuration.obfuscationDictionary);

        task.obfuscationdictionary(dictFile);

        assertNotNull(task.configuration.obfuscationDictionary);
    }

    @Test
    void testObfuscationdictionaryPreservesOtherConfigurationSettings() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.verbose();

        task.obfuscationdictionary(dictFile);

        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertNotNull(task.configuration.obfuscationDictionary);
    }

    @Test
    void testObfuscationdictionaryWithComplexConfigurationChain() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.obfuscationdictionary(dictFile);
        task.verbose();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testObfuscationdictionaryDoesNotAffectShrinkSetting() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        // Verify shrink remains at default
        task.obfuscationdictionary(dictFile);

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    void testObfuscationdictionaryDoesNotAffectOptimizeSetting() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        // Verify optimize remains at default
        task.obfuscationdictionary(dictFile);

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    void testObfuscationdictionaryDoesNotAffectObfuscateSetting() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        // Verify obfuscate remains at default
        task.obfuscationdictionary(dictFile);

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    // ============================================================
    // Output Organization Scenarios
    // ============================================================

    @Test
    void testObfuscationdictionaryOrganizedByBuildType() throws Exception {
        File dir = new File(testProjectDir, "dictionaries/release");
        dir.mkdirs();
        File dictFile = new File(dir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        assertNotNull(task.configuration.obfuscationDictionary);
    }

    @Test
    void testObfuscationdictionaryOrganizedByProject() throws Exception {
        File dir = new File(testProjectDir, "config/proguard");
        dir.mkdirs();
        File dictFile = new File(dir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        assertNotNull(task.configuration.obfuscationDictionary);
    }

    // ============================================================
    // Configuration Preservation Tests
    // ============================================================

    @Test
    void testObfuscationdictionaryPreservesOtherSettings() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.verbose();

        task.obfuscationdictionary(dictFile);

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testObfuscationdictionaryAfterComplexConfiguration() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.verbose();
        task.mergeinterfacesaggressively();
        task.obfuscationdictionary(dictFile);

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    // ============================================================
    // File Naming Conventions
    // ============================================================

    @Test
    void testObfuscationdictionaryStandardNaming() throws Exception {
        String[] standardNames = {
            "dictionary.txt",
            "obfuscation-dict.txt",
            "names.dict",
            "words.txt"
        };

        for (String name : standardNames) {
            File dictFile = new File(testProjectDir, name);
            dictFile.createNewFile();

            ProGuardTask freshTask = project.getTasks().create("task_" + name.replace(".", "_").replace("-", "_"), ProGuardTask.class);
            freshTask.obfuscationdictionary(dictFile);
            assertNotNull(freshTask.configuration.obfuscationDictionary,
                    "Should handle standard name: " + name);
        }
    }

    @Test
    void testObfuscationdictionaryDescriptiveNaming() throws Exception {
        File dictFile = new File(testProjectDir, "app-release-obfuscation-dict.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        assertNotNull(task.configuration.obfuscationDictionary);
    }

    @Test
    void testObfuscationdictionaryWithVersionedFile() throws Exception {
        File dictFile = new File(testProjectDir, "dictionary-v1.0.0.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        assertNotNull(task.configuration.obfuscationDictionary);
    }

    // ============================================================
    // Build System Integration
    // ============================================================

    @Test
    void testObfuscationdictionaryGradleBuildDirectory() throws Exception {
        File buildDir = new File(testProjectDir, "build");
        buildDir.mkdirs();
        File dictFile = new File(buildDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        assertNotNull(task.configuration.obfuscationDictionary);
    }

    @Test
    void testObfuscationdictionaryCustomConfigDirectory() throws Exception {
        File configDir = new File(testProjectDir, "config");
        configDir.mkdirs();
        File dictFile = new File(configDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        assertNotNull(task.configuration.obfuscationDictionary);
    }

    @Test
    void testObfuscationdictionaryFromResourcesDirectory() throws Exception {
        File resourcesDir = new File(testProjectDir, "src/main/resources");
        resourcesDir.mkdirs();
        File dictFile = new File(resourcesDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        assertNotNull(task.configuration.obfuscationDictionary);
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    void testObfuscationdictionaryForDockerizedBuilds() throws Exception {
        // Dockerized build environment with mounted dictionary
        File dictFile = new File(testProjectDir, "docker-dict.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testObfuscationdictionaryForCloudBuilds() throws Exception {
        // Cloud build service with shared dictionary
        File dictFile = new File(testProjectDir, "cloud-dict.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testObfuscationdictionaryForLocalDevelopment() throws Exception {
        // Local development environment
        File dictFile = new File(testProjectDir, "local-dict.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testObfuscationdictionaryForContinuousIntegration() throws Exception {
        // CI pipeline configuration
        File dictFile = new File(testProjectDir, "ci-dict.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testObfuscationdictionaryWithMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        File dictFile = new File(testProjectDir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);

        assertNotNull(task.configuration.obfuscationDictionary);
    }

    // ============================================================
    // Dictionary Content Scenarios
    // ============================================================

    @Test
    void testObfuscationdictionaryForShortNames() throws Exception {
        // Dictionary with short names for size optimization
        File dictFile = new File(testProjectDir, "short-names.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testObfuscationdictionaryForReadableNames() throws Exception {
        // Dictionary with readable names for debugging
        File dictFile = new File(testProjectDir, "readable-names.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testObfuscationdictionaryForUniqueBranding() throws Exception {
        // Dictionary with brand-specific unique names
        File dictFile = new File(testProjectDir, "branded-names.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    // ============================================================
    // Multi-variant Scenarios
    // ============================================================

    @Test
    void testObfuscationdictionaryProductionVariant() throws Exception {
        File dir = new File(testProjectDir, "config/production");
        dir.mkdirs();
        File dictFile = new File(dir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testObfuscationdictionaryStagingVariant() throws Exception {
        File dir = new File(testProjectDir, "config/staging");
        dir.mkdirs();
        File dictFile = new File(dir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testObfuscationdictionaryDevelopmentVariant() throws Exception {
        File dir = new File(testProjectDir, "config/development");
        dir.mkdirs();
        File dictFile = new File(dir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);

        assertNotNull(task.configuration.obfuscationDictionary);
    }

    @Test
    void testObfuscationdictionaryBetaVariant() throws Exception {
        File dir = new File(testProjectDir, "config/beta");
        dir.mkdirs();
        File dictFile = new File(dir, "dictionary.txt");
        dictFile.createNewFile();

        task.obfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.obfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }
}
