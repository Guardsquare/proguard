package proguard;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ConfigurationParserDiffblueTest {
  /**
   * Test {@link ConfigurationParser#ConfigurationParser(WordReader, Properties)}.
   *
   * <ul>
   *   <li>Given {@code Next Word}.
   *   <li>Then calls {@link ArgumentWordReader#nextWord(boolean, boolean)}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationParser#ConfigurationParser(WordReader, Properties)}
   */
  @Test
  @DisplayName(
      "Test new ConfigurationParser(WordReader, Properties); given 'Next Word'; then calls nextWord(boolean, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationParser.<init>(WordReader, Properties)"})
  void testNewConfigurationParser_givenNextWord_thenCallsNextWord() throws IOException {
    // Arrange
    ArgumentWordReader reader = mock(ArgumentWordReader.class);
    when(reader.nextWord(anyBoolean(), anyBoolean())).thenReturn("Next Word");

    // Act
    new ConfigurationParser(reader, new Properties());

    // Assert
    verify(reader).nextWord(false, false);
  }

  /**
   * Test {@link ConfigurationParser#parseClassSpecificationArguments(boolean, boolean, boolean)}
   * with {@code readFirstWord}, {@code allowClassMembers}, {@code allowValues}.
   *
   * <p>Method under test: {@link ConfigurationParser#parseClassSpecificationArguments(boolean,
   * boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test parseClassSpecificationArguments(boolean, boolean, boolean) with 'readFirstWord', 'allowClassMembers', 'allowValues'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "proguard.ClassSpecification ConfigurationParser.parseClassSpecificationArguments(boolean, boolean, boolean)"
  })
  void testParseClassSpecificationArgumentsWithReadFirstWordAllowClassMembersAllowValues()
      throws IOException, ParseException {
    // Arrange
    URL url = Paths.get(System.getProperty("java.io.tmpdir"), "").toUri().toURL();
    ConfigurationParser configurationParser = new ConfigurationParser(url, new Properties());

    // Act and Assert
    assertThrows(
        ParseException.class,
        () -> configurationParser.parseClassSpecificationArguments(true, true, true));
  }

  /**
   * Test {@link ConfigurationParser#parseClassSpecificationArguments(boolean, boolean, boolean)}
   * with {@code readFirstWord}, {@code allowClassMembers}, {@code allowValues}.
   *
   * <p>Method under test: {@link ConfigurationParser#parseClassSpecificationArguments(boolean,
   * boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test parseClassSpecificationArguments(boolean, boolean, boolean) with 'readFirstWord', 'allowClassMembers', 'allowValues'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "proguard.ClassSpecification ConfigurationParser.parseClassSpecificationArguments(boolean, boolean, boolean)"
  })
  void testParseClassSpecificationArgumentsWithReadFirstWordAllowClassMembersAllowValues2()
      throws IOException, ParseException {
    // Arrange
    URL url = Paths.get(System.getProperty("java.io.tmpdir"), "").toUri().toURL();
    ConfigurationParser configurationParser = new ConfigurationParser(url, new Properties());

    // Act and Assert
    assertThrows(
        ParseException.class,
        () -> configurationParser.parseClassSpecificationArguments(false, true, true));
  }

  /**
   * Test {@link ConfigurationParser#parseClassSpecificationArguments(boolean, boolean, boolean)}
   * with {@code readFirstWord}, {@code allowClassMembers}, {@code allowValues}.
   *
   * <p>Method under test: {@link ConfigurationParser#parseClassSpecificationArguments(boolean,
   * boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test parseClassSpecificationArguments(boolean, boolean, boolean) with 'readFirstWord', 'allowClassMembers', 'allowValues'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "proguard.ClassSpecification ConfigurationParser.parseClassSpecificationArguments(boolean, boolean, boolean)"
  })
  void testParseClassSpecificationArgumentsWithReadFirstWordAllowClassMembersAllowValues3()
      throws IOException, ParseException {
    // Arrange
    URL url =
        Paths.get(System.getProperty("java.io.tmpdir"), "2025_11_13_00_48_37_22549")
            .toUri()
            .toURL();
    ConfigurationParser configurationParser = new ConfigurationParser(url, new Properties());

    // Act and Assert
    assertThrows(
        ParseException.class,
        () -> configurationParser.parseClassSpecificationArguments(false, true, true));
  }

  /**
   * Test {@link ConfigurationParser#parseClassSpecificationArguments(boolean, boolean, boolean)}
   * with {@code readFirstWord}, {@code allowClassMembers}, {@code allowValues}.
   *
   * <p>Method under test: {@link ConfigurationParser#parseClassSpecificationArguments(boolean,
   * boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test parseClassSpecificationArguments(boolean, boolean, boolean) with 'readFirstWord', 'allowClassMembers', 'allowValues'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "proguard.ClassSpecification ConfigurationParser.parseClassSpecificationArguments(boolean, boolean, boolean)"
  })
  void testParseClassSpecificationArgumentsWithReadFirstWordAllowClassMembersAllowValues4()
      throws IOException, ParseException {
    // Arrange
    URL url =
        Paths.get(System.getProperty("java.io.tmpdir"), "2025_11_13_00_48_37_22549")
            .toUri()
            .toURL();
    ConfigurationParser configurationParser = new ConfigurationParser(url, new Properties());

    // Act and Assert
    assertThrows(
        ParseException.class,
        () -> configurationParser.parseClassSpecificationArguments(true, true, true));
  }

  /**
   * Test {@link ConfigurationParser#parseClassSpecificationArguments()}.
   *
   * <ul>
   *   <li>Then throw {@link ParseException}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationParser#parseClassSpecificationArguments()}
   */
  @Test
  @DisplayName("Test parseClassSpecificationArguments(); then throw ParseException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "proguard.ClassSpecification ConfigurationParser.parseClassSpecificationArguments()"
  })
  void testParseClassSpecificationArguments_thenThrowParseException()
      throws IOException, ParseException {
    // Arrange
    URL url = Paths.get(System.getProperty("java.io.tmpdir"), "").toUri().toURL();
    ConfigurationParser configurationParser = new ConfigurationParser(url, new Properties());

    // Act and Assert
    assertThrows(
        ParseException.class, () -> configurationParser.parseClassSpecificationArguments());
  }
}
