package proguard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class FileWordReaderDiffblueTest {
  /**
   * Test {@link FileWordReader#FileWordReader(URL)}.
   *
   * <p>Method under test: {@link FileWordReader#FileWordReader(URL)}
   */
  @Test
  @DisplayName("Test new FileWordReader(URL)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void FileWordReader.<init>(URL)"})
  void testNewFileWordReader() throws IOException {
    // Arrange
    URL url = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toUri().toURL();

    // Act
    FileWordReader actualFileWordReader = new FileWordReader(url);

    // Assert
    assertNull(actualFileWordReader.getBaseDir());
    assertSame(url, actualFileWordReader.getBaseURL());
  }

  /**
   * Test {@link FileWordReader#FileWordReader(File)}.
   *
   * <ul>
   *   <li>Then return BaseDir Name is {@code Temp}.
   * </ul>
   *
   * <p>Method under test: {@link FileWordReader#FileWordReader(File)}
   */
  @Test
  @DisplayName("Test new FileWordReader(File); then return BaseDir Name is 'Temp'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void FileWordReader.<init>(File)"})
  void testNewFileWordReader_thenReturnBaseDirNameIsTemp() throws IOException {
    // Arrange and Act
    FileWordReader actualFileWordReader =
        new FileWordReader(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

    // Assert
    File baseDir = actualFileWordReader.getBaseDir();
    assertEquals("Temp", baseDir.getName());
    assertNull(actualFileWordReader.getBaseURL());
    assertTrue(baseDir.isAbsolute());
  }
}
