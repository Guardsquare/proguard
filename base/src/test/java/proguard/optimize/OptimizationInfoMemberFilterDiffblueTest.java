package proguard.optimize;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
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
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.visitor.MemberVisitor;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.obfuscate.ClassRenamer;
import proguard.obfuscate.MemberNameCleaner;
import proguard.obfuscate.kotlin.KotlinValueParameterUsageMarker;
import proguard.optimize.info.MethodOptimizationInfo;
import proguard.optimize.info.ProgramFieldOptimizationInfo;
import proguard.testutils.cpa.NamedMember;

class OptimizationInfoMemberFilterDiffblueTest {
  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField() {
    // Arrange
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(memberVisitor, new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    // Act and Assert
    assertDoesNotThrow(
        () -> optimizationInfoMemberFilter.visitProgramField(programClass, new ProgramField()));
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField2() {
    // Arrange
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(new KotlinAnnotationCounter(), null);
    ProgramClass programClass = new ProgramClass();

    // Act and Assert
    assertDoesNotThrow(
        () -> optimizationInfoMemberFilter.visitProgramField(programClass, new ProgramField()));
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField3() {
    // Arrange
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(memberVisitor, new MemberNameCleaner());
    ProgramClass programClass = new ProgramClass();

    // Act and Assert
    assertDoesNotThrow(
        () -> optimizationInfoMemberFilter.visitProgramField(programClass, new ProgramField()));
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramField#setProcessingInfo(Object)}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); then calls setProcessingInfo(Object)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField_thenCallsSetProcessingInfo() {
    // Arrange
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(new MemberNameCleaner(), mock(MemberVisitor.class));
    ProgramClass programClass = new ProgramClass();

    ProgramField programField = mock(ProgramField.class);
    doNothing().when(programField).setProcessingInfo(Mockito.<Object>any());
    when(programField.getProcessingInfo()).thenReturn(mock(ProgramFieldOptimizationInfo.class));

    // Act
    optimizationInfoMemberFilter.visitProgramField(programClass, programField);

    // Assert
    verify(programField, atLeast(1)).getProcessingInfo();
    verify(programField).setProcessingInfo(isNull());
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then calls {@link MemberVisitor#visitProgramField(ProgramClass, ProgramField)}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); then calls visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField_thenCallsVisitProgramField() {
    // Arrange
    MemberVisitor otherMemberVisitor = mock(MemberVisitor.class);
    doNothing()
        .when(otherMemberVisitor)
        .visitProgramField(Mockito.<ProgramClass>any(), Mockito.<ProgramField>any());
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(new KotlinAnnotationCounter(), otherMemberVisitor);
    ProgramClass programClass = new ProgramClass();

    // Act
    optimizationInfoMemberFilter.visitProgramField(programClass, new ProgramField());

    // Assert
    verify(otherMemberVisitor).visitProgramField(isA(ProgramClass.class), isA(ProgramField.class));
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField_thenThrowUnsupportedOperationException() {
    // Arrange
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(
            new BootstrapMethodArgumentShrinker(), mock(MemberVisitor.class));
    ProgramClass programClass = new ProgramClass();

    ProgramField programField = mock(ProgramField.class);
    when(programField.getProcessingInfo()).thenReturn(mock(ProgramFieldOptimizationInfo.class));

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> optimizationInfoMemberFilter.visitProgramField(programClass, programField));
    verify(programField).getProcessingInfo();
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>When {@link ProgramField} {@link ProgramField#accept(Clazz, MemberVisitor)} does nothing.
   *   <li>Then calls {@link ProgramField#accept(Clazz, MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); when ProgramField accept(Clazz, MemberVisitor) does nothing; then calls accept(Clazz, MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField_whenProgramFieldAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(new KotlinAnnotationCounter(), mock(MemberVisitor.class));
    ProgramClass programClass = new ProgramClass();

    ProgramField programField = mock(ProgramField.class);
    doNothing().when(programField).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
    when(programField.getProcessingInfo()).thenReturn(mock(ProgramFieldOptimizationInfo.class));

    // Act
    optimizationInfoMemberFilter.visitProgramField(programClass, programField);

    // Assert
    verify(programField).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(programField).getProcessingInfo();
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod() {
    // Arrange
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(memberVisitor, new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();
    ProgramMethod programMethod = new ProgramMethod();

    // Act
    optimizationInfoMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert that nothing has changed
    assertNull(programMethod.getProcessingInfo());
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod2() {
    // Arrange
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(new KotlinAnnotationCounter(), null);
    ProgramClass programClass = new ProgramClass();
    ProgramMethod programMethod = new ProgramMethod();

    // Act
    optimizationInfoMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert that nothing has changed
    assertNull(programMethod.getProcessingInfo());
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod3() {
    // Arrange
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(memberVisitor, new MemberNameCleaner());
    ProgramClass programClass = new ProgramClass();
    ProgramMethod programMethod = new ProgramMethod();

    // Act
    optimizationInfoMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert that nothing has changed
    assertNull(programMethod.getProcessingInfo());
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod4() {
    // Arrange
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(memberVisitor, new MemberNameCleaner());
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(new MethodOptimizationInfo());

    // Act
    optimizationInfoMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    assertNull(programMethod.getProcessingInfo());
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod5() {
    // Arrange
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(memberVisitor, new KotlinValueParameterUsageMarker());
    ProgramClass programClass = new ProgramClass();
    ProgramMethod programMethod = new ProgramMethod();

    // Act
    optimizationInfoMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert that nothing has changed
    assertNull(programMethod.getProcessingInfo());
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod6() {
    // Arrange
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(memberVisitor, new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    NamedMember programMethod = new NamedMember("Member Name", "Descriptor");
    programMethod.addProcessingFlags(2, 1, 2, 1);

    // Act
    optimizationInfoMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert that nothing has changed
    assertNull(programMethod.getProcessingInfo());
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod7() {
    // Arrange
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(memberVisitor, new ClassRenamer());
    ProgramClass programClass = new ProgramClass();

    NamedMember programMethod = new NamedMember("Member Name", "Descriptor");
    programMethod.addProcessingFlags(2, 1, 2, 1);

    // Act
    optimizationInfoMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert that nothing has changed
    assertNull(programMethod.getProcessingInfo());
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then {@link ProgramMethod#ProgramMethod()} ProcessingInfo {@link MethodOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then ProgramMethod() ProcessingInfo MethodOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_thenProgramMethodProcessingInfoMethodOptimizationInfo() {
    // Arrange
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(memberVisitor, new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = new ProgramMethod();
    MethodOptimizationInfo methodOptimizationInfo = new MethodOptimizationInfo();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    optimizationInfoMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert that nothing has changed
    Object processingInfo = programMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertSame(methodOptimizationInfo, processingInfo);
  }

  /**
   * Test {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then {@link ProgramMethod#ProgramMethod()} ProcessingInfo {@link MethodOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizationInfoMemberFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then ProgramMethod() ProcessingInfo MethodOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationInfoMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_thenProgramMethodProcessingInfoMethodOptimizationInfo2() {
    // Arrange
    KotlinAnnotationCounter memberVisitor = new KotlinAnnotationCounter();
    OptimizationInfoMemberFilter optimizationInfoMemberFilter =
        new OptimizationInfoMemberFilter(memberVisitor, new BootstrapMethodArgumentShrinker());
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = new ProgramMethod();
    MethodOptimizationInfo methodOptimizationInfo = new MethodOptimizationInfo();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    optimizationInfoMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert that nothing has changed
    Object processingInfo = programMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertSame(methodOptimizationInfo, processingInfo);
  }
}
