package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for FilterElement.setName method.
 */
public class FilterElementClaude_setNameTest {

    /**
     * Test that setName accepts a simple filter string.
     */
    @Test
    public void testSetNameWithSimpleFilter() {
        FilterElement element = new FilterElement();

        assertDoesNotThrow(() -> element.setName("com.example.**"),
            "Should accept simple filter string");
    }

    /**
     * Test that setName with a simple filter string works correctly with appendTo.
     */
    @Test
    public void testSetNameSimpleFilterAppendsCorrectly() {
        FilterElement element = new FilterElement();
        element.setName("com.example.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertFalse(filterList.isEmpty(), "Filter list should not be empty");
        assertTrue(filterList.contains("com.example.**"),
            "Filter list should contain the exact filter string");
    }

    /**
     * Test that setName accepts null value.
     */
    @Test
    public void testSetNameWithNull() {
        FilterElement element = new FilterElement();

        assertDoesNotThrow(() -> element.setName(null),
            "Should accept null value");
    }

    /**
     * Test that setName with null clears the filter list when used with appendTo.
     */
    @Test
    public void testSetNameNullClearsFilterList() {
        FilterElement element = new FilterElement();
        element.setName(null);

        List<String> filterList = new ArrayList<>();
        filterList.add("existing.item1");
        filterList.add("existing.item2");

        element.appendTo(filterList, false);

        assertTrue(filterList.isEmpty(),
            "Filter list should be cleared when name is set to null");
    }

    /**
     * Test that setName accepts an empty string.
     */
    @Test
    public void testSetNameWithEmptyString() {
        FilterElement element = new FilterElement();

        assertDoesNotThrow(() -> element.setName(""),
            "Should accept empty string");
    }

    /**
     * Test that setName with empty string produces an empty filter list.
     */
    @Test
    public void testSetNameEmptyStringProducesEmptyFilterList() {
        FilterElement element = new FilterElement();
        element.setName("");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertTrue(filterList.isEmpty(),
            "Filter list should be empty when name is set to empty string");
    }

    /**
     * Test that setName accepts comma-separated filter strings.
     */
    @Test
    public void testSetNameWithCommaSeparatedFilters() {
        FilterElement element = new FilterElement();

        assertDoesNotThrow(() -> element.setName("com.example.package1.**,com.example.package2.**"),
            "Should accept comma-separated filter strings");
    }

    /**
     * Test that setName with comma-separated filters produces multiple entries in the list.
     */
    @Test
    public void testSetNameCommaSeparatedFiltersProducesMultipleEntries() {
        FilterElement element = new FilterElement();
        element.setName("com.example.package1.**,com.example.package2.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertEquals(2, filterList.size(),
            "Filter list should contain 2 entries for comma-separated filters");
        assertTrue(filterList.contains("com.example.package1.**"),
            "Filter list should contain first filter");
        assertTrue(filterList.contains("com.example.package2.**"),
            "Filter list should contain second filter");
    }

    /**
     * Test that setName can be called multiple times, with the last value taking effect.
     */
    @Test
    public void testSetNameMultipleTimes() {
        FilterElement element = new FilterElement();

        element.setName("com.example.first.**");
        element.setName("com.example.second.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertEquals(1, filterList.size(), "Filter list should contain only one entry");
        assertTrue(filterList.contains("com.example.second.**"),
            "Filter list should contain the last set value");
        assertFalse(filterList.contains("com.example.first.**"),
            "Filter list should not contain the first value");
    }

    /**
     * Test that setName overwrites previously set filter.
     */
    @Test
    public void testSetNameOverwritesPreviousFilter() {
        FilterElement element = new FilterElement();

        element.setFilter("com.example.filter.**");
        element.setName("com.example.name.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertEquals(1, filterList.size(), "Filter list should contain only one entry");
        assertTrue(filterList.contains("com.example.name.**"),
            "Filter list should contain the name value");
        assertFalse(filterList.contains("com.example.filter.**"),
            "Filter list should not contain the filter value");
    }

    /**
     * Test that setName works correctly with internal class name conversion.
     */
    @Test
    public void testSetNameWithInternalClassNameConversion() {
        FilterElement element = new FilterElement();
        element.setName("com.example.MyClass");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, true);

        assertFalse(filterList.isEmpty(), "Filter list should not be empty");
        assertEquals(1, filterList.size(), "Filter list should contain one entry");
        assertTrue(filterList.get(0).contains("/"),
            "Internal conversion should convert dots to slashes");
        assertEquals("com/example/MyClass", filterList.get(0),
            "Internal conversion should produce correct internal class name");
    }

    /**
     * Test that setName works correctly without internal class name conversion.
     */
    @Test
    public void testSetNameWithoutInternalClassNameConversion() {
        FilterElement element = new FilterElement();
        element.setName("com.example.MyClass");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertFalse(filterList.isEmpty(), "Filter list should not be empty");
        assertEquals(1, filterList.size(), "Filter list should contain one entry");
        assertTrue(filterList.get(0).contains("."),
            "Without internal conversion, dots should remain as dots");
        assertEquals("com.example.MyClass", filterList.get(0),
            "Without internal conversion, the original string should be preserved");
    }

    /**
     * Test that setName with wildcard patterns works correctly.
     */
    @Test
    public void testSetNameWithWildcardPattern() {
        FilterElement element = new FilterElement();
        element.setName("com.example.*");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertFalse(filterList.isEmpty(), "Filter list should not be empty");
        assertTrue(filterList.contains("com.example.*"),
            "Filter list should contain wildcard pattern");
    }

    /**
     * Test that setName with double wildcard patterns works correctly.
     */
    @Test
    public void testSetNameWithDoubleWildcardPattern() {
        FilterElement element = new FilterElement();
        element.setName("com.example.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertFalse(filterList.isEmpty(), "Filter list should not be empty");
        assertTrue(filterList.contains("com.example.**"),
            "Filter list should contain double wildcard pattern");
    }

    /**
     * Test that setName with special characters works correctly.
     */
    @Test
    public void testSetNameWithSpecialCharacters() {
        FilterElement element = new FilterElement();
        element.setName("com.example.!public,com.example.!private");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertEquals(2, filterList.size(), "Filter list should contain 2 entries");
        assertTrue(filterList.contains("com.example.!public"),
            "Filter list should contain negation pattern");
        assertTrue(filterList.contains("com.example.!private"),
            "Filter list should contain second negation pattern");
    }

    /**
     * Test that setName can be called on an element that is part of an Ant project.
     */
    @Test
    public void testSetNameWithAntProject() {
        FilterElement element = new FilterElement();
        Project project = new Project();
        project.init();
        element.setProject(project);

        assertDoesNotThrow(() -> element.setName("com.example.**"),
            "Should work with Ant project");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertFalse(filterList.isEmpty(), "Filter list should be populated");
    }

    /**
     * Test that setName with whitespace-containing strings works correctly.
     */
    @Test
    public void testSetNameWithWhitespace() {
        FilterElement element = new FilterElement();
        element.setName("com.example.package1.**, com.example.package2.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertEquals(2, filterList.size(), "Filter list should contain 2 entries");
        // Note: whitespace after comma is trimmed by ListUtil.commaSeparatedList
    }

    /**
     * Test that setName followed by null resets the filter.
     */
    @Test
    public void testSetNameFollowedByNull() {
        FilterElement element = new FilterElement();
        element.setName("com.example.**");
        element.setName(null);

        List<String> filterList = new ArrayList<>();
        filterList.add("existing.item");
        element.appendTo(filterList, false);

        assertTrue(filterList.isEmpty(),
            "Filter list should be cleared when name is reset to null");
    }

    /**
     * Test that setName can handle complex filter patterns with multiple wildcards.
     */
    @Test
    public void testSetNameWithComplexFilterPattern() {
        FilterElement element = new FilterElement();
        element.setName("com.*.example.**,org.**.test.*");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertEquals(2, filterList.size(), "Filter list should contain 2 entries");
        assertTrue(filterList.contains("com.*.example.**"),
            "Filter list should contain first complex pattern");
        assertTrue(filterList.contains("org.**.test.*"),
            "Filter list should contain second complex pattern");
    }

    /**
     * Test that setName with a single wildcard works correctly.
     */
    @Test
    public void testSetNameWithSingleWildcard() {
        FilterElement element = new FilterElement();
        element.setName("*");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertFalse(filterList.isEmpty(), "Filter list should not be empty");
        assertTrue(filterList.contains("*"), "Filter list should contain single wildcard");
    }

    /**
     * Test that setName can be used multiple times with different patterns.
     */
    @Test
    public void testSetNameMultipleTimesWithDifferentPatterns() {
        FilterElement element = new FilterElement();
        List<String> filterList1 = new ArrayList<>();
        List<String> filterList2 = new ArrayList<>();

        element.setName("com.example.first.**");
        element.appendTo(filterList1, false);

        element.setName("com.example.second.**");
        element.appendTo(filterList2, false);

        assertTrue(filterList1.contains("com.example.first.**"),
            "First filter list should contain first pattern");
        assertTrue(filterList2.contains("com.example.second.**"),
            "Second filter list should contain second pattern");
        assertFalse(filterList2.contains("com.example.first.**"),
            "Second filter list should not contain first pattern");
    }

    /**
     * Test that setName works correctly when the filter contains only commas.
     */
    @Test
    public void testSetNameWithOnlyCommas() {
        FilterElement element = new FilterElement();
        element.setName(",,,");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        // ListUtil.commaSeparatedList should handle this - empty strings are typically filtered out
        assertTrue(filterList.isEmpty() || filterList.stream().allMatch(String::isEmpty),
            "Filter list should be empty or contain only empty strings");
    }

    /**
     * Test that setName works correctly with internal name conversion for complex patterns.
     */
    @Test
    public void testSetNameWithInternalConversionComplexPattern() {
        FilterElement element = new FilterElement();
        element.setName("com.example.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, true);

        assertFalse(filterList.isEmpty(), "Filter list should not be empty");
        assertTrue(filterList.get(0).contains("/"),
            "Internal conversion should convert dots to slashes");
        assertEquals("com/example/**", filterList.get(0),
            "Internal conversion should produce correct pattern");
    }
}
