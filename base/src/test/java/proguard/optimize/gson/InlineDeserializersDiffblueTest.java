package proguard.optimize.gson;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.diffblue.cover.annotations.ManagedByDiffblue;
import com.diffblue.cover.annotations.MethodsUnderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.Field;
import proguard.classfile.LibraryClass;
import proguard.classfile.ProgramClass;
import proguard.classfile.ProgramField;
import proguard.classfile.editor.CompactCodeAttributeComposer;
import proguard.classfile.editor.CompactCodeAttributeComposer.Label;
import proguard.classfile.kotlin.KotlinConstants;
import proguard.optimize.gson.InlineDeserializers.InlinePrimitiveIntegerDeserializer;
import proguard.optimize.gson.InlineDeserializers.InlineStringDeserializer;

class InlineDeserializersDiffblueTest {
  /**
   * Test InlinePrimitiveIntegerDeserializer {@link
   * InlinePrimitiveIntegerDeserializer#canDeserialize(GsonRuntimeSettings)}.
   *
   * <p>Method under test: {@link
   * InlinePrimitiveIntegerDeserializer#canDeserialize(GsonRuntimeSettings)}
   */
  @Test
  @DisplayName("Test InlinePrimitiveIntegerDeserializer canDeserialize(GsonRuntimeSettings)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "boolean InlinePrimitiveIntegerDeserializer.canDeserialize(GsonRuntimeSettings)"
  })
  void testInlinePrimitiveIntegerDeserializerCanDeserialize() {
    // Arrange
    Class<Object> targetType = Object.class;
    InlinePrimitiveIntegerDeserializer inlinePrimitiveIntegerDeserializer =
        new InlinePrimitiveIntegerDeserializer(targetType);

    ClassPool classPool = KotlinConstants.dummyClassPool;
    classPool.addClass("java/lang/Integer", new LibraryClass());
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
    gsonRuntimeSettings.typeAdapterClassPool = classPool;

    // Act and Assert
    assertFalse(inlinePrimitiveIntegerDeserializer.canDeserialize(gsonRuntimeSettings));
  }

  /**
   * Test InlinePrimitiveIntegerDeserializer {@link
   * InlinePrimitiveIntegerDeserializer#deserialize(ProgramClass, ProgramField,
   * CompactCodeAttributeComposer, GsonRuntimeSettings)}.
   *
   * <ul>
   *   <li>Then calls {@link CompactCodeAttributeComposer#aload(int)}.
   * </ul>
   *
   * <p>Method under test: {@link InlinePrimitiveIntegerDeserializer#deserialize(ProgramClass,
   * ProgramField, CompactCodeAttributeComposer, GsonRuntimeSettings)}
   */
  @Test
  @DisplayName(
      "Test InlinePrimitiveIntegerDeserializer deserialize(ProgramClass, ProgramField, CompactCodeAttributeComposer, GsonRuntimeSettings); then calls aload(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void InlinePrimitiveIntegerDeserializer.deserialize(ProgramClass, ProgramField, CompactCodeAttributeComposer, GsonRuntimeSettings)"
  })
  void testInlinePrimitiveIntegerDeserializerDeserialize_thenCallsAload() {
    // Arrange
    Class<Object> targetType = Object.class;
    InlinePrimitiveIntegerDeserializer inlinePrimitiveIntegerDeserializer =
        new InlinePrimitiveIntegerDeserializer(targetType);
    ProgramClass programClass = new ProgramClass();
    ProgramField programField = new ProgramField();

    CompactCodeAttributeComposer compactCodeAttributeComposer =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer.invokevirtual(
            Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
        .thenReturn(new CompactCodeAttributeComposer(new ProgramClass()));

    CompactCodeAttributeComposer compactCodeAttributeComposer2 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer2.aload(anyInt())).thenReturn(compactCodeAttributeComposer);

    CompactCodeAttributeComposer compactCodeAttributeComposer3 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer3.aload(anyInt())).thenReturn(compactCodeAttributeComposer2);

    CompactCodeAttributeComposer compactCodeAttributeComposer4 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer4.label(Mockito.<Label>any()))
        .thenReturn(new CompactCodeAttributeComposer(new ProgramClass()));

    CompactCodeAttributeComposer compactCodeAttributeComposer5 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer5.goto_(Mockito.<Label>any()))
        .thenReturn(compactCodeAttributeComposer4);

    CompactCodeAttributeComposer compactCodeAttributeComposer6 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer6.label(Mockito.<Label>any()))
        .thenReturn(new CompactCodeAttributeComposer(new ProgramClass()));

    CompactCodeAttributeComposer compactCodeAttributeComposer7 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer7.athrow()).thenReturn(compactCodeAttributeComposer6);

    CompactCodeAttributeComposer compactCodeAttributeComposer8 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer8.invokespecial(
            Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
        .thenReturn(compactCodeAttributeComposer7);

    CompactCodeAttributeComposer compactCodeAttributeComposer9 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer9.aload(anyInt())).thenReturn(compactCodeAttributeComposer8);

    CompactCodeAttributeComposer compactCodeAttributeComposer10 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer10.dup()).thenReturn(compactCodeAttributeComposer9);

    CompactCodeAttributeComposer compactCodeAttributeComposer11 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer11.new_(Mockito.<String>any()))
        .thenReturn(compactCodeAttributeComposer10);

    CompactCodeAttributeComposer compactCodeAttributeComposer12 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer12.astore(anyInt()))
        .thenReturn(compactCodeAttributeComposer11);

    CompactCodeAttributeComposer ____ = mock(CompactCodeAttributeComposer.class);
    when(____.catch_(
            Mockito.<Label>any(),
            Mockito.<Label>any(),
            Mockito.<String>any(),
            Mockito.<Clazz>any()))
        .thenReturn(compactCodeAttributeComposer12);
    when(____.putfield(Mockito.<Clazz>any(), Mockito.<Field>any()))
        .thenReturn(compactCodeAttributeComposer5);
    when(____.label(Mockito.<Label>any())).thenReturn(compactCodeAttributeComposer3);
    when(____.createLabel()).thenReturn(mock(Label.class));
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

    // Act
    inlinePrimitiveIntegerDeserializer.deserialize(
        programClass, programField, ____, gsonRuntimeSettings);

    // Assert
    verify(compactCodeAttributeComposer3).aload(0);
    verify(compactCodeAttributeComposer2).aload(2);
    verify(compactCodeAttributeComposer9).aload(4);
    verify(compactCodeAttributeComposer12).astore(4);
    verify(compactCodeAttributeComposer7).athrow();
    verify(____)
        .catch_(
            isA(Label.class), isA(Label.class), eq("java/lang/NumberFormatException"), isNull());
    verify(____, atLeast(1)).createLabel();
    verify(compactCodeAttributeComposer10).dup();
    verify(compactCodeAttributeComposer5).goto_(isA(Label.class));
    verify(compactCodeAttributeComposer8)
        .invokespecial("com/google/gson/JsonSyntaxException", "<init>", "(Ljava/lang/Throwable;)V");
    verify(compactCodeAttributeComposer)
        .invokevirtual("com/google/gson/stream/JsonReader", "nextInt", "()I");
    verify(____).label(isA(Label.class));
    verify(compactCodeAttributeComposer6).label(isA(Label.class));
    verify(compactCodeAttributeComposer4).label(isA(Label.class));
    verify(compactCodeAttributeComposer11).new_("com/google/gson/JsonSyntaxException");
    verify(____).putfield(isA(Clazz.class), isA(Field.class));
  }

  /**
   * Test InlineStringDeserializer {@link
   * InlineStringDeserializer#canDeserialize(GsonRuntimeSettings)}.
   *
   * <p>Method under test: {@link InlineStringDeserializer#canDeserialize(GsonRuntimeSettings)}
   */
  @Test
  @DisplayName("Test InlineStringDeserializer canDeserialize(GsonRuntimeSettings)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({"boolean InlineStringDeserializer.canDeserialize(GsonRuntimeSettings)"})
  void testInlineStringDeserializerCanDeserialize() {
    // Arrange
    InlineStringDeserializer inlineStringDeserializer = new InlineStringDeserializer();

    ClassPool classPool = KotlinConstants.dummyClassPool;
    classPool.addClass("java/lang/String", new LibraryClass());
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
    gsonRuntimeSettings.typeAdapterClassPool = classPool;

    // Act and Assert
    assertFalse(inlineStringDeserializer.canDeserialize(gsonRuntimeSettings));
  }

  /**
   * Test InlineStringDeserializer {@link InlineStringDeserializer#deserialize(ProgramClass,
   * ProgramField, CompactCodeAttributeComposer, GsonRuntimeSettings)}.
   *
   * <ul>
   *   <li>Then calls {@link CompactCodeAttributeComposer#aload(int)}.
   * </ul>
   *
   * <p>Method under test: {@link InlineStringDeserializer#deserialize(ProgramClass, ProgramField,
   * CompactCodeAttributeComposer, GsonRuntimeSettings)}
   */
  @Test
  @DisplayName(
      "Test InlineStringDeserializer deserialize(ProgramClass, ProgramField, CompactCodeAttributeComposer, GsonRuntimeSettings); then calls aload(int)")
  @Tag("ContributionFromDiffblue")
  @ManagedByDiffblue
  @MethodsUnderTest({
    "void InlineStringDeserializer.deserialize(ProgramClass, ProgramField, CompactCodeAttributeComposer, GsonRuntimeSettings)"
  })
  void testInlineStringDeserializerDeserialize_thenCallsAload() {
    // Arrange
    InlineStringDeserializer inlineStringDeserializer = new InlineStringDeserializer();
    ProgramClass programClass = mock(ProgramClass.class);
    ProgramField programField = mock(ProgramField.class);

    CompactCodeAttributeComposer compactCodeAttributeComposer =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer.ifacmpeq(Mockito.<Label>any()))
        .thenReturn(new CompactCodeAttributeComposer(new ProgramClass()));

    CompactCodeAttributeComposer compactCodeAttributeComposer2 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer2.getstatic(
            Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
        .thenReturn(compactCodeAttributeComposer);

    CompactCodeAttributeComposer compactCodeAttributeComposer3 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer3.goto_(Mockito.<Label>any()))
        .thenReturn(new CompactCodeAttributeComposer(new ProgramClass()));

    CompactCodeAttributeComposer compactCodeAttributeComposer4 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer4.putfield(Mockito.<Clazz>any(), Mockito.<Field>any()))
        .thenReturn(compactCodeAttributeComposer3);

    CompactCodeAttributeComposer compactCodeAttributeComposer5 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer5.invokevirtual(
            Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
        .thenReturn(compactCodeAttributeComposer4);

    CompactCodeAttributeComposer compactCodeAttributeComposer6 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer6.aload(anyInt())).thenReturn(compactCodeAttributeComposer5);
    when(compactCodeAttributeComposer6.invokevirtual(
            Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
        .thenReturn(compactCodeAttributeComposer2);

    CompactCodeAttributeComposer compactCodeAttributeComposer7 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer7.label(Mockito.<Label>any()))
        .thenReturn(new CompactCodeAttributeComposer(new ProgramClass()));

    CompactCodeAttributeComposer compactCodeAttributeComposer8 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer8.putfield(Mockito.<Clazz>any(), Mockito.<Field>any()))
        .thenReturn(compactCodeAttributeComposer7);

    CompactCodeAttributeComposer compactCodeAttributeComposer9 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer9.invokestatic(
            Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
        .thenReturn(compactCodeAttributeComposer8);

    CompactCodeAttributeComposer compactCodeAttributeComposer10 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer10.invokevirtual(
            Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any()))
        .thenReturn(compactCodeAttributeComposer9);

    CompactCodeAttributeComposer compactCodeAttributeComposer11 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer11.aload(anyInt())).thenReturn(compactCodeAttributeComposer10);

    CompactCodeAttributeComposer compactCodeAttributeComposer12 =
        mock(CompactCodeAttributeComposer.class);
    when(compactCodeAttributeComposer12.aload(anyInt())).thenReturn(compactCodeAttributeComposer11);

    CompactCodeAttributeComposer ____ = mock(CompactCodeAttributeComposer.class);
    when(____.label(Mockito.<Label>any())).thenReturn(compactCodeAttributeComposer12);
    when(____.aload(anyInt())).thenReturn(compactCodeAttributeComposer6);
    when(____.createLabel()).thenReturn(mock(Label.class));
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

    // Act
    inlineStringDeserializer.deserialize(programClass, programField, ____, gsonRuntimeSettings);

    // Assert
    verify(____, atLeast(1)).aload(anyInt());
    verify(compactCodeAttributeComposer12).aload(0);
    verify(compactCodeAttributeComposer11).aload(2);
    verify(compactCodeAttributeComposer6).aload(2);
    verify(____, atLeast(1)).createLabel();
    verify(compactCodeAttributeComposer2)
        .getstatic(
            "com/google/gson/stream/JsonToken", "BOOLEAN", "Lcom/google/gson/stream/JsonToken;");
    verify(compactCodeAttributeComposer3).goto_(isA(Label.class));
    verify(compactCodeAttributeComposer).ifacmpeq(isA(Label.class));
    verify(compactCodeAttributeComposer9)
        .invokestatic("java/lang/Boolean", "toString", "(Z)Ljava/lang/String;");
    verify(compactCodeAttributeComposer10)
        .invokevirtual("com/google/gson/stream/JsonReader", "nextBoolean", "()Z");
    verify(compactCodeAttributeComposer5)
        .invokevirtual("com/google/gson/stream/JsonReader", "nextString", "()Ljava/lang/String;");
    verify(compactCodeAttributeComposer6)
        .invokevirtual(
            "com/google/gson/stream/JsonReader", "peek", "()Lcom/google/gson/stream/JsonToken;");
    verify(____).label(isA(Label.class));
    verify(compactCodeAttributeComposer7).label(isA(Label.class));
    verify(compactCodeAttributeComposer8).putfield(isA(Clazz.class), isA(Field.class));
    verify(compactCodeAttributeComposer4).putfield(isA(Clazz.class), isA(Field.class));
  }
}
