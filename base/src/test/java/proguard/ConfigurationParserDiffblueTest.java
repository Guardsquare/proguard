package proguard;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.BiConsumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    Paths.get(System.getProperty("java.io.tmpdir"), "test.txt");

    ArgumentWordReader reader = mock(ArgumentWordReader.class);
    when(reader.nextWord(anyBoolean(), anyBoolean())).thenReturn("Next Word");

    // Act
    new ConfigurationParser(reader, new Properties());

    // Assert
    verify(reader).nextWord(false, false);
  }

  /**
   * Test {@link ConfigurationParser#parse(Configuration, BiConsumer)} with {@code configuration},
   * {@code unknownOptionHandler}.
   *
   * <ul>
   *   <li>Then calls {@link BiConsumer#accept(Object, Object)}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationParser#parse(Configuration, BiConsumer)}
   */
  @Test
  @DisplayName(
      "Test parse(Configuration, BiConsumer) with 'configuration', 'unknownOptionHandler'; then calls accept(Object, Object)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationParser.parse(Configuration, BiConsumer)"})
  void testParseWithConfigurationUnknownOptionHandler_thenCallsAccept()
      throws IOException, ParseException {
    // Arrange
    ConfigurationParser configurationParser =
        new ConfigurationParser(
            Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
    Configuration configuration = mock(Configuration.class);

    BiConsumer<String, String> unknownOptionHandler = mock(BiConsumer.class);
    doNothing().when(unknownOptionHandler).accept(Mockito.<String>any(), Mockito.<String>any());

    // Act
    configurationParser.parse(configuration, unknownOptionHandler);

    // Assert
    String joinResult =
        String.join(
            "",
            "line 1 of file '",
            Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toString(),
            "'");
    verify(unknownOptionHandler)
        .accept("foo", "line 1 of file 'C:\\Users\\afry\\AppData\\Local\\Temp\\test.txt'");
  }

  /**
   * Test {@link ConfigurationParser#parse(Configuration, BiConsumer)} with {@code configuration},
   * {@code unknownOptionHandler}.
   *
   * <ul>
   *   <li>Then throw {@link NumberFormatException}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationParser#parse(Configuration, BiConsumer)}
   */
  @Test
  @DisplayName(
      "Test parse(Configuration, BiConsumer) with 'configuration', 'unknownOptionHandler'; then throw NumberFormatException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationParser.parse(Configuration, BiConsumer)"})
  void testParseWithConfigurationUnknownOptionHandler_thenThrowNumberFormatException()
      throws IOException, ParseException {
    // Arrange
    ConfigurationParser configurationParser =
        new ConfigurationParser(
            Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());
    Configuration configuration = mock(Configuration.class);

    BiConsumer<String, String> unknownOptionHandler = mock(BiConsumer.class);
    doThrow(new NumberFormatException())
        .when(unknownOptionHandler)
        .accept(Mockito.<String>any(), Mockito.<String>any());

    // Act and Assert
    assertThrows(
        NumberFormatException.class,
        () -> configurationParser.parse(configuration, unknownOptionHandler));
    String joinResult =
        String.join(
            "",
            "line 1 of file '",
            Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toString(),
            "'");
    verify(unknownOptionHandler)
        .accept("foo", "line 1 of file 'C:\\Users\\afry\\AppData\\Local\\Temp\\test.txt'");
  }

  /**
   * Test {@link ConfigurationParser#parse(Configuration)} with {@code configuration}.
   *
   * <ul>
   *   <li>Then throw {@link ParseException}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationParser#parse(Configuration)}
   */
  @Test
  @DisplayName("Test parse(Configuration) with 'configuration'; then throw ParseException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationParser.parse(Configuration)"})
  void testParseWithConfiguration_thenThrowParseException() throws IOException, ParseException {
    // Arrange, Act and Assert
    assertThrows(
        ParseException.class,
        () ->
            new ConfigurationParser(
                    Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile())
                .parse(mock(Configuration.class)));
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
    // Arrange, Act and Assert
    assertThrows(
        ParseException.class,
        () ->
            new ConfigurationParser(
                    Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile())
                .parseClassSpecificationArguments(true, true, true));
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
    // Arrange, Act and Assert
    assertThrows(
        ParseException.class,
        () ->
            new ConfigurationParser(
                    Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile())
                .parseClassSpecificationArguments(false, true, true));
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
    Paths.get(System.getProperty("java.io.tmpdir"), "test.txt");
    URL url = Paths.get(System.getProperty("java.io.tmpdir"), "").toUri().toURL();
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
    // Arrange, Act and Assert
    assertThrows(
        ParseException.class,
        () ->
            new ConfigurationParser(
                    Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile())
                .parseClassSpecificationArguments());
  }
}
