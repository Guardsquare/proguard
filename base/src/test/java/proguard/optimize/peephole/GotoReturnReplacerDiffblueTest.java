package proguard.optimize.peephole;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import proguard.classfile.instruction.BranchInstruction;

class GotoReturnReplacerDiffblueTest {
  /**
   * Test {@link GotoReturnReplacer#visitBranchInstruction(Clazz, Method, CodeAttribute, int,
   * BranchInstruction)}.
   *
   * <ul>
   *   <li>Then calls {@link CodeAttributeEditor#isModified(int)}.
   * </ul>
   *
   * <p>Method under test: {@link GotoReturnReplacer#visitBranchInstruction(Clazz, Method,
   * CodeAttribute, int, BranchInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction); then calls isModified(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GotoReturnReplacer.visitBranchInstruction(Clazz, Method, CodeAttribute, int, BranchInstruction)"
  })
  void testVisitBranchInstruction_thenCallsIsModified() {
    // Arrange
    CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
    when(codeAttributeEditor.isModified(anyInt())).thenReturn(true);
    GotoReturnReplacer gotoReturnReplacer = new GotoReturnReplacer(codeAttributeEditor);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    gotoReturnReplacer.visitBranchInstruction(
        clazz, method, codeAttribute, 2, new BranchInstruction((byte) -89, -89));

    // Assert
    verify(codeAttributeEditor).isModified(2);
  }
}
