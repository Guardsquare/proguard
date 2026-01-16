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
 * Test class for ClassPathElement.setWarfilter method.
 * The setWarfilter method sets a WAR (Web Application Archive) filter string that is later used
 * when appending class path entries to filter WAR files within archives.
 */
public class ClassPathElementClaude_setWarfilterTest {

    @TempDir
    Path tempDir;

    /**
     * Test setWarfilter with a simple filter string.
     * Verifies that the WAR filter can be set without throwing exceptions.
     */
    @Test
    public void testSetWarfilterWithSimpleString() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setWarfilter("*.war"));
    }

    /**
     * Test setWarfilter with null value.
     * The method should accept null filters gracefully.
     */
    @Test
    public void testSetWarfilterWithNull() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setWarfilter(null));
    }

    /**
     * Test setWarfilter with an empty string.
     * The method should accept empty string filters.
     */
    @Test
    public void testSetWarfilterWithEmptyString() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setWarfilter(""));
    }

    /**
     * Test setWarfilter with a comma-separated filter list.
     * The WAR filter supports comma-separated values for multiple patterns.
     */
    @Test
    public void testSetWarfilterWithCommaSeparatedList() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setWarfilter("application.war,admin.war,services.war"));
    }

    /**
     * Test setWarfilter with wildcard patterns.
     * Verifies that wildcard patterns are accepted.
     */
    @Test
    public void testSetWarfilterWithWildcardPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setWarfilter("*.war"));
        assertDoesNotThrow(() -> element.setWarfilter("app-*.war"));
        assertDoesNotThrow(() -> element.setWarfilter("**/*.war"));
    }

    /**
     * Test setWarfilter multiple times on the same element.
     * Each call should replace the previous filter value.
     */
    @Test
    public void testSetWarfilterMultipleTimes() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should be able to set WAR filter multiple times
        assertDoesNotThrow(() -> {
            element.setWarfilter("application.war");
            element.setWarfilter("services.war");
            element.setWarfilter("admin.war");
        });
    }

    /**
     * Test setWarfilter with negation patterns.
     * WAR filters can include negation patterns (typically with ! prefix).
     */
    @Test
    public void testSetWarfilterWithNegationPattern() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setWarfilter("!test.war"));
        assertDoesNotThrow(() -> element.setWarfilter("*.war,!*-debug.war"));
    }

    /**
     * Test setWarfilter integration with appendClassPathEntriesTo.
     * Verifies that after setting a WAR filter, the element can be used
     * to append entries to a ClassPath without errors.
     */
    @Test
    public void testSetWarfilterIntegrationWithAppendClassPathEntries() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Create a test WAR file
        File testWar = tempDir.resolve("test.war").toFile();
        assertTrue(testWar.createNewFile(), "Test WAR file should be created");

        // Set a location and WAR filter
        element.setLocation(testWar);
        element.setWarfilter("*.war");

        // Create a ClassPath and append entries
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
    }

    /**
     * Test setWarfilter with null project.
     * The method should work even with a null project.
     */
    @Test
    public void testSetWarfilterWithNullProject() {
        ClassPathElement element = new ClassPathElement(null);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setWarfilter("*.war"));
    }

    /**
     * Test setWarfilter with special characters.
     * WAR filters may contain special regex or path characters.
     */
    @Test
    public void testSetWarfilterWithSpecialCharacters() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should handle special characters in WAR filter strings
        assertDoesNotThrow(() -> element.setWarfilter("my-app-1.0.0.war"));
        assertDoesNotThrow(() -> element.setWarfilter("**/*.{war}"));
    }

    /**
     * Test setWarfilter with path-style filters.
     * WAR filters may include path separators.
     */
    @Test
    public void testSetWarfilterWithPathNotation() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setWarfilter("target/*.war"));
        assertDoesNotThrow(() -> element.setWarfilter("build/libs/**/*.war"));
    }

    /**
     * Test setWarfilter followed by appendClassPathEntriesTo with output flag.
     * Verifies that WAR filter works correctly for output entries.
     */
    @Test
    public void testSetWarfilterWithOutputEntry() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Create a test output WAR file
        File outputWar = tempDir.resolve("output.war").toFile();
        assertTrue(outputWar.createNewFile(), "Output WAR file should be created");

        element.setLocation(outputWar);
        element.setWarfilter("*.war");

        // Create a ClassPath and append as output entry
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, true));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
        assertTrue(classPath.get(0).isOutput(), "Entry should be marked as output");
    }

    /**
     * Test setWarfilter with whitespace in the filter string.
     * Verifies handling of filters with spaces.
     */
    @Test
    public void testSetWarfilterWithWhitespace() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Whitespace might be significant in filter patterns
        assertDoesNotThrow(() -> element.setWarfilter("application.war, services.war"));
        assertDoesNotThrow(() -> element.setWarfilter(" *.war "));
    }

    /**
     * Test that setWarfilter can be called after setting location.
     * Order of operations should not matter.
     */
    @Test
    public void testSetWarfilterAfterSetLocation() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testWar = tempDir.resolve("test.war").toFile();
        assertTrue(testWar.createNewFile(), "Test WAR should be created");

        // Set location first, then WAR filter
        element.setLocation(testWar);
        assertDoesNotThrow(() -> element.setWarfilter("*.war"));

        // Verify it can be used
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));
    }

    /**
     * Test that setWarfilter can be called before setting location.
     * Order of operations should not matter.
     */
    @Test
    public void testSetWarfilterBeforeSetLocation() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Set WAR filter first, then location
        assertDoesNotThrow(() -> element.setWarfilter("*.war"));

        File testWar = tempDir.resolve("test.war").toFile();
        assertTrue(testWar.createNewFile(), "Test WAR should be created");
        element.setLocation(testWar);

        // Verify it can be used
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));
    }

    /**
     * Test setWarfilter with Java EE application naming conventions.
     * Java EE WAR files often follow specific naming patterns.
     */
    @Test
    public void testSetWarfilterWithJavaEENamingConventions() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Common Java EE WAR naming patterns
        assertDoesNotThrow(() -> element.setWarfilter("application.war"));
        assertDoesNotThrow(() -> element.setWarfilter("myapp-1.0-SNAPSHOT.war"));
        assertDoesNotThrow(() -> element.setWarfilter("ROOT.war"));
        assertDoesNotThrow(() -> element.setWarfilter("api-service.war"));
    }

    /**
     * Test setWarfilter combined with other filter setters.
     * Verifies that WAR filter can coexist with other filters like jarFilter.
     */
    @Test
    public void testSetWarfilterWithOtherFilters() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testFile = tempDir.resolve("test.ear").toFile();
        assertTrue(testFile.createNewFile(), "Test file should be created");

        // Set multiple filters
        element.setLocation(testFile);
        element.setWarfilter("*.war");
        element.setEarfilter("*.ear");
        element.setFilter("*.class");

        // Should work without errors
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
    }

    /**
     * Test setWarfilter with version pattern filters.
     * Useful for filtering WAR files by version.
     */
    @Test
    public void testSetWarfilterWithVersionPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Version-specific patterns
        assertDoesNotThrow(() -> element.setWarfilter("app-1.*.war"));
        assertDoesNotThrow(() -> element.setWarfilter("*-[0-9]*.war"));
        assertDoesNotThrow(() -> element.setWarfilter("*-SNAPSHOT.war"));
        assertDoesNotThrow(() -> element.setWarfilter("myapp-?.?.?.war"));
    }

    /**
     * Test setWarfilter with Spring Boot style WAR naming.
     * Spring Boot applications have specific naming conventions.
     */
    @Test
    public void testSetWarfilterWithSpringBootNaming() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Spring Boot WAR naming patterns
        assertDoesNotThrow(() -> element.setWarfilter("spring-boot-app-*.war"));
        assertDoesNotThrow(() -> element.setWarfilter("*-boot.war"));
        assertDoesNotThrow(() -> element.setWarfilter("application-[0-9]*.war"));
    }

    /**
     * Test setWarfilter with Tomcat context path patterns.
     * Tomcat uses WAR file names as context paths.
     */
    @Test
    public void testSetWarfilterWithTomcatContextPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Tomcat context patterns
        assertDoesNotThrow(() -> element.setWarfilter("ROOT.war"));
        assertDoesNotThrow(() -> element.setWarfilter("my##context.war"));
        assertDoesNotThrow(() -> element.setWarfilter("api#v1.war"));
    }
}
