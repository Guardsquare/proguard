package proguard.ant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.File;
import java.nio.file.Paths;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileList.FileName;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PropertySet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proguard.ClassPath;

@ExtendWith(MockitoExtension.class)
class ClassPathElementDiffblueTest {
  @InjectMocks private ClassPathElement classPathElement;

  @Mock private Project project;

  /**
   * Test {@link ClassPathElement#ClassPathElement(Project)}.
   *
   * <p>Method under test: {@link ClassPathElement#ClassPathElement(Project)}
   */
  @Test
  @DisplayName("Test new ClassPathElement(Project)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.<init>(Project)"})
  void testNewClassPathElement() {
    // Arrange
    Project project = new Project();

    // Act
    ClassPathElement actualClassPathElement = new ClassPathElement(project);

    // Assert
    assertNull(actualClassPathElement.getDescription());
    assertNull(actualClassPathElement.getRefid());
    assertEquals(0, actualClassPathElement.size());
    assertFalse(actualClassPathElement.isReference());
    assertTrue(actualClassPathElement.isEmpty());
    assertSame(project, actualClassPathElement.getProject());
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName("Test appendClassPathEntriesTo(ClassPath, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo() throws BuildException {
    // Arrange
    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.addFilelist(new FileList());

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classPathElement.appendClassPathEntriesTo(new ClassPath(), true));
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName("Test appendClassPathEntriesTo(ClassPath, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo2() throws BuildException {
    // Arrange
    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.add(Path.systemBootClasspath);

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classPathElement.appendClassPathEntriesTo(new ClassPath(), true));
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName("Test appendClassPathEntriesTo(ClassPath, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo3() {
    // Arrange
    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.add((ResourceCollection) Path.systemBootClasspath);

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classPathElement.appendClassPathEntriesTo(new ClassPath(), true));
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName("Test appendClassPathEntriesTo(ClassPath, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo4() {
    // Arrange
    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.addJavaRuntime();

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classPathElement.appendClassPathEntriesTo(new ClassPath(), true));
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName("Test appendClassPathEntriesTo(ClassPath, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo5() throws BuildException {
    // Arrange
    FileName name = new FileName();
    name.setName("The <outjar> element must specify exactly one file or directory [");

    FileList fl = new FileList();
    fl.addConfiguredFile(name);

    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.addFilelist(fl);
    ClassPath classPath = new ClassPath();

    // Act
    classPathElement.appendClassPathEntriesTo(classPath, true);

    // Assert
    assertEquals(1, classPath.size());
    assertFalse(classPath.isEmpty());
    assertTrue(classPath.hasOutput());
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName("Test appendClassPathEntriesTo(ClassPath, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo6() throws BuildException {
    // Arrange
    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.add(new Path(new Project()));

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classPathElement.appendClassPathEntriesTo(new ClassPath(), true));
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName("Test appendClassPathEntriesTo(ClassPath, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo7() throws BuildException {
    // Arrange
    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.add(
        new Path(
            new Project(), "The <outjar> element must specify exactly one file or directory ["));
    ClassPath classPath = new ClassPath();

    // Act
    classPathElement.appendClassPathEntriesTo(classPath, true);

    // Assert
    assertEquals(1, classPath.size());
    assertFalse(classPath.isEmpty());
    assertTrue(classPath.hasOutput());
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName("Test appendClassPathEntriesTo(ClassPath, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo8() throws BuildException {
    // Arrange
    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.add(new ClassPathElement(new Project()));

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classPathElement.appendClassPathEntriesTo(new ClassPath(), true));
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName("Test appendClassPathEntriesTo(ClassPath, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo9() {
    // Arrange
    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.add(new PropertySet());

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classPathElement.appendClassPathEntriesTo(new ClassPath(), true));
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName("Test appendClassPathEntriesTo(ClassPath, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo10() {
    // Arrange
    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.add(new Resource());

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classPathElement.appendClassPathEntriesTo(new ClassPath(), true));
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName("Test appendClassPathEntriesTo(ClassPath, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo11() throws BuildException {
    // Arrange
    FileSet fs = new FileSet();
    fs.setFile(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.addFileset(fs);
    ClassPath classPath = new ClassPath();

    // Act
    classPathElement.appendClassPathEntriesTo(classPath, true);

    // Assert
    assertEquals(1, classPath.size());
    assertFalse(classPath.isEmpty());
    assertTrue(classPath.hasOutput());
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName("Test appendClassPathEntriesTo(ClassPath, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo12() {
    // Arrange
    when(project.getBaseDir())
        .thenReturn(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classPathElement.appendClassPathEntriesTo(new ClassPath(), true));
    verify(project).getBaseDir();
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <ul>
   *   <li>Given {@link ClassPathElement#ClassPathElement(Project)} with project is {@link Project}
   *       (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendClassPathEntriesTo(ClassPath, boolean); given ClassPathElement(Project) with project is Project (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo_givenClassPathElementWithProjectIsProject() {
    // Arrange
    ClassPathElement classPathElement = new ClassPathElement(new Project());

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classPathElement.appendClassPathEntriesTo(new ClassPath(), true));
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <ul>
   *   <li>Given {@link FileName} (default constructor) Name is {@code 42}.
   *   <li>Then throw {@link BuildException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendClassPathEntriesTo(ClassPath, boolean); given FileName (default constructor) Name is '42'; then throw BuildException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo_givenFileNameNameIs42_thenThrowBuildException()
      throws BuildException {
    // Arrange
    FileName name = new FileName();
    name.setName("The <outjar> element must specify exactly one file or directory [");

    FileName name2 = new FileName();
    name2.setName("42");

    FileList fl = new FileList();
    fl.addConfiguredFile(name2);
    fl.addConfiguredFile(name);

    ClassPathElement classPathElement = new ClassPathElement(new Project());
    classPathElement.addFilelist(fl);

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classPathElement.appendClassPathEntriesTo(new ClassPath(), true));
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <ul>
   *   <li>Given {@code Object}.
   *   <li>Then throw {@link BuildException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendClassPathEntriesTo(ClassPath, boolean); given 'java.lang.Object'; then throw BuildException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo_givenJavaLangObject_thenThrowBuildException() {
    // Arrange
    Project project = new Project();
    Class<Object> typeClass = Object.class;
    project.addDataTypeDefinition("]", typeClass);
    project.addBuildListener(new AntClassLoader());
    ClassPathElement classPathElement = new ClassPathElement(project);

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classPathElement.appendClassPathEntriesTo(new ClassPath(), true));
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <ul>
   *   <li>Given {@link Project} (default constructor) addBuildListener {@link
   *       AntClassLoader#AntClassLoader()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendClassPathEntriesTo(ClassPath, boolean); given Project (default constructor) addBuildListener AntClassLoader()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo_givenProjectAddBuildListenerAntClassLoader() {
    // Arrange
    Project project = new Project();
    project.addBuildListener(new AntClassLoader());
    ClassPathElement classPathElement = new ClassPathElement(project);

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classPathElement.appendClassPathEntriesTo(new ClassPath(), true));
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <ul>
   *   <li>Given {@link Project} {@link Project#getBaseDir()} throw {@link
   *       BuildException#BuildException()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendClassPathEntriesTo(ClassPath, boolean); given Project getBaseDir() throw BuildException()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo_givenProjectGetBaseDirThrowBuildException() {
    // Arrange
    when(project.getBaseDir()).thenThrow(new BuildException());

    // Act and Assert
    assertThrows(
        BuildException.class,
        () -> classPathElement.appendClassPathEntriesTo(new ClassPath(), true));
    verify(project).getBaseDir();
  }

  /**
   * Test {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}.
   *
   * <ul>
   *   <li>When {@code false}.
   *   <li>Then {@link ClassPath} (default constructor) size is zero.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathElement#appendClassPathEntriesTo(ClassPath, boolean)}
   */
  @Test
  @DisplayName(
      "Test appendClassPathEntriesTo(ClassPath, boolean); when 'false'; then ClassPath (default constructor) size is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.appendClassPathEntriesTo(ClassPath, boolean)"})
  void testAppendClassPathEntriesTo_whenFalse_thenClassPathSizeIsZero() {
    // Arrange
    ClassPathElement classPathElement = new ClassPathElement(new Project());
    ClassPath classPath = new ClassPath();

    // Act
    classPathElement.appendClassPathEntriesTo(classPath, false);

    // Assert that nothing has changed
    assertEquals(0, classPath.size());
    assertFalse(classPath.hasOutput());
    assertTrue(classPath.isEmpty());
  }

  /**
   * Test {@link ClassPathElement#setFile(File)}.
   *
   * <p>Method under test: {@link ClassPathElement#setFile(File)}
   */
  @Test
  @DisplayName("Test setFile(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.setFile(File)"})
  void testSetFile() {
    // Arrange
    ClassPathElement classPathElement = new ClassPathElement(new Project());

    // Act
    classPathElement.setFile(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

    // Assert
    assertEquals(1, classPathElement.size());
    assertFalse(classPathElement.isEmpty());
  }

  /**
   * Test {@link ClassPathElement#setDir(File)}.
   *
   * <p>Method under test: {@link ClassPathElement#setDir(File)}
   */
  @Test
  @DisplayName("Test setDir(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.setDir(File)"})
  void testSetDir() {
    // Arrange
    ClassPathElement classPathElement = new ClassPathElement(new Project());

    // Act
    classPathElement.setDir(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

    // Assert
    assertEquals(1, classPathElement.size());
    assertFalse(classPathElement.isEmpty());
  }

  /**
   * Test {@link ClassPathElement#setName(File)}.
   *
   * <p>Method under test: {@link ClassPathElement#setName(File)}
   */
  @Test
  @DisplayName("Test setName(File)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathElement.setName(File)"})
  void testSetName() {
    // Arrange
    ClassPathElement classPathElement = new ClassPathElement(new Project());

    // Act
    classPathElement.setName(Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile());

    // Assert
    assertEquals(1, classPathElement.size());
    assertFalse(classPathElement.isEmpty());
  }
}
