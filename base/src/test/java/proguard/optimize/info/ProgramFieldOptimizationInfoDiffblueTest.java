package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import proguard.classfile.Field;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.ConstantValueAttribute;
import proguard.classfile.constant.Constant;
import proguard.classfile.constant.DoubleConstant;
import proguard.classfile.constant.FloatConstant;
import proguard.classfile.constant.IntegerConstant;
import proguard.evaluation.value.CompositeDoubleValue;
import proguard.evaluation.value.DetailedArrayReferenceValue;
import proguard.evaluation.value.ParticularDoubleValue;
import proguard.evaluation.value.ParticularFloatValue;
import proguard.evaluation.value.ParticularIntegerValue;
import proguard.evaluation.value.ReferenceValue;
import proguard.evaluation.value.TopValue;
import proguard.evaluation.value.UnknownDoubleValue;
import proguard.evaluation.value.UnknownReferenceValue;
import proguard.evaluation.value.Value;
import proguard.testutils.cpa.NamedField;

class ProgramFieldOptimizationInfoDiffblueTest {
  /**
   * Test {@link ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(Clazz, Field, boolean)}.
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(Clazz,
   * Field, boolean)}
   */
  @Test
  @DisplayName("Test new ProgramFieldOptimizationInfo(Clazz, Field, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProgramFieldOptimizationInfo.<init>(Clazz, Field, boolean)"})
  void testNewProgramFieldOptimizationInfo() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    // Act
    ProgramFieldOptimizationInfo actualProgramFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);

    // Assert
    Value value = actualProgramFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularDoubleValue);
    assertEquals(0.0d, ((ParticularDoubleValue) value).value());
    assertTrue(value.isCategory2());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(Clazz, Field, boolean)}.
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(Clazz,
   * Field, boolean)}
   */
  @Test
  @DisplayName("Test new ProgramFieldOptimizationInfo(Clazz, Field, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProgramFieldOptimizationInfo.<init>(Clazz, Field, boolean)"})
  void testNewProgramFieldOptimizationInfo2() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(8, "Name", "Descriptor");

    // Act
    ProgramFieldOptimizationInfo actualProgramFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);

    // Assert
    Value value = actualProgramFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularDoubleValue);
    assertEquals(0.0d, ((ParticularDoubleValue) value).value());
    assertTrue(value.isCategory2());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(Clazz, Field, boolean)}.
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(Clazz,
   * Field, boolean)}
   */
  @Test
  @DisplayName("Test new ProgramFieldOptimizationInfo(Clazz, Field, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProgramFieldOptimizationInfo.<init>(Clazz, Field, boolean)"})
  void testNewProgramFieldOptimizationInfo3() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    // Act
    ProgramFieldOptimizationInfo actualProgramFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, false);

    // Assert
    Value value = actualProgramFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularDoubleValue);
    assertEquals(0.0d, ((ParticularDoubleValue) value).value());
    assertTrue(value.isCategory2());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(Clazz, Field, boolean)}.
   *
   * <ul>
   *   <li>Then return Read.
   * </ul>
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(Clazz,
   * Field, boolean)}
   */
  @Test
  @DisplayName("Test new ProgramFieldOptimizationInfo(Clazz, Field, boolean); then return Read")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProgramFieldOptimizationInfo.<init>(Clazz, Field, boolean)"})
  void testNewProgramFieldOptimizationInfo_thenReturnRead() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(Double.SIZE, "Name", "Descriptor");

    // Act
    ProgramFieldOptimizationInfo actualProgramFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);

    // Assert
    Value value = actualProgramFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularDoubleValue);
    assertEquals(0.0d, ((ParticularDoubleValue) value).value());
    assertTrue(value.isCategory2());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
    assertTrue(actualProgramFieldOptimizationInfo.isRead());
    assertTrue(actualProgramFieldOptimizationInfo.isWritten());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(Clazz, Field, boolean)}.
   *
   * <ul>
   *   <li>Then return ReferencedClass is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(Clazz,
   * Field, boolean)}
   */
  @Test
  @DisplayName(
      "Test new ProgramFieldOptimizationInfo(Clazz, Field, boolean); then return ReferencedClass is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProgramFieldOptimizationInfo.<init>(Clazz, Field, boolean)"})
  void testNewProgramFieldOptimizationInfo_thenReturnReferencedClassIsNull() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(Short.SIZE, "Name", "Descriptor");

    // Act
    ProgramFieldOptimizationInfo actualProgramFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, false);

    // Assert
    assertNull(actualProgramFieldOptimizationInfo.getReferencedClass());
    assertNull(actualProgramFieldOptimizationInfo.getValue());
    assertFalse(actualProgramFieldOptimizationInfo.isKept());
    assertFalse(actualProgramFieldOptimizationInfo.isRead());
    assertFalse(actualProgramFieldOptimizationInfo.isWritten());
    assertTrue(actualProgramFieldOptimizationInfo.canBeMadePrivate());
  }

  /**
   * Test {@link
   * ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(ProgramFieldOptimizationInfo)}.
   *
   * <ul>
   *   <li>Then Value return {@link ParticularDoubleValue}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(ProgramFieldOptimizationInfo)}
   */
  @Test
  @DisplayName(
      "Test new ProgramFieldOptimizationInfo(ProgramFieldOptimizationInfo); then Value return ParticularDoubleValue")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProgramFieldOptimizationInfo.<init>(ProgramFieldOptimizationInfo)"})
  void testNewProgramFieldOptimizationInfo_thenValueReturnParticularDoubleValue() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);

    // Act
    ProgramFieldOptimizationInfo actualProgramFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(programFieldOptimizationInfo);

    // Assert
    Value value = actualProgramFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularDoubleValue);
    assertNull(actualProgramFieldOptimizationInfo.getReferencedClass());
    assertEquals(0.0d, ((ParticularDoubleValue) value).value());
    assertFalse(actualProgramFieldOptimizationInfo.isKept());
    assertFalse(actualProgramFieldOptimizationInfo.isRead());
    assertFalse(actualProgramFieldOptimizationInfo.isWritten());
    assertTrue(value.isCategory2());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
    assertTrue(actualProgramFieldOptimizationInfo.canBeMadePrivate());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(Clazz, Field, boolean)}.
   *
   * <ul>
   *   <li>Then Value return {@link ParticularFloatValue}.
   * </ul>
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(Clazz,
   * Field, boolean)}
   */
  @Test
  @DisplayName(
      "Test new ProgramFieldOptimizationInfo(Clazz, Field, boolean); then Value return ParticularFloatValue")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProgramFieldOptimizationInfo.<init>(Clazz, Field, boolean)"})
  void testNewProgramFieldOptimizationInfo_thenValueReturnParticularFloatValue() {
    // Arrange
    LibraryClass clazz = new LibraryClass();

    // Act
    ProgramFieldOptimizationInfo actualProgramFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(
            clazz, new NamedField("Field Name", "Field Descriptor"), true);

    // Assert
    Value value = actualProgramFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularFloatValue);
    assertEquals(0.0f, ((ParticularFloatValue) value).value());
    assertFalse(value.isCategory2());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(Clazz, Field, boolean)}.
   *
   * <ul>
   *   <li>Then Value return {@link ParticularFloatValue}.
   * </ul>
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#ProgramFieldOptimizationInfo(Clazz,
   * Field, boolean)}
   */
  @Test
  @DisplayName(
      "Test new ProgramFieldOptimizationInfo(Clazz, Field, boolean); then Value return ParticularFloatValue")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProgramFieldOptimizationInfo.<init>(Clazz, Field, boolean)"})
  void testNewProgramFieldOptimizationInfo_thenValueReturnParticularFloatValue2() {
    // Arrange
    LibraryClass clazz = new LibraryClass();

    // Act
    ProgramFieldOptimizationInfo actualProgramFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(
            clazz, new NamedField("Field Name", "Field Descriptor"), false);

    // Assert
    Value value = actualProgramFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularFloatValue);
    assertEquals(0.0f, ((ParticularFloatValue) value).value());
    assertFalse(value.isCategory2());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#generalizeReferencedClass(ReferenceValue)}.
   *
   * <p>Method under test: {@link
   * ProgramFieldOptimizationInfo#generalizeReferencedClass(ReferenceValue)}
   */
  @Test
  @DisplayName("Test generalizeReferencedClass(ReferenceValue)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProgramFieldOptimizationInfo.generalizeReferencedClass(ReferenceValue)"})
  void testGeneralizeReferencedClass() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);
    UnknownReferenceValue referencedClass = new UnknownReferenceValue();

    // Act
    programFieldOptimizationInfo.generalizeReferencedClass(referencedClass);

    // Assert
    assertSame(referencedClass, programFieldOptimizationInfo.getReferencedClass());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#generalizeReferencedClass(ReferenceValue)}.
   *
   * <p>Method under test: {@link
   * ProgramFieldOptimizationInfo#generalizeReferencedClass(ReferenceValue)}
   */
  @Test
  @DisplayName("Test generalizeReferencedClass(ReferenceValue)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProgramFieldOptimizationInfo.generalizeReferencedClass(ReferenceValue)"})
  void testGeneralizeReferencedClass2() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);

    ProgramFieldOptimizationInfo programFieldOptimizationInfo2 =
        new ProgramFieldOptimizationInfo(programFieldOptimizationInfo);
    UnknownReferenceValue referencedClass = new UnknownReferenceValue();
    programFieldOptimizationInfo2.generalizeReferencedClass(referencedClass);

    // Act
    programFieldOptimizationInfo2.generalizeReferencedClass(new UnknownReferenceValue());

    // Assert that nothing has changed
    assertSame(referencedClass, programFieldOptimizationInfo2.getReferencedClass());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#generalizeReferencedClass(ReferenceValue)}.
   *
   * <p>Method under test: {@link
   * ProgramFieldOptimizationInfo#generalizeReferencedClass(ReferenceValue)}
   */
  @Test
  @DisplayName("Test generalizeReferencedClass(ReferenceValue)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProgramFieldOptimizationInfo.generalizeReferencedClass(ReferenceValue)"})
  void testGeneralizeReferencedClass3() {
    // Arrange
    DetailedArrayReferenceValue referencedClass = mock(DetailedArrayReferenceValue.class);
    when(referencedClass.generalize(Mockito.<ReferenceValue>any()))
        .thenReturn(new UnknownReferenceValue());
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);

    ProgramFieldOptimizationInfo programFieldOptimizationInfo2 =
        new ProgramFieldOptimizationInfo(programFieldOptimizationInfo);
    programFieldOptimizationInfo2.generalizeReferencedClass(referencedClass);

    // Act
    programFieldOptimizationInfo2.generalizeReferencedClass(new UnknownReferenceValue());

    // Assert
    verify(referencedClass).generalize(isA(ReferenceValue.class));
    ReferenceValue referencedClass2 = programFieldOptimizationInfo2.getReferencedClass();
    assertTrue(referencedClass2 instanceof UnknownReferenceValue);
    assertEquals("Ljava/lang/Object;", referencedClass2.getType());
    assertNull(referencedClass2.getReferencedClass());
    assertEquals(0, referencedClass2.isNotNull());
    assertEquals(0, referencedClass2.isNull());
    assertFalse(referencedClass2.isCategory2());
    assertFalse(referencedClass2.isParticular());
    assertFalse(referencedClass2.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#resetValue(Clazz, Field)}.
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#resetValue(Clazz, Field)}
   */
  @Test
  @DisplayName("Test resetValue(Clazz, Field)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProgramFieldOptimizationInfo.resetValue(Clazz, Field)"})
  void testResetValue() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);
    LibraryClass clazz2 = new LibraryClass();
    LibraryField field2 = new LibraryField(8, "Name", "Descriptor");

    // Act
    programFieldOptimizationInfo.resetValue(clazz2, field2);

    // Assert that nothing has changed
    assertTrue(programFieldOptimizationInfo.getValue() instanceof ParticularDoubleValue);
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#resetValue(Clazz, Field)}.
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#resetValue(Clazz, Field)}
   */
  @Test
  @DisplayName("Test resetValue(Clazz, Field)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProgramFieldOptimizationInfo.resetValue(Clazz, Field)"})
  void testResetValue2() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);
    LibraryClass clazz2 = new LibraryClass();

    // Act
    programFieldOptimizationInfo.resetValue(
        clazz2, new NamedField("Field Name", "Field Descriptor"));

    // Assert
    Value value = programFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularFloatValue);
    assertEquals(0.0f, ((ParticularFloatValue) value).value());
    assertFalse(value.isCategory2());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#generalizeValue(Value)}.
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#generalizeValue(Value)}
   */
  @Test
  @DisplayName("Test generalizeValue(Value)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ProgramFieldOptimizationInfo.generalizeValue(Value)"})
  void testGeneralizeValue() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);
    UnknownDoubleValue doubleValue1 = new UnknownDoubleValue();

    // Act
    programFieldOptimizationInfo.generalizeValue(
        new CompositeDoubleValue(doubleValue1, (byte) 'A', new UnknownDoubleValue()));

    // Assert
    Value value = programFieldOptimizationInfo.getValue();
    assertTrue(value instanceof UnknownDoubleValue);
    assertEquals(doubleValue1, value);
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz,
   * Field, ConstantValueAttribute)}
   */
  @Test
  @DisplayName("Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);
    Constant[] constantPool = new Constant[] {new DoubleConstant()};
    ProgramClass clazz2 = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field2 = new LibraryField();

    // Act
    programFieldOptimizationInfo.visitConstantValueAttribute(
        clazz2, field2, new ConstantValueAttribute());

    // Assert that nothing has changed
    Value value = programFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularDoubleValue);
    assertEquals(0.0d, ((ParticularDoubleValue) value).value());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz,
   * Field, ConstantValueAttribute)}
   */
  @Test
  @DisplayName("Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute2() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);
    Constant[] constantPool = new Constant[] {new DoubleConstant(10.0d)};
    ProgramClass clazz2 = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field2 = new LibraryField();

    // Act
    programFieldOptimizationInfo.visitConstantValueAttribute(
        clazz2, field2, new ConstantValueAttribute());

    // Assert
    Value value = programFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularDoubleValue);
    assertEquals(10.0d, ((ParticularDoubleValue) value).value());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz,
   * Field, ConstantValueAttribute)}
   */
  @Test
  @DisplayName("Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute3() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);
    Constant[] constantPool = new Constant[] {new FloatConstant()};
    ProgramClass clazz2 = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field2 = new LibraryField();

    // Act
    programFieldOptimizationInfo.visitConstantValueAttribute(
        clazz2, field2, new ConstantValueAttribute());

    // Assert
    Value value = programFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularFloatValue);
    assertEquals(0.0f, ((ParticularFloatValue) value).value());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz,
   * Field, ConstantValueAttribute)}
   */
  @Test
  @DisplayName("Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute4() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);
    Constant[] constantPool = new Constant[] {new FloatConstant(10.0f)};
    ProgramClass clazz2 = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field2 = new LibraryField();

    // Act
    programFieldOptimizationInfo.visitConstantValueAttribute(
        clazz2, field2, new ConstantValueAttribute());

    // Assert
    Value value = programFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularFloatValue);
    assertEquals(10.0f, ((ParticularFloatValue) value).value());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz,
   * Field, ConstantValueAttribute)}
   */
  @Test
  @DisplayName("Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute5() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);
    Constant[] constantPool = new Constant[] {new IntegerConstant()};
    ProgramClass clazz2 = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field2 = new LibraryField();

    // Act
    programFieldOptimizationInfo.visitConstantValueAttribute(
        clazz2, field2, new ConstantValueAttribute());

    // Assert
    Value value = programFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularIntegerValue);
    assertEquals(0, ((ParticularIntegerValue) value).value());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz,
   * Field, ConstantValueAttribute)}
   */
  @Test
  @DisplayName("Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute6() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);
    Constant[] constantPool = new Constant[] {new IntegerConstant(42)};
    ProgramClass clazz2 = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field2 = new LibraryField();

    // Act
    programFieldOptimizationInfo.visitConstantValueAttribute(
        clazz2, field2, new ConstantValueAttribute());

    // Assert
    Value value = programFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularIntegerValue);
    assertEquals(42, ((ParticularIntegerValue) value).value());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz,
   * Field, ConstantValueAttribute)}
   */
  @Test
  @DisplayName("Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute7() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);
    Constant[] constantPool = new Constant[] {new IntegerConstant(1)};
    ProgramClass clazz2 = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field2 = new LibraryField();

    // Act
    programFieldOptimizationInfo.visitConstantValueAttribute(
        clazz2, field2, new ConstantValueAttribute());

    // Assert
    Value value = programFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularIntegerValue);
    assertEquals(1, ((ParticularIntegerValue) value).value());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz, Field,
   * ConstantValueAttribute)}.
   *
   * <p>Method under test: {@link ProgramFieldOptimizationInfo#visitConstantValueAttribute(Clazz,
   * Field, ConstantValueAttribute)}
   */
  @Test
  @DisplayName("Test visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.visitConstantValueAttribute(Clazz, Field, ConstantValueAttribute)"
  })
  void testVisitConstantValueAttribute8() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field, true);
    Constant[] constantPool = new Constant[] {new IntegerConstant(-1)};
    ProgramClass clazz2 = new ProgramClass(1, 3, constantPool, 1, 1, 1);
    LibraryField field2 = new LibraryField();

    // Act
    programFieldOptimizationInfo.visitConstantValueAttribute(
        clazz2, field2, new ConstantValueAttribute());

    // Assert
    Value value = programFieldOptimizationInfo.getValue();
    assertTrue(value instanceof ParticularIntegerValue);
    assertEquals(-1, ((ParticularIntegerValue) value).value());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#setProgramFieldOptimizationInfo(Clazz, Field,
   * boolean)}.
   *
   * <p>Method under test: {@link
   * ProgramFieldOptimizationInfo#setProgramFieldOptimizationInfo(Clazz, Field, boolean)}
   */
  @Test
  @DisplayName("Test setProgramFieldOptimizationInfo(Clazz, Field, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(Clazz, Field, boolean)"
  })
  void testSetProgramFieldOptimizationInfo() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    // Act
    ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(clazz, field, true);

    // Assert
    Object processingInfo = field.getProcessingInfo();
    Value value = ((ProgramFieldOptimizationInfo) processingInfo).getValue();
    assertTrue(value instanceof ParticularDoubleValue);
    assertTrue(processingInfo instanceof ProgramFieldOptimizationInfo);
    assertEquals(0.0d, ((ParticularDoubleValue) value).value());
    assertTrue(value.isCategory2());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#setProgramFieldOptimizationInfo(Clazz, Field,
   * boolean)}.
   *
   * <p>Method under test: {@link
   * ProgramFieldOptimizationInfo#setProgramFieldOptimizationInfo(Clazz, Field, boolean)}
   */
  @Test
  @DisplayName("Test setProgramFieldOptimizationInfo(Clazz, Field, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(Clazz, Field, boolean)"
  })
  void testSetProgramFieldOptimizationInfo2() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    NamedField field = new NamedField("Field Name", "Field Descriptor");

    // Act
    ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(clazz, field, true);

    // Assert
    Object processingInfo = field.getProcessingInfo();
    Value value = ((ProgramFieldOptimizationInfo) processingInfo).getValue();
    assertTrue(value instanceof ParticularFloatValue);
    assertTrue(processingInfo instanceof ProgramFieldOptimizationInfo);
    assertEquals(0.0f, ((ParticularFloatValue) value).value());
    assertFalse(value.isCategory2());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#setProgramFieldOptimizationInfo(Clazz, Field,
   * boolean)}.
   *
   * <p>Method under test: {@link
   * ProgramFieldOptimizationInfo#setProgramFieldOptimizationInfo(Clazz, Field, boolean)}
   */
  @Test
  @DisplayName("Test setProgramFieldOptimizationInfo(Clazz, Field, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(Clazz, Field, boolean)"
  })
  void testSetProgramFieldOptimizationInfo3() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(Double.SIZE, "Name", "Descriptor");

    // Act
    ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(clazz, field, true);

    // Assert
    Object processingInfo = field.getProcessingInfo();
    Value value = ((ProgramFieldOptimizationInfo) processingInfo).getValue();
    assertTrue(value instanceof ParticularDoubleValue);
    assertTrue(processingInfo instanceof ProgramFieldOptimizationInfo);
    assertEquals(0.0d, ((ParticularDoubleValue) value).value());
    assertTrue(value.isCategory2());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
    assertTrue(((ProgramFieldOptimizationInfo) processingInfo).isRead());
    assertTrue(((ProgramFieldOptimizationInfo) processingInfo).isWritten());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#setProgramFieldOptimizationInfo(Clazz, Field,
   * boolean)}.
   *
   * <p>Method under test: {@link
   * ProgramFieldOptimizationInfo#setProgramFieldOptimizationInfo(Clazz, Field, boolean)}
   */
  @Test
  @DisplayName("Test setProgramFieldOptimizationInfo(Clazz, Field, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(Clazz, Field, boolean)"
  })
  void testSetProgramFieldOptimizationInfo4() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(8, "Name", "Descriptor");

    // Act
    ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(clazz, field, true);

    // Assert
    Object processingInfo = field.getProcessingInfo();
    Value value = ((ProgramFieldOptimizationInfo) processingInfo).getValue();
    assertTrue(value instanceof ParticularDoubleValue);
    assertTrue(processingInfo instanceof ProgramFieldOptimizationInfo);
    assertEquals(0.0d, ((ParticularDoubleValue) value).value());
    assertTrue(value.isCategory2());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#setProgramFieldOptimizationInfo(Clazz, Field,
   * boolean)}.
   *
   * <p>Method under test: {@link
   * ProgramFieldOptimizationInfo#setProgramFieldOptimizationInfo(Clazz, Field, boolean)}
   */
  @Test
  @DisplayName("Test setProgramFieldOptimizationInfo(Clazz, Field, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(Clazz, Field, boolean)"
  })
  void testSetProgramFieldOptimizationInfo5() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    // Act
    ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(clazz, field, false);

    // Assert
    Object processingInfo = field.getProcessingInfo();
    Value value = ((ProgramFieldOptimizationInfo) processingInfo).getValue();
    assertTrue(value instanceof ParticularDoubleValue);
    assertTrue(processingInfo instanceof ProgramFieldOptimizationInfo);
    assertEquals(0.0d, ((ParticularDoubleValue) value).value());
    assertTrue(value.isCategory2());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#setProgramFieldOptimizationInfo(Clazz, Field,
   * boolean)}.
   *
   * <p>Method under test: {@link
   * ProgramFieldOptimizationInfo#setProgramFieldOptimizationInfo(Clazz, Field, boolean)}
   */
  @Test
  @DisplayName("Test setProgramFieldOptimizationInfo(Clazz, Field, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(Clazz, Field, boolean)"
  })
  void testSetProgramFieldOptimizationInfo6() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryField field = new LibraryField(Short.SIZE, "Name", "Descriptor");

    // Act
    ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(clazz, field, false);

    // Assert
    Object processingInfo = field.getProcessingInfo();
    assertTrue(processingInfo instanceof ProgramFieldOptimizationInfo);
    assertNull(((ProgramFieldOptimizationInfo) processingInfo).getReferencedClass());
    assertNull(((ProgramFieldOptimizationInfo) processingInfo).getValue());
    assertFalse(((ProgramFieldOptimizationInfo) processingInfo).isKept());
    assertFalse(((ProgramFieldOptimizationInfo) processingInfo).isRead());
    assertFalse(((ProgramFieldOptimizationInfo) processingInfo).isWritten());
    assertTrue(((ProgramFieldOptimizationInfo) processingInfo).canBeMadePrivate());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#setProgramFieldOptimizationInfo(Clazz, Field,
   * boolean)}.
   *
   * <p>Method under test: {@link
   * ProgramFieldOptimizationInfo#setProgramFieldOptimizationInfo(Clazz, Field, boolean)}
   */
  @Test
  @DisplayName("Test setProgramFieldOptimizationInfo(Clazz, Field, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(Clazz, Field, boolean)"
  })
  void testSetProgramFieldOptimizationInfo7() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    NamedField field = new NamedField("Field Name", "Field Descriptor");

    // Act
    ProgramFieldOptimizationInfo.setProgramFieldOptimizationInfo(clazz, field, false);

    // Assert
    Object processingInfo = field.getProcessingInfo();
    Value value = ((ProgramFieldOptimizationInfo) processingInfo).getValue();
    assertTrue(value instanceof ParticularFloatValue);
    assertTrue(processingInfo instanceof ProgramFieldOptimizationInfo);
    assertEquals(0.0f, ((ParticularFloatValue) value).value());
    assertFalse(value.isCategory2());
    assertTrue(value.isParticular());
    assertTrue(value.isSpecific());
  }

  /**
   * Test {@link ProgramFieldOptimizationInfo#getProgramFieldOptimizationInfo(Field)}.
   *
   * <ul>
   *   <li>When {@link LibraryField#LibraryField()}.
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link
   * ProgramFieldOptimizationInfo#getProgramFieldOptimizationInfo(Field)}
   */
  @Test
  @DisplayName(
      "Test getProgramFieldOptimizationInfo(Field); when LibraryField(); then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "ProgramFieldOptimizationInfo ProgramFieldOptimizationInfo.getProgramFieldOptimizationInfo(Field)"
  })
  void testGetProgramFieldOptimizationInfo_whenLibraryField_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(ProgramFieldOptimizationInfo.getProgramFieldOptimizationInfo(new LibraryField()));
  }
}
