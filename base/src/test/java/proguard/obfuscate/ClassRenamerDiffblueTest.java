package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
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
import proguard.classfile.LibraryField;
import proguard.classfile.LibraryMember;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMember;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.AllFieldVisitor;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.ImplementedClassFilter;
import proguard.classfile.visitor.MemberVisitor;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.util.SimpleProcessable;

class ClassRenamerDiffblueTest {
  /**
   * Test {@link ClassRenamer#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassRenamer#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitAnyClass(Clazz); when LibraryClass(); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitAnyClass(Clazz)"})
  void testVisitAnyClass_whenLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    ClassRenamer classRenamer = new ClassRenamer();

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class, () -> classRenamer.visitAnyClass(new LibraryClass()));
  }

  /**
   * Test {@link ClassRenamer#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassConstant#accept(Clazz, ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassRenamer#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then calls accept(Clazz, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsAccept() {
    // Arrange
    ClassRenamer classRenamer = new ClassRenamer();

    ClassConstant classConstant = mock(ClassConstant.class);
    doNothing().when(classConstant).accept(Mockito.<Clazz>any(), Mockito.<ConstantVisitor>any());
    ProgramClass programClass =
        new ProgramClass(1, 3, new Constant[] {new ClassConstant(), classConstant}, 1, 1, 1);
    programClass.u2fieldsCount = 0;
    programClass.u2methodsCount = 0;

    // Act
    classRenamer.visitProgramClass(programClass);

    // Assert
    verify(classConstant).accept(isA(Clazz.class), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link ClassRenamer#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#fieldsAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassRenamer#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass); then calls fieldsAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsFieldsAccept() {
    // Arrange
    ClassRenamer classRenamer = new ClassRenamer();

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).fieldsAccept(Mockito.<MemberVisitor>any());
    doNothing().when(programClass).methodsAccept(Mockito.<MemberVisitor>any());
    doNothing().when(programClass).thisClassConstantAccept(Mockito.<ConstantVisitor>any());

    // Act
    classRenamer.visitProgramClass(programClass);

    // Assert
    verify(programClass).fieldsAccept(isA(MemberVisitor.class));
    verify(programClass).methodsAccept(isA(MemberVisitor.class));
    verify(programClass).thisClassConstantAccept(isA(ConstantVisitor.class));
  }

  /**
   * Test {@link ClassRenamer#visitLibraryClass(LibraryClass)}.
   *
   * <p>Method under test: {@link ClassRenamer#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass() {
    // Arrange
    ClassVisitor extraClassVisitor = mock(ClassVisitor.class);
    ClassRenamer classRenamer = new ClassRenamer(extraClassVisitor, new KotlinAnnotationCounter());

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo(null);

    // Act
    classRenamer.visitLibraryClass(libraryClass);

    // Assert that nothing has changed
    assertEquals("This Class Name", libraryClass.getName());
  }

  /**
   * Test {@link ClassRenamer#visitLibraryClass(LibraryClass)}.
   *
   * <p>Method under test: {@link ClassRenamer#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass2() {
    // Arrange
    ClassRenamer classRenamer = new ClassRenamer(null, new KotlinAnnotationCounter());

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo("Library Class");

    // Act
    classRenamer.visitLibraryClass(libraryClass);

    // Assert
    assertEquals("Library Class", libraryClass.getName());
  }

  /**
   * Test {@link ClassRenamer#visitLibraryClass(LibraryClass)}.
   *
   * <p>Method under test: {@link ClassRenamer#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass3() {
    // Arrange
    ClassVisitor extraClassVisitor = mock(ClassVisitor.class);
    doNothing().when(extraClassVisitor).visitLibraryClass(Mockito.<LibraryClass>any());
    ClassRenamer classRenamer = new ClassRenamer(extraClassVisitor, new KotlinAnnotationCounter());

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo("Library Class");

    // Act
    classRenamer.visitLibraryClass(libraryClass);

    // Assert
    verify(extraClassVisitor).visitLibraryClass(isA(LibraryClass.class));
    assertEquals("Library Class", libraryClass.getName());
  }

  /**
   * Test {@link ClassRenamer#visitLibraryClass(LibraryClass)}.
   *
   * <p>Method under test: {@link ClassRenamer#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass4() {
    // Arrange
    ClassVisitor extraClassVisitor = mock(ClassVisitor.class);
    ClassRenamer classRenamer = new ClassRenamer(extraClassVisitor, new KotlinAnnotationCounter());

    LibraryClass libraryClass = new LibraryClass(1, "Library Class", "Super Class Name");
    libraryClass.setProcessingInfo("Library Class");

    // Act
    classRenamer.visitLibraryClass(libraryClass);

    // Assert that nothing has changed
    assertEquals("Library Class", libraryClass.getName());
  }

  /**
   * Test {@link ClassRenamer#visitLibraryClass(LibraryClass)}.
   *
   * <p>Method under test: {@link ClassRenamer#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass5() {
    // Arrange
    ClassVisitor rejectedClassVisistor = mock(ClassVisitor.class);
    doNothing().when(rejectedClassVisistor).visitLibraryClass(Mockito.<LibraryClass>any());
    ImplementedClassFilter extraClassVisitor =
        new ImplementedClassFilter(
            new LibraryClass(), true, mock(ClassVisitor.class), rejectedClassVisistor);
    ClassRenamer classRenamer = new ClassRenamer(extraClassVisitor, new KotlinAnnotationCounter());

    LibraryClass libraryClass = new LibraryClass(1, null, "Super Class Name");
    libraryClass.setProcessingInfo("Library Class");

    // Act
    classRenamer.visitLibraryClass(libraryClass);

    // Assert
    verify(rejectedClassVisistor).visitLibraryClass(isA(LibraryClass.class));
    assertEquals("Library Class", libraryClass.getName());
  }

  /**
   * Test {@link ClassRenamer#visitLibraryClass(LibraryClass)}.
   *
   * <p>Method under test: {@link ClassRenamer#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass6() {
    // Arrange
    AllFieldVisitor extraClassVisitor = new AllFieldVisitor(new KotlinAnnotationCounter());
    ClassRenamer classRenamer = new ClassRenamer(extraClassVisitor, new KotlinAnnotationCounter());

    LibraryClass libraryClass = new LibraryClass(1, null, "Super Class Name");
    libraryClass.setProcessingInfo("Library Class");

    // Act
    classRenamer.visitLibraryClass(libraryClass);

    // Assert
    assertEquals("Library Class", libraryClass.getName());
  }

  /**
   * Test {@link ClassRenamer#visitLibraryClass(LibraryClass)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link ClassRenamer#visitLibraryClass(LibraryClass)}
   */
  @Test
  @DisplayName("Test visitLibraryClass(LibraryClass); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitLibraryClass(LibraryClass)"})
  void testVisitLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    ClassVisitor extraClassVisitor = mock(ClassVisitor.class);
    doThrow(new UnsupportedOperationException())
        .when(extraClassVisitor)
        .visitLibraryClass(Mockito.<LibraryClass>any());
    ClassRenamer classRenamer = new ClassRenamer(extraClassVisitor, new KotlinAnnotationCounter());

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo("Library Class");

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class, () -> classRenamer.visitLibraryClass(libraryClass));
    verify(extraClassVisitor).visitLibraryClass(isA(LibraryClass.class));
  }

  /**
   * Test {@link ClassRenamer#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <ul>
   *   <li>Given {@code String}.
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassRenamer#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMember(ProgramClass, ProgramMember); given 'String'; then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember_givenString_thenCallsGetString() {
    // Arrange
    ClassRenamer classRenamer = new ClassRenamer();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    classRenamer.visitProgramMember(programClass, new ProgramField());

    // Assert
    verify(programClass).getString(0);
  }

  /**
   * Test {@link ClassRenamer#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <ul>
   *   <li>When {@link ProgramField#ProgramField()} ProcessingInfo is {@code String}.
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassRenamer#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMember(ProgramClass, ProgramMember); when ProgramField() ProcessingInfo is 'String'; then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember_whenProgramFieldProcessingInfoIsString_thenCallsGetString() {
    // Arrange
    ClassRenamer classRenamer = new ClassRenamer();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    ProgramField programMember = new ProgramField();
    programMember.setProcessingInfo("String");

    // Act
    classRenamer.visitProgramMember(programClass, programMember);

    // Assert
    verify(programClass).getString(0);
  }

  /**
   * Test {@link ClassRenamer#visitLibraryMember(LibraryClass, LibraryMember)}.
   *
   * <p>Method under test: {@link ClassRenamer#visitLibraryMember(LibraryClass, LibraryMember)}
   */
  @Test
  @DisplayName("Test visitLibraryMember(LibraryClass, LibraryMember)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitLibraryMember(LibraryClass, LibraryMember)"})
  void testVisitLibraryMember() {
    // Arrange
    ClassVisitor extraClassVisitor = mock(ClassVisitor.class);
    ClassRenamer classRenamer = new ClassRenamer(extraClassVisitor, new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();

    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    LibraryField libraryField2 = new LibraryField();
    libraryField.setProcessingInfo(libraryField2);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    LibraryField libraryField17 = new LibraryField(1, "Name", "Descriptor");
    libraryField17.setProcessingInfo(libraryField16);

    LibraryField libraryField18 = new LibraryField(1, "Name", "Descriptor");
    libraryField18.setProcessingInfo(libraryField17);

    LibraryField libraryMember = new LibraryField(1, "Name", "Descriptor");
    libraryMember.setProcessingInfo(libraryField18);

    // Act
    classRenamer.visitLibraryMember(libraryClass, libraryMember);

    // Assert
    assertSame(libraryField2, libraryMember.getProcessingInfo());
  }

  /**
   * Test {@link ClassRenamer#visitLibraryMember(LibraryClass, LibraryMember)}.
   *
   * <p>Method under test: {@link ClassRenamer#visitLibraryMember(LibraryClass, LibraryMember)}
   */
  @Test
  @DisplayName("Test visitLibraryMember(LibraryClass, LibraryMember)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitLibraryMember(LibraryClass, LibraryMember)"})
  void testVisitLibraryMember2() {
    // Arrange
    ClassVisitor extraClassVisitor = mock(ClassVisitor.class);
    ClassRenamer classRenamer = new ClassRenamer(extraClassVisitor, new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();

    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    libraryField.setProcessingInfo(new LibraryField());

    LibraryField libraryField2 = new LibraryField(1, "Name", "Descriptor");
    libraryField2.setProcessingInfo(libraryField);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField2);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    SimpleProcessable simpleProcessable = new SimpleProcessable();
    simpleProcessable.setProcessingInfo(libraryField16);

    LibraryField libraryMember = new LibraryField(1, "Name", "Descriptor");
    libraryMember.setProcessingInfo(simpleProcessable);

    // Act
    classRenamer.visitLibraryMember(libraryClass, libraryMember);

    // Assert that nothing has changed
    assertEquals("Name", libraryMember.name);
    assertSame(simpleProcessable, libraryMember.getProcessingInfo());
  }

  /**
   * Test {@link ClassRenamer#visitLibraryMember(LibraryClass, LibraryMember)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   *   <li>Then {@link LibraryField#LibraryField()} {@link LibraryMember#name} is {@code Processing
   *       Info}.
   * </ul>
   *
   * <p>Method under test: {@link ClassRenamer#visitLibraryMember(LibraryClass, LibraryMember)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMember(LibraryClass, LibraryMember); given 'Processing Info'; then LibraryField() name is 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitLibraryMember(LibraryClass, LibraryMember)"})
  void testVisitLibraryMember_givenProcessingInfo_thenLibraryFieldNameIsProcessingInfo() {
    // Arrange
    ClassRenamer classRenamer = new ClassRenamer();
    LibraryClass libraryClass = new LibraryClass();

    LibraryField libraryMember = new LibraryField();
    libraryMember.setProcessingInfo("Processing Info");

    // Act
    classRenamer.visitLibraryMember(libraryClass, libraryMember);

    // Assert
    assertEquals("Processing Info", libraryMember.name);
    assertEquals("Processing Info", libraryMember.getProcessingInfo());
  }

  /**
   * Test {@link ClassRenamer#visitLibraryMember(LibraryClass, LibraryMember)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   *   <li>Then {@link LibraryField#LibraryField()} {@link LibraryMember#name} is {@code Processing
   *       Info}.
   * </ul>
   *
   * <p>Method under test: {@link ClassRenamer#visitLibraryMember(LibraryClass, LibraryMember)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMember(LibraryClass, LibraryMember); given 'Processing Info'; then LibraryField() name is 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitLibraryMember(LibraryClass, LibraryMember)"})
  void testVisitLibraryMember_givenProcessingInfo_thenLibraryFieldNameIsProcessingInfo2() {
    // Arrange
    ClassVisitor extraClassVisitor = mock(ClassVisitor.class);
    ClassRenamer classRenamer = new ClassRenamer(extraClassVisitor, new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();

    LibraryField libraryMember = new LibraryField();
    libraryMember.setProcessingInfo("Processing Info");

    // Act
    classRenamer.visitLibraryMember(libraryClass, libraryMember);

    // Assert
    assertEquals("Processing Info", libraryMember.name);
    assertEquals("Processing Info", libraryMember.getProcessingInfo());
  }

  /**
   * Test {@link ClassRenamer#visitLibraryMember(LibraryClass, LibraryMember)}.
   *
   * <ul>
   *   <li>When {@link LibraryField#LibraryField()}.
   *   <li>Then {@link LibraryField#LibraryField()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ClassRenamer#visitLibraryMember(LibraryClass, LibraryMember)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMember(LibraryClass, LibraryMember); when LibraryField(); then LibraryField() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitLibraryMember(LibraryClass, LibraryMember)"})
  void testVisitLibraryMember_whenLibraryField_thenLibraryFieldProcessingInfoIsNull() {
    // Arrange
    ClassRenamer classRenamer = new ClassRenamer();
    LibraryClass libraryClass = new LibraryClass();
    LibraryField libraryMember = new LibraryField();

    // Act
    classRenamer.visitLibraryMember(libraryClass, libraryMember);

    // Assert that nothing has changed
    assertNull(libraryMember.getProcessingInfo());
    assertNull(libraryMember.name);
  }

  /**
   * Test {@link ClassRenamer#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Given {@code Name}.
   *   <li>Then calls {@link LibraryClass#addSubClass(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link ClassRenamer#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); given 'Name'; then calls addSubClass(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ClassRenamer.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_givenName_thenCallsAddSubClass() {
    // Arrange
    ClassRenamer classRenamer = new ClassRenamer();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getProcessingInfo()).thenReturn("Name");
    when(clazz.getName()).thenReturn("Name");
    doNothing().when(clazz).addSubClass(Mockito.<Clazz>any());
    doNothing().when(clazz).setProcessingInfo(Mockito.<Object>any());
    clazz.addSubClass(new LibraryClass());
    clazz.setProcessingInfo("Clazz");

    // Act
    classRenamer.visitClassConstant(clazz, new ClassConstant());

    // Assert
    verify(clazz).addSubClass(isA(Clazz.class));
    verify(clazz).getName();
    verify(clazz).getProcessingInfo();
    verify(clazz).setProcessingInfo(isA(Object.class));
  }
}
