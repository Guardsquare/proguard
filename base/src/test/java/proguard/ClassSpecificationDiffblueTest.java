package proguard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ClassSpecificationDiffblueTest {
  /**
   * Test {@link ClassSpecification#ClassSpecification(ClassSpecification)}.
   *
   * <p>Method under test: {@link ClassSpecification#ClassSpecification(ClassSpecification)}
   */
  @Test
  @DisplayName("Test new ClassSpecification(ClassSpecification)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecification.<init>(ClassSpecification)"})
  void testNewClassSpecification() {
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

    // Act
    ClassSpecification actualClassSpecification = new ClassSpecification(classSpecification);

    // Assert
    assertEquals(classSpecification, actualClassSpecification);
  }

  /**
   * Test {@link ClassSpecification#ClassSpecification()}.
   *
   * <ul>
   *   <li>Then return {@link ClassSpecification#annotationType} is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#ClassSpecification()}
   */
  @Test
  @DisplayName("Test new ClassSpecification(); then return annotationType is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassSpecification.<init>()",
    "void ClassSpecification.<init>(String, int, int, String, String, String, String)",
    "void ClassSpecification.<init>(String, int, int, String, String, String, String, List, List)"
  })
  void testNewClassSpecification_thenReturnAnnotationTypeIsNull() {
    // Arrange and Act
    ClassSpecification actualClassSpecification = new ClassSpecification();

    // Assert
    assertNull(actualClassSpecification.annotationType);
    assertNull(actualClassSpecification.className);
    assertNull(actualClassSpecification.comments);
    assertNull(actualClassSpecification.extendsAnnotationType);
    assertNull(actualClassSpecification.extendsClassName);
    assertNull(actualClassSpecification.memberComments);
    assertNull(actualClassSpecification.attributeNames);
    assertNull(actualClassSpecification.fieldSpecifications);
    assertNull(actualClassSpecification.methodSpecifications);
    assertEquals(0, actualClassSpecification.requiredSetAccessFlags);
    assertEquals(0, actualClassSpecification.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecification#ClassSpecification(String, int, int, String, String, String,
   * String, List, List)}.
   *
   * <ul>
   *   <li>When {@link ArrayList#ArrayList()}.
   *   <li>Then return {@link ClassSpecification#fieldSpecifications} Empty.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#ClassSpecification(String, int, int, String,
   * String, String, String, List, List)}
   */
  @Test
  @DisplayName(
      "Test new ClassSpecification(String, int, int, String, String, String, String, List, List); when ArrayList(); then return fieldSpecifications Empty")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassSpecification.<init>()",
    "void ClassSpecification.<init>(String, int, int, String, String, String, String)",
    "void ClassSpecification.<init>(String, int, int, String, String, String, String, List, List)"
  })
  void testNewClassSpecification_whenArrayList_thenReturnFieldSpecificationsEmpty() {
    // Arrange
    ArrayList<Object> fieldSpecifications = new ArrayList<>();

    // Act
    ClassSpecification actualClassSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name",
            fieldSpecifications,
            new ArrayList<>());

    // Assert
    assertTrue(actualClassSpecification.fieldSpecifications.isEmpty());
    assertTrue(actualClassSpecification.methodSpecifications.isEmpty());
  }

  /**
   * Test {@link ClassSpecification#ClassSpecification(String, int, int, String, String, String,
   * String)}.
   *
   * <ul>
   *   <li>When {@code Comments}.
   *   <li>Then return {@code Annotation Type}.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#ClassSpecification(String, int, int, String,
   * String, String, String)}
   */
  @Test
  @DisplayName(
      "Test new ClassSpecification(String, int, int, String, String, String, String); when 'Comments'; then return 'Annotation Type'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassSpecification.<init>()",
    "void ClassSpecification.<init>(String, int, int, String, String, String, String)",
    "void ClassSpecification.<init>(String, int, int, String, String, String, String, List, List)"
  })
  void testNewClassSpecification_whenComments_thenReturnAnnotationType() {
    // Arrange and Act
    ClassSpecification actualClassSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Assert
    assertEquals("Annotation Type", actualClassSpecification.annotationType);
    assertEquals("Class Name", actualClassSpecification.className);
    assertEquals("Comments", actualClassSpecification.comments);
    assertEquals("Extends Annotation Type", actualClassSpecification.extendsAnnotationType);
    assertEquals("Extends Class Name", actualClassSpecification.extendsClassName);
    assertNull(actualClassSpecification.memberComments);
    assertNull(actualClassSpecification.attributeNames);
    assertNull(actualClassSpecification.fieldSpecifications);
    assertNull(actualClassSpecification.methodSpecifications);
    assertEquals(1, actualClassSpecification.requiredSetAccessFlags);
    assertEquals(1, actualClassSpecification.requiredUnsetAccessFlags);
  }

  /**
   * Test {@link ClassSpecification#addField(MemberSpecification)}.
   *
   * <p>Method under test: {@link ClassSpecification#addField(MemberSpecification)}
   */
  @Test
  @DisplayName("Test addField(MemberSpecification)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecification.addField(MemberSpecification)"})
  void testAddField() {
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
    MemberSpecification fieldSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");

    // Act
    classSpecification.addField(fieldSpecification);

    // Assert
    List<MemberSpecification> memberSpecificationList = classSpecification.fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    assertSame(fieldSpecification, memberSpecificationList.get(0));
  }

  /**
   * Test {@link ClassSpecification#addField(MemberSpecification)}.
   *
   * <p>Method under test: {@link ClassSpecification#addField(MemberSpecification)}
   */
  @Test
  @DisplayName("Test addField(MemberSpecification)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecification.addField(MemberSpecification)"})
  void testAddField2() {
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
    ClassSpecification classSpecification2 = new ClassSpecification(classSpecification);
    classSpecification2.fieldSpecifications = new ArrayList<>();
    MemberSpecification fieldSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");

    // Act
    classSpecification2.addField(fieldSpecification);

    // Assert
    List<MemberSpecification> memberSpecificationList = classSpecification2.fieldSpecifications;
    assertEquals(1, memberSpecificationList.size());
    assertSame(fieldSpecification, memberSpecificationList.get(0));
  }

  /**
   * Test {@link ClassSpecification#addMethod(MemberSpecification)}.
   *
   * <p>Method under test: {@link ClassSpecification#addMethod(MemberSpecification)}
   */
  @Test
  @DisplayName("Test addMethod(MemberSpecification)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecification.addMethod(MemberSpecification)"})
  void testAddMethod() {
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
    MemberSpecification methodSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");

    // Act
    classSpecification.addMethod(methodSpecification);

    // Assert
    List<MemberSpecification> memberSpecificationList = classSpecification.methodSpecifications;
    assertEquals(1, memberSpecificationList.size());
    assertSame(methodSpecification, memberSpecificationList.get(0));
  }

  /**
   * Test {@link ClassSpecification#addMethod(MemberSpecification)}.
   *
   * <p>Method under test: {@link ClassSpecification#addMethod(MemberSpecification)}
   */
  @Test
  @DisplayName("Test addMethod(MemberSpecification)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassSpecification.addMethod(MemberSpecification)"})
  void testAddMethod2() {
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
    ClassSpecification classSpecification2 = new ClassSpecification(classSpecification);
    classSpecification2.methodSpecifications = new ArrayList<>();
    MemberSpecification methodSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Name", "Descriptor");

    // Act
    classSpecification2.addMethod(methodSpecification);

    // Assert
    List<MemberSpecification> memberSpecificationList = classSpecification2.methodSpecifications;
    assertEquals(1, memberSpecificationList.size());
    assertSame(methodSpecification, memberSpecificationList.get(0));
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}, and {@link ClassSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is equal.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link ClassSpecification#equals(Object)}
   *   <li>{@link ClassSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is equal; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual() {
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
    assertEquals(classSpecification, classSpecification2);
    assertEquals(classSpecification.hashCode(), classSpecification2.hashCode());
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}, and {@link ClassSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is equal.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link ClassSpecification#equals(Object)}
   *   <li>{@link ClassSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is equal; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual2() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments", 1, 1, null, "Class Name", "Extends Annotation Type", "Extends Class Name");
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments", 1, 1, null, "Class Name", "Extends Annotation Type", "Extends Class Name");

    // Act and Assert
    assertEquals(classSpecification, classSpecification2);
    assertEquals(classSpecification.hashCode(), classSpecification2.hashCode());
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}, and {@link ClassSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is equal.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link ClassSpecification#equals(Object)}
   *   <li>{@link ClassSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is equal; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual3() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            null,
            "Extends Annotation Type",
            "Extends Class Name");
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            null,
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertEquals(classSpecification, classSpecification2);
    assertEquals(classSpecification.hashCode(), classSpecification2.hashCode());
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}, and {@link ClassSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is equal.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link ClassSpecification#equals(Object)}
   *   <li>{@link ClassSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is equal; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual4() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments", 1, 1, "Annotation Type", "Class Name", null, "Extends Class Name");
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments", 1, 1, "Annotation Type", "Class Name", null, "Extends Class Name");

    // Act and Assert
    assertEquals(classSpecification, classSpecification2);
    assertEquals(classSpecification.hashCode(), classSpecification2.hashCode());
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}, and {@link ClassSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is equal.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link ClassSpecification#equals(Object)}
   *   <li>{@link ClassSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is equal; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual5() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments", 1, 1, "Annotation Type", "Class Name", "Extends Annotation Type", null);
    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments", 1, 1, "Annotation Type", "Class Name", "Extends Annotation Type", null);

    // Act and Assert
    assertEquals(classSpecification, classSpecification2);
    assertEquals(classSpecification.hashCode(), classSpecification2.hashCode());
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}, and {@link ClassSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is equal.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link ClassSpecification#equals(Object)}
   *   <li>{@link ClassSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is equal; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsEqual_thenReturnEqual6() {
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
    MemberSpecification fieldSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Annotation Type", "Annotation Type");
    classSpecification.addField(fieldSpecification);

    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    MemberSpecification fieldSpecification2 =
        new MemberSpecification(1, 1, "Annotation Type", "Annotation Type", "Annotation Type");
    classSpecification2.addField(fieldSpecification2);

    // Act and Assert
    assertEquals(classSpecification, classSpecification2);
    assertEquals(classSpecification.hashCode(), classSpecification2.hashCode());
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}, and {@link ClassSpecification#hashCode()}.
   *
   * <ul>
   *   <li>When other is same.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link ClassSpecification#equals(Object)}
   *   <li>{@link ClassSpecification#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is same; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEqualsAndHashCode_whenOtherIsSame_thenReturnEqual() {
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

    // Act and Assert
    assertEquals(classSpecification, classSpecification);
    int expectedHashCodeResult = classSpecification.hashCode();
    assertEquals(expectedHashCodeResult, classSpecification.hashCode());
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            0,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        classSpecification,
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name"));
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual2() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            0,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        classSpecification,
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name"));
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual3() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Class Name",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        classSpecification,
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name"));
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual4() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments", 1, 1, null, "Class Name", "Extends Annotation Type", "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        classSpecification,
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name"));
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual5() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Annotation Type",
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        classSpecification,
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name"));
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual6() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            null,
            "Extends Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        classSpecification,
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name"));
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual7() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Annotation Type",
            "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        classSpecification,
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name"));
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual8() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments", 1, 1, "Annotation Type", "Class Name", null, "Extends Class Name");

    // Act and Assert
    assertNotEquals(
        classSpecification,
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name"));
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual9() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Annotation Type");

    // Act and Assert
    assertNotEquals(
        classSpecification,
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name"));
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual10() {
    // Arrange
    ClassSpecification classSpecification =
        new ClassSpecification(
            "Comments", 1, 1, "Annotation Type", "Class Name", "Extends Annotation Type", null);

    // Act and Assert
    assertNotEquals(
        classSpecification,
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name"));
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual11() {
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
    MemberSpecification fieldSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Annotation Type", "Annotation Type");
    classSpecification.addField(fieldSpecification);

    // Act and Assert
    assertNotEquals(
        classSpecification,
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name"));
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual12() {
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
    MemberSpecification methodSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Annotation Type", "Annotation Type");
    classSpecification.addMethod(methodSpecification);

    // Act and Assert
    assertNotEquals(
        classSpecification,
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name"));
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual13() {
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

    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    MemberSpecification fieldSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Annotation Type", "Annotation Type");
    classSpecification2.addField(fieldSpecification);

    // Act and Assert
    assertNotEquals(classSpecification, classSpecification2);
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual14() {
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

    ClassSpecification classSpecification2 =
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name");
    MemberSpecification methodSpecification =
        new MemberSpecification(1, 1, "Annotation Type", "Annotation Type", "Annotation Type");
    classSpecification2.addMethod(methodSpecification);

    // Act and Assert
    assertNotEquals(classSpecification, classSpecification2);
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is {@code null}.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is 'null'; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsNull_thenReturnNotEqual() {
    // Arrange, Act and Assert
    assertNotEquals(
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name"),
        null);
  }

  /**
   * Test {@link ClassSpecification#equals(Object)}.
   *
   * <ul>
   *   <li>When other is wrong type.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassSpecification#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is wrong type; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ClassSpecification.equals(Object)",
    "int ClassSpecification.hashCode()"
  })
  void testEquals_whenOtherIsWrongType_thenReturnNotEqual() {
    // Arrange, Act and Assert
    assertNotEquals(
        new ClassSpecification(
            "Comments",
            1,
            1,
            "Annotation Type",
            "Class Name",
            "Extends Annotation Type",
            "Extends Class Name"),
        "Different type to ClassSpecification");
  }

  /**
   * Test {@link ClassSpecification#clone()}.
   *
   * <p>Method under test: {@link ClassSpecification#clone()}
   */
  @Test
  @DisplayName("Test clone()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Object ClassSpecification.clone()"})
  void testClone() {
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

    // Act
    Object actualCloneResult = classSpecification.clone();

    // Assert
    assertTrue(actualCloneResult instanceof ClassSpecification);
    assertEquals(classSpecification, actualCloneResult);
  }
}
