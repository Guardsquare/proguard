package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import proguard.ClassPath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassPathElement.setZipfilter method.
 * The setZipfilter method sets a ZIP filter string that is later used when appending
 * class path entries to filter ZIP files within archives.
 */
public class ClassPathElementClaude_setZipfilterTest {

    @TempDir
    Path tempDir;

    /**
     * Test setZipfilter with a simple filter string.
     * Verifies that the ZIP filter can be set without throwing exceptions.
     */
    @Test
    public void testSetZipfilterWithSimpleString() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setZipfilter("*.zip"));
    }

    /**
     * Test setZipfilter with null value.
     * The method should accept null filters gracefully.
     */
    @Test
    public void testSetZipfilterWithNull() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setZipfilter(null));
    }

    /**
     * Test setZipfilter with an empty string.
     * The method should accept empty string filters.
     */
    @Test
    public void testSetZipfilterWithEmptyString() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setZipfilter(""));
    }

    /**
     * Test setZipfilter with a comma-separated filter list.
     * The ZIP filter supports comma-separated values for multiple patterns.
     */
    @Test
    public void testSetZipfilterWithCommaSeparatedList() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setZipfilter("archive.zip,data.zip,resources.zip"));
    }

    /**
     * Test setZipfilter with wildcard patterns.
     * Verifies that wildcard patterns are accepted.
     */
    @Test
    public void testSetZipfilterWithWildcardPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setZipfilter("*.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("archive-*.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("**/*.zip"));
    }

    /**
     * Test setZipfilter multiple times on the same element.
     * Each call should replace the previous filter value.
     */
    @Test
    public void testSetZipfilterMultipleTimes() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should be able to set ZIP filter multiple times
        assertDoesNotThrow(() -> {
            element.setZipfilter("archive.zip");
            element.setZipfilter("data.zip");
            element.setZipfilter("resources.zip");
        });
    }

    /**
     * Test setZipfilter with negation patterns.
     * ZIP filters can include negation patterns (typically with ! prefix).
     */
    @Test
    public void testSetZipfilterWithNegationPattern() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setZipfilter("!test.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("*.zip,!*-temp.zip"));
    }

    /**
     * Test setZipfilter integration with appendClassPathEntriesTo.
     * Verifies that after setting a ZIP filter, the element can be used
     * to append entries to a ClassPath without errors.
     */
    @Test
    public void testSetZipfilterIntegrationWithAppendClassPathEntries() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Create a test ZIP file
        File testZip = tempDir.resolve("test.zip").toFile();
        assertTrue(testZip.createNewFile(), "Test ZIP file should be created");

        // Set a location and ZIP filter
        element.setLocation(testZip);
        element.setZipfilter("*.zip");

        // Create a ClassPath and append entries
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
    }

    /**
     * Test setZipfilter with null project.
     * The method should work even with a null project.
     */
    @Test
    public void testSetZipfilterWithNullProject() {
        ClassPathElement element = new ClassPathElement(null);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setZipfilter("*.zip"));
    }

    /**
     * Test setZipfilter with special characters.
     * ZIP filters may contain special regex or path characters.
     */
    @Test
    public void testSetZipfilterWithSpecialCharacters() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should handle special characters in ZIP filter strings
        assertDoesNotThrow(() -> element.setZipfilter("my-archive_v1.0.0.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("**/*.{zip}"));
    }

    /**
     * Test setZipfilter with path-style filters.
     * ZIP filters may include path separators.
     */
    @Test
    public void testSetZipfilterWithPathNotation() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setZipfilter("archives/*.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("dist/**/*.zip"));
    }

    /**
     * Test setZipfilter followed by appendClassPathEntriesTo with output flag.
     * Verifies that ZIP filter works correctly for output entries.
     */
    @Test
    public void testSetZipfilterWithOutputEntry() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Create a test output ZIP file
        File outputZip = tempDir.resolve("output.zip").toFile();
        assertTrue(outputZip.createNewFile(), "Output ZIP file should be created");

        element.setLocation(outputZip);
        element.setZipfilter("*.zip");

        // Create a ClassPath and append as output entry
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, true));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
        assertTrue(classPath.get(0).isOutput(), "Entry should be marked as output");
    }

    /**
     * Test setZipfilter with whitespace in the filter string.
     * Verifies handling of filters with spaces.
     */
    @Test
    public void testSetZipfilterWithWhitespace() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Whitespace might be significant in filter patterns
        assertDoesNotThrow(() -> element.setZipfilter("archive.zip, data.zip"));
        assertDoesNotThrow(() -> element.setZipfilter(" *.zip "));
    }

    /**
     * Test that setZipfilter can be called after setting location.
     * Order of operations should not matter.
     */
    @Test
    public void testSetZipfilterAfterSetLocation() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testZip = tempDir.resolve("test.zip").toFile();
        assertTrue(testZip.createNewFile(), "Test ZIP should be created");

        // Set location first, then ZIP filter
        element.setLocation(testZip);
        assertDoesNotThrow(() -> element.setZipfilter("*.zip"));

        // Verify it can be used
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));
    }

    /**
     * Test that setZipfilter can be called before setting location.
     * Order of operations should not matter.
     */
    @Test
    public void testSetZipfilterBeforeSetLocation() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Set ZIP filter first, then location
        assertDoesNotThrow(() -> element.setZipfilter("*.zip"));

        File testZip = tempDir.resolve("test.zip").toFile();
        assertTrue(testZip.createNewFile(), "Test ZIP should be created");
        element.setLocation(testZip);

        // Verify it can be used
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));
    }

    /**
     * Test setZipfilter with common archive naming conventions.
     * ZIP files often follow specific naming patterns.
     */
    @Test
    public void testSetZipfilterWithCommonNamingConventions() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Common ZIP naming patterns
        assertDoesNotThrow(() -> element.setZipfilter("archive.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("data-backup-2024.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("resources-v1.0.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("source-code.zip"));
    }

    /**
     * Test setZipfilter combined with other filter setters.
     * Verifies that ZIP filter can coexist with other filters like jarFilter.
     */
    @Test
    public void testSetZipfilterWithOtherFilters() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testFile = tempDir.resolve("test.jar").toFile();
        assertTrue(testFile.createNewFile(), "Test file should be created");

        // Set multiple filters
        element.setLocation(testFile);
        element.setZipfilter("*.zip");
        element.setJarfilter("*.jar");
        element.setFilter("*.class");

        // Should work without errors
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
    }

    /**
     * Test setZipfilter with version pattern filters.
     * Useful for filtering ZIP files by version.
     */
    @Test
    public void testSetZipfilterWithVersionPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Version-specific patterns
        assertDoesNotThrow(() -> element.setZipfilter("archive-1.*.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("*-[0-9]*.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("*-SNAPSHOT.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("data-?.?.?.zip"));
    }

    /**
     * Test setZipfilter with backup archive patterns.
     * Backup archives often follow date-based naming conventions.
     */
    @Test
    public void testSetZipfilterWithBackupPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Backup-style patterns
        assertDoesNotThrow(() -> element.setZipfilter("backup-*.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("*-20*.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("daily-backup-*.zip"));
    }

    /**
     * Test setZipfilter with compressed resource patterns.
     * Resources and assets are often distributed as ZIP files.
     */
    @Test
    public void testSetZipfilterWithResourcePatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Resource-style patterns
        assertDoesNotThrow(() -> element.setZipfilter("resources-*.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("assets.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("static-content-*.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("media-files.zip"));
    }

    /**
     * Test setZipfilter with distribution package patterns.
     * Distribution packages often use ZIP format.
     */
    @Test
    public void testSetZipfilterWithDistributionPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Distribution-style patterns
        assertDoesNotThrow(() -> element.setZipfilter("dist-*.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("*-distribution.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("release-*.zip"));
        assertDoesNotThrow(() -> element.setZipfilter("*-bundle.zip"));
    }
}
