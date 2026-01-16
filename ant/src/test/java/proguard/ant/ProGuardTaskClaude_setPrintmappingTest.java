package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setPrintmapping method.
 * Tests that the method properly sets the printMapping field in the configuration
 * using the optionalFile helper which handles special boolean-like filenames.
 */
public class ProGuardTaskClaude_setPrintmappingTest {

    /**
     * Test that setPrintmapping with a regular file sets the printMapping field.
     */
    @Test
    public void testSetPrintmappingRegularFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File mappingFile = new File("mapping.txt");
        task.setPrintmapping(mappingFile);

        assertNotNull(task.configuration.printMapping,
            "printMapping should be set");
        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
            "printMapping filename should match");
    }

    /**
     * Test that setPrintmapping with absolute path file works correctly.
     */
    @Test
    public void testSetPrintmappingAbsoluteFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File mappingFile = new File("/tmp/mapping.txt");
        task.setPrintmapping(mappingFile);

        assertNotNull(task.configuration.printMapping,
            "printMapping should be set");
        assertTrue(task.configuration.printMapping.isAbsolute(),
            "printMapping should be absolute path");
        assertEquals("/tmp/mapping.txt", task.configuration.printMapping.getPath(),
            "printMapping path should match");
    }

    /**
     * Test that setPrintmapping with "false" filename sets printMapping to null.
     */
    @Test
    public void testSetPrintmappingFalse() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("false");
        task.setPrintmapping(falseFile);

        assertNull(task.configuration.printMapping,
            "printMapping should be null for 'false' filename");
    }

    /**
     * Test that setPrintmapping with "no" filename sets printMapping to null.
     */
    @Test
    public void testSetPrintmappingNo() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File noFile = new File("no");
        task.setPrintmapping(noFile);

        assertNull(task.configuration.printMapping,
            "printMapping should be null for 'no' filename");
    }

    /**
     * Test that setPrintmapping with "off" filename sets printMapping to null.
     */
    @Test
    public void testSetPrintmappingOff() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File offFile = new File("off");
        task.setPrintmapping(offFile);

        assertNull(task.configuration.printMapping,
            "printMapping should be null for 'off' filename");
    }

    /**
     * Test that setPrintmapping with "true" filename sets printMapping to STD_OUT.
     */
    @Test
    public void testSetPrintmappingTrue() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("true");
        task.setPrintmapping(trueFile);

        assertNotNull(task.configuration.printMapping,
            "printMapping should not be null for 'true' filename");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
            "printMapping should be Configuration.STD_OUT for 'true' filename");
    }

    /**
     * Test that setPrintmapping with "yes" filename sets printMapping to STD_OUT.
     */
    @Test
    public void testSetPrintmappingYes() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File yesFile = new File("yes");
        task.setPrintmapping(yesFile);

        assertNotNull(task.configuration.printMapping,
            "printMapping should not be null for 'yes' filename");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
            "printMapping should be Configuration.STD_OUT for 'yes' filename");
    }

    /**
     * Test that setPrintmapping with "on" filename sets printMapping to STD_OUT.
     */
    @Test
    public void testSetPrintmappingOn() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File onFile = new File("on");
        task.setPrintmapping(onFile);

        assertNotNull(task.configuration.printMapping,
            "printMapping should not be null for 'on' filename");
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
            "printMapping should be Configuration.STD_OUT for 'on' filename");
    }

    /**
     * Test that setPrintmapping is case-insensitive for "FALSE".
     */
    @Test
    public void testSetPrintmappingFalseUpperCase() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("FALSE");
        task.setPrintmapping(falseFile);

        assertNull(task.configuration.printMapping,
            "printMapping should be null for 'FALSE' filename (case-insensitive)");
    }

    /**
     * Test that setPrintmapping is case-insensitive for "True".
     */
    @Test
    public void testSetPrintmappingTrueMixedCase() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("True");
        task.setPrintmapping(trueFile);

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
            "printMapping should be Configuration.STD_OUT for 'True' filename (case-insensitive)");
    }

    /**
     * Test that setPrintmapping is case-insensitive for "NO".
     */
    @Test
    public void testSetPrintmappingNoUpperCase() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File noFile = new File("NO");
        task.setPrintmapping(noFile);

        assertNull(task.configuration.printMapping,
            "printMapping should be null for 'NO' filename (case-insensitive)");
    }

    /**
     * Test that setPrintmapping default value is null.
     */
    @Test
    public void testSetPrintmappingDefaultIsNull() {
        ProGuardTask task = new ProGuardTask();

        assertNull(task.configuration.printMapping,
            "printMapping should default to null");
    }

    /**
     * Test that setPrintmapping can be called multiple times.
     */
    @Test
    public void testSetPrintmappingMultipleCalls() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File file1 = new File("mapping1.txt");
        task.setPrintmapping(file1);
        assertEquals("mapping1.txt", task.configuration.printMapping.getName());

        File file2 = new File("mapping2.txt");
        task.setPrintmapping(file2);
        assertEquals("mapping2.txt", task.configuration.printMapping.getName(),
            "Second call should overwrite first value");
    }

    /**
     * Test that setPrintmapping on different task instances are independent.
     */
    @Test
    public void testSetPrintmappingIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        Project project = new Project();
        project.init();
        task1.setProject(project);
        task2.setProject(project);

        File file1 = new File("mapping1.txt");
        File file2 = new File("mapping2.txt");

        task1.setPrintmapping(file1);
        task2.setPrintmapping(file2);

        assertEquals("mapping1.txt", task1.configuration.printMapping.getName(),
            "task1 should have its own printMapping value");
        assertEquals("mapping2.txt", task2.configuration.printMapping.getName(),
            "task2 should have its own printMapping value");
    }

    /**
     * Test that setting printMapping doesn't affect other configuration fields.
     */
    @Test
    public void testSetPrintmappingDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;
        File initialPrintSeeds = task.configuration.printSeeds;
        File initialPrintUsage = task.configuration.printUsage;

        File mappingFile = new File("mapping.txt");
        task.setPrintmapping(mappingFile);

        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
            "obfuscate field should not be affected");
        assertEquals(initialPrintSeeds, task.configuration.printSeeds,
            "printSeeds field should not be affected");
        assertEquals(initialPrintUsage, task.configuration.printUsage,
            "printUsage field should not be affected");
    }

    /**
     * Test that the printMapping value persists across other setter calls.
     */
    @Test
    public void testSetPrintmappingPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File mappingFile = new File("mapping.txt");
        task.setPrintmapping(mappingFile);

        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
            "printMapping should remain unchanged after other setters are called");
    }

    /**
     * Test that setPrintmapping with file that has path separator.
     */
    @Test
    public void testSetPrintmappingWithPath() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File mappingFile = new File("output/mapping.txt");
        task.setPrintmapping(mappingFile);

        assertNotNull(task.configuration.printMapping,
            "printMapping should be set");
        assertTrue(task.configuration.printMapping.getName().contains("mapping.txt"),
            "printMapping filename should contain mapping.txt");
    }

    /**
     * Test that setPrintmapping can set to null via "false" then to file.
     */
    @Test
    public void testSetPrintmappingNullThenFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("false");
        task.setPrintmapping(falseFile);
        assertNull(task.configuration.printMapping);

        File mappingFile = new File("mapping.txt");
        task.setPrintmapping(mappingFile);
        assertNotNull(task.configuration.printMapping,
            "printMapping should be set after null");
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    /**
     * Test that setPrintmapping can set to file then to null via "false".
     */
    @Test
    public void testSetPrintmappingFileThenNull() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File mappingFile = new File("mapping.txt");
        task.setPrintmapping(mappingFile);
        assertNotNull(task.configuration.printMapping);

        File falseFile = new File("false");
        task.setPrintmapping(falseFile);
        assertNull(task.configuration.printMapping,
            "printMapping should be null after setting to 'false'");
    }

    /**
     * Test that setPrintmapping with "true" then regular file works.
     */
    @Test
    public void testSetPrintmappingTrueThenFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("true");
        task.setPrintmapping(trueFile);
        assertEquals(Configuration.STD_OUT, task.configuration.printMapping);

        File mappingFile = new File("mapping.txt");
        task.setPrintmapping(mappingFile);
        assertNotEquals(Configuration.STD_OUT, task.configuration.printMapping,
            "printMapping should no longer be STD_OUT");
        assertEquals("mapping.txt", task.configuration.printMapping.getName());
    }

    /**
     * Test that Configuration.STD_OUT has empty name.
     */
    @Test
    public void testSetPrintmappingStdOutIsEmptyFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("true");
        task.setPrintmapping(trueFile);

        assertEquals("", task.configuration.printMapping.getName(),
            "Configuration.STD_OUT should have empty name");
    }

    /**
     * Test that setPrintmapping with file named "False" (capitalized) is case-insensitive.
     */
    @Test
    public void testSetPrintmappingFalseCapitalized() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("False");
        task.setPrintmapping(falseFile);

        assertNull(task.configuration.printMapping,
            "printMapping should be null for 'False' filename");
    }

    /**
     * Test that setPrintmapping with file named "YES" (all caps) works.
     */
    @Test
    public void testSetPrintmappingYesAllCaps() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File yesFile = new File("YES");
        task.setPrintmapping(yesFile);

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
            "printMapping should be Configuration.STD_OUT for 'YES' filename");
    }

    /**
     * Test that setPrintmapping with file named "Off" (capitalized) is case-insensitive.
     */
    @Test
    public void testSetPrintmappingOffCapitalized() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File offFile = new File("Off");
        task.setPrintmapping(offFile);

        assertNull(task.configuration.printMapping,
            "printMapping should be null for 'Off' filename");
    }

    /**
     * Test that setPrintmapping with file named "ON" (all caps) works.
     */
    @Test
    public void testSetPrintmappingOnAllCaps() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File onFile = new File("ON");
        task.setPrintmapping(onFile);

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
            "printMapping should be Configuration.STD_OUT for 'ON' filename");
    }

    /**
     * Test that setPrintmapping works without setting a project (for relative files this may fail).
     */
    @Test
    public void testSetPrintmappingWithoutProject() {
        ProGuardTask task = new ProGuardTask();

        File trueFile = new File("true");
        task.setPrintmapping(trueFile);

        assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
            "Special filenames like 'true' should work without project");
    }

    /**
     * Test that setPrintmapping with different special filenames produce correct results.
     */
    @Test
    public void testSetPrintmappingAllSpecialFilenames() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Test all "false-like" filenames
        String[] falseNames = {"false", "no", "off"};
        for (String name : falseNames) {
            task.setPrintmapping(new File(name));
            assertNull(task.configuration.printMapping,
                "printMapping should be null for '" + name + "'");
        }

        // Test all "true-like" filenames
        String[] trueNames = {"true", "yes", "on"};
        for (String name : trueNames) {
            task.setPrintmapping(new File(name));
            assertEquals(Configuration.STD_OUT, task.configuration.printMapping,
                "printMapping should be STD_OUT for '" + name + "'");
        }
    }

    /**
     * Test that setPrintmapping is independent from other print output setters.
     */
    @Test
    public void testSetPrintmappingIndependentFromOtherPrintOutputs() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File mappingFile = new File("mapping.txt");
        File seedsFile = new File("seeds.txt");
        File usageFile = new File("usage.txt");

        task.setPrintmapping(mappingFile);
        task.setPrintseeds(seedsFile);
        task.setPrintusage(usageFile);

        assertEquals("mapping.txt", task.configuration.printMapping.getName(),
            "printMapping should be mapping.txt");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
            "printSeeds should be seeds.txt");
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
            "printUsage should be usage.txt");
    }
}
