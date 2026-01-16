package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setDump method.
 * Tests that the method properly sets the dump field in the configuration
 * using the optionalFile helper which handles special boolean-like filenames.
 */
public class ProGuardTaskClaude_setDumpTest {

    /**
     * Test that setDump with a regular file sets the dump field.
     */
    @Test
    public void testSetDumpRegularFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dumpFile = new File("dump.txt");
        task.setDump(dumpFile);

        assertNotNull(task.configuration.dump,
            "dump should be set");
        assertEquals("dump.txt", task.configuration.dump.getName(),
            "dump filename should match");
    }

    /**
     * Test that setDump with absolute path file works correctly.
     */
    @Test
    public void testSetDumpAbsoluteFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dumpFile = new File("/tmp/dump.txt");
        task.setDump(dumpFile);

        assertNotNull(task.configuration.dump,
            "dump should be set");
        assertTrue(task.configuration.dump.isAbsolute(),
            "dump should be absolute path");
        assertEquals("/tmp/dump.txt", task.configuration.dump.getPath(),
            "dump path should match");
    }

    /**
     * Test that setDump with "false" filename sets dump to null.
     */
    @Test
    public void testSetDumpFalse() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("false");
        task.setDump(falseFile);

        assertNull(task.configuration.dump,
            "dump should be null for 'false' filename");
    }

    /**
     * Test that setDump with "no" filename sets dump to null.
     */
    @Test
    public void testSetDumpNo() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File noFile = new File("no");
        task.setDump(noFile);

        assertNull(task.configuration.dump,
            "dump should be null for 'no' filename");
    }

    /**
     * Test that setDump with "off" filename sets dump to null.
     */
    @Test
    public void testSetDumpOff() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File offFile = new File("off");
        task.setDump(offFile);

        assertNull(task.configuration.dump,
            "dump should be null for 'off' filename");
    }

    /**
     * Test that setDump with "true" filename sets dump to STD_OUT.
     */
    @Test
    public void testSetDumpTrue() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("true");
        task.setDump(trueFile);

        assertNotNull(task.configuration.dump,
            "dump should not be null for 'true' filename");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
            "dump should be Configuration.STD_OUT for 'true' filename");
    }

    /**
     * Test that setDump with "yes" filename sets dump to STD_OUT.
     */
    @Test
    public void testSetDumpYes() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File yesFile = new File("yes");
        task.setDump(yesFile);

        assertNotNull(task.configuration.dump,
            "dump should not be null for 'yes' filename");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
            "dump should be Configuration.STD_OUT for 'yes' filename");
    }

    /**
     * Test that setDump with "on" filename sets dump to STD_OUT.
     */
    @Test
    public void testSetDumpOn() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File onFile = new File("on");
        task.setDump(onFile);

        assertNotNull(task.configuration.dump,
            "dump should not be null for 'on' filename");
        assertEquals(Configuration.STD_OUT, task.configuration.dump,
            "dump should be Configuration.STD_OUT for 'on' filename");
    }

    /**
     * Test that setDump is case-insensitive for "FALSE".
     */
    @Test
    public void testSetDumpFalseUpperCase() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("FALSE");
        task.setDump(falseFile);

        assertNull(task.configuration.dump,
            "dump should be null for 'FALSE' filename (case-insensitive)");
    }

    /**
     * Test that setDump is case-insensitive for "True".
     */
    @Test
    public void testSetDumpTrueMixedCase() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("True");
        task.setDump(trueFile);

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
            "dump should be Configuration.STD_OUT for 'True' filename (case-insensitive)");
    }

    /**
     * Test that setDump is case-insensitive for "NO".
     */
    @Test
    public void testSetDumpNoUpperCase() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File noFile = new File("NO");
        task.setDump(noFile);

        assertNull(task.configuration.dump,
            "dump should be null for 'NO' filename (case-insensitive)");
    }

    /**
     * Test that setDump default value is null.
     */
    @Test
    public void testSetDumpDefaultIsNull() {
        ProGuardTask task = new ProGuardTask();

        assertNull(task.configuration.dump,
            "dump should default to null");
    }

    /**
     * Test that setDump can be called multiple times.
     */
    @Test
    public void testSetDumpMultipleCalls() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File file1 = new File("dump1.txt");
        task.setDump(file1);
        assertEquals("dump1.txt", task.configuration.dump.getName());

        File file2 = new File("dump2.txt");
        task.setDump(file2);
        assertEquals("dump2.txt", task.configuration.dump.getName(),
            "Second call should overwrite first value");
    }

    /**
     * Test that setDump on different task instances are independent.
     */
    @Test
    public void testSetDumpIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        Project project = new Project();
        project.init();
        task1.setProject(project);
        task2.setProject(project);

        File file1 = new File("dump1.txt");
        File file2 = new File("dump2.txt");

        task1.setDump(file1);
        task2.setDump(file2);

        assertEquals("dump1.txt", task1.configuration.dump.getName(),
            "task1 should have its own dump value");
        assertEquals("dump2.txt", task2.configuration.dump.getName(),
            "task2 should have its own dump value");
    }

    /**
     * Test that setting dump doesn't affect other configuration fields.
     */
    @Test
    public void testSetDumpDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        File initialPrintSeeds = task.configuration.printSeeds;

        File dumpFile = new File("dump.txt");
        task.setDump(dumpFile);

        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialPrintSeeds, task.configuration.printSeeds,
            "printSeeds field should not be affected");
    }

    /**
     * Test that the dump value persists across other setter calls.
     */
    @Test
    public void testSetDumpPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dumpFile = new File("dump.txt");
        task.setDump(dumpFile);

        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        assertEquals("dump.txt", task.configuration.dump.getName(),
            "dump should remain unchanged after other setters are called");
    }

    /**
     * Test that setDump with file that has path separator.
     */
    @Test
    public void testSetDumpWithPath() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dumpFile = new File("output/dump.txt");
        task.setDump(dumpFile);

        assertNotNull(task.configuration.dump,
            "dump should be set");
        assertTrue(task.configuration.dump.getName().contains("dump.txt"),
            "dump filename should contain dump.txt");
    }

    /**
     * Test that setDump can set to null via "false" then to file.
     */
    @Test
    public void testSetDumpNullThenFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("false");
        task.setDump(falseFile);
        assertNull(task.configuration.dump);

        File dumpFile = new File("dump.txt");
        task.setDump(dumpFile);
        assertNotNull(task.configuration.dump,
            "dump should be set after null");
        assertEquals("dump.txt", task.configuration.dump.getName());
    }

    /**
     * Test that setDump can set to file then to null via "false".
     */
    @Test
    public void testSetDumpFileThenNull() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dumpFile = new File("dump.txt");
        task.setDump(dumpFile);
        assertNotNull(task.configuration.dump);

        File falseFile = new File("false");
        task.setDump(falseFile);
        assertNull(task.configuration.dump,
            "dump should be null after setting to 'false'");
    }

    /**
     * Test that setDump with "true" then regular file works.
     */
    @Test
    public void testSetDumpTrueThenFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("true");
        task.setDump(trueFile);
        assertEquals(Configuration.STD_OUT, task.configuration.dump);

        File dumpFile = new File("dump.txt");
        task.setDump(dumpFile);
        assertNotEquals(Configuration.STD_OUT, task.configuration.dump,
            "dump should no longer be STD_OUT");
        assertEquals("dump.txt", task.configuration.dump.getName());
    }

    /**
     * Test that Configuration.STD_OUT has empty name.
     */
    @Test
    public void testSetDumpStdOutIsEmptyFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File trueFile = new File("true");
        task.setDump(trueFile);

        assertEquals("", task.configuration.dump.getName(),
            "Configuration.STD_OUT should have empty name");
    }

    /**
     * Test that setDump with file named "False" (capitalized) is case-insensitive.
     */
    @Test
    public void testSetDumpFalseCapitalized() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File falseFile = new File("False");
        task.setDump(falseFile);

        assertNull(task.configuration.dump,
            "dump should be null for 'False' filename");
    }

    /**
     * Test that setDump with file named "YES" (all caps) works.
     */
    @Test
    public void testSetDumpYesAllCaps() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File yesFile = new File("YES");
        task.setDump(yesFile);

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
            "dump should be Configuration.STD_OUT for 'YES' filename");
    }

    /**
     * Test that setDump with file named "Off" (capitalized) is case-insensitive.
     */
    @Test
    public void testSetDumpOffCapitalized() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File offFile = new File("Off");
        task.setDump(offFile);

        assertNull(task.configuration.dump,
            "dump should be null for 'Off' filename");
    }

    /**
     * Test that setDump with file named "ON" (all caps) works.
     */
    @Test
    public void testSetDumpOnAllCaps() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File onFile = new File("ON");
        task.setDump(onFile);

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
            "dump should be Configuration.STD_OUT for 'ON' filename");
    }

    /**
     * Test that setDump works without setting a project (for relative files this may fail).
     */
    @Test
    public void testSetDumpWithoutProject() {
        ProGuardTask task = new ProGuardTask();

        File trueFile = new File("true");
        task.setDump(trueFile);

        assertEquals(Configuration.STD_OUT, task.configuration.dump,
            "Special filenames like 'true' should work without project");
    }

    /**
     * Test that setDump with different special filenames produce correct results.
     */
    @Test
    public void testSetDumpAllSpecialFilenames() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Test all "false-like" filenames
        String[] falseNames = {"false", "no", "off"};
        for (String name : falseNames) {
            task.setDump(new File(name));
            assertNull(task.configuration.dump,
                "dump should be null for '" + name + "'");
        }

        // Test all "true-like" filenames
        String[] trueNames = {"true", "yes", "on"};
        for (String name : trueNames) {
            task.setDump(new File(name));
            assertEquals(Configuration.STD_OUT, task.configuration.dump,
                "dump should be STD_OUT for '" + name + "'");
        }
    }

    /**
     * Test that setDump is independent from setPrintconfiguration.
     */
    @Test
    public void testSetDumpIndependentFromPrintconfiguration() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File dumpFile = new File("dump.txt");
        File configFile = new File("config.txt");

        task.setDump(dumpFile);
        task.setPrintconfiguration(configFile);

        assertEquals("dump.txt", task.configuration.dump.getName(),
            "dump should be dump.txt");
        assertEquals("config.txt", task.configuration.printConfiguration.getName(),
            "printConfiguration should be config.txt");
    }
}
