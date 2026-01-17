package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter#missing(String, String, String)} method.
 * Tests missing(String, String, String) -> MethodReplacement method.
 */
public class AbstractAPIConverterClaude_missingTest {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;

    /**
     * Creates a concrete test subclass of AbstractAPIConverter for testing purposes.
     * This is necessary because AbstractAPIConverter is abstract and the missing method is protected.
     */
    private static class TestAPIConverter extends AbstractAPIConverter {
        TestAPIConverter(ClassPool programClassPool,
                        ClassPool libraryClassPool,
                        WarningPrinter warningPrinter,
                        ClassVisitor modifiedClassVisitor,
                        InstructionVisitor extraInstructionVisitor) {
            super(programClassPool, libraryClassPool, warningPrinter,
                  modifiedClassVisitor, extraInstructionVisitor);
        }

        // Expose the protected missing method for testing
        public AbstractAPIConverter.MethodReplacement testMissing(String className, String methodName, String methodDesc) {
            return missing(className, methodName, methodDesc);
        }
    }

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        warningPrinter = mock(WarningPrinter.class);
    }

    /**
     * Tests missing with standard class name, method name, and descriptor.
     * Verifies that a MissingMethodReplacement is returned.
     */
    @Test
    public void testMissingWithStandardParameters() {
        // Arrange
        String className = "java/util/stream/Stream";
        String methodName = "of";
        String methodDesc = "([Ljava/lang/Object;)Ljava/util/stream/Stream;";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null");
    }

    /**
     * Tests missing with a constructor method name.
     * Verifies that constructor names are handled.
     */
    @Test
    public void testMissingWithConstructor() {
        // Arrange
        String className = "java/util/ArrayList";
        String methodName = "<init>";
        String methodDesc = "()V";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for constructor");
    }

    /**
     * Tests missing with a static initializer method name.
     * Verifies that static initializer names are handled.
     */
    @Test
    public void testMissingWithStaticInitializer() {
        // Arrange
        String className = "java/lang/String";
        String methodName = "<clinit>";
        String methodDesc = "()V";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for static initializer");
    }

    /**
     * Tests missing with empty strings for all parameters.
     * Verifies that empty strings are handled.
     */
    @Test
    public void testMissingWithEmptyStrings() {
        // Arrange
        String className = "";
        String methodName = "";
        String methodDesc = "";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for empty strings");
    }

    /**
     * Tests missing with a method that has no parameters and returns void.
     * Verifies that simple void methods are handled.
     */
    @Test
    public void testMissingWithVoidNoArgsMethod() {
        // Arrange
        String className = "java/lang/Object";
        String methodName = "notify";
        String methodDesc = "()V";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for void method with no args");
    }

    /**
     * Tests missing with a method that has multiple parameters.
     * Verifies that complex method descriptors are handled.
     */
    @Test
    public void testMissingWithMultipleParameters() {
        // Arrange
        String className = "java/lang/String";
        String methodName = "substring";
        String methodDesc = "(II)Ljava/lang/String;";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for method with multiple parameters");
    }

    /**
     * Tests missing with a method that returns a primitive type.
     * Verifies that primitive return types are handled.
     */
    @Test
    public void testMissingWithPrimitiveReturnType() {
        // Arrange
        String className = "java/lang/String";
        String methodName = "length";
        String methodDesc = "()I";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for primitive return type");
    }

    /**
     * Tests missing with a method that has array parameters.
     * Verifies that array types in descriptors are handled.
     */
    @Test
    public void testMissingWithArrayParameters() {
        // Arrange
        String className = "java/util/Arrays";
        String methodName = "sort";
        String methodDesc = "([I)V";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for array parameters");
    }

    /**
     * Tests missing with a method that returns an array.
     * Verifies that array return types are handled.
     */
    @Test
    public void testMissingWithArrayReturnType() {
        // Arrange
        String className = "java/lang/String";
        String methodName = "getBytes";
        String methodDesc = "()[B";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for array return type");
    }

    /**
     * Tests missing with an inner class name.
     * Verifies that inner class names are handled.
     */
    @Test
    public void testMissingWithInnerClassName() {
        // Arrange
        String className = "java/util/Map$Entry";
        String methodName = "getKey";
        String methodDesc = "()Ljava/lang/Object;";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for inner class");
    }

    /**
     * Tests missing with a very long class name.
     * Verifies that long class names are handled.
     */
    @Test
    public void testMissingWithLongClassName() {
        // Arrange
        String className = "com/example/very/long/package/name/with/many/levels/MyClass";
        String methodName = "myMethod";
        String methodDesc = "()V";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for long class name");
    }

    /**
     * Tests missing with a very long method name.
     * Verifies that long method names are handled.
     */
    @Test
    public void testMissingWithLongMethodName() {
        // Arrange
        String className = "java/lang/String";
        String methodName = "veryLongMethodNameThatSomeoneDecidedToUseForSomeReason";
        String methodDesc = "()V";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for long method name");
    }

    /**
     * Tests missing with a complex descriptor with multiple object parameters.
     * Verifies that complex descriptors are handled.
     */
    @Test
    public void testMissingWithComplexDescriptor() {
        // Arrange
        String className = "java/util/HashMap";
        String methodName = "computeIfAbsent";
        String methodDesc = "(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for complex descriptor");
    }

    /**
     * Tests missing multiple times with the same parameters.
     * Verifies that the method consistently creates new instances.
     */
    @Test
    public void testMissingMultipleTimesWithSameParameters() {
        // Arrange
        String className = "java/util/stream/Stream";
        String methodName = "of";
        String methodDesc = "([Ljava/lang/Object;)Ljava/util/stream/Stream;";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result1 = converter.testMissing(className, methodName, methodDesc);
        AbstractAPIConverter.MethodReplacement result2 = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result1, "First MethodReplacement should not be null");
        assertNotNull(result2, "Second MethodReplacement should not be null");
        assertNotSame(result1, result2, "Each call should create a new MethodReplacement instance");
    }

    /**
     * Tests missing with different converters.
     * Verifies that different converter instances create independent replacements.
     */
    @Test
    public void testMissingWithDifferentConverters() {
        // Arrange
        String className = "java/util/stream/Stream";
        String methodName = "of";
        String methodDesc = "([Ljava/lang/Object;)Ljava/util/stream/Stream;";

        TestAPIConverter converter1 = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result1 = converter1.testMissing(className, methodName, methodDesc);
        AbstractAPIConverter.MethodReplacement result2 = converter2.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result1, "First MethodReplacement should not be null");
        assertNotNull(result2, "Second MethodReplacement should not be null");
        assertNotSame(result1, result2, "Different converters should create different MethodReplacement instances");
    }

    /**
     * Tests missing with a generic method descriptor.
     * Verifies that generic types in descriptors are handled.
     */
    @Test
    public void testMissingWithGenericDescriptor() {
        // Arrange
        String className = "java/util/List";
        String methodName = "get";
        String methodDesc = "(I)Ljava/lang/Object;";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for generic descriptor");
    }

    /**
     * Tests missing with varargs method descriptor.
     * Verifies that varargs methods are handled.
     */
    @Test
    public void testMissingWithVarargsMethod() {
        // Arrange
        String className = "java/lang/String";
        String methodName = "format";
        String methodDesc = "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for varargs method");
    }

    /**
     * Tests missing with all primitive parameter types.
     * Verifies that all primitive types are handled in descriptors.
     */
    @Test
    public void testMissingWithAllPrimitiveTypes() {
        // Arrange
        String className = "com/example/TestClass";
        String methodName = "testMethod";
        String methodDesc = "(BCDFIJSZ)V";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for all primitive types");
    }

    /**
     * Tests missing with special characters in method names that are valid in bytecode.
     * Verifies that method names with special valid characters are handled.
     */
    @Test
    public void testMissingWithSpecialCharactersInMethodName() {
        // Arrange
        String className = "java/lang/String";
        String methodName = "method$name_with$special_chars";
        String methodDesc = "()V";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for method name with special characters");
    }

    /**
     * Tests missing with a multidimensional array parameter.
     * Verifies that multidimensional arrays in descriptors are handled.
     */
    @Test
    public void testMissingWithMultidimensionalArray() {
        // Arrange
        String className = "java/util/Arrays";
        String methodName = "deepToString";
        String methodDesc = "([[Ljava/lang/Object;)Ljava/lang/String;";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.MethodReplacement result = converter.testMissing(className, methodName, methodDesc);

        // Assert
        assertNotNull(result, "MethodReplacement should not be null for multidimensional array");
    }
}
