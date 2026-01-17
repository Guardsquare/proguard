package proguard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link ClassPath}.
 * Tests all methods to ensure proper functionality of the ClassPath wrapper.
 */
public class ClassPathClaudeTest {

    private ClassPath classPath;
    private ClassPathEntry inputEntry1;
    private ClassPathEntry inputEntry2;
    private ClassPathEntry outputEntry1;
    private ClassPathEntry outputEntry2;

    @BeforeEach
    public void setUp() {
        classPath = new ClassPath();
        inputEntry1 = new ClassPathEntry(new File("input1.jar"), false);
        inputEntry2 = new ClassPathEntry(new File("input2.jar"), false);
        outputEntry1 = new ClassPathEntry(new File("output1.jar"), true);
        outputEntry2 = new ClassPathEntry(new File("output2.jar"), true);
    }

    /**
     * Tests the no-argument constructor ClassPath().
     * Verifies that a new ClassPath is empty.
     */
    @Test
    public void testConstructor() {
        // Act
        ClassPath newClassPath = new ClassPath();

        // Assert
        assertTrue(newClassPath.isEmpty(), "Newly constructed ClassPath should be empty");
        assertEquals(0, newClassPath.size(), "Newly constructed ClassPath should have size 0");
    }

    /**
     * Tests hasOutput() on an empty ClassPath.
     * Should return false when no entries are present.
     */
    @Test
    public void testHasOutputOnEmptyClassPath() {
        // Assert
        assertFalse(classPath.hasOutput(), "Empty ClassPath should not have output");
    }

    /**
     * Tests hasOutput() when ClassPath contains only input entries.
     * Should return false when no output entries are present.
     */
    @Test
    public void testHasOutputWithOnlyInputEntries() {
        // Arrange
        classPath.add(inputEntry1);
        classPath.add(inputEntry2);

        // Assert
        assertFalse(classPath.hasOutput(), "ClassPath with only input entries should not have output");
    }

    /**
     * Tests hasOutput() when ClassPath contains only output entries.
     * Should return true when output entries are present.
     */
    @Test
    public void testHasOutputWithOnlyOutputEntries() {
        // Arrange
        classPath.add(outputEntry1);

        // Assert
        assertTrue(classPath.hasOutput(), "ClassPath with output entries should have output");
    }

    /**
     * Tests hasOutput() when ClassPath contains mixed input and output entries.
     * Should return true when at least one output entry is present.
     */
    @Test
    public void testHasOutputWithMixedEntries() {
        // Arrange
        classPath.add(inputEntry1);
        classPath.add(outputEntry1);
        classPath.add(inputEntry2);

        // Assert
        assertTrue(classPath.hasOutput(), "ClassPath with mixed entries should have output if any entry is output");
    }

    /**
     * Tests hasOutput() when output entry is at the end.
     * Verifies the method scans through all entries.
     */
    @Test
    public void testHasOutputWithOutputEntryAtEnd() {
        // Arrange
        classPath.add(inputEntry1);
        classPath.add(inputEntry2);
        classPath.add(outputEntry1);

        // Assert
        assertTrue(classPath.hasOutput(), "ClassPath should detect output entry at end");
    }

    /**
     * Tests hasOutput() when output entry is at the beginning.
     * Verifies early return optimization when output is found early.
     */
    @Test
    public void testHasOutputWithOutputEntryAtBeginning() {
        // Arrange
        classPath.add(outputEntry1);
        classPath.add(inputEntry1);
        classPath.add(inputEntry2);

        // Assert
        assertTrue(classPath.hasOutput(), "ClassPath should detect output entry at beginning");
    }

    /**
     * Tests clear() method.
     * Verifies that clear removes all entries from the ClassPath.
     */
    @Test
    public void testClear() {
        // Arrange
        classPath.add(inputEntry1);
        classPath.add(outputEntry1);
        assertEquals(2, classPath.size(), "ClassPath should have 2 entries before clear");

        // Act
        classPath.clear();

        // Assert
        assertTrue(classPath.isEmpty(), "ClassPath should be empty after clear");
        assertEquals(0, classPath.size(), "ClassPath size should be 0 after clear");
        assertFalse(classPath.hasOutput(), "ClassPath should not have output after clear");
    }

    /**
     * Tests clear() on already empty ClassPath.
     * Should be safe to call clear on empty ClassPath.
     */
    @Test
    public void testClearOnEmptyClassPath() {
        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> classPath.clear(), "Clear on empty ClassPath should not throw exception");
        assertTrue(classPath.isEmpty(), "ClassPath should remain empty");
    }

    /**
     * Tests add(int, ClassPathEntry) method.
     * Verifies insertion at specific index.
     */
    @Test
    public void testAddAtIndex() {
        // Arrange
        classPath.add(inputEntry1);
        classPath.add(inputEntry2);

        // Act
        classPath.add(1, outputEntry1);

        // Assert
        assertEquals(3, classPath.size(), "ClassPath should have 3 entries");
        assertSame(inputEntry1, classPath.get(0), "First entry should be inputEntry1");
        assertSame(outputEntry1, classPath.get(1), "Second entry should be outputEntry1");
        assertSame(inputEntry2, classPath.get(2), "Third entry should be inputEntry2");
    }

    /**
     * Tests add(int, ClassPathEntry) at the beginning (index 0).
     */
    @Test
    public void testAddAtIndexZero() {
        // Arrange
        classPath.add(inputEntry1);

        // Act
        classPath.add(0, outputEntry1);

        // Assert
        assertEquals(2, classPath.size(), "ClassPath should have 2 entries");
        assertSame(outputEntry1, classPath.get(0), "First entry should be outputEntry1");
        assertSame(inputEntry1, classPath.get(1), "Second entry should be inputEntry1");
    }

    /**
     * Tests add(int, ClassPathEntry) at the end.
     */
    @Test
    public void testAddAtIndexAtEnd() {
        // Arrange
        classPath.add(inputEntry1);
        classPath.add(inputEntry2);

        // Act
        classPath.add(2, outputEntry1);

        // Assert
        assertEquals(3, classPath.size(), "ClassPath should have 3 entries");
        assertSame(outputEntry1, classPath.get(2), "Last entry should be outputEntry1");
    }

    /**
     * Tests add(ClassPathEntry) method.
     * Verifies that entries are added to the end.
     */
    @Test
    public void testAddEntry() {
        // Act
        boolean result1 = classPath.add(inputEntry1);
        boolean result2 = classPath.add(outputEntry1);

        // Assert
        assertTrue(result1, "add() should return true");
        assertTrue(result2, "add() should return true");
        assertEquals(2, classPath.size(), "ClassPath should have 2 entries");
        assertSame(inputEntry1, classPath.get(0), "First entry should be inputEntry1");
        assertSame(outputEntry1, classPath.get(1), "Second entry should be outputEntry1");
    }

    /**
     * Tests add(ClassPathEntry) with null entry.
     * The underlying ArrayList allows null, so this should work.
     */
    @Test
    public void testAddNullEntry() {
        // Act
        boolean result = classPath.add(null);

        // Assert
        assertTrue(result, "add() should return true even for null");
        assertEquals(1, classPath.size(), "ClassPath should have 1 entry");
        assertNull(classPath.get(0), "Entry should be null");
    }

    /**
     * Tests addAll(ClassPath) method.
     * Verifies that all entries from another ClassPath are added.
     */
    @Test
    public void testAddAll() {
        // Arrange
        classPath.add(inputEntry1);

        ClassPath otherClassPath = new ClassPath();
        otherClassPath.add(inputEntry2);
        otherClassPath.add(outputEntry1);

        // Act
        boolean result = classPath.addAll(otherClassPath);

        // Assert
        assertTrue(result, "addAll() should return true when entries are added");
        assertEquals(3, classPath.size(), "ClassPath should have 3 entries");
        assertSame(inputEntry1, classPath.get(0), "First entry should be inputEntry1");
        assertSame(inputEntry2, classPath.get(1), "Second entry should be inputEntry2");
        assertSame(outputEntry1, classPath.get(2), "Third entry should be outputEntry1");
    }

    /**
     * Tests addAll(ClassPath) with empty ClassPath.
     * Should return false when no entries are added.
     */
    @Test
    public void testAddAllEmptyClassPath() {
        // Arrange
        classPath.add(inputEntry1);
        ClassPath emptyClassPath = new ClassPath();

        // Act
        boolean result = classPath.addAll(emptyClassPath);

        // Assert
        assertFalse(result, "addAll() should return false when adding empty ClassPath");
        assertEquals(1, classPath.size(), "ClassPath size should remain unchanged");
    }

    /**
     * Tests addAll(ClassPath) on empty ClassPath.
     * Verifies adding to empty ClassPath works.
     */
    @Test
    public void testAddAllToEmptyClassPath() {
        // Arrange
        ClassPath otherClassPath = new ClassPath();
        otherClassPath.add(inputEntry1);
        otherClassPath.add(outputEntry1);

        // Act
        boolean result = classPath.addAll(otherClassPath);

        // Assert
        assertTrue(result, "addAll() should return true");
        assertEquals(2, classPath.size(), "ClassPath should have 2 entries");
    }

    /**
     * Tests addAll(ClassPath) doesn't modify the source ClassPath.
     */
    @Test
    public void testAddAllDoesNotModifySource() {
        // Arrange
        ClassPath otherClassPath = new ClassPath();
        otherClassPath.add(inputEntry1);
        otherClassPath.add(outputEntry1);

        // Act
        classPath.addAll(otherClassPath);

        // Assert
        assertEquals(2, otherClassPath.size(), "Source ClassPath should remain unchanged");
        assertSame(inputEntry1, otherClassPath.get(0), "Source ClassPath entries should be unchanged");
    }

    /**
     * Tests get(int) method.
     * Verifies retrieval of entries by index.
     */
    @Test
    public void testGet() {
        // Arrange
        classPath.add(inputEntry1);
        classPath.add(outputEntry1);
        classPath.add(inputEntry2);

        // Act & Assert
        assertSame(inputEntry1, classPath.get(0), "get(0) should return inputEntry1");
        assertSame(outputEntry1, classPath.get(1), "get(1) should return outputEntry1");
        assertSame(inputEntry2, classPath.get(2), "get(2) should return inputEntry2");
    }

    /**
     * Tests get(int) with invalid index.
     * Should throw IndexOutOfBoundsException.
     */
    @Test
    public void testGetWithInvalidIndex() {
        // Arrange
        classPath.add(inputEntry1);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> classPath.get(1),
                "get() with invalid index should throw IndexOutOfBoundsException");
        assertThrows(IndexOutOfBoundsException.class, () -> classPath.get(-1),
                "get() with negative index should throw IndexOutOfBoundsException");
    }

    /**
     * Tests get(int) on empty ClassPath.
     */
    @Test
    public void testGetOnEmptyClassPath() {
        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> classPath.get(0),
                "get() on empty ClassPath should throw IndexOutOfBoundsException");
    }

    /**
     * Tests remove(int) method.
     * Verifies removal of entry at specific index.
     */
    @Test
    public void testRemove() {
        // Arrange
        classPath.add(inputEntry1);
        classPath.add(outputEntry1);
        classPath.add(inputEntry2);

        // Act
        ClassPathEntry removed = classPath.remove(1);

        // Assert
        assertSame(outputEntry1, removed, "remove() should return the removed entry");
        assertEquals(2, classPath.size(), "ClassPath should have 2 entries after removal");
        assertSame(inputEntry1, classPath.get(0), "First entry should still be inputEntry1");
        assertSame(inputEntry2, classPath.get(1), "Second entry should now be inputEntry2");
    }

    /**
     * Tests remove(int) at index 0.
     */
    @Test
    public void testRemoveAtIndexZero() {
        // Arrange
        classPath.add(inputEntry1);
        classPath.add(outputEntry1);

        // Act
        ClassPathEntry removed = classPath.remove(0);

        // Assert
        assertSame(inputEntry1, removed, "remove(0) should return inputEntry1");
        assertEquals(1, classPath.size(), "ClassPath should have 1 entry");
        assertSame(outputEntry1, classPath.get(0), "First entry should now be outputEntry1");
    }

    /**
     * Tests remove(int) at last index.
     */
    @Test
    public void testRemoveAtLastIndex() {
        // Arrange
        classPath.add(inputEntry1);
        classPath.add(outputEntry1);

        // Act
        ClassPathEntry removed = classPath.remove(1);

        // Assert
        assertSame(outputEntry1, removed, "remove(1) should return outputEntry1");
        assertEquals(1, classPath.size(), "ClassPath should have 1 entry");
    }

    /**
     * Tests remove(int) with invalid index.
     * Should throw IndexOutOfBoundsException.
     */
    @Test
    public void testRemoveWithInvalidIndex() {
        // Arrange
        classPath.add(inputEntry1);

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> classPath.remove(1),
                "remove() with invalid index should throw IndexOutOfBoundsException");
        assertThrows(IndexOutOfBoundsException.class, () -> classPath.remove(-1),
                "remove() with negative index should throw IndexOutOfBoundsException");
    }

    /**
     * Tests remove(int) on empty ClassPath.
     */
    @Test
    public void testRemoveOnEmptyClassPath() {
        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> classPath.remove(0),
                "remove() on empty ClassPath should throw IndexOutOfBoundsException");
    }

    /**
     * Tests remove(int) until ClassPath is empty.
     */
    @Test
    public void testRemoveUntilEmpty() {
        // Arrange
        classPath.add(inputEntry1);
        classPath.add(outputEntry1);

        // Act
        classPath.remove(0);
        classPath.remove(0);

        // Assert
        assertTrue(classPath.isEmpty(), "ClassPath should be empty after removing all entries");
        assertEquals(0, classPath.size(), "Size should be 0");
    }

    /**
     * Tests isEmpty() on newly constructed ClassPath.
     */
    @Test
    public void testIsEmptyOnNewClassPath() {
        // Assert
        assertTrue(classPath.isEmpty(), "New ClassPath should be empty");
    }

    /**
     * Tests isEmpty() after adding entries.
     */
    @Test
    public void testIsEmptyAfterAdding() {
        // Act
        classPath.add(inputEntry1);

        // Assert
        assertFalse(classPath.isEmpty(), "ClassPath should not be empty after adding entry");
    }

    /**
     * Tests isEmpty() after clearing.
     */
    @Test
    public void testIsEmptyAfterClear() {
        // Arrange
        classPath.add(inputEntry1);
        classPath.add(outputEntry1);

        // Act
        classPath.clear();

        // Assert
        assertTrue(classPath.isEmpty(), "ClassPath should be empty after clear");
    }

    /**
     * Tests isEmpty() after adding and removing all entries.
     */
    @Test
    public void testIsEmptyAfterRemovingAll() {
        // Arrange
        classPath.add(inputEntry1);

        // Act
        classPath.remove(0);

        // Assert
        assertTrue(classPath.isEmpty(), "ClassPath should be empty after removing all entries");
    }

    /**
     * Tests size() on empty ClassPath.
     */
    @Test
    public void testSizeOnEmptyClassPath() {
        // Assert
        assertEquals(0, classPath.size(), "Empty ClassPath should have size 0");
    }

    /**
     * Tests size() after adding single entry.
     */
    @Test
    public void testSizeAfterAddingSingleEntry() {
        // Act
        classPath.add(inputEntry1);

        // Assert
        assertEquals(1, classPath.size(), "ClassPath should have size 1");
    }

    /**
     * Tests size() after adding multiple entries.
     */
    @Test
    public void testSizeAfterAddingMultipleEntries() {
        // Act
        classPath.add(inputEntry1);
        classPath.add(outputEntry1);
        classPath.add(inputEntry2);
        classPath.add(outputEntry2);

        // Assert
        assertEquals(4, classPath.size(), "ClassPath should have size 4");
    }

    /**
     * Tests size() after removing entry.
     */
    @Test
    public void testSizeAfterRemovingEntry() {
        // Arrange
        classPath.add(inputEntry1);
        classPath.add(outputEntry1);
        classPath.add(inputEntry2);

        // Act
        classPath.remove(1);

        // Assert
        assertEquals(2, classPath.size(), "ClassPath should have size 2 after removal");
    }

    /**
     * Tests size() after clear.
     */
    @Test
    public void testSizeAfterClear() {
        // Arrange
        classPath.add(inputEntry1);
        classPath.add(outputEntry1);

        // Act
        classPath.clear();

        // Assert
        assertEquals(0, classPath.size(), "ClassPath should have size 0 after clear");
    }

    /**
     * Tests size() after addAll.
     */
    @Test
    public void testSizeAfterAddAll() {
        // Arrange
        classPath.add(inputEntry1);

        ClassPath otherClassPath = new ClassPath();
        otherClassPath.add(inputEntry2);
        otherClassPath.add(outputEntry1);

        // Act
        classPath.addAll(otherClassPath);

        // Assert
        assertEquals(3, classPath.size(), "ClassPath should have size 3 after addAll");
    }

    /**
     * Integration test: Complex operations sequence.
     * Tests multiple operations in sequence to verify state consistency.
     */
    @Test
    public void testComplexOperationsSequence() {
        // Add entries
        classPath.add(inputEntry1);
        classPath.add(0, outputEntry1);
        classPath.add(inputEntry2);
        assertEquals(3, classPath.size(), "Should have 3 entries");

        // Verify order
        assertSame(outputEntry1, classPath.get(0));
        assertSame(inputEntry1, classPath.get(1));
        assertSame(inputEntry2, classPath.get(2));

        // Remove middle entry
        ClassPathEntry removed = classPath.remove(1);
        assertSame(inputEntry1, removed);
        assertEquals(2, classPath.size(), "Should have 2 entries");

        // Add another ClassPath
        ClassPath other = new ClassPath();
        other.add(outputEntry2);
        classPath.addAll(other);
        assertEquals(3, classPath.size(), "Should have 3 entries");

        // Verify hasOutput
        assertTrue(classPath.hasOutput(), "Should have output entries");

        // Clear
        classPath.clear();
        assertTrue(classPath.isEmpty(), "Should be empty");
        assertFalse(classPath.hasOutput(), "Should not have output");
    }

    /**
     * Tests that ClassPath operations maintain reference integrity.
     * Verifies that entries are stored by reference, not copied.
     */
    @Test
    public void testReferenceIntegrity() {
        // Arrange
        classPath.add(inputEntry1);

        // Act - modify the original entry
        inputEntry1.setOutput(true);

        // Assert - verify the change is reflected in ClassPath
        assertTrue(classPath.get(0).isOutput(), "Changes to original entry should be reflected in ClassPath");
        assertTrue(classPath.hasOutput(), "ClassPath should now have output");
    }

    /**
     * Tests multiple ClassPath instances are independent.
     */
    @Test
    public void testMultipleClassPathsAreIndependent() {
        // Arrange
        ClassPath classPath1 = new ClassPath();
        ClassPath classPath2 = new ClassPath();

        // Act
        classPath1.add(inputEntry1);
        classPath2.add(outputEntry1);

        // Assert
        assertEquals(1, classPath1.size(), "classPath1 should have 1 entry");
        assertEquals(1, classPath2.size(), "classPath2 should have 1 entry");
        assertFalse(classPath1.hasOutput(), "classPath1 should not have output");
        assertTrue(classPath2.hasOutput(), "classPath2 should have output");
    }
}
