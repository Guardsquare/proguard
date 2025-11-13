package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ConstantDoubleDiffblueTest {
  /**
   * Test {@link ConstantDouble#getDouble(long)}.
   *
   * <p>Method under test: {@link ConstantDouble#getDouble(long)}
   */
  @Test
  @DisplayName("Test getDouble(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"double ConstantDouble.getDouble(long)"})
  void testGetDouble() {
    // Arrange, Act and Assert
    assertEquals(10.0d, new ConstantDouble(10.0d).getDouble(10L));
  }
}
