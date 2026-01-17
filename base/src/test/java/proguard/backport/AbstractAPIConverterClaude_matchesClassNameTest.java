package proguard.backport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.util.WarningPrinter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AbstractAPIConverter.TypeReplacement#matchesClassName(String)} method.
 * Tests the matchesClassName method of the TypeReplacement inner class.
 */
public class AbstractAPIConverterClaude_matchesClassNameTest {

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
     * Tests matchesClassName returns true for exact match.
     * When the matching pattern is a specific class name, it should match exactly.
     */
    @Test
    public void testMatchesClassNameReturnsTrueForExactMatch() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/lang/String",
                "java/lang/StringBuilder");

        // Act
        boolean result = typeReplacement.matchesClassName("java/lang/String");

        // Assert
        assertTrue(result, "matchesClassName should return true for exact match");
    }

    /**
     * Tests matchesClassName returns false when class name doesn't match.
     * Different class names should not match.
     */
    @Test
    public void testMatchesClassNameReturnsFalseForNonMatch() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/lang/String",
                "java/lang/StringBuilder");

        // Act
        boolean result = typeReplacement.matchesClassName("java/lang/Integer");

        // Assert
        assertFalse(result, "matchesClassName should return false for non-matching class");
    }

    /**
     * Tests matchesClassName with double wildcard pattern matching nested packages.
     * The ** pattern should match all classes in a package hierarchy.
     */
    @Test
    public void testMatchesClassNameWithDoubleWildcardMatchesNestedPackages() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/**",
                "org/threeten/bp/<1>");

        // Act & Assert
        assertTrue(typeReplacement.matchesClassName("java/time/LocalDate"),
                "Should match direct child");
        assertTrue(typeReplacement.matchesClassName("java/time/format/DateTimeFormatter"),
                "Should match nested child");
        assertTrue(typeReplacement.matchesClassName("java/time/temporal/ChronoUnit"),
                "Should match deeply nested child");
    }

    /**
     * Tests matchesClassName with double wildcard doesn't match outside package.
     * Classes outside the specified package should not match.
     */
    @Test
    public void testMatchesClassNameWithDoubleWildcardDoesNotMatchOutsidePackage() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/**",
                "org/threeten/bp/<1>");

        // Act & Assert
        assertFalse(typeReplacement.matchesClassName("java/lang/String"),
                "Should not match class in different package");
        assertFalse(typeReplacement.matchesClassName("java/util/Date"),
                "Should not match class in sibling package");
        assertFalse(typeReplacement.matchesClassName("org/time/LocalDate"),
                "Should not match class with similar but different package");
    }

    /**
     * Tests matchesClassName with pattern ending in double wildcard.
     * Patterns like "Optional**" should match Optional and OptionalInt, OptionalDouble, etc.
     */
    @Test
    public void testMatchesClassNameWithTrailingDoubleWildcard() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/util/Optional**",
                "java8/util/Optional<1>");

        // Act & Assert
        assertTrue(typeReplacement.matchesClassName("java/util/Optional"),
                "Should match base class");
        assertTrue(typeReplacement.matchesClassName("java/util/OptionalInt"),
                "Should match with suffix");
        assertTrue(typeReplacement.matchesClassName("java/util/OptionalDouble"),
                "Should match with different suffix");
        assertTrue(typeReplacement.matchesClassName("java/util/OptionalLong"),
                "Should match with another suffix");
    }

    /**
     * Tests matchesClassName with trailing double wildcard doesn't match unrelated classes.
     * Only classes starting with the prefix should match.
     */
    @Test
    public void testMatchesClassNameWithTrailingDoubleWildcardDoesNotMatchUnrelated() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/util/Optional**",
                "java8/util/Optional<1>");

        // Act & Assert
        assertFalse(typeReplacement.matchesClassName("java/util/ArrayList"),
                "Should not match unrelated class in same package");
        assertFalse(typeReplacement.matchesClassName("java/util/Opt"),
                "Should not match partial prefix");
        assertFalse(typeReplacement.matchesClassName("java/util/MyOptional"),
                "Should not match class with prefix in middle");
    }

    /**
     * Tests matchesClassName with single wildcard pattern.
     * Single wildcard should match a single package level or class name component.
     */
    @Test
    public void testMatchesClassNameWithSingleWildcard() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/*/Date",
                "java8/*/Date");

        // Act & Assert
        assertTrue(typeReplacement.matchesClassName("java/util/Date"),
                "Should match with single package level");
        assertTrue(typeReplacement.matchesClassName("java/sql/Date"),
                "Should match with different single package level");
    }

    /**
     * Tests matchesClassName with single wildcard doesn't match multiple levels.
     * Single wildcard should not match nested packages.
     */
    @Test
    public void testMatchesClassNameWithSingleWildcardDoesNotMatchMultipleLevels() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/*/Date",
                "java8/*/Date");

        // Act & Assert
        assertFalse(typeReplacement.matchesClassName("java/util/time/Date"),
                "Should not match multiple levels with single wildcard");
        assertFalse(typeReplacement.matchesClassName("java/Date"),
                "Should not match when missing a level");
    }

    /**
     * Tests matchesClassName with wildcard at end of class name.
     * Patterns like "Local*" should match classes starting with "Local".
     */
    @Test
    public void testMatchesClassNameWithWildcardAtEnd() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/Local*",
                "org/threeten/bp/Local*");

        // Act & Assert
        assertTrue(typeReplacement.matchesClassName("java/time/LocalDate"),
                "Should match LocalDate");
        assertTrue(typeReplacement.matchesClassName("java/time/LocalTime"),
                "Should match LocalTime");
        assertTrue(typeReplacement.matchesClassName("java/time/LocalDateTime"),
                "Should match LocalDateTime");
    }

    /**
     * Tests matchesClassName with wildcard at end doesn't match different prefix.
     * Only classes with the specified prefix should match.
     */
    @Test
    public void testMatchesClassNameWithWildcardAtEndDoesNotMatchDifferentPrefix() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/Local*",
                "org/threeten/bp/Local*");

        // Act & Assert
        assertFalse(typeReplacement.matchesClassName("java/time/Instant"),
                "Should not match class with different prefix");
        assertFalse(typeReplacement.matchesClassName("java/time/Duration"),
                "Should not match another class with different prefix");
    }

    /**
     * Tests matchesClassName with wildcard in middle of pattern.
     * Patterns can have wildcards in any position.
     */
    @Test
    public void testMatchesClassNameWithWildcardInMiddle() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/util/*/Iterator",
                "java8/util/*/Iterator");

        // Act & Assert
        assertTrue(typeReplacement.matchesClassName("java/util/stream/Iterator"),
                "Should match with wildcard in middle");
        assertTrue(typeReplacement.matchesClassName("java/util/function/Iterator"),
                "Should match with different middle component");
    }

    /**
     * Tests matchesClassName with question mark wildcard.
     * Question mark matches a single character.
     */
    @Test
    public void testMatchesClassNameWithQuestionMarkWildcard() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/util/List?",
                "java8/util/List?");

        // Act & Assert
        assertTrue(typeReplacement.matchesClassName("java/util/Lists"),
                "Should match with single character");
    }

    /**
     * Tests matchesClassName with question mark doesn't match multiple characters.
     * Question mark should only match exactly one character.
     */
    @Test
    public void testMatchesClassNameWithQuestionMarkDoesNotMatchMultipleChars() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/util/List?",
                "java8/util/List?");

        // Act & Assert
        assertFalse(typeReplacement.matchesClassName("java/util/ListIterator"),
                "Should not match multiple characters");
        assertFalse(typeReplacement.matchesClassName("java/util/List"),
                "Should not match zero characters");
    }

    /**
     * Tests matchesClassName with inner class names.
     * Inner classes with $ should be matched correctly.
     */
    @Test
    public void testMatchesClassNameWithInnerClass() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/util/Map$Entry",
                "java8/util/Map$Entry");

        // Act
        boolean result = typeReplacement.matchesClassName("java/util/Map$Entry");

        // Assert
        assertTrue(result, "Should match inner class with $ separator");
    }

    /**
     * Tests matchesClassName with inner class pattern using wildcards.
     * Wildcards should work with inner class patterns.
     */
    @Test
    public void testMatchesClassNameWithInnerClassWildcard() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/util/Map$*",
                "java8/util/Map$*");

        // Act & Assert
        assertTrue(typeReplacement.matchesClassName("java/util/Map$Entry"),
                "Should match inner class Entry");
        assertTrue(typeReplacement.matchesClassName("java/util/Map$Node"),
                "Should match inner class Node");
    }

    /**
     * Tests matchesClassName is case-sensitive.
     * Class names should match case-sensitively.
     */
    @Test
    public void testMatchesClassNameIsCaseSensitive() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/lang/String",
                "java8/lang/String");

        // Act & Assert
        assertTrue(typeReplacement.matchesClassName("java/lang/String"),
                "Should match with exact case");
        assertFalse(typeReplacement.matchesClassName("java/lang/string"),
                "Should not match with different case");
        assertFalse(typeReplacement.matchesClassName("JAVA/LANG/STRING"),
                "Should not match with all uppercase");
    }

    /**
     * Tests matchesClassName with empty matching pattern.
     * Empty patterns should have defined behavior.
     */
    @Test
    public void testMatchesClassNameWithEmptyPattern() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "",
                "java8/util/String");

        // Act
        boolean result = typeReplacement.matchesClassName("java/lang/String");

        // Assert
        assertFalse(result, "Empty pattern should not match non-empty class name");
    }

    /**
     * Tests matchesClassName with empty class name to match.
     * Matching against empty string should have defined behavior.
     */
    @Test
    public void testMatchesClassNameWithEmptyClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/lang/String",
                "java8/lang/String");

        // Act
        boolean result = typeReplacement.matchesClassName("");

        // Assert
        assertFalse(result, "Non-empty pattern should not match empty class name");
    }

    /**
     * Tests matchesClassName with real JSR310Converter pattern.
     * Verifies the method works with actual converter patterns.
     */
    @Test
    public void testMatchesClassNameWithJSR310ConverterPattern() {
        // Arrange - Pattern from JSR310Converter
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/**",
                "org/threeten/bp/<1>");

        // Act & Assert - Test various java.time classes
        assertTrue(typeReplacement.matchesClassName("java/time/LocalDate"),
                "Should match LocalDate");
        assertTrue(typeReplacement.matchesClassName("java/time/LocalTime"),
                "Should match LocalTime");
        assertTrue(typeReplacement.matchesClassName("java/time/ZonedDateTime"),
                "Should match ZonedDateTime");
        assertTrue(typeReplacement.matchesClassName("java/time/format/DateTimeFormatter"),
                "Should match nested format classes");
        assertFalse(typeReplacement.matchesClassName("java/util/Date"),
                "Should not match non-time classes");
    }

    /**
     * Tests matchesClassName with real StreamSupportConverter patterns.
     * Verifies the method works with various real-world patterns.
     */
    @Test
    public void testMatchesClassNameWithStreamSupportConverterPatterns() {
        // Arrange - Patterns from StreamSupportConverter
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);

        // Test java/util/stream/** pattern
        AbstractAPIConverter.TypeReplacement streamReplacement = converter.createTypeReplacement(
                "java/util/stream/**",
                "java8/util/stream/<1>");
        assertTrue(streamReplacement.matchesClassName("java/util/stream/Stream"),
                "Should match Stream class");
        assertTrue(streamReplacement.matchesClassName("java/util/stream/Collectors"),
                "Should match Collectors class");

        // Test java/util/Optional** pattern
        AbstractAPIConverter.TypeReplacement optionalReplacement = converter.createTypeReplacement(
                "java/util/Optional**",
                "java8/util/Optional<1>");
        assertTrue(optionalReplacement.matchesClassName("java/util/Optional"),
                "Should match Optional");
        assertTrue(optionalReplacement.matchesClassName("java/util/OptionalInt"),
                "Should match OptionalInt");
        assertTrue(optionalReplacement.matchesClassName("java/util/OptionalDouble"),
                "Should match OptionalDouble");

        // Test specific class pattern
        AbstractAPIConverter.TypeReplacement specificReplacement = converter.createTypeReplacement(
                "java/util/DoubleSummaryStatistics",
                "java8/util/DoubleSummaryStatistics");
        assertTrue(specificReplacement.matchesClassName("java/util/DoubleSummaryStatistics"),
                "Should match specific class exactly");
        assertFalse(specificReplacement.matchesClassName("java/util/IntSummaryStatistics"),
                "Should not match different class");
    }

    /**
     * Tests matchesClassName with complex nested package hierarchy.
     * Deep nesting should work correctly with ** pattern.
     */
    @Test
    public void testMatchesClassNameWithDeeplyNestedPackages() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/**",
                "org/<1>");

        // Act & Assert
        assertTrue(typeReplacement.matchesClassName("java/lang/String"),
                "Should match two-level nesting");
        assertTrue(typeReplacement.matchesClassName("java/util/concurrent/locks/ReentrantLock"),
                "Should match five-level nesting");
        assertTrue(typeReplacement.matchesClassName("java/awt/event/MouseEvent"),
                "Should match four-level nesting");
    }

    /**
     * Tests matchesClassName with multiple wildcards in pattern.
     * Complex patterns with multiple wildcards should work.
     */
    @Test
    public void testMatchesClassNameWithMultipleWildcards() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "*/util/*",
                "*/java8/*");

        // Act & Assert
        assertTrue(typeReplacement.matchesClassName("java/util/List"),
                "Should match with wildcards in multiple positions");
        assertTrue(typeReplacement.matchesClassName("com/util/Helper"),
                "Should match with different first wildcard");
    }

    /**
     * Tests matchesClassName with very long class names.
     * Long class names should be handled correctly.
     */
    @Test
    public void testMatchesClassNameWithVeryLongClassName() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/very/long/package/name/hierarchy/**",
                "org/backport/<1>");

        // Act
        boolean result = typeReplacement.matchesClassName(
                "com/example/very/long/package/name/hierarchy/sub/package/VeryLongClassName");

        // Assert
        assertTrue(result, "Should match very long class names");
    }

    /**
     * Tests matchesClassName matches the same pattern consistently.
     * Multiple calls with the same input should return the same result.
     */
    @Test
    public void testMatchesClassNameIsConsistent() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/**",
                "org/threeten/bp/<1>");

        // Act
        boolean result1 = typeReplacement.matchesClassName("java/time/LocalDate");
        boolean result2 = typeReplacement.matchesClassName("java/time/LocalDate");
        boolean result3 = typeReplacement.matchesClassName("java/time/LocalDate");

        // Assert
        assertTrue(result1, "First call should return true");
        assertTrue(result2, "Second call should return true");
        assertTrue(result3, "Third call should return true");
        assertEquals(result1, result2, "Results should be consistent");
        assertEquals(result2, result3, "Results should be consistent");
    }

    /**
     * Tests matchesClassName with special characters in class names.
     * Special characters like underscore and numbers should be handled.
     */
    @Test
    public void testMatchesClassNameWithSpecialCharacters() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/MyClass_123",
                "org/example/MyClass_123");

        // Act
        boolean result = typeReplacement.matchesClassName("com/example/MyClass_123");

        // Assert
        assertTrue(result, "Should match class names with underscores and numbers");
    }

    /**
     * Tests matchesClassName with Unicode characters in class names.
     * Unicode characters should be handled correctly.
     */
    @Test
    public void testMatchesClassNameWithUnicodeCharacters() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "com/example/MyClass\u00E9",
                "org/example/MyClass\u00E9");

        // Act
        boolean result = typeReplacement.matchesClassName("com/example/MyClass\u00E9");

        // Assert
        assertTrue(result, "Should match class names with Unicode characters");
    }

    /**
     * Tests matchesClassName with PrimitiveIterator pattern from StreamSupportConverter.
     * Verifies pattern matching for classes with common prefixes.
     */
    @Test
    public void testMatchesClassNameWithPrimitiveIteratorPattern() {
        // Arrange - Pattern from StreamSupportConverter line 64
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/util/PrimitiveIterator**",
                "java8/util/PrimitiveIterator<1>");

        // Act & Assert
        assertTrue(typeReplacement.matchesClassName("java/util/PrimitiveIterator"),
                "Should match PrimitiveIterator");
        assertTrue(typeReplacement.matchesClassName("java/util/PrimitiveIterator$OfInt"),
                "Should match PrimitiveIterator$OfInt");
        assertTrue(typeReplacement.matchesClassName("java/util/PrimitiveIterator$OfLong"),
                "Should match PrimitiveIterator$OfLong");
        assertTrue(typeReplacement.matchesClassName("java/util/PrimitiveIterator$OfDouble"),
                "Should match PrimitiveIterator$OfDouble");
        assertFalse(typeReplacement.matchesClassName("java/util/Iterator"),
                "Should not match plain Iterator");
    }

    /**
     * Tests matchesClassName with Spliterator pattern from StreamSupportConverter.
     * Verifies another real-world pattern with trailing double wildcard.
     */
    @Test
    public void testMatchesClassNameWithSpliteratorPattern() {
        // Arrange - Pattern from StreamSupportConverter line 66
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/util/Spliterator**",
                "java8/util/Spliterator<1>");

        // Act & Assert
        assertTrue(typeReplacement.matchesClassName("java/util/Spliterator"),
                "Should match Spliterator");
        assertTrue(typeReplacement.matchesClassName("java/util/Spliterators"),
                "Should match Spliterators");
        assertTrue(typeReplacement.matchesClassName("java/util/Spliterator$OfInt"),
                "Should match nested Spliterator classes");
        assertFalse(typeReplacement.matchesClassName("java/util/Split"),
                "Should not match partial prefix");
    }

    /**
     * Tests matchesClassName doesn't match partial package names.
     * Package boundaries should be respected.
     */
    @Test
    public void testMatchesClassNameDoesNotMatchPartialPackage() {
        // Arrange
        TestAPIConverter converter = new TestAPIConverter(programClassPool, libraryClassPool, warningPrinter);
        AbstractAPIConverter.TypeReplacement typeReplacement = converter.createTypeReplacement(
                "java/time/**",
                "org/threeten/bp/<1>");

        // Act & Assert
        assertFalse(typeReplacement.matchesClassName("java/time2/LocalDate"),
                "Should not match similar but different package");
        assertFalse(typeReplacement.matchesClassName("java/timeout/Handler"),
                "Should not match package with pattern as prefix");
    }
}
