package proguard;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
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
   * <ul>
   *   <li>Then return BaseDir is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link FileWordReader#FileWordReader(URL)}
   */
  @Test
  @DisplayName("Test new FileWordReader(URL); then return BaseDir is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void FileWordReader.<init>(URL)"})
  void testNewFileWordReader_thenReturnBaseDirIsNull() throws IOException {
    // Arrange
    URL url = Paths.get(System.getProperty("java.io.tmpdir"), "").toUri().toURL();

    // Act
    FileWordReader actualFileWordReader = new FileWordReader(url);

    // Assert
    assertNull(actualFileWordReader.getBaseDir());
    assertSame(url, actualFileWordReader.getBaseURL());
  }
}
