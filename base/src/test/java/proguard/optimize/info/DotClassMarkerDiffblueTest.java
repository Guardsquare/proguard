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

class DotClassMarkerDiffblueTest {
  /**
   * Test {@link DotClassMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <p>Method under test: {@link DotClassMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DotClassMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant() {
    // Arrange
    DotClassMarker dotClassMarker = new DotClassMarker();
    LibraryClass clazz = new LibraryClass();

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(new ProgramClassOptimizationInfo());
    ClassConstant classConstant = new ClassConstant(1, referencedClass);

    // Act
    dotClassMarker.visitClassConstant(clazz, classConstant);

    // Assert
    Clazz clazz2 = classConstant.referencedClass;
    assertTrue(clazz2 instanceof LibraryClass);
    Object processingInfo = clazz2.getProcessingInfo();
    assertTrue(processingInfo instanceof ProgramClassOptimizationInfo);
    assertTrue(((ProgramClassOptimizationInfo) processingInfo).isDotClassed());
  }

  /**
   * Test {@link DotClassMarker#visitAnyClass(Clazz)}.
   *
   * <p>Method under test: {@link DotClassMarker#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void DotClassMarker.visitAnyClass(Clazz)"})
  void testVisitAnyClass() {
    // Arrange
    DotClassMarker dotClassMarker = new DotClassMarker();

    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act
    dotClassMarker.visitAnyClass(clazz);

    // Assert
    Object processingInfo = clazz.getProcessingInfo();
    assertTrue(processingInfo instanceof ProgramClassOptimizationInfo);
    assertTrue(((ProgramClassOptimizationInfo) processingInfo).isDotClassed());
  }

  /**
   * Test {@link DotClassMarker#isDotClassed(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link DotClassMarker#isDotClassed(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isDotClassed(Clazz); given ClassOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean DotClassMarker.isDotClassed(Clazz)"})
  void testIsDotClassed_givenClassOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    clazz.processingInfo = new ClassOptimizationInfo();

    // Act and Assert
    assertTrue(DotClassMarker.isDotClassed(clazz));
  }

  /**
   * Test {@link DotClassMarker#isDotClassed(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link DotClassMarker#isDotClassed(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isDotClassed(Clazz); given ProgramClassOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean DotClassMarker.isDotClassed(Clazz)"})
  void testIsDotClassed_givenProgramClassOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    clazz.processingInfo = new ProgramClassOptimizationInfo();

    // Act and Assert
    assertFalse(DotClassMarker.isDotClassed(clazz));
  }
}
