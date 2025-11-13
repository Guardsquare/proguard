package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;

class FinalFieldAssignmentMarkerDiffblueTest {
  /**
   * Test {@link FinalFieldAssignmentMarker#assignsFinalField(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link FinalFieldAssignmentMarker#assignsFinalField(Method)}
   */
  @Test
  @DisplayName(
      "Test assignsFinalField(Method); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean FinalFieldAssignmentMarker.assignsFinalField(Method)"})
  void testAssignsFinalField_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(FinalFieldAssignmentMarker.assignsFinalField(method));
  }
}
