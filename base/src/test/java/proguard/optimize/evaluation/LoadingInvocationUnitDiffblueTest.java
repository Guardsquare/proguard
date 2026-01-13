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
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryField;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.evaluation.ParticularReferenceValueFactory;
import proguard.evaluation.value.BasicValueFactory;
import proguard.evaluation.value.IdentifiedReferenceValue;
import proguard.evaluation.value.MultiTypedReferenceValue;
import proguard.evaluation.value.MultiTypedReferenceValueFactory;
import proguard.evaluation.value.PrimitiveTypedReferenceValueFactory;
import proguard.evaluation.value.TypedReferenceValue;
import proguard.evaluation.value.UnknownReferenceValue;
import proguard.evaluation.value.Value;
import proguard.evaluation.value.ValueFactory;
import proguard.evaluation.value.object.AnalyzedObject;
import proguard.optimize.info.FieldOptimizationInfo;
import proguard.optimize.info.MethodOptimizationInfo;
import proguard.optimize.info.ProgramFieldOptimizationInfo;

class LoadingInvocationUnitDiffblueTest {
  /**
   * Test {@link LoadingInvocationUnit#LoadingInvocationUnit(ValueFactory)}.
   *
   * <p>Method under test: {@link LoadingInvocationUnit#LoadingInvocationUnit(ValueFactory)}
   */
  @Test
  @DisplayName("Test new LoadingInvocationUnit(ValueFactory)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LoadingInvocationUnit.<init>(ValueFactory)"})
  void testNewLoadingInvocationUnit() {
    // Arrange, Act and Assert
    Value exceptionValue =
        new LoadingInvocationUnit(new ParticularReferenceValueFactory())
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
   * Test {@link LoadingInvocationUnit#LoadingInvocationUnit(ValueFactory, boolean, boolean,
   * boolean)}.
   *
   * <p>Method under test: {@link LoadingInvocationUnit#LoadingInvocationUnit(ValueFactory, boolean,
   * boolean, boolean)}
   */
  @Test
  @DisplayName("Test new LoadingInvocationUnit(ValueFactory, boolean, boolean, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void LoadingInvocationUnit.<init>(ValueFactory, boolean, boolean, boolean)"})
  void testNewLoadingInvocationUnit2() {
    // Arrange and Act
    LoadingInvocationUnit actualLoadingInvocationUnit =
        new LoadingInvocationUnit(new ParticularReferenceValueFactory(), true, true, true);

    // Assert
    Value exceptionValue = actualLoadingInvocationUnit.getExceptionValue(null, null);
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
   * Test {@link LoadingInvocationUnit#getFieldClassValue(Clazz, FieldrefConstant, String)}.
   *
   * <ul>
   *   <li>Given {@link FieldOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getFieldClassValue(Clazz, FieldrefConstant,
   * String)}
   */
  @Test
  @DisplayName(
      "Test getFieldClassValue(Clazz, FieldrefConstant, String); given FieldOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value LoadingInvocationUnit.getFieldClassValue(Clazz, FieldrefConstant, String)"
  })
  void testGetFieldClassValue_givenFieldOptimizationInfo() {
    // Arrange
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();

    LibraryField referencedField = new LibraryField();
    referencedField.setProcessingInfo(new FieldOptimizationInfo());

    // Act
    Value actualFieldClassValue =
        loadingInvocationUnit.getFieldClassValue(
            clazz, new FieldrefConstant(1, 1, new LibraryClass(), referencedField), "Type");

    // Assert
    assertTrue(
        loadingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
    assertTrue(actualFieldClassValue instanceof IdentifiedReferenceValue);
    assertEquals("Type", ((IdentifiedReferenceValue) actualFieldClassValue).getType());
    assertNull(((IdentifiedReferenceValue) actualFieldClassValue).getReferencedClass());
    assertEquals(0, ((IdentifiedReferenceValue) actualFieldClassValue).isNull());
    assertFalse(actualFieldClassValue.isParticular());
    assertTrue(((IdentifiedReferenceValue) actualFieldClassValue).mayBeExtension());
    assertTrue(actualFieldClassValue.isSpecific());
  }

  /**
   * Test {@link LoadingInvocationUnit#getFieldClassValue(Clazz, FieldrefConstant, String)}.
   *
   * <ul>
   *   <li>Then return GeneralizedType Type is {@code Type}.
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getFieldClassValue(Clazz, FieldrefConstant,
   * String)}
   */
  @Test
  @DisplayName(
      "Test getFieldClassValue(Clazz, FieldrefConstant, String); then return GeneralizedType Type is 'Type'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value LoadingInvocationUnit.getFieldClassValue(Clazz, FieldrefConstant, String)"
  })
  void testGetFieldClassValue_thenReturnGeneralizedTypeTypeIsType() {
    // Arrange
    MultiTypedReferenceValueFactory valueFactory =
        new MultiTypedReferenceValueFactory(
            true, KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool);
    LoadingInvocationUnit loadingInvocationUnit = new LoadingInvocationUnit(valueFactory);
    LibraryClass clazz = new LibraryClass();

    // Act
    Value actualFieldClassValue =
        loadingInvocationUnit.getFieldClassValue(clazz, new FieldrefConstant(), "Type");

    // Assert
    assertTrue(actualFieldClassValue instanceof MultiTypedReferenceValue);
    assertEquals("Type", ((MultiTypedReferenceValue) actualFieldClassValue).getType());
    assertEquals(
        "Type", ((MultiTypedReferenceValue) actualFieldClassValue).getGeneralizedType().getType());
    assertEquals(1, ((MultiTypedReferenceValue) actualFieldClassValue).getPotentialTypes().size());
  }

  /**
   * Test {@link LoadingInvocationUnit#getFieldClassValue(Clazz, FieldrefConstant, String)}.
   *
   * <ul>
   *   <li>Then return {@link IdentifiedReferenceValue}.
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getFieldClassValue(Clazz, FieldrefConstant,
   * String)}
   */
  @Test
  @DisplayName(
      "Test getFieldClassValue(Clazz, FieldrefConstant, String); then return IdentifiedReferenceValue")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value LoadingInvocationUnit.getFieldClassValue(Clazz, FieldrefConstant, String)"
  })
  void testGetFieldClassValue_thenReturnIdentifiedReferenceValue() {
    // Arrange
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();

    // Act
    Value actualFieldClassValue =
        loadingInvocationUnit.getFieldClassValue(clazz, new FieldrefConstant(), "Type");

    // Assert
    assertTrue(
        loadingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
    assertTrue(actualFieldClassValue instanceof IdentifiedReferenceValue);
    assertEquals("Type", ((IdentifiedReferenceValue) actualFieldClassValue).getType());
    assertNull(((IdentifiedReferenceValue) actualFieldClassValue).getReferencedClass());
    assertEquals(0, ((IdentifiedReferenceValue) actualFieldClassValue).isNull());
    assertFalse(actualFieldClassValue.isParticular());
    assertTrue(((IdentifiedReferenceValue) actualFieldClassValue).mayBeExtension());
    assertTrue(actualFieldClassValue.isSpecific());
  }

  /**
   * Test {@link LoadingInvocationUnit#getFieldClassValue(Clazz, FieldrefConstant, String)}.
   *
   * <ul>
   *   <li>Then return {@link BasicValueFactory#REFERENCE_VALUE}.
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getFieldClassValue(Clazz, FieldrefConstant,
   * String)}
   */
  @Test
  @DisplayName(
      "Test getFieldClassValue(Clazz, FieldrefConstant, String); then return REFERENCE_VALUE")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value LoadingInvocationUnit.getFieldClassValue(Clazz, FieldrefConstant, String)"
  })
  void testGetFieldClassValue_thenReturnReference_value() {
    // Arrange
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(new PrimitiveTypedReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();

    // Act and Assert
    assertSame(
        BasicValueFactory.REFERENCE_VALUE,
        loadingInvocationUnit.getFieldClassValue(clazz, new FieldrefConstant(), "Type"));
  }

  /**
   * Test {@link LoadingInvocationUnit#getFieldClassValue(Clazz, FieldrefConstant, String)}.
   *
   * <ul>
   *   <li>Then return {@link UnknownReferenceValue} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getFieldClassValue(Clazz, FieldrefConstant,
   * String)}
   */
  @Test
  @DisplayName(
      "Test getFieldClassValue(Clazz, FieldrefConstant, String); then return UnknownReferenceValue (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value LoadingInvocationUnit.getFieldClassValue(Clazz, FieldrefConstant, String)"
  })
  void testGetFieldClassValue_thenReturnUnknownReferenceValue() {
    // Arrange
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(new ParticularReferenceValueFactory());
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

    // Act
    Value actualFieldClassValue =
        loadingInvocationUnit.getFieldClassValue(
            clazz, new FieldrefConstant(1, 1, new LibraryClass(), referencedField), "Type");

    // Assert
    assertTrue(
        loadingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
    assertSame(referencedClass, actualFieldClassValue);
  }

  /**
   * Test {@link LoadingInvocationUnit#getFieldValue(Clazz, FieldrefConstant, String)}.
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getFieldValue(Clazz, FieldrefConstant,
   * String)}
   */
  @Test
  @DisplayName("Test getFieldValue(Clazz, FieldrefConstant, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Value LoadingInvocationUnit.getFieldValue(Clazz, FieldrefConstant, String)"})
  void testGetFieldValue() {
    // Arrange
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();

    // Act
    Value actualFieldValue =
        loadingInvocationUnit.getFieldValue(clazz, new FieldrefConstant(), "Type");

    // Assert
    assertTrue(
        loadingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
    assertTrue(actualFieldValue instanceof IdentifiedReferenceValue);
    assertEquals("Type", ((IdentifiedReferenceValue) actualFieldValue).getType());
    assertNull(((IdentifiedReferenceValue) actualFieldValue).getReferencedClass());
    assertEquals(0, ((IdentifiedReferenceValue) actualFieldValue).isNull());
    assertFalse(actualFieldValue.isParticular());
    assertTrue(((IdentifiedReferenceValue) actualFieldValue).mayBeExtension());
    assertTrue(actualFieldValue.isSpecific());
  }

  /**
   * Test {@link LoadingInvocationUnit#getFieldValue(Clazz, FieldrefConstant, String)}.
   *
   * <ul>
   *   <li>Given {@link FieldOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getFieldValue(Clazz, FieldrefConstant,
   * String)}
   */
  @Test
  @DisplayName(
      "Test getFieldValue(Clazz, FieldrefConstant, String); given FieldOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Value LoadingInvocationUnit.getFieldValue(Clazz, FieldrefConstant, String)"})
  void testGetFieldValue_givenFieldOptimizationInfo() {
    // Arrange
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();

    LibraryField referencedField = new LibraryField();
    referencedField.setProcessingInfo(new FieldOptimizationInfo());

    // Act
    Value actualFieldValue =
        loadingInvocationUnit.getFieldValue(
            clazz, new FieldrefConstant(1, 1, new LibraryClass(), referencedField), "Type");

    // Assert
    assertTrue(
        loadingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
    assertTrue(actualFieldValue instanceof IdentifiedReferenceValue);
    assertEquals("Type", ((IdentifiedReferenceValue) actualFieldValue).getType());
    assertNull(((IdentifiedReferenceValue) actualFieldValue).getReferencedClass());
    assertEquals(0, ((IdentifiedReferenceValue) actualFieldValue).isNull());
    assertFalse(actualFieldValue.isParticular());
    assertTrue(((IdentifiedReferenceValue) actualFieldValue).mayBeExtension());
    assertTrue(actualFieldValue.isSpecific());
  }

  /**
   * Test {@link LoadingInvocationUnit#getFieldValue(Clazz, FieldrefConstant, String)}.
   *
   * <ul>
   *   <li>Then return GeneralizedType Type is {@code Type}.
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getFieldValue(Clazz, FieldrefConstant,
   * String)}
   */
  @Test
  @DisplayName(
      "Test getFieldValue(Clazz, FieldrefConstant, String); then return GeneralizedType Type is 'Type'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Value LoadingInvocationUnit.getFieldValue(Clazz, FieldrefConstant, String)"})
  void testGetFieldValue_thenReturnGeneralizedTypeTypeIsType() {
    // Arrange
    MultiTypedReferenceValueFactory valueFactory =
        new MultiTypedReferenceValueFactory(
            true, KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool);
    LoadingInvocationUnit loadingInvocationUnit = new LoadingInvocationUnit(valueFactory);
    LibraryClass clazz = new LibraryClass();

    // Act
    Value actualFieldValue =
        loadingInvocationUnit.getFieldValue(clazz, new FieldrefConstant(), "Type");

    // Assert
    assertTrue(actualFieldValue instanceof MultiTypedReferenceValue);
    assertEquals("Type", ((MultiTypedReferenceValue) actualFieldValue).getType());
    assertEquals(
        "Type", ((MultiTypedReferenceValue) actualFieldValue).getGeneralizedType().getType());
    assertEquals(1, ((MultiTypedReferenceValue) actualFieldValue).getPotentialTypes().size());
  }

  /**
   * Test {@link LoadingInvocationUnit#getFieldValue(Clazz, FieldrefConstant, String)}.
   *
   * <ul>
   *   <li>Then return {@link BasicValueFactory#REFERENCE_VALUE}.
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getFieldValue(Clazz, FieldrefConstant,
   * String)}
   */
  @Test
  @DisplayName("Test getFieldValue(Clazz, FieldrefConstant, String); then return REFERENCE_VALUE")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Value LoadingInvocationUnit.getFieldValue(Clazz, FieldrefConstant, String)"})
  void testGetFieldValue_thenReturnReference_value() {
    // Arrange
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(new PrimitiveTypedReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();

    // Act and Assert
    assertSame(
        BasicValueFactory.REFERENCE_VALUE,
        loadingInvocationUnit.getFieldValue(clazz, new FieldrefConstant(), "Type"));
  }

  /**
   * Test {@link LoadingInvocationUnit#getMethodParameterValue(Clazz, Method, int, String, Clazz)}.
   *
   * <ul>
   *   <li>Then return not Particular.
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getMethodParameterValue(Clazz, Method, int,
   * String, Clazz)}
   */
  @Test
  @DisplayName(
      "Test getMethodParameterValue(Clazz, Method, int, String, Clazz); then return not Particular")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value LoadingInvocationUnit.getMethodParameterValue(Clazz, Method, int, String, Clazz)"
  })
  void testGetMethodParameterValue_thenReturnNotParticular() {
    // Arrange
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(new ParticularReferenceValueFactory(), true, false, true);
    LibraryClass clazz = new LibraryClass();

    // Act
    Value actualMethodParameterValue =
        loadingInvocationUnit.getMethodParameterValue(clazz, new LibraryMethod(), 1, "Type", null);

    // Assert
    assertTrue(
        loadingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
    assertTrue(actualMethodParameterValue instanceof IdentifiedReferenceValue);
    assertEquals("Type", ((IdentifiedReferenceValue) actualMethodParameterValue).getType());
    assertNull(((IdentifiedReferenceValue) actualMethodParameterValue).getReferencedClass());
    assertFalse(actualMethodParameterValue.isParticular());
    assertTrue(((IdentifiedReferenceValue) actualMethodParameterValue).mayBeExtension());
    assertTrue(actualMethodParameterValue.isSpecific());
  }

  /**
   * Test {@link LoadingInvocationUnit#getMethodParameterValue(Clazz, Method, int, String, Clazz)}.
   *
   * <ul>
   *   <li>Then return Null is zero.
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getMethodParameterValue(Clazz, Method, int,
   * String, Clazz)}
   */
  @Test
  @DisplayName(
      "Test getMethodParameterValue(Clazz, Method, int, String, Clazz); then return Null is zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value LoadingInvocationUnit.getMethodParameterValue(Clazz, Method, int, String, Clazz)"
  })
  void testGetMethodParameterValue_thenReturnNullIsZero() {
    // Arrange
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(new ParticularReferenceValueFactory(), true, false, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    LibraryClass referencedClass = new LibraryClass();

    // Act
    Value actualMethodParameterValue =
        loadingInvocationUnit.getMethodParameterValue(clazz, method, 1, "Type", referencedClass);

    // Assert
    Clazz referencedClass2 =
        ((IdentifiedReferenceValue) actualMethodParameterValue).getReferencedClass();
    assertTrue(referencedClass2 instanceof LibraryClass);
    assertTrue(actualMethodParameterValue instanceof IdentifiedReferenceValue);
    assertEquals(0, ((IdentifiedReferenceValue) actualMethodParameterValue).isNull());
    assertSame(referencedClass, referencedClass2);
  }

  /**
   * Test {@link LoadingInvocationUnit#getMethodParameterValue(Clazz, Method, int, String, Clazz)}.
   *
   * <ul>
   *   <li>Then return PotentialTypes size is one.
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getMethodParameterValue(Clazz, Method, int,
   * String, Clazz)}
   */
  @Test
  @DisplayName(
      "Test getMethodParameterValue(Clazz, Method, int, String, Clazz); then return PotentialTypes size is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value LoadingInvocationUnit.getMethodParameterValue(Clazz, Method, int, String, Clazz)"
  })
  void testGetMethodParameterValue_thenReturnPotentialTypesSizeIsOne() {
    // Arrange
    MultiTypedReferenceValueFactory valueFactory =
        new MultiTypedReferenceValueFactory(
            true, KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool);
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(valueFactory, true, false, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    LibraryClass referencedClass = new LibraryClass();

    // Act
    Value actualMethodParameterValue =
        loadingInvocationUnit.getMethodParameterValue(clazz, method, 1, "Type", referencedClass);

    // Assert
    Clazz referencedClass2 =
        ((MultiTypedReferenceValue) actualMethodParameterValue).getReferencedClass();
    assertTrue(referencedClass2 instanceof LibraryClass);
    assertTrue(actualMethodParameterValue instanceof MultiTypedReferenceValue);
    assertEquals("Type", ((MultiTypedReferenceValue) actualMethodParameterValue).getType());
    assertEquals(
        1, ((MultiTypedReferenceValue) actualMethodParameterValue).getPotentialTypes().size());
    assertSame(referencedClass, referencedClass2);
  }

  /**
   * Test {@link LoadingInvocationUnit#getMethodParameterValue(Clazz, Method, int, String, Clazz)}.
   *
   * <ul>
   *   <li>Then return {@link BasicValueFactory#REFERENCE_VALUE}.
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getMethodParameterValue(Clazz, Method, int,
   * String, Clazz)}
   */
  @Test
  @DisplayName(
      "Test getMethodParameterValue(Clazz, Method, int, String, Clazz); then return REFERENCE_VALUE")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value LoadingInvocationUnit.getMethodParameterValue(Clazz, Method, int, String, Clazz)"
  })
  void testGetMethodParameterValue_thenReturnReference_value() {
    // Arrange
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(new PrimitiveTypedReferenceValueFactory(), true, false, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act and Assert
    assertSame(
        BasicValueFactory.REFERENCE_VALUE,
        loadingInvocationUnit.getMethodParameterValue(
            clazz, method, 1, "Type", new LibraryClass()));
  }

  /**
   * Test {@link LoadingInvocationUnit#getMethodParameterValue(Clazz, Method, int, String, Clazz)}.
   *
   * <ul>
   *   <li>When zero.
   *   <li>Then return Null is minus one.
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getMethodParameterValue(Clazz, Method, int,
   * String, Clazz)}
   */
  @Test
  @DisplayName(
      "Test getMethodParameterValue(Clazz, Method, int, String, Clazz); when zero; then return Null is minus one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value LoadingInvocationUnit.getMethodParameterValue(Clazz, Method, int, String, Clazz)"
  })
  void testGetMethodParameterValue_whenZero_thenReturnNullIsMinusOne() {
    // Arrange
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(new ParticularReferenceValueFactory(), true, false, true);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act
    Value actualMethodParameterValue =
        loadingInvocationUnit.getMethodParameterValue(clazz, method, 0, "Type", new LibraryClass());

    // Assert
    assertTrue(
        ((IdentifiedReferenceValue) actualMethodParameterValue).getReferencedClass()
            instanceof LibraryClass);
    assertTrue(
        loadingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
    assertTrue(actualMethodParameterValue instanceof IdentifiedReferenceValue);
    assertEquals(-1, ((IdentifiedReferenceValue) actualMethodParameterValue).isNull());
    assertEquals(1, ((IdentifiedReferenceValue) actualMethodParameterValue).isNotNull());
  }

  /**
   * Test {@link LoadingInvocationUnit#getMethodReturnValue(Clazz, AnyMethodrefConstant, String)}
   * with {@code clazz}, {@code refConstant}, {@code type}.
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getMethodReturnValue(Clazz,
   * AnyMethodrefConstant, String)}
   */
  @Test
  @DisplayName(
      "Test getMethodReturnValue(Clazz, AnyMethodrefConstant, String) with 'clazz', 'refConstant', 'type'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value LoadingInvocationUnit.getMethodReturnValue(Clazz, AnyMethodrefConstant, String)"
  })
  void testGetMethodReturnValueWithClazzRefConstantType() {
    // Arrange
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();

    // Act
    Value actualMethodReturnValue =
        loadingInvocationUnit.getMethodReturnValue(clazz, new InterfaceMethodrefConstant(), "Type");

    // Assert
    assertTrue(
        loadingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
    assertTrue(actualMethodReturnValue instanceof IdentifiedReferenceValue);
    assertEquals("Type", ((IdentifiedReferenceValue) actualMethodReturnValue).getType());
    assertNull(((IdentifiedReferenceValue) actualMethodReturnValue).getReferencedClass());
    assertEquals(0, ((IdentifiedReferenceValue) actualMethodReturnValue).isNull());
    assertFalse(actualMethodReturnValue.isParticular());
    assertTrue(((IdentifiedReferenceValue) actualMethodReturnValue).mayBeExtension());
    assertTrue(actualMethodReturnValue.isSpecific());
  }

  /**
   * Test {@link LoadingInvocationUnit#getMethodReturnValue(Clazz, AnyMethodrefConstant, String)}
   * with {@code clazz}, {@code refConstant}, {@code type}.
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getMethodReturnValue(Clazz,
   * AnyMethodrefConstant, String)}
   */
  @Test
  @DisplayName(
      "Test getMethodReturnValue(Clazz, AnyMethodrefConstant, String) with 'clazz', 'refConstant', 'type'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value LoadingInvocationUnit.getMethodReturnValue(Clazz, AnyMethodrefConstant, String)"
  })
  void testGetMethodReturnValueWithClazzRefConstantType2() {
    // Arrange
    MultiTypedReferenceValueFactory valueFactory =
        new MultiTypedReferenceValueFactory(
            true, KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool);
    LoadingInvocationUnit loadingInvocationUnit = new LoadingInvocationUnit(valueFactory);
    LibraryClass clazz = new LibraryClass();

    // Act
    Value actualMethodReturnValue =
        loadingInvocationUnit.getMethodReturnValue(clazz, new InterfaceMethodrefConstant(), "Type");

    // Assert
    assertTrue(actualMethodReturnValue instanceof MultiTypedReferenceValue);
    assertEquals("Type", ((MultiTypedReferenceValue) actualMethodReturnValue).getType());
    TypedReferenceValue generalizedType =
        ((MultiTypedReferenceValue) actualMethodReturnValue).getGeneralizedType();
    assertEquals("Type", generalizedType.getType());
    assertNull(((MultiTypedReferenceValue) actualMethodReturnValue).getReferencedClass());
    assertNull(generalizedType.getReferencedClass());
    assertEquals(0, generalizedType.isNotNull());
    assertEquals(0, generalizedType.isNull());
    assertEquals(
        1, ((MultiTypedReferenceValue) actualMethodReturnValue).getPotentialTypes().size());
    assertFalse(generalizedType.isCategory2());
    assertFalse(generalizedType.isParticular());
    assertFalse(actualMethodReturnValue.isParticular());
    assertFalse(generalizedType.isSpecific());
    assertFalse(((MultiTypedReferenceValue) actualMethodReturnValue).mayBeUnknown);
    assertTrue(generalizedType.mayBeExtension());
  }

  /**
   * Test {@link LoadingInvocationUnit#getMethodReturnValue(Clazz, AnyMethodrefConstant, String)}
   * with {@code clazz}, {@code refConstant}, {@code type}.
   *
   * <ul>
   *   <li>Given {@link MethodOptimizationInfo} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getMethodReturnValue(Clazz,
   * AnyMethodrefConstant, String)}
   */
  @Test
  @DisplayName(
      "Test getMethodReturnValue(Clazz, AnyMethodrefConstant, String) with 'clazz', 'refConstant', 'type'; given MethodOptimizationInfo (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value LoadingInvocationUnit.getMethodReturnValue(Clazz, AnyMethodrefConstant, String)"
  })
  void testGetMethodReturnValueWithClazzRefConstantType_givenMethodOptimizationInfo() {
    // Arrange
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(new ParticularReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();

    LibraryMethod referencedMethod = new LibraryMethod();
    referencedMethod.setProcessingInfo(new MethodOptimizationInfo());

    // Act
    Value actualMethodReturnValue =
        loadingInvocationUnit.getMethodReturnValue(
            clazz,
            new InterfaceMethodrefConstant(1, 1, new LibraryClass(), referencedMethod),
            "Type");

    // Assert
    assertTrue(
        loadingInvocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
    assertTrue(actualMethodReturnValue instanceof IdentifiedReferenceValue);
    assertEquals("Type", ((IdentifiedReferenceValue) actualMethodReturnValue).getType());
    assertNull(((IdentifiedReferenceValue) actualMethodReturnValue).getReferencedClass());
    assertEquals(0, ((IdentifiedReferenceValue) actualMethodReturnValue).isNull());
    assertFalse(actualMethodReturnValue.isParticular());
    assertTrue(((IdentifiedReferenceValue) actualMethodReturnValue).mayBeExtension());
    assertTrue(actualMethodReturnValue.isSpecific());
  }

  /**
   * Test {@link LoadingInvocationUnit#getMethodReturnValue(Clazz, AnyMethodrefConstant, String)}
   * with {@code clazz}, {@code refConstant}, {@code type}.
   *
   * <ul>
   *   <li>Then return {@link BasicValueFactory#REFERENCE_VALUE}.
   * </ul>
   *
   * <p>Method under test: {@link LoadingInvocationUnit#getMethodReturnValue(Clazz,
   * AnyMethodrefConstant, String)}
   */
  @Test
  @DisplayName(
      "Test getMethodReturnValue(Clazz, AnyMethodrefConstant, String) with 'clazz', 'refConstant', 'type'; then return REFERENCE_VALUE")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value LoadingInvocationUnit.getMethodReturnValue(Clazz, AnyMethodrefConstant, String)"
  })
  void testGetMethodReturnValueWithClazzRefConstantType_thenReturnReference_value() {
    // Arrange
    LoadingInvocationUnit loadingInvocationUnit =
        new LoadingInvocationUnit(new PrimitiveTypedReferenceValueFactory());
    LibraryClass clazz = new LibraryClass();

    // Act and Assert
    assertSame(
        BasicValueFactory.REFERENCE_VALUE,
        loadingInvocationUnit.getMethodReturnValue(
            clazz, new InterfaceMethodrefConstant(), "Type"));
  }
}
