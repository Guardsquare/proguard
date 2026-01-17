package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.ProgramClass;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter.TypeReplacement#isValid()} method.
 * Tests the isValid() method of the TypeReplacement inner class.
 */
public class AbstractAPIConverterClaude_isValidTest {

    private ClassPool programClassPool;
    private ClassPool libraryClassPool;
    private WarningPrinter warningPrinter;

    /**
     * Concrete test subclass that exposes the TypeReplacement for testing.
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
     * Tests isValid returns true when replacement class name contains asterisk wildcard.
     * The wildcard indicates a pattern-based replacement, which is always valid.
     */
    @Test
    public void testIsValidReturnsTrueForWildcardInReplacementClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/LocalDate",
                "org/threeten/bp/*");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true when replacement class name contains *");
    }

    /**
     * Tests isValid returns true when replacement class name contains placeholder.
     * The placeholder <1> is used for dynamic replacements.
     */
    @Test
    public void testIsValidReturnsTrueForPlaceholderInReplacementClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/**",
                "org/threeten/bp/<1>");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true when replacement class name contains <1>");
    }

    /**
     * Tests isValid returns true when replacement class exists in program class pool.
     * If the replacement class can be found, the replacement is valid.
     */
    @Test
    public void testIsValidReturnsTrueWhenReplacementClassExistsInProgramPool() {
        // Arrange
        programClassPool.addClass(createProgramClass("com/example/ReplacementClass"));

        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/OldClass",
                "com/example/ReplacementClass");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true when replacement class exists in program class pool");
    }

    /**
     * Tests isValid returns true when replacement class exists in library class pool.
     * Library classes are also valid targets for replacement.
     */
    @Test
    public void testIsValidReturnsTrueWhenReplacementClassExistsInLibraryPool() {
        // Arrange
        libraryClassPool.addClass(createProgramClass("com/example/LibraryReplacementClass"));

        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/OldClass",
                "com/example/LibraryReplacementClass");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true when replacement class exists in library class pool");
    }

    /**
     * Tests isValid returns false when replacement class does not exist in any pool.
     * Without a valid target class and no wildcards/placeholders, the replacement is invalid.
     */
    @Test
    public void testIsValidReturnsFalseWhenReplacementClassDoesNotExist() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/OldClass",
                "com/example/NonExistentClass");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertFalse(result, "isValid should return false when replacement class does not exist");
    }

    /**
     * Tests isValid returns true for wildcard at the end of class name.
     * Verifies that wildcards work regardless of position.
     */
    @Test
    public void testIsValidReturnsTrueForWildcardAtEnd() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/LocalDate",
                "org/threeten/bp/Local*");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true for wildcard at end of class name");
    }

    /**
     * Tests isValid returns true for wildcard in the middle of class name.
     * Wildcards can appear anywhere in the replacement class name.
     */
    @Test
    public void testIsValidReturnsTrueForWildcardInMiddle() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/LocalDate",
                "org/*/bp/LocalDate");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true for wildcard in middle of class name");
    }

    /**
     * Tests isValid returns true for double wildcard pattern.
     * The ** pattern is commonly used for package-level replacements.
     */
    @Test
    public void testIsValidReturnsTrueForDoubleWildcard() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/**",
                "org/threeten/bp/**");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true for double wildcard pattern");
    }

    /**
     * Tests isValid returns true for multiple wildcards in replacement class name.
     * Multiple wildcards are valid pattern indicators.
     */
    @Test
    public void testIsValidReturnsTrueForMultipleWildcards() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/LocalDate",
                "org/*/bp/*");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true for multiple wildcards");
    }

    /**
     * Tests isValid returns true for placeholder at the end of class name.
     * Placeholders can appear anywhere in the replacement class name.
     */
    @Test
    public void testIsValidReturnsTrueForPlaceholderAtEnd() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/LocalDate",
                "org/threeten/bp/<1>");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true for placeholder at end");
    }

    /**
     * Tests isValid returns true for placeholder in the middle of class name.
     * The <1> placeholder is recognized anywhere in the string.
     */
    @Test
    public void testIsValidReturnsTrueForPlaceholderInMiddle() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/LocalDate",
                "org/<1>/bp/LocalDate");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true for placeholder in middle");
    }

    /**
     * Tests isValid returns true when both wildcard and placeholder are present.
     * Combined patterns are valid.
     */
    @Test
    public void testIsValidReturnsTrueForWildcardAndPlaceholder() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/**",
                "org/*/bp/<1>");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true when both wildcard and placeholder are present");
    }

    /**
     * Tests isValid returns false for empty replacement class name.
     * An empty string is not a valid replacement target.
     */
    @Test
    public void testIsValidReturnsFalseForEmptyReplacementClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/OldClass",
                "");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertFalse(result, "isValid should return false for empty replacement class name");
    }

    /**
     * Tests isValid returns true when replacement class exists even with complex name.
     * Classes with special characters like $ (inner classes) should be found.
     */
    @Test
    public void testIsValidReturnsTrueForInnerClassInPool() {
        // Arrange
        programClassPool.addClass(createProgramClass("com/example/OuterClass$InnerClass"));

        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/OldClass",
                "com/example/OuterClass$InnerClass");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true for inner class in pool");
    }

    /**
     * Tests isValid with the actual JSR310Converter pattern.
     * Verifies the method works with real-world usage patterns.
     */
    @Test
    public void testIsValidWithJSR310ConverterPattern() {
        // Arrange - Pattern from JSR310Converter
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/**",
                "org/threeten/bp/<1>");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true for JSR310Converter pattern");
    }

    /**
     * Tests isValid returns false when class name doesn't have wildcards or placeholders
     * and doesn't exist in either pool.
     */
    @Test
    public void testIsValidReturnsFalseForNonPatternNonExistentClass() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/lang/String",
                "com/nonexistent/MyString");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertFalse(result, "isValid should return false for non-pattern non-existent class");
    }

    /**
     * Tests isValid returns true even when program pool has the class with same name.
     * Priority doesn't matter for validity check.
     */
    @Test
    public void testIsValidReturnsTrueWhenClassExistsInBothPools() {
        // Arrange
        String className = "com/example/DuplicateClass";
        programClassPool.addClass(createProgramClass(className));
        libraryClassPool.addClass(createProgramClass(className));

        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/OldClass",
                className);

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true when class exists in both pools");
    }

    /**
     * Tests isValid with very long class name containing wildcards.
     * Long class names with patterns should still be valid.
     */
    @Test
    public void testIsValidReturnsTrueForLongClassNameWithWildcard() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/very/long/package/name/OriginalClass",
                "org/another/very/long/package/name/*/ReplacementClass");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertTrue(result, "isValid should return true for long class name with wildcard");
    }

    /**
     * Tests isValid returns false for class name that looks like a wildcard but isn't.
     * Only actual * characters are recognized as wildcards.
     */
    @Test
    public void testIsValidReturnsFalseForPseudoWildcard() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/OldClass",
                "com/example/StarInName");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertFalse(result, "isValid should return false when class doesn't exist and has no actual wildcard");
    }

    /**
     * Tests isValid returns false for class with similar placeholder-like string.
     * Only the exact string "<1>" is recognized as a placeholder.
     */
    @Test
    public void testIsValidReturnsFalseForPseudoPlaceholder() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/OldClass",
                "com/example/Class<2>");

        // Act
        boolean result = typeReplacement.isValid();

        // Assert
        assertFalse(result, "isValid should return false for placeholder other than <1>");
    }

    /**
     * Helper method to create a minimal ProgramClass.
     * Used to populate class pools for testing.
     */
    private ProgramClass createProgramClass(String className) {
        ProgramClass programClass = mock(ProgramClass.class);
        when(programClass.getName()).thenReturn(className);
        return programClass;
    }
}
