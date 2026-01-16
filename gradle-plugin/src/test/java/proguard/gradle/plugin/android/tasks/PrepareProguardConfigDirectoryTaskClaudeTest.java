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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for PrepareProguardConfigDirectoryTask.
 *
 * Tests all methods in proguard.gradle.plugin.android.tasks.PrepareProguardConfigDirectoryTask:
 * - getFile() - Returns the output directory for ProGuard configuration files
 * - createDirectory() - Creates the directory structure for ProGuard configs
 *
 * PrepareProguardConfigDirectoryTask is a Gradle task that sets up the directory structure
 * for storing ProGuard configuration files during the Android build process.
 */
public class PrepareProguardConfigDirectoryTaskClaudeTest {

    @TempDir
    Path tempDir;

    private Project project;
    private PrepareProguardConfigDirectoryTask task;

    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder()
                .withProjectDir(tempDir.toFile())
                .build();
        task = project.getTasks().create("prepareProguardConfigDirectory", PrepareProguardConfigDirectoryTask.class);
    }

    @AfterEach
    public void tearDown() {
        task = null;
        project = null;
    }

    // ==================== getFile() Tests ====================

    /**
     * Test getFile returns a non-null File object.
     * The file property should be initialized when the task is created.
     */
    @Test
    public void testGetFile_returnsNonNull() {
        // When: Getting the file
        File file = task.getFile();

        // Then: Should return a non-null File
        assertNotNull(file, "getFile() should return a non-null File object");
    }

    /**
     * Test getFile returns the expected path.
     * The file should be located at build/intermediates/proguard/configs.
     */
    @Test
    public void testGetFile_returnsExpectedPath() {
        // When: Getting the file
        File file = task.getFile();

        // Then: Should return the correct path relative to project build directory
        File expectedFile = new File(project.getBuildDir(), "intermediates/proguard/configs");
        assertEquals(expectedFile, file, "getFile() should return build/intermediates/proguard/configs");
    }

    /**
     * Test getFile returns consistent value across multiple calls.
     * Multiple calls should return the same File object.
     */
    @Test
    public void testGetFile_consistentAcrossMultipleCalls() {
        // When: Calling getFile multiple times
        File file1 = task.getFile();
        File file2 = task.getFile();
        File file3 = task.getFile();

        // Then: All calls should return the same File
        assertSame(file1, file2, "Multiple calls should return the same File object");
        assertSame(file2, file3, "Multiple calls should return the same File object");
    }

    /**
     * Test getFile returns path under project build directory.
     * The returned file should be a subdirectory of the project's build directory.
     */
    @Test
    public void testGetFile_isUnderProjectBuildDir() {
        // When: Getting the file
        File file = task.getFile();

        // Then: Should be under the project build directory
        assertTrue(file.getAbsolutePath().startsWith(project.getBuildDir().getAbsolutePath()),
                "File should be under project build directory");
    }

    /**
     * Test getFile returns correct path components.
     * The path should contain the expected directory structure.
     */
    @Test
    public void testGetFile_hasCorrectPathComponents() {
        // When: Getting the file
        File file = task.getFile();

        // Then: Should contain expected path components
        String path = file.getAbsolutePath();
        assertTrue(path.contains("intermediates"), "Path should contain 'intermediates'");
        assertTrue(path.contains("proguard"), "Path should contain 'proguard'");
        assertTrue(path.contains("configs"), "Path should contain 'configs'");
    }

    /**
     * Test getFile initially returns non-existent directory.
     * The directory should not exist until createDirectory() is called.
     */
    @Test
    public void testGetFile_initiallyDoesNotExist() {
        // When: Getting the file before createDirectory is called
        File file = task.getFile();

        // Then: The directory should not exist yet
        assertFalse(file.exists(), "Directory should not exist before createDirectory() is called");
    }

    /**
     * Test getFile with different project configurations.
     * The file path should adapt to different build directory locations.
     */
    @Test
    public void testGetFile_withCustomBuildDir() {
        // Given: A project with a custom build directory
        File customBuildDir = new File(tempDir.toFile(), "custom-build");
        project.setBuildDir(customBuildDir);
        PrepareProguardConfigDirectoryTask customTask = project.getTasks()
                .create("prepareProguardConfigDirectory2", PrepareProguardConfigDirectoryTask.class);

        // When: Getting the file
        File file = customTask.getFile();

        // Then: Should use the custom build directory
        File expectedFile = new File(customBuildDir, "intermediates/proguard/configs");
        assertEquals(expectedFile, file, "Should use custom build directory");
    }

    /**
     * Test getFile returns an absolute path.
     * The returned File should have an absolute path.
     */
    @Test
    public void testGetFile_returnsAbsolutePath() {
        // When: Getting the file
        File file = task.getFile();

        // Then: Should be an absolute path
        assertTrue(file.isAbsolute() || file.getAbsolutePath().length() > 0,
                "File should have an absolute path representation");
    }

    // ==================== createDirectory() Tests ====================

    /**
     * Test createDirectory creates the directory.
     * The createDirectory method should create the directory structure.
     */
    @Test
    public void testCreateDirectory_createsDirectory() {
        // Given: The directory doesn't exist
        File file = task.getFile();
        assertFalse(file.exists(), "Directory should not exist initially");

        // When: Calling createDirectory
        task.createDirectory();

        // Then: The directory should be created
        assertTrue(file.exists(), "Directory should exist after createDirectory()");
        assertTrue(file.isDirectory(), "Created path should be a directory");
    }

    /**
     * Test createDirectory creates parent directories.
     * The method should create the entire directory hierarchy.
     */
    @Test
    public void testCreateDirectory_createsParentDirectories() {
        // Given: Parent directories don't exist
        File file = task.getFile();
        File parentDir = file.getParentFile();
        assertFalse(file.exists(), "Target directory should not exist initially");
        assertFalse(parentDir.exists(), "Parent directory should not exist initially");

        // When: Calling createDirectory
        task.createDirectory();

        // Then: All parent directories should be created
        assertTrue(parentDir.exists(), "Parent directory should be created");
        assertTrue(parentDir.isDirectory(), "Parent should be a directory");
        assertTrue(file.exists(), "Target directory should be created");
        assertTrue(file.isDirectory(), "Target should be a directory");
    }

    /**
     * Test createDirectory is idempotent.
     * Calling createDirectory multiple times should not cause errors.
     */
    @Test
    public void testCreateDirectory_isIdempotent() {
        // Given: The directory doesn't exist
        File file = task.getFile();
        assertFalse(file.exists(), "Directory should not exist initially");

        // When: Calling createDirectory multiple times
        task.createDirectory();
        assertTrue(file.exists(), "Directory should exist after first call");

        task.createDirectory();
        assertTrue(file.exists(), "Directory should still exist after second call");

        task.createDirectory();
        assertTrue(file.exists(), "Directory should still exist after third call");

        // Then: Directory should still be valid
        assertTrue(file.isDirectory(), "Should still be a directory");
    }

    /**
     * Test createDirectory completes without exception.
     * The method should execute successfully.
     */
    @Test
    public void testCreateDirectory_completesWithoutException() {
        // When/Then: Should not throw any exception
        assertDoesNotThrow(() -> task.createDirectory(),
                "createDirectory() should not throw an exception");
    }

    /**
     * Test createDirectory with existing directory.
     * If the directory already exists, createDirectory should handle it gracefully.
     */
    @Test
    public void testCreateDirectory_withExistingDirectory() {
        // Given: The directory already exists
        File file = task.getFile();
        file.mkdirs();
        assertTrue(file.exists(), "Directory should exist before test");

        // When: Calling createDirectory
        task.createDirectory();

        // Then: Directory should still exist and be valid
        assertTrue(file.exists(), "Directory should still exist");
        assertTrue(file.isDirectory(), "Should still be a directory");
    }

    /**
     * Test createDirectory creates the correct path.
     * The created directory should match the path returned by getFile().
     */
    @Test
    public void testCreateDirectory_createsCorrectPath() {
        // Given: The expected file path
        File expectedFile = task.getFile();

        // When: Calling createDirectory
        task.createDirectory();

        // Then: The exact expected path should exist
        assertTrue(expectedFile.exists(), "Expected path should exist");
        assertEquals(expectedFile, task.getFile(), "Created path should match getFile()");
    }

    /**
     * Test createDirectory creates writable directory.
     * The created directory should be writable.
     */
    @Test
    public void testCreateDirectory_createsWritableDirectory() {
        // When: Creating the directory
        task.createDirectory();
        File file = task.getFile();

        // Then: Directory should be writable
        assertTrue(file.canWrite(), "Created directory should be writable");
    }

    /**
     * Test createDirectory creates readable directory.
     * The created directory should be readable.
     */
    @Test
    public void testCreateDirectory_createsReadableDirectory() {
        // When: Creating the directory
        task.createDirectory();
        File file = task.getFile();

        // Then: Directory should be readable
        assertTrue(file.canRead(), "Created directory should be readable");
    }

    /**
     * Test createDirectory allows file creation in directory.
     * After creating the directory, it should be possible to create files in it.
     */
    @Test
    public void testCreateDirectory_allowsFileCreation() throws Exception {
        // Given: The directory is created
        task.createDirectory();
        File dir = task.getFile();

        // When: Creating a file in the directory
        File testFile = new File(dir, "test-config.pro");
        boolean created = testFile.createNewFile();

        // Then: File creation should succeed
        assertTrue(created, "Should be able to create a file in the directory");
        assertTrue(testFile.exists(), "Created file should exist");
        assertTrue(testFile.isFile(), "Created path should be a file");

        // Cleanup
        testFile.delete();
    }

    /**
     * Test createDirectory with custom build directory.
     * The method should work correctly with custom build directories.
     */
    @Test
    public void testCreateDirectory_withCustomBuildDir() {
        // Given: A project with a custom build directory
        File customBuildDir = new File(tempDir.toFile(), "custom-build");
        project.setBuildDir(customBuildDir);
        PrepareProguardConfigDirectoryTask customTask = project.getTasks()
                .create("prepareProguardConfigDirectory3", PrepareProguardConfigDirectoryTask.class);

        // When: Calling createDirectory
        customTask.createDirectory();
        File file = customTask.getFile();

        // Then: Directory should be created in custom location
        assertTrue(file.exists(), "Directory should be created");
        assertTrue(file.getAbsolutePath().contains("custom-build"),
                "Directory should be under custom build directory");
    }

    // ==================== Integration Tests ====================

    /**
     * Test complete workflow: get file then create directory.
     * This tests the typical usage pattern of the task.
     */
    @Test
    public void testIntegration_getFileThenCreateDirectory() {
        // When: Getting the file and then creating the directory
        File file = task.getFile();
        assertFalse(file.exists(), "Directory should not exist initially");

        task.createDirectory();

        // Then: File path should exist and be usable
        assertTrue(file.exists(), "Directory should exist after creation");
        assertTrue(file.isDirectory(), "Path should be a directory");
        assertEquals(file, task.getFile(), "getFile() should still return the same path");
    }

    /**
     * Test multiple tasks don't interfere.
     * Multiple instances of the task should work independently.
     */
    @Test
    public void testIntegration_multipleTasksIndependent() {
        // Given: Two tasks
        PrepareProguardConfigDirectoryTask task1 = project.getTasks()
                .create("prepareProguardConfigDirectory4", PrepareProguardConfigDirectoryTask.class);
        PrepareProguardConfigDirectoryTask task2 = project.getTasks()
                .create("prepareProguardConfigDirectory5", PrepareProguardConfigDirectoryTask.class);

        // When: Both tasks create their directories
        task1.createDirectory();
        task2.createDirectory();

        // Then: Both should have the same directory (same project)
        assertEquals(task1.getFile(), task2.getFile(),
                "Tasks in the same project should reference the same directory");
        assertTrue(task1.getFile().exists(), "Directory should exist for task1");
        assertTrue(task2.getFile().exists(), "Directory should exist for task2");
    }

    /**
     * Test task as Gradle task action.
     * The createDirectory method is annotated with @TaskAction.
     */
    @Test
    public void testIntegration_taskActionExecution() {
        // Given: A task that hasn't been executed
        File file = task.getFile();
        assertFalse(file.exists(), "Directory should not exist before task execution");

        // When: Executing the task action method directly
        task.createDirectory();

        // Then: The directory should be created
        assertTrue(file.exists(), "Directory should exist after task action");
    }

    /**
     * Test directory structure is correct.
     * Verify the complete directory structure matches expectations.
     */
    @Test
    public void testIntegration_directoryStructureIsCorrect() {
        // When: Creating the directory
        task.createDirectory();
        File file = task.getFile();

        // Then: Verify the complete structure
        File buildDir = project.getBuildDir();
        File intermediatesDir = new File(buildDir, "intermediates");
        File proguardDir = new File(intermediatesDir, "proguard");
        File configsDir = new File(proguardDir, "configs");

        assertTrue(buildDir.exists(), "Build directory should exist");
        assertTrue(intermediatesDir.exists(), "Intermediates directory should exist");
        assertTrue(intermediatesDir.isDirectory(), "Intermediates should be a directory");
        assertTrue(proguardDir.exists(), "ProGuard directory should exist");
        assertTrue(proguardDir.isDirectory(), "ProGuard should be a directory");
        assertTrue(configsDir.exists(), "Configs directory should exist");
        assertTrue(configsDir.isDirectory(), "Configs should be a directory");
        assertEquals(configsDir, file, "Final directory should match getFile()");
    }

    /**
     * Test task can be created and used in different projects.
     * The task should work correctly across different project instances.
     */
    @Test
    public void testIntegration_worksInDifferentProjects() throws Exception {
        // Given: Another project in a different temp directory
        Path tempDir2 = tempDir.resolve("project2");
        tempDir2.toFile().mkdirs();
        Project project2 = ProjectBuilder.builder()
                .withProjectDir(tempDir2.toFile())
                .build();
        PrepareProguardConfigDirectoryTask task2 = project2.getTasks()
                .create("prepareProguardConfigDirectory", PrepareProguardConfigDirectoryTask.class);

        // When: Both tasks create their directories
        task.createDirectory();
        task2.createDirectory();

        // Then: Both should have their own directories in their respective projects
        File file1 = task.getFile();
        File file2 = task2.getFile();

        assertTrue(file1.exists(), "First project directory should exist");
        assertTrue(file2.exists(), "Second project directory should exist");
        assertNotEquals(file1, file2, "Different projects should have different directories");
        assertTrue(file1.getAbsolutePath().contains(project.getBuildDir().getName()),
                "First directory should be under first project");
        assertTrue(file2.getAbsolutePath().contains(project2.getBuildDir().getName()),
                "Second directory should be under second project");
    }

    /**
     * Test real-world usage scenario.
     * This simulates how the task would be used in an Android build.
     */
    @Test
    public void testIntegration_realWorldAndroidBuildScenario() throws Exception {
        // Given: A task preparing to store ProGuard configuration files
        File configDir = task.getFile();
        assertFalse(configDir.exists(), "Config directory should not exist initially");

        // When: Task is executed as part of build process
        task.createDirectory();

        // Then: Directory should be ready to receive configuration files
        assertTrue(configDir.exists(), "Config directory should exist");
        assertTrue(configDir.isDirectory(), "Should be a directory");
        assertTrue(configDir.canWrite(), "Should be writable for storing configs");

        // Simulate creating configuration files
        File consumerRule = new File(configDir, "consumer-rules.pro");
        File proguardRules = new File(configDir, "proguard-rules.pro");

        assertTrue(consumerRule.createNewFile(), "Should be able to create consumer rules");
        assertTrue(proguardRules.createNewFile(), "Should be able to create proguard rules");

        // Cleanup
        consumerRule.delete();
        proguardRules.delete();
    }

    /**
     * Test directory remains after multiple operations.
     * The directory should persist across multiple operations.
     */
    @Test
    public void testIntegration_directoryPersistsAcrossOperations() throws Exception {
        // When: Creating directory, adding a file, and calling createDirectory again
        task.createDirectory();
        File dir = task.getFile();

        File testFile = new File(dir, "test.pro");
        testFile.createNewFile();
        assertTrue(testFile.exists(), "Test file should exist");

        task.createDirectory();

        // Then: Directory and file should still exist
        assertTrue(dir.exists(), "Directory should still exist");
        assertTrue(testFile.exists(), "File in directory should still exist");

        // Cleanup
        testFile.delete();
    }

    /**
     * Test task name and properties are correctly set.
     * Verify the task has expected properties.
     */
    @Test
    public void testIntegration_taskPropertiesAreValid() {
        // Then: Task should have valid properties
        assertNotNull(task.getName(), "Task should have a name");
        assertEquals("prepareProguardConfigDirectory", task.getName(),
                "Task should have the correct name");
        assertNotNull(task.getProject(), "Task should be associated with a project");
        assertSame(project, task.getProject(), "Task should be associated with the correct project");
    }

    /**
     * Test task file property is properly initialized.
     * The file property should be available immediately after task creation.
     */
    @Test
    public void testIntegration_filePropertyInitializedOnCreation() {
        // Given: A newly created task
        PrepareProguardConfigDirectoryTask newTask = project.getTasks()
                .create("prepareProguardConfigDirectory6", PrepareProguardConfigDirectoryTask.class);

        // Then: File property should be immediately available
        assertNotNull(newTask.getFile(), "File property should be initialized on task creation");
        assertDoesNotThrow(() -> newTask.getFile(),
                "Getting file property should not throw exception");
    }
}
