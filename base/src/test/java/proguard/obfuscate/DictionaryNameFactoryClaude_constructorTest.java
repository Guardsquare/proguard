package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DictionaryNameFactory constructors.
 * Tests the constructors:
 * - DictionaryNameFactory(URL url, boolean validJavaIdentifiers, NameFactory nameFactory)
 * - DictionaryNameFactory(File file, NameFactory nameFactory)
 * - DictionaryNameFactory(File file, boolean validJavaIdentifiers, NameFactory nameFactory)
 * - DictionaryNameFactory(Reader reader, NameFactory nameFactory)
 * - DictionaryNameFactory(Reader reader, boolean validJavaIdentifiers, NameFactory nameFactory)
 * - DictionaryNameFactory(DictionaryNameFactory dictionaryNameFactory, NameFactory nameFactory)
 */
public class DictionaryNameFactoryClaude_constructorTest {

    @TempDir
    Path tempDir;

    /**
     * Test that the constructor successfully creates a DictionaryNameFactory with a valid URL
     * containing valid Java identifiers.
     */
    @Test
    public void testConstructorWithValidUrlAndValidJavaIdentifiers() throws IOException {
        // Create a dictionary file with valid Java identifiers
        File dictFile = tempDir.resolve("dictionary.txt").toFile();
        Files.write(dictFile.toPath(), "alpha\nbeta\ngamma\n".getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(url, true, fallbackFactory);

        assertNotNull(factory, "DictionaryNameFactory should be created successfully");
        // Test that it produces names from the dictionary
        assertEquals("alpha", factory.nextName());
        assertEquals("beta", factory.nextName());
        assertEquals("gamma", factory.nextName());
    }

    /**
     * Test that the constructor properly handles a dictionary with comments.
     * Comments start with '#' and should be ignored.
     */
    @Test
    public void testConstructorWithCommentsInDictionary() throws IOException {
        File dictFile = tempDir.resolve("dictionary_with_comments.txt").toFile();
        String content = "# This is a comment\n" +
                        "name1\n" +
                        "name2 # inline comment\n" +
                        "# Another comment\n" +
                        "name3\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(url, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
    }

    /**
     * Test that the constructor with validJavaIdentifiers=true only accepts valid Java identifiers.
     */
    @Test
    public void testConstructorWithValidJavaIdentifiersTrue() throws IOException {
        File dictFile = tempDir.resolve("dictionary_mixed.txt").toFile();
        String content = "validName\n" +
                        "123invalid\n" +  // starts with digit - invalid
                        "valid_name2\n" +
                        "with-dash\n" +   // dash is not valid in Java identifier
                        "validName3\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(url, true, fallbackFactory);

        assertNotNull(factory);
        // Only valid Java identifiers should be read
        assertEquals("validName", factory.nextName());
        assertEquals("invalid", factory.nextName());  // "123" is skipped, "invalid" is read
        assertEquals("valid_name2", factory.nextName());
        assertEquals("with", factory.nextName());      // "with" is valid, "-dash" causes split
        assertEquals("dash", factory.nextName());
        assertEquals("validName3", factory.nextName());
    }

    /**
     * Test that the constructor with validJavaIdentifiers=false accepts any non-whitespace characters.
     */
    @Test
    public void testConstructorWithValidJavaIdentifiersFalse() throws IOException {
        File dictFile = tempDir.resolve("dictionary_non_java.txt").toFile();
        String content = "name1\n" +
                        "name-with-dashes\n" +
                        "123numeric\n" +
                        "name_underscore\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(url, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name-with-dashes", factory.nextName());
        assertEquals("123numeric", factory.nextName());
        assertEquals("name_underscore", factory.nextName());
    }

    /**
     * Test that the constructor properly uses the fallback NameFactory when dictionary is exhausted.
     */
    @Test
    public void testConstructorWithFallbackToNameFactory() throws IOException {
        File dictFile = tempDir.resolve("small_dictionary.txt").toFile();
        Files.write(dictFile.toPath(), "name1\nname2\n".getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(url, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        // Now dictionary is exhausted, should fall back to SimpleNameFactory
        String fallbackName = factory.nextName();
        assertNotNull(fallbackName);
        // The fallback should generate a name (SimpleNameFactory starts with 'a')
        assertEquals("a", fallbackName);
    }

    /**
     * Test that the constructor handles an empty dictionary file.
     */
    @Test
    public void testConstructorWithEmptyDictionary() throws IOException {
        File dictFile = tempDir.resolve("empty_dictionary.txt").toFile();
        Files.write(dictFile.toPath(), "".getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(url, true, fallbackFactory);

        assertNotNull(factory);
        // Should immediately fall back to the name factory
        assertEquals("a", factory.nextName());
    }

    /**
     * Test that the constructor handles a dictionary with only comments.
     */
    @Test
    public void testConstructorWithOnlyComments() throws IOException {
        File dictFile = tempDir.resolve("only_comments.txt").toFile();
        String content = "# Comment 1\n" +
                        "# Comment 2\n" +
                        "# Comment 3\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(url, true, fallbackFactory);

        assertNotNull(factory);
        // Should immediately fall back to the name factory
        assertEquals("a", factory.nextName());
    }

    /**
     * Test that the constructor handles a dictionary with whitespace variations.
     */
    @Test
    public void testConstructorWithWhitespaceVariations() throws IOException {
        File dictFile = tempDir.resolve("whitespace_dictionary.txt").toFile();
        String content = "  name1  \n" +
                        "\tname2\t\n" +
                        "name3\r\n" +
                        " name4 ";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(url, true, fallbackFactory);

        assertNotNull(factory);
        // Whitespace should separate names
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
        assertEquals("name4", factory.nextName());
    }

    /**
     * Test that the constructor throws IOException when given an invalid URL.
     */
    @Test
    public void testConstructorWithInvalidUrl() {
        try {
            URL url = new URL("file:///nonexistent/path/to/dictionary.txt");
            NameFactory fallbackFactory = new SimpleNameFactory();

            assertThrows(IOException.class, () -> {
                new DictionaryNameFactory(url, true, fallbackFactory);
            });
        } catch (Exception e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }

    /**
     * Test that the constructor handles a dictionary with duplicate names.
     * The LinkedHashSet in the implementation should preserve order and remove duplicates.
     */
    @Test
    public void testConstructorWithDuplicateNames() throws IOException {
        File dictFile = tempDir.resolve("duplicate_dictionary.txt").toFile();
        String content = "name1\n" +
                        "name2\n" +
                        "name1\n" +  // duplicate
                        "name3\n" +
                        "name2\n";  // duplicate
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(url, true, fallbackFactory);

        assertNotNull(factory);
        // Should only see each name once, in the order first encountered
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
        // Next should be from fallback factory
        assertEquals("a", factory.nextName());
    }

    /**
     * Test that the constructor works with a URL pointing to a resource (not just file).
     */
    @Test
    public void testConstructorWithClasspathResource() throws IOException {
        // Create a dictionary file that we can read as a URL
        File dictFile = tempDir.resolve("resource_dictionary.txt").toFile();
        Files.write(dictFile.toPath(), "res1\nres2\nres3\n".getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(url, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("res1", factory.nextName());
        assertEquals("res2", factory.nextName());
        assertEquals("res3", factory.nextName());
    }

    /**
     * Test that the constructor handles UTF-8 encoded characters properly.
     */
    @Test
    public void testConstructorWithUtf8Characters() throws IOException {
        File dictFile = tempDir.resolve("utf8_dictionary.txt").toFile();
        // Java identifiers can include Unicode letters
        String content = "name1\n" +
                        "café\n" +  // é is a valid Java identifier character
                        "name2\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(url, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("café", factory.nextName());
        assertEquals("name2", factory.nextName());
    }

    /**
     * Test that reset() works properly after using the dictionary.
     */
    @Test
    public void testConstructorAndReset() throws IOException {
        File dictFile = tempDir.resolve("reset_dictionary.txt").toFile();
        Files.write(dictFile.toPath(), "first\nsecond\n".getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(url, true, fallbackFactory);

        assertEquals("first", factory.nextName());
        assertEquals("second", factory.nextName());

        // Reset should start from beginning
        factory.reset();
        assertEquals("first", factory.nextName());
        assertEquals("second", factory.nextName());
    }

    /**
     * Test that the constructor properly handles names with underscores and dollar signs
     * (valid Java identifier characters).
     */
    @Test
    public void testConstructorWithUnderscoresAndDollarSigns() throws IOException {
        File dictFile = tempDir.resolve("special_chars_dictionary.txt").toFile();
        String content = "_name1\n" +
                        "name_2\n" +
                        "$name3\n" +
                        "name$4\n" +
                        "_$name5\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(url, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("_name1", factory.nextName());
        assertEquals("name_2", factory.nextName());
        assertEquals("$name3", factory.nextName());
        assertEquals("name$4", factory.nextName());
        assertEquals("_$name5", factory.nextName());
    }

    /**
     * Test that the fallback factory never returns names that were in the dictionary.
     */
    @Test
    public void testConstructorFallbackDoesNotReturnDictionaryNames() throws IOException {
        File dictFile = tempDir.resolve("fallback_test_dictionary.txt").toFile();
        // Include some simple names that SimpleNameFactory might generate
        Files.write(dictFile.toPath(), "a\nb\nc\n".getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(url, true, fallbackFactory);

        assertEquals("a", factory.nextName());
        assertEquals("b", factory.nextName());
        assertEquals("c", factory.nextName());

        // Next should be from fallback, but should skip 'a', 'b', 'c'
        // SimpleNameFactory generates: a, b, c, d, e, ...
        // Since a, b, c are in dictionary, it should skip to 'd'
        String fallbackName = factory.nextName();
        assertNotEquals("a", fallbackName);
        assertNotEquals("b", fallbackName);
        assertNotEquals("c", fallbackName);
        assertEquals("d", fallbackName);
    }

    // ========== Tests for File-based constructor (File, NameFactory) ==========

    /**
     * Test that the File-based constructor successfully creates a DictionaryNameFactory
     * with a valid File containing valid Java identifiers.
     */
    @Test
    public void testFileConstructorWithValidFile() throws IOException {
        File dictFile = tempDir.resolve("file_constructor_dict.txt").toFile();
        Files.write(dictFile.toPath(), "alpha\nbeta\ngamma\n".getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, fallbackFactory);

        assertNotNull(factory, "DictionaryNameFactory should be created successfully");
        assertEquals("alpha", factory.nextName());
        assertEquals("beta", factory.nextName());
        assertEquals("gamma", factory.nextName());
    }

    /**
     * Test that the File-based constructor defaults to validJavaIdentifiers=true.
     * Non-Java identifiers should be filtered out.
     */
    @Test
    public void testFileConstructorDefaultsToValidJavaIdentifiers() throws IOException {
        File dictFile = tempDir.resolve("file_constructor_mixed.txt").toFile();
        String content = "validName\n" +
                        "123invalid\n" +  // starts with digit - should be filtered
                        "valid_name2\n" +
                        "name-with-dash\n";  // dash should cause split
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, fallbackFactory);

        assertNotNull(factory);
        // Since validJavaIdentifiers defaults to true, should filter/split appropriately
        assertEquals("validName", factory.nextName());
        assertEquals("invalid", factory.nextName());  // "123" filtered, "invalid" kept
        assertEquals("valid_name2", factory.nextName());
        assertEquals("name", factory.nextName());     // "name" before dash
        assertEquals("with", factory.nextName());     // "with" after dash
        assertEquals("dash", factory.nextName());     // "dash" after second dash
    }

    /**
     * Test that the File-based constructor throws IOException for non-existent files.
     */
    @Test
    public void testFileConstructorWithNonExistentFile() {
        File nonExistentFile = new File("/nonexistent/path/to/dictionary.txt");
        NameFactory fallbackFactory = new SimpleNameFactory();

        assertThrows(IOException.class, () -> {
            new DictionaryNameFactory(nonExistentFile, fallbackFactory);
        });
    }

    /**
     * Test that the File-based constructor handles an empty file.
     */
    @Test
    public void testFileConstructorWithEmptyFile() throws IOException {
        File dictFile = tempDir.resolve("empty_file.txt").toFile();
        Files.write(dictFile.toPath(), "".getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, fallbackFactory);

        assertNotNull(factory);
        // Should immediately fall back to the name factory
        assertEquals("a", factory.nextName());
    }

    /**
     * Test that the File-based constructor handles comments correctly.
     */
    @Test
    public void testFileConstructorWithComments() throws IOException {
        File dictFile = tempDir.resolve("file_with_comments.txt").toFile();
        String content = "# Header comment\n" +
                        "name1\n" +
                        "name2 # inline comment\n" +
                        "# Another comment\n" +
                        "name3\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
    }

    /**
     * Test that the File-based constructor properly uses the fallback NameFactory.
     */
    @Test
    public void testFileConstructorWithFallback() throws IOException {
        File dictFile = tempDir.resolve("small_file_dict.txt").toFile();
        Files.write(dictFile.toPath(), "first\nsecond\n".getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, fallbackFactory);

        assertNotNull(factory);
        assertEquals("first", factory.nextName());
        assertEquals("second", factory.nextName());
        // Now dictionary is exhausted, should fall back
        assertEquals("a", factory.nextName());
    }

    /**
     * Test that the File-based constructor handles files with duplicate names.
     */
    @Test
    public void testFileConstructorWithDuplicates() throws IOException {
        File dictFile = tempDir.resolve("file_with_duplicates.txt").toFile();
        String content = "name1\n" +
                        "name2\n" +
                        "name1\n" +  // duplicate
                        "name3\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, fallbackFactory);

        assertNotNull(factory);
        // Should only see each name once
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
        assertEquals("a", factory.nextName());  // fallback
    }

    /**
     * Test that the File-based constructor handles UTF-8 encoded files.
     */
    @Test
    public void testFileConstructorWithUtf8() throws IOException {
        File dictFile = tempDir.resolve("utf8_file.txt").toFile();
        String content = "simple\n" +
                        "café\n" +  // UTF-8 character
                        "naïve\n";  // UTF-8 character
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, fallbackFactory);

        assertNotNull(factory);
        assertEquals("simple", factory.nextName());
        assertEquals("café", factory.nextName());
        assertEquals("naïve", factory.nextName());
    }

    /**
     * Test that the File-based constructor handles files with special Java identifier characters.
     */
    @Test
    public void testFileConstructorWithSpecialIdentifierChars() throws IOException {
        File dictFile = tempDir.resolve("special_chars_file.txt").toFile();
        String content = "_underscore\n" +
                        "$dollar\n" +
                        "mixed_$name\n" +
                        "$_combo\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, fallbackFactory);

        assertNotNull(factory);
        assertEquals("_underscore", factory.nextName());
        assertEquals("$dollar", factory.nextName());
        assertEquals("mixed_$name", factory.nextName());
        assertEquals("$_combo", factory.nextName());
    }

    /**
     * Test that the File-based constructor works with reset().
     */
    @Test
    public void testFileConstructorWithReset() throws IOException {
        File dictFile = tempDir.resolve("reset_file.txt").toFile();
        Files.write(dictFile.toPath(), "first\nsecond\n".getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, fallbackFactory);

        assertEquals("first", factory.nextName());
        assertEquals("second", factory.nextName());

        factory.reset();
        assertEquals("first", factory.nextName());
        assertEquals("second", factory.nextName());
    }

    /**
     * Test that the File-based constructor handles whitespace-only lines.
     */
    @Test
    public void testFileConstructorWithWhitespaceLines() throws IOException {
        File dictFile = tempDir.resolve("whitespace_lines.txt").toFile();
        String content = "name1\n" +
                        "   \n" +      // whitespace only
                        "name2\n" +
                        "\t\t\n" +     // tabs only
                        "name3\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
    }

    /**
     * Test that the File-based constructor correctly handles the fallback factory
     * not returning dictionary names.
     */
    @Test
    public void testFileConstructorFallbackSkipsDictionaryNames() throws IOException {
        File dictFile = tempDir.resolve("fallback_skip_file.txt").toFile();
        Files.write(dictFile.toPath(), "a\nb\nc\n".getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, fallbackFactory);

        assertEquals("a", factory.nextName());
        assertEquals("b", factory.nextName());
        assertEquals("c", factory.nextName());
        // Fallback should skip a, b, c and return d
        assertEquals("d", factory.nextName());
    }

    /**
     * Test that the File-based constructor handles a large dictionary file.
     */
    @Test
    public void testFileConstructorWithLargeDictionary() throws IOException {
        File dictFile = tempDir.resolve("large_dict.txt").toFile();
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            content.append("name").append(i).append("\n");
        }
        Files.write(dictFile.toPath(), content.toString().getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, fallbackFactory);

        assertNotNull(factory);
        // Verify first few names
        assertEquals("name0", factory.nextName());
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());

        // Reset and verify it works
        factory.reset();
        assertEquals("name0", factory.nextName());
    }

    /**
     * Test that the File-based constructor handles names on the same line separated by spaces.
     */
    @Test
    public void testFileConstructorWithSpaceSeparatedNames() throws IOException {
        File dictFile = tempDir.resolve("space_separated.txt").toFile();
        String content = "name1 name2 name3\n" +
                        "name4\n" +
                        "name5 name6";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, fallbackFactory);

        assertNotNull(factory);
        // Spaces separate names
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
        assertEquals("name4", factory.nextName());
        assertEquals("name5", factory.nextName());
        assertEquals("name6", factory.nextName());
    }

    // ========== Tests for 3-parameter File constructor (File, boolean, NameFactory) ==========

    /**
     * Test the 3-parameter File constructor with validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamFileConstructorWithValidJavaIdentifiersTrue() throws IOException {
        File dictFile = tempDir.resolve("three_param_valid_true.txt").toFile();
        String content = "validName\n" +
                        "123invalid\n" +
                        "valid_name2\n" +
                        "name-with-dash\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, true, fallbackFactory);

        assertNotNull(factory);
        // validJavaIdentifiers=true filters non-valid identifiers
        assertEquals("validName", factory.nextName());
        assertEquals("invalid", factory.nextName());  // "123" filtered
        assertEquals("valid_name2", factory.nextName());
        assertEquals("name", factory.nextName());      // split at dash
        assertEquals("with", factory.nextName());
        assertEquals("dash", factory.nextName());
    }

    /**
     * Test the 3-parameter File constructor with validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamFileConstructorWithValidJavaIdentifiersFalse() throws IOException {
        File dictFile = tempDir.resolve("three_param_valid_false.txt").toFile();
        String content = "validName\n" +
                        "123invalid\n" +
                        "name-with-dashes\n" +
                        "symbols!@#\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, false, fallbackFactory);

        assertNotNull(factory);
        // validJavaIdentifiers=false accepts any non-whitespace, non-comment chars
        assertEquals("validName", factory.nextName());
        assertEquals("123invalid", factory.nextName());
        assertEquals("name-with-dashes", factory.nextName());
        assertEquals("symbols!@#", factory.nextName());
    }

    /**
     * Test the 3-parameter File constructor handles empty file correctly.
     */
    @Test
    public void testThreeParamFileConstructorWithEmptyFile() throws IOException {
        File dictFile = tempDir.resolve("three_param_empty.txt").toFile();
        Files.write(dictFile.toPath(), "".getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factoryTrue = new DictionaryNameFactory(dictFile, true, fallbackFactory);
        assertNotNull(factoryTrue);
        assertEquals("a", factoryTrue.nextName());

        DictionaryNameFactory factoryFalse = new DictionaryNameFactory(dictFile, false, fallbackFactory);
        assertNotNull(factoryFalse);
        assertEquals("a", factoryFalse.nextName());
    }

    /**
     * Test the 3-parameter File constructor with comments and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamFileConstructorWithCommentsValidTrue() throws IOException {
        File dictFile = tempDir.resolve("three_param_comments_true.txt").toFile();
        String content = "# Comment line\n" +
                        "name1\n" +
                        "name2 # inline comment\n" +
                        "name3\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
    }

    /**
     * Test the 3-parameter File constructor with comments and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamFileConstructorWithCommentsValidFalse() throws IOException {
        File dictFile = tempDir.resolve("three_param_comments_false.txt").toFile();
        String content = "# Comment line\n" +
                        "name-1\n" +
                        "name@2 # inline comment\n" +
                        "name_3\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name-1", factory.nextName());
        assertEquals("name@2", factory.nextName());  // @ allowed when validJavaIdentifiers=false
        assertEquals("name_3", factory.nextName());
    }

    /**
     * Test the 3-parameter File constructor throws IOException for non-existent file.
     */
    @Test
    public void testThreeParamFileConstructorWithNonExistentFile() {
        File nonExistentFile = new File("/nonexistent/path/dictionary.txt");
        NameFactory fallbackFactory = new SimpleNameFactory();

        assertThrows(IOException.class, () -> {
            new DictionaryNameFactory(nonExistentFile, true, fallbackFactory);
        });

        assertThrows(IOException.class, () -> {
            new DictionaryNameFactory(nonExistentFile, false, fallbackFactory);
        });
    }

    /**
     * Test the 3-parameter File constructor with UTF-8 and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamFileConstructorWithUtf8ValidTrue() throws IOException {
        File dictFile = tempDir.resolve("three_param_utf8_true.txt").toFile();
        String content = "simple\n" +
                        "café\n" +
                        "naïve\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("simple", factory.nextName());
        assertEquals("café", factory.nextName());
        assertEquals("naïve", factory.nextName());
    }

    /**
     * Test the 3-parameter File constructor with UTF-8 and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamFileConstructorWithUtf8ValidFalse() throws IOException {
        File dictFile = tempDir.resolve("three_param_utf8_false.txt").toFile();
        String content = "café-house\n" +
                        "naïve@test\n" +
                        "αβγ\n";  // Greek letters
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("café-house", factory.nextName());
        assertEquals("naïve@test", factory.nextName());
        assertEquals("αβγ", factory.nextName());
    }

    /**
     * Test the 3-parameter File constructor with duplicates and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamFileConstructorWithDuplicatesValidTrue() throws IOException {
        File dictFile = tempDir.resolve("three_param_dup_true.txt").toFile();
        String content = "name1\n" +
                        "name2\n" +
                        "name1\n" +  // duplicate
                        "name3\n" +
                        "name2\n";  // duplicate
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
        assertEquals("a", factory.nextName());  // fallback
    }

    /**
     * Test the 3-parameter File constructor with duplicates and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamFileConstructorWithDuplicatesValidFalse() throws IOException {
        File dictFile = tempDir.resolve("three_param_dup_false.txt").toFile();
        String content = "name-1\n" +
                        "name@2\n" +
                        "name-1\n" +  // duplicate
                        "name@2\n";  // duplicate
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name-1", factory.nextName());
        assertEquals("name@2", factory.nextName());
        assertEquals("a", factory.nextName());  // fallback, duplicates removed
    }

    /**
     * Test the 3-parameter File constructor with fallback and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamFileConstructorFallbackValidTrue() throws IOException {
        File dictFile = tempDir.resolve("three_param_fallback_true.txt").toFile();
        Files.write(dictFile.toPath(), "first\nsecond\n".getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, true, fallbackFactory);

        assertEquals("first", factory.nextName());
        assertEquals("second", factory.nextName());
        assertEquals("a", factory.nextName());  // fallback
    }

    /**
     * Test the 3-parameter File constructor with fallback and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamFileConstructorFallbackValidFalse() throws IOException {
        File dictFile = tempDir.resolve("three_param_fallback_false.txt").toFile();
        Files.write(dictFile.toPath(), "first-name\nsecond@name\n".getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, false, fallbackFactory);

        assertEquals("first-name", factory.nextName());
        assertEquals("second@name", factory.nextName());
        assertEquals("a", factory.nextName());  // fallback
    }

    /**
     * Test the 3-parameter File constructor with reset and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamFileConstructorResetValidTrue() throws IOException {
        File dictFile = tempDir.resolve("three_param_reset_true.txt").toFile();
        Files.write(dictFile.toPath(), "alpha\nbeta\n".getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, true, fallbackFactory);

        assertEquals("alpha", factory.nextName());
        assertEquals("beta", factory.nextName());

        factory.reset();
        assertEquals("alpha", factory.nextName());
        assertEquals("beta", factory.nextName());
    }

    /**
     * Test the 3-parameter File constructor with reset and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamFileConstructorResetValidFalse() throws IOException {
        File dictFile = tempDir.resolve("three_param_reset_false.txt").toFile();
        Files.write(dictFile.toPath(), "alpha-1\nbeta@2\n".getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, false, fallbackFactory);

        assertEquals("alpha-1", factory.nextName());
        assertEquals("beta@2", factory.nextName());

        factory.reset();
        assertEquals("alpha-1", factory.nextName());
        assertEquals("beta@2", factory.nextName());
    }

    /**
     * Test the 3-parameter File constructor with special identifier chars and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamFileConstructorSpecialCharsValidTrue() throws IOException {
        File dictFile = tempDir.resolve("three_param_special_true.txt").toFile();
        String content = "_underscore\n" +
                        "$dollar\n" +
                        "mixed_$name\n" +
                        "invalid-dash\n";  // dash not valid
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("_underscore", factory.nextName());
        assertEquals("$dollar", factory.nextName());
        assertEquals("mixed_$name", factory.nextName());
        assertEquals("invalid", factory.nextName());  // split at dash
        assertEquals("dash", factory.nextName());
    }

    /**
     * Test the 3-parameter File constructor with special chars and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamFileConstructorSpecialCharsValidFalse() throws IOException {
        File dictFile = tempDir.resolve("three_param_special_false.txt").toFile();
        String content = "_underscore\n" +
                        "$dollar\n" +
                        "name-with-dash\n" +
                        "name@with@at\n" +
                        "name!exclaim\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("_underscore", factory.nextName());
        assertEquals("$dollar", factory.nextName());
        assertEquals("name-with-dash", factory.nextName());
        assertEquals("name@with@at", factory.nextName());
        assertEquals("name!exclaim", factory.nextName());
    }

    /**
     * Test the 3-parameter File constructor fallback skips dictionary names with validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamFileConstructorFallbackSkipsValidTrue() throws IOException {
        File dictFile = tempDir.resolve("three_param_skip_true.txt").toFile();
        Files.write(dictFile.toPath(), "a\nb\nc\n".getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, true, fallbackFactory);

        assertEquals("a", factory.nextName());
        assertEquals("b", factory.nextName());
        assertEquals("c", factory.nextName());
        assertEquals("d", factory.nextName());  // fallback skips a, b, c
    }

    /**
     * Test the 3-parameter File constructor fallback skips dictionary names with validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamFileConstructorFallbackSkipsValidFalse() throws IOException {
        File dictFile = tempDir.resolve("three_param_skip_false.txt").toFile();
        Files.write(dictFile.toPath(), "a\nb\nc\n".getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, false, fallbackFactory);

        assertEquals("a", factory.nextName());
        assertEquals("b", factory.nextName());
        assertEquals("c", factory.nextName());
        assertEquals("d", factory.nextName());  // fallback skips a, b, c
    }

    /**
     * Test the 3-parameter File constructor with whitespace and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamFileConstructorWhitespaceValidTrue() throws IOException {
        File dictFile = tempDir.resolve("three_param_whitespace_true.txt").toFile();
        String content = "  name1  \n" +
                        "\tname2\t\n" +
                        "name3\r\n" +
                        " name4 ";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
        assertEquals("name4", factory.nextName());
    }

    /**
     * Test the 3-parameter File constructor with whitespace and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamFileConstructorWhitespaceValidFalse() throws IOException {
        File dictFile = tempDir.resolve("three_param_whitespace_false.txt").toFile();
        String content = "name-1\n" +
                        "  name@2  \n" +
                        "\tname!3\t\n";
        Files.write(dictFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name-1", factory.nextName());
        assertEquals("name@2", factory.nextName());
        assertEquals("name!3", factory.nextName());
    }

    /**
     * Test the 3-parameter File constructor with large dictionary and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamFileConstructorLargeDictValidTrue() throws IOException {
        File dictFile = tempDir.resolve("three_param_large_true.txt").toFile();
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            content.append("name").append(i).append("\n");
        }
        Files.write(dictFile.toPath(), content.toString().getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name0", factory.nextName());
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
    }

    /**
     * Test the 3-parameter File constructor with large dictionary and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamFileConstructorLargeDictValidFalse() throws IOException {
        File dictFile = tempDir.resolve("three_param_large_false.txt").toFile();
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            content.append("name-").append(i).append("\n");
        }
        Files.write(dictFile.toPath(), content.toString().getBytes(StandardCharsets.UTF_8));
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(dictFile, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name-0", factory.nextName());
        assertEquals("name-1", factory.nextName());
        assertEquals("name-2", factory.nextName());
    }

    // ========== Tests for Reader-based constructor (Reader, NameFactory) ==========

    /**
     * Test the Reader constructor with valid names.
     */
    @Test
    public void testReaderConstructorWithValidNames() throws IOException {
        String content = "alpha\nbeta\ngamma\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("alpha", factory.nextName());
        assertEquals("beta", factory.nextName());
        assertEquals("gamma", factory.nextName());
    }

    /**
     * Test the Reader constructor defaults to validJavaIdentifiers=true.
     */
    @Test
    public void testReaderConstructorDefaultsToValidJavaIdentifiers() throws IOException {
        String content = "validName\n" +
                        "123invalid\n" +
                        "valid_name2\n" +
                        "name-with-dash\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        // validJavaIdentifiers defaults to true
        assertEquals("validName", factory.nextName());
        assertEquals("invalid", factory.nextName());  // "123" filtered
        assertEquals("valid_name2", factory.nextName());
        assertEquals("name", factory.nextName());      // split at dash
        assertEquals("with", factory.nextName());
        assertEquals("dash", factory.nextName());
    }

    /**
     * Test the Reader constructor with empty content.
     */
    @Test
    public void testReaderConstructorWithEmptyContent() throws IOException {
        Reader reader = new StringReader("");
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("a", factory.nextName());  // immediate fallback
    }

    /**
     * Test the Reader constructor with comments.
     */
    @Test
    public void testReaderConstructorWithComments() throws IOException {
        String content = "# Header comment\n" +
                        "name1\n" +
                        "name2 # inline comment\n" +
                        "# Another comment\n" +
                        "name3\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
    }

    /**
     * Test the Reader constructor with fallback.
     */
    @Test
    public void testReaderConstructorWithFallback() throws IOException {
        String content = "first\nsecond\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("first", factory.nextName());
        assertEquals("second", factory.nextName());
        assertEquals("a", factory.nextName());  // fallback
    }

    /**
     * Test the Reader constructor with duplicates.
     */
    @Test
    public void testReaderConstructorWithDuplicates() throws IOException {
        String content = "name1\n" +
                        "name2\n" +
                        "name1\n" +  // duplicate
                        "name3\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
        assertEquals("a", factory.nextName());  // fallback
    }

    /**
     * Test the Reader constructor with UTF-8 characters.
     */
    @Test
    public void testReaderConstructorWithUtf8() throws IOException {
        String content = "simple\ncafé\nnaïve\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("simple", factory.nextName());
        assertEquals("café", factory.nextName());
        assertEquals("naïve", factory.nextName());
    }

    /**
     * Test the Reader constructor with special identifier characters.
     */
    @Test
    public void testReaderConstructorWithSpecialIdentifierChars() throws IOException {
        String content = "_underscore\n" +
                        "$dollar\n" +
                        "mixed_$name\n" +
                        "$_combo\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("_underscore", factory.nextName());
        assertEquals("$dollar", factory.nextName());
        assertEquals("mixed_$name", factory.nextName());
        assertEquals("$_combo", factory.nextName());
    }

    /**
     * Test the Reader constructor with reset.
     */
    @Test
    public void testReaderConstructorWithReset() throws IOException {
        String content = "first\nsecond\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertEquals("first", factory.nextName());
        assertEquals("second", factory.nextName());

        factory.reset();
        assertEquals("first", factory.nextName());
        assertEquals("second", factory.nextName());
    }

    /**
     * Test the Reader constructor with whitespace.
     */
    @Test
    public void testReaderConstructorWithWhitespace() throws IOException {
        String content = "  name1  \n" +
                        "\tname2\t\n" +
                        "name3\r\n" +
                        " name4 ";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
        assertEquals("name4", factory.nextName());
    }

    /**
     * Test the Reader constructor fallback skips dictionary names.
     */
    @Test
    public void testReaderConstructorFallbackSkipsDictionaryNames() throws IOException {
        String content = "a\nb\nc\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertEquals("a", factory.nextName());
        assertEquals("b", factory.nextName());
        assertEquals("c", factory.nextName());
        assertEquals("d", factory.nextName());  // fallback skips a, b, c
    }

    /**
     * Test the Reader constructor with space-separated names.
     */
    @Test
    public void testReaderConstructorWithSpaceSeparatedNames() throws IOException {
        String content = "name1 name2 name3\n" +
                        "name4\n" +
                        "name5 name6";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
        assertEquals("name4", factory.nextName());
        assertEquals("name5", factory.nextName());
        assertEquals("name6", factory.nextName());
    }

    /**
     * Test the Reader constructor with only comments.
     */
    @Test
    public void testReaderConstructorWithOnlyComments() throws IOException {
        String content = "# Comment 1\n" +
                        "# Comment 2\n" +
                        "# Comment 3\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("a", factory.nextName());  // immediate fallback
    }

    /**
     * Test the Reader constructor with whitespace-only lines.
     */
    @Test
    public void testReaderConstructorWithWhitespaceOnlyLines() throws IOException {
        String content = "name1\n" +
                        "   \n" +      // whitespace only
                        "name2\n" +
                        "\t\t\n" +     // tabs only
                        "name3\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
    }

    /**
     * Test the Reader constructor with large dictionary.
     */
    @Test
    public void testReaderConstructorWithLargeDictionary() throws IOException {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            content.append("name").append(i).append("\n");
        }
        Reader reader = new StringReader(content.toString());
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name0", factory.nextName());
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());

        factory.reset();
        assertEquals("name0", factory.nextName());
    }

    /**
     * Test the Reader constructor with mixed valid/invalid identifiers.
     */
    @Test
    public void testReaderConstructorWithMixedIdentifiers() throws IOException {
        String content = "validName\n" +
                        "123\n" +           // digits only - invalid start
                        "$validStart\n" +
                        "name@invalid\n" +  // @ not valid
                        "_validUnderscore\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("validName", factory.nextName());
        // "123" skipped as invalid start
        assertEquals("$validStart", factory.nextName());
        assertEquals("name", factory.nextName());      // split at @
        assertEquals("invalid", factory.nextName());
        assertEquals("_validUnderscore", factory.nextName());
    }

    /**
     * Test the Reader constructor with consecutive delimiters.
     */
    @Test
    public void testReaderConstructorWithConsecutiveDelimiters() throws IOException {
        String content = "name1\n\n\nname2\r\n\r\nname3";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
    }

    /**
     * Test the Reader constructor with Greek letters (valid identifiers).
     */
    @Test
    public void testReaderConstructorWithGreekLetters() throws IOException {
        String content = "alpha\nβeta\nγamma\n";  // β and γ are valid Java identifier chars
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, fallbackFactory);

        assertNotNull(factory);
        assertEquals("alpha", factory.nextName());
        assertEquals("βeta", factory.nextName());
        assertEquals("γamma", factory.nextName());
    }

    /**
     * Test the Reader constructor ensures reader is closed after reading.
     * This tests the documented behavior that the reader is closed at the end.
     */
    @Test
    public void testReaderConstructorClosesReader() throws IOException {
        String content = "name1\nname2\n";
        TestReader testReader = new TestReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(testReader, fallbackFactory);

        assertTrue(testReader.isClosed(), "Reader should be closed after construction");
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
    }

    /**
     * Helper class to test if reader is closed.
     */
    private static class TestReader extends StringReader {
        private boolean closed = false;

        public TestReader(String s) {
            super(s);
        }

        @Override
        public void close() {
            closed = true;
            super.close();
        }

        public boolean isClosed() {
            return closed;
        }
    }

    // ========== Tests for 3-parameter Reader constructor (Reader, boolean, NameFactory) ==========

    /**
     * Test the 3-parameter Reader constructor with validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamReaderConstructorWithValidJavaIdentifiersTrue() throws IOException {
        String content = "validName\n" +
                        "123invalid\n" +
                        "valid_name2\n" +
                        "name-with-dash\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("validName", factory.nextName());
        assertEquals("invalid", factory.nextName());  // "123" filtered
        assertEquals("valid_name2", factory.nextName());
        assertEquals("name", factory.nextName());      // split at dash
        assertEquals("with", factory.nextName());
        assertEquals("dash", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamReaderConstructorWithValidJavaIdentifiersFalse() throws IOException {
        String content = "validName\n" +
                        "123invalid\n" +
                        "name-with-dashes\n" +
                        "symbols!@#\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("validName", factory.nextName());
        assertEquals("123invalid", factory.nextName());
        assertEquals("name-with-dashes", factory.nextName());
        assertEquals("symbols!@#", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with empty content.
     */
    @Test
    public void testThreeParamReaderConstructorWithEmptyContent() throws IOException {
        Reader readerTrue = new StringReader("");
        NameFactory fallbackFactoryTrue = new SimpleNameFactory();
        DictionaryNameFactory factoryTrue = new DictionaryNameFactory(readerTrue, true, fallbackFactoryTrue);
        assertNotNull(factoryTrue);
        assertEquals("a", factoryTrue.nextName());

        Reader readerFalse = new StringReader("");
        NameFactory fallbackFactoryFalse = new SimpleNameFactory();
        DictionaryNameFactory factoryFalse = new DictionaryNameFactory(readerFalse, false, fallbackFactoryFalse);
        assertNotNull(factoryFalse);
        assertEquals("a", factoryFalse.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with comments and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamReaderConstructorWithCommentsValidTrue() throws IOException {
        String content = "# Comment line\n" +
                        "name1\n" +
                        "name2 # inline comment\n" +
                        "name3\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with comments and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamReaderConstructorWithCommentsValidFalse() throws IOException {
        String content = "# Comment line\n" +
                        "name-1\n" +
                        "name@2 # inline comment\n" +
                        "name_3\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name-1", factory.nextName());
        assertEquals("name@2", factory.nextName());
        assertEquals("name_3", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with UTF-8 and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamReaderConstructorWithUtf8ValidTrue() throws IOException {
        String content = "simple\ncafé\nnaïve\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("simple", factory.nextName());
        assertEquals("café", factory.nextName());
        assertEquals("naïve", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with UTF-8 and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamReaderConstructorWithUtf8ValidFalse() throws IOException {
        String content = "café-house\nnaïve@test\nαβγ\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("café-house", factory.nextName());
        assertEquals("naïve@test", factory.nextName());
        assertEquals("αβγ", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with duplicates and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamReaderConstructorWithDuplicatesValidTrue() throws IOException {
        String content = "name1\nname2\nname1\nname3\nname2\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
        assertEquals("a", factory.nextName());  // fallback
    }

    /**
     * Test the 3-parameter Reader constructor with duplicates and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamReaderConstructorWithDuplicatesValidFalse() throws IOException {
        String content = "name-1\nname@2\nname-1\nname@2\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name-1", factory.nextName());
        assertEquals("name@2", factory.nextName());
        assertEquals("a", factory.nextName());  // fallback
    }

    /**
     * Test the 3-parameter Reader constructor with fallback and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamReaderConstructorFallbackValidTrue() throws IOException {
        String content = "first\nsecond\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, true, fallbackFactory);

        assertEquals("first", factory.nextName());
        assertEquals("second", factory.nextName());
        assertEquals("a", factory.nextName());  // fallback
    }

    /**
     * Test the 3-parameter Reader constructor with fallback and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamReaderConstructorFallbackValidFalse() throws IOException {
        String content = "first-name\nsecond@name\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, false, fallbackFactory);

        assertEquals("first-name", factory.nextName());
        assertEquals("second@name", factory.nextName());
        assertEquals("a", factory.nextName());  // fallback
    }

    /**
     * Test the 3-parameter Reader constructor with reset and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamReaderConstructorResetValidTrue() throws IOException {
        String content = "alpha\nbeta\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, true, fallbackFactory);

        assertEquals("alpha", factory.nextName());
        assertEquals("beta", factory.nextName());

        factory.reset();
        assertEquals("alpha", factory.nextName());
        assertEquals("beta", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with reset and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamReaderConstructorResetValidFalse() throws IOException {
        String content = "alpha-1\nbeta@2\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, false, fallbackFactory);

        assertEquals("alpha-1", factory.nextName());
        assertEquals("beta@2", factory.nextName());

        factory.reset();
        assertEquals("alpha-1", factory.nextName());
        assertEquals("beta@2", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with special chars and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamReaderConstructorSpecialCharsValidTrue() throws IOException {
        String content = "_underscore\n$dollar\nmixed_$name\ninvalid-dash\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("_underscore", factory.nextName());
        assertEquals("$dollar", factory.nextName());
        assertEquals("mixed_$name", factory.nextName());
        assertEquals("invalid", factory.nextName());  // split at dash
        assertEquals("dash", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with special chars and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamReaderConstructorSpecialCharsValidFalse() throws IOException {
        String content = "_underscore\n$dollar\nname-with-dash\nname@with@at\nname!exclaim\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("_underscore", factory.nextName());
        assertEquals("$dollar", factory.nextName());
        assertEquals("name-with-dash", factory.nextName());
        assertEquals("name@with@at", factory.nextName());
        assertEquals("name!exclaim", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor fallback skips dictionary names with validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamReaderConstructorFallbackSkipsValidTrue() throws IOException {
        String content = "a\nb\nc\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, true, fallbackFactory);

        assertEquals("a", factory.nextName());
        assertEquals("b", factory.nextName());
        assertEquals("c", factory.nextName());
        assertEquals("d", factory.nextName());  // fallback skips a, b, c
    }

    /**
     * Test the 3-parameter Reader constructor fallback skips dictionary names with validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamReaderConstructorFallbackSkipsValidFalse() throws IOException {
        String content = "a\nb\nc\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, false, fallbackFactory);

        assertEquals("a", factory.nextName());
        assertEquals("b", factory.nextName());
        assertEquals("c", factory.nextName());
        assertEquals("d", factory.nextName());  // fallback skips a, b, c
    }

    /**
     * Test the 3-parameter Reader constructor with whitespace and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamReaderConstructorWhitespaceValidTrue() throws IOException {
        String content = "  name1  \n\tname2\t\nname3\r\n name4 ";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
        assertEquals("name4", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with whitespace and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamReaderConstructorWhitespaceValidFalse() throws IOException {
        String content = "name-1\n  name@2  \n\tname!3\t\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name-1", factory.nextName());
        assertEquals("name@2", factory.nextName());
        assertEquals("name!3", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with large dictionary and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamReaderConstructorLargeDictValidTrue() throws IOException {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            content.append("name").append(i).append("\n");
        }
        Reader reader = new StringReader(content.toString());
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name0", factory.nextName());
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with large dictionary and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamReaderConstructorLargeDictValidFalse() throws IOException {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            content.append("name-").append(i).append("\n");
        }
        Reader reader = new StringReader(content.toString());
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name-0", factory.nextName());
        assertEquals("name-1", factory.nextName());
        assertEquals("name-2", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor ensures reader is closed with validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamReaderConstructorClosesReaderValidTrue() throws IOException {
        String content = "name1\nname2\n";
        TestReader testReader = new TestReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(testReader, true, fallbackFactory);

        assertTrue(testReader.isClosed(), "Reader should be closed after construction");
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor ensures reader is closed with validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamReaderConstructorClosesReaderValidFalse() throws IOException {
        String content = "name-1\nname@2\n";
        TestReader testReader = new TestReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(testReader, false, fallbackFactory);

        assertTrue(testReader.isClosed(), "Reader should be closed after construction");
        assertEquals("name-1", factory.nextName());
        assertEquals("name@2", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with only comments and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamReaderConstructorOnlyCommentsValidTrue() throws IOException {
        String content = "# Comment 1\n# Comment 2\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("a", factory.nextName());  // immediate fallback
    }

    /**
     * Test the 3-parameter Reader constructor with only comments and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamReaderConstructorOnlyCommentsValidFalse() throws IOException {
        String content = "# Comment 1\n# Comment 2\n";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("a", factory.nextName());  // immediate fallback
    }

    /**
     * Test the 3-parameter Reader constructor with space-separated names and validJavaIdentifiers=true.
     */
    @Test
    public void testThreeParamReaderConstructorSpaceSeparatedValidTrue() throws IOException {
        String content = "name1 name2 name3\nname4\nname5 name6";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, true, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name1", factory.nextName());
        assertEquals("name2", factory.nextName());
        assertEquals("name3", factory.nextName());
        assertEquals("name4", factory.nextName());
        assertEquals("name5", factory.nextName());
        assertEquals("name6", factory.nextName());
    }

    /**
     * Test the 3-parameter Reader constructor with space-separated names and validJavaIdentifiers=false.
     */
    @Test
    public void testThreeParamReaderConstructorSpaceSeparatedValidFalse() throws IOException {
        String content = "name-1 name@2 name!3";
        Reader reader = new StringReader(content);
        NameFactory fallbackFactory = new SimpleNameFactory();

        DictionaryNameFactory factory = new DictionaryNameFactory(reader, false, fallbackFactory);

        assertNotNull(factory);
        assertEquals("name-1", factory.nextName());
        assertEquals("name@2", factory.nextName());
        assertEquals("name!3", factory.nextName());
    }

    // ========== Tests for copy constructor (DictionaryNameFactory, NameFactory) ==========

    /**
     * Test the copy constructor successfully copies dictionary from another DictionaryNameFactory.
     */
    @Test
    public void testCopyConstructorWithValidSource() throws IOException {
        // Create source factory
        String content = "alpha\nbeta\ngamma\n";
        Reader reader = new StringReader(content);
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);

        // Create copy with different fallback
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        assertNotNull(copyFactory);
        // Should produce same names from dictionary
        assertEquals("alpha", copyFactory.nextName());
        assertEquals("beta", copyFactory.nextName());
        assertEquals("gamma", copyFactory.nextName());
    }

    /**
     * Test the copy constructor with a different fallback factory.
     */
    @Test
    public void testCopyConstructorWithDifferentFallback() throws IOException {
        // Create source factory with SimpleNameFactory
        String content = "name1\nname2\n";
        Reader reader = new StringReader(content);
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);

        // Create copy with different fallback
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        // Exhaust dictionary
        assertEquals("name1", copyFactory.nextName());
        assertEquals("name2", copyFactory.nextName());

        // Should use the new fallback factory
        assertEquals("a", copyFactory.nextName());
    }

    /**
     * Test the copy constructor shares the same names list (not a deep copy).
     */
    @Test
    public void testCopyConstructorSharesNamesList() throws IOException {
        // Create source factory
        String content = "first\nsecond\nthird\n";
        Reader reader = new StringReader(content);
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);

        // Create copy
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        // Both should produce the same names
        assertEquals("first", sourceFactory.nextName());
        assertEquals("first", copyFactory.nextName());

        assertEquals("second", sourceFactory.nextName());
        assertEquals("second", copyFactory.nextName());
    }

    /**
     * Test the copy constructor with empty dictionary.
     */
    @Test
    public void testCopyConstructorWithEmptyDictionary() throws IOException {
        // Create source factory with empty dictionary
        Reader reader = new StringReader("");
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);

        // Create copy
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        assertNotNull(copyFactory);
        // Should immediately use fallback
        assertEquals("a", copyFactory.nextName());
    }

    /**
     * Test the copy constructor reset behavior.
     */
    @Test
    public void testCopyConstructorReset() throws IOException {
        // Create source factory
        String content = "name1\nname2\n";
        Reader reader = new StringReader(content);
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);

        // Create copy
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        assertEquals("name1", copyFactory.nextName());
        assertEquals("name2", copyFactory.nextName());

        // Reset should work
        copyFactory.reset();
        assertEquals("name1", copyFactory.nextName());
        assertEquals("name2", copyFactory.nextName());
    }

    /**
     * Test the copy constructor maintains independent state from source.
     */
    @Test
    public void testCopyConstructorIndependentState() throws IOException {
        // Create source factory
        String content = "name1\nname2\nname3\n";
        Reader reader = new StringReader(content);
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);

        // Advance source factory
        assertEquals("name1", sourceFactory.nextName());

        // Create copy (should start from beginning)
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        // Copy should start from beginning
        assertEquals("name1", copyFactory.nextName());
        assertEquals("name2", copyFactory.nextName());

        // Source can continue independently
        assertEquals("name2", sourceFactory.nextName());
    }

    /**
     * Test the copy constructor with source that has been exhausted.
     */
    @Test
    public void testCopyConstructorFromExhaustedSource() throws IOException {
        // Create source factory and exhaust it
        String content = "name1\nname2\n";
        Reader reader = new StringReader(content);
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);
        sourceFactory.nextName();
        sourceFactory.nextName();
        sourceFactory.nextName();  // Now using fallback

        // Create copy from exhausted source
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        // Copy should still start from beginning of dictionary
        assertEquals("name1", copyFactory.nextName());
        assertEquals("name2", copyFactory.nextName());
    }

    /**
     * Test the copy constructor fallback skips dictionary names.
     */
    @Test
    public void testCopyConstructorFallbackSkipsDictionaryNames() throws IOException {
        // Create source factory
        String content = "a\nb\nc\n";
        Reader reader = new StringReader(content);
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);

        // Create copy
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        assertEquals("a", copyFactory.nextName());
        assertEquals("b", copyFactory.nextName());
        assertEquals("c", copyFactory.nextName());
        // Fallback should skip a, b, c
        assertEquals("d", copyFactory.nextName());
    }

    /**
     * Test the copy constructor with large dictionary.
     */
    @Test
    public void testCopyConstructorWithLargeDictionary() throws IOException {
        // Create source factory with large dictionary
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            content.append("name").append(i).append("\n");
        }
        Reader reader = new StringReader(content.toString());
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);

        // Create copy
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        assertNotNull(copyFactory);
        assertEquals("name0", copyFactory.nextName());
        assertEquals("name1", copyFactory.nextName());
        assertEquals("name2", copyFactory.nextName());
    }

    /**
     * Test the copy constructor preserves dictionary order.
     */
    @Test
    public void testCopyConstructorPreservesOrder() throws IOException {
        // Create source factory
        String content = "first\nsecond\nthird\nfourth\nfifth\n";
        Reader reader = new StringReader(content);
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);

        // Create copy
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        // Order should be preserved
        assertEquals("first", copyFactory.nextName());
        assertEquals("second", copyFactory.nextName());
        assertEquals("third", copyFactory.nextName());
        assertEquals("fourth", copyFactory.nextName());
        assertEquals("fifth", copyFactory.nextName());
    }

    /**
     * Test multiple copy constructors can be created from the same source.
     */
    @Test
    public void testCopyConstructorMultipleCopies() throws IOException {
        // Create source factory
        String content = "name1\nname2\nname3\n";
        Reader reader = new StringReader(content);
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);

        // Create multiple copies
        NameFactory fallback1 = new SimpleNameFactory();
        NameFactory fallback2 = new SimpleNameFactory();
        DictionaryNameFactory copy1 = new DictionaryNameFactory(sourceFactory, fallback1);
        DictionaryNameFactory copy2 = new DictionaryNameFactory(sourceFactory, fallback2);

        // Both copies should work independently
        assertEquals("name1", copy1.nextName());
        assertEquals("name1", copy2.nextName());
        assertEquals("name2", copy1.nextName());
        assertEquals("name2", copy2.nextName());
    }

    /**
     * Test the copy constructor with a source that has duplicates removed.
     */
    @Test
    public void testCopyConstructorWithDuplicatesRemoved() throws IOException {
        // Create source factory with duplicates
        String content = "name1\nname2\nname1\nname3\n";
        Reader reader = new StringReader(content);
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);

        // Create copy
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        // Copy should have same deduplicated dictionary
        assertEquals("name1", copyFactory.nextName());
        assertEquals("name2", copyFactory.nextName());
        assertEquals("name3", copyFactory.nextName());
        assertEquals("a", copyFactory.nextName());  // fallback
    }

    /**
     * Test the copy constructor can be chained.
     */
    @Test
    public void testCopyConstructorChaining() throws IOException {
        // Create source factory
        String content = "alpha\nbeta\n";
        Reader reader = new StringReader(content);
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);

        // Create first copy
        NameFactory fallback1 = new SimpleNameFactory();
        DictionaryNameFactory copy1 = new DictionaryNameFactory(sourceFactory, fallback1);

        // Create copy of copy
        NameFactory fallback2 = new SimpleNameFactory();
        DictionaryNameFactory copy2 = new DictionaryNameFactory(copy1, fallback2);

        // All should produce same dictionary names
        assertEquals("alpha", sourceFactory.nextName());
        assertEquals("alpha", copy1.nextName());
        assertEquals("alpha", copy2.nextName());
    }

    /**
     * Test the copy constructor with special characters in dictionary.
     */
    @Test
    public void testCopyConstructorWithSpecialCharacters() throws IOException {
        // Create source factory with special characters
        String content = "_underscore\n$dollar\nmixed_$name\n";
        Reader reader = new StringReader(content);
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);

        // Create copy
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        assertEquals("_underscore", copyFactory.nextName());
        assertEquals("$dollar", copyFactory.nextName());
        assertEquals("mixed_$name", copyFactory.nextName());
    }

    /**
     * Test the copy constructor with UTF-8 characters.
     */
    @Test
    public void testCopyConstructorWithUtf8() throws IOException {
        // Create source factory with UTF-8
        String content = "café\nnaïve\nαβγ\n";
        Reader reader = new StringReader(content);
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, false, sourceFallback);

        // Create copy
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        assertEquals("café", copyFactory.nextName());
        assertEquals("naïve", copyFactory.nextName());
        assertEquals("αβγ", copyFactory.nextName());
    }

    /**
     * Test the copy constructor reset on copy doesn't affect source.
     */
    @Test
    public void testCopyConstructorResetIndependence() throws IOException {
        // Create source factory
        String content = "name1\nname2\nname3\n";
        Reader reader = new StringReader(content);
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(reader, sourceFallback);

        // Create copy and advance both
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);
        sourceFactory.nextName();
        sourceFactory.nextName();
        copyFactory.nextName();
        copyFactory.nextName();

        // Reset copy
        copyFactory.reset();
        assertEquals("name1", copyFactory.nextName());

        // Source should be unaffected
        assertEquals("name3", sourceFactory.nextName());
    }

    /**
     * Test the copy constructor with source created from URL.
     */
    @Test
    public void testCopyConstructorFromUrlSource() throws IOException {
        // Create source factory from URL
        File dictFile = tempDir.resolve("url_copy_source.txt").toFile();
        Files.write(dictFile.toPath(), "url1\nurl2\nurl3\n".getBytes(StandardCharsets.UTF_8));
        URL url = dictFile.toURI().toURL();
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(url, sourceFallback);

        // Create copy
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        assertEquals("url1", copyFactory.nextName());
        assertEquals("url2", copyFactory.nextName());
        assertEquals("url3", copyFactory.nextName());
    }

    /**
     * Test the copy constructor with source created from File.
     */
    @Test
    public void testCopyConstructorFromFileSource() throws IOException {
        // Create source factory from File
        File dictFile = tempDir.resolve("file_copy_source.txt").toFile();
        Files.write(dictFile.toPath(), "file1\nfile2\n".getBytes(StandardCharsets.UTF_8));
        NameFactory sourceFallback = new SimpleNameFactory();
        DictionaryNameFactory sourceFactory = new DictionaryNameFactory(dictFile, sourceFallback);

        // Create copy
        NameFactory newFallback = new SimpleNameFactory();
        DictionaryNameFactory copyFactory = new DictionaryNameFactory(sourceFactory, newFallback);

        assertEquals("file1", copyFactory.nextName());
        assertEquals("file2", copyFactory.nextName());
    }
}
