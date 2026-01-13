package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
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
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.BranchInstruction;
import proguard.classfile.instruction.Instruction;

class EscapingClassMarkerDiffblueTest {
  /**
   * Test {@link EscapingClassMarker#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>Given one.
   *   <li>Then calls {@link BranchInstruction#stackPushCount(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link EscapingClassMarker#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction); given one; then calls stackPushCount(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void EscapingClassMarker.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction_givenOne_thenCallsStackPushCount() {
    // Arrange
    EscapingClassMarker escapingClassMarker = new EscapingClassMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    when(instruction.stackPushCount(Mockito.<Clazz>any())).thenReturn(1);

    // Act
    escapingClassMarker.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction).stackPushCount(isA(Clazz.class));
  }

  /**
   * Test {@link EscapingClassMarker#isClassEscaping(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link EscapingClassMarker#isClassEscaping(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isClassEscaping(Clazz); given ClassOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean EscapingClassMarker.isClassEscaping(Clazz)"})
  void testIsClassEscaping_givenClassOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ClassOptimizationInfo());

    // Act and Assert
    assertTrue(EscapingClassMarker.isClassEscaping(clazz));
  }

  /**
   * Test {@link EscapingClassMarker#isClassEscaping(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link EscapingClassMarker#isClassEscaping(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isClassEscaping(Clazz); given ProgramClassOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean EscapingClassMarker.isClassEscaping(Clazz)"})
  void testIsClassEscaping_givenProgramClassOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act and Assert
    assertFalse(EscapingClassMarker.isClassEscaping(clazz));
  }
}
