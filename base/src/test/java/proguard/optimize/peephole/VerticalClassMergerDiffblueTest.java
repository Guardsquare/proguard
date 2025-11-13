package proguard.optimize.peephole;

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
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassVisitor;

class VerticalClassMergerDiffblueTest {
  /**
   * Test {@link VerticalClassMerger#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link LibraryClass} {@link LibraryClass#accept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link LibraryClass#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link VerticalClassMerger#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given LibraryClass accept(ClassVisitor) does nothing; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void VerticalClassMerger.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenLibraryClassAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    VerticalClassMerger verticalClassMerger = new VerticalClassMerger(true, true);

    LibraryClass clazz = mock(LibraryClass.class);
    doNothing().when(clazz).accept(Mockito.<ClassVisitor>any());

    ProgramClass programClass = new ProgramClass();
    programClass.addSubClass(clazz);

    // Act
    verticalClassMerger.visitProgramClass(programClass);

    // Assert
    verify(clazz).accept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link VerticalClassMerger#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#subclassesAccept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link VerticalClassMerger#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then calls subclassesAccept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void VerticalClassMerger.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsSubclassesAccept() {
    // Arrange
    VerticalClassMerger verticalClassMerger = new VerticalClassMerger(true, true);

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).subclassesAccept(Mockito.<ClassVisitor>any());

    // Act
    verticalClassMerger.visitProgramClass(programClass);

    // Assert
    verify(programClass).subclassesAccept(isA(ClassVisitor.class));
  }
}
