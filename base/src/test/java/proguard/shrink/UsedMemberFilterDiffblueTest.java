package proguard.shrink;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
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
import proguard.classfile.LibraryField;
import proguard.classfile.LibraryMethod;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.visitor.MemberVisitor;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.obfuscate.MemberNameCleaner;
import proguard.obfuscate.kotlin.KotlinValueParameterUsageMarker;
import proguard.util.Processable;

class UsedMemberFilterDiffblueTest {
  /**
   * Test {@link UsedMemberFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link UsedMemberFilter#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsedMemberFilter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinAnnotationCounter usedMemberFilter = new KotlinAnnotationCounter();

    UsedMemberFilter usedMemberFilter2 =
        new UsedMemberFilter(usageMarker, usedMemberFilter, new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    // Act
    usedMemberFilter2.visitProgramField(programClass, new ProgramField());

    // Assert
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsedMemberFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link UsedMemberFilter#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsedMemberFilter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField2() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    MemberNameCleaner usedMemberFilter = new MemberNameCleaner();

    UsedMemberFilter usedMemberFilter2 =
        new UsedMemberFilter(usageMarker, usedMemberFilter, new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    // Act
    usedMemberFilter2.visitProgramField(programClass, new ProgramField());

    // Assert
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsedMemberFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramField#accept(Clazz, MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link UsedMemberFilter#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); then calls accept(Clazz, MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsedMemberFilter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_thenCallsAccept() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinAnnotationCounter usedMemberFilter = new KotlinAnnotationCounter();

    UsedMemberFilter usedMemberFilter2 =
        new UsedMemberFilter(usageMarker, usedMemberFilter, new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    ProgramField programField = mock(ProgramField.class);
    doNothing().when(programField).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());

    // Act
    usedMemberFilter2.visitProgramField(programClass, programField);

    // Assert
    verify(programField).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsedMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link UsedMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsedMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinAnnotationCounter usedMemberFilter = new KotlinAnnotationCounter();

    UsedMemberFilter usedMemberFilter2 =
        new UsedMemberFilter(usageMarker, usedMemberFilter, new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    // Act
    usedMemberFilter2.visitProgramMethod(programClass, new ProgramMethod());

    // Assert
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsedMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link UsedMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsedMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod2() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    MemberNameCleaner usedMemberFilter = new MemberNameCleaner();

    UsedMemberFilter usedMemberFilter2 =
        new UsedMemberFilter(usageMarker, usedMemberFilter, new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    // Act
    usedMemberFilter2.visitProgramMethod(programClass, new ProgramMethod());

    // Assert
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsedMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>When {@link ProgramMethod} {@link ProgramMethod#accept(Clazz, MemberVisitor)} does
   *       nothing.
   *   <li>Then calls {@link ProgramMethod#accept(Clazz, MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link UsedMemberFilter#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); when ProgramMethod accept(Clazz, MemberVisitor) does nothing; then calls accept(Clazz, MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsedMemberFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_whenProgramMethodAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinAnnotationCounter usedMemberFilter = new KotlinAnnotationCounter();

    UsedMemberFilter usedMemberFilter2 =
        new UsedMemberFilter(usageMarker, usedMemberFilter, new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = mock(ProgramMethod.class);
    doNothing().when(programMethod).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());

    // Act
    usedMemberFilter2.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programMethod).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsedMemberFilter#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <p>Method under test: {@link UsedMemberFilter#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName("Test visitLibraryField(LibraryClass, LibraryField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsedMemberFilter.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinAnnotationCounter usedMemberFilter = new KotlinAnnotationCounter();

    UsedMemberFilter usedMemberFilter2 =
        new UsedMemberFilter(usageMarker, usedMemberFilter, new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();

    // Act
    usedMemberFilter2.visitLibraryField(libraryClass, new LibraryField());

    // Assert
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsedMemberFilter#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <p>Method under test: {@link UsedMemberFilter#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName("Test visitLibraryField(LibraryClass, LibraryField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsedMemberFilter.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField2() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    MemberNameCleaner usedMemberFilter = new MemberNameCleaner();

    UsedMemberFilter usedMemberFilter2 =
        new UsedMemberFilter(usageMarker, usedMemberFilter, new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();

    // Act
    usedMemberFilter2.visitLibraryField(libraryClass, new LibraryField());

    // Assert
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsedMemberFilter#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <p>Method under test: {@link UsedMemberFilter#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName("Test visitLibraryField(LibraryClass, LibraryField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsedMemberFilter.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField3() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinValueParameterUsageMarker usedMemberFilter = new KotlinValueParameterUsageMarker();

    UsedMemberFilter usedMemberFilter2 =
        new UsedMemberFilter(usageMarker, usedMemberFilter, new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();

    // Act
    usedMemberFilter2.visitLibraryField(libraryClass, new LibraryField());

    // Assert
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsedMemberFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <p>Method under test: {@link UsedMemberFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}
   */
  @Test
  @DisplayName("Test visitLibraryMethod(LibraryClass, LibraryMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsedMemberFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinAnnotationCounter usedMemberFilter = new KotlinAnnotationCounter();

    UsedMemberFilter usedMemberFilter2 =
        new UsedMemberFilter(usageMarker, usedMemberFilter, new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();

    // Act
    usedMemberFilter2.visitLibraryMethod(libraryClass, new LibraryMethod());

    // Assert
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsedMemberFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <p>Method under test: {@link UsedMemberFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}
   */
  @Test
  @DisplayName("Test visitLibraryMethod(LibraryClass, LibraryMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsedMemberFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod2() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    MemberNameCleaner usedMemberFilter = new MemberNameCleaner();

    UsedMemberFilter usedMemberFilter2 =
        new UsedMemberFilter(usageMarker, usedMemberFilter, new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();

    // Act
    usedMemberFilter2.visitLibraryMethod(libraryClass, new LibraryMethod());

    // Assert
    verify(usageMarker).isUsed(isA(Processable.class));
  }

  /**
   * Test {@link UsedMemberFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <p>Method under test: {@link UsedMemberFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}
   */
  @Test
  @DisplayName("Test visitLibraryMethod(LibraryClass, LibraryMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void UsedMemberFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod3() {
    // Arrange
    ShortestUsageMarker usageMarker = mock(ShortestUsageMarker.class);
    when(usageMarker.isUsed(Mockito.<Processable>any())).thenReturn(true);
    KotlinValueParameterUsageMarker usedMemberFilter = new KotlinValueParameterUsageMarker();

    UsedMemberFilter usedMemberFilter2 =
        new UsedMemberFilter(usageMarker, usedMemberFilter, new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();

    // Act
    usedMemberFilter2.visitLibraryMethod(libraryClass, new LibraryMethod());

    // Assert
    verify(usageMarker).isUsed(isA(Processable.class));
  }
}
