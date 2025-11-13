package proguard.optimize.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
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
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.evaluation.ParticularReferenceValueFactory;
import proguard.evaluation.value.BasicValueFactory;
import proguard.evaluation.value.IdentifiedReferenceValue;
import proguard.evaluation.value.MultiTypedReferenceValue;
import proguard.evaluation.value.ReferenceValue;
import proguard.evaluation.value.TopValue;
import proguard.evaluation.value.TypedReferenceValue;
import proguard.evaluation.value.UnknownReferenceValue;
import proguard.evaluation.value.Value;
import proguard.evaluation.value.ValueFactory;
import proguard.evaluation.value.object.AnalyzedObject;
import proguard.optimize.info.FieldOptimizationInfo;
import proguard.optimize.info.MethodOptimizationInfo;
import proguard.optimize.info.ProgramFieldOptimizationInfo;

class StoringInvocationUnitDiffblueTest {
  /**
   * Test {@link StoringInvocationUnit#StoringInvocationUnit(ValueFactory)}.
   *
   * <p>Method under test: {@link StoringInvocationUnit#StoringInvocationUnit(ValueFactory)}
   */
  @Test
  @DisplayName("Test new StoringInvocationUnit(ValueFactory)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void StoringInvocationUnit.<init>(ValueFactory)"})
  void testNewStoringInvocationUnit() {
    // Arrange, Act and Assert
    Value exceptionValue =
        new StoringInvocationUnit(new ParticularReferenceValueFactory())
            .getExceptionValue(null, null);
    assertTrue(exceptionValue instanceof IdentifiedReferenceValue);
    assertEquals("Ljava/lang/Throwable;", ((IdentifiedReferenceValue) exceptionValue).getType());
    AnalyzedObject value = ((IdentifiedReferenceValue) exceptionValue).getValue();
    assertNull(value.getPreciseValue());
    assertNull(((IdentifiedReferenceValue) exceptionValue).getReferencedClass());
    assertNull(value.getModeledOrNullValue());
    assertEquals(-1, ((IdentifiedReferenceValue) exceptionValue).isNull());
    assertEquals(1, ((IdentifiedReferenceValue) exceptionValue).isNotNull());
    assertFalse(exceptionValue.isCategory2());
    assertFalse(exceptionValue.isParticular());
    assertTrue(((IdentifiedReferenceValue) exceptionValue).mayBeExtension());
    assertTrue(exceptionValue.isSpecific());
  }

  /**
   * Test {@link StoringInvocationUnit#StoringInvocationUnit(ValueFactory, boolean, boolean,
   * boolean)}.
   *
   * <p>Method under test: {@link StoringInvocationUnit#StoringInvocationUnit(ValueFactory, boolean,
   * boolean, boolean)}
   */
  @Test
  @DisplayName("Test new StoringInvocationUnit(ValueFactory, boolean, boolean, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void StoringInvocationUnit.<init>(ValueFactory, boolean, boolean, boolean)"})
  void testNewStoringInvocationUnit2() {
    // Arrange and Act
    StoringInvocationUnit actualStoringInvocationUnit =
        new StoringInvocationUnit(new ParticularReferenceValueFactory(), true, true, true);

    // Assert
    Value exceptionValue = actualStoringInvocationUnit.getExceptionValue(null, null);
    assertTrue(exceptionValue instanceof IdentifiedReferenceValue);
    assertEquals("Ljava/lang/Throwable;", ((IdentifiedReferenceValue) exceptionValue).getType());
    AnalyzedObject value = ((IdentifiedReferenceValue) exceptionValue).getValue();
    assertNull(value.getPreciseValue());
    assertNull(((IdentifiedReferenceValue) exceptionValue).getReferencedClass());
    assertNull(value.getModeledOrNullValue());
    assertEquals(-1, ((IdentifiedReferenceValue) exceptionValue).isNull());
    assertEquals(1, ((IdentifiedReferenceValue) exceptionValue).isNotNull());
    assertFalse(exceptionValue.isCategory2());
    assertFalse(exceptionValue.isParticular());
    assertTrue(((IdentifiedReferenceValue) exceptionValue).mayBeExtension());
    assertTrue(exceptionValue.isSpecific());
  }

  /**
   * Test {@link StoringInvocationUnit#setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue)}.
   *
   * <p>Method under test: {@link StoringInvocationUnit#setFieldClassValue(Clazz, FieldrefConstant,
   * ReferenceValue)}
   */
  @Test
  @DisplayName("Test setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void StoringInvocationUnit.setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue)"
  })
  void testSetFieldClassValue() {
    // Arrange
    StoringInvocationUnit storingInvocationUnit =
        new StoringInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();
    LibraryClass clazz2 = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz2, field, true);

    ProgramFieldOptimizationInfo programFieldOptimizationInfo2 =
        new ProgramFieldOptimizationInfo(programFieldOptimizationInfo);
    UnknownReferenceValue referencedClass = new UnknownReferenceValue();
    programFieldOptimizationInfo2.generalizeReferencedClass(referencedClass);

    LibraryField referencedField = new LibraryField(1, "Name", "Descriptor");
    referencedField.setProcessingInfo(programFieldOptimizationInfo2);
    FieldrefConstant refConstant = new FieldrefConstant(1, 1, new LibraryClass(), referencedField);

    // Act
    storingInvocationUnit.setFieldClassValue(clazz, refConstant, new UnknownReferenceValue());

    // Assert
    Field field2 = refConstant.referencedField;
    assertTrue(field2 instanceof LibraryField);
    assertTrue(
        storingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
    Object processingInfo = field2.getProcessingInfo();
    ReferenceValue referencedClass2 =
        ((ProgramFieldOptimizationInfo) processingInfo).getReferencedClass();
    assertTrue(referencedClass2 instanceof UnknownReferenceValue);
    assertTrue(processingInfo instanceof ProgramFieldOptimizationInfo);
    assertEquals(BasicValueFactory.REFERENCE_VALUE, referencedClass2);
    assertSame(referencedClass, referencedClass2);
  }

  /**
   * Test {@link StoringInvocationUnit#setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue)}.
   *
   * <p>Method under test: {@link StoringInvocationUnit#setFieldClassValue(Clazz, FieldrefConstant,
   * ReferenceValue)}
   */
  @Test
  @DisplayName("Test setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void StoringInvocationUnit.setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue)"
  })
  void testSetFieldClassValue2() {
    // Arrange
    StoringInvocationUnit storingInvocationUnit =
        new StoringInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();
    LibraryClass clazz2 = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz2, field, true);

    ProgramFieldOptimizationInfo programFieldOptimizationInfo2 =
        new ProgramFieldOptimizationInfo(programFieldOptimizationInfo);
    programFieldOptimizationInfo2.generalizeReferencedClass(null);

    LibraryField referencedField = new LibraryField(1, "Name", "Descriptor");
    referencedField.setProcessingInfo(programFieldOptimizationInfo2);
    FieldrefConstant refConstant = new FieldrefConstant(1, 1, new LibraryClass(), referencedField);
    UnknownReferenceValue value = new UnknownReferenceValue();

    // Act
    storingInvocationUnit.setFieldClassValue(clazz, refConstant, value);

    // Assert
    Field field2 = refConstant.referencedField;
    assertTrue(field2 instanceof LibraryField);
    assertTrue(
        storingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
    Object processingInfo = field2.getProcessingInfo();
    assertTrue(processingInfo instanceof ProgramFieldOptimizationInfo);
    assertSame(value, ((ProgramFieldOptimizationInfo) processingInfo).getReferencedClass());
  }

  /**
   * Test {@link StoringInvocationUnit#setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue)}.
   *
   * <p>Method under test: {@link StoringInvocationUnit#setFieldClassValue(Clazz, FieldrefConstant,
   * ReferenceValue)}
   */
  @Test
  @DisplayName("Test setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void StoringInvocationUnit.setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue)"
  })
  void testSetFieldClassValue3() {
    // Arrange
    StoringInvocationUnit storingInvocationUnit =
        new StoringInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();
    LibraryClass clazz2 = new LibraryClass();
    LibraryField field = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz2, field, true);

    ProgramFieldOptimizationInfo programFieldOptimizationInfo2 =
        new ProgramFieldOptimizationInfo(programFieldOptimizationInfo);
    TypedReferenceValue type = new TypedReferenceValue("Type", new LibraryClass(), true, true);
    MultiTypedReferenceValue referencedClass = new MultiTypedReferenceValue(type, true);
    programFieldOptimizationInfo2.generalizeReferencedClass(referencedClass);

    LibraryField referencedField = new LibraryField(1, "Name", "Descriptor");
    referencedField.setProcessingInfo(programFieldOptimizationInfo2);
    FieldrefConstant refConstant = new FieldrefConstant(1, 1, new LibraryClass(), referencedField);

    // Act
    storingInvocationUnit.setFieldClassValue(clazz, refConstant, new UnknownReferenceValue());

    // Assert
    Field field2 = refConstant.referencedField;
    assertTrue(field2 instanceof LibraryField);
    assertTrue(
        storingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
    Object processingInfo = field2.getProcessingInfo();
    ReferenceValue referencedClass2 =
        ((ProgramFieldOptimizationInfo) processingInfo).getReferencedClass();
    assertTrue(referencedClass2 instanceof MultiTypedReferenceValue);
    assertTrue(processingInfo instanceof ProgramFieldOptimizationInfo);
    assertEquals(referencedClass, referencedClass2);
  }

  /**
   * Test {@link StoringInvocationUnit#setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue)}.
   *
   * <ul>
   *   <li>Given {@link FieldOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link StoringInvocationUnit#setFieldClassValue(Clazz, FieldrefConstant,
   * ReferenceValue)}
   */
  @Test
  @DisplayName(
      "Test setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue); given FieldOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void StoringInvocationUnit.setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue)"
  })
  void testSetFieldClassValue_givenFieldOptimizationInfo() {
    // Arrange
    StoringInvocationUnit storingInvocationUnit =
        new StoringInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();

    LibraryField referencedField = new LibraryField();
    referencedField.setProcessingInfo(new FieldOptimizationInfo());
    FieldrefConstant refConstant = new FieldrefConstant(1, 1, new LibraryClass(), referencedField);

    // Act
    storingInvocationUnit.setFieldClassValue(clazz, refConstant, new UnknownReferenceValue());

    // Assert
    assertTrue(
        storingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
  }

  /**
   * Test {@link StoringInvocationUnit#setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue)}.
   *
   * <ul>
   *   <li>When {@link FieldrefConstant#FieldrefConstant()}.
   * </ul>
   *
   * <p>Method under test: {@link StoringInvocationUnit#setFieldClassValue(Clazz, FieldrefConstant,
   * ReferenceValue)}
   */
  @Test
  @DisplayName(
      "Test setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue); when FieldrefConstant()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void StoringInvocationUnit.setFieldClassValue(Clazz, FieldrefConstant, ReferenceValue)"
  })
  void testSetFieldClassValue_whenFieldrefConstant() {
    // Arrange
    StoringInvocationUnit storingInvocationUnit =
        new StoringInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();
    FieldrefConstant refConstant = new FieldrefConstant();

    // Act
    storingInvocationUnit.setFieldClassValue(clazz, refConstant, new UnknownReferenceValue());

    // Assert
    assertTrue(
        storingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
  }

  /**
   * Test {@link StoringInvocationUnit#setFieldValue(Clazz, FieldrefConstant, Value)}.
   *
   * <ul>
   *   <li>Given {@link FieldOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link StoringInvocationUnit#setFieldValue(Clazz, FieldrefConstant,
   * Value)}
   */
  @Test
  @DisplayName(
      "Test setFieldValue(Clazz, FieldrefConstant, Value); given FieldOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void StoringInvocationUnit.setFieldValue(Clazz, FieldrefConstant, Value)"})
  void testSetFieldValue_givenFieldOptimizationInfo() {
    // Arrange
    StoringInvocationUnit storingInvocationUnit =
        new StoringInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();

    LibraryField referencedField = new LibraryField();
    referencedField.setProcessingInfo(new FieldOptimizationInfo());
    FieldrefConstant refConstant = new FieldrefConstant(1, 1, new LibraryClass(), referencedField);

    // Act
    storingInvocationUnit.setFieldValue(clazz, refConstant, new TopValue());

    // Assert
    assertTrue(
        storingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
  }

  /**
   * Test {@link StoringInvocationUnit#setFieldValue(Clazz, FieldrefConstant, Value)}.
   *
   * <ul>
   *   <li>When {@link FieldrefConstant#FieldrefConstant()}.
   * </ul>
   *
   * <p>Method under test: {@link StoringInvocationUnit#setFieldValue(Clazz, FieldrefConstant,
   * Value)}
   */
  @Test
  @DisplayName("Test setFieldValue(Clazz, FieldrefConstant, Value); when FieldrefConstant()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void StoringInvocationUnit.setFieldValue(Clazz, FieldrefConstant, Value)"})
  void testSetFieldValue_whenFieldrefConstant() {
    // Arrange
    StoringInvocationUnit storingInvocationUnit =
        new StoringInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();
    FieldrefConstant refConstant = new FieldrefConstant();

    // Act
    storingInvocationUnit.setFieldValue(clazz, refConstant, new TopValue());

    // Assert
    assertTrue(
        storingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
  }

  /**
   * Test {@link StoringInvocationUnit#setMethodParameterValue(Clazz, AnyMethodrefConstant, int,
   * Value)}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link StoringInvocationUnit#setMethodParameterValue(Clazz,
   * AnyMethodrefConstant, int, Value)}
   */
  @Test
  @DisplayName(
      "Test setMethodParameterValue(Clazz, AnyMethodrefConstant, int, Value); given MethodOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void StoringInvocationUnit.setMethodParameterValue(Clazz, AnyMethodrefConstant, int, Value)"
  })
  void testSetMethodParameterValue_givenMethodOptimizationInfo() {
    // Arrange
    StoringInvocationUnit storingInvocationUnit =
        new StoringInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();

    LibraryMethod referencedMethod = new LibraryMethod();
    referencedMethod.setProcessingInfo(new MethodOptimizationInfo());
    InterfaceMethodrefConstant refConstant =
        new InterfaceMethodrefConstant(1, 1, new LibraryClass(), referencedMethod);

    // Act
    storingInvocationUnit.setMethodParameterValue(clazz, refConstant, 1, new TopValue());

    // Assert
    assertTrue(
        storingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
  }

  /**
   * Test {@link StoringInvocationUnit#setMethodParameterValue(Clazz, AnyMethodrefConstant, int,
   * Value)}.
   *
   * <ul>
   *   <li>When {@link InterfaceMethodrefConstant#InterfaceMethodrefConstant()}.
   * </ul>
   *
   * <p>Method under test: {@link StoringInvocationUnit#setMethodParameterValue(Clazz,
   * AnyMethodrefConstant, int, Value)}
   */
  @Test
  @DisplayName(
      "Test setMethodParameterValue(Clazz, AnyMethodrefConstant, int, Value); when InterfaceMethodrefConstant()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void StoringInvocationUnit.setMethodParameterValue(Clazz, AnyMethodrefConstant, int, Value)"
  })
  void testSetMethodParameterValue_whenInterfaceMethodrefConstant() {
    // Arrange
    StoringInvocationUnit storingInvocationUnit =
        new StoringInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();
    InterfaceMethodrefConstant refConstant = new InterfaceMethodrefConstant();

    // Act
    storingInvocationUnit.setMethodParameterValue(clazz, refConstant, 1, new TopValue());

    // Assert
    assertTrue(
        storingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
  }

  /**
   * Test {@link StoringInvocationUnit#setMethodReturnValue(Clazz, Method, Value)}.
   *
   * <p>Method under test: {@link StoringInvocationUnit#setMethodReturnValue(Clazz, Method, Value)}
   */
  @Test
  @DisplayName("Test setMethodReturnValue(Clazz, Method, Value)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void StoringInvocationUnit.setMethodReturnValue(Clazz, Method, Value)"})
  void testSetMethodReturnValue() {
    // Arrange
    StoringInvocationUnit storingInvocationUnit =
        new StoringInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act
    storingInvocationUnit.setMethodReturnValue(clazz, method, new TopValue());

    // Assert
    assertTrue(
        storingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
  }

  /**
   * Test {@link StoringInvocationUnit#getFieldClassValue(Field)} with {@code field}.
   *
   * <p>Method under test: {@link StoringInvocationUnit#getFieldClassValue(Field)}
   */
  @Test
  @DisplayName("Test getFieldClassValue(Field) with 'field'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"ReferenceValue StoringInvocationUnit.getFieldClassValue(Field)"})
  void testGetFieldClassValueWithField() {
    // Arrange
    LibraryField field = new LibraryField(1, "Name", "Descriptor");
    LibraryClass clazz = new LibraryClass();
    LibraryField field2 = new LibraryField(1, "Name", "Descriptor");

    ProgramFieldOptimizationInfo programFieldOptimizationInfo =
        new ProgramFieldOptimizationInfo(clazz, field2, true);
    field.setProcessingInfo(programFieldOptimizationInfo);

    // Act and Assert
    assertNull(StoringInvocationUnit.getFieldClassValue(field));
  }

  /**
   * Test {@link StoringInvocationUnit#getFieldClassValue(Field)} with {@code field}.
   *
   * <ul>
   *   <li>Given {@link FieldOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link StoringInvocationUnit#getFieldClassValue(Field)}
   */
  @Test
  @DisplayName(
      "Test getFieldClassValue(Field) with 'field'; given FieldOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"ReferenceValue StoringInvocationUnit.getFieldClassValue(Field)"})
  void testGetFieldClassValueWithField_givenFieldOptimizationInfo() {
    // Arrange
    LibraryField field = new LibraryField(1, "Name", "Descriptor");
    field.setProcessingInfo(new FieldOptimizationInfo());

    // Act and Assert
    assertNull(StoringInvocationUnit.getFieldClassValue(field));
  }

  /**
   * Test {@link StoringInvocationUnit#getFieldValue(Field)} with {@code field}.
   *
   * <ul>
   *   <li>Given {@link FieldOptimizationInfo} (default constructor).
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link StoringInvocationUnit#getFieldValue(Field)}
   */
  @Test
  @DisplayName(
      "Test getFieldValue(Field) with 'field'; given FieldOptimizationInfo (default constructor); then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Value StoringInvocationUnit.getFieldValue(Field)"})
  void testGetFieldValueWithField_givenFieldOptimizationInfo_thenReturnNull() {
    // Arrange
    LibraryField field = new LibraryField(1, "Name", "Descriptor");
    field.setProcessingInfo(new FieldOptimizationInfo());

    // Act and Assert
    assertNull(StoringInvocationUnit.getFieldValue(field));
  }

  /**
   * Test {@link StoringInvocationUnit#getMethodParameterValue(Method, int)} with {@code method},
   * {@code parameterIndex}.
   *
   * <ul>
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link StoringInvocationUnit#getMethodParameterValue(Method, int)}
   */
  @Test
  @DisplayName(
      "Test getMethodParameterValue(Method, int) with 'method', 'parameterIndex'; then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Value StoringInvocationUnit.getMethodParameterValue(Method, int)"})
  void testGetMethodParameterValueWithMethodParameterIndex_thenReturnNull() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertNull(StoringInvocationUnit.getMethodParameterValue(method, 1));
  }

  /**
   * Test {@link StoringInvocationUnit#getMethodReturnValue(Method)} with {@code method}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link StoringInvocationUnit#getMethodReturnValue(Method)}
   */
  @Test
  @DisplayName(
      "Test getMethodReturnValue(Method) with 'method'; given MethodOptimizationInfo (default constructor); then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Value StoringInvocationUnit.getMethodReturnValue(Method)"})
  void testGetMethodReturnValueWithMethod_givenMethodOptimizationInfo_thenReturnNull() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act and Assert
    assertNull(StoringInvocationUnit.getMethodReturnValue(method));
  }
}
