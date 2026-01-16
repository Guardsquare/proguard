package proguard.ant;

import org.apache.tools.ant.Project;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for FilterElement.setFilter method.
 */
public class FilterElementClaude_setFilterTest {

    /**
     * Test that setFilter accepts a simple filter string.
     */
    @Test
    public void testSetFilterWithSimpleFilter() {
        FilterElement element = new FilterElement();

        assertDoesNotThrow(() -> element.setFilter("com.example.**"),
            "Should accept simple filter string");
    }

    /**
     * Test that setFilter with a simple filter string works correctly with appendTo.
     */
    @Test
    public void testSetFilterSimpleFilterAppendsCorrectly() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertFalse(filterList.isEmpty(), "Filter list should not be empty");
        assertTrue(filterList.contains("com.example.**"),
            "Filter list should contain the exact filter string");
    }

    /**
     * Test that setFilter accepts null value.
     */
    @Test
    public void testSetFilterWithNull() {
        FilterElement element = new FilterElement();

        assertDoesNotThrow(() -> element.setFilter(null),
            "Should accept null value");
    }

    /**
     * Test that setFilter with null clears the filter list when used with appendTo.
     */
    @Test
    public void testSetFilterNullClearsFilterList() {
        FilterElement element = new FilterElement();
        element.setFilter(null);

        List<String> filterList = new ArrayList<>();
        filterList.add("existing.item1");
        filterList.add("existing.item2");

        element.appendTo(filterList, false);

        assertTrue(filterList.isEmpty(),
            "Filter list should be cleared when filter is set to null");
    }

    /**
     * Test that setFilter accepts an empty string.
     */
    @Test
    public void testSetFilterWithEmptyString() {
        FilterElement element = new FilterElement();

        assertDoesNotThrow(() -> element.setFilter(""),
            "Should accept empty string");
    }

    /**
     * Test that setFilter with empty string produces an empty filter list.
     */
    @Test
    public void testSetFilterEmptyStringProducesEmptyFilterList() {
        FilterElement element = new FilterElement();
        element.setFilter("");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertTrue(filterList.isEmpty(),
            "Filter list should be empty when filter is set to empty string");
    }

    /**
     * Test that setFilter accepts comma-separated filter strings.
     */
    @Test
    public void testSetFilterWithCommaSeparatedFilters() {
        FilterElement element = new FilterElement();

        assertDoesNotThrow(() -> element.setFilter("com.example.package1.**,com.example.package2.**"),
            "Should accept comma-separated filter strings");
    }

    /**
     * Test that setFilter with comma-separated filters produces multiple entries in the list.
     */
    @Test
    public void testSetFilterCommaSeparatedFiltersProducesMultipleEntries() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.package1.**,com.example.package2.**");

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
     * Test that setFilter can be called multiple times, with the last value taking effect.
     */
    @Test
    public void testSetFilterMultipleTimes() {
        FilterElement element = new FilterElement();

        element.setFilter("com.example.first.**");
        element.setFilter("com.example.second.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertEquals(1, filterList.size(), "Filter list should contain only one entry");
        assertTrue(filterList.contains("com.example.second.**"),
            "Filter list should contain the last set value");
        assertFalse(filterList.contains("com.example.first.**"),
            "Filter list should not contain the first value");
    }

    /**
     * Test that setFilter overwrites previously set name.
     */
    @Test
    public void testSetFilterOverwritesPreviousName() {
        FilterElement element = new FilterElement();

        element.setName("com.example.name.**");
        element.setFilter("com.example.filter.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertEquals(1, filterList.size(), "Filter list should contain only one entry");
        assertTrue(filterList.contains("com.example.filter.**"),
            "Filter list should contain the filter value");
        assertFalse(filterList.contains("com.example.name.**"),
            "Filter list should not contain the name value");
    }

    /**
     * Test that setFilter works correctly with internal class name conversion.
     */
    @Test
    public void testSetFilterWithInternalClassNameConversion() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.MyClass");

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
     * Test that setFilter works correctly without internal class name conversion.
     */
    @Test
    public void testSetFilterWithoutInternalClassNameConversion() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.MyClass");

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
     * Test that setFilter with wildcard patterns works correctly.
     */
    @Test
    public void testSetFilterWithWildcardPattern() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.*");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertFalse(filterList.isEmpty(), "Filter list should not be empty");
        assertTrue(filterList.contains("com.example.*"),
            "Filter list should contain wildcard pattern");
    }

    /**
     * Test that setFilter with double wildcard patterns works correctly.
     */
    @Test
    public void testSetFilterWithDoubleWildcardPattern() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertFalse(filterList.isEmpty(), "Filter list should not be empty");
        assertTrue(filterList.contains("com.example.**"),
            "Filter list should contain double wildcard pattern");
    }

    /**
     * Test that setFilter with special characters works correctly.
     */
    @Test
    public void testSetFilterWithSpecialCharacters() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.!public,com.example.!private");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertEquals(2, filterList.size(), "Filter list should contain 2 entries");
        assertTrue(filterList.contains("com.example.!public"),
            "Filter list should contain negation pattern");
        assertTrue(filterList.contains("com.example.!private"),
            "Filter list should contain second negation pattern");
    }

    /**
     * Test that setFilter can be called on an element that is part of an Ant project.
     */
    @Test
    public void testSetFilterWithAntProject() {
        FilterElement element = new FilterElement();
        Project project = new Project();
        project.init();
        element.setProject(project);

        assertDoesNotThrow(() -> element.setFilter("com.example.**"),
            "Should work with Ant project");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertFalse(filterList.isEmpty(), "Filter list should be populated");
    }

    /**
     * Test that setFilter with whitespace-containing strings works correctly.
     */
    @Test
    public void testSetFilterWithWhitespace() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.package1.**, com.example.package2.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertEquals(2, filterList.size(), "Filter list should contain 2 entries");
        // Note: whitespace after comma is trimmed by ListUtil.commaSeparatedList
    }

    /**
     * Test that setFilter followed by null resets the filter.
     */
    @Test
    public void testSetFilterFollowedByNull() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.**");
        element.setFilter(null);

        List<String> filterList = new ArrayList<>();
        filterList.add("existing.item");
        element.appendTo(filterList, false);

        assertTrue(filterList.isEmpty(),
            "Filter list should be cleared when filter is reset to null");
    }

    /**
     * Test that setFilter can handle complex filter patterns with multiple wildcards.
     */
    @Test
    public void testSetFilterWithComplexFilterPattern() {
        FilterElement element = new FilterElement();
        element.setFilter("com.*.example.**,org.**.test.*");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertEquals(2, filterList.size(), "Filter list should contain 2 entries");
        assertTrue(filterList.contains("com.*.example.**"),
            "Filter list should contain first complex pattern");
        assertTrue(filterList.contains("org.**.test.*"),
            "Filter list should contain second complex pattern");
    }

    /**
     * Test that setFilter with a single wildcard works correctly.
     */
    @Test
    public void testSetFilterWithSingleWildcard() {
        FilterElement element = new FilterElement();
        element.setFilter("*");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertFalse(filterList.isEmpty(), "Filter list should not be empty");
        assertTrue(filterList.contains("*"), "Filter list should contain single wildcard");
    }

    /**
     * Test that setFilter can be used multiple times with different patterns.
     */
    @Test
    public void testSetFilterMultipleTimesWithDifferentPatterns() {
        FilterElement element = new FilterElement();
        List<String> filterList1 = new ArrayList<>();
        List<String> filterList2 = new ArrayList<>();

        element.setFilter("com.example.first.**");
        element.appendTo(filterList1, false);

        element.setFilter("com.example.second.**");
        element.appendTo(filterList2, false);

        assertTrue(filterList1.contains("com.example.first.**"),
            "First filter list should contain first pattern");
        assertTrue(filterList2.contains("com.example.second.**"),
            "Second filter list should contain second pattern");
        assertFalse(filterList2.contains("com.example.first.**"),
            "Second filter list should not contain first pattern");
    }

    /**
     * Test that setFilter works correctly when the filter contains only commas.
     */
    @Test
    public void testSetFilterWithOnlyCommas() {
        FilterElement element = new FilterElement();
        element.setFilter(",,,");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        // ListUtil.commaSeparatedList should handle this - empty strings are typically filtered out
        assertTrue(filterList.isEmpty() || filterList.stream().allMatch(String::isEmpty),
            "Filter list should be empty or contain only empty strings");
    }

    /**
     * Test that setFilter works correctly with internal name conversion for complex patterns.
     */
    @Test
    public void testSetFilterWithInternalConversionComplexPattern() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, true);

        assertFalse(filterList.isEmpty(), "Filter list should not be empty");
        assertTrue(filterList.get(0).contains("/"),
            "Internal conversion should convert dots to slashes");
        assertEquals("com/example/**", filterList.get(0),
            "Internal conversion should produce correct pattern");
    }

    /**
     * Test that setFilter and setName are interchangeable.
     */
    @Test
    public void testSetFilterAndSetNameAreInterchangeable() {
        FilterElement element1 = new FilterElement();
        FilterElement element2 = new FilterElement();

        element1.setFilter("com.example.**");
        element2.setName("com.example.**");

        List<String> filterList1 = new ArrayList<>();
        List<String> filterList2 = new ArrayList<>();

        element1.appendTo(filterList1, false);
        element2.appendTo(filterList2, false);

        assertEquals(filterList1, filterList2,
            "setFilter and setName should produce identical results");
    }

    /**
     * Test that setFilter with three comma-separated filters works correctly.
     */
    @Test
    public void testSetFilterWithThreeFilters() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.a.**,com.example.b.**,com.example.c.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertEquals(3, filterList.size(), "Filter list should contain 3 entries");
        assertTrue(filterList.contains("com.example.a.**"),
            "Filter list should contain first filter");
        assertTrue(filterList.contains("com.example.b.**"),
            "Filter list should contain second filter");
        assertTrue(filterList.contains("com.example.c.**"),
            "Filter list should contain third filter");
    }

    /**
     * Test that setFilter with mixed wildcards and concrete names.
     */
    @Test
    public void testSetFilterWithMixedWildcardsAndConcreteNames() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.ConcreteClass,com.example.*,com.example.**");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertEquals(3, filterList.size(), "Filter list should contain 3 entries");
        assertTrue(filterList.contains("com.example.ConcreteClass"),
            "Filter list should contain concrete class");
        assertTrue(filterList.contains("com.example.*"),
            "Filter list should contain single wildcard");
        assertTrue(filterList.contains("com.example.**"),
            "Filter list should contain double wildcard");
    }

    /**
     * Test that setFilter with internal conversion handles multiple patterns.
     */
    @Test
    public void testSetFilterInternalConversionWithMultiplePatterns() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.First,org.test.Second");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, true);

        assertEquals(2, filterList.size(), "Filter list should contain 2 entries");
        assertTrue(filterList.contains("com/example/First"),
            "First pattern should be converted to internal format");
        assertTrue(filterList.contains("org/test/Second"),
            "Second pattern should be converted to internal format");
    }

    /**
     * Test that setFilter with leading/trailing whitespace in comma-separated list.
     */
    @Test
    public void testSetFilterWithLeadingTrailingWhitespace() {
        FilterElement element = new FilterElement();
        element.setFilter(" com.example.first.** , com.example.second.** ");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertEquals(2, filterList.size(), "Filter list should contain 2 entries");
        // ListUtil.commaSeparatedList trims whitespace
    }

    /**
     * Test that setFilter stores the value that can be retrieved via appendTo.
     */
    @Test
    public void testSetFilterValueIsPreserved() {
        FilterElement element = new FilterElement();
        String expectedFilter = "com.example.test.**";
        element.setFilter(expectedFilter);

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertTrue(filterList.contains(expectedFilter),
            "The exact filter value should be preserved in the list");
    }

    /**
     * Test that setFilter with question mark wildcard.
     */
    @Test
    public void testSetFilterWithQuestionMarkWildcard() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.?Class");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertFalse(filterList.isEmpty(), "Filter list should not be empty");
        assertTrue(filterList.contains("com.example.?Class"),
            "Filter list should contain question mark wildcard");
    }

    /**
     * Test that setFilter handles filters with multiple consecutive wildcards.
     */
    @Test
    public void testSetFilterWithConsecutiveWildcards() {
        FilterElement element = new FilterElement();
        element.setFilter("com.example.***");

        List<String> filterList = new ArrayList<>();
        element.appendTo(filterList, false);

        assertFalse(filterList.isEmpty(), "Filter list should not be empty");
        assertTrue(filterList.contains("com.example.***"),
            "Filter list should preserve consecutive wildcards");
    }
}
