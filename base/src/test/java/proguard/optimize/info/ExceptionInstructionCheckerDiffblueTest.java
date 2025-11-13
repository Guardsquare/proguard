package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.CodeAttributeEditor.Label;
import proguard.classfile.instruction.Instruction;

class ExceptionInstructionCheckerDiffblueTest {
  /**
   * Test {@link ExceptionInstructionChecker#mayThrowExceptions(Clazz, Method, CodeAttribute)} with
   * {@code clazz}, {@code method}, {@code codeAttribute}.
   *
   * <p>Method under test: {@link ExceptionInstructionChecker#mayThrowExceptions(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test mayThrowExceptions(Clazz, Method, CodeAttribute) with 'clazz', 'method', 'codeAttribute'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ExceptionInstructionChecker.mayThrowExceptions(Clazz, Method, CodeAttribute)"
  })
  void testMayThrowExceptionsWithClazzMethodCodeAttribute() throws UnsupportedEncodingException {
    // Arrange
    ExceptionInstructionChecker exceptionInstructionChecker = new ExceptionInstructionChecker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute(1, 3, 3, 3, "A\tA\tA\tA\t".getBytes("UTF-8"));

    // Act and Assert
    assertFalse(exceptionInstructionChecker.mayThrowExceptions(clazz, method, codeAttribute));
  }

  /**
   * Test {@link ExceptionInstructionChecker#mayThrowExceptions(Clazz, Method, CodeAttribute, int)}
   * with {@code clazz}, {@code method}, {@code codeAttribute}, {@code offset}.
   *
   * <p>Method under test: {@link ExceptionInstructionChecker#mayThrowExceptions(Clazz, Method,
   * CodeAttribute, int)}
   */
  @Test
  @DisplayName(
      "Test mayThrowExceptions(Clazz, Method, CodeAttribute, int) with 'clazz', 'method', 'codeAttribute', 'offset'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ExceptionInstructionChecker.mayThrowExceptions(Clazz, Method, CodeAttribute, int)"
  })
  void testMayThrowExceptionsWithClazzMethodCodeAttributeOffset()
      throws UnsupportedEncodingException {
    // Arrange
    ExceptionInstructionChecker exceptionInstructionChecker = new ExceptionInstructionChecker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute(1, 3, 3, 3, "AXAXAXAX".getBytes("UTF-8"));

    // Act and Assert
    assertFalse(exceptionInstructionChecker.mayThrowExceptions(clazz, method, codeAttribute, 2));
  }

  /**
   * Test {@link ExceptionInstructionChecker#mayThrowExceptions(Clazz, Method, CodeAttribute, int,
   * Instruction)} with {@code clazz}, {@code method}, {@code codeAttribute}, {@code offset}, {@code
   * instruction}.
   *
   * <p>Method under test: {@link ExceptionInstructionChecker#mayThrowExceptions(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test mayThrowExceptions(Clazz, Method, CodeAttribute, int, Instruction) with 'clazz', 'method', 'codeAttribute', 'offset', 'instruction'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ExceptionInstructionChecker.mayThrowExceptions(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testMayThrowExceptionsWithClazzMethodCodeAttributeOffsetInstruction() {
    // Arrange
    ExceptionInstructionChecker exceptionInstructionChecker = new ExceptionInstructionChecker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act and Assert
    assertFalse(
        exceptionInstructionChecker.mayThrowExceptions(
            clazz, method, codeAttribute, 2, new Label(1)));
  }

  /**
   * Test {@link ExceptionInstructionChecker#mayThrowExceptions(Clazz, Method, CodeAttribute, int)}
   * with {@code clazz}, {@code method}, {@code codeAttribute}, {@code offset}.
   *
   * <ul>
   *   <li>When {@code X}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ExceptionInstructionChecker#mayThrowExceptions(Clazz, Method,
   * CodeAttribute, int)}
   */
  @Test
  @DisplayName(
      "Test mayThrowExceptions(Clazz, Method, CodeAttribute, int) with 'clazz', 'method', 'codeAttribute', 'offset'; when 'X'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ExceptionInstructionChecker.mayThrowExceptions(Clazz, Method, CodeAttribute, int)"
  })
  void testMayThrowExceptionsWithClazzMethodCodeAttributeOffset_whenX_thenReturnFalse() {
    // Arrange
    ExceptionInstructionChecker exceptionInstructionChecker = new ExceptionInstructionChecker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 'X', 4, 'X', 'A', 'X', 'A', 'X'});

    // Act and Assert
    assertFalse(exceptionInstructionChecker.mayThrowExceptions(clazz, method, codeAttribute, 2));
  }

  /**
   * Test {@link ExceptionInstructionChecker#mayThrowExceptions(Clazz, Method, CodeAttribute, int,
   * int)} with {@code clazz}, {@code method}, {@code codeAttribute}, {@code startOffset}, {@code
   * endOffset}.
   *
   * <p>Method under test: {@link ExceptionInstructionChecker#mayThrowExceptions(Clazz, Method,
   * CodeAttribute, int, int)}
   */
  @Test
  @DisplayName(
      "Test mayThrowExceptions(Clazz, Method, CodeAttribute, int, int) with 'clazz', 'method', 'codeAttribute', 'startOffset', 'endOffset'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ExceptionInstructionChecker.mayThrowExceptions(Clazz, Method, CodeAttribute, int, int)"
  })
  void testMayThrowExceptionsWithClazzMethodCodeAttributeStartOffsetEndOffset() {
    // Arrange
    ExceptionInstructionChecker exceptionInstructionChecker = new ExceptionInstructionChecker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 3, 'A', 3, 'A', 3, 'A', 3});

    // Act and Assert
    assertFalse(exceptionInstructionChecker.mayThrowExceptions(clazz, method, codeAttribute, 1, 3));
  }

  /**
   * Test {@link ExceptionInstructionChecker#mayThrowExceptions(Clazz, Method, CodeAttribute)} with
   * {@code clazz}, {@code method}, {@code codeAttribute}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ExceptionInstructionChecker#mayThrowExceptions(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test mayThrowExceptions(Clazz, Method, CodeAttribute) with 'clazz', 'method', 'codeAttribute'; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean ExceptionInstructionChecker.mayThrowExceptions(Clazz, Method, CodeAttribute)"
  })
  void testMayThrowExceptionsWithClazzMethodCodeAttribute_thenReturnFalse() {
    // Arrange
    ExceptionInstructionChecker exceptionInstructionChecker = new ExceptionInstructionChecker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act and Assert
    assertFalse(exceptionInstructionChecker.mayThrowExceptions(clazz, method, new CodeAttribute()));
  }

  /**
   * Test {@link ExceptionInstructionChecker#firstExceptionThrowingInstructionOffset(Clazz, Method,
   * CodeAttribute, int, int)}.
   *
   * <ul>
   *   <li>Then return three.
   * </ul>
   *
   * <p>Method under test: {@link
   * ExceptionInstructionChecker#firstExceptionThrowingInstructionOffset(Clazz, Method,
   * CodeAttribute, int, int)}
   */
  @Test
  @DisplayName(
      "Test firstExceptionThrowingInstructionOffset(Clazz, Method, CodeAttribute, int, int); then return three")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "int ExceptionInstructionChecker.firstExceptionThrowingInstructionOffset(Clazz, Method, CodeAttribute, int, int)"
  })
  void testFirstExceptionThrowingInstructionOffset_thenReturnThree() {
    // Arrange
    ExceptionInstructionChecker exceptionInstructionChecker = new ExceptionInstructionChecker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 3, 'A', 3, 'A', 3, 'A', 3});

    // Act and Assert
    assertEquals(
        3,
        exceptionInstructionChecker.firstExceptionThrowingInstructionOffset(
            clazz, method, codeAttribute, 1, 3));
  }

  /**
   * Test {@link ExceptionInstructionChecker#lastExceptionThrowingInstructionOffset(Clazz, Method,
   * CodeAttribute, int, int)}.
   *
   * <ul>
   *   <li>Then return one.
   * </ul>
   *
   * <p>Method under test: {@link
   * ExceptionInstructionChecker#lastExceptionThrowingInstructionOffset(Clazz, Method,
   * CodeAttribute, int, int)}
   */
  @Test
  @DisplayName(
      "Test lastExceptionThrowingInstructionOffset(Clazz, Method, CodeAttribute, int, int); then return one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "int ExceptionInstructionChecker.lastExceptionThrowingInstructionOffset(Clazz, Method, CodeAttribute, int, int)"
  })
  void testLastExceptionThrowingInstructionOffset_thenReturnOne() {
    // Arrange
    ExceptionInstructionChecker exceptionInstructionChecker = new ExceptionInstructionChecker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 3, 'A', 3, 'A', 3, 'A', 3});

    // Act and Assert
    assertEquals(
        1,
        exceptionInstructionChecker.lastExceptionThrowingInstructionOffset(
            clazz, method, codeAttribute, 1, 3));
  }
}
