package proguard.optimize.info;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import proguard.analysis.CallHandler;
import proguard.analysis.CallResolver;
import proguard.analysis.CallResolver.Builder;
import proguard.analysis.datastructure.callgraph.CallGraph;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.evaluation.ExecutingInvocationUnit;
import proguard.evaluation.ParticularReferenceValueFactory;

class OptimizationCodeAttributeFilterDiffblueTest {
  /**
   * Test {@link OptimizationCodeAttributeFilter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link Supplier#get()}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizationCodeAttributeFilter#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute); then calls get()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizationCodeAttributeFilter.visitCodeAttribute(Clazz, Method, CodeAttribute)"
  })
  void testVisitCodeAttribute_thenCallsGet() {
    // Arrange
    Supplier<Boolean> shouldAnalyzeNextCodeAttribute = mock(Supplier.class);
    when(shouldAnalyzeNextCodeAttribute.get()).thenReturn(true);

    Builder builder =
        new Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    Builder setEvaluateAllCodeResult =
        builder
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver attributeVisitor =
        setEvaluateAllCodeResult
            .setExecutingInvocationUnitBuilder(
                new ExecutingInvocationUnit.Builder(
                    KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(shouldAnalyzeNextCodeAttribute)
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();

    Builder builder2 =
        new Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    Builder setEvaluateAllCodeResult2 =
        builder2
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver otherAttributeVisitor =
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
            .build();

    OptimizationCodeAttributeFilter optimizationCodeAttributeFilter =
        new OptimizationCodeAttributeFilter(attributeVisitor, otherAttributeVisitor);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    // Act
    optimizationCodeAttributeFilter.visitCodeAttribute(clazz, method, new CodeAttribute());

    // Assert
    verify(shouldAnalyzeNextCodeAttribute).get();
  }
}
