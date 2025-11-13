package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class DictionaryNameFactoryDiffblueTest {
  /**
   * Test {@link DictionaryNameFactory#nextName()}.
   *
   * <p>Method under test: {@link DictionaryNameFactory#nextName()}
   */
  @Test
  @DisplayName("Test nextName()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String DictionaryNameFactory.nextName()"})
  void testNextName() throws IOException {
    // Arrange
    StringReader reader = new StringReader("42");
    StringReader reader2 = new StringReader("foo");
    DictionaryNameFactory nameFactory =
        new DictionaryNameFactory(reader2, new NumericNameFactory());

    DictionaryNameFactory dictionaryNameFactory = new DictionaryNameFactory(reader, nameFactory);

    // Act and Assert
    assertEquals("foo", dictionaryNameFactory.nextName());
  }

  /**
   * Test {@link DictionaryNameFactory#nextName()}.
   *
   * <ul>
   *   <li>Given {@link StringReader#StringReader(String)} with {@code 42}.
   *   <li>Then return {@code 1}.
   * </ul>
   *
   * <p>Method under test: {@link DictionaryNameFactory#nextName()}
   */
  @Test
  @DisplayName("Test nextName(); given StringReader(String) with '42'; then return '1'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String DictionaryNameFactory.nextName()"})
  void testNextName_givenStringReaderWith42_thenReturn1() throws IOException {
    // Arrange
    StringReader reader = new StringReader("42");
    DictionaryNameFactory dictionaryNameFactory =
        new DictionaryNameFactory(reader, new NumericNameFactory());

    // Act and Assert
    assertEquals("1", dictionaryNameFactory.nextName());
  }

  /**
   * Test {@link DictionaryNameFactory#nextName()}.
   *
   * <ul>
   *   <li>Given {@link StringReader#StringReader(String)} with {@code foo}.
   *   <li>Then return {@code foo}.
   * </ul>
   *
   * <p>Method under test: {@link DictionaryNameFactory#nextName()}
   */
  @Test
  @DisplayName("Test nextName(); given StringReader(String) with 'foo'; then return 'foo'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String DictionaryNameFactory.nextName()"})
  void testNextName_givenStringReaderWithFoo_thenReturnFoo() throws IOException {
    // Arrange
    StringReader reader = new StringReader("foo");
    DictionaryNameFactory dictionaryNameFactory =
        new DictionaryNameFactory(reader, new NumericNameFactory());

    // Act and Assert
    assertEquals("foo", dictionaryNameFactory.nextName());
  }
}
