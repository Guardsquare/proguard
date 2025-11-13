package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.VariableInstruction;

class VariableUsageMarkerDiffblueTest {
  /**
   * Test {@link VariableUsageMarker#isVariableUsed(int)}.
   *
   * <ul>
   *   <li>When one.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link VariableUsageMarker#isVariableUsed(int)}
   */
  @Test
  @DisplayName("Test isVariableUsed(int); when one; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean VariableUsageMarker.isVariableUsed(int)"})
  void testIsVariableUsed_whenOne_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new VariableUsageMarker().isVariableUsed(1));
  }

  /**
   * Test {@link VariableUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link VariableUsageMarker#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void VariableUsageMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute() {
    // Arrange
    VariableUsageMarker variableUsageMarker = new VariableUsageMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(
            1,
            3,
            3,
            3,
            new byte[] {'A', 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3, 'A', 3});

    // Act
    variableUsageMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert that nothing has changed
    assertFalse(variableUsageMarker.isVariableUsed(1));
  }

  /**
   * Test {@link VariableUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then {@link VariableUsageMarker} (default constructor) VariableUsed is one.
   * </ul>
   *
   * <p>Method under test: {@link VariableUsageMarker#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then VariableUsageMarker (default constructor) VariableUsed is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void VariableUsageMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenVariableUsageMarkerVariableUsedIsOne()
      throws UnsupportedEncodingException {
    // Arrange
    VariableUsageMarker variableUsageMarker = new VariableUsageMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute(1, 3, 3, 3, "A@A@A@A@".getBytes("UTF-8"));

    // Act
    variableUsageMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    assertTrue(variableUsageMarker.isVariableUsed(1));
  }

  /**
   * Test {@link VariableUsageMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>When {@link CodeAttribute#CodeAttribute()}.
   * </ul>
   *
   * <p>Method under test: {@link VariableUsageMarker#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute); when CodeAttribute()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void VariableUsageMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_whenCodeAttribute() {
    // Arrange
    VariableUsageMarker variableUsageMarker = new VariableUsageMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act
    variableUsageMarker.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert that nothing has changed
    assertFalse(variableUsageMarker.isVariableUsed(1));
  }

  /**
   * Test {@link VariableUsageMarker#visitVariableInstruction(Clazz, Method, CodeAttribute, int,
   * VariableInstruction)}.
   *
   * <ul>
   *   <li>Then {@link VariableUsageMarker} (default constructor) VariableUsed is one.
   * </ul>
   *
   * <p>Method under test: {@link VariableUsageMarker#visitVariableInstruction(Clazz, Method,
   * CodeAttribute, int, VariableInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitVariableInstruction(Clazz, Method, CodeAttribute, int, VariableInstruction); then VariableUsageMarker (default constructor) VariableUsed is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void VariableUsageMarker.visitVariableInstruction(Clazz, Method, CodeAttribute, int, VariableInstruction)"
  })
  void testVisitVariableInstruction_thenVariableUsageMarkerVariableUsedIsOne() {
    // Arrange
    VariableUsageMarker variableUsageMarker = new VariableUsageMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    variableUsageMarker.visitVariableInstruction(
        clazz, method, codeAttribute, 2, new VariableInstruction(Byte.MAX_VALUE));

    // Assert
    assertTrue(variableUsageMarker.isVariableUsed(1));
  }

  /**
   * Test {@link VariableUsageMarker#visitVariableInstruction(Clazz, Method, CodeAttribute, int,
   * VariableInstruction)}.
   *
   * <ul>
   *   <li>When {@link VariableInstruction#VariableInstruction()}.
   * </ul>
   *
   * <p>Method under test: {@link VariableUsageMarker#visitVariableInstruction(Clazz, Method,
   * CodeAttribute, int, VariableInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitVariableInstruction(Clazz, Method, CodeAttribute, int, VariableInstruction); when VariableInstruction()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void VariableUsageMarker.visitVariableInstruction(Clazz, Method, CodeAttribute, int, VariableInstruction)"
  })
  void testVisitVariableInstruction_whenVariableInstruction() {
    // Arrange
    VariableUsageMarker variableUsageMarker = new VariableUsageMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    variableUsageMarker.visitVariableInstruction(
        clazz, method, codeAttribute, 2, new VariableInstruction());

    // Assert that nothing has changed
    assertFalse(variableUsageMarker.isVariableUsed(1));
  }

  /**
   * Test {@link VariableUsageMarker#visitVariableInstruction(Clazz, Method, CodeAttribute, int,
   * VariableInstruction)}.
   *
   * <ul>
   *   <li>When {@link VariableInstruction#VariableInstruction(byte)} with opcode is {@code A}.
   * </ul>
   *
   * <p>Method under test: {@link VariableUsageMarker#visitVariableInstruction(Clazz, Method,
   * CodeAttribute, int, VariableInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitVariableInstruction(Clazz, Method, CodeAttribute, int, VariableInstruction); when VariableInstruction(byte) with opcode is 'A'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void VariableUsageMarker.visitVariableInstruction(Clazz, Method, CodeAttribute, int, VariableInstruction)"
  })
  void testVisitVariableInstruction_whenVariableInstructionWithOpcodeIsA() {
    // Arrange
    VariableUsageMarker variableUsageMarker = new VariableUsageMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    variableUsageMarker.visitVariableInstruction(
        clazz, method, codeAttribute, 2, new VariableInstruction((byte) 'A'));

    // Assert that nothing has changed
    assertFalse(variableUsageMarker.isVariableUsed(1));
  }

  /**
   * Test new {@link VariableUsageMarker} (default constructor).
   *
   * <p>Method under test: default or parameterless constructor of {@link VariableUsageMarker}
   */
  @Test
  @DisplayName("Test new VariableUsageMarker (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void VariableUsageMarker.<init>()"})
  void testNewVariableUsageMarker() {
    // Arrange, Act and Assert
    assertFalse(new VariableUsageMarker().isVariableUsed(1));
  }
}
