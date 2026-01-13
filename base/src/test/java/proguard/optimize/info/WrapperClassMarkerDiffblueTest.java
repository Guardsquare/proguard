package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
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
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.visitor.MemberVisitor;

class WrapperClassMarkerDiffblueTest {
  /**
   * Test {@link WrapperClassMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link WrapperClassMarker} (default constructor).
   *   <li>Then calls {@link ProgramClass#fieldsAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link WrapperClassMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given WrapperClassMarker (default constructor); then calls fieldsAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void WrapperClassMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenWrapperClassMarker_thenCallsFieldsAccept() {
    // Arrange
    WrapperClassMarker wrapperClassMarker = new WrapperClassMarker();

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).fieldsAccept(Mockito.<MemberVisitor>any());

    // Act
    wrapperClassMarker.visitProgramClass(programClass);

    // Assert
    verify(programClass).fieldsAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link WrapperClassMarker#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given one.
   *   <li>When {@link ProgramClass}.
   *   <li>Then calls {@link ProgramField#getAccessFlags()}.
   * </ul>
   *
   * <p>Method under test: {@link WrapperClassMarker#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given one; when ProgramClass; then calls getAccessFlags()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void WrapperClassMarker.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_givenOne_whenProgramClass_thenCallsGetAccessFlags() {
    // Arrange
    WrapperClassMarker wrapperClassMarker = new WrapperClassMarker();
    ProgramClass programClass = mock(ProgramClass.class);

    ProgramField programField = mock(ProgramField.class);
    when(programField.getAccessFlags()).thenReturn(1);
    when(programField.getDescriptor(Mockito.<Clazz>any())).thenReturn("");

    // Act
    wrapperClassMarker.visitProgramField(programClass, programField);

    // Assert
    verify(programField).getAccessFlags();
    verify(programField).getDescriptor(isA(Clazz.class));
  }

  /**
   * Test {@link WrapperClassMarker#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@link WrapperClassMarker} (default constructor).
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link WrapperClassMarker#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given WrapperClassMarker (default constructor); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void WrapperClassMarker.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_givenWrapperClassMarker_thenCallsGetString() {
    // Arrange
    WrapperClassMarker wrapperClassMarker = new WrapperClassMarker();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    wrapperClassMarker.visitProgramField(programClass, new ProgramField());

    // Assert
    verify(programClass).getString(0);
  }

  /**
   * Test {@link WrapperClassMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@link WrapperClassMarker} (default constructor).
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link WrapperClassMarker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given WrapperClassMarker (default constructor); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void WrapperClassMarker.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_givenWrapperClassMarker_thenCallsGetString() {
    // Arrange
    WrapperClassMarker wrapperClassMarker = new WrapperClassMarker();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    wrapperClassMarker.visitProgramMethod(programClass, new ProgramMethod());

    // Assert
    verify(programClass).getString(0);
  }

  /**
   * Test {@link WrapperClassMarker#getWrappedClass(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link WrapperClassMarker#getWrappedClass(Clazz)}
   */
  @Test
  @DisplayName("Test getWrappedClass(Clazz); given ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Clazz WrapperClassMarker.getWrappedClass(Clazz)"})
  void testGetWrappedClass_givenClassOptimizationInfo() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ClassOptimizationInfo());

    // Act and Assert
    assertNull(WrapperClassMarker.getWrappedClass(clazz));
  }

  /**
   * Test {@link WrapperClassMarker#getWrappedClass(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link WrapperClassMarker#getWrappedClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test getWrappedClass(Clazz); given ProgramClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Clazz WrapperClassMarker.getWrappedClass(Clazz)"})
  void testGetWrappedClass_givenProgramClassOptimizationInfo() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act and Assert
    assertNull(WrapperClassMarker.getWrappedClass(clazz));
  }
}
