package proguard.shrink;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.Member;
import proguard.classfile.visitor.ClassVisitor;

class ShortestUsageMarkDiffblueTest {
  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link ShortestUsageMark#ShortestUsageMark(String)}
   *   <li>{@link ShortestUsageMark#getReason()}
   *   <li>{@link ShortestUsageMark#isCertain()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ShortestUsageMark.<init>(String)",
    "String ShortestUsageMark.getReason()",
    "boolean ShortestUsageMark.isCertain()"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    ShortestUsageMark actualShortestUsageMark = new ShortestUsageMark("Just cause");
    String actualReason = actualShortestUsageMark.getReason();

    // Assert
    assertEquals("Just cause", actualReason);
    assertTrue(actualShortestUsageMark.isCertain());
  }

  /**
   * Test {@link ShortestUsageMark#ShortestUsageMark(ShortestUsageMark, String, int, Clazz)}.
   *
   * <ul>
   *   <li>Then return Reason is {@code Just cause}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsageMark#ShortestUsageMark(ShortestUsageMark, String,
   * int, Clazz)}
   */
  @Test
  @DisplayName(
      "Test new ShortestUsageMark(ShortestUsageMark, String, int, Clazz); then return Reason is 'Just cause'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsageMark.<init>(ShortestUsageMark, String, int, Clazz)"})
  void testNewShortestUsageMark_thenReturnReasonIsJustCause() {
    // Arrange
    ShortestUsageMark previousUsageMark = new ShortestUsageMark("Just cause");

    // Act
    ShortestUsageMark actualShortestUsageMark =
        new ShortestUsageMark(previousUsageMark, "Just cause", 1, new LibraryClass());

    // Assert
    assertEquals("Just cause", actualShortestUsageMark.getReason());
    assertTrue(actualShortestUsageMark.isCertain());
  }

  /**
   * Test {@link ShortestUsageMark#ShortestUsageMark(ShortestUsageMark, String, int, Clazz,
   * Member)}.
   *
   * <ul>
   *   <li>Then return Reason is {@code Just cause}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsageMark#ShortestUsageMark(ShortestUsageMark, String,
   * int, Clazz, Member)}
   */
  @Test
  @DisplayName(
      "Test new ShortestUsageMark(ShortestUsageMark, String, int, Clazz, Member); then return Reason is 'Just cause'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ShortestUsageMark.<init>(ShortestUsageMark, String, int, Clazz, Member)"
  })
  void testNewShortestUsageMark_thenReturnReasonIsJustCause2() {
    // Arrange
    ShortestUsageMark previousUsageMark = new ShortestUsageMark("Just cause");
    LibraryClass clazz = new LibraryClass();

    // Act
    ShortestUsageMark actualShortestUsageMark =
        new ShortestUsageMark(previousUsageMark, "Just cause", 1, clazz, new LibraryField());

    // Assert
    assertEquals("Just cause", actualShortestUsageMark.getReason());
    assertTrue(actualShortestUsageMark.isCertain());
  }

  /**
   * Test {@link ShortestUsageMark#ShortestUsageMark(ShortestUsageMark, boolean)}.
   *
   * <ul>
   *   <li>Then return Reason is {@code Just cause}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsageMark#ShortestUsageMark(ShortestUsageMark, boolean)}
   */
  @Test
  @DisplayName(
      "Test new ShortestUsageMark(ShortestUsageMark, boolean); then return Reason is 'Just cause'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsageMark.<init>(ShortestUsageMark, boolean)"})
  void testNewShortestUsageMark_thenReturnReasonIsJustCause3() {
    // Arrange and Act
    ShortestUsageMark actualShortestUsageMark =
        new ShortestUsageMark(new ShortestUsageMark("Just cause"), true);

    // Assert
    assertEquals("Just cause", actualShortestUsageMark.getReason());
    assertTrue(actualShortestUsageMark.isCertain());
  }

  /**
   * Test {@link ShortestUsageMark#isShorter(ShortestUsageMark)}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsageMark#isShorter(ShortestUsageMark)}
   */
  @Test
  @DisplayName("Test isShorter(ShortestUsageMark); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ShortestUsageMark.isShorter(ShortestUsageMark)"})
  void testIsShorter_thenReturnTrue() {
    // Arrange
    ShortestUsageMark shortestUsageMark = new ShortestUsageMark("Just cause");
    ShortestUsageMark previousUsageMark = new ShortestUsageMark("Just cause");
    ShortestUsageMark otherUsageMark =
        new ShortestUsageMark(previousUsageMark, "Just cause", 1, new LibraryClass());

    // Act
    boolean actualIsShorterResult = shortestUsageMark.isShorter(otherUsageMark);

    // Assert
    assertTrue(actualIsShorterResult);
  }

  /**
   * Test {@link ShortestUsageMark#isShorter(ShortestUsageMark)}.
   *
   * <ul>
   *   <li>When {@link ShortestUsageMark#ShortestUsageMark(String)} with reason is {@code Just
   *       cause}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsageMark#isShorter(ShortestUsageMark)}
   */
  @Test
  @DisplayName(
      "Test isShorter(ShortestUsageMark); when ShortestUsageMark(String) with reason is 'Just cause'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ShortestUsageMark.isShorter(ShortestUsageMark)"})
  void testIsShorter_whenShortestUsageMarkWithReasonIsJustCause_thenReturnFalse() {
    // Arrange
    ShortestUsageMark shortestUsageMark = new ShortestUsageMark("Just cause");

    // Act
    boolean actualIsShorterResult =
        shortestUsageMark.isShorter(new ShortestUsageMark("Just cause"));

    // Assert
    assertFalse(actualIsShorterResult);
  }

  /**
   * Test {@link ShortestUsageMark#isCausedBy(Clazz, Member)} with {@code clazz}, {@code member}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsageMark#isCausedBy(Clazz, Member)}
   */
  @Test
  @DisplayName(
      "Test isCausedBy(Clazz, Member) with 'clazz', 'member'; when LibraryClass(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ShortestUsageMark.isCausedBy(Clazz, Member)"})
  void testIsCausedByWithClazzMember_whenLibraryClass_thenReturnFalse() {
    // Arrange
    ShortestUsageMark shortestUsageMark = new ShortestUsageMark("Just cause");
    LibraryClass clazz = new LibraryClass();

    // Act and Assert
    assertFalse(shortestUsageMark.isCausedBy(clazz, new LibraryField()));
  }

  /**
   * Test {@link ShortestUsageMark#isCausedBy(Clazz)} with {@code clazz}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsageMark#isCausedBy(Clazz)}
   */
  @Test
  @DisplayName("Test isCausedBy(Clazz) with 'clazz'; when LibraryClass(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ShortestUsageMark.isCausedBy(Clazz)"})
  void testIsCausedByWithClazz_whenLibraryClass_thenReturnFalse() {
    // Arrange
    ShortestUsageMark shortestUsageMark = new ShortestUsageMark("Just cause");

    // Act and Assert
    assertFalse(shortestUsageMark.isCausedBy(new LibraryClass()));
  }

  /**
   * Test {@link ShortestUsageMark#isCausedByMember(Clazz)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsageMark#isCausedByMember(Clazz)}
   */
  @Test
  @DisplayName("Test isCausedByMember(Clazz); when LibraryClass(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ShortestUsageMark.isCausedByMember(Clazz)"})
  void testIsCausedByMember_whenLibraryClass_thenReturnFalse() {
    // Arrange
    ShortestUsageMark shortestUsageMark = new ShortestUsageMark("Just cause");

    // Act and Assert
    assertFalse(shortestUsageMark.isCausedByMember(new LibraryClass()));
  }

  /**
   * Test {@link ShortestUsageMark#acceptClassVisitor(ClassVisitor)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassVisitor#visitLibraryClass(LibraryClass)}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsageMark#acceptClassVisitor(ClassVisitor)}
   */
  @Test
  @DisplayName("Test acceptClassVisitor(ClassVisitor); then calls visitLibraryClass(LibraryClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortestUsageMark.acceptClassVisitor(ClassVisitor)"})
  void testAcceptClassVisitor_thenCallsVisitLibraryClass() {
    // Arrange
    ShortestUsageMark previousUsageMark = new ShortestUsageMark("Just cause");
    ShortestUsageMark shortestUsageMark =
        new ShortestUsageMark(previousUsageMark, "Just cause", 1, new LibraryClass());

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitLibraryClass(Mockito.<LibraryClass>any());

    // Act
    shortestUsageMark.acceptClassVisitor(classVisitor);

    // Assert
    verify(classVisitor).visitLibraryClass(isA(LibraryClass.class));
  }

  /**
   * Test {@link ShortestUsageMark#toString()}.
   *
   * <ul>
   *   <li>Then return {@code certain=true, depth=0: Just cause(none): (none)}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsageMark#toString()}
   */
  @Test
  @DisplayName("Test toString(); then return 'certain=true, depth=0: Just cause(none): (none)'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ShortestUsageMark.toString()"})
  void testToString_thenReturnCertainTrueDepth0JustCauseNoneNone() {
    // Arrange, Act and Assert
    assertEquals(
        "certain=true, depth=0: Just cause(none): (none)",
        new ShortestUsageMark("Just cause").toString());
  }

  /**
   * Test {@link ShortestUsageMark#toString()}.
   *
   * <ul>
   *   <li>Then return {@code certain=true, depth=1: Just causenull: (none)}.
   * </ul>
   *
   * <p>Method under test: {@link ShortestUsageMark#toString()}
   */
  @Test
  @DisplayName("Test toString(); then return 'certain=true, depth=1: Just causenull: (none)'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String ShortestUsageMark.toString()"})
  void testToString_thenReturnCertainTrueDepth1JustCausenullNone() {
    // Arrange
    ShortestUsageMark previousUsageMark = new ShortestUsageMark("Just cause");
    ShortestUsageMark shortestUsageMark =
        new ShortestUsageMark(previousUsageMark, "Just cause", 1, new LibraryClass());

    // Act and Assert
    assertEquals("certain=true, depth=1: Just causenull: (none)", shortestUsageMark.toString());
  }
}
