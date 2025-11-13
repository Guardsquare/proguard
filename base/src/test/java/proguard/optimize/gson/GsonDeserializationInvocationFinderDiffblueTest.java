package proguard.optimize.gson;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.BranchInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;

class GsonDeserializationInvocationFinderDiffblueTest {
  /**
   * Test {@link GsonDeserializationInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}.
   *
   * <ul>
   *   <li>When {@link BranchInstruction} {@link BranchInstruction#accept(Clazz, Method,
   *       CodeAttribute, int, InstructionVisitor)} does nothing.
   *   <li>Then calls {@link BranchInstruction#accept(Clazz, Method, CodeAttribute, int,
   *       InstructionVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link GsonDeserializationInvocationFinder#visitAnyInstruction(Clazz,
   * Method, CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction); when BranchInstruction accept(Clazz, Method, CodeAttribute, int, InstructionVisitor) does nothing; then calls accept(Clazz, Method, CodeAttribute, int, InstructionVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonDeserializationInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction_whenBranchInstructionAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    ClassVisitor domainClassVisitor = mock(ClassVisitor.class);
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));

    GsonDeserializationInvocationFinder gsonDeserializationInvocationFinder =
        new GsonDeserializationInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            domainClassVisitor,
            warningPrinter);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonDeserializationInvocationFinder.visitAnyInstruction(
        clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }
}
