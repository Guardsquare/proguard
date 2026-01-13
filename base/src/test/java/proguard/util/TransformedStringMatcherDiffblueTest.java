package proguard.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TransformedStringMatcherDiffblueTest {
  /**
   * Test {@link TransformedStringMatcher#matches(String)} with {@code string}.
   *
   * <p>Method under test: {@link TransformedStringMatcher#matches(String)}
   */
  @Test
  @DisplayName("Test matches(String) with 'string'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean TransformedStringMatcher.matches(String)"})
  void testMatchesWithString() {
    // Arrange
    StringFunction stringFunction = mock(StringFunction.class);
    when(stringFunction.transform(Mockito.<String>any())).thenReturn("Transform");
    TransformedStringMatcher transformedStringMatcher =
        new TransformedStringMatcher(stringFunction, new EmptyStringMatcher());

    // Act
    boolean actualMatchesResult = transformedStringMatcher.matches("String");

    // Assert
    verify(stringFunction).transform("String");
    assertFalse(actualMatchesResult);
  }

  /**
   * Test {@link TransformedStringMatcher#matches(String)} with {@code string}.
   *
   * <p>Method under test: {@link TransformedStringMatcher#matches(String)}
   */
  @Test
  @DisplayName("Test matches(String) with 'string'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean TransformedStringMatcher.matches(String)"})
  void testMatchesWithString2() {
    // Arrange
    StringFunction stringFunction = mock(StringFunction.class);
    when(stringFunction.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction2 = mock(StringFunction.class);
    when(stringFunction2.transform(Mockito.<String>any())).thenReturn("Transform");
    TransformedStringMatcher stringMatcher =
        new TransformedStringMatcher(stringFunction2, new EmptyStringMatcher());

    TransformedStringMatcher transformedStringMatcher =
        new TransformedStringMatcher(stringFunction, stringMatcher);

    // Act
    boolean actualMatchesResult = transformedStringMatcher.matches("String");

    // Assert
    verify(stringFunction).transform("String");
    verify(stringFunction2).transform("Transform");
    assertFalse(actualMatchesResult);
  }

  /**
   * Test {@link TransformedStringMatcher#matches(String)} with {@code string}.
   *
   * <p>Method under test: {@link TransformedStringMatcher#matches(String)}
   */
  @Test
  @DisplayName("Test matches(String) with 'string'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean TransformedStringMatcher.matches(String)"})
  void testMatchesWithString3() {
    // Arrange
    StringFunction stringFunction = mock(StringFunction.class);
    when(stringFunction.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction2 = mock(StringFunction.class);
    when(stringFunction2.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction3 = mock(StringFunction.class);
    when(stringFunction3.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction4 = mock(StringFunction.class);
    when(stringFunction4.transform(Mockito.<String>any())).thenReturn("Transform");
    TransformedStringMatcher matcher1 =
        new TransformedStringMatcher(stringFunction4, new EmptyStringMatcher());
    StringFunction stringFunction5 = mock(StringFunction.class);
    TransformedStringMatcher matcher2 =
        new TransformedStringMatcher(stringFunction5, new EmptyStringMatcher());

    AndMatcher stringMatcher = new AndMatcher(matcher1, matcher2);

    TransformedStringMatcher stringMatcher2 =
        new TransformedStringMatcher(stringFunction3, stringMatcher);

    TransformedStringMatcher stringMatcher3 =
        new TransformedStringMatcher(stringFunction2, stringMatcher2);

    TransformedStringMatcher transformedStringMatcher =
        new TransformedStringMatcher(stringFunction, stringMatcher3);

    // Act
    boolean actualMatchesResult = transformedStringMatcher.matches("String");

    // Assert
    verify(stringFunction).transform("String");
    verify(stringFunction2).transform("Transform");
    verify(stringFunction3).transform("Transform");
    verify(stringFunction4).transform("Transform");
    assertFalse(actualMatchesResult);
  }

  /**
   * Test {@link TransformedStringMatcher#matches(String)} with {@code string}.
   *
   * <p>Method under test: {@link TransformedStringMatcher#matches(String)}
   */
  @Test
  @DisplayName("Test matches(String) with 'string'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean TransformedStringMatcher.matches(String)"})
  void testMatchesWithString4() {
    // Arrange
    StringFunction stringFunction = mock(StringFunction.class);
    when(stringFunction.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction2 = mock(StringFunction.class);
    when(stringFunction2.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction3 = mock(StringFunction.class);
    when(stringFunction3.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction4 = mock(StringFunction.class);
    when(stringFunction4.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction5 = mock(StringFunction.class);
    when(stringFunction5.transform(Mockito.<String>any())).thenReturn("Transform");
    TransformedStringMatcher stringMatcher =
        new TransformedStringMatcher(stringFunction5, new EmptyStringMatcher());

    TransformedStringMatcher matcher1 =
        new TransformedStringMatcher(stringFunction4, stringMatcher);
    StringFunction stringFunction6 = mock(StringFunction.class);
    TransformedStringMatcher matcher2 =
        new TransformedStringMatcher(stringFunction6, new EmptyStringMatcher());

    AndMatcher stringMatcher2 = new AndMatcher(matcher1, matcher2);

    TransformedStringMatcher stringMatcher3 =
        new TransformedStringMatcher(stringFunction3, stringMatcher2);

    TransformedStringMatcher stringMatcher4 =
        new TransformedStringMatcher(stringFunction2, stringMatcher3);

    TransformedStringMatcher transformedStringMatcher =
        new TransformedStringMatcher(stringFunction, stringMatcher4);

    // Act
    boolean actualMatchesResult = transformedStringMatcher.matches("String");

    // Assert
    verify(stringFunction).transform("String");
    verify(stringFunction2).transform("Transform");
    verify(stringFunction3).transform("Transform");
    verify(stringFunction4).transform("Transform");
    verify(stringFunction5).transform("Transform");
    assertFalse(actualMatchesResult);
  }

  /**
   * Test {@link TransformedStringMatcher#matches(String, int, int)} with {@code string}, {@code
   * beginOffset}, {@code endOffset}.
   *
   * <p>Method under test: {@link TransformedStringMatcher#matches(String, int, int)}
   */
  @Test
  @DisplayName("Test matches(String, int, int) with 'string', 'beginOffset', 'endOffset'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean TransformedStringMatcher.matches(String, int, int)"})
  void testMatchesWithStringBeginOffsetEndOffset() {
    // Arrange
    StringFunction stringFunction = mock(StringFunction.class);
    when(stringFunction.transform(Mockito.<String>any())).thenReturn("Transform");
    TransformedStringMatcher transformedStringMatcher =
        new TransformedStringMatcher(stringFunction, new EmptyStringMatcher());

    // Act
    boolean actualMatchesResult = transformedStringMatcher.matches("String", 1, 3);

    // Assert
    verify(stringFunction).transform("tr");
    assertFalse(actualMatchesResult);
  }

  /**
   * Test {@link TransformedStringMatcher#matches(String, int, int)} with {@code string}, {@code
   * beginOffset}, {@code endOffset}.
   *
   * <p>Method under test: {@link TransformedStringMatcher#matches(String, int, int)}
   */
  @Test
  @DisplayName("Test matches(String, int, int) with 'string', 'beginOffset', 'endOffset'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean TransformedStringMatcher.matches(String, int, int)"})
  void testMatchesWithStringBeginOffsetEndOffset2() {
    // Arrange
    StringFunction stringFunction = mock(StringFunction.class);
    when(stringFunction.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction2 = mock(StringFunction.class);
    when(stringFunction2.transform(Mockito.<String>any())).thenReturn("Transform");
    TransformedStringMatcher stringMatcher =
        new TransformedStringMatcher(stringFunction2, new EmptyStringMatcher());

    TransformedStringMatcher transformedStringMatcher =
        new TransformedStringMatcher(stringFunction, stringMatcher);

    // Act
    boolean actualMatchesResult = transformedStringMatcher.matches("String", 1, 3);

    // Assert
    verify(stringFunction2).transform("Transform");
    verify(stringFunction).transform("tr");
    assertFalse(actualMatchesResult);
  }

  /**
   * Test {@link TransformedStringMatcher#matches(String, int, int)} with {@code string}, {@code
   * beginOffset}, {@code endOffset}.
   *
   * <p>Method under test: {@link TransformedStringMatcher#matches(String, int, int)}
   */
  @Test
  @DisplayName("Test matches(String, int, int) with 'string', 'beginOffset', 'endOffset'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean TransformedStringMatcher.matches(String, int, int)"})
  void testMatchesWithStringBeginOffsetEndOffset3() {
    // Arrange
    StringFunction stringFunction = mock(StringFunction.class);
    when(stringFunction.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction2 = mock(StringFunction.class);
    when(stringFunction2.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction3 = mock(StringFunction.class);
    when(stringFunction3.transform(Mockito.<String>any())).thenReturn("Transform");
    TransformedStringMatcher stringMatcher =
        new TransformedStringMatcher(stringFunction3, new EmptyStringMatcher());

    TransformedStringMatcher stringMatcher2 =
        new TransformedStringMatcher(stringFunction2, stringMatcher);

    TransformedStringMatcher transformedStringMatcher =
        new TransformedStringMatcher(stringFunction, stringMatcher2);

    // Act
    boolean actualMatchesResult = transformedStringMatcher.matches("String", 1, 3);

    // Assert
    verify(stringFunction2).transform("Transform");
    verify(stringFunction3).transform("Transform");
    verify(stringFunction).transform("tr");
    assertFalse(actualMatchesResult);
  }

  /**
   * Test {@link TransformedStringMatcher#matches(String, int, int)} with {@code string}, {@code
   * beginOffset}, {@code endOffset}.
   *
   * <p>Method under test: {@link TransformedStringMatcher#matches(String, int, int)}
   */
  @Test
  @DisplayName("Test matches(String, int, int) with 'string', 'beginOffset', 'endOffset'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean TransformedStringMatcher.matches(String, int, int)"})
  void testMatchesWithStringBeginOffsetEndOffset4() {
    // Arrange
    StringFunction stringFunction = mock(StringFunction.class);
    when(stringFunction.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction2 = mock(StringFunction.class);
    when(stringFunction2.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction3 = mock(StringFunction.class);
    when(stringFunction3.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction4 = mock(StringFunction.class);
    when(stringFunction4.transform(Mockito.<String>any())).thenReturn("Transform");

    StringFunction stringFunction5 = mock(StringFunction.class);
    when(stringFunction5.transform(Mockito.<String>any())).thenReturn("Transform");
    TransformedStringMatcher matcher1 =
        new TransformedStringMatcher(stringFunction5, new EmptyStringMatcher());
    StringFunction stringFunction6 = mock(StringFunction.class);
    TransformedStringMatcher matcher2 =
        new TransformedStringMatcher(stringFunction6, new EmptyStringMatcher());

    AndMatcher stringMatcher = new AndMatcher(matcher1, matcher2);

    TransformedStringMatcher stringMatcher2 =
        new TransformedStringMatcher(stringFunction4, stringMatcher);

    TransformedStringMatcher stringMatcher3 =
        new TransformedStringMatcher(stringFunction3, stringMatcher2);

    TransformedStringMatcher stringMatcher4 =
        new TransformedStringMatcher(stringFunction2, stringMatcher3);

    TransformedStringMatcher transformedStringMatcher =
        new TransformedStringMatcher(stringFunction, stringMatcher4);

    // Act
    boolean actualMatchesResult = transformedStringMatcher.matches("String", 1, 3);

    // Assert
    verify(stringFunction2).transform("Transform");
    verify(stringFunction3).transform("Transform");
    verify(stringFunction4).transform("Transform");
    verify(stringFunction5).transform("Transform");
    verify(stringFunction).transform("tr");
    assertFalse(actualMatchesResult);
  }
}
