package proguard.optimize.gson;

import static org.mockito.Mockito.atLeast;
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
import proguard.classfile.instruction.Instruction;
import proguard.classfile.kotlin.KotlinConstants;

class GsonInstrumentationAdderDiffblueTest {
  /**
   * Test {@link GsonInstrumentationAdder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>Given {@code A}.
   *   <li>Then calls {@link BranchInstruction#actualOpcode()}.
   * </ul>
   *
   * <p>Method under test: {@link GsonInstrumentationAdder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction); given 'A'; then calls actualOpcode()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonInstrumentationAdder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction_givenA_thenCallsActualOpcode() {
    // Arrange
    GsonInstrumentationAdder gsonInstrumentationAdder =
        new GsonInstrumentationAdder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            new CodeAttributeEditor());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    when(instruction.actualOpcode()).thenReturn((byte) 'A');

    // Act
    gsonInstrumentationAdder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1)).actualOpcode();
  }
}
