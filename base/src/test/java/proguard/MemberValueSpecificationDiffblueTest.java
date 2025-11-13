package proguard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class MemberValueSpecificationDiffblueTest {
  /**
   * Test {@link MemberValueSpecification#MemberValueSpecification()}.
   *
   * <ul>
   *   <li>Then return {@link MemberValueSpecification#values} is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link MemberValueSpecification#MemberValueSpecification()}
   */
  @Test
  @DisplayName("Test new MemberValueSpecification(); then return values is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MemberValueSpecification.<init>()",
    "void MemberValueSpecification.<init>(int, int, String, String, String, Number[])"
  })
  void testNewMemberValueSpecification_thenReturnValuesIsNull() {
    // Arrange and Act
    MemberValueSpecification actualMemberValueSpecification = new MemberValueSpecification();

    // Assert
    assertNull(actualMemberValueSpecification.values);
    assertNull(actualMemberValueSpecification.annotationType);
    assertNull(actualMemberValueSpecification.descriptor);
    assertNull(actualMemberValueSpecification.name);
    assertNull(actualMemberValueSpecification.attributeNames);
    assertEquals(0, actualMemberValueSpecification.requiredSetAccessFlags);
    assertEquals(0, actualMemberValueSpecification.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link MemberValueSpecification#MemberValueSpecification(int, int, String, String, String,
   * Number[])}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return {@code Annotation Type}.
   * </ul>
   *
   * <p>Method under test: {@link MemberValueSpecification#MemberValueSpecification(int, int,
   * String, String, String, Number[])}
   */
  @Test
  @DisplayName(
      "Test new MemberValueSpecification(int, int, String, String, String, Number[]); when one; then return 'Annotation Type'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MemberValueSpecification.<init>()",
    "void MemberValueSpecification.<init>(int, int, String, String, String, Number[])"
  })
  void testNewMemberValueSpecification_whenOne_thenReturnAnnotationType() {
    // Arrange
    Integer valueOfResult = Integer.valueOf(1);
    Number[] values = new Number[] {valueOfResult};

    // Act
    MemberValueSpecification actualMemberValueSpecification =
        new MemberValueSpecification(1, 1, "Annotation Type", "Name", "Descriptor", values);

    // Assert
    assertEquals("Annotation Type", actualMemberValueSpecification.annotationType);
    assertEquals("Descriptor", actualMemberValueSpecification.descriptor);
    assertEquals("Name", actualMemberValueSpecification.name);
    assertNull(actualMemberValueSpecification.attributeNames);
    Number[] numberArray = actualMemberValueSpecification.values;
    Number number = numberArray[0];
    assertEquals(1, number.intValue());
    assertEquals(1, numberArray.length);
    assertEquals(1, actualMemberValueSpecification.requiredSetAccessFlags);
    assertEquals(1, actualMemberValueSpecification.requiredUnsetAccessFlags);
    assertSame(valueOfResult, number);
  }

  /**
   * Test {@link MemberValueSpecification#equals(Object)}, and {@link
   * MemberValueSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is equal.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link MemberValueSpecification#equals(Object)}
   *   <li>{@link MemberValueSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is equal; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberValueSpecification.equals(Object)",
    "int MemberValueSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual() {
    // Arrange
    MemberValueSpecification memberValueSpecification = new MemberValueSpecification();
    MemberValueSpecification memberValueSpecification2 = new MemberValueSpecification();

    // Act and Assert
    assertEquals(memberValueSpecification, memberValueSpecification2);
    assertEquals(memberValueSpecification.hashCode(), memberValueSpecification2.hashCode());
  }

  /**
   * Test {@link MemberValueSpecification#equals(Object)}, and {@link
   * MemberValueSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is same.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link MemberValueSpecification#equals(Object)}
   *   <li>{@link MemberValueSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is same; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberValueSpecification.equals(Object)",
    "int MemberValueSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsSame_thenReturnEqual() {
    // Arrange
    MemberValueSpecification memberValueSpecification = new MemberValueSpecification();

    // Act and Assert
    assertEquals(memberValueSpecification, memberValueSpecification);
    int expectedHashCodeResult = memberValueSpecification.hashCode();
    assertEquals(expectedHashCodeResult, memberValueSpecification.hashCode());
  }

  /**
   * Test {@link MemberValueSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link MemberValueSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberValueSpecification.equals(Object)",
    "int MemberValueSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual() {
    // Arrange
    Number[] values = new Number[] {Integer.valueOf(1)};
    MemberValueSpecification memberValueSpecification =
        new MemberValueSpecification(1, 1, "Annotation Type", "Name", "Descriptor", values);

    // Act and Assert
    assertNotEquals(memberValueSpecification, new MemberValueSpecification());
  }

  /**
   * Test {@link MemberValueSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link MemberValueSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberValueSpecification.equals(Object)",
    "int MemberValueSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual2() {
    // Arrange
    MemberValueSpecification memberValueSpecification =
        new MemberValueSpecification(
            1,
            1,
            "Annotation Type",
            "Name",
            "Descriptor",
            new Number[] {Integer.valueOf(1), Integer.valueOf(1)});

    // Act and Assert
    assertNotEquals(
        memberValueSpecification,
        new MemberValueSpecification(1, 1, "Annotation Type", "Name", "Descriptor", null));
  }

  /**
   * Test {@link MemberValueSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is {@code null}.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link MemberValueSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is 'null'; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberValueSpecification.equals(Object)",
    "int MemberValueSpecification.hashCode()"
  })
  void testEquals_whenOtherIsNull_thenReturnNotEqual() {
    // Arrange, Act and Assert
    assertNotEquals(new MemberValueSpecification(), null);
  }

  /**
   * Test {@link MemberValueSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is wrong type.
   *   <li>Then throw exception.
   * </ul>
   *
   * <p>Method under test: {@link MemberValueSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is wrong type; then throw exception")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberValueSpecification.equals(Object)",
    "int MemberValueSpecification.hashCode()"
  })
  void testEquals_whenOtherIsWrongType_thenThrowException() {
    // Arrange, Act and Assert
    assertThrows(
        ClassCastException.class,
        () -> new MemberValueSpecification().equals("Different type to MemberValueSpecification"));
  }
}
