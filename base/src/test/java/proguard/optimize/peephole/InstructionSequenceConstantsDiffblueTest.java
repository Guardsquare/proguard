package proguard.optimize.peephole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.kotlin.KotlinConstants;

class InstructionSequenceConstantsDiffblueTest {
  /**
   * Test {@link InstructionSequenceConstants#InstructionSequenceConstants(ClassPool, ClassPool)}.
   *
   * <ul>
   *   <li>When {@code null}.
   *   <li>Then return array length is ten.
   * </ul>
   *
   * <p>Method under test: {@link
   * InstructionSequenceConstants#InstructionSequenceConstants(ClassPool, ClassPool)}
   */
  @Test
  @DisplayName(
      "Test new InstructionSequenceConstants(ClassPool, ClassPool); when 'null'; then return array length is ten")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InstructionSequenceConstants.<init>(ClassPool, ClassPool)"})
  void testNewInstructionSequenceConstants_whenNull_thenReturnArrayLengthIsTen() {
    // Arrange and Act
    InstructionSequenceConstants actualInstructionSequenceConstants =
        new InstructionSequenceConstants(null, KotlinConstants.dummyClassPool);

    // Assert
    assertEquals(10, actualInstructionSequenceConstants.MATH_ANDROID_SEQUENCES.length);
    assertEquals(100, actualInstructionSequenceConstants.BRANCH_SEQUENCES.length);
    assertEquals(133, actualInstructionSequenceConstants.ARITHMETIC_SEQUENCES.length);
    assertEquals(14, actualInstructionSequenceConstants.CAST_SEQUENCES.length);
    assertEquals(150, actualInstructionSequenceConstants.STRING_SEQUENCES.length);
    assertEquals(19, actualInstructionSequenceConstants.FIELD_SEQUENCES.length);
    assertEquals(31, actualInstructionSequenceConstants.VARIABLE_SEQUENCES.length);
    assertEquals(337, actualInstructionSequenceConstants.CONSTANTS.length);
    assertEquals(54, actualInstructionSequenceConstants.OBJECT_SEQUENCES.length);
    assertEquals(8, actualInstructionSequenceConstants.MATH_SEQUENCES.length);
  }
}
