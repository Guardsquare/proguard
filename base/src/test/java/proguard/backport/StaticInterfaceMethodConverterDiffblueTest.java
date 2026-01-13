package proguard.backport;

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
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.visitor.ClassVisitor;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.io.ExtraDataEntryNameMap;

class StaticInterfaceMethodConverterDiffblueTest {
  /**
   * Test {@link StaticInterfaceMethodConverter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>When {@link ProgramClass} {@link ProgramClass#accept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link ProgramClass#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link StaticInterfaceMethodConverter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); when ProgramClass accept(ClassVisitor) does nothing; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void StaticInterfaceMethodConverter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_whenProgramClassAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

    StaticInterfaceMethodConverter staticInterfaceMethodConverter =
        new StaticInterfaceMethodConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            extraDataEntryNameMap,
            modifiedClassVisitor,
            new KotlinAnnotationCounter());

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).accept(Mockito.<ClassVisitor>any());

    // Act
    staticInterfaceMethodConverter.visitProgramClass(programClass);

    // Assert
    verify(programClass).accept(isA(ClassVisitor.class));
  }
}
