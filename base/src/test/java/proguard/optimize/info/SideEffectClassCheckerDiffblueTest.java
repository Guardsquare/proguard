package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.isA;
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
import proguard.classfile.LibraryField;
import proguard.classfile.Member;

class SideEffectClassCheckerDiffblueTest {
  /**
   * Test {@link SideEffectClassChecker#mayHaveSideEffects(Clazz, Clazz, Member)} with {@code
   * referencingClass}, {@code referencedClass}, {@code referencedMember}.
   *
   * <p>Method under test: {@link SideEffectClassChecker#mayHaveSideEffects(Clazz, Clazz, Member)}
   */
  @Test
  @DisplayName(
      "Test mayHaveSideEffects(Clazz, Clazz, Member) with 'referencingClass', 'referencedClass', 'referencedMember'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean SideEffectClassChecker.mayHaveSideEffects(Clazz, Clazz, Member)"})
  void testMayHaveSideEffectsWithReferencingClassReferencedClassReferencedMember() {
    // Arrange
    LibraryClass referencingClass = new LibraryClass();
    LibraryClass referencedClass = new LibraryClass();

    LibraryField referencedMember = mock(LibraryField.class);
    when(referencedMember.getAccessFlags()).thenReturn(1);
    when(referencedMember.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act
    boolean actualMayHaveSideEffectsResult =
        SideEffectClassChecker.mayHaveSideEffects(
            referencingClass, referencedClass, referencedMember);

    // Assert
    verify(referencedMember).getAccessFlags();
    verify(referencedMember).getName(isA(Clazz.class));
    assertFalse(actualMayHaveSideEffectsResult);
  }
}
