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
 * Tests for ProGuardTask.packageobfuscationdictionary(Object) method.
 *
 * This tests the method that sets configuration.packageObfuscationDictionary to a URL.
 */
class ProGuardTaskClaude_packageobfuscationdictionaryTest {

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
    void testPackageobfuscationdictionaryWithString_setsPackageObfuscationDictionaryToURL() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile.getAbsolutePath());
        assertNotNull(task.configuration.packageObfuscationDictionary, "packageObfuscationDictionary should not be null");
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dictionary.txt"),
                "packageObfuscationDictionary should reference package-dictionary.txt");
    }

    @Test
    void testPackageobfuscationdictionaryWithFile_acceptsFileObject() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.packageObfuscationDictionary, "packageObfuscationDictionary should not be null");
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dictionary.txt"),
                "packageObfuscationDictionary should reference package-dictionary.txt");
    }

    @Test
    void testPackageobfuscationdictionaryWithURL_acceptsURLObject() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();
        URL dictURL = dictFile.toURI().toURL();

        task.packageobfuscationdictionary(dictURL);
        assertNotNull(task.configuration.packageObfuscationDictionary, "packageObfuscationDictionary should not be null");
        assertEquals(dictURL, task.configuration.packageObfuscationDictionary,
                "packageObfuscationDictionary should match provided URL");
    }

    @Test
    void testPackageobfuscationdictionaryChangesConfigurationFromNull() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        assertNull(task.configuration.packageObfuscationDictionary, "packageObfuscationDictionary should initially be null");
        task.packageobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.packageObfuscationDictionary, "packageObfuscationDictionary should be set after packageobfuscationdictionary()");
    }

    @Test
    void testPackageobfuscationdictionaryWithFreshTask() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        ProGuardTask freshTask = project.getTasks().create("freshProguard", ProGuardTask.class);
        freshTask.packageobfuscationdictionary(dictFile);
        assertNotNull(freshTask.configuration.packageObfuscationDictionary);
    }

    // ============================================================
    // Multiple Calls Tests
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryOverwritesPreviousValue() throws Exception {
        File firstDict = new File(testProjectDir, "first.txt");
        firstDict.createNewFile();
        File secondDict = new File(testProjectDir, "second.txt");
        secondDict.createNewFile();

        task.packageobfuscationdictionary(firstDict);
        URL firstURL = task.configuration.packageObfuscationDictionary;

        task.packageobfuscationdictionary(secondDict);
        URL secondURL = task.configuration.packageObfuscationDictionary;

        assertNotEquals(firstURL, secondURL, "packageObfuscationDictionary should be overwritten");
        assertTrue(secondURL.toString().contains("second.txt"),
                "packageObfuscationDictionary should reference second.txt");
    }

    @Test
    void testPackageobfuscationdictionaryMultipleCallsWithDifferentFiles() throws Exception {
        File first = new File(testProjectDir, "first.txt");
        first.createNewFile();
        File second = new File(testProjectDir, "second.txt");
        second.createNewFile();
        File third = new File(testProjectDir, "third.txt");
        third.createNewFile();

        task.packageobfuscationdictionary(first);
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("first.txt"));

        task.packageobfuscationdictionary(second);
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("second.txt"));

        task.packageobfuscationdictionary(third);
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("third.txt"));
    }

    @Test
    void testPackageobfuscationdictionaryCalledMultipleTimes() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.packageobfuscationdictionary(dictFile);
        task.packageobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.packageObfuscationDictionary,
                "Multiple calls with same file should work");
    }

    // ============================================================
    // File Path Variations
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryWithRelativePath() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary("package-dictionary.txt");
        assertNotNull(task.configuration.packageObfuscationDictionary, "packageObfuscationDictionary should not be null");
    }

    @Test
    void testPackageobfuscationdictionaryWithNestedDirectoryPath() throws Exception {
        File dir = new File(testProjectDir, "config");
        dir.mkdirs();
        File dictFile = new File(dir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.packageObfuscationDictionary, "packageObfuscationDictionary should not be null");
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dictionary.txt"));
    }

    @Test
    void testPackageobfuscationdictionaryWithDifferentExtensions() throws Exception {
        String[] extensions = {"packages.txt", "package-names.dict", "package-obf.txt", "packagenames.list"};
        for (String filename : extensions) {
            File dictFile = new File(testProjectDir, filename);
            dictFile.createNewFile();

            ProGuardTask freshTask = project.getTasks().create("proguard_" + filename.replace(".", "_").replace("-", "_"), ProGuardTask.class);
            freshTask.packageobfuscationdictionary(dictFile);
            assertNotNull(freshTask.configuration.packageObfuscationDictionary,
                    "packageObfuscationDictionary should not be null for " + filename);
        }
    }

    @Test
    void testPackageobfuscationdictionaryWithAbsolutePath() throws Exception {
        File absoluteFile = new File(testProjectDir, "absolute-package-dict.txt");
        absoluteFile.createNewFile();

        task.packageobfuscationdictionary(absoluteFile.getAbsolutePath());
        assertNotNull(task.configuration.packageObfuscationDictionary, "packageObfuscationDictionary should not be null");
    }

    @Test
    void testPackageobfuscationdictionaryWithComplexPath() throws Exception {
        File dir1 = new File(testProjectDir, "build");
        File dir2 = new File(dir1, "config");
        File dir3 = new File(dir2, "proguard");
        dir3.mkdirs();
        File dictFile = new File(dir3, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dictionary.txt"));
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryWithDontobfuscate() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.dontobfuscate();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    void testPackageobfuscationdictionaryWithDontshrink() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.dontshrink();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertFalse(task.configuration.shrink);
    }

    @Test
    void testPackageobfuscationdictionaryWithDontoptimize() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.dontoptimize();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertFalse(task.configuration.optimize);
    }

    @Test
    void testPackageobfuscationdictionaryWithVerbose() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPackageobfuscationdictionaryWithAllowAccessModification() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testPackageobfuscationdictionaryWithMergeInterfacesAggressively() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPackageobfuscationdictionaryWithMultipleConfigMethods() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPackageobfuscationdictionaryWithAllOptimizationSettings() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    // ============================================================
    // Call Order Flexibility Tests
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryBeforeOtherConfig() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.verbose();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPackageobfuscationdictionaryAfterOtherConfig() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.verbose();
        task.packageobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPackageobfuscationdictionaryBetweenOtherConfig() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.packageobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryForCustomPackageNames() throws Exception {
        // Using custom dictionary for package name obfuscation
        File dictFile = new File(testProjectDir, "custom-package-names.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPackageobfuscationdictionaryForBrandedPackageNames() throws Exception {
        // Using dictionary with brand-specific package names
        File dictFile = new File(testProjectDir, "branded-package-names.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPackageobfuscationdictionaryForConsistentPackageNaming() throws Exception {
        // Maintaining consistent package naming across builds
        File dictFile = new File(testProjectDir, "consistent-package-dict.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testPackageobfuscationdictionaryInMinimalProguardConfig() throws Exception {
        // Minimal ProGuard setup with package dictionary
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    @Test
    void testPackageobfuscationdictionaryInMaximalProguardConfig() throws Exception {
        // Comprehensive ProGuard setup
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();
        task.verbose();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryWithNullConfiguration() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        // Configuration should never be null in normal usage
        assertNotNull(task.configuration);
        task.packageobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    @Test
    void testPackageobfuscationdictionaryOnMultipleTasks() throws Exception {
        File dict1 = new File(testProjectDir, "package-dict1.txt");
        dict1.createNewFile();
        File dict2 = new File(testProjectDir, "package-dict2.txt");
        dict2.createNewFile();
        File dict3 = new File(testProjectDir, "package-dict3.txt");
        dict3.createNewFile();

        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.packageobfuscationdictionary(dict1);
        task2.packageobfuscationdictionary(dict2);
        task3.packageobfuscationdictionary(dict3);

        assertTrue(task1.configuration.packageObfuscationDictionary.toString().contains("package-dict1.txt"));
        assertTrue(task2.configuration.packageObfuscationDictionary.toString().contains("package-dict2.txt"));
        assertTrue(task3.configuration.packageObfuscationDictionary.toString().contains("package-dict3.txt"));
    }

    @Test
    void testPackageobfuscationdictionaryIndependentOfOtherTasks() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.packageobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertNull(otherTask.configuration.packageObfuscationDictionary, "Other task should not be affected");
    }

    @Test
    void testPackageobfuscationdictionaryAfterConfigurationAccess() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        // Access configuration before calling packageobfuscationdictionary
        assertNotNull(task.configuration);

        task.packageobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    @Test
    void testPackageobfuscationdictionaryRepeatedOverTime() throws Exception {
        File dict1 = new File(testProjectDir, "package-dict1.txt");
        dict1.createNewFile();
        File dict2 = new File(testProjectDir, "package-dict2.txt");
        dict2.createNewFile();
        File dict3 = new File(testProjectDir, "package-dict3.txt");
        dict3.createNewFile();

        // Simulate multiple calls over time
        task.packageobfuscationdictionary(dict1);
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dict1.txt"));

        task.allowaccessmodification();
        task.packageobfuscationdictionary(dict2);
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dict2.txt"));

        task.verbose();
        task.packageobfuscationdictionary(dict3);
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dict3.txt"));
    }

    // ============================================================
    // Android-Specific Scenarios
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryInAndroidReleaseVariant() throws Exception {
        // Android release variant with custom package dictionary
        File dictFile = new File(testProjectDir, "android-package-dict.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPackageobfuscationdictionaryInAndroidLibraryModule() throws Exception {
        // Android library with custom package dictionary
        File dictFile = new File(testProjectDir, "library-package-dict.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    @Test
    void testPackageobfuscationdictionaryForAndroidMultiDexBuild() throws Exception {
        // Multi-dex build with custom package dictionary
        File dictFile = new File(testProjectDir, "multidex-package-dict.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    // ============================================================
    // Build Variant Scenarios
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryInProductionBuild() throws Exception {
        // Production release build with custom package dictionary
        File dictFile = new File(testProjectDir, "production-package-dict.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPackageobfuscationdictionaryInStagingForQa() throws Exception {
        // QA staging build with custom package dictionary
        File dictFile = new File(testProjectDir, "staging-package-dict.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPackageobfuscationdictionaryInBetaBuild() throws Exception {
        // Beta testing build with custom package dictionary
        File dictFile = new File(testProjectDir, "beta-package-dict.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testPackageobfuscationdictionaryInAlphaBuild() throws Exception {
        // Alpha testing build with custom package dictionary
        File dictFile = new File(testProjectDir, "alpha-package-dict.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryModifiesConfigurationState() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        assertNull(task.configuration.packageObfuscationDictionary);

        task.packageobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    @Test
    void testPackageobfuscationdictionaryPreservesOtherConfigurationSettings() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.verbose();

        task.packageobfuscationdictionary(dictFile);

        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    @Test
    void testPackageobfuscationdictionaryWithComplexConfigurationChain() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.packageobfuscationdictionary(dictFile);
        task.verbose();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPackageobfuscationdictionaryDoesNotAffectShrinkSetting() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        // Verify shrink remains at default
        task.packageobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.shrink, "Shrink should remain at default true");
    }

    @Test
    void testPackageobfuscationdictionaryDoesNotAffectOptimizeSetting() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        // Verify optimize remains at default
        task.packageobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.optimize, "Optimize should remain at default true");
    }

    @Test
    void testPackageobfuscationdictionaryDoesNotAffectObfuscateSetting() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        // Verify obfuscate remains at default
        task.packageobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default true");
    }

    // ============================================================
    // Output Organization Scenarios
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryOrganizedByBuildType() throws Exception {
        File dir = new File(testProjectDir, "dictionaries/release");
        dir.mkdirs();
        File dictFile = new File(dir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    @Test
    void testPackageobfuscationdictionaryOrganizedByProject() throws Exception {
        File dir = new File(testProjectDir, "config/proguard");
        dir.mkdirs();
        File dictFile = new File(dir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    // ============================================================
    // Configuration Preservation Tests
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryPreservesOtherSettings() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.verbose();

        task.packageobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPackageobfuscationdictionaryAfterComplexConfiguration() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.allowaccessmodification();
        task.verbose();
        task.mergeinterfacesaggressively();
        task.packageobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    // ============================================================
    // File Naming Conventions
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryStandardNaming() throws Exception {
        String[] standardNames = {
            "package-dictionary.txt",
            "package-obfuscation-dict.txt",
            "packagenames.dict",
            "package-words.txt"
        };

        for (String name : standardNames) {
            File dictFile = new File(testProjectDir, name);
            dictFile.createNewFile();

            ProGuardTask freshTask = project.getTasks().create("task_" + name.replace(".", "_").replace("-", "_"), ProGuardTask.class);
            freshTask.packageobfuscationdictionary(dictFile);
            assertNotNull(freshTask.configuration.packageObfuscationDictionary,
                    "Should handle standard name: " + name);
        }
    }

    @Test
    void testPackageobfuscationdictionaryDescriptiveNaming() throws Exception {
        File dictFile = new File(testProjectDir, "app-release-package-obfuscation-dict.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    @Test
    void testPackageobfuscationdictionaryWithVersionedFile() throws Exception {
        File dictFile = new File(testProjectDir, "package-dictionary-v1.0.0.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    // ============================================================
    // Build System Integration
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryGradleBuildDirectory() throws Exception {
        File buildDir = new File(testProjectDir, "build");
        buildDir.mkdirs();
        File dictFile = new File(buildDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    @Test
    void testPackageobfuscationdictionaryCustomConfigDirectory() throws Exception {
        File configDir = new File(testProjectDir, "config");
        configDir.mkdirs();
        File dictFile = new File(configDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    @Test
    void testPackageobfuscationdictionaryFromResourcesDirectory() throws Exception {
        File resourcesDir = new File(testProjectDir, "src/main/resources");
        resourcesDir.mkdirs();
        File dictFile = new File(resourcesDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    // ============================================================
    // Special Use Cases
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryForDockerizedBuilds() throws Exception {
        // Dockerized build environment with mounted package dictionary
        File dictFile = new File(testProjectDir, "docker-package-dict.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPackageobfuscationdictionaryForCloudBuilds() throws Exception {
        // Cloud build service with shared package dictionary
        File dictFile = new File(testProjectDir, "cloud-package-dict.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPackageobfuscationdictionaryForLocalDevelopment() throws Exception {
        // Local development environment
        File dictFile = new File(testProjectDir, "local-package-dict.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPackageobfuscationdictionaryForContinuousIntegration() throws Exception {
        // CI pipeline configuration
        File dictFile = new File(testProjectDir, "ci-package-dict.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPackageobfuscationdictionaryWithMinimalConfiguration() throws Exception {
        // Absolute minimal configuration
        File dictFile = new File(testProjectDir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    // ============================================================
    // Dictionary Content Scenarios
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryForShortPackageNames() throws Exception {
        // Dictionary with short package names for size optimization
        File dictFile = new File(testProjectDir, "short-package-names.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    void testPackageobfuscationdictionaryForReadablePackageNames() throws Exception {
        // Dictionary with readable package names for debugging
        File dictFile = new File(testProjectDir, "readable-package-names.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPackageobfuscationdictionaryForUniqueBranding() throws Exception {
        // Dictionary with brand-specific unique package names
        File dictFile = new File(testProjectDir, "branded-package-names.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    // ============================================================
    // Multi-variant Scenarios
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryProductionVariant() throws Exception {
        File dir = new File(testProjectDir, "config/production");
        dir.mkdirs();
        File dictFile = new File(dir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    void testPackageobfuscationdictionaryStagingVariant() throws Exception {
        File dir = new File(testProjectDir, "config/staging");
        dir.mkdirs();
        File dictFile = new File(dir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.verbose();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.verbose);
    }

    @Test
    void testPackageobfuscationdictionaryDevelopmentVariant() throws Exception {
        File dir = new File(testProjectDir, "config/development");
        dir.mkdirs();
        File dictFile = new File(dir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);

        assertNotNull(task.configuration.packageObfuscationDictionary);
    }

    @Test
    void testPackageobfuscationdictionaryBetaVariant() throws Exception {
        File dir = new File(testProjectDir, "config/beta");
        dir.mkdirs();
        File dictFile = new File(dir, "package-dictionary.txt");
        dictFile.createNewFile();

        task.packageobfuscationdictionary(dictFile);
        task.allowaccessmodification();

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.allowAccessModification);
    }

    // ============================================================
    // Integration with Other Dictionary Methods
    // ============================================================

    @Test
    void testPackageobfuscationdictionaryWithObfuscationdictionary() throws Exception {
        File generalDict = new File(testProjectDir, "general-dictionary.txt");
        generalDict.createNewFile();
        File packageDict = new File(testProjectDir, "package-dictionary.txt");
        packageDict.createNewFile();

        task.obfuscationdictionary(generalDict);
        task.packageobfuscationdictionary(packageDict);

        assertNotNull(task.configuration.obfuscationDictionary);
        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("general-dictionary.txt"));
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dictionary.txt"));
    }

    @Test
    void testPackageobfuscationdictionaryWithClassobfuscationdictionary() throws Exception {
        File classDict = new File(testProjectDir, "class-dictionary.txt");
        classDict.createNewFile();
        File packageDict = new File(testProjectDir, "package-dictionary.txt");
        packageDict.createNewFile();

        task.classobfuscationdictionary(classDict);
        task.packageobfuscationdictionary(packageDict);

        assertNotNull(task.configuration.classObfuscationDictionary);
        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dictionary.txt"));
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dictionary.txt"));
    }

    @Test
    void testPackageobfuscationdictionaryWithAllDictionaries() throws Exception {
        File generalDict = new File(testProjectDir, "general-dictionary.txt");
        generalDict.createNewFile();
        File classDict = new File(testProjectDir, "class-dictionary.txt");
        classDict.createNewFile();
        File packageDict = new File(testProjectDir, "package-dictionary.txt");
        packageDict.createNewFile();

        task.obfuscationdictionary(generalDict);
        task.classobfuscationdictionary(classDict);
        task.packageobfuscationdictionary(packageDict);

        assertNotNull(task.configuration.obfuscationDictionary);
        assertNotNull(task.configuration.classObfuscationDictionary);
        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("general-dictionary.txt"));
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dictionary.txt"));
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dictionary.txt"));
    }

    @Test
    void testPackageobfuscationdictionaryIndependentOfOtherDictionaries() throws Exception {
        File generalDict = new File(testProjectDir, "general-dictionary.txt");
        generalDict.createNewFile();
        File packageDict = new File(testProjectDir, "package-dictionary.txt");
        packageDict.createNewFile();

        task.obfuscationdictionary(generalDict);

        assertNotNull(task.configuration.obfuscationDictionary);
        assertNull(task.configuration.packageObfuscationDictionary, "packageObfuscationDictionary should still be null");
        assertNull(task.configuration.classObfuscationDictionary, "classObfuscationDictionary should still be null");

        task.packageobfuscationdictionary(packageDict);

        assertNotNull(task.configuration.packageObfuscationDictionary);
        assertNotNull(task.configuration.obfuscationDictionary, "obfuscationDictionary should remain set");
        assertNull(task.configuration.classObfuscationDictionary, "classObfuscationDictionary should still be null");
    }
}
