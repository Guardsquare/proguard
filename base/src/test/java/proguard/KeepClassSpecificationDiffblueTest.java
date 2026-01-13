package proguard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class KeepClassSpecificationDiffblueTest {
  /**
   * Test {@link KeepClassSpecification#KeepClassSpecification(boolean, boolean, boolean, boolean,
   * boolean, boolean, boolean, ClassSpecification, ClassSpecification)}.
   *
   * <p>Method under test: {@link KeepClassSpecification#KeepClassSpecification(boolean, boolean,
   * boolean, boolean, boolean, boolean, boolean, ClassSpecification, ClassSpecification)}
   */
  @Test
  @DisplayName(
      "Test new KeepClassSpecification(boolean, boolean, boolean, boolean, boolean, boolean, boolean, ClassSpecification, ClassSpecification)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KeepClassSpecification.<init>(boolean, boolean, boolean, boolean, boolean, boolean, boolean, ClassSpecification, ClassSpecification)"
  })
  void testNewKeepClassSpecification() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act
    KeepClassSpecification actualKeepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);

    // Assert
    assertEquals("Annotation Type", actualKeepClassSpecification.annotationType);
    assertEquals("Class Name", actualKeepClassSpecification.className);
    assertEquals("Comments", actualKeepClassSpecification.comments);
    assertEquals("Extends Annotation Type", actualKeepClassSpecification.extendsAnnotationType);
    assertEquals("Extends Class Name", actualKeepClassSpecification.extendsClassName);
    assertNull(actualKeepClassSpecification.memberComments);
    assertNull(actualKeepClassSpecification.attributeNames);
    assertNull(actualKeepClassSpecification.fieldSpecifications);
    assertNull(actualKeepClassSpecification.methodSpecifications);
    assertEquals(1, actualKeepClassSpecification.requiredSetAccessFlags);
    assertEquals(1, actualKeepClassSpecification.requiredUnsetAccessFlags);
    assertTrue(actualKeepClassSpecification.allowObfuscation);
    assertTrue(actualKeepClassSpecification.allowOptimization);
    assertTrue(actualKeepClassSpecification.allowShrinking);
    assertTrue(actualKeepClassSpecification.markClassMembers);
    assertTrue(actualKeepClassSpecification.markClasses);
    assertTrue(actualKeepClassSpecification.markCodeAttributes);
    assertTrue(actualKeepClassSpecification.markConditionally);
    assertTrue(actualKeepClassSpecification.markDescriptorClasses);
  }

  /**
   * Test {@link KeepClassSpecification#KeepClassSpecification(boolean, boolean, boolean, boolean,
   * boolean, boolean, boolean, boolean, ClassSpecification, ClassSpecification)}.
   *
   * <p>Method under test: {@link KeepClassSpecification#KeepClassSpecification(boolean, boolean,
   * boolean, boolean, boolean, boolean, boolean, boolean, ClassSpecification, ClassSpecification)}
   */
  @Test
  @DisplayName(
      "Test new KeepClassSpecification(boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, ClassSpecification, ClassSpecification)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KeepClassSpecification.<init>(boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, ClassSpecification, ClassSpecification)"
  })
  void testNewKeepClassSpecification2() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act
    KeepClassSpecification actualKeepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, true, condition, classSpecification);

    // Assert
    assertEquals("Annotation Type", actualKeepClassSpecification.annotationType);
    assertEquals("Class Name", actualKeepClassSpecification.className);
    assertEquals("Comments", actualKeepClassSpecification.comments);
    assertEquals("Extends Annotation Type", actualKeepClassSpecification.extendsAnnotationType);
    assertEquals("Extends Class Name", actualKeepClassSpecification.extendsClassName);
    assertNull(actualKeepClassSpecification.memberComments);
    assertNull(actualKeepClassSpecification.attributeNames);
    assertNull(actualKeepClassSpecification.fieldSpecifications);
    assertNull(actualKeepClassSpecification.methodSpecifications);
    assertEquals(1, actualKeepClassSpecification.requiredSetAccessFlags);
    assertEquals(1, actualKeepClassSpecification.requiredUnsetAccessFlags);
    assertTrue(actualKeepClassSpecification.allowObfuscation);
    assertTrue(actualKeepClassSpecification.allowOptimization);
    assertTrue(actualKeepClassSpecification.allowShrinking);
    assertTrue(actualKeepClassSpecification.markClassMembers);
    assertTrue(actualKeepClassSpecification.markClasses);
    assertTrue(actualKeepClassSpecification.markCodeAttributes);
    assertTrue(actualKeepClassSpecification.markConditionally);
    assertTrue(actualKeepClassSpecification.markDescriptorClasses);
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}, and {@link
   * KeepClassSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is equal.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link KeepClassSpecification#equals(Object)}
   *   <li>{@link KeepClassSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is equal; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassSpecification condition2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification2 =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition2, classSpecification2);

    // Act and Assert
    assertEquals(keepClassSpecification, keepClassSpecification2);
    assertEquals(keepClassSpecification.hashCode(), keepClassSpecification2.hashCode());
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}, and {@link
   * KeepClassSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is equal.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link KeepClassSpecification#equals(Object)}
   *   <li>{@link KeepClassSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is equal; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual2() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, null, classSpecification);
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    KeepClassSpecification keepClassSpecification2 =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, null, classSpecification2);

    // Act and Assert
    assertEquals(keepClassSpecification, keepClassSpecification2);
    assertEquals(keepClassSpecification.hashCode(), keepClassSpecification2.hashCode());
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}, and {@link
   * KeepClassSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is same.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link KeepClassSpecification#equals(Object)}
   *   <li>{@link KeepClassSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is same; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsSame_thenReturnEqual() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);

    // Act and Assert
    assertEquals(keepClassSpecification, keepClassSpecification);
    int expectedHashCodeResult = keepClassSpecification.hashCode();
    assertEquals(expectedHashCodeResult, keepClassSpecification.hashCode());
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link KeepClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            false, true, true, true, true, true, true, condition, classSpecification);
    ClassSpecification condition2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        keepClassSpecification,
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition2, classSpecification2));
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link KeepClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual2() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, false, true, true, true, true, true, condition, classSpecification);
    ClassSpecification condition2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        keepClassSpecification,
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition2, classSpecification2));
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link KeepClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual3() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, false, true, true, true, true, condition, classSpecification);
    ClassSpecification condition2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        keepClassSpecification,
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition2, classSpecification2));
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link KeepClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual4() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, false, true, true, true, condition, classSpecification);
    ClassSpecification condition2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        keepClassSpecification,
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition2, classSpecification2));
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link KeepClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual5() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, false, true, true, condition, classSpecification);
    ClassSpecification condition2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        keepClassSpecification,
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition2, classSpecification2));
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link KeepClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual6() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, false, true, condition, classSpecification);
    ClassSpecification condition2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        keepClassSpecification,
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition2, classSpecification2));
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link KeepClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual7() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, false, condition, classSpecification);
    ClassSpecification condition2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        keepClassSpecification,
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition2, classSpecification2));
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link KeepClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual8() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            0,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassSpecification condition2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        keepClassSpecification,
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition2, classSpecification2));
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link KeepClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual9() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification condition2 =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition2, classSpecification2);
    ClassSpecification condition3 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification3 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        keepClassSpecification,
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition3, classSpecification3));
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link KeepClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual10() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, null, classSpecification);
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        keepClassSpecification,
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification2));
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link KeepClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual11() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);
    MemberSpecification fieldSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");
    keepClassSpecification.addField(fieldSpecification);
    ClassSpecification condition2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        keepClassSpecification,
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition2, classSpecification2));
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is {@code null}.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link KeepClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is 'null'; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsNull_thenReturnNotEqual() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification),
        null);
  }

  /**
   * Test {@link KeepClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is wrong type.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link KeepClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is wrong type; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean KeepClassSpecification.equals(Object)",
    "int KeepClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsWrongType_thenReturnNotEqual() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification),
        "Different type to KeepClassSpecification");
  }

  /**
   * Test {@link KeepClassSpecification#clone()}.
   *
   * <p>Method under test: {@link KeepClassSpecification#clone()}
   */
  @Test
  @DisplayName("Test clone()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Object KeepClassSpecification.clone()"})
  void testClone() {
    // Arrange
    ClassSpecification condition =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    KeepClassSpecification keepClassSpecification =
        new KeepClassSpecification(
            true, true, true, true, true, true, true, condition, classSpecification);

    // Act
    Object actualCloneResult = keepClassSpecification.clone();

    // Assert
    assertTrue(actualCloneResult instanceof KeepClassSpecification);
    assertEquals(keepClassSpecification, actualCloneResult);
  }
}
