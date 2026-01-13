package proguard.optimize.evaluation;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.ExceptionInfo;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.exception.EmptyCodeAttributeException;

class EvaluationShrinkerDiffblueTest {
  /**
   * Test {@link EvaluationShrinker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>When {@code A}.
   *   <li>Then calls {@link PartialEvaluator#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   * </ul>
   *
   * <p>Method under test: {@link EvaluationShrinker#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); when 'A'; then calls visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationShrinker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_whenA_thenCallsVisitCodeAttribute() {
    // Arrange
    PartialEvaluator partialEvaluator = mock(PartialEvaluator.class);
    doNothing()
        .when(partialEvaluator)
        .visitCodeAttribute(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<CodeAttribute>any());
    EvaluationShrinker evaluationShrinker =
        new EvaluationShrinker(
            partialEvaluator,
            true,
            true,
            mock(InstructionVisitor.class),
            mock(InstructionVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    evaluationShrinker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(partialEvaluator)
        .visitCodeAttribute(isA(Clazz.class), isA(Method.class), isA(CodeAttribute.class));
  }

  /**
   * Test {@link EvaluationShrinker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>When {@link CodeAttribute#CodeAttribute(int)} with u2attributeNameIndex is one.
   * </ul>
   *
   * <p>Method under test: {@link EvaluationShrinker#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); when CodeAttribute(int) with u2attributeNameIndex is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationShrinker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_whenCodeAttributeWithU2attributeNameIndexIsOne() {
    // Arrange
    PartialEvaluator partialEvaluator = mock(PartialEvaluator.class);
    doNothing()
        .when(partialEvaluator)
        .visitCodeAttribute(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<CodeAttribute>any());
    EvaluationShrinker evaluationShrinker =
        new EvaluationShrinker(
            partialEvaluator,
            true,
            true,
            mock(InstructionVisitor.class),
            mock(InstructionVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act
    evaluationShrinker.visitCodeAttribute(clazz, method, new CodeAttribute(1));

    // Assert
    verify(partialEvaluator)
        .visitCodeAttribute(isA(Clazz.class), isA(Method.class), isA(CodeAttribute.class));
  }

  /**
   * Test {@link EvaluationShrinker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>When {@link CodeAttribute#CodeAttribute()}.
   *   <li>Then calls {@link PartialEvaluator#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   * </ul>
   *
   * <p>Method under test: {@link EvaluationShrinker#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); when CodeAttribute(); then calls visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationShrinker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_whenCodeAttribute_thenCallsVisitCodeAttribute() {
    // Arrange
    PartialEvaluator partialEvaluator = mock(PartialEvaluator.class);
    doNothing()
        .when(partialEvaluator)
        .visitCodeAttribute(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<CodeAttribute>any());
    EvaluationShrinker evaluationShrinker =
        new EvaluationShrinker(
            partialEvaluator,
            true,
            true,
            mock(InstructionVisitor.class),
            mock(InstructionVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act
    evaluationShrinker.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    verify(partialEvaluator)
        .visitCodeAttribute(isA(Clazz.class), isA(Method.class), isA(CodeAttribute.class));
  }

  /**
   * Test {@link EvaluationShrinker#visitCodeAttribute0(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then throw {@link EmptyCodeAttributeException}.
   * </ul>
   *
   * <p>Method under test: {@link EvaluationShrinker#visitCodeAttribute0(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute0(Clazz, Method, CodeAttribute); then throw EmptyCodeAttributeException")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationShrinker.visitCodeAttribute0(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute0_thenThrowEmptyCodeAttributeException() {
    // Arrange
    PartialEvaluator partialEvaluator = mock(PartialEvaluator.class);
    doNothing()
        .when(partialEvaluator)
        .visitCodeAttribute(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<CodeAttribute>any());
    EvaluationShrinker evaluationShrinker =
        new EvaluationShrinker(
            partialEvaluator,
            true,
            true,
            mock(InstructionVisitor.class),
            mock(InstructionVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act and Assert
    assertThrows(
        EmptyCodeAttributeException.class,
        () -> evaluationShrinker.visitCodeAttribute0(clazz, method, new CodeAttribute(1)));
    verify(partialEvaluator)
        .visitCodeAttribute(isA(Clazz.class), isA(Method.class), isA(CodeAttribute.class));
  }

  /**
   * Test {@link EvaluationShrinker#visitExceptionInfo(Clazz, Method, CodeAttribute,
   * ExceptionInfo)}.
   *
   * <ul>
   *   <li>Given {@link PartialEvaluator} {@link PartialEvaluator#isTraced(int)} return {@code
   *       true}.
   *   <li>Then calls {@link PartialEvaluator#isTraced(int)}.
   * </ul>
   *
   * <p>Method under test: {@link EvaluationShrinker#visitExceptionInfo(Clazz, Method,
   * CodeAttribute, ExceptionInfo)}
   */
  @Test
  @DisplayName(
      "Test visitExceptionInfo(Clazz, Method, CodeAttribute, ExceptionInfo); given PartialEvaluator isTraced(int) return 'true'; then calls isTraced(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void EvaluationShrinker.visitExceptionInfo(Clazz, Method, CodeAttribute, ExceptionInfo)"
  })
  void testVisitExceptionInfo_givenPartialEvaluatorIsTracedReturnTrue_thenCallsIsTraced() {
    // Arrange
    PartialEvaluator partialEvaluator = mock(PartialEvaluator.class);
    when(partialEvaluator.isTraced(anyInt())).thenReturn(true);
    EvaluationShrinker evaluationShrinker =
        new EvaluationShrinker(
            partialEvaluator,
            true,
            true,
            mock(InstructionVisitor.class),
            mock(InstructionVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    evaluationShrinker.visitExceptionInfo(clazz, method, codeAttribute, new ExceptionInfo());

    // Assert
    verify(partialEvaluator).isTraced(0);
  }
}
