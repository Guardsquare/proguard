package proguard.ant;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ConfigurationElement default constructor.
 */
public class ConfigurationElementClaude_constructorTest {

    /**
     * Test that the default constructor successfully creates a ConfigurationElement.
     */
    @Test
    public void testDefaultConstructorCreatesInstance() {
        ConfigurationElement element = new ConfigurationElement();

        assertNotNull(element, "ConfigurationElement should be created successfully");
    }

    /**
     * Test that the default constructor creates a valid DataType.
     * ConfigurationElement extends FileSet (which extends DataType), so it should behave as one.
     */
    @Test
    public void testDefaultConstructorCreatesValidDataType() {
        ConfigurationElement element = new ConfigurationElement();

        // Verify it behaves as a DataType by checking we can use DataType methods
        assertFalse(element.isReference(), "Newly created element should not be a reference");
    }

    /**
     * Test that the default constructor creates a valid FileSet.
     * ConfigurationElement extends FileSet directly.
     */
    @Test
    public void testDefaultConstructorCreatesValidFileSet() {
        ConfigurationElement element = new ConfigurationElement();

        // Verify it behaves as a FileSet
        assertInstanceOf(FileSet.class, element, "ConfigurationElement should be a FileSet");
    }

    /**
     * Test that the constructor initializes the element to work with Ant projects.
     */
    @Test
    public void testDefaultConstructorWorksWithAntProject() {
        ConfigurationElement element = new ConfigurationElement();
        Project project = new Project();
        project.init();

        // DataType provides setProject method
        element.setProject(project);

        assertSame(project, element.getProject(), "Project should be settable on the element");
    }

    /**
     * Test that multiple instances can be created independently.
     */
    @Test
    public void testDefaultConstructorCreatesIndependentInstances() {
        ConfigurationElement element1 = new ConfigurationElement();
        ConfigurationElement element2 = new ConfigurationElement();

        assertNotNull(element1, "First element should be created");
        assertNotNull(element2, "Second element should be created");
        assertNotSame(element1, element2, "Elements should be different instances");
    }

    /**
     * Test that the constructor initializes the element to work with file includes.
     * Since ConfigurationElement extends FileSet, it should support file patterns.
     */
    @Test
    public void testDefaultConstructorAllowsFileIncludes() {
        ConfigurationElement element = new ConfigurationElement();

        // Should be able to set includes pattern without errors
        assertDoesNotThrow(() -> element.setIncludes("*.pro"),
            "Should be able to set includes pattern");
    }

    /**
     * Test that the constructor initializes the element to work with file excludes.
     */
    @Test
    public void testDefaultConstructorAllowsFileExcludes() {
        ConfigurationElement element = new ConfigurationElement();

        // Should be able to set excludes pattern without errors
        assertDoesNotThrow(() -> element.setExcludes("*.tmp"),
            "Should be able to set excludes pattern");
    }

    /**
     * Test that the constructor initializes the element to work with a base directory.
     */
    @Test
    public void testDefaultConstructorAllowsBaseDirectory(@TempDir Path tempDir) {
        ConfigurationElement element = new ConfigurationElement();
        File baseDir = tempDir.toFile();

        // Should be able to set base directory without errors
        assertDoesNotThrow(() -> element.setDir(baseDir),
            "Should be able to set base directory");
    }

    /**
     * Test that the constructor initializes the element to work with references.
     */
    @Test
    public void testDefaultConstructorAllowsReferences() {
        ConfigurationElement element = new ConfigurationElement();
        Project project = new Project();
        project.init();
        element.setProject(project);

        // Should be able to set a reference without errors
        assertDoesNotThrow(() -> element.setRefid(new org.apache.tools.ant.types.Reference(project, "test-ref")),
            "Should be able to set reference");
    }

    /**
     * Test that a newly constructed element can be used with the appendTo method.
     */
    @Test
    public void testDefaultConstructorAllowsAppendTo(@TempDir Path tempDir) throws Exception {
        ConfigurationElement element = new ConfigurationElement();
        Project project = new Project();
        project.init();
        element.setProject(project);

        // Set up a valid file set with an existing configuration file
        File configFile = tempDir.resolve("test.pro").toFile();
        configFile.createNewFile();

        element.setDir(tempDir.toFile());
        element.setIncludes("*.pro");

        proguard.Configuration configuration = new proguard.Configuration();

        // Should be able to call appendTo without errors (even if the config file is empty)
        assertDoesNotThrow(() -> element.appendTo(configuration),
            "Should be able to call appendTo");
    }

    /**
     * Test that the constructor creates an element that can handle case sensitivity settings.
     */
    @Test
    public void testDefaultConstructorAllowsCaseSensitivity() {
        ConfigurationElement element = new ConfigurationElement();

        // Should be able to set case sensitivity without errors
        assertDoesNotThrow(() -> element.setCaseSensitive(true),
            "Should be able to set case sensitivity to true");
        assertDoesNotThrow(() -> element.setCaseSensitive(false),
            "Should be able to set case sensitivity to false");
    }

    /**
     * Test that the constructor creates an element that can handle follow symlinks setting.
     */
    @Test
    public void testDefaultConstructorAllowsFollowSymlinks() {
        ConfigurationElement element = new ConfigurationElement();

        // Should be able to set follow symlinks without errors
        assertDoesNotThrow(() -> element.setFollowSymlinks(true),
            "Should be able to set follow symlinks to true");
        assertDoesNotThrow(() -> element.setFollowSymlinks(false),
            "Should be able to set follow symlinks to false");
    }

    /**
     * Test that the constructor creates an element that can create nested include patterns.
     */
    @Test
    public void testDefaultConstructorAllowsNestedIncludes() {
        ConfigurationElement element = new ConfigurationElement();

        // Should be able to create nested includes without errors
        assertDoesNotThrow(() -> {
            org.apache.tools.ant.types.PatternSet.NameEntry include = element.createInclude();
            include.setName("*.pro");
        }, "Should be able to create nested include patterns");
    }

    /**
     * Test that the constructor creates an element that can create nested exclude patterns.
     */
    @Test
    public void testDefaultConstructorAllowsNestedExcludes() {
        ConfigurationElement element = new ConfigurationElement();

        // Should be able to create nested excludes without errors
        assertDoesNotThrow(() -> {
            org.apache.tools.ant.types.PatternSet.NameEntry exclude = element.createExclude();
            exclude.setName("*.tmp");
        }, "Should be able to create nested exclude patterns");
    }

    /**
     * Test that the constructor creates an element that can add nested PatternSets.
     */
    @Test
    public void testDefaultConstructorAllowsNestedPatternSets() {
        ConfigurationElement element = new ConfigurationElement();

        // Should be able to add nested pattern sets without errors
        assertDoesNotThrow(() -> {
            org.apache.tools.ant.types.PatternSet patternSet = element.createPatternSet();
            patternSet.setIncludes("*.pro");
        }, "Should be able to create nested pattern sets");
    }

    /**
     * Test that the constructor creates an element that works with multiple file patterns.
     */
    @Test
    public void testDefaultConstructorHandlesMultiplePatterns() {
        ConfigurationElement element = new ConfigurationElement();

        // Should be able to set multiple patterns without errors
        assertDoesNotThrow(() -> {
            element.setIncludes("*.pro, *.txt");
            element.setExcludes("test*.pro, temp*.txt");
        }, "Should be able to set multiple file patterns");
    }

    /**
     * Test that the constructor creates an element that can be fully configured as a FileSet.
     */
    @Test
    public void testDefaultConstructorAllowsFullFileSetConfiguration(@TempDir Path tempDir) {
        ConfigurationElement element = new ConfigurationElement();
        Project project = new Project();
        project.init();
        element.setProject(project);

        // Configure all FileSet properties
        assertDoesNotThrow(() -> {
            element.setDir(tempDir.toFile());
            element.setIncludes("*.pro");
            element.setExcludes("*.tmp");
            element.setCaseSensitive(true);
            element.setFollowSymlinks(false);

            org.apache.tools.ant.types.PatternSet.NameEntry include = element.createInclude();
            include.setName("config.pro");

            org.apache.tools.ant.types.PatternSet.NameEntry exclude = element.createExclude();
            exclude.setName("test.pro");
        }, "Should be able to configure all FileSet properties");
    }
}
