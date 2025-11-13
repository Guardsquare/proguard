package proguard.gradle.plugin.android;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.QualifiedContent.DefaultContentType;
import com.android.build.gradle.BaseExtension;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.gradle.api.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.gradle.plugin.android.dsl.ProGuardAndroidExtension;

class ProGuardTransformDiffblueTest {
  /**
   * Test {@link ProGuardTransform#ProGuardTransform(Project, ProGuardAndroidExtension,
   * AndroidProjectType, BaseExtension)}.
   *
   * <ul>
   *   <li>When {@link Project}.
   *   <li>Then SecondaryDirectoryOutputs return {@link List}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTransform#ProGuardTransform(Project,
   * ProGuardAndroidExtension, AndroidProjectType, BaseExtension)}
   */
  @Test
  @DisplayName(
      "Test new ProGuardTransform(Project, ProGuardAndroidExtension, AndroidProjectType, BaseExtension); when Project; then SecondaryDirectoryOutputs return List")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProGuardTransform.<init>(Project, ProGuardAndroidExtension, AndroidProjectType, BaseExtension)"
  })
  void testNewProGuardTransform_whenProject_thenSecondaryDirectoryOutputsReturnList() {
    // Arrange and Act
    ProGuardTransform actualProGuardTransform =
        new ProGuardTransform(
            mock(Project.class),
            mock(ProGuardAndroidExtension.class),
            AndroidProjectType.ANDROID_APPLICATION,
            mock(BaseExtension.class));

    // Assert
    Collection<File> secondaryDirectoryOutputs =
        actualProGuardTransform.getSecondaryDirectoryOutputs();
    assertTrue(secondaryDirectoryOutputs instanceof List);
    assertEquals("ProguardTransform", actualProGuardTransform.getName());
    assertEquals(1, actualProGuardTransform.getReferencedScopes().size());
    Set<DefaultContentType> inputTypes = actualProGuardTransform.getInputTypes();
    assertEquals(2, inputTypes.size());
    assertEquals(3, actualProGuardTransform.getScopes().size());
    assertFalse(actualProGuardTransform.isCacheable());
    assertFalse(actualProGuardTransform.isIncremental());
    assertTrue(secondaryDirectoryOutputs.isEmpty());
    assertTrue(actualProGuardTransform.getParameterInputs().isEmpty());
    assertEquals(inputTypes, actualProGuardTransform.getOutputTypes());
    assertSame(secondaryDirectoryOutputs, actualProGuardTransform.getSecondaryFileInputs());
    assertSame(secondaryDirectoryOutputs, actualProGuardTransform.getSecondaryFileOutputs());
  }

  /**
   * Test {@link ProGuardTransform#ProGuardTransform(Project, ProGuardAndroidExtension,
   * AndroidProjectType, BaseExtension)}.
   *
   * <ul>
   *   <li>When {@link Project}.
   *   <li>Then SecondaryDirectoryOutputs return {@link List}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTransform#ProGuardTransform(Project,
   * ProGuardAndroidExtension, AndroidProjectType, BaseExtension)}
   */
  @Test
  @DisplayName(
      "Test new ProGuardTransform(Project, ProGuardAndroidExtension, AndroidProjectType, BaseExtension); when Project; then SecondaryDirectoryOutputs return List")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProGuardTransform.<init>(Project, ProGuardAndroidExtension, AndroidProjectType, BaseExtension)"
  })
  void testNewProGuardTransform_whenProject_thenSecondaryDirectoryOutputsReturnList2() {
    // Arrange and Act
    ProGuardTransform actualProGuardTransform =
        new ProGuardTransform(
            mock(Project.class),
            mock(ProGuardAndroidExtension.class),
            AndroidProjectType.ANDROID_APPLICATION,
            mock(BaseExtension.class));

    // Assert
    Collection<File> secondaryDirectoryOutputs =
        actualProGuardTransform.getSecondaryDirectoryOutputs();
    assertTrue(secondaryDirectoryOutputs instanceof List);
    assertEquals("ProguardTransform", actualProGuardTransform.getName());
    assertEquals(1, actualProGuardTransform.getReferencedScopes().size());
    Set<DefaultContentType> inputTypes = actualProGuardTransform.getInputTypes();
    assertEquals(2, inputTypes.size());
    assertEquals(3, actualProGuardTransform.getScopes().size());
    assertFalse(actualProGuardTransform.isCacheable());
    assertFalse(actualProGuardTransform.isIncremental());
    assertTrue(secondaryDirectoryOutputs.isEmpty());
    assertTrue(actualProGuardTransform.getParameterInputs().isEmpty());
    assertEquals(inputTypes, actualProGuardTransform.getOutputTypes());
    assertSame(secondaryDirectoryOutputs, actualProGuardTransform.getSecondaryFileInputs());
    assertSame(secondaryDirectoryOutputs, actualProGuardTransform.getSecondaryFileOutputs());
  }
}
