package proguard.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.File;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ExtensionFileFilterDiffblueTest {
  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link ExtensionFileFilter#ExtensionFileFilter(String, String[])}
   *   <li>{@link ExtensionFileFilter#getDescription()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ExtensionFileFilter.<init>(String, String[])",
    "String ExtensionFileFilter.getDescription()"
  })
  void testGettersAndSetters() {
    // Arrange
    String[] extensions = new String[] {"Extensions"};

    // Act
    ExtensionFileFilter actualExtensionFileFilter =
        new ExtensionFileFilter("The characteristics of someone or something", extensions);

    // Assert
    assertEquals(
        "The characteristics of someone or something", actualExtensionFileFilter.getDescription());
  }

  /**
   * Test {@link ExtensionFileFilter#accept(File)}.
   *
   * <p>Method under test: {@link ExtensionFileFilter#accept(File)}
   */
  @Test
  @DisplayName("Test accept(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ExtensionFileFilter.accept(File)"})
  void testAccept() {
    // Arrange
    String[] extensions = new String[] {""};
    ExtensionFileFilter extensionFileFilter =
        new ExtensionFileFilter("The characteristics of someone or something", extensions);

    // Act
    boolean actualAcceptResult =
        extensionFileFilter.accept(
            Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

    // Assert
    assertTrue(actualAcceptResult);
  }

  /**
   * Test {@link ExtensionFileFilter#accept(File)}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ExtensionFileFilter#accept(File)}
   */
  @Test
  @DisplayName("Test accept(File); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ExtensionFileFilter.accept(File)"})
  void testAccept_thenReturnFalse() {
    // Arrange
    String[] extensions = new String[] {"Extensions"};
    ExtensionFileFilter extensionFileFilter =
        new ExtensionFileFilter("The characteristics of someone or something", extensions);

    // Act
    boolean actualAcceptResult =
        extensionFileFilter.accept(
            Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

    // Assert
    assertFalse(actualAcceptResult);
  }

  /**
   * Test {@link ExtensionFileFilter#accept(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is empty string toFile.
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ExtensionFileFilter#accept(File)}
   */
  @Test
  @DisplayName(
      "Test accept(File); when Property is 'java.io.tmpdir' is empty string toFile; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ExtensionFileFilter.accept(File)"})
  void testAccept_whenPropertyIsJavaIoTmpdirIsEmptyStringToFile_thenReturnTrue() {
    // Arrange
    String[] extensions = new String[] {"Extensions"};
    ExtensionFileFilter extensionFileFilter =
        new ExtensionFileFilter("The characteristics of someone or something", extensions);

    // Act
    boolean actualAcceptResult =
        extensionFileFilter.accept(Paths.get(System.getProperty("java.io.tmpdir"), "").toFile());

    // Assert
    assertTrue(actualAcceptResult);
  }
}
