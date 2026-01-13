package proguard.optimize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.evaluation.value.ArrayReferenceValue;
import proguard.evaluation.value.TopValue;
import proguard.evaluation.value.UnknownIntegerValue;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.obfuscate.MemberNameCleaner;
import proguard.optimize.info.MethodOptimizationInfo;
import proguard.testutils.cpa.NamedMember;

class ConstantMemberFilterDiffblueTest {
  /**
   * Test {@link ConstantMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link ConstantMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConstantMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod() {
    // Arrange
    ConstantMemberFilter constantMemberFilter =
        new ConstantMemberFilter(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.getReturnValue()).thenReturn(new TopValue());

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    constantMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).getReturnValue();
  }

  /**
   * Test {@link ConstantMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link ConstantMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConstantMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod2() {
    // Arrange
    ConstantMemberFilter constantMemberFilter =
        new ConstantMemberFilter(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    LibraryClass referencedClass = new LibraryClass();
    ArrayReferenceValue arrayReferenceValue =
        new ArrayReferenceValue("Type", referencedClass, true, new UnknownIntegerValue());
    when(methodOptimizationInfo.getReturnValue()).thenReturn(arrayReferenceValue);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    constantMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).getReturnValue();
  }

  /**
   * Test {@link ConstantMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link ConstantMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConstantMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod3() {
    // Arrange
    ConstantMemberFilter constantMemberFilter =
        new ConstantMemberFilter(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.getReturnValue()).thenReturn(new TopValue());

    NamedMember programMethod = new NamedMember("Member Name", "Descriptor");
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    constantMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).getReturnValue();
  }

  /**
   * Test {@link ConstantMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link MethodOptimizationInfo#getUsedParameters()}.
   * </ul>
   *
   * <p>Method under test: {@link ConstantMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then calls getUsedParameters()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConstantMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenCallsGetUsedParameters() {
    // Arrange
    ConstantMemberFilter constantMemberFilter =
        new ConstantMemberFilter(new BootstrapMethodArgumentShrinker());
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.getUsedParameters()).thenReturn(1L);
    when(methodOptimizationInfo.getReturnValue()).thenReturn(new TopValue());

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    constantMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).getReturnValue();
    verify(methodOptimizationInfo).getUsedParameters();
  }

  /**
   * Test {@link ConstantMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then {@link ProgramMethod#ProgramMethod()} ProcessingInfo is {@link
   *       MethodOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ConstantMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then ProgramMethod() ProcessingInfo is MethodOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConstantMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenProgramMethodProcessingInfoIsMethodOptimizationInfo() {
    // Arrange
    ConstantMemberFilter constantMemberFilter =
        new ConstantMemberFilter(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = new ProgramMethod();
    MethodOptimizationInfo methodOptimizationInfo = new MethodOptimizationInfo();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    constantMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert that nothing has changed
    assertSame(methodOptimizationInfo, programMethod.getProcessingInfo());
  }

  /**
   * Test {@link ConstantMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then {@link ProgramMethod#ProgramMethod()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ConstantMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then ProgramMethod() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConstantMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenProgramMethodProcessingInfoIsNull() {
    // Arrange
    ConstantMemberFilter constantMemberFilter = new ConstantMemberFilter(new MemberNameCleaner());
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.getReturnValue()).thenReturn(new TopValue());

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    constantMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).getReturnValue();
    assertNull(programMethod.getProcessingInfo());
  }

  /**
   * Test {@link ConstantMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then {@link ProgramMethod#ProgramMethod()} ProcessingInfo {@link MethodOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link ConstantMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then ProgramMethod() ProcessingInfo MethodOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ConstantMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenProgramMethodProcessingInfoMethodOptimizationInfo() {
    // Arrange
    ConstantMemberFilter constantMemberFilter = new ConstantMemberFilter(new KeepMarker());
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.getReturnValue()).thenReturn(new TopValue());

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    constantMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).getReturnValue();
    Object processingInfo = programMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertNull(((MethodOptimizationInfo) processingInfo).getReturnValue());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getEscapedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getEscapingParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getModifiedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getReturnedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getUsedParameters());
    assertEquals(0, ((MethodOptimizationInfo) processingInfo).getParameterSize());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoEscapingParameters());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoExternalReturnValues());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoExternalSideEffects());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoSideEffects());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasUnusedParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasSideEffects());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasSynchronizedBlock());
    assertTrue(((MethodOptimizationInfo) processingInfo).isKept());
    assertEquals(Integer.MAX_VALUE, ((MethodOptimizationInfo) processingInfo).getInvocationCount());
  }
}
