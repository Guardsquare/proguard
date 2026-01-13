package proguard.obfuscate.util;

import static org.mockito.ArgumentMatchers.isA;
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
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.visitor.MemberVisitor;
import proguard.obfuscate.kotlin.KotlinIntrinsicsReplacementSequences;

class InstructionSequenceObfuscatorDiffblueTest {
  /**
   * Test {@link InstructionSequenceObfuscator#InstructionSequenceObfuscator(ReplacementSequences)}.
   *
   * <ul>
   *   <li>Then calls {@link ReplacementSequences#getConstants()}.
   * </ul>
   *
   * <p>Method under test: {@link
   * InstructionSequenceObfuscator#InstructionSequenceObfuscator(ReplacementSequences)}
   */
  @Test
  @DisplayName(
      "Test new InstructionSequenceObfuscator(ReplacementSequences); then calls getConstants()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InstructionSequenceObfuscator.<init>(ReplacementSequences)"})
  void testNewInstructionSequenceObfuscator_thenCallsGetConstants() {
    // Arrange
    ReplacementSequences replacementSequences = mock(ReplacementSequences.class);
    when(replacementSequences.getConstants()).thenReturn(new Constant[] {new ClassConstant()});
    when(replacementSequences.getSequences()).thenReturn(new Instruction[][][] {});

    // Act
    new InstructionSequenceObfuscator(replacementSequences);

    // Assert
    verify(replacementSequences).getConstants();
    verify(replacementSequences).getSequences();
  }

  /**
   * Test {@link InstructionSequenceObfuscator#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#methodsAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link InstructionSequenceObfuscator#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then calls methodsAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InstructionSequenceObfuscator.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsMethodsAccept() {
    // Arrange
    InstructionSequenceObfuscator instructionSequenceObfuscator =
        new InstructionSequenceObfuscator(
            new KotlinIntrinsicsReplacementSequences(
                KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool));

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).methodsAccept(Mockito.<MemberVisitor>any());

    // Act
    instructionSequenceObfuscator.visitProgramClass(programClass);

    // Assert
    verify(programClass).methodsAccept(isA(MemberVisitor.class));
  }
}
