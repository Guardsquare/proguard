package proguard.io;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ExtraDataEntryReader.
 * Tests all constructors and the read method with comprehensive coverage.
 */
public class ExtraDataEntryReaderClaudeTest {

    /**
     * Test constructor with extra entry name and single reader.
     */
    @Test
    public void testConstructorWithExtraEntryNameAndSingleReader() {
        // Arrange
        String extraEntryName = "extra.txt";
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader(extraEntryName, reader);

        // Assert
        assertNotNull(extraReader, "ExtraDataEntryReader should be created");
    }

    /**
     * Test constructor with extra entry name and two readers.
     */
    @Test
    public void testConstructorWithExtraEntryNameAndTwoReaders() {
        // Arrange
        String extraEntryName = "extra.txt";
        TestDataEntryReader dataReader = new TestDataEntryReader();
        TestDataEntryReader extraReader = new TestDataEntryReader();

        // Act
        ExtraDataEntryReader reader = new ExtraDataEntryReader(extraEntryName, dataReader, extraReader);

        // Assert
        assertNotNull(reader, "ExtraDataEntryReader should be created");
    }

    /**
     * Test constructor with ExtraDataEntryNameMap and single reader.
     */
    @Test
    public void testConstructorWithNameMapAndSingleReader() {
        // Arrange
        ExtraDataEntryNameMap nameMap = new ExtraDataEntryNameMap();
        nameMap.addExtraDataEntry("extra.txt");
        TestDataEntryReader reader = new TestDataEntryReader();

        // Act
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader(nameMap, reader);

        // Assert
        assertNotNull(extraReader, "ExtraDataEntryReader should be created");
    }

    /**
     * Test constructor with ExtraDataEntryNameMap and two readers.
     */
    @Test
    public void testConstructorWithNameMapAndTwoReaders() {
        // Arrange
        ExtraDataEntryNameMap nameMap = new ExtraDataEntryNameMap();
        nameMap.addExtraDataEntry("extra.txt");
        TestDataEntryReader dataReader = new TestDataEntryReader();
        TestDataEntryReader extraReader = new TestDataEntryReader();

        // Act
        ExtraDataEntryReader reader = new ExtraDataEntryReader(nameMap, dataReader, extraReader);

        // Assert
        assertNotNull(reader, "ExtraDataEntryReader should be created");
    }

    /**
     * Test read method reads default extra entry before main entry.
     */
    @Test
    public void testReadWithDefaultExtraEntry() throws IOException {
        // Arrange
        String extraEntryName = "extra.txt";
        TestDataEntryReader reader = new TestDataEntryReader();
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader(extraEntryName, reader);

        TestDataEntry mainEntry = new TestDataEntry("main.txt", null);

        // Act
        extraReader.read(mainEntry);

        // Assert
        assertEquals(2, reader.getEntriesRead().size(), "Should read 2 entries (extra + main)");
        assertEquals("extra.txt", reader.getEntriesRead().get(0).getName(),
            "Extra entry should be read first");
        assertEquals("main.txt", reader.getEntriesRead().get(1).getName(),
            "Main entry should be read second");
    }

    /**
     * Test read method only reads default extra entry once.
     */
    @Test
    public void testReadDefaultExtraEntryOnlyOnce() throws IOException {
        // Arrange
        String extraEntryName = "extra.txt";
        TestDataEntryReader reader = new TestDataEntryReader();
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader(extraEntryName, reader);

        TestDataEntry entry1 = new TestDataEntry("main1.txt", null);
        TestDataEntry entry2 = new TestDataEntry("main2.txt", null);

        // Act
        extraReader.read(entry1);
        extraReader.read(entry2);

        // Assert
        assertEquals(3, reader.getEntriesRead().size(),
            "Should read 3 entries (extra once + 2 main entries)");
        assertEquals("extra.txt", reader.getEntriesRead().get(0).getName(),
            "Extra entry should be read first");
        assertEquals("main1.txt", reader.getEntriesRead().get(1).getName(),
            "First main entry should be read second");
        assertEquals("main2.txt", reader.getEntriesRead().get(2).getName(),
            "Second main entry should be read third");
    }

    /**
     * Test read method with entry-specific extra entries.
     */
    @Test
    public void testReadWithEntrySpecificExtraEntries() throws IOException {
        // Arrange
        ExtraDataEntryNameMap nameMap = new ExtraDataEntryNameMap();
        nameMap.addExtraDataEntry("main.txt", "extra1.txt");
        nameMap.addExtraDataEntry("main.txt", "extra2.txt");

        TestDataEntryReader reader = new TestDataEntryReader();
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader(nameMap, reader);

        TestDataEntry mainEntry = new TestDataEntry("main.txt", null);

        // Act
        extraReader.read(mainEntry);

        // Assert
        assertTrue(reader.getEntriesRead().size() >= 3,
            "Should read at least 3 entries (2 extras + main)");

        List<String> readNames = new ArrayList<>();
        for (TestDataEntry entry : reader.getEntriesRead()) {
            readNames.add(entry.getName());
        }
        assertTrue(readNames.contains("extra1.txt"), "Should read extra1.txt");
        assertTrue(readNames.contains("extra2.txt"), "Should read extra2.txt");
        assertEquals("main.txt", reader.getEntriesRead().get(reader.getEntriesRead().size() - 1).getName(),
            "Main entry should be read last");
    }

    /**
     * Test read method with both default and entry-specific extra entries.
     */
    @Test
    public void testReadWithMixedExtraEntries() throws IOException {
        // Arrange
        ExtraDataEntryNameMap nameMap = new ExtraDataEntryNameMap();
        nameMap.addExtraDataEntry("default.txt"); // Default extra entry
        nameMap.addExtraDataEntry("main.txt", "specific.txt"); // Entry-specific

        TestDataEntryReader reader = new TestDataEntryReader();
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader(nameMap, reader);

        TestDataEntry mainEntry = new TestDataEntry("main.txt", null);

        // Act
        extraReader.read(mainEntry);

        // Assert
        assertTrue(reader.getEntriesRead().size() >= 3,
            "Should read at least 3 entries (default + specific + main)");

        List<String> readNames = new ArrayList<>();
        for (TestDataEntry entry : reader.getEntriesRead()) {
            readNames.add(entry.getName());
        }
        assertTrue(readNames.contains("default.txt"), "Should read default.txt");
        assertTrue(readNames.contains("specific.txt"), "Should read specific.txt");
        assertTrue(readNames.contains("main.txt"), "Should read main.txt");

        // Main entry should be last
        assertEquals("main.txt", reader.getEntriesRead().get(reader.getEntriesRead().size() - 1).getName(),
            "Main entry should be read last");
    }

    /**
     * Test read method reads each extra entry only once even if referenced multiple times.
     */
    @Test
    public void testReadExtraEntryOnlyOnceWhenReferencedMultipleTimes() throws IOException {
        // Arrange
        ExtraDataEntryNameMap nameMap = new ExtraDataEntryNameMap();
        nameMap.addExtraDataEntry("entry1.txt", "extra.txt");
        nameMap.addExtraDataEntry("entry2.txt", "extra.txt"); // Same extra for different entries

        TestDataEntryReader reader = new TestDataEntryReader();
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader(nameMap, reader);

        TestDataEntry entry1 = new TestDataEntry("entry1.txt", null);
        TestDataEntry entry2 = new TestDataEntry("entry2.txt", null);

        // Act
        extraReader.read(entry1);
        extraReader.read(entry2);

        // Assert
        List<String> readNames = new ArrayList<>();
        for (TestDataEntry entry : reader.getEntriesRead()) {
            readNames.add(entry.getName());
        }

        long extraCount = readNames.stream().filter(name -> name.equals("extra.txt")).count();
        assertEquals(1, extraCount, "Extra entry should be read only once");
        assertEquals(3, reader.getEntriesRead().size(),
            "Should read 3 entries total (extra once + 2 main entries)");
    }

    /**
     * Test read method with separate data and extra data readers.
     */
    @Test
    public void testReadWithSeparateReaders() throws IOException {
        // Arrange
        ExtraDataEntryNameMap nameMap = new ExtraDataEntryNameMap();
        nameMap.addExtraDataEntry("extra.txt");

        TestDataEntryReader dataReader = new TestDataEntryReader();
        TestDataEntryReader extraDataReader = new TestDataEntryReader();
        ExtraDataEntryReader reader = new ExtraDataEntryReader(nameMap, dataReader, extraDataReader);

        TestDataEntry mainEntry = new TestDataEntry("main.txt", null);

        // Act
        reader.read(mainEntry);

        // Assert
        assertEquals(1, extraDataReader.getEntriesRead().size(),
            "Extra reader should read 1 entry");
        assertEquals("extra.txt", extraDataReader.getEntriesRead().get(0).getName(),
            "Extra reader should read extra entry");

        assertEquals(1, dataReader.getEntriesRead().size(),
            "Data reader should read 1 entry");
        assertEquals("main.txt", dataReader.getEntriesRead().get(0).getName(),
            "Data reader should read main entry");
    }

    /**
     * Test read method with entry that has no extra entries.
     */
    @Test
    public void testReadEntryWithNoExtraEntries() throws IOException {
        // Arrange
        ExtraDataEntryNameMap nameMap = new ExtraDataEntryNameMap();
        TestDataEntryReader reader = new TestDataEntryReader();
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader(nameMap, reader);

        TestDataEntry mainEntry = new TestDataEntry("main.txt", null);

        // Act
        extraReader.read(mainEntry);

        // Assert
        assertEquals(1, reader.getEntriesRead().size(), "Should read only main entry");
        assertEquals("main.txt", reader.getEntriesRead().get(0).getName(),
            "Should read main entry");
    }

    /**
     * Test read method with nested extra entries (extra entry has its own extra entries).
     */
    @Test
    public void testReadWithNestedExtraEntries() throws IOException {
        // Arrange
        ExtraDataEntryNameMap nameMap = new ExtraDataEntryNameMap();
        nameMap.addExtraDataEntry("main.txt", "extra1.txt");
        nameMap.addExtraDataEntry("extra1.txt", "extra2.txt"); // Nested extra

        TestDataEntryReader reader = new TestDataEntryReader();
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader(nameMap, reader);

        TestDataEntry mainEntry = new TestDataEntry("main.txt", null);

        // Act
        extraReader.read(mainEntry);

        // Assert
        assertTrue(reader.getEntriesRead().size() >= 3,
            "Should read at least 3 entries");

        List<String> readNames = new ArrayList<>();
        for (TestDataEntry entry : reader.getEntriesRead()) {
            readNames.add(entry.getName());
        }

        assertTrue(readNames.contains("extra2.txt"), "Should read nested extra2.txt");
        assertTrue(readNames.contains("extra1.txt"), "Should read extra1.txt");
        assertTrue(readNames.contains("main.txt"), "Should read main.txt");

        // Main entry should be last
        assertEquals("main.txt", reader.getEntriesRead().get(reader.getEntriesRead().size() - 1).getName(),
            "Main entry should be read last");
    }

    /**
     * Test read method preserves parent entry for main entry.
     */
    @Test
    public void testReadPreservesParentEntry() throws IOException {
        // Arrange
        TestDataEntry parentEntry = new TestDataEntry("parent.jar", null);
        TestDataEntry childEntry = new TestDataEntry("child.txt", parentEntry);

        TestDataEntryReader reader = new TestDataEntryReader();
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader("extra.txt", reader);

        // Act
        extraReader.read(childEntry);

        // Assert
        TestDataEntry readMainEntry = reader.getEntriesRead().get(reader.getEntriesRead().size() - 1);
        assertEquals("child.txt", readMainEntry.getName(), "Main entry name should be preserved");
        assertEquals(parentEntry, readMainEntry.getParent(), "Parent entry should be preserved");
    }

    /**
     * Test read method creates extra entries with correct parent.
     */
    @Test
    public void testReadCreatesExtraEntriesWithCorrectParent() throws IOException {
        // Arrange
        TestDataEntry parentEntry = new TestDataEntry("parent.jar", null);
        TestDataEntry childEntry = new TestDataEntry("child.txt", parentEntry);

        TestDataEntryReader reader = new TestDataEntryReader();
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader("extra.txt", reader);

        // Act
        extraReader.read(childEntry);

        // Assert
        TestDataEntry extraEntry = reader.getEntriesRead().get(0);
        assertEquals("extra.txt", extraEntry.getName(), "Extra entry should have correct name");
        assertEquals(parentEntry, extraEntry.getParent(), "Extra entry should have same parent as main entry");
        assertFalse(extraEntry.isDirectory(), "Extra entry should not be a directory");
    }

    /**
     * Test read method with null parent entry.
     */
    @Test
    public void testReadWithNullParent() throws IOException {
        // Arrange
        TestDataEntry mainEntry = new TestDataEntry("main.txt", null);

        TestDataEntryReader reader = new TestDataEntryReader();
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader("extra.txt", reader);

        // Act
        extraReader.read(mainEntry);

        // Assert
        TestDataEntry extraEntry = reader.getEntriesRead().get(0);
        assertNull(extraEntry.getParent(), "Extra entry should have null parent when main entry has null parent");
    }

    /**
     * Test read method propagates IOException from reader.
     */
    @Test
    public void testReadPropagatesIOException() {
        // Arrange
        FailingDataEntryReader failingReader = new FailingDataEntryReader();
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader("extra.txt", failingReader);

        TestDataEntry mainEntry = new TestDataEntry("main.txt", null);

        // Act & Assert
        assertThrows(IOException.class, () -> extraReader.read(mainEntry),
            "Should propagate IOException from underlying reader");
    }

    /**
     * Test read method with empty extra entry name map.
     */
    @Test
    public void testReadWithEmptyNameMap() throws IOException {
        // Arrange
        ExtraDataEntryNameMap nameMap = new ExtraDataEntryNameMap();
        TestDataEntryReader reader = new TestDataEntryReader();
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader(nameMap, reader);

        TestDataEntry mainEntry = new TestDataEntry("main.txt", null);

        // Act
        extraReader.read(mainEntry);

        // Assert
        assertEquals(1, reader.getEntriesRead().size(), "Should read only main entry");
        assertEquals("main.txt", reader.getEntriesRead().get(0).getName());
    }

    /**
     * Test read method with multiple default extra entries.
     */
    @Test
    public void testReadWithMultipleDefaultExtraEntries() throws IOException {
        // Arrange
        ExtraDataEntryNameMap nameMap = new ExtraDataEntryNameMap();
        nameMap.addExtraDataEntry("default1.txt");
        nameMap.addExtraDataEntry("default2.txt");
        nameMap.addExtraDataEntry("default3.txt");

        TestDataEntryReader reader = new TestDataEntryReader();
        ExtraDataEntryReader extraReader = new ExtraDataEntryReader(nameMap, reader);

        TestDataEntry mainEntry = new TestDataEntry("main.txt", null);

        // Act
        extraReader.read(mainEntry);

        // Assert
        assertTrue(reader.getEntriesRead().size() >= 4,
            "Should read at least 4 entries (3 defaults + main)");

        List<String> readNames = new ArrayList<>();
        for (TestDataEntry entry : reader.getEntriesRead()) {
            readNames.add(entry.getName());
        }

        assertTrue(readNames.contains("default1.txt"), "Should read default1.txt");
        assertTrue(readNames.contains("default2.txt"), "Should read default2.txt");
        assertTrue(readNames.contains("default3.txt"), "Should read default3.txt");
        assertTrue(readNames.contains("main.txt"), "Should read main.txt");

        // Main should be last
        assertEquals("main.txt", reader.getEntriesRead().get(reader.getEntriesRead().size() - 1).getName());
    }

    // Helper classes for testing

    /**
     * Test implementation of DataEntry interface.
     */
    private static class TestDataEntry implements proguard.io.DataEntry {
        private final String name;
        private final proguard.io.DataEntry parent;

        public TestDataEntry(String name, proguard.io.DataEntry parent) {
            this.name = name;
            this.parent = parent;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalName() {
            return name;
        }

        @Override
        public long getSize() {
            return 0;
        }

        @Override
        public boolean isDirectory() {
            return false;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(new byte[0]);
        }

        @Override
        public void closeInputStream() throws IOException {
        }

        @Override
        public proguard.io.DataEntry getParent() {
            return parent;
        }
    }

    /**
     * Test implementation of DataEntryReader that tracks read entries.
     */
    private static class TestDataEntryReader implements proguard.io.DataEntryReader {
        private final List<TestDataEntry> entriesRead = new ArrayList<>();

        @Override
        public void read(proguard.io.DataEntry dataEntry) throws IOException {
            // Store a copy for verification
            entriesRead.add(new TestDataEntry(dataEntry.getName(), dataEntry.getParent()));
        }

        public List<TestDataEntry> getEntriesRead() {
            return entriesRead;
        }
    }

    /**
     * Test implementation of DataEntryReader that throws IOException.
     */
    private static class FailingDataEntryReader implements proguard.io.DataEntryReader {
        @Override
        public void read(proguard.io.DataEntry dataEntry) throws IOException {
            throw new IOException("Simulated read failure");
        }
    }
}
