package proguard.optimize;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.UnsupportedEncodingException;
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

class ChangedCodePrinterDiffblueTest {
  /**
   * Test {@link ChangedCodePrinter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link ChangedCodePrinter#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ChangedCodePrinter.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute() throws UnsupportedEncodingException {
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
    ChangedCodePrinter changedCodePrinter = new ChangedCodePrinter(attributeVisitor);
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    LibraryMethod method = new LibraryMethod(1, "Name", "Descriptor");
    CodeAttribute codeAttribute = new CodeAttribute(1, 3, 3, 8, "AXAXAXAX".getBytes("UTF-8"));

    // Act
    changedCodePrinter.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(shouldAnalyzeNextCodeAttribute).get();
  }

  /**
   * Test {@link ChangedCodePrinter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <p>Method under test: {@link ChangedCodePrinter#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ChangedCodePrinter.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute2() throws UnsupportedEncodingException {
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
    ChangedCodePrinter changedCodePrinter = new ChangedCodePrinter(attributeVisitor);
    LibraryClass clazz = new LibraryClass(1, "This Class Name", "Super Class Name");
    LibraryMethod method = new LibraryMethod(8, "Name", "Descriptor");
    CodeAttribute codeAttribute = new CodeAttribute(1, 3, 3, 8, "AXAXAXAX".getBytes("UTF-8"));

    // Act
    changedCodePrinter.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(shouldAnalyzeNextCodeAttribute).get();
  }

  /**
   * Test {@link ChangedCodePrinter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Given zero.
   * </ul>
   *
   * <p>Method under test: {@link ChangedCodePrinter#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName("Test visitCodeAttribute(Clazz, Method, CodeAttribute); given zero")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void ChangedCodePrinter.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_givenZero() {
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
    ChangedCodePrinter changedCodePrinter = new ChangedCodePrinter(attributeVisitor);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute(1);
    codeAttribute.u4codeLength = 0;

    // Act
    changedCodePrinter.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(shouldAnalyzeNextCodeAttribute).get();
  }
}
