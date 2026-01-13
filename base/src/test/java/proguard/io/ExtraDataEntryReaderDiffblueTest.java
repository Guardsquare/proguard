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
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.IOException;
import java.util.HashSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExtraDataEntryReaderDiffblueTest {
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
   * Test {@link ExtraDataEntryReader#read(DataEntry)}.
   *
   * <ul>
   *   <li>Given {@link HashSet#HashSet()} add {@code foo}.
   *   <li>When {@code Object}.
   *   <li>Then calls {@link ExtraDataEntryNameMap#getExtraDataEntryNames(String)}.
   * </ul>
   *
   * <p>Method under test: {@link ExtraDataEntryReader#read(DataEntry)}
   */
  @Test
  @DisplayName(
      "Test read(DataEntry); given HashSet() add 'foo'; when 'java.lang.Object'; then calls getExtraDataEntryNames(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ExtraDataEntryReader.read(DataEntry)"})
  void testRead_givenHashSetAddFoo_whenJavaLangObject_thenCallsGetExtraDataEntryNames()
      throws IOException {
    // Arrange
    HashSet<String> stringSet = new HashSet<>();
    stringSet.add("foo");

    ExtraDataEntryNameMap extraEntryNameMap = mock(ExtraDataEntryNameMap.class);
    when(extraEntryNameMap.getDefaultExtraDataEntryNames()).thenReturn(new HashSet<>());
    when(extraEntryNameMap.getExtraDataEntryNames(Mockito.<String>any())).thenReturn(stringSet);

    DataEntryReader dataEntryReader = mock(DataEntryReader.class);
    doThrow(new IOException()).when(dataEntryReader).read(Mockito.<DataEntry>any());

    ExtraDataEntryReader extraDataEntryReader =
        new ExtraDataEntryReader(extraEntryNameMap, dataEntryReader);
    Class<Object> clazz = Object.class;

    // Act and Assert
    assertThrows(IOException.class, () -> extraDataEntryReader.read(new ClassPathDataEntry(clazz)));
    verify(dataEntryReader).read(isA(DataEntry.class));
    verify(extraEntryNameMap).getDefaultExtraDataEntryNames();
    verify(extraEntryNameMap).getExtraDataEntryNames("java/lang/Object.class");
  }
}
