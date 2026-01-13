package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;

class SuperInvocationMarkerDiffblueTest {
  /**
   * Test {@link SuperInvocationMarker#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@code <init>}.
   *   <li>When {@link LibraryClass} {@link LibraryClass#getName(int)} return {@code <init>}.
   * </ul>
   *
   * <p>Method under test: {@link SuperInvocationMarker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); given '<init>'; when LibraryClass getName(int) return '<init>'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SuperInvocationMarker.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_givenInit_whenLibraryClassGetNameReturnInit() {
    // Arrange
    SuperInvocationMarker superInvocationMarker = new SuperInvocationMarker();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getName(anyInt())).thenReturn("<init>");

    // Act
    superInvocationMarker.visitAnyMethodrefConstant(clazz, new InterfaceMethodrefConstant());

    // Assert
    verify(clazz).getName(0);
  }

  /**
   * Test {@link SuperInvocationMarker#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@code Name}.
   *   <li>Then calls {@link LibraryClass#getName(int)}.
   * </ul>
   *
   * <p>Method under test: {@link SuperInvocationMarker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); given 'Name'; then calls getName(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void SuperInvocationMarker.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_givenName_thenCallsGetName() {
    // Arrange
    SuperInvocationMarker superInvocationMarker = new SuperInvocationMarker();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getName(anyInt())).thenReturn("Name");

    // Act
    superInvocationMarker.visitAnyMethodrefConstant(clazz, new InterfaceMethodrefConstant());

    // Assert
    verify(clazz).getName(0);
  }

  /**
   * Test {@link SuperInvocationMarker#invokesSuperMethods(Method)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link SuperInvocationMarker#invokesSuperMethods(Method)}
   */
  @Test
  @DisplayName(
      "Test invokesSuperMethods(Method); given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean SuperInvocationMarker.invokesSuperMethods(Method)"})
  void testInvokesSuperMethods_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(SuperInvocationMarker.invokesSuperMethods(method));
  }
}
