package proguard.optimize.peephole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.visitor.ClassVisitor;
import proguard.optimize.info.ClassOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfo;

class ClassFinalizerDiffblueTest {
  /**
   * Test {@link ClassFinalizer#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassFinalizer#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassFinalizer.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass() {
    // Arrange
    ClassVisitor extraClassVisitor = mock(ClassVisitor.class);
    doNothing().when(extraClassVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    ClassFinalizer classFinalizer = new ClassFinalizer(extraClassVisitor);
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass programClass = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    programClass.subClassCount = 0;

    // Act
    classFinalizer.visitProgramClass(programClass);

    // Assert
    verify(extraClassVisitor).visitProgramClass(isA(ProgramClass.class));
    assertEquals(17, programClass.getAccessFlags());
  }

  /**
   * Test {@link ClassFinalizer#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassFinalizer#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassFinalizer.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass2() {
    // Arrange
    ClassFinalizer classFinalizer = new ClassFinalizer(mock(ClassVisitor.class));
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass programClass = new ProgramClass(1, 3, constantPool, 1552, 1, 1);
    programClass.subClassCount = 0;

    // Act
    classFinalizer.visitProgramClass(programClass);

    // Assert that nothing has changed
    assertEquals(1552, programClass.getAccessFlags());
  }

  /**
   * Test {@link ClassFinalizer#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link ClassFinalizer#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassFinalizer.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass3() {
    // Arrange
    ClassFinalizer classFinalizer = new ClassFinalizer(mock(ClassVisitor.class));
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass programClass = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    programClass.subClassCount = 1552;

    // Act
    classFinalizer.visitProgramClass(programClass);

    // Assert that nothing has changed
    assertEquals(1, programClass.getAccessFlags());
  }

  /**
   * Test {@link ClassFinalizer#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ClassFinalizer#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ProgramClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassFinalizer.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenProgramClassOptimizationInfo() {
    // Arrange
    ClassFinalizer classFinalizer = new ClassFinalizer();

    ProgramClass programClass = new ProgramClass();
    programClass.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act
    classFinalizer.visitProgramClass(programClass);

    // Assert
    assertEquals(Short.SIZE, programClass.getAccessFlags());
  }

  /**
   * Test {@link ClassFinalizer#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then {@link ProgramClass#ProgramClass()} AccessFlags is zero.
   * </ul>
   *
   * <p>Method under test: {@link ClassFinalizer#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then ProgramClass() AccessFlags is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassFinalizer.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenProgramClassAccessFlagsIsZero() {
    // Arrange
    ClassFinalizer classFinalizer = new ClassFinalizer();

    ProgramClass programClass = new ProgramClass();
    programClass.setProcessingInfo(new ClassOptimizationInfo());

    // Act
    classFinalizer.visitProgramClass(programClass);

    // Assert that nothing has changed
    assertEquals(0, programClass.getAccessFlags());
  }

  /**
   * Test {@link ClassFinalizer#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>When {@link ProgramClass#ProgramClass()}.
   *   <li>Then {@link ProgramClass#ProgramClass()} AccessFlags is {@link Short#SIZE}.
   * </ul>
   *
   * <p>Method under test: {@link ClassFinalizer#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); when ProgramClass(); then ProgramClass() AccessFlags is SIZE")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassFinalizer.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_whenProgramClass_thenProgramClassAccessFlagsIsSize() {
    // Arrange
    ClassFinalizer classFinalizer = new ClassFinalizer();
    ProgramClass programClass = new ProgramClass();

    // Act
    classFinalizer.visitProgramClass(programClass);

    // Assert
    assertEquals(Short.SIZE, programClass.getAccessFlags());
  }
}
