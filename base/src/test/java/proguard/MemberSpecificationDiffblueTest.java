package proguard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class MemberSpecificationDiffblueTest {
  /**
   * Test {@link MemberSpecification#MemberSpecification()}.
   *
   * <ul>
   *   <li>Then return {@link MemberSpecification#annotationType} is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecification#MemberSpecification()}
   */
  @Test
  @DisplayName("Test new MemberSpecification(); then return annotationType is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MemberSpecification.<init>()",
    "void MemberSpecification.<init>(int, int, String, String, String)"
  })
  void testNewMemberSpecification_thenReturnAnnotationTypeIsNull() {
    // Arrange and Act
    MemberSpecification actualMemberSpecification = new MemberSpecification();

    // Assert
    assertNull(actualMemberSpecification.annotationType);
    assertNull(actualMemberSpecification.descriptor);
    assertNull(actualMemberSpecification.name);
    assertNull(actualMemberSpecification.attributeNames);
    assertEquals(0, actualMemberSpecification.requiredSetAccessFlags);
    assertEquals(0, actualMemberSpecification.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link MemberSpecification#MemberSpecification(int, int, String, String, String)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return {@code Annotation Type}.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecification#MemberSpecification(int, int, String, String,
   * String)}
   */
  @Test
  @DisplayName(
      "Test new MemberSpecification(int, int, String, String, String); when one; then return 'Annotation Type'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MemberSpecification.<init>()",
    "void MemberSpecification.<init>(int, int, String, String, String)"
  })
  void testNewMemberSpecification_whenOne_thenReturnAnnotationType() {
    // Arrange and Act
    MemberSpecification actualMemberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");

    // Assert
    assertEquals("Annotation Type", actualMemberSpecification.annotationType);
    assertEquals("Descriptor", actualMemberSpecification.descriptor);
    assertEquals("Name", actualMemberSpecification.name);
    assertNull(actualMemberSpecification.attributeNames);
    assertEquals(1, actualMemberSpecification.requiredSetAccessFlags);
    assertEquals(1, actualMemberSpecification.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}, and {@link MemberSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is equal.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link MemberSpecification#equals(Object)}
   *   <li>{@link MemberSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is equal; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual() {
    // Arrange
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    MemberSpecification memberSpecification2 =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");

    // Act and Assert
    assertEquals(memberSpecification, memberSpecification2);
    assertEquals(memberSpecification.hashCode(), memberSpecification2.hashCode());
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}, and {@link MemberSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is equal.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link MemberSpecification#equals(Object)}
   *   <li>{@link MemberSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is equal; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual2() {
    // Arrange
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, null, "Name", "Descriptor");
    MemberSpecification memberSpecification2 =
        new MemberSpecification(1, 1, null, "Name", "Descriptor");

    // Act and Assert
    assertEquals(memberSpecification, memberSpecification2);
    assertEquals(memberSpecification.hashCode(), memberSpecification2.hashCode());
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}, and {@link MemberSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is equal.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link MemberSpecification#equals(Object)}
   *   <li>{@link MemberSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is equal; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual3() {
    // Arrange
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", null, "Descriptor");
    MemberSpecification memberSpecification2 =
        new MemberSpecification(1, 1, "Annotation Type", null, "Descriptor");

    // Act and Assert
    assertEquals(memberSpecification, memberSpecification2);
    assertEquals(memberSpecification.hashCode(), memberSpecification2.hashCode());
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}, and {@link MemberSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is equal.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link MemberSpecification#equals(Object)}
   *   <li>{@link MemberSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is equal; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual4() {
    // Arrange
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", null);
    MemberSpecification memberSpecification2 =
        new MemberSpecification(1, 1, "Annotation Type", "Name", null);

    // Act and Assert
    assertEquals(memberSpecification, memberSpecification2);
    assertEquals(memberSpecification.hashCode(), memberSpecification2.hashCode());
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}, and {@link MemberSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is same.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link MemberSpecification#equals(Object)}
   *   <li>{@link MemberSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is same; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsSame_thenReturnEqual() {
    // Arrange
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");

    // Act and Assert
    assertEquals(memberSpecification, memberSpecification);
    int expectedHashCodeResult = memberSpecification.hashCode();
    assertEquals(expectedHashCodeResult, memberSpecification.hashCode());
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual() {
    // Arrange
    MemberSpecification memberSpecification =
        new MemberSpecification(0, 1, "Annotation Type", "Name", "Descriptor");

    // Act and Assert
    assertNotEquals(
        memberSpecification,
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor"));
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual2() {
    // Arrange
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 0, "Annotation Type", "Name", "Descriptor");

    // Act and Assert
    assertNotEquals(
        memberSpecification,
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor"));
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual3() {
    // Arrange
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Name", "Name", "Descriptor");

    // Act and Assert
    assertNotEquals(
        memberSpecification,
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor"));
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual4() {
    // Arrange
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, null, "Name", "Descriptor");

    // Act and Assert
    assertNotEquals(
        memberSpecification,
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor"));
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual5() {
    // Arrange
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Annotation Type", "Descriptor");

    // Act and Assert
    assertNotEquals(
        memberSpecification,
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor"));
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual6() {
    // Arrange
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", null, "Descriptor");

    // Act and Assert
    assertNotEquals(
        memberSpecification,
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor"));
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual7() {
    // Arrange
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Annotation Type");

    // Act and Assert
    assertNotEquals(
        memberSpecification,
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor"));
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual8() {
    // Arrange
    MemberSpecification memberSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", null);

    // Act and Assert
    assertNotEquals(
        memberSpecification,
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor"));
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is {@code null}.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is 'null'; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEquals_whenOtherIsNull_thenReturnNotEqual() {
    // Arrange, Act and Assert
    assertNotEquals(new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor"), null);
  }

  /**
   * Test {@link MemberSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is wrong type.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link MemberSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is wrong type; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean MemberSpecification.equals(Object)",
    "int MemberSpecification.hashCode()"
  })
  void testEquals_whenOtherIsWrongType_thenReturnNotEqual() {
    // Arrange, Act and Assert
    assertNotEquals(
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor"),
        "Different type to MemberSpecification");
  }
}
