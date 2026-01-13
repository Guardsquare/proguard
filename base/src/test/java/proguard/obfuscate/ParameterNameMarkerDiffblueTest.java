package proguard.obfuscate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
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
import proguard.classfile.attribute.LocalVariableInfo;
import proguard.classfile.attribute.LocalVariableTableAttribute;
import proguard.classfile.attribute.LocalVariableTypeInfo;
import proguard.classfile.attribute.LocalVariableTypeTableAttribute;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.evaluation.ExecutingInvocationUnit;
import proguard.evaluation.ParticularReferenceValueFactory;

class ParameterNameMarkerDiffblueTest {
  /**
   * Test {@link ParameterNameMarker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <p>Method under test: {@link ParameterNameMarker#visitLocalVariableTableAttribute(Clazz,
   * Method, CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterNameMarker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute() {
    // Arrange
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
    CallResolver attributeUsageMarker =
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
    ParameterNameMarker parameterNameMarker = new ParameterNameMarker(attributeUsageMarker);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod(1, "Name", "Descriptor");
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTableAttribute localVariableTableAttribute = new LocalVariableTableAttribute();

    // Act
    parameterNameMarker.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert that nothing has changed
    assertEquals(0, localVariableTableAttribute.u2localVariableTableLength);
  }

  /**
   * Test {@link ParameterNameMarker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <p>Method under test: {@link ParameterNameMarker#visitLocalVariableTableAttribute(Clazz,
   * Method, CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterNameMarker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute2() {
    // Arrange
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
    CallResolver attributeUsageMarker =
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
    ParameterNameMarker parameterNameMarker = new ParameterNameMarker(attributeUsageMarker);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod(1, "Name", "Descriptor");
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo[] localVariableTable = new LocalVariableInfo[] {new LocalVariableInfo()};
    LocalVariableTableAttribute localVariableTableAttribute =
        new LocalVariableTableAttribute(1, 1, localVariableTable);

    // Act
    parameterNameMarker.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert that nothing has changed
    assertEquals(1, localVariableTableAttribute.u2localVariableTableLength);
  }

  /**
   * Test {@link ParameterNameMarker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <p>Method under test: {@link ParameterNameMarker#visitLocalVariableTableAttribute(Clazz,
   * Method, CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterNameMarker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute3() {
    // Arrange
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
    CallResolver attributeUsageMarker =
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
    ParameterNameMarker parameterNameMarker = new ParameterNameMarker(attributeUsageMarker);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod(1, "Name", "Descriptor");
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo localVariableInfo = new LocalVariableInfo(1, 3, 1, 1, 1);
    LocalVariableInfo[] localVariableTable = new LocalVariableInfo[] {localVariableInfo};
    LocalVariableTableAttribute localVariableTableAttribute =
        new LocalVariableTableAttribute(1, 1, localVariableTable);

    // Act
    parameterNameMarker.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert
    assertEquals(0, localVariableTableAttribute.u2localVariableTableLength);
  }

  /**
   * Test {@link ParameterNameMarker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <p>Method under test: {@link ParameterNameMarker#visitLocalVariableTableAttribute(Clazz,
   * Method, CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterNameMarker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute4() {
    // Arrange
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
    CallResolver attributeUsageMarker =
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
    ParameterNameMarker parameterNameMarker = new ParameterNameMarker(attributeUsageMarker);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod(1, "Name", "Descriptor");
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTableAttribute localVariableTableAttribute = new LocalVariableTableAttribute();

    // Act
    parameterNameMarker.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert that nothing has changed
    assertEquals(0, localVariableTableAttribute.u2localVariableTableLength);
  }

  /**
   * Test {@link ParameterNameMarker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <p>Method under test: {@link ParameterNameMarker#visitLocalVariableTableAttribute(Clazz,
   * Method, CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterNameMarker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute5() {
    // Arrange
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
    CallResolver attributeUsageMarker =
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
    ParameterNameMarker parameterNameMarker = new ParameterNameMarker(attributeUsageMarker);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod(1, "Name", "Descriptor");
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo[] localVariableTable = new LocalVariableInfo[] {new LocalVariableInfo()};
    LocalVariableTableAttribute localVariableTableAttribute =
        new LocalVariableTableAttribute(1, 1, localVariableTable);

    // Act
    parameterNameMarker.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert that nothing has changed
    assertEquals(1, localVariableTableAttribute.u2localVariableTableLength);
  }

  /**
   * Test {@link ParameterNameMarker#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <p>Method under test: {@link ParameterNameMarker#visitLocalVariableTableAttribute(Clazz,
   * Method, CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterNameMarker.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute6() {
    // Arrange
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
    CallResolver attributeUsageMarker =
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
    ParameterNameMarker parameterNameMarker = new ParameterNameMarker(attributeUsageMarker);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod(1, "Name", "Descriptor");
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableInfo localVariableInfo = new LocalVariableInfo(1, 3, 1, 1, 1);
    LocalVariableInfo[] localVariableTable = new LocalVariableInfo[] {localVariableInfo};
    LocalVariableTableAttribute localVariableTableAttribute =
        new LocalVariableTableAttribute(1, 1, localVariableTable);

    // Act
    parameterNameMarker.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert
    assertEquals(0, localVariableTableAttribute.u2localVariableTableLength);
  }

  /**
   * Test {@link ParameterNameMarker#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}.
   *
   * <p>Method under test: {@link ParameterNameMarker#visitLocalVariableTypeTableAttribute(Clazz,
   * Method, CodeAttribute, LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterNameMarker.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute() {
    // Arrange
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
    CallResolver attributeUsageMarker =
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
    ParameterNameMarker parameterNameMarker = new ParameterNameMarker(attributeUsageMarker);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod(1, "Name", "Descriptor");
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeTableAttribute localVariableTypeTableAttribute =
        new LocalVariableTypeTableAttribute();

    // Act
    parameterNameMarker.visitLocalVariableTypeTableAttribute(
        clazz, method, codeAttribute, localVariableTypeTableAttribute);

    // Assert that nothing has changed
    assertEquals(0, localVariableTypeTableAttribute.u2localVariableTypeTableLength);
  }

  /**
   * Test {@link ParameterNameMarker#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}.
   *
   * <p>Method under test: {@link ParameterNameMarker#visitLocalVariableTypeTableAttribute(Clazz,
   * Method, CodeAttribute, LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterNameMarker.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute2() {
    // Arrange
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
    CallResolver attributeUsageMarker =
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
    ParameterNameMarker parameterNameMarker = new ParameterNameMarker(attributeUsageMarker);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod(1, "Name", "Descriptor");
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeInfo[] localVariableTypeTable =
        new LocalVariableTypeInfo[] {new LocalVariableTypeInfo()};
    LocalVariableTypeTableAttribute localVariableTypeTableAttribute =
        new LocalVariableTypeTableAttribute(1, 1, localVariableTypeTable);

    // Act
    parameterNameMarker.visitLocalVariableTypeTableAttribute(
        clazz, method, codeAttribute, localVariableTypeTableAttribute);

    // Assert that nothing has changed
    assertEquals(1, localVariableTypeTableAttribute.u2localVariableTypeTableLength);
  }

  /**
   * Test {@link ParameterNameMarker#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}.
   *
   * <p>Method under test: {@link ParameterNameMarker#visitLocalVariableTypeTableAttribute(Clazz,
   * Method, CodeAttribute, LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void ParameterNameMarker.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute3() {
    // Arrange
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
    CallResolver attributeUsageMarker =
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
    ParameterNameMarker parameterNameMarker = new ParameterNameMarker(attributeUsageMarker);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod(1, "Name", "Descriptor");
    CodeAttribute codeAttribute = new CodeAttribute();
    LocalVariableTypeInfo localVariableTypeInfo = new LocalVariableTypeInfo(1, 3, 1, 1, 1);
    LocalVariableTypeInfo[] localVariableTypeTable =
        new LocalVariableTypeInfo[] {localVariableTypeInfo};
    LocalVariableTypeTableAttribute localVariableTypeTableAttribute =
        new LocalVariableTypeTableAttribute(1, 1, localVariableTypeTable);

    // Act
    parameterNameMarker.visitLocalVariableTypeTableAttribute(
        clazz, method, codeAttribute, localVariableTypeTableAttribute);

    // Assert
    assertEquals(0, localVariableTypeTableAttribute.u2localVariableTypeTableLength);
  }
}
