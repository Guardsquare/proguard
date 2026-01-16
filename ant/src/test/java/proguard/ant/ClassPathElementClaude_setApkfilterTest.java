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
 * Test class for ClassPathElement.setApkfilter method.
 * The setApkfilter method sets an APK filter string that is later used when appending
 * class path entries to filter APK files within archives.
 */
public class ClassPathElementClaude_setApkfilterTest {

    @TempDir
    Path tempDir;

    /**
     * Test setApkfilter with a simple filter string.
     * Verifies that the APK filter can be set without throwing exceptions.
     */
    @Test
    public void testSetApkfilterWithSimpleString() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setApkfilter("*.apk"));
    }

    /**
     * Test setApkfilter with null value.
     * The method should accept null filters gracefully.
     */
    @Test
    public void testSetApkfilterWithNull() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setApkfilter(null));
    }

    /**
     * Test setApkfilter with an empty string.
     * The method should accept empty string filters.
     */
    @Test
    public void testSetApkfilterWithEmptyString() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setApkfilter(""));
    }

    /**
     * Test setApkfilter with a comma-separated filter list.
     * The APK filter supports comma-separated values for multiple patterns.
     */
    @Test
    public void testSetApkfilterWithCommaSeparatedList() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setApkfilter("app.apk,base.apk,feature.apk"));
    }

    /**
     * Test setApkfilter with wildcard patterns.
     * Verifies that wildcard patterns are accepted.
     */
    @Test
    public void testSetApkfilterWithWildcardPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setApkfilter("*.apk"));
        assertDoesNotThrow(() -> element.setApkfilter("app-*.apk"));
        assertDoesNotThrow(() -> element.setApkfilter("**/*.apk"));
    }

    /**
     * Test setApkfilter multiple times on the same element.
     * Each call should replace the previous filter value.
     */
    @Test
    public void testSetApkfilterMultipleTimes() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should be able to set APK filter multiple times
        assertDoesNotThrow(() -> {
            element.setApkfilter("base.apk");
            element.setApkfilter("feature.apk");
            element.setApkfilter("app.apk");
        });
    }

    /**
     * Test setApkfilter with negation patterns.
     * APK filters can include negation patterns (typically with ! prefix).
     */
    @Test
    public void testSetApkfilterWithNegationPattern() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setApkfilter("!test.apk"));
        assertDoesNotThrow(() -> element.setApkfilter("*.apk,!*-debug.apk"));
    }

    /**
     * Test setApkfilter integration with appendClassPathEntriesTo.
     * Verifies that after setting an APK filter, the element can be used
     * to append entries to a ClassPath without errors.
     */
    @Test
    public void testSetApkfilterIntegrationWithAppendClassPathEntries() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Create a test APK file
        File testApk = tempDir.resolve("test.apk").toFile();
        assertTrue(testApk.createNewFile(), "Test APK file should be created");

        // Set a location and APK filter
        element.setLocation(testApk);
        element.setApkfilter("*.apk");

        // Create a ClassPath and append entries
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
    }

    /**
     * Test setApkfilter with null project.
     * The method should work even with a null project.
     */
    @Test
    public void testSetApkfilterWithNullProject() {
        ClassPathElement element = new ClassPathElement(null);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setApkfilter("*.apk"));
    }

    /**
     * Test setApkfilter with special characters.
     * APK filters may contain special regex or path characters.
     */
    @Test
    public void testSetApkfilterWithSpecialCharacters() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should handle special characters in APK filter strings
        assertDoesNotThrow(() -> element.setApkfilter("app-debug_v1.0.apk"));
        assertDoesNotThrow(() -> element.setApkfilter("**/*.{apk}"));
    }

    /**
     * Test setApkfilter with path-style filters.
     * APK filters may include path separators.
     */
    @Test
    public void testSetApkfilterWithPathNotation() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setApkfilter("debug/*.apk"));
        assertDoesNotThrow(() -> element.setApkfilter("release/**/*.apk"));
    }

    /**
     * Test setApkfilter followed by appendClassPathEntriesTo with output flag.
     * Verifies that APK filter works correctly for output entries.
     */
    @Test
    public void testSetApkfilterWithOutputEntry() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Create a test output APK file
        File outputApk = tempDir.resolve("output.apk").toFile();
        assertTrue(outputApk.createNewFile(), "Output APK file should be created");

        element.setLocation(outputApk);
        element.setApkfilter("*.apk");

        // Create a ClassPath and append as output entry
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, true));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
        assertTrue(classPath.get(0).isOutput(), "Entry should be marked as output");
    }

    /**
     * Test setApkfilter with whitespace in the filter string.
     * Verifies handling of filters with spaces.
     */
    @Test
    public void testSetApkfilterWithWhitespace() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Whitespace might be significant in filter patterns
        assertDoesNotThrow(() -> element.setApkfilter("base.apk, feature.apk"));
        assertDoesNotThrow(() -> element.setApkfilter(" *.apk "));
    }

    /**
     * Test that setApkfilter can be called after setting location.
     * Order of operations should not matter.
     */
    @Test
    public void testSetApkfilterAfterSetLocation() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testApk = tempDir.resolve("test.apk").toFile();
        assertTrue(testApk.createNewFile(), "Test APK should be created");

        // Set location first, then APK filter
        element.setLocation(testApk);
        assertDoesNotThrow(() -> element.setApkfilter("*.apk"));

        // Verify it can be used
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));
    }

    /**
     * Test that setApkfilter can be called before setting location.
     * Order of operations should not matter.
     */
    @Test
    public void testSetApkfilterBeforeSetLocation() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Set APK filter first, then location
        assertDoesNotThrow(() -> element.setApkfilter("*.apk"));

        File testApk = tempDir.resolve("test.apk").toFile();
        assertTrue(testApk.createNewFile(), "Test APK should be created");
        element.setLocation(testApk);

        // Verify it can be used
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));
    }

    /**
     * Test setApkfilter with Android-specific APK naming conventions.
     * Android APKs often follow specific naming patterns.
     */
    @Test
    public void testSetApkfilterWithAndroidNamingConventions() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Common Android APK naming patterns
        assertDoesNotThrow(() -> element.setApkfilter("app-debug.apk"));
        assertDoesNotThrow(() -> element.setApkfilter("app-release-unsigned.apk"));
        assertDoesNotThrow(() -> element.setApkfilter("base-master.apk"));
        assertDoesNotThrow(() -> element.setApkfilter("split_config.*.apk"));
    }

    /**
     * Test setApkfilter combined with other filter setters.
     * Verifies that APK filter can coexist with other filters like jarFilter.
     */
    @Test
    public void testSetApkfilterWithOtherFilters() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testFile = tempDir.resolve("test.jar").toFile();
        assertTrue(testFile.createNewFile(), "Test file should be created");

        // Set multiple filters
        element.setLocation(testFile);
        element.setApkfilter("*.apk");
        element.setJarfilter("*.jar");
        element.setFilter("*.class");

        // Should work without errors
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
    }
}
