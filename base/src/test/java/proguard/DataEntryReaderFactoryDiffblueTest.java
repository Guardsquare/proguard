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
