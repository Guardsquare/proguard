package proguard;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.visitor.AllFieldVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.ImplementedClassFilter;
import proguard.fixer.kotlin.KotlinAnnotationCounter;

class SubclassedClassFilterDiffblueTest {
  /**
   * Test {@link SubclassedClassFilter#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link SubclassedClassFilter#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitAnyClass(Clazz); when LibraryClass(); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SubclassedClassFilter.visitAnyClass(Clazz)"})
  void testVisitAnyClass_whenLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    SubclassedClassFilter subclassedClassFilter =
        new SubclassedClassFilter(mock(ClassVisitor.class));

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> subclassedClassFilter.visitAnyClass(new LibraryClass()));
  }

  /**
   * Test {@link SubclassedClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link SubclassedClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SubclassedClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass() {
    // Arrange
    SubclassedClassFilter subclassedClassFilter =
        new SubclassedClassFilter(new AllFieldVisitor(new KotlinAnnotationCounter()));
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass programClass = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    programClass.subClassCount = 1;

    // Act and Assert
    assertDoesNotThrow(() -> subclassedClassFilter.visitProgramClass(programClass));
  }

  /**
   * Test {@link SubclassedClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassVisitor} {@link ClassVisitor#visitProgramClass(ProgramClass)} does
   *       nothing.
   * </ul>
   *
   * <p>Method under test: {@link SubclassedClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassVisitor visitProgramClass(ProgramClass) does nothing")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SubclassedClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassVisitorVisitProgramClassDoesNothing() {
    // Arrange
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    SubclassedClassFilter subclassedClassFilter = new SubclassedClassFilter(classVisitor);
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass programClass = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    programClass.subClassCount = 1;

    // Act
    subclassedClassFilter.visitProgramClass(programClass);

    // Assert
    verify(classVisitor).visitProgramClass(isA(ProgramClass.class));
  }

  /**
   * Test {@link SubclassedClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link SubclassedClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SubclassedClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenThrowUnsupportedOperationException() {
    // Arrange
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doThrow(new UnsupportedOperationException())
        .when(classVisitor)
        .visitProgramClass(Mockito.<ProgramClass>any());
    SubclassedClassFilter subclassedClassFilter = new SubclassedClassFilter(classVisitor);
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass programClass = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    programClass.subClassCount = 1;

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> subclassedClassFilter.visitProgramClass(programClass));
    verify(classVisitor).visitProgramClass(isA(ProgramClass.class));
  }

  /**
   * Test {@link SubclassedClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>When {@link ProgramClass#ProgramClass()}.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link SubclassedClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); when ProgramClass(); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SubclassedClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_whenProgramClass_thenDoesNotThrow() {
    // Arrange
    SubclassedClassFilter subclassedClassFilter =
        new SubclassedClassFilter(mock(ClassVisitor.class));

    // Act and Assert
    assertDoesNotThrow(() -> subclassedClassFilter.visitProgramClass(new ProgramClass()));
  }

  /**
   * Test {@link SubclassedClassFilter#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassVisitor} {@link ClassVisitor#visitLibraryClass(LibraryClass)} does
   *       nothing.
   * </ul>
   *
   * <p>Method under test: {@link SubclassedClassFilter#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryClass(LibraryClass); given ClassVisitor visitLibraryClass(LibraryClass) does nothing")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SubclassedClassFilter.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_givenClassVisitorVisitLibraryClassDoesNothing() {
    // Arrange
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    SubclassedClassFilter subclassedClassFilter = new SubclassedClassFilter(classVisitor);
    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.subClassCount = 1;

    // Act
    subclassedClassFilter.visitLibraryClass(libraryClass);

    // Assert
    verify(classVisitor).visitLibraryClass(isA(LibraryClass.class));
  }

  /**
   * Test {@link SubclassedClassFilter#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassVisitor} {@link ClassVisitor#visitLibraryClass(LibraryClass)} does
   *       nothing.
   * </ul>
   *
   * <p>Method under test: {@link SubclassedClassFilter#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryClass(LibraryClass); given ClassVisitor visitLibraryClass(LibraryClass) does nothing")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SubclassedClassFilter.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_givenClassVisitorVisitLibraryClassDoesNothing2() {
    // Arrange
    ClassVisitor rejectedClassVisistor = mock(ClassVisitor.class);
    doNothing().when(rejectedClassVisistor).visitLibraryClass(Mockito.<LibraryClass>any());
    SubclassedClassFilter subclassedClassFilter =
        new SubclassedClassFilter(
            new ImplementedClassFilter(
                new LibraryClass(), true, mock(ClassVisitor.class), rejectedClassVisistor));
    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.subClassCount = 1;

    // Act
    subclassedClassFilter.visitLibraryClass(libraryClass);

    // Assert
    verify(rejectedClassVisistor).visitLibraryClass(isA(LibraryClass.class));
  }

  /**
   * Test {@link SubclassedClassFilter#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link SubclassedClassFilter#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SubclassedClassFilter.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doThrow(new UnsupportedOperationException())
        .when(classVisitor)
        .visitLibraryClass(Mockito.<LibraryClass>any());
    SubclassedClassFilter subclassedClassFilter = new SubclassedClassFilter(classVisitor);
    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.subClassCount = 1;

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> subclassedClassFilter.visitLibraryClass(libraryClass));
    verify(classVisitor).visitLibraryClass(isA(LibraryClass.class));
  }

  /**
   * Test {@link SubclassedClassFilter#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link SubclassedClassFilter#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); when LibraryClass(); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SubclassedClassFilter.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_whenLibraryClass_thenDoesNotThrow() {
    // Arrange
    SubclassedClassFilter subclassedClassFilter =
        new SubclassedClassFilter(mock(ClassVisitor.class));

    // Act and Assert
    assertDoesNotThrow(() -> subclassedClassFilter.visitLibraryClass(new LibraryClass()));
  }
}
