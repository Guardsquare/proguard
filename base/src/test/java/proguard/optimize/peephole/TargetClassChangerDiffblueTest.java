package proguard.optimize.peephole;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
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
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableTableAttribute;
import proguard.classfile.attribute.LocalVariableTypeTableAttribute;
import proguard.classfile.attribute.RecordComponentInfo;
import proguard.classfile.attribute.SignatureAttribute;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.AnnotationDefaultAttribute;
import proguard.classfile.attribute.annotation.AnnotationElementValue;
import proguard.classfile.attribute.annotation.AnnotationsAttribute;
import proguard.classfile.attribute.annotation.ArrayElementValue;
import proguard.classfile.attribute.annotation.ParameterAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeInvisibleAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeInvisibleParameterAnnotationsAttribute;
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.attribute.visitor.LocalVariableInfoVisitor;
import proguard.classfile.attribute.visitor.LocalVariableTypeInfoVisitor;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.constant.StringConstant;
import proguard.optimize.info.ClassOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfo;
import proguard.testutils.cpa.NamedField;
import proguard.testutils.cpa.NamedMember;

class TargetClassChangerDiffblueTest {
  /**
   * Test {@link TargetClassChanger#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitAnyClass(Clazz); when LibraryClass(); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitAnyClass(Clazz)"})
  void testVisitAnyClass_whenLibraryClass_thenThrowUnsupportedOperationException() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> targetClassChanger.visitAnyClass(new LibraryClass()));
  }

  /**
   * Test {@link TargetClassChanger#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_thenThrowUnsupportedOperationException() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    ProgramClass programClass = new ProgramClass();

    ProgramField programField = mock(ProgramField.class);
    doThrow(new UnsupportedOperationException())
        .when(programField)
        .attributesAccept(Mockito.<ProgramClass>any(), Mockito.<AttributeVisitor>any());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> targetClassChanger.visitProgramField(programClass, programField));
    verify(programField).attributesAccept(isA(ProgramClass.class), isA(AttributeVisitor.class));
  }

  /**
   * Test {@link TargetClassChanger#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>When {@link NamedField#NamedField(String, String)} with {@code Field Name} and {@code
   *       Field Descriptor}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); when NamedField(String, String) with 'Field Name' and 'Field Descriptor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_whenNamedFieldWithFieldNameAndFieldDescriptor() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    ProgramClass programClass = new ProgramClass();

    // Act and Assert
    assertDoesNotThrow(
        () ->
            targetClassChanger.visitProgramField(
                programClass, new NamedField("Field Name", "Field Descriptor")));
  }

  /**
   * Test {@link TargetClassChanger#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>When {@link ProgramField#ProgramField()}.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); when ProgramField(); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_whenProgramField_thenDoesNotThrow() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    ProgramClass programClass = new ProgramClass();

    // Act and Assert
    assertDoesNotThrow(
        () -> targetClassChanger.visitProgramField(programClass, new ProgramField()));
  }

  /**
   * Test {@link TargetClassChanger#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenThrowUnsupportedOperationException() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = mock(ProgramMethod.class);
    doThrow(new UnsupportedOperationException())
        .when(programMethod)
        .attributesAccept(Mockito.<ProgramClass>any(), Mockito.<AttributeVisitor>any());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> targetClassChanger.visitProgramMethod(programClass, programMethod));
    verify(programMethod).attributesAccept(isA(ProgramClass.class), isA(AttributeVisitor.class));
  }

  /**
   * Test {@link TargetClassChanger#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>When {@link NamedMember#NamedMember(String, String)} with {@code Member Name} and {@code
   *       Descriptor}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); when NamedMember(String, String) with 'Member Name' and 'Descriptor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_whenNamedMemberWithMemberNameAndDescriptor() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    ProgramClass programClass = new ProgramClass();

    // Act and Assert
    assertDoesNotThrow(
        () ->
            targetClassChanger.visitProgramMethod(
                programClass, new NamedMember("Member Name", "Descriptor")));
  }

  /**
   * Test {@link TargetClassChanger#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>When {@link ProgramMethod#ProgramMethod()}.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); when ProgramMethod(); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_whenProgramMethod_thenDoesNotThrow() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    ProgramClass programClass = new ProgramClass();

    // Act and Assert
    assertDoesNotThrow(
        () -> targetClassChanger.visitProgramMethod(programClass, new ProgramMethod()));
  }

  /**
   * Test {@link TargetClassChanger#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); given ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_givenClassOptimizationInfo() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(new ClassOptimizationInfo());
    StringConstant stringConstant = new StringConstant(1, referencedClass, new LibraryField());

    // Act and Assert
    assertDoesNotThrow(() -> targetClassChanger.visitStringConstant(clazz, stringConstant));
  }

  /**
   * Test {@link TargetClassChanger#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} {@link ClassOptimizationInfo#getTargetClass()} return
   *       {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); given ClassOptimizationInfo getTargetClass() return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_givenClassOptimizationInfoGetTargetClassReturnNull() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.getTargetClass()).thenReturn(null);

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(classOptimizationInfo);
    StringConstant stringConstant = new StringConstant(1, referencedClass, new LibraryField());

    // Act
    targetClassChanger.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(classOptimizationInfo).getTargetClass();
  }

  /**
   * Test {@link TargetClassChanger#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); given ProgramClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_givenProgramClassOptimizationInfo() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(new ProgramClassOptimizationInfo());
    StringConstant stringConstant = new StringConstant(1, referencedClass, new LibraryField());

    // Act and Assert
    assertDoesNotThrow(() -> targetClassChanger.visitStringConstant(clazz, stringConstant));
  }

  /**
   * Test {@link TargetClassChanger#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenThrowUnsupportedOperationException() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    LibraryClass libraryClass = mock(LibraryClass.class);
    when(libraryClass.getProcessingInfo()).thenThrow(new UnsupportedOperationException());

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.getTargetClass()).thenReturn(libraryClass);

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(classOptimizationInfo);
    StringConstant stringConstant = new StringConstant(1, referencedClass, new LibraryField());

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> targetClassChanger.visitStringConstant(clazz, stringConstant));
    verify(classOptimizationInfo).getTargetClass();
    verify(libraryClass).getProcessingInfo();
  }

  /**
   * Test {@link TargetClassChanger#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>When {@link StringConstant#StringConstant()}.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); when StringConstant(); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_whenStringConstant_thenDoesNotThrow() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    // Act and Assert
    assertDoesNotThrow(() -> targetClassChanger.visitStringConstant(clazz, new StringConstant()));
  }

  /**
   * Test {@link TargetClassChanger#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); given ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_givenClassOptimizationInfo() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(new ClassOptimizationInfo());

    // Act and Assert
    assertDoesNotThrow(
        () ->
            targetClassChanger.visitAnyMethodrefConstant(
                clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, new LibraryMethod())));
  }

  /**
   * Test {@link TargetClassChanger#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} {@link ClassOptimizationInfo#getTargetClass()} return
   *       {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); given ClassOptimizationInfo getTargetClass() return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_givenClassOptimizationInfoGetTargetClassReturnNull() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.getTargetClass()).thenReturn(null);

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(classOptimizationInfo);

    // Act
    targetClassChanger.visitAnyMethodrefConstant(
        clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, new LibraryMethod()));

    // Assert
    verify(classOptimizationInfo).getTargetClass();
  }

  /**
   * Test {@link TargetClassChanger#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); given ProgramClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_givenProgramClassOptimizationInfo() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act and Assert
    assertDoesNotThrow(
        () ->
            targetClassChanger.visitAnyMethodrefConstant(
                clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, new LibraryMethod())));
  }

  /**
   * Test {@link TargetClassChanger#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_thenThrowUnsupportedOperationException() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    LibraryClass libraryClass = mock(LibraryClass.class);
    when(libraryClass.getProcessingInfo()).thenThrow(new UnsupportedOperationException());

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.getTargetClass()).thenReturn(libraryClass);

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(classOptimizationInfo);

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () ->
            targetClassChanger.visitAnyMethodrefConstant(
                clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, new LibraryMethod())));
    verify(classOptimizationInfo).getTargetClass();
    verify(libraryClass).getProcessingInfo();
  }

  /**
   * Test {@link TargetClassChanger#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>When {@link InterfaceMethodrefConstant#InterfaceMethodrefConstant()}.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); when InterfaceMethodrefConstant(); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_whenInterfaceMethodrefConstant_thenDoesNotThrow() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    // Act and Assert
    assertDoesNotThrow(
        () ->
            targetClassChanger.visitAnyMethodrefConstant(clazz, new InterfaceMethodrefConstant()));
  }

  /**
   * Test {@link TargetClassChanger#visitFieldrefConstant(Clazz, FieldrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitFieldrefConstant(Clazz, FieldrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitFieldrefConstant(Clazz, FieldrefConstant); given ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitFieldrefConstant(Clazz, FieldrefConstant)"})
  void testVisitFieldrefConstant_givenClassOptimizationInfo() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(new ClassOptimizationInfo());

    // Act and Assert
    assertDoesNotThrow(
        () ->
            targetClassChanger.visitFieldrefConstant(
                clazz, new FieldrefConstant(1, 1, referencedClass, new LibraryField())));
  }

  /**
   * Test {@link TargetClassChanger#visitFieldrefConstant(Clazz, FieldrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} {@link ClassOptimizationInfo#getTargetClass()} return
   *       {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitFieldrefConstant(Clazz, FieldrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitFieldrefConstant(Clazz, FieldrefConstant); given ClassOptimizationInfo getTargetClass() return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitFieldrefConstant(Clazz, FieldrefConstant)"})
  void testVisitFieldrefConstant_givenClassOptimizationInfoGetTargetClassReturnNull() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.getTargetClass()).thenReturn(null);

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(classOptimizationInfo);

    // Act
    targetClassChanger.visitFieldrefConstant(
        clazz, new FieldrefConstant(1, 1, referencedClass, new LibraryField()));

    // Assert
    verify(classOptimizationInfo).getTargetClass();
  }

  /**
   * Test {@link TargetClassChanger#visitFieldrefConstant(Clazz, FieldrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitFieldrefConstant(Clazz, FieldrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitFieldrefConstant(Clazz, FieldrefConstant); given ProgramClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitFieldrefConstant(Clazz, FieldrefConstant)"})
  void testVisitFieldrefConstant_givenProgramClassOptimizationInfo() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act and Assert
    assertDoesNotThrow(
        () ->
            targetClassChanger.visitFieldrefConstant(
                clazz, new FieldrefConstant(1, 1, referencedClass, new LibraryField())));
  }

  /**
   * Test {@link TargetClassChanger#visitFieldrefConstant(Clazz, FieldrefConstant)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitFieldrefConstant(Clazz, FieldrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitFieldrefConstant(Clazz, FieldrefConstant); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitFieldrefConstant(Clazz, FieldrefConstant)"})
  void testVisitFieldrefConstant_thenThrowUnsupportedOperationException() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    LibraryClass libraryClass = mock(LibraryClass.class);
    when(libraryClass.getProcessingInfo()).thenThrow(new UnsupportedOperationException());

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.getTargetClass()).thenReturn(libraryClass);

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(classOptimizationInfo);

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () ->
            targetClassChanger.visitFieldrefConstant(
                clazz, new FieldrefConstant(1, 1, referencedClass, new LibraryField())));
    verify(classOptimizationInfo).getTargetClass();
    verify(libraryClass).getProcessingInfo();
  }

  /**
   * Test {@link TargetClassChanger#visitFieldrefConstant(Clazz, FieldrefConstant)}.
   *
   * <ul>
   *   <li>When {@link FieldrefConstant#FieldrefConstant()}.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitFieldrefConstant(Clazz, FieldrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitFieldrefConstant(Clazz, FieldrefConstant); when FieldrefConstant(); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitFieldrefConstant(Clazz, FieldrefConstant)"})
  void testVisitFieldrefConstant_whenFieldrefConstant_thenDoesNotThrow() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    // Act and Assert
    assertDoesNotThrow(
        () -> targetClassChanger.visitFieldrefConstant(clazz, new FieldrefConstant()));
  }

  /**
   * Test {@link TargetClassChanger#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); given ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_givenClassOptimizationInfo() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(new ClassOptimizationInfo());
    ClassConstant classConstant = new ClassConstant(1, referencedClass);

    // Act and Assert
    assertDoesNotThrow(() -> targetClassChanger.visitClassConstant(clazz, classConstant));
  }

  /**
   * Test {@link TargetClassChanger#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} {@link ClassOptimizationInfo#getTargetClass()} return
   *       {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); given ClassOptimizationInfo getTargetClass() return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_givenClassOptimizationInfoGetTargetClassReturnNull() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.getTargetClass()).thenReturn(null);

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(classOptimizationInfo);
    ClassConstant classConstant = new ClassConstant(1, referencedClass);

    // Act
    targetClassChanger.visitClassConstant(clazz, classConstant);

    // Assert
    verify(classOptimizationInfo).getTargetClass();
  }

  /**
   * Test {@link TargetClassChanger#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); given ProgramClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_givenProgramClassOptimizationInfo() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(new ProgramClassOptimizationInfo());
    ClassConstant classConstant = new ClassConstant(1, referencedClass);

    // Act and Assert
    assertDoesNotThrow(() -> targetClassChanger.visitClassConstant(clazz, classConstant));
  }

  /**
   * Test {@link TargetClassChanger#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then throw {@link UnsupportedOperationException}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); then throw UnsupportedOperationException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenThrowUnsupportedOperationException() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    LibraryClass libraryClass = mock(LibraryClass.class);
    when(libraryClass.getProcessingInfo()).thenThrow(new UnsupportedOperationException());

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.getTargetClass()).thenReturn(libraryClass);

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(classOptimizationInfo);
    ClassConstant classConstant = new ClassConstant(1, referencedClass);

    // Act and Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> targetClassChanger.visitClassConstant(clazz, classConstant));
    verify(classOptimizationInfo).getTargetClass();
    verify(libraryClass).getProcessingInfo();
  }

  /**
   * Test {@link TargetClassChanger#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>When {@link ClassConstant#ClassConstant()}.
   *   <li>Then does not throw.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); when ClassConstant(); then does not throw")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_whenClassConstant_thenDoesNotThrow() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    // Act and Assert
    assertDoesNotThrow(() -> targetClassChanger.visitClassConstant(clazz, new ClassConstant()));
  }

  /**
   * Test {@link TargetClassChanger#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link CodeAttribute#attributesAccept(Clazz, Method, AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then calls attributesAccept(Clazz, Method, AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenCallsAttributesAccept() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing()
        .when(codeAttribute)
        .attributesAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());

    // Act
    targetClassChanger.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(codeAttribute)
        .attributesAccept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
  }

  /**
   * Test {@link TargetClassChanger#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link LocalVariableTableAttribute#localVariablesAccept(Clazz, Method,
   *       CodeAttribute, LocalVariableInfoVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitLocalVariableTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute); then calls localVariablesAccept(Clazz, Method, CodeAttribute, LocalVariableInfoVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute_thenCallsLocalVariablesAccept() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    LocalVariableTableAttribute localVariableTableAttribute =
        mock(LocalVariableTableAttribute.class);
    doNothing()
        .when(localVariableTableAttribute)
        .localVariablesAccept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            Mockito.<LocalVariableInfoVisitor>any());

    // Act
    targetClassChanger.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert
    verify(localVariableTableAttribute)
        .localVariablesAccept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            isA(LocalVariableInfoVisitor.class));
  }

  /**
   * Test {@link TargetClassChanger#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link LocalVariableTypeTableAttribute#localVariablesAccept(Clazz, Method,
   *       CodeAttribute, LocalVariableTypeInfoVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitLocalVariableTypeTableAttribute(Clazz,
   * Method, CodeAttribute, LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute); then calls localVariablesAccept(Clazz, Method, CodeAttribute, LocalVariableTypeInfoVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute_thenCallsLocalVariablesAccept() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    LocalVariableTypeTableAttribute localVariableTypeTableAttribute =
        mock(LocalVariableTypeTableAttribute.class);
    doNothing()
        .when(localVariableTypeTableAttribute)
        .localVariablesAccept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            Mockito.<LocalVariableTypeInfoVisitor>any());

    // Act
    targetClassChanger.visitLocalVariableTypeTableAttribute(
        clazz, method, codeAttribute, localVariableTypeTableAttribute);

    // Assert
    verify(localVariableTypeTableAttribute)
        .localVariablesAccept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            isA(LocalVariableTypeInfoVisitor.class));
  }

  /**
   * Test {@link TargetClassChanger#visitSignatureAttribute(Clazz, SignatureAttribute)} with {@code
   * clazz}, {@code signatureAttribute}.
   *
   * <ul>
   *   <li>Then calls {@link ClassOptimizationInfo#getTargetClass()}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'; then calls getTargetClass()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitSignatureAttribute(Clazz, SignatureAttribute)"})
  void testVisitSignatureAttributeWithClazzSignatureAttribute_thenCallsGetTargetClass() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.getTargetClass()).thenReturn(null);

    LibraryClass libraryClass = new LibraryClass();
    libraryClass.setProcessingInfo(classOptimizationInfo);
    SignatureAttribute signatureAttribute = new SignatureAttribute(1, 1);
    signatureAttribute.referencedClasses = new Clazz[] {libraryClass};

    // Act
    targetClassChanger.visitSignatureAttribute(clazz, signatureAttribute);

    // Assert
    verify(classOptimizationInfo).getTargetClass();
  }

  /**
   * Test {@link TargetClassChanger#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link RuntimeInvisibleAnnotationsAttribute#annotationsAccept(Clazz,
   *       AnnotationVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitAnyAnnotationsAttribute(Clazz,
   * AnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute); then calls annotationsAccept(Clazz, AnnotationVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)"
  })
  void testVisitAnyAnnotationsAttribute_thenCallsAnnotationsAccept() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    RuntimeInvisibleAnnotationsAttribute annotationsAttribute =
        mock(RuntimeInvisibleAnnotationsAttribute.class);
    doNothing()
        .when(annotationsAttribute)
        .annotationsAccept(Mockito.<Clazz>any(), Mockito.<AnnotationVisitor>any());

    // Act
    targetClassChanger.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

    // Assert
    verify(annotationsAttribute).annotationsAccept(isA(Clazz.class), isA(AnnotationVisitor.class));
  }

  /**
   * Test {@link TargetClassChanger#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link Annotation#elementValuesAccept(Clazz, ElementValueVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitAnyAnnotationsAttribute(Clazz,
   * AnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute); then calls elementValuesAccept(Clazz, ElementValueVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)"
  })
  void testVisitAnyAnnotationsAttribute_thenCallsElementValuesAccept() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    Annotation annotation = mock(Annotation.class);
    doNothing()
        .when(annotation)
        .elementValuesAccept(Mockito.<Clazz>any(), Mockito.<ElementValueVisitor>any());
    Annotation[] annotations = new Annotation[] {annotation};

    // Act
    targetClassChanger.visitAnyAnnotationsAttribute(
        clazz, new RuntimeInvisibleAnnotationsAttribute(1, 1, annotations));

    // Assert
    verify(annotation).elementValuesAccept(isA(Clazz.class), isA(ElementValueVisitor.class));
  }

  /**
   * Test {@link TargetClassChanger#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link RuntimeInvisibleParameterAnnotationsAttribute#annotationsAccept(Clazz,
   *       Method, AnnotationVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitAnyParameterAnnotationsAttribute(Clazz,
   * Method, ParameterAnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute); then calls annotationsAccept(Clazz, Method, AnnotationVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)"
  })
  void testVisitAnyParameterAnnotationsAttribute_thenCallsAnnotationsAccept() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    RuntimeInvisibleParameterAnnotationsAttribute parameterAnnotationsAttribute =
        mock(RuntimeInvisibleParameterAnnotationsAttribute.class);
    doNothing()
        .when(parameterAnnotationsAttribute)
        .annotationsAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AnnotationVisitor>any());

    // Act
    targetClassChanger.visitAnyParameterAnnotationsAttribute(
        clazz, method, parameterAnnotationsAttribute);

    // Assert
    verify(parameterAnnotationsAttribute)
        .annotationsAccept(isA(Clazz.class), isA(Method.class), isA(AnnotationVisitor.class));
  }

  /**
   * Test {@link TargetClassChanger#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link Annotation#elementValuesAccept(Clazz, ElementValueVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitAnyParameterAnnotationsAttribute(Clazz,
   * Method, ParameterAnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute); then calls elementValuesAccept(Clazz, ElementValueVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)"
  })
  void testVisitAnyParameterAnnotationsAttribute_thenCallsElementValuesAccept() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    Annotation annotation = mock(Annotation.class);
    doNothing()
        .when(annotation)
        .elementValuesAccept(Mockito.<Clazz>any(), Mockito.<ElementValueVisitor>any());
    Annotation[][] parameterAnnotations = new Annotation[][] {new Annotation[] {annotation}};

    // Act
    targetClassChanger.visitAnyParameterAnnotationsAttribute(
        clazz,
        method,
        new RuntimeInvisibleParameterAnnotationsAttribute(
            1, 1, new int[] {1, 1, 3, 1}, parameterAnnotations));

    // Assert
    verify(annotation).elementValuesAccept(isA(Clazz.class), isA(ElementValueVisitor.class));
  }

  /**
   * Test {@link TargetClassChanger#visitAnnotationDefaultAttribute(Clazz, Method,
   * AnnotationDefaultAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link AnnotationDefaultAttribute#defaultValueAccept(Clazz,
   *       ElementValueVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitAnnotationDefaultAttribute(Clazz, Method,
   * AnnotationDefaultAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotationDefaultAttribute(Clazz, Method, AnnotationDefaultAttribute); then calls defaultValueAccept(Clazz, ElementValueVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitAnnotationDefaultAttribute(Clazz, Method, AnnotationDefaultAttribute)"
  })
  void testVisitAnnotationDefaultAttribute_thenCallsDefaultValueAccept() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    AnnotationDefaultAttribute annotationDefaultAttribute = mock(AnnotationDefaultAttribute.class);
    doNothing()
        .when(annotationDefaultAttribute)
        .defaultValueAccept(Mockito.<Clazz>any(), Mockito.<ElementValueVisitor>any());

    // Act
    targetClassChanger.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

    // Assert
    verify(annotationDefaultAttribute)
        .defaultValueAccept(isA(Clazz.class), isA(ElementValueVisitor.class));
  }

  /**
   * Test {@link TargetClassChanger#visitRecordComponentInfo(Clazz, RecordComponentInfo)}.
   *
   * <ul>
   *   <li>Then calls {@link AnnotationDefaultAttribute#accept(Clazz, RecordComponentInfo,
   *       AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitRecordComponentInfo(Clazz,
   * RecordComponentInfo)}
   */
  @Test
  @DisplayName(
      "Test visitRecordComponentInfo(Clazz, RecordComponentInfo); then calls accept(Clazz, RecordComponentInfo, AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitRecordComponentInfo(Clazz, RecordComponentInfo)"
  })
  void testVisitRecordComponentInfo_thenCallsAccept() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    AnnotationDefaultAttribute annotationDefaultAttribute = mock(AnnotationDefaultAttribute.class);
    doNothing()
        .when(annotationDefaultAttribute)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<RecordComponentInfo>any(),
            Mockito.<AttributeVisitor>any());
    Attribute[] attributes = new Attribute[] {annotationDefaultAttribute};

    // Act
    targetClassChanger.visitRecordComponentInfo(
        clazz, new RecordComponentInfo(1, 1, 1, attributes));

    // Assert
    verify(annotationDefaultAttribute)
        .accept(isA(Clazz.class), isA(RecordComponentInfo.class), isA(AttributeVisitor.class));
  }

  /**
   * Test {@link TargetClassChanger#visitRecordComponentInfo(Clazz, RecordComponentInfo)}.
   *
   * <ul>
   *   <li>Then calls {@link RecordComponentInfo#attributesAccept(Clazz, AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitRecordComponentInfo(Clazz,
   * RecordComponentInfo)}
   */
  @Test
  @DisplayName(
      "Test visitRecordComponentInfo(Clazz, RecordComponentInfo); then calls attributesAccept(Clazz, AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitRecordComponentInfo(Clazz, RecordComponentInfo)"
  })
  void testVisitRecordComponentInfo_thenCallsAttributesAccept() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    RecordComponentInfo recordComponentInfo = mock(RecordComponentInfo.class);
    doNothing()
        .when(recordComponentInfo)
        .attributesAccept(Mockito.<Clazz>any(), Mockito.<AttributeVisitor>any());

    // Act
    targetClassChanger.visitRecordComponentInfo(clazz, recordComponentInfo);

    // Assert
    verify(recordComponentInfo).attributesAccept(isA(Clazz.class), isA(AttributeVisitor.class));
  }

  /**
   * Test {@link TargetClassChanger#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <ul>
   *   <li>Then calls {@link Annotation#elementValuesAccept(Clazz, ElementValueVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'; then calls elementValuesAccept(Clazz, ElementValueVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TargetClassChanger.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation_thenCallsElementValuesAccept() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();

    Annotation annotation = mock(Annotation.class);
    doNothing()
        .when(annotation)
        .elementValuesAccept(Mockito.<Clazz>any(), Mockito.<ElementValueVisitor>any());

    // Act
    targetClassChanger.visitAnnotation(clazz, annotation);

    // Assert
    verify(annotation).elementValuesAccept(isA(Clazz.class), isA(ElementValueVisitor.class));
  }

  /**
   * Test {@link TargetClassChanger#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}.
   *
   * <ul>
   *   <li>Then calls {@link AnnotationElementValue#annotationAccept(Clazz, AnnotationVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue); then calls annotationAccept(Clazz, AnnotationVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)"
  })
  void testVisitAnnotationElementValue_thenCallsAnnotationAccept() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();

    AnnotationElementValue annotationElementValue = mock(AnnotationElementValue.class);
    doNothing()
        .when(annotationElementValue)
        .annotationAccept(Mockito.<Clazz>any(), Mockito.<AnnotationVisitor>any());

    // Act
    targetClassChanger.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

    // Assert
    verify(annotationElementValue).annotationAccept(isA(Clazz.class), isA(AnnotationVisitor.class));
  }

  /**
   * Test {@link TargetClassChanger#visitArrayElementValue(Clazz, Annotation, ArrayElementValue)}.
   *
   * <ul>
   *   <li>Then calls {@link ArrayElementValue#elementValuesAccept(Clazz, Annotation,
   *       ElementValueVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link TargetClassChanger#visitArrayElementValue(Clazz, Annotation,
   * ArrayElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitArrayElementValue(Clazz, Annotation, ArrayElementValue); then calls elementValuesAccept(Clazz, Annotation, ElementValueVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TargetClassChanger.visitArrayElementValue(Clazz, Annotation, ArrayElementValue)"
  })
  void testVisitArrayElementValue_thenCallsElementValuesAccept() {
    // Arrange
    TargetClassChanger targetClassChanger = new TargetClassChanger();
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();

    ArrayElementValue arrayElementValue = mock(ArrayElementValue.class);
    doNothing()
        .when(arrayElementValue)
        .elementValuesAccept(
            Mockito.<Clazz>any(), Mockito.<Annotation>any(), Mockito.<ElementValueVisitor>any());

    // Act
    targetClassChanger.visitArrayElementValue(clazz, annotation, arrayElementValue);

    // Assert
    verify(arrayElementValue)
        .elementValuesAccept(
            isA(Clazz.class), isA(Annotation.class), isA(ElementValueVisitor.class));
  }
}
