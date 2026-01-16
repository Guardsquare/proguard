package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ProGuardTask.classobfuscationdictionary(Object) method.
 *
 * This tests the method that sets configuration.classObfuscationDictionary to a URL.
 */
class ProGuardTaskClaude_classobfuscationdictionaryTest {

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
    void testClassobfuscationdictionaryWithString_setsClassObfuscationDictionaryToURL() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile.getAbsolutePath());
        assertNotNull(task.configuration.classObfuscationDictionary, "classObfuscationDictionary should not be null");
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dictionary.txt"),
                "classObfuscationDictionary should reference class-dictionary.txt");
    }

    @Test
    void testClassobfuscationdictionaryWithFile_acceptsFileObject() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.classObfuscationDictionary, "classObfuscationDictionary should not be null");
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dictionary.txt"),
                "classObfuscationDictionary should reference class-dictionary.txt");
    }

    @Test
    void testClassobfuscationdictionaryWithURL_acceptsURLObject() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();
        URL dictURL = dictFile.toURI().toURL();

        task.classobfuscationdictionary(dictURL);
        assertNotNull(task.configuration.classObfuscationDictionary, "classObfuscationDictionary should not be null");
        assertEquals(dictURL, task.configuration.classObfuscationDictionary,
                "classObfuscationDictionary should match provided URL");
    }

    @Test
    void testClassobfuscationdictionaryChangesConfigurationFromNull() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        assertNull(task.configuration.classObfuscationDictionary, "classObfuscationDictionary should initially be null");
        task.classobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.classObfuscationDictionary, "classObfuscationDictionary should be set after classobfuscationdictionary()");
    }

    @Test
    void testClassobfuscationdictionaryWithFreshTask() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        freshTask.classobfuscationdictionary(dictFile);
        assertNotNull(freshTask.configuration.classObfuscationDictionary);
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    void testClassobfuscationdictionaryOverwritesPreviousValue() throws Exception {
        File firstDict = new File(testProjectDir, "first.txt");
        firstDict.createNewFile();
        File secondDict = new File(testProjectDir, "second.txt");
        secondDict.createNewFile();

        task.classobfuscationdictionary(firstDict);
        URL firstURL = task.configuration.classObfuscationDictionary;

        task.classobfuscationdictionary(secondDict);
        URL secondURL = task.configuration.classObfuscationDictionary;

        assertNotEquals(firstURL, secondURL, "classObfuscationDictionary should be overwritten");
        assertTrue(secondURL.toString().contains("second.txt"),
                "classObfuscationDictionary should reference second.txt");
    }

    @Test
    void testClassobfuscationdictionaryMultipleCallsWithDifferentFiles() throws Exception {
        File first = new File(testProjectDir, "first.txt");
        first.createNewFile();
        File second = new File(testProjectDir, "second.txt");
        second.createNewFile();
        File third = new File(testProjectDir, "third.txt");
        third.createNewFile();

        task.classobfuscationdictionary(first);
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("first.txt"));

        task.classobfuscationdictionary(second);
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("second.txt"));

        task.classobfuscationdictionary(third);
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("third.txt"));
    }

    @Test
    void testClassobfuscationdictionaryCalledMultipleTimes() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.classobfuscationdictionary(dictFile);
        task.classobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.classObfuscationDictionary,
                "Multiple calls with same file should work");
    }

    // ============================================================
    // File Path Variations
    // ============================================================

    @Test
    void testClassobfuscationdictionaryWithRelativePath() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary("class-dictionary.txt");
        assertNotNull(task.configuration.classObfuscationDictionary, "classObfuscationDictionary should not be null");
    }

    @Test
    void testClassobfuscationdictionaryWithNestedDirectoryPath() throws Exception {
        File dir = new File(testProjectDir, "config");
        dir.mkdirs();
        File dictFile = new File(dir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.classObfuscationDictionary, "classObfuscationDictionary should not be null");
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dictionary.txt"));
    }

    @Test
    void testClassobfuscationdictionaryWithDifferentExtensions() throws Exception {
        String[] extensions = {"classes.txt", "class-names.dict", "class-obf.txt", "classnames.list"};
        for (String filename : extensions) {
            File dictFile = new File(testProjectDir, filename);
            dictFile.createNewFile();

            ProGuardTask freshTask = project.getTasks().create("proguard_" + filename.replace(".", "_").replace("-", "_"), ProGuardTask.class);
            freshTask.classobfuscationdictionary(dictFile);
            assertNotNull(freshTask.configuration.classObfuscationDictionary,
                    "classObfuscationDictionary should not be null for " + filename);
        }
    }

    @Test
    void testClassobfuscationdictionaryWithAbsolutePath() throws Exception {
        File absoluteFile = new File(testProjectDir, "absolute-class-dict.txt");
        absoluteFile.createNewFile();

        task.classobfuscationdictionary(absoluteFile.getAbsolutePath());
        assertNotNull(task.configuration.classObfuscationDictionary, "classObfuscationDictionary should not be null");
    }

    @Test
    void testClassobfuscationdictionaryWithComplexPath() throws Exception {
        File dir1 = new File(testProjectDir, "build");
        File dir2 = new File(dir1, "config");
        File dir3 = new File(dir2, "proguard");
        dir3.mkdirs();
        File dictFile = new File(dir3, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dictionary.txt"));
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    void testClassobfuscationdictionaryWithDontobfuscate() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.dontobfuscate();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testClassobfuscationdictionaryWithDontshrink() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.dontshrink();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertFalse(task.configuration.shrink);
    }

    @Test
    void testClassobfuscationdictionaryWithDontoptimize() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.dontoptimize();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testClassobfuscationdictionaryWithVerbose() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testClassobfuscationdictionaryWithAllowAccessModification() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testClassobfuscationdictionaryWithMergeInterfacesAggressively() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testClassobfuscationdictionaryWithMultipleConfigMethods() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testClassobfuscationdictionaryWithAllOptimizationSettings() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    void testClassobfuscationdictionaryBeforeOtherConfig() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testClassobfuscationdictionaryAfterOtherConfig() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.verbose();
        task.classobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testClassobfuscationdictionaryBetweenOtherConfig() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.classobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    void testClassobfuscationdictionaryForCustomClassNames() throws Exception {
        // Using custom dictionary for class name obfuscation
        File dictFile = new File(testProjectDir, "custom-class-names.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testClassobfuscationdictionaryForBrandedClassNames() throws Exception {
        // Using dictionary with brand-specific class names
        File dictFile = new File(testProjectDir, "branded-class-names.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testClassobfuscationdictionaryForConsistentClassNaming() throws Exception {
        // Maintaining consistent class naming across builds
        File dictFile = new File(testProjectDir, "consistent-class-dict.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testClassobfuscationdictionaryInMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup with class dictionary
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    @Test
    void testClassobfuscationdictionaryInMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    void testClassobfuscationdictionaryWithNullConfiguration() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        // Configuration should never be null in normal usage
        assertNotNull(task.configuration);
        task.classobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    @Test
    void testClassobfuscationdictionaryOnMultipleTasks() throws Exception {
        File dict1 = new File(testProjectDir, "class-dict1.txt");
        dict1.createNewFile();
        File dict2 = new File(testProjectDir, "class-dict2.txt");
        dict2.createNewFile();
        File dict3 = new File(testProjectDir, "class-dict3.txt");
        dict3.createNewFile();

        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.classobfuscationdictionary(dict1);
        task2.classobfuscationdictionary(dict2);
        task3.classobfuscationdictionary(dict3);

        assertTrue(task1.configuration.classObfuscationDictionary.toString().contains("class-dict1.txt"));
        assertTrue(task2.configuration.classObfuscationDictionary.toString().contains("class-dict2.txt"));
        assertTrue(task3.configuration.classObfuscationDictionary.toString().contains("class-dict3.txt"));
    }

    @Test
    void testClassobfuscationdictionaryIndependentOfOtherTasks() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.classobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertNull(otherTask.configuration.classObfuscationDictionary, "Other task should not be affected");
    }

    @Test
    void testClassobfuscationdictionaryAfterConfigurationAccess() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        // Access configuration before calling classobfuscationdictionary
        assertNotNull(task.configuration);

        task.classobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    @Test
    void testClassobfuscationdictionaryRepeatedOverTime() throws Exception {
        File dict1 = new File(testProjectDir, "class-dict1.txt");
        dict1.createNewFile();
        File dict2 = new File(testProjectDir, "class-dict2.txt");
        dict2.createNewFile();
        File dict3 = new File(testProjectDir, "class-dict3.txt");
        dict3.createNewFile();

        // Simulate multiple calls over time
        task.classobfuscationdictionary(dict1);
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dict1.txt"));

        task.allowaccessmodification();
        task.classobfuscationdictionary(dict2);
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dict2.txt"));

        task.verbose();
        task.classobfuscationdictionary(dict3);
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dict3.txt"));
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    void testClassobfuscationdictionaryInAndroidReleaseVariant() throws Exception {
        // Android release variant with custom class dictionary
        File dictFile = new File(testProjectDir, "android-class-dict.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testClassobfuscationdictionaryInAndroidLibraryModule() throws Exception {
        // Android library with custom class dictionary
        File dictFile = new File(testProjectDir, "library-class-dict.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    @Test
    void testClassobfuscationdictionaryForAndroidMultiDexBuild() throws Exception {
        // Multi-dex build with custom class dictionary
        File dictFile = new File(testProjectDir, "multidex-class-dict.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    void testClassobfuscationdictionaryInProductionBuild() throws Exception {
        // Production release build with custom class dictionary
        File dictFile = new File(testProjectDir, "production-class-dict.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testClassobfuscationdictionaryInStagingForQa() throws Exception {
        // QA staging build with custom class dictionary
        File dictFile = new File(testProjectDir, "staging-class-dict.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testClassobfuscationdictionaryInBetaBuild() throws Exception {
        // Beta testing build with custom class dictionary
        File dictFile = new File(testProjectDir, "beta-class-dict.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testClassobfuscationdictionaryInAlphaBuild() throws Exception {
        // Alpha testing build with custom class dictionary
        File dictFile = new File(testProjectDir, "alpha-class-dict.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    void testClassobfuscationdictionaryModifiesConfigurationState() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        assertNull(task.configuration.classObfuscationDictionary);

        task.classobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    @Test
    void testClassobfuscationdictionaryPreservesOtherConfigurationSettings() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.verbose();

        task.classobfuscationdictionary(dictFile);

        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    @Test
    void testClassobfuscationdictionaryWithComplexConfigurationChain() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.classobfuscationdictionary(dictFile);
        task.verbose();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testClassobfuscationdictionaryDoesNotAffectShrinkSetting() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        // Verify shrink remains at default
        task.classobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    void testClassobfuscationdictionaryDoesNotAffectOptimizeSetting() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        // Verify optimize remains at default
        task.classobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    void testClassobfuscationdictionaryDoesNotAffectObfuscateSetting() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        // Verify obfuscate remains at default
        task.classobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    // ============================================================
    // Output Organization Scenarios
    // ============================================================

    @Test
    void testClassobfuscationdictionaryOrganizedByBuildType() throws Exception {
        File dir = new File(testProjectDir, "dictionaries/release");
        dir.mkdirs();
        File dictFile = new File(dir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    @Test
    void testClassobfuscationdictionaryOrganizedByProject() throws Exception {
        File dir = new File(testProjectDir, "config/proguard");
        dir.mkdirs();
        File dictFile = new File(dir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    // ============================================================
    // Configuration Preservation Tests
    // ============================================================

    @Test
    void testClassobfuscationdictionaryPreservesOtherSettings() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.verbose();

        task.classobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testClassobfuscationdictionaryAfterComplexConfiguration() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.verbose();
        task.mergeinterfacesaggressively();
        task.classobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    // ============================================================
    // File Naming Conventions
    // ============================================================

    @Test
    void testClassobfuscationdictionaryStandardNaming() throws Exception {
        String[] standardNames = {
            "class-dictionary.txt",
            "class-obfuscation-dict.txt",
            "classnames.dict",
            "class-words.txt"
        };

        for (String name : standardNames) {
            File dictFile = new File(testProjectDir, name);
            dictFile.createNewFile();

            ProGuardTask freshTask = project.getTasks().create("task_" + name.replace(".", "_").replace("-", "_"), ProGuardTask.class);
            freshTask.classobfuscationdictionary(dictFile);
            assertNotNull(freshTask.configuration.classObfuscationDictionary,
                    "Should handle standard name: " + name);
        }
    }

    @Test
    void testClassobfuscationdictionaryDescriptiveNaming() throws Exception {
        File dictFile = new File(testProjectDir, "app-release-class-obfuscation-dict.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    @Test
    void testClassobfuscationdictionaryWithVersionedFile() throws Exception {
        File dictFile = new File(testProjectDir, "class-dictionary-v1.0.0.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    // ============================================================
    // Build System Integration
    // ============================================================

    @Test
    void testClassobfuscationdictionaryGradleBuildDirectory() throws Exception {
        File buildDir = new File(testProjectDir, "build");
        buildDir.mkdirs();
        File dictFile = new File(buildDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    @Test
    void testClassobfuscationdictionaryCustomConfigDirectory() throws Exception {
        File configDir = new File(testProjectDir, "config");
        configDir.mkdirs();
        File dictFile = new File(configDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    @Test
    void testClassobfuscationdictionaryFromResourcesDirectory() throws Exception {
        File resourcesDir = new File(testProjectDir, "src/main/resources");
        resourcesDir.mkdirs();
        File dictFile = new File(resourcesDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    void testClassobfuscationdictionaryForDockerizedBuilds() throws Exception {
        // Dockerized build environment with mounted class dictionary
        File dictFile = new File(testProjectDir, "docker-class-dict.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testClassobfuscationdictionaryForCloudBuilds() throws Exception {
        // Cloud build service with shared class dictionary
        File dictFile = new File(testProjectDir, "cloud-class-dict.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testClassobfuscationdictionaryForLocalDevelopment() throws Exception {
        // Local development environment
        File dictFile = new File(testProjectDir, "local-class-dict.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testClassobfuscationdictionaryForContinuousIntegration() throws Exception {
        // CI pipeline configuration
        File dictFile = new File(testProjectDir, "ci-class-dict.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testClassobfuscationdictionaryWithMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        File dictFile = new File(testProjectDir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    // ============================================================
    // Dictionary Content Scenarios
    // ============================================================

    @Test
    void testClassobfuscationdictionaryForShortClassNames() throws Exception {
        // Dictionary with short class names for size optimization
        File dictFile = new File(testProjectDir, "short-class-names.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testClassobfuscationdictionaryForReadableClassNames() throws Exception {
        // Dictionary with readable class names for debugging
        File dictFile = new File(testProjectDir, "readable-class-names.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testClassobfuscationdictionaryForUniqueBranding() throws Exception {
        // Dictionary with brand-specific unique class names
        File dictFile = new File(testProjectDir, "branded-class-names.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    // ============================================================
    // Multi-variant Scenarios
    // ============================================================

    @Test
    void testClassobfuscationdictionaryProductionVariant() throws Exception {
        File dir = new File(testProjectDir, "config/production");
        dir.mkdirs();
        File dictFile = new File(dir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testClassobfuscationdictionaryStagingVariant() throws Exception {
        File dir = new File(testProjectDir, "config/staging");
        dir.mkdirs();
        File dictFile = new File(dir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testClassobfuscationdictionaryDevelopmentVariant() throws Exception {
        File dir = new File(testProjectDir, "config/development");
        dir.mkdirs();
        File dictFile = new File(dir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.classObfuscationDictionary);
    }

    @Test
    void testClassobfuscationdictionaryBetaVariant() throws Exception {
        File dir = new File(testProjectDir, "config/beta");
        dir.mkdirs();
        File dictFile = new File(dir, "class-dictionary.txt");
        dictFile.createNewFile();

        task.classobfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    // ============================================================
    // Integration with Other Dictionary Methods
    // ============================================================

    @Test
    void testClassobfuscationdictionaryWithObfuscationdictionary() throws Exception {
        File generalDict = new File(testProjectDir, "general-dictionary.txt");
        generalDict.createNewFile();
        File classDict = new File(testProjectDir, "class-dictionary.txt");
        classDict.createNewFile();

        task.obfuscationdictionary(generalDict);
        task.classobfuscationdictionary(classDict);

        assertNotNull(task.configuration.obfuscationDictionary);
        assertNotNull(task.configuration.classObfuscationDictionary);
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("general-dictionary.txt"));
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dictionary.txt"));
    }

    @Test
    void testClassobfuscationdictionaryIndependentOfObfuscationdictionary() throws Exception {
        File generalDict = new File(testProjectDir, "general-dictionary.txt");
        generalDict.createNewFile();
        File classDict = new File(testProjectDir, "class-dictionary.txt");
        classDict.createNewFile();

        task.obfuscationdictionary(generalDict);

        assertNotNull(task.configuration.obfuscationDictionary);
        assertNull(task.configuration.classObfuscationDictionary, "classObfuscationDictionary should still be null");

        task.classobfuscationdictionary(classDict);

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertNotNull(task.configuration.obfuscationDictionary, "obfuscationDictionary should remain set");
    }
}
