package proguard.optimize.peephole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.UnsupportedEncodingException;
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
import proguard.classfile.attribute.visitor.ExceptionInfoVisitor;

class UnreachableExceptionRemoverDiffblueTest {
  /**
   * Test {@link UnreachableExceptionRemover#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link CodeAttribute#exceptionsAccept(Clazz, Method, ExceptionInfoVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link UnreachableExceptionRemover#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then calls exceptionsAccept(Clazz, Method, ExceptionInfoVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnreachableExceptionRemover.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute_thenCallsExceptionsAccept() {
    // Arrange
    UnreachableExceptionRemover unreachableExceptionRemover = new UnreachableExceptionRemover();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing()
        .when(codeAttribute)
        .exceptionsAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<ExceptionInfoVisitor>any());

    // Act
    unreachableExceptionRemover.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(codeAttribute)
        .exceptionsAccept(isA(Clazz.class), isA(Method.class), isA(ExceptionInfoVisitor.class));
  }

  /**
   * Test {@link UnreachableExceptionRemover#visitExceptionInfo(Clazz, Method, CodeAttribute,
   * ExceptionInfo)}.
   *
   * <p>Method under test: {@link UnreachableExceptionRemover#visitExceptionInfo(Clazz, Method,
   * CodeAttribute, ExceptionInfo)}
   */
  @Test
  @DisplayName("Test visitExceptionInfo(Clazz, Method, CodeAttribute, ExceptionInfo)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnreachableExceptionRemover.visitExceptionInfo(Clazz, Method, CodeAttribute, ExceptionInfo)"
  })
  void testVisitExceptionInfo() throws UnsupportedEncodingException {
    // Arrange
    ExceptionInfoVisitor extraExceptionInfoVisitor = mock(ExceptionInfoVisitor.class);
    doNothing()
        .when(extraExceptionInfoVisitor)
        .visitExceptionInfo(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            Mockito.<ExceptionInfo>any());
    UnreachableExceptionRemover unreachableExceptionRemover =
        new UnreachableExceptionRemover(extraExceptionInfoVisitor);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute(1, 3, 3, 3, "AXAXAXAX".getBytes("UTF-8"));
    ExceptionInfo exceptionInfo = new ExceptionInfo(1, 3, 1, 1);

    // Act
    unreachableExceptionRemover.visitExceptionInfo(clazz, method, codeAttribute, exceptionInfo);

    // Assert
    verify(extraExceptionInfoVisitor)
        .visitExceptionInfo(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            isA(ExceptionInfo.class));
    assertEquals(1, exceptionInfo.u2endPC);
  }

  /**
   * Test {@link UnreachableExceptionRemover#visitExceptionInfo(Clazz, Method, CodeAttribute,
   * ExceptionInfo)}.
   *
   * <ul>
   *   <li>Given {@link UnreachableExceptionRemover#UnreachableExceptionRemover()}.
   * </ul>
   *
   * <p>Method under test: {@link UnreachableExceptionRemover#visitExceptionInfo(Clazz, Method,
   * CodeAttribute, ExceptionInfo)}
   */
  @Test
  @DisplayName(
      "Test visitExceptionInfo(Clazz, Method, CodeAttribute, ExceptionInfo); given UnreachableExceptionRemover()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnreachableExceptionRemover.visitExceptionInfo(Clazz, Method, CodeAttribute, ExceptionInfo)"
  })
  void testVisitExceptionInfo_givenUnreachableExceptionRemover() {
    // Arrange
    UnreachableExceptionRemover unreachableExceptionRemover = new UnreachableExceptionRemover();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    ExceptionInfo exceptionInfo = new ExceptionInfo();

    // Act
    unreachableExceptionRemover.visitExceptionInfo(clazz, method, codeAttribute, exceptionInfo);

    // Assert that nothing has changed
    assertEquals(0, exceptionInfo.u2endPC);
  }

  /**
   * Test {@link UnreachableExceptionRemover#visitExceptionInfo(Clazz, Method, CodeAttribute,
   * ExceptionInfo)}.
   *
   * <ul>
   *   <li>When {@link ExceptionInfo#ExceptionInfo()}.
   *   <li>Then {@link ExceptionInfo#ExceptionInfo()} {@link ExceptionInfo#u2endPC} is zero.
   * </ul>
   *
   * <p>Method under test: {@link UnreachableExceptionRemover#visitExceptionInfo(Clazz, Method,
   * CodeAttribute, ExceptionInfo)}
   */
  @Test
  @DisplayName(
      "Test visitExceptionInfo(Clazz, Method, CodeAttribute, ExceptionInfo); when ExceptionInfo(); then ExceptionInfo() u2endPC is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void UnreachableExceptionRemover.visitExceptionInfo(Clazz, Method, CodeAttribute, ExceptionInfo)"
  })
  void testVisitExceptionInfo_whenExceptionInfo_thenExceptionInfoU2endPCIsZero() {
    // Arrange
    ExceptionInfoVisitor extraExceptionInfoVisitor = mock(ExceptionInfoVisitor.class);
    doNothing()
        .when(extraExceptionInfoVisitor)
        .visitExceptionInfo(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            Mockito.<ExceptionInfo>any());
    UnreachableExceptionRemover unreachableExceptionRemover =
        new UnreachableExceptionRemover(extraExceptionInfoVisitor);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();
    ExceptionInfo exceptionInfo = new ExceptionInfo();

    // Act
    unreachableExceptionRemover.visitExceptionInfo(clazz, method, codeAttribute, exceptionInfo);

    // Assert that nothing has changed
    verify(extraExceptionInfoVisitor)
        .visitExceptionInfo(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            isA(ExceptionInfo.class));
    assertEquals(0, exceptionInfo.u2endPC);
  }
}
