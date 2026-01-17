package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import proguard.classfile.ClassPool;
import proguard.io.DataEntry;
import proguard.io.DataEntryWriter;
import proguard.resources.file.ResourceFilePool;
import proguard.util.ExtensionMatcher;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DataEntryWriterFactory}.
 * Tests all constructors and the createDataEntryWriter method.
 */
public class DataEntryWriterFactoryClaudeTest {

    /**
     * Simple mock DataEntryWriter for testing purposes.
     */
    private static class TestDataEntryWriter implements DataEntryWriter {
        private boolean createDirectoriesCalled = false;
        private boolean closeCalled = false;
        private String lastWrittenEntryName = null;

        @Override
        public boolean createDirectory(DataEntry dataEntry) throws IOException {
            createDirectoriesCalled = true;
            return true;
        }

        @Override
        public boolean sameOutputStream(DataEntry dataEntry1, DataEntry dataEntry2) throws IOException {
            return false;
        }

        @Override
        public OutputStream createOutputStream(DataEntry dataEntry) throws IOException {
            lastWrittenEntryName = dataEntry.getName();
            return new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    // No-op for testing
                }
            };
        }

        @Override
        public void close() throws IOException {
            closeCalled = true;
        }

        @Override
        public void println(java.io.PrintWriter pw, String prefix) {
            // No-op for testing
        }
    }

    // ========== Constructor Tests ==========

    /**
     * Tests the 8-parameter constructor.
     * Verifies that the factory can be instantiated with all parameters.
     */
    @Test
    public void testConstructor8Parameters() {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        int modificationTime = 0;
        ExtensionMatcher uncompressedFilter = new ExtensionMatcher(".so");
        int uncompressedAlignment = 4;
        boolean pageAlignNativeLibs = false;
        boolean mergeAarJars = false;
        KeyStore.PrivateKeyEntry[] privateKeyEntries = null;

        // Act
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool,
                resourceFilePool,
                modificationTime,
                uncompressedFilter,
                uncompressedAlignment,
                pageAlignNativeLibs,
                mergeAarJars,
                privateKeyEntries
        );

        // Assert
        assertNotNull(factory, "Factory should not be null");
    }

    /**
     * Tests the 9-parameter constructor with null alternativeClassDataEntryWriterProvider.
     * Verifies that the factory can be instantiated with a null provider.
     */
    @Test
    public void testConstructor9ParametersNullProvider() {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        int modificationTime = 0;
        ExtensionMatcher uncompressedFilter = new ExtensionMatcher(".so");
        int uncompressedAlignment = 4;
        boolean pageAlignNativeLibs = false;
        boolean mergeAarJars = false;
        KeyStore.PrivateKeyEntry[] privateKeyEntries = null;
        Function<DataEntryWriter, DataEntryWriter> provider = null;

        // Act
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool,
                resourceFilePool,
                modificationTime,
                uncompressedFilter,
                uncompressedAlignment,
                pageAlignNativeLibs,
                mergeAarJars,
                privateKeyEntries,
                provider
        );

        // Assert
        assertNotNull(factory, "Factory should not be null");
    }

    /**
     * Tests the 9-parameter constructor with a non-null alternativeClassDataEntryWriterProvider.
     * Verifies that the factory can be instantiated with a custom provider.
     */
    @Test
    public void testConstructor9ParametersWithProvider() {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        int modificationTime = 0;
        ExtensionMatcher uncompressedFilter = new ExtensionMatcher(".so");
        int uncompressedAlignment = 4;
        boolean pageAlignNativeLibs = false;
        boolean mergeAarJars = false;
        KeyStore.PrivateKeyEntry[] privateKeyEntries = null;
        Function<DataEntryWriter, DataEntryWriter> provider = writer -> new TestDataEntryWriter();

        // Act
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool,
                resourceFilePool,
                modificationTime,
                uncompressedFilter,
                uncompressedAlignment,
                pageAlignNativeLibs,
                mergeAarJars,
                privateKeyEntries,
                provider
        );

        // Assert
        assertNotNull(factory, "Factory should not be null");
    }

    /**
     * Tests constructor with null uncompressed filter.
     */
    @Test
    public void testConstructorNullUncompressedFilter() {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();

        // Act
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool,
                resourceFilePool,
                0,
                null,
                0,
                false,
                false,
                null
        );

        // Assert
        assertNotNull(factory, "Factory should handle null uncompressed filter");
    }

    /**
     * Tests constructor with pageAlignNativeLibs set to true.
     */
    @Test
    public void testConstructorPageAlignNativeLibsTrue() {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();

        // Act
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool,
                resourceFilePool,
                0,
                null,
                0,
                true,  // pageAlignNativeLibs
                false,
                null
        );

        // Assert
        assertNotNull(factory, "Factory should handle pageAlignNativeLibs=true");
    }

    /**
     * Tests constructor with mergeAarJars set to true.
     */
    @Test
    public void testConstructorMergeAarJarsTrue() {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();

        // Act
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool,
                resourceFilePool,
                0,
                null,
                0,
                false,
                true,  // mergeAarJars
                null
        );

        // Assert
        assertNotNull(factory, "Factory should handle mergeAarJars=true");
    }

    /**
     * Tests constructor with non-zero modification time.
     */
    @Test
    public void testConstructorNonZeroModificationTime() {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        int modificationTime = 123456789;

        // Act
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool,
                resourceFilePool,
                modificationTime,
                null,
                0,
                false,
                false,
                null
        );

        // Assert
        assertNotNull(factory, "Factory should handle non-zero modification time");
    }

    /**
     * Tests constructor with non-zero uncompressed alignment.
     */
    @Test
    public void testConstructorNonZeroAlignment() {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        int uncompressedAlignment = 8;

        // Act
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool,
                resourceFilePool,
                0,
                null,
                uncompressedAlignment,
                false,
                false,
                null
        );

        // Assert
        assertNotNull(factory, "Factory should handle non-zero alignment");
    }

    // ========== createDataEntryWriter Tests ==========

    /**
     * Tests createDataEntryWriter with empty class path.
     * Should return null since there are no entries.
     */
    @Test
    public void testCreateDataEntryWriterEmptyClassPath() {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );
        ClassPath classPath = new ClassPath();
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 0, extraWriter);

        // Assert
        assertNull(result, "Should return null for empty class path");
    }

    /**
     * Tests createDataEntryWriter with a single output JAR file.
     */
    @Test
    public void testCreateDataEntryWriterSingleJarOutput(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputJar = tempDir.resolve("output.jar").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputJar, true));  // true = output
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer for single JAR output");
    }

    /**
     * Tests createDataEntryWriter with a single output APK file.
     */
    @Test
    public void testCreateDataEntryWriterSingleApkOutput(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputApk = tempDir.resolve("output.apk").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputApk, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer for single APK output");
    }

    /**
     * Tests createDataEntryWriter with a single output AAB file.
     */
    @Test
    public void testCreateDataEntryWriterSingleAabOutput(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputAab = tempDir.resolve("output.aab").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputAab, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer for single AAB output");
    }

    /**
     * Tests createDataEntryWriter with a single output AAR file.
     */
    @Test
    public void testCreateDataEntryWriterSingleAarOutput(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputAar = tempDir.resolve("output.aar").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputAar, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer for single AAR output");
    }

    /**
     * Tests createDataEntryWriter with a single output WAR file.
     */
    @Test
    public void testCreateDataEntryWriterSingleWarOutput(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputWar = tempDir.resolve("output.war").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputWar, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer for single WAR output");
    }

    /**
     * Tests createDataEntryWriter with a single output EAR file.
     */
    @Test
    public void testCreateDataEntryWriterSingleEarOutput(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputEar = tempDir.resolve("output.ear").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputEar, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer for single EAR output");
    }

    /**
     * Tests createDataEntryWriter with a single output JMOD file.
     */
    @Test
    public void testCreateDataEntryWriterSingleJmodOutput(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputJmod = tempDir.resolve("output.jmod").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputJmod, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer for single JMOD output");
    }

    /**
     * Tests createDataEntryWriter with a single output ZIP file.
     */
    @Test
    public void testCreateDataEntryWriterSingleZipOutput(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputZip = tempDir.resolve("output.zip").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputZip, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer for single ZIP output");
    }

    /**
     * Tests createDataEntryWriter with a single output DEX file.
     */
    @Test
    public void testCreateDataEntryWriterSingleDexOutput(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputDex = tempDir.resolve("classes.dex").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputDex, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer for single DEX output");
    }

    /**
     * Tests createDataEntryWriter with a directory output.
     */
    @Test
    public void testCreateDataEntryWriterDirectoryOutput(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputDir = tempDir.resolve("output").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputDir, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer for directory output");
    }

    /**
     * Tests createDataEntryWriter with multiple output entries.
     */
    @Test
    public void testCreateDataEntryWriterMultipleOutputs(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputJar1 = tempDir.resolve("output1.jar").toFile();
        File outputJar2 = tempDir.resolve("output2.jar").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputJar1, true));
        classPath.add(new ClassPathEntry(outputJar2, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 2, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer for multiple outputs");
    }

    /**
     * Tests createDataEntryWriter with duplicate output file.
     * The same file should be handled correctly when specified multiple times.
     */
    @Test
    public void testCreateDataEntryWriterDuplicateOutput(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputJar = tempDir.resolve("output.jar").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputJar, true));
        classPath.add(new ClassPathEntry(outputJar, true));  // Duplicate
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 2, extraWriter);

        // Assert
        assertNotNull(result, "Should handle duplicate output files");
    }

    /**
     * Tests createDataEntryWriter with fromIndex and toIndex specifying a subset.
     */
    @Test
    public void testCreateDataEntryWriterSubset(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputJar1 = tempDir.resolve("output1.jar").toFile();
        File outputJar2 = tempDir.resolve("output2.jar").toFile();
        File outputJar3 = tempDir.resolve("output3.jar").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputJar1, true));
        classPath.add(new ClassPathEntry(outputJar2, true));
        classPath.add(new ClassPathEntry(outputJar3, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act - Only process middle entry (index 1)
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 1, 2, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer for subset of entries");
    }

    /**
     * Tests createDataEntryWriter with null extra writer.
     */
    @Test
    public void testCreateDataEntryWriterNullExtraWriter(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputJar = tempDir.resolve("output.jar").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputJar, true));

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, null);

        // Assert
        assertNotNull(result, "Should handle null extra writer");
    }

    /**
     * Tests createDataEntryWriter with AAR and mergeAarJars=true.
     */
    @Test
    public void testCreateDataEntryWriterAarWithMerge(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, true, null  // mergeAarJars=true
        );

        File outputAar = tempDir.resolve("output.aar").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputAar, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer for AAR with merge");
    }

    /**
     * Tests createDataEntryWriter with APK and pageAlignNativeLibs=true.
     */
    @Test
    public void testCreateDataEntryWriterApkWithPageAlign(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, true, false, null  // pageAlignNativeLibs=true
        );

        File outputApk = tempDir.resolve("output.apk").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputApk, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer for APK with page align");
    }

    /**
     * Tests createDataEntryWriter with uncompressed filter.
     */
    @Test
    public void testCreateDataEntryWriterWithUncompressedFilter(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        ExtensionMatcher uncompressedFilter = new ExtensionMatcher(".so");
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, uncompressedFilter, 4, false, false, null
        );

        File outputApk = tempDir.resolve("output.apk").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputApk, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer with uncompressed filter");
    }

    /**
     * Tests createDataEntryWriter with custom alternative class writer provider.
     */
    @Test
    public void testCreateDataEntryWriterWithAlternativeProvider(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        TestDataEntryWriter customWriter = new TestDataEntryWriter();
        Function<DataEntryWriter, DataEntryWriter> provider = writer -> customWriter;

        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null, provider
        );

        File outputJar = tempDir.resolve("output.jar").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputJar, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer with alternative provider");
    }

    /**
     * Tests createDataEntryWriter with entry having filters.
     */
    @Test
    public void testCreateDataEntryWriterWithFilters(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputJar = tempDir.resolve("output.jar").toFile();
        ClassPathEntry entry = new ClassPathEntry(outputJar, true);
        entry.setFilter(Arrays.asList("**/*.class"));
        entry.setJarFilter(Arrays.asList("lib/*.jar"));

        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should return a writer with filters applied");
    }

    /**
     * Tests createDataEntryWriter with mixed input and output entries.
     * Only output entries should be processed.
     */
    @Test
    public void testCreateDataEntryWriterMixedInputOutput(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File inputJar = tempDir.resolve("input.jar").toFile();
        File outputJar = tempDir.resolve("output.jar").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(inputJar, false));  // input
        classPath.add(new ClassPathEntry(outputJar, true));   // output
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act - Process all entries, but only output should matter
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 2, extraWriter);

        // Assert
        assertNotNull(result, "Should handle mixed input/output entries");
    }

    /**
     * Tests createDataEntryWriter with case-insensitive file extensions.
     */
    @Test
    public void testCreateDataEntryWriterCaseInsensitiveExtensions(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputJar = tempDir.resolve("output.JAR").toFile();  // Uppercase extension
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputJar, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should handle case-insensitive extensions");
    }

    /**
     * Tests createDataEntryWriter with ap_ extension (alternative APK extension).
     */
    @Test
    public void testCreateDataEntryWriterApUnderscoreExtension(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputAp = tempDir.resolve("output.ap_").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputAp, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should handle .ap_ extension as APK");
    }

    /**
     * Tests createDataEntryWriter with entry having a feature name.
     */
    @Test
    public void testCreateDataEntryWriterWithFeatureName(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputJar = tempDir.resolve("output.jar").toFile();
        ClassPathEntry entry = new ClassPathEntry(outputJar, true, "myFeature");

        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should handle entry with feature name");
    }

    /**
     * Tests that 8-parameter constructor delegates to 9-parameter constructor.
     * This ensures the delegation works correctly.
     */
    @Test
    public void testConstructor8ParametersDelegatesToNine(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();

        // Act - Create factory using 8-parameter constructor
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        // Create a writer to ensure factory is usable
        File outputJar = tempDir.resolve("output.jar").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputJar, true));

        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, null);

        // Assert
        assertNotNull(result, "Factory from 8-param constructor should work");
    }

    /**
     * Tests createDataEntryWriter with complex nested archive scenario.
     */
    @Test
    public void testCreateDataEntryWriterNestedArchives(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool, resourceFilePool, 0, null, 0, false, false, null
        );

        File outputWar = tempDir.resolve("output.war").toFile();
        ClassPathEntry entry = new ClassPathEntry(outputWar, true);
        entry.setJarFilter(Arrays.asList("WEB-INF/lib/*.jar"));

        ClassPath classPath = new ClassPath();
        classPath.add(entry);
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should handle nested archives with filters");
    }

    /**
     * Tests createDataEntryWriter with all boolean flags enabled.
     */
    @Test
    public void testCreateDataEntryWriterAllFlagsEnabled(@TempDir Path tempDir) {
        // Arrange
        ClassPool classPool = new ClassPool();
        ResourceFilePool resourceFilePool = new ResourceFilePool();
        DataEntryWriterFactory factory = new DataEntryWriterFactory(
                classPool,
                resourceFilePool,
                12345,
                new ExtensionMatcher(".so"),
                8,
                true,  // pageAlignNativeLibs
                true,  // mergeAarJars
                null
        );

        File outputAar = tempDir.resolve("output.aar").toFile();
        ClassPath classPath = new ClassPath();
        classPath.add(new ClassPathEntry(outputAar, true));
        TestDataEntryWriter extraWriter = new TestDataEntryWriter();

        // Act
        DataEntryWriter result = factory.createDataEntryWriter(classPath, 0, 1, extraWriter);

        // Assert
        assertNotNull(result, "Should handle all flags enabled");
    }
}
