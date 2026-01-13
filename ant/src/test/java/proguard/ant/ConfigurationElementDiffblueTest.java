package proguard.ant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.File;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proguard.Configuration;

@ExtendWith(MockitoExtension.class)
class ConfigurationElementDiffblueTest {
  @InjectMocks private ConfigurationElement configurationElement;

  @Mock private File file;

  @Mock private PatternSet patternSet;

  /**
   * Test {@link ConfigurationElement#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Given {@link ConfigurationElement} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationElement#appendTo(Configuration)}
   */
  @Test
  @DisplayName("Test appendTo(Configuration); given ConfigurationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationElement.appendTo(Configuration)"})
  void testAppendTo_givenConfigurationElement() {
    // Arrange, Act and Assert
    assertThrows(
        BuildException.class, () -> new ConfigurationElement().appendTo(mock(Configuration.class)));
  }

  /**
   * Test {@link ConfigurationElement#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Given {@link ConfigurationElement} (default constructor) appendSelector {@link
   *       FileSelector}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationElement#appendTo(Configuration)}
   */
  @Test
  @DisplayName(
      "Test appendTo(Configuration); given ConfigurationElement (default constructor) appendSelector FileSelector")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationElement.appendTo(Configuration)"})
  void testAppendTo_givenConfigurationElementAppendSelectorFileSelector() {
    // Arrange
    ConfigurationElement configurationElement = new ConfigurationElement();
    configurationElement.appendSelector(mock(FileSelector.class));

    // Act and Assert
    assertThrows(
        BuildException.class, () -> configurationElement.appendTo(mock(Configuration.class)));
  }

  /**
   * Test {@link ConfigurationElement#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Given {@link ConfigurationElement} (default constructor) Project is {@link Project}
   *       (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationElement#appendTo(Configuration)}
   */
  @Test
  @DisplayName(
      "Test appendTo(Configuration); given ConfigurationElement (default constructor) Project is Project (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationElement.appendTo(Configuration)"})
  void testAppendTo_givenConfigurationElementProjectIsProject() {
    // Arrange
    ConfigurationElement configurationElement = new ConfigurationElement();
    configurationElement.setProject(new Project());

    // Act and Assert
    assertThrows(
        BuildException.class, () -> configurationElement.appendTo(mock(Configuration.class)));
  }

  /**
   * Test {@link ConfigurationElement#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Given {@link File} {@link File#exists()} return {@code false}.
   *   <li>Then calls {@link File#getAbsolutePath()}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationElement#appendTo(Configuration)}
   */
  @Test
  @DisplayName(
      "Test appendTo(Configuration); given File exists() return 'false'; then calls getAbsolutePath()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationElement.appendTo(Configuration)"})
  void testAppendTo_givenFileExistsReturnFalse_thenCallsGetAbsolutePath() {
    // Arrange
    when(file.exists()).thenReturn(false);
    when(file.getAbsolutePath()).thenReturn("Absolute Path");

    // Act and Assert
    assertThrows(
        BuildException.class, () -> configurationElement.appendTo(mock(Configuration.class)));
    verify(file).exists();
    verify(file).getAbsolutePath();
  }

  /**
   * Test {@link ConfigurationElement#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Given {@link File} {@link File#exists()} throw {@link BuildException#BuildException()}.
   *   <li>Then calls {@link File#exists()}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationElement#appendTo(Configuration)}
   */
  @Test
  @DisplayName(
      "Test appendTo(Configuration); given File exists() throw BuildException(); then calls exists()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationElement.appendTo(Configuration)"})
  void testAppendTo_givenFileExistsThrowBuildException_thenCallsExists() {
    // Arrange
    when(file.exists()).thenThrow(new BuildException());

    // Act and Assert
    assertThrows(
        BuildException.class, () -> configurationElement.appendTo(mock(Configuration.class)));
    verify(file).exists();
  }

  /**
   * Test {@link ConfigurationElement#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Given {@link File} {@link File#isDirectory()} return {@code false}.
   *   <li>Then calls {@link File#getAbsolutePath()}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationElement#appendTo(Configuration)}
   */
  @Test
  @DisplayName(
      "Test appendTo(Configuration); given File isDirectory() return 'false'; then calls getAbsolutePath()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationElement.appendTo(Configuration)"})
  void testAppendTo_givenFileIsDirectoryReturnFalse_thenCallsGetAbsolutePath() {
    // Arrange
    when(file.isDirectory()).thenReturn(false);
    when(file.exists()).thenReturn(true);
    when(file.getAbsolutePath()).thenReturn("Absolute Path");

    // Act and Assert
    assertThrows(
        BuildException.class, () -> configurationElement.appendTo(mock(Configuration.class)));
    verify(file, atLeast(1)).exists();
    verify(file).getAbsolutePath();
    verify(file).isDirectory();
  }

  /**
   * Test {@link ConfigurationElement#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Given {@link File} {@link File#isDirectory()} throw {@link
   *       BuildException#BuildException()}.
   *   <li>Then calls {@link File#isDirectory()}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationElement#appendTo(Configuration)}
   */
  @Test
  @DisplayName(
      "Test appendTo(Configuration); given File isDirectory() throw BuildException(); then calls isDirectory()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationElement.appendTo(Configuration)"})
  void testAppendTo_givenFileIsDirectoryThrowBuildException_thenCallsIsDirectory() {
    // Arrange
    when(file.isDirectory()).thenThrow(new BuildException());
    when(file.exists()).thenReturn(true);

    // Act and Assert
    assertThrows(
        BuildException.class, () -> configurationElement.appendTo(mock(Configuration.class)));
    verify(file).exists();
    verify(file).isDirectory();
  }

  /**
   * Test {@link ConfigurationElement#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Given {@code Object}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationElement#appendTo(Configuration)}
   */
  @Test
  @DisplayName("Test appendTo(Configuration); given 'java.lang.Object'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationElement.appendTo(Configuration)"})
  void testAppendTo_givenJavaLangObject() {
    // Arrange
    Project project = new Project();
    Class<Object> typeClass = Object.class;
    project.addDataTypeDefinition("42", typeClass);
    project.addBuildListener(new AntClassLoader());

    ConfigurationElement configurationElement = new ConfigurationElement();
    configurationElement.setProject(project);

    // Act and Assert
    assertThrows(
        BuildException.class, () -> configurationElement.appendTo(mock(Configuration.class)));
  }

  /**
   * Test {@link ConfigurationElement#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Given {@link PatternSet} {@link PatternSet#clone()} throw {@link
   *       BuildException#BuildException()}.
   *   <li>Then calls {@link PatternSet#clone()}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationElement#appendTo(Configuration)}
   */
  @Test
  @DisplayName(
      "Test appendTo(Configuration); given PatternSet clone() throw BuildException(); then calls clone()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationElement.appendTo(Configuration)"})
  void testAppendTo_givenPatternSetCloneThrowBuildException_thenCallsClone() {
    // Arrange
    when(patternSet.clone()).thenThrow(new BuildException());
    when(file.isDirectory()).thenReturn(true);
    when(file.exists()).thenReturn(true);

    // Act and Assert
    assertThrows(
        BuildException.class, () -> configurationElement.appendTo(mock(Configuration.class)));
    verify(file).exists();
    verify(file).isDirectory();
    verify(patternSet).clone();
  }

  /**
   * Test {@link ConfigurationElement#appendTo(Configuration)}.
   *
   * <ul>
   *   <li>Given {@link Project} (default constructor) addBuildListener {@link
   *       AntClassLoader#AntClassLoader()}.
   * </ul>
   *
   * <p>Method under test: {@link ConfigurationElement#appendTo(Configuration)}
   */
  @Test
  @DisplayName(
      "Test appendTo(Configuration); given Project (default constructor) addBuildListener AntClassLoader()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationElement.appendTo(Configuration)"})
  void testAppendTo_givenProjectAddBuildListenerAntClassLoader() {
    // Arrange
    Project project = new Project();
    project.addBuildListener(new AntClassLoader());

    ConfigurationElement configurationElement = new ConfigurationElement();
    configurationElement.setProject(project);

    // Act and Assert
    assertThrows(
        BuildException.class, () -> configurationElement.appendTo(mock(Configuration.class)));
  }

  /**
   * Test new {@link ConfigurationElement} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link ConfigurationElement}
   */
  @Test
  @DisplayName("Test new ConfigurationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConfigurationElement.<init>()"})
  void testNewConfigurationElement() {
    // Arrange and Act
    ConfigurationElement actualConfigurationElement = new ConfigurationElement();

    // Assert
    assertNull(actualConfigurationElement.getDir());
    Location location = actualConfigurationElement.getLocation();
    assertNull(location.getFileName());
    assertNull(actualConfigurationElement.getDescription());
    assertNull(actualConfigurationElement.getProject());
    assertNull(actualConfigurationElement.getRefid());
    assertEquals(0, location.getColumnNumber());
    assertEquals(0, location.getLineNumber());
    assertEquals(5, actualConfigurationElement.getMaxLevelsOfSymlinks());
    assertFalse(actualConfigurationElement.isReference());
    assertTrue(actualConfigurationElement.getDefaultexcludes());
    assertTrue(actualConfigurationElement.getErrorOnMissingDir());
    assertTrue(actualConfigurationElement.isFilesystemOnly());
  }
}
