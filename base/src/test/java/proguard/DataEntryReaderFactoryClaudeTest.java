package proguard;

import org.junit.jupiter.api.Test;
import proguard.io.DataEntryReader;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DataEntryReaderFactory}.
 * Tests all methods including constructor, createDataEntryReader, and getFilterExcludingVersionedClasses.
 */
public class DataEntryReaderFactoryClaudeTest {

    // ========== Constructor Tests ==========

    /**
     * Tests the constructor with android=false.
     * Verifies that the factory can be instantiated for non-Android platforms.
     */
    @Test
    public void testConstructorNonAndroid() {
        // Act
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);

        // Assert
        assertNotNull(factory, "Factory should not be null");
    }

    /**
     * Tests the constructor with android=true.
     * Verifies that the factory can be instantiated for Android platforms.
     */
    @Test
    public void testConstructorAndroid() {
        // Act
        DataEntryReaderFactory factory = new DataEntryReaderFactory(true);

        // Assert
        assertNotNull(factory, "Factory should not be null");
    }

    // ========== getFilterExcludingVersionedClasses Tests ==========

    /**
     * Tests getFilterExcludingVersionedClasses with null filter.
     * Should return a list with only the VERSIONS_EXCLUDE pattern.
     */
    @Test
    public void testGetFilterExcludingVersionedClasses_NullFilter() {
        // Arrange
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        entry.setFilter(null);

        // Act
        List<String> result = DataEntryReaderFactory.getFilterExcludingVersionedClasses(entry);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Result should contain 1 element");
        assertEquals("!META-INF/versions/**", result.get(0), "Should exclude versioned classes");
    }

    /**
     * Tests getFilterExcludingVersionedClasses with an empty filter list.
     * Should return a list with only the VERSIONS_EXCLUDE pattern.
     */
    @Test
    public void testGetFilterExcludingVersionedClasses_EmptyFilter() {
        // Arrange
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        entry.setFilter(Collections.emptyList());

        // Act
        List<String> result = DataEntryReaderFactory.getFilterExcludingVersionedClasses(entry);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Result should contain 1 element");
        assertEquals("!META-INF/versions/**", result.get(0), "Should exclude versioned classes");
    }

    /**
     * Tests getFilterExcludingVersionedClasses with a filter that already contains VERSIONS pattern.
     * Should return the original filter unchanged.
     */
    @Test
    public void testGetFilterExcludingVersionedClasses_WithVersionsPattern() {
        // Arrange
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        List<String> originalFilter = Arrays.asList("**/*.class", "META-INF/versions/9/**");
        entry.setFilter(originalFilter);

        // Act
        List<String> result = DataEntryReaderFactory.getFilterExcludingVersionedClasses(entry);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertSame(originalFilter, result, "Should return original filter when versions pattern exists");
        assertEquals(2, result.size(), "Result should have original size");
    }

    /**
     * Tests getFilterExcludingVersionedClasses with a filter that doesn't contain VERSIONS pattern.
     * Should prepend VERSIONS_EXCLUDE to the original filter.
     */
    @Test
    public void testGetFilterExcludingVersionedClasses_WithoutVersionsPattern() {
        // Arrange
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        List<String> originalFilter = Arrays.asList("**/*.class", "!**/*Test.class");
        entry.setFilter(originalFilter);

        // Act
        List<String> result = DataEntryReaderFactory.getFilterExcludingVersionedClasses(entry);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(3, result.size(), "Result should have original size + 1");
        assertEquals("!META-INF/versions/**", result.get(0), "First element should be VERSIONS_EXCLUDE");
        assertEquals("**/*.class", result.get(1), "Second element should be first original element");
        assertEquals("!**/*Test.class", result.get(2), "Third element should be second original element");
    }

    /**
     * Tests getFilterExcludingVersionedClasses with a filter containing multiple elements.
     * None contain the VERSIONS pattern, so VERSIONS_EXCLUDE should be prepended.
     */
    @Test
    public void testGetFilterExcludingVersionedClasses_MultipleElementsNoVersions() {
        // Arrange
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        List<String> originalFilter = Arrays.asList("com/example/**", "org/test/**", "!**/*.xml");
        entry.setFilter(originalFilter);

        // Act
        List<String> result = DataEntryReaderFactory.getFilterExcludingVersionedClasses(entry);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(4, result.size(), "Result should have 4 elements");
        assertEquals("!META-INF/versions/**", result.get(0), "First element should be VERSIONS_EXCLUDE");
        assertTrue(result.containsAll(originalFilter), "Result should contain all original elements");
    }

    /**
     * Tests getFilterExcludingVersionedClasses with a filter containing "versions" in lowercase.
     * Since it doesn't match "META-INF/versions" pattern, VERSIONS_EXCLUDE should be prepended.
     */
    @Test
    public void testGetFilterExcludingVersionedClasses_LowercaseVersionsNotInPattern() {
        // Arrange
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        List<String> originalFilter = Arrays.asList("**/*.class", "!**/versions.txt");
        entry.setFilter(originalFilter);

        // Act
        List<String> result = DataEntryReaderFactory.getFilterExcludingVersionedClasses(entry);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(3, result.size(), "Result should have 3 elements");
        assertEquals("!META-INF/versions/**", result.get(0), "VERSIONS_EXCLUDE should be prepended");
    }

    /**
     * Tests getFilterExcludingVersionedClasses with a filter containing partial VERSIONS pattern.
     * If it contains "META-INF/versions" substring, should return original filter.
     */
    @Test
    public void testGetFilterExcludingVersionedClasses_PartialVersionsPattern() {
        // Arrange
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        List<String> originalFilter = Arrays.asList("**/*.class", "some/META-INF/versions/path");
        entry.setFilter(originalFilter);

        // Act
        List<String> result = DataEntryReaderFactory.getFilterExcludingVersionedClasses(entry);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertSame(originalFilter, result, "Should return original filter when versions pattern substring exists");
    }

    // ========== createDataEntryReader Tests ==========

    /**
     * Simple mock DataEntryReader for testing purposes.
     * We don't need to implement read() since we only need to verify
     * that the factory wraps the reader correctly.
     */
    private static class TestDataEntryReader implements DataEntryReader {
        @Override
        public void read(proguard.io.DataEntry dataEntry) throws java.io.IOException {
            // No-op implementation for testing
        }
    }

    /**
     * Tests createDataEntryReader with a simple jar file entry.
     * The returned reader should not be the same as the input reader (it should be wrapped).
     */
    @Test
    public void testCreateDataEntryReader_JarFile() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotSame(reader, result, "Result should be wrapped, not the original reader");
    }

    /**
     * Tests createDataEntryReader with an APK file entry.
     * The returned reader should be wrapped to handle APK files.
     */
    @Test
    public void testCreateDataEntryReader_ApkFile() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);
        File file = new File("test.apk");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotSame(reader, result, "Result should be wrapped for APK handling");
    }

    /**
     * Tests createDataEntryReader with an AAB file entry.
     * The returned reader should be wrapped to handle AAB files.
     */
    @Test
    public void testCreateDataEntryReader_AabFile() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);
        File file = new File("test.aab");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotSame(reader, result, "Result should be wrapped for AAB handling");
    }

    /**
     * Tests createDataEntryReader with a WAR file entry.
     * The returned reader should be wrapped to handle WAR files.
     */
    @Test
    public void testCreateDataEntryReader_WarFile() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);
        File file = new File("test.war");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotSame(reader, result, "Result should be wrapped for WAR handling");
    }

    /**
     * Tests createDataEntryReader with an EAR file entry.
     * The returned reader should be wrapped to handle EAR files.
     */
    @Test
    public void testCreateDataEntryReader_EarFile() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);
        File file = new File("test.ear");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotSame(reader, result, "Result should be wrapped for EAR handling");
    }

    /**
     * Tests createDataEntryReader with a JMOD file entry.
     * The returned reader should be wrapped to handle JMOD files.
     */
    @Test
    public void testCreateDataEntryReader_JmodFile() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);
        File file = new File("test.jmod");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotSame(reader, result, "Result should be wrapped for JMOD handling");
    }

    /**
     * Tests createDataEntryReader with a ZIP file entry.
     * The returned reader should be wrapped to handle ZIP files.
     */
    @Test
    public void testCreateDataEntryReader_ZipFile() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);
        File file = new File("test.zip");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotSame(reader, result, "Result should be wrapped for ZIP handling");
    }

    /**
     * Tests createDataEntryReader with an AAR file entry.
     * The returned reader should be wrapped to handle AAR files.
     */
    @Test
    public void testCreateDataEntryReader_AarFile() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);
        File file = new File("test.aar");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotSame(reader, result, "Result should be wrapped for AAR handling");
    }

    /**
     * Tests createDataEntryReader with a directory entry (no specific archive extension).
     * The reader might be returned as-is or wrapped minimally.
     */
    @Test
    public void testCreateDataEntryReader_Directory() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);
        File file = new File("testdir");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
    }

    /**
     * Tests createDataEntryReader with Android flag set to true and APK file.
     * The factory should handle Android-specific packaging.
     */
    @Test
    public void testCreateDataEntryReader_AndroidApk() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(true);
        File file = new File("test.apk");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotSame(reader, result, "Result should be wrapped for Android APK handling");
    }

    /**
     * Tests createDataEntryReader with a filter applied to the class path entry.
     * The filter should be incorporated into the reader pipeline.
     */
    @Test
    public void testCreateDataEntryReader_WithFilter() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        List<String> filter = Arrays.asList("**/*.class");
        entry.setFilter(filter);
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotSame(reader, result, "Result should be wrapped with filter");
    }

    /**
     * Tests createDataEntryReader with jar filter applied.
     * The jar filter should affect how jar files are processed.
     */
    @Test
    public void testCreateDataEntryReader_WithJarFilter() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        List<String> jarFilter = Arrays.asList("lib/*.jar");
        entry.setJarFilter(jarFilter);
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotSame(reader, result, "Result should be wrapped with jar filter");
    }

    /**
     * Tests createDataEntryReader with APK filter applied.
     * The APK filter should affect how APK files are processed.
     */
    @Test
    public void testCreateDataEntryReader_WithApkFilter() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);
        File file = new File("test.apk");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        List<String> apkFilter = Arrays.asList("*.apk");
        entry.setApkFilter(apkFilter);
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotSame(reader, result, "Result should be wrapped with APK filter");
    }

    /**
     * Tests createDataEntryReader with multiple filters applied.
     * Multiple filters should all be incorporated into the reader pipeline.
     */
    @Test
    public void testCreateDataEntryReader_WithMultipleFilters() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        entry.setFilter(Arrays.asList("**/*.class"));
        entry.setJarFilter(Arrays.asList("lib/*.jar"));
        entry.setZipFilter(Arrays.asList("data/*.zip"));
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotSame(reader, result, "Result should be wrapped with multiple filters");
    }

    /**
     * Tests createDataEntryReader with Android flag and non-APK file.
     * Should still work correctly for non-APK archives on Android.
     */
    @Test
    public void testCreateDataEntryReader_AndroidNonApk() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(true);
        File file = new File("test.jar");
        ClassPathEntry entry = new ClassPathEntry(file, false);
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        DataEntryReader result = factory.createDataEntryReader(entry, reader);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotSame(reader, result, "Result should be wrapped");
    }

    /**
     * Tests createDataEntryReader with case-insensitive file extensions.
     * File extensions should be matched case-insensitively (e.g., .JAR, .Jar, .jar).
     */
    @Test
    public void testCreateDataEntryReader_CaseInsensitiveExtensions() {
        // Arrange
        DataEntryReaderFactory factory = new DataEntryReaderFactory(false);

        // Test uppercase
        File fileUpper = new File("test.JAR");
        ClassPathEntry entryUpper = new ClassPathEntry(fileUpper, false);
        TestDataEntryReader readerUpper = new TestDataEntryReader();

        // Act
        DataEntryReader resultUpper = factory.createDataEntryReader(entryUpper, readerUpper);

        // Assert
        assertNotNull(resultUpper, "Result should not be null for uppercase extension");
        assertNotSame(readerUpper, resultUpper, "Result should be wrapped for uppercase .JAR");

        // Test mixed case
        File fileMixed = new File("test.Zip");
        ClassPathEntry entryMixed = new ClassPathEntry(fileMixed, false);
        TestDataEntryReader readerMixed = new TestDataEntryReader();

        // Act
        DataEntryReader resultMixed = factory.createDataEntryReader(entryMixed, readerMixed);

        // Assert
        assertNotNull(resultMixed, "Result should not be null for mixed case extension");
        assertNotSame(readerMixed, resultMixed, "Result should be wrapped for mixed case .Zip");
    }
}
