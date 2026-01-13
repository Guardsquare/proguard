package proguard.optimize.peephole;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.BranchInstruction;

class ReachableCodeMarkerDiffblueTest {
  /**
   * Test {@link ReachableCodeMarker#isReachable(int)} with {@code offset}.
   *
   * <ul>
   *   <li>When two.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ReachableCodeMarker#isReachable(int)}
   */
  @Test
  @DisplayName("Test isReachable(int) with 'offset'; when two; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ReachableCodeMarker.isReachable(int)"})
  void testIsReachableWithOffset_whenTwo_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ReachableCodeMarker().isReachable(2));
  }

  /**
   * Test {@link ReachableCodeMarker#isReachable(int, int)} with {@code startOffset}, {@code
   * endOffset}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ReachableCodeMarker#isReachable(int, int)}
   */
  @Test
  @DisplayName(
      "Test isReachable(int, int) with 'startOffset', 'endOffset'; when one; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ReachableCodeMarker.isReachable(int, int)"})
  void testIsReachableWithStartOffsetEndOffset_whenOne_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ReachableCodeMarker().isReachable(1, 3));
  }

  /**
   * Test {@link ReachableCodeMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link ReachableCodeMarker#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReachableCodeMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute() {
    // Arrange
    ReachableCodeMarker reachableCodeMarker = new ReachableCodeMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {-87, -96, 'A', -96, 'A', -96, 'A', -96});

    // Act
    reachableCodeMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert that nothing has changed
    assertFalse(reachableCodeMarker.isReachable(2));
  }

  /**
   * Test {@link ReachableCodeMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link ReachableCodeMarker#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReachableCodeMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute2() {
    // Arrange
    ReachableCodeMarker reachableCodeMarker = new ReachableCodeMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', -87, 'A', -96, 'A', -96, 'A', -96});

    // Act
    reachableCodeMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert that nothing has changed
    assertFalse(reachableCodeMarker.isReachable(2));
  }

  /**
   * Test {@link ReachableCodeMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link ReachableCodeMarker#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReachableCodeMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute3() {
    // Arrange
    ReachableCodeMarker reachableCodeMarker = new ReachableCodeMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(
            1,
            3,
            3,
            3,
            new byte[] {-84, 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3});

    // Act
    reachableCodeMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert that nothing has changed
    assertFalse(reachableCodeMarker.isReachable(2));
  }

  /**
   * Test {@link ReachableCodeMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link ReachableCodeMarker#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReachableCodeMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute4() {
    // Arrange
    ReachableCodeMarker reachableCodeMarker = new ReachableCodeMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(
            1,
            3,
            3,
            3,
            new byte[] {-83, 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3});

    // Act
    reachableCodeMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert that nothing has changed
    assertFalse(reachableCodeMarker.isReachable(2));
  }

  /**
   * Test {@link ReachableCodeMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then {@link ReachableCodeMarker} (default constructor) Reachable is two.
   * </ul>
   *
   * <p>Method under test: {@link ReachableCodeMarker#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then ReachableCodeMarker (default constructor) Reachable is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReachableCodeMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenReachableCodeMarkerReachableIsTwo() {
    // Arrange
    ReachableCodeMarker reachableCodeMarker = new ReachableCodeMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(
            1,
            3,
            3,
            3,
            new byte[] {'A', 3, -87, 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3});

    // Act
    reachableCodeMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    assertTrue(reachableCodeMarker.isReachable(2));
  }

  /**
   * Test {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method, CodeAttribute, int,
   * BranchInstruction)}.
   *
   * <ul>
   *   <li>When {@code A}.
   *   <li>Then {@link ReachableCodeMarker} (default constructor) Reachable is two.
   * </ul>
   *
   * <p>Method under test: {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method,
   * CodeAttribute, int, BranchInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction); when 'A'; then ReachableCodeMarker (default constructor) Reachable is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ReachableCodeMarker.visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)"
  })
  void testVisitBranchInstruction_whenA_thenReachableCodeMarkerReachableIsTwo() {
    // Arrange
    ReachableCodeMarker reachableCodeMarker = new ReachableCodeMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 'X', 'A', -87, 'A', 'X', 'A', 'X'});

    // Act
    reachableCodeMarker.visitBranchInstruction(
        clazz, method, codeAttribute, 2, new BranchInstruction());

    // Assert
    assertTrue(reachableCodeMarker.isReachable(2));
  }

  /**
   * Test {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method, CodeAttribute, int,
   * BranchInstruction)}.
   *
   * <ul>
   *   <li>When {@code A}.
   *   <li>Then {@link ReachableCodeMarker} (default constructor) Reachable is two.
   * </ul>
   *
   * <p>Method under test: {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method,
   * CodeAttribute, int, BranchInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction); when 'A'; then ReachableCodeMarker (default constructor) Reachable is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ReachableCodeMarker.visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)"
  })
  void testVisitBranchInstruction_whenA_thenReachableCodeMarkerReachableIsTwo2() {
    // Arrange
    ReachableCodeMarker reachableCodeMarker = new ReachableCodeMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 'X', 'A', 'X', -87, 'X', 'A', 'X'});

    // Act
    reachableCodeMarker.visitBranchInstruction(
        clazz, method, codeAttribute, 2, new BranchInstruction());

    // Assert
    assertTrue(reachableCodeMarker.isReachable(2));
  }

  /**
   * Test {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method, CodeAttribute, int,
   * BranchInstruction)}.
   *
   * <ul>
   *   <li>When {@code X}.
   *   <li>Then {@link ReachableCodeMarker} (default constructor) Reachable is two.
   * </ul>
   *
   * <p>Method under test: {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method,
   * CodeAttribute, int, BranchInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction); when 'X'; then ReachableCodeMarker (default constructor) Reachable is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ReachableCodeMarker.visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)"
  })
  void testVisitBranchInstruction_whenX_thenReachableCodeMarkerReachableIsTwo() {
    // Arrange
    ReachableCodeMarker reachableCodeMarker = new ReachableCodeMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 'X', -87, 'X', 'A', 'X', 'A', 'X'});

    // Act
    reachableCodeMarker.visitBranchInstruction(
        clazz, method, codeAttribute, 2, new BranchInstruction());

    // Assert
    assertTrue(reachableCodeMarker.isReachable(2));
  }

  /**
   * Test {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method, CodeAttribute, int,
   * BranchInstruction)}.
   *
   * <ul>
   *   <li>When {@code X}.
   *   <li>Then {@link ReachableCodeMarker} (default constructor) Reachable is two.
   * </ul>
   *
   * <p>Method under test: {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method,
   * CodeAttribute, int, BranchInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction); when 'X'; then ReachableCodeMarker (default constructor) Reachable is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ReachableCodeMarker.visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)"
  })
  void testVisitBranchInstruction_whenX_thenReachableCodeMarkerReachableIsTwo2() {
    // Arrange
    ReachableCodeMarker reachableCodeMarker = new ReachableCodeMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 'X', -84, 'X', 'A', 'X', 'A', 'X'});

    // Act
    reachableCodeMarker.visitBranchInstruction(
        clazz, method, codeAttribute, 2, new BranchInstruction());

    // Assert
    assertTrue(reachableCodeMarker.isReachable(2));
  }

  /**
   * Test {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method, CodeAttribute, int,
   * BranchInstruction)}.
   *
   * <ul>
   *   <li>When {@code X}.
   *   <li>Then {@link ReachableCodeMarker} (default constructor) Reachable is two.
   * </ul>
   *
   * <p>Method under test: {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method,
   * CodeAttribute, int, BranchInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction); when 'X'; then ReachableCodeMarker (default constructor) Reachable is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ReachableCodeMarker.visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)"
  })
  void testVisitBranchInstruction_whenX_thenReachableCodeMarkerReachableIsTwo3() {
    // Arrange
    ReachableCodeMarker reachableCodeMarker = new ReachableCodeMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 'X', -83, 'X', 'A', 'X', 'A', 'X'});

    // Act
    reachableCodeMarker.visitBranchInstruction(
        clazz, method, codeAttribute, 2, new BranchInstruction());

    // Assert
    assertTrue(reachableCodeMarker.isReachable(2));
  }

  /**
   * Test {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method, CodeAttribute, int,
   * BranchInstruction)}.
   *
   * <ul>
   *   <li>When {@code X}.
   *   <li>Then {@link ReachableCodeMarker} (default constructor) Reachable is two.
   * </ul>
   *
   * <p>Method under test: {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method,
   * CodeAttribute, int, BranchInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction); when 'X'; then ReachableCodeMarker (default constructor) Reachable is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ReachableCodeMarker.visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)"
  })
  void testVisitBranchInstruction_whenX_thenReachableCodeMarkerReachableIsTwo4() {
    // Arrange
    ReachableCodeMarker reachableCodeMarker = new ReachableCodeMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 'X', -82, 'X', 'A', 'X', 'A', 'X'});

    // Act
    reachableCodeMarker.visitBranchInstruction(
        clazz, method, codeAttribute, 2, new BranchInstruction());

    // Assert
    assertTrue(reachableCodeMarker.isReachable(2));
  }

  /**
   * Test {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method, CodeAttribute, int,
   * BranchInstruction)}.
   *
   * <ul>
   *   <li>When {@code X}.
   *   <li>Then {@link ReachableCodeMarker} (default constructor) Reachable is two.
   * </ul>
   *
   * <p>Method under test: {@link ReachableCodeMarker#visitBranchInstruction(Clazz, Method,
   * CodeAttribute, int, BranchInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction); when 'X'; then ReachableCodeMarker (default constructor) Reachable is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ReachableCodeMarker.visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)"
  })
  void testVisitBranchInstruction_whenX_thenReachableCodeMarkerReachableIsTwo5() {
    // Arrange
    ReachableCodeMarker reachableCodeMarker = new ReachableCodeMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 'X', -81, 'X', 'A', 'X', 'A', 'X'});

    // Act
    reachableCodeMarker.visitBranchInstruction(
        clazz, method, codeAttribute, 2, new BranchInstruction());

    // Assert
    assertTrue(reachableCodeMarker.isReachable(2));
  }

  /**
   * Test new {@link ReachableCodeMarker} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link ReachableCodeMarker}
   */
  @Test
  @DisplayName("Test new ReachableCodeMarker (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReachableCodeMarker.<init>()"})
  void testNewReachableCodeMarker() {
    // Arrange, Act and Assert
    assertFalse(new ReachableCodeMarker().isReachable(2));
  }
}
