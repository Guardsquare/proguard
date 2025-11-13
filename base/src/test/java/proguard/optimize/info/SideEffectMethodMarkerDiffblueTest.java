package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.BranchInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;

class SideEffectMethodMarkerDiffblueTest {
  /**
   * Test {@link SideEffectMethodMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link MethodOptimizationInfo#hasSideEffects()}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectMethodMarker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod); then calls hasSideEffects()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SideEffectMethodMarker.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenCallsHasSideEffects() {
    // Arrange
    SideEffectMethodMarker sideEffectMethodMarker = new SideEffectMethodMarker(true);
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasSideEffects()).thenReturn(false);
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    ProgramMethod programMethod = new ProgramMethod(288, 1, 1, referencedClasses);
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    sideEffectMethodMarker.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).hasSideEffects();
  }

  /**
   * Test {@link SideEffectMethodMarker#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>When {@link BranchInstruction} {@link BranchInstruction#accept(Clazz, Method,
   *       CodeAttribute, int, InstructionVisitor)} does nothing.
   *   <li>Then calls {@link BranchInstruction#accept(Clazz, Method, CodeAttribute, int,
   *       InstructionVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectMethodMarker#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction); when BranchInstruction accept(Clazz, Method, CodeAttribute, int, InstructionVisitor) does nothing; then calls accept(Clazz, Method, CodeAttribute, int, InstructionVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SideEffectMethodMarker.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction_whenBranchInstructionAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    SideEffectMethodMarker sideEffectMethodMarker = new SideEffectMethodMarker(true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    sideEffectMethodMarker.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction)
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            isA(InstructionVisitor.class));
  }

  /**
   * Test {@link SideEffectMethodMarker#hasSideEffects(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectMethodMarker#hasSideEffects(Method)}
   */
  @Test
  @DisplayName(
      "Test hasSideEffects(Method); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean SideEffectMethodMarker.hasSideEffects(Method)"})
  void testHasSideEffects_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(SideEffectMethodMarker.hasSideEffects(method));
  }
}
