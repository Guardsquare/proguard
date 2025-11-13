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

class SimpleEnumMarkerDiffblueTest {
  /**
   * Test {@link SimpleEnumMarker#isSimpleEnum(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumMarker#isSimpleEnum(Clazz)}
   */
  @Test
  @DisplayName("Test isSimpleEnum(Clazz); given ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean SimpleEnumMarker.isSimpleEnum(Clazz)"})
  void testIsSimpleEnum_givenClassOptimizationInfo() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ClassOptimizationInfo());

    // Act and Assert
    assertFalse(SimpleEnumMarker.isSimpleEnum(clazz));
  }

  /**
   * Test {@link SimpleEnumMarker#isSimpleEnum(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumMarker#isSimpleEnum(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isSimpleEnum(Clazz); given ProgramClassOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean SimpleEnumMarker.isSimpleEnum(Clazz)"})
  void testIsSimpleEnum_givenProgramClassOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act and Assert
    assertFalse(SimpleEnumMarker.isSimpleEnum(clazz));
  }

  /**
   * Test {@link SimpleEnumMarker#isSimpleEnum(Clazz)}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumMarker#isSimpleEnum(Clazz)}
   */
  @Test
  @DisplayName("Test isSimpleEnum(Clazz); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean SimpleEnumMarker.isSimpleEnum(Clazz)"})
  void testIsSimpleEnum_thenReturnTrue() {
    // Arrange
    ProgramClassOptimizationInfo programClassOptimizationInfo = new ProgramClassOptimizationInfo();
    programClassOptimizationInfo.setSimpleEnum(true);

    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(programClassOptimizationInfo);

    // Act and Assert
    assertTrue(SimpleEnumMarker.isSimpleEnum(clazz));
  }
}
