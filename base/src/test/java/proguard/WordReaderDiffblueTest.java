package proguard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class WordReaderDiffblueTest {
  /**
   * Test {@link WordReader#setBaseDir(File)}.
   *
   * <p>Method under test: {@link WordReader#setBaseDir(File)}
   */
  @Test
  @DisplayName("Test setBaseDir(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void WordReader.setBaseDir(File)"})
  void testSetBaseDir() throws IOException {
    // Arrange
    FileWordReader fileWordReader =
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
    File baseDir = Configuration.STD_OUT;

    // Act
    fileWordReader.setBaseDir(baseDir);

    // Assert
    assertSame(baseDir, fileWordReader.getBaseDir());
  }

  /**
   * Test {@link WordReader#setBaseDir(File)}.
   *
   * <p>Method under test: {@link WordReader#setBaseDir(File)}
   */
  @Test
  @DisplayName("Test setBaseDir(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void WordReader.setBaseDir(File)"})
  void testSetBaseDir2() throws IOException {
    // Arrange
    FileWordReader fileWordReader =
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
    String[] arguments = new String[] {"Arguments"};
    ArgumentWordReader newIncludeWordReader =
        new ArgumentWordReader(arguments, Configuration.STD_OUT);
    fileWordReader.includeWordReader(newIncludeWordReader);
    File baseDir = Configuration.STD_OUT;

    // Act
    fileWordReader.setBaseDir(baseDir);

    // Assert that nothing has changed
    assertSame(baseDir, fileWordReader.getBaseDir());
  }

  /**
   * Test {@link WordReader#getBaseDir()}.
   *
   * <ul>
   *   <li>Then return Name is empty string.
   * </ul>
   *
   * <p>Method under test: {@link WordReader#getBaseDir()}
   */
  @Test
  @DisplayName("Test getBaseDir(); then return Name is empty string")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"File WordReader.getBaseDir()"})
  void testGetBaseDir_thenReturnNameIsEmptyString() throws IOException {
    // Arrange
    FileWordReader fileWordReader =
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
    String[] arguments = new String[] {"Arguments"};
    ArgumentWordReader newIncludeWordReader =
        new ArgumentWordReader(arguments, Configuration.STD_OUT);
    fileWordReader.includeWordReader(newIncludeWordReader);

    // Act
    File actualBaseDir = fileWordReader.getBaseDir();

    // Assert
    assertEquals("", actualBaseDir.getName());
    assertFalse(actualBaseDir.isAbsolute());
  }

  /**
   * Test {@link WordReader#getBaseDir()}.
   *
   * <ul>
   *   <li>Then return Name is {@code Temp}.
   * </ul>
   *
   * <p>Method under test: {@link WordReader#getBaseDir()}
   */
  @Test
  @DisplayName("Test getBaseDir(); then return Name is 'Temp'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"File WordReader.getBaseDir()"})
  void testGetBaseDir_thenReturnNameIsTemp() throws IOException {
    // Arrange and Act
    File actualBaseDir =
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile())
            .getBaseDir();

    // Assert
    assertEquals("Temp", actualBaseDir.getName());
    assertTrue(actualBaseDir.isAbsolute());
  }

  /**
   * Test {@link WordReader#getBaseURL()}.
   *
   * <p>Method under test: {@link WordReader#getBaseURL()}
   */
  @Test
  @DisplayName("Test getBaseURL()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.net.URL WordReader.getBaseURL()"})
  void testGetBaseURL() throws IOException {
    // Arrange, Act and Assert
    assertNull(
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile())
            .getBaseURL());
  }

  /**
   * Test {@link WordReader#getBaseURL()}.
   *
   * <p>Method under test: {@link WordReader#getBaseURL()}
   */
  @Test
  @DisplayName("Test getBaseURL()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.net.URL WordReader.getBaseURL()"})
  void testGetBaseURL2() throws IOException {
    // Arrange
    FileWordReader fileWordReader =
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
    String[] arguments = new String[] {"Arguments"};
    ArgumentWordReader newIncludeWordReader =
        new ArgumentWordReader(arguments, Configuration.STD_OUT);
    fileWordReader.includeWordReader(newIncludeWordReader);

    // Act and Assert
    assertNull(fileWordReader.getBaseURL());
  }

  /**
   * Test {@link WordReader#nextWord(boolean, boolean)}.
   *
   * <p>Method under test: {@link WordReader#nextWord(boolean, boolean)}
   */
  @Test
  @DisplayName("Test nextWord(boolean, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String WordReader.nextWord(boolean, boolean)"})
  void testNextWord() throws IOException {
    // Arrange
    FileWordReader fileWordReader =
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
    fileWordReader.includeWordReader(
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile()));

    // Act and Assert
    assertEquals("foo", fileWordReader.nextWord(true, true));
  }

  /**
   * Test {@link WordReader#nextWord(boolean, boolean)}.
   *
   * <p>Method under test: {@link WordReader#nextWord(boolean, boolean)}
   */
  @Test
  @DisplayName("Test nextWord(boolean, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String WordReader.nextWord(boolean, boolean)"})
  void testNextWord2() throws IOException {
    // Arrange
    FileWordReader fileWordReader =
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
    fileWordReader.includeWordReader(
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "foo").toFile()));

    // Act and Assert
    assertEquals("foo", fileWordReader.nextWord(true, true));
  }

  /**
   * Test {@link WordReader#nextWord(boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link FileWordReader#FileWordReader(File)} with file is Property is {@code
   *       java.io.tmpdir} is {@code test.txt} toFile.
   * </ul>
   *
   * <p>Method under test: {@link WordReader#nextWord(boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test nextWord(boolean, boolean); given FileWordReader(File) with file is Property is 'java.io.tmpdir' is 'test.txt' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String WordReader.nextWord(boolean, boolean)"})
  void testNextWord_givenFileWordReaderWithFileIsPropertyIsJavaIoTmpdirIsTestTxtToFile()
      throws IOException {
    // Arrange, Act and Assert
    assertEquals(
        "foo",
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile())
            .nextWord(true, true));
  }

  /**
   * Test {@link WordReader#nextWord(boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link FileWordReader#FileWordReader(File)} with file is Property is {@code
   *       java.io.tmpdir} is {@code test.txt} toFile.
   * </ul>
   *
   * <p>Method under test: {@link WordReader#nextWord(boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test nextWord(boolean, boolean); given FileWordReader(File) with file is Property is 'java.io.tmpdir' is 'test.txt' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String WordReader.nextWord(boolean, boolean)"})
  void testNextWord_givenFileWordReaderWithFileIsPropertyIsJavaIoTmpdirIsTestTxtToFile2()
      throws IOException {
    // Arrange, Act and Assert
    assertEquals(
        "foo",
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile())
            .nextWord(false, true));
  }

  /**
   * Test {@link WordReader#nextWord(boolean, boolean)}.
   *
   * <ul>
   *   <li>Given {@link FileWordReader#FileWordReader(File)} with file is Property is {@code
   *       java.io.tmpdir} is {@code test.txt} toFile.
   * </ul>
   *
   * <p>Method under test: {@link WordReader#nextWord(boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test nextWord(boolean, boolean); given FileWordReader(File) with file is Property is 'java.io.tmpdir' is 'test.txt' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String WordReader.nextWord(boolean, boolean)"})
  void testNextWord_givenFileWordReaderWithFileIsPropertyIsJavaIoTmpdirIsTestTxtToFile3()
      throws IOException {
    // Arrange, Act and Assert
    assertEquals(
        "foo",
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile())
            .nextWord(true, false));
  }

  /**
   * Test {@link WordReader#nextWord(boolean, boolean)}.
   *
   * <ul>
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link WordReader#nextWord(boolean, boolean)}
   */
  @Test
  @DisplayName("Test nextWord(boolean, boolean); then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String WordReader.nextWord(boolean, boolean)"})
  void testNextWord_thenReturnNull() throws IOException {
    // Arrange, Act and Assert
    assertNull(
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "foo").toFile())
            .nextWord(true, true));
  }

  /**
   * Test {@link WordReader#lastComments()}.
   *
   * <p>Method under test: {@link WordReader#lastComments()}
   */
  @Test
  @DisplayName("Test lastComments()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String WordReader.lastComments()"})
  void testLastComments() throws IOException {
    // Arrange, Act and Assert
    assertNull(
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile())
            .lastComments());
  }

  /**
   * Test {@link WordReader#lastComments()}.
   *
   * <p>Method under test: {@link WordReader#lastComments()}
   */
  @Test
  @DisplayName("Test lastComments()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String WordReader.lastComments()"})
  void testLastComments2() throws IOException {
    // Arrange
    FileWordReader fileWordReader =
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
    String[] arguments = new String[] {"Arguments"};
    ArgumentWordReader newIncludeWordReader =
        new ArgumentWordReader(arguments, Configuration.STD_OUT);
    fileWordReader.includeWordReader(newIncludeWordReader);

    // Act and Assert
    assertNull(fileWordReader.lastComments());
  }

  /**
   * Test {@link WordReader#locationDescription()}.
   *
   * <p>Method under test: {@link WordReader#locationDescription()}
   */
  @Test
  @DisplayName("Test locationDescription()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String WordReader.locationDescription()"})
  void testLocationDescription() throws IOException {
    // Arrange and Act
    String actualLocationDescriptionResult =
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile())
            .locationDescription();

    // Assert
    String expectedLocationDescriptionResult =
        String.join(
            "",
            "end of line 0 of file '",
            Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toString(),
            "'");
    assertEquals(expectedLocationDescriptionResult, actualLocationDescriptionResult);
  }

  /**
   * Test {@link WordReader#locationDescription()}.
   *
   * <p>Method under test: {@link WordReader#locationDescription()}
   */
  @Test
  @DisplayName("Test locationDescription()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String WordReader.locationDescription()"})
  void testLocationDescription2() throws IOException {
    // Arrange
    FileWordReader fileWordReader =
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
    fileWordReader.includeWordReader(
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile()));

    // Act
    String actualLocationDescriptionResult = fileWordReader.locationDescription();

    // Assert
    String expectedLocationDescriptionResult =
        String.join(
            "",
            "end of line 0 of file '",
            Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toString(),
            "',\n  included from line 0 of file '",
            Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toString(),
            "'");
    assertEquals(expectedLocationDescriptionResult, actualLocationDescriptionResult);
  }

  /**
   * Test {@link WordReader#close()}.
   *
   * <p>Method under test: {@link WordReader#close()}
   */
  @Test
  @DisplayName("Test close()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void WordReader.close()"})
  void testClose() throws IOException {
    // Arrange
    String[] arguments = new String[] {"Arguments"};
    ArgumentWordReader argumentWordReader =
        new ArgumentWordReader(arguments, Configuration.STD_OUT);

    // Act
    argumentWordReader.close();

    // Assert that nothing has changed
    File baseDir = argumentWordReader.getBaseDir();
    assertEquals("", baseDir.getName());
    assertFalse(baseDir.isAbsolute());
  }

  /**
   * Test {@link WordReader#close()}.
   *
   * <p>Method under test: {@link WordReader#close()}
   */
  @Test
  @DisplayName("Test close()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void WordReader.close()"})
  void testClose2() throws IOException {
    // Arrange
    String[] arguments = new String[] {"Arguments"};

    ArgumentWordReader argumentWordReader =
        new ArgumentWordReader(arguments, Configuration.STD_OUT);
    argumentWordReader.includeWordReader(
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile()));

    // Act
    argumentWordReader.close();

    // Assert
    File baseDir = argumentWordReader.getBaseDir();
    assertEquals("", baseDir.getName());
    assertFalse(baseDir.isAbsolute());
  }
}
