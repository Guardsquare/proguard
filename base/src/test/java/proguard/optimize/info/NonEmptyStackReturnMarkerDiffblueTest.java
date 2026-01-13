package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
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
import proguard.classfile.attribute.visitor.StackSizeComputer;
import proguard.classfile.instruction.SimpleInstruction;

class NonEmptyStackReturnMarkerDiffblueTest {
  /**
   * Test {@link NonEmptyStackReturnMarker#visitSimpleInstruction(Clazz, Method, CodeAttribute, int,
   * SimpleInstruction)}.
   *
   * <p>Method under test: {@link NonEmptyStackReturnMarker#visitSimpleInstruction(Clazz, Method,
   * CodeAttribute, int, SimpleInstruction)}
   */
  @Test
  @DisplayName("Test visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NonEmptyStackReturnMarker.visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)"
  })
  void testVisitSimpleInstruction() {
    // Arrange
    StackSizeComputer stackSizeComputer = mock(StackSizeComputer.class);
    when(stackSizeComputer.getStackSizeBefore(anyInt())).thenReturn(-84);
    when(stackSizeComputer.isReachable(anyInt())).thenReturn(true);
    NonEmptyStackReturnMarker nonEmptyStackReturnMarker =
        new NonEmptyStackReturnMarker(stackSizeComputer);
    LibraryClass clazz = new LibraryClass();
    Method method = mock(Method.class);
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    nonEmptyStackReturnMarker.visitSimpleInstruction(
        clazz, method, codeAttribute, 2, new SimpleInstruction((byte) -84));

    // Assert
    verify(stackSizeComputer).getStackSizeBefore(2);
    verify(stackSizeComputer).isReachable(2);
  }

  /**
   * Test {@link NonEmptyStackReturnMarker#visitSimpleInstruction(Clazz, Method, CodeAttribute, int,
   * SimpleInstruction)}.
   *
   * <ul>
   *   <li>Given {@link StackSizeComputer} {@link StackSizeComputer#isReachable(int)} return {@code
   *       false}.
   * </ul>
   *
   * <p>Method under test: {@link NonEmptyStackReturnMarker#visitSimpleInstruction(Clazz, Method,
   * CodeAttribute, int, SimpleInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction); given StackSizeComputer isReachable(int) return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NonEmptyStackReturnMarker.visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)"
  })
  void testVisitSimpleInstruction_givenStackSizeComputerIsReachableReturnFalse() {
    // Arrange
    StackSizeComputer stackSizeComputer = mock(StackSizeComputer.class);
    when(stackSizeComputer.isReachable(anyInt())).thenReturn(false);
    NonEmptyStackReturnMarker nonEmptyStackReturnMarker =
        new NonEmptyStackReturnMarker(stackSizeComputer);
    LibraryClass clazz = new LibraryClass();

    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setReturnsWithNonEmptyStack();

    Method method = mock(Method.class);
    when(method.getProcessingInfo()).thenReturn(programMethodOptimizationInfo);
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    nonEmptyStackReturnMarker.visitSimpleInstruction(
        clazz, method, codeAttribute, 2, new SimpleInstruction((byte) -84));

    // Assert
    verify(stackSizeComputer).isReachable(2);
    verify(programMethodOptimizationInfo).setReturnsWithNonEmptyStack();
    verify(method, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link NonEmptyStackReturnMarker#visitSimpleInstruction(Clazz, Method, CodeAttribute, int,
   * SimpleInstruction)}.
   *
   * <ul>
   *   <li>Then calls {@link StackSizeComputer#getStackSizeBefore(int)}.
   * </ul>
   *
   * <p>Method under test: {@link NonEmptyStackReturnMarker#visitSimpleInstruction(Clazz, Method,
   * CodeAttribute, int, SimpleInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction); then calls getStackSizeBefore(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NonEmptyStackReturnMarker.visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)"
  })
  void testVisitSimpleInstruction_thenCallsGetStackSizeBefore() {
    // Arrange
    StackSizeComputer stackSizeComputer = mock(StackSizeComputer.class);
    when(stackSizeComputer.getStackSizeBefore(anyInt())).thenReturn(3);
    when(stackSizeComputer.isReachable(anyInt())).thenReturn(true);
    NonEmptyStackReturnMarker nonEmptyStackReturnMarker =
        new NonEmptyStackReturnMarker(stackSizeComputer);
    LibraryClass clazz = new LibraryClass();

    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setReturnsWithNonEmptyStack();

    Method method = mock(Method.class);
    when(method.getProcessingInfo()).thenReturn(programMethodOptimizationInfo);
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    nonEmptyStackReturnMarker.visitSimpleInstruction(
        clazz, method, codeAttribute, 2, new SimpleInstruction((byte) -84));

    // Assert
    verify(stackSizeComputer).getStackSizeBefore(2);
    verify(stackSizeComputer).isReachable(2);
    verify(programMethodOptimizationInfo).setReturnsWithNonEmptyStack();
    verify(method, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link NonEmptyStackReturnMarker#visitSimpleInstruction(Clazz, Method, CodeAttribute, int,
   * SimpleInstruction)}.
   *
   * <ul>
   *   <li>When {@link SimpleInstruction#SimpleInstruction(byte)} with opcode is minus eighty-three.
   * </ul>
   *
   * <p>Method under test: {@link NonEmptyStackReturnMarker#visitSimpleInstruction(Clazz, Method,
   * CodeAttribute, int, SimpleInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction); when SimpleInstruction(byte) with opcode is minus eighty-three")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NonEmptyStackReturnMarker.visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)"
  })
  void testVisitSimpleInstruction_whenSimpleInstructionWithOpcodeIsMinusEightyThree() {
    // Arrange
    StackSizeComputer stackSizeComputer = mock(StackSizeComputer.class);
    when(stackSizeComputer.getStackSizeBefore(anyInt())).thenReturn(3);
    when(stackSizeComputer.isReachable(anyInt())).thenReturn(true);
    NonEmptyStackReturnMarker nonEmptyStackReturnMarker =
        new NonEmptyStackReturnMarker(stackSizeComputer);
    LibraryClass clazz = new LibraryClass();

    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setReturnsWithNonEmptyStack();

    Method method = mock(Method.class);
    when(method.getProcessingInfo()).thenReturn(programMethodOptimizationInfo);
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    nonEmptyStackReturnMarker.visitSimpleInstruction(
        clazz, method, codeAttribute, 2, new SimpleInstruction((byte) -83));

    // Assert
    verify(stackSizeComputer).getStackSizeBefore(2);
    verify(stackSizeComputer).isReachable(2);
    verify(programMethodOptimizationInfo).setReturnsWithNonEmptyStack();
    verify(method, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link NonEmptyStackReturnMarker#visitSimpleInstruction(Clazz, Method, CodeAttribute, int,
   * SimpleInstruction)}.
   *
   * <ul>
   *   <li>When {@link SimpleInstruction#SimpleInstruction(byte)} with opcode is minus seventy-nine.
   * </ul>
   *
   * <p>Method under test: {@link NonEmptyStackReturnMarker#visitSimpleInstruction(Clazz, Method,
   * CodeAttribute, int, SimpleInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction); when SimpleInstruction(byte) with opcode is minus seventy-nine")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NonEmptyStackReturnMarker.visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)"
  })
  void testVisitSimpleInstruction_whenSimpleInstructionWithOpcodeIsMinusSeventyNine() {
    // Arrange
    StackSizeComputer stackSizeComputer = mock(StackSizeComputer.class);
    when(stackSizeComputer.getStackSizeBefore(anyInt())).thenReturn(3);
    when(stackSizeComputer.isReachable(anyInt())).thenReturn(true);
    NonEmptyStackReturnMarker nonEmptyStackReturnMarker =
        new NonEmptyStackReturnMarker(stackSizeComputer);
    LibraryClass clazz = new LibraryClass();

    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setReturnsWithNonEmptyStack();

    Method method = mock(Method.class);
    when(method.getProcessingInfo()).thenReturn(programMethodOptimizationInfo);
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    nonEmptyStackReturnMarker.visitSimpleInstruction(
        clazz, method, codeAttribute, 2, new SimpleInstruction((byte) -79));

    // Assert
    verify(stackSizeComputer).getStackSizeBefore(2);
    verify(stackSizeComputer).isReachable(2);
    verify(programMethodOptimizationInfo).setReturnsWithNonEmptyStack();
    verify(method, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link NonEmptyStackReturnMarker#returnsWithNonEmptyStack(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link NonEmptyStackReturnMarker#returnsWithNonEmptyStack(Method)}
   */
  @Test
  @DisplayName(
      "Test returnsWithNonEmptyStack(Method); given MethodOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean NonEmptyStackReturnMarker.returnsWithNonEmptyStack(Method)"})
  void testReturnsWithNonEmptyStack_givenMethodOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertFalse(NonEmptyStackReturnMarker.returnsWithNonEmptyStack(method));
  }
}
