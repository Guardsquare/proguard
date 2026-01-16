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
 * Test class for ClassPathElement.setFilter method.
 * The setFilter method sets a filter string that is later used when appending
 * class path entries to filter resources/classes.
 */
public class ClassPathElementClaude_setFilterTest {

    @TempDir
    Path tempDir;

    /**
     * Test setFilter with a simple filter string.
     * Verifies that the filter can be set without throwing exceptions.
     */
    @Test
    public void testSetFilterWithSimpleString() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setFilter("*.class"));
    }

    /**
     * Test setFilter with null value.
     * The method should accept null filters gracefully.
     */
    @Test
    public void testSetFilterWithNull() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setFilter(null));
    }

    /**
     * Test setFilter with an empty string.
     * The method should accept empty string filters.
     */
    @Test
    public void testSetFilterWithEmptyString() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setFilter(""));
    }

    /**
     * Test setFilter with a comma-separated filter list.
     * The filter supports comma-separated values for multiple patterns.
     */
    @Test
    public void testSetFilterWithCommaSeparatedList() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setFilter("*.class,*.properties,*.xml"));
    }

    /**
     * Test setFilter with wildcard patterns.
     * Verifies that wildcard patterns are accepted.
     */
    @Test
    public void testSetFilterWithWildcardPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setFilter("com/example/**/*.class"));
        assertDoesNotThrow(() -> element.setFilter("*.jar"));
        assertDoesNotThrow(() -> element.setFilter("**/*Test.class"));
    }

    /**
     * Test setFilter multiple times on the same element.
     * Each call should replace the previous filter value.
     */
    @Test
    public void testSetFilterMultipleTimes() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should be able to set filter multiple times
        assertDoesNotThrow(() -> {
            element.setFilter("*.class");
            element.setFilter("*.properties");
            element.setFilter("*.xml");
        });
    }

    /**
     * Test setFilter with negation patterns.
     * Filters can include negation patterns (typically with ! prefix).
     */
    @Test
    public void testSetFilterWithNegationPattern() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setFilter("!*.class"));
        assertDoesNotThrow(() -> element.setFilter("*.class,!*Test.class"));
    }

    /**
     * Test setFilter integration with appendClassPathEntriesTo.
     * Verifies that after setting a filter, the element can be used
     * to append entries to a ClassPath without errors.
     */
    @Test
    public void testSetFilterIntegrationWithAppendClassPathEntries() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Create a test jar file
        File testJar = tempDir.resolve("test.jar").toFile();
        assertTrue(testJar.createNewFile(), "Test jar file should be created");

        // Set a location and filter
        element.setLocation(testJar);
        element.setFilter("*.class");

        // Create a ClassPath and append entries
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
    }

    /**
     * Test setFilter with null project.
     * The method should work even with a null project.
     */
    @Test
    public void testSetFilterWithNullProject() {
        ClassPathElement element = new ClassPathElement(null);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setFilter("*.class"));
    }

    /**
     * Test setFilter with special characters.
     * Filters may contain special regex or path characters.
     */
    @Test
    public void testSetFilterWithSpecialCharacters() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should handle special characters in filter strings
        assertDoesNotThrow(() -> element.setFilter("com/example-*/lib_*.class"));
        assertDoesNotThrow(() -> element.setFilter("**/*.{class,properties}"));
    }

    /**
     * Test setFilter with package-style filters.
     * Filters often use package notation with slashes.
     */
    @Test
    public void testSetFilterWithPackageNotation() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setFilter("com/example/*.class"));
        assertDoesNotThrow(() -> element.setFilter("org/apache/**/*.class"));
    }

    /**
     * Test setFilter followed by appendClassPathEntriesTo with output flag.
     * Verifies that filter works correctly for output entries.
     */
    @Test
    public void testSetFilterWithOutputEntry() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Create a test output jar file
        File outputJar = tempDir.resolve("output.jar").toFile();
        assertTrue(outputJar.createNewFile(), "Output jar file should be created");

        element.setLocation(outputJar);
        element.setFilter("*.class");

        // Create a ClassPath and append as output entry
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, true));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
        assertTrue(classPath.get(0).isOutput(), "Entry should be marked as output");
    }

    /**
     * Test setFilter with whitespace in the filter string.
     * Verifies handling of filters with spaces.
     */
    @Test
    public void testSetFilterWithWhitespace() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Whitespace might be significant in filter patterns
        assertDoesNotThrow(() -> element.setFilter("*.class, *.properties"));
        assertDoesNotThrow(() -> element.setFilter(" *.class "));
    }

    /**
     * Test that setFilter can be called after setting location.
     * Order of operations should not matter.
     */
    @Test
    public void testSetFilterAfterSetLocation() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testJar = tempDir.resolve("test.jar").toFile();
        assertTrue(testJar.createNewFile(), "Test jar should be created");

        // Set location first, then filter
        element.setLocation(testJar);
        assertDoesNotThrow(() -> element.setFilter("*.class"));

        // Verify it can be used
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));
    }

    /**
     * Test that setFilter can be called before setting location.
     * Order of operations should not matter.
     */
    @Test
    public void testSetFilterBeforeSetLocation() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Set filter first, then location
        assertDoesNotThrow(() -> element.setFilter("*.class"));

        File testJar = tempDir.resolve("test.jar").toFile();
        assertTrue(testJar.createNewFile(), "Test jar should be created");
        element.setLocation(testJar);

        // Verify it can be used
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));
    }
}
