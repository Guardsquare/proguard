package proguard.optimize.peephole;

import static org.mockito.ArgumentMatchers.eq;
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
import proguard.classfile.LibraryField;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.optimize.info.ClassOptimizationInfo;

class NoConstructorReferenceReplacerDiffblueTest {
  /**
   * Test {@link NoConstructorReferenceReplacer#visitConstantInstruction(Clazz, Method,
   * CodeAttribute, int, ConstantInstruction)}.
   *
   * <ul>
   *   <li>Then calls {@link CodeAttributeEditor#replaceInstruction(int, Instruction[])}.
   * </ul>
   *
   * <p>Method under test: {@link NoConstructorReferenceReplacer#visitConstantInstruction(Clazz,
   * Method, CodeAttribute, int, ConstantInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction); then calls replaceInstruction(int, Instruction[])")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NoConstructorReferenceReplacer.visitConstantInstruction(Clazz, Method, CodeAttribute, int, ConstantInstruction)"
  })
  void testVisitConstantInstruction_thenCallsReplaceInstruction() {
    // Arrange
    CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
    doNothing()
        .when(codeAttributeEditor)
        .replaceInstruction(anyInt(), Mockito.<Instruction[]>any());
    NoConstructorReferenceReplacer noConstructorReferenceReplacer =
        new NoConstructorReferenceReplacer(codeAttributeEditor);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    noConstructorReferenceReplacer.visitConstantInstruction(
        clazz, method, codeAttribute, 0, new ConstantInstruction((byte) -76, 1));

    // Assert
    verify(codeAttributeEditor).replaceInstruction(eq(0), isA(Instruction[].class));
  }

  /**
   * Test {@link NoConstructorReferenceReplacer#visitFieldrefConstant(Clazz, FieldrefConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassOptimizationInfo#containsConstructors()}.
   * </ul>
   *
   * <p>Method under test: {@link NoConstructorReferenceReplacer#visitFieldrefConstant(Clazz,
   * FieldrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitFieldrefConstant(Clazz, FieldrefConstant); then calls containsConstructors()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NoConstructorReferenceReplacer.visitFieldrefConstant(Clazz, FieldrefConstant)"
  })
  void testVisitFieldrefConstant_thenCallsContainsConstructors() {
    // Arrange
    NoConstructorReferenceReplacer noConstructorReferenceReplacer =
        new NoConstructorReferenceReplacer(new CodeAttributeEditor());
    LibraryClass clazz = new LibraryClass();

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.containsConstructors()).thenReturn(true);

    LibraryClass referencedClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    referencedClass.setProcessingInfo(classOptimizationInfo);

    // Act
    noConstructorReferenceReplacer.visitFieldrefConstant(
        clazz, new FieldrefConstant(1, 1, referencedClass, new LibraryField()));

    // Assert
    verify(classOptimizationInfo).containsConstructors();
  }

  /**
   * Test {@link NoConstructorReferenceReplacer#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link ClassOptimizationInfo#containsConstructors()}.
   * </ul>
   *
   * <p>Method under test: {@link NoConstructorReferenceReplacer#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); then calls containsConstructors()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NoConstructorReferenceReplacer.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_thenCallsContainsConstructors() {
    // Arrange
    NoConstructorReferenceReplacer noConstructorReferenceReplacer =
        new NoConstructorReferenceReplacer(new CodeAttributeEditor());
    LibraryClass clazz = new LibraryClass();

    ClassOptimizationInfo classOptimizationInfo = mock(ClassOptimizationInfo.class);
    when(classOptimizationInfo.containsConstructors()).thenReturn(true);

    LibraryClass referencedClass = new LibraryClass(1, "This Class Name", "Super Class Name");
    referencedClass.setProcessingInfo(classOptimizationInfo);

    // Act
    noConstructorReferenceReplacer.visitAnyMethodrefConstant(
        clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, new LibraryMethod()));

    // Assert
    verify(classOptimizationInfo).containsConstructors();
  }
}
