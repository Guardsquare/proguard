package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassPathElement.setName method.
 * The setName method is deprecated and delegates to setLocation.
 */
public class ClassPathElementClaude_setNameTest {

    @TempDir
    Path tempDir;

    /**
     * Test setName with a valid file.
     * Verifies that the file location is added to the path.
     */
    @Test
    public void testSetNameWithValidFile() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testFile = tempDir.resolve("test.jar").toFile();
        assertTrue(testFile.createNewFile(), "Test file should be created");

        element.setName(testFile);

        String[] pathElements = element.list();
        assertNotNull(pathElements, "Path elements should not be null");
        assertEquals(1, pathElements.length, "Should have one path element");
        assertTrue(pathElements[0].endsWith("test.jar"), "Path should contain the test file");
    }

    /**
     * Test setName with a valid directory.
     * Verifies that the directory location is added to the path.
     */
    @Test
    public void testSetNameWithDirectory() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testDir = tempDir.resolve("testDir").toFile();
        assertTrue(testDir.mkdir(), "Test directory should be created");

        element.setName(testDir);

        String[] pathElements = element.list();
        assertNotNull(pathElements, "Path elements should not be null");
        assertEquals(1, pathElements.length, "Should have one path element");
        assertTrue(pathElements[0].endsWith("testDir"), "Path should contain the test directory");
    }

    /**
     * Test setName with a non-existent file.
     * The method should still accept the path even if the file doesn't exist.
     */
    @Test
    public void testSetNameWithNonExistentFile() {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File nonExistentFile = new File(tempDir.toFile(), "nonexistent.jar");
        assertFalse(nonExistentFile.exists(), "File should not exist");

        element.setName(nonExistentFile);

        String[] pathElements = element.list();
        assertNotNull(pathElements, "Path elements should not be null");
        assertEquals(1, pathElements.length, "Should have one path element");
        assertTrue(pathElements[0].contains("nonexistent.jar"), "Path should contain the file name");
    }

    /**
     * Test setName multiple times on the same element.
     * Each call should add to the path.
     */
    @Test
    public void testSetNameMultipleTimes() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File file1 = tempDir.resolve("file1.jar").toFile();
        File file2 = tempDir.resolve("file2.jar").toFile();
        assertTrue(file1.createNewFile(), "First file should be created");
        assertTrue(file2.createNewFile(), "Second file should be created");

        element.setName(file1);
        element.setName(file2);

        String[] pathElements = element.list();
        assertNotNull(pathElements, "Path elements should not be null");
        assertEquals(2, pathElements.length, "Should have two path elements");
    }

    /**
     * Test setName with an absolute path.
     */
    @Test
    public void testSetNameWithAbsolutePath() throws IOException {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        File absoluteFile = tempDir.resolve("absolute.jar").toFile();
        assertTrue(absoluteFile.createNewFile(), "Absolute file should be created");
        assertTrue(absoluteFile.isAbsolute(), "File should have absolute path");

        element.setName(absoluteFile);

        String[] pathElements = element.list();
        assertNotNull(pathElements, "Path elements should not be null");
        assertEquals(1, pathElements.length, "Should have one path element");
        assertEquals(absoluteFile.getAbsolutePath(), pathElements[0], "Path should be absolute");
    }

    /**
     * Test setName with a relative path.
     */
    @Test
    public void testSetNameWithRelativePath() {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File relativeFile = new File("relative.jar");
        assertFalse(relativeFile.isAbsolute(), "File should be relative");

        element.setName(relativeFile);

        String[] pathElements = element.list();
        assertNotNull(pathElements, "Path elements should not be null");
        assertEquals(1, pathElements.length, "Should have one path element");
        assertTrue(pathElements[0].endsWith("relative.jar"), "Path should contain the relative file");
    }

    /**
     * Test setName with null project.
     * The method should handle this gracefully.
     */
    @Test
    public void testSetNameWithNullProject() throws IOException {
        ClassPathElement element = new ClassPathElement(null);

        File testFile = tempDir.resolve("test.jar").toFile();
        assertTrue(testFile.createNewFile(), "Test file should be created");

        element.setName(testFile);

        String[] pathElements = element.list();
        assertNotNull(pathElements, "Path elements should not be null");
        assertEquals(1, pathElements.length, "Should have one path element");
    }

    /**
     * Test setName with a file containing special characters in the name.
     */
    @Test
    public void testSetNameWithSpecialCharactersInFileName() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File specialFile = tempDir.resolve("test-file_v1.0.jar").toFile();
        assertTrue(specialFile.createNewFile(), "File with special chars should be created");

        element.setName(specialFile);

        String[] pathElements = element.list();
        assertNotNull(pathElements, "Path elements should not be null");
        assertEquals(1, pathElements.length, "Should have one path element");
        assertTrue(pathElements[0].contains("test-file_v1.0.jar"), "Path should contain the file with special chars");
    }

    /**
     * Test that setName properly integrates with the Path functionality.
     * After calling setName, the element should be usable in classpath operations.
     */
    @Test
    public void testSetNameIntegrationWithPath() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testFile = tempDir.resolve("integration.jar").toFile();
        assertTrue(testFile.createNewFile(), "Integration test file should be created");

        element.setName(testFile);

        // Verify that the element is not empty
        assertFalse(element.toString().isEmpty(), "Path toString should not be empty");

        // Verify that list() returns the correct path
        String[] pathElements = element.list();
        assertEquals(1, pathElements.length, "Should have exactly one element");
        assertTrue(new File(pathElements[0]).exists(), "The file in the path should exist");
    }
}
