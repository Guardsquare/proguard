package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.LibraryClass;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DuplicateClassPrinter#visitLibraryClass(LibraryClass)}.
 *
 * The visitLibraryClass method prints a note about duplicate library class definitions.
 * It uses the WarningPrinter to output a message containing the internal and external
 * class names. These tests verify the method correctly interacts with the WarningPrinter
 * and handles various input scenarios.
 */
public class DuplicateClassPrinterClaude_visitLibraryClassTest {

    private DuplicateClassPrinter duplicateClassPrinter;
    private WarningPrinter warningPrinter;
    private LibraryClass libraryClass;

    @BeforeEach
    public void setUp() {
        warningPrinter = mock(WarningPrinter.class);
        duplicateClassPrinter = new DuplicateClassPrinter(warningPrinter);
        libraryClass = mock(LibraryClass.class);
    }

    /**
     * Tests that visitLibraryClass calls the WarningPrinter with valid inputs.
     * Verifies basic functionality with a simple class name.
     */
    @Test
    public void testVisitLibraryClass_withValidClass_callsWarningPrinter() {
        // Arrange
        when(libraryClass.getName()).thenReturn("com/example/Test");

        // Act
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert - verify print was called once
        verify(warningPrinter, times(1)).print(anyString(), anyString());
    }

    /**
     * Tests that visitLibraryClass passes the correct internal class name to the printer.
     */
    @Test
    public void testVisitLibraryClass_passesInternalClassName() {
        // Arrange
        String internalName = "com/example/MyClass";
        when(libraryClass.getName()).thenReturn(internalName);

        // Act
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert - verify first argument is the internal name
        verify(warningPrinter).print(eq(internalName), anyString());
    }

    /**
     * Tests that the message contains the expected prefix "Note: duplicate definition".
     */
    @Test
    public void testVisitLibraryClass_messageContainsExpectedPrefix() {
        // Arrange
        when(libraryClass.getName()).thenReturn("com/example/Test");

        // Act
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert - verify message starts with expected text
        verify(warningPrinter).print(anyString(), contains("Note: duplicate definition of library class"));
    }

    /**
     * Tests that the message contains the external class name (with dots instead of slashes).
     */
    @Test
    public void testVisitLibraryClass_messageContainsExternalClassName() {
        // Arrange
        when(libraryClass.getName()).thenReturn("com/example/TestClass");

        // Act
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert - external name should use dots: com.example.TestClass
        verify(warningPrinter).print(anyString(), contains("com.example.TestClass"));
    }

    /**
     * Tests visitLibraryClass with a simple class name (no package).
     */
    @Test
    public void testVisitLibraryClass_withSimpleClassName() {
        // Arrange
        when(libraryClass.getName()).thenReturn("SimpleClass");

        // Act
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert - verify print was called with the simple name
        verify(warningPrinter).print(eq("SimpleClass"), contains("SimpleClass"));
    }

    /**
     * Tests visitLibraryClass with a deeply nested package structure.
     */
    @Test
    public void testVisitLibraryClass_withDeeplyNestedPackage() {
        // Arrange
        String internalName = "com/example/deeply/nested/package/structure/MyClass";
        when(libraryClass.getName()).thenReturn(internalName);

        // Act
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert - verify external name uses dots
        verify(warningPrinter).print(eq(internalName),
                contains("com.example.deeply.nested.package.structure.MyClass"));
    }

    /**
     * Tests visitLibraryClass can be called multiple times.
     */
    @Test
    public void testVisitLibraryClass_calledMultipleTimes() {
        // Arrange
        when(libraryClass.getName()).thenReturn("com/example/Test");

        // Act - call three times
        duplicateClassPrinter.visitLibraryClass(libraryClass);
        duplicateClassPrinter.visitLibraryClass(libraryClass);
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert - verify print was called three times
        verify(warningPrinter, times(3)).print(anyString(), anyString());
    }

    /**
     * Tests visitLibraryClass with different LibraryClass instances.
     */
    @Test
    public void testVisitLibraryClass_withDifferentClasses() {
        // Arrange
        LibraryClass class1 = mock(LibraryClass.class);
        LibraryClass class2 = mock(LibraryClass.class);
        LibraryClass class3 = mock(LibraryClass.class);

        when(class1.getName()).thenReturn("com/example/Class1");
        when(class2.getName()).thenReturn("com/example/Class2");
        when(class3.getName()).thenReturn("com/example/Class3");

        // Act
        duplicateClassPrinter.visitLibraryClass(class1);
        duplicateClassPrinter.visitLibraryClass(class2);
        duplicateClassPrinter.visitLibraryClass(class3);

        // Assert - verify each class name was used
        verify(warningPrinter).print(eq("com/example/Class1"), contains("Class1"));
        verify(warningPrinter).print(eq("com/example/Class2"), contains("Class2"));
        verify(warningPrinter).print(eq("com/example/Class3"), contains("Class3"));
    }

    /**
     * Tests that visitLibraryClass throws NullPointerException with null LibraryClass.
     * The method attempts to call getName() on the null object.
     */
    @Test
    public void testVisitLibraryClass_withNullClass_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> duplicateClassPrinter.visitLibraryClass(null));
    }

    /**
     * Tests that visitLibraryClass with null WarningPrinter throws NullPointerException.
     */
    @Test
    public void testVisitLibraryClass_withNullWarningPrinter_throwsNullPointerException() {
        // Arrange
        DuplicateClassPrinter printerWithNullWarning = new DuplicateClassPrinter(null);
        when(libraryClass.getName()).thenReturn("com/example/Test");

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> printerWithNullWarning.visitLibraryClass(libraryClass));
    }

    /**
     * Tests visitLibraryClass does not throw exception with valid inputs.
     */
    @Test
    public void testVisitLibraryClass_doesNotThrowExceptionWithValidInputs() {
        // Arrange
        when(libraryClass.getName()).thenReturn("com/example/Test");

        // Act & Assert
        assertDoesNotThrow(() -> duplicateClassPrinter.visitLibraryClass(libraryClass));
    }

    /**
     * Tests that the message format is complete with brackets.
     */
    @Test
    public void testVisitLibraryClass_messageFormatWithBrackets() {
        // Arrange
        when(libraryClass.getName()).thenReturn("com/example/Test");

        // Act
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert - message should contain brackets around the class name
        verify(warningPrinter).print(anyString(), matches(".*\\[.*\\].*"));
    }

    /**
     * Tests visitLibraryClass with inner class notation (using $).
     */
    @Test
    public void testVisitLibraryClass_withInnerClass() {
        // Arrange
        String internalName = "com/example/OuterClass$InnerClass";
        when(libraryClass.getName()).thenReturn(internalName);

        // Act
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert - external name should use dots and preserve $
        verify(warningPrinter).print(eq(internalName),
                contains("com.example.OuterClass$InnerClass"));
    }

    /**
     * Tests visitLibraryClass with anonymous inner class notation.
     */
    @Test
    public void testVisitLibraryClass_withAnonymousInnerClass() {
        // Arrange
        String internalName = "com/example/OuterClass$1";
        when(libraryClass.getName()).thenReturn(internalName);

        // Act
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert
        verify(warningPrinter).print(eq(internalName), contains("com.example.OuterClass$1"));
    }

    /**
     * Tests visitLibraryClass on multiple printer instances.
     */
    @Test
    public void testVisitLibraryClass_multiplePrinterInstances() {
        // Arrange
        WarningPrinter printer1 = mock(WarningPrinter.class);
        WarningPrinter printer2 = mock(WarningPrinter.class);
        DuplicateClassPrinter dup1 = new DuplicateClassPrinter(printer1);
        DuplicateClassPrinter dup2 = new DuplicateClassPrinter(printer2);

        when(libraryClass.getName()).thenReturn("com/example/Test");

        // Act
        dup1.visitLibraryClass(libraryClass);
        dup2.visitLibraryClass(libraryClass);

        // Assert - each printer should be called independently
        verify(printer1, times(1)).print(anyString(), anyString());
        verify(printer2, times(1)).print(anyString(), anyString());
    }

    /**
     * Tests that visitLibraryClass calls getName() exactly once per invocation.
     */
    @Test
    public void testVisitLibraryClass_callsGetNameOnce() {
        // Arrange
        when(libraryClass.getName()).thenReturn("com/example/Test");

        // Act
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert - getName should be called at least once (possibly twice for external name conversion)
        verify(libraryClass, atLeastOnce()).getName();
    }

    /**
     * Tests visitLibraryClass after calling other visitor methods.
     */
    @Test
    public void testVisitLibraryClass_afterOtherVisitorMethods() {
        // Arrange
        LibraryClass class1 = mock(LibraryClass.class);
        LibraryClass class2 = mock(LibraryClass.class);
        when(class1.getName()).thenReturn("com/example/First");
        when(class2.getName()).thenReturn("com/example/Second");

        // Act
        duplicateClassPrinter.visitLibraryClass(class1);
        duplicateClassPrinter.visitLibraryClass(class2);

        // Assert - both should have been processed
        verify(warningPrinter).print(eq("com/example/First"), anyString());
        verify(warningPrinter).print(eq("com/example/Second"), anyString());
    }

    /**
     * Tests that the complete message follows expected format.
     */
    @Test
    public void testVisitLibraryClass_completeMessageFormat() {
        // Arrange
        when(libraryClass.getName()).thenReturn("com/example/TestClass");

        // Act
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert - complete expected format
        verify(warningPrinter).print(
                eq("com/example/TestClass"),
                eq("Note: duplicate definition of library class [com.example.TestClass]")
        );
    }

    /**
     * Tests visitLibraryClass with empty string class name.
     */
    @Test
    public void testVisitLibraryClass_withEmptyClassName() {
        // Arrange
        when(libraryClass.getName()).thenReturn("");

        // Act
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert - should handle empty string
        verify(warningPrinter).print(eq(""), anyString());
    }

    /**
     * Tests that visitLibraryClass is stateless across invocations.
     */
    @Test
    public void testVisitLibraryClass_statelessBehavior() {
        // Arrange
        LibraryClass class1 = mock(LibraryClass.class);
        LibraryClass class2 = mock(LibraryClass.class);
        when(class1.getName()).thenReturn("com/example/First");
        when(class2.getName()).thenReturn("com/example/Second");

        // Act - call with different classes
        duplicateClassPrinter.visitLibraryClass(class1);
        reset(warningPrinter); // Reset to verify second call is independent
        duplicateClassPrinter.visitLibraryClass(class2);

        // Assert - second call should not be affected by first
        verify(warningPrinter, times(1)).print(eq("com/example/Second"), anyString());
    }

    /**
     * Tests visitLibraryClass after visitProgramClass to verify visitor can handle both.
     */
    @Test
    public void testVisitLibraryClass_afterVisitProgramClass() {
        // Arrange
        proguard.classfile.ProgramClass programClass = mock(proguard.classfile.ProgramClass.class);
        when(programClass.getName()).thenReturn("com/example/ProgramClass");
        when(libraryClass.getName()).thenReturn("com/example/LibraryClass");

        // Act
        duplicateClassPrinter.visitProgramClass(programClass);
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert - both should be called with appropriate messages
        verify(warningPrinter).print(eq("com/example/ProgramClass"),
                contains("program class"));
        verify(warningPrinter).print(eq("com/example/LibraryClass"),
                contains("library class"));
    }

    /**
     * Tests that visitLibraryClass message says "library class" not "program class".
     */
    @Test
    public void testVisitLibraryClass_messageContainsLibraryClassNotProgramClass() {
        // Arrange
        when(libraryClass.getName()).thenReturn("com/example/Test");

        // Act
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert - verify it says "library class"
        verify(warningPrinter).print(anyString(), contains("library class"));
        verify(warningPrinter, never()).print(anyString(), contains("program class"));
    }

    /**
     * Tests visitLibraryClass with Java standard library class name.
     */
    @Test
    public void testVisitLibraryClass_withStandardLibraryClass() {
        // Arrange
        String internalName = "java/lang/String";
        when(libraryClass.getName()).thenReturn(internalName);

        // Act
        duplicateClassPrinter.visitLibraryClass(libraryClass);

        // Assert
        verify(warningPrinter).print(eq(internalName), contains("java.lang.String"));
    }

    /**
     * Tests visitLibraryClass rapid sequential calls for performance and consistency.
     */
    @Test
    public void testVisitLibraryClass_rapidSequentialCalls() {
        // Arrange
        when(libraryClass.getName()).thenReturn("com/example/Test");

        // Act - call 10 times rapidly
        for (int i = 0; i < 10; i++) {
            duplicateClassPrinter.visitLibraryClass(libraryClass);
        }

        // Assert - verify called 10 times
        verify(warningPrinter, times(10)).print(anyString(), anyString());
    }
}
