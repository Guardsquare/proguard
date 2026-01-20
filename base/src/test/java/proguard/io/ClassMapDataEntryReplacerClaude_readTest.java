package proguard.io;

import org.junit.jupiter.api.Test;
import proguard.classfile.*;
import proguard.classfile.editor.ClassBuilder;
import proguard.configuration.InitialStateInfo;
import proguard.util.ProcessingFlags;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ClassMapDataEntryReplacer.read method.
 * Tests the writing of class mapping data to data entries.
 */
public class ClassMapDataEntryReplacerClaude_readTest {

    /**
     * Test that read method writes class pool size correctly.
     */
    @Test
    public void testReadWritesClassPoolSize() throws IOException {
        // Arrange
        ClassPool classPool = new ClassPool();
        ProgramClass testClass = createSimpleClass("TestClass", "java/lang/Object");
        classPool.addClass(testClass);

        InitialStateInfo initialStateInfo = new InitialStateInfo(classPool);
        ByteArrayDataEntryWriter writer = new ByteArrayDataEntryWriter();
        ClassMapDataEntryReplacer replacer = new ClassMapDataEntryReplacer(classPool, initialStateInfo, writer);

        TestDataEntry dataEntry = new TestDataEntry("test.map");

        // Act
        replacer.read(dataEntry);

        // Assert
        byte[] result = writer.getWrittenData();
        assertNotNull(result, "Written data should not be null");
        assertTrue(result.length > 0, "Written data should not be empty");

        // Verify the first integer is the class pool size
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(result));
        int size = dis.readInt();
        assertEquals(1, size, "Class pool size should be 1");
    }

    /**
     * Test that read method writes data for empty class pool.
     */
    @Test
    public void testReadEmptyClassPool() throws IOException {
        // Arrange
        ClassPool classPool = new ClassPool();
        InitialStateInfo initialStateInfo = new InitialStateInfo(classPool);
        ByteArrayDataEntryWriter writer = new ByteArrayDataEntryWriter();
        ClassMapDataEntryReplacer replacer = new ClassMapDataEntryReplacer(classPool, initialStateInfo, writer);

        TestDataEntry dataEntry = new TestDataEntry("test.map");

        // Act
        replacer.read(dataEntry);

        // Assert
        byte[] result = writer.getWrittenData();
        assertNotNull(result, "Written data should not be null");

        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(result));
        int size = dis.readInt();
        assertEquals(0, size, "Class pool size should be 0 for empty pool");
    }

    /**
     * Test that read method writes class data correctly for a simple class.
     */
    @Test
    public void testReadSimpleClass() throws IOException {
        // Arrange
        ClassPool classPool = new ClassPool();
        ProgramClass testClass = createSimpleClass("com/example/Test", "java/lang/Object");
        classPool.addClass(testClass);

        InitialStateInfo initialStateInfo = new InitialStateInfo(classPool);
        ByteArrayDataEntryWriter writer = new ByteArrayDataEntryWriter();
        ClassMapDataEntryReplacer replacer = new ClassMapDataEntryReplacer(classPool, initialStateInfo, writer);

        TestDataEntry dataEntry = new TestDataEntry("test.map");

        // Act
        replacer.read(dataEntry);

        // Assert
        byte[] result = writer.getWrittenData();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(result));

        int size = dis.readInt();
        assertEquals(1, size, "Should have 1 class");

        String originalName = dis.readUTF();
        assertEquals("com.example.Test", originalName, "Original class name should match");

        String obfuscatedName = dis.readUTF();
        assertEquals("com.example.Test", obfuscatedName, "Obfuscated name should match original");

        String superName = dis.readUTF();
        assertEquals("java.lang.Object", superName, "Super class name should match");
    }

    /**
     * Test that read method handles multiple classes.
     */
    @Test
    public void testReadMultipleClasses() throws IOException {
        // Arrange
        ClassPool classPool = new ClassPool();
        classPool.addClass(createSimpleClass("com/example/ClassA", "java/lang/Object"));
        classPool.addClass(createSimpleClass("com/example/ClassB", "java/lang/Object"));
        classPool.addClass(createSimpleClass("com/example/ClassC", "java/lang/Object"));

        InitialStateInfo initialStateInfo = new InitialStateInfo(classPool);
        ByteArrayDataEntryWriter writer = new ByteArrayDataEntryWriter();
        ClassMapDataEntryReplacer replacer = new ClassMapDataEntryReplacer(classPool, initialStateInfo, writer);

        TestDataEntry dataEntry = new TestDataEntry("test.map");

        // Act
        replacer.read(dataEntry);

        // Assert
        byte[] result = writer.getWrittenData();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(result));

        int size = dis.readInt();
        assertEquals(3, size, "Should have 3 classes");
    }

    /**
     * Test that read method handles class with fields.
     */
    @Test
    public void testReadClassWithFields() throws IOException {
        // Arrange
        ClassPool classPool = new ClassPool();
        ProgramClass testClass = createClassWithFields("com/example/WithFields", "java/lang/Object");
        classPool.addClass(testClass);

        InitialStateInfo initialStateInfo = new InitialStateInfo(classPool);
        ByteArrayDataEntryWriter writer = new ByteArrayDataEntryWriter();
        ClassMapDataEntryReplacer replacer = new ClassMapDataEntryReplacer(classPool, initialStateInfo, writer);

        TestDataEntry dataEntry = new TestDataEntry("test.map");

        // Act
        replacer.read(dataEntry);

        // Assert
        byte[] result = writer.getWrittenData();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(result));

        dis.readInt(); // size
        dis.readUTF(); // original name
        dis.readUTF(); // obfuscated name
        dis.readUTF(); // super name
        dis.readShort(); // flags

        short fieldCount = dis.readShort();
        assertEquals(2, fieldCount, "Should have 2 fields");

        // Verify field data exists
        for (int i = 0; i < fieldCount; i++) {
            int hash = dis.readInt();
            assertNotEquals(0, hash, "Field hash should not be 0");
            byte flags = dis.readByte();
            // Flags can be any value, just verify we can read it
        }
    }

    /**
     * Test that read method handles class with methods.
     */
    @Test
    public void testReadClassWithMethods() throws IOException {
        // Arrange
        ClassPool classPool = new ClassPool();
        ProgramClass testClass = createClassWithMethods("com/example/WithMethods", "java/lang/Object");
        classPool.addClass(testClass);

        InitialStateInfo initialStateInfo = new InitialStateInfo(classPool);
        ByteArrayDataEntryWriter writer = new ByteArrayDataEntryWriter();
        ClassMapDataEntryReplacer replacer = new ClassMapDataEntryReplacer(classPool, initialStateInfo, writer);

        TestDataEntry dataEntry = new TestDataEntry("test.map");

        // Act
        replacer.read(dataEntry);

        // Assert
        byte[] result = writer.getWrittenData();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(result));

        dis.readInt(); // size
        dis.readUTF(); // original name
        dis.readUTF(); // obfuscated name
        dis.readUTF(); // super name
        dis.readShort(); // flags
        dis.readShort(); // field count (0)

        short methodCount = dis.readShort();
        assertEquals(2, methodCount, "Should have 2 methods");

        // Verify method data exists
        for (int i = 0; i < methodCount; i++) {
            int hash = dis.readInt();
            assertNotEquals(0, hash, "Method hash should not be 0");
            byte flags = dis.readByte();
            // Flags can be any value, just verify we can read it
        }
    }

    /**
     * Test that read method properly handles null output stream from writer.
     */
    @Test
    public void testReadWithNullOutputStream() throws IOException {
        // Arrange
        ClassPool classPool = new ClassPool();
        classPool.addClass(createSimpleClass("TestClass", "java/lang/Object"));

        InitialStateInfo initialStateInfo = new InitialStateInfo(classPool);
        NullDataEntryWriter nullWriter = new NullDataEntryWriter();
        ClassMapDataEntryReplacer replacer = new ClassMapDataEntryReplacer(classPool, initialStateInfo, nullWriter);

        TestDataEntry dataEntry = new TestDataEntry("test.map");

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> replacer.read(dataEntry),
            "read should handle null output stream gracefully");
    }

    /**
     * Test that read method closes output stream even when exception occurs.
     */
    @Test
    public void testReadClosesStreamOnException() throws IOException {
        // Arrange
        ClassPool classPool = new ClassPool();
        classPool.addClass(createSimpleClass("TestClass", "java/lang/Object"));

        InitialStateInfo initialStateInfo = new InitialStateInfo(classPool);
        FailingDataEntryWriter failingWriter = new FailingDataEntryWriter();
        ClassMapDataEntryReplacer replacer = new ClassMapDataEntryReplacer(classPool, initialStateInfo, failingWriter);

        TestDataEntry dataEntry = new TestDataEntry("test.map");

        // Act & Assert
        assertThrows(IOException.class, () -> replacer.read(dataEntry),
            "Should throw IOException when writing fails");

        assertTrue(failingWriter.isClosed(), "Output stream should be closed even after exception");
    }

    /**
     * Test that read method handles class with kept flags.
     */
    @Test
    public void testReadClassWithKeptFlags() throws IOException {
        // Arrange
        ClassPool classPool = new ClassPool();
        ProgramClass testClass = createSimpleClass("com/example/Kept", "java/lang/Object");
        testClass.setProcessingFlags(ProcessingFlags.DONT_OBFUSCATE | ProcessingFlags.DONT_SHRINK);
        classPool.addClass(testClass);

        InitialStateInfo initialStateInfo = new InitialStateInfo(classPool);
        ByteArrayDataEntryWriter writer = new ByteArrayDataEntryWriter();
        ClassMapDataEntryReplacer replacer = new ClassMapDataEntryReplacer(classPool, initialStateInfo, writer);

        TestDataEntry dataEntry = new TestDataEntry("test.map");

        // Act
        replacer.read(dataEntry);

        // Assert
        byte[] result = writer.getWrittenData();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(result));

        dis.readInt(); // size
        dis.readUTF(); // original name
        dis.readUTF(); // obfuscated name
        dis.readUTF(); // super name

        short flags = dis.readShort();
        // The flags should indicate the class is kept (bit 0 set)
        assertTrue((flags & 0x01) != 0, "CLASS_KEPT flag should be set");
    }

    /**
     * Test that read method handles class hierarchy correctly.
     */
    @Test
    public void testReadClassHierarchy() throws IOException {
        // Arrange
        ClassPool classPool = new ClassPool();
        ProgramClass baseClass = createSimpleClass("com/example/Base", "java/lang/Object");
        ProgramClass derivedClass = createSimpleClass("com/example/Derived", "com/example/Base");
        classPool.addClass(baseClass);
        classPool.addClass(derivedClass);

        InitialStateInfo initialStateInfo = new InitialStateInfo(classPool);
        ByteArrayDataEntryWriter writer = new ByteArrayDataEntryWriter();
        ClassMapDataEntryReplacer replacer = new ClassMapDataEntryReplacer(classPool, initialStateInfo, writer);

        TestDataEntry dataEntry = new TestDataEntry("test.map");

        // Act
        replacer.read(dataEntry);

        // Assert
        byte[] result = writer.getWrittenData();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(result));

        int size = dis.readInt();
        assertEquals(2, size, "Should have 2 classes");

        // Read both classes and verify super class names
        Set<String> superNames = new HashSet<>();
        for (int i = 0; i < size; i++) {
            dis.readUTF(); // original name
            dis.readUTF(); // obfuscated name
            String superName = dis.readUTF();
            superNames.add(superName);

            short flags = dis.readShort();
            short fieldCount = dis.readShort();
            for (int j = 0; j < fieldCount; j++) {
                dis.readInt(); // hash
                dis.readByte(); // flags
            }
            short methodCount = dis.readShort();
            for (int j = 0; j < methodCount; j++) {
                dis.readInt(); // hash
                dis.readByte(); // flags
            }
        }

        assertTrue(superNames.contains("java.lang.Object"), "Should have Object as super class");
        assertTrue(superNames.contains("com.example.Base"), "Should have Base as super class");
    }

    /**
     * Test that read method writes data even when class has no members.
     */
    @Test
    public void testReadClassWithNoMembers() throws IOException {
        // Arrange
        ClassPool classPool = new ClassPool();
        ProgramClass testClass = createSimpleClass("com/example/Empty", "java/lang/Object");
        classPool.addClass(testClass);

        InitialStateInfo initialStateInfo = new InitialStateInfo(classPool);
        ByteArrayDataEntryWriter writer = new ByteArrayDataEntryWriter();
        ClassMapDataEntryReplacer replacer = new ClassMapDataEntryReplacer(classPool, initialStateInfo, writer);

        TestDataEntry dataEntry = new TestDataEntry("test.map");

        // Act
        replacer.read(dataEntry);

        // Assert
        byte[] result = writer.getWrittenData();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(result));

        dis.readInt(); // size
        dis.readUTF(); // original name
        dis.readUTF(); // obfuscated name
        dis.readUTF(); // super name
        dis.readShort(); // flags

        short fieldCount = dis.readShort();
        assertEquals(0, fieldCount, "Should have 0 fields");

        short methodCount = dis.readShort();
        assertEquals(0, methodCount, "Should have 0 methods");
    }

    /**
     * Test that read method handles shrunk classes (present in initial state but removed from pool).
     */
    @Test
    public void testReadShrunkClass() throws IOException {
        // Arrange
        ClassPool initialClassPool = new ClassPool();
        ProgramClass testClass = createClassWithFields("com/example/Shrunk", "java/lang/Object");
        initialClassPool.addClass(testClass);

        // Create initial state with the class
        InitialStateInfo initialStateInfo = new InitialStateInfo(initialClassPool);

        // Create a new empty class pool (simulating the class being shrunk)
        ClassPool currentClassPool = new ClassPool();

        ByteArrayDataEntryWriter writer = new ByteArrayDataEntryWriter();
        ClassMapDataEntryReplacer replacer = new ClassMapDataEntryReplacer(currentClassPool, initialStateInfo, writer);

        TestDataEntry dataEntry = new TestDataEntry("test.map");

        // Act
        replacer.read(dataEntry);

        // Assert
        byte[] result = writer.getWrittenData();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(result));

        int size = dis.readInt();
        assertEquals(1, size, "Should have 1 class in initial state");

        String originalName = dis.readUTF();
        assertEquals("com.example.Shrunk", originalName, "Original class name should match");

        String obfuscatedName = dis.readUTF();
        assertEquals("com.example.Shrunk", obfuscatedName, "Obfuscated name should be original (class was shrunk)");

        String superName = dis.readUTF();
        assertEquals("java.lang.Object", superName, "Super class name should match");

        short flags = dis.readShort();
        // The CLASS_SHRUNK flag should be set (bit 7)
        assertTrue((flags & 0x80) != 0, "CLASS_SHRUNK flag should be set");

        short fieldCount = dis.readShort();
        assertEquals(2, fieldCount, "Should have 2 fields from initial state");

        // Verify field data exists with MEMBER_SHRUNK flags
        for (int i = 0; i < fieldCount; i++) {
            int hash = dis.readInt();
            assertNotEquals(0, hash, "Field hash should not be 0");
            byte memberFlags = dis.readByte();
            // The MEMBER_SHRUNK flag should be set (bit 1)
            assertTrue((memberFlags & 0x02) != 0, "MEMBER_SHRUNK flag should be set");
        }
    }

    // Helper methods to create test classes

    private ProgramClass createSimpleClass(String name, String superName) {
        return new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            name,
            superName
        ).getProgramClass();
    }

    private ProgramClass createClassWithFields(String name, String superName) {
        return new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            name,
            superName
        )
        .addField(AccessConstants.PUBLIC, "field1", "I")
        .addField(AccessConstants.PRIVATE, "field2", "Ljava/lang/String;")
        .getProgramClass();
    }

    private ProgramClass createClassWithMethods(String name, String superName) {
        return new ClassBuilder(
            VersionConstants.CLASS_VERSION_1_8,
            AccessConstants.PUBLIC,
            name,
            superName
        )
        .addMethod(AccessConstants.PUBLIC, "method1", "()V", 50, code -> code.return_())
        .addMethod(AccessConstants.PRIVATE, "method2", "(Ljava/lang/String;)I", 50, code -> code.iconst_0().ireturn())
        .getProgramClass();
    }

    // Helper classes for testing

    private static class TestDataEntry implements proguard.io.DataEntry {
        private final String name;

        public TestDataEntry(String name) {
            this.name = name;
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
            return null;
        }
    }

    private static class ByteArrayDataEntryWriter implements proguard.io.DataEntryWriter {
        private ByteArrayOutputStream baos;
        private boolean closed = false;

        @Override
        public boolean createDirectory(proguard.io.DataEntry dataEntry) throws IOException {
            return true;
        }

        @Override
        public boolean sameOutputStream(proguard.io.DataEntry dataEntry1, proguard.io.DataEntry dataEntry2) throws IOException {
            return false;
        }

        @Override
        public OutputStream createOutputStream(proguard.io.DataEntry dataEntry) throws IOException {
            baos = new ByteArrayOutputStream();
            closed = false;
            return baos;
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        @Override
        public void println(java.io.PrintWriter pw, String prefix) {
        }

        public byte[] getWrittenData() {
            return baos != null ? baos.toByteArray() : new byte[0];
        }

        public boolean isClosed() {
            return closed;
        }
    }

    private static class NullDataEntryWriter implements proguard.io.DataEntryWriter {
        @Override
        public boolean createDirectory(proguard.io.DataEntry dataEntry) throws IOException {
            return false;
        }

        @Override
        public boolean sameOutputStream(proguard.io.DataEntry dataEntry1, proguard.io.DataEntry dataEntry2) throws IOException {
            return false;
        }

        @Override
        public OutputStream createOutputStream(proguard.io.DataEntry dataEntry) throws IOException {
            return null;
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public void println(java.io.PrintWriter pw, String prefix) {
        }
    }

    private static class FailingDataEntryWriter implements proguard.io.DataEntryWriter {
        private boolean closed = false;

        @Override
        public boolean createDirectory(proguard.io.DataEntry dataEntry) throws IOException {
            return true;
        }

        @Override
        public boolean sameOutputStream(proguard.io.DataEntry dataEntry1, proguard.io.DataEntry dataEntry2) throws IOException {
            return false;
        }

        @Override
        public OutputStream createOutputStream(proguard.io.DataEntry dataEntry) throws IOException {
            return new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    throw new IOException("Simulated write failure");
                }

                @Override
                public void close() throws IOException {
                    closed = true;
                }
            };
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        @Override
        public void println(java.io.PrintWriter pw, String prefix) {
        }

        public boolean isClosed() {
            return closed;
        }
    }
}
