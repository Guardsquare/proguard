package proguard.pass;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.AppView;

class PassRunnerDiffblueTest {
  /**
   * Test {@link PassRunner#run(Pass, AppView)}.
   *
   * <p>Method under test: {@link PassRunner#run(Pass, AppView)}
   */
  @Test
  @DisplayName("Test run(Pass, AppView)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void PassRunner.run(Pass, AppView)"})
  void testRun() throws Exception {
    // Arrange
    PassRunner passRunner = new PassRunner();

    Pass pass = mock(Pass.class);
    doNothing().when(pass).execute(Mockito.<AppView>any());

    // Act
    passRunner.run(pass, new AppView());

    // Assert
    verify(pass).execute(isA(AppView.class));
  }
}
