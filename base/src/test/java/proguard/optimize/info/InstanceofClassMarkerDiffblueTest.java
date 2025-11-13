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

class InstanceofClassMarkerDiffblueTest {
  /**
   * Test {@link InstanceofClassMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link InstanceofClassMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InstanceofClassMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant() {
    // Arrange
    InstanceofClassMarker instanceofClassMarker = new InstanceofClassMarker();
    LibraryClass clazz = new LibraryClass();

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(new ProgramClassOptimizationInfo());
    ClassConstant classConstant = new ClassConstant(1, referencedClass);

    // Act
    instanceofClassMarker.visitClassConstant(clazz, classConstant);

    // Assert
    Clazz clazz2 = classConstant.referencedClass;
    assertTrue(clazz2 instanceof LibraryClass);
    Object processingInfo = clazz2.getProcessingInfo();
    assertTrue(processingInfo instanceof ProgramClassOptimizationInfo);
    assertTrue(((ProgramClassOptimizationInfo) processingInfo).isInstanceofed());
  }

  /**
   * Test {@link InstanceofClassMarker#visitAnyClass(Clazz)}.
   *
   * <p>Method under test: {@link InstanceofClassMarker#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InstanceofClassMarker.visitAnyClass(Clazz)"})
  void testVisitAnyClass() {
    // Arrange
    InstanceofClassMarker instanceofClassMarker = new InstanceofClassMarker();

    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act
    instanceofClassMarker.visitAnyClass(clazz);

    // Assert
    Object processingInfo = clazz.getProcessingInfo();
    assertTrue(processingInfo instanceof ProgramClassOptimizationInfo);
    assertTrue(((ProgramClassOptimizationInfo) processingInfo).isInstanceofed());
  }

  /**
   * Test {@link InstanceofClassMarker#isInstanceofed(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link InstanceofClassMarker#isInstanceofed(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isInstanceofed(Clazz); given ClassOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean InstanceofClassMarker.isInstanceofed(Clazz)"})
  void testIsInstanceofed_givenClassOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    clazz.processingInfo = new ClassOptimizationInfo();

    // Act and Assert
    assertTrue(InstanceofClassMarker.isInstanceofed(clazz));
  }

  /**
   * Test {@link InstanceofClassMarker#isInstanceofed(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link InstanceofClassMarker#isInstanceofed(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isInstanceofed(Clazz); given ProgramClassOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean InstanceofClassMarker.isInstanceofed(Clazz)"})
  void testIsInstanceofed_givenProgramClassOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    clazz.processingInfo = new ProgramClassOptimizationInfo();

    // Act and Assert
    assertFalse(InstanceofClassMarker.isInstanceofed(clazz));
  }
}
