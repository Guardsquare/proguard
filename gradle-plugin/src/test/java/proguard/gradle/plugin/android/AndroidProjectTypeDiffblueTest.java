package proguard.gradle.plugin.android;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import kotlin.enums.EnumEntries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class AndroidProjectTypeDiffblueTest {
  /**
   * Test {@link AndroidProjectType#getEntries()}.
   *
   * <p>Method under test: {@link AndroidProjectType#getEntries()}
   */
  @Test
  @DisplayName("Test getEntries()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"EnumEntries AndroidProjectType.getEntries()"})
  void testGetEntries() {
    // Arrange and Act
    EnumEntries<AndroidProjectType> actualEntries =
        AndroidProjectType.valueOf("ANDROID_APPLICATION").getEntries();

    // Assert
    assertEquals(2, actualEntries.size());
    assertEquals(AndroidProjectType.ANDROID_APPLICATION, actualEntries.get(0));
    assertEquals(AndroidProjectType.ANDROID_LIBRARY, actualEntries.get(1));
  }
}
