package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ConstantStringDiffblueTest {
  /**
   * Test {@link ConstantString#getString(long)}.
   *
   * <p>Method under test: {@link ConstantString#getString(long)}
   */
  @Test
  @DisplayName("Test getString(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String ConstantString.getString(long)"})
  void testGetString() {
    // Arrange, Act and Assert
    assertEquals("42", new ConstantString("42").getString(10L));
  }
}
