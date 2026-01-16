package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setObfuscationdictionary method.
 * Tests that the method properly sets the obfuscationDictionary field in the configuration
 * using the resolvedURL helper which converts File to URL.
 */
public class ProGuardTaskClaude_setObfuscationdictionaryTest {

    /**
     * Test that setObfuscationdictionary with a regular file sets the obfuscationDictionary field.
     */
    @Test
    public void testSetObfuscationdictionaryRegularFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("dictionary.txt");
        task.setObfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.obfuscationDictionary,
            "obfuscationDictionary should be set");
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("dictionary.txt"),
            "obfuscationDictionary URL should contain dictionary.txt");
    }

    /**
     * Test that setObfuscationdictionary with absolute path file works correctly.
     */
    @Test
    public void testSetObfuscationdictionaryAbsoluteFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("/tmp/dictionary.txt");
        task.setObfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.obfuscationDictionary,
            "obfuscationDictionary should be set");
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("dictionary.txt"),
            "obfuscationDictionary URL should contain dictionary.txt");
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("/tmp/"),
            "obfuscationDictionary URL should contain /tmp/");
    }

    /**
     * Test that setObfuscationdictionary default value is null.
     */
    @Test
    public void testSetObfuscationdictionaryDefaultIsNull() {
        ProGuardTask task = new ProGuardTask();

        assertNull(task.configuration.obfuscationDictionary,
            "obfuscationDictionary should default to null");
    }

    /**
     * Test that setObfuscationdictionary can be called multiple times.
     */
    @Test
    public void testSetObfuscationdictionaryMultipleCalls() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File file1 = new File("dict1.txt");
        task.setObfuscationdictionary(file1);
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("dict1.txt"));

        File file2 = new File("dict2.txt");
        task.setObfuscationdictionary(file2);
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("dict2.txt"),
            "Second call should overwrite first value");
    }

    /**
     * Test that setObfuscationdictionary on different task instances are independent.
     */
    @Test
    public void testSetObfuscationdictionaryIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        Project project = new Project();
        project.init();
        task1.setProject(project);
        task2.setProject(project);

        File file1 = new File("dict1.txt");
        File file2 = new File("dict2.txt");

        task1.setObfuscationdictionary(file1);
        task2.setObfuscationdictionary(file2);

        assertTrue(task1.configuration.obfuscationDictionary.toString().contains("dict1.txt"),
            "task1 should have its own obfuscationDictionary value");
        assertTrue(task2.configuration.obfuscationDictionary.toString().contains("dict2.txt"),
            "task2 should have its own obfuscationDictionary value");
    }

    /**
     * Test that setting obfuscationDictionary doesn't affect other configuration fields.
     */
    @Test
    public void testSetObfuscationdictionaryDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;

        File dictionaryFile = new File("dictionary.txt");
        task.setObfuscationdictionary(dictionaryFile);

        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
            "obfuscate field should not be affected");
    }

    /**
     * Test that the obfuscationDictionary value persists across other setter calls.
     */
    @Test
    public void testSetObfuscationdictionaryPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("dictionary.txt");
        task.setObfuscationdictionary(dictionaryFile);

        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        assertTrue(task.configuration.obfuscationDictionary.toString().contains("dictionary.txt"),
            "obfuscationDictionary should remain unchanged after other setters are called");
    }

    /**
     * Test that setObfuscationdictionary with file that has path separator.
     */
    @Test
    public void testSetObfuscationdictionaryWithPath() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("config/dictionary.txt");
        task.setObfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.obfuscationDictionary,
            "obfuscationDictionary should be set");
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("dictionary.txt"),
            "obfuscationDictionary URL should contain dictionary.txt");
    }

    /**
     * Test that setObfuscationdictionary creates a valid URL.
     */
    @Test
    public void testSetObfuscationdictionaryCreatesValidURL() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("dictionary.txt");
        task.setObfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.obfuscationDictionary,
            "obfuscationDictionary should be set");
        assertInstanceOf(URL.class, task.configuration.obfuscationDictionary,
            "obfuscationDictionary should be a URL instance");
    }

    /**
     * Test that setObfuscationdictionary with absolute path creates correct URL.
     */
    @Test
    public void testSetObfuscationdictionaryAbsolutePathURL() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File absoluteFile = new File("/absolute/path/to/dictionary.txt");
        task.setObfuscationdictionary(absoluteFile);

        assertNotNull(task.configuration.obfuscationDictionary,
            "obfuscationDictionary should be set");
        String urlString = task.configuration.obfuscationDictionary.toString();
        assertTrue(urlString.contains("dictionary.txt"),
            "URL should contain filename");
        assertTrue(urlString.startsWith("file:"),
            "URL should start with file: protocol");
    }

    /**
     * Test that setObfuscationdictionary works immediately after construction.
     */
    @Test
    public void testSetObfuscationdictionaryImmediatelyAfterConstruction() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        assertDoesNotThrow(() -> task.setObfuscationdictionary(new File("dict.txt")),
            "Should be able to call setObfuscationdictionary immediately after construction");

        assertNotNull(task.configuration.obfuscationDictionary,
            "obfuscationDictionary should be set");
    }

    /**
     * Test that setObfuscationdictionary with different files results in different URLs.
     */
    @Test
    public void testSetObfuscationdictionaryDifferentFiles() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        Project project = new Project();
        project.init();
        task1.setProject(project);
        task2.setProject(project);

        task1.setObfuscationdictionary(new File("dict1.txt"));
        task2.setObfuscationdictionary(new File("dict2.txt"));

        assertNotEquals(task1.configuration.obfuscationDictionary.toString(),
            task2.configuration.obfuscationDictionary.toString(),
            "Different files should result in different obfuscationDictionary URLs");
    }

    /**
     * Test that setObfuscationdictionary can be used in combination with other setters.
     */
    @Test
    public void testSetObfuscationdictionaryWithOtherSetters() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        task.setObfuscate(true);
        task.setObfuscationdictionary(new File("dict.txt"));
        task.setShrink(false);

        assertNotNull(task.configuration.obfuscationDictionary,
            "obfuscationDictionary should be set");
        assertTrue(task.configuration.obfuscate,
            "obfuscate should be true");
        assertFalse(task.configuration.shrink,
            "shrink should be false");
    }

    /**
     * Test that setObfuscationdictionary handles files with various extensions.
     */
    @Test
    public void testSetObfuscationdictionaryVariousExtensions() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        String[] filenames = {"dictionary.txt", "dictionary.dic", "dictionary.cfg", "dictionary"};

        for (String filename : filenames) {
            task.setObfuscationdictionary(new File(filename));
            assertNotNull(task.configuration.obfuscationDictionary,
                "obfuscationDictionary should be set for " + filename);
            assertTrue(task.configuration.obfuscationDictionary.toString().contains(filename),
                "obfuscationDictionary should contain " + filename);
        }
    }

    /**
     * Test that setObfuscationdictionary with the same file twice maintains the value.
     */
    @Test
    public void testSetObfuscationdictionarySameFileTwice() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("dictionary.txt");
        task.setObfuscationdictionary(dictionaryFile);
        String firstURL = task.configuration.obfuscationDictionary.toString();

        task.setObfuscationdictionary(dictionaryFile);
        String secondURL = task.configuration.obfuscationDictionary.toString();

        assertEquals(firstURL, secondURL,
            "Setting the same file twice should result in the same URL");
    }

    /**
     * Test that setObfuscationdictionary works correctly with Ant project context.
     */
    @Test
    public void testSetObfuscationdictionaryWithAntProjectContext() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("dictionary.txt");
        task.setObfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.obfuscationDictionary,
            "obfuscationDictionary should be set with Ant project context");
    }

    /**
     * Test that setObfuscationdictionary URL has file protocol.
     */
    @Test
    public void testSetObfuscationdictionaryURLProtocol() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("dictionary.txt");
        task.setObfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.obfuscationDictionary,
            "obfuscationDictionary should be set");
        assertEquals("file", task.configuration.obfuscationDictionary.getProtocol(),
            "URL should use file protocol");
    }

    /**
     * Test that setObfuscationdictionary with relative path creates URL.
     */
    @Test
    public void testSetObfuscationdictionaryRelativePathCreatesURL() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File relativeFile = new File("subdir/dictionary.txt");
        task.setObfuscationdictionary(relativeFile);

        assertNotNull(task.configuration.obfuscationDictionary,
            "obfuscationDictionary should be set");
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("dictionary.txt"),
            "URL should contain the filename");
    }

    /**
     * Test that setObfuscationdictionary resolves relative paths correctly.
     */
    @Test
    public void testSetObfuscationdictionaryResolvesRelativePath() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File relativeFile = new File("dictionary.txt");
        task.setObfuscationdictionary(relativeFile);

        assertNotNull(task.configuration.obfuscationDictionary,
            "obfuscationDictionary should be set");
        // The URL should be resolved relative to the project base
        String urlString = task.configuration.obfuscationDictionary.toString();
        assertTrue(urlString.startsWith("file:"),
            "URL should have file protocol");
        assertTrue(urlString.contains("dictionary.txt"),
            "URL should contain the filename");
    }
}
