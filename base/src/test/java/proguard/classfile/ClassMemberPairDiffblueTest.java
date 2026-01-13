package proguard.classfile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class ClassMemberPairDiffblueTest {
  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link ClassMemberPair#ClassMemberPair(Clazz, Member)}
   *   <li>{@link ClassMemberPair#toString()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ClassMemberPair.<init>(Clazz, Member)",
    "java.lang.String ClassMemberPair.toString()"
  })
  void testGettersAndSetters() {
    // Arrange
    LibraryClass clazz = new LibraryClass();

    // Act
    ClassMemberPair actualClassMemberPair = new ClassMemberPair(clazz, new LibraryField());

    // Assert
    assertEquals("null.nullnull", actualClassMemberPair.toString());
  }

  /**
   * Test {@link ClassMemberPair#getName()}.
   *
   * <ul>
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassMemberPair#getName()}
   */
  @Test
  @DisplayName("Test getName(); then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"java.lang.String ClassMemberPair.getName()"})
  void testGetName_thenReturnNull() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    ClassMemberPair classMemberPair = new ClassMemberPair(clazz, new LibraryField());

    // Act and Assert
    assertNull(classMemberPair.getName());
  }

  /**
   * Test {@link ClassMemberPair#equals(Object)}, and {@link ClassMemberPair#hashCode()}.
   *
   * <ul>
   *   <li>When other is same.
   *   <li>Then return equal.
   * </ul>
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link ClassMemberPair#equals(Object)}
   *   <li>{@link ClassMemberPair#hashCode()}
   * </ul>
   */
  @Test
  @DisplayName("Test equals(Object), and hashCode(); when other is same; then return equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassMemberPair.equals(Object)", "int ClassMemberPair.hashCode()"})
  void testEqualsAndHashCode_whenOtherIsSame_thenReturnEqual() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    ClassMemberPair classMemberPair = new ClassMemberPair(clazz, new LibraryField());

    // Act and Assert
    assertEquals(classMemberPair, classMemberPair);
    int expectedHashCodeResult = classMemberPair.hashCode();
    assertEquals(expectedHashCodeResult, classMemberPair.hashCode());
  }

  /**
   * Test {@link ClassMemberPair#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassMemberPair#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassMemberPair.equals(Object)", "int ClassMemberPair.hashCode()"})
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    ClassMemberPair classMemberPair = new ClassMemberPair(clazz, new LibraryField());
    LibraryClass clazz2 = new LibraryClass();
    ClassMemberPair classMemberPair2 = new ClassMemberPair(clazz2, new LibraryField());

    // Act and Assert
    assertNotEquals(classMemberPair, classMemberPair2);
  }

  /**
   * Test {@link ClassMemberPair#equals(Object)}.
   *
   * <ul>
   *   <li>When other is different.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassMemberPair#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is different; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassMemberPair.equals(Object)", "int ClassMemberPair.hashCode()"})
  void testEquals_whenOtherIsDifferent_thenReturnNotEqual2() {
    // Arrange
    ClassMemberPair classMemberPair = new ClassMemberPair(null, new LibraryField());
    ClassMemberPair classMemberPair2 = new ClassMemberPair(null, new LibraryField());

    // Act and Assert
    assertNotEquals(classMemberPair, classMemberPair2);
  }

  /**
   * Test {@link ClassMemberPair#equals(Object)}.
   *
   * <ul>
   *   <li>When other is {@code null}.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassMemberPair#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is 'null'; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassMemberPair.equals(Object)", "int ClassMemberPair.hashCode()"})
  void testEquals_whenOtherIsNull_thenReturnNotEqual() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    ClassMemberPair classMemberPair = new ClassMemberPair(clazz, new LibraryField());

    // Act and Assert
    assertNotEquals(classMemberPair, null);
  }

  /**
   * Test {@link ClassMemberPair#equals(Object)}.
   *
   * <ul>
   *   <li>When other is wrong type.
   *   <li>Then return not equal.
   * </ul>
   *
   * <p>Method under test: {@link ClassMemberPair#equals(Object)}
   */
  @Test
  @DisplayName("Test equals(Object); when other is wrong type; then return not equal")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ClassMemberPair.equals(Object)", "int ClassMemberPair.hashCode()"})
  void testEquals_whenOtherIsWrongType_thenReturnNotEqual() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    ClassMemberPair classMemberPair = new ClassMemberPair(clazz, new LibraryField());

    // Act and Assert
    assertNotEquals(classMemberPair, "Different type to ClassMemberPair");
  }
}
