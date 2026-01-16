package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setClassobfuscationdictionary method.
 * Tests that the method properly sets the classObfuscationDictionary field in the configuration
 * using the resolvedURL helper which converts File to URL.
 */
public class ProGuardTaskClaude_setClassobfuscationdictionaryTest {

    /**
     * Test that setClassobfuscationdictionary with a regular file sets the classObfuscationDictionary field.
     */
    @Test
    public void testSetClassobfuscationdictionaryRegularFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("class-dict.txt");
        task.setClassobfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.classObfuscationDictionary,
            "classObfuscationDictionary should be set");
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dict.txt"),
            "classObfuscationDictionary URL should contain class-dict.txt");
    }

    /**
     * Test that setClassobfuscationdictionary with absolute path file works correctly.
     */
    @Test
    public void testSetClassobfuscationdictionaryAbsoluteFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("/tmp/class-dict.txt");
        task.setClassobfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.classObfuscationDictionary,
            "classObfuscationDictionary should be set");
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dict.txt"),
            "classObfuscationDictionary URL should contain class-dict.txt");
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("/tmp/"),
            "classObfuscationDictionary URL should contain /tmp/");
    }

    /**
     * Test that setClassobfuscationdictionary default value is null.
     */
    @Test
    public void testSetClassobfuscationdictionaryDefaultIsNull() {
        ProGuardTask task = new ProGuardTask();

        assertNull(task.configuration.classObfuscationDictionary,
            "classObfuscationDictionary should default to null");
    }

    /**
     * Test that setClassobfuscationdictionary can be called multiple times.
     */
    @Test
    public void testSetClassobfuscationdictionaryMultipleCalls() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File file1 = new File("class-dict1.txt");
        task.setClassobfuscationdictionary(file1);
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dict1.txt"));

        File file2 = new File("class-dict2.txt");
        task.setClassobfuscationdictionary(file2);
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dict2.txt"),
            "Second call should overwrite first value");
    }

    /**
     * Test that setClassobfuscationdictionary on different task instances are independent.
     */
    @Test
    public void testSetClassobfuscationdictionaryIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        Project project = new Project();
        project.init();
        task1.setProject(project);
        task2.setProject(project);

        File file1 = new File("class-dict1.txt");
        File file2 = new File("class-dict2.txt");

        task1.setClassobfuscationdictionary(file1);
        task2.setClassobfuscationdictionary(file2);

        assertTrue(task1.configuration.classObfuscationDictionary.toString().contains("class-dict1.txt"),
            "task1 should have its own classObfuscationDictionary value");
        assertTrue(task2.configuration.classObfuscationDictionary.toString().contains("class-dict2.txt"),
            "task2 should have its own classObfuscationDictionary value");
    }

    /**
     * Test that setting classObfuscationDictionary doesn't affect other configuration fields.
     */
    @Test
    public void testSetClassobfuscationdictionaryDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;
        URL initialObfuscationDictionary = task.configuration.obfuscationDictionary;

        File dictionaryFile = new File("class-dict.txt");
        task.setClassobfuscationdictionary(dictionaryFile);

        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
            "obfuscate field should not be affected");
        assertEquals(initialObfuscationDictionary, task.configuration.obfuscationDictionary,
            "obfuscationDictionary field should not be affected");
    }

    /**
     * Test that the classObfuscationDictionary value persists across other setter calls.
     */
    @Test
    public void testSetClassobfuscationdictionaryPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("class-dict.txt");
        task.setClassobfuscationdictionary(dictionaryFile);

        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dict.txt"),
            "classObfuscationDictionary should remain unchanged after other setters are called");
    }

    /**
     * Test that setClassobfuscationdictionary with file that has path separator.
     */
    @Test
    public void testSetClassobfuscationdictionaryWithPath() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("config/class-dict.txt");
        task.setClassobfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.classObfuscationDictionary,
            "classObfuscationDictionary should be set");
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dict.txt"),
            "classObfuscationDictionary URL should contain class-dict.txt");
    }

    /**
     * Test that setClassobfuscationdictionary creates a valid URL.
     */
    @Test
    public void testSetClassobfuscationdictionaryCreatesValidURL() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("class-dict.txt");
        task.setClassobfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.classObfuscationDictionary,
            "classObfuscationDictionary should be set");
        assertInstanceOf(URL.class, task.configuration.classObfuscationDictionary,
            "classObfuscationDictionary should be a URL instance");
    }

    /**
     * Test that setClassobfuscationdictionary with absolute path creates correct URL.
     */
    @Test
    public void testSetClassobfuscationdictionaryAbsolutePathURL() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File absoluteFile = new File("/absolute/path/to/class-dict.txt");
        task.setClassobfuscationdictionary(absoluteFile);

        assertNotNull(task.configuration.classObfuscationDictionary,
            "classObfuscationDictionary should be set");
        String urlString = task.configuration.classObfuscationDictionary.toString();
        assertTrue(urlString.contains("class-dict.txt"),
            "URL should contain filename");
        assertTrue(urlString.startsWith("file:"),
            "URL should start with file: protocol");
    }

    /**
     * Test that setClassobfuscationdictionary works immediately after construction.
     */
    @Test
    public void testSetClassobfuscationdictionaryImmediatelyAfterConstruction() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        assertDoesNotThrow(() -> task.setClassobfuscationdictionary(new File("dict.txt")),
            "Should be able to call setClassobfuscationdictionary immediately after construction");

        assertNotNull(task.configuration.classObfuscationDictionary,
            "classObfuscationDictionary should be set");
    }

    /**
     * Test that setClassobfuscationdictionary with different files results in different URLs.
     */
    @Test
    public void testSetClassobfuscationdictionaryDifferentFiles() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        Project project = new Project();
        project.init();
        task1.setProject(project);
        task2.setProject(project);

        task1.setClassobfuscationdictionary(new File("dict1.txt"));
        task2.setClassobfuscationdictionary(new File("dict2.txt"));

        assertNotEquals(task1.configuration.classObfuscationDictionary.toString(),
            task2.configuration.classObfuscationDictionary.toString(),
            "Different files should result in different classObfuscationDictionary URLs");
    }

    /**
     * Test that setClassobfuscationdictionary can be used in combination with other setters.
     */
    @Test
    public void testSetClassobfuscationdictionaryWithOtherSetters() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        task.setObfuscate(true);
        task.setClassobfuscationdictionary(new File("class-dict.txt"));
        task.setShrink(false);

        assertNotNull(task.configuration.classObfuscationDictionary,
            "classObfuscationDictionary should be set");
        assertTrue(task.configuration.obfuscate,
            "obfuscate should be true");
        assertFalse(task.configuration.shrink,
            "shrink should be false");
    }

    /**
     * Test that setClassobfuscationdictionary handles files with various extensions.
     */
    @Test
    public void testSetClassobfuscationdictionaryVariousExtensions() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        String[] filenames = {"classes.txt", "classes.dic", "classes.cfg", "classes"};

        for (String filename : filenames) {
            task.setClassobfuscationdictionary(new File(filename));
            assertNotNull(task.configuration.classObfuscationDictionary,
                "classObfuscationDictionary should be set for " + filename);
            assertTrue(task.configuration.classObfuscationDictionary.toString().contains(filename),
                "classObfuscationDictionary should contain " + filename);
        }
    }

    /**
     * Test that setClassobfuscationdictionary with the same file twice maintains the value.
     */
    @Test
    public void testSetClassobfuscationdictionarySameFileTwice() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("class-dict.txt");
        task.setClassobfuscationdictionary(dictionaryFile);
        String firstURL = task.configuration.classObfuscationDictionary.toString();

        task.setClassobfuscationdictionary(dictionaryFile);
        String secondURL = task.configuration.classObfuscationDictionary.toString();

        assertEquals(firstURL, secondURL,
            "Setting the same file twice should result in the same URL");
    }

    /**
     * Test that setClassobfuscationdictionary works correctly with Ant project context.
     */
    @Test
    public void testSetClassobfuscationdictionaryWithAntProjectContext() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("class-dict.txt");
        task.setClassobfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.classObfuscationDictionary,
            "classObfuscationDictionary should be set with Ant project context");
    }

    /**
     * Test that setClassobfuscationdictionary URL has file protocol.
     */
    @Test
    public void testSetClassobfuscationdictionaryURLProtocol() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("class-dict.txt");
        task.setClassobfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.classObfuscationDictionary,
            "classObfuscationDictionary should be set");
        assertEquals("file", task.configuration.classObfuscationDictionary.getProtocol(),
            "URL should use file protocol");
    }

    /**
     * Test that setClassobfuscationdictionary with relative path creates URL.
     */
    @Test
    public void testSetClassobfuscationdictionaryRelativePathCreatesURL() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File relativeFile = new File("subdir/class-dict.txt");
        task.setClassobfuscationdictionary(relativeFile);

        assertNotNull(task.configuration.classObfuscationDictionary,
            "classObfuscationDictionary should be set");
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dict.txt"),
            "URL should contain the filename");
    }

    /**
     * Test that setClassobfuscationdictionary resolves relative paths correctly.
     */
    @Test
    public void testSetClassobfuscationdictionaryResolvesRelativePath() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File relativeFile = new File("class-dict.txt");
        task.setClassobfuscationdictionary(relativeFile);

        assertNotNull(task.configuration.classObfuscationDictionary,
            "classObfuscationDictionary should be set");
        // The URL should be resolved relative to the project base
        String urlString = task.configuration.classObfuscationDictionary.toString();
        assertTrue(urlString.startsWith("file:"),
            "URL should have file protocol");
        assertTrue(urlString.contains("class-dict.txt"),
            "URL should contain the filename");
    }

    /**
     * Test that setClassobfuscationdictionary is independent from setObfuscationdictionary.
     */
    @Test
    public void testSetClassobfuscationdictionaryIndependentFromObfuscationdictionary() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File classDict = new File("class-dict.txt");
        File memberDict = new File("member-dict.txt");

        task.setClassobfuscationdictionary(classDict);
        task.setObfuscationdictionary(memberDict);

        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dict.txt"),
            "classObfuscationDictionary should be class-dict.txt");
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("member-dict.txt"),
            "obfuscationDictionary should be member-dict.txt");
    }
}
