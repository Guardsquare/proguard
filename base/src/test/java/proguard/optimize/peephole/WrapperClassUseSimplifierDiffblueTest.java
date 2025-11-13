package proguard.optimize.peephole;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.ProgramClass;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.visitor.ClassVisitor;

@ExtendWith(MockitoExtension.class)
class WrapperClassUseSimplifierDiffblueTest {
  @InjectMocks private WrapperClassUseSimplifier wrapperClassUseSimplifier;

  /**
   * Test {@link WrapperClassUseSimplifier#visitMethodrefConstant(Clazz, MethodrefConstant)}.
   *
   * <p>Method under test: {@link WrapperClassUseSimplifier#visitMethodrefConstant(Clazz,
   * MethodrefConstant)}
   */
  @Test
  @DisplayName("Test visitMethodrefConstant(Clazz, MethodrefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void WrapperClassUseSimplifier.visitMethodrefConstant(Clazz, MethodrefConstant)"
  })
  void testVisitMethodrefConstant() {
    // Arrange
    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getName(anyInt())).thenReturn("<init>");
    LibraryClass referencedClass = new LibraryClass();

    // Act
    wrapperClassUseSimplifier.visitMethodrefConstant(
        clazz, new MethodrefConstant(1, 1, referencedClass, new LibraryMethod()));

    // Assert
    verify(clazz).getName(1);
  }

  /**
   * Test {@link WrapperClassUseSimplifier#visitMethodrefConstant(Clazz, MethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@code Name}.
   *   <li>When {@link LibraryClass} {@link LibraryClass#getName(int)} return {@code Name}.
   * </ul>
   *
   * <p>Method under test: {@link WrapperClassUseSimplifier#visitMethodrefConstant(Clazz,
   * MethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodrefConstant(Clazz, MethodrefConstant); given 'Name'; when LibraryClass getName(int) return 'Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void WrapperClassUseSimplifier.visitMethodrefConstant(Clazz, MethodrefConstant)"
  })
  void testVisitMethodrefConstant_givenName_whenLibraryClassGetNameReturnName() {
    // Arrange
    WrapperClassUseSimplifier wrapperClassUseSimplifier = new WrapperClassUseSimplifier();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getName(anyInt())).thenReturn("Name");

    // Act
    wrapperClassUseSimplifier.visitMethodrefConstant(clazz, new MethodrefConstant());

    // Assert
    verify(clazz).getName(0);
  }

  /**
   * Test {@link WrapperClassUseSimplifier#visitMethodrefConstant(Clazz, MethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link WrapperClassUseSimplifier#WrapperClassUseSimplifier()}.
   *   <li>Then calls {@link LibraryClass#getName(int)}.
   * </ul>
   *
   * <p>Method under test: {@link WrapperClassUseSimplifier#visitMethodrefConstant(Clazz,
   * MethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodrefConstant(Clazz, MethodrefConstant); given WrapperClassUseSimplifier(); then calls getName(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void WrapperClassUseSimplifier.visitMethodrefConstant(Clazz, MethodrefConstant)"
  })
  void testVisitMethodrefConstant_givenWrapperClassUseSimplifier_thenCallsGetName() {
    // Arrange
    WrapperClassUseSimplifier wrapperClassUseSimplifier = new WrapperClassUseSimplifier();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getName(anyInt())).thenReturn("<init>");

    // Act
    wrapperClassUseSimplifier.visitMethodrefConstant(clazz, new MethodrefConstant());

    // Assert
    verify(clazz).getName(0);
  }

  /**
   * Test {@link WrapperClassUseSimplifier#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Given {@link LibraryClass#LibraryClass()}.
   *   <li>Then calls {@link ProgramClass#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link WrapperClassUseSimplifier#visitClassConstant(Clazz,
   * ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); given LibraryClass(); then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void WrapperClassUseSimplifier.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_givenLibraryClass_thenCallsAccept() {
    // Arrange
    WrapperClassUseSimplifier wrapperClassUseSimplifier = new WrapperClassUseSimplifier();
    LibraryClass clazz = new LibraryClass();

    ProgramClass referencedClass = mock(ProgramClass.class);
    doNothing().when(referencedClass).accept(Mockito.<ClassVisitor>any());
    doNothing().when(referencedClass).addSubClass(Mockito.<Clazz>any());
    referencedClass.addSubClass(new LibraryClass());
    ClassConstant classConstant = new ClassConstant(1, referencedClass);

    // Act
    wrapperClassUseSimplifier.visitClassConstant(clazz, classConstant);

    // Assert
    verify(referencedClass).accept(isA(ClassVisitor.class));
    verify(referencedClass).addSubClass(isA(Clazz.class));
  }

  /**
   * Test {@link WrapperClassUseSimplifier#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassConstant#referencedClassAccept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link WrapperClassUseSimplifier#visitClassConstant(Clazz,
   * ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); then calls referencedClassAccept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void WrapperClassUseSimplifier.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenCallsReferencedClassAccept() {
    // Arrange
    WrapperClassUseSimplifier wrapperClassUseSimplifier = new WrapperClassUseSimplifier();
    LibraryClass clazz = new LibraryClass();

    ClassConstant classConstant = mock(ClassConstant.class);
    doNothing().when(classConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    wrapperClassUseSimplifier.visitClassConstant(clazz, classConstant);

    // Assert
    verify(classConstant).referencedClassAccept(isA(ClassVisitor.class));
  }
}
