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

class NoSideEffectMethodMarkerDiffblueTest {
  /**
   * Test {@link NoSideEffectMethodMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then {@link ProgramMethod#ProgramMethod()} ProcessingInfo {@link MethodOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link NoSideEffectMethodMarker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then ProgramMethod() ProcessingInfo MethodOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NoSideEffectMethodMarker.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_thenProgramMethodProcessingInfoMethodOptimizationInfo() {
    // Arrange
    NoSideEffectMethodMarker noSideEffectMethodMarker = new NoSideEffectMethodMarker();
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(new MethodOptimizationInfo());

    // Act
    noSideEffectMethodMarker.visitProgramMethod(programClass, programMethod);

    // Assert
    Object processingInfo = programMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertEquals(0L, ((MethodOptimizationInfo) processingInfo).getEscapingParameters());
    assertEquals(0L, ((MethodOptimizationInfo) processingInfo).getModifiedParameters());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasSideEffects());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasNoEscapingParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasNoExternalSideEffects());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasNoSideEffects());
  }

  /**
   * Test {@link NoSideEffectMethodMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Then {@link LibraryMethod#LibraryMethod()} ProcessingInfo {@link MethodOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link NoSideEffectMethodMarker#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); then LibraryMethod() ProcessingInfo MethodOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NoSideEffectMethodMarker.visitLibraryMethod(LibraryClass, LibraryMethod)"
  })
  void testVisitLibraryMethod_thenLibraryMethodProcessingInfoMethodOptimizationInfo() {
    // Arrange
    NoSideEffectMethodMarker noSideEffectMethodMarker = new NoSideEffectMethodMarker();
    LibraryClass libraryClass = new LibraryClass();

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo(new MethodOptimizationInfo());

    // Act
    noSideEffectMethodMarker.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    Object processingInfo = libraryMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertEquals(0L, ((MethodOptimizationInfo) processingInfo).getEscapingParameters());
    assertEquals(0L, ((MethodOptimizationInfo) processingInfo).getModifiedParameters());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasSideEffects());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasNoEscapingParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasNoExternalSideEffects());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasNoSideEffects());
  }

  /**
   * Test {@link NoSideEffectMethodMarker#hasNoSideEffects(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link NoSideEffectMethodMarker#hasNoSideEffects(Method)}
   */
  @Test
  @DisplayName(
      "Test hasNoSideEffects(Method); given MethodOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean NoSideEffectMethodMarker.hasNoSideEffects(Method)"})
  void testHasNoSideEffects_givenMethodOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertFalse(NoSideEffectMethodMarker.hasNoSideEffects(method));
  }
}
