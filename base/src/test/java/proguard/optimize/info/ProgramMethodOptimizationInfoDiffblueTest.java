package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertNull;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;

class ProgramMethodOptimizationInfoDiffblueTest {
  /**
   * Test {@link ProgramMethodOptimizationInfo#getProgramMethodOptimizationInfo(Method)}.
   *
   * <ul>
   *   <li>When {@link LibraryMethod#LibraryMethod()}.
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ProgramMethodOptimizationInfo#getProgramMethodOptimizationInfo(Method)}
   */
  @Test
  @DisplayName(
      "Test getProgramMethodOptimizationInfo(Method); when LibraryMethod(); then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ProgramMethodOptimizationInfo ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(Method)"
  })
  void testGetProgramMethodOptimizationInfo_whenLibraryMethod_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(ProgramMethodOptimizationInfo.getProgramMethodOptimizationInfo(new LibraryMethod()));
  }
}
