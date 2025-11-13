package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.LibraryMethod;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.util.SimpleFeatureNamedProcessable;

class MemberNameCleanerDiffblueTest {
  /**
   * Test {@link MemberNameCleaner#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameCleaner#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField); given 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameCleaner.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_givenProcessingInfo() {
    // Arrange
    MemberNameCleaner memberNameCleaner = new MemberNameCleaner();
    ProgramClass programClass = new ProgramClass();

    ProgramField programField = new ProgramField();
    programField.setProcessingInfo("Processing Info");

    // Act
    memberNameCleaner.visitProgramField(programClass, programField);

    // Assert
    assertNull(programField.getProcessingInfo());
  }

  /**
   * Test {@link MemberNameCleaner#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>When {@link ProgramField#ProgramField()}.
   *   <li>Then {@link ProgramField#ProgramField()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameCleaner#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); when ProgramField(); then ProgramField() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameCleaner.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_whenProgramField_thenProgramFieldProcessingInfoIsNull() {
    // Arrange
    MemberNameCleaner memberNameCleaner = new MemberNameCleaner();
    ProgramClass programClass = new ProgramClass();
    ProgramField programField = new ProgramField();

    // Act
    memberNameCleaner.visitProgramField(programClass, programField);

    // Assert that nothing has changed
    assertNull(programField.getProcessingInfo());
  }

  /**
   * Test {@link MemberNameCleaner#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameCleaner#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod); given 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameCleaner.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_givenProcessingInfo() {
    // Arrange
    MemberNameCleaner memberNameCleaner = new MemberNameCleaner();
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo("Processing Info");

    // Act
    memberNameCleaner.visitProgramMethod(programClass, programMethod);

    // Assert
    assertNull(programMethod.getProcessingInfo());
  }

  /**
   * Test {@link MemberNameCleaner#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>When {@link ProgramMethod#ProgramMethod()}.
   *   <li>Then {@link ProgramMethod#ProgramMethod()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameCleaner#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); when ProgramMethod(); then ProgramMethod() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameCleaner.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_whenProgramMethod_thenProgramMethodProcessingInfoIsNull() {
    // Arrange
    MemberNameCleaner memberNameCleaner = new MemberNameCleaner();
    ProgramClass programClass = new ProgramClass();
    ProgramMethod programMethod = new ProgramMethod();

    // Act
    memberNameCleaner.visitProgramMethod(programClass, programMethod);

    // Assert that nothing has changed
    assertNull(programMethod.getProcessingInfo());
  }

  /**
   * Test {@link MemberNameCleaner#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <p>Method under test: {@link MemberNameCleaner#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName("Test visitLibraryField(LibraryClass, LibraryField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameCleaner.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField() {
    // Arrange
    MemberNameCleaner memberNameCleaner = new MemberNameCleaner();
    LibraryClass libraryClass = new LibraryClass();

    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    LibraryField libraryField2 = new LibraryField();
    libraryField.setProcessingInfo(libraryField2);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    LibraryField libraryField17 = new LibraryField(1, "Name", "Descriptor");
    libraryField17.setProcessingInfo(libraryField16);

    LibraryField libraryField18 = new LibraryField(1, "Name", "Descriptor");
    libraryField18.setProcessingInfo(libraryField17);

    LibraryField libraryField19 = new LibraryField(1, "Name", "Descriptor");
    libraryField19.setProcessingInfo(libraryField18);

    // Act
    memberNameCleaner.visitLibraryField(libraryClass, libraryField19);

    // Assert
    assertSame(libraryField2, libraryField19.getProcessingInfo());
  }

  /**
   * Test {@link MemberNameCleaner#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <p>Method under test: {@link MemberNameCleaner#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName("Test visitLibraryField(LibraryClass, LibraryField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameCleaner.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField2() {
    // Arrange
    MemberNameCleaner memberNameCleaner = new MemberNameCleaner();
    LibraryClass libraryClass = new LibraryClass();

    LibraryField libraryField = new LibraryField(1, "Name", "Descriptor");
    libraryField.setProcessingInfo(new LibraryField());

    LibraryField libraryField2 = new LibraryField(1, "Name", "Descriptor");
    libraryField2.setProcessingInfo(libraryField);

    LibraryField libraryField3 = new LibraryField(1, "Name", "Descriptor");
    libraryField3.setProcessingInfo(libraryField2);

    LibraryField libraryField4 = new LibraryField(1, "Name", "Descriptor");
    libraryField4.setProcessingInfo(libraryField3);

    LibraryField libraryField5 = new LibraryField(1, "Name", "Descriptor");
    libraryField5.setProcessingInfo(libraryField4);

    LibraryField libraryField6 = new LibraryField(1, "Name", "Descriptor");
    libraryField6.setProcessingInfo(libraryField5);

    LibraryField libraryField7 = new LibraryField(1, "Name", "Descriptor");
    libraryField7.setProcessingInfo(libraryField6);

    LibraryField libraryField8 = new LibraryField(1, "Name", "Descriptor");
    libraryField8.setProcessingInfo(libraryField7);

    LibraryField libraryField9 = new LibraryField(1, "Name", "Descriptor");
    libraryField9.setProcessingInfo(libraryField8);

    LibraryField libraryField10 = new LibraryField(1, "Name", "Descriptor");
    libraryField10.setProcessingInfo(libraryField9);

    LibraryField libraryField11 = new LibraryField(1, "Name", "Descriptor");
    libraryField11.setProcessingInfo(libraryField10);

    LibraryField libraryField12 = new LibraryField(1, "Name", "Descriptor");
    libraryField12.setProcessingInfo(libraryField11);

    LibraryField libraryField13 = new LibraryField(1, "Name", "Descriptor");
    libraryField13.setProcessingInfo(libraryField12);

    LibraryField libraryField14 = new LibraryField(1, "Name", "Descriptor");
    libraryField14.setProcessingInfo(libraryField13);

    LibraryField libraryField15 = new LibraryField(1, "Name", "Descriptor");
    libraryField15.setProcessingInfo(libraryField14);

    LibraryField libraryField16 = new LibraryField(1, "Name", "Descriptor");
    libraryField16.setProcessingInfo(libraryField15);

    SimpleFeatureNamedProcessable simpleFeatureNamedProcessable =
        new SimpleFeatureNamedProcessable();
    simpleFeatureNamedProcessable.setProcessingInfo(libraryField16);

    LibraryField libraryField17 = new LibraryField(1, "Name", "Descriptor");
    libraryField17.setProcessingInfo(simpleFeatureNamedProcessable);

    // Act
    memberNameCleaner.visitLibraryField(libraryClass, libraryField17);

    // Assert that nothing has changed
    assertSame(simpleFeatureNamedProcessable, libraryField17.getProcessingInfo());
  }

  /**
   * Test {@link MemberNameCleaner#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameCleaner#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName("Test visitLibraryField(LibraryClass, LibraryField); given 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameCleaner.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField_givenProcessingInfo() {
    // Arrange
    MemberNameCleaner memberNameCleaner = new MemberNameCleaner();
    LibraryClass libraryClass = new LibraryClass();

    LibraryField libraryField = new LibraryField();
    libraryField.setProcessingInfo("Processing Info");

    // Act
    memberNameCleaner.visitLibraryField(libraryClass, libraryField);

    // Assert
    assertNull(libraryField.getProcessingInfo());
  }

  /**
   * Test {@link MemberNameCleaner#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <ul>
   *   <li>When {@link LibraryField#LibraryField()}.
   *   <li>Then {@link LibraryField#LibraryField()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameCleaner#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryField(LibraryClass, LibraryField); when LibraryField(); then LibraryField() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameCleaner.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField_whenLibraryField_thenLibraryFieldProcessingInfoIsNull() {
    // Arrange
    MemberNameCleaner memberNameCleaner = new MemberNameCleaner();
    LibraryClass libraryClass = new LibraryClass();
    LibraryField libraryField = new LibraryField();

    // Act
    memberNameCleaner.visitLibraryField(libraryClass, libraryField);

    // Assert that nothing has changed
    assertNull(libraryField.getProcessingInfo());
  }

  /**
   * Test {@link MemberNameCleaner#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameCleaner#visitLibraryMethod(LibraryClass, LibraryMethod)}
   */
  @Test
  @DisplayName("Test visitLibraryMethod(LibraryClass, LibraryMethod); given 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameCleaner.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_givenProcessingInfo() {
    // Arrange
    MemberNameCleaner memberNameCleaner = new MemberNameCleaner();
    LibraryClass libraryClass = new LibraryClass();

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo("Processing Info");

    // Act
    memberNameCleaner.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    assertNull(libraryMethod.getProcessingInfo());
  }

  /**
   * Test {@link MemberNameCleaner#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>When {@link LibraryMethod#LibraryMethod()}.
   *   <li>Then {@link LibraryMethod#LibraryMethod()} ProcessingInfo is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link MemberNameCleaner#visitLibraryMethod(LibraryClass, LibraryMethod)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryMethod(LibraryClass, LibraryMethod); when LibraryMethod(); then LibraryMethod() ProcessingInfo is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MemberNameCleaner.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_whenLibraryMethod_thenLibraryMethodProcessingInfoIsNull() {
    // Arrange
    MemberNameCleaner memberNameCleaner = new MemberNameCleaner();
    LibraryClass libraryClass = new LibraryClass();
    LibraryMethod libraryMethod = new LibraryMethod();

    // Act
    memberNameCleaner.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert that nothing has changed
    assertNull(libraryMethod.getProcessingInfo());
  }
}
