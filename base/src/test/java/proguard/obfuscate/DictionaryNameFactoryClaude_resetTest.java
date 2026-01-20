package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DictionaryNameFactory.reset() method.
 */
public class DictionaryNameFactoryClaude_resetTest {

    @TempDir
    Path tempDir;

    /**
     * Test that reset returns to the beginning of the dictionary.
     */
    @Test
    public void testResetReturnsToBeginning() throws IOException {
        String content = "name1\nname2\nname3\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        // Consume some names
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());

        // Reset
        factory.reset();

        // Should start from beginning
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
    }

    /**
     * Test that reset works after exhausting the dictionary.
     */
    @Test
    public void testResetAfterExhausted() throws IOException {
        String content = "name1\nname2\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        // Exhaust dictionary
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("a", factory.nextName());  // fallback

        // Reset
        factory.reset();

        // Should start from beginning of dictionary
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
    }

    /**
     * Test that reset resets the fallback factory too.
     */
    @Test
    public void testResetAlsoResetsFallbackFactory() throws IOException {
        String content = "name1\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        // Exhaust dictionary and use fallback
        assertEquals("name1", factory.nextName());
        assertEquals("a", factory.nextName());
        assertEquals("b", factory.nextName());
        assertEquals("c", factory.nextName());

        // Reset
        factory.reset();

        // Dictionary should restart, and fallback should also reset
        assertEquals("name1", factory.nextName());
        assertEquals("a", factory.nextName());  // fallback resets to 'a'
    }

    /**
     * Test reset on empty dictionary.
     */
    @Test
    public void testResetOnEmptyDictionary() throws IOException {
        Reader reader = new StringReader("");
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        // Use fallback
        assertEquals("a", factory.nextName());
        assertEquals("b", factory.nextName());

        // Reset
        factory.reset();

        // Still uses fallback, but fallback is reset
        assertEquals("a", factory.nextName());
    }

    /**
     * Test multiple consecutive resets.
     */
    @Test
    public void testMultipleConsecutiveResets() throws IOException {
        String content = "name1\nname2\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        assertEquals("name1", factory.nextName());

        factory.reset();
        factory.reset();
        factory.reset();

        // Should still work correctly
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
    }

    /**
     * Test reset without consuming any names.
     */
    @Test
    public void testResetWithoutConsuming() throws IOException {
        String content = "name1\nname2\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        // Reset immediately
        factory.reset();

        // Should work normally
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
    }

    /**
     * Test reset at different positions in the dictionary.
     */
    @Test
    public void testResetAtDifferentPositions() throws IOException {
        String content = "name1\nname2\nname3\nname4\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        // Reset at position 1
        assertEquals("name1", factory.nextName());
        factory.reset();
        assertEquals("name1", factory.nextName());

        // Reset at position 3
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
        factory.reset();
        assertEquals("name1", factory.nextName());
    }

    /**
     * Test reset with large dictionary.
     */
    @Test
    public void testResetWithLargeDictionary() throws IOException {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            content.append("name").append(i).append("\n");
        }
        Reader reader = new StringReader(content.toString());
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        // Consume 50 names
        for (int i = 0; i < 50; i++) {
            factory.nextName();
        }

        // Reset
        factory.reset();

        // Should start from beginning
        assertEquals("name0", factory.nextName());
        assertEquals("name1", factory.nextName());
    }

    /**
     * Test reset with copy constructor.
     */
    @Test
    public void testResetWithCopyConstructor() throws IOException {
        String content = "name1\nname2\nname3\n";
        Reader reader = new StringReader(content);
        NameFactory fallback1 = new SimpleNameFactory();
        DictionaryNameFactory source = new DictionaryNameFactory(reader, fallback1);

        // Create copy
        NameFactory fallback2 = new SimpleNameFactory();
        DictionaryNameFactory copy = new DictionaryNameFactory(source, fallback2);

        // Advance copy
        assertEquals("name1", copy.nextName());
        assertEquals("name2", copy.nextName());

        // Reset copy
        copy.reset();

        // Copy should restart
        assertEquals("name1", copy.nextName());

        // Source should be unaffected
        assertEquals("name1", source.nextName());
    }

    /**
     * Test reset preserves dictionary content.
     */
    @Test
    public void testResetPreservesDictionaryContent() throws IOException {
        String content = "alpha\nbeta\ngamma\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        // First iteration
        assertEquals("alpha", factory.nextName());
        assertEquals("beta", factory.nextName());
        assertEquals("gamma", factory.nextName());

        factory.reset();

        // Second iteration - same content
        assertEquals("alpha", factory.nextName());
        assertEquals("beta", factory.nextName());
        assertEquals("gamma", factory.nextName());
    }

    /**
     * Test reset with File-based constructor.
     */
    @Test
    public void testResetWithFileConstructor() throws IOException {
        File dictFile = tempDir.resolve("reset_test.txt").toFile();
        Files.write(dictFile.toPath(), "file1\nfile2\n".getBytes(StandardCharsets.UTF_8));
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, fallback);

        assertEquals("file1", factory.nextName());
        assertEquals("file2", factory.nextName());

        factory.reset();

        assertEquals("file1", factory.nextName());
        assertEquals("file2", factory.nextName());
    }

    /**
     * Test reset doesn't affect fallback behavior.
     */
    @Test
    public void testResetDoesNotAffectFallbackBehavior() throws IOException {
        String content = "a\nb\nc\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        // Exhaust dictionary
        assertEquals("a", factory.nextName());
        assertEquals("b", factory.nextName());
        assertEquals("c", factory.nextName());
        assertEquals("d", factory.nextName());  // fallback still skips a, b, c

        factory.reset();

        // After reset, fallback still skips dictionary names
        assertEquals("a", factory.nextName());
        assertEquals("b", factory.nextName());
        assertEquals("c", factory.nextName());
        assertEquals("d", factory.nextName());  // fallback still skips a, b, c
    }

    /**
     * Test reset after partial dictionary consumption.
     */
    @Test
    public void testResetAfterPartialConsumption() throws IOException {
        String content = "name1\nname2\nname3\nname4\nname5\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        // Consume first 2
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());

        factory.reset();

        // Consume first 3
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());

        factory.reset();

        // Full iteration
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
        assertEquals("name4", factory.nextName());
        assertEquals("name5", factory.nextName());
    }

    /**
     * Test reset with validJavaIdentifiers=false.
     */
    @Test
    public void testResetWithNonJavaIdentifiers() throws IOException {
        String content = "name-1\nname@2\nname!3\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, false, fallback);

        assertEquals("name-1", factory.nextName());
        assertEquals("name@2", factory.nextName());

        factory.reset();

        assertEquals("name-1", factory.nextName());
        assertEquals("name@2", factory.nextName());
        assertEquals("name!3", factory.nextName());
    }

    /**
     * Test reset with comments in dictionary.
     */
    @Test
    public void testResetWithComments() throws IOException {
        String content = "# Comment\nname1\nname2 # inline\nname3\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());

        factory.reset();

        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
    }

    /**
     * Test reset with duplicates in dictionary.
     */
    @Test
    public void testResetWithDuplicates() throws IOException {
        String content = "name1\nname2\nname1\nname3\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        // First iteration (duplicates removed)
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());

        factory.reset();

        // Second iteration (same deduplicated content)
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
    }

    /**
     * Test reset with UTF-8 characters.
     */
    @Test
    public void testResetWithUtf8() throws IOException {
        String content = "café\nnaïve\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        assertEquals("café", factory.nextName());

        factory.reset();

        assertEquals("café", factory.nextName());
        assertEquals("naïve", factory.nextName());
    }

    /**
     * Test reset multiple times in sequence with consumption.
     */
    @Test
    public void testResetMultipleTimesWithConsumption() throws IOException {
        String content = "name1\nname2\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        // Iteration 1
        assertEquals("name1", factory.nextName());
        factory.reset();

        // Iteration 2
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        factory.reset();

        // Iteration 3
        assertEquals("name1", factory.nextName());
        factory.reset();

        // Iteration 4
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
    }

    /**
     * Test that reset works correctly with single-entry dictionary.
     */
    @Test
    public void testResetWithSingleEntry() throws IOException {
        String content = "onlyname\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        assertEquals("onlyname", factory.nextName());
        assertEquals("a", factory.nextName());

        factory.reset();

        assertEquals("onlyname", factory.nextName());
        assertEquals("a", factory.nextName());
    }

    /**
     * Test reset behavior is consistent across multiple resets and iterations.
     */
    @Test
    public void testResetConsistencyAcrossIterations() throws IOException {
        String content = "name1\nname2\nname3\n";
        Reader reader = new StringReader(content);
        NameFactory fallback = new SimpleNameFactory();
        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallback);

        // Do 5 complete iterations
        for (int iteration = 0; iteration < 5; iteration++) {
            assertEquals("name1", factory.nextName());
            assertEquals("name2", factory.nextName());
            assertEquals("name3", factory.nextName());
            factory.reset();
        }

        // Verify still works
        assertEquals("name1", factory.nextName());
    }
}
