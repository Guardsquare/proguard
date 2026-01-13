package proguard.optimize.info;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.Stack;
import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.analysis.CallHandler;
import proguard.analysis.CallResolver;
import proguard.analysis.datastructure.callgraph.CallGraph;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.util.BranchTargetFinder;
import proguard.evaluation.BasicBranchUnit;
import proguard.evaluation.BasicInvocationUnit;
import proguard.evaluation.ExecutingInvocationUnit;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.PartialEvaluator.Builder;
import proguard.evaluation.ParticularReferenceValueFactory;
import proguard.evaluation.util.DebugPrinter;

class ReferenceEscapeCheckerDiffblueTest {
  /**
   * Test {@link ReferenceEscapeChecker#ReferenceEscapeChecker()}.
   *
   * <p>Method under test: {@link ReferenceEscapeChecker#ReferenceEscapeChecker()}
   */
  @Test
  @DisplayName("Test new ReferenceEscapeChecker()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReferenceEscapeChecker.<init>()"})
  void testNewReferenceEscapeChecker() {
    // Arrange and Act
    ReferenceEscapeChecker actualReferenceEscapeChecker = new ReferenceEscapeChecker();

    // Assert
    assertFalse(actualReferenceEscapeChecker.isInstanceEscaping(1));
    assertFalse(actualReferenceEscapeChecker.isInstanceExternal(1));
    assertFalse(actualReferenceEscapeChecker.isInstanceModified(1));
    assertFalse(actualReferenceEscapeChecker.isInstanceReturned(1));
  }

  /**
   * Test {@link ReferenceEscapeChecker#ReferenceEscapeChecker(PartialEvaluator, boolean)}.
   *
   * <p>Method under test: {@link ReferenceEscapeChecker#ReferenceEscapeChecker(PartialEvaluator,
   * boolean)}
   */
  @Test
  @DisplayName("Test new ReferenceEscapeChecker(PartialEvaluator, boolean)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ReferenceEscapeChecker.<init>(PartialEvaluator, boolean)"})
  void testNewReferenceEscapeChecker2() {
    // Arrange and Act
    ReferenceEscapeChecker actualReferenceEscapeChecker =
        new ReferenceEscapeChecker(new PartialEvaluator(), true);

    // Assert
    assertFalse(actualReferenceEscapeChecker.isInstanceEscaping(1));
    assertFalse(actualReferenceEscapeChecker.isInstanceExternal(1));
    assertFalse(actualReferenceEscapeChecker.isInstanceModified(1));
    assertFalse(actualReferenceEscapeChecker.isInstanceReturned(1));
  }

  /**
   * Test {@link ReferenceEscapeChecker#isInstanceEscaping(int)}.
   *
   * <ul>
   *   <li>Given {@link ReferenceEscapeChecker#ReferenceEscapeChecker()}.
   *   <li>When one.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ReferenceEscapeChecker#isInstanceEscaping(int)}
   */
  @Test
  @DisplayName(
      "Test isInstanceEscaping(int); given ReferenceEscapeChecker(); when one; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ReferenceEscapeChecker.isInstanceEscaping(int)"})
  void testIsInstanceEscaping_givenReferenceEscapeChecker_whenOne_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ReferenceEscapeChecker().isInstanceEscaping(1));
  }

  /**
   * Test {@link ReferenceEscapeChecker#isInstanceReturned(int)}.
   *
   * <ul>
   *   <li>Given {@link ReferenceEscapeChecker#ReferenceEscapeChecker()}.
   *   <li>When one.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ReferenceEscapeChecker#isInstanceReturned(int)}
   */
  @Test
  @DisplayName(
      "Test isInstanceReturned(int); given ReferenceEscapeChecker(); when one; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ReferenceEscapeChecker.isInstanceReturned(int)"})
  void testIsInstanceReturned_givenReferenceEscapeChecker_whenOne_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ReferenceEscapeChecker().isInstanceReturned(1));
  }

  /**
   * Test {@link ReferenceEscapeChecker#isInstanceModified(int)}.
   *
   * <ul>
   *   <li>Given {@link ReferenceEscapeChecker#ReferenceEscapeChecker()}.
   *   <li>When one.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ReferenceEscapeChecker#isInstanceModified(int)}
   */
  @Test
  @DisplayName(
      "Test isInstanceModified(int); given ReferenceEscapeChecker(); when one; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ReferenceEscapeChecker.isInstanceModified(int)"})
  void testIsInstanceModified_givenReferenceEscapeChecker_whenOne_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ReferenceEscapeChecker().isInstanceModified(1));
  }

  /**
   * Test {@link ReferenceEscapeChecker#isInstanceExternal(int)}.
   *
   * <ul>
   *   <li>Given {@link ReferenceEscapeChecker#ReferenceEscapeChecker()}.
   *   <li>When one.
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link ReferenceEscapeChecker#isInstanceExternal(int)}
   */
  @Test
  @DisplayName(
      "Test isInstanceExternal(int); given ReferenceEscapeChecker(); when one; then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean ReferenceEscapeChecker.isInstanceExternal(int)"})
  void testIsInstanceExternal_givenReferenceEscapeChecker_whenOne_thenReturnFalse() {
    // Arrange, Act and Assert
    assertFalse(new ReferenceEscapeChecker().isInstanceExternal(1));
  }

  /**
   * Test {@link ReferenceEscapeChecker#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryMethod#getDescriptor(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link ReferenceEscapeChecker#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then calls getDescriptor(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ReferenceEscapeChecker.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute_thenCallsGetDescriptor() {
    // Arrange
    Builder setBranchTargetFinderResult =
        Builder.create().setBranchTargetFinder(mock(BranchTargetFinder.class));

    Builder setBranchUnitResult = setBranchTargetFinderResult.setBranchUnit(new BasicBranchUnit());

    Builder setEvaluateAllCodeResult =
        setBranchUnitResult.setCallingInstructionBlockStack(new Stack<>()).setEvaluateAllCode(true);

    CallResolver.Builder builder =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult2 =
        builder
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);

    Builder setExtraInstructionVisitorResult =
        setEvaluateAllCodeResult.setExtraInstructionVisitor(
            setEvaluateAllCodeResult2
                .setExecutingInvocationUnitBuilder(
                    new ExecutingInvocationUnit.Builder(
                        KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
                .setIgnoreExceptions(true)
                .setIncludeSubClasses(true)
                .setMaxPartialEvaluations(3)
                .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
                .setSkipIncompleteCalls(true)
                .setUseDominatorAnalysis(true)
                .build());

    Builder setPrettyPrintingResult =
        setExtraInstructionVisitorResult
            .setInvocationUnit(new BasicInvocationUnit(new ParticularReferenceValueFactory()))
            .setPrettyPrinting(1);

    Builder stopAnalysisAfterNEvaluationsResult =
        setPrettyPrintingResult
            .setStateTracker(new DebugPrinter(true, true))
            .stopAnalysisAfterNEvaluations(42);
    PartialEvaluator partialEvaluator =
        stopAnalysisAfterNEvaluationsResult
            .setValueFactory(new ParticularReferenceValueFactory())
            .build();
    ReferenceEscapeChecker referenceEscapeChecker =
        new ReferenceEscapeChecker(partialEvaluator, false);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    referenceEscapeChecker.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method).getDescriptor(isA(Clazz.class));
    verify(method).getName(isA(Clazz.class));
  }

  /**
   * Test {@link ReferenceEscapeChecker#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <p>Method under test: {@link ReferenceEscapeChecker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ReferenceEscapeChecker.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant() {
    // Arrange
    ReferenceEscapeChecker referenceEscapeChecker = new ReferenceEscapeChecker();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getType(anyInt())).thenReturn("Type");
    LibraryClass referencedClass = new LibraryClass();

    // Act
    referenceEscapeChecker.visitAnyMethodrefConstant(
        clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, new LibraryMethod()));

    // Assert
    verify(clazz).getType(1);
  }

  /**
   * Test {@link ReferenceEscapeChecker#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Given empty string.
   * </ul>
   *
   * <p>Method under test: {@link ReferenceEscapeChecker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName("Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); given empty string")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ReferenceEscapeChecker.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_givenEmptyString() {
    // Arrange
    ReferenceEscapeChecker referenceEscapeChecker = new ReferenceEscapeChecker();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getType(anyInt())).thenReturn("");
    LibraryClass referencedClass = new LibraryClass();

    // Act
    referenceEscapeChecker.visitAnyMethodrefConstant(
        clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, new LibraryMethod()));

    // Assert
    verify(clazz).getType(1);
  }

  /**
   * Test {@link ReferenceEscapeChecker#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link ReferenceEscapeChecker#ReferenceEscapeChecker()}.
   *   <li>Then calls {@link LibraryClass#getType(int)}.
   * </ul>
   *
   * <p>Method under test: {@link ReferenceEscapeChecker#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); given ReferenceEscapeChecker(); then calls getType(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ReferenceEscapeChecker.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_givenReferenceEscapeChecker_thenCallsGetType() {
    // Arrange
    ReferenceEscapeChecker referenceEscapeChecker = new ReferenceEscapeChecker();

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getType(anyInt())).thenReturn("Type");

    // Act
    referenceEscapeChecker.visitAnyMethodrefConstant(clazz, new InterfaceMethodrefConstant());

    // Assert
    verify(clazz).getType(0);
  }
}
