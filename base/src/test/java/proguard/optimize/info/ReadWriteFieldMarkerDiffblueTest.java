package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
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
import proguard.classfile.Field;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.visitor.MemberVisitor;

class ReadWriteFieldMarkerDiffblueTest {
  /**
   * Test {@link ReadWriteFieldMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link StringConstant#referencedMemberAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ReadWriteFieldMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); then calls referencedMemberAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReadWriteFieldMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenCallsReferencedMemberAccept() {
    // Arrange
    ReadWriteFieldMarker readWriteFieldMarker = new ReadWriteFieldMarker(new MutableBoolean());
    LibraryClass clazz = new LibraryClass();

    StringConstant stringConstant = mock(StringConstant.class);
    doNothing().when(stringConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());

    // Act
    readWriteFieldMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(stringConstant).referencedMemberAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link ReadWriteFieldMarker#visitFieldrefConstant(Clazz, FieldrefConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link FieldrefConstant#referencedFieldAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ReadWriteFieldMarker#visitFieldrefConstant(Clazz,
   * FieldrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitFieldrefConstant(Clazz, FieldrefConstant); then calls referencedFieldAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReadWriteFieldMarker.visitFieldrefConstant(Clazz, FieldrefConstant)"})
  void testVisitFieldrefConstant_thenCallsReferencedFieldAccept() {
    // Arrange
    ReadWriteFieldMarker readWriteFieldMarker = new ReadWriteFieldMarker(new MutableBoolean());
    LibraryClass clazz = new LibraryClass();

    FieldrefConstant fieldrefConstant = mock(FieldrefConstant.class);
    doNothing().when(fieldrefConstant).referencedFieldAccept(Mockito.<MemberVisitor>any());

    // Act
    readWriteFieldMarker.visitFieldrefConstant(clazz, fieldrefConstant);

    // Assert
    verify(fieldrefConstant).referencedFieldAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link ReadWriteFieldMarker#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link ReadWriteFieldMarker#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReadWriteFieldMarker.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField() {
    // Arrange
    ReadWriteFieldMarker readWriteFieldMarker =
        new ReadWriteFieldMarker(new MutableBoolean(), true, true);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        mock(ProgramFieldOptimizationInfo.class);
    doNothing().when(programFieldOptimizationInfo).setRead();
    doNothing().when(programFieldOptimizationInfo).setWritten();
    when(programFieldOptimizationInfo.isWritten()).thenReturn(false);
    when(programFieldOptimizationInfo.isRead()).thenReturn(false);

    ProgramField programField = mock(ProgramField.class);
    when(programField.getName(Mockito.<Clazz>any())).thenReturn("Name");
    when(programField.getProcessingInfo()).thenReturn(programFieldOptimizationInfo);
    doNothing().when(programField).setProcessingInfo(Mockito.<Object>any());
    programField.setProcessingInfo(mock(FieldOptimizationInfo.class));

    // Act
    readWriteFieldMarker.visitProgramField(programClass, programField);

    // Assert
    verify(programClass, atLeast(1)).getName();
    verify(programField, atLeast(1)).getName(isA(Clazz.class));
    verify(programFieldOptimizationInfo).isRead();
    verify(programFieldOptimizationInfo).isWritten();
    verify(programFieldOptimizationInfo).setRead();
    verify(programFieldOptimizationInfo).setWritten();
    verify(programField, atLeast(1)).getProcessingInfo();
    verify(programField).setProcessingInfo(isA(Object.class));
  }

  /**
   * Test {@link ReadWriteFieldMarker#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link ReadWriteFieldMarker#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReadWriteFieldMarker.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField2() {
    // Arrange
    ReadWriteFieldMarker readWriteFieldMarker =
        new ReadWriteFieldMarker(new MutableBoolean(), false, true);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        mock(ProgramFieldOptimizationInfo.class);
    doNothing().when(programFieldOptimizationInfo).setWritten();
    when(programFieldOptimizationInfo.isWritten()).thenReturn(false);

    ProgramField programField = mock(ProgramField.class);
    when(programField.getName(Mockito.<Clazz>any())).thenReturn("Name");
    when(programField.getProcessingInfo()).thenReturn(programFieldOptimizationInfo);
    doNothing().when(programField).setProcessingInfo(Mockito.<Object>any());
    programField.setProcessingInfo(mock(FieldOptimizationInfo.class));

    // Act
    readWriteFieldMarker.visitProgramField(programClass, programField);

    // Assert
    verify(programClass).getName();
    verify(programField).getName(isA(Clazz.class));
    verify(programFieldOptimizationInfo).isWritten();
    verify(programFieldOptimizationInfo).setWritten();
    verify(programField).getProcessingInfo();
    verify(programField).setProcessingInfo(isA(Object.class));
  }

  /**
   * Test {@link ReadWriteFieldMarker#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link ReadWriteFieldMarker#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReadWriteFieldMarker.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField3() {
    // Arrange
    ReadWriteFieldMarker readWriteFieldMarker =
        new ReadWriteFieldMarker(new MutableBoolean(), true, false);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        mock(ProgramFieldOptimizationInfo.class);
    doNothing().when(programFieldOptimizationInfo).setRead();
    when(programFieldOptimizationInfo.isRead()).thenReturn(false);

    ProgramField programField = mock(ProgramField.class);
    when(programField.getName(Mockito.<Clazz>any())).thenReturn("Name");
    when(programField.getProcessingInfo()).thenReturn(programFieldOptimizationInfo);
    doNothing().when(programField).setProcessingInfo(Mockito.<Object>any());
    programField.setProcessingInfo(mock(FieldOptimizationInfo.class));

    // Act
    readWriteFieldMarker.visitProgramField(programClass, programField);

    // Assert
    verify(programClass).getName();
    verify(programField).getName(isA(Clazz.class));
    verify(programFieldOptimizationInfo).isRead();
    verify(programFieldOptimizationInfo).setRead();
    verify(programField).getProcessingInfo();
    verify(programField).setProcessingInfo(isA(Object.class));
  }

  /**
   * Test {@link ReadWriteFieldMarker#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@link MutableBoolean} {@link MutableBoolean#set()} does nothing.
   *   <li>Then calls {@link MutableBoolean#set()}.
   * </ul>
   *
   * <p>Method under test: {@link ReadWriteFieldMarker#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given MutableBoolean set() does nothing; then calls set()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReadWriteFieldMarker.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_givenMutableBooleanSetDoesNothing_thenCallsSet() {
    // Arrange
    MutableBoolean repeatTrigger = mock(MutableBoolean.class);
    doNothing().when(repeatTrigger).set();
    ReadWriteFieldMarker readWriteFieldMarker = new ReadWriteFieldMarker(repeatTrigger);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        mock(ProgramFieldOptimizationInfo.class);
    doNothing().when(programFieldOptimizationInfo).setWritten();
    when(programFieldOptimizationInfo.isWritten()).thenReturn(false);
    when(programFieldOptimizationInfo.isRead()).thenReturn(true);

    ProgramField programField = mock(ProgramField.class);
    when(programField.getName(Mockito.<Clazz>any())).thenReturn("Name");
    when(programField.getProcessingInfo()).thenReturn(programFieldOptimizationInfo);
    doNothing().when(programField).setProcessingInfo(Mockito.<Object>any());
    programField.setProcessingInfo(mock(FieldOptimizationInfo.class));

    // Act
    readWriteFieldMarker.visitProgramField(programClass, programField);

    // Assert
    verify(programClass).getName();
    verify(programField).getName(isA(Clazz.class));
    verify(repeatTrigger).set();
    verify(programFieldOptimizationInfo).isRead();
    verify(programFieldOptimizationInfo).isWritten();
    verify(programFieldOptimizationInfo).setWritten();
    verify(programField, atLeast(1)).getProcessingInfo();
    verify(programField).setProcessingInfo(isA(Object.class));
  }

  /**
   * Test {@link ReadWriteFieldMarker#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@link MutableBoolean} {@link MutableBoolean#set()} does nothing.
   *   <li>Then calls {@link MutableBoolean#set()}.
   * </ul>
   *
   * <p>Method under test: {@link ReadWriteFieldMarker#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given MutableBoolean set() does nothing; then calls set()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReadWriteFieldMarker.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_givenMutableBooleanSetDoesNothing_thenCallsSet2() {
    // Arrange
    MutableBoolean repeatTrigger = mock(MutableBoolean.class);
    doNothing().when(repeatTrigger).set();
    ReadWriteFieldMarker readWriteFieldMarker = new ReadWriteFieldMarker(repeatTrigger);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        mock(ProgramFieldOptimizationInfo.class);
    doNothing().when(programFieldOptimizationInfo).setRead();
    doNothing().when(programFieldOptimizationInfo).setWritten();
    when(programFieldOptimizationInfo.isWritten()).thenReturn(false);
    when(programFieldOptimizationInfo.isRead()).thenReturn(false);

    ProgramField programField = mock(ProgramField.class);
    when(programField.getName(Mockito.<Clazz>any())).thenReturn("Name");
    when(programField.getProcessingInfo()).thenReturn(programFieldOptimizationInfo);
    doNothing().when(programField).setProcessingInfo(Mockito.<Object>any());
    programField.setProcessingInfo(mock(FieldOptimizationInfo.class));

    // Act
    readWriteFieldMarker.visitProgramField(programClass, programField);

    // Assert
    verify(programClass, atLeast(1)).getName();
    verify(programField, atLeast(1)).getName(isA(Clazz.class));
    verify(repeatTrigger, atLeast(1)).set();
    verify(programFieldOptimizationInfo).isRead();
    verify(programFieldOptimizationInfo).isWritten();
    verify(programFieldOptimizationInfo).setRead();
    verify(programFieldOptimizationInfo).setWritten();
    verify(programField, atLeast(1)).getProcessingInfo();
    verify(programField).setProcessingInfo(isA(Object.class));
  }

  /**
   * Test {@link ReadWriteFieldMarker#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@link ProgramFieldOptimizationInfo} {@link ProgramFieldOptimizationInfo#isRead()}
   *       return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ReadWriteFieldMarker#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given ProgramFieldOptimizationInfo isRead() return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReadWriteFieldMarker.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_givenProgramFieldOptimizationInfoIsReadReturnTrue() {
    // Arrange
    ReadWriteFieldMarker readWriteFieldMarker = new ReadWriteFieldMarker(new MutableBoolean());

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        mock(ProgramFieldOptimizationInfo.class);
    doNothing().when(programFieldOptimizationInfo).setWritten();
    when(programFieldOptimizationInfo.isWritten()).thenReturn(false);
    when(programFieldOptimizationInfo.isRead()).thenReturn(true);

    ProgramField programField = mock(ProgramField.class);
    when(programField.getName(Mockito.<Clazz>any())).thenReturn("Name");
    when(programField.getProcessingInfo()).thenReturn(programFieldOptimizationInfo);
    doNothing().when(programField).setProcessingInfo(Mockito.<Object>any());
    programField.setProcessingInfo(mock(FieldOptimizationInfo.class));

    // Act
    readWriteFieldMarker.visitProgramField(programClass, programField);

    // Assert
    verify(programClass).getName();
    verify(programField).getName(isA(Clazz.class));
    verify(programFieldOptimizationInfo).isRead();
    verify(programFieldOptimizationInfo).isWritten();
    verify(programFieldOptimizationInfo).setWritten();
    verify(programField, atLeast(1)).getProcessingInfo();
    verify(programField).setProcessingInfo(isA(Object.class));
  }

  /**
   * Test {@link ReadWriteFieldMarker#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then calls {@link FieldOptimizationInfo#isRead()}.
   * </ul>
   *
   * <p>Method under test: {@link ReadWriteFieldMarker#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField); then calls isRead()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReadWriteFieldMarker.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_thenCallsIsRead() {
    // Arrange
    ReadWriteFieldMarker readWriteFieldMarker = new ReadWriteFieldMarker(new MutableBoolean());
    ProgramClass programClass = new ProgramClass();

    FieldOptimizationInfo fieldOptimizationInfo = mock(FieldOptimizationInfo.class);
    when(fieldOptimizationInfo.isRead()).thenReturn(false);
    when(fieldOptimizationInfo.isWritten()).thenReturn(false);

    ProgramField programField = new ProgramField();
    programField.setProcessingInfo(fieldOptimizationInfo);

    // Act
    readWriteFieldMarker.visitProgramField(programClass, programField);

    // Assert
    verify(fieldOptimizationInfo).isRead();
    verify(fieldOptimizationInfo).isWritten();
  }

  /**
   * Test {@link ReadWriteFieldMarker#isRead(Field)}.
   *
   * <ul>
   *   <li>Given {@link FieldOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ReadWriteFieldMarker#isRead(Field)}
   */
  @Test
  @DisplayName(
      "Test isRead(Field); given FieldOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ReadWriteFieldMarker.isRead(Field)"})
  void testIsRead_givenFieldOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryField field = new LibraryField(1, "Name", "Descriptor");
    field.setProcessingInfo(new FieldOptimizationInfo());

    // Act and Assert
    assertTrue(ReadWriteFieldMarker.isRead(field));
  }

  /**
   * Test {@link ReadWriteFieldMarker#isRead(Field)}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ReadWriteFieldMarker#isRead(Field)}
   */
  @Test
  @DisplayName("Test isRead(Field); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ReadWriteFieldMarker.isRead(Field)"})
  void testIsRead_thenReturnFalse() {
    // Arrange
    LibraryField field = new LibraryField(1, "Name", "Descriptor");
    LibraryClass clazz = new LibraryClass();
    LibraryField field2 = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field2, true);
    field.setProcessingInfo(programFieldOptimizationInfo);

    // Act and Assert
    assertFalse(ReadWriteFieldMarker.isRead(field));
  }

  /**
   * Test {@link ReadWriteFieldMarker#isWritten(Field)}.
   *
   * <ul>
   *   <li>Given {@link FieldOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ReadWriteFieldMarker#isWritten(Field)}
   */
  @Test
  @DisplayName(
      "Test isWritten(Field); given FieldOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ReadWriteFieldMarker.isWritten(Field)"})
  void testIsWritten_givenFieldOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryField field = new LibraryField(1, "Name", "Descriptor");
    field.setProcessingInfo(new FieldOptimizationInfo());

    // Act and Assert
    assertTrue(ReadWriteFieldMarker.isWritten(field));
  }

  /**
   * Test {@link ReadWriteFieldMarker#isWritten(Field)}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ReadWriteFieldMarker#isWritten(Field)}
   */
  @Test
  @DisplayName("Test isWritten(Field); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ReadWriteFieldMarker.isWritten(Field)"})
  void testIsWritten_thenReturnFalse() {
    // Arrange
    LibraryField field = new LibraryField(1, "Name", "Descriptor");
    LibraryClass clazz = new LibraryClass();
    LibraryField field2 = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field2, true);
    field.setProcessingInfo(programFieldOptimizationInfo);

    // Act and Assert
    assertFalse(ReadWriteFieldMarker.isWritten(field));
  }
}
