package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link WordReader#setBaseDir(File)}.
 * Tests the setBaseDir method behavior with and without included readers.
 */
public class WordReaderClaude_setBaseDirTest {

    /**
     * Tests setBaseDir when there is no included reader.
     * Verifies that the base directory is set directly on the reader.
     */
    @Test
    public void testSetBaseDirWithNoIncludedReader(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("test");
        }

        File originalBaseDir = tempDir.resolve("original").toFile();
        originalBaseDir.mkdir();
        File newBaseDir = tempDir.resolve("new").toFile();
        newBaseDir.mkdir();

        ArgumentWordReader reader = new ArgumentWordReader(new String[]{"word"}, originalBaseDir);

        // Act
        reader.setBaseDir(newBaseDir);

        // Assert
        assertEquals(newBaseDir, reader.getBaseDir(), "BaseDir should be updated to the new directory");

        reader.close();
    }

    /**
     * Tests setBaseDir with null as the new base directory.
     * Verifies that null can be set as the base directory.
     */
    @Test
    public void testSetBaseDirWithNull(@TempDir Path tempDir) throws IOException {
        // Arrange
        File originalBaseDir = tempDir.resolve("original").toFile();
        originalBaseDir.mkdir();

        ArgumentWordReader reader = new ArgumentWordReader(new String[]{"word"}, originalBaseDir);

        // Act
        reader.setBaseDir(null);

        // Assert
        assertNull(reader.getBaseDir(), "BaseDir should be set to null");

        reader.close();
    }

    /**
     * Tests setBaseDir when initially created with null base directory.
     * Verifies that a base directory can be set when initially null.
     */
    @Test
    public void testSetBaseDirFromNull(@TempDir Path tempDir) throws IOException {
        // Arrange
        File newBaseDir = tempDir.resolve("new").toFile();
        newBaseDir.mkdir();

        ArgumentWordReader reader = new ArgumentWordReader(new String[]{"word"}, null);

        // Act
        reader.setBaseDir(newBaseDir);

        // Assert
        assertEquals(newBaseDir, reader.getBaseDir(), "BaseDir should be set to the new directory");

        reader.close();
    }

    /**
     * Tests setBaseDir when there is an included reader.
     * Verifies that setBaseDir is delegated to the included reader.
     */
    @Test
    public void testSetBaseDirWithIncludedReader(@TempDir Path tempDir) throws IOException {
        // Arrange
        File originalBaseDir = tempDir.resolve("original").toFile();
        originalBaseDir.mkdir();
        File newBaseDir = tempDir.resolve("new").toFile();
        newBaseDir.mkdir();

        ArgumentWordReader mainReader = new ArgumentWordReader(new String[]{"main"}, originalBaseDir);
        ArgumentWordReader includedReader = new ArgumentWordReader(new String[]{"included"}, originalBaseDir);

        // Include the reader
        mainReader.includeWordReader(includedReader);

        // Act
        mainReader.setBaseDir(newBaseDir);

        // Assert
        // When an included reader exists, setBaseDir should be delegated to it
        assertEquals(newBaseDir, mainReader.getBaseDir(), "BaseDir should be updated through the included reader");
        assertEquals(newBaseDir, includedReader.getBaseDir(), "Included reader's BaseDir should be updated");

        mainReader.close();
    }

    /**
     * Tests setBaseDir with a chain of included readers.
     * Verifies that setBaseDir propagates through the entire chain.
     */
    @Test
    public void testSetBaseDirWithChainedIncludedReaders(@TempDir Path tempDir) throws IOException {
        // Arrange
        File originalBaseDir = tempDir.resolve("original").toFile();
        originalBaseDir.mkdir();
        File newBaseDir = tempDir.resolve("new").toFile();
        newBaseDir.mkdir();

        ArgumentWordReader reader1 = new ArgumentWordReader(new String[]{"reader1"}, originalBaseDir);
        ArgumentWordReader reader2 = new ArgumentWordReader(new String[]{"reader2"}, originalBaseDir);
        ArgumentWordReader reader3 = new ArgumentWordReader(new String[]{"reader3"}, originalBaseDir);

        // Create a chain: reader1 includes reader2, reader2 includes reader3
        reader1.includeWordReader(reader2);
        reader2.includeWordReader(reader3);

        // Act
        reader1.setBaseDir(newBaseDir);

        // Assert
        assertEquals(newBaseDir, reader1.getBaseDir(), "First reader should return the updated BaseDir");
        assertEquals(newBaseDir, reader2.getBaseDir(), "Second reader should return the updated BaseDir");
        assertEquals(newBaseDir, reader3.getBaseDir(), "Third reader should return the updated BaseDir");

        reader1.close();
    }

    /**
     * Tests that setBaseDir updates the base directory after consuming an included reader.
     * Verifies that once the included reader is exhausted, setBaseDir affects the main reader.
     */
    @Test
    public void testSetBaseDirAfterExhaustingIncludedReader(@TempDir Path tempDir) throws IOException {
        // Arrange
        File originalBaseDir = tempDir.resolve("original").toFile();
        originalBaseDir.mkdir();
        File newBaseDir = tempDir.resolve("new").toFile();
        newBaseDir.mkdir();

        ArgumentWordReader mainReader = new ArgumentWordReader(new String[]{"main"}, originalBaseDir);
        ArgumentWordReader includedReader = new ArgumentWordReader(new String[]{"included"}, originalBaseDir);

        mainReader.includeWordReader(includedReader);

        // Exhaust the included reader
        assertEquals("included", mainReader.nextWord(false, false));
        assertNull(includedReader.nextWord(false, false)); // Included reader is now exhausted

        // Act
        mainReader.setBaseDir(newBaseDir);

        // Assert
        assertEquals(newBaseDir, mainReader.getBaseDir(), "BaseDir should be updated on main reader");

        mainReader.close();
    }

    /**
     * Tests setBaseDir multiple times in succession.
     * Verifies that the base directory can be changed multiple times.
     */
    @Test
    public void testSetBaseDirMultipleTimes(@TempDir Path tempDir) throws IOException {
        // Arrange
        File baseDir1 = tempDir.resolve("dir1").toFile();
        baseDir1.mkdir();
        File baseDir2 = tempDir.resolve("dir2").toFile();
        baseDir2.mkdir();
        File baseDir3 = tempDir.resolve("dir3").toFile();
        baseDir3.mkdir();

        ArgumentWordReader reader = new ArgumentWordReader(new String[]{"word"}, baseDir1);

        // Act & Assert
        assertEquals(baseDir1, reader.getBaseDir());

        reader.setBaseDir(baseDir2);
        assertEquals(baseDir2, reader.getBaseDir());

        reader.setBaseDir(baseDir3);
        assertEquals(baseDir3, reader.getBaseDir());

        reader.setBaseDir(null);
        assertNull(reader.getBaseDir());

        reader.close();
    }

    /**
     * Tests setBaseDir with the same directory.
     * Verifies that setting the same directory is idempotent.
     */
    @Test
    public void testSetBaseDirWithSameDirectory(@TempDir Path tempDir) throws IOException {
        // Arrange
        File baseDir = tempDir.resolve("basedir").toFile();
        baseDir.mkdir();

        ArgumentWordReader reader = new ArgumentWordReader(new String[]{"word"}, baseDir);

        // Act
        reader.setBaseDir(baseDir);

        // Assert
        assertEquals(baseDir, reader.getBaseDir(), "BaseDir should remain the same");

        reader.close();
    }

    /**
     * Tests that setBaseDir works correctly with FileWordReader.
     * Verifies that setBaseDir can change the base directory of a FileWordReader.
     */
    @Test
    public void testSetBaseDirWithFileWordReader(@TempDir Path tempDir) throws IOException {
        // Arrange
        File testFile = tempDir.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("word");
        }

        File newBaseDir = tempDir.resolve("newbase").toFile();
        newBaseDir.mkdir();

        FileWordReader reader = new FileWordReader(testFile);
        File originalBaseDir = reader.getBaseDir();

        // Act
        reader.setBaseDir(newBaseDir);

        // Assert
        assertNotEquals(originalBaseDir, reader.getBaseDir(), "BaseDir should be changed");
        assertEquals(newBaseDir, reader.getBaseDir(), "BaseDir should be the new directory");

        reader.close();
    }

    /**
     * Tests setBaseDir with non-existent directory.
     * Verifies that setBaseDir accepts non-existent directories (since it doesn't validate).
     */
    @Test
    public void testSetBaseDirWithNonExistentDirectory(@TempDir Path tempDir) throws IOException {
        // Arrange
        File nonExistentDir = tempDir.resolve("nonexistent").toFile();

        ArgumentWordReader reader = new ArgumentWordReader(new String[]{"word"}, null);

        // Act
        reader.setBaseDir(nonExistentDir);

        // Assert
        assertEquals(nonExistentDir, reader.getBaseDir(),
            "BaseDir should be set even if directory doesn't exist");

        reader.close();
    }

    /**
     * Tests that setBaseDir doesn't affect the reading functionality.
     * Verifies that changing the base directory doesn't break word reading.
     */
    @Test
    public void testSetBaseDirDoesNotAffectReading(@TempDir Path tempDir) throws IOException {
        // Arrange
        File originalBaseDir = tempDir.resolve("original").toFile();
        originalBaseDir.mkdir();
        File newBaseDir = tempDir.resolve("new").toFile();
        newBaseDir.mkdir();

        ArgumentWordReader reader = new ArgumentWordReader(
            new String[]{"word1", "word2", "word3"}, originalBaseDir);

        // Act & Assert - read a word before changing base dir
        assertEquals("word1", reader.nextWord(false, false));

        // Change base dir
        reader.setBaseDir(newBaseDir);

        // Continue reading - should work fine
        assertEquals("word2", reader.nextWord(false, false));
        assertEquals("word3", reader.nextWord(false, false));
        assertNull(reader.nextWord(false, false));

        reader.close();
    }
}
