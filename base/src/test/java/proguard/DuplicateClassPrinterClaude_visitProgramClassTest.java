package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.ProgramClass;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DuplicateClassPrinter#visitProgramClass(ProgramClass)}.
 *
 * The visitProgramClass method prints a note about duplicate program class definitions.
 * It uses the WarningPrinter to output a message containing the internal and external
 * class names. These tests verify the method correctly interacts with the WarningPrinter
 * and handles various input scenarios.
 */
public class DuplicateClassPrinterClaude_visitProgramClassTest {

    private DuplicateClassPrinter duplicateClassPrinter;
    private WarningPrinter warningPrinter;
    private ProgramClass programClass;

    @BeforeEach
    public void setUp() {
        warningPrinter = mock(WarningPrinter.class);
        duplicateClassPrinter = new DuplicateClassPrinter(warningPrinter);
        programClass = mock(ProgramClass.class);
    }

    /**
     * Tests that visitProgramClass calls the WarningPrinter with valid inputs.
     * Verifies basic functionality with a simple class name.
     */
    @Test
    public void testVisitProgramClass_withValidClass_callsWarningPrinter() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Test");

        // Act
        duplicateClassPrinter.visitProgramClass(programClass);

        // Assert - verify print was called once
        verify(warningPrinter, times(1)).print(anyString(), anyString());
    }

    /**
     * Tests that visitProgramClass passes the correct internal class name to the printer.
     */
    @Test
    public void testVisitProgramClass_passesInternalClassName() {
        // Arrange
        String internalName = "com/example/MyClass";
        when(programClass.getName()).thenReturn(internalName);

        // Act
        duplicateClassPrinter.visitProgramClass(programClass);

        // Assert - verify first argument is the internal name
        verify(warningPrinter).print(eq(internalName), anyString());
    }

    /**
     * Tests that the message contains the expected prefix "Note: duplicate definition".
     */
    @Test
    public void testVisitProgramClass_messageContainsExpectedPrefix() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Test");

        // Act
        duplicateClassPrinter.visitProgramClass(programClass);

        // Assert - verify message starts with expected text
        verify(warningPrinter).print(anyString(), contains("Note: duplicate definition of program class"));
    }

    /**
     * Tests that the message contains the external class name (with dots instead of slashes).
     */
    @Test
    public void testVisitProgramClass_messageContainsExternalClassName() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/TestClass");

        // Act
        duplicateClassPrinter.visitProgramClass(programClass);

        // Assert - external name should use dots: com.example.TestClass
        verify(warningPrinter).print(anyString(), contains("com.example.TestClass"));
    }

    /**
     * Tests visitProgramClass with a simple class name (no package).
     */
    @Test
    public void testVisitProgramClass_withSimpleClassName() {
        // Arrange
        when(programClass.getName()).thenReturn("SimpleClass");

        // Act
        duplicateClassPrinter.visitProgramClass(programClass);

        // Assert - verify print was called with the simple name
        verify(warningPrinter).print(eq("SimpleClass"), contains("SimpleClass"));
    }

    /**
     * Tests visitProgramClass with a deeply nested package structure.
     */
    @Test
    public void testVisitProgramClass_withDeeplyNestedPackage() {
        // Arrange
        String internalName = "com/example/deeply/nested/package/structure/MyClass";
        when(programClass.getName()).thenReturn(internalName);

        // Act
        duplicateClassPrinter.visitProgramClass(programClass);

        // Assert - verify external name uses dots
        verify(warningPrinter).print(eq(internalName),
                contains("com.example.deeply.nested.package.structure.MyClass"));
    }

    /**
     * Tests visitProgramClass can be called multiple times.
     */
    @Test
    public void testVisitProgramClass_calledMultipleTimes() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Test");

        // Act - call three times
        duplicateClassPrinter.visitProgramClass(programClass);
        duplicateClassPrinter.visitProgramClass(programClass);
        duplicateClassPrinter.visitProgramClass(programClass);

        // Assert - verify print was called three times
        verify(warningPrinter, times(3)).print(anyString(), anyString());
    }

    /**
     * Tests visitProgramClass with different ProgramClass instances.
     */
    @Test
    public void testVisitProgramClass_withDifferentClasses() {
        // Arrange
        ProgramClass class1 = mock(ProgramClass.class);
        ProgramClass class2 = mock(ProgramClass.class);
        ProgramClass class3 = mock(ProgramClass.class);

        when(class1.getName()).thenReturn("com/example/Class1");
        when(class2.getName()).thenReturn("com/example/Class2");
        when(class3.getName()).thenReturn("com/example/Class3");

        // Act
        duplicateClassPrinter.visitProgramClass(class1);
        duplicateClassPrinter.visitProgramClass(class2);
        duplicateClassPrinter.visitProgramClass(class3);

        // Assert - verify each class name was used
        verify(warningPrinter).print(eq("com/example/Class1"), contains("Class1"));
        verify(warningPrinter).print(eq("com/example/Class2"), contains("Class2"));
        verify(warningPrinter).print(eq("com/example/Class3"), contains("Class3"));
    }

    /**
     * Tests that visitProgramClass throws NullPointerException with null ProgramClass.
     * The method attempts to call getName() on the null object.
     */
    @Test
    public void testVisitProgramClass_withNullClass_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> duplicateClassPrinter.visitProgramClass(null));
    }

    /**
     * Tests that visitProgramClass with null WarningPrinter throws NullPointerException.
     */
    @Test
    public void testVisitProgramClass_withNullWarningPrinter_throwsNullPointerException() {
        // Arrange
        DuplicateClassPrinter printerWithNullWarning = new DuplicateClassPrinter(null);
        when(programClass.getName()).thenReturn("com/example/Test");

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> printerWithNullWarning.visitProgramClass(programClass));
    }

    /**
     * Tests visitProgramClass does not throw exception with valid inputs.
     */
    @Test
    public void testVisitProgramClass_doesNotThrowExceptionWithValidInputs() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Test");

        // Act & Assert
        assertDoesNotThrow(() -> duplicateClassPrinter.visitProgramClass(programClass));
    }

    /**
     * Tests that the message format is complete with brackets.
     */
    @Test
    public void testVisitProgramClass_messageFormatWithBrackets() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Test");

        // Act
        duplicateClassPrinter.visitProgramClass(programClass);

        // Assert - message should contain brackets around the class name
        verify(warningPrinter).print(anyString(), matches(".*\\[.*\\].*"));
    }

    /**
     * Tests visitProgramClass with inner class notation (using $).
     */
    @Test
    public void testVisitProgramClass_withInnerClass() {
        // Arrange
        String internalName = "com/example/OuterClass$InnerClass";
        when(programClass.getName()).thenReturn(internalName);

        // Act
        duplicateClassPrinter.visitProgramClass(programClass);

        // Assert - external name should use dots and preserve $
        verify(warningPrinter).print(eq(internalName),
                contains("com.example.OuterClass$InnerClass"));
    }

    /**
     * Tests visitProgramClass with anonymous inner class notation.
     */
    @Test
    public void testVisitProgramClass_withAnonymousInnerClass() {
        // Arrange
        String internalName = "com/example/OuterClass$1";
        when(programClass.getName()).thenReturn(internalName);

        // Act
        duplicateClassPrinter.visitProgramClass(programClass);

        // Assert
        verify(warningPrinter).print(eq(internalName), contains("com.example.OuterClass$1"));
    }

    /**
     * Tests visitProgramClass on multiple printer instances.
     */
    @Test
    public void testVisitProgramClass_multiplePrinterInstances() {
        // Arrange
        WarningPrinter printer1 = mock(WarningPrinter.class);
        WarningPrinter printer2 = mock(WarningPrinter.class);
        DuplicateClassPrinter dup1 = new DuplicateClassPrinter(printer1);
        DuplicateClassPrinter dup2 = new DuplicateClassPrinter(printer2);

        when(programClass.getName()).thenReturn("com/example/Test");

        // Act
        dup1.visitProgramClass(programClass);
        dup2.visitProgramClass(programClass);

        // Assert - each printer should be called independently
        verify(printer1, times(1)).print(anyString(), anyString());
        verify(printer2, times(1)).print(anyString(), anyString());
    }

    /**
     * Tests that visitProgramClass calls getName() exactly once per invocation.
     */
    @Test
    public void testVisitProgramClass_callsGetNameOnce() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/Test");

        // Act
        duplicateClassPrinter.visitProgramClass(programClass);

        // Assert - getName should be called at least once (possibly twice for external name conversion)
        verify(programClass, atLeastOnce()).getName();
    }

    /**
     * Tests visitProgramClass after calling other visitor methods.
     */
    @Test
    public void testVisitProgramClass_afterOtherVisitorMethods() {
        // Arrange
        ProgramClass class1 = mock(ProgramClass.class);
        ProgramClass class2 = mock(ProgramClass.class);
        when(class1.getName()).thenReturn("com/example/First");
        when(class2.getName()).thenReturn("com/example/Second");

        // Act
        duplicateClassPrinter.visitProgramClass(class1);
        duplicateClassPrinter.visitProgramClass(class2);

        // Assert - both should have been processed
        verify(warningPrinter).print(eq("com/example/First"), anyString());
        verify(warningPrinter).print(eq("com/example/Second"), anyString());
    }

    /**
     * Tests that the complete message follows expected format.
     */
    @Test
    public void testVisitProgramClass_completeMessageFormat() {
        // Arrange
        when(programClass.getName()).thenReturn("com/example/TestClass");

        // Act
        duplicateClassPrinter.visitProgramClass(programClass);

        // Assert - complete expected format
        verify(warningPrinter).print(
                eq("com/example/TestClass"),
                eq("Note: duplicate definition of program class [com.example.TestClass]")
        );
    }

    /**
     * Tests visitProgramClass with empty string class name.
     */
    @Test
    public void testVisitProgramClass_withEmptyClassName() {
        // Arrange
        when(programClass.getName()).thenReturn("");

        // Act
        duplicateClassPrinter.visitProgramClass(programClass);

        // Assert - should handle empty string
        verify(warningPrinter).print(eq(""), anyString());
    }

    /**
     * Tests that visitProgramClass is stateless across invocations.
     */
    @Test
    public void testVisitProgramClass_statelessBehavior() {
        // Arrange
        ProgramClass class1 = mock(ProgramClass.class);
        ProgramClass class2 = mock(ProgramClass.class);
        when(class1.getName()).thenReturn("com/example/First");
        when(class2.getName()).thenReturn("com/example/Second");

        // Act - call with different classes
        duplicateClassPrinter.visitProgramClass(class1);
        reset(warningPrinter); // Reset to verify second call is independent
        duplicateClassPrinter.visitProgramClass(class2);

        // Assert - second call should not be affected by first
        verify(warningPrinter, times(1)).print(eq("com/example/Second"), anyString());
    }
}
