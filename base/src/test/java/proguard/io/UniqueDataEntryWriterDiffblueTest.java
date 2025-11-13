package proguard.io;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.Configuration;

class UniqueDataEntryWriterDiffblueTest {
  /**
   * Test {@link UniqueDataEntryWriter#createDirectory(DataEntry)}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link UniqueDataEntryWriter#createDirectory(DataEntry)}
   */
  @Test
  @DisplayName("Test createDirectory(DataEntry); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean UniqueDataEntryWriter.createDirectory(DataEntry)"})
  void testCreateDirectory_thenReturnFalse() throws IOException {
    // Arrange
    CascadingDataEntryWriter dataEntryWriter = mock(CascadingDataEntryWriter.class);
    when(dataEntryWriter.createDirectory(Mockito.<DataEntry>any())).thenReturn(false);
    UniqueDataEntryWriter uniqueDataEntryWriter = new UniqueDataEntryWriter(dataEntryWriter);
    Class<Object> clazz = Object.class;

    // Act
    boolean actualCreateDirectoryResult =
        uniqueDataEntryWriter.createDirectory(new ClassPathDataEntry(clazz));

    // Assert
    verify(dataEntryWriter).createDirectory(isA(DataEntry.class));
    assertFalse(actualCreateDirectoryResult);
  }

  /**
   * Test {@link UniqueDataEntryWriter#createDirectory(DataEntry)}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link UniqueDataEntryWriter#createDirectory(DataEntry)}
   */
  @Test
  @DisplayName("Test createDirectory(DataEntry); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean UniqueDataEntryWriter.createDirectory(DataEntry)"})
  void testCreateDirectory_thenReturnTrue() throws IOException {
    // Arrange
    CascadingDataEntryWriter dataEntryWriter = mock(CascadingDataEntryWriter.class);
    when(dataEntryWriter.createDirectory(Mockito.<DataEntry>any())).thenReturn(true);
    UniqueDataEntryWriter uniqueDataEntryWriter = new UniqueDataEntryWriter(dataEntryWriter);
    Class<Object> clazz = Object.class;

    // Act
    boolean actualCreateDirectoryResult =
        uniqueDataEntryWriter.createDirectory(new ClassPathDataEntry(clazz));

    // Assert
    verify(dataEntryWriter).createDirectory(isA(DataEntry.class));
    assertTrue(actualCreateDirectoryResult);
  }

  /**
   * Test {@link UniqueDataEntryWriter#sameOutputStream(DataEntry, DataEntry)}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link UniqueDataEntryWriter#sameOutputStream(DataEntry, DataEntry)}
   */
  @Test
  @DisplayName("Test sameOutputStream(DataEntry, DataEntry); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean UniqueDataEntryWriter.sameOutputStream(DataEntry, DataEntry)"})
  void testSameOutputStream_thenReturnFalse() throws IOException {
    // Arrange
    UniqueDataEntryWriter dataEntryWriter1 =
        new UniqueDataEntryWriter(new DirectoryWriter(Configuration.STD_OUT));
    UniqueDataEntryWriter dataEntryWriter2 =
        new UniqueDataEntryWriter(new DirectoryWriter(Configuration.STD_OUT));

    CascadingDataEntryWriter dataEntryWriter =
        new CascadingDataEntryWriter(dataEntryWriter1, dataEntryWriter2);
    UniqueDataEntryWriter uniqueDataEntryWriter = new UniqueDataEntryWriter(dataEntryWriter);
    ClassPathDataEntry dataEntry1 = new ClassPathDataEntry("Name");
    Class<Object> clazz = Object.class;

    // Act and Assert
    assertFalse(uniqueDataEntryWriter.sameOutputStream(dataEntry1, new ClassPathDataEntry(clazz)));
  }

  /**
   * Test {@link UniqueDataEntryWriter#sameOutputStream(DataEntry, DataEntry)}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link UniqueDataEntryWriter#sameOutputStream(DataEntry, DataEntry)}
   */
  @Test
  @DisplayName("Test sameOutputStream(DataEntry, DataEntry); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean UniqueDataEntryWriter.sameOutputStream(DataEntry, DataEntry)"})
  void testSameOutputStream_thenReturnTrue() throws IOException {
    // Arrange
    UniqueDataEntryWriter uniqueDataEntryWriter =
        new UniqueDataEntryWriter(new DirectoryWriter(Configuration.STD_OUT));
    Class<Object> clazz = Object.class;
    ClassPathDataEntry dataEntry1 = new ClassPathDataEntry(clazz);
    Class<Object> clazz2 = Object.class;

    // Act and Assert
    assertTrue(uniqueDataEntryWriter.sameOutputStream(dataEntry1, new ClassPathDataEntry(clazz2)));
  }

  /**
   * Test {@link UniqueDataEntryWriter#sameOutputStream(DataEntry, DataEntry)}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link UniqueDataEntryWriter#sameOutputStream(DataEntry, DataEntry)}
   */
  @Test
  @DisplayName("Test sameOutputStream(DataEntry, DataEntry); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean UniqueDataEntryWriter.sameOutputStream(DataEntry, DataEntry)"})
  void testSameOutputStream_thenReturnTrue2() throws IOException {
    // Arrange
    UniqueDataEntryWriter dataEntryWriter1 =
        new UniqueDataEntryWriter(new DirectoryWriter(Configuration.STD_OUT));
    UniqueDataEntryWriter dataEntryWriter2 =
        new UniqueDataEntryWriter(new DirectoryWriter(Configuration.STD_OUT));

    CascadingDataEntryWriter dataEntryWriter =
        new CascadingDataEntryWriter(dataEntryWriter1, dataEntryWriter2);
    UniqueDataEntryWriter uniqueDataEntryWriter = new UniqueDataEntryWriter(dataEntryWriter);
    Class<Object> clazz = Object.class;
    ClassPathDataEntry dataEntry1 = new ClassPathDataEntry(clazz);
    Class<Object> clazz2 = Object.class;

    // Act and Assert
    assertTrue(uniqueDataEntryWriter.sameOutputStream(dataEntry1, new ClassPathDataEntry(clazz2)));
  }

  /**
   * Test {@link UniqueDataEntryWriter#sameOutputStream(DataEntry, DataEntry)}.
   *
   * <ul>
   *   <li>When {@link ClassPathDataEntry#ClassPathDataEntry(String)} with {@code Name}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link UniqueDataEntryWriter#sameOutputStream(DataEntry, DataEntry)}
   */
  @Test
  @DisplayName(
      "Test sameOutputStream(DataEntry, DataEntry); when ClassPathDataEntry(String) with 'Name'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean UniqueDataEntryWriter.sameOutputStream(DataEntry, DataEntry)"})
  void testSameOutputStream_whenClassPathDataEntryWithName_thenReturnFalse() throws IOException {
    // Arrange
    UniqueDataEntryWriter uniqueDataEntryWriter =
        new UniqueDataEntryWriter(new DirectoryWriter(Configuration.STD_OUT));
    ClassPathDataEntry dataEntry1 = new ClassPathDataEntry("Name");
    Class<Object> clazz = Object.class;

    // Act and Assert
    assertFalse(uniqueDataEntryWriter.sameOutputStream(dataEntry1, new ClassPathDataEntry(clazz)));
  }
}
