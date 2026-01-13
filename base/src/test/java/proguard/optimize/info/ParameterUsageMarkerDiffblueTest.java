package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
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
import org.mockito.Mockito;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;

class ParameterUsageMarkerDiffblueTest {
  /**
   * Test {@link ParameterUsageMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Given {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterUsageMarker#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName("Test visitLibraryMethod(LibraryClass, LibraryMethod); given 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterUsageMarker.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_givenFalse() {
    // Arrange
    ParameterUsageMarker parameterUsageMarker = new ParameterUsageMarker();

    LibraryClass libraryClass = mock(LibraryClass.class);
    when(libraryClass.mayHaveImplementations(Mockito.<Method>any())).thenReturn(false);

    // Act
    parameterUsageMarker.visitLibraryMethod(libraryClass, new LibraryMethod());

    // Assert
    verify(libraryClass).mayHaveImplementations(isA(Method.class));
  }

  /**
   * Test {@link ParameterUsageMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethodOptimizationInfo#updateUsedParameters(long)}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterUsageMarker#visitLibraryMethod(LibraryClass,
   * LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); then calls updateUsedParameters(long)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterUsageMarker.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_thenCallsUpdateUsedParameters() {
    // Arrange
    ParameterUsageMarker parameterUsageMarker = new ParameterUsageMarker();

    LibraryClass libraryClass = mock(LibraryClass.class);
    when(libraryClass.mayHaveImplementations(Mockito.<Method>any())).thenReturn(true);

    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).updateUsedParameters(anyLong());

    LibraryMethod libraryMethod = mock(LibraryMethod.class);
    when(libraryMethod.getProcessingInfo()).thenReturn(programMethodOptimizationInfo);

    // Act
    parameterUsageMarker.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    verify(libraryClass).mayHaveImplementations(isA(Method.class));
    verify(programMethodOptimizationInfo).updateUsedParameters(-1L);
    verify(libraryMethod, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link ParameterUsageMarker#getParameterSize(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return zero.
   * </ul>
   *
   * <p>Method under test: {@link ParameterUsageMarker#getParameterSize(Method)}
   */
  @Test
  @DisplayName(
      "Test getParameterSize(Method); given MethodOptimizationInfo (default constructor); then return zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"int ParameterUsageMarker.getParameterSize(Method)"})
  void testGetParameterSize_givenMethodOptimizationInfo_thenReturnZero() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertEquals(0, ParameterUsageMarker.getParameterSize(method));
  }

  /**
   * Test {@link ParameterUsageMarker#markParameterUsed(Method, int)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethodOptimizationInfo#setParameterUsed(int)}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterUsageMarker#markParameterUsed(Method, int)}
   */
  @Test
  @DisplayName("Test markParameterUsed(Method, int); then calls setParameterUsed(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterUsageMarker.markParameterUsed(Method, int)"})
  void testMarkParameterUsed_thenCallsSetParameterUsed() {
    // Arrange
    ProgramMethodOptimizationInfo programMethodOptimizationInfo =
        mock(ProgramMethodOptimizationInfo.class);
    doNothing().when(programMethodOptimizationInfo).setParameterUsed(anyInt());

    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(programMethodOptimizationInfo);

    // Act
    ParameterUsageMarker.markParameterUsed(method, 1);

    // Assert
    verify(programMethodOptimizationInfo).setParameterUsed(1);
  }

  /**
   * Test {@link ParameterUsageMarker#hasUnusedParameters(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterUsageMarker#hasUnusedParameters(Method)}
   */
  @Test
  @DisplayName(
      "Test hasUnusedParameters(Method); given MethodOptimizationInfo (default constructor); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ParameterUsageMarker.hasUnusedParameters(Method)"})
  void testHasUnusedParameters_givenMethodOptimizationInfo_thenReturnFalse() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertFalse(ParameterUsageMarker.hasUnusedParameters(method));
  }

  /**
   * Test {@link ParameterUsageMarker#isParameterUsed(Method, int)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterUsageMarker#isParameterUsed(Method, int)}
   */
  @Test
  @DisplayName(
      "Test isParameterUsed(Method, int); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ParameterUsageMarker.isParameterUsed(Method, int)"})
  void testIsParameterUsed_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(ParameterUsageMarker.isParameterUsed(method, 1));
  }

  /**
   * Test {@link ParameterUsageMarker#getUsedParameters(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return minus one.
   * </ul>
   *
   * <p>Method under test: {@link ParameterUsageMarker#getUsedParameters(Method)}
   */
  @Test
  @DisplayName(
      "Test getUsedParameters(Method); given MethodOptimizationInfo (default constructor); then return minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long ParameterUsageMarker.getUsedParameters(Method)"})
  void testGetUsedParameters_givenMethodOptimizationInfo_thenReturnMinusOne() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertEquals(-1L, ParameterUsageMarker.getUsedParameters(method));
  }
}
