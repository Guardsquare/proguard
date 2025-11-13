package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.evaluation.value.ParticularFloatValue;
import proguard.evaluation.value.Value;
import proguard.testutils.cpa.NamedField;

class ProgramMemberOptimizationInfoSetterDiffblueTest {
  /**
   * Test {@link ProgramMemberOptimizationInfoSetter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link
   * ProgramMemberOptimizationInfoSetter#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramMemberOptimizationInfoSetter.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField() {
    // Arrange
    ProgramMemberOptimizationInfoSetter programMemberOptimizationInfoSetter =
        new ProgramMemberOptimizationInfoSetter(false);
    ProgramClass programClass = new ProgramClass();

    ProgramField programField = new ProgramField(1, 1, 1, new LibraryClass());
    programField.setProcessingInfo("Program Field");

    // Act
    programMemberOptimizationInfoSetter.visitProgramField(programClass, programField);

    // Assert that nothing has changed
    assertEquals("Program Field", programField.getProcessingInfo());
  }

  /**
   * Test {@link ProgramMemberOptimizationInfoSetter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link
   * ProgramMemberOptimizationInfoSetter#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramMemberOptimizationInfoSetter.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField2() {
    // Arrange
    ProgramMemberOptimizationInfoSetter programMemberOptimizationInfoSetter =
        new ProgramMemberOptimizationInfoSetter(true, true);
    ProgramClass programClass = new ProgramClass();
    NamedField programField = new NamedField("Field Name", "Field Descriptor");

    // Act
    programMemberOptimizationInfoSetter.visitProgramField(programClass, programField);

    // Assert
    Object processingInfo = programField.getProcessingInfo();
    Value value = ((ProgramFieldOptimizationInfo) processingInfo).getValue();
    assertTrue(value instanceof ParticularFloatValue);
    assertTrue(processingInfo instanceof ProgramFieldOptimizationInfo);
    assertNull(((ProgramFieldOptimizationInfo) processingInfo).getReferencedClass());
    assertEquals(0.0f, ((ParticularFloatValue) value).value());
    assertFalse(value.isCategory2());
    assertFalse(((ProgramFieldOptimizationInfo) processingInfo).isKept());
    assertFalse(((ProgramFieldOptimizationInfo) processingInfo).isRead());
    assertFalse(((ProgramFieldOptimizationInfo) processingInfo).isWritten());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
    assertTrue(((ProgramFieldOptimizationInfo) processingInfo).canBeMadePrivate());
  }

  /**
   * Test {@link ProgramMemberOptimizationInfoSetter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ProgramMemberOptimizationInfoSetter#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField); given 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramMemberOptimizationInfoSetter.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField_givenProcessingInfo() {
    // Arrange
    ProgramMemberOptimizationInfoSetter programMemberOptimizationInfoSetter =
        new ProgramMemberOptimizationInfoSetter(true);
    ProgramClass programClass = new ProgramClass();

    NamedField programField = new NamedField("Field Name", "Field Descriptor");
    programField.setProcessingInfo("Processing Info");

    // Act
    programMemberOptimizationInfoSetter.visitProgramField(programClass, programField);

    // Assert
    Object processingInfo = programField.getProcessingInfo();
    Value value = ((ProgramFieldOptimizationInfo) processingInfo).getValue();
    assertTrue(value instanceof ParticularFloatValue);
    assertTrue(processingInfo instanceof ProgramFieldOptimizationInfo);
    assertNull(((ProgramFieldOptimizationInfo) processingInfo).getReferencedClass());
    assertEquals(0.0f, ((ParticularFloatValue) value).value());
    assertFalse(value.isCategory2());
    assertFalse(((ProgramFieldOptimizationInfo) processingInfo).isKept());
    assertFalse(((ProgramFieldOptimizationInfo) processingInfo).isRead());
    assertFalse(((ProgramFieldOptimizationInfo) processingInfo).isWritten());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
    assertTrue(((ProgramFieldOptimizationInfo) processingInfo).canBeMadePrivate());
  }

  /**
   * Test {@link ProgramMemberOptimizationInfoSetter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>When {@link NamedField#NamedField(String, String)} with {@code Field Name} and {@code
   *       Field Descriptor}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ProgramMemberOptimizationInfoSetter#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); when NamedField(String, String) with 'Field Name' and 'Field Descriptor'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramMemberOptimizationInfoSetter.visitProgramField(ProgramClass, ProgramField)"
  })
  void testVisitProgramField_whenNamedFieldWithFieldNameAndFieldDescriptor() {
    // Arrange
    ProgramMemberOptimizationInfoSetter programMemberOptimizationInfoSetter =
        new ProgramMemberOptimizationInfoSetter(true);
    ProgramClass programClass = new ProgramClass();
    NamedField programField = new NamedField("Field Name", "Field Descriptor");

    // Act
    programMemberOptimizationInfoSetter.visitProgramField(programClass, programField);

    // Assert
    Object processingInfo = programField.getProcessingInfo();
    Value value = ((ProgramFieldOptimizationInfo) processingInfo).getValue();
    assertTrue(value instanceof ParticularFloatValue);
    assertTrue(processingInfo instanceof ProgramFieldOptimizationInfo);
    assertNull(((ProgramFieldOptimizationInfo) processingInfo).getReferencedClass());
    assertEquals(0.0f, ((ParticularFloatValue) value).value());
    assertFalse(value.isCategory2());
    assertFalse(((ProgramFieldOptimizationInfo) processingInfo).isKept());
    assertFalse(((ProgramFieldOptimizationInfo) processingInfo).isRead());
    assertFalse(((ProgramFieldOptimizationInfo) processingInfo).isWritten());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
    assertTrue(((ProgramFieldOptimizationInfo) processingInfo).canBeMadePrivate());
  }

  /**
   * Test {@link ProgramMemberOptimizationInfoSetter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link ProgramMethod#getProcessingInfo()}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ProgramMemberOptimizationInfoSetter#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then calls getProcessingInfo()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramMemberOptimizationInfoSetter.visitProgramMethod(ProgramClass, ProgramMethod)"
  })
  void testVisitProgramMethod_thenCallsGetProcessingInfo() {
    // Arrange
    ProgramMemberOptimizationInfoSetter programMemberOptimizationInfoSetter =
        new ProgramMemberOptimizationInfoSetter(false);
    ProgramClass programClass = mock(ProgramClass.class);

    ProgramMethod programMethod = mock(ProgramMethod.class);
    when(programMethod.getProcessingInfo()).thenReturn("Processing Info");

    // Act
    programMemberOptimizationInfoSetter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programMethod, atLeast(1)).getProcessingInfo();
  }
}
