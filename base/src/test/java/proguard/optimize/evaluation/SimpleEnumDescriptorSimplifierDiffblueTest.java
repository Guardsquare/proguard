package proguard.optimize.evaluation;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyBoolean;
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
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableInfo;
import proguard.classfile.attribute.LocalVariableTableAttribute;
import proguard.classfile.attribute.LocalVariableTypeTableAttribute;
import proguard.classfile.attribute.SignatureAttribute;
import proguard.classfile.attribute.annotation.AnnotationDefaultAttribute;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.attribute.visitor.LocalVariableInfoVisitor;
import proguard.classfile.attribute.visitor.LocalVariableTypeInfoVisitor;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.InvokeDynamicConstant;
import proguard.classfile.constant.MethodTypeConstant;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.optimize.info.ClassOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfo;
import proguard.resources.file.ResourceFile;

class SimpleEnumDescriptorSimplifierDiffblueTest {
  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@code Name}.
   *   <li>Then calls {@link ProgramClass#constantPoolEntriesAccept(ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given 'Name'; then calls constantPoolEntriesAccept(ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void SimpleEnumDescriptorSimplifier.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenName_thenCallsConstantPoolEntriesAccept() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");
    doNothing().when(programClass).constantPoolEntriesAccept(Mockito.<ConstantVisitor>any());
    doNothing().when(programClass).fieldsAccept(Mockito.<MemberVisitor>any());
    doNothing().when(programClass).methodsAccept(Mockito.<MemberVisitor>any());

    // Act
    simpleEnumDescriptorSimplifier.visitProgramClass(programClass);

    // Assert
    verify(programClass).constantPoolEntriesAccept(isA(ConstantVisitor.class));
    verify(programClass).fieldsAccept(isA(MemberVisitor.class));
    verify(programClass).getName();
    verify(programClass).methodsAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClassOptimizationInfo#isSimpleEnum()}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitStringConstant(Clazz,
   * StringConstant)}
   */
  @Test
  @DisplayName("Test visitStringConstant(Clazz, StringConstant); then calls isSimpleEnum()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitStringConstant(Clazz, StringConstant)"
  })
  void testVisitStringConstant_thenCallsIsSimpleEnum() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 1, constantPool, 1, 1, 1);

    ProgramClassOptimizationInfo programClassOptimizationInfo =
        mock(ProgramClassOptimizationInfo.class);
    when(programClassOptimizationInfo.isSimpleEnum()).thenReturn(false);
    doNothing().when(programClassOptimizationInfo).setSimpleEnum(anyBoolean());
    programClassOptimizationInfo.setSimpleEnum(false);

    LibraryClass libraryClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    libraryClass.setProcessingInfo(programClassOptimizationInfo);
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = libraryClass;

    // Act
    simpleEnumDescriptorSimplifier.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(programClassOptimizationInfo).isSimpleEnum();
    verify(programClassOptimizationInfo).setSimpleEnum(false);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}.
   *
   * <ul>
   *   <li>Given {@code Before: [{}]}.
   *   <li>When {@link LibraryClass} {@link LibraryClass#getType(int)} return {@code Before: [{}]}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant); given 'Before: [{}]'; when LibraryClass getType(int) return 'Before: [{}]'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)"
  })
  void testVisitInvokeDynamicConstant_givenBefore_whenLibraryClassGetTypeReturnBefore() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getType(anyInt())).thenReturn("  Before: [{}]");
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    // Act
    simpleEnumDescriptorSimplifier.visitInvokeDynamicConstant(
        clazz, new InvokeDynamicConstant(1, 1, referencedClasses));

    // Assert
    verify(clazz).getType(1);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}.
   *
   * <ul>
   *   <li>Given {@code Type}.
   *   <li>When {@link LibraryClass}.
   *   <li>Then calls {@link InvokeDynamicConstant#getType(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant); given 'Type'; when LibraryClass; then calls getType(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)"
  })
  void testVisitInvokeDynamicConstant_givenType_whenLibraryClass_thenCallsGetType() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();
    LibraryClass clazz = mock(LibraryClass.class);

    InvokeDynamicConstant invokeDynamicConstant = mock(InvokeDynamicConstant.class);
    when(invokeDynamicConstant.getType(Mockito.<Clazz>any())).thenReturn("Type");

    // Act
    simpleEnumDescriptorSimplifier.visitInvokeDynamicConstant(clazz, invokeDynamicConstant);

    // Assert
    verify(invokeDynamicConstant).getType(isA(Clazz.class));
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryClass#getType(int)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant); then calls getType(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)"
  })
  void testVisitInvokeDynamicConstant_thenCallsGetType() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getType(anyInt())).thenReturn("Type");

    // Act
    simpleEnumDescriptorSimplifier.visitInvokeDynamicConstant(clazz, new InvokeDynamicConstant());

    // Assert
    verify(clazz).getType(0);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramClassOptimizationInfo#isSimpleEnum()}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitClassConstant(Clazz,
   * ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant); then calls isSimpleEnum()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitClassConstant(Clazz, ClassConstant)"
  })
  void testVisitClassConstant_thenCallsIsSimpleEnum() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass clazz = new ProgramClass(1, 1, constantPool, 1, 1, 1);

    ProgramClassOptimizationInfo programClassOptimizationInfo =
        mock(ProgramClassOptimizationInfo.class);
    when(programClassOptimizationInfo.isSimpleEnum()).thenReturn(false);
    doNothing().when(programClassOptimizationInfo).setSimpleEnum(anyBoolean());
    programClassOptimizationInfo.setSimpleEnum(false);

    LibraryClass referencedClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    referencedClass.setProcessingInfo(programClassOptimizationInfo);
    ClassConstant classConstant = new ClassConstant(1, referencedClass);

    // Act
    simpleEnumDescriptorSimplifier.visitClassConstant(clazz, classConstant);

    // Assert
    verify(programClassOptimizationInfo).isSimpleEnum();
    verify(programClassOptimizationInfo).setSimpleEnum(false);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <ul>
   *   <li>Given {@code Before: [{}]}.
   *   <li>When {@link LibraryClass} {@link LibraryClass#getString(int)} return {@code Before:
   *       [{}]}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodTypeConstant(Clazz, MethodTypeConstant); given 'Before: [{}]'; when LibraryClass getString(int) return 'Before: [{}]'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitMethodTypeConstant(Clazz, MethodTypeConstant)"
  })
  void testVisitMethodTypeConstant_givenBefore_whenLibraryClassGetStringReturnBefore() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("  Before: [{}]");
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};
    MethodTypeConstant methodTypeConstant = new MethodTypeConstant(1, referencedClasses);

    // Act
    simpleEnumDescriptorSimplifier.visitMethodTypeConstant(clazz, methodTypeConstant);

    // Assert
    verify(clazz).getString(1);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <ul>
   *   <li>Given {@code String}.
   *   <li>Then calls {@link LibraryClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodTypeConstant(Clazz, MethodTypeConstant); given 'String'; then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitMethodTypeConstant(Clazz, MethodTypeConstant)"
  })
  void testVisitMethodTypeConstant_givenString_thenCallsGetString() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");

    // Act
    simpleEnumDescriptorSimplifier.visitMethodTypeConstant(clazz, new MethodTypeConstant());

    // Assert
    verify(clazz).getString(0);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <ul>
   *   <li>Given {@code String}.
   *   <li>Then calls {@link LibraryClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodTypeConstant(Clazz, MethodTypeConstant); given 'String'; then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitMethodTypeConstant(Clazz, MethodTypeConstant)"
  })
  void testVisitMethodTypeConstant_givenString_thenCallsGetString2() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};
    MethodTypeConstant methodTypeConstant = new MethodTypeConstant(1, referencedClasses);

    // Act
    simpleEnumDescriptorSimplifier.visitMethodTypeConstant(clazz, methodTypeConstant);

    // Assert
    verify(clazz).getString(1);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitMethodTypeConstant(Clazz, MethodTypeConstant)}.
   *
   * <ul>
   *   <li>Given {@code Type}.
   *   <li>When {@link LibraryClass}.
   *   <li>Then calls {@link MethodTypeConstant#getType(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitMethodTypeConstant(Clazz,
   * MethodTypeConstant)}
   */
  @Test
  @DisplayName(
      "Test visitMethodTypeConstant(Clazz, MethodTypeConstant); given 'Type'; when LibraryClass; then calls getType(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitMethodTypeConstant(Clazz, MethodTypeConstant)"
  })
  void testVisitMethodTypeConstant_givenType_whenLibraryClass_thenCallsGetType() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();
    LibraryClass clazz = mock(LibraryClass.class);

    MethodTypeConstant methodTypeConstant = mock(MethodTypeConstant.class);
    when(methodTypeConstant.getType(Mockito.<Clazz>any())).thenReturn("Type");

    // Act
    simpleEnumDescriptorSimplifier.visitMethodTypeConstant(clazz, methodTypeConstant);

    // Assert
    verify(methodTypeConstant).getType(isA(Clazz.class));
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given ClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField_givenClassOptimizationInfo() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(new ClassOptimizationInfo());

    // Act
    simpleEnumDescriptorSimplifier.visitProgramField(
        programClass, new ProgramField(1, 1, 1, referencedClass));

    // Assert
    verify(programClass).getString(1);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given ProgramClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField_givenProgramClassOptimizationInfo() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    LibraryClass referencedClass = new LibraryClass();
    referencedClass.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act
    simpleEnumDescriptorSimplifier.visitProgramField(
        programClass, new ProgramField(1, 1, 1, referencedClass));

    // Assert
    verify(programClass).getString(1);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@code String}.
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given 'String'; then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField_givenString_thenCallsGetString() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    simpleEnumDescriptorSimplifier.visitProgramField(programClass, new ProgramField());

    // Assert
    verify(programClass).getString(0);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    // Act
    simpleEnumDescriptorSimplifier.visitProgramMethod(
        programClass, new ProgramMethod(1, 1, 1, referencedClasses));

    // Assert
    verify(programClass).getString(1);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@code Before: [{}]}.
   *   <li>When {@link ProgramClass} {@link ProgramClass#getString(int)} return {@code Before:
   *       [{}]}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given 'Before: [{}]'; when ProgramClass getString(int) return 'Before: [{}]'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_givenBefore_whenProgramClassGetStringReturnBefore() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("  Before: [{}]");
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    // Act
    simpleEnumDescriptorSimplifier.visitProgramMethod(
        programClass, new ProgramMethod(1, 1, 1, referencedClasses));

    // Assert
    verify(programClass).getString(1);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@code String}.
   *   <li>When {@link ProgramMethod#ProgramMethod()}.
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given 'String'; when ProgramMethod(); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_givenString_whenProgramMethod_thenCallsGetString() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    simpleEnumDescriptorSimplifier.visitProgramMethod(programClass, new ProgramMethod());

    // Assert
    verify(programClass).getString(0);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link AnnotationDefaultAttribute#accept(Clazz, Method, AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then calls accept(Clazz, Method, AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_thenCallsAccept() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    AnnotationDefaultAttribute annotationDefaultAttribute = mock(AnnotationDefaultAttribute.class);
    doNothing()
        .when(annotationDefaultAttribute)
        .accept(Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());
    Attribute[] attributes = new Attribute[] {annotationDefaultAttribute};
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    ProgramMethod programMethod = new ProgramMethod(1, 1, 1, 1, attributes, referencedClasses);

    // Act
    simpleEnumDescriptorSimplifier.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programClass).getString(1);
    verify(annotationDefaultAttribute)
        .accept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link CodeAttribute#attributesAccept(Clazz, Method, AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then calls attributesAccept(Clazz, Method, AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute_thenCallsAttributesAccept() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing()
        .when(codeAttribute)
        .attributesAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());

    // Act
    simpleEnumDescriptorSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(codeAttribute)
        .attributesAccept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitLocalVariableTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTableAttribute)}.
   *
   * <ul>
   *   <li>Given {@code Descriptor}.
   *   <li>Then calls {@link LocalVariableInfo#getDescriptor(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * SimpleEnumDescriptorSimplifier#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute); given 'Descriptor'; then calls getDescriptor(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute_givenDescriptor_thenCallsGetDescriptor() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    LocalVariableInfo localVariableInfo = mock(LocalVariableInfo.class);
    when(localVariableInfo.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    LocalVariableInfo[] localVariableTable = new LocalVariableInfo[] {localVariableInfo};

    // Act
    simpleEnumDescriptorSimplifier.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, new LocalVariableTableAttribute(1, 1, localVariableTable));

    // Assert
    verify(localVariableInfo).getDescriptor(isA(Clazz.class));
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitLocalVariableTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTableAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link LocalVariableTableAttribute#localVariablesAccept(Clazz, Method,
   *       CodeAttribute, LocalVariableInfoVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * SimpleEnumDescriptorSimplifier#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute); then calls localVariablesAccept(Clazz, Method, CodeAttribute, LocalVariableInfoVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute_thenCallsLocalVariablesAccept() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();
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
    simpleEnumDescriptorSimplifier.visitLocalVariableTableAttribute(
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
   * Test {@link SimpleEnumDescriptorSimplifier#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link LocalVariableTypeTableAttribute#localVariablesAccept(Clazz, Method,
   *       CodeAttribute, LocalVariableTypeInfoVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link
   * SimpleEnumDescriptorSimplifier#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute); then calls localVariablesAccept(Clazz, Method, CodeAttribute, LocalVariableTypeInfoVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute_thenCallsLocalVariablesAccept() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();
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
    simpleEnumDescriptorSimplifier.visitLocalVariableTypeTableAttribute(
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
   * Test {@link SimpleEnumDescriptorSimplifier#visitSignatureAttribute(Clazz, SignatureAttribute)}
   * with {@code clazz}, {@code signatureAttribute}.
   *
   * <ul>
   *   <li>Given {@code Before: [{}]}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'; given 'Before: [{}]'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitSignatureAttribute(Clazz, SignatureAttribute)"
  })
  void testVisitSignatureAttributeWithClazzSignatureAttribute_givenBefore() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("  Before: [{}]");
    SignatureAttribute signatureAttribute = new SignatureAttribute(1, 1);
    signatureAttribute.referencedClasses = new Clazz[] {new LibraryClass()};

    // Act
    simpleEnumDescriptorSimplifier.visitSignatureAttribute(clazz, signatureAttribute);

    // Assert
    verify(clazz).getString(1);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitSignatureAttribute(Clazz, SignatureAttribute)}
   * with {@code clazz}, {@code signatureAttribute}.
   *
   * <ul>
   *   <li>Then calls {@link SignatureAttribute#getSignature(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'; then calls getSignature(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitSignatureAttribute(Clazz, SignatureAttribute)"
  })
  void testVisitSignatureAttributeWithClazzSignatureAttribute_thenCallsGetSignature() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();
    LibraryClass clazz = mock(LibraryClass.class);

    SignatureAttribute signatureAttribute = mock(SignatureAttribute.class);
    when(signatureAttribute.getSignature(Mockito.<Clazz>any())).thenReturn("Signature");

    // Act
    simpleEnumDescriptorSimplifier.visitSignatureAttribute(clazz, signatureAttribute);

    // Assert
    verify(signatureAttribute).getSignature(isA(Clazz.class));
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitSignatureAttribute(Clazz, SignatureAttribute)}
   * with {@code clazz}, {@code signatureAttribute}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'; then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitSignatureAttribute(Clazz, SignatureAttribute)"
  })
  void testVisitSignatureAttributeWithClazzSignatureAttribute_thenCallsGetString() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");

    // Act
    simpleEnumDescriptorSimplifier.visitSignatureAttribute(clazz, new SignatureAttribute());

    // Assert
    verify(clazz).getString(0);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitSignatureAttribute(Clazz, SignatureAttribute)}
   * with {@code clazz}, {@code signatureAttribute}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'; then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitSignatureAttribute(Clazz, SignatureAttribute)"
  })
  void testVisitSignatureAttributeWithClazzSignatureAttribute_thenCallsGetString2() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");
    SignatureAttribute signatureAttribute = new SignatureAttribute(1, 1);
    signatureAttribute.referencedClasses = new Clazz[] {new LibraryClass()};

    // Act
    simpleEnumDescriptorSimplifier.visitSignatureAttribute(clazz, signatureAttribute);

    // Assert
    verify(clazz).getString(1);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitLocalVariableInfo(Clazz, Method, CodeAttribute,
   * LocalVariableInfo)}.
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitLocalVariableInfo(Clazz,
   * Method, CodeAttribute, LocalVariableInfo)}
   */
  @Test
  @DisplayName("Test visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)"
  })
  void testVisitLocalVariableInfo() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    ProgramClassOptimizationInfo programClassOptimizationInfo = new ProgramClassOptimizationInfo();
    programClassOptimizationInfo.setSimpleEnum(false);

    LibraryClass libraryClass = new LibraryClass(1, "String", "String");
    libraryClass.setProcessingInfo(programClassOptimizationInfo);
    LocalVariableInfo localVariableInfo = new LocalVariableInfo(1, 3, 1, 1, 1);
    localVariableInfo.referencedClass = libraryClass;

    // Act
    simpleEnumDescriptorSimplifier.visitLocalVariableInfo(
        clazz, method, codeAttribute, localVariableInfo);

    // Assert
    verify(clazz).getString(1);
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitLocalVariableInfo(Clazz, Method, CodeAttribute,
   * LocalVariableInfo)}.
   *
   * <ul>
   *   <li>Given {@code Descriptor}.
   *   <li>Then calls {@link LocalVariableInfo#getDescriptor(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitLocalVariableInfo(Clazz,
   * Method, CodeAttribute, LocalVariableInfo)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo); given 'Descriptor'; then calls getDescriptor(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)"
  })
  void testVisitLocalVariableInfo_givenDescriptor_thenCallsGetDescriptor() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();
    LibraryClass clazz = mock(LibraryClass.class);
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    LocalVariableInfo localVariableInfo = mock(LocalVariableInfo.class);
    when(localVariableInfo.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");

    // Act
    simpleEnumDescriptorSimplifier.visitLocalVariableInfo(
        clazz, method, codeAttribute, localVariableInfo);

    // Assert
    verify(localVariableInfo).getDescriptor(isA(Clazz.class));
  }

  /**
   * Test {@link SimpleEnumDescriptorSimplifier#visitLocalVariableInfo(Clazz, Method, CodeAttribute,
   * LocalVariableInfo)}.
   *
   * <ul>
   *   <li>Given {@code String}.
   *   <li>When {@link LocalVariableInfo#LocalVariableInfo()}.
   *   <li>Then calls {@link LibraryClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link SimpleEnumDescriptorSimplifier#visitLocalVariableInfo(Clazz,
   * Method, CodeAttribute, LocalVariableInfo)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo); given 'String'; when LocalVariableInfo(); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SimpleEnumDescriptorSimplifier.visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)"
  })
  void testVisitLocalVariableInfo_givenString_whenLocalVariableInfo_thenCallsGetString() {
    // Arrange
    SimpleEnumDescriptorSimplifier simpleEnumDescriptorSimplifier =
        new SimpleEnumDescriptorSimplifier();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    simpleEnumDescriptorSimplifier.visitLocalVariableInfo(
        clazz, method, codeAttribute, new LocalVariableInfo());

    // Assert
    verify(clazz).getString(0);
  }
}
