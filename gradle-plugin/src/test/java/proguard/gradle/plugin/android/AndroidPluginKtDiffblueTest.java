package proguard.gradle.plugin.android;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.LibraryExtension;
import com.android.build.gradle.internal.dsl.AaptOptions;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class AndroidPluginKtDiffblueTest {
  /**
   * Test {@link AndroidPluginKt#getAaptAdditionalParameters(BaseExtension)}.
   *
   * <ul>
   *   <li>Then return {@link List}.
   * </ul>
   *
   * <p>Method under test: {@link AndroidPluginKt#getAaptAdditionalParameters(BaseExtension)}
   */
  @Test
  @DisplayName("Test getAaptAdditionalParameters(BaseExtension); then return List")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Collection AndroidPluginKt.getAaptAdditionalParameters(BaseExtension)"})
  void testGetAaptAdditionalParameters_thenReturnList() {
    // Arrange
    LibraryExtension $this$aaptAdditionalParameters = mock(LibraryExtension.class);
    when($this$aaptAdditionalParameters.getAaptOptions()).thenReturn(new AaptOptions(true));

    // Act
    Collection<String> actualAaptAdditionalParameters =
        AndroidPluginKt.getAaptAdditionalParameters($this$aaptAdditionalParameters);

    // Assert
    verify($this$aaptAdditionalParameters).getAaptOptions();
    assertTrue(actualAaptAdditionalParameters instanceof List);
    assertTrue(actualAaptAdditionalParameters.isEmpty());
  }
}
