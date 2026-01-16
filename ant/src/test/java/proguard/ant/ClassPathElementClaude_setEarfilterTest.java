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
 * Test class for ClassPathElement.setEarfilter method.
 * The setEarfilter method sets an EAR (Enterprise Archive) filter string that is later used
 * when appending class path entries to filter EAR files within archives.
 */
public class ClassPathElementClaude_setEarfilterTest {

    @TempDir
    Path tempDir;

    /**
     * Test setEarfilter with a simple filter string.
     * Verifies that the EAR filter can be set without throwing exceptions.
     */
    @Test
    public void testSetEarfilterWithSimpleString() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setEarfilter("*.ear"));
    }

    /**
     * Test setEarfilter with null value.
     * The method should accept null filters gracefully.
     */
    @Test
    public void testSetEarfilterWithNull() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setEarfilter(null));
    }

    /**
     * Test setEarfilter with an empty string.
     * The method should accept empty string filters.
     */
    @Test
    public void testSetEarfilterWithEmptyString() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setEarfilter(""));
    }

    /**
     * Test setEarfilter with a comma-separated filter list.
     * The EAR filter supports comma-separated values for multiple patterns.
     */
    @Test
    public void testSetEarfilterWithCommaSeparatedList() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setEarfilter("application.ear,services.ear,portal.ear"));
    }

    /**
     * Test setEarfilter with wildcard patterns.
     * Verifies that wildcard patterns are accepted.
     */
    @Test
    public void testSetEarfilterWithWildcardPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setEarfilter("*.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("app-*.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("**/*.ear"));
    }

    /**
     * Test setEarfilter multiple times on the same element.
     * Each call should replace the previous filter value.
     */
    @Test
    public void testSetEarfilterMultipleTimes() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should be able to set EAR filter multiple times
        assertDoesNotThrow(() -> {
            element.setEarfilter("application.ear");
            element.setEarfilter("services.ear");
            element.setEarfilter("portal.ear");
        });
    }

    /**
     * Test setEarfilter with negation patterns.
     * EAR filters can include negation patterns (typically with ! prefix).
     */
    @Test
    public void testSetEarfilterWithNegationPattern() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setEarfilter("!test.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("*.ear,!*-debug.ear"));
    }

    /**
     * Test setEarfilter integration with appendClassPathEntriesTo.
     * Verifies that after setting an EAR filter, the element can be used
     * to append entries to a ClassPath without errors.
     */
    @Test
    public void testSetEarfilterIntegrationWithAppendClassPathEntries() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Create a test EAR file
        File testEar = tempDir.resolve("test.ear").toFile();
        assertTrue(testEar.createNewFile(), "Test EAR file should be created");

        // Set a location and EAR filter
        element.setLocation(testEar);
        element.setEarfilter("*.ear");

        // Create a ClassPath and append entries
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
    }

    /**
     * Test setEarfilter with null project.
     * The method should work even with a null project.
     */
    @Test
    public void testSetEarfilterWithNullProject() {
        ClassPathElement element = new ClassPathElement(null);

        // Should not throw any exception
        assertDoesNotThrow(() -> element.setEarfilter("*.ear"));
    }

    /**
     * Test setEarfilter with special characters.
     * EAR filters may contain special regex or path characters.
     */
    @Test
    public void testSetEarfilterWithSpecialCharacters() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Should handle special characters in EAR filter strings
        assertDoesNotThrow(() -> element.setEarfilter("my-enterprise-app-1.0.0.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("**/*.{ear}"));
    }

    /**
     * Test setEarfilter with path-style filters.
     * EAR filters may include path separators.
     */
    @Test
    public void testSetEarfilterWithPathNotation() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertDoesNotThrow(() -> element.setEarfilter("target/*.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("build/dist/**/*.ear"));
    }

    /**
     * Test setEarfilter followed by appendClassPathEntriesTo with output flag.
     * Verifies that EAR filter works correctly for output entries.
     */
    @Test
    public void testSetEarfilterWithOutputEntry() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Create a test output EAR file
        File outputEar = tempDir.resolve("output.ear").toFile();
        assertTrue(outputEar.createNewFile(), "Output EAR file should be created");

        element.setLocation(outputEar);
        element.setEarfilter("*.ear");

        // Create a ClassPath and append as output entry
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, true));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
        assertTrue(classPath.get(0).isOutput(), "Entry should be marked as output");
    }

    /**
     * Test setEarfilter with whitespace in the filter string.
     * Verifies handling of filters with spaces.
     */
    @Test
    public void testSetEarfilterWithWhitespace() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Whitespace might be significant in filter patterns
        assertDoesNotThrow(() -> element.setEarfilter("application.ear, services.ear"));
        assertDoesNotThrow(() -> element.setEarfilter(" *.ear "));
    }

    /**
     * Test that setEarfilter can be called after setting location.
     * Order of operations should not matter.
     */
    @Test
    public void testSetEarfilterAfterSetLocation() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testEar = tempDir.resolve("test.ear").toFile();
        assertTrue(testEar.createNewFile(), "Test EAR should be created");

        // Set location first, then EAR filter
        element.setLocation(testEar);
        assertDoesNotThrow(() -> element.setEarfilter("*.ear"));

        // Verify it can be used
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));
    }

    /**
     * Test that setEarfilter can be called before setting location.
     * Order of operations should not matter.
     */
    @Test
    public void testSetEarfilterBeforeSetLocation() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        // Set EAR filter first, then location
        assertDoesNotThrow(() -> element.setEarfilter("*.ear"));

        File testEar = tempDir.resolve("test.ear").toFile();
        assertTrue(testEar.createNewFile(), "Test EAR should be created");
        element.setLocation(testEar);

        // Verify it can be used
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));
    }

    /**
     * Test setEarfilter with Java EE enterprise application naming conventions.
     * Java EE EAR files often follow specific naming patterns.
     */
    @Test
    public void testSetEarfilterWithJavaEENamingConventions() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Common Java EE EAR naming patterns
        assertDoesNotThrow(() -> element.setEarfilter("enterprise-app.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("myapp-1.0-SNAPSHOT.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("portal-application.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("banking-services.ear"));
    }

    /**
     * Test setEarfilter combined with other filter setters.
     * Verifies that EAR filter can coexist with other filters like warFilter.
     */
    @Test
    public void testSetEarfilterWithOtherFilters() throws IOException {
        Project project = new Project();
        project.init();
        project.setBasedir(tempDir.toFile().getAbsolutePath());

        ClassPathElement element = new ClassPathElement(project);

        File testFile = tempDir.resolve("test.jar").toFile();
        assertTrue(testFile.createNewFile(), "Test file should be created");

        // Set multiple filters
        element.setLocation(testFile);
        element.setEarfilter("*.ear");
        element.setWarfilter("*.war");
        element.setFilter("*.class");

        // Should work without errors
        ClassPath classPath = new ClassPath();
        assertDoesNotThrow(() -> element.appendClassPathEntriesTo(classPath, false));

        // Verify that an entry was added
        assertNotNull(classPath.get(0), "ClassPath should have at least one entry");
    }

    /**
     * Test setEarfilter with version pattern filters.
     * Useful for filtering EAR files by version.
     */
    @Test
    public void testSetEarfilterWithVersionPatterns() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Version-specific patterns
        assertDoesNotThrow(() -> element.setEarfilter("app-1.*.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("*-[0-9]*.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("*-SNAPSHOT.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("enterprise-?.?.?.ear"));
    }

    /**
     * Test setEarfilter with WebLogic EAR naming conventions.
     * WebLogic may have specific EAR naming patterns.
     */
    @Test
    public void testSetEarfilterWithWebLogicNaming() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // WebLogic-style EAR naming patterns
        assertDoesNotThrow(() -> element.setEarfilter("weblogic-*.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("*-ejb.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("portal-app-*.ear"));
    }

    /**
     * Test setEarfilter with WebSphere EAR naming conventions.
     * WebSphere may have specific EAR naming patterns.
     */
    @Test
    public void testSetEarfilterWithWebSphereNaming() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // WebSphere-style EAR naming patterns
        assertDoesNotThrow(() -> element.setEarfilter("*Cell*.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("*App.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("DefaultApplication.ear"));
    }

    /**
     * Test setEarfilter with JBoss/WildFly EAR naming conventions.
     * JBoss/WildFly may have specific EAR naming patterns.
     */
    @Test
    public void testSetEarfilterWithJBossNaming() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // JBoss/WildFly-style EAR naming patterns
        assertDoesNotThrow(() -> element.setEarfilter("jboss-*.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("*-service.ear"));
        assertDoesNotThrow(() -> element.setEarfilter("wildfly-app-*.ear"));
    }
}
