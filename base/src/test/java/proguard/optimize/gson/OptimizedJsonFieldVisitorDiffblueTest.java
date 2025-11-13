package proguard.optimize.gson;

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
import proguard.classfile.ProgramField;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MethodImplementationFilter;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.testutils.cpa.NamedClass;

class OptimizedJsonFieldVisitorDiffblueTest {
  /**
   * Test {@link OptimizedJsonFieldVisitor#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>When {@link NamedClass#NamedClass(String)} with {@code Member Name}.
   *   <li>Then calls {@link ClassVisitor#visitProgramClass(ProgramClass)}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizedJsonFieldVisitor#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); when NamedClass(String) with 'Member Name'; then calls visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizedJsonFieldVisitor.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_whenNamedClassWithMemberName_thenCallsVisitProgramClass() {
    // Arrange
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    OptimizedJsonFieldVisitor optimizedJsonFieldVisitor =
        new OptimizedJsonFieldVisitor(classVisitor, new KotlinAnnotationCounter());

    // Act
    optimizedJsonFieldVisitor.visitProgramClass(new NamedClass("Member Name"));

    // Assert
    verify(classVisitor).visitProgramClass(isA(ProgramClass.class));
  }

  /**
   * Test {@link OptimizedJsonFieldVisitor#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then calls {@link MethodImplementationFilter#visitProgramField(ProgramClass,
   *       ProgramField)}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizedJsonFieldVisitor#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); then calls visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizedJsonFieldVisitor.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField_thenCallsVisitProgramField() {
    // Arrange
    MethodImplementationFilter memberVisitor = mock(MethodImplementationFilter.class);
    doNothing()
        .when(memberVisitor)
        .visitProgramField(Mockito.<ProgramClass>any(), Mockito.<ProgramField>any());
    OptimizedJsonFieldVisitor optimizedJsonFieldVisitor =
        new OptimizedJsonFieldVisitor(mock(ClassVisitor.class), memberVisitor);
    ProgramClass programClass = new ProgramClass();

    // Act
    optimizedJsonFieldVisitor.visitProgramField(programClass, new ProgramField());

    // Assert
    verify(memberVisitor).visitProgramField(isA(ProgramClass.class), isA(ProgramField.class));
  }
}
