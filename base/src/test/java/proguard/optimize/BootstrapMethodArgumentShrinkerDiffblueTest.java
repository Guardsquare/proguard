package proguard.optimize;

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
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.BootstrapMethodInfo;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.MethodHandleConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.optimize.info.MethodOptimizationInfo;

class BootstrapMethodArgumentShrinkerDiffblueTest {
  /**
   * Test {@link BootstrapMethodArgumentShrinker#visitBootstrapMethodInfo(Clazz,
   * BootstrapMethodInfo)}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryClass#constantPoolEntryAccept(int, ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link BootstrapMethodArgumentShrinker#visitBootstrapMethodInfo(Clazz,
   * BootstrapMethodInfo)}
   */
  @Test
  @DisplayName(
      "Test visitBootstrapMethodInfo(Clazz, BootstrapMethodInfo); then calls constantPoolEntryAccept(int, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void BootstrapMethodArgumentShrinker.visitBootstrapMethodInfo(Clazz, BootstrapMethodInfo)"
  })
  void testVisitBootstrapMethodInfo_thenCallsConstantPoolEntryAccept() {
    // Arrange
    BootstrapMethodArgumentShrinker bootstrapMethodArgumentShrinker =
        new BootstrapMethodArgumentShrinker();

    LibraryClass clazz = mock(LibraryClass.class);
    doNothing().when(clazz).constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());

    // Act
    bootstrapMethodArgumentShrinker.visitBootstrapMethodInfo(clazz, new BootstrapMethodInfo());

    // Assert
    verify(clazz).constantPoolEntryAccept(eq(0), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link BootstrapMethodArgumentShrinker#visitMethodHandleConstant(Clazz,
   * MethodHandleConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryClass#constantPoolEntryAccept(int, ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link BootstrapMethodArgumentShrinker#visitMethodHandleConstant(Clazz,
   * MethodHandleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodHandleConstant(Clazz, MethodHandleConstant); then calls constantPoolEntryAccept(int, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void BootstrapMethodArgumentShrinker.visitMethodHandleConstant(Clazz, MethodHandleConstant)"
  })
  void testVisitMethodHandleConstant_thenCallsConstantPoolEntryAccept() {
    // Arrange
    BootstrapMethodArgumentShrinker bootstrapMethodArgumentShrinker =
        new BootstrapMethodArgumentShrinker();

    LibraryClass clazz = mock(LibraryClass.class);
    doNothing().when(clazz).constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());

    // Act
    bootstrapMethodArgumentShrinker.visitMethodHandleConstant(clazz, new MethodHandleConstant());

    // Assert
    verify(clazz).constantPoolEntryAccept(eq(0), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link BootstrapMethodArgumentShrinker#visitMethodHandleConstant(Clazz,
   * MethodHandleConstant)}.
   *
   * <ul>
   *   <li>When {@link ClassConstant} {@link ClassConstant#accept(Clazz, ConstantVisitor)} does
   *       nothing.
   *   <li>Then calls {@link ClassConstant#accept(Clazz, ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link BootstrapMethodArgumentShrinker#visitMethodHandleConstant(Clazz,
   * MethodHandleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodHandleConstant(Clazz, MethodHandleConstant); when ClassConstant accept(Clazz, ConstantVisitor) does nothing; then calls accept(Clazz, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void BootstrapMethodArgumentShrinker.visitMethodHandleConstant(Clazz, MethodHandleConstant)"
  })
  void testVisitMethodHandleConstant_whenClassConstantAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    BootstrapMethodArgumentShrinker bootstrapMethodArgumentShrinker =
        new BootstrapMethodArgumentShrinker();

    ClassConstant classConstant = mock(ClassConstant.class);
    doNothing().when(classConstant).accept(Mockito.<Clazz>any(), Mockito.<ConstantVisitor>any());
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    bootstrapMethodArgumentShrinker.visitMethodHandleConstant(clazz, new MethodHandleConstant());

    // Assert
    verify(classConstant).accept(isA(Clazz.class), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link BootstrapMethodArgumentShrinker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link MethodOptimizationInfo#getUsedParameters()}.
   * </ul>
   *
   * <p>Method under test: {@link BootstrapMethodArgumentShrinker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then calls getUsedParameters()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void BootstrapMethodArgumentShrinker.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_thenCallsGetUsedParameters() {
    // Arrange
    BootstrapMethodArgumentShrinker bootstrapMethodArgumentShrinker =
        new BootstrapMethodArgumentShrinker();
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.getUsedParameters()).thenReturn(1L);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    bootstrapMethodArgumentShrinker.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).getUsedParameters();
  }
}
