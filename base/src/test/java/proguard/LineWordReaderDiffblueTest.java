package proguard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Paths;
import org.jetbrains.kotlin.com.fasterxml.aalto.in.Utf32Reader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class LineWordReaderDiffblueTest {
  /**
   * Test {@link LineWordReader#LineWordReader(LineNumberReader, String, URL)}.
   *
   * <p>Method under test: {@link LineWordReader#LineWordReader(LineNumberReader, String, URL)}
   */
  @Test
  @DisplayName("Test new LineWordReader(LineNumberReader, String, URL)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void LineWordReader.<init>(LineNumberReader, String, File)",
    "void LineWordReader.<init>(LineNumberReader, String, URL)"
  })
  void testNewLineWordReader() throws IOException {
    // Arrange
    LineNumberReader lineNumberReader = new LineNumberReader(new StringReader("foo"), 1);
    URL baseURL = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();

    // Act
    LineWordReader actualLineWordReader =
        new LineWordReader(
            lineNumberReader, "The characteristics of someone or something", baseURL);

    // Assert
    URL baseURL2 = actualLineWordReader.getBaseURL();
    assertEquals("file:/C:/Users/afry/AppData/Local/Temp/test.txt", baseURL2.toString());
    assertNull(actualLineWordReader.getBaseDir());
    assertSame(baseURL, baseURL2);
  }

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
    "void LineWordReader.<init>(LineNumberReader, String, URL)"
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

  /**
   * Test {@link LineWordReader#close()}.
   *
   * <p>Method under test: {@link LineWordReader#close()}
   */
  @Test
  @DisplayName("Test close()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LineWordReader.close()"})
  void testClose() throws IOException {
    // Arrange
    LineNumberReader lineNumberReader = new LineNumberReader(new StringReader("foo"), 1);
    LineWordReader lineWordReader =
        new LineWordReader(
            lineNumberReader, "The characteristics of someone or something", Configuration.STD_OUT);

    // Act
    lineWordReader.close();

    // Assert that nothing has changed
    File baseDir = lineWordReader.getBaseDir();
    assertEquals("", baseDir.getName());
    assertFalse(baseDir.isAbsolute());
  }

  /**
   * Test {@link LineWordReader#close()}.
   *
   * <p>Method under test: {@link LineWordReader#close()}
   */
  @Test
  @DisplayName("Test close()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LineWordReader.close()"})
  void testClose2() throws IOException {
    // Arrange
    LineWordReader lineWordReader =
        new LineWordReader(
            null, "The characteristics of someone or something", Configuration.STD_OUT);

    // Act
    lineWordReader.close();

    // Assert that nothing has changed
    File baseDir = lineWordReader.getBaseDir();
    assertEquals("", baseDir.getName());
    assertFalse(baseDir.isAbsolute());
  }

  /**
   * Test {@link LineWordReader#close()}.
   *
   * <p>Method under test: {@link LineWordReader#close()}
   */
  @Test
  @DisplayName("Test close()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LineWordReader.close()"})
  void testClose3() throws IOException {
    // Arrange
    LineWordReader lineWordReader =
        new LineWordReader(
            null, "The characteristics of someone or something", Configuration.STD_OUT);
    lineWordReader.includeWordReader(
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile()));

    // Act
    lineWordReader.close();

    // Assert
    File baseDir = lineWordReader.getBaseDir();
    assertEquals("", baseDir.getName());
    assertFalse(baseDir.isAbsolute());
  }

  /**
   * Test {@link LineWordReader#close()}.
   *
   * <p>Method under test: {@link LineWordReader#close()}
   */
  @Test
  @DisplayName("Test close()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LineWordReader.close()"})
  void testClose4() throws IOException {
    // Arrange
    ArgumentWordReader newIncludeWordReader = mock(ArgumentWordReader.class);
    doNothing().when(newIncludeWordReader).close();

    LineWordReader lineWordReader =
        new LineWordReader(
            null, "The characteristics of someone or something", Configuration.STD_OUT);
    lineWordReader.includeWordReader(newIncludeWordReader);
    Paths.get(System.getProperty("java.io.tmpdir"), "test.txt");

    // Act
    lineWordReader.close();

    // Assert
    verify(newIncludeWordReader).close();
    File baseDir = lineWordReader.getBaseDir();
    assertEquals("", baseDir.getName());
    assertFalse(baseDir.isAbsolute());
  }

  /**
   * Test {@link LineWordReader#close()}.
   *
   * <ul>
   *   <li>Given {@link ArgumentWordReader} {@link ArgumentWordReader#close()} throw {@link
   *       IOException#IOException()}.
   *   <li>Then throw {@link IOException}.
   * </ul>
   *
   * <p>Method under test: {@link LineWordReader#close()}
   */
  @Test
  @DisplayName(
      "Test close(); given ArgumentWordReader close() throw IOException(); then throw IOException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LineWordReader.close()"})
  void testClose_givenArgumentWordReaderCloseThrowIOException_thenThrowIOException()
      throws IOException {
    // Arrange
    ArgumentWordReader newIncludeWordReader = mock(ArgumentWordReader.class);
    doThrow(new IOException()).when(newIncludeWordReader).close();
    LineNumberReader lineNumberReader = new LineNumberReader(mock(Utf32Reader.class), 1);

    LineWordReader lineWordReader =
        new LineWordReader(
            lineNumberReader, "The characteristics of someone or something", Configuration.STD_OUT);
    lineWordReader.includeWordReader(newIncludeWordReader);
    Paths.get(System.getProperty("java.io.tmpdir"), "test.txt");

    // Act and Assert
    assertThrows(IOException.class, () -> lineWordReader.close());
    verify(newIncludeWordReader).close();
  }
}
