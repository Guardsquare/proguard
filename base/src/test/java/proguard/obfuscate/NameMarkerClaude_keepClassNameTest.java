package proguard.obfuscate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link NameMarker#keepClassName(Clazz)}.
 * Tests the method with signature: (Lproguard/classfile/Clazz;)V
 *
 * The keepClassName method ensures that the name of the given class will be kept (not obfuscated)
 * during the obfuscation process. It does this by calling ClassObfuscator.setNewClassName with the
 * class's current name, effectively marking it to retain its original name.
 *
 * The method:
 * - Takes a Clazz instance as parameter
 * - Gets the current name of the class via clazz.getName()
 * - Calls ClassObfuscator.setNewClassName(clazz, clazz.getName())
 * - This stores the current name in the class's processing info, preventing obfuscation
 */
public class NameMarkerClaude_keepClassNameTest {

    private NameMarker nameMarker;

    @BeforeEach
    public void setUp() {
        nameMarker = new NameMarker();
    }

    // ========== Tests for keepClassName - Basic Functionality ==========

    /**
     * Tests that keepClassName calls getName on the Clazz and then sets the processing info
     * with that same name, effectively preserving the class name.
     */
    @Test
    public void testKeepClassName_callsGetNameAndSetProcessingInfo() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/MyClass";
        when(mockClazz.getName()).thenReturn(className);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(className));
    }

    /**
     * Tests that keepClassName preserves a simple class name (no package).
     */
    @Test
    public void testKeepClassName_withSimpleClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String simpleName = "MyClass";
        when(mockClazz.getName()).thenReturn(simpleName);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(simpleName));
    }

    /**
     * Tests that keepClassName preserves a fully qualified class name.
     */
    @Test
    public void testKeepClassName_withFullyQualifiedName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String fullyQualifiedName = "com/example/foo/bar/MyClass";
        when(mockClazz.getName()).thenReturn(fullyQualifiedName);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(fullyQualifiedName));
    }

    /**
     * Tests that keepClassName preserves inner class names (containing $ separator).
     */
    @Test
    public void testKeepClassName_withInnerClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String innerClassName = "com/example/OuterClass$InnerClass";
        when(mockClazz.getName()).thenReturn(innerClassName);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(innerClassName));
    }

    /**
     * Tests that keepClassName preserves numeric inner class names (anonymous classes).
     */
    @Test
    public void testKeepClassName_withNumericInnerClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String numericInnerClassName = "com/example/OuterClass$1";
        when(mockClazz.getName()).thenReturn(numericInnerClassName);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(numericInnerClassName));
    }

    /**
     * Tests that keepClassName preserves nested inner class names.
     */
    @Test
    public void testKeepClassName_withNestedInnerClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nestedInnerClassName = "com/example/OuterClass$MiddleClass$InnerClass";
        when(mockClazz.getName()).thenReturn(nestedInnerClassName);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(nestedInnerClassName));
    }

    /**
     * Tests that keepClassName does not throw an exception with valid inputs.
     */
    @Test
    public void testKeepClassName_doesNotThrowException() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getName()).thenReturn("com/example/TestClass");

        // Act & Assert
        assertDoesNotThrow(() -> nameMarker.keepClassName(mockClazz));
    }

    /**
     * Tests that keepClassName can be called multiple times on the same class.
     * Each call should preserve the current name at the time of the call.
     */
    @Test
    public void testKeepClassName_calledMultipleTimes() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/MyClass";
        when(mockClazz.getName()).thenReturn(className);

        // Act
        nameMarker.keepClassName(mockClazz);
        nameMarker.keepClassName(mockClazz);
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(3)).getName();
        verify(mockClazz, times(3)).setProcessingInfo(eq(className));
    }

    /**
     * Tests that keepClassName works with different Clazz instances.
     */
    @Test
    public void testKeepClassName_withDifferentClazzes() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);
        String name1 = "com/example/Class1";
        String name2 = "com/example/Class2";
        String name3 = "com/example/Class3";
        when(clazz1.getName()).thenReturn(name1);
        when(clazz2.getName()).thenReturn(name2);
        when(clazz3.getName()).thenReturn(name3);

        // Act
        nameMarker.keepClassName(clazz1);
        nameMarker.keepClassName(clazz2);
        nameMarker.keepClassName(clazz3);

        // Assert
        verify(clazz1, times(1)).getName();
        verify(clazz1, times(1)).setProcessingInfo(eq(name1));
        verify(clazz2, times(1)).getName();
        verify(clazz2, times(1)).setProcessingInfo(eq(name2));
        verify(clazz3, times(1)).getName();
        verify(clazz3, times(1)).setProcessingInfo(eq(name3));
    }

    /**
     * Tests that multiple NameMarker instances can independently preserve class names.
     */
    @Test
    public void testKeepClassName_withMultipleNameMarkers() {
        // Arrange
        NameMarker marker1 = new NameMarker();
        NameMarker marker2 = new NameMarker();
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        String name1 = "com/example/Class1";
        String name2 = "com/example/Class2";
        when(clazz1.getName()).thenReturn(name1);
        when(clazz2.getName()).thenReturn(name2);

        // Act
        marker1.keepClassName(clazz1);
        marker2.keepClassName(clazz2);

        // Assert
        verify(clazz1, times(1)).getName();
        verify(clazz1, times(1)).setProcessingInfo(eq(name1));
        verify(clazz2, times(1)).getName();
        verify(clazz2, times(1)).setProcessingInfo(eq(name2));
    }

    // ========== Tests for Edge Cases ==========

    /**
     * Tests that keepClassName works with special characters in the name.
     */
    @Test
    public void testKeepClassName_withSpecialCharacters() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nameWithSpecialChars = "com/example/_Test$Class_123";
        when(mockClazz.getName()).thenReturn(nameWithSpecialChars);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(nameWithSpecialChars));
    }

    /**
     * Tests that keepClassName works with very long class names.
     */
    @Test
    public void testKeepClassName_withLongClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String longName = "com/example/very/long/package/structure/with/many/levels/VeryLongClassName";
        when(mockClazz.getName()).thenReturn(longName);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(longName));
    }

    /**
     * Tests that keepClassName works with single-character class names.
     */
    @Test
    public void testKeepClassName_withSingleCharacter() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String singleChar = "A";
        when(mockClazz.getName()).thenReturn(singleChar);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(singleChar));
    }

    /**
     * Tests that keepClassName preserves the exact name returned by getName.
     */
    @Test
    public void testKeepClassName_preservesExactName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/TestClass";
        when(mockClazz.getName()).thenReturn(originalName);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(same(originalName));
    }

    /**
     * Tests that keepClassName works with mixed case names.
     */
    @Test
    public void testKeepClassName_withMixedCase() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String mixedCaseName = "CoM/eXaMpLe/TeSt";
        when(mockClazz.getName()).thenReturn(mixedCaseName);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(mixedCaseName));
    }

    /**
     * Tests that keepClassName works when called in sequence on multiple classes.
     */
    @Test
    public void testKeepClassName_sequentialProcessing() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        Clazz clazz3 = mock(Clazz.class);
        String name1 = "a/b/C1";
        String name2 = "a/b/C2";
        String name3 = "a/b/C3";
        when(clazz1.getName()).thenReturn(name1);
        when(clazz2.getName()).thenReturn(name2);
        when(clazz3.getName()).thenReturn(name3);

        // Act
        nameMarker.keepClassName(clazz1);
        nameMarker.keepClassName(clazz2);
        nameMarker.keepClassName(clazz3);

        // Assert
        verify(clazz1, times(1)).getName();
        verify(clazz1, times(1)).setProcessingInfo(eq(name1));
        verify(clazz2, times(1)).getName();
        verify(clazz2, times(1)).setProcessingInfo(eq(name2));
        verify(clazz3, times(1)).getName();
        verify(clazz3, times(1)).setProcessingInfo(eq(name3));
    }

    /**
     * Tests that keepClassName works with classes in the default package.
     */
    @Test
    public void testKeepClassName_withDefaultPackage() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String defaultPackageName = "TestClass";
        when(mockClazz.getName()).thenReturn(defaultPackageName);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(defaultPackageName));
    }

    /**
     * Tests that keepClassName works with lambda class names.
     */
    @Test
    public void testKeepClassName_withLambdaClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String lambdaClassName = "com/example/MyClass$$Lambda$1";
        when(mockClazz.getName()).thenReturn(lambdaClassName);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(lambdaClassName));
    }

    /**
     * Tests that keepClassName is idempotent - calling it multiple times has consistent behavior.
     */
    @Test
    public void testKeepClassName_isIdempotent() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/Test";
        when(mockClazz.getName()).thenReturn(className);

        // Act
        nameMarker.keepClassName(mockClazz);
        nameMarker.keepClassName(mockClazz);
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(3)).getName();
        verify(mockClazz, times(3)).setProcessingInfo(eq(className));
    }

    /**
     * Tests that keepClassName properly integrates with ClassObfuscator.setNewClassName.
     */
    @Test
    public void testKeepClassName_integrationWithSetNewClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/MyClass";
        when(mockClazz.getName()).thenReturn(className);
        when(mockClazz.getProcessingInfo()).thenReturn(className);

        // Act
        nameMarker.keepClassName(mockClazz);
        String retrievedName = ClassObfuscator.newClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(className));
        assertEquals(className, retrievedName, "Retrieved name should match the original name");
    }

    /**
     * Tests that keepClassName works with both ProgramClass and LibraryClass.
     */
    @Test
    public void testKeepClassName_withDifferentClazzTypes() {
        // Arrange
        Clazz programClazz = mock(ProgramClass.class);
        Clazz libraryClazz = mock(proguard.classfile.LibraryClass.class);
        String programClassName = "com/example/ProgramClass";
        String libraryClassName = "com/example/LibraryClass";
        when(programClazz.getName()).thenReturn(programClassName);
        when(libraryClazz.getName()).thenReturn(libraryClassName);

        // Act
        nameMarker.keepClassName(programClazz);
        nameMarker.keepClassName(libraryClazz);

        // Assert
        verify(programClazz, times(1)).getName();
        verify(programClazz, times(1)).setProcessingInfo(eq(programClassName));
        verify(libraryClazz, times(1)).getName();
        verify(libraryClazz, times(1)).setProcessingInfo(eq(libraryClassName));
    }

    /**
     * Tests that keepClassName correctly handles classes with numeric names.
     */
    @Test
    public void testKeepClassName_withNumericClassName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String numericClassName = "com/example/Class123";
        when(mockClazz.getName()).thenReturn(numericClassName);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(numericClassName));
    }

    /**
     * Tests rapid sequential calls to keepClassName.
     */
    @Test
    public void testKeepClassName_rapidSequentialCalls() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/Test";
        when(mockClazz.getName()).thenReturn(className);

        // Act - Rapid calls
        for (int i = 0; i < 50; i++) {
            nameMarker.keepClassName(mockClazz);
        }

        // Assert
        verify(mockClazz, times(50)).getName();
        verify(mockClazz, times(50)).setProcessingInfo(eq(className));
    }

    /**
     * Tests that keepClassName preserves names with underscores.
     */
    @Test
    public void testKeepClassName_withUnderscores() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nameWithUnderscores = "com/example/_Test_Class_";
        when(mockClazz.getName()).thenReturn(nameWithUnderscores);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(nameWithUnderscores));
    }

    /**
     * Tests that keepClassName preserves names with multiple dollar signs.
     */
    @Test
    public void testKeepClassName_withMultipleDollarSigns() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String nameWithMultipleDollarSigns = "com/example/Outer$Middle$Inner$Deep";
        when(mockClazz.getName()).thenReturn(nameWithMultipleDollarSigns);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(nameWithMultipleDollarSigns));
    }

    /**
     * Tests that keepClassName preserves the purpose of preventing obfuscation.
     */
    @Test
    public void testKeepClassName_preventsObfuscation() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String originalName = "com/example/ImportantClass";
        when(mockClazz.getName()).thenReturn(originalName);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert - The method sets the "new" name to the current name,
        // effectively marking the class to keep its original name
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(originalName));
    }

    /**
     * Tests that keepClassName works correctly when getName is called multiple times.
     */
    @Test
    public void testKeepClassName_getNameCalledOnce() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/Test";
        when(mockClazz.getName()).thenReturn(className);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert - getName should be called exactly once
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(className));
    }

    /**
     * Tests that keepClassName works when the class name changes between calls.
     */
    @Test
    public void testKeepClassName_withChangingName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String firstName = "com/example/FirstName";
        String secondName = "com/example/SecondName";

        when(mockClazz.getName()).thenReturn(firstName);

        // Act - First call
        nameMarker.keepClassName(mockClazz);

        // Arrange - Change the name
        when(mockClazz.getName()).thenReturn(secondName);

        // Act - Second call
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(2)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(firstName));
        verify(mockClazz, times(1)).setProcessingInfo(eq(secondName));
    }

    /**
     * Tests that keepClassName correctly implements the documented behavior
     * of ensuring the class name will be kept.
     */
    @Test
    public void testKeepClassName_ensuresNameIsKept() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/PreservedClass";
        when(mockClazz.getName()).thenReturn(className);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert - The method should set the new class name to the current name,
        // which prevents obfuscation by explicitly setting the "obfuscated" name
        // to be the same as the original
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(className));
    }

    /**
     * Tests that keepClassName works with alternating classes.
     */
    @Test
    public void testKeepClassName_alternatingClasses() {
        // Arrange
        Clazz clazz1 = mock(Clazz.class);
        Clazz clazz2 = mock(Clazz.class);
        String name1 = "com/example/Class1";
        String name2 = "com/example/Class2";
        when(clazz1.getName()).thenReturn(name1);
        when(clazz2.getName()).thenReturn(name2);

        // Act - Alternate between two classes
        nameMarker.keepClassName(clazz1);
        nameMarker.keepClassName(clazz2);
        nameMarker.keepClassName(clazz1);
        nameMarker.keepClassName(clazz2);

        // Assert
        verify(clazz1, times(2)).getName();
        verify(clazz1, times(2)).setProcessingInfo(eq(name1));
        verify(clazz2, times(2)).getName();
        verify(clazz2, times(2)).setProcessingInfo(eq(name2));
    }

    /**
     * Tests that keepClassName uses the exact string returned by getName.
     */
    @Test
    public void testKeepClassName_usesExactStringFromGetName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = new String("com/example/Test"); // Create new String instance
        when(mockClazz.getName()).thenReturn(className);

        // Use argument captor to verify the exact string is used
        final Object[] capturedArg = new Object[1];
        doAnswer(invocation -> {
            capturedArg[0] = invocation.getArgument(0);
            return null;
        }).when(mockClazz).setProcessingInfo(any());

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(any());
        assertSame(className, capturedArg[0], "Should use exact string returned by getName");
    }

    /**
     * Tests that keepClassName works correctly as a public method that can be
     * called externally (not just from within NameMarker's visitor methods).
     */
    @Test
    public void testKeepClassName_asPublicMethod() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/PublicTest";
        when(mockClazz.getName()).thenReturn(className);

        // Act - Call directly as a public method
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(className));
    }

    /**
     * Tests that keepClassName preserves empty string names (edge case).
     */
    @Test
    public void testKeepClassName_withEmptyString() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String emptyName = "";
        when(mockClazz.getName()).thenReturn(emptyName);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(emptyName));
    }

    /**
     * Tests that keepClassName works when getName returns null (edge case).
     */
    @Test
    public void testKeepClassName_withNullName() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        when(mockClazz.getName()).thenReturn(null);

        // Act
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(isNull());
    }

    /**
     * Tests that keepClassName integrates correctly with the name marking workflow
     * by ensuring it can be called as part of the visitor pattern.
     */
    @Test
    public void testKeepClassName_integrationWithVisitorPattern() {
        // Arrange
        Clazz mockClazz = mock(Clazz.class);
        String className = "com/example/VisitorTest";
        when(mockClazz.getName()).thenReturn(className);

        // Act - This method is called from visitProgramClass and visitLibraryClass
        nameMarker.keepClassName(mockClazz);

        // Assert
        verify(mockClazz, times(1)).getName();
        verify(mockClazz, times(1)).setProcessingInfo(eq(className));
    }
}
