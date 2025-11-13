package proguard.optimize.evaluation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
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
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.evaluation.BasicInvocationUnit;
import proguard.evaluation.ExecutingInvocationUnit;
import proguard.evaluation.ParticularReferenceValueFactory;
import proguard.evaluation.SimplifiedInvocationUnit;
import proguard.evaluation.value.BasicValueFactory;
import proguard.evaluation.value.IdentifiedReferenceValue;
import proguard.evaluation.value.InstructionOffsetValue;
import proguard.evaluation.value.MultiTypedReferenceValue;
import proguard.evaluation.value.MultiTypedReferenceValueFactory;
import proguard.evaluation.value.ReferenceValue;
import proguard.evaluation.value.TopValue;
import proguard.evaluation.value.TracedReferenceValue;
import proguard.evaluation.value.Value;

class ParameterTracingInvocationUnitDiffblueTest {
  /**
   * Test {@link
   * ParameterTracingInvocationUnit#ParameterTracingInvocationUnit(SimplifiedInvocationUnit)}.
   *
   * <p>Method under test: {@link
   * ParameterTracingInvocationUnit#ParameterTracingInvocationUnit(SimplifiedInvocationUnit)}
   */
  @Test
  @DisplayName("Test new ParameterTracingInvocationUnit(SimplifiedInvocationUnit)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ParameterTracingInvocationUnit.<init>(SimplifiedInvocationUnit)"})
  void testNewParameterTracingInvocationUnit() {
    // Arrange
    BasicInvocationUnit invocationUnit =
        new BasicInvocationUnit(new ParticularReferenceValueFactory());

    // Act
    ParameterTracingInvocationUnit actualParameterTracingInvocationUnit =
        new ParameterTracingInvocationUnit(invocationUnit);

    // Assert
    assertTrue(invocationUnit.getExceptionValue(null, null) instanceof IdentifiedReferenceValue);
    Value exceptionValue = actualParameterTracingInvocationUnit.getExceptionValue(null, null);
    assertTrue(
        ((TracedReferenceValue) exceptionValue).getReferenceValue()
            instanceof IdentifiedReferenceValue);
    assertTrue(
        ((TracedReferenceValue) exceptionValue).getTraceValue() instanceof InstructionOffsetValue);
    assertTrue(exceptionValue instanceof TracedReferenceValue);
    assertEquals("Ljava/lang/Throwable;", ((TracedReferenceValue) exceptionValue).getType());
    assertNull(((TracedReferenceValue) exceptionValue).getReferencedClass());
    assertEquals(-1, ((TracedReferenceValue) exceptionValue).isNull());
    assertEquals(1, ((TracedReferenceValue) exceptionValue).isNotNull());
    assertFalse(exceptionValue.isCategory2());
    assertFalse(exceptionValue.isParticular());
    assertTrue(exceptionValue.isSpecific());
  }

  /**
   * Test {@link ParameterTracingInvocationUnit#setMethodParameterValue(Clazz, AnyMethodrefConstant,
   * int, Value)}.
   *
   * <p>Method under test: {@link ParameterTracingInvocationUnit#setMethodParameterValue(Clazz,
   * AnyMethodrefConstant, int, Value)}
   */
  @Test
  @DisplayName("Test setMethodParameterValue(Clazz, AnyMethodrefConstant, int, Value)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterTracingInvocationUnit.setMethodParameterValue(Clazz, AnyMethodrefConstant, int, Value)"
  })
  void testSetMethodParameterValue() {
    // Arrange
    ParameterTracingInvocationUnit parameterTracingInvocationUnit =
        new ParameterTracingInvocationUnit(
            new BasicInvocationUnit(new ParticularReferenceValueFactory()));
    LibraryClass clazz = new LibraryClass();
    InterfaceMethodrefConstant refConstant = new InterfaceMethodrefConstant();

    // Act
    parameterTracingInvocationUnit.setMethodParameterValue(clazz, refConstant, 1, new TopValue());

    // Assert
    Value exceptionValue = parameterTracingInvocationUnit.getExceptionValue(null, null);
    assertTrue(
        ((TracedReferenceValue) exceptionValue).getReferenceValue()
            instanceof IdentifiedReferenceValue);
    assertTrue(exceptionValue instanceof TracedReferenceValue);
  }

  /**
   * Test {@link ParameterTracingInvocationUnit#setMethodParameterValue(Clazz, AnyMethodrefConstant,
   * int, Value)}.
   *
   * <ul>
   *   <li>Then calls {@link ExecutingInvocationUnit#setMethodParameterValue(Clazz,
   *       AnyMethodrefConstant, int, Value)}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterTracingInvocationUnit#setMethodParameterValue(Clazz,
   * AnyMethodrefConstant, int, Value)}
   */
  @Test
  @DisplayName(
      "Test setMethodParameterValue(Clazz, AnyMethodrefConstant, int, Value); then calls setMethodParameterValue(Clazz, AnyMethodrefConstant, int, Value)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterTracingInvocationUnit.setMethodParameterValue(Clazz, AnyMethodrefConstant, int, Value)"
  })
  void testSetMethodParameterValue_thenCallsSetMethodParameterValue() {
    // Arrange
    ExecutingInvocationUnit invocationUnit = mock(ExecutingInvocationUnit.class);
    doNothing()
        .when(invocationUnit)
        .setMethodParameterValue(
            Mockito.<Clazz>any(),
            Mockito.<AnyMethodrefConstant>any(),
            anyInt(),
            Mockito.<Value>any());
    ParameterTracingInvocationUnit parameterTracingInvocationUnit =
        new ParameterTracingInvocationUnit(invocationUnit);
    LibraryClass clazz = new LibraryClass();
    InterfaceMethodrefConstant refConstant = new InterfaceMethodrefConstant();

    // Act
    parameterTracingInvocationUnit.setMethodParameterValue(clazz, refConstant, 1, new TopValue());

    // Assert
    verify(invocationUnit)
        .setMethodParameterValue(
            isA(Clazz.class), isA(AnyMethodrefConstant.class), eq(1), isA(Value.class));
  }

  /**
   * Test {@link ParameterTracingInvocationUnit#getMethodReturnValue(Clazz, AnyMethodrefConstant,
   * String)} with {@code clazz}, {@code refConstant}, {@code type}.
   *
   * <p>Method under test: {@link ParameterTracingInvocationUnit#getMethodReturnValue(Clazz,
   * AnyMethodrefConstant, String)}
   */
  @Test
  @DisplayName(
      "Test getMethodReturnValue(Clazz, AnyMethodrefConstant, String) with 'clazz', 'refConstant', 'type'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value ParameterTracingInvocationUnit.getMethodReturnValue(Clazz, AnyMethodrefConstant, String)"
  })
  void testGetMethodReturnValueWithClazzRefConstantType() {
    // Arrange
    ParameterTracingInvocationUnit parameterTracingInvocationUnit =
        new ParameterTracingInvocationUnit(
            new BasicInvocationUnit(new ParticularReferenceValueFactory()));
    LibraryClass clazz = new LibraryClass();

    // Act
    Value actualMethodReturnValue =
        parameterTracingInvocationUnit.getMethodReturnValue(
            clazz, new InterfaceMethodrefConstant(), "Type");

    // Assert
    ReferenceValue referenceValue =
        ((TracedReferenceValue) actualMethodReturnValue).getReferenceValue();
    assertTrue(referenceValue instanceof IdentifiedReferenceValue);
    assertTrue(actualMethodReturnValue instanceof TracedReferenceValue);
    assertEquals("Type", referenceValue.getType());
    assertNull(referenceValue.getReferencedClass());
    assertEquals(0, referenceValue.isNull());
    assertFalse(referenceValue.isParticular());
    assertTrue(referenceValue.mayBeExtension());
    assertTrue(referenceValue.isSpecific());
    assertTrue(actualMethodReturnValue.isSpecific());
  }

  /**
   * Test {@link ParameterTracingInvocationUnit#getMethodReturnValue(Clazz, AnyMethodrefConstant,
   * String)} with {@code clazz}, {@code refConstant}, {@code type}.
   *
   * <p>Method under test: {@link ParameterTracingInvocationUnit#getMethodReturnValue(Clazz,
   * AnyMethodrefConstant, String)}
   */
  @Test
  @DisplayName(
      "Test getMethodReturnValue(Clazz, AnyMethodrefConstant, String) with 'clazz', 'refConstant', 'type'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value ParameterTracingInvocationUnit.getMethodReturnValue(Clazz, AnyMethodrefConstant, String)"
  })
  void testGetMethodReturnValueWithClazzRefConstantType2() {
    // Arrange
    ParameterTracingInvocationUnit parameterTracingInvocationUnit =
        new ParameterTracingInvocationUnit(
            new BasicInvocationUnit(new MultiTypedReferenceValueFactory()));
    LibraryClass clazz = new LibraryClass();

    // Act
    Value actualMethodReturnValue =
        parameterTracingInvocationUnit.getMethodReturnValue(
            clazz, new InterfaceMethodrefConstant(), "Type");

    // Assert
    ReferenceValue referenceValue =
        ((TracedReferenceValue) actualMethodReturnValue).getReferenceValue();
    assertTrue(referenceValue instanceof MultiTypedReferenceValue);
    assertTrue(actualMethodReturnValue instanceof TracedReferenceValue);
    assertEquals("Type", referenceValue.getType());
    assertNull(referenceValue.getReferencedClass());
    assertEquals(1, ((MultiTypedReferenceValue) referenceValue).getPotentialTypes().size());
    assertFalse(referenceValue.isParticular());
    assertFalse(((MultiTypedReferenceValue) referenceValue).mayBeUnknown);
  }

  /**
   * Test {@link ParameterTracingInvocationUnit#getMethodReturnValue(Clazz, AnyMethodrefConstant,
   * String)} with {@code clazz}, {@code refConstant}, {@code type}.
   *
   * <p>Method under test: {@link ParameterTracingInvocationUnit#getMethodReturnValue(Clazz,
   * AnyMethodrefConstant, String)}
   */
  @Test
  @DisplayName(
      "Test getMethodReturnValue(Clazz, AnyMethodrefConstant, String) with 'clazz', 'refConstant', 'type'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value ParameterTracingInvocationUnit.getMethodReturnValue(Clazz, AnyMethodrefConstant, String)"
  })
  void testGetMethodReturnValueWithClazzRefConstantType3() {
    // Arrange
    MultiTypedReferenceValueFactory valueFactory =
        new MultiTypedReferenceValueFactory(
            true, KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool);
    ParameterTracingInvocationUnit parameterTracingInvocationUnit =
        new ParameterTracingInvocationUnit(new BasicInvocationUnit(valueFactory));
    LibraryClass clazz = new LibraryClass();

    // Act
    Value actualMethodReturnValue =
        parameterTracingInvocationUnit.getMethodReturnValue(
            clazz, new InterfaceMethodrefConstant(), "Type");

    // Assert
    ReferenceValue referenceValue =
        ((TracedReferenceValue) actualMethodReturnValue).getReferenceValue();
    assertTrue(referenceValue instanceof MultiTypedReferenceValue);
    assertTrue(actualMethodReturnValue instanceof TracedReferenceValue);
    assertEquals("Type", referenceValue.getType());
    assertNull(referenceValue.getReferencedClass());
    assertEquals(1, ((MultiTypedReferenceValue) referenceValue).getPotentialTypes().size());
    assertFalse(referenceValue.isParticular());
    assertFalse(((MultiTypedReferenceValue) referenceValue).mayBeUnknown);
  }

  /**
   * Test {@link ParameterTracingInvocationUnit#getMethodReturnValue(Clazz, AnyMethodrefConstant,
   * String)} with {@code clazz}, {@code refConstant}, {@code type}.
   *
   * <ul>
   *   <li>Then return {@link TopValue} (default constructor).
   * </ul>
   *
   * <p>Method under test: {@link ParameterTracingInvocationUnit#getMethodReturnValue(Clazz,
   * AnyMethodrefConstant, String)}
   */
  @Test
  @DisplayName(
      "Test getMethodReturnValue(Clazz, AnyMethodrefConstant, String) with 'clazz', 'refConstant', 'type'; then return TopValue (default constructor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value ParameterTracingInvocationUnit.getMethodReturnValue(Clazz, AnyMethodrefConstant, String)"
  })
  void testGetMethodReturnValueWithClazzRefConstantType_thenReturnTopValue() {
    // Arrange
    ExecutingInvocationUnit invocationUnit = mock(ExecutingInvocationUnit.class);
    TopValue topValue = new TopValue();
    when(invocationUnit.getMethodReturnValue(
            Mockito.<Clazz>any(), Mockito.<AnyMethodrefConstant>any(), Mockito.<String>any()))
        .thenReturn(topValue);
    ParameterTracingInvocationUnit parameterTracingInvocationUnit =
        new ParameterTracingInvocationUnit(invocationUnit);
    LibraryClass clazz = new LibraryClass();

    // Act
    Value actualMethodReturnValue =
        parameterTracingInvocationUnit.getMethodReturnValue(
            clazz, new InterfaceMethodrefConstant(), "Type");

    // Assert
    verify(invocationUnit)
        .getMethodReturnValue(isA(Clazz.class), isA(AnyMethodrefConstant.class), eq("Type"));
    assertSame(topValue, actualMethodReturnValue);
  }

  /**
   * Test {@link ParameterTracingInvocationUnit#getMethodReturnValue(Clazz, AnyMethodrefConstant,
   * String)} with {@code clazz}, {@code refConstant}, {@code type}.
   *
   * <ul>
   *   <li>Then return Type is {@code Ljava/lang/Object;}.
   * </ul>
   *
   * <p>Method under test: {@link ParameterTracingInvocationUnit#getMethodReturnValue(Clazz,
   * AnyMethodrefConstant, String)}
   */
  @Test
  @DisplayName(
      "Test getMethodReturnValue(Clazz, AnyMethodrefConstant, String) with 'clazz', 'refConstant', 'type'; then return Type is 'Ljava/lang/Object;'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "Value ParameterTracingInvocationUnit.getMethodReturnValue(Clazz, AnyMethodrefConstant, String)"
  })
  void testGetMethodReturnValueWithClazzRefConstantType_thenReturnTypeIsLjavaLangObject() {
    // Arrange
    ParameterTracingInvocationUnit parameterTracingInvocationUnit =
        new ParameterTracingInvocationUnit(new BasicInvocationUnit(new BasicValueFactory()));
    LibraryClass clazz = new LibraryClass();

    // Act
    Value actualMethodReturnValue =
        parameterTracingInvocationUnit.getMethodReturnValue(
            clazz, new InterfaceMethodrefConstant(), "Type");

    // Assert
    assertTrue(actualMethodReturnValue instanceof TracedReferenceValue);
    assertEquals("Ljava/lang/Object;", ((TracedReferenceValue) actualMethodReturnValue).getType());
    assertSame(
        BasicValueFactory.REFERENCE_VALUE,
        ((TracedReferenceValue) actualMethodReturnValue).getReferenceValue());
  }
}
