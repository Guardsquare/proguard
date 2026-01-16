package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setPrintusage method.
 * Tests that the method properly sets the printUsage field in the configuration
 * using the optionalFile helper which handles special boolean-like filenames.
 */
public class ProGuardTaskClaude_setPrintusageTest {

    /**
     * Test that setPrintusage with a regular file sets the printUsage field.
     */
    @Test
    public void testSetPrintusageRegularFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File usageFile = new File("usage.txt");
        task.setPrintusage(usageFile);

        assertNotNull(task.configuration.printUsage,
            "printUsage should be set");
        assertEquals("usage.txt", task.configuration.printUsage.getName(),
            "printUsage filename should match");
    }

    /**
     * Test that setPrintusage with absolute path file works correctly.
     */
    @Test
    public void testSetPrintusageAbsoluteFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File usageFile = new File("/tmp/usage.txt");
        task.setPrintusage(usageFile);

        assertNotNull(task.configuration.printUsage,
            "printUsage should be set");
        assertTrue(task.configuration.printUsage.isAbsolute(),
            "printUsage should be absolute path");
        assertEquals("/tmp/usage.txt", task.configuration.printUsage.getPath(),
            "printUsage path should match");
    }

    /**
     * Test that setPrintusage with "false" filename sets printUsage to null.
     */
    @Test
    public void testSetPrintusageFalse() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("false");
        task.setPrintusage(falseFile);

        assertNull(task.configuration.printUsage,
            "printUsage should be null for 'false' filename");
    }

    /**
     * Test that setPrintusage with "no" filename sets printUsage to null.
     */
    @Test
    public void testSetPrintusageNo() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File noFile = new File("no");
        task.setPrintusage(noFile);

        assertNull(task.configuration.printUsage,
            "printUsage should be null for 'no' filename");
    }

    /**
     * Test that setPrintusage with "off" filename sets printUsage to null.
     */
    @Test
    public void testSetPrintusageOff() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File offFile = new File("off");
        task.setPrintusage(offFile);

        assertNull(task.configuration.printUsage,
            "printUsage should be null for 'off' filename");
    }

    /**
     * Test that setPrintusage with "true" filename sets printUsage to STD_OUT.
     */
    @Test
    public void testSetPrintusageTrue() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("true");
        task.setPrintusage(trueFile);

        assertNotNull(task.configuration.printUsage,
            "printUsage should not be null for 'true' filename");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
            "printUsage should be Configuration.STD_OUT for 'true' filename");
    }

    /**
     * Test that setPrintusage with "yes" filename sets printUsage to STD_OUT.
     */
    @Test
    public void testSetPrintusageYes() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File yesFile = new File("yes");
        task.setPrintusage(yesFile);

        assertNotNull(task.configuration.printUsage,
            "printUsage should not be null for 'yes' filename");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
            "printUsage should be Configuration.STD_OUT for 'yes' filename");
    }

    /**
     * Test that setPrintusage with "on" filename sets printUsage to STD_OUT.
     */
    @Test
    public void testSetPrintusageOn() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File onFile = new File("on");
        task.setPrintusage(onFile);

        assertNotNull(task.configuration.printUsage,
            "printUsage should not be null for 'on' filename");
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
            "printUsage should be Configuration.STD_OUT for 'on' filename");
    }

    /**
     * Test that setPrintusage is case-insensitive for "FALSE".
     */
    @Test
    public void testSetPrintusageFalseUpperCase() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("FALSE");
        task.setPrintusage(falseFile);

        assertNull(task.configuration.printUsage,
            "printUsage should be null for 'FALSE' filename (case-insensitive)");
    }

    /**
     * Test that setPrintusage is case-insensitive for "True".
     */
    @Test
    public void testSetPrintusageTrueMixedCase() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("True");
        task.setPrintusage(trueFile);

        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
            "printUsage should be Configuration.STD_OUT for 'True' filename (case-insensitive)");
    }

    /**
     * Test that setPrintusage is case-insensitive for "NO".
     */
    @Test
    public void testSetPrintusageNoUpperCase() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File noFile = new File("NO");
        task.setPrintusage(noFile);

        assertNull(task.configuration.printUsage,
            "printUsage should be null for 'NO' filename (case-insensitive)");
    }

    /**
     * Test that setPrintusage default value is null.
     */
    @Test
    public void testSetPrintusageDefaultIsNull() {
        ProGuardTask task = new ProGuardTask();

        assertNull(task.configuration.printUsage,
            "printUsage should default to null");
    }

    /**
     * Test that setPrintusage can be called multiple times.
     */
    @Test
    public void testSetPrintusageMultipleCalls() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File file1 = new File("usage1.txt");
        task.setPrintusage(file1);
        assertEquals("usage1.txt", task.configuration.printUsage.getName());

        File file2 = new File("usage2.txt");
        task.setPrintusage(file2);
        assertEquals("usage2.txt", task.configuration.printUsage.getName(),
            "Second call should overwrite first value");
    }

    /**
     * Test that setPrintusage on different task instances are independent.
     */
    @Test
    public void testSetPrintusageIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        Project project = new Project();
        project.init();
        task1.setProject(project);
        task2.setProject(project);

        File file1 = new File("usage1.txt");
        File file2 = new File("usage2.txt");

        task1.setPrintusage(file1);
        task2.setPrintusage(file2);

        assertEquals("usage1.txt", task1.configuration.printUsage.getName(),
            "task1 should have its own printUsage value");
        assertEquals("usage2.txt", task2.configuration.printUsage.getName(),
            "task2 should have its own printUsage value");
    }

    /**
     * Test that setting printUsage doesn't affect other configuration fields.
     */
    @Test
    public void testSetPrintusageDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        File initialPrintSeeds = task.configuration.printSeeds;

        File usageFile = new File("usage.txt");
        task.setPrintusage(usageFile);

        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialPrintSeeds, task.configuration.printSeeds,
            "printSeeds field should not be affected");
    }

    /**
     * Test that the printUsage value persists across other setter calls.
     */
    @Test
    public void testSetPrintusagePersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File usageFile = new File("usage.txt");
        task.setPrintusage(usageFile);

        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        assertEquals("usage.txt", task.configuration.printUsage.getName(),
            "printUsage should remain unchanged after other setters are called");
    }

    /**
     * Test that setPrintusage with file that has path separator.
     */
    @Test
    public void testSetPrintusageWithPath() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File usageFile = new File("output/usage.txt");
        task.setPrintusage(usageFile);

        assertNotNull(task.configuration.printUsage,
            "printUsage should be set");
        assertTrue(task.configuration.printUsage.getName().contains("usage.txt"),
            "printUsage filename should contain usage.txt");
    }

    /**
     * Test that setPrintusage can set to null via "false" then to file.
     */
    @Test
    public void testSetPrintusageNullThenFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("false");
        task.setPrintusage(falseFile);
        assertNull(task.configuration.printUsage);

        File usageFile = new File("usage.txt");
        task.setPrintusage(usageFile);
        assertNotNull(task.configuration.printUsage,
            "printUsage should be set after null");
        assertEquals("usage.txt", task.configuration.printUsage.getName());
    }

    /**
     * Test that setPrintusage can set to file then to null via "false".
     */
    @Test
    public void testSetPrintusageFileThenNull() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File usageFile = new File("usage.txt");
        task.setPrintusage(usageFile);
        assertNotNull(task.configuration.printUsage);

        File falseFile = new File("false");
        task.setPrintusage(falseFile);
        assertNull(task.configuration.printUsage,
            "printUsage should be null after setting to 'false'");
    }

    /**
     * Test that setPrintusage with "true" then regular file works.
     */
    @Test
    public void testSetPrintusageTrueThenFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("true");
        task.setPrintusage(trueFile);
        assertEquals(Configuration.STD_OUT, task.configuration.printUsage);

        File usageFile = new File("usage.txt");
        task.setPrintusage(usageFile);
        assertNotEquals(Configuration.STD_OUT, task.configuration.printUsage,
            "printUsage should no longer be STD_OUT");
        assertEquals("usage.txt", task.configuration.printUsage.getName());
    }

    /**
     * Test that Configuration.STD_OUT has empty name.
     */
    @Test
    public void testSetPrintusageStdOutIsEmptyFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("true");
        task.setPrintusage(trueFile);

        assertEquals("", task.configuration.printUsage.getName(),
            "Configuration.STD_OUT should have empty name");
    }

    /**
     * Test that setPrintusage with file named "False" (capitalized) is case-insensitive.
     */
    @Test
    public void testSetPrintusageFalseCapitalized() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("False");
        task.setPrintusage(falseFile);

        assertNull(task.configuration.printUsage,
            "printUsage should be null for 'False' filename");
    }

    /**
     * Test that setPrintusage with file named "YES" (all caps) works.
     */
    @Test
    public void testSetPrintusageYesAllCaps() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File yesFile = new File("YES");
        task.setPrintusage(yesFile);

        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
            "printUsage should be Configuration.STD_OUT for 'YES' filename");
    }

    /**
     * Test that setPrintusage with file named "Off" (capitalized) is case-insensitive.
     */
    @Test
    public void testSetPrintusageOffCapitalized() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File offFile = new File("Off");
        task.setPrintusage(offFile);

        assertNull(task.configuration.printUsage,
            "printUsage should be null for 'Off' filename");
    }

    /**
     * Test that setPrintusage with file named "ON" (all caps) works.
     */
    @Test
    public void testSetPrintusageOnAllCaps() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File onFile = new File("ON");
        task.setPrintusage(onFile);

        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
            "printUsage should be Configuration.STD_OUT for 'ON' filename");
    }

    /**
     * Test that setPrintusage works without setting a project (for relative files this may fail).
     */
    @Test
    public void testSetPrintusageWithoutProject() {
        ProGuardTask task = new ProGuardTask();

        File trueFile = new File("true");
        task.setPrintusage(trueFile);

        assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
            "Special filenames like 'true' should work without project");
    }

    /**
     * Test that setPrintusage with different special filenames produce correct results.
     */
    @Test
    public void testSetPrintusageAllSpecialFilenames() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Test all "false-like" filenames
        String[] falseNames = {"false", "no", "off"};
        for (String name : falseNames) {
            task.setPrintusage(new File(name));
            assertNull(task.configuration.printUsage,
                "printUsage should be null for '" + name + "'");
        }

        // Test all "true-like" filenames
        String[] trueNames = {"true", "yes", "on"};
        for (String name : trueNames) {
            task.setPrintusage(new File(name));
            assertEquals(Configuration.STD_OUT, task.configuration.printUsage,
                "printUsage should be STD_OUT for '" + name + "'");
        }
    }

    /**
     * Test that setPrintusage is independent from setPrintseeds.
     */
    @Test
    public void testSetPrintusageIndependentFromPrintseeds() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File usageFile = new File("usage.txt");
        File seedsFile = new File("seeds.txt");

        task.setPrintusage(usageFile);
        task.setPrintseeds(seedsFile);

        assertEquals("usage.txt", task.configuration.printUsage.getName(),
            "printUsage should be usage.txt");
        assertEquals("seeds.txt", task.configuration.printSeeds.getName(),
            "printSeeds should be seeds.txt");
    }
}
