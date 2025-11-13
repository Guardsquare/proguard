package proguard.retrace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class FramePatternDiffblueTest {
  /**
   * Test {@link FramePattern#FramePattern(String, boolean)}.
   *
   * <p>Method under test: {@link FramePattern#FramePattern(String, boolean)}
   */
  @Test
  @DisplayName("Test new FramePattern(String, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void FramePattern.<init>(String, boolean)"})
  void testNewFramePattern() {
    // Arrange, Act and Assert
    assertNull(new FramePattern("Regular Expression", true).parse("Line"));
  }

  /**
   * Test {@link FramePattern#parse(String)}.
   *
   * <ul>
   *   <li>When {@code Line}.
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link FramePattern#parse(String)}
   */
  @Test
  @DisplayName("Test parse(String); when 'Line'; then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"FrameInfo FramePattern.parse(String)"})
  void testParse_whenLine_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(new FramePattern("Regular Expression", true).parse("Line"));
  }

  /**
   * Test {@link FramePattern#parse(String)}.
   *
   * <ul>
   *   <li>When {@code Regular Expression}.
   *   <li>Then return Arguments is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link FramePattern#parse(String)}
   */
  @Test
  @DisplayName("Test parse(String); when 'Regular Expression'; then return Arguments is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"FrameInfo FramePattern.parse(String)"})
  void testParse_whenRegularExpression_thenReturnArgumentsIsNull() {
    // Arrange and Act
    FrameInfo actualParseResult =
        new FramePattern("Regular Expression", true).parse("Regular Expression");

    // Assert
    assertNull(actualParseResult.getArguments());
    assertNull(actualParseResult.getClassName());
    assertNull(actualParseResult.getFieldName());
    assertNull(actualParseResult.getMethodName());
    assertNull(actualParseResult.getSourceFile());
    assertNull(actualParseResult.getType());
    assertEquals(0, actualParseResult.getLineNumber());
  }

  /**
   * Test {@link FramePattern#format(String, FrameInfo)}.
   *
   * <ul>
   *   <li>When {@code Line}.
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link FramePattern#format(String, FrameInfo)}
   */
  @Test
  @DisplayName("Test format(String, FrameInfo); when 'Line'; then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String FramePattern.format(String, FrameInfo)"})
  void testFormat_whenLine_thenReturnNull() {
    // Arrange
    FramePattern framePattern = new FramePattern("Regular Expression", true);
    FrameInfo frameInfo =
        new FrameInfo(
            "Class Name", "Source File", 2, "Type", "Field Name", "Method Name", "Arguments");

    // Act and Assert
    assertNull(framePattern.format("Line", frameInfo));
  }

  /**
   * Test {@link FramePattern#format(String, FrameInfo)}.
   *
   * <ul>
   *   <li>When {@code Regular Expression}.
   *   <li>Then return {@code Regular Expression}.
   * </ul>
   *
   * <p>Method under test: {@link FramePattern#format(String, FrameInfo)}
   */
  @Test
  @DisplayName(
      "Test format(String, FrameInfo); when 'Regular Expression'; then return 'Regular Expression'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String FramePattern.format(String, FrameInfo)"})
  void testFormat_whenRegularExpression_thenReturnRegularExpression() {
    // Arrange
    FramePattern framePattern = new FramePattern("Regular Expression", true);
    FrameInfo frameInfo =
        new FrameInfo(
            "Class Name", "Source File", 2, "Type", "Field Name", "Method Name", "Arguments");

    // Act and Assert
    assertEquals("Regular Expression", framePattern.format("Regular Expression", frameInfo));
  }
}
