package proguard.gradle.plugin.android.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ProGuardConfigurationDiffblueTest {
  /**
   * Test {@link ProGuardConfiguration#getFilename()}.
   *
   * <p>Method under test: {@link ProGuardConfiguration#getFilename()}
   */
  @Test
  @DisplayName("Test getFilename()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String ProGuardConfiguration.getFilename()"})
  void testGetFilename() {
    // Arrange, Act and Assert
    assertEquals("foo.txt", new UserProGuardConfiguration("foo.txt").getFilename());
  }

  /**
   * Test {@link ProGuardConfiguration#getPath()}.
   *
   * <p>Method under test: {@link ProGuardConfiguration#getPath()}
   */
  @Test
  @DisplayName("Test getPath()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String ProGuardConfiguration.getPath()"})
  void testGetPath() {
    // Arrange, Act and Assert
    assertEquals("foo.txt", new UserProGuardConfiguration("foo.txt").getPath());
  }

  /**
   * Test {@link ProGuardConfiguration#toString()}.
   *
   * <p>Method under test: {@link ProGuardConfiguration#toString()}
   */
  @Test
  @DisplayName("Test toString()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String ProGuardConfiguration.toString()"})
  void testToString() {
    // Arrange, Act and Assert
    assertEquals("foo.txt", new UserProGuardConfiguration("foo.txt").toString());
  }
}
