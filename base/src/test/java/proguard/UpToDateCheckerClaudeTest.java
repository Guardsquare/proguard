package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link UpToDateChecker}.
 * Tests the constructor and check() method with various input/output configurations.
 */
public class UpToDateCheckerClaudeTest {

    /**
     * Tests the constructor with a valid Configuration.
     * Verifies that the UpToDateChecker instance is created successfully.
     */
    @Test
    public void testConstructor() {
        // Arrange
        Configuration configuration = new Configuration();

        // Act
        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Assert
        assertNotNull(checker, "UpToDateChecker instance should not be null");
    }

    /**
     * Tests the constructor with null Configuration.
     * Verifies that the checker accepts null configuration.
     */
    @Test
    public void testConstructorWithNullConfiguration() {
        // Act
        UpToDateChecker checker = new UpToDateChecker(null);

        // Assert
        assertNotNull(checker, "UpToDateChecker instance should not be null even with null configuration");
    }

    /**
     * Tests check() with empty Configuration (no jars).
     * Should throw UpToDateException because output appears up to date (no files to compare).
     */
    @Test
    public void testCheckWithEmptyConfiguration() {
        // Arrange
        Configuration configuration = new Configuration();
        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertThrows(UpToDateChecker.UpToDateException.class, checker::check,
                "check() should throw UpToDateException with empty configuration");
    }

    /**
     * Tests check() with null programJars and libraryJars.
     * Should throw UpToDateException.
     */
    @Test
    public void testCheckWithNullJars() {
        // Arrange
        Configuration configuration = new Configuration();
        configuration.programJars = null;
        configuration.libraryJars = null;
        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertThrows(UpToDateChecker.UpToDateException.class, checker::check,
                "check() should throw UpToDateException when jars are null");
    }

    /**
     * Tests check() with output file newer than input file.
     * Should throw UpToDateException because output is up to date.
     */
    @Test
    public void testCheckWithNewerOutputFile(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File inputFile = tempDir.resolve("input.jar").toFile();
        File outputFile = tempDir.resolve("output.jar").toFile();

        assertTrue(inputFile.createNewFile());
        Thread.sleep(10);
        assertTrue(outputFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(inputFile, false));
        configuration.programJars.add(new ClassPathEntry(outputFile, true));

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertThrows(UpToDateChecker.UpToDateException.class, checker::check,
                "check() should throw UpToDateException when output is newer than input");
    }

    /**
     * Tests check() with input file newer than output file.
     * Should return normally (not throw UpToDateException) because output is outdated.
     */
    @Test
    public void testCheckWithNewerInputFile(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File outputFile = tempDir.resolve("output.jar").toFile();
        File inputFile = tempDir.resolve("input.jar").toFile();

        assertTrue(outputFile.createNewFile());
        Thread.sleep(10);
        assertTrue(inputFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(inputFile, false));
        configuration.programJars.add(new ClassPathEntry(outputFile, true));

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when input is newer than output");
    }

    /**
     * Tests check() with library jars.
     * Library jars are treated as input files.
     */
    @Test
    public void testCheckWithLibraryJars(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File outputFile = tempDir.resolve("output.jar").toFile();
        File libraryFile = tempDir.resolve("library.jar").toFile();

        assertTrue(outputFile.createNewFile());
        Thread.sleep(10);
        assertTrue(libraryFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(outputFile, true));
        configuration.libraryJars = new ClassPath();
        configuration.libraryJars.add(new ClassPathEntry(libraryFile, false));

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when library file is newer than output");
    }

    /**
     * Tests check() with input directory containing files.
     * All files in the directory should be checked recursively.
     */
    @Test
    public void testCheckWithInputDirectory(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File inputDir = tempDir.resolve("inputDir").toFile();
        File outputFile = tempDir.resolve("output.jar").toFile();

        assertTrue(inputDir.mkdir());
        assertTrue(outputFile.createNewFile());
        Thread.sleep(10);

        File fileInDir = new File(inputDir, "class1.class");
        assertTrue(fileInDir.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(inputDir, false));
        configuration.programJars.add(new ClassPathEntry(outputFile, true));

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when input directory file is newer");
    }

    /**
     * Tests check() with empty output directory.
     * Empty output directory should indicate outdated output.
     */
    @Test
    public void testCheckWithEmptyOutputDirectory(@TempDir Path tempDir) throws IOException {
        // Arrange
        File inputFile = tempDir.resolve("input.jar").toFile();
        File outputDir = tempDir.resolve("outputDir").toFile();

        assertTrue(inputFile.createNewFile());
        assertTrue(outputDir.mkdir());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(inputFile, false));
        configuration.programJars.add(new ClassPathEntry(outputDir, true));

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException with empty output directory");
    }

    /**
     * Tests check() with auxiliary input file (applyMapping).
     * Auxiliary input files should also be checked for modification times.
     */
    @Test
    public void testCheckWithAuxiliaryInputFile(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File outputFile = tempDir.resolve("output.jar").toFile();
        File applyMappingFile = tempDir.resolve("mapping.txt").toFile();

        assertTrue(outputFile.createNewFile());
        Thread.sleep(10);
        assertTrue(applyMappingFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(outputFile, true));
        configuration.applyMapping = applyMappingFile;

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when auxiliary input is newer");
    }

    /**
     * Tests check() with auxiliary output file (printSeeds).
     * Auxiliary output files should also be checked for modification times.
     */
    @Test
    public void testCheckWithAuxiliaryOutputFile(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File inputFile = tempDir.resolve("input.jar").toFile();
        File printSeedsFile = tempDir.resolve("seeds.txt").toFile();

        assertTrue(printSeedsFile.createNewFile());
        Thread.sleep(10);
        assertTrue(inputFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(inputFile, false));
        configuration.printSeeds = printSeedsFile;

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when input is newer than auxiliary output");
    }

    /**
     * Tests check() with configuration.lastModified timestamp.
     * lastModified should be treated as an input modification time.
     */
    @Test
    public void testCheckWithLastModifiedTimestamp(@TempDir Path tempDir) throws IOException {
        // Arrange
        File outputFile = tempDir.resolve("output.jar").toFile();
        assertTrue(outputFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(outputFile, true));
        configuration.lastModified = System.currentTimeMillis() + 1000;

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when lastModified is newer than output");
    }

    /**
     * Tests check() with obfuscation dictionary URL (file protocol).
     * URL-based auxiliary input files should be checked.
     */
    @Test
    public void testCheckWithObfuscationDictionaryURL(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File outputFile = tempDir.resolve("output.jar").toFile();
        File dictionaryFile = tempDir.resolve("dictionary.txt").toFile();

        assertTrue(outputFile.createNewFile());
        Thread.sleep(10);
        assertTrue(dictionaryFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(outputFile, true));
        configuration.obfuscationDictionary = dictionaryFile.toURI().toURL();

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when dictionary file is newer");
    }

    /**
     * Tests check() with non-file URL (http protocol).
     * Non-file URLs should be ignored.
     */
    @Test
    public void testCheckWithNonFileURL(@TempDir Path tempDir) throws IOException, MalformedURLException {
        // Arrange
        File outputFile = tempDir.resolve("output.jar").toFile();
        assertTrue(outputFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(outputFile, true));
        configuration.obfuscationDictionary = new URL("http://example.com/dictionary.txt");

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertThrows(UpToDateChecker.UpToDateException.class, checker::check,
                "check() should throw UpToDateException when non-file URL is ignored");
    }

    /**
     * Tests check() with multiple program jars (mixed input and output).
     */
    @Test
    public void testCheckWithMultipleProgramJars(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File input1 = tempDir.resolve("input1.jar").toFile();
        File input2 = tempDir.resolve("input2.jar").toFile();
        File output1 = tempDir.resolve("output1.jar").toFile();
        File output2 = tempDir.resolve("output2.jar").toFile();

        assertTrue(output1.createNewFile());
        assertTrue(output2.createNewFile());
        Thread.sleep(10);
        assertTrue(input1.createNewFile());
        assertTrue(input2.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(input1, false));
        configuration.programJars.add(new ClassPathEntry(output1, true));
        configuration.programJars.add(new ClassPathEntry(input2, false));
        configuration.programJars.add(new ClassPathEntry(output2, true));

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when any input is newer");
    }

    /**
     * Tests check() with printMapping auxiliary output file.
     */
    @Test
    public void testCheckWithPrintMapping(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File inputFile = tempDir.resolve("input.jar").toFile();
        File mappingFile = tempDir.resolve("mapping.txt").toFile();

        assertTrue(mappingFile.createNewFile());
        Thread.sleep(10);
        assertTrue(inputFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(inputFile, false));
        configuration.printMapping = mappingFile;

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when input is newer than printMapping");
    }

    /**
     * Tests check() with printUsage auxiliary output file.
     */
    @Test
    public void testCheckWithPrintUsage(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File inputFile = tempDir.resolve("input.jar").toFile();
        File usageFile = tempDir.resolve("usage.txt").toFile();

        assertTrue(usageFile.createNewFile());
        Thread.sleep(10);
        assertTrue(inputFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(inputFile, false));
        configuration.printUsage = usageFile;

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when input is newer than printUsage");
    }

    /**
     * Tests check() with printConfiguration auxiliary output file.
     */
    @Test
    public void testCheckWithPrintConfiguration(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File inputFile = tempDir.resolve("input.jar").toFile();
        File configFile = tempDir.resolve("config.txt").toFile();

        assertTrue(configFile.createNewFile());
        Thread.sleep(10);
        assertTrue(inputFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(inputFile, false));
        configuration.printConfiguration = configFile;

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when input is newer than printConfiguration");
    }

    /**
     * Tests check() with dump auxiliary output file.
     */
    @Test
    public void testCheckWithDump(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File inputFile = tempDir.resolve("input.jar").toFile();
        File dumpFile = tempDir.resolve("dump.txt").toFile();

        assertTrue(dumpFile.createNewFile());
        Thread.sleep(10);
        assertTrue(inputFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(inputFile, false));
        configuration.dump = dumpFile;

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when input is newer than dump");
    }

    /**
     * Tests check() with classObfuscationDictionary URL.
     */
    @Test
    public void testCheckWithClassObfuscationDictionaryURL(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File outputFile = tempDir.resolve("output.jar").toFile();
        File dictionaryFile = tempDir.resolve("classDictionary.txt").toFile();

        assertTrue(outputFile.createNewFile());
        Thread.sleep(10);
        assertTrue(dictionaryFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(outputFile, true));
        configuration.classObfuscationDictionary = dictionaryFile.toURI().toURL();

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when class dictionary file is newer");
    }

    /**
     * Tests check() with packageObfuscationDictionary URL.
     */
    @Test
    public void testCheckWithPackageObfuscationDictionaryURL(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File outputFile = tempDir.resolve("output.jar").toFile();
        File dictionaryFile = tempDir.resolve("packageDictionary.txt").toFile();

        assertTrue(outputFile.createNewFile());
        Thread.sleep(10);
        assertTrue(dictionaryFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(outputFile, true));
        configuration.packageObfuscationDictionary = dictionaryFile.toURI().toURL();

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when package dictionary file is newer");
    }

    /**
     * Tests check() with nested directory structure.
     * Checks that nested files are properly considered.
     */
    @Test
    public void testCheckWithNestedDirectories(@TempDir Path tempDir) throws IOException, InterruptedException {
        // Arrange
        File inputDir = tempDir.resolve("inputDir").toFile();
        File nestedDir = new File(inputDir, "nested");
        File outputFile = tempDir.resolve("output.jar").toFile();

        assertTrue(inputDir.mkdir());
        assertTrue(nestedDir.mkdir());
        assertTrue(outputFile.createNewFile());
        Thread.sleep(10);

        File nestedFile = new File(nestedDir, "class1.class");
        assertTrue(nestedFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(inputDir, false));
        configuration.programJars.add(new ClassPathEntry(outputFile, true));

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertDoesNotThrow(checker::check,
                "check() should not throw UpToDateException when nested file is newer");
    }

    /**
     * Tests check() with output file having empty name.
     * Output files with empty names should be ignored.
     */
    @Test
    public void testCheckWithEmptyNameOutputFile(@TempDir Path tempDir) throws IOException {
        // Arrange
        File inputFile = tempDir.resolve("input.jar").toFile();
        assertTrue(inputFile.createNewFile());

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(inputFile, false));
        // Use Configuration.STD_OUT which has empty name
        configuration.printSeeds = Configuration.STD_OUT;

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertThrows(UpToDateChecker.UpToDateException.class, checker::check,
                "check() should throw UpToDateException when output with empty name is ignored");
    }

    /**
     * Tests check() with all files having the same modification time.
     * When modification times are equal, output should be considered up to date.
     */
    @Test
    public void testCheckWithSameModificationTimes(@TempDir Path tempDir) throws IOException {
        // Arrange
        File inputFile = tempDir.resolve("input.jar").toFile();
        File outputFile = tempDir.resolve("output.jar").toFile();

        assertTrue(inputFile.createNewFile());
        assertTrue(outputFile.createNewFile());

        long sameTime = System.currentTimeMillis();
        assertTrue(inputFile.setLastModified(sameTime));
        assertTrue(outputFile.setLastModified(sameTime));

        Configuration configuration = new Configuration();
        configuration.programJars = new ClassPath();
        configuration.programJars.add(new ClassPathEntry(inputFile, false));
        configuration.programJars.add(new ClassPathEntry(outputFile, true));

        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        assertThrows(UpToDateChecker.UpToDateException.class, checker::check,
                "check() should throw UpToDateException when modification times are equal");
    }

    /**
     * Tests UpToDateException is a RuntimeException.
     */
    @Test
    public void testUpToDateExceptionIsRuntimeException() {
        // Act
        UpToDateChecker.UpToDateException exception = new UpToDateChecker.UpToDateException();

        // Assert
        assertNotNull(exception);
        assertTrue(exception instanceof RuntimeException,
                "UpToDateException should be a RuntimeException");
    }

    /**
     * Tests that UpToDateException can be caught and rethrown.
     */
    @Test
    public void testUpToDateExceptionHandling() {
        // Arrange
        Configuration configuration = new Configuration();
        UpToDateChecker checker = new UpToDateChecker(configuration);

        // Act & Assert
        try {
            checker.check();
            fail("Should have thrown UpToDateException");
        } catch (UpToDateChecker.UpToDateException e) {
            // Expected - rethrow to verify it's catchable
            assertThrows(UpToDateChecker.UpToDateException.class, () -> {
                throw e;
            });
        }
    }
}
