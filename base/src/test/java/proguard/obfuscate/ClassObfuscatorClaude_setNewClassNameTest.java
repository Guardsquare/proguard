package proguard.obfuscate;

import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ClassObfuscator#setNewClassName(Clazz, String)}.
 *
 * This class tests the static setNewClassName method, which assigns a new obfuscated name
 * to a given class. The method stores the new name in the class's processing info, which
 * is later used during the renaming phase of obfuscation.
 *
 * The method under test:
 * - Takes a Clazz instance and a String name as parameters
 * - Stores the name in the Clazz's processing info by calling clazz.setProcessingInfo(name)
 * - Is a simple setter with no complex logic or return value
 */
public class ClassObfuscatorClaude_setNewClassNameTest {

    // ========== Tests for setNewClassName - Basic Functionality ==========

    /**
     * Tests that setNewClassName calls setProcessingInfo on the Clazz with the provided name.
     * This is the primary behavior of the method.
     */
    @Test
    public void testSetNewClassName_callsSetProcessingInfo() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String newName = "com/example/ObfuscatedClass";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, newName);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(newName));
    }

    /**
     * Tests that setNewClassName works with a simple class name (no package).
     */
    @Test
    public void testSetNewClassName_withSimpleClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String simpleName = "A";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, simpleName);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(simpleName));
    }

    /**
     * Tests that setNewClassName works with a fully qualified class name.
     */
    @Test
    public void testSetNewClassName_withFullyQualifiedName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String fullyQualifiedName = "com/example/foo/bar/ObfuscatedClassName";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, fullyQualifiedName);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(fullyQualifiedName));
    }

    /**
     * Tests that setNewClassName works with inner class names (containing $ separator).
     */
    @Test
    public void testSetNewClassName_withInnerClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String innerClassName = "com/example/OuterClass$InnerClass";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, innerClassName);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(innerClassName));
    }

    /**
     * Tests that setNewClassName works with numeric inner class names.
     */
    @Test
    public void testSetNewClassName_withNumericInnerClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String numericInnerClassName = "com/example/OuterClass$1";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, numericInnerClassName);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(numericInnerClassName));
    }

    /**
     * Tests that setNewClassName works with nested inner class names.
     */
    @Test
    public void testSetNewClassName_withNestedInnerClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nestedInnerClassName = "com/example/OuterClass$MiddleClass$InnerClass";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, nestedInnerClassName);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(nestedInnerClassName));
    }

    /**
     * Tests that setNewClassName works with an empty string as the name.
     */
    @Test
    public void testSetNewClassName_withEmptyString() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String emptyName = "";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, emptyName);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(emptyName));
    }

    /**
     * Tests that setNewClassName works with null as the name.
     */
    @Test
    public void testSetNewClassName_withNullName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);

        // Act
        ClassObfuscator.setNewClassName(mockClazz, null);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(isNull());
    }

    /**
     * Tests that setNewClassName does not throw an exception with valid inputs.
     */
    @Test
    public void testSetNewClassName_doesNotThrowException() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String name = "com/example/TestClass";

        // Act & Assert
        assertDoesNotThrow(() -> ClassObfuscator.setNewClassName(mockClazz, name));
    }

    /**
     * Tests that setNewClassName can be called multiple times on the same Clazz.
     * Each call should update the processing info with the new name.
     */
    @Test
    public void testSetNewClassName_calledMultipleTimes() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String name1 = "com/example/FirstName";
        String name2 = "com/example/SecondName";
        String name3 = "com/example/ThirdName";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, name1);
        ClassObfuscator.setNewClassName(mockClazz, name2);
        ClassObfuscator.setNewClassName(mockClazz, name3);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(name1));
        verify(mockClazz, times(1)).setProcessingInfo(eq(name2));
        verify(mockClazz, times(1)).setProcessingInfo(eq(name3));
        verify(mockClazz, times(3)).setProcessingInfo(any());
    }

    /**
     * Tests that setNewClassName works with different Clazz instances.
     */
    @Test
    public void testSetNewClassName_withDifferentClazzes() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);
        String name1 = "com/example/Class1";
        String name2 = "com/example/Class2";
        String name3 = "com/example/Class3";

        // Act
        ClassObfuscator.setNewClassName(clazz1, name1);
        ClassObfuscator.setNewClassName(clazz2, name2);
        ClassObfuscator.setNewClassName(clazz3, name3);

        // Assert
        verify(clazz1, times(1)).setProcessingInfo(eq(name1));
        verify(clazz2, times(1)).setProcessingInfo(eq(name2));
        verify(clazz3, times(1)).setProcessingInfo(eq(name3));
    }

    // ========== Tests for Edge Cases ==========

    /**
     * Tests that setNewClassName works with special characters in the name.
     */
    @Test
    public void testSetNewClassName_withSpecialCharacters() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nameWithSpecialChars = "com/example/_Test$Class_123";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, nameWithSpecialChars);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(nameWithSpecialChars));
    }

    /**
     * Tests that setNewClassName works with very long class names.
     */
    @Test
    public void testSetNewClassName_withLongClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String longName = "com/example/very/long/package/structure/with/many/levels/VeryLongObfuscatedClassName";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, longName);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(longName));
    }

    /**
     * Tests that setNewClassName works with single-character obfuscated names.
     */
    @Test
    public void testSetNewClassName_withSingleCharacter() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String singleChar = "a";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, singleChar);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(singleChar));
    }

    /**
     * Tests that setNewClassName works when called in sequence on multiple classes.
     */
    @Test
    public void testSetNewClassName_sequentialProcessing() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);
        String name1 = "a/b/C1";
        String name2 = "a/b/C2";
        String name3 = "a/b/C3";

        // Act
        ClassObfuscator.setNewClassName(clazz1, name1);
        ClassObfuscator.setNewClassName(clazz2, name2);
        ClassObfuscator.setNewClassName(clazz3, name3);

        // Assert
        verify(clazz1, times(1)).setProcessingInfo(eq(name1));
        verify(clazz2, times(1)).setProcessingInfo(eq(name2));
        verify(clazz3, times(1)).setProcessingInfo(eq(name3));
    }

    /**
     * Tests that setNewClassName passes the exact string without modification.
     */
    @Test
    public void testSetNewClassName_passesExactString() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, originalName);

        // Assert
        // Verify that the exact same string instance/value is passed
        verify(mockClazz, times(1)).setProcessingInfo(same(originalName));
    }

    /**
     * Tests that setNewClassName works with mixed case names.
     */
    @Test
    public void testSetNewClassName_withMixedCase() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String mixedCaseName = "CoM/eXaMpLe/TeSt";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, mixedCaseName);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(mixedCaseName));
    }

    /**
     * Tests that setNewClassName works with package-only names (ending with /).
     */
    @Test
    public void testSetNewClassName_withPackageOnlyName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String packageOnly = "com/example/";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, packageOnly);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(packageOnly));
    }

    /**
     * Tests that setNewClassName works with names containing multiple consecutive separators.
     */
    @Test
    public void testSetNewClassName_withMultipleSeparators() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nameWithMultipleSeparators = "com//example///Test";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, nameWithMultipleSeparators);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(nameWithMultipleSeparators));
    }

    /**
     * Tests that setNewClassName correctly handles the scenario where the same name
     * is set for multiple different classes.
     */
    @Test
    public void testSetNewClassName_sameNameDifferentClasses() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        String sameName = "com/example/SameName";

        // Act
        ClassObfuscator.setNewClassName(clazz1, sameName);
        ClassObfuscator.setNewClassName(clazz2, sameName);

        // Assert
        verify(clazz1, times(1)).setProcessingInfo(eq(sameName));
        verify(clazz2, times(1)).setProcessingInfo(eq(sameName));
    }

    /**
     * Tests that setNewClassName properly integrates with the obfuscation workflow
     * by verifying the name can be stored and later retrieved via newClassName.
     */
    @Test
    public void testSetNewClassName_integrationWithNewClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String newName = "com/example/ObfuscatedName";

        // Setup mock to return the name when getProcessingInfo is called
        when(mockClazz.getProcessingInfo()).thenReturn(newName);

        // Act
        ClassObfuscator.setNewClassName(mockClazz, newName);
        String retrievedName = ClassObfuscator.newClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(newName));
        assertEquals(newName, retrievedName, "Retrieved name should match the set name");
    }

    /**
     * Tests that setNewClassName can overwrite a previously set name.
     */
    @Test
    public void testSetNewClassName_overwritesPreviousName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String firstName = "com/example/FirstName";
        String secondName = "com/example/SecondName";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, firstName);
        ClassObfuscator.setNewClassName(mockClazz, secondName);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(firstName));
        verify(mockClazz, times(1)).setProcessingInfo(eq(secondName));
    }

    /**
     * Tests that setNewClassName works correctly when setting the same name multiple times.
     */
    @Test
    public void testSetNewClassName_idempotent() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String name = "com/example/Test";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, name);
        ClassObfuscator.setNewClassName(mockClazz, name);
        ClassObfuscator.setNewClassName(mockClazz, name);

        // Assert
        verify(mockClazz, times(3)).setProcessingInfo(eq(name));
    }

    /**
     * Tests that setNewClassName properly handles unicode characters in the name.
     */
    @Test
    public void testSetNewClassName_withUnicodeCharacters() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String unicodeName = "com/example/\u4E2D\u6587/Test";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, unicodeName);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(unicodeName));
    }

    /**
     * Tests that setNewClassName works with names that look like file paths.
     */
    @Test
    public void testSetNewClassName_withFilePathLikeName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String pathLikeName = "com/example/path/to/Class";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, pathLikeName);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(pathLikeName));
    }

    /**
     * Tests that setNewClassName works with names starting with a separator.
     */
    @Test
    public void testSetNewClassName_withLeadingSeparator() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nameWithLeadingSeparator = "/com/example/Test";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, nameWithLeadingSeparator);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(nameWithLeadingSeparator));
    }

    /**
     * Tests that setNewClassName works with whitespace in the name.
     */
    @Test
    public void testSetNewClassName_withWhitespace() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nameWithWhitespace = "com/example/Test Class";

        // Act
        ClassObfuscator.setNewClassName(mockClazz, nameWithWhitespace);

        // Assert
        verify(mockClazz, times(1)).setProcessingInfo(eq(nameWithWhitespace));
    }
}
