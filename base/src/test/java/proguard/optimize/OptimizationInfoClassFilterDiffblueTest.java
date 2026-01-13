package proguard.optimize;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassVisitor;
import proguard.optimize.info.ProgramClassOptimizationInfo;

class OptimizationInfoClassFilterDiffblueTest {
  /**
   * Test {@link OptimizationInfoClassFilter#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizationInfoClassFilter#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitAnyClass(Clazz); when LibraryClass(); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizationInfoClassFilter.visitAnyClass(Clazz)"})
  void testVisitAnyClass_whenLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    OptimizationInfoClassFilter optimizationInfoClassFilter =
        new OptimizationInfoClassFilter(mock(ClassVisitor.class));

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> optimizationInfoClassFilter.visitAnyClass(new LibraryClass()));
  }

  /**
   * Test {@link OptimizationInfoClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassVisitor} {@link ClassVisitor#visitProgramClass(ProgramClass)} does
   *       nothing.
   * </ul>
   *
   * <p>Method under test: {@link OptimizationInfoClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassVisitor visitProgramClass(ProgramClass) does nothing")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizationInfoClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassVisitorVisitProgramClassDoesNothing() {
    // Arrange
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    OptimizationInfoClassFilter optimizationInfoClassFilter =
        new OptimizationInfoClassFilter(classVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(new ProgramClassOptimizationInfo());
    doNothing().when(programClass).addSubClass(Mockito.<Clazz>any());
    programClass.addSubClass(new LibraryClass());

    // Act
    optimizationInfoClassFilter.visitProgramClass(programClass);

    // Assert
    verify(programClass).addSubClass(isA(Clazz.class));
    verify(classVisitor).visitProgramClass(isA(ProgramClass.class));
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link OptimizationInfoClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizationInfoClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizationInfoClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenThrowUnsupportedOperationException() {
    // Arrange
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doThrow(new UnsupportedOperationException())
        .when(classVisitor)
        .visitProgramClass(Mockito.<ProgramClass>any());
    OptimizationInfoClassFilter optimizationInfoClassFilter =
        new OptimizationInfoClassFilter(classVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(new ProgramClassOptimizationInfo());
    doNothing().when(programClass).addSubClass(Mockito.<Clazz>any());
    programClass.addSubClass(new LibraryClass());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> optimizationInfoClassFilter.visitProgramClass(programClass));
    verify(programClass).addSubClass(isA(Clazz.class));
    verify(classVisitor).visitProgramClass(isA(ProgramClass.class));
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link OptimizationInfoClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>When {@link ProgramClass#ProgramClass()}.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link OptimizationInfoClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); when ProgramClass(); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizationInfoClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_whenProgramClass_thenDoesNotThrow() {
    // Arrange
    OptimizationInfoClassFilter optimizationInfoClassFilter =
        new OptimizationInfoClassFilter(mock(ClassVisitor.class));

    // Act and Assert
    assertDoesNotThrow(() -> optimizationInfoClassFilter.visitProgramClass(new ProgramClass()));
  }

  /**
   * Test {@link OptimizationInfoClassFilter#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassVisitor} {@link ClassVisitor#visitLibraryClass(LibraryClass)} does
   *       nothing.
   * </ul>
   *
   * <p>Method under test: {@link OptimizationInfoClassFilter#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryClass(LibraryClass); given ClassVisitor visitLibraryClass(LibraryClass) does nothing")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizationInfoClassFilter.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_givenClassVisitorVisitLibraryClassDoesNothing() {
    // Arrange
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    OptimizationInfoClassFilter optimizationInfoClassFilter =
        new OptimizationInfoClassFilter(classVisitor);

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act
    optimizationInfoClassFilter.visitLibraryClass(libraryClass);

    // Assert
    verify(classVisitor).visitLibraryClass(isA(LibraryClass.class));
  }

  /**
   * Test {@link OptimizationInfoClassFilter#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizationInfoClassFilter#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizationInfoClassFilter.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doThrow(new UnsupportedOperationException())
        .when(classVisitor)
        .visitLibraryClass(Mockito.<LibraryClass>any());
    OptimizationInfoClassFilter optimizationInfoClassFilter =
        new OptimizationInfoClassFilter(classVisitor);

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> optimizationInfoClassFilter.visitLibraryClass(libraryClass));
    verify(classVisitor).visitLibraryClass(isA(LibraryClass.class));
  }

  /**
   * Test {@link OptimizationInfoClassFilter#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link OptimizationInfoClassFilter#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); when LibraryClass(); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizationInfoClassFilter.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_whenLibraryClass_thenDoesNotThrow() {
    // Arrange
    OptimizationInfoClassFilter optimizationInfoClassFilter =
        new OptimizationInfoClassFilter(mock(ClassVisitor.class));

    // Act and Assert
    assertDoesNotThrow(() -> optimizationInfoClassFilter.visitLibraryClass(new LibraryClass()));
  }
}
