package proguard.backport;

import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link JSR310Converter}.
 * Tests the constructor:
 * <init>.(Lproguard/classfile/ClassPool;Lproguard/classfile/ClassPool;Lproguard/classfile/util/WarningPrinter;Lproguard/classfile/visitor/ClassVisitor;Lproguard/classfile/instruction/visitor/InstructionVisitor;)V
 */
public class JSR310ConverterClaudeTest {

    /**
     * Tests the constructor with all valid non-null parameters.
     * Verifies that a JSR310Converter can be instantiated with valid parameters.
     */
    @Test
    public void testConstructorWithAllValidParameters() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        JSR310Converter converter = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Assert
        assertNotNull(converter, "JSR310Converter should be created successfully");
    }

    /**
     * Tests the constructor with null modifiedClassVisitor.
     * Verifies that null modifiedClassVisitor is accepted.
     */
    @Test
    public void testConstructorWithNullModifiedClassVisitor() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        JSR310Converter converter = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null,
            extraInstructionVisitor
        );

        // Assert
        assertNotNull(converter, "JSR310Converter should be created with null modifiedClassVisitor");
    }

    /**
     * Tests the constructor with null extraInstructionVisitor.
     * Verifies that null extraInstructionVisitor is accepted.
     */
    @Test
    public void testConstructorWithNullExtraInstructionVisitor() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

        // Act
        JSR310Converter converter = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            null
        );

        // Assert
        assertNotNull(converter, "JSR310Converter should be created with null extraInstructionVisitor");
    }

    /**
     * Tests the constructor with both optional visitors as null.
     * Verifies that both optional parameters can be null.
     */
    @Test
    public void testConstructorWithBothOptionalVisitorsNull() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);

        // Act
        JSR310Converter converter = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null,
            null
        );

        // Assert
        assertNotNull(converter, "JSR310Converter should be created with both optional visitors null");
    }

    /**
     * Tests the constructor with empty class pools.
     * Verifies that empty class pools are accepted.
     */
    @Test
    public void testConstructorWithEmptyClassPools() {
        // Arrange
        ClassPool emptyProgramPool = new ClassPool();
        ClassPool emptyLibraryPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        JSR310Converter converter = new JSR310Converter(
            emptyProgramPool,
            emptyLibraryPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Assert
        assertNotNull(converter, "JSR310Converter should be created with empty class pools");
    }

    /**
     * Tests that multiple instances of JSR310Converter can be created independently.
     * Verifies that each instance is independent.
     */
    @Test
    public void testMultipleConverterInstances() {
        // Arrange
        ClassPool programClassPool1 = new ClassPool();
        ClassPool libraryClassPool1 = new ClassPool();
        StringWriter stringWriter1 = new StringWriter();
        PrintWriter printWriter1 = new PrintWriter(stringWriter1);
        WarningPrinter warningPrinter1 = new WarningPrinter(printWriter1);
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        ClassPool programClassPool2 = new ClassPool();
        ClassPool libraryClassPool2 = new ClassPool();
        StringWriter stringWriter2 = new StringWriter();
        PrintWriter printWriter2 = new PrintWriter(stringWriter2);
        WarningPrinter warningPrinter2 = new WarningPrinter(printWriter2);

        // Act
        JSR310Converter converter1 = new JSR310Converter(
            programClassPool1,
            libraryClassPool1,
            warningPrinter1,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        JSR310Converter converter2 = new JSR310Converter(
            programClassPool2,
            libraryClassPool2,
            warningPrinter2,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Assert
        assertNotNull(converter1, "First converter should be created");
        assertNotNull(converter2, "Second converter should be created");
        assertNotSame(converter1, converter2, "Converters should be different instances");
    }

    /**
     * Tests the constructor with same parameters creates different instances.
     * Verifies that each constructor call creates a new instance.
     */
    @Test
    public void testConstructorCreatesDifferentInstances() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        JSR310Converter converter1 = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        JSR310Converter converter2 = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Assert
        assertNotSame(converter1, converter2, "Each constructor call should create a new instance");
    }

    /**
     * Tests that the converter is an instance of AbstractAPIConverter.
     * Verifies the inheritance hierarchy.
     */
    @Test
    public void testConverterExtendsAbstractAPIConverter() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        JSR310Converter converter = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Assert
        assertTrue(converter instanceof AbstractAPIConverter,
                   "JSR310Converter should extend AbstractAPIConverter");
    }

    /**
     * Tests that the converter implements ClassVisitor interface.
     * Verifies that it can be used as a ClassVisitor.
     */
    @Test
    public void testConverterImplementsClassVisitor() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        JSR310Converter converter = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Assert
        assertTrue(converter instanceof ClassVisitor,
                   "JSR310Converter should implement ClassVisitor");
    }

    /**
     * Tests that the constructor doesn't invoke any methods on the visitor parameters.
     * Verifies that the constructor only stores the visitors without using them.
     */
    @Test
    public void testConstructorDoesNotInvokeVisitors() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        JSR310Converter converter = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Assert
        assertNotNull(converter, "Converter should be created");
        verifyNoInteractions(modifiedClassVisitor);
        verifyNoInteractions(extraInstructionVisitor);
    }

    /**
     * Tests that the constructor completes quickly.
     * Verifies that the constructor is efficient and doesn't perform heavy operations.
     */
    @Test
    public void testConstructorIsEfficient() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);
        long startTime = System.nanoTime();

        // Act
        JSR310Converter converter = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Assert
        long duration = System.nanoTime() - startTime;
        assertNotNull(converter, "Converter should be created");
        // Constructor should complete in less than 10 milliseconds
        assertTrue(duration < 10_000_000L,
            "Constructor should complete quickly (took " + duration + " ns)");
    }

    /**
     * Tests creating multiple converters with the same visitors.
     * Verifies that multiple instances can be created using the same visitor objects.
     */
    @Test
    public void testMultipleConvertersWithSameVisitors() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);
        InstructionVisitor extraInstructionVisitor = mock(InstructionVisitor.class);

        // Act
        JSR310Converter converter1 = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        JSR310Converter converter2 = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor
        );

        // Assert
        assertNotNull(converter1, "First converter should be created");
        assertNotNull(converter2, "Second converter should be created");
        assertNotSame(converter1, converter2, "Converter instances should be different");
    }

    /**
     * Tests creating converters with different visitor combinations.
     * Verifies that converters can be created with different configurations.
     */
    @Test
    public void testConvertersWithDifferentVisitorCombinations() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);
        ClassVisitor classVisitor1 = mock(ClassVisitor.class);
        ClassVisitor classVisitor2 = mock(ClassVisitor.class);
        InstructionVisitor instructionVisitor1 = mock(InstructionVisitor.class);
        InstructionVisitor instructionVisitor2 = mock(InstructionVisitor.class);

        // Act
        JSR310Converter converter1 = new JSR310Converter(
            programClassPool, libraryClassPool, warningPrinter,
            classVisitor1, instructionVisitor1
        );

        JSR310Converter converter2 = new JSR310Converter(
            programClassPool, libraryClassPool, warningPrinter,
            classVisitor2, instructionVisitor2
        );

        // Assert
        assertNotNull(converter1, "First converter should be created");
        assertNotNull(converter2, "Second converter should be created");
        assertNotSame(converter1, converter2, "Converter instances should be different");
    }

    /**
     * Tests the constructor with shared class pools but different visitors.
     * Verifies that converters can share class pools.
     */
    @Test
    public void testConstructorWithSharedClassPools() {
        // Arrange
        ClassPool sharedProgramPool = new ClassPool();
        ClassPool sharedLibraryPool = new ClassPool();
        StringWriter stringWriter1 = new StringWriter();
        PrintWriter printWriter1 = new PrintWriter(stringWriter1);
        WarningPrinter warningPrinter1 = new WarningPrinter(printWriter1);
        StringWriter stringWriter2 = new StringWriter();
        PrintWriter printWriter2 = new PrintWriter(stringWriter2);
        WarningPrinter warningPrinter2 = new WarningPrinter(printWriter2);
        ClassVisitor classVisitor1 = mock(ClassVisitor.class);
        ClassVisitor classVisitor2 = mock(ClassVisitor.class);

        // Act
        JSR310Converter converter1 = new JSR310Converter(
            sharedProgramPool, sharedLibraryPool, warningPrinter1,
            classVisitor1, null
        );

        JSR310Converter converter2 = new JSR310Converter(
            sharedProgramPool, sharedLibraryPool, warningPrinter2,
            classVisitor2, null
        );

        // Assert
        assertNotNull(converter1, "First converter should be created");
        assertNotNull(converter2, "Second converter should be created");
        assertNotSame(converter1, converter2, "Converter instances should be different");
    }

    /**
     * Tests the constructor with all parameters null except class pools and warning printer.
     * Verifies that only the required parameters need to be non-null.
     */
    @Test
    public void testConstructorWithOnlyRequiredParameters() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);

        // Act
        JSR310Converter converter = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null,
            null
        );

        // Assert
        assertNotNull(converter, "JSR310Converter should be created with only required parameters");
    }

    /**
     * Tests that converter can be assigned to AbstractAPIConverter reference.
     * Verifies polymorphic behavior.
     */
    @Test
    public void testConverterAsAbstractAPIConverter() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);

        // Act
        AbstractAPIConverter converter = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null,
            null
        );

        // Assert
        assertNotNull(converter, "JSR310Converter should be assignable to AbstractAPIConverter");
    }

    /**
     * Tests that converter can be assigned to ClassVisitor reference.
     * Verifies interface implementation.
     */
    @Test
    public void testConverterAsClassVisitor() {
        // Arrange
        ClassPool programClassPool = new ClassPool();
        ClassPool libraryClassPool = new ClassPool();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        WarningPrinter warningPrinter = new WarningPrinter(printWriter);

        // Act
        ClassVisitor converter = new JSR310Converter(
            programClassPool,
            libraryClassPool,
            warningPrinter,
            null,
            null
        );

        // Assert
        assertNotNull(converter, "JSR310Converter should be assignable to ClassVisitor");
    }
}
