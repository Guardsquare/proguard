package proguard.optimize;

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
import org.mockito.junit.jupiter.MockitoExtension;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;

@ExtendWith(MockitoExtension.class)
class TailRecursionSimplifierDiffblueTest {
  @Mock private Method method;

  @InjectMocks private TailRecursionSimplifier tailRecursionSimplifier;

  /**
   * Test {@link TailRecursionSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link TailRecursionSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TailRecursionSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute() {
    // Arrange
    when(method.getAccessFlags()).thenReturn(26);
    LibraryClass clazz = new LibraryClass();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 26, 'A', 26, 'A', 26, 'A', 26});

    // Act
    tailRecursionSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method).getAccessFlags();
  }

  /**
   * Test {@link TailRecursionSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link TailRecursionSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TailRecursionSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute2() {
    // Arrange
    when(method.getAccessFlags()).thenReturn(26);
    LibraryClass clazz = new LibraryClass();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {' ', 26, 'A', 26, 'A', 26, 'A', 26});

    // Act
    tailRecursionSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method).getAccessFlags();
  }

  /**
   * Test {@link TailRecursionSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link TailRecursionSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TailRecursionSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute3() {
    // Arrange
    when(method.getAccessFlags()).thenReturn(26);
    LibraryClass clazz = new LibraryClass();
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'X', 26, 'A', 26, 'A', 26, 'A', 26});

    // Act
    tailRecursionSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method).getAccessFlags();
  }

  /**
   * Test {@link TailRecursionSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Given {@link Method} {@link Method#getAccessFlags()} return minus one.
   *   <li>When {@link CodeAttribute#CodeAttribute()}.
   * </ul>
   *
   * <p>Method under test: {@link TailRecursionSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); given Method getAccessFlags() return minus one; when CodeAttribute()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TailRecursionSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute_givenMethodGetAccessFlagsReturnMinusOne_whenCodeAttribute() {
    // Arrange
    when(method.getAccessFlags()).thenReturn(-1);
    LibraryClass clazz = new LibraryClass();

    // Act
    tailRecursionSimplifier.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    verify(method).getAccessFlags();
  }

  /**
   * Test {@link TailRecursionSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Given {@link Method} {@link Method#getAccessFlags()} return one.
   *   <li>When {@link CodeAttribute#CodeAttribute()}.
   * </ul>
   *
   * <p>Method under test: {@link TailRecursionSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); given Method getAccessFlags() return one; when CodeAttribute()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TailRecursionSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute_givenMethodGetAccessFlagsReturnOne_whenCodeAttribute() {
    // Arrange
    when(method.getAccessFlags()).thenReturn(1);
    LibraryClass clazz = new LibraryClass();

    // Act
    tailRecursionSimplifier.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    verify(method).getAccessFlags();
  }

  /**
   * Test {@link TailRecursionSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>When {@link CodeAttribute#CodeAttribute()}.
   *   <li>Then calls {@link Method#getAccessFlags()}.
   * </ul>
   *
   * <p>Method under test: {@link TailRecursionSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); when CodeAttribute(); then calls getAccessFlags()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void TailRecursionSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute_whenCodeAttribute_thenCallsGetAccessFlags() {
    // Arrange
    when(method.getAccessFlags()).thenReturn(26);
    LibraryClass clazz = new LibraryClass();

    // Act
    tailRecursionSimplifier.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    verify(method).getAccessFlags();
  }
}
