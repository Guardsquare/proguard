package proguard.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import javax.swing.JCheckBox;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class FilterBuilderDiffblueTest {
  /**
   * Test {@link FilterBuilder#buildFilter()}.
   *
   * <p>Method under test: {@link FilterBuilder#buildFilter()}
   */
  @Test
  @DisplayName("Test buildFilter()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String FilterBuilder.buildFilter()"})
  void testBuildFilter() {
    // Arrange
    JCheckBox[] checkBoxes = new JCheckBox[] {new JCheckBox()};
    FilterBuilder filterBuilder = new FilterBuilder(checkBoxes, 'A');

    // Act and Assert
    assertEquals("", filterBuilder.buildFilter());
  }

  /**
   * Test {@link FilterBuilder#buildFilter()}.
   *
   * <ul>
   *   <li>Given array of {@link JCheckBox} with {@link JCheckBox#JCheckBox(String, boolean)} with
   *       {@code Text} and {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link FilterBuilder#buildFilter()}
   */
  @Test
  @DisplayName(
      "Test buildFilter(); given array of JCheckBox with JCheckBox(String, boolean) with 'Text' and 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String FilterBuilder.buildFilter()"})
  void testBuildFilter_givenArrayOfJCheckBoxWithJCheckBoxWithTextAndTrue() {
    // Arrange
    JCheckBox[] checkBoxes = new JCheckBox[] {new JCheckBox("Text", true)};
    FilterBuilder filterBuilder = new FilterBuilder(checkBoxes, 'A');

    // Act and Assert
    assertEquals("", filterBuilder.buildFilter());
  }
}
