package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ClassObfuscator#newClassName(Clazz)}.
 *
 * This class tests the static newClassName method, which retrieves the new obfuscated
 * name assigned to a class during the obfuscation process. The method accesses the
 * class's processing info and returns it if it's a String, otherwise returns null.
 *
 * The method under test:
 * - Takes a Clazz instance as parameter
 * - Returns the String stored in clazz.getProcessingInfo() if it's a String
 * - Returns null if processing info is null or not a String
 * - This is used to retrieve the obfuscated name that was set by setNewClassName
 */
public class ClassObfuscatorClaude_newClassNameTest {

    // ========== Tests for newClassName - Basic Functionality ==========

    /**
     * Tests that newClassName returns the correct name when a String is stored
     * in processing info.
     */
    @Test
    public void testNewClassName_returnsName_whenProcessingInfoIsString() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String expectedName = "com/example/ObfuscatedClass";

        when(mockClazz.getProcessingInfo()).thenReturn(expectedName);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(expectedName, result, "Should return the String stored in processing info");
        verify(mockClazz, times(1)).getProcessingInfo();
    }

    /**
     * Tests that newClassName returns null when processing info is null.
     */
    @Test
    public void testNewClassName_returnsNull_whenProcessingInfoIsNull() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);

        when(mockClazz.getProcessingInfo()).thenReturn(null);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertNull(result, "Should return null when processing info is null");
        verify(mockClazz, times(1)).getProcessingInfo();
    }

    /**
     * Tests that newClassName returns null when processing info is not a String.
     */
    @Test
    public void testNewClassName_returnsNull_whenProcessingInfoIsNotString() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        Object nonStringObject = new Object();

        when(mockClazz.getProcessingInfo()).thenReturn(nonStringObject);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertNull(result, "Should return null when processing info is not a String");
        verify(mockClazz, times(1)).getProcessingInfo();
    }

    /**
     * Tests that newClassName works with simple class names.
     */
    @Test
    public void testNewClassName_withSimpleClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String simpleName = "A";

        when(mockClazz.getProcessingInfo()).thenReturn(simpleName);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(simpleName, result, "Should return simple class name");
    }

    /**
     * Tests that newClassName works with fully qualified class names.
     */
    @Test
    public void testNewClassName_withFullyQualifiedName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String fullyQualifiedName = "com/example/foo/bar/ObfuscatedClassName";

        when(mockClazz.getProcessingInfo()).thenReturn(fullyQualifiedName);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(fullyQualifiedName, result, "Should return fully qualified name");
    }

    /**
     * Tests that newClassName works with inner class names.
     */
    @Test
    public void testNewClassName_withInnerClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String innerClassName = "com/example/OuterClass$InnerClass";

        when(mockClazz.getProcessingInfo()).thenReturn(innerClassName);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(innerClassName, result, "Should return inner class name");
    }

    /**
     * Tests that newClassName works with numeric inner class names.
     */
    @Test
    public void testNewClassName_withNumericInnerClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String numericInnerClassName = "com/example/OuterClass$1";

        when(mockClazz.getProcessingInfo()).thenReturn(numericInnerClassName);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(numericInnerClassName, result, "Should return numeric inner class name");
    }

    /**
     * Tests that newClassName works with nested inner class names.
     */
    @Test
    public void testNewClassName_withNestedInnerClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nestedInnerClassName = "com/example/OuterClass$MiddleClass$InnerClass";

        when(mockClazz.getProcessingInfo()).thenReturn(nestedInnerClassName);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(nestedInnerClassName, result, "Should return nested inner class name");
    }

    /**
     * Tests that newClassName works with empty string.
     */
    @Test
    public void testNewClassName_withEmptyString() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String emptyName = "";

        when(mockClazz.getProcessingInfo()).thenReturn(emptyName);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(emptyName, result, "Should return empty string");
        assertNotNull(result, "Empty string should not be null");
    }

    /**
     * Tests that newClassName does not throw exception with valid String input.
     */
    @Test
    public void testNewClassName_doesNotThrowException_withString() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String name = "com/example/TestClass";

        when(mockClazz.getProcessingInfo()).thenReturn(name);

        // Act & Assert
        assertDoesNotThrow(() -> ClassObfuscator.newClassName(mockClazz));
    }

    /**
     * Tests that newClassName does not throw exception with null input.
     */
    @Test
    public void testNewClassName_doesNotThrowException_withNull() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);

        when(mockClazz.getProcessingInfo()).thenReturn(null);

        // Act & Assert
        assertDoesNotThrow(() -> ClassObfuscator.newClassName(mockClazz));
    }

    // ========== Tests for Edge Cases ==========

    /**
     * Tests that newClassName returns null when processing info is an Integer.
     */
    @Test
    public void testNewClassName_returnsNull_whenProcessingInfoIsInteger() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        Integer integerValue = 42;

        when(mockClazz.getProcessingInfo()).thenReturn(integerValue);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertNull(result, "Should return null when processing info is Integer");
    }

    /**
     * Tests that newClassName returns null when processing info is a List.
     */
    @Test
    public void testNewClassName_returnsNull_whenProcessingInfoIsList() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        java.util.List<String> listValue = java.util.Arrays.asList("test");

        when(mockClazz.getProcessingInfo()).thenReturn(listValue);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertNull(result, "Should return null when processing info is List");
    }

    /**
     * Tests that newClassName returns null when processing info is a Boolean.
     */
    @Test
    public void testNewClassName_returnsNull_whenProcessingInfoIsBoolean() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        Boolean booleanValue = Boolean.TRUE;

        when(mockClazz.getProcessingInfo()).thenReturn(booleanValue);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertNull(result, "Should return null when processing info is Boolean");
    }

    /**
     * Tests that newClassName can be called multiple times and returns consistent results.
     */
    @Test
    public void testNewClassName_multipleCallsConsistent_withString() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/TestClass";

        when(mockClazz.getProcessingInfo()).thenReturn(className);

        // Act
        String result1 = ClassObfuscator.newClassName(mockClazz);
        String result2 = ClassObfuscator.newClassName(mockClazz);
        String result3 = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(className, result1, "First call should return the class name");
        assertEquals(className, result2, "Second call should return the class name");
        assertEquals(className, result3, "Third call should return the class name");
        verify(mockClazz, times(3)).getProcessingInfo();
    }

    /**
     * Tests that newClassName can be called multiple times and consistently returns null.
     */
    @Test
    public void testNewClassName_multipleCallsConsistent_withNull() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);

        when(mockClazz.getProcessingInfo()).thenReturn(null);

        // Act
        String result1 = ClassObfuscator.newClassName(mockClazz);
        String result2 = ClassObfuscator.newClassName(mockClazz);
        String result3 = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertNull(result1, "First call should return null");
        assertNull(result2, "Second call should return null");
        assertNull(result3, "Third call should return null");
        verify(mockClazz, times(3)).getProcessingInfo();
    }

    /**
     * Tests that newClassName works correctly with different Clazz instances.
     */
    @Test
    public void testNewClassName_withDifferentClazzes() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);

        String name1 = "com/example/Class1";
        String name2 = "com/example/Class2";

        when(clazz1.getProcessingInfo()).thenReturn(name1);
        when(clazz2.getProcessingInfo()).thenReturn(name2);
        when(clazz3.getProcessingInfo()).thenReturn(null);

        // Act
        String result1 = ClassObfuscator.newClassName(clazz1);
        String result2 = ClassObfuscator.newClassName(clazz2);
        String result3 = ClassObfuscator.newClassName(clazz3);

        // Assert
        assertEquals(name1, result1, "Should return name1 for clazz1");
        assertEquals(name2, result2, "Should return name2 for clazz2");
        assertNull(result3, "Should return null for clazz3");
    }

    /**
     * Tests that newClassName works with very long class names.
     */
    @Test
    public void testNewClassName_withLongClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String longName = "com/example/very/long/package/structure/with/many/levels/VeryLongObfuscatedClassName";

        when(mockClazz.getProcessingInfo()).thenReturn(longName);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(longName, result, "Should return long class name");
    }

    /**
     * Tests that newClassName works with single-character obfuscated names.
     */
    @Test
    public void testNewClassName_withSingleCharacter() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String singleChar = "a";

        when(mockClazz.getProcessingInfo()).thenReturn(singleChar);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(singleChar, result, "Should return single character name");
    }

    /**
     * Tests that newClassName works with special characters in names.
     */
    @Test
    public void testNewClassName_withSpecialCharacters() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nameWithSpecialChars = "com/example/_Test$Class_123";

        when(mockClazz.getProcessingInfo()).thenReturn(nameWithSpecialChars);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(nameWithSpecialChars, result, "Should return name with special characters");
    }

    /**
     * Tests that newClassName properly handles unicode characters.
     */
    @Test
    public void testNewClassName_withUnicodeCharacters() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String unicodeName = "com/example/\u4E2D\u6587/Test";

        when(mockClazz.getProcessingInfo()).thenReturn(unicodeName);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(unicodeName, result, "Should return name with unicode characters");
    }

    /**
     * Tests that newClassName works with names containing whitespace.
     */
    @Test
    public void testNewClassName_withWhitespace() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nameWithWhitespace = "com/example/Test Class";

        when(mockClazz.getProcessingInfo()).thenReturn(nameWithWhitespace);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(nameWithWhitespace, result, "Should return name with whitespace");
    }

    /**
     * Tests that newClassName works with names starting with separators.
     */
    @Test
    public void testNewClassName_withLeadingSeparator() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nameWithLeadingSeparator = "/com/example/Test";

        when(mockClazz.getProcessingInfo()).thenReturn(nameWithLeadingSeparator);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(nameWithLeadingSeparator, result, "Should return name with leading separator");
    }

    /**
     * Tests that newClassName works with names ending with separators.
     */
    @Test
    public void testNewClassName_withTrailingSeparator() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nameWithTrailingSeparator = "com/example/Test/";

        when(mockClazz.getProcessingInfo()).thenReturn(nameWithTrailingSeparator);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(nameWithTrailingSeparator, result, "Should return name with trailing separator");
    }

    /**
     * Tests that newClassName works with mixed case names.
     */
    @Test
    public void testNewClassName_withMixedCase() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String mixedCaseName = "CoM/eXaMpLe/TeSt";

        when(mockClazz.getProcessingInfo()).thenReturn(mixedCaseName);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(mixedCaseName, result, "Should return mixed case name");
    }

    /**
     * Tests that newClassName returns the exact same String reference.
     */
    @Test
    public void testNewClassName_returnsSameStringReference() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/TestClass";

        when(mockClazz.getProcessingInfo()).thenReturn(className);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertSame(className, result, "Should return the same String reference");
    }

    // ========== Integration Tests ==========

    /**
     * Tests integration with setNewClassName - after setting a name,
     * newClassName should return that name.
     */
    @Test
    public void testNewClassName_integrationWithSetNewClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String newName = "com/example/ObfuscatedName";

        // Setup mock to capture what's set and return it
        final String[] capturedName = {null};
        doAnswer(invocation -> {
            capturedName[0] = invocation.getArgument(0);
            return null;
        }).when(mockClazz).setProcessingInfo(any());

        when(mockClazz.getProcessingInfo()).thenAnswer(invocation -> capturedName[0]);

        // Act
        ClassObfuscator.setNewClassName(mockClazz, newName);
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(newName, result, "Should return the name that was set");
    }

    /**
     * Tests integration with setNewClassName - before setting a name,
     * newClassName should return null.
     */
    @Test
    public void testNewClassName_integrationWithSetNewClassName_beforeSet() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);

        when(mockClazz.getProcessingInfo()).thenReturn(null);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertNull(result, "Should return null before any name is set");
    }

    /**
     * Tests integration with setNewClassName - after setting multiple names,
     * newClassName should return the most recent one.
     */
    @Test
    public void testNewClassName_integrationWithSetNewClassName_multipleSets() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String name1 = "com/example/FirstName";
        String name2 = "com/example/SecondName";
        String name3 = "com/example/ThirdName";

        // Setup mock to capture what's set and return it
        final String[] capturedName = {null};
        doAnswer(invocation -> {
            capturedName[0] = invocation.getArgument(0);
            return null;
        }).when(mockClazz).setProcessingInfo(any());

        when(mockClazz.getProcessingInfo()).thenAnswer(invocation -> capturedName[0]);

        // Act & Assert
        ClassObfuscator.setNewClassName(mockClazz, name1);
        assertEquals(name1, ClassObfuscator.newClassName(mockClazz), "Should return first name");

        ClassObfuscator.setNewClassName(mockClazz, name2);
        assertEquals(name2, ClassObfuscator.newClassName(mockClazz), "Should return second name");

        ClassObfuscator.setNewClassName(mockClazz, name3);
        assertEquals(name3, ClassObfuscator.newClassName(mockClazz), "Should return third name");
    }

    /**
     * Tests that newClassName returns null when setNewClassName sets null.
     */
    @Test
    public void testNewClassName_integrationWithSetNewClassName_setNull() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);

        // Setup mock to capture what's set and return it
        final Object[] capturedValue = {null};
        doAnswer(invocation -> {
            capturedValue[0] = invocation.getArgument(0);
            return null;
        }).when(mockClazz).setProcessingInfo(any());

        when(mockClazz.getProcessingInfo()).thenAnswer(invocation -> capturedValue[0]);

        // Act
        ClassObfuscator.setNewClassName(mockClazz, null);
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertNull(result, "Should return null when null was set");
    }

    /**
     * Tests that newClassName returns empty string when setNewClassName sets empty string.
     */
    @Test
    public void testNewClassName_integrationWithSetNewClassName_setEmptyString() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String emptyName = "";

        // Setup mock to capture what's set and return it
        final String[] capturedName = {null};
        doAnswer(invocation -> {
            capturedName[0] = invocation.getArgument(0);
            return null;
        }).when(mockClazz).setProcessingInfo(any());

        when(mockClazz.getProcessingInfo()).thenAnswer(invocation -> capturedName[0]);

        // Act
        ClassObfuscator.setNewClassName(mockClazz, emptyName);
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(emptyName, result, "Should return empty string when empty string was set");
        assertNotNull(result, "Empty string should not be null");
    }

    /**
     * Tests integration with hasOriginalClassName - verifying that newClassName
     * is used internally by hasOriginalClassName.
     */
    @Test
    public void testNewClassName_integrationWithHasOriginalClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        String newName = "com/example/a";

        when(mockClazz.getName()).thenReturn(originalName);
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        String retrievedName = ClassObfuscator.newClassName(mockClazz);
        boolean hasOriginalName = ClassObfuscator.hasOriginalClassName(mockClazz);

        // Assert
        assertEquals(newName, retrievedName, "newClassName should return the obfuscated name");
        assertFalse(hasOriginalName, "hasOriginalClassName should return false since names differ");
    }

    /**
     * Tests that newClassName handles the scenario where processing info changes
     * between calls (simulating concurrent modification).
     */
    @Test
    public void testNewClassName_reflectsChangesInProcessingInfo() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String name1 = "com/example/FirstName";
        String name2 = "com/example/SecondName";

        // Setup mock to return different values on subsequent calls
        when(mockClazz.getProcessingInfo())
            .thenReturn(null)
            .thenReturn(name1)
            .thenReturn(name2);

        // Act & Assert
        assertNull(ClassObfuscator.newClassName(mockClazz), "First call should return null");
        assertEquals(name1, ClassObfuscator.newClassName(mockClazz), "Second call should return name1");
        assertEquals(name2, ClassObfuscator.newClassName(mockClazz), "Third call should return name2");
    }

    /**
     * Tests that newClassName works correctly when called on the same class multiple times
     * in different contexts.
     */
    @Test
    public void testNewClassName_sameClassMultipleContexts() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/TestClass";

        when(mockClazz.getProcessingInfo()).thenReturn(className);

        // Act - Simulate multiple retrievals in different contexts
        String context1Result = ClassObfuscator.newClassName(mockClazz);
        String context2Result = ClassObfuscator.newClassName(mockClazz);
        String context3Result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertEquals(className, context1Result, "Context 1 should retrieve the name");
        assertEquals(className, context2Result, "Context 2 should retrieve the name");
        assertEquals(className, context3Result, "Context 3 should retrieve the name");
        assertEquals(context1Result, context2Result, "All contexts should get the same result");
        assertEquals(context2Result, context3Result, "All contexts should get the same result");
    }

    /**
     * Tests that newClassName properly handles the type check for instanceof String.
     */
    @Test
    public void testNewClassName_typeCheckForString() {
        // Arrange - Create different types of objects
        Clazz mockClazz1 = mock(Clazz.class);
        Clazz mockClazz2 = mock(Clazz.class);
        Clazz mockClazz3 = mock(Clazz.class);
        Clazz mockClazz4 = mock(Clazz.class);

        when(mockClazz1.getProcessingInfo()).thenReturn("ValidString");
        when(mockClazz2.getProcessingInfo()).thenReturn(new StringBuilder("NotAString"));
        when(mockClazz3.getProcessingInfo()).thenReturn(new Object());
        when(mockClazz4.getProcessingInfo()).thenReturn(123);

        // Act
        String result1 = ClassObfuscator.newClassName(mockClazz1);
        String result2 = ClassObfuscator.newClassName(mockClazz2);
        String result3 = ClassObfuscator.newClassName(mockClazz3);
        String result4 = ClassObfuscator.newClassName(mockClazz4);

        // Assert
        assertEquals("ValidString", result1, "Should return String");
        assertNull(result2, "Should return null for StringBuilder");
        assertNull(result3, "Should return null for Object");
        assertNull(result4, "Should return null for Integer");
    }

    /**
     * Tests that newClassName works correctly with array as processing info.
     */
    @Test
    public void testNewClassName_returnsNull_whenProcessingInfoIsArray() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String[] arrayValue = {"test"};

        when(mockClazz.getProcessingInfo()).thenReturn(arrayValue);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertNull(result, "Should return null when processing info is an array");
    }

    /**
     * Tests that newClassName works correctly with a Map as processing info.
     */
    @Test
    public void testNewClassName_returnsNull_whenProcessingInfoIsMap() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        java.util.Map<String, String> mapValue = new java.util.HashMap<>();
        mapValue.put("key", "value");

        when(mockClazz.getProcessingInfo()).thenReturn(mapValue);

        // Act
        String result = ClassObfuscator.newClassName(mockClazz);

        // Assert
        assertNull(result, "Should return null when processing info is a Map");
    }
}
