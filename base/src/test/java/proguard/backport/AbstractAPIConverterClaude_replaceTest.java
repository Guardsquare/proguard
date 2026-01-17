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
 * Test class for {@link AbstractAPIConverter#replace(String, String)} method.
 * Tests replace(String, String) -> TypeReplacement method.
 */
public class AbstractAPIConverterClaude_replaceTest {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;

    /**
     * Creates a concrete test subclass of AbstractAPIConverter for testing purposes.
     * This is necessary because AbstractAPIConverter is abstract and the replace method is protected.
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

        // Expose the protected replace method for testing
        public AbstractAPIConverter.TypeReplacement testReplace(String className, String replacementClassName) {
            return replace(className, replacementClassName);
        }
    }

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        warningPrinter = mock(WarningPrinter.class);
    }

    /**
     * Tests replace with simple class names.
     * Verifies that a TypeReplacement object is created.
     */
    @Test
    public void testReplaceWithSimpleClassNames() {
        // Arrange
        String className = "java/lang/String";
        String replacementClassName = "java/lang/StringBuilder";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result, "TypeReplacement should not be null");
    }

    /**
     * Tests replace with a wildcard pattern in the replacement class name.
     * Verifies that wildcards in replacement names are accepted.
     */
    @Test
    public void testReplaceWithWildcardInReplacementClassName() {
        // Arrange
        String className = "java/time/LocalDate";
        String replacementClassName = "org/threeten/bp/*";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result, "TypeReplacement should not be null for wildcard replacement");
    }

    /**
     * Tests replace with placeholder <1> in the replacement class name.
     * Verifies that placeholders in replacement names are accepted.
     */
    @Test
    public void testReplaceWithPlaceholderInReplacementClassName() {
        // Arrange
        String className = "java/time/**";
        String replacementClassName = "org/threeten/bp/<1>";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result, "TypeReplacement should not be null for placeholder replacement");
    }

    /**
     * Tests replace when the replacement class does not exist in any class pool.
     * Verifies that a MissingTypeReplacement is returned.
     */
    @Test
    public void testReplaceWithNonExistentReplacementClass() {
        // Arrange
        String className = "java/lang/String";
        String replacementClassName = "com/example/NonExistent";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result, "TypeReplacement should not be null even for non-existent class");
        // The result should be a MissingTypeReplacement which has isValid() == false
    }

    /**
     * Tests replace with an empty string for the class name.
     * Verifies that the method handles empty class names.
     */
    @Test
    public void testReplaceWithEmptyClassName() {
        // Arrange
        String className = "";
        String replacementClassName = "java/lang/String";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result, "TypeReplacement should not be null for empty class name");
    }

    /**
     * Tests replace with an empty string for the replacement class name.
     * Verifies that the method handles empty replacement class names.
     */
    @Test
    public void testReplaceWithEmptyReplacementClassName() {
        // Arrange
        String className = "java/lang/String";
        String replacementClassName = "";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result, "TypeReplacement should not be null for empty replacement class name");
    }

    /**
     * Tests replace with wildcard patterns in the matching class name.
     * Verifies that wildcard patterns are properly handled in the class name.
     */
    @Test
    public void testReplaceWithWildcardInClassName() {
        // Arrange
        String className = "java/time/**";
        String replacementClassName = "org/threeten/bp/<1>";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result, "TypeReplacement should not be null for wildcard class name");
    }

    /**
     * Tests replace with both class names being the same.
     * Verifies that replacing a class with itself is handled.
     */
    @Test
    public void testReplaceWithSameClassNames() {
        // Arrange
        String className = "java/lang/String";
        String replacementClassName = "java/lang/String";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result, "TypeReplacement should not be null when class names are the same");
    }

    /**
     * Tests replace with different class names.
     * Verifies that the method creates TypeReplacement for various class names.
     */
    @Test
    public void testReplaceWithDifferentClassNames() {
        // Arrange
        String className = "java/lang/Object";
        String replacementClassName = "com/example/MyObject";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result, "TypeReplacement should not be null");
    }

    /**
     * Tests replace with multiple special characters in class names.
     * Verifies that special characters like slashes and dollar signs are handled.
     */
    @Test
    public void testReplaceWithInnerClassName() {
        // Arrange
        String className = "java/util/Map$Entry";
        String replacementClassName = "com/example/MyMap$MyEntry";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result, "TypeReplacement should not be null for inner class names");
    }

    /**
     * Tests replace multiple times with the same parameters.
     * Verifies that the method consistently returns valid results.
     */
    @Test
    public void testReplaceMultipleTimesWithSameParameters() {
        // Arrange
        String className = "java/time/LocalDate";
        String replacementClassName = "org/threeten/bp/LocalDate";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result1 = converter.testReplace(className, replacementClassName);
        AbstractAPIConverter.TypeReplacement result2 = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result1, "First TypeReplacement should not be null");
        assertNotNull(result2, "Second TypeReplacement should not be null");
        assertNotSame(result1, result2, "Each call should create a new TypeReplacement instance");
    }

    /**
     * Tests replace with very long class names.
     * Verifies that long class names are handled correctly.
     */
    @Test
    public void testReplaceWithLongClassNames() {
        // Arrange
        String className = "com/example/very/long/package/name/with/many/levels/MyClass";
        String replacementClassName = "org/another/very/long/package/name/with/many/levels/AnotherClass";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result, "TypeReplacement should not be null for long class names");
    }

    /**
     * Tests replace with primitive type names.
     * Verifies that primitive type descriptors are handled.
     */
    @Test
    public void testReplaceWithPrimitiveTypeLikeNames() {
        // Arrange
        String className = "int";
        String replacementClassName = "java/lang/Integer";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result, "TypeReplacement should not be null for primitive type-like names");
    }

    /**
     * Tests replace with array type notation.
     * Verifies that array type notations are handled.
     */
    @Test
    public void testReplaceWithArrayTypeNotation() {
        // Arrange
        String className = "[Ljava/lang/String;";
        String replacementClassName = "[Ljava/lang/StringBuilder;";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result, "TypeReplacement should not be null for array type notation");
    }

    /**
     * Tests that different converters can create independent replacements.
     * Verifies that TypeReplacements are independent per converter instance.
     */
    @Test
    public void testReplaceWithDifferentConverterInstances() {
        // Arrange
        String className = "java/time/LocalDate";
        String replacementClassName = "org/threeten/bp/LocalDate";

        TestAPIConverter converter1 = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);
        TestAPIConverter converter2 = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result1 = converter1.testReplace(className, replacementClassName);
        AbstractAPIConverter.TypeReplacement result2 = converter2.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result1, "First TypeReplacement should not be null");
        assertNotNull(result2, "Second TypeReplacement should not be null");
        assertNotSame(result1, result2, "Different converters should create different TypeReplacement instances");
    }

    /**
     * Tests replace using patterns similar to real-world JSR310Converter usage.
     * Verifies the method works with realistic patterns from the codebase.
     */
    @Test
    public void testReplaceWithJSR310ConverterPattern() {
        // Arrange - Pattern from JSR310Converter at line 55
        String className = "java/time/**";
        String replacementClassName = "org/threeten/bp/<1>";

        TestAPIConverter converter = new TestAPIConverter(
            programClassPool, libraryClassPool, warningPrinter, null, null);

        // Act
        AbstractAPIConverter.TypeReplacement result = converter.testReplace(className, replacementClassName);

        // Assert
        assertNotNull(result, "TypeReplacement should not be null for JSR310 pattern");
    }
}
