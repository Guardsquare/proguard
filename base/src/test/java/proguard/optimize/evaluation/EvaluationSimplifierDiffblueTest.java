package proguard.optimize.evaluation;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
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
import proguard.classfile.instruction.Instruction;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.util.BranchTargetFinder;
import proguard.classfile.visitor.MemberVisitor;
import proguard.evaluation.BasicBranchUnit;
import proguard.evaluation.BasicInvocationUnit;
import proguard.evaluation.ExecutingInvocationUnit;
import proguard.evaluation.PartialEvaluator;
import proguard.evaluation.PartialEvaluator.Builder;
import proguard.evaluation.ParticularReferenceValueFactory;
import proguard.evaluation.TracedStack;
import proguard.evaluation.TracedVariables;
import proguard.evaluation.Variables;
import proguard.evaluation.util.DebugPrinter;
import proguard.evaluation.util.jsonprinter.JsonPrinter;

class EvaluationSimplifierDiffblueTest {
  /**
   * Test {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute() {
    // Arrange
    Builder createResult = Builder.create();

    Builder setBranchTargetFinderResult =
        createResult.setBranchTargetFinder(new BranchTargetFinder());

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

    CallResolver.Builder builder2 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult3 =
        builder2
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult3
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    EvaluationSimplifier evaluationSimplifier =
        new EvaluationSimplifier(partialEvaluator, extraInstructionVisitor, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    doNothing().when(method).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());

    // Act
    evaluationSimplifier.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    verify(method).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
  }

  /**
   * Test {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute2() {
    // Arrange
    BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
    when(branchTargetFinder.isBranchTarget(anyInt())).thenReturn(true);

    Builder setBranchTargetFinderResult =
        Builder.create().setBranchTargetFinder(branchTargetFinder);

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

    CallResolver.Builder builder2 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult3 =
        builder2
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult3
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    EvaluationSimplifier evaluationSimplifier =
        new EvaluationSimplifier(partialEvaluator, extraInstructionVisitor, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    doNothing().when(method).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
    CodeAttribute codeAttribute =
        new CodeAttribute(1, -1, 3, 3, new byte[] {'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    evaluationSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
    verify(branchTargetFinder, atLeast(1)).isBranchTarget(anyInt());
  }

  /**
   * Test {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute3() {
    // Arrange
    BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
    when(branchTargetFinder.isBranchTarget(anyInt())).thenReturn(true);

    Builder setBranchTargetFinderResult =
        Builder.create().setBranchTargetFinder(branchTargetFinder);

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

    CallResolver.Builder builder2 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult3 =
        builder2
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult3
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    EvaluationSimplifier evaluationSimplifier =
        new EvaluationSimplifier(partialEvaluator, extraInstructionVisitor, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    doNothing().when(method).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, -1, 3, new byte[] {'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    evaluationSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
    verify(branchTargetFinder, atLeast(1)).isBranchTarget(anyInt());
  }

  /**
   * Test {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute4() {
    // Arrange
    BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
    when(branchTargetFinder.isBranchTarget(anyInt())).thenReturn(true);

    Builder setBranchTargetFinderResult =
        Builder.create().setBranchTargetFinder(branchTargetFinder);

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

    CallResolver.Builder builder2 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult3 =
        builder2
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult3
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    EvaluationSimplifier evaluationSimplifier =
        new EvaluationSimplifier(partialEvaluator, extraInstructionVisitor, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    doNothing().when(method).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, -1, new byte[] {'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    evaluationSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
    verify(branchTargetFinder).isBranchTarget(0);
  }

  /**
   * Test {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute5() {
    // Arrange
    BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
    doNothing()
        .when(branchTargetFinder)
        .visitCodeAttribute(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<CodeAttribute>any());
    when(branchTargetFinder.isBranchTarget(anyInt())).thenReturn(true);

    Builder setBranchTargetFinderResult =
        Builder.create().setBranchTargetFinder(branchTargetFinder);

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

    BasicInvocationUnit invocationUnit = mock(BasicInvocationUnit.class);
    doNothing()
        .when(invocationUnit)
        .enterMethod(Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<Variables>any());

    Builder setPrettyPrintingResult =
        setExtraInstructionVisitorResult.setInvocationUnit(invocationUnit).setPrettyPrinting(1);

    Builder stopAnalysisAfterNEvaluationsResult =
        setPrettyPrintingResult
            .setStateTracker(new DebugPrinter(true, true))
            .stopAnalysisAfterNEvaluations(42);
    PartialEvaluator partialEvaluator =
        stopAnalysisAfterNEvaluationsResult
            .setValueFactory(new ParticularReferenceValueFactory())
            .build();

    CallResolver.Builder builder2 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult3 =
        builder2
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult3
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    EvaluationSimplifier evaluationSimplifier =
        new EvaluationSimplifier(partialEvaluator, extraInstructionVisitor, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    doNothing().when(method).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    evaluationSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
    verify(branchTargetFinder, atLeast(1)).isBranchTarget(anyInt());
    verify(branchTargetFinder)
        .visitCodeAttribute(isA(Clazz.class), isA(Method.class), isA(CodeAttribute.class));
    verify(invocationUnit).enterMethod(isA(Clazz.class), isA(Method.class), isA(Variables.class));
  }

  /**
   * Test {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute6() {
    // Arrange
    BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
    doNothing()
        .when(branchTargetFinder)
        .visitCodeAttribute(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<CodeAttribute>any());

    Builder setBranchTargetFinderResult =
        Builder.create().setBranchTargetFinder(branchTargetFinder);

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

    BasicInvocationUnit invocationUnit = mock(BasicInvocationUnit.class);
    doNothing()
        .when(invocationUnit)
        .enterMethod(Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<Variables>any());

    Builder setPrettyPrintingResult =
        setExtraInstructionVisitorResult.setInvocationUnit(invocationUnit).setPrettyPrinting(1);

    Builder stopAnalysisAfterNEvaluationsResult =
        setPrettyPrintingResult
            .setStateTracker(new DebugPrinter(false, true))
            .stopAnalysisAfterNEvaluations(42);
    PartialEvaluator partialEvaluator =
        stopAnalysisAfterNEvaluationsResult
            .setValueFactory(new ParticularReferenceValueFactory())
            .build();

    CallResolver.Builder builder2 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult3 =
        builder2
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult3
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    EvaluationSimplifier evaluationSimplifier =
        new EvaluationSimplifier(partialEvaluator, extraInstructionVisitor, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    evaluationSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
    verify(branchTargetFinder)
        .visitCodeAttribute(isA(Clazz.class), isA(Method.class), isA(CodeAttribute.class));
    verify(invocationUnit).enterMethod(isA(Clazz.class), isA(Method.class), isA(Variables.class));
  }

  /**
   * Test {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute7() {
    // Arrange
    BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
    doNothing()
        .when(branchTargetFinder)
        .visitCodeAttribute(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<CodeAttribute>any());

    Builder setBranchTargetFinderResult =
        Builder.create().setBranchTargetFinder(branchTargetFinder);

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

    BasicInvocationUnit invocationUnit = mock(BasicInvocationUnit.class);
    doNothing()
        .when(invocationUnit)
        .enterMethod(Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<Variables>any());

    Builder stopAnalysisAfterNEvaluationsResult =
        setExtraInstructionVisitorResult
            .setInvocationUnit(invocationUnit)
            .setPrettyPrinting(1)
            .setStateTracker(null)
            .stopAnalysisAfterNEvaluations(42);
    PartialEvaluator partialEvaluator =
        stopAnalysisAfterNEvaluationsResult
            .setValueFactory(new ParticularReferenceValueFactory())
            .build();

    CallResolver.Builder builder2 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult3 =
        builder2
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult3
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    EvaluationSimplifier evaluationSimplifier =
        new EvaluationSimplifier(partialEvaluator, extraInstructionVisitor, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    evaluationSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
    verify(branchTargetFinder)
        .visitCodeAttribute(isA(Clazz.class), isA(Method.class), isA(CodeAttribute.class));
    verify(invocationUnit).enterMethod(isA(Clazz.class), isA(Method.class), isA(Variables.class));
  }

  /**
   * Test {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute8() {
    // Arrange
    BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
    doNothing()
        .when(branchTargetFinder)
        .visitCodeAttribute(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<CodeAttribute>any());

    Builder setBranchTargetFinderResult =
        Builder.create().setBranchTargetFinder(branchTargetFinder);

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

    BasicInvocationUnit invocationUnit = mock(BasicInvocationUnit.class);
    doNothing()
        .when(invocationUnit)
        .enterMethod(Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<Variables>any());

    Builder setPrettyPrintingResult =
        setExtraInstructionVisitorResult.setInvocationUnit(invocationUnit).setPrettyPrinting(1);

    Builder stopAnalysisAfterNEvaluationsResult =
        setPrettyPrintingResult
            .setStateTracker(new JsonPrinter())
            .stopAnalysisAfterNEvaluations(42);
    PartialEvaluator partialEvaluator =
        stopAnalysisAfterNEvaluationsResult
            .setValueFactory(new ParticularReferenceValueFactory())
            .build();

    CallResolver.Builder builder2 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult3 =
        builder2
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult3
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    EvaluationSimplifier evaluationSimplifier =
        new EvaluationSimplifier(partialEvaluator, extraInstructionVisitor, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    evaluationSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
    verify(branchTargetFinder)
        .visitCodeAttribute(isA(Clazz.class), isA(Method.class), isA(CodeAttribute.class));
    verify(invocationUnit).enterMethod(isA(Clazz.class), isA(Method.class), isA(Variables.class));
  }

  /**
   * Test {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute9() {
    // Arrange
    BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
    doNothing()
        .when(branchTargetFinder)
        .visitCodeAttribute(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<CodeAttribute>any());

    Builder setBranchTargetFinderResult =
        Builder.create().setBranchTargetFinder(branchTargetFinder);

    BasicBranchUnit branchUnit = mock(BasicBranchUnit.class);
    doNothing().when(branchUnit).reset();

    Builder setBranchUnitResult = setBranchTargetFinderResult.setBranchUnit(branchUnit);

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

    BasicInvocationUnit invocationUnit = mock(BasicInvocationUnit.class);
    doNothing()
        .when(invocationUnit)
        .enterMethod(Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<Variables>any());

    Builder setPrettyPrintingResult =
        setExtraInstructionVisitorResult.setInvocationUnit(invocationUnit).setPrettyPrinting(1);

    DebugPrinter stateTracker = mock(DebugPrinter.class);
    doNothing()
        .when(stateTracker)
        .registerException(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            Mockito.<PartialEvaluator>any(),
            Mockito.<Throwable>any());
    doNothing()
        .when(stateTracker)
        .startCodeAttribute(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            Mockito.<Variables>any());
    doNothing()
        .when(stateTracker)
        .startInstructionBlock(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            Mockito.<TracedVariables>any(),
            Mockito.<TracedStack>any(),
            anyInt());
    doNothing()
        .when(stateTracker)
        .startInstructionEvaluation(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            anyInt(),
            Mockito.<Instruction>any(),
            Mockito.<TracedVariables>any(),
            Mockito.<TracedStack>any(),
            anyInt());

    Builder stopAnalysisAfterNEvaluationsResult =
        setPrettyPrintingResult.setStateTracker(stateTracker).stopAnalysisAfterNEvaluations(42);
    PartialEvaluator partialEvaluator =
        stopAnalysisAfterNEvaluationsResult
            .setValueFactory(new ParticularReferenceValueFactory())
            .build();

    CallResolver.Builder builder2 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult3 =
        builder2
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult3
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    EvaluationSimplifier evaluationSimplifier =
        new EvaluationSimplifier(partialEvaluator, extraInstructionVisitor, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    evaluationSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
    verify(branchTargetFinder)
        .visitCodeAttribute(isA(Clazz.class), isA(Method.class), isA(CodeAttribute.class));
    verify(branchUnit).reset();
    verify(invocationUnit).enterMethod(isA(Clazz.class), isA(Method.class), isA(Variables.class));
    verify(stateTracker)
        .registerException(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            isA(PartialEvaluator.class),
            isA(Throwable.class));
    verify(stateTracker)
        .startCodeAttribute(
            isA(Clazz.class), isA(Method.class), isA(CodeAttribute.class), isA(Variables.class));
    verify(stateTracker)
        .startInstructionBlock(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            isA(TracedVariables.class),
            isA(TracedStack.class),
            eq(0));
    verify(stateTracker)
        .startInstructionEvaluation(
            isA(Clazz.class),
            isA(Method.class),
            eq(0),
            isA(Instruction.class),
            isA(TracedVariables.class),
            isA(TracedStack.class),
            eq(0));
  }

  /**
   * Test {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Given {@link BranchTargetFinder} {@link BranchTargetFinder#isBranchTarget(int)} return
   *       {@code true}.
   * </ul>
   *
   * <p>Method under test: {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); given BranchTargetFinder isBranchTarget(int) return 'true'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_givenBranchTargetFinderIsBranchTargetReturnTrue() {
    // Arrange
    BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
    when(branchTargetFinder.isBranchTarget(anyInt())).thenReturn(true);

    Builder setBranchTargetFinderResult =
        Builder.create().setBranchTargetFinder(branchTargetFinder);

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

    CallResolver.Builder builder2 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult3 =
        builder2
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult3
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    EvaluationSimplifier evaluationSimplifier =
        new EvaluationSimplifier(partialEvaluator, extraInstructionVisitor, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    doNothing().when(method).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());

    // Act
    evaluationSimplifier.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    verify(method).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
    verify(branchTargetFinder).isBranchTarget(0);
  }

  /**
   * Test {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Given one.
   *   <li>Then calls {@link LibraryMethod#getAccessFlags()}.
   * </ul>
   *
   * <p>Method under test: {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); given one; then calls getAccessFlags()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_givenOne_thenCallsGetAccessFlags() {
    // Arrange
    BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
    when(branchTargetFinder.isBranchTarget(anyInt())).thenReturn(true);

    Builder setBranchTargetFinderResult =
        Builder.create().setBranchTargetFinder(branchTargetFinder);

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

    CallResolver.Builder builder2 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult3 =
        builder2
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult3
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    EvaluationSimplifier evaluationSimplifier =
        new EvaluationSimplifier(partialEvaluator, extraInstructionVisitor, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getAccessFlags()).thenReturn(1);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    doNothing().when(method).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    evaluationSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(method).getAccessFlags();
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
    verify(branchTargetFinder, atLeast(1)).isBranchTarget(anyInt());
  }

  /**
   * Test {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link BranchTargetFinder#isExceptionHandler(int)}.
   * </ul>
   *
   * <p>Method under test: {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then calls isExceptionHandler(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenCallsIsExceptionHandler() {
    // Arrange
    BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
    when(branchTargetFinder.isBranchTarget(anyInt())).thenReturn(false);
    when(branchTargetFinder.isExceptionHandler(anyInt())).thenReturn(true);

    Builder setBranchTargetFinderResult =
        Builder.create().setBranchTargetFinder(branchTargetFinder);

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

    CallResolver.Builder builder2 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult3 =
        builder2
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult3
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    EvaluationSimplifier evaluationSimplifier =
        new EvaluationSimplifier(partialEvaluator, extraInstructionVisitor, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getAccessFlags()).thenReturn(1);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    doNothing().when(method).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    evaluationSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(method).getAccessFlags();
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
    verify(branchTargetFinder, atLeast(1)).isBranchTarget(anyInt());
    verify(branchTargetFinder, atLeast(1)).isExceptionHandler(anyInt());
  }

  /**
   * Test {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link Builder#setBranchTargetFinder(BranchTargetFinder)}.
   * </ul>
   *
   * <p>Method under test: {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then calls setBranchTargetFinder(BranchTargetFinder)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenCallsSetBranchTargetFinder() {
    // Arrange
    Builder builder = mock(Builder.class);
    when(builder.setBranchTargetFinder(Mockito.<BranchTargetFinder>any()))
        .thenReturn(Builder.create());

    Builder setBranchTargetFinderResult =
        builder.setBranchTargetFinder(mock(BranchTargetFinder.class));

    BasicBranchUnit branchUnit = mock(BasicBranchUnit.class);
    doNothing().when(branchUnit).reset();

    Builder setBranchUnitResult = setBranchTargetFinderResult.setBranchUnit(branchUnit);

    Builder setEvaluateAllCodeResult =
        setBranchUnitResult.setCallingInstructionBlockStack(new Stack<>()).setEvaluateAllCode(true);

    CallResolver.Builder builder2 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult2 =
        builder2
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

    BasicInvocationUnit invocationUnit = mock(BasicInvocationUnit.class);
    doNothing()
        .when(invocationUnit)
        .enterMethod(Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<Variables>any());

    Builder setPrettyPrintingResult =
        setExtraInstructionVisitorResult.setInvocationUnit(invocationUnit).setPrettyPrinting(1);

    DebugPrinter stateTracker = mock(DebugPrinter.class);
    doNothing()
        .when(stateTracker)
        .registerException(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            Mockito.<PartialEvaluator>any(),
            Mockito.<Throwable>any());
    doNothing()
        .when(stateTracker)
        .startCodeAttribute(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            Mockito.<Variables>any());
    doNothing()
        .when(stateTracker)
        .startInstructionBlock(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            Mockito.<TracedVariables>any(),
            Mockito.<TracedStack>any(),
            anyInt());
    doNothing()
        .when(stateTracker)
        .startInstructionEvaluation(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            anyInt(),
            Mockito.<Instruction>any(),
            Mockito.<TracedVariables>any(),
            Mockito.<TracedStack>any(),
            anyInt());

    Builder stopAnalysisAfterNEvaluationsResult =
        setPrettyPrintingResult.setStateTracker(stateTracker).stopAnalysisAfterNEvaluations(42);
    PartialEvaluator partialEvaluator =
        stopAnalysisAfterNEvaluationsResult
            .setValueFactory(new ParticularReferenceValueFactory())
            .build();

    CallResolver.Builder builder3 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult3 =
        builder3
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult3
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    EvaluationSimplifier evaluationSimplifier =
        new EvaluationSimplifier(partialEvaluator, extraInstructionVisitor, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    CodeAttribute codeAttribute =
        new CodeAttribute(1, 3, 3, 3, new byte[] {'A', 1, 'A', 1, 'A', 1, 'A', 1});

    // Act
    evaluationSimplifier.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
    verify(branchUnit).reset();
    verify(builder).setBranchTargetFinder(isA(BranchTargetFinder.class));
    verify(invocationUnit).enterMethod(isA(Clazz.class), isA(Method.class), isA(Variables.class));
    verify(stateTracker)
        .registerException(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            isA(PartialEvaluator.class),
            isA(Throwable.class));
    verify(stateTracker)
        .startCodeAttribute(
            isA(Clazz.class), isA(Method.class), isA(CodeAttribute.class), isA(Variables.class));
    verify(stateTracker)
        .startInstructionBlock(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            isA(TracedVariables.class),
            isA(TracedStack.class),
            eq(0));
    verify(stateTracker)
        .startInstructionEvaluation(
            isA(Clazz.class),
            isA(Method.class),
            eq(0),
            isA(Instruction.class),
            isA(TracedVariables.class),
            isA(TracedStack.class),
            eq(0));
  }

  /**
   * Test {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>When {@link CodeAttribute#CodeAttribute(int)} with u2attributeNameIndex is one.
   * </ul>
   *
   * <p>Method under test: {@link EvaluationSimplifier#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); when CodeAttribute(int) with u2attributeNameIndex is one")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void EvaluationSimplifier.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_whenCodeAttributeWithU2attributeNameIndexIsOne() {
    // Arrange
    BranchTargetFinder branchTargetFinder = mock(BranchTargetFinder.class);
    when(branchTargetFinder.isBranchTarget(anyInt())).thenReturn(true);

    Builder setBranchTargetFinderResult =
        Builder.create().setBranchTargetFinder(branchTargetFinder);

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

    CallResolver.Builder builder2 =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult3 =
        builder2
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult3
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    EvaluationSimplifier evaluationSimplifier =
        new EvaluationSimplifier(partialEvaluator, extraInstructionVisitor, true);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getDescriptor(Mockito.<Clazz>any())).thenReturn("Descriptor");
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    doNothing().when(method).accept(Mockito.<Clazz>any(), Mockito.<MemberVisitor>any());

    // Act
    evaluationSimplifier.visitCodeAttribute(clazz, method, new CodeAttribute(1));

    // Assert
    verify(method).accept(isA(Clazz.class), isA(MemberVisitor.class));
    verify(method, atLeast(1)).getDescriptor(isA(Clazz.class));
    verify(method, atLeast(1)).getName(isA(Clazz.class));
    verify(branchTargetFinder).isBranchTarget(0);
  }
}
