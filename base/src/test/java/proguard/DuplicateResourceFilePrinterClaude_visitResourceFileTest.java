package proguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import proguard.classfile.util.WarningPrinter;
import proguard.resources.file.ResourceFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link DuplicateResourceFilePrinter#visitResourceFile(ResourceFile)}.
 *
 * The visitResourceFile method prints a note about duplicate resource file definitions.
 * It uses the WarningPrinter to output a message containing the resource file name.
 * These tests verify the method correctly interacts with the WarningPrinter
 * and handles various input scenarios.
 */
public class DuplicateResourceFilePrinterClaude_visitResourceFileTest {

    private DuplicateResourceFilePrinter duplicateResourceFilePrinter;
    private WarningPrinter warningPrinter;
    private ResourceFile resourceFile;

    @BeforeEach
    public void setUp() {
        warningPrinter = mock(WarningPrinter.class);
        duplicateResourceFilePrinter = new DuplicateResourceFilePrinter(warningPrinter);
        resourceFile = mock(ResourceFile.class);
    }

    /**
     * Tests that visitResourceFile calls the WarningPrinter with valid inputs.
     * Verifies basic functionality with a simple file name.
     */
    @Test
    public void testVisitResourceFile_withValidFile_callsWarningPrinter() {
        // Arrange
        when(resourceFile.getFileName()).thenReturn("config.xml");

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - verify print was called once
        verify(warningPrinter, times(1)).print(anyString(), anyString());
    }

    /**
     * Tests that visitResourceFile passes the correct file name to the printer.
     */
    @Test
    public void testVisitResourceFile_passesFileName() {
        // Arrange
        String fileName = "resources/data.json";
        when(resourceFile.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - verify first argument is the file name
        verify(warningPrinter).print(eq(fileName), anyString());
    }

    /**
     * Tests that the message contains the expected prefix "Note: duplicate definition".
     */
    @Test
    public void testVisitResourceFile_messageContainsExpectedPrefix() {
        // Arrange
        when(resourceFile.getFileName()).thenReturn("test.xml");

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - verify message starts with expected text
        verify(warningPrinter).print(anyString(), contains("Note: duplicate definition of resource file"));
    }

    /**
     * Tests that the message contains the resource file name.
     */
    @Test
    public void testVisitResourceFile_messageContainsFileName() {
        // Arrange
        String fileName = "assets/image.png";
        when(resourceFile.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - message should contain the file name
        verify(warningPrinter).print(anyString(), contains(fileName));
    }

    /**
     * Tests visitResourceFile with a simple file name (no path).
     */
    @Test
    public void testVisitResourceFile_withSimpleFileName() {
        // Arrange
        when(resourceFile.getFileName()).thenReturn("config.xml");

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - verify print was called with the simple name
        verify(warningPrinter).print(eq("config.xml"), contains("config.xml"));
    }

    /**
     * Tests visitResourceFile with a file path including directories.
     */
    @Test
    public void testVisitResourceFile_withDirectoryPath() {
        // Arrange
        String fileName = "META-INF/resources/styles/main.css";
        when(resourceFile.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - verify the full path is used
        verify(warningPrinter).print(eq(fileName), contains(fileName));
    }

    /**
     * Tests visitResourceFile can be called multiple times.
     */
    @Test
    public void testVisitResourceFile_calledMultipleTimes() {
        // Arrange
        when(resourceFile.getFileName()).thenReturn("test.txt");

        // Act - call three times
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - verify print was called three times
        verify(warningPrinter, times(3)).print(anyString(), anyString());
    }

    /**
     * Tests visitResourceFile with different ResourceFile instances.
     */
    @Test
    public void testVisitResourceFile_withDifferentFiles() {
        // Arrange
        ResourceFile file1 = mock(ResourceFile.class);
        ResourceFile file2 = mock(ResourceFile.class);
        ResourceFile file3 = mock(ResourceFile.class);

        when(file1.getFileName()).thenReturn("file1.xml");
        when(file2.getFileName()).thenReturn("file2.json");
        when(file3.getFileName()).thenReturn("file3.properties");

        // Act
        duplicateResourceFilePrinter.visitResourceFile(file1);
        duplicateResourceFilePrinter.visitResourceFile(file2);
        duplicateResourceFilePrinter.visitResourceFile(file3);

        // Assert - verify each file name was used
        verify(warningPrinter).print(eq("file1.xml"), contains("file1.xml"));
        verify(warningPrinter).print(eq("file2.json"), contains("file2.json"));
        verify(warningPrinter).print(eq("file3.properties"), contains("file3.properties"));
    }

    /**
     * Tests that visitResourceFile throws NullPointerException with null ResourceFile.
     * The method attempts to call getFileName() on the null object.
     */
    @Test
    public void testVisitResourceFile_withNullFile_throwsNullPointerException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> duplicateResourceFilePrinter.visitResourceFile(null));
    }

    /**
     * Tests that visitResourceFile with null WarningPrinter throws NullPointerException.
     */
    @Test
    public void testVisitResourceFile_withNullWarningPrinter_throwsNullPointerException() {
        // Arrange
        DuplicateResourceFilePrinter printerWithNullWarning = new DuplicateResourceFilePrinter(null);
        when(resourceFile.getFileName()).thenReturn("test.xml");

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> printerWithNullWarning.visitResourceFile(resourceFile));
    }

    /**
     * Tests visitResourceFile does not throw exception with valid inputs.
     */
    @Test
    public void testVisitResourceFile_doesNotThrowExceptionWithValidInputs() {
        // Arrange
        when(resourceFile.getFileName()).thenReturn("test.xml");

        // Act & Assert
        assertDoesNotThrow(() -> duplicateResourceFilePrinter.visitResourceFile(resourceFile));
    }

    /**
     * Tests that the message format is complete with brackets.
     */
    @Test
    public void testVisitResourceFile_messageFormatWithBrackets() {
        // Arrange
        when(resourceFile.getFileName()).thenReturn("test.xml");

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - message should contain brackets around the file name
        verify(warningPrinter).print(anyString(), matches(".*\\[.*\\].*"));
    }

    /**
     * Tests visitResourceFile with a deeply nested path.
     */
    @Test
    public void testVisitResourceFile_withDeeplyNestedPath() {
        // Arrange
        String fileName = "assets/images/icons/buttons/submit/active.png";
        when(resourceFile.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - verify the full nested path is used
        verify(warningPrinter).print(eq(fileName), contains(fileName));
    }

    /**
     * Tests visitResourceFile on multiple printer instances.
     */
    @Test
    public void testVisitResourceFile_multiplePrinterInstances() {
        // Arrange
        WarningPrinter printer1 = mock(WarningPrinter.class);
        WarningPrinter printer2 = mock(WarningPrinter.class);
        DuplicateResourceFilePrinter dup1 = new DuplicateResourceFilePrinter(printer1);
        DuplicateResourceFilePrinter dup2 = new DuplicateResourceFilePrinter(printer2);

        when(resourceFile.getFileName()).thenReturn("test.xml");

        // Act
        dup1.visitResourceFile(resourceFile);
        dup2.visitResourceFile(resourceFile);

        // Assert - each printer should be called independently
        verify(printer1, times(1)).print(anyString(), anyString());
        verify(printer2, times(1)).print(anyString(), anyString());
    }

    /**
     * Tests that visitResourceFile calls getFileName() exactly once per invocation.
     */
    @Test
    public void testVisitResourceFile_callsGetFileNameTwice() {
        // Arrange
        when(resourceFile.getFileName()).thenReturn("test.xml");

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - getFileName should be called twice (once for first arg, once for message)
        verify(resourceFile, times(2)).getFileName();
    }

    /**
     * Tests visitResourceFile after calling it with other files.
     */
    @Test
    public void testVisitResourceFile_afterOtherVisitorMethods() {
        // Arrange
        ResourceFile file1 = mock(ResourceFile.class);
        ResourceFile file2 = mock(ResourceFile.class);
        when(file1.getFileName()).thenReturn("first.xml");
        when(file2.getFileName()).thenReturn("second.xml");

        // Act
        duplicateResourceFilePrinter.visitResourceFile(file1);
        duplicateResourceFilePrinter.visitResourceFile(file2);

        // Assert - both should have been processed
        verify(warningPrinter).print(eq("first.xml"), anyString());
        verify(warningPrinter).print(eq("second.xml"), anyString());
    }

    /**
     * Tests that the complete message follows expected format.
     */
    @Test
    public void testVisitResourceFile_completeMessageFormat() {
        // Arrange
        when(resourceFile.getFileName()).thenReturn("config.properties");

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - complete expected format
        verify(warningPrinter).print(
                eq("config.properties"),
                eq("Note: duplicate definition of resource file [config.properties]")
        );
    }

    /**
     * Tests visitResourceFile with empty string file name.
     */
    @Test
    public void testVisitResourceFile_withEmptyFileName() {
        // Arrange
        when(resourceFile.getFileName()).thenReturn("");

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - should handle empty string
        verify(warningPrinter).print(eq(""), anyString());
    }

    /**
     * Tests that visitResourceFile is stateless across invocations.
     */
    @Test
    public void testVisitResourceFile_statelessBehavior() {
        // Arrange
        ResourceFile file1 = mock(ResourceFile.class);
        ResourceFile file2 = mock(ResourceFile.class);
        when(file1.getFileName()).thenReturn("first.xml");
        when(file2.getFileName()).thenReturn("second.xml");

        // Act - call with different files
        duplicateResourceFilePrinter.visitResourceFile(file1);
        reset(warningPrinter); // Reset to verify second call is independent
        duplicateResourceFilePrinter.visitResourceFile(file2);

        // Assert - second call should not be affected by first
        verify(warningPrinter, times(1)).print(eq("second.xml"), anyString());
    }

    /**
     * Tests visitResourceFile with special characters in file name.
     */
    @Test
    public void testVisitResourceFile_withSpecialCharactersInFileName() {
        // Arrange
        String fileName = "file-name_with.special$chars@123.xml";
        when(resourceFile.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - special characters should be preserved
        verify(warningPrinter).print(eq(fileName), contains(fileName));
    }

    /**
     * Tests visitResourceFile with Windows-style path separator.
     */
    @Test
    public void testVisitResourceFile_withWindowsStylePath() {
        // Arrange
        String fileName = "resources\\config\\app.properties";
        when(resourceFile.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - Windows path should be preserved
        verify(warningPrinter).print(eq(fileName), contains(fileName));
    }

    /**
     * Tests visitResourceFile with file extension variations.
     */
    @Test
    public void testVisitResourceFile_withVariousFileExtensions() {
        // Arrange - test different file types
        String[] fileNames = {
            "data.json",
            "config.xml",
            "styles.css",
            "script.js",
            "image.png",
            "document.pdf",
            "archive.zip"
        };

        // Act & Assert
        for (String fileName : fileNames) {
            ResourceFile file = mock(ResourceFile.class);
            when(file.getFileName()).thenReturn(fileName);

            duplicateResourceFilePrinter.visitResourceFile(file);

            verify(warningPrinter).print(eq(fileName), contains(fileName));
        }
    }

    /**
     * Tests visitResourceFile with file name containing spaces.
     */
    @Test
    public void testVisitResourceFile_withSpacesInFileName() {
        // Arrange
        String fileName = "my config file.xml";
        when(resourceFile.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - spaces should be preserved
        verify(warningPrinter).print(eq(fileName), contains(fileName));
    }

    /**
     * Tests visitResourceFile with file name that has no extension.
     */
    @Test
    public void testVisitResourceFile_withNoFileExtension() {
        // Arrange
        when(resourceFile.getFileName()).thenReturn("README");

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - should handle files without extension
        verify(warningPrinter).print(eq("README"), contains("README"));
    }

    /**
     * Tests visitResourceFile with very long file path.
     */
    @Test
    public void testVisitResourceFile_withVeryLongFilePath() {
        // Arrange
        String longPath = "very/long/path/with/many/nested/directories/containing/a/resource/file/named/config.xml";
        when(resourceFile.getFileName()).thenReturn(longPath);

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - should handle long paths
        verify(warningPrinter).print(eq(longPath), contains(longPath));
    }

    /**
     * Tests visitResourceFile with file name containing dots.
     */
    @Test
    public void testVisitResourceFile_withMultipleDotsInFileName() {
        // Arrange
        String fileName = "my.config.file.v2.xml";
        when(resourceFile.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - multiple dots should be preserved
        verify(warningPrinter).print(eq(fileName), contains(fileName));
    }

    /**
     * Tests that both arguments to print use the same file name.
     */
    @Test
    public void testVisitResourceFile_bothArgumentsUseSameFileName() {
        // Arrange
        String fileName = "test.xml";
        when(resourceFile.getFileName()).thenReturn(fileName);

        // Act
        duplicateResourceFilePrinter.visitResourceFile(resourceFile);

        // Assert - both the first argument and the message should contain the same file name
        verify(warningPrinter).print(
                eq(fileName),
                contains(fileName)
        );
    }
}
