package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class SimpleNameFactoryDiffblueTest {
  /**
   * Test {@link SimpleNameFactory#nextName()}.
   *
   * <ul>
   *   <li>Given {@link SimpleNameFactory#SimpleNameFactory(boolean)} with generateMixedCaseNames is
   *       {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleNameFactory#nextName()}
   */
  @Test
  @DisplayName(
      "Test nextName(); given SimpleNameFactory(boolean) with generateMixedCaseNames is 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String SimpleNameFactory.nextName()"})
  void testNextName_givenSimpleNameFactoryWithGenerateMixedCaseNamesIsFalse() {
    // Arrange, Act and Assert
    assertEquals("a", new SimpleNameFactory(false).nextName());
  }

  /**
   * Test {@link SimpleNameFactory#nextName()}.
   *
   * <ul>
   *   <li>Given {@link SimpleNameFactory#SimpleNameFactory(boolean)} with generateMixedCaseNames is
   *       {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleNameFactory#nextName()}
   */
  @Test
  @DisplayName(
      "Test nextName(); given SimpleNameFactory(boolean) with generateMixedCaseNames is 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String SimpleNameFactory.nextName()"})
  void testNextName_givenSimpleNameFactoryWithGenerateMixedCaseNamesIsTrue() {
    // Arrange, Act and Assert
    assertEquals("a", new SimpleNameFactory(true).nextName());
  }
}
