package proguard.io;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UniqueDataEntryWriter.
 * Tests all methods with comprehensive coverage.
 */
public class UniqueDataEntryWriterClaudeTest {

    /**
     * Test constructor initializes correctly.
     */
    @Test
    public void testConstructor() {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();

        // Act
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);

        // Assert
        assertNotNull(uniqueWriter, "UniqueDataEntryWriter should be created");
    }

    /**
     * Test createDirectory creates directory for first call.
     */
    @Test
    public void testCreateDirectoryFirstCall() throws IOException {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry = new TestDataEntry("dir1/", null);

        // Act
        boolean result = uniqueWriter.createDirectory(entry);

        // Assert
        assertTrue(result, "Should return true for first call");
        assertEquals(1, writer.getDirectoriesCreated().size(), "Should create directory");
        assertEquals("dir1/", writer.getDirectoriesCreated().get(0).getName());
    }

    /**
     * Test createDirectory returns false for duplicate directory.
     */
    @Test
    public void testCreateDirectoryDuplicate() throws IOException {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry = new TestDataEntry("dir1/", null);

        // Act
        boolean result1 = uniqueWriter.createDirectory(entry);
        boolean result2 = uniqueWriter.createDirectory(entry);

        // Assert
        assertTrue(result1, "First call should return true");
        assertFalse(result2, "Second call should return false");
        assertEquals(1, writer.getDirectoriesCreated().size(),
            "Should only create directory once");
    }

    /**
     * Test createDirectory with different directories.
     */
    @Test
    public void testCreateDirectoryMultipleDifferent() throws IOException {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry1 = new TestDataEntry("dir1/", null);
        TestDataEntry entry2 = new TestDataEntry("dir2/", null);

        // Act
        boolean result1 = uniqueWriter.createDirectory(entry1);
        boolean result2 = uniqueWriter.createDirectory(entry2);

        // Assert
        assertTrue(result1, "First directory should be created");
        assertTrue(result2, "Second directory should be created");
        assertEquals(2, writer.getDirectoriesCreated().size(),
            "Should create both directories");
    }

    /**
     * Test createDirectory tracks entry even when underlying writer returns false.
     */
    @Test
    public void testCreateDirectoryTracksEvenWhenUnderlyingReturnsFalse() throws IOException {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        writer.setCreateDirectoryResult(false);
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry = new TestDataEntry("dir1/", null);

        // Act
        boolean result1 = uniqueWriter.createDirectory(entry);
        boolean result2 = uniqueWriter.createDirectory(entry);

        // Assert
        assertFalse(result1, "First call should return false from underlying writer");
        assertFalse(result2, "Second call should return false (not delegated)");
        assertEquals(1, writer.getDirectoriesCreated().size(),
            "Should only delegate once even though underlying returned false");
    }

    /**
     * Test createDirectory propagates IOException.
     */
    @Test
    public void testCreateDirectoryPropagatesIOException() {
        // Arrange
        FailingDataEntryWriter writer = new FailingDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry = new TestDataEntry("dir1/", null);

        // Act & Assert
        assertThrows(IOException.class, () -> uniqueWriter.createDirectory(entry),
            "Should propagate IOException");
    }

    /**
     * Test createDirectory tracks entry even when IOException is thrown.
     */
    @Test
    public void testCreateDirectoryTracksEvenWhenIOExceptionThrown() {
        // Arrange
        FailingDataEntryWriter writer = new FailingDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry = new TestDataEntry("dir1/", null);

        // Act
        try {
            uniqueWriter.createDirectory(entry);
            fail("Should throw IOException");
        } catch (IOException e) {
            // Expected
        }

        // Try again - should return false without delegating
        try {
            boolean result = uniqueWriter.createDirectory(entry);
            assertFalse(result, "Second call should return false without delegating");
        } catch (IOException e) {
            fail("Should not throw IOException on second call");
        }
    }

    /**
     * Test sameOutputStream delegates to underlying writer.
     */
    @Test
    public void testSameOutputStreamDelegates() throws IOException {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        writer.setSameOutputStreamResult(true);
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry1 = new TestDataEntry("file1.txt", null);
        TestDataEntry entry2 = new TestDataEntry("file2.txt", null);

        // Act
        boolean result = uniqueWriter.sameOutputStream(entry1, entry2);

        // Assert
        assertTrue(result, "Should return result from underlying writer");
        assertEquals(1, writer.getSameOutputStreamCalls().size());
        assertEquals("file1.txt", writer.getSameOutputStreamCalls().get(0)[0].getName());
        assertEquals("file2.txt", writer.getSameOutputStreamCalls().get(0)[1].getName());
    }

    /**
     * Test sameOutputStream returns false when underlying writer returns false.
     */
    @Test
    public void testSameOutputStreamReturnsFalse() throws IOException {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        writer.setSameOutputStreamResult(false);
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry1 = new TestDataEntry("file1.txt", null);
        TestDataEntry entry2 = new TestDataEntry("file2.txt", null);

        // Act
        boolean result = uniqueWriter.sameOutputStream(entry1, entry2);

        // Assert
        assertFalse(result, "Should return false from underlying writer");
    }

    /**
     * Test sameOutputStream propagates IOException.
     */
    @Test
    public void testSameOutputStreamPropagatesIOException() {
        // Arrange
        FailingDataEntryWriter writer = new FailingDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry1 = new TestDataEntry("file1.txt", null);
        TestDataEntry entry2 = new TestDataEntry("file2.txt", null);

        // Act & Assert
        assertThrows(IOException.class, () -> uniqueWriter.sameOutputStream(entry1, entry2),
            "Should propagate IOException");
    }

    /**
     * Test createOutputStream creates stream for first call.
     */
    @Test
    public void testCreateOutputStreamFirstCall() throws IOException {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry = new TestDataEntry("file1.txt", null);

        // Act
        OutputStream result = uniqueWriter.createOutputStream(entry);

        // Assert
        assertNotNull(result, "Should return output stream for first call");
        assertEquals(1, writer.getOutputStreamsCreated().size(),
            "Should create output stream");
        assertEquals("file1.txt", writer.getOutputStreamsCreated().get(0).getName());
    }

    /**
     * Test createOutputStream returns null for duplicate entry.
     */
    @Test
    public void testCreateOutputStreamDuplicate() throws IOException {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry = new TestDataEntry("file1.txt", null);

        // Act
        OutputStream result1 = uniqueWriter.createOutputStream(entry);
        OutputStream result2 = uniqueWriter.createOutputStream(entry);

        // Assert
        assertNotNull(result1, "First call should return output stream");
        assertNull(result2, "Second call should return null");
        assertEquals(1, writer.getOutputStreamsCreated().size(),
            "Should only create output stream once");
    }

    /**
     * Test createOutputStream with different entries.
     */
    @Test
    public void testCreateOutputStreamMultipleDifferent() throws IOException {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry1 = new TestDataEntry("file1.txt", null);
        TestDataEntry entry2 = new TestDataEntry("file2.txt", null);

        // Act
        OutputStream result1 = uniqueWriter.createOutputStream(entry1);
        OutputStream result2 = uniqueWriter.createOutputStream(entry2);

        // Assert
        assertNotNull(result1, "First stream should be created");
        assertNotNull(result2, "Second stream should be created");
        assertEquals(2, writer.getOutputStreamsCreated().size(),
            "Should create both streams");
    }

    /**
     * Test createOutputStream tracks entry even when underlying writer returns null.
     */
    @Test
    public void testCreateOutputStreamTracksEvenWhenUnderlyingReturnsNull() throws IOException {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        writer.setCreateOutputStreamResult(null);
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry = new TestDataEntry("file1.txt", null);

        // Act
        OutputStream result1 = uniqueWriter.createOutputStream(entry);
        OutputStream result2 = uniqueWriter.createOutputStream(entry);

        // Assert
        assertNull(result1, "First call should return null from underlying writer");
        assertNull(result2, "Second call should return null (not delegated)");
        assertEquals(1, writer.getOutputStreamsCreated().size(),
            "Should only delegate once even though underlying returned null");
    }

    /**
     * Test createOutputStream propagates IOException.
     */
    @Test
    public void testCreateOutputStreamPropagatesIOException() {
        // Arrange
        FailingDataEntryWriter writer = new FailingDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry = new TestDataEntry("file1.txt", null);

        // Act & Assert
        assertThrows(IOException.class, () -> uniqueWriter.createOutputStream(entry),
            "Should propagate IOException");
    }

    /**
     * Test createOutputStream tracks entry even when IOException is thrown.
     */
    @Test
    public void testCreateOutputStreamTracksEvenWhenIOExceptionThrown() {
        // Arrange
        FailingDataEntryWriter writer = new FailingDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry = new TestDataEntry("file1.txt", null);

        // Act
        try {
            uniqueWriter.createOutputStream(entry);
            fail("Should throw IOException");
        } catch (IOException e) {
            // Expected
        }

        // Try again - should return null without delegating
        try {
            OutputStream result = uniqueWriter.createOutputStream(entry);
            assertNull(result, "Second call should return null without delegating");
        } catch (IOException e) {
            fail("Should not throw IOException on second call");
        }
    }

    /**
     * Test createDirectory and createOutputStream track the same entry.
     */
    @Test
    public void testCreateDirectoryAndCreateOutputStreamShareTracking() throws IOException {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry = new TestDataEntry("item", null);

        // Act
        boolean dirResult = uniqueWriter.createDirectory(entry);
        OutputStream streamResult = uniqueWriter.createOutputStream(entry);

        // Assert
        assertTrue(dirResult, "First call (createDirectory) should succeed");
        assertNull(streamResult, "Second call (createOutputStream) should return null");
        assertEquals(1, writer.getDirectoriesCreated().size(),
            "Should only create directory");
        assertEquals(0, writer.getOutputStreamsCreated().size(),
            "Should not create output stream for duplicate");
    }

    /**
     * Test createOutputStream and createDirectory track the same entry.
     */
    @Test
    public void testCreateOutputStreamAndCreateDirectoryShareTracking() throws IOException {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        TestDataEntry entry = new TestDataEntry("item", null);

        // Act
        OutputStream streamResult = uniqueWriter.createOutputStream(entry);
        boolean dirResult = uniqueWriter.createDirectory(entry);

        // Assert
        assertNotNull(streamResult, "First call (createOutputStream) should succeed");
        assertFalse(dirResult, "Second call (createDirectory) should return false");
        assertEquals(1, writer.getOutputStreamsCreated().size(),
            "Should only create output stream");
        assertEquals(0, writer.getDirectoriesCreated().size(),
            "Should not create directory for duplicate");
    }

    /**
     * Test close delegates to underlying writer.
     */
    @Test
    public void testClose() throws IOException {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);

        // Act
        uniqueWriter.close();

        // Assert
        assertTrue(writer.isClosed(), "Should close underlying writer");
    }

    /**
     * Test close propagates IOException.
     */
    @Test
    public void testClosePropagatesIOException() {
        // Arrange
        FailingDataEntryWriter writer = new FailingDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);

        // Act & Assert
        assertThrows(IOException.class, () -> uniqueWriter.close(),
            "Should propagate IOException");
    }

    /**
     * Test println outputs correct format.
     */
    @Test
    public void testPrintln() {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        // Act
        uniqueWriter.println(printWriter, "  ");
        printWriter.flush();

        // Assert
        String output = stringWriter.toString();
        assertTrue(output.contains("  UniqueDataEntryWriter"),
            "Should print with correct prefix");
        assertTrue(output.contains("    TestDataEntryWriter"),
            "Should delegate with increased indentation");
    }

    /**
     * Test println with empty prefix.
     */
    @Test
    public void testPrintlnEmptyPrefix() {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        // Act
        uniqueWriter.println(printWriter, "");
        printWriter.flush();

        // Assert
        String output = stringWriter.toString();
        assertTrue(output.contains("UniqueDataEntryWriter"),
            "Should print class name");
        assertTrue(output.contains("  TestDataEntryWriter"),
            "Should delegate with two-space indentation");
    }

    /**
     * Test println with null prefix handles gracefully.
     */
    @Test
    public void testPrintlnNullPrefix() {
        // Arrange
        TestDataEntryWriter writer = new TestDataEntryWriter();
        UniqueDataEntryWriter uniqueWriter = new UniqueDataEntryWriter(writer);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        // Act
        uniqueWriter.println(printWriter, null);
        printWriter.flush();

        // Assert
        String output = stringWriter.toString();
        assertTrue(output.contains("UniqueDataEntryWriter"),
            "Should print class name");
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
            return name.endsWith("/");
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
     * Test implementation of DataEntryWriter that tracks operations.
     */
    private static class TestDataEntryWriter implements proguard.io.DataEntryWriter {
        private final List<TestDataEntry> directoriesCreated = new ArrayList<>();
        private final List<TestDataEntry> outputStreamsCreated = new ArrayList<>();
        private final List<proguard.io.DataEntry[]> sameOutputStreamCalls = new ArrayList<>();
        private boolean closed = false;
        private boolean createDirectoryResult = true;
        private Boolean sameOutputStreamResult = false;
        private OutputStream createOutputStreamResult = new ByteArrayOutputStream();

        @Override
        public boolean createDirectory(proguard.io.DataEntry dataEntry) throws IOException {
            directoriesCreated.add(new TestDataEntry(dataEntry.getName(), dataEntry.getParent()));
            return createDirectoryResult;
        }

        @Override
        public boolean sameOutputStream(proguard.io.DataEntry dataEntry1,
                                       proguard.io.DataEntry dataEntry2) throws IOException {
            sameOutputStreamCalls.add(new proguard.io.DataEntry[]{dataEntry1, dataEntry2});
            return sameOutputStreamResult;
        }

        @Override
        public OutputStream createOutputStream(proguard.io.DataEntry dataEntry) throws IOException {
            outputStreamsCreated.add(new TestDataEntry(dataEntry.getName(), dataEntry.getParent()));
            return createOutputStreamResult;
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        @Override
        public void println(PrintWriter pw, String prefix) {
            pw.println(prefix + "TestDataEntryWriter");
        }

        public List<TestDataEntry> getDirectoriesCreated() {
            return directoriesCreated;
        }

        public List<TestDataEntry> getOutputStreamsCreated() {
            return outputStreamsCreated;
        }

        public List<proguard.io.DataEntry[]> getSameOutputStreamCalls() {
            return sameOutputStreamCalls;
        }

        public boolean isClosed() {
            return closed;
        }

        public void setCreateDirectoryResult(boolean result) {
            this.createDirectoryResult = result;
        }

        public void setSameOutputStreamResult(boolean result) {
            this.sameOutputStreamResult = result;
        }

        public void setCreateOutputStreamResult(OutputStream result) {
            this.createOutputStreamResult = result;
        }
    }

    /**
     * Test implementation of DataEntryWriter that throws IOException.
     */
    private static class FailingDataEntryWriter implements proguard.io.DataEntryWriter {
        @Override
        public boolean createDirectory(proguard.io.DataEntry dataEntry) throws IOException {
            throw new IOException("Simulated createDirectory failure");
        }

        @Override
        public boolean sameOutputStream(proguard.io.DataEntry dataEntry1,
                                       proguard.io.DataEntry dataEntry2) throws IOException {
            throw new IOException("Simulated sameOutputStream failure");
        }

        @Override
        public OutputStream createOutputStream(proguard.io.DataEntry dataEntry) throws IOException {
            throw new IOException("Simulated createOutputStream failure");
        }

        @Override
        public void close() throws IOException {
            throw new IOException("Simulated close failure");
        }

        @Override
        public void println(PrintWriter pw, String prefix) {
            pw.println(prefix + "FailingDataEntryWriter");
        }
    }
}
