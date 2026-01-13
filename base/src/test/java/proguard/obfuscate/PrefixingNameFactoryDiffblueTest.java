package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class PrefixingNameFactoryDiffblueTest {
  /**
   * Test {@link PrefixingNameFactory#nextName()}.
   *
   * <ul>
   *   <li>Then return {@code Prefix1}.
   * </ul>
   *
   * <p>Method under test: {@link PrefixingNameFactory#nextName()}
   */
  @Test
  @DisplayName("Test nextName(); then return 'Prefix1'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String PrefixingNameFactory.nextName()"})
  void testNextName_thenReturnPrefix1() {
    // Arrange, Act and Assert
    assertEquals(
        "Prefix1", new PrefixingNameFactory(new NumericNameFactory(), "Prefix").nextName());
  }

  /**
   * Test {@link PrefixingNameFactory#nextName()}.
   *
   * <ul>
   *   <li>Then return {@code PrefixPrefix1}.
   * </ul>
   *
   * <p>Method under test: {@link PrefixingNameFactory#nextName()}
   */
  @Test
  @DisplayName("Test nextName(); then return 'PrefixPrefix1'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String PrefixingNameFactory.nextName()"})
  void testNextName_thenReturnPrefixPrefix1() {
    // Arrange, Act and Assert
    assertEquals(
        "PrefixPrefix1",
        new PrefixingNameFactory(
                new PrefixingNameFactory(new NumericNameFactory(), "Prefix"), "Prefix")
            .nextName());
  }
}
