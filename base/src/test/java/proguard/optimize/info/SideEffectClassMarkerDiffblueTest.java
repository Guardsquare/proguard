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

class SideEffectClassMarkerDiffblueTest {
  /**
   * Test {@link SideEffectClassMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then {@link ProgramClass#ProgramClass()} ProcessingInfo {@link
   *       ProgramClassOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectClassMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); then ProgramClass() ProcessingInfo ProgramClassOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SideEffectClassMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenProgramClassProcessingInfoProgramClassOptimizationInfo() {
    // Arrange
    SideEffectClassMarker sideEffectClassMarker = new SideEffectClassMarker();
    ProgramClass programClass = new ProgramClass();
    programClass.processingInfo = new ProgramClassOptimizationInfo();

    // Act
    sideEffectClassMarker.visitProgramClass(programClass);

    // Assert
    Object processingInfo = programClass.getProcessingInfo();
    assertTrue(processingInfo instanceof ProgramClassOptimizationInfo);
    assertTrue(((ProgramClassOptimizationInfo) processingInfo).hasSideEffects());
  }

  /**
   * Test {@link SideEffectClassMarker#hasSideEffects(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectClassMarker#hasSideEffects(Clazz)}
   */
  @Test
  @DisplayName(
      "Test hasSideEffects(Clazz); given ClassOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean SideEffectClassMarker.hasSideEffects(Clazz)"})
  void testHasSideEffects_givenClassOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    clazz.processingInfo = new ClassOptimizationInfo();

    // Act and Assert
    assertTrue(SideEffectClassMarker.hasSideEffects(clazz));
  }

  /**
   * Test {@link SideEffectClassMarker#hasSideEffects(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectClassMarker#hasSideEffects(Clazz)}
   */
  @Test
  @DisplayName(
      "Test hasSideEffects(Clazz); given ProgramClassOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean SideEffectClassMarker.hasSideEffects(Clazz)"})
  void testHasSideEffects_givenProgramClassOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    clazz.processingInfo = new ProgramClassOptimizationInfo();

    // Act and Assert
    assertFalse(SideEffectClassMarker.hasSideEffects(clazz));
  }
}
