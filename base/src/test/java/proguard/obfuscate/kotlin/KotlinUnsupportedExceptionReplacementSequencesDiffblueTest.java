package proguard.obfuscate.kotlin;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.ClassPool;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.MethodrefConstant;
import proguard.classfile.constant.NameAndTypeConstant;
import proguard.classfile.constant.Utf8Constant;
import proguard.classfile.instruction.BranchInstruction;
import proguard.classfile.instruction.ConstantInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.instruction.VariableInstruction;
import proguard.classfile.kotlin.KotlinConstants;

class KotlinUnsupportedExceptionReplacementSequencesDiffblueTest {
  /**
   * Test {@link
   * KotlinUnsupportedExceptionReplacementSequences#KotlinUnsupportedExceptionReplacementSequences(ClassPool,
   * ClassPool)}.
   *
   * <p>Method under test: {@link
   * KotlinUnsupportedExceptionReplacementSequences#KotlinUnsupportedExceptionReplacementSequences(ClassPool,
   * ClassPool)}
   */
  @Test
  @DisplayName("Test new KotlinUnsupportedExceptionReplacementSequences(ClassPool, ClassPool)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUnsupportedExceptionReplacementSequences.<init>(ClassPool, ClassPool)"
  })
  void testNewKotlinUnsupportedExceptionReplacementSequences() throws UnsupportedEncodingException {
    // Arrange, Act and Assert
    Constant[] constants =
        new KotlinUnsupportedExceptionReplacementSequences(
                KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool)
            .getConstants();
    Constant constant = constants[1];
    assertTrue(constant instanceof Utf8Constant);
    Constant constant2 = constants[3];
    assertTrue(constant2 instanceof Utf8Constant);
    Constant constant3 = constants[4];
    assertTrue(constant3 instanceof Utf8Constant);
    Constant constant4 = constants[7];
    assertTrue(constant4 instanceof Utf8Constant);
    assertEquals(10, constants.length);
    assertArrayEquals("()V".getBytes("UTF-8"), ((Utf8Constant) constant4).getBytes());
    assertArrayEquals(
        "(Ljava/lang/String;)V".getBytes("UTF-8"), ((Utf8Constant) constant3).getBytes());
    assertArrayEquals("<init>".getBytes("UTF-8"), ((Utf8Constant) constant2).getBytes());
    assertArrayEquals(
        "java/lang/UnsupportedOperationException".getBytes("UTF-8"),
        ((Utf8Constant) constant).getBytes());
  }

  /**
   * Test {@link
   * KotlinUnsupportedExceptionReplacementSequences#KotlinUnsupportedExceptionReplacementSequences(ClassPool,
   * ClassPool)}.
   *
   * <p>Method under test: {@link
   * KotlinUnsupportedExceptionReplacementSequences#KotlinUnsupportedExceptionReplacementSequences(ClassPool,
   * ClassPool)}
   */
  @Test
  @DisplayName("Test new KotlinUnsupportedExceptionReplacementSequences(ClassPool, ClassPool)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void KotlinUnsupportedExceptionReplacementSequences.<init>(ClassPool, ClassPool)"
  })
  void testNewKotlinUnsupportedExceptionReplacementSequences2()
      throws UnsupportedEncodingException {
    // Arrange, Act and Assert
    Constant[] constants =
        new KotlinUnsupportedExceptionReplacementSequences(null, KotlinConstants.dummyClassPool)
            .getConstants();
    Constant constant = constants[1];
    assertTrue(constant instanceof Utf8Constant);
    Constant constant2 = constants[3];
    assertTrue(constant2 instanceof Utf8Constant);
    Constant constant3 = constants[4];
    assertTrue(constant3 instanceof Utf8Constant);
    Constant constant4 = constants[7];
    assertTrue(constant4 instanceof Utf8Constant);
    assertEquals(10, constants.length);
    assertArrayEquals("()V".getBytes("UTF-8"), ((Utf8Constant) constant4).getBytes());
    assertArrayEquals(
        "(Ljava/lang/String;)V".getBytes("UTF-8"), ((Utf8Constant) constant3).getBytes());
    assertArrayEquals("<init>".getBytes("UTF-8"), ((Utf8Constant) constant2).getBytes());
    assertArrayEquals(
        "java/lang/UnsupportedOperationException".getBytes("UTF-8"),
        ((Utf8Constant) constant).getBytes());
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>{@link KotlinUnsupportedExceptionReplacementSequences#getConstants()}
   *   <li>{@link KotlinUnsupportedExceptionReplacementSequences#getSequences()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Constant[] KotlinUnsupportedExceptionReplacementSequences.getConstants()",
    "Instruction[][][] KotlinUnsupportedExceptionReplacementSequences.getSequences()"
  })
  void testGettersAndSetters() {
    // Arrange
    KotlinUnsupportedExceptionReplacementSequences kotlinUnsupportedExceptionReplacementSequences =
        new KotlinUnsupportedExceptionReplacementSequences(
            KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool);

    // Act
    Constant[] actualConstants = kotlinUnsupportedExceptionReplacementSequences.getConstants();
    Instruction[][][] actualSequences =
        kotlinUnsupportedExceptionReplacementSequences.getSequences();

    // Assert
    Constant constant = actualConstants[2];
    assertTrue(constant instanceof ClassConstant);
    Constant constant2 = actualConstants[6];
    assertTrue(constant2 instanceof MethodrefConstant);
    Constant constant3 = actualConstants[9];
    assertTrue(constant3 instanceof MethodrefConstant);
    assertTrue(actualConstants[5] instanceof NameAndTypeConstant);
    assertTrue(actualConstants[8] instanceof NameAndTypeConstant);
    assertTrue(actualConstants[1] instanceof Utf8Constant);
    assertTrue(actualConstants[3] instanceof Utf8Constant);
    assertTrue(actualConstants[4] instanceof Utf8Constant);
    assertTrue(actualConstants[7] instanceof Utf8Constant);
    Instruction[][] instructionArray = actualSequences[0];
    Instruction[] instructionArray2 = instructionArray[0];
    Instruction instruction = instructionArray2[1];
    assertTrue(instruction instanceof BranchInstruction);
    Instruction[] instructionArray3 = instructionArray[1];
    assertTrue(instructionArray3[1] instanceof BranchInstruction);
    Instruction instruction2 = instructionArray2[2];
    assertTrue(instruction2 instanceof ConstantInstruction);
    Instruction instruction3 = instructionArray2[4];
    assertTrue(instruction3 instanceof ConstantInstruction);
    Instruction instruction4 = instructionArray2[5];
    assertTrue(instruction4 instanceof ConstantInstruction);
    assertTrue(instructionArray3[2] instanceof ConstantInstruction);
    Instruction instruction5 = instructionArray3[4];
    assertTrue(instruction5 instanceof ConstantInstruction);
    Instruction instruction6 = instructionArray2[3];
    assertTrue(instruction6 instanceof SimpleInstruction);
    assertTrue(instructionArray3[3] instanceof SimpleInstruction);
    Instruction instruction7 = instructionArray2[0];
    assertTrue(instruction7 instanceof VariableInstruction);
    assertTrue(instructionArray3[0] instanceof VariableInstruction);
    assertNull(((ClassConstant) constant).javaLangClassClass);
    assertNull(((ClassConstant) constant).referencedClass);
    assertNull(((MethodrefConstant) constant2).referencedClass);
    assertNull(((MethodrefConstant) constant3).referencedClass);
    assertNull(((MethodrefConstant) constant2).referencedMethod);
    assertNull(((MethodrefConstant) constant3).referencedMethod);
    assertNull(actualConstants[0]);
    assertEquals((byte) -58, ((BranchInstruction) instruction).opcode);
    assertEquals((byte) -69, ((ConstantInstruction) instruction2).opcode);
    assertEquals((byte) -73, ((ConstantInstruction) instruction4).opcode);
    assertEquals((byte) -73, ((ConstantInstruction) instruction5).opcode);
    assertEquals(0, ((ConstantInstruction) instruction2).constant);
    assertEquals(0, ((ConstantInstruction) instruction3).constant);
    assertEquals(0, ((ConstantInstruction) instruction4).constant);
    assertEquals(0, ((ConstantInstruction) instruction5).constant);
    assertEquals(0, ((SimpleInstruction) instruction6).constant);
    assertEquals(0, ((VariableInstruction) instruction7).constant);
    assertEquals(1, actualSequences.length);
    assertEquals(1, ((ClassConstant) constant).u2nameIndex);
    assertEquals(10, actualConstants.length);
    assertEquals(1073741824, ((ConstantInstruction) instruction3).constantIndex);
    assertEquals(1073741825, ((BranchInstruction) instruction).branchOffset);
    assertEquals(1073741827, ((VariableInstruction) instruction7).variableIndex);
    assertEquals((byte) 18, ((ConstantInstruction) instruction3).opcode);
    assertEquals(2, instructionArray.length);
    assertEquals(2, ((ConstantInstruction) instruction2).constantIndex);
    assertEquals((byte) 25, ((VariableInstruction) instruction7).opcode);
    assertEquals(5, instructionArray3.length);
    assertEquals(6, instructionArray2.length);
    assertEquals(6, ((ConstantInstruction) instruction4).constantIndex);
    assertEquals(9, ((ConstantInstruction) instruction5).constantIndex);
    assertEquals('Y', ((SimpleInstruction) instruction6).opcode);
  }
}
