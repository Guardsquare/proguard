package proguard.gui.splash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class LinearIntDiffblueTest {
  /**
   * Test {@link LinearInt#getInt(long)}.
   *
   * <p>Method under test: {@link LinearInt#getInt(long)}
   */
  @Test
  @DisplayName("Test getInt(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"int LinearInt.getInt(long)"})
  void testGetInt() {
    // Arrange
    Timing timing = mock(Timing.class);
    when(timing.getTiming(anyLong())).thenReturn(10.0d);

    // Act
    int actualInt = new LinearInt(42, 42, timing).getInt(10L);

    // Assert
    verify(timing).getTiming(10L);
    assertEquals(42, actualInt);
  }
}
