package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
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
import proguard.classfile.Field;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.RefConstant;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.MemberVisitor;

class NonPrivateMemberMarkerDiffblueTest {
  /**
   * Test {@link NonPrivateMemberMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClass#constantPoolEntriesAccept(ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link NonPrivateMemberMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); then calls constantPoolEntriesAccept(ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NonPrivateMemberMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenCallsConstantPoolEntriesAccept() {
    // Arrange
    NonPrivateMemberMarker nonPrivateMemberMarker = new NonPrivateMemberMarker();

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).constantPoolEntriesAccept(Mockito.<ConstantVisitor>any());
    doNothing()
        .when(programClass)
        .methodAccept(Mockito.<String>any(), Mockito.<String>any(), Mockito.<MemberVisitor>any());
    doNothing().when(programClass).methodsAccept(Mockito.<MemberVisitor>any());

    // Act
    nonPrivateMemberMarker.visitProgramClass(programClass);

    // Assert
    verify(programClass).constantPoolEntriesAccept(isA(ConstantVisitor.class));
    verify(programClass, atLeast(1))
        .methodAccept(Mockito.<String>any(), eq("()V"), isA(MemberVisitor.class));
    verify(programClass).methodsAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link NonPrivateMemberMarker#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>When {@link ClassConstant} {@link ClassConstant#accept(Clazz, ConstantVisitor)} does
   *       nothing.
   *   <li>Then calls {@link ClassConstant#accept(Clazz, ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link NonPrivateMemberMarker#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); when ClassConstant accept(Clazz, ConstantVisitor) does nothing; then calls accept(Clazz, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NonPrivateMemberMarker.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_whenClassConstantAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    NonPrivateMemberMarker nonPrivateMemberMarker = new NonPrivateMemberMarker();

    ClassConstant classConstant = mock(ClassConstant.class);
    doNothing().when(classConstant).accept(Mockito.<Clazz>any(), Mockito.<ConstantVisitor>any());
    ProgramClass programClass =
        new ProgramClass(1, 2, new Constant[] {mock(ClassConstant.class), classConstant}, 1, 1, 1);
    programClass.u2methodsCount = 0;

    // Act
    nonPrivateMemberMarker.visitProgramClass(programClass);

    // Assert
    verify(classConstant).accept(isA(Clazz.class), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link NonPrivateMemberMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link StringConstant#referencedMemberAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link NonPrivateMemberMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); then calls referencedMemberAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NonPrivateMemberMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenCallsReferencedMemberAccept() {
    // Arrange
    NonPrivateMemberMarker nonPrivateMemberMarker = new NonPrivateMemberMarker();
    LibraryClass clazz = new LibraryClass();

    StringConstant stringConstant = mock(StringConstant.class);
    doNothing().when(stringConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());

    // Act
    nonPrivateMemberMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(stringConstant).referencedMemberAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link NonPrivateMemberMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryClass#getClassName(int)}.
   * </ul>
   *
   * <p>Method under test: {@link NonPrivateMemberMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant); then calls getClassName(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NonPrivateMemberMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant_thenCallsGetClassName() {
    // Arrange
    NonPrivateMemberMarker nonPrivateMemberMarker = new NonPrivateMemberMarker();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");
    when(clazz.getName()).thenReturn("Name");

    // Act
    nonPrivateMemberMarker.visitAnyRefConstant(clazz, new FieldrefConstant());

    // Assert
    verify(clazz).getClassName(0);
    verify(clazz).getName();
  }

  /**
   * Test {@link NonPrivateMemberMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link FieldrefConstant#referencedMemberAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link NonPrivateMemberMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyRefConstant(Clazz, RefConstant); then calls referencedMemberAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NonPrivateMemberMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant_thenCallsReferencedMemberAccept() {
    // Arrange
    NonPrivateMemberMarker nonPrivateMemberMarker = new NonPrivateMemberMarker();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getName()).thenReturn("Name");

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    when(refConstant.getClassName(Mockito.<Clazz>any())).thenReturn("Class Name");

    // Act
    nonPrivateMemberMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(clazz).getName();
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).getClassName(isA(Clazz.class));
  }

  /**
   * Test {@link NonPrivateMemberMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass} {@link LibraryClass#getName()} return {@code Class Name}.
   * </ul>
   *
   * <p>Method under test: {@link NonPrivateMemberMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyRefConstant(Clazz, RefConstant); when LibraryClass getName() return 'Class Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NonPrivateMemberMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant_whenLibraryClassGetNameReturnClassName() {
    // Arrange
    NonPrivateMemberMarker nonPrivateMemberMarker = new NonPrivateMemberMarker();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getName()).thenReturn("Class Name");

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    when(refConstant.getClassName(Mockito.<Clazz>any())).thenReturn("Class Name");

    // Act
    nonPrivateMemberMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(clazz).getName();
    verify(refConstant).getClassName(isA(Clazz.class));
  }

  /**
   * Test {@link NonPrivateMemberMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethodOptimizationInfo#setCanNotBeMadePrivate()}.
   * </ul>
   *
   * <p>Method under test: {@link NonPrivateMemberMarker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then calls setCanNotBeMadePrivate()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void NonPrivateMemberMarker.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenCallsSetCanNotBeMadePrivate() {
    // Arrange
    NonPrivateMemberMarker nonPrivateMemberMarker = new NonPrivateMemberMarker();
    ProgramClass programClass = new ProgramClass();

    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setCanNotBeMadePrivate();

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(programMethodOptimizationInfo);

    // Act
    nonPrivateMemberMarker.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programMethodOptimizationInfo).setCanNotBeMadePrivate();
  }

  /**
   * Test {@link NonPrivateMemberMarker#canBeMadePrivate(Field)} with {@code field}.
   *
   * <ul>
   *   <li>Given {@link FieldOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link NonPrivateMemberMarker#canBeMadePrivate(Field)}
   */
  @Test
  @DisplayName(
      "Test canBeMadePrivate(Field) with 'field'; given FieldOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean NonPrivateMemberMarker.canBeMadePrivate(Field)"})
  void testCanBeMadePrivateWithField_givenFieldOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryField field = new LibraryField(1, "Name", "Descriptor");
    field.setProcessingInfo(new FieldOptimizationInfo());

    // Act and Assert
    assertFalse(NonPrivateMemberMarker.canBeMadePrivate(field));
  }

  /**
   * Test {@link NonPrivateMemberMarker#canBeMadePrivate(Field)} with {@code field}.
   *
   * <ul>
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link NonPrivateMemberMarker#canBeMadePrivate(Field)}
   */
  @Test
  @DisplayName("Test canBeMadePrivate(Field) with 'field'; then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean NonPrivateMemberMarker.canBeMadePrivate(Field)"})
  void testCanBeMadePrivateWithField_thenReturnTrue() {
    // Arrange
    LibraryField field = new LibraryField(1, "Name", "Descriptor");
    LibraryClass clazz = new LibraryClass();
    LibraryField field2 = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field2, true);
    field.setProcessingInfo(programFieldOptimizationInfo);

    // Act and Assert
    assertTrue(NonPrivateMemberMarker.canBeMadePrivate(field));
  }

  /**
   * Test {@link NonPrivateMemberMarker#canBeMadePrivate(Method)} with {@code method}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link NonPrivateMemberMarker#canBeMadePrivate(Method)}
   */
  @Test
  @DisplayName(
      "Test canBeMadePrivate(Method) with 'method'; given MethodOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean NonPrivateMemberMarker.canBeMadePrivate(Method)"})
  void testCanBeMadePrivateWithMethod_givenMethodOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertFalse(NonPrivateMemberMarker.canBeMadePrivate(method));
  }
}
