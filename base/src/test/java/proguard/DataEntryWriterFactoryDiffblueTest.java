package proguard;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.io.DataEntryWriter;
import proguard.io.DirectoryWriter;
import proguard.io.NameFilteredDataEntryWriter;
import proguard.io.UniqueDataEntryWriter;
import proguard.resources.file.ResourceFilePool;
import proguard.util.EmptyStringMatcher;
import proguard.util.StringFunction;
import proguard.util.StringMatcher;
import proguard.util.TransformedStringMatcher;

class DataEntryWriterFactoryDiffblueTest {
  /**
   * Test {@link DataEntryWriterFactory#DataEntryWriterFactory(ClassPool, ResourceFilePool, int,
   * StringMatcher, int, boolean, boolean, PrivateKeyEntry[])}.
   *
   * <p>Method under test: {@link DataEntryWriterFactory#DataEntryWriterFactory(ClassPool,
   * ResourceFilePool, int, StringMatcher, int, boolean, boolean, KeyStore.PrivateKeyEntry[])}
   */
  @Test
  @DisplayName(
      "Test new DataEntryWriterFactory(ClassPool, ResourceFilePool, int, StringMatcher, int, boolean, boolean, PrivateKeyEntry[])")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void DataEntryWriterFactory.<init>(ClassPool, ResourceFilePool, int, StringMatcher, int, boolean, boolean, KeyStore.PrivateKeyEntry[])"
  })
  void testNewDataEntryWriterFactory() {
    // Arrange
    ResourceFilePool resourceFilePool = new ResourceFilePool();
    StringFunction stringFunction = mock(StringFunction.class);
    TransformedStringMatcher uncompressedFilter =
        new TransformedStringMatcher(stringFunction, new EmptyStringMatcher());
    PrivateKeyEntry[] privateKeyEntries = new PrivateKeyEntry[] {null};

    // Act
    DataEntryWriterFactory actualDataEntryWriterFactory =
        new DataEntryWriterFactory(
            KotlinConstants.dummyClassPool,
            resourceFilePool,
            1,
            uncompressedFilter,
            1,
            true,
            true,
            privateKeyEntries);

    // Assert
    assertNull(actualDataEntryWriterFactory.createDataEntryWriter(null, 1, 1, null));
  }

  /**
   * Test {@link DataEntryWriterFactory#createDataEntryWriter(ClassPath, int, int,
   * DataEntryWriter)}.
   *
   * <p>Method under test: {@link DataEntryWriterFactory#createDataEntryWriter(ClassPath, int, int,
   * DataEntryWriter)}
   */
  @Test
  @DisplayName("Test createDataEntryWriter(ClassPath, int, int, DataEntryWriter)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "DataEntryWriter DataEntryWriterFactory.createDataEntryWriter(ClassPath, int, int, DataEntryWriter)"
  })
  void testCreateDataEntryWriter() {
    // Arrange
    ResourceFilePool resourceFilePool = new ResourceFilePool();
    StringFunction stringFunction = mock(StringFunction.class);
    TransformedStringMatcher uncompressedFilter =
        new TransformedStringMatcher(stringFunction, new EmptyStringMatcher());

    DataEntryWriterFactory dataEntryWriterFactory =
        new DataEntryWriterFactory(
            KotlinConstants.dummyClassPool,
            resourceFilePool,
            1,
            uncompressedFilter,
            1,
            true,
            true,
            null);

    ClassPath classPath = new ClassPath();
    classPath.add(
        new ClassPathEntry(
            Paths.get(System.getProperty("java.io.tmpdir"), "test.txt").toFile(), true));
    classPath.add(0, new ClassPathEntry(Configuration.STD_OUT, true));

    // Act and Assert
    assertTrue(
        dataEntryWriterFactory.createDataEntryWriter(
                classPath,
                0,
                1,
                new UniqueDataEntryWriter(new DirectoryWriter(Configuration.STD_OUT)))
            instanceof NameFilteredDataEntryWriter);
  }

  /**
   * Test {@link DataEntryWriterFactory#createDataEntryWriter(ClassPath, int, int,
   * DataEntryWriter)}.
   *
   * <ul>
   *   <li>Given {@link ClassPathEntry#ClassPathEntry(File, boolean)} with file is {@link
   *       Configuration#STD_OUT} and isOutput is {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link DataEntryWriterFactory#createDataEntryWriter(ClassPath, int, int,
   * DataEntryWriter)}
   */
  @Test
  @DisplayName(
      "Test createDataEntryWriter(ClassPath, int, int, DataEntryWriter); given ClassPathEntry(File, boolean) with file is STD_OUT and isOutput is 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "DataEntryWriter DataEntryWriterFactory.createDataEntryWriter(ClassPath, int, int, DataEntryWriter)"
  })
  void testCreateDataEntryWriter_givenClassPathEntryWithFileIsStd_outAndIsOutputIsFalse() {
    // Arrange
    ResourceFilePool resourceFilePool = new ResourceFilePool();
    StringFunction stringFunction = mock(StringFunction.class);
    TransformedStringMatcher uncompressedFilter =
        new TransformedStringMatcher(stringFunction, new EmptyStringMatcher());

    DataEntryWriterFactory dataEntryWriterFactory =
        new DataEntryWriterFactory(
            KotlinConstants.dummyClassPool,
            resourceFilePool,
            1,
            uncompressedFilter,
            1,
            true,
            true,
            null);

    ClassPath classPath = new ClassPath();
    classPath.add(new ClassPathEntry(Configuration.STD_OUT, false));
    classPath.add(new ClassPathEntry(Configuration.STD_OUT, true));
    classPath.add(0, new ClassPathEntry(Configuration.STD_OUT, true));

    // Act and Assert
    assertTrue(
        dataEntryWriterFactory.createDataEntryWriter(
                classPath,
                0,
                1,
                new UniqueDataEntryWriter(new DirectoryWriter(Configuration.STD_OUT)))
            instanceof NameFilteredDataEntryWriter);
  }

  /**
   * Test {@link DataEntryWriterFactory#createDataEntryWriter(ClassPath, int, int,
   * DataEntryWriter)}.
   *
   * <ul>
   *   <li>Then return {@link NameFilteredDataEntryWriter}.
   * </ul>
   *
   * <p>Method under test: {@link DataEntryWriterFactory#createDataEntryWriter(ClassPath, int, int,
   * DataEntryWriter)}
   */
  @Test
  @DisplayName(
      "Test createDataEntryWriter(ClassPath, int, int, DataEntryWriter); then return NameFilteredDataEntryWriter")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "DataEntryWriter DataEntryWriterFactory.createDataEntryWriter(ClassPath, int, int, DataEntryWriter)"
  })
  void testCreateDataEntryWriter_thenReturnNameFilteredDataEntryWriter() {
    // Arrange
    ResourceFilePool resourceFilePool = new ResourceFilePool();
    StringFunction stringFunction = mock(StringFunction.class);
    TransformedStringMatcher uncompressedFilter =
        new TransformedStringMatcher(stringFunction, new EmptyStringMatcher());

    DataEntryWriterFactory dataEntryWriterFactory =
        new DataEntryWriterFactory(
            KotlinConstants.dummyClassPool,
            resourceFilePool,
            1,
            uncompressedFilter,
            1,
            true,
            true,
            null);

    ClassPath classPath = new ClassPath();
    classPath.add(0, new ClassPathEntry(Configuration.STD_OUT, true));

    // Act and Assert
    assertTrue(
        dataEntryWriterFactory.createDataEntryWriter(
                classPath,
                0,
                1,
                new UniqueDataEntryWriter(new DirectoryWriter(Configuration.STD_OUT)))
            instanceof NameFilteredDataEntryWriter);
  }

  /**
   * Test {@link DataEntryWriterFactory#createDataEntryWriter(ClassPath, int, int,
   * DataEntryWriter)}.
   *
   * <ul>
   *   <li>Then return {@link NameFilteredDataEntryWriter}.
   * </ul>
   *
   * <p>Method under test: {@link DataEntryWriterFactory#createDataEntryWriter(ClassPath, int, int,
   * DataEntryWriter)}
   */
  @Test
  @DisplayName(
      "Test createDataEntryWriter(ClassPath, int, int, DataEntryWriter); then return NameFilteredDataEntryWriter")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "DataEntryWriter DataEntryWriterFactory.createDataEntryWriter(ClassPath, int, int, DataEntryWriter)"
  })
  void testCreateDataEntryWriter_thenReturnNameFilteredDataEntryWriter2() {
    // Arrange
    ResourceFilePool resourceFilePool = new ResourceFilePool();
    StringFunction stringFunction = mock(StringFunction.class);
    TransformedStringMatcher uncompressedFilter =
        new TransformedStringMatcher(stringFunction, new EmptyStringMatcher());

    DataEntryWriterFactory dataEntryWriterFactory =
        new DataEntryWriterFactory(
            KotlinConstants.dummyClassPool,
            resourceFilePool,
            1,
            uncompressedFilter,
            1,
            true,
            true,
            null);

    ClassPath classPath = new ClassPath();
    classPath.add(new ClassPathEntry(Configuration.STD_OUT, true));
    classPath.add(0, new ClassPathEntry(Configuration.STD_OUT, true));

    // Act and Assert
    assertTrue(
        dataEntryWriterFactory.createDataEntryWriter(
                classPath,
                0,
                1,
                new UniqueDataEntryWriter(new DirectoryWriter(Configuration.STD_OUT)))
            instanceof NameFilteredDataEntryWriter);
  }

  /**
   * Test {@link DataEntryWriterFactory#createDataEntryWriter(ClassPath, int, int,
   * DataEntryWriter)}.
   *
   * <ul>
   *   <li>When {@link ClassPath} (default constructor).
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link DataEntryWriterFactory#createDataEntryWriter(ClassPath, int, int,
   * DataEntryWriter)}
   */
  @Test
  @DisplayName(
      "Test createDataEntryWriter(ClassPath, int, int, DataEntryWriter); when ClassPath (default constructor); then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "DataEntryWriter DataEntryWriterFactory.createDataEntryWriter(ClassPath, int, int, DataEntryWriter)"
  })
  void testCreateDataEntryWriter_whenClassPath_thenReturnNull() {
    // Arrange
    ResourceFilePool resourceFilePool = new ResourceFilePool();
    StringFunction stringFunction = mock(StringFunction.class);
    TransformedStringMatcher uncompressedFilter =
        new TransformedStringMatcher(stringFunction, new EmptyStringMatcher());
    PrivateKeyEntry[] privateKeyEntries = new PrivateKeyEntry[] {null};

    DataEntryWriterFactory dataEntryWriterFactory =
        new DataEntryWriterFactory(
            KotlinConstants.dummyClassPool,
            resourceFilePool,
            1,
            uncompressedFilter,
            1,
            true,
            true,
            privateKeyEntries);
    ClassPath classPath = new ClassPath();

    // Act and Assert
    assertNull(
        dataEntryWriterFactory.createDataEntryWriter(
            classPath,
            1,
            1,
            new UniqueDataEntryWriter(new DirectoryWriter(Configuration.STD_OUT))));
  }
}
