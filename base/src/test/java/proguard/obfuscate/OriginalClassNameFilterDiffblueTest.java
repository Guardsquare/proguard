package proguard.obfuscate;

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
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.ClassVisitor;
import proguard.testutils.cpa.NamedClass;

class OriginalClassNameFilterDiffblueTest {
  /**
   * Test {@link OriginalClassNameFilter#visitAnyClass(Clazz)}.
   *
   * <p>Method under test: {@link OriginalClassNameFilter#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OriginalClassNameFilter.visitAnyClass(Clazz)"})
  void testVisitAnyClass() {
    // Arrange
    ClassVisitor rejectedClassVisitor = mock(ClassVisitor.class);
    doNothing().when(rejectedClassVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    OriginalClassNameFilter originalClassNameFilter =
        new OriginalClassNameFilter(mock(ClassVisitor.class), rejectedClassVisitor);

    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo("Clazz");

    // Act
    originalClassNameFilter.visitAnyClass(clazz);

    // Assert
    verify(rejectedClassVisitor).visitLibraryClass(isA(LibraryClass.class));
  }

  /**
   * Test {@link OriginalClassNameFilter#visitAnyClass(Clazz)}.
   *
   * <p>Method under test: {@link OriginalClassNameFilter#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OriginalClassNameFilter.visitAnyClass(Clazz)"})
  void testVisitAnyClass2() {
    // Arrange
    ClassVisitor acceptedClassVisitor = mock(ClassVisitor.class);
    doNothing().when(acceptedClassVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    OriginalClassNameFilter originalClassNameFilter =
        new OriginalClassNameFilter(acceptedClassVisitor, mock(ClassVisitor.class));

    LibraryClass clazz = new LibraryClass(1, "Clazz", "Super Class Name");
    clazz.setProcessingInfo("Clazz");

    // Act
    originalClassNameFilter.visitAnyClass(clazz);

    // Assert
    verify(acceptedClassVisitor).visitLibraryClass(isA(LibraryClass.class));
  }

  /**
   * Test {@link OriginalClassNameFilter#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>Given one.
   * </ul>
   *
   * <p>Method under test: {@link OriginalClassNameFilter#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz); given one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OriginalClassNameFilter.visitAnyClass(Clazz)"})
  void testVisitAnyClass_givenOne() {
    // Arrange
    ClassVisitor rejectedClassVisitor = mock(ClassVisitor.class);
    doNothing().when(rejectedClassVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    OriginalClassNameFilter originalClassNameFilter =
        new OriginalClassNameFilter(mock(ClassVisitor.class), rejectedClassVisitor);

    LibraryClass clazz = new LibraryClass(1, "Clazz", "Super Class Name");
    clazz.setProcessingInfo(1);

    // Act
    originalClassNameFilter.visitAnyClass(clazz);

    // Assert
    verify(rejectedClassVisitor).visitLibraryClass(isA(LibraryClass.class));
  }

  /**
   * Test {@link OriginalClassNameFilter#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassVisitor#visitProgramClass(ProgramClass)}.
   * </ul>
   *
   * <p>Method under test: {@link OriginalClassNameFilter#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz); then calls visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OriginalClassNameFilter.visitAnyClass(Clazz)"})
  void testVisitAnyClass_thenCallsVisitProgramClass() {
    // Arrange
    ClassVisitor rejectedClassVisitor = mock(ClassVisitor.class);
    doNothing().when(rejectedClassVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    OriginalClassNameFilter originalClassNameFilter =
        new OriginalClassNameFilter(mock(ClassVisitor.class), rejectedClassVisitor);

    NamedClass clazz = new NamedClass("Member Name");
    clazz.addSubClass(new LibraryClass());

    // Act
    originalClassNameFilter.visitAnyClass(clazz);

    // Assert
    verify(rejectedClassVisitor).visitProgramClass(isA(ProgramClass.class));
  }
}
