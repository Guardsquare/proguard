package proguard.ant;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for FilterElement default constructor.
 */
public class FilterElementClaude_constructorTest {

    /**
     * Test that the default constructor successfully creates a FilterElement instance.
     */
    @Test
    public void testDefaultConstructorCreatesInstance() {
        FilterElement element = new FilterElement();

        assertNotNull(element, "FilterElement should be created successfully");
    }

    /**
     * Test that the default constructor creates a valid DataType.
     * FilterElement extends DataType, so it should behave as one.
     */
    @Test
    public void testDefaultConstructorCreatesValidDataType() {
        FilterElement element = new FilterElement();

        // Verify it behaves as a DataType by checking we can use DataType methods
        assertFalse(element.isReference(), "Newly created element should not be a reference");
        assertInstanceOf(DataType.class, element, "FilterElement should be a DataType");
    }

    /**
     * Test that the constructor initializes the element to work with Ant projects.
     */
    @Test
    public void testDefaultConstructorWorksWithAntProject() {
        FilterElement element = new FilterElement();
        Project project = new Project();
        project.init();

        // DataType provides setProject method
        element.setProject(project);

        assertSame(project, element.getProject(), "Project should be settable on the element");
    }

    /**
     * Test that multiple instances can be created independently.
     */
    @Test
    public void testDefaultConstructorCreatesIndependentInstances() {
        FilterElement element1 = new FilterElement();
        FilterElement element2 = new FilterElement();

        assertNotNull(element1, "First element should be created");
        assertNotNull(element2, "Second element should be created");
        assertNotSame(element1, element2, "Elements should be different instances");
    }

    /**
     * Test that the constructor allows setting the filter name.
     * This verifies that the internal state is properly initialized.
     */
    @Test
    public void testDefaultConstructorAllowsSettingName() {
        FilterElement element = new FilterElement();

        // Should be able to set name without errors
        assertDoesNotThrow(() -> element.setName("com.example.**"),
            "Should be able to set name");
    }

    /**
     * Test that the constructor allows setting the filter.
     * This verifies that the internal state is properly initialized.
     */
    @Test
    public void testDefaultConstructorAllowsSettingFilter() {
        FilterElement element = new FilterElement();

        // Should be able to set filter without errors
        assertDoesNotThrow(() -> element.setFilter("com.example.**"),
            "Should be able to set filter");
    }

    /**
     * Test that a newly constructed element can be used with the appendTo method.
     */
    @Test
    public void testDefaultConstructorAllowsAppendTo() {
        FilterElement element = new FilterElement();
        element.setName("com.example.**");

        List<String> filterList = new ArrayList<>();

        // Should be able to call appendTo without errors
        assertDoesNotThrow(() -> element.appendTo(filterList, false),
            "Should be able to call appendTo with internal=false");

        assertFalse(filterList.isEmpty(), "Filter list should be populated after appendTo");
    }

    /**
     * Test that a newly constructed element can be used with appendTo and internal conversion.
     */
    @Test
    public void testDefaultConstructorAllowsAppendToWithInternalConversion() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.**");

        List<String> filterList = new ArrayList<>();

        // Should be able to call appendTo with internal conversion
        assertDoesNotThrow(() -> element.appendTo(filterList, true),
            "Should be able to call appendTo with internal=true");

        assertFalse(filterList.isEmpty(), "Filter list should be populated after appendTo");
    }

    /**
     * Test that a newly constructed element with no filter set clears the target list.
     * This verifies the constructor initializes the filter field to null (default behavior).
     */
    @Test
    public void testDefaultConstructorWithNullFilterClearsList() {
        FilterElement element = new FilterElement();
        // Don't set any filter - it should be null by default after construction

        List<String> filterList = new ArrayList<>();
        filterList.add("existing.item");

        element.appendTo(filterList, false);

        assertTrue(filterList.isEmpty(), "Filter list should be cleared when filter is null");
    }

    /**
     * Test that the constructor initializes the element to work with references.
     */
    @Test
    public void testDefaultConstructorAllowsReferences() {
        FilterElement element = new FilterElement();
        Project project = new Project();
        project.init();
        element.setProject(project);

        // Should be able to set a reference without errors
        assertDoesNotThrow(() -> element.setRefid(new org.apache.tools.ant.types.Reference(project, "test-ref")),
            "Should be able to set reference");
    }

    /**
     * Test that a newly constructed element can be configured with a name
     * and used multiple times with different lists.
     */
    @Test
    public void testDefaultConstructorAllowsMultipleAppendToOperations() {
        FilterElement element = new FilterElement();
        element.setName("com.example.**");

        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        // Should be able to call appendTo multiple times
        assertDoesNotThrow(() -> {
            element.appendTo(list1, false);
            element.appendTo(list2, false);
        }, "Should be able to call appendTo multiple times");

        assertFalse(list1.isEmpty(), "First list should be populated");
        assertFalse(list2.isEmpty(), "Second list should be populated");
    }

    /**
     * Test that multiple FilterElement instances can be created and configured independently.
     */
    @Test
    public void testDefaultConstructorCreatesIndependentlyConfigurableInstances() {
        FilterElement element1 = new FilterElement();
        FilterElement element2 = new FilterElement();

        element1.setName("com.example.package1.**");
        element2.setFilter("com.example.package2.**");

        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        element1.appendTo(list1, false);
        element2.appendTo(list2, false);

        // Verify that the elements are independent
        assertNotEquals(list1, list2, "Different elements should produce different filter lists");
    }

    /**
     * Test that a newly constructed element works correctly with comma-separated filters.
     */
    @Test
    public void testDefaultConstructorHandlesCommaSeparatedFilters() {
        FilterElement element = new FilterElement();
        element.setName("com.example.package1.**,com.example.package2.**");

        List<String> filterList = new ArrayList<>();

        element.appendTo(filterList, false);

        assertTrue(filterList.size() > 1, "Comma-separated filter should produce multiple entries");
    }

    /**
     * Test that the constructor creates an element that handles internal class name conversion.
     */
    @Test
    public void testDefaultConstructorHandlesInternalClassNameConversion() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.MyClass");

        List<String> filterList = new ArrayList<>();

        element.appendTo(filterList, true);

        assertFalse(filterList.isEmpty(), "Filter list should be populated");
        // When internal=true, dots should be converted to slashes
        assertTrue(filterList.stream().anyMatch(s -> s.contains("/")),
            "Internal conversion should convert dots to slashes");
    }

    /**
     * Test that a newly constructed element without a project can still be used.
     */
    @Test
    public void testDefaultConstructorWorksWithoutProject() {
        FilterElement element = new FilterElement();
        // Don't set a project
        assertNull(element.getProject(), "Project should be null by default");

        element.setName("com.example.**");
        List<String> filterList = new ArrayList<>();

        // Should still work without a project
        assertDoesNotThrow(() -> element.appendTo(filterList, false),
            "Should work without a project set");
    }

    /**
     * Test that the constructor creates an element that can be checked for reference status.
     */
    @Test
    public void testDefaultConstructorIsNotReference() {
        FilterElement element = new FilterElement();

        assertFalse(element.isReference(), "Newly constructed element should not be a reference");
    }

    /**
     * Test that the constructor creates an element that can be used immediately
     * after construction without any configuration.
     */
    @Test
    public void testDefaultConstructorAllowsImmediateUse() {
        FilterElement element = new FilterElement();
        List<String> filterList = new ArrayList<>();
        filterList.add("existing.item");

        // Should be able to use element immediately (with null filter, it clears the list)
        assertDoesNotThrow(() -> element.appendTo(filterList, false),
            "Should be able to use element immediately after construction");
    }
}
