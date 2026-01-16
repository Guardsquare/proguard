package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassPathElement constructor that takes a Project parameter.
 */
public class ClassPathElementClaude_constructorTest {

    /**
     * Test that the constructor successfully creates a ClassPathElement with a valid Project.
     */
    @Test
    public void testConstructorWithValidProject() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        assertNotNull(element, "ClassPathElement should be created successfully");
        assertNotNull(element.getProject(), "Project should be set in ClassPathElement");
        assertSame(project, element.getProject(), "The project should be the same instance that was passed to constructor");
    }

    /**
     * Test that the constructor works with a null Project.
     * The Apache Ant Path class allows null projects, so this should work.
     */
    @Test
    public void testConstructorWithNullProject() {
        ClassPathElement element = new ClassPathElement(null);

        assertNotNull(element, "ClassPathElement should be created successfully even with null project");
        assertNull(element.getProject(), "Project should be null");
    }

    /**
     * Test that the created ClassPathElement is a valid Path object.
     */
    @Test
    public void testConstructorCreatesValidPathObject() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Verify it behaves as a Path by checking we can use Path methods
        assertNotNull(element.list(), "Should be able to call list() method from Path");
        assertEquals(0, element.list().length, "Empty path should have no elements");
    }

    /**
     * Test that multiple ClassPathElement instances can be created with different projects.
     */
    @Test
    public void testConstructorWithMultipleProjects() {
        Project project1 = new Project();
        project1.init();
        project1.setName("Project1");

        Project project2 = new Project();
        project2.init();
        project2.setName("Project2");

        ClassPathElement element1 = new ClassPathElement(project1);
        ClassPathElement element2 = new ClassPathElement(project2);

        assertNotNull(element1, "First element should be created");
        assertNotNull(element2, "Second element should be created");
        assertNotSame(element1, element2, "Elements should be different instances");
        assertSame(project1, element1.getProject(), "First element should have first project");
        assertSame(project2, element2.getProject(), "Second element should have second project");
    }

    /**
     * Test that the constructor properly initializes the ClassPathElement
     * to be used in classpath operations.
     */
    @Test
    public void testConstructorInitializesForClassPathOperations() {
        Project project = new Project();
        project.init();

        ClassPathElement element = new ClassPathElement(project);

        // Verify that the element is not a reference (default behavior)
        assertFalse(element.isReference(), "Newly created element should not be a reference");
    }
}
