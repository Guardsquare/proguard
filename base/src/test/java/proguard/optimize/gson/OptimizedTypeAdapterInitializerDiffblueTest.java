package proguard.optimize.gson;

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
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.testutils.cpa.NamedClass;

class OptimizedTypeAdapterInitializerDiffblueTest {
  /**
   * Test {@link OptimizedTypeAdapterInitializer#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link NamedClass#NamedClass(String)} with {@code Member Name}.
   *   <li>Then calls {@link ProgramClass#methodsAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizedTypeAdapterInitializer#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given NamedClass(String) with 'Member Name'; then calls methodsAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizedTypeAdapterInitializer.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenNamedClassWithMemberName_thenCallsMethodsAccept() {
    // Arrange
    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    NamedClass objectProgramClass = new NamedClass("Member Name");
    CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
    OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();

    OptimizedTypeAdapterInitializer optimizedTypeAdapterInitializer =
        new OptimizedTypeAdapterInitializer(
            "Type Adapter Class Name",
            objectProgramClass,
            codeAttributeEditor,
            serializationInfo,
            new OptimizedJsonInfo(),
            KotlinConstants.dummyClassPool,
            classVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).methodsAccept(Mockito.<MemberVisitor>any());
    doNothing().when(programClass).thisClassConstantAccept(Mockito.<ConstantVisitor>any());

    // Act
    optimizedTypeAdapterInitializer.visitProgramClass(programClass);

    // Assert
    verify(programClass).methodsAccept(isA(MemberVisitor.class));
    verify(programClass).thisClassConstantAccept(isA(ConstantVisitor.class));
    verify(classVisitor).visitProgramClass(isA(ProgramClass.class));
  }

  /**
   * Test {@link OptimizedTypeAdapterInitializer#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClass} {@link ProgramClass#getAccessFlags()} return {@code 16384}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizedTypeAdapterInitializer#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ProgramClass getAccessFlags() return '16384'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizedTypeAdapterInitializer.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenProgramClassGetAccessFlagsReturn16384() {
    // Arrange
    ProgramClass objectProgramClass = mock(ProgramClass.class);
    when(objectProgramClass.getAccessFlags()).thenReturn(16384);
    when(objectProgramClass.getName()).thenReturn("Name");

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
    OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();

    OptimizedTypeAdapterInitializer optimizedTypeAdapterInitializer =
        new OptimizedTypeAdapterInitializer(
            "Type Adapter Class Name",
            objectProgramClass,
            codeAttributeEditor,
            serializationInfo,
            new OptimizedJsonInfo(),
            KotlinConstants.dummyClassPool,
            classVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).methodsAccept(Mockito.<MemberVisitor>any());
    doNothing().when(programClass).thisClassConstantAccept(Mockito.<ConstantVisitor>any());

    // Act
    optimizedTypeAdapterInitializer.visitProgramClass(programClass);

    // Assert
    verify(objectProgramClass).getAccessFlags();
    verify(objectProgramClass).getName();
    verify(programClass).methodsAccept(isA(MemberVisitor.class));
    verify(programClass).thisClassConstantAccept(isA(ConstantVisitor.class));
    verify(classVisitor).visitProgramClass(isA(ProgramClass.class));
  }

  /**
   * Test {@link OptimizedTypeAdapterInitializer#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#getAccessFlags()}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizedTypeAdapterInitializer#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then calls getAccessFlags()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizedTypeAdapterInitializer.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsGetAccessFlags() {
    // Arrange
    ProgramClass objectProgramClass = mock(ProgramClass.class);
    when(objectProgramClass.getAccessFlags()).thenReturn(1);
    when(objectProgramClass.getName()).thenReturn("Name");

    ClassVisitor classVisitor = mock(ClassVisitor.class);
    doNothing().when(classVisitor).visitProgramClass(Mockito.<ProgramClass>any());
    CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
    OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();

    OptimizedTypeAdapterInitializer optimizedTypeAdapterInitializer =
        new OptimizedTypeAdapterInitializer(
            "Type Adapter Class Name",
            objectProgramClass,
            codeAttributeEditor,
            serializationInfo,
            new OptimizedJsonInfo(),
            KotlinConstants.dummyClassPool,
            classVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).methodsAccept(Mockito.<MemberVisitor>any());
    doNothing().when(programClass).thisClassConstantAccept(Mockito.<ConstantVisitor>any());

    // Act
    optimizedTypeAdapterInitializer.visitProgramClass(programClass);

    // Assert
    verify(objectProgramClass).getAccessFlags();
    verify(objectProgramClass).getName();
    verify(programClass).methodsAccept(isA(MemberVisitor.class));
    verify(programClass).thisClassConstantAccept(isA(ConstantVisitor.class));
    verify(classVisitor).visitProgramClass(isA(ProgramClass.class));
  }
}
