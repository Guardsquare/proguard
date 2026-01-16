/*
 * ProGuard -- shrinking, optimization, obfuscation, and preverification
 *             of Java bytecode.
 *
 * Copyright (c) 2002-2020 Guardsquare NV
 */
package proguard.gradle.plugin.android.tasks;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CollectConsumerRulesTask.getOutputFile()
 *
 * Tests: proguard.gradle.plugin.android.tasks.CollectConsumerRulesTask.getOutputFile.()Ljava/io/File;
 *
 * The getOutputFile() method returns the File object that was set as the output file.
 * This File represents where the collected consumer rules will be written.
 * The property is marked with @OutputFile annotation, making it a task output.
 */
public class CollectConsumerRulesTaskClaude_getOutputFileTest {

    @TempDir
    Path tempDir;

    private Project project;
    private CollectConsumerRulesTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        task = project.getTasks().create("testCollectConsumerRules", CollectConsumerRulesTask.class);
    }

    // ==================== Basic Getter Tests ====================

    /**
     * Test that getOutputFile returns the file that was set.
     * After setting an output file, the getter should return that same file.
     */
    @Test
    public void testGetOutputFile_returnsSetFile() {
        // Given: An output file is set
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should return the same file
        assertNotNull(result, "getOutputFile() should not return null");
        assertEquals(outputFile, result, "Should return the exact file that was set");
    }

    /**
     * Test that getOutputFile returns non-null after setting.
     * The getter should never return null once a file is set.
     */
    @Test
    public void testGetOutputFile_notNull() {
        // Given: An output file is set
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should not be null
        assertNotNull(result, "getOutputFile() should not return null");
    }

    /**
     * Test that getOutputFile returns File type.
     * The return type should be a File instance.
     */
    @Test
    public void testGetOutputFile_returnsFileType() {
        // Given: An output file is set
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        Object result = task.getOutputFile();

        // Then: Should be a File instance
        assertInstanceOf(File.class, result,
            "getOutputFile() should return File type");
    }

    // ==================== File Path Tests ====================

    /**
     * Test that getOutputFile preserves the file path.
     * The returned file should have the same path as the set file.
     */
    @Test
    public void testGetOutputFile_preservesFilePath() {
        // Given: An output file with a specific path is set
        File outputFile = new File(tempDir.toFile(), "output/consumer-rules.txt");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should preserve the file path
        assertEquals(outputFile.getAbsolutePath(), result.getAbsolutePath(),
            "File path should be preserved");
    }

    /**
     * Test that getOutputFile preserves the file name.
     * The returned file should have the same name as the set file.
     */
    @Test
    public void testGetOutputFile_preservesFileName() {
        // Given: An output file with a specific name is set
        File outputFile = new File(tempDir.toFile(), "my-consumer-rules.pro");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should preserve the file name
        assertEquals(outputFile.getName(), result.getName(),
            "File name should be preserved");
    }

    /**
     * Test that getOutputFile works with absolute paths.
     * The getter should correctly handle files with absolute paths.
     */
    @Test
    public void testGetOutputFile_worksWithAbsolutePath() {
        // Given: An output file with absolute path is set
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt").getAbsoluteFile();
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should return file with absolute path
        assertTrue(result.isAbsolute() || outputFile.isAbsolute(),
            "Should handle absolute paths");
        assertEquals(outputFile.getAbsolutePath(), result.getAbsolutePath(),
            "Absolute paths should match");
    }

    /**
     * Test that getOutputFile works with relative paths.
     * The getter should correctly handle files with relative paths.
     */
    @Test
    public void testGetOutputFile_worksWithRelativePath() {
        // Given: An output file with relative path is set
        File outputFile = new File("consumer-rules.txt");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should return file with relative path
        assertEquals(outputFile.getPath(), result.getPath(),
            "Relative paths should match");
    }

    // ==================== File State Tests ====================

    /**
     * Test that getOutputFile returns file even if it doesn't exist yet.
     * The file doesn't need to exist on the filesystem.
     */
    @Test
    public void testGetOutputFile_fileDoesNotNeedToExist() {
        // Given: An output file that doesn't exist is set
        File outputFile = new File(tempDir.toFile(), "non-existent.txt");
        assertFalse(outputFile.exists(), "File should not exist initially");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should still return the file
        assertNotNull(result, "Should return file even if it doesn't exist");
        assertEquals(outputFile.getPath(), result.getPath(),
            "Path should match even for non-existent file");
    }

    /**
     * Test that getOutputFile returns file even if it already exists.
     * The file can exist on the filesystem.
     */
    @Test
    public void testGetOutputFile_fileCanExist() throws IOException {
        // Given: An output file that exists is set
        File outputFile = new File(tempDir.toFile(), "existing.txt");
        assertTrue(outputFile.createNewFile(), "Should create file");
        assertTrue(outputFile.exists(), "File should exist");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should return the existing file
        assertNotNull(result, "Should return existing file");
        assertEquals(outputFile.getAbsolutePath(), result.getAbsolutePath(),
            "Should return same path for existing file");
        assertTrue(result.exists(), "Returned file should exist");
    }

    // ==================== Different File Types Tests ====================

    /**
     * Test that getOutputFile works with .txt extension.
     * The file extension should be preserved.
     */
    @Test
    public void testGetOutputFile_txtExtension() {
        // Given: An output file with .txt extension is set
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should preserve .txt extension
        assertTrue(result.getName().endsWith(".txt"),
            "Should preserve .txt extension");
    }

    /**
     * Test that getOutputFile works with .pro extension.
     * ProGuard rule files typically use .pro extension.
     */
    @Test
    public void testGetOutputFile_proExtension() {
        // Given: An output file with .pro extension is set
        File outputFile = new File(tempDir.toFile(), "consumer-rules.pro");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should preserve .pro extension
        assertTrue(result.getName().endsWith(".pro"),
            "Should preserve .pro extension");
    }

    /**
     * Test that getOutputFile works with no extension.
     * Files without extensions should be supported.
     */
    @Test
    public void testGetOutputFile_noExtension() {
        // Given: An output file without extension is set
        File outputFile = new File(tempDir.toFile(), "consumer-rules");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should work with no extension
        assertEquals("consumer-rules", result.getName(),
            "Should preserve file name without extension");
    }

    // ==================== Nested Directory Tests ====================

    /**
     * Test that getOutputFile works with nested directories.
     * Files in nested directories should be supported.
     */
    @Test
    public void testGetOutputFile_nestedDirectory() {
        // Given: An output file in nested directories is set
        File outputFile = new File(tempDir.toFile(), "build/outputs/proguard/consumer-rules.txt");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should preserve nested directory structure
        assertTrue(result.getPath().contains("build"),
            "Should contain 'build' in path");
        assertTrue(result.getPath().contains("outputs"),
            "Should contain 'outputs' in path");
        assertTrue(result.getPath().contains("proguard"),
            "Should contain 'proguard' in path");
    }

    /**
     * Test that getOutputFile works with deeply nested directories.
     * Files in deeply nested directory structures should be supported.
     */
    @Test
    public void testGetOutputFile_deeplyNestedDirectory() {
        // Given: An output file in deeply nested directories is set
        File outputFile = new File(tempDir.toFile(), "a/b/c/d/e/consumer-rules.txt");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should preserve deeply nested directory structure
        assertEquals(outputFile.getPath(), result.getPath(),
            "Should preserve deeply nested path");
    }

    // ==================== Multiple Set/Get Tests ====================

    /**
     * Test that getOutputFile returns updated file after setting a new one.
     * The getter should always return the most recently set file.
     */
    @Test
    public void testGetOutputFile_returnsUpdatedFile() {
        // Given: An output file is set
        File firstFile = new File(tempDir.toFile(), "first.txt");
        task.setOutputFile(firstFile);

        // When: Setting a different output file
        File secondFile = new File(tempDir.toFile(), "second.txt");
        task.setOutputFile(secondFile);
        File result = task.getOutputFile();

        // Then: Should return the second file
        assertEquals(secondFile, result,
            "Should return the most recently set file");
        assertNotEquals(firstFile, result,
            "Should not return the first file");
    }

    /**
     * Test that getOutputFile is consistent across multiple calls.
     * Multiple calls to getOutputFile should return the same file.
     */
    @Test
    public void testGetOutputFile_consistentAcrossMultipleCalls() {
        // Given: An output file is set
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");
        task.setOutputFile(outputFile);

        // When: Getting the output file multiple times
        File result1 = task.getOutputFile();
        File result2 = task.getOutputFile();
        File result3 = task.getOutputFile();

        // Then: All results should be equal
        assertEquals(result1, result2,
            "First and second calls should return equal files");
        assertEquals(result2, result3,
            "Second and third calls should return equal files");
        assertEquals(result1, result3,
            "First and third calls should return equal files");
    }

    // ==================== Integration Tests ====================

    /**
     * Test that getOutputFile works after setting other task properties.
     * The output file should remain accessible even after setting other properties.
     */
    @Test
    public void testGetOutputFile_worksAfterSettingOtherProperties() {
        // Given: Output file and other properties are set
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");
        task.setOutputFile(outputFile);
        task.setConsumerRuleFilter(java.util.Collections.emptyList());

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should still return the correct file
        assertEquals(outputFile, result,
            "Output file should be accessible after setting other properties");
    }

    /**
     * Test that getOutputFile works with the task created from different projects.
     * The getter should work regardless of the project the task belongs to.
     */
    @Test
    public void testGetOutputFile_worksWithDifferentProject() {
        // Given: A task from a different project
        Project anotherProject = ProjectBuilder.builder()
                .withProjectDir(tempDir.resolve("another").toFile())
                .build();
        CollectConsumerRulesTask anotherTask = anotherProject.getTasks()
                .create("anotherTask", CollectConsumerRulesTask.class);
        File outputFile = new File(anotherProject.getProjectDir(), "consumer-rules.txt");
        anotherTask.setOutputFile(outputFile);

        // When: Getting the output file
        File result = anotherTask.getOutputFile();

        // Then: Should return the correct file
        assertEquals(outputFile, result,
            "Output file should work with different project");
    }

    // ==================== Special Characters Tests ====================

    /**
     * Test that getOutputFile works with file names containing spaces.
     * File names with spaces should be supported.
     */
    @Test
    public void testGetOutputFile_fileNameWithSpaces() {
        // Given: An output file with spaces in the name is set
        File outputFile = new File(tempDir.toFile(), "consumer rules output.txt");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should preserve file name with spaces
        assertEquals("consumer rules output.txt", result.getName(),
            "File name with spaces should be preserved");
    }

    /**
     * Test that getOutputFile works with file names containing dashes.
     * File names with dashes should be supported.
     */
    @Test
    public void testGetOutputFile_fileNameWithDashes() {
        // Given: An output file with dashes in the name is set
        File outputFile = new File(tempDir.toFile(), "consumer-rules-output.txt");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should preserve file name with dashes
        assertEquals("consumer-rules-output.txt", result.getName(),
            "File name with dashes should be preserved");
    }

    /**
     * Test that getOutputFile works with file names containing underscores.
     * File names with underscores should be supported.
     */
    @Test
    public void testGetOutputFile_fileNameWithUnderscores() {
        // Given: An output file with underscores in the name is set
        File outputFile = new File(tempDir.toFile(), "consumer_rules_output.txt");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should preserve file name with underscores
        assertEquals("consumer_rules_output.txt", result.getName(),
            "File name with underscores should be preserved");
    }

    // ==================== File Object Identity Tests ====================

    /**
     * Test that getOutputFile returns same File object when set via setOutputFile.
     * The getter should return the exact same File object instance.
     */
    @Test
    public void testGetOutputFile_returnsSameFileObject() {
        // Given: An output file is set
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");
        task.setOutputFile(outputFile);

        // When: Getting the output file
        File result = task.getOutputFile();

        // Then: Should return the same File object
        assertSame(outputFile, result,
            "Should return the same File object instance");
    }

    /**
     * Test that getOutputFile on different tasks returns different files.
     * Each task instance should have its own independent output file.
     */
    @Test
    public void testGetOutputFile_differentTasksDifferentFiles() {
        // Given: Two tasks with different output files
        CollectConsumerRulesTask task1 = project.getTasks()
                .create("task1", CollectConsumerRulesTask.class);
        CollectConsumerRulesTask task2 = project.getTasks()
                .create("task2", CollectConsumerRulesTask.class);

        File outputFile1 = new File(tempDir.toFile(), "output1.txt");
        File outputFile2 = new File(tempDir.toFile(), "output2.txt");

        task1.setOutputFile(outputFile1);
        task2.setOutputFile(outputFile2);

        // When: Getting output files from both tasks
        File result1 = task1.getOutputFile();
        File result2 = task2.getOutputFile();

        // Then: Should return different files
        assertNotEquals(result1, result2,
            "Different tasks should have different output files");
        assertEquals(outputFile1, result1,
            "Task1 should return its own output file");
        assertEquals(outputFile2, result2,
            "Task2 should return its own output file");
    }
}
