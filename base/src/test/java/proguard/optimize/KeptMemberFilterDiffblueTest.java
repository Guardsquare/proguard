package proguard.optimize;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.LibraryMethod;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.visitor.MethodImplementationFilter;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.obfuscate.MemberNameCleaner;
import proguard.optimize.info.FieldOptimizationInfo;
import proguard.optimize.info.MethodOptimizationInfo;

class KeptMemberFilterDiffblueTest {
  /**
   * Test {@link KeptMemberFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@link FieldOptimizationInfo} {@link FieldOptimizationInfo#isKept()} return {@code
   *       false}.
   * </ul>
   *
   * <p>Method under test: {@link KeptMemberFilter#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given FieldOptimizationInfo isKept() return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeptMemberFilter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_givenFieldOptimizationInfoIsKeptReturnFalse() {
    // Arrange
    KeptMemberFilter keptMemberFilter = new KeptMemberFilter(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    FieldOptimizationInfo fieldOptimizationInfo = mock(FieldOptimizationInfo.class);
    when(fieldOptimizationInfo.isKept()).thenReturn(false);

    ProgramField programField = new ProgramField();
    programField.setProcessingInfo(fieldOptimizationInfo);

    // Act
    keptMemberFilter.visitProgramField(programClass, programField);

    // Assert
    verify(fieldOptimizationInfo).isKept();
  }

  /**
   * Test {@link KeptMemberFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then calls {@link MethodImplementationFilter#visitProgramField(ProgramClass,
   *       ProgramField)}.
   * </ul>
   *
   * <p>Method under test: {@link KeptMemberFilter#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); then calls visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeptMemberFilter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_thenCallsVisitProgramField() {
    // Arrange
    MethodImplementationFilter memberVisitor = mock(MethodImplementationFilter.class);
    doNothing()
        .when(memberVisitor)
        .visitProgramField(Mockito.<ProgramClass>any(), Mockito.<ProgramField>any());
    KeptMemberFilter keptMemberFilter = new KeptMemberFilter(memberVisitor);
    ProgramClass programClass = new ProgramClass();

    FieldOptimizationInfo fieldOptimizationInfo = mock(FieldOptimizationInfo.class);
    when(fieldOptimizationInfo.isKept()).thenReturn(true);

    ProgramField programField = new ProgramField();
    programField.setProcessingInfo(fieldOptimizationInfo);

    // Act
    keptMemberFilter.visitProgramField(programClass, programField);

    // Assert
    verify(memberVisitor).visitProgramField(isA(ProgramClass.class), isA(ProgramField.class));
    verify(fieldOptimizationInfo).isKept();
  }

  /**
   * Test {@link KeptMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} {@link MethodOptimizationInfo#isKept()} return
   *       {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link KeptMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given MethodOptimizationInfo isKept() return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeptMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_givenMethodOptimizationInfoIsKeptReturnFalse() {
    // Arrange
    KeptMemberFilter keptMemberFilter = new KeptMemberFilter(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.isKept()).thenReturn(false);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    keptMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).isKept();
  }

  /**
   * Test {@link KeptMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link MethodImplementationFilter#visitProgramMethod(ProgramClass,
   *       ProgramMethod)}.
   * </ul>
   *
   * <p>Method under test: {@link KeptMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then calls visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeptMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenCallsVisitProgramMethod() {
    // Arrange
    MethodImplementationFilter memberVisitor = mock(MethodImplementationFilter.class);
    doNothing()
        .when(memberVisitor)
        .visitProgramMethod(Mockito.<ProgramClass>any(), Mockito.<ProgramMethod>any());
    KeptMemberFilter keptMemberFilter = new KeptMemberFilter(memberVisitor);
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.isKept()).thenReturn(true);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    keptMemberFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(memberVisitor).visitProgramMethod(isA(ProgramClass.class), isA(ProgramMethod.class));
    verify(methodOptimizationInfo).isKept();
  }

  /**
   * Test {@link KeptMemberFilter#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <p>Method under test: {@link KeptMemberFilter#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName("Test visitLibraryField(LibraryClass, LibraryField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeptMemberFilter.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField() {
    // Arrange
    KeptMemberFilter keptMemberFilter = new KeptMemberFilter(new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();

    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    FieldOptimizationInfo fieldOptimizationInfo = new FieldOptimizationInfo();
    libraryField.setProcessingInfo(fieldOptimizationInfo);

    // Act
    keptMemberFilter.visitLibraryField(libraryClass, libraryField);

    // Assert that nothing has changed
    Object processingInfo = libraryField.getProcessingInfo();
    assertTrue(processingInfo instanceof FieldOptimizationInfo);
    assertSame(fieldOptimizationInfo, processingInfo);
  }

  /**
   * Test {@link KeptMemberFilter#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <p>Method under test: {@link KeptMemberFilter#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName("Test visitLibraryField(LibraryClass, LibraryField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeptMemberFilter.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField2() {
    // Arrange
    KeptMemberFilter keptMemberFilter = new KeptMemberFilter(new MemberNameCleaner());
    LibraryClass libraryClass = new LibraryClass();

    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    libraryField.setProcessingInfo(new FieldOptimizationInfo());

    // Act
    keptMemberFilter.visitLibraryField(libraryClass, libraryField);

    // Assert
    assertNull(libraryField.getProcessingInfo());
  }

  /**
   * Test {@link KeptMemberFilter#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <ul>
   *   <li>When {@link LibraryField#LibraryField()}.
   *   <li>Then {@link LibraryField#LibraryField()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link KeptMemberFilter#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryField(LibraryClass, LibraryField); when LibraryField(); then LibraryField() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeptMemberFilter.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField_whenLibraryField_thenLibraryFieldProcessingInfoIsNull() {
    // Arrange
    KeptMemberFilter keptMemberFilter = new KeptMemberFilter(new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();
    LibraryField libraryField = new LibraryField();

    // Act
    keptMemberFilter.visitLibraryField(libraryClass, libraryField);

    // Assert that nothing has changed
    assertNull(libraryField.getProcessingInfo());
  }

  /**
   * Test {@link KeptMemberFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} {@link MethodOptimizationInfo#isKept()} return
   *       {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link KeptMemberFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); given MethodOptimizationInfo isKept() return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeptMemberFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_givenMethodOptimizationInfoIsKeptReturnFalse() {
    // Arrange
    KeptMemberFilter keptMemberFilter = new KeptMemberFilter(new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.isKept()).thenReturn(false);

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    keptMemberFilter.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    verify(methodOptimizationInfo).isKept();
  }

  /**
   * Test {@link KeptMemberFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link MethodImplementationFilter#visitLibraryMethod(LibraryClass,
   *       LibraryMethod)}.
   * </ul>
   *
   * <p>Method under test: {@link KeptMemberFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); then calls visitLibraryMethod(LibraryClass, LibraryMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeptMemberFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_thenCallsVisitLibraryMethod() {
    // Arrange
    MethodImplementationFilter memberVisitor = mock(MethodImplementationFilter.class);
    doNothing()
        .when(memberVisitor)
        .visitLibraryMethod(Mockito.<LibraryClass>any(), Mockito.<LibraryMethod>any());
    KeptMemberFilter keptMemberFilter = new KeptMemberFilter(memberVisitor);
    LibraryClass libraryClass = new LibraryClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.isKept()).thenReturn(true);

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    keptMemberFilter.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    verify(memberVisitor).visitLibraryMethod(isA(LibraryClass.class), isA(LibraryMethod.class));
    verify(methodOptimizationInfo).isKept();
  }
}
