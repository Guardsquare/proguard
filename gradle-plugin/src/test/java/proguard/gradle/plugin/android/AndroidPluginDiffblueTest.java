package proguard.gradle.plugin.android;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.android.build.gradle.LibraryExtension;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class AndroidPluginDiffblueTest {
  /**
   * Test {@link AndroidPlugin#apply(Project)} with {@code project}.
   *
   * <ul>
   *   <li>Given {@link GradleException#GradleException()}.
   *   <li>Then throw {@link GradleException}.
   * </ul>
   *
   * <p>Method under test: {@link AndroidPlugin#apply(Project)}
   */
  @Test
  @DisplayName(
      "Test apply(Project) with 'project'; given GradleException(); then throw GradleException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AndroidPlugin.apply(Project)"})
  void testApplyWithProject_givenGradleException_thenThrowGradleException() {
    // Arrange
    AndroidPlugin androidPlugin = new AndroidPlugin(mock(LibraryExtension.class));

    Project project = mock(Project.class);
    when(project.getTasks()).thenThrow(new GradleException());

    // Act and Assert
    assertThrows(GradleException.class, () -> androidPlugin.apply(project));
    verify(project).getTasks();
  }
}
