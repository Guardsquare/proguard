package proguard.ant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Paths;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.listener.BigProjectLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import proguard.Configuration;

@ExtendWith(MockitoExtension.class)
class ProGuardTaskDiffblueTest {
  @InjectMocks private ProGuardTask proGuardTask;

  /**
   * Test {@link ProGuardTask#setConfiguration(File)}.
   *
   * <p>Method under test: {@link ProGuardTask#setConfiguration(File)}
   */
  @Test
  @DisplayName("Test setConfiguration(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setConfiguration(File)"})
  void testSetConfiguration() throws BuildException {
    // Arrange
    BigProjectLogger listener = new BigProjectLogger();
    listener.setOutputPrintStream(new PrintStream(new ByteArrayOutputStream()));

    Project project = new Project();
    project.addBuildListener(listener);
    proGuardTask.setProject(project);

    // Act and Assert
    assertThrows(
        BuildException.class,
        () ->
            proGuardTask.setConfiguration(
                Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile()));
  }

  /**
   * Test {@link ProGuardTask#setConfiguration(File)}.
   *
   * <ul>
   *   <li>Given {@code Object}.
   *   <li>Then throw {@link BuildException}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setConfiguration(File)}
   */
  @Test
  @DisplayName("Test setConfiguration(File); given 'java.lang.Object'; then throw BuildException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setConfiguration(File)"})
  void testSetConfiguration_givenJavaLangObject_thenThrowBuildException() throws BuildException {
    // Arrange
    Project project = new Project();
    Class<Object> typeClass = Object.class;
    project.addDataTypeDefinition("42", typeClass);
    project.addBuildListener(new AntClassLoader());

    ProGuardTask proGuardTask = new ProGuardTask();
    proGuardTask.setProject(project);

    // Act and Assert
    assertThrows(
        BuildException.class,
        () ->
            proGuardTask.setConfiguration(
                Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile()));
  }

  /**
   * Test {@link ProGuardTask#setConfiguration(File)}.
   *
   * <ul>
   *   <li>Given {@link ProGuardTask} (default constructor) Project is {@link Project} (default
   *       constructor).
   *   <li>Then throw {@link BuildException}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setConfiguration(File)}
   */
  @Test
  @DisplayName(
      "Test setConfiguration(File); given ProGuardTask (default constructor) Project is Project (default constructor); then throw BuildException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setConfiguration(File)"})
  void testSetConfiguration_givenProGuardTaskProjectIsProject_thenThrowBuildException()
      throws BuildException {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();
    proGuardTask.setProject(new Project());

    // Act and Assert
    assertThrows(
        BuildException.class,
        () ->
            proGuardTask.setConfiguration(
                Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile()));
  }

  /**
   * Test {@link ProGuardTask#setConfiguration(File)}.
   *
   * <ul>
   *   <li>Given {@link Project} (default constructor) addBuildListener {@link
   *       AntClassLoader#AntClassLoader()}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setConfiguration(File)}
   */
  @Test
  @DisplayName(
      "Test setConfiguration(File); given Project (default constructor) addBuildListener AntClassLoader()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setConfiguration(File)"})
  void testSetConfiguration_givenProjectAddBuildListenerAntClassLoader() throws BuildException {
    // Arrange
    Project project = new Project();
    project.addBuildListener(new AntClassLoader());

    ProGuardTask proGuardTask = new ProGuardTask();
    proGuardTask.setProject(project);

    // Act and Assert
    assertThrows(
        BuildException.class,
        () ->
            proGuardTask.setConfiguration(
                Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile()));
  }

  /**
   * Test {@link ProGuardTask#setConfiguration(File)}.
   *
   * <ul>
   *   <li>Given {@link Project} (default constructor) addTarget {@code 42} and {@link
   *       Target#Target()}.
   *   <li>Then throw {@link BuildException}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setConfiguration(File)}
   */
  @Test
  @DisplayName(
      "Test setConfiguration(File); given Project (default constructor) addTarget '42' and Target(); then throw BuildException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setConfiguration(File)"})
  void testSetConfiguration_givenProjectAddTarget42AndTarget_thenThrowBuildException()
      throws BuildException {
    // Arrange
    Project project = new Project();
    project.addTarget("42", new Target());
    project.addBuildListener(new AntClassLoader());

    ProGuardTask proGuardTask = new ProGuardTask();
    proGuardTask.setProject(project);

    // Act and Assert
    assertThrows(
        BuildException.class,
        () ->
            proGuardTask.setConfiguration(
                Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile()));
  }

  /**
   * Test {@link ProGuardTask#setConfiguration(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code foo} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setConfiguration(File)}
   */
  @Test
  @DisplayName("Test setConfiguration(File); when Property is 'java.io.tmpdir' is 'foo' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setConfiguration(File)"})
  void testSetConfiguration_whenPropertyIsJavaIoTmpdirIsFooToFile() throws BuildException {
    // Arrange
    proGuardTask.setProject(new Project());

    // Act and Assert
    assertThrows(
        BuildException.class,
        () ->
            proGuardTask.setConfiguration(
                Paths.get(System.getProperty("java.io.tmpdir"), "foo").toFile()));
  }

  /**
   * Test {@link ProGuardTask#setOutjar(String)}.
   *
   * <p>Method under test: {@link ProGuardTask#setOutjar(String)}
   */
  @Test
  @DisplayName("Test setOutjar(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setOutjar(String)"})
  void testSetOutjar() {
    // Arrange, Act and Assert
    assertThrows(BuildException.class, () -> new ProGuardTask().setOutjar("Parameters"));
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 5}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3211264}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '5'; then ProGuardTask (default constructor) configuration targetClassVersion is '3211264'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when5_thenProGuardTaskConfigurationTargetClassVersionIs3211264() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("5");

    // Assert
    assertEquals(3211264, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 6}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3276800}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '6'; then ProGuardTask (default constructor) configuration targetClassVersion is '3276800'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when6_thenProGuardTaskConfigurationTargetClassVersionIs3276800() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("6");

    // Assert
    assertEquals(3276800, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 7}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3342336}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '7'; then ProGuardTask (default constructor) configuration targetClassVersion is '3342336'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when7_thenProGuardTaskConfigurationTargetClassVersionIs3342336() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("7");

    // Assert
    assertEquals(3342336, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 8}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3407872}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '8'; then ProGuardTask (default constructor) configuration targetClassVersion is '3407872'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when8_thenProGuardTaskConfigurationTargetClassVersionIs3407872() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("8");

    // Assert
    assertEquals(3407872, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 9}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3473408}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '9'; then ProGuardTask (default constructor) configuration targetClassVersion is '3473408'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when9_thenProGuardTaskConfigurationTargetClassVersionIs3473408() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("9");

    // Assert
    assertEquals(3473408, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 1.0}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 2949123}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '1.0'; then ProGuardTask (default constructor) configuration targetClassVersion is '2949123'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when10_thenProGuardTaskConfigurationTargetClassVersionIs2949123() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("1.0");

    // Assert
    assertEquals(2949123, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 10}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3538944}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '10'; then ProGuardTask (default constructor) configuration targetClassVersion is '3538944'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when10_thenProGuardTaskConfigurationTargetClassVersionIs3538944() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("10");

    // Assert
    assertEquals(3538944, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 1.1}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 2949123}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '1.1'; then ProGuardTask (default constructor) configuration targetClassVersion is '2949123'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when11_thenProGuardTaskConfigurationTargetClassVersionIs2949123() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("1.1");

    // Assert
    assertEquals(2949123, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 11}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3604480}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '11'; then ProGuardTask (default constructor) configuration targetClassVersion is '3604480'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when11_thenProGuardTaskConfigurationTargetClassVersionIs3604480() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("11");

    // Assert
    assertEquals(3604480, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 1.2}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3014656}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '1.2'; then ProGuardTask (default constructor) configuration targetClassVersion is '3014656'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when12_thenProGuardTaskConfigurationTargetClassVersionIs3014656() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("1.2");

    // Assert
    assertEquals(3014656, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 12}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3670016}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '12'; then ProGuardTask (default constructor) configuration targetClassVersion is '3670016'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when12_thenProGuardTaskConfigurationTargetClassVersionIs3670016() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("12");

    // Assert
    assertEquals(3670016, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 1.3}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3080192}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '1.3'; then ProGuardTask (default constructor) configuration targetClassVersion is '3080192'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when13_thenProGuardTaskConfigurationTargetClassVersionIs3080192() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("1.3");

    // Assert
    assertEquals(3080192, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 13}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3735552}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '13'; then ProGuardTask (default constructor) configuration targetClassVersion is '3735552'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when13_thenProGuardTaskConfigurationTargetClassVersionIs3735552() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("13");

    // Assert
    assertEquals(3735552, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 1.4}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3145728}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '1.4'; then ProGuardTask (default constructor) configuration targetClassVersion is '3145728'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when14_thenProGuardTaskConfigurationTargetClassVersionIs3145728() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("1.4");

    // Assert
    assertEquals(3145728, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 14}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3801088}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '14'; then ProGuardTask (default constructor) configuration targetClassVersion is '3801088'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when14_thenProGuardTaskConfigurationTargetClassVersionIs3801088() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("14");

    // Assert
    assertEquals(3801088, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 1.5}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3211264}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '1.5'; then ProGuardTask (default constructor) configuration targetClassVersion is '3211264'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when15_thenProGuardTaskConfigurationTargetClassVersionIs3211264() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("1.5");

    // Assert
    assertEquals(3211264, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 15}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3866624}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '15'; then ProGuardTask (default constructor) configuration targetClassVersion is '3866624'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when15_thenProGuardTaskConfigurationTargetClassVersionIs3866624() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("15");

    // Assert
    assertEquals(3866624, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 1.6}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3276800}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '1.6'; then ProGuardTask (default constructor) configuration targetClassVersion is '3276800'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when16_thenProGuardTaskConfigurationTargetClassVersionIs3276800() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("1.6");

    // Assert
    assertEquals(3276800, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 16}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3932160}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '16'; then ProGuardTask (default constructor) configuration targetClassVersion is '3932160'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when16_thenProGuardTaskConfigurationTargetClassVersionIs3932160() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("16");

    // Assert
    assertEquals(3932160, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 1.7}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3342336}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '1.7'; then ProGuardTask (default constructor) configuration targetClassVersion is '3342336'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when17_thenProGuardTaskConfigurationTargetClassVersionIs3342336() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("1.7");

    // Assert
    assertEquals(3342336, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 17}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3997696}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '17'; then ProGuardTask (default constructor) configuration targetClassVersion is '3997696'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when17_thenProGuardTaskConfigurationTargetClassVersionIs3997696() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("17");

    // Assert
    assertEquals(3997696, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 1.8}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3407872}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '1.8'; then ProGuardTask (default constructor) configuration targetClassVersion is '3407872'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when18_thenProGuardTaskConfigurationTargetClassVersionIs3407872() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("1.8");

    // Assert
    assertEquals(3407872, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 18}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 4063232}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '18'; then ProGuardTask (default constructor) configuration targetClassVersion is '4063232'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when18_thenProGuardTaskConfigurationTargetClassVersionIs4063232() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("18");

    // Assert
    assertEquals(4063232, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 1.9}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 3473408}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '1.9'; then ProGuardTask (default constructor) configuration targetClassVersion is '3473408'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when19_thenProGuardTaskConfigurationTargetClassVersionIs3473408() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("1.9");

    // Assert
    assertEquals(3473408, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 19}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 4128768}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '19'; then ProGuardTask (default constructor) configuration targetClassVersion is '4128768'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when19_thenProGuardTaskConfigurationTargetClassVersionIs4128768() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("19");

    // Assert
    assertEquals(4128768, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 20}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 4194304}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '20'; then ProGuardTask (default constructor) configuration targetClassVersion is '4194304'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when20_thenProGuardTaskConfigurationTargetClassVersionIs4194304() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("20");

    // Assert
    assertEquals(4194304, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 21}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 4259840}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '21'; then ProGuardTask (default constructor) configuration targetClassVersion is '4259840'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when21_thenProGuardTaskConfigurationTargetClassVersionIs4259840() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("21");

    // Assert
    assertEquals(4259840, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 22}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 4325376}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '22'; then ProGuardTask (default constructor) configuration targetClassVersion is '4325376'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when22_thenProGuardTaskConfigurationTargetClassVersionIs4325376() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("22");

    // Assert
    assertEquals(4325376, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 23}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 4390912}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '23'; then ProGuardTask (default constructor) configuration targetClassVersion is '4390912'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when23_thenProGuardTaskConfigurationTargetClassVersionIs4390912() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("23");

    // Assert
    assertEquals(4390912, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code 24}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#targetClassVersion} is {@code 4456448}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName(
      "Test setTarget(String); when '24'; then ProGuardTask (default constructor) configuration targetClassVersion is '4456448'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_when24_thenProGuardTaskConfigurationTargetClassVersionIs4456448() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setTarget("24");

    // Assert
    assertEquals(4456448, proGuardTask.configuration.targetClassVersion);
  }

  /**
   * Test {@link ProGuardTask#setTarget(String)}.
   *
   * <ul>
   *   <li>When {@code Target}.
   *   <li>Then throw {@link BuildException}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setTarget(String)}
   */
  @Test
  @DisplayName("Test setTarget(String); when 'Target'; then throw BuildException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setTarget(String)"})
  void testSetTarget_whenTarget_thenThrowBuildException() {
    // Arrange, Act and Assert
    assertThrows(BuildException.class, () -> new ProGuardTask().setTarget("Target"));
  }

  /**
   * Test {@link ProGuardTask#setForceprocessing(boolean)}.
   *
   * <ul>
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#lastModified} is {@link Long#MAX_VALUE}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setForceprocessing(boolean)}
   */
  @Test
  @DisplayName(
      "Test setForceprocessing(boolean); then ProGuardTask (default constructor) configuration lastModified is MAX_VALUE")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setForceprocessing(boolean)"})
  void testSetForceprocessing_thenProGuardTaskConfigurationLastModifiedIsMax_value() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setForceprocessing(true);

    // Assert
    assertEquals(Long.MAX_VALUE, proGuardTask.configuration.lastModified);
  }

  /**
   * Test {@link ProGuardTask#setForceprocessing(boolean)}.
   *
   * <ul>
   *   <li>When {@code false}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#lastModified} is zero.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setForceprocessing(boolean)}
   */
  @Test
  @DisplayName(
      "Test setForceprocessing(boolean); when 'false'; then ProGuardTask (default constructor) configuration lastModified is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setForceprocessing(boolean)"})
  void testSetForceprocessing_whenFalse_thenProGuardTaskConfigurationLastModifiedIsZero() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setForceprocessing(false);

    // Assert that nothing has changed
    assertEquals(0L, proGuardTask.configuration.lastModified);
  }

  /**
   * Test {@link ProGuardTask#setPrintseeds(File)}.
   *
   * <p>Method under test: {@link ProGuardTask#setPrintseeds(File)}
   */
  @Test
  @DisplayName("Test setPrintseeds(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintseeds(File)"})
  void testSetPrintseeds() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();
    File printSeeds = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();

    // Act
    proGuardTask.setPrintseeds(printSeeds);

    // Assert
    assertSame(printSeeds, proGuardTask.configuration.printSeeds);
  }

  /**
   * Test {@link ProGuardTask#setPrintseeds(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@link Boolean#FALSE} toString toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintseeds(File)}
   */
  @Test
  @DisplayName(
      "Test setPrintseeds(File); when Property is 'java.io.tmpdir' is FALSE toString toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintseeds(File)"})
  void testSetPrintseeds_whenPropertyIsJavaIoTmpdirIsFalseToStringToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintseeds(
        Paths.get(System.getProperty("java.io.tmpdir"), Boolean.FALSE.toString()).toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.printSeeds);
  }

  /**
   * Test {@link ProGuardTask#setPrintseeds(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code no} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintseeds(File)}
   */
  @Test
  @DisplayName("Test setPrintseeds(File); when Property is 'java.io.tmpdir' is 'no' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintseeds(File)"})
  void testSetPrintseeds_whenPropertyIsJavaIoTmpdirIsNoToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintseeds(Paths.get(System.getProperty("java.io.tmpdir"), "no").toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.printSeeds);
  }

  /**
   * Test {@link ProGuardTask#setPrintseeds(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code off} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintseeds(File)}
   */
  @Test
  @DisplayName("Test setPrintseeds(File); when Property is 'java.io.tmpdir' is 'off' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintseeds(File)"})
  void testSetPrintseeds_whenPropertyIsJavaIoTmpdirIsOffToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintseeds(Paths.get(System.getProperty("java.io.tmpdir"), "off").toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.printSeeds);
  }

  /**
   * Test {@link ProGuardTask#setPrintseeds(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code on} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintseeds(File)}
   */
  @Test
  @DisplayName("Test setPrintseeds(File); when Property is 'java.io.tmpdir' is 'on' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintseeds(File)"})
  void testSetPrintseeds_whenPropertyIsJavaIoTmpdirIsOnToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintseeds(Paths.get(System.getProperty("java.io.tmpdir"), "on").toFile());

    // Assert
    File file = proGuardTask.configuration.printSeeds;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#setPrintseeds(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@link Boolean#TRUE} toString toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintseeds(File)}
   */
  @Test
  @DisplayName(
      "Test setPrintseeds(File); when Property is 'java.io.tmpdir' is TRUE toString toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintseeds(File)"})
  void testSetPrintseeds_whenPropertyIsJavaIoTmpdirIsTrueToStringToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintseeds(
        Paths.get(System.getProperty("java.io.tmpdir"), Boolean.TRUE.toString()).toFile());

    // Assert
    File file = proGuardTask.configuration.printSeeds;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#setPrintseeds(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code yes} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintseeds(File)}
   */
  @Test
  @DisplayName("Test setPrintseeds(File); when Property is 'java.io.tmpdir' is 'yes' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintseeds(File)"})
  void testSetPrintseeds_whenPropertyIsJavaIoTmpdirIsYesToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintseeds(Paths.get(System.getProperty("java.io.tmpdir"), "yes").toFile());

    // Assert
    File file = proGuardTask.configuration.printSeeds;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#setPrintusage(File)}.
   *
   * <p>Method under test: {@link ProGuardTask#setPrintusage(File)}
   */
  @Test
  @DisplayName("Test setPrintusage(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintusage(File)"})
  void testSetPrintusage() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();
    File printUsage = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();

    // Act
    proGuardTask.setPrintusage(printUsage);

    // Assert
    assertSame(printUsage, proGuardTask.configuration.printUsage);
  }

  /**
   * Test {@link ProGuardTask#setPrintusage(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@link Boolean#FALSE} toString toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintusage(File)}
   */
  @Test
  @DisplayName(
      "Test setPrintusage(File); when Property is 'java.io.tmpdir' is FALSE toString toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintusage(File)"})
  void testSetPrintusage_whenPropertyIsJavaIoTmpdirIsFalseToStringToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintusage(
        Paths.get(System.getProperty("java.io.tmpdir"), Boolean.FALSE.toString()).toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.printUsage);
  }

  /**
   * Test {@link ProGuardTask#setPrintusage(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code no} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintusage(File)}
   */
  @Test
  @DisplayName("Test setPrintusage(File); when Property is 'java.io.tmpdir' is 'no' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintusage(File)"})
  void testSetPrintusage_whenPropertyIsJavaIoTmpdirIsNoToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintusage(Paths.get(System.getProperty("java.io.tmpdir"), "no").toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.printUsage);
  }

  /**
   * Test {@link ProGuardTask#setPrintusage(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code off} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintusage(File)}
   */
  @Test
  @DisplayName("Test setPrintusage(File); when Property is 'java.io.tmpdir' is 'off' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintusage(File)"})
  void testSetPrintusage_whenPropertyIsJavaIoTmpdirIsOffToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintusage(Paths.get(System.getProperty("java.io.tmpdir"), "off").toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.printUsage);
  }

  /**
   * Test {@link ProGuardTask#setPrintusage(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code on} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintusage(File)}
   */
  @Test
  @DisplayName("Test setPrintusage(File); when Property is 'java.io.tmpdir' is 'on' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintusage(File)"})
  void testSetPrintusage_whenPropertyIsJavaIoTmpdirIsOnToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintusage(Paths.get(System.getProperty("java.io.tmpdir"), "on").toFile());

    // Assert
    File file = proGuardTask.configuration.printUsage;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#setPrintusage(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@link Boolean#TRUE} toString toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintusage(File)}
   */
  @Test
  @DisplayName(
      "Test setPrintusage(File); when Property is 'java.io.tmpdir' is TRUE toString toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintusage(File)"})
  void testSetPrintusage_whenPropertyIsJavaIoTmpdirIsTrueToStringToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintusage(
        Paths.get(System.getProperty("java.io.tmpdir"), Boolean.TRUE.toString()).toFile());

    // Assert
    File file = proGuardTask.configuration.printUsage;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#setPrintusage(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code yes} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintusage(File)}
   */
  @Test
  @DisplayName("Test setPrintusage(File); when Property is 'java.io.tmpdir' is 'yes' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintusage(File)"})
  void testSetPrintusage_whenPropertyIsJavaIoTmpdirIsYesToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintusage(Paths.get(System.getProperty("java.io.tmpdir"), "yes").toFile());

    // Assert
    File file = proGuardTask.configuration.printUsage;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#setPrintmapping(File)}.
   *
   * <p>Method under test: {@link ProGuardTask#setPrintmapping(File)}
   */
  @Test
  @DisplayName("Test setPrintmapping(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintmapping(File)"})
  void testSetPrintmapping() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();
    File printMapping = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();

    // Act
    proGuardTask.setPrintmapping(printMapping);

    // Assert
    assertSame(printMapping, proGuardTask.configuration.printMapping);
  }

  /**
   * Test {@link ProGuardTask#setPrintmapping(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@link Boolean#FALSE} toString toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintmapping(File)}
   */
  @Test
  @DisplayName(
      "Test setPrintmapping(File); when Property is 'java.io.tmpdir' is FALSE toString toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintmapping(File)"})
  void testSetPrintmapping_whenPropertyIsJavaIoTmpdirIsFalseToStringToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintmapping(
        Paths.get(System.getProperty("java.io.tmpdir"), Boolean.FALSE.toString()).toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.printMapping);
  }

  /**
   * Test {@link ProGuardTask#setPrintmapping(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code no} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintmapping(File)}
   */
  @Test
  @DisplayName("Test setPrintmapping(File); when Property is 'java.io.tmpdir' is 'no' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintmapping(File)"})
  void testSetPrintmapping_whenPropertyIsJavaIoTmpdirIsNoToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintmapping(Paths.get(System.getProperty("java.io.tmpdir"), "no").toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.printMapping);
  }

  /**
   * Test {@link ProGuardTask#setPrintmapping(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code off} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintmapping(File)}
   */
  @Test
  @DisplayName("Test setPrintmapping(File); when Property is 'java.io.tmpdir' is 'off' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintmapping(File)"})
  void testSetPrintmapping_whenPropertyIsJavaIoTmpdirIsOffToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintmapping(Paths.get(System.getProperty("java.io.tmpdir"), "off").toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.printMapping);
  }

  /**
   * Test {@link ProGuardTask#setPrintmapping(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code on} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintmapping(File)}
   */
  @Test
  @DisplayName("Test setPrintmapping(File); when Property is 'java.io.tmpdir' is 'on' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintmapping(File)"})
  void testSetPrintmapping_whenPropertyIsJavaIoTmpdirIsOnToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintmapping(Paths.get(System.getProperty("java.io.tmpdir"), "on").toFile());

    // Assert
    File file = proGuardTask.configuration.printMapping;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#setPrintmapping(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@link Boolean#TRUE} toString toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintmapping(File)}
   */
  @Test
  @DisplayName(
      "Test setPrintmapping(File); when Property is 'java.io.tmpdir' is TRUE toString toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintmapping(File)"})
  void testSetPrintmapping_whenPropertyIsJavaIoTmpdirIsTrueToStringToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintmapping(
        Paths.get(System.getProperty("java.io.tmpdir"), Boolean.TRUE.toString()).toFile());

    // Assert
    File file = proGuardTask.configuration.printMapping;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#setPrintmapping(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code yes} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintmapping(File)}
   */
  @Test
  @DisplayName("Test setPrintmapping(File); when Property is 'java.io.tmpdir' is 'yes' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintmapping(File)"})
  void testSetPrintmapping_whenPropertyIsJavaIoTmpdirIsYesToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintmapping(Paths.get(System.getProperty("java.io.tmpdir"), "yes").toFile());

    // Assert
    File file = proGuardTask.configuration.printMapping;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#setApplymapping(File)}.
   *
   * <p>Method under test: {@link ProGuardTask#setApplymapping(File)}
   */
  @Test
  @DisplayName("Test setApplymapping(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setApplymapping(File)"})
  void testSetApplymapping() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();
    File applyMapping = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();

    // Act
    proGuardTask.setApplymapping(applyMapping);

    // Assert
    assertSame(applyMapping, proGuardTask.configuration.applyMapping);
  }

  /**
   * Test {@link ProGuardTask#setObfuscationdictionary(File)}.
   *
   * <p>Method under test: {@link ProGuardTask#setObfuscationdictionary(File)}
   */
  @Test
  @DisplayName("Test setObfuscationdictionary(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setObfuscationdictionary(File)"})
  void testSetObfuscationdictionary() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setObfuscationdictionary(
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

    // Assert
    assertEquals(
        "file:/C:/Users/afry/AppData/Local/Temp/test.txt",
        proGuardTask.configuration.obfuscationDictionary.toString());
  }

  /**
   * Test {@link ProGuardTask#setClassobfuscationdictionary(File)}.
   *
   * <p>Method under test: {@link ProGuardTask#setClassobfuscationdictionary(File)}
   */
  @Test
  @DisplayName("Test setClassobfuscationdictionary(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setClassobfuscationdictionary(File)"})
  void testSetClassobfuscationdictionary() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setClassobfuscationdictionary(
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

    // Assert
    assertEquals(
        "file:/C:/Users/afry/AppData/Local/Temp/test.txt",
        proGuardTask.configuration.classObfuscationDictionary.toString());
  }

  /**
   * Test {@link ProGuardTask#setPackageobfuscationdictionary(File)}.
   *
   * <p>Method under test: {@link ProGuardTask#setPackageobfuscationdictionary(File)}
   */
  @Test
  @DisplayName("Test setPackageobfuscationdictionary(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPackageobfuscationdictionary(File)"})
  void testSetPackageobfuscationdictionary() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPackageobfuscationdictionary(
        Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

    // Assert
    assertEquals(
        "file:/C:/Users/afry/AppData/Local/Temp/test.txt",
        proGuardTask.configuration.packageObfuscationDictionary.toString());
  }

  /**
   * Test {@link ProGuardTask#setFlattenpackagehierarchy(String)}.
   *
   * <p>Method under test: {@link ProGuardTask#setFlattenpackagehierarchy(String)}
   */
  @Test
  @DisplayName("Test setFlattenpackagehierarchy(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setFlattenpackagehierarchy(String)"})
  void testSetFlattenpackagehierarchy() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setFlattenpackagehierarchy("java.text");

    // Assert
    assertEquals("java/text", proGuardTask.configuration.flattenPackageHierarchy);
  }

  /**
   * Test {@link ProGuardTask#setRepackageclasses(String)}.
   *
   * <p>Method under test: {@link ProGuardTask#setRepackageclasses(String)}
   */
  @Test
  @DisplayName("Test setRepackageclasses(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setRepackageclasses(String)"})
  void testSetRepackageclasses() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setRepackageclasses("java.text");

    // Assert
    assertEquals("java/text", proGuardTask.configuration.repackageClasses);
  }

  /**
   * Test {@link ProGuardTask#setDefaultpackage(String)}.
   *
   * <p>Method under test: {@link ProGuardTask#setDefaultpackage(String)}
   */
  @Test
  @DisplayName("Test setDefaultpackage(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setDefaultpackage(String)"})
  void testSetDefaultpackage() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setDefaultpackage("java.text");

    // Assert
    assertEquals("java/text", proGuardTask.configuration.repackageClasses);
  }

  /**
   * Test {@link ProGuardTask#setNote(boolean)}.
   *
   * <ul>
   *   <li>Given {@link ProGuardTask} (default constructor).
   *   <li>When {@code false}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#note} Empty.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setNote(boolean)}
   */
  @Test
  @DisplayName(
      "Test setNote(boolean); given ProGuardTask (default constructor); when 'false'; then ProGuardTask (default constructor) configuration note Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setNote(boolean)"})
  void testSetNote_givenProGuardTask_whenFalse_thenProGuardTaskConfigurationNoteEmpty() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setNote(false);

    // Assert
    assertTrue(proGuardTask.configuration.note.isEmpty());
  }

  /**
   * Test {@link ProGuardTask#setNote(boolean)}.
   *
   * <ul>
   *   <li>Given {@link ProGuardTask} (default constructor).
   *   <li>When {@code true}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#note} is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setNote(boolean)}
   */
  @Test
  @DisplayName(
      "Test setNote(boolean); given ProGuardTask (default constructor); when 'true'; then ProGuardTask (default constructor) configuration note is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setNote(boolean)"})
  void testSetNote_givenProGuardTask_whenTrue_thenProGuardTaskConfigurationNoteIsNull() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setNote(true);

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.note);
  }

  /**
   * Test {@link ProGuardTask#setNote(boolean)}.
   *
   * <ul>
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#note} is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setNote(boolean)}
   */
  @Test
  @DisplayName(
      "Test setNote(boolean); then ProGuardTask (default constructor) configuration note is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setNote(boolean)"})
  void testSetNote_thenProGuardTaskConfigurationNoteIsNull() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();
    proGuardTask.addConfiguredDontnote(new FilterElement());

    // Act
    proGuardTask.setNote(true);

    // Assert
    assertNull(proGuardTask.configuration.note);
  }

  /**
   * Test {@link ProGuardTask#setNote(boolean)}.
   *
   * <ul>
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#note} size is one.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setNote(boolean)}
   */
  @Test
  @DisplayName(
      "Test setNote(boolean); then ProGuardTask (default constructor) configuration note size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setNote(boolean)"})
  void testSetNote_thenProGuardTaskConfigurationNoteSizeIsOne() {
    // Arrange
    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    ProGuardTask proGuardTask = new ProGuardTask();
    proGuardTask.addConfiguredDontnote(filterElement);

    // Act
    proGuardTask.setNote(true);

    // Assert that nothing has changed
    assertEquals(1, proGuardTask.configuration.note.size());
  }

  /**
   * Test {@link ProGuardTask#setWarn(boolean)}.
   *
   * <ul>
   *   <li>Given {@link ProGuardTask} (default constructor).
   *   <li>When {@code false}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#warn} Empty.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setWarn(boolean)}
   */
  @Test
  @DisplayName(
      "Test setWarn(boolean); given ProGuardTask (default constructor); when 'false'; then ProGuardTask (default constructor) configuration warn Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setWarn(boolean)"})
  void testSetWarn_givenProGuardTask_whenFalse_thenProGuardTaskConfigurationWarnEmpty() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setWarn(false);

    // Assert
    assertTrue(proGuardTask.configuration.warn.isEmpty());
  }

  /**
   * Test {@link ProGuardTask#setWarn(boolean)}.
   *
   * <ul>
   *   <li>Given {@link ProGuardTask} (default constructor).
   *   <li>When {@code true}.
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#warn} is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setWarn(boolean)}
   */
  @Test
  @DisplayName(
      "Test setWarn(boolean); given ProGuardTask (default constructor); when 'true'; then ProGuardTask (default constructor) configuration warn is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setWarn(boolean)"})
  void testSetWarn_givenProGuardTask_whenTrue_thenProGuardTaskConfigurationWarnIsNull() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setWarn(true);

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.warn);
  }

  /**
   * Test {@link ProGuardTask#setWarn(boolean)}.
   *
   * <ul>
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#warn} is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setWarn(boolean)}
   */
  @Test
  @DisplayName(
      "Test setWarn(boolean); then ProGuardTask (default constructor) configuration warn is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setWarn(boolean)"})
  void testSetWarn_thenProGuardTaskConfigurationWarnIsNull() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();
    proGuardTask.addConfiguredDontwarn(new FilterElement());

    // Act
    proGuardTask.setWarn(true);

    // Assert
    assertNull(proGuardTask.configuration.warn);
  }

  /**
   * Test {@link ProGuardTask#setWarn(boolean)}.
   *
   * <ul>
   *   <li>Then {@link ProGuardTask} (default constructor) {@link ConfigurationTask#configuration}
   *       {@link Configuration#warn} size is one.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setWarn(boolean)}
   */
  @Test
  @DisplayName(
      "Test setWarn(boolean); then ProGuardTask (default constructor) configuration warn size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setWarn(boolean)"})
  void testSetWarn_thenProGuardTaskConfigurationWarnSizeIsOne() {
    // Arrange
    FilterElement filterElement = new FilterElement();
    filterElement.setName("Name");

    ProGuardTask proGuardTask = new ProGuardTask();
    proGuardTask.addConfiguredDontwarn(filterElement);

    // Act
    proGuardTask.setWarn(true);

    // Assert that nothing has changed
    assertEquals(1, proGuardTask.configuration.warn.size());
  }

  /**
   * Test {@link ProGuardTask#setPrintconfiguration(File)}.
   *
   * <p>Method under test: {@link ProGuardTask#setPrintconfiguration(File)}
   */
  @Test
  @DisplayName("Test setPrintconfiguration(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintconfiguration(File)"})
  void testSetPrintconfiguration() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();
    File printConfiguration = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();

    // Act
    proGuardTask.setPrintconfiguration(printConfiguration);

    // Assert
    assertSame(printConfiguration, proGuardTask.configuration.printConfiguration);
  }

  /**
   * Test {@link ProGuardTask#setPrintconfiguration(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@link Boolean#FALSE} toString toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintconfiguration(File)}
   */
  @Test
  @DisplayName(
      "Test setPrintconfiguration(File); when Property is 'java.io.tmpdir' is FALSE toString toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintconfiguration(File)"})
  void testSetPrintconfiguration_whenPropertyIsJavaIoTmpdirIsFalseToStringToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintconfiguration(
        Paths.get(System.getProperty("java.io.tmpdir"), Boolean.FALSE.toString()).toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.printConfiguration);
  }

  /**
   * Test {@link ProGuardTask#setPrintconfiguration(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code no} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintconfiguration(File)}
   */
  @Test
  @DisplayName("Test setPrintconfiguration(File); when Property is 'java.io.tmpdir' is 'no' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintconfiguration(File)"})
  void testSetPrintconfiguration_whenPropertyIsJavaIoTmpdirIsNoToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintconfiguration(
        Paths.get(System.getProperty("java.io.tmpdir"), "no").toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.printConfiguration);
  }

  /**
   * Test {@link ProGuardTask#setPrintconfiguration(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code off} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintconfiguration(File)}
   */
  @Test
  @DisplayName(
      "Test setPrintconfiguration(File); when Property is 'java.io.tmpdir' is 'off' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintconfiguration(File)"})
  void testSetPrintconfiguration_whenPropertyIsJavaIoTmpdirIsOffToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintconfiguration(
        Paths.get(System.getProperty("java.io.tmpdir"), "off").toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.printConfiguration);
  }

  /**
   * Test {@link ProGuardTask#setPrintconfiguration(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code on} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintconfiguration(File)}
   */
  @Test
  @DisplayName("Test setPrintconfiguration(File); when Property is 'java.io.tmpdir' is 'on' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintconfiguration(File)"})
  void testSetPrintconfiguration_whenPropertyIsJavaIoTmpdirIsOnToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintconfiguration(
        Paths.get(System.getProperty("java.io.tmpdir"), "on").toFile());

    // Assert
    File file = proGuardTask.configuration.printConfiguration;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#setPrintconfiguration(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@link Boolean#TRUE} toString toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintconfiguration(File)}
   */
  @Test
  @DisplayName(
      "Test setPrintconfiguration(File); when Property is 'java.io.tmpdir' is TRUE toString toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintconfiguration(File)"})
  void testSetPrintconfiguration_whenPropertyIsJavaIoTmpdirIsTrueToStringToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintconfiguration(
        Paths.get(System.getProperty("java.io.tmpdir"), Boolean.TRUE.toString()).toFile());

    // Assert
    File file = proGuardTask.configuration.printConfiguration;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#setPrintconfiguration(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code yes} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setPrintconfiguration(File)}
   */
  @Test
  @DisplayName(
      "Test setPrintconfiguration(File); when Property is 'java.io.tmpdir' is 'yes' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setPrintconfiguration(File)"})
  void testSetPrintconfiguration_whenPropertyIsJavaIoTmpdirIsYesToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setPrintconfiguration(
        Paths.get(System.getProperty("java.io.tmpdir"), "yes").toFile());

    // Assert
    File file = proGuardTask.configuration.printConfiguration;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#setDump(File)}.
   *
   * <p>Method under test: {@link ProGuardTask#setDump(File)}
   */
  @Test
  @DisplayName("Test setDump(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setDump(File)"})
  void testSetDump() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();
    File dump = Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile();

    // Act
    proGuardTask.setDump(dump);

    // Assert
    assertSame(dump, proGuardTask.configuration.dump);
  }

  /**
   * Test {@link ProGuardTask#setDump(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@link Boolean#FALSE} toString toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setDump(File)}
   */
  @Test
  @DisplayName("Test setDump(File); when Property is 'java.io.tmpdir' is FALSE toString toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setDump(File)"})
  void testSetDump_whenPropertyIsJavaIoTmpdirIsFalseToStringToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setDump(
        Paths.get(System.getProperty("java.io.tmpdir"), Boolean.FALSE.toString()).toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.dump);
  }

  /**
   * Test {@link ProGuardTask#setDump(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code no} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setDump(File)}
   */
  @Test
  @DisplayName("Test setDump(File); when Property is 'java.io.tmpdir' is 'no' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setDump(File)"})
  void testSetDump_whenPropertyIsJavaIoTmpdirIsNoToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setDump(Paths.get(System.getProperty("java.io.tmpdir"), "no").toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.dump);
  }

  /**
   * Test {@link ProGuardTask#setDump(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code off} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setDump(File)}
   */
  @Test
  @DisplayName("Test setDump(File); when Property is 'java.io.tmpdir' is 'off' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setDump(File)"})
  void testSetDump_whenPropertyIsJavaIoTmpdirIsOffToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setDump(Paths.get(System.getProperty("java.io.tmpdir"), "off").toFile());

    // Assert that nothing has changed
    assertNull(proGuardTask.configuration.dump);
  }

  /**
   * Test {@link ProGuardTask#setDump(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code on} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setDump(File)}
   */
  @Test
  @DisplayName("Test setDump(File); when Property is 'java.io.tmpdir' is 'on' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setDump(File)"})
  void testSetDump_whenPropertyIsJavaIoTmpdirIsOnToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setDump(Paths.get(System.getProperty("java.io.tmpdir"), "on").toFile());

    // Assert
    File file = proGuardTask.configuration.dump;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#setDump(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@link Boolean#TRUE} toString toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setDump(File)}
   */
  @Test
  @DisplayName("Test setDump(File); when Property is 'java.io.tmpdir' is TRUE toString toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setDump(File)"})
  void testSetDump_whenPropertyIsJavaIoTmpdirIsTrueToStringToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setDump(
        Paths.get(System.getProperty("java.io.tmpdir"), Boolean.TRUE.toString()).toFile());

    // Assert
    File file = proGuardTask.configuration.dump;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#setDump(File)}.
   *
   * <ul>
   *   <li>When Property is {@code java.io.tmpdir} is {@code yes} toFile.
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#setDump(File)}
   */
  @Test
  @DisplayName("Test setDump(File); when Property is 'java.io.tmpdir' is 'yes' toFile")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.setDump(File)"})
  void testSetDump_whenPropertyIsJavaIoTmpdirIsYesToFile() {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();

    // Act
    proGuardTask.setDump(Paths.get(System.getProperty("java.io.tmpdir"), "yes").toFile());

    // Assert
    File file = proGuardTask.configuration.dump;
    assertEquals("", file.getName());
    assertFalse(file.isAbsolute());
  }

  /**
   * Test {@link ProGuardTask#execute()}.
   *
   * <p>Method under test: {@link ProGuardTask#execute()}
   */
  @Test
  @DisplayName("Test execute()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.execute()"})
  void testExecute() throws BuildException {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();
    proGuardTask.addConfiguredLibraryjar(new ClassPathElement(new Project()));

    // Act and Assert
    assertThrows(BuildException.class, () -> proGuardTask.execute());
  }

  /**
   * Test {@link ProGuardTask#execute()}.
   *
   * <ul>
   *   <li>Given {@link ProGuardTask} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#execute()}
   */
  @Test
  @DisplayName("Test execute(); given ProGuardTask (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.execute()"})
  void testExecute_givenProGuardTask() throws BuildException {
    // Arrange, Act and Assert
    assertThrows(BuildException.class, () -> new ProGuardTask().execute());
  }

  /**
   * Test {@link ProGuardTask#execute()}.
   *
   * <ul>
   *   <li>Given {@link ProGuardTask} (default constructor) addConfiguredKeep {@link
   *       KeepSpecificationElement} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#execute()}
   */
  @Test
  @DisplayName(
      "Test execute(); given ProGuardTask (default constructor) addConfiguredKeep KeepSpecificationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.execute()"})
  void testExecute_givenProGuardTaskAddConfiguredKeepKeepSpecificationElement()
      throws BuildException {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();
    proGuardTask.addConfiguredKeep(new KeepSpecificationElement());

    // Act and Assert
    assertThrows(BuildException.class, () -> proGuardTask.execute());
  }

  /**
   * Test {@link ProGuardTask#execute()}.
   *
   * <ul>
   *   <li>Given {@link ProGuardTask} (default constructor) addConfiguredKeep {@link
   *       KeepSpecificationElement} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#execute()}
   */
  @Test
  @DisplayName(
      "Test execute(); given ProGuardTask (default constructor) addConfiguredKeep KeepSpecificationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.execute()"})
  void testExecute_givenProGuardTaskAddConfiguredKeepKeepSpecificationElement2()
      throws BuildException {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();
    proGuardTask.addConfiguredKeep(new KeepSpecificationElement());
    proGuardTask.addConfiguredKeep(new KeepSpecificationElement());

    // Act and Assert
    assertThrows(BuildException.class, () -> proGuardTask.execute());
  }

  /**
   * Test {@link ProGuardTask#execute()}.
   *
   * <ul>
   *   <li>Given {@link ProGuardTask} (default constructor) addConfiguredKeepnames {@link
   *       KeepSpecificationElement} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ProGuardTask#execute()}
   */
  @Test
  @DisplayName(
      "Test execute(); given ProGuardTask (default constructor) addConfiguredKeepnames KeepSpecificationElement (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.execute()"})
  void testExecute_givenProGuardTaskAddConfiguredKeepnamesKeepSpecificationElement()
      throws BuildException {
    // Arrange
    ProGuardTask proGuardTask = new ProGuardTask();
    proGuardTask.addConfiguredKeepnames(new KeepSpecificationElement());

    // Act and Assert
    assertThrows(BuildException.class, () -> proGuardTask.execute());
  }

  /**
   * Test new {@link ProGuardTask} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link ProGuardTask}
   */
  @Test
  @DisplayName("Test new ProGuardTask (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProGuardTask.<init>()"})
  void testNewProGuardTask() {
    // Arrange and Act
    ProGuardTask actualProGuardTask = new ProGuardTask();

    // Assert
    assertNull(actualProGuardTask.getDescription());
    assertNull(actualProGuardTask.getTaskName());
    assertNull(actualProGuardTask.getTaskType());
    assertNull(actualProGuardTask.getProject());
    assertNull(actualProGuardTask.getOwningTarget());
  }
}
