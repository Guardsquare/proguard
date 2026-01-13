package proguard.optimize.peephole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.optimize.info.MethodOptimizationInfo;

class MemberPrivatizerDiffblueTest {
  /**
   * Test {@link MemberPrivatizer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link MemberPrivatizer#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberPrivatizer.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod() {
    // Arrange
    MemberPrivatizer memberPrivatizer = new MemberPrivatizer(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.canBeMadePrivate()).thenReturn(true);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    memberPrivatizer.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).canBeMadePrivate();
    assertEquals(2, programMethod.getAccessFlags());
  }

  /**
   * Test {@link MemberPrivatizer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link MemberPrivatizer#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given MethodOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberPrivatizer.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_givenMethodOptimizationInfo() {
    // Arrange
    MemberPrivatizer memberPrivatizer = new MemberPrivatizer();
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(new MethodOptimizationInfo());

    // Act
    memberPrivatizer.visitProgramMethod(programClass, programMethod);

    // Assert that nothing has changed
    assertEquals(0, programMethod.getAccessFlags());
  }

  /**
   * Test {@link MemberPrivatizer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} {@link MethodOptimizationInfo#canBeMadePrivate()}
   *       return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link MemberPrivatizer#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given MethodOptimizationInfo canBeMadePrivate() return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberPrivatizer.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_givenMethodOptimizationInfoCanBeMadePrivateReturnFalse() {
    // Arrange
    MemberPrivatizer memberPrivatizer = new MemberPrivatizer();
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.canBeMadePrivate()).thenReturn(false);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    memberPrivatizer.visitProgramMethod(programClass, programMethod);

    // Assert that nothing has changed
    verify(methodOptimizationInfo).canBeMadePrivate();
    assertEquals(0, programMethod.getAccessFlags());
  }

  /**
   * Test {@link MemberPrivatizer#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then {@link ProgramMethod#ProgramMethod()} AccessFlags is two.
   * </ul>
   *
   * <p>Method under test: {@link MemberPrivatizer#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then ProgramMethod() AccessFlags is two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberPrivatizer.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenProgramMethodAccessFlagsIsTwo() {
    // Arrange
    MemberPrivatizer memberPrivatizer = new MemberPrivatizer();
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.canBeMadePrivate()).thenReturn(true);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    memberPrivatizer.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).canBeMadePrivate();
    assertEquals(2, programMethod.getAccessFlags());
  }
}
