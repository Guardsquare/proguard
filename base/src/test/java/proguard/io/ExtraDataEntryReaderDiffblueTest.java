package proguard.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExtraDataEntryReaderDiffblueTest {
  /**
   * Test {@link ExtraDataEntryReader#ExtraDataEntryReader(String, DataEntryReader)}.
   *
   * <p>Method under test: {@link ExtraDataEntryReader#ExtraDataEntryReader(String,
   * DataEntryReader)}
   */
  @Test
  @DisplayName("Test new ExtraDataEntryReader(String, DataEntryReader)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExtraDataEntryReader.<init>(String, DataEntryReader)"})
  void testNewExtraDataEntryReader() throws IOException {
    // Arrange
    DataEntryReader dataEntryReader = mock(DataEntryReader.class);
    doNothing().when(dataEntryReader).read(Mockito.<DataEntry>any());

    // Act
    ExtraDataEntryReader actualExtraDataEntryReader =
        new ExtraDataEntryReader("Extra Entry Name", dataEntryReader);
    Class<Object> clazz = Object.class;
    ClassPathDataEntry dataEntry = new ClassPathDataEntry(clazz);
    actualExtraDataEntryReader.read(dataEntry);

    // Assert that nothing has changed
    verify(dataEntryReader, atLeast(1)).read(Mockito.<DataEntry>any());
    assertEquals("java/lang/Object.class", dataEntry.getName());
    assertEquals("java/lang/Object.class", dataEntry.getOriginalName());
    assertEquals(-1L, dataEntry.getSize());
    byte[] byteArray = new byte[51];
    assertEquals(51, dataEntry.getInputStream().read(byteArray));
    assertFalse(dataEntry.isDirectory());
    assertArrayEquals(
        new byte[] {
          -54, -2, -70, -66, 0, 0, 0, '4', 0, 'N', 7, 0, '1', '\n', 0, 1, 0, '2', '\n', 0, 17, 0,
          '3', '\n', 0, '4', 0, '5', '\n', 0, 1, 0, '6', '\b', 0, '7', '\n', 0, 17, 0, '8', '\n', 0,
          '9', 0, ':', '\n', 0, 1, 0, ';'
        },
        byteArray);
  }

  /**
   * Test {@link ExtraDataEntryReader#ExtraDataEntryReader(String, DataEntryReader)}.
   *
   * <p>Method under test: {@link ExtraDataEntryReader#ExtraDataEntryReader(String,
   * DataEntryReader)}
   */
  @Test
  @DisplayName("Test new ExtraDataEntryReader(String, DataEntryReader)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExtraDataEntryReader.<init>(String, DataEntryReader)"})
  void testNewExtraDataEntryReader2() throws IOException {
    // Arrange
    DataEntryReader dataEntryReader = mock(DataEntryReader.class);
    doNothing().when(dataEntryReader).read(Mockito.<DataEntry>any());

    // Act
    ExtraDataEntryReader actualExtraDataEntryReader =
        new ExtraDataEntryReader("java.lang.String", dataEntryReader);
    Class<Object> clazz = Object.class;
    ClassPathDataEntry parent = new ClassPathDataEntry(clazz);
    DummyDataEntry dataEntry = new DummyDataEntry(parent, "Name", 3L, true);
    actualExtraDataEntryReader.read(dataEntry);

    // Assert that nothing has changed
    verify(dataEntryReader, atLeast(1)).read(Mockito.<DataEntry>any());
    assertEquals("Name", dataEntry.getName());
    assertEquals("Name", dataEntry.getOriginalName());
    assertEquals(3L, dataEntry.getSize());
    assertTrue(dataEntry.isDirectory());
    assertSame(parent, dataEntry.getParent());
  }

  /**
   * Test {@link ExtraDataEntryReader#ExtraDataEntryReader(String, DataEntryReader,
   * DataEntryReader)}.
   *
   * <p>Method under test: {@link ExtraDataEntryReader#ExtraDataEntryReader(String, DataEntryReader,
   * DataEntryReader)}
   */
  @Test
  @DisplayName("Test new ExtraDataEntryReader(String, DataEntryReader, DataEntryReader)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExtraDataEntryReader.<init>(String, DataEntryReader, DataEntryReader)"})
  void testNewExtraDataEntryReader3() throws IOException {
    // Arrange
    DataEntryReader dataEntryReader = mock(DataEntryReader.class);
    doNothing().when(dataEntryReader).read(Mockito.<DataEntry>any());

    DataEntryReader extraDataEntryReader = mock(DataEntryReader.class);
    doNothing().when(extraDataEntryReader).read(Mockito.<DataEntry>any());

    // Act
    ExtraDataEntryReader actualExtraDataEntryReader =
        new ExtraDataEntryReader("Extra Entry Name", dataEntryReader, extraDataEntryReader);
    Class<Object> clazz = Object.class;
    ClassPathDataEntry dataEntry = new ClassPathDataEntry(clazz);
    actualExtraDataEntryReader.read(dataEntry);

    // Assert that nothing has changed
    verify(dataEntryReader).read(isA(DataEntry.class));
    verify(extraDataEntryReader).read(isA(DataEntry.class));
    assertEquals("java/lang/Object.class", dataEntry.getName());
    assertEquals("java/lang/Object.class", dataEntry.getOriginalName());
    assertEquals(-1L, dataEntry.getSize());
    byte[] byteArray = new byte[51];
    assertEquals(51, dataEntry.getInputStream().read(byteArray));
    assertFalse(dataEntry.isDirectory());
    assertArrayEquals(
        new byte[] {
          -54, -2, -70, -66, 0, 0, 0, '4', 0, 'N', 7, 0, '1', '\n', 0, 1, 0, '2', '\n', 0, 17, 0,
          '3', '\n', 0, '4', 0, '5', '\n', 0, 1, 0, '6', '\b', 0, '7', '\n', 0, 17, 0, '8', '\n', 0,
          '9', 0, ':', '\n', 0, 1, 0, ';'
        },
        byteArray);
  }

  /**
   * Test {@link ExtraDataEntryReader#ExtraDataEntryReader(String, DataEntryReader,
   * DataEntryReader)}.
   *
   * <p>Method under test: {@link ExtraDataEntryReader#ExtraDataEntryReader(String, DataEntryReader,
   * DataEntryReader)}
   */
  @Test
  @DisplayName("Test new ExtraDataEntryReader(String, DataEntryReader, DataEntryReader)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExtraDataEntryReader.<init>(String, DataEntryReader, DataEntryReader)"})
  void testNewExtraDataEntryReader4() throws IOException {
    // Arrange
    DataEntryReader dataEntryReader = mock(DataEntryReader.class);
    doNothing().when(dataEntryReader).read(Mockito.<DataEntry>any());

    DataEntryReader extraDataEntryReader = mock(DataEntryReader.class);
    doNothing().when(extraDataEntryReader).read(Mockito.<DataEntry>any());

    // Act
    ExtraDataEntryReader actualExtraDataEntryReader =
        new ExtraDataEntryReader("java.lang.String", dataEntryReader, extraDataEntryReader);
    Class<Object> clazz = Object.class;
    ClassPathDataEntry parent = new ClassPathDataEntry(clazz);
    DummyDataEntry dataEntry = new DummyDataEntry(parent, "Name", 3L, true);
    actualExtraDataEntryReader.read(dataEntry);

    // Assert that nothing has changed
    verify(dataEntryReader).read(isA(DataEntry.class));
    verify(extraDataEntryReader).read(isA(DataEntry.class));
    assertEquals("Name", dataEntry.getName());
    assertEquals("Name", dataEntry.getOriginalName());
    assertEquals(3L, dataEntry.getSize());
    assertTrue(dataEntry.isDirectory());
    assertSame(parent, dataEntry.getParent());
  }

  /**
   * Test {@link ExtraDataEntryReader#ExtraDataEntryReader(String, DataEntryReader)}.
   *
   * <ul>
   *   <li>When {@link DataEntryReader}.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link ExtraDataEntryReader#ExtraDataEntryReader(String,
   * DataEntryReader)}
   */
  @Test
  @DisplayName(
      "Test new ExtraDataEntryReader(String, DataEntryReader); when DataEntryReader; then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExtraDataEntryReader.<init>(String, DataEntryReader)"})
  void testNewExtraDataEntryReader_whenDataEntryReader_thenDoesNotThrow() {
    // Arrange, Act and Assert
    assertDoesNotThrow(
        () -> new ExtraDataEntryReader("Extra Entry Name", mock(DataEntryReader.class)));
  }

  /**
   * Test {@link ExtraDataEntryReader#ExtraDataEntryReader(String, DataEntryReader,
   * DataEntryReader)}.
   *
   * <ul>
   *   <li>When {@link DataEntryReader}.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link ExtraDataEntryReader#ExtraDataEntryReader(String, DataEntryReader,
   * DataEntryReader)}
   */
  @Test
  @DisplayName(
      "Test new ExtraDataEntryReader(String, DataEntryReader, DataEntryReader); when DataEntryReader; then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExtraDataEntryReader.<init>(String, DataEntryReader, DataEntryReader)"})
  void testNewExtraDataEntryReader_whenDataEntryReader_thenDoesNotThrow2() {
    // Arrange, Act and Assert
    assertDoesNotThrow(
        () ->
            new ExtraDataEntryReader(
                "Extra Entry Name", mock(DataEntryReader.class), mock(DataEntryReader.class)));
  }

  /**
   * Test {@link ExtraDataEntryReader#ExtraDataEntryReader(String, DataEntryReader)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ExtraDataEntryReader#ExtraDataEntryReader(String,
   * DataEntryReader)}
   */
  @Test
  @DisplayName("Test new ExtraDataEntryReader(String, DataEntryReader); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExtraDataEntryReader.<init>(String, DataEntryReader)"})
  void testNewExtraDataEntryReader_whenNull() throws IOException {
    // Arrange
    DataEntryReader dataEntryReader = mock(DataEntryReader.class);
    doNothing().when(dataEntryReader).read(Mockito.<DataEntry>any());

    // Act
    ExtraDataEntryReader actualExtraDataEntryReader =
        new ExtraDataEntryReader((String) null, dataEntryReader);
    Class<Object> clazz = Object.class;
    ClassPathDataEntry dataEntry = new ClassPathDataEntry(clazz);
    actualExtraDataEntryReader.read(dataEntry);

    // Assert that nothing has changed
    verify(dataEntryReader, atLeast(1)).read(Mockito.<DataEntry>any());
    assertEquals("java/lang/Object.class", dataEntry.getName());
    assertEquals("java/lang/Object.class", dataEntry.getOriginalName());
    assertEquals(-1L, dataEntry.getSize());
    byte[] byteArray = new byte[51];
    assertEquals(51, dataEntry.getInputStream().read(byteArray));
    assertFalse(dataEntry.isDirectory());
    assertArrayEquals(
        new byte[] {
          -54, -2, -70, -66, 0, 0, 0, '4', 0, 'N', 7, 0, '1', '\n', 0, 1, 0, '2', '\n', 0, 17, 0,
          '3', '\n', 0, '4', 0, '5', '\n', 0, 1, 0, '6', '\b', 0, '7', '\n', 0, 17, 0, '8', '\n', 0,
          '9', 0, ':', '\n', 0, 1, 0, ';'
        },
        byteArray);
  }

  /**
   * Test {@link ExtraDataEntryReader#ExtraDataEntryReader(String, DataEntryReader,
   * DataEntryReader)}.
   *
   * <ul>
   *   <li>When {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ExtraDataEntryReader#ExtraDataEntryReader(String, DataEntryReader,
   * DataEntryReader)}
   */
  @Test
  @DisplayName(
      "Test new ExtraDataEntryReader(String, DataEntryReader, DataEntryReader); when 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExtraDataEntryReader.<init>(String, DataEntryReader, DataEntryReader)"})
  void testNewExtraDataEntryReader_whenNull2() throws IOException {
    // Arrange
    DataEntryReader dataEntryReader = mock(DataEntryReader.class);
    doNothing().when(dataEntryReader).read(Mockito.<DataEntry>any());

    DataEntryReader extraDataEntryReader = mock(DataEntryReader.class);
    doNothing().when(extraDataEntryReader).read(Mockito.<DataEntry>any());

    // Act
    ExtraDataEntryReader actualExtraDataEntryReader =
        new ExtraDataEntryReader((String) null, dataEntryReader, extraDataEntryReader);
    Class<Object> clazz = Object.class;
    ClassPathDataEntry dataEntry = new ClassPathDataEntry(clazz);
    actualExtraDataEntryReader.read(dataEntry);

    // Assert that nothing has changed
    verify(dataEntryReader).read(isA(DataEntry.class));
    verify(extraDataEntryReader).read(isA(DataEntry.class));
    assertEquals("java/lang/Object.class", dataEntry.getName());
    assertEquals("java/lang/Object.class", dataEntry.getOriginalName());
    assertEquals(-1L, dataEntry.getSize());
    byte[] byteArray = new byte[51];
    assertEquals(51, dataEntry.getInputStream().read(byteArray));
    assertFalse(dataEntry.isDirectory());
    assertArrayEquals(
        new byte[] {
          -54, -2, -70, -66, 0, 0, 0, '4', 0, 'N', 7, 0, '1', '\n', 0, 1, 0, '2', '\n', 0, 17, 0,
          '3', '\n', 0, '4', 0, '5', '\n', 0, 1, 0, '6', '\b', 0, '7', '\n', 0, 17, 0, '8', '\n', 0,
          '9', 0, ':', '\n', 0, 1, 0, ';'
        },
        byteArray);
  }

  /**
   * Test {@link ExtraDataEntryReader#read(DataEntry)}.
   *
   * <p>Method under test: {@link ExtraDataEntryReader#read(DataEntry)}
   */
  @Test
  @DisplayName("Test read(DataEntry)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExtraDataEntryReader.read(DataEntry)"})
  void testRead() throws IOException {
    // Arrange
    DataEntryReader dataEntryReader = mock(DataEntryReader.class);
    doThrow(new IOException()).when(dataEntryReader).read(Mockito.<DataEntry>any());
    ExtraDataEntryReader extraDataEntryReader =
        new ExtraDataEntryReader(new ExtraDataEntryNameMap(), dataEntryReader);
    Class<Object> clazz = Object.class;
    DummyDataEntry dataEntry = new DummyDataEntry(new ClassPathDataEntry(clazz), "Name", 3L, true);

    // Act and Assert
    assertThrows(IOException.class, () -> extraDataEntryReader.read(dataEntry));
    verify(dataEntryReader).read(isA(DataEntry.class));
  }

  /**
   * Test {@link ExtraDataEntryReader#read(DataEntry)}.
   *
   * <ul>
   *   <li>Given {@link DataEntryReader} {@link DataEntryReader#read(DataEntry)} does nothing.
   *   <li>When {@code Object}.
   *   <li>Then calls {@link DataEntryReader#read(DataEntry)}.
   * </ul>
   *
   * <p>Method under test: {@link ExtraDataEntryReader#read(DataEntry)}
   */
  @Test
  @DisplayName(
      "Test read(DataEntry); given DataEntryReader read(DataEntry) does nothing; when 'java.lang.Object'; then calls read(DataEntry)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExtraDataEntryReader.read(DataEntry)"})
  void testRead_givenDataEntryReaderReadDoesNothing_whenJavaLangObject_thenCallsRead()
      throws IOException {
    // Arrange
    DataEntryReader dataEntryReader = mock(DataEntryReader.class);
    doNothing().when(dataEntryReader).read(Mockito.<DataEntry>any());
    ExtraDataEntryReader extraDataEntryReader =
        new ExtraDataEntryReader("Extra Entry Name", dataEntryReader);
    Class<Object> clazz = Object.class;

    // Act
    extraDataEntryReader.read(new ClassPathDataEntry(clazz));

    // Assert
    verify(dataEntryReader, atLeast(1)).read(Mockito.<DataEntry>any());
  }

  /**
   * Test {@link ExtraDataEntryReader#read(DataEntry)}.
   *
   * <ul>
   *   <li>Given {@link ExtraDataEntryReader#ExtraDataEntryReader(String, DataEntryReader)} with
   *       {@code Extra Entry Name} and {@link DataEntryReader}.
   * </ul>
   *
   * <p>Method under test: {@link ExtraDataEntryReader#read(DataEntry)}
   */
  @Test
  @DisplayName(
      "Test read(DataEntry); given ExtraDataEntryReader(String, DataEntryReader) with 'Extra Entry Name' and DataEntryReader")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExtraDataEntryReader.read(DataEntry)"})
  void testRead_givenExtraDataEntryReaderWithExtraEntryNameAndDataEntryReader() throws IOException {
    // Arrange
    DataEntryReader dataEntryReader = mock(DataEntryReader.class);
    doThrow(new IOException()).when(dataEntryReader).read(Mockito.<DataEntry>any());
    ExtraDataEntryReader extraDataEntryReader =
        new ExtraDataEntryReader("Extra Entry Name", dataEntryReader);
    Class<Object> clazz = Object.class;

    // Act and Assert
    assertThrows(IOException.class, () -> extraDataEntryReader.read(new ClassPathDataEntry(clazz)));
    verify(dataEntryReader).read(isA(DataEntry.class));
  }

  /**
   * Test {@link ExtraDataEntryReader#read(DataEntry)}.
   *
   * <ul>
   *   <li>Then throw {@link IOException}.
   * </ul>
   *
   * <p>Method under test: {@link ExtraDataEntryReader#read(DataEntry)}
   */
  @Test
  @DisplayName("Test read(DataEntry); then throw IOException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExtraDataEntryReader.read(DataEntry)"})
  void testRead_thenThrowIOException() throws IOException {
    // Arrange
    DataEntryReader dataEntryReader = mock(DataEntryReader.class);
    doThrow(new IOException()).when(dataEntryReader).read(Mockito.<DataEntry>any());
    ExtraDataEntryReader extraDataEntryReader =
        new ExtraDataEntryReader(new ExtraDataEntryNameMap(), dataEntryReader);
    Class<Object> clazz = Object.class;

    // Act and Assert
    assertThrows(IOException.class, () -> extraDataEntryReader.read(new ClassPathDataEntry(clazz)));
    verify(dataEntryReader).read(isA(DataEntry.class));
  }
}
