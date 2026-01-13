package proguard.optimize.gson;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.instruction.BranchInstruction;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.instruction.visitor.InstructionVisitor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.classfile.visitor.ClassVisitor;

class GsonBuilderInvocationFinderDiffblueTest {
  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName("Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = false;
    gsonRuntimeSettings.addSerializationExclusionStrategy = true;
    gsonRuntimeSettings.disableInnerClassSerialization = true;
    gsonRuntimeSettings.excludeFieldsWithModifiers = true;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
    gsonRuntimeSettings.generateNonExecutableJson = true;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = true;
    gsonRuntimeSettings.serializeNulls = true;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
    gsonRuntimeSettings.setExclusionStrategies = true;
    gsonRuntimeSettings.setFieldNamingPolicy = true;
    gsonRuntimeSettings.setFieldNamingStrategy = true;
    gsonRuntimeSettings.setLongSerializationPolicy = true;
    gsonRuntimeSettings.setVersion = true;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }

  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName("Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction2() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
    gsonRuntimeSettings.addSerializationExclusionStrategy = false;
    gsonRuntimeSettings.disableInnerClassSerialization = true;
    gsonRuntimeSettings.excludeFieldsWithModifiers = true;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
    gsonRuntimeSettings.generateNonExecutableJson = true;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = true;
    gsonRuntimeSettings.serializeNulls = true;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
    gsonRuntimeSettings.setExclusionStrategies = true;
    gsonRuntimeSettings.setFieldNamingPolicy = true;
    gsonRuntimeSettings.setFieldNamingStrategy = true;
    gsonRuntimeSettings.setLongSerializationPolicy = true;
    gsonRuntimeSettings.setVersion = true;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }

  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName("Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction3() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
    gsonRuntimeSettings.addSerializationExclusionStrategy = true;
    gsonRuntimeSettings.disableInnerClassSerialization = false;
    gsonRuntimeSettings.excludeFieldsWithModifiers = true;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
    gsonRuntimeSettings.generateNonExecutableJson = true;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = true;
    gsonRuntimeSettings.serializeNulls = true;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
    gsonRuntimeSettings.setExclusionStrategies = true;
    gsonRuntimeSettings.setFieldNamingPolicy = true;
    gsonRuntimeSettings.setFieldNamingStrategy = true;
    gsonRuntimeSettings.setLongSerializationPolicy = true;
    gsonRuntimeSettings.setVersion = true;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }

  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName("Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction4() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
    gsonRuntimeSettings.addSerializationExclusionStrategy = true;
    gsonRuntimeSettings.disableInnerClassSerialization = true;
    gsonRuntimeSettings.excludeFieldsWithModifiers = false;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
    gsonRuntimeSettings.generateNonExecutableJson = true;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = true;
    gsonRuntimeSettings.serializeNulls = true;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
    gsonRuntimeSettings.setExclusionStrategies = true;
    gsonRuntimeSettings.setFieldNamingPolicy = true;
    gsonRuntimeSettings.setFieldNamingStrategy = true;
    gsonRuntimeSettings.setLongSerializationPolicy = true;
    gsonRuntimeSettings.setVersion = true;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }

  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName("Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction5() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
    gsonRuntimeSettings.addSerializationExclusionStrategy = true;
    gsonRuntimeSettings.disableInnerClassSerialization = true;
    gsonRuntimeSettings.excludeFieldsWithModifiers = true;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = false;
    gsonRuntimeSettings.generateNonExecutableJson = true;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = true;
    gsonRuntimeSettings.serializeNulls = true;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
    gsonRuntimeSettings.setExclusionStrategies = true;
    gsonRuntimeSettings.setFieldNamingPolicy = true;
    gsonRuntimeSettings.setFieldNamingStrategy = true;
    gsonRuntimeSettings.setLongSerializationPolicy = true;
    gsonRuntimeSettings.setVersion = true;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }

  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName("Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction6() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
    gsonRuntimeSettings.addSerializationExclusionStrategy = true;
    gsonRuntimeSettings.disableInnerClassSerialization = true;
    gsonRuntimeSettings.excludeFieldsWithModifiers = true;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
    gsonRuntimeSettings.generateNonExecutableJson = true;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = false;
    gsonRuntimeSettings.serializeNulls = true;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
    gsonRuntimeSettings.setExclusionStrategies = true;
    gsonRuntimeSettings.setFieldNamingPolicy = true;
    gsonRuntimeSettings.setFieldNamingStrategy = true;
    gsonRuntimeSettings.setLongSerializationPolicy = true;
    gsonRuntimeSettings.setVersion = true;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }

  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName("Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction7() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
    gsonRuntimeSettings.addSerializationExclusionStrategy = true;
    gsonRuntimeSettings.disableInnerClassSerialization = true;
    gsonRuntimeSettings.excludeFieldsWithModifiers = true;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
    gsonRuntimeSettings.generateNonExecutableJson = true;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = true;
    gsonRuntimeSettings.serializeNulls = true;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = false;
    gsonRuntimeSettings.setExclusionStrategies = true;
    gsonRuntimeSettings.setFieldNamingPolicy = true;
    gsonRuntimeSettings.setFieldNamingStrategy = true;
    gsonRuntimeSettings.setLongSerializationPolicy = true;
    gsonRuntimeSettings.setVersion = true;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }

  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName("Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction8() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
    gsonRuntimeSettings.addSerializationExclusionStrategy = true;
    gsonRuntimeSettings.disableInnerClassSerialization = true;
    gsonRuntimeSettings.excludeFieldsWithModifiers = true;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
    gsonRuntimeSettings.generateNonExecutableJson = true;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = true;
    gsonRuntimeSettings.serializeNulls = true;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
    gsonRuntimeSettings.setExclusionStrategies = true;
    gsonRuntimeSettings.setFieldNamingPolicy = true;
    gsonRuntimeSettings.setFieldNamingStrategy = true;
    gsonRuntimeSettings.setLongSerializationPolicy = false;
    gsonRuntimeSettings.setVersion = true;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }

  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>Given {@link GsonRuntimeSettings} (default constructor) {@link
   *       GsonRuntimeSettings#generateNonExecutableJson} is {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction); given GsonRuntimeSettings (default constructor) generateNonExecutableJson is 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction_givenGsonRuntimeSettingsGenerateNonExecutableJsonIsFalse() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
    gsonRuntimeSettings.addSerializationExclusionStrategy = true;
    gsonRuntimeSettings.disableInnerClassSerialization = true;
    gsonRuntimeSettings.excludeFieldsWithModifiers = true;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
    gsonRuntimeSettings.generateNonExecutableJson = false;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = true;
    gsonRuntimeSettings.serializeNulls = true;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
    gsonRuntimeSettings.setExclusionStrategies = true;
    gsonRuntimeSettings.setFieldNamingPolicy = true;
    gsonRuntimeSettings.setFieldNamingStrategy = true;
    gsonRuntimeSettings.setLongSerializationPolicy = true;
    gsonRuntimeSettings.setVersion = true;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }

  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>Given {@link GsonRuntimeSettings} (default constructor) {@link
   *       GsonRuntimeSettings#serializeNulls} is {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction); given GsonRuntimeSettings (default constructor) serializeNulls is 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction_givenGsonRuntimeSettingsSerializeNullsIsFalse() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
    gsonRuntimeSettings.addSerializationExclusionStrategy = true;
    gsonRuntimeSettings.disableInnerClassSerialization = true;
    gsonRuntimeSettings.excludeFieldsWithModifiers = true;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
    gsonRuntimeSettings.generateNonExecutableJson = true;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = true;
    gsonRuntimeSettings.serializeNulls = false;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
    gsonRuntimeSettings.setExclusionStrategies = true;
    gsonRuntimeSettings.setFieldNamingPolicy = true;
    gsonRuntimeSettings.setFieldNamingStrategy = true;
    gsonRuntimeSettings.setLongSerializationPolicy = true;
    gsonRuntimeSettings.setVersion = true;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }

  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>Given {@link GsonRuntimeSettings} (default constructor) {@link
   *       GsonRuntimeSettings#setExclusionStrategies} is {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction); given GsonRuntimeSettings (default constructor) setExclusionStrategies is 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction_givenGsonRuntimeSettingsSetExclusionStrategiesIsFalse() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
    gsonRuntimeSettings.addSerializationExclusionStrategy = true;
    gsonRuntimeSettings.disableInnerClassSerialization = true;
    gsonRuntimeSettings.excludeFieldsWithModifiers = true;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
    gsonRuntimeSettings.generateNonExecutableJson = true;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = true;
    gsonRuntimeSettings.serializeNulls = true;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
    gsonRuntimeSettings.setExclusionStrategies = false;
    gsonRuntimeSettings.setFieldNamingPolicy = true;
    gsonRuntimeSettings.setFieldNamingStrategy = true;
    gsonRuntimeSettings.setLongSerializationPolicy = true;
    gsonRuntimeSettings.setVersion = true;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }

  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>Given {@link GsonRuntimeSettings} (default constructor) {@link
   *       GsonRuntimeSettings#setFieldNamingPolicy} is {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction); given GsonRuntimeSettings (default constructor) setFieldNamingPolicy is 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction_givenGsonRuntimeSettingsSetFieldNamingPolicyIsFalse() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
    gsonRuntimeSettings.addSerializationExclusionStrategy = true;
    gsonRuntimeSettings.disableInnerClassSerialization = true;
    gsonRuntimeSettings.excludeFieldsWithModifiers = true;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
    gsonRuntimeSettings.generateNonExecutableJson = true;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = true;
    gsonRuntimeSettings.serializeNulls = true;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
    gsonRuntimeSettings.setExclusionStrategies = true;
    gsonRuntimeSettings.setFieldNamingPolicy = false;
    gsonRuntimeSettings.setFieldNamingStrategy = true;
    gsonRuntimeSettings.setLongSerializationPolicy = true;
    gsonRuntimeSettings.setVersion = true;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }

  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>Given {@link GsonRuntimeSettings} (default constructor) {@link
   *       GsonRuntimeSettings#setFieldNamingStrategy} is {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction); given GsonRuntimeSettings (default constructor) setFieldNamingStrategy is 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction_givenGsonRuntimeSettingsSetFieldNamingStrategyIsFalse() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
    gsonRuntimeSettings.addSerializationExclusionStrategy = true;
    gsonRuntimeSettings.disableInnerClassSerialization = true;
    gsonRuntimeSettings.excludeFieldsWithModifiers = true;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
    gsonRuntimeSettings.generateNonExecutableJson = true;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = true;
    gsonRuntimeSettings.serializeNulls = true;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
    gsonRuntimeSettings.setExclusionStrategies = true;
    gsonRuntimeSettings.setFieldNamingPolicy = true;
    gsonRuntimeSettings.setFieldNamingStrategy = false;
    gsonRuntimeSettings.setLongSerializationPolicy = true;
    gsonRuntimeSettings.setVersion = true;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }

  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>Given {@link GsonRuntimeSettings} (default constructor) {@link
   *       GsonRuntimeSettings#setVersion} is {@code false}.
   * </ul>
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction); given GsonRuntimeSettings (default constructor) setVersion is 'false'")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction_givenGsonRuntimeSettingsSetVersionIsFalse() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
    gsonRuntimeSettings.addSerializationExclusionStrategy = true;
    gsonRuntimeSettings.disableInnerClassSerialization = true;
    gsonRuntimeSettings.excludeFieldsWithModifiers = true;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
    gsonRuntimeSettings.generateNonExecutableJson = true;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = true;
    gsonRuntimeSettings.serializeNulls = true;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
    gsonRuntimeSettings.setExclusionStrategies = true;
    gsonRuntimeSettings.setFieldNamingPolicy = true;
    gsonRuntimeSettings.setFieldNamingStrategy = true;
    gsonRuntimeSettings.setLongSerializationPolicy = true;
    gsonRuntimeSettings.setVersion = false;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }

  /**
   * Test {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method, CodeAttribute, int,
   * Instruction)}.
   *
   * <ul>
   *   <li>When {@link BranchInstruction} {@link BranchInstruction#accept(Clazz, Method,
   *       CodeAttribute, int, InstructionVisitor)} does nothing.
   *   <li>Then calls {@link BranchInstruction#accept(Clazz, Method, CodeAttribute, int,
   *       InstructionVisitor)}.
   * </ul>
   *
   * <p>Method under test: {@link GsonBuilderInvocationFinder#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction); when BranchInstruction accept(Clazz, Method, CodeAttribute, int, InstructionVisitor) does nothing; then calls accept(Clazz, Method, CodeAttribute, int, InstructionVisitor)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void GsonBuilderInvocationFinder.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction_whenBranchInstructionAcceptDoesNothing_thenCallsAccept() {
    // Arrange
    GsonRuntimeSettings gsonRuntimeSettings = new GsonRuntimeSettings();
    gsonRuntimeSettings.addDeserializationExclusionStrategy = true;
    gsonRuntimeSettings.addSerializationExclusionStrategy = true;
    gsonRuntimeSettings.disableInnerClassSerialization = true;
    gsonRuntimeSettings.excludeFieldsWithModifiers = true;
    gsonRuntimeSettings.excludeFieldsWithoutExposeAnnotation = true;
    gsonRuntimeSettings.generateNonExecutableJson = true;
    gsonRuntimeSettings.instanceCreatorClassPool = KotlinConstants.dummyClassPool;
    gsonRuntimeSettings.registerTypeAdapterFactory = true;
    gsonRuntimeSettings.serializeNulls = true;
    gsonRuntimeSettings.serializeSpecialFloatingPointValues = true;
    gsonRuntimeSettings.setExclusionStrategies = true;
    gsonRuntimeSettings.setFieldNamingPolicy = true;
    gsonRuntimeSettings.setFieldNamingStrategy = true;
    gsonRuntimeSettings.setLongSerializationPolicy = true;
    gsonRuntimeSettings.setVersion = true;
    gsonRuntimeSettings.typeAdapterClassPool = KotlinConstants.dummyClassPool;
    GsonBuilderInvocationFinder gsonBuilderInvocationFinder =
        new GsonBuilderInvocationFinder(
            KotlinConstants.dummyClassPool,
            KotlinConstants.dummyClassPool,
            gsonRuntimeSettings,
            mock(ClassVisitor.class),
            mock(ClassVisitor.class));
    LibraryClass clazz = new LibraryClass();
    LibraryMethod method = new LibraryMethod();
    CodeAttribute codeAttribute = new CodeAttribute();

    BranchInstruction instruction = mock(BranchInstruction.class);
    doNothing()
        .when(instruction)
        .accept(
            Mockito.<Clazz>any(),
            Mockito.<Method>any(),
            Mockito.<CodeAttribute>any(),
            anyInt(),
            Mockito.<InstructionVisitor>any());

    // Act
    gsonBuilderInvocationFinder.visitAnyInstruction(clazz, method, codeAttribute, 2, instruction);

    // Assert
    verify(instruction, atLeast(1))
        .accept(
            isA(Clazz.class),
            isA(Method.class),
            isA(CodeAttribute.class),
            eq(2),
            Mockito.<InstructionVisitor>any());
  }
}
