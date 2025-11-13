package proguard.configuration;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.StringConstant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.CodeAttributeEditor.Label;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.util.BranchTargetFinder;

class ConfigurationLoggingInstructionSequenceReplacerDiffblueTest {
  /**
   * Test {@link ConfigurationLoggingInstructionSequenceReplacer#matchedArgument(Clazz, Method,
   * CodeAttribute, int, int)} with {@code clazz}, {@code method}, {@code codeAttribute}, {@code
   * offset}, {@code argument}.
   *
   * <p>Method under test: {@link
   * ConfigurationLoggingInstructionSequenceReplacer#matchedArgument(Clazz, Method, CodeAttribute,
   * int, int)}
   */
  @Test
  @DisplayName(
      "Test matchedArgument(Clazz, Method, CodeAttribute, int, int) with 'clazz', 'method', 'codeAttribute', 'offset', 'argument'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "int ConfigurationLoggingInstructionSequenceReplacer.matchedArgument(Clazz, Method, CodeAttribute, int, int)"
  })
  void testMatchedArgumentWithClazzMethodCodeAttributeOffsetArgument() {
    // Arrange
    Constant[] patternConstants = new Constant[] {new ClassConstant()};
    Instruction[] patternInstructions = new Instruction[] {new Label(1)};
    Constant[] replacementConstants = new Constant[] {new ClassConstant()};
    Instruction[] replacementInstructions = new Instruction[] {new Label(1)};
    BranchTargetFinder branchTargetFinder = new BranchTargetFinder();

    ConfigurationLoggingInstructionSequenceReplacer
        configurationLoggingInstructionSequenceReplacer =
            new ConfigurationLoggingInstructionSequenceReplacer(
                patternConstants,
                patternInstructions,
                replacementConstants,
                replacementInstructions,
                branchTargetFinder,
                new CodeAttributeEditor());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act and Assert
    assertEquals(
        1,
        configurationLoggingInstructionSequenceReplacer.matchedArgument(
            clazz,
            method,
            new CodeAttribute(),
            2,
            ConfigurationLoggingInstructionSequenceConstants.LOCAL_VARIABLE_INDEX_2));
  }

  /**
   * Test {@link ConfigurationLoggingInstructionSequenceReplacer#matchedArgument(Clazz, Method,
   * CodeAttribute, int, int)} with {@code clazz}, {@code method}, {@code codeAttribute}, {@code
   * offset}, {@code argument}.
   *
   * <ul>
   *   <li>Then return two.
   * </ul>
   *
   * <p>Method under test: {@link
   * ConfigurationLoggingInstructionSequenceReplacer#matchedArgument(Clazz, Method, CodeAttribute,
   * int, int)}
   */
  @Test
  @DisplayName(
      "Test matchedArgument(Clazz, Method, CodeAttribute, int, int) with 'clazz', 'method', 'codeAttribute', 'offset', 'argument'; then return two")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "int ConfigurationLoggingInstructionSequenceReplacer.matchedArgument(Clazz, Method, CodeAttribute, int, int)"
  })
  void testMatchedArgumentWithClazzMethodCodeAttributeOffsetArgument_thenReturnTwo() {
    // Arrange
    Constant[] patternConstants = new Constant[] {new ClassConstant()};
    Instruction[] patternInstructions = new Instruction[] {new Label(1)};
    Constant[] replacementConstants = new Constant[] {new ClassConstant()};
    Instruction[] replacementInstructions = new Instruction[] {new Label(1)};
    BranchTargetFinder branchTargetFinder = new BranchTargetFinder();

    ConfigurationLoggingInstructionSequenceReplacer
        configurationLoggingInstructionSequenceReplacer =
            new ConfigurationLoggingInstructionSequenceReplacer(
                patternConstants,
                patternInstructions,
                replacementConstants,
                replacementInstructions,
                branchTargetFinder,
                new CodeAttributeEditor());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act and Assert
    assertEquals(
        2,
        configurationLoggingInstructionSequenceReplacer.matchedArgument(
            clazz,
            method,
            new CodeAttribute(),
            2,
            ConfigurationLoggingInstructionSequenceConstants.LOCAL_VARIABLE_INDEX_3));
  }

  /**
   * Test {@link ConfigurationLoggingInstructionSequenceReplacer#matchedArgument(Clazz, Method,
   * CodeAttribute, int, int)} with {@code clazz}, {@code method}, {@code codeAttribute}, {@code
   * offset}, {@code argument}.
   *
   * <ul>
   *   <li>Then return zero.
   * </ul>
   *
   * <p>Method under test: {@link
   * ConfigurationLoggingInstructionSequenceReplacer#matchedArgument(Clazz, Method, CodeAttribute,
   * int, int)}
   */
  @Test
  @DisplayName(
      "Test matchedArgument(Clazz, Method, CodeAttribute, int, int) with 'clazz', 'method', 'codeAttribute', 'offset', 'argument'; then return zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "int ConfigurationLoggingInstructionSequenceReplacer.matchedArgument(Clazz, Method, CodeAttribute, int, int)"
  })
  void testMatchedArgumentWithClazzMethodCodeAttributeOffsetArgument_thenReturnZero() {
    // Arrange
    Constant[] patternConstants = new Constant[] {new ClassConstant()};
    Instruction[] patternInstructions = new Instruction[] {new Label(1)};
    Constant[] replacementConstants = new Constant[] {new ClassConstant()};
    Instruction[] replacementInstructions = new Instruction[] {new Label(1)};
    BranchTargetFinder branchTargetFinder = new BranchTargetFinder();

    ConfigurationLoggingInstructionSequenceReplacer
        configurationLoggingInstructionSequenceReplacer =
            new ConfigurationLoggingInstructionSequenceReplacer(
                patternConstants,
                patternInstructions,
                replacementConstants,
                replacementInstructions,
                branchTargetFinder,
                new CodeAttributeEditor());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act and Assert
    assertEquals(
        0,
        configurationLoggingInstructionSequenceReplacer.matchedArgument(
            clazz,
            method,
            new CodeAttribute(),
            2,
            ConfigurationLoggingInstructionSequenceConstants.LOCAL_VARIABLE_INDEX_1));
  }

  /**
   * Test {@link ConfigurationLoggingInstructionSequenceReplacer#matchedArgument(Clazz, Method,
   * CodeAttribute, int, int)} with {@code clazz}, {@code method}, {@code codeAttribute}, {@code
   * offset}, {@code argument}.
   *
   * <ul>
   *   <li>When one.
   * </ul>
   *
   * <p>Method under test: {@link
   * ConfigurationLoggingInstructionSequenceReplacer#matchedArgument(Clazz, Method, CodeAttribute,
   * int, int)}
   */
  @Test
  @DisplayName(
      "Test matchedArgument(Clazz, Method, CodeAttribute, int, int) with 'clazz', 'method', 'codeAttribute', 'offset', 'argument'; when one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "int ConfigurationLoggingInstructionSequenceReplacer.matchedArgument(Clazz, Method, CodeAttribute, int, int)"
  })
  void testMatchedArgumentWithClazzMethodCodeAttributeOffsetArgument_whenOne() {
    // Arrange
    Constant[] patternConstants = new Constant[] {new ClassConstant()};
    Instruction[] patternInstructions = new Instruction[] {new Label(1)};
    Constant[] replacementConstants = new Constant[] {new ClassConstant()};
    Instruction[] replacementInstructions = new Instruction[] {new Label(1)};
    BranchTargetFinder branchTargetFinder = new BranchTargetFinder();

    ConfigurationLoggingInstructionSequenceReplacer
        configurationLoggingInstructionSequenceReplacer =
            new ConfigurationLoggingInstructionSequenceReplacer(
                patternConstants,
                patternInstructions,
                replacementConstants,
                replacementInstructions,
                branchTargetFinder,
                new CodeAttributeEditor());
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act and Assert
    assertEquals(
        1,
        configurationLoggingInstructionSequenceReplacer.matchedArgument(
            clazz, method, new CodeAttribute(), 2, 1));
  }

  /**
   * Test {@link ConfigurationLoggingInstructionSequenceReplacer#matchedConstantIndex(ProgramClass,
   * int)}.
   *
   * <ul>
   *   <li>Then second element {@link ClassConstant}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ConfigurationLoggingInstructionSequenceReplacer#matchedConstantIndex(ProgramClass, int)}
   */
  @Test
  @DisplayName("Test matchedConstantIndex(ProgramClass, int); then second element ClassConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "int ConfigurationLoggingInstructionSequenceReplacer.matchedConstantIndex(ProgramClass, int)"
  })
  void testMatchedConstantIndex_thenSecondElementClassConstant() {
    // Arrange
    ClassConstant classConstant = new ClassConstant();
    Constant[] patternConstants = new Constant[] {classConstant};
    Instruction[] patternInstructions = new Instruction[] {new Label(1)};
    Constant[] replacementConstants = new Constant[] {new ClassConstant()};
    Instruction[] replacementInstructions = new Instruction[] {new Label(1)};
    BranchTargetFinder branchTargetFinder = new BranchTargetFinder();

    ConfigurationLoggingInstructionSequenceReplacer
        configurationLoggingInstructionSequenceReplacer =
            new ConfigurationLoggingInstructionSequenceReplacer(
                patternConstants,
                patternInstructions,
                replacementConstants,
                replacementInstructions,
                branchTargetFinder,
                new CodeAttributeEditor());
    ClassConstant classConstant2 = new ClassConstant();
    ClassConstant classConstant3 = new ClassConstant();
    ProgramClass programClass =
        new ProgramClass(1, 2, new Constant[] {classConstant2, classConstant3}, 1, 1, 1);

    // Act
    int actualMatchedConstantIndexResult =
        configurationLoggingInstructionSequenceReplacer.matchedConstantIndex(
            programClass, 536870920);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    Constant constant = constantArray[1];
    assertTrue(constant instanceof ClassConstant);
    assertTrue(constantArray[3] instanceof StringConstant);
    assertTrue(constantArray[2] instanceof Utf8Constant);
    assertNull(constantArray[17]);
    assertEquals(18, constantArray.length);
    assertEquals(3, actualMatchedConstantIndexResult);
    assertEquals(4, programClass.u2constantPoolCount);
    assertEquals(classConstant, constant);
    assertSame(classConstant3, constant);
  }

  /**
   * Test {@link ConfigurationLoggingInstructionSequenceReplacer#matchedConstantIndex(ProgramClass,
   * int)}.
   *
   * <ul>
   *   <li>Then second element {@link StringConstant}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ConfigurationLoggingInstructionSequenceReplacer#matchedConstantIndex(ProgramClass, int)}
   */
  @Test
  @DisplayName("Test matchedConstantIndex(ProgramClass, int); then second element StringConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "int ConfigurationLoggingInstructionSequenceReplacer.matchedConstantIndex(ProgramClass, int)"
  })
  void testMatchedConstantIndex_thenSecondElementStringConstant() {
    // Arrange
    Constant[] patternConstants = new Constant[] {new ClassConstant()};
    Instruction[] patternInstructions = new Instruction[] {new Label(1)};
    Constant[] replacementConstants = new Constant[] {new ClassConstant()};
    Instruction[] replacementInstructions = new Instruction[] {new Label(1)};
    BranchTargetFinder branchTargetFinder = new BranchTargetFinder();

    ConfigurationLoggingInstructionSequenceReplacer
        configurationLoggingInstructionSequenceReplacer =
            new ConfigurationLoggingInstructionSequenceReplacer(
                patternConstants,
                patternInstructions,
                replacementConstants,
                replacementInstructions,
                branchTargetFinder,
                new CodeAttributeEditor());
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass programClass = new ProgramClass(1, 0, constantPool, 1, 1, 1);

    // Act
    int actualMatchedConstantIndexResult =
        configurationLoggingInstructionSequenceReplacer.matchedConstantIndex(
            programClass, 536870920);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    assertTrue(constantArray[1] instanceof StringConstant);
    assertTrue(constantArray[0] instanceof Utf8Constant);
    assertNull(constantArray[2]);
    assertEquals(1, actualMatchedConstantIndexResult);
    assertEquals(17, constantArray.length);
    assertEquals(2, programClass.u2constantPoolCount);
  }

  /**
   * Test {@link ConfigurationLoggingInstructionSequenceReplacer#matchedConstantIndex(ProgramClass,
   * int)}.
   *
   * <ul>
   *   <li>When {@code 536870920}.
   *   <li>Then third element {@link StringConstant}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ConfigurationLoggingInstructionSequenceReplacer#matchedConstantIndex(ProgramClass, int)}
   */
  @Test
  @DisplayName(
      "Test matchedConstantIndex(ProgramClass, int); when '536870920'; then third element StringConstant")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "int ConfigurationLoggingInstructionSequenceReplacer.matchedConstantIndex(ProgramClass, int)"
  })
  void testMatchedConstantIndex_when536870920_thenThirdElementStringConstant()
      throws UnsupportedEncodingException {
    // Arrange
    Constant[] patternConstants = new Constant[] {new ClassConstant()};
    Instruction[] patternInstructions = new Instruction[] {new Label(1)};
    Constant[] replacementConstants = new Constant[] {new ClassConstant()};
    Instruction[] replacementInstructions = new Instruction[] {new Label(1)};
    BranchTargetFinder branchTargetFinder = new BranchTargetFinder();

    ConfigurationLoggingInstructionSequenceReplacer
        configurationLoggingInstructionSequenceReplacer =
            new ConfigurationLoggingInstructionSequenceReplacer(
                patternConstants,
                patternInstructions,
                replacementConstants,
                replacementInstructions,
                branchTargetFinder,
                new CodeAttributeEditor());
    Constant[] constantPool = new Constant[] {new ClassConstant()};
    ProgramClass programClass = new ProgramClass(1, 1, constantPool, 1, 1, 1);

    // Act
    int actualMatchedConstantIndexResult =
        configurationLoggingInstructionSequenceReplacer.matchedConstantIndex(
            programClass, 536870920);

    // Assert
    Constant[] constantArray = programClass.constantPool;
    Constant constant = constantArray[2];
    assertTrue(constant instanceof StringConstant);
    Constant constant2 = constantArray[1];
    assertTrue(constant2 instanceof Utf8Constant);
    assertNull(((StringConstant) constant).javaLangStringClass);
    assertNull(((StringConstant) constant).referencedClass);
    assertNull(((StringConstant) constant).referencedMember);
    assertNull(((StringConstant) constant).referencedResourceFile);
    assertEquals(0, ((StringConstant) constant).referencedResourceId);
    assertEquals(1, constant2.getTag());
    assertEquals(1, ((StringConstant) constant).u2stringIndex);
    assertEquals(17, constantArray.length);
    assertEquals(2, actualMatchedConstantIndexResult);
    assertEquals(3, programClass.u2constantPoolCount);
    assertEquals(8, constant.getTag());
    assertFalse(constant2.isCategory2());
    assertFalse(constant.isCategory2());
    assertEquals(Boolean.FALSE.toString(), ((Utf8Constant) constant2).getString());
    assertArrayEquals("false".getBytes("UTF-8"), ((Utf8Constant) constant2).getBytes());
  }
}
