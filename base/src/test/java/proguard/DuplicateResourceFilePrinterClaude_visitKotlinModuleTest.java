package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.util.WarningPrinter;
import proguard.resources.kotlinmodule.KotlinModule;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DuplicateResourceFilePrinter#visitKotlinModule(KotlinModule)}.
 *
 * The visitKotlinModule method prints a note about duplicate Kotlin module file definitions.
 * It uses the WarningPrinter to output a message containing the Kotlin module file name.
 * These tests verify the method correctly interacts with the WarningPrinter
 * and handles various input scenarios.
 */
public class DuplicateResourceFilePrinterClaude_visitKotlinModuleTest {

    private DuplicateResourceFilePrinter duplicateResourceFilePrinter;
    private WarningPrinter warningPrinter;
    private KotlinModule kotlinModule;

    @BeforeEach
    public void setUp() {
        warningPrinter = mock(WarningPrinter.class);
        duplicateResourceFilePrinter = new DuplicateResourceFilePrinter(warningPrinter);
        kotlinModule = mock(KotlinModule.class);
    }

    /**
     * Tests that visitKotlinModule calls the WarningPrinter with valid inputs.
     * Verifies basic functionality with a simple file name.
     */
    @Test
    public void testVisitKotlinModule_withValidModule_callsWarningPrinter() {
        // Arrange
        when(kotlinModule.getFileName()).thenReturn("module.kotlin_module");

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - verify print was called once
        verify(warningPrinter, times(1)).print(anyString(), anyString());
    }

    /**
     * Tests that visitKotlinModule passes the correct file name to the printer.
     */
    @Test
    public void testVisitKotlinModule_passesFileName() {
        // Arrange
        String fileName = "META-INF/test.kotlin_module";
        when(kotlinModule.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - verify first argument is the file name
        verify(warningPrinter).print(eq(fileName), anyString());
    }

    /**
     * Tests that the message contains the expected prefix "Note: duplicate definition".
     */
    @Test
    public void testVisitKotlinModule_messageContainsExpectedPrefix() {
        // Arrange
        when(kotlinModule.getFileName()).thenReturn("module.kotlin_module");

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - verify message starts with expected text
        verify(warningPrinter).print(anyString(), contains("Note: duplicate definition of Kotlin module file"));
    }

    /**
     * Tests that the message contains the Kotlin module file name.
     */
    @Test
    public void testVisitKotlinModule_messageContainsFileName() {
        // Arrange
        String fileName = "META-INF/app.kotlin_module";
        when(kotlinModule.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - message should contain the file name
        verify(warningPrinter).print(anyString(), contains(fileName));
    }

    /**
     * Tests visitKotlinModule with a simple file name (no path).
     */
    @Test
    public void testVisitKotlinModule_withSimpleFileName() {
        // Arrange
        when(kotlinModule.getFileName()).thenReturn("test.kotlin_module");

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - verify print was called with the simple name
        verify(warningPrinter).print(eq("test.kotlin_module"), contains("test.kotlin_module"));
    }

    /**
     * Tests visitKotlinModule with a file path including directories.
     */
    @Test
    public void testVisitKotlinModule_withDirectoryPath() {
        // Arrange
        String fileName = "META-INF/services/app.kotlin_module";
        when(kotlinModule.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - verify the full path is used
        verify(warningPrinter).print(eq(fileName), contains(fileName));
    }

    /**
     * Tests visitKotlinModule can be called multiple times.
     */
    @Test
    public void testVisitKotlinModule_calledMultipleTimes() {
        // Arrange
        when(kotlinModule.getFileName()).thenReturn("module.kotlin_module");

        // Act - call three times
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - verify print was called three times
        verify(warningPrinter, times(3)).print(anyString(), anyString());
    }

    /**
     * Tests visitKotlinModule with different KotlinModule instances.
     */
    @Test
    public void testVisitKotlinModule_withDifferentModules() {
        // Arrange
        KotlinModule module1 = mock(KotlinModule.class);
        KotlinModule module2 = mock(KotlinModule.class);
        KotlinModule module3 = mock(KotlinModule.class);

        when(module1.getFileName()).thenReturn("module1.kotlin_module");
        when(module2.getFileName()).thenReturn("module2.kotlin_module");
        when(module3.getFileName()).thenReturn("module3.kotlin_module");

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(module1);
        duplicateResourceFilePrinter.visitKotlinModule(module2);
        duplicateResourceFilePrinter.visitKotlinModule(module3);

        // Assert - verify each file name was used
        verify(warningPrinter).print(eq("module1.kotlin_module"), contains("module1.kotlin_module"));
        verify(warningPrinter).print(eq("module2.kotlin_module"), contains("module2.kotlin_module"));
        verify(warningPrinter).print(eq("module3.kotlin_module"), contains("module3.kotlin_module"));
    }

    /**
     * Tests that visitKotlinModule throws NullPointerException with null KotlinModule.
     * The method attempts to call getFileName() on the null object.
     */
    @Test
    public void testVisitKotlinModule_withNullModule_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> duplicateResourceFilePrinter.visitKotlinModule(null));
    }

    /**
     * Tests that visitKotlinModule with null WarningPrinter throws NullPointerException.
     */
    @Test
    public void testVisitKotlinModule_withNullWarningPrinter_throwsNullPointerException() {
        // Arrange
        DuplicateResourceFilePrinter printerWithNullWarning = new DuplicateResourceFilePrinter(null);
        when(kotlinModule.getFileName()).thenReturn("test.kotlin_module");

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> printerWithNullWarning.visitKotlinModule(kotlinModule));
    }

    /**
     * Tests visitKotlinModule does not throw exception with valid inputs.
     */
    @Test
    public void testVisitKotlinModule_doesNotThrowExceptionWithValidInputs() {
        // Arrange
        when(kotlinModule.getFileName()).thenReturn("test.kotlin_module");

        // Act & Assert
        assertDoesNotThrow(() -> duplicateResourceFilePrinter.visitKotlinModule(kotlinModule));
    }

    /**
     * Tests that the message format is complete with brackets.
     */
    @Test
    public void testVisitKotlinModule_messageFormatWithBrackets() {
        // Arrange
        when(kotlinModule.getFileName()).thenReturn("test.kotlin_module");

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - message should contain brackets around the file name
        verify(warningPrinter).print(anyString(), matches(".*\\[.*\\].*"));
    }

    /**
     * Tests visitKotlinModule with a deeply nested path.
     */
    @Test
    public void testVisitKotlinModule_withDeeplyNestedPath() {
        // Arrange
        String fileName = "META-INF/services/kotlin/modules/app/main.kotlin_module";
        when(kotlinModule.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - verify the full nested path is used
        verify(warningPrinter).print(eq(fileName), contains(fileName));
    }

    /**
     * Tests visitKotlinModule on multiple printer instances.
     */
    @Test
    public void testVisitKotlinModule_multiplePrinterInstances() {
        // Arrange
        WarningPrinter printer1 = mock(WarningPrinter.class);
        WarningPrinter printer2 = mock(WarningPrinter.class);
        DuplicateResourceFilePrinter dup1 = new DuplicateResourceFilePrinter(printer1);
        DuplicateResourceFilePrinter dup2 = new DuplicateResourceFilePrinter(printer2);

        when(kotlinModule.getFileName()).thenReturn("test.kotlin_module");

        // Act
        dup1.visitKotlinModule(kotlinModule);
        dup2.visitKotlinModule(kotlinModule);

        // Assert - each printer should be called independently
        verify(printer1, times(1)).print(anyString(), anyString());
        verify(printer2, times(1)).print(anyString(), anyString());
    }

    /**
     * Tests that visitKotlinModule calls getFileName() exactly twice per invocation.
     */
    @Test
    public void testVisitKotlinModule_callsGetFileNameTwice() {
        // Arrange
        when(kotlinModule.getFileName()).thenReturn("test.kotlin_module");

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - getFileName should be called twice (once for first arg, once for message)
        verify(kotlinModule, times(2)).getFileName();
    }

    /**
     * Tests visitKotlinModule after calling it with other modules.
     */
    @Test
    public void testVisitKotlinModule_afterOtherVisitorMethods() {
        // Arrange
        KotlinModule module1 = mock(KotlinModule.class);
        KotlinModule module2 = mock(KotlinModule.class);
        when(module1.getFileName()).thenReturn("first.kotlin_module");
        when(module2.getFileName()).thenReturn("second.kotlin_module");

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(module1);
        duplicateResourceFilePrinter.visitKotlinModule(module2);

        // Assert - both should have been processed
        verify(warningPrinter).print(eq("first.kotlin_module"), anyString());
        verify(warningPrinter).print(eq("second.kotlin_module"), anyString());
    }

    /**
     * Tests that the complete message follows expected format.
     */
    @Test
    public void testVisitKotlinModule_completeMessageFormat() {
        // Arrange
        when(kotlinModule.getFileName()).thenReturn("app.kotlin_module");

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - complete expected format
        verify(warningPrinter).print(
                eq("app.kotlin_module"),
                eq("Note: duplicate definition of Kotlin module file [app.kotlin_module]")
        );
    }

    /**
     * Tests visitKotlinModule with empty string file name.
     */
    @Test
    public void testVisitKotlinModule_withEmptyFileName() {
        // Arrange
        when(kotlinModule.getFileName()).thenReturn("");

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - should handle empty string
        verify(warningPrinter).print(eq(""), anyString());
    }

    /**
     * Tests that visitKotlinModule is stateless across invocations.
     */
    @Test
    public void testVisitKotlinModule_statelessBehavior() {
        // Arrange
        KotlinModule module1 = mock(KotlinModule.class);
        KotlinModule module2 = mock(KotlinModule.class);
        when(module1.getFileName()).thenReturn("first.kotlin_module");
        when(module2.getFileName()).thenReturn("second.kotlin_module");

        // Act - call with different modules
        duplicateResourceFilePrinter.visitKotlinModule(module1);
        reset(warningPrinter); // Reset to verify second call is independent
        duplicateResourceFilePrinter.visitKotlinModule(module2);

        // Assert - second call should not be affected by first
        verify(warningPrinter, times(1)).print(eq("second.kotlin_module"), anyString());
    }

    /**
     * Tests visitKotlinModule with special characters in file name.
     */
    @Test
    public void testVisitKotlinModule_withSpecialCharactersInFileName() {
        // Arrange
        String fileName = "module-name_with.special$chars@123.kotlin_module";
        when(kotlinModule.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - special characters should be preserved
        verify(warningPrinter).print(eq(fileName), contains(fileName));
    }

    /**
     * Tests visitKotlinModule with Windows-style path separator.
     */
    @Test
    public void testVisitKotlinModule_withWindowsStylePath() {
        // Arrange
        String fileName = "META-INF\\modules\\app.kotlin_module";
        when(kotlinModule.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - Windows path should be preserved
        verify(warningPrinter).print(eq(fileName), contains(fileName));
    }

    /**
     * Tests visitKotlinModule with file name containing spaces.
     */
    @Test
    public void testVisitKotlinModule_withSpacesInFileName() {
        // Arrange
        String fileName = "my module file.kotlin_module";
        when(kotlinModule.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - spaces should be preserved
        verify(warningPrinter).print(eq(fileName), contains(fileName));
    }

    /**
     * Tests visitKotlinModule with very long file path.
     */
    @Test
    public void testVisitKotlinModule_withVeryLongFilePath() {
        // Arrange
        String longPath = "very/long/path/with/many/nested/directories/containing/a/kotlin/module/file/named/module.kotlin_module";
        when(kotlinModule.getFileName()).thenReturn(longPath);

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - should handle long paths
        verify(warningPrinter).print(eq(longPath), contains(longPath));
    }

    /**
     * Tests visitKotlinModule with file name containing dots.
     */
    @Test
    public void testVisitKotlinModule_withMultipleDotsInFileName() {
        // Arrange
        String fileName = "my.module.file.v2.kotlin_module";
        when(kotlinModule.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - multiple dots should be preserved
        verify(warningPrinter).print(eq(fileName), contains(fileName));
    }

    /**
     * Tests that both arguments to print use the same file name.
     */
    @Test
    public void testVisitKotlinModule_bothArgumentsUseSameFileName() {
        // Arrange
        String fileName = "test.kotlin_module";
        when(kotlinModule.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - both the first argument and the message should contain the same file name
        verify(warningPrinter).print(
                eq(fileName),
                contains(fileName)
        );
    }

    /**
     * Tests visitKotlinModule with typical Kotlin module file naming convention.
     */
    @Test
    public void testVisitKotlinModule_withTypicalKotlinModuleFileName() {
        // Arrange
        String fileName = "META-INF/main.kotlin_module";
        when(kotlinModule.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - verify typical file name pattern works
        verify(warningPrinter).print(eq(fileName), contains(fileName));
    }

    /**
     * Tests that message distinguishes Kotlin module files from regular resource files.
     */
    @Test
    public void testVisitKotlinModule_messageSpecifiesKotlinModule() {
        // Arrange
        when(kotlinModule.getFileName()).thenReturn("test.kotlin_module");

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - message should specifically mention "Kotlin module file"
        verify(warningPrinter).print(anyString(), contains("Kotlin module file"));
    }

    /**
     * Tests visitKotlinModule with file name without .kotlin_module extension.
     */
    @Test
    public void testVisitKotlinModule_withNonStandardExtension() {
        // Arrange
        String fileName = "module.km";
        when(kotlinModule.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - should handle any file name
        verify(warningPrinter).print(eq(fileName), contains(fileName));
    }

    /**
     * Tests visitKotlinModule with mixed invocations of visitResourceFile.
     * Verifies that both methods work independently.
     */
    @Test
    public void testVisitKotlinModule_mixedWithVisitResourceFile() {
        // Arrange
        proguard.resources.file.ResourceFile resourceFile = mock(proguard.resources.file.ResourceFile.class);
        when(kotlinModule.getFileName()).thenReturn("module.kotlin_module");
        when(resourceFile.getFileName()).thenReturn("resource.xml");

        // Act - alternate between the two methods
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);
        duplicateResourceFilePrinter.visitKotlinModule(kotlinModule);

        // Assert - verify correct messages for each
        verify(warningPrinter, times(2)).print(eq("module.kotlin_module"), contains("Kotlin module file"));
        verify(warningPrinter, times(1)).print(eq("resource.xml"), contains("resource file"));
    }
}
