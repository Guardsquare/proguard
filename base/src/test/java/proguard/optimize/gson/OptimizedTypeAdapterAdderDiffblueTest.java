package proguard.optimize.gson;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.io.ExtraDataEntryNameMap;

class OptimizedTypeAdapterAdderDiffblueTest {
  /**
   * Test {@link OptimizedTypeAdapterAdder#visitProgramClass(ProgramClass)}.
   *
   * <p>Method under test: {@link OptimizedTypeAdapterAdder#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName("Test visitProgramClass(ProgramClass)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizedTypeAdapterAdder.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass() {
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
    ClassPool programClassPool = new ClassPool();
    CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
    OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
    OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
    ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();

    OptimizedTypeAdapterAdder optimizedTypeAdapterAdder =
        new OptimizedTypeAdapterAdder(
            programClassPool,
            KotlinConstants.dummyClassPool,
            codeAttributeEditor,
            serializationInfo,
            deserializationInfo,
            extraDataEntryNameMap,
            new HashMap<>(),
            gsonRuntimeSettings);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getAccessFlags()).thenReturn(1);
    when(programClass.getName()).thenReturn("Name");

    // Act
    optimizedTypeAdapterAdder.visitProgramClass(programClass);

    // Assert
    verify(programClass).getAccessFlags();
    verify(programClass, atLeast(1)).getName();
  }

  /**
   * Test {@link OptimizedTypeAdapterAdder#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassPool} {@link ClassPool#getClass(String)} return {@link
   *       LibraryClass#LibraryClass()}.
   *   <li>Then calls {@link ClassPool#getClass(String)}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizedTypeAdapterAdder#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassPool getClass(String) return LibraryClass(); then calls getClass(String)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizedTypeAdapterAdder.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassPoolGetClassReturnLibraryClass_thenCallsGetClass() {
    // Arrange
    ClassPool programClassPool = mock(ClassPool.class);
    when(programClassPool.getClass(Mockito.<String>any())).thenReturn(new LibraryClass());
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
    CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
    OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
    OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
    ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();

    OptimizedTypeAdapterAdder optimizedTypeAdapterAdder =
        new OptimizedTypeAdapterAdder(
            programClassPool,
            KotlinConstants.dummyClassPool,
            codeAttributeEditor,
            serializationInfo,
            deserializationInfo,
            extraDataEntryNameMap,
            new HashMap<>(),
            gsonRuntimeSettings);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getName()).thenReturn("Name");

    // Act
    optimizedTypeAdapterAdder.visitProgramClass(programClass);

    // Assert
    verify(programClassPool).getClass("/OptimizedNameTypeAdapter");
    verify(programClass).getName();
  }

  /**
   * Test {@link OptimizedTypeAdapterAdder#visitProgramClass(ProgramClass)}.
   *
   * <ul>
   *   <li>Given {@link ClassPool} {@link ClassPool#getClass(String)} return {@code null}.
   *   <li>Then calls {@link ClassPool#addClass(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizedTypeAdapterAdder#visitProgramClass(ProgramClass)}
   */
  @Test
  @DisplayName(
      "Test visitProgramClass(ProgramClass); given ClassPool getClass(String) return 'null'; then calls addClass(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"void OptimizedTypeAdapterAdder.visitProgramClass(ProgramClass)"})
  void testVisitProgramClass_givenClassPoolGetClassReturnNull_thenCallsAddClass() {
    // Arrange
    ClassPool programClassPool = mock(ClassPool.class);
    when(programClassPool.getClass(Mockito.<String>any())).thenReturn(null);
    doNothing().when(programClassPool).addClass(Mockito.<Clazz>any());
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
    CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();
    OptimizedJsonInfo serializationInfo = new OptimizedJsonInfo();
    OptimizedJsonInfo deserializationInfo = new OptimizedJsonInfo();
    ExtraDataEntryNameMap extraDataEntryNameMap = new ExtraDataEntryNameMap();

    OptimizedTypeAdapterAdder optimizedTypeAdapterAdder =
        new OptimizedTypeAdapterAdder(
            programClassPool,
            KotlinConstants.dummyClassPool,
            codeAttributeEditor,
            serializationInfo,
            deserializationInfo,
            extraDataEntryNameMap,
            new HashMap<>(),
            gsonRuntimeSettings);

    ProgramClass programClass = mock(ProgramClass.class);
    when(programClass.getAccessFlags()).thenReturn(1);
    when(programClass.getName()).thenReturn("Name");

    // Act
    optimizedTypeAdapterAdder.visitProgramClass(programClass);

    // Assert
    verify(programClassPool).addClass(isA(Clazz.class));
    verify(programClassPool, atLeast(1)).getClass(Mockito.<String>any());
    verify(programClass).getAccessFlags();
    verify(programClass, atLeast(1)).getName();
  }
}
