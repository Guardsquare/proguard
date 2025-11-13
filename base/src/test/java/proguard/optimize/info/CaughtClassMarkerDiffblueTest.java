package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;

class CaughtClassMarkerDiffblueTest {
  /**
   * Test {@link CaughtClassMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then {@link ProgramClass#ProgramClass()} ProcessingInfo {@link
   *       ProgramClassOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link CaughtClassMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); then ProgramClass() ProcessingInfo ProgramClassOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void CaughtClassMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenProgramClassProcessingInfoProgramClassOptimizationInfo() {
    // Arrange
    CaughtClassMarker caughtClassMarker = new CaughtClassMarker();
    ProgramClass programClass = new ProgramClass();
    programClass.processingInfo = new ProgramClassOptimizationInfo();

    // Act
    caughtClassMarker.visitProgramClass(programClass);

    // Assert
    Object processingInfo = programClass.getProcessingInfo();
    assertTrue(processingInfo instanceof ProgramClassOptimizationInfo);
    assertTrue(((ProgramClassOptimizationInfo) processingInfo).isCaught());
  }

  /**
   * Test {@link CaughtClassMarker#isCaught(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link CaughtClassMarker#isCaught(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isCaught(Clazz); given ClassOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean CaughtClassMarker.isCaught(Clazz)"})
  void testIsCaught_givenClassOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ClassOptimizationInfo());

    // Act and Assert
    assertTrue(CaughtClassMarker.isCaught(clazz));
  }

  /**
   * Test {@link CaughtClassMarker#isCaught(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link CaughtClassMarker#isCaught(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isCaught(Clazz); given ProgramClassOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean CaughtClassMarker.isCaught(Clazz)"})
  void testIsCaught_givenProgramClassOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act and Assert
    assertFalse(CaughtClassMarker.isCaught(clazz));
  }
}
