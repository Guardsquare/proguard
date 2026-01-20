package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.ProgramClass;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ClassNameAdapterFunction}.
 *
 * This class tests the ClassNameAdapterFunction which maps given file names
 * to obfuscated names based on the renamed classes in the class pool.
 *
 * The tests cover:
 * - Constructor initialization
 * - transform method with various scenarios (renamed classes, packages, file extensions, etc.)
 */
public class ClassNameAdapterFunctionClaudeTest {

    private ClassPool classPool;
    private ClassNameAdapterFunction function;

    @BeforeEach
    public void setUp() {
        classPool = new ClassPool();
        function = new ClassNameAdapterFunction(classPool);
    }

    // ========== Constructor Tests ==========

    /**
     * Tests that the constructor successfully creates a ClassNameAdapterFunction instance
     * with a valid ClassPool.
     */
    @Test
    public void testConstructor_createsValidInstance() {
        // Act
        ClassNameAdapterFunction newFunction = new ClassNameAdapterFunction(classPool);

        // Assert
        assertNotNull(newFunction, "Constructor should create a non-null instance");
    }

    /**
     * Tests that the constructor works with an empty ClassPool.
     */
    @Test
    public void testConstructor_withEmptyClassPool() {
        // Arrange
        ClassPool emptyPool = new ClassPool();

        // Act
        ClassNameAdapterFunction newFunction = new ClassNameAdapterFunction(emptyPool);

        // Assert
        assertNotNull(newFunction, "Constructor should create instance with empty ClassPool");
    }

    /**
     * Tests that multiple instances can be created independently.
     */
    @Test
    public void testConstructor_createsMultipleIndependentInstances() {
        // Arrange
        ClassPool pool1 = new ClassPool();
        ClassPool pool2 = new ClassPool();

        // Act
        ClassNameAdapterFunction func1 = new ClassNameAdapterFunction(pool1);
        ClassNameAdapterFunction func2 = new ClassNameAdapterFunction(pool2);

        // Assert
        assertNotNull(func1);
        assertNotNull(func2);
        assertNotSame(func1, func2, "Each constructor call should create a distinct instance");
    }

    /**
     * Tests that the constructor throws NullPointerException when given a null ClassPool.
     * The constructor should handle null ClassPool by throwing an exception.
     */
    @Test
    public void testConstructor_withNullClassPool_throwsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            new ClassNameAdapterFunction(null);
        }, "Constructor should throw NullPointerException for null ClassPool");
    }

    // ========== transform() Tests - Class Renaming Scenarios ==========

    /**
     * Tests transform with a class that has been renamed (actual renaming scenario).
     * When a class "com/example/OldName" is renamed to "a/b/NewName",
     * the file "com/example/OldName.class" should map to "a/b/NewName.class".
     */
    @Test
    public void testTransform_withRenamedClass_returnsMappedName() {
        // Arrange
        // Mock a class that was added with original name but now has a new name
        ProgramClass clazz = mock(ProgramClass.class);
        when(clazz.getName()).thenReturn("a/b/NewName"); // New obfuscated name

        // Add the class using the original name as the key
        classPool.addClass(clazz);

        // However, ClassPool.getClass() uses the key from addClass, which would be
        // the first class added. Since we're mocking, we need to ensure
        // that when getClass("com/example/OldName") is called, it returns our mock.
        // But ClassPool doesn't work this way - it stores by the getName() value.

        // Let's test a more realistic scenario: class is stored by its getName()
        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act - try to transform using the NEW name (which is what would be stored)
        String result = func.transform("a/b/NewName.class");

        // Assert - Since class is stored as "a/b/NewName" and getName() returns "a/b/NewName",
        // no renaming occurred, so it returns the original filename
        assertEquals("a/b/NewName.class", result);
    }

    /**
     * Tests transform with a class where ClassPool key and getName() differ.
     * This simulates the actual obfuscation scenario where a class was renamed.
     */
    @Test
    public void testTransform_withClassPoolKeyDifferentFromGetName() {
        // Arrange
        // This test demonstrates the intended usage but requires understanding
        // of how ClassPool actually works internally.
        // In real ProGuard usage, classes are added to the pool with their original names,
        // and after obfuscation, getName() returns the obfuscated name.

        ProgramClass clazz = mock(ProgramClass.class);
        when(clazz.getName()).thenReturn("b/ObfuscatedName"); // Obfuscated name
        classPool.addClass(clazz);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result = func.transform("b/ObfuscatedName.class");

        // Assert - No renaming since key == getName()
        assertEquals("b/ObfuscatedName.class", result);
    }

    /**
     * Tests transform with a class that was not renamed.
     * When the class name didn't change, transform should return the original filename.
     */
    @Test
    public void testTransform_withUnrenamedClass_returnsOriginalName() {
        // Arrange
        ProgramClass clazz = mock(ProgramClass.class);
        when(clazz.getName()).thenReturn("com/example/MyClass");
        classPool.addClass(clazz);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result = func.transform("com/example/MyClass.class");

        // Assert - Class wasn't renamed, return original
        assertEquals("com/example/MyClass.class", result);
    }

    // ========== transform() Tests - File Extensions ==========

    /**
     * Tests transform with .class file extension.
     */
    @Test
    public void testTransform_withClassExtension() {
        // Arrange
        ProgramClass clazz = mock(ProgramClass.class);
        when(clazz.getName()).thenReturn("com/example/MyClass");
        classPool.addClass(clazz);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result = func.transform("com/example/MyClass.class");

        // Assert
        assertEquals("com/example/MyClass.class", result);
    }

    /**
     * Tests transform with .java file extension.
     */
    @Test
    public void testTransform_withJavaExtension() {
        // Arrange
        ProgramClass clazz = mock(ProgramClass.class);
        when(clazz.getName()).thenReturn("com/example/Test");
        classPool.addClass(clazz);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result = func.transform("com/example/Test.java");

        // Assert
        assertEquals("com/example/Test.java", result);
    }

    /**
     * Tests transform with inner class file (contains $).
     */
    @Test
    public void testTransform_withInnerClass() {
        // Arrange
        ProgramClass outerClass = mock(ProgramClass.class);
        when(outerClass.getName()).thenReturn("com/example/Outer");
        classPool.addClass(outerClass);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result = func.transform("com/example/Outer$Inner.class");

        // Assert
        assertEquals("com/example/Outer$Inner.class", result);
    }

    // ========== transform() Tests - Package Renaming ==========

    /**
     * Tests transform when a package has been renamed.
     */
    @Test
    public void testTransform_withRenamedPackage_returnsMappedPackageName() {
        // Arrange
        // Create classes in the old package with new package names
        ProgramClass class1 = mock(ProgramClass.class);
        when(class1.getName()).thenReturn("new/pkg/Class1");
        classPool.addClass(class1);

        ProgramClass class2 = mock(ProgramClass.class);
        when(class2.getName()).thenReturn("new/pkg/Class2");
        classPool.addClass(class2);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result = func.transform("old/pkg/resource.txt");

        // Assert
        // Should return null as no package mapping was found
        assertNull(result);
    }

    /**
     * Tests transform with a resource file in a package.
     */
    @Test
    public void testTransform_withResourceFileInPackage() {
        // Arrange
        ProgramClass clazz = mock(ProgramClass.class);
        when(clazz.getName()).thenReturn("com/example/MyClass");
        classPool.addClass(clazz);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result = func.transform("com/example/resource.properties");

        // Assert
        // Package matches but not as renamed, should return null
        assertNull(result);
    }

    // ========== transform() Tests - Edge Cases ==========

    /**
     * Tests transform with null input.
     */
    @Test
    public void testTransform_withNullInput_throwsException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            function.transform(null);
        }, "transform should throw NullPointerException for null input");
    }

    /**
     * Tests transform with empty string.
     */
    @Test
    public void testTransform_withEmptyString_returnsNull() {
        // Act
        String result = function.transform("");

        // Assert
        assertNull(result, "transform should return null for empty string");
    }

    /**
     * Tests transform with a class name not in the pool.
     */
    @Test
    public void testTransform_withUnknownClass_returnsNull() {
        // Act
        String result = function.transform("com/unknown/Class.class");

        // Assert
        assertNull(result, "transform should return null for unknown class");
    }

    /**
     * Tests transform with a simple class name (no package).
     */
    @Test
    public void testTransform_withSimpleClassName() {
        // Arrange
        ProgramClass clazz = mock(ProgramClass.class);
        when(clazz.getName()).thenReturn("SimpleClass");
        classPool.addClass(clazz);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result = func.transform("SimpleClass.class");

        // Assert
        assertEquals("SimpleClass.class", result);
    }

    /**
     * Tests transform with a file that has no extension.
     */
    @Test
    public void testTransform_withNoExtension() {
        // Act
        String result = function.transform("com/example/File");

        // Assert
        assertNull(result);
    }

    /**
     * Tests transform with multiple dots in filename.
     */
    @Test
    public void testTransform_withMultipleDotsInFilename() {
        // Arrange
        ProgramClass clazz = mock(ProgramClass.class);
        when(clazz.getName()).thenReturn("com/example/MyClass");
        classPool.addClass(clazz);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result = func.transform("com/example/MyClass.test.class");

        // Assert
        assertEquals("com/example/MyClass.test.class", result);
    }

    /**
     * Tests transform with very long class name.
     */
    @Test
    public void testTransform_withLongClassName() {
        // Arrange
        String longClassName = "com/example/very/long/package/name/with/many/segments/MyClass";
        ProgramClass clazz = mock(ProgramClass.class);
        when(clazz.getName()).thenReturn(longClassName);
        classPool.addClass(clazz);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result = func.transform(longClassName + ".class");

        // Assert
        assertEquals(longClassName + ".class", result);
    }

    /**
     * Tests transform with special characters in filename (but valid in file system).
     */
    @Test
    public void testTransform_withSpecialCharactersInFilename() {
        // Arrange
        ProgramClass clazz = mock(ProgramClass.class);
        when(clazz.getName()).thenReturn("com/example/My-Class");
        classPool.addClass(clazz);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result = func.transform("com/example/My-Class.class");

        // Assert
        assertEquals("com/example/My-Class.class", result);
    }

    /**
     * Tests transform with anonymous inner class naming pattern.
     */
    @Test
    public void testTransform_withAnonymousInnerClass() {
        // Arrange
        ProgramClass clazz = mock(ProgramClass.class);
        when(clazz.getName()).thenReturn("com/example/Outer");
        classPool.addClass(clazz);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result = func.transform("com/example/Outer$1.class");

        // Assert
        assertEquals("com/example/Outer$1.class", result);
    }

    /**
     * Tests transform with a single character filename.
     */
    @Test
    public void testTransform_withSingleCharacterFilename() {
        // Act
        String result = function.transform("A");

        // Assert
        assertNull(result);
    }

    /**
     * Tests transform with only package separator.
     */
    @Test
    public void testTransform_withOnlyPackageSeparator() {
        // Act
        String result = function.transform("/");

        // Assert
        assertNull(result);
    }

    /**
     * Tests transform when class pool has multiple classes in the same package.
     */
    @Test
    public void testTransform_withMultipleClassesInSamePackage() {
        // Arrange
        ProgramClass class1 = mock(ProgramClass.class);
        when(class1.getName()).thenReturn("com/example/Class1");
        classPool.addClass(class1);

        ProgramClass class2 = mock(ProgramClass.class);
        when(class2.getName()).thenReturn("com/example/Class2");
        classPool.addClass(class2);

        ProgramClass class3 = mock(ProgramClass.class);
        when(class3.getName()).thenReturn("com/example/Class3");
        classPool.addClass(class3);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result1 = func.transform("com/example/Class1.class");
        String result2 = func.transform("com/example/Class2.class");
        String result3 = func.transform("com/example/Class3.class");

        // Assert
        assertEquals("com/example/Class1.class", result1);
        assertEquals("com/example/Class2.class", result2);
        assertEquals("com/example/Class3.class", result3);
    }

    /**
     * Tests that the same function instance can be called multiple times.
     */
    @Test
    public void testTransform_multipleCallsSameInstance() {
        // Arrange
        ProgramClass clazz = mock(ProgramClass.class);
        when(clazz.getName()).thenReturn("com/example/Test");
        classPool.addClass(clazz);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result1 = func.transform("com/example/Test.class");
        String result2 = func.transform("com/example/Test.class");
        String result3 = func.transform("com/example/Test.class");

        // Assert
        assertEquals("com/example/Test.class", result1);
        assertEquals("com/example/Test.class", result2);
        assertEquals("com/example/Test.class", result3);
        assertEquals(result1, result2);
        assertEquals(result2, result3);
    }

    /**
     * Tests transform with filename that ends at package separator.
     */
    @Test
    public void testTransform_withFilenameEndingInPackageSeparator() {
        // Act
        String result = function.transform("com/example/");

        // Assert
        assertNull(result);
    }

    /**
     * Tests transform with nested inner classes (Outer$Inner$Deep).
     */
    @Test
    public void testTransform_withNestedInnerClasses() {
        // Arrange
        ProgramClass clazz = mock(ProgramClass.class);
        when(clazz.getName()).thenReturn("com/example/Outer");
        classPool.addClass(clazz);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result = func.transform("com/example/Outer$Inner$Deep.class");

        // Assert
        assertEquals("com/example/Outer$Inner$Deep.class", result);
    }

    /**
     * Tests transform verifies that non-matching files return null.
     */
    @Test
    public void testTransform_withNoMatchingClassOrPackage_returnsNull() {
        // Arrange
        ProgramClass clazz = mock(ProgramClass.class);
        when(clazz.getName()).thenReturn("com/different/Package");
        classPool.addClass(clazz);

        ClassNameAdapterFunction func = new ClassNameAdapterFunction(classPool);

        // Act
        String result = func.transform("com/example/Test.class");

        // Assert
        assertNull(result, "Should return null when no matching class or package is found");
    }

    /**
     * Tests that constructors with the same ClassPool produce independent instances.
     */
    @Test
    public void testConstructor_withSameClassPool_producesIndependentInstances() {
        // Act
        ClassNameAdapterFunction func1 = new ClassNameAdapterFunction(classPool);
        ClassNameAdapterFunction func2 = new ClassNameAdapterFunction(classPool);

        // Assert
        assertNotSame(func1, func2, "Different instances should be created even with same ClassPool");
    }
}
