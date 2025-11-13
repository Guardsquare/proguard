package proguard;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.io.ClassPathDataEntry;
import proguard.io.DataEntryReader;
import proguard.io.FilteredDataEntryReader;

class DataEntryReaderFactoryDiffblueTest {
  /**
   * Test {@link DataEntryReaderFactory#createDataEntryReader(ClassPathEntry, DataEntryReader)}.
   *
   * <p>Method under test: {@link DataEntryReaderFactory#createDataEntryReader(ClassPathEntry,
   * DataEntryReader)}
   */
  @Test
  @DisplayName("Test createDataEntryReader(ClassPathEntry, DataEntryReader)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "DataEntryReader DataEntryReaderFactory.createDataEntryReader(ClassPathEntry, DataEntryReader)"
  })
  void testCreateDataEntryReader() throws IOException {
    // Arrange
    DataEntryReaderFactory dataEntryReaderFactory = new DataEntryReaderFactory(false);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("META-INF/versions");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);
    classPathEntry.setApkFilter(new ArrayList<>());
    classPathEntry.setAabFilter(new ArrayList<>());
    classPathEntry.setJarFilter(new ArrayList<>());
    classPathEntry.setAarFilter(new ArrayList<>());
    classPathEntry.setWarFilter(new ArrayList<>());
    classPathEntry.setEarFilter(new ArrayList<>());
    classPathEntry.setJmodFilter(new ArrayList<>());
    classPathEntry.setZipFilter(new ArrayList<>());

    // Act
    DataEntryReader actualCreateDataEntryReaderResult =
        dataEntryReaderFactory.createDataEntryReader(classPathEntry, mock(DataEntryReader.class));
    Class<Object> clazz = Object.class;
    ClassPathDataEntry classPathDataEntry = new ClassPathDataEntry(clazz);
    actualCreateDataEntryReaderResult.read(classPathDataEntry);

    // Assert
    assertTrue(actualCreateDataEntryReaderResult instanceof FilteredDataEntryReader);
    assertEquals("java/lang/Object.class", classPathDataEntry.getName());
    assertEquals("java/lang/Object.class", classPathDataEntry.getOriginalName());
    assertNull(classPathEntry.getFeatureName());
    assertNull(classPathEntry.getAabFilter());
    assertNull(classPathEntry.getAarFilter());
    assertNull(classPathEntry.getApkFilter());
    assertNull(classPathEntry.getEarFilter());
    assertNull(classPathEntry.getJarFilter());
    assertNull(classPathEntry.getJmodFilter());
    assertNull(classPathEntry.getWarFilter());
    assertNull(classPathEntry.getZipFilter());
    assertNull(classPathDataEntry.getParent());
    assertEquals(-1L, classPathDataEntry.getSize());
    List<String> filter2 = classPathEntry.getFilter();
    assertEquals(1, filter2.size());
    byte[] byteArray = new byte[51];
    assertEquals(51, classPathDataEntry.getInputStream().read(byteArray));
    assertFalse(classPathEntry.isAab());
    assertFalse(classPathEntry.isAar());
    assertFalse(classPathEntry.isApk());
    assertFalse(classPathEntry.isDex());
    assertFalse(classPathEntry.isEar());
    assertFalse(classPathEntry.isJar());
    assertFalse(classPathEntry.isJmod());
    assertFalse(classPathEntry.isWar());
    assertFalse(classPathEntry.isZip());
    assertFalse(classPathDataEntry.isDirectory());
    assertTrue(classPathEntry.isFiltered());
    assertTrue(classPathEntry.isOutput());
    assertEquals(System.getProperty("user.dir"), classPathEntry.getName());
    assertSame(filter, filter2);
    assertArrayEquals(
        new byte[] {
          -54, -2, -70, -66, 0, 0, 0, '4', 0, 'N', 7, 0, '1', '\n', 0, 1, 0, '2', '\n', 0, 17, 0,
          '3', '\n', 0, '4', 0, '5', '\n', 0, 1, 0, '6', '\b', 0, '7', '\n', 0, 17, 0, '8', '\n', 0,
          '9', 0, ':', '\n', 0, 1, 0, ';'
        },
        byteArray);
  }

  /**
   * Test {@link DataEntryReaderFactory#createDataEntryReader(ClassPathEntry, DataEntryReader)}.
   *
   * <p>Method under test: {@link DataEntryReaderFactory#createDataEntryReader(ClassPathEntry,
   * DataEntryReader)}
   */
  @Test
  @DisplayName("Test createDataEntryReader(ClassPathEntry, DataEntryReader)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "DataEntryReader DataEntryReaderFactory.createDataEntryReader(ClassPathEntry, DataEntryReader)"
  })
  void testCreateDataEntryReader2() {
    // Arrange
    DataEntryReaderFactory dataEntryReaderFactory = new DataEntryReaderFactory(true);
    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);

    // Act and Assert
    assertTrue(
        dataEntryReaderFactory.createDataEntryReader(classPathEntry, mock(DataEntryReader.class))
            instanceof FilteredDataEntryReader);
    assertNull(classPathEntry.getFeatureName());
    assertNull(classPathEntry.getAabFilter());
    assertNull(classPathEntry.getAarFilter());
    assertNull(classPathEntry.getApkFilter());
    assertNull(classPathEntry.getEarFilter());
    assertNull(classPathEntry.getFilter());
    assertNull(classPathEntry.getJarFilter());
    assertNull(classPathEntry.getJmodFilter());
    assertNull(classPathEntry.getWarFilter());
    assertNull(classPathEntry.getZipFilter());
    assertFalse(classPathEntry.isAab());
    assertFalse(classPathEntry.isAar());
    assertFalse(classPathEntry.isApk());
    assertFalse(classPathEntry.isDex());
    assertFalse(classPathEntry.isEar());
    assertFalse(classPathEntry.isFiltered());
    assertFalse(classPathEntry.isJar());
    assertFalse(classPathEntry.isJmod());
    assertFalse(classPathEntry.isWar());
    assertFalse(classPathEntry.isZip());
    assertTrue(classPathEntry.isOutput());
    assertEquals(System.getProperty("user.dir"), classPathEntry.getName());
  }

  /**
   * Test {@link DataEntryReaderFactory#createDataEntryReader(ClassPathEntry, DataEntryReader)}.
   *
   * <p>Method under test: {@link DataEntryReaderFactory#createDataEntryReader(ClassPathEntry,
   * DataEntryReader)}
   */
  @Test
  @DisplayName("Test createDataEntryReader(ClassPathEntry, DataEntryReader)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "DataEntryReader DataEntryReaderFactory.createDataEntryReader(ClassPathEntry, DataEntryReader)"
  })
  void testCreateDataEntryReader3() throws IOException {
    // Arrange
    DataEntryReaderFactory dataEntryReaderFactory = new DataEntryReaderFactory(false);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("META-INF/versions");

    ArrayList<String> filter2 = new ArrayList<>();
    filter2.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);
    classPathEntry.setApkFilter(filter2);
    classPathEntry.setAabFilter(new ArrayList<>());
    classPathEntry.setJarFilter(new ArrayList<>());
    classPathEntry.setAarFilter(new ArrayList<>());
    classPathEntry.setWarFilter(new ArrayList<>());
    classPathEntry.setEarFilter(new ArrayList<>());
    classPathEntry.setJmodFilter(new ArrayList<>());
    classPathEntry.setZipFilter(new ArrayList<>());

    // Act
    DataEntryReader actualCreateDataEntryReaderResult =
        dataEntryReaderFactory.createDataEntryReader(classPathEntry, mock(DataEntryReader.class));
    Class<Object> clazz = Object.class;
    ClassPathDataEntry classPathDataEntry = new ClassPathDataEntry(clazz);
    actualCreateDataEntryReaderResult.read(classPathDataEntry);

    // Assert
    assertTrue(actualCreateDataEntryReaderResult instanceof FilteredDataEntryReader);
    assertEquals("java/lang/Object.class", classPathDataEntry.getName());
    assertEquals("java/lang/Object.class", classPathDataEntry.getOriginalName());
    assertNull(classPathEntry.getFeatureName());
    assertNull(classPathEntry.getAabFilter());
    assertNull(classPathEntry.getAarFilter());
    assertNull(classPathEntry.getEarFilter());
    assertNull(classPathEntry.getJarFilter());
    assertNull(classPathEntry.getJmodFilter());
    assertNull(classPathEntry.getWarFilter());
    assertNull(classPathEntry.getZipFilter());
    assertNull(classPathDataEntry.getParent());
    assertEquals(-1L, classPathDataEntry.getSize());
    List<String> apkFilter = classPathEntry.getApkFilter();
    assertEquals(1, apkFilter.size());
    List<String> filter3 = classPathEntry.getFilter();
    assertEquals(1, filter3.size());
    byte[] byteArray = new byte[51];
    assertEquals(51, classPathDataEntry.getInputStream().read(byteArray));
    assertFalse(classPathEntry.isAab());
    assertFalse(classPathEntry.isAar());
    assertFalse(classPathEntry.isApk());
    assertFalse(classPathEntry.isDex());
    assertFalse(classPathEntry.isEar());
    assertFalse(classPathEntry.isJar());
    assertFalse(classPathEntry.isJmod());
    assertFalse(classPathEntry.isWar());
    assertFalse(classPathEntry.isZip());
    assertFalse(classPathDataEntry.isDirectory());
    assertTrue(classPathEntry.isFiltered());
    assertTrue(classPathEntry.isOutput());
    assertEquals(System.getProperty("user.dir"), classPathEntry.getName());
    assertSame(filter2, apkFilter);
    assertSame(filter, filter3);
    assertArrayEquals(
        new byte[] {
          -54, -2, -70, -66, 0, 0, 0, '4', 0, 'N', 7, 0, '1', '\n', 0, 1, 0, '2', '\n', 0, 17, 0,
          '3', '\n', 0, '4', 0, '5', '\n', 0, 1, 0, '6', '\b', 0, '7', '\n', 0, 17, 0, '8', '\n', 0,
          '9', 0, ':', '\n', 0, 1, 0, ';'
        },
        byteArray);
  }

  /**
   * Test {@link DataEntryReaderFactory#createDataEntryReader(ClassPathEntry, DataEntryReader)}.
   *
   * <p>Method under test: {@link DataEntryReaderFactory#createDataEntryReader(ClassPathEntry,
   * DataEntryReader)}
   */
  @Test
  @DisplayName("Test createDataEntryReader(ClassPathEntry, DataEntryReader)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "DataEntryReader DataEntryReaderFactory.createDataEntryReader(ClassPathEntry, DataEntryReader)"
  })
  void testCreateDataEntryReader4() throws IOException {
    // Arrange
    DataEntryReaderFactory dataEntryReaderFactory = new DataEntryReaderFactory(false);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("META-INF/versions");

    ArrayList<String> filter2 = new ArrayList<>();
    filter2.add("");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);
    classPathEntry.setApkFilter(filter2);
    classPathEntry.setAabFilter(new ArrayList<>());
    classPathEntry.setJarFilter(new ArrayList<>());
    classPathEntry.setAarFilter(new ArrayList<>());
    classPathEntry.setWarFilter(new ArrayList<>());
    classPathEntry.setEarFilter(new ArrayList<>());
    classPathEntry.setJmodFilter(new ArrayList<>());
    classPathEntry.setZipFilter(new ArrayList<>());

    // Act
    DataEntryReader actualCreateDataEntryReaderResult =
        dataEntryReaderFactory.createDataEntryReader(classPathEntry, mock(DataEntryReader.class));
    Class<Object> clazz = Object.class;
    ClassPathDataEntry classPathDataEntry = new ClassPathDataEntry(clazz);
    actualCreateDataEntryReaderResult.read(classPathDataEntry);

    // Assert
    assertTrue(actualCreateDataEntryReaderResult instanceof FilteredDataEntryReader);
    assertEquals("java/lang/Object.class", classPathDataEntry.getName());
    assertEquals("java/lang/Object.class", classPathDataEntry.getOriginalName());
    assertNull(classPathEntry.getFeatureName());
    assertNull(classPathEntry.getAabFilter());
    assertNull(classPathEntry.getAarFilter());
    assertNull(classPathEntry.getEarFilter());
    assertNull(classPathEntry.getJarFilter());
    assertNull(classPathEntry.getJmodFilter());
    assertNull(classPathEntry.getWarFilter());
    assertNull(classPathEntry.getZipFilter());
    assertNull(classPathDataEntry.getParent());
    assertEquals(-1L, classPathDataEntry.getSize());
    List<String> apkFilter = classPathEntry.getApkFilter();
    assertEquals(1, apkFilter.size());
    List<String> filter3 = classPathEntry.getFilter();
    assertEquals(1, filter3.size());
    byte[] byteArray = new byte[51];
    assertEquals(51, classPathDataEntry.getInputStream().read(byteArray));
    assertFalse(classPathEntry.isAab());
    assertFalse(classPathEntry.isAar());
    assertFalse(classPathEntry.isApk());
    assertFalse(classPathEntry.isDex());
    assertFalse(classPathEntry.isEar());
    assertFalse(classPathEntry.isJar());
    assertFalse(classPathEntry.isJmod());
    assertFalse(classPathEntry.isWar());
    assertFalse(classPathEntry.isZip());
    assertFalse(classPathDataEntry.isDirectory());
    assertTrue(classPathEntry.isFiltered());
    assertTrue(classPathEntry.isOutput());
    assertEquals(System.getProperty("user.dir"), classPathEntry.getName());
    assertSame(filter2, apkFilter);
    assertSame(filter, filter3);
    assertArrayEquals(
        new byte[] {
          -54, -2, -70, -66, 0, 0, 0, '4', 0, 'N', 7, 0, '1', '\n', 0, 1, 0, '2', '\n', 0, 17, 0,
          '3', '\n', 0, '4', 0, '5', '\n', 0, 1, 0, '6', '\b', 0, '7', '\n', 0, 17, 0, '8', '\n', 0,
          '9', 0, ':', '\n', 0, 1, 0, ';'
        },
        byteArray);
  }

  /**
   * Test {@link DataEntryReaderFactory#createDataEntryReader(ClassPathEntry, DataEntryReader)}.
   *
   * <ul>
   *   <li>Given {@link ArrayList#ArrayList()} add empty string.
   * </ul>
   *
   * <p>Method under test: {@link DataEntryReaderFactory#createDataEntryReader(ClassPathEntry,
   * DataEntryReader)}
   */
  @Test
  @DisplayName(
      "Test createDataEntryReader(ClassPathEntry, DataEntryReader); given ArrayList() add empty string")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "DataEntryReader DataEntryReaderFactory.createDataEntryReader(ClassPathEntry, DataEntryReader)"
  })
  void testCreateDataEntryReader_givenArrayListAddEmptyString() throws IOException {
    // Arrange
    DataEntryReaderFactory dataEntryReaderFactory = new DataEntryReaderFactory(false);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("");
    filter.add("META-INF/versions");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);
    classPathEntry.setApkFilter(new ArrayList<>());
    classPathEntry.setAabFilter(new ArrayList<>());
    classPathEntry.setJarFilter(new ArrayList<>());
    classPathEntry.setAarFilter(new ArrayList<>());
    classPathEntry.setWarFilter(new ArrayList<>());
    classPathEntry.setEarFilter(new ArrayList<>());
    classPathEntry.setJmodFilter(new ArrayList<>());
    classPathEntry.setZipFilter(new ArrayList<>());

    // Act
    DataEntryReader actualCreateDataEntryReaderResult =
        dataEntryReaderFactory.createDataEntryReader(classPathEntry, mock(DataEntryReader.class));
    Class<Object> clazz = Object.class;
    ClassPathDataEntry classPathDataEntry = new ClassPathDataEntry(clazz);
    actualCreateDataEntryReaderResult.read(classPathDataEntry);

    // Assert
    assertTrue(actualCreateDataEntryReaderResult instanceof FilteredDataEntryReader);
    assertEquals("java/lang/Object.class", classPathDataEntry.getName());
    assertEquals("java/lang/Object.class", classPathDataEntry.getOriginalName());
    assertNull(classPathEntry.getFeatureName());
    assertNull(classPathEntry.getAabFilter());
    assertNull(classPathEntry.getAarFilter());
    assertNull(classPathEntry.getApkFilter());
    assertNull(classPathEntry.getEarFilter());
    assertNull(classPathEntry.getJarFilter());
    assertNull(classPathEntry.getJmodFilter());
    assertNull(classPathEntry.getWarFilter());
    assertNull(classPathEntry.getZipFilter());
    assertNull(classPathDataEntry.getParent());
    assertEquals(-1L, classPathDataEntry.getSize());
    byte[] byteArray = new byte[51];
    assertEquals(51, classPathDataEntry.getInputStream().read(byteArray));
    assertFalse(classPathEntry.isAab());
    assertFalse(classPathEntry.isAar());
    assertFalse(classPathEntry.isApk());
    assertFalse(classPathEntry.isDex());
    assertFalse(classPathEntry.isEar());
    assertFalse(classPathEntry.isJar());
    assertFalse(classPathEntry.isJmod());
    assertFalse(classPathEntry.isWar());
    assertFalse(classPathEntry.isZip());
    assertFalse(classPathDataEntry.isDirectory());
    assertTrue(classPathEntry.isFiltered());
    assertTrue(classPathEntry.isOutput());
    assertEquals(System.getProperty("user.dir"), classPathEntry.getName());
    assertSame(filter, classPathEntry.getFilter());
    assertArrayEquals(
        new byte[] {
          -54, -2, -70, -66, 0, 0, 0, '4', 0, 'N', 7, 0, '1', '\n', 0, 1, 0, '2', '\n', 0, 17, 0,
          '3', '\n', 0, '4', 0, '5', '\n', 0, 1, 0, '6', '\b', 0, '7', '\n', 0, 17, 0, '8', '\n', 0,
          '9', 0, ':', '\n', 0, 1, 0, ';'
        },
        byteArray);
  }

  /**
   * Test {@link DataEntryReaderFactory#createDataEntryReader(ClassPathEntry, DataEntryReader)}.
   *
   * <ul>
   *   <li>Given {@link ArrayList#ArrayList()} add {@code foo}.
   * </ul>
   *
   * <p>Method under test: {@link DataEntryReaderFactory#createDataEntryReader(ClassPathEntry,
   * DataEntryReader)}
   */
  @Test
  @DisplayName(
      "Test createDataEntryReader(ClassPathEntry, DataEntryReader); given ArrayList() add 'foo'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "DataEntryReader DataEntryReaderFactory.createDataEntryReader(ClassPathEntry, DataEntryReader)"
  })
  void testCreateDataEntryReader_givenArrayListAddFoo() throws IOException {
    // Arrange
    DataEntryReaderFactory dataEntryReaderFactory = new DataEntryReaderFactory(false);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");
    filter.add("META-INF/versions");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);
    classPathEntry.setApkFilter(new ArrayList<>());
    classPathEntry.setAabFilter(new ArrayList<>());
    classPathEntry.setJarFilter(new ArrayList<>());
    classPathEntry.setAarFilter(new ArrayList<>());
    classPathEntry.setWarFilter(new ArrayList<>());
    classPathEntry.setEarFilter(new ArrayList<>());
    classPathEntry.setJmodFilter(new ArrayList<>());
    classPathEntry.setZipFilter(new ArrayList<>());

    // Act
    DataEntryReader actualCreateDataEntryReaderResult =
        dataEntryReaderFactory.createDataEntryReader(classPathEntry, mock(DataEntryReader.class));
    Class<Object> clazz = Object.class;
    ClassPathDataEntry classPathDataEntry = new ClassPathDataEntry(clazz);
    actualCreateDataEntryReaderResult.read(classPathDataEntry);

    // Assert
    assertTrue(actualCreateDataEntryReaderResult instanceof FilteredDataEntryReader);
    assertEquals("java/lang/Object.class", classPathDataEntry.getName());
    assertEquals("java/lang/Object.class", classPathDataEntry.getOriginalName());
    assertNull(classPathEntry.getFeatureName());
    assertNull(classPathEntry.getAabFilter());
    assertNull(classPathEntry.getAarFilter());
    assertNull(classPathEntry.getApkFilter());
    assertNull(classPathEntry.getEarFilter());
    assertNull(classPathEntry.getJarFilter());
    assertNull(classPathEntry.getJmodFilter());
    assertNull(classPathEntry.getWarFilter());
    assertNull(classPathEntry.getZipFilter());
    assertNull(classPathDataEntry.getParent());
    assertEquals(-1L, classPathDataEntry.getSize());
    byte[] byteArray = new byte[51];
    assertEquals(51, classPathDataEntry.getInputStream().read(byteArray));
    assertFalse(classPathEntry.isAab());
    assertFalse(classPathEntry.isAar());
    assertFalse(classPathEntry.isApk());
    assertFalse(classPathEntry.isDex());
    assertFalse(classPathEntry.isEar());
    assertFalse(classPathEntry.isJar());
    assertFalse(classPathEntry.isJmod());
    assertFalse(classPathEntry.isWar());
    assertFalse(classPathEntry.isZip());
    assertFalse(classPathDataEntry.isDirectory());
    assertTrue(classPathEntry.isFiltered());
    assertTrue(classPathEntry.isOutput());
    assertEquals(System.getProperty("user.dir"), classPathEntry.getName());
    assertSame(filter, classPathEntry.getFilter());
    assertArrayEquals(
        new byte[] {
          -54, -2, -70, -66, 0, 0, 0, '4', 0, 'N', 7, 0, '1', '\n', 0, 1, 0, '2', '\n', 0, 17, 0,
          '3', '\n', 0, '4', 0, '5', '\n', 0, 1, 0, '6', '\b', 0, '7', '\n', 0, 17, 0, '8', '\n', 0,
          '9', 0, ':', '\n', 0, 1, 0, ';'
        },
        byteArray);
  }

  /**
   * Test {@link DataEntryReaderFactory#createDataEntryReader(ClassPathEntry, DataEntryReader)}.
   *
   * <ul>
   *   <li>Given {@link ArrayList#ArrayList()} add {@code foo}.
   * </ul>
   *
   * <p>Method under test: {@link DataEntryReaderFactory#createDataEntryReader(ClassPathEntry,
   * DataEntryReader)}
   */
  @Test
  @DisplayName(
      "Test createDataEntryReader(ClassPathEntry, DataEntryReader); given ArrayList() add 'foo'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "DataEntryReader DataEntryReaderFactory.createDataEntryReader(ClassPathEntry, DataEntryReader)"
  })
  void testCreateDataEntryReader_givenArrayListAddFoo2() throws IOException {
    // Arrange
    DataEntryReaderFactory dataEntryReaderFactory = new DataEntryReaderFactory(false);

    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);
    classPathEntry.setApkFilter(new ArrayList<>());
    classPathEntry.setAabFilter(new ArrayList<>());
    classPathEntry.setJarFilter(new ArrayList<>());
    classPathEntry.setAarFilter(new ArrayList<>());
    classPathEntry.setWarFilter(new ArrayList<>());
    classPathEntry.setEarFilter(new ArrayList<>());
    classPathEntry.setJmodFilter(new ArrayList<>());
    classPathEntry.setZipFilter(new ArrayList<>());

    // Act
    DataEntryReader actualCreateDataEntryReaderResult =
        dataEntryReaderFactory.createDataEntryReader(classPathEntry, mock(DataEntryReader.class));
    Class<Object> clazz = Object.class;
    ClassPathDataEntry classPathDataEntry = new ClassPathDataEntry(clazz);
    actualCreateDataEntryReaderResult.read(classPathDataEntry);

    // Assert
    assertTrue(actualCreateDataEntryReaderResult instanceof FilteredDataEntryReader);
    assertEquals("java/lang/Object.class", classPathDataEntry.getName());
    assertEquals("java/lang/Object.class", classPathDataEntry.getOriginalName());
    assertNull(classPathEntry.getFeatureName());
    assertNull(classPathEntry.getAabFilter());
    assertNull(classPathEntry.getAarFilter());
    assertNull(classPathEntry.getApkFilter());
    assertNull(classPathEntry.getEarFilter());
    assertNull(classPathEntry.getJarFilter());
    assertNull(classPathEntry.getJmodFilter());
    assertNull(classPathEntry.getWarFilter());
    assertNull(classPathEntry.getZipFilter());
    assertNull(classPathDataEntry.getParent());
    assertEquals(-1L, classPathDataEntry.getSize());
    List<String> filter2 = classPathEntry.getFilter();
    assertEquals(1, filter2.size());
    byte[] byteArray = new byte[51];
    assertEquals(51, classPathDataEntry.getInputStream().read(byteArray));
    assertFalse(classPathEntry.isAab());
    assertFalse(classPathEntry.isAar());
    assertFalse(classPathEntry.isApk());
    assertFalse(classPathEntry.isDex());
    assertFalse(classPathEntry.isEar());
    assertFalse(classPathEntry.isJar());
    assertFalse(classPathEntry.isJmod());
    assertFalse(classPathEntry.isWar());
    assertFalse(classPathEntry.isZip());
    assertFalse(classPathDataEntry.isDirectory());
    assertTrue(classPathEntry.isFiltered());
    assertTrue(classPathEntry.isOutput());
    assertEquals(System.getProperty("user.dir"), classPathEntry.getName());
    assertSame(filter, filter2);
    assertArrayEquals(
        new byte[] {
          -54, -2, -70, -66, 0, 0, 0, '4', 0, 'N', 7, 0, '1', '\n', 0, 1, 0, '2', '\n', 0, 17, 0,
          '3', '\n', 0, '4', 0, '5', '\n', 0, 1, 0, '6', '\b', 0, '7', '\n', 0, 17, 0, '8', '\n', 0,
          '9', 0, ':', '\n', 0, 1, 0, ';'
        },
        byteArray);
  }

  /**
   * Test {@link DataEntryReaderFactory#getFilterExcludingVersionedClasses(ClassPathEntry)}.
   *
   * <p>Method under test: {@link
   * DataEntryReaderFactory#getFilterExcludingVersionedClasses(ClassPathEntry)}
   */
  @Test
  @DisplayName("Test getFilterExcludingVersionedClasses(ClassPathEntry)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "List DataEntryReaderFactory.getFilterExcludingVersionedClasses(ClassPathEntry)"
  })
  void testGetFilterExcludingVersionedClasses() {
    // Arrange and Act
    List<String> actualFilterExcludingVersionedClasses =
        DataEntryReaderFactory.getFilterExcludingVersionedClasses(
            new ClassPathEntry(Configuration.STD_OUT, true));

    // Assert
    assertEquals(1, actualFilterExcludingVersionedClasses.size());
    assertEquals("!META-INF/versions/**", actualFilterExcludingVersionedClasses.get(0));
  }

  /**
   * Test {@link DataEntryReaderFactory#getFilterExcludingVersionedClasses(ClassPathEntry)}.
   *
   * <ul>
   *   <li>Then return first is {@code META-INF/versions}.
   * </ul>
   *
   * <p>Method under test: {@link
   * DataEntryReaderFactory#getFilterExcludingVersionedClasses(ClassPathEntry)}
   */
  @Test
  @DisplayName(
      "Test getFilterExcludingVersionedClasses(ClassPathEntry); then return first is 'META-INF/versions'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "List DataEntryReaderFactory.getFilterExcludingVersionedClasses(ClassPathEntry)"
  })
  void testGetFilterExcludingVersionedClasses_thenReturnFirstIsMetaInfVersions() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("META-INF/versions");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);

    // Act
    List<String> actualFilterExcludingVersionedClasses =
        DataEntryReaderFactory.getFilterExcludingVersionedClasses(classPathEntry);

    // Assert
    assertEquals(1, actualFilterExcludingVersionedClasses.size());
    assertEquals("META-INF/versions", actualFilterExcludingVersionedClasses.get(0));
  }

  /**
   * Test {@link DataEntryReaderFactory#getFilterExcludingVersionedClasses(ClassPathEntry)}.
   *
   * <ul>
   *   <li>Then return second is {@code foo}.
   * </ul>
   *
   * <p>Method under test: {@link
   * DataEntryReaderFactory#getFilterExcludingVersionedClasses(ClassPathEntry)}
   */
  @Test
  @DisplayName(
      "Test getFilterExcludingVersionedClasses(ClassPathEntry); then return second is 'foo'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "List DataEntryReaderFactory.getFilterExcludingVersionedClasses(ClassPathEntry)"
  })
  void testGetFilterExcludingVersionedClasses_thenReturnSecondIsFoo() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);

    // Act
    List<String> actualFilterExcludingVersionedClasses =
        DataEntryReaderFactory.getFilterExcludingVersionedClasses(classPathEntry);

    // Assert
    assertEquals(2, actualFilterExcludingVersionedClasses.size());
    assertEquals("!META-INF/versions/**", actualFilterExcludingVersionedClasses.get(0));
    assertEquals("foo", actualFilterExcludingVersionedClasses.get(1));
  }

  /**
   * Test {@link DataEntryReaderFactory#getFilterExcludingVersionedClasses(ClassPathEntry)}.
   *
   * <ul>
   *   <li>Then return second is {@code META-INF/versions}.
   * </ul>
   *
   * <p>Method under test: {@link
   * DataEntryReaderFactory#getFilterExcludingVersionedClasses(ClassPathEntry)}
   */
  @Test
  @DisplayName(
      "Test getFilterExcludingVersionedClasses(ClassPathEntry); then return second is 'META-INF/versions'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "List DataEntryReaderFactory.getFilterExcludingVersionedClasses(ClassPathEntry)"
  })
  void testGetFilterExcludingVersionedClasses_thenReturnSecondIsMetaInfVersions() {
    // Arrange
    ArrayList<String> filter = new ArrayList<>();
    filter.add("foo");
    filter.add("META-INF/versions");

    ClassPathEntry classPathEntry = new ClassPathEntry(Configuration.STD_OUT, true);
    classPathEntry.setFilter(filter);

    // Act
    List<String> actualFilterExcludingVersionedClasses =
        DataEntryReaderFactory.getFilterExcludingVersionedClasses(classPathEntry);

    // Assert
    assertEquals(2, actualFilterExcludingVersionedClasses.size());
    assertEquals("META-INF/versions", actualFilterExcludingVersionedClasses.get(1));
    assertEquals("foo", actualFilterExcludingVersionedClasses.get(0));
  }
}
