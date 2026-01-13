package proguard.optimize.peephole;

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
import proguard.analysis.CallHandler;
import proguard.analysis.CallResolver;
import proguard.analysis.CallResolver.Builder;
import proguard.analysis.datastructure.callgraph.CallGraph;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.SimpleInstruction;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.evaluation.ExecutingInvocationUnit;
import proguard.evaluation.ParticularReferenceValueFactory;

class NopRemoverDiffblueTest {
  /**
   * Test {@link NopRemover#visitSimpleInstruction(Clazz, Method, CodeAttribute, int,
   * SimpleInstruction)}.
   *
   * <p>Method under test: {@link NopRemover#visitSimpleInstruction(Clazz, Method, CodeAttribute,
   * int, SimpleInstruction)}
   */
  @Test
  @DisplayName("Test visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NopRemover.visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)"
  })
  void testVisitSimpleInstruction() {
    // Arrange
    CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
    when(codeAttributeEditor.isModified(anyInt())).thenReturn(false);
    doNothing().when(codeAttributeEditor).deleteInstruction(anyInt());

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
    CallResolver extraInstructionVisitor =
        setEvaluateAllCodeResult
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

    NopRemover nopRemover = new NopRemover(codeAttributeEditor, extraInstructionVisitor);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 2, new SimpleInstruction());

    // Assert
    verify(codeAttributeEditor).deleteInstruction(2);
    verify(codeAttributeEditor).isModified(2);
  }

  /**
   * Test {@link NopRemover#visitSimpleInstruction(Clazz, Method, CodeAttribute, int,
   * SimpleInstruction)}.
   *
   * <ul>
   *   <li>Then calls {@link CodeAttributeEditor#deleteInstruction(int)}.
   * </ul>
   *
   * <p>Method under test: {@link NopRemover#visitSimpleInstruction(Clazz, Method, CodeAttribute,
   * int, SimpleInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction); then calls deleteInstruction(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NopRemover.visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)"
  })
  void testVisitSimpleInstruction_thenCallsDeleteInstruction() {
    // Arrange
    CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
    when(codeAttributeEditor.isModified(anyInt())).thenReturn(false);
    doNothing().when(codeAttributeEditor).deleteInstruction(anyInt());
    NopRemover nopRemover = new NopRemover(codeAttributeEditor);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 2, new SimpleInstruction());

    // Assert
    verify(codeAttributeEditor).deleteInstruction(2);
    verify(codeAttributeEditor).isModified(2);
  }

  /**
   * Test {@link NopRemover#visitSimpleInstruction(Clazz, Method, CodeAttribute, int,
   * SimpleInstruction)}.
   *
   * <ul>
   *   <li>Then calls {@link CodeAttributeEditor#isModified(int)}.
   * </ul>
   *
   * <p>Method under test: {@link NopRemover#visitSimpleInstruction(Clazz, Method, CodeAttribute,
   * int, SimpleInstruction)}
   */
  @Test
  @DisplayName(
      "Test visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction); then calls isModified(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void NopRemover.visitSimpleInstruction(Clazz, Method, CodeAttribute, int, SimpleInstruction)"
  })
  void testVisitSimpleInstruction_thenCallsIsModified() {
    // Arrange
    CodeAttributeEditor codeAttributeEditor = mock(CodeAttributeEditor.class);
    when(codeAttributeEditor.isModified(anyInt())).thenReturn(true);
    NopRemover nopRemover = new NopRemover(codeAttributeEditor);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    nopRemover.visitSimpleInstruction(clazz, method, codeAttribute, 2, new SimpleInstruction());

    // Assert
    verify(codeAttributeEditor).isModified(2);
  }
}
