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

class NoSideEffectClassMarkerDiffblueTest {
  /**
   * Test {@link NoSideEffectClassMarker#visitAnyClass(Clazz)}.
   *
   * <p>Method under test: {@link NoSideEffectClassMarker#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NoSideEffectClassMarker.visitAnyClass(Clazz)"})
  void testVisitAnyClass() {
    // Arrange
    NoSideEffectClassMarker noSideEffectClassMarker = new NoSideEffectClassMarker();

    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ClassOptimizationInfo());

    // Act
    noSideEffectClassMarker.visitAnyClass(clazz);

    // Assert
    Object processingInfo = clazz.getProcessingInfo();
    assertTrue(processingInfo instanceof ClassOptimizationInfo);
    assertFalse(((ClassOptimizationInfo) processingInfo).hasSideEffects());
    assertFalse(((ClassOptimizationInfo) processingInfo).isDotClassed());
    assertFalse(((ClassOptimizationInfo) processingInfo).isInstanceofed());
    assertTrue(((ClassOptimizationInfo) processingInfo).hasNoSideEffects());
  }

  /**
   * Test {@link NoSideEffectClassMarker#hasNoSideEffects(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link NoSideEffectClassMarker#hasNoSideEffects(Clazz)}
   */
  @Test
  @DisplayName(
      "Test hasNoSideEffects(Clazz); given ClassOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean NoSideEffectClassMarker.hasNoSideEffects(Clazz)"})
  void testHasNoSideEffects_givenClassOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ClassOptimizationInfo());

    // Act and Assert
    assertFalse(NoSideEffectClassMarker.hasNoSideEffects(clazz));
  }
}
