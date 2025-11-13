package proguard.optimize.gson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
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
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.NameAndTypeConstant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.kotlin.KotlinConstants;

class OptimizedJsonReaderImplInitializerDiffblueTest {
  /**
   * Test {@link OptimizedJsonReaderImplInitializer#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}.
   *
   * <p>Method under test: {@link OptimizedJsonReaderImplInitializer#visitCodeAttribute(Clazz,
   * Method, CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizedJsonReaderImplInitializer.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute() {
    // Arrange
    CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
    doNothing()
        .when(codeAttributeEditor)
        .replaceInstruction(anyInt(), Mockito.<Instruction[]>any());
    doNothing()
        .when(codeAttributeEditor)
        .visitCodeAttribute(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<CodeAttribute>any());
    doNothing().when(codeAttributeEditor).reset(anyInt());
    OptimizedJsonReaderImplInitializer optimizedJsonReaderImplInitializer =
        new OptimizedJsonReaderImplInitializer(
            null, KotlinConstants.dummyClassPool, codeAttributeEditor, new OptimizedJsonInfo());
    ClassConstant classConstant = new ClassConstant();
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 1, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing().when(codeAttribute).addProcessingFlags((int[]) Mockito.any());
    codeAttribute.addProcessingFlags(2, 1, 2, 1);

    // Act
    optimizedJsonReaderImplInitializer.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(codeAttributeEditor).replaceInstruction(eq(0), isA(Instruction[].class));
    verify(codeAttributeEditor).reset(0);
    verify(codeAttributeEditor)
        .visitCodeAttribute(isA(Clazz.class), isA(Method.class), isA(CodeAttribute.class));
    verify(codeAttribute).addProcessingFlags((int[]) Mockito.any());
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertTrue(constantArray[2] instanceof ClassConstant);
    assertTrue(constantArray[6] instanceof MethodrefConstant);
    assertTrue(constantArray[5] instanceof NameAndTypeConstant);
    assertTrue(constantArray[1] instanceof Utf8Constant);
    assertTrue(constantArray[4] instanceof Utf8Constant);
    assertEquals(17, constantArray.length);
    assertEquals(7, clazz.u2constantPoolCount);
    assertSame(classConstant, constant);
  }

  /**
   * Test {@link OptimizedJsonReaderImplInitializer#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}.
   *
   * <ul>
   *   <li>Then first element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizedJsonReaderImplInitializer#visitCodeAttribute(Clazz,
   * Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then first element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizedJsonReaderImplInitializer.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute_thenFirstElementClassConstant() {
    // Arrange
    CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
    doNothing()
        .when(codeAttributeEditor)
        .replaceInstruction(anyInt(), Mockito.<Instruction[]>any());
    doNothing()
        .when(codeAttributeEditor)
        .visitCodeAttribute(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<CodeAttribute>any());
    doNothing().when(codeAttributeEditor).reset(anyInt());
    OptimizedJsonReaderImplInitializer optimizedJsonReaderImplInitializer =
        new OptimizedJsonReaderImplInitializer(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            codeAttributeEditor,
            new OptimizedJsonInfo());
    ClassConstant classConstant = new ClassConstant();
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 1, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing().when(codeAttribute).addProcessingFlags((int[]) Mockito.any());
    codeAttribute.addProcessingFlags(2, 1, 2, 1);

    // Act
    optimizedJsonReaderImplInitializer.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(codeAttributeEditor).replaceInstruction(eq(0), isA(Instruction[].class));
    verify(codeAttributeEditor).reset(0);
    verify(codeAttributeEditor)
        .visitCodeAttribute(isA(Clazz.class), isA(Method.class), isA(CodeAttribute.class));
    verify(codeAttribute).addProcessingFlags((int[]) Mockito.any());
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[0];
    assertTrue(constant instanceof ClassConstant);
    assertTrue(constantArray[2] instanceof ClassConstant);
    assertTrue(constantArray[6] instanceof MethodrefConstant);
    assertTrue(constantArray[5] instanceof NameAndTypeConstant);
    assertTrue(constantArray[1] instanceof Utf8Constant);
    assertTrue(constantArray[4] instanceof Utf8Constant);
    assertEquals(17, constantArray.length);
    assertEquals(7, clazz.u2constantPoolCount);
    assertSame(classConstant, constant);
  }

  /**
   * Test {@link OptimizedJsonReaderImplInitializer#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}.
   *
   * <ul>
   *   <li>Then second element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizedJsonReaderImplInitializer#visitCodeAttribute(Clazz,
   * Method, CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then second element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizedJsonReaderImplInitializer.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute_thenSecondElementClassConstant() {
    // Arrange
    CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
    doNothing()
        .when(codeAttributeEditor)
        .replaceInstruction(anyInt(), Mockito.<Instruction[]>any());
    doNothing()
        .when(codeAttributeEditor)
        .visitCodeAttribute(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<CodeAttribute>any());
    doNothing().when(codeAttributeEditor).reset(anyInt());
    OptimizedJsonReaderImplInitializer optimizedJsonReaderImplInitializer =
        new OptimizedJsonReaderImplInitializer(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            codeAttributeEditor,
            new OptimizedJsonInfo());
    ClassConstant classConstant = new ClassConstant();
    Constant[] constantPool = new Constant[] {classConstant};
    ProgramClass clazz = new ProgramClass(1, 0, constantPool, 1, 1, 1);
    LibraryMethod method = new LibraryMethod();

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing().when(codeAttribute).addProcessingFlags((int[]) Mockito.any());
    codeAttribute.addProcessingFlags(2, 1, 2, 1);

    // Act
    optimizedJsonReaderImplInitializer.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(codeAttributeEditor).replaceInstruction(eq(0), isA(Instruction[].class));
    verify(codeAttributeEditor).reset(0);
    verify(codeAttributeEditor)
        .visitCodeAttribute(isA(Clazz.class), isA(Method.class), isA(CodeAttribute.class));
    verify(codeAttribute).addProcessingFlags((int[]) Mockito.any());
    Constant[] constantArray = clazz.constantPool;
    Constant constant = constantArray[1];
    assertTrue(constant instanceof ClassConstant);
    assertTrue(constantArray[5] instanceof MethodrefConstant);
    assertTrue(constantArray[4] instanceof NameAndTypeConstant);
    assertTrue(constantArray[0] instanceof Utf8Constant);
    assertTrue(constantArray[2] instanceof Utf8Constant);
    assertEquals("java/util/HashMap", clazz.getName());
    assertEquals("java/util/HashMap", clazz.getSuperName());
    assertNull(clazz.getSuperClass());
    assertNull(constantArray[6]);
    assertEquals(17, constantArray.length);
    assertEquals(6, clazz.u2constantPoolCount);
    assertEquals(classConstant, constant);
  }
}
