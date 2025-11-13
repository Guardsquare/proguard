package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Member;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.DoubleConstant;
import proguard.classfile.constant.DynamicConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.FloatConstant;
import proguard.classfile.constant.IntegerConstant;
import proguard.classfile.constant.InvokeDynamicConstant;
import proguard.classfile.constant.LongConstant;
import proguard.classfile.constant.MethodHandleConstant;
import proguard.classfile.constant.MethodTypeConstant;
import proguard.classfile.constant.ModuleConstant;
import proguard.classfile.constant.NameAndTypeConstant;
import proguard.classfile.constant.PackageConstant;
import proguard.classfile.constant.RefConstant;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.resources.file.ResourceFile;

class AccessMethodMarkerDiffblueTest {
  /**
   * Test {@link AccessMethodMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int,
   * ConstantInstruction)}.
   *
   * <ul>
   *   <li>When {@link Clazz} {@link Clazz#accept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link Clazz#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitConstantInstruction(Clazz, Method,
   * CodeAttribute, int, ConstantInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction); when Clazz accept(ClassVisitor) does nothing; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AccessMethodMarker.visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)"
  })
  void testVisitConstantInstruction_whenClazzAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();

    Clazz referencedClass = mock(Clazz.class);
    doNothing().when(referencedClass).accept(Mockito.<ClassVisitor>any());
    ClassConstant classConstant = new ClassConstant(1, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    accessMethodMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());

    // Assert
    verify(referencedClass).accept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Given {@link LibraryField} {@link LibraryField#accept(Clazz, MemberVisitor)} does
   *       nothing.
   *   <li>Then calls {@link LibraryField#accept(Clazz, MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); given LibraryField accept(Clazz, MemberVisitor) does nothing; then calls accept(Clazz, MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_givenLibraryFieldAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();

    LibraryField libraryField = mock(LibraryField.class);
    doNothing().when(libraryField).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = new LibraryClass();
    stringConstant.referencedMember = libraryField;

    // Act
    accessMethodMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(libraryField).accept(isA(Clazz.class), isA(MemberVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Given {@code null}.
   *   <li>Then calls {@link LibraryClass#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); given 'null'; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_givenNull_thenCallsAccept() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();

    LibraryClass libraryClass = mock(LibraryClass.class);
    doNothing().when(libraryClass).accept(Mockito.<ClassVisitor>any());
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = libraryClass;
    stringConstant.referencedMember = null;

    // Act
    accessMethodMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(libraryClass).accept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link StringConstant#referencedClassAccept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); then calls referencedClassAccept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenCallsReferencedClassAccept() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();

    StringConstant stringConstant = mock(StringConstant.class);
    doNothing().when(stringConstant).referencedClassAccept(Mockito.<ClassVisitor>any());
    doNothing().when(stringConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());

    // Act
    accessMethodMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(stringConstant).referencedClassAccept(isA(ClassVisitor.class));
    verify(stringConstant).referencedMemberAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethodOptimizationInfo#setAccessesPackageCode()}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); then calls setAccessesPackageCode()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenCallsSetAccessesPackageCode() {
    // Arrange
    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setAccessesPackageCode();

    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(programMethodOptimizationInfo);

    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();
    CodeAttribute codeAttribute = new CodeAttribute();

    accessMethodMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());
    LibraryClass clazz2 = new LibraryClass();
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = mock(LibraryClass.class);
    LibraryMethod libraryMethod = new LibraryMethod(0, "Name", "Descriptor");
    stringConstant.referencedMember = libraryMethod;

    // Act
    accessMethodMarker.visitStringConstant(clazz2, stringConstant);

    // Assert
    verify(programMethodOptimizationInfo).setAccessesPackageCode();
  }

  /**
   * Test {@link AccessMethodMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethodOptimizationInfo#setAccessesPrivateCode()}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); then calls setAccessesPrivateCode()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenCallsSetAccessesPrivateCode() {
    // Arrange
    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setAccessesPrivateCode();

    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(programMethodOptimizationInfo);

    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();
    CodeAttribute codeAttribute = new CodeAttribute();

    accessMethodMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());
    LibraryClass clazz2 = new LibraryClass();
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = mock(LibraryClass.class);
    LibraryMethod libraryMethod = new LibraryMethod(2, "Name", "Descriptor");
    stringConstant.referencedMember = libraryMethod;

    // Act
    accessMethodMarker.visitStringConstant(clazz2, stringConstant);

    // Assert
    verify(programMethodOptimizationInfo).setAccessesPrivateCode();
  }

  /**
   * Test {@link AccessMethodMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethodOptimizationInfo#setAccessesProtectedCode()}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); then calls setAccessesProtectedCode()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenCallsSetAccessesProtectedCode() {
    // Arrange
    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setAccessesProtectedCode();

    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(programMethodOptimizationInfo);

    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();
    CodeAttribute codeAttribute = new CodeAttribute();

    accessMethodMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());
    LibraryClass clazz2 = new LibraryClass();
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = mock(LibraryClass.class);
    LibraryMethod libraryMethod = new LibraryMethod(4, "Name", "Descriptor");
    stringConstant.referencedMember = libraryMethod;

    // Act
    accessMethodMarker.visitStringConstant(clazz2, stringConstant);

    // Assert
    verify(programMethodOptimizationInfo).setAccessesProtectedCode();
  }

  /**
   * Test {@link AccessMethodMarker#visitDynamicConstant(Clazz, DynamicConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link DynamicConstant#bootstrapMethodHandleAccept(Clazz, ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitDynamicConstant(Clazz, DynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitDynamicConstant(Clazz, DynamicConstant); then calls bootstrapMethodHandleAccept(Clazz, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitDynamicConstant(Clazz, DynamicConstant)"})
  void testVisitDynamicConstant_thenCallsBootstrapMethodHandleAccept() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();

    DynamicConstant dynamicConstant = mock(DynamicConstant.class);
    doNothing()
        .when(dynamicConstant)
        .bootstrapMethodHandleAccept(Mockito.<Clazz>any(), Mockito.<ConstantVisitor>any());

    // Act
    accessMethodMarker.visitDynamicConstant(clazz, dynamicConstant);

    // Assert
    verify(dynamicConstant)
        .bootstrapMethodHandleAccept(isA(Clazz.class), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link InvokeDynamicConstant#bootstrapMethodHandleAccept(Clazz,
   *       ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant); then calls bootstrapMethodHandleAccept(Clazz, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AccessMethodMarker.visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)"
  })
  void testVisitInvokeDynamicConstant_thenCallsBootstrapMethodHandleAccept() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();

    InvokeDynamicConstant invokeDynamicConstant = mock(InvokeDynamicConstant.class);
    doNothing()
        .when(invokeDynamicConstant)
        .bootstrapMethodHandleAccept(Mockito.<Clazz>any(), Mockito.<ConstantVisitor>any());

    // Act
    accessMethodMarker.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

    // Assert
    verify(invokeDynamicConstant)
        .bootstrapMethodHandleAccept(isA(Clazz.class), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitMethodHandleConstant(Clazz, MethodHandleConstant)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass} {@link LibraryClass#accept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link LibraryClass#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitMethodHandleConstant(Clazz,
   * MethodHandleConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodHandleConstant(Clazz, MethodHandleConstant); when LibraryClass accept(ClassVisitor) does nothing; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AccessMethodMarker.visitMethodHandleConstant(Clazz, MethodHandleConstant)"
  })
  void testVisitMethodHandleConstant_whenLibraryClassAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();

    LibraryClass referencedClass = mock(LibraryClass.class);
    doNothing().when(referencedClass).accept(Mockito.<ClassVisitor>any());
    ClassConstant classConstant = new ClassConstant(1, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    // Act
    accessMethodMarker.visitMethodHandleConstant(clazz, new MethodHandleConstant());

    // Assert
    verify(referencedClass).accept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant2() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant3() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    Constant[] constantPool = new Constant[] {new DynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant4() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant5() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant6() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    Constant[] constantPool = new Constant[] {new InvokeDynamicConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant7() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass referencedClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    ClassConstant classConstant = new ClassConstant(1, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant8() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    Constant[] constantPool = new Constant[] {new LongConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant9() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    Constant[] constantPool = new Constant[] {new MethodTypeConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant10() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    Constant[] constantPool = new Constant[] {new ModuleConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant11() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    Constant[] constantPool = new Constant[] {new NameAndTypeConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyRefConstant(Clazz, RefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant12() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    Constant[] constantPool = new Constant[] {new PackageConstant()};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass} {@link LibraryClass#accept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link LibraryClass#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyRefConstant(Clazz, RefConstant); when LibraryClass accept(ClassVisitor) does nothing; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant_whenLibraryClassAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();

    LibraryClass referencedClass = mock(LibraryClass.class);
    doNothing().when(referencedClass).accept(Mockito.<ClassVisitor>any());
    ClassConstant classConstant = new ClassConstant(1, referencedClass);
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 3, constantPool, 1, 1, 1);

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(referencedClass).accept(isA(ClassVisitor.class));
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then calls {@link FieldrefConstant#referencedMemberAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyRefConstant(Clazz, RefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyRefConstant(Clazz, RefConstant); when LibraryClass(); then calls referencedMemberAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyRefConstant(Clazz, RefConstant)"})
  void testVisitAnyRefConstant_whenLibraryClass_thenCallsReferencedMemberAccept() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();

    FieldrefConstant refConstant = mock(FieldrefConstant.class);
    doNothing().when(refConstant).referencedMemberAccept(Mockito.<MemberVisitor>any());
    doNothing().when(refConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitAnyRefConstant(clazz, refConstant);

    // Assert
    verify(refConstant).referencedMemberAccept(isA(MemberVisitor.class));
    verify(refConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassConstant#referencedClassAccept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); then calls referencedClassAccept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenCallsReferencedClassAccept() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();

    ClassConstant classConstant = mock(ClassConstant.class);
    doNothing().when(classConstant).referencedClassAccept(Mockito.<ClassVisitor>any());

    // Act
    accessMethodMarker.visitClassConstant(clazz, classConstant);

    // Assert
    verify(classConstant).referencedClassAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethodOptimizationInfo#setAccessesPackageCode()}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant); then calls setAccessesPackageCode()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenCallsSetAccessesPackageCode() {
    // Arrange
    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setAccessesPackageCode();

    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(programMethodOptimizationInfo);

    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();
    CodeAttribute codeAttribute = new CodeAttribute();

    accessMethodMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());
    LibraryClass clazz2 = new LibraryClass();
    ClassConstant classConstant = new ClassConstant(1, new LibraryClass());

    // Act
    accessMethodMarker.visitClassConstant(clazz2, classConstant);

    // Assert
    verify(programMethodOptimizationInfo).setAccessesPackageCode();
  }

  /**
   * Test {@link AccessMethodMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass} {@link LibraryClass#accept(ClassVisitor)} does nothing.
   *   <li>Then calls {@link LibraryClass#accept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); when LibraryClass accept(ClassVisitor) does nothing; then calls accept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_whenLibraryClassAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();

    LibraryClass referencedClass = mock(LibraryClass.class);
    doNothing().when(referencedClass).accept(Mockito.<ClassVisitor>any());
    ClassConstant classConstant = new ClassConstant(1, referencedClass);

    // Act
    accessMethodMarker.visitClassConstant(clazz, classConstant);

    // Assert
    verify(referencedClass).accept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethodOptimizationInfo#setAccessesPackageCode()}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName("Test visitAnyClass(Clazz); then calls setAccessesPackageCode()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyClass(Clazz)"})
  void testVisitAnyClass_thenCallsSetAccessesPackageCode() {
    // Arrange
    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setAccessesPackageCode();

    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(programMethodOptimizationInfo);

    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();
    CodeAttribute codeAttribute = new CodeAttribute();

    accessMethodMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());

    // Act
    accessMethodMarker.visitAnyClass(new LibraryClass());

    // Assert
    verify(programMethodOptimizationInfo).setAccessesPackageCode();
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyMember(Clazz, Member)}.
   *
   * <ul>
   *   <li>Given one.
   *   <li>When {@link LibraryField} {@link LibraryField#getAccessFlags()} return one.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyMember(Clazz, Member)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMember(Clazz, Member); given one; when LibraryField getAccessFlags() return one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyMember(Clazz, Member)"})
  void testVisitAnyMember_givenOne_whenLibraryFieldGetAccessFlagsReturnOne() {
    // Arrange
    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();

    LibraryField member = mock(LibraryField.class);
    when(member.getAccessFlags()).thenReturn(1);

    // Act
    accessMethodMarker.visitAnyMember(clazz, member);

    // Assert
    verify(member).getAccessFlags();
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyMember(Clazz, Member)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethodOptimizationInfo#setAccessesPackageCode()}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyMember(Clazz, Member)}
   */
  @Test
  @DisplayName("Test visitAnyMember(Clazz, Member); then calls setAccessesPackageCode()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyMember(Clazz, Member)"})
  void testVisitAnyMember_thenCallsSetAccessesPackageCode() {
    // Arrange
    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setAccessesPackageCode();

    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(programMethodOptimizationInfo);

    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();
    CodeAttribute codeAttribute = new CodeAttribute();

    accessMethodMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());
    LibraryClass clazz2 = new LibraryClass();

    LibraryField member = mock(LibraryField.class);
    when(member.getAccessFlags()).thenReturn(0);

    // Act
    accessMethodMarker.visitAnyMember(clazz2, member);

    // Assert
    verify(member).getAccessFlags();
    verify(programMethodOptimizationInfo).setAccessesPackageCode();
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyMember(Clazz, Member)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethodOptimizationInfo#setAccessesPrivateCode()}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyMember(Clazz, Member)}
   */
  @Test
  @DisplayName("Test visitAnyMember(Clazz, Member); then calls setAccessesPrivateCode()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyMember(Clazz, Member)"})
  void testVisitAnyMember_thenCallsSetAccessesPrivateCode() {
    // Arrange
    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setAccessesPrivateCode();

    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(programMethodOptimizationInfo);

    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();
    CodeAttribute codeAttribute = new CodeAttribute();

    accessMethodMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());
    LibraryClass clazz2 = new LibraryClass();

    LibraryField member = mock(LibraryField.class);
    when(member.getAccessFlags()).thenReturn(2);

    // Act
    accessMethodMarker.visitAnyMember(clazz2, member);

    // Assert
    verify(member).getAccessFlags();
    verify(programMethodOptimizationInfo).setAccessesPrivateCode();
  }

  /**
   * Test {@link AccessMethodMarker#visitAnyMember(Clazz, Member)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethodOptimizationInfo#setAccessesProtectedCode()}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#visitAnyMember(Clazz, Member)}
   */
  @Test
  @DisplayName("Test visitAnyMember(Clazz, Member); then calls setAccessesProtectedCode()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AccessMethodMarker.visitAnyMember(Clazz, Member)"})
  void testVisitAnyMember_thenCallsSetAccessesProtectedCode() {
    // Arrange
    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setAccessesProtectedCode();

    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(programMethodOptimizationInfo);

    AccessMethodMarker accessMethodMarker = new AccessMethodMarker();
    LibraryClass clazz = new LibraryClass();
    CodeAttribute codeAttribute = new CodeAttribute();

    accessMethodMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction());
    LibraryClass clazz2 = new LibraryClass();

    LibraryField member = mock(LibraryField.class);
    when(member.getAccessFlags()).thenReturn(4);

    // Act
    accessMethodMarker.visitAnyMember(clazz2, member);

    // Assert
    verify(member).getAccessFlags();
    verify(programMethodOptimizationInfo).setAccessesProtectedCode();
  }

  /**
   * Test {@link AccessMethodMarker#accessesPrivateCode(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#accessesPrivateCode(Method)}
   */
  @Test
  @DisplayName(
      "Test accessesPrivateCode(Method); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean AccessMethodMarker.accessesPrivateCode(Method)"})
  void testAccessesPrivateCode_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(AccessMethodMarker.accessesPrivateCode(method));
  }

  /**
   * Test {@link AccessMethodMarker#accessesPackageCode(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#accessesPackageCode(Method)}
   */
  @Test
  @DisplayName(
      "Test accessesPackageCode(Method); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean AccessMethodMarker.accessesPackageCode(Method)"})
  void testAccessesPackageCode_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(AccessMethodMarker.accessesPackageCode(method));
  }

  /**
   * Test {@link AccessMethodMarker#accessesProtectedCode(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link AccessMethodMarker#accessesProtectedCode(Method)}
   */
  @Test
  @DisplayName(
      "Test accessesProtectedCode(Method); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean AccessMethodMarker.accessesProtectedCode(Method)"})
  void testAccessesProtectedCode_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(AccessMethodMarker.accessesProtectedCode(method));
  }
}
