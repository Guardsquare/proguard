package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
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
import proguard.classfile.ProgramMember;
import proguard.testutils.cpa.NamedClass;

class RenamedFlagSetterDiffblueTest {
  /**
   * Test {@link RenamedFlagSetter#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link RenamedFlagSetter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RenamedFlagSetter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass() {
    // Arrange
    RenamedFlagSetter renamedFlagSetter = new RenamedFlagSetter();

    NamedClass programClass = new NamedClass("Processing Info");
    programClass.setProcessingInfo("Processing Info");

    // Act
    renamedFlagSetter.visitProgramClass(programClass);

    // Assert that nothing has changed
    assertEquals(0, programClass.getAccessFlags());
  }

  /**
   * Test {@link RenamedFlagSetter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then {@link NamedClass#NamedClass(String)} with {@code Member Name} AccessFlags is {@code
   *       65536}.
   * </ul>
   *
   * <p>Method under test: {@link RenamedFlagSetter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); then NamedClass(String) with 'Member Name' AccessFlags is '65536'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RenamedFlagSetter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenNamedClassWithMemberNameAccessFlagsIs65536() {
    // Arrange
    RenamedFlagSetter renamedFlagSetter = new RenamedFlagSetter();

    NamedClass programClass = new NamedClass("Member Name");
    programClass.setProcessingInfo("Processing Info");

    // Act
    renamedFlagSetter.visitProgramClass(programClass);

    // Assert
    assertEquals(65536, programClass.getAccessFlags());
  }

  /**
   * Test {@link RenamedFlagSetter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Then {@link NamedClass#NamedClass(String)} with {@code Member Name} AccessFlags is zero.
   * </ul>
   *
   * <p>Method under test: {@link RenamedFlagSetter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); then NamedClass(String) with 'Member Name' AccessFlags is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RenamedFlagSetter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_thenNamedClassWithMemberNameAccessFlagsIsZero() {
    // Arrange
    RenamedFlagSetter renamedFlagSetter = new RenamedFlagSetter();
    NamedClass programClass = new NamedClass("Member Name");

    // Act
    renamedFlagSetter.visitProgramClass(programClass);

    // Assert that nothing has changed
    assertEquals(0, programClass.getAccessFlags());
  }

  /**
   * Test {@link RenamedFlagSetter#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <ul>
   *   <li>Given {@code String}.
   *   <li>Then {@link ProgramField#ProgramField()} AccessFlags is zero.
   * </ul>
   *
   * <p>Method under test: {@link RenamedFlagSetter#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMember(ProgramClass, ProgramMember); given 'String'; then ProgramField() AccessFlags is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RenamedFlagSetter.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember_givenString_thenProgramFieldAccessFlagsIsZero() {
    // Arrange
    RenamedFlagSetter renamedFlagSetter = new RenamedFlagSetter();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");
    ProgramField programMember = new ProgramField();

    // Act
    renamedFlagSetter.visitProgramMember(programClass, programMember);

    // Assert that nothing has changed
    verify(programClass).getString(0);
    assertEquals(0, programMember.getAccessFlags());
  }

  /**
   * Test {@link RenamedFlagSetter#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <ul>
   *   <li>Then {@link ProgramField#ProgramField()} AccessFlags is {@code 65536}.
   * </ul>
   *
   * <p>Method under test: {@link RenamedFlagSetter#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMember(ProgramClass, ProgramMember); then ProgramField() AccessFlags is '65536'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RenamedFlagSetter.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember_thenProgramFieldAccessFlagsIs65536() {
    // Arrange
    RenamedFlagSetter renamedFlagSetter = new RenamedFlagSetter();

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    ProgramField programMember = new ProgramField();
    programMember.setProcessingInfo("Processing Info");

    // Act
    renamedFlagSetter.visitProgramMember(programClass, programMember);

    // Assert
    verify(programClass).getString(0);
    assertEquals(65536, programMember.getAccessFlags());
  }

  /**
   * Test {@link RenamedFlagSetter#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <ul>
   *   <li>When {@link ProgramMember} {@link ProgramMember#getProcessingInfo()} return {@code Name}.
   * </ul>
   *
   * <p>Method under test: {@link RenamedFlagSetter#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMember(ProgramClass, ProgramMember); when ProgramMember getProcessingInfo() return 'Name'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RenamedFlagSetter.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember_whenProgramMemberGetProcessingInfoReturnName() {
    // Arrange
    RenamedFlagSetter renamedFlagSetter = new RenamedFlagSetter();
    ProgramClass programClass = mock(ProgramClass.class);

    ProgramMember programMember = mock(ProgramMember.class);
    when(programMember.getProcessingInfo()).thenReturn("Name");
    when(programMember.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act
    renamedFlagSetter.visitProgramMember(programClass, programMember);

    // Assert
    verify(programMember).getName(isA(Clazz.class));
    verify(programMember, atLeast(1)).getProcessingInfo();
  }

  /**
   * Test {@link RenamedFlagSetter#visitProgramMember(ProgramClass, ProgramMember)}.
   *
   * <ul>
   *   <li>When {@link ProgramMember} {@link ProgramMember#getProcessingInfo()} return {@code
   *       Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link RenamedFlagSetter#visitProgramMember(ProgramClass, ProgramMember)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMember(ProgramClass, ProgramMember); when ProgramMember getProcessingInfo() return 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void RenamedFlagSetter.visitProgramMember(ProgramClass, ProgramMember)"})
  void testVisitProgramMember_whenProgramMemberGetProcessingInfoReturnProcessingInfo() {
    // Arrange
    RenamedFlagSetter renamedFlagSetter = new RenamedFlagSetter();
    ProgramClass programClass = mock(ProgramClass.class);

    ProgramMember programMember = mock(ProgramMember.class);
    when(programMember.getProcessingInfo()).thenReturn("Processing Info");
    when(programMember.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act
    renamedFlagSetter.visitProgramMember(programClass, programMember);

    // Assert
    verify(programMember).getName(isA(Clazz.class));
    verify(programMember, atLeast(1)).getProcessingInfo();
  }
}
