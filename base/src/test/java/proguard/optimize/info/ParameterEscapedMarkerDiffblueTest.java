package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.visitor.ClassVisitor;

@ExtendWith(MockitoExtension.class)
class ParameterEscapedMarkerDiffblueTest {
  @Mock private Method method;

  @InjectMocks private ParameterEscapedMarker parameterEscapedMarker;

  /**
   * Test {@link ParameterEscapedMarker#visitClassPool(ClassPool)}.
   *
   * <ul>
   *   <li>Given {@link ParameterEscapedMarker} (default constructor).
   *   <li>Then calls {@link ClassPool#classesAccept(ClassVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapedMarker#visitClassPool(ClassPool)}
   */
  @Test
  @DisplayName(
      "Test visitClassPool(ClassPool); given ParameterEscapedMarker (default constructor); then calls classesAccept(ClassVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterEscapedMarker.visitClassPool(ClassPool)"})
  void testVisitClassPool_givenParameterEscapedMarker_thenCallsClassesAccept() {
    // Arrange
    ParameterEscapedMarker parameterEscapedMarker = new ParameterEscapedMarker();

    ClassPool classPool = mock(ClassPool.class);
    doNothing().when(classPool).classesAccept(Mockito.<ClassVisitor>any());

    // Act
    parameterEscapedMarker.visitClassPool(classPool);

    // Assert
    verify(classPool).classesAccept(isA(ClassVisitor.class));
  }

  /**
   * Test {@link ParameterEscapedMarker#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <p>Method under test: {@link ParameterEscapedMarker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterEscapedMarker.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant() {
    // Arrange
    when(method.getProcessingInfo()).thenReturn(new MethodOptimizationInfo());
    LibraryClass clazz = new LibraryClass();

    // Act
    parameterEscapedMarker.visitAnyMethodrefConstant(
        clazz, new InterfaceMethodrefConstant(1, 1, new LibraryClass(), method));

    // Assert
    verify(method, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link ParameterEscapedMarker#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link Method} {@link Method#getProcessingInfo()} return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapedMarker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); given Method getProcessingInfo() return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterEscapedMarker.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_givenMethodGetProcessingInfoReturnNull() {
    // Arrange
    when(method.getProcessingInfo()).thenReturn(null);
    LibraryClass clazz = new LibraryClass();

    // Act
    parameterEscapedMarker.visitAnyMethodrefConstant(
        clazz, new InterfaceMethodrefConstant(1, 1, new LibraryClass(), method));

    // Assert
    verify(method, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link ParameterEscapedMarker#hasParameterEscaped(Method, int)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapedMarker#hasParameterEscaped(Method, int)}
   */
  @Test
  @DisplayName(
      "Test hasParameterEscaped(Method, int); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ParameterEscapedMarker.hasParameterEscaped(Method, int)"})
  void testHasParameterEscaped_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(ParameterEscapedMarker.hasParameterEscaped(method, 1));
  }

  /**
   * Test {@link ParameterEscapedMarker#getEscapedParameters(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return minus one.
   * </ul>
   *
   * <p>Method under test: {@link ParameterEscapedMarker#getEscapedParameters(Method)}
   */
  @Test
  @DisplayName(
      "Test getEscapedParameters(Method); given MethodOptimizationInfo (default constructor); then return minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long ParameterEscapedMarker.getEscapedParameters(Method)"})
  void testGetEscapedParameters_givenMethodOptimizationInfo_thenReturnMinusOne() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertEquals(-1L, ParameterEscapedMarker.getEscapedParameters(method));
  }
}
