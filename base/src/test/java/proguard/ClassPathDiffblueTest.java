package proguard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ClassPathDiffblueTest {
  /**
   * Test {@link ClassPath#hasOutput()}.
   *
   * <ul>
   *   <li>Given {@link ClassPath} (default constructor) add {@link
   *       ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link Configuration#STD_OUT}
   *       and isOutput is {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPath#hasOutput()}
   */
  @Test
  @DisplayName(
      "Test hasOutput(); given ClassPath (default constructor) add ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPath.hasOutput()"})
  void testHasOutput_givenClassPathAddClassPathEntryWithFileIsStd_outAndIsOutputIsFalse() {
    // Arrange
    ClassPath classPath = new ClassPath();
    classPath.add(new ClassPathEntry(Configuration.STD_OUT, false));

    // Act and Assert
    assertFalse(classPath.hasOutput());
  }

  /**
   * Test {@link ClassPath#hasOutput()}.
   *
   * <ul>
   *   <li>Given {@link ClassPath} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPath#hasOutput()}
   */
  @Test
  @DisplayName("Test hasOutput(); given ClassPath (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPath.hasOutput()"})
  void testHasOutput_givenClassPath_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ClassPath().hasOutput());
  }

  /**
   * Test {@link ClassPath#hasOutput()}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPath#hasOutput()}
   */
  @Test
  @DisplayName("Test hasOutput(); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPath.hasOutput()"})
  void testHasOutput_thenReturnTrue() {
    // Arrange
    ClassPath classPath = new ClassPath();
    classPath.add(new ClassPathEntry(Configuration.STD_OUT, true));

    // Act and Assert
    assertTrue(classPath.hasOutput());
  }

  /**
   * Test {@link ClassPath#add(ClassPathEntry)} with {@code classPathEntry}.
   *
   * <p>Method under test: {@link ClassPath#add(ClassPathEntry)}
   */
  @Test
  @DisplayName("Test add(ClassPathEntry) with 'classPathEntry'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPath.add(ClassPathEntry)"})
  void testAddWithClassPathEntry() {
    // Arrange
    ClassPath classPath = new ClassPath();

    // Act
    boolean actualAddResult = classPath.add(new ClassPathEntry(Configuration.STD_OUT, true));

    // Assert
    assertEquals(1, classPath.size());
    assertFalse(classPath.isEmpty());
    assertTrue(actualAddResult);
    assertTrue(classPath.hasOutput());
  }

  /**
   * Test {@link ClassPath#add(int, ClassPathEntry)} with {@code index}, {@code classPathEntry}.
   *
   * <ul>
   *   <li>Then {@link ClassPath} (default constructor) size is two.
   * </ul>
   *
   * <p>Method under test: {@link ClassPath#add(int, ClassPathEntry)}
   */
  @Test
  @DisplayName(
      "Test add(int, ClassPathEntry) with 'index', 'classPathEntry'; then ClassPath (default constructor) size is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPath.add(int, ClassPathEntry)"})
  void testAddWithIndexClassPathEntry_thenClassPathSizeIsTwo() {
    // Arrange
    ClassPath classPath = new ClassPath();
    classPath.add(new ClassPathEntry(Configuration.STD_OUT, true));

    // Act
    classPath.add(1, new ClassPathEntry(Configuration.STD_OUT, true));

    // Assert
    assertEquals(2, classPath.size());
  }

  /**
   * Test {@link ClassPath#addAll(ClassPath)}.
   *
   * <ul>
   *   <li>Given {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true}.
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPath#addAll(ClassPath)}
   */
  @Test
  @DisplayName(
      "Test addAll(ClassPath); given ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true'; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPath.addAll(ClassPath)"})
  void testAddAll_givenClassPathEntryWithFileIsStd_outAndIsOutputIsTrue_thenReturnTrue() {
    // Arrange
    ClassPath classPath = new ClassPath();

    ClassPath classPath2 = new ClassPath();
    classPath2.add(new ClassPathEntry(Configuration.STD_OUT, true));

    // Act and Assert
    assertTrue(classPath.addAll(classPath2));
  }

  /**
   * Test {@link ClassPath#addAll(ClassPath)}.
   *
   * <ul>
   *   <li>When {@link ClassPath} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPath#addAll(ClassPath)}
   */
  @Test
  @DisplayName("Test addAll(ClassPath); when ClassPath (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPath.addAll(ClassPath)"})
  void testAddAll_whenClassPath_thenReturnFalse() {
    // Arrange
    ClassPath classPath = new ClassPath();

    // Act and Assert
    assertFalse(classPath.addAll(new ClassPath()));
  }

  /**
   * Test {@link ClassPath#get(int)}.
   *
   * <ul>
   *   <li>Then return {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPath#get(int)}
   */
  @Test
  @DisplayName(
      "Test get(int); then return ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"ClassPathEntry ClassPath.get(int)"})
  void testGet_thenReturnClassPathEntryWithFileIsStd_outAndIsOutputIsTrue() {
    // Arrange
    ClassPath classPath = new ClassPath();
    classPath.add(new ClassPathEntry(Configuration.STD_OUT, true));
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPath.add(classPathEntry);

    // Act and Assert
    assertSame(classPathEntry, classPath.get(1));
  }

  /**
   * Test {@link ClassPath#remove(int)}.
   *
   * <ul>
   *   <li>Then {@link ClassPath} (default constructor) size is one.
   * </ul>
   *
   * <p>Method under test: {@link ClassPath#remove(int)}
   */
  @Test
  @DisplayName("Test remove(int); then ClassPath (default constructor) size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"ClassPathEntry ClassPath.remove(int)"})
  void testRemove_thenClassPathSizeIsOne() {
    // Arrange
    ClassPath classPath = new ClassPath();
    classPath.add(new ClassPathEntry(Configuration.STD_OUT, true));
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPath.add(classPathEntry);

    // Act
    ClassPathEntry actualRemoveResult = classPath.remove(1);

    // Assert
    assertEquals(1, classPath.size());
    assertSame(classPathEntry, actualRemoveResult);
  }

  /**
   * Test {@link ClassPath#isEmpty()}.
   *
   * <ul>
   *   <li>Given {@link ClassPath} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPath#isEmpty()}
   */
  @Test
  @DisplayName("Test isEmpty(); given ClassPath (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPath.isEmpty()"})
  void testIsEmpty_givenClassPath_thenReturnTrue() {
    // Arrange, Act and Assert
    assertTrue(new ClassPath().isEmpty());
  }

  /**
   * Test {@link ClassPath#isEmpty()}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPath#isEmpty()}
   */
  @Test
  @DisplayName("Test isEmpty(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPath.isEmpty()"})
  void testIsEmpty_thenReturnFalse() {
    // Arrange
    ClassPath classPath = new ClassPath();
    classPath.add(new ClassPathEntry(Configuration.STD_OUT, true));

    // Act and Assert
    assertFalse(classPath.isEmpty());
  }

  /**
   * Test {@link ClassPath#size()}.
   *
   * <p>Method under test: {@link ClassPath#size()}
   */
  @Test
  @DisplayName("Test size()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"int ClassPath.size()"})
  void testSize() {
    // Arrange, Act and Assert
    assertEquals(0, new ClassPath().size());
  }

  /**
   * Test new {@link ClassPath} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link ClassPath}
   */
  @Test
  @DisplayName("Test new ClassPath (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPath.<init>()"})
  void testNewClassPath() {
    // Arrange and Act
    ClassPath actualClassPath = new ClassPath();

    // Assert
    assertEquals(0, actualClassPath.size());
    assertFalse(actualClassPath.hasOutput());
    assertTrue(actualClassPath.isEmpty());
  }
}
