package proguard.optimize;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
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
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;

class MemberDescriptorSpecializerDiffblueTest {
  /**
   * Test {@link MemberDescriptorSpecializer#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link MemberDescriptorSpecializer#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MemberDescriptorSpecializer.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField() {
    // Arrange
    MemberDescriptorSpecializer memberDescriptorSpecializer =
        new MemberDescriptorSpecializer(false, true, true);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    memberDescriptorSpecializer.visitProgramField(programClass, new ProgramField());

    // Assert
    verify(programClass).getString(0);
  }

  /**
   * Test {@link MemberDescriptorSpecializer#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given empty string.
   *   <li>When {@link ProgramClass}.
   *   <li>Then calls {@link ProgramField#getDescriptor(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link MemberDescriptorSpecializer#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given empty string; when ProgramClass; then calls getDescriptor(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MemberDescriptorSpecializer.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField_givenEmptyString_whenProgramClass_thenCallsGetDescriptor() {
    // Arrange
    MemberDescriptorSpecializer memberDescriptorSpecializer =
        new MemberDescriptorSpecializer(true, true, true);
    ProgramClass programClass = mock(ProgramClass.class);

    ProgramField programField = mock(ProgramField.class);
    when(programField.getDescriptor(Mockito.<Clazz>any())).thenReturn("");

    // Act
    memberDescriptorSpecializer.visitProgramField(programClass, programField);

    // Assert
    verify(programField).getDescriptor(isA(Clazz.class));
  }

  /**
   * Test {@link MemberDescriptorSpecializer#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@code String}.
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link MemberDescriptorSpecializer#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given 'String'; then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MemberDescriptorSpecializer.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField_givenString_thenCallsGetString() {
    // Arrange
    MemberDescriptorSpecializer memberDescriptorSpecializer =
        new MemberDescriptorSpecializer(true, true, true);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    memberDescriptorSpecializer.visitProgramField(programClass, new ProgramField());

    // Assert
    verify(programClass).getString(0);
  }
}
