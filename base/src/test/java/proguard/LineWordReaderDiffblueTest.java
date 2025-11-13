package proguard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class LineWordReaderDiffblueTest {
  /**
   * Test {@link LineWordReader#LineWordReader(LineNumberReader, String, File)}.
   *
   * <ul>
   *   <li>When {@link Configuration#STD_OUT}.
   *   <li>Then return BaseURL is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link LineWordReader#LineWordReader(LineNumberReader, String, File)}
   */
  @Test
  @DisplayName(
      "Test new LineWordReader(LineNumberReader, String, File); when STD_OUT; then return BaseURL is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LineWordReader.<init>(LineNumberReader, String, File)",
    "void LineWordReader.<init>(LineNumberReader, String, java.net.URL)"
  })
  void testNewLineWordReader_whenStd_out_thenReturnBaseURLIsNull() throws IOException {
    // Arrange
    LineNumberReader lineNumberReader = new LineNumberReader(new StringReader("foo"), 1);
    File baseDir = Configuration.STD_OUT;

    // Act
    LineWordReader actualLineWordReader =
        new LineWordReader(
            lineNumberReader, "The characteristics of someone or something", baseDir);

    // Assert
    assertNull(actualLineWordReader.getBaseURL());
    assertSame(baseDir, actualLineWordReader.getBaseDir());
  }

  /**
   * Test {@link LineWordReader#nextLine()}.
   *
   * <ul>
   *   <li>Given {@link StringReader#StringReader(String)} with {@code foo}.
   *   <li>Then return {@code foo}.
   * </ul>
   *
   * <p>Method under test: {@link LineWordReader#nextLine()}
   */
  @Test
  @DisplayName("Test nextLine(); given StringReader(String) with 'foo'; then return 'foo'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String LineWordReader.nextLine()"})
  void testNextLine_givenStringReaderWithFoo_thenReturnFoo() throws IOException {
    // Arrange
    LineNumberReader lineNumberReader = new LineNumberReader(new StringReader("foo"), 1);
    LineWordReader lineWordReader =
        new LineWordReader(
            lineNumberReader, "The characteristics of someone or something", Configuration.STD_OUT);

    // Act and Assert
    assertEquals("foo", lineWordReader.nextLine());
  }

  /**
   * Test {@link LineWordReader#lineLocationDescription()}.
   *
   * <p>Method under test: {@link LineWordReader#lineLocationDescription()}
   */
  @Test
  @DisplayName("Test lineLocationDescription()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String LineWordReader.lineLocationDescription()"})
  void testLineLocationDescription() throws IOException {
    // Arrange
    LineNumberReader lineNumberReader = new LineNumberReader(new StringReader("foo"), 1);
    LineWordReader lineWordReader =
        new LineWordReader(
            lineNumberReader, "The characteristics of someone or something", Configuration.STD_OUT);

    // Act and Assert
    assertEquals(
        "line 0 of The characteristics of someone or something",
        lineWordReader.lineLocationDescription());
  }
}
