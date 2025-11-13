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

class NoEscapingParametersMethodMarkerDiffblueTest {
  /**
   * Test {@link NoEscapingParametersMethodMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then {@link ProgramMethod#ProgramMethod()} ProcessingInfo {@link MethodOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link NoEscapingParametersMethodMarker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then ProgramMethod() ProcessingInfo MethodOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NoEscapingParametersMethodMarker.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_thenProgramMethodProcessingInfoMethodOptimizationInfo() {
    // Arrange
    NoEscapingParametersMethodMarker noEscapingParametersMethodMarker =
        new NoEscapingParametersMethodMarker();
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(new MethodOptimizationInfo());

    // Act
    noEscapingParametersMethodMarker.visitProgramMethod(programClass, programMethod);

    // Assert
    Object processingInfo = programMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertEquals(0L, ((MethodOptimizationInfo) processingInfo).getEscapingParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasNoEscapingParameters());
  }

  /**
   * Test {@link NoEscapingParametersMethodMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Then {@link LibraryMethod#LibraryMethod()} ProcessingInfo {@link MethodOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link NoEscapingParametersMethodMarker#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); then LibraryMethod() ProcessingInfo MethodOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NoEscapingParametersMethodMarker.visitLibraryMethod(LibraryClass, LibraryMethod)"
  })
  void testVisitLibraryMethod_thenLibraryMethodProcessingInfoMethodOptimizationInfo() {
    // Arrange
    NoEscapingParametersMethodMarker noEscapingParametersMethodMarker =
        new NoEscapingParametersMethodMarker();
    LibraryClass libraryClass = new LibraryClass();

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo(new MethodOptimizationInfo());

    // Act
    noEscapingParametersMethodMarker.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    Object processingInfo = libraryMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertEquals(0L, ((MethodOptimizationInfo) processingInfo).getEscapingParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasNoEscapingParameters());
  }

  /**
   * Test {@link NoEscapingParametersMethodMarker#hasNoParameterEscaping(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link NoEscapingParametersMethodMarker#hasNoParameterEscaping(Method)}
   */
  @Test
  @DisplayName(
      "Test hasNoParameterEscaping(Method); given MethodOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean NoEscapingParametersMethodMarker.hasNoParameterEscaping(Method)"})
  void testHasNoParameterEscaping_givenMethodOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertFalse(NoEscapingParametersMethodMarker.hasNoParameterEscaping(method));
  }
}
