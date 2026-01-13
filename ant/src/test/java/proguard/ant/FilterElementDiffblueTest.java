package proguard.ant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class FilterElementDiffblueTest {
  /**
   * Test {@link FilterElement#appendTo(List, boolean)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ArrayList#ArrayList()} add {@code 42}.
   *   <li>Then {@link ArrayList#ArrayList()} size is two.
   * </ul>
   *
   * <p>Method under test: {@link FilterElement#appendTo(List, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean); given '42'; when ArrayList() add '42'; then ArrayList() size is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void FilterElement.appendTo(List, boolean)"})
  void testAppendTo_given42_whenArrayListAdd42_thenArrayListSizeIsTwo() {
    // Arrange
    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    ArrayList<Object> filter = new ArrayList<>();
    filter.add("42");

    // Act
    filterElement.appendTo(filter, true);

    // Assert
    assertEquals(2, filter.size());
    assertEquals("42", filter.get(0));
    assertEquals("Name", filter.get(1));
  }

  /**
   * Test {@link FilterElement#appendTo(List, boolean)}.
   *
   * <ul>
   *   <li>Given {@link FilterElement} (default constructor) Name is {@code Name}.
   *   <li>When {@link ArrayList#ArrayList()}.
   *   <li>Then {@link ArrayList#ArrayList()} size is one.
   * </ul>
   *
   * <p>Method under test: {@link FilterElement#appendTo(List, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean); given FilterElement (default constructor) Name is 'Name'; when ArrayList(); then ArrayList() size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void FilterElement.appendTo(List, boolean)"})
  void testAppendTo_givenFilterElementNameIsName_whenArrayList_thenArrayListSizeIsOne() {
    // Arrange
    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");
    ArrayList<Object> filter = new ArrayList<>();

    // Act
    filterElement.appendTo(filter, true);

    // Assert
    assertEquals(1, filter.size());
    assertEquals("Name", filter.get(0));
  }

  /**
   * Test {@link FilterElement#appendTo(List, boolean)}.
   *
   * <ul>
   *   <li>Given {@link FilterElement} (default constructor) Name is {@code Name}.
   *   <li>When {@code false}.
   *   <li>Then {@link ArrayList#ArrayList()} size is one.
   * </ul>
   *
   * <p>Method under test: {@link FilterElement#appendTo(List, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean); given FilterElement (default constructor) Name is 'Name'; when 'false'; then ArrayList() size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void FilterElement.appendTo(List, boolean)"})
  void testAppendTo_givenFilterElementNameIsName_whenFalse_thenArrayListSizeIsOne() {
    // Arrange
    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");
    ArrayList<Object> filter = new ArrayList<>();

    // Act
    filterElement.appendTo(filter, false);

    // Assert
    assertEquals(1, filter.size());
    assertEquals("Name", filter.get(0));
  }

  /**
   * Test {@link FilterElement#appendTo(List, boolean)}.
   *
   * <ul>
   *   <li>Given {@link FilterElement} (default constructor).
   *   <li>When {@link ArrayList#ArrayList()}.
   *   <li>Then {@link ArrayList#ArrayList()} Empty.
   * </ul>
   *
   * <p>Method under test: {@link FilterElement#appendTo(List, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendTo(List, boolean); given FilterElement (default constructor); when ArrayList(); then ArrayList() Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void FilterElement.appendTo(List, boolean)"})
  void testAppendTo_givenFilterElement_whenArrayList_thenArrayListEmpty() {
    // Arrange
    FilterElement filterElement = new FilterElement();
    ArrayList<Object> filter = new ArrayList<>();

    // Act
    filterElement.appendTo(filter, true);

    // Assert that nothing has changed
    assertTrue(filter.isEmpty());
  }

  /**
   * Test new {@link FilterElement} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link FilterElement}
   */
  @Test
  @DisplayName("Test new FilterElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void FilterElement.<init>()"})
  void testNewFilterElement() {
    // Arrange and Act
    FilterElement actualFilterElement = new FilterElement();

    // Assert
    Location location = actualFilterElement.getLocation();
    assertNull(location.getFileName());
    assertNull(actualFilterElement.getDescription());
    assertNull(actualFilterElement.getProject());
    assertNull(actualFilterElement.getRefid());
    assertEquals(0, location.getColumnNumber());
    assertEquals(0, location.getLineNumber());
    assertFalse(actualFilterElement.isReference());
  }
}
