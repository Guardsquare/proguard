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
import proguard.optimize.info.MethodOptimizationInfo;

class SingleInvocationMethodInlinerDiffblueTest {
  /**
   * Test {@link SingleInvocationMethodInliner#SingleInvocationMethodInliner(boolean, boolean,
   * boolean)}.
   *
   * <ul>
   *   <li>Then return {@link MethodInliner#maxResultingCodeLength} is {@code 2000}.
   * </ul>
   *
   * <p>Method under test: {@link
   * SingleInvocationMethodInliner#SingleInvocationMethodInliner(boolean, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test new SingleInvocationMethodInliner(boolean, boolean, boolean); then return maxResultingCodeLength is '2000'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SingleInvocationMethodInliner.<init>(boolean, boolean, boolean)"})
  void testNewSingleInvocationMethodInliner_thenReturnMaxResultingCodeLengthIs2000() {
    // Arrange and Act
    SingleInvocationMethodInliner actualSingleInvocationMethodInliner =
        new SingleInvocationMethodInliner(true, true, true);

    // Assert
    assertNull(actualSingleInvocationMethodInliner.extraInlinedInvocationVisitor);
    assertEquals(2000, actualSingleInvocationMethodInliner.maxResultingCodeLength);
    assertTrue(actualSingleInvocationMethodInliner.allowAccessModification);
    assertTrue(actualSingleInvocationMethodInliner.android);
    assertTrue(actualSingleInvocationMethodInliner.microEdition);
    assertTrue(actualSingleInvocationMethodInliner.usesOptimizationInfo);
  }

  /**
   * Test {@link SingleInvocationMethodInliner#SingleInvocationMethodInliner(boolean, boolean,
   * boolean)}.
   *
   * <ul>
   *   <li>Then return {@link MethodInliner#maxResultingCodeLength} is {@code 7000}.
   * </ul>
   *
   * <p>Method under test: {@link
   * SingleInvocationMethodInliner#SingleInvocationMethodInliner(boolean, boolean, boolean)}
   */
  @Test
  @DisplayName(
      "Test new SingleInvocationMethodInliner(boolean, boolean, boolean); then return maxResultingCodeLength is '7000'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SingleInvocationMethodInliner.<init>(boolean, boolean, boolean)"})
  void testNewSingleInvocationMethodInliner_thenReturnMaxResultingCodeLengthIs7000() {
    // Arrange and Act
    SingleInvocationMethodInliner actualSingleInvocationMethodInliner =
        new SingleInvocationMethodInliner(false, true, true);

    // Assert
    assertNull(actualSingleInvocationMethodInliner.extraInlinedInvocationVisitor);
    assertEquals(7000, actualSingleInvocationMethodInliner.maxResultingCodeLength);
    assertFalse(actualSingleInvocationMethodInliner.microEdition);
    assertTrue(actualSingleInvocationMethodInliner.allowAccessModification);
    assertTrue(actualSingleInvocationMethodInliner.android);
    assertTrue(actualSingleInvocationMethodInliner.usesOptimizationInfo);
  }

  /**
   * Test {@link SingleInvocationMethodInliner#SingleInvocationMethodInliner(boolean, boolean,
   * boolean, InstructionVisitor)}.
   *
   * <ul>
   *   <li>Then return {@link MethodInliner#maxResultingCodeLength} is {@code 2000}.
   * </ul>
   *
   * <p>Method under test: {@link
   * SingleInvocationMethodInliner#SingleInvocationMethodInliner(boolean, boolean, boolean,
   * InstructionVisitor)}
   */
  @Test
  @DisplayName(
      "Test new SingleInvocationMethodInliner(boolean, boolean, boolean, InstructionVisitor); then return maxResultingCodeLength is '2000'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SingleInvocationMethodInliner.<init>(boolean, boolean, boolean, InstructionVisitor)"
  })
  void testNewSingleInvocationMethodInliner_thenReturnMaxResultingCodeLengthIs20002() {
    // Arrange
    CodeAttributeEditor extraInlinedInvocationVisitor = new CodeAttributeEditor();

    // Act
    SingleInvocationMethodInliner actualSingleInvocationMethodInliner =
        new SingleInvocationMethodInliner(true, true, true, extraInlinedInvocationVisitor);

    // Assert
    InstructionVisitor instructionVisitor =
        actualSingleInvocationMethodInliner.extraInlinedInvocationVisitor;
    assertTrue(instructionVisitor instanceof CodeAttributeEditor);
    assertEquals(2000, actualSingleInvocationMethodInliner.maxResultingCodeLength);
    assertFalse(((CodeAttributeEditor) instructionVisitor).isModified());
    assertTrue(actualSingleInvocationMethodInliner.allowAccessModification);
    assertTrue(actualSingleInvocationMethodInliner.android);
    assertTrue(actualSingleInvocationMethodInliner.microEdition);
    assertTrue(actualSingleInvocationMethodInliner.usesOptimizationInfo);
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
   * Test {@link SingleInvocationMethodInliner#SingleInvocationMethodInliner(boolean, boolean,
   * boolean, InstructionVisitor)}.
   *
   * <ul>
   *   <li>Then return {@link MethodInliner#maxResultingCodeLength} is {@code 7000}.
   * </ul>
   *
   * <p>Method under test: {@link
   * SingleInvocationMethodInliner#SingleInvocationMethodInliner(boolean, boolean, boolean,
   * InstructionVisitor)}
   */
  @Test
  @DisplayName(
      "Test new SingleInvocationMethodInliner(boolean, boolean, boolean, InstructionVisitor); then return maxResultingCodeLength is '7000'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SingleInvocationMethodInliner.<init>(boolean, boolean, boolean, InstructionVisitor)"
  })
  void testNewSingleInvocationMethodInliner_thenReturnMaxResultingCodeLengthIs70002() {
    // Arrange
    CodeAttributeEditor extraInlinedInvocationVisitor = new CodeAttributeEditor();

    // Act
    SingleInvocationMethodInliner actualSingleInvocationMethodInliner =
        new SingleInvocationMethodInliner(false, true, true, extraInlinedInvocationVisitor);

    // Assert
    InstructionVisitor instructionVisitor =
        actualSingleInvocationMethodInliner.extraInlinedInvocationVisitor;
    assertTrue(instructionVisitor instanceof CodeAttributeEditor);
    assertEquals(7000, actualSingleInvocationMethodInliner.maxResultingCodeLength);
    assertFalse(((CodeAttributeEditor) instructionVisitor).isModified());
    assertFalse(actualSingleInvocationMethodInliner.microEdition);
    assertTrue(actualSingleInvocationMethodInliner.allowAccessModification);
    assertTrue(actualSingleInvocationMethodInliner.android);
    assertTrue(actualSingleInvocationMethodInliner.usesOptimizationInfo);
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
   * Test {@link SingleInvocationMethodInliner#shouldInline(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link SingleInvocationMethodInliner#shouldInline(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test shouldInline(Clazz, Method, CodeAttribute); given MethodOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean SingleInvocationMethodInliner.shouldInline(Clazz, Method, CodeAttribute)"
  })
  void testShouldInline_givenMethodOptimizationInfo_thenReturnFalse() {
    // Arrange
    SingleInvocationMethodInliner singleInvocationMethodInliner =
        new SingleInvocationMethodInliner(true, true, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertFalse(singleInvocationMethodInliner.shouldInline(clazz, method, new CodeAttribute()));
  }
}
