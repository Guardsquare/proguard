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
 * Test class for CollectConsumerRulesTask.setOutputFile(File)
 *
 * Tests: proguard.gradle.plugin.android.tasks.CollectConsumerRulesTask.setOutputFile.(Ljava/io/File;)V
 *
 * The setOutputFile(File) method sets the output file where collected consumer rules will be written.
 * This File represents the destination for the task's output.
 * The property is marked with @OutputFile annotation, making it a task output.
 */
public class CollectConsumerRulesTaskClaude_setOutputFileTest {

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

    // ==================== Basic Setter Tests ====================

    /**
     * Test that setOutputFile accepts a valid file.
     * The setter should accept a File object without throwing exceptions.
     */
    @Test
    public void testSetOutputFile_acceptsValidFile() {
        // Given: A valid file
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");

        // When: Setting the output file
        // Then: Should not throw any exception
        assertDoesNotThrow(() -> task.setOutputFile(outputFile),
            "setOutputFile should accept valid file without throwing exception");
    }

    /**
     * Test that setOutputFile sets the file correctly.
     * After calling setOutputFile, getOutputFile should return that file.
     */
    @Test
    public void testSetOutputFile_setsFileCorrectly() {
        // Given: A file to set
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: getOutputFile should return the same file
        assertEquals(outputFile, task.getOutputFile(),
            "setOutputFile should set the file so getOutputFile returns it");
    }

    /**
     * Test that setOutputFile returns void.
     * The method should have void return type (no return value to check).
     */
    @Test
    public void testSetOutputFile_hasVoidReturn() {
        // Given: A file to set
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");

        // When: Setting the output file (checking it doesn't return anything meaningful)
        task.setOutputFile(outputFile);

        // Then: Method completes successfully (void return is implicit)
        // This test verifies the method signature is correct
        assertNotNull(task.getOutputFile(), "File should be set after calling setOutputFile");
    }

    // ==================== File Path Tests ====================

    /**
     * Test that setOutputFile works with absolute path.
     * The setter should accept files with absolute paths.
     */
    @Test
    public void testSetOutputFile_worksWithAbsolutePath() {
        // Given: A file with absolute path
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt").getAbsoluteFile();
        assertTrue(outputFile.isAbsolute(), "File should have absolute path");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be set correctly
        assertEquals(outputFile.getAbsolutePath(), task.getOutputFile().getAbsolutePath(),
            "Should handle absolute paths correctly");
    }

    /**
     * Test that setOutputFile works with relative path.
     * The setter should accept files with relative paths.
     */
    @Test
    public void testSetOutputFile_worksWithRelativePath() {
        // Given: A file with relative path
        File outputFile = new File("consumer-rules.txt");
        assertFalse(outputFile.isAbsolute(), "File should have relative path");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be set correctly
        assertEquals(outputFile.getPath(), task.getOutputFile().getPath(),
            "Should handle relative paths correctly");
    }

    /**
     * Test that setOutputFile preserves the file path.
     * The exact path provided should be preserved.
     */
    @Test
    public void testSetOutputFile_preservesFilePath() {
        // Given: A file with a specific path
        File outputFile = new File(tempDir.toFile(), "output/consumer-rules.txt");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Path should be preserved
        assertEquals(outputFile.getPath(), task.getOutputFile().getPath(),
            "File path should be preserved exactly");
    }

    // ==================== File State Tests ====================

    /**
     * Test that setOutputFile works with non-existent file.
     * The file doesn't need to exist on the filesystem.
     */
    @Test
    public void testSetOutputFile_worksWithNonExistentFile() {
        // Given: A file that doesn't exist
        File outputFile = new File(tempDir.toFile(), "non-existent.txt");
        assertFalse(outputFile.exists(), "File should not exist");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be set successfully
        assertEquals(outputFile, task.getOutputFile(),
            "Should accept non-existent file");
    }

    /**
     * Test that setOutputFile works with existing file.
     * The file can already exist on the filesystem.
     */
    @Test
    public void testSetOutputFile_worksWithExistingFile() throws IOException {
        // Given: A file that exists
        File outputFile = new File(tempDir.toFile(), "existing.txt");
        assertTrue(outputFile.createNewFile(), "Should create file");
        assertTrue(outputFile.exists(), "File should exist");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be set successfully
        assertEquals(outputFile, task.getOutputFile(),
            "Should accept existing file");
    }

    /**
     * Test that setOutputFile works with file in non-existent directory.
     * The parent directory doesn't need to exist.
     */
    @Test
    public void testSetOutputFile_worksWithNonExistentDirectory() {
        // Given: A file in a directory that doesn't exist
        File outputFile = new File(tempDir.toFile(), "non-existent-dir/consumer-rules.txt");
        assertFalse(outputFile.getParentFile().exists(), "Parent directory should not exist");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be set successfully
        assertEquals(outputFile, task.getOutputFile(),
            "Should accept file in non-existent directory");
    }

    // ==================== Different File Types Tests ====================

    /**
     * Test that setOutputFile works with .txt extension.
     * Text files should be accepted.
     */
    @Test
    public void testSetOutputFile_txtExtension() {
        // Given: A file with .txt extension
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be accepted
        assertTrue(task.getOutputFile().getName().endsWith(".txt"),
            "Should accept .txt files");
    }

    /**
     * Test that setOutputFile works with .pro extension.
     * ProGuard rule files typically use .pro extension.
     */
    @Test
    public void testSetOutputFile_proExtension() {
        // Given: A file with .pro extension
        File outputFile = new File(tempDir.toFile(), "consumer-rules.pro");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be accepted
        assertTrue(task.getOutputFile().getName().endsWith(".pro"),
            "Should accept .pro files");
    }

    /**
     * Test that setOutputFile works with no extension.
     * Files without extensions should be accepted.
     */
    @Test
    public void testSetOutputFile_noExtension() {
        // Given: A file without extension
        File outputFile = new File(tempDir.toFile(), "consumer-rules");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be accepted
        assertEquals("consumer-rules", task.getOutputFile().getName(),
            "Should accept files without extension");
    }

    // ==================== Nested Directory Tests ====================

    /**
     * Test that setOutputFile works with nested directories.
     * Files in nested directory structures should be accepted.
     */
    @Test
    public void testSetOutputFile_nestedDirectory() {
        // Given: A file in nested directories
        File outputFile = new File(tempDir.toFile(), "build/outputs/proguard/consumer-rules.txt");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be accepted
        assertTrue(task.getOutputFile().getPath().contains("build"),
            "Should handle nested directories");
    }

    /**
     * Test that setOutputFile works with deeply nested directories.
     * Files in deeply nested structures should be accepted.
     */
    @Test
    public void testSetOutputFile_deeplyNestedDirectory() {
        // Given: A file in deeply nested directories
        File outputFile = new File(tempDir.toFile(), "a/b/c/d/e/consumer-rules.txt");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be accepted
        assertEquals(outputFile.getPath(), task.getOutputFile().getPath(),
            "Should handle deeply nested directories");
    }

    // ==================== Multiple Set Tests ====================

    /**
     * Test that setOutputFile can be called multiple times.
     * The setter should allow changing the output file.
     */
    @Test
    public void testSetOutputFile_canBeCalledMultipleTimes() {
        // Given: Setting an output file
        File firstFile = new File(tempDir.toFile(), "first.txt");
        task.setOutputFile(firstFile);

        // When: Setting a different output file
        File secondFile = new File(tempDir.toFile(), "second.txt");
        task.setOutputFile(secondFile);

        // Then: Should update to the new file
        assertEquals(secondFile, task.getOutputFile(),
            "Should update to the new file");
        assertNotEquals(firstFile, task.getOutputFile(),
            "Should not return the old file");
    }

    /**
     * Test that setOutputFile overwrites previous value.
     * Each call to setOutputFile should replace the previous value.
     */
    @Test
    public void testSetOutputFile_overwritesPreviousValue() {
        // Given: Multiple files
        File file1 = new File(tempDir.toFile(), "file1.txt");
        File file2 = new File(tempDir.toFile(), "file2.txt");
        File file3 = new File(tempDir.toFile(), "file3.txt");

        // When: Setting the output file multiple times
        task.setOutputFile(file1);
        task.setOutputFile(file2);
        task.setOutputFile(file3);

        // Then: Should only have the last value
        assertEquals(file3, task.getOutputFile(),
            "Should have the last set value");
    }

    /**
     * Test that setOutputFile can change from one directory to another.
     * The setter should allow changing directories.
     */
    @Test
    public void testSetOutputFile_canChangeBetweenDirectories() {
        // Given: Files in different directories
        File fileInDir1 = new File(tempDir.toFile(), "dir1/output.txt");
        File fileInDir2 = new File(tempDir.toFile(), "dir2/output.txt");

        // When: Setting files from different directories
        task.setOutputFile(fileInDir1);
        assertEquals(fileInDir1, task.getOutputFile(), "Should set first directory");

        task.setOutputFile(fileInDir2);

        // Then: Should update to the new directory
        assertEquals(fileInDir2, task.getOutputFile(),
            "Should change to different directory");
    }

    // ==================== Integration Tests ====================

    /**
     * Test that setOutputFile works with other task properties.
     * Setting output file shouldn't interfere with other properties.
     */
    @Test
    public void testSetOutputFile_worksWithOtherProperties() {
        // Given: Other task properties are set
        task.setConsumerRuleFilter(java.util.Collections.emptyList());

        // When: Setting the output file
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");
        task.setOutputFile(outputFile);

        // Then: Both properties should be accessible
        assertEquals(outputFile, task.getOutputFile(),
            "Output file should be set");
        assertNotNull(task.getConsumerRuleFilter(),
            "Other properties should still be accessible");
    }

    /**
     * Test that setOutputFile on different tasks are independent.
     * Each task instance should maintain its own output file.
     */
    @Test
    public void testSetOutputFile_independentAcrossTasks() {
        // Given: Two different tasks
        CollectConsumerRulesTask task1 = project.getTasks()
                .create("task1", CollectConsumerRulesTask.class);
        CollectConsumerRulesTask task2 = project.getTasks()
                .create("task2", CollectConsumerRulesTask.class);

        File file1 = new File(tempDir.toFile(), "output1.txt");
        File file2 = new File(tempDir.toFile(), "output2.txt");

        // When: Setting different output files
        task1.setOutputFile(file1);
        task2.setOutputFile(file2);

        // Then: Each task should have its own file
        assertEquals(file1, task1.getOutputFile(),
            "Task1 should have its file");
        assertEquals(file2, task2.getOutputFile(),
            "Task2 should have its file");
        assertNotEquals(task1.getOutputFile(), task2.getOutputFile(),
            "Tasks should have different files");
    }

    /**
     * Test that setOutputFile doesn't modify the file on disk.
     * Setting the output file should not create or modify the actual file.
     */
    @Test
    public void testSetOutputFile_doesNotModifyDisk() {
        // Given: A file that doesn't exist
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");
        assertFalse(outputFile.exists(), "File should not exist initially");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: File should still not exist
        assertFalse(outputFile.exists(),
            "setOutputFile should not create the file on disk");
    }

    // ==================== Special Characters Tests ====================

    /**
     * Test that setOutputFile works with file names containing spaces.
     * File names with spaces should be accepted.
     */
    @Test
    public void testSetOutputFile_fileNameWithSpaces() {
        // Given: A file with spaces in the name
        File outputFile = new File(tempDir.toFile(), "consumer rules output.txt");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be accepted
        assertEquals("consumer rules output.txt", task.getOutputFile().getName(),
            "Should accept file names with spaces");
    }

    /**
     * Test that setOutputFile works with file names containing dashes.
     * File names with dashes should be accepted.
     */
    @Test
    public void testSetOutputFile_fileNameWithDashes() {
        // Given: A file with dashes in the name
        File outputFile = new File(tempDir.toFile(), "consumer-rules-output.txt");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be accepted
        assertEquals("consumer-rules-output.txt", task.getOutputFile().getName(),
            "Should accept file names with dashes");
    }

    /**
     * Test that setOutputFile works with file names containing underscores.
     * File names with underscores should be accepted.
     */
    @Test
    public void testSetOutputFile_fileNameWithUnderscores() {
        // Given: A file with underscores in the name
        File outputFile = new File(tempDir.toFile(), "consumer_rules_output.txt");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be accepted
        assertEquals("consumer_rules_output.txt", task.getOutputFile().getName(),
            "Should accept file names with underscores");
    }

    /**
     * Test that setOutputFile works with file names containing dots.
     * Multiple dots in file names should be accepted.
     */
    @Test
    public void testSetOutputFile_fileNameWithMultipleDots() {
        // Given: A file with multiple dots
        File outputFile = new File(tempDir.toFile(), "consumer.rules.output.txt");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be accepted
        assertEquals("consumer.rules.output.txt", task.getOutputFile().getName(),
            "Should accept file names with multiple dots");
    }

    // ==================== File Object Reference Tests ====================

    /**
     * Test that setOutputFile stores the same File object.
     * The exact File object instance should be stored.
     */
    @Test
    public void testSetOutputFile_storesSameFileObject() {
        // Given: A File object
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should store the same object
        assertSame(outputFile, task.getOutputFile(),
            "Should store the same File object instance");
    }

    /**
     * Test that setOutputFile accepts same file twice.
     * Setting the same file again should work.
     */
    @Test
    public void testSetOutputFile_acceptsSameFileTwice() {
        // Given: A File object
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");

        // When: Setting the same file twice
        task.setOutputFile(outputFile);
        task.setOutputFile(outputFile);

        // Then: Should still be set correctly
        assertEquals(outputFile, task.getOutputFile(),
            "Should accept setting same file twice");
    }

    // ==================== Edge Cases ====================

    /**
     * Test that setOutputFile works with file at project root.
     * Files at the project root directory should be accepted.
     */
    @Test
    public void testSetOutputFile_fileAtProjectRoot() {
        // Given: A file at project root
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be accepted
        assertEquals(outputFile, task.getOutputFile(),
            "Should accept file at project root");
    }

    /**
     * Test that setOutputFile works with long file name.
     * Long file names should be accepted.
     */
    @Test
    public void testSetOutputFile_longFileName() {
        // Given: A file with a very long name
        String longName = "consumer-rules-output-file-with-a-very-long-name-for-testing-purposes.txt";
        File outputFile = new File(tempDir.toFile(), longName);

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be accepted
        assertEquals(longName, task.getOutputFile().getName(),
            "Should accept long file names");
    }

    /**
     * Test that setOutputFile works with file name containing numbers.
     * File names with numbers should be accepted.
     */
    @Test
    public void testSetOutputFile_fileNameWithNumbers() {
        // Given: A file with numbers in the name
        File outputFile = new File(tempDir.toFile(), "consumer-rules-2024-01-15.txt");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be accepted
        assertTrue(task.getOutputFile().getName().contains("2024"),
            "Should accept file names with numbers");
    }

    // ==================== Gradle Task Integration Tests ====================

    /**
     * Test that setOutputFile integrates with Gradle task system.
     * The property should be accessible through the task.
     */
    @Test
    public void testSetOutputFile_integratesWithGradleTask() {
        // Given: A file to set
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");

        // When: Setting through the task
        task.setOutputFile(outputFile);

        // Then: Should be accessible through Gradle task methods
        assertNotNull(task.getOutputFile(),
            "Output file should be accessible through task");
        assertEquals(task.getProject(), project,
            "Task should still belong to correct project");
    }

    /**
     * Test that setOutputFile can be called immediately after task creation.
     * The setter should be available right after task instantiation.
     */
    @Test
    public void testSetOutputFile_availableImmediatelyAfterCreation() {
        // Given: A newly created task
        CollectConsumerRulesTask newTask = project.getTasks()
                .create("newTask", CollectConsumerRulesTask.class);
        File outputFile = new File(tempDir.toFile(), "consumer-rules.txt");

        // When: Setting output file immediately
        newTask.setOutputFile(outputFile);

        // Then: Should work without issues
        assertEquals(outputFile, newTask.getOutputFile(),
            "Should be able to set output file immediately after task creation");
    }

    /**
     * Test that setOutputFile works with file from different project directory.
     * Files outside the project directory should be accepted.
     */
    @Test
    public void testSetOutputFile_fileOutsideProjectDirectory() {
        // Given: A file outside the project directory
        File outputFile = new File(tempDir.getParent().toFile(), "external-consumer-rules.txt");

        // When: Setting the output file
        task.setOutputFile(outputFile);

        // Then: Should be accepted
        assertEquals(outputFile, task.getOutputFile(),
            "Should accept files outside project directory");
    }
}
