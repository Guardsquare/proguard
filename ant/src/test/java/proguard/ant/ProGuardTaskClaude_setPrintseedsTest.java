package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setPrintseeds method.
 * Tests that the method properly sets the printSeeds field in the configuration
 * using the optionalFile helper which handles special boolean-like filenames.
 */
public class ProGuardTaskClaude_setPrintseedsTest {

    /**
     * Test that setPrintseeds with a regular file sets the printSeeds field.
     */
    @Test
    public void testSetPrintseedsRegularFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File seedsFile = new File("seeds.txt");
        task.setPrintseeds(seedsFile);

        assertNotNull(task.configuration.printSeeds,
            "printSeeds should be set");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
            "printSeeds filename should match");
    }

    /**
     * Test that setPrintseeds with absolute path file works correctly.
     */
    @Test
    public void testSetPrintseedsAbsoluteFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File seedsFile = new File("/tmp/seeds.txt");
        task.setPrintseeds(seedsFile);

        assertNotNull(task.configuration.printSeeds,
            "printSeeds should be set");
        assertTrue(task.configuration.printSeeds.isAbsolute(),
            "printSeeds should be absolute path");
        assertEquals("/tmp/seeds.txt", task.configuration.printSeeds.getPath(),
            "printSeeds path should match");
    }

    /**
     * Test that setPrintseeds with "false" filename sets printSeeds to null.
     */
    @Test
    public void testSetPrintseedsFalse() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("false");
        task.setPrintseeds(falseFile);

        assertNull(task.configuration.printSeeds,
            "printSeeds should be null for 'false' filename");
    }

    /**
     * Test that setPrintseeds with "no" filename sets printSeeds to null.
     */
    @Test
    public void testSetPrintseedsNo() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File noFile = new File("no");
        task.setPrintseeds(noFile);

        assertNull(task.configuration.printSeeds,
            "printSeeds should be null for 'no' filename");
    }

    /**
     * Test that setPrintseeds with "off" filename sets printSeeds to null.
     */
    @Test
    public void testSetPrintseedsOff() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File offFile = new File("off");
        task.setPrintseeds(offFile);

        assertNull(task.configuration.printSeeds,
            "printSeeds should be null for 'off' filename");
    }

    /**
     * Test that setPrintseeds with "true" filename sets printSeeds to STD_OUT.
     */
    @Test
    public void testSetPrintseedsTrue() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("true");
        task.setPrintseeds(trueFile);

        assertNotNull(task.configuration.printSeeds,
            "printSeeds should not be null for 'true' filename");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
            "printSeeds should be Configuration.STD_OUT for 'true' filename");
    }

    /**
     * Test that setPrintseeds with "yes" filename sets printSeeds to STD_OUT.
     */
    @Test
    public void testSetPrintseedsYes() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File yesFile = new File("yes");
        task.setPrintseeds(yesFile);

        assertNotNull(task.configuration.printSeeds,
            "printSeeds should not be null for 'yes' filename");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
            "printSeeds should be Configuration.STD_OUT for 'yes' filename");
    }

    /**
     * Test that setPrintseeds with "on" filename sets printSeeds to STD_OUT.
     */
    @Test
    public void testSetPrintseedsOn() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File onFile = new File("on");
        task.setPrintseeds(onFile);

        assertNotNull(task.configuration.printSeeds,
            "printSeeds should not be null for 'on' filename");
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
            "printSeeds should be Configuration.STD_OUT for 'on' filename");
    }

    /**
     * Test that setPrintseeds is case-insensitive for "FALSE".
     */
    @Test
    public void testSetPrintseedsFalseUpperCase() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("FALSE");
        task.setPrintseeds(falseFile);

        assertNull(task.configuration.printSeeds,
            "printSeeds should be null for 'FALSE' filename (case-insensitive)");
    }

    /**
     * Test that setPrintseeds is case-insensitive for "True".
     */
    @Test
    public void testSetPrintseedsTrueMixedCase() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("True");
        task.setPrintseeds(trueFile);

        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
            "printSeeds should be Configuration.STD_OUT for 'True' filename (case-insensitive)");
    }

    /**
     * Test that setPrintseeds is case-insensitive for "NO".
     */
    @Test
    public void testSetPrintseedsNoUpperCase() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File noFile = new File("NO");
        task.setPrintseeds(noFile);

        assertNull(task.configuration.printSeeds,
            "printSeeds should be null for 'NO' filename (case-insensitive)");
    }

    /**
     * Test that setPrintseeds default value is null.
     */
    @Test
    public void testSetPrintseedsDefaultIsNull() {
        ProGuardTask task = new ProGuardTask();

        assertNull(task.configuration.printSeeds,
            "printSeeds should default to null");
    }

    /**
     * Test that setPrintseeds can be called multiple times.
     */
    @Test
    public void testSetPrintseedsMultipleCalls() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File file1 = new File("seeds1.txt");
        task.setPrintseeds(file1);
        assertEquals("seeds1.txt", task.configuration.printSeeds.getName());

        File file2 = new File("seeds2.txt");
        task.setPrintseeds(file2);
        assertEquals("seeds2.txt", task.configuration.printSeeds.getName(),
            "Second call should overwrite first value");
    }

    /**
     * Test that setPrintseeds on different task instances are independent.
     */
    @Test
    public void testSetPrintseedsIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        Project project = new Project();
        project.init();
        task1.setProject(project);
        task2.setProject(project);

        File file1 = new File("seeds1.txt");
        File file2 = new File("seeds2.txt");

        task1.setPrintseeds(file1);
        task2.setPrintseeds(file2);

        assertEquals("seeds1.txt", task1.configuration.printSeeds.getName(),
            "task1 should have its own printSeeds value");
        assertEquals("seeds2.txt", task2.configuration.printSeeds.getName(),
            "task2 should have its own printSeeds value");
    }

    /**
     * Test that setting printSeeds doesn't affect other configuration fields.
     */
    @Test
    public void testSetPrintseedsDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        File initialPrintUsage = task.configuration.printUsage;

        File seedsFile = new File("seeds.txt");
        task.setPrintseeds(seedsFile);

        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialPrintUsage, task.configuration.printUsage,
            "printUsage field should not be affected");
    }

    /**
     * Test that the printSeeds value persists across other setter calls.
     */
    @Test
    public void testSetPrintseedsPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File seedsFile = new File("seeds.txt");
        task.setPrintseeds(seedsFile);

        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
            "printSeeds should remain unchanged after other setters are called");
    }

    /**
     * Test that setPrintseeds with file that has path separator.
     */
    @Test
    public void testSetPrintseedsWithPath() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File seedsFile = new File("output/seeds.txt");
        task.setPrintseeds(seedsFile);

        assertNotNull(task.configuration.printSeeds,
            "printSeeds should be set");
        assertTrue(task.configuration.printSeeds.getName().contains("seeds.txt"),
            "printSeeds filename should contain seeds.txt");
    }

    /**
     * Test that setPrintseeds can set to null via "false" then to file.
     */
    @Test
    public void testSetPrintseedsNullThenFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("false");
        task.setPrintseeds(falseFile);
        assertNull(task.configuration.printSeeds);

        File seedsFile = new File("seeds.txt");
        task.setPrintseeds(seedsFile);
        assertNotNull(task.configuration.printSeeds,
            "printSeeds should be set after null");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName());
    }

    /**
     * Test that setPrintseeds can set to file then to null via "false".
     */
    @Test
    public void testSetPrintseedsFileThenNull() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File seedsFile = new File("seeds.txt");
        task.setPrintseeds(seedsFile);
        assertNotNull(task.configuration.printSeeds);

        File falseFile = new File("false");
        task.setPrintseeds(falseFile);
        assertNull(task.configuration.printSeeds,
            "printSeeds should be null after setting to 'false'");
    }

    /**
     * Test that setPrintseeds with "true" then regular file works.
     */
    @Test
    public void testSetPrintseedsTrueThenFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("true");
        task.setPrintseeds(trueFile);
        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds);

        File seedsFile = new File("seeds.txt");
        task.setPrintseeds(seedsFile);
        assertNotEquals(Configuration.STD_OUT, task.configuration.printSeeds,
            "printSeeds should no longer be STD_OUT");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName());
    }

    /**
     * Test that Configuration.STD_OUT has empty name.
     */
    @Test
    public void testSetPrintseedsStdOutIsEmptyFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("true");
        task.setPrintseeds(trueFile);

        assertEquals("", task.configuration.printSeeds.getName(),
            "Configuration.STD_OUT should have empty name");
    }

    /**
     * Test that setPrintseeds with file named "False" (capitalized) is case-insensitive.
     */
    @Test
    public void testSetPrintseedsFalseCapitalized() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("False");
        task.setPrintseeds(falseFile);

        assertNull(task.configuration.printSeeds,
            "printSeeds should be null for 'False' filename");
    }

    /**
     * Test that setPrintseeds with file named "YES" (all caps) works.
     */
    @Test
    public void testSetPrintseedsYesAllCaps() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File yesFile = new File("YES");
        task.setPrintseeds(yesFile);

        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
            "printSeeds should be Configuration.STD_OUT for 'YES' filename");
    }

    /**
     * Test that setPrintseeds with file named "Off" (capitalized) is case-insensitive.
     */
    @Test
    public void testSetPrintseedsOffCapitalized() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File offFile = new File("Off");
        task.setPrintseeds(offFile);

        assertNull(task.configuration.printSeeds,
            "printSeeds should be null for 'Off' filename");
    }

    /**
     * Test that setPrintseeds with file named "ON" (all caps) works.
     */
    @Test
    public void testSetPrintseedsOnAllCaps() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File onFile = new File("ON");
        task.setPrintseeds(onFile);

        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
            "printSeeds should be Configuration.STD_OUT for 'ON' filename");
    }

    /**
     * Test that setPrintseeds works without setting a project (for relative files this may fail).
     */
    @Test
    public void testSetPrintseedsWithoutProject() {
        ProGuardTask task = new ProGuardTask();

        File trueFile = new File("true");
        task.setPrintseeds(trueFile);

        assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
            "Special filenames like 'true' should work without project");
    }

    /**
     * Test that setPrintseeds with different special filenames produce correct results.
     */
    @Test
    public void testSetPrintseedsAllSpecialFilenames() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Test all "false-like" filenames
        String[] falseNames = {"false", "no", "off"};
        for (String name : falseNames) {
            task.setPrintseeds(new File(name));
            assertNull(task.configuration.printSeeds,
                "printSeeds should be null for '" + name + "'");
        }

        // Test all "true-like" filenames
        String[] trueNames = {"true", "yes", "on"};
        for (String name : trueNames) {
            task.setPrintseeds(new File(name));
            assertEquals(Configuration.STD_OUT, task.configuration.printSeeds,
                "printSeeds should be STD_OUT for '" + name + "'");
        }
    }
}
