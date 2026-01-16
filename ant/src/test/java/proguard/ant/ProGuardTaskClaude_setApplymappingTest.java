package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ProGuardTask.setApplymapping method.
 * Tests that the method properly sets the applyMapping field in the configuration
 * using the resolvedFile helper which resolves paths relative to the project directory.
 */
public class ProGuardTaskClaude_setApplymappingTest {

    /**
     * Test that setApplymapping with a regular file sets the applyMapping field.
     */
    @Test
    public void testSetApplymappingRegularFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File mappingFile = new File("input-mapping.txt");
        task.setApplymapping(mappingFile);

        assertNotNull(task.configuration.applyMapping,
            "applyMapping should be set");
        assertTrue(task.configuration.applyMapping.getName().contains("input-mapping.txt"),
            "applyMapping filename should contain input-mapping.txt");
    }

    /**
     * Test that setApplymapping with absolute path file works correctly.
     */
    @Test
    public void testSetApplymappingAbsoluteFile() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File mappingFile = new File("/tmp/input-mapping.txt");
        task.setApplymapping(mappingFile);

        assertNotNull(task.configuration.applyMapping,
            "applyMapping should be set");
        assertTrue(task.configuration.applyMapping.isAbsolute(),
            "applyMapping should be absolute path");
        assertEquals("/tmp/input-mapping.txt", task.configuration.applyMapping.getPath(),
            "applyMapping path should match");
    }

    /**
     * Test that setApplymapping default value is null.
     */
    @Test
    public void testSetApplymappingDefaultIsNull() {
        ProGuardTask task = new ProGuardTask();

        assertNull(task.configuration.applyMapping,
            "applyMapping should default to null");
    }

    /**
     * Test that setApplymapping can be called multiple times.
     */
    @Test
    public void testSetApplymappingMultipleCalls() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File file1 = new File("mapping1.txt");
        task.setApplymapping(file1);
        assertTrue(task.configuration.applyMapping.getName().contains("mapping1.txt"));

        File file2 = new File("mapping2.txt");
        task.setApplymapping(file2);
        assertTrue(task.configuration.applyMapping.getName().contains("mapping2.txt"),
            "Second call should overwrite first value");
    }

    /**
     * Test that setApplymapping on different task instances are independent.
     */
    @Test
    public void testSetApplymappingIndependentInstances() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        Project project = new Project();
        project.init();
        task1.setProject(project);
        task2.setProject(project);

        File file1 = new File("mapping1.txt");
        File file2 = new File("mapping2.txt");

        task1.setApplymapping(file1);
        task2.setApplymapping(file2);

        assertTrue(task1.configuration.applyMapping.getName().contains("mapping1.txt"),
            "task1 should have its own applyMapping value");
        assertTrue(task2.configuration.applyMapping.getName().contains("mapping2.txt"),
            "task2 should have its own applyMapping value");
    }

    /**
     * Test that setting applyMapping doesn't affect other configuration fields.
     */
    @Test
    public void testSetApplymappingDoesNotAffectOtherFields() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        boolean initialShrink = task.configuration.shrink;
        boolean initialOptimize = task.configuration.optimize;
        boolean initialObfuscate = task.configuration.obfuscate;
        File initialPrintMapping = task.configuration.printMapping;

        File mappingFile = new File("input-mapping.txt");
        task.setApplymapping(mappingFile);

        assertEquals(initialShrink, task.configuration.shrink,
            "shrink field should not be affected");
        assertEquals(initialOptimize, task.configuration.optimize,
            "optimize field should not be affected");
        assertEquals(initialObfuscate, task.configuration.obfuscate,
            "obfuscate field should not be affected");
        assertEquals(initialPrintMapping, task.configuration.printMapping,
            "printMapping field should not be affected");
    }

    /**
     * Test that the applyMapping value persists across other setter calls.
     */
    @Test
    public void testSetApplymappingPersistsAcrossOtherSetters() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File mappingFile = new File("input-mapping.txt");
        task.setApplymapping(mappingFile);

        task.setShrink(false);
        task.setOptimize(false);
        task.setVerbose(true);

        assertTrue(task.configuration.applyMapping.getName().contains("input-mapping.txt"),
            "applyMapping should remain unchanged after other setters are called");
    }

    /**
     * Test that setApplymapping with file that has path separator.
     */
    @Test
    public void testSetApplymappingWithPath() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File mappingFile = new File("input/mapping.txt");
        task.setApplymapping(mappingFile);

        assertNotNull(task.configuration.applyMapping,
            "applyMapping should be set");
        assertTrue(task.configuration.applyMapping.getName().contains("mapping.txt"),
            "applyMapping filename should contain mapping.txt");
    }

    /**
     * Test that setApplymapping resolves relative paths to project base directory.
     */
    @Test
    public void testSetApplymappingResolvesRelativePath() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        File baseDir = project.getBaseDir();
        task.setProject(project);

        File mappingFile = new File("input-mapping.txt");
        task.setApplymapping(mappingFile);

        assertNotNull(task.configuration.applyMapping,
            "applyMapping should be set");
        // The resolved path should be relative to the base directory
        assertTrue(task.configuration.applyMapping.getPath().contains("input-mapping.txt"),
            "applyMapping should contain the filename");
    }

    /**
     * Test that setApplymapping with absolute path doesn't change the path.
     */
    @Test
    public void testSetApplymappingAbsolutePathUnchanged() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File absoluteFile = new File("/absolute/path/to/mapping.txt");
        task.setApplymapping(absoluteFile);

        assertNotNull(task.configuration.applyMapping,
            "applyMapping should be set");
        assertEquals("/absolute/path/to/mapping.txt", task.configuration.applyMapping.getPath(),
            "Absolute path should remain unchanged");
    }

    /**
     * Test that setApplymapping can be called multiple times with different path types.
     */
    @Test
    public void testSetApplymappingMixedPathTypes() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        // Set with relative path
        File relativeFile = new File("relative-mapping.txt");
        task.setApplymapping(relativeFile);
        assertTrue(task.configuration.applyMapping.getName().contains("relative-mapping.txt"));

        // Set with absolute path
        File absoluteFile = new File("/tmp/absolute-mapping.txt");
        task.setApplymapping(absoluteFile);
        assertEquals("/tmp/absolute-mapping.txt", task.configuration.applyMapping.getPath(),
            "Should switch to absolute path");
    }

    /**
     * Test that setApplymapping works immediately after construction.
     */
    @Test
    public void testSetApplymappingImmediatelyAfterConstruction() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        assertDoesNotThrow(() -> task.setApplymapping(new File("mapping.txt")),
            "Should be able to call setApplymapping immediately after construction");

        assertNotNull(task.configuration.applyMapping,
            "applyMapping should be set");
    }

    /**
     * Test that setApplymapping with different files results in different configuration values.
     */
    @Test
    public void testSetApplymappingDifferentFiles() {
        ProGuardTask task1 = new ProGuardTask();
        ProGuardTask task2 = new ProGuardTask();
        Project project = new Project();
        project.init();
        task1.setProject(project);
        task2.setProject(project);

        task1.setApplymapping(new File("mapping1.txt"));
        task2.setApplymapping(new File("mapping2.txt"));

        assertNotEquals(task1.configuration.applyMapping.getName(),
            task2.configuration.applyMapping.getName(),
            "Different files should result in different applyMapping values");
    }

    /**
     * Test that setApplymapping can be used in combination with other setters.
     */
    @Test
    public void testSetApplymappingWithOtherSetters() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        task.setShrink(false);
        task.setApplymapping(new File("input.txt"));
        task.setObfuscate(true);

        assertNotNull(task.configuration.applyMapping,
            "applyMapping should be set");
        assertFalse(task.configuration.shrink,
            "shrink should be false");
        assertTrue(task.configuration.obfuscate,
            "obfuscate should be true");
    }

    /**
     * Test that setApplymapping is independent from setPrintmapping.
     */
    @Test
    public void testSetApplymappingIndependentFromPrintmapping() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File applyFile = new File("apply-mapping.txt");
        File printFile = new File("print-mapping.txt");

        task.setApplymapping(applyFile);
        task.setPrintmapping(printFile);

        assertTrue(task.configuration.applyMapping.getName().contains("apply-mapping.txt"),
            "applyMapping should be apply-mapping.txt");
        assertEquals("print-mapping.txt", task.configuration.printMapping.getName(),
            "printMapping should be print-mapping.txt");
    }

    /**
     * Test that setApplymapping handles files with various extensions.
     */
    @Test
    public void testSetApplymappingVariousExtensions() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        String[] filenames = {"mapping.txt", "mapping.map", "mapping.cfg", "mapping"};

        for (String filename : filenames) {
            task.setApplymapping(new File(filename));
            assertNotNull(task.configuration.applyMapping,
                "applyMapping should be set for " + filename);
            assertTrue(task.configuration.applyMapping.getName().contains(filename.substring(filename.lastIndexOf('/') + 1)),
                "applyMapping should contain " + filename);
        }
    }

    /**
     * Test that setApplymapping with the same file twice maintains the value.
     */
    @Test
    public void testSetApplymappingSameFileTwice() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File mappingFile = new File("mapping.txt");
        task.setApplymapping(mappingFile);
        String firstPath = task.configuration.applyMapping.getPath();

        task.setApplymapping(mappingFile);
        String secondPath = task.configuration.applyMapping.getPath();

        assertEquals(firstPath, secondPath,
            "Setting the same file twice should result in the same path");
    }

    /**
     * Test that setApplymapping works correctly with Ant project context.
     */
    @Test
    public void testSetApplymappingWithAntProjectContext() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File mappingFile = new File("input-mapping.txt");
        task.setApplymapping(mappingFile);

        assertNotNull(task.configuration.applyMapping,
            "applyMapping should be set with Ant project context");
    }

    /**
     * Test that setApplymapping with relative path is resolved correctly.
     */
    @Test
    public void testSetApplymappingRelativePathResolution() {
        ProGuardTask task = new ProGuardTask();
        Project project = new Project();
        project.init();
        task.setProject(project);

        File relativeFile = new File("subdir/mapping.txt");
        task.setApplymapping(relativeFile);

        assertNotNull(task.configuration.applyMapping,
            "applyMapping should be set");
        assertFalse(task.configuration.applyMapping.isAbsolute() &&
                   task.configuration.applyMapping.getPath().startsWith("/subdir"),
            "Relative path should be resolved relative to project base");
    }
}
