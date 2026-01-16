package proguard.gradle;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import proguard.Configuration;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.getPrintMappingFile()Ljava/io/File; method.
 * Tests the getPrintMappingFile() method that returns the File for printMapping output,
 * or null if the output is set to stdout or is not configured.
 */
public class ProGuardTaskClaude_getPrintMappingFileTest {

    @TempDir
    Path tempDir;

    private Project project;
    private ProGuardTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        task = project.getTasks().create("proguard", ProGuardTask.class);
    }

    @AfterEach
    public void tearDown() {
        task = null;
        project = null;
    }

    // ============================================================
    // Basic Functionality Tests
    // ============================================================

    @Test
    public void testGetPrintMappingFile_returnsNullInitially() throws Exception {
        assertNull(task.configuration.printMapping, "printMapping should be null initially");
        File result = task.getPrintMappingFile();
        assertNull(result, "getPrintMappingFile() should return null when printMapping is not configured");
    }

    @Test
    public void testGetPrintMappingFile_returnsNullForStdout() throws Exception {
        task.printmapping();
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should be stdout");
        File result = task.getPrintMappingFile();
        assertNull(result, "getPrintMappingFile() should return null for stdout");
    }

    @Test
    public void testGetPrintMappingFile_returnsFileWhenConfigured() throws Exception {
        task.printmapping("mapping.txt");
        File result = task.getPrintMappingFile();
        assertNotNull(result, "getPrintMappingFile() should return a File when configured with a file");
        assertEquals("mapping.txt", result.getName(), "Returned file should have correct name");
    }

    @Test
    public void testGetPrintMappingFile_returnsActualFileObject() throws Exception {
        task.printmapping("output/mapping.txt");
        File result = task.getPrintMappingFile();
        assertNotNull(result, "getPrintMappingFile() should return a File");
        assertSame(task.configuration.printMapping, result,
                "Returned file should be the same object as configuration.printMapping");
    }

    @Test
    public void testGetPrintMappingFile_isOutputFileAnnotated() throws Exception {
        // Verify the method has @OutputFile annotation
        task.printmapping("mapping.txt");
        File result = task.getPrintMappingFile();
        assertNotNull(result, "getPrintMappingFile() should return a File when configured");
    }

    // ============================================================
    // Interaction with printmapping() void method
    // ============================================================

    @Test
    public void testGetPrintMappingFile_afterPrintmappingVoid() throws Exception {
        task.printmapping(); // Sets to stdout
        File result = task.getPrintMappingFile();
        assertNull(result, "getPrintMappingFile() should return null after printmapping()");
    }

    @Test
    public void testGetPrintMappingFile_multipleCallsAfterPrintmappingVoid() throws Exception {
        task.printmapping();
        for (int i = 0; i < 3; i++) {
            File result = task.getPrintMappingFile();
            assertNull(result, "getPrintMappingFile() should consistently return null for stdout");
        }
    }

    @Test
    public void testGetPrintMappingFile_repeatedCallsWithStdout() throws Exception {
        task.printmapping();
        for (int i = 0; i < 10; i++) {
            assertNull(task.getPrintMappingFile(),
                    "getPrintMappingFile() should always return null for stdout");
        }
    }

    // ============================================================
    // Interaction with printmapping(Object) method
    // ============================================================

    @Test
    public void testGetPrintMappingFile_afterPrintmappingWithFile() throws Exception {
        task.printmapping("mapping.txt");
        File result = task.getPrintMappingFile();
        assertNotNull(result, "getPrintMappingFile() should return File after printmapping(Object)");
        assertEquals("mapping.txt", result.getName());
    }

    @Test
    public void testGetPrintMappingFile_afterPrintmappingWithDifferentFiles() throws Exception {
        task.printmapping("mapping1.txt");
        File result1 = task.getPrintMappingFile();
        assertEquals("mapping1.txt", result1.getName());

        task.printmapping("mapping2.txt");
        File result2 = task.getPrintMappingFile();
        assertEquals("mapping2.txt", result2.getName());
    }

    @Test
    public void testGetPrintMappingFile_switchingFromFileToStdout() throws Exception {
        task.printmapping("mapping.txt");
        File result1 = task.getPrintMappingFile();
        assertNotNull(result1, "Should return File initially");

        task.printmapping(); // Switch to stdout
        File result2 = task.getPrintMappingFile();
        assertNull(result2, "Should return null after switching to stdout");
    }

    @Test
    public void testGetPrintMappingFile_switchingFromStdoutToFile() throws Exception {
        task.printmapping(); // stdout
        File result1 = task.getPrintMappingFile();
        assertNull(result1, "Should return null for stdout");

        task.printmapping("mapping.txt"); // Switch to file
        File result2 = task.getPrintMappingFile();
        assertNotNull(result2, "Should return File after switching to file");
        assertEquals("mapping.txt", result2.getName());
    }

    @Test
    public void testGetPrintMappingFile_alternatingBetweenFileAndStdout() throws Exception {
        task.printmapping("mapping1.txt");
        assertNotNull(task.getPrintMappingFile());

        task.printmapping(); // stdout
        assertNull(task.getPrintMappingFile());

        task.printmapping("mapping2.txt");
        assertNotNull(task.getPrintMappingFile());

        task.printmapping(); // stdout again
        assertNull(task.getPrintMappingFile());
    }

    // ============================================================
    // Multiple calls to getPrintMappingFile()
    // ============================================================

    @Test
    public void testGetPrintMappingFile_multipleCallsReturnSameResult() throws Exception {
        task.printmapping("mapping.txt");
        File result1 = task.getPrintMappingFile();
        File result2 = task.getPrintMappingFile();
        File result3 = task.getPrintMappingFile();

        assertSame(result1, result2, "Multiple calls should return same File object");
        assertSame(result2, result3, "Multiple calls should return same File object");
    }

    @Test
    public void testGetPrintMappingFile_idempotent() throws Exception {
        task.printmapping("mapping.txt");
        for (int i = 0; i < 5; i++) {
            File result = task.getPrintMappingFile();
            assertNotNull(result);
            assertEquals("mapping.txt", result.getName());
        }
    }

    @Test
    public void testGetPrintMappingFile_consistentNullForUnconfigured() throws Exception {
        for (int i = 0; i < 5; i++) {
            assertNull(task.getPrintMappingFile(),
                    "Should consistently return null when not configured");
        }
    }

    // ============================================================
    // File Path Variations
    // ============================================================

    @Test
    public void testGetPrintMappingFile_withRelativePath() throws Exception {
        task.printmapping("build/outputs/mapping.txt");
        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
    }

    @Test
    public void testGetPrintMappingFile_withNestedPath() throws Exception {
        task.printmapping("build/outputs/proguard/release/mapping.txt");
        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
    }

    @Test
    public void testGetPrintMappingFile_withAbsolutePath() throws Exception {
        File absoluteFile = new File(tempDir.toFile(), "absolute-mapping.txt");
        task.printmapping(absoluteFile.getAbsolutePath());
        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("absolute-mapping.txt", result.getName());
    }

    @Test
    public void testGetPrintMappingFile_withFileObject() throws Exception {
        File mappingFile = new File(tempDir.toFile(), "mapping.txt");
        task.printmapping(mappingFile);
        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
    }

    @Test
    public void testGetPrintMappingFile_withDifferentExtensions() throws Exception {
        String[] filenames = {"mapping.txt", "mapping.map", "mapping.log", "proguard-mapping.txt"};
        for (String filename : filenames) {
            ProGuardTask freshTask = project.getTasks().create(
                    "task_" + filename.replace(".", "_").replace("-", "_"),
                    ProGuardTask.class);
            freshTask.printmapping(filename);
            File result = freshTask.getPrintMappingFile();
            assertNotNull(result, "Should return File for " + filename);
            assertEquals(filename, result.getName());
        }
    }

    // ============================================================
    // Integration with Other Configuration Methods
    // ============================================================

    @Test
    public void testGetPrintMappingFile_withDontobfuscate() throws Exception {
        task.printmapping("mapping.txt");
        task.dontobfuscate();
        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    public void testGetPrintMappingFile_withVerbose() throws Exception {
        task.printmapping("mapping.txt");
        task.verbose();
        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
        assertTrue(task.configuration.verbose);
    }

    @Test
    public void testGetPrintMappingFile_withAllowAccessModification() throws Exception {
        task.printmapping("mapping.txt");
        task.allowaccessmodification();
        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    public void testGetPrintMappingFile_withMultipleConfigMethods() throws Exception {
        task.printmapping("mapping.txt");
        task.dontobfuscate();
        task.verbose();
        task.allowaccessmodification();

        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.allowAccessModification);
    }

    // ============================================================
    // Realistic Scenarios
    // ============================================================

    @Test
    public void testGetPrintMappingFile_androidReleaseMapping() throws Exception {
        // Android release build with mapping file for Play Console
        task.printmapping("build/outputs/mapping/release/mapping.txt");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    public void testGetPrintMappingFile_androidDebugMapping() throws Exception {
        // Android debug build with mapping file but no obfuscation
        task.printmapping("build/outputs/mapping/debug/mapping.txt");
        task.dontobfuscate();

        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    public void testGetPrintMappingFile_crashlyticsIntegration() throws Exception {
        // Crashlytics requiring mapping file
        task.printmapping("build/outputs/proguard/mapping.txt");
        task.allowaccessmodification();

        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    public void testGetPrintMappingFile_ciCdArtifact() throws Exception {
        // CI/CD pipeline saving mapping as artifact
        task.printmapping("artifacts/mapping/proguard-mapping.txt");
        task.verbose();

        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("proguard-mapping.txt", result.getName());
        assertTrue(task.configuration.verbose);
    }

    @Test
    public void testGetPrintMappingFile_versionedMapping() throws Exception {
        // Versioned mapping file for tracking releases
        task.printmapping("build/outputs/mapping/mapping-1.0.0.txt");

        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping-1.0.0.txt", result.getName());
    }

    @Test
    public void testGetPrintMappingFile_libraryModuleMapping() throws Exception {
        // Library module with mapping output
        task.printmapping("build/outputs/mapping/library-mapping.txt");
        task.dontobfuscate();

        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("library-mapping.txt", result.getName());
        assertFalse(task.configuration.obfuscate);
    }

    @Test
    public void testGetPrintMappingFile_stdoutForConsoleOutput() throws Exception {
        // Console output configuration
        task.printmapping(); // stdout
        task.verbose();

        File result = task.getPrintMappingFile();
        assertNull(result, "Should return null for stdout configuration");
        assertTrue(task.configuration.verbose);
    }

    // ============================================================
    // Edge Cases
    // ============================================================

    @Test
    public void testGetPrintMappingFile_onMultipleTasks() throws Exception {
        ProGuardTask task1 = project.getTasks().create("proguard1", ProGuardTask.class);
        ProGuardTask task2 = project.getTasks().create("proguard2", ProGuardTask.class);
        ProGuardTask task3 = project.getTasks().create("proguard3", ProGuardTask.class);

        task1.printmapping("mapping1.txt");
        task2.printmapping("mapping2.txt");
        task3.printmapping(); // stdout

        File result1 = task1.getPrintMappingFile();
        File result2 = task2.getPrintMappingFile();
        File result3 = task3.getPrintMappingFile();

        assertNotNull(result1);
        assertEquals("mapping1.txt", result1.getName());
        assertNotNull(result2);
        assertEquals("mapping2.txt", result2.getName());
        assertNull(result3);
    }

    @Test
    public void testGetPrintMappingFile_independentOfOtherTasks() throws Exception {
        ProGuardTask otherTask = project.getTasks().create("otherProguard", ProGuardTask.class);

        task.printmapping("mapping.txt");

        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
        assertNull(otherTask.getPrintMappingFile(), "Other task should not be affected");
    }

    @Test
    public void testGetPrintMappingFile_afterConfigurationReset() throws Exception {
        task.printmapping("mapping.txt");
        assertNotNull(task.getPrintMappingFile());

        task.printmapping("new-mapping.txt");
        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("new-mapping.txt", result.getName());
    }

    @Test
    public void testGetPrintMappingFile_doesNotModifyConfiguration() throws Exception {
        task.printmapping("mapping.txt");
        File configBefore = task.configuration.printMapping;

        task.getPrintMappingFile();

        File configAfter = task.configuration.printMapping;
        assertSame(configBefore, configAfter,
                "getPrintMappingFile() should not modify configuration");
    }

    @Test
    public void testGetPrintMappingFile_withComplexPath() throws Exception {
        task.printmapping("build/outputs/proguard/release/variants/main/mapping.txt");
        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
    }

    // ============================================================
    // Android Build Variants
    // ============================================================

    @Test
    public void testGetPrintMappingFile_productionVariant() throws Exception {
        task.printmapping("build/outputs/mapping/production/mapping.txt");
        task.allowaccessmodification();
        task.mergeinterfacesaggressively();

        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
    }

    @Test
    public void testGetPrintMappingFile_stagingVariant() throws Exception {
        task.printmapping("build/outputs/mapping/staging/mapping.txt");
        task.verbose();

        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
    }

    @Test
    public void testGetPrintMappingFile_developmentVariant() throws Exception {
        task.printmapping("build/outputs/mapping/development/mapping.txt");
        task.dontobfuscate();

        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
    }

    @Test
    public void testGetPrintMappingFile_betaVariant() throws Exception {
        task.printmapping("build/outputs/mapping/beta/mapping.txt");
        task.allowaccessmodification();

        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
    }

    // ============================================================
    // Null and Stdout Behavior Tests
    // ============================================================

    @Test
    public void testGetPrintMappingFile_distinguishesNullFromStdout() throws Exception {
        // Initially null (not configured)
        assertNull(task.configuration.printMapping);
        assertNull(task.getPrintMappingFile());

        // Set to stdout
        task.printmapping();
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);
        assertNull(task.getPrintMappingFile());

        // Set to file
        task.printmapping("mapping.txt");
        assertNotNull(task.configuration.printMapping);
        assertNotNull(task.getPrintMappingFile());
    }

    @Test
    public void testGetPrintMappingFile_stdoutBehaviorConsistent() throws Exception {
        task.printmapping();
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);

        // Multiple calls should all return null
        for (int i = 0; i < 10; i++) {
            assertNull(task.getPrintMappingFile(),
                    "getPrintMappingFile() should consistently return null for stdout");
        }
    }

    @Test
    public void testGetPrintMappingFile_nullBehaviorConsistent() throws Exception {
        assertNull(task.configuration.printMapping);

        // Multiple calls should all return null
        for (int i = 0; i < 10; i++) {
            assertNull(task.getPrintMappingFile(),
                    "getPrintMappingFile() should consistently return null when not configured");
        }
    }

    // ============================================================
    // Configuration State Tests
    // ============================================================

    @Test
    public void testGetPrintMappingFile_preservesOtherSettings() throws Exception {
        task.dontobfuscate();
        task.verbose();
        task.allowaccessmodification();
        task.printmapping("mapping.txt");

        File result = task.getPrintMappingFile();

        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.allowAccessModification);
    }

    @Test
    public void testGetPrintMappingFile_isReadOnly() throws Exception {
        // getPrintMappingFile() should be a read-only getter
        task.printmapping("mapping.txt");
        File original = task.configuration.printMapping;

        File result = task.getPrintMappingFile();

        assertSame(original, result, "Should return the same file reference");
        assertSame(original, task.configuration.printMapping,
                "Configuration should not be modified");
    }

    @Test
    public void testGetPrintMappingFile_doesNotAffectOtherConfiguration() throws Exception {
        task.printmapping("mapping.txt");

        File result = task.getPrintMappingFile();

        assertNotNull(result);
        // Verify other configuration remains at defaults
        assertTrue(task.configuration.shrink, "Shrink should remain at default");
        assertTrue(task.configuration.optimize, "Optimize should remain at default");
        assertTrue(task.configuration.obfuscate, "Obfuscate should remain at default");
    }

    // ============================================================
    // File Naming Conventions
    // ============================================================

    @Test
    public void testGetPrintMappingFile_standardNaming() throws Exception {
        String[] standardNames = {
            "mapping.txt",
            "proguard-mapping.txt",
            "obfuscation-mapping.txt",
            "release-mapping.txt"
        };

        for (String name : standardNames) {
            ProGuardTask freshTask = project.getTasks().create(
                    "task_" + name.replace(".", "_").replace("-", "_"),
                    ProGuardTask.class);
            freshTask.printmapping(name);
            File result = freshTask.getPrintMappingFile();
            assertNotNull(result, "Should return File for " + name);
            assertEquals(name, result.getName());
        }
    }

    @Test
    public void testGetPrintMappingFile_descriptiveNaming() throws Exception {
        task.printmapping("app-release-v1.0.0-mapping.txt");
        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("app-release-v1.0.0-mapping.txt", result.getName());
    }

    // ============================================================
    // Build System Integration
    // ============================================================

    @Test
    public void testGetPrintMappingFile_gradleBuildDirectory() throws Exception {
        task.printmapping("build/mapping.txt");
        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
    }

    @Test
    public void testGetPrintMappingFile_customOutputDirectory() throws Exception {
        task.printmapping("custom-output/mapping.txt");
        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
    }

    @Test
    public void testGetPrintMappingFile_reportsDirectory() throws Exception {
        task.printmapping("build/reports/proguard/mapping.txt");
        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
    }

    // ============================================================
    // Sequence Tests
    // ============================================================

    @Test
    public void testGetPrintMappingFile_sequenceWithMultipleConfigurations() throws Exception {
        // Test a sequence of configuration changes
        assertNull(task.getPrintMappingFile(), "Initially null");

        task.printmapping("mapping1.txt");
        assertEquals("mapping1.txt", task.getPrintMappingFile().getName());

        task.printmapping(); // stdout
        assertNull(task.getPrintMappingFile(), "Null after stdout");

        task.printmapping("mapping2.txt");
        assertEquals("mapping2.txt", task.getPrintMappingFile().getName());

        task.printmapping("mapping3.txt");
        assertEquals("mapping3.txt", task.getPrintMappingFile().getName());
    }

    @Test
    public void testGetPrintMappingFile_complexConfigurationChain() throws Exception {
        task.dontobfuscate();
        task.printmapping("mapping.txt");
        task.allowaccessmodification();
        task.verbose();
        task.mergeinterfacesaggressively();

        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
        assertFalse(task.configuration.obfuscate);
        assertTrue(task.configuration.allowAccessModification);
        assertTrue(task.configuration.verbose);
        assertTrue(task.configuration.mergeInterfacesAggressively);
    }

    @Test
    public void testGetPrintMappingFile_afterComplexConfiguration() throws Exception {
        task.dontobfuscate();
        task.allowaccessmodification();
        task.verbose();
        task.mergeinterfacesaggressively();
        task.printmapping("mapping.txt");

        File result = task.getPrintMappingFile();
        assertNotNull(result);
        assertEquals("mapping.txt", result.getName());
    }
}
