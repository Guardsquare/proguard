package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
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
import proguard.classfile.LibraryMember;
import proguard.classfile.LibraryMethod;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.visitor.MemberVisitor;
import proguard.fixer.kotlin.KotlinAnnotationCounter;
import proguard.util.SimpleProcessable;

class NewMemberNameFilterDiffblueTest {
  /**
   * Test {@link NewMemberNameFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter = new NewMemberNameFilter(new MemberNameCleaner());
    ProgramClass programClass = new ProgramClass();

    ProgramField programField = new ProgramField();
    programField.setProcessingInfo("Processing Info");

    // Act
    newMemberNameFilter.visitProgramField(programClass, programField);

    // Assert
    assertNull(programField.getProcessingInfo());
  }

  /**
   * Test {@link NewMemberNameFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then {@link ProgramField#ProgramField()} ProcessingInfo is {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); then ProgramField() ProcessingInfo is 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_thenProgramFieldProcessingInfoIsProcessingInfo() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter =
        new NewMemberNameFilter(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();

    ProgramField programField = new ProgramField();
    programField.setProcessingInfo("Processing Info");

    // Act
    newMemberNameFilter.visitProgramField(programClass, programField);

    // Assert that nothing has changed
    assertEquals("Processing Info", programField.getProcessingInfo());
  }

  /**
   * Test {@link NewMemberNameFilter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>When {@link ProgramField#ProgramField()}.
   *   <li>Then {@link ProgramField#ProgramField()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); when ProgramField(); then ProgramField() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_whenProgramField_thenProgramFieldProcessingInfoIsNull() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter =
        new NewMemberNameFilter(new KotlinAnnotationCounter());
    ProgramClass programClass = new ProgramClass();
    ProgramField programField = new ProgramField();

    // Act
    newMemberNameFilter.visitProgramField(programClass, programField);

    // Assert that nothing has changed
    assertNull(programField.getProcessingInfo());
  }

  /**
   * Test {@link NewMemberNameFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@code String}.
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given 'String'; then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_givenString_thenCallsGetString() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter =
        new NewMemberNameFilter(new KotlinAnnotationCounter());

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");
    when(programClass.getProcessingInfo()).thenReturn("Processing Info");

    // Act
    newMemberNameFilter.visitProgramMethod(programClass, new ProgramMethod());

    // Assert
    verify(programClass).getString(0);
    verify(programClass).getProcessingInfo();
  }

  /**
   * Test {@link NewMemberNameFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethod#setProcessingInfo(Object)}.
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then calls setProcessingInfo(Object)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenCallsSetProcessingInfo() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter = new NewMemberNameFilter(new MemberNameCleaner());
    ProgramClass programClass = mock(ProgramClass.class);

    ProgramMethod programMethod = mock(ProgramMethod.class);
    doNothing().when(programMethod).setProcessingInfo(Mockito.<Object>any());
    when(programMethod.getProcessingInfo()).thenReturn("Processing Info");

    // Act
    newMemberNameFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programMethod, atLeast(1)).getProcessingInfo();
    verify(programMethod).setProcessingInfo(isNull());
  }

  /**
   * Test {@link NewMemberNameFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link MemberVisitor#visitProgramMethod(ProgramClass, ProgramMethod)}.
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then calls visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenCallsVisitProgramMethod() {
    // Arrange
    MemberVisitor memberVisitor = mock(MemberVisitor.class);
    doNothing()
        .when(memberVisitor)
        .visitProgramMethod(Mockito.<ProgramClass>any(), Mockito.<ProgramMethod>any());
    NewMemberNameFilter newMemberNameFilter = new NewMemberNameFilter(memberVisitor);
    ProgramClass programClass = mock(ProgramClass.class);

    ProgramMethod programMethod = mock(ProgramMethod.class);
    when(programMethod.getProcessingInfo()).thenReturn("Processing Info");

    // Act
    newMemberNameFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(memberVisitor).visitProgramMethod(isA(ProgramClass.class), isA(ProgramMethod.class));
    verify(programMethod, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link NewMemberNameFilter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>When {@link ProgramClass}.
   *   <li>Then calls {@link ProgramMethod#accept(Clazz, MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); when ProgramClass; then calls accept(Clazz, MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_whenProgramClass_thenCallsAccept() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter =
        new NewMemberNameFilter(new KotlinAnnotationCounter());
    ProgramClass programClass = mock(ProgramClass.class);

    ProgramMethod programMethod = mock(ProgramMethod.class);
    doNothing().when(programMethod).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
    when(programMethod.getProcessingInfo()).thenReturn("Processing Info");

    // Act
    newMemberNameFilter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programMethod).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(programMethod, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName("Test visitLibraryField(LibraryClass, LibraryField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter =
        new NewMemberNameFilter(new KotlinAnnotationCounter());
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

    LibraryField libraryField19 = new LibraryField(1, "Name", "Descriptor");
    libraryField19.setProcessingInfo(libraryField18);

    // Act
    newMemberNameFilter.visitLibraryField(libraryClass, libraryField19);

    // Assert
    assertSame(libraryField2, libraryField19.getProcessingInfo());
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName("Test visitLibraryField(LibraryClass, LibraryField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField2() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter = new NewMemberNameFilter(new MemberNameCleaner());
    LibraryClass libraryClass = new LibraryClass();

    LibraryField libraryField = new LibraryField();
    libraryField.setProcessingInfo("Processing Info");

    // Act
    newMemberNameFilter.visitLibraryField(libraryClass, libraryField);

    // Assert
    assertNull(libraryField.getProcessingInfo());
    assertNull(libraryField.name);
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName("Test visitLibraryField(LibraryClass, LibraryField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField3() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter =
        new NewMemberNameFilter(new KotlinAnnotationCounter());
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

    LibraryField libraryField17 = new LibraryField(1, "Name", "Descriptor");
    libraryField17.setProcessingInfo(simpleProcessable);

    // Act
    newMemberNameFilter.visitLibraryField(libraryClass, libraryField17);

    // Assert that nothing has changed
    assertEquals("Name", libraryField17.name);
    assertSame(simpleProcessable, libraryField17.getProcessingInfo());
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <ul>
   *   <li>Given {@link NewMemberNameFilter#NewMemberNameFilter(MemberVisitor)} with memberVisitor
   *       is {@link NameMarker} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryField(LibraryClass, LibraryField); given NewMemberNameFilter(MemberVisitor) with memberVisitor is NameMarker (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField_givenNewMemberNameFilterWithMemberVisitorIsNameMarker() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter = new NewMemberNameFilter(new NameMarker());
    LibraryClass libraryClass = new LibraryClass();

    LibraryField libraryField = new LibraryField();
    libraryField.setProcessingInfo("Processing Info");

    // Act
    newMemberNameFilter.visitLibraryField(libraryClass, libraryField);

    // Assert
    assertNull(libraryField.getProcessingInfo());
    assertNull(libraryField.name);
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <ul>
   *   <li>Then {@link LibraryField#LibraryField()} {@link LibraryMember#name} is {@code Processing
   *       Info}.
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryField(LibraryClass, LibraryField); then LibraryField() name is 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField_thenLibraryFieldNameIsProcessingInfo() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter = new NewMemberNameFilter(new ClassRenamer());
    LibraryClass libraryClass = new LibraryClass();

    LibraryField libraryField = new LibraryField();
    libraryField.setProcessingInfo("Processing Info");

    // Act
    newMemberNameFilter.visitLibraryField(libraryClass, libraryField);

    // Assert
    assertEquals("Processing Info", libraryField.name);
    assertEquals("Processing Info", libraryField.getProcessingInfo());
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <ul>
   *   <li>Then {@link LibraryField#LibraryField()} ProcessingInfo is {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryField(LibraryClass, LibraryField); then LibraryField() ProcessingInfo is 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField_thenLibraryFieldProcessingInfoIsProcessingInfo() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter =
        new NewMemberNameFilter(new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();

    LibraryField libraryField = new LibraryField();
    libraryField.setProcessingInfo("Processing Info");

    // Act
    newMemberNameFilter.visitLibraryField(libraryClass, libraryField);

    // Assert that nothing has changed
    assertEquals("Processing Info", libraryField.getProcessingInfo());
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <ul>
   *   <li>When {@link LibraryField#LibraryField()}.
   *   <li>Then {@link LibraryField#LibraryField()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryField(LibraryClass, LibraryField); when LibraryField(); then LibraryField() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField_whenLibraryField_thenLibraryFieldProcessingInfoIsNull() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter =
        new NewMemberNameFilter(new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();
    LibraryField libraryField = new LibraryField();

    // Act
    newMemberNameFilter.visitLibraryField(libraryClass, libraryField);

    // Assert that nothing has changed
    assertNull(libraryField.getProcessingInfo());
    assertNull(libraryField.name);
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName("Test visitLibraryMethod(LibraryClass, LibraryMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter =
        new NewMemberNameFilter(new KotlinAnnotationCounter());

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo("Library Class");
    LibraryMethod libraryMethod = new LibraryMethod(1, "Name", "Descriptor");

    // Act
    newMemberNameFilter.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert that nothing has changed
    assertEquals("Name", libraryMethod.name);
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName("Test visitLibraryMethod(LibraryClass, LibraryMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod2() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter =
        new NewMemberNameFilter(new KotlinAnnotationCounter());

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo("Library Class");
    LibraryMethod libraryMethod = new LibraryMethod(1, "<init>", "Descriptor");

    // Act
    newMemberNameFilter.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert that nothing has changed
    assertEquals("<init>", libraryMethod.name);
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName("Test visitLibraryMethod(LibraryClass, LibraryMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod3() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter = new NewMemberNameFilter(new MemberNameCleaner());
    LibraryClass libraryClass = new LibraryClass();

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo("Processing Info");

    // Act
    newMemberNameFilter.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    assertNull(libraryMethod.getProcessingInfo());
    assertNull(libraryMethod.name);
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName("Test visitLibraryMethod(LibraryClass, LibraryMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod4() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter = new NewMemberNameFilter(new NameMarker());

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo("Library Class");
    LibraryMethod libraryMethod = new LibraryMethod(1, "<init>", "Descriptor");

    // Act
    newMemberNameFilter.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert that nothing has changed
    assertEquals("<init>", libraryMethod.name);
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Then {@link LibraryMethod#LibraryMethod()} {@link LibraryMember#name} is {@code
   *       Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); then LibraryMethod() name is 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_thenLibraryMethodNameIsProcessingInfo() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter = new NewMemberNameFilter(new ClassRenamer());
    LibraryClass libraryClass = new LibraryClass();

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo("Processing Info");

    // Act
    newMemberNameFilter.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    assertEquals("Processing Info", libraryMethod.name);
    assertEquals("Processing Info", libraryMethod.getProcessingInfo());
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Then {@link LibraryMethod#LibraryMethod()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); then LibraryMethod() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_thenLibraryMethodProcessingInfoIsNull() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter = new NewMemberNameFilter(new NameMarker());
    LibraryClass libraryClass = new LibraryClass();

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo("Processing Info");

    // Act
    newMemberNameFilter.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    assertNull(libraryMethod.getProcessingInfo());
    assertNull(libraryMethod.name);
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Then {@link LibraryMethod#LibraryMethod()} ProcessingInfo is {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); then LibraryMethod() ProcessingInfo is 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_thenLibraryMethodProcessingInfoIsProcessingInfo() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter =
        new NewMemberNameFilter(new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo("Processing Info");

    // Act
    newMemberNameFilter.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert that nothing has changed
    assertEquals("Processing Info", libraryMethod.getProcessingInfo());
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Then {@link LibraryMethod#LibraryMethod()} ProcessingInfo {@link LibraryField}.
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); then LibraryMethod() ProcessingInfo LibraryField")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_thenLibraryMethodProcessingInfoLibraryField() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter = new NewMemberNameFilter(new NameMarker());
    LibraryClass libraryClass = new LibraryClass();

    LibraryMethod libraryMethod = new LibraryMethod();
    LibraryField libraryField = new LibraryField();
    libraryMethod.setProcessingInfo(libraryField);

    // Act
    newMemberNameFilter.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert that nothing has changed
    Object processingInfo = libraryMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof LibraryField);
    assertSame(libraryField, processingInfo);
  }

  /**
   * Test {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>When {@link LibraryMethod#LibraryMethod()}.
   *   <li>Then {@link LibraryMethod#LibraryMethod()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link NewMemberNameFilter#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); when LibraryMethod(); then LibraryMethod() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NewMemberNameFilter.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_whenLibraryMethod_thenLibraryMethodProcessingInfoIsNull() {
    // Arrange
    NewMemberNameFilter newMemberNameFilter =
        new NewMemberNameFilter(new KotlinAnnotationCounter());
    LibraryClass libraryClass = new LibraryClass();
    LibraryMethod libraryMethod = new LibraryMethod();

    // Act
    newMemberNameFilter.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert that nothing has changed
    assertNull(libraryMethod.getProcessingInfo());
    assertNull(libraryMethod.name);
  }
}
