package proguard.optimize.evaluation;

import static org.mockito.ArgumentMatchers.eq;
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
import proguard.classfile.ProgramClass;
import proguard.classfile.visitor.MemberVisitor;

class SimpleEnumClassSimplifierDiffblueTest {
  /**
   * Test {@link SimpleEnumClassSimplifier#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@code null}.
   *   <li>Then calls {@link ProgramClass#findMethod(String, String)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumClassSimplifier#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given 'null'; then calls findMethod(String, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SimpleEnumClassSimplifier.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenNull_thenCallsFindMethod() {
    // Arrange
    SimpleEnumClassSimplifier simpleEnumClassSimplifier = new SimpleEnumClassSimplifier();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");
    when(programClass.findMethod(Mockito.<String>any(), Mockito.<String>any())).thenReturn(null);
    doNothing()
        .when(programClass)
        .methodAccept(Mockito.<String>any(), Mockito.<String>any(), Mockito.<MemberVisitor>any());

    // Act
    simpleEnumClassSimplifier.visitProgramClass(programClass);

    // Assert
    verify(programClass).findMethod("valueOf", null);
    verify(programClass).getName();
    verify(programClass).methodAccept(eq("<clinit>"), eq("()V"), isA(MemberVisitor.class));
  }
}
