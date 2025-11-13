package proguard.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class TimeUtilDiffblueTest {
  /**
   * Test {@link TimeUtil#millisecondsToMinSecReadable(int)}.
   *
   * <p>Method under test: {@link TimeUtil#millisecondsToMinSecReadable(int)}
   */
  @Test
  @DisplayName("Test millisecondsToMinSecReadable(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String TimeUtil.millisecondsToMinSecReadable(int)"})
  void testMillisecondsToMinSecReadable() {
    // Arrange, Act and Assert
    assertEquals("0:0.1 (m:s.ms)", TimeUtil.millisecondsToMinSecReadable(1));
  }
}
