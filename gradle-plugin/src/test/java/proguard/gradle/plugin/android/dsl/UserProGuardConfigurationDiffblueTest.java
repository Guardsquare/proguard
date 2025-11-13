package proguard.gradle.plugin.android.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class UserProGuardConfigurationDiffblueTest {
  /**
   * Test {@link UserProGuardConfiguration#UserProGuardConfiguration(String)}.
   *
   * <ul>
   *   <li>When {@code foo.txt}.
   *   <li>Then return Filename is {@code foo.txt}.
   * </ul>
   *
   * <p>Method under test: {@link UserProGuardConfiguration#UserProGuardConfiguration(String)}
   */
  @Test
  @DisplayName(
      "Test new UserProGuardConfiguration(String); when 'foo.txt'; then return Filename is 'foo.txt'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UserProGuardConfiguration.<init>(String)"})
  void testNewUserProGuardConfiguration_whenFooTxt_thenReturnFilenameIsFooTxt() {
    // Arrange and Act
    UserProGuardConfiguration actualUserProGuardConfiguration =
        new UserProGuardConfiguration("foo.txt");

    // Assert
    assertEquals("foo.txt", actualUserProGuardConfiguration.getFilename());
    assertEquals("foo.txt", actualUserProGuardConfiguration.getPath());
    assertEquals("foo.txt", actualUserProGuardConfiguration.toString());
  }
}
