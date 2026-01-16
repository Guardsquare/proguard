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
 * Test class for ClassPathElement.setAarfilter method.
 * The setAarfilter method sets an AAR (Android Archive) filter string that is later used
 * when appending class path entries to filter AAR files within archives.
 */
public class ClassPathElementClaude_setAarfilterTest {

    @TempDir
    Path tempDir;

    /**
     * Test setAarfilter with a simple filter string.
     * Verifies that the AAR filter can be set without throwing exceptions.
     */
    @Test
    public void testSetAarfilterWithSimpleString() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setAarfilter("*.aar"));
    }

    /**
     * Test setAarfilter with null value.
     * The method should accept null filters gracefully.
     */
    @Test
    public void testSetAarfilterWithNull() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setAarfilter(null));
    }

    /**
     * Test setAarfilter with an empty string.
     * The method should accept empty string filters.
     */
    @Test
    public void testSetAarfilterWithEmptyString() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setAarfilter(""));
    }

    /**
     * Test setAarfilter with a comma-separated filter list.
     * The AAR filter supports comma-separated values for multiple patterns.
     */
    @Test
    public void testSetAarfilterWithCommaSeparatedList() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setAarfilter("support-v4.aar,appcompat-v7.aar,recyclerview.aar"));
    }

    /**
     * Test setAarfilter with wildcard patterns.
     * Verifies that wildcard patterns are accepted.
     */
    @Test
    public void testSetAarfilterWithWildcardPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setAarfilter("*.aar"));
        assertDoesNotThrow(() -> element.setAarfilter("support-*.aar"));
        assertDoesNotThrow(() -> element.setAarfilter("**/*.aar"));
    }

    /**
     * Test setAarfilter multiple times on the same element.
     * Each call should replace the previous filter value.
     */
    @Test
    public void testSetAarfilterMultipleTimes() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should be able to set AAR filter multiple times
        assertDoesNotThrow(() -> {
            element.setAarfilter("support-v4.aar");
            element.setAarfilter("appcompat-v7.aar");
            element.setAarfilter("design.aar");
        });
    }

    /**
     * Test setAarfilter with negation patterns.
     * AAR filters can include negation patterns (typically with ! prefix).
     */
    @Test
    public void testSetAarfilterWithNegationPattern() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setAarfilter("!test.aar"));
        assertDoesNotThrow(() -> element.setAarfilter("*.aar,!*-debug.aar"));
    }

    /**
     * Test setAarfilter integration with appendClassPathEntriesTo.
     * Verifies that after setting an AAR filter, the element can be used
     * to append entries to a ClassPath without errors.
     */
    @Test
    public void testSetAarfilterIntegrationWithAppendClassPathEntries() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Create a test AAR file
        File testAar = tempDir.resolve("test.aar").toFile();
        assertTrue(testAar.createNewFile(), "Test AAR file should be created");

        // Set a location and AAR filter
        element.setLocation(testAar);
        element.setAarfilter("*.aar");

        // Create a ClassPath and append entries
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
    }

    /**
     * Test setAarfilter with null project.
     * The method should work even with a null project.
     */
    @Test
    public void testSetAarfilterWithNullProject() {
        ClassPathElement element = new ClassPathElement(null);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setAarfilter("*.aar"));
    }

    /**
     * Test setAarfilter with special characters.
     * AAR filters may contain special regex or path characters.
     */
    @Test
    public void testSetAarfilterWithSpecialCharacters() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should handle special characters in AAR filter strings
        assertDoesNotThrow(() -> element.setAarfilter("support-v4-28.0.0.aar"));
        assertDoesNotThrow(() -> element.setAarfilter("**/*.{aar}"));
    }

    /**
     * Test setAarfilter with path-style filters.
     * AAR filters may include path separators.
     */
    @Test
    public void testSetAarfilterWithPathNotation() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setAarfilter("libs/*.aar"));
        assertDoesNotThrow(() -> element.setAarfilter("build/outputs/**/*.aar"));
    }

    /**
     * Test setAarfilter followed by appendClassPathEntriesTo with output flag.
     * Verifies that AAR filter works correctly for output entries.
     */
    @Test
    public void testSetAarfilterWithOutputEntry() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Create a test output AAR file
        File outputAar = tempDir.resolve("output.aar").toFile();
        assertTrue(outputAar.createNewFile(), "Output AAR file should be created");

        element.setLocation(outputAar);
        element.setAarfilter("*.aar");

        // Create a ClassPath and append as output entry
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, true));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
        assertTrue(classPath.get(0).isOutput(), "Entry should be marked as output");
    }

    /**
     * Test setAarfilter with whitespace in the filter string.
     * Verifies handling of filters with spaces.
     */
    @Test
    public void testSetAarfilterWithWhitespace() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Whitespace might be significant in filter patterns
        assertDoesNotThrow(() -> element.setAarfilter("support-v4.aar, appcompat-v7.aar"));
        assertDoesNotThrow(() -> element.setAarfilter(" *.aar "));
    }

    /**
     * Test that setAarfilter can be called after setting location.
     * Order of operations should not matter.
     */
    @Test
    public void testSetAarfilterAfterSetLocation() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testAar = tempDir.resolve("test.aar").toFile();
        assertTrue(testAar.createNewFile(), "Test AAR should be created");

        // Set location first, then AAR filter
        element.setLocation(testAar);
        assertDoesNotThrow(() -> element.setAarfilter("*.aar"));

        // Verify it can be used
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));
    }

    /**
     * Test that setAarfilter can be called before setting location.
     * Order of operations should not matter.
     */
    @Test
    public void testSetAarfilterBeforeSetLocation() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Set AAR filter first, then location
        assertDoesNotThrow(() -> element.setAarfilter("*.aar"));

        File testAar = tempDir.resolve("test.aar").toFile();
        assertTrue(testAar.createNewFile(), "Test AAR should be created");
        element.setLocation(testAar);

        // Verify it can be used
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));
    }

    /**
     * Test setAarfilter with Android Support Library naming conventions.
     * Android AAR files follow specific naming patterns.
     */
    @Test
    public void testSetAarfilterWithAndroidSupportLibraryNamingConventions() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Common Android Support Library AAR naming patterns
        assertDoesNotThrow(() -> element.setAarfilter("support-v4-28.0.0.aar"));
        assertDoesNotThrow(() -> element.setAarfilter("appcompat-v7-28.0.0.aar"));
        assertDoesNotThrow(() -> element.setAarfilter("recyclerview-v7-28.0.0.aar"));
        assertDoesNotThrow(() -> element.setAarfilter("design-28.0.0.aar"));
    }

    /**
     * Test setAarfilter with AndroidX naming conventions.
     * AndroidX libraries use different naming patterns than support libraries.
     */
    @Test
    public void testSetAarfilterWithAndroidXNamingConventions() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Common AndroidX AAR naming patterns
        assertDoesNotThrow(() -> element.setAarfilter("androidx.core-1.3.0.aar"));
        assertDoesNotThrow(() -> element.setAarfilter("androidx.appcompat-1.2.0.aar"));
        assertDoesNotThrow(() -> element.setAarfilter("androidx.*.aar"));
    }

    /**
     * Test setAarfilter combined with other filter setters.
     * Verifies that AAR filter can coexist with other filters like jarFilter.
     */
    @Test
    public void testSetAarfilterWithOtherFilters() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testFile = tempDir.resolve("test.jar").toFile();
        assertTrue(testFile.createNewFile(), "Test file should be created");

        // Set multiple filters
        element.setLocation(testFile);
        element.setAarfilter("*.aar");
        element.setJarfilter("*.jar");
        element.setFilter("*.class");

        // Should work without errors
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
    }

    /**
     * Test setAarfilter with version pattern filters.
     * Useful for filtering AAR files by version.
     */
    @Test
    public void testSetAarfilterWithVersionPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Version-specific patterns
        assertDoesNotThrow(() -> element.setAarfilter("support-*-28.*.aar"));
        assertDoesNotThrow(() -> element.setAarfilter("*-[0-9]*.aar"));
        assertDoesNotThrow(() -> element.setAarfilter("*-SNAPSHOT.aar"));
    }

    /**
     * Test setAarfilter with Google Play Services patterns.
     * Google Play Services AAR files have specific naming conventions.
     */
    @Test
    public void testSetAarfilterWithGooglePlayServicesPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Google Play Services patterns
        assertDoesNotThrow(() -> element.setAarfilter("play-services-*.aar"));
        assertDoesNotThrow(() -> element.setAarfilter("play-services-base-17.0.0.aar"));
        assertDoesNotThrow(() -> element.setAarfilter("play-services-maps-17.0.0.aar"));
    }
}
