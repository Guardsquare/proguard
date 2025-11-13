package proguard.classfile.visitor;

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

class InjectedClassFilterDiffblueTest {
  /**
   * Test {@link InjectedClassFilter#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link InjectedClassFilter#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitAnyClass(Clazz); when LibraryClass(); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InjectedClassFilter.visitAnyClass(Clazz)"})
  void testVisitAnyClass_whenLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    InjectedClassFilter injectedClassFilter =
        new InjectedClassFilter(mock(ClassVisitor.class), mock(ClassVisitor.class));

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> injectedClassFilter.visitAnyClass(new LibraryClass()));
  }

  /**
   * Test {@link InjectedClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link InjectedClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InjectedClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass() {
    // Arrange
    ClassVisitor injectedClassVisitor = mock(ClassVisitor.class);
    doNothing().when(injectedClassVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    InjectedClassFilter injectedClassFilter =
        new InjectedClassFilter(injectedClassVisitor, mock(ClassVisitor.class));
    Constant[] constantPool = new Constant[] {new ClassConstant()};

    // Act
    injectedClassFilter.visitProgramClass(
        new ProgramClass(
            512, 3, constantPool, 512, 512, 512, "Feature Name", 512, "Processing Info"));

    // Assert
    verify(injectedClassVisitor).visitProgramClass(isA(ProgramClass.class));
  }

  /**
   * Test {@link InjectedClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassVisitor#visitProgramClass(ProgramClass)}.
   * </ul>
   *
   * <p>Method under test: {@link InjectedClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then calls visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InjectedClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsVisitProgramClass() {
    // Arrange
    ClassVisitor otherClassVisitor = mock(ClassVisitor.class);
    doNothing().when(otherClassVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    InjectedClassFilter injectedClassFilter =
        new InjectedClassFilter(mock(ClassVisitor.class), otherClassVisitor);

    // Act
    injectedClassFilter.visitProgramClass(new ProgramClass());

    // Assert
    verify(otherClassVisitor).visitProgramClass(isA(ProgramClass.class));
  }

  /**
   * Test {@link InjectedClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link InjectedClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InjectedClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenDoesNotThrow() {
    // Arrange
    InjectedClassFilter injectedClassFilter =
        new InjectedClassFilter(mock(ClassVisitor.class), null);

    // Act and Assert
    assertDoesNotThrow(() -> injectedClassFilter.visitProgramClass(new ProgramClass()));
  }

  /**
   * Test {@link InjectedClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link InjectedClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InjectedClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenThrowUnsupportedOperationException() {
    // Arrange
    ClassVisitor otherClassVisitor = mock(ClassVisitor.class);
    doThrow(new UnsupportedOperationException())
        .when(otherClassVisitor)
        .visitProgramClass(Mockito.<ProgramClass>any());
    InjectedClassFilter injectedClassFilter =
        new InjectedClassFilter(mock(ClassVisitor.class), otherClassVisitor);

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> injectedClassFilter.visitProgramClass(new ProgramClass()));
    verify(otherClassVisitor).visitProgramClass(isA(ProgramClass.class));
  }

  /**
   * Test {@link InjectedClassFilter#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassVisitor} {@link ClassVisitor#visitLibraryClass(LibraryClass)} does
   *       nothing.
   * </ul>
   *
   * <p>Method under test: {@link InjectedClassFilter#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryClass(LibraryClass); given ClassVisitor visitLibraryClass(LibraryClass) does nothing")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InjectedClassFilter.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_givenClassVisitorVisitLibraryClassDoesNothing() {
    // Arrange
    ClassVisitor otherClassVisitor = mock(ClassVisitor.class);
    doNothing().when(otherClassVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    InjectedClassFilter injectedClassFilter =
        new InjectedClassFilter(mock(ClassVisitor.class), otherClassVisitor);

    // Act
    injectedClassFilter.visitLibraryClass(new LibraryClass());

    // Assert
    verify(otherClassVisitor).visitLibraryClass(isA(LibraryClass.class));
  }

  /**
   * Test {@link InjectedClassFilter#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link InjectedClassFilter#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InjectedClassFilter.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_thenDoesNotThrow() {
    // Arrange
    InjectedClassFilter injectedClassFilter =
        new InjectedClassFilter(mock(ClassVisitor.class), null);

    // Act and Assert
    assertDoesNotThrow(() -> injectedClassFilter.visitLibraryClass(new LibraryClass()));
  }

  /**
   * Test {@link InjectedClassFilter#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link InjectedClassFilter#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void InjectedClassFilter.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    ClassVisitor otherClassVisitor = mock(ClassVisitor.class);
    doThrow(new UnsupportedOperationException())
        .when(otherClassVisitor)
        .visitLibraryClass(Mockito.<LibraryClass>any());
    InjectedClassFilter injectedClassFilter =
        new InjectedClassFilter(mock(ClassVisitor.class), otherClassVisitor);

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> injectedClassFilter.visitLibraryClass(new LibraryClass()));
    verify(otherClassVisitor).visitLibraryClass(isA(LibraryClass.class));
  }
}
