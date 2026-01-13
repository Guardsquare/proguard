package proguard.optimize.info;

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
import proguard.classfile.LibraryMethod;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.visitor.MethodImplementationFilter;
import proguard.fixer.kotlin.KotlinAnnotationCounter;

class SideEffectMethodFilterDiffblueTest {
  /**
   * Test {@link SideEffectMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} {@link MethodOptimizationInfo#hasSideEffects()}
   *       return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given MethodOptimizationInfo hasSideEffects() return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SideEffectMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_givenMethodOptimizationInfoHasSideEffectsReturnFalse() {
    // Arrange
    SideEffectMethodFilter sideEffectMethodFilter =
        new SideEffectMethodFilter(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasSideEffects()).thenReturn(false);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    sideEffectMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).hasSideEffects();
  }

  /**
   * Test {@link SideEffectMethodFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link MethodImplementationFilter#visitProgramMethod(ProgramClass,
   *       ProgramMethod)}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectMethodFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then calls visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SideEffectMethodFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenCallsVisitProgramMethod() {
    // Arrange
    MethodImplementationFilter memberVisitor = mock(MethodImplementationFilter.class);
    doNothing()
        .when(memberVisitor)
        .visitProgramMethod(Mockito.<ProgramClass>any(), Mockito.<ProgramMethod>any());
    SideEffectMethodFilter sideEffectMethodFilter = new SideEffectMethodFilter(memberVisitor);
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasSideEffects()).thenReturn(true);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    sideEffectMethodFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(memberVisitor).visitProgramMethod(isA(ProgramClass.class), isA(ProgramMethod.class));
    verify(methodOptimizationInfo).hasSideEffects();
  }

  /**
   * Test {@link SideEffectMethodFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} {@link MethodOptimizationInfo#hasSideEffects()}
   *       return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectMethodFilter#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); given MethodOptimizationInfo hasSideEffects() return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SideEffectMethodFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_givenMethodOptimizationInfoHasSideEffectsReturnFalse() {
    // Arrange
    SideEffectMethodFilter sideEffectMethodFilter =
        new SideEffectMethodFilter(new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasSideEffects()).thenReturn(false);

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    sideEffectMethodFilter.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    verify(methodOptimizationInfo).hasSideEffects();
  }

  /**
   * Test {@link SideEffectMethodFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link MethodImplementationFilter#visitLibraryMethod(LibraryClass,
   *       LibraryMethod)}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectMethodFilter#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); then calls visitLibraryMethod(LibraryClass, LibraryMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SideEffectMethodFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_thenCallsVisitLibraryMethod() {
    // Arrange
    MethodImplementationFilter memberVisitor = mock(MethodImplementationFilter.class);
    doNothing()
        .when(memberVisitor)
        .visitLibraryMethod(Mockito.<LibraryClass>any(), Mockito.<LibraryMethod>any());
    SideEffectMethodFilter sideEffectMethodFilter = new SideEffectMethodFilter(memberVisitor);
    LibraryClass libraryClass = new LibraryClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasSideEffects()).thenReturn(true);

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    sideEffectMethodFilter.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    verify(memberVisitor).visitLibraryMethod(isA(LibraryClass.class), isA(LibraryMethod.class));
    verify(methodOptimizationInfo).hasSideEffects();
  }
}
