package proguard.gui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ExtensionFileFilter constructor.
 *
 * The constructor ExtensionFileFilter(String description, String[] extensions)
 * performs the following operations:
 * - Line 46: Stores the description parameter in the description field
 * - Line 47: Stores the extensions parameter in the extensions field
 *
 * These tests verify:
 * - The constructor properly initializes the description field (verified via getDescription())
 * - The constructor properly initializes the extensions field (verified via accept())
 * - The filter works correctly with various extension configurations
 * - The filter properly handles edge cases (null, empty arrays, etc.)
 * - The filter correctly identifies files with matching extensions
 * - The filter correctly accepts directories regardless of extensions
 */
public class ExtensionFileFilterClaude_constructorTest {

    @TempDir
    Path tempDir;

    /**
     * Test constructor with a single extension.
     * This verifies lines 46-47: description and extensions are properly stored.
     */
    @Test
    public void testConstructorWithSingleExtension() throws IOException {
        String description = "Java Files";
        String[] extensions = {".java"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        // Verify description was stored (line 46)
        assertEquals(description, filter.getDescription(),
                     "Description should be stored correctly");

        // Verify extensions were stored (line 47) by testing accept()
        File javaFile = tempDir.resolve("Test.java").toFile();
        assertTrue(javaFile.createNewFile(), "Should create test file");
        assertTrue(filter.accept(javaFile),
                   "Should accept file with matching extension");

        File txtFile = tempDir.resolve("Test.txt").toFile();
        assertTrue(txtFile.createNewFile(), "Should create test file");
        assertFalse(filter.accept(txtFile),
                    "Should not accept file with non-matching extension");
    }

    /**
     * Test constructor with multiple extensions.
     * Verifies that all extensions in the array are properly stored.
     */
    @Test
    public void testConstructorWithMultipleExtensions() throws IOException {
        String description = "Archive Files";
        String[] extensions = {".jar", ".zip", ".war"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription());

        // Test that all extensions work
        File jarFile = tempDir.resolve("app.jar").toFile();
        assertTrue(jarFile.createNewFile());
        assertTrue(filter.accept(jarFile),
                   "Should accept .jar file");

        File zipFile = tempDir.resolve("archive.zip").toFile();
        assertTrue(zipFile.createNewFile());
        assertTrue(filter.accept(zipFile),
                   "Should accept .zip file");

        File warFile = tempDir.resolve("webapp.war").toFile();
        assertTrue(warFile.createNewFile());
        assertTrue(filter.accept(warFile),
                   "Should accept .war file");

        File txtFile = tempDir.resolve("readme.txt").toFile();
        assertTrue(txtFile.createNewFile());
        assertFalse(filter.accept(txtFile),
                    "Should not accept file with non-matching extension");
    }

    /**
     * Test constructor with empty extension array.
     * Verifies the constructor handles empty arrays without error.
     */
    @Test
    public void testConstructorWithEmptyExtensions() throws IOException {
        String description = "No Extensions";
        String[] extensions = {};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription());

        // With empty extensions, no files should match (except directories)
        File file = tempDir.resolve("anyfile.txt").toFile();
        assertTrue(file.createNewFile());
        assertFalse(filter.accept(file),
                    "Should not accept any file with empty extensions array");
    }

    /**
     * Test constructor with null description.
     * Verifies the constructor stores null description.
     */
    @Test
    public void testConstructorWithNullDescription() {
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(null, extensions);

        assertNull(filter.getDescription(),
                   "Should store null description");
    }

    /**
     * Test constructor with empty description.
     * Verifies the constructor stores empty string description.
     */
    @Test
    public void testConstructorWithEmptyDescription() {
        String description = "";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals("", filter.getDescription(),
                     "Should store empty description");
    }

    /**
     * Test that directories are always accepted regardless of extension.
     * This verifies the extensions field is properly initialized and used in accept().
     */
    @Test
    public void testConstructorDirectoryAcceptance() throws IOException {
        String description = "Text Files";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        // Create a directory
        Path dirPath = tempDir.resolve("testdir");
        Files.createDirectory(dirPath);
        File dir = dirPath.toFile();

        assertTrue(filter.accept(dir),
                   "Should accept directories regardless of name");

        // Create a directory with extension-like name
        Path dirWithExtPath = tempDir.resolve("directory.jar");
        Files.createDirectory(dirWithExtPath);
        File dirWithExt = dirWithExtPath.toFile();

        assertTrue(filter.accept(dirWithExt),
                   "Should accept directories even with extension-like names");
    }

    /**
     * Test constructor with case-sensitive extensions.
     * Verifies that the stored extensions work with case-insensitive matching.
     */
    @Test
    public void testConstructorCaseInsensitiveExtensions() throws IOException {
        String description = "Java Files";
        String[] extensions = {".java"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        // Test with different cases
        File upperFile = tempDir.resolve("Test.JAVA").toFile();
        assertTrue(upperFile.createNewFile());
        assertTrue(filter.accept(upperFile),
                   "Should accept file with uppercase extension");

        File mixedFile = tempDir.resolve("Test.JaVa").toFile();
        assertTrue(mixedFile.createNewFile());
        assertTrue(filter.accept(mixedFile),
                   "Should accept file with mixed case extension");

        File lowerFile = tempDir.resolve("Test.java").toFile();
        assertTrue(lowerFile.createNewFile());
        assertTrue(filter.accept(lowerFile),
                   "Should accept file with lowercase extension");
    }

    /**
     * Test constructor with extensions that don't start with dot.
     * Verifies the constructor stores extensions as provided.
     */
    @Test
    public void testConstructorWithExtensionsWithoutDot() throws IOException {
        String description = "Config Files";
        String[] extensions = {"xml", "properties"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription());

        // Files ending with these strings (without dot) should match
        File xmlFile = tempDir.resolve("config.xml").toFile();
        assertTrue(xmlFile.createNewFile());
        assertTrue(filter.accept(xmlFile),
                   "Should accept file ending with 'xml'");

        File propsFile = tempDir.resolve("app.properties").toFile();
        assertTrue(propsFile.createNewFile());
        assertTrue(filter.accept(propsFile),
                   "Should accept file ending with 'properties'");
    }

    /**
     * Test constructor with long description.
     * Verifies the constructor can handle lengthy descriptions.
     */
    @Test
    public void testConstructorWithLongDescription() {
        String description = "This is a very long description for a file filter that " +
                           "accepts multiple types of files with various extensions " +
                           "and is used to demonstrate that the constructor properly " +
                           "handles long strings without truncation or errors";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "Should store full long description");
    }

    /**
     * Test constructor with special characters in description.
     * Verifies the constructor handles special characters correctly.
     */
    @Test
    public void testConstructorWithSpecialCharactersInDescription() {
        String description = "Files (*.txt, *.doc) & Others";
        String[] extensions = {".txt", ".doc"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "Should store description with special characters");
    }

    /**
     * Test constructor with special characters in extensions.
     * Verifies the constructor stores extensions with special characters.
     */
    @Test
    public void testConstructorWithSpecialCharactersInExtensions() throws IOException {
        String description = "Special Files";
        String[] extensions = {".txt~", ".bak"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        File backupFile = tempDir.resolve("file.txt~").toFile();
        assertTrue(backupFile.createNewFile());
        assertTrue(filter.accept(backupFile),
                   "Should accept file with special character extension");
    }

    /**
     * Test that constructor creates independent instances.
     * Verifies that modifying one filter doesn't affect another.
     */
    @Test
    public void testConstructorCreatesIndependentInstances() throws IOException {
        String desc1 = "Description 1";
        String[] ext1 = {".txt"};

        String desc2 = "Description 2";
        String[] ext2 = {".java"};

        ExtensionFileFilter filter1 = new ExtensionFileFilter(desc1, ext1);
        ExtensionFileFilter filter2 = new ExtensionFileFilter(desc2, ext2);

        assertEquals(desc1, filter1.getDescription());
        assertEquals(desc2, filter2.getDescription());

        // Verify they work independently
        File txtFile = tempDir.resolve("test.txt").toFile();
        assertTrue(txtFile.createNewFile());

        assertTrue(filter1.accept(txtFile),
                   "Filter 1 should accept .txt file");
        assertFalse(filter2.accept(txtFile),
                    "Filter 2 should not accept .txt file");
    }

    /**
     * Test constructor with file that has multiple dots in name.
     * Verifies extension matching works correctly with complex filenames.
     */
    @Test
    public void testConstructorWithMultipleDotsInFilename() throws IOException {
        String description = "Archive Files";
        String[] extensions = {".tar.gz", ".gz"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        File tarGzFile = tempDir.resolve("archive.tar.gz").toFile();
        assertTrue(tarGzFile.createNewFile());
        assertTrue(filter.accept(tarGzFile),
                   "Should accept file ending with .tar.gz");

        File gzFile = tempDir.resolve("file.gz").toFile();
        assertTrue(gzFile.createNewFile());
        assertTrue(filter.accept(gzFile),
                   "Should accept file ending with .gz");

        File tarFile = tempDir.resolve("archive.tar").toFile();
        assertTrue(tarFile.createNewFile());
        assertFalse(filter.accept(tarFile),
                    "Should not accept file ending with .tar only");
    }

    /**
     * Test constructor with extension matching part of filename.
     * Verifies that accept() uses endsWith, not contains.
     */
    @Test
    public void testConstructorExtensionMatchingPartOfFilename() throws IOException {
        String description = "Text Files";
        String[] extensions = {"txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        // Should match files ending with "txt"
        File file1 = tempDir.resolve("readme.txt").toFile();
        assertTrue(file1.createNewFile());
        assertTrue(filter.accept(file1),
                   "Should accept file ending with txt");

        // Should also match if "txt" appears in the middle
        File file2 = tempDir.resolve("txtfile.dat").toFile();
        assertTrue(file2.createNewFile());
        assertFalse(filter.accept(file2),
                    "Should not accept file with txt in middle but not at end");

        // Edge case: filename that contains extension but doesn't end with it
        File file3 = tempDir.resolve("text.pdf").toFile();
        assertTrue(file3.createNewFile());
        assertFalse(filter.accept(file3),
                    "Should not accept file with txt in name but not at end");
    }

    /**
     * Test constructor with no file extension.
     * Verifies behavior when file has no extension.
     */
    @Test
    public void testConstructorWithNoExtensionFile() throws IOException {
        String description = "Text Files";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        File noExtFile = tempDir.resolve("README").toFile();
        assertTrue(noExtFile.createNewFile());
        assertFalse(filter.accept(noExtFile),
                    "Should not accept file with no extension");
    }

    /**
     * Test constructor with file that starts with dot (hidden file).
     * Verifies behavior with hidden files.
     */
    @Test
    public void testConstructorWithHiddenFile() throws IOException {
        String description = "Config Files";
        String[] extensions = {".conf"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        File hiddenFile = tempDir.resolve(".config.conf").toFile();
        assertTrue(hiddenFile.createNewFile());
        assertTrue(filter.accept(hiddenFile),
                   "Should accept hidden file with matching extension");

        File hiddenNoExt = tempDir.resolve(".hidden").toFile();
        assertTrue(hiddenNoExt.createNewFile());
        assertFalse(filter.accept(hiddenNoExt),
                    "Should not accept hidden file without matching extension");
    }

    /**
     * Test constructor with array containing duplicate extensions.
     * Verifies the constructor stores duplicates as provided.
     */
    @Test
    public void testConstructorWithDuplicateExtensions() throws IOException {
        String description = "Text Files";
        String[] extensions = {".txt", ".txt", ".text"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription());

        File txtFile = tempDir.resolve("file.txt").toFile();
        assertTrue(txtFile.createNewFile());
        assertTrue(filter.accept(txtFile),
                   "Should accept file even with duplicate extensions in array");
    }

    /**
     * Test constructor and verify it doesn't modify the input arrays.
     * This ensures the constructor stores the reference correctly.
     */
    @Test
    public void testConstructorDoesNotModifyInputArray() throws IOException {
        String description = "Test Files";
        String[] extensions = {".test", ".tmp"};
        String[] originalExtensions = extensions.clone();

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        // Verify original array wasn't modified
        assertArrayEquals(originalExtensions, extensions,
                         "Constructor should not modify input array");

        // Verify filter still works
        File testFile = tempDir.resolve("file.test").toFile();
        assertTrue(testFile.createNewFile());
        assertTrue(filter.accept(testFile),
                   "Filter should work correctly");
    }

    /**
     * Test constructor with very long extension strings.
     * Verifies the constructor handles long extension strings.
     */
    @Test
    public void testConstructorWithLongExtensions() throws IOException {
        String description = "Special Files";
        String[] extensions = {".verylongextensionname"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        File file = tempDir.resolve("file.verylongextensionname").toFile();
        assertTrue(file.createNewFile());
        assertTrue(filter.accept(file),
                   "Should accept file with long extension");
    }

    /**
     * Test constructor with Unicode characters in description.
     * Verifies the constructor handles Unicode correctly.
     */
    @Test
    public void testConstructorWithUnicodeDescription() {
        String description = "Fichiers texte (文本文件)";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "Should store Unicode description correctly");
    }

    /**
     * Test constructor with mixed case in description.
     * Verifies description is stored as-is without case modification.
     */
    @Test
    public void testConstructorWithMixedCaseDescription() {
        String description = "TeXt FiLeS";
        String[] extensions = {".txt"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        assertEquals(description, filter.getDescription(),
                     "Should preserve case in description");
    }

    /**
     * Test that the temp directory itself is accepted.
     * Verifies directory acceptance works with the temp dir.
     */
    @Test
    public void testConstructorAcceptsTempDirectory() {
        String description = "Test Filter";
        String[] extensions = {".test"};

        ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);

        File dir = tempDir.toFile();
        assertTrue(filter.accept(dir),
                   "Should accept the temp directory");
    }
}
