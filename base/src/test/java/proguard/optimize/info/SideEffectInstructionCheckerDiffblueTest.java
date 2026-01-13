package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.instruction.BranchInstruction;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.LookUpSwitchInstruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.instruction.VariableInstruction;
import proguard.classfile.visitor.MemberVisitor;

class SideEffectInstructionCheckerDiffblueTest {
  /**
   * Test {@link SideEffectInstructionChecker#hasSideEffects(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>When {@link BranchInstruction#BranchInstruction()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#hasSideEffects(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test hasSideEffects(Clazz, Method, CodeAttribute, int, Instruction); when BranchInstruction(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean SideEffectInstructionChecker.hasSideEffects(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testHasSideEffects_whenBranchInstruction_thenReturnFalse() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act and Assert
    assertFalse(
        sideEffectInstructionChecker.hasSideEffects(
            clazz, method, codeAttribute, 2, new BranchInstruction()));
  }

  /**
   * Test {@link SideEffectInstructionChecker#hasSideEffects(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>When {@link ConstantInstruction#ConstantInstruction()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#hasSideEffects(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test hasSideEffects(Clazz, Method, CodeAttribute, int, Instruction); when ConstantInstruction(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean SideEffectInstructionChecker.hasSideEffects(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testHasSideEffects_whenConstantInstruction_thenReturnFalse() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act and Assert
    assertFalse(
        sideEffectInstructionChecker.hasSideEffects(
            clazz, method, codeAttribute, 2, new ConstantInstruction()));
  }

  /**
   * Test {@link SideEffectInstructionChecker#hasSideEffects(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>When {@link LookUpSwitchInstruction#LookUpSwitchInstruction()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#hasSideEffects(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test hasSideEffects(Clazz, Method, CodeAttribute, int, Instruction); when LookUpSwitchInstruction(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean SideEffectInstructionChecker.hasSideEffects(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testHasSideEffects_whenLookUpSwitchInstruction_thenReturnFalse() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act and Assert
    assertFalse(
        sideEffectInstructionChecker.hasSideEffects(
            clazz, method, codeAttribute, 2, new LookUpSwitchInstruction()));
  }

  /**
   * Test {@link SideEffectInstructionChecker#hasSideEffects(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>When {@link SimpleInstruction#SimpleInstruction()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#hasSideEffects(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test hasSideEffects(Clazz, Method, CodeAttribute, int, Instruction); when SimpleInstruction(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean SideEffectInstructionChecker.hasSideEffects(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testHasSideEffects_whenSimpleInstruction_thenReturnFalse() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act and Assert
    assertFalse(
        sideEffectInstructionChecker.hasSideEffects(
            clazz, method, codeAttribute, 2, new SimpleInstruction()));
  }

  /**
   * Test {@link SideEffectInstructionChecker#hasSideEffects(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>When {@link VariableInstruction#VariableInstruction()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#hasSideEffects(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test hasSideEffects(Clazz, Method, CodeAttribute, int, Instruction); when VariableInstruction(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean SideEffectInstructionChecker.hasSideEffects(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testHasSideEffects_whenVariableInstruction_thenReturnFalse() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act and Assert
    assertFalse(
        sideEffectInstructionChecker.hasSideEffects(
            clazz, method, codeAttribute, 2, new VariableInstruction()));
  }

  /**
   * Test {@link SideEffectInstructionChecker#visitFieldrefConstant(Clazz, FieldrefConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link FieldrefConstant#referencedFieldAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#visitFieldrefConstant(Clazz,
   * FieldrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitFieldrefConstant(Clazz, FieldrefConstant); then calls referencedFieldAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SideEffectInstructionChecker.visitFieldrefConstant(Clazz, FieldrefConstant)"
  })
  void testVisitFieldrefConstant_thenCallsReferencedFieldAccept() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);
    LibraryClass clazz = new LibraryClass();

    FieldrefConstant fieldrefConstant = mock(FieldrefConstant.class);
    doNothing().when(fieldrefConstant).referencedFieldAccept(Mockito.<MemberVisitor>any());

    // Act
    sideEffectInstructionChecker.visitFieldrefConstant(clazz, fieldrefConstant);

    // Assert
    verify(fieldrefConstant).referencedFieldAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link SideEffectInstructionChecker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); given MethodOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SideEffectInstructionChecker.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_givenMethodOptimizationInfo() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);
    LibraryClass clazz = new LibraryClass();

    LibraryClass referencedClass = mock(LibraryClass.class);
    doNothing().when(referencedClass).addSubClass(Mockito.<Clazz>any());
    referencedClass.addSubClass(new LibraryClass());

    LibraryMethod referencedMethod = new LibraryMethod();
    referencedMethod.setProcessingInfo(new MethodOptimizationInfo());

    // Act
    sideEffectInstructionChecker.visitAnyMethodrefConstant(
        clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, referencedMethod));

    // Assert
    verify(referencedClass).addSubClass(isA(Clazz.class));
  }

  /**
   * Test {@link SideEffectInstructionChecker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link MethodOptimizationInfo#hasNoSideEffects()}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); then calls hasNoSideEffects()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SideEffectInstructionChecker.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_thenCallsHasNoSideEffects() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);
    LibraryClass clazz = new LibraryClass();

    LibraryClass referencedClass = mock(LibraryClass.class);
    doNothing().when(referencedClass).addSubClass(Mockito.<Clazz>any());
    referencedClass.addSubClass(new LibraryClass());

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasNoSideEffects()).thenReturn(true);

    LibraryMethod referencedMethod = new LibraryMethod();
    referencedMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    sideEffectInstructionChecker.visitAnyMethodrefConstant(
        clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, referencedMethod));

    // Assert
    verify(referencedClass).addSubClass(isA(Clazz.class));
    verify(methodOptimizationInfo).hasNoSideEffects();
  }

  /**
   * Test {@link SideEffectInstructionChecker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link InterfaceMethodrefConstant#referencedMethodAccept(MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); then calls referencedMethodAccept(MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SideEffectInstructionChecker.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_thenCallsReferencedMethodAccept() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);
    LibraryClass clazz = new LibraryClass();

    InterfaceMethodrefConstant anyMethodrefConstant = mock(InterfaceMethodrefConstant.class);
    doNothing().when(anyMethodrefConstant).referencedMethodAccept(Mockito.<MemberVisitor>any());

    // Act
    sideEffectInstructionChecker.visitAnyMethodrefConstant(clazz, anyMethodrefConstant);

    // Assert
    verify(anyMethodrefConstant).referencedMethodAccept(isA(MemberVisitor.class));
  }

  /**
   * Test {@link SideEffectInstructionChecker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>When {@link LibraryMethod} {@link LibraryMethod#accept(Clazz, MemberVisitor)} does
   *       nothing.
   *   <li>Then calls {@link LibraryMethod#accept(Clazz, MemberVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); when LibraryMethod accept(Clazz, MemberVisitor) does nothing; then calls accept(Clazz, MemberVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SideEffectInstructionChecker.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_whenLibraryMethodAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod referencedMethod = mock(LibraryMethod.class);
    doNothing().when(referencedMethod).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());

    // Act
    sideEffectInstructionChecker.visitAnyMethodrefConstant(
        clazz, new InterfaceMethodrefConstant(1, 1, new LibraryClass(), referencedMethod));

    // Assert
    verify(referencedMethod).accept(isA(Clazz.class), isA(MemberVisitor.class));
  }

  /**
   * Test {@link SideEffectInstructionChecker#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@code String}.
   *   <li>When {@link ProgramField#ProgramField()}.
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given 'String'; when ProgramField(); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SideEffectInstructionChecker.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField_givenString_whenProgramField_thenCallsGetString() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    sideEffectInstructionChecker.visitProgramField(programClass, new ProgramField());

    // Assert
    verify(programClass).getString(0);
  }

  /**
   * Test {@link SideEffectInstructionChecker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} {@link MethodOptimizationInfo#hasSideEffects()}
   *       return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given MethodOptimizationInfo hasSideEffects() return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SideEffectInstructionChecker.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_givenMethodOptimizationInfoHasSideEffectsReturnTrue() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);
    ProgramClass programClass = new ProgramClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasSideEffects()).thenReturn(true);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    sideEffectInstructionChecker.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(methodOptimizationInfo).hasSideEffects();
  }

  /**
   * Test {@link SideEffectInstructionChecker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@code String}.
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given 'String'; then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SideEffectInstructionChecker.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_givenString_thenCallsGetString() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasSideEffects()).thenReturn(false);

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    sideEffectInstructionChecker.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programClass).getString(0);
    verify(methodOptimizationInfo).hasSideEffects();
  }

  /**
   * Test {@link SideEffectInstructionChecker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethod#getAccessFlags()}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod); then calls getAccessFlags()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SideEffectInstructionChecker.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_thenCallsGetAccessFlags() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    sideEffectInstructionChecker.visitSimpleInstruction(
        clazz, method, codeAttribute, 2, new SimpleInstruction());

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.hasNoSideEffects()).thenReturn(true);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getProcessingInfo()).thenReturn(classOptimizationInfo);

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasSideEffects()).thenReturn(false);

    ProgramMethod programMethod = mock(ProgramMethod.class);
    when(programMethod.getAccessFlags()).thenReturn(-1);
    when(programMethod.getProcessingInfo()).thenReturn(methodOptimizationInfo);
    doNothing().when(programMethod).setProcessingInfo(Mockito.<Object>any());
    programMethod.setProcessingInfo(mock(MethodOptimizationInfo.class));

    // Act
    sideEffectInstructionChecker.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programMethod).getAccessFlags();
    verify(classOptimizationInfo).hasNoSideEffects();
    verify(methodOptimizationInfo).hasSideEffects();
    verify(programClass).getProcessingInfo();
    verify(programMethod, atLeast(1)).getProcessingInfo();
    verify(programMethod).setProcessingInfo(isA(Object.class));
  }

  /**
   * Test {@link SideEffectInstructionChecker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} {@link MethodOptimizationInfo#hasNoSideEffects()}
   *       return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); given MethodOptimizationInfo hasNoSideEffects() return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SideEffectInstructionChecker.visitLibraryMethod(LibraryClass, LibraryMethod)"
  })
  void testVisitLibraryMethod_givenMethodOptimizationInfoHasNoSideEffectsReturnFalse() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);
    LibraryClass libraryClass = new LibraryClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasNoSideEffects()).thenReturn(false);

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    sideEffectInstructionChecker.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    verify(methodOptimizationInfo).hasNoSideEffects();
  }

  /**
   * Test {@link SideEffectInstructionChecker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} {@link MethodOptimizationInfo#hasNoSideEffects()}
   *       return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link SideEffectInstructionChecker#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); given MethodOptimizationInfo hasNoSideEffects() return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SideEffectInstructionChecker.visitLibraryMethod(LibraryClass, LibraryMethod)"
  })
  void testVisitLibraryMethod_givenMethodOptimizationInfoHasNoSideEffectsReturnTrue() {
    // Arrange
    SideEffectInstructionChecker sideEffectInstructionChecker =
        new SideEffectInstructionChecker(true, true, true);
    LibraryClass libraryClass = new LibraryClass();

    MethodOptimizationInfo methodOptimizationInfo = mock(MethodOptimizationInfo.class);
    when(methodOptimizationInfo.hasNoSideEffects()).thenReturn(true);

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo(methodOptimizationInfo);

    // Act
    sideEffectInstructionChecker.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    verify(methodOptimizationInfo).hasNoSideEffects();
  }
}
