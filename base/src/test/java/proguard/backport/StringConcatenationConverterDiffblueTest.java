package proguard.backport;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
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
import proguard.classfile.ProgramClass;
import proguard.classfile.attribute.BootstrapMethodInfo;
import proguard.classfile.attribute.BootstrapMethodsAttribute;
import proguard.classfile.attribute.visitor.BootstrapMethodInfoVisitor;
import proguard.classfile.constant.MethodHandleConstant;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.evaluation.ExecutingInvocationUnit;
import proguard.evaluation.ExecutingInvocationUnit.Builder;
import proguard.evaluation.ParticularReferenceValueFactory;
import proguard.evaluation.executor.Executor;
import proguard.evaluation.value.ValueFactory;

class StringConcatenationConverterDiffblueTest {
  /**
   * Test {@link StringConcatenationConverter#visitBootstrapMethodsAttribute(Clazz,
   * BootstrapMethodsAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link BootstrapMethodsAttribute#bootstrapMethodEntryAccept(Clazz, int,
   *       BootstrapMethodInfoVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link StringConcatenationConverter#visitBootstrapMethodsAttribute(Clazz,
   * BootstrapMethodsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute); then calls bootstrapMethodEntryAccept(Clazz, int, BootstrapMethodInfoVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void StringConcatenationConverter.visitBootstrapMethodsAttribute(Clazz, BootstrapMethodsAttribute)"
  })
  void testVisitBootstrapMethodsAttribute_thenCallsBootstrapMethodEntryAccept() {
    // Arrange
    CallResolver.Builder builder =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));

    CallResolver.Builder setEvaluateAllCodeResult =
        builder
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true);
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult
            .setExecutingInvocationUnitBuilder(
                new Builder(KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool))
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();
    StringConcatenationConverter stringConcatenationConverter =
        new StringConcatenationConverter(extraInstructionVisitor, new CodeAttributeEditor());
    LibraryClass clazz = new LibraryClass();

    BootstrapMethodsAttribute bootstrapMethodsAttribute = mock(BootstrapMethodsAttribute.class);
    doNothing()
        .when(bootstrapMethodsAttribute)
        .bootstrapMethodEntryAccept(
            Mockito.<Clazz>any(), anyInt(), Mockito.<BootstrapMethodInfoVisitor>any());
    doNothing().when(bootstrapMethodsAttribute).addProcessingFlags((int[]) Mockito.any());
    bootstrapMethodsAttribute.addProcessingFlags(2, 1, 2, 1);

    // Act
    stringConcatenationConverter.visitBootstrapMethodsAttribute(clazz, bootstrapMethodsAttribute);

    // Assert
    verify(bootstrapMethodsAttribute)
        .bootstrapMethodEntryAccept(isA(Clazz.class), eq(0), isA(BootstrapMethodInfoVisitor.class));
    verify(bootstrapMethodsAttribute).addProcessingFlags((int[]) Mockito.any());
  }

  /**
   * Test {@link StringConcatenationConverter#visitBootstrapMethodInfo(Clazz, BootstrapMethodInfo)}.
   *
   * <ul>
   *   <li>Given {@link Builder} {@link Builder#build(ValueFactory)} return {@code null}.
   *   <li>Then calls {@link MethodHandleConstant#getClassName(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link StringConcatenationConverter#visitBootstrapMethodInfo(Clazz,
   * BootstrapMethodInfo)}
   */
  @Test
  @DisplayName(
      "Test visitBootstrapMethodInfo(Clazz, BootstrapMethodInfo); given Builder build(ValueFactory) return 'null'; then calls getClassName(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void StringConcatenationConverter.visitBootstrapMethodInfo(Clazz, BootstrapMethodInfo)"
  })
  void testVisitBootstrapMethodInfo_givenBuilderBuildReturnNull_thenCallsGetClassName() {
    // Arrange
    Builder executingInvocationUnitBuilder = mock(Builder.class);
    when(executingInvocationUnitBuilder.build(Mockito.<ValueFactory>any())).thenReturn(null);
    when(executingInvocationUnitBuilder.addExecutor(Mockito.<Executor.Builder<?>>any()))
        .thenReturn(new Builder(KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool));
    executingInvocationUnitBuilder.addExecutor(mock(Executor.Builder.class));

    CallResolver.Builder builder =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));
    CallResolver extraInstructionVisitor =
        builder
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true)
            .setExecutingInvocationUnitBuilder(executingInvocationUnitBuilder)
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();
    StringConcatenationConverter stringConcatenationConverter =
        new StringConcatenationConverter(extraInstructionVisitor, new CodeAttributeEditor());

    MethodHandleConstant methodHandleConstant = mock(MethodHandleConstant.class);
    when(methodHandleConstant.getClassName(Mockito.<Clazz>any())).thenReturn("Class Name");

    ProgramClass clazz = mock(ProgramClass.class);
    when(clazz.getConstant(anyInt())).thenReturn(methodHandleConstant);

    // Act
    stringConcatenationConverter.visitBootstrapMethodInfo(clazz, new BootstrapMethodInfo());

    // Assert
    verify(clazz).getConstant(0);
    verify(methodHandleConstant).getClassName(isA(Clazz.class));
    verify(executingInvocationUnitBuilder).addExecutor(isA(Executor.Builder.class));
    verify(executingInvocationUnitBuilder).build(isA(ValueFactory.class));
  }

  /**
   * Test {@link StringConcatenationConverter#visitBootstrapMethodInfo(Clazz, BootstrapMethodInfo)}.
   *
   * <ul>
   *   <li>Given {@link MethodHandleConstant#MethodHandleConstant()}.
   *   <li>Then calls {@link ProgramClass#getRefClassName(int)}.
   * </ul>
   *
   * <p>Method under test: {@link StringConcatenationConverter#visitBootstrapMethodInfo(Clazz,
   * BootstrapMethodInfo)}
   */
  @Test
  @DisplayName(
      "Test visitBootstrapMethodInfo(Clazz, BootstrapMethodInfo); given MethodHandleConstant(); then calls getRefClassName(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void StringConcatenationConverter.visitBootstrapMethodInfo(Clazz, BootstrapMethodInfo)"
  })
  void testVisitBootstrapMethodInfo_givenMethodHandleConstant_thenCallsGetRefClassName() {
    // Arrange
    Builder executingInvocationUnitBuilder = mock(Builder.class);
    when(executingInvocationUnitBuilder.build(Mockito.<ValueFactory>any())).thenReturn(null);
    when(executingInvocationUnitBuilder.addExecutor(Mockito.<Executor.Builder<?>>any()))
        .thenReturn(new Builder(KotlinConstants.dummyClassPool, KotlinConstants.dummyClassPool));
    executingInvocationUnitBuilder.addExecutor(mock(Executor.Builder.class));

    CallResolver.Builder builder =
        new CallResolver.Builder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            CallGraph.concurrentCallGraph(),
            mock(CallHandler.class));
    CallResolver extraInstructionVisitor =
        builder
            .setArrayValueFactory(new ParticularReferenceValueFactory())
            .setClearCallValuesAfterVisit(true)
            .setEvaluateAllCode(true)
            .setExecutingInvocationUnitBuilder(executingInvocationUnitBuilder)
            .setIgnoreExceptions(true)
            .setIncludeSubClasses(true)
            .setMaxPartialEvaluations(3)
            .setShouldAnalyzeNextCodeAttribute(mock(Supplier.class))
            .setSkipIncompleteCalls(true)
            .setUseDominatorAnalysis(true)
            .build();
    StringConcatenationConverter stringConcatenationConverter =
        new StringConcatenationConverter(extraInstructionVisitor, new CodeAttributeEditor());

    ProgramClass clazz = mock(ProgramClass.class);
    when(clazz.getRefClassName(anyInt())).thenReturn("Ref Class Name");
    when(clazz.getConstant(anyInt())).thenReturn(new MethodHandleConstant());

    // Act
    stringConcatenationConverter.visitBootstrapMethodInfo(clazz, new BootstrapMethodInfo());

    // Assert
    verify(clazz).getConstant(0);
    verify(clazz).getRefClassName(0);
    verify(executingInvocationUnitBuilder).addExecutor(isA(Executor.Builder.class));
    verify(executingInvocationUnitBuilder).build(isA(ValueFactory.class));
  }
}
