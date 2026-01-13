package proguard.optimize;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.classfile.Clazz;
import proguard.classfile.Field;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.CodeAttribute;
import proguard.optimize.info.ClassOptimizationInfo;
import proguard.optimize.info.CodeAttributeOptimizationInfo;
import proguard.optimize.info.FieldOptimizationInfo;
import proguard.optimize.info.MethodOptimizationInfo;
import proguard.optimize.info.ProgramClassOptimizationInfo;

class KeepMarkerDiffblueTest {
  /**
   * Test {@link KeepMarker#visitAnyClass(Clazz)}.
   *
   * <ul>
   *   <li>Then {@link LibraryClass#LibraryClass()} ProcessingInfo {@link ClassOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#visitAnyClass(Clazz)}
   */
  @Test
  @DisplayName(
      "Test visitAnyClass(Clazz); then LibraryClass() ProcessingInfo ClassOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeepMarker.visitAnyClass(Clazz)"})
  void testVisitAnyClass_thenLibraryClassProcessingInfoClassOptimizationInfo() {
    // Arrange
    KeepMarker keepMarker = new KeepMarker();
    LibraryClass clazz = new LibraryClass();

    // Act
    keepMarker.visitAnyClass(clazz);

    // Assert
    Object processingInfo = clazz.getProcessingInfo();
    assertTrue(processingInfo instanceof ClassOptimizationInfo);
    assertNull(((ClassOptimizationInfo) processingInfo).getTargetClass());
    assertNull(((ClassOptimizationInfo) processingInfo).getWrappedClass());
    assertFalse(((ClassOptimizationInfo) processingInfo).hasNoSideEffects());
    assertFalse(((ClassOptimizationInfo) processingInfo).isSimpleEnum());
    assertTrue(((ClassOptimizationInfo) processingInfo).hasSideEffects());
    assertTrue(((ClassOptimizationInfo) processingInfo).isCaught());
    assertTrue(((ClassOptimizationInfo) processingInfo).isDotClassed());
    assertTrue(((ClassOptimizationInfo) processingInfo).isEscaping());
    assertTrue(((ClassOptimizationInfo) processingInfo).isInstanceofed());
    assertTrue(((ClassOptimizationInfo) processingInfo).isInstantiated());
    assertTrue(((ClassOptimizationInfo) processingInfo).isKept());
  }

  /**
   * Test {@link KeepMarker#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then {@link ProgramField#ProgramField()} ProcessingInfo {@link FieldOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#visitProgramField(ProgramClass, ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); then ProgramField() ProcessingInfo FieldOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeepMarker.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_thenProgramFieldProcessingInfoFieldOptimizationInfo() {
    // Arrange
    KeepMarker keepMarker = new KeepMarker();
    ProgramClass programClass = new ProgramClass();
    ProgramField programField = new ProgramField();

    // Act
    keepMarker.visitProgramField(programClass, programField);

    // Assert
    Object processingInfo = programField.getProcessingInfo();
    assertTrue(processingInfo instanceof FieldOptimizationInfo);
    assertNull(((FieldOptimizationInfo) processingInfo).getReferencedClass());
    assertNull(((FieldOptimizationInfo) processingInfo).getValue());
    assertTrue(((FieldOptimizationInfo) processingInfo).isKept());
    assertTrue(((FieldOptimizationInfo) processingInfo).isRead());
    assertTrue(((FieldOptimizationInfo) processingInfo).isWritten());
  }

  /**
   * Test {@link KeepMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod); given 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeepMarker.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_givenProcessingInfo() {
    // Arrange
    KeepMarker keepMarker = new KeepMarker();
    ProgramClass programClass = new ProgramClass();

    ProgramMethod programMethod = new ProgramMethod();
    programMethod.setProcessingInfo("Processing Info");

    // Act
    keepMarker.visitProgramMethod(programClass, programMethod);

    // Assert
    Object processingInfo = programMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertNull(((MethodOptimizationInfo) processingInfo).getReturnValue());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getEscapedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getEscapingParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getModifiedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getReturnedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getUsedParameters());
    assertEquals(0, ((MethodOptimizationInfo) processingInfo).getParameterSize());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoEscapingParameters());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoExternalReturnValues());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoExternalSideEffects());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoSideEffects());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasUnusedParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasSideEffects());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasSynchronizedBlock());
    assertTrue(((MethodOptimizationInfo) processingInfo).isKept());
    assertEquals(Integer.MAX_VALUE, ((MethodOptimizationInfo) processingInfo).getInvocationCount());
  }

  /**
   * Test {@link KeepMarker#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>When {@link ProgramMethod#ProgramMethod()}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#visitProgramMethod(ProgramClass, ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod); when ProgramMethod()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeepMarker.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_whenProgramMethod() {
    // Arrange
    KeepMarker keepMarker = new KeepMarker();
    ProgramClass programClass = new ProgramClass();
    ProgramMethod programMethod = new ProgramMethod();

    // Act
    keepMarker.visitProgramMethod(programClass, programMethod);

    // Assert
    Object processingInfo = programMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertNull(((MethodOptimizationInfo) processingInfo).getReturnValue());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getEscapedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getEscapingParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getModifiedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getReturnedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getUsedParameters());
    assertEquals(0, ((MethodOptimizationInfo) processingInfo).getParameterSize());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoEscapingParameters());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoExternalReturnValues());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoExternalSideEffects());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoSideEffects());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasUnusedParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasSideEffects());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasSynchronizedBlock());
    assertTrue(((MethodOptimizationInfo) processingInfo).isKept());
    assertEquals(Integer.MAX_VALUE, ((MethodOptimizationInfo) processingInfo).getInvocationCount());
  }

  /**
   * Test {@link KeepMarker#visitLibraryField(LibraryClass, LibraryField)}.
   *
   * <ul>
   *   <li>Then {@link LibraryField#LibraryField()} ProcessingInfo {@link FieldOptimizationInfo}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#visitLibraryField(LibraryClass, LibraryField)}
   */
  @Test
  @DisplayName(
      "Test visitLibraryField(LibraryClass, LibraryField); then LibraryField() ProcessingInfo FieldOptimizationInfo")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeepMarker.visitLibraryField(LibraryClass, LibraryField)"})
  void testVisitLibraryField_thenLibraryFieldProcessingInfoFieldOptimizationInfo() {
    // Arrange
    KeepMarker keepMarker = new KeepMarker();
    LibraryClass libraryClass = new LibraryClass();
    LibraryField libraryField = new LibraryField();

    // Act
    keepMarker.visitLibraryField(libraryClass, libraryField);

    // Assert
    Object processingInfo = libraryField.getProcessingInfo();
    assertTrue(processingInfo instanceof FieldOptimizationInfo);
    assertNull(((FieldOptimizationInfo) processingInfo).getReferencedClass());
    assertNull(((FieldOptimizationInfo) processingInfo).getValue());
    assertTrue(((FieldOptimizationInfo) processingInfo).isKept());
    assertTrue(((FieldOptimizationInfo) processingInfo).isRead());
    assertTrue(((FieldOptimizationInfo) processingInfo).isWritten());
  }

  /**
   * Test {@link KeepMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}
   */
  @Test
  @DisplayName("Test visitLibraryMethod(LibraryClass, LibraryMethod); given 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeepMarker.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_givenProcessingInfo() {
    // Arrange
    KeepMarker keepMarker = new KeepMarker();
    LibraryClass libraryClass = new LibraryClass();

    LibraryMethod libraryMethod = new LibraryMethod();
    libraryMethod.setProcessingInfo("Processing Info");

    // Act
    keepMarker.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    Object processingInfo = libraryMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertNull(((MethodOptimizationInfo) processingInfo).getReturnValue());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getEscapedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getEscapingParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getModifiedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getReturnedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getUsedParameters());
    assertEquals(0, ((MethodOptimizationInfo) processingInfo).getParameterSize());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoEscapingParameters());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoExternalReturnValues());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoExternalSideEffects());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoSideEffects());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasUnusedParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasSideEffects());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasSynchronizedBlock());
    assertTrue(((MethodOptimizationInfo) processingInfo).isKept());
    assertEquals(Integer.MAX_VALUE, ((MethodOptimizationInfo) processingInfo).getInvocationCount());
  }

  /**
   * Test {@link KeepMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}.
   *
   * <ul>
   *   <li>When {@link LibraryMethod#LibraryMethod()}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#visitLibraryMethod(LibraryClass, LibraryMethod)}
   */
  @Test
  @DisplayName("Test visitLibraryMethod(LibraryClass, LibraryMethod); when LibraryMethod()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeepMarker.visitLibraryMethod(LibraryClass, LibraryMethod)"})
  void testVisitLibraryMethod_whenLibraryMethod() {
    // Arrange
    KeepMarker keepMarker = new KeepMarker();
    LibraryClass libraryClass = new LibraryClass();
    LibraryMethod libraryMethod = new LibraryMethod();

    // Act
    keepMarker.visitLibraryMethod(libraryClass, libraryMethod);

    // Assert
    Object processingInfo = libraryMethod.getProcessingInfo();
    assertTrue(processingInfo instanceof MethodOptimizationInfo);
    assertNull(((MethodOptimizationInfo) processingInfo).getReturnValue());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getEscapedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getEscapingParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getModifiedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getReturnedParameters());
    assertEquals(-1L, ((MethodOptimizationInfo) processingInfo).getUsedParameters());
    assertEquals(0, ((MethodOptimizationInfo) processingInfo).getParameterSize());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoEscapingParameters());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoExternalReturnValues());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoExternalSideEffects());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasNoSideEffects());
    assertFalse(((MethodOptimizationInfo) processingInfo).hasUnusedParameters());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasSideEffects());
    assertTrue(((MethodOptimizationInfo) processingInfo).hasSynchronizedBlock());
    assertTrue(((MethodOptimizationInfo) processingInfo).isKept());
    assertEquals(Integer.MAX_VALUE, ((MethodOptimizationInfo) processingInfo).getInvocationCount());
  }

  /**
   * Test {@link KeepMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link KeepMarker#visitCodeAttribute(Clazz, Method, CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void KeepMarker.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute() {
    // Arrange
    KeepMarker keepMarker = new KeepMarker();
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    keepMarker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    Object processingInfo = codeAttribute.getProcessingInfo();
    assertTrue(processingInfo instanceof CodeAttributeOptimizationInfo);
    assertTrue(((CodeAttributeOptimizationInfo) processingInfo).isKept());
  }

  /**
   * Test {@link KeepMarker#isKept(Clazz)} with {@code clazz}.
   *
   * <ul>
   *   <li>Given {@link ClassOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#isKept(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isKept(Clazz) with 'clazz'; given ClassOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean KeepMarker.isKept(Clazz)"})
  void testIsKeptWithClazz_givenClassOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    clazz.setProcessingInfo(new ClassOptimizationInfo());

    // Act and Assert
    assertTrue(KeepMarker.isKept(clazz));
  }

  /**
   * Test {@link KeepMarker#isKept(Clazz)} with {@code clazz}.
   *
   * <ul>
   *   <li>Given {@link ProgramClassOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#isKept(Clazz)}
   */
  @Test
  @DisplayName(
      "Test isKept(Clazz) with 'clazz'; given ProgramClassOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean KeepMarker.isKept(Clazz)"})
  void testIsKeptWithClazz_givenProgramClassOptimizationInfo() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    clazz.setProcessingInfo(new ProgramClassOptimizationInfo());

    // Act and Assert
    assertFalse(KeepMarker.isKept(clazz));
  }

  /**
   * Test {@link KeepMarker#isKept(Clazz)} with {@code clazz}.
   *
   * <ul>
   *   <li>When {@link LibraryClass#LibraryClass()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#isKept(Clazz)}
   */
  @Test
  @DisplayName("Test isKept(Clazz) with 'clazz'; when LibraryClass(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean KeepMarker.isKept(Clazz)"})
  void testIsKeptWithClazz_whenLibraryClass_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(KeepMarker.isKept(new LibraryClass()));
  }

  /**
   * Test {@link KeepMarker#isKept(CodeAttribute)} with {@code codeAttribute}.
   *
   * <ul>
   *   <li>Given {@link CodeAttributeOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#isKept(CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test isKept(CodeAttribute) with 'codeAttribute'; given CodeAttributeOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean KeepMarker.isKept(CodeAttribute)"})
  void testIsKeptWithCodeAttribute_givenCodeAttributeOptimizationInfo_thenReturnTrue() {
    // Arrange
    CodeAttribute codeAttribute = new CodeAttribute(1);
    codeAttribute.setProcessingInfo(new CodeAttributeOptimizationInfo());

    // Act and Assert
    assertTrue(KeepMarker.isKept(codeAttribute));
  }

  /**
   * Test {@link KeepMarker#isKept(CodeAttribute)} with {@code codeAttribute}.
   *
   * <ul>
   *   <li>When {@link CodeAttribute#CodeAttribute()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#isKept(CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test isKept(CodeAttribute) with 'codeAttribute'; when CodeAttribute(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean KeepMarker.isKept(CodeAttribute)"})
  void testIsKeptWithCodeAttribute_whenCodeAttribute_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(KeepMarker.isKept(new CodeAttribute()));
  }

  /**
   * Test {@link KeepMarker#isKept(Field)} with {@code field}.
   *
   * <ul>
   *   <li>Given {@link FieldOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#isKept(Field)}
   */
  @Test
  @DisplayName(
      "Test isKept(Field) with 'field'; given FieldOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean KeepMarker.isKept(Field)"})
  void testIsKeptWithField_givenFieldOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryField field = new LibraryField(1, "Name", "Descriptor");
    field.setProcessingInfo(new FieldOptimizationInfo());

    // Act and Assert
    assertTrue(KeepMarker.isKept(field));
  }

  /**
   * Test {@link KeepMarker#isKept(Field)} with {@code field}.
   *
   * <ul>
   *   <li>When {@link LibraryField#LibraryField()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#isKept(Field)}
   */
  @Test
  @DisplayName("Test isKept(Field) with 'field'; when LibraryField(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean KeepMarker.isKept(Field)"})
  void testIsKeptWithField_whenLibraryField_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(KeepMarker.isKept(new LibraryField()));
  }

  /**
   * Test {@link KeepMarker#isKept(Method)} with {@code method}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#isKept(Method)}
   */
  @Test
  @DisplayName(
      "Test isKept(Method) with 'method'; given MethodOptimizationInfo (default constructor); then return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean KeepMarker.isKept(Method)"})
  void testIsKeptWithMethod_givenMethodOptimizationInfo_thenReturnTrue() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertTrue(KeepMarker.isKept(method));
  }

  /**
   * Test {@link KeepMarker#isKept(Method)} with {@code method}.
   *
   * <ul>
   *   <li>When {@link LibraryMethod#LibraryMethod()}.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link KeepMarker#isKept(Method)}
   */
  @Test
  @DisplayName("Test isKept(Method) with 'method'; when LibraryMethod(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean KeepMarker.isKept(Method)"})
  void testIsKeptWithMethod_whenLibraryMethod_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(KeepMarker.isKept(new LibraryMethod()));
  }
}
