package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyBoolean;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Member;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.constant.InvokeDynamicConstant;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.resources.file.ResourceFile;

@ExtendWith(MockitoExtension.class)
class ParameterEscapeMarkerDiffblueTest {
  @Mock private Clazz clazz;

  @Mock private Method method;

  @InjectMocks private ParameterEscapeMarker parameterEscapeMarker;

  /**
   * Test {@link ParameterEscapeMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterEscapeMarker.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod() {
    // Arrange
    ParameterEscapeMarker parameterEscapeMarker = new ParameterEscapeMarker();
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.modifiesAnything()).thenReturn(false);
    when(methodOptimizationInfo.getEscapingParameters()).thenReturn(-1L);
    when(methodOptimizationInfo.getModifiedParameters()).thenReturn(-1L);
    when(methodOptimizationInfo.getReturnedParameters()).thenReturn(-1L);
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    ProgramMethod programMethod = new ProgramMethod(256, 1, 1, referencedClasses);
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    parameterEscapeMarker.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).getEscapingParameters();
    verify(methodOptimizationInfo).getModifiedParameters();
    verify(methodOptimizationInfo).getReturnedParameters();
    verify(methodOptimizationInfo).modifiesAnything();
  }

  /**
   * Test {@link ParameterEscapeMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterEscapeMarker.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod2() {
    // Arrange
    ParameterEscapeMarker parameterEscapeMarker = new ParameterEscapeMarker();
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.modifiesAnything()).thenReturn(false);
    when(methodOptimizationInfo.getEscapingParameters()).thenReturn(0L);
    when(methodOptimizationInfo.getModifiedParameters()).thenReturn(1L);
    when(methodOptimizationInfo.getReturnedParameters()).thenReturn(-1L);
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    ProgramMethod programMethod = new ProgramMethod(256, 1, 1, referencedClasses);
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    parameterEscapeMarker.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).getEscapingParameters();
    verify(methodOptimizationInfo).getModifiedParameters();
    verify(methodOptimizationInfo).getReturnedParameters();
    verify(methodOptimizationInfo).modifiesAnything();
  }

  /**
   * Test {@link ParameterEscapeMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterEscapeMarker.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod3() {
    // Arrange
    ParameterEscapeMarker parameterEscapeMarker = new ParameterEscapeMarker();
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.getModifiedParameters()).thenReturn(0L);
    when(methodOptimizationInfo.modifiesAnything()).thenReturn(true);
    when(methodOptimizationInfo.getEscapingParameters()).thenReturn(1L);
    when(methodOptimizationInfo.getReturnedParameters()).thenReturn(1L);
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    ProgramMethod programMethod = new ProgramMethod(256, 1, 1, referencedClasses);
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    parameterEscapeMarker.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).getEscapingParameters();
    verify(methodOptimizationInfo).getModifiedParameters();
    verify(methodOptimizationInfo).getReturnedParameters();
    verify(methodOptimizationInfo).modifiesAnything();
  }

  /**
   * Test {@link ParameterEscapeMarker#visitConstantInstruction(Clazz, Method, CodeAttribute, int,
   * ConstantInstruction)}.
   *
   * <ul>
   *   <li>Then calls {@link Clazz#constantPoolEntryAccept(int, ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitConstantInstruction(Clazz, Method,
   * CodeAttribute, int, ConstantInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction); then calls constantPoolEntryAccept(int, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterEscapeMarker.visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)"
  })
  void testVisitConstantInstruction_thenCallsConstantPoolEntryAccept() {
    // Arrange
    doNothing().when(clazz).constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    parameterEscapeMarker.visitConstantInstruction(
        clazz, method, codeAttribute, 2, new ConstantInstruction((byte) -78, 1));

    // Assert
    verify(clazz).constantPoolEntryAccept(eq(1), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link ParameterEscapeMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Given {@link Clazz} {@link Clazz#extendsOrImplements(Clazz)} return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); given Clazz extendsOrImplements(Clazz) return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterEscapeMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_givenClazzExtendsOrImplementsReturnTrue() {
    // Arrange
    when(clazz.extendsOrImplements(Mockito.<Clazz>any())).thenReturn(true);
    when(clazz.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = clazz;

    // Act
    parameterEscapeMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(clazz).extendsOrImplements(isA(Clazz.class));
    verify(clazz).getProcessingInfo();
  }

  /**
   * Test {@link ParameterEscapeMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link Method#getProcessingInfo()}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName("Test visitStringConstant(Clazz, StringConstant); then calls getProcessingInfo()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterEscapeMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenCallsGetProcessingInfo() {
    // Arrange
    when(method.getProcessingInfo()).thenReturn(new MethodOptimizationInfo());
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = null;

    // Act
    parameterEscapeMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(method, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link ParameterEscapeMarker#visitStringConstant(Clazz, StringConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link Clazz#hierarchyAccept(boolean, boolean, boolean, boolean,
   *       ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitStringConstant(Clazz, StringConstant)}
   */
  @Test
  @DisplayName(
      "Test visitStringConstant(Clazz, StringConstant); then calls hierarchyAccept(boolean, boolean, boolean, boolean, ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterEscapeMarker.visitStringConstant(Clazz, StringConstant)"})
  void testVisitStringConstant_thenCallsHierarchyAccept() {
    // Arrange
    when(clazz.extendsOrImplements(Mockito.<Clazz>any())).thenReturn(false);
    doNothing()
        .when(clazz)
        .hierarchyAccept(
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), Mockito.<ClassVisitor>any());
    when(clazz.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());
    StringConstant stringConstant = new StringConstant(1, new ResourceFile("foo.txt", 3L));
    stringConstant.referencedClass = clazz;

    // Act
    parameterEscapeMarker.visitStringConstant(clazz, stringConstant);

    // Assert
    verify(clazz).extendsOrImplements(isA(Clazz.class));
    verify(clazz, atLeast(1))
        .hierarchyAccept(eq(true), eq(true), eq(true), eq(false), Mockito.<ClassVisitor>any());
    verify(clazz).getProcessingInfo();
  }

  /**
   * Test {@link ParameterEscapeMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link Clazz#extendsOrImplements(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); then calls extendsOrImplements(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterEscapeMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenCallsExtendsOrImplements() {
    // Arrange
    when(clazz.extendsOrImplements(Mockito.<Clazz>any())).thenReturn(true);
    when(clazz.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());

    // Act
    parameterEscapeMarker.visitClassConstant(clazz, new ClassConstant(1, clazz));

    // Assert
    verify(clazz).extendsOrImplements(isA(Clazz.class));
    verify(clazz).getProcessingInfo();
  }

  /**
   * Test {@link ParameterEscapeMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link Method#getProcessingInfo()}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant); then calls getProcessingInfo()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterEscapeMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenCallsGetProcessingInfo() {
    // Arrange
    when(method.getProcessingInfo()).thenReturn(new MethodOptimizationInfo());
    ClassConstant classConstant = new ClassConstant(1, null);

    // Act
    parameterEscapeMarker.visitClassConstant(clazz, classConstant);

    // Assert
    verify(method, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link ParameterEscapeMarker#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link Clazz#hierarchyAccept(boolean, boolean, boolean, boolean,
   *       ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); then calls hierarchyAccept(boolean, boolean, boolean, boolean, ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterEscapeMarker.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenCallsHierarchyAccept() {
    // Arrange
    when(clazz.extendsOrImplements(Mockito.<Clazz>any())).thenReturn(false);
    doNothing()
        .when(clazz)
        .hierarchyAccept(
            anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean(), Mockito.<ClassVisitor>any());
    when(clazz.getProcessingInfo()).thenReturn(new ClassOptimizationInfo());

    // Act
    parameterEscapeMarker.visitClassConstant(clazz, new ClassConstant(1, clazz));

    // Assert
    verify(clazz).extendsOrImplements(isA(Clazz.class));
    verify(clazz, atLeast(1))
        .hierarchyAccept(eq(true), eq(true), eq(true), eq(false), Mockito.<ClassVisitor>any());
    verify(clazz).getProcessingInfo();
  }

  /**
   * Test {@link ParameterEscapeMarker#visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link Method#getProcessingInfo()}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitInvokeDynamicConstant(Clazz,
   * InvokeDynamicConstant)}
   */
  @Test
  @DisplayName(
      "Test visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant); then calls getProcessingInfo()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterEscapeMarker.visitInvokeDynamicConstant(Clazz, InvokeDynamicConstant)"
  })
  void testVisitInvokeDynamicConstant_thenCallsGetProcessingInfo() {
    // Arrange
    when(method.getProcessingInfo()).thenReturn(new MethodOptimizationInfo());

    // Act
    parameterEscapeMarker.visitInvokeDynamicConstant(clazz, new InvokeDynamicConstant());

    // Assert
    verify(method, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link ParameterEscapeMarker#visitFieldrefConstant(Clazz, FieldrefConstant)}.
   *
   * <ul>
   *   <li>When {@link Clazz}.
   *   <li>Then calls {@link Clazz#constantPoolEntryAccept(int, ConstantVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitFieldrefConstant(Clazz,
   * FieldrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitFieldrefConstant(Clazz, FieldrefConstant); when Clazz; then calls constantPoolEntryAccept(int, ConstantVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterEscapeMarker.visitFieldrefConstant(Clazz, FieldrefConstant)"})
  void testVisitFieldrefConstant_whenClazz_thenCallsConstantPoolEntryAccept() {
    // Arrange
    doNothing().when(clazz).constantPoolEntryAccept(anyInt(), Mockito.<ConstantVisitor>any());

    // Act
    parameterEscapeMarker.visitFieldrefConstant(clazz, new FieldrefConstant());

    // Assert
    verify(clazz).constantPoolEntryAccept(eq(0), isA(ConstantVisitor.class));
  }

  /**
   * Test {@link ParameterEscapeMarker#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then calls {@link Method#accept(Clazz, MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); given MethodOptimizationInfo (default constructor); then calls accept(Clazz, MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterEscapeMarker.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_givenMethodOptimizationInfo_thenCallsAccept() {
    // Arrange
    doNothing().when(method).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
    when(method.getProcessingInfo()).thenReturn(new MethodOptimizationInfo());

    // Act
    parameterEscapeMarker.visitAnyMethodrefConstant(
        clazz, new InterfaceMethodrefConstant(1, 1, clazz, method));

    // Assert
    verify(method).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(method, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link ParameterEscapeMarker#visitParameter(Clazz, Member, int, int, int, int, String,
   * Clazz)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} {@link MethodOptimizationInfo#returnsParameter(int)}
   *       return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitParameter(Clazz, Member, int, int, int,
   * int, String, Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitParameter(Clazz, Member, int, int, int, int, String, Clazz); given MethodOptimizationInfo returnsParameter(int) return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterEscapeMarker.visitParameter(Clazz, Member, int, int, int, int, String, Clazz)"
  })
  void testVisitParameter_givenMethodOptimizationInfoReturnsParameterReturnTrue() {
    // Arrange
    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.isParameterEscaping(anyInt())).thenReturn(false);
    when(methodOptimizationInfo.isParameterModified(anyInt())).thenReturn(false);
    when(methodOptimizationInfo.returnsParameter(anyInt())).thenReturn(true);

    LibraryMethod member = mock(LibraryMethod.class);
    when(member.getProcessingInfo()).thenReturn(methodOptimizationInfo);

    // Act
    parameterEscapeMarker.visitParameter(clazz, member, 1, 3, 1, 3, "Parameter Type", clazz);

    // Assert
    verify(methodOptimizationInfo).isParameterEscaping(1);
    verify(methodOptimizationInfo).isParameterModified(1);
    verify(methodOptimizationInfo, atLeast(1)).returnsParameter(1);
    verify(member, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link ParameterEscapeMarker#visitParameter(Clazz, Member, int, int, int, int, String,
   * Clazz)}.
   *
   * <ul>
   *   <li>Then calls {@link MethodOptimizationInfo#isParameterEscaping(int)}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#visitParameter(Clazz, Member, int, int, int,
   * int, String, Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitParameter(Clazz, Member, int, int, int, int, String, Clazz); then calls isParameterEscaping(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterEscapeMarker.visitParameter(Clazz, Member, int, int, int, int, String, Clazz)"
  })
  void testVisitParameter_thenCallsIsParameterEscaping() {
    // Arrange
    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.isParameterEscaping(anyInt())).thenReturn(false);
    when(methodOptimizationInfo.isParameterModified(anyInt())).thenReturn(false);
    when(methodOptimizationInfo.returnsParameter(anyInt())).thenReturn(false);

    LibraryMethod member = mock(LibraryMethod.class);
    when(member.getProcessingInfo()).thenReturn(methodOptimizationInfo);

    // Act
    parameterEscapeMarker.visitParameter(clazz, member, 1, 3, 1, 3, "Parameter Type", clazz);

    // Assert
    verify(methodOptimizationInfo).isParameterEscaping(1);
    verify(methodOptimizationInfo).isParameterModified(1);
    verify(methodOptimizationInfo, atLeast(1)).returnsParameter(1);
    verify(member, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link ParameterEscapeMarker#isParameterEscaping(Method, int)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#isParameterEscaping(Method, int)}
   */
  @Test
  @DisplayName(
      "Test isParameterEscaping(Method, int); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ParameterEscapeMarker.isParameterEscaping(Method, int)"})
  void testIsParameterEscaping_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(ParameterEscapeMarker.isParameterEscaping(method, 1));
  }

  /**
   * Test {@link ParameterEscapeMarker#getEscapingParameters(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return minus one.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#getEscapingParameters(Method)}
   */
  @Test
  @DisplayName(
      "Test getEscapingParameters(Method); given MethodOptimizationInfo (default constructor); then return minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long ParameterEscapeMarker.getEscapingParameters(Method)"})
  void testGetEscapingParameters_givenMethodOptimizationInfo_thenReturnMinusOne() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertEquals(-1L, ParameterEscapeMarker.getEscapingParameters(method));
  }

  /**
   * Test {@link ParameterEscapeMarker#isParameterReturned(Method, int)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#isParameterReturned(Method, int)}
   */
  @Test
  @DisplayName(
      "Test isParameterReturned(Method, int); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ParameterEscapeMarker.isParameterReturned(Method, int)"})
  void testIsParameterReturned_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(ParameterEscapeMarker.isParameterReturned(method, 1));
  }

  /**
   * Test {@link ParameterEscapeMarker#getReturnedParameters(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return minus one.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#getReturnedParameters(Method)}
   */
  @Test
  @DisplayName(
      "Test getReturnedParameters(Method); given MethodOptimizationInfo (default constructor); then return minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long ParameterEscapeMarker.getReturnedParameters(Method)"})
  void testGetReturnedParameters_givenMethodOptimizationInfo_thenReturnMinusOne() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertEquals(-1L, ParameterEscapeMarker.getReturnedParameters(method));
  }

  /**
   * Test {@link ParameterEscapeMarker#returnsNewInstances(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#returnsNewInstances(Method)}
   */
  @Test
  @DisplayName(
      "Test returnsNewInstances(Method); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ParameterEscapeMarker.returnsNewInstances(Method)"})
  void testReturnsNewInstances_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(ParameterEscapeMarker.returnsNewInstances(method));
  }

  /**
   * Test {@link ParameterEscapeMarker#returnsExternalValues(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#returnsExternalValues(Method)}
   */
  @Test
  @DisplayName(
      "Test returnsExternalValues(Method); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ParameterEscapeMarker.returnsExternalValues(Method)"})
  void testReturnsExternalValues_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(ParameterEscapeMarker.returnsExternalValues(method));
  }

  /**
   * Test {@link ParameterEscapeMarker#isParameterModified(Method, int)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#isParameterModified(Method, int)}
   */
  @Test
  @DisplayName(
      "Test isParameterModified(Method, int); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ParameterEscapeMarker.isParameterModified(Method, int)"})
  void testIsParameterModified_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(ParameterEscapeMarker.isParameterModified(method, 1));
  }

  /**
   * Test {@link ParameterEscapeMarker#getModifiedParameters(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return minus one.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#getModifiedParameters(Method)}
   */
  @Test
  @DisplayName(
      "Test getModifiedParameters(Method); given MethodOptimizationInfo (default constructor); then return minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long ParameterEscapeMarker.getModifiedParameters(Method)"})
  void testGetModifiedParameters_givenMethodOptimizationInfo_thenReturnMinusOne() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertEquals(-1L, ParameterEscapeMarker.getModifiedParameters(method));
  }

  /**
   * Test {@link ParameterEscapeMarker#modifiesAnything(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapeMarker#modifiesAnything(Method)}
   */
  @Test
  @DisplayName(
      "Test modifiesAnything(Method); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ParameterEscapeMarker.modifiesAnything(Method)"})
  void testModifiesAnything_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(ParameterEscapeMarker.modifiesAnything(method));
  }
}
