package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;

class CatchExceptionMarkerDiffblueTest {
  /**
   * Test {@link CatchExceptionMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethodOptimizationInfo#setCatchesExceptions()}.
   * </ul>
   *
   * <p>Method under test: {@link CatchExceptionMarker#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then calls setCatchesExceptions()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void CatchExceptionMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenCallsSetCatchesExceptions() {
    // Arrange
    CatchExceptionMarker catchExceptionMarker = new CatchExceptionMarker();
    LibraryClass clazz = new LibraryClass();

    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setCatchesExceptions();

    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(programMethodOptimizationInfo);
    CodeAttribute codeAttribute = new CodeAttribute(1);
    codeAttribute.u2exceptionTableLength = 1;

    // Act
    catchExceptionMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(programMethodOptimizationInfo).setCatchesExceptions();
  }

  /**
   * Test {@link CatchExceptionMarker#catchesExceptions(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link CatchExceptionMarker#catchesExceptions(Method)}
   */
  @Test
  @DisplayName(
      "Test catchesExceptions(Method); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean CatchExceptionMarker.catchesExceptions(Method)"})
  void testCatchesExceptions_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(CatchExceptionMarker.catchesExceptions(method));
  }
}
