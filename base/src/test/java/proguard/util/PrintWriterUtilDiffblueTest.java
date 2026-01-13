package proguard.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.File;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.Configuration;

class PrintWriterUtilDiffblueTest {
  /**
   * Test {@link PrintWriterUtil#fileName(File)}.
   *
   * <ul>
   *   <li>Then return Property is {@code java.io.tmpdir} is array of {@link String} with {@code
   *       test.txt} toString.
   * </ul>
   *
   * <p>Method under test: {@link PrintWriterUtil#fileName(File)}
   */
  @Test
  @DisplayName(
      "Test fileName(File); then return Property is 'java.io.tmpdir' is array of String with 'test.txt' toString")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String PrintWriterUtil.fileName(File)"})
  void testFileName_thenReturnPropertyIsJavaIoTmpdirIsArrayOfStringWithTestTxtToString() {
    // Arrange, Act and Assert
    assertEquals(
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toString(),
        PrintWriterUtil.fileName(
            Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile()));
  }

  /**
   * Test {@link PrintWriterUtil#fileName(File)}.
   *
   * <ul>
   *   <li>When {@link Configuration#STD_OUT}.
   *   <li>Then return {@code standard output}.
   * </ul>
   *
   * <p>Method under test: {@link PrintWriterUtil#fileName(File)}
   */
  @Test
  @DisplayName("Test fileName(File); when STD_OUT; then return 'standard output'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String PrintWriterUtil.fileName(File)"})
  void testFileName_whenStd_out_thenReturnStandardOutput() {
    // Arrange, Act and Assert
    assertEquals("standard output", PrintWriterUtil.fileName(Configuration.STD_OUT));
  }
}
