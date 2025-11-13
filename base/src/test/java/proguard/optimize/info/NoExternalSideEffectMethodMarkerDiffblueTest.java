package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;

class NoExternalSideEffectMethodMarkerDiffblueTest {
  /**
   * Test {@link NoExternalSideEffectMethodMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then {@link ProgramMethod#ProgramMethod()} ProcessingInfo {@link MethodOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link NoExternalSideEffectMethodMarker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then ProgramMethod() ProcessingInfo MethodOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NoExternalSideEffectMethodMarker.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_thenProgramMethodProcessingInfoMethodOptimizationInfo() {
    // Arrange
    NoExternalSideEffectMethodMarker noExternalSideEffectMethodMarker =
        new NoExternalSideEffectMethodMarker();
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(new MethodOptimizationInfo());

    // Act
    noExternalSideEffectMethodMarker.visitProgramMethod(programClass, programMethod);

    // Assert
    Object processingInfo = programMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertEquals(0L, ((MethodOptimizationInfo) processingInfo).getEscapingParameters());
    assertEquals(1L, ((MethodOptimizationInfo) processingInfo).getModifiedParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasNoEscapingParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasNoExternalSideEffects());
  }

  /**
   * Test {@link NoExternalSideEffectMethodMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Then {@link LibraryMethod#LibraryMethod()} ProcessingInfo {@link MethodOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link NoExternalSideEffectMethodMarker#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); then LibraryMethod() ProcessingInfo MethodOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NoExternalSideEffectMethodMarker.visitLibraryMethod(LibraryClass, LibraryMethod)"
  })
  void testVisitLibraryMethod_thenLibraryMethodProcessingInfoMethodOptimizationInfo() {
    // Arrange
    NoExternalSideEffectMethodMarker noExternalSideEffectMethodMarker =
        new NoExternalSideEffectMethodMarker();
    LibraryClass libraryClass = new LibraryClass();

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo(new MethodOptimizationInfo());

    // Act
    noExternalSideEffectMethodMarker.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    Object processingInfo = libraryMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertEquals(0L, ((MethodOptimizationInfo) processingInfo).getEscapingParameters());
    assertEquals(1L, ((MethodOptimizationInfo) processingInfo).getModifiedParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasNoEscapingParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasNoExternalSideEffects());
  }

  /**
   * Test {@link NoExternalSideEffectMethodMarker#hasNoExternalSideEffects(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link NoExternalSideEffectMethodMarker#hasNoExternalSideEffects(Method)}
   */
  @Test
  @DisplayName(
      "Test hasNoExternalSideEffects(Method); given MethodOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(Method)"})
  void testHasNoExternalSideEffects_givenMethodOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertFalse(NoExternalSideEffectMethodMarker.hasNoExternalSideEffects(method));
  }
}
