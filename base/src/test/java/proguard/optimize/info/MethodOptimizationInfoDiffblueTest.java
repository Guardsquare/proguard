package proguard.optimize.info;

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
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.evaluation.value.TopValue;
import proguard.evaluation.value.Value;

class MethodOptimizationInfoDiffblueTest {
  /**
   * Test {@link MethodOptimizationInfo#setNoSideEffects()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#setNoSideEffects()}
   */
  @Test
  @DisplayName("Test setNoSideEffects()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodOptimizationInfo.setNoSideEffects()"})
  void testSetNoSideEffects() {
    // Arrange
    MethodOptimizationInfo methodOptimizationInfo = new MethodOptimizationInfo();

    // Act
    methodOptimizationInfo.setNoSideEffects();

    // Assert
    assertEquals(0L, methodOptimizationInfo.getEscapingParameters());
    assertEquals(0L, methodOptimizationInfo.getModifiedParameters());
    assertFalse(methodOptimizationInfo.hasSideEffects());
    assertTrue(methodOptimizationInfo.hasNoEscapingParameters());
    assertTrue(methodOptimizationInfo.hasNoExternalSideEffects());
    assertTrue(methodOptimizationInfo.hasNoSideEffects());
  }

  /**
   * Test {@link MethodOptimizationInfo#setNoExternalSideEffects()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#setNoExternalSideEffects()}
   */
  @Test
  @DisplayName("Test setNoExternalSideEffects()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodOptimizationInfo.setNoExternalSideEffects()"})
  void testSetNoExternalSideEffects() {
    // Arrange
    MethodOptimizationInfo methodOptimizationInfo = new MethodOptimizationInfo();

    // Act
    methodOptimizationInfo.setNoExternalSideEffects();

    // Assert
    assertEquals(0L, methodOptimizationInfo.getEscapingParameters());
    assertEquals(1L, methodOptimizationInfo.getModifiedParameters());
    assertTrue(methodOptimizationInfo.hasNoEscapingParameters());
    assertTrue(methodOptimizationInfo.hasNoExternalSideEffects());
  }

  /**
   * Test {@link MethodOptimizationInfo#hasSideEffects()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#hasSideEffects()}
   */
  @Test
  @DisplayName("Test hasSideEffects()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.hasSideEffects()"})
  void testHasSideEffects() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().hasSideEffects());
  }

  /**
   * Test {@link MethodOptimizationInfo#canBeMadePrivate()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#canBeMadePrivate()}
   */
  @Test
  @DisplayName("Test canBeMadePrivate()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.canBeMadePrivate()"})
  void testCanBeMadePrivate() {
    // Arrange, Act and Assert
    assertFalse(new MethodOptimizationInfo().canBeMadePrivate());
  }

  /**
   * Test {@link MethodOptimizationInfo#catchesExceptions()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#catchesExceptions()}
   */
  @Test
  @DisplayName("Test catchesExceptions()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.catchesExceptions()"})
  void testCatchesExceptions() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().catchesExceptions());
  }

  /**
   * Test {@link MethodOptimizationInfo#branchesBackward()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#branchesBackward()}
   */
  @Test
  @DisplayName("Test branchesBackward()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.branchesBackward()"})
  void testBranchesBackward() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().branchesBackward());
  }

  /**
   * Test {@link MethodOptimizationInfo#invokesSuperMethods()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#invokesSuperMethods()}
   */
  @Test
  @DisplayName("Test invokesSuperMethods()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.invokesSuperMethods()"})
  void testInvokesSuperMethods() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().invokesSuperMethods());
  }

  /**
   * Test {@link MethodOptimizationInfo#invokesDynamically()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#invokesDynamically()}
   */
  @Test
  @DisplayName("Test invokesDynamically()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.invokesDynamically()"})
  void testInvokesDynamically() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().invokesDynamically());
  }

  /**
   * Test {@link MethodOptimizationInfo#accessesPrivateCode()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#accessesPrivateCode()}
   */
  @Test
  @DisplayName("Test accessesPrivateCode()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.accessesPrivateCode()"})
  void testAccessesPrivateCode() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().accessesPrivateCode());
  }

  /**
   * Test {@link MethodOptimizationInfo#accessesPackageCode()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#accessesPackageCode()}
   */
  @Test
  @DisplayName("Test accessesPackageCode()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.accessesPackageCode()"})
  void testAccessesPackageCode() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().accessesPackageCode());
  }

  /**
   * Test {@link MethodOptimizationInfo#accessesProtectedCode()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#accessesProtectedCode()}
   */
  @Test
  @DisplayName("Test accessesProtectedCode()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.accessesProtectedCode()"})
  void testAccessesProtectedCode() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().accessesProtectedCode());
  }

  /**
   * Test {@link MethodOptimizationInfo#assignsFinalField()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#assignsFinalField()}
   */
  @Test
  @DisplayName("Test assignsFinalField()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.assignsFinalField()"})
  void testAssignsFinalField() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().assignsFinalField());
  }

  /**
   * Test {@link MethodOptimizationInfo#returnsWithNonEmptyStack()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#returnsWithNonEmptyStack()}
   */
  @Test
  @DisplayName("Test returnsWithNonEmptyStack()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.returnsWithNonEmptyStack()"})
  void testReturnsWithNonEmptyStack() {
    // Arrange, Act and Assert
    assertFalse(new MethodOptimizationInfo().returnsWithNonEmptyStack());
  }

  /**
   * Test {@link MethodOptimizationInfo#isParameterUsed(int)}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#isParameterUsed(int)}
   */
  @Test
  @DisplayName("Test isParameterUsed(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.isParameterUsed(int)"})
  void testIsParameterUsed() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().isParameterUsed(1));
  }

  /**
   * Test {@link MethodOptimizationInfo#hasParameterEscaped(int)}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#hasParameterEscaped(int)}
   */
  @Test
  @DisplayName("Test hasParameterEscaped(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.hasParameterEscaped(int)"})
  void testHasParameterEscaped() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().hasParameterEscaped(1));
  }

  /**
   * Test {@link MethodOptimizationInfo#isParameterEscaping(int)}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#isParameterEscaping(int)}
   */
  @Test
  @DisplayName("Test isParameterEscaping(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.isParameterEscaping(int)"})
  void testIsParameterEscaping() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().isParameterEscaping(1));
  }

  /**
   * Test {@link MethodOptimizationInfo#getEscapingParameters()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#getEscapingParameters()}
   */
  @Test
  @DisplayName("Test getEscapingParameters()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long MethodOptimizationInfo.getEscapingParameters()"})
  void testGetEscapingParameters() {
    // Arrange, Act and Assert
    assertEquals(-1L, new MethodOptimizationInfo().getEscapingParameters());
  }

  /**
   * Test {@link MethodOptimizationInfo#isParameterModified(int)}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#isParameterModified(int)}
   */
  @Test
  @DisplayName("Test isParameterModified(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.isParameterModified(int)"})
  void testIsParameterModified() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().isParameterModified(1));
  }

  /**
   * Test {@link MethodOptimizationInfo#getModifiedParameters()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#getModifiedParameters()}
   */
  @Test
  @DisplayName("Test getModifiedParameters()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"long MethodOptimizationInfo.getModifiedParameters()"})
  void testGetModifiedParameters() {
    // Arrange, Act and Assert
    assertEquals(-1L, new MethodOptimizationInfo().getModifiedParameters());
  }

  /**
   * Test {@link MethodOptimizationInfo#modifiesAnything()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#modifiesAnything()}
   */
  @Test
  @DisplayName("Test modifiesAnything()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.modifiesAnything()"})
  void testModifiesAnything() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().modifiesAnything());
  }

  /**
   * Test {@link MethodOptimizationInfo#getParameterValue(int)}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#getParameterValue(int)}
   */
  @Test
  @DisplayName("Test getParameterValue(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"Value MethodOptimizationInfo.getParameterValue(int)"})
  void testGetParameterValue() {
    // Arrange, Act and Assert
    assertNull(new MethodOptimizationInfo().getParameterValue(1));
  }

  /**
   * Test {@link MethodOptimizationInfo#returnsParameter(int)}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#returnsParameter(int)}
   */
  @Test
  @DisplayName("Test returnsParameter(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.returnsParameter(int)"})
  void testReturnsParameter() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().returnsParameter(1));
  }

  /**
   * Test {@link MethodOptimizationInfo#returnsNewInstances()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#returnsNewInstances()}
   */
  @Test
  @DisplayName("Test returnsNewInstances()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.returnsNewInstances()"})
  void testReturnsNewInstances() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().returnsNewInstances());
  }

  /**
   * Test {@link MethodOptimizationInfo#returnsExternalValues()}.
   *
   * <p>Method under test: {@link MethodOptimizationInfo#returnsExternalValues()}
   */
  @Test
  @DisplayName("Test returnsExternalValues()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodOptimizationInfo.returnsExternalValues()"})
  void testReturnsExternalValues() {
    // Arrange, Act and Assert
    assertTrue(new MethodOptimizationInfo().returnsExternalValues());
  }

  /**
   * Test {@link MethodOptimizationInfo#setMethodOptimizationInfo(Clazz, Method)}.
   *
   * <ul>
   *   <li>Given {@code Processing Info}.
   * </ul>
   *
   * <p>Method under test: {@link MethodOptimizationInfo#setMethodOptimizationInfo(Clazz, Method)}
   */
  @Test
  @DisplayName("Test setMethodOptimizationInfo(Clazz, Method); given 'Processing Info'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodOptimizationInfo.setMethodOptimizationInfo(Clazz, Method)"})
  void testSetMethodOptimizationInfo_givenProcessingInfo() {
    // Arrange
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo("Processing Info");

    // Act
    MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

    // Assert
    Object processingInfo = method.getProcessingInfo();
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
   * Test {@link MethodOptimizationInfo#setMethodOptimizationInfo(Clazz, Method)}.
   *
   * <ul>
   *   <li>When {@link LibraryMethod#LibraryMethod()}.
   * </ul>
   *
   * <p>Method under test: {@link MethodOptimizationInfo#setMethodOptimizationInfo(Clazz, Method)}
   */
  @Test
  @DisplayName("Test setMethodOptimizationInfo(Clazz, Method); when LibraryMethod()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void MethodOptimizationInfo.setMethodOptimizationInfo(Clazz, Method)"})
  void testSetMethodOptimizationInfo_whenLibraryMethod() {
    // Arrange
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act
    MethodOptimizationInfo.setMethodOptimizationInfo(clazz, method);

    // Assert
    Object processingInfo = method.getProcessingInfo();
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
   * Test {@link MethodOptimizationInfo#getMethodOptimizationInfo(Method)}.
   *
   * <ul>
   *   <li>Then return ReturnValue is {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link MethodOptimizationInfo#getMethodOptimizationInfo(Method)}
   */
  @Test
  @DisplayName("Test getMethodOptimizationInfo(Method); then return ReturnValue is 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "MethodOptimizationInfo MethodOptimizationInfo.getMethodOptimizationInfo(Method)"
  })
  void testGetMethodOptimizationInfo_thenReturnReturnValueIsNull() {
    // Arrange
    LibraryMethod method = new LibraryMethod();
    method.setProcessingInfo(new MethodOptimizationInfo());

    // Act
    MethodOptimizationInfo actualMethodOptimizationInfo =
        MethodOptimizationInfo.getMethodOptimizationInfo(method);

    // Assert
    assertNull(actualMethodOptimizationInfo.getReturnValue());
    assertEquals(-1L, actualMethodOptimizationInfo.getEscapedParameters());
    assertEquals(-1L, actualMethodOptimizationInfo.getEscapingParameters());
    assertEquals(-1L, actualMethodOptimizationInfo.getModifiedParameters());
    assertEquals(-1L, actualMethodOptimizationInfo.getReturnedParameters());
    assertEquals(-1L, actualMethodOptimizationInfo.getUsedParameters());
    assertEquals(0, actualMethodOptimizationInfo.getParameterSize());
    assertFalse(actualMethodOptimizationInfo.hasNoEscapingParameters());
    assertFalse(actualMethodOptimizationInfo.hasNoExternalReturnValues());
    assertFalse(actualMethodOptimizationInfo.hasNoExternalSideEffects());
    assertFalse(actualMethodOptimizationInfo.hasNoSideEffects());
    assertFalse(actualMethodOptimizationInfo.hasUnusedParameters());
    assertTrue(actualMethodOptimizationInfo.hasSideEffects());
    assertTrue(actualMethodOptimizationInfo.hasSynchronizedBlock());
    assertTrue(actualMethodOptimizationInfo.isKept());
    assertEquals(Integer.MAX_VALUE, actualMethodOptimizationInfo.getInvocationCount());
  }

  /**
   * Test {@link MethodOptimizationInfo#getMethodOptimizationInfo(Method)}.
   *
   * <ul>
   *   <li>When {@link LibraryMethod#LibraryMethod()}.
   *   <li>Then return {@code null}.
   * </ul>
   *
   * <p>Method under test: {@link MethodOptimizationInfo#getMethodOptimizationInfo(Method)}
   */
  @Test
  @DisplayName("Test getMethodOptimizationInfo(Method); when LibraryMethod(); then return 'null'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "MethodOptimizationInfo MethodOptimizationInfo.getMethodOptimizationInfo(Method)"
  })
  void testGetMethodOptimizationInfo_whenLibraryMethod_thenReturnNull() {
    // Arrange, Act and Assert
    assertNull(MethodOptimizationInfo.getMethodOptimizationInfo(new LibraryMethod()));
  }

  /**
   * Test getters and setters.
   *
   * <p>Methods under test:
   *
   * <ul>
   *   <li>default or parameterless constructor of {@link MethodOptimizationInfo}
   *   <li>{@link MethodOptimizationInfo#setReturnValue(Value)}
   *   <li>{@link MethodOptimizationInfo#setNoEscapingParameters()}
   *   <li>{@link MethodOptimizationInfo#setNoExternalReturnValues()}
   *   <li>{@link MethodOptimizationInfo#getEscapedParameters()}
   *   <li>{@link MethodOptimizationInfo#getInvocationCount()}
   *   <li>{@link MethodOptimizationInfo#getParameterSize()}
   *   <li>{@link MethodOptimizationInfo#getReturnValue()}
   *   <li>{@link MethodOptimizationInfo#getReturnedParameters()}
   *   <li>{@link MethodOptimizationInfo#getUsedParameters()}
   *   <li>{@link MethodOptimizationInfo#hasNoEscapingParameters()}
   *   <li>{@link MethodOptimizationInfo#hasNoExternalReturnValues()}
   *   <li>{@link MethodOptimizationInfo#hasNoExternalSideEffects()}
   *   <li>{@link MethodOptimizationInfo#hasNoSideEffects()}
   *   <li>{@link MethodOptimizationInfo#hasSynchronizedBlock()}
   *   <li>{@link MethodOptimizationInfo#hasUnusedParameters()}
   *   <li>{@link MethodOptimizationInfo#isKept()}
   * </ul>
   */
  @Test
  @DisplayName("Test getters and setters")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MethodOptimizationInfo.<init>()",
    "long MethodOptimizationInfo.getEscapedParameters()",
    "int MethodOptimizationInfo.getInvocationCount()",
    "int MethodOptimizationInfo.getParameterSize()",
    "Value MethodOptimizationInfo.getReturnValue()",
    "long MethodOptimizationInfo.getReturnedParameters()",
    "long MethodOptimizationInfo.getUsedParameters()",
    "boolean MethodOptimizationInfo.hasNoEscapingParameters()",
    "boolean MethodOptimizationInfo.hasNoExternalReturnValues()",
    "boolean MethodOptimizationInfo.hasNoExternalSideEffects()",
    "boolean MethodOptimizationInfo.hasNoSideEffects()",
    "boolean MethodOptimizationInfo.hasSynchronizedBlock()",
    "boolean MethodOptimizationInfo.hasUnusedParameters()",
    "boolean MethodOptimizationInfo.isKept()",
    "void MethodOptimizationInfo.setNoEscapingParameters()",
    "void MethodOptimizationInfo.setNoExternalReturnValues()",
    "void MethodOptimizationInfo.setReturnValue(Value)"
  })
  void testGettersAndSetters() {
    // Arrange and Act
    MethodOptimizationInfo actualMethodOptimizationInfo = new MethodOptimizationInfo();
    TopValue returnValue = new TopValue();
    actualMethodOptimizationInfo.setReturnValue(returnValue);
    actualMethodOptimizationInfo.setNoEscapingParameters();
    actualMethodOptimizationInfo.setNoExternalReturnValues();
    long actualEscapedParameters = actualMethodOptimizationInfo.getEscapedParameters();
    int actualInvocationCount = actualMethodOptimizationInfo.getInvocationCount();
    int actualParameterSize = actualMethodOptimizationInfo.getParameterSize();
    Value actualReturnValue = actualMethodOptimizationInfo.getReturnValue();
    long actualReturnedParameters = actualMethodOptimizationInfo.getReturnedParameters();
    long actualUsedParameters = actualMethodOptimizationInfo.getUsedParameters();
    boolean actualHasNoEscapingParametersResult =
        actualMethodOptimizationInfo.hasNoEscapingParameters();
    boolean actualHasNoExternalReturnValuesResult =
        actualMethodOptimizationInfo.hasNoExternalReturnValues();
    boolean actualHasNoExternalSideEffectsResult =
        actualMethodOptimizationInfo.hasNoExternalSideEffects();
    boolean actualHasNoSideEffectsResult = actualMethodOptimizationInfo.hasNoSideEffects();
    boolean actualHasSynchronizedBlockResult = actualMethodOptimizationInfo.hasSynchronizedBlock();
    boolean actualHasUnusedParametersResult = actualMethodOptimizationInfo.hasUnusedParameters();

    // Assert
    assertEquals(-1L, actualEscapedParameters);
    assertEquals(-1L, actualReturnedParameters);
    assertEquals(-1L, actualUsedParameters);
    assertEquals(0, actualParameterSize);
    assertFalse(actualHasNoExternalSideEffectsResult);
    assertFalse(actualHasNoSideEffectsResult);
    assertFalse(actualHasUnusedParametersResult);
    assertTrue(actualHasNoEscapingParametersResult);
    assertTrue(actualHasNoExternalReturnValuesResult);
    assertTrue(actualHasSynchronizedBlockResult);
    assertTrue(actualMethodOptimizationInfo.isKept());
    assertEquals(Integer.MAX_VALUE, actualInvocationCount);
    assertSame(returnValue, actualReturnValue);
  }
}
