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
import proguard.classfile.constant.ClassConstant;

class InstantiationClassMarkerDiffblueTest {
  /**
   * Test {@link InstantiationClassMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link InstantiationClassMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InstantiationClassMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant() {
    // Arrange
    InstantiationClassMarker instantiationClassMarker = new InstantiationClassMarker();
    LibraryClass clazz = new LibraryClass();

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(new ProgramClassOptimizationInfo());
    ClassConstant classConstant = new ClassConstant(1, referencedClass);

    // Act
    instantiationClassMarker.visitClassConstant(clazz, classConstant);

    // Assert
    Clazz clazz2 = classConstant.referencedClass;
    assertTrue(clazz2 instanceof LibraryClass);
    Object processingInfo = clazz2.getProcessingInfo();
    assertTrue(processingInfo instanceof ProgramClassOptimizationInfo);
    assertTrue(((ProgramClassOptimizationInfo) processingInfo).isInstantiated());
  }

  /**
   * Test {@link InstantiationClassMarker#visitAnyClass(Clazz)}.
   *
   * <p>Method under test: {@link InstantiationClassMarker#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InstantiationClassMarker.visitAnyClass(Clazz)"})
  void testVisitAnyClass() {
    // Arrange
    InstantiationClassMarker instantiationClassMarker = new InstantiationClassMarker();

    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act
    instantiationClassMarker.visitAnyClass(clazz);

    // Assert
    Object processingInfo = clazz.getProcessingInfo();
    assertTrue(processingInfo instanceof ProgramClassOptimizationInfo);
    assertTrue(((ProgramClassOptimizationInfo) processingInfo).isInstantiated());
  }

  /**
   * Test {@link InstantiationClassMarker#isInstantiated(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link InstantiationClassMarker#isInstantiated(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isInstantiated(Clazz); given ClassOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean InstantiationClassMarker.isInstantiated(Clazz)"})
  void testIsInstantiated_givenClassOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ClassOptimizationInfo());

    // Act and Assert
    assertTrue(InstantiationClassMarker.isInstantiated(clazz));
  }

  /**
   * Test {@link InstantiationClassMarker#isInstantiated(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link InstantiationClassMarker#isInstantiated(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isInstantiated(Clazz); given ProgramClassOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean InstantiationClassMarker.isInstantiated(Clazz)"})
  void testIsInstantiated_givenProgramClassOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act and Assert
    assertFalse(InstantiationClassMarker.isInstantiated(clazz));
  }
}
