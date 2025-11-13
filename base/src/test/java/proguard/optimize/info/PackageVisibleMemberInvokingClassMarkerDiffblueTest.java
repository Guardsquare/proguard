package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.RefConstant;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.resources.file.ResourceFile;

class PackageVisibleMemberInvokingClassMarkerDiffblueTest {
  /**
   * Test {@link PackageVisibleMemberInvokingClassMarker#visitStringConstant(Clazz,
   * StringConstant)}.
   *
   * <ul>
   *   <li>Given {@link LibraryClass} {@link LibraryClass#accept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link LibraryClass#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link PackageVisibleMemberInvokingClassMarker#visitStringConstant(Clazz,
   * StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); given LibraryClass accept(ClassVisitor) does nothing; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void PackageVisibleMemberInvokingClassMarker.visitStringConstant(Clazz, StringConstant)"
  })
  void testVisitStringConstant_givenLibraryClassAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    PackageVisibleMemberInvokingClassMarker packageVisibleMemberInvokingClassMarker =
        new PackageVisibleMemberInvokingClassMarker();
    LibraryClass clazz = new LibraryClass();

    LibraryClass libraryClass = mock(LibraryClass.class);
    doNothing().when(libraryClass).accept(Mockito.<ClassVisitor>any());
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = libraryClass;
    stringConstant.referencedMember = null;

    // Act
    packageVisibleMemberInvokingClassMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(libraryClass).accept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link PackageVisibleMemberInvokingClassMarker#visitStringConstant(Clazz,
   * StringConstant)}.
   *
   * <ul>
   *   <li>Given {@link LibraryField} {@link LibraryField#accept(Clazz, MemberVisitor)} does
   *       nothing.
   *   <li>Then calls {@link LibraryField#accept(Clazz, MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link PackageVisibleMemberInvokingClassMarker#visitStringConstant(Clazz,
   * StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); given LibraryField accept(Clazz, MemberVisitor) does nothing; then calls accept(Clazz, MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void PackageVisibleMemberInvokingClassMarker.visitStringConstant(Clazz, StringConstant)"
  })
  void testVisitStringConstant_givenLibraryFieldAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    PackageVisibleMemberInvokingClassMarker packageVisibleMemberInvokingClassMarker =
        new PackageVisibleMemberInvokingClassMarker();
    LibraryClass clazz = new LibraryClass();

    LibraryField libraryField = mock(LibraryField.class);
    doNothing().when(libraryField).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = new LibraryClass();
    stringConstant.referencedMember = libraryField;

    // Act
    packageVisibleMemberInvokingClassMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(libraryField).accept(isA(Clazz.class), isA(MemberVisitor.class));
  }

  /**
   * Test {@link PackageVisibleMemberInvokingClassMarker#visitStringConstant(Clazz,
   * StringConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link StringConstant#referencedClassAccept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link PackageVisibleMemberInvokingClassMarker#visitStringConstant(Clazz,
   * StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); then calls referencedClassAccept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void PackageVisibleMemberInvokingClassMarker.visitStringConstant(Clazz, StringConstant)"
  })
  void testVisitStringConstant_thenCallsReferencedClassAccept() {
    // Arrange
    PackageVisibleMemberInvokingClassMarker packageVisibleMemberInvokingClassMarker =
        new PackageVisibleMemberInvokingClassMarker();
    LibraryClass clazz = new LibraryClass();

    StringConstant stringConstant = mock(StringConstant.class);
    doNothing().when(stringConstant).referencedClassAccept(Mockito.<ClassVisitor>any());
    doNothing().when(stringConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());

    // Act
    packageVisibleMemberInvokingClassMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(stringConstant).referencedClassAccept(isA(ClassVisitor.class));
    verify(stringConstant).referencedMemberAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link PackageVisibleMemberInvokingClassMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link FieldrefConstant#referencedMemberAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link PackageVisibleMemberInvokingClassMarker#visitAnyRefConstant(Clazz,
   * RefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyRefConstant(Clazz, RefConstant); then calls referencedMemberAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void PackageVisibleMemberInvokingClassMarker.visitAnyRefConstant(Clazz, RefConstant)"
  })
  void testVisitAnyRefConstant_thenCallsReferencedMemberAccept() {
    // Arrange
    PackageVisibleMemberInvokingClassMarker packageVisibleMemberInvokingClassMarker =
        new PackageVisibleMemberInvokingClassMarker();
    LibraryClass clazz = new LibraryClass();

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    packageVisibleMemberInvokingClassMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link PackageVisibleMemberInvokingClassMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassConstant#referencedClassAccept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link PackageVisibleMemberInvokingClassMarker#visitClassConstant(Clazz,
   * ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); then calls referencedClassAccept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void PackageVisibleMemberInvokingClassMarker.visitClassConstant(Clazz, ClassConstant)"
  })
  void testVisitClassConstant_thenCallsReferencedClassAccept() {
    // Arrange
    PackageVisibleMemberInvokingClassMarker packageVisibleMemberInvokingClassMarker =
        new PackageVisibleMemberInvokingClassMarker();
    LibraryClass clazz = new LibraryClass();

    ClassConstant classConstant = mock(ClassConstant.class);
    doNothing().when(classConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    packageVisibleMemberInvokingClassMarker.visitClassConstant(clazz, classConstant);

    // Assert
    verify(classConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link PackageVisibleMemberInvokingClassMarker#invokesPackageVisibleMembers(Clazz)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link
   * PackageVisibleMemberInvokingClassMarker#invokesPackageVisibleMembers(Clazz)}
   */
  @Test
  @DisplayName(
      "Test invokesPackageVisibleMembers(Clazz); given ClassOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean PackageVisibleMemberInvokingClassMarker.invokesPackageVisibleMembers(Clazz)"
  })
  void testInvokesPackageVisibleMembers_givenClassOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ClassOptimizationInfo());

    // Act and Assert
    assertTrue(PackageVisibleMemberInvokingClassMarker.invokesPackageVisibleMembers(clazz));
  }

  /**
   * Test {@link PackageVisibleMemberInvokingClassMarker#invokesPackageVisibleMembers(Clazz)}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link
   * PackageVisibleMemberInvokingClassMarker#invokesPackageVisibleMembers(Clazz)}
   */
  @Test
  @DisplayName("Test invokesPackageVisibleMembers(Clazz); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean PackageVisibleMemberInvokingClassMarker.invokesPackageVisibleMembers(Clazz)"
  })
  void testInvokesPackageVisibleMembers_thenReturnFalse() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act and Assert
    assertFalse(PackageVisibleMemberInvokingClassMarker.invokesPackageVisibleMembers(clazz));
  }
}
