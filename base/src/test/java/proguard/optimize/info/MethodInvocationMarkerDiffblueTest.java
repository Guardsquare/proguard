package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.visitor.MemberVisitor;

class MethodInvocationMarkerDiffblueTest {
  /**
   * Test {@link MethodInvocationMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link StringConstant#referencedMemberAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInvocationMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); then calls referencedMemberAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodInvocationMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenCallsReferencedMemberAccept() {
    // Arrange
    MethodInvocationMarker methodInvocationMarker = new MethodInvocationMarker();
    LibraryClass clazz = new LibraryClass();

    StringConstant stringConstant = mock(StringConstant.class);
    doNothing().when(stringConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());

    // Act
    methodInvocationMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(stringConstant).referencedMemberAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link MethodInvocationMarker#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link InterfaceMethodrefConstant#referencedMethodAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInvocationMarker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); then calls referencedMethodAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MethodInvocationMarker.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_thenCallsReferencedMethodAccept() {
    // Arrange
    MethodInvocationMarker methodInvocationMarker = new MethodInvocationMarker();
    LibraryClass clazz = new LibraryClass();

    InterfaceMethodrefConstant anyMethodrefConstant = mock(InterfaceMethodrefConstant.class);
    doNothing().when(anyMethodrefConstant).referencedMethodAccept(Mockito.<MemberVisitor>any());

    // Act
    methodInvocationMarker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

    // Assert
    verify(anyMethodrefConstant).referencedMethodAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link MethodInvocationMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethodOptimizationInfo#incrementInvocationCount()}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInvocationMarker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then calls incrementInvocationCount()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodInvocationMarker.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenCallsIncrementInvocationCount() {
    // Arrange
    MethodInvocationMarker methodInvocationMarker = new MethodInvocationMarker();
    ProgramClass programClass = new ProgramClass();

    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).incrementInvocationCount();

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(programMethodOptimizationInfo);

    // Act
    methodInvocationMarker.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programMethodOptimizationInfo).incrementInvocationCount();
  }

  /**
   * Test {@link MethodInvocationMarker#getInvocationCount(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@link Integer#MAX_VALUE}.
   * </ul>
   *
   * <p>Method under test: {@link MethodInvocationMarker#getInvocationCount(Method)}
   */
  @Test
  @DisplayName(
      "Test getInvocationCount(Method); given MethodOptimizationInfo (default constructor); then return MAX_VALUE")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"int MethodInvocationMarker.getInvocationCount(Method)"})
  void testGetInvocationCount_givenMethodOptimizationInfo_thenReturnMax_value() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertEquals(Integer.MAX_VALUE, MethodInvocationMarker.getInvocationCount(method));
  }
}
