package proguard.optimize.peephole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
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
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.visitor.InstructionVisitor;

class ShortMethodInlinerDiffblueTest {
  /**
   * Test {@link ShortMethodInliner#ShortMethodInliner(boolean, boolean, boolean,
   * InstructionVisitor)}.
   *
   * <ul>
   *   <li>Then return {@link MethodInliner#maxResultingCodeLength} is {@code 2000}.
   * </ul>
   *
   * <p>Method under test: {@link ShortMethodInliner#ShortMethodInliner(boolean, boolean, boolean,
   * InstructionVisitor)}
   */
  @Test
  @DisplayName(
      "Test new ShortMethodInliner(boolean, boolean, boolean, InstructionVisitor); then return maxResultingCodeLength is '2000'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ShortMethodInliner.<init>(boolean, boolean, boolean, InstructionVisitor)"
  })
  void testNewShortMethodInliner_thenReturnMaxResultingCodeLengthIs2000() {
    // Arrange
    CodeAttributeEditor extraInlinedInvocationVisitor = new CodeAttributeEditor();

    // Act
    ShortMethodInliner actualShortMethodInliner =
        new ShortMethodInliner(true, true, true, extraInlinedInvocationVisitor);

    // Assert
    InstructionVisitor instructionVisitor = actualShortMethodInliner.extraInlinedInvocationVisitor;
    assertTrue(instructionVisitor instanceof CodeAttributeEditor);
    assertEquals(2000, actualShortMethodInliner.maxResultingCodeLength);
    assertFalse(((CodeAttributeEditor) instructionVisitor).isModified());
    assertTrue(actualShortMethodInliner.allowAccessModification);
    assertTrue(actualShortMethodInliner.android);
    assertTrue(actualShortMethodInliner.microEdition);
    assertTrue(actualShortMethodInliner.usesOptimizationInfo);
    assertSame(
        extraInlinedInvocationVisitor.deleted, ((CodeAttributeEditor) instructionVisitor).deleted);
    assertSame(
        extraInlinedInvocationVisitor.postInsertions,
        ((CodeAttributeEditor) instructionVisitor).postInsertions);
    assertSame(
        extraInlinedInvocationVisitor.preInsertions,
        ((CodeAttributeEditor) instructionVisitor).preInsertions);
    assertSame(
        extraInlinedInvocationVisitor.preOffsetInsertions,
        ((CodeAttributeEditor) instructionVisitor).preOffsetInsertions);
    assertSame(
        extraInlinedInvocationVisitor.replacements,
        ((CodeAttributeEditor) instructionVisitor).replacements);
  }

  /**
   * Test {@link ShortMethodInliner#ShortMethodInliner(boolean, boolean, boolean)}.
   *
   * <ul>
   *   <li>When {@code false}.
   *   <li>Then return {@link MethodInliner#maxResultingCodeLength} is {@code 7000}.
   * </ul>
   *
   * <p>Method under test: {@link ShortMethodInliner#ShortMethodInliner(boolean, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test new ShortMethodInliner(boolean, boolean, boolean); when 'false'; then return maxResultingCodeLength is '7000'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortMethodInliner.<init>(boolean, boolean, boolean)"})
  void testNewShortMethodInliner_whenFalse_thenReturnMaxResultingCodeLengthIs7000() {
    // Arrange and Act
    ShortMethodInliner actualShortMethodInliner = new ShortMethodInliner(false, true, true);

    // Assert
    assertNull(actualShortMethodInliner.extraInlinedInvocationVisitor);
    assertEquals(7000, actualShortMethodInliner.maxResultingCodeLength);
    assertFalse(actualShortMethodInliner.microEdition);
    assertTrue(actualShortMethodInliner.allowAccessModification);
    assertTrue(actualShortMethodInliner.android);
    assertTrue(actualShortMethodInliner.usesOptimizationInfo);
  }

  /**
   * Test {@link ShortMethodInliner#ShortMethodInliner(boolean, boolean, boolean,
   * InstructionVisitor)}.
   *
   * <ul>
   *   <li>When {@code false}.
   *   <li>Then return {@link MethodInliner#maxResultingCodeLength} is {@code 7000}.
   * </ul>
   *
   * <p>Method under test: {@link ShortMethodInliner#ShortMethodInliner(boolean, boolean, boolean,
   * InstructionVisitor)}
   */
  @Test
  @DisplayName(
      "Test new ShortMethodInliner(boolean, boolean, boolean, InstructionVisitor); when 'false'; then return maxResultingCodeLength is '7000'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ShortMethodInliner.<init>(boolean, boolean, boolean, InstructionVisitor)"
  })
  void testNewShortMethodInliner_whenFalse_thenReturnMaxResultingCodeLengthIs70002() {
    // Arrange
    CodeAttributeEditor extraInlinedInvocationVisitor = new CodeAttributeEditor();

    // Act
    ShortMethodInliner actualShortMethodInliner =
        new ShortMethodInliner(false, true, true, extraInlinedInvocationVisitor);

    // Assert
    InstructionVisitor instructionVisitor = actualShortMethodInliner.extraInlinedInvocationVisitor;
    assertTrue(instructionVisitor instanceof CodeAttributeEditor);
    assertEquals(7000, actualShortMethodInliner.maxResultingCodeLength);
    assertFalse(((CodeAttributeEditor) instructionVisitor).isModified());
    assertFalse(actualShortMethodInliner.microEdition);
    assertTrue(actualShortMethodInliner.allowAccessModification);
    assertTrue(actualShortMethodInliner.android);
    assertTrue(actualShortMethodInliner.usesOptimizationInfo);
    assertSame(
        extraInlinedInvocationVisitor.deleted, ((CodeAttributeEditor) instructionVisitor).deleted);
    assertSame(
        extraInlinedInvocationVisitor.postInsertions,
        ((CodeAttributeEditor) instructionVisitor).postInsertions);
    assertSame(
        extraInlinedInvocationVisitor.preInsertions,
        ((CodeAttributeEditor) instructionVisitor).preInsertions);
    assertSame(
        extraInlinedInvocationVisitor.preOffsetInsertions,
        ((CodeAttributeEditor) instructionVisitor).preOffsetInsertions);
    assertSame(
        extraInlinedInvocationVisitor.replacements,
        ((CodeAttributeEditor) instructionVisitor).replacements);
  }

  /**
   * Test {@link ShortMethodInliner#ShortMethodInliner(boolean, boolean, boolean)}.
   *
   * <ul>
   *   <li>When {@code true}.
   *   <li>Then return {@link MethodInliner#maxResultingCodeLength} is {@code 2000}.
   * </ul>
   *
   * <p>Method under test: {@link ShortMethodInliner#ShortMethodInliner(boolean, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test new ShortMethodInliner(boolean, boolean, boolean); when 'true'; then return maxResultingCodeLength is '2000'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ShortMethodInliner.<init>(boolean, boolean, boolean)"})
  void testNewShortMethodInliner_whenTrue_thenReturnMaxResultingCodeLengthIs2000() {
    // Arrange and Act
    ShortMethodInliner actualShortMethodInliner = new ShortMethodInliner(true, true, true);

    // Assert
    assertNull(actualShortMethodInliner.extraInlinedInvocationVisitor);
    assertEquals(2000, actualShortMethodInliner.maxResultingCodeLength);
    assertTrue(actualShortMethodInliner.allowAccessModification);
    assertTrue(actualShortMethodInliner.android);
    assertTrue(actualShortMethodInliner.microEdition);
    assertTrue(actualShortMethodInliner.usesOptimizationInfo);
  }

  /**
   * Test {@link ShortMethodInliner#shouldInline(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Given {@link MethodInliner#MAXIMUM_INLINED_CODE_LENGTH_android}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ShortMethodInliner#shouldInline(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test shouldInline(Clazz, Method, CodeAttribute); given MAXIMUM_INLINED_CODE_LENGTH_android; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ShortMethodInliner.shouldInline(Clazz, Method, CodeAttribute)"})
  void testShouldInline_givenMAXIMUM_INLINED_CODE_LENGTH_android_thenReturnFalse() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, false, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute(1);
    codeAttribute.u4codeLength = MethodInliner.MAXIMUM_INLINED_CODE_LENGTH_android;

    // Act and Assert
    assertFalse(shortMethodInliner.shouldInline(clazz, method, codeAttribute));
  }

  /**
   * Test {@link ShortMethodInliner#shouldInline(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Given {@link MethodInliner#MAXIMUM_INLINED_CODE_LENGTH_JVM}.
   * </ul>
   *
   * <p>Method under test: {@link ShortMethodInliner#shouldInline(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test shouldInline(Clazz, Method, CodeAttribute); given MAXIMUM_INLINED_CODE_LENGTH_JVM")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ShortMethodInliner.shouldInline(Clazz, Method, CodeAttribute)"})
  void testShouldInline_givenMaximum_inlined_code_length_jvm() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, false, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute(1);
    codeAttribute.u4codeLength = MethodInliner.MAXIMUM_INLINED_CODE_LENGTH_JVM;

    // Act and Assert
    assertTrue(shortMethodInliner.shouldInline(clazz, method, codeAttribute));
  }

  /**
   * Test {@link ShortMethodInliner#shouldInline(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>When {@link CodeAttribute#CodeAttribute()}.
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ShortMethodInliner#shouldInline(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test shouldInline(Clazz, Method, CodeAttribute); when CodeAttribute(); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ShortMethodInliner.shouldInline(Clazz, Method, CodeAttribute)"})
  void testShouldInline_whenCodeAttribute_thenReturnTrue() {
    // Arrange
    ShortMethodInliner shortMethodInliner = new ShortMethodInliner(true, true, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act and Assert
    assertTrue(shortMethodInliner.shouldInline(clazz, method, new CodeAttribute()));
  }
}
