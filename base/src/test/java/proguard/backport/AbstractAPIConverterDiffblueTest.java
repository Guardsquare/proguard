package proguard.backport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import proguard.analysis.CallHandler;
import proguard.analysis.CallResolver;
import proguard.analysis.CallResolver.Builder;
import proguard.analysis.datastructure.callgraph.CallGraph;
import proguard.backport.AbstractAPIConverter.MethodReplacement;
import proguard.backport.AbstractAPIConverter.TypeReplacement;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Field;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.ProgramMethod;
import proguard.classfile.attribute.Attribute;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.attribute.LocalVariableInfo;
import proguard.classfile.attribute.LocalVariableTableAttribute;
import proguard.classfile.attribute.LocalVariableTypeInfo;
import proguard.classfile.attribute.LocalVariableTypeTableAttribute;
import proguard.classfile.attribute.SignatureAttribute;
import proguard.classfile.attribute.annotation.Annotation;
import proguard.classfile.attribute.annotation.AnnotationDefaultAttribute;
import proguard.classfile.attribute.annotation.AnnotationElementValue;
import proguard.classfile.attribute.annotation.AnnotationsAttribute;
import proguard.classfile.attribute.annotation.ArrayElementValue;
import proguard.classfile.attribute.annotation.ClassElementValue;
import proguard.classfile.attribute.annotation.ConstantElementValue;
import proguard.classfile.attribute.annotation.ElementValue;
import proguard.classfile.attribute.annotation.EnumConstantElementValue;
import proguard.classfile.attribute.annotation.ParameterAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeInvisibleAnnotationsAttribute;
import proguard.classfile.attribute.annotation.RuntimeInvisibleParameterAnnotationsAttribute;
import proguard.classfile.attribute.annotation.visitor.AnnotationVisitor;
import proguard.classfile.attribute.annotation.visitor.ElementValueVisitor;
import proguard.classfile.attribute.visitor.AttributeVisitor;
import proguard.classfile.attribute.visitor.LocalVariableInfoVisitor;
import proguard.classfile.attribute.visitor.LocalVariableTypeInfoVisitor;
import proguard.classfile.constant.AnyMethodrefConstant;
import proguard.classfile.constant.ClassConstant;
import proguard.classfile.constant.FieldrefConstant;
import proguard.classfile.constant.InterfaceMethodrefConstant;
import proguard.classfile.constant.visitor.ConstantVisitor;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.util.WarningPrinter;
import proguard.classfile.visitor.ClassVisitor;
import proguard.classfile.visitor.MemberVisitor;
import proguard.evaluation.ExecutingInvocationUnit;
import proguard.evaluation.ParticularReferenceValueFactory;
import proguard.testutils.cpa.NamedField;
import proguard.testutils.cpa.NamedMember;
import proguard.util.ConstantMatcher;
import proguard.util.EmptyStringMatcher;
import proguard.util.FixedStringMatcher;
import proguard.util.StringMatcher;
import proguard.util.VariableStringMatcher;

@ExtendWith(MockitoExtension.class)
class AbstractAPIConverterDiffblueTest {
  @InjectMocks private AbstractAPIConverter abstractAPIConverter;

  /**
   * Test MethodReplacement {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}.
   *
   * <p>Method under test: {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}
   */
  @Test
  @DisplayName("Test MethodReplacement matches(Clazz, AnyMethodrefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodReplacement.matches(Clazz, AnyMethodrefConstant)"})
  void testMethodReplacementMatches() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    MethodReplacement methodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "Class Name",
            "Method Name",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");
    when(clazz.getName(anyInt())).thenReturn("Name");
    when(clazz.getType(anyInt())).thenReturn("Type");
    LibraryClass referencedClass = new LibraryClass();

    // Act
    boolean actualMatchesResult =
        methodReplacement.matches(
            clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, new LibraryMethod()));

    // Assert
    verify(clazz).getClassName(1);
    verify(clazz).getName(1);
    verify(clazz).getType(1);
    assertFalse(actualMatchesResult);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}.
   *
   * <p>Method under test: {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}
   */
  @Test
  @DisplayName("Test MethodReplacement matches(Clazz, AnyMethodrefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodReplacement.matches(Clazz, AnyMethodrefConstant)"})
  void testMethodReplacementMatches2() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    MethodReplacement methodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "Class Name",
            "Method Name",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");
    when(clazz.getName(anyInt())).thenReturn("Name");
    when(clazz.getType(anyInt())).thenReturn("Type");
    InterfaceMethodrefConstant anyMethodrefConstant =
        new InterfaceMethodrefConstant(1, 1, new LibraryClass(), null);

    // Act
    boolean actualMatchesResult = methodReplacement.matches(clazz, anyMethodrefConstant);

    // Assert
    verify(clazz).getClassName(1);
    verify(clazz).getName(1);
    verify(clazz).getType(1);
    assertFalse(actualMatchesResult);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}.
   *
   * <p>Method under test: {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}
   */
  @Test
  @DisplayName("Test MethodReplacement matches(Clazz, AnyMethodrefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodReplacement.matches(Clazz, AnyMethodrefConstant)"})
  void testMethodReplacementMatches3() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    MethodReplacement methodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "<default>",
            "Method Name",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");
    when(clazz.getName(anyInt())).thenReturn("Name");
    when(clazz.getType(anyInt())).thenReturn("Type");
    LibraryClass referencedClass = new LibraryClass();

    // Act
    boolean actualMatchesResult =
        methodReplacement.matches(
            clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, new LibraryMethod()));

    // Assert
    verify(clazz).getClassName(1);
    verify(clazz).getName(1);
    verify(clazz).getType(1);
    assertFalse(actualMatchesResult);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}.
   *
   * <p>Method under test: {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}
   */
  @Test
  @DisplayName("Test MethodReplacement matches(Clazz, AnyMethodrefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodReplacement.matches(Clazz, AnyMethodrefConstant)"})
  void testMethodReplacementMatches4() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    MethodReplacement methodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "Class Name",
            "<default>",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");
    when(clazz.getName(anyInt())).thenReturn("Name");
    when(clazz.getType(anyInt())).thenReturn("Type");
    LibraryClass referencedClass = new LibraryClass();

    // Act
    boolean actualMatchesResult =
        methodReplacement.matches(
            clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, new LibraryMethod()));

    // Assert
    verify(clazz).getClassName(1);
    verify(clazz).getName(1);
    verify(clazz).getType(1);
    assertFalse(actualMatchesResult);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}.
   *
   * <p>Method under test: {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}
   */
  @Test
  @DisplayName("Test MethodReplacement matches(Clazz, AnyMethodrefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodReplacement.matches(Clazz, AnyMethodrefConstant)"})
  void testMethodReplacementMatches5() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    MethodReplacement methodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "Class Name",
            "<static>",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");
    when(clazz.getName(anyInt())).thenReturn("Name");
    when(clazz.getType(anyInt())).thenReturn("Type");
    LibraryClass referencedClass = new LibraryClass();

    // Act
    boolean actualMatchesResult =
        methodReplacement.matches(
            clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, new LibraryMethod()));

    // Assert
    verify(clazz).getClassName(1);
    verify(clazz).getName(1);
    verify(clazz).getType(1);
    assertFalse(actualMatchesResult);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}.
   *
   * <p>Method under test: {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}
   */
  @Test
  @DisplayName("Test MethodReplacement matches(Clazz, AnyMethodrefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodReplacement.matches(Clazz, AnyMethodrefConstant)"})
  void testMethodReplacementMatches6() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    MethodReplacement methodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "Class Name",
            "*",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");
    when(clazz.getName(anyInt())).thenReturn("Name");
    when(clazz.getType(anyInt())).thenReturn("Type");
    LibraryClass referencedClass = new LibraryClass();

    // Act
    boolean actualMatchesResult =
        methodReplacement.matches(
            clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, new LibraryMethod()));

    // Assert
    verify(clazz).getClassName(1);
    verify(clazz).getName(1);
    verify(clazz).getType(1);
    StringMatcher stringMatcher = methodReplacement.methodNameMatcher;
    assertTrue(stringMatcher instanceof VariableStringMatcher);
    assertEquals("Name", ((VariableStringMatcher) stringMatcher).getMatchingString());
    assertFalse(actualMatchesResult);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}.
   *
   * <p>Method under test: {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}
   */
  @Test
  @DisplayName("Test MethodReplacement matches(Clazz, AnyMethodrefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodReplacement.matches(Clazz, AnyMethodrefConstant)"})
  void testMethodReplacementMatches7() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    MethodReplacement methodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "<default>",
            "Method Name",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");
    when(clazz.getName(anyInt())).thenReturn("Name");
    when(clazz.getType(anyInt())).thenReturn("Type");
    ProgramClass referencedClass = new ProgramClass();

    // Act
    boolean actualMatchesResult =
        methodReplacement.matches(
            clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, new LibraryMethod()));

    // Assert
    verify(clazz).getClassName(1);
    verify(clazz).getName(1);
    verify(clazz).getType(1);
    assertFalse(actualMatchesResult);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@code true}.
   *   <li>Then calls {@link LibraryClass#extendsOrImplements(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test MethodReplacement matches(Clazz, AnyMethodrefConstant); given 'true'; then calls extendsOrImplements(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodReplacement.matches(Clazz, AnyMethodrefConstant)"})
  void testMethodReplacementMatches_givenTrue_thenCallsExtendsOrImplements() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    MethodReplacement methodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "<default>",
            "Method Name",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");
    when(clazz.getName(anyInt())).thenReturn("Name");
    when(clazz.getType(anyInt())).thenReturn("Type");

    LibraryClass referencedClass = mock(LibraryClass.class);
    when(referencedClass.extendsOrImplements(Mockito.<Clazz>any())).thenReturn(true);

    // Act
    boolean actualMatchesResult =
        methodReplacement.matches(
            clazz, new InterfaceMethodrefConstant(1, 1, referencedClass, new LibraryMethod()));

    // Assert
    verify(referencedClass).extendsOrImplements((Clazz) isNull());
    verify(clazz).getClassName(1);
    verify(clazz).getName(1);
    verify(clazz).getType(1);
    assertFalse(actualMatchesResult);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>When {@link InterfaceMethodrefConstant#InterfaceMethodrefConstant()}.
   * </ul>
   *
   * <p>Method under test: {@link MethodReplacement#matches(Clazz, AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test MethodReplacement matches(Clazz, AnyMethodrefConstant); when InterfaceMethodrefConstant()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean MethodReplacement.matches(Clazz, AnyMethodrefConstant)"})
  void testMethodReplacementMatches_whenInterfaceMethodrefConstant() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    MethodReplacement methodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "Class Name",
            "Method Name",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");
    when(clazz.getName(anyInt())).thenReturn("Name");
    when(clazz.getType(anyInt())).thenReturn("Type");

    // Act
    boolean actualMatchesResult =
        methodReplacement.matches(clazz, new InterfaceMethodrefConstant());

    // Assert
    verify(clazz).getClassName(0);
    verify(clazz).getName(0);
    verify(clazz).getType(0);
    assertFalse(actualMatchesResult);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#MethodReplacement(AbstractAPIConverter, String,
   * String, String, String, String, String)}.
   *
   * <p>Method under test: {@link MethodReplacement#MethodReplacement(AbstractAPIConverter, String,
   * String, String, String, String, String)}
   */
  @Test
  @DisplayName(
      "Test MethodReplacement new MethodReplacement(AbstractAPIConverter, String, String, String, String, String, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MethodReplacement.<init>(AbstractAPIConverter, String, String, String, String, String, String)"
  })
  void testMethodReplacementNewMethodReplacement() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualMethodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "Class Name",
            "Method Name",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    // Assert
    assertTrue(actualMethodReplacement.classNameMatcher instanceof FixedStringMatcher);
    assertTrue(actualMethodReplacement.descMatcher instanceof FixedStringMatcher);
    assertTrue(actualMethodReplacement.methodNameMatcher instanceof FixedStringMatcher);
    assertEquals("Class Name", actualMethodReplacement.matchingClassName);
    assertEquals("Method Desc", actualMethodReplacement.matchingMethodDesc);
    assertEquals("Method Name", actualMethodReplacement.matchingMethodName);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#MethodReplacement(AbstractAPIConverter, String,
   * String, String, String, String, String)}.
   *
   * <p>Method under test: {@link MethodReplacement#MethodReplacement(AbstractAPIConverter, String,
   * String, String, String, String, String)}
   */
  @Test
  @DisplayName(
      "Test MethodReplacement new MethodReplacement(AbstractAPIConverter, String, String, String, String, String, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MethodReplacement.<init>(AbstractAPIConverter, String, String, String, String, String, String)"
  })
  void testMethodReplacementNewMethodReplacement2() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualMethodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "**",
            "Method Name",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    // Assert
    assertTrue(actualMethodReplacement.descMatcher instanceof FixedStringMatcher);
    assertTrue(actualMethodReplacement.methodNameMatcher instanceof FixedStringMatcher);
    assertTrue(actualMethodReplacement.classNameMatcher instanceof VariableStringMatcher);
    assertEquals("**", actualMethodReplacement.matchingClassName);
    assertEquals("Method Desc", actualMethodReplacement.matchingMethodDesc);
    assertEquals("Method Name", actualMethodReplacement.matchingMethodName);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#MethodReplacement(AbstractAPIConverter, String,
   * String, String, String, String, String)}.
   *
   * <p>Method under test: {@link MethodReplacement#MethodReplacement(AbstractAPIConverter, String,
   * String, String, String, String, String)}
   */
  @Test
  @DisplayName(
      "Test MethodReplacement new MethodReplacement(AbstractAPIConverter, String, String, String, String, String, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MethodReplacement.<init>(AbstractAPIConverter, String, String, String, String, String, String)"
  })
  void testMethodReplacementNewMethodReplacement3() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualMethodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "Class Name",
            "**",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    // Assert
    assertTrue(actualMethodReplacement.classNameMatcher instanceof FixedStringMatcher);
    assertTrue(actualMethodReplacement.methodNameMatcher instanceof VariableStringMatcher);
    assertEquals("**", actualMethodReplacement.matchingMethodName);
    assertEquals("Class Name", actualMethodReplacement.matchingClassName);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#MethodReplacement(AbstractAPIConverter, String,
   * String, String, String, String, String)}.
   *
   * <ul>
   *   <li>Then {@link MethodReplacement#descMatcher} return {@link ConstantMatcher}.
   * </ul>
   *
   * <p>Method under test: {@link MethodReplacement#MethodReplacement(AbstractAPIConverter, String,
   * String, String, String, String, String)}
   */
  @Test
  @DisplayName(
      "Test MethodReplacement new MethodReplacement(AbstractAPIConverter, String, String, String, String, String, String); then descMatcher return ConstantMatcher")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MethodReplacement.<init>(AbstractAPIConverter, String, String, String, String, String, String)"
  })
  void testMethodReplacementNewMethodReplacement_thenDescMatcherReturnConstantMatcher() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualMethodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "Class Name",
            "Method Name",
            "**",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    // Assert
    assertTrue(actualMethodReplacement.descMatcher instanceof ConstantMatcher);
    assertTrue(actualMethodReplacement.classNameMatcher instanceof FixedStringMatcher);
    assertEquals("**", actualMethodReplacement.matchingMethodDesc);
    assertEquals("Class Name", actualMethodReplacement.matchingClassName);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#MethodReplacement(AbstractAPIConverter, String,
   * String, String, String, String, String)}.
   *
   * <ul>
   *   <li>Then return {@link MethodReplacement#matchingClassName} is {@code *}.
   * </ul>
   *
   * <p>Method under test: {@link MethodReplacement#MethodReplacement(AbstractAPIConverter, String,
   * String, String, String, String, String)}
   */
  @Test
  @DisplayName(
      "Test MethodReplacement new MethodReplacement(AbstractAPIConverter, String, String, String, String, String, String); then return matchingClassName is '*'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MethodReplacement.<init>(AbstractAPIConverter, String, String, String, String, String, String)"
  })
  void testMethodReplacementNewMethodReplacement_thenReturnMatchingClassNameIsAsterisk() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualMethodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "*",
            "Method Name",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    // Assert
    assertTrue(actualMethodReplacement.descMatcher instanceof FixedStringMatcher);
    assertTrue(actualMethodReplacement.methodNameMatcher instanceof FixedStringMatcher);
    assertTrue(actualMethodReplacement.classNameMatcher instanceof VariableStringMatcher);
    assertEquals("*", actualMethodReplacement.matchingClassName);
    assertEquals("Method Desc", actualMethodReplacement.matchingMethodDesc);
    assertEquals("Method Name", actualMethodReplacement.matchingMethodName);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#replaceInstruction(int, Clazz, Method,
   * AnyMethodrefConstant)}.
   *
   * <p>Method under test: {@link MethodReplacement#replaceInstruction(int, Clazz, Method,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test MethodReplacement replaceInstruction(int, Clazz, Method, AnyMethodrefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MethodReplacement.replaceInstruction(int, Clazz, Method, AnyMethodrefConstant)"
  })
  void testMethodReplacementReplaceInstruction() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    JSR310Converter jsr310Converter =
        new JSR310Converter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    MethodReplacement methodReplacement =
        jsr310Converter
        .new MethodReplacement(
            "Class Name",
            "<default>",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");
    when(clazz.getName(anyInt())).thenReturn("Name");
    when(clazz.getType(anyInt())).thenReturn("Type");
    LibraryMethod method = new LibraryMethod();

    // Act
    methodReplacement.replaceInstruction(2, clazz, method, new InterfaceMethodrefConstant());

    // Assert
    verify(clazz).getClassName(0);
    verify(clazz).getName(0);
    verify(clazz).getType(0);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#replaceInstruction(int, Clazz, Method,
   * AnyMethodrefConstant)}.
   *
   * <p>Method under test: {@link MethodReplacement#replaceInstruction(int, Clazz, Method,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test MethodReplacement replaceInstruction(int, Clazz, Method, AnyMethodrefConstant)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MethodReplacement.replaceInstruction(int, Clazz, Method, AnyMethodrefConstant)"
  })
  void testMethodReplacementReplaceInstruction2() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    JSR310Converter jsr310Converter =
        new JSR310Converter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    MethodReplacement methodReplacement =
        jsr310Converter
        .new MethodReplacement(
            "<1>",
            "<default>",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");
    when(clazz.getName(anyInt())).thenReturn("Name");
    when(clazz.getType(anyInt())).thenReturn("Type");
    LibraryMethod method = new LibraryMethod();

    // Act
    methodReplacement.replaceInstruction(2, clazz, method, new InterfaceMethodrefConstant());

    // Assert
    verify(clazz).getClassName(0);
    verify(clazz).getName(0);
    verify(clazz).getType(0);
  }

  /**
   * Test MethodReplacement {@link MethodReplacement#replaceInstruction(int, Clazz, Method,
   * AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryClass#getClassName(int)}.
   * </ul>
   *
   * <p>Method under test: {@link MethodReplacement#replaceInstruction(int, Clazz, Method,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test MethodReplacement replaceInstruction(int, Clazz, Method, AnyMethodrefConstant); then calls getClassName(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void MethodReplacement.replaceInstruction(int, Clazz, Method, AnyMethodrefConstant)"
  })
  void testMethodReplacementReplaceInstruction_thenCallsGetClassName() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    MethodReplacement methodReplacement =
        abstractAPIConverter
        .new MethodReplacement(
            "Class Name",
            "Method Name",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getClassName(anyInt())).thenReturn("Class Name");
    when(clazz.getName(anyInt())).thenReturn("Name");
    when(clazz.getType(anyInt())).thenReturn("Type");
    LibraryMethod method = new LibraryMethod();

    // Act
    methodReplacement.replaceInstruction(2, clazz, method, new InterfaceMethodrefConstant());

    // Assert
    verify(clazz).getClassName(0);
    verify(clazz).getName(0);
    verify(clazz).getType(0);
  }

  /**
   * Test {@link AbstractAPIConverter#AbstractAPIConverter(ClassPool, ClassPool, WarningPrinter,
   * ClassVisitor, InstructionVisitor)}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#AbstractAPIConverter(ClassPool, ClassPool,
   * WarningPrinter, ClassVisitor, InstructionVisitor)}
   */
  @Test
  @DisplayName(
      "Test new AbstractAPIConverter(ClassPool, ClassPool, WarningPrinter, ClassVisitor, InstructionVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.<init>(ClassPool, ClassPool, WarningPrinter, ClassVisitor, InstructionVisitor)"
  })
  void testNewAbstractAPIConverter() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

    // Act
    AbstractAPIConverter actualAbstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            new CodeAttributeEditor());

    // Assert
    TypeReplacement missingResult = actualAbstractAPIConverter.missing("Class Name");
    assertTrue(missingResult.classNameMatcher instanceof FixedStringMatcher);
    assertEquals("Class Name", missingResult.matchingClassName);
    assertNull(missingResult.replacementClassName);
  }

  /**
   * Test {@link AbstractAPIConverter#replace(String, String, String, String, String, String)} with
   * {@code className}, {@code methodName}, {@code methodDesc}, {@code replacementClassName}, {@code
   * replacementMethodName}, {@code replacementMethodDesc}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#replace(String, String, String, String,
   * String, String)}
   */
  @Test
  @DisplayName(
      "Test replace(String, String, String, String, String, String) with 'className', 'methodName', 'methodDesc', 'replacementClassName', 'replacementMethodName', 'replacementMethodDesc'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "MethodReplacement AbstractAPIConverter.replace(String, String, String, String, String, String)"
  })
  void
      testReplaceWithClassNameMethodNameMethodDescReplacementClassNameReplacementMethodNameReplacementMethodDesc() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualReplaceResult =
        abstractAPIConverter.replace(
            "Class Name",
            "Method Name",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    // Assert
    assertTrue(actualReplaceResult.classNameMatcher instanceof FixedStringMatcher);
    assertEquals("Class Name", actualReplaceResult.matchingClassName);
    assertNull(actualReplaceResult.replacementClassName);
    assertNull(actualReplaceResult.replacementMethodDesc);
    assertNull(actualReplaceResult.replacementMethodName);
  }

  /**
   * Test {@link AbstractAPIConverter#replace(String, String, String, String, String, String)} with
   * {@code className}, {@code methodName}, {@code methodDesc}, {@code replacementClassName}, {@code
   * replacementMethodName}, {@code replacementMethodDesc}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#replace(String, String, String, String,
   * String, String)}
   */
  @Test
  @DisplayName(
      "Test replace(String, String, String, String, String, String) with 'className', 'methodName', 'methodDesc', 'replacementClassName', 'replacementMethodName', 'replacementMethodDesc'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "MethodReplacement AbstractAPIConverter.replace(String, String, String, String, String, String)"
  })
  void
      testReplaceWithClassNameMethodNameMethodDescReplacementClassNameReplacementMethodNameReplacementMethodDesc2() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualReplaceResult =
        abstractAPIConverter.replace(
            "**",
            "Method Name",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    // Assert
    assertTrue(actualReplaceResult.classNameMatcher instanceof VariableStringMatcher);
    assertEquals("**", actualReplaceResult.matchingClassName);
    assertNull(actualReplaceResult.replacementClassName);
    assertNull(actualReplaceResult.replacementMethodDesc);
    assertNull(actualReplaceResult.replacementMethodName);
  }

  /**
   * Test {@link AbstractAPIConverter#replace(String, String, String, String, String, String)} with
   * {@code className}, {@code methodName}, {@code methodDesc}, {@code replacementClassName}, {@code
   * replacementMethodName}, {@code replacementMethodDesc}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#replace(String, String, String, String,
   * String, String)}
   */
  @Test
  @DisplayName(
      "Test replace(String, String, String, String, String, String) with 'className', 'methodName', 'methodDesc', 'replacementClassName', 'replacementMethodName', 'replacementMethodDesc'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "MethodReplacement AbstractAPIConverter.replace(String, String, String, String, String, String)"
  })
  void
      testReplaceWithClassNameMethodNameMethodDescReplacementClassNameReplacementMethodNameReplacementMethodDesc3() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualReplaceResult =
        abstractAPIConverter.replace(
            "*",
            "Method Name",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    // Assert
    assertTrue(actualReplaceResult.classNameMatcher instanceof VariableStringMatcher);
    assertEquals("*", actualReplaceResult.matchingClassName);
    assertNull(actualReplaceResult.replacementClassName);
    assertNull(actualReplaceResult.replacementMethodDesc);
    assertNull(actualReplaceResult.replacementMethodName);
  }

  /**
   * Test {@link AbstractAPIConverter#replace(String, String, String, String, String, String)} with
   * {@code className}, {@code methodName}, {@code methodDesc}, {@code replacementClassName}, {@code
   * replacementMethodName}, {@code replacementMethodDesc}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#replace(String, String, String, String,
   * String, String)}
   */
  @Test
  @DisplayName(
      "Test replace(String, String, String, String, String, String) with 'className', 'methodName', 'methodDesc', 'replacementClassName', 'replacementMethodName', 'replacementMethodDesc'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "MethodReplacement AbstractAPIConverter.replace(String, String, String, String, String, String)"
  })
  void
      testReplaceWithClassNameMethodNameMethodDescReplacementClassNameReplacementMethodNameReplacementMethodDesc4() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualReplaceResult =
        abstractAPIConverter.replace(
            "Class Name",
            "**",
            "Method Desc",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    // Assert
    assertTrue(actualReplaceResult.methodNameMatcher instanceof VariableStringMatcher);
    assertEquals("**", actualReplaceResult.matchingMethodName);
    assertNull(actualReplaceResult.replacementClassName);
    assertNull(actualReplaceResult.replacementMethodDesc);
    assertNull(actualReplaceResult.replacementMethodName);
  }

  /**
   * Test {@link AbstractAPIConverter#replace(String, String, String, String, String, String)} with
   * {@code className}, {@code methodName}, {@code methodDesc}, {@code replacementClassName}, {@code
   * replacementMethodName}, {@code replacementMethodDesc}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#replace(String, String, String, String,
   * String, String)}
   */
  @Test
  @DisplayName(
      "Test replace(String, String, String, String, String, String) with 'className', 'methodName', 'methodDesc', 'replacementClassName', 'replacementMethodName', 'replacementMethodDesc'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "MethodReplacement AbstractAPIConverter.replace(String, String, String, String, String, String)"
  })
  void
      testReplaceWithClassNameMethodNameMethodDescReplacementClassNameReplacementMethodNameReplacementMethodDesc5() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualReplaceResult =
        abstractAPIConverter.replace(
            "Class Name",
            "Method Name",
            "**",
            "Replacement Class Name",
            "Replacement Method Name",
            "Replacement Method Desc");

    // Assert
    assertTrue(actualReplaceResult.descMatcher instanceof ConstantMatcher);
    assertEquals("**", actualReplaceResult.matchingMethodDesc);
    assertNull(actualReplaceResult.replacementClassName);
    assertNull(actualReplaceResult.replacementMethodDesc);
    assertNull(actualReplaceResult.replacementMethodName);
  }

  /**
   * Test {@link AbstractAPIConverter#replace(String, String, String, String, String, String)} with
   * {@code className}, {@code methodName}, {@code methodDesc}, {@code replacementClassName}, {@code
   * replacementMethodName}, {@code replacementMethodDesc}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#replace(String, String, String, String,
   * String, String)}
   */
  @Test
  @DisplayName(
      "Test replace(String, String, String, String, String, String) with 'className', 'methodName', 'methodDesc', 'replacementClassName', 'replacementMethodName', 'replacementMethodDesc'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "MethodReplacement AbstractAPIConverter.replace(String, String, String, String, String, String)"
  })
  void
      testReplaceWithClassNameMethodNameMethodDescReplacementClassNameReplacementMethodNameReplacementMethodDesc6() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualReplaceResult =
        abstractAPIConverter.replace(
            "Class Name",
            "Method Name",
            "Method Desc",
            "**",
            "Replacement Method Name",
            "Replacement Method Desc");

    // Assert
    assertTrue(actualReplaceResult.classNameMatcher instanceof FixedStringMatcher);
    assertEquals("**", actualReplaceResult.replacementClassName);
    assertEquals("Class Name", actualReplaceResult.matchingClassName);
    assertEquals("Replacement Method Desc", actualReplaceResult.replacementMethodDesc);
    assertEquals("Replacement Method Name", actualReplaceResult.replacementMethodName);
  }

  /**
   * Test {@link AbstractAPIConverter#replace(String, String, String, String, String, String)} with
   * {@code className}, {@code methodName}, {@code methodDesc}, {@code replacementClassName}, {@code
   * replacementMethodName}, {@code replacementMethodDesc}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#replace(String, String, String, String,
   * String, String)}
   */
  @Test
  @DisplayName(
      "Test replace(String, String, String, String, String, String) with 'className', 'methodName', 'methodDesc', 'replacementClassName', 'replacementMethodName', 'replacementMethodDesc'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "MethodReplacement AbstractAPIConverter.replace(String, String, String, String, String, String)"
  })
  void
      testReplaceWithClassNameMethodNameMethodDescReplacementClassNameReplacementMethodNameReplacementMethodDesc7() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualReplaceResult =
        abstractAPIConverter.replace(
            "Class Name",
            "Method Name",
            "Method Desc",
            "<1>",
            "Replacement Method Name",
            "Replacement Method Desc");

    // Assert
    assertTrue(actualReplaceResult.classNameMatcher instanceof FixedStringMatcher);
    assertEquals("<1>", actualReplaceResult.replacementClassName);
    assertEquals("Class Name", actualReplaceResult.matchingClassName);
    assertEquals("Replacement Method Desc", actualReplaceResult.replacementMethodDesc);
    assertEquals("Replacement Method Name", actualReplaceResult.replacementMethodName);
  }

  /**
   * Test {@link AbstractAPIConverter#replace(String, String)} with {@code className}, {@code
   * replacementClassName}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#replace(String, String)}
   */
  @Test
  @DisplayName("Test replace(String, String) with 'className', 'replacementClassName'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TypeReplacement AbstractAPIConverter.replace(String, String)"})
  void testReplaceWithClassNameReplacementClassName() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    TypeReplacement actualReplaceResult =
        abstractAPIConverter.replace("Class Name", "Replacement Class Name");

    // Assert
    assertTrue(actualReplaceResult.classNameMatcher instanceof FixedStringMatcher);
    assertEquals("Class Name", actualReplaceResult.matchingClassName);
    assertNull(actualReplaceResult.replacementClassName);
  }

  /**
   * Test {@link AbstractAPIConverter#replace(String, String)} with {@code className}, {@code
   * replacementClassName}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#replace(String, String)}
   */
  @Test
  @DisplayName("Test replace(String, String) with 'className', 'replacementClassName'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TypeReplacement AbstractAPIConverter.replace(String, String)"})
  void testReplaceWithClassNameReplacementClassName2() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    TypeReplacement actualReplaceResult =
        abstractAPIConverter.replace("*", "Replacement Class Name");

    // Assert
    assertTrue(actualReplaceResult.classNameMatcher instanceof VariableStringMatcher);
    assertEquals("*", actualReplaceResult.matchingClassName);
    assertNull(actualReplaceResult.replacementClassName);
  }

  /**
   * Test {@link AbstractAPIConverter#replace(String, String)} with {@code className}, {@code
   * replacementClassName}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#replace(String, String)}
   */
  @Test
  @DisplayName("Test replace(String, String) with 'className', 'replacementClassName'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TypeReplacement AbstractAPIConverter.replace(String, String)"})
  void testReplaceWithClassNameReplacementClassName3() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    TypeReplacement actualReplaceResult = abstractAPIConverter.replace("Class Name", "*");

    // Assert
    assertTrue(actualReplaceResult.classNameMatcher instanceof FixedStringMatcher);
    assertEquals("*", actualReplaceResult.replacementClassName);
    assertEquals("Class Name", actualReplaceResult.matchingClassName);
  }

  /**
   * Test {@link AbstractAPIConverter#replace(String, String)} with {@code className}, {@code
   * replacementClassName}.
   *
   * <ul>
   *   <li>Then return {@link TypeReplacement#replacementClassName} is {@code <1>}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#replace(String, String)}
   */
  @Test
  @DisplayName(
      "Test replace(String, String) with 'className', 'replacementClassName'; then return replacementClassName is '<1>'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TypeReplacement AbstractAPIConverter.replace(String, String)"})
  void testReplaceWithClassNameReplacementClassName_thenReturnReplacementClassNameIs1() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    TypeReplacement actualReplaceResult = abstractAPIConverter.replace("Class Name", "<1>");

    // Assert
    assertTrue(actualReplaceResult.classNameMatcher instanceof FixedStringMatcher);
    assertEquals("<1>", actualReplaceResult.replacementClassName);
    assertEquals("Class Name", actualReplaceResult.matchingClassName);
  }

  /**
   * Test {@link AbstractAPIConverter#missing(String, String, String)} with {@code className},
   * {@code methodName}, {@code methodDesc}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#missing(String, String, String)}
   */
  @Test
  @DisplayName("Test missing(String, String, String) with 'className', 'methodName', 'methodDesc'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"MethodReplacement AbstractAPIConverter.missing(String, String, String)"})
  void testMissingWithClassNameMethodNameMethodDesc() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualMissingResult =
        abstractAPIConverter.missing("Class Name", "Method Name", "Method Desc");

    // Assert
    assertTrue(actualMissingResult.classNameMatcher instanceof FixedStringMatcher);
    assertTrue(actualMissingResult.descMatcher instanceof FixedStringMatcher);
    assertTrue(actualMissingResult.methodNameMatcher instanceof FixedStringMatcher);
    assertEquals("Class Name", actualMissingResult.matchingClassName);
    assertEquals("Method Desc", actualMissingResult.matchingMethodDesc);
    assertEquals("Method Name", actualMissingResult.matchingMethodName);
  }

  /**
   * Test {@link AbstractAPIConverter#missing(String, String, String)} with {@code className},
   * {@code methodName}, {@code methodDesc}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#missing(String, String, String)}
   */
  @Test
  @DisplayName("Test missing(String, String, String) with 'className', 'methodName', 'methodDesc'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"MethodReplacement AbstractAPIConverter.missing(String, String, String)"})
  void testMissingWithClassNameMethodNameMethodDesc2() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualMissingResult =
        abstractAPIConverter.missing("**", "Method Name", "Method Desc");

    // Assert
    assertTrue(actualMissingResult.descMatcher instanceof FixedStringMatcher);
    assertTrue(actualMissingResult.methodNameMatcher instanceof FixedStringMatcher);
    assertTrue(actualMissingResult.classNameMatcher instanceof VariableStringMatcher);
    assertEquals("**", actualMissingResult.matchingClassName);
    assertEquals("Method Desc", actualMissingResult.matchingMethodDesc);
    assertEquals("Method Name", actualMissingResult.matchingMethodName);
  }

  /**
   * Test {@link AbstractAPIConverter#missing(String, String, String)} with {@code className},
   * {@code methodName}, {@code methodDesc}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#missing(String, String, String)}
   */
  @Test
  @DisplayName("Test missing(String, String, String) with 'className', 'methodName', 'methodDesc'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"MethodReplacement AbstractAPIConverter.missing(String, String, String)"})
  void testMissingWithClassNameMethodNameMethodDesc3() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualMissingResult =
        abstractAPIConverter.missing("Class Name", "**", "Method Desc");

    // Assert
    assertTrue(actualMissingResult.classNameMatcher instanceof FixedStringMatcher);
    assertTrue(actualMissingResult.descMatcher instanceof FixedStringMatcher);
    assertTrue(actualMissingResult.methodNameMatcher instanceof VariableStringMatcher);
    assertEquals("**", actualMissingResult.matchingMethodName);
    assertEquals("Class Name", actualMissingResult.matchingClassName);
    assertEquals("Method Desc", actualMissingResult.matchingMethodDesc);
  }

  /**
   * Test {@link AbstractAPIConverter#missing(String, String, String)} with {@code className},
   * {@code methodName}, {@code methodDesc}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#missing(String, String, String)}
   */
  @Test
  @DisplayName("Test missing(String, String, String) with 'className', 'methodName', 'methodDesc'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"MethodReplacement AbstractAPIConverter.missing(String, String, String)"})
  void testMissingWithClassNameMethodNameMethodDesc4() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    MethodReplacement actualMissingResult =
        abstractAPIConverter.missing("Class Name", "Method Name", "**");

    // Assert
    assertTrue(actualMissingResult.descMatcher instanceof ConstantMatcher);
    assertTrue(actualMissingResult.classNameMatcher instanceof FixedStringMatcher);
    assertTrue(actualMissingResult.methodNameMatcher instanceof FixedStringMatcher);
    assertEquals("**", actualMissingResult.matchingMethodDesc);
    assertEquals("Class Name", actualMissingResult.matchingClassName);
    assertEquals("Method Name", actualMissingResult.matchingMethodName);
  }

  /**
   * Test {@link AbstractAPIConverter#missing(String)} with {@code className}.
   *
   * <ul>
   *   <li>Then {@link TypeReplacement#classNameMatcher} return {@link EmptyStringMatcher}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#missing(String)}
   */
  @Test
  @DisplayName(
      "Test missing(String) with 'className'; then classNameMatcher return EmptyStringMatcher")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TypeReplacement AbstractAPIConverter.missing(String)"})
  void testMissingWithClassName_thenClassNameMatcherReturnEmptyStringMatcher() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    TypeReplacement actualMissingResult = abstractAPIConverter.missing("");

    // Assert
    assertTrue(actualMissingResult.classNameMatcher instanceof EmptyStringMatcher);
    assertEquals("", actualMissingResult.matchingClassName);
    assertNull(actualMissingResult.replacementClassName);
  }

  /**
   * Test {@link AbstractAPIConverter#missing(String)} with {@code className}.
   *
   * <ul>
   *   <li>Then {@link TypeReplacement#classNameMatcher} return {@link FixedStringMatcher}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#missing(String)}
   */
  @Test
  @DisplayName(
      "Test missing(String) with 'className'; then classNameMatcher return FixedStringMatcher")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"TypeReplacement AbstractAPIConverter.missing(String)"})
  void testMissingWithClassName_thenClassNameMatcherReturnFixedStringMatcher() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    TypeReplacement actualMissingResult = abstractAPIConverter.missing("Class Name");

    // Assert
    assertTrue(actualMissingResult.classNameMatcher instanceof FixedStringMatcher);
    assertEquals("Class Name", actualMissingResult.matchingClassName);
    assertNull(actualMissingResult.replacementClassName);
  }

  /**
   * Test TypeReplacement {@link TypeReplacement#isValid()}.
   *
   * <p>Method under test: {@link TypeReplacement#isValid()}
   */
  @Test
  @DisplayName("Test TypeReplacement isValid()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean TypeReplacement.isValid()"})
  void testTypeReplacementIsValid() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act and Assert
    assertTrue(abstractAPIConverter.new TypeReplacement("Matching Class Name", "*").isValid());
  }

  /**
   * Test TypeReplacement {@link TypeReplacement#isValid()}.
   *
   * <p>Method under test: {@link TypeReplacement#isValid()}
   */
  @Test
  @DisplayName("Test TypeReplacement isValid()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean TypeReplacement.isValid()"})
  void testTypeReplacementIsValid2() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act and Assert
    assertTrue(abstractAPIConverter.new TypeReplacement("Matching Class Name", "<1>").isValid());
  }

  /**
   * Test TypeReplacement {@link TypeReplacement#isValid()}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link TypeReplacement#isValid()}
   */
  @Test
  @DisplayName("Test TypeReplacement isValid(); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean TypeReplacement.isValid()"})
  void testTypeReplacementIsValid_thenReturnFalse() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act and Assert
    assertFalse(
        abstractAPIConverter.new TypeReplacement("Matching Class Name", "Replacement Class Name")
            .isValid());
  }

  /**
   * Test TypeReplacement {@link TypeReplacement#matchesClassName(String)}.
   *
   * <p>Method under test: {@link TypeReplacement#matchesClassName(String)}
   */
  @Test
  @DisplayName("Test TypeReplacement matchesClassName(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean TypeReplacement.matchesClassName(String)"})
  void testTypeReplacementMatchesClassName() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    TypeReplacement typeReplacement =
        abstractAPIConverter.new TypeReplacement("*", "Replacement Class Name");

    // Act
    boolean actualMatchesClassNameResult = typeReplacement.matchesClassName("Class Name");

    // Assert
    StringMatcher stringMatcher = typeReplacement.classNameMatcher;
    assertTrue(stringMatcher instanceof VariableStringMatcher);
    assertEquals("Class Name", ((VariableStringMatcher) stringMatcher).getMatchingString());
    assertTrue(actualMatchesClassNameResult);
  }

  /**
   * Test TypeReplacement {@link TypeReplacement#matchesClassName(String)}.
   *
   * <ul>
   *   <li>Then return {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link TypeReplacement#matchesClassName(String)}
   */
  @Test
  @DisplayName("Test TypeReplacement matchesClassName(String); then return 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean TypeReplacement.matchesClassName(String)"})
  void testTypeReplacementMatchesClassName_thenReturnFalse() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act and Assert
    assertFalse(
        abstractAPIConverter.new TypeReplacement("Matching Class Name", "Replacement Class Name")
            .matchesClassName("Class Name"));
  }

  /**
   * Test TypeReplacement {@link TypeReplacement#TypeReplacement(AbstractAPIConverter, String,
   * String)}.
   *
   * <p>Method under test: {@link TypeReplacement#TypeReplacement(AbstractAPIConverter, String,
   * String)}
   */
  @Test
  @DisplayName("Test TypeReplacement new TypeReplacement(AbstractAPIConverter, String, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TypeReplacement.<init>(AbstractAPIConverter, String, String)"})
  void testTypeReplacementNewTypeReplacement() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    TypeReplacement actualTypeReplacement =
        abstractAPIConverter.new TypeReplacement("Matching Class Name", "Replacement Class Name");

    // Assert
    assertTrue(actualTypeReplacement.classNameMatcher instanceof FixedStringMatcher);
    assertEquals("Matching Class Name", actualTypeReplacement.matchingClassName);
    assertEquals("Replacement Class Name", actualTypeReplacement.replacementClassName);
  }

  /**
   * Test TypeReplacement {@link TypeReplacement#TypeReplacement(AbstractAPIConverter, String,
   * String)}.
   *
   * <p>Method under test: {@link TypeReplacement#TypeReplacement(AbstractAPIConverter, String,
   * String)}
   */
  @Test
  @DisplayName("Test TypeReplacement new TypeReplacement(AbstractAPIConverter, String, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void TypeReplacement.<init>(AbstractAPIConverter, String, String)"})
  void testTypeReplacementNewTypeReplacement2() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    // Act
    TypeReplacement actualTypeReplacement =
        abstractAPIConverter.new TypeReplacement("*", "Replacement Class Name");

    // Assert
    assertTrue(actualTypeReplacement.classNameMatcher instanceof VariableStringMatcher);
    assertEquals("*", actualTypeReplacement.matchingClassName);
    assertEquals("Replacement Class Name", actualTypeReplacement.replacementClassName);
  }

  /**
   * Test TypeReplacement {@link TypeReplacement#replaceClassName(Clazz, String)}.
   *
   * <p>Method under test: {@link TypeReplacement#replaceClassName(Clazz, String)}
   */
  @Test
  @DisplayName("Test TypeReplacement replaceClassName(Clazz, String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"String TypeReplacement.replaceClassName(Clazz, String)"})
  void testTypeReplacementReplaceClassName() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    TypeReplacement typeReplacement =
        abstractAPIConverter.new TypeReplacement("Matching Class Name", "Replacement Class Name");

    // Act and Assert
    assertEquals(
        "Replacement Class Name",
        typeReplacement.replaceClassName(new LibraryClass(), "Class Name"));
  }

  /**
   * Test {@link AbstractAPIConverter#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link PrintWriter#PrintWriter(Writer)} with {@link StringWriter#StringWriter()}.
   *   <li>Then calls {@link ProgramClass#attributesAccept(AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given PrintWriter(Writer) with StringWriter(); then calls attributesAccept(AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenPrintWriterWithStringWriter_thenCallsAttributesAccept() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    doNothing().when(programClass).attributesAccept(Mockito.<AttributeVisitor>any());
    doNothing().when(programClass).constantPoolEntriesAccept(Mockito.<ConstantVisitor>any());
    doNothing().when(programClass).fieldsAccept(Mockito.<MemberVisitor>any());
    doNothing().when(programClass).methodsAccept(Mockito.<MemberVisitor>any());

    // Act
    abstractAPIConverter.visitProgramClass(programClass);

    // Assert
    verify(programClass).attributesAccept(isA(AttributeVisitor.class));
    verify(programClass).constantPoolEntriesAccept(isA(ConstantVisitor.class));
    verify(programClass).fieldsAccept(isA(MemberVisitor.class));
    verify(programClass, atLeast(1)).methodsAccept(Mockito.<MemberVisitor>any());
  }

  /**
   * Test {@link AbstractAPIConverter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName("Test visitProgramField(ProgramClass, ProgramField)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    abstractAPIConverter.visitProgramField(programClass, new NamedField("String", "String"));

    // Assert
    verify(programClass).getString(0);
  }

  /**
   * Test {@link AbstractAPIConverter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Given {@link PrintWriter#PrintWriter(Writer)} with {@link StringWriter#StringWriter()}.
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); given PrintWriter(Writer) with StringWriter(); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_givenPrintWriterWithStringWriter_thenCallsGetString() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    abstractAPIConverter.visitProgramField(programClass, new ProgramField());

    // Assert
    verify(programClass).getString(0);
  }

  /**
   * Test {@link AbstractAPIConverter#visitProgramField(ProgramClass, ProgramField)}.
   *
   * <ul>
   *   <li>Then calls {@link AnnotationDefaultAttribute#accept(Clazz, Field, AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitProgramField(ProgramClass,
   * ProgramField)}
   */
  @Test
  @DisplayName(
      "Test visitProgramField(ProgramClass, ProgramField); then calls accept(Clazz, Field, AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitProgramField(ProgramClass, ProgramField)"})
  void testVisitProgramField_thenCallsAccept() {
    // Arrange
    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    AnnotationDefaultAttribute annotationDefaultAttribute = mock(AnnotationDefaultAttribute.class);
    doNothing()
        .when(annotationDefaultAttribute)
        .accept(Mockito.<Clazz>any(), Mockito.<Field>any(), Mockito.<AttributeVisitor>any());
    Attribute[] attributes = new Attribute[] {annotationDefaultAttribute};
    ProgramField programField = new ProgramField(1, 1, 1, 1, attributes, new LibraryClass());

    // Act
    abstractAPIConverter.visitProgramField(programClass, programField);

    // Assert
    verify(programClass).getString(1);
    verify(annotationDefaultAttribute)
        .accept(isA(Clazz.class), isA(Field.class), isA(AttributeVisitor.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName("Test visitProgramMethod(ProgramClass, ProgramMethod)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    abstractAPIConverter.visitProgramMethod(programClass, new NamedMember("String", "String"));

    // Assert
    verify(programClass).getString(0);
  }

  /**
   * Test {@link AbstractAPIConverter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Given {@link PrintWriter#PrintWriter(Writer)} with {@link StringWriter#StringWriter()}.
   *   <li>Then calls {@link ProgramClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); given PrintWriter(Writer) with StringWriter(); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_givenPrintWriterWithStringWriter_thenCallsGetString() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    // Act
    abstractAPIConverter.visitProgramMethod(programClass, new ProgramMethod());

    // Assert
    verify(programClass).getString(0);
  }

  /**
   * Test {@link AbstractAPIConverter#visitProgramMethod(ProgramClass, ProgramMethod)}.
   *
   * <ul>
   *   <li>Then calls {@link AnnotationDefaultAttribute#accept(Clazz, Method, AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitProgramMethod(ProgramClass,
   * ProgramMethod)}
   */
  @Test
  @DisplayName(
      "Test visitProgramMethod(ProgramClass, ProgramMethod); then calls accept(Clazz, Method, AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitProgramMethod(ProgramClass, ProgramMethod)"})
  void testVisitProgramMethod_thenCallsAccept() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getString(anyInt())).thenReturn("String");

    AnnotationDefaultAttribute annotationDefaultAttribute = mock(AnnotationDefaultAttribute.class);
    doNothing()
        .when(annotationDefaultAttribute)
        .accept(Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());
    Attribute[] attributes = new Attribute[] {annotationDefaultAttribute};
    Clazz[] referencedClasses = new Clazz[] {new LibraryClass()};

    ProgramMethod programMethod = new ProgramMethod(1, 1, 1, 1, attributes, referencedClasses);

    // Act
    abstractAPIConverter.visitProgramMethod(programClass, programMethod);

    // Assert
    verify(programClass).getString(1);
    verify(annotationDefaultAttribute)
        .accept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitCodeAttribute(Clazz, Method, CodeAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link CodeAttribute#attributesAccept(Clazz, Method, AttributeVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitCodeAttribute(Clazz, Method,
   * CodeAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitCodeAttribute(Clazz, Method, CodeAttribute); then calls attributesAccept(Clazz, Method, AttributeVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitCodeAttribute(Clazz, Method, CodeAttribute)"})
  void testVisitCodeAttribute_thenCallsAttributesAccept() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    CodeAttribute codeAttribute = mock(CodeAttribute.class);
    doNothing()
        .when(codeAttribute)
        .attributesAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AttributeVisitor>any());
    doNothing()
        .when(codeAttribute)
        .instructionsAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<InstructionVisitor>any());

    // Act
    abstractAPIConverter.visitCodeAttribute(clazz, method, codeAttribute);

    // Assert
    verify(codeAttribute)
        .attributesAccept(isA(Clazz.class), isA(Method.class), isA(AttributeVisitor.class));
    verify(codeAttribute)
        .instructionsAccept(isA(Clazz.class), isA(Method.class), isA(InstructionVisitor.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute,
   * LocalVariableTableAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link LocalVariableTableAttribute#localVariablesAccept(Clazz, Method,
   *       CodeAttribute, LocalVariableInfoVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitLocalVariableTableAttribute(Clazz,
   * Method, CodeAttribute, LocalVariableTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute); then calls localVariablesAccept(Clazz, Method, CodeAttribute, LocalVariableInfoVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitLocalVariableTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTableAttribute)"
  })
  void testVisitLocalVariableTableAttribute_thenCallsLocalVariablesAccept() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    LocalVariableTableAttribute localVariableTableAttribute =
        mock(LocalVariableTableAttribute.class);
    doNothing()
        .when(localVariableTableAttribute)
        .localVariablesAccept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            Mockito.<LocalVariableInfoVisitor>any());

    // Act
    abstractAPIConverter.visitLocalVariableTableAttribute(
        clazz, method, codeAttribute, localVariableTableAttribute);

    // Assert
    verify(localVariableTableAttribute)
        .localVariablesAccept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            isA(LocalVariableInfoVisitor.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitLocalVariableTypeTableAttribute(Clazz, Method,
   * CodeAttribute, LocalVariableTypeTableAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link LocalVariableTypeTableAttribute#localVariablesAccept(Clazz, Method,
   *       CodeAttribute, LocalVariableTypeInfoVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitLocalVariableTypeTableAttribute(Clazz,
   * Method, CodeAttribute, LocalVariableTypeTableAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute); then calls localVariablesAccept(Clazz, Method, CodeAttribute, LocalVariableTypeInfoVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitLocalVariableTypeTableAttribute(Clazz, Method, CodeAttribute, LocalVariableTypeTableAttribute)"
  })
  void testVisitLocalVariableTypeTableAttribute_thenCallsLocalVariablesAccept() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    LocalVariableTypeTableAttribute localVariableTypeTableAttribute =
        mock(LocalVariableTypeTableAttribute.class);
    doNothing()
        .when(localVariableTypeTableAttribute)
        .localVariablesAccept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            Mockito.<LocalVariableTypeInfoVisitor>any());

    // Act
    abstractAPIConverter.visitLocalVariableTypeTableAttribute(
        clazz, method, codeAttribute, localVariableTypeTableAttribute);

    // Assert
    verify(localVariableTypeTableAttribute)
        .localVariablesAccept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            isA(LocalVariableTypeInfoVisitor.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitSignatureAttribute(Clazz, SignatureAttribute)} with
   * {@code clazz}, {@code signatureAttribute}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitSignatureAttribute(Clazz,
   * SignatureAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitSignatureAttribute(Clazz, SignatureAttribute) with 'clazz', 'signatureAttribute'; then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitSignatureAttribute(Clazz, SignatureAttribute)"
  })
  void testVisitSignatureAttributeWithClazzSignatureAttribute_thenCallsGetString() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");

    // Act
    abstractAPIConverter.visitSignatureAttribute(clazz, new SignatureAttribute());

    // Assert
    verify(clazz).getString(0);
  }

  /**
   * Test {@link AbstractAPIConverter#visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link RuntimeInvisibleAnnotationsAttribute#annotationsAccept(Clazz,
   *       AnnotationVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitAnyAnnotationsAttribute(Clazz,
   * AnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute); then calls annotationsAccept(Clazz, AnnotationVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitAnyAnnotationsAttribute(Clazz, AnnotationsAttribute)"
  })
  void testVisitAnyAnnotationsAttribute_thenCallsAnnotationsAccept() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    LibraryClass clazz = new LibraryClass();

    RuntimeInvisibleAnnotationsAttribute annotationsAttribute =
        mock(RuntimeInvisibleAnnotationsAttribute.class);
    doNothing()
        .when(annotationsAttribute)
        .annotationsAccept(Mockito.<Clazz>any(), Mockito.<AnnotationVisitor>any());

    // Act
    abstractAPIConverter.visitAnyAnnotationsAttribute(clazz, annotationsAttribute);

    // Assert
    verify(annotationsAttribute).annotationsAccept(isA(Clazz.class), isA(AnnotationVisitor.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitAnyParameterAnnotationsAttribute(Clazz, Method,
   * ParameterAnnotationsAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link RuntimeInvisibleParameterAnnotationsAttribute#annotationsAccept(Clazz,
   *       Method, AnnotationVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitAnyParameterAnnotationsAttribute(Clazz,
   * Method, ParameterAnnotationsAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute); then calls annotationsAccept(Clazz, Method, AnnotationVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitAnyParameterAnnotationsAttribute(Clazz, Method, ParameterAnnotationsAttribute)"
  })
  void testVisitAnyParameterAnnotationsAttribute_thenCallsAnnotationsAccept() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    RuntimeInvisibleParameterAnnotationsAttribute parameterAnnotationsAttribute =
        mock(RuntimeInvisibleParameterAnnotationsAttribute.class);
    doNothing()
        .when(parameterAnnotationsAttribute)
        .annotationsAccept(
            Mockito.<Clazz>any(), Mockito.<Method>any(), Mockito.<AnnotationVisitor>any());

    // Act
    abstractAPIConverter.visitAnyParameterAnnotationsAttribute(
        clazz, method, parameterAnnotationsAttribute);

    // Assert
    verify(parameterAnnotationsAttribute)
        .annotationsAccept(isA(Clazz.class), isA(Method.class), isA(AnnotationVisitor.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitAnnotationDefaultAttribute(Clazz, Method,
   * AnnotationDefaultAttribute)}.
   *
   * <ul>
   *   <li>Then calls {@link AnnotationDefaultAttribute#defaultValueAccept(Clazz,
   *       ElementValueVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitAnnotationDefaultAttribute(Clazz,
   * Method, AnnotationDefaultAttribute)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotationDefaultAttribute(Clazz, Method, AnnotationDefaultAttribute); then calls defaultValueAccept(Clazz, ElementValueVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitAnnotationDefaultAttribute(Clazz, Method, AnnotationDefaultAttribute)"
  })
  void testVisitAnnotationDefaultAttribute_thenCallsDefaultValueAccept() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();

    AnnotationDefaultAttribute annotationDefaultAttribute = mock(AnnotationDefaultAttribute.class);
    doNothing()
        .when(annotationDefaultAttribute)
        .defaultValueAccept(Mockito.<Clazz>any(), Mockito.<ElementValueVisitor>any());

    // Act
    abstractAPIConverter.visitAnnotationDefaultAttribute(clazz, method, annotationDefaultAttribute);

    // Assert
    verify(annotationDefaultAttribute)
        .defaultValueAccept(isA(Clazz.class), isA(ElementValueVisitor.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitLocalVariableInfo(Clazz, Method, CodeAttribute,
   * LocalVariableInfo)}.
   *
   * <ul>
   *   <li>Given {@link PrintWriter#PrintWriter(Writer)} with {@link StringWriter#StringWriter()}.
   *   <li>Then calls {@link LibraryClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitLocalVariableInfo(Clazz, Method,
   * CodeAttribute, LocalVariableInfo)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo); given PrintWriter(Writer) with StringWriter(); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitLocalVariableInfo(Clazz, Method, CodeAttribute, LocalVariableInfo)"
  })
  void testVisitLocalVariableInfo_givenPrintWriterWithStringWriter_thenCallsGetString() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    abstractAPIConverter.visitLocalVariableInfo(
        clazz, method, codeAttribute, new LocalVariableInfo());

    // Assert
    verify(clazz).getString(0);
  }

  /**
   * Test {@link AbstractAPIConverter#visitLocalVariableTypeInfo(Clazz, Method, CodeAttribute,
   * LocalVariableTypeInfo)}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitLocalVariableTypeInfo(Clazz, Method,
   * CodeAttribute, LocalVariableTypeInfo)}
   */
  @Test
  @DisplayName(
      "Test visitLocalVariableTypeInfo(Clazz, Method, CodeAttribute, LocalVariableTypeInfo); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitLocalVariableTypeInfo(Clazz, Method, CodeAttribute, LocalVariableTypeInfo)"
  })
  void testVisitLocalVariableTypeInfo_thenCallsGetString() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    abstractAPIConverter.visitLocalVariableTypeInfo(
        clazz, method, codeAttribute, new LocalVariableTypeInfo());

    // Assert
    verify(clazz).getString(0);
  }

  /**
   * Test {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName("Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation() {
    // Arrange
    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");
    AnnotationElementValue annotationElementValue = new AnnotationElementValue(1, new Annotation());

    // Act
    abstractAPIConverter.visitAnnotation(
        clazz,
        new Annotation(
            1, 1, new ElementValue[] {annotationElementValue, new AnnotationElementValue()}));

    // Assert
    verify(clazz, atLeast(1)).getString(anyInt());
  }

  /**
   * Test {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName("Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation2() {
    // Arrange
    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");

    ArrayElementValue arrayElementValue = new ArrayElementValue();
    arrayElementValue.addProcessingFlags(2, 1, 2, 1);

    // Act
    abstractAPIConverter.visitAnnotation(
        clazz,
        new Annotation(1, 1, new ElementValue[] {arrayElementValue, new AnnotationElementValue()}));

    // Assert
    verify(clazz).getString(1);
  }

  /**
   * Test {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName("Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation3() {
    // Arrange
    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");

    ConstantElementValue constantElementValue = new ConstantElementValue('\u0001');
    constantElementValue.addProcessingFlags(2, 1, 2, 1);

    // Act
    abstractAPIConverter.visitAnnotation(
        clazz,
        new Annotation(
            1, 1, new ElementValue[] {constantElementValue, new AnnotationElementValue()}));

    // Assert
    verify(clazz).getString(1);
  }

  /**
   * Test {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName("Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation4() {
    // Arrange
    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");

    EnumConstantElementValue enumConstantElementValue = new EnumConstantElementValue();
    enumConstantElementValue.addProcessingFlags(2, 1, 2, 1);

    // Act
    abstractAPIConverter.visitAnnotation(
        clazz,
        new Annotation(
            1, 1, new ElementValue[] {enumConstantElementValue, new AnnotationElementValue()}));

    // Assert
    verify(clazz, atLeast(1)).getString(anyInt());
  }

  /**
   * Test {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <ul>
   *   <li>Given {@link PrintWriter#PrintWriter(Writer)} with {@link StringWriter#StringWriter()}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'; given PrintWriter(Writer) with StringWriter()")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation_givenPrintWriterWithStringWriter() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");

    // Act
    abstractAPIConverter.visitAnnotation(clazz, new Annotation());

    // Assert
    verify(clazz).getString(0);
  }

  /**
   * Test {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <ul>
   *   <li>Then calls {@link AnnotationElementValue#accept(Clazz, Annotation, ElementValueVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'; then calls accept(Clazz, Annotation, ElementValueVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation_thenCallsAccept() {
    // Arrange
    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");

    AnnotationElementValue annotationElementValue = mock(AnnotationElementValue.class);
    doNothing()
        .when(annotationElementValue)
        .accept(
            Mockito.<Clazz>any(), Mockito.<Annotation>any(), Mockito.<ElementValueVisitor>any());
    ElementValue[] elementValues = new ElementValue[] {annotationElementValue};

    // Act
    abstractAPIConverter.visitAnnotation(clazz, new Annotation(1, 1, elementValues));

    // Assert
    verify(clazz).getString(1);
    verify(annotationElementValue)
        .accept(isA(Clazz.class), isA(Annotation.class), isA(ElementValueVisitor.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)} with {@code clazz}, {@code
   * annotation}.
   *
   * <ul>
   *   <li>Then calls {@link Annotation#elementValuesAccept(Clazz, ElementValueVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitAnnotation(Clazz, Annotation)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotation(Clazz, Annotation) with 'clazz', 'annotation'; then calls elementValuesAccept(Clazz, ElementValueVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitAnnotation(Clazz, Annotation)"})
  void testVisitAnnotationWithClazzAnnotation_thenCallsElementValuesAccept() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");

    Annotation annotation = mock(Annotation.class);
    doNothing()
        .when(annotation)
        .elementValuesAccept(Mockito.<Clazz>any(), Mockito.<ElementValueVisitor>any());

    // Act
    abstractAPIConverter.visitAnnotation(clazz, annotation);

    // Assert
    verify(clazz).getString(0);
    verify(annotation).elementValuesAccept(isA(Clazz.class), isA(ElementValueVisitor.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitEnumConstantElementValue(Clazz, Annotation,
   * EnumConstantElementValue)}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitEnumConstantElementValue(Clazz,
   * Annotation, EnumConstantElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitEnumConstantElementValue(Clazz, Annotation, EnumConstantElementValue); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitEnumConstantElementValue(Clazz, Annotation, EnumConstantElementValue)"
  })
  void testVisitEnumConstantElementValue_thenCallsGetString() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getString(anyInt())).thenReturn("String");
    Annotation annotation = new Annotation();

    // Act
    abstractAPIConverter.visitEnumConstantElementValue(
        clazz, annotation, new EnumConstantElementValue());

    // Assert
    verify(clazz).getString(0);
  }

  /**
   * Test {@link AbstractAPIConverter#visitClassElementValue(Clazz, Annotation, ClassElementValue)}.
   *
   * <ul>
   *   <li>Given {@code Class Name}.
   *   <li>When {@link LibraryClass}.
   *   <li>Then calls {@link ClassElementValue#getClassName(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitClassElementValue(Clazz, Annotation,
   * ClassElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitClassElementValue(Clazz, Annotation, ClassElementValue); given 'Class Name'; when LibraryClass; then calls getClassName(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitClassElementValue(Clazz, Annotation, ClassElementValue)"
  })
  void testVisitClassElementValue_givenClassName_whenLibraryClass_thenCallsGetClassName() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    JSR310Converter jsr310Converter =
        new JSR310Converter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    LibraryClass libraryClass = mock(LibraryClass.class);
    Annotation annotation = new Annotation();

    ClassElementValue classElementValue = mock(ClassElementValue.class);
    when(classElementValue.getClassName(Mockito.<Clazz>any())).thenReturn("Class Name");

    // Act
    jsr310Converter.visitClassElementValue(libraryClass, annotation, classElementValue);

    // Assert
    verify(classElementValue).getClassName(isA(Clazz.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitClassElementValue(Clazz, Annotation, ClassElementValue)}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitClassElementValue(Clazz, Annotation,
   * ClassElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitClassElementValue(Clazz, Annotation, ClassElementValue); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitClassElementValue(Clazz, Annotation, ClassElementValue)"
  })
  void testVisitClassElementValue_thenCallsGetString() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    JSR310Converter jsr310Converter =
        new JSR310Converter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    LibraryClass libraryClass = mock(LibraryClass.class);
    when(libraryClass.getString(anyInt())).thenReturn("String");
    Annotation annotation = new Annotation();

    // Act
    jsr310Converter.visitClassElementValue(libraryClass, annotation, new ClassElementValue());

    // Assert
    verify(libraryClass).getString(0);
  }

  /**
   * Test {@link AbstractAPIConverter#visitAnnotationElementValue(Clazz, Annotation,
   * AnnotationElementValue)}.
   *
   * <ul>
   *   <li>Then calls {@link AnnotationElementValue#annotationAccept(Clazz, AnnotationVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitAnnotationElementValue(Clazz,
   * Annotation, AnnotationElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue); then calls annotationAccept(Clazz, AnnotationVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitAnnotationElementValue(Clazz, Annotation, AnnotationElementValue)"
  })
  void testVisitAnnotationElementValue_thenCallsAnnotationAccept() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();

    AnnotationElementValue annotationElementValue = mock(AnnotationElementValue.class);
    doNothing()
        .when(annotationElementValue)
        .annotationAccept(Mockito.<Clazz>any(), Mockito.<AnnotationVisitor>any());

    // Act
    abstractAPIConverter.visitAnnotationElementValue(clazz, annotation, annotationElementValue);

    // Assert
    verify(annotationElementValue).annotationAccept(isA(Clazz.class), isA(AnnotationVisitor.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitArrayElementValue(Clazz, Annotation, ArrayElementValue)}.
   *
   * <ul>
   *   <li>Then calls {@link AnnotationElementValue#accept(Clazz, Annotation, ElementValueVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitArrayElementValue(Clazz, Annotation,
   * ArrayElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitArrayElementValue(Clazz, Annotation, ArrayElementValue); then calls accept(Clazz, Annotation, ElementValueVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitArrayElementValue(Clazz, Annotation, ArrayElementValue)"
  })
  void testVisitArrayElementValue_thenCallsAccept() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();

    AnnotationElementValue annotationElementValue = mock(AnnotationElementValue.class);
    doNothing()
        .when(annotationElementValue)
        .accept(
            Mockito.<Clazz>any(), Mockito.<Annotation>any(), Mockito.<ElementValueVisitor>any());
    ElementValue[] elementValues = new ElementValue[] {annotationElementValue};

    // Act
    abstractAPIConverter.visitArrayElementValue(
        clazz, annotation, new ArrayElementValue(1, 1, elementValues));

    // Assert
    verify(annotationElementValue)
        .accept(isA(Clazz.class), isA(Annotation.class), isA(ElementValueVisitor.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitArrayElementValue(Clazz, Annotation, ArrayElementValue)}.
   *
   * <ul>
   *   <li>Then calls {@link ArrayElementValue#elementValuesAccept(Clazz, Annotation,
   *       ElementValueVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitArrayElementValue(Clazz, Annotation,
   * ArrayElementValue)}
   */
  @Test
  @DisplayName(
      "Test visitArrayElementValue(Clazz, Annotation, ArrayElementValue); then calls elementValuesAccept(Clazz, Annotation, ElementValueVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitArrayElementValue(Clazz, Annotation, ArrayElementValue)"
  })
  void testVisitArrayElementValue_thenCallsElementValuesAccept() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    LibraryClass clazz = new LibraryClass();
    Annotation annotation = new Annotation();

    ArrayElementValue arrayElementValue = mock(ArrayElementValue.class);
    doNothing()
        .when(arrayElementValue)
        .elementValuesAccept(
            Mockito.<Clazz>any(), Mockito.<Annotation>any(), Mockito.<ElementValueVisitor>any());

    // Act
    abstractAPIConverter.visitArrayElementValue(clazz, annotation, arrayElementValue);

    // Assert
    verify(arrayElementValue)
        .elementValuesAccept(
            isA(Clazz.class), isA(Annotation.class), isA(ElementValueVisitor.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Given {@code Name}.
   *   <li>When {@link LibraryClass}.
   *   <li>Then calls {@link ClassConstant#getName(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName(
      "Test visitClassConstant(Clazz, ClassConstant); given 'Name'; when LibraryClass; then calls getName(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_givenName_whenLibraryClass_thenCallsGetName() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    JSR310Converter jsr310Converter =
        new JSR310Converter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    LibraryClass libraryClass = mock(LibraryClass.class);

    ClassConstant classConstant = mock(ClassConstant.class);
    when(classConstant.getName(Mockito.<Clazz>any())).thenReturn("Name");

    // Act
    jsr310Converter.visitClassConstant(libraryClass, classConstant);

    // Assert
    verify(classConstant).getName(isA(Clazz.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitClassConstant(Clazz, ClassConstant)}.
   *
   * <ul>
   *   <li>Then calls {@link LibraryClass#getString(int)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitClassConstant(Clazz, ClassConstant)}
   */
  @Test
  @DisplayName("Test visitClassConstant(Clazz, ClassConstant); then calls getString(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitClassConstant(Clazz, ClassConstant)"})
  void testVisitClassConstant_thenCallsGetString() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    JSR310Converter jsr310Converter =
        new JSR310Converter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);

    LibraryClass libraryClass = mock(LibraryClass.class);
    when(libraryClass.getString(anyInt())).thenReturn("String");

    // Act
    jsr310Converter.visitClassConstant(libraryClass, new ClassConstant());

    // Assert
    verify(libraryClass).getString(0);
  }

  /**
   * Test {@link AbstractAPIConverter#visitFieldrefConstant(Clazz, FieldrefConstant)}.
   *
   * <ul>
   *   <li>Given {@link PrintWriter#PrintWriter(Writer)} with {@link StringWriter#StringWriter()}.
   *   <li>Then calls {@link FieldrefConstant#getName(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitFieldrefConstant(Clazz,
   * FieldrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitFieldrefConstant(Clazz, FieldrefConstant); given PrintWriter(Writer) with StringWriter(); then calls getName(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitFieldrefConstant(Clazz, FieldrefConstant)"})
  void testVisitFieldrefConstant_givenPrintWriterWithStringWriter_thenCallsGetName() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    AbstractAPIConverter abstractAPIConverter =
        new AbstractAPIConverter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    LibraryClass clazz = mock(LibraryClass.class);

    FieldrefConstant fieldrefConstant = mock(FieldrefConstant.class);
    when(fieldrefConstant.getName(Mockito.<Clazz>any())).thenReturn("Name");
    when(fieldrefConstant.getType(Mockito.<Clazz>any())).thenReturn("foo");

    // Act
    abstractAPIConverter.visitFieldrefConstant(clazz, fieldrefConstant);

    // Assert
    verify(fieldrefConstant).getName(isA(Clazz.class));
    verify(fieldrefConstant).getType(isA(Clazz.class));
  }

  /**
   * Test {@link AbstractAPIConverter#visitFieldrefConstant(Clazz, FieldrefConstant)}.
   *
   * <ul>
   *   <li>When {@link LibraryClass} {@link LibraryClass#getType(int)} return {@code foo}.
   *   <li>Then calls {@link LibraryClass#getName(int)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitFieldrefConstant(Clazz,
   * FieldrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitFieldrefConstant(Clazz, FieldrefConstant); when LibraryClass getType(int) return 'foo'; then calls getName(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void AbstractAPIConverter.visitFieldrefConstant(Clazz, FieldrefConstant)"})
  void testVisitFieldrefConstant_whenLibraryClassGetTypeReturnFoo_thenCallsGetName() {
    // Arrange
    LibraryClass clazz = mock(LibraryClass.class);
    when(clazz.getName(anyInt())).thenReturn("Name");
    when(clazz.getType(anyInt())).thenReturn("foo");

    // Act
    abstractAPIConverter.visitFieldrefConstant(clazz, new FieldrefConstant());

    // Assert
    verify(clazz).getName(0);
    verify(clazz).getType(0);
  }

  /**
   * Test {@link AbstractAPIConverter#visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)}.
   *
   * <ul>
   *   <li>Given {@code foo}.
   *   <li>When {@link LibraryClass}.
   *   <li>Then calls {@link InterfaceMethodrefConstant#getClassName(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link AbstractAPIConverter#visitAnyMethodrefConstant(Clazz,
   * AnyMethodrefConstant)}
   */
  @Test
  @DisplayName(
      "Test visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant); given 'foo'; when LibraryClass; then calls getClassName(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void AbstractAPIConverter.visitAnyMethodrefConstant(Clazz, AnyMethodrefConstant)"
  })
  void testVisitAnyMethodrefConstant_givenFoo_whenLibraryClass_thenCallsGetClassName() {
    // Arrange
    WarningPrinter warningPrinter = new WarningPrinter(new PrintWriter(new StringWriter()));
    ClassVisitor modifiedClassVisitor = mock(ClassVisitor.class);

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

    JSR310Converter jsr310Converter =
        new JSR310Converter(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            warningPrinter,
            modifiedClassVisitor,
            extraInstructionVisitor);
    LibraryClass libraryClass = mock(LibraryClass.class);

    InterfaceMethodrefConstant interfaceMethodrefConstant = mock(InterfaceMethodrefConstant.class);
    when(interfaceMethodrefConstant.getClassName(Mockito.<Clazz>any())).thenReturn("Class Name");
    when(interfaceMethodrefConstant.getName(Mockito.<Clazz>any())).thenReturn("Name");
    when(interfaceMethodrefConstant.getType(Mockito.<Clazz>any())).thenReturn("foo");

    // Act
    jsr310Converter.visitAnyMethodrefConstant(libraryClass, interfaceMethodrefConstant);

    // Assert
    verify(interfaceMethodrefConstant).getClassName(isA(Clazz.class));
    verify(interfaceMethodrefConstant, atLeast(1)).getName(isA(Clazz.class));
    verify(interfaceMethodrefConstant, atLeast(1)).getType(isA(Clazz.class));
  }
}
