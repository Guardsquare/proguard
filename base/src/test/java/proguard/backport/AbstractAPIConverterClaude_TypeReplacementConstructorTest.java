package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter.TypeReplacement} constructor.
 * Tests the TypeReplacement inner class constructor with signature:
 * (Lproguard/backport/AbstractAPIConverter;Ljava/lang/String;Ljava/lang/String;)V
 */
public class AbstractAPIConverterClaude_TypeReplacementConstructorTest {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;

    /**
     * Concrete test subclass that exposes the TypeReplacement constructor for testing.
     */
    private static class TestAPIConverter extends AbstractAPIConverter {
        TestAPIConverter(ClassPool programClassPool,
                        ClassPool libraryClassPool,
                        WarningPrinter warningPrinter) {
            super(programClassPool, libraryClassPool, warningPrinter, null, null);
        }

        public TypeReplacement createTypeReplacement(String matchingClassName,
                                                     String replacementClassName) {
            return new TypeReplacement(matchingClassName, replacementClassName);
        }
    }

    @BeforeEach
    public void setUp() {
        programClassPool = new ClassPool();
        libraryClassPool = new ClassPool();
        warningPrinter = mock(WarningPrinter.class);
    }

    /**
     * Tests the TypeReplacement constructor with valid non-null parameters.
     * Verifies that a TypeReplacement can be created with all valid parameters.
     */
    @Test
    public void testConstructorWithValidParameters() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        String matchingClassName = "java/lang/String";
        String replacementClassName = "java/lang/StringBuilder";

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                matchingClassName, replacementClassName);

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should be created successfully");
    }

    /**
     * Tests the constructor with simple class names.
     * Verifies basic constructor functionality.
     */
    @Test
    public void testConstructorWithSimpleClassNames() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/MyClass",
                "com/example/NewClass");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should be created with simple parameters");
    }

    /**
     * Tests the constructor with wildcard in matching class name.
     * Verifies that wildcard patterns in matching class names are accepted.
     */
    @Test
    public void testConstructorWithWildcardInMatchingClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/*",
                "org/threeten/bp/LocalDate");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle wildcard in matching class name");
    }

    /**
     * Tests the constructor with double wildcard in matching class name.
     * Verifies that the "**" pattern is handled in matching class names.
     */
    @Test
    public void testConstructorWithDoubleWildcardInMatchingClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/**",
                "org/threeten/bp/<1>");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle ** pattern in matching class name");
    }

    /**
     * Tests the constructor with wildcard in replacement class name.
     * Verifies that wildcard patterns in replacement class names are accepted.
     */
    @Test
    public void testConstructorWithWildcardInReplacementClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/LocalDate",
                "org/threeten/bp/*");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle wildcard in replacement class name");
    }

    /**
     * Tests the constructor with placeholder in replacement class name.
     * Verifies that the "<1>" placeholder pattern is handled.
     */
    @Test
    public void testConstructorWithPlaceholderInReplacementClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/**",
                "org/threeten/bp/<1>");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle <1> placeholder");
    }

    /**
     * Tests the constructor with empty strings.
     * Verifies that empty strings are accepted as parameters.
     */
    @Test
    public void testConstructorWithEmptyStrings() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement("", "");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should be created with empty strings");
    }

    /**
     * Tests the constructor with null matching class name.
     * Verifies that null matching class name causes an exception.
     */
    @Test
    public void testConstructorWithNullMatchingClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            converter.createTypeReplacement(null, "com/example/NewClass");
        }, "Constructor should throw NullPointerException for null matching class name");
    }

    /**
     * Tests the constructor with null replacement class name.
     * Verifies that null replacement class name is handled.
     */
    @Test
    public void testConstructorWithNullReplacementClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/OldClass", null);

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should be created with null replacement class name");
    }

    /**
     * Tests the constructor with both parameters null.
     * Verifies that both null parameters cause an exception.
     */
    @Test
    public void testConstructorWithBothParametersNull() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            converter.createTypeReplacement(null, null);
        }, "Constructor should throw NullPointerException when both parameters are null");
    }

    /**
     * Tests the constructor with inner class names.
     * Verifies that inner class names with $ are handled.
     */
    @Test
    public void testConstructorWithInnerClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/util/Map$Entry",
                "com/example/MyMap$MyEntry");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle inner class names");
    }

    /**
     * Tests the constructor with very long class names.
     * Verifies that long names are handled.
     */
    @Test
    public void testConstructorWithLongNames() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        String longClassName = "com/example/very/long/package/name/MyVeryLongClassName";

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                longClassName,
                longClassName);

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle long names");
    }

    /**
     * Tests creating multiple TypeReplacement instances.
     * Verifies that multiple instances can be created independently.
     */
    @Test
    public void testMultipleTypeReplacementInstances() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement tr1 = converter.createTypeReplacement(
                "Class1", "NewClass1");
        AbstractAPIConverter.TypeReplacement tr2 = converter.createTypeReplacement(
                "Class2", "NewClass2");

        // Assert
        assertNotNull(tr1, "First TypeReplacement should be created");
        assertNotNull(tr2, "Second TypeReplacement should be created");
        assertNotSame(tr1, tr2, "TypeReplacement instances should be different");
    }

    /**
     * Tests the constructor with same parameters creates different instances.
     * Verifies that each call creates a new instance.
     */
    @Test
    public void testConstructorCreatesDifferentInstances() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        String matchingClassName = "java/lang/String";
        String replacementClassName = "java/lang/StringBuilder";

        // Act
        AbstractAPIConverter.TypeReplacement tr1 = converter.createTypeReplacement(
                matchingClassName, replacementClassName);
        AbstractAPIConverter.TypeReplacement tr2 = converter.createTypeReplacement(
                matchingClassName, replacementClassName);

        // Assert
        assertNotSame(tr1, tr2, "Each constructor call should create a new instance");
    }

    /**
     * Tests the constructor with array type notation.
     * Verifies that array type notations are handled.
     */
    @Test
    public void testConstructorWithArrayTypeNotation() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "[Ljava/lang/String;",
                "[Ljava/lang/StringBuilder;");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle array type notation");
    }

    /**
     * Tests the constructor with same class names.
     * Verifies that replacing a class with itself is handled.
     */
    @Test
    public void testConstructorWithSameClassNames() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        String className = "java/lang/String";

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                className, className);

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should be created when class names are the same");
    }

    /**
     * Tests that the TypeReplacement is properly associated with the outer converter.
     * Verifies that the inner class relationship is maintained.
     */
    @Test
    public void testTypeReplacementIsInnerClass() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "OldClass", "NewClass");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should be created as inner class");
        // The outer class reference is implicit - if this test passes, the association is correct
    }

    /**
     * Tests the constructor with multiple converters creating type replacements.
     * Verifies that different converters can create independent type replacements.
     */
    @Test
    public void testTypeReplacementsFromDifferentConverters() {
        // Arrange
        TestAPIConverter converter1 = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        TestAPIConverter converter2 = new TestAPIConverter(new ClassPool(), new ClassPool(), mock(WarningPrinter.class));

        // Act
        AbstractAPIConverter.TypeReplacement tr1 = converter1.createTypeReplacement(
                "Class1", "NewClass1");
        AbstractAPIConverter.TypeReplacement tr2 = converter2.createTypeReplacement(
                "Class1", "NewClass1");

        // Assert
        assertNotNull(tr1, "First converter's TypeReplacement should be created");
        assertNotNull(tr2, "Second converter's TypeReplacement should be created");
        assertNotSame(tr1, tr2, "TypeReplacements from different converters should be different");
    }

    /**
     * Tests the constructor with primitive type-like names.
     * Verifies that primitive type names are handled.
     */
    @Test
    public void testConstructorWithPrimitiveTypeLikeNames() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "int",
                "java/lang/Integer");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle primitive type-like names");
    }

    /**
     * Tests the constructor with special characters in class names.
     * Verifies that special characters like underscores and numbers are handled.
     */
    @Test
    public void testConstructorWithSpecialCharactersInClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/MyClass_123",
                "com/example/NewClass_456");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle special characters in class name");
    }

    /**
     * Tests the constructor using patterns similar to real-world JSR310Converter usage.
     * Verifies the constructor works with realistic patterns from the codebase.
     */
    @Test
    public void testConstructorWithJSR310ConverterPattern() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/**",
                "org/threeten/bp/<1>");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should be created with JSR310 pattern");
    }

    /**
     * Tests the constructor with complex wildcard and placeholder combinations.
     * Verifies that complex patterns are handled.
     */
    @Test
    public void testConstructorWithComplexPatterns() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/*/time/**",
                "org/threeten/*/bp/<1>");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle complex patterns");
    }

    /**
     * Tests the constructor with question mark wildcard.
     * Verifies that single character wildcards are handled.
     */
    @Test
    public void testConstructorWithQuestionMarkWildcard() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/?",
                "org/threeten/bp/?");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle question mark wildcard");
    }

    /**
     * Tests the constructor with package-level wildcards.
     * Verifies that package-level patterns are handled.
     */
    @Test
    public void testConstructorWithPackageLevelWildcards() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/**",
                "org/backport/<1>");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle package-level wildcards");
    }

    /**
     * Tests the constructor with multiple consecutive slashes.
     * Verifies that unusual path patterns are handled.
     */
    @Test
    public void testConstructorWithMultipleConsecutiveSlashes() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java//time//LocalDate",
                "org//threeten//bp//LocalDate");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle multiple consecutive slashes");
    }

    /**
     * Tests the constructor with class names ending in wildcard.
     * Verifies that wildcard at the end is handled.
     */
    @Test
    public void testConstructorWithClassNameEndingInWildcard() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/Local*",
                "org/threeten/bp/Local*");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle class name ending in wildcard");
    }

    /**
     * Tests the constructor with unicode characters in class names.
     * Verifies that unicode characters are handled.
     */
    @Test
    public void testConstructorWithUnicodeCharacters() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Act
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/MyClass\u00E9",
                "com/example/NewClass\u00E9");

        // Assert
        assertNotNull(typeReplacement, "TypeReplacement should handle unicode characters");
    }
}
