package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setPackageobfuscationdictionary method.
 * Tests that the method properly sets the packageObfuscationDictionary field in the configuration
 * using the resolvedURL helper which converts File to URL.
 */
public class ProGuardTaskClaude_setPackageobfuscationdictionaryTest {

    /**
     * Test that setPackageobfuscationdictionary with a regular file sets the packageObfuscationDictionary field.
     */
    @Test
    public void testSetPackageobfuscationdictionaryRegularFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("package-dict.txt");
        task.setPackageobfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.packageObfuscationDictionary,
            "packageObfuscationDictionary should be set");
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dict.txt"),
            "packageObfuscationDictionary URL should contain package-dict.txt");
    }

    /**
     * Test that setPackageobfuscationdictionary with absolute path file works correctly.
     */
    @Test
    public void testSetPackageobfuscationdictionaryAbsoluteFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("/tmp/package-dict.txt");
        task.setPackageobfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.packageObfuscationDictionary,
            "packageObfuscationDictionary should be set");
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dict.txt"),
            "packageObfuscationDictionary URL should contain package-dict.txt");
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("/tmp/"),
            "packageObfuscationDictionary URL should contain /tmp/");
    }

    /**
     * Test that setPackageobfuscationdictionary default value is null.
     */
    @Test
    public void testSetPackageobfuscationdictionaryDefaultIsNull() {
        ProGuardTask task = new ProGuardTask();

        assertNull(task.configuration.packageObfuscationDictionary,
            "packageObfuscationDictionary should default to null");
    }

    /**
     * Test that setPackageobfuscationdictionary can be called multiple times.
     */
    @Test
    public void testSetPackageobfuscationdictionaryMultipleCalls() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File file1 = new File("package-dict1.txt");
        task.setPackageobfuscationdictionary(file1);
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dict1.txt"));

        File file2 = new File("package-dict2.txt");
        task.setPackageobfuscationdictionary(file2);
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dict2.txt"),
            "Second call should overwrite first value");
    }

    /**
     * Test that setPackageobfuscationdictionary on different task instances are independent.
     */
    @Test
    public void testSetPackageobfuscationdictionaryIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        Project project = new Project();
        project.init();
        task1.setProject(project);
        task2.setProject(project);

        File file1 = new File("package-dict1.txt");
        File file2 = new File("package-dict2.txt");

        task1.setPackageobfuscationdictionary(file1);
        task2.setPackageobfuscationdictionary(file2);

        assertTrue(task1.configuration.packageObfuscationDictionary.toString().contains("package-dict1.txt"),
            "task1 should have its own packageObfuscationDictionary value");
        assertTrue(task2.configuration.packageObfuscationDictionary.toString().contains("package-dict2.txt"),
            "task2 should have its own packageObfuscationDictionary value");
    }

    /**
     * Test that setting packageObfuscationDictionary doesn't affect other configuration fields.
     */
    @Test
    public void testSetPackageobfuscationdictionaryDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;
        URL initialObfuscationDictionary = task.configuration.obfuscationDictionary;
        URL initialClassObfuscationDictionary = task.configuration.classObfuscationDictionary;

        File dictionaryFile = new File("package-dict.txt");
        task.setPackageobfuscationdictionary(dictionaryFile);

        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
            "obfuscate field should not be affected");
        assertEquals(initialObfuscationDictionary, task.configuration.obfuscationDictionary,
            "obfuscationDictionary field should not be affected");
        assertEquals(initialClassObfuscationDictionary, task.configuration.classObfuscationDictionary,
            "classObfuscationDictionary field should not be affected");
    }

    /**
     * Test that the packageObfuscationDictionary value persists across other setter calls.
     */
    @Test
    public void testSetPackageobfuscationdictionaryPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("package-dict.txt");
        task.setPackageobfuscationdictionary(dictionaryFile);

        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dict.txt"),
            "packageObfuscationDictionary should remain unchanged after other setters are called");
    }

    /**
     * Test that setPackageobfuscationdictionary with file that has path separator.
     */
    @Test
    public void testSetPackageobfuscationdictionaryWithPath() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("config/package-dict.txt");
        task.setPackageobfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.packageObfuscationDictionary,
            "packageObfuscationDictionary should be set");
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dict.txt"),
            "packageObfuscationDictionary URL should contain package-dict.txt");
    }

    /**
     * Test that setPackageobfuscationdictionary creates a valid URL.
     */
    @Test
    public void testSetPackageobfuscationdictionaryCreatesValidURL() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("package-dict.txt");
        task.setPackageobfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.packageObfuscationDictionary,
            "packageObfuscationDictionary should be set");
        assertInstanceOf(URL.class, task.configuration.packageObfuscationDictionary,
            "packageObfuscationDictionary should be a URL instance");
    }

    /**
     * Test that setPackageobfuscationdictionary with absolute path creates correct URL.
     */
    @Test
    public void testSetPackageobfuscationdictionaryAbsolutePathURL() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File absoluteFile = new File("/absolute/path/to/package-dict.txt");
        task.setPackageobfuscationdictionary(absoluteFile);

        assertNotNull(task.configuration.packageObfuscationDictionary,
            "packageObfuscationDictionary should be set");
        String urlString = task.configuration.packageObfuscationDictionary.toString();
        assertTrue(urlString.contains("package-dict.txt"),
            "URL should contain filename");
        assertTrue(urlString.startsWith("file:"),
            "URL should start with file: protocol");
    }

    /**
     * Test that setPackageobfuscationdictionary works immediately after construction.
     */
    @Test
    public void testSetPackageobfuscationdictionaryImmediatelyAfterConstruction() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        assertDoesNotThrow(() -> task.setPackageobfuscationdictionary(new File("dict.txt")),
            "Should be able to call setPackageobfuscationdictionary immediately after construction");

        assertNotNull(task.configuration.packageObfuscationDictionary,
            "packageObfuscationDictionary should be set");
    }

    /**
     * Test that setPackageobfuscationdictionary with different files results in different URLs.
     */
    @Test
    public void testSetPackageobfuscationdictionaryDifferentFiles() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        Project project = new Project();
        project.init();
        task1.setProject(project);
        task2.setProject(project);

        task1.setPackageobfuscationdictionary(new File("dict1.txt"));
        task2.setPackageobfuscationdictionary(new File("dict2.txt"));

        assertNotEquals(task1.configuration.packageObfuscationDictionary.toString(),
            task2.configuration.packageObfuscationDictionary.toString(),
            "Different files should result in different packageObfuscationDictionary URLs");
    }

    /**
     * Test that setPackageobfuscationdictionary can be used in combination with other setters.
     */
    @Test
    public void testSetPackageobfuscationdictionaryWithOtherSetters() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        task.setObfuscate(true);
        task.setPackageobfuscationdictionary(new File("package-dict.txt"));
        task.setShrink(false);

        assertNotNull(task.configuration.packageObfuscationDictionary,
            "packageObfuscationDictionary should be set");
        assertTrue(task.configuration.obfuscate,
            "obfuscate should be true");
        assertFalse(task.configuration.shrink,
            "shrink should be false");
    }

    /**
     * Test that setPackageobfuscationdictionary handles files with various extensions.
     */
    @Test
    public void testSetPackageobfuscationdictionaryVariousExtensions() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        String[] filenames = {"packages.txt", "packages.dic", "packages.cfg", "packages"};

        for (String filename : filenames) {
            task.setPackageobfuscationdictionary(new File(filename));
            assertNotNull(task.configuration.packageObfuscationDictionary,
                "packageObfuscationDictionary should be set for " + filename);
            assertTrue(task.configuration.packageObfuscationDictionary.toString().contains(filename),
                "packageObfuscationDictionary should contain " + filename);
        }
    }

    /**
     * Test that setPackageobfuscationdictionary with the same file twice maintains the value.
     */
    @Test
    public void testSetPackageobfuscationdictionarySameFileTwice() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("package-dict.txt");
        task.setPackageobfuscationdictionary(dictionaryFile);
        String firstURL = task.configuration.packageObfuscationDictionary.toString();

        task.setPackageobfuscationdictionary(dictionaryFile);
        String secondURL = task.configuration.packageObfuscationDictionary.toString();

        assertEquals(firstURL, secondURL,
            "Setting the same file twice should result in the same URL");
    }

    /**
     * Test that setPackageobfuscationdictionary works correctly with Ant project context.
     */
    @Test
    public void testSetPackageobfuscationdictionaryWithAntProjectContext() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("package-dict.txt");
        task.setPackageobfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.packageObfuscationDictionary,
            "packageObfuscationDictionary should be set with Ant project context");
    }

    /**
     * Test that setPackageobfuscationdictionary URL has file protocol.
     */
    @Test
    public void testSetPackageobfuscationdictionaryURLProtocol() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dictionaryFile = new File("package-dict.txt");
        task.setPackageobfuscationdictionary(dictionaryFile);

        assertNotNull(task.configuration.packageObfuscationDictionary,
            "packageObfuscationDictionary should be set");
        assertEquals("file", task.configuration.packageObfuscationDictionary.getProtocol(),
            "URL should use file protocol");
    }

    /**
     * Test that setPackageobfuscationdictionary with relative path creates URL.
     */
    @Test
    public void testSetPackageobfuscationdictionaryRelativePathCreatesURL() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File relativeFile = new File("subdir/package-dict.txt");
        task.setPackageobfuscationdictionary(relativeFile);

        assertNotNull(task.configuration.packageObfuscationDictionary,
            "packageObfuscationDictionary should be set");
        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dict.txt"),
            "URL should contain the filename");
    }

    /**
     * Test that setPackageobfuscationdictionary resolves relative paths correctly.
     */
    @Test
    public void testSetPackageobfuscationdictionaryResolvesRelativePath() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File relativeFile = new File("package-dict.txt");
        task.setPackageobfuscationdictionary(relativeFile);

        assertNotNull(task.configuration.packageObfuscationDictionary,
            "packageObfuscationDictionary should be set");
        // The URL should be resolved relative to the project base
        String urlString = task.configuration.packageObfuscationDictionary.toString();
        assertTrue(urlString.startsWith("file:"),
            "URL should have file protocol");
        assertTrue(urlString.contains("package-dict.txt"),
            "URL should contain the filename");
    }

    /**
     * Test that all three dictionary types can be set independently.
     */
    @Test
    public void testSetPackageobfuscationdictionaryIndependentFromOtherDictionaries() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File packageDict = new File("package-dict.txt");
        File classDict = new File("class-dict.txt");
        File memberDict = new File("member-dict.txt");

        task.setPackageobfuscationdictionary(packageDict);
        task.setClassobfuscationdictionary(classDict);
        task.setObfuscationdictionary(memberDict);

        assertTrue(task.configuration.packageObfuscationDictionary.toString().contains("package-dict.txt"),
            "packageObfuscationDictionary should be package-dict.txt");
        assertTrue(task.configuration.classObfuscationDictionary.toString().contains("class-dict.txt"),
            "classObfuscationDictionary should be class-dict.txt");
        assertTrue(task.configuration.obfuscationDictionary.toString().contains("member-dict.txt"),
            "obfuscationDictionary should be member-dict.txt");
    }
}
