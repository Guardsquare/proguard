package proguard.optimize.gson;

import static org.mockito.ArgumentMatchers.isA;
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
import proguard.classfile.Clazz;
import proguard.classfile.LibraryClass;
import proguard.classfile.LibraryMethod;
import proguard.classfile.Method;
import proguard.classfile.attribute.CodeAttribute;
import proguard.classfile.editor.CodeAttributeEditor;
import proguard.classfile.editor.CodeAttributeEditor.Label;
import proguard.classfile.instruction.Instruction;
import proguard.classfile.kotlin.KotlinConstants;

class OptimizedTypeAdapterFactoryInitializerDiffblueTest {
  /**
   * Test {@link OptimizedTypeAdapterFactoryInitializer#visitAnyInstruction(Clazz, Method,
   * CodeAttribute, int, Instruction)}.
   *
   * <ul>
   *   <li>Given {@code Name}.
   *   <li>Then calls {@link LibraryMethod#getName(Clazz)}.
   * </ul>
   *
   * <p>Method under test: {@link OptimizedTypeAdapterFactoryInitializer#visitAnyInstruction(Clazz,
   * Method, CodeAttribute, int, Instruction)}
   */
  @Test
  @DisplayName(
      "Test visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction); given 'Name'; then calls getName(Clazz)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void OptimizedTypeAdapterFactoryInitializer.visitAnyInstruction(Clazz, Method, CodeAttribute, int, Instruction)"
  })
  void testVisitAnyInstruction_givenName_thenCallsGetName() {
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
    CodeAttributeEditor codeAttributeEditor = new CodeAttributeEditor();

    OptimizedTypeAdapterFactoryInitializer optimizedTypeAdapterFactoryInitializer =
        new OptimizedTypeAdapterFactoryInitializer(
            KotlinConstants.dummyClassPool,
            codeAttributeEditor,
            new HashMap<>(),
            gsonRuntimeSettings);
    LibraryClass clazz = new LibraryClass();

    LibraryMethod method = mock(LibraryMethod.class);
    when(method.getName(Mockito.<Clazz>any())).thenReturn("Name");
    CodeAttribute codeAttribute = new CodeAttribute();

    // Act
    optimizedTypeAdapterFactoryInitializer.visitAnyInstruction(
        clazz, method, codeAttribute, 2, new Label(1));

    // Assert
    verify(method).getName(isA(Clazz.class));
  }
}
