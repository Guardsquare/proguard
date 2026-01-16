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
 * Test class for ClassPathElement.setJarfilter method.
 * The setJarfilter method sets a JAR filter string that is later used when appending
 * class path entries to filter JAR files within archives.
 */
public class ClassPathElementClaude_setJarfilterTest {

    @TempDir
    Path tempDir;

    /**
     * Test setJarfilter with a simple filter string.
     * Verifies that the JAR filter can be set without throwing exceptions.
     */
    @Test
    public void testSetJarfilterWithSimpleString() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setJarfilter("*.jar"));
    }

    /**
     * Test setJarfilter with null value.
     * The method should accept null filters gracefully.
     */
    @Test
    public void testSetJarfilterWithNull() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setJarfilter(null));
    }

    /**
     * Test setJarfilter with an empty string.
     * The method should accept empty string filters.
     */
    @Test
    public void testSetJarfilterWithEmptyString() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setJarfilter(""));
    }

    /**
     * Test setJarfilter with a comma-separated filter list.
     * The JAR filter supports comma-separated values for multiple patterns.
     */
    @Test
    public void testSetJarfilterWithCommaSeparatedList() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setJarfilter("commons-*.jar,guava-*.jar,slf4j-*.jar"));
    }

    /**
     * Test setJarfilter with wildcard patterns.
     * Verifies that wildcard patterns are accepted.
     */
    @Test
    public void testSetJarfilterWithWildcardPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setJarfilter("*.jar"));
        assertDoesNotThrow(() -> element.setJarfilter("lib-*.jar"));
        assertDoesNotThrow(() -> element.setJarfilter("**/*.jar"));
    }

    /**
     * Test setJarfilter multiple times on the same element.
     * Each call should replace the previous filter value.
     */
    @Test
    public void testSetJarfilterMultipleTimes() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should be able to set JAR filter multiple times
        assertDoesNotThrow(() -> {
            element.setJarfilter("commons-lang.jar");
            element.setJarfilter("guava.jar");
            element.setJarfilter("junit.jar");
        });
    }

    /**
     * Test setJarfilter with negation patterns.
     * JAR filters can include negation patterns (typically with ! prefix).
     */
    @Test
    public void testSetJarfilterWithNegationPattern() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setJarfilter("!test.jar"));
        assertDoesNotThrow(() -> element.setJarfilter("*.jar,!*-sources.jar"));
    }

    /**
     * Test setJarfilter integration with appendClassPathEntriesTo.
     * Verifies that after setting a JAR filter, the element can be used
     * to append entries to a ClassPath without errors.
     */
    @Test
    public void testSetJarfilterIntegrationWithAppendClassPathEntries() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Create a test JAR file
        File testJar = tempDir.resolve("test.jar").toFile();
        assertTrue(testJar.createNewFile(), "Test JAR file should be created");

        // Set a location and JAR filter
        element.setLocation(testJar);
        element.setJarfilter("*.jar");

        // Create a ClassPath and append entries
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
    }

    /**
     * Test setJarfilter with null project.
     * The method should work even with a null project.
     */
    @Test
    public void testSetJarfilterWithNullProject() {
        ClassPathElement element = new ClassPathElement(null);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setJarfilter("*.jar"));
    }

    /**
     * Test setJarfilter with special characters.
     * JAR filters may contain special regex or path characters.
     */
    @Test
    public void testSetJarfilterWithSpecialCharacters() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should handle special characters in JAR filter strings
        assertDoesNotThrow(() -> element.setJarfilter("commons-lang3-3.12.0.jar"));
        assertDoesNotThrow(() -> element.setJarfilter("**/*.{jar}"));
    }

    /**
     * Test setJarfilter with path-style filters.
     * JAR filters may include path separators.
     */
    @Test
    public void testSetJarfilterWithPathNotation() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setJarfilter("lib/*.jar"));
        assertDoesNotThrow(() -> element.setJarfilter("WEB-INF/lib/**/*.jar"));
    }

    /**
     * Test setJarfilter followed by appendClassPathEntriesTo with output flag.
     * Verifies that JAR filter works correctly for output entries.
     */
    @Test
    public void testSetJarfilterWithOutputEntry() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Create a test output JAR file
        File outputJar = tempDir.resolve("output.jar").toFile();
        assertTrue(outputJar.createNewFile(), "Output JAR file should be created");

        element.setLocation(outputJar);
        element.setJarfilter("*.jar");

        // Create a ClassPath and append as output entry
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, true));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
        assertTrue(classPath.get(0).isOutput(), "Entry should be marked as output");
    }

    /**
     * Test setJarfilter with whitespace in the filter string.
     * Verifies handling of filters with spaces.
     */
    @Test
    public void testSetJarfilterWithWhitespace() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Whitespace might be significant in filter patterns
        assertDoesNotThrow(() -> element.setJarfilter("commons-lang.jar, guava.jar"));
        assertDoesNotThrow(() -> element.setJarfilter(" *.jar "));
    }

    /**
     * Test that setJarfilter can be called after setting location.
     * Order of operations should not matter.
     */
    @Test
    public void testSetJarfilterAfterSetLocation() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testJar = tempDir.resolve("test.jar").toFile();
        assertTrue(testJar.createNewFile(), "Test JAR should be created");

        // Set location first, then JAR filter
        element.setLocation(testJar);
        assertDoesNotThrow(() -> element.setJarfilter("*.jar"));

        // Verify it can be used
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));
    }

    /**
     * Test that setJarfilter can be called before setting location.
     * Order of operations should not matter.
     */
    @Test
    public void testSetJarfilterBeforeSetLocation() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Set JAR filter first, then location
        assertDoesNotThrow(() -> element.setJarfilter("*.jar"));

        File testJar = tempDir.resolve("test.jar").toFile();
        assertTrue(testJar.createNewFile(), "Test JAR should be created");
        element.setLocation(testJar);

        // Verify it can be used
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));
    }

    /**
     * Test setJarfilter with common library naming conventions.
     * JAR files often follow specific naming patterns with versions.
     */
    @Test
    public void testSetJarfilterWithLibraryNamingConventions() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Common JAR naming patterns
        assertDoesNotThrow(() -> element.setJarfilter("commons-lang3-3.12.0.jar"));
        assertDoesNotThrow(() -> element.setJarfilter("guava-30.1-jre.jar"));
        assertDoesNotThrow(() -> element.setJarfilter("slf4j-api-1.7.30.jar"));
        assertDoesNotThrow(() -> element.setJarfilter("spring-*.jar"));
    }

    /**
     * Test setJarfilter combined with other filter setters.
     * Verifies that JAR filter can coexist with other filters like apkFilter.
     */
    @Test
    public void testSetJarfilterWithOtherFilters() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testFile = tempDir.resolve("test.war").toFile();
        assertTrue(testFile.createNewFile(), "Test file should be created");

        // Set multiple filters
        element.setLocation(testFile);
        element.setJarfilter("*.jar");
        element.setWarfilter("*.war");
        element.setFilter("*.class");

        // Should work without errors
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
    }

    /**
     * Test setJarfilter with version range patterns.
     * Useful for filtering JAR files by version patterns.
     */
    @Test
    public void testSetJarfilterWithVersionPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Version-specific patterns
        assertDoesNotThrow(() -> element.setJarfilter("lib-[0-9]*.jar"));
        assertDoesNotThrow(() -> element.setJarfilter("commons-*-3.*.jar"));
        assertDoesNotThrow(() -> element.setJarfilter("*-SNAPSHOT.jar"));
    }

    /**
     * Test setJarfilter with Maven/Gradle-style artifact patterns.
     * Common in build systems that use standard repository layouts.
     */
    @Test
    public void testSetJarfilterWithArtifactPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Maven/Gradle artifact patterns
        assertDoesNotThrow(() -> element.setJarfilter("com/example/**/*.jar"));
        assertDoesNotThrow(() -> element.setJarfilter("**/maven-metadata.xml"));
    }
}
