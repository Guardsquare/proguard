package proguard.optimize.peephole;

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
import proguard.optimize.info.ClassOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfo;

class RetargetedClassFilterDiffblueTest {
  /**
   * Test {@link RetargetedClassFilter#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link RetargetedClassFilter#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitAnyClass(Clazz); when LibraryClass(); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RetargetedClassFilter.visitAnyClass(Clazz)"})
  void testVisitAnyClass_whenLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    RetargetedClassFilter retargetedClassFilter =
        new RetargetedClassFilter(mock(ClassVisitor.class), mock(ClassVisitor.class));

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> retargetedClassFilter.visitAnyClass(new LibraryClass()));
  }

  /**
   * Test {@link RetargetedClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link RetargetedClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RetargetedClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass() {
    // Arrange
    RetargetedClassFilter retargetedClassFilter =
        new RetargetedClassFilter(mock(ClassVisitor.class));

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());
    doNothing().when(programClass).addSubClass(Mockito.<Clazz>any());
    programClass.addSubClass(new LibraryClass());

    // Act
    retargetedClassFilter.visitProgramClass(programClass);

    // Assert
    verify(programClass).addSubClass(isA(Clazz.class));
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link RetargetedClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link RetargetedClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RetargetedClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassOptimizationInfo() {
    // Arrange
    ClassVisitor otherClassVisitor = mock(ClassVisitor.class);
    doNothing().when(otherClassVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    RetargetedClassFilter retargetedClassFilter =
        new RetargetedClassFilter(mock(ClassVisitor.class), otherClassVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());
    doNothing().when(programClass).addSubClass(Mockito.<Clazz>any());
    programClass.addSubClass(new LibraryClass());

    // Act
    retargetedClassFilter.visitProgramClass(programClass);

    // Assert
    verify(programClass).addSubClass(isA(Clazz.class));
    verify(otherClassVisitor).visitProgramClass(isA(ProgramClass.class));
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link RetargetedClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} {@link ClassOptimizationInfo#getTargetClass()} return
   *       {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link RetargetedClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassOptimizationInfo getTargetClass() return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RetargetedClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassOptimizationInfoGetTargetClassReturnNull() {
    // Arrange
    ClassVisitor otherClassVisitor = mock(ClassVisitor.class);
    doNothing().when(otherClassVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    RetargetedClassFilter retargetedClassFilter =
        new RetargetedClassFilter(mock(ClassVisitor.class), otherClassVisitor);

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.getTargetClass()).thenReturn(null);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(classOptimizationInfo);
    doNothing().when(programClass).addSubClass(Mockito.<Clazz>any());
    programClass.addSubClass(new LibraryClass());

    // Act
    retargetedClassFilter.visitProgramClass(programClass);

    // Assert
    verify(programClass).addSubClass(isA(Clazz.class));
    verify(otherClassVisitor).visitProgramClass(isA(ProgramClass.class));
    verify(classOptimizationInfo).getTargetClass();
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link RetargetedClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link RetargetedClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ProgramClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RetargetedClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenProgramClassOptimizationInfo() {
    // Arrange
    ClassVisitor otherClassVisitor = mock(ClassVisitor.class);
    doNothing().when(otherClassVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    RetargetedClassFilter retargetedClassFilter =
        new RetargetedClassFilter(mock(ClassVisitor.class), otherClassVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(new ProgramClassOptimizationInfo());
    doNothing().when(programClass).addSubClass(Mockito.<Clazz>any());
    programClass.addSubClass(new LibraryClass());

    // Act
    retargetedClassFilter.visitProgramClass(programClass);

    // Assert
    verify(programClass).addSubClass(isA(Clazz.class));
    verify(otherClassVisitor).visitProgramClass(isA(ProgramClass.class));
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link RetargetedClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryClass#getProcessingInfo()}.
   * </ul>
   *
   * <p>Method under test: {@link RetargetedClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then calls getProcessingInfo()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RetargetedClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsGetProcessingInfo() {
    // Arrange
    ClassVisitor retargetedClassVisitor = mock(ClassVisitor.class);
    doNothing().when(retargetedClassVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    RetargetedClassFilter retargetedClassFilter =
        new RetargetedClassFilter(retargetedClassVisitor, mock(ClassVisitor.class));

    LibraryClass libraryClass = mock(LibraryClass.class);
    when(libraryClass.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.getTargetClass()).thenReturn(libraryClass);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(classOptimizationInfo);
    doNothing().when(programClass).addSubClass(Mockito.<Clazz>any());
    programClass.addSubClass(new LibraryClass());

    // Act
    retargetedClassFilter.visitProgramClass(programClass);

    // Assert
    verify(programClass).addSubClass(isA(Clazz.class));
    verify(retargetedClassVisitor).visitProgramClass(isA(ProgramClass.class));
    verify(classOptimizationInfo).getTargetClass();
    verify(libraryClass).getProcessingInfo();
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link RetargetedClassFilter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link RetargetedClassFilter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RetargetedClassFilter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenThrowUnsupportedOperationException() {
    // Arrange
    ClassVisitor otherClassVisitor = mock(ClassVisitor.class);
    doThrow(new UnsupportedOperationException())
        .when(otherClassVisitor)
        .visitProgramClass(Mockito.<ProgramClass>any());
    RetargetedClassFilter retargetedClassFilter =
        new RetargetedClassFilter(mock(ClassVisitor.class), otherClassVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());
    doNothing().when(programClass).addSubClass(Mockito.<Clazz>any());
    programClass.addSubClass(new LibraryClass());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> retargetedClassFilter.visitProgramClass(programClass));
    verify(programClass).addSubClass(isA(Clazz.class));
    verify(otherClassVisitor).visitProgramClass(isA(ProgramClass.class));
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link RetargetedClassFilter#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassVisitor} {@link ClassVisitor#visitLibraryClass(LibraryClass)} does
   *       nothing.
   * </ul>
   *
   * <p>Method under test: {@link RetargetedClassFilter#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryClass(LibraryClass); given ClassVisitor visitLibraryClass(LibraryClass) does nothing")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RetargetedClassFilter.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_givenClassVisitorVisitLibraryClassDoesNothing() {
    // Arrange
    ClassVisitor otherClassVisitor = mock(ClassVisitor.class);
    doNothing().when(otherClassVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    RetargetedClassFilter retargetedClassFilter =
        new RetargetedClassFilter(mock(ClassVisitor.class), otherClassVisitor);

    // Act
    retargetedClassFilter.visitLibraryClass(new LibraryClass());

    // Assert
    verify(otherClassVisitor).visitLibraryClass(isA(LibraryClass.class));
  }

  /**
   * Test {@link RetargetedClassFilter#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link RetargetedClassFilter#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RetargetedClassFilter.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_thenDoesNotThrow() {
    // Arrange
    RetargetedClassFilter retargetedClassFilter =
        new RetargetedClassFilter(mock(ClassVisitor.class));

    // Act and Assert
    assertDoesNotThrow(() -> retargetedClassFilter.visitLibraryClass(new LibraryClass()));
  }

  /**
   * Test {@link RetargetedClassFilter#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link RetargetedClassFilter#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RetargetedClassFilter.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    ClassVisitor otherClassVisitor = mock(ClassVisitor.class);
    doThrow(new UnsupportedOperationException())
        .when(otherClassVisitor)
        .visitLibraryClass(Mockito.<LibraryClass>any());
    RetargetedClassFilter retargetedClassFilter =
        new RetargetedClassFilter(mock(ClassVisitor.class), otherClassVisitor);

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> retargetedClassFilter.visitLibraryClass(new LibraryClass()));
    verify(otherClassVisitor).visitLibraryClass(isA(LibraryClass.class));
  }
}
