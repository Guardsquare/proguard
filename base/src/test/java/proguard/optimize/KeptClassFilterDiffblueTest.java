package proguard.optimize;

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
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.visitor.ClassVisitor;
import proguard.optimize.info.ClassOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfo;

class KeptClassFilterDiffblueTest {
  /**
   * Test {@link KeptClassFilter#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link KeptClassFilter#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz); given ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeptClassFilter.visitAnyClass(Clazz)"})
  void testVisitAnyClass_givenClassOptimizationInfo() {
    // Arrange
    ClassVisitor acceptedVisitor = mock(ClassVisitor.class);
    doNothing().when(acceptedVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    KeptClassFilter keptClassFilter =
        new KeptClassFilter(acceptedVisitor, mock(ClassVisitor.class));

    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ClassOptimizationInfo());

    // Act
    keptClassFilter.visitAnyClass(clazz);

    // Assert
    verify(acceptedVisitor).visitLibraryClass(isA(LibraryClass.class));
  }

  /**
   * Test {@link KeptClassFilter#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link KeptClassFilter#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitAnyClass(Clazz); given ProgramClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeptClassFilter.visitAnyClass(Clazz)"})
  void testVisitAnyClass_givenProgramClassOptimizationInfo() {
    // Arrange
    ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
    doNothing().when(rejectedVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    KeptClassFilter keptClassFilter =
        new KeptClassFilter(mock(ClassVisitor.class), rejectedVisitor);

    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act
    keptClassFilter.visitAnyClass(clazz);

    // Assert
    verify(rejectedVisitor).visitLibraryClass(isA(LibraryClass.class));
  }

  /**
   * Test {@link KeptClassFilter#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then calls {@link ClassVisitor#visitLibraryClass(LibraryClass)}.
   * </ul>
   *
   * <p>Method under test: {@link KeptClassFilter#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitAnyClass(Clazz); when LibraryClass(); then calls visitLibraryClass(LibraryClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeptClassFilter.visitAnyClass(Clazz)"})
  void testVisitAnyClass_whenLibraryClass_thenCallsVisitLibraryClass() {
    // Arrange
    ClassVisitor rejectedVisitor = mock(ClassVisitor.class);
    doNothing().when(rejectedVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    KeptClassFilter keptClassFilter =
        new KeptClassFilter(mock(ClassVisitor.class), rejectedVisitor);

    // Act
    keptClassFilter.visitAnyClass(new LibraryClass());

    // Assert
    verify(rejectedVisitor).visitLibraryClass(isA(LibraryClass.class));
  }
}
