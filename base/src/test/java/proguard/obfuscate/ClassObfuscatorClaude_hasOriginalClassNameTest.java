package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ClassObfuscator#hasOriginalClassName(Clazz)}.
 *
 * This class tests the static hasOriginalClassName method, which determines whether
 * a class has kept its original name during obfuscation. The method returns true if
 * the class's original name (from clazz.getName()) equals its new name (stored in
 * processing info), indicating the class name was not changed.
 *
 * The method under test:
 * - Takes a Clazz instance as parameter
 * - Returns true if clazz.getName().equals(newClassName(clazz))
 * - Returns false if the names differ or if no new name is set (newClassName returns null)
 * - This is used to check if a class was actually renamed during obfuscation
 */
public class ClassObfuscatorClaude_hasOriginalClassNameTest {

    // ========== Tests for hasOriginalClassName - Basic Functionality ==========

    /**
     * Tests that hasOriginalClassName returns true when the original name
     * equals the new name.
     */
    @Test
    public void testHasOriginalClassName_returnsTrue_whenNamesMatch() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/TestClass";

        when(mockClazz.getName()).thenReturn(className);
        when(mockClazz.getProcessingInfo()).thenReturn(className);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertTrue(result, "Should return true when original and new names match");
        verify(mockClazz, atLeastOnce()).getName();
        verify(mockClazz, atLeastOnce()).getProcessingInfo();
    }

    /**
     * Tests that hasOriginalClassName returns false when the original name
     * differs from the new name (class was renamed).
     */
    @Test
    public void testHasOriginalClassName_returnsFalse_whenNamesDiffer() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/example/a";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should return false when original and new names differ");
        verify(mockClazz, atLeastOnce()).getName();
        verify(mockClazz, atLeastOnce()).getProcessingInfo();
    }

    /**
     * Tests that hasOriginalClassName returns false when no new name is set
     * (processing info is null).
     */
    @Test
    public void testHasOriginalClassName_returnsFalse_whenNoNewNameSet() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(null);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should return false when no new name is set (null processing info)");
        verify(mockClazz, atLeastOnce()).getName();
        verify(mockClazz, atLeastOnce()).getProcessingInfo();
    }

    /**
     * Tests that hasOriginalClassName returns false when processing info
     * is a non-String object.
     */
    @Test
    public void testHasOriginalClassName_returnsFalse_whenProcessingInfoIsNotString() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        Object nonStringProcessingInfo = new Object();

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(nonStringProcessingInfo);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should return false when processing info is not a String");
        verify(mockClazz, atLeastOnce()).getName();
        verify(mockClazz, atLeastOnce()).getProcessingInfo();
    }

    /**
     * Tests that hasOriginalClassName works correctly with simple class names.
     */
    @Test
    public void testHasOriginalClassName_withSimpleClassName_matching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String simpleName = "TestClass";

        when(mockClazz.getName()).thenReturn(simpleName);
        when(mockClazz.getProcessingInfo()).thenReturn(simpleName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertTrue(result, "Should return true for matching simple class names");
    }

    /**
     * Tests that hasOriginalClassName works correctly with fully qualified names.
     */
    @Test
    public void testHasOriginalClassName_withFullyQualifiedName_matching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String fullyQualifiedName = "com/example/foo/bar/TestClass";

        when(mockClazz.getName()).thenReturn(fullyQualifiedName);
        when(mockClazz.getProcessingInfo()).thenReturn(fullyQualifiedName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertTrue(result, "Should return true for matching fully qualified names");
    }

    /**
     * Tests that hasOriginalClassName works correctly with inner class names.
     */
    @Test
    public void testHasOriginalClassName_withInnerClassName_matching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String innerClassName = "com/example/OuterClass$InnerClass";

        when(mockClazz.getName()).thenReturn(innerClassName);
        when(mockClazz.getProcessingInfo()).thenReturn(innerClassName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertTrue(result, "Should return true for matching inner class names");
    }

    /**
     * Tests that hasOriginalClassName properly detects name changes for inner classes.
     */
    @Test
    public void testHasOriginalClassName_withInnerClassName_notMatching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/OuterClass$InnerClass";
        String newName = "com/example/a$b";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should return false when inner class names differ");
    }

    // ========== Tests for Edge Cases ==========

    /**
     * Tests that hasOriginalClassName handles empty string class names.
     */
    @Test
    public void testHasOriginalClassName_withEmptyString_matching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String emptyName = "";

        when(mockClazz.getName()).thenReturn(emptyName);
        when(mockClazz.getProcessingInfo()).thenReturn(emptyName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertTrue(result, "Should return true when both names are empty strings");
    }

    /**
     * Tests that hasOriginalClassName returns false when original is empty
     * but new name is not.
     */
    @Test
    public void testHasOriginalClassName_withEmptyOriginal_nonEmptyNew() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "";
        String newName = "a";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should return false when empty original differs from non-empty new");
    }

    /**
     * Tests that hasOriginalClassName is case-sensitive.
     */
    @Test
    public void testHasOriginalClassName_caseSensitive() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/example/testclass";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should be case-sensitive and return false for different cases");
    }

    /**
     * Tests that hasOriginalClassName works with single character names.
     */
    @Test
    public void testHasOriginalClassName_withSingleCharacter_matching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String singleChar = "a";

        when(mockClazz.getName()).thenReturn(singleChar);
        when(mockClazz.getProcessingInfo()).thenReturn(singleChar);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertTrue(result, "Should return true for matching single character names");
    }

    /**
     * Tests that hasOriginalClassName works with single character names that differ.
     */
    @Test
    public void testHasOriginalClassName_withSingleCharacter_notMatching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "a";
        String newName = "b";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should return false for different single character names");
    }

    /**
     * Tests that hasOriginalClassName can be called multiple times with consistent results.
     */
    @Test
    public void testHasOriginalClassName_multipleCallsConsistent_matching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/TestClass";

        when(mockClazz.getName()).thenReturn(className);
        when(mockClazz.getProcessingInfo()).thenReturn(className);

        // Act
        boolean result1 = ClassObfuscator.hasOriginalClassName(mockClazz);
        boolean result2 = ClassObfuscator.hasOriginalClassName(mockClazz);
        boolean result3 = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
    }

    /**
     * Tests that hasOriginalClassName can be called multiple times with consistent results
     * when names don't match.
     */
    @Test
    public void testHasOriginalClassName_multipleCallsConsistent_notMatching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/example/a";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        boolean result1 = ClassObfuscator.hasOriginalClassName(mockClazz);
        boolean result2 = ClassObfuscator.hasOriginalClassName(mockClazz);
        boolean result3 = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result1, "First call should return false");
        assertFalse(result2, "Second call should return false");
        assertFalse(result3, "Third call should return false");
    }

    /**
     * Tests that hasOriginalClassName works correctly with different Clazz instances.
     */
    @Test
    public void testHasOriginalClassName_withDifferentClazzes() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        // clazz1: original name preserved
        when(clazz1.getName()).thenReturn("com/example/Class1");
        when(clazz1.getProcessingInfo()).thenReturn("com/example/Class1");

        // clazz2: name changed
        when(clazz2.getName()).thenReturn("com/example/Class2");
        when(clazz2.getProcessingInfo()).thenReturn("com/example/a");

        // clazz3: no new name set
        when(clazz3.getName()).thenReturn("com/example/Class3");
        when(clazz3.getProcessingInfo()).thenReturn(null);

        // Act
        boolean result1 = ClassObfuscator.hasOriginalClassName(clazz1);
        boolean result2 = ClassObfuscator.hasOriginalClassName(clazz2);
        boolean result3 = ClassObfuscator.hasOriginalClassName(clazz3);

        // Assert
        assertTrue(result1, "Class1 should have original name");
        assertFalse(result2, "Class2 should not have original name");
        assertFalse(result3, "Class3 should not have original name (no new name set)");
    }

    /**
     * Tests that hasOriginalClassName works with very long class names.
     */
    @Test
    public void testHasOriginalClassName_withLongClassName_matching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String longName = "com/example/very/long/package/structure/with/many/levels/VeryLongClassName";

        when(mockClazz.getName()).thenReturn(longName);
        when(mockClazz.getProcessingInfo()).thenReturn(longName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertTrue(result, "Should return true for matching long class names");
    }

    /**
     * Tests that hasOriginalClassName correctly detects differences in long names.
     */
    @Test
    public void testHasOriginalClassName_withLongClassName_notMatching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/very/long/package/structure/with/many/levels/VeryLongClassName";
        String newName = "a/b/c";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should return false when long class names differ");
    }

    /**
     * Tests that hasOriginalClassName works with numeric inner class names.
     */
    @Test
    public void testHasOriginalClassName_withNumericInnerClass_matching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String numericInnerClass = "com/example/OuterClass$1";

        when(mockClazz.getName()).thenReturn(numericInnerClass);
        when(mockClazz.getProcessingInfo()).thenReturn(numericInnerClass);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertTrue(result, "Should return true for matching numeric inner class names");
    }

    /**
     * Tests that hasOriginalClassName detects changes in numeric inner class names.
     */
    @Test
    public void testHasOriginalClassName_withNumericInnerClass_notMatching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/OuterClass$1";
        String newName = "com/example/a$1";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should return false when numeric inner class names differ");
    }

    /**
     * Tests that hasOriginalClassName works with nested inner classes.
     */
    @Test
    public void testHasOriginalClassName_withNestedInnerClass_matching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nestedInnerClass = "com/example/Outer$Middle$Inner";

        when(mockClazz.getName()).thenReturn(nestedInnerClass);
        when(mockClazz.getProcessingInfo()).thenReturn(nestedInnerClass);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertTrue(result, "Should return true for matching nested inner class names");
    }

    /**
     * Tests that hasOriginalClassName works with special characters in names.
     */
    @Test
    public void testHasOriginalClassName_withSpecialCharacters_matching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nameWithSpecialChars = "com/example/_Test$Class_123";

        when(mockClazz.getName()).thenReturn(nameWithSpecialChars);
        when(mockClazz.getProcessingInfo()).thenReturn(nameWithSpecialChars);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertTrue(result, "Should return true for matching names with special characters");
    }

    /**
     * Tests that hasOriginalClassName properly handles unicode characters.
     */
    @Test
    public void testHasOriginalClassName_withUnicodeCharacters_matching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String unicodeName = "com/example/\u4E2D\u6587/Test";

        when(mockClazz.getName()).thenReturn(unicodeName);
        when(mockClazz.getProcessingInfo()).thenReturn(unicodeName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertTrue(result, "Should return true for matching unicode names");
    }

    /**
     * Tests that hasOriginalClassName detects differences in unicode names.
     */
    @Test
    public void testHasOriginalClassName_withUnicodeCharacters_notMatching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/\u4E2D\u6587/Test";
        String newName = "com/example/a/Test";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should return false when unicode names differ");
    }

    // ========== Integration Tests ==========

    /**
     * Tests integration with setNewClassName - when a new name is set that matches
     * the original, hasOriginalClassName should return true.
     */
    @Test
    public void testHasOriginalClassName_integrationWithSetNewClassName_matching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/TestClass";

        when(mockClazz.getName()).thenReturn(className);

        // Setup mock to capture what's set and return it
        final String[] capturedName = {null};
        doAnswer(invocation -> {
            capturedName[0] = invocation.getArgument(0);
            return null;
        }).when(mockClazz).setProcessingInfo(any());

        when(mockClazz.getProcessingInfo()).thenAnswer(invocation -> capturedName[0]);

        // Act
        ClassObfuscator.setNewClassName(mockClazz, className);
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertTrue(result, "Should return true when new name matches original");
    }

    /**
     * Tests integration with setNewClassName - when a new name is set that differs
     * from the original, hasOriginalClassName should return false.
     */
    @Test
    public void testHasOriginalClassName_integrationWithSetNewClassName_notMatching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/example/a";

        when(mockClazz.getName()).thenReturn(originalName);

        // Setup mock to capture what's set and return it
        final String[] capturedName = {null};
        doAnswer(invocation -> {
            capturedName[0] = invocation.getArgument(0);
            return null;
        }).when(mockClazz).setProcessingInfo(any());

        when(mockClazz.getProcessingInfo()).thenAnswer(invocation -> capturedName[0]);

        // Act
        ClassObfuscator.setNewClassName(mockClazz, newName);
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should return false when new name differs from original");
    }

    /**
     * Tests that hasOriginalClassName reflects changes when a class name is updated.
     */
    @Test
    public void testHasOriginalClassName_reflectsNameChanges() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/example/a";

        when(mockClazz.getName()).thenReturn(originalName);

        // Setup mock to capture what's set and return it
        final String[] capturedName = {null};
        doAnswer(invocation -> {
            capturedName[0] = invocation.getArgument(0);
            return null;
        }).when(mockClazz).setProcessingInfo(any());

        when(mockClazz.getProcessingInfo()).thenAnswer(invocation -> capturedName[0]);

        // Act & Assert - Initially no new name set
        boolean resultBeforeSet = ClassObfuscator.hasOriginalClassName(mockClazz);
        assertFalse(resultBeforeSet, "Should return false when no new name is set");

        // Set new name to match original
        ClassObfuscator.setNewClassName(mockClazz, originalName);
        boolean resultAfterMatchingSet = ClassObfuscator.hasOriginalClassName(mockClazz);
        assertTrue(resultAfterMatchingSet, "Should return true when new name matches original");

        // Change to different name
        ClassObfuscator.setNewClassName(mockClazz, newName);
        boolean resultAfterDifferentSet = ClassObfuscator.hasOriginalClassName(mockClazz);
        assertFalse(resultAfterDifferentSet, "Should return false when new name differs from original");
    }

    /**
     * Tests that hasOriginalClassName correctly handles the scenario where
     * processing info is set to a non-String value.
     */
    @Test
    public void testHasOriginalClassName_withNonStringProcessingInfo() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        Integer nonStringInfo = 42;

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(nonStringInfo);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should return false when processing info is not a String");
    }

    /**
     * Tests that hasOriginalClassName works correctly when names have trailing/leading spaces.
     */
    @Test
    public void testHasOriginalClassName_withWhitespace_notMatching() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newNameWithSpace = "com/example/TestClass ";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newNameWithSpace);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should return false when names differ by whitespace");
    }

    /**
     * Tests that hasOriginalClassName handles package name changes correctly.
     */
    @Test
    public void testHasOriginalClassName_packageNameChanged() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/obfuscated/TestClass";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should return false when package name changes");
    }

    /**
     * Tests that hasOriginalClassName handles class name changes correctly
     * (package stays the same).
     */
    @Test
    public void testHasOriginalClassName_classNameChanged_packageSame() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/example/a";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        boolean result = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertFalse(result, "Should return false when class name changes");
    }
}
