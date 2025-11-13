package proguard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ClassPathEntryDiffblueTest {
  /**
   * Test getters and setters.
   *
   * <ul>
   *   <li>When {@code Feature Name}.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link ClassPathEntry#ClassPathEntry(File, boolean, String)}
   *   <li>{@link ClassPathEntry#setFeatureName(String)}
   *   <li>{@link ClassPathEntry#setOutput(boolean)}
   *   <li>{@link ClassPathEntry#getAabFilter()}
   *   <li>{@link ClassPathEntry#getAarFilter()}
   *   <li>{@link ClassPathEntry#getApkFilter()}
   *   <li>{@link ClassPathEntry#getEarFilter()}
   *   <li>{@link ClassPathEntry#getFeatureName()}
   *   <li>{@link ClassPathEntry#getFile()}
   *   <li>{@link ClassPathEntry#getFilter()}
   *   <li>{@link ClassPathEntry#getJarFilter()}
   *   <li>{@link ClassPathEntry#getJmodFilter()}
   *   <li>{@link ClassPathEntry#getWarFilter()}
   *   <li>{@link ClassPathEntry#getZipFilter()}
   *   <li>{@link ClassPathEntry#isOutput()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters; when 'Feature Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassPathEntry.<init>(File, boolean)",
    "void ClassPathEntry.<init>(File, boolean, String)",
    "List ClassPathEntry.getAabFilter()",
    "List ClassPathEntry.getAarFilter()",
    "List ClassPathEntry.getApkFilter()",
    "List ClassPathEntry.getEarFilter()",
    "String ClassPathEntry.getFeatureName()",
    "File ClassPathEntry.getFile()",
    "List ClassPathEntry.getFilter()",
    "List ClassPathEntry.getJarFilter()",
    "List ClassPathEntry.getJmodFilter()",
    "List ClassPathEntry.getWarFilter()",
    "List ClassPathEntry.getZipFilter()",
    "boolean ClassPathEntry.isOutput()",
    "void ClassPathEntry.setFeatureName(String)",
    "void ClassPathEntry.setOutput(boolean)"
  })
  void testGettersAndSetters_whenFeatureName() {
    // Arrange
    File file = Configuration.STD_OUT;

    // Act
    ClassPathEntry actualClassPathEntry = new ClassPathEntry(file, true, "Feature Name");
    actualClassPathEntry.setFeatureName("Feature Name");
    actualClassPathEntry.setOutput(true);
    List<String> actualAabFilter = actualClassPathEntry.getAabFilter();
    List<String> actualAarFilter = actualClassPathEntry.getAarFilter();
    List<String> actualApkFilter = actualClassPathEntry.getApkFilter();
    List<String> actualEarFilter = actualClassPathEntry.getEarFilter();
    String actualFeatureName = actualClassPathEntry.getFeatureName();
    File actualFile = actualClassPathEntry.getFile();
    List<String> actualFilter = actualClassPathEntry.getFilter();
    List<String> actualJarFilter = actualClassPathEntry.getJarFilter();
    List<String> actualJmodFilter = actualClassPathEntry.getJmodFilter();
    List<String> actualWarFilter = actualClassPathEntry.getWarFilter();
    List<String> actualZipFilter = actualClassPathEntry.getZipFilter();

    // Assert
    assertEquals("Feature Name", actualFeatureName);
    assertNull(actualAabFilter);
    assertNull(actualAarFilter);
    assertNull(actualApkFilter);
    assertNull(actualEarFilter);
    assertNull(actualFilter);
    assertNull(actualJarFilter);
    assertNull(actualJmodFilter);
    assertNull(actualWarFilter);
    assertNull(actualZipFilter);
    assertTrue(actualClassPathEntry.isOutput());
    assertSame(file, actualFile);
  }

  /**
   * Test getters and setters.
   *
   * <ul>
   *   <li>When {@link Configuration#STD_OUT}.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link ClassPathEntry#ClassPathEntry(File, boolean)}
   *   <li>{@link ClassPathEntry#setFeatureName(String)}
   *   <li>{@link ClassPathEntry#setOutput(boolean)}
   *   <li>{@link ClassPathEntry#getAabFilter()}
   *   <li>{@link ClassPathEntry#getAarFilter()}
   *   <li>{@link ClassPathEntry#getApkFilter()}
   *   <li>{@link ClassPathEntry#getEarFilter()}
   *   <li>{@link ClassPathEntry#getFeatureName()}
   *   <li>{@link ClassPathEntry#getFile()}
   *   <li>{@link ClassPathEntry#getFilter()}
   *   <li>{@link ClassPathEntry#getJarFilter()}
   *   <li>{@link ClassPathEntry#getJmodFilter()}
   *   <li>{@link ClassPathEntry#getWarFilter()}
   *   <li>{@link ClassPathEntry#getZipFilter()}
   *   <li>{@link ClassPathEntry#isOutput()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters; when STD_OUT")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassPathEntry.<init>(File, boolean)",
    "void ClassPathEntry.<init>(File, boolean, String)",
    "List ClassPathEntry.getAabFilter()",
    "List ClassPathEntry.getAarFilter()",
    "List ClassPathEntry.getApkFilter()",
    "List ClassPathEntry.getEarFilter()",
    "String ClassPathEntry.getFeatureName()",
    "File ClassPathEntry.getFile()",
    "List ClassPathEntry.getFilter()",
    "List ClassPathEntry.getJarFilter()",
    "List ClassPathEntry.getJmodFilter()",
    "List ClassPathEntry.getWarFilter()",
    "List ClassPathEntry.getZipFilter()",
    "boolean ClassPathEntry.isOutput()",
    "void ClassPathEntry.setFeatureName(String)",
    "void ClassPathEntry.setOutput(boolean)"
  })
  void testGettersAndSetters_whenStd_out() {
    // Arrange
    File file = Configuration.STD_OUT;

    // Act
    ClassPathEntry actualClassPathEntry = new ClassPathEntry(file, true);
    actualClassPathEntry.setFeatureName("Feature Name");
    actualClassPathEntry.setOutput(true);
    List<String> actualAabFilter = actualClassPathEntry.getAabFilter();
    List<String> actualAarFilter = actualClassPathEntry.getAarFilter();
    List<String> actualApkFilter = actualClassPathEntry.getApkFilter();
    List<String> actualEarFilter = actualClassPathEntry.getEarFilter();
    String actualFeatureName = actualClassPathEntry.getFeatureName();
    File actualFile = actualClassPathEntry.getFile();
    List<String> actualFilter = actualClassPathEntry.getFilter();
    List<String> actualJarFilter = actualClassPathEntry.getJarFilter();
    List<String> actualJmodFilter = actualClassPathEntry.getJmodFilter();
    List<String> actualWarFilter = actualClassPathEntry.getWarFilter();
    List<String> actualZipFilter = actualClassPathEntry.getZipFilter();

    // Assert
    assertEquals("Feature Name", actualFeatureName);
    assertNull(actualAabFilter);
    assertNull(actualAarFilter);
    assertNull(actualApkFilter);
    assertNull(actualEarFilter);
    assertNull(actualFilter);
    assertNull(actualJarFilter);
    assertNull(actualJmodFilter);
    assertNull(actualWarFilter);
    assertNull(actualZipFilter);
    assertTrue(actualClassPathEntry.isOutput());
    assertSame(file, actualFile);
  }

  /**
   * Test {@link ClassPathEntry#getName()}.
   *
   * <ul>
   *   <li>Then return Property is {@code user.dir}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#getName()}
   */
  @Test
  @DisplayName("Test getName(); then return Property is 'user.dir'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassPathEntry.getName()"})
  void testGetName_thenReturnPropertyIsUserDir() {
    // Arrange, Act and Assert
    assertEquals(
        System.getProperty("user.dir"), new ClassPathEntry(Configuration.STD_OUT, true).getName());
  }

  /**
   * Test {@link ClassPathEntry#isDex()}.
   *
   * <ul>
   *   <li>Given {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#isDex()}
   */
  @Test
  @DisplayName(
      "Test isDex(); given ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isDex()"})
  void testIsDex_givenClassPathEntryWithFileIsStd_outAndIsOutputIsTrue_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ClassPathEntry(Configuration.STD_OUT, true).isDex());
  }

  /**
   * Test {@link ClassPathEntry#isApk()}.
   *
   * <ul>
   *   <li>Given {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#isApk()}
   */
  @Test
  @DisplayName(
      "Test isApk(); given ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isApk()"})
  void testIsApk_givenClassPathEntryWithFileIsStd_outAndIsOutputIsTrue_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ClassPathEntry(Configuration.STD_OUT, true).isApk());
  }

  /**
   * Test {@link ClassPathEntry#isAab()}.
   *
   * <ul>
   *   <li>Given {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#isAab()}
   */
  @Test
  @DisplayName(
      "Test isAab(); given ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isAab()"})
  void testIsAab_givenClassPathEntryWithFileIsStd_outAndIsOutputIsTrue_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ClassPathEntry(Configuration.STD_OUT, true).isAab());
  }

  /**
   * Test {@link ClassPathEntry#isJar()}.
   *
   * <ul>
   *   <li>Given {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#isJar()}
   */
  @Test
  @DisplayName(
      "Test isJar(); given ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isJar()"})
  void testIsJar_givenClassPathEntryWithFileIsStd_outAndIsOutputIsTrue_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ClassPathEntry(Configuration.STD_OUT, true).isJar());
  }

  /**
   * Test {@link ClassPathEntry#isAar()}.
   *
   * <ul>
   *   <li>Given {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#isAar()}
   */
  @Test
  @DisplayName(
      "Test isAar(); given ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isAar()"})
  void testIsAar_givenClassPathEntryWithFileIsStd_outAndIsOutputIsTrue_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ClassPathEntry(Configuration.STD_OUT, true).isAar());
  }

  /**
   * Test {@link ClassPathEntry#isWar()}.
   *
   * <ul>
   *   <li>Given {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#isWar()}
   */
  @Test
  @DisplayName(
      "Test isWar(); given ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isWar()"})
  void testIsWar_givenClassPathEntryWithFileIsStd_outAndIsOutputIsTrue_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ClassPathEntry(Configuration.STD_OUT, true).isWar());
  }

  /**
   * Test {@link ClassPathEntry#isEar()}.
   *
   * <ul>
   *   <li>Given {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#isEar()}
   */
  @Test
  @DisplayName(
      "Test isEar(); given ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isEar()"})
  void testIsEar_givenClassPathEntryWithFileIsStd_outAndIsOutputIsTrue_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ClassPathEntry(Configuration.STD_OUT, true).isEar());
  }

  /**
   * Test {@link ClassPathEntry#isJmod()}.
   *
   * <ul>
   *   <li>Given {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#isJmod()}
   */
  @Test
  @DisplayName(
      "Test isJmod(); given ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isJmod()"})
  void testIsJmod_givenClassPathEntryWithFileIsStd_outAndIsOutputIsTrue_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ClassPathEntry(Configuration.STD_OUT, true).isJmod());
  }

  /**
   * Test {@link ClassPathEntry#isZip()}.
   *
   * <ul>
   *   <li>Given {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#isZip()}
   */
  @Test
  @DisplayName(
      "Test isZip(); given ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isZip()"})
  void testIsZip_givenClassPathEntryWithFileIsStd_outAndIsOutputIsTrue_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ClassPathEntry(Configuration.STD_OUT, true).isZip());
  }

  /**
   * Test {@link ClassPathEntry#isFiltered()}.
   *
   * <p>Method under test: {@link ClassPathEntry#isFiltered()}
   */
  @Test
  @DisplayName("Test isFiltered()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isFiltered()"})
  void testIsFiltered() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);

    // Act and Assert
    assertTrue(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#isFiltered()}.
   *
   * <p>Method under test: {@link ClassPathEntry#isFiltered()}
   */
  @Test
  @DisplayName("Test isFiltered()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isFiltered()"})
  void testIsFiltered2() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setApkFilter(filter);

    // Act and Assert
    assertTrue(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#isFiltered()}.
   *
   * <p>Method under test: {@link ClassPathEntry#isFiltered()}
   */
  @Test
  @DisplayName("Test isFiltered()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isFiltered()"})
  void testIsFiltered3() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setAabFilter(filter);

    // Act and Assert
    assertTrue(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#isFiltered()}.
   *
   * <p>Method under test: {@link ClassPathEntry#isFiltered()}
   */
  @Test
  @DisplayName("Test isFiltered()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isFiltered()"})
  void testIsFiltered4() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setJarFilter(filter);

    // Act and Assert
    assertTrue(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#isFiltered()}.
   *
   * <p>Method under test: {@link ClassPathEntry#isFiltered()}
   */
  @Test
  @DisplayName("Test isFiltered()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isFiltered()"})
  void testIsFiltered5() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setAarFilter(filter);

    // Act and Assert
    assertTrue(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#isFiltered()}.
   *
   * <p>Method under test: {@link ClassPathEntry#isFiltered()}
   */
  @Test
  @DisplayName("Test isFiltered()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isFiltered()"})
  void testIsFiltered6() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setWarFilter(filter);

    // Act and Assert
    assertTrue(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#isFiltered()}.
   *
   * <p>Method under test: {@link ClassPathEntry#isFiltered()}
   */
  @Test
  @DisplayName("Test isFiltered()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isFiltered()"})
  void testIsFiltered7() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(new ArrayList<>());
    classPathEntry.setApkFilter(new ArrayList<>());
    classPathEntry.setAabFilter(new ArrayList<>());
    classPathEntry.setJarFilter(new ArrayList<>());
    classPathEntry.setAarFilter(new ArrayList<>());
    classPathEntry.setWarFilter(new ArrayList<>());
    classPathEntry.setEarFilter(filter);
    classPathEntry.setJmodFilter(new ArrayList<>());
    classPathEntry.setZipFilter(new ArrayList<>());

    // Act and Assert
    assertTrue(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#isFiltered()}.
   *
   * <p>Method under test: {@link ClassPathEntry#isFiltered()}
   */
  @Test
  @DisplayName("Test isFiltered()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isFiltered()"})
  void testIsFiltered8() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(new ArrayList<>());
    classPathEntry.setApkFilter(new ArrayList<>());
    classPathEntry.setAabFilter(new ArrayList<>());
    classPathEntry.setJarFilter(new ArrayList<>());
    classPathEntry.setAarFilter(new ArrayList<>());
    classPathEntry.setWarFilter(new ArrayList<>());
    classPathEntry.setEarFilter(new ArrayList<>());
    classPathEntry.setJmodFilter(filter);
    classPathEntry.setZipFilter(null);

    // Act and Assert
    assertTrue(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#isFiltered()}.
   *
   * <p>Method under test: {@link ClassPathEntry#isFiltered()}
   */
  @Test
  @DisplayName("Test isFiltered()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isFiltered()"})
  void testIsFiltered9() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(new ArrayList<>());
    classPathEntry.setApkFilter(new ArrayList<>());
    classPathEntry.setAabFilter(new ArrayList<>());
    classPathEntry.setJarFilter(new ArrayList<>());
    classPathEntry.setAarFilter(new ArrayList<>());
    classPathEntry.setWarFilter(new ArrayList<>());
    classPathEntry.setEarFilter(new ArrayList<>());
    classPathEntry.setJmodFilter(null);
    classPathEntry.setZipFilter(filter);

    // Act and Assert
    assertTrue(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#isFiltered()}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#isFiltered()}
   */
  @Test
  @DisplayName("Test isFiltered(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassPathEntry.isFiltered()"})
  void testIsFiltered_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ClassPathEntry(Configuration.STD_OUT, true).isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setFilter(List)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ArrayList#ArrayList()} add {@code 42}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setFilter(List)}
   */
  @Test
  @DisplayName("Test setFilter(List); given '42'; when ArrayList() add '42'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setFilter(List)"})
  void testSetFilter_given42_whenArrayListAdd42() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("42");
    filter.add("foo");

    // Act
    classPathEntry.setFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getFilter());
  }

  /**
   * Test {@link ClassPathEntry#setFilter(List)}.
   *
   * <ul>
   *   <li>Then {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true} Filtered.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setFilter(List)}
   */
  @Test
  @DisplayName(
      "Test setFilter(List); then ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true' Filtered")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setFilter(List)"})
  void testSetFilter_thenClassPathEntryWithFileIsStd_outAndIsOutputIsTrueFiltered() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    // Act
    classPathEntry.setFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getFilter());
  }

  /**
   * Test {@link ClassPathEntry#setFilter(List)}.
   *
   * <ul>
   *   <li>When {@link ArrayList#ArrayList()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setFilter(List)}
   */
  @Test
  @DisplayName("Test setFilter(List); when ArrayList()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setFilter(List)"})
  void testSetFilter_whenArrayList() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setFilter(new ArrayList<>());

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setFilter(List)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setFilter(List)}
   */
  @Test
  @DisplayName("Test setFilter(List); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setFilter(List)"})
  void testSetFilter_whenNull() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setFilter(null);

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setApkFilter(List)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ArrayList#ArrayList()} add {@code 42}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setApkFilter(List)}
   */
  @Test
  @DisplayName("Test setApkFilter(List); given '42'; when ArrayList() add '42'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setApkFilter(List)"})
  void testSetApkFilter_given42_whenArrayListAdd42() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("42");
    filter.add("foo");

    // Act
    classPathEntry.setApkFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getApkFilter());
  }

  /**
   * Test {@link ClassPathEntry#setApkFilter(List)}.
   *
   * <ul>
   *   <li>Then {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true} Filtered.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setApkFilter(List)}
   */
  @Test
  @DisplayName(
      "Test setApkFilter(List); then ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true' Filtered")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setApkFilter(List)"})
  void testSetApkFilter_thenClassPathEntryWithFileIsStd_outAndIsOutputIsTrueFiltered() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    // Act
    classPathEntry.setApkFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getApkFilter());
  }

  /**
   * Test {@link ClassPathEntry#setApkFilter(List)}.
   *
   * <ul>
   *   <li>When {@link ArrayList#ArrayList()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setApkFilter(List)}
   */
  @Test
  @DisplayName("Test setApkFilter(List); when ArrayList()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setApkFilter(List)"})
  void testSetApkFilter_whenArrayList() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setApkFilter(new ArrayList<>());

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setApkFilter(List)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setApkFilter(List)}
   */
  @Test
  @DisplayName("Test setApkFilter(List); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setApkFilter(List)"})
  void testSetApkFilter_whenNull() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setApkFilter(null);

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setAabFilter(List)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ArrayList#ArrayList()} add {@code 42}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setAabFilter(List)}
   */
  @Test
  @DisplayName("Test setAabFilter(List); given '42'; when ArrayList() add '42'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setAabFilter(List)"})
  void testSetAabFilter_given42_whenArrayListAdd42() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("42");
    filter.add("foo");

    // Act
    classPathEntry.setAabFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getAabFilter());
  }

  /**
   * Test {@link ClassPathEntry#setAabFilter(List)}.
   *
   * <ul>
   *   <li>Then {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true} Filtered.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setAabFilter(List)}
   */
  @Test
  @DisplayName(
      "Test setAabFilter(List); then ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true' Filtered")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setAabFilter(List)"})
  void testSetAabFilter_thenClassPathEntryWithFileIsStd_outAndIsOutputIsTrueFiltered() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    // Act
    classPathEntry.setAabFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getAabFilter());
  }

  /**
   * Test {@link ClassPathEntry#setAabFilter(List)}.
   *
   * <ul>
   *   <li>When {@link ArrayList#ArrayList()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setAabFilter(List)}
   */
  @Test
  @DisplayName("Test setAabFilter(List); when ArrayList()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setAabFilter(List)"})
  void testSetAabFilter_whenArrayList() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setAabFilter(new ArrayList<>());

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setAabFilter(List)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setAabFilter(List)}
   */
  @Test
  @DisplayName("Test setAabFilter(List); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setAabFilter(List)"})
  void testSetAabFilter_whenNull() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setAabFilter(null);

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setJarFilter(List)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ArrayList#ArrayList()} add {@code 42}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setJarFilter(List)}
   */
  @Test
  @DisplayName("Test setJarFilter(List); given '42'; when ArrayList() add '42'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setJarFilter(List)"})
  void testSetJarFilter_given42_whenArrayListAdd42() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("42");
    filter.add("foo");

    // Act
    classPathEntry.setJarFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getJarFilter());
  }

  /**
   * Test {@link ClassPathEntry#setJarFilter(List)}.
   *
   * <ul>
   *   <li>Then {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true} Filtered.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setJarFilter(List)}
   */
  @Test
  @DisplayName(
      "Test setJarFilter(List); then ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true' Filtered")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setJarFilter(List)"})
  void testSetJarFilter_thenClassPathEntryWithFileIsStd_outAndIsOutputIsTrueFiltered() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    // Act
    classPathEntry.setJarFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getJarFilter());
  }

  /**
   * Test {@link ClassPathEntry#setJarFilter(List)}.
   *
   * <ul>
   *   <li>When {@link ArrayList#ArrayList()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setJarFilter(List)}
   */
  @Test
  @DisplayName("Test setJarFilter(List); when ArrayList()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setJarFilter(List)"})
  void testSetJarFilter_whenArrayList() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setJarFilter(new ArrayList<>());

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setJarFilter(List)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setJarFilter(List)}
   */
  @Test
  @DisplayName("Test setJarFilter(List); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setJarFilter(List)"})
  void testSetJarFilter_whenNull() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setJarFilter(null);

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setAarFilter(List)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ArrayList#ArrayList()} add {@code 42}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setAarFilter(List)}
   */
  @Test
  @DisplayName("Test setAarFilter(List); given '42'; when ArrayList() add '42'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setAarFilter(List)"})
  void testSetAarFilter_given42_whenArrayListAdd42() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("42");
    filter.add("foo");

    // Act
    classPathEntry.setAarFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getAarFilter());
  }

  /**
   * Test {@link ClassPathEntry#setAarFilter(List)}.
   *
   * <ul>
   *   <li>Then {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true} Filtered.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setAarFilter(List)}
   */
  @Test
  @DisplayName(
      "Test setAarFilter(List); then ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true' Filtered")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setAarFilter(List)"})
  void testSetAarFilter_thenClassPathEntryWithFileIsStd_outAndIsOutputIsTrueFiltered() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    // Act
    classPathEntry.setAarFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getAarFilter());
  }

  /**
   * Test {@link ClassPathEntry#setAarFilter(List)}.
   *
   * <ul>
   *   <li>When {@link ArrayList#ArrayList()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setAarFilter(List)}
   */
  @Test
  @DisplayName("Test setAarFilter(List); when ArrayList()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setAarFilter(List)"})
  void testSetAarFilter_whenArrayList() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setAarFilter(new ArrayList<>());

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setAarFilter(List)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setAarFilter(List)}
   */
  @Test
  @DisplayName("Test setAarFilter(List); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setAarFilter(List)"})
  void testSetAarFilter_whenNull() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setAarFilter(null);

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setWarFilter(List)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ArrayList#ArrayList()} add {@code 42}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setWarFilter(List)}
   */
  @Test
  @DisplayName("Test setWarFilter(List); given '42'; when ArrayList() add '42'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setWarFilter(List)"})
  void testSetWarFilter_given42_whenArrayListAdd42() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("42");
    filter.add("foo");

    // Act
    classPathEntry.setWarFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getWarFilter());
  }

  /**
   * Test {@link ClassPathEntry#setWarFilter(List)}.
   *
   * <ul>
   *   <li>Then {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true} Filtered.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setWarFilter(List)}
   */
  @Test
  @DisplayName(
      "Test setWarFilter(List); then ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true' Filtered")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setWarFilter(List)"})
  void testSetWarFilter_thenClassPathEntryWithFileIsStd_outAndIsOutputIsTrueFiltered() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    // Act
    classPathEntry.setWarFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getWarFilter());
  }

  /**
   * Test {@link ClassPathEntry#setWarFilter(List)}.
   *
   * <ul>
   *   <li>When {@link ArrayList#ArrayList()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setWarFilter(List)}
   */
  @Test
  @DisplayName("Test setWarFilter(List); when ArrayList()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setWarFilter(List)"})
  void testSetWarFilter_whenArrayList() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setWarFilter(new ArrayList<>());

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setWarFilter(List)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setWarFilter(List)}
   */
  @Test
  @DisplayName("Test setWarFilter(List); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setWarFilter(List)"})
  void testSetWarFilter_whenNull() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setWarFilter(null);

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setEarFilter(List)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ArrayList#ArrayList()} add {@code 42}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setEarFilter(List)}
   */
  @Test
  @DisplayName("Test setEarFilter(List); given '42'; when ArrayList() add '42'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setEarFilter(List)"})
  void testSetEarFilter_given42_whenArrayListAdd42() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("42");
    filter.add("foo");

    // Act
    classPathEntry.setEarFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getEarFilter());
  }

  /**
   * Test {@link ClassPathEntry#setEarFilter(List)}.
   *
   * <ul>
   *   <li>Then {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true} Filtered.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setEarFilter(List)}
   */
  @Test
  @DisplayName(
      "Test setEarFilter(List); then ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true' Filtered")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setEarFilter(List)"})
  void testSetEarFilter_thenClassPathEntryWithFileIsStd_outAndIsOutputIsTrueFiltered() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    // Act
    classPathEntry.setEarFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getEarFilter());
  }

  /**
   * Test {@link ClassPathEntry#setEarFilter(List)}.
   *
   * <ul>
   *   <li>When {@link ArrayList#ArrayList()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setEarFilter(List)}
   */
  @Test
  @DisplayName("Test setEarFilter(List); when ArrayList()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setEarFilter(List)"})
  void testSetEarFilter_whenArrayList() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setEarFilter(new ArrayList<>());

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setEarFilter(List)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setEarFilter(List)}
   */
  @Test
  @DisplayName("Test setEarFilter(List); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setEarFilter(List)"})
  void testSetEarFilter_whenNull() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setEarFilter(null);

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setJmodFilter(List)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ArrayList#ArrayList()} add {@code 42}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setJmodFilter(List)}
   */
  @Test
  @DisplayName("Test setJmodFilter(List); given '42'; when ArrayList() add '42'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setJmodFilter(List)"})
  void testSetJmodFilter_given42_whenArrayListAdd42() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("42");
    filter.add("foo");

    // Act
    classPathEntry.setJmodFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getJmodFilter());
  }

  /**
   * Test {@link ClassPathEntry#setJmodFilter(List)}.
   *
   * <ul>
   *   <li>Then {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true} Filtered.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setJmodFilter(List)}
   */
  @Test
  @DisplayName(
      "Test setJmodFilter(List); then ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true' Filtered")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setJmodFilter(List)"})
  void testSetJmodFilter_thenClassPathEntryWithFileIsStd_outAndIsOutputIsTrueFiltered() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    // Act
    classPathEntry.setJmodFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getJmodFilter());
  }

  /**
   * Test {@link ClassPathEntry#setJmodFilter(List)}.
   *
   * <ul>
   *   <li>When {@link ArrayList#ArrayList()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setJmodFilter(List)}
   */
  @Test
  @DisplayName("Test setJmodFilter(List); when ArrayList()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setJmodFilter(List)"})
  void testSetJmodFilter_whenArrayList() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setJmodFilter(new ArrayList<>());

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setJmodFilter(List)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setJmodFilter(List)}
   */
  @Test
  @DisplayName("Test setJmodFilter(List); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setJmodFilter(List)"})
  void testSetJmodFilter_whenNull() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setJmodFilter(null);

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setZipFilter(List)}.
   *
   * <ul>
   *   <li>Given {@code 42}.
   *   <li>When {@link ArrayList#ArrayList()} add {@code 42}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setZipFilter(List)}
   */
  @Test
  @DisplayName("Test setZipFilter(List); given '42'; when ArrayList() add '42'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setZipFilter(List)"})
  void testSetZipFilter_given42_whenArrayListAdd42() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("42");
    filter.add("foo");

    // Act
    classPathEntry.setZipFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getZipFilter());
  }

  /**
   * Test {@link ClassPathEntry#setZipFilter(List)}.
   *
   * <ul>
   *   <li>Then {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code true} Filtered.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setZipFilter(List)}
   */
  @Test
  @DisplayName(
      "Test setZipFilter(List); then ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'true' Filtered")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setZipFilter(List)"})
  void testSetZipFilter_thenClassPathEntryWithFileIsStd_outAndIsOutputIsTrueFiltered() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    // Act
    classPathEntry.setZipFilter(filter);

    // Assert
    assertTrue(classPathEntry.isFiltered());
    assertSame(filter, classPathEntry.getZipFilter());
  }

  /**
   * Test {@link ClassPathEntry#setZipFilter(List)}.
   *
   * <ul>
   *   <li>When {@link ArrayList#ArrayList()}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setZipFilter(List)}
   */
  @Test
  @DisplayName("Test setZipFilter(List); when ArrayList()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setZipFilter(List)"})
  void testSetZipFilter_whenArrayList() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setZipFilter(new ArrayList<>());

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#setZipFilter(List)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#setZipFilter(List)}
   */
  @Test
  @DisplayName("Test setZipFilter(List); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassPathEntry.setZipFilter(List)"})
  void testSetZipFilter_whenNull() {
    // Arrange
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act
    classPathEntry.setZipFilter(null);

    // Assert that nothing has changed
    assertFalse(classPathEntry.isFiltered());
  }

  /**
   * Test {@link ClassPathEntry#toString()}.
   *
   * <p>Method under test: {@link ClassPathEntry#toString()}
   */
  @Test
  @DisplayName("Test toString()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassPathEntry.toString()"})
  void testToString() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);

    // Act
    String actualToStringResult = classPathEntry.toString();

    // Assert
    String expectedToStringResult = String.join("", System.getProperty("user.dir"), "(;;;;;;;;'')");
    assertEquals(expectedToStringResult, actualToStringResult);
  }

  /**
   * Test {@link ClassPathEntry#toString()}.
   *
   * <ul>
   *   <li>Then return join empty string and Property is {@code user.dir} and {@code (;;;;;;;;foo)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#toString()}
   */
  @Test
  @DisplayName(
      "Test toString(); then return join empty string and Property is 'user.dir' and '(;;;;;;;;foo)'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassPathEntry.toString()"})
  void testToString_thenReturnJoinEmptyStringAndPropertyIsUserDirAndFoo() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);

    // Act
    String actualToStringResult = classPathEntry.toString();

    // Assert
    String expectedToStringResult =
        String.join("", System.getProperty("user.dir"), "(;;;;;;;;foo)");
    assertEquals(expectedToStringResult, actualToStringResult);
  }

  /**
   * Test {@link ClassPathEntry#toString()}.
   *
   * <ul>
   *   <li>Then return join empty string and Property is {@code user.dir} and {@code
   *       (;;;;;;;;';',foo)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#toString()}
   */
  @Test
  @DisplayName(
      "Test toString(); then return join empty string and Property is 'user.dir' and '(;;;;;;;;';',foo)'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassPathEntry.toString()"})
  void testToString_thenReturnJoinEmptyStringAndPropertyIsUserDirAndFoo2() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add(ConfigurationConstants.SEPARATOR_KEYWORD);
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);

    // Act
    String actualToStringResult = classPathEntry.toString();

    // Assert
    String expectedToStringResult =
        String.join("", System.getProperty("user.dir"), "(;;;;;;;;';',foo)");
    assertEquals(expectedToStringResult, actualToStringResult);
  }

  /**
   * Test {@link ClassPathEntry#toString()}.
   *
   * <ul>
   *   <li>Then return join empty string and Property is {@code user.dir} and {@code
   *       (;;;;;;;;')',';',foo)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#toString()}
   */
  @Test
  @DisplayName(
      "Test toString(); then return join empty string and Property is 'user.dir' and '(;;;;;;;;')',';',foo)'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassPathEntry.toString()"})
  void testToString_thenReturnJoinEmptyStringAndPropertyIsUserDirAndFoo3() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add(ConfigurationConstants.CLOSE_ARGUMENTS_KEYWORD);
    filter.add(ConfigurationConstants.SEPARATOR_KEYWORD);
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);

    // Act
    String actualToStringResult = classPathEntry.toString();

    // Assert
    String expectedToStringResult =
        String.join("", System.getProperty("user.dir"), "(;;;;;;;;')',';',foo)");
    assertEquals(expectedToStringResult, actualToStringResult);
  }

  /**
   * Test {@link ClassPathEntry#toString()}.
   *
   * <ul>
   *   <li>Then return join empty string and Property is {@code user.dir} and {@code
   *       (;;;;;;;;'(',')',';',foo)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#toString()}
   */
  @Test
  @DisplayName(
      "Test toString(); then return join empty string and Property is 'user.dir' and '(;;;;;;;;'(',')',';',foo)'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassPathEntry.toString()"})
  void testToString_thenReturnJoinEmptyStringAndPropertyIsUserDirAndFoo4() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add(ConfigurationConstants.OPEN_ARGUMENTS_KEYWORD);
    filter.add(ConfigurationConstants.CLOSE_ARGUMENTS_KEYWORD);
    filter.add(ConfigurationConstants.SEPARATOR_KEYWORD);
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);

    // Act
    String actualToStringResult = classPathEntry.toString();

    // Assert
    String expectedToStringResult =
        String.join("", System.getProperty("user.dir"), "(;;;;;;;;'(',')',';',foo)");
    assertEquals(expectedToStringResult, actualToStringResult);
  }

  /**
   * Test {@link ClassPathEntry#toString()}.
   *
   * <ul>
   *   <li>Then return join empty string and Property is {@code user.dir} and {@code (;;foo;;;;;;)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#toString()}
   */
  @Test
  @DisplayName(
      "Test toString(); then return join empty string and Property is 'user.dir' and '(;;foo;;;;;;)'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassPathEntry.toString()"})
  void testToString_thenReturnJoinEmptyStringAndPropertyIsUserDirAndFoo5() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setApkFilter(filter);

    // Act
    String actualToStringResult = classPathEntry.toString();

    // Assert
    String expectedToStringResult =
        String.join("", System.getProperty("user.dir"), "(;;foo;;;;;;)");
    assertEquals(expectedToStringResult, actualToStringResult);
  }

  /**
   * Test {@link ClassPathEntry#toString()}.
   *
   * <ul>
   *   <li>Then return join empty string and Property is {@code user.dir} and {@code (;foo;;;;;;;)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#toString()}
   */
  @Test
  @DisplayName(
      "Test toString(); then return join empty string and Property is 'user.dir' and '(;foo;;;;;;;)'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassPathEntry.toString()"})
  void testToString_thenReturnJoinEmptyStringAndPropertyIsUserDirAndFoo6() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setAabFilter(filter);

    // Act
    String actualToStringResult = classPathEntry.toString();

    // Assert
    String expectedToStringResult =
        String.join("", System.getProperty("user.dir"), "(;foo;;;;;;;)");
    assertEquals(expectedToStringResult, actualToStringResult);
  }

  /**
   * Test {@link ClassPathEntry#toString()}.
   *
   * <ul>
   *   <li>Then return join empty string and Property is {@code user.dir} and {@code (;;;;;;;foo;)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#toString()}
   */
  @Test
  @DisplayName(
      "Test toString(); then return join empty string and Property is 'user.dir' and '(;;;;;;;foo;)'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassPathEntry.toString()"})
  void testToString_thenReturnJoinEmptyStringAndPropertyIsUserDirAndFoo7() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setJarFilter(filter);

    // Act
    String actualToStringResult = classPathEntry.toString();

    // Assert
    String expectedToStringResult =
        String.join("", System.getProperty("user.dir"), "(;;;;;;;foo;)");
    assertEquals(expectedToStringResult, actualToStringResult);
  }

  /**
   * Test {@link ClassPathEntry#toString()}.
   *
   * <ul>
   *   <li>Then return join empty string and Property is {@code user.dir} and {@code (foo;;;;;;;;)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#toString()}
   */
  @Test
  @DisplayName(
      "Test toString(); then return join empty string and Property is 'user.dir' and '(foo;;;;;;;;)'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassPathEntry.toString()"})
  void testToString_thenReturnJoinEmptyStringAndPropertyIsUserDirAndFoo8() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setAarFilter(filter);

    // Act
    String actualToStringResult = classPathEntry.toString();

    // Assert
    String expectedToStringResult =
        String.join("", System.getProperty("user.dir"), "(foo;;;;;;;;)");
    assertEquals(expectedToStringResult, actualToStringResult);
  }

  /**
   * Test {@link ClassPathEntry#toString()}.
   *
   * <ul>
   *   <li>Then return join empty string and Property is {@code user.dir} and {@code (;;;;;;foo;;)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#toString()}
   */
  @Test
  @DisplayName(
      "Test toString(); then return join empty string and Property is 'user.dir' and '(;;;;;;foo;;)'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassPathEntry.toString()"})
  void testToString_thenReturnJoinEmptyStringAndPropertyIsUserDirAndFoo9() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setWarFilter(filter);

    // Act
    String actualToStringResult = classPathEntry.toString();

    // Assert
    String expectedToStringResult =
        String.join("", System.getProperty("user.dir"), "(;;;;;;foo;;)");
    assertEquals(expectedToStringResult, actualToStringResult);
  }

  /**
   * Test {@link ClassPathEntry#toString()}.
   *
   * <ul>
   *   <li>Then return join empty string and Property is {@code user.dir} and {@code (;;;;;foo;;;)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#toString()}
   */
  @Test
  @DisplayName(
      "Test toString(); then return join empty string and Property is 'user.dir' and '(;;;;;foo;;;)'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassPathEntry.toString()"})
  void testToString_thenReturnJoinEmptyStringAndPropertyIsUserDirAndFoo10() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setEarFilter(filter);

    // Act
    String actualToStringResult = classPathEntry.toString();

    // Assert
    String expectedToStringResult =
        String.join("", System.getProperty("user.dir"), "(;;;;;foo;;;)");
    assertEquals(expectedToStringResult, actualToStringResult);
  }

  /**
   * Test {@link ClassPathEntry#toString()}.
   *
   * <ul>
   *   <li>Then return join empty string and Property is {@code user.dir} and {@code (;;;;foo;;;;)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#toString()}
   */
  @Test
  @DisplayName(
      "Test toString(); then return join empty string and Property is 'user.dir' and '(;;;;foo;;;;)'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassPathEntry.toString()"})
  void testToString_thenReturnJoinEmptyStringAndPropertyIsUserDirAndFoo11() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setJmodFilter(filter);

    // Act
    String actualToStringResult = classPathEntry.toString();

    // Assert
    String expectedToStringResult =
        String.join("", System.getProperty("user.dir"), "(;;;;foo;;;;)");
    assertEquals(expectedToStringResult, actualToStringResult);
  }

  /**
   * Test {@link ClassPathEntry#toString()}.
   *
   * <ul>
   *   <li>Then return Property is {@code user.dir}.
   * </ul>
   *
   * <p>Method under test: {@link ClassPathEntry#toString()}
   */
  @Test
  @DisplayName("Test toString(); then return Property is 'user.dir'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ClassPathEntry.toString()"})
  void testToString_thenReturnPropertyIsUserDir() {
    // Arrange, Act and Assert
    assertEquals(
        System.getProperty("user.dir"), new ClassPathEntry(Configuration.STD_OUT, true).toString());
  }
}
