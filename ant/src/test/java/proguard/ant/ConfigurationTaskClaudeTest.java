package proguard.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import proguard.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ConfigurationTask.
 * Tests all public methods including constructor, appendTo, addConfigured* methods, and addText.
 */
public class ConfigurationTaskClaudeTest {

    private ConfigurationTask configurationTask;
    private Project project;

    @BeforeEach
    public void setUp() {
        configurationTask = new ConfigurationTask();
        project = new Project();
        project.init();
        configurationTask.setProject(project);
    }

    /**
     * Test that the default constructor successfully creates a ConfigurationTask instance
     * with an initialized Configuration object.
     */
    @Test
    public void testConstructorCreatesInstance() {
        ConfigurationTask task = new ConfigurationTask();
        assertNotNull(task, "ConfigurationTask should be created successfully");
        assertNotNull(task.configuration, "Configuration should be initialized");
    }

    /**
     * Test that multiple instances are independent.
     */
    @Test
    public void testConstructorCreatesIndependentInstances() {
        ConfigurationTask task1 = new ConfigurationTask();
        ConfigurationTask task2 = new ConfigurationTask();

        assertNotNull(task1, "First task should be created");
        assertNotNull(task2, "Second task should be created");
        assertNotSame(task1, task2, "Tasks should be different instances");
        assertNotSame(task1.configuration, task2.configuration, "Configurations should be different instances");
    }

    /**
     * Test appendTo method with an empty configuration task.
     */
    @Test
    public void testAppendToWithEmptyConfiguration() {
        Configuration targetConfig = new Configuration();

        assertDoesNotThrow(() -> configurationTask.appendTo(targetConfig),
            "appendTo should work with empty configuration");
    }

    /**
     * Test addConfiguredInjar with a valid ClassPathElement.
     */
    @Test
    public void testAddConfiguredInjar(@TempDir Path tempDir) throws Exception {
        File jarFile = tempDir.resolve("input.jar").toFile();
        jarFile.createNewFile();

        ClassPathElement classPathElement = new ClassPathElement(project);
        classPathElement.setLocation(jarFile);

        assertDoesNotThrow(() -> configurationTask.addConfiguredInjar(classPathElement),
            "addConfiguredInjar should accept valid ClassPathElement");

        assertNotNull(configurationTask.configuration.programJars,
            "programJars should be initialized");
    }

    /**
     * Test addConfiguredOutjar with a valid ClassPathElement.
     */
    @Test
    public void testAddConfiguredOutjar(@TempDir Path tempDir) throws Exception {
        File jarFile = tempDir.resolve("output.jar").toFile();
        jarFile.createNewFile();

        ClassPathElement classPathElement = new ClassPathElement(project);
        classPathElement.setLocation(jarFile);

        assertDoesNotThrow(() -> configurationTask.addConfiguredOutjar(classPathElement),
            "addConfiguredOutjar should accept valid ClassPathElement");

        assertNotNull(configurationTask.configuration.programJars,
            "programJars should be initialized");
    }

    /**
     * Test addConfiguredLibraryjar with a valid ClassPathElement.
     */
    @Test
    public void testAddConfiguredLibraryjar(@TempDir Path tempDir) throws Exception {
        File jarFile = tempDir.resolve("library.jar").toFile();
        jarFile.createNewFile();

        ClassPathElement classPathElement = new ClassPathElement(project);
        classPathElement.setLocation(jarFile);

        assertDoesNotThrow(() -> configurationTask.addConfiguredLibraryjar(classPathElement),
            "addConfiguredLibraryjar should accept valid ClassPathElement");

        assertNotNull(configurationTask.configuration.libraryJars,
            "libraryJars should be initialized");
    }

    /**
     * Test addConfiguredKeepdirectory with a valid FilterElement.
     */
    @Test
    public void testAddConfiguredKeepdirectory() {
        FilterElement filterElement = new FilterElement();
        filterElement.setName("com/example/**");

        assertDoesNotThrow(() -> configurationTask.addConfiguredKeepdirectory(filterElement),
            "addConfiguredKeepdirectory should accept valid FilterElement");

        assertNotNull(configurationTask.configuration.keepDirectories,
            "keepDirectories should be initialized");
    }

    /**
     * Test addConfiguredKeepdirectories with a valid FilterElement.
     */
    @Test
    public void testAddConfiguredKeepdirectories() {
        FilterElement filterElement = new FilterElement();
        filterElement.setName("com/example/**");

        assertDoesNotThrow(() -> configurationTask.addConfiguredKeepdirectories(filterElement),
            "addConfiguredKeepdirectories should accept valid FilterElement");

        assertNotNull(configurationTask.configuration.keepDirectories,
            "keepDirectories should be initialized");
    }

    /**
     * Test addConfiguredKeep with a valid KeepSpecificationElement.
     */
    @Test
    public void testAddConfiguredKeep() {
        KeepSpecificationElement keepElement = new KeepSpecificationElement();
        keepElement.setName("com.example.MyClass");

        assertDoesNotThrow(() -> configurationTask.addConfiguredKeep(keepElement),
            "addConfiguredKeep should accept valid KeepSpecificationElement");

        assertNotNull(configurationTask.configuration.keep,
            "keep should be initialized");
        assertEquals(1, configurationTask.configuration.keep.size(),
            "keep should contain one specification");
    }

    /**
     * Test addConfiguredKeepclassmembers with a valid KeepSpecificationElement.
     */
    @Test
    public void testAddConfiguredKeepclassmembers() {
        KeepSpecificationElement keepElement = new KeepSpecificationElement();
        keepElement.setName("com.example.MyClass");

        assertDoesNotThrow(() -> configurationTask.addConfiguredKeepclassmembers(keepElement),
            "addConfiguredKeepclassmembers should accept valid KeepSpecificationElement");

        assertNotNull(configurationTask.configuration.keep,
            "keep should be initialized");
    }

    /**
     * Test addConfiguredKeepclasseswithmembers with a valid KeepSpecificationElement.
     */
    @Test
    public void testAddConfiguredKeepclasseswithmembers() {
        KeepSpecificationElement keepElement = new KeepSpecificationElement();
        keepElement.setName("com.example.MyClass");

        assertDoesNotThrow(() -> configurationTask.addConfiguredKeepclasseswithmembers(keepElement),
            "addConfiguredKeepclasseswithmembers should accept valid KeepSpecificationElement");

        assertNotNull(configurationTask.configuration.keep,
            "keep should be initialized");
    }

    /**
     * Test addConfiguredKeepnames with a valid KeepSpecificationElement.
     * This method should set allowshrinking to true.
     */
    @Test
    public void testAddConfiguredKeepnames() {
        KeepSpecificationElement keepElement = new KeepSpecificationElement();
        keepElement.setName("com.example.MyClass");

        assertDoesNotThrow(() -> configurationTask.addConfiguredKeepnames(keepElement),
            "addConfiguredKeepnames should accept valid KeepSpecificationElement");

        assertNotNull(configurationTask.configuration.keep,
            "keep should be initialized");
    }

    /**
     * Test addConfiguredKeepclassmembernames with a valid KeepSpecificationElement.
     */
    @Test
    public void testAddConfiguredKeepclassmembernames() {
        KeepSpecificationElement keepElement = new KeepSpecificationElement();
        keepElement.setName("com.example.MyClass");

        assertDoesNotThrow(() -> configurationTask.addConfiguredKeepclassmembernames(keepElement),
            "addConfiguredKeepclassmembernames should accept valid KeepSpecificationElement");

        assertNotNull(configurationTask.configuration.keep,
            "keep should be initialized");
    }

    /**
     * Test addConfiguredKeepclasseswithmembernames with a valid KeepSpecificationElement.
     */
    @Test
    public void testAddConfiguredKeepclasseswithmembernames() {
        KeepSpecificationElement keepElement = new KeepSpecificationElement();
        keepElement.setName("com.example.MyClass");

        assertDoesNotThrow(() -> configurationTask.addConfiguredKeepclasseswithmembernames(keepElement),
            "addConfiguredKeepclasseswithmembernames should accept valid KeepSpecificationElement");

        assertNotNull(configurationTask.configuration.keep,
            "keep should be initialized");
    }

    /**
     * Test addConfiguredWhyareyoukeeping with a valid ClassSpecificationElement.
     */
    @Test
    public void testAddConfiguredWhyareyoukeeping() {
        ClassSpecificationElement classSpec = new ClassSpecificationElement();
        classSpec.setName("com.example.MyClass");

        assertDoesNotThrow(() -> configurationTask.addConfiguredWhyareyoukeeping(classSpec),
            "addConfiguredWhyareyoukeeping should accept valid ClassSpecificationElement");

        assertNotNull(configurationTask.configuration.whyAreYouKeeping,
            "whyAreYouKeeping should be initialized");
    }

    /**
     * Test addConfiguredAssumenosideeffects with a valid ClassSpecificationElement.
     */
    @Test
    public void testAddConfiguredAssumenosideeffects() {
        ClassSpecificationElement classSpec = new ClassSpecificationElement();
        classSpec.setName("com.example.MyClass");

        assertDoesNotThrow(() -> configurationTask.addConfiguredAssumenosideeffects(classSpec),
            "addConfiguredAssumenosideeffects should accept valid ClassSpecificationElement");

        assertNotNull(configurationTask.configuration.assumeNoSideEffects,
            "assumeNoSideEffects should be initialized");
    }

    /**
     * Test addConfiguredAssumenoexternalsideeffects with a valid ClassSpecificationElement.
     */
    @Test
    public void testAddConfiguredAssumenoexternalsideeffects() {
        ClassSpecificationElement classSpec = new ClassSpecificationElement();
        classSpec.setName("com.example.MyClass");

        assertDoesNotThrow(() -> configurationTask.addConfiguredAssumenoexternalsideeffects(classSpec),
            "addConfiguredAssumenoexternalsideeffects should accept valid ClassSpecificationElement");

        assertNotNull(configurationTask.configuration.assumeNoExternalSideEffects,
            "assumeNoExternalSideEffects should be initialized");
    }

    /**
     * Test addConfiguredAssumenoescapingparameters with a valid ClassSpecificationElement.
     */
    @Test
    public void testAddConfiguredAssumenoescapingparameters() {
        ClassSpecificationElement classSpec = new ClassSpecificationElement();
        classSpec.setName("com.example.MyClass");

        assertDoesNotThrow(() -> configurationTask.addConfiguredAssumenoescapingparameters(classSpec),
            "addConfiguredAssumenoescapingparameters should accept valid ClassSpecificationElement");

        assertNotNull(configurationTask.configuration.assumeNoEscapingParameters,
            "assumeNoEscapingParameters should be initialized");
    }

    /**
     * Test addConfiguredAssumenoexternalreturnvalues with a valid ClassSpecificationElement.
     */
    @Test
    public void testAddConfiguredAssumenoexternalreturnvalues() {
        ClassSpecificationElement classSpec = new ClassSpecificationElement();
        classSpec.setName("com.example.MyClass");

        assertDoesNotThrow(() -> configurationTask.addConfiguredAssumenoexternalreturnvalues(classSpec),
            "addConfiguredAssumenoexternalreturnvalues should accept valid ClassSpecificationElement");

        assertNotNull(configurationTask.configuration.assumeNoExternalReturnValues,
            "assumeNoExternalReturnValues should be initialized");
    }

    /**
     * Test addConfiguredAssumevalues with a valid ClassSpecificationElement.
     */
    @Test
    public void testAddConfiguredAssumevalues() {
        ClassSpecificationElement classSpec = new ClassSpecificationElement();
        classSpec.setName("com.example.MyClass");

        assertDoesNotThrow(() -> configurationTask.addConfiguredAssumevalues(classSpec),
            "addConfiguredAssumevalues should accept valid ClassSpecificationElement");

        assertNotNull(configurationTask.configuration.assumeValues,
            "assumeValues should be initialized");
    }

    /**
     * Test addConfiguredOptimizations with a valid FilterElement.
     */
    @Test
    public void testAddConfiguredOptimizations() {
        FilterElement filterElement = new FilterElement();
        filterElement.setName("!code/simplification/arithmetic");

        assertDoesNotThrow(() -> configurationTask.addConfiguredOptimizations(filterElement),
            "addConfiguredOptimizations should accept valid FilterElement");

        assertNotNull(configurationTask.configuration.optimizations,
            "optimizations should be initialized");
    }

    /**
     * Test addConfiguredOptimization with a valid FilterElement.
     */
    @Test
    public void testAddConfiguredOptimization() {
        FilterElement filterElement = new FilterElement();
        filterElement.setName("!code/simplification/arithmetic");

        assertDoesNotThrow(() -> configurationTask.addConfiguredOptimization(filterElement),
            "addConfiguredOptimization should accept valid FilterElement");

        assertNotNull(configurationTask.configuration.optimizations,
            "optimizations should be initialized");
    }

    /**
     * Test addConfiguredKeeppackagename with a valid FilterElement.
     */
    @Test
    public void testAddConfiguredKeeppackagename() {
        FilterElement filterElement = new FilterElement();
        filterElement.setName("com.example.**");

        assertDoesNotThrow(() -> configurationTask.addConfiguredKeeppackagename(filterElement),
            "addConfiguredKeeppackagename should accept valid FilterElement");

        assertNotNull(configurationTask.configuration.keepPackageNames,
            "keepPackageNames should be initialized");
    }

    /**
     * Test addConfiguredKeeppackagenames with a valid FilterElement.
     */
    @Test
    public void testAddConfiguredKeeppackagenames() {
        FilterElement filterElement = new FilterElement();
        filterElement.setName("com.example.**");

        assertDoesNotThrow(() -> configurationTask.addConfiguredKeeppackagenames(filterElement),
            "addConfiguredKeeppackagenames should accept valid FilterElement");

        assertNotNull(configurationTask.configuration.keepPackageNames,
            "keepPackageNames should be initialized");
    }

    /**
     * Test addConfiguredKeepattributes with a valid FilterElement.
     */
    @Test
    public void testAddConfiguredKeepattributes() {
        FilterElement filterElement = new FilterElement();
        filterElement.setName("Signature,InnerClasses");

        assertDoesNotThrow(() -> configurationTask.addConfiguredKeepattributes(filterElement),
            "addConfiguredKeepattributes should accept valid FilterElement");

        assertNotNull(configurationTask.configuration.keepAttributes,
            "keepAttributes should be initialized");
    }

    /**
     * Test addConfiguredKeepattribute with a valid FilterElement.
     */
    @Test
    public void testAddConfiguredKeepattribute() {
        FilterElement filterElement = new FilterElement();
        filterElement.setName("Signature");

        assertDoesNotThrow(() -> configurationTask.addConfiguredKeepattribute(filterElement),
            "addConfiguredKeepattribute should accept valid FilterElement");

        assertNotNull(configurationTask.configuration.keepAttributes,
            "keepAttributes should be initialized");
    }

    /**
     * Test addConfiguredAdaptclassstrings with a valid FilterElement.
     */
    @Test
    public void testAddConfiguredAdaptclassstrings() {
        FilterElement filterElement = new FilterElement();
        filterElement.setName("com.example.**");

        assertDoesNotThrow(() -> configurationTask.addConfiguredAdaptclassstrings(filterElement),
            "addConfiguredAdaptclassstrings should accept valid FilterElement");

        assertNotNull(configurationTask.configuration.adaptClassStrings,
            "adaptClassStrings should be initialized");
    }

    /**
     * Test addConfiguredAdaptresourcefilenames with a valid FilterElement.
     */
    @Test
    public void testAddConfiguredAdaptresourcefilenames() {
        FilterElement filterElement = new FilterElement();
        filterElement.setName("**.properties");

        assertDoesNotThrow(() -> configurationTask.addConfiguredAdaptresourcefilenames(filterElement),
            "addConfiguredAdaptresourcefilenames should accept valid FilterElement");

        assertNotNull(configurationTask.configuration.adaptResourceFileNames,
            "adaptResourceFileNames should be initialized");
    }

    /**
     * Test addConfiguredAdaptresourcefilecontents with a valid FilterElement.
     */
    @Test
    public void testAddConfiguredAdaptresourcefilecontents() {
        FilterElement filterElement = new FilterElement();
        filterElement.setName("**.properties");

        assertDoesNotThrow(() -> configurationTask.addConfiguredAdaptresourcefilecontents(filterElement),
            "addConfiguredAdaptresourcefilecontents should accept valid FilterElement");

        assertNotNull(configurationTask.configuration.adaptResourceFileContents,
            "adaptResourceFileContents should be initialized");
    }

    /**
     * Test addConfiguredDontnote with a valid FilterElement.
     */
    @Test
    public void testAddConfiguredDontnote() {
        FilterElement filterElement = new FilterElement();
        filterElement.setName("com.example.**");

        assertDoesNotThrow(() -> configurationTask.addConfiguredDontnote(filterElement),
            "addConfiguredDontnote should accept valid FilterElement");

        assertNotNull(configurationTask.configuration.note,
            "note should be initialized");
    }

    /**
     * Test addConfiguredDontwarn with a valid FilterElement.
     */
    @Test
    public void testAddConfiguredDontwarn() {
        FilterElement filterElement = new FilterElement();
        filterElement.setName("com.example.**");

        assertDoesNotThrow(() -> configurationTask.addConfiguredDontwarn(filterElement),
            "addConfiguredDontwarn should accept valid FilterElement");

        assertNotNull(configurationTask.configuration.warn,
            "warn should be initialized");
    }

    /**
     * Test addConfiguredConfiguration with a valid ConfigurationElement.
     */
    @Test
    public void testAddConfiguredConfiguration(@TempDir Path tempDir) throws Exception {
        // Create a simple ProGuard configuration file
        File configFile = tempDir.resolve("test.pro").toFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("-dontoptimize\n");
        }

        ConfigurationElement configElement = new ConfigurationElement();
        configElement.setProject(project);
        configElement.setDir(tempDir.toFile());
        configElement.setIncludes("test.pro");

        assertDoesNotThrow(() -> configurationTask.addConfiguredConfiguration(configElement),
            "addConfiguredConfiguration should accept valid ConfigurationElement");
    }

    /**
     * Test addText with valid configuration text.
     */
    @Test
    public void testAddTextWithValidConfiguration() {
        String configText = "-dontoptimize\n-dontobfuscate\n";

        assertDoesNotThrow(() -> configurationTask.addText(configText),
            "addText should accept valid configuration text");
    }

    /**
     * Test addText with empty text.
     */
    @Test
    public void testAddTextWithEmptyText() {
        assertDoesNotThrow(() -> configurationTask.addText(""),
            "addText should accept empty text");
    }

    /**
     * Test addText with whitespace only.
     */
    @Test
    public void testAddTextWithWhitespace() {
        assertDoesNotThrow(() -> configurationTask.addText("   \n\t  "),
            "addText should accept whitespace");
    }

    /**
     * Test addText with invalid configuration throws BuildException.
     */
    @Test
    public void testAddTextWithInvalidConfiguration() {
        String invalidConfig = "-invalidoption";

        assertThrows(BuildException.class,
            () -> configurationTask.addText(invalidConfig),
            "addText should throw BuildException for invalid configuration");
    }

    /**
     * Test appendTo copies programJars correctly.
     */
    @Test
    public void testAppendToCopiesProgramJars(@TempDir Path tempDir) throws Exception {
        File jarFile = tempDir.resolve("input.jar").toFile();
        jarFile.createNewFile();

        ClassPathElement classPathElement = new ClassPathElement(project);
        classPathElement.setLocation(jarFile);
        configurationTask.addConfiguredInjar(classPathElement);

        Configuration targetConfig = new Configuration();
        configurationTask.appendTo(targetConfig);

        assertNotNull(targetConfig.programJars, "programJars should be copied");
        assertFalse(targetConfig.programJars.isEmpty(), "programJars should not be empty");
    }

    /**
     * Test appendTo copies libraryJars correctly.
     */
    @Test
    public void testAppendToCopiesLibraryJars(@TempDir Path tempDir) throws Exception {
        File jarFile = tempDir.resolve("library.jar").toFile();
        jarFile.createNewFile();

        ClassPathElement classPathElement = new ClassPathElement(project);
        classPathElement.setLocation(jarFile);
        configurationTask.addConfiguredLibraryjar(classPathElement);

        Configuration targetConfig = new Configuration();
        configurationTask.appendTo(targetConfig);

        assertNotNull(targetConfig.libraryJars, "libraryJars should be copied");
        assertFalse(targetConfig.libraryJars.isEmpty(), "libraryJars should not be empty");
    }

    /**
     * Test appendTo copies keep specifications correctly.
     */
    @Test
    public void testAppendToCopiesKeepSpecifications() {
        KeepSpecificationElement keepElement = new KeepSpecificationElement();
        keepElement.setName("com.example.MyClass");
        configurationTask.addConfiguredKeep(keepElement);

        Configuration targetConfig = new Configuration();
        configurationTask.appendTo(targetConfig);

        assertNotNull(targetConfig.keep, "keep should be copied");
        assertFalse(targetConfig.keep.isEmpty(), "keep should not be empty");
    }

    /**
     * Test appendTo merges with existing configuration.
     */
    @Test
    public void testAppendToMergesWithExistingConfiguration() {
        KeepSpecificationElement keepElement1 = new KeepSpecificationElement();
        keepElement1.setName("com.example.Class1");

        KeepSpecificationElement keepElement2 = new KeepSpecificationElement();
        keepElement2.setName("com.example.Class2");

        Configuration targetConfig = new Configuration();
        configurationTask.addConfiguredKeep(keepElement1);
        configurationTask.appendTo(targetConfig);

        int initialSize = targetConfig.keep.size();

        ConfigurationTask task2 = new ConfigurationTask();
        task2.setProject(project);
        task2.addConfiguredKeep(keepElement2);
        task2.appendTo(targetConfig);

        assertEquals(initialSize + 1, targetConfig.keep.size(),
            "Target configuration should contain specifications from both tasks");
    }

    /**
     * Test that multiple keep specifications can be added.
     */
    @Test
    public void testMultipleKeepSpecifications() {
        KeepSpecificationElement keep1 = new KeepSpecificationElement();
        keep1.setName("com.example.Class1");

        KeepSpecificationElement keep2 = new KeepSpecificationElement();
        keep2.setName("com.example.Class2");

        configurationTask.addConfiguredKeep(keep1);
        configurationTask.addConfiguredKeepclassmembers(keep2);

        assertNotNull(configurationTask.configuration.keep);
        assertEquals(2, configurationTask.configuration.keep.size(),
            "Should have two keep specifications");
    }

    /**
     * Test that multiple filter elements can be added.
     */
    @Test
    public void testMultipleFilterElements() {
        FilterElement filter1 = new FilterElement();
        filter1.setName("com.example.**");

        FilterElement filter2 = new FilterElement();
        filter2.setName("org.test.**");

        configurationTask.addConfiguredDontwarn(filter1);
        configurationTask.addConfiguredDontwarn(filter2);

        assertNotNull(configurationTask.configuration.warn);
        assertTrue(configurationTask.configuration.warn.size() >= 2,
            "Should have multiple filter entries");
    }

    /**
     * Test addText with Ant properties replacement.
     */
    @Test
    public void testAddTextWithAntProperties() {
        project.setProperty("test.option", "dontoptimize");
        String configText = "-${test.option}\n";

        assertDoesNotThrow(() -> configurationTask.addText(configText),
            "addText should handle Ant property substitution");
    }

    /**
     * Test appendTo with null lists in target configuration.
     */
    @Test
    public void testAppendToWithNullListsInTarget() {
        KeepSpecificationElement keepElement = new KeepSpecificationElement();
        keepElement.setName("com.example.MyClass");
        configurationTask.addConfiguredKeep(keepElement);

        Configuration targetConfig = new Configuration();
        // Ensure the target has null lists (default state)
        assertNull(targetConfig.keep, "Initial keep should be null");

        configurationTask.appendTo(targetConfig);

        assertNotNull(targetConfig.keep, "keep should be initialized after append");
        assertFalse(targetConfig.keep.isEmpty(), "keep should contain specifications");
    }

    /**
     * Test that FilterElement with null name clears the filter.
     */
    @Test
    public void testFilterElementWithNullName() {
        FilterElement filterElement = new FilterElement();
        // Don't set a name, leaving it null

        assertDoesNotThrow(() -> configurationTask.addConfiguredDontwarn(filterElement),
            "Should handle FilterElement with null name");

        assertNotNull(configurationTask.configuration.warn);
    }

    /**
     * Test addConfiguredConfiguration with ConfigurationTask reference.
     */
    @Test
    public void testAddConfiguredConfigurationWithTaskReference() {
        ConfigurationTask referencedTask = new ConfigurationTask();
        referencedTask.setProject(project);

        KeepSpecificationElement keepElement = new KeepSpecificationElement();
        keepElement.setName("com.example.MyClass");
        referencedTask.addConfiguredKeep(keepElement);

        project.addReference("referenced.config", referencedTask);

        ConfigurationElement configElement = new ConfigurationElement();
        configElement.setProject(project);
        configElement.setRefid(new org.apache.tools.ant.types.Reference(project, "referenced.config"));

        assertDoesNotThrow(() -> configurationTask.addConfiguredConfiguration(configElement),
            "addConfiguredConfiguration should handle ConfigurationTask reference");
    }

    /**
     * Test that constructor initializes configuration with empty state.
     */
    @Test
    public void testConstructorInitializesEmptyConfiguration() {
        ConfigurationTask task = new ConfigurationTask();

        assertNotNull(task.configuration);
        assertNull(task.configuration.programJars, "programJars should start as null");
        assertNull(task.configuration.libraryJars, "libraryJars should start as null");
        assertNull(task.configuration.keep, "keep should start as null");
    }

    /**
     * Test addConfiguredInjar initializes programJars when null.
     */
    @Test
    public void testAddConfiguredInjarInitializesProgramJars(@TempDir Path tempDir) throws Exception {
        File jarFile = tempDir.resolve("test.jar").toFile();
        jarFile.createNewFile();

        ClassPathElement element = new ClassPathElement(project);
        element.setLocation(jarFile);

        assertNull(configurationTask.configuration.programJars, "Initial state should be null");

        configurationTask.addConfiguredInjar(element);

        assertNotNull(configurationTask.configuration.programJars, "programJars should be initialized");
    }

    /**
     * Test addConfiguredOutjar initializes programJars when null.
     */
    @Test
    public void testAddConfiguredOutjarInitializesProgramJars(@TempDir Path tempDir) throws Exception {
        File jarFile = tempDir.resolve("test.jar").toFile();
        jarFile.createNewFile();

        ClassPathElement element = new ClassPathElement(project);
        element.setLocation(jarFile);

        assertNull(configurationTask.configuration.programJars, "Initial state should be null");

        configurationTask.addConfiguredOutjar(element);

        assertNotNull(configurationTask.configuration.programJars, "programJars should be initialized");
    }

    /**
     * Test addConfiguredLibraryjar initializes libraryJars when null.
     */
    @Test
    public void testAddConfiguredLibraryjarInitializesLibraryJars(@TempDir Path tempDir) throws Exception {
        File jarFile = tempDir.resolve("test.jar").toFile();
        jarFile.createNewFile();

        ClassPathElement element = new ClassPathElement(project);
        element.setLocation(jarFile);

        assertNull(configurationTask.configuration.libraryJars, "Initial state should be null");

        configurationTask.addConfiguredLibraryjar(element);

        assertNotNull(configurationTask.configuration.libraryJars, "libraryJars should be initialized");
    }
}
