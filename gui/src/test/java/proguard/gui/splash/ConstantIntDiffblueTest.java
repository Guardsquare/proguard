package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ConstantIntDiffblueTest {
  /**
   * Test {@link ConstantInt#getInt(long)}.
   *
   * <p>Method under test: {@link ConstantInt#getInt(long)}
   */
  @Test
  @DisplayName("Test getInt(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"int ConstantInt.getInt(long)"})
  void testGetInt() {
    // Arrange, Act and Assert
    assertEquals(42, new ConstantInt(42).getInt(10L));
  }
}
