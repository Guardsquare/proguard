package proguard.gui;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class SwingUtilDiffblueTest {
  /**
   * Test {@link SwingUtil#invokeAndWait(Runnable)}.
   *
   * <p>Method under test: {@link SwingUtil#invokeAndWait(Runnable)}
   */
  @Test
  @DisplayName("Test invokeAndWait(Runnable)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SwingUtil.invokeAndWait(Runnable)"})
  void testInvokeAndWait() throws InterruptedException, InvocationTargetException {
    // Arrange
    Runnable runnable = mock(Runnable.class);
    doNothing().when(runnable).run();

    // Act
    SwingUtil.invokeAndWait(runnable);

    // Assert
    verify(runnable).run();
  }
}
